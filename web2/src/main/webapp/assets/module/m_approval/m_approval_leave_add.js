/**
 * 添加请假申请
 * Created by wrb on 2018/9/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_leave_add",
        defaults = {
            isDialog:true,
            dataInfo:null,//dataInfo不为null,即编辑
            saveCallBack:null,
            doType: 3// 报销=1=expense,费用=2=costApply,请假=3=leave,出差=4=onBusiness,付款申请=5=projectPayApply
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._currentCompanyUserId = window.currentCompanyUserId;
        this._currentCompanyId = window.currentCompanyId;
        this._currentUserId = window.currentUserId;
        this._fastdfsUrl = window.fastdfsUrl;

        this._uploadmgrContainer = null;
        this._maxExpNo = null;//报销编号
        this._uuid = UUID.genV4().hexNoDelim;//targetId
        this._baseData = {};
        this._auditList = [];//审批人
        this._ccCompanyUserList = [];//抄送人

        this._deleteAttachList = [];//编辑才用，已有删除的集合

        this._title = this.settings.doType==3?'请假':'出差';
        this._auditType = this.settings.doType==3?'leave':'onBusiness';//auditType 的取值： 请假leave 出差onBusiness 报销expense 费用costApply 付款申请projectPayApply

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderDialog(function () {

                var option = {};
                option.url = restApi.url_getProcessType;
                option.postData = {
                    auditType:that._auditType
                };
                m_ajax.postJson(option, function (response) {
                    if (response.code == '0') {

                        that._baseData = response.data;
                        that._baseData.doType = that.settings.doType;
                        that._baseData.title = that._title;
                        var html = template('m_approval/m_approval_leave_add', {
                            data: that._baseData,
                            dataInfo:that.settings.dataInfo
                        });
                        $(that.element).html(html);
                        if(that.settings.doType==3){
                            that.renderLeaveType();
                        }
                        if(that.settings.doType==4){
                            that.renderProjectList();
                        }

                        that.fileUpload();
                        that.bindActionClick();
                        that.save_validate();
                        $(that.element).find('input[name="leaveTime"]').on('keyup',function () {
                            if(that._baseData.processType=='3'){
                                that.renderApprover();
                            }
                        });

                        if(that.settings.dataInfo!=null){

                            if(that.settings.dataInfo.attachList){

                                $.each(that.settings.dataInfo.attachList,function (i,item) {
                                    var fileData = {};
                                    fileData.fullPath = item.fileFullPath;
                                    fileData.netFileId = item.id;
                                    fileData.fileName = item.fileName;
                                    fileData.type = -1;//代表已存在
                                    var html = template('m_common/m_attach', fileData);
                                    $('#showFileLoading').append(html);
                                    that.bindAttachDelele();
                                })
                            }
                            if(that.settings.dataInfo.ccCompanyUserList){

                                that._ccCompanyUserList = [];

                                $.each(that.settings.dataInfo.ccCompanyUserList,function (i,item) {
                                    that._ccCompanyUserList.push({
                                        id:item.companyUserId,
                                        userName:item.userName,
                                        userId:item.userId
                                    })
                                });
                                var html = template('m_approval/m_approval_cost_add_ccUser', {userList: that._ccCompanyUserList});
                                $(that.element).find('#ccUserListBox').html(html);
                                that.ccBoxDeal();
                            }
                            if(that.settings.dataInfo.auditList && that.settings.dataInfo.processFlag=='1'){

                                that._auditList = [];
                                $.each(that.settings.dataInfo.auditList,function (i,item) {
                                    if(i==that.settings.dataInfo.auditList.length-1){//取最后一条
                                        that._auditList.push({
                                            id:item.companyUserId,
                                            userName:item.userName,
                                            userId:item.userId,
                                            fileFullPath:item.fileFullPath
                                        })
                                    }
                                });
                                var html = template('m_approval/m_approval_cost_add_approver', {userList: that._auditList});
                                $(that.element).find('#approverBox').html(html);
                            }else{
                                that.renderApprover();
                            }

                        }else{
                            that.renderApprover();
                        }


                    } else {
                        S_dialog.error(response.info);
                    }
                });

            });

        }
        //初始化数据,生成html
        ,renderDialog:function (callBack) {

            var that = this;
            if(that.settings.isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title||'请假申请',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '705',
                    tPadding: '0',
                    height: '630',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {

                    },
                    ok:function () {
                        var flag = $(that.element).find('form').valid();
                        if (!flag || that.save()) {
                            return false;
                        }
                    }
                },function(d){//加载html后触发
                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    if(callBack!=null)
                        callBack();

                });
            }else{//不以弹窗编辑
                if(callBack!=null)
                    callBack();
            }
        }
        //渲染请假类型
        ,renderLeaveType:function () {
            var that = this;
            var option = {};
            option.url = restApi.url_getLeaveType;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {

                    var data = [];
                    $.each(response.data, function (i, o) {
                        data.push({id: o.vl, text: o.name});
                    });
                    $(that.element).find('select[name="leaveType"]').select2({
                        width: '100%',
                        allowClear: false,
                        language: 'zh-CN',
                        minimumResultsForSearch: Infinity,
                        placeholder: '请选择关联项目!',
                        data: data
                    });

                    if(that.settings.dataInfo && that.settings.dataInfo.leaveType)
                        $(that.element).find('select[name="leaveType"]').val(that.settings.dataInfo.leaveType).trigger('change');

                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //渲染项目列表
        ,renderProjectList:function () {
            var that = this;
            var option = {};
            option.url = restApi.url_getProjectList;
            option.postData = {
                currentCompanyId:that._currentCompanyId,
                currentCompanyUserId:that._currentCompanyUserId
            };
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    var data = [];
                    $.each(response.data, function (i, o) {
                        data.push({id: o.id, text: o.projectName});
                    });
                    $(that.element).find('select[name="projectId"]').select2({
                        width: '100%',
                        allowClear: true,
                        language: "zh-CN",
                        minimumResultsForSearch: Infinity,
                        data: data
                    });

                    if(that.settings.dataInfo && that.settings.dataInfo.projectId)
                        $(that.element).find('select[name="projectId"]').val(that.settings.dataInfo.projectId).trigger('change');

                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        ,renderApprover:function () {
            var that = this;
            var expAmout = 0,userList = [];

            $(that.element).find('input[name="leaveTime"]').each(function () {

                expAmout = expAmout + ($(this).val()-0);
            });

            if(that._baseData.processType=='2'  && that._baseData.conditionList!=null && that._baseData.conditionList.length>0){//固定流程

                userList = that._baseData.conditionList[0].userList;

            }else if(that._baseData.processType=='3'  && that._baseData.conditionList!=null && that._baseData.conditionList.length>0){//条件流程

                $.each(that._baseData.conditionList,function (i,item) {
                    if((item.min=='null' || isNullOrBlank(item.min)) && expAmout>=0 && (item.max!='null' && !isNullOrBlank(item.max) && expAmout<item.max-0)){//min为空，max不为空
                        userList = item.userList;
                        return false;
                    }else if(item.min!='null' && !isNullOrBlank(item.min) && expAmout>=item.min-0 && item.max!='null' && !isNullOrBlank(item.max) && expAmout<item.max-0){//min不为空，max不为空
                        userList = item.userList;
                        return false;
                    }else if(item.min!='null' && !isNullOrBlank(item.min) && expAmout>=item.min-0 && (item.max=='null' || isNullOrBlank(item.max))){//min不为空，max为空
                        userList = item.userList;
                        return false;
                    }
                });
            }
            var html = template('m_approval/m_approval_cost_add_approver', {userList: userList});
            $(that.element).find('#approverBox').html(html);
        }
        //保存
        ,save:function () {
            var that = this;

            var $data = $(that.element).find("form.form-horizontal").serializeObject();
            $data.type = that.settings.doType;
            $data.userId = that._currentCompanyUserId;
            $data.targetId = that._uuid;

            if(that._auditList!=null && that._auditList.length>0){
                $data.auditPerson = that._auditList[0].id;
            }

            var ccUser = [] ;
            if(that._ccCompanyUserList!=null && that._ccCompanyUserList.length>0){
                $.each(that._ccCompanyUserList,function (i,item) {
                    ccUser.push(item.id);
                });
            }
            $data.ccCompanyUserList = ccUser;

            if(that.settings.dataInfo){
                $data.id = that.settings.dataInfo.id;
                $data.deleteAttachList = that._deleteAttachList;
            }

            var option = {};
            option.classId = '#content-right';
            option.url = restApi.url_saveLeave;
            option.postData = $data;

            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('操作成功');
                    if(that.settings.saveCallBack)
                        that.settings.saveCallBack();
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //上传附件
        ,fileUpload:function () {
            var that =this;
            var option = {};
            that._uploadmgrContainer = $(that.element).find('.uploadmgrContainer:eq(0)');
            option.server = restApi.url_attachment_uploadExpenseAttach;
            option.accept={
                title: '上传附件',
                extensions: 'jpg,jpeg,png,bmp',
                mimeTypes: 'image/jpg,image/jpeg,image/png,image/bmp'
            };
            option.btnPickText = '<i class="fa fa-upload"></i>&nbsp;上传附件';
            option.ifCloseItemFinished = true;
            option.boxClass = 'no-borders';
            option.isShowBtnClose = false;
            option.uploadBeforeSend = function (object, data, headers) {
                data.companyId = that._currentCompanyId;
                data.accountId = that._currentUserId;
                data.targetId = that._uuid;
            };
            option.uploadSuccessCallback = function (file, response) {
                console.log(response);
                var fileData = {
                    netFileId: response.data.netFileId,
                    fileName: response.data.fileName,
                    fullPath: that._fastdfsUrl + response.data.fastdfsGroup + '/' + response.data.fastdfsPath
                };
                var $uploadItem = that._uploadmgrContainer.find('.uploadItem_' + file.id + ':eq(0)');
                if (!isNullOrBlank(fileData.netFileId)) {
                    $uploadItem.find('.span_status:eq(0)').html('上传成功');
                    var html = template('m_common/m_attach', fileData);
                    $('#showFileLoading').append(html);
                    var obj = 'a[data-net-file-id="' + fileData.netFileId + '"]';
                    that.bindAttachDelele();
                } else {
                    $uploadItem.find('.span_status:eq(0)').html('上传失败');
                }

            };
            that._uploadmgrContainer.m_uploadmgr(option, true);
        }
        ,bindAttachDelele: function () {
            var that = this;
            $.each($('#showFileLoading').find('a[data-action="deleteAttach"]'), function (i, o) {
                $(o).off('click.deleteAttach').on('click.deleteAttach', function () {
                    var netFileId = $(this).attr('data-net-file-id');
                    var type = $(this).attr('data-type');

                    var ajaxDelete = function () {
                        var ajaxOption = {};
                        ajaxOption.classId = '.file-list:eq(0)';
                        ajaxOption.url = restApi.url_attachment_delete;
                        ajaxOption.postData = {
                            id: netFileId,
                            accountId: that._currentUserId
                        };
                        m_ajax.postJson(ajaxOption, function (res) {
                            if (res.code === '0') {
                                S_toastr.success("删除成功");
                            } else if (res.code === '1') {
                                S_dialog.error(res.msg);
                            }
                        });
                    };

                    if(type==-1){
                        that._deleteAttachList.push(netFileId);
                    }else{
                        ajaxDelete();
                    }

                    $(this).closest('span').remove();
                })
            });
            $.each($('#showFileLoading').find('a[data-action="preview"]'), function (i, o) {
                $(o).off('click.preview').on('click.preview', function () {
                    window.open($(this).attr('data-src'));
                })
            });
        }
        //抄送人事件处理
        ,ccBoxDeal:function () {
            var that = this;
            $(that.element).find('#ccUserListBox .approver-box').hover(function () {
                $(this).find('.cc-remove').show();
            },function () {
                $(this).find('.cc-remove').hide();
            });
            $(that.element).find('#ccUserListBox a[data-action="removeCcUser"]').off('click').on('click',function () {

                var i = $(that.element).find('#ccUserListBox .approver-outbox').index($(this).closest('.approver-outbox'));
                that._ccCompanyUserList.splice(i,1);
                console.log(that._ccCompanyUserList)
                $(this).closest('.approver-outbox').remove();
            });
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action],a[data-action]').off('click').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');

                switch (dataAction){
                    case 'addItem':
                        that.addItem();
                        return false;
                        break;
                    case 'addCcUser'://添加抄送人

                        var options = {};
                        options.title = '添加抄送人员';
                        options.selectedUserList = that._ccCompanyUserList;
                        options.url = restApi.url_getOrgTree;
                        options.saveCallback = function (data) {
                            console.log(data)
                            that._ccCompanyUserList = data.selectedUserList;
                            var html = template('m_approval/m_approval_cost_add_ccUser', {userList: data.selectedUserList});
                            $(that.element).find('#ccUserListBox').html(html);
                            that.ccBoxDeal();
                        };
                        $('body').m_orgByTree(options);
                        break;
                    case 'addApprover'://添加审批人

                        var options = {};
                        options.title = '添加审批人员';
                        options.selectedUserList = that._auditList;
                        options.isASingleSelectUser = 2;
                        options.url = restApi.url_getOrgTree;
                        options.saveCallback = function (data) {
                            that._auditList = data.selectedUserList;
                            var html = template('m_approval/m_approval_cost_add_approver', {userList: data.selectedUserList});
                            $(that.element).find('#approverBox').html(html);
                            $(that.element).find('#approver-error.error').remove();//若有提示，删除
                        };
                        $('body').m_orgByTree(options);
                        break;
                }

            })
        }

        //表单验证
        ,save_validate:function(){
            var that = this;
            $(that.element).find('form.form-horizontal').validate({
                ignore : [],
                rules: {
                    leaveType: {
                        required: true
                    },
                    address:{
                        required: true,
                        maxlength: 250
                    },
                    leaveStartTime:{
                        required: true
                    },
                    leaveEndTime:{
                        required: true
                    },
                    leaveTime:{
                        required: true,
                        number:true,
                        minNumber:true
                    },
                    approver:{
                        approverCk: true
                    }
                },
                messages: {
                    leaveType: {
                        required: '请输入节点描述！'
                    },
                    address:{
                        required:'请输入出差地点！',
                        maxlength: '出差地点不超过250位！'
                    },
                    leaveStartTime:{
                        required:'请选择开始时间！'
                    },
                    leaveEndTime:{
                        required: '请选择结束时间！'
                    },
                    leaveTime:{
                        required: '请输入出差天数！',
                        number:'请输入有效数字',
                        minNumber:'请输入大于0的数字!'
                    },
                    approver:{
                        approverCk: '请选择审批人员！'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.appendTo(element.closest('.col-sm-10'));
                }
            });
            $.validator.addMethod('minNumber', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if( value<=0){
                    isOk = false;
                }
                return  isOk;
            }, '请输入大于0的数字!');
            $.validator.addMethod('approverCk', function(value, element) {

                var isOk = true;
                if(that._baseData.processFlag=='1' && (that._auditList==null || that._auditList.length==0)){
                    isOk = false;
                }
                return  isOk;

            }, '请选择审批人员!');
        }

    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            // if (!$.data(this, "plugin_" + pluginName)) {
            $.data(this, "plugin_" +
                pluginName, new Plugin(this, options));
            // }
        });
    };

})(jQuery, window, document);
