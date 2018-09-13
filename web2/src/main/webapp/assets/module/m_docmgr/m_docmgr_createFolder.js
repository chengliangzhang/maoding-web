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
            var html = template('m_docmgr/m_docmgr_createFolder',{
                itemData:that.settings.$itemData,
                fileTypeStr:that._fileTypeStr
            });
            that.initHtmlData(html,function () {
                that.save_validate();
            });
        }
        //初始化数据并加载模板
        ,initHtmlData:function (html,callBack) {
            var that = this;
            if(that.settings.$isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.$title||'新建文件夹',
                    area : '600px',
                    content:html,
                    cancel:function () {
                    },
                    ok:function () {

                        if($('form.createFolder').valid()){
                            that.saveCreateFolder();
                        }else{
                            return false;
                        }
                    }

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.$isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;
                    if(callBack)
                        callBack();
                });

            }else{//不以弹窗编辑
                $(that.element).html(html);
                if(callBack)
                    callBack();
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
                            S_layer.close(e);
                        }
                        if(that.settings.$saveCallBack!=null){
                            return that.settings.$saveCallBack(response.data);
                        }
                    }else {
                        S_layer.error(response.info);
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
                            S_layer.close(e);
                        }
                        if(that.settings.$saveCallBack!=null){
                            return that.settings.$saveCallBack(response.data);
                        }
                    }else {
                        S_layer.error(response.info);
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
