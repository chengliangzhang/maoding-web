/**
 * 选择经营负责人和选择设计负责人
 * Created by wrb on 2018/3/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_changeOperator",
        defaults = {
            $title:null,
            $isDialog:true,
            $projectId:null,
            $type:0,//0=经营负责人，1=设计负责人
            $selectedUserList:null,
            $selectUserCallback:null,
            $renderCallBack:null//弹窗回掉方法

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
            if(that.settings.$isDialog){//以弹窗编辑
                S_layer.dialog({
                    title: that.settings.$title || '选择经营负责人',
                    area : '750px',
                    content:html,
                    cancel:function () {
                    },
                    ok:function () {

                        var flag = $(that.element).find('form').valid();
                        if (!flag || that.save()) {
                            return false;
                        }
                    }

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

            that.getUserList(function (data) {
                var html = template('m_taskIssue/m_changeOperator',{
                    orgUserList:data,
                    selectedUserList:that.settings.$selectedUserList
                });
                that.renderDialog(html,function () {
                    that.bindActionClick();
                    if(that.settings.$renderCallBack!=null){
                        that.settings.$renderCallBack(that.element);
                    }
                });


            });
        }
        //查出人员列表
        ,getUserList:function (callBack) {
            var that = this;
            var options={};
            options.url= that.settings.$type==0?restApi.url_listOperatorManager:restApi.url_listDesignManager;
            m_ajax.get(options,function (response) {
                if(response.code=='0'){
                    if(callBack!=null){
                        return callBack(response.data);
                    }
                }else {
                    S_layer.error(response.info);
                }
            })
        }
        //按钮事件绑定
        , bindActionClick: function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click', function () {

                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'choseUser':
                        if (that.settings.$selectUserCallback != null) {
                            var $data = {};
                            $data.userId = $this.attr('data-userId');//用户账户ID
                            $data.companyUserId = $this.attr('data-companyUserId');//组织人员ID
                            $data.userName = $this.parent().parent().find('td:eq(0)').text();
                            $data.id = $this.parents('.ui-dialog-content').attr('id');
                            return that.settings.$selectUserCallback($data, $this);
                        }
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


