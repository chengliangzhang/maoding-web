/**
 * 发送归档通知
 * Created by wrb on 2018/1/12.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_docmgr_sendArchiveNotice",
        defaults = {
            $title:null,
            $isDialog:true,
            $selectedNodeObj:null,//当前选中对象
            $itemData:null,//编辑对象
            $saveCallBack:null//保存后事件
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
            var html = template('m_docmgr/m_docmgr_sendArchiveNotice',{
                selectedNodeObj:that.settings.$selectedNodeObj,
                nowDate: getNowDate()
            });
            that.initHtmlData(html,function () {
                that.save_validate();
                that.initSelect2();
            });
        }
        //初始化数据并加载模板
        ,initHtmlData:function (html,callBack) {
            var that = this;
            if(that.settings.$isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.$title||'发送归档通知',
                    area : '750px',
                    content:html,
                    cancel:function () {
                    },
                    ok:function () {

                        if($('form.sendArchiveNotice').valid()){
                            if(that.sendArchiveNotice()==false){
                                return false;
                            }
                        }else{
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
        //初始化select2
        ,initSelect2:function () {
            var that = this;
            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_getArchivedFileNotifier;
            option.postData = {
                companyId: window.currentCompanyId,
                projectId: that.settings.$selectedNodeObj.projectId,
                taskId: that.settings.$selectedNodeObj.taskId
            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    var staffArr = [],selectedArr=[];
                    var selectNameArr = response.data.selectName;
                    var defaultNameArr = response.data.defaultName;
                    if(selectNameArr!=null && selectNameArr.length>0){
                        $.each(selectNameArr, function (i, o) {
                            staffArr.push({
                                id: o.id,
                                text: o.userName
                            });
                        });
                    }
                    if(defaultNameArr!=null && defaultNameArr.length>0){
                        $.each(defaultNameArr, function (i, o) {
                            selectedArr.push(o.id);
                        });
                    }
                    $(that.element).find('select.js-example-disabled-results').select2({
                        tags: true,
                        tokenSeparators: [',', ' '],
                        multiple: true,
                        data: staffArr
                    });
                    $(that.element).find('select.js-example-disabled-results').val(selectedArr).trigger('change');
                    $(that.element).find('select.js-example-disabled-results').on("change", function(e) {
                        $(this).parent().find('.error').hide();
                    })



                }else {
                    S_layer.error(response.info);
                }
            })

        }
        //发送
        ,sendArchiveNotice:function (e) {
            var that = this;
            var userArr = [];
            var usersArr = $(that.element).find('select.js-example-disabled-results').select2("data");
            var taskName = $(that.element).find('input[name="taskName"]').val();
            var deadline = $(that.element).find('input[name="deadline"]').val();
            var remarks = $(that.element).find('textarea[name="remarks"]').val();
            if(usersArr!=null && usersArr.length>0){
                $.each(usersArr, function (i, o) {
                    var user = new Object;
                    user.id = o.id;
                    user.name = o.text;
                    userArr.push(user);
                });

            }
            if(usersArr==null || usersArr.length==0){
                S_toastr.error('当前任务没有发送人员，请在生产安排确定参与人员！');
                return false;
            }

            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_sendArchivedFileNotifier;
            option.postData = {
                targetId: that.settings.$selectedNodeObj.taskId,
                projectId: that.settings.$selectedNodeObj.projectId,
                companyId: window.currentCompanyId,
                id:that.settings.$selectedNodeObj.id,
                userId: window.currentUserId,
                taskName:taskName,
                deadline:deadline,
                remarks:remarks,
                userArr: userArr
            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('发送成功！');
                    if(that.settings.$saveCallBack!=null){
                        that.settings.$saveCallBack();
                    }
                }else {
                    S_layer.error(response.info);
                }
            })

        }
        //保存验证
        ,save_validate:function(){
            var that = this;
            $('form.sendArchiveNotice').validate({
                rules: {
                    taskName:{
                        required:true,
                        maxlength:50
                    },
                    users:{
                        required:true
                    },
                    deadline:{
                        required:true
                    }
                },
                messages: {
                    taskName:{
                        required:'请输入归档名称!',
                        maxlength:'归档名称不超过50位!'
                    },
                    users:{
                        required:'请选择发送人员!'
                    },
                    deadline:{
                        required:'请选择截止日期!'
                    }
                },
                errorPlacement:function(error,element){
                    error.appendTo(element.parent());
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
