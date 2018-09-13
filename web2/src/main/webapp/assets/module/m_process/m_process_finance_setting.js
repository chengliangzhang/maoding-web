/**
 * 项目收支流程设置
 * 合同回款=2；技术审查费收款=3；技术审查费付款=4；合作设计费收款=5,；合作设计费付款=6
 * Created by wrb on 2018/7/17.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_process_finance_setting",
        defaults = {
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._selectedOrg = null;//当前选中组织
        this._processList = [];//请求返回的数据缓存
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_process/m_process_finance_setting',{});
            $(that.element).html(html);


            var option = {};
            option.$renderType = 1;
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                that.renderContent();
            };
            option.$renderCallBack = function (data) {

            };
            $(that.element).find('#left-box').m_org_chose_byTree(option);

        }

        //渲染右边（项目收支流程设置列表）
        ,renderContent:function () {
            var that = this;
            var option  = {};
            option.classId = '#content-right';
            option.url = restApi.url_getProcessByCompany;
            option.postData = {
                companyId:that._selectedOrg.id
            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    that._processList = response.data;
                    var html = template('m_process/m_process_finance_setting_content',{
                        receiveProcessList:response.data.receiveProcessList,
                        payProcessList:response.data.payProcessList
                    });
                    $(that.element).find('#right-box').html(html);
                    that.initICheck();
                    that.bindActionClick();

                }else {
                    S_layer.error(response.info);
                }
            });

        }
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
                that.saveChangeSelectionStatus(param);
            };
            $(that.element).find('input[name^="iCheck"]').iCheck({
                checkboxClass: 'icheckbox_square-pink',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked).on('ifClicked',ifClicked);
        }
        //更改选择状态
        ,saveChangeSelectionStatus:function (param) {
            var that = this;
            var option = {};
            option.classId = '#content-right';
            option.url = restApi.url_selectedProcessForProjectPay;
            option.postData = param;
            m_ajax.postJson(option, function (response) {
                if(response.code=='0'){
                    S_toastr.success('操作成功！');
                }else {
                    S_layer.error(response.info);
                }
            });
        }

        //事件绑定
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action],button[data-action]').off('click').on('click',function (e) {
                var $this = $(this),dataAction = $this.attr('data-action'),
                    $i = $this.closest('tr').attr('data-i');
                var dataId = $this.closest('tr').attr('data-id'),
                    dataProcessType = $this.closest('tr').attr('data-process-type'),
                    dataProcessId = $this.closest('tr').attr('data-process-id');

                switch (dataAction){
                    case 'add'://添加流程设置

                        var option = {};
                        option.$type = $this.attr('data-action-type');//0=收款，1=付款
                        option.$saveCallBack = function () {
                            that.renderContent();
                        };
                        $('body').m_process_finance_setting_add(option);

                        return false;
                        break;
                    case 'edit'://编辑流程设置

                        var option = {};
                        option.$type = $this.attr('data-action-type');//0=收款，1=付款
                        if(that._processList!=null && that._processList.receiveProcessList!=null && option.$type==0){
                            option.$processInfo = that._processList.receiveProcessList[$i];
                        }else if(that._processList!=null && that._processList.payProcessList!=null && option.$type==1){
                            option.$processInfo = that._processList.payProcessList[$i];
                        }else{
                            option.$processInfo = null;
                        }
                        option.$saveCallBack = function () {
                            that.renderContent();
                        };
                        $('body').m_process_finance_setting_add(option);
                        return false;
                        break;

                    case 'del'://删除流程

                        S_layer.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                            var option = {};
                            option.classId = '#content-right';
                            option.url = restApi.url_deleteProcessForProjectPay;
                            option.postData = {};
                            option.postData.id = dataId;
                            m_ajax.postJson(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('删除成功！');
                                    that.renderContent();
                                } else {
                                    S_layer.error(response.info);
                                }
                            });

                        }, function () {
                        });
                        break;

                    case 'itemEdit'://编辑列表项

                        window.location.hash = '/backstageMgt/financeSettingProcess?processType='+dataProcessType+'&processId='+dataProcessId;
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
