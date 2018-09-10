/**
 * 列表-表头字段选择
 * Created by wrb on 2018/8/22.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_field_settings",
        defaults = {
            isDialog:true,
            dialogMinHeight:null,
            type:0,//标题栏类型：0-我的项目标题栏，1-项目总览标题栏，2-发票汇总标题栏
            saveCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        this._selectedTitleList = [];//当前选中字段列表
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.renderDialog(function () {

                var option  = {};
                option.classId = that.element;
                option.url = restApi.url_listOptionalTitle;
                option.postData = {
                    type:that.settings.type
                };

                m_ajax.postJson(option,function (response) {
                    if(response.code=='0'){

                        that._selectedTitleList = response.data.selectedTitleList;
                        var $data = response.data;
                        if(that.settings.dialogMinHeight)
                            $data.dialogMinHeight = 'min-height:'+that.settings.dialogMinHeight+'px';

                        var html = template('m_field/m_field_settings',$data);
                        $(that.element).html(html);

                        that.initICheck();
                        that.renderSelectedList();
                        that.ckAllByGroup();

                    }else {
                        S_dialog.error(response.info);
                    }
                });
            });
        },
        //初始化数据,生成html
        renderDialog:function (callBack) {

            var that = this;
            if(that.settings.isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.title||'设置显示字段',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '705',
                    minHeight: that.settings.dialogMinHeight || '460',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancel:function () {

                    },
                    ok:function () {
                        that.save();
                    }
                },function(d){//加载html后触发
                    that.element = 'div[id="content:'+d.id+'"] .dialogOBox';
                    if(callBack!=null)
                        callBack();

                });
            }else{//不以弹窗编辑
                if(callBack!=null)
                    callBack();
            }
        }
        //渲染已选列表
        ,renderSelectedList:function () {
            var that = this;
            var html = template('m_field/m_field_settings_selected',{selectedTitleList:that._selectedTitleList});
            $(that.element).find('#selectedFieldBox').html(html);
            that.bindActionClick();
            that.bindSortable();
        }
        //初始化iCheck
        ,initICheck:function () {
            var that = this;
            var ifChecked = function (e) {
                var $this = $(this);
                var code = $this.val();
                if(code==''){//空是全选
                    $this.closest('.field-group').find('input[name="itemCk"][value!=""]:not(:checked)').each(function () {
                        that.pushField($(this));
                    });
                }else{
                    that.pushField($this);
                }
                that.ckAllByGroup();
                //重新渲染已选列表
                that.renderSelectedList();

            };
            var ifUnchecked = function (e) {
                var $this = $(this);
                var code = $this.val();
                if(code==''){//空是全选
                    $this.closest('.field-group').find('input[name="itemCk"][value!=""]:checked:not(:disabled)').each(function () {
                        that.delSelectedField($(this).val());
                    });
                }else{
                    that.delSelectedField(code);
                }
                that.ckAllByGroup();
                //重新渲染已选列表
                that.renderSelectedList();
            };
            $(that.element).find('input[name="itemCk"]').iCheck({
                checkboxClass: 'icheckbox_square-blue',
                radioClass: 'iradio_square-blue'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);
        }
        //that._selectedTitleList.push({})
        ,pushField:function ($this) {
            var that = this;
            that._selectedTitleList.push({
                code:$this.val(),
                name:$this.closest('.i-checks').find('.i-checks-span').text()
            });
            $this.prop('checked',true);
            $this.iCheck('update');
        }
        //已选自定义属性排序拖拽
        , bindSortable: function () {
            var that = this;
            var sortable = Sortable.create(document.getElementById('selectedFieldBox'), {
                animation: 200,
                handle: '.list-group-item',
                sort: true,
                dataIdAttr: 'data-sort-id',
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
                    //console.log(evt);
                    that._selectedTitleList = sortList(evt.oldIndex,evt.newIndex,that._selectedTitleList);
                    console.log(that._selectedTitleList);
                }
            });
        }
        //删除右侧字段
        ,delSelectedField:function (code) {
            var that = this;
            that._selectedTitleList = delItemByList(that._selectedTitleList,'code',code);
            console.log(that._selectedTitleList);
            $(that.element).find('ul#selectedFieldBox li.list-group-item[data-code="'+code+'"]').remove();
            $(that.element).find('input[name="itemCk"][value="'+code+'"]').prop('checked',false);
            $(that.element).find('input[name="itemCk"][value="'+code+'"]').iCheck('update');
            that.ckAllByGroup();
        }
        //全选判断，并赋予选中或未选状态
        ,ckAllByGroup:function () {
            var that = this;
            //循环全选
            $(that.element).find('input[name="itemCk"][value=""]').each(function () {
                var $group = $(this).closest('.field-group');
                var itemCheckedLen = $group.find('input[name="itemCk"][value!=""]:checked').length;
                var itemLen = $group.find('input[name="itemCk"][value!=""]').length;
                if(itemCheckedLen == itemLen){
                    $(this).prop('checked',true);
                    $(this).iCheck('update');
                }else{
                    $(this).prop('checked',false);
                    $(this).iCheck('update');
                }
            });
        }
        ,dealHeight:function () {
            
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('a[data-action]').off('click').on('click',function () {
                var $this = $(this),dataAction = $this.attr('data-action');

                switch (dataAction){
                    case 'delSelectedField'://删除
                        var code = $this.closest('li.list-group-item').attr('data-code');
                        that.delSelectedField(code);
                        break;
                }
            });
        }
        //保存
        ,save:function () {
            var that = this;
            var option = {};
            option.url = restApi.url_changeTitle;

            var codeList = [];
            $(that.element).find('ul#selectedFieldBox li.list-group-item').each(function () {
                codeList.push($(this).attr('data-code'));
            });
            option.postData = {
                type:that.settings.type,
                titleCodeList:codeList
            };
            m_ajax.postJson(option, function (response) {
                if (response.code == '0') {
                    S_toastr.success('保存成功！');

                    if(that.settings.saveCallBack)
                        that.settings.saveCallBack();

                } else {
                    S_dialog.error(response.info);
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
