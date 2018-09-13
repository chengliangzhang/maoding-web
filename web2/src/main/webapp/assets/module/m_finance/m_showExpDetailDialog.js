/**
 * 报销详情
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_showExpDetailDialog",
        defaults = {
            isDialog:true,
            title:null,//弹窗标题
            url:null,//请求地址
            type:1,//1=报销,2=费用
            expDetail:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._typeStr = this.settings.type==1?'报销':'费用';
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.getExpData();
        }
        //加载弹窗
        ,renderDialog: function (html,callBack) {
            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.title||'报销详情',
                    area : '750px',
                    content:html,
                    cancelText:'关闭',
                    cancel:function () {

                    }

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;
                    if(callBack)
                        callBack();
                });

            }else{//不以弹窗编辑
                $(that.element).html(html);
                if(callBack)
                    callBack();
            }
        }
        //加载
        ,getExpData:function(){
            var that = this;
            var option = {};
            if(that.settings.url!=null){
                option.url=that.settings.url;
            }else{
                option.url=restApi.url_getExpMainDetail+'/'+that.settings.expDetail.id;
            }

            m_ajax.post(option,function (response) {
                if(response.code=='0'){
                    var $data={};
                    $data.myExpDetails = response.data;
                    $data.expNo = that.settings.expDetail.expNo;
                    $data.fastdfsUrl = window.fastdfsUrl;
                    $.each($data.myExpDetails.auditList,function(i,item){
                        if(item.expDate!=null&&item.expDate!=''){
                            item.expDate=moment(item.expDate).format('YYYY-MM-DD');
                        }
                    });
                    $.each($data.myExpDetails.detailList,function(i,item){
                        item.expAmount = expNumberFilter(item.expAmount);
                    });
                    $data.myExpDetails.totalExpAmount = expNumberFilter($data.myExpDetails.totalExpAmount);
                    $data.type = that.settings.type;
                    $data.typeStr = that._typeStr;
                    var html = template('m_finance/m_showExpDetailDialog',$data);
                    that.renderDialog(html);
                }else {
                    S_layer.error(response.info);
                }

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
