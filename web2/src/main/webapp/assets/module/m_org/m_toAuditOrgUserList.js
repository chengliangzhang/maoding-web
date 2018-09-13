/**
 * 待审核人员
 * Created by wrb on 2016/12/18.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_toAuditOrgUserList",
        defaults = {
            $isDialog:true,
            $dataUrl:null,
            $userAuditList:[],
            $auditOrgUserCallBack:null
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

            this.initHtmlData();
        }
        //数据并加载模板
        ,initHtmlData:function () {

            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.title||'待审核人员',
                    area : ['750px','600px'],
                    content:html,
                    cancelText:'关闭',
                    cancel:function () {

                    }

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;

                    $(dialogEle).html('<form class="sky-form showAuditListOBox rounded-4x p-m" style="position: static; zoom: 1;">' +
                        '<fieldset><div id="toAuditOrgUser-pagination-container" class="m-pagination pull-right"></div>' +
                        '</fieldset></form>');

                    that.getDataByPage(function (data) {
                        $('table.toAuditOrgUserListBox').remove();
                        var html = template('m_org/m_toAuditOrgUserList',{userAuditList:data});
                        $('#toAuditOrgUser-pagination-container').before(html);
                        that.bindActionClick();
                    });
                });

            }else{//不以弹窗编辑
                $(that.element).html('<div id="toAuditOrgUser-pagination-container" class="m-pagination pull-right"></div>');
                that.getDataByPage(function () {
                    $('table.toAuditOrgUserListBox').remove();
                    var html = template('m_org/m_toAuditOrgUserList',{userAuditList:data});
                    $(that.element).prepend(html);
                    that.bindActionClick();
                });
            }

        }
        ,getDataByPage:function (callBack) {
            var that = this;
            var url = that.settings.$dataUrl!=null&&that.settings.$dataUrl!=''?that.settings.$dataUrl:restApi.url_getPendingAudiOrgUser ;
            var params = {};
            paginationFun({
                eleId:'#toAuditOrgUser-pagination-container',
                url:url,
                params:params
            },function(response){
                that.settings.$userAuditList = response.data.data;
                if(callBack!=null){
                    return callBack(response.data.data);
                }

            });
        }
        //同意，拒绝
        ,auditUser:function (obj) {
            var that = this;
            var option  = {};
            option.url = restApi.url_audiOrgUser;
            option.postData = obj;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success(response.info);
                    that.getDataByPage(function (data) {
                        $('table.toAuditOrgUserListBox').remove();
                        var html = template('m_org/m_toAuditOrgUserList',{userAuditList:data});
                        $('#toAuditOrgUser-pagination-container').before(html);

                        $('#organization_treeH .jstree-clicked').click();//刷新当前人员列表
                        if(that.settings.$auditOrgUserCallBack!=null){
                            that.settings.$auditOrgUserCallBack();
                        }

                    });
                }else {
                    S_layer.error(response.info);
                }
            })
        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;
            $('.toAuditOrgUserListBox a[data-action]').on('click',function () {
                var dataAction = $(this).attr('data-action');
                if(dataAction=='agreedToJoin'){
                    var i = $(this).attr('data-i');
                    var obj = {};
                    obj = that.settings.$userAuditList[i];
                    obj.auditStatus='1';
                    that.auditUser(obj);
                }else if(dataAction=='refusedToJoin'){
                    var i = $(this).attr('data-i');
                    var obj = {};
                    obj = that.settings.$userAuditList[i];
                    obj.auditStatus='3';
                    that.auditUser(obj);
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
