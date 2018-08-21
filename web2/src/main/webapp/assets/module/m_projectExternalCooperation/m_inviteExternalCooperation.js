/**
 * 邀请人员
 * Created by wrb on 2016/12/17.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_inviteExternalCooperation",
        defaults = {
            title: '',
            inivteUserUrl: '',
            isDailog: true,
            inviteType:3, //外部合作
            projectId:null,
            saveCallBack:null//操作完成回滚事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
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
        //初始化数据并加载模板
        , initHtmlData: function (callBack) {
            var that = this;
            if (that.settings.isDailog) {//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title || '邀请合作设计组织',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '460',
                    tPadding: '0px',
                    url: rootPath + '/assets/module/m_common/m_dialog.html'
                }, function (d) {//加载html后触发

                    that.element = 'div[id="content:' + d.id + '"] .dialogOBox';
                    var html = template('m_projectExternalCooperation/m_inviteExternalCooperation', {});
                    $(that.element).html(html);
                    $('.inviteCorpBox').closest('.dialogOBox').css('overflow','inherit');
                    that.bindSendMessage();
                });
            } else {//不以弹窗编辑

                var html = template('m_projectExternalCooperation/m_inviteExternalCooperation', {});
                $(that.element).html(html);
                that.bindSendMessage();
            }
        }
        //按钮事件绑定
        , bindSendMessage: function () {
            var that = this;
            $('a[data-action="sendMessage"]').click(function (e) {
                var $btn=$(this);
                var $input=$('.inviteCorpBox').find('input[name="bPartnerPhone"]:eq(0)');
                var phone=$input.val();
                var pattern = /^1\d{10}$/;
                if(isNullOrBlank(phone)||!pattern.test(phone))
                {
                    S_dialog.tips('请输入11位有效手机号码');
                    return false;
                }

                $btn.m_popover({
                    popoverStyle:'width:250px;',
                    placement: 'top',
                    content: template('m_common/m_popover_confirm', {confirmMsg: '确定要发送邀请给 <strong>'+phone+'</strong> 吗?'}),
                    onSave: function ($popover) {
                        var cellphoneList = [];
                        cellphoneList.push(phone);
                        var option = {};
                        option.url = restApi.url_inviteProjectParent;
                        option.classId = '.inviteBPartnerBox';
                        option.postData = {cellphoneList: cellphoneList,type:that.settings.inviteType,projectId:that.settings.projectId};
                        m_ajax.postJson(option, function (response) {
                            if (response.code == '0') {
                                S_dialog.tips("邀请外部合作伙伴短信已发送");
                                S_dialog.close($(e.target));
                                if(that.settings.saveCallBack!=null){
                                    that.settings.saveCallBack();
                                }
                            } else {
                                S_dialog.error(response.info);
                            }
                        });
                    }
                }, true);
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
