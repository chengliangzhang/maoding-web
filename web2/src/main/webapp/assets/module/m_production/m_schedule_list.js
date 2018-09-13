/**
 * 进度变更记录列表
 * Created by wrb on 2018/6/28.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_schedule_list",
        defaults = {
            $title:null,
            $isDialog:true,
            $eleId:null,
            $projectId:null,
            $taskId:null,
            $publishId:null,
            $type:null,//1=合同约定，2＝计划
            $taskCompanyId:null,
            $renderCallBack:null,//弹窗回掉方法
            $align:null

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
            that.initHtmlTemplate();
        },
        //初始化数据
        renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.$isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: '计划进度调整历史',
                    area : '750px',
                    content:html,
                    cancel:function () {
                    },
                    cancelText:'关闭'

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.$isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
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
        //生成html
        ,initHtmlTemplate:function () {
            var that = this;

            that.getTaskScheduleChangesList(function (data) {
                var html = template('m_production/m_schedule_list',{taskScheduleChangesList:data});
                that.renderDialog(html,function () {
                    if(that.settings.$renderCallBack!=null){
                        that.settings.$renderCallBack(that.element);
                    }
                });


            });
        }
        //根据任务ID获取该任务的进度变更列表
        ,getTaskScheduleChangesList:function (callBack) {
            var that = this;
            var options={};
            options.url=restApi.url_getChangeTimeList;
            options.postData = {};
            options.postData.targetId = that.settings.$taskId;

            if(that.settings.$type!=2){
                options.postData.type = that.settings.$type;
            }

            m_ajax.postJson(options,function (response) {
                if(response.code=='0'){
                    if(callBack!=null){
                        return callBack(response.data);

                    }
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


