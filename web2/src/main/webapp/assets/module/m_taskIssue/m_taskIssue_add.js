/**
 * 项目信息－添加任务签发
 * Created by wrb on 2017/2/21.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_taskIssue_add",
        defaults = {
            $placement:null,
            $projectId:null,
            $doType:null,//默认是添加任务，'changeOrg'=更改设计组织
            $taskId:null,//任务ID
            $okCallBack:null,//回调函数
            $departId:null,//部门ID
            $companyId:null,//当前签发的组织ID
            $currentAppointmentDate:null//时间限制，这里指合同的时间
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
            m_ajax.postJson(option,function (response) {//查询组织

                if(response.code=='0'){
                    var $data = {};
                    $data = response.data;
                    $data.doType = that.settings.$doType;
                    $data.currentCompanyId = that._currentCompanyId;

                    that._getDepartByCompanyId(function (data) {//查询部门

                        $data.orgList = data;
                        $data.orgId = that.settings.$departId;
                        $data.companyId = that.settings.$companyId;
                        if (that.settings.$currentAppointmentDate != null) {
                            $data.appointmentStartTime = that.settings.$currentAppointmentDate.startTime;
                            $data.appointmentEndTime = that.settings.$currentAppointmentDate.endTime;
                        }
                        var $btnPopover = $(that.element).find('i').length>0?$(that.element).find('i'):$(that.element);
                        $btnPopover.m_popover({
                            placement: 'left',
                            content: template('m_taskIssue/m_taskIssue_add', $data),
                            titleHtml: '<h3 class="popover-title">'+(that.settings.$title?that.settings.$title:'任务签发')+'</h3>',
                            onShown: function ($popover) {
                                that._bindSelChange($popover);
                                that._saveTaskIssue_validate();
                                if(that.settings.$doType!=null && that.settings.$doType=='changeOrg'){
                                    $popover.find('.time-box').remove();
                                }else{
                                    $popover.find('.input-group-addon').on('click',function(e){
                                        $(this).prev('input').focus();
                                        stopPropagation(e);
                                    });
                                }

                                var statOrgs = [],departOrg = [];
                                $.each($data.allCompanyList, function (i, o) {
                                    statOrgs.push({
                                        id: o.id,
                                        text: o.companyName
                                    });
                                });
                                $.each($data.orgList, function (i, o) {
                                    if(currentCompanyId!=o.id){
                                        statOrgs.push({
                                            id: o.id,
                                            text: o.companyName
                                        });
                                    }
                                });
                                $popover.find('select[name="designOrg"]').select2({
                                    allowClear: false,
                                    language: "zh-CN",
                                    minimumResultsForSearch: Infinity,
                                    data: statOrgs
                                });
                                $popover.find('select[name="orgId"]').select2({
                                    allowClear: false,
                                    language: "zh-CN",
                                    minimumResultsForSearch: Infinity,
                                    data: departOrg
                                });
                                $popover.find('select[name="orgId"]').val($data.orgId).trigger('change');
                                $popover.find('select[name="designOrg"]').val($data.companyId).trigger('change');

                            },
                            onSave: function ($popover) {
                                var $options = that._saveTaskTssueParam($popover);
                                if ($('form.add-issue-form').valid()) {

                                    if(that.settings.$doType!=null && that.settings.$doType=='changeOrg'){
                                        var $btn=$popover.find('button.btn-primary');
                                        $btn.m_popover({
                                            clearOnInit:false,
                                            placement: 'left',
                                            content: template('m_common/m_popover_confirm',
                                                {confirmMsg: '确定更换该任务的设计组织？'}),
                                            onShown: function ($popover){
                                                $popover.css('z-index','99999');
                                            },
                                            onSave: function ($popover) {
                                                that._saveTaskIssue($options)
                                            }
                                        }, true);
                                        return false;
                                    }else{

                                        if(that._saveTaskIssue($options)){
                                            return false;
                                        }
                                    }

                                }else{
                                    return false;
                                }
                            }
                        }, true);
                    })
                }else {
                    S_layer.error(response.info);
                }
            })
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
                            companyId: $(that.element).next('.popover').find('select[name="designOrg"]').val()
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

        //保存签发
        ,_saveTaskTssueParam:function ($popover) {
            var options={},that=this;
            options.url=restApi.url_saveTaskIssuing;
            options.postData = {};
            options.postData.projectId = that.settings.$projectId;
            options.postData.taskType =2;
            options.postData.companyId = $popover.find('select[name="designOrg"]').val();

            if(that.settings.$doType=='changeOrg'){
                options.postData.id = $(that.element).attr('data-id');//that.settings.$taskId;
            }else{
                options.postData.taskPid = $(that.element).attr('data-id');
                options.postData.taskName = $.trim($popover.find('input[name="taskName"]').val());
                options.postData.startTime = $popover.find('input[name="startTime"]').val();
                options.postData.endTime = $popover.find('input[name="endTime"]').val();
            }
            var t = $popover.find('select[name="designOrg"] option:selected').val();
            if(t==that._currentCompanyId){//选择的是本组织
                options.postData.managerId = null;
                var orgId = $popover.find('select[name="orgId"] option:selected').val();
                options.postData.orgId = orgId;
            }

            return options;
        }
        ,_saveTaskIssue:function (options) {
            var that=this,isError = false;
            options.async = false;
            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){

                    if(that.settings.$doType!=null && that.settings.$doType=='changeOrg'){
                        S_toastr.success('修改成功！');
                    }else {
                        S_toastr.success('添加成功！');
                    }
                    if(that.settings.$okCallBack){
                        that.settings.$okCallBack();
                    }else{
                        that._refreshTaskIssuePage();
                    }
                }else {
                    S_layer.error(response.info);
                    isError = true;
                }
            });
            if(isError){//产生错误
                that._stopPropagationByDialog();
            }
            return isError;
        }

        //查询组织部门
        ,_getDepartByCompanyId:function (callBack) {
            var that = this;
            var option  = {};
            option.url = restApi.url_getDepartByCompanyId+'/'+that._currentCompanyId;
            m_ajax.get(option,function (response) {
                if(response.code=='0'){
                    return callBack(response.data);
                }else {
                    S_layer.error(response.info);
                }
            })
        }

        //验证当前选的组织是否已选过，选过的话，经营负责人不能另选
        ,_validateIssueTaskCompany:function (toCompanyId,callBack) {

            var options = {},that = this;

            options.url = restApi.url_validateIssueTaskCompany;
            options.postData = {};
            options.postData.toCompanyId = toCompanyId;
            options.postData.projectId = that.settings.$projectId;
            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){
                    return callBack(response.data);
                }else {
                    S_layer.error(response.info);
                }
            })
        }

        //绑定select change事件
        ,_bindSelChange:function ($popover) {
            var that = this;
            $popover.find('select[name="designOrg"]').change(function () {

                var t = $popover.find('select[name="designOrg"] option:selected').val();
                var departDivObj = $popover.find('.depart-div');
                if(t==that._currentCompanyId){
                    departDivObj.removeClass('hide');//显示部门选择
                }else{
                    departDivObj.addClass('hide');//隐藏部门选择

                }
            });
        }

        //保存验证
        , _saveTaskIssue_validate: function () {
            var that = this;
            $('form.add-issue-form').validate({
                rules: {
                    taskName: {
                        required: true
                    }
                    ,designOrg: {
                        required: true
                    }
                    ,startTime: {
                        required: true
                    }
                    ,endTime: {
                        required: true
                    }
                },
                messages: {
                    taskName: {
                        required: '设计任务不能为空！'
                    }
                    ,designOrg: {
                        required: '请选择设计组织！'
                    }
                    ,startTime: {
                        required: '请设置开始日期！'
                    }
                    ,endTime: {
                        required: '请设置结束日期！'
                    }


                },
                errorElement: "label",  //用来创建错误提示信息标签
                errorPlacement: function (error, element) {  //重新编辑error信息的显示位置
                    error.appendTo(element.closest('.col-24-sm-18'));
                }
            });
        }

        //刷新当前界面
        ,_refreshTaskIssuePage:function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            $('#project_detail').m_taskIssue(option);
        }

        //防止冒泡
        ,_stopPropagationByDialog:function () {
            $('.ui-popup').off('click.ui-popup').on('click.ui-popup', function (e) {
                stopPropagation(e);
                return false;
            });
            $('.ui-popup-backdrop').off('click.ui-popup-backdrop').on('click.ui-popup-backdrop', function (e) {
                stopPropagation(e);
                return false;
            });
            $('.ui-dialog-autofocus').off('click.ui-dialog-autofocus').on('click.ui-dialog-autofocus', function (e) {
                $('.ui-popup').remove();
                $('.ui-popup-backdrop').remove();
                stopPropagation(e);
                return false;
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
