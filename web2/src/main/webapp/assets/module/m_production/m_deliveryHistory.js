/**
 * 交付历史
 * Created by wrb on 2018/7/14.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_deliveryHistory",
        defaults = {
            $isDialog:true,//是否弹窗展示
            $issueTaskId:null,//任务节点ID
            $taskId:null,//任务ID
            $projectId:null,//项目ID
            $saveCallBack:null

        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._noticeInfo = null;
        this._name = pluginName;
        this._listDeliver = [];//交付历史缓存

        this._deliverHistoryIdsByEdit = [];//当前编辑状态下人历史交付记录
        this._currentUserId = window.currentUserId;//当前用户ID

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderPage();

        }
        ,renderPage:function () {
            var that = this;
            var currDate = getNowDate();
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'交付历史',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '1000',
                    height:'250',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancelText:'关闭',
                    cancel:function(){
                    }

                },function(d){//加载html后触发

                    that.element = $('div[id="content:'+d.id+'"] .dialogOBox');
                    that.renderListDeliver();
                });
            }else {//不以弹窗编辑
                that.renderListDeliver();
            }
        }
        //请求交付历史数据并渲染
        ,renderListDeliver:function () {
            var that = this;
            var options={};
            options.classId = '.ibox-content';
            options.url=restApi.url_listDeliver;
            options.postData = {};
            options.postData.isDelivery = 1;
            options.postData.issueTaskId=that.settings.$issueTaskId;
            m_ajax.postJson(options,function (response) {

                if(response.code=='0'){
                    var html = template('m_production/m_deliveryHistory',{listDeliver:response.data});
                    $(that.element).html(html);
                    that.bindActionClick();
                    that.initXeditable();

                    //禁用
                    $(that.element).find('a[data-action="xeditable"]').editable('toggleDisabled');

                    //编辑刷新 开放保留的编辑项
                    if(that._deliverHistoryIdsByEdit.length>0){
                        $.each(that._deliverHistoryIdsByEdit,function (i,item) {
                            that.dealEditStatus($(that.element).find('tr[data-id="'+item+'"]'),1);
                        });
                    }

                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        ,initXeditable:function () {
            var that = this;
            $(that.element).find('a[data-action="xeditable"]').each(function () {

                var $this = $(this),$i = $this.closest('tr').attr('data-i'),type = $this.attr('data-action-type'),text = $.trim($this.text());

                var dataId = $this.closest('tr').attr('data-id');

                switch (type) {
                    case 'selectUser':
                        $this.click(function () {
                            if($this.hasClass('editable-disabled')){
                                return false;
                            }

                            var userList = [];
                            $this.find('span').each(function () {
                                userList.push({
                                    id:$(this).attr('data-id'),
                                    userName:$(this).text()
                                })
                            });
                            that.choseUser(userList,dataId);
                        });
                        break;
                    case 'time':

                        $this.click(function () {

                            if($this.hasClass('editable-disabled')){
                                return false;
                            }
                            var options = {};
                            options.popoverStyle = 'border: 0;border-radius: 0;box-shadow: none;position: relative;display: block;';
                            options.contentStyle = 'padding:0;';
                            options.type = 'inline';
                            options.hideArrow = true;
                            options.hideElement = true;
                            options.content = template('m_xeditable/m_xeditable_time', {text:$this.attr('data-value')});
                            options.onShown = function ($popover) {
                                $popover.find('.fa-calendar').on('click',function () {
                                    $(this).parent().prev().click();
                                });
                                that.time_validate($popover);
                            };
                            options.onClose = function ($popover) {
                                var flag = $popover.find('form').valid();
                                if (!flag) {
                                    return false;
                                }else {
                                    var param = {};
                                    param.deadline = $popover.find('input[name="time"]').val();
                                    param.id = dataId;
                                    that.save(param);
                                }
                            };
                            options.onClear = function ($popover) {

                            };
                            $this.m_popover(options, true);
                        });


                        break;
                    case 'select':
                        $this.editable({//编辑
                            type: 'select',
                            mode: 'inline',
                            value: text,
                            source: [
                                {value: 0, text: '未完成'},
                                {value: 1, text: '已完成'}
                            ],
                            showbuttons: false,
                            success: function (response, newValue) {
                                var param = {};
                                param.isFinished = newValue;
                                param.id = dataId;
                                that.save(param);
                            },
                            display: function (value, sourceData) {
                            }
                        });
                        break;
                    default:
                        var options = {//编辑
                            type: type,
                            mode: 'inline',
                            value: text,
                            emptytext: '',
                            onblur: 'submit',
                            showbuttons: false,
                            success: function (response, newValue) {

                                var field = $this.attr('data-field');
                                if (newValue != text) {
                                    var param = {};
                                    param[field] = newValue;
                                    param.id = dataId;
                                    that.save(param);
                                }
                            },
                            display: function (value, sourceData) {
                            },
                            validate: function (value) {
                                if ($.trim(value) == '') {
                                    S_toastr.error('不可为空！');
                                    return '不能为空';
                                }
                                if (value.length > 50) {
                                    S_toastr.error('内容超出字数限制！');
                                    return '内容超出字数限制！';
                                }
                            }
                        };
                        if(type=='textarea'){
                            $.extend(options, {rows:2});
                        }
                        $this.editable(options);
                        break;
                }
            });
        }
        //保存接口
        ,save:function (param) {
            var options={},that=this;
            options.url=restApi.url_sendArchivedFileNotifier;
            options.postData = {};
            options.postData.targetId = that.settings.$taskId;
            options.postData.projectId = that.settings.$projectId;
            options.postData.companyId = window.currentCompanyId;
            options.postData.userId = window.currentUserId;

            $.extend(options.postData, param);
            m_ajax.postJson(options,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    that.renderListDeliver();
                }else {
                    S_dialog.error(response.info);
                }
            });
        }
        //选择负责人
        ,choseUser:function (userSelectedList,dataId) {
            var that=this,options = {};
            options.title = '选择负责人';
            options.selectedUserList = userSelectedList;
            options.url = restApi.url_getOrgTree;
            options.delSelectedUserCallback = function () {
            };
            options.saveCallback = function (data) {

                var userArr = [];
                if(data!=null && data.selectedUserList!=null && data.selectedUserList.length>0){
                    $.each(data.selectedUserList,function (i,item) {
                        userArr.push({
                            id:item.id,
                            name:item.userName
                        });
                    });
                    var param = {};
                    param.userArr = userArr;
                    param.id = dataId;
                    that.save(param);

                }else{

                    S_toastr.warning('请选择负责人！');
                    return false;
                }


            };
            options.renderTreeCallBack = function () {
            };
            $('body').m_orgByTree(options);
        }
        //更新编辑状态 $ele = tr标签 ,t=0=处理编辑记录ID
        ,dealEditStatus:function ($ele,t) {
            var that = this;
            var dataId = $ele.attr('data-id');
            var isPush = 0;//是否记录下来 editable-disabled是tr编辑项同步的

            $ele.find('a[data-action="xeditable"]').each(function () {
                var type = $(this).attr('data-action-type');
                switch (type){
                    case 'time':
                    case 'selectUser':
                        if($(this).hasClass('editable-disabled')){//开放编辑
                            $(this).removeClass('editable-disabled');
                            isPush = 1;
                        }else{
                            isPush = 0;
                            $(this).addClass('editable-disabled');
                        }
                        break;
                    default:
                        $(this).editable('toggleDisabled');
                        break;
                }
            });

            if(t==0){
                if(isPush==1){
                    that._deliverHistoryIdsByEdit.push(dataId);
                }else{
                    if(that._deliverHistoryIdsByEdit.indexOf(dataId)>-1){
                        that._deliverHistoryIdsByEdit.splice(that._deliverHistoryIdsByEdit.indexOf(dataId), 1);
                    }
                }
            }
        }
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').off('click').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');
                var dataId = $this.closest('tr').attr('data-id');
                switch (dataAction){
                    case 'edit'://点击编辑,初始化编辑状态
                        that.dealEditStatus($this.closest('tr'),0);
                        break;

                    case 'delete'://删除交付

                        S_dialog.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                            var option = {};
                            option.url = restApi.url_deleteDeliver;
                            option.postData = {};
                            option.postData.id = dataId;
                            option.postData.accountId = that._currentUserId;
                            m_ajax.postJson(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('删除成功！');
                                    that.renderListDeliver();
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });

                        }, function () {
                        });
                        break;
                }
            });
        }
        //时间不为空验证
        ,time_validate:function ($ele) {
            $ele.find('form').validate({
                rules: {
                    time:{
                        required:true
                    }

                },
                messages: {
                    time:{
                        required:'请选择截止日期!'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.appendTo(element.closest('form'));
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
