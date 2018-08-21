/**
 * 选择人员
 * Created by wrb on 2017/5/26.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_choseUserBox",
        defaults = {
            //$orgId:null//组织ID
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
            this.initUserData();
        }
        //人员数据并加载模板
        , initUserData: function () {

            var that = this;
            var html = template('m_production/m_choseUserBox', {});
            $(that.element).html(html);

            that.initTreeData(function (data) {

                that.initTreeStructure(data);
                that.closeTreeBox();

            });
            that.bindBtnFilter();


        }
        ,renderUserListPage:function (orgId) {
            var that = this;
            var url = that.settings.userUrl != null && that.settings.userUrl != '' ? that.settings.userUrl : restApi.url_getOrgUser;
            var params = {};
            params.orgId = orgId;
            paginationFun({
                eleId: '#userlist-pagination-container',
                loadingId: '.userListBox',
                url: url,
                params: params
            }, function (response) {

                var $data = {};
                $data.orgUserList = response.data.data;
                var html = template('m_production/m_choseUserList', $data);
                $(that.element).find('#userListBox').html(html);
                that.bindChoseOrgUser();

            });
        }
        //按钮绑定控制树展示与否
        ,bindBtnFilter:function () {
            var that = this;
            $(that.element).find('button[name="btn-filter"]').off('click').on('click',function () {
                var treeBox = $(that.element).find('.chose-user-tree');
                if(treeBox.hasClass('hide')){
                    treeBox.removeClass('hide');
                }else{
                    treeBox.addClass('hide');
                }
            });

        }
        //选择用户
        ,bindChoseOrgUser:function () {

            var that = this;
            $(that.element).find('a[data-action="choseOrgUser"]').on('click',function () {

                var iHtml='',companyUserId = $(this).attr('data-id'),userId = $(this).attr('data-user-id'),userName = $(this).closest('TR').find('td:first').html(),
                    i = $(this).attr('data-i');

                if($('.designerRow[data-node-seq="'+i+'"]').find('.user-list .designerSpan[data-companyUserId="'+companyUserId+'"]').length>0){

                }else{
                    iHtml += '<span class="label label-default inline m-r-xs m-b-xs designerSpan" data-companyUserId="' + companyUserId + '">';
                    iHtml += '<span class="nameSpan">' + userName + '</span>';
                    iHtml += '<a href="javascript:void(0)" data-action="delDesigner"><i class="glyphicon glyphicon-remove text-danger"></i></a>';
                    iHtml += '</span>';

                    $('.designerRow[data-node-seq="'+i+'"]').find('.user-list').append(iHtml);
                    $('.designerRow[data-node-seq="'+i+'"]').find('.user-list .designerSpan:last a[data-action="delDesigner"]').on('click',function () {
                        $(this).parent('.designerSpan').remove();
                        return false;
                    });
                }


            });

        }
        //初始化树数据
        , initTreeData: function (callBack) {

            var that = this;
            var option = {};
            option.url = that.settings.treeUrl != null && that.settings.treeUrl != '' ? that.settings.treeUrl : restApi.url_getOrgTreeForSearch;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    if(that.settings.initTreeDataCallBack){
                        that.settings.initTreeDataCallBack(response.info);
                    }
                    if (callBack != null) {
                        return callBack(response.data);
                    }
                } else {
                    S_dialog.error(response.info);
                }

            })
        }
        //生成树结构
        , initTreeStructure: function (orgData) {
            var that = this;
            var $tree = $('#organizationTree');
            $tree.jstree({
                'core': {
                    'check_callback': true,
                    'data': orgData
                },
                'plugins': ['types'],
                'types': {
                    'default': {
                        'icon': 'fa fa-users'
                    },
                    'independent': {   //独立经营图标
                        'icon': 'fa fa-trademark'
                    },
                    'partner': {       //合作伙伴图标

                        'icon': 'fa fa-share-alt'
                    },
                    'root': {         //根节点图标
                        'icon': 'fa fa-building'
                    }
                }
            }).on('select_node.jstree', function (e, data) {

                var currOrgObj = data.node.original;//获取当前树的对象
                that.settings.currOrgTreeObj = currOrgObj;
                var inst = data.instance;
                var parentOrgObj = inst.get_node(data.node.parent).original;

                that.renderUserListPage(data.node.original.realId);//分页
                $(that.element).find('.chose-user-tree').addClass('hide');

                $(that.element).find('#orgName').html(currOrgObj.text);


            }).on('ready.jstree', function (e, data) {//loaded.jstree

                var inst = data.instance;
                var obj = inst.get_node(e.target.firstChild.firstChild.lastChild);
                var currOrgObj = obj.original;//获取当前树的对象
                var tree = $('#organizationTree').jstree(true);
                var selectedObj = tree.get_selected();

                if (selectedObj != null && selectedObj != undefined) {
                    var selectedTreeObj = tree.get_node(selectedObj[0]);
                    currOrgObj = selectedTreeObj.original;//获取当前树的对象
                }
                that.settings.currOrgTreeObj = currOrgObj;
                $(that.element).find('#orgName').html(currOrgObj.text);

            });
            that.renderUserListPage(orgData.realId);
        }

        //当鼠标点击的焦点不在浮窗内时，关闭浮窗
        ,closeTreeBox:function() {
            var that = this;
            $('body').on('click', function (event) {

                if ($(event.target).parents('.chose-user-tree').length < 1 && !($(event.target).attr('name')=='btn-filter')
                    && !$(event.target).hasClass('btn-filter-name') && !$(event.target).hasClass('btn-filter-icon')) {

                    var treeBox = $(that.element).find('.chose-user-tree');
                    if(!treeBox.hasClass('hide')){
                        treeBox.addClass('hide');
                    }
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
