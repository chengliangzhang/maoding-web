/**
 * 项目信息－收款计划-发起收款
 * Created by wrb on 2018/8/10.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_cost_addPayment",
        defaults = {
            isDialog:true,
            projectId:null,
            relationCompany:null,
            backFee:null,
            maxFee:null,
            pointId:null,
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
            var html = template('m_cost/m_cost_addPayment',{currentDate:getNowDate()});

            that.renderDialog(html,function () {
                that.renderListCompany();
                that.initICheck();
                that.save_validate();
                clickTimeIcon($(that.element));
            });
        },
        //初始化数据,生成html
        renderDialog:function (html,callBack) {

            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.title||'发起回款',
                    area : '750px',
                    content:html,
                    cancel:function () {
                    },
                    ok:function () {

                        var flag = $(that.element).find('form').valid();
                        if (!flag || that.save()) {
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
        //渲染收票方
        ,renderListCompany:function () {
            var that = this;
            var staffArr = [];
            var company = that.settings.relationCompany;
            if(company!=null){
                staffArr.push({
                    id: company.companyId,
                    text: company.companyName,
                    taxNumber:company.taxNumber

                });
            }
            $(that.element).find('select[name="invoiceName"]').select2({
                tags:true,
                allowClear: true,
                placeholder: "请选择或输入收票方名称!",
                language: "zh-CN",
                data: staffArr
            });

            //设置默认选择
            if(company!=null){
                $(that.element).find('select[name="invoiceName"]').val(company.companyId).trigger('change');
                $(that.element).find('input[name="taxIdNumber"]').val(company.taxNumber==null?'':company.taxNumber);
            }
            //select change事件
            $(that.element).find('select[name="invoiceName"]').on('change', function (e) {

                var selectedData = $(this).select2("data")[0];
                if(isNullOrBlank(selectedData)){
                    $(that.element).find('input[name="taxIdNumber"]').val('');
                }
                else{
                    $(that.element).find('input[name="taxIdNumber"]').val(selectedData.taxNumber==null?'':selectedData.taxNumber);
                }
            })


        }
        //初始化iCheck
        ,initICheck:function () {
            var that = this;
            var ifChecked = function (e) {
                //that.add_invoice_validate();
                //$(that.element).find('.invoice-box').show();
            };
            var ifUnchecked = function (e) {
                //that.remove_invoice_validate();
                //$(that.element).find('.invoice-box').hide();
            };
            var ifClicked = function (e) {
                var val = $(this).val();
                if(val==1){

                    $(that.element).find('.invoice-box').show();
                    $(that.element).find('.invoice-box1').hide();
                    that.add_invoice_validate();
                    that.remove_invoice_validate1();

                }else if(val==2){

                    $(that.element).find('.invoice-box').show();
                    $(that.element).find('.invoice-box1').show();
                    that.add_invoice_validate();
                    that.add_invoice_validate1();

                }else{

                    $(that.element).find('.invoice-box').hide();
                    $(that.element).find('.invoice-box1').hide();
                    that.remove_invoice_validate();
                    that.remove_invoice_validate1();
                }
            };
            $(that.element).find('input[name="invoiceType"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked).on('ifClicked',ifClicked);
        }
        //保存
        ,save:function () {

            var that = this,option  = {};
            option.classId = that.element;
            option.url = restApi.url_saveReturnMoneyDetail;
            option.postData = {};
            option.postData.projectId = that.settings.projectId;
            option.postData.pointId = that.settings.pointId;
            option.postData.payType = 1;
            option.postData.fee = $(that.element).find('input[name="fee"]').val();

            var invoiceType = $(that.element).find('input[name="invoiceType"]:checked').val();
            if(invoiceType!=0) {
                option.postData.isInvoice = '1';
                option.postData.applyDate = $(that.element).find('input[name="applyDate"]').val();

                var id = $(that.element).find('select[name="invoiceName"]').val();
                var text = $(that.element).find('select[name="invoiceName"] option:selected').text();
                if(id==text){//输入的
                    option.postData.relationCompanyName = id;
                }else{//选择的
                    option.postData.relationCompanyId = id;
                }

                option.postData.taxIdNumber = $(that.element).find('input[name="taxIdNumber"]').val();
                option.postData.invoiceContent = $(that.element).find('input[name="invoiceContent"]').val();
                option.postData.invoiceRemark = $(that.element).find('textarea[name="invoiceRemark"]').val();

                option.postData.invoiceType = invoiceType;
                option.postData.address = $(that.element).find('input[name="address"]').val();
                option.postData.cellphone = $(that.element).find('input[name="cellphone"]').val();
                option.postData.accountBank = $(that.element).find('input[name="accountBank"]').val();
                option.postData.bankNo = $(that.element).find('input[name="bankNo"]').val();

            }else{
                option.postData.isInvoice = '0';
            }

            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    if(that.settings.saveCallBack)
                        that.settings.saveCallBack();
                }else {
                    S_layer.error(response.info);
                }
            });
        }
        //添加回款的表单验证
        ,save_validate:function(){
            var that = this;
            var maxFee = that.settings.maxFee.sub(that.settings.backFee-0);
            $(that.element).find('form').validate({
                rules: {
                    fee:{
                        required: true,
                        number:true,
                        minNumber:true,
                        over10:true,//整数部分是否超过10位
                        pointSix:true,
                        ckFee:true
                    }
                },
                messages: {
                    fee:{
                        required:'发起金额不能为空',
                        number:'请输入有效数字',
                        minNumber:'请输入大于0的数字!',
                        over10:'对不起，您的操作超出了系统允许的范围。合同总金额的单位为“万元”',
                        pointSix:'请保留小数点后六位!',
                        ckFee:'发起回款金额不能大于'+maxFee+'!'

                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    //error.insertAfter(element);
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
            $.validator.addMethod('over10', function (value, element) {
                value = $.trim(value);
                var isOk = true;
                if(parseInt(value).toString().length>10)
                    isOk=false;
                return isOk;
            }, '对不起，您的操作超出了系统允许的范围。合同总金额的单位为“万元”');
            $.validator.addMethod('pointSix', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if(!regularExpressions.pnumber.test(value)){
                    isOk = false;
                }
                return  isOk;
            }, '请保留小数点后六位!');

            $.validator.addMethod('ckFee', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                var total = maxFee-0;
                if(value>total ){
                    isOk = false;
                }
                return  isOk;
            }, '发起回款金额不能大于'+maxFee+'!');
        }

        //添加发票验证
        ,add_invoice_validate:function () {
            var that = this;
            $(that.element).find('form input[name="applyDate"]').rules('add',{
                required:true,
                messages:{
                    required:'请选择申请时间！'
                }
            });
            $(that.element).find('form select[name="invoiceName"]').rules('add',{
                required:true,
                messages:{
                    required:'请输入收票方名称！'
                }
            });
            $(that.element).find('form input[name="taxIdNumber"]').rules('add',{
                required:true,
                messages:{
                    required:'请输入纳税识别号！'
                }
            });
            $(that.element).find('form input[name="invoiceContent"]').rules('add',{
                required:true,
                messages:{
                    required:'请输入开票内容！'
                }
            });
        }
        //删除发票验证
        ,remove_invoice_validate:function () {
            var that = this;
            $(that.element).find('form input[name="applyDate"]').rules('remove','required');
            $(that.element).find('form select[name="invoiceName"]').rules('remove','required');
            $(that.element).find('form input[name="taxIdNumber"]').rules('remove','required');
            $(that.element).find('form input[name="invoiceContent"]').rules('remove','required');
        }
        //添加发票验证
        ,add_invoice_validate1:function () {
            var that = this;
            $(that.element).find('form input[name="address"]').rules('add',{
                required:true,
                messages:{
                    required:'请输入地址！'
                }
            });
            $(that.element).find('form select[name="cellphone"]').rules('add',{
                required:true,
                messages:{
                    required:'请输入电话！'
                }
            });
            $(that.element).find('form input[name="accountBank"]').rules('add',{
                required:true,
                messages:{
                    required:'请输入开户行！'
                }
            });
            $(that.element).find('form input[name="bankNo"]').rules('add',{
                required:true,
                messages:{
                    required:'请输入账号！'
                }
            });
        }
        //删除发票验证
        ,remove_invoice_validate1:function () {
            var that = this;
            $(that.element).find('form input[name="address"]').rules('remove','required');
            $(that.element).find('form select[name="cellphone"]').rules('remove','required');
            $(that.element).find('form input[name="accountBank"]').rules('remove','required');
            $(that.element).find('form input[name="bankNo"]').rules('remove','required');
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
