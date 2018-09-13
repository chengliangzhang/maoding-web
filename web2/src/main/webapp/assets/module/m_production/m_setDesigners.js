/**
 * 进度变更
 * Created by wrb on 2016/12/26.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_setDesigners",
        defaults = {
            $title:null,
            $isDialog:true,
            $projectId:null,
            $taskId:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._projectProcess = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._initHtmlTemplate();
        },
        //初始化数据
        _renderDialog:function (callBack) {
            var that = this;
            if(that.settings.$isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    title: that.settings.$title||'设置参与人员',
                    area : '850px',
                    content:html,
                    cancel:function () {
                        that._refresh();
                    },
                    ok:function () {

                        var res = that._saveTaskParticipant();
                        if(!res){
                            return false;
                        }
                    }

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.$isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
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
        //生成html
        ,_initHtmlTemplate:function (data) {
            var that = this;

            //根据任务ID获取参与人员列表
            var options={};
            options.url=restApi.url_getProcessesByTask+'/'+that.settings.$taskId;
            m_ajax.get(options,function (response) {
                if(response.code=='0'){
                    that._projectProcess = response.data.projectProcess;
                    var $data = {};
                    $data.projectProcessNodes = response.data.projectProcess.projectProcessNodes;
                    var html = template('m_production/m_setDesigners',$data);
                    that._renderDialog(html,function () {
                        that._bindActionClick();
                        $(that.element).find('#choseUserBox').m_choseUserBox();
                    });
                }else {
                    S_layer.error(response.info);
                }
            })
        }
        //保存参与人员
        ,_saveTaskParticipant:function () {
            var that = this;
            var option  = {};
            option.classId = that.element;
            option.url = restApi.url_saveOrUpdateProcess;
            var param ={};

            var isError = true;
            var nodes = [];
            $(that.element).find('div.designerRow').each(function (_i) {

                //var id = $(this).attr('data-node-id')==''?null:$(this).attr('data-node-id');
                var nodeName = $(this).attr('data-node-name');
                var nodeSeq = $(this).attr('data-node-seq');
                $(this).find('span.designerSpan').each(function (i) {
                    if(_i==0 && i==0){
                        isError = false;//进来说明存在设计人
                    }
                    var dataUserId = $(this).attr('data-companyUserId');
                    nodes.push({
                        id:$(this).attr('data-id')==''?null:$(this).attr('data-id'),
                        companyUserId:dataUserId,
                        userName:$(this).find('span').text(),
                        nodeName:nodeName,
                        seq:nodeSeq
                    });
                });
            });
            /*if(isError){
                S_layer.tips('设计人不能为空！');
                return false;
            }*/

            if(that._projectProcess!=null && that._projectProcess.id!=null){
                param.id=that._projectProcess.id;
            }else{
                param.id=null;
            }

            param.processName = "";
            param.nodes = nodes;
            param.projectId = that.settings.$projectId;
            param.taskManageId = that.settings.$taskId;
            option.postData = param;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！')
                    S_layer.close($(that.element));
                    that._refresh();
                }else {
                    S_layer.error(response.info);
                }

            })
        }
        //刷新当前界面
        ,_refresh:function () {
            var option = {}, that = this;
            var scrollTop = $('body').scrollTop();
            option.$projectId = that.settings.$projectId;
            option.$scrollCallBack = function () {
                $('body').scrollTop(scrollTop);
            };
            $('#project_detail').m_production(option);
        }

        //按钮事件绑定
        ,_bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').on('click',function () {

                var dataAction = $(this).attr('data-action');

                if(dataAction=='addDesigners') {//添加参与人员

                    var selectedUserList = [], _this = this;
                    var nodeName = $(_this).closest('.designerRow').attr('data-node-name');
                    $(this).parent().find('span.designerSpan').each(function (i) {
                        selectedUserList.push({
                            id: $(this).attr('data-companyUserId'),
                            userName: $(this).find('span').text()
                        });
                    });

                    var options = {};
                    options.title = '选择' + nodeName + '人';
                    options.selectedUserList = selectedUserList;
                    options.saveCallback = function (data) {
                        var iHtml = '';
                        if (data != null && data.selectedUserList != null && data.selectedUserList.length > 0) {
                            for (var i = 0; i < data.selectedUserList.length; i++) {
                                iHtml += '<span class="label label-default inline m-r-xs designerSpan" data-companyUserId="' + data.selectedUserList[i].id + '">';
                                iHtml += '<span class="nameSpan">' + data.selectedUserList[i].userName + '</span>';
                                iHtml += '<a href="javascript:void(0)" data-action="delDesigner"><i class="glyphicon glyphicon-remove text-danger"></i></a>';
                                iHtml += '</span>';
                            }
                        }
                        $(_this).parent().find('.designerSpan').remove();
                        $(_this).before(iHtml);
                        $(_this).parent().find('.designerSpan a[data-action="delDesigner"]').unbind();
                        $(_this).parent().find('.designerSpan a[data-action="delDesigner"]').on('click',function () {
                            $(this).parent('.designerSpan').remove();
                            return false;
                        });
                    }
                    $('body').m_orgByTree(options);

                }else if(dataAction=='delDesigner'){//删除参与人员

                    $(this).parent('.designerSpan').remove();
                    return false;
                }
                return false;
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


