/**
 * Created by wrb on 2016/12/14.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_userList",
        defaults = {
              orgId: ''
            , userUrl: ''
            , selectUserCallback: null
            , delSelectedUserCallback: null
            , selectedUserList: null//选中的人员列表[{id,userId,userName}...]
            , isASingleSelectUser: false//是否单个选择人员，默认false,2为单选且提示不关窗
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._selectedUserIds = '';

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            this.initUserData();
        }
        //人员数据并加载模板
        , initUserData: function () {
            var that = this;
            var url = that.settings.userUrl != null && that.settings.userUrl != '' ? that.settings.userUrl : restApi.url_getOrgUser;
            var params = {};
            params.orgId = that.settings.orgId;
            paginationFun({
                eleId: '#userlist-pagination-container',
                loadingId: '.userListBox',
                url: url,
                params: params
            }, function (response) {

                var $data = {};
                $data.orgUserList = response.data.data;
                var html = template('m_org/m_userList', $data);
                $(that.element).html(html);
                that.dealChosedUserList();
                that.bindActionClick();
                if (that.settings.isASingleSelectUser != 2) {
                    that.dealChoseUserBtnClass();
                }

            });
        }
        //处理按钮样式与可否点击
        , dealChoseUserBtnClass: function () {
            var that = this;
            $(that.element).find('a[data-action="choseUser"]').each(function () {
                var id = $(this).attr('data-companyUserId');
                if (that._selectedUserIds != null && that._selectedUserIds.indexOf(id) > -1) {
                    $(this).addClass('btn-u-default');
                    $(this).css('cursor', 'default');
                } else {
                    $(this).removeClass('btn-u-default');
                    $(this).css('cursor', 'pointer');

                }
            });
        }
        //显示添加后的成员并给其绑定删除按钮
        , addChosedUserList: function (data) {
            var that = this;
            if ($('.chosedUserBox').length > 0) {

                if (that.settings.isASingleSelectUser && that.settings.isASingleSelectUser == 2) {//单选且不关窗

                    var _iHtml = '';
                    _iHtml += '<span style="background-color: rgb(245, 245, 245);padding: 2px 0px;">' +
                        '<span class="label label-light m-r-xs" style="display: inline-block">' + data.userName + '' +
                        '</span></span>';

                    $('.chosedUserBox').html(_iHtml);

                } else {//默认

                    var _iHtml = '';
                    _iHtml += '<span class=" m-r-xs" style="background-color: rgb(245, 245, 245);padding: 2px 0px;">' +
                        '<span class="label label-light m-r-xs" style="display: inline-block;padding-right: 0;background: transparent">' + data.userName + '' +
                        '<a class="curp" href="javascript:void(0)" data-id="' + data.companyUserId + '" data-userId="'+data.userId+'" data-action="delChosedUser">' +
                        '<i class="fa fa-times fc-red"></i></a></span></span>';

                    if(that.settings.isASingleSelectUser){//单选
                        $('.chosedUserBox').html(_iHtml);
                    }else{
                        $('.chosedUserBox').append(_iHtml);
                    }
                    that.bindDelChosedUser(data);
                    that.dealChosedUserList();
                    that.dealChoseUserBtnClass();
                }
            }
        }
        //删除添加的人员
        , bindDelChosedUser: function (data) {
            var that = this;
            $('.chosedUserBox').find('a[data-id="' + data.companyUserId + '"]').bind('click', function () {

                that.delChosedUser(this, data);
            });
        }
        //删除人员相关变量与样式处理
        , delChosedUser: function (obj, data) {
            var that = this;
            var d = $(obj).attr('data-id');
            $('.userListBox').find('a[data-companyuserid="' + d + '"]').removeClass('btn-u-default');
            $('.userListBox').find('a[data-companyuserid="' + d + '"]').css('cursor', 'pointer');
            $(obj).closest('span.label').remove();
            if (that.settings.selectedUserList != null) {
                for (var i = 0; i < that.settings.selectedUserList.length; i++) {
                    if (that.settings.selectedUserList[i].id == d) {
                        that.settings.selectedUserList.splice(i, 1);
                        break;
                    }
                }
            }
            that.dealChosedUserList();
            that.dealChoseUserBtnClass();
            if (that.settings.delSelectedUserCallback != null) {
                return that.settings.delSelectedUserCallback(data, obj);
            }
        }
        //把已选的人员列表转为id集合字符串，用于来作判断
        , dealChosedUserList: function () {
            var that = this;
            if (that.settings.selectedUserList != null) {
                that._selectedUserIds = '';
                var list = that.settings.selectedUserList;
                for (var i = 0; i < list.length; i++) {
                    that._selectedUserIds += list[i].id + ',';
                }
            }
        }
        //按钮事件绑定
        , bindActionClick: function () {
            var that = this;
            $('.userListBox a[data-action]').on('click', function () {

                var $this = $(this);
                if ($this.attr('data-action') == "choseUser") {//选择用户

                    if ($this.hasClass('btn-u-default')) {//已置灰
                        return false;
                    }

                    if (that.settings.isASingleSelectUser || that.settings.isASingleSelectUser == 2) {//单选且不关窗，这里把原selectedUserList设为空
                        /*var _isASingleSelectUser = false;
                         $(that.element).find('a[data-action="choseUser"]').each(function () {

                         if($(this).hasClass('btn-u-default')){
                         _isASingleSelectUser = true;
                         return false;
                         }
                         });
                         if(_isASingleSelectUser){
                         S_layer.tips('只能选择一人！');
                         return false;
                         }*/
                        that.settings.selectedUserList = [];
                    }

                    if (that.settings.selectUserCallback != null) {
                        var $data = {};
                        $data.userId = $this.attr('data-userId');//用户账户ID
                        $data.companyUserId = $this.attr('data-companyUserId');//组织人员ID
                        $data.userName = $this.parent().parent().find('td:eq(0)').text();
                        $data.id = $this.parents('.layui-layer').attr('times');

                        if (that.settings.selectedUserList == null) {

                            that.settings.selectedUserList = [];
                        }
                        that.settings.selectedUserList.push({id: $data.companyUserId, userName: $data.userName,userId:$data.userId});//每选中，就在selectedUserList添加人员记录
                        $data.selectedUserList = that.settings.selectedUserList;
                        that.addChosedUserList($data);
                        $data.selectedUserIds = that._selectedUserIds;
                        return that.settings.selectUserCallback($data, $this);
                    }
                }
            });

            if ($('.chosedUserBox').length > 0) {
                $('.chosedUserBox a[data-action="delChosedUser"]').on('click', function () {

                    that.delChosedUser(this);
                });
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
