/**
 * 待审核组织－列表
 * Created by wrb on 2016/12/18.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_toAuditOrgList",
        defaults = {
            $isDialog:true,
            $dataUrl:null,
            $type:'2'//(type：2，分支机构：3，合作伙伴为)
            ,$toAuditCompanyList:[]
            ,$auditOrgCallBack:null
            ,$currentCompanyId:''
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
            this.initHtmlData(function (data) {
                that.settings.$currentCompanyId = window.currentCompanyId;
                var html = template('m_org/m_toAuditOrgList',{
                    toAuditCompanyList:data.toAuditCompanyList,
                    currentCompanyId:that.settings.$currentCompanyId
                });
                $(that.element).html(html);
                that.bindActionClick();
            });
        }
        //数据并加载模板
        ,initHtmlData:function (callBack) {

            var that = this;
            var option  = {};
            option.url = restApi.url_selectInvitedPartner;
            option.postData = {};
            option.postData.type = that.settings.$type;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    that.settings.$toAuditCompanyList = response.data.toAuditCompanyList;
                    $.each(that.settings.$toAuditCompanyList,function(i,item){
                        item.createDate = item.createDate.substr(0,11);
                    });
                    if(callBack!=null){
                        return callBack(response.data);
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //同意，拒绝
        ,auditOrg:function (i,status) {
            var that = this;
            var option  = {};
            option.url = restApi.url_auditOrgRelation;
            var obj = {};
            obj = that.settings.$toAuditCompanyList[i];
            obj.auditStatus=status;
            option.postData = obj;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success(response.info);
                    var $currentCompanyId = window.currentCompanyId;
                    if (obj.auditStatus == 0 && obj.type == 2 && $currentCompanyId == obj.orgPid) {//分支机构
                        addNodeByTreeByRoot(response.data, $currentCompanyId + 'subCompanyId');//添加节点
                    } else if (obj.auditStatus == 0 && obj.type == 3 && $currentCompanyId == obj.orgPid) {//合作伙伴
                        addNodeByTreeByRoot(response.data, $currentCompanyId + 'partnerId');//添加节点
                    }
                    that.initHtmlData(function (data) {
                        var html = template('m_org/m_toAuditOrgList',{toAuditCompanyList:data.toAuditCompanyList});
                        $(that.element).html(html);
                        that.bindActionClick();
                        if(that.settings.$auditOrgCallBack!=null){
                            that.settings.$auditOrgCallBack();
                        }
                    });

                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;
            $('.toAuditOrgListBox a[data-action]').on('click',function () {
                var dataAction = $(this).attr('data-action');
                if(dataAction=='orgRelationApproval0'){
                    var i = $(this).attr('data-i');
                    that.auditOrg(i,0);
                    return false;
                }else if(dataAction=='orgRelationApproval1'){
                    var i = $(this).attr('data-i');
                    that.auditOrg(i,1);
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
