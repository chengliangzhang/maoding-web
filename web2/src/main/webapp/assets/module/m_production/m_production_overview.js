/**
 * 项目信息－生产安排
 * Created by wrb on 2017/2/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_production_overview",
        defaults = {
            $projectInfo:null,
            $projectId:null,
            $getCallBack:null//请求渲染html后回滚事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this._productionList = [];//当前生产总览
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._initHtml();
        },
        //初始化数据,生成html
        _initHtml:function (callBack) {
            var that = this;
            var options={};
            options.classId = '#productionList';
            options.url=restApi.url_getProductTaskOverview+'/'+that.settings.$projectId;

            m_ajax.get(options,function (response) {

                if(response.code=='0'){

                    that._productionList = response.data;

                    var html = template('m_production/m_production_overview',{});
                    $(that.element).html(html);
                    var html1 = template('m_production/m_production_overview_list',{productionList:response.data});
                    $(that.element).find('#productionList').html(html1);
                    $(that.element).find('.tree').treegrid(
                        {
                            /*expanderExpandedClass: 'fa fa-minus-square-o',
                            expanderCollapsedClass: 'fa fa-plus-square-o',*/
                            expanderExpandedClass: 'icon iconfont icon-iconfontttpodicon2',
                            expanderCollapsedClass: 'icon iconfont icon-shouqi',
                            treeColumn: 0
                        }
                    );
                    //设校审tooltip
                    $(that.element).find('span[tooltip-type]').each(function () {
                        var $this = $(this);
                        var $i = $this.closest('tr').attr('data-i');
                        var tooltipType = $this.attr('tooltip-type');
                        switch (tooltipType){
                            case '1':
                                var userList = that._productionList[$i].designUser.userList;
                                that.toolTipRender($this,userList);
                                break;
                            case '2':
                                var userList = that._productionList[$i].checkUser.userList;
                                that.toolTipRender($this,userList);
                                break;
                            case '3':
                                var userList = that._productionList[$i].examineUser.userList;
                                that.toolTipRender($this,userList);
                                break;
                        }
                    });
                    stringCtrl('taskName');
                    stringCtrl('taskRemark');
                    if(that.settings.$getCallBack!=null){
                        that.settings.$getCallBack();
                    }

                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //设校审－已提交未提交tooltip
        ,toolTipRender:function ($this,userList) {
            var that = this;
            var submittedUserList = [],unSubmittedUserList=[];
            $.each(userList,function (index,item) {
                if(item.completeTime==null){
                    unSubmittedUserList.push(item);
                }else{
                    submittedUserList.push(item);
                }
            });
            var iHtml = template('m_production/m_production_list_usertip',{submittedUserList:submittedUserList,unSubmittedUserList:unSubmittedUserList});
            $this.attr('data-original-title',iHtml);
            $this.tooltip({html : true });
        }
        //渲染头部展示信息
        ,_renderHeaderInfo:function () {
            var that=this,option = {};
            option.url = restApi.url_getProjectInfoForTask+'/'+that.settings.$projectId;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    var html = template('m_production/m_production_overview_header',response.data);
                    $(that.element).find('#productionOverviewHeader').html(html);

                } else {
                    S_dialog.error(response.info);
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
