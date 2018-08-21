/**
 * 编辑在位编辑type为popup时里面内容的自定义
 * Created by veata on 2016/12/21.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_defineEditableContent",
        defaults = {
            top:null,
            html:null,
            showAllButton:true,
            showCloseButton:false,
            title:null,
            p_width:null,
            p_height:null,
            placement:null,//浮窗是在哪个位置展开：‘left’,‘right’,‘top’,‘bottom’,空值则默认为top
            onShown: null,//浮窗显示后的事件，可以用来重新绑定值
            saveCallback:null,//点击确定触发的事件
            afterCallback:null,//页面加载后触发的事件
            cancelCallback:null,//点击取消触发的事件
            ifDelay:null,//是否延迟加载afterCallback
            errorTipsMarginLeft:null,//错误提示margin-left
            template:'m_project/m_defineEditableContent'
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._popoverTop = null;//初始化页面时，保存浮窗的top值
        this._popoverHeight = null;//初始化页面时，保存浮窗的height值
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            // that.closePopover();
            $('body').find('.popover').remove();
            if(!$(that.element).parent().find('.popover').length){
                that.getHtml(function(){
                    that.getPosition();
                    that.bindButtonClick();
                    that.closePopover();
                });
            }

        },
        //当鼠标点击的焦点不在浮窗内时，关闭浮窗
        closePopover:function(){
            var that = this;
            $('body').on('click',function(event){
                // console.log('body.click');
                // console.log(event.target);
                // console.log($(event.target).closest('.select2-search__field').length)
                if($(that.element).next('.popover').length>0){
                    if($(event.target).parents('.popover.editable-container').length<1
                        && event.target!=that.element && $(event.target).closest('.select2-search__field').length<1){
                        that.closeEditable();
                    }
                }

            })
        },
        getPosition:function(){
            var that = this;
            var p_p = that.settings.placement?that.settings.placement:'top';//浮窗的展示位置
            var a_ptop = $(that.element).position().top;//a标签的top值
            var a_width = $(that.element).outerWidth();//a标签的width值
            var a_height = $(that.element).outerHeight();//a标签的height值
            var a_pleft = $(that.element).position().left;//a标签的left值
            var p_width = that.settings.p_width || $(that.element).next('.popover').width();//浮窗的宽度
            var p_height = that.settings.p_height || $(that.element).next('.popover').height();//浮窗的高度
            var p_top = 0;//浮窗的top值
            var p_left = 0;//浮窗的left值
            switch(p_p){
                case 'top':
                    p_top = (a_ptop-p_height);
                    p_left = a_pleft+a_width/2-p_width/2;
                    break;
                case 'bottom':
                    p_top = (a_ptop+a_height);
                    p_left = a_pleft+a_width/2-p_width/2;
                    break;
                case 'left':
                    p_top = (a_ptop-p_height/2+5);
                    p_left = a_pleft-p_width-10;
                    break;
                case 'right':
                    p_top = (a_ptop-p_height/2+7);
                    p_left = a_pleft+a_width;
                    break;


            };
            that._popoverTop = p_top;
            that._popoverHeight = p_height;
            $(that.element).next('.popover').removeClass('top').addClass(p_p);
            if(p_p.indexOf('left')>-1||p_p.indexOf('right')>-1){
                $(that.element).next('.popover').find('.arrow').css({'top':'50%','left':''});
            }
            $(that.element).next('.popover').css({display:'inline-block',position:'absolute',top:p_top,left:p_left});


        },
        getHtml:function(callback){
            var that=this;
            var data={};
            data.showAllButton = that.settings.showAllButton;
            data.showCloseButton = that.settings.showCloseButton;
            data.title = that.settings.title;
            var html = template(that.settings.template,data);
            $(html).insertAfter(that.element);
            $(that.element).next('.popover').find('.editable-input').html(that.settings.html);
            var $popover = $(that.element).next('.popover.editable-container');

            if (that.settings.onShown && that.settings.onShown !== null)
                that.settings.onShown($popover);

            if(that.settings.afterCallback){
                that.settings.afterCallback($popover);
            }
            //绑定回车事件
            $popover.find('input[type="text"],input[type="password"]').keydown(function() {
                if (event.keyCode == '13') {//keyCode=13是回车键
                    $popover.find('.editable-submit').click();
                    preventDefault(event);
                }
            });
            if(callback){
                return callback($popover);
            }

        },
        bindButtonClick:function(){
            var that = this;
            var $popover = $(that.element).next('.popover');
            $popover.find('button').on('click',function(){
                if($(this).is('.editable-submit')){
                    var error = '';
                    if(that.settings.saveCallback){
                        error = that.settings.saveCallback();
                    }
                    if(error!=false){
                        that.closeEditable(error);
                    }
                }else{
                    if(that.settings.cancelCallback){
                        return that.settings.cancelCallback;
                    }
                    that.closeEditable();
                }
            });
            //点击submit按钮或浮窗其他地方，出现验证信息时，相应改变popover的top值
            $popover.find('.popover-content,button').off('click.changePosition').on('click.changePosition',function(e){
                setTimeout(function(){
                    that.changePosition($popover);
                },20);
            });
            //点击input表单，出现验证信息时，相应改变popover的top值
            $popover.find('input[type="text"]').off('keyup.changePosition').on('keyup.changePosition',function(e){
                setTimeout(function(){
                    that.changePosition($popover);
                },20);
            });
        },

        //通过改变弹窗的top值来改变弹窗的位置
        changePosition:function($popover){
            var that = this;
            var errTag = $popover.find('label.error,div.editable-error-block').length;
            var popH = $popover.outerHeight();
            var h1 = popH-that._popoverHeight;
            if(errTag>0){
                var newTop = that._popoverTop-h1;
                $popover.css('top',newTop);
            }
        },
        closeEditable:function(data){
            var that = this;
            if(data && data!=''){
                var cssObj = {display:'block',color:'#a94442'};
                if(that.settings.errorTipsMarginLeft!=null){
                    cssObj.marginLeft = that.settings.errorTipsMarginLeft+'px';
                }
                return $(that.element).next('.popover').find('.editable-error-block').css(cssObj).text(data);
            }
            $(that.element).next('.popover').remove();
            $('body').off("click");
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


