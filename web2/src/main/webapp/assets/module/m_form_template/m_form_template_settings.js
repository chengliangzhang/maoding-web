/**
 * 审批管理-添加审批
 * Created by wrb on 2018/9/14.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_form_template_settings",
        defaults = {
             isDialog:true
            ,type:1//1=我的审批
            ,id:null//表单ID
            ,saveCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentUserId = window.currentUserId;
        this._currentCompanyId = window.currentCompanyId;

        this._title = '自定义审批表单';
        this._subTitle = '我的审批';

        this._$controlBox = {};//控件面板
        this._$contentForm = {};//已选择的控件表单
        this._$propertyForm = {};//控件属性表单
        this._$formProperty = {};//审批属性表单

        this._formFieldInfo = [];//预存的json，
        this._baseData = {};//
        this._isStatisticsList = [];
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            var option = {};
            option.url = restApi.url_prepareFormToEdit ;
            option.postData = {};
            if(that.settings.id)
                option.postData.id = that.settings.id;

            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    that._baseData = response.data;
                    var html = template('m_form_template/m_form_template_settings',{
                        title:that._title,
                        subTitle:that._subTitle,
                        baseData:response.data
                    });
                    that.renderDialog(html,function () {

                        that._$controlBox = $(that.element).find('#controlBox');
                        that._$contentForm = $(that.element).find('#contentBox form.content-form');
                        that._$propertyForm = $(that.element).find('#propertyBox form[data-property-type="2"]');

                        $(that.element).css('overflow','initial');
                        $(that.element).parents('.layui-layer').css('overflow','auto');

                        if(that.settings.id)
                            that.renderBaseDataPage();

                        that.renderICheckOrSelect($(that.element).find('#propertyBox'));
                        that.renderICheckOrSelect(that._$contentForm);
                        that.bindActionClick();
                        that.controlMousemove();
                        that.clickOutStoreData();
                        that.resizeFun();

                    });

                } else {
                    S_layer.error(response.info);
                }
            });

        }
        ,setBoxHeight : function () {
            var that = this;
            var dialogH = $(that.element).height();
            var contentH = $(that.element).find('#contentBox').height();
            if(dialogH>=contentH+110){
                that._$controlBox.height(dialogH-120);
                that._$propertyForm.parent().height(dialogH-120);

            }else{
                that._$controlBox.height(contentH);
                that._$propertyForm.parent().height(contentH);
            }
        }
        ,resizeFun : function () {
            var that = this;
            that.setBoxHeight();
            $(window).on('resize.m-form-template-settings', function(e){
                //console.log('resize.m-form-template-settings')
                that.setBoxHeight();
            });
            console.log($(that.element))

        }
        //渲染列表内容
        ,renderDialog:function (html,callBack) {
            var that = this;
            if(that.settings.isDialog===true){//以弹窗编辑

                S_layer.dialog({
                    area : ['100%','100%'],
                    content:html,
                    closeBtn:0,
                    fixed:true,
                    scrollbar:false,
                    anim:1,
                    btn:false

                },function(layero,index,dialogEle){//加载html后触发
                    that.settings.isDialog = index;//设置值为index,重新渲染时不重新加载弹窗
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
        ,renderBaseDataPage:function () {
            var that = this;

            if(that._baseData && that._baseData.fieldList && that._baseData.fieldList.length>0){

                var j = -1;
                $.each(that._baseData.fieldList,function (i,item) {

                    if(i==j)
                        return true;

                    if(item.fieldType==4){//时间区间，需要合并一个组件
                        j = i+1;
                    }
                    that.baseDateToConvert(item,that._baseData.fieldList[i+1]);

                    if(item.detailFieldList!=null && item.detailFieldList.length>0){

                        var subJ = -1;
                        $.each(item.detailFieldList,function (subI,subItem) {

                            if(subI==subJ)
                                return true;

                            if(item.fieldType==4){//时间区间，需要合并一个组件
                                subJ = subI+1;
                            }
                            that.baseDateToConvert(subItem,item.detailFieldList[subI+1]);

                        });
                    }

                });
            }
            if(!isNullOrBlank(that._baseData.iconKey)){
                $(that.element).find('.icon-circle-out ').removeClass('active');
                $(that.element).find('.icon-circle-out i[data-icon="'+that._baseData.iconKey+'"]').parents('.icon-circle-out').addClass('active');
            }
        }
        ,baseDateToConvert:function (item,nextItem) {
            var that = this;
            var itemKey = item.id;
            var html = template('m_form_template/m_form_template_item',{type:item.fieldType,itemKey:itemKey});
            if(item.fieldPid!=null){
                that._$contentForm.find('.form-item[data-type="9"] .panel-body form').append(html);
            }else{
                that._$contentForm.append(html);
            }


            var dataItem = {};
            dataItem.dataType = item.fieldType;
            dataItem.itemKey = itemKey;
            dataItem.dateType = item.dateFormatType;
            dataItem.fieldTitle = item.fieldTitle;
            dataItem.fieldTooltip = item.fieldTooltip;
            dataItem.fieldUnit = item.fieldUnit;
            dataItem.requiredType = item.requiredType;
            dataItem.isStatistics = item.isStatistics;

            if(item.fieldType==4){//时间区间，需要合并一个组件

                dataItem.fieldTitle2 = nextItem?nextItem.fieldTitle:'';
                dataItem.fieldTooltip2 = nextItem?nextItem.fieldTooltip:'';

            }else if(item.fieldType==6 || item.fieldType==7 || item.fieldType==8){//下拉列表

                dataItem.optional = item.fieldSelectValueType;
                dataItem.iptOptional = [];
                if(item.fieldSelectedValueList!=null && item.fieldSelectedValueList.length>0){

                    $.each(item.fieldSelectedValueList,function (fi,fitem) {
                        dataItem.iptOptional.push(fitem.selectableName);
                    })
                }

            }else if(item.fieldType==11){//关联审批

                dataItem.approvalAttr = item.fieldSelectValueType;
                if(item.fieldSelectValueType && item.fieldSelectValueType.indexOf(',')>-1)
                    dataItem.approvalAttr = item.fieldSelectValueType.split(',');

            }else if(item.fieldType==12){//关联项目

                dataItem.projectAttr = item.fieldSelectValueType;

            }else if(item.fieldType==9){


            }

            that._formFieldInfo.push(dataItem);
            var $formItem = that._$contentForm.find('.form-item[data-key="'+itemKey+'"]');
            that.bindFormItemClick($formItem);
            $formItem.click();
        }
        //初始化iCheck
        ,renderICheckOrSelect:function ($ele,ifCheckedFun,ifUncheckedFun,ifClickedFun) {


            var ifChecked = function (e) {
                if(ifCheckedFun)
                    ifCheckedFun($(this));
            };
            var ifUnchecked = function (e) {
                if(ifUncheckedFun)
                    ifUncheckedFun($(this));
            };
            var ifClicked = function (e) {
                if(ifClickedFun)
                    ifClickedFun($(this));
            };
            $ele.find('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked).on('ifClicked',ifClicked);
            $ele.find('select').select2({
                tags:false,
                allowClear: false,
                minimumResultsForSearch: -1,
                width:'100%',
                language: "zh-CN"
            });
        }
        //点击文本外预存数据
        ,clickOutStoreData:function () {
            var that = this;
            $(that.element).on('click.m-form-template-settings',function () {
                console.log('click.m-form-template-settings');
                //预存数据
                that.storeFieldData();
            });
        }
        //预存数据
        ,storeFieldData:function () {
            var that = this;
            var formItem = that._$contentForm.find('.form-item');
            if(formItem.length==0)
                return false;
            var data = that._$propertyForm.serializeObject();

            if(!data.hasOwnProperty('itemKey'))
                return false;

            //判断该itemKey的记录是否存在，存在先删除
            if(that._formFieldInfo.length>0){
                var index = null;
                $.each(that._formFieldInfo,function (i,item) {
                    if(data.itemKey==item.itemKey){
                        index = i;
                        return false;
                    }
                });
                if(index!=null)
                    that._formFieldInfo.splice(index,1);
            }
            that._formFieldInfo.push(data);
            console.log(that._formFieldInfo)
        }
        //转换数据格式
        ,convertDataFormat:function ($ele) {
            var that = this;
            var formField = [];
            //获取第一层formItem//$(that.element).find('#contentBox>form>div.form-item')
            $ele.each(function (i) {
                var $this = $(this);
                var type = $this.attr('data-type'),itemKey = $this.attr('data-key');
                var newDataItem = {};
                //从已预存的数据取出
                var dataItem = getObjectInArray(that._formFieldInfo,itemKey,'itemKey');
                if(isNullOrBlank(dataItem))
                    return true;

                newDataItem = {
                    fieldTitle:dataItem.fieldTitle,
                    fieldType:dataItem.dataType,
                    fieldUnit:dataItem.fieldUnit,
                    fieldTooltip:dataItem.fieldTooltip,
                    arrangeType:dataItem.arrangeType,
                    requiredType:dataItem.requiredType,
                    isStatistics:dataItem.isStatistics
                };
                if(!isNullOrBlank(dataItem.isStatistics))
                    that._isStatisticsList.push({itemKey:itemKey,isStatistics:dataItem.isStatistics})

                if(type==4){//时间区间，需要拆成两个组件

                    newDataItem.dateFormatType = dataItem.dateType;
                    newDataItem.seqX = '1';

                    var clone = $.extend(true, {}, newDataItem);
                    clone.fieldTitle = dataItem.fieldTitle2;
                    clone.fieldTooltip = dataItem.fieldTooltip2;
                    clone.seqX = '2';
                    formField.push(filterParam(clone));

                }else if(type==6 || type==7 || type==8){//下拉列表

                    newDataItem.fieldSelectValueType = dataItem.optional;

                    if(dataItem.iptOptional!=null && dataItem.iptOptional.length>0){

                        $.each(dataItem.iptOptional,function (i,item) {

                            if(newDataItem.fieldSelectedValueList==null)
                                newDataItem.fieldSelectedValueList = [];

                            newDataItem.fieldSelectedValueList.push({
                                selectableName:item
                            });
                        })
                    }

                }else if(type==11){//关联审批

                    if(dataItem.approvalAttr!=null && typeof (dataItem.approvalAttr) == 'object'){
                        newDataItem.fieldSelectValueType = dataItem.approvalAttr.join(',');
                    }else{
                        newDataItem.fieldSelectValueType = isNullOrBlank(dataItem.approvalAttr)?'':dataItem.approvalAttr;
                    }
                }else if(type==12){//关联项目

                    newDataItem.fieldSelectValueType = isNullOrBlank(dataItem.projectAttr)?'':dataItem.projectAttr;

                }else if(type==9){

                    //取第二层明细数据
                    newDataItem.detailFieldList = {};
                    newDataItem.detailFieldList = that.convertDataFormat($this.find('.panel .form-item'));
                }

                formField.push(filterParam(newDataItem));
            });

            return formField;
        }
        //整合保存数据
        ,getSaveData:function () {
            var that = this;
            var dataInfo = {};
            that._isStatisticsList = [];
            //表单属性
            dataInfo = $(that.element).find('#propertyBox form[data-property-type="1"]').serializeObject();

            //取第一层数据
            dataInfo.fieldList = that.convertDataFormat($(that.element).find('#contentBox>form>div.form-item'));

            return dataInfo;
        }
        ,save:function (status) {
            var that = this;
            var option = {};
            option.url = restApi.url_saveDynamicForm ;
            option.postData = that.getSaveData();

            option.postData.iconKey = $(that.element).find('.icon-circle-out.active i').attr('data-icon');

            if(that._baseData && that._baseData.id!=null)
                option.postData.id = that._baseData.id;

            if(status)
                option.postData.status = status;

            if(that._isStatisticsList && that._isStatisticsList.length>1){
                S_toastr.warning('数字控件只能一项参与统计！');
                return false;
            }

            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {

                    S_toastr.success('保存成功！');
                    S_layer.close($(that.element),function () {
                        $(document).off('mouseup.m-form-template-settings');
                        $(document).off('mousemove.m-form-template-settings');
                        $('body>.control-clone').remove();
                    });
                    if(that.settings.saveCallBack)
                        that.settings.saveCallBack();

                } else {
                    S_layer.error(response.info);
                }
            });
        }
        //类型 明细，附件，只能放一个，
        ,judgingControlOnlyOne:function (type) {
            var that = this;
            if((type==9||type==10) && that._$contentForm.find('.form-item[data-type="'+type+'"]').length==1)
                return true;

            return false;
        }
        //左边控件拖拽
        ,controlMousemove:function () {
            var that = this;
            var $controlItem = that._$controlBox.children('div');
            $controlItem.each(function () {
                var el = this;
                var $drag = $(this);
                //dragging = true, 记录起始坐标位置，设置鼠标捕获
                //判断如果dragging = true, 则当前坐标位置 - 记录起始坐标位置，绝对定位的元素获得差值
                //dragging = false, 释放鼠标捕获，防止冒泡
                var dragging = false;
                var iX, iY;
                if(document.attachEvent) {//ie的事件监听，拖拽div时禁止选中内容，firefox与chrome已在css中设置过-moz-user-select: none; -webkit-user-select: none;
                    $controlItem[0].attachEvent('onselectstart', function() {
                        return false;
                    });
                }
                $drag.mousemove(function(e) {
                    //console.log('mousemove')
                });
                $drag.mouseout(function(e) {
                    //console.log('mouseout')
                });
                $drag.mouseup(function(e) {
                    var index=$(this).index();
                    //console.log('mouseup')
                });
                $drag.mousedown(function(e) {//鼠标按下，鼠标变移动标志，克隆元素，并确定新克隆元素位置

                    console.log('mousedown');
                    var type = $(e.target).closest('.control-item').attr('data-type');
                    if(isNullOrBlank(type))
                        return false;

                    if(that.judgingControlOnlyOne(type)){
                        S_toastr.warning('该控件目前只开放一个！');
                        stopPropagation(e);
                        return false;
                    }

                    var html = that.renderSelectingControl(type);
                    $(html).addClass('control-clone').appendTo($("body"));
                    that.renderICheckOrSelect($('.control-clone'));

                    $("body").css('cursor','move');
                    e = e || window.event;
                    dragging = true;
                    //按下元素后，计算当前鼠标位置
                    iX = e.clientX - el.offsetLeft;
                    iY = e.clientY - el.offsetTop;
                    //IE下捕捉焦点
                    el.setCapture && el.setCapture();
                    return false;

                });
                $(document).on('mousemove.m-form-template-settings',function (e) {
                    //console.log('document.mousemove')
                    if (dragging) {
                        e = e || window.event;
                        var oX = e.clientX + 1;
                        var oY = e.clientY + 1;

                        if($(".control-clone").length>0)
                        {
                            $(".control-clone").css({"left":oX + "px", "top":oY + "px"});
                            that.makeSpaceToFormItem(oX,oY,$(".control-clone"));
                        }
                        return false;
                    }

                });
                $(document).on('mouseup.m-form-template-settings',function (e) {

                    console.log('document.mouseup')
                    $("body").css('cursor','auto');
                    if($(".control-clone").length>0){
                        //IE下释放焦点
                        el.releaseCapture && el.releaseCapture();

                        var cleft = that._$contentForm.offset().left-50;
                        var eleft = $(".control-clone").offset().left;
                        if(eleft>=cleft){
                            var itemKey = $(".control-clone").attr('data-key');
                            var dataType = $(".control-clone").attr('data-type');
                            if( that._$contentForm.find('.form-item.m-b-space').length>0){

                                var ele = $(".control-clone").clone().removeClass('control-clone').removeAttr('style');
                                if(dataType==10 && that._$contentForm.find('.form-item.m-b-space').closest('.panel').length>0){
                                    S_toastr.warning('该控件暂不放在明细里！');
                                }else{
                                    that._$contentForm.find('.form-item.m-b-space').after(ele.prop('outerHTML'));
                                }


                            }else if(that._$contentForm.find('h4.m-b-space').length>0){

                                var ele = $(".control-clone").clone().removeClass('control-clone').removeAttr('style');

                                if(dataType==10 && that._$contentForm.find('h4.m-b-space').closest('.panel').length>0){
                                    S_toastr.warning('该控件暂不放在明细里！');
                                }else{
                                    that._$contentForm.find('h4.m-b-space').after(ele.prop('outerHTML'));
                                }


                            }else if(that._$contentForm.find('.form-item.m-t-space').length>0){

                                var ele = $(".control-clone").clone().removeClass('control-clone').removeAttr('style');

                                if(dataType==10 && that._$contentForm.find('.form-item.m-t-space').closest('.panel').length>0){
                                    S_toastr.warning('该控件暂不放在明细里！');
                                }else{
                                    that._$contentForm.find('.form-item.m-t-space').before(ele.prop('outerHTML'));
                                }

                            }else{
                                $(".control-clone").clone().removeClass('control-clone').removeAttr('style').appendTo(that._$contentForm);
                            }
                            that.bindFormItemClick(that._$contentForm.find('.form-item[data-key="'+itemKey+'"]'));
                            that._$contentForm.find('.form-item[data-key="'+itemKey+'"]').click();
                        }
                        that._$contentForm.find('.form-item').removeClass('m-b-space').removeClass('m-t-space');
                        that._$contentForm.find('.panel h4').removeClass('m-b-space');
                        $('body>.control-clone').remove();
                    }
                    dragging = false;
                    //阻止冒泡
                    stopPropagation(e);
                });
            });
        }
        //渲染右边属性面板
        ,renderProperty:function () {
            var that = this;

            var $activeFormItem = that._$contentForm.find('.form-item.active');
            if($activeFormItem.length>1){//多个，明细控件存在
                $activeFormItem = $activeFormItem.eq(1);//取第二个
            }
            var type = $activeFormItem.attr('data-type');
            //获取选中的key
            var itemKey = $activeFormItem.attr('data-key');

            var formFieldInfo = {};
            if(that._formFieldInfo.length>0){
                formFieldInfo = getObjectInArray(that._formFieldInfo,itemKey,'itemKey');
                if(isNullOrBlank(formFieldInfo))
                    formFieldInfo = {};

                if(formFieldInfo.approvalAttr && formFieldInfo.approvalAttr.length>1){
                    formFieldInfo.approvalAttr = formFieldInfo.approvalAttr.join(',');
                }
            }
            if($activeFormItem.closest('.panel').length>0)
                formFieldInfo.isShowStatistics = 1;
            
            var html = template('m_form_template/m_form_template_item_property',{type:type,itemKey:itemKey,formFieldInfo:formFieldInfo});
            that._$propertyForm.html(html);

            if(type==9 || type==10 || type==14)
                return false;

            that.renderICheckOrSelect(that._$propertyForm,function ($this) {//选中事件

            },function ($this) {//未选中事件

            },function ($this) {//点击事件

                if(type==4 && $this.attr('name')=='dateType'){

                    var radioValue =  $this.val();
                    var $time = that._$contentForm.find('.form-item[data-key="'+itemKey+'"]').find('.time-box');

                    if(radioValue==2){

                        $time.each(function () {
                            $(this).removeClass('col-24-xs-24').addClass('col-24-xs-10');
                            $(this).parent().find('.time-hh').show();
                            $(this).parent().find('.time-mm').show();
                            $(this).parent().find('.am-pm').hide();
                        });


                    }else if(radioValue==3){

                        $time.each(function () {
                            $(this).removeClass('col-24-xs-24').addClass('col-24-xs-10');
                            $(this).parent().find('.time-hh').hide();
                            $(this).parent().find('.time-mm').hide();
                            $(this).parent().find('.am-pm').show();

                        });

                    }else{
                        $time.each(function () {
                            $(this).removeClass('col-24-xs-10').addClass('col-24-xs-24');
                            $(this).parent().find('.time-hh').hide();
                            $(this).parent().find('.time-mm').hide();
                            $(this).parent().find('.am-pm').hide();

                        });

                    }

                }
                //下拉列表
                if(type==6 && $this.attr('name')=='optional'){
                    var radioValue =  $this.val();
                    if(radioValue=='0'){
                        that._$propertyForm.find('.select-item').show();
                        delClick();
                        addSelectItem();
                    }else{
                        that._$propertyForm.find('.select-item').hide();
                    }
                }
            });

            var delClick = function(){
                that._$propertyForm.find('.row button[data-action="delSelectItem"]').off('click').on('click',function () {
                    $(this).parents('.row[data-type="optional"]').remove();
                    delItem();
                    return false;
                });
            };

            var delItem = function () {
                that._$propertyForm.find('.row[data-type="optional"] input').each(function (i) {
                    $(this).attr('placeholder','选项'+(i+1));
                });
            };
            var addSelectItem = function () {
                that._$propertyForm.find('button[data-action="addSelectItem"]').on('click',function () {

                    if($(this).parents('.form-group').find('.select-item').length>0){
                        $(this).next().clone().appendTo($(this).parents('.form-group').find('.select-item'));
                    }else{
                        $(this).next().clone().appendTo($(this).parents('.form-group'));
                    }

                    $(this).parents('.form-group').find('.row[data-type="optional"]:last input').val('');
                    $(this).parents('.form-group').find('.row[data-type="optional"]:last .col-xs-1').removeAttr('style');
                    delItem();
                    delClick();
                    return false;
                });
            };

            //单选框和复选框
            if(type==7 || type==8){
                delClick();
                addSelectItem();
            }

        }
        //渲染中间正在选择控件面板（点击或拖拽）
        ,renderSelectingControl:function (type) {
            var that = this;

            var itemKey = UUID.genV4().hexNoDelim;//生成key，对应选择的控件，控件属性
            var html = template('m_form_template/m_form_template_item',{type:type,itemKey:itemKey});
            //$(that.element).find('#contentBox form.content-form').append(html);
            return html;
        }
        //计算form-item位置，拖拽到contentForm，form-item中，留空效果
        ,makeSpaceToFormItem:function (x,y,$clone) {
            var that = this;
            var $ele = null;
            that._$contentForm.find('.form-item').each(function (i) {
                var top = $(this).offset().top;
                var left = $(this).offset().left-50;
                var height = $(this).height();
                var width = $(this).width()+50;
                var type = $(this).attr('data-type');

                if(type==9 && $(this).find('.panel').length>0 && $(this).find('.panel .form-item').length==0 && x>left && x<left+width && y>(top+(height/3)) && y<(top+height)){

                    that._$contentForm.find('.form-item').removeClass('m-b-space').removeClass('m-t-space');
                    that._$contentForm.find('.panel h4').removeClass('m-b-space');
                    $(this).find('.panel h4').addClass('m-b-space');

                }else if(x>left && x<left+width && y>top-40 && y< (top+(height/2)) &&
                     ($(this).attr('data-key')==that._$contentForm.find('.form-item').eq(0).attr('data-key') || $(this).attr('data-key')==that._$contentForm.find('.form-item .panel .form-item').eq(0).attr('data-key'))){

                    that._$contentForm.find('.form-item').removeClass('m-b-space').removeClass('m-t-space');
                    that._$contentForm.find('.panel h4').removeClass('m-b-space');
                    $(this).addClass('m-t-space');
                }else if(x>left && x<left+width && y>(top+(height/3)) && y<(top+height+50)) {
                    that._$contentForm.find('.form-item').removeClass('m-b-space').removeClass('m-t-space');
                    that._$contentForm.find('.panel h4').removeClass('m-b-space');
                    $(this).addClass('m-b-space');
                }
            });
            return $ele;
        }
        //已选控件点击事件
        ,bindFormItemClick:function ($formItem) {
            var that = this;
            //选择后的控件点击事件
            $formItem.on('click',function (e) {
                //先预存数据
                that.storeFieldData();
                //添加点击样式
                that._$contentForm.find('.form-item').removeClass('active');
                $(this).addClass('active');
                if($(this).parents('.form-item').length>0)
                    $(this).parents('.form-item').addClass('active');

                //渲染右边控件属性
                that.renderProperty();

                stopPropagation(e);
                return false;

            });
            console.log($formItem.find('button[data-action="delItem"]').length)
            $formItem.find('button[data-action="delItem"]').on('click',function () {
                $(this).parent('.form-item').remove();
            });
            $formItem.hover(function () {
                $formItem.find('button[data-action="delItem"]').show();
            },function () {
                $formItem.find('button[data-action="delItem"]').hide();
            });


        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;

            //左边控件点击事件
            $(that.element).find('#controlBox').children().off('click').on('click',function (e) {

                var $this = $(this),type = $this.attr('data-type');

                if(that.judgingControlOnlyOne(type)){

                    //判断提示是否已有，mousedown原因，避免重复提示
                    var isExist = false;
                    $('#toast-container .toast-message').each(function () {
                        if($.trim($(this).text()) == '该控件目前只开放一个！'){
                            isExist = true;
                            return false;
                        }
                    });
                    if(isExist)
                        return false;
                    S_toastr.warning('该控件目前只开放一个！');
                    return false;
                }

                var html = that.renderSelectingControl(type);
                var itemKey = $(html).attr('data-key');

                //当前选中是明细，且已出现明细面板,追加到明细里
                var $activeFormItem = that._$contentForm.find('.form-item.active[data-type="9"]');
                if($activeFormItem.length==1 && $activeFormItem.find('.panel').length>0 && type!=10){
                    $activeFormItem.find('.panel form').append(html);
                }else{
                    $(that.element).find('#contentBox form.content-form').append(html);
                }
                var $formItem = that._$contentForm.find('.form-item[data-key="'+itemKey+'"]');
                that.bindFormItemClick($formItem);
                that.renderICheckOrSelect($formItem);
                $formItem.click();
                that.setBoxHeight();
                stopPropagation(e);
                return false;

            });
            $(that.element).find('.secondary-menu-ul li a').off('click').on('click',function () {
                var type = $(this).attr('type');
                $(this).parents('li').addClass('active').siblings().removeClass('active');
                var $propertyForm = $(that.element).find('form[data-property-type="'+type+'"]');
                $propertyForm.show().siblings('form').hide();

                var itemKey = $propertyForm.children().attr('data-key');

                if(type==2){

                    //没控件，提示
                    if(that._$contentForm.find('.form-item').length==0){
                        that._$propertyForm.html('<div>请选择控件</div>');
                    }
                    //已有控件，没选中，当前属性面板匹配选中控件并添加选中样式
                    if(that._$contentForm.find('.form-item').length>0 && that._$contentForm.find('.form-item.active')==0){
                        that._$contentForm.find('.form-item[data-key="'+itemKey+'"]').addClass('active').siblings().removeClass('active');
                    }
                    //已有控件，已选中，当前选中控件匹配属性面板,若对不上，则重新加载属性面板
                    if(that._$contentForm.find('.form-item').length>0 &&  that._$contentForm.find('.form-item.active')>=0 && that._$contentForm.find('.form-item.active').attr('data-key')!=itemKey){
                        that.renderProperty();
                    }
                }
            });
            $(that.element).find('.icon-circle').on('click',function () {
                $(that.element).find('.icon-circle-out').removeClass('active');
                $(this).parent().addClass('active');
            });
            $(that.element).find('input[name="formName"]').on('keyup',function () {
                $(that.element).find('h4#formName').html($(this).val());
            });

            $(that.element).find('a[data-action],button[data-action]').on('click',function () {
               var $this = $(this);
               var dataAction = $this.attr('data-action');
               switch (dataAction){
                   case 'close'://关闭
                       S_layer.close($this,function () {
                           $(document).off('mouseup.m-form-template-settings');
                           $(document).off('mousemove.m-form-template-settings');
                           $('body>.control-clone').remove();
                       });
                       break;
                   case 'formPreview'://预览
                        //先预存数据
                        that.storeFieldData();
                        var dataInfo = that.getSaveData();
                        dataInfo.formGroupList = that._baseData.formGroupList;//分组基础数据
                        $('body').m_form_template_preview({
                            dataInfo:dataInfo
                        },true);

                       break;
                   case 'save'://保存

                       //先预存数据
                       that.storeFieldData();
                       if(that._$contentForm.find('.form-item').length==0){
                           S_toastr.warning('请选择控件！');
                           return false;
                       }

                       if(that.save()===false)
                           return false;

                       break;
                   case 'saveAndEnable'://保存并启用

                       //先预存数据
                       that.storeFieldData();
                       if(that._$contentForm.find('.form-item').length==0){
                           S_toastr.warning('请选择控件！');
                           return false;
                       }
                       that.save(1);
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
