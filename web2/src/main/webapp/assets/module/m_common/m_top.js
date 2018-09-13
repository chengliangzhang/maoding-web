/**
 * Created by veata on 2017/02/14.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_top",
        defaults = {
            data: null
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
            this.initHtml();
        },
        initHtml: function () {
            var that = this;

            //请求基础数据
            var option = {};
            option.url = restApi.url_getCurrUserOfWork;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    var $data = {};
                    $data.rootPath = window.rootPath;
                    if (response.data != null) {
                        $data.companyFilePath = response.data.companyInfo != null ? response.data.companyInfo.filePath : '';
                        $data.userInfo = response.data.userInfo != null ? response.data.userInfo : {};
                        $data.orgList = response.data.orgList;
                        $data.companyInfo = response.data.companyInfo != null ? response.data.companyInfo : {};
                        $data.companyUserInfo = response.data.companyUser != null ? response.data.companyUser : {};
                        $data.fastdfsUrl = response.data.fastdfsUrl;
                        $data.roleCodes = response.data.roleCodes;
                        $data.fileCenterUrl = response.data.fileCenterUrl;
                        $data.enterpriseUrl = response.data.enterpriseUrl;
                        $data.adminFlag = response.data.adminFlag;
                        $data.isAuth = response.data.isAuth;
                        window.adminFlag = $data.adminFlag;
                        var globalData = {
                            currentCompanyId : $data.companyInfo.id,
                            currentUserId : $data.userInfo.id,
                            currentCompanyUserId : $data.companyUserInfo.id,
                            currentRoleCodes : $data.roleCodes==null?'':$data.roleCodes
                        };
                        that.globalVariables(globalData);

                        if (isNullOrBlank(response.data.unReadMessage) || response.data.unReadMessage === 0)
                            $data.unReadMessage = '';
                        else
                            $data.unReadMessage = response.data.unReadMessage;

                        if (isNullOrBlank(response.data.unReadNotice) || response.data.unReadNotice === 0)
                            $data.unReadNotice = '';
                        else
                            $data.unReadNotice = response.data.unReadNotice;

                        if (isNullOrBlank($data.userInfo.imgUrl))
                            $data.userInfo.imgUrl = window.rootPath + '/assets/img/head_default.png';
                        else
                            $data.userInfo.imgUrl = $data.userInfo.imgUrl;


                        var data = {};
                        data.rootPath = window.rootPath;
                        $.extend(data, $data);
                        var html = template('m_common/m_top', data);
                        $('#m_top').html(html);

                        rolesControl();
                        that.bindActionClick();
                        $.each($('#top-nav li'), function (i, o) {
                            var $el = $(o);
                            if ($el.data('nav') === currentNav)
                                $el.addClass('active');
                        });

                        var oLi=$('#m_top_orgList');
                        if(oLi.find('li').length==0){
                            oLi.prev().find('.caret').remove();
                            oLi.remove()
                        }
                    }

                    that.renderToolTip();

                } else {
                    S_layer.error(response.info);
                }
            });


        }
        //渲染ToolTip
        , renderToolTip: function () {
            $('.tooltip-demo').tooltip({
                selector: '[data-toggle=tooltip]',
                container: 'body',
                trigger: 'hover'
            });
        },
        //更改组织全局变量
        globalVariables:function (data) {
            window.currentCompanyId = data.currentCompanyId;
            window.currentUserId = data.currentUserId;
            window.currentCompanyUserId = data.currentCompanyUserId;
            window.currentRoleCodes = data.currentRoleCodes;
        }
        , switchOrg: function (orgId) {
            var option = {};
            option.url = restApi.url_switchCompany + '/' + orgId;
            m_ajax.getJson(option, function (response) {
                if (response.code == '0') {
                    window.location.href = window.rootPath + '/iWork/home/workbench';
                } else {
                    S_layer.error(response.info);
                }
            })
        }
        //事件绑定
        , bindActionClick: function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click', function (e) {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction) {
                    case 'switchOrg'://切换组织
                        var companyInfoId = window.currentCompanyId;
                        var orgId = $(this).attr('org-id');
                        if (orgId != companyInfoId) {
                            that.switchOrg(orgId);
                        }
                        return false;
                        break;
                    case 'messageCenter'://消息界面
                        location.hash = '/messageCenter';
                        $('#unReadMessageCount').html('');
                        break;
                    case 'backstageMgt'://后台管理

                        if(window.currentRoleCodes.indexOf('com_enterprise_edit')>-1 || window.currentRoleCodes.indexOf('sys_enterprise_logout')>-1){//具有组织编辑权限
                            location.hash = '/backstageMgt/orgInfo';
                        }else if(window.currentRoleCodes.indexOf('hr_org_set')>-1 || window.currentRoleCodes.indexOf('hr_employee')>-1 || window.currentRoleCodes.indexOf('org_data_import')>-1 || window.currentRoleCodes.indexOf('org_partner')>-1){//具有组织架构权限
                            location.hash = '/backstageMgt/organizational';
                        }else if(window.currentRoleCodes.indexOf('sys_role_permission')>-1 || window.adminFlag==1){//具有权限配置权限
                            location.hash = '/backstageMgt/organizational';
                        }else if(window.currentRoleCodes.indexOf('sys_role_auth')>-1){//具有企业认证权限
                            location.hash = '/backstageMgt/enterpriseCertification';
                        }else if(window.currentRoleCodes.indexOf('data_import')>-1){//具有历史导入
                            location.hash = '/backstageMgt/historicalDataImport';
                        }
                        break;
                    case 'personalSettings'://个人设置
                        location.hash = '/personalSettings';
                        break;
                    case 'announcement'://公告
                        location.hash = '/announcement';
                        $('#unReadNoticeCount').html('');
                        break;
                    case 'createOrg'://创建组织
                        location.hash = '/createOrg';
                        break;
                    /*case 'financeSettings'://财务设置
                        var option = {};
                        option.$isFirstEnter = true;
                        $('#content-right').m_financeSettings_menu(option);
                        that.dealMenuShowOrHide(0);
                        break;*/
                }
            })
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
