/**
 * 收支类别设置
 * Created by wrb on 2017/11/29.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_feeEntry_settings",
        defaults = {
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentExpFixedData = null;//当前费用数据
        this._selectedOrg = null;//当前选中组织
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_finance/m_feeEntry_settings',{});
            $(that.element).html(html);

            var option = {};
            option.$type = 1;
            option.$selectedCallBack = function (data) {
                that._selectedOrg = data;
                that.renderContent();
            };
            option.$renderCallBack = function () {
                that.initSelect2();
            };
            $(that.element).find('#selectOrg').m_org_chose_byTree(option);

        }
        , initSelect2:function () {
            var that = this;
            $(that.element).find('select[name="payType"]').select2({
                allowClear: false,
                language: "zh-CN",
                minimumResultsForSearch: -1
            });
            $(that.element).find('select[name="payType"]').on("change", function (e) {
                that.renderContent();
            })
        }
        //渲染右边内容
        , renderContent:function () {
            var that = this;
            var payType = $(that.element).find('select[name="payType"]').val();
            if(payType==''){
                payType = null;
            }
            var option  = {};
            option.classId= '#content-box';
            option.url = restApi.url_getExpFixTypeList;
            option.postData = {
                companyId : that._selectedOrg.id,
                payType: payType
            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){

                    /*var feeEntryFieldList = [
                        {
                            "id": "c0946ad1713244f1a4736462989223b3",
                            "token": null,
                            "appOrgId": null,
                            "accountId": null,
                            "currentCompanyId": null,
                            "name": "其他业务收入",
                            "child": [
                                {
                                    "code": "ztfy",
                                    "companyId": "d18bda5241da402c965923aca6554fb0",
                                    "expTypeMemo": "效果图设计制作费，多媒体制作费，动画制作费，绘图打印费",
                                    "id": "1b033e48b5004682932954dfccb4530e",
                                    "name": "制图费用",
                                    "pid": "c0946ad1713244f1a4736462989223b3"
                                }
                            ]
                        },
                        {
                            "id": "c0946ad1713244f1a4736462989223b1",
                            "token": null,
                            "appOrgId": null,
                            "accountId": null,
                            "currentCompanyId": null,
                            "name": "直接人工成本",
                            "child": [
                                {
                                    "code": "ztfy",
                                    "companyId": "d18bda5241da402c965923aca6554fb0",
                                    "expTypeMemo": "11",
                                    "id": "1b033e48b5004682932954dfccb45311",
                                    "name": "员工工资",
                                    "pid": "c0946ad1713244f1a4736462989223b1"
                                },
                                {
                                    "code": "ztfy",
                                    "companyId": "d18bda5241da402c965923aca6554fb0",
                                    "expTypeMemo": "22",
                                    "id": "1b033e48b5004682932954dfccb45322",
                                    "name": "五险一金",
                                    "pid": "c0946ad1713244f1a4736462989223b1"
                                },
                                {
                                    "code": "ztfy",
                                    "companyId": "d18bda5241da402c965923aca6554fb0",
                                    "expTypeMemo": "33",
                                    "id": "1b033e48b5004682932954dfccb45333",
                                    "name": "员工奖金",
                                    "pid": "c0946ad1713244f1a4736462989223b1"
                                }
                            ]
                        }
                    ];*/
                    var feeEntryFieldList = response.data;
                    var html = template('m_finance/m_feeEntry_settings_content',{feeEntryFieldList:feeEntryFieldList});
                    $(that.element).find('#right-box').html(html);

                    that.dealPidCheck();
                    that.initItemICheck();
                    that.initBtnHover();
                    that.bindActionClick();

                }else {
                    S_dialog.error(response.info);
                }
            });

        }
        //初始ICheck
        , initItemICheck:function () {
            var that = this;
            var ifChecked = function (e) {
                var dataId = $(this).attr('data-id');
                var dataPid = $(this).attr('data-pid');

                if(dataPid==''){//根目录
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').not(':disabled').prop('checked',true);
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').not(':disabled').iCheck('update');

                }else{
                    var childLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataPid+'"]').not(':disabled').length;
                    var childCheckedLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataPid+'"]:not(:disabled):checked').length;
                    if(childLen==childCheckedLen && childLen!=0){
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').not(':disabled').prop('checked',true);
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').not(':disabled').iCheck('update');
                    }else{
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').not(':disabled').prop('checked',false);
                        $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').not(':disabled').iCheck('update');
                    }
                }
                that.bindSaveFeeField();
            };
            var ifUnchecked = function (e) {
                var dataId = $(this).attr('data-id');
                var dataPid = $(this).attr('data-pid');

                if(dataPid==''){//根目录
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').not(':disabled').prop('checked',false);
                    $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').not(':disabled').iCheck('update');
                }else{

                    $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').not(':disabled').prop('checked',false);
                    $(that.element).find('input[name="itemCk"][data-id="'+dataPid+'"]').not(':disabled').iCheck('update');
                }
                that.bindSaveFeeField();
            };
            $(that.element).find('input[name="itemCk"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);
        }
        //子项全选，父项选中
        , dealPidCheck:function () {
            var that = this;
            $(that.element).find('input[name="itemCk"][data-pid=""]').each(function () {
                var dataId = $(this).attr('data-id');

                var childDisableLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]:disabled:checked').length;
                var childCheckedLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]:not(:disabled):checked').length;
                var childLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').not(':disabled').length;
                var allChildLen = $(that.element).find('input[name="itemCk"][data-pid="'+dataId+'"]').length;

                if(childCheckedLen==childLen && childLen!=0){
                    $(this).prop('checked',true);
                    $(this).iCheck('update');
                }
                if(childLen == 0 || allChildLen==0){
                    $(this).attr('disabled','disabled');
                }
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
            option.url = restApi.url_saveExpTypeShowStatus;
            option.postData = {
                payType:$(that.element).find('select[name="payType"]').val(),
                companyId : that._selectedOrg.id,
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
        //编辑按钮
        , initBtnHover:function () {
            var that = this;
            $(that.element).find('a[data-action="editName"],a[data-action="delFeeField"]').each(function () {
                var $this = $(this);
                $this.closest('td').hover(function () {
                    $this.show();
                },function () {
                    $this.hide();
                });
            });
        }
        //事件绑定
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').off('click').on('click',function (e) {
                var $this = $(this),dataAction = $this.attr('data-action');
                var dataId = $this.attr('data-id');
                var dataPid = $this.attr('data-pid');
                var dataName = $this.attr('data-name');
                var dataCategoryType = $this.attr('data-category-type');
                switch (dataAction){
                    case 'editName'://编辑费用类型
                        S_dialog.dialog({
                            title: '编辑费用类型',
                            contentEle: 'dialogOBox',
                            lock: 3,
                            width: '350',
                            height:'90',
                            tPadding:'0',
                            url: rootPath+'/assets/module/m_common/m_dialog.html',
                            ok:function(){

                                if ($('form.addFeeFieldForm').valid()) {

                                    var feeField = $('form.addFeeFieldForm input[name="feeField"]').val();
                                    //var categoryType = $('form.addFeeFieldForm select[name="categoryType"]').val();
                                    var option  = {};
                                    option.url = restApi.url_saveExpFixCategory;
                                    option.postData = {
                                        pid:dataPid,
                                        id:dataId,
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
                                //$('form.addFeeFieldForm select[name="categoryType"]').select2('destroy');
                            }
                        },function(d){//加载html后触发

                            var $dialogEle = $('div[id="content:'+d.id+'"] .dialogOBox');
                            var html = template('m_finance/m_feeEntry_settings_add',{});
                            $dialogEle.html(html);

                            /*$dialogEle.find('select[name="categoryType"]').select2({
                                allowClear: false,
                                language: "zh-CN",
                                minimumResultsForSearch: -1
                            });*/

                            $('form.addFeeFieldForm input[name="feeField"]').val(dataName);
                            //$('form.addFeeFieldForm select[name="categoryType"]').val(dataCategoryType).trigger('change');

                            that.saveFeeField_validate();
                        });
                        e.stopPropagation();
                        return false;
                        break;
                    case 'addFeeField'://新增费用类型
                        S_dialog.dialog({
                            title: '新增费用类型',
                            contentEle: 'dialogOBox',
                            lock: 3,
                            width: '350',
                            height:'90',
                            tPadding:'0',
                            url: rootPath+'/assets/module/m_common/m_dialog.html',
                            ok:function(){

                                if ($('form.addFeeFieldForm').valid()) {

                                    var feeField = $('form.addFeeFieldForm input[name="feeField"]').val();
                                    //var categoryType = $('form.addFeeFieldForm select[name="categoryType"]').val();
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
                                //$('form.addFeeFieldForm select[name="categoryType"]').select2('destroy');
                            }
                        },function(d){//加载html后触发

                            var $dialogEle = $('div[id="content:'+d.id+'"] .dialogOBox');
                            var html = template('m_finance/m_feeEntry_settings_add',{});
                            $dialogEle.html(html);

                            /*$dialogEle.find('select[name="categoryType"]').select2({
                                allowClear: false,
                                language: "zh-CN",
                                minimumResultsForSearch: -1
                            });*/
                            that.saveFeeField_validate();
                        });
                        e.stopPropagation();
                        return false;
                        break;
                    case 'delFeeField'://删除费用类型

                        S_dialog.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                            var option = {};
                            option.url = restApi.url_deleteExpCategory;
                            option.postData = {};
                            option.postData.id = dataId;
                            m_ajax.postJson(option, function (response) {
                                if (response.code == '0') {
                                    S_toastr.success('删除成功！');
                                    that.renderContent();
                                } else {
                                    S_dialog.error(response.info);
                                }
                            });

                        }, function () {
                        });
                        break;
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
                    }/*,
                    categoryType:{
                        isSelected:true
                    }*/
                },
                messages: {
                    feeField: {
                        required: '请输入类型名称！',
                        maxlength: '请控制在50字符以内！'
                    }/*,
                    categoryType:{
                        isSelected:'请选择数据来源！'
                    }*/
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.appendTo(element.closest('.col-md-9'));
                }
            });
            $.validator.addMethod('isSelected', function(value, element) {
                var isOk = true;
                if( value==''){
                    isOk = false;
                }
                return  isOk;
            }, '请选择数据来源！');
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
