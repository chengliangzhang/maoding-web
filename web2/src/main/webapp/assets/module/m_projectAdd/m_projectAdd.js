/**
 * Created by Wuwq on 2017/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_projectAdd",
        defaults = {
            rangeList: [],
            contentList: [],
        };

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this._contentList=null;
        this._constructCompanyList = [];//甲方列表
        this._projectTypeList = [];//项目列表
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._render();
        }
        , _render: function () {
            var that = this;
            // var html = template('m_projectAdd/m_projectAdd', {});
            // $(that.element).html(html);
            that.getProjectType(function(){
                that.getBasicData();
            });
        }
        //地区选择事件绑定
        , bindAddressClick:function () {
            var that = this;
            $(that.element).find('.cityBox select').change(function () {
                var w = 508;
                var p = $(that.element).find('select.prov').val();
                var c = $(that.element).find('select.city').val();
                var d = $(that.element).find('select.dist').val();
                if(p==null|| p==undefined)
                    p='';
                if(c==null|| c==undefined)
                    c='';
                if(d==null|| d==undefined)
                    d='';
                var txt = p+c+d;
                $(that.element).find('.cityText').html(txt);
                $(that.element).find('.detailAddressLabel').css('width',(w-(txt.length*14)-5)+'px');
            });
        }
        , getProjectType:function (callBack) {
            var that = this;
            var option  = {};
            option.url = restApi.url_projectType;
            option.postData = {};
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    that._projectTypeList = response.data;
                    if(callBack!=null){
                        return callBack();
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //获取立项基本数据（设计范围，设计阶段）
        , getBasicData: function () {
            var that = this;

            var option = {};
            option.url = restApi.url_addProjectBasicData;
            option.classId = '.addProjectOBox';
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    var $data = {};
                    $data.rangeList = response.data.rangeList;
                    $data.contentList = response.data.contentList;
                    $data.projectTypeList = that._projectTypeList;
                    that._contentList=response.data.contentList;
                    that._constructCompanyList = response.data.projectConstructList;

                    var html = template('m_projectAdd/m_projectAdd', $data);
                    $(that.element).html(html);

                    $("#selectRegion").citySelect({
                        prov:null,
                        city:null,
                        dist:null,
                        nodata:"none",
                        required:false
                    });
                    that.constructKeyupFun();
                    that.bindAddressClick();
                    that.bindDesignContent($(that.element));

                    that.bindChooseManagerBtn();
                    that.bindBtnSave();

                    that.bindChoseTemplateFun();
                    that.bindBtnAddDesignContent(that._contentList);
                    $(that.element).find('span[data-toggle="tooltip"]').tooltip();


                    $(that.element).find('button[data-toggle]').off('click').on('click',function () {
                        $(that.element).find('ul.dropdown-menu').toggle();
                    });
                    $(that.element).find('.project-type ul>li>a').off('click').on('click',function () {
                        var name = $(this).text();
                        $(that.element).find('input[name="projectType"]').val(name);
                        $(that.element).find('ul.dropdown-menu').toggle();
                    });


                } else {
                    S_dialog.error(response.info);
                }
            });
        }


        //给甲方表单增加keyup事件，进行模糊搜索
        , constructKeyupFun:function(){
            var that = this;
            $(that.element).find('input#constructCompanyName').bind("input propertychange change focus",function(event){
                var $this = $(this);
                var txt = $this.val();
                var mPartyALen = $('.m_partyA').length;
                if ($.trim(txt) != '' && mPartyALen==0) {
                    var option = {};
                    option.$eleId = 'constructCompanyName';
                    $(that.element).find('.partyA-select-box').m_partyA(option);
                    $(that.element).find('.partyA-select-box').show();
                    that.documentBindFun();
                }else if($.trim(txt)!=''){
                    $(that.element).find('.partyA-select-box').show();
                }
            });

        }
        //window document事件绑定
        ,documentBindFun:function () {
            var that = this;
            $(document).bind('click',function(e){
                console.log('document event');
                if(!($(e.target).closest('.partyA-select-box').length>0 || $(e.target).hasClass('partyA-select-box') || $(e.target).hasClass('constructCompanyName'))){
                    $(that.element).find('.partyA-select-box').hide();
                }
            });
        }
        //处理甲方列表
        , dealConstructCompanyLIst: function (data) {
            var that = this;
            var list = [];
            $.each(data, function (i, item) {//constructCompany: constructCompanyName:
                list.push({id: item.companyId, value: item.companyName});
            });
            return list;
        }
        //给模板的设计阶段绑定checkbox点击事件
        , bindDesignContent: function ($obj) {
            var that = this;
            $obj.find('.i-checks input[name="designContent"]').iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_minimal-green'
            }).on('ifChecked', function (e) {
                var $li = $(this).closest('span.designContentDiv');
                var $btnSetTime = $li.find('a[data-action="edit_setDesignContentTime"]:first');
                $btnSetTime.on('click',function () {
                    var options = {};
                    options.$isHaveMemo = false;
                    options.$title = '设置进度';
                    options.$timeInfo = {};

                    options.$timeInfo.startTime = $btnSetTime.attr('data-startTime') || '';
                    options.$timeInfo.endTime = $btnSetTime.attr('data-endTime') || '';
                    options.$validate = 2;
                    options.$okCallBack = function (data) {
                        $btnSetTime.attr('data-startTime', data.startTime);
                        $btnSetTime.attr('data-endTime', data.endTime);

                        var content = '';
                        var startTime = data.startTime;
                        var endTime = data.endTime;
                        if (!isNullOrBlank(startTime))
                            content += startTime;
                        if (!isNullOrBlank(endTime))
                            content += '~' + endTime;
                        if (!isNullOrBlank(startTime) && !isNullOrBlank(endTime))
                            content += (' 共' + (diffDays(startTime, endTime) - 0 + 1) + '天');

                        if(!isNullOrBlank(startTime) || !isNullOrBlank(endTime)){
                            $btnSetTime.html(content).css('color','#4765a0').removeClass('editable-empty');
                        }else{
                            $btnSetTime.html('设置合同进度').css('color','#4765a0').addClass('editable-empty').removeAttr('style');
                        }
                    };
                    $('body').m_inputProcessTime(options);
                });
            });
            if($obj[0]==that.element){
                $obj.find('.i-checks input[name="designContent"]').on('ifUnchecked', function (e) {
                    var $li = $(this).closest('span.designContentDiv');
                    var $btnSetTime = $li.find('a[data-action="edit_setDesignContentTime"]:first');
                    $btnSetTime.attr('data-startTime', '');
                    $btnSetTime.attr('data-endTime', '');
                    $(this).closest('span.designContentDiv').find('a[data-action="edit_setDesignContentTime"]:first').text('设置合同进度').css('color','#ccc');
                    $btnSetTime.off('click');
                });
            }else{
                $obj.find('.i-checks input[name="designContent"]').on('ifUnchecked', function (e) {
                    $obj.remove();
                });
            }
        }
        //立项提交事件
        , saveAddProjectFunc: function () {
            var that = this;
            var option = {};
            option.url = restApi.url_project;
            option.classId = 'body';
            option.postData = {};
            var projectName = $(that.element).find('input[name="projectName"]').val();
            option.postData.projectName = projectName;
            option.postData.flag = 2;//新建甲方的标识
            option.postData.constructCompanyName = $(that.element).find('input[name="constructCompanyName"]').val();
            //option.postData.enterpriseId = $(that.element).find('select[name="constructCompanyName"]').val();
            option.postData.province = $(that.element).find('select[name="province"]').val();
            option.postData.city = $(that.element).find('select[name="city"]').val();
            option.postData.county = $(that.element).find('select[name="county"]').val();
            option.postData.detailAddress = $(that.element).find('input[name="detailAddress"]').val();

            option.postData.projectType = $(that.element).find('input[name="projectType"]').val();

            //option.postData.projectDesignContentList = [];
            /*var timeIsNull = false;//确定合同进度是否为空
            $(that.element).find('input[name="designContent"]:checked').each(function () {
                var obj = {
                    contentId: $(this).attr('data-id'),
                    insertStatus: '0',
                    contentName: $(this).attr('data-name')
                };
                var $span = $(this).closest('span');
                var $btnSetTime = $span.find('a[data-action="edit_setDesignContentTime"]:first');
                var startTime = $btnSetTime.attr('data-startTime');
                var endTime = $btnSetTime.attr('data-endTime');
                var $_class = $(this).closest('.designContentDiv').find('a[data-action="edit_setDesignContentTime"]')[0].className;

                /!*if($_class.indexOf('editable-empty')>0){
                 return timeIsNull = true;
                 }*!/

                if (!isNullOrBlank(startTime) || !isNullOrBlank(endTime)) {
                    obj.projectProcessTimeEntityList = [];
                    obj.projectProcessTimeEntityList.push({
                        startTime: startTime,
                        endTime: endTime
                    });
                }

                option.postData.projectDesignContentList.push(obj);
            });*/
            /*if(timeIsNull){
             return S_dialog.tips('请设置合同进度');
             }*/
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    S_toastr.success('保存成功！');
                    // return;
                    //var urlBaseInfo = window.rootPath + '/iWork/project/projectInformation/' + response.data + '/1';
                    ///window.location.href = urlBaseInfo;

                    that.goProjectDetail(response.data,projectName);

                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        , goProjectDetail:function (id,name) {
            var that = this;
            /*var option = {};
            option.$projectId = id;
            option.$projectName = name;
            option.$contentEle = '#content-right';
            $('#left-menu-box').m_metismenu(option);*/
            location.hash = '/projectDetails/basicInfo?id='+id+'&projectName='+encodeURI(name);
        }
        //立项提交按钮绑定事件（只在非弹窗显示状态下才调用）
        , bindBtnSave: function () {
            var that = this;
            $(that.element).find('.btn-submit').on('click', function (e) {
                that.validateSaveAppProject() && that.saveAddProjectFunc();
                stopPropagation(e);
            });
            $(that.element).find('.btn-cancel').on('click', function (e) {
                //window.location.reload();
                //$('.m_metismenu a[id="projectList"]').click();
                location.hash = '/';
                stopPropagation(e);
            });
        }
        //地区选择
        , bindRegionSelect: function () {
            var that = this;
            $("#selectRegion").citySelect({
                nodata: "none",
                required: false
            });
            $(that.element).find('.cityBox select').change(function () {
                var w = 678;
                var p = $(that.element).find('select.prov').val();
                var c = $(that.element).find('select.city').val();
                var d = $(that.element).find('select.dist').val();
                if (p == null || p == undefined)
                    p = '';
                if (c == null || c == undefined)
                    c = '';
                if (d == null || d == undefined)
                    d = '';
                var txt = p + c + d;
                $(that.element).find('.cityText').html(txt);
                $(that.element).find('.detailAddressLabel').css('width', (w - (txt.length * 14) - 5) + 'px');
            });
        }
        //点击展开选择经营负责人弹窗
        , bindChooseManagerBtn: function () {
            var that = this;
            $(that.element).find('select[name="chooseManager"]').select2({
                placeholder: {
                    id: '',
                    text: '请输入关键字查找'
                },
                clear: true,
                language: "zh-CN",
                ajax: {
                    contentType: "application/json",
                    url: restApi.url_getUserByKeyWord,
                    dataType: 'json',
                    type: 'POST',
                    delay: 500,
                    data: function (params) {
                        var ret = {
                            keyword: params.term,
                            companyId: window.currentCompanyId
                        };
                        return JSON.stringify(ret);
                    },
                    processResults: function (data, params) {
                        return {
                            results: $.map(data.data, function (o, i) {
                                return {
                                    id: o.id,
                                    text: o.user_name
                                }
                            })
                        };
                    },
                    cache: true
                }
            });
        }
        //绑定模板选择相关按钮
        , bindChoseTemplateFun:function(){
            var that = this;
            $(that.element).find('li.designContentTemplate a').on('click',function(){
                var action = $(this).attr('data-action');
                if(action=='withoutTemplate'){
                    var $designContentLi = $(this).closest('ul').find('li.designContentLi');
                    if($designContentLi.find('span').length>0){
                        $designContentLi.html('');
                        $(this).closest('li').find('a').removeClass('actived');
                        $(this).addClass('actived');
                    }
                }else if(action=='choseTemplate'){
                    var iHtml = template('m_projectAdd/m_designContent_template',{contentList:that._contentList});
                    var $designContentLi = $(this).closest('ul').find('li.designContentLi');
                    if($(this).attr('disabled')&&$(this).attr('disabled').length>0){
                        return false;
                    }
                    $designContentLi.html('');
                    if(!($designContentLi.find('span').length>0)){
                        $designContentLi.append(iHtml);
                        that.bindDesignContent($(that.element));
                        $(this).closest('li').find('a').removeClass('actived');
                        $(this).addClass('actived');
                    }
                }
                return false;
            });
        }
        //绑定添加自定义阶段事件
        , bindBtnAddDesignContent:function () {
            var that = this;
            $(that.element).find('a[data-action="addDesignContent"]').on('click',function(e){
                var options = {};
                var $this = $(this);
                options.designContentNameList = [];
                $(that.element).find('.designContentDiv').each(function(){
                    var text = $.trim($(this).find('div>span').text());
                    options.designContentNameList.push(text);

                });
                options.callBack = function(data){
                    data.$obj = $this;
                    that.renderNewHtml(data);
                };
                $(this).m_projectDesignContent_add(options);
                stopPropagation(e);
            });
            $(that.element).find('a[data-action="addDesignContent"]').on('click.bindClear', function (e) {

                var $this = $(this);
                var $$this = $this.closest('li').find('.editable-container input.form-control');
                // $$this.unbind();
                $$this.val('');
                stopPropagation(e);
            });

        }
        //成功添加阶段时渲染新的阶段内容
        , renderNewHtml :function(data){
            var that = this;
            var diff = timeDiffTime(data.startTime,data.endTime);
            var iHtml = '';
            iHtml += '<span class="design_item designContentDiv" data-i="">' +
                '<label class="i-checks dp-inline-block" style="margin-bottom: 0;">' +
                '<input name="designContent" class="checkbox dinline" type="checkbox" value="" data-name="'+data.contentName+'"/>' +
                '</label>' +
                '<div class="pull-left">' +
                '<span class="content-name" data-toggle="tooltip" data-original-title="'+data.contentName+'">'+data.contentName+'</span>';

            if(data.startTime!=undefined || data.endTime!=undefined){
                var startTime = data.startTime;
                var endTime = data.endTime;
                var content = '';
                if (!isNullOrBlank(startTime)){
                    content += startTime;
                }else{
                    data.startTime = '';
                }
                if (!isNullOrBlank(endTime)){
                    content += '~' + endTime;
                }else {
                    data.endTime = '';
                }
                if (!isNullOrBlank(startTime) && !isNullOrBlank(endTime))
                    content += (' 共' + (diffDays(startTime, endTime) - 0 + 1) + '天');

                iHtml += '<a href="javascript:void(0)" class="editable editable-click dp-block" data-action="edit_setDesignContentTime"  data-startTime="'+data.startTime+'"' +
                    ' data-endTime="'+data.endTime+'">'+content+' </a>';
            }else{
                iHtml += '<a href="javascript:void(0)" class="editable editable-click editable-empty dp-block" data-action="edit_setDesignContentTime"  data-startTime="" data-endTime="">设置合同进度</a>' ;
            }

            iHtml += '</div></span>';

            $(that.element).find('li.designContentLi').append(iHtml);

            var $lastEle = (data.$obj).closest('ul').find('li.designContentLi span.designContentDiv:last');

            that.bindDesignContent($lastEle);

            $lastEle.find('input[name="designContent"]').iCheck('check');

            if($(that.element).find('#designContent-error').length>0){
                $(that.element).find('#designContent-error').remove();
            }
            $(that.element).find('span[data-toggle="tooltip"]').tooltip();
        }
        //设计设计内容名称编辑绑定事件
        , bindEditDesignContentName: function ($obj) {
            $obj.editable( {
                type: 'text'
                , mode: 'inline'
                , placeholder: '输入或选择要添加的设计内容'
                // , value: $obj.text()
                , emptytext: ''
                , success: function (response, newValue) {
                }
                , validate: function(value){
                    if ($.trim(value) == '') {
                        S_toastr.warning("设计内容不能为空！");
                        return ' ';
                    }
                }
            });
        }
        //甲方(暂无用)
        , selectFun:function () {
            var that = this;
            $(".js-example-disabled-results").select2({
                language: "zh-CN",
                ajax: {
                    contentType: "application/json",
                    url: restApi.url_enterpriseSearch,
                    dataType: 'json',
                    type:'POST',
                    delay: 500,
                    data: function (params) {
                        var ret={
                            name: params.term // search term
                        };
                        return JSON.stringify(ret);
                    },
                    processResults: function (data, params) {
                        return {
                            results: $.map(data.data.details,function(o,i){
                                return {
                                    id:o.companyid,
                                    text:o.corpname
                                }
                            })
                        };
                    },
                    cache: true
                }
            });
        }
        //验证设计设计内容是否为空
        , validateSaveAppProject:function(){
            var that = this,error = '';
            $(that.element).find('input,select').each(function(){
                var $this = $(this);
                var name = $(this).attr('name');
                if($.trim($this.val())==''){
                    switch (name){
                        case 'projectType':
                            error = '请输入或选择项目类型!';
                            break;
                        case 'projectName':
                            error = '请输入项目名称!';
                            break;
                        case 'constructCompanyName':
                            error = '请输入甲方名称!';
                            break;
                        case 'province':
                            error = '请选择所在地区!';
                            break;

                    };
                    return false;
                }
            });
            /*var flag = $(that.element).find('.designContentBox li.designContentLi span.designContentDiv input[name="designContent"]:checked').length>0;
            if(!flag && error==''){
                error='请设置设计内容!';
            }*/
            if( error!=''){
                S_toastr.warning(error);
                return false;
            }else{
                return true;
            }
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