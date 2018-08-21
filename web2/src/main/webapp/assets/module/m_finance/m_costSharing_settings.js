/**
 * 费用均摊项设置
 * Created by wrb on 2018/05/30.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_costSharing_settings",
        defaults = {
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentExpFixedData = null;//当前费用数据
        this._currentSelectedOrg = null;//当前选中组织
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_finance/m_costSharing_settings',{});
            $(that.element).html(html);
            that.initOrgTree();
            that.leftBoxHeightResize();
            $(window).resize(function () {
                var t = setTimeout(function () {
                    that.leftBoxHeightResize();
                    clearTimeout(t);
                });
            });
        }
        //初始化组织树
        , initOrgTree: function () {
            var that = this;
            var options = {};
            options.isDialog = false;
            options.isGetUserList = false;
            options.treeUrl = restApi.url_getRelationTypeIsThree;
            options.currentCompanyId = window.currentCompanyId;
            options.treeIconObj = {
                'default': {
                    'icon': 'iconfont rounded icon-zuzhijiagou'
                },
                'independent': {   //独立经营图标
                    'icon': 'fa fa-users'
                },
                'partner': {       //事业合伙人图标
                    'icon': 'iconfont rounded icon-cooperation'
                },
                'partnerContainer': {       //事业合伙人容器图标
                    'icon': 'iconfont rounded icon-cooperation'
                },
                'subCompany': {       //分支机构图标
                    'icon': 'iconfont rounded icon-2fengongsi1'
                },
                'subCompanyContainer': {       //分支机构容器图标
                    'icon': 'iconfont rounded icon-2fengongsi1'
                },
                'company': {         //根节点图标
                    'icon': 'iconfont rounded icon-2fengongsi'
                }
            };
            options.afterOpenCallBack = function (data) {

                that.dealTreeIconColorFun(data.node.id);
            };
            options.selectNodeCallBack = function (data,type) {
                console.log(data);
                that._currentSelectedOrg = data;
                that.renderContent();
            };
            $(that.element).find('#organization_treeH').m_orgByTree(options);
        }
        //树icon颜色处理
        , dealTreeIconColorFun:function (id) {
            $('#organization_treeH').find('li[id="'+id+'"]>ul>li').each(function () {
                var $this = $(this);
                var relationType = $this.attr('relationtype');
                if(relationType==1){
                    $this.find('a.jstree-anchor i.jstree-icon').addClass('color-blue');
                }else if(relationType==2){
                    $this.find('a.jstree-anchor i.jstree-icon').addClass('color-green');
                }else if(relationType==3){
                    $this.find('a.jstree-anchor i.jstree-icon').addClass('color-red');
                }
            });
        }
        //树菜单高度自适应
        , leftBoxHeightResize:function () {
            var that = this;
            var pageWrapperH = $('#page-wrapper').css('min-height');
            $(that.element).find('#left-box').css('min-height',pageWrapperH);
        }

        //渲染右边内容
        , renderContent:function () {
            var that = this;
            var categoryType = $(that.element).find('select[name="categoryType"]').val();
            if(categoryType==''){
                categoryType = null;
            }
            var option  = {};
            option.classId= '#content-box';
            option.url = restApi.url_getExpShareTypeList;
            option.postData = {
                companyId : that._currentSelectedOrg.id
            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){

                    var feeEntryFieldList = response.data;
                    var html = template('m_finance/m_costSharing_settings_content',{feeEntryFieldList:feeEntryFieldList});
                    $(that.element).find('#right-box').html(html);
                    that.initItemICheck();
                    that.bindAddFeeFieldBtn();
                    that.dealPidCheck();

                }else {
                    S_dialog.error(response.info);
                }
            });

        }
        //初始ICheck
        ,initItemICheck:function () {
            var that = this;
            var ifChecked = function (e) {
                var dataId = $(this).attr('data-id');
                var dataPid = $(this).attr('data-pid');

                if(dataPid==''){//根目录
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').prop('checked',true);
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').iCheck('update');

                }else{
                    var childLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataPid+'"]').length;
                    var childCheckedLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataPid+'"]:checked').length;
                    if(childLen==childCheckedLen){
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').prop('checked',true);
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').iCheck('update');
                    }else{
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').prop('checked',false);
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').iCheck('update');
                    }
                }
                that.bindSaveFeeField();
            };
            var ifUnchecked = function (e) {
                var dataId = $(this).attr('data-id');
                var dataPid = $(this).attr('data-pid');

                if(dataPid==''){//根目录
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').prop('checked',false);
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').iCheck('update');
                }else{

                    $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').prop('checked',false);
                    $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').iCheck('update');
                }
                that.bindSaveFeeField();
            };
            $(that.element).find('input[name="itemCk"]').iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_minimal-green'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);
        }
        //子项全选，父项选中
        , dealPidCheck:function () {
            var that = this;
            $(that.element).find('input[name="itemCk"][data-pid=""]').each(function () {
                var dataId = $(this).attr('data-id');
                var childCheckedLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]:checked').length;
                var childLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').length;
                if(childCheckedLen==childLen){
                    $(this).prop('checked',true);
                    $(this).iCheck('update');
                }
            });
        }
        //添加费用类型
        , bindAddFeeFieldBtn:function () {
            var that = this;
            $(that.element).find('button[data-action="addFeeField"]').off('click').on('click',function (e) {
                var $this = $(this);
                S_dialog.dialog({
                    title: '新增费用类型',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '350',
                    height:'100',
                    tPadding:'0',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    ok:function(){

                        if ($('form.addFeeFieldForm').valid()) {

                            var feeField = $('form.addFeeFieldForm input[name="feeField"]').val();
                            var categoryType = $('form.addFeeFieldForm select[name="categoryType"]').val();
                            var option  = {};
                            option.url = restApi.url_saveExpFixCategory;
                            option.postData = {
                                pid:$this.attr('data-id'),
                                //categoryType:categoryType,
                                name:feeField
                            };
                            m_ajax.postJson(option,function (response) {
                                if(response.code=='0'){
                                    S_toastr.success('操作成功');
                                    that.renderContent();
                                }else {
                                    S_dialog.error(response.info);
                                }
                            });

                        } else {
                            return false;
                        }

                    },
                    cancelText:'取消',
                    cancel:function(){

                    }
                },function(d){//加载html后触发

                    var $dialogEle = $('div[id="content:'+d.id+'"] .dialogOBox');
                    var html = template('m_finance/m_feeEntry_settings_add',{editType:'costSharing'});
                    $dialogEle.html(html);
                    $dialogEle.find('select[name="categoryType"]').select2({
                        allowClear: false,
                        language: "zh-CN",
                        minimumResultsForSearch: -1
                    });
                    $dialogEle.find('select[name="categoryType"]').val(2).trigger('change');
                    $dialogEle.find('select[name="categoryType"]').prop('disabled',true);
                    that.saveFeeField_validate();

                });
                e.stopPropagation();
                return false;
            });
        }
        , bindSaveFeeField:function () {
            var that = this;
            var checkedList = [];
            $(that.element).find('input[name="itemCk"]:checked').each(function () {
                var dataId = $(this).attr('data-id');
                var dataPid = $(this).attr('data-pid');
                if(dataPid!=undefined && dataPid!=''){
                    checkedList.push(dataId);
                }
            });

            var option  = {};
            option.url = restApi.url_saveExpShareTypeShowStatus;
            option.postData = {
                categoryType:3,
                companyId : that._currentSelectedOrg.id,
                idList:checkedList
            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                }else {
                    S_dialog.error(response.info);
                }
            });

        }
        //类型不为空判断
        , saveFeeField_validate: function () {
            var that = this;
            $('form.addFeeFieldForm').validate({
                rules: {
                    feeField: {
                        required: true,
                        maxlength: 50
                    }
                },
                messages: {
                    feeField: {
                        required: '请输入类型名称！',
                        maxlength: '请控制在50字符以内！'
                    }
                }
            });
        }
    });

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            // if (!$.data(this, "plugin_" + pluginName)) {
            $.data(this, "plugin_" +
                pluginName, new Plugin(this, options));
            // }
        });
    };

})(jQuery, window, document);
