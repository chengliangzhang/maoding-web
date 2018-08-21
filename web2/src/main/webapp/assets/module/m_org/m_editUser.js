/**
 * 添加人员，编辑人员
 * Created by wrb on 2016/12/16.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_editUser",
        defaults = {
            title: '',
            isDailog: true,
            userObj: null,
            doType: 'add'//默认添加 edit=编辑
            , saveCallBack: null
            , companyId: ''
            , realId: ''
            , orgList: []
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
                that.saveUser_validate();
            });
        }
        //初始化数据并加载模板
        , initHtmlData: function (callBack) {
            var that = this;
            if (that.settings.isDailog) {//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title || '添加人员',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '800',
                    height: '450',
                    maxHeight: '550',
                    tPadding: '0px',
                    url: rootPath + '/assets/module/m_common/m_dialog.html',
                    okText: '保存',
                    ok: function () {
                        if ( !(that.saveUser_depart_validate() && $('form.editUserOBox').valid())) {
                            return false;
                        } else {
                            that.saveUser();

                        }
                    },
                    cancel: function () {

                    }
                }, function (d) {//加载html后触发

                    var $data = {};
                    $data.memberObj = {};//添加人员对象
                    that.getDepartByCompanyId(function () {

                        $data.memberObj.departList = [];
                        $data.orgList = that.settings.orgList;
                        if (that.settings.userObj != null) {//编辑
                            $data.memberObj = that.settings.userObj;
                        } else {
                            var org = {
                                "id": null,
                                "companyId": null,
                                "departId": null,
                                "cuId": null,
                                "userId": null,
                                "serverStation": null
                            };
                            $data.memberObj.departList.push(org);
                            $data.memberObj.departList[0].departId = that.settings.realId;
                        }

                        var html = template('m_org/m_editUser', $data);
                        $('div[id="content:' + d.id + '"] .dialogOBox').html(html);
                        $('form.editUserOBox a[data-action="delDepartServerStation"]').eq(0).addClass('hide');
                        if (that.settings.userObj != null) {
                            that.dealSelectDisabled();
                        }
                        if (callBack != null) {
                            callBack();
                        }
                    });

                });
            } else {//不以弹窗编辑
                var html = template('m_org/m_editUser', {});
                $(that.element).html(html);
                if (callBack != null) {
                    callBack();
                }
            }
        }
        , getDepartByCompanyId: function (callBack) {
            var that = this;
            var option = {};
            option.url = restApi.url_getDepartByCompanyId + '/' + that.settings.companyId;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    that.settings.orgList = response.data;
                    if (callBack != null) {
                        callBack();
                    }
                } else {
                    S_dialog.error(response.info);
                }
            })
        }
        //人员保存
        , saveUser: function (e) {
            var that = this;
            var option = {};
            option.url = restApi.url_saveCompanyUser;
            var $data = $("form.editUserOBox").serializeObject();
            $data.departList = [];

            $('form.editUserOBox .departList').each(function () {
                var obj = {};
                obj.departId = $(this).find('select[name="departIdSel"] option:selected').val();
                obj.serverStation = $(this).find('input[name="serverStation"]').val();
                if (that.settings.userObj != null) {
                    var dataDepartId = $(this).find('select[name="departIdSel"]').attr('data-depart-id');
                    if (dataDepartId != undefined && dataDepartId != '') {
                        obj.id = dataDepartId;
                    }
                    obj.userId = that.settings.userObj.userId;
                }
                $data.departList.push(obj);
            })
            $data.serverStation = null;
            $data.companyId = that.settings.companyId;
            $data.cellphone = $('.editUserOBox input[name="cellphone"]').val();
            if (that.settings.userObj != null) {
                $data.id = that.settings.userObj.id;
                $data.userId = that.settings.userObj.userId;
            }
            option.postData = $data;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('保存成功！');
                    /*if(that.settings.isDailog){
                     S_dialog.close(e);
                     }*/
                    $('#organization_treeH a.jstree-anchor.jstree-clicked').click();//刷新员工页面数据
                    if (that.settings.doType == 'add') {
                        var count_num1 = $('.panel-footer span[data-key="companyUserCount"]').text();
                        var count_num2 = $('.panel-footer span[data-key="companyUserNotActiveCount"]').text();
                        $('.panel-footer span[data-key="companyUserCount"]').text(count_num1 - 0 + 1);
                        $('.panel-footer span[data-key="companyUserNotActiveCount"]').text(count_num2 - 0 + 1);
                    }
                    if (that.settings.saveCallBack != null) {
                        return that.settings.saveCallBack(response.data);
                    }
                } else {
                    S_dialog.error(response.info);
                }
            })
        }
        //添加职位
        , addDepartServerStation: function () {
            var that = this;
            var trHtml = $('.departListTbody').eq(0).clone();
            trHtml.find('input[name="serverStation"]').val('');
            trHtml.find('select[name="departIdSel"] option').removeAttr('selected');
            trHtml.find('a[data-action="delDepartServerStation"]').removeClass('hide');
            trHtml.find('select[name="departIdSel"]').attr('data-depart-id', '');
            trHtml.find('a[data-action="delDepartServerStation"]').on('click', function () {
                $(this).closest('tr').remove();
                that.dealSelectDisabled();
            });
            trHtml.find('select[name="departIdSel"]').on('change', function () {
                that.dealSelectDisabled();
                return false;
            });

            $('.addDepartListTbody').before(trHtml);

            that.dealSelectDisabled();

            // console.log(trHtml)
        }
        //部门选择处理
        , dealSelectDisabled: function () {
            var departIds = '';
            $('select[name="departIdSel"]').each(function () {
                var id = $(this).find('option:selected').val();
                if (id != '') {
                    departIds += id + ',';
                }

            });
            $('select[name="departIdSel"]').each(function () {
                $(this).find('option').each(function () {
                    $(this).removeAttr('disabled');
                    if ($(this).val() != '' && departIds.indexOf($(this).val()) > -1) {
                        $(this).attr('disabled', 'disabled');
                    }
                });

            });
        }
        , bindSelChangeClick: function () {
            var that = this;
            $('form.editUserOBox select[name="departIdSel"]').on('change', function () {
                that.dealSelectDisabled();
                return false;
            });
            $('.editUserOBox span.input-group-addon').off('click.iconBind').bind('click.iconBind',function(e){
                $(this).prev('input').focus();
                stopPropagation(e);
            });
        }
        //按钮事件绑定
        , bindActionClick: function () {
            var that = this;
            $('.editUserOBox button[data-action],.editUserOBox a[data-action]').on('click', function () {
                var dataAction = $(this).attr('data-action');
                /*if(dataAction=='saveUser'){//保存人员
                 that.saveUser($(this));
                 return false;
                 }else*/
                if (dataAction == 'addDepartServerStation') {
                    that.addDepartServerStation();
                } else if (dataAction == 'delDepartServerStation') {
                    $(this).closest('tr').remove();
                    that.dealSelectDisabled();
                }
            });
            that.bindSelChangeClick();
        }
        , saveUser_validate: function () {
            var that = this;
            $('form.editUserOBox').validate({
                rules: {
                    userName: 'required',
                    cellphone: {
                        required: true,
                        isMobile: true
                    }
                    , email: {
                        email: true
                    }

                },
                messages: {
                    userName: '请输入姓名！',
                    cellphone: {
                        required: '请输入手机号码！',
                        isMobile: "请正确填写您的手机号码"

                    }
                    , email: {
                        email: '请输入正确的邮箱!'
                    }
                }
            });
            // 手机号码验证
            jQuery.validator.addMethod("isMobile", function (value, element) {
                var length = value.length;
                var mobile = regularExpressions.mobile;
                return this.optional(element) || (length == 11 && mobile.test(value));
            }, "请正确填写您的手机号码");

        }
        //验证所属部门不能为空
        ,saveUser_depart_validate:function(){
            var that = this;
            var error = 0;
            $('tbody.departListTbody .departList select[name="departIdSel"]').each(function(){
                var $this = $(this);
                var value = $this.find('option:selected').val();
                if(value===null || value===void 0 || $.trim(value)===''){
                    var html = '<label id="departIdSel-error" class="error" for="cellphone">';
                    html += '所属部门不能为空';
                    html += '</label>';
                    if ($this.parent().find('#departIdSel-error').length < 1) {
                        $this.after(html);
                    }
                    error+=1;
                }
            });
            if(error>0){
                return false;
            }else{
                return true;
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
