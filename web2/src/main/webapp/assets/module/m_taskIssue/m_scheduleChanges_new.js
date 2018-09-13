/**
 * 进度变更
 * Created by wrb on 2016/12/26.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_scheduleChanges_new",
        defaults = {
            $title:null,
            $isDialog:true,
            $okCallBack:null,
            $parentTaskObj:null,
            $currentAppointmentDate:null,//限制时间数据
            $theCurrentTaskObj:null,
            $projectId:null,
            $designContentId:null,
            $taskId:null,
            $labelText:null,
            $type:null,//1=合同约定，2＝计划
            $taskCompanyId:null
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
            that._getTaskScheduleChangesList(function (data) {
                that._initHtmlTemplate(data);
            });

        },
        //初始化界面
        _renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.$isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.$title||'进度变更',
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
        ,_initHtmlTemplate:function (data) {
            var that = this;
            var $data = {};
            if(data!=null){
                $data.taskScheduleChangesList = data;
                //$data.taskScheduleObj = that.settings.$theCurrentTaskObj;
            }
            $.each($data.taskScheduleChangesList,function(i,item){
                $data.taskScheduleChangesList[i].timeDiffStr=timeDiffTime(item.startTime,item.endTime);
            });
            $data.$labelText = that.settings.$labelText;
            if(that.settings.$type==2){
                $data.$labelText='计划进度'
            }
            var html = template('m_taskIssue/m_scheduleChanges_new',$data);
            that._renderDialog(html,function () {
                that._bindActionClick();

            });
        }
        //刷新当前界面
        ,_refresh:function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            $('#project_detail').m_taskIssue(option);
        }
        //根据任务ID获取该任务的进度变更列表
        ,_getTaskScheduleChangesList:function (callBack) {
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
        //进度变更
        ,_taskScheduleChange:function (obj) {
            var that = this,options={};
            options.$title = '添加变更';
            options.$isOkSave = true;

            var $data = {};
            if(that.settings.$type==2){
                $data.id = that.settings.$taskId;
                $data.taskType = 2;
                $data.companyId = that.settings.$taskCompanyId;

                options.$saveDataUrl = restApi.url_saveTaskIssuing;
            }else{//计划变更
                $data.targetId = that.settings.$taskId;
                $data.type = that.settings.$type;//计划进度

                options.$saveDataUrl = restApi.url_saveProjectProcessTime;
            }
            options.$saveData = $data;
            options.$saveTimeKeyVal=['startTime','endTime','memo'];
            options.$currentAppointmentDate=that.settings.$currentAppointmentDate;
            options.$okCallBack = function (data) {
                that.init();
                if(that.settings.$okCallBack!=null){
                    that.settings.$okCallBack();
                }
            };
            $('body').m_inputProcessTime(options);
        }
        //进度变更修改（针对设计任务的计划进度）
        ,_taskScheduleUpdate:function (obj) {
            var that = this,options={};

            var dataId = $(obj).attr('data-id');
            var dataStaTime = $(obj).attr('data-start-time');
            var dataEndTime = $(obj).attr('data-end-time');
            var dataListType = $(obj).attr('data-list-type');
            var dataMemo = $(obj).parents('.ibox-content').find('span.memo-span:last').html();

            options.$title = '修改变更';
            options.$isOkSave = true;

            var $data = {};
            $data.targetId = that.settings.$taskId;
            //$data.type = that.settings.$type;//计划进度
            $data.id = dataId;
            options.$saveDataUrl = restApi.url_saveProjectProcessTime;
            if(dataListType!=undefined && dataListType==1){//没原因
                options.$isHaveMemo = false;
                options.$timeInfo = {
                    startTime:dataStaTime,
                    endTime:dataEndTime
                };
                options.$saveTimeKeyVal=['startTime','endTime'];
            }else {
                options.$timeInfo = {
                    startTime:dataStaTime,
                    endTime:dataEndTime,
                    memo:dataMemo
                };
                options.$saveTimeKeyVal=['startTime','endTime','memo'];
            }
            options.$saveData = $data;
            options.$currentAppointmentDate=that.settings.$currentAppointmentDate;
            options.$okCallBack = function (data) {
                that.init();
                if(that.settings.$okCallBack!=null){
                    that.settings.$okCallBack();
                }
            };
            $('body').m_inputProcessTime(options);
        }
        //删除变更
        ,_delTaskScheduleChange:function (obj) {
            var that = this;
            S_layer.confirm('您确定要删除吗？', function () {

                that._delChangeTimeById(obj);

            }, function () {
            });
        }
        //删除变更请求
        ,_delChangeTimeById:function (obj) {
            var that = this;
            var options = {};
            options.url=restApi.url_saveProjectProcessTime+'/'+$(obj).attr('data-id');
            var seq = $(obj).attr('data-seq');
            m_ajax.get(options,function (response) {

                if(response.code=='0'){
                    if(seq==0){
                        S_layer.close($(obj));
                        var options = {};
                        options.$projectId = that.settings.$projectId;
                        options.$title = '设置计划进度';
                        options.$isHaveMemo = false;
                        options.$isOkSave = true;
                        options.$saveDataUrl = restApi.url_saveProjectProcessTime;
                        var $data = {};
                        $data.targetId = that.settings.$taskId;
                        $data.type = that.settings.$type;
                        options.$saveData = $data;
                        options.$saveTimeKeyVal=['startTime','endTime'];
                        options.$okCallBack = function (data) {

                            if(that.settings.$okCallBack!=null){
                                that.settings.$okCallBack();
                            }
                        };
                        $('body').m_inputProcessTime(options);
                    }else {
                        that.init();
                        if(that.settings.$okCallBack!=null){
                            that.settings.$okCallBack();
                        }
                    }
                }else {
                    S_layer.error(response.info);
                }
            })
        }
        //按钮事件绑定
        ,_bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click',function () {
                var _this = this,dataAction = $(this).attr('data-action');
                if(dataAction=='addScheduleProgressChange'){//添加变更

                    var dataType = $(_this).attr('data-type');
                    if(dataType==3){
                        that._taskScheduleUpdate(_this);
                    }else{//添加变更，修改变更
                        that._taskScheduleChange(_this);
                    }

                    return false;
                }else if(dataAction=='delScheduleProgressChange'){//删除变更
                    that._delTaskScheduleChange(_this);
                    return false;
                }
            });
           /* $(that.element).find('.taskParticipantTr input[name="userName"]').on('click',function () {
                that._choseTaskParticipant($(this));
            });*/
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


