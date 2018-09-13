/**
 * 收支总览-利润报表
 * Created by wrb on 2017/11/30.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_profitStatement",
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
        this._selectedOrg = null;//当前组织筛选-选中组织对象
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData();
        }
        //初始化数据并加载模板
        ,initHtmlData:function () {
            var that = this;
            var html = template('m_payments/m_payments_profitStatement',{currentYear:new Date().getFullYear()});
            $(that.element).html(html);
            var option = {};
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                that.renderProfitList();
            };
            option.$renderCallBack = function () {
                that.bindDateAction();
                that.bindRefreshBtn();
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);
        }
        //渲染列表
        ,renderProfitList:function () {
            var that = this;
            var option = {};
            option.url = restApi.url_getProfitDetail;
            option.postData = {};
            option.postData.combineCompanyId=that._selectedOrg.id;
            var selectedDate = $(that.element).find('#ipt_date').val();
            if(selectedDate!=''){
                selectedDate=selectedDate.substring(0,4);
                option.postData.date=selectedDate;
            }else{
                selectedDate=new data().getFullYear();
                option.postData.date=new data().getFullYear();
            }
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    var html = template('m_payments/m_payments_profitStatement_list',{
                        profitData:response.data,
                        selectedDate:selectedDate
                    });
                    $(that.element).find('#profitBox').html(html);
                    that.bindExpandFeeItem();

                } else {
                    S_layer.error(response.info);
                }
            });
        }
        //展开子项
        , bindExpandFeeItem:function () {

            var that = this;
            $(that.element).find('a[data-action="expand"]').on('click',function () {
                var $this = $(this);
                var dataType = $this.attr('data-type');

                if($this.find('i').hasClass('fa-angle-right')){
                    $this.find('i').removeClass('fa-angle-right').addClass('fa-angle-down');
                    $this.parents('table').find('tr[data-type="'+dataType+'"]').show();
                }else{
                    $this.find('i').removeClass('fa-angle-down').addClass('fa-angle-right');
                    $this.parents('table').find('tr[data-type="'+dataType+'"]').hide();
                }

            });
        }
        //时间事件
        , bindDateAction:function () {

            var that = this;
            $(that.element).find('a[data-action="setTime"]').on('click',function () {
                var $this = $(this);
                var dataType = $this.attr('data-type');

                if(dataType=='0'){
                    $(that.element).find('#ipt_date').val(new Date().getFullYear()+'年');
                }else{
                    $(that.element).find('#ipt_date').val((new Date().getFullYear())-1+'年');
                }
                that.renderProfitList();
            });
            $(that.element).find('input[id="ipt_date"]').off('click').on('click',function () {

                var endTime = (new Date().getFullYear());
                var onpicked =function(dp){

                    that.renderProfitList();

                };
                WdatePicker({el:this,maxDate:endTime,dateFmt:'yyyy年',isShowClear:false,onpicked:onpicked})
            });
            $(that.element).find('i.fa-calendar').off('click').on('click',function () {
                $(this).closest('.input-group').find('input').click();
            });
        }
        , bindRefreshBtn:function () {
            var that = this;
            $(that.element).find('button[data-action="refreshBtn"]').on("click", function (e) {

                that.initHtmlData();
                return false;
            })
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
