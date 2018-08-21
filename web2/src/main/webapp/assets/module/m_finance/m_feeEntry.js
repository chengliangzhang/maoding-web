/**
 * 费用录入
 * Created by wrb on 2017/11/29.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_feeEntry",
        defaults = {
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentExpFixedData = null;//当前费用数据
        this._selectedOrg = null;//当前组织筛选-选中组织对象
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_finance/m_feeEntry',{});
            $(that.element).html(html);
            var option = {};
            option.$type = 1;
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                that.renderMonthList(null);
            };
            option.$renderCallBack = function () {
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);

        }
        //根据月份渲染费用数据
        , renderFeeEntry:function (expDate) {
            var that = this;
            var option  = {};
            if(expDate==''){
                var dateStr = getNowDate();
                expDate = moment(dateStr).format('YYYY/MM');
            }
            var companyId = that._selectedOrg.id;
            option.url = restApi.url_getExpFixedByExpDate+'/'+expDate+'/'+companyId;
            m_ajax.get(option,function (response) {
                if(response.code=='0'){
                    that._currentExpFixedData = response.data;
                    var html = template('m_finance/m_feeEntry_content',{expFixedData:response.data});
                    $(that.element).find('#right-box').html(html);
                    that.bindSaveExpFixed();
                    that.saveFee_validate();
                    that.leftBoxHeightResize();
                    $(window).resize(function () {
                        var t = setTimeout(function () {
                            that.leftBoxHeightResize();
                            clearTimeout(t);
                        });
                    });
                }else {
                    S_dialog.error(response.info);
                }
            });

            //WdatePicker({eCont:'div1',dateFmt:'yyyy',onpicked:function(dp){alert('你选择的日期是:'+dp.cal.getDateStr())}})
        }
        //渲染月份
        , renderMonthList:function (year,expDate) {
            var that = this;
            var option  = {};
            var dateStr = new Date().getFullYear();
            if(year==null){
                year = dateStr;
            }
            var companyId = that._selectedOrg.id;
            option.url = restApi.url_getExpAmountByYear+'/'+year+'/'+companyId;
            m_ajax.get(option,function (response) {
                if(response.code=='0'){

                    if(expDate==null && response.data!=null && response.data.length>0){
                        expDate = response.data[0].expDate;
                    }
                    var html = template('m_finance/m_feeEntry_monthList',{
                        monthList:response.data,
                        currentYear :dateStr,
                        currentExpDate:expDate,
                        selectedYear:year
                    });
                    $(that.element).find('#left-box').html(html);
                    that.renderFeeEntry(expDate);
                    that.bindGetExpFixed();
                    that.bindYearSwitch();


                }else {
                    S_dialog.error(response.info);
                }
            });
        }
        //树菜单高度自适应
        , leftBoxHeightResize:function () {
            var that = this;
            var pageWrapperMH = $('#page-wrapper').css('min-height');
            var pageWrapperH = $('#page-wrapper').height();
            if(pageWrapperH-110>parseInt(pageWrapperMH)){
                $(that.element).find('#left-box').css('height',(pageWrapperH-110)+'px');
            }else{
                $(that.element).find('#left-box').css('height',pageWrapperMH);
            }

        }
        // 绑定保存费用金额
        , bindSaveExpFixed:function () {
            var that = this;
            $(that.element).find('#right-box').find('a[data-action="saveExpFixed"]').off('click').on('click',function () {


                var flag = $(that.element).find('form#expFixedForm').valid();
                if (!flag) {
                    return false;
                }else {
                    var option  = {};
                    option.classId = '#content-right';
                    option.postData = {};
                    option.postData.expDate = that._currentExpFixedData.expDate;
                    option.postData.fixedList = [];

                    $(that.element).find('#right-box').find('input[name="expAmount"]').each(function () {

                        var $this = $(this);
                        var id = $this.attr('id');
                        var expType = $this.attr('data-exptype');
                        var val = $this.val();
                        option.postData.fixedList.push({
                            id:id==''?null:id,
                            expType:expType,
                            expAmount:val,
                            expTypeParentName:$this.attr('data-parent-name'),
                            expTypeName:$this.attr('data-name'),
                            seq:$this.attr('data-seq')
                        })

                    });
                    option.postData.companyId = that._selectedOrg.id;
                    option.url = restApi.url_saveExpFixedByExpDate;
                    m_ajax.postJson(option,function (response) {
                        if(response.code=='0'){
                            S_toastr.success('保存成功');

                            var year = that._currentExpFixedData.expDate;
                            year = year.substring(0,4);
                            that.renderMonthList(year,that._currentExpFixedData.expDate);
                        }else {
                            S_dialog.error(response.info);
                        }
                    });
                }
            });
        }
        //根据月份查费用数据
        , bindGetExpFixed:function () {

            var that = this;
            $(that.element).find('#left-box div[data-action="getExpFixed"]').on('click',function () {
                var expDate = $(this).attr('data-expdate');

                that.renderFeeEntry(expDate);
                $(this).addClass('active').siblings('div').removeClass('active');

            });
        }
        //年份切换事件
        , bindYearSwitch:function () {
            var that = this;
            $(that.element).find('#left-box a[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                var selectedYear = $this.closest('DIV').find('span[data-type="selectedYear"]').text();
                switch (dataAction){
                    case 'prevYear':
                        that.renderMonthList(selectedYear-0-1);
                        break;
                    case 'nextYear':
                        that.renderMonthList(selectedYear-0+1);
                        break;
                }
            })
        }
        //金额比例验证
        , saveFee_validate:function(){
            var that = this;
            $(that.element).find('form#expFixedForm').validate({
                rules: {
                    expAmount:{
                        number:true,
                        ckFee:true,//验证数字
                        over10:true//整数部分是否超过32位
                    }
                },
                messages: {
                    expAmount:{
                        number:'请输入有效数字',
                        minNumber:'请输入大于0的数字!',
                        over10:'对不起，您的操作超出了系统允许的范围。合同总金额的单位为“万元”',
                        ckFee:'请保留小数点后两位!'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置

                    error.appendTo(element.closest('td'));
                }
            });
            $.validator.addMethod('ckFee', function(value, element) {
                var isOk = true;
                if(value!='' && !regularExpressions.numberWithPoints_2.test(value)){
                    isOk = false;
                }
                return  isOk;

            }, '请保留小数点后两位!');
            $.validator.addMethod('over10', function(value, element) {
                value = $.trim(value);
                var isOk = true;
                if(parseInt(value).toString().length>32){
                    isOk = false;
                }
                return  isOk;

            }, '对不起，您的操作超出了系统允许的范围。合同总金额的单位为“元”');
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
