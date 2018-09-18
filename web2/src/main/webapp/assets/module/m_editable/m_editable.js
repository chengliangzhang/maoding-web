/**
 *
 * Created by wrb on 2018/9/13.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_editable",
        defaults = {
            type:'1'//type=1=input
            ,inline:false
            ,placement:'bottom'
            ,ok:null//提交方法
            ,cancel:null//关闭方法
            ,completed:null//渲染完成
            ,hideElement:false//是否隐藏元素
            ,contentClass:null//内容外层样式
            ,content:null//内容
            ,btnRight:false//多行按钮是否在右侧
            ,value:null
            ,dataInfo:null
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;



        this._dialogType = 2;
        if(this.settings.inline)
            this._dialogType = 3;

        //获取element属性
        this._placeholder = $(this.element).attr('data-placeholder');
        if(this._placeholder==undefined)
            this._placeholder = '请输入';

        this._valid = $(this.element).attr('data-valid');//验证方式
        this._type = $(this.element).attr('data-type');//类型:1=input
        this._key = $(this.element).attr('data-key');
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.render();
        }
        , render: function () {
            var that = this;

            if(isNullOrBlank(that.settings.value))
                $(that.element).html('<span class="fc-v1-grey">未设置</span>');

            //浮窗，添加padding
            var contentClass = that.settings.contentClass;
            if(contentClass==null && that.settings.inline===false)
                contentClass = 'p-m';

            var content = that.renderEditTypeHtml();
            var iHtml = template('m_editable/m_editable',{content:that.settings.content||content,title:that.settings.title,contentClass:contentClass,btnRight:that.settings.btnRight});

            $(that.element).off('click').on('click',function (e) {

                var options = {};
                options.content=iHtml;
                options.placement=that.settings.placement;
                options.type=that._dialogType;
                options.scrollClose=false;
                options.hideElement = that.settings.hideElement;
                options.popoverClass = that.settings.inline?'editable-inline':null;
                options.popoverStyle = that.settings.inline?{'position': 'relative','top': '-14px','left': '0px'}:null;

                if(that.settings.inline===false){
                    options.isArrow = true;
                }
                options.closed=function () {

                };
                options.renderedCallBack=function ($popover) {

                    that.renderingCompleted($popover);
                };

                $(that.element).m_floating_popover(options,true);
                e.stopPropagation();
            })

        }
        //文本清空
        ,inputClear:function ($popover) {
            var that = this;
            $popover.find('.m-editable-clear').prev().off('focus keyup').on('focus keyup',function () {
                if($.trim($(this).val())!=''){
                    $popover.find('span.m-editable-clear').show();
                }else{
                    $popover.find('span.m-editable-clear').hide();
                }
            });
            $popover.find('.m-editable-clear').prev().focus();
            $popover.find('span.m-editable-clear').off('click').on('click',function () {
                $popover.find('.m-editable-clear').prev().val('');
                $popover.find('span.m-editable-clear').hide();
            });
        }
        ,renderEditTypeHtml:function () {
            var that = this,html = '';
            switch (that._type){
                case '1'://文本
                    html = template('m_editable/m_editable_input',{placeholder:that._placeholder,value:that.settings.value,key:that._key});
                    break;
                case '2':
                    html = template('m_editable/m_editable_textarea',{placeholder:that._placeholder,value:that.settings.value,key:that._key});
                    break;
                case '3':
                    html = template('m_editable/m_editable_checkbox',{placeholder:that._placeholder,dataInfo:that.settings.dataInfo,key:that._key});
                    break;
                case '4':
                    html = template('m_editable/m_editable_address',{placeholder:that._placeholder,dataInfo:that.settings.dataInfo,key:that._key});
                    break;
                default :

                    break;
            }
            return html;
        }
        ,renderingCompleted:function ($popover) {
            var that = this;
            switch (that._type){
                case '1':
                case '2':
                    that.inputClear($popover);
                    break;
                case '4':
                    var province = undefined,city = undefined,county = undefined;
                    if(that.settings.dataInfo!=null && !isNullOrBlank(that.settings.dataInfo.province))
                        province = that.settings.dataInfo.province;

                    if(that.settings.dataInfo!=null && !isNullOrBlank(that.settings.dataInfo.city))
                        city = that.settings.dataInfo.city;

                    if(that.settings.dataInfo!=null && !isNullOrBlank(that.settings.dataInfo.county))
                        county = that.settings.dataInfo.county;

                    $popover.find('#selectRegion').citySelect({
                        prov:province,
                        city:city,
                        dist:county,
                        nodata:'none',
                        required:false
                    });
                    break;
            }

            if(that.settings.placement=='bottom' && !isNullOrBlank(that.settings.title))
                $popover.find('.arrow').addClass('has-title');

            that.bindActionClick($popover);
            that.save_validate($popover);
            if(that.settings.completed)
                that.settings.completed($popover);
        }
        ,bindActionClick:function ($popover) {
            var that = this;
            $popover.find('button.m-editable-submit').off('click').on('click',function () {
                if(that.saveValid($popover)){
                    var flag = true;
                    var data = $popover.find('form').serializeObject();
                    if(that.settings.ok)
                        flag = that.settings.ok(data);

                    if(flag!==false)
                        $(that.element).m_floating_popover('closePopover');
                }
            });
            $popover.find('button.m-editable-cancel').off('click').on('click',function () {
                $(that.element).m_floating_popover('closePopover');
            });
        }
        //验证
        ,saveValid:function ($popover) {
            var that = this;
            if ($popover.find('form').valid()) {
                return true;
            } else {
                return false;
            }
        }
        ,save_validate:function($popover){
            var that = this;
            var options = {};
            var t = that._valid;
            if(isNullOrBlank(t))
                return false;
            options.rules = {};
            options.messages = {};
            switch (t){
                case '1':
                    options.rules[that._key]={required: true};
                    options.messages[that._key]={required: that._placeholder};
                    break;
                default :

                    break;
            }
            options.errorPlacement=function (error, element) { //指定错误信息位置
                error.appendTo(element.closest('.m-editable-content'));
            };
            $popover.find('form').validate(options);

        }
        ,save_validate10:function($popover){
            var that = this;
            $popover.find('form').validate({
                rules: {
                    expAmount: {
                        required: true,
                        number:true,
                        minNumber:true,
                        maxlength:25,//是否超过25位
                        pointTwo:true
                    }
                },
                messages: {
                    expAmount: {
                        required: '请输入金额！',
                        number:'请输入有效数字',
                        minNumber:'请输入大于0的数字!',
                        maxlength:'对不起，您的操作超出了系统允许的范围。',
                        pointTwo:'请保留小数点后两位!'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.appendTo(element.closest('.col-sm-10'));
                }
            });
            $.validator.addMethod('minNumber', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if( value<=0){
                    isOk = false;
                }
                return  isOk;
            }, '请输入大于0的数字!');
            $.validator.addMethod('pointTwo', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if(!regularExpressions.proportionnumber.test(value)){
                    isOk = false;
                }
                return  isOk;
            }, '请保留小数点后两位!');
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
