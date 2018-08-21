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
        this._companyListBySelect = null;//筛选组织
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_finance/m_costSummary', {});
            $(that.element).html(html);
            that.getData(0);
        }

        //加载基本数据
        , getData: function (type) {
            var that = this;
            var option = {};
            option.classId = 'mySummaryListBox';
            option.param = {};
            option.param.type = 2;
            if (type === 0) {
                option.param.expName = '';
                option.param.expPName = '';
                option.param.expAllName = '';
                option.param.expType = '';
                option.param.expParentType = '';
                option.param.expNo = '';
            } else {

                option.param.startDate = $('input[name="myExpStartDate"]').val();
                option.param.endDate = $('input[name="myExpEndDate"]').val();
                option.param.expNo = $.trim($('input[name="expNo"]').val());
                //option.param.type = $.trim($('input[name="expType"]').val());

                option.param.approveStartDate = $('input[name="approveStartDate"]').val();
                option.param.approveEndDate = $('input[name="approveEndDate"]').val();

                option.param.applyUserName = $('input[name="approveUserName"]').val();
                option.param.allocationStatus = $('input[name="allocationStatus"]').val();
                option.param.allocationStartDate = $('input[name="allocationStartDate"]').val();
                option.param.allocationEndDate = $('input[name="allocationEndDate"]').val();

                option.param.applyCompanyName = $('input[name="applyCompanyName"]').val();

                option.param.allocationOrder = $('input[name="sortAllocationDate"]').val();
            }
            paginationFun({
                eleId: '#mySummary-pagination-container',
                loadingId: '#mySummaryListData',
                url: restApi.url_getExpMainPageForSummary,
                params: option.param
            }, function (response) {
                // data为ajax返回数据
                if (response.code == '0') {

                    that._companyListBySelect = response.data.companyList;
                    var $data = {};
                    $data.myDataList = response.data.data;
                    $data.expSumAmount = response.data.expSumAmount;
                    $data.financialAllocationSumAmount = response.data.financialAllocationSumAmount;
                    $data.rootPath = window.rootPath;
                    if(type!=1 && !($data.myDataList && $data.myDataList.length>0)){
                        $(that.element).find('#summary .mySummaryFilterBox').html('');
                    }
                    $data.pageIndex=$("#mySummary-pagination-container").pagination('getPageIndex');
                    $data.isFinance = window.currentRoleCodes.indexOf('project_charge_manage')>-1?1:0;
                    var html = template('m_finance/m_costSummary_list', $data);
                    $('#mySummaryListData').html(html);
                    rolesControl();
                    that.bindClickOpenShowExp($data.myDataList);
                    that.bindActionClick();
                    that.filterHover();
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
            $('#mySummaryListData').find('tr[data-action="openShowExp"]').each(function () {
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
        //筛选hover事件
        ,filterHover:function () {
            var that =  this;

            $(that.element).find('.mySummaryListBox  th').hover(function () {
                if( $(this).find(' .icon-filter i').hasClass('icon-shaixuan') && !$(this).find(' .icon-filter i').hasClass('fc-v1-blue')){
                    $(this).find(' .icon-filter').show();
                }
            },function () {
                if( $(this).find(' .icon-filter i').hasClass('icon-shaixuan') && !$(this).find(' .icon-filter i').hasClass('fc-v1-blue')) {
                    $(this).find(' .icon-filter').hide();
                }
            });
        }
        //绑定拨款事件
        , bindActionClick:function () {
            var that = this;
            $('#mySummaryListData').find('a[data-action]').on('click',function (e) {

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
                switch (id){
                    case 'filterApproveUserName'://审批人
                    case 'filterExpNo': //项目编号
                        var txtVal = '',placeholder='';
                        if(id=='filterApproveUserName'){
                            txtVal = $('input[name="approveUserName"]').val();
                            placeholder = '申请人';
                        }else{
                            txtVal = $('input[name="expNo"]').val();
                            placeholder = '报销编号';
                        }

                        if(txtVal!=''){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }
                        $this.on('click',function () {
                            S_dialog.dialog({
                                contentEle: 'dialogOBox',
                                ele:id,
                                lock: 2,
                                align: 'bottom right',
                                quickClose:true,
                                noTriangle:true,
                                width: '220',
                                minHeight:'100',
                                tPadding: '0px',
                                url: rootPath+'/assets/module/m_common/m_dialog.html'


                            },function(d){//加载html后触发

                                var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';


                                var iHtml = template('m_filterableField/m_filter_input',{
                                    txtVal:txtVal,
                                    placeholder:placeholder
                                });

                                $(dialogEle).html(iHtml);
                                $(dialogEle).find('button[data-action="sureFilter"]').on('click',function () {
                                    var val = $(dialogEle).find('input[name="txtVal"]').val();

                                    if(id=='filterApproveUserName'){
                                        $(that.element).find('input[name="approveUserName"]').val(val);
                                    }else{
                                        $(that.element).find('input[name="expNo"]').val(val);
                                    }

                                    that.getData(1);

                                    S_dialog.close($(dialogEle));
                                });

                            });
                        });

                        break;
                    case 'filterApproveDate'://审批时间
                    case 'filterAllocationDate'://拨款时间
                    case 'filterExpDate': //申请时间

                        var startTime = '';
                        var endTime = '';
                        if(id=='filterApproveDate'){
                            startTime = $(that.element).find('input[name="approveStartDate"]').val();
                            endTime = $(that.element).find('input[name="approveEndDate"]').val();
                        }else if(id=='filterAllocationDate'){
                            startTime = $(that.element).find('input[name="allocationStartDate"]').val();
                            endTime = $(that.element).find('input[name="allocationEndDate"]').val();
                        }else{
                            startTime = $(that.element).find('input[name="myExpStartDate"]').val();
                            endTime = $(that.element).find('input[name="myExpEndDate"]').val();
                        }
                        if(startTime!='' || endTime!=''){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }
                        $this.on('click',function () {
                            S_dialog.dialog({
                                contentEle: 'dialogOBox',
                                ele:id,
                                lock: 2,
                                align: 'bottom right',
                                quickClose:true,
                                noTriangle:true,
                                width: '220',
                                minHeight:'100',
                                tPadding: '0px',
                                url: rootPath+'/assets/module/m_common/m_dialog.html'


                            },function(d){//加载html后触发

                                var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';

                                var iHtml = template('m_filterableField/m_filter_time',{
                                    startTime:startTime,
                                    endTime:endTime
                                });

                                $(dialogEle).html(iHtml);
                                $(dialogEle).find('button[data-action="sureTimeFilter"]').off('click').on('click',function () {

                                    var startTime = $(dialogEle).find('input[name="startTime"]').val();
                                    var endTime = $(dialogEle).find('input[name="endTime"]').val();

                                    if(id=='filterApproveDate'){
                                        $(that.element).find('input[name="approveStartDate"]').val(startTime);
                                        $(that.element).find('input[name="approveEndDate"]').val(endTime);
                                    }else if(id=='filterAllocationDate'){
                                        $(that.element).find('input[name="allocationStartDate"]').val(startTime);
                                        $(that.element).find('input[name="allocationEndDate"]').val(endTime);
                                    }else{
                                        $(that.element).find('input[name="myExpStartDate"]').val(startTime);
                                        $(that.element).find('input[name="myExpEndDate"]').val(endTime);
                                    }

                                    that.getData(1);

                                    S_dialog.close($(dialogEle));
                                });
                                $(dialogEle).find('button[data-action="clearTimeInput"]').off('click').on('click',function () {
                                    $(dialogEle).find('input').val('');
                                });
                                $(dialogEle).find('i.fa-calendar').off('click').on('click',function () {
                                    $(this).closest('.input-group').find('input').focus();
                                });

                            });
                        });

                        break;
                    case 'filterApproveStatus': //状态
                    case 'filterAllocationStatus': //拨款情况
                    case 'filterExpType': //报销类型
                    case 'filterTheOrg'://所在组织

                        var currCheckValue = '',selectList = [];
                        if(id=='filterApproveStatus'){
                            currCheckValue = $(that.element).find('input[name="approveStatus"]').val()
                            selectList = [
                                {fieldName:'全部',fieldValue:''},
                                {fieldName:'待审批',fieldValue:'0'},
                                {fieldName:'审批中',fieldValue:'5'},
                                {fieldName:'已完成',fieldValue:'1'},
                                {fieldName:'已退回',fieldValue:'2'}
                            ]
                        }else if(id=='filterAllocationStatus'){
                            currCheckValue = $(that.element).find('input[name="allocationStatus"]').val()
                            selectList = [
                                {fieldName:'全部',fieldValue:''},
                                {fieldName:'未拨款',fieldValue:'0'},
                                {fieldName:'已拨款',fieldValue:'1'}
                            ]
                        }else if(id=='filterExpType'){
                            currCheckValue = $(that.element).find('input[name="expType"]').val()
                            selectList = [
                                {fieldName:'全部',fieldValue:''},
                                {fieldName:'费用申请',fieldValue:'2'},
                                {fieldName:'报销申请',fieldValue:'1'}
                            ]
                        }else if(id=='filterTheOrg'){
                            currCheckValue = $(that.element).find('input[name="applyCompanyName"]').val();
                            selectList.push({fieldName:'全部',fieldValue:''});

                            if(that._companyListBySelect!=null && that._companyListBySelect.length>0){
                                $.each(that._companyListBySelect, function (i, item) {
                                    selectList.push({fieldValue: item.companyName, fieldName: item.companyName});
                                });
                            }
                        }
                        if(currCheckValue!=''){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }

                        var iHtml = template('m_filterableField/m_filter_select',{
                            currCheckValue:currCheckValue,
                            selectList:selectList
                        });
                        var iTextObj = iHtml.getTextWH();
                        var iWHObj = setDialogWH(iTextObj.width,iTextObj.height);

                        $this.on('click',function () {
                            S_dialog.dialog({
                                contentEle: 'dialogOBox',
                                ele:id,
                                lock: 2,
                                align: 'bottom right',
                                quickClose:true,
                                noTriangle:true,
                                width: iWHObj.width,
                                height:iWHObj.height,
                                tPadding: '0px',
                                url: rootPath+'/assets/module/m_common/m_dialog.html'

                            },function(d){//加载html后触发

                                var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';
                                $(dialogEle).html(iHtml);

                                $(dialogEle).find('.dropdown-menu a').on('click',function () {

                                    var val = $(this).attr('data-state-no');
                                    if(id=='filterApproveStatus'){
                                        $(that.element).find('input[name="approveStatus"]').val(val);
                                    }else if(id=='filterAllocationStatus'){
                                        $(that.element).find('input[name="allocationStatus"]').val(val);
                                    }else if(id=='filterExpType'){
                                        $(that.element).find('input[name="expType"]').val(val);
                                    }else if(id=='filterTheOrg'){
                                        $(that.element).find('input[name="applyCompanyName"]').val(val);
                                    }

                                    that.getData(1);
                                    S_dialog.close($(dialogEle));
                                });
                            });
                        });

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
                        var sortAllocationDate = $(that.element).find('input[name="sortAllocationDate"]').val();
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
                                $(that.element).find('input[name="sortAllocationDate"]').val('1');
                            }
                            else if($this.hasClass('sorting_desc')){
                                $(that.element).find('input[name="sortAllocationDate"]').val('0');
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
