/**
 * 组织－部门树选择
 * Created by wrb on 2017/6/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_selectedOrgTree",
        defaults = {
             $selectedCallBack:null//选择节点事件
            ,$isFirstSelectedCallBack:true//是否需要首次触发选中事件
            ,$isAbsolutelyPositioned:false//当前树是否绝对定位
            ,$isClickDisappear:true//是否点击外部就消失该树
            ,$clickDisappearForClass:[]//点击外部就消失该树不包括的类（[class1,class2,...]）
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            //渲染容器
            $(that.element).html(template('m_org/m_selectedOrgTree', {}));
            if(that.settings.$isAbsolutelyPositioned){
                $(that.element).find('.chose-user-tree').addClass('m_selectedOrgTree');
            }
            that._initTreeData(function (data) {

                that._initTreeStructure(data);
                if(that.settings.$isClickDisappear){
                    that._closeTreeBox();
                }
            });
        }
        //初始化树数据
        , _initTreeData: function (callBack) {

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
        , _initTreeStructure: function (orgData) {
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

                //that.renderUserListPage(data.node.original.realId);//分页
                $(that.element).find('.chose-user-tree').addClass('hide');

                if(that.settings.$selectedCallBack!=null){
                    that.settings.$selectedCallBack(currOrgObj);
                }


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

                if(that.settings.$isFirstSelectedCallBack && that.settings.$selectedCallBack!=null){
                    that.settings.$selectedCallBack(currOrgObj);
                }

            });
            //that.renderUserListPage(orgData.realId);
        }
        //当鼠标点击的焦点不在浮窗内时，关闭浮窗
        ,_closeTreeBox:function() {
            var that = this;
            $('body').on('click', function (event) {

                var isHide = true;
                if ($(event.target).parents('.chose-user-tree').length > 0 ) {
                    isHide = false;
                }

                if(that.settings.$clickDisappearForClass!=null && that.settings.$clickDisappearForClass.length>0){
                    var c = that.settings.$clickDisappearForClass;
                    for(var i=0;i<c.length;i++){

                        if ($(event.target).hasClass(c[i])) {
                            isHide = false;
                            break;
                        }
                    }
                }

                if(isHide){
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
