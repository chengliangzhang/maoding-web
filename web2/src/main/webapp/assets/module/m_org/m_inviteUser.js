/**
 * 邀请人员
 * Created by wrb on 2016/12/17.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_inviteUser",
        defaults = {
            title:'',
            inivteUserUrl:'',
            isDialog:true
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
            var $data = {};
            var url = window.rootPath+'/iWork/sys/shareInvitation/'+window.currentCompanyId+'/'+window.currentUserId;
            $data.inivteUserUrl = url;
            var html = template('m_org/m_inviteUser',$data);
            that.renderDialog(html,function () {
                that.bindActionClick();
            });
        }
        //初始化数据并加载模板
        ,renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑
                S_layer.dialog({
                    title: that.settings.title||'邀请人员',
                    area : '460px',
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
        //复制链接
        ,copyUrl:function () {
            var Url2 = $(".invitePersonelOBox #url");
            Url2.select(); // 选择对象
            document.execCommand("Copy"); // 执行浏览器复制命令
            S_toastr.success('复制成功！')
        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;
            $('.invitePersonelOBox button[data-action]').on('click',function () {
                var dataAction = $(this).attr('data-action');
                if(dataAction=='copyUrl'){//复制链接
                    that.copyUrl();
                    return false;
                }
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
