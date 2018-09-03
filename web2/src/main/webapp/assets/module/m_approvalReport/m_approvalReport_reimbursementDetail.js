/**
 * 报销详情
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approvalReport_reimbursementDetail",
        defaults = {
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
            that.getData();
        }
        //加载弹窗
        ,getData: function () {
            var that = this;
            S_dialog.dialog({
                title: that.settings.title||'报销详情',
                contentEle: 'TConsentOBox',
                lock: 3,
                width: '800',
                minHeight: '450',
                tPadding: '0px',
                url: rootPath+'/assets/module/m_common/m_dialog.html',
                cancelText:'关闭',
                cancel:function () {

                }
            },function(d){//加载html后触发
                that.getExpData(d);


            });
        }
        //加载
        ,getExpData:function(d){
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
                    var html = template('m_approvalReport/m_approvalReport_reimbursementDetail',$data);
                    $('div[id="content:'+d.id+'"] .dialogOBox').html(html);
                }else {
                    S_dialog.error(response.info);
                }

            })
        }

    });

    /*
     1.一般初始化（缓存单例）： $('#id').pluginName(initOptions);
     2.强制初始化（无视缓存）： $('#id').pluginName(initOptions,true);
     3.调用方法： $('#id').pluginName('methodName',args);
     */
    $.fn[pluginName] = function (options, args) {
        var instance;
        var funcResult;
        var jqObj = this.each(function () {

            //从缓存获取实例
            instance = $.data(this, "plugin_" + pluginName);

            if (options === undefined || options === null || typeof options === "object") {

                var opts = $.extend(true, {}, defaults, options);

                //options作为初始化参数，若args===true则强制重新初始化，否则根据缓存判断是否需要初始化
                if (args === true) {
                    instance = new Plugin(this, opts);
                } else {
                    if (instance === undefined || instance === null)
                        instance = new Plugin(this, opts);
                }

                //写入缓存
                $.data(this, "plugin_" + pluginName, instance);
            }
            else if (typeof options === "string" && typeof instance[options] === "function") {

                //options作为方法名，args则作为方法要调用的参数
                //如果方法没有返回值，funcReuslt为undefined
                funcResult = instance[options].call(instance, args);
            }
        });

        return funcResult === undefined ? jqObj : funcResult;
    };

})(jQuery, window, document);
