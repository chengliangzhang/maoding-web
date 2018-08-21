/**
 * Created by veata on 2016/12/22.
 * it applies in setting Uediter!
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_establishUediter",
        defaults = {
            loadingId:null,
            initialFrameWidth:"100%",
            initialFrameHeight:300
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
            that.getMainContent();

        }
        //加载页面
        ,getMainContent:function(){
            var that = this;
            var html = template('m_notice/m_establishUediter');
            $(that.settings.loadingId).html(html);
            setTimeout(function() {
                that.settingUE('myEditor2');
            },200);
        }
        //建立UE
        ,settingUE:function (editorId) {
            var that = this;
                UE.getEditor(
                    editorId,
                    {
                        toolbars: [
                            [
                                //'source', //源代码
                                //'anchor', //锚点
                                //'undo', //撤销
                                //'redo', //重做
                                'bold', //加粗
                                'italic', //斜体
                                'fontborder', //字符边框
                                'underline', //下划线
                                'strikethrough', //删除线
                                '|',
                                'forecolor', //字体颜色
                                'backcolor', //背景色
                                '|',
                                'fontfamily', //字体
                                'fontsize', //字号
                                'customstyle', //自定义标题
                                '|',
                                'link', //超链接
                                'unlink', //取消链接
                                '|',
                                //'subscript', //下标
                                //'superscript', //上标
                                //'formatmatch', //格式刷
                                //'blockquote', //引用
                                //'pasteplain', //纯文本粘贴模式
                                //'selectall', //全选
                                //'snapscreen', //截图
                                //'print', //打印
                                'horizontal', //分隔线
                                'cleardoc', //清空文档
                                'justifyleft', //居左对齐
                                'justifyright', //居右对齐
                                'justifycenter', //居中对齐
                                'justifyjustify', //两端对齐
                                '|',
                                'insertorderedlist', //有序列表
                                'insertunorderedlist', //无序列表
                                '|',
                                //'removeformat', //清除格式
                                //'time', //时间
                                //'date', //日期
                                //'inserttable', //插入表格
                                //'edittable', //表格属性
                                //'edittd', //单元格属性
                                //'insertrow', //前插入行
                                //'insertcol', //前插入列
                                //'deleterow', //删除行
                                //'deletecol', //删除列
                                //'splittorows', //拆分成行
                                //'splittocols', //拆分成列
                                //'mergeright', //右合并单元格
                                //'mergedown', //下合并单元格
                                //'mergecells', //合并多个单元格
                                //'splittocells', //完全拆分单元格
                                //'deletecaption', //删除表格标题
                                //'deletetable', //删除表格
                                //'insertparagraphbeforetable', //"表格前插入行"
                                //'inserttitle', //插入标题
                                //'insertcode', //代码语言
                                //'insertimage', //多图上传
                                //'emotion', //表情
                                //'spechars', //特殊字符
                                //'searchreplace', //查询替换
                                //'map', //Baidu地图
                                //'gmap', //Google地图
                                //'insertvideo', //视频
                                //'help', //帮助
                                //'directionalityltr', //从左向右输入
                                //'directionalityrtl', //从右向左输入
                                'indent', //首行缩进
                                'rowspacingtop', //段前距
                                'rowspacingbottom', //段后距
                                'lineheight', //行间距
                                'paragraph', //段落格式
                                //'|',
                                // 'simpleupload', //单图上传
                                //'insertframe', //插入Iframe
                                //'imagenone', //默认
                                //'imageleft', //左浮动
                                //'imageright', //右浮动
                                //'attachment', //附件
                                //'imagecenter', //居中
                                //'wordimage', //图片转存
                                //'edittip ', //编辑提示
                                //'autotypeset', //自动排版
                                //'webapp', //百度应用
                                //'touppercase', //字母大写
                                //'tolowercase', //字母小写
                                //'pagebreak', //分页
                                //'background', //背景
                                //'template', //模板
                                //'scrawl', //涂鸦
                                //'music', //音乐
                                //'drafts', // 从草稿箱加载
                                //'charts', // 图表
                                //'|',
                                //'preview', //预览
                                //'fullscreen' //全屏
                            ]
                        ],
                        autoHeightEnabled : false,
                        initialFrameWidth:that.settings.initialFrameWidth, //初始化编辑器宽度,默认500
                        initialFrameHeight:that.settings.initialFrameHeight,  //初始化编辑器高度,默认500
                        autoClearinitialContent: false,//focus时自动清空初始化时的内容
                        //关闭elementPath
                        elementPathEnabled: false
                    }
                );

                //$('.edui-for-dialogbuttondialog').remove();


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
