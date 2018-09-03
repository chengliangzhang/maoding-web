/**
 * Created by wrb on 2016/12/19.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_projectBasicInfo",
        defaults = {
            $isDialog:false,//是否弹窗
            $isView:false,//是否信息浏览,是则屏蔽操作,屏蔽面包屑
            $projectId: null,
            $editFlag:null,
            $deleteFlag:null,
            $renderCallBack: null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._projectInfo = null;//项目信息
        this._currentUserId = null;//当前用户id
        this._currentCompanyId = window.currentCompanyId;
        this._currentCompanyUserId = window.currentCompanyUserId;
        this._designContentAncestors = null;//项目设计阶段数据
        this._constructionCateList = null;//功能分类数据字典的list
        this._designContentList = null;//设计阶段数据字典的list
        this._projectTypeList = null;//项目类型
        this._name = pluginName;

        this._currentUserId = window.currentUserId;
        this._currentCompanyUserId = window.currentCompanyUserId;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            if(that.settings.$isDialog){
                S_dialog.dialog({
                    title: that.settings.$title||'项目基本信息',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    tPadding: '0px',
                    width: '1000',
                    minHeight:'700',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {
                    },
                    cancelText:'关闭'

                },function(d){//加载html后触发

                    that.element = $('div[id="content:'+d.id+'"] .dialogOBox');
                    that.renderPage();
                });
            }else{
                that.renderPage();
            }
        }
        //渲染界面
        ,renderPage:function () {
            var that = this;
            that.getProjectData(function (data) {
                that.initHtmlData(data);
                //that.bindAttentionActionClick();
                rolesControl();

                if(that.settings.$isView==false){
                    if (that.settings.$editFlag!=null && that.settings.$editFlag==1) {//具有编辑权限操作
                        that.bindEditItem();
                        that.uploadRecordFile();
                        that.bindEditable();
                        that.bindBtnAddDesignContent();
                        that.bindCustomInfoTemp();
                        that.bindFileEditFun();
                    }
                    that.bindDeleteProject();
                }
            });
        }
        ,getProjectData: function (callback) {
            var that = this;
            var option = {};
            option.url = restApi.url_loadProjectDetails + '/' + that.settings.$projectId;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    that._projectInfo = response.data;
                    that._designContentAncestors = response.data.projectDesignContentList;
                    that._constructionCateList = response.data.constructionCateList;
                    that.getContentData(function () {

                        if (callback)
                            callback.call(that, that._projectInfo);
                    });
                } else {
                    S_dialog.error(response.info);
                }
            });
        },
        //获取设计阶段基础数据
        getContentData: function (callback) {
            var that = this;
            var option = {};
            option.classId = that.element;
            option.url = restApi.url_getDesignContentList;
            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    that._designContentList = response.data;
                    if (callback) {
                        return callback();
                    }
                } else {
                    S_dialog.error(response.info);
                }
            })
        },
        initHtmlData: function (data) {
            var that = this;
            var flag =  that.settings.$editFlag!=null && that.settings.$editFlag==1 ? true : false;//编辑标识
            var html = template('m_project/m_projectBasicInfo', {
                project: data,
                isCreate: that._currentUserId == data.createBy && that._currentCompanyId == data.companyId,
                isOperator: that._currentCompanyUserId == data.operatorPerson,
                editFlag:flag,
                deleteFlag:that.settings.$deleteFlag,
                currentCompanyId : that._currentCompanyId,
                roles: window.currentRoleCodes,
                fastdfsUrl: window.fastdfsUrl + '/',
                isView:that.settings.$isView
            });
            $(that.element).html(html);
            if (that.settings.$renderCallBack != null) {
                that.settings.$renderCallBack();
            }

            $(that.element).find('span[data-toggle="tooltip"]').tooltip();

            if (!flag) {
                $(that.element).find('a[data-action][data-action!="deleteProject"]').addClass('normalAElem');//删除的权限单独控制，跟编辑权限不一样,删除的权限在渲染此组件时作了处理
            }
            rolesControl();
            that.bindDesignContentCheckbox();//给设计阶段的checkbox绑定事件
        },
        //处理文件上传的删除事件，hover事件
        bindFileEditFun:function () {
            var that = this;
            $(that.element).find('.file-span').hover(function () {
                $(this).find('a[data-action="delFile"]').show();
            },function () {
                $(this).find('a[data-action="delFile"]').hide();
            });
            $(that.element).find('.file-span a[data-action="delFile"]').on('click',function () {

                var dataId = $(this).attr('data-id');

                S_dialog.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                    var option = {};
                    option.url = restApi.url_netFile_delete;
                    option.postData = {
                        id: dataId,
                        accountId: window.currentUserId
                    };
                    m_ajax.postJson(option, function (response) {
                        if (response.code == '0') {
                            S_toastr.success('删除成功！');
                            that._refresh();
                        } else {
                            S_dialog.error(response.info);
                        }
                    });

                }, function () {
                });
            });
        },
        //绑定点击关注或取消关注按钮
        bindAttentionActionClick: function () {
            var that = this;
            $(that.element).find('a.attention').click(function () {
                if ($(this).hasClass('shoucang')) {//点击关注
                    that.addAttention(this);
                } else if ($(this).hasClass('shoucangshixin')) {//点击取消关注
                    that.delAttention(this);
                }
                return false;
            });
        },
        //添加项目关注
        addAttention: function (obj) {
            var option = {}, that = this;
            option.url = restApi.url_attention;
            option.postData = {};
            option.postData.targetId = $(obj).attr('data-id');
            option.postData.type = '1';
            option.postData.companyUserId = window.currentCompanyUserId;
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('关注成功！');
                    $(obj).removeClass('shoucang').addClass('shoucangshixin');
                    $(obj).find('i').addClass('fc-v1-yellow');
                    $(obj).find('.text').text('取消关注');
                    $(obj).attr('data-attention-id', response.data);
                    $(obj).attr('title', '取消关注该项目');
                } else {
                    S_dialog.error(response.info);
                }
            });
        },
        //取消项目关注
        delAttention: function (obj) {
            var option = {}, that = this;
            option.url = restApi.url_attention + '/' + $(obj).attr('data-attention-id');
            m_ajax.get(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('取消关注成功！');
                    $(obj).removeClass('shoucangshixin').addClass('shoucang');
                    $(obj).find('i').removeClass('fc-v1-yellow');
                    $(obj).find('.text').text('关注');
                    $(obj).attr('data-attention-id', '');
                    $(obj).attr('title', '关注该项目');
                } else {
                    S_dialog.error(response.info);
                }
            });
        },
        //点击删除项目
        bindDeleteProject: function () {
            var that = this;
            $(that.element).find('a[data-action="deleteProject"]').on('click',function(){
                var $this = this;

                S_dialog.confirm('删除后将不能恢复，您确定要删除吗？', function () {

                    var option = {};
                    var id = $($this).attr('data-id');
                    option.url = restApi.url_deleteProject + '/' + id;
                    m_ajax.get(option, function (response) {
                        if (response.code == '0') {
                            S_toastr.success('删除成功！');
                            location.hash = '/';
                        } else {
                            S_dialog.error(response.info);
                        }
                    });

                }, function () {
                });
            });
        },
        //当在位编辑的文本显示为empty时，改为“请设置”
        dealEmptyText: function () {
            var that = this;
            $('.m_projectBasicInfo .editable-empty,.m_projectBasicInfo a.editable').each(function () {

                var text = $(this).text();
                if( text == 'Empty' || $.trim(text) == ''){
                    text = '未设置'
                }
                $(this).text(text);

                if (that.settings.$editFlag!=null && that.settings.$editFlag==1) {
                    $(this).css('color', (($.trim(text).indexOf('未设置') > -1 || $.trim(text) == '未签订')) ? '#ccc' : '#4765a0');
                } else {
                    $(this).css('color', '#676a6c');
                }
            });
        },
        //上传附件
        uploadRecordFile: function () {
            var that = this;
            $('#filePicker').m_fileUploader({
                server: restApi.url_attachment_uploadProjectContract,
                fileExts: 'pdf',
                fileSingleSizeLimit:20*1024*1024,
                formData: {},
                multiple:true,
                //duplicate:false,
                loadingId: '.ibox-content',
                innerHTML: '<i class="fa fa-upload fa-fixed"></i>',
                uploadBeforeSend: function (object, data, headers) {
                    data.companyId = window.currentCompanyId;
                    data.accountId = window.currentUserId;
                    data.projectId = that.settings.$projectId;
                },
                uploadSuccessCallback: function (file, response) {
                    $(that.element).m_projectBasicInfo({
                        $projectId: that.settings.$projectId,
                        $editFlag:that.settings.$editFlag,
                        $deleteFlag:that.settings.$deleteFlag
                    });

                    S_toastr.success('保存成功！');
                }
            },true);
        }

        //验证当前选的组织是否已选过，选过的话，经营负责人不能另选
        , _validateIssueTaskCompany: function (toCompanyId, callBack) {

            var options = {}, that = this;

            options.url = restApi.url_validateIssueTaskCompany;
            options.postData = {};
            options.postData.toCompanyId = toCompanyId;
            options.postData.projectId = that.settings.$projectId;
            m_ajax.postJson(options, function (response) {

                if (response.code == '0') {
                    return callBack(response.data);
                } else {
                    S_dialog.error(response.info);
                }
            })
        }
        //获取甲方列表
        , getConstructCompanyList: function (keyword, callback) {
            var that = this;
            var option = {};
            option.url = restApi.url_constructList;
            option.postData = {};
            if (keyword != null) {
                option.postData.keyword = keyword;
            }
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    that._constructCompanyList = that.dealConstructCompanyLIst(response.data);
                    if (callback != null) {
                        return callback(that._constructCompanyList);
                    }
                } else {
                    S_dialog.error(response.info);
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
        //乙方
        , chosePartyBOrg: function ($btn) {
            var that = this;
            var options = {};
            options.titleHtml = '<h3 class="popover-title">编辑</h3>';
            options.popoverStyle = 'width:360px;min-width:360px';
            options.contentStyle = 'padding:15px 15px 0';
            options.content = template('m_project/m_popover_partyB', {});
            options.onShown = function ($popover) {

                var $select_partB = $popover.find('select[name="partB"]:first').select2({
                    placeholder: {
                        id: '',
                        text: ''
                    },
                    allowClear: true,
                    language: "zh-CN",
                    minimumResultsForSearch: Infinity,
                    ajax: {
                        contentType: "application/json",
                        url: restApi.url_getUsedPartB,
                        dataType: 'json',
                        type: 'POST',
                        delay: 500,
                        data: function (params) {
                            var ret = {
                                keyword: params.term
                                /*,companyId: window.currentCompanyId*/
                            };
                            return JSON.stringify(ret);
                        },
                        processResults: function (data, params) {

                            return {
                                results: $.map(data.data, function (o, i) {
                                    return {
                                        id: o.id,
                                        text: o.companyName
                                    }
                                })
                            };
                        },
                        cache: false
                    }
                });

                var companyBid = $btn.attr('data-companyBid');
                var companyBidName = $btn.attr('data-companyBidName');
                if (!isNullOrBlank(companyBid)) {//不为空，初始化select值
                    var $companyB = $('<option selected>' + companyBidName + '</option>').val(companyBid);
                    $select_partB.append($companyB).trigger('change');
                }
                that._savePartyB_validate($popover);
            };
            options.onSave = function ($popover) {
                var data = {};
                data.companyBid = $popover.find('select[name="partB"]').val();
                /*data.partBManagerId = $popover.find('select[name="operator"]').val();*/
                /*data.partBDesignerId = $popover.find('select[name="projectLeader"]').val();*/

                var flag = $popover.find('form').valid();
                if (!flag) {
                    return false;
                } else {

                    if (that._projectInfo.companyBid != null && that._projectInfo.companyBid != '' && data.companyBid != that._projectInfo.companyBid) {
                        var $btn = $popover.find('button.m-popover-submit');
                        if($btn.closest('div').find('.m-popover').length>0){
                            return false;
                        }

                        S_dialog.confirm('删除（更换）乙方后，相关费用信息将被删除，你确定保存吗？', function () {

                            that.saveProjectData(null, data, 'partyB', function () {
                                S_toastr.success('保存成功！');
                                that._refreshMenu();
                            });

                        }, function () {
                        });

                    } else {
                        that.saveProjectData(null, data, 'partyB', function () {
                            //window.location.href = window.location.href;
                            that._refreshMenu();
                        });
                    }
                }
                return false;
            };
            $btn.m_popover(options, true);
            return false;
        }
        //甲方
        , chosePartyAOrg: function ($btn) {
            var that = this;
            var options = {};
            options.placement = 'bottom';
            options.titleHtml = '<h3 class="popover-title">编辑</h3>';
            options.popoverStyle = 'width:400px;min-width:400px';
            options.contentStyle = 'padding:15px 0';

            var companyName = that._projectInfo.partyACompany==null?'':that._projectInfo.partyACompany.companyName;
            var companyId = that._projectInfo.partyACompany==null?'':that._projectInfo.partyACompany.id;

            options.content = template('m_project/m_popover_partyA', {
                constructCompanyName:companyName,
                enterpriseId:companyId
            });
            options.onShown = function ($popover) {

                $popover.find('input#constructCompanyName').bind("input propertychange change focus",function(event){
                    var $this = $(this);
                    var txt = $this.val();
                    var mPartyALen = $('.m_partyA').length;
                    if ($.trim(txt) != '' && mPartyALen==0) {
                        var option = {};
                        option.$eleId = 'constructCompanyName';
                        $popover.find('.partyA-select-box').m_partyA(option);
                        $popover.find('.partyA-select-box').show();
                        that.documentBindFun();
                    }else if($.trim(txt)!=''){
                        $popover.find('.partyA-select-box').show();
                    }
                });

            };
            options.onSave = function ($popover) {
                var data = {};
                var enterpriseId = $popover.find('input[name="constructCompanyName"]').attr('data-id');
                var constructCompanyName = $popover.find('input[name="constructCompanyName"]').val();
                //if(!(that._projectInfo.partyACompany!=null && that._projectInfo.partyACompany.id==enterpriseId)){
                    that.saveProjectData(null, constructCompanyName, 'constructCompanyName');
                    return false;
                //}
            };
            $btn.m_popover(options, true);
            return false;
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
        //给设计范围、设计阶段、地址绑定点击事件
        , bindEditItem: function () {
            var that = this;
            $(that.element).find('a[data-action],div[data-action]').on('click', function (event) {
                var action = $(this).attr('data-action');
                var data = {};
                var $this = $(this);
                switch (action){
                    case 'edit_designRange':
                        var options = {};
                        options.$projectId = that._projectInfo.id;
                        options.$projectDesignRange = that._projectInfo.projectRangeList;
                        options.$okCallBack = function (data) {
                            var obj = {};
                            obj.projectDesignRangeList = data;
                            if (!data || data.length < 1) {
                                obj.projectDesign = '1';//原有设计范围，后删空
                            } else {
                                obj.projectDesign = '0';//增改涉及范围
                            }
                            that.saveProjectData(null, obj, 'projectDesignRangeList');
                        };
                        $this.m_addProjectDesignRange(options);
                        break;

                    case 'edit_setDesignContentTime'://项目阶段12按钮绑定
                        that.bindTimeChangeRecord(this);
                        break;

                    case 'edit_signDate'://合同签订按钮绑定
                        var options = {};
                        options.$placement = 'right';
                        options.$eleId = 'a[data-action="edit_signDate"]';
                        options.$okCallBack = function ($data) {
                            that.saveProjectData(null, $data, 'signDate');
                        };
                        $this.m_quickDatePicker(options);
                        break;

                    case 'edit_projectCreateDate'://编辑立项时间
                        var options = {};
                        options.$title = '编辑立项时间';
                        options.$placement = 'right';
                        options.$eleId = 'a[data-action="edit_projectCreateDate"]';
                        options.$okCallBack = function ($data) {
                            that.saveProjectData(null, $data, 'projectCreateDate');
                        };
                        $this.m_quickDatePicker(options);
                        break;

                    case 'edit_address'://编辑地址按钮绑定

                        var options = {};
                        options.$title = '编辑';
                        if(that._projectInfo.projectLocation!=null){
                            options.$province = that._projectInfo.projectLocation.province;
                            options.$city = that._projectInfo.projectLocation.city;
                            options.$county = that._projectInfo.projectLocation.county;
                            options.$detailAddress = that._projectInfo.projectLocation.detailAddress;
                        }
                        options.$placement = 'top';
                        options.$okCallBack = function (data) {
                            if (data.province== '' && data.city== '' && data.detailAddress== '') {
                                S_toastr.warning('项目地点不能为空')
                            }else{
                                that.saveProjectData(null, data, 'address')
                            }
                        };
                        $(this).m_entryAddress(options);
                        break;

                    case 'edit_companyBidName'://编辑乙方
                        that.chosePartyBOrg($this);
                        break;

                    case 'edit_constructCompanyName'://编辑甲方
                        that.chosePartyAOrg($this);
                        break;

                    case 'edit_builtType'://功能分类
                        var option = {};
                        option.$title = '功能分类';
                        option.$dataDictionaryList = that._constructionCateList;
                        option.$dataList = that._projectInfo.buildTypeList;
                        option.$okCallBack = function (data) {
                            that.saveProjectData(null, data, 'changedBuiltTypeList');
                        };
                        $this.m_customDataDictionary(option);

                        break;

                    case 'edit_projectType'://项目类型

                        if(that._projectTypeList==null){
                            that.getProjectType(function (data) {
                                that.editProjectType(data,$this);
                            })
                        }else{
                            that.editProjectType(that._projectTypeList,$this);
                        }
                        return false;
                        break;
                }
                return false;
            })
        }

        , editProjectType : function (projectTypeList,$this) {
            var that = this;
            var projectType = that._projectInfo==null||that._projectInfo.projectType==null ||that._projectInfo.projectType.content==null?'':that._projectInfo.projectType.content;
            var options = {};
            options.popoverStyle = 'border: 0;border-radius: 0;box-shadow: none;position: relative;display: block;';
            options.contentStyle = 'padding:0;';
            options.type = 'inline';
            options.hideArrow = true;
            options.hideElement = true;
            options.content = template('m_project/m_popover_projectType', {projectTypeList:projectTypeList,projectType:projectType});
            options.onShown = function ($popover) {

                $popover.find('input[name="projectType"]').off('focus keyup').on('focus keyup',function () {
                    if($.trim($(this).val())!=''){
                        $popover.find('span.editable-clear-x').show();
                    }else{
                        $popover.find('span.editable-clear-x').hide();
                    }
                });
                $popover.find('input[name="projectType"]').focus();
                $popover.find('span.editable-clear-x').off('click').on('click',function () {
                    $popover.find('input[name="projectType"]').val('');
                    $popover.find('span.editable-clear-x').hide();
                });
                $popover.find('button[data-toggle]').off('click').on('click',function () {
                    $popover.find('ul.dropdown-menu').toggle();
                });
                $popover.find('ul>li>a').off('click').on('click',function () {
                    var name = $(this).text();
                    $popover.find('input[name="projectType"]').val(name);
                    $popover.find('ul.dropdown-menu').toggle();
                });

            };
            options.onSave = function ($popover) {
                var projectType = $popover.find('input[name="projectType"]').val();
                if($.trim(projectType)==''){
                    $popover.find('form').append('<div class="editable-error-block help-block" style="display: block;">项目类型不能为空！</div>');
                    return false;
                }else{
                    that.saveProjectData(null, projectType, 'projectType');
                }

            };
            $this.m_popover(options, true);
        }
        //获取项目类型
        ,getProjectType:function (callBack) {
            var that = this;
            var option  = {};
            option.url = restApi.url_projectType;
            option.postData = {};
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    that._projectTypeList = response.data;
                    if(callBack!=null){
                        return callBack(response.data);
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })
        }


        //'设置进度'/'进度变更'按钮事件绑定
        , bindTimeChangeRecord: function (obj) {
            var that = this;
            var contentIndex = $(obj).closest('.designContentDiv').attr('data-i');
            var contentObj = that._designContentAncestors[contentIndex];
            var timeList = contentObj.projectProcessTimeEntityList;
            if (timeList == null) {
                contentObj.projectProcessTimeEntityList = [];
            }
            var options = {};
            if ($.trim($(obj).html()) == '未设置合同进度') {
                options.$isHaveMemo = false;
                options.$timeInfo = {};
                options.$timeInfo.startTime = (timeList && timeList.length > 0) ? timeList[0].startTime : '';
                options.$timeInfo.endTime = (timeList && timeList.length > 0) ? timeList[0].endTime : '';
                options.$title = '设置进度';
                options.$okText = '保存';
                options.$validate = 1;
                options.$okCallBack = function (data) {
                    if (data != null) {
                        var obj = data;
                        obj.targetId = contentObj.id;
                        obj.type = 1;
                        that.saveProjectProcessTime(obj);
                    }
                };
                $('body').m_inputProcessTime(options);
            } else {
                options.$isHaveMemo = false;
                var designContentArr = [];
                var contentObjClone = jQuery.extend(true, {}, contentObj);
                designContentArr.push(contentObjClone);
                options.$designContentList = designContentArr;
                options.$okCallBack = function (data, returnType) {
                    if (data != null) {
                        var obj = data;
                        obj.targetId = contentObj.id;
                        obj.type = 1;
                        that.saveProjectProcessTime(obj);
                    }
                };
                $('body').m_editDesignContent(options);
            }
        }
        //绑定设计阶段Checkbox的触发事件
        , bindDesignContentCheckbox: function () {
            var that = this;
            var resetCheck = function ($el, checked) {
                $el.prop('checked', checked);
                var $icheck = $el.iCheck({
                    checkboxClass: 'icheckbox_square-blue',
                    radioClass: 'iradio_square-blue'
                });
                $icheck.off('ifUnchecked.s').on('ifUnchecked.s', ifUnchecked);
                $icheck.off('ifChecked.s').on('ifChecked.s', ifChecked);
            };
            var ifChecked = function (e) {
                var $this = $(this);
                var i = $(this).closest('.designContentDiv').attr('data-i');
                that._designContentList[i].isChecked = 1;
                var designContentList = that.dealChoseDesignContent(that._designContentList);
                that.saveProjectData(null, designContentList, 'projectDesignContentList');
            };

            var ifUnchecked = function (e) {
                var $this = $(this);
                var i = $(this).closest('.designContentDiv').attr('data-i');
                var isHas = $this.attr('data-isHas');
                var isAllUnchecked = $this.closest('div.projectDesignContent').find('input.checkbox[name="designContent"]:checked').length < 1 ? true : false;
                if (!isNullOrBlank(isHas) && isHas === '0') {
                    S_toastr.success('该设计内容存在签发分解任务，不能删除！');
                    resetCheck($this, true);
                }
                /*else if (isAllUnchecked) {
                    S_toastr.warning('请至少选择一个设计内容！');
                    resetCheck($this, true);
                } */
                else {
                    resetCheck($this, true);

                    S_dialog.confirm('该设计内容取消后将不能恢复，确定吗？', function () {

                        var id = $this.closest('.designContentDiv').attr('data-id');
                        that.deleteProjectTask(id,function(){
                            $this.closest('.designContentDiv').remove();
                            S_toastr.success('该设计内容已取消！');
                        });
                        resetCheck($this, false);

                    }, function () {
                    });
                }
            };

            $('.i-checks input[name="designContent"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);
        }
        //处理已选上的设计内容
        , dealChoseDesignContent: function (list) {
            var that = this;
            var designContentList = [];
            var idString = '';//原有的设计内容的id集合，用于判断保存时是否传递该内容
            $.each(that._designContentAncestors, function (j, obj) {
                idString += obj.id + ',';
            });
            $.each(list, function (i, item) {
                if (item.isChecked == 1) {
                    if (idString.indexOf(item.designContentId) < 0) {
                        item.contentId = item.contentId ? item.contentId : item.id;
                        item.id = null;
                    }
                    item.contentName = item.contentName ? item.contentName : item.name;
                    item.insertStatus = '0';
                    designContentList.push(item);
                } else if (idString.indexOf(item.designContentId) > -1) {
                    item.insertStatus = '1';//删除某一设计内容时
                    designContentList.push(item);
                }
            });
            return designContentList;
        }
        //更新设计内容时间html
        , designContentTimeHtml: function (obj, contentIndex) {
            var that = this;
            var contentObj = that._designContentList[contentIndex];
            var iHtml = '';
            var startTime = '', endTime = '';
            if (contentObj.projectProcessTimeEntityList && contentObj.projectProcessTimeEntityList.length > 0) {
                var len = contentObj.projectProcessTimeEntityList.length;
                startTime = contentObj.projectProcessTimeEntityList[len - 1].startTime;
                endTime = contentObj.projectProcessTimeEntityList[len - 1].endTime;
            }
            if (startTime != null && startTime != '') {
                iHtml += startTime;
            }
            if (endTime != null && endTime != '') {
                iHtml += '~' + endTime;
            }
            if (startTime != null && startTime != '' && endTime != null && endTime != '') {
                iHtml += (' 共' + (diffDays(startTime, endTime) - 0 + 1) + '天');
            }
            // $(obj).closest('.designContentDiv').find('.timeSpan').html(iHtml);
            $(obj).text(iHtml);
            that._designContentList[contentIndex] = contentObj;
        }
        //在位编辑内容初始化
        , bindEditable: function () {
            var that = this;
            var elements = $(that.element).find('td a[data-action]:not([data-action="addDesignContent"],[data-action="customInfoTemp"],[data-action="delFile"])');


            elements.each(function () {
                var $this = $(this),
                    obj = null,
                    action = $this.attr('data-action') ? $this.attr('data-action') : $this.closest('td').attr('data-action'),
                    type = action.indexOf("_") > -1 ? action.split("_")[0] : 'text',
                    name = action.indexOf("_") > -1 ? action.split("_")[1] : 'text',
                    isNumber = $this.attr('data-isNum') == 'number' ? true : false,//判断输入内容是否是数字
                    isProjectName = name == 'projectName' ? true : false,//判断输入内容是否是项目名称
                    noPoint = $this.attr('data-isNoPoint') == 'noPoint' ? true : false,//判断输入内容是否是数字
                    source = '',//当为下拉框类型时
                    content = $this.text(),//输入的内容
                    title = $this.closest('td').prev('td').text();//输入的内容
                if (type != 'edit') {//主要是除设计范围跟设计内容以外的信息
                    switch (name) {//当需要多个输入框时，需设置各个value值
                        case 'status':
                            obj = {
                                type: type
                                , name: name
                                , value: $('a[data-action="select_status"]').attr('data-status')
                                , mode: 'inline'
                                , source: [
                                    {value: 0, text: '进行中'},
                                    {value: 2, text: '已完成-未结清'},
                                    {value: 4, text: '已完成-已结清'},
                                    {value: 1, text: '已暂停-未结清'},
                                    {value: 5, text: '已暂停-已结清'},
                                    {value: 3, text: '已终止-未结清'},
                                    {value: 6, text: '已终止-已结清'}
                                ]
                                , success: function (response, newValue) {
                                    if (newValue === void 0 || newValue === null)
                                        return false;
                                    that.saveProjectData(response, newValue, name);
                                }
                            };
                            break;
                        /*case 'constructCompanyName':
                            obj = {
                                name: name
                                , type: type
                                , value: $.trim($('a[data-action="typeaheadjs_constructCompanyName"]').text())
                                , mode: 'inline'
                                , clear: true
                                , typeahead: {
                                    name: name,
                                    local: []//that._constructCompanyList
                                }
                                , success: function (response, newValue) {
                                    var text = $this.next().find('input.tt-query').next().text();
                                    newValue = text;
                                    that.saveProjectData(response, newValue, name);
                                },validate:function(value){

                                    if ($.trim(value) == ''){
                                        var text = $this.text();
                                        if(text =='未设置' ){
                                            return "请输入甲方名称！";
                                        };
                                        return "甲方名称不能为空！";
                                    }
                                }
                                , display: function (newValue) {
                                    if (newValue == '') {
                                        $this.next('未设置');
                                    }
                                }
                            };

                            break;*/
                    }
                    if (!obj) {
                        $this.editable({
                            type: type
                            , name: name
                            , mode: 'inline'
                            , placeholder: '请输入' + title
                            , source: source
                            , value: content
                            , emptytext: ''
                            , success: function (response, newValue) {

                                if(name && name=='designContentName') {

                                    //var id = $this.attr('data-id');
                                    //that._saveDesignContent(newValue,id);

                                }else if(name && name.indexOf('propertyField')>-1){//当为field时，自定义属性值保存

                                    var fieldId = $this.attr('data-field-id');
                                    if(name.indexOf('propertyFieldDown')>-1){
                                        var upStoreys = $(that.element).find('a[data-action^="text_propertyFieldUp"]').attr('data-field-value');
                                        newValue = newValue+';'+upStoreys;
                                    }else if(name.indexOf('propertyFieldUp')>-1){
                                        var downStoreys = $(that.element).find('a[data-action^="text_propertyFieldDown"]').attr('data-field-value');
                                        newValue = downStoreys+';'+newValue;
                                    }
                                    that.saveProjectPropertyFieldValue(response,newValue,fieldId,name);

                                }else{
                                    that.saveProjectData(response, newValue, name);
                                }
                            }
                            , validate: (isNumber && function (value) {
                                if ($.trim(value) != '') {
                                    var reg = /^[0-9]+(\.[0-9]{1,2})?$/;
                                    if (!reg.test(value)) return "请输入数字，小数位保留2位！";
                                }
                            }) || (noPoint && function (value) {
                                if ($.trim(value) != '') {
                                    var reg = /^[0-9]*[1-9][0-9]*$/;
                                    if (!reg.test(value)) return "请输入整数！";
                                }
                            }) || (isProjectName && function (value) {//
                                if ($.trim(value) == '') {
                                    return "项目名称不能为空！";
                                }
                            }) || (name == 'designContentName' && function(value){
                                if ($.trim(value) == '') {
                                    S_toastr.warning("设计内容不能为空！");
                                    return ' ';
                                }
                            })
                        });
                    } else {
                        if(obj.name==='constructCompanyName'){
                            $this.editable(obj).on('shown',function(e,editable){
                                //editable.input.$input.val(editable.value);

                            });
                        }else{
                            $this.editable(obj);
                        }
                    }
                }
                elements.off('click.clearEditable').on('click.clearEditable', function (event) {
                    var action = $(this).attr('data-action');
                    $(that.element).find('.editable-container,.popover.m-popover').each(function () {
                        if ($(this).parent().find('a').attr('data-action') != action) {
                            if($(this).find('button.editable-cancel').length>0){
                                $(this).find('button.editable-cancel').click();
                            }else{
                                $(this).find('button.m-popover-close').click();
                            }

                        }
                    });
                    if (action && action.indexOf('designContentName')>-1) {
                        $(this).closest('label.i-checks').iCheck('disable');
                    }


                    return false;
                });
                //显示x-editable事件shown
                $this.on('shown', function(e, editable) {
                    var unitName = $(this).attr('data-field-unit');
                    if(unitName!=undefined){
                        $this.parent().find('.unit-span').hide();
                        $this.next().find('.editable-input input').css('padding-right','36px');
                        $this.next().find('.editable-input').append('<span class="unit-span-in">'+unitName+'</span>');
                    }
                });
                //点击x-editable cancel事件
                $this.on('hidden', function(e, reason) {
                    if(reason === 'cancel') {
                        var unitName = $(this).attr('data-field-unit');
                        if(unitName!=undefined){
                            $this.parent().find('.unit-span').show();
                        }
                    }
                });
                if (action && action.indexOf('designContentName')>-1) {
                    elements.on('hidden', function (e, reason) {
                        $(this).closest('label.i-checks').iCheck('enable');
                    });
                }

                that.dealEmptyText();//当在为编辑的文本显示为empty时，改为“请设置”
            });

        }

        //绑定添加自定义阶段事件
        , bindBtnAddDesignContent:function () {
            var that=this,contentList = that._designContentList;
            $(that.element).find('a[data-action="addDesignContent"]').on('click',function(e){
                var options = {};
                var $that = $(this);
                options.$title = '添加设计任务';
                options.designContentNameList = [];
                $(that.element).find('.designContentDiv').each(function(){
                    var text = $.trim($(this).find('div>span').text());
                    options.designContentNameList.push(text);

                });
                options.onShown = function(data){

                    setTimeout(function(){
                    var p_left = $that.next('.popover.m-popover').css('left').replace('px','')-0;
                    if(p_left>1000){
                            $that.next('.popover.m-popover').css('left','1070px');
                    }
                    },50);
                };
                options.callBack = function(data){
                    that._saveDesignContent(data);

                };
                $(this).m_projectDesignContent_add(options);
                //stopPropagation(e);
            });
            $(that.element).find('a[data-action="addDesignContent"]').on('click.bindClear', function (e) {

                var $this = $(this);
                var $$this = $this.closest('LI').find('.editable-container input.form-control');
                $$this.unbind();
                that.dealDesignContent($this,$$this,contentList);
                $this.closest('LI').find('.editable-container input.form-control').on('click',function () {
                    that.dealDesignContent($this,$$this,contentList);
                });
                stopPropagation(e);
            });
        }
        //提交保存设计阶段
        ,_saveDesignContent: function(data) {
            var that = this;
            var options = {};
            options.url = restApi.url_saveOrUpdateProjectDesign;
            data.projectId = that.settings.$projectId;
            data.companyId = that._currentCompanyId;
            options.postData = data;
            m_ajax.postJson(options, function (response) {
                if (response.code == '0') {
                    S_toastr.success('保存成功！');
                    if(that.settings.$okBackCall!=null){
                        that.settings.$okBackCall();
                    }
                    that._refresh();
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //阶段预选展示处理
        , dealDesignContent : function($this,$$this,contentList){

            if (contentList != null && contentList.length > 0 && $this.closest('LI').find('.tt-dropdown-menu,.tt-dataset-designContent').css('display')=='none') {

                var iHtml = '<div class="tt-dataset-designConten"><div class="tt-suggestions">';

                for (var i = 0; i < contentList.length; i++) {
                    iHtml += '<div class="tt-suggestion" style="white-space: nowrap; cursor: pointer;">' +
                        '<p style="white-space: normal;">' + contentList[i].name + '</p></div>';
                }
                iHtml += '</div></div>';

                $this.closest('LI').find('.tt-dropdown-menu').html(iHtml).show();
                $this.next('.editable-container').find('.editable-input .twitter-typeahead input.form-control').attr('placeholder','输入或选择要添加的设计内容');
                $this.closest('LI').find('.tt-dropdown-menu .tt-suggestions').find('.tt-suggestion').on('click', function () {
                    var t = $(this).find('p').text();
                    $$this.val(t);
                    $$this.next().text(t);
                    $this.closest('LI').find('.tt-dropdown-menu,.tt-dataset-designContent,.tt-suggestions').hide();
                });
            }else{
                return false;
            }
        }

        //保存进度时间到后台
        , saveProjectProcessTime: function (obj) {
            var that = this;
            var options = {};
            options.url = restApi.url_saveOrUpdateProjectProcessTime;
            options.postData = obj;
            options.postData.projectId = that._projectInfo.id;
            m_ajax.postJson(options, function (response) {
                if (response.code == '0') {

                    S_toastr.success('保存成功！');
                    that._refresh();
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //保存基本信息时访问到后台
        , saveProjectData: function (res, value, name, callback) {
            var that = this;
            var options = {};
            options.url = restApi.url_project;
            options.classId = '#project_detail';
            options.postData = {};
            if (typeof value == "object" && !value && !name) {
                options.postData = value;
            }
            else if (name && name == 'partyB') {
                options.postData.companyBid = value.companyBid;
                options.postData.flag = 3;
            }
            else if (name && name == 'projectDesignRangeList') {

                options.postData.projectDesignRangeList = value.projectDesignRangeList;
                options.postData.projectDesign = value.projectDesign;
            }
            else if (name && name == 'investmentEstimation') {
                options.postData.flag = 1;
                options.postData[name] = value;
            }
            else if (name && name == 'constructCompanyName') {
                options.postData.flag = 2;
                options.postData.constructCompanyName = value;
            }
            else if(name && name == 'address'){
                options.postData.province = value.province;
                options.postData.city = value.city;
                options.postData.county = value.county;
                options.postData.detailAddress = value.detailAddress;
            }
            else if(name && name == 'signDate'){
                options.postData.flag = 4;
                options.postData[name] = value;
            }
            else {
                options.postData[name] = value;
            }
            options.postData.id = that._projectInfo.id;

            m_ajax.postJson(options, function (response) {
                if (response.code == '0') {
                    $(that.element).m_projectBasicInfo({
                        $projectId: that.settings.$projectId,
                        $editFlag:that.settings.$editFlag,
                        $deleteFlag:that.settings.$deleteFlag
                    });
                    if(!(name && name == 'partyB')){//乙方已有swal弹窗提示
                        S_toastr.success('保存成功！');
                    }

                    if (callback) {
                        return callback();
                    }
                } else {
                    S_dialog.error(response.info);
                }
            });
        }

        //保存自定义属性值
        ,saveProjectPropertyFieldValue:function (res,newValue,fieldId,name) {
            var that = this;
            var options = {};
            options.url = restApi.url_saveProjectField;
            options.postData = {};
            options.postData.projectId = that._projectInfo.id;
            options.postData.operatorId = that._currentUserId;
            options.postData.id = fieldId;
            options.postData.fieldValue = newValue;
            m_ajax.postJson(options, function (response) {
                if (response.code == '0') {
                    S_toastr.success('保存成功！');
                    that._refresh();
                } else {
                    S_dialog.error(response.info);
                }
            });
        }

        //删除项目阶段
        , deleteProjectTask: function (id,callback) {
            var that = this;
            var options = {};
            options.url = restApi.url_deleteProjectDesign;
            options.postData = {};
            options.postData.id = id;
            m_ajax.postJson(options, function (response) {
                if (response.code == '0') {
                    //S_toastr.success('保存成功！');
                    if(callback){
                        return callback();
                    }
                } else {
                    S_dialog.error(response.info);
                }
            });
        }

        //保存乙方验证
        , _savePartyB_validate: function (ele) {
            var that = this, $ele = $(ele);
            $ele.find('form').validate({
                rules: {
                    operator: {
                        operatorCk: true
                    }
                },
                messages: {
                    operator: {
                        operatorCk: '请选择经营负责人！'
                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    error.appendTo(element.closest('.form-group'));
                    error.css({'margin-left':'0px'});
                }
            });
            $.validator.addMethod('operatorCk', function (value, element) {
                var isTrue = false;
                var partB = $ele.find('select[name="partB"]');
                if (partB.val() == null || partB.val() == '' || (partB.val() != null && partB.val() != '' && value != null && value != '')) {
                    isTrue = true;
                }
                return isTrue;
            }, '请选择经营负责人！');
        }
        //绑定自定义编辑信息模板事件
        ,bindCustomInfoTemp:function () {

            var that = this;
            $(that.element).find('a[data-action="customInfoTemp"]').on('click',function () {
                var option = {};
                option.$projectId = that._projectInfo.id;
                option.$okCallBack = function () {
                    that._refresh();
                };
                $('body').m_editCustomPropertyTemp(option);
            });
        }
        //刷新当前菜单
        , _refreshMenu: function () {
            var that=this;
            /*var option = {};
            option.$projectId = that.settings.$projectId;
            $('#project_menu').m_projectMenu(option);*/
            that._refresh();
        }
        //刷新当前界面
        , _refresh: function () {
            var option = {}, that = this;
            option.$projectId = that.settings.$projectId;
            option.$editFlag = that.settings.$editFlag;
            option.$deleteFlag = that.settings.$deleteFlag;
            option.$renderCallBack = function () {
                if(that.settings.$deleteFlag!=1){//不存在删除项目的权限，删除此按钮
                    $('#project_detail a[data-action="deleteProject"]').remove();
                }
            };
            $('#content-right').m_projectBasicInfo(option,true);
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
