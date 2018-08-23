/**
 * 地址筛选
 * Created by wrb on 2018/8/15.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_filter_address",
        defaults = {
            eleId:null,//元素ID
            align:null,//浮窗位置
            isEndTime:true,//是否显示结束时间
            addressData:{},//时间缓存值{startTime,endTime}
            okCallBack:null//选择回调
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.render();
        }
        , render: function () {
            var that = this;

            if(that.settings.addressData && (!isNullOrBlank(that.settings.addressData.province) || !isNullOrBlank(that.settings.addressData.city) || !isNullOrBlank(that.settings.addressData.county))){
                $(that.element).find('i').addClass('fc-v1-blue');
            }
            $(that.element).on('click',function (e) {

                S_dialog.dialog({
                    contentEle: 'dialogOBox',
                    ele:that.settings.eleId,
                    lock: 2,
                    align: that.settings.align || 'bottom right',
                    quickClose:true,
                    noTriangle:true,
                    width: '350',
                    minHeight:'110',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html'

                },function(d){//加载html后触发

                    var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';
                    var iHtml = template('m_filterableField/m_filter_address',{});
                    $(dialogEle).html(iHtml);

                    $(dialogEle).find("#selectRegion").citySelect({
                        prov:isNullOrBlank(that.settings.addressData.province)?'':that.settings.addressData.province,
                        city:isNullOrBlank(that.settings.addressData.city)?'':that.settings.addressData.city,
                        dist:isNullOrBlank(that.settings.addressData.county)?'':that.settings.addressData.county,
                        nodata:"none",
                        required:false
                    });
                    $(dialogEle).find('button[data-action="cancel"]').on('click',function () {
                        S_dialog.close($(dialogEle));
                    });
                    $(dialogEle).find('button[data-action="confirm"]').on('click',function () {

                        var province = $(dialogEle).find('select[name="province"]').val();
                        var city = $(dialogEle).find('select[name="city"]').val();
                        var county = $(dialogEle).find('select[name="county"]').val();

                        province = province==undefined?'':province;
                        city = city==undefined?'':city;
                        county = county==undefined?'':county;

                        if(that.settings.okCallBack)
                            that.settings.okCallBack({province:province,city:city,county:county});

                        S_dialog.close($(dialogEle));
                    });

                });
                stopPropagation(e);
                return false;
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
