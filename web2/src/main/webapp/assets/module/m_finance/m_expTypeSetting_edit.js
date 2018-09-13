/**
 * 财务类别设置-编辑
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_expTypeSetting_edit",
        defaults = {
            expTypeData:null,
            expTypeList:null
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
            that.initThisCategoryList();
        }
        //获取数据并生成编辑页面
        ,initThisCategoryList: function () {
            var that = this;
            var $data = {};
            $data.expTypeData = that.settings.expTypeData;
            var html = template('m_finance/m_expTypeSetting_edit',$data);
            $('#expTypeSetting #expTypeMainContent').html(html);
            that.bindClickFun($data.expTypeData);

        }
        //刷新页面
        ,refeshPage:function () {
            var that = this;
            $('#content-right #content-box').m_expTypeSetting();
        }
        //给按钮绑定事件
        ,bindClickFun:function(data){
            var that = this;
            var deleteExpTypeList = [];
            $(that.element).find('a[data-action]').each(function(){
                var action = $(this).attr('data-action');
                $(this).off('click').bind('click',function(){

                    switch (action){
                        case 'categoryDelete'://删除按钮
                            var id = $(this).parents('tr').attr('id');
                            if($(this).closest('tbody').find('tr.categoryInput').length==1){
                                return S_toastr.warning('类别不能少于一条！');
                            }
                            $.each(data.child,function(i,item){
                                if(item.id!='' && item.id!=null && item.id == id){
                                    deleteExpTypeList.push(item);
                                }
                            });
                            $.each(deleteExpTypeList,function(j,items){
                                if(items.id!='' && items.id!=null && items.id == id) {
                                    data.child.splice(data.child.indexOf(items), 1);
                                }
                            });
                            $(this).parents('.categoryInput[id="'+id+'"]').remove();
                            return false;
                            break;
                        case 'cancelEditType'://取消按钮
                            that.refeshPage();
                            return false;
                            break;
                        case 'saveEditType'://保存按钮
                            var newTr = $('.categorySettingOBox tbody').find('tr[id]');
                            newTr.each(function(){
                                var newobj = {};
                                var $this = $(this);
                                var id = $(this).attr('id');
                                if(data.child && data.child.length>0){
                                    $.each(data.child,function(j,item){
                                        if(item.id==id){
                                            data.child[j].name = $this.find('input[name="categoryName"]').val();
                                            data.child[j].expTypeMemo = $this.find('input[name="categoryMemo"]').val();
                                        }
                                    })
                                }
                                if(id=='newTr'){
                                    var check = true;
                                    newobj.id=null;
                                    newobj.name = $this.find('input[name="categoryName"]').val();
                                    newobj.expTypeMemo = $this.find('input[name="categoryMemo"]').val();
                                    $.each(data.child,function(i,item){
                                        if((item.name==newobj.name)||newobj.name==''){
                                            return check = false;
                                        }
                                    });

                                    if(check){
                                        data.child.push(newobj);
                                    }
                                }
                            });
                            var option  = {};
                            option.url = restApi.url_expCategory;

                            var ExpTypeOutDTO={};
                            var expTypeList=that.settings.expTypeList;
                            expTypeList[expTypeList.indexOf(that.settings.expTypeData)]=data;
                            ExpTypeOutDTO.deleteExpTypeList=deleteExpTypeList;
                            ExpTypeOutDTO.expTypeDTOList=expTypeList;
                            option.postData=ExpTypeOutDTO;
                            if(that.validateCategoryName(data)){
                                m_ajax.postJson(option,function (response) {
                                    if(response.code=='0'){
                                        S_toastr.success('保存成功');
                                        that.refeshPage();
                                    }else {
                                        S_layer.error(response.info);
                                    }

                                })
                            }
                            return false;
                            break;
                    }

                });
            });

            $('div[data-action="addList"]').bind('click',function(){
                var i = $('.categorySettingOBox tbody tr').length;
                var a = $('.categorySettingOBox tbody').find('tr').eq(i-2).clone(true);

                a.find('input[name="categoryName"]').val('');
                a.find('input[name="categoryMemo"]').val('');
                a.attr('id','newTr');
                $('.addList').before(a);
            });
        }
        //验证类别名称
        ,validateCategoryName:function(data){
            var errors = [];
            $('.categorySettingOBox tr.categoryInput input[name="categoryName"]').each(function(){
                var categoryName = $(this).val();
                var j = $(this).parents('tr.categoryInput').prevAll().length;
                if(categoryName=='' || categoryName==null){
                    errors.push('类别名称不能为空！');
                }
                if(data.child && data.child.length>0){
                    $.each(data.child,function(i,item){
                        if(i!=j){
                            if(item.name == categoryName){
                                errors.push('类别名称不能重复！');
                                return ;
                            }
                        }
                    });
                }
            });
            if(errors.length>0){
                S_toastr.warning(errors[0]);
                return false;
            }else{
                return true;
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
