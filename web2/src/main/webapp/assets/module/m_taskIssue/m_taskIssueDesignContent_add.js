/**
 * 任务签发－添加设计内容
 * Created by wrb on 2017/5/15.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_taskIssueDesignContent_add",
        defaults = {
            $title:null,
            $isDialog:true,
            $projectId:null,
            $okBackCall:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._initHtml();
        }
        //初始化数据,生成html
        ,_initHtml:function () {
            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'添加设计任务',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '600',
                    minHeight:'125',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {

                    },
                    okText:'保存',
                    ok:function () {
                        if($(that.element).find('form.content-form').valid()){//&& $(that.element).find('form.inputTimeOBox').valid()
                            that._saveDesignContent();
                        }else {
                            return false;
                        }
                    }

                },function(d){//加载html后触发

                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    var html = template('m_taskIssue/m_taskIssueDesignContent_add', {});
                    $(that.element).html(html);

                    var option = {};
                    option.$isDialog = false;
                    option.$isHaveMemo = false;
                    $(that.element).find('#time-box').m_inputProcessTime(option);

                    that._saveDesignContent_validate();
                    //that._saveDesignContent_validate2();

                });
            }else{//不以弹窗编辑


                var html = template('m_taskIssue/m_taskIssueDesignContent_add', {});
                $(that.element).html(html);

                var option = {};
                option.$isDialog = false;
                $(that.element).find('#time-box').m_inputProcessTime(option);
            }
        }

        //添加或编辑设计阶段
        , _saveDesignContent:function () {
            var that = this;
            var options = {};
            options.url = restApi.url_saveOrUpdateProjectDesign;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            options.postData.companyId = that._currentCompanyId;
            options.postData.contentName = $(that.element).find('input[name="taskName"]').val();
            options.postData.startTime = $(that.element).find('input[name="startTime"]').val();
            options.postData.endTime = $(that.element).find('input[name="endTime"]').val();

            m_ajax.postJson(options, function (response) {
                if (response.code == '0') {
                    S_toastr.success('保存成功！');
                    if(that.settings.$okBackCall!=null){
                        that.settings.$okBackCall();
                    }
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //验证
        ,_saveDesignContent_validate:function(){
            var that = this;
            $(that.element).find('form.content-form').validate({
                rules: {
                    taskName:{
                        required:true
                    }
                },
                messages: {
                    taskName:{
                        required:'请输入设计内容!'
                    }
                }
            });
        }
        ,_saveDesignContent_validate2:function(){
            var that = this;
            $(that.element).find('form.inputTimeOBox').validate({
                rules: {
                    startTime:{
                        required:true
                    },
                    endTime:{
                        required:true
                    }

                },
                messages: {
                    startTime:{
                        required:'请设置开始日期!'
                    },
                    endTime:{
                        required:'请设置结束日期!'
                    }
                }
                ,errorPlacement: function (error, element) { //指定错误信息位置
                    if (element.hasClass('timeInput')) {
                        error.appendTo(element.closest('.form-group'));
                    } else {
                        error.insertAfter(element);
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
