/**
 * 审批管理
 * Created by wrb on 2018/8/2.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_mgt",
        defaults = {
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentUserId = window.currentUserId;
        this._currentCompanyId = window.currentCompanyId;

        this._dataList = [];
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_approval/m_approval_mgt',{});
            $(that.element).html(html);

            that.renderContent(function () {
                that.initICheck();
                that.bindActionClick();
            });


        }
        //渲染列表内容
        ,renderContent:function (callBack) {
            var that = this;
            var option = {};
            option.classId = '#content-right';
            option.url = restApi.url_listProcessDefine;
            option.postData = {};
            option.accountId = that._currentUserId;
            option.currentCompanyId = that._currentCompanyId;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    that._dataList = response.data;
                    var html = template('m_approval/m_approval_mgt_content',{approvalList:response.data});
                    $(that.element).find('#approvalManagement').html(html);
                    if(callBack)
                        callBack();

                } else {
                    S_layer.error(response.info);
                }
            })

        }
        //初始化iCheck
        ,initICheck:function () {
            var that = this;
            var ifChecked = function (e) {
            };
            var ifUnchecked = function (e) {
            };
            var ifClicked = function (e) {
                var param = {};
                if ($(this).is(':checked')) {
                    $(this).iCheck('uncheck');
                    param.status = 0;
                }else{
                    $(this).iCheck('check');
                    param.status = 1;
                }
                param.id = $(this).closest('tr').attr('data-id');
            };
            $(that.element).find('input[name^="iCheck"]').iCheck({
                checkboxClass: 'icheckbox_square-pink',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked).on('ifClicked',ifClicked);
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action],button[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                var dataId = $this.closest('tr').attr('data-id');
                var dataPid = $this.closest('tr').attr('data-pid');

                //获取节点数据
                var dataItem = null;

                if(isNullOrBlank(dataPid)){//当前是分组

                    dataItem = getObjectInArray(that._dataList,dataId);

                }else{//当前是子集

                    var pidDataItem = getObjectInArray(that._dataList,dataPid);
                    dataItem = getObjectInArray(pidDataItem.processDefineList,dataId);
                }

               switch (dataAction){
                   case 'setProcess':

                        var option = {};
                        option.key = $this.attr('data-key');
                        option.type = $this.attr('data-type');
                        $(that.element).m_approval_mgt_setProcess(option,true);
                       return false;
                       break;

                   case 'addApproval'://添加审批

                       $('body').m_form_template_settings({type:1},true);
                       return false;
                       break;
                   case 'addGroup'://添加分组

                       $('body').m_approval_mgt_addGroup({
                           saveCallBack:function () {
                               that.init();
                           }
                       },true);
                       return false;
                       break;
                   case 'editGroup'://编辑分组

                       $('body').m_approval_mgt_addGroup({
                           title:'编辑分组',
                           dataInfo:dataItem,
                           saveCallBack:function () {
                               that.init();
                           }
                       },true);
                       return false;
                       break;
                   case 'delGroup'://删除分组

                       S_layer.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                           var option = {};
                           option.url = restApi.url_deleteDynamicFormGroup ;
                           option.postData = {
                               id:dataId
                           };
                           m_ajax.postJson(option, function (response) {
                               if (response.code == '0') {
                                   S_toastr.success('删除成功！');
                                   that.init();
                               } else {
                                   S_layer.error(response.info);
                               }
                           });

                       }, function () {
                       });
                       return false;
                       break;
                   case 'moveToGroup'://编辑分组

                       $('body').m_approval_mgt_moveToGroup({
                           dataInfo:dataItem,
                           saveCallBack:function () {
                               that.init();
                           }
                       },true);
                       return false;
                       break;
               }

            });
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
