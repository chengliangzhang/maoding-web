/**
 *
 * Created by veata on 2016/12/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_changeManager",
        defaults = {
            adminInfo:{},
            title:null,
            type:null,//type=1：系统管理员，type=2：企业负责人
            oldSysManagerUserId:null,//原此系统管理员的用户ID
            oldOrgManagerUserId:null,//原企业负责人的用户ID
            companyId:null,
            saveCallback:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._type = 1;//1=移交管理员，2＝指定管理员，3＝移交企业负责人，4＝指定企业负责人
        this._title = '移交管理员';
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData();
        }
        ,initHtmlData:function(){
            var that = this;

            if(that.settings.type!=null && that.settings.type==1 && that.settings.oldSysManagerUserId!=null && that.settings.oldSysManagerUserId == window.currentUserId){//移交管理员
                that._type = 1;
                that._title = '移交管理员';
            }else if(that.settings.type!=null && that.settings.type==1 && that.settings.oldSysManagerUserId!=null && that.settings.oldSysManagerUserId != window.currentUserId){//指定管理员
                that._type = 2;
                that._title = '指定管理员';
            }else if(that.settings.type!=null && that.settings.type==2 && that.settings.oldOrgManagerUserId!=null && that.settings.oldOrgManagerUserId == window.currentUserId){//移交企业负责人
                that._type = 3;
                that._title = '移交企业负责人';
            }else if(that.settings.type!=null && that.settings.type==2 && that.settings.oldOrgManagerUserId!=null && that.settings.oldOrgManagerUserId != window.currentUserId){//指定企业负责人
                that._type = 4;
                that._title = '指定企业负责人';
            }
            var $data = {};
            $data.adminInfo = that.settings.adminInfo;
            $data.type = that._type;
            var html = template('m_role/m_changeManager',$data);
            S_layer.dialog({
                title:that._title,//that.settings.title||'选择人员',
                area : '400px',
                content:html,
                cancel:function () {
                },
                ok:function () {

                    var flag = $("form.ensureChangeAdministraterOBox").valid();
                    if(flag){
                        var data = that.settings.adminInfo;
                        data.companyId = that.settings.companyId;
                        //data.adminPassword = $('.dialogOBox').find('input[name="adminPassword"]').val();
                        // data.newAdminPassword = $('.dialogOBox').find('input[name="newAdminPassword"]').val();

                        if(that._type==1 || that._type==3){
                            data.flag='1'
                        }else {
                            data.flag='2';
                        }
                        data.type = that.settings.type;


                        var options= {};
                        options.classId = '#content-right';
                        options.url = restApi.url_transferSys;
                        options.postData = data;
                        m_ajax.postJson(options,function (response) {
                            if(response.code=='0'){

                                if(that.settings.type!=null && that.settings.type==1){//管理员
                                    if(that.settings.adminInfo!=null && that.settings.adminInfo.userId!=window.currentUserId
                                        && that.settings.oldOrgManagerUserId!=null && that.settings.oldOrgManagerUserId!=window.currentUserId){//企业负责人或系统管理员皆不是指定的人
                                        S_layer.success('处理成功，请重新登录!','提示',function(){
                                            window.location.href = rootPath+'/iWork/sys/logout';
                                        })
                                    }else{
                                        S_toastr.success('处理成功！');
                                        if(that.settings.saveCallback!=null){
                                            that.settings.saveCallback();
                                        }
                                    }
                                    that.permissionSettings();//刷新组件
                                }else{//企业负责人
                                    if(that.settings.adminInfo!=null && that.settings.adminInfo.userId!=window.currentUserId
                                        && that.settings.oldSysManagerUserId!=null && that.settings.oldSysManagerUserId!=window.currentUserId){//企业负责人或系统管理员皆不是指定的人
                                        S_layer.success('处理成功，请重新登录!','提示',function(){
                                            window.location.href = rootPath+'/iWork/sys/logout';
                                        })
                                    }else {
                                        S_toastr.success('处理成功！');
                                        if(that.settings.saveCallback!=null){
                                            that.settings.saveCallback();
                                        }
                                    }
                                    that.permissionSettings();//刷新组件
                                }
                            }else {
                                S_layer.error(response.info);
                            }
                        });
                    }else{
                        return false;
                    }
                }

            },function(layero,index,dialogEle){//加载html后触发

            });
        }
        //移交管理员密码的表单验证
        ,submit_validate:function(){
            $("form.ensureChangeAdministraterOBox").validate({
                rules: {
                    adminPassword: "required",
                    newAdminPassword: {
                        required: true,
                        rangelength:[6,12],
                        checkSpace:true
                    }
                },
                messages: {
                    adminPassword: "请输入管理密码！",
                    newAdminPassword: {
                        required:'请输入移交后的管理员密码！',
                        rangelength: "密码为6-12位！",
                        checkSpace: "密码不应含有空格!"
                    }

                }
            });
            $.validator.addMethod("checkSpace", function(value, element) {
                var pattern=/^\S+$/gi;
                return this.optional(element) || pattern.test( value ) ;
            }, "密码不应含有空格!");
        }
        //权限设置
        ,permissionSettings:function () {
            var option = {};
            option.isAddUser = 1;
            $('#content-box').m_roleList(option);
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
