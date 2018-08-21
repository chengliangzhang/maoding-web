/**
 * 组织－组织架构-批量导入
 * Created by wrb on 2017/2/23.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_bulkImport",
        defaults = {
            $companyInfo: null//组织信息对象
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._currentCompanyId = window.currentCompanyId;
        this._currTreeObj = null;
        this._cdnUrl = window.cdnUrl;

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._renderHtml();
            that._uploadUserFile();
            that._bindActionClick();
            that._bindBackTolast();
        }
        //渲染界面
        , _renderHtml: function () {
            var that = this;
            var html = template('m_org/m_bulkImport', {companyName: that.settings.$companyInfo.companyName});
            $(that.element).html(html);
        }
        , _uploadUserFile: function () {
            var options = {}, that = this;
            options.server = restApi.url_uploadUserFile;
            options.formData = {companyId: that.settings.$companyInfo.companyId};
            // options.id = '#filePicker';
            options.accept = {
                title: '请选择Excel文件',
                extensions: 'xlsx,xls',
                mimeTypes: '.xlsx,.xls'
            };
            options.loadingId = '#content-box .ibox';
            options.uploadProgressCallback = function (file, percentage) {
                var progressObj = $(that.element).find('.progress');
                progressObj.removeClass('hide');
                progressObj.find('.progress-bar').css('width', percentage * 100 + '%');
                if (percentage == 1) {
                    progressObj.addClass('hide');
                }
            };
            options.uploadSuccessCallback = function (file, response) {
                S_toastr.success(response.info);
                $(that.element).find('#errorListBox').html('');
                if (response.data != null && response.data.error!=null) {
                    var options = {};
                    options.$userErrorList = response.data.error;
                    $(that.element).find('#errorListBox').m_bulkImportListTips(options);
                } else {//返回组织架构
                    $(that.element).m_organizational();
                }
            };
            $(that.element).find('#filePicker').m_fileUploader(options, true);
        }
        //下载模板
        , _downLoadTemplate: function () {
            var that = this;
            window.open(that._cdnUrl+'/download/OrgImportTpl.xlsx');
        }
        //按钮事件绑定
        , _bindActionClick: function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click', function () {
                var dataAction = $(this).attr('data-action');
                if (dataAction == 'downLoadTemplate') {
                    that._downLoadTemplate();
                    return false;
                }
            });
        }
        //返回按钮绑定
        , _bindBackTolast: function () {
            var that = this;
            $(that.element).find("a[data-action='backToLast']").on('click', function () {
                $(that.element).m_organizational();
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
