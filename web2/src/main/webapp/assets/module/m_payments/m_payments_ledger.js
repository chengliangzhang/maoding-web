/**
 * 收支总览－收支明细-台账
 * Created by wrb on 2017/11/30.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_ledger",
        defaults = {
            $contentEle:null,
            $isFirstEnter:false//是否是第一次進來
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._companyList = null;//组织列表
        this._companyListBySelect = null;//筛选组织
        this._selectedOrg = null;//当前组织筛选-选中组织对象
        this._feeTypeList = [];//筛选组织-收支类型
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData();
        }
        //初始化数据并加载模板
        ,initHtmlData:function () {
            var that = this;
            var html = template('m_payments/m_payments_ledger',{});
            $(that.element).html(html);
            var option = {};
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                that.renderLedgerList();
            };
            option.$renderCallBack = function () {
                that.bindSetTime();
                that.bindChoseTime();
                that.bindBtnActionClick();
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);
        }
        //渲染台账list
        , renderLedgerList:function () {
            var that = this;

            var option = {};
            option.param = {};

            if(that._selectedOrg==null){
                option.param.combineCompanyId=that._companyList.id;
            }else{
                option.param.combineCompanyId=that._selectedOrg.id;
            }

            var startDate=$(that.element).find('#ipt_startTime').val();
            var endDate=$(that.element).find('#ipt_endTime').val();
            if(startDate!=''){
                option.param.startDate=startDate;
            }
            if(endDate!=''){
                option.param.endDate=endDate;
            }

            option.param.profitType=$(that.element).find('input[name="profitType"]').val();
            option.param.feeType=$(that.element).find('input[name="feeType"]').val();
            //option.param.associatedOrg=$(that.element).find('input[name="associatedOrg"]').val();
            option.param.projectName=$(that.element).find('input[name="projectName"]').val();
            option.param.feeTypeList = that._feeTypeList;

            //option.param.toCompanyName=$(that.element).find('input[name="toCompany"]').val();
            option.param.fromCompanyName=$(that.element).find('input[name="fromCompany"]').val();

            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-box',
                url: restApi.url_getExpensesDetailLedger,
                params: option.param
            }, function (response) {

                if (response.code == '0') {

                    that._companyListBySelect = response.data.organization;
                    var html = template('m_payments/m_payments_ledger_list',{
                        dataList:response.data.data,
                        summary:response.data.StatisticDetailSummaryDTO
                    });
                    $(that.element).find('.data-list-container').html(html);

                    var startDateStr = response.data.startDateStr;
                    var endDateStr = response.data.endDateStr;
                    if(startDateStr!=null && startDateStr!=''){

                        $(that.element).find('input[name="startTime"]').val(startDateStr);
                    }
                    if(endDateStr!=null && endDateStr!=''){

                        $(that.element).find('input[name="endTime"]').val(endDateStr);
                    }

                    that.filterHover();
                    that.filterActionClick();

                } else {
                    S_dialog.error(response.info);
                }
            });
        }

        //快捷时间
        , bindSetTime: function () {
            var that = this;
            $(that.element).find('a[data-action="setTime"]').click(function () {
                var days = $(this).attr('data-days');
                var endTime = getNowDate();
                var startTime = '';//moment(endTime).subtract(days, 'days').format('YYYY-MM-DD');

                if (endTime != null && endTime.indexOf('-') > -1) {

                    var month = endTime.substring(5, 7) - 0;//当前月份

                    if (days == 30) {//一个月

                        startTime = endTime.substring(0, 8) + '01';

                    } else if (days == 90) {//一季度

                        if (month >= 1 && month <= 3) {//第一季度
                            startTime = endTime.substring(0, 5) + '01-01';
                        } else if (month >= 4 && month <= 6) {//第二季度
                            startTime = endTime.substring(0, 5) + '04-01';
                        } else if (month >= 7 && month <= 9) {//第三季度
                            startTime = endTime.substring(0, 5) + '07-01';
                        } else if (month >= 10 && month <= 12) {//第四季度
                            startTime = endTime.substring(0, 5) + '10-01';
                        }

                    } else if (days == 180) {//半年

                        if (month >= 1 && month <= 6) {//前半年
                            startTime = endTime.substring(0, 5) + '01-01';
                        } else if (month >= 7 && month <= 12) {//后半年
                            startTime = endTime.substring(0, 5) + '07-01';
                        }

                    } else if (days == 360) {//一年

                        startTime = endTime.substring(0, 5) + '01-01';
                    }
                }
                $('#ipt_startTime').val(startTime);
                $('#ipt_endTime').val(endTime);
                that.renderLedgerList();
                $(this).blur();
            });
        }
        //时间绑定事件
        , bindChoseTime:function () {
            var that = this;
            $(that.element).find('input[name="startTime"]').off('click').on('click',function () {

                var endTime = $(that.element).find('input[name="endTime"]').val();
                var onpicked =function(dp){

                    that.renderLedgerList();

                };
                WdatePicker({el:this,maxDate:endTime,onpicked:onpicked})
            });
            $(that.element).find('input[name="endTime"]').off('click').on('click',function () {

                var startTime = $(that.element).find('input[name="startTime"]').val();
                var onpicked =function(dp){

                    that.renderLedgerList();

                };
                WdatePicker({el:this,minDate:startTime,onpicked:onpicked})
            });
            $(that.element).find('i.fa-calendar').off('click').on('click',function () {
                $(this).closest('.input-group').find('input').click();
            });
        }
        //按钮事件绑定
        , bindBtnActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){

                    case 'refreshBtn':
                        that.initHtmlData();
                        return false;
                        break;
                }
            });
        }
        //筛选hover事件
        , filterHover:function () {
            var that =  this;
            $(that.element).find('.data-list-box  th').hover(function () {
                if( $(this).find(' .icon-filter i').hasClass('icon-shaixuan') && !$(this).find(' .icon-filter i').hasClass('fc-v1-blue')){
                    $(this).find(' .icon-filter').show();
                }
            },function () {
                if( $(this).find(' .icon-filter i').hasClass('icon-shaixuan') && !$(this).find(' .icon-filter i').hasClass('fc-v1-blue')) {
                    $(this).find(' .icon-filter').hide();
                }
            });
        }
        //筛选事件
        , filterActionClick:function () {
            var that = this;
            $(that.element).find('a.icon-filter').each(function () {

                var $this = $(this);
                var id = $this.attr('id');
                switch (id){
                    case 'filterProfitType': //报销类型
                    case 'filterFromCompany'://收款组织
                    case 'filterToCompany'://付款组织

                        var currCheckValue = '',selectList = [];
                        if(id=='filterProfitType'){
                            currCheckValue = $(that.element).find('input[name="profitType"]').val();
                            selectList = [
                                {fieldName:'全部',fieldValue:''},
                                /*{fieldName:'收入',fieldValue:'1'},
                                {fieldName:'支出',fieldValue:'2'}*/
                                {fieldName:'项目收支',fieldValue:'3'},
                                {fieldName:'非项目收支',fieldValue:'4'}
                            ]
                        }else if(id=='filterFromCompany'){
                            currCheckValue = $(that.element).find('input[name="fromCompany"]').val();
                            selectList.push({fieldName:'全部',fieldValue:''});
                            if(that._companyListBySelect!=null && that._companyListBySelect.fromCompany!=null && that._companyListBySelect.fromCompany.length>0){
                                $.each(that._companyListBySelect.fromCompany, function (i, item) {
                                    selectList.push({fieldValue: item.companyName, fieldName: item.companyName});
                                });
                            }
                        }else if(id=='filterToCompany'){
                            currCheckValue = $(that.element).find('input[name="toCompany"]').val();
                            selectList.push({fieldName:'全部',fieldValue:''});
                            if(that._companyListBySelect!=null && that._companyListBySelect.toCompany!=null && that._companyListBySelect.toCompany.length>0){
                                $.each(that._companyListBySelect.toCompany, function (i, item) {
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
                                    if(id=='filterProfitType'){
                                        $(that.element).find('input[name="profitType"]').val(val);
                                    }else if(id=='filterFromCompany'){
                                        $(that.element).find('input[name="fromCompany"]').val(val);
                                    }else if(id=='filterToCompany'){
                                        $(that.element).find('input[name="toCompany"]').val(val);
                                    }
                                    that.renderLedgerList();
                                    S_dialog.close($(dialogEle));
                                });
                            });
                        });

                        break;
                    case 'filterProjectName': //项目
                        var txtVal = '',placeholder='';
                        if(id=='filterProjectName'){
                            txtVal = $('input[name="projectName"]').val();
                            placeholder = '请输入项目名称';
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

                                    if(id=='filterProjectName'){
                                        $(that.element).find('input[name="projectName"]').val(val);
                                    }
                                    that.renderLedgerList();

                                    S_dialog.close($(dialogEle));
                                });

                            });
                        });

                        break;
                    case 'filterFeeType'://收支类型

                        if(that._feeTypeList!=null && that._feeTypeList.length>0){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }

                        $this.on('click',function () {
                            var option = {};
                            option.$eleId = 'filterFeeType';
                            option.$feeTypeList = that._feeTypeList;
                            option.$okCallBack = function (data) {
                                that._feeTypeList = data;
                                that.renderLedgerList();
                            };
                            $('body').m_payments_setFields(option);
                            return false;
                        });
                        break;
                }

            });
        }
    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {

            //if (!$.data(this, "plugin_" + pluginName)) {
            $.data(this, "plugin_" +
                pluginName, new Plugin(this, options));
            //}
        });
    };

})(jQuery, window, document);
