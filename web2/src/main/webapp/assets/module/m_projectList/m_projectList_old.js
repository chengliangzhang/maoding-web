/**
 * Created by Wuwq on 2017/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_projectList_old",
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
            {code:'partyB',name:'乙方'}/*,
            {code:'busPersonInCharge',name:'经营负责人'},
            {code:'busPersonInChargeAssistant',name:'经营助理'},
            {code:'designPersonInCharge',name:'设计负责人'},
            {code:'designPersonInChargeAssistant',name:'设计助理'}*/
        ];
        this._columnCodes = 'projectCreateDate,signDate,status,buildName,address,partyA,partyB';
        this._type = 0;//我的项目
        this._companyNames = [];//筛选条件-立项组织
        this._partyANames = [];//筛选条件-甲方
        this._partyBNames = [];//筛选条件-乙方
        this._buildTypeNames = [];//筛选条件-功能分类

        this._busPersonInChargeMap = [];//筛选条件-经营负责人
        this._busPersonInChargeAssistantMap = [];//筛选条件-经营负责人助理
        this._designPersonInChargeMap = [];//筛选条件-设计负责人
        this._designPersonInChargeAssistantMap = [];//筛选条件-设计负责人助理

        this._projectList = [];//项目列表


        /******************* 筛选条件 值预存 *********************/

        this._filterData = {
            filterStatus:'',
            filterCreateBy:'',
            filterPartyA:'',
            filterPartyB:'',
            filterSignDate:[],
            filterAddress:[],
            filterBuildName:'',
            filterCreateDate:[],
            filterBusPersonInCharge:'',
            filterBusAss:'',
            filterDesignPersonInCharge:'',
            filterDesignAss:'',
            filterSortCreateDate:'',
            filterSortSignDate:''
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

                    that._companyNames = response.data.companyNames;
                    that._partyANames = response.data.partyANames;
                    that._partyBNames = response.data.partyBNames;
                    that._buildTypeNames = response.data.buildTypeNames;

                    that._busPersonInChargeMap = response.data.busPersonInChargeMap;//筛选条件-经营负责人
                    that._busPersonInChargeAssistantMap = response.data.busPersonInChargeAssistantMap;//筛选条件-经营负责人助理
                    that._designPersonInChargeMap = response.data.designPersonInChargeMap;//筛选条件-设计负责人
                    that._designPersonInChargeAssistantMap = response.data.designPersonInChargeAssistantMap;//筛选条件-设计负责人助理

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
                option.param.type = 1;
            }else{
                option.param.type = null;
                that._type = 1;
            }

            //条件查询
            var keyword = $(that.element).find('input[name="keyword"]').val();
            keyword = $.trim(keyword);
            if(keyword!=''){
                option.param.keyword = keyword;//关键字查询
            }

            option.param.companyNames = that._filterData.filterCreateBy;
            option.param.partyANames = that._filterData.filterPartyA;
            option.param.partyBNames = that._filterData.filterPartyB;

            if(!_.isBlank(that._filterData.filterSignDate[0])){
                option.param.startSignDate = that._filterData.filterSignDate[0];
            }
            if(!_.isBlank(that._filterData.filterSignDate[1])){
                option.param.endSignDate = that._filterData.filterSignDate[1];
            }
            option.param.province = that._filterData.filterAddress[0];
            option.param.city = that._filterData.filterAddress[1];
            option.param.county = that._filterData.filterAddress[2];
            option.param.buildTypeNames = that._filterData.filterBuildName;

            if(!_.isBlank(that._filterData.filterCreateDate[0])){
                option.param.startTime = that._filterData.filterCreateDate[0];
            }

            if(!_.isBlank(that._filterData.filterCreateDate[1])){
                option.param.endTime = that._filterData.filterCreateDate[1];
            }
            option.param.status = that._filterData.filterStatus;

            option.param.orderField = that._filterData.filterSortCreateDate;
            option.param.orderSign = that._filterData.filterSortSignDate;

            option.param.busPersonInCharge = that._filterData.filterBusPersonInCharge;
            option.param.busPersonInChargeAssistant = that._filterData.filterBusAss;
            option.param.designPersonInCharge = that._filterData.filterDesignPersonInCharge;
            option.param.designPersonInChargeAssistant = that._filterData.filterDesignAss;

            var cookiesData = Cookies.get('projectList_cookiesData');

            if(cookiesData!=undefined && fromType==1){
                cookiesData = $.parseJSON(cookiesData);
                that.paramSetVal(cookiesData.param);
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
                        that.filterHover();
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
        , paramSetVal:function (param) {
            var that = this;
            //条件查询
            $(that.element).find('input[name="keyword"]').val(param.keyword);
            that._filterData.filterStatus = param.status;
            that._filterData.filterCreateBy = param.companyNames;
            that._filterData.filterPartyA = param.partyANames;
            that._filterData.filterPartyB = param.partyBNames;
            that._filterData.filterSignDate = [param.startSignDate, param.endSignDate];
            that._filterData.filterAddress = [param.province,param.city,param.county];
            that._filterData.filterBuildName = param.buildTypeNames;
            that._filterData.filterCreateDate = [param.startTime,param.endTime];
            that._filterData.filterSortCreateDate = param.orderField;
            that._filterData.filterSortSignDate = param.orderSign;
            that._filterData.filterBusPersonInCharge = param.busPersonInCharge;
            that._filterData.filterBusAss = param.busPersonInChargeAssistant;
            that._filterData.filterDesignPersonInCharge = param.designPersonInCharge;
            that._filterData.filterDesignAss = param.designPersonInChargeAssistant;
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
            that.filterHover();
            that.filterActionClick();
            that.sortActionClick();
            that.bindGotoProject();
        }
        //筛选hover事件
        , filterHover:function () {
            var that =  this;
            $(that.element).find('.data-list-box  th').hover(function () {
                if( $(this).find(' .icon-filter i').hasClass('icon-shaixuan') && !$(this).find(' .icon-filter i').hasClass('fc-v1-blue')){
                    $(this).find(' .icon-filter').show();
                }
            },function () {
                if( $(this).find(' .icon-filter i').hasClass('icon-shaixuan') && !$(this).find(' .icon-filter i').hasClass('fc-v1-blue')) {
                    $(this).find(' .icon-filter').hide();
                }
            });
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
                    case 'filterBusPersonInCharge': //经营负责人
                    case 'filterBusAss': //经营负责人助理
                    case 'filterDesignPersonInCharge': //设计负责人
                    case 'filterDesignAss': //设计负责人助理

                        var currCheckValue = '',selectList = [];
                        if(id=='filterCreateBy'){
                            currCheckValue = that._filterData.filterCreateBy;
                            selectList.push({fieldName:'全部',fieldValue:''});
                            if(that._companyNames!=null && Object.getOwnPropertyNames(that._companyNames).length>0){
                                $.each(that._companyNames, function (key, value) {
                                    selectList.push({fieldValue: key, fieldName: value});
                                });
                            }
                        }
                        else if(id=='filterStatus'){
                            currCheckValue = that._filterData.filterStatus;
                            selectList = [
                                {fieldName:'全部',fieldValue:''},
                                {fieldName:'进行中',fieldValue:'0'},
                                {fieldName:'已完成-未结清',fieldValue:'2'},
                                {fieldName:'已完成-已结清',fieldValue:'4'},
                                {fieldName:'已暂停-未结清',fieldValue:'1'},
                                {fieldName:'已暂停-已结清',fieldValue:'5'},
                                {fieldName:'已终止-未结清',fieldValue:'3'},
                                {fieldName:'已终止-已结清',fieldValue:'6'}
                            ]
                        }
                        else if(id=='filterPartyA'){
                            currCheckValue = that._filterData.filterPartyA;
                            selectList.push({fieldName:'全部',fieldValue:''});
                            if(that._partyANames!=null && Object.getOwnPropertyNames(that._partyANames).length>0){
                                $.each(that._partyANames, function (key, value) {
                                    selectList.push({fieldValue: key, fieldName: value});
                                });
                            }
                        }
                        else if(id=='filterPartyB'){
                            currCheckValue = that._filterData.filterPartyB;
                            selectList.push({fieldName:'全部',fieldValue:''});
                            if(that._partyBNames!=null && Object.getOwnPropertyNames(that._partyBNames).length>0){
                                $.each(that._partyBNames, function (key, value) {
                                    selectList.push({fieldValue: key, fieldName: value});
                                });
                            }
                        }
                        else if(id=='filterBusPersonInCharge'){
                            currCheckValue = that._filterData.filterBusPersonInCharge;
                            selectList.push({fieldName:'全部',fieldValue:''});
                            if(that._busPersonInChargeMap!=null && Object.getOwnPropertyNames(that._busPersonInChargeMap).length>0){
                                $.each(that._busPersonInChargeMap, function (key, value) {
                                    selectList.push({fieldValue: key, fieldName: value});
                                });
                            }
                        }
                        else if(id=='filterBusAss'){
                            currCheckValue = that._filterData.filterBusAss;
                            selectList.push({fieldName:'全部',fieldValue:''});
                            if(that._busPersonInChargeAssistantMap!=null && Object.getOwnPropertyNames(that._busPersonInChargeAssistantMap).length>0){
                                $.each(that._busPersonInChargeAssistantMap, function (key, value) {
                                    selectList.push({fieldValue: key, fieldName: value});
                                });
                            }
                        }
                        else if(id=='filterDesignPersonInCharge'){
                            currCheckValue = that._filterData.filterDesignPersonInCharge;
                            selectList.push({fieldName:'全部',fieldValue:''});
                            if(that._designPersonInChargeMap!=null && Object.getOwnPropertyNames(that._designPersonInChargeMap).length>0){
                                $.each(that._designPersonInChargeMap, function (key, value) {
                                    selectList.push({fieldValue: key, fieldName: value});
                                });
                            }
                        }
                        else if(id=='filterDesignAss'){
                            currCheckValue = that._filterData.filterDesignAss;
                            selectList.push({fieldName:'全部',fieldValue:''});
                            if(that._designPersonInChargeAssistantMap!=null && Object.getOwnPropertyNames(that._designPersonInChargeAssistantMap).length>0){
                                $.each(that._designPersonInChargeAssistantMap, function (key, value) {
                                    selectList.push({fieldValue: key, fieldName: value});
                                });
                            }
                        }
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

                        $this.on('click',function (e) {
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
                                    that._filterData[id] = val;
                                    that.renderProjectList(2);
                                    S_dialog.close($(dialogEle));
                                });
                            });
                            e.stopPropagation();
                            return false;
                        });

                        break;
                    case 'filterSignDate': //合同签订
                    case 'filterCreateDate'://立项时间

                        var startTime = '';
                        var endTime = '';

                        startTime = that._filterData[id][0];
                        endTime = that._filterData[id][1];


                        if(!_.isBlank(startTime) || !_.isBlank(endTime)){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }
                        $this.on('click',function (e) {
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

                                    that._filterData[id][0] = startTime;
                                    that._filterData[id][1] = endTime;

                                    that.renderProjectList(2);

                                    S_dialog.close($(dialogEle));
                                });
                                $(dialogEle).find('button[data-action="clearTimeInput"]').off('click').on('click',function () {
                                    $(dialogEle).find('input').val('');
                                });
                                $(dialogEle).find('i.fa-calendar').off('click').on('click',function () {
                                    $(this).closest('.input-group').find('input').focus();
                                });

                            });
                            e.stopPropagation();
                            return false;
                        });

                        break;
                    case 'filterAddress'://地点

                        var currProv = '',currCity = '',currCounty = '';

                        currProv = that._filterData.filterAddress[0];
                        currCity = that._filterData.filterAddress[1];
                        currCounty = that._filterData.filterAddress[2];
                        if(!_.isBlank(currProv)){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }
                        $this.on('click',function (e) {

                            S_dialog.dialog({
                                contentEle: 'dialogOBox',
                                ele:id,
                                lock: 2,
                                align: 'bottom right',
                                quickClose:true,
                                noTriangle:true,
                                width: '350',
                                minHeight:'110',
                                tPadding: '0px',
                                url: rootPath+'/assets/module/m_common/m_dialog.html'

                            },function(d){//加载html后触发

                                var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';
                                var iHtml = template('m_filterableField/m_filter_address',{});
                                $(dialogEle).html(iHtml);

                                $(dialogEle).find("#selectRegion").citySelect({
                                    prov:_.isBlank(currProv)?'':currProv,
                                    city:_.isBlank(currCity)?'':currCity,
                                    dist:_.isBlank(currCounty)?'':currCounty,
                                    nodata:"none",
                                    required:false
                                });
                                $(dialogEle).find('button[data-action="cancel"]').on('click',function () {
                                    S_dialog.close($(dialogEle));
                                });
                                $(dialogEle).find('button[data-action="confirm"]').on('click',function () {

                                    var province = $(dialogEle).find('select[name="province"]').val();
                                    var city = $(dialogEle).find('select[name="city"]').val();
                                    var county = $(dialogEle).find('select[name="county"]').val();

                                    province = province==undefined?'':province;
                                    city = city==undefined?'':city;
                                    county = county==undefined?'':county;

                                    that._filterData.filterAddress = [province,city,county];
                                    that.renderProjectList(2);

                                    S_dialog.close($(dialogEle));
                                });

                            });
                            e.stopPropagation();
                            return false;;
                        });
                        break;
                    case 'filterBuildName'://功能分类
                        var currCheckedVal = '',checkList=[];

                        currCheckedVal = that._filterData.filterBuildName;
                        if(currCheckedVal!=''){
                            $this.closest('th').find('.icon-filter i').addClass('fc-v1-blue');
                            $this.closest('th').find('.icon-filter').show();
                        }

                        if(that._buildTypeNames!=null && Object.getOwnPropertyNames(that._buildTypeNames).length>0){
                            $.each(that._buildTypeNames, function (key, value) {
                                checkList.push({id: key, name: value});
                            });
                        }
                        //checkList.push({name:'--',id:''});

                        $this.on('click',function (e) {

                            S_dialog.dialog({
                                contentEle: 'dialogOBox',
                                ele:id,
                                lock: 2,
                                align: 'bottom right',
                                quickClose:true,
                                noTriangle:true,
                                width: '370',
                                minHeight:'110',
                                tPadding: '0px',
                                url: rootPath+'/assets/module/m_common/m_dialog.html'

                            },function(d){//加载html后触发

                                var dialogEle = 'div[id="content:'+d.id+'"] .dialogOBox';

                                var iHtml = template('m_filterableField/m_filter_checkbox',{
                                    currCheckedVal:currCheckedVal,
                                    checkList:checkList
                                });
                                $(dialogEle).html(iHtml);


                                $(dialogEle).find('button[data-action="confirm"]').on('click',function () {

                                    var currBuildType = '';
                                    $(dialogEle).find('input[name="ipt_check"]:checked').each(function () {
                                        var choseBuildType = $(this).val();
                                        currBuildType+=choseBuildType+',';
                                    });
                                    if(currBuildType.length>0){
                                        currBuildType = currBuildType.substring(0,currBuildType.length-1);
                                    }
                                    that._filterData.filterBuildName = currBuildType;

                                    that.renderProjectList(2);

                                    S_dialog.close($(dialogEle));
                                });
                                $(dialogEle).find('button[data-action="cancel"]').on('click',function () {
                                    S_dialog.close($(dialogEle));
                                });
                                var ifChecked = function (e) {
                                    $('input[name="ipt_check"]').iCheck('check');
                                };
                                var ifUnchecked = function (e) {
                                    $('input[name="ipt_check"]').iCheck('uncheck');
                                };
                                //初始化全选checkbox
                                $(dialogEle).find('input[name="ipt_allCheck"]').iCheck({
                                    checkboxClass: 'icheckbox_minimal-green',
                                    radioClass: 'iradio_minimal-green'
                                }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);

                                function delAllBuildTypeCheck() {
                                    var checkedLen = $(dialogEle).find('input[name="ipt_check"]:checked').length;
                                    var allLen = $(dialogEle).find('input[name="ipt_check"]').length;
                                    if(checkedLen==allLen){
                                        $('input[name="ipt_allCheck"]').prop('checked',true);
                                    }else{
                                        $('input[name="ipt_allCheck"]').prop('checked',false);
                                    }
                                    $('input[name="ipt_allCheck"]').iCheck('update');
                                }
                                var ifCheckedByBuildType = function (e) {
                                    delAllBuildTypeCheck();
                                };
                                var ifUncheckedByBuildType = function (e) {
                                    delAllBuildTypeCheck();
                                };
                                //初始化功能分类checkbox
                                $(dialogEle).find('input[name="ipt_check"]').iCheck({
                                    checkboxClass: 'icheckbox_minimal-green',
                                    radioClass: 'iradio_minimal-green'
                                }).on('ifUnchecked.s', ifUncheckedByBuildType).on('ifChecked.s', ifCheckedByBuildType);
                                delAllBuildTypeCheck();

                            });
                            e.stopPropagation();
                            return false;
                        });
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
                        var sortCreateDate = that._filterData.filterSortCreateDate;
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
                                that._filterData.filterSortCreateDate = '1';
                            }
                            else if($this.hasClass('sorting_desc')){
                                that._filterData.filterSortCreateDate = '0';
                            }
                            that._filterData.filterSortSignDate = '';
                            that.renderProjectList(2);
                            e.stopPropagation();
                            return false;
                        });
                        break;
                    case 'signDate'://签订时间
                        var sortSignDate = that._filterData.filterSortSignDate;
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
                                that._filterData.filterSortSignDate = '1';
                            }
                            else if($this.hasClass('sorting_desc')){
                                that._filterData.filterSortSignDate = '0'
                            }
                            that._filterData.filterSortCreateDate = '';
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