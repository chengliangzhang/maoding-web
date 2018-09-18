/**
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_teamDissolution",
        defaults = {
            teamInfo:null
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
            that.getData();
        }
        //加载痰喘
        ,getData: function () {
            var that = this;
            S_layer.dialog({
                title: '解散组织',
                area : '600px',
                content:template('m_teamInfo/m_teamDissolution',{teamInfo:that.settings.teamInfo}),
                cancel:function () {
                },
                ok:function () {

                    var option = {};
                    option.url=restApi.url_disbandCompany;
                    option.postData={};
                    option.postData.adminPassword=$('.teamDissolutionOBox input[name="password"]').val();
                    if(!$('form.teamDissolutionOBox').valid()){
                        return false;
                    }else{
                        m_ajax.postJson(option,function (response) {
                            if(response.code=='0'){
                                var url = '/iWork/sys/logout';
                                if (response.data != null && response.data != '')
                                    url = response.data;
                                S_layer.success('解散当前组织成功，系统将跳转至登录界面','提示',function(){
                                    window.location.href = rootPath+url;
                                })
                            }else {
                                S_layer.error(response.info);
                                return false;
                            }

                        })

                    }
                }

            },function(layero,index,dialogEle){//加载html后触发
                that.changeAdmin_validate();
            });
        }
        ,changeAdmin_validate:function(){
            var that = this;
            $('form.teamDissolutionOBox').validate({
                rules: {
                    password:"required"
                },
                messages: {
                    password:"请输入当前用户密码!"
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
