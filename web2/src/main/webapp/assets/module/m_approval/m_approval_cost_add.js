/**
 * 添加报销申请
 * Created by wrb on 2018/9/4.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_cost_add",
        defaults = {
            isDialog:true,
            dataInfo:null,//dataInfo不为null,即编辑
            doType: 1// 报销=1=expense,费用=2=costApply,请假=3=leave,出差=4=onBusiness,付款申请=5=projectPayApply
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
        this._uuid = UUID.genV4().hexNoDelim;//targetId
        this._baseData = null;
        this._auditList = [];//审批人
        this._ccCompanyUserList = [];//抄送人


        this._deleteAttachList = [];//编辑才用，已有删除的集合

        this._title = this.settings.doType==1?'报销':'费用';
        this._auditType = this.settings.doType==1?'expense':'costApply';//auditType 的取值： 请假leave 出差onBusiness 报销expense 费用costApply 付款申请projectPayApply
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderDialog(function () {
                var option = {};
                option.url = restApi.url_getExpBaseData;
                option.postData = {
                    auditType:that._auditType
                };
                m_ajax.postJson(option, function (response) {
                    if (response.code == '0') {

                        that._baseData = response.data;
                        that._baseData.doType = that.settings.doType;
                        that._baseData.title = that._title;
                        var html = template('m_approval/m_approval_cost_add', {
                            data: that._baseData,
                            dataInfo:that.settings.dataInfo
                        });
                        $(that.element).html(html);
                        that.fileUpload();
                        that.bindActionClick();
                        that.save_validate();

                        if(that.settings.dataInfo!=null){

                            if(that.settings.dataInfo.detailList){
                                $.each(that.settings.dataInfo.detailList,function (i,item) {
                                    that.addItem(item);
                                });
                            }else{
                                that.addItem();
                            }

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
                            that.addItem();
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
                    title: that.settings.title||that._title+'申请',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '705',
                    tPadding: '0',
                    height:'650',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {

                    },
                    ok:function () {

                        var error = [];
                        var flag = $(that.element).find('form').valid();

                        $(that.element).find('.panel.panel-default form').each(function (i) {
                            if(!$(this).valid()){
                                error.push(i);
                            }
                        });
                        if(error.length>0){
                            flag = false;
                        }
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
        //保存
        ,save:function () {
            var that = this;
            var $data = {};
            $data.detailList = [];
            $(that.element).find('.panel.panel-default').each(function () {
                var $this = $(this), expItem = {};
                expItem.expAmount = $this.find('input[name="expAmount"]').val();
                expItem.expUse = $this.find('textarea[name="expUse"]').val();
                expItem.projectId = $this.find('select[name="projectName"]').val();
                expItem.expType =  $this.find('select[name="expType"]').val();
                expItem.expAllName = $this.find('select[name="expType"] option:selected').parent().attr('label')+'-'+$this.find('select[name="expType"] option:selected').text();

                var linkageApproval = $this.find('select[name="linkageApproval"]').val();
                if(!isNullOrBlank(linkageApproval)){
                    expItem.relationRecord = {
                        relationId : linkageApproval,
                        recordType : '13'
                    };
                }
                $data.detailList.push(expItem);
            });
            $data.type = that.settings.doType;

            $data.userId = that._currentCompanyUserId;
            //$data.expNo = that._maxExpNo;
            $data.targetId = that._uuid;

            var ccUser = [] ;
            if(that._ccCompanyUserList!=null && that._ccCompanyUserList.length>0){
                $.each(that._ccCompanyUserList,function (i,item) {
                    ccUser.push(item.id);
                });
            }
            $data.ccCompanyUserList = ccUser;

            if(that.settings.doType==2){//费用申请
                $data.remark = $(that.element).find('textarea[name="remark"]').val();
                $data.enterpriseName = $(that.element).find('input[name="enterpriseName"]').val();
            }
            if(!(that._baseData.processType=='2' || that._baseData.processType=='3')){//不存在流程
                $data.auditPerson = that._auditList[0].id;
            }

            if(that.settings.dataInfo){
                $data.id = that.settings.dataInfo.id;
                $data.deleteAttachList = that._deleteAttachList;
            }

            var option = {};
            option.classId = '#content-right';
            option.url = restApi.url_saveOrUpdateExpMainAndDetail;
            option.postData = $data;

            console.log(option);
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
        //添加明细
        ,addItem:function (item) {
            var that = this;
            var panelBoxLen = $(that.element).find('.panel').length;
            that._baseData.itemIndex = panelBoxLen+1;
            var html = template('m_approval/m_approval_cost_add_item', {
                data:that._baseData,
                dataInfo:item
            });
            $(that.element).find('button[data-action="addItem"]').parents('.form-group').before(html);

            var $ele = $(that.element).find('button[data-action="addItem"]').parents('.form-group').prev();
            $ele.find('select[name="expType"]').select2({
                tags:false,
                allowClear: false,
                minimumResultsForSearch: -1,
                width:'100%',
                language: "zh-CN"
            });
            $ele.find('select[name="projectName"]').select2({
                allowClear: true,
                //minimumResultsForSearch: -1,
                placeholder: "请选择关联项目!",
                width:'100%',
                language: "zh-CN"
            });
            $ele.find('a[data-action="delItem"]').on('click',function () {
                $(this).closest('.panel').remove();
                $(that.element).find('span[data-action="itemIndex"]').each(function (i) {
                    $(this).html(i+1);
                });
            });
            $ele.find('select[name="linkageApproval"]').select2({
                width: '100%',
                allowClear: true,
                language: "zh-CN",
                minimumResultsForSearch: Infinity,
                placeholder: "请选择关联审批!"
            });

            $ele.find('input[name="expAmount"]').on('keyup',function () {

                var expAmout = 0;

                $(that.element).find('input[name="expAmount"]').each(function () {

                    expAmout = expAmout + ($(this).val()-0);
                });
                $(that.element).find('#expAmount').html(expAmout);

                if(that._baseData.processType=='3'){
                    that.renderApprover();
                }
            });
            that.save_itemValidate($ele);
        }
        ,renderApprover:function () {
            var that = this;
            var expAmout = 0,userList = [];

            $(that.element).find('input[name="expAmount"]').each(function () {

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
                $(this).closest('.approver-outbox').remove();
            });
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action],a[data-action]').off('click').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');

                switch (dataAction){
                    case 'addItem'://添加明细
                        that.addItem();
                        return false;
                        break;

                    case 'addCcUser'://添加抄送人

                        var options = {};
                        options.title = '添加抄送人员';
                        options.selectedUserList = [];
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
                    approver:{
                        approverCk: true
                    }
                },
                messages: {
                    approver:{
                        approverCk: '请选择审批人员！'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.appendTo(element.closest('.col-sm-10'));
                }
            });
            $.validator.addMethod('approverCk', function(value, element) {

                var isOk = true;
                if(that._baseData.processFlag=='1' && (that._auditList==null || that._auditList.length==0)){
                    isOk = false;
                }
                return  isOk;

            }, '请选择审批人员!');
        }
        ,save_itemValidate:function($ele){
            var that = this;
            $ele.find('form').validate({
                rules: {
                    expAmount: {
                        required: true,
                        number:true,
                        minNumber:true,
                        maxlength:25,//是否超过25位
                        pointTwo:true
                    },
                    expUse:{
                        required: true
                    },
                    projectName:{
                        associatedProjectCk: true
                    }
                },
                messages: {
                    expAmount: {
                        required: '请输入金额！',
                        number:'请输入有效数字',
                        minNumber:'请输入大于0的数字!',
                        maxlength:'对不起，您的操作超出了系统允许的范围。',
                        pointTwo:'请保留小数点后两位!'
                    },
                    expUse:{
                        required: '请输入用途说明！'
                    },
                    projectName:{
                        associatedProjectCk: '请选择关联项目！'
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
            $.validator.addMethod('pointTwo', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if(!regularExpressions.proportionnumber.test(value)){
                    isOk = false;
                }
                return  isOk;
            }, '请保留小数点后两位!');
            $.validator.addMethod('associatedProjectCk', function(value, element) {

                var isOk = true;
                var $panel = $(element).closest('.panel');
                value = $panel.find('select[name="projectName"]').val();
                var expParentType = $panel.find('select[name="expType"] option:selected').parent().attr('label');
                if(expParentType == '直接项目成本' && isNullOrBlank(value)){
                    isOk = false;
                }
                return  isOk;

            }, '请选择关联项目!');
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
