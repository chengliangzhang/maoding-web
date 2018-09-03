/**
 * 设置显示字段
 * Created by wrb on 2018/05/04.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_payments_setFields",
        defaults = {
            $title:null,
            $isDialog:true,
            $feeTypeList:null,//选中的
            $eleId:null,//定位元素
            $okCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._selectedList = [];//选中的项

        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderHtml();
        },
        //初始化数据
        renderHtml:function () {
            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                that.getCostType(function (data) {
                    var html = template('m_payments/m_payments_setFields',{fieldsList:data});

                    var iTextObj = html.getTextWH();
                    var iWHObj = setDialogWH(iTextObj.width,iTextObj.height);

                    S_dialog.dialog({
                        //title: that.settings.$title||'设置显示字段',
                        contentEle: 'dialogOBox',
                        ele:that.settings.$eleId,
                        lock: 2,
                        align: 'bottom right',
                        quickClose:true,
                        noTriangle:true,
                        width: 500,
                        height:200,
                        tPadding: '0px',
                        url: rootPath+'/assets/module/m_common/m_dialog.html',
                        cancel:function () {
                        },
                        ok:function () {
                            return that.settings.$okCallBack(that._selectedList);
                        }

                    },function(d){//加载html后触发

                        that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                        $(that.element).html(html);
                        that.initICheck();
                        if(that.settings.$feeTypeList!=null && that.settings.$feeTypeList.length>0){
                            $.each(that.settings.$feeTypeList,function (i,item) {
                                $(that.element).find('input[name="typeValueCk"][data-key="'+item+'"]').iCheck('check');
                            });
                        }
                    });
                })

            }else{//不以弹窗编辑

            }
        }
        //获取字段信息
        , getCostType:function (callBack) {
            var option = {};
            option.url = restApi.url_getCostType;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    return callBack(response.data);
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //初始ICheck
        ,initICheck:function () {
            var that = this;
            var ifItemChecked = function (e) {
                var $ele = $(this).closest('li').find('input[name="typeValueCk"]');
                $ele.prop('checked',true);
                $ele.iCheck('update');
                that.isAllCheck($(this));
                that.addItemToRight($ele);
            };
            var ifItemUnchecked = function (e) {
                var $ele = $(this).closest('li').find('input[name="typeValueCk"]');
                $ele.prop('checked',false);
                $ele.iCheck('update');
                that.isAllCheck($(this));
                that.delItemToRight($ele);
            };
            $(that.element).find('input[name="typeValueCk"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifItemUnchecked).on('ifChecked.s', ifItemChecked);
        }
        //判断全选是否该选中并给相关处理
        ,isAllCheck:function ($this) {
           var $pEle = $this.closest('ul').prev().find('input[name="typeValueCk"]');
           var $ppEle = $this.closest('ul').parents('ul').prev().find('input[name="typeValueCk"]');
           var noCheckLen = $this.closest('ul').find('input[name="typeValueCk"]').length;
           var checkedLen = $this.closest('ul').find('input[name="typeValueCk"]:checked').length;
           if(noCheckLen==checkedLen && $pEle.length>0){
               $pEle.prop('checked',true);
               $pEle.iCheck('update');
               if($ppEle.length>0){
                   $ppEle.prop('checked',true);
                   $ppEle.iCheck('update');
               }
           }else{
               $pEle.prop('checked',false);
               $pEle.iCheck('update');
               if($ppEle.length>0 && noCheckLen==checkedLen){
                   $ppEle.prop('checked',true);
                   $ppEle.iCheck('update');
               }else if($ppEle.length>0 && $pEle.length==0 && noCheckLen==checkedLen){
                   $ppEle.prop('checked',false);
                   $ppEle.iCheck('update');
               }else{
                   $ppEle.prop('checked',false);
                   $ppEle.iCheck('update');
               }
           }
        }
        //选中出现在右边
        , addItemToRight:function ($this) {
            var that = this;
            $this.each(function () {
                var fieldKey = $(this).attr('data-key'),level = $(this).attr('data-level');
                if($.inArray(fieldKey, that._selectedList)<0 && level==3){
                    that._selectedList.push(fieldKey);
                    //$(that.element).find('#fieldItems').append('<li class="list-group-item"><span class="field-name" data-key="'+fieldKey+'">'+fieldKey+'</span><i class="glyphicon glyphicon-remove pull-right curp"></i></li>');

                }
            });
            /*$(that.element).find('#fieldItems .glyphicon-remove').off('click').on('click',function () {

                var fieldKey = $(this).closest('li.list-group-item').find('.field-name').text();
                that._selectedList.splice($.inArray(fieldKey,that._selectedList),1);
                $(this).closest('li.list-group-item').remove();
                $(that.element).find('input[name="typeValueCk"][data-key="'+fieldKey+'"]').iCheck('uncheck');
                return false;
            });*/
        }
        //删除右边
        , delItemToRight:function ($this) {
            var that = this;
            $this.each(function () {
                var fieldKey = $(this).attr('data-key');
                that._selectedList.splice($.inArray(fieldKey,that._selectedList),1);
                //$(that.element).find('span.field-name[data-key="'+fieldKey+'"]').closest('li.list-group-item').remove();

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


