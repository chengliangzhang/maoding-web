/**
 * Created by wrb on 2016/12/15.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_firstCreateOrg",
        defaults = {
            orgId: '',
            userUrl: '',
            saveOrgCallback: null,
            showPre: null
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
            if (that.settings.showPre === '1')
                that.initPre();
            else
                that.initMain();
        }
        , initPre: function () {
            var that = this;
            var html = template('m_org/m_firstCreateOrg_pre', {});
            $(that.element).html(html);
            $(that.element).find('a[data-action="startCreateOrg"]').click(function (e) {
                that.initMain();
            });
        }
        , initMain: function () {
            var that = this;
            that.initData(function () {
                that.bindActionClick();
                that.bindKeyDownEnter();
                that.bindCollapase();

                //加载省市区
                $("#citysBox").citySelect({
                    nodata: "none",
                    required: false
                });

            });
        }
        //敲打回车enter键提交请求
        ,bindKeyDownEnter: function () {
            var that = this;
            $(that.element).find('input').keydown(function () {
                if (event.keyCode == 13){
                    that.saveOrg();
                    preventDefault(event);
                }
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
                    // $(that.element).swal({
                    //     title: "Welcome in Alerts",
                    //     text: "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                    // });
                    var html = template('m_org/m_firstCreateOrg', {serverTypeList: response.data});
                    $(that.element).html(html);
                    that.saveCreatOrg_validate();
                    /*that.addMask();*/
                    if (callback != null) {
                        callback();
                    }
                } else {
                    S_dialog.error(response.info);
                }
            })
        }
        //给头部可点击按钮增加遮罩层
        , addMask: function () {
            var that = this;
            var $top = $('body');
            var html = '<div style="position: fixed;top: 0;z-index: 10000;width:100%;height:100%;background: rgba(0,0,0,0.4)">';
            html += '</div>';
            $top.append(html);
        }
        //给’更多‘，’收起‘绑定事件
        , bindCollapase: function () {
            // Collapse ibox function
            $('.collapse-link').on('click', function () {
                var ibox = $(this).closest('div.ibox');
                var button = $(this).find('i');
                var content = ibox.find('div.ibox-content');
                var text = $(this).text();
                content.slideToggle(200);
                button.toggleClass('fa-chevron-down').toggleClass('fa-chevron-up');
                $(this).find('.text').text($.trim(text) == '更多' ? '收起' : '更多');
                // ibox.toggleClass('').toggleClass('border-bottom');
                setTimeout(function () {
                    ibox.resize();
                    ibox.find('[id^=map-]').resize();
                }, 50);
            });
        }
        //保存组织
        , saveOrg: function () {
            var that = this;
            var option = {};
            option.url = restApi.url_registerCompany;
            option.postData = $('form.createOrgBox').serializeObject();
            option.postData.companyShortName = option.postData.companyName;
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
