/**
 * 项目信息－生产安排-签发信息
 * Created by wrb on 2018/7/17.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_production_taskIssue",
        defaults = {
            $projectId:null,
            $projectName:null,
            $issueTaskId:null,//签发节点ID
            $companyId:null,//视图切换传过来的组织ID
            $scrollCallBack:null//请求渲染html后回滚事件
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
            that.renderPage();
        }


        //初始化数据,生成html
        ,renderPage:function () {

            var that = this;
            var options={};
            options.classId = '.ibox-content';
            options.url=restApi.url_getIssueInfo;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            options.postData.issueTaskId = that.settings.$issueTaskId;
            if(that.settings.$companyId!=null){
                options.postData.companyId = that.settings.$companyId;
            }
            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){

                    var html = template('m_production/m_production_taskIssue',{
                        contentTaskList:response.data.contentTaskList
                    });
                    $(that.element).html(html);
                    that.bindClickAction();
                }else {
                    S_layer.error(response.info);
                }
            })
        }

        //事件绑定
        ,bindClickAction:function(){
            var that = this;

            $(that.element).find('a[data-action]').bind('click',function(){
                var $this = $(this),
                    $i = $this.attr('data-i'),
                    dataAction = $this.attr('data-action');

                switch (dataAction) {//切换自己生产与总览

                    case 'deliveryHistory':
                        var option = {};
                        option.$projectId = that.settings.$projectId;
                        option.$issueTaskId = that.settings.$issueTaskId;
                        option.$taskId = $this.attr('data-task-id');
                        option.$saveCallBack = function () {
                        };
                        $('body').m_deliveryHistory(option);
                        break;
                    case 'initiateDelivery':
                        var option = {};
                        option.$projectId = that.settings.$projectId;
                        option.$taskId = $this.attr('data-task-id');
                        option.$saveCallBack = function () {
                            that.renderPage();
                        };
                        $('body').m_initiateDelivery(option);
                        break;
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
