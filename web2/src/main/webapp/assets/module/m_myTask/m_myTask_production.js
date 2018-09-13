/**
 * 我的任务－生产安排
 * Created by wrb on 2017/11/9.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_myTask_production",
        defaults = {
            $projectId:null,
            $projectName:null,
            $enterType:null//'approved'=任务负责人审核
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this._currentCompanyUserId = window.currentCompanyUserId;//当前组织人员ID
        this._currentUserId = window.currentUserId;//当前用户ID
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
            options.classId = '#project_detail';
            options.url=restApi.url_getDesignTaskList;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            options.postData.type=2;
            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){

                    var currentManagerObj = {};//当前设计负责人、助理
                    currentManagerObj.projectManager = response.data.projectManager;
                    currentManagerObj.assistant = response.data.assistant;

                    var html = template('m_myTask/m_myTask_production',{
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
                    option.$taskIssueOrgList = response.data.projectManagerDataDTOList;
                    option.$projectCreateBy = response.data.projectCreateBy;
                    option.$projectCompanyId = response.data.projectCompanyId;
                    option.$partB = response.data.partB;
                    option.$productionList = response.data.projectDesignContentShowList;
                    option.$projectName = that.settings.$projectName;

                    if(that.settings.$scrollCallBack!=null){
                        option.$scrollCallBack = that.settings.$scrollCallBack;
                    }

                    option.$pageEntrance = 'myProductionTask';

                    $(that.element).find('#productionList').m_production_list(option);

                }else {
                    S_layer.error(response.info);
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
