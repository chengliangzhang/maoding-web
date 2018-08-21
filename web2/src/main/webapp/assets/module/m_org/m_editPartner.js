7/**
 * 创建事业合伙人
 * Created by wrb on 2016/12/17.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_editPartner",
        defaults = {
            title:'',
            type:'add',//add为添加，edit为编辑
            isDailog:true,
            companyId:'',
            companyObj:null,
            saveCallBack:null

        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData(function () {
                that.bindActionClick();
                that.saveCompany_validate();
                var options = {};
                /*if(that.settings.companyObj!=null){
                    options.$province = that.settings.companyObj.province;
                    options.$city = that.settings.companyObj.city;
                    //加载省市区
                    $("#citysBox").citySelect({
                        prov:that.settings.companyObj.province,
                        city:that.settings.companyObj.city,
                        dist:that.settings.companyObj.county,
                        nodata:"none",
                        required:false
                    });
                }else{
                    //加载省市区
                    $("#citysBox").citySelect({
                        nodata:"none",
                        required:false
                    });
                }
                //$('.branchCompanyOBox #citysBox').m_choseCity(options);
                $('.branchCompanyOBox input[name="serverType"]').bind('click',function(event){
                    $(this).parents('.serviceTypeEdit').find('span[name="severType"]').html('');
                });*/


            });
        }
        //初始化数据并加载模板
        ,initHtmlData:function (callBack) {
            var that = this;
            if(that.settings.isDailog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title||'创建事业合伙人',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '700',
                    height:'340',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    okText:'保存',
                    ok:function(){
                        var check_1 = $('form.branchCompanyOBox').valid();
                        //var check_2 = that.validateServerType();
                        if(!check_1){
                            return false;
                        }else{
                            that.saveCompany();
                        }
                    },
                    cancelText:'取消',
                    cancel:function(){

                    }
                },function(d){//加载html后触发

                    var $data = {};
                    $data.companyObj = {};//添加组织对象
                    $data.companyObj.editType = '1';
                    if(that.settings.companyObj!=null){
                        $data.companyObj.editType = '2';
                        $data.companyObj = that.settings.companyObj;
                    }
                    that.getProjectType(function (data) {

                        that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                        $data.currentCompanyId = window.currentCompanyId;
                        $data.serverTypeList = data;
                        var html = template('m_org/m_editPartner',$data);
                        $(that.element).html(html);
                        /*if(that.settings.companyObj!=null){
                            $('.branchCompanyOBox input[name="serverType"]').each(function () {
                                if($data.companyObj.serverType.indexOf($(this).val())>-1){
                                    $(this).attr('checked','checked');
                                }
                            });
                        }*/
                        if(callBack!=null){
                            callBack();
                        }
                    });

                });
            }else{//不以弹窗编辑
                var html = template('m_org/m_editPartner',{});
                $(that.element).html(html);
                if(callBack!=null){
                    callBack();
                }
            }
        }
        ,getProjectType:function (callBack) {
            var that = this;
            var option  = {};
            option.url = restApi.url_projectType;
            option.postData = {};
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    if(callBack!=null){
                        return callBack(response.data);
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //组织保存
        ,saveCompany:function () {
            var that = this;
            var option  = {};
            option.url = restApi.url_businessPartner;
            var $data = $("form.branchCompanyOBox").serializeObject();
            /*var serverTypeStr = '';
            var i=0;
            $('form.branchCompanyOBox input[name="serverType"]:checked').each(function(){
                serverTypeStr += $(this).val()  + ',';
                i++;
            });
            if(i>0){
                serverTypeStr = serverTypeStr.substring(0,serverTypeStr.length-1);
            }
            $data.serverType = serverTypeStr;*/
            if(that.settings.companyObj!=null){//编辑
                $data.id = that.settings.companyObj.id;
            }
            option.postData = $data;
            option.classId = '.branchCompanyOBox';
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    if(that.settings.saveCallBack!=null){
                        return that.settings.saveCallBack(response.data);
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //删除事业合伙人事件
        ,deleteSubCompany:function(e){
            var that = this;
            var option  = {};
            option.url = restApi.url_subCompany+'/'+that.settings.companyObj.id;
            S_dialog.confirm('您确定要删除吗？',function(){
                m_ajax.get(option,function (response) {
                    if(response.code=='0'){
                        S_toastr.success('删除成功！');
                        if(that.settings.isDailog){
                            S_dialog.close(e);
                            delNodeByTree();
                        }
                    }else {
                        S_dialog.error(response.info);
                    }
                })
            },function(){})

        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;
            $('.branchCompanyOBox button[data-action],.branchCompanyOBox a[data-action]').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'saveCompany'://保存组织
                        that.saveCompany($this);
                        return false;
                        break;
                    case 'deleteSubCompany'://删除组织
                        that.deleteSubCompany($this);
                        break;
                    case 'roleRightsPreView':
                        var option = {};
                        option.$type = $(that.element).find('input[name="roleType"]:checked').val();
                        $('body').m_roleRightsPreview(option);
                        break;
                }
            });

            /*$(that.element).find('input[name="roleType"]').on('click',function () {

                $(that.element).find('a[data-action="roleRightsPreView"]').addClass('hide');
                $(this).parent().find('a[data-action="roleRightsPreView"]').removeClass('hide');

            });*/

        }
        ,saveCompany_validate:function(){
            var that = this;
            $('form.branchCompanyOBox').validate({
                rules: {
                    companyName:{
                        required:true,
                        maxlength:50
                    },
                    /*companyShortName:"required",
                    province:"required",
                    city:"required",*/
                    cellphone: {
                        required: true,
                        minlength: 11,
                        isMobile: true
                    },
                    userName:{
                        required:true,
                        isEmpty:true
                    }
                    /*,adminPassword:{
                        required: true,
                        rangelength:[6,12],
                        checkSpace:true
                    }*/


                },
                messages: {
                    companyName:{
                        required:'请输入事业合伙人名称!',
                        maxlength:'事业合伙人名称不超过50位!'
                    },
                    /*companyShortName:"请输入事业合伙人简称!",
                    province:"请选择所在地区!",
                    city:"请选择所在地区!",*/
                    cellphone: {
                        required: "请输入手机号码",
                        minlength: "确认手机不能小于11个字符",
                        isMobile: "请正确填写您的手机号码"
                    },
                    userName:{
                        required:"请输入负责人姓名!",
                        isEmpty:'请输入负责人姓名!'
                    }
                    /*,adminPassword:{
                        required:'请输入卯丁账号密码！',
                        rangelength: "密码为6-12位！",
                        checkSpace: "密码不应含有空格!"
                    }*/
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    if (element.hasClass('prov') || element.hasClass('city')) {
                        error.appendTo(element.closest('.col-md-6'));
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            // 名称空格验证
            jQuery.validator.addMethod("isEmpty", function (value, element) {
                if($.trim(value)==''){
                    return false;
                }else{
                    return true;
                }

            }, "请输入负责人姓名!");
            $.validator.addMethod("checkSpace", function(value, element) {
                var pattern=/^\S+$/gi;
                return this.optional(element) || pattern.test( value ) ;
            }, "密码不应含有空格!");
            // 手机号码验证
            jQuery.validator.addMethod("isMobile", function(value, element) {
                var length = value.length;
                var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
                return this.optional(element) || (length == 11 && mobile.test(value));
            }, "请正确填写您的手机号码");
        }
        //验证服务类型是否为空
        ,validateServerType:function(){
            var len = $('.branchCompanyOBox input[name="serverType"]:checked').length;
            if(len>0){
                return true;
            }else{
                var html = '<label id="severType-error" class="error" for="severType">请选择服务类型!</label>';
                $('.branchCompanyOBox span[name="severType"]').html(html)
                return false;
            }
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
