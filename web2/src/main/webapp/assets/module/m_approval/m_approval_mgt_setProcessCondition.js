/**
 * 审批管理-设置流程条件
 * Created by wrb on 2018/8/3.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_mgt_setProcessCondition",
        defaults = {
            isDialog:true,
            type:null,
            processData:null,//流程信息数据{类型名称,key...}
            oKCallBack:null//保存回滚事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentUserId = window.currentUserId;
        this._currentCompanyId = window.currentCompanyId;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;

            that.initHtmlData();

        }
        ,renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑
                S_layer.dialog({
                    title: that.settings.$title||'设置审批条件',
                    area : ['750px','600px'],
                    content:html,
                    cancel:function () {
                    },
                    ok:function () {

                        if ($(that.element).find('form.form-horizontal').valid()) {
                            return that.saveProcessCondtion();
                        }else{
                            return false;
                        }
                    }

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;
                    $(that.element).closest('.layui-layer').find('.layui-layer-title').append('<span class="fc-v1-grey">（审批条件仅能指定一个，且为必填字段）</span>');
                    if(callBack)
                        callBack();
                });

            }else{//不以弹窗编辑
                $(that.element).html(html);
                if(callBack)
                    callBack();
            }
        }
        //渲染列表内容
        ,initHtmlData:function () {
            var that =this;
            var html = template('m_approval/m_approval_mgt_setProcessCondition',{processData:that.settings.processData});
            that.renderDialog(html,function () {

                that.bindActionClick();
                that.submit_validate();

                if(that.settings.processData && that.settings.processData.flowTaskGroupList!=null && that.settings.processData.flowTaskGroupList.length>0){
                    $.each(that.settings.processData.flowTaskGroupList,function (i,item) {
                        if(i>1 && i<that.settings.processData.flowTaskGroupList.length-1){
                            that.addInput($(that.element).find('a[data-action="addCondition"]'));
                        }
                        $(that.element).find('input[name="conditionalVal"]').eq(i).val(item.maxValue);

                    })
                }
            });
        }
        ,saveProcessCondtion:function () {
            var that =this;
            var option = {};
            option.classId = '#content-right';
            option.url = restApi.url_prepareProcessDefine;
            option.postData = {};
            option.postData.key = that.settings.processData.key;
            option.postData.type = that.settings.type;

            var list = [];
            $(that.element).find('input[name="conditionalVal"]').each(function () {
               list.push($(this).val());
            });
            option.postData.startDigitCondition = {};
            option.postData.startDigitCondition.pointList = list;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    if(that.settings.oKCallBack)
                        that.settings.oKCallBack(response.data.flowTaskGroupList);

                } else {
                    S_layer.error(response.info);
                }
            });
        }
        ,renderRemoveInput:function () {
            var that = this;
            var len = $(that.element).find('input[name="conditionalVal"]').length;
            $(that.element).find('a[data-action="removeInput"]').remove();
            $(that.element).find('input[name="conditionalVal"]').each(function (i) {
                if(i>0 && len>2){

                    $(this).after('<a href="javascript:void(0);" class="icon-remove fc-red" data-action="removeInput"><i class="glyphicon glyphicon-remove"></i></a>')
                }
            });
            $(that.element).find('a[data-action="removeInput"]').off('click').on('click',function () {
                $(this).parent().prev().remove();
                $(this).parent().remove();
                that.renderRemoveInput();
            });
        }
        ,addInput:function ($this) {
            var that = this;
            $this.parent().before('<div class="col-md-1 m-b-xs p-xxs text-center"> < </div><div class="col-md-2 m-b-xs"><input class="form-control input-sm" type="text" name="conditionalVal"></div>');
            that.renderRemoveInput();
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'addCondition'://添加条件

                        that.addInput($this);
                        break;
                }
            })
        }
        //保存验证
        , submit_validate: function () {
            var that = this;
            $(that.element).find('form.form-horizontal').validate({
                rules: {
                    conditionalVal: {
                        ckIsEmpty:true,
                        ckIsNumber:true
                    }
                },
                messages: {
                    conditionalVal: {
                        ckIsEmpty: '请输入数值区间！',
                        ckIsNumber:'请输入数字！'
                    }
                },
                errorElement: "label",  //用来创建错误提示信息标签
                errorPlacement: function (error, element) {  //重新编辑error信息的显示位置
                    error.appendTo(element.closest('.form-group'));
                }
            });
            $.validator.addMethod('ckIsEmpty', function(value, element) {
                var isOk = true;
                $(that.element).find('input[name="conditionalVal"]').each(function () {
                    var val = $(this).val();
                    if($.trim(val)==''){
                        isOk = false;
                        return false;
                    }
                });
                return  isOk;
            }, '请输入数值区间！');
            $.validator.addMethod('ckIsNumber', function(value, element) {
                var isOk = true;
                $(that.element).find('input[name="conditionalVal"]').each(function () {
                    var val = $(this).val();
                    if(!regularExpressions.number.test(val)){
                        isOk = false;
                        return false;
                    }
                });
                return  isOk;
            }, '请输入数字！');
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
