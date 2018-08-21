/**
 * 项目信息－生产安排
 * Created by wrb on 2017/2/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_production",
        defaults = {
            $projectInfo:null,
            $projectId:null,
            $projectName:null,
            $scrollCallBack:null//请求渲染html后回滚事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this.currentCompanyUserId = window.currentCompanyUserId;//当前用户ID
        this._productionList = null;
        this._tabList = null;//tab页list
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderPage();
        }
        //渲染界面
        ,renderPage:function () {
            var that = this;
            var options={};
            options.classId = '#content-right';
            options.url=restApi.url_getDesignManagerInfo;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;

            var companyId = $(that.element).find('select[name="viewByOrg"]').val();
            if(companyId!=undefined && companyId!=''){
                options.postData.companyId = companyId;
            }
            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){

                    var currentManagerObj = {};//当前设计负责人、助理
                    currentManagerObj.projectManager = response.data.projectManager;
                    currentManagerObj.assistant = response.data.assistant;

                    var html = template('m_production/m_production',{
                        projectName:that.settings.$projectName,
                        orgList:response.data.orgList,
                        dataCompanyId:response.data.dataCompanyId,
                        currentCompanyId:that._currentCompanyId,
                        currentManagerObj:currentManagerObj,
                        currentCompanyUserId:window.currentCompanyUserId
                    });
                    $(that.element).html(html);
                    that.renderTabList(0);
                    that.bindClickAction();
                    that.initSelectByView();
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //渲染tab页
        ,renderTabList:function (i) {
            var that = this;
            var companyId = $(that.element).find('select[name="viewByOrg"]').val();

            var options={};
            options.classId = '#content-right';
            options.url=restApi.url_getTaskGroupInfo;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            options.postData.type=2;
            if(companyId!=undefined && companyId!=''){
                options.postData.companyId = companyId;
            }
            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){
                    that._tabList = response.data.tabList;
                    if(that._tabList!=null && that._tabList.length>0){

                        var html = template('m_production/m_production_tab',{
                            tabList : that._tabList,
                            currTabI :i
                        });
                        $(that.element).find('#tabContent').html(html);
                        that.bindTabClickAction();
                        that.renderTaskList(i);
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //初始化数据,生成html
        ,renderTaskList:function (i) {

            var that = this;
            var companyId = $(that.element).find('select[name="viewByOrg"]').val();

            var options={};
            options.classId = '.ibox-content';
            options.url=restApi.url_getDesignTaskList;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            options.postData.type=2;
            if(that._tabList!=null && that._tabList.length>0) {
                options.postData.issueTaskId = that._tabList[i].id;
            }
            if(companyId!=undefined && companyId!=''){
                options.postData.companyId = companyId;
            }
            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){

                    /*var currentManagerObj = {};//当前设计负责人、助理
                    currentManagerObj.projectManager = response.data.projectManager;
                    currentManagerObj.assistant = response.data.assistant;

                    var html = template('m_production/m_production',{
                        tabList : that._tabList,
                        currTabI :i,
                        projectName:that.settings.$projectName,
                        orgList:response.data.orgList,
                        dataCompanyId:response.data.dataCompanyId,
                        currentCompanyId:that._currentCompanyId,
                        currentManagerObj:currentManagerObj,
                        currentCompanyUserId:window.currentCompanyUserId
                    });
                    $(that.element).html(html);*/
                    var option = {};
                    option.$projectId = that.settings.$projectId;
                    option.$taskIssueOrgList = response.data.projectManagerDataDTOList;
                    option.$projectCreateBy = response.data.projectCreateBy;
                    option.$projectCompanyId = response.data.projectCompanyId;
                    option.$productionList = response.data.projectDesignContentShowList;
                    option.$projectName = that.settings.$projectName;

                    if(that.settings.$scrollCallBack!=null){
                        option.$scrollCallBack = that.settings.$scrollCallBack;
                    }

                    option.$pageEntrance = 'production';
                    $(that.element).find('#productionList'+i).m_production_list(option);

                    if(i>0){
                        that.renderTaskIssue(i);
                    }

                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //渲染签发信息界面
        ,renderTaskIssue:function (i) {
            var that = this;
            var option = {};
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            if(that._tabList!=null && that._tabList.length>0) {
                option.$issueTaskId = that._tabList[i].id;
            }
            option.$companyId = $(that.element).find('select[name="viewByOrg"]').val();
            $(that.element).find('#taskIssueList'+i).m_production_taskIssue(option);
        }
        //视图切换
        ,initSelectByView:function () {
            var that = this;
            $(that.element).find('select[name="viewByOrg"]').select2({
                allowClear: false,
                containerCssClass:'select-sm',
                language: "zh-CN",
                minimumResultsForSearch: -1
            });
            $(that.element).find('select[name="viewByOrg"]').on("change", function (e) {
                that.renderPage();
            })
        }
        //更改设计负责人或设计人事件
        ,managerChangeEvent:function ($this) {
            var that = this;
            var action = $this.attr('data-action');
            var companyId = $this.attr('data-company-id');//当前要获取的项目立项组织的index

            if (action == 'changeOperatorPerson') {
                var operatorPersonId = $this.attr('data-id'),userName=$this.attr('data-user-name');

                var option = {};
                option.$type = 1;
                option.$title = '选择设计负责人';
                option.$selectedUserList = [{
                    id:operatorPersonId,
                    userName:userName
                }];
                option.$selectUserCallback = function (data, event) {//1
                    data.type = 1;
                    var targetUser='<strong style="color:red;margin:0 3px;">'+data.userName+'</strong>';
                    S_dialog.confirm('确定将设计负责人更换为'+targetUser+'？', function () {
                        that.postManagerChange(data, companyId, event);
                    }, function () {
                        //S_dialog.close($(event));
                    });
                };
                $('body').m_changeOperator(option);

            } else if (action == 'changeManagerPerson') {

                var isFirstSetDesign = $.trim($this.text()) == '未设置' ? true : false;
                var designPersonId = $this.attr('data-id'),userName=$this.attr('data-user-name');
                var options = {};
                options.url = restApi.url_getOrgTree;
                options.isASingleSelectUser = true;
                options.delChoseUserCallBack = function () {
                    $(that.element).find('input[name="chooseManager"]').removeAttr('companyUserId');
                    $(that.element).find('input[name="chooseManager"]').val('');
                };
                options.title = '选择助理';
                if(designPersonId!=undefined && designPersonId!=''){
                    options.selectedUserList = [{
                        id:designPersonId,
                        userName:userName
                    }];
                }

                options.isOkSave = true;
                options.saveCallback = function (data, event) {
                    var obj = {};
                    obj.type = 2;
                    if(data!=null && data.selectedUserList!=null && data.selectedUserList.length>0){
                        obj.userName = data.selectedUserList[0].userName;
                        obj.companyUserId = data.selectedUserList[0].id;
                        obj.isFirstSetDesign = isFirstSetDesign;
                        var targetUser='<strong style="color:red;margin:0 3px;">'+obj.userName+'</strong>';
                        S_dialog.confirm('确定将助理更换为'+targetUser+'？', function () {
                            that.postManagerChange(obj, companyId, event);
                        }, function () {
                        });
                    }else{
                        obj.companyUserId=null;
                        S_dialog.confirm('确定将助理置空？', function () {
                            that.postManagerChange(obj, companyId, event);
                        }, function () {
                        });
                    }
                };
                $('body').m_orgByTree(options);
            }
            return false;
        }
        //移交设计负责人或助理的请求
        ,postManagerChange: function (data, companyId, event) {
            var that = this;
            var option = {};

            option.postData = {};
            if(data.type==2){//助理
                option.url = restApi.url_updateProjectAssistant;
                option.postData.type = '2';
            }else{//设计负责人
                if (data.isFirstSetDesign != null && data.isFirstSetDesign == false) {
                    option.url = restApi.url_transferTaskDesigner;
                } else {
                    option.url = restApi.url_updateProjectManager;
                }
                option.postData.type = '2';
            }
            option.postData.projectId = that.settings.$projectId;
            option.postData.companyId = companyId;
            option.postData.companyUserId = data.companyUserId;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_dialog.close($(event));
                    S_toastr.success('保存成功！');
                    that.refreshPage();
                } else {
                    S_dialog.error(response.info);
                }
            })
        }
        ,refreshPage:function () {
            var option = {}, that = this;
            var scrollTop = $('body').scrollTop();
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            option.$scrollCallBack = function () {
                $('body').scrollTop(scrollTop);
            };
            $('#content-right').m_production(option);
        }
        //事件绑定
        ,bindClickAction:function(){
            var that = this;

            $(that.element).find('a[data-action]').off('click').on('click',function(){
                var $this = $(this),
                    $i = $this.attr('data-i'),
                    dataAction = $this.attr('data-action');

                switch (dataAction) {//切换自己生产与总览

                    case 'changeOperatorPerson'://修改当前组织设计负责人

                        that.managerChangeEvent($this);
                        break;
                    case 'changeManagerPerson'://修改当前组织设计助理

                        that.managerChangeEvent($this);
                        break;
                }

            });
        }
        //渲染tab页生产安排
        ,bindTabClickAction:function () {
            var that = this;
            $(that.element).find('a[data-action="getProductionById"]').off('click').on('click',function(){
                var $this = $(this),
                    $i = $this.attr('data-i');

                if($(that.element).find('#productionList'+$i).children().length==0){//未渲染过，需要渲染

                    that.renderTaskList($i);
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
