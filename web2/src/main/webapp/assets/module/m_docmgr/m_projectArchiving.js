/**
 * 项目归档
 * Created by wrb on 2017/12/13.
 */
;(function ($, window, document, undefined) {

    "use strict";

    var pluginName = "m_projectArchiving",
        defaults = {
            $projectId: '',
            $projectName:null
        };

    function Plugin(element, options) {
        this.element = element;
        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this._selectedNodeObj = null;//当前选中节点对象
        this._fileItems = [];
        this._uploadmgrContainer = null;
        this._btnNewDir = null;
        this._btnUpload = null;
        this._fileListItemsContainer = null;
        this._breadcrumbContainer = null;
        this._breadcrumb = {
            allowOperate: false,
            curDirId: null,
            lastDirId: null,
            items: [{dirId: null, text: '根目录'}]
        };

        this._companyId = window.currentCompanyId;
        if (_.isBlank(this._companyId)) {
            S_dialog.error('无法获取团队Id');
            return;
        }

        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderHtml();


        },
        renderHtml: function () {
            var that = this;

            var html = template('m_docmgr/m_projectArchiving', {
                projectName:that.settings.$projectName
            });
            $(that.element).html(html);

            that.initDocmgrTree();
            that.treeBoxHeightResize();
            $(window).resize(function () {
                var t = setTimeout(function () {
                    that.treeBoxHeightResize();
                    clearTimeout(t);
                });
            });
            $(that.element).find('select[name="selectSearchBy"]').select2({
                allowClear: false,
                language: "zh-CN",
                minimumResultsForSearch: -1
            });
            $(that.element).find('select[name="selectSearchBy"]').on("change", function (e) {
                var val = $(this).val();
                if(val==0){
                    $(that.element).find('input[name="projectName"]').attr('placeholder','请输入项目名称关键字');
                }else{
                    $(that.element).find('input[name="projectName"]').attr('placeholder','请输入文件名称关键字');
                }

            });

        }
        //初始化组织树
        , initDocmgrTree: function () {
            var that = this;
            var keyword = '',selectBy=$(that.element).find('select[name="selectSearchBy"]').val();
            if(selectBy==0){
                keyword = $.trim($(that.element).find('input[name="projectName"]').val());
            }
            var $tree = $('#documentDirectoryTree');
            $tree.jstree({
                'core': {
                    'check_callback': true,
                    'data':function (node, callback) {
                        var option = {};
                        option.async = false;
                        option.postData = {};
                        var jsonStr='[]';
                        var jsonArray = eval('('+jsonStr+')');
                        if(node.id === '#'){
                            option.url = restApi.url_getProjectsDocuments;
                            option.postData.projectName = keyword;
                            m_ajax.postJson(option, function (response) {
                                if (response.code == '0') {
                                    var arrays= response.data.tableDTOS;
                                    for(var i=0 ; i<arrays.length; i++){
                                        var arr = {
                                            'id':arrays[i].id,
                                            'children':arrays[i].childId,
                                            'text':arrays[i].projectName,
                                            'pid':null,
                                            'taskId':arrays[i].taskId,
                                            'type':arrays[i].type,
                                            'haveNoticeRight':arrays[i].haveNoticeRight,
                                            'uploadFlag':response.data.uploadFlag
                                        };
                                        jsonArray.push(arr);
                                    }
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });
                            callback.call(this, jsonArray);
                        }else{
                            option.url = restApi.url_getMyProjectSkyDriveByParam;
                            if(node.original.projectId!=null){//存在此字段
                                option.postData.projectId = node.original.projectId;
                                option.postData.pid = node.id;
                            }else{
                                option.postData.projectId = node.id;
                            }
                            option.postData.type = that._selectedNodeObj.type;
                            m_ajax.postJson(option, function (response) {
                                if (response.code == '0') {
                                    var arrays= response.data.list;
                                    if(arrays!=null && arrays.length>0){
                                        for(var i=0 ; i<arrays.length; i++){
                                            var arr = {
                                                'id':arrays[i].id,
                                                'pid':node.id,
                                                'children':arrays[i].childId,
                                                'text':arrays[i].fileName,
                                                'projectId':arrays[i].projectId,
                                                'taskId':arrays[i].taskId,
                                                'type':arrays[i].type,
                                                'haveNoticeRight':arrays[i].haveNoticeRight,
                                                'uploadFlag':response.data.uploadFlag
                                            };
                                            jsonArray.push(arr);
                                        }
                                    }
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });
                            callback.call(this, jsonArray);
                        }
                    }
                },
                'plugins': ['noclose', 'types', 'wholerow'],
                'types': {
                    'default': {
                        'icon': 'fa fa-folder'
                    },
                    '1': {   //文件
                        'icon': 'fa fa-file'
                    },
                    '50': {   //归档文件
                        'icon': 'fa fa-steam-square'
                    }
                }
            }).on('select_node.jstree', function (e, data) {

                that._selectedNodeObj = data.node.original;
                that.getFileItemsList();

            }).on('ready.jstree', function (e, data) {//loaded.jstree

                if(selectBy==0){
                    var tree = $('#documentDirectoryTree').jstree(true);
                    var firstId = $(that.element).find('li:first').attr('id');
                    tree.select_node('#'+firstId);
                    that.bindActionClick();
                }

            }).on('load_node.jstree', function (e, data) {//load_node.jstree

                that.treeBoxHeightResize();
            });
        }
        //获取文件列表
        , getFileItemsList:function () {
            var that = this;
            var projectId = that._selectedNodeObj.pid==null?that._selectedNodeObj.id:that._selectedNodeObj.projectId;
            var pid = that._selectedNodeObj.id;
            var options = {};
            //options.$url = restApi.url_getMyProjectSkyDriveByParam;
            options.$type = 1;
            options.$projectId = projectId;
            if(projectId!=pid){//不是根目录
                options.$pid = pid;
            }
            options.$selectedNodeObj = that._selectedNodeObj;
            options.$requestCallBack = function (data) {
                that.hideBtnAction(data);
                that.initContextMenu(data.uploadFlag);
            };
            options.$refreshDataCallBack = function () {
                that.getFileItemsList();
                var tree = $('#documentDirectoryTree').jstree(true),
                    sel = tree.get_selected();
                tree.refresh_node('#'+sel);
            };
            options.$renderCallBack = function () {
                that.fileItemsBoxHeightResize();
                $(window).resize(function () {
                    var t1 = setTimeout(function () {
                        that.fileItemsBoxHeightResize();
                        clearTimeout(t1);
                    });
                });
                $(that.element).find('.m-pagination').hide();
            };
            $(that.element).find('#fileItems').m_docmgr_fileItems_list(options,true);

        }
        //获取文件列表
        , getFileItemsListBySearch:function (keyword) {
            var that = this;
            var options = {};
            options.$type = 1;
            options.$isBySearch = true;
            options.$keywordBySearch = keyword;
            options.$renderCallBack = function (data) {
                data.uploadFlag = 0;
                that.hideBtnAction(data);
                that.initContextMenu(data.uploadFlag);
                $(that.element).find('.m-pagination').show();
            };
            $(that.element).find('#fileItems').m_docmgr_fileItems_list(options,true);

        }
        //初始化右键菜单
        ,initContextMenu:function (flag) {
            var that = this;
            var items = [];
            if(flag==1){
                items.push({
                    content: "上传文件",
                    action: function (e, item) {
                        that.uploadFile();
                    }
                });
                items.push({
                    content: "新建文件夹",
                    action: function (e, item) {
                        that.createFolder();
                    }
                });
            }
            if(that._selectedNodeObj.taskId!=null && that._selectedNodeObj.type==40){
                items.push({
                    content: "发送归档通知",
                    action: function (e, item) {
                        that.sendArchiveNotice();
                    }
                });
            }
            if(items.length>0){
                var menu = new contextMenu({
                    target: document.getElementById('fileItems'),//开启自定义右键菜单的目标,
                    hasIcon: false,//是否需要图标
                    hasTitle: false,//是否需要标题
                    autoHide:false,//是否自动隐藏右键菜单
                    menuClass:'outContentMenu',
                    menu: {//菜单数据配置
                        items: items
                    },
                    beforeShow:function () {
                        $('.contextMenuBox').hide();
                    },
                    afterShow:function () {
                        console.log(1);
                    }
                });
            }
        }
        //隐藏按钮
        ,hideBtnAction:function (data) {
            var that = this;
            if(data.uploadFlag==0){
                $(that.element).find('a.btn-flag').hide();
            }else{
                $(that.element).find('a.btn-flag').show();
            }
            if(that._selectedNodeObj.taskId!=null && that._selectedNodeObj.type==40 && data.haveNoticeRight==true){
                $(that.element).find('a[data-action="sendArchiveNotice"]').show();
            }else{
                $(that.element).find('a[data-action="sendArchiveNotice"]').hide();
            }

        }
        //树菜单高度自适应
        , treeBoxHeightResize:function () {
            var that = this;
            var pageWrapperH = $('#page-wrapper').css('min-height');
            $(that.element).find('#documentTreeBox').css('height',pageWrapperH);
        }
        //fileItems元素高度自适应
        , fileItemsBoxHeightResize:function () {
            var that = this;
            var itemsListHeight = $(that.element).find('#fileItems table').height();
            var pageWrapperH = $('#page-wrapper').css('min-height');
            if(parseInt(pageWrapperH)>itemsListHeight+50){
                $(that.element).find('#fileItems').css('height',(parseInt(pageWrapperH)-50)+'px');
            }else{
                $(that.element).find('#fileItems').css('height','auto');
            }
        }
        //创建文件夹
        ,createFolder:function () {
            var that = this,options = {};
            options.$selectedNodeObj = that._selectedNodeObj;
            options.$saveCallBack = function () {
                that.getFileItemsList();
                var tree = $('#documentDirectoryTree').jstree(true),
                    sel = tree.get_selected();
                tree.refresh_node('#'+sel);
            };
            $('body').m_docmgr_createFolder(options);
        }
        //上传文件
        ,uploadFile:function () {
            var that = this,options = {};
            options.$selectedNodeObj = that._selectedNodeObj;
            options.$saveCallBack = function () {
                that.getFileItemsList();
            };
            $('body').m_docmgr_fileUpload(options);
        }
        //发送归档通知
        ,sendArchiveNotice:function () {
            var that = this,options = {};
            options.$selectedNodeObj = that._selectedNodeObj;
            options.$saveCallBack = function () {
                that.getFileItemsList();
                var tree = $('#documentDirectoryTree').jstree(true),
                    sel = tree.get_selected();
                tree.refresh_node('#'+sel);
            };
            $('body').m_docmgr_sendArchiveNotice(options);
        }
        //绑定事件
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action],button[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'createFolder'://创建文件夹
                        that.createFolder();
                        break;
                    case 'uploadFile'://上传文件
                        that.uploadFile();
                        break;
                    case 'searchByProjectName'://搜索

                        var selectVal = $(that.element).find('select[name="selectSearchBy"]').val();
                        var keyword = $(that.element).find('input[name="projectName"]').val();
                        $('#documentDirectoryTree').jstree().destroy ();
                        that.initDocmgrTree();
                        if(selectVal==1){
                            if(keyword!=undefined && $.trim(keyword)!=''){
                                that.getFileItemsListBySearch($.trim(keyword));
                            }
                        }
                        break;
                    case 'sendArchiveNotice'://发送归档通知
                        that.sendArchiveNotice();
                    case 'keywordSearch'://发送归档通知
                        var keyword = $(that.element).find('input[name="keyword"]').val();
                        if(keyword!=undefined && $.trim(keyword)!=''){
                            that.getFileItemsListBySearch($.trim(keyword));
                        }
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
