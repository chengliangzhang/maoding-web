/**
 * 我的任务－任务签发
 * Created by wrb on 2017/11/9.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_myTask_taskIssue",
        defaults = {
            $projectId:null,
            $projectName:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this._currentTaskIssueType = 0;//0=当前组织签发，1=总览
        this._taskIssueOptionData = null;//签发列表数据
        this._optionDataByPartnerships = null;//签发组织关系数据
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtml();
        },
        //初始化数据,生成html
        initHtml:function () {

            var that = this;
            var options={};
            options.classId = '#project_content';
            options.url=restApi.url_getIssueInfo;
            options.postData={
                projectId:that.settings.$projectId
            };

            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){

                    var currentManagerObj = {};//当前经营负责人、助理
                    currentManagerObj.projectManager = response.data.projectManager;
                    currentManagerObj.assistant = response.data.assistant;

                    var html = template('m_myTask/m_myTask_taskIssue',{
                        projectName:that.settings.$projectName,
                        orgList:response.data.orgList,
                        dataCompanyId:response.data.dataCompanyId,
                        currentCompanyId:that._currentCompanyId,
                        currentManagerObj:currentManagerObj,
                        currentCompanyUserId:window.currentCompanyUserId
                    });
                    $(that.element).html(html);
                    var option = {};
                    option.$projectId = that.settings.$projectId;
                    option.$projectName = that.settings.$projectName;
                    option.$projectCompanyId = response.data.companyId;
                    option.$projectCreateBy = response.data.createBy;
                    option.$projectManagerId = response.data.managerId;
                    option.$currentManagerObj = currentManagerObj;
                    option.$taskIssueList = response.data.contentTaskList;
                    option.$dataCompanyId = response.data.dataCompanyId;

                    option.$pageEntrance = 'myIssueTask';

                    $(that.element).find('#taskIssueList').m_taskIssue_list(option);

                }else {
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
