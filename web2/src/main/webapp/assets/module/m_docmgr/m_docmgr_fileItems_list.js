/**
 * 项目归档-文件列表
 * Created by wrb on 2017/1/9.
 */
;(function ($, window, document, undefined) {

    "use strict";

    var pluginName = "m_docmgr_fileItems_list",
        defaults = {
            $projectId: '',
            $pid:null,
            $projectName:null,
            $type:0,//默认=0=项目归档,1=项目详情中的项目归档
            $isBySearch:false,//是否搜索的列表
            $keywordBySearch:'',//搜索的关键字
            $url:restApi.url_getSkyDriverByProjectList,
            $selectedNodeObj:null,//当前选中对象
            $refreshDataCallBack:null,//刷新回滚事件
            $renderCallBack:null,//渲染完成事件
            $requestCallBack:null//请求回滚事件
        };

    function Plugin(element, options) {
        this.element = element;
        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this._selectedNodeObj = null;//当前选中节点对象
        this._fileItems = [];
        this._breadcrumbContainer = null;
        this._breadcrumb = {
            allowOperate: false,
            curDirId: null,
            lastDirId: null,
            items: [{dirId: null, text: '根目录'}]
        };

        this._currentCompanyId = window.currentCompanyId;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._selectedNodeObj = that.settings.$selectedNodeObj;
            that.renderHtml();
        },
        renderHtml: function () {
            var that = this;
            that.queryFileItems();
        }
        //查询文件项
        , queryFileItems: function () {

            var that = this;

            if(!that.settings.$isBySearch){
                var ajaxOption = {};
                ajaxOption.classId = '.file-list-items:eq(0)';
                //ajaxOption.url = restApi.url_getSkyDriverByProject;
                ajaxOption.url = that.settings.$url;
                ajaxOption.postData = {
                    projectId: that.settings.$projectId,
                    pid: that.settings.$pid,
                    type:that.settings.$selectedNodeObj.type
                };
                m_ajax.postJson(ajaxOption, function (res) {
                    if (res.code && res.code === '0') {

                        //判断是否能进行操作
                        //that._breadcrumb.allowOperate = (res.data && res.data.uploadFlag === 1);
                        that._fileItems = _.filter(res.data.list, function (o) {
                            return o.pid === that.settings.$pid;
                        });
                        that.renderFileItems(that._fileItems);
                        if(that.settings.$requestCallBack!=null){
                            that.settings.$requestCallBack(res.data);
                        }
                    }
                });

            }else{

                var option = {};
                option.param = {};
                option.param.fileName=that.settings.$keywordBySearch;
                paginationFun({
                    eleId: '#data-pagination-container',
                    loadingId: '.data-list-box',
                    url: restApi.url_getProjectFileByFileName,
                    params: option.param
                }, function (response) {

                    if (response.code == '0') {
                        that._fileItems = response.data.data;
                        $.each(that._fileItems, function (index, item) {
                            item.createDate = moment(item.createDate).format("YYYY-MM-DD");
                            item.fileSize = WebUploader.Base.formatSize(item.fileSize);
                        });
                        var html = template('m_docmgr/m_docmgr_fileItems_list', {
                            fileItems: that._fileItems,
                            rootPath: window.rootPath,
                            type:that.settings.$type
                        });
                        $(that.element).html(html);
                        that.initICheck();
                        that.bindActionClick();
                        that.editHoverFun();
                        that.initContextMenu(that._fileItems);
                        if(that.settings.$renderCallBack!=null){
                            that.settings.$renderCallBack(that._fileItems);
                        }

                    } else {
                        S_dialog.error(response.info);
                    }
                });
            }
        }
        //渲染文件项
        , renderFileItems: function (fileItems) {
            var that = this;
            $.each(fileItems, function (index, item) {
                //item.createDate = moment(item.createDate).format("YYYY-MM-DD HH:mm:ss");
                item.createDate = moment(item.createDate).format("YYYY-MM-DD");
                item.fileSize = WebUploader.Base.formatSize(item.fileSize);
            });
            var html = template('m_docmgr/m_docmgr_fileItems_list', {
                fileItems: fileItems,
                rootPath: window.rootPath,
                type:that.settings.$type
            });
            $(that.element).html(html);
            that.initICheck();
            that.bindActionClick();
            that.editHoverFun();
            that.initContextMenu(fileItems);
            if(that.settings.$renderCallBack!=null){
                that.settings.$renderCallBack();
            }
        }
        //初始化右键菜单
        , initContextMenu:function (fileItems) {
            var that = this;
            if(fileItems!=null && fileItems.length>0){
                $.each(fileItems, function (i, o) {
                    var id = 'box' + o.id;

                    var items = [];
                    if (o.type == 2) {
                        items.push({
                            content: "修改名称",//菜单项内容
                            action: function (e, item) {//菜单项单击和快捷键触发事件
                                var index = $(that.element).find('tr[id="'+id+'"]').attr('data-i');
                                that.editFileName(index);
                            }
                        });
                    }
                    if (o.type == 1) {
                        items.push({
                            content: "下载文件",
                            action: function (e, item) {
                                that.donwloadFile(o.id);
                            }
                        });
                    }
                    if (o.type == 0 || o.type == 1){
                        items.push({
                            content: "删除文件",
                            action: function (e, item) {
                                var index = $(that.element).find('tr[id="'+id+'"]').attr('data-i');
                                that.delFile(index);
                            }
                        });
                    }
                    if(o.type==50){
                        items.push({
                            content: "提交成果",
                            action: function (e, item) {
                                var index =  $(that.element).find('tr[id="'+id+'"]').attr('data-i');
                                that.submitDocResults(index);
                            }
                        });
                    }
                    if(o.sendResults==1 && o.type==30){
                        items.push({
                            content: "发送成果",
                            action: function (e, item) {
                                var index =  $(that.element).find('tr[id="'+id+'"]').attr('data-i');
                                that.sendDocResults(index);
                            }
                        });
                    }
                    if(!(o.type == 2 || o.type == 1 || (o.type == 0 || o.type == 1) || o.type==50 || (o.sendResults==1 && o.type==30)) ){
                        return true;
                    }
                    var menu = new contextMenu({
                        target: document.getElementById(id),//开启自定义右键菜单的目标,
                        hasIcon: false,//是否需要图标
                        hasTitle: false,//是否需要标题
                        autoHide:false,//是否自动隐藏右键菜单
                        menuClass:'innerContentMenu',
                        menu: {//菜单数据配置
                            items: items
                        },
                        beforeShow:function () {
                            $('.contextMenuBox').hide();
                            /*$(that.element).find('input[name="itemCk"]').prop('checked',false);
                            $(that.element).find('input[name="itemCk"]').iCheck('update');
                            $(that.element).find('tr[id="'+id+'"] input[name="itemCk"]').prop('checked',true);
                            $(that.element).find('tr[id="'+id+'"] input[name="itemCk"]').iCheck('update');*/
                        },
                        afterShow:function () {
                            $(that.element).find('tr[id="'+id+'"]').addClass('active').siblings().removeClass('active');
                        }
                    });
                });
            }

        }
        //初始化icheck
        , initICheck:function () {
            var that = this;
            var ifChecked = function (e) {
                $(this).closest('tr').find('.singleOperation').show();
                that.isItemAllCheck();
            };
            var ifUnchecked = function (e) {
                $(this).closest('tr').find('.singleOperation').hide();
                that.isItemAllCheck();
            };
            $(that.element).find('input[name="itemCk"]').iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_minimal-green'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);
            var ifAllChecked = function (e) {
                $(that.element).find('.batchOperation').show();
                $(that.element).find('input[name="itemCk"]').iCheck('check');
            };
            var ifAllUnchecked = function (e) {
                $(that.element).find('input[name="itemCk"]').prop('checked',false);
                $(that.element).find('input[name="itemCk"]').iCheck('update');
                $(that.element).find('input[name="itemCk"]').iCheck('enable');
                $(that.element).find('.singleOperation').hide();
                $(that.element).find('.batchOperation').hide();
            };
            $(that.element).find('input[name="itemAllCk"]').iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_minimal-green'
            }).on('ifUnchecked.s', ifAllUnchecked).on('ifChecked.s', ifAllChecked);
        }
        //判断全选是否该选中并给相关处理
        , isItemAllCheck:function () {
            var that =this;
            var len = $(that.element).find('input[name="itemCk"]:checked').length;
            var itemLen = that._fileItems.length;
            if(len==itemLen){
                $(that.element).find('input[name="itemAllCk"]').prop('checked',true);
                $(that.element).find('input[name="itemAllCk"]').iCheck('update');
            }else{
                $(that.element).find('input[name="itemAllCk"]').prop('checked',false);
                $(that.element).find('input[name="itemAllCk"]').iCheck('update');
            }
        }
        //hover事件
        , editHoverFun:function () {
            var that = this;
            $(that.element).find('TR').each(function () {

                var $this = $(this);
                var itemCkEle = $this.find('input[name="itemCk"]');
                var singleOperation = $this.find('.singleOperation');
                $this.hover(function () {
                    if(!itemCkEle.is(':checked')){//选中，出现操作按钮
                        singleOperation.show();
                    }
                },function () {
                    if(!itemCkEle.is(':checked')){//选中，出现操作按钮
                        singleOperation.hide();
                    }
                });
            });
        }
        //删除文件
        , delFile:function (index) {
            var that = this;
            var content = '确定要删除 【' + that._fileItems[index].fileName + '】 吗？';
            if (that._fileItems[index].type == 0 || that._fileItems[index].type == 40 || that._fileItems[index].type==30 || that._fileItems[index].type==2 || that._fileItems[index].type == 50){
                content = '删除文件夹会移除所有子文件,' + content;
            }
            S_swal.confirm({
                title:'确定删除吗?',
                text:content,
                callBack:function () {
                    that.ajaxDelete(that._fileItems[index]);
                }
            });
        }
        //删除文件
        , ajaxDelete : function (data) {
            var that = this;
            var ajaxOption = {};
            ajaxOption.url = restApi.url_netFile_delete;
            ajaxOption.postData = {
                id: data.id,
                accountId: window.currentUserId
            };
            m_ajax.postJson(ajaxOption, function (res) {
                if (res.code === '0') {

                    S_swal.sure({
                        title:'已删除',
                        text:'该文件删除成功。',
                        callBack:function () {
                            if(that.settings.$refreshDataCallBack!=null){
                                that.settings.$refreshDataCallBack();
                            }
                        }
                    });
                } else if (res.code === '1') {
                    S_dialog.error(res.msg);
                }
            });
        }
        //下载文件
        , donwloadFile:function (id) {
            var url = restApi.url_downLoadFile+'/'+id;
            window.open(url);
            return false;
        }
        //修改名称
        , editFileName:function (index) {
            var that = this, options = {};
            options.$title = that._fileItems[index].type==0?'修改文件夹':'修改文件';
            options.$editType = 1;
            options.$itemData = {
                id:that._fileItems[index].id,
                fileName:that._fileItems[index].fileName,
                fileExtName:that._fileItems[index].fileExtName,
                type:that._fileItems[index].type
            };
            options.$selectedNodeObj = that._selectedNodeObj;
            options.$saveCallBack = function () {
                if(that.settings.$refreshDataCallBack!=null){
                    that.settings.$refreshDataCallBack();
                }
            };
            $('body').m_docmgr_createFolder(options);
        }
        //提交成果文件
        , submitDocResults:function (index) {
            var that = this;
            S_dialog.confirm('确定提交成果文件？',function () {

                var option = {};
                option.url = restApi.url_notarizeArchivedFileNotifier;
                option.postData = {
                    projectId:that.settings.$projectId,
                    companyId:that._currentCompanyId,
                    id:that._fileItems[index].id,
                    pid:that._selectedNodeObj.id
                };
                m_ajax.postJson(option, function (response) {
                    if (response.code == '0') {
                        S_toastr.success('操作成功');
                        if(that.settings.$refreshDataCallBack!=null){
                            that.settings.$refreshDataCallBack();
                        }
                    } else {
                        S_dialog.error(response.info);
                    }
                });
            },function () {
            });
        }
        //发送成果文件
        , sendDocResults:function (index) {
            var that = this;
            var options = {};
            options.$itemData = {
                id:that._fileItems[index].id,
                fileName:that._fileItems[index].fileName,
                fileExtName:that._fileItems[index].fileExtName,
                type:that._fileItems[index].type
            };
            options.$selectedNodeObj = that._selectedNodeObj;
            options.$saveCallBack = function () {
                if(that.settings.$refreshDataCallBack!=null){
                    that.settings.$refreshDataCallBack();
                }
            };
            $('body').m_docmgr_sendDocResults(options);
        }
        , openOrSelectNode:function (skyDrivePathArr,i) {
            var that = this;
            var tree = $('#documentDirectoryTree').jstree(true);
            var node = tree.get_node(skyDrivePathArr[i]);

            if(skyDrivePathArr.length==1){

                tree.select_node('#'+skyDrivePathArr[i]);

            }else if(i<=skyDrivePathArr.length-2){
                if(!tree.is_open(node)){
                    tree.open_node(node,function () {
                        if(i<=skyDrivePathArr.length-1){
                            that.openOrSelectNode(skyDrivePathArr,i+1);
                        }
                    });
                }else{
                    if(i<=skyDrivePathArr.length-1){
                        that.openOrSelectNode(skyDrivePathArr,i+1);
                    }
                }
            }else{
                tree.select_node('#'+skyDrivePathArr[i]);
            }
        }
        //事件绑定
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').off('click').on('click',function (e) {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                var index = $this.closest('TR').attr('data-i');
                switch (dataAction){
                    case 'editFileName'://修改文件夹或文件名称
                        that.editFileName(index);
                        break;
                    case 'downloadFile'://下载文件
                        that.donwloadFile(that._fileItems[index].id);
                        return false;
                        break;
                    case 'batchDownloadFile'://批量下载文件

                        $(that.element).find('input[name="itemCk"]:checked').each(function () {
                            var id = $(this).closest('TR').attr('data-id');
                            var type = $(this).closest('TR').attr('data-type');
                            if(type==1){
                                that.donwloadFile(id);
                            }
                        });
                        return false;
                        break;
                    case 'delFile'://删除文件
                        that.delFile(index);
                        break;
                    case 'batchDelFile'://批量删除

                        break;
                    case 'intoSubDirectory'://点击进入子目录

                        if(that._fileItems[index].type==0 || that._fileItems[index].type==40 || that._fileItems[index].type==30 || that._fileItems[index].type==2 || that._fileItems[index].type == 50 || that._fileItems[index].type == 41){

                            if(that.settings.$isBySearch){//经搜索

                                var skyDrivePath = that._fileItems[index].skyDrivePath;
                                skyDrivePath = that._fileItems[index].projectId + '-' + skyDrivePath;
                                var skyDrivePathArr = skyDrivePath.split('-');
                                that.openOrSelectNode(skyDrivePathArr,0);


                            }else{//不经搜索

                                var tree = $('#documentDirectoryTree').jstree(true),
                                    sel = tree.get_selected();

                                if(!tree.is_open(sel)){
                                    tree.open_node(sel,function () {
                                        tree.select_node('#'+that._fileItems[index].id);
                                    });
                                }else{
                                    tree.select_node('#'+that._fileItems[index].id);
                                }
                                tree.deselect_node(sel);
                            }

                        }
                        break;
                    case 'submitDocResults'://提交成果文件

                        that.submitDocResults(index);
                        e.stopPropagation();
                        break;
                    case 'sendDocResults'://发送成果文件
                        that.sendDocResults(index);
                        e.stopPropagation();
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
