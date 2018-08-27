/**
 * 项目信息－收款计划-发起收款
 * Created by wrb on 2018/8/10.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_cost_confirmInvoice",
        defaults = {
            isDialog:true,
            dialogHeight:null,//弹窗高度
            projectId:null,
            invoiceId:null,//发票ID
            pointInfo:null,//节点信息
            taskId:null,//任务ID
            saveCallBack:null
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
            that.renderDialog(function () {

                var option  = {};
                option.classId = that.element;
                option.url = restApi.url_getInvoice;
                option.postData = {
                    id:that.settings.invoiceId
                };

                m_ajax.postJson(option,function (response) {
                    if(response.code=='0'){

                        var html = template('m_cost/m_cost_confirmInvoice',{
                            pointInfo:that.settings.pointInfo,
                            invoiceInfo:response.data
                        });
                        $(that.element).html(html);
                        that.renderListCompany();
                        //that.initICheck();
                        that.save_validate();
                        //clickTimeIcon($(that.element));

                    }else {
                        S_dialog.error(response.info);
                    }
                });


            });
        },
        //初始化数据,生成html
        renderDialog:function (callBack) {

            var that = this;
            if(that.settings.isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title||'确认开票',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '600',
                    minHeight: that.settings.dialogHeight||'200',
                    tPadding: '0px',
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
        //渲染收票方
        ,renderListCompany:function () {
            var that = this;
            var staffArr = [];
            var company = that.settings.relationCompany;
            if(company!=null){
                staffArr.push({
                    id: company.companyId,
                    text: company.companyName
                });
            }
            $(that.element).find('select[name="invoiceName"]').select2({
                tags:true,
                allowClear: true,
                placeholder: "请选择或输入收票方名称!",
                language: "zh-CN",
                data: staffArr
            });
        }
        //初始化iCheck
        ,initICheck:function () {
            var that = this;
            var ifChecked = function (e) {
            };
            var ifUnchecked = function (e) {
            };
            var ifClicked = function (e) {

                if($(this).val()==2){
                    $(that.element).find('.invoice-box').show();
                }else{
                    $(that.element).find('.invoice-box').hide();
                }

            };
            $(that.element).find('input[name="invoiceType"]').iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked).on('ifClicked',ifClicked);

        }
        //保存
        ,save:function () {

            var that = this,option  = {};
            option.classId = that.element;
            option.url = restApi.url_handleMyTask;
            option.postData = {};

            option.postData.invoiceData = $(that.element).find("form.form-horizontal").serializeObject();
            option.postData.invoiceData.id = that.settings.invoiceId;
            option.postData.projectId = that.settings.projectId;
            option.postData.id = that.settings.taskId;


            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    if(that.settings.saveCallBack)
                        that.settings.saveCallBack();
                }else {
                    S_dialog.error(response.info);
                }
            });
        }
        //添加回款的表单验证
        ,save_validate:function(){
            var that = this;
            $(that.element).find('form').validate({
                rules: {
                    invoiceNo:{
                        required: true
                    }/*,
                    address:{
                        required: true
                    },
                    cellphone:{
                        required: true
                    },
                    accountBank:{
                        required: true
                    },
                    bankNo:{
                        required: true
                    }*/
                },
                messages: {
                    invoiceNo:{
                        required:'请输入发票号！'

                    }/*,
                    address:{
                        required: '请输入地址！'
                    },
                    cellphone:{
                        required: '请输入电话！'
                    },
                    accountBank:{
                        required: '请输入开户行！'
                    },
                    bankNo:{
                        required: '请输入账号！'
                    }*/

                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.insertAfter(element);
                }
            });
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
