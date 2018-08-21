/**
 * 发送成果
 * Created by wrb on 2018/1/12.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_docmgr_sendDocResults",
        defaults = {
            $title:null,
            $isDialog:true,
            $selectedNodeObj:null,//当前选中对象
            $itemData:null,//编辑对象
            $saveCallBack:null//保存后事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;
        this._companyInfoData = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData(function () {
            });
        }
        //初始化数据并加载模板
        ,initHtmlData:function (callBack) {
            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'发送归档通知',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '700',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    okText:'发送',
                    ok:function () {
                        S_dialog.confirm('确定发送成果？',function () {
                           that.sendArchiveNotice();
                        },function () {
                        });
                    },
                    cancel:function () {

                    }
                },function(d){//加载html后触发

                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    that.getPartAInfo(function (data) {
                        console.log(data)

                        var html = template('m_docmgr/m_docmgr_sendDocResults',{
                            companyData:data
                        });
                        $(that.element).html(html);
                        if(callBack!=null){
                            callBack();
                        }
                    });

                });
            }else{//不以弹窗编辑
                var html = template('m_docmgr/m_docmgr_sendDocResults',{
                });
                $(that.element).html(html);
                if(callBack!=null){
                    callBack();
                }
            }
        }
        //查询甲方信息
        ,getPartAInfo:function (callback) {
            var that = this;
            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_sendOwner;
            option.postData = {
                projectId:that.settings.$selectedNodeObj.projectId,
                companyId:that._currentCompanyId,
                id:that.settings.$itemData.id,
                pid:that.settings.$selectedNodeObj.id
            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    that._companyInfoData = response.data;
                    if(callback!=null){
                        callback(response.data);
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })

        }
        //发送
        ,sendArchiveNotice:function (e) {
            var that = this;
            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_sendOwnerProjectFile;
            option.postData = {
                id: that._companyInfoData.id,
                projectId: that._companyInfoData.projectId,
                companyId: window.currentCompanyId,
                pid: that._companyInfoData.pid,
                fromCompanyId: that._companyInfoData.fromCompanyId,
                companyName: that._companyInfoData.companyName,
                fileName: that._companyInfoData.fileName
            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('发送成功！');
                    if(that.settings.$saveCallBack!=null){
                        that.settings.$saveCallBack();
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })

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
