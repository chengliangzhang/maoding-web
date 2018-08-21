/**
 * Created by veata on 2016/12/14.
 * It only applies in choosing Team or Organization from Tree(with checkBox).
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_onlyGetTeamByTree",
        defaults = {
            title: null,
            treeUrl: null,
            minHeight: null,
            width: null,
            currOrgTreeObj: null,
            parentOrgObj: null,
            isExcludeOrgChoice: null,//是否暂时隐藏树里面团队里的部门
            ids: '',
            CallBack: null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._orgData = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {

            this.initUserTreeDialog();
        }
        //任务签发弹窗
        , initUserTreeDialog: function () {
            var that = this;

            S_dialog.dialog({
                title: that.settings.title || '选择组织',
                contentEle: 'dialogOBox',
                lock: 3,
                width: that.settings.width || '600',
                minHeight: that.settings.minHeight || '400',
                tPadding: '0px',
                url: rootPath + '/assets/module/m_common/m_dialog.html',
                ok: function () {
                    var data = that.settings.ids;
                    var companyName = that.getCompanyName(that.settings.ids);
                    // console.log('确定：'+data);
                    if (data && data.length > 0) {
                        $(that.element).find('input[data-action="choseDepartment"]').val(companyName);
                    } else {
                        $(that.element).find('input[data-action="choseDepartment"]').val('点击设置');
                    }
                    return that.settings.CallBack(data);
                },
                cancel: function () {

                }
            }, function (d) {//加载html后触发
                that.initTreeData(function (data) {
                    var html = template('m_notice/m_onlyGetTeamByTree', {});
                    $('div[id="content:' + d.id + '"] .dialogOBox').html(html);

                    that.initTreeStructure(data, that.settings.ids);
                });
            });


        }
        //获取选中的组织名称
        , getCompanyName: function (ids) {
            var that = this;
            var companyNameStr = '';
            if (ids.indexOf(that._orgData.children[0].id+',') > -1) {
                companyNameStr += that._orgData.children[0].text+'，';
            }
            if(that._orgData.children && that._orgData.children.length>1){
                for(var j=0;j<that._orgData.children.length;j++){
                    if (that._orgData.children[j].children && that._orgData.children[j].children.length > 0) {
                        $.each(that._orgData.children[j].children, function (i, item) {
                            if (ids.indexOf(item.id) > -1) {
                                companyNameStr += item.text +'，';
                            }
                        });
                    }
                }
            }
            return companyNameStr.substr(0,companyNameStr.length-1);

        }

        //初始化树数据
        , initTreeData: function (callBack) {

            var that = this;
            var option = {};
            option.url = that.settings.treeUrl != null && that.settings.treeUrl != '' ? that.settings.treeUrl : restApi.url_getOrgTreeForNotice;

            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    if (callBack != null) {
                        that._orgData = response.data;
                        if (that.settings.isExcludeOrgChoice != null) {
                            that.excludeOrgChoice(response.data);
                        }
                        return callBack(response.data);
                    }
                } else {
                    S_dialog.error(response.info);
                }

            })
        }
        //是否排除团队里的部门选择（暂时去掉本部的子节点 以及分支机构与事业合伙人下的子节点分支机构与事业合伙人）
        , excludeOrgChoice: function (data) {
            $.each(data.children, function (i, child) {
                if (!(child.id.indexOf('partnerId') > -1 || child.id.indexOf('subCompanyId') > -1)) {
                    if (child.children && child.children.length > 0) {
                        child.children = [];
                    }
                } else {
                    if (child.children && child.children.length > 0) {
                        var childList1 = child.children;
                        for (var i = 0; i < childList1.length; i++) {
                            if (childList1[i].children && childList1[i].children.length > 0) {
                                childList1[i].children = [];
                            }
                        }
                    }
                }
            });
            return data;
        }
        //生成树结构
        , initTreeStructure: function (orgData, ids) {

            var that = this;

            $('#organization_treeH').jstree({
                'core': {
                    'check_callback': true,
                    'data': orgData
                },
                'plugins': ['types', 'checkbox'],
                "checkbox": {
                    "keep_selected_style": false
                },
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
            }).bind('select_node.jstree', function (e, data) {
                //alert(data.node.original.realId);
                var currOrgObj = data.node.original;//获取当前树的对象
                that.settings.currOrgTreeObj = currOrgObj;

                var inst = data.instance;
                var parentOrgObj = inst.get_node(data.node.parent).original;

                if (that.settings.treeClickCallBack != null) {
                    currOrgObj.parentOrgObj = parentOrgObj;
                    return that.settings.treeClickCallBack(currOrgObj);
                }

            }).bind('click.jstree', function (event) {
                var id = '';
                var ids = that.settings.ids;
                if (event.target.nodeName != 'I' && event.target.nodeName != 'A') {
                    return;
                }
                else if (event.target.nodeName == 'I') {
                    if ($(event.target).is('.jstree-ocl')) {
                        return false;
                    } else {
                        id = $(event.target).parent().parent().attr('id');
                    }
                } else if (event.target.nodeName == 'A') {
                    id = $(event.target).parent().attr('id');
                }

                var tree = $('#organization_treeH').jstree(true);
                var getRoot = tree.get_node(id);
                var eTarget = event.target;
                var isCheck = false;
                var b_p = $(eTarget).parents('li#' + id).find('i.jstree-checkbox').css('background-position');
                if (b_p == '-228px -4px' || b_p == '-228px -36px') {//checkbox勾上时
                    isCheck = true;
                }
                if (isCheck) {
                    if (!(ids.indexOf(id + ',') > -1)) {
                        ids += id + ',';
                    }
                    if (getRoot.children_d && getRoot.children_d.length > 0) {
                        $.each(getRoot.children_d, function (i, item) {
                            if (!(ids.indexOf(item + ',') > -1)) {
                                ids += item + ',';
                            }

                        });
                    }
                    if (getRoot.parents && getRoot.parents.length > 0) {
                        $.each(getRoot.parents, function (i, item) {
                            if (item != '' && item != "#") {
                                var thisLi = $('#organization_treeH li#' + item);
                                var b_p = thisLi.find('a.jstree-anchor').eq(0).find('i.jstree-checkbox').css('background-position');
                                if ((b_p == '-228px -4px' || b_p == '-228px -36px') && !(ids.indexOf(item + ',') > -1)) {
                                    ids += item + ',';
                                }
                            }
                        });
                    }

                } else {
                    if (ids.indexOf(id + ',') > -1) {
                        ids = ids.replace(id + ',', '');
                    }
                    if (getRoot.children_d && getRoot.children_d.length > 0) {
                        $.each(getRoot.children_d, function (i, item) {
                            if (ids.indexOf(item + ',') > -1) {
                                ids = ids.replace(item + ',', '');
                            }

                        });
                    }
                    if (getRoot.parents && getRoot.parents.length > 0) {
                        $.each(getRoot.parents, function (i, item) {
                            if (item.id != '#' && ids.indexOf(item + ',') > -1) {
                                ids = ids.replace(item + ',', '');
                            }

                        });
                    }
                }
                that.settings.ids = ids;
            }).bind('ready.jstree', function (e, data) {//loaded.jstree

                var inst = data.instance;
                var obj = inst.get_node(e.target.firstChild.firstChild.lastChild);

                var currOrgObj = obj.original;//获取当前树的对象
                that.settings.currOrgTreeObj = currOrgObj;
                if (that.settings.treeClickCallBack != null) {
                    return that.settings.treeClickCallBack(currOrgObj);
                }
                var tree = $('#organization_treeH').jstree(true);
                var ids = that.settings.ids;
                if (ids != '' && ids != null) {//当初始化时有ids且不为空，即已选择发送范围时，勾上已选的checkbox
                    if (ids.lastIndexOf(',') != ids.length - 1) {
                        ids += ',';
                    }
                    var orgList = ids.split(',');
                    $.each(orgList, function (j, childId) {
                        if (!(childId.indexOf('partnerId') > -1 || childId.indexOf('subCompanyId') > -1 || childId == "")) {
                            tree.select_node(childId);
                        }
                    })
                } else {//当 初始化时无ids或ids为空时，则默认勾上公司本部
                    var thisTeamId = '';//公司本部的Id
                    $.each(obj.children, function (i, item) {
                        if (!(item.indexOf('partnerId') > -1 || item.indexOf('subCompanyId') > -1)) {
                            thisTeamId = item;
                            return;
                        }
                    });
                    tree.select_node(thisTeamId);
                    ids += thisTeamId + ',';
                }
                that.settings.ids = ids;
                // console.log(ids);
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
