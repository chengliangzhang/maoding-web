/**
 * 项目信息－任务签发列表
 * Created by wrb on 2017/2/21.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_taskIssue_list_old",
        defaults = {
            $projectId: null,
            $projectName:null,
            $projectCompanyId: null,
            $taskIssueList: null,
            $publishId:null,
            $projectManagerId:null,//经营负责人ID
            $currentManagerObj:null,//经营人对象
            $pageEntrance:'taskIssue'//页面入口（我的任务=myIssueTask,任务签发=taskIssue）
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyUserId = window.currentCompanyUserId;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this._taskIssueList = [];//当前经营列表
        this._isOrgManager = 0;
        this._isAssistant = 0;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtml();
            that.bindActionClick();
        },
        //初始化数据,生成html
        initHtml: function () {
            var that = this;
            var $data = {};
            $data.taskIssueList = that.settings.$taskIssueList;
            that._taskIssueList = that.settings.$taskIssueList;
            $data.currentCompanyId = that._currentCompanyId;
            $data.projectCompanyId = that.settings.$projectCompanyId;
            $data.isView = that.settings.$isView;
            $data.isOrgManager = 0;//是否经营负责人
            if(window.currentCompanyUserId==that.settings.$projectManagerId){//that.settings.$projectCompanyId == that._currentCompanyId &&
                $data.isOrgManager = 1;
                that._isOrgManager = 1;
            }
            if(that.settings.$currentManagerObj.assistant!=null && window.currentCompanyUserId==that.settings.$currentManagerObj.assistant.companyUserId){//that.settings.$projectCompanyId == that._currentCompanyId &&
                $data.isAssistant = 1;
                that._isAssistant = 1;
            }
            var html = template('m_taskIssue/m_taskIssue_list', $data);
            $(that.element).html(html);
            $(that.element).find('.tree').treegrid({
                expanderExpandedClass: 'ic-open',
                expanderCollapsedClass: 'ic-retract',
                treeColumn: 2
            });
            var tableWidth = $(that.element).find('table.table').width();
            stringCtrl('taskName',tableWidth*0.25);

            //设计名称编辑
            $(that.element).find('a[data-action="taskNameEdit"]').each(function () {
                var $this = $(this);
                var taskName = $this.prev().attr('data-string');
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
                        $this.prev('.show-span').show();
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
                        $this.prev('.show-span').hide();
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

            if(!that.settings.$isView){

                //操作列表没有可操作则不展示该按钮组
                $(that.element).find('ul.dropdown-menu').each(function () {
                    var len = $(this).find('li a[data-action]').length;
                    if(len==0){
                        $(this).parents('tr').find('td:first .list-check-box').remove();
                        $(this).parents('.btn-group').remove();

                    }
                });
                that.editHoverFun();
                that.documentBindFun();
                that.initICheck();
            }
            else{//查看状态下删除相关操作按钮
                $(that.element).find('.list-check-box').remove();
                $(that.element).find('.list-action-box').remove();
                $(that.element).find('button[data-action="addDesignTask"]').remove();
            }
            $(that.element).find('a[data-toggle="tooltip"],i[data-toggle="tooltip"]').tooltip();
            that.editTaskRemark();
        }
        //任务描述编辑
        , editTaskRemark:function () {
            var that = this;
            $(that.element).find('a[data-action="taskRemarkEdit"]').each(function () {
                var $this = $(this);
                var id = $this.attr('id'),i=$this.closest('TR').attr('data-i'),currentObj = that._taskIssueList[i];
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
        }
        //hover事件
        , editHoverFun:function () {

            var that = this;
            //文本移上去出现编辑图标hover事件
            $(that.element).find('a[data-action][data-deal-type="edit"]').each(function () {

                var $this = $(this);
                
                $this.closest('TD').hover(function () {
                    if($this.next('.edit-box').length<=0 && $this.next('.editable-container').length<=0){
                        $this.show();
                    }
                },function () {
                    if($this.next('.edit-box').length<=0 && $this.next('.editable-container').length<=0) {
                        $this.hide();
                    }
                })
            });
            //TR hover效果
            $(that.element).find('TR').each(function () {

                var $this = $(this);
                var singleOperation = $this.find('.singleOperation');
                var batchOperation = $this.find('.batchOperation');

                $this.hover(function () {
                    if(batchOperation.length==0 || batchOperation.css('display')=='none'){
                        singleOperation.show();
                    }
                    if(!$this.hasClass('chose-operable')){
                        $this.addClass('tr-hover');
                    }

                },function () {
                    singleOperation.hide();
                    if(!$this.hasClass('chose-operable')){
                        $this.removeClass('tr-hover');
                    }
                });
            });
        }
        //保存签发
        ,saveTaskIssue:function (taskId) {
            var options={},that=this;
            options.classId = 'body';
            options.url=restApi.url_saveTaskIssuing;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            options.postData.taskType =2;
            options.postData.companyId = $(that.element).find('.org-select-box .company-name').attr('data-company-id');

            var departId = $(that.element).find('.org-select-box .company-name').attr('data-depart-id');
            if( departId!=undefined && departId!=''){
                options.postData.orgId = departId;
            }
            if(taskId!=null){
                options.postData.taskPid = taskId;
            }
            options.postData.taskName = $.trim($(that.element).find('.row-edit input[name="taskName"]').val());
            options.postData.startTime = $(that.element).find('.row-edit input[name="startTime"]').val();
            options.postData.endTime = $(that.element).find('.row-edit input[name="endTime"]').val();
            options.postData.taskRemark = $(that.element).find('.row-edit input[name="taskRemark"]').val();

            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    that.refreshPage();
                }else {
                    if(response.info.indexOf('已存在')>-1){
                        S_toastr.error(response.info);
                    }else{
                        S_dialog.error(response.info);
                    }
                }
            });
        }
        //编辑签发保存
        ,saveTaskIssueByEdit:function ($this,param) {
            var options={},that=this;
            options.url=restApi.url_saveTaskIssuing;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            options.postData.taskType =2;
            options.postData.companyId = $this.closest('tr').attr('data-company-id');
            options.postData.id = $this.closest('tr').attr('data-id');
            $.extend(options.postData, param);
            m_ajax.postJson(options,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    that.refreshPage();
                }else {
                    if(response.info.indexOf('已存在')>-1){
                        S_toastr.error(response.info);
                        $this.editable('show');
                    }else{
                        S_dialog.error(response.info);
                    }
                }
            });
        }
        //编辑签发保存
        ,saveTaskIssueBySyncEdit:function ($this,param) {
            var options={},that=this;
            options.url=restApi.url_saveTaskIssuing;
            options.async = false;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            options.postData.taskType =2;
            options.postData.companyId = $this.closest('tr').attr('data-company-id');
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
            options.postData.targetId = $this.closest('tr').attr('data-publish-id');
            options.postData.companyId = $this.closest('tr').attr('data-company-id');
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
        //查询组织部门
        ,getDepartByCompanyId:function (callBack) {
            var that = this;
            var option  = {};
            option.url = restApi.url_getDepartByCompanyId+'/'+that._currentCompanyId;
            m_ajax.get(option,function (response) {
                if(response.code=='0'){
                    return callBack(response.data);
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //查询组织
        ,getCompanyListByTaskId:function (taskId,callBack) {
            var that = this,option={};
            option.url=restApi.url_getIssueTaskCompany;
            option.postData = {};
            option.postData.projectId = that.settings.$projectId;
            if(taskId!=null){
                option.postData.taskId = taskId;
            }
            m_ajax.postJson(option,function (response) {

                if(response.code=='0'){

                    if(callBack!=null){
                        callBack(response.data);
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //添加设计任务
        ,addTaskRow:function (taskId) {
            var that = this;
            that.getCompanyListByTaskId(taskId,function (resData) {

                that.getDepartByCompanyId(function (data) {//查询部门

                    var html =  template('m_taskIssue/m_taskIssue_list_add', {
                        currentCompanyId:that._currentCompanyId,
                        allCompanyList:resData.allCompanyList,
                        departList:data
                    });
                    if(taskId!=null){//添加子任务
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

                    }else{//添加根任务
                        $(that.element).find('tr:last').after(html);
                    }
                    that.timeIconClick1($('.row-edit'));
                    $(that.element).find('button[data-action="cancel"]').on('click',function () {//取消事件
                        $(this).closest('tr').next('tr.row-edit').remove();
                        $(this).closest('tr').remove();
                        $(that.element).find('button[data-action="addDesignTask"]').attr('disabled',false);
                        $(that.element).find('tr[data-id="'+taskId+'"]').removeAttr('style');
                        //判断当前是否没数据
                        if($(that.element).find('tr.no-data-tr').length>0){
                            $(that.element).find('tr.no-data-tr').show();
                        }
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
                        /*if(that._taskIssueList!=null && that._taskIssueList.length>0){
                            $.each(that._taskIssueList,function (index,item) {
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
                        that.saveTaskIssue(taskId);
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
                    that.dealSelectOrgEvent(0);
                });
            })
        }
        //选中组织事件{0：添加任务；1：编辑组织}
        ,dealSelectOrgEvent:function (type) {
            var that = this;
            $(that.element).find('a.open-depart-btn').on('click',function (e) {//打开部门选择

                if($(this).find('i').hasClass('fa-angle-double-down')){
                    $(this).find('i').removeClass('fa-angle-double-down').addClass('fa-angle-double-up');
                    $(this).next('.dropdown-menu').show();
                }else{
                    $(this).find('i').addClass('fa-angle-double-down').removeClass('fa-angle-double-up');
                    $(this).next('.dropdown-menu').hide();
                }
                e.stopPropagation();
            });
            $(that.element).find('a[data-action="choseOrg"]').on('click',function (e) {//选择组织处理事件

                var dataChoseType = $(this).attr('data-chose-type');
                var companyName = $(this).text();
                var companyId = $(this).attr('data-company-id');
                var departId = null;
                var isDisabled = $(this).attr('disabled');
                $(that.element).find('.edit-box .company-name').html(companyName);
                if(dataChoseType=='company'){
                    $(that.element).find('.edit-box .company-name').attr('data-company-id',companyId);
                    $(that.element).find('.edit-box .company-name').attr('data-depart-id','');
                }else{
                    companyId = $(this).parents('ul').parent().find('a:first').attr('data-company-id');
                    departId = $(this).attr('data-company-id');
                    $(that.element).find('.edit-box .company-name').attr('data-company-id',companyId);
                    $(that.element).find('.edit-box .company-name').attr('data-depart-id',departId);
                }
                $(that.element).find('.edit-box .dropdown-menu').hide();
                $(that.element).find('a.open-depart-btn i').addClass('fa-angle-double-down').removeClass('fa-angle-double-up');

                if(type==1 && (isDisabled==undefined || isDisabled!='disabled')){//编辑状态下选中即保存,并且选的不是当前组织
                    var param = {};
                    param.companyId = companyId;
                    param.orgId = departId;
                    param.isChangeOrg = 1;
                    that.saveTaskIssueByEdit($(this),param);
                }
                e.stopPropagation();
            });
            $(that.element).find('.edit-box button:first').on('click',function () {//点击选择btn事件
                if($(that.element).find('.edit-box .dropdown-menu').eq(0).css('display')=='none'){
                    $(that.element).find('.edit-box .dropdown-menu').eq(0).show();
                }else{
                    $(that.element).find('.edit-box .dropdown-menu').hide();
                }
                //处理选中效果状态(加底色不可触发)
                var selectedCompanyId = $(that.element).find('.edit-box button .company-name').attr('data-company-id');
                var selectedDepartId = $(that.element).find('.edit-box button .company-name').attr('data-depart-id');
                if(selectedDepartId!=undefined && selectedDepartId!=''){//选择的是部门
                    selectedCompanyId = selectedDepartId;
                }
                $(that.element).find('.edit-box a[data-action="choseOrg"]').css('background-color','');
                $(that.element).find('.edit-box a[data-action="choseOrg"]').removeAttr('disabled');
                var $ele = $(that.element).find('.edit-box a[data-action="choseOrg"][data-company-id="'+selectedCompanyId+'"]');
                $ele.css('background-color','#f5f5f5');
                $ele.attr('disabled','disabled');
            });
        }
        //window document事件绑定
        ,documentBindFun:function () {
            var that = this;
            $(document).bind('click',function(e){

                console.log('document event');
                if(!($(e.target).closest('.reign-edit-box').length>0 || $(e.target).hasClass('reign-edit-box'))){
                    $(that.element).find('.reign-edit-box').parents('td').find('.show-span').show();
                    $(that.element).find('.reign-edit-box').remove();
                }

                if(!($(e.target).hasClass('org-select-box') || $(e.target).closest('.org-select-box').length>0)) {
                    $(that.element).find('.org-select-box .dropdown-menu').hide();
                    $(that.element).find('a.open-depart-btn i').addClass('fa-angle-double-down').removeClass('fa-angle-double-up');
                }
            });
        }
        //编辑前清空所有编辑情况
        ,removeAllEditBox:function () {
            var that = this;
            $(that.element).find('.reign-edit-box').parents('td').find('.show-span').show();
            $(that.element).find('.reign-edit-box').remove();
            $(that.element).find('.editable').prev().show();
            $(that.element).find('.editable').editable('hide');
        }
        ,initICheck:function () {
            var that = this;
            var ifChecked = function (e) {

                $(this).closest('tr').find('.singleOperation').hide();

                var $i = $(this).closest('tr').attr('data-i')-0;//当前任务list下标
                var $ele = $(that.element).find('tr[data-i="'+$i+'"]');
                if(that._taskIssueList.length>0){

                    if((that._taskIssueList[$i].taskStatus==2 || that._taskIssueList[$i].canBeDelete) && that._isOrgManager==1){
                        $ele.addClass('chose-operable');
                    }


                    //父级出现批量操作按钮
                    if(that._taskIssueList[$i].isHasChild==0){
                        var dataPid = $ele.attr('data-pid');
                        var $pEle = $(that.element).find('tr[data-id="'+dataPid+'"]');
                        var checkedLen = $(that.element).find('tr[data-pid="'+dataPid+'"] input[name="taskCk"]:checked').length;
                        var checkLen = $(that.element).find('tr[data-pid="'+dataPid+'"] input[name="taskCk"]').length;
                        $pEle.find('.batchOperation').show();
                        if(checkLen==checkedLen){
                            //父checkbox
                            $pEle.find('input[name="taskCk"]').prop('checked',true);
                            $pEle.find('input[name="taskCk"]').iCheck('update');
                        }

                    }else{
                        $ele.find('.batchOperation').show();
                    }

                    for(var i=($i+1);i<that._taskIssueList.length;i++){
                        var $currTrEle = $(that.element).find('tr[data-i="'+i+'"]');
                        if(that._taskIssueList[i]==null){
                            break;
                        }
                        if(that._taskIssueList[i].taskLevel>that._taskIssueList[$i].taskLevel){//taskLevel大于当前taskLevel，即是子集
                            $currTrEle.find('.singleOperation').hide();
                            $currTrEle.find('input[name="taskCk"]').prop('checked',true);
                            $currTrEle.find('input[name="taskCk"]').iCheck('update');

                            if((that._taskIssueList[i].taskStatus==2 || that._taskIssueList[i].canBeDelete) && that._isOrgManager==1){
                                $currTrEle.addClass('chose-operable');
                            }
                        }else{//遇到同级即退出循环
                            break;
                        }
                    }

                }
                that.isTaskAllCheck();
            };
            var ifUnchecked = function (e) {

                $(this).closest('tr').find('.singleOperation').show();
                var $i = $(this).closest('tr').attr('data-i')-0;//当前任务list下标
                var $ele = $(that.element).find('tr[data-i="'+$i+'"]');
                var currentDataId = $ele.attr('data-id');
                $ele.removeClass('chose-operable');
                if(that._taskIssueList.length>0){

                    //父级批量操作按钮展示与否
                    if(that._taskIssueList[$i].isHasChild==0){
                        var dataPid = $ele.attr('data-pid');
                        var $pEle = $(that.element).find('tr[data-id="'+dataPid+'"]');
                        var checkedLen = $(that.element).find('tr[data-pid="'+dataPid+'"] input[name="taskCk"]:checked').length;
                        var checkLen = $(that.element).find('tr[data-pid="'+dataPid+'"] input[name="taskCk"]').length;

                        if(checkedLen>0){//子集存在选中
                            $pEle.find('.batchOperation').show();
                            if(checkLen>checkedLen){
                                //父checkbox去勾
                                $pEle.find('input[name="taskCk"]').prop('checked',false);
                                $pEle.find('input[name="taskCk"]').iCheck('update');
                            }
                        }else{
                            $pEle.find('.batchOperation').hide();
                            //父checkbox去勾
                            $pEle.find('input[name="taskCk"]').prop('checked',false);
                            $pEle.find('input[name="taskCk"]').iCheck('update');
                        }

                    }else{
                        $ele.find('.batchOperation').hide();
                        $(that.element).find('tr[data-pid="'+currentDataId+'"]').find('.batchOperation').hide();
                    }

                    for(var i=($i+1);$i<i<that._taskIssueList.length;i++){
                        var currTrEle = $(that.element).find('tr[data-i="'+i+'"]');
                        if(that._taskIssueList[i]==null){
                            break;
                        }
                        if(that._taskIssueList[i].taskLevel>that._taskIssueList[$i].taskLevel){
                            currTrEle.find('input[name="taskCk"]').prop('checked',false);
                            currTrEle.find('input[name="taskCk"]').iCheck('update');
                            currTrEle.find('input[name="taskCk"]').iCheck('enable');

                            $(that.element).find('tr[data-i="'+i+'"]').removeClass('chose-operable');

                        }else{//遇到同级即退出循环
                            break;
                        }
                    }
                }
                that.isTaskAllCheck();
            };
            $(that.element).find('input[name="taskCk"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);
            var ifAllChecked = function (e) {
                $(that.element).find('input[name="taskCk"]').iCheck('check');
            };
            var ifAllUnchecked = function (e) {
                //$(that.element).find('input[name="taskCk"]').iCheck('uncheck');
                $(that.element).find('input[name="taskCk"]').prop('checked',false);
                $(that.element).find('input[name="taskCk"]').iCheck('update');
                $(that.element).find('input[name="taskCk"]').iCheck('enable');
                $(that.element).find('tbody .singleOperation').hide();
                $(that.element).find('tbody .batchOperation ').hide();
                $(that.element).find('tr[data-i]').removeClass('chose-operable');
            };
            $(that.element).find('input[name="taskAllCk"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifAllUnchecked).on('ifChecked.s', ifAllChecked);
        }
        //判断全选是否该选中并给相关处理
        ,isTaskAllCheck:function () {
            var that =this;
            var len = $(that.element).find('input[name="taskCk"]:checked').length;
            var taskLen = that._taskIssueList.length;
            if(len==taskLen){
                $(that.element).find('input[name="taskAllCk"]').prop('checked',true);
                $(that.element).find('input[name="taskAllCk"]').iCheck('update');
            }else{
                $(that.element).find('input[name="taskAllCk"]').prop('checked',false);
                $(that.element).find('input[name="taskAllCk"]').iCheck('update');
            }
        }
        //批量删除任务
        ,batchDelTask:function (idList) {
            var that = this;

            S_dialog.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                var option = {};
                option.url = restApi.url_deleteProjectTask;
                option.postData = {};
                option.postData.idList = idList;
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
        }
        //批量发布
        ,batchPublishTask:function (idList) {

            var that = this;
            S_dialog.confirm('确定发布任务？',function () {

                var option = {};
                option.classId = '.ibox-content';
                option.url = restApi.url_publishIssueTask;
                option.postData = {};
                option.postData.projectId = that.settings.$projectId;
                option.postData.idList = idList;
                m_ajax.postJson(option, function (response) {
                    if (response.code == '0') {
                        S_toastr.success('发布成功');
                        that.refreshPage();
                    } else {
                        S_dialog.error(response.info);
                    }
                });
            },function () {
            });
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
        //设置进度变更时间{type==1==合同时间,type==2==计划进度}
        , setTheScheduleTime: function ($this,type) {

            var that = this,taskId = $this.closest('tr').attr('data-id');
            var dataStartTime = $this.attr('data-start-time');
            var dataEndTime = $this.attr('data-end-time');
            var companyId = $this.closest('tr').attr('data-company-id');
            var taskStatus = $this.attr('data-status');
            var options = {};
            options.$projectId = that.settings.$projectId;


            if (taskStatus==2 || (dataStartTime=='' && dataEndTime=='')) {//设置计划进度

                var $data = {};
                options.$title = '设置计划进度';
                options.$isHaveMemo = false;
                options.$isOkSave = true;
                options.$saveDataUrl = restApi.url_saveProjectProcessTime;
                options.$timeInfo = {
                    startTime:dataStartTime,
                    endTime:dataEndTime
                };
                $data.type = 2;
                $data.companyId = companyId;
                $data.targetId = $this.closest('tr').attr('data-publish-id');
                options.$saveData = $data;
                options.$saveTimeKeyVal = ['startTime', 'endTime'];
                options.$okCallBack = function (data) {
                    that.refreshPage();
                };
                $('body').m_inputProcessTime(options);

            } else {//计划进度变更
                options.$taskId = taskId;
                options.$type = type;
                options.$labelText = $this.closest('TR').find('td span.taskName').attr('data-string');
                options.$taskCompanyId = companyId;
                options.$okCallBack = function (data) {
                    that.refreshPage();
                };
                $('body').m_scheduleChanges_new(options);
            }
        }
        //事件绑定
        , bindActionClick: function () {
            var that = this;
            $(that.element).find('a[data-action],button[data-action]').on('click', function (e) {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                var taskId = $this.closest('tr').attr('data-id'),i=$this.closest('tr').attr('data-i');
                var dataI = $this.closest('tr').attr('data-i')-0;
                var publishId = $this.closest('tr').attr('data-publish-id');
                switch (dataAction) {
                    case 'choseDesignOrg'://更换设计组织

                        if(!($(that.element).find('.row-edit').length>0)){

                            that.getCompanyListByTaskId(taskId,function (resData) {

                                that.getDepartByCompanyId(function (data) {//查询部门

                                    that.removeAllEditBox();
                                    var selectedCompanyId = $this.prev('.show-span').attr('data-company-id');
                                    var selectedDepartId = $this.prev('.show-span').attr('data-depart-id');
                                    var selectedCompanyText = $this.prev('.show-span').text();
                                    if(selectedCompanyId==undefined || selectedCompanyId==''){
                                        selectedCompanyId = (resData.allCompanyList!=null && resData.allCompanyList.length>0)?resData.allCompanyList[0].id:'';
                                        selectedCompanyText = (resData.allCompanyList!=null && resData.allCompanyList.length>0)?resData.allCompanyList[0].companyName:'';
                                    }
                                    var html =  template('m_taskIssue/m_taskIssue_list_editOrg', {
                                        currentCompanyId:that._currentCompanyId,
                                        allCompanyList:resData.allCompanyList,
                                        departList:data,
                                        selectedCompanyId:selectedCompanyId,
                                        selectedCompanyText:selectedCompanyText,
                                        selectedDepartId:selectedDepartId

                                    });
                                    $this.prev().hide();
                                    $this.hide();
                                    $this.parent().append(html);
                                    that.dealSelectOrgEvent(1);
                                    e.stopPropagation();
                                });
                            });
                            e.stopPropagation();
                        }else{
                            S_toastr.warning('当前正在创建设计任务中...');
                        }
                        break;
                    case 'delTask'://删除任务

                        var canBeDelete = $this.attr('data-canbedelete');
                        var idList = [];
                        idList.push(taskId);
                        that.batchDelTask(idList);
                        break;
                    case 'batchDelTask'://批量删除
                        var idList = [];
                        $(that.element).find('input[name="taskCk"]:checked').each(function () {
                            var i = $(this).closest('tr').attr('data-i');
                            var currentTaskIssue = that._taskIssueList[i];
                            if(currentTaskIssue.canBeDelete){//可删除的任务
                                idList.push(currentTaskIssue.id);
                            }
                        });
                        if(idList.length>0){
                            that.batchDelTask(idList);
                        }else{
                            S_toastr.warning('当前不存在可删除的任务，请重新选择！')
                        }
                        break;
                    case 'addDesignTask'://添加设计任务
                        that.removeAllEditBox();
                        if(!($(that.element).find('.row-edit').length>0)){
                            //判断当前是否没数据
                            if($(that.element).find('tr.no-data-tr').length>0){
                                $(that.element).find('tr.no-data-tr').hide();
                            }
                            that.addTaskRow(null);
                            $this.attr('disabled',true);
                        }else{
                            S_toastr.warning('当前正在创建设计任务中...');
                        }
                        e.stopPropagation();
                        return false;
                        break;
                    case 'addSubTask'://添加子任务

                        if(!($(that.element).find('.row-edit').length>0)){
                            that.addTaskRow(taskId);
                            $this.attr('disabled',true);
                        }else{
                            S_toastr.warning('当前正在创建设计任务中...');
                        }
                        //e.stopPropagation();
                        break;
                    case 'publishTask'://发布

                        var idList = [];
                        idList.push(publishId);
                        that.batchPublishTask(idList);
                        break;
                    case 'batchPublishTask'://批量发布

                        var idList = [];
                        $(that.element).find('input[name="taskCk"]:checked').each(function () {
                            var i = $(this).closest('tr').attr('data-i');
                            var currentTaskIssue = that._taskIssueList[i];
                            if(currentTaskIssue.taskStatus==2){//可发布的任务
                                idList.push(currentTaskIssue.publishId);
                            }
                        });
                        if(idList.length>0){
                            that.batchPublishTask(idList);
                        }else{
                            S_toastr.warning('当前不存在可发布的任务，请重新选择！');
                        }
                        break;
                    case 'startTimeEdit'://开始时间编辑

                        if(!($(that.element).find('.row-edit').length>0)){
                            that.removeAllEditBox();
                            that.setTheScheduleTime($this,1);
                            e.stopPropagation();
                        }else{
                            S_toastr.warning('当前正在创建设计任务中...');
                        }
                        break;
                    case 'endTimeEdit'://开始时间编辑

                        if(!($(that.element).find('.row-edit').length>0)){
                            that.removeAllEditBox();
                            that.setTheScheduleTime($this,1);
                            e.stopPropagation();
                        }else{
                            S_toastr.warning('当前正在创建设计任务中...');
                        }
                        break;
                    case 'viewProgressChange'://获取时间变更列表

                        var option = {};
                        option.$taskId = taskId;
                        option.$publishId = $this.closest('tr').attr('data-publish-id');
                        option.$type = 2;
                        option.$eleId = $this.closest('a').attr('id');
                        option.$isView = that.settings.$isView;
                        option.$renderCallBack = function (dialogEle) {
                            if(that.settings.$isView){
                                $(dialogEle).parents('.ui-popup').css('z-index','29891016');//layer弹窗下，需要改下z-index
                                $(dialogEle).parents('.ui-popup').prev('.ui-popup-backdrop').css('z-index','29891016');//layer弹窗下，需要改下z-index
                            }
                        };
                        $('body').m_progressChange_list(option);
                        e.stopPropagation();
                        break;
                    case 'moveUp'://向上移动

                        var taskArr = [];
                        taskArr.push({
                            id:that._taskIssueList[dataI].id,
                            seq:that._taskIssueList[dataI].seq-0
                        });
                        for(var i=dataI-1;i<dataI;i--){
                            if(that._taskIssueList[i].taskLevel==that._taskIssueList[dataI].taskLevel){
                                taskArr.push({
                                    id:that._taskIssueList[i].id,
                                    seq:that._taskIssueList[i].seq-0
                                });
                                break;
                            }
                        }
                        that.saveTaskMoveInSeq(taskArr);

                        break;
                    case 'moveDown'://向下移动

                        var taskArr = [];
                        taskArr.push({
                            id:that._taskIssueList[dataI].id,
                            seq:that._taskIssueList[dataI].seq
                        });
                        for(var i=dataI+1;i<that._taskIssueList.length;i++){
                            if(that._taskIssueList[i].taskLevel==that._taskIssueList[dataI].taskLevel){
                                taskArr.push({
                                    id:that._taskIssueList[i].id,
                                    seq:that._taskIssueList[i].seq
                                });
                                break;
                            }
                        }
                        that.saveTaskMoveInSeq(taskArr);
                        break;
                    case 'viewPlanTime'://与自己有关的约定时间
                        S_dialog.dialog({
                            title: '约定时间',
                            contentEle: 'dialogOBox',
                            ele:'viewPlanTime'+taskId,//弹框位置定位
                            lock: 2,
                            quickClose:true,
                            noTriangle:true,
                            width: '420',
                            minHeight:'110',
                            tPadding: '0px',
                            url: rootPath+'/assets/module/m_common/m_dialog.html'

                        },function(d){//加载html后触发

                            var tipsEle = 'div[id="content:'+d.id+'"] .dialogOBox';
                            var currentTaskObj = that._taskIssueList[i];
                            $(tipsEle).html(template('m_taskIssue/m_taskIssue_list_changeTime',{
                                issuePlanList:currentTaskObj.issuePlanList
                            }));
                            if(that.settings.$isView) {
                                $(tipsEle).parents('.ui-popup').css('z-index', '29891016');//layer弹窗下，需要改下z-index
                                $(tipsEle).parents('.ui-popup').prev('.ui-popup-backdrop').css('z-index', '29891016');//layer弹窗下，需要改下z-index
                            }

                            //点击获取变更列表
                            $(tipsEle).find('a[data-action]').click(function(e){
                                var $this = $(this);
                                var id = $this.closest('a').attr('id');
                                var taskId = $this.closest('a').attr('data-id'),i=$this.closest('a').attr('data-i');
                                var option = {};
                                option.$taskId = taskId;
                                option.$publishId = $this.closest('a').attr('data-publish-id');
                                option.$type = 2;
                                option.$eleId = id;
                                option.$isView = that.settings.$isView;
                                option.$renderCallBack = function (dialogEle) {
                                    if(that.settings.$isView){
                                        $(dialogEle).parents('.ui-popup').css('z-index','29891016');//layer弹窗下，需要改下z-index
                                        $(dialogEle).parents('.ui-popup').prev('.ui-popup-backdrop').css('z-index','29891016');//layer弹窗下，需要改下z-index
                                    }
                                };
                                $('body').m_progressChange_list(option);
                                e.stopPropagation();
                            })
                        });
                        return false;
                        break;

                    case 'viewTaskRemarkEdit'://查看任务描述

                        var id = $this.attr('id'),currentObj = that._taskIssueList[i];
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


                }

            });

        }
        //刷新当前界面
        , refreshPage: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$projectName = that.settings.$projectName;
            if(that.settings.$pageEntrance == 'myIssueTask'){
                $('#content-right').m_myTask_taskIssue(option,true);
            }else{
                $('#content-right').m_taskIssue(option,true);
            }

            return false;
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
