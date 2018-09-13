/**
 * 公共列表-表头渲染
 * Created by wrb on 2018/8/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_field_list_row",
        defaults = {
            isPagination:true,//是否分页
            pageIndex:null,//当前页
            isRefreshCurrentPage:false,//是否刷新当前页
            headerList:null,//表头列表
            param:null,//参数
            url:null,//请求地址
            isSetCookies:false,//是否请求参数保存Cookies
            dataAction:null,//菜单路径
            aStr:null,//需要用a元素展示的字段code,例：{code1,code2}
            buttonMap:null,//按钮列表map对象，{key=value}， key是code,value是按钮文字_条件code,存在条件code(是列表的某字段),加判断 条件code 0-展示，1-不展示
            isFirstLoad:false,//是否第一次进来
            renderCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._currentCompanyUserId = window.currentCompanyUserId;
        this._cookiesMark = 'cookiesData_'+this.settings.dataAction+'_'+this._currentCompanyUserId;

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            if(that.settings.isPagination){
                that.renderListByPagination();
            }else{
                that.renderList();
            }

        }
        //渲染列表-不分页
        ,renderList:function () {
            var that = this;
            var option  = {};
            option.classId = that.element;
            option.url = that.settings.url;
            option.postData = {};
            option.postData = filterParam(that.settings.param);

            var cookiesData = undefined;
            if(that.settings.isSetCookies)
                cookiesData = Cookies.get(that._cookiesMark);

            if(cookiesData!=undefined && that.settings.isFirstLoad){
                cookiesData = $.parseJSON(cookiesData);
                option.postData = cookiesData.param;
            }

            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){

                    if(that.settings.isSetCookies){
                        //通过Cookie记住项目页码
                        var $cookiesData = {
                            param:option.postData,
                            dataAction:that.settings.dataAction
                        };
                        Cookies.set(that._cookiesMark, $cookiesData);
                    }

                    var html = template('m_field/m_field_list_row',{
                        dataList:response.data,
                        headerList:that.settings.headerList,
                        aStr:that.settings.aStr
                    });
                    $(that.element).html(html);

                    if(that.settings.renderCallBack)
                        that.settings.renderCallBack(response.data);

                }else {
                    S_layer.error(response.info);
                }
            });
        }
        //渲染列表-分页
        ,renderListByPagination:function () {
            var that = this;
            var option = {};
            option.param = {};
            option.param = filterParam(that.settings.param);

            var cookiesData = undefined;
            if(that.settings.isSetCookies)
                cookiesData = Cookies.get(that._cookiesMark);

            if(cookiesData!=undefined && that.settings.isFirstLoad){
                cookiesData = $.parseJSON(cookiesData);
                option.param = cookiesData.param;
            }

            if(that.settings.pageIndex)
                option.param.pageIndex = that.settings.pageIndex;

            if(that.settings.isRefreshCurrentPage && $("#data-pagination-container").pagination() && typeof($("#data-pagination-container").pagination('getPageIndex'))==='number' && $("#data-pagination-container").pagination('getPageIndex')!=0)
                option.param.pageIndex = $("#data-pagination-container").pagination('getPageIndex');

            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-box',
                url: that.settings.url,
                params: option.param
            }, function (response) {

                if (response.code == '0') {

                    if(that.settings.isSetCookies){
                        //通过Cookie记住项目页码
                        option.param.pageIndex = $("#data-pagination-container").pagination('getPageIndex');
                        var $cookiesData = {
                            param:option.param,
                            dataAction:that.settings.dataAction
                        };
                        Cookies.set(that._cookiesMark, $cookiesData);
                    }

                    var html = template('m_field/m_field_list_row',{
                        dataList:response.data.data,
                        headerList:that.reSetHeaderList(that.settings.headerList),
                        aStr:that.settings.aStr
                    });
                    $(that.element).html(html);

                    if(that.settings.renderCallBack)
                        that.settings.renderCallBack(response.data);

                } else {
                    S_layer.error(response.info);
                }
            });
        }

        //重新遍历headerList
        ,reSetHeaderList:function (headerList) {

            var that = this;

            //indexOf(','+aStr+',');避免重复
            if(!isNullOrBlank(this.settings.aStr))
                that.settings.aStr = ','+this.settings.aStr+',';

            if(headerList!=null && headerList.length>0){
                $.each(headerList,function (i,item) {

                    //以A标签展示
                    if(that.settings.aStr && that.settings.aStr.indexOf(','+item.code+',')>-1){
                        item.showByATag = true;
                        return true;
                    }

                    //以button标签展示
                    if(that.settings.buttonMap!=null && that.settings.buttonMap.get(item.code)!=undefined){

                        var value = that.settings.buttonMap.get(item.code);
                        var valueArr = [];
                        if(value.indexOf('_')){
                            valueArr = value.split('_');
                            item.showByButtonTag = valueArr[0];
                            item.showByButtonTagCode = valueArr[1];
                        }else{
                            item.showByButtonTag = value;
                        }
                        return true;
                    }

                })
            }

            return headerList;
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
