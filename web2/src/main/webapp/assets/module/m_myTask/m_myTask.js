/**
 * 我的任务
 * Created by wrb on 2017/11/9.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_myTask",
        defaults = {
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this._taskList = null;//任务列表
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var html = template('m_myTask/m_myTask', {});
            $(that.element).html(html);
            $(that.element).find('select[name="searchByState"]').select2({
                allowClear: false,
                containerCssClass:'select-sm',
                language: "zh-CN",
                minimumResultsForSearch: -1
            });
            $(that.element).find('select[name="searchByState"]').on("change", function (e) {
                that.renderTaskList();
            });
            that.renderTaskList();
        }

        ,renderTaskList:function () {

            var that = this;
            var url = restApi.url_getMyTaskList4;
            var params = {};
            var status = $(that.element).find('select[name="searchByState"]').val();
            if(status!=undefined && status!=''){
                params.status = status;
            }
            paginationFun({
                eleId: '#pagination-container',
                loadingId: '.my-task-list',
                url: url,
                params: params
            }, function (response) {

                var html = template('m_myTask/m_myTask_list', {
                    myDataList:response.data.data,
                    pageIndex:$("#pagination-container").pagination('getPageIndex')
                });
                $(that.element).find('.my-task-list').html(html);
                that.goToTaskDetail();

            });

        }

        //跳转到任务详细界面
        ,goToTaskDetail:function () {
            var that = this;
            $(that.element).find('tr').on('click',function () {
                var $this = $(this);
                var dataType = $this.attr('data-type');
                var projectId = $this.attr('data-project-id');
                var projectName = $this.find('span[data-type="projectName"]').text();
                var  targetId = $this.attr('data-target-id');
                var  id = $this.attr('id');
                switch (dataType){
                    case '1'://任务签发

                        location.hash = '/myTask/taskIssue?projectId='+projectId+'&projectName='+encodeURI(projectName);
                        break;
                    case '12'://生产安排
                    case '22':

                        location.hash = '/myTask/production?projectId='+projectId+'&projectName='+encodeURI(projectName);
                        break;
                    case '3'://设、校、审

                        location.hash = '/myTask/designReview?projectId='+projectId+'&projectName='+encodeURI(projectName);
                        break;
                    case '13'://任务负责人审核

                        location.hash = '/myTask/approved?projectId='+projectId+'&projectName='+encodeURI(projectName);
                        break;
                    case '4'://收支管理-付款
                    //case '5':
                    case '6':
                    //case '7':
                    case '16':
                    case '18':
                    case '20':
                    case '30':
                    case '32':
                        location.hash = '/myTask/cost?dataType=paymentPlan&projectId='+projectId+'&myTaskId='+id+'&projectName='+encodeURI(projectName);
                        break;
                    case '19'://收支管理-收款
                    case '8':
                    case '9':
                    case '10':
                    case '17':
                    case '21':
                    case '29'://发票
                    case '31':
                    case '33':
                        location.hash = '/myTask/cost?dataType=collectionPlan&projectId='+projectId+'&myTaskId='+id+'&projectName='+encodeURI(projectName);
                        break;
                    case '11'://报销审批
                        var options = {};
                        options.expDetail = {
                            id:targetId
                        };
                        $('body').m_myTask_expDetail(options);
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