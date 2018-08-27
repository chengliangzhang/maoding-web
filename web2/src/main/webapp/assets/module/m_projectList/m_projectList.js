/**
 * Created by Wuwq on 2017/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_projectList",
        defaults = {
            postData: null,
            renderCallback:null,//渲染页面完成后事件
            isAllProject: false//true=项目总览,false=我的项目
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this._columnArr = [
            {code:'projectNo',name:'项目编号'},
            {code:'projectName',name:'项目名称'},
            {code:'createBy',name:'立项组织/立项人'},
            {code:'projectCreateDate',name:'立项时间'},
            {code:'signDate',name:'合同签订'},
            {code:'status',name:'项目状态'},
            {code:'buildName',name:'功能分类'},
            {code:'address',name:'地点'},
            {code:'partyA',name:'甲方'},
            {code:'partyB',name:'乙方'},
            {code:'designCompanyName',name:'合作组织'}/*,
            {code:'busPersonInCharge',name:'经营负责人'},
            {code:'busPersonInChargeAssistant',name:'经营助理'},
            {code:'designPersonInCharge',name:'设计负责人'},
            {code:'designPersonInChargeAssistant',name:'设计助理'}*/
        ];
        this._columnCodes = 'projectCreateDate,signDate,status,buildName,address,partyA,partyB';
        this._type = 0;//我的项目
        this._companyNameList = [];//筛选条件-立项组织
        this._partyANameList = [];//筛选条件-甲方
        this._partyBNameList = [];//筛选条件-乙方
        this._buildTypeNameList = [];//筛选条件-功能分类
        this._designCompanyNameList = [];//筛选条件-合作组织

        this._projectList = [];//项目列表


        /******************* 筛选条件 值预存 *********************/

        this._filterData = {
            keyword:null,
            companyNames:null,
            partyANames:null,
            partyBNames:null,
            startSignDate:null,
            endSignDate:null,
            province:null,
            city:null,
            county:null,
            startTime:null,
            endTime:null,
            status:null,
            designCompanyName:null,
            orderField:null,
            orderSign:null
        };

        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this,option = {};

            //渲染容器
            $(that.element).html(template('m_projectList/m_projectList', {}));
            that.renderProjectList(1);
            that.bindActionClick();

        }
        //请求筛选数据
        ,getProjectConditions:function (callBack) {
            var that = this;
            var option = {};
            option.url = restApi.url_getProjectConditions;
            option.postData = {

            };
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    that._companyNameList = response.data.companyNameList;
                    that._partyANameList = response.data.partyANameList;
                    that._partyBNameList = response.data.partyBNameList;
                    that._buildTypeNameList = response.data.buildTypeNameList;
                    that._designCompanyNameList = response.data.designCompanyNameList;

                    return callBack(response.data);
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //渲染项目list
        ,renderProjectList:function (fromType) {
            var that = this;

            var option = {};
            option.param = {};
            if(that.settings.isAllProject==false){
                that._filterData.type = 1;
            }else{
                that._filterData.type = null;
                that._type = 1;
            }

            //条件查询
            var keyword = $(that.element).find('input[name="keyword"]').val();
            that._filterData.keyword = isNullOrBlank(keyword)?null:keyword;

            console.log(filterParam(that._filterData));

            option.param = filterParam(that._filterData);

            var cookiesData = Cookies.get('projectList_cookiesData');

            if(cookiesData!=undefined && fromType==1){
                cookiesData = $.parseJSON(cookiesData);
                //that.paramSetVal(cookiesData.param);
                option.param = cookiesData.param;
            }

            paginationFun({
                eleId: '#data-pagination-container',
                loadingId: '.data-list-box',
                url: restApi.url_getMyProjectList,
                params: option.param
            }, function (response) {

                if (response.code == '0') {

                    //通过Cookie记住项目页码
                    option.param.pageIndex = $("#data-pagination-container").pagination('getPageIndex');

                    var $cookiesData = {
                        param:option.param,
                        dataAction:$('#content-right').find('ul.secondary-menu-ul li.active').attr('id')
                    };
                    Cookies.set('projectList_cookiesData', $cookiesData);

                    that._projectList = response.data.data;

                    that._columnCodes = response.data.columnCodes;
                    if(response.data.columnCodes!=null && response.data.columnCodes!=''){
                        that._columnCodes = response.data.columnCodes;
                    }
                    var columnArr = that._columnCodes.split(',');
                    var html = template('m_projectList/m_projectList_content',{
                        projectList:response.data.data,
                        columnCodes:that._columnCodes,
                        columnLen:columnArr.length
                    });
                    $(that.element).find('.data-list-container').html(html);
                    that.bindGotoProject();

                    that.getProjectConditions(function (data) {
                        that.filterActionClick();
                        that.sortActionClick();
                        if(that.settings.renderCallback!=null){
                            that.settings.renderCallback(data);
                        }
                    });


                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //跳转详情
        , bindGotoProject: function () {
            var that = this;
            $(that.element).find('a[data-action="gotoProject"]').off('click').on('click', function (e) {
                var $btn = $(this);
                var pId = $btn.attr('data-pId');//项目ID
                var pName = $btn.attr('data-pName');//项目名称
                location.hash = '/projectDetails/basicInfo?id='+pId+'&projectName='+encodeURI(pName);
                stopPropagation(e);
                return false;
            });
            $(that.element).find('button[data-action="setField"]').off('click').on('click', function (e) {
                var option = {};
                $('body').m_field_settings(option,true);
                return false;
            });
        }
        //事件绑定
        , bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');
                switch (dataAction){
                    case 'searchProject':

                        that.renderProjectList(2);
                        break;
                    case 'selectColumn':
                        S_dialog.dialog({
                            contentEle: 'dialogOBox',
                            ele:dataAction,
                            lock: 4,
                            align: 'bottom left',
                            quickClose:false,
                            noTriangle:true,
                            width: '180',
                            height:'234',
                            tPadding: '0px',
                            url: rootPath+'/assets/module/m_common/m_dialog.html'

                        },function(d){//加载html后触发

                            var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';

                            var iHtml = template('m_projectList/m_projectList_selectColumn',{
                                columnArr:that._columnArr,
                                columnCodes:that._columnCodes
                            });
                            $(dialogEle).html(iHtml);
                            $(dialogEle).parents('.ui-popup-modal').prev('div.ui-popup-backdrop').off('click').on('click',function () {
                                S_dialog.close($(dialogEle));
                            });
                            $(dialogEle).find('ul li a').off('click').on('click',function (e) {

                                var $this = $(this);
                                if($this.hasClass('active')){
                                    $this.removeClass('active');
                                }else{
                                    $this.addClass('active');
                                }
                                var codeStr = '';
                                $(dialogEle).find('ul li a.active').each(function () {
                                    codeStr+=$(this).attr('data-code')+',';
                                });
                                if(codeStr.length>0){
                                    codeStr = codeStr.substring(0,codeStr.length-1);
                                }
                                var option = {};
                                option.url = restApi.url_insertProCondition;
                                option.postData = {
                                    type:that._type,
                                    code:codeStr,
                                    companyId:window.currentCompanyId
                                };
                                that._columnCodes = codeStr;
                                m_ajax.postJson(option, function (response) {
                                    if (response.code == '0') {

                                        that.renderProjectListByColumn();

                                    } else {
                                        S_dialog.error(response.info);
                                    }
                                });
                            });
                        });
                        break;
                }
            });
        }
        //重新渲染
        , renderProjectListByColumn:function () {
            var that = this;
            var columnArr = that._columnCodes.split(',');
            var html = template('m_projectList/m_projectList_content',{
                projectList:that._projectList,
                columnCodes:that._columnCodes,
                columnLen:columnArr.length
            });
            $(that.element).find('.data-list-container').html(html);
            that.filterActionClick();
            that.sortActionClick();
            that.bindGotoProject();
        }
        //筛选事件
        , filterActionClick:function () {
            var that = this;
            $(that.element).find('a.icon-filter').each(function (e) {

                var $this = $(this);
                var id = $this.attr('id');
                switch (id){
                    case 'filterCreateBy': //立项组织/立项人
                    case 'filterStatus'://项目状态
                    case 'filterPartyA'://甲方
                    case 'filterPartyB'://乙方
                    case 'filterDesignCompanyName'://合作组织

                        var currCheckValue = '',key = '',selectList = [],selectedArr = [];
                        if(id=='filterCreateBy'){
                            key = 'companyNames';
                            currCheckValue = that._filterData.companyNames;
                            selectList = that._companyNameList;
                        }
                        else if(id=='filterStatus'){
                            key = 'status';
                            currCheckValue = that._filterData.status;
                            selectList = [
                                {name:'进行中',id:'0'},
                                {name:'已完成-未结清',id:'2'},
                                {name:'已完成-已结清',id:'4'},
                                {name:'已暂停-未结清',id:'1'},
                                {name:'已暂停-已结清',id:'5'},
                                {name:'已终止-未结清',id:'3'},
                                {name:'已终止-已结清',id:'6'}
                            ];

                        }
                        else if(id=='filterPartyA'){
                            key = 'partyANames';
                            currCheckValue = that._filterData.partyANames;
                            selectList = that._partyANameList;
                        }
                        else if(id=='filterPartyB'){
                            key = 'partyBNames';
                            currCheckValue = that._filterData.partyBNames;
                            selectList = that._partyBNameList;
                        }
                        else if(id=='filterDesignCompanyName'){
                            key = 'designCompanyName';
                            currCheckValue = that._filterData.designCompanyName;
                            selectList = that._designCompanyNameList;
                        }
                        if(!isNullOrBlank(currCheckValue))
                            selectedArr.push(currCheckValue);

                        var option = {};
                        option.selectArr = selectList;
                        option.selectedArr = selectedArr;
                        option.eleId = id;
                        option.selectedCallBack = function (data) {
                            if(data && data.length>0){
                                that._filterData[key] = data[0];
                            }else{
                                that._filterData[key] = null;
                            }
                            that.renderProjectList(2);
                        };
                        $(that.element).find('#'+id).m_filter_select(option, true);
                        break;
                    case 'filterSignDate': //合同签订
                    case 'filterCreateDate'://立项时间

                        var timeData = {};
                        if(id=='filterSignDate'){
                            timeData.startTime = that._filterData.startSignDate;
                            timeData.endTime = that._filterData.endSignDate;
                        }
                        else if(id='filterCreateDate'){
                            timeData.startTime = that._filterData.startTime;
                            timeData.endTime = that._filterData.endTime;
                        }

                        var option = {};
                        option.timeData = timeData;
                        option.eleId = id;
                        option.okCallBack = function (data) {
                            console.log(data);
                            if(id=='filterSignDate'){
                                that._filterData.startSignDate = data.startTime;
                                that._filterData.endSignDate = data.endTime;
                            }
                            else if(id='filterCreateDate'){
                                that._filterData.startTime = data.startTime;
                                that._filterData.endTime = data.endTime;
                            }

                            that.renderProjectList(2);
                        };
                        $(that.element).find('#'+id).m_filter_time(option, true);
                        break;
                    case 'filterAddress'://地点

                        var option = {};
                        option.addressData = {
                            province:that._filterData.province,
                            city:that._filterData.city,
                            county:that._filterData.county
                        };
                        option.eleId = id;
                        option.okCallBack = function (data) {
                            console.log(data);
                            that._filterData.province = data.province;
                            that._filterData.city = data.city;
                            that._filterData.county = data.county;
                            that.renderProjectList(2);
                        };
                        $(that.element).find('#'+id).m_filter_address(option, true);

                        break;
                    case 'filterBuildName'://功能分类

                        var option = {};
                        option.selectArr = that._buildTypeNameList;
                        if(!isNullOrBlank(that._filterData.buildTypeNames))
                            option.selectedArr = that._filterData.buildTypeNames.split(',');

                        option.eleId = 'filterBuildName';
                        option.dialogWidth = '420';
                        option.selectedCallBack = function (data) {
                            console.log(data);
                            if(data && data.length>0)
                                that._filterData.buildTypeNames = data.join(',');

                            that.renderProjectList(2);
                        };
                        $(that.element).find('#filterBuildName').m_filter_checkbox_select(option, true);

                        break;

                }

            });
        }
        //排序
        , sortActionClick:function () {
            var that = this;
            $(that.element).find('th[data-action="sort"]').each(function () {

                var $this = $(this);
                var sortType = $this.attr('data-sort-type');

                switch(sortType){
                    case 'createDate'://立项时间
                        var sortCreateDate = that._filterData.orderField;
                        var sortClass = '';
                        if(sortCreateDate=='0'){
                            sortClass = 'sorting_asc';
                        }else if(sortCreateDate=='1'){
                            sortClass = 'sorting_desc';
                        }else{
                            sortClass = 'sorting';
                        }
                        $this.removeClass().addClass(sortClass);
                        $this.off('click').on('click',function (e) {
                            if($this.hasClass('sorting')||$this.hasClass('sorting_asc')){
                                that._filterData.orderField = '1';
                            }
                            else if($this.hasClass('sorting_desc')){
                                that._filterData.orderField = '0';
                            }
                            that._filterData.orderSign = '';
                            that.renderProjectList(2);
                            e.stopPropagation();
                            return false;
                        });
                        break;
                    case 'signDate'://签订时间
                        var sortSignDate = that._filterData.orderSign;
                        var sortSignDateClass = '';
                        if(sortSignDate=='0'){
                            sortSignDateClass = 'sorting_asc';
                        }else if(sortSignDate=='1'){
                            sortSignDateClass = 'sorting_desc';
                        }else{
                            sortSignDateClass = 'sorting';
                        }
                        $this.removeClass().addClass(sortSignDateClass);
                        $this.off('click').on('click',function (e) {
                            if($this.hasClass('sorting')||$this.hasClass('sorting_asc')){
                                that._filterData.orderSign = '1';
                            }
                            else if($this.hasClass('sorting_desc')){
                                that._filterData.orderSign = '0'
                            }
                            that._filterData.orderField = '';
                            that.renderProjectList(2);
                            e.stopPropagation();
                            return false;
                        });
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