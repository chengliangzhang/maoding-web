/**
 * 收支总览－收支明细-应付
 * Created by wrb on 2017/11/30.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_payable",
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
        this._companyList = null;//组织列表
        this._companyListBySelect = null;//筛选组织
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
            var html = template('m_payments/m_payments_payable',{});
            $(that.element).html(html);
            var option = {};
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                that.renderPayableList();
            };
            option.$renderCallBack = function () {
                that.bindSetTime();
                that.bindChoseTime();
                that.bindRefreshBtn();
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);
        }
        //渲染台账list
        ,renderPayableList:function () {
            var that = this;

            var option = {};
            option.param = {};
            option.param.paymentId = that._selectedOrg.id;
            var startDate = $(that.element).find('#ipt_startTime').val();
            var endDate = $(that.element).find('#ipt_endTime').val();
            if(startDate!=''){
                option.param.startDate=startDate;
            }
            if(endDate!=''){
                option.param.endDate=endDate;
            }

            option.param.feeType=$(that.element).find('input[name="feeType"]').val();
            option.param.associatedOrg=$(that.element).find('input[name="associatedOrg"]').val();
            option.param.projectName=$(that.element).find('input[name="projectName"]').val();

            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-box',
                url: restApi.url_getPayment,
                params: option.param
            }, function (response) {

                if (response.code == '0') {

                    that._companyListBySelect = response.data.organization;
                    var html = template('m_payments/m_payments_payable_list',{
                        dataList:response.data.data,
                        paymentSum:response.data.paymentSum
                    });
                    $(that.element).find('.data-list-container').html(html);
                    that.bindViewDetail();
                    that.bindGoExpensesPage();
                    that.filterHover();
                    that.filterActionClick();

                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //快捷时间
        , bindSetTime: function () {
            var that = this;
            $(that.element).find('a[data-action="setTime"]').click(function () {
                var days = $(this).attr('data-days');
                var endTime = getNowDate();
                var startTime = '';//moment(endTime).subtract(days, 'days').format('YYYY-MM-DD');

                if (endTime != null && endTime.indexOf('-') > -1) {

                    var month = endTime.substring(5, 7) - 0;//当前月份

                    if (days == 30) {//一个月

                        startTime = endTime.substring(0, 8) + '01';

                    } else if (days == 90) {//一季度

                        if (month >= 1 && month <= 3) {//第一季度
                            startTime = endTime.substring(0, 5) + '01-01';
                        } else if (month >= 4 && month <= 6) {//第二季度
                            startTime = endTime.substring(0, 5) + '04-01';
                        } else if (month >= 7 && month <= 9) {//第三季度
                            startTime = endTime.substring(0, 5) + '07-01';
                        } else if (month >= 10 && month <= 12) {//第四季度
                            startTime = endTime.substring(0, 5) + '10-01';
                        }

                    } else if (days == 180) {//半年

                        if (month >= 1 && month <= 6) {//前半年
                            startTime = endTime.substring(0, 5) + '01-01';
                        } else if (month >= 7 && month <= 12) {//后半年
                            startTime = endTime.substring(0, 5) + '07-01';
                        }

                    } else if (days == 360) {//一年

                        startTime = endTime.substring(0, 5) + '01-01';
                    }

                }

                $('#ipt_startTime').val(startTime);
                $('#ipt_endTime').val(endTime);
                that.renderPayableList();
                $(this).blur();
            });
        }
        //时间绑定事件
        , bindChoseTime:function () {
            var that = this;
            $(that.element).find('input[name="startTime"]').off('click').on('click',function () {

                var endTime = $(that.element).find('input[name="endTime"]').val();
                var onpicked =function(dp){

                    that.renderPayableList();

                };
                WdatePicker({el:this,maxDate:endTime,onpicked:onpicked})
            });
            $(that.element).find('input[name="endTime"]').off('click').on('click',function () {

                var startTime = $(that.element).find('input[name="startTime"]').val();
                var onpicked =function(dp){

                    that.renderPayableList();

                };
                WdatePicker({el:this,minDate:startTime,onpicked:onpicked})
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
        //查看详情
        ,bindViewDetail:function () {
            var that = this;
            $(that.element).find('a[data-action="viewDetail"]').on('click',function () {
                var option = {};
                option.$title = '应付详情';
                option.$id = $(this).attr('data-id');
                option.$type = 2;
                $('body').m_payments_list_detail(option);
            });
        }
        //跳转到收支管理
        ,bindGoExpensesPage:function () {
            var that = this;
            $(that.element).find('a[data-action="goExpensesPage"]').off('click').on('click',function () {
                var projectId = $(this).attr('data-project-id');
                var projectName = $(this).text();
                var type = $(this).attr('data-type');
                location.hash = '/projectDetails/incomeExpenditure?type='+type+'&id='+projectId+'&projectName='+encodeURI(projectName);
                return false;
            });
        }
        //筛选hover事件
        ,filterHover:function () {
            var that =  this;
            $(that.element).find('.data-list-box  th').hover(function () {
                if( $(this).find(' .icon-filter i').hasClass('icon-shaixuan') && !$(this).find(' .icon-filter i').hasClass('fc-v1-blue')){
                    $(this).find(' .icon-filter').show();
                }
            },function () {
                if( $(this).find(' .icon-filter i').hasClass('icon-shaixuan') && !$(this).find(' .icon-filter i').hasClass('fc-v1-blue')) {
                    $(this).find(' .icon-filter').hide();
                }
            });
        }
        //筛选事件
        ,filterActionClick:function () {
            var that = this;
            $(that.element).find('a.icon-filter').each(function () {

                var $this = $(this);
                var id = $this.attr('id');
                switch (id){
                    case 'filterFeeType'://收支类型
                    case 'filterAssociatedOrg'://关联组织

                        var currCheckValue = '',selectList = [];
                        if(id=='filterFeeType'){
                            currCheckValue = $(that.element).find('input[name="feeType"]').val()
                            selectList = [
                                {fieldName:'全部',fieldValue:''},
                                //{fieldName:'合同回款',fieldValue:'1'},
                                {fieldName:'技术审查费',fieldValue:'2'},
                                {fieldName:'合作设计费',fieldValue:'3'},
                                {fieldName:'其他收支',fieldValue:'4'}
                            ]
                        }else if(id=='filterAssociatedOrg'){
                            currCheckValue = $(that.element).find('input[name="associatedOrg"]').val()
                            selectList.push({fieldName:'全部',fieldValue:''});
                            /*if(that._companyList!=null && that._companyList.length>0){
                                $.each(that._companyList, function (i, item) {
                                    selectList.push({fieldValue: item.id, fieldName: item.companyName});
                                });
                            }*/
                            if(that._companyListBySelect!=null && Object.getOwnPropertyNames(that._companyListBySelect).length>0){
                                $.each(that._companyListBySelect, function (key, value) {
                                    selectList.push({fieldValue: key, fieldName: value});
                                });
                            }
                        }
                        if(currCheckValue!=''){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }
                        $this.on('click',function () {
                            S_dialog.dialog({
                                contentEle: 'dialogOBox',
                                ele:id,
                                lock: 2,
                                align: 'bottom right',
                                quickClose:true,
                                noTriangle:true,
                                width: '180',
                                height:'195',
                                tPadding: '0px',
                                url: rootPath+'/assets/module/m_common/m_dialog.html'

                            },function(d){//加载html后触发

                                var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';

                                var iHtml = template('m_filterableField/m_filter_select',{
                                    currCheckValue:currCheckValue,
                                    selectList:selectList
                                });
                                $(dialogEle).html(iHtml);
                                $(dialogEle).find('.dropdown-menu a').on('click',function () {

                                    var val = $(this).attr('data-state-no');
                                    if(id=='filterFeeType'){
                                        $(that.element).find('input[name="feeType"]').val(val);
                                    }else if(id=='filterAssociatedOrg'){
                                        $(that.element).find('input[name="associatedOrg"]').val(val);
                                    }
                                    that.renderPayableList();
                                    S_dialog.close($(dialogEle));
                                });
                            });
                        });

                        break;
                    case 'filterProjectName': //项目
                        var txtVal = '',placeholder='';
                        if(id=='filterProjectName'){
                            txtVal = $('input[name="projectName"]').val();
                            placeholder = '请输入项目名称';
                        }

                        if(txtVal!=''){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }
                        $this.on('click',function () {
                            S_dialog.dialog({
                                contentEle: 'dialogOBox',
                                ele:id,
                                lock: 2,
                                align: 'bottom right',
                                quickClose:true,
                                noTriangle:true,
                                width: '220',
                                minHeight:'100',
                                tPadding: '0px',
                                url: rootPath+'/assets/module/m_common/m_dialog.html'


                            },function(d){//加载html后触发

                                var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';


                                var iHtml = template('m_filterableField/m_filter_input',{
                                    txtVal:txtVal,
                                    placeholder:placeholder
                                });

                                $(dialogEle).html(iHtml);
                                $(dialogEle).find('button[data-action="sureFilter"]').on('click',function () {
                                    var val = $(dialogEle).find('input[name="txtVal"]').val();

                                    if(id=='filterProjectName'){
                                        $(that.element).find('input[name="projectName"]').val(val);
                                    }
                                    that.renderPayableList();

                                    S_dialog.close($(dialogEle));
                                });

                            });
                        });

                        break;
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
