/**
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_teamInfoShow",
        defaults = {
            teamInfo:null,
            serverTypeList:null,
            isEdit:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._serverTypeList = null;//用于适应x-editable插入数据的服务类型列表
        this._serverTypeIndex = null;//用于已选的服务类型的index集合，是string类型
        this._roleCode = null;//当前用户的权限集
        this._teamInfo = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._roleCode = window.currentRoleCodes;
            that.initMainContent();
        }
        //加载主要页面
        ,initMainContent:function(){
            var that = this;
            var $data = {};
            $data.teamInfo = that.settings.teamInfo;
            that._teamInfo = that.settings.teamInfo;
            that._serverTypeIndex = that.settings.teamInfo.serverType;
            $data.serverTypeList = that.settings.serverTypeList;
            var html = template('m_teamInfo/m_teamInfoShow',$data);
            $(that.element).html(html);

            // rolesControl();
            that.dealServerTypeList();
            //判断当前用户有没有权限操作
            if(that.settings.isEdit){
                that.bindClickFun();
                that.bindEditable();
                that.dealEmptyText();
                that.bindServerType();
            }else{
                $(that.element).find('a[data-action]').addClass('notRole');
            }


        }
        //绑定点击事件
        ,bindClickFun:function(){
            var that = this;
            $(that.element).find('a[data-action]').on('click',function(event){
                var action = $(this).attr('data-action');
                var $this = $(this);
                if(action == 'edit_address'){
                    var options = {};
                    options.$province = that._teamInfo.province;
                    options.$city = that._teamInfo.city;
                    options.$county = that._teamInfo.county;
                    options.$detailAddress = that._teamInfo.companyAddress;
                    options.$placement = 'right';
                    options.$okCallBack = function (data) {
                        data.companyAddress  = data.detailAddress;
                        delete data.detailAddress;
                        that.saveTeamInfo(null,data,null,function(){
                            that._teamInfo.province = data.province;
                            that._teamInfo.city = data.city;
                            that._teamInfo.county = data.county;
                            that._teamInfo.companyAddress = data.companyAddress;
                            if (data.province != '' || data.city != '' || data.county != '' || data.companyAddress != '') {
                                var html = that._teamInfo.province?that._teamInfo.province+' ':'';
                                html += that._teamInfo.city?that._teamInfo.city+' ':'';
                                html += that._teamInfo.county?that._teamInfo.county+' ':'';
                                html += that._teamInfo.companyAddress?that._teamInfo.companyAddress:'';
                                $this.text(html);
                            }else{
                                $this.text('');
                                that.dealEmptyText();
                            }
                        });
                    };
                    $(this).m_entryAddress(options);
                }
                return false;
            });
        }
        //处理服务类型数据，用于适应x-editable插入数据that.settings.serverTypeList
        ,dealServerTypeList:function(){
            var that = this;
            var $data = {};
            $data.serverTypeList = that.settings.serverTypeList;
            $.each($data.serverTypeList,function(i,item){
                item.text = item.name;
                item.value = i;
            });
            that._serverTypeList = $data.serverTypeList;
        }
        //绑定服务类型点击事件
        ,bindServerType:function(){
            var that = this;
            $(that.element).find('a[data-action="checklist_serverTypeList"]').on('click',function(){
                var options = {};
                var $this = $(this);
                var serverTypeList = that._serverTypeList;
                options.title = "编辑服务类型";
                options.placement = "right";
                options.html = template('m_teamInfo/m_editServerType', {
                    serverTypeList : serverTypeList,
                    serverTypeIndex : that._serverTypeIndex
                });
                options.saveCallback = function () {
                    var newStr = '';
                    $('.serverTypeBox input[name="serverType"]:checked').each(function () {
                        var id = $(this).attr('data-id');
                        newStr += id + ',';
                    });
                    if (newStr.length > 0) that._serverTypeIndex = newStr = newStr.substr(0, newStr.lastIndexOf(','));
                    // if(!newStr || newStr === void 0 || newStr.length<1){
                    //     return '服务类型不能为空！';
                    // }
                    that.saveTeamInfo(null, newStr, 'serverType',function(){
                        that.getNewServerTypeHtml($this,newStr,that._serverTypeList);
                        that.dealEmptyText();
                    });
                };
                options.afterCallback = function () {
                    $('.i-checks').iCheck({
                        checkboxClass: 'icheckbox_square-blue',
                        radioClass: 'iradio_square-blue'
                    });
                };
                $(this).m_defineEditableContent(options);
            });
        }
        //在位编辑内容初始化
        ,bindEditable:function(){
            var that = this;
            var elements = $(that.element).find('div.form-control-static a[data-action]');
            elements.each(function(){
                var $this = $(this),
                    obj = null,
                    action = $this.attr('data-action'),
                    type = action.indexOf("_")>-1?action.split("_")[0]:'text',
                    name = action.indexOf("_")>-1?action.split("_")[1]:'',
                    mode = $this.attr('data-mode')?$this.attr('data-mode'):'inline',
                    isCompanyName = name=="companyName" ? true :false,
                    content = $this.text(),//输入的内容
                    title = $this.parent().prev().text()//输入的内容
                if(type=='text' || type=='textarea'){//主要是除设计范围跟设计阶段以外的信息
                    $this.editable({
                        type : type
                        ,pk: 1
                        ,title: '编辑组织简介'
                        ,name : name
                        ,mode: mode || 'inline'
                        ,placement: 'top'
                        ,placeholder : '请输入'+title
                        ,value :content
                        ,inputclass :'form-control input-large textarea-long'
                        ,success:function(response,newValue){
                            that.saveTeamInfo(response,newValue,name);
                        }
                        ,validate: isCompanyName && function(value) {
                            if(!value || value == '') return '公司名称不能为空！';
                            if(value && value.length>50)return '公司名称不能超过50字符！';
                        }
                    });
                }

                that.dealEmptyText();//当在为编辑的文本显示为empty时，改为“请设置”

            });
            elements.off('click.clearEditable').on('click.clearEditable', function (event) {
                var action = $(this).attr('data-action');
                $(that.element).find('.editable-container').each(function () {
                    if ($(this).parent().find('a').attr('data-action') != action) {
                        $(this).find('button.editable-cancel').click();
                    }
                });
                return false;
            });
        },
        //保存组织信息时调用的方法
        saveTeamInfo:function(res,value,name,callback){
            var that = this;
            var options = {};
            options.url = restApi.url_saveOrUpdateCompany;
            options.postData = that._teamInfo;
            if(typeof value == "object" && !(value.length)){
                $.each(value,function(key,item){
                    options.postData[key] = item;
                });
            }else{
                options.postData[name] = value;
            }
            m_ajax.postJson(options,function (response) {
                if(response.code=='0'){
                    S_toastr.success('保存成功！');
                    if(callback){
                        callback.call(that);
                    }
                    that.dealEmptyText();
                    // $(that.element).m_teamInfoShow({teamInfo:response.data,serverTypeList:that.settings.serverTypeList})
                    if(name=='companyName'){
                        $('#navbar a.orgInfo').html(value+'<span class="caret"></span>')
                    }
                }else {
                    S_dialog.error(response.info);
                }
            });
        },
        //保存服务类型成功时生成新页面
        getNewServerTypeHtml:function($obj,value,source){
            var that = this;
            var html = '';
            if(!value) {
                $obj.empty();
                return;
            }else{
                $.each(source,function(j,obj){
                    if(value!=undefined && value.indexOf(obj.id)>-1){
                        html += '<span class="serverType m-r">'+ obj.text +'</span>';
                    }
                });
            }
            $obj.html(html);
        },
        //当在为编辑的文本显示为empty时，改为“请设置”
        dealEmptyText:function(){
            var that = this;
            $(that.element).find('a.editable-empty, a.editable').each(function(){
                var text = $(this).html();
                $(this).html(( text == 'Empty' || $.trim(text) == '')? '未设置' : text)
                    .css('color',($.trim(text).indexOf('Empty')>-1 || $.trim(text).indexOf('未设置')>-1)? 'rgb(204, 204, 204)' : 'rgb(71, 101, 160)');

            });
        },

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
