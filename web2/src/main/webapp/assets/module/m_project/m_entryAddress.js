/**
 * 录入地址
 * Created by wrb on 2016/12/21.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_entryAddress",
        defaults = {
            $title:null,
            $isDialog:true,
            $placement:'null',
            $province:null,//选中的省份
            $city:null,//选中的城市
            $county:null,//选中的县或区或镇
            $detailAddress:'',
            $okCallBack:null
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
            that.initHtmlData(function (data) {
                $("#selectRegion").citySelect({
                    prov:that.settings.$province,
                    city:that.settings.$city,
                    dist:that.settings.$county,
                    nodata:"none",
                    required:false
                });
                that.bindActionClick();
            });
        },
        //初始化数据
        initHtmlData:function (callBack) {
            var that = this;
            var options = {};
            options.titleHtml ='<h3 class="popover-title">'+(that.settings.$title?that.settings.$title:'编辑地址')+'</h3>';
            options.placement =that.settings.$placement?that.settings.$placement:'top';
            options.content =that.initHtmlTemplate();
            options.onShown = function ($popover) {
                if(callBack!=null){
                    callBack();
                }
            };
            options.onSave = function ($popover) {
                return that.saveAddress();
            };

            $(that.element).m_popover(options,true);
        }
        //保存地址详情编辑
        ,saveAddress:function () {
            var that = this;

            var p = $(that.element).next('.popover').find('select.prov').val();
            var c = $(that.element).next('.popover').find('select.city').val();
            var d = $(that.element).next('.popover').find('select.dist').val();
            if(p==null|| p==undefined)
                p='';
            if(c==null|| c==undefined)
                c='';
            if(d==null|| d==undefined)
                d='';
            var $data = {};
            $data.province = p;
            $data.city = c;
            $data.county = d;
            $data.detailAddress = $('.selectRegionOBox input[name="detailAddress"]').val();

            if(that.settings.$okCallBack!=null){
                return that.settings.$okCallBack($data);
            }

        }
        //生成html
        ,initHtmlTemplate:function () {
            var that = this;
            var data = {};
            data.$detailAddress = that.settings.$detailAddress;
            var html = template('m_project/m_entryAddress',data);
            return html;
            // $(that.element).next('.popover').find('editable-input').html(html);
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('.cityBox select').change(function () {
                var w = 508;
                var p = $(that.element).find('select.prov').val();
                var c = $(that.element).find('select.city').val();
                var d = $(that.element).find('select.dist').val();
                if(p==null|| p==undefined)
                    p='';
                if(c==null|| c==undefined)
                    c='';
                if(d==null|| d==undefined)
                    d='';
                var txt = p+c+d;
                $(that.element).find('.cityText').html(txt);
                $(that.element).find('.detailAddressLabel').css('width',(w-(txt.length*14)-5)+'px');
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


