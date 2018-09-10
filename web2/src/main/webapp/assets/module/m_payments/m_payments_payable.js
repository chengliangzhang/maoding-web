/**
 * 收支总览－收支明细-应付
 * Created by wrb on 2017/11/30.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_payable",
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
        this._fromCompanyList  = [];//筛选-付款组织
        this._toCompanyList  = [];//筛选-收款组织
        this._feeTypeNameList  = [];//筛选-收支类型

        this.initParam();
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData();
        }
        //初始化参数
        ,initParam:function () {
            var that = this;
            /******************** 筛选字段 ********************/
            that._filterData = {
                startDate:null,
                endDate:null,
                paymentId:null,
                feeType:null,
                feeTypeList:null,
                associatedOrg:null,
                projectName:null
            };
        }
        //初始化数据并加载模板
        ,initHtmlData:function () {
            var that = this;
            var html = template('m_payments/m_payments_payable',{});
            $(that.element).html(html);
            var option = {};
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                that._filterData.paymentId = that._selectedOrg.id;
                that.renderDataList(0);
            };
            option.$renderCallBack = function () {
                that.bindRefreshBtn();
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);


            var timeOption = {};
            timeOption.selectTimeCallBack = function (data) {

                if(!isNullOrBlank(data.startTime))
                    that._filterData.startDate = data.startTime;

                if(!isNullOrBlank(data.endTime))
                    that._filterData.endDate = data.endTime;

                that.renderDataList();
            };
            $(that.element).find('.time-combination').m_filter_timeGroup(timeOption,true);
        }
        //渲染台账list
        ,renderDataList:function (t) {
            var that = this;

            var option = {};
            option.param = {};
            that._filterData.paymentId = that._selectedOrg.id;
            option.param = filterParam(that._filterData);

            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-box',
                url: restApi.url_getPayment,
                params: option.param
            }, function (response) {

                if (response.code == '0') {

                    that._fromCompanyList = response.data.fromCompanyList;
                    that._toCompanyList = response.data.toCompanyList;

                    var html = template('m_payments/m_payments_payable_list',{
                        dataList:response.data.data,
                        paymentSum:response.data.paymentSum
                    });
                    $(that.element).find('.data-list-container').html(html);
                    that.bindViewDetail();
                    that.bindGoExpensesPage();

                    if(t==0){
                        that.getFeeType(function () {
                            that.filterActionClick();
                        });
                    }else{
                        that.filterActionClick();
                    }


                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        , bindRefreshBtn:function () {
            var that = this;
            $(that.element).find('button[data-action="refreshBtn"]').on("click", function (e) {

                that.initParam();
                that.initHtmlData();
                return false;
            })
        }
        //查看详情
        ,bindViewDetail:function () {
            var that = this;
            $(that.element).find('a[data-action="viewDetail"]').on('click',function () {
                var option = {};
                option.$title = '应付详情';
                option.$id = $(this).attr('data-id');
                option.$type = 2;
                $('body').m_payments_list_detail(option);
            });
        }
        //跳转到收支管理
        ,bindGoExpensesPage:function () {
            var that = this;
            $(that.element).find('a[data-action="goExpensesPage"]').off('click').on('click',function () {
                var projectId = $(this).attr('data-project-id');
                var projectName = $(this).text();
                var type = $(this).attr('data-type');
                location.hash = '/projectDetails/incomeExpenditure?type='+type+'&id='+projectId+'&projectName='+encodeURI(projectName);
                return false;
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
                    case 'filter_toCompanyId'://收款组织
                    case 'filter_fromCompanyId'://付款组织

                        var selectedArr = [],selectList = [];
                        if(id=='filter_toCompanyId'){
                            
                            if(that._toCompanyList!=null && that._toCompanyList.length>0){
                                selectList = that._toCompanyList;
                            }
                        }
                        else if(id=='filter_fromCompanyId'){

                            if(that._fromCompanyList!=null && that._fromCompanyList.length>0){
                                selectList = that._fromCompanyList;
                            }
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
                            that.renderDataList();
                        };
                        $(that.element).find('#'+id).m_filter_select(option, true);

                        break;
                    case 'filter_projectName': //项目

                        var option = {};
                        option.inputValue = that._filterData[filterArr[1]];
                        option.eleId = id;
                        option.oKCallBack = function (data) {

                            that._filterData[filterArr[1]] = data;
                            that.renderDataList();
                        };
                        $(that.element).find('#'+id).m_filter_input(option, true);

                        break;
                    case 'filter_feeType'://收支分类子项

                        var option = {};
                        var newList = [];

                        newList = [
                            {name:'技术审查费',id:'2'},
                            {name:'合作设计费',id:'3'},
                            {name:'其他收支',id:'4'}
                        ];
                        option.selectArr = newList;
                        option.selectedArr = that._filterData.feeTypeList;
                        option.eleId = 'filter_feeType';
                        option.boxStyle = 'min-width:150px;';
                        option.dialogWidth = '150';
                        option.colClass = 'col-md-12';
                        option.selectedCallBack = function (data) {
                            console.log(data);
                            that._filterData.feeTypeList = data;
                            that.renderDataList();

                        };
                        $(that.element).find('#filter_feeType').m_filter_checkbox_select(option, true);
                        break;
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
