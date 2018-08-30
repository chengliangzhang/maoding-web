/**
 * 项目信息－生产安排列表
 * Created by wrb on 2017/2/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_production_list_old",
        defaults = {
            $projectId:null,
            $projectName:null,
            $productionList:null,
            $getCallBack:null,//请求渲染html后回滚事件
            $scrollCallBack:null,//定位scrollTop
            $pageEntrance:'production'//页面入口（我的任务=myProductionTask,生产安排=production）
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
        this._productionList = [];//当前生产列表
        this._designerListByAdd=[];//添加任务－设计人员列表
        this._checkUserListByAdd=[];//添加任务－校对人员列表
        this._examineUserListByAdd=[];//添加任务－审核人员列表
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
            var $data = {};

            $data.productionList = that.settings.$productionList;
            that._productionList = that.settings.$productionList;
            $data.currentCompanyId = that._currentCompanyId;
            $data.currentCompanyUserId = that._currentCompanyUserId;
            $data.currentUserId = that._currentUserId;
            var html = template('m_production/m_production_list',$data);
            $(that.element).html(html);
            $(that.element).find('.tree').treegrid(
                {
                    expanderExpandedClass: 'ic-open',
                    expanderCollapsedClass: 'ic-retract',
                    treeColumn: 1
                }
            );
            that.bindActionClick();
            var tableWidth = $(that.element).find('table.table').width();
            stringCtrl('taskName',tableWidth*0.2);
            if(that.settings.$getCallBack!=null){
                that.settings.$getCallBack();
            }
            if(that.settings.$scrollCallBack!=null){
                that.settings.$scrollCallBack();
            }
            $(that.element).find('span.person-in-charge,a[data-toggle="tooltip"]').tooltip();
            that.editHoverFun();
            //操作列表没有可操作则不展示该按钮组
            $(that.element).find('ul.dropdown-menu').each(function () {
                var len = $(this).find('li a[data-action]').length;
                if(len==0){
                    $(this).parents('.btn-group').remove();
                }
            });

            //设计名称编辑
            $(that.element).find('a[data-action="taskNameEdit"]').each(function () {
                var $this = $(this);
                var taskName = $this.closest('td').find('span.taskName').attr('data-string');
                $this.editable({//编辑
                    type: 'text',
                    mode: 'inline',
                    value:taskName,
                    emptytext:'',
                    savenochange:true,
                    placeholder: '设计任务',
                    onblur:'submit',
                    showbuttons:false,
                    success: function(response, newValue) {
                        if(newValue!=taskName){
                            var param = {};
                            param.taskName = newValue;
                            if(that.saveTaskIssueBySyncEdit($this,param)){
                                return false;
                            }
                        }
                    },
                    display: function(value, sourceData) {
                        $this.closest('td').find('.show-span').show();
                    },
                    validate: function(value) {
                        if($.trim(value) == ''){
                            S_toastr.error('设计任务名称不可为空！');
                            return '不能为空';
                        }
                        if(value.length>50){
                            S_toastr.error('设计任务名称超出字数限制！');
                            return '设计任务名称超出字数限制！';
                        }
                    }
                });
                //显示x-editable事件shown
                $this.on('shown', function(e, editable) {
                    if(!($(that.element).find('.row-edit').length>0)) {
                        $this.closest('td').find('.show-span').hide();
                        var len = taskName.length;
                        $this.next('.editable-container').find('.editable-input').after('<span style="padding: 4px 12px;display: inline-block;"><span class="wordCount">' + len + '</span>/<span>50</span></span>');
                    }else{
                        S_toastr.warning('当前正在创建设计任务中...');
                        $this.editable('hide');
                    }
                    $this.next('.editable-container').find('.editable-error-block').remove();//屏蔽错误提示，改为toastr提示
                    $this.next('.editable-container').find('.editable-input input').on('keyup',function () {//任务名称字数事件
                        var len = $this.next('.editable-container').find('.editable-input input').val().length;
                        $this.next('.editable-container').find('.wordCount').text(len);
                        if(len>50){
                            $this.next('.editable-container').find('.wordCount').addClass('fc-red');
                        }else{
                            if($this.next('.editable-container').find('.wordCount').hasClass('fc-red')){
                                $this.next('.editable-container').find('.wordCount').removeClass('fc-red');
                            }
                        }
                    });
                });
            });
            //任务描述编辑
            $(that.element).find('a[data-action="taskRemarkEdit"]').each(function () {
                var $this = $(this);
                var id = $this.attr('id'),i=$this.closest('TR').attr('data-i'),currentObj = that._productionList[i];
                $this.on('click',function () {

                    if(!($(that.element).find('.row-edit').length>0)) {

                        S_dialog.dialog({
                            title: '任务描述',
                            contentEle: 'dialogOBox',
                            ele:id,
                            lock: 2,
                            quickClose:true,
                            noTriangle:true,
                            width: '320',
                            minHeight:'110',
                            tPadding: '0px',
                            url: rootPath+'/assets/module/m_common/m_dialog.html'

                        },function(d){//加载html后触发

                            var tipsEle = 'div[id="content:'+d.id+'"] .dialogOBox';
                            var remark = currentObj.taskRemark==null?'':currentObj.taskRemark;

                            var html = template('m_production/m_production_editRemark',{
                                remark:remark
                            });
                            $(tipsEle).html(html);

                            $(tipsEle).find('button[data-action="cancel"]').off('click').on('click',function () {
                                S_dialog.close($(tipsEle));
                            });
                            $(tipsEle).find('button[data-action="submit"]').off('click').on('click',function () {
                                var text = $(tipsEle).find('textarea').val();
                                var param = {};
                                param.taskRemark = text;
                                that.saveTaskIssueByEdit($this,param);
                                S_dialog.close($(tipsEle));
                            });

                        });

                    }else{
                        S_toastr.warning('当前正在创建设计任务中...');
                    }
                });
            });
            that.editPriority();
        }
        //优先级编辑
        ,editPriority:function () {
            var that = this;
            $(that.element).find('a[data-action="priorityEdit"]').each(function () {

                var $this = $(this);
                $this.editable({//编辑
                    type: 'select',
                    mode: 'inline',
                    value: $this.attr('data-priority'),
                    source: [
                        {value: 5, text: '紧急'},
                        {value: 4, text: '高'},
                        {value: 3, text: '中'},
                        {value: 2, text: '低'},
                        {value: 1, text: '无关紧要'},
                        {value: 0, text: '无'}
                    ],
                    showbuttons:false,
                    success: function(response, newValue) {
                        if(newValue==''){
                            newValue = null;
                        }
                        var param = {};
                        param.priority = newValue;
                        that.saveTaskIssueByEdit($this,param);
                    },
                    display: function(value, sourceData) {
                        $this.closest('td').find('.show-span').show();
                    }
                });
                //显示x-editable事件shown
                $this.on('shown', function(e, editable) {
                    if($(that.element).find('.row-edit').length>0) {
                        S_toastr.warning('当前正在创建设计任务中...');
                        $this.editable('hide');
                    }else{
                        $this.closest('td').find('.show-span').hide();
                    }
                });
                $this.on('hidden', function(e, reason) {
                    if(reason === 'save' || reason === 'cancel' || reason=== 'onblur') {
                        $this.closest('td').find('.show-span').show();
                    }
                });

            });
        }
        //hover事件
        ,editHoverFun:function () {

            var that = this;
            //文本移上去出现编辑图标hover事件
            $(that.element).find('a[data-action][data-deal-type="edit"]').each(function () {

                var $this = $(this);

                $this.closest('TD').hover(function () {
                    if($this.closest('TD').find('.edit-box').length<=0 && $this.closest('TD').find('.editable-container').length<=0){
                        $this.css('display','block');
                    }
                },function () {
                    if($this.closest('TD').find('.edit-box').length<=0 && $this.closest('TD').find('.editable-container').length<=0) {
                        $this.css('display','none');
                    }
                })
            });
            //TR hover效果
            $(that.element).find('TR').each(function () {
                var $this = $(this);
                var singleOperation = $this.find('.singleOperation');
                $this.hover(function () {
                    singleOperation.show();
                    if(!$this.hasClass('no-data')){
                        $this.addClass('tr-hover');
                    }
                },function () {
                    singleOperation.hide();
                    $this.removeClass('tr-hover');
                });
            });
        }
        //编辑任务名称
        ,saveProductionTask:function (taskId,$this,taskType) {

            var options={},that=this;
            options.url=restApi.url_saveTaskIssuing;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            if(taskType!=null){
                options.postData.taskType =taskType;
            }else{
                options.postData.taskType =0;
            }

            options.postData.companyId = that._currentCompanyId;
            options.postData.taskPid = taskId;
            options.postData.taskName = $.trim($this.closest('tr').find('input[name="taskName"]').val());
            options.postData.taskRemark = $this.closest('tr').next('tr.row-edit').find('input[name="taskRemark"]').val();
            options.postData.startTime = $this.closest('tr').find('input[name="startTime"]').val();
            options.postData.endTime = $this.closest('tr').find('input[name="endTime"]').val();
            options.postData.designerId = $this.closest('tr').find('a[data-action="setTaskLeader"]').attr('data-id');
            options.postData.designUserList = [];
            options.postData.checkUserList = [];
            options.postData.examineUserList = [];

            if(that._designerListByAdd.length>0){
                $.each(that._designerListByAdd,function (index,item) {
                    options.postData.designUserList.push(item.id);
                });
            }
            if(that._checkUserListByAdd.length>0){
                $.each(that._checkUserListByAdd,function (index,item) {
                    options.postData.checkUserList.push(item.id);
                });
            }
            if(that._examineUserListByAdd.length>0){
                $.each(that._examineUserListByAdd,function (index,item) {
                    options.postData.examineUserList.push(item.id);
                });
            }
            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){
                    S_toastr.success('添加成功！');
                    that.refreshPage();
                }else {
                    if(response.info.indexOf('已存在')>-1){
                        S_toastr.error(response.info);
                    }else{
                        S_dialog.error(response.info);
                    }
                }
            })
        }
        //设置进度变更时间
        ,setTheScheduleTime:function ($this) {

            var that = this,taskId = $this.closest('tr').attr('data-id');
            var dataStartTime = $this.attr('data-start-time');
            var dataEndTime = $this.attr('data-end-time');

            var _i = $this.closest('TR').attr('data-i');
            var theCurrentTaskObj = that._productionList[_i];
            var theParentTaskObj = [];
            $.each(that._productionList,function(i,item){
                if(item.id==theCurrentTaskObj.taskPid){
                    theParentTaskObj = item;
                    return ;
                }
            });
            var options = {};
            options.$projectId = that.settings.$projectId;
            options.$projectName = that.settings.$projectName;

            if(dataStartTime=='' && dataEndTime==''){//设置计划进度

                options.$title = '设置计划进度';
                options.$isHaveMemo = false;
                options.$isOkSave = true;
                options.$saveDataUrl = restApi.url_saveProjectProcessTime;
                var $data = {};
                $data.companyId = that._currentCompanyId;
                $data.targetId = taskId;
                $data.type = 2;
                options.$saveData = $data;
                options.$saveTimeKeyVal=['startTime','endTime'];
                options.$okCallBack = function (data) {
                    that.refreshPage();
                };
                $('body').m_inputProcessTime(options);

            }else{//计划进度变更
                options.$theCurrentTaskObj = theCurrentTaskObj;
                $('body').m_productionSchedule(options);
            }
        }
        ,planningSchedule:function ($this) {

            var that = this,taskId = $this.closest('tr').attr('data-id');
            var option = {};
            option.$saveDataUrl = restApi.url_saveProjectProcessTime;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            option.$title = '调整计划进度';
            option.$isOkSave = true;
            option.$validate = 2;
            var $data = {};
            $data.companyId = that._currentCompanyId;
            $data.targetId = taskId;
            $data.type = 2;
            option.$saveData = $data;
            option.$saveTimeKeyVal=['startTime','endTime','memo'];
            option.$okCallBack = function (data) {
                that.refreshPage();
            };
            $('body').m_inputProcessTime(option);
        }
        //刷新当前界面
        ,refreshPage:function () {
            var option = {}, that = this;
            var scrollTop = $('body').scrollTop();
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            option.$scrollCallBack = function () {
                $('body').scrollTop(scrollTop);
            };
            if(that.settings.$pageEntrance == 'myProductionTask'){
                $('#content-right').m_myTask_production(option,true);
            }else{
                $('#content-right').m_production(option);
            }

        }
        //移交任务负责人
        ,postManagerChange:function(data,event){
            var that = this;
            var option={};
            option.url=restApi.url_transferTaskResponse;
            option.postData={};
            option.postData.id = data.id;
            option.postData.taskId = data.taskId;
            option.postData.projectId=that.settings.$projectId;
            option.postData.companyId=that._currentCompanyId;
            option.postData.type=data.type;
            option.postData.targetId=data.companyUserId;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_dialog.close($(event));
                    S_toastr.success('保存成功！');
                    that.refreshPage();
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //编辑签发保存
        ,saveTaskIssueByEdit:function ($this,param) {
            var options={},that=this;
            options.url=restApi.url_updateTaskInfo;
            options.postData = {};
            options.postData.id = $this.closest('tr').attr('data-id');
            $.extend(options.postData, param);
            m_ajax.postJson(options,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    that.refreshPage();
                }else {
                    S_dialog.error(response.info);
                }
            });
        }
        ,saveTaskIssueBySyncEdit:function ($this,param) {
            var options={},that=this;
            options.url=restApi.url_updateTaskInfo;
            options.async = false;
            options.postData = {};
            options.postData.id = $this.closest('tr').attr('data-id');
            $.extend(options.postData, param);
            var isError = false;
            m_ajax.postJson(options,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    that.refreshPage();
                }else {
                    if(response.info.indexOf('已存在')>-1){
                        S_toastr.error(response.info);
                        isError = true;
                    }else{
                        S_dialog.error(response.info);
                    }
                }
            });
            return isError;
        }
        //编辑签发进度变更保存
        ,saveProgressChangeByEdit:function ($this,param) {
            var options={},that=this;
            options.url=restApi.url_saveProjectProcessTime;
            options.postData = {};
            options.postData.targetId = $this.closest('tr').attr('data-id');
            options.postData.companyId = that._currentCompanyId;
            options.postData.type = 2;
            $.extend(options.postData, param);
            m_ajax.postJson(options,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    that.refreshPage();
                }else {
                    S_dialog.error(response.info);
                }
            });
        }
        //保存参与人员
        ,saveTaskParticipant:function ($this,t) {

            var that = this;
            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_saveOrUpdateProcess;
            var param ={};
            var nodes = [];
            var dataI = $this.closest('tr').attr('data-i');
            if(t==1) {
                param.memberType = that._productionList[dataI].designUser.memberType;
                nodes = that.getChosedUserList(that._designerListByAdd,that._productionList[dataI].designUser);
            }else if(t==2){
                param.memberType = that._productionList[dataI].checkUser.memberType;
                nodes = that.getChosedUserList(that._checkUserListByAdd,that._productionList[dataI].checkUser);
            }else if(t==3){
                param.memberType = that._productionList[dataI].examineUser.memberType;
                nodes = that.getChosedUserList(that._examineUserListByAdd,that._productionList[dataI].examineUser);
            }
            param.nodes = nodes;
            param.projectId = that.settings.$projectId;
            param.taskManageId = $this.closest('tr').attr('data-id');
            option.postData = param;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    that.refreshPage();
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //向上、向下移动保存
        ,saveTaskMoveInSeq:function (taskList) {

            var that = this;
            var option = {};
            option.url = restApi.url_exchangeTask;
            option.postData = [];
            option.postData = taskList;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('更新成功');
                    that.refreshPage();
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //选择的人员，跟旧数据对比，并返回
        ,getChosedUserList:function (newList,oldList) {
            var nodes = [];
            if (newList.length > 0) {
                $.each(newList, function (index, item) {

                    var obj = {};
                    obj.companyUserId = item.id;
                    obj.userName = item.userName;
                    obj.memberType = oldList.memberType;
                    obj.nodeName = oldList.nodeName;

                    if (oldList.userList != null && oldList.userList.length > 0) {

                        $.each(oldList.userList, function (i, t) {//跟旧数据对比
                            if (item.id == t.companyUserId) {
                                obj.id = t.id;
                                return false;
                            }
                        });
                    }
                    nodes.push(obj);
                });
            }
            return nodes;
        }
        //添加设计任务
        ,addTaskRow:function ($this,taskType) {
            var that = this;
            var taskId = $this.closest('tr').attr('data-id');
            var dataI = $this.closest('tr').attr('data-i');
            var currentTaskObj = that._productionList[dataI];
            var html =  template('m_production/m_production_list_add', {
                personInCharge:currentTaskObj.personInCharge,
                personInChargeId:currentTaskObj.personInChargeId,
                taskType:taskType
            });
            if(taskId!=null){
                if($(that.element).find('tr[data-pid="'+taskId+'"]').length>0){//存在子任务

                    $(that.element).find('tr[data-pid="'+taskId+'"]:last').after(html);
                }else{
                    $(that.element).find('tr[data-id="'+taskId+'"]').after(html);
                }
                //把父任务时间带过来
                var $pEle = $(that.element).find('tr[data-id="'+taskId+'"]');
                var sTime = $pEle.find('span.show-span[data-type="planTime"]').attr('data-start-time');
                var eTime = $pEle.find('span.show-span[data-type="planTime"]').attr('data-end-time');
                $(that.element).find('.row-edit input[name="startTime"]').val(sTime);
                $(that.element).find('.row-edit input[name="endTime"]').val(eTime);

                //父任务加底色显示
                $(that.element).find('tr[data-id="'+taskId+'"]').css('background-color','#d7d7d7');

            }else{
                $(that.element).find('tr:last').after(html);
            }
            that.timeIconClick1($('.row-edit'));
            $(that.element).find('.row-edit a[data-action]').on('click',function (e) {//选择人员事件

                var dataAction = $(this).attr('data-action');
                switch (dataAction){
                    case "setTaskLeader"://设置任务负责人
                        that.choseTaskLeader($(this),0);
                        break;
                    case "setTaskDesigner"://设置设计人员

                        that.choseUser($(this),1);
                        break;
                    case "setTaskCheckUser"://设置校对人员
                        that.choseUser($(this),2);
                        break;
                    case "setTaskExamineUser"://设置审核人员
                        that.choseUser($(this),3);
                        break;
                }
                e.stopPropagation();
            });

            $(that.element).find('button[data-action="cancel"]').on('click',function () {//取消事件
                $(this).closest('tr').next('tr.row-edit').remove();
                $(this).closest('tr.row-edit').remove();
                $(that.element).find('tr[data-id="'+taskId+'"]').removeAttr('style');
            });
            $(that.element).find('button[data-action="submit"]').on('click',function (e) {//提交事件

                /****************** 验证 - 开始**********************/

                var taskName = $.trim($(this).closest('tr.row-edit').find('input[name="taskName"]').val());
                var isRepeatName = false;//任务名称是否重名

                if(taskName==''){
                    S_toastr.error('设计任务名称不可为空！');
                    return false;
                }
                if(taskName.length>50){
                    S_toastr.error('设计任务名称超出字数限制！');
                    return false;
                }
                /*if(that._productionList!=null && that._productionList.length>0){
                    $.each(that._productionList,function (index,item) {
                        if(taskName==item.taskName){
                            isRepeatName = true;
                            return false;
                        }
                    })
                }
                if(isRepeatName){
                    S_toastr.error(taskName+'已存在！');
                    return false;
                }*/
                /****************** 验证 - 结束**********************/
                that.saveProductionTask(taskId,$(this),taskType);
                e.stopPropagation();
                return false;
            });
            $(that.element).find('input[name="taskName"]').on('keyup',function () {//任务名称字数事件
                var len = $(that.element).find('input[name="taskName"]').val().length;
                $(that.element).find('.wordCount').text(len);
                if(len>50){
                    $(that.element).find('.wordCount').addClass('fc-red');
                }else{
                    if($(that.element).find('.wordCount').hasClass('fc-red')){
                        $(that.element).find('.wordCount').removeClass('fc-red');
                    }
                }
            });
            //绑定回车事件
            $(that.element).find('input[name="taskName"]').keydown(function(e) {
                if (event.keyCode == '13') {//keyCode=13是回车键
                    $(that.element).find('button[data-action="submit"]').click();
                    preventDefault(e);
                }
            });
        }
        //选择任务负责人
        ,choseTaskLeader:function ($this,t) {
            var that = this;
            var personInChargeId = $this.attr('data-id'),userName=$this.attr('data-user-name');
            var options = {};
            options.selectedUserList = [{
                id:personInChargeId,
                userName:userName
            }];
            options.url = restApi.url_getOrgTree;
            options.isASingleSelectUser = true;
            options.delChoseUserCallBack = function () {

            };
            options.title = '选择任务负责人';
            options.choseUserCallback = function (data,event) {

                var targetUser='<strong style="color:red;margin:0px 3px;">'+data.userName+'</strong>';
                var confirmTitle = '确定安排'+targetUser+'为新的任务负责人？';
                S_dialog.confirm(confirmTitle,function(){

                    if(t==0){//添加情况下
                        $this.attr('data-id',data.companyUserId);
                        $this.attr('data-user-name',data.userName);
                        $this.prev().find('span').html(data.userName);
                        S_dialog.close($(event));
                    }else{//编辑状态下
                        data.type = 1;
                        data.id = $this.attr('data-id');//旧任务负责人
                        data.taskId = $this.closest('tr').attr('data-id');
                        that.postManagerChange(data,event);
                    }
                },function(){
                    //S_dialog.close($(event));
                });
            };
            $('body').m_orgByTree(options);
        }
        //选择设校审人员
        ,choseUser:function ($this,t) {
            var that=this,options = {};
            var editType = $this.attr('data-deal-type');
            if(t==1){//设计人员
                options.title = '选择设计人员';
                options.selectedUserList = that._designerListByAdd;
            }else if(t==2){//校对
                options.title = '选择校对人员';
                options.selectedUserList = that._checkUserListByAdd;
            }else if(t==3){//审核
                options.title = '选择审核人员';
                options.selectedUserList = that._examineUserListByAdd;
            }

            options.url = restApi.url_getOrgTree;
            options.delChoseUserCallBack = function () {

            };
            options.saveChoseUserCallback = function (data) {

                if(t==1){//设计人员
                    that._designerListByAdd=data.selectedUserList;
                }else if(t==2){//校对
                    that._checkUserListByAdd=data.selectedUserList;
                }else if(t==3){//审核
                    that._examineUserListByAdd=data.selectedUserList;
                }
                if(editType=='edit'){//编辑情况下

                    that.saveTaskParticipant($this,t);
                }else{//添加情况下
                    that.renderChoseUserShow($this,data.selectedUserList);
                }

            };
            options.renderTreeCallBack = function () {

                //把已提交的人员的删除按钮去掉，不可删除
                $('.orgUserTreeOBox .chosedUserBox').find('a[data-action="delChosedUser"]').each(function () {
                    var $this = $(this);
                    var dataId = $this.attr('data-id');

                    if(options.selectedUserList!=null && options.selectedUserList.length>0){
                        $.each(options.selectedUserList,function (index,item) {
                            if(dataId==item.id && item.completeTime!=null){
                                $this.remove();
                            }
                        })
                    }
                });

            };
            $('body').m_orgByTree(options);
        }
        //选择人员后展示在前端处理
        ,renderChoseUserShow:function ($this,userList) {
            var that = this;
            var tiphtml = '';
            var userShowObj = null;
            if(userList!=null && userList.length>0){
                $.each(userList,function (index,item) {
                    if(that._currentCompanyUserId==item.id){//当前人员展示在界面
                        userShowObj = item;
                    }
                    tiphtml += item.userName+',';
                });
                tiphtml = tiphtml.substring(0,tiphtml.length-1);
                if(userShowObj==null){
                    userShowObj = userList[0];//当前所选的人员不存在自己，取第一个
                }
                $this.closest('td').find('.show-span span').html(userShowObj.userName);
                $this.closest('td').find('.show-span').attr('data-original-title',tiphtml);
                $this.closest('td').find('span[data-toggle="tooltip"]').tooltip({html : true });
            }
        }

        //编辑前清空所有编辑情况
        ,removeAllEditBox:function () {
            var that = this;
            $(that.element).find('.reign-edit-box').parents('td').find('.show-span').show();
            $(that.element).find('.reign-edit-box').remove();
            $(that.element).find('.editable').parents('td').find('.show-span').show();
            $(that.element).find('.editable').editable('hide');
        }
        //时间图标事件点击
        ,timeIconClick:function ($this) {
            $this.next().find('.fa-calendar').off('click').on('click',function (e) {
                $(this).closest('.input-group').find('input[name="ipt_time"]').click();
                e.stopPropagation();
            });
        }
        //时间图标事件点击
        ,timeIconClick1:function ($this) {
            $this.find('.fa-calendar').off('click').on('click',function (e) {
                $(this).closest('.input-group').find('input.timeInput').focus();
                e.stopPropagation();
            });
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click',function (e) {

                var $this = $(this),$dataAction = $this.attr('data-action');
                var $taskId = $this.closest('tr').attr('data-id');
                var $i = $this.closest('tr').attr('data-i')-0;
                switch ($dataAction) {

                    case 'setPersonInCharge'://设置任务负责人
                        that.removeAllEditBox();
                        that.choseTaskLeader($this,1);
                        e.stopPropagation();
                        break;
                    case 'delTask'://删除任务

                        S_dialog.confirm('删除后将不能恢复，您确定要删除吗？', function () {
                            var option = {};
                            option.url = restApi.url_deleteProjectTask;
                            option.postData = {};
                            option.postData.id = $taskId;
                            m_ajax.postJson(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('删除成功！');
                                    that.refreshPage();
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });

                        }, function () {
                        });
                        break;
                    case 'addSubTask'://分解子任务
                        that.removeAllEditBox();
                        if(!($(that.element).find('.row-edit').length>0)){
                            that.addTaskRow($this);
                            //置空数据
                            that._designerListByAdd=[];//添加任务－设计人员列表
                            that._checkUserListByAdd=[];//添加任务－校对人员列表
                            that._examineUserListByAdd=[];//添加任务－审核人员列表
                            $this.attr('disabled',true);
                        }else{
                            S_toastr.warning('当前正在创建设计任务中...');
                        }
                        //e.stopPropagation();
                        break;
                    case 'addSubTaskByDesigner':
                        that.removeAllEditBox();
                        if(!($(that.element).find('.row-edit').length>0)){
                            that.addTaskRow($this,5);//taskType=5
                            //置空数据
                            that._designerListByAdd=[];//添加任务－设计人员列表
                            that._checkUserListByAdd=[];//添加任务－校对人员列表
                            that._examineUserListByAdd=[];//添加任务－审核人员列表
                            $this.attr('disabled',true);
                        }else{
                            S_toastr.warning('当前正在创建设计任务中...');
                        }
                        break;
                    case 'setTaskDesigner'://设设计人员
                        that.removeAllEditBox();
                        var designUser = that._productionList[$i].designUser;
                        that._designerListByAdd = [];
                        if(designUser!=null && designUser.userList!=null && designUser.userList.length>0){

                            $.each(designUser.userList,function (index,item) {
                                that._designerListByAdd.push({
                                    id:item.companyUserId,
                                    userName:item.userName,
                                    completeTime:item.completeTime
                                })
                            })
                        }
                        that.choseUser($this,1);
                        break;
                    case 'setTaskCheckUser'://设校对人员
                        var checkUser = that._productionList[$i].checkUser;
                        that._checkUserListByAdd = [];
                        if(checkUser!=null && checkUser.userList!=null && checkUser.userList.length>0){

                            $.each(checkUser.userList,function (index,item) {
                                that._checkUserListByAdd.push({
                                    id:item.companyUserId,
                                    userName:item.userName,
                                    completeTime:item.completeTime
                                })
                            })
                        }
                        that.choseUser($this,2);
                        break;
                    case 'setTaskExamineUser'://设审核人员
                        that.removeAllEditBox();
                        var examineUser = that._productionList[$i].examineUser;
                        that._examineUserListByAdd = [];
                        if(examineUser!=null && examineUser.userList!=null && examineUser.userList.length>0){

                            $.each(examineUser.userList,function (index,item) {
                                that._examineUserListByAdd.push({
                                    id:item.companyUserId,
                                    userName:item.userName,
                                    completeTime:item.completeTime
                                })
                            })
                        }
                        that.choseUser($this,3);
                        break;
                    case 'startTimeEdit'://开始时间编辑

                        if(!($(that.element).find('.row-edit').length>0)){
                            that.removeAllEditBox();
                            that.setTheScheduleTime($this);
                            e.stopPropagation();
                        }else{
                            S_toastr.warning('当前正在创建设计任务中...');
                        }
                        break;
                    case 'endTimeEdit'://开始时间编辑

                        if(!($(that.element).find('.row-edit').length>0)){
                            that.removeAllEditBox();
                            that.setTheScheduleTime($this);
                            e.stopPropagation();
                        }else{
                            S_toastr.warning('当前正在创建设计任务中...');
                        }
                        break;
                    case 'planningSchedule'://计划进度

                        if(!($(that.element).find('.row-edit').length>0)){
                            that.removeAllEditBox();
                            that.planningSchedule($this);
                            e.stopPropagation();
                        }else{
                            S_toastr.warning('当前正在创建设计任务中...');
                        }

                        break;
                    case 'viewProgressChange'://获取时间变更列表

                        var option = {};
                        option.$taskId = $taskId;
                        option.$publishId = $this.closest('tr').attr('data-id');
                        option.$type = 2;
                        option.$eleId = $this.attr('id');
                        $('body').m_schedule_list(option);
                        e.stopPropagation();
                        break;
                    case 'completeTask'://确认完成

                        var tipStr = $this.attr('data-original-title');
                        var dataStatus = $this.attr('data-status');
                        if(dataStatus=='0'){//激活

                            S_dialog.confirm(tipStr+'？',function () {
                                var option = {};
                                //option.url = restApi.url_completeTask+'/'+$taskId+'/'+dataStatus;
                                option.url = restApi.url_completeTask;
                                option.postData = {
                                    taskId:$taskId,
                                    status:dataStatus
                                };
                                m_ajax.postJson(option,function (response) {
                                    if (response.code == '0') {
                                        S_toastr.success('操作成功');
                                        that.refreshPage();
                                    } else {
                                        S_dialog.error(response.info);
                                    }
                                });
                            },function () {
                            });
                        }else{//完成

                            var option = {};
                            option.$taskId = $taskId;
                            option.$status = dataStatus;
                            option.$saveCallBack = function () {
                                that.refreshPage();
                            };
                            $('body').m_confirmCompletion(option);
                        }
                        e.stopPropagation();
                        break;

                    case 'moveUp'://向上移动

                        var taskArr = [];
                        taskArr.push({
                            id:that._productionList[$i].id,
                            seq:that._productionList[$i].seq-0
                        });
                        for(var i=$i-1;i<$i;i--){
                            if(that._productionList[i].taskLevel==that._productionList[$i].taskLevel){
                                taskArr.push({
                                    id:that._productionList[i].id,
                                    seq:that._productionList[i].seq-0
                                });
                                break;
                            }
                        }
                        that.saveTaskMoveInSeq(taskArr);

                        break;

                    case 'moveDown'://向下移动

                        var taskArr = [];
                        taskArr.push({
                            id:that._productionList[$i].id,
                            seq:that._productionList[$i].seq
                        });
                        for(var i=$i+1;i<that._productionList.length;i++){
                            if(that._productionList[i].taskLevel==that._productionList[$i].taskLevel){
                                taskArr.push({
                                    id:that._productionList[i].id,
                                    seq:that._productionList[i].seq
                                });
                                break;
                            }
                        }
                        that.saveTaskMoveInSeq(taskArr);
                        break;

                    case 'submitTask'://提交任务(设校审)

                        var dataId = $this.attr('data-id');
                        S_dialog.confirm('确定完成此任务？',function () {

                            var option = {};
                            option.url = restApi.url_updateCompleteTask+'/'+dataId;
                            m_ajax.get(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('操作成功');
                                    that.refreshPage();
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });
                        },function () {
                        });
                        e.stopPropagation();
                        break;
                    case 'activateTask'://提交任务(设校审)

                        var dataId = $this.attr('data-id');
                        S_dialog.confirm('确定激活此任务？',function () {

                            var option = {};
                            option.url = restApi.url_activeProjectTask+'/'+dataId;
                            m_ajax.get(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('操作成功');
                                    that.refreshPage();
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });
                        },function () {
                        });
                        e.stopPropagation();
                        break;

                    case 'viewTaskRemarkEdit'://查看任务描述

                        var id = $this.attr('id'),i=$this.closest('TR').attr('data-i'),currentObj = that._productionList[i];
                        S_dialog.dialog({
                            title: '任务描述',
                            contentEle: 'dialogOBox',
                            ele:id,
                            lock: 2,
                            quickClose:true,
                            noTriangle:true,
                            width: '320',
                            minHeight:'110',
                            tPadding: '0px',
                            url: rootPath+'/assets/module/m_common/m_dialog.html'

                        },function(d){//加载html后触发

                            var tipsEle = 'div[id="content:'+d.id+'"] .dialogOBox';
                            var remark = currentObj.taskRemark==null?'':currentObj.taskRemark;
                            $(tipsEle).html('<div class="m-sm" style="word-break: break-all;">'+remark+'</div>');

                        });
                        e.stopPropagation();
                        break;
                    case 'viewTaskCompletion'://查看任务完成情况

                        var id = $this.attr('id'),i=$this.closest('TR').attr('data-i'),currentObj = that._productionList[i];
                        S_dialog.dialog({
                            title: '完成情况',
                            contentEle: 'dialogOBox',
                            ele:id,
                            lock: 2,
                            align:'left',
                            quickClose:true,
                            noTriangle:true,
                            width: '300',
                            minHeight:'110',
                            tPadding: '0px',
                            url: rootPath+'/assets/module/m_common/m_dialog.html'

                        },function(d){//加载html后触发

                            var tipsEle = 'div[id="content:'+d.id+'"] .dialogOBox';
                            var completion = currentObj.completion==null?'':currentObj.completion;
                            $(tipsEle).html('<div class="m-sm" style="word-break: break-all;">'+completion+'</div>');

                        });
                        e.stopPropagation();
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
