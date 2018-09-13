/**
 * 添加部门，编辑部门
 * Created by wrb on 2016/12/16.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_editDepart",
        defaults = {
            title:'',
            isDialog:true,
            departObj:null,
            doType:'add',//默认添加 edit=编辑
            saveCallBack:null,//保存回滚事件
            delCallBack:null//删除回滚事件

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
            var $data = {};
            $data.departObj={};
            $data.parentDepart=that.settings.departObj.parentDepart;
            $data.isDialog=that.settings.isDialog;
            if(that.settings.doType=='edit'){
                $data.departObj = that.settings.departObj;
                $data.doType = that.settings.doType;
            }
            var html = template('m_org/m_editDepart',$data);
            that.initHtmlData(html,function () {
                that.bindActionClick();
                that.saveDepart_validate();
            });
        }
        //初始化数据并加载模板
        ,initHtmlData:function (html,callBack) {
            var that = this;

            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.title||'添加部门',
                    area : '600px',
                    content:html,
                    btn:false

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;
                    if(callBack)
                        callBack();
                });

            }else{//不以弹窗编辑
                $(that.element).html(html);
                if(callBack)
                    callBack();
            }
        }
        //部门保存
        ,saveDepart:function (e) {
            var that = this;
            if($('form.editDepartOBox').valid()){
                var option  = {};
                option.url = restApi.url_saveOrUpdateDepart;
                if(that.settings.doType=='edit'){
                    that.settings.departObj.departName = $('.editDepartOBox input[name="departName"]').val();
                    option.postData = that.settings.departObj;
                }else{
                    var departObj = {};
                    departObj.parentDepart = that.settings.departObj.parentDepart;
                    departObj.departType='0';
                    departObj.pid = that.settings.departObj.parentDepart.id;
                    departObj.companyId = that.settings.departObj.parentDepart.companyId;
                    departObj.departName = $('.editDepartOBox input[name="departName"]').val();
                    option.postData = departObj;
                }

                // console.log(option.postData)
                m_ajax.postJson(option,function (response) {
                    if(response.code=='0'){
                        S_toastr.success('保存成功！');
                        if(that.settings.isDialog){
                            S_layer.close(e);
                        }
                        if(that.settings.saveCallBack!=null){
                            return that.settings.saveCallBack(response.data);
                        }
                    }else {
                        S_layer.error(response.info);
                    }
                })
            }
        }
        //删除部门
        ,delDepart:function (e) {
            var that = this;
            S_layer.confirm('部门下包含的人员或子部门将一起删除，此操作不可恢复。确定要继续吗？',function(){
                var options = {};
                options.url = restApi.url_depart+'/'+that.settings.departObj.id;
                m_ajax.get(options,function (response) {
                    if(response.code=='0'){
                        S_toastr.success('删除成功！');
                        if(that.settings.isDialog){
                            S_layer.close(e);
                        }
                        delNodeByTree();
                        if(that.settings.delCallBack!=null){
                            that.settings.delCallBack();
                        }
                    }else {
                        S_layer.error(response.info);
                    }
                })
            },function(){});
        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;
            $('.editDepartOBox button[data-action]').on('click',function () {
                var dataAction = $(this).attr('data-action');
                if(dataAction=='saveDepart'){//保存部门
                    that.saveDepart($(this));
                    return false;
                }else if(dataAction=='delDepart'){//删除部门
                    that.delDepart($(this));
                    return false;
                }
            });
        }
        ,saveDepart_validate:function(){
            var that = this;
            $('form.editDepartOBox').validate({
                rules: {
                    departName:{
                        required:true,
                        maxlength:50
                    }

                },
                messages: {
                    departName:{
                        required:'请输入部门名称!',
                        maxlength:'部门名称不超过50位!'
                    }
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
