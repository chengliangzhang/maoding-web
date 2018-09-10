/**
 * 1=我申请的,2=待我审批,3=我已审批,4=抄送我的
 * Created by wrb on 2018/9/3.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_approval_data",
        defaults = {
            doType: 1//1=我申请的,2=待我审批,3=我已审批,4=抄送我的
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._currentCompanyUserId = window.currentCompanyUserId;

        this._title = '';
        if(this.settings.doType==1){
            this._title = '我申请的';
        }else if(this.settings.doType==2){
            this._title = '待我审批';
        }else if(this.settings.doType==3){
            this._title = '我已审批';
        }else if(this.settings.doType==4){
            this._title = '抄送我的';
        }
        this._filterData = {};
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_approval/m_approval_data', {title:that._title});
            $(that.element).html(html);
            that.renderDataList();
        }

        //加载数据
        , renderDataList: function () {
            var that = this;

            var option = {};
            option.param = {};
            that._filterData.type=that.settings.doType;
            option.param = filterParam(that._filterData);
            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-container',
                url: restApi.url_getAuditData,
                params: option.param
            }, function (response) {
                // data为ajax返回数据
                if (response.code == '0') {

                    var html = template('m_approval/m_approval_data_list', {dataList:response.data.data,currentCompanyUserId:that._currentCompanyUserId});
                    $(that.element).find('.data-list-container').html(html);
                    that.bindTrClick();
                    that.bindActionClick();
                    that.filterActionClick();
                } else {
                    S_dialog.error(response.info);
                }
            });

        }
        ,bindTrClick:function () {
            var that = this;
            $(that.element).find('tr').off('click').on('click',function () {
                var $this = $(this);
                var type = $this.attr('data-type');
                var dataId = $this.attr('data-id');

                if(type==1 || type==2 ){

                    var option = {};
                    option.doType = type;
                    option.id = dataId;
                    option.closeCallBack = function () {
                        that.renderDataList();
                    };
                    $('body').m_approval_cost_details(option,true);

                }else if(type==3 || type==4){

                    var option = {};
                    option.doType = type;
                    option.id = dataId;
                    option.closeCallBack = function () {
                        that.renderDataList();
                    };
                    $('body').m_approval_leave_details(option,true);

                }else if(type==5){
                    var option = {};
                    option.doType = type;
                    option.id = dataId;
                    option.closeCallBack = function () {
                        that.renderDataList();
                    };
                    $('body').m_approval_payment_details(option,true);
                }

            });
        }
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action="edit"]').off('click').on('click',function () {

                var $this = $(this),dataType = $this.closest('tr').attr('data-type'),dataId = $this.closest('tr').attr('data-id');

                switch (dataType){
                    case '1':
                    case '2':

                        var option = {};
                        option.url = restApi.url_getAuditDetailForExp;
                        option.postData = {
                            id:dataId
                        };
                        m_ajax.postJson(option, function (response) {
                            if (response.code == '0') {

                                var option = {};
                                option.doType = dataType;
                                option.dataInfo = response.data;
                                option.saveCallBack = function () {
                                    that.renderDataList();
                                };
                                $('body').m_approval_cost_add(option,true);

                            } else {
                                S_dialog.error(response.info);
                            }
                        });

                        return false;
                        break;

                    case '3':
                    case '4':

                        var option = {};
                        option.url = restApi.url_getLeaveDetailForWeb;
                        option.postData = {
                            id:dataId
                        };
                        m_ajax.postJson(option, function (response) {
                            if (response.code == '0') {

                                var option = {};
                                option.doType = dataType;
                                option.dataInfo = response.data;
                                option.saveCallBack = function () {
                                    that.renderDataList();
                                };
                                $('body').m_approval_leave_add(option,true);

                            } else {
                                S_dialog.error(response.info);
                            }
                        });

                        return false;
                        break;
                    case '5':


                        break;
                }


            });
        }
        //筛选事件
        ,filterActionClick:function () {
            var that = this;
            $(that.element).find('a.icon-filter').each(function () {

                var $this = $(this);
                var id = $this.attr('id');
                var filterArr = id.split('_');
                switch (id){
                    case 'filter_expType'://收款组织
                    case 'filter_approveStatus'://付款组织

                        var selectedArr = [],selectList = [];
                        if(id=='filter_expType'){

                            //1=报销申请，2=费用申请,3请假，4出差,5=项目费用申请
                            selectList = [
                                {id:1,name:'报销申请'},
                                {id:2,name:'费用申请'},
                                {id:3,name:'请假'},
                                {id:4,name:'出差'},
                                {id:5,name:'项目费用申请'}
                            ];
                        }
                        else if(id=='filter_approveStatus'){

                            //审批状态(0:待审核，1:同意，2，退回,3:撤回,4:删除,5.审批中）,6:财务已拨款,7:财务拒绝拨款',

                            if(that.settings.doType==2){
                                selectList = [
                                    {id:0,name:'待审核'},
                                    {id:5,name:'审批中'}
                                ];
                            }else if(that.settings.doType==3){
                                selectList = [
                                    {id:0,name:'待审核'},
                                    {id:1,name:'已完成'},
                                    {id:2,name:'已退回'},
                                    {id:5,name:'审批中'},
                                    {id:6,name:'财务已拨款'},
                                    {id:7,name:'财务拒绝拨款'}
                                ];
                            }else{
                                selectList = [
                                    {id:0,name:'待审核'},
                                    {id:1,name:'已完成'},
                                    {id:2,name:'已退回'},
                                    {id:3,name:'已撤回'},
                                    {id:5,name:'审批中'},
                                    {id:6,name:'财务已拨款'},
                                    {id:7,name:'财务拒绝拨款'}
                                ];
                            }

                        }

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
                            that.renderDataList();
                        };
                        $(that.element).find('#'+id).m_filter_select(option, true);

                        break;
                    case 'filter_userName': //

                        var newCode = filterArr[1]+'Like';
                        var option = {};
                        option.inputValue = that._filterData[newCode];
                        option.eleId = id;
                        option.oKCallBack = function (data) {

                            that._filterData[newCode] = data;
                            that.renderDataList();
                        };
                        $this.m_filter_input(option, true);

                        break;

                }

            });
        }
    });

    /*
     1.一般初始化（缓存单例）： $('#id').pluginName(initOptions);
     2.强制初始化（无视缓存）： $('#id').pluginName(initOptions,true);
     3.调用方法： $('#id').pluginName('methodName',args);
     */
    $.fn[pluginName] = function (options, args) {
        var instance;
        var funcResult;
        var jqObj = this.each(function () {

            //从缓存获取实例
            instance = $.data(this, "plugin_" + pluginName);

            if (options === undefined || options === null || typeof options === "object") {

                var opts = $.extend(true, {}, defaults, options);

                //options作为初始化参数，若args===true则强制重新初始化，否则根据缓存判断是否需要初始化
                if (args === true) {
                    instance = new Plugin(this, opts);
                } else {
                    if (instance === undefined || instance === null)
                        instance = new Plugin(this, opts);
                }

                //写入缓存
                $.data(this, "plugin_" + pluginName, instance);
            }
            else if (typeof options === "string" && typeof instance[options] === "function") {

                //options作为方法名，args则作为方法要调用的参数
                //如果方法没有返回值，funcReuslt为undefined
                funcResult = instance[options].call(instance, args);
            }
        });

        return funcResult === undefined ? jqObj : funcResult;
    };

})(jQuery, window, document);
