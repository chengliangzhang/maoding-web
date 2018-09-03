/**
 * 收支总览-分类统计
 * Created by wrb on 2017/11/30.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_statistics",
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
        this._companyList = [];
        this._companyIdList = [];
        this._selectedOrg = null;//当前组织筛选-选中组织对象
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {

            var that = this;
            var html = template('m_payments/m_payments_statistics',{});
            $(that.element).html(html);
            var option = {};
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                that.renderCategoryTypeList();
            };
            option.$renderCallBack = function () {
                that.bindSetTime();
                that.bindRefreshBtn();
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);

        }
        //初始ICheck
        , initItemICheck:function () {
            var that = this;
            var ifChecked = function (e) {
                var dataId = $(this).attr('data-id');
                var dataPid = $(this).attr('data-pid');

                if(dataPid==''){//根目录
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').prop('checked',true);
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').iCheck('update');

                }else{
                    var childLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataPid+'"]').length;
                    var childCheckedLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataPid+'"]:checked').length;
                    if(childLen==childCheckedLen){
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').prop('checked',true);
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').iCheck('update');
                    }else{
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').prop('checked',false);
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').iCheck('update');
                    }
                }
                that.renderBarChart();
            };
            var ifUnchecked = function (e) {
                var dataId = $(this).attr('data-id');
                var dataPid = $(this).attr('data-pid');

                if(dataPid==''){//根目录
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').prop('checked',false);
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').iCheck('update');
                }else{

                    $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').prop('checked',false);
                    $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').iCheck('update');
                }
                that.renderBarChart();
            };
            $(that.element).find('input[name="itemCk"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);
        }
        //初始化分类统计范围
        ,renderCategoryTypeList:function () {
            var that = this;
            var option = {};
            option.url = restApi.url_getCategoryTypeList;
            option.postData = {};
            option.postData.companyId = that._selectedOrg.id;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    var html = template('m_payments/m_payments_statistics_categoryType',{
                        categoryTypeList:response.data
                    });
                    $(that.element).find('#categoryTypeBox').html(html);
                    that.initItemICheck();
                    that.renderBarChart();

                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //请求barChart数据
        ,renderBarChart:function () {
            var that = this;

            var option = {};
            option.url = restApi.url_getStatisticClassicData;

            option.postData={};
            if(that._selectedOrg==null){
                option.postData.combineCompanyId=that._companyList.id;
            }else{
                option.postData.combineCompanyId=that._selectedOrg.id;
            }

            if($(that.element).find('a[data-action="setTime"][data-type="month"]').hasClass('btn-primary')){
                option.postData.groupByTime = 1;
            }else{
                option.postData.groupByTime = 2;
            }
            option.postData.startDateStr=$(that.element).find('input[name="timeStart"]').val();
            option.postData.endDateStr=$(that.element).find('input[name="timeEnd"]').val();
            option.postData.feeTypeList  = [];
            $(that.element).find('input[name="itemCk"]:checked').each(function () {
                option.postData.feeTypeList .push($(this).attr('data-value'));
            });
           
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    if(response.data!=null){

                        var html = template('m_payments/m_payments_statistics_barChart',{});
                        $(that.element).find('#barChartBox').html(html);

                        var startDateStr = response.data.startDateStr;
                        var endDateStr = response.data.endDateStr;
                        if(startDateStr!=null && startDateStr!=''){
                            if(startDateStr.length>7){
                                startDateStr = startDateStr.substring(0,7);
                            }
                            $(that.element).find('input[name="timeStart"]').val(startDateStr);
                        }
                        if(endDateStr!=null && endDateStr!=''){
                            if(endDateStr.length>7){
                                endDateStr = endDateStr.substring(0,7);
                            }
                            $(that.element).find('input[name="timeEnd"]').val(endDateStr);
                        }

                        if(response.data.columnarDataForTimeGroup!=null){
                            that.renderBarChartItem(response.data.columnarDataForTimeGroup,'barChart1');
                        }
                        if(response.data.columnarDataForOrgGroup!=null){
                            that.renderBarChartItem(response.data.columnarDataForOrgGroup,'barChart2');
                        }
                    }
                } else {
                    S_dialog.error(response.info);
                }
            });


        }
        //生成barChart
        ,renderBarChartItem:function (barData,id) {
            var that = this;
            /*barData = {
                            labels: ["卯丁设计有限公司", "华丽设计工作室", "诺明设计事务所", "华丽丽工作室", "亨利来设计有限公司四川分公司"],
                            datasets: [
                                {
                                    label: "总收入",
                                    backgroundColor: '#4765a1',
                                    borderColor: "rgba(26,179,148,0.7)",
                                    pointBackgroundColor: "rgba(26,179,148,1)",
                                    pointBorderColor: "#fff",
                                    fill: false,
                                    data:[65, 59, 80, 81, 56]
                                },
                                {
                                    label: "总支出",
                                    backgroundColor: '#449fae',
                                    borderColor: "rgba(26,179,148,0.7)",
                                    pointBackgroundColor: "rgba(26,179,148,1)",
                                    pointBorderColor: "#fff",
                                    fill: false,
                                    data: [65, 59, 80, 81, 56]
                                }
                            ]
                        };*/

            // Define a plugin to provide data labels
            /*Chart.plugins.register({
                afterDatasetsDraw: function(chart) {
                    var ctx = chart.ctx;

                    chart.data.datasets.forEach(function(dataset, i) {
                        var meta = chart.getDatasetMeta(i);
                        if (!meta.hidden) {
                            meta.data.forEach(function(element, index) {
                                // Draw the text in black, with the specified font
                                ctx.fillStyle = 'rgb(0, 0, 0)';

                                var fontSize = 16;
                                var fontStyle = 'normal';
                                var fontFamily = 'Helvetica Neue';
                                ctx.font = Chart.helpers.fontString(fontSize, fontStyle, fontFamily);

                                // Just naively convert to string for now
                                var dataString = dataset.data[index];
                                dataString = expNumberFilter(dataString).toString();

                                // Make sure alignment settings are correct
                                ctx.textAlign = 'center';
                                ctx.textBaseline = 'middle';

                                var padding = 5;
                                var position = element.tooltipPosition();
                                ctx.fillText(dataString, position.x, position.y - (fontSize / 2) - padding);
                            });
                        }
                    });
                }
            });*/

            var ctx = document.getElementById(id).getContext('2d');
            window.myBar = new Chart(ctx, {
                type: 'bar',
                data: barData,
                options: {
                    responsive: true,
                    title: {
                        display: true,
                        text: ''
                    },
                    tooltips: {
                        enabled: false,
                        custom: function(tooltip) {
                            // Tooltip Element
                            var tooltipEl = document.getElementById('chartjs-tooltip-'+id);

                            if (!tooltipEl) {
                                tooltipEl = document.createElement('div');
                                tooltipEl.id = 'chartjs-tooltip-'+id;
                                tooltipEl.innerHTML = "<table></table>";
                                this._chart.canvas.parentNode.appendChild(tooltipEl);
                            }

                            // Hide if no tooltip
                            if (tooltip.opacity === 0) {
                                tooltipEl.style.opacity = 0;
                                return;
                            }

                            // Set caret Position
                            tooltipEl.classList.remove('above', 'below', 'no-transform');
                            if (tooltip.yAlign) {
                                tooltipEl.classList.add(tooltip.yAlign);
                            } else {
                                tooltipEl.classList.add('no-transform');
                            }

                            function getBody(bodyItem) {
                                return bodyItem.lines;
                            }

                            // Set Text
                            if (tooltip.body) {
                                var titleLines = tooltip.title || [];
                                var bodyLines = tooltip.body.map(getBody);

                                var innerHtml = '<thead>';

                                titleLines.forEach(function(title) {
                                    innerHtml += '<tr><th>' + title + '</th></tr>';
                                });
                                innerHtml += '</thead><tbody>';

                                bodyLines.forEach(function(body, i) {
                                    var colors = tooltip.labelColors[i];
                                    var style = 'background:#000';
                                    style += '; border-color:#000';
                                    style += '; border-width: 2px';
                                    var span = '<span class="chartjs-tooltip-key" style="' + style + '"></span>';
                                    if(body!=null && body.length>0){
                                        var money = body[0].substring(body[0].indexOf(': ')+1,body[0].length);
                                        var moneyByFormat = expNumberFilter(money);
                                        body[0] = body[0].replaceAll(money,moneyByFormat)+'元';
                                    }


                                    innerHtml += '<tr><td>' + body + '</td></tr>';
                                });
                                innerHtml += '</tbody>';

                                var tableRoot = tooltipEl.querySelector('table');
                                tableRoot.innerHTML = innerHtml;
                            }

                            var positionY = this._chart.canvas.offsetTop;
                            var positionX = this._chart.canvas.offsetLeft;

                            // Display, position, and set styles for font
                            tooltipEl.style.opacity = 1;
                            tooltipEl.style.left = positionX + tooltip.x + 'px';
                            tooltipEl.style.top = positionY + tooltip.y + 'px';
                            tooltipEl.style.fontFamily = tooltip._fontFamily;
                            tooltipEl.style.fontSize = tooltip.fontSize;
                            tooltipEl.style.fontStyle = tooltip._fontStyle;
                            tooltipEl.style.padding = tooltip.yPadding + 'px ' + tooltip.xPadding + 'px';
                        }
                    }
                }
            });
        }
        //快捷时间
        , bindSetTime: function () {
            var that = this;
            $(that.element).find('a[data-action="setTime"]').off('click').on('click',function () {
                var dataType = $(this).attr('data-type');

                $(this).addClass('btn-primary').removeClass('btn-default').siblings().addClass('btn-default').removeClass('btn-primary');

                $(that.element).find('input[name="timeStart"]').val('');
                $(that.element).find('input[name="timeEnd"]').val('');
                if(dataType=='month'){
                    $(that.element).find('input[name="timeStart"]').attr('placeholder','开始月份');
                    $(that.element).find('input[name="timeEnd"]').attr('placeholder','结束月份');
                }else{
                    $(that.element).find('input[name="timeStart"]').attr('placeholder','开始年份');
                    $(that.element).find('input[name="timeEnd"]').attr('placeholder','结束年份');
                }
            });
            $(that.element).find('input[name="timeStart"]').off('click').on('click',function () {

                var dataType = $(that.element).find('a.btn-primary[data-action="setTime"]').attr('data-type');
                var fomartStr = 'yyyy-MM';
                if(dataType=='month'){
                    fomartStr = 'yyyy-MM';
                }else{
                    fomartStr = 'yyyy';
                }
                var endTime = $(that.element).find('input[name="timeEnd"]').val();
                var onpicked =function(dp){

                    if(endTime==''){//没有结束时间，弹出结束时间弹窗
                        $(that.element).find('input[name="timeEnd"]').click();
                    }else{
                        that.renderBarChart();
                    }
                };
                WdatePicker({el:this,dateFmt:fomartStr,maxDate:endTime,onpicked:onpicked})
            });
            $(that.element).find('input[name="timeEnd"]').off('click').on('click',function () {

                var dataType = $(that.element).find('a.btn-primary[data-action="setTime"]').attr('data-type');
                var fomartStr = 'yyyy-MM';
                if(dataType=='month'){
                    fomartStr = 'yyyy-MM';
                }else{
                    fomartStr = 'yyyy';
                }
                var startTime = $(that.element).find('input[name="timeStart"]').val();
                var onpicked =function(dp){
                    if(startTime==''){//没有开始时间，弹出开始时间弹窗
                        $(that.element).find('input[name="timeStart"]').click();
                    }else{
                        that.renderBarChart();
                    }
                };
                WdatePicker({el:this,dateFmt:fomartStr,minDate:startTime,onpicked:onpicked})
            });

            $(that.element).find('span.input-group-addon').off('click').on('click',function () {
                $(this).closest('.input-group').find('input[class!="form-control input-sm hide"]').focus();
            });
        }
        //金额单位切换
        , bindSwitchAmountUnit:function () {
            var that = this;
            $(that.element).find('a[data-action="amountUnit"]').off('click').on('click',function () {
                $(this).addClass('text-info').siblings('a').removeClass('text-info');
                that.renderBarChart();
            });
        }
        , bindRefreshBtn:function () {
            var that = this;
            $(that.element).find('button[data-action="refreshBtn"]').on("click", function (e) {

                that.init();
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
