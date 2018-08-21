/**
 * 报销条目
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_addExpDetailTr",
        defaults = {
            tableId:'',
            detailLen:0,
            callBack1:null,//传送expTypeData条目的方法
            // callBack2:null//当编辑报销明细时，打开弹窗生成明细后调用的方法
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
            that.getAddExpData();
        }
        //给删除按钮添加事件
        ,bindDeleteClickFun:function(pTarget){
            var that = this;
            var a = $(that.settings.tableId).find(pTarget);
            a.find('a[data-action="deleteDetail"]').bind('click',function(event){
                var $_target = $(this).closest('tr');
                $_target.remove();
                stopPropagation(event);
            });
        }
        //添加报销明细
        ,getAddExpData: function () {
            var that = this;
            var $data = {};
            var len = $(that.settings.tableId +' tbody tr').length-2;//现有的报销明细条数
            /************获取关联项目列表************/
            var option  = {};
            option.url = restApi.url_getExpBaseData;
            // option.postData={};
            m_ajax.get(option,function (response) {
                if(response.code=='0'){
                    $data.detail = that.settings.detailLen>0?that.settings.detailLen-0+1:1;;
                    $data.projectList = response.data.projectList;
                    var html = template('m_finance/m_addExpDetailTr',$data);
                    $('#toExpApplication .call-action-v1-boxed').closest('tr').before(html);
                    /************加载报销类别表单************/
                    var options = {};
                    options.delButtonType = 1;
                    options.parentTarget = 'tr[target="target'+(that.settings.detailLen-0+1)+'"]';
                    options.callBack1 = that.settings.callBack1;
                    $('#reimburseTable').m_expTypeSelect(options);
                    if($('#reimburseTable tbody tr').eq(0).find('a[data-action="deleteDetail"]').length>0){

                        $('#reimburseTable tbody tr').eq(0).find('a[data-action="deleteDetail"]').remove();
                    }
                    that.bindDeleteClickFun(options.parentTarget);
                    that.bindMouseBlur(options.parentTarget);
                }else {
                    S_dialog.error(response.info);
                }

            });
        }
        //给输入金额绑定鼠标移除事件
        ,bindMouseBlur:function(pTarget){
            var that = this;
            $(pTarget+' input#expAmount').bind('blur',function(){
                var $this = $(this);
                that.numberFilterFun($this);
            });
            $(pTarget+' input#expAmount').focus(function(){
                var $this = $(this);
                var $el = $this.next('#moneyError');
                if($el.length>0) {return $el.remove();}
            });
        }
        //当光标移除金额时判断输入金额是否为0-9数字，如不是则清空
        ,numberFilterFun:function(obj){
            var reg = new RegExp(/^\d+(\.\d+)?$/);
            var val = obj.val();
            var len = val.substr(val.indexOf('.'),val.length-1).length;
            if((!reg.test(val)) || len>3){
                obj.val('');
                var iHtml = '<div id="moneyError" style="color:red;">请输入小数点不超过两位的数字金额!</div>';
                if(obj.next('#moneyError').length<1){
                    obj.after(iHtml);
                }
                return false;
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
