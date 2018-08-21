/**
 * 财务设置－团队基础财务数据设置
 * Created by wrb on 2018/5/23.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_finance_basic_settings",
        defaults = {
            $contentEle:null,
            $isFirstEnter:false//是否是第一次進來
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderPage();
        }
        //初始化数据并加载模板
        ,renderPage:function () {
            var that = this;

            var option  = {};
            option.classId= '#content-box';
            option.url = restApi.url_getCompanyBalance;
            option.postData = {

            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){

                    var html = template('m_finance/m_finance_basic_settings',{companyDataList:response.data});
                    $(that.element).html(html);
                    that.bindEditable();

                }else {
                    S_dialog.error(response.info);
                }
            });
        }
        , bindEditable:function () {
            var that = this;
            $(that.element).find('a[data-action="xEditable"]').each(function () {
                var $this = $(this);
                var $i = $this.closest('TR').attr('data-i');
                var companyId = $this.closest('TR').attr('data-company-id');
                var dataActionType = $this.attr('data-action-type');
                var dataValue= $this.attr('data-value');
                $this.editable({//编辑
                    type: 'text',
                    mode: 'inline',
                    emptytext:'',
                    savenochange:true,
                    placeholder: '金额',
                    value: dataValue,
                    success: function(response, newValue) {
                        var $data = {};
                        $data.companyId = companyId;
                        $data.type = dataActionType;
                        $data[dataActionType] = newValue;
                        if(that.saveCompanyBalance($data)){
                            return false;
                        }
                    },
                    display: function(value, sourceData) {},
                    validate: function(value) {
                        var errTips = '';
                        var number = regularExpressions.number;
                        var numberWithPoints = regularExpressions.numberWithPoints_2;
                        if(value!=''){
                            if(!number.test(value)){
                                errTips = '请输入有效数字!';
                            }else if(!numberWithPoints.test(value)){
                                errTips = '请保留小数点后两位!';
                            }else if(parseInt(value).toString().length>32){
                                errTips = '对不起，您的操作超出了系统允许的范围。'
                            }
                        }
                        return errTips;
                    }
                });
            });
            $(that.element).find('a[data-action="editDate"]').off('click').on('click',function () {
                var $this = $(this);
                var $i = $this.closest('TR').attr('data-i');
                var companyId = $this.closest('TR').attr('data-company-id');
                var options = {};
                options.$title = '设置余额初始日期';
                options.$placement = 'right';
                options.$eleId = 'a[data-action="editDate"][data-action-type="'+$i+'"]';
                options.$isClear = false;
                options.$okCallBack = function (data) {
                    if(data==null){
                        data = '';
                    }
                    var $data = {};
                    $data.companyId = companyId;
                    $data.type = 'setBalanceDate';
                    $data.setBalanceDate = data;
                    if(that.saveCompanyBalance($data)){
                        return false;
                    }
                };
                $this.m_quickDatePicker(options);
            });
        }
        //保存
        , saveCompanyBalance:function ($data) {
            var that = this;
            var option  = {},isError = false;
            option.classId= '#content-box';
            option.async = false;
            option.url = restApi.url_saveCompanyBalance;
            option.postData = $data;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功');
                    that.renderPage();

                }else {
                    S_dialog.error(response.info);
                    isError = true;
                }
            });
            return isError;
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
