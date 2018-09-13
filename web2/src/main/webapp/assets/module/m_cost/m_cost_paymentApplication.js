/**
 * 项目信息－付款计划-付款申请
 * Created by wrb on 2018/8/10.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_cost_paymentApplication",
        defaults = {
            isDialog:true,
            projectId:null,
            pointInfo:null,//节点信息
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
            var html = template('m_cost/m_cost_paymentApplication',{pointInfo:that.settings.pointInfo});
            that.renderDialog(html,function () {
                that.save_validate();
            });
        },
        //初始化数据,生成html
        renderDialog:function (html,callBack) {

            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.title||'付款申请',
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
        //保存
        ,save:function () {

            var that = this,option  = {};
            option.classId = that.element;
            option.postData = {};
            if(that.settings.pointInfo.isInnerCompany){//内部
                option.url = restApi.url_applyProjectCostPayFee;
                option.postData.id = that.settings.pointInfo.subId;
            }else{
                option.url = restApi.url_saveReturnMoneyDetail;
            }

            option.postData.projectId = that.settings.projectId;
            option.postData.pointId = that.settings.pointInfo.pointId;
            option.postData.payType = 2;
            option.postData.fee = $(that.element).find('input[name="fee"]').val();
            option.postData.pointDetailDescription = $(that.element).find('textarea[name="pointDetailDescription"]').val();

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
            var maxFee = (that.settings.pointInfo.fee).sub(that.settings.pointInfo.backFee);
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
                        required:'请输入金额！',
                        number:'请输入有效数字',
                        minNumber:'请输入大于0的数字!',
                        over10:'对不起，您的操作超出了系统允许的范围。合同总金额的单位为“万元”',
                        pointSix:'请保留小数点后六位!',
                        ckFee:'发起申请金额不能大于'+maxFee+'!'

                    }

                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.insertAfter(element);
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
            }, '发起申请金额不能大于'+maxFee+'!');
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
