/**
 * 选择甲方（建设单位）
 * Created by wrb on 2016/12/21.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_choseConstructCompany",
        defaults = {
            $title:null,
            $isDialog:true,
            $okCallBack:null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;

        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that.initHtmlData(function () {
                that.bindActionClick();

            });
        },
        //初始化数据
        initHtmlData:function (callBack) {
            var that = this;
            if(that.settings.$isDialog){//以弹窗编辑
                S_dialog.dialog({
                    title: that.settings.$title||'甲方单位',
                    contentEle: 'dialogOBox',
                    lock: 3,
                    width: '850',
                    minHeight:'200',
                    tPadding: '0px',
                    url: rootPath+'/assets/module/m_common/m_dialog.html',
                    cancelText:'关闭',
                    cancel:function () {

                    },
                    okText:'清除',
                    ok:function () {
                        if(that.settings.$okCallBack!=null){
                            return that.settings.$okCallBack(null);
                        }
                    }
                },function(d){//加载html后触发

                    var classIdObj = $('div[id="content:'+d.id+'"] .dialogOBox');
                    that.initHtmlTemplate(classIdObj)
                    that.getConstructListData(1);
                    if(callBack!=null){
                        callBack();
                    }
                });
            }else{//不以弹窗编辑
                var classIdObj = $(that.element);
                that.initHtmlTemplate(classIdObj);
                if(callBack!=null){
                    callBack();
                }
            }
        }
        //生成html
        ,initHtmlTemplate:function (classIdObj) {
            var that = this;
            var html = template('m_project/m_choseConstructCompany',{});
            classIdObj.html(html);
        }
        //获取甲方单位数据{type=1=常用甲方,type=2=查询甲方}
        ,getConstructListData:function (type) {
            var that = this;
            var option={};
            if(type==1){
                option.url=restApi.url_getUsedPartA;
            }else{
                option.url=restApi.url_getLikedPartA;
                option.postData={keyword:$('.choiseConstructOBox input[name="keyword"]').val()}
            }

            m_ajax.postJson(option,function (response) {
                if(response.code=='0'){
                    that.toGenerateConstructListHtml(response.data);
                }else {
                    S_dialog.error(response.info);
                }

            })
        }
        //生成甲方单位列表html
        ,toGenerateConstructListHtml:function (data) {
            var that = this
            var iHtml = '';
            if(data!=null && data.length>0){
                for(var i=0;i<data.length;i++){
                    iHtml+='<tr class="curp" data-id="'+data[i].id+'" data-name="'+data[i].companyName+'"><td>'+(i+1)+'</td>' +
                        '<td style="word-break: break-all;">'+data[i].companyName+'</td></tr>';
                }
                $('.choiseConstructOBox table.constructList tbody').html(iHtml);
                $('.choiseConstructOBox table.constructList tbody').find('tr').on('click',function () {
                    var $data={};
                    $data.id=$(this).attr('data-id');
                    $data.companyName=$(this).attr('data-name');
                    S_dialog.close($('.choiseConstructOBox'));
                    if(that.settings.$okCallBack!=null){
                        return that.settings.$okCallBack($data);
                    }
                });
            }
            return false;
        }
        //按钮事件绑定
        ,bindActionClick:function () {
            var that = this;
            $('.choiseConstructOBox').find('input[name="choiseConstruct"],button[data-action]').on('click',function () {
                var dataAction = $(this).attr('data-action');
                if(dataAction=='choiseCommonAConstruct'){//常用甲方
                    that.getConstructListData(1);
                    $('.choiseConstructOBox .searchBox').addClass('hide');
                }else if(dataAction=='searchAConstruct' || dataAction=='toSearchAConstruct'){//查询甲方
                    that.getConstructListData(2);
                    $('.choiseConstructOBox .searchBox').removeClass('hide');
                }
            });
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


