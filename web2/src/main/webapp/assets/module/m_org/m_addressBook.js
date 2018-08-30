/**
 * 组织－组织架构
 * Created by wrb on 2017/2/23.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_addressBook",
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

                    var html = template('m_org/m_addressBook', {roleCodes:that._roleCode,toAuditEmpNum:response.data.toAuditEmpNum,rootPath:window.rootPath});
                    $(that.element).html(html);
                    rolesControl();
                    that._initOrgTree();
                    that.bindTreeSwitch();
                    //that.getCompanyCountBasicData();

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
            options.isClick = false;
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

                //that.dealTreeIconColorFun(data.node.id);
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

        ,_aliasbPartner:function($obj,data){
            $obj.find('.tree-btn a[data-action="aliasbPartner"]').on('click',function(event) {
                var $btn = $(this);
                var options = {
                    companyId:data.companyId,
                    companyOriginalName:data.treeEntity.companyName,
                    companyAlias:data.text,
                    saveCallback:function(){
                        $('#box_detail').m_organizational();
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
