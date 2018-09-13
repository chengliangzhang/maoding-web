/**
 * Created by Wuwq on 2017/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_historyData",
        defaults = {};

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this._creatorOrgId = null;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._render();
        }
        , _renderSelectOrg: function ($select) {
            var option = {
                url: restApi.url_listCompanyAndChildren
            };
            m_ajax.getJson(option, function (res) {
                if (res.code === '0') {
                    var data = [];
                    $.each(res.data, function (i, o) {
                        data.push({id: o.id, text: o.companyName});
                    });
                    $select.select2({
                        width: '200px',
                        allowClear: false,
                        language: "zh-CN",
                        minimumResultsForSearch: Infinity,
                        data: data
                    });
                } else
                    S_toastr.error(res.info);
            });
        }
        , _render: function () {
            var that = this;
            var html = template('m_historyData/m_historyData', {});
            $(that.element).html(html);

            that._renderSelectOrg($(that.element).find('#selectOrg'));

            $(that.element).find('button[data-action="downloadTemplate"]').click(function () {
                downLoadFile({
                    url:window.cdnUrl+'/cs/tpl/importProject.xlsx'
                });
                return false;
            });

            /*$(that.element).find('button[data-action="uploadFile"]').click(function () {
                S_toastr.success('文件上传成功，正在生成预览...');
                setTimeout(function () {
                    that._renderStep2();
                }, 2000);
            });
            return;*/

            $(that.element).find('button[data-action="uploadFile"]').m_fileUploader({
                server: restApi.url_historyData_importProjects,
                fileExts: 'xls,xlsx',
                fileSingleSizeLimit: 20 * 1024 * 1024,
                formData: {},
                innerHTML: '上传文件',
                loadingId: '#content-box .ibox',
                uploadBeforeSend: function (object, data, headers) {
                    data.creatorOrgId = $(that.element).find('#selectOrg').val();
                    that._creatorOrgId = data.creatorOrgId;
                },
                uploadSuccessCallback: function (file, res) {
                    if (res.code === '0') {
                        S_toastr.success('文件上传成功，正在生成预览...');
                        setTimeout(function () {
                            that._renderStep2(res.data);
                        }, 2000);
                    } else {
                        S_toastr.error(res.info);
                    }
                }
            }, true);
        }
        , _renderStep2: function (data) {
            var that = this;

            var list = [];
            var index = 0;
            if (data.invalidCount && data.invalidCount > 0) {
                $.each(data.invalidList, function (i, o) {
                    list.push(o);
                    o.valid = false;
                    o.index = index;
                    index += 1;
                });
            }
            if (data.validCount && data.validCount > 0) {
                $.each(data.validList, function (i, o) {
                    list.push(o);
                    o.valid = true;
                    o.index = index;
                    index += 1;
                });
            }

            var showSubmit = false;
            var msg = '';
            if (data.validCount > 0) {
                showSubmit = true;
                msg = '已上传文档里发现 有效数据：' + data.validCount + ' 项';
                if (data.invalidCount > 0)
                    msg += '，无效数据：' + data.invalidCount + ' 项';
            }
            else
                msg = '已上传文档里找不到任何有效数据';

            var html = template('m_historyData/m_historyData_step2', {list: list, msg: msg, showSubmit: showSubmit});
            $(that.element).find('.step-container').html(html);
            $(that.element).find('div[data-step="1"]').removeClass('active').addClass('done');
            $(that.element).find('div[data-step="2"]').addClass('active');
            $(that.element).find('.footable').footable();
            $(that.element).find('.footable a[data-toggle="tooltip"]').tooltip();

            $(that.element).find('button[data-action="submit"]').click(function () {

                S_layer.confirm('你确定要提交吗？', function () {

                    var option = {
                        url: restApi.url_historyData_createProjects,
                        loadingEl: '',
                        postData: {
                            creatorOrgId: that._creatorOrgId,
                            validList:data.validList
                        }
                    };
                    m_ajax.postJson(option, function (res) {
                        if (res.code === '0') {
                            that._renderStep3();
                        } else {
                            S_toastr.error(res.info);
                        }
                    });

                }, function () {
                });
            });
            $(that.element).find('a[data-action="reImport"]').click(function () {

                that._render();
                return false;
            });
        }
        , _renderStep3: function () {
            var that = this;
            var html = template('m_historyData/m_historyData_step3', {});
            $(that.element).find('.step-container').html(html);
            $(that.element).find('div[data-step="2"]').removeClass('active').addClass('done');
            $(that.element).find('div[data-step="3"]').addClass('active');
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