/**
 * 数据字典选择(针对功能分类、设计范围这类编辑、自定义等)
 * Created by wrb on 2018/6/26.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_customDataDictionary",
        defaults = {
            $title:null,
            $isDialog:true,
            $dataDictionaryList:null,//数据字典
            $dataList:null,//数据列表（包括数据字典）
            $placement:null,
            $okCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._designRangeList = null;
        this._name = pluginName;
        this._strName = this.settings.$title?this.settings.$title:'';
        this._selectedData = [];//数据字典
        this._selectedCustomData = [];//非数据字典
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData();
        },
        //初始化数据
        initHtmlData:function () {
            var that = this;
            var $data = that.dealDataList();
            var $top = 'top',left = $(that.element).position().left;
            if(parseInt(left)>600){
                $top = 'left';
            }
            $(that.element).m_popover({

                placement: $top,
                content: template('m_project/m_customDataDictionary', $data),
                titleHtml: '<h3 class="popover-title">'+(that.settings.$title?that.settings.$title:'编辑')+'</h3>',
                onShown: function ($popover) {

                    that.bindActionClick();
                    that.editValid();
                    that.bindIcheckbox($('.data-dictionary'));
                },
                onSave: function ($popover) {

                    if ($('.data-dictionary form').valid()) {
                        that.saveData();
                    }else{
                        return false;
                    }

                }
            }, true);

        }
        //把数据解析(哪些是来自基础数据，来自自定义)
        ,dealDataList:function () {
            var that = this;
            var dataListClone = that.settings.$dataList;//已选中的数据
            var dataDictionaryList = that.settings.$dataDictionaryList;//数据字典
            var dataDictionarySelectedList=[];
            var customDataList=[];
            if(dataListClone!=null && dataListClone.length>0){
                for (var i = 0; i < dataListClone.length; i++) {
                    var isCon = false;
                    for (var j = 0; j < dataDictionaryList.length; j++) {
                        if (dataListClone[i].name == dataDictionaryList[j].name) {
                            isCon = true;
                            dataDictionaryList[j].selected = true;//初始化选中
                            dataDictionarySelectedList.push({
                                name:dataListClone[i].name,
                                id:dataListClone[i].id,
                                selected:true,
                                default:true
                            });
                            continue;
                        }
                    }
                    if (!isCon) {
                        dataListClone[i].selected = true;//初始化选中
                        customDataList.push({
                            name:dataListClone[i].name,
                            id:dataListClone[i].id,
                            selected:true,
                            default:false
                        });
                    }
                }
            }
            var $data = {};
            $data.dataDictionarySelectedList = dataDictionarySelectedList;
            $data.customDataList = customDataList;
            $data.dataDictionaryList = dataDictionaryList;
            that._selectedData = dataDictionarySelectedList;
            that._selectedCustomData = customDataList;
            return $data;
        }
        //保存功能分类编辑
        ,saveData:function (text) {
            var that = this;

            if($('.popover .editable-error-block').length>0){
                $('.popover .editable-error-block').html('');
            }
            var dataDictionaryList = [];
            $('.data-dictionary input[type="checkbox"][name="dataDictionary"]:checked').each(function () {

                dataDictionaryList.push({
                    name:$(this).val(),
                    id:$(this).attr('data-id'),
                    selected:true,
                    default:true
                });
            });
            $('.data-dictionary input[type="checkbox"][name="customDataDictionary"]:checked').each(function () {

                dataDictionaryList.push({
                    name:$(this).closest('.liBox').find('input[name="iptCustomDataDictionary"]').val(),
                    id:$(this).attr('data-id')==''?null:$(this).attr('data-id'),
                    selected:true,
                    default:false
                });
            });

            var newDataDictionaryList = dataDictionaryList.concat();
            var oldDataList = that._selectedData.concat(that._selectedCustomData);
            if(oldDataList!=null && oldDataList.length>0){
                $.each(oldDataList, function (i, item) {

                    var isRepeat = false;//不存在，则表明是删除，添加到数组，返回到后台
                    $.each(dataDictionaryList, function (i0, item0) {
                        if(item0.id!=null && item0.id!='' && item0.id==item.id){
                            isRepeat = true;
                            return false;
                        }
                    });
                    if(!isRepeat){
                        item.selected = false;
                        newDataDictionaryList.push(item);
                    }
                });
            }
            if(that.settings.$okCallBack!=null){
                 that.settings.$okCallBack(newDataDictionaryList);
            }
            return text;

         }
        //添加自定义功能分类事件
        ,bindAddDataDictionary:function (obj) {
            var that = this;

            var iHtml = '';
            iHtml+='<div class="col-md-4 liBox">';
            iHtml+='    <div class="col-md-2 no-padding" >';
            iHtml+='        <label class="i-checks" title="">';
            iHtml+='            <input name="customDataDictionary" class="checkbox" checked type="checkbox"/>';
            iHtml+='            <i></i>';
            iHtml+='        </label>';
            iHtml+='    </div>';
            iHtml+='    <div class="col-md-10 out-box" style="padding-left: 0;padding-right: 0;">';
            iHtml+='        <label class="input">';
            iHtml+='            <input class="form-control input-sm" type="text" maxlength="50" name="iptCustomDataDictionary" placeholder="请输入名称" />';
            iHtml+='        </label>';
            iHtml+='    </div>';
            iHtml+='</div>';

            obj.parent().before(iHtml);
            that.bindCustomDataDictionaryCk(obj.parent().prev());
            that.bindIcheckbox(obj.parent().prev());
            that.editValid();
        }
        //绑定checkbox显示
        ,bindIcheckbox:function($el){
            var that = this;
            var ifChecked = function (e) {
            };
            var ifUnchecked = function (e) {
            };
            $el.find('input[name="dataDictionary"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);

            var ifCheckedByCustom = function (e) {
                console.log(11)
            };
            var ifUncheckedByCustom = function (e) {

            };
            $el.find('input[name="customDataDictionary"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUncheckedByCustom.s', ifUnchecked).on('ifCheckedByCustom.s', ifChecked);

        }
        //绑定选择自定义功能分类事件
        ,bindCustomDataDictionaryCk:function (obj) {
            obj.find('input[name="customDataDictionary"]').on('ifUnchecked.s',function () {
                $(this).parents('.liBox').find('label.error').hide();
                $(this).parents('.liBox').remove();
                if($('.custom-data-dictionary div.liBox').length<1){
                    $('.popover .editable-error-block').html('');
                }
                return false;
            });
            obj.find('input[name="iptCustomDataDictionary"]').keyup(function () {
                $(this).parents('.liBox').find('label.error').hide();
                return false;
            });
        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;
            $('.data-dictionary').find('label[data-action]').on('click',function () {
                var dataAction = $(this).attr('data-action');
                if(dataAction=='addCustomDataDictionary'){
                    that.bindAddDataDictionary($(this));
                    return false;
                }
            });
        }
        //自定义功能分类时的验证
        ,editValid:function(){
            var that = this;
            $(".data-dictionary form").validate({
                rules: {
                    customDataDictionary:{
                        ckCustomDataDictionary:true
                    },
                    iptCustomDataDictionary:{
                        ckCustomDataDictionary:true,
                        ckIsRepeat:true,
                        maxlengthCK:50
                    }
                },
                messages: {
                    customDataDictionary:{
                        ckCustomDataDictionary:'请输入'+that._strName+'名称!'
                    },
                    iptCustomDataDictionary:{
                        ckCustomDataDictionary:'请输入'+that._strName+'名称!',
                        ckIsRepeat:'名称重复或已存在！',
                        maxlengthCK:that._strName+'名称不能超过50个字！'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置

                    $('.data-dictionary form').find('.error-box .col-md-12').html(error);
                }
            });
            $.validator.addMethod('ckCustomDataDictionary', function(value, element) {
                var isTrue = true;
                $('.data-dictionary form').find(' input[name="customDataDictionary"]:checked').each(function () {
                    var val = $(this).closest('.liBox').find('input[name="iptCustomDataDictionary"]').val();
                    if($.trim(val).length===0){
                        isTrue = false;
                        return false;
                    }
                });
                if(isTrue){
                    $('.data-dictionary form').find('.error-box .col-md-12').html('');
                }
                return  isTrue;
            }, '请输入'+that._strName+'名称!');
            $.validator.addMethod('ckIsRepeat', function(value, element) {
                var isTrue = true;
                var customDataList = [];
                $('.data-dictionary input[type="checkbox"][name="customDataDictionary"]:checked').each(function(){

                    var name = $(this).closest('.liBox').find('input[name="iptCustomDataDictionary"]').val();
                    customDataList.push(name);
                });

                if(customDataList.length>0){

                    var newCustomDataList = customDataList.sort();
                    for(var i=0;i<customDataList.length;i++){

                        if (newCustomDataList[i]==newCustomDataList[i+1]){

                            isTrue = false;
                        }
                    }
                    $.each(customDataList,function (i,item) {
                        $.each(that.settings.$dataDictionaryList, function (i0, item0) {
                            if(item==item0.name){
                                isTrue = false;
                            }
                        });
                    })
                }
                if(isTrue){
                    $('.data-dictionary form').find('.error-box .col-md-12').html('');
                }
                return  isTrue;
            }, '名称重复或已存在！');
            $.validator.addMethod('maxlengthCK', function(value, element) {
                var isTrue = true;
                $('.data-dictionary form').find(' input[name="customDataDictionary"]:checked').each(function () {
                    var val = $(this).closest('.liBox').find('input[name="iptCustomDataDictionary"]').val();
                    if($.trim(val).length>50){
                        isTrue = false;
                        return false;
                    }
                });
                if(isTrue){
                    $('.data-dictionary form').find('.error-box .col-md-12').html('');
                }
                return  isTrue;
            }, that._strName+'名称不能超过50个字');
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
