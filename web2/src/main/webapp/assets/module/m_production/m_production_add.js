/**
 * 项目信息－添加生产任务
 * Created by wrb on 2017/2/21.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_production_add",
        defaults = {
            $title:null,
            $placement:null,
            $projectId:null,
            $doType:null,//默认是添加任务，'changeOrg'=更改设计组织
            $taskId:null,
            $personInCharge:null,//当前任务负责人
            $personInChargeId:null,//当前任务负责人员工ID
            $currentAppointmentDate:null//时间限制，这里指计划进度
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;//当前组织ID
        this._userList = [];//负责人匹配
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._initHtmlData();
        }
        //初始化数据,生成html
        ,_initHtmlData:function (callBack) {

            var option={},that = this;
            option.url=restApi.url_getIssueTaskCompany;
            option.postData = {};
            option.postData.taskId = that.settings.$taskId;
            m_ajax.postJson(option,function (response) {

                if(response.code=='0'){

                    var $data = {};
                    $data = response.data;
                    $data.doType = that.settings.$doType;
                    $data.personInCharge = that.settings.$personInCharge;
                    $data.personInChargeId = that.settings.$personInChargeId;

                    if (that.settings.$currentAppointmentDate != null) {
                        $data.appointmentStartTime = that.settings.$currentAppointmentDate.startTime;
                        $data.appointmentEndTime = that.settings.$currentAppointmentDate.endTime;
                    }

                    $(that.element).find('i').m_popover({
                        placement: 'left',
                        content: template('m_production/m_production_add', $data),
                        titleHtml: '<h3 class="popover-title">'+(that.settings.$title?that.settings.$title:'任务分解')+'</h3>',
                        onShown: function ($popover) {
                            that._selectFun();
                            that._saveTaskIssue_validate();
                            $popover.find('.input-group-addon').on('click',function(e){
                                $(this).prev('input').focus();
                                stopPropagation(e);
                            });
                        },
                        onSave: function ($popover) {
                            if ($('form.add-issue-form').valid()) {
                                return that._saveTaskIssue();
                            }else{
                                return false;
                            }
                        }
                    }, true);

                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //刷新当前界面
        ,_refresh:function () {
            var option = {}, that = this;
            var scrollTop = $('body').scrollTop();
            option.$projectId = that.settings.$projectId;
            option.$scrollCallBack = function () {
                $('body').scrollTop(scrollTop);
            };
            $('#project_detail').m_production(option);
        }
        ,_selectFun:function () {
            var that = this;
            $(".js-example-disabled-results").select2({
                language: "zh-CN",
                ajax: {
                    contentType: "application/json",
                    url: restApi.url_getUserByKeyWord,
                    dataType: 'json',
                    type:'POST',
                    delay: 500,
                    data: function (params) {
                        var ret={
                            keyword: params.term, // search term
                            companyId: that._currentCompanyId
                        };
                        return JSON.stringify(ret);
                    },
                    processResults: function (data, params) {
                        return {
                            results: $.map(data.data,function(o,i){
                                return {
                                    id:o.id,
                                    text:o.user_name
                                }
                            })
                        };
                    },
                    cache: true
                }
            });
        }

        //分解任务
        ,_saveTaskIssue:function () {
            var isTrue = false;
            var options={},that=this;
            options.url=restApi.url_saveTaskIssuing;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            options.postData.taskType =0;
            options.postData.companyId = that._currentCompanyId;
            options.postData.taskPid = $(that.element).attr('data-id');
            options.postData.taskName = $.trim($(that.element).find('i').next('.popover').find('input[name="taskName"]').val());
            /*if(options.postData.taskName==undefined || options.postData.taskName==''){
                return '请输入设计任务！';
            }*/

            options.postData.designerId = $(that.element).find('i').next('.popover').find('select[name="designerId"]').val();
            /*if(options.postData.designerId==undefined || options.postData.designerId==''){
                return '请选择任务负责人！';
            }*/
            options.postData.taskRemark = $(that.element).find('i').next('.popover').find('textarea[name="taskRemark"]').val();

            options.postData.startTime = $(that.element).find('i').next('.popover').find('input[name="startTime"]').val();
            options.postData.endTime = $(that.element).find('i').next('.popover').find('input[name="endTime"]').val();

            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){

                    S_toastr.success('添加成功！');
                    that._refresh();
                }else {
                    S_dialog.error(response.info);
                }
            })
        }

        //保存验证
        , _saveTaskIssue_validate: function () {
            var that = this;
            $('form.add-issue-form').validate({
                rules: {
                    taskName: {
                        required: true
                    }
                    ,designerId: {
                        required: true
                    }
                    ,taskRemark: {
                        maxlength: 500
                    }
                },
                messages: {
                    taskName: {
                        required: '设计任务不能为空！'
                    }
                    ,designerId: {
                        required: '请选择任务负责人！'
                    }
                    ,taskRemark: {
                        maxlength: '输入不能超过500个字符！'
                    }
                },
                errorElement: "label",  //用来创建错误提示信息标签
                errorPlacement: function (error, element) {  //重新编辑error信息的显示位置
                    error.appendTo(element.closest('.col-sm-9'));
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
