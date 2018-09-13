/**
 * 项目-收支管理-添加收款计划/付款计划
 * Created by wrb on 2018/8/8.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_cost_collectionPlan_add",
        defaults = {
            isDialog:true,
            doType:0,//操作类型 0=收款，1=付款
            projectId:null,
            oKCallBack:null//保存回滚事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentUserId = window.currentUserId;
        this._currentCompanyId = window.currentCompanyId;
        this._uploadmgrContainer = null;//上传容器
        this._contactAttachList = [];//关联合同-上传的合同附件
        this._attachList = null;//查询的合同列表

        this._title = '收款';
        if(this.settings.doType==1){//付款
            this.settings.title = '添加付款计划';
            this._title = '付款';
        }

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData();
        }
        ,renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.title||'添加收款计划',
                    area : '600px',
                    content:html,
                    cancel:function () {
                    },
                    ok:function () {

                        if ($(that.element).find('form.form-horizontal').valid()) {
                            return that.save();
                        }else{
                            return false;
                        }
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
        //渲染列表内容
        ,initHtmlData:function () {
            var that =this;

            var html = template('m_cost/m_cost_collectionPlan_add',{
                doType:that.settings.doType,
                title:that._title
            });
            that.renderDialog(html,function () {
                that.submit_validate();
                that.initSelect2();
                that.uploadRecordFile();
                that.bindActionClick();
            });

        }
        //select2初始化
        ,initSelect2:function () {
            var that = this;
            $(that.element).find('select[name="feeType"]').select2({
                tags:false,
                allowClear: false,
                //containerCssClass:'select-sm b-r-sm',
                minimumResultsForSearch: -1,
                language: "zh-CN"
            });
            $(that.element).find('select[name="feeType"]').on("change", function (e) {
                var feeType = $(this).val();
                if(isNullOrBlank(feeType))
                    return false;

                var option = {};
                option.url = restApi.url_listCompany;
                option.postData = {};
                option.postData.feeType = feeType;
                option.postData.isPay = that.settings.doType;
                option.postData.projectId = that.settings.projectId;
                m_ajax.postJson(option, function (response) {
                    if (response.code == '0') {

                        that.renderListCompany(response.data);

                    } else {
                        S_layer.error(response.info);
                    }
                });
            });

            var option = {};
            option.classId = that.element;
            option.url = restApi.url_getProjectContract;
            option.postData = {};
            option.postData.projectId = that.settings.projectId;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    that._attachList = response.data;
                    var staffArr = [];
                    var selectArr = response.data;
                    if(selectArr!=null && selectArr.length>0){
                        $.each(selectArr, function (i, o) {
                            staffArr.push({
                                id: o.id,
                                text: o.fileName
                            });
                        });
                    }
                    $(that.element).find('select[name="projectContract"]').select2({
                        //tags: true,
                        tokenSeparators: [',', ' '],
                        multiple: true,
                        placeholder: "请选择或上传关联合同!",
                        language: "zh-CN",
                        data: staffArr
                    });

                    $(that.element).find('.select2-multiple input[type="search"]').on("blur focus",function(){
                        //console.log('blur')
                        var len = $(that.element).find('.select2-multiple .select2-selection__rendered li.select2-selection__choice').length;
                        if(len>0){
                            $(this).css('width','0.75em');
                            $(this).attr('placeholder','');
                        }else{
                            $(this).css('width','390px');
                            $(this).attr('placeholder','请选择或上传关联合同!');
                        }
                    })

                } else {
                    S_layer.error(response.info);
                }
            });


        }
        //渲染付款单位
        ,renderListCompany:function (data) {
            var that = this;
            var staffArr = [];
            var selectArr = data;
            if(selectArr!=null && selectArr.length>0){
                $.each(selectArr, function (i, o) {
                    staffArr.push({
                        id: o.id,
                        text: o.name
                    });
                });
            }
            $(that.element).find('select[name="receivingUnit"]').select2({
                tags:true,
                allowClear: true,
                //containerCssClass:'select-sm b-r-sm',
                //minimumResultsForSearch: -1,
                placeholder: "请选择或输入付款单位!",
                language: "zh-CN",
                data: staffArr
            });

        }
        //上传附件
        ,uploadRecordFile: function () {
            var that = this;
            $(that.element).find('button[data-action="upload"]').m_fileUploader({
                server: restApi.url_attachment_uploadCostPlanAttach,
                fileExts: 'pdf',
                fileSingleSizeLimit:20*1024*1024,
                formData: {},
                multiple:true,
                //duplicate:false,
                loadingId: that.element,
                innerHTML: '上传',
                uploadBeforeSend: function (object, data, headers) {
                    data.companyId = window.currentCompanyId;
                    data.accountId = window.currentUserId;
                    data.projectId = that.settings.projectId;

                },
                uploadSuccessCallback: function (file, response) {
                    //console.log(response)
                    S_toastr.success('上传成功！');

                    //上传后append到文件选择框
                    var $ele = $(that.element).find('.select2-multiple .select2-selection__rendered');
                    var iHtml = '<li class="select2-selection__choice" title="'+response.data.fileName+'" data-id="'+response.data.netFileId+'">' +
                        '<span class="select2-selection__choice__remove" role="presentation">×</span>'+response.data.fileName+'</li>';

                    $(that.element).find('.select2-multiple .select2-selection__rendered .select2-search--inline').before(iHtml);
                    that._contactAttachList.push(response.data.netFileId);

                    $(that.element).find('.select2-multiple input[type="search"]').css('width','0.75em');
                    $(that.element).find('.select2-multiple input[type="search"]').attr('placeholder','');

                    //删除事件绑定
                    $ele.find('li.select2-selection__choice').last().find('.select2-selection__choice__remove').on('click',function () {

                        var id = $(this).parent().attr('data-id');
                        that._contactAttachList.splice(that._contactAttachList.indexOf(id),1);
                        $(this).parent().remove();
                    });
                }
            },true);
        }
        ,save:function () {
            var that =this;
            var option = {};
            option.classId = '#content-right';
            option.url = restApi.url_saveProjectCost;
            option.postData = {};
            option.postData.projectId = that.settings.projectId;

            if(that.settings.doType==0){
                option.postData.fromCompanyId = $(that.element).find('select[name="receivingUnit"]').val();
            }else{
                option.postData.toCompanyId = $(that.element).find('select[name="receivingUnit"]').val();
            }

            option.postData.fee = $(that.element).find('input[name="fee"]').val();
            option.postData.type = $(that.element).find('select[name="feeType"]').val();

            var contactAttactList = [];
            //从select选择的
            var selectedAttact = $(that.element).find('select[name="projectContract"]').val();
            if(selectedAttact!=null && selectedAttact.length>0){
                $.each(selectedAttact,function (i,item) {
                    contactAttactList.push({id:item})
                })
            }

            if(that._contactAttachList.length>0){
                $.each(that._contactAttachList,function (i,item) {
                    contactAttactList.push({id:item,isUploadFile:1})
                })
            }
            option.postData.contactAttachList = contactAttactList;
            option.postData.payType = that.settings.doType==0?1:2;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    if(that.settings.oKCallBack)
                        that.settings.oKCallBack();

                } else {
                    S_layer.error(response.info);
                }
            });
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').off('click').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'getProjectContract'://选择合同

                        var $ele = $(that.element).find('input[name="projectContract"]');
                        var val = $ele.val();
                        if($ele.val()==''){
                            $ele.val($this.text());
                        }else{
                            $ele.val(val+','+$this.text());
                        }


                        return false;
                        break;
                }

            });
            $(that.element).find('input[name="projectContract"],input[name="receivingUnit"]').click(function (e) {
                $(this).parent().find('button[data-toggle="dropdown"]').click();
                e.stopPropagation()
            });

        }
        //保存验证
        , submit_validate: function () {
            var that = this;
            $(that.element).find('form.form-horizontal').validate({
                rules: {
                    conditionalVal: {
                        ckIsEmpty:true,
                        ckIsNumber:true
                    }
                },
                messages: {
                    conditionalVal: {
                        ckIsEmpty: '请输入数值区间！',
                        ckIsNumber:'请输入数字！'
                    }
                },
                errorElement: "label",  //用来创建错误提示信息标签
                errorPlacement: function (error, element) {  //重新编辑error信息的显示位置
                    error.appendTo(element.closest('.form-group'));
                }
            });
            $.validator.addMethod('ckIsEmpty', function(value, element) {
                var isOk = true;
                $(that.element).find('input[name="conditionalVal"]').each(function () {
                    var val = $(this).val();
                    if($.trim(val)==''){
                        isOk = false;
                        return false;
                    }
                });
                return  isOk;
            }, '请输入数值区间！');
            $.validator.addMethod('ckIsNumber', function(value, element) {
                var isOk = true;
                $(that.element).find('input[name="conditionalVal"]').each(function () {
                    var val = $(this).val();
                    if(!regularExpressions.number.test(val)){
                        isOk = false;
                        return false;
                    }
                });
                return  isOk;
            }, '请输入数字！');
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
