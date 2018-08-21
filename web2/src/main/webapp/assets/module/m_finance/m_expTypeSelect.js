/**
 * 报销类别选择
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_expTypeSelect",
        defaults = {
            delButtonType:0,//当有默认delButtonType==1时，则把清除按钮去掉
            parentTarget:'',//当有默认targetP时，则主要用于我的报销
            expTypeThis:null,//当有expTypeName时
            callBack1:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.getExpData();
        }
        //加载基本数据
        ,getExpData: function () {
            var that=this;
            var option  = {};
            option.url = restApi.url_getExpTypeList;
            option.postData={};
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    var targetP = $('.expTypeTd');
                    if(that.settings.parentTarget!=null && that.settings.parentTarget!=''){
                        targetP = $(that.settings.parentTarget + ' .expTypeTd');//当有默认targetP时，则给加上targetP选择器
                    }else{
                        targetP = $('.expTypeTd');
                    }
                    var html = template('m_finance/m_expTypeSelect',{expTypeList:response.data});
                    targetP.html(html);
                    if(that.settings.delButtonType==1){//当有默认delButtonType==1时，则把清除按钮去掉
                        $('.typeNameArea').find('a[data-action="deleteAll"]').remove();
                        targetP.find('.thumbnail').find('a[data-action="firstItem"]').addClass('no-hover');
                    }
                    if(that.settings.expTypeThis){//当有默认expTypeName时，则让表单默认为expTypeName
                        var attrObj = that.settings.expTypeThis;
                        attrObj.i = targetP.parents('tr').attr('target');
                        targetP.find('input').val(that.settings.expTypeThis.expTypeName);
                        that.settings.callBack2 (attrObj,that.settings.expSumFilterData);
                    }
                    that.bindClickFun(targetP);
                }else {
                    S_dialog.error(response.info);
                }
            });
        }
        //给select表单里的所有可点击按钮添加点击事件
        ,bindClickFun:function(targetP){
            var that = this;
            //给类别的input表单添加点击事件
            targetP.find('input[data-action="collectExpType"]').bind('click',function(event){
                var $this = $(this);
                $('.typeNameArea').each(function(){
                    if($(this).prev('input')[0]!=$this[0]){
                        $(this).addClass($(this).is('.hide')?'':'hide');
                    }
                });
                if($(this).next('.typeNameArea').is('.hide')){
                    $(this).next('.typeNameArea').removeClass('hide');
                }else{
                    $(this).next('.typeNameArea').addClass('hide');
                }
                stopPropagation(event);
            });
            //给类别的下拉选项添加选择事件
            targetP.find('.typeNameArea a[data-action]').each(function(){
                var action = $(this).attr('data-action');
                $(this).bind('click',function(event){
                    var attrObj = {};
                    if(action=='deleteAll'){//清除按钮
                        $(this).parents('.expTypeTd').find('input[data-action="collectExpType"]').val('');
                        attrObj.expName = '';
                        attrObj.expPName = '';
                        attrObj.expAllName = '';
                        attrObj.expType = '';
                        attrObj.expParentType = '';
                    }
                    else if(action=='firstItem' && !(that.settings.parentTarget==''||that.settings.parentTarget==null)){//父类别不可选择时：我的报销页面里
                        return false;
                    }
                    else if(action=='firstItem'){//选择父类别
                        attrObj.expName = '';
                        attrObj.expPName = $(this).attr('name');
                        attrObj.expAllName = '';
                        attrObj.expType = '';
                        attrObj.expParentType = $(this).attr('id');
                        $(this).parents('.expTypeTd').find('input[data-action="collectExpType"]').val(attrObj.expPName);
                    }
                    else{//选择子类别
                        attrObj.expName = $(this).attr('name');
                        attrObj.expPName = '';
                        attrObj.expAllName = $(this).attr('parent-name')+'-'+$(this).attr('name');
                        attrObj.expType = $(this).attr('id');
                        attrObj.expParentType = '';
                        var expTypeName = $(this).attr('parent-name')+'('+$(this).attr('name')+')';
                        $(this).parents('.expTypeTd').find('input[data-action="collectExpType"]').val(expTypeName);
                    }
                    $(this).parents('.typeNameArea').addClass('hide');
                    if(that.settings.callBack1!=null){
                        attrObj.i = $(this).parents('tr').attr('target');
                        return that.settings.callBack1 (attrObj,that.settings.expSumFilterData);
                    }
                    event.stopPropagation();
                });

            });

        }


    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            // if (!$.data(this, "plugin_" + pluginName)) {
                $.data(this, "plugin_" +
                    pluginName, new Plugin(this, options));
            // }
        });
    };

})(jQuery, window, document);
