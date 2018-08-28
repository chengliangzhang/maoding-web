/**
 * Created by Wuwq on 2017/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_projectList",
        defaults = {
            postData: null,
            renderCallback:null,//渲染页面完成后事件
            dataAction: 'myProjectList'//{myProjectList,projectOverview}
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;

        this._headerList = null;
        this._companyNameList = [];//筛选条件-立项组织
        this._partyANameList = [];//筛选条件-甲方
        this._partyBNameList = [];//筛选条件-乙方
        this._buildTypeNameList = [];//筛选条件-功能分类
        this._designCompanyNameList = [];//筛选条件-合作组织

        this._projectList = [];//项目列表
        this._type = this.settings.dataAction=='myProjectList'?0:1;

        /******************* 筛选条件 值预存 *********************/

        this._filterData = {};

        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this,option = {};

            //渲染容器
            $(that.element).html(template('m_projectList/m_projectList', {}));
            that.bindActionClick();
            that.renderListHeader(0);
        }
        //渲染列表头部
        ,renderListHeader:function (t) {
            var that = this;
            var option = {};
            option.dataAction = that.settings.dataAction;
            option.type = that._type;
            if(t==0)
                option.isFirstLoad = true;

            option.renderCallBack = function (data) {
                that._headerList = data;
                that.renderListContent(t);
            };
            option.filterCallBack = function (data) {
                console.log(data);
                that._filterData = data;
                that.renderListContent();
            };
            $(that.element).find('.data-list-container table thead').m_field_list_header(option,true);
        }
        ,renderListContent:function (t) {
            var that = this;

            if(that.settings.dataAction=='projectOverview'){
                that._filterData.type = 1;
            }else{
                that._filterData.type = null;
            }

            var option = {};
            option.url = restApi.url_listProject;
            option.headerList = that._headerList;
            option.isSetCookies = true;
            option.dataAction = that.settings.dataAction;
            option.aStr = 'projectName';
            option.param = that._filterData;
            option.renderCallBack = function (data) {
                that.bindGotoProject();
                if(that.settings.renderCallback)
                    that.settings.renderCallback(data)
            };
            if(t==0)
                option.isFirstLoad = true;

            $(that.element).find('.data-list-container table tbody').m_field_list_row(option,true);
        }
        //跳转详情
        , bindGotoProject: function () {
            var that = this;
            $(that.element).find('a[data-action="projectName"]').off('click').on('click', function (e) {
                var $btn = $(this);
                var pId = $btn.closest('tr').attr('data-id');//项目ID
                var pName = $btn.text();//项目名称
                location.hash = '/projectDetails/basicInfo?id='+pId+'&projectName='+encodeURI(pName);
                stopPropagation(e);
                return false;
            });
        }
        //事件绑定
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'searchProject':

                        that._filterData.keyword = $(that.element).find('input[name="keyword"]').val();
                        that.renderListContent();
                        break;
                    case 'setField'://设置字段
                        var option = {};
                        option.saveCallBack = function () {
                            that.renderListHeader();
                        };
                        $('body').m_field_settings(option,true);
                        return false;
                        break;
                    case 'exportDetails'://导出
                        downLoadFile({
                            url:restApi.url_exportProjectList,
                            data:filterParam(that._filterData),
                            type:1
                        });
                        return false;
                        break;
                }
            });
        }
    });

    /*
     1.一般初始化（缓存单例）： $('#id').pluginName(initOptions);
     2.强制初始化（无视缓存）： $('#id').pluginName(initOptions,true);
     3.调用方法： $('#id').pluginName('methodName',args);
     */
    $.fn[pluginName] = function (options, args) {
        var instance;
        var funcResult;
        var jqObj = this.each(function () {

            //从缓存获取实例
            instance = $.data(this, "plugin_" + pluginName);

            if (options === undefined || options === null || typeof options === "object") {

                var opts = $.extend(true, {}, defaults, options);

                //options作为初始化参数，若args===true则强制重新初始化，否则根据缓存判断是否需要初始化
                if (args === true) {
                    instance = new Plugin(this, opts);
                } else {
                    if (instance === undefined || instance === null)
                        instance = new Plugin(this, opts);
                }

                //写入缓存
                $.data(this, "plugin_" + pluginName, instance);
            }
            else if (typeof options === "string" && typeof instance[options] === "function") {

                //options作为方法名，args则作为方法要调用的参数
                //如果方法没有返回值，funcReuslt为undefined
                funcResult = instance[options].call(instance, args);
            }
        });

        return funcResult === undefined ? jqObj : funcResult;
    };


})(jQuery, window, document);