/**
 * 项目信息－生产安排组织
 * Created by wrb on 2017/2/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_production_org",
        defaults = {
            $projectId: null,
            partB: null,
            $productionOrgList: null,
            $projectCreateBy:null,//立项人用户ID
            $projectCompanyId:null//立项人组织
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._initHtml();
        },
        //初始化数据,生成html
        _initHtml: function () {
            var that = this;
            var $data = {};
            $data.departBCompany = that.settings.partB;//获取乙方组织
            $data.isRoleCompany = false;//判断是立项方组织或者乙方组织
            $data.productionOrgList = that.settings.$productionOrgList;
            $data.currentCompanyId = window.currentCompanyId;
            $data.currentCompanyUserId = window.currentCompanyUserId;
            $data.isShowTips = 0;
            if($data.productionOrgList!=null && $data.productionOrgList.length>0){
                for (var i=0;i<$data.productionOrgList.length;i++){
                    if ($data.productionOrgList[0].type == 1) {
                        var flag = $data.productionOrgList[0].id == window.currentCompanyId || ($data.departBCompany&&$data.departBCompany.id == that._currentCompanyId);
                        $data.isRoleCompany = flag ? true : false;
                    }
                    //合作组织是当前组织且经营负责人是当前人且未设置设计负责人
                    if($data.productionOrgList[i].id==$data.currentCompanyId
                        && $data.productionOrgList[i].operatorPersonId==$data.currentCompanyUserId
                        && $data.productionOrgList[i].designPersonId==null){
                        $data.isShowTips = 1;
                        break;
                    }
                }
            }
            $data.projectCreateBy = that.settings.$projectCreateBy;
            $data.projectCompanyId = that.settings.$projectCompanyId;
            $data.currentUserId = window.currentUserId;
            $data.isOrgManager = window.currentRoleCodes!=null && window.currentRoleCodes.indexOf('sys_enterprise_logout')>-1?1:0;//是否是当前组织企业负责人
            var html = template('m_production/m_production_org', $data);
            $(that.element).html(html);
            //$(that.element).find('.tree').treegrid();
            //$(that.element).find('.no-treegrid-expander span.treegrid-expander-expanded').unbind();
            stringCtrl('companyName');
            that._bindClickFun();
        },
        _bindClickFun: function () {
            var that = this;
            $(that.element).find('a[data-toggle="tooltip"]').tooltip();
            $(that.element).find('a[data-action]').on('click', function (event) {//移交经营负责人与项目负责人按钮
                var action = $(this).attr('data-action');
                var companyId = $(this).attr('data-company-id');//当前要获取的项目立项组织的index
                var options = {};

                options.url = restApi.url_getOrgTree;
                options.isASingleSelectUser = true;
                options.delSelectedUserCallback = function () {
                    $(that.element).find('input[name="chooseManager"]').removeAttr('companyUserId');
                    $(that.element).find('input[name="chooseManager"]').val('');
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
                        S_dialog.confirm('确定将经营负责人更换为'+targetUser+'？', function () {
                            that._postManagerChange(data, companyId, event);
                        }, function () {
                            //S_dialog.close($(event));
                        });
                    }
                } else if (action == 'changeManagerPerson') {

                    var isFirstSetDesign =  $.trim($(this).text())=='未设置'?true:false;
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

                        data.isFirstSetDesign = isFirstSetDesign;
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
            if(data.isFirstSetDesign!=null && data.isFirstSetDesign==false){
                option.url = restApi.url_transferTaskDesigner;
            }else{
                option.url = restApi.url_updateProjectManager;
            }
            option.postData = {};
            option.postData.projectId = that.settings.$projectId;
            option.postData.companyId = companyId;
            option.postData.type = data.type;
            option.postData.companyUserId = data.companyUserId;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_dialog.close($(event));
                    S_toastr.success('保存成功！');
                    if(data.type==1){//移交经营负责人会影响项目权限的编辑更改，需要刷新数据
                        that._refreshMenu();
                    }
                    that._refresh();
                } else {
                    S_dialog.error(response.info);
                }
            })
        }
        //刷新当前菜单
        , _refreshMenu: function () {
            var that=this,option = {};
            option.$projectId = that.settings.$projectId;
            option.$type = 'productionArrangement';
            $('#project_menu').m_projectMenu(option);
        }
        //刷新当前界面
        , _refresh: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            $('#project_detail').m_production(option,true);
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
