/**
 * 组织树
 * Created by wrb on 2016/12/14.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_orgByTree_old",
        defaults = {
              title: ''//弹窗标题
            , treeUrl: ''//获取树的url
            , choseUserCallback: null//选择人员事件
            , isDialog: true//是否弹窗，默认弹窗
            , hasRole: true//是否弹窗，默认弹窗
            , isGetUserList: true//是否加载右边人员列表,默认加载
            , saveChoseUserCallback: null//点击保存(确定)按钮时触发事件
            , delChoseUserCallBack: null//点击删除已选的人员后触发的事件
            , treeClickCallBack: null//树点击事件
            , treeIconObj: null//树生成的图标对象
            , currOrgTreeObj: {}//当前树选中节点对象
            , chosedUserList: null//当前窗口选中的人员[{id,userId,userName}...]
            , isASingleSelectUser: false//是否单个选择人员，默认false,2为单选且提示不关窗
            , $isOkSave: false//默认false
            , $saveDataUrl: null//直接保存url
            , $saveData: null//保存格外的参数
            , $saveTimeKeyVal: null//保存到库所对应的时间字段 key 值
            , $okText:null//按钮文字
            , initTreeDataCallBack:null//加载树数据后调用的回调
            , renderTreeCallBack:null//树渲染完后回调
            , afterOpenCallBack:null//打开树回调
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._chosedUserIds = '';//已选人员ids
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            this.initUserTreeDialog();
        }
        //初始化人员选择弹窗
        , initUserTreeDialog: function () {
            var that = this;
            if (that.settings.isDialog) {
                var options = {};
                options.title = that.settings.title || '选择人员';
                options.contentEle = 'dialogOBox';
                options.lock = 3;
                options.width = '800';
                options.minHeight = '400';
                options.tPadding = '0px';
                options.url = rootPath + '/assets/module/m_common/m_dialog.html';

                if (that.settings.isASingleSelectUser && !that.settings.$isOkSave) {//单选且没有保存按钮

                    options.cancelText = '关闭';
                    options.cancel = function () {};

                } else if ((!that.settings.isASingleSelectUser && !that.settings.$isOkSave) || (that.settings.isASingleSelectUser && that.settings.$isOkSave)) {//没单选且确定(没保存)按钮

                    options.cancel = function () {};
                    options.ok = function () {
                        if (that.settings.saveChoseUserCallback != null) {
                            var $data = {};
                            $data.chosedUserList = that.settings.chosedUserList;
                            return that.settings.saveChoseUserCallback($data);
                        }
                    };
                } else if (that.settings.$isOkSave) {//存在保存按钮
                    options.cancel = function () {};
                    options.okText = '保存';
                    options.ok = function () {

                        var timeObj = {};
                        if (that.settings.$saveTimeKeyVal != null) {//处理保存到库所对应的时间字段 key 值,此处传入的key是有序的

                            timeObj[that.settings.$saveTimeKeyVal[0]] = that._chosedUserIds;
                        }
                        $.extend(timeObj, that.settings.$saveData);
                        var option = {};
                        option.url = that.settings.$saveDataUrl;
                        option.postData = timeObj;
                        m_ajax.postJson(option, function (response) {
                            if (response.code == '0') {
                                if (that.settings.saveChoseUserCallback != null) {
                                    var $data = {};
                                    $data.data = response.data;
                                    $data.chosedUserList = that.settings.chosedUserList;
                                    return that.settings.saveChoseUserCallback($data);
                                }
                            } else {
                                S_dialog.error(response.info);
                            }
                        })
                    };
                }
                if(that.settings.$okText!=null){
                    options.okText = that.settings.$okText;
                }
                S_dialog.dialog(options, function (d) {//加载html后触发
                    that.initTreeData(function (data) {

                        that.element = 'div[id="content:' + d.id + '"] .dialogOBox';
                        var $data = {};
                        if (that.settings.chosedUserList != null) {
                            $data.chosedUserList = that.settings.chosedUserList;
                        }
                        var html = template('m_org/m_orgByTree', $data);
                        $(that.element).html(html);
                        that.initTreeStructure(data);
                    });
                });
            } else {
                that.initTreeData(function (data) {

                    if (that.settings.isGetUserList) {
                        var $data = {};
                        if (that.settings.chosedUserList != null) {
                            $data.chosedUserList = that.settings.chosedUserList;

                        }
                        $data.isASingleSelectUser = that.settings.isASingleSelectUser;
                        var html = template('m_org/m_orgByTree', $data);
                        $(that.element).html(html);

                    }
                    that.initTreeStructure(data);
                });
            }
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
            var $tree = $('#organization_treeH');
            $tree.jstree({
                'core': {
                    'check_callback': true,
                    'data': orgData
                },
                'plugins': ['types'],
                'types': that.settings.treeIconObj || {
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
                //alert(data.node.original.realId);
                var currOrgObj = data.node.original;//获取当前树的对象
                that.settings.currOrgTreeObj = currOrgObj;
                var inst = data.instance;
                var parentOrgObj = inst.get_node(data.node.parent).original;

                if (that.settings.isGetUserList) {

                    that.userDateByPage(data.node.original.realId);//分页
                }
                if (that.settings.treeClickCallBack != null) {
                    currOrgObj.parentOrgObj = parentOrgObj;
                    return that.settings.treeClickCallBack(currOrgObj);
                }

            }).on('ready.jstree', function (e, data) {//loaded.jstree

                var inst = data.instance;
                var obj = inst.get_node(e.target.firstChild.firstChild.lastChild);

                var currOrgObj = obj.original;//获取当前树的对象

                var tree = $('#organization_treeH').jstree(true);
                var selectedObj = tree.get_selected();
                if (selectedObj != null && selectedObj != undefined) {
                    var selectedTreeObj = tree.get_node(selectedObj[0]);
                    currOrgObj = selectedTreeObj.original;//获取当前树的对象
                }
                that.settings.currOrgTreeObj = currOrgObj;
                if (that.settings.treeClickCallBack != null) {
                    return that.settings.treeClickCallBack(currOrgObj,'ready');//字符串ready表示初始化树时才调用此方法
                }
                if (that.settings.isGetUserList) {
                    that.userDateByPage(currOrgObj.realId);
                }
                if(that.settings.renderTreeCallBack!=null){
                    that.settings.renderTreeCallBack();
                }
            }).on('after_open.jstree', function (e, data) {//load_node.jstree
                if(that.settings.afterOpenCallBack){
                    that.settings.afterOpenCallBack(data);
                }
            });
        }
        //把已选的人员列表转为id集合字符串，用于来作判断
        , dealChosedUserList: function () {
            var that = this;
            if (that.settings.chosedUserList != null) {
                var list = that.settings.chosedUserList;
                for (var i = 0; i < list.length; i++) {
                    that._chosedUserIds += list[i].id + ',';
                }
            }
        }

        //人员数据并加载模板
        , userDateByPage: function (orgId) {
            var that = this;
            var options = {};
            options.orgId = orgId;
            options.isASingleSelectUser = that.settings.isASingleSelectUser;
            options.choseUserCallback = function (data, event) {
                that._chosedUserIds = data.chosedUserIds;
                that.settings.chosedUserList = data.chosedUserList;
                if (that.settings.choseUserCallback != null) {
                    return that.settings.choseUserCallback(data, event);
                }
            };
            options.delChoseUserCallBack = that.settings.delChoseUserCallBack;
            options.chosedUserList = that.settings.chosedUserList;
            $(that.element).find('div[data-list="userList"]').m_userList(options);
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
