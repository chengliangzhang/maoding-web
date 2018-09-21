/**
 * 审批管理-设置流程
 * Created by wrb on 2018/8/3.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_mgt_setProcess",
        defaults = {
            type:null,
            key:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentUserId = window.currentUserId;
        this._currentCompanyId = window.currentCompanyId;
        this._processDetail = null;//当前流程信息
        this._editFixedProcess = [];//固定流程保存数据
        this._editCondProcess = [];//条件流程保存数据
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderContent(function (data) {
                that.initICheck();
                that.bindActionClick();
                that.approvalRemoveIconHover();
                $(that.element).find('input[type="radio"][data-type="'+data.type+'"]').iCheck('check');
            });
        }
        //渲染列表内容
        ,renderContent:function (callBack) {
            var that = this;
            var option = {};
            option.classId = '#content-right';
            option.url = restApi.url_prepareProcessDefine;
            option.postData = {};
            option.postData.key = that.settings.key;
            option.postData.currentCompanyId = that._currentCompanyId;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    that._processDetail = $.extend(true, {}, response.data);

                    //生成固定流程保存数据
                    if(that._processDetail.type==2){
                        that._editFixedProcess = $.extend(true, [], that._processDetail.flowTaskGroupList);
                    }

                    //空数据时，创建默认数据
                    if(that._editCondProcess==null || that._editFixedProcess[0]==null || that._processDetail.type!=2){
                        that._editFixedProcess = [];
                        that._editFixedProcess[0] = {};
                        that._editFixedProcess[0].flowTaskList = [];
                        that._editFixedProcess[0].maxValue = null;
                        that._editFixedProcess[0].minValue = null;
                        that._editFixedProcess[0].title = null;
                    }


                    //生成条件流程保存数据
                    that._editCondProcess = $.extend(true, [], that._processDetail.flowTaskGroupList);

                    //空数据时，创建默认数据
                    if(that._editCondProcess==null || that._processDetail.type!=3){
                        that._editCondProcess = [];
                        that._editCondProcess[0] = {};
                        that._editCondProcess[0].flowTaskList = [];

                    }
                    console.log(that._editFixedProcess);
                    console.log(that._editCondProcess);

                    var html = template('m_approval/m_approval_mgt_setProcess',{
                        name:response.data.name,
                        key:response.data.key,
                        fixedProcess:that._editFixedProcess,
                        condProcess:that._editCondProcess,
                        optionalConditionList:that._processDetail.optionalConditionList
                    });
                    $(that.element).html(html);


                    if(callBack)
                        callBack(response.data);

                } else {
                    S_layer.error(response.info);
                }
            })

        }
        //初始化iCheck
        ,initICheck:function () {
            var that = this;
            var ifChecked = function (e) {
                var type = $(this).attr('data-type');
                $(that.element).find('.panel[data-type]').hide();
                $(that.element).find('.panel[data-type="'+type+'"]').show();
                if(type==3){
                    $(that.element).find('a[data-action="setApprovalCondition"]').show();
                }else{
                    $(that.element).find('a[data-action="setApprovalCondition"]').hide();
                }
            };
            var ifUnchecked = function (e) {
            };
            var ifClicked = function (e) {

            };
            $(that.element).find('input[name^="iCheck"]').iCheck({
                checkboxClass: 'icheckbox_square-pink',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked).on('ifClicked',ifClicked);

        }
        //保存设置
        ,saveProcess:function () {
            var that =this;
            var type = $(that.element).find('input[type="radio"]:checked').attr('data-type');

            var isError = false;

            if(type==2){

                if(that._editFixedProcess==null)
                    isError = true;
                if(that._editFixedProcess && (that._editFixedProcess[0].flowTaskList==null || that._editFixedProcess[0].flowTaskList.length==0))
                    isError = true;

                that._processDetail.flowTaskGroupList = that._editFixedProcess;

            }else if(type==3){

                if(that._editCondProcess==null )
                    isError = true;
                if(that._editFixedProcess){
                    $.each(that._editCondProcess,function (i,item) {
                        if(item.flowTaskList==null || item.flowTaskList.length==0){
                            isError = true;
                            return false;
                        }
                    })
                }

                that._processDetail.flowTaskGroupList = that._editCondProcess;

            }else{

                that._processDetail.flowTaskGroupList = [];
            }

            if(isError){
                S_toastr.error('请设置审批人！');
                return false;
            }

            var option = {};
            option.classId = '#content-right';
            option.url = restApi.url_changeProcessDefine;
            option.postData = {};
            option.postData = that._processDetail;
            option.postData.type = type;

            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    S_toastr.success('保存成功！');

                } else {
                    S_layer.error(response.info);
                }
            });
        }
        //选择人员
        ,choseUser:function ($this) {
            var that = this,options = {};
            options.title = '添加审批人员';
            options.selectedUserList = [];
            options.url = restApi.url_getOrgTree;
            options.saveCallback = function (data) {

                var len = $this.closest('.panel-body').find('.approver-outbox').length;
                var isAppendArrow = 0;
                if(len>0 && data.selectedUserList &&  data.selectedUserList.length>0){//存在审批人,添加时，前面加箭头
                    isAppendArrow = 1;
                }
                var html = template('m_approval/m_approval_mgt_setProcess_approver',{approverList:data.selectedUserList,isAppendArrow:isAppendArrow});
                $this.parent().before(html);
                that.approvalRemoveIconHover();
                that.bindActionClick();//重新绑定事件，针对新添加的审批人删除事件

                var i = $this.closest('div.panel').attr('data-i');
                var type = $(that.element).find('input[type="radio"]:checked').attr('data-type');

                var flowTaskList = [];
                $.each(data.selectedUserList,function (i,item) {
                    var userList = [];
                    userList.push({id:item.id});
                    flowTaskList.push({candidateUserList:userList});
                });

                //固定流程
                if(type==2){
                    that._editFixedProcess[0].flowTaskList =  that._editFixedProcess[0].flowTaskList.concat(flowTaskList);
                }
                //条件流程
                if(type==3){

                    if(that._editCondProcess[i].flowTaskList==null)
                        that._editCondProcess[i].flowTaskList = [];

                    that._editCondProcess[i].flowTaskList =  that._editCondProcess[i].flowTaskList.concat(flowTaskList);
                }

            };
            $('body').m_orgByTree(options);
        }
        //删除审批人图标hover事件
        ,approvalRemoveIconHover:function ($ele) {
            var that = this;
            if(isNullOrBlank($ele))
                $ele = $(that.element);

            $ele.find('.approver-box').hover(function () {
                $(this).find('.approver-remove').show();
            },function () {
                $(this).find('.approver-remove').hide();
            });
        }
        //删除审批人处理
        ,removeApprover:function ($this) {
            var that = this;
            var $ele = $this.closest('.approver-outbox');
            //var i = $this.closest('.approver-outbox').index('.approver-outbox');
            var i = $this.closest('.panel-body').find('.approver-outbox').index($this.closest('.approver-outbox'));

            var panelI = $this.closest('.panel').attr('data-i');
            var len = $this.closest('.panel-body').find('.approver-outbox').length;
            var type = $(that.element).find('input[type="radio"]:checked').attr('data-type');

            //固定流程
            if(type==2){
                that._editFixedProcess[0].flowTaskList.splice(i,1);
            }

            if(type==3){
                that._editCondProcess[panelI].flowTaskList.splice(i,1);
            }

            if(len==i+1){//删除的是最后一个
                $ele.prev('.arrow-icon').remove();
                $ele.remove();
            }else{
                $ele.next('.arrow-icon').remove();
                $ele.remove();
            }
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'setApprovalCondition'://设置审批流程

                        var option = {};
                        option.type = $this.attr('data-type');
                        option.processData = that._processDetail;
                        option.oKCallBack = function (data) {

                            that._editCondProcess = data;
                            var html = template('m_approval/m_approval_mgt_setProcess_flow',{flowTaskGroupList:data});
                            $(that.element).find('#flowTaskGroupList').html(html);
                            that.bindActionClick();//重新绑定事件
                        };
                        $('body').m_approval_mgt_setProcessCondition(option,true);
                        break;
                    case 'addReview'://添加人员

                        that.choseUser($this);

                        break;
                    case 'back'://返回

                        $(that.element).m_approval_mgt({}, true);

                        break;

                    case 'save'://保存

                        that.saveProcess();
                        break;

                    case 'removeApprover'://删除审批人

                        that.removeApprover($this);
                        break;
                }
            })
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
