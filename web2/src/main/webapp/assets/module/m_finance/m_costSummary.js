/**
 * 费用汇总
 * Created by wrb on 2017/12/25.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_costSummary",
        defaults = {
            expSumFilterData: {}//盛装报销汇总查询条件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._companyList = null;//筛选组织

        /******************** 筛选字段 ********************/
        this._filterData = {
            startDate:null,
            endDate:null,
            expNo:null,
            approveStartDate:null,
            approveEndDate:null,
            applyUserName:null,
            allocationStatus:null,
            allocationStartDate:null,
            allocationEndDate:null,
            applyCompanyName:null,
            allocationOrder:null
        };
        
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_finance/m_costSummary', {});
            $(that.element).html(html);
            that.getData();
        }

        //加载基本数据
        , getData: function () {
            var that = this;
            var option = {};
            option.param = {};
            that._filterData.type=2;
            option.param = filterParam(that._filterData);
            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-container',
                url: restApi.url_getExpMainPageForSummary,
                params: option.param
            }, function (response) {
                // data为ajax返回数据
                if (response.code == '0') {

                    that._companyList = response.data.companyList;
                    var $data = {};
                    $data.myDataList = response.data.data;
                    $data.expSumAmount = response.data.expSumAmount;
                    $data.financialAllocationSumAmount = response.data.financialAllocationSumAmount;
                    $data.rootPath = window.rootPath;
                    $data.pageIndex=$("#data-pagination-container").pagination('getPageIndex');
                    $data.isFinance = window.currentRoleCodes.indexOf('project_charge_manage')>-1?1:0;

                    var html = template('m_finance/m_costSummary_list', $data);
                    $(that.element).find('.data-list-container').html(html);
                    rolesControl();
                    that.bindClickOpenShowExp($data.myDataList);
                    that.bindActionClick();
                    that.sortActionClick();

                    return false;
                } else {
                    S_dialog.error(response.info);
                }
            });

        }
        //打开查看报销详情
        , bindClickOpenShowExp: function (data) {//openShowExp
            var that = this;
            $(that.element).find('.data-list-container tr[data-action="openShowExp"]').each(function () {
                $(this).bind('click', function (event) {
                    var i = $(this).attr('i');
                    var options = {};
                    options.title = '费用详情';
                    options.expDetail = data[i];
                    options.type = 2;
                    $(this).m_showExpDetailDialog(options);
                    event.stopPropagation();
                    return false;
                });
            });
        }
        //绑定拨款事件
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('.data-list-container a[data-action]').on('click',function (e) {

                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'agreeToGrant'://拨款
                        S_dialog.dialog({
                            title: '请选择时间',
                            contentEle: 'dialogOBox',
                            lock: 3,
                            width: '200',
                            height:'50',
                            url: rootPath+'/assets/module/m_common/m_dialog.html',
                            ok:function(){

                                if ($('form.agreeToGrantForm').valid()) {

                                    var financialDate = $('form.agreeToGrantForm input[name="allocationDate"]').val();
                                    //financialDate = moment(financialDate).format('YYYY/MM/DD');
                                    var option  = {};
                                    option.url = restApi.url_financialAllocation;
                                    option.postData = {
                                        id:$this.attr('data-id'),
                                        financialDate:financialDate
                                    };
                                    m_ajax.postJson(option,function (response) {
                                        if(response.code=='0'){
                                            S_toastr.success('操作成功');
                                            //that.init();
                                            $this.parents('td').html(moment(financialDate).format('YYYY/MM/DD'));
                                        }else {
                                            S_dialog.error(response.info);
                                        }
                                    });

                                } else {
                                    return false;
                                }
                            },
                            cancelText:'取消',
                            cancel:function(){
                            }
                        },function(d){//加载html后触发

                            var $dialogEle = $('div[id="content:'+d.id+'"] .dialogOBox');
                            var currDate = getNowDate();
                            $dialogEle.html('<form class="agreeToGrantForm"><div class="form-group text-center col-md-12 "><input class="form-control" type="text" name="allocationDate" onclick="WdatePicker()" value="'+currDate+'" readonly></div></form>');
                            that.saveAgreeToGrant_validate();
                        });
                        e.stopPropagation();
                        return false;
                        break;
                    case 'sendBack'://退回

                        S_dialog.dialog({
                            title: '退回原因',
                            contentEle: 'dialogOBox',
                            lock: 3,
                            width: '300',
                            height:'100',
                            tPadding:'0',
                            url: rootPath+'/assets/module/m_common/m_dialog.html',
                            ok:function(){

                                if ($('form.sendBackForm').valid()) {

                                    var sendBackReason = $('form.sendBackForm textarea[name="sendBackReason"]').val();
                                    var option  = {};
                                    option.url = restApi.url_financialRecallExpMain;
                                    option.postData = {
                                        id:$this.attr('data-id'),
                                        reason:sendBackReason
                                    };
                                    m_ajax.postJson(option,function (response) {
                                        if(response.code=='0'){
                                            S_toastr.success('操作成功');
                                            //that.init();
                                            $this.parents('TR').remove();
                                        }else {
                                            S_dialog.error(response.info);
                                        }
                                    });

                                } else {
                                    return false;
                                }
                            },
                            cancelText:'取消',
                            cancel:function(){
                            }
                        },function(d){//加载html后触发

                            var $dialogEle = $('div[id="content:'+d.id+'"] .dialogOBox');
                            $dialogEle.html('<form class="sendBackForm"><div class=" m-t-md col-md-12 "><textarea class="form-control" name="sendBackReason"></textarea></div></form>');
                            that.saveSendBack_validate();
                        });
                        e.stopPropagation();
                        return false;
                        break;
                }


            });
            //筛选事件
            $(that.element).find('a.icon-filter').each(function () {

                var $this = $(this);
                var id = $this.attr('id');

                var filterArr = id.split('_');

                switch (id){
                    case 'filter_applyUserName'://审批人
                    case 'filter_expNo': //项目编号

                        var option = {};
                        option.inputValue = that._filterData[filterArr[1]];
                        option.eleId = id;
                        option.oKCallBack = function (data) {

                            that._filterData[filterArr[1]] = data;
                            that.getData();
                        };
                        $(that.element).find('#'+id).m_filter_input(option, true);

                        break;
                    case 'filter_allocationStartDate_allocationEndDate'://拨款时间
                    case 'filter_startDate_endDate': //申请时间

                        var timeData = {};
                        timeData.startTime = that._filterData[filterArr[1]];
                        timeData.endTime = that._filterData[filterArr[2]];

                        var option = {};
                        option.timeData = timeData;
                        option.eleId = id;
                        option.okCallBack = function (data) {

                            that._filterData[filterArr[1]] = data.startTime;
                            that._filterData[filterArr[2]] = data.endTime;
                            that.getData();
                        };
                        $(that.element).find('#'+id).m_filter_time(option, true);

                        break;
                    case 'filter_applyCompanyName'://所在组织

                        var selectList = [],selectedArr = [];

                        if(that._companyList!=null && that._companyList.length>0){
                            $.each(that._companyList,function (i,item) {
                                selectList.push({id:item.companyName,name:item.companyName});
                            })
                        }
                        if(!isNullOrBlank(that._filterData[filterArr[1]]))
                            selectedArr.push(that._filterData[filterArr[1]]);

                        var option = {};
                        option.selectArr = selectList;
                        option.selectedArr = selectedArr;
                        option.eleId = id;
                        option.selectedCallBack = function (data) {
                            if(data && data.length>0){
                                that._filterData[filterArr[1]] = data[0];
                            }else{
                                that._filterData[filterArr[1]] = null;
                            }
                            that.getData();
                        };
                        $(that.element).find('#'+id).m_filter_select(option, true);

                        break;
                }

            });
        }
        //排序
        , sortActionClick:function () {
            var that = this;
            $(that.element).find('th[data-action="sort"]').each(function () {

                var $this = $(this);
                var sortType = $this.attr('data-sort-type');

                switch(sortType){
                    case 'allocationDate'://立项时间
                        var sortAllocationDate = that._filterData.allocationOrder;
                        var sortClass = '';
                        if(sortAllocationDate=='0'){
                            sortClass = 'sorting_asc';
                        }else if(sortAllocationDate=='1'){
                            sortClass = 'sorting_desc';
                        }else{
                            sortClass = 'sorting';
                        }
                        $this.removeClass().addClass(sortClass);
                        $this.off('click').on('click',function (e) {
                            if($this.hasClass('sorting')||$this.hasClass('sorting_asc')){
                                that._filterData.allocationOrder = 1;
                            }
                            else if($this.hasClass('sorting_desc')){
                                that._filterData.allocationOrder = 0;
                            }
                            that.getData(1);
                            e.stopPropagation();
                            return false;
                        });
                        break;

                }

            });
        }
        //时间验证
        , saveAgreeToGrant_validate: function () {
            var that = this;
            $('form.agreeToGrantForm').validate({
                rules: {
                    allocationDate: 'required'
                },
                messages: {
                    allocationDate: '请选择时间！'
                }
            });
        }
        //原因不为空验证
        , saveSendBack_validate: function () {
            var that = this;
            $('form.sendBackForm').validate({
                rules: {
                    sendBackReason: 'required'
                },
                messages: {
                    sendBackReason: '请输入退回原因！'
                }
            });
        }
    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            // if (!$.data(this, "plugin_" + pluginName)) {
            $.data(this, "plugin_" +
                pluginName, new Plugin(this, options));
            // }
        });
    };

})(jQuery, window, document);
