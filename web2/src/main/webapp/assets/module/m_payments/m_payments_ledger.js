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

        this._feeTypeNameList  = [];
        this._feeTypeParentNameList  = [];

        this._filterTimeData = {};//时间筛选
        this._filterData = {
            combineCompanyId:null,
            startDate:null,
            endDate:null,
            profitType:null,
            feeType:null,
            projectName:null,
            feeTypeList:[],
            feeTypeParentList:[],
            fromCompanyName:null
        };

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
            that.getFeeType();

            var timeOption = {};
            timeOption.selectTimeCallBack = function (data) {
                console.log(data);
                if(!isNullOrBlank(data.startTime))
                    that._filterData.startDate = data.startTime;

                if(!isNullOrBlank(data.endTime))
                    that._filterData.endDate = data.endTime;


                that.renderLedgerList();
            };
            $(that.element).find('.time-combination').m_filter_timeCombination(timeOption,true);

            var option = {};
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                if(that._selectedOrg==null){
                    that._filterData.combineCompanyId=that._companyList.id;
                }else{
                    that._filterData.combineCompanyId=that._selectedOrg.id;
                }
                that.renderLedgerList();
            };
            option.$renderCallBack = function () {
                that.bindBtnActionClick();
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);
        }
        //渲染台账list
        , renderLedgerList:function () {
            var that = this;

            var option = {};
            option.param = {};
            option.param = that._filterData;

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

                    that.renderFeeTypeFilter();
                    that.renderSubFeeTypeFilter();
                    that.renderProfitTypeFilter();

                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        /**
         * 获取收支分类
         * @param feeTypeParentList feeTypeParentList==null0为一级，feeTypeParentList!=null则查询此子类
         */
        ,getFeeType:function (callBack) {
            var that = this;
            var option = {};
            option.url = restApi.url_getTitleFilter;
            option.postData = {};
            option.postData.feeTypeParentList  = that._filterData.feeTypeParentList;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    that._feeTypeNameList  = response.data.feeTypeNameList;
                    that._feeTypeParentNameList  = response.data.feeTypeParentNameList;
                    if(callBack!=null)
                        callBack();
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //渲染收支类型筛选
        ,renderFeeTypeFilter:function () {
            var that  = this;
            var option = {};
            var newList = [];

            if(that._feeTypeParentNameList!=null && that._feeTypeParentNameList.length>0){
                $.each(that._feeTypeParentNameList,function (i,item) {
                    newList.push({id:item.expTypeValue,name:item.expTypeValue});
                })
            }
            option.selectArr = newList;
            option.selectedArr = that._filterData.feeTypeParentList;
            option.eleId = 'filterFeeType';
            option.selectedCallBack = function (data) {
                console.log(data);
                that._filterData.feeTypeParentList = data;
                that.getFeeType(function () {
                    that.renderSubFeeTypeFilter(1);
                });
                that.renderLedgerList();
            };
            $(that.element).find('#filterFeeType').m_filter_select(option, true);
        }
        /**
         * 渲染收支子类筛选
         */
        ,renderSubFeeTypeFilter:function () {
            var that  = this;
            var option = {};
            var newList = [];

            if(that._feeTypeNameList!=null && that._feeTypeNameList.length>0){
                $.each(that._feeTypeNameList,function (i,item) {
                    var childList = [];
                    if(item.childList!=null && item.childList.length>0){
                        $.each(item.childList,function (subI,subItem) {
                            childList.push({id:item.expTypeValue+'_'+subItem.expTypeValue,name:subItem.expTypeValue});
                        });
                    }
                    newList.push({id:item.expTypeValue,name:item.expTypeValue,childList:childList});
                })
            }
            option.selectArr = newList;
            option.selectedArr = that._filterData.feeTypeList;
            option.eleId = 'filterSubFeeType';
            option.boxStyle = 'min-width:525px;';
            option.dialogWidth = '525';
            option.selectedCallBack = function (data) {
                console.log(data);
                that._filterData.feeTypeList = data;
                that.renderLedgerList();

            };
            $(that.element).find('#filterSubFeeType').m_filter_checkbox_select(option, true);
        }
        //渲染金额类型筛选
        ,renderProfitTypeFilter:function () {
            var that  = this;
            var option = {};
            var newList = [
                {name:'项目收支',id:'3'},
                {name:'非项目收支',id:'4'}
            ];
            option.selectArr = newList;
            option.selectedArr = [];
            if(!isNullOrBlank(that._filterData.profitType))
                option.selectedArr.push(that._filterData.profitType);

            option.eleId = 'filterProfitType';
            option.selectedCallBack = function (data) {
                if(data && data.length>0){
                    that._filterData.profitType = data[0];
                }else{
                    that._filterData.profitType = null;
                }
                that.renderLedgerList();
            };
            $(that.element).find('#filterProfitType').m_filter_select(option, true);
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
                    case 'filterSubFeeType'://收支类型

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
