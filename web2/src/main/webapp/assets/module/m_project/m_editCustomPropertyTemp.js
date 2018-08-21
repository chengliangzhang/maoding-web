/**
 * 基本信息－自定义属性模板
 * Created by wrb on 2017/08/15.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_editCustomPropertyTemp",
        defaults = {
            $title:null,
            $isDialog:true,
            $projectId:null,
            $okCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._currentCompanyId = window.currentCompanyId;
        this._currentUserId = window.currentUserId;
        this._projectPropertyData = null;//加载的数据
        this._projectPropertyEditData = null;//改动后的数据
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData();
        },
        //初始化数据
        initHtmlData:function () {
            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'编辑项信息',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    tPadding: '0px',
                    width: '1000',
                    minHeight:'550',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {

                        /*S_dialog.confirm('当前数据未保存',function () {

                        },function () {
                            
                        })*/
                    },
                    okText:'保存',
                    ok:function () {
                        that.saveProjectCustomFields();

                    }
                },function(d){//加载html后触发

                    that.element = $('div[id="content:'+d.id+'"] .dialogOBox');
                    that.initHtmlTemplate()
                });
            }else{//不以弹窗编辑
                that.initHtmlTemplate();
            }
        }
        //生成html
        ,initHtmlTemplate:function () {
            var that = this;
            that.getCustomProjectPropertyData(function (data) {
                var html = template('m_project/m_editCustomPropertyTemp',{projectPropertyData:data});
                $(that.element).html(html);
                that.renderSelectProperty(data.selectedPropertyList);
                that.renderAddCusProperty(data.customPropertyList,0);
                that.addCustomLibrary();
                that.bindChoseLibrary();
                that.initICheck();
                that.addProperty_validate();
            });
        }
        //获取自定义属性数据
        ,getCustomProjectPropertyData:function (callback) {
            var that = this;
            var options = {};
            options.url = restApi.url_loadProjectCustomFields;
            options.postData = {};
            options.postData.companyId = that._currentCompanyId;
            options.postData.projectId = that.settings.$projectId;
            m_ajax.postJson(options, function (response) {
                if (response.code == '0') {
                    that._projectPropertyData = response.data;
                    that._projectPropertyEditData = jQuery.extend(true, {}, that._projectPropertyData);
                    if(callback){
                        return callback(response.data);
                    }
                } else {
                    S_dialog.error(response.info);
                }
            });
        }
        //添加自定义库
        ,addCustomLibrary:function () {
            var that = this;
            $(that.element).find('button[data-action="addPropertyBtn"]').on('click',function () {

                if($('form.addPropertyForm').valid()){
                    var fieldName = $(this).parents('.row').find('input[name="fieldName"]').val();
                    var unitName = $(this).parents('.row').find('input[name="unitName"]').val();

                    //追加到customPropertyList
                    if(that._projectPropertyEditData.customPropertyList==null){
                        that._projectPropertyEditData.customPropertyList = [];
                    }
                    that._projectPropertyEditData.customPropertyList.push({
                        changeStatus:1,
                        fieldName:fieldName,
                        fieldValue:null,
                        id:null,
                        sequencing:null,
                        unitName:unitName
                    });
                    that.renderAddCusProperty(that._projectPropertyEditData.customPropertyList,1);
                    //追加到selectedPropertyList
                    if(that._projectPropertyEditData.selectedPropertyList==null){
                        that._projectPropertyEditData.selectedPropertyList = [];
                    }
                    var newObj = {};
                    that.dealSelectedPropertyList(fieldName,unitName);
                    newObj.changeStatus = 1;
                    newObj.fieldName = fieldName;
                    newObj.unitName = unitName;
                    that._projectPropertyEditData.selectedPropertyList.push(newObj);
                    that.renderSelectProperty(that._projectPropertyEditData.selectedPropertyList);//重新渲染右边模板
                    //清空
                    $(this).parents('.row').find('input[name="fieldName"]').val('');
                    $(this).parents('.row').find('input[name="unitName"]').val('');
                }
            });
        }
        //绑定选择模板库
        ,bindChoseLibrary:function () {
            var that = this;
            $(that.element).find('.addPropertyForm ul.dropdown-menu>li>a').off('click').on('click',function () {
                var value = $(this).text();
                $(that.element).find('input[name="unitName"]').val(value);
            });
        }
        //选中项删掉某项
        ,dealSelectedPropertyList:function (fieldName,unitName) {
            var that = this;
            if(that._projectPropertyEditData.selectedPropertyList!=null && that._projectPropertyEditData.selectedPropertyList.length>0){
                $.each(that._projectPropertyEditData.selectedPropertyList,function(index,item){
                    if(item.fieldName+''+that.conversionUnitFormat(item.unitName)==fieldName+''+that.conversionUnitFormat(unitName)){//已存在已选项中
                        that._projectPropertyEditData.selectedPropertyList.splice(index,1);
                        return false;//跳出循环
                    }
                });
            }
        }
        //自定义项删掉某项
        ,dealCustomPropertyList:function (i) {
            var that = this;
            $.each(that._projectPropertyEditData.customPropertyList,function(index,item){
                if(index==i){
                    that._projectPropertyEditData.customPropertyList.splice(index,1);
                }
            });
        }
        //渲染添加的属性{isLastCheck:1=最后选中，即添加时需要}
        ,renderAddCusProperty:function (cusList,isLastCheck) {
            var that = this;
            $(that.element).find('#customPropertyBox').html(template('m_project/m_editCustomPropertyTempAdd',{customPropertyList:cusList}));

            var ifUnchecked = function (e) {

                var fieldName = $(this).attr('data-field-name');
                var unitName = $(this).attr('data-unit-name');
                that.dealSelectedPropertyList(fieldName,unitName);
                that.renderSelectProperty(that._projectPropertyEditData.selectedPropertyList);//重新渲染右边模板
            };
            var ifChecked = function (e) {
                //追加到selectedPropertyList
                if(that._projectPropertyEditData.selectedPropertyList==null){
                    that._projectPropertyEditData.selectedPropertyList = [];
                }
                var fieldName = $(this).attr('data-field-name');
                var unitName = $(this).attr('data-unit-name');

                that.dealSelectedPropertyList(fieldName,unitName);
                var newObj = {};
                newObj.changeStatus = 1;
                newObj.fieldName = fieldName;
                newObj.unitName = unitName;
                that._projectPropertyEditData.selectedPropertyList.push(newObj);
                that.renderSelectProperty(that._projectPropertyEditData.selectedPropertyList);//重新渲染右边模板
            };
            $(that.element).find('#customPropertyBox .i-checks input[name="cusProjectFieldCk"]').iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_minimal-green'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);

            if(isLastCheck==1){
                $(that.element).find('#customPropertyBox .i-checks input[name="cusProjectFieldCk"]:last').iCheck('check');
            }

            $(that.element).find('#customPropertyBox').find('span.field-name').each(function () {
                singleLimitString($(this).parents('.col-md-6'),$(this),200,20,'top');
            });

            $(that.element).find('#customPropertyBox a[data-action="delCusProperty"]').off().on('click',function (e) {

                var i = $(this).attr('data-index');
                var fieldName = $(this).attr('data-field-name');
                var unitName = $(this).attr('data-unit-name');
                that.dealCustomPropertyList(i);
                that.renderAddCusProperty(that._projectPropertyEditData.customPropertyList,0);//重新渲染左边自定义模板
                that.dealSelectedPropertyList(fieldName,unitName);
                that.renderSelectProperty(that._projectPropertyEditData.selectedPropertyList);//重新渲染右边模板
                e.stopPropagation();
            });
            $(that.element).find('input[name="cusProjectFieldCk"]').each(function () {
                var $this = $(this);
                var fieldName = $this.attr('data-field-name');
                var unitName = $this.attr('data-unit-name');
                if(that._projectPropertyEditData.selectedPropertyList!=null && that._projectPropertyEditData.selectedPropertyList.length>0){
                    $.each(that._projectPropertyEditData.selectedPropertyList,function(index,item){
                        if(item.fieldName+''+that.conversionUnitFormat(item.unitName)==fieldName+''+that.conversionUnitFormat(unitName)){//已存在已选项中
                            $this.prop('checked',true);
                            $this.iCheck('update');
                            return false;
                        }
                    });
                }
            });
        }
        //渲染已选中的属性
        ,renderSelectProperty:function (selectList) {
            var that = this;
            $(that.element).find('#selectPropertyBox').html(template('m_project/m_editCustomPropertyTempSelect',{selectedPropertyList:selectList}));
            $(that.element).find('#selectPropertyBox').find('span.field-name').each(function () {
                singleLimitString($(this).parents('.col-md-6'),$(this),200,20,'bottom');
            });
            $(that.element).find('#selectPropertyBox').find('a[data-action="delSelectedProperty"]').off().on('click',function (e) {

                var fieldName = $(this).attr('data-field-name');
                var unitName = $(this).attr('data-unit-name');
                that.dealSelectedPropertyList(fieldName,unitName);
                that.renderSelectProperty(that._projectPropertyEditData.selectedPropertyList);//重新渲染右边模板

                if($(that.element).find('input[name="projectFieldCk"][data-field-name="'+fieldName+'"][data-unit-name="'+unitName+'"]').length>0){//存在基本模板，uncheck
                    $(that.element).find('input[name="projectFieldCk"][data-field-name="'+fieldName+'"][data-unit-name="'+unitName+'"]').iCheck('uncheck');
                }
                if($(that.element).find('input[name="cusProjectFieldCk"][data-field-name="'+fieldName+'"][data-unit-name="'+unitName+'"]').length>0){//若存在自定义模板,uncheck
                    $(that.element).find('input[name="cusProjectFieldCk"][data-field-name="'+fieldName+'"][data-unit-name="'+unitName+'"]').iCheck('uncheck');
                }
                e.stopPropagation();
            });
            that.bindSortable();
        }
        //初始化icheck
        ,initICheck:function () {
            var that = this;
            var ifAllChecked = function (e) {
                $(that.element).find('input[name="projectFieldCk"]').iCheck('check');

                var baseList = that._projectPropertyEditData.basicPropertyList;
                var selectList = that._projectPropertyEditData.selectedPropertyList;

                if(selectList==null){
                    selectList = [];
                }
                for(var i = 0; i < baseList.length; i++){
                    var isResult = true;
                    if(selectList.length>0){
                        for(var j=0;j<selectList.length;j++){
                            if(selectList[j].fieldName+''+that.conversionUnitFormat(selectList[j].unitName)==baseList[i].fieldName+''+that.conversionUnitFormat(baseList[i].unitName)){
                                isResult = false;
                                break;
                            }
                        }
                    }
                    if(isResult){
                        baseList[i].id = null;
                        selectList.push(baseList[i]);
                    }
                }
                that._projectPropertyEditData.selectedPropertyList = selectList;
                that.renderSelectProperty(selectList);

            };
            var ifAllUnchecked = function (e) {
                $(that.element).find('input[name="projectFieldCk"]').iCheck('uncheck');
            };
            $(that.element).find('input[name="allProjectFieldCK"]').iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_minimal-green'
            }).on('ifUnchecked.s', ifAllUnchecked).on('ifChecked.s', ifAllChecked);
            var ifChecked = function (e) {
                that.dealAllCheck();

                //追加到selectedPropertyList
                if(that._projectPropertyEditData.selectedPropertyList==null){
                    that._projectPropertyEditData.selectedPropertyList = [];
                }
                var fieldName = $(this).attr('data-field-name');
                var unitName = $(this).attr('data-unit-name');

                that.dealSelectedPropertyList(fieldName,unitName);
                var newObj = {};
                newObj.changeStatus = 1;
                newObj.fieldName = fieldName;
                newObj.unitName = unitName;
                that._projectPropertyEditData.selectedPropertyList.push(newObj);
                console.log(that._projectPropertyEditData.selectedPropertyList);
                that.renderSelectProperty(that._projectPropertyEditData.selectedPropertyList);//重新渲染右边模板

            };
            var ifUnchecked = function (e) {
                that.dealAllCheck();
                var fieldName = $(this).attr('data-field-name');
                var unitName = $(this).attr('data-unit-name');
                that.dealSelectedPropertyList(fieldName,unitName);
                that.renderSelectProperty(that._projectPropertyEditData.selectedPropertyList);//重新渲染右边模板
            };
            $(that.element).find('input[name="projectFieldCk"]').iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_minimal-green'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);

            $(that.element).find('input[name="projectFieldCk"]').each(function () {
                var $this = $(this);
                var fieldName = $this.attr('data-field-name'),unitName = $this.attr('data-unit-name');
                if(that._projectPropertyEditData.selectedPropertyList!=null && that._projectPropertyEditData.selectedPropertyList.length>0){
                    $.each(that._projectPropertyEditData.selectedPropertyList,function(index,item){
                        if(item.fieldName+''+that.conversionUnitFormat(item.unitName)==fieldName+''+that.conversionUnitFormat(unitName)){//已存在已选项中
                            $this.prop('checked',true);
                            $this.iCheck('update');
                        }
                    });
                    that.dealAllCheck();
                }
            });

        }
        //当选择标签库时，判断全选checkbox是否该选中
        ,dealAllCheck:function() {
            var that = this;
            var checkedLen = $(that.element).find('input[name="projectFieldCk"]:checked').length;
            var allLen = $(that.element).find('input[name="projectFieldCk"]').length;
            if(checkedLen==allLen){
                $('input[name="allProjectFieldCK"]').prop('checked',true);
            }else{
                $('input[name="allProjectFieldCK"]').prop('checked',false);
            }
            $('input[name="allProjectFieldCK"]').iCheck('update');
        }
        //已选自定义属性排序拖拽
        , bindSortable: function () {
            var that = this;
            var sortable = Sortable.create(document.getElementById('selectPropertyBox'), {
                animation: 200,
                handle: '.property-span',
                sort: true,
                dataIdAttr: 'data-sortId',
                ghostClass: 'my-sortable-ghost',
                chosenClass: 'my-sortable-chosen',
                dragClass: 'my-sortable-drag',
                onAdd: function (evt){ //拖拽时候添加有新的节点的时候发生该事件
                    //console.log('onAdd.foo:', [evt.item, evt.from]);
                },
                onUpdate: function (evt){ //拖拽更新节点位置发生该事件
                    //console.log('onUpdate.foo:', [evt.item, evt.from]);
                },
                onRemove: function (evt){ //删除拖拽节点的时候促发该事件
                    //console.log('onRemove.foo:', [evt.item, evt.from]);
                },
                onStart:function(evt){ //开始拖拽出发该函数
                    //console.log('onStart.foo:', [evt.item, evt.from]);
                },
                onSort:function(evt){ //发生排序发生该事件
                    //console.log('onSort.foo:', [evt.item, evt.from]);
                },
                onEnd: function(evt){ //拖拽完毕之后发生该事件
                    //console.log('onEnd.foo:', [evt.item, evt.from]);
                    //console.log(evt)
                    that.sortSelectProperty(evt.oldIndex,evt.newIndex);
                }
            });
        }
        //选中的属性定段进行排行，重新生成新的下标值
        ,sortSelectProperty:function (oldIndex,newIndex) {
            var that = this;
            var list = that._projectPropertyEditData.selectedPropertyList;
            var newList = [];
            for(var i=0;i<list.length;i++){
                if(newIndex>oldIndex){//向后拖拽

                    if(i<oldIndex){
                        newList.push(list[i]);
                    }else if(i>=oldIndex && i<newIndex){
                        newList.push(list[i+1]);
                    }else if(i==newIndex){
                        newList.push(list[oldIndex]);
                    }else{
                        newList.push(list[i]);
                    }
                }else{
                    if(i<newIndex){
                        newList.push(list[i]);
                    }else if(i==newIndex){
                        newList.push(list[oldIndex]);
                    }else if(i>newIndex && i<=oldIndex){
                        newList.push(list[i-1]);
                    }else{
                        newList.push(list[i]);
                    }
                }
            }
            console.log(newList);
            that._projectPropertyEditData.selectedPropertyList = newList;

        }
        //根据fieldName移除某项
        ,delFieldList:function (list,name) {
            if(list!=null){
                $.each(list,function (index,item) {
                    if(item!=undefined && item!=null){
                        if(item.fieldName == name){
                            list.splice(index,1);//存在则删除
                        }
                    }
                })
            }
            return list;
        }
        //保存自定义模板
        ,saveProjectCustomFields:function () {

            var that = this,option  = {};
            option.classId = that.element;
            option.url = restApi.url_saveProjectCustomFields;
            option.postData = {};
            option.postData.projectId = that.settings.$projectId;
            option.postData.companyId = that._currentCompanyId;
            option.postData.operatorId = that._currentUserId;

            var selectedPropertyList =  that._projectPropertyEditData.selectedPropertyList;
            var oldSelectedPropertyList = that._projectPropertyData.selectedPropertyList;
            var delSelectedPropertyList = that._projectPropertyData.selectedPropertyList;

            if(selectedPropertyList==null)
                selectedPropertyList = [];
            if(oldSelectedPropertyList==null)
                oldSelectedPropertyList = [];
            if(delSelectedPropertyList==null)
                delSelectedPropertyList = [];

            if(oldSelectedPropertyList.length>0 && selectedPropertyList.length>0){//对比数据

                for(var i=0;i<selectedPropertyList.length;i++){

                    selectedPropertyList[i].sequencing = i;//排序值
                    var newItem = jQuery.extend(true, {}, selectedPropertyList[i]);
                    for(var j=0;j<oldSelectedPropertyList.length;j++){
                        if(newItem.fieldName+''+that.conversionUnitFormat(newItem.unitName) == oldSelectedPropertyList[j].fieldName+''+ that.conversionUnitFormat(oldSelectedPropertyList[j].unitName)){//相同，copy旧对象，替换sequencing，fieldName,unitName
                            selectedPropertyList[i] = jQuery.extend(true, {}, oldSelectedPropertyList[j]);
                            selectedPropertyList[i].changeStatus = 2;//当存在库里则update
                            selectedPropertyList[i].sequencing = i;
                            selectedPropertyList[i].fieldName = newItem.fieldName;
                            selectedPropertyList[i].unitName = newItem.unitName;
                            delSelectedPropertyList = that.delFieldList(delSelectedPropertyList,newItem.fieldName);//存在则删除
                            break;
                        }
                    }
                }
            }
            if(delSelectedPropertyList.length>0){
                $.each(delSelectedPropertyList,function(index,item){
                    item.changeStatus=-1;//状态设为删除,追加到selectedPropertyList上
                    selectedPropertyList.push(item);
                });
            }
            var customPropertyList = that._projectPropertyEditData.customPropertyList;
            var oldCustomPropertyList = that._projectPropertyData.customPropertyList;
            var delCustomPropertyList = that._projectPropertyData.customPropertyList;
            if(customPropertyList==null)
                customPropertyList = [];
            if(oldCustomPropertyList==null)
                oldCustomPropertyList = [];
            if(delCustomPropertyList==null)
                delCustomPropertyList = [];

            if(oldCustomPropertyList.length>0 && customPropertyList.length>0){//对比数据

                for(var m=0;m<customPropertyList.length;m++){

                    customPropertyList[m].sequencing = m;//排序值
                    var newItem = jQuery.extend(true, {}, customPropertyList[m]);
                    for(var n=0;n<oldCustomPropertyList.length;n++){
                        if(newItem.fieldName+''+that.conversionUnitFormat(newItem.unitName) == oldCustomPropertyList[n].fieldName+''+ that.conversionUnitFormat(oldCustomPropertyList[n].unitName)){//相同，copy旧对象，替换sequencing，fieldName,unitName
                            customPropertyList[m] = jQuery.extend(true, {}, oldCustomPropertyList[n]);
                            customPropertyList[m].changeStatus = 2;//当存在库里则update
                            customPropertyList[m].sequencing = m;
                            customPropertyList[m].fieldName = newItem.fieldName;
                            customPropertyList[m].unitName = newItem.unitName;
                            delCustomPropertyList = that.delFieldList(delCustomPropertyList,newItem.fieldName);//存在则删除
                            break;
                        }
                    }
                }
            }
            if(delCustomPropertyList.length>0){
                $.each(delCustomPropertyList,function(index,item){
                    item.changeStatus=-1;//状态设为删除,追加到selectedPropertyList上
                    customPropertyList.push(item);
                });
            }
            option.postData.basicPropertyList = that._projectPropertyEditData.basicPropertyList;
            option.postData.customPropertyList = customPropertyList;
            option.postData.selectedPropertyList = selectedPropertyList;
            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    if(that.settings.$okCallBack!=null){
                        return that.settings.$okCallBack(null);
                    }
                }else {
                    S_dialog.error(response.info);
                }
            })
        }
        //转换单位格式
        , conversionUnitFormat:function (unitName) {

            var that = this,_unitName = '';
            switch(unitName){
                case 'm&sup2;':
                    _unitName = 'm²';
                    break;
                case 'm&sup3;':
                    _unitName = 'm³';
                    break;
                default:
                    _unitName = unitName;
                    break;
            }
            return _unitName;
        }
        //添加自定义属性验证
        , addProperty_validate: function () {
            var that = this;
            $('form.addPropertyForm').validate({
                rules: {
                    fieldName: {
                        required: true,
                        isReName: true
                    }
                },
                messages: {
                    fieldName: {
                        required: '标签名称不可为空！',
                        isReName: "该标签库已存在，请勿重复添加"

                    }
                },
                errorPlacement: function (error, element) { //指定错误信息位置
                    element.closest('.row').find('.col-md-12').html(error);
                }
            });
            // 重名验证
            jQuery.validator.addMethod("isReName", function (value, element) {

                value = $.trim(value);
                var unitName = $(element).parents('.addPropertyForm').find('input[name="unitName"]').val();
                var isOk = true;
                if(that._projectPropertyData!=null && that._projectPropertyData.basicPropertyList!=null && that._projectPropertyData.basicPropertyList.length>0){
                    var baseList = that._projectPropertyData.basicPropertyList;
                    for(var i=0;i<baseList.length;i++){
                        if(value+'' + unitName == baseList[i].fieldName+''+ that.conversionUnitFormat(baseList[i].unitName)){
                            isOk = false;
                            break;
                        }
                    }
                }
                if(isOk && that._projectPropertyEditData!=null && that._projectPropertyEditData.customPropertyList!=null && that._projectPropertyEditData.customPropertyList.length>0){
                    var customList = that._projectPropertyEditData.customPropertyList;
                    for(var i=0;i<customList.length;i++){
                        if(value+'' + unitName ==customList[i].fieldName+''+ that.conversionUnitFormat(customList[i].unitName)){
                            isOk = false;
                            break;
                        }
                    }
                }
                return  isOk;

            }, "该标签库已存在，请勿重复添加");

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


