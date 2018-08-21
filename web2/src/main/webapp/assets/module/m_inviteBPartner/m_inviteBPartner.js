/**
 * Created by Wuwq on 2017/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_inviteBPartner",
        defaults = {
            a_companyName: null,
            a_systemManager: null,
            cellphone: null,
            userId: null
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._render();
        }
        //渲染首屏
        , _render: function () {
            var that = this;

            var option = {};
            option.url = restApi.url_getCompanyByInviteUrl + '/' + $('#invitedId').val();
            option.classId = '.div-invite-bPaner';
            option.postData = {};
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    that.settings.a_companyName = response.data.companyName;
                    that.settings.a_systemManager = response.data.systemManager;

                    var data = {};
                    data.companyName = response.data.companyName;
                    data.systemManager = response.data.systemManager;
                    data.cellphone = response.data.cellphone;
                    data.filePath = response.data.filePath;
                    var html = template('m_inviteBPartner/m_inviteBPartner', data);
                    $(that.element).html(html);
                    that._bindKeyDownEnter();
                    that._bindAction();
                } else {
                    S_dialog.tips(response.info);
                }
            });
        }
        //回车键
        , _bindKeyDownEnter: function () {
            var that = this;
            $('#cellphone').keydown(function (e) {
                if (event.keyCode == 13) {
                    that._verify();
                    stopPropagation(e);
                    preventDefault(e);
                    return false;
                }
            });
        }
        , _bindAction: function () {
            var that = this;
            $(that.element).find('.btn-ok:eq(0)').click(function () {
                that._verify();
            });
        }
        //验证手机号
        , _verify: function () {
            var that = this;
            var invitedId = $('#invitedId').val();
            that.settings.cellphone = $('#cellphone').val();

            var option = {};
            option.url = restApi.url_verifyIdentityForBPartner;
            option.classId = '.div-invite-bPaner';
            option.postData = {id: invitedId, cellphone: that.settings.cellphone};
            m_ajax.postJson(option, function (response) {
                if (response.code === '0') {
                    that._renderOrg();
                } else {
                    S_dialog.tips(response.info);
                }
            });
        }
        //渲染团队选择页面
        , _renderOrg: function () {
            var that = this;

            var invitedId = $('#invitedId').val();

            var option = {};
            option.url = restApi.url_getCompanyPrincipal;
            option.classId = '.div-invite-bPaner';
            option.postData = {id: invitedId, cellphone: that.settings.cellphone};
            m_ajax.postJson(option, function (response) {
                if (response.code === '0') {
                    if (response.data.companyList) {
                        if (response.data.companyList.length > 0) {
                            var html = template('m_inviteBPartner/m_inviteBPartner_org', {companyList: response.data.companyList,a_companyName:that.settings.a_companyName});
                            $(that.element).html(html);
                            that._bindCreateOrg(response.data);
                            that._bindSelectOrg();
                        } else {
                            var html = template('m_inviteBPartner/m_inviteBPartner_org_hasNo', {});
                            $(that.element).html(html);
                            that._bindCreateOrg(response.data);
                        }
                    }
                } else {
                    S_dialog.tips(response.info);
                }
            });
        }
        //创建团队
        , _bindCreateOrg: function (data) {
            var that = this;
            $(that.element).find('a[data-action="createOrg"]').click(function (e) {
                if (data.userId === void 0 || data.userId === null) {
                    var html = template('m_inviteBPartner/m_inviteBPartner_create_hasNo', {a_companyName:that.settings.a_companyName});
                    $(that.element).html(html);
                    that._bindCreateOrgAndAccountSubmit();
                    that.createOrgOBox_validate();
                } else {
                    that.settings.userId = data.userId;
                    var html = template('m_inviteBPartner/m_inviteBPartner_create_has', {a_companyName:that.settings.a_companyName});
                    $(that.element).html(html);
                    that._bindCreateOrgSubmit();
                }
            });
        }
        , _bindCreateOrgSubmit: function () {
            var that = this;
            var invitedId = $('#invitedId').val();
            $(that.element).find('a[data-action="createOrgSubmit"]').click(function (e) {
                var companyName = $(that.element).find('input[name="companyName"]:eq(0)').val();
                var option = {};
                if($(that.element).find("form.createOrgOBox").valid()) {
                    option.url = restApi.url_createBusinessPartner;
                    option.classId = 'body';
                    option.postData = {
                        userId: that.settings.userId,
                        inviteId: invitedId,
                        cellphone: that.settings.cellphone,
                        companyName: companyName
                    };
                    m_ajax.postJson(option, function (response) {
                        if (response.code === '0') {
                            var html = template('m_inviteBPartner/m_inviteBPartner_success', {
                                companyName: companyName,
                                a_companyName: that.settings.a_companyName
                            });
                            $(that.element).html(html);
                        } else {
                            S_dialog.tips(response.info);
                        }
                    });
                }
            });
        },
        createOrgOBox_validate: function () {//注册的表单验证
            var that = this;
            $(that.element).find("form.createOrgOBox").validate({
                rules: {
                    userName: "required",
                    adminPassword: {
                        required: true,
                        rangelength: [6, 12],
                        checkSpace: true
                    },
                    companyName:"required"

                },
                messages: {
                    userName: "请输入姓名！",
                    adminPassword: {
                        required: '请输入密码！',
                        rangelength: "密码为6-12位！",
                        checkSpace: "密码不应含有空格!"
                    },
                    companyName:"请输入组织名称！"

                },
                errorElement: 'label'
            });
            //密码验证
            $.validator.addMethod("checkSpace", function (value, element) {
                var pattern = /^\S+$/gi;
                return this.optional(element) || pattern.test(value);
            }, "密码不应含有空格!");
        }
        , _bindCreateOrgAndAccountSubmit: function () {
            var that = this;
            var invitedId = $('#invitedId').val();
            $(that.element).find('a[data-action="createOrgAndAccountSubmit"]').click(function (e) {

                var userName = $(that.element).find('input[name="userName"]:eq(0)').val();
                var adminPassword = $(that.element).find('input[name="adminPassword"]:eq(0)').val();
                var companyName = $(that.element).find('input[name="companyName"]:eq(0)').val();

                var option = {};

                if($(that.element).find("form.createOrgOBox").valid()){
                    option.url = restApi.url_createBusinessPartner;
                    option.classId = 'body';
                    option.postData = {
                        inviteId: invitedId,
                        companyName: companyName,
                        cellphone: that.settings.cellphone,
                        userName: userName,
                        adminPassword: adminPassword
                    };
                    m_ajax.postJson(option, function (response) {
                        if (response.code === '0') {
                            S_dialog.tips("操作成功");
                            var html = template('m_inviteBPartner/m_inviteBPartner_success', {
                                companyName: companyName,
                                a_companyName: that.settings.a_companyName
                            });
                            $(that.element).html(html);
                        } else {
                            S_dialog.tips(response.info);
                        }
                    });
                }

            });
        }
        , _bindSelectOrg: function () {
            var that = this;
            var invitedId = $('#invitedId').val();

            $(that.element).find('li[data-action="selectOrg"]').click(function (e) {
                S_dialog.tips('选择无效，该组织'+$(this).attr('data-memo'));
            });

            $(that.element).find('li[data-action="selectOrgApply"]').click(function (e) {
                var companyId = $(this).attr('data-company-id');
                var companyName=$(this).attr('data-company-name');
                $(this).m_popover({
                    placement: 'top',
                    content: template('m_common/m_popover_confirm', {confirmMsg: '确定选择该组织来建立事业合伙人关系吗?'}),
                    onSave: function ($popover) {
                        var option = {};
                        option.url = restApi.url_applayBusinessPartner;
                        option.classId = 'body';
                        option.postData = {
                            inviteId: invitedId,
                            cellphone: that.settings.cellphone,
                            companyId: companyId
                        };
                        m_ajax.postJson(option, function (response) {
                            if (response.code === '0') {
                                S_dialog.tips("操作成功");
                                var html = template('m_inviteBPartner/m_inviteBPartner_success', {
                                    companyName: companyName,
                                    a_companyName: that.settings.a_companyName
                                });
                                $(that.element).html(html);
                            } else {
                                S_dialog.tips(response.info);
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