/**
 * 设计阶段
 * Created by wrb on 2016/12/20.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_editDesignContent",
        defaults = {
            $title: null,
            $isDialog: true,
            $projectId: '',
            $projectDesignContent: null,
            $okCallBack: null,
            $cancelCallBack: null,
            $designContentList: null//此字段跟$projectDesignContent一样，这里不代表所有
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._designContentListClone = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            if (that.settings.$designContentList != null) {
                that._designContentListClone = jQuery.extend(true, {}, that.settings.$designContentList);
            }
            var $data = {};
            $data.designContentList = that.settings.$designContentList;
            $data.designContentList[0].projectProcessTimeEntityList = that.getChangeTimeDiff($data.designContentList[0].projectProcessTimeEntityList);
            var html = template('m_project/m_editDesignContent', $data);
            that.renderDialog(html,function () {
                that.bindActionClick();
                that.bindDesignContentCk();
            });
        },
        //初始化数据
        renderDialog: function (html,callBack) {
            var that = this;
            if (that.settings.$isDialog) {//以弹窗编辑
                S_layer.dialog({
                    title: that.settings.$title || '进度变更',
                    area : '750px',
                    content:html,
                    cancel: function () {
                    },
                    cancelText:'关闭'

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.$isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
                    that.element = dialogEle;
                    if(callBack)
                        callBack();
                });
            } else {//不以弹窗编辑

            }
        }
        //计算变更容器的时间差（天）
        , getChangeTimeDiff: function (list) {
            var that = this;
            $.each(list, function (i, item) {
                list[i].timeDiffStr = that.countTimeDiff(item.startTime, item.endTime);
            });
            return list;
        }
        //计算时间差的方法
        , countTimeDiff:function (startTime, endTime) {
            var diffStr = moment(endTime).diff(moment(startTime), 'days')+1;
            if(isNaN(diffStr) || diffStr==undefined){
                diffStr = '';
            }
            return diffStr;
        }
        //获取设计阶段基础数据
        , getContentData: function (callBack) {
            var that = this;
            var option = {};
            option.classId = that.element;
            option.url = restApi.url_getDesignContentList;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    that.settings.$designContentList = response.data;
                    return callBack(response.data);
                } else {
                    S_layer.error(response.info);
                }

            })
        }

        //变更按钮事件绑定
        , bindTimeChangeRecord: function (obj) {
            var that = this;
            var contentIndex = obj.closest('.designContentDiv').attr('data-i');
            var contentObj = that.settings.$designContentList[contentIndex];
            var options = {};
            options.$title = '添加变更';
            options.$okText = '保存';
            options.$minHeight = '180';
            options.$validate = 1;
            options.$timeInfo={
                startTime:obj.closest('.time-row').find('span[data-type="startTime"]').text(),
                endTime:obj.closest('.time-row').find('span[data-type="endTime"]').text()
            }
            options.$okCallBack = function (data) {
                data.timeDiffStr = that.countTimeDiff(data.startTime, data.endTime);
                if (data != null) {
                    if (that.settings.$okCallBack != null) {
                        that.settings.$okCallBack(data);
                    }
                    if (contentObj.projectProcessTimeEntityList != null) {
                        contentObj.projectProcessTimeEntityList.push({
                            startTime: data.startTime,
                            endTime: data.endTime,
                            timeDiffStr: data.timeDiffStr,
                            memo: data.memo
                        });
                    }
                    var $option = {};
                    $option.$data = data;
                    $option.$data.$index = contentObj.projectProcessTimeEntityList.length - 2;
                    $(that.element).find('div.designContentDiv[data-i="' + contentIndex + '"]').m_addTimeChangeRecord($option);
                    $(that.element).find('.detailListDiv:last').siblings().find('a[data-action="addTimeChangeRecord"]').addClass('hide');
                    $(that.element).find('.detailListDiv:last').siblings().find('a[data-action="delTimeChangeRecord"]').addClass('hide');
                    $(that.element).find('.detailListDiv:last').find('a[data-action="addTimeChangeRecord"]').on('click', function () {
                        that.bindTimeChangeRecord($(this));
                    });
                }

            };
            $('body').m_inputProcessTime(options);
        }
        //删除变更列表
        , delTimeChangeRecord: function (obj) {
            var that = this;
            var contentIndex = $(obj).closest('.designContentDiv').attr('data-i');
            var contentObj = that.settings.$designContentList[contentIndex];
            if (contentObj.projectProcessTimeEntityList && contentObj.projectProcessTimeEntityList.length > 1) {//删除变更列表
                if (contentObj.projectProcessTimeEntityList.length == 2) {
                    $(obj).closest('.detailListDiv').prev().find('a').removeClass('hide');
                } else {
                    $(obj).closest('.detailListDiv').prev().find('a').removeClass('hide');
                }
                $(obj).closest('.detailListDiv').remove();
                contentObj.projectProcessTimeEntityList.splice(contentObj.projectProcessTimeEntityList.length - 1, 1);
                that.settings.$designContentList[contentIndex] = contentObj;
            } else {//第一条合同进度时间
                $(obj).closest('.liBox').find('input[name="startTime"]').val('');
                $(obj).closest('.liBox').find('input[name="endTime"]').val('');
                $(obj).closest('.liBox').find('input').removeAttr('disabled');
                $(obj).closest('.liBox').find('.btnBox a').addClass('hide');
                contentObj.projectProcessTimeEntityList.splice(contentObj.projectProcessTimeEntityList.length - 1, 1);
                that.settings.$designContentList[contentIndex] = contentObj;

                S_layer.close($(that.element));
                var options = {};
                options.$isHaveMemo = false;
                options.$timeInfo = {};
                options.$title = '设计阶段';
                options.$okCallBack = function (data) {
                    if (that.settings.$okCallBack != null) {
                        return that.settings.$okCallBack(data, 1);
                    }
                };
                $('body').m_inputProcessTime(options);
            }
        }
        , bindDesignContentCk: function () {
            $(this.element).find('input[type="checkbox"][name="designContent"]').on('click', function () {
                if ($(this).is(':checked')) {
                    $(this).closest('.designContentDiv').find('.start-time').removeClass('hide');
                    $(this).closest('.designContentDiv').find('.end-time').removeClass('hide');
                } else {
                    $(this).closest('.designContentDiv').find('.start-time').addClass('hide');
                    $(this).closest('.designContentDiv').find('.end-time').addClass('hide');
                }
            });
        }
        //按钮事件绑定
        , bindActionClick: function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click', function () {
                var dataAction = $(this).attr('data-action');
                if (dataAction == 'addOtherContent') {
                    that.bindAddContent($(this));
                    return false;
                } else if (dataAction == 'addTimeChangeRecord') {//点击变更
                    that.bindTimeChangeRecord($(this));
                    return false;
                }
                /*else if(dataAction=='delTimeChangeRecord'){
                 var $that = this;
                 S_layer.confirm('您确定要删除吗？',function(){
                 that.delTimeChangeRecord($that);
                 },function(){})
                 return false;
                 }*/
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


