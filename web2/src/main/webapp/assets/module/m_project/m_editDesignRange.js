/**
 * 设计范围
 * Created by wrb on 2016/12/20.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_editDesignRange",
        defaults = {
            $title:null,
            $isDialog:true,
            $projectId:'',
            $projectDesignRange:null,
            $okCallBack:null
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
            });
        },
        //初始化数据
        initHtmlData:function (callBack) {
            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'设计范围',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '800',
                    minHeight:'250',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {
                        
                    },
                    ok:function () {
                        var check = $(".addRangeOBox form").valid();
                        if(!check){
                            return false;
                        }else{
                            var projectDesignRangeList = [];
                            $('.addRangeOBox input[type="checkbox"][name="range"]:checked').each(function () {
                                projectDesignRangeList.push({designRange:$(this).val()});
                            });
                            $('.addRangeOBox input[type="checkbox"][name="otherRange"]:checked').each(function () {
                                projectDesignRangeList.push({designRange:$(this).closest('.liBox').find('input[name="designRange"]').val()});
                            });
                            if(that.settings.$okCallBack!=null){
                                return that.settings.$okCallBack(projectDesignRangeList);
                            }
                        }


                    }
                    
                },function(d){//加载html后触发
                    that.element = '#content:'+d.id;
                    that.getRangeData(function (data) {

                        var $data = that.dealDesignRange();
                        var classIdObj = $('div[id="content:'+d.id+'"] .dialogOBox');
                        that.initHtmlTemplate(callBack,$data,classIdObj)
                    });


                });
            }else{//不以弹窗编辑
                var $data = that.dealDesignRange();
                var classIdObj = $(that.element);
                that.initHtmlTemplate(callBack,$data,classIdObj)
            }
        }
        //生成html
        ,initHtmlTemplate:function (callBack,data,classIdObj) {
            var that = this;
            var html = template('m_project/m_editDesignRange',data);
            classIdObj.html(html);
            if(callBack!=null){
                callBack();
            }
            //给所有已有的自定义范围的checkbox添加事件
            $.each(data.otherRange,function(i,item){
                var obj = $('input#'+item.id).parents('.liBox');
                that.bindOtherRangeCk(obj);
            });
        }
        //把设计范围解析(哪些是来自基础数据，来自自定义)
        ,dealDesignRange:function () {
            var that = this;
            var rangeListClone = that.settings.$projectDesignRange;//已选中的设计范围
            var designRangeList = that.settings.$designRangeList;//数据字典的设计范围
            var rangeList=[];
            var otherRange=[];
            if(rangeListClone!=null && rangeListClone.length>0){
                for (var i = 0; i < rangeListClone.length; i++) {
                    var isCon = false;
                    for (var j = 0; j < designRangeList.length; j++) {
                        if (rangeListClone[i].designRange == designRangeList[j].name) {
                            isCon = true;
                            designRangeList[j].isChecked = 1;//初始化选中
                            rangeList.push(rangeListClone[i]);
                            continue;
                        }
                    }
                    if (!isCon) {
                        rangeListClone[i].isChecked = 1;//初始化选中
                        otherRange.push(rangeListClone[i]);
                    }
                }
            }
            var $data = {};
            $data.rangeList = rangeList;
            $data.otherRange = otherRange;
            $data.designRangeList = designRangeList;
            return $data;
        }
        //获取设计范围基础数据
        ,getRangeData:function (callBack) {
            var that = this;
            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_getDesignRangeList;
            m_ajax.get(option,function (response) {
                if(response.code=='0'){
                    that.settings.$designRangeList = response.data;
                    return callBack(response.data);
                }else {
                    S_dialog.error(response.info);
                }

            })
        }
        //添加自定义设计范围事件
        ,bindAddRange:function (obj) {
            var that = this;

            var iHtml = '';
            iHtml+='<div class="col-md-3 liBox">';
            iHtml+='    <div class="col-md-2 no-padding" >';
            iHtml+='        <label class="checkbox" title="">';
            iHtml+='            <input name="otherRange" class="checkbox" checked type="checkbox"/>';
            iHtml+='            <i></i>';
            iHtml+='        </label>';
            iHtml+='    </div>';
            iHtml+='    <div class="col-md-10 out-box" style="padding-left: 0;padding-right: 0;">';
            iHtml+='        <label class="input">';
            iHtml+='            <input id="designRange" class="designRange form-control input-sm" type="text" name="designRange" placeholder="请输入名称" />';
            iHtml+='        </label>';
            iHtml+='    </div>';
            iHtml+='</div>';

            obj.parent().before(iHtml);
            that.bindOtherRangeCk(obj.parent().prev());
            that.editOtherRangeValid();
        }
        //绑定选择自定义设计范围事件
        ,bindOtherRangeCk:function (obj) {
            obj.find('input[name="otherRange"]').on('click',function () {
                $(this).parents('.liBox').find('label.error').hide();
                $(this).parents('.liBox').remove();
            });
            obj.find('input[name="designRange"]').keyup(function () {
                $(this).parents('.liBox').find('label.error').hide();
            });
        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;
            $('.addRangeOBox').find('label[data-action]').on('click',function () {
                var dataAction = $(this).attr('data-action');
                if(dataAction=='addOtherRange'){
                    that.bindAddRange($(this));
                    return false;
                }
            });
        }
        //自定义范围时的验证
        ,editOtherRangeValid:function(){
            var that = this;
            $(".addRangeOBox form").validate({
                onfocusout:false,
                rules: {
                    otherRange:{
                        ckDesignRange:true
                     }
                },
                messages: {
                    otherRange:{
                        ckDesignRange:'请输入设计范围名称!'
                     }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    if (element.is(':radio') || element.is(':checkbox')) {
                        error.appendTo(element.closest('.liBox'));
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            $.validator.addMethod('ckDesignRange', function(value, element) {
                var that = this;
                var isTrue = true;
                $(that.element).find(' input[name="otherRange"]:checked').each(function () {
                    var val = $(this).closest('.liBox').find('input[name="designRange"]').val();
                    if($.trim(val).length===0){
                        isTrue = false;
                    }
                });
                return  isTrue;
            }, '请输入设计范围名称!');
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
