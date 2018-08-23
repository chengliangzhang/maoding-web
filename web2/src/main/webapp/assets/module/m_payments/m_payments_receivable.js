/**
 * 收支总览－收支明细-应收
 * Created by wrb on 2017/11/30.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_receivable",
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
                receivableId:null,
                feeType:null,
                associatedOrg:null,
                projectName:null
            };
        }
        //初始化数据并加载模板
        ,initHtmlData:function () {
            var that = this;
            var html = template('m_payments/m_payments_receivable',{});
            $(that.element).html(html);
            var option = {};
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                that.renderDataList();
            };
            option.$renderCallBack = function () {
                that.bindRefreshBtn();
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);


            var timeOption = {};
            timeOption.selectTimeCallBack = function (data) {
                console.log(data);
                if(!isNullOrBlank(data.startTime))
                    that._filterData.startDate = data.startTime;

                if(!isNullOrBlank(data.endTime))
                    that._filterData.endDate = data.endTime;

                that.renderDataList();
            };
            $(that.element).find('.time-combination').m_filter_timeCombination(timeOption,true);

        }
        //渲染应收list
        ,renderDataList:function () {
            var that = this;
            var option = {};
            option.param = {};
            that._filterData.receivableId = that._selectedOrg.id;
            option.param = filterParam(that._filterData);

            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-box',
                url: restApi.url_getReceivable,
                params: option.param
            }, function (response) {

                if (response.code == '0') {

                    that._companyListBySelect = response.data.organization;
                    var html = template('m_payments/m_payments_receivable_list',{
                        dataList:response.data.data,
                        receivaleSum:response.data.receivaleSum
                    });
                    $(that.element).find('.data-list-container').html(html);
                    that.bindViewDetail();
                    that.bindGoExpensesPage();
                    that.filterActionClick();

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
                option.$title = '应收详情';
                option.$id = $(this).attr('data-id');
                option.$type = 1;
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
                    case 'filter_feeType'://收支类型
                    case 'filter_associatedOrg'://关联组织

                        var selectedArr = [],selectList = [];
                        if(id=='filter_feeType'){
                            selectList = [
                                {name:'技术审查费',id:'2'},
                                {name:'合作设计费',id:'3'},
                                {name:'其他收支',id:'4'}
                            ]
                        }
                        else if(id=='filter_associatedOrg'){

                            if(that._companyListBySelect!=null && Object.getOwnPropertyNames(that._companyListBySelect).length>0){
                                $.each(that._companyListBySelect, function (key, value) {
                                    selectList.push({id: key, name: value});
                                });
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
