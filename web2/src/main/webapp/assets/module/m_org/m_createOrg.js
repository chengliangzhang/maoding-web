/**
 * Created by wrb on 2016/12/15.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_createOrg",
        defaults = {
            orgId: '',
            userUrl: '',
            saveOrgCallback: null
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
            that.initData(function () {
                that.bindActionClick();

                //加载省市区
                $("#citysBox").citySelect({
                    nodata: "none",
                    required: false
                });

            });

        }
        //初始化数据并加载模板
        , initData: function (callback) {
            var that = this;
            var option = {};
            option.url = restApi.url_projectType;
            option.postData = {};
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    var html = template('m_org/m_createOrg', {serverTypeList: response.data});
                    $(that.element).html(html);
                    that.saveCreatOrg_validate();
                    if (callback != null) {
                        callback();
                    }
                } else {
                    S_dialog.error(response.info);
                }
            })
        }
        //保存组织
        , saveOrg: function () {
            var that = this;
            var option = {};
            option.url = restApi.url_registerCompany;
            option.postData = $('form.createOrgBox').serializeObject();
            option.postData.companyShortName=option.postData.companyName;
            var serverTypeStr = '';
            var i = 0;
            $('form.createOrgBox input[name="serverType"]:checked').each(function () {
                serverTypeStr += $(this).val() + ',';
                i++;
            });
            if (i > 0) {
                serverTypeStr = serverTypeStr.substring(0, serverTypeStr.length - 1);
            }
            option.postData.serverType = serverTypeStr;
            if ($('form.createOrgBox').valid()) {
                $(that.element).find('a[data-action="saveOrg"]').removeAttr('data-action');
                $_loading.show('form.createOrgBox');
                m_ajax.postJson(option, function (response) {
                    if (response.code == '0') {
                        $_loading.close('form.createOrgBox');
                        S_toastr.success('保存成功！');
                        if (that.settings.saveOrgCallback != null) {
                            that.settings.saveOrgCallback(response.data);
                        }
                    } else {
                        S_dialog.error(response.info);
                    }
                });
            }
        }
        //按钮事件绑定
        , bindActionClick: function () {
            var that = this;
            $('form.createOrgBox a[data-action]').on('click', function (event) {
                var dataAction = $(this).attr('data-action');
                if (dataAction == "saveOrg") {//保存组织
                    that.saveOrg();
                    return false;
                }
            });
        }
        , saveCreatOrg_validate: function () {
            var that = this;
            $('form.createOrgBox').validate({
                rules: {
                    companyName: {
                        required: true,
                        maxlength: 50,
                        isEmpty:true
                    }
                },
                messages: {
                    companyName: {
                        required: '请输入组织名称!',
                        maxlength: '组织名称不超过50位!',
                        isEmpty:'请输入组织名称!'
                    }
                }
            });
            // 名称空格验证
            jQuery.validator.addMethod("isEmpty", function (value, element) {
                if($.trim(value)==''){
                    return false;
                }else{
                    return true;
                }

            }, "请输入组织名称!");
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
