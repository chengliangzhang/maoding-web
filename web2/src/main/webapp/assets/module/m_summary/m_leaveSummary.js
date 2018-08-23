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

        /******************** 筛选字段 ********************/
        this._filterData = {
            startDate:null,
            endDate:null,
            applyName:null,
            leaveType:null,
            auditPerson:null
        };

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

            that._filterData.type = that.settings.$type;
            option.param = filterParam(that._filterData);

            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-container',
                url: restApi.url_getLeaveDetailList,
                params: option.param
            }, function (response) {
                // data为ajax返回数据
                if (response.code == '0') {

                    var $data = {};
                    $data.type = that.settings.$type;
                    $data.myDataList = response.data.data;
                    $data.rootPath = window.rootPath;
                    $data.pageIndex = $("#data-pagination-container").pagination('getPageIndex');
                    var html = template('m_summary/m_leaveSummary_list', $data);
                    $(that.element).find('.data-list-container').html(html);

                    rolesControl();
                    that.bindClickOpenShowExp($data.myDataList);
                    that.bindActionClick();

                    return false;
                } else {
                    S_dialog.error(response.info);
                }
            });

        }
        //打开查看报销详情
        , bindClickOpenShowExp: function (data) {//openShowExp
            var that = this;
            $(that.element).find('.data-list-container tr[data-action="openShowExp"]').each(function () {
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

        //绑定事件
        , bindActionClick:function () {
            var that = this;
            //筛选事件
            $(that.element).find('a.icon-filter').each(function () {

                var $this = $(this);
                var id = $this.attr('id');
                var filterArr = id.split('_');
                switch (id){
                    case 'filter_auditPerson'://审批人
                    case 'filter_applyName': //申请人

                        var option = {};
                        option.inputValue = that._filterData[filterArr[1]];
                        option.eleId = id;
                        option.oKCallBack = function (data) {

                            that._filterData[filterArr[1]] = data;
                            that.getData();
                        };
                        $(that.element).find('#'+id).m_filter_input(option, true);
                        break;
                    case 'filter_startDate_endDate': //申请时间
                        var timeData = {};
                        timeData.startTime = that._filterData[filterArr[1]];
                        timeData.endTime = that._filterData[filterArr[2]];

                        var option = {};
                        option.timeData = timeData;
                        option.eleId = id;
                        option.okCallBack = function (data) {

                            that._filterData[filterArr[1]] = data.startTime;
                            that._filterData[filterArr[2]] = data.endTime;
                            that.getData();
                        };
                        $(that.element).find('#'+id).m_filter_time(option, true);
                        break;
                    case 'filter_leaveType': //申请类型

                        var selectList = [],selectedArr = [];

                        selectList = [
                            {name:'年假',id:'1'},
                            {name:'事假',id:'2'},
                            {name:'病假',id:'3'},
                            {name:'调休假',id:'4'},
                            {name:'婚假',id:'5'},
                            {name:'产假',id:'6'},
                            {name:'陪产假',id:'7'},
                            {name:'丧假',id:'8'},
                            {name:'其他',id:'9'}
                        ];
                        if(!isNullOrBlank(that._filterData[filterArr[1]]))
                            selectedArr.push(that._filterData[filterArr[1]]);

                        var option = {};
                        option.selectArr = selectList;
                        option.selectedArr = selectedArr;
                        option.eleId = id;
                        option.selectedCallBack = function (data) {
                            if(data && data.length>0){
                                that._filterData[filterArr[1]] = data[0];
                            }else{
                                that._filterData[filterArr[1]] = null;
                            }
                            that.getData();
                        };
                        $(that.element).find('#'+id).m_filter_select(option, true);

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
