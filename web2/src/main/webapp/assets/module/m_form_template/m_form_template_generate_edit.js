/**
 * 表单生成界面，并保存
 * Created by wrb on 2018/9/20.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_form_template_generate_edit",
        defaults = {
             isDialog:true
            ,type:1//1=我的审批
            ,dataInfo:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentUserId = window.currentUserId;
        this._currentCompanyId = window.currentCompanyId;

        this._uploadmgrContainer = null;
        this._uuid = UUID.genV4().hexNoDelim;//targetId

        this._dataInfo = {};

        this._auditList = [];//审批人
        this._ccCompanyUserList = [];//抄送人
        this._deleteAttachList = [];//编辑才用，已有删除的集合

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            var option = {};
            option.url = restApi.url_initDynamicAudit;
            option.postData = {};
            if(that.settings.dataInfo && that.settings.dataInfo.formId)
                option.postData.formId = that.settings.dataInfo.formId;

            if(that.settings.dataInfo && that.settings.dataInfo.id)
                option.postData.id = that.settings.dataInfo.id;

            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    that._dataInfo = response.data;
                    var html = template('m_form_template/m_form_template_generate_edit',{dataInfo:response.data});
                    that.renderDialog(html,function () {

                        if(response.data && response.data.dynamicAudit && response.data.dynamicAudit.fieldList){

                            var fieldList = response.data.dynamicAudit.fieldList;
                            var j = -1;
                            $.each(fieldList,function (i,item) {

                                if(i==j)
                                    return true;

                                if(item.fieldType==4){//时间区间，需要合并一个组件
                                    j = i+1;
                                    item.fieldId2 = fieldList[i+1].fieldId;
                                    item.fieldTitle2 = fieldList[i+1].fieldTitle;
                                    item.fieldTooltip2 = fieldList[i+1].fieldTooltip;
                                    item.fieldValue2 = fieldList[i+1].fieldValue;
                                    item.dateFormatType2 = fieldList[i+1].dateFormatType;
                                }

                                if(item.fieldType!=9){
                                    var iHtml = template('m_form_template/m_form_template_itemForEdit',item);
                                    $(that.element).find('form').eq(0).append(iHtml);
                                }
                                //明细面板
                                if(item.detailFieldList!=null && item.detailFieldList.length>0){


                                    $.each(item.detailFieldList,function (panelI,panelItem) {

                                        var panelInfo = {};
                                        panelInfo.panelIndex = $(that.element).find('.form-item[data-type="9"] .panel').length+1;
                                        panelInfo.fieldType = 9;
                                        panelInfo.fieldId = item.fieldId;

                                        var $panel = $(that.element).find('.form-item[data-type="9"][data-field-id="'+item.fieldId+'"]');
                                        if($panel.length>0){
                                            panelInfo.fieldType = 'panelBody9';
                                            var iHtml = template('m_form_template/m_form_template_itemForEdit',panelInfo);
                                            $panel.find('button[data-action="addItem"]').parent().before(iHtml);
                                        }else{
                                            var iHtml = template('m_form_template/m_form_template_itemForEdit',panelInfo);
                                            $(that.element).find('form').eq(0).append(iHtml);
                                        }


                                        if(panelItem!=null && panelItem.length>0){
                                            var subJ = -1;
                                            $.each(panelItem,function (subI,subItem) {

                                                if(subI==subJ)
                                                    return true;

                                                if(subItem.fieldType==4){//时间区间，需要合并一个组件
                                                    subJ = subI+1;
                                                    subItem.fieldId2 = panelItem[subI+1].fieldId;
                                                    subItem.fieldTitle2 = panelItem[subI+1].fieldTitle;
                                                    subItem.fieldTooltip2 = panelItem[subI+1].fieldTooltip;
                                                    subItem.fieldValue2 = panelItem[subI+1].fieldValue;
                                                    subItem.dateFormatType2 = panelItem[subI+1].dateFormatType;
                                                }
                                                var iHtml = template('m_form_template/m_form_template_itemForEdit',subItem);
                                                $(that.element).find('.form-item[data-type="9"]:last .panel:eq('+panelI+') form').append(iHtml);
                                            });
                                        }

                                    });


                                }
                            });
                        }
                        that.renderICheckOrSelect($(that.element));
                        that.bindActionClick();
                        that.save_validate();
                        $(that.element).find('.form-item .panel').each(function () {
                            that.save_itemValidate($(this));
                        });
                        that.fileUpload();

                        if(that._dataInfo.dynamicAudit.ccCompanyUserList){

                            that._ccCompanyUserList = [];

                            $.each(that._dataInfo.dynamicAudit.ccCompanyUserList,function (i,item) {
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


                        that.inputKeyup();

                        //编辑
                        if(that.settings.dataInfo.id){

                            //时间区间
                            $(that.element).find('div[data-format-type]').each(function () {

                                var value = $(this).attr('data-value'),formatType = $(this).attr('data-format-type');
                                $(this).find('input').val(value.substring(0,10));
                                if(formatType==2 || formatType==3){
                                    $(this).find('select').eq(0).val(value.substring(11,13)).trigger('change');
                                }
                                if(formatType==2){
                                    $(this).find('select').eq(1).val(value.substring(14,16)).trigger('change');
                                }

                            });
                            if(that._dataInfo.dynamicAudit.attachList){

                                $.each(that._dataInfo.dynamicAudit.attachList,function (i,item) {
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

                            if(that._dataInfo.dynamicAudit.auditList && that._dataInfo.processFlag=='1'){

                                that._auditList = [];
                                $.each(that._dataInfo.dynamicAudit.auditList,function (i,item) {
                                    if(i==that._dataInfo.dynamicAudit.auditList.length-1){//取最后一条
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


                        return false;
                    });

                } else {
                    S_layer.error(response.info);
                }
            });

        }
        //渲染列表内容
        ,renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title:that.settings.dataInfo.formName || '我的审批',
                    area : ['900px','600px'],
                    fixed:true,
                    scrollbar:false,
                    content:html,
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
                        if (!flag) {
                            return false;
                        }
                        if(!(that._dataInfo.processType=='2' || that._dataInfo.processType=='3') && (that._auditList==null || that._auditList[0]==null) ){//不存在流程
                            S_toastr.error('请选择审批人！');
                            return false;
                        }
                        that.save();
                    }

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;
                    if(callBack)
                        callBack();
                });

            }else{//不以弹窗编辑
                $(that.element).html(html);
                if(callBack)
                    callBack();
            }

        }
        ,inputKeyup:function () {
            var that = this;
            //是否出现统计
            $(that.element).find('.form-item[data-type="5"][data-statistics="1"] input').on('keyup',function () {
                var expAmout = 0,isShowStatistics=0;
                $(that.element).find('.form-item[data-type="5"][data-statistics="1"] input').each(function () {
                    expAmout = expAmout + ($(this).val()-0);
                    isShowStatistics++;
                });
                if(isShowStatistics>0){
                    $(that.element).find('#isShowStatistics').show();
                    $(that.element).find('#isShowStatistics #expAmount').html(expAmout);
                }
                $(that.element).find('#isShowStatistics #expAmount').html(expAmout);
            });
            //用于分条件，流程变化
            $(that.element).find('.form-item[data-type="5"][data-statistical-condition="1"]').on('keyup',function () {
                if(that._dataInfo.processType=='3'){
                    console.log('that._dataInfo.processType==3');
                    that.renderApprover();
                }
            });
        }
        ,renderApprover:function () {
            var that = this;
            var expAmout = 0,userList = [];

            $(that.element).find('.form-item[data-type="5"][data-statistical-condition="1"] input').each(function () {

                expAmout = expAmout + ($(this).val()-0);
            });

            if(that._dataInfo.processType=='2'  && that._dataInfo.conditionList!=null && that._dataInfo.conditionList.length>0){//固定流程

                userList = that._dataInfo.conditionList[0].userList;

            }else if(that._dataInfo.processType=='3'  && that._dataInfo.conditionList!=null && that._dataInfo.conditionList.length>0){//条件流程

                $.each(that._dataInfo.conditionList,function (i,item) {

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
        //初始化iCheck
        ,renderICheckOrSelect:function ($ele) {

            var that = this;
            var ifChecked = function (e) {
            };
            var ifUnchecked = function (e) {
            };
            var ifClicked = function (e) {
            };
            $ele.find('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked).on('ifClicked',ifClicked);

            $ele.find('select').each(function () {
                if($(this).closest('.form-item').attr('data-type')==12){
                    $(this).select2({
                        allowClear: true,
                        //minimumResultsForSearch: -1,
                        placeholder: "请选择关联项目",
                        width:'100%',
                        language: "zh-CN"
                    });
                }else{
                    $(this).select2({
                        tags:false,
                        allowClear: false,
                        minimumResultsForSearch: -1,
                        placeholder: "请选择",
                        width:'100%',
                        language: "zh-CN"
                    });
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
                                S_layer.error(res.msg);
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
        ,recursiveData:function ($formItem,item) {

            switch (item.fieldType){
                case 1://单行文本
                case 3://日期
                case 5:
                    item.fieldValue =$formItem.find('input').val();
                    break;
                case 2://多行文本
                    item.fieldValue =$formItem.find('textarea').val();
                    break;
                case 4://日期区间
                    var yyyymmdd = $formItem.find('input').val();
                    if(item.dateFormatType==2){
                        var hh =  $formItem.find('select').eq(0).val();
                        var mm =  $formItem.find('select').eq(1).val();
                        item.fieldValue = yyyymmdd + ' ' + hh + ':' + mm;
                    }else if(item.dateFormatType==3){
                        var hh =  $formItem.find('select').eq(0).val();
                        item.fieldValue = yyyymmdd + ' ' + hh;
                    }else{
                        item.fieldValue = yyyymmdd;
                    }

                    break;
                case 6://下拉列表
                case 11://关联审批
                case 12://关联项目
                    item.fieldValue = $formItem.find('select').val();
                    break;
                case 7://单选
                    item.fieldValue = $formItem.find('input[name="item_radio"]').val();
                    break;
                case 8://复选
                    var v = $formItem.find('input[name="item_checkbox"]').val();
                    if(v!=null && typeof v == 'object'){
                        item.fieldValue = v.join(',');
                    }else{
                        item.fieldValue = isNullOrBlank(v)?'':v;
                    }
                    break;
                case 9:
                case 10:
                case 13:
                case 14:
                    break;

            }
            return item;
        }
        ,save:function () {
            var that = this;
            var $data = that._dataInfo;
            var fieldList =  $.extend(true, [], that._dataInfo.dynamicAudit.fieldList);
            $.each(fieldList,function (i,item) {
                var $formItem = $(that.element).find('div[data-field-id="'+item.fieldId+'"]');

                item = that.recursiveData($formItem,item);

                if(item.detailFieldList!=null && item.detailFieldList.length>0){

                    var detailFieldListLen = item.detailFieldList.length;
                    $.each(item.detailFieldList,function (panelI,panelItem) {

                        $.each(panelItem,function (subI,subItem) {
                            var $subFormItem = $(that.element).find('div[data-field-id="'+item.fieldId+'"] .panel:eq('+panelI+') div[data-field-id="'+subItem.fieldId+'"]');
                            subItem = that.recursiveData($subFormItem,subItem);
                        });
                    });

                    var $panel = $(that.element).find('div[data-field-id="'+item.fieldId+'"] .panel');
                    var detailsLen = $panel.length;
                    //明细多于1
                    if(detailsLen>detailFieldListLen){
                        var detailFieldList = $.extend(true, {}, item.detailFieldList[0]);
                        $panel.each(function (pi) {
                            if(pi<detailFieldListLen)
                                return true;
                            var itemArr = [];

                            $.each(detailFieldList,function (subI,subItem) {

                                var $ssubFormItem = $(that.element).find('div[data-field-id="'+item.fieldId+'"] .panel:eq('+pi+') div[data-field-id="'+subItem.fieldId+'"]');
                                itemArr.push(that.recursiveData($ssubFormItem,subItem)) ;
                            });
                            item.detailFieldList.push(itemArr);

                        });
                    }
                }
            });
            
            $data.dynamicAudit.targetId = that._uuid;
            $data.dynamicAudit.fieldList = fieldList;

            $data.dynamicAudit.ccCompanyUserList = that._ccCompanyUserList;

            if(!(that._dataInfo.processType=='2' || that._dataInfo.processType=='3')){//不存在流程
                $data.dynamicAudit.auditPerson = that._auditList[0].id;
            }

            var option = {};
            option.classId = '#content-right';
            option.url = restApi.url_saveDynamicAudit;
            option.postData = $data.dynamicAudit;

            if(that.settings.dataInfo && that.settings.dataInfo.id)
                option.postData.id = that.settings.dataInfo.id;

            console.log(option);
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('操作成功');
                    if(that.settings.saveCallBack)
                        that.settings.saveCallBack();
                } else {
                    S_layer.error(response.info);
                }
            });
        }
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action],button[data-action]').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'addItem'://添加明细

                        var $newPanel = $this.parent().prev().clone();
                        //$newPanel.find('select').select2('destroy');
                        $newPanel.find('.select2.select2-container').remove();
                        var panelBoxLen = $(that.element).find('.panel').length;
                        $newPanel.find('span[data-action="panelIndex"]').html(panelBoxLen+1);
                        $newPanel.find('input,textarea').val('');
                        $this.parent().before($newPanel.prop('outerHTML'));
                        that.renderICheckOrSelect($this.parent().prev());
                        $this.parent().prev().find('a[data-action="delItem"]').removeClass('hide').on('click',function () {
                            $(this).parents('.panel.panel-default').remove();
                            $(that.element).find('span[data-action="panelIndex"]').each(function (i) {
                                $(this).html(i+1);
                            });
                            return false;
                        });
                        that.save_itemValidate($this.parent().prev());
                        that.inputKeyup();
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

            });
        }
        ,validateOptions:function ($ele,options) {

            $ele.each(function (i) {
                var $this = $(this);
                var spanRed = $this.find('span.fc-red').length;
                var type = $this.attr('data-type');
                var key = $this.attr('data-field-id');
                if(type==4){//时间
                    $this.find('div[data-type="4"]').each(function () {
                        if(spanRed>0){
                            key = $(this).attr('data-field-id');
                            options.rules[key]={required: true};
                            options.messages[key]={required: '不为空！'};
                        }
                    });
                }else if(type==5){//数字
                    if(spanRed>0){
                        options.rules[key]={
                            required: true,
                            number:true
                        };
                        options.messages[key]={
                            required: '不为空！',
                            number:'请输入数字!'
                        };
                    }else{
                        options.rules[key]={
                            number:true
                        };
                        options.messages[key]={
                            number:'请输入数字!'
                        };
                    }
                }else{
                    if(spanRed>0){
                        options.rules[key]={required: true};
                        options.messages[key]={required: '不为空！'};
                    }
                }
            });
            return options;
        }
        ,save_validate:function(){
            var that = this;

            var options = {};
            options.rules = {};
            options.messages = {};
            options = that.validateOptions($(that.element).find('form#fieldForm>div.form-item'),options);
            options.errorPlacement=function (error, element) { //指定错误信息位置
                console.log(element)
                if(element.closest('div[data-type="4"]').length>0){
                    error.appendTo(element.parent().parent());
                }else{
                    error.appendTo(element.closest('.col-xs-10'));
                }

            };
            $(that.element).find('form#fieldForm').validate(options);

        }
        ,save_itemValidate:function($ele){
            var that = this;
            var options = {};
            options.rules = {};
            options.messages = {};
            options = that.validateOptions($ele.find('form .form-item'),options);

            options.errorPlacement=function (error, element) { //指定错误信息位置
                console.log(element)
                error.appendTo(element.closest('.col-xs-10'));
            };
            $ele.find('form').validate(options);
        }

    });

    /*
     1.一般初始化（缓存单例）： $('#id').pluginName(initOptions);
     2.强制初始化（无视缓存）： $('#id').pluginName(initOptions,true);
     3.调用方法： $('#id').pluginName('methodName',args);
     */
    $.fn[pluginName] = function (options, args) {
        var instance;
        var funcResult;
        var jqObj = this.each(function () {

            //从缓存获取实例
            instance = $.data(this, "plugin_" + pluginName);

            if (options === undefined || options === null || typeof options === "object") {

                var opts = $.extend(true, {}, defaults, options);

                //options作为初始化参数，若args===true则强制重新初始化，否则根据缓存判断是否需要初始化
                if (args === true) {
                    instance = new Plugin(this, opts);
                } else {
                    if (instance === undefined || instance === null)
                        instance = new Plugin(this, opts);
                }

                //写入缓存
                $.data(this, "plugin_" + pluginName, instance);
            }
            else if (typeof options === "string" && typeof instance[options] === "function") {

                //options作为方法名，args则作为方法要调用的参数
                //如果方法没有返回值，funcReuslt为undefined
                funcResult = instance[options].call(instance, args);
            }
        });

        return funcResult === undefined ? jqObj : funcResult;
    };

})(jQuery, window, document);
