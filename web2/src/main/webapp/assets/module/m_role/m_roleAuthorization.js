/**
 *
 * Created by veata on 2016/12/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_roleAuthorization",
        defaults = {
            user: null,
            userId: null,
            dialogWidth: null,
            isDialogShow: null,//判断是否是打开弹窗方式显示，当为1时则为查看人员权限总览（弹窗），为2时是人员权限预览（弹窗），为3时是编辑权限里的人员权限编辑（弹窗），为null时则是后台的编辑权限（非弹窗）
            dialogTitle: null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._element = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData();
        },
        initHtmlData: function () {
            var that = this;
            var userIds = '';
            var options = {};
            options.url = restApi.url_getRolePermissionByUser + '/' + that.settings.userId;
            m_ajax.get(options, function (response) {
                if (response.code == '0') {
                    var rolePermissions = response.data;
                        S_dialog.dialog({
                                title: that.settings.dialogTitle || ('人员权限总览' + that.settings.user),
                                contentEle: 'dialogOBox',
                                lock: 3,
                                width: that.settings.dialogWidth || '700',
                                minHeight: '400',
                                tPadding: '0px',
                                url: rootPath + '/assets/module/m_common/m_dialog.html',
                                cancelText: '关闭',
                                cancel: function () {
                                },
                                okText: '保存',
                                ok: function () {

                                    var errorTip = '';
                                    $('.m-roleList').find('tr td[data-type="roleUsersTd"]').each(function () {

                                        var $this = $(this);
                                        var pCode = $this.parents('tbody').attr('p-code');
                                        var code = $this.parent().find('td[p-code]:first').attr('p-code');
                                        var name = $this.parent().find('span[data-type="roleName"]:last').text();
                                        var len = $this.find('.user-name').length;
                                        var isChecked = $('.m-roleAuthorization').find('td[p-code="'+code+'"]').next().find('input[name="userPermission"]').is(':checked');
                                        if(pCode=='OrgManager'||pCode=='SystemManager'){
                                            return true;
                                        }
                                        if(len==1 && isChecked==false){
                                            var userId = $this.find('.user-span').attr('userid');
                                            if(userId==that.settings.userId) {
                                                errorTip = '“' + name + '”角色必须保留至少一个成员！';
                                                return false;
                                            }
                                        }
                                    });
                                    if(errorTip!=''){
                                        S_toastr.warning(errorTip);
                                        return false;
                                    }
                                    return that.saveRoleAuthorization(rolePermissions);
                                }
                            }
                            , function (d) {//加载html后触发
                                that._element = 'div[id="content:' + d.id + '"] .dialogOBox';
                                var $data = {};
                                $data.rolePermissions = rolePermissions;
                                var html = template('m_role/m_roleAuthorization', $data);
                                $(that._element).html(html);
                                that.bindChoseClick();
                                $(that._element).find('div.i-checks input[type="checkbox"]').each(function () {
                                    var isChecked = $(this).attr('isCheck');
                                    if (isChecked == 1) {
                                        $(this).iCheck('check');
                                    }
                                });
                            });
                } else {
                    S_dialog.error(response.info);
                }

            });

        }
        //给人员权限选择里的‘全选’，‘全不选’，‘恢复原始’等按钮绑定点击事件
        , bindChoseClick: function () {
            var that = this;
            $(that._element).find('a[data-action],td[data-action]').bind('click', function (event) {
                var action = $(this).attr('data-action');
                if (action == 'chooseAll') {//点击'全选'
                    $(that._element).find('div.i-checks input[type="checkbox"]').iCheck('check');
                } else if (action == 'chooseNothing') {//点击'全不选'
                    $(that._element).find('div.i-checks input[type="checkbox"]').iCheck('uncheck');
                } else if (action == 'recoveryChoice') {//点击'恢复原始'
                    $(that._element).find('div.i-checks input[type="checkbox"]').each(function () {
                        var isChecked = $(this).attr('isCheck');
                        if (isChecked == 1) {
                            $(this).iCheck('check');
                        } else {
                            $(this).iCheck('uncheck');
                        }
                    });
                } else if (action == 'chooseAllRoleItem') {//点击单条权限的'全选'
                    $(this).closest('tbody').find('div.i-checks input[type="checkbox"]').iCheck('check');
                } else if (action == 'delAllRoleItem') {//点击单条权限的'全不选'
                    $(this).closest('tbody').find('div.i-checks input[type="checkbox"]').iCheck('uncheck');;
                } else if (action == 'chooseUserPermission') {//点击checkbox所在的td进行勾选checkbox
                    var ischeck = $(this).find('div.i-checks input[type="checkbox"]').iCheck('check');
                    $(this).find('div.i-checks input[type="checkbox"]').iCheck(ischeck ? 'uncheck' : 'check');
                }
            });
        }
        //保存角色权限编辑
        ,saveRoleAuthorization:function(rolePermissions) {
            var that = this;
            var userId = that.settings.userId;
            var option = {};
            option.postData = {};
            option.postData.permissionIds = [];
            $('.fa-check').each(function () {
                var id = $(this).attr('permission-id');
                option.postData.permissionIds.push(id);
            });
            option.postData.userId = userId;
            option.url = restApi.url_userPermission + '/' + userId;
            if ($('div.i-checks input[type="checkbox"]:checked').length > 0) {
                $('div.i-checks input[type="checkbox"]:checked').each(function () {
                    option.postData.permissionIds.push($(this).attr('permission-id'));
                });
            }
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('保存成功！');
                    var $data = {};
                    /*$(that.element).m_roleList($data);*/
                    that._refreshMenu();
                } else {
                    S_dialog.error(response.info);
                }
            });
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
