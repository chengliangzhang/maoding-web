/**
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_teamInfoEdit",
        defaults = {
            teamInfo:null,
            serverTypeList:null,
            fastUrl:null
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
            that.initMainContent();
        }
        //加载主要页面
        ,initMainContent:function(){
            var that = this;
            var $data = {};
            $data.teamInfo = that.settings.teamInfo;
            $data.serverTypeList = that.settings.serverTypeList;
            var filePath = that.settings.teamInfo.filePath;
            var isHaveFile = false;
            if(filePath!=null && filePath!=""){
                filePath = that.settings.fastUrl + filePath;
                isHaveFile = true;
            }else{
                filePath = rootPath+"/assets/img/withoutLogo.png";
            }
            $data.filePath = filePath;
            var html = template('m_teamInfo/m_teamInfoEdit',$data);
            $('#infoMainOBox').html(html);
            var option = {};
            option.filePath=filePath;
            option.fastUrl=that.settings.fastUrl;
            option.isEdit='yes';
            option.isHaveFile = isHaveFile;
            $('#teamPicShow').m_teamPicUpload(option);
            //that.getProvinceAndCitys(that.settings.teamInfo);
            that.getCheckSeverType(that.settings.teamInfo);
            that.bindClickForSaveBtn(that.settings.teamInfo);
            that.saveTeamInfo_validate();
            $(that.element).find('input[name="serverType"]').bind('click',function(event){
                $(this).parents('.serviceTypeEdit').find('span[name="severType"]').html('');
            });

            //加载省市区
            $("#city_1").citySelect({
                prov:that.settings.teamInfo.province,
                city:that.settings.teamInfo.city,
                dist:that.settings.teamInfo.county,
                nodata:"none",
                required:false
            });

        }
        //获取省市编辑器
        ,getProvinceAndCitys:function(teamInfo){
            var that = this;
            var options={};
            options.$province = teamInfo.province;
            options.$city = teamInfo.city;
            $('.choseProvinceCity').m_choseCity(options);
        }
        //筛选出已选有的服务类型
        ,getCheckSeverType:function(teamInfo){
            var that = this;
            var serverTypeList = teamInfo.serverTypeList;
            $('.serviceType input[name="serverType"]').each(function(){
                var id = $(this).val();
                var $this = $(this);
                $.each(serverTypeList,function(i,item){
                    if(id==item.id){
                        $this.prop('checked',true);
                    }
                });
            });
        }
        //给上传头像按钮与保存按钮绑定事件
        ,bindClickForSaveBtn:function(tInfo){
            var that = this;
            $('#infoMainOBox button[data-action="saveTeamInfo"]').bind('click',function(){
                var action = $(this).attr("data-action");
                if(action == "saveTeamInfo"){//保存按钮
                    var $data = $('#infoMainOBox form').serializeObject();
                    var teamInfo = tInfo;
                    teamInfo.companyName=$data.companyName;
                    teamInfo.companyShortName=$data.companyShortName;
                    teamInfo.province=$data.province;
                    teamInfo.city=$data.city;
                    teamInfo.county=$data.county;
                    teamInfo.companyAddress=$data.companyAddress;
                    teamInfo.companyPhone=$data.companyPhone;
                    teamInfo.companyFax=$data.companyFax;
                    teamInfo.companyEmail=$data.companyEmail;
                    teamInfo.companyComment=$data.companyComment;
                    teamInfo.serverType = '';
                    $('.serviceType input[name="serverType"]:checked').each(function(){
                        var id = $(this).val();
                        teamInfo.serverType+=id+',';
                    });
                    teamInfo.serverType = teamInfo.serverType.substr(0,teamInfo.serverType.length-1);
                    var options = {};
                    options.url = restApi.url_saveOrUpdateCompany;
                    options.postData=teamInfo;
                    var flag1=$('form#sky-form1').valid();
                    var flag2=that.validateServerType();
                    if(flag1&&flag2){
                        m_ajax.postJson(options,function (response) {
                            if(response.code=='0'){
                                S_toastr.success('保存成功！');
                                tInfo = teamInfo;
                                $('#mainContain').find('a[data-action="abandonEditTeamInfo"]').click();
                            }else {
                                S_dialog.error(response.info);
                            }
                        });
                    }

                }

                return false;
            });
        }
        ,saveTeamInfo_validate:function(){
            var that = this;
            $('form#sky-form1').validate({
                rules: {
                    companyName:{
                        required:true,
                        maxlength:50
                    },
                    companyShortName:"required",
                    province:"required",
                    city:"required"


                },
                messages: {
                    companyName:{
                        required:'请输入组织名称!',
                        maxlength:'组织名称不超过50位!'
                    },
                    companyShortName:"请输入组织简称!",
                    province:"请选择所在地区!",
                    city:"请选择所在地区!"
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    if (element.hasClass('prov') || element.hasClass('city')) {
                        error.appendTo(element.closest('.choseProvinceCity'));
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        }
        //验证服务类型是否为空
        ,validateServerType:function(){
            var len = $('#sky-form1 input[name="serverType"]:checked').length;
            if(len>0){
                return true;
            }else{
                var html = '<label id="severType-error" class="error" for="severType">请选择服务类型!</label>';
                $('#sky-form1 span[name="severType"]').html(html)
                return false;
            }
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
