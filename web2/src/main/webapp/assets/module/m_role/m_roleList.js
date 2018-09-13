/**
 *
 * Created by veata on 2016/12/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_roleList",
        defaults = {
            getDataUrl: null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._orgManagerUserId = null;//企业负责人用户ID
        this._sysManagerUserId = null;//系统管理员用户ID
        this._orgManagerCompanyUserId = null;//企业负责人员工ID
        this._sysManagerCompanyUserId = null;//系统管理员员工ID
        this._orgManagerUserName = null;//企业负责人员工姓名
        this._sysManagerUserName = null;//系统管理员员工姓名
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData();
        },
        initHtmlData: function (callBack) {
            var that = this;
            var userIds = '';
            var options = {};
            options.url = that.settings.getDataUrl || restApi.url_getRoleUserPermission;
            m_ajax.get(options, function (response) {
                if (response.code == '0') {
                    var rolePermissions = response.data;
                    var $data = {};
                    $data.rolePermissions = rolePermissions;
                    $data.currentCompanyUserId = window.currentCompanyUserId;
                    var html = template('m_role/m_roleList', $data);
                    $(that.element).html(html);

                    var orgManagerObj = $(that.element).find('span[class^="user-name"][p-code="sys_enterprise_logout"]').eq(0);
                    var sysManagerObj = $(that.element).find('span[class^="user-name"][p-code="background_management"]').eq(0);
                    //$(that.element).find('#orgManagerA').text(orgManagerObj.text());
                    //$(that.element).find('#sysManagerA').text(sysManagerObj.text());
                    that._orgManagerUserId = orgManagerObj.attr('userId');
                    that._sysManagerUserId = sysManagerObj.attr('userId');
                    that._orgManagerCompanyUserId = orgManagerObj.attr('data-company-user-id');
                    that._sysManagerCompanyUserId = sysManagerObj.attr('data-company-user-id');
                    that._orgManagerUserName = orgManagerObj.text();
                    that._sysManagerUserName = sysManagerObj.text();

                    if(that._sysManagerUserId == window.currentUserId){//移交管理员
                        $(that.element).find('#sysManagerA').next().attr('title','移交管理员');
                    }else if(that._sysManagerUserId != window.currentUserId){//指定管理员
                        $(that.element).find('#sysManagerA').next().attr('title','指定管理员');
                    }
                    if(that._orgManagerUserId == window.currentUserId){//移交企业负责人
                        $(that.element).find('#orgManagerA').next().attr('title','移交企业负责人');
                    }else if(that._orgManagerUserId != window.currentUserId){//指定企业负责人
                        $(that.element).find('#orgManagerA').next().attr('title','指定企业负责人');
                    }

                    that.bindMouseOverFun();
                    that.bindAddUserClick();
                } else {
                    S_layer.error(response.info);
                }

            });

        }
        //给每个td以及含有 span.user-span的标签绑定鼠标经过效果，鼠标经过时即显示，鼠标移出即隐藏
        , bindMouseOverFun: function () {
            var that = this;
            $(that.element).find('td.vmiddle').bind('mouseover', function () {//鼠标进入时
                if ($(this).find('a[data-action="addMemberBtn"]').is('.hide')) {
                    $(this).find('a[data-action="addMemberBtn"]').removeClass('hide');
                }

            }).bind('mouseout', function () {//鼠标滑出时
                if (!($(this).find('a[data-action="addMemberBtn"]').is('.hide'))) {
                    $(this).find('a[data-action="addMemberBtn"]').addClass('hide');
                }
            });
            $(that.element).find('td span.user-span').bind('mouseover', function () {//鼠标进入时
                $(this).css('background-color', '#f5f5f5');
                $(this).find('span.user-del').removeClass('hide');

            }).bind('mouseout', function () {//鼠标滑出时
                $(this).css('background-color', '#fff');
                $(this).find('span.user-del').addClass('hide');
            });
        }
        //给每个添加成员按钮绑定点击事件
        , bindAddUserClick: function () {

            var that = this;
            $(that.element).find('a[data-action],span[data-action]').bind('click', function () {
                var $this = $(this);
                var action = $this.attr('data-action');
                var isOrgManager = $this.closest('TR').attr('role-code');
                if (action == 'addMemberBtn') {//权限组(权限表)－添加人员按钮
                    var options = {};
                    options.url = restApi.url_getOrgTreeForSearch;
                    options.isShowChoseUserList = 1;
                    options.selectedUserList = [];
                    options.chosedUserIds = '';
                    options.selectUserCallback = function (data, event) {//选择人员回调方法
                        if(isOrgManager=='OrgManager'){
                            that._saveUserRolePermission(data,$this);
                        }
                    };
                    if(isOrgManager!='OrgManager') {
                        options.saveCallback = function (data) {//点击保存回调方法
                            that._saveUserRolePermission(data,$this);
                        };
                    }else{
                        options.isASingleSelectUser = true;
                        options.title = "选择人员";
                    }
                    $(that.element).m_orgByTree(options);
                } else if (action == 'deleteRoleUser') {//删除角色人员
                    that._delUserRolePermission($this)
                } else if (action == 'chooseRoleByDialog') {//个人权限保存
                    that._saveUerRolePermissionByUser($this);
                } /*else if (action == 'transferAdministrator') {//移交管理员
                    that._changeManager($this);

                }*/else if (action == 'transferOrgManager') {//移交或指定企业负责人
                    that._changeManager($this,2);

                }else if (action == 'transferSysManager') {//移交或指定管理员
                    that._changeManager($this,1);

                }
            });
        }

        //保存用户角色权限
        ,_saveUserRolePermission:function (data,$this) {
            var that = this,option = {};
            var isParent = $this.parents('td').is('.pTarget');
            var roleId = $this.parent().parent().attr('id');
            var isOrgManager = $this.closest('TR').attr('role-code');
            var selectedUserList = data.selectedUserList;
            option.classId = '#content-right';
            option.postData = {};
            if (isParent) {
                option.postData.roleId = roleId;
                option.url = restApi.url_roleUser;
            } else {
                option.postData.permissionId = roleId;
                option.url = restApi.url_saveUserPermission + '/' + roleId;
            }
            option.postData.userIds = [];
            $.each(selectedUserList, function (i, item) {
                option.postData.userIds.push(item.userId);
            });
            if(isOrgManager=='OrgManager'){

                var delUserId = $this.closest('TBODY[p-code="OrgManager"]').find('tr .user-span').attr('userid');//暂时只有一种，后续多种此处需要作修改
                option.postData.deleteUserIds = [];
                option.postData.deleteUserIds.push(delUserId);
            }
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('保存成功！');
                    S_layer.close($('div[id="' + data.id + '"]'));
                    var $data = {};
                    /*$(that.element).m_roleList($data);*/
                    that._refreshMenu();
                    //location.hash = '/backstageMgt/permissionSettings';
                } else {
                    S_layer.error(response.info);
                }
            });
        }

        //删除用户角色权限
        ,_delUserRolePermission:function ($this) {
            var that = this;
            var userId = $this.parents('.user-span').attr('userId');
            var roleId = $this.parent().attr('roleId');
            var pcode=$this.closest('td').attr('p-code');

            //if(pcode==='project_manager'||pcode==='design_manager'||pcode==='project_charge_manage'){
                if($this.closest('td').find('.user-name').length===1){
                    S_toastr.warning("该角色必须保留至少一个成员");
                    return;
                }
            //}

            $this.m_popConfirm({
                content: '您确定要删除吗？',
                hasClick: true,
                onYes: function (el) {
                    var $el = $(el);
                    var option = {};
                    option.url = restApi.url_deleteUserPermission + '/' + roleId + '/' + userId;
                    m_ajax.get(option, function (response) {
                        if (response.code == '0') {
                            S_toastr.success('删除成功！');
                            var $data = {};
                            /*$(that.element).m_roleList($data);*/
                            that._refreshMenu();
                        } else {
                            S_layer.error(response.info);
                        }
                    });
                }
            });
        }

        //个人权限保存
        ,_saveUerRolePermissionByUser:function ($this) {
            var data = {},that = this;
            var userId = $this.parent().attr('userId');
            var user = $this.text();
            $(that.element).m_roleAuthorization({user:user,userId:userId});

        }

        //移交管理员
        ,_changeManager:function (obj,type) {
            var options = {},that = this;
            options.title = '选择管理员';

            var userId = '',userName='';
            if(type==1){
                userId = that._sysManagerCompanyUserId;
                userName = that._sysManagerUserName;
            }else{
                userId = that._orgManagerCompanyUserId;
                userName = that._orgManagerUserName;
                options.title = '选择企业负责人';
            }
            if (userId != null && userId != '') {
                options.selectedUserList = [{
                    id: userId,
                    userName: userName
                }];
            }
            options.isASingleSelectUser = true;
            options.cancelText = '关闭';
            options.isOkSave = false;
            options.selectUserCallback = function (data) {
                var option = {};
                option.title = '提示';
                option.adminInfo = {};
                option.companyId = window.currentCompanyId;
                option.adminInfo.userName = data.userName;
                option.adminInfo.userId = data.userId;
                option.type = type;
                option.oldSysManagerUserId = that._sysManagerUserId;
                option.oldOrgManagerUserId = that._orgManagerUserId;
                option.saveCallback = function () {
                    layer.close(data.id);
                };

                $('body').m_changeManager(option);
            };
            $('body').m_orgByTree(options);
        }
        //刷新左菜单(权限有变可能导致左菜单变化)
        ,_refreshMenu:function () {
            $('#content-right').m_org_menu({
                $dataAction:'permissionSettings'
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
