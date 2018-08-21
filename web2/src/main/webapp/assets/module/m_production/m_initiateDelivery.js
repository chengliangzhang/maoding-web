/**
 * 发起交付
 * Created by wrb on 2018/7/14.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_initiateDelivery",
        defaults = {
            $isDialog:true,//是否弹窗展示
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
        this._userSelectedList = [];//选择的负责人列表
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderPage(function () {

                that.formSubmit_validate();
                that.bindActionClick();
            });

        }
        ,renderPage:function (callBack) {
            var that = this;
            var currDate = getNowDate();
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'新的交付',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '600',
                    height:'250',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    ok:function(){

                        if ($(that.element).find('form.form-horizontal').valid()) {

                            var option  = {};
                            option.classId = '#content-right';
                            option.url = restApi.url_sendArchivedFileNotifier;
                            var $data = $(that.element).find('form.form-horizontal').serializeObject();
                            var userArr = [];
                            if(that._userSelectedList!=null && that._userSelectedList.length>0){
                                $.each(that._userSelectedList,function (i,item) {
                                    userArr.push({
                                        id:item.id,
                                        name:item.userName
                                    });
                                });
                            }
                            option.postData = {
                                targetId: that.settings.$taskId,
                                projectId: that.settings.$projectId,
                                companyId: window.currentCompanyId,
                                userId: window.currentUserId,
                                userArr: userArr
                            };
                            $.extend(option.postData, $data);
                            m_ajax.postJson(option,function (response) {
                                if(response.code=='0'){
                                    S_toastr.success('操作成功');

                                    if(that.settings.$saveCallBack!=null){
                                        that.settings.$saveCallBack();
                                    }
                                }else {
                                    S_dialog.error(response.info);
                                }
                            });

                        } else {
                            return false;
                        }
                    },
                    okText:'保存',
                    cancelText:'取消',
                    cancel:function(){
                    }

                },function(d){//加载html后触发

                    that.element = $('div[id="content:'+d.id+'"] .dialogOBox');
                    var html = template('m_production/m_initiateDelivery',{currDate:currDate});
                    $(that.element).html(html);

                    if(callBack!=null)
                        callBack();
                });
            }else {//不以弹窗编辑
                var html = template('m_production/m_confirmCompletion', {currDate: currDate});
                $(that.element).html(html);
                if(callBack!=null)
                    callBack();
            }
        }

        //选择负责人
        ,choseUser:function () {
            var that=this,options = {};
            options.title = '选择负责人';
            options.selectedUserList = that._userSelectedList;
            options.url = restApi.url_getOrgTree;
            options.delSelectedUserCallback = function () {
            };
            options.saveCallback = function (data) {
                console.log(data)

                that.renderPersonInChargeHtml(data);

            };
            options.renderTreeCallBack = function () {
            };
            console.log(options);
            $('body').m_orgByTree(options);
        }
        //渲染添加负责人html
        ,renderPersonInChargeHtml:function (data) {
            var that = this;

            if(data!=null && data.selectedUserList!=null && data.selectedUserList.length>0){

                that._userSelectedList = data.selectedUserList;
                $(that.element).find('span.span-box').remove();//删除原人员html
                $.each(data.selectedUserList,function (i,item) {//循环写入添加人员html
                    var iHtml = '<span class="span-box m-r-xs bg-muted p-xxs f-s-12">' +
                        '<span>'+item.userName+'</span>' +
                        '<a class="curp" href="javascript:void(0)" data-id="'+item.id+'" data-i="'+i+'" data-action="delPersonInCharge" >' +
                        '<i class="fa fa-times color-red"></i>' +
                        '</a></span>';

                    $(that.element).find('a[data-action="addPersonInCharge"]').before(iHtml);

                });

                $(that.element).find('a[data-action="delPersonInCharge"]').off('click').on('click',function () {
                    var i = $(this).attr('data-i');
                    $(this).parent('span').remove();
                    that._userSelectedList.splice(i, 1);
                    return false;
                });
            }
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').off('click').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'addPersonInCharge'://添加负责人
                        that.choseUser();
                        break;
                }

            });
            //时间图标事件
            $(that.element).find('.fa-calendar').on('click',function () {
                $(this).parent().prev().click();
            });
        }
        //验证
        , formSubmit_validate: function () {
            var that = this;
            $(that.element).find('form').validate({
                ignore:'',
                rules: {
                    name: {
                        required:true,
                        maxlength:50
                    },
                    description: 'required',
                    endTime: 'required',
                    personInCharge:{
                        ckPersonInCharge:true
                    }
                },
                messages: {
                    name: {
                        required:'请输入名称！',
                        maxlength:'请控制在50个字符以内！'
                    },
                    description: '请输入说明！',
                    endTime: '请选择时间！',
                    personInCharge:{
                        ckPersonInCharge:'请选择负责人！'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.appendTo(element.closest('.col-sm-10'));
                }
            });
            $.validator.addMethod('ckPersonInCharge', function(value, element) {
                var isOk = true;
                if(that._userSelectedList==null || that._userSelectedList.length==0){
                    isOk = false;
                }
                return  isOk;
            }, '请选择负责人！');
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
