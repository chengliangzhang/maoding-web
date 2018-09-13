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
        this._selectedOrg = null;//当前组织筛选-选中组织对象

        this._feeTypeNameList  = [];
        this._feeTypeParentNameList  = [];
        this._fromCompanyList  = [];
        this._toCompanyList  = [];

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
            fromCompanyName:null,
            toCompanyName:null
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

            var timeOption = {};
            timeOption.selectTimeCallBack = function (data) {
                console.log(data);
                if(!isNullOrBlank(data.startTime))
                    that._filterData.startDate = data.startTime;

                if(!isNullOrBlank(data.endTime))
                    that._filterData.endDate = data.endTime;

                that.renderDataList();
            };
            $(that.element).find('.time-combination').m_filter_timeGroup(timeOption,true);

            var option = {};
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                if(that._selectedOrg==null){
                    that._filterData.combineCompanyId=that._companyList.id;
                }else{
                    that._filterData.combineCompanyId=that._selectedOrg.id;
                }
                that.renderDataList(0);
            };
            option.$renderCallBack = function () {
                that.bindBtnActionClick();
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);
        }
        //渲染台账list
        , renderDataList:function (t) {
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


                    that._fromCompanyList = response.data.organization.fromCompany;
                    that._toCompanyList = response.data.organization.toCompany;

                    var html = template('m_payments/m_payments_ledger_list',{
                        dataList:response.data.data,
                        summary:response.data.StatisticDetailSummaryDTO
                    });
                    $(that.element).find('.data-list-container').html(html);

                    if(t==0){
                        that.setTimeInput(response.data.startDateStr,response.data.endDateStr);
                        that.getFeeType(function () {
                            that.filterActionClick();
                        });
                    }else{
                        that.filterActionClick();
                    }


                } else {
                    S_layer.error(response.info);
                }
            });
        }
        //查询list返回时间设置
        ,setTimeInput:function (startDateStr,endDateStr) {
            var that = this;
            var dataType = $(that.element).find('a.btn-primary[data-action="setTime"]').attr('data-type');

            if(dataType=='month'){

                if(!isNullOrBlank(startDateStr))
                    startDateStr = startDateStr.substring(0,7);

                if(!isNullOrBlank(endDateStr)){
                    endDateStr = endDateStr.substring(0,7);
                }

            }else if(dataType=='year'){

                if(!isNullOrBlank(startDateStr))
                    startDateStr = startDateStr.substring(0,4);

                if(!isNullOrBlank(endDateStr)){
                    endDateStr = endDateStr.substring(0,4);
                }
            }
            if(!isNullOrBlank(startDateStr))
                $(that.element).find('input[name="startTime"]').val(startDateStr);

            if(!isNullOrBlank(endDateStr))
                $(that.element).find('input[name="endTime"]').val(endDateStr);
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
            if(that._selectedOrg==null){
                option.postData.combineCompanyId=that._companyList.id;
            }else{
                option.postData.combineCompanyId=that._selectedOrg.id;
            }
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    that._feeTypeNameList  = response.data.feeTypeNameList;
                    that._feeTypeParentNameList  = response.data.feeTypeParentNameList;
                    if(callBack!=null)
                        callBack();
                } else {
                    S_layer.error(response.info);
                }
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
                        that._filterData = {
                            combineCompanyId:null,
                            startDate:null,
                            endDate:null,
                            profitType:null,
                            feeType:null,
                            projectName:null,
                            feeTypeList:[],
                            feeTypeParentList:[],
                            fromCompanyName:null,
                            toCompanyName:null
                        };
                        that.initHtmlData();
                        return false;
                        break;
                    case 'exportDetails'://导出
                        downLoadFile({
                            url:restApi.url_exportBalanceDetail,
                            data:filterParam(that._filterData),
                            type:1
                        });
                        return false;
                        break;
                }
            });
        }
        //筛选事件
        ,filterActionClick:function () {
            var that = this;
            $(that.element).find('a.icon-filter').each(function () {

                var $this = $(this);
                var id = $this.attr('id');
                var filterArr = id.split('_');
                switch (id){
                    case 'filter_profitType':
                    case 'filter_toCompanyName'://收款组织
                    case 'filter_fromCompanyName'://付款组织

                        var newList = [],list=[],selectStr='';

                        if(id=='filter_fromCompanyName'){

                            list = that._fromCompanyList;

                        }else if(id=='filter_toCompanyName'){

                            list = that._toCompanyList;

                        }else if(id=='filter_profitType'){

                            newList = [
                                {name:'项目收支',id:'3'},
                                {name:'非项目收支',id:'4'}
                            ];
                        }
                        selectStr = that._filterData[filterArr[1]];

                        if(list!=null && list.length>0){
                            $.each(list,function (i,item) {
                                newList.push({id:item.companyName,name:item.companyName});
                            })
                        }

                        var option = {};
                        option.selectArr = newList;
                        option.selectedArr = [];
                        if(!isNullOrBlank(selectStr))
                            option.selectedArr.push(selectStr);

                        option.eleId = id;
                        option.selectedCallBack = function (data) {
                            if(data && data.length>0){
                                that._filterData[filterArr[1]] = data[0];
                            }else{
                                that._filterData[filterArr[1]] = null;
                            }
                            that.renderDataList();
                        };
                        $this.m_filter_select(option, true);

                        break;
                    case 'filter_projectName': //项目

                        var option = {};

                        option.inputValue = that._filterData[filterArr[1]];
                        option.eleId = id;
                        option.oKCallBack = function (data) {

                            that._filterData[filterArr[1]] = data;
                            that.renderDataList();
                        };
                        $this.m_filter_input(option, true);

                        break;
                    case 'filter_feeTypeParentList'://收支分类子项

                        var option = {};
                        var newList = [];

                        if(that._feeTypeParentNameList!=null && that._feeTypeParentNameList.length>0){
                            $.each(that._feeTypeParentNameList,function (i,item) {
                                newList.push({id:item.expTypeValue,name:item.expTypeValue});
                            })
                        }
                        option.selectArr = newList;
                        option.selectedArr = that._filterData.feeTypeParentList;
                        option.eleId = id;
                        option.selectedCallBack = function (data) {
                            that._filterData.feeTypeParentList = data;
                            that._filterData.feeTypeList = [];
                            that.getFeeType(function () {
                                that.renderSubFeeTypeFilter();
                            });
                            that.renderDataList();
                        };
                        $this.m_filter_select(option, true);
                        break;

                    case 'filter_feeType'://收支分类子项
                        that.renderSubFeeTypeFilter();
                        break;
                }

            });
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
            option.eleId = 'filter_feeType';
            option.boxStyle = 'min-width:525px;';
            option.dialogWidth = '525';
            option.selectedCallBack = function (data) {
                console.log(data);
                that._filterData.feeTypeList = data;
                that.renderDataList();

            };
            $(that.element).find('#filter_feeType').m_filter_checkbox_select(option, true);
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
