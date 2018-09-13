/**
 * 项目信息－任务签发
 * Created by wrb on 2017/2/21.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_taskIssue",
        defaults = {
            $projectId:null,
            $projectName:null,
            $isView:false//是否是查看状态，当是查看状态时，把相对应的权限设为无权限即可
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
            that.initHtml();
        },
        //初始化数据,生成html
        initHtml:function () {

            var that = this;
            var companyId = $(that.element).find('select[name="viewByOrg"]').val();

            var options={};
            options.classId = '#content-right';
            options.url=restApi.url_getIssueInfo;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            if(companyId!=undefined && companyId!=''){
                options.postData.companyId = companyId;
            }

            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){

                    var currentManagerObj = {};//当前经营负责人、助理
                    currentManagerObj.projectManager = response.data.projectManager;
                    currentManagerObj.assistant = response.data.assistant;

                    var html = template('m_taskIssue/m_taskIssue',{
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

                    option.$pageEntrance = 'taskIssue';

                    $(that.element).find('#taskIssueList').m_taskIssue_list(option);
                    that.bindClickAction();
                    that.initSelectByView();
                    $(that.element).find('a[data-toggle="tooltip"]').tooltip();

                }else {
                    S_layer.error(response.info);
                }
            })
        }
        //视图切换
        , initSelectByView:function () {
            var that = this;
            $(that.element).find('select[name="viewByOrg"]').select2({
                allowClear: false,
                containerCssClass:'select-sm',
                language: "zh-CN",
                minimumResultsForSearch: -1
            });
            $(that.element).find('select[name="viewByOrg"]').on("change", function (e) {
                that.initHtml();
            })
        }

        //刷新当前界面
        , refresh: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            $('#content-right').m_taskIssue(option,true);
            return false;
        }

        //更改经营负责人或设计人事件
        , managerChangeEvent:function ($this) {
            var that = this;
            var action = $this.attr('data-action');
            var companyId = $this.attr('data-company-id');//当前要获取的项目立项组织的index

            if (action == 'changeOperatorPerson') {
                var operatorPersonId = $this.attr('data-id'),userName=$this.attr('data-user-name');

                var option = {};
                option.$selectedUserList = [{
                    id:operatorPersonId,
                    userName:userName
                }];
                option.$selectUserCallback = function (data, event) {//1
                    data.type = 1;
                    var targetUser='<strong style="color:red;margin:0 3px;">'+data.userName+'</strong>';
                    S_layer.confirm('确定将经营负责人更换为'+targetUser+'？', function () {
                        that.postManagerChange(data, companyId, event);
                    }, function () {
                        //S_layer.close($(event));
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
                        S_layer.confirm('确定将助理更换为'+targetUser+'？', function () {
                            that.postManagerChange(obj, companyId, event);
                        }, function () {
                        });
                    }else{
                        obj.companyUserId=null;
                        S_layer.confirm('确定将助理置空？', function () {
                            that.postManagerChange(obj, companyId, event);
                        }, function () {
                        });
                    }
                };
                $('body').m_orgByTree(options);
            }

            return false;
        }
        //移交经营负责人或项目负责人的请求
        , postManagerChange: function (data, companyId, event) {
            var that = this;
            var option = {};
            option.postData = {};

            if(data.type==2){
                option.url = restApi.url_updateProjectAssistant;
                option.postData.type = '1';
            }else{
                if (data.isFirstSetDesign != null && data.isFirstSetDesign == false) {
                    option.url = restApi.url_transferTaskDesigner;
                } else {
                    option.url = restApi.url_updateProjectManager;
                }
                option.postData.type = data.type;
            }

            option.postData.projectId = that.settings.$projectId;
            option.postData.companyId = companyId;
            option.postData.companyUserId = data.companyUserId;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_layer.close($(event));
                    S_toastr.success('保存成功！');
                    that.refresh();
                } else {
                    S_layer.error(response.info);
                }
            })
        }
        //事件绑定
        ,bindClickAction:function(){

            var that = this;
            $(that.element).find('a[data-action]').bind('click',function(){
                var $this = $(this),
                    dataAction = $this.attr('data-action');

                switch (dataAction) {//切换自己任务签发与总览

                    case 'changeOperatorPerson'://修改当前组织经营负责人

                        that.managerChangeEvent($this);
                        break;
                    case 'changeManagerPerson'://修改当前组织设计负责人

                        that.managerChangeEvent($this);
                        break;

                    case 'viewProjectInfo'://浏览项目基本信息

                        var option = {};
                        option.$isDialog = true;
                        option.$isView = true;
                        option.$projectId = that.settings.$projectId;
                        $('body').m_projectBasicInfo(option);
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
