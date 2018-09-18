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

            //判断当前用户有没有权限操作
            if(that.settings.isEdit){
                that.bindEditable();
            }else{
                $(that.element).find('a[data-action]').removeClass('editable-click').addClass('not-role');
            }

        }
        //在位编辑内容初始化
        ,bindEditable:function(){
            var that = this;
            $(that.element).find('a[data-action]').each(function () {
                var $this = $(this);
                var key = $this.attr('data-key');
                var value = $.trim($this.text());

                var dataInfo = null;

                if(key=='serverType' && that.settings.serverTypeList!=null && that.settings.serverTypeList.length>0){//服务类型
                    dataInfo = [];

                    $.each(that.settings.serverTypeList,function (i,item) {
                        var isSelected = false;
                        if(!isNullOrBlank(that._teamInfo.serverType))
                            isSelected = that._teamInfo.serverType.indexOf(item.id)>-1?true:false;

                        dataInfo.push({
                            id:item.id,
                            name:item.name,
                            isSelected:isSelected
                        });
                    });
                }
                else if(key=='address'){//地址
                    dataInfo = {};
                    dataInfo.province = that._teamInfo.province;
                    dataInfo.city = that._teamInfo.city;
                    dataInfo.county = that._teamInfo.county;
                    dataInfo.address = that._teamInfo.companyAddress;
                }

                $this.m_editable({
                    inline:true,
                    hideElement:true,
                    value:value,
                    dataInfo:dataInfo,
                    ok:function (data) {
                        console.log(data);
                        if(key=='serverType'){
                            if(data.serverType!=null && typeof (data.serverType) == 'object'){
                                data.serverType = data.serverType.join(',');
                            }else{
                                data.serverType = isNullOrBlank(data.serverType)?'':data.serverType;
                            }
                        }
                        else if(key=='address'){
                            data.companyAddress = data.address;
                            data.address = undefined;
                            data.city = data.city==undefined?'':data.city;
                            data.county = data.county==undefined?'':data.county;
                            console.log(data);
                        }
                        that.saveTeamInfo(data,key);
                    },
                    cancel:function () {

                    }
                },true);
            });
        },
        //保存组织信息时调用的方法
        saveTeamInfo:function(value,name,callback){
            var that = this;
            var options = {};
            options.url = restApi.url_saveOrUpdateCompany;
            options.classId = '#content-right';
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
                    if(name=='companyName'){
                        $('#navbar a.orgInfo').html(value+'<span class="caret"></span>')
                    }

                    that.settings.teamInfo = response.data;
                    that.initMainContent();

                    if(that.settings.saveCallBack)
                        that.settings.saveCallBack();
                }else {
                    S_layer.error(response.info);
                }
            });
        }
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
