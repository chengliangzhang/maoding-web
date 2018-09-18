/**
 * 审批管理-添加审批
 * Created by wrb on 2018/9/14.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_form_template_settings",
        defaults = {
             isDialog:true
            ,type:1//1=我的审批
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentUserId = window.currentUserId;
        this._currentCompanyId = window.currentCompanyId;

        this._title = '自定义审批表单';
        this._subTitle = '我的审批';

        this._$controlBox = {};//控件面板
        this._$contentForm = {};//已选择的控件表单
        this._$propertyForm = {};//控件属性表单

        this._formFieldInfo = [];//保存的json，

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_form_template/m_form_template_settings',{
                title:that._title,
                subTitle:that._subTitle
            });
            that.renderDialog(html,function () {


                that._$controlBox = $(that.element).find('#controlBox');
                that._$contentForm = $(that.element).find('#contentBox form.content-form');
                that._$propertyForm = $(that.element).find('#propertyBox form[data-property-type="2"]');

                $(that.element).css('overflow','initial');
                $(that.element).parents('.layui-layer').css('overflow','auto');

                that.renderICheckOrSelect($(that.element).find('#propertyBox'));
                that.bindActionClick();
                that.controlMousemove();
                that.resizeFun();

            });
        }
        ,resizeFun : function () {
            var that = this;
            var setBoxHeight = function () {
                var dialogH = $(that.element).height();
                var contentH = $(that.element).find('.m-form-template-settings').height();
                if(dialogH>=contentH){
                    that._$controlBox.height(dialogH-110);
                    that._$propertyForm.parent().height(dialogH-110);

                }else{
                    that._$controlBox.height(contentH-110);
                    that._$propertyForm.parent().height(contentH-110);
                }
            };
            setBoxHeight();
            $(window).on('resize.m-form-template-settings', function(e){
                //console.log('resize.m-form-template-settings')
                setBoxHeight();
            });
            console.log($(that.element))
            /*$(that.element).parents('.layui-layer').on('scroll.m-form-template-settings', function(e){
                //console.log('scroll.m-form-template-settings')
                setBoxHeight();
            });*/
        }
        //渲染列表内容
        ,renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    area : ['100%','100%'],
                    content:html,
                    closeBtn:0,
                    fixed:true,
                    scrollbar:true,
                    anim:1,
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
        ,renderICheckOrSelect:function ($ele,ifCheckedFun,ifUncheckedFun,ifClickedFun) {


            var ifChecked = function (e) {
                if(ifCheckedFun)
                    ifCheckedFun($(this));
            };
            var ifUnchecked = function (e) {
                if(ifUncheckedFun)
                    ifUncheckedFun($(this));
            };
            var ifClicked = function (e) {
                if(ifClickedFun)
                    ifClickedFun($(this));
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

        //左边控件拖拽
        ,controlMousemove:function () {
            var that = this;
            var $controlItem = that._$controlBox.children('div');
            $controlItem.each(function () {
                var el = this;
                var $drag = $(this);
                //dragging = true, 记录起始坐标位置，设置鼠标捕获
                //判断如果dragging = true, 则当前坐标位置 - 记录起始坐标位置，绝对定位的元素获得差值
                //dragging = false, 释放鼠标捕获，防止冒泡
                var dragging = false;
                var iX, iY;
                if(document.attachEvent) {//ie的事件监听，拖拽div时禁止选中内容，firefox与chrome已在css中设置过-moz-user-select: none; -webkit-user-select: none;
                    $controlItem[0].attachEvent('onselectstart', function() {
                        return false;
                    });
                }
                $drag.mousemove(function(e) {
                    //console.log('mousemove')
                });
                $drag.mouseout(function(e) {
                    //console.log('mouseout')
                });
                $drag.mouseup(function(e) {
                    var index=$(this).index();
                    //console.log('mouseup')
                });
                $drag.mousedown(function(e) {//鼠标按下，鼠标变移动标志，克隆元素，并确定新克隆元素位置

                    //console.log('mousedown')
                    //that.renderSelectingControl();
                    //$(this).clone().addClass("clone").appendTo($("body"));
                    var type = $(e.target).closest('.control-item').attr('data-type');
                    if(isNullOrBlank(type))
                        return false;
                    var html = that.renderSelectingControl(type);
                    //console.log(html)
                    $(html).addClass('clone').appendTo($("body"));

                    that.renderICheckOrSelect($('.clone'));

                    $("body").css('cursor','move');
                    e = e || window.event;
                    dragging = true;
                    //按下元素后，计算当前鼠标位置
                    iX = e.clientX - el.offsetLeft;
                    iY = e.clientY - el.offsetTop;
                    //IE下捕捉焦点
                    el.setCapture && el.setCapture();
                    return false;

                });
                $(document).on('mousemove.m-form-template-settings',function (e) {
                    //console.log('document.mousemove')
                    if (dragging) {
                        e = e || window.event;
                        var oX = e.clientX + 1;
                        var oY = e.clientY + 1;

                        if($(".clone").length>0)
                        {
                            $(".clone").css({"left":oX + "px", "top":oY + "px"});
                            that.makeSpaceToFormItem(oX,oY);
                        }
                        return false;
                    }

                });
                $(document).on('mouseup.m-form-template-settings',function (e) {

                    //console.log('document.mouseup')
                    $("body").css('cursor','auto');
                    if($(".clone").length>0){
                        //IE下释放焦点
                        el.releaseCapture && el.releaseCapture();

                        var cleft = that._$contentForm.offset().left;
                        var eleft = $(".clone").offset().left;
                        if(eleft>=cleft){
                            var itemKey = $(".clone").attr('data-key');
                            console.log('itemKey=='+itemKey)
                            if( that._$contentForm.find('.form-item.m-b-space').length>0){

                                var ele = $(".clone").removeClass('clone').removeAttr('style');
                                that._$contentForm.find('.form-item.m-b-space').after(ele.prop('outerHTML'));

                            }else if(that._$contentForm.find('h4.m-b-space').length>0){

                                var ele = $(".clone").removeClass('clone').removeAttr('style');
                                that._$contentForm.find('h4.m-b-space').after(ele.prop('outerHTML'));

                            }else if(that._$contentForm.find('.form-item.m-t-space').length>0){

                                var ele = $(".clone").removeClass('clone').removeAttr('style');
                                that._$contentForm.find('.form-item.m-t-space').before(ele.prop('outerHTML'));

                            }else{
                                $(".clone").removeClass('clone').removeAttr('style').appendTo(that._$contentForm);
                            }
                            that._$contentForm.find('.form-item').removeClass('m-b-space').removeClass('m-t-space');
                            that._$contentForm.find('.panel h4').removeClass('m-b-space');
                            that.bindFormItemClick(that._$contentForm.find('.form-item[data-key="'+itemKey+'"]'));
                            that._$contentForm.find('.form-item[data-key="'+itemKey+'"]').click();
                        }
                        $(".clone").remove();
                    }
                    dragging = false;
                    //阻止冒泡
                    e.cancelBubble = true;
                });
            });
        }
        //渲染右边属性面板
        ,renderProperty:function () {
            var that = this;

            var type = that._$contentForm.find('.form-item.active').attr('data-type');
            //获取选中的key
            var itemKey = that._$contentForm.find('.form-item.active').attr('data-key');

            var html = template('m_form_template/m_form_template_item_property',{type:type,itemKey:itemKey});
            that._$propertyForm.html(html);
            that.renderICheckOrSelect(that._$propertyForm,function ($this) {//选中事件

            },function ($this) {//未选中事件

            },function ($this) {//点击事件

                if(type==6){
                    var radioType =  $this.attr('data-type');
                    if(radioType=='4'){
                        that._$propertyForm.find('.select-item').show();
                        delClick();
                        addSelectItem();
                    }else{
                        that._$propertyForm.find('.select-item').hide();
                    }
                }

            });


            var delClick = function(){
                that._$propertyForm.find('.row button[data-action="delSelectItem"]').off('click').on('click',function () {
                    $(this).parents('.row[data-type="optional"]').remove();
                    delItem();
                    return false;
                });
            };

            var delItem = function () {
                that._$propertyForm.find('.row[data-type="optional"] input').each(function (i) {
                    $(this).attr('placeholder','选项'+(i+1));
                });
            };
            var addSelectItem = function () {
                that._$propertyForm.find('button[data-action="addSelectItem"]').on('click',function () {
                    $(this).next().clone().appendTo($(this).parents('.form-group'));
                    $(this).parents('.form-group').find('.row[data-type="optional"]:last input').val('');
                    $(this).parents('.form-group').find('.row[data-type="optional"]:last .col-xs-1').removeAttr('style');
                    delItem();
                    delClick();
                    return false;
                });
            };

            //下拉列表
            if(type==6){
                that._$propertyForm.find('input[name="optional"]').on('click',function () {
                    var radioType =  $(this).attr('data-type');
                    if(radioType=='4'){
                        that._$propertyForm.find('.select-item').show();
                        delClick();
                        addSelectItem();
                    }else{
                        that._$propertyForm.find('.select-item').hide();
                    }
                });
            }
            //单选框和复选框
            if(type==7 || type==8){
                delClick();
                addSelectItem();
            }


        }
        //渲染中间正在选择控件面板（点击或拖拽）
        ,renderSelectingControl:function (type) {
            var that = this;

            var itemKey = UUID.genV4().hexNoDelim;//生成key，对应选择的控件，控件属性
            var html = template('m_form_template/m_form_template_item',{type:type,itemKey:itemKey});
            //$(that.element).find('#contentBox form.content-form').append(html);
            return html;
        }
        //计算form-item位置，拖拽到contentForm，form-item中，留空效果
        ,makeSpaceToFormItem:function (x,y) {
            var that = this;
            var $ele = null;
            that._$contentForm.find('.form-item').each(function (i) {
                 var top = $(this).offset().top;
                 var left = $(this).offset().left;
                 var height = $(this).height();
                 var width = $(this).width();
                 var type = $(this).attr('data-type');
                if(type==9 && $(this).find('.panel').length>0 && $(this).find('.panel .form-item').length==0 && x>left && x<left+width && y>(top+(height/2)) && y<(top+height)){

                    console.log('panel');
                    that._$contentForm.find('.form-item').removeClass('m-b-space').removeClass('m-t-space');
                    that._$contentForm.find('.panel h4').removeClass('m-b-space');
                    $(this).find('.panel h4').addClass('m-b-space');

                }else if(x>left && x<left+width && y>top && y< (top+(height/2)) &&
                     ($(this).attr('data-key')==that._$contentForm.find('.form-item').eq(0).attr('data-key') || $(this).attr('data-key')==that._$contentForm.find('.form-item .panel .form-item').eq(0).attr('data-key'))){

                     that._$contentForm.find('.form-item').removeClass('m-b-space').removeClass('m-t-space');
                     that._$contentForm.find('.panel h4').removeClass('m-b-space');
                     $(this).addClass('m-t-space');
                 }else if(x>left && x<left+width && y>(top+(height/2))) {
                     that._$contentForm.find('.form-item').removeClass('m-b-space').removeClass('m-t-space');
                     that._$contentForm.find('.panel h4').removeClass('m-b-space');
                     $(this).addClass('m-b-space');
                 }
            });
            return $ele;
        }
        ,bindFormItemClick:function ($formItem) {
            var that = this;
            //选择后的控件点击事件
            $formItem.on('click',function () {

                //添加点击样式
                $(this).addClass('active').siblings().removeClass('active');
                //渲染右边控件属性
                that.renderProperty();
            });
            $formItem.find('button[data-action="delItem"]').on('click',function () {
                $(this).parent('.form-item').remove();
            });
            $formItem.hover(function () {
                $formItem.find('button[data-action="delItem"]').show();
            },function () {
                $formItem.find('button[data-action="delItem"]').hide();
            });

            //添加明细
            if($formItem.attr('data-type')=='9'){
               
                //绑定明细按钮
                $formItem.find('button[data-action="addItem"]').on('click',function () {

                    if($formItem.find('.panel').length>0){
                        S_toastr.warning('暂时只支持一个明细模板！');
                        return false;
                    }
                    $(this).before('<div class="panel panel-default"><div class="panel-body"><form><h4 class="title-line"> 明细 </h4></form></div></div> ');
                    return false;
                });
            }
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;

            //左边控件点击事件
            $(that.element).find('#controlBox').children().off('click').on('click',function (e) {

                //console.log('controlBox.click')
                var $this = $(this),type = $this.attr('data-type');
                var itemKey = UUID.genV4().hexNoDelim;//生成key，对应选择的控件，控件属性
                var html = template('m_form_template/m_form_template_item',{type:type,itemKey:itemKey});

                //当前选中是明细，且已出现明细面板,追加到明细里
                var $activeFormItem = that._$contentForm.find('.form-item.active[data-type="9"]');
                if($activeFormItem.length==1 && $activeFormItem.find('.panel').length>0){
                    $activeFormItem.find('.panel form').append(html);
                }else{
                    $(that.element).find('#contentBox form.content-form').append(html);
                }


                var $formItem = that._$contentForm.find('.form-item[data-key="'+itemKey+'"]');
                that.bindFormItemClick($formItem);
                that.renderICheckOrSelect($formItem);
                $formItem.click();
                stopPropagation(e);
                return false;

            });
            $(that.element).find('.secondary-menu-ul li a').off('click').on('click',function () {
                var type = $(this).attr('type');
                $(this).parents('li').addClass('active').siblings().removeClass('active');
                var $propertyForm = $(that.element).find('form[data-property-type="'+type+'"]');
                $propertyForm.show().siblings('form').hide();

                var itemKey = $propertyForm.children().attr('data-key');

                if(type==2){

                    //没控件，提示
                    if(that._$contentForm.find('.form-item').length==0){
                        that._$propertyForm.html('<div>请选择控件</div>');
                    }
                    //已有控件，没选中，当前属性面板匹配选中控件并添加选中样式
                    if(that._$contentForm.find('.form-item').length>0 && that._$contentForm.find('.form-item.active')==0){
                        that._$contentForm.find('.form-item[data-key="'+itemKey+'"]').addClass('active').siblings().removeClass('active');
                    }
                    //已有控件，已选中，当前选中控件匹配属性面板,若对不上，则重新加载属性面板
                    if(that._$contentForm.find('.form-item').length>0 &&  that._$contentForm.find('.form-item.active')>=0 && that._$contentForm.find('.form-item.active').attr('data-key')!=itemKey){
                        that.renderProperty();
                    }
                }
            });

            $(that.element).find('a[data-action],button[data-action]').off('click').on('click',function () {
               var $this = $(this);
               var dataAction = $this.attr('data-action');
               switch (dataAction){
                   case 'close':
                       S_layer.close($this,function () {
                           $(document).off('mouseup.m-form-template-settings');
                           $(document).off('mousemove.m-form-template-settings');
                       });
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
