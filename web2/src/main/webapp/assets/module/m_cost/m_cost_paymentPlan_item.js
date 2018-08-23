/**
 * 项目信息－收款计划列表-子项
 * Created by wrb on 2018/8/10.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_cost_paymentPlan_item",
        defaults = {
            myTaskId:null,//任务ID
            projectId:null,
            isAppend:false,//是否追加到that.element
            projectCost:null//合同回款对象
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this._projectCost = this.settings.projectCost;
        this._pageText = {};//界面的文字
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            if(that.settings.isAppend){
                $(that.element).append('<div class="panel revenue-expenditure" data-id="'+that._projectCost.costId+'"></div>');
                that.element = '.panel[data-id="'+that._projectCost.costId+'"]';
            }

            that.initHtml();

        }
        //初始化数据,生成html
        ,initHtml:function (t) {
            var that = this;
            that.postData(function () {

                that.initText();
                that._projectCost.currentCompanyId = that._currentCompanyId;
                var html = template('m_cost/m_cost_paymentPlan_item',that._projectCost);
                $(that.element).html(html);

                $(that.element).find('span[data-toggle="tooltip"]').tooltip();

                rolesControl();//权限控制初始化
                stringCtrl('feeDescription');

                that.bindEditable();
                that.bindActionClick();
                that.initEditCostPoint();
                that.uploadRecordFile();
            },t);
        }
        //{t==1，重新请求数据}
        ,postData:function (callBack,t) {

            var that = this;
            //重新请求数据
            if(t==1 || that._projectCost==null){
                var option  = {};
                option.classId = that.element;
                option.url = restApi.url_listProjectCost;
                option.postData = {};
                option.postData.payType = '2';
                option.postData.projectId = that.settings.projectId;
                option.postData.costId = that._projectCost.costId;
                if(that.settings.myTaskId!=null)
                    option.postData.myTaskId = that.settings.myTaskId;

                m_ajax.postJson(option,function (response) {
                    if(response.code=='0'){

                        that._projectCost = response.data.costList[0];
                        if(callBack!=null)
                            callBack();
                    }else {
                        S_dialog.error(response.info);
                    }
                })
            }else{
                if(callBack!=null)
                    callBack();
            }
        }
        //初始化文字
        ,initText : function () {
            var that = this;

            switch (that._projectCost.type){
                case '1': //合同回款
                    that._pageText = {
                        title:'合同回款'
                    };
                    break;
                case '2': //技术审查费
                    that._pageText = {
                        title:'技术审查费'
                    };
                    break;
                case '3': //合作设计费
                    that._pageText = {
                        title:'合作设计费'
                    };
                    break;
                case '4': //
                    that._pageText = {
                        title:'其他支出'
                    };
                    break;
                case '5': //其他收款
                    that._pageText = {
                        title:'其他收款'
                    };
                    break;
            }
            that._projectCost.pageText = that._pageText;
        }
        //初始化合同总金额 － 在位编辑
        ,bindEditable:function () {
            var that = this;
            var $this = $(that.element).find('a.totalContract');
            $this.editable({//编辑
                type: 'text',
                title: '合同总金额',
                emptytext:'0',
                savenochange:true,
                placeholder: '金额',
                value: ($this.text().indexOf('未设置')>-1||$this.attr('data-value')=='')?'':$.trim($this.attr('data-value')),
                success: function(response, newValue) {
                    that.saveInitContract(newValue);
                },
                display: function(value, sourceData) {},
                validate: function(value) {
                    var errTips = '';
                    if($.trim(value) == ''){
                        errTips = '合同总金额不能为空';
                    }else if(parseInt(value).toString().length>10){
                        errTips = '对不起，您的操作超出了系统允许的范围。合同总金额的单位为“万元”'
                    }else if(value<=0){
                        errTips = '请输入大于0的数字'
                    }else{
                        var pnumber = regularExpressions.pnumber;
                        if(!pnumber.test(value)){
                            errTips = '请保留小数点后六位!';
                        }
                    }
                    return errTips;
                }
            });
        }
        //保存合同总金额
        ,saveInitContract:function (newValue) {

            var that = this,option  = {};
            option.classId = that.element;
            option.url = restApi.url_saveProjectCost;
            var param ={};
            param.fee = newValue;
            param.type = that._projectCost.type;
            param.projectId = that.settings.projectId;
            if(that._projectCost!=null && that._projectCost.costId!=null && that._projectCost.costId!=''){
                param.id = that._projectCost.costId;
            }
            option.postData = param;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    that.initHtml(1);

                }else {
                    S_dialog.error(response.info);
                }
            })
        }

        //初始化编辑x-editable{dataEditType:1=节点描述，2＝比例，3＝金额，4＝明细金额,6=明细付款金额}
        ,initEditCostPoint:function () {
            var that = this,projectId = that.settings.projectId;

            $(that.element).find('a[data-action="editContract"][data-edit-type="2"],a[data-action="editContract"][data-edit-type="3"]').on('click',function () {
                var options = {},_this = this;

                var dataEditType = $(this).attr('data-edit-type');

                options.content = template('m_cost/m_editFee');
                options.titleHtml = '<h3 class="popover-title">编辑</h3>';
                options.closeOnDocumentClicked=function (e) {

                    if($(e.target).closest('.ui-popup-backdrop').length > 0 || $(e.target).closest('.ui-popup').length > 0){
                        return false;
                    }
                };
                options.onShown = function ($popover) {
                    //恢复浮动窗口里的值
                    var feeProportion = $(_this).closest('TR').find('td.feeProportion').attr('data-value');
                    var fee = $(_this).closest('TR').find('td.fee').attr('data-value');

                    $popover.find('input[name="feeProportion"]').val(feeProportion);
                    $popover.find('input[name="fee"]').val(fee);
                    that.bindFeeCalculation($popover);
                    that.saveCostFee_validate($popover);
                };
                options.onSave = function ($popover) {

                    var flag = $($popover).find('form').valid();
                    if (!flag) {
                        return false;
                    }else {

                        var $data = {};
                        $data.id = $(_this).attr('data-id');
                        $data.feeProportion = parseFloat($popover.find('input[name="feeProportion"]').val());
                        $data.fee = $popover.find('input[name="fee"]').val();

                        if(that.saveConstPoint($data,dataEditType)){
                            return false;
                        }
                    }
                };

                $(this).m_popover(options,true);
            });

            $(that.element).find('a[data-action="editContract"]').each(function () {
                var $this = $(this);
                var placeholder = $(this).attr('data-edit-type')==1?'描述':'金额';
                var dataEditType = $(this).attr('data-edit-type');
                var text = $(this).parent().attr('data-string');//当存在data-string 表示节点描述需要控制字数展示(此处多一个外元素包含)
                if(text==undefined){
                    text = $.trim($(this).closest('td').attr('data-value'));
                }
                if(!(dataEditType==2 || dataEditType==3)){
                    $(this).editable({//编辑
                        type: 'text',
                        title: '编辑',
                        emptytext:'0',
                        value:text==null||text==''||text===void 0?'':$.trim(text),
                        savenochange:true,
                        placeholder: placeholder,
                        closeClass:['.ui-popup-backdrop','.ui-popup'],
                        success: function(response, newValue,event) {
                            var $data = {};

                            $data.id = $(this).attr('data-id');

                            if(dataEditType==1){//节点名称
                                $data.feeDescription = newValue;
                            }else if(dataEditType==2){
                                $data.feeProportion = newValue;
                            }else if(dataEditType==6){
                                $data.paidFee = newValue;
                            }else{//节点金额、明细金额
                                $data.fee = newValue;
                            }
                            if(dataEditType==4 || dataEditType==6){
                                $data.pointId = $(this).attr('data-point-id');
                            }
                            if(that.saveConstPoint($data,dataEditType)){
                                return false;
                            }
                        },
                        display: function(value) {
                        },
                        validate: function(value) {

                            if($(this).attr('data-edit-type')==1){
                                if($.trim(value) == ''){
                                    return '请输入付款节点描述';
                                }else if($.trim(value).length>250){
                                    return '节点描述不超过250位!';
                                }
                            }else{
                                var errTips = '';
                                if($.trim(value) == ''){
                                    errTips = '付款金额不能为空!';
                                }else if(dataEditType==4){
                                    errTips = that.saveFee_validate(value,$this);
                                }
                                return errTips;
                            }
                        }
                    });
                }
            });
        }
        //编辑节点名称，金额，明细金额
        ,saveConstPoint:function (data,dataEditType) {
            var that = this,option  = {},isError = false;
            option.classId = that.element;
            if(dataEditType==4 || dataEditType==6){//费用明细
                option.url = restApi.url_saveReturnMoneyDetail;
            }else{//费用节点
                option.url = restApi.url_saveProjectCostPoint;
            }
            option.async = false;
            option.postData = data;
            option.postData.projectId = that.settings.projectId;
            option.postData.type = 1;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');

                    that.initHtml(1);

                }else {
                    S_dialog.error(response.info);
                    isError = true;
                }
            });
            return isError;
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;

            $(that.element).find('a[data-action]').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');
                var pointId = $this.closest('tr').attr('data-id');//节点ID
                var dataId = $this.attr('data-id');//当前元素赋予的ID
                //获取节点数据
                var dataItem = getObjectInArray(that._projectCost.pointList,pointId);
                //获取子节点数据
                var dataSubItem = dataItem==null?null: getObjectInArray(dataItem.pointDetailList,dataId);

                switch (dataAction) {
                    case 'addContract':
                        if(that._projectCost.totalCost==null || that._projectCost.totalCost<=0){
                            S_toastr.warning('请确定合同总金额！');
                            return false;
                        }
                        var options = {};
                        options.title = '添加付款节点';
                        options.projectId = that.settings.projectId;
                        options.totalCost = that._projectCost.totalCost;
                        options.costId = that._projectCost.costId;
                        options.saveCallBack = function () {
                            that.initHtml(1);
                        };
                        $('body').m_cost_addNode(options);
                        break;

                    case 'delCostPointDetail'://删除费用明细

                        S_dialog.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                            var option = {};
                            option.url = restApi.url_deleteProjectCostPointDetail+'/'+$this.attr('data-id');
                            m_ajax.get(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('删除成功！');
                                    that.initHtml(1);
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });

                        }, function () {
                        });

                        break;

                    case 'delCostPoint'://删除费用节点

                        S_dialog.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                            var option = {};
                            option.url = restApi.url_deleteProjectCostPoint+'/'+$this.attr('data-id');
                            m_ajax.get(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('删除成功！');
                                    that.initHtml(1);
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });

                        }, function () {
                        });
                        break;

                    case 'delPaidFee'://删除到账节点

                        S_dialog.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                            var option = {};
                            option.url = restApi.url_deleteProjectCostPaymentDetail+'/'+$this.attr('data-id');
                            m_ajax.get(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('删除成功！');
                                    that.initHtml(1);
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });

                        }, function () {
                        });
                        break;

                    case 'costConfirm':
                        var taskId = $this.attr('data-id');
                        $this.m_popover({
                            titleHtml: '<h3 class="popover-title">编辑</h3>',
                            placement: 'left',
                            content: template('m_cost/m_cost_addPaidFee', {doType:2}),
                            onShown: function ($popover) {
                                var fee = $this.closest('.footTools2').siblings('p[name="fee"]:first').html();
                                $popover.find('input[name="fee"]:first').val(fee);
                                var dateStr = getNowDate();
                                $popover.find('input[name="paidDate"]:first').val(dateStr);
                                $popover.find('.input-group-addon').on('click',function(e){
                                    $(this).prev('input').focus();
                                    stopPropagation(e);
                                });
                                that.saveCostFee_validate($popover);
                            },
                            onSave: function ($popover) {
                                var fee = $popover.find('input[name="fee"]:first').val();
                                var paidDate = $popover.find('input[name="paidDate"]:first').val();

                                var flag = $($popover).find('form').valid();
                                if (!flag) {
                                    return false;
                                }else {
                                    var option = {};
                                    option.url = restApi.url_handleMyTask;
                                    option.postData = {id: taskId, status: '1', result: fee,paidDate:paidDate};
                                    m_ajax.postJson(option, function (response) {
                                        if (response.code == '0') {
                                            S_toastr.success('操作成功');
                                            that.initHtml(1);
                                        } else {
                                            S_dialog.error(response.info);
                                        }
                                    });
                                }
                            }
                        }, true);
                        return false;
                        break;

                    case 'paymentRequest'://付款申请(内部)
                        var option = {};
                        option.projectId = that.settings.projectId;
                        option.pointInfo = {
                            companyName:that._projectCost.companyName,//收款方
                            costId:that._projectCost.costId,
                            isInnerCompany : that._projectCost.isInnerCompany,
                            fee:dataItem.fee,
                            feeDescription:dataItem.feeDescription,
                            pointId:pointId,
                            subId :dataSubItem.id,
                            subFee:dataSubItem.fee,
                            userName:dataSubItem.userName,
                            backFee : dataItem.backFee
                        };
                        option.saveCallBack = function () {
                            that.initHtml(1);
                        };
                        $('body').m_cost_paymentApplication(option,true);
                        break;

                    case 'paymentRequest2'://付款申请(外部)
                        var option = {};
                        option.projectId = that.settings.projectId;
                        option.pointInfo = {
                            companyName:that._projectCost.companyName,//收款方
                            costId:that._projectCost.costId,
                            isInnerCompany : that._projectCost.isInnerCompany,
                            fee:dataItem.fee,
                            feeDescription:dataItem.feeDescription,
                            pointId:pointId,
                            backFee : dataItem.backFee
                        };
                        option.saveCallBack = function () {
                            that.initHtml(1);
                        };
                        $('body').m_cost_paymentApplication(option,true);
                        break;

                    case 'viewPaymentDetails'://付款详情

                        var option = {};
                        option.pointDetailId = dataId;
                        $('body').m_cost_paymentDetails(option,true);
                        break;
                    case 'delAttach'://删除附件

                        S_dialog.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                            var option = {};
                            option.url = restApi.url_netFile_delete;
                            option.postData = {
                                id: dataId,
                                accountId: window.currentUserId
                            };
                            m_ajax.postJson(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('删除成功！');
                                    that.initHtml(1);
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });

                        }, function () {
                        });
                        break;
                }

            });
        }
        //当存在到账节点时，修改明细金额需进行验证
        ,saveFee_validate:function(value,$obj){
            var that = this,errTips='';
            var maxLimit = $.trim($obj.closest('tr').attr('data-fee'))-0;
            var hasFee = ($obj.closest('tr').attr('data-backFee')-0).sub($.trim($obj.closest('td').attr('data-value'))-0);
            var minLimit = 0;
            var pnumber = regularExpressions.pnumber;
            value = $.trim(value)-0;
            if( value<=0){
               return '请输入大于0的数字';
            }
            if(parseInt(value).toString().length>10){
                return '对不起，您的操作超出了系统允许的范围。合同总金额的单位为“万元”'
            }
            if(!pnumber.test(value-0)){
                return '请保留小数点后六位!';
            }

            $obj.closest('tr').find('div.bPaidFee').each(function(){
                minLimit += ($(this).find('span').text()-0);
            });
            if(!(value>=minLimit && (value.add(hasFee))<=maxLimit)){
                if(minLimit==0){
                    errTips  = '请输入大于0且不超过'+maxLimit.sub(hasFee)+'的数字!'
                }else{
                    errTips = '请输入区间为'+minLimit+'到'+maxLimit.sub(hasFee)+'的数字!'
                }
            }
            return errTips;

        }
        //金额比例验证
        ,saveCostFee_validate:function(ele){
            var that = this;

            //flag==true验证付款明细，flag==false验证节点金额编辑
            var flag = $(ele).closest('.popover').prev('a[data-action="costConfirm"]').length>0;
            var limit=$(ele).closest('tr').attr('data-backfee');
            var limit0=(ele).prev().attr('data-limit');
            $(ele).find('form').validate({
                rules: {
                    feeProportion:{
                        required:true,
                        number:true,
                        limitProportion:true,
                        pointNumber:true
                    },
                    fee:{
                        required:true,
                        number:true,
                        minNumber:true,
                        feeLimit:flag,
                        ckFee:true,//验证数字
                        over10:true,//整数部分是否超过10位
                        ckFee2:!flag,//验证是否超过合同总金额
                        notReachFee:!flag//验证是否超过合同总金额
                    },
                    paidDate:{
                        required:true
                    }

                },
                messages: {
                    feeProportion:{
                        required:'请输入比例!',
                        number:'请输入有效数字',
                        limitProportion:'请输入0到100之间的数字!',
                        pointNumber:'请保留小数点后两位!'
                    },
                    fee:{
                        required:'请输入金额!',
                        number:'请输入有效数字',
                        minNumber:'请输入大于0的数字!',
                        feeLimit:'付款金额不能超过'+limit0+'!',
                        over10:'对不起，您的操作超出了系统允许的范围。合同总金额的单位为“万元”',
                        ckFee:'请保留小数点后六位!',
                        ckFee2:'节点金额不能超过合同总金额!',
                        notReachFee:'付款金额不能小于已申请金额（'+limit+'）!'
                    },
                    paidDate:{
                        required:'请选择付款日期!'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    if ($(element).attr('data-action')=='feeCalculation') {
                        error.appendTo(element.closest('.form-group'));
                    }
                    else {
                        error.appendTo(element.closest('.col-24-md-16'));
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
            $.validator.addMethod('feeLimit', function(value, element) {
                var $plus_T = $(element).closest('.popover').prev('a[data-action="costConfirm"]');
                var maxLimit = $(ele).prev('a[data-action="costConfirm"]').attr("data-limit")-0;
                value = $.trim(value);
                var isOk = true;
                if(!(value<=maxLimit)){
                    isOk = false;
                }
                return  isOk;
            }, '数字不能超过发起收款金额!');
            $.validator.addMethod('ckFee', function(value, element) {
                var isOk = true;
                if(!regularExpressions.pnumber.test(value)){
                    isOk = false;
                }
                return  isOk;

            }, '请保留小数点后六位!');
            $.validator.addMethod('over10', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if(parseInt(value).toString().length>10){
                    isOk = false;
                }
                return  isOk;

            }, '对不起，您的操作超出了系统允许的范围。合同总金额的单位为“万元”');
            $.validator.addMethod('ckFee2', function(value, element) {

                return  that.validator_ckFee(value);
            }, '金额不能超过合同总金额!');
            $.validator.addMethod('notReachFee', function(value, element) {
                value = value-0;
                var backFee = $(element).closest('tr').attr('data-backFee')-0;
                if(value<backFee){
                    return false;
                }else{
                    return true;
                }

            }, '输入金额不能小于发起收款金额!');
        }
        //金额验证
        ,validator_ckFee:function (value) {
            var that = this;
            var totalCost = that._projectCost.totalCost;
            value = $.trim(value);
            var isOk = true;
            var total = totalCost-0;
            if(value>total){
                isOk = false;
            }
            return isOk;
        }

        //金额比例计算
        ,bindFeeCalculation:function (ele) {
            var that = this;
            $(ele).find('input[data-action="feeCalculation"]').keyup(function () {

                var name = $(this).attr('name');
                var val = $(this).val();
                var total = that._projectCost.totalCost;
                if(name=='feeProportion'){//比例
                    var fee = (val-0)*(total-0)/100;
                    fee = decimal(fee,7);
                    fee = parseFloat(decimal(fee,6));
                    $(ele).find('input[name="fee"]').val(fee);
                }
                if(name=='fee'){//金额
                    var proportion = (val-0)/(total-0)*100;
                    proportion = decimal(proportion,3);
                    proportion = decimal(proportion,2);
                    $(ele).find('input[name="feeProportion"]').val(proportion);
                }

            });
        }
        //上传附件
        ,uploadRecordFile: function () {
            var that = this;
            $(that.element).find('a[data-action="recordAttach"]').m_fileUploader({
                server: restApi.url_attachment_uploadCostPlanAttach,
                fileExts: 'pdf',
                fileSingleSizeLimit:20*1024*1024,
                formData: {},
                multiple:true,
                //duplicate:false,
                loadingId: that.element,
                innerHTML: '<i class="fa fa-upload fa-fixed"></i>',
                uploadBeforeSend: function (object, data, headers) {
                    data.companyId = window.currentCompanyId;
                    data.accountId = window.currentUserId;
                    data.projectId = that.settings.projectId;
                    data.targetId = that._projectCost.costId;

                },
                uploadSuccessCallback: function (file, response) {
                    //console.log(response)
                    S_toastr.success('上传成功！');
                    that.initHtml(1);
                }
            },true);
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
