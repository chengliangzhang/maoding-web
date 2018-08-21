/**
 * Created by wrb on 2016/12/15.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_uptPassword",
        defaults = {
            savePasswordCallBack:null
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
            that.initHtml();

        }
        ,initHtml:function(){
            var that = this;
            S_dialog.dialog({
                title: '修改密码',
                //contentEle: 'changePassWordOBox',
                lock: 3,
                width: '500',
                tPadding: '0px',
                url: rootPath+'/assets/module/m_common/m_dialog.html',
                ok:function(){
                    var flag = $('form.changePassWordOBox').valid();
                    if(!flag){
                        return false;
                    }else{
                        that.savePassword();
                    }
                },
                okText:'保存',
                cancel:function(){},
                cancelText:'取消',
            },function(d){//加载html后触发
                var html = template('m_personal/m_uptPassword',{});
                $('div[id="content:'+d.id+'"]').html(html);
                that.savePassword_validate();
            });
        }
        //保存密码
        ,savePassword:function () {
            var that = this;
            var option  = {};
            option.url = restApi.url_changePassword;
            var $data = $('form.changePassWordOBox').serializeObject();
            option.postData = $data;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_dialog.success('修改成功，请重新登录!','提示',function(){
                        window.location.href = rootPath+'/iWork/sys/logout';
                    })
                }else {
                    S_dialog.error(response.info);
                    return false;
                }
            });
        }
        ,savePassword_validate:function(){
            $('form.changePassWordOBox').validate({
                rules: {
                    oldPassword: {
                        required: true
                    },
                    password:{
                        required:true,
                        minlength: 6,
                        maxlength:12
                    },
                    rePassword:{
                        required:true,
                        minlength: 6,
                        maxlength:12,
                        equalTo: "#password"
                    }
                },
                messages: {
                    oldPassword: {
                        required: '请输入旧密码！'
                    },
                    password:{
                        required:'请输入新密码！',
                        minlength:'密码为6-12位！',
                        maxlength:'密码为6-12位！'
                    },
                    rePassword:{
                        required:'请确认新密码！',
                        minlength:'密码为6-12位！',
                        maxlength:'密码为6-12位！',
                        equalTo: '两次密码输入不一致'
                    }
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
