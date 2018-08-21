/**
 * 收支总览-分类统计-按收支类型统计
 * Created by wrb on 2017/11/30.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_statistics_pieChart",
        defaults = {
            $chartData:null,
            $renderCallback:null
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
            var html = template('m_payments/m_payments_statistics_pieChart',{});
            $(that.element).html(html);
            var pie = new d3pie("totalRevenueChart", {
                "header": {
                    "title": {
                        "text": "总收入",
                        "color": "#666666",
                        "fontSize": 24,
                        "font": "courier"
                    },
                    "subtitle": {
                        "text": expNumberFilter(that.settings.$chartData.oneCount)+"元",
                        "color": "#333333",
                        "fontSize": 18,
                        "font": "courier"
                    },
                    "location": "pie-center",
                    "titleSubtitlePadding": 2
                },
                "footer": {
                    "color": "#999999",
                    "fontSize": 10,
                    "font": "open sans",
                    "location": "bottom-left"
                },
                "size": {
                    "canvasWidth": 590,
                    "pieInnerRadius": "60%",
                    "pieOuterRadius": "70%"
                },
                "data": {
                    "content": [
                        {
                            "label": "其他收入",
                            "value": that.settings.$chartData.oneData[0],
                            "color": "#c1cde4"
                        },
                        {
                            "label": "合同回款",
                            "value": that.settings.$chartData.oneData[1],
                            "color": "#4765a1"
                        },
                        {
                            "label": "技术审查费",
                            "value": that.settings.$chartData.oneData[2],
                            "color": "#cbe5e9"
                        },
                        {
                            "label": "合作设计费",
                            "value": that.settings.$chartData.oneData[3],
                            "color": "#449fae"
                        }
                    ]
                },
                "labels": {
                    "inner": {
                        "format": "none"
                    },
                    "mainLabel": {
                        "color": "#999999",
                        "fontSize": 18
                    },
                    "percentage": {
                        "color": "#999999",
                        "fontSize": 11,
                        "decimalPlaces": 0
                    },
                    "value": {
                        "color": "#999999",
                        "fontSize": 11
                    },
                    "lines": {
                        "enabled": true,
                        "style": "straight",
                        "color": "#777777"
                    },
                    "truncation": {
                        "enabled": true
                    }
                },
                "tooltips": {
                    "enabled": true,
                    "type": "placeholder",
                    "string": "{label}: {value}元, {percentage}%"
                },
                "effects": {
                    "pullOutSegmentOnClick": {
                        "effect": "linear",
                        "speed": 400,
                        "size": 8
                    }
                },
                "misc": {
                    "colors": {
                        "background": "#ffffff"
                    }
                },
                "callbacks": {
                    onload:function () {
                        console.log("loaded.");
                        that.formatTooltipMoney('totalRevenueChart');
                    }
                }
            });
            var pie = new d3pie("totalExpenditureChart", {
                "header": {
                    "title": {
                        "text": "总支出",
                        "color": "#666666",
                        "fontSize": 24,
                        "font": "courier"
                    },
                    "subtitle": {
                        "text": expNumberFilter(that.settings.$chartData.twoCount)+"元",
                        "color": "#333333",
                        "fontSize": 18,
                        "font": "courier"
                    },
                    "location": "pie-center",
                    "titleSubtitlePadding": 2
                },
                "footer": {
                    "color": "#999999",
                    "fontSize": 10,
                    "font": "open sans",
                    "location": "bottom-left"
                },
                "size": {
                    "canvasWidth": 590,
                    "pieInnerRadius": "60%",
                    "pieOuterRadius": "70%"
                },
                "data": {
                    "content": [
                        {
                            "label": "主营业务税金及附加",
                            "value": that.settings.$chartData.twoData[0],
                            "color": "#c1cde4"
                        },
                        {
                            "label": "主营业务成本",
                            "value": that.settings.$chartData.twoData[1],
                            "color": "#4765a1"
                        },
                        {
                            "label": "财务费用",
                            "value": that.settings.$chartData.twoData[2],
                            "color": "#344a77"
                        },
                        {
                            "label": "所得税费用",
                            "value": that.settings.$chartData.twoData[3],
                            "color": "#cbe5e9"
                        },
                        {
                            "label": "管理费用",
                            "value": that.settings.$chartData.twoData[4],
                            "color": "#449fae"
                        }
                    ]
                },
                "labels": {
                    "inner": {
                        "format": "none"
                    },
                    "mainLabel": {
                        "color": "#999999",
                        "fontSize": 18
                    },
                    "percentage": {
                        "color": "#999999",
                        "fontSize": 11,
                        "decimalPlaces": 0
                    },
                    "value": {
                        "color": "#999999",
                        "fontSize": 11
                    },
                    "lines": {
                        "enabled": true,
                        "style": "straight",
                        "color": "#777777"
                    },
                    "truncation": {
                        "enabled": true
                    }
                },
                "tooltips": {
                    "enabled": true,
                    "type": "placeholder",
                    "string": "{label}: {value}元, {percentage}%"
                },
                "effects": {
                    "pullOutSegmentOnClick": {
                        "effect": "linear",
                        "speed": 400,
                        "size": 8
                    }
                },
                "misc": {
                    "colors": {
                        "background": "#ffffff"
                    }
                },
                "callbacks": {
                    onload:function () {
                        console.log("loaded.");
                        that.formatTooltipMoney('totalExpenditureChart');
                    }
                }
            });

        }
        //格式化tooltip中的money
        ,formatTooltipMoney:function (id) {
            $('#'+id+' g[class$="_tooltip"] text').each(function () {
                var text = $(this).text();
                var money = text.substring(text.indexOf(': ')+1,text.indexOf('元'));
                var moneyByFormat = expNumberFilter(money);
                var newText = $(this).text().replaceAll(money,moneyByFormat);
                $(this).text(newText);
                var w = $(this).parent('g').find('rect').attr('width');
                $(this).parent('g').find('rect').attr('width',(w-0+20));
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
