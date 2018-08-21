/**
 * 请假汇总
 * Created by wrb on 2017/12/26.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_leaveSummary",
        defaults = {
            $type:3//默认3=请假,4=出差
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
            var html = template('m_summary/m_leaveSummary', {
                type:that.settings.$type
            });
            $(that.element).html(html);
            that.getData();
        }

        //加载基本数据
        , getData: function () {
            var that = this;
            var option = {};
            option.classId = 'mySummaryListBox';
            option.param = {};
            option.param.type = that.settings.$type;

            option.param.startDate = $('input[name="startDate"]').val();
            option.param.endDate = $('input[name="endDate"]').val();
            option.param.applyName = $('input[name="applyName"]').val();
            option.param.leaveType = $('input[name="leaveType"]').val();
            option.param.auditPerson = $('input[name="auditPerson"]').val();

            paginationFun({
                eleId: '#mySummary-pagination-container',
                loadingId: '#mySummaryListData',
                url: restApi.url_getLeaveDetailList,
                params: option.param
            }, function (response) {
                // data为ajax返回数据
                if (response.code == '0') {

                    var $data = {};
                    $data.type = that.settings.$type;
                    $data.myDataList = response.data.data;
                    $data.rootPath = window.rootPath;
                    $data.pageIndex = $("#mySummary-pagination-container").pagination('getPageIndex');
                    var html = template('m_summary/m_leaveSummary_list', $data);
                    $('#mySummaryListData').html(html);
                    rolesControl();
                    that.bindClickOpenShowExp($data.myDataList);
                    that.bindActionClick();
                    that.filterHover();

                    return false;
                } else {
                    S_dialog.error(response.info);
                }
            });

        }
        //打开查看报销详情
        , bindClickOpenShowExp: function (data) {//openShowExp
            var that = this;
            $('#mySummaryListData').find('tr[data-action="openShowExp"]').each(function () {
                $(this).bind('click', function (event) {
                    var i = $(this).attr('i');
                    var options = {};
                    if(that.settings.$type==4){
                        options.$title = '出差详情';
                    }else{
                        options.$title = '请假详情';
                    }
                    options.$url = restApi.url_getLeaveDetail;
                    options.$detailData = data[i];
                    options.$type = that.settings.$type;
                    console.log(options);
                    $(this).m_leaveSummary_detail(options);
                    event.stopPropagation();
                    return false;
                });
            });
        }
        //筛选hover事件
        ,filterHover:function () {
            var that =  this;

            $(that.element).find('.mySummaryListBox  th').hover(function () {
                if( $(this).find(' .icon-filter i').hasClass('icon-shaixuan') && !$(this).find(' .icon-filter i').hasClass('fc-v1-blue')){
                    $(this).find(' .icon-filter').show();
                }
            },function () {
                if( $(this).find(' .icon-filter i').hasClass('icon-shaixuan') && !$(this).find(' .icon-filter i').hasClass('fc-v1-blue')) {
                    $(this).find(' .icon-filter').hide();
                }
            });
        }
        //绑定事件
        , bindActionClick:function () {
            var that = this;
            //筛选事件
            $(that.element).find('a.icon-filter').each(function () {

                var $this = $(this);
                var id = $this.attr('id');
                switch (id){
                    case 'filterAuditPerson'://审批人
                    case 'filterApplyName': //申请人
                        var txtVal = '',placeholder='';
                        if(id=='filterAuditPerson'){
                            txtVal = $('input[name="auditPerson"]').val();
                            placeholder = '审批人';
                        }else{
                            txtVal = $('input[name="applyName"]').val();
                            placeholder = '申请人';
                        }

                        if(txtVal!=''){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }
                        $this.on('click',function () {
                            S_dialog.dialog({
                                contentEle: 'dialogOBox',
                                ele:id,
                                lock: 2,
                                align: 'bottom right',
                                quickClose:true,
                                noTriangle:true,
                                width: '220',
                                minHeight:'100',
                                tPadding: '0px',
                                url: rootPath+'/assets/module/m_common/m_dialog.html'


                            },function(d){//加载html后触发

                                var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';


                                var iHtml = template('m_filterableField/m_filter_input',{
                                    txtVal:txtVal,
                                    placeholder:placeholder
                                });

                                $(dialogEle).html(iHtml);
                                $(dialogEle).find('button[data-action="sureFilter"]').on('click',function () {
                                    var val = $(dialogEle).find('input[name="txtVal"]').val();

                                    if(id=='filterAuditPerson'){
                                        $(that.element).find('input[name="auditPerson"]').val(val);
                                    }else{
                                        $(that.element).find('input[name="applyName"]').val(val);
                                    }

                                    that.getData();

                                    S_dialog.close($(dialogEle));
                                });

                            });
                        });

                        break;
                    case 'filterApplyDate': //申请时间

                        var startTime = '';
                        var endTime = '';

                        startTime = $(that.element).find('input[name="startDate"]').val();
                        endTime = $(that.element).find('input[name="endDate"]').val();

                        if(startTime!='' || endTime!=''){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }
                        $this.on('click',function () {
                            S_dialog.dialog({
                                contentEle: 'dialogOBox',
                                ele:id,
                                lock: 2,
                                align: 'bottom right',
                                quickClose:true,
                                noTriangle:true,
                                width: '220',
                                minHeight:'100',
                                tPadding: '0px',
                                url: rootPath+'/assets/module/m_common/m_dialog.html'


                            },function(d){//加载html后触发

                                var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';

                                var iHtml = template('m_filterableField/m_filter_time',{
                                    startTime:startTime,
                                    endTime:endTime
                                });

                                $(dialogEle).html(iHtml);
                                $(dialogEle).find('button[data-action="sureTimeFilter"]').off('click').on('click',function () {

                                    var startTime = $(dialogEle).find('input[name="startTime"]').val();
                                    var endTime = $(dialogEle).find('input[name="endTime"]').val();

                                    $(that.element).find('input[name="startDate"]').val(startTime);
                                    $(that.element).find('input[name="endDate"]').val(endTime);

                                    that.getData();

                                    S_dialog.close($(dialogEle));
                                });
                                $(dialogEle).find('button[data-action="clearTimeInput"]').off('click').on('click',function () {
                                    $(dialogEle).find('input').val('');
                                });
                                $(dialogEle).find('i.fa-calendar').off('click').on('click',function () {
                                    $(this).closest('.input-group').find('input').focus();
                                });

                            });
                        });

                        break;
                    case 'filterLeaveType': //申请类型

                        var currCheckValue = '',selectList = [];

                        currCheckValue = $(that.element).find('input[name="leaveType"]').val();
                        selectList = [
                            {fieldName:'全部',fieldValue:''},
                            {fieldName:'年假',fieldValue:'1'},
                            {fieldName:'事假',fieldValue:'2'},
                            {fieldName:'病假',fieldValue:'3'},
                            {fieldName:'调休假',fieldValue:'4'},
                            {fieldName:'婚假',fieldValue:'5'},
                            {fieldName:'产假',fieldValue:'6'},
                            {fieldName:'陪产假',fieldValue:'7'},
                            {fieldName:'丧假',fieldValue:'8'},
                            {fieldName:'其他',fieldValue:'9'}
                        ];

                        if(currCheckValue!=''){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }

                        var iHtml = template('m_filterableField/m_filter_select',{
                            currCheckValue:currCheckValue,
                            selectList:selectList
                        });
                        var iTextObj = iHtml.getTextWH();
                        var iWHObj = setDialogWH(iTextObj.width,iTextObj.height);

                        $this.on('click',function () {
                            S_dialog.dialog({
                                contentEle: 'dialogOBox',
                                ele:id,
                                lock: 2,
                                align: 'bottom right',
                                quickClose:true,
                                noTriangle:true,
                                width: iWHObj.width,
                                height:iWHObj.height,
                                tPadding: '0px',
                                url: rootPath+'/assets/module/m_common/m_dialog.html'

                            },function(d){//加载html后触发

                                var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';
                                $(dialogEle).html(iHtml);

                                $(dialogEle).find('.dropdown-menu a').on('click',function () {

                                    var val = $(this).attr('data-state-no');

                                    $(that.element).find('input[name="leaveType"]').val(val);

                                    that.getData();
                                    S_dialog.close($(dialogEle));
                                });
                            });
                        });

                        break;
                }

            });
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
