/**
 * 项目信息－收款计划-添加节点
 * Created by wrb on 2018/8/10.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_cost_addNode",
        defaults = {
            isDialog:true,
            projectId:null,
            totalCost:null,
            costId:null,
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
            that._initHtml();
        },
        //初始化数据,生成html
        _initHtml:function () {

            var that = this;
            if(that.settings.isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title||'添加回款节点',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '600',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {

                    },
                    okText:'保存',
                    ok:function () {
                        var flag = $(that.element).find('form').valid();
                        if (!flag || that._saveContract()) {
                            return false;
                        }
                    }
                },function(d){//加载html后触发

                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    var html = template('m_cost/m_cost_addNode',{});
                    $(that.element).html(html);
                    that._saveContract_validate();
                    that._bindFeeCalculation();

                });
            }else{//不以弹窗编辑

            }
        }
        //保存合同回款节点
        ,_saveContract:function () {

            var that = this,option  = {},isError = false;
            option.classId = that.element;
            option.url = restApi.url_saveProjectCostPoint;
            option.async = false;
            var $data = $(that.element).find('form').serializeObject();
            option.postData = $data;
            option.postData.projectId = that.settings.projectId;
            option.postData.feeProportion = parseFloat(option.postData.feeProportion);
            option.postData.type = '1';
            option.postData.costId = that.settings.costId;

            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    if(that.settings.saveCallBack!=null){
                        that.settings.saveCallBack();
                    }

                }else {
                    S_dialog.error(response.info);
                    isError = true;
                }
            });
            return isError;
        }
        //添加回款的表单验证
        ,_saveContract_validate:function(){
            var that = this;
            $(that.element).find('form').validate({
                rules: {
                    feeDescription: {
                        required: true,
                        maxlength: 250
                    },
                    feeProportion:{
                        required: true,
                        number:true,
                        limitProportion:true,
                        pointNumber:true
                    },
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
                    feeDescription: {
                        required: '请输入节点描述!',
                        maxlength: '节点描述不超过250位!'
                    },
                    feeProportion:{
                        required:'请输入比例',
                        number:'请输入有效数字',
                        limitProportion:'请输入0到100之间的数字!',
                        pointNumber:'请保留小数点后两位!'
                    },
                    fee:{
                        required:'金额不能为空',
                        number:'请输入有效数字',
                        minNumber:'请输入大于0的数字!',
                        over10:'对不起，您的操作超出了系统允许的范围。合同总金额的单位为“万元”',
                        pointSix:'请保留小数点后六位!',
                        ckFee:'金额不能超过合同总金额!'

                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    if ($(element).attr('data-action')=='feeCalculation') {
                        error.appendTo(element.closest('.form-group'));
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            $.validator.addMethod('limitProportion', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if( value<=0 || value>100){
                    isOk = false;
                }
                return  isOk;
            }, '请输入0到100之间的数字!');
            $.validator.addMethod('minNumber', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if( value<=0){
                    isOk = false;
                }
                return  isOk;
            }, '请输入大于0的数字!');
            $.validator.addMethod('pointNumber', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if(!regularExpressions.proportionnumber.test(value)){
                    isOk = false;
                }
                return  isOk;
            }, '请保留小数点后两位!');
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
                var that = this;
                value = $.trim(value);
                var isOk = true;
                var total = that.settings.totalCost-0;
                if(value>total ){
                    isOk = false;
                }
                return  isOk;
            }, '金额不能超过合同总金额!');
        }
        //金额比例keyup事件
        ,_bindFeeCalculation:function () {
            var that = this;
            $(that.element).find('input[data-action="feeCalculation"]').keyup(function () {

                var name = $(this).attr('name');
                var val = $(this).val();
                var total = that.settings.totalCost;
                if(name=='feeProportion'){//比例
                    var fee = (val-0)*(total-0)/100;
                    fee = decimal(fee,7);
                    fee = parseFloat(decimal(fee,6));
                    $(that.element).find('input[name="fee"]').val(fee);
                }
                if(name=='fee'){//金额
                    var proportion = (val-0)/(total-0)*100;
                    proportion = decimal(proportion,3);
                    proportion = decimal(proportion,2);
                    $(that.element).find('input[name="feeProportion"]').val(proportion);
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
