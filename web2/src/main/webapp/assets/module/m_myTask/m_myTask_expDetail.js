/**
 * Created by wrb on 2016/12/7.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_myTask_expDetail",
        defaults = {
            expDetail:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this._versionNum = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.getExpData();
        }
        //加载弹窗
        ,renderDialog: function (html,callBack) {
            var that = this;
            S_layer.dialog({
                title: '报销明细',
                area : '750px',
                content:html,
                btn:false

            },function(layero,index,dialogEle){//加载html后触发
                that.element = dialogEle;
                if(callBack)
                    callBack();
            });


        }
        //加载
        ,getExpData:function(){
            var that = this;
            var option = {};
            option.url=restApi.url_getExpMainDetail+'/'+that.settings.expDetail.id;
            m_ajax.post(option,function (response) {
                if(response.code=='0'){

                    that._versionNum = response.data.versionNum;

                    var $data={};
                    $data.myExpDetails = response.data;
                    $data.expNo = response.data.expNo;
                    $data.fastdfsUrl = window.fastdfsUrl;
                    $.each($data.myExpDetails.auditList,function(i,item){
                        if(item.expDate!=null&&item.expDate!=''){
                            item.expDate=moment(item.expDate).format('YYYY-MM-DD');
                        }
                    });
                    $.each($data.myExpDetails.detailList,function(i,item){
                        item.expAmount = expNumberFilter(item.expAmount);
                    });
                    $data.myExpDetails.totalExpAmount = expNumberFilter($data.myExpDetails.totalExpAmount);
                    var html = template('m_myTask/m_myTask_expDetail',$data);
                    that.renderDialog(html,function () {
                        that.bindActionClick();
                    });
                }else {
                    S_layer.error(response.info);
                }

            })
        }
        //事件绑定
        ,bindActionClick:function () {

            var that = this;
            $(that.element).find('a[data-action]').on('click',function (event) {
                var $this = $(this);
                var dataAction = $this.attr('data-action');

                switch (dataAction){
                    case 'cancel':
                        S_layer.close($this);
                        break;
                    case 'agreeAndDone':
                        var options = {};
                        var id = that.settings.expDetail.id;
                        var versionNum = that.settings.myExamineData.versionNum;
                        options.postData={id:id,versionNum:versionNum};
                        options.url=restApi.url_agreeExpMain+'/'+id+'/'+versionNum;
                        S_layer.close($this);
                        m_ajax.postJson(options,function (response) {
                            if(response.code=='0'){
                                S_layer.close($(event));
                                S_toastr.success('操作成功');
                                that.refreshMyChecking();
                            }else {
                                S_layer.error(response.info);
                            }

                        });
                        break;
                    case 'agreeAndToNext':

                        var options = {};
                        options.url = restApi.url_getOrgTree;
                        S_layer.close($this);
                        options.selectUserCallback = function (data,event) {

                            var options = {};
                            var id = that.settings.expDetail.id;
                            var versionNum = that._versionNum;
                            options.url=restApi.url_agreeAndTransAuditPerExpMain+'/'+id+'/'+data.companyUserId+'/'+versionNum;
                            m_ajax.postJson(options,function (response) {
                                if(response.code=='0'){
                                    S_layer.close($(event));
                                    S_toastr.success('操作成功');
                                    that.refreshMyChecking();
                                }else {
                                    S_layer.error(response.info);
                                }
                            });
                        };

                        $('body').m_orgByTree(options);
                        break;
                }

            });
        }
        //加载我任务的页面
        , refreshMyChecking: function () {
            $('ul.metismenu li a[id="myTask"]').click();
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
