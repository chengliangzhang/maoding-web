/**
 * Created by Wuwq on 2017/3/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_projectMember",
        defaults = {
            $projectId: null,
            $projectName:null
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            //that._bindPage();
            that._renderHtml();
        },
        _renderHtml: function () {
            var that = this;
            var option = {};
            option.classId = that.element;
            option.url = restApi.url_getProjectParticipants + '/' + that.settings.$projectId;
            m_ajax.getJson(option, function (response) {
                if (response.code == '0') {
                    var parts = response.data;
                    var html = template('m_projectMember/m_projectMember', {
                        parts: parts,
                        rootpath: window.rootPath,
                        currentCompanyUserId: window.currentCompanyUserId,
                        currentCompanyId:window.currentCompanyId,
                        currentUserId : window.currentUserId,
                        projectName:that.settings.$projectName,
                        isOrgManager : window.currentRoleCodes!=null && window.currentRoleCodes.indexOf('sys_enterprise_logout')>-1?1:0//是否是当前组织企业负责人
                    });
                    $(that.element).html(html);
                    that._bindBtn();
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        , _bindBtn: function () {
            var that = this;
            $(that.element).find('a[data-action]').click(function (event) {//移交经营负责人与项目负责人按钮
                var $btn=$(this);
                var action = $(this).attr('data-action');
                var companyId = $(this).attr('data-companyId');//当前要获取的项目立项组织的index
                var options = {};

                options.url = restApi.url_getOrgTree;
                options.isASingleSelectUser = true;
                options.delSelectedUserCallback = function () {

                };
                if (action == 'changeOperatorPerson') {
                    var operatorPersonId = $(this).attr('data-id'),userName=$(this).attr('data-user-name');
                    options.title = '选择经营负责人';
                    options.selectedUserList = [{
                        id:operatorPersonId,
                        userName:userName
                    }];
                    options.selectUserCallback = function (data, event) {
                        data.type = 1;
                        var targetUser='<strong style="color:red;margin:0 3px;">'+data.userName+'</strong>';
                        S_dialog.confirm('确定将经营负责人更换为'+targetUser+'？',function(){
                            that._postManagerChange(data,companyId,event);
                        },function(){
                            //S_dialog.close($(event));
                        });
                    }
                } else if (action == 'changeManagerPerson') {
                    var designPersonId = $(this).attr('data-id'),userName=$(this).attr('data-user-name');
                    options.title = '选择设计负责人';
                    options.selectedUserList = [{
                        id:designPersonId,
                        userName:userName
                    }];
                    options.selectUserCallback = function (data, event) {
                        data.type = 2;
                        var targetUser='<strong style="color:red;margin:0 3px;">'+data.userName+'</strong>';
                        S_dialog.confirm('确定将设计负责人更换为'+targetUser+'？', function () {
                            that._postManagerChange(data, companyId, event);
                        }, function () {
                            //S_dialog.close($(event));
                        });
                    }
                }
                $('body').m_orgByTree(options);
                return false;
            });
        }
        //移交经营负责人或项目负责人的请求
        , _postManagerChange: function (data, companyId, event) {
            var that = this;
            var option = {};
            option.url = restApi.url_updateProjectManager;
            option.postData = {};
            option.postData.projectId = that.settings.$projectId;
            option.postData.companyId = companyId;
            option.postData.type = data.type;
            option.postData.companyUserId = data.companyUserId;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_dialog.close($(event));
                    that._renderHtml();
                    if(data.type==1){//移交经营负责人会影响项目权限的编辑更改，需要刷新数据
                        that._refreshMenu();
                    }
                    S_toastr.success('保存成功！');
                } else {
                    S_dialog.error(response.info);
                }
            })
        }
        //刷新当前菜单
        , _refreshMenu: function () {
            var that=this,option = {};
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            //option.$type = 'projectMember';
            $('#content-right').m_projectMenu(option);
        }
    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            new Plugin(this, options);
        });
    };

})(jQuery, window, document);
