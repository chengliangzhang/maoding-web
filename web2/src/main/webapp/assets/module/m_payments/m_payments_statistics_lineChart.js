/**
 * 收支总览-分类统计-按账期统计
 * Created by wrb on 2017/11/30.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_statistics_lineChart",
        defaults = {
            $chartData:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._companyList = [];
        this._companyIdList = [];
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_payments/m_payments_statistics_lineChart',{});
            $(that.element).html(html);

            var lineChartData = that.settings.$chartData;
            var dateArr = lineChartData[0];
            $.each(dateArr, function (i, item) {
                dateArr[i]=item+'-01';
            });
            dateArr.splice(0, 0, "x");
            var data1Arr = lineChartData[2];
            data1Arr.splice(0, 0, "data1");
            var data2Arr = lineChartData[1];
            data2Arr.splice(0, 0, "data2");
            var data3Arr = lineChartData[3];
            data3Arr.splice(0, 0, "data3");

            c3.generate({
                bindto: '#lineChart',
                data:{
                    names: {
                        data1: '总收入',
                        data2: '总支出'
                        //,data3: '合同总金额'
                    },
                    x: 'x',
                    columns: [
                        dateArr,
                        data1Arr,
                        data2Arr
                        //,data3Arr
                    ],
                    colors:{
                        data1: '#4765a1',
                        data2: '#449fae'
                        //,data3: '#999'
                    }
                },
                axis: {
                    x: {
                        type: 'timeseries',
                        tick: {
                            format: '%Y年%m月'
                        }
                    }
                },
                tooltip: {
                    format: {
                        value: function (value, ratio, id) {
                            return expNumberFilter(value)+'元';
                        }
                    }
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
