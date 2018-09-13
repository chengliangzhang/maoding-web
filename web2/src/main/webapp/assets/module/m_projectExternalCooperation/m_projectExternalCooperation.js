/**
 * 项目－外部合作
 * Created by wrb on 2017/5/6.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_projectExternalCooperation",
        defaults = {
            $projectId:null,
            $projectName:null,
            $isManager:null//在当前项目是否是经营负责人1=是
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
            var option = {};
            option.url = restApi.url_getProjectPartnerList;
            option.classId = '';
            option.postData = {
                projectId: that.settings.$projectId,
                fromCompanyId: window.currentCompanyId
            };
            m_ajax.postJson(option, function (response) {
                if (response.code === '0') {

                    var isHasRoleOperate = 0;
                    if((that.settings.$isManager!=null && that.settings.$isManager==1)
                        || window.currentRoleCodes.indexOf('project_manager')>-1
                        || window.currentRoleCodes.indexOf('sys_enterprise_logout')>-1){
                        isHasRoleOperate = 1;
                    }

                    var html = template('m_projectExternalCooperation/m_projectExternalCooperation',{
                        projectPartnerList:response.data,
                        isHasRoleOperate:isHasRoleOperate,
                        projectName:that.settings.$projectName
                    });
                    $(that.element).html(html);
                    that._bindActionClick();

                } else {
                    S_layer.error(response.info);
                }
            });
        }

        //事件绑定
        ,_bindActionClick:function () {
            var that = this;

            $(that.element).find('a[data-action]').on('click',function () {
                var _this = this;
                var dataAction = $(_this).attr('data-action');
                switch (dataAction) {
                    case 'inviteExternalCooperation':

                        $('body').m_inviteExternalCooperation({
                            inviteType:3,
                            projectId:that.settings.$projectId,
                            saveCallBack:function () {
                                that._initHtml();
                            }
                        },true);
                        break;
                    case 'relieveRelationship':
                        $(_this).m_popover({
                            placement: 'left',
                            content: template('m_common/m_popover_confirm', {confirmMsg: '确定要解除外部合作关系吗？'}),
                            onSave: function ($popover) {
                                var option = {};
                                option.url = restApi.url_relieveRelationship+'/'+$(_this).attr('data-id');
                                m_ajax.get(option, function (response) {
                                    if (response.code == '0') {
                                        S_toastr.success('操作成功');
                                        that._initHtml();
                                    } else {
                                        S_layer.error(response.info);
                                    }
                                });
                            }
                        }, true);
                        break;

                    case 'resendSMS':

                        var option = {};
                        option.url = restApi.url_resendSMS+'/'+$(_this).attr('data-id');
                        m_ajax.get(option, function (response) {
                            if (response.code == '0') {
                                S_toastr.success('发送成功!');
                            } else {
                                S_layer.error(response.info);
                            }
                        });

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
