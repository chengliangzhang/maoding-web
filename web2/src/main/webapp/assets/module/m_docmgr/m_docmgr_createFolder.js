/**
 * 创建文件夹
 * Created by wrb on 2018/1/10.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_docmgr_createFolder",
        defaults = {
            $title:null,
            $isDialog:true,
            $selectedNodeObj:null,//当前选中对象
            $editType:0,//0=默认，1=编辑
            $itemData:null,//编辑对象
            $saveCallBack:null//保存后事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._fileTypeStr = this.settings.$itemData!=null &&this.settings.$itemData.type==1?'文件':'文件夹';
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData(function () {
                that.save_validate();
            });
        }
        //初始化数据并加载模板
        ,initHtmlData:function (callBack) {
            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'新建文件夹',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '600',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    okText:'保存',
                    ok:function () {
                        if($('form.createFolder').valid()){
                            that.saveCreateFolder();
                        }else{
                            return false;
                        }
                    },
                    cancel:function () {

                    }
                },function(d){//加载html后触发

                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    var html = template('m_docmgr/m_docmgr_createFolder',{
                        itemData:that.settings.$itemData,
                        fileTypeStr:that._fileTypeStr
                    });
                    $(that.element).html(html);
                    if(callBack!=null){
                        callBack();
                    }

                });
            }else{//不以弹窗编辑
                var html = template('m_docmgr/m_docmgr_createFolder',{
                });
                $(that.element).html(html);
                if(callBack!=null){
                    callBack();
                }
            }
        }
        //文件夹保存
        ,saveCreateFolder:function (e) {
            var that = this;
            var fileName = $(that.element).find('input[name="folderName"]').val();

            var option  = {};
            if(that.settings.$editType==1){
                option.classId = that.element;
                option.url = restApi.url_netFile_rename;
                option.postData = {
                    id: that.settings.$itemData.id,
                    accountId: window.currentUserId,
                    fileName: $.trim(fileName)
                };
                m_ajax.postJson(option,function (response) {
                    if(response.code=='0'){
                        S_toastr.success('修改成功！');
                        if(that.settings.isDailog){
                            S_dialog.close(e);
                        }
                        if(that.settings.$saveCallBack!=null){
                            return that.settings.$saveCallBack(response.data);
                        }
                    }else {
                        S_dialog.error(response.info);
                    }
                })
            }else{
                option.url = restApi.url_netFile_createDirectory;
                option.postData = {
                    accountId: window.currentUserId,
                    companyId: window.currentCompanyId,
                    projectId: that.settings.$selectedNodeObj.projectId,
                    pid: that.settings.$selectedNodeObj.id,
                    fileName: $.trim(fileName)
                };
                m_ajax.postJson(option,function (response) {
                    if(response.code=='0'){
                        S_toastr.success('保存成功！');
                        if(that.settings.isDailog){
                            S_dialog.close(e);
                        }
                        if(that.settings.$saveCallBack!=null){
                            return that.settings.$saveCallBack(response.data);
                        }
                    }else {
                        S_dialog.error(response.info);
                    }
                })
            }
        }
        //保存验证
        ,save_validate:function(){
            var that = this;
            $('form.createFolder').validate({
                rules: {
                    folderName:{
                        required:true,
                        maxlength:50
                    }
                },
                messages: {
                    folderName:{
                        required:'请输入'+that._fileTypeStr+'名称!',
                        maxlength:that._fileTypeStr+'名称不超过50位!'
                    }
                }
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
