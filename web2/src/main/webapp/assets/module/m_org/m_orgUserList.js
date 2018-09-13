/**
 * Created by wrb on 2016/12/15.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_orgUserList",
        defaults = {
            orgId:'',//必传参数
            currOrgTreeObj:null,
            orgUserList:[],
            isClick:false,
            deleteBtnCallBack:null
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
            this.initUserData();
        }
        //人员数据并加载模板
        ,initUserData:function () {

            var that = this;
            var option  = {};
            option.url = restApi.url_getOrgUserNoPage+'/'+that.settings.orgId;
            m_ajax.get(option,function (response) {
                if(response.code=='0'){
                    var $data = {};
                    $data.orgUserList = response.data;
                    that.settings.orgUserList = response.data;
                    $data.isClick = that.settings.isClick;
                    var html = template('m_org/m_orgUserList',$data);
                    $(that.element).html(html);
                    if(that.settings.deleteBtnCallBack!=null){
                        that.settings.deleteBtnCallBack();
                    }
                    rolesControl();
                    that.bindTrActionClick();
                    that.bindActionClick();
                }else {
                    S_layer.error(response.info);
                }

            })

        }
        //tr绑定事件
        ,bindTrActionClick:function () {
            $('tr.userListTr').on('click',function (event) {
                if(!($(event.target).closest('.btnReturnFalse').length>0)){
                    $(this).next().toggle();
                    if($(this).find('.td-first .fa-plus-square').length>0){
                        $(this).find('.td-first .fa').removeClass('fa-plus-square').addClass('fa-minus-square');
                    }else{
                        $(this).find('.td-first .fa').removeClass('fa-minus-square').addClass('fa-plus-square');
                    }
                }

            });

        }
        //编辑人员
        ,editOrgUser:function (i) {

            var that = this;
            var options = {};
            options.companyId = that.settings.currOrgTreeObj.companyId;
            options.realId = that.settings.currOrgTreeObj.realId;
            options.userObj = that.settings.orgUserList[i];
            options.doType = 'edit';//等于edit是编辑
            $('body').m_editUser(options);
        }
        //向上排序
        ,upSorting:function (i) {
            var that = this;
            var option  = {};
            option.url = restApi.url_orderCompanyUser;

            var param = {};
            param.companyUser1 = that.settings.orgUserList[i-0];
            param.companyUser2 = that.settings.orgUserList[i-0-1];
            param.orgId = that.settings.orgId;
            option.postData = param;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                   /* S_layer.tips('操作成功！');*/
                    that.initUserData();
                }else {
                    S_layer.error(response.info);
                }

            })
        }
        //向下排序
        ,downSorting:function (i) {
            var that = this;
            var option  = {};
            option.url = restApi.url_orderCompanyUser;

            var param = {};
            param.companyUser1 = that.settings.orgUserList[i-0];
            param.companyUser2 = that.settings.orgUserList[i-0+1];
            param.orgId = that.settings.orgId;
            option.postData = param;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                   /* S_layer.tips('操作成功！');*/
                    that.initUserData();
                }else {
                    S_layer.error(response.info);
                }

            })
        }
        //删除人员
        // ,delOrgUser:function (id) {
        //     var that = this;
        //     var option  = {};
        //     option.url = restApi.url_saveCompanyUser+'/'+id;
        //
        //     m_ajax.get(option,function (response) {
        //         if(response.code=='0'){
        //             S_toastr.success('操作成功！');
        //             that.initUserData();
        //
        //         }else {
        //             S_layer.error(response.info);
        //         }
        //
        //     })
        // }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;

            $('.orgUserOBox a[data-action]').on('click',function () {
                var _this = this;
                var dataAction = $(this).attr('data-action');
                if(dataAction=='editOrgUser'){//编辑人员
                    that.editOrgUser($(this).attr('data-i'));
                    return false;
                }else if(dataAction=='upSorting'){//向上排序
                    that.upSorting($(this).attr('data-i'));
                    return false;
                }else if(dataAction=='downSorting'){//向下排序
                    that.downSorting($(this).attr('data-i'));
                    return false;
                }else if(dataAction=='delOrgUser'){//删除人员

                    S_layer.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                        var option = {};
                        var id = $(_this).attr('data-id');
                        option.url = restApi.url_saveCompanyUser + '/' + id;
                        m_ajax.get(option, function (response) {
                            if (response.code == '0') {
                                S_toastr.success('删除成功！');
                                that.initUserData();
                            } else {
                                S_layer.error(response.info);
                            }
                        });

                    }, function () {
                    });
                    return false;
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
