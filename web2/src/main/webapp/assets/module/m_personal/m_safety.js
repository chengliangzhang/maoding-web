/**
 * 个人中心－安全设置
 * Created by wrb on 2017/03/01.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_safety",
        defaults = {
            userDto: null//用户信息数据(当从外传入，则不需要重新请求)
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
            this._getUserInfo();
            this._bindActionClick();
        },
        //渲染页面
        _renderHtml: function (data) {
            var that = this;

            var html = template('m_personal/m_safety', {userDto:data});

            $(that.element).html(html);
        },
        //获取用户信息
        _getUserInfo: function () {
            var that = this;
            var userDto = that.settings.userDto;
            if (userDto === void 0 || userDto === null) {
                var option = {};
                option.url = restApi.url_userInfo;
                m_ajax.get(option, function (response) {
                    if (response.code === '0') {
                        that.settings.userDto=response.data;
                        that._renderHtml(response.data)
                    } else {
                        S_layer.error(response.info);
                    }
                });
            } else {
                that._renderHtml(userDto)
            }
        }
        //打开绑定邮箱弹窗
        ,_openBindEmailDialog:function () {
            S_layer.dialog({
                title: '绑定邮箱',
                area : '750px',
                content:html,
                cancelText:'关闭',
                cancel:function () {

                }

            },function(layero,index,dialogEle){//加载html后触发
                var options = {};
                options.sendEmailCallBack = function () {
                    S_layer.close($('.bindEmailBox'))
                };
                $(dialogEle).m_bindEmail(options);
            });
        }
        //事件绑定
        ,_bindActionClick:function () {

            var that = this;

            $(that.element).find('a[data-action]').on('click',function () {

                var dataAction = $(this).attr('data-action');

                if(dataAction=='bindEmail'){//绑定邮箱

                    that._openBindEmailDialog();

                }else if(dataAction=='bindPhone'){//绑定手机

                    $('body').m_bindPhone();

                }else if(dataAction=='changePwd'){//修改密码

                    $('body').m_uptPassword();//打开修改密码弹窗
                }
                return false;
            });
        }

    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            new Plugin(this, options);
        });
    };

})(jQuery, window, document);
