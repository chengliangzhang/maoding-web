/**
 * 项目收支流程设置-添加/编辑
 * Created by wrb on 2018/7/17.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_process_finance_setting_add",
        defaults = {
            $title:null,
            $isDialog:true,
            $type:0,//0=收款，1=付款
            $processInfo:null,//流程信息，不为空即编辑
            $saveCallBack:null//保存后事件
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._applicableTeam = null;//适用团队选择
        this._associatedTeam = null;//关联团队选择


        this._typeName= this.settings.$type==0?'收款':'付款';
        this._editTypeName= this.settings.$processInfo==null?'新增':'编辑';


        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData(function () {
                that.save_validate();

                //适用团队选择框
                var option = {};
                option.$buttonStyle = 'width: 516px;border-radius: 4px;';
                option.$spanStyle = 'width: 487px;display: inline-block;text-align: left;color: #444;font-size: 13px;';
                if(that.settings.$processInfo!=null && that.settings.$processInfo.companyId!=null & that.settings.$processInfo.companyId!=''){
                    option.$selectedId = that.settings.$processInfo.companyId;
                }
                option.$selectedCallBack = function (data) {
                    that._applicableTeam = data;
                };
                option.$renderCallBack = function () {
                };
                $(that.element).find('#applicableTeam').m_org_chose_byTree(option);

                //关联团队选择框
                var option1 = {};
                option1.$buttonStyle = 'width: 516px;border-radius: 4px;';
                option1.$spanStyle = 'width: 487px;display: inline-block;text-align: left;color: #444;font-size: 13px;';
                if(that.settings.$processInfo!=null && that.settings.$processInfo.companyId!=null & that.settings.$processInfo.companyId!=''){
                    option1.$selectedId = that.settings.$processInfo.relationCompanyId;
                }
                option1.$selectedCallBack = function (data) {
                    that._associatedTeam = data;
                };
                option1.$renderCallBack = function () {
                };
                $(that.element).find('#associatedTeam').m_org_chose_byTree(option1);


                $(that.element).find('select[name="receiptType"]').select2({
                    allowClear: false,
                    language: "zh-CN",
                    minimumResultsForSearch: -1
                });
                //编辑状态 展示信息处理
                if(that.settings.$processInfo!=null){
                    $(that.element).find('select[name="receiptType"]').val(that.settings.$processInfo.processType).trigger('change');
                    $(that.element).find('select[name="receiptType"]').prop("disabled", true);
                }
            });
        }
        //初始化数据并加载模板
        ,initHtmlData:function (callBack) {
            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||that._editTypeName+that._typeName+'计划流程',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '700',
                    tPadding: '0px',
                    overFlow:'unset',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    okText:'保存',
                    ok:function () {
                        if($(that.element).find('form.form-horizontal').valid()){
                            that.save();
                        }else{
                            return false;
                        }
                    },
                    cancel:function () {

                    }
                },function(d){//加载html后触发

                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    that.renderPage();

                    if(callBack!=null){
                        callBack();
                    }

                });
            }else{//不以弹窗编辑

                that.renderPage();
                if(callBack!=null){
                    callBack();
                }
            }
        }
        //渲染界面
        ,renderPage:function () {
            var that = this;
            var html = template('m_process/m_process_finance_setting_add',{
                type:that.settings.$type,
                typeName:that._typeName,
                processInfo:that.settings.$processInfo
            });
            $(that.element).html(html);
        }

        //发送
        ,save:function () {
            var that = this;
            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_saveProcess;
            option.postData = {
                processType:$(that.element).find('select[name="receiptType"]').val(),
                companyId:that._applicableTeam.id,
                relationCompanyId:that._associatedTeam.id,
                description:$(that.element).find('textarea[name="description"]').val()

            };
            if(that.settings.$processInfo!=null && that.settings.$processInfo.id!=null){
                option.postData.id = that.settings.$processInfo.id;
                option.postData.processId = that.settings.$processInfo.processId;
            }
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('操作成功！');
                    if(that.settings.$saveCallBack!=null){
                        that.settings.$saveCallBack();
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })

        }
        //保存验证
        ,save_validate:function(){
            var that = this;
            $(that.element).find('form.form-horizontal').validate({
                rules: {
                    receiptType:{
                        required:true
                    }
                },
                messages: {
                    receiptType:{
                        required:'请选择'+that._typeName+'类型!'
                    }
                },
                errorPlacement:function(error,element){
                    error.appendTo(element.parent());
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
