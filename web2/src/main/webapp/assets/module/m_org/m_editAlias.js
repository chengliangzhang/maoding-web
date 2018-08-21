/**
 * 设置别名
 * Created by wrb on 2016/12/16.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_editAlias",
        defaults = {
            title:'',
            isDailog:true,
            companyId:null,
            companyOriginalName:null,
            companyAlias:null,
            relationTypeId:null,
            saveCallback:null
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
            that.initHtmlData();
        }
        //初始化数据并加载模板
        , initHtmlData:function () {
            var that = this;
            S_dialog.dialog({
                title: that.settings.title||'编辑事业合伙人',
                contentEle: 'dialogOBox',
                lock: 3,
                width: '430',
                tPadding: '0',
                url: rootPath+'/assets/module/m_common/m_dialog.html',
                ok:function(){
                    if(!$('form.editAliasForm').valid()){
                        return false;
                    }else{
                        that.submitFunction();
                    }
                },
                okText:'保存',
                cancel:function(){

                },
                cancelText:'取消'
            },function(d){//加载html后触发

                that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                var opt = {
                    companyOriginalName:that.settings.companyOriginalName,
                    companyAlias:that.settings.companyAlias,
                    relationTypeId:that.settings.relationTypeId
                };
                var html = template('m_org/m_editAlias',opt);
                $(that.element).html(html);
                that.initValidate();
                that.bindActionClick();
            });
        }
        , submitFunction:function () {
            var that = this;
            var option  = {};
            option.url = restApi.url_setBusinessPartnerNickName;
            option.postData={
                companyId : that.settings.companyId,
                companyName :  $(that.element).find('input[name="companyName"]').val(),
                relationTypeId  : $(that.element).find('input[name="roleType"]:checked').val()
            };
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    if(that.settings.saveCallback)
                        that.settings.saveCallback();
                }else {
                    S_dialog.error(response.info);
                }
            });
        }
        //按钮事件绑定
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'roleRightsPreView':
                        var option = {};
                        option.$type = $(that.element).find('input[name="roleType"]:checked').val();
                        $('body').m_roleRightsPreview(option);
                        break;
                }
            });
        }
        ,initValidate:function(){
            var that = this;
            $('form.editAliasForm').validate({
                rules: {
                    companyName:{
                        required:true
                    }/*,
                    companyAlias:{
                        required:true
                    }*/
                },
                messages: {
                    companyName:{
                        required:'请输入组织名称!'
                    }/*,
                    companyAlias:{
                        required:'请输入别名!'
                    }*/
                },
                errorPlacement: function (error, element) {
                    error.insertAfter(element);
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
