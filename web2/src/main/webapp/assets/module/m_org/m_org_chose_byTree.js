/**
 * 选择组织
 * Created by wrb on 2018/6/28.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_org_chose_byTree",
        defaults = {
            $type:0,//类型（选择不同URL）默认0=总公司/分支机构/事业合伙人  可进行点击；1=总公司/分支机构/事业合伙人  不可进行点击
            $renderType:0,//默认0=浮窗展示,1=界面展示
            $selectedCallBack:null,//选中回滚事件
            $renderCallBack:null,//渲染完成事件
            $selectedId:null,//选中某节点
            $buttonStyle:null,//组件button样式
            $spanStyle:null//组件button>span样式
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;
        this._selectedOrg = null;//当前选中的组织
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            var option = {};
            if(that.settings.$type==1){//总公司/分支机构/事业合伙人  不可进行点击
                option.url = restApi.url_getStaticCompanyForFinance;
            }else{//节点没禁用
                option.url = restApi.url_getStaticCompanyForPaymentDetail;
            }
            m_ajax.post(option, function (response) {
                if (response.code == '0') {

                    that._companyList = response.data;
                    var html = template('m_org/m_org_chose_byTree',{
                        companyList:response.data,
                        renderType:that.settings.$renderType,
                        buttonStyle:that.settings.$buttonStyle,
                        spanStyle:that.settings.$spanStyle
                    });
                    $(that.element).html(html);

                    that.renderOrgSelect();

                } else {
                    S_layer.error(response.info);
                }
            });
        }
        , renderOrgSelect:function () {
            var that = this;
            var $tree = $(that.element).find('#orgTreeH');
            $tree.jstree({
                'core': {
                    'check_callback': true,
                    'data': that._companyList
                },
                'plugins': ['types'],
                'types': that.settings.treeIconObj || {
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
                    },
                    'root': {         //根节点图标
                        'icon': 'iconfont rounded icon-2fengongsi'
                    }
                }
            }).on('select_node.jstree', function (e, data) {

                that._selectedOrg = data.node.original;//获取当前树的对象
                $(that.element).find('.company-name').html(that._selectedOrg.text);
                if(that.settings.$selectedCallBack!=null){
                    that.settings.$selectedCallBack(that._selectedOrg);
                }

                $('.btn-group.m_org_chose_byTree').removeClass('open');//关闭浮窗

            }).on('ready.jstree', function (e, data) {//loaded.jstree

                var tree = $(that.element).find('#orgTreeH').jstree(true);
                tree.open_all();
                tree.select_node(that._currentCompanyId);
                if(that.settings.$selectedId!=null){
                    tree.select_node(that.settings.$selectedId);
                }
                if(that.settings.$renderCallBack!=null){
                    that.settings.$renderCallBack();
                }
                //阻止浮窗关闭
                $(that.element).find('#orgTreeH').on('click',function (e) {
                    e.stopPropagation();
                });

            }).on('after_open.jstree', function (e, data) {//load_node.jstree
                that.dealTreeIconColorFun(data.node.original.id);
                var tree = $(that.element).find('#orgTreeH').jstree(true);
                //禁用根节点选择
                if(that.settings.$type==1) {

                    tree.disable_node('#root');
                }


            }).on('open_node.jstree', function (e, data) {//open_node.jstree

            }).on('close_node.jstree', function (e, data) {//close_node.jstree

            });
        }
        //树icon颜色处理
        , dealTreeIconColorFun:function (id) {
            var that = this;
            $(that.element).find('#orgTreeH').find('li[id="'+id+'"]>ul>li').each(function () {
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
