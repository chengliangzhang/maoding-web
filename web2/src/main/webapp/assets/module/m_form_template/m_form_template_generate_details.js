/**
 * 表单生成界面，并保存
 * Created by wrb on 2018/9/20.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_form_template_generate_details",
        defaults = {
             isDialog:true
            ,type:1//1=我的审批
            ,dataInfo:null
            ,closeCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentUserId = window.currentUserId;
        this._currentCompanyId = window.currentCompanyId;
        this._currentCompanyUserId = window.currentCompanyUserId;

        this._uploadmgrContainer = null;
        this._uuid = UUID.genV4().hexNoDelim;//targetId

        this._dataInfo = {};

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            var option = {};
            option.url = restApi.url_initDynamicAudit;
            option.postData = {
                id:that.settings.dataInfo.id
            };
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    that._dataInfo = response.data;
                    that._dataInfo.dialogHeight = 'height:'+(600-100)+'px';
                    var html = template('m_form_template/m_form_template_generate_details',{
                        dataInfo:that._dataInfo,
                        currentCompanyUserId:that._currentCompanyUserId
                    });
                    that.renderDialog(html,function () {

                        that.bindActionClick();
                        var expAmout = 0,isShowStatistics=false,fieldUnit = '';
                        $(that.element).find('div[data-statistics="1"]').each(function () {
                            expAmout = expAmout + ($(this).attr('data-value')-0);
                            fieldUnit = $(this).attr('data-field-unit');
                            isShowStatistics = true;
                        });
                        if(isShowStatistics){
                            $(that.element).find('#isShowStatistics').show();
                            if(fieldUnit=='元'||fieldUnit=='万元')
                                expAmout = expNumberFilter(expAmout);
                            $(that.element).find('#isShowStatistics #expAmount').html(expAmout+''+fieldUnit);
                        }

                        return false;
                    });

                } else {
                    S_layer.error(response.info);
                }
            });

        }
        //渲染列表内容
        ,renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title:that.settings.dataInfo.formName || '我的审批',
                    area : ['900px','600px'],
                    fixed:true,
                    scrollbar:false,
                    content:html,
                    btn:false

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;
                    if(callBack)
                        callBack();
                });

            }else{//不以弹窗编辑
                $(that.element).html(html);
                if(callBack)
                    callBack();
            }

        }
        //初始化iCheck
        ,renderICheckOrSelect:function ($ele) {

            var that = this;
            var ifChecked = function (e) {
            };
            var ifUnchecked = function (e) {
            };
            var ifClicked = function (e) {
            };
            $ele.find('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked).on('ifClicked',ifClicked);

            $ele.find('select').select2({
                tags:false,
                allowClear: false,
                minimumResultsForSearch: -1,
                width:'100%',
                language: "zh-CN"
            });
        }
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action],button[data-action]').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'cancel'://取消
                        S_layer.close($(that.element));

                        break;

                    case 'agree'://同意
                        var option = {};
                        option.dataInfo = {
                            id:that.settings.dataInfo.id,
                            processFlag:that._dataInfo.processFlag
                        };
                        option.doType = 1;
                        option.saveCallBack = function () {
                            that.init();
                            if(that.settings.closeCallBack)
                                that.settings.closeCallBack();
                        };
                        console.log(option);
                        $('body').m_approval_operational_comments(option,true);

                        break;
                    case 'returnBack'://退回

                        var option = {};
                        option.dataInfo = {
                            id:that.settings.dataInfo.id,
                            processFlag:that._baseData.processFlag
                        };
                        option.doType = 2;
                        option.saveCallBack = function () {
                            that.init();
                            if(that.settings.closeCallBack)
                                that.settings.closeCallBack();
                        };
                        $('body').m_approval_operational_comments(option,true);

                        break;
                    case 'cancellation'://撤销

                        var option = {};
                        option.dataInfo = {
                            id:that.settings.dataInfo.id,
                            processFlag:that._dataInfo.processFlag
                        };
                        option.doType = 3;
                        option.saveCallBack = function () {
                            that.init();
                            if(that.settings.closeCallBack)
                                that.settings.closeCallBack();
                        };
                        $('body').m_approval_operational_comments(option,true);

                        break;
                    case 'preview'://查看文件

                        window.open($this.attr('data-src'));

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
