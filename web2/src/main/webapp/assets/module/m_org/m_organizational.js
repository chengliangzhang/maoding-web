/**
 * 组织－组织架构
 * Created by wrb on 2017/2/23.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_organizational",
        defaults = {};

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;
        this._currTreeObj = null;
        this._selectedCompanyId = null;
        this._treeType = 1;
        this._roleCode = null;

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._roleCode = window.currentRoleCodes;
            var that = this;
            var options={};
            options.url=restApi.url_getToAuditCompanyCount;
            m_ajax.get(options,function (response) {
                if(response.code=='0'){

                    var html = template('m_org/m_organizational', {roleCodes:that._roleCode,toAuditEmpNum:response.data.toAuditEmpNum,rootPath:window.rootPath});
                    $(that.element).html(html);
                    rolesControl();
                    that._initOrgTree();
                    that.bindTreeSwitch();
                    that._bindActionClick();
                    that.treeBoxHeightResize();
                    $(window).resize(function () {
                        var t = setTimeout(function () {
                            that.treeBoxHeightResize();
                            clearTimeout(t);
                        });
                    });
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //树菜单高度自适应
        , treeBoxHeightResize:function () {
            var that = this;
            var pageWrapperH = $('#page-wrapper').css('min-height');
            $(that.element).find('#org-tree-box').css('height',pageWrapperH);
        }
        //初始化人员列表
        , _initOrgUserList: function (orgId) {
            var options = {};
            var that = this;
            var hasHr_employee = that._roleCode.indexOf('hr_org_set')>-1;
            var isCurrentCompany = this._currTreeObj.companyId == window.currentCompanyId ? true : false;
            options.orgId = orgId;
            options.currOrgTreeObj = this._currTreeObj;
            //options.isClick = hasHr_employee && isCurrentCompany && that._treeType===1;
            options.isClick = hasHr_employee && that._treeType===1;
            options.currOrgTreeObj = this._currTreeObj;
            $(this.element).find('#orgUserListBox').m_orgUserList(options);
        }
        //初始化组织树
        , _initOrgTree: function () {//type==1为显示完整组织树并可操作，type！=1为只显示本组织下的树
            var that = this;
            var options = {},type=that._treeType;
            options.isDialog = false;
            options.isGetUserList = false;
            options.treeUrl = type!=1?restApi.url_getOrgStructureTree:restApi.url_getOrgTreeSimple;
            // options.treeUrl = restApi.url_getOrgStructureTreeForSearch;
            options.currentCompanyId = window.currentCompanyId;
            options.treeIconObj = {
                'default': {
                    'icon': 'iconfont rounded icon-zuzhijiagou'
                },
                'independent': {   //独立经营图标
                    'icon': 'fa fa-users'
                },
                'partner': {       //事业合伙人图标
                    'icon': 'iconfont rounded icon-cooperation'
                },
                'partnerContainer': {       //事业合伙人容器图标
                    'icon': 'iconfont rounded icon-cooperation'
                },
                'subCompany': {       //分支机构图标
                    'icon': 'iconfont rounded icon-2fengongsi1'
                },
                'subCompanyContainer': {       //分支机构容器图标
                    'icon': 'iconfont rounded icon-2fengongsi1'
                },
                'company': {         //根节点图标
                    'icon': 'iconfont rounded icon-2fengongsi'
                }
            };
            options.initTreeDataCallBack = function(info){//用于判断info,如果Info为“0”表示不需要显示切换组织树，为“1”表示需要显示
                if(that._treeType ==1 && info && info!=1){
                    $('.treeSwitch a[data-action="treeSwitch"]').remove();
                }
            };
            options.afterOpenCallBack = function (data) {

                if(that._treeType !==1){
                    var html = '<i class="fa fa-flag" role="presentation" style="color: #4765a0;margin:0 5px;"></i>';
                    var id = that._selectedCompanyId;
                    if(id && !($('#organization_treeH li#'+id+'>a#'+id+'_anchor').find('i.fa-flag').length>0)){
                        $('#organization_treeH li#'+id+'>a#'+id+'_anchor').find('.icon-2fengongsi1').remove().end().prepend(html);
                    }
                }
               that.dealTreeIconColorFun(data.node.id);
            };
            options.selectNodeCallBack = function (data,type) {
                that._currTreeObj = data;
                that._initOrgUserList(data.realId);
                $('#organization_treeH .tree-btn').remove();
                $('#organization_treeH .tooltip').remove();
                if(that._treeType!=1){
                    $(that.element).find('.headContent').hide();
                    //给我的组织加上图标标识
                    if(type &&　type == 'ready'){
                        var html = '<i class="fa fa-flag" role="presentation" style="color: #4765a0;margin:0 5px;"></i>';
                        var id = data.id;
                        that._selectedCompanyId = id;
                        if(id && !($('#organization_treeH li#'+id+'>a#'+id+'_anchor').find('i.fa-flag').length>0)) {
                            $('#organization_treeH li#' + id + '>a#' + id + '_anchor').find('.icon-2fengongsi1').remove().end().prepend(html);
                        }
                    }
                    return;
                }else{
                    $(that.element).find('.headContent').show();
                }

                //先给所有操作按钮去除禁用状态，以及绑定点击事件
                $(that.element).find('.headContent a[data-action]').removeAttr('disabled');
                $(that.element).find('.headContent a[data-action="drop_down"]').attr('data-toggle','dropdown');
                that._bindActionClick();
                //当选择节点是本公司时
                if(data.companyId == window.currentCompanyId && data.type != 'depart' ){
                    $(that.element).find('.headContent a[data-action="editDepart"],.headContent a[data-action="deleteSubCompany"]').attr('disabled','disabled');
                    $(that.element).find('.headContent a[data-action="editDepart"],.headContent a[data-action="deleteSubCompany"]').off('click');
                }
                //当选择节点是本公司部门时
                else if(data.companyId == window.currentCompanyId && data.type == 'depart'){
                    if(that._roleCode.indexOf('hr_org_set')>-1){
                        $('#organization_treeH .jstree-clicked').after('<span class="tree-btn" >&nbsp;' +
                            '<a data-toggle="tooltip"data-placement="top" title="编辑部门"><i class="fa fa-pencil"></i></a></span>');
                        $('#organization_treeH .jstree-clicked').parent().find('span.tree-btn a').tooltip();
                        that.bindEditDepartment($('#organization_treeH .jstree-clicked').closest('li'));
                    }
                }
                //当选择节点是分支机构时
                else if(data.isCurrentSubCompany == 1 && data.type == 'subCompany'){
                    if(that._roleCode.indexOf('hr_org_set')>-1){
                        var toolTip='<span class="tree-btn" >&nbsp;<a data-action="deleteSubCompany" data-toggle="tooltip" data-placement="top" title="解除分支机构"><i class="fa fa-times"></i></a></span>' +
                            '<span class="tree-btn" >&nbsp;<a data-toggle="tooltip" data-action="aliasbPartner" data-placement="top" title="编辑"><i class="fa fa-pencil"></i></a></span>';
                        $('#organization_treeH .jstree-clicked').after(toolTip);
                        $('#organization_treeH .jstree-clicked').parent().find('span.tree-btn a').tooltip();
                        that._deleteSubCompany($('#organization_treeH .jstree-clicked').closest('li'),that._currTreeObj);
                        that._aliasbPartner($('#organization_treeH .jstree-clicked').closest('li'),that._currTreeObj);
                    }
                    that.childOrgBtnDisabled();
                }
                //当选择节点是事业合伙人
                else if(data.isCurrentSubCompany == 1 && data.type === 'partner'){
                    if(that._roleCode.indexOf('hr_org_set')>-1){
                        var toolTip='<span class="tree-btn" >&nbsp;<a data-toggle="tooltip" data-action="deletePartner" data-placement="top" title="解除事业合伙人"><i class="fa fa-times"></i></a></span>' +
                            '<span class="tree-btn" >&nbsp;<a data-toggle="tooltip" data-action="aliasbPartner" data-placement="top" title="编辑"><i class="fa fa-pencil"></i></a></span>';
                        $('#organization_treeH .jstree-clicked').after(toolTip);
                        $('#organization_treeH .jstree-clicked').parent().find('span.tree-btn a[data-toggle="tooltip"]').tooltip();
                        that._deletebPartner($('#organization_treeH .jstree-clicked').closest('li'),that._currTreeObj);
                        that._aliasbPartner($('#organization_treeH .jstree-clicked').closest('li'),that._currTreeObj);
                    }
                    that.childOrgBtnDisabled();
                }
                else if(data.companyId != window.currentCompanyId && data.type == 'depart'){
                    if(that._roleCode.indexOf('hr_org_set')>-1){
                        $('#organization_treeH .jstree-clicked').after('<span class="tree-btn" >&nbsp;' +
                            '<a data-toggle="tooltip"data-placement="top" title="编辑部门"><i class="fa fa-pencil"></i></a></span>');
                        $('#organization_treeH .jstree-clicked').parent().find('span.tree-btn a').tooltip();
                        that.bindEditDepartment($('#organization_treeH .jstree-clicked').closest('li'));
                    }
                    that.childOrgBtnDisabled();
                }
                else{
                    $(that.element).find('.headContent a[data-action]').attr('disabled','disabled');
                    $(that.element).find('.headContent a[data-action="drop_down"]').attr('data-toggle','');
                    $(that.element).find('.headContent a[data-action]').off('click');
                }
                rolesControl();
            };
            $(that.element).find('#organization_treeH').m_orgByTree(options);
        }
        //树icon颜色处理
        , dealTreeIconColorFun:function (id) {
            $('#organization_treeH').find('li[id="'+id+'"]>ul>li').each(function () {
                var $this = $(this);
                var relationType = $this.attr('relationtype');
                if(relationType==1){
                    $this.find('a.jstree-anchor i.jstree-icon').addClass('color-blue');
                }else if(relationType==2){
                    $this.find('a.jstree-anchor i.jstree-icon').addClass('color-green');
                }else if(relationType==3){
                    $this.find('a.jstree-anchor i.jstree-icon').addClass('fc-red');
                }
            });
        }
        //选择分公司或事业合伙人禁用一些操作按钮
        ,childOrgBtnDisabled:function () {
            var that = this;
            $(that.element).find('.headContent a[data-action="toAuditUser"]').attr('disabled','disabled').off('click');//待审核人员
            $(that.element).find('.headContent a[data-action="addSubCompany"]').attr('disabled','disabled').off('click');//添加分公司
            $(that.element).find('.headContent a[data-action="inviteBranch"]').attr('disabled','disabled').off('click');//邀请分公司
            $(that.element).find('.headContent a[data-action="addPartner"]').attr('disabled','disabled').off('click');//添加事业合伙人
            $(that.element).find('.headContent a[data-action="inviteCorp"]').attr('disabled','disabled').off('click');//邀请事业合伙人
            $(that.element).find('.headContent a[data-action="drop_down"]').attr('disabled','disabled').off('click');//邀请人员下拉菜单图标
            $(that.element).find('.headContent a[data-action="drop_down"]').attr('data-toggle','').off('click');
        }
        //绑定转换组织架构树的按钮
        ,bindTreeSwitch:function(){
            var that = this;
            $(that.element).find('.treeSwitch a[data-action="treeSwitch"]').bind('click',function(e){
                var html = '';
                if(that._treeType===1){
                    html = '<img src="'+window.rootPath+'/assets/img/my_org_framework.png" style="height: 23px;" data-toggle="tooltip" data-placement="top" title="查看我的组织架构" />';
                }else{
                    html = '<img src="'+window.rootPath+'/assets/img/whole_org_framework.png" style="height: 23px;" data-toggle="tooltip" data-placement="top" title="查看总公司组织架构" />';
                }
                $(this).html(html);
                $(this).find('img').tooltip();
                that._treeType=that._treeType===1?0:1;
                var html = '<div class="clearfix margin-bottom-10"></div><div id="organization_treeH">\
                            <ul class="sidebar-nav list-group sidebar-nav-v1">\
                            </ul>\
                            </div>';
                $(that.element).find('#organization_treeH').parent().html(html);
                that._initOrgTree(that._treeType);
                stopPropagation(e);
            });
            $(that.element).find('.treeSwitch a[data-action="treeSwitch"]>img').tooltip();
        }

        //界面文字刷新
        , dealHtmlTextRefresh: function (data) {
            $('span[data-obj="currOrgObj"][data-key]').each(function () {
                var name = $(this).attr('data-key');
                if (data[name] != null) {
                    $(this).html(data[name]);
                }
            });
        }
        //给部门绑定编辑部门按钮
        ,bindEditDepartment:function($obj){
            var that = this;
            $obj.find('.tree-btn').on('click',function(event) {
                that._editDepart($obj);
            });
        }
        //给批量导入按钮绑定事件
        ,bindBulkImport:function($obj){
            var that = this;
            $obj.find('a[data-action="bulkImport"]').on('click',function(event){
               that._bulkImport();
            });
        }
        ,_editDepart: function ($obj) {

                var that = this;
                var options = {};
                options.parentDepart = {};
                var selectDepart = {};
                selectDepart.id = that._currTreeObj.realId;
                selectDepart.departName = that._currTreeObj.treeEntity.departName;
                selectDepart.pid = that._currTreeObj.treeEntity.pid;
                selectDepart.departLevel = that._currTreeObj.treeEntity.departLevel;
                selectDepart.departPath = that._currTreeObj.treeEntity.departPath;
                selectDepart.departType = that._currTreeObj.treeEntity.departType;
                selectDepart.companyId = that._currTreeObj.companyId;
                options.doType = 'edit';
                options.title = '编辑部门';
                var departObj = selectDepart;//当前对象
                var parentDepart = {};
                parentDepart.id = that._currTreeObj.parentOrgObj.realId;
                parentDepart.departName = that._currTreeObj.parentOrgObj.text;
                parentDepart.pid = that._currTreeObj.parentOrgObj.treeEntity.pid;
                parentDepart.departLevel = that._currTreeObj.parentOrgObj.treeEntity.departLevel;
                parentDepart.departPath = that._currTreeObj.parentOrgObj.treeEntity.departPath;
                departObj.parentDepart = parentDepart;
                options.departObj = departObj;
                options.saveCallBack = function (data) {
                    editNodeByTree(data);
                    that._currTreeObj.treeEntity = data.treeEntity;
                    that.dealHtmlTextRefresh(data);
                };
                options.delCallBack = function () {
                    var rel =  $('#organization_treeH').jstree(true);
                    var id = $('#organization_treeH>ul>li>a:eq(0)').attr('id');
                    rel.select_node(id);
                };
                $('body').m_editDepart(options);
        }
        //删除分支机构事件
        ,_deleteSubCompany:function($obj,data){
            var that = this;
            $obj.find('.tree-btn a[data-action="deleteSubCompany"]').on('click',function(event) {
                var $deleteTarget = $('#organization_treeH a.jstree-anchor.jstree-clicked');
                $(this).m_popover({
                    placement: 'right',
                    content: template('m_common/m_popover_confirm', {confirmMsg: '确定要删除该分支机构吗?'}),
                    onShown : function ($popover) {
                        var scrollTop = $popover.parents('#organization_treeH').parent().scrollTop();
                        if(scrollTop-0>0){
                            var $time = setTimeout(function () {
                                var nowTop = $obj.find('.m-popover').css('top');
                                $obj.find('.m-popover').css('top',(parseInt(nowTop)+scrollTop)+'px');
                                clearTimeout($time);
                            },100);
                        }
                    },
                    onSave: function ($popover) {
                        var option = {};
                        option.url = restApi.url_subCompany + '/' + data.companyId;
                        m_ajax.get(option, function (response) {
                            if (response.code == '0') {
                                S_toastr.success('删除成功！');
                                var rel =  $('#organization_treeH').jstree(true);
                                delNodeByTree();
                                var subCompanyId = window.currentCompanyId+'subCompanyId_anchor';
                                that.dealTreeIconColorFun(window.currentCompanyId+'subCompanyId');
                                if($('#organization_treeH').find('a#'+subCompanyId).next('ul').length<1){
                                    rel.delete_node(subCompanyId);
                                }
                                var id = $('#organization_treeH>ul>li>a:eq(0)').attr('id');
                                rel.select_node(id);
                            } else {
                                S_dialog.error(response.info);
                            }
                        });
                    }

                }, true);
                setTimeout(function(){
                    var $target =  $obj.closest('#organization_treeH').parent();
                    var width = $target.outerWidth()+"px";
                    $target.animate({scrollLeft: width}, {
                        duration: 600,
                        easing: 'linear'
                    });
                },100)
            });

        }
        //删除事业合伙人
        ,_deletebPartner:function($obj,data){
            var that = this;
            $obj.find('.tree-btn a[data-action="deletePartner"]').on('click',function(event) {
                var $deleteTarget = $('#organization_treeH a.jstree-anchor.jstree-clicked');
                $(this).m_popover({
                    placement: 'right',
                    content: template('m_common/m_popover_confirm', {confirmMsg: '确定要删除该事业合伙人吗?'}),
                    onShown : function ($popover) {
                        var scrollTop = $popover.parents('#organization_treeH').parent().scrollTop();
                        if(scrollTop-0>0){
                            var $time = setTimeout(function () {
                                var nowTop = $obj.find('.m-popover').css('top');
                                $obj.find('.m-popover').css('top',(parseInt(nowTop)+scrollTop)+'px');
                                clearTimeout($time);
                            },100);
                        }
                    },
                    onSave: function ($popover) {
                        var option = {};
                        option.url = restApi.url_businessPartner + '/' + data.companyId;
                        m_ajax.get(option, function (response) {
                            if (response.code == '0') {
                                S_toastr.success('删除成功！');
                                delNodeByTree();
                                var rel =  $('#organization_treeH').jstree(true);
                                var partnerId = window.currentCompanyId+'partnerId_anchor';
                                that.dealTreeIconColorFun(window.currentCompanyId+'partnerId');
                                if($('#organization_treeH').find('a#'+partnerId).next('ul').length<1){
                                    rel.delete_node(partnerId);
                                }
                                var id = $('#organization_treeH>ul>li>a:eq(0)').attr('id');
                                rel.select_node(id);
                            } else {
                                S_dialog.error(response.info);
                            }
                        });
                    }

                }, true);
                setTimeout(function(){
                    var $target =  $obj.closest('#organization_treeH').parent();
                    var width = $target.outerWidth()+"px";
                    $target.animate({scrollLeft: width}, {
                        duration: 600,
                        easing: 'linear'
                    });
                },100)
            });

        }
        ,_aliasbPartner:function($obj,data){
            $obj.find('.tree-btn a[data-action="aliasbPartner"]').on('click',function(event) {
                var $btn = $(this);
                var options = {
                    companyId:data.companyId,
                    companyOriginalName:data.treeEntity.companyName,
                    companyAlias:data.text,
                    relationTypeId:data.relationType,
                    saveCallback:function(){
                        $('#content-box').m_organizational();
                    }
                };

                $('body').m_editAlias(options);

                return false;
            });
        }
        , _addDepart: function () {
            var that = this;
            var options = {};
            options.departObj = {};
            var selectDepart = {};
            if (that._currTreeObj.companyId != null) {
                selectDepart.id = that._currTreeObj.realId;
                selectDepart.departName = that._currTreeObj.text;
                selectDepart.pid = that._currTreeObj.treeEntity.pid;
                selectDepart.departLevel = that._currTreeObj.treeEntity.departLevel;
                selectDepart.departPath = that._currTreeObj.treeEntity.departPath;
                selectDepart.departType = that._currTreeObj.treeEntity.departType;
                selectDepart.companyId = that._currTreeObj.companyId;
                options.departObj.parentDepart = selectDepart;
                options.doType = 'add';
                options.saveCallBack = function (data) {
                    addNodeByTree(data, that._currTreeObj, that._currentCompanyId);
                };
                $('body').m_editDepart(options);
            } else {
                S_toastr.warning('请选择组织！');
            }
        }
        //添加人员
        , _addUser: function () {
            var that = this;
            var options = {};
            if (that._currTreeObj.companyId != null) {
                options.companyId = that._currTreeObj.companyId;
                options.realId = that._currTreeObj.realId;
                $('body').m_editUser(options);
            } else {
                S_toastr.warning('请选择组织或部门！');
            }
        }
        //批量导入
        , _bulkImport: function () {
            var option = {}, that = this;
            if(that._currTreeObj.companyId == window.currentCompanyId && that._currTreeObj.type=='depart'){
                option.$companyInfo = {};
                option.$companyInfo.companyId = that._currTreeObj.companyId;
                $('#organization_treeH a.jstree-anchor').each(function(){
                    var id = $(this).attr('id');
                    if(id == that._currTreeObj.companyId + "_anchor"){
                        return option.$companyInfo.companyName = $(this).text();
                    }
                });
            }else{
                option.$companyInfo = {};
                option.$companyInfo.companyId = that._currTreeObj.companyId;
                option.$companyInfo.companyName = that._currTreeObj.text;
            }

            $(that.element).m_bulkImport(option);
        }
        //事件绑定
        , _bindActionClick: function () {
            var that = this;
            $(that.element).find('.headContent button[data-action],.headContent a[data-action]').off('click').on('click', function () {
                var dataAction = $(this).attr('data-action');
                // if (dataAction == 'editDepart') {//编辑部门
                //     that._editDepart();
                //     return false;
                // }
                if (dataAction == 'addDepart') {//添加部门
                    that._addDepart();
                    return false;
                }
                else if (dataAction == 'editDepart') {//编辑部门
                    that.bindEditDepartment();
                    return false;
                }
                else if (dataAction == 'addUser') {//添加人员
                    that._addUser();
                    return false;
                }
                else if (dataAction == 'inviteUser') {//邀请人员
                    $('body').m_inviteUser();
                    return false;
                }
                else if (dataAction == 'bulkImport') {//批量导入
                    that._bulkImport();
                    return false;
                }
                /* else if (dataAction == 'deleteSubCompany') {//删除分支机构
                    that._deleteSubCompany($(this));
                    return false;
                } */
                else if (dataAction == 'addSubCompany') {//创建分支机构
                    var options = {};
                    options.companyId = that._currentCompanyId;
                    options.saveCallBack = function (data) {
                        addNodeByTreeOfPartner(1,that._currentCompanyId);
                        addNodeByTreeByRoot(data,that._currentCompanyId+'subCompanyId');
                        that.dealTreeIconColorFun(that._currentCompanyId+'subCompanyId');
                        $('#m_top').m_top({});//刷新头部（存在创建事业合伙人是当前人员的手机创建时需要刷新）
                    };
                    $('body').m_editSubCompany(options);
                    return false;
                }
                else if (dataAction == 'toAuditUser') {//待审核人员
                    var options = {};
                    options.companyId = that._currentCompanyId;
                    options.$auditOrgUserCallBack = function () {
                        var toAuditEmpNumTip = $(that.element).find('#toAuditEmpNumTip');
                        var newNum = toAuditEmpNumTip.text()-0-1;
                        toAuditEmpNumTip.text(newNum);
                        if(newNum==0){
                            toAuditEmpNumTip.remove();
                        }
                    };
                    $('body').m_toAuditOrgUserList(options);
                    return false;
                }
                else if(dataAction=='addPartner'){//创建事业合伙人
                    var options = {};
                    options.companyId = that._currentCompanyId;
                    options.saveCallBack = function (data) {
                        addNodeByTreeOfPartner(2,that._currentCompanyId);
                        addNodeByTreeByRoot(data,that._currentCompanyId+'partnerId');
                        that.dealTreeIconColorFun(that._currentCompanyId+'partnerId');
                        $('#m_top').m_top({});//刷新头部（存在创建事业合伙人是当前人员的手机创建时需要刷新）
                    };
                    $('body').m_editPartner(options);
                    return false;
                }
                else if (dataAction == 'inviteBranch') {//邀请分支机构
                    $('body').m_inviteCorp({inviteType:1},true);
                    return false;
                }
                else if (dataAction == 'inviteCorp') {//邀请事业合伙人
                    $('body').m_inviteCorp({inviteType:2},true);
                    return false;
                }
                /*else if (dataAction == 'toAuditOrg') {//待审核团队
                    var options = {};
                    options.companyId = that._currentCompanyId;
                    options.$auditOrgCallBack = function () {
                        that.getToAuditCompanyCount(function (data) {
                        });
                    };
                    $('body').m_toAuditOrgTab(options);
                    return false;
                }*/
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
