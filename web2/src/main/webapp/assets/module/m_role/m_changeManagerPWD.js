/**
 *
 * Created by veata on 2016/12/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_changeManagerPWD",
        defaults = {
            adminInfo:{},
            title:null,
            companyId:null,
            saveCallback:null
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
            that.initHtmlData();
        }
        ,initHtmlData:function(){
            var that = this;

            S_dialog.dialog({
                title:'修改管理员密码',
                contentEle:'dialogOBox',
                lock:3,
                width:'600',
                minHeight:'300',
                tPadding:'0px',
                url:rootPath+'/assets/module/m_common/m_dialog.html',
                cancel:function () {

                    },
                ok:function () {
                    var options = {};
                    options.url = restApi.url_changAdminPassword;
                    options.postData = $('form.changeAdminPWDOBox').serializeObject();
                    var flag = $("form.changeAdminPWDOBox").valid();
                    if(!flag){
                        return false;
                    }else{
                        m_ajax.postJson(options,function (response) {
                            if(response.code=='0'){
                                S_dialog.success('修改成功，请重新登录!','提示',function(){
                                    window.location.href = rootPath+'/iWork/sys/logout';
                                })
                            }else {
                                S_dialog.error(response.info);
                            }

                        });
                    }

                    }
                }

                ,function(d){//加载html后触发
                var $data = {};
                var html = template('m_role/m_changeManagerPWD',$data);
                $('div[id="content:'+d.id+'"] .dialogOBox').html(html);
                that.changeAdminPWDOBox_validate();
            });

        }
        ,changeAdminPWDOBox_validate:function(){//step1的表单验证
            $("form.changeAdminPWDOBox").validate({
                rules: {
                    oldPassword: 'required',
                    newPassword: {
                        required: true,
                        rangelength:[6,12],
                        checkSpace:true
                    },
                    changeAdminRePwd: {
                        required:true,
                        rangelength:[6,12],
                        checkSpace:true,
                        equalTo:'#newPassword'
                    }
                },
                messages: {
                    oldPassword: '请输入旧密码！',
                    newPassword: {
                        required:'请输入新密码！',
                        rangelength: "密码为6-12位！",
                        checkSpace: "密码不应含有空格!"
                    },
                    changeAdminRePwd: {
                    required:'请确认新密码！',
                    rangelength: "密码为6-12位！",
                    checkSpace: "密码不应含有空格!",
                    equalTo:'两次输入的密码不相同，请重新输入！！'
                    }
                }
            });
            $.validator.addMethod("checkSpace", function(value, element) {
                var pattern=/^\S+$/gi;
                return this.optional(element) || pattern.test( value ) ;
            }, "密码不应含有空格!");
        },



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
