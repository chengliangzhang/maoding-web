/**
 * 公共列表-表头渲染
 * Created by wrb on 2018/8/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_field_list_header",
        defaults = {
            isDialog:true,
            type:0,//标题栏类型：0-我的项目标题栏，1-项目总览标题栏，2-发票汇总标题栏
            filterData:null,//请求后台的参数
            filterCallBack:null,//筛选回滚事件
            renderCallBack:null//渲染回滚事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._fieldList = [];//字段列表
        this._filterData = this.settings.filterData || {};
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_listTitle;
            option.postData = {
                type:that.settings.type
            };

            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){

                    that._fieldList = response.data;
                    var html = template('m_field/m_field_list_header',{fieldList:that._fieldList});
                    $(that.element).html(html);
                    if(that.settings.renderCallBack)
                        that.settings.renderCallBack(that._fieldList);

                    that.filterActionClick();

                }else {
                    S_dialog.error(response.info);
                }
            });
        }
        ,filterActionClick:function () {
            var that = this;
            //筛选事件
            $(that.element).find('a.icon-filter').each(function () {

                var $this = $(this);
                var id = $this.attr('id');

                var filterArr = id.split('_');

                var code = $this.closest('th').attr('data-code');
                //获取节点数据
                var dataItem = getObjectInArray(that._fieldList,code,'code');

                //过滤器类型：0-无过滤器，1-字符串，2-单选列表，3-多选列表，4-时间，5-地址
                switch (dataItem.filterType){
                    case 1://文本
                        var option = {};
                        option.inputValue = that._filterData[code];
                        option.eleId = id;
                        option.oKCallBack = function (data) {

                            that._filterData[code] = data;

                            if(that.settings.filterCallBack)
                                that.settings.filterCallBack(that._filterData);

                        };
                        $this.m_filter_input(option, true);

                        break;
                    case 2://单选列表

                        var selectList = [],selectedArr = [];

                        if(!isNullOrBlank(that._filterData[code]))
                            selectedArr.push(that._filterData[code]);

                        var option = {};
                        option.selectArr = dataItem.filterList;
                        option.selectedArr = selectedArr;
                        option.eleId = id;
                        option.selectedCallBack = function (data) {
                            if(data && data.length>0){
                                that._filterData[code] = data[0];
                            }else{
                                that._filterData[code] = null;
                            }
                            if(that.settings.filterCallBack)
                                that.settings.filterCallBack(that._filterData);
                        };
                        $this.m_filter_select(option, true);

                        break;
                    case 3://多选列表

                        var newCode = code+'List';
                        var option = {};
                        option.selectArr = dataItem.filterList;
                        if(!isNullOrBlank(that._filterData[newCode]))
                            option.selectedArr = that._filterData[newCode];

                        option.eleId = id;
                        option.dialogWidth = '420';
                        option.selectedCallBack = function (data) {
                            console.log(data);
                            if(data && data.length>0)
                                that._filterData[newCode] = data.join(',');

                            if(that.settings.filterCallBack)
                                that.settings.filterCallBack(that._filterData);
                        };
                        $this.m_filter_checkbox_select(option, true);

                        break;
                    case 4:
                        var newCode1 = code+'Start', newCode2 = code+'End';
                        var timeData = {};
                        timeData.startTime = that._filterData[newCode1];
                        timeData.endTime = that._filterData[newCode2];

                        var option = {};
                        option.timeData = timeData;
                        option.eleId = id;
                        option.okCallBack = function (data) {

                            that._filterData[newCode1] = data.startTime;
                            that._filterData[newCode2] = data.endTime;
                            that.getData();
                        };
                        $this.m_filter_time(option, true);

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
