/*TMODJS:{"version":"1.0.0"}*/
!function () {

    function template (filename, content) {
        return (
            /string|function/.test(typeof content)
            ? compile : renderFile
        )(filename, content);
    };


    var cache = template.cache = {};
    var String = this.String;

    function toString (value, type) {

        if (typeof value !== 'string') {

            type = typeof value;
            if (type === 'number') {
                value += '';
            } else if (type === 'function') {
                value = toString(value.call(value));
            } else {
                value = '';
            }
        }

        return value;

    };


    var escapeMap = {
        "<": "&#60;",
        ">": "&#62;",
        '"': "&#34;",
        "'": "&#39;",
        "&": "&#38;"
    };


    function escapeFn (s) {
        return escapeMap[s];
    }


    function escapeHTML (content) {
        return toString(content)
        .replace(/&(?![\w#]+;)|[<>"']/g, escapeFn);
    };


    var isArray = Array.isArray || function(obj) {
        return ({}).toString.call(obj) === '[object Array]';
    };


    function each (data, callback) {
        if (isArray(data)) {
            for (var i = 0, len = data.length; i < len; i++) {
                callback.call(data, data[i], i, data);
            }
        } else {
            for (i in data) {
                callback.call(data, data[i], i);
            }
        }
    };


    function resolve (from, to) {
        var DOUBLE_DOT_RE = /(\/)[^/]+\1\.\.\1/;
        var dirname = ('./' + from).replace(/[^/]+$/, "");
        var filename = dirname + to;
        filename = filename.replace(/\/\.\//g, "/");
        while (filename.match(DOUBLE_DOT_RE)) {
            filename = filename.replace(DOUBLE_DOT_RE, "/");
        }
        return filename;
    };


    var utils = template.utils = {

        $helpers: {},

        $include: function (filename, data, from) {
            filename = resolve(from, filename);
            return renderFile(filename, data);
        },

        $string: toString,

        $escape: escapeHTML,

        $each: each
        
    };


    var helpers = template.helpers = utils.$helpers;


    function renderFile (filename, data) {
        var fn = template.get(filename) || showDebugInfo({
            filename: filename,
            name: 'Render Error',
            message: 'Template not found'
        });
        return data ? fn(data) : fn; 
    };


    function compile (filename, fn) {

        if (typeof fn === 'string') {
            var string = fn;
            fn = function () {
                return new String(string);
            };
        }

        var render = cache[filename] = function (data) {
            try {
                return new fn(data, filename) + '';
            } catch (e) {
                return showDebugInfo(e)();
            }
        };

        render.prototype = fn.prototype = utils;
        render.toString = function () {
            return fn + '';
        };

        return render;
    };


    function showDebugInfo (e) {

        var type = "{Template Error}";
        var message = e.stack || '';

        if (message) {
            // 利用报错堆栈信息
            message = message.split('\n').slice(0,2).join('\n');
        } else {
            // 调试版本，直接给出模板语句行
            for (var name in e) {
                message += "<" + name + ">\n" + e[name] + "\n\n";
            }  
        }

        return function () {
            if (typeof console === "object") {
                console.error(type + "\n\n" + message);
            }
            return type;
        };
    };


    template.get = function (filename) {
        return cache[filename.replace(/^\.\//, '')];
    };


    template.helper = function (name, helper) {
        helpers[name] = helper;
    };


    if (typeof define === 'function') {define(function() {return template;});} else if (typeof exports !== 'undefined') {module.exports = template;} else {this.template = template;}
    /**
 * Created by Wuwq on 2017/1/12.
 */


/*自动组合rootPath生成完整URL*/
template.helper('_url', function (url) {
    return window.rootPath + url;
});

/*自动组合rootPath生成完整URL*/
template.helper('_fastdfsUrl', function (url) {
    return window.fastdfsUrl + url;
});


/*语言化描述过滤日期*/
template.helper('_filterDateRangeToString', function (filterValue) {
    var split = filterValue.split(',');
    if (split[0] === split[1])
        return split[0];
    else {
        if (isNullOrBlank(split[0]) && !isNullOrBlank(split[1]))
            return split[1] + ' 及以前';
        else if (!isNullOrBlank(split[0]) && isNullOrBlank(split[1]))
            return split[0] + ' 及以后';
        else
            return split[0] + ' ~ ' + split[1];
    }
});

/*Join某个数组的指定字段*/
template.helper('_mapJoin', function (array, field, separator) {
    if (array === undefined || array === null || array.length === 0)
        return '';
    return _.map(array, function (o) {
        return o[field];
    }).join(separator);
});

/*序列化为JSON字符串*/
template.helper('_jsonStringify', function (obj) {
    return JSON.stringify(obj);
});

/*判断是否为空字符串*/
template.helper('_isBlank', function (str) {
    return _.isBlank(str);
});

/*判断字符串是否为undefined、Null或空*/
template.helper('_isNullOrBlank', function (str) {
    return isNullOrBlank(str);
});

/*字符串固定长度，不足位则左边补充指定字符*/
template.helper('_lpad', function (o, len, c) {
    if (len < 2) len = 2;
    return _.lpad(o, len, c);
});

/*生成唯一DOM元素唯一ID*/
template.helper('_uniqueId', function (prefix) {
    return _.uniqueId(prefix);
});

/*截断字符串*/
template.helper('_cutString', function (str, length, suffix) {
    return cutString(str, length, suffix)
});

/*短格式时间
 * 1：00
 * 2：00
 * */
template.helper('_shortTime', function (datetime) {
    return shortTime(datetime);
});

/*格式化日期*/
template.helper('_momentFormat', function (datetime, pattern) {

    if(pattern == "A") {
        var date = new Date(Date.parse(datetime.replace(/-/g, "/")));
        if (date.getHours() <= 12) {
            return date.getFullYear().toString() + "/" + (date.getMonth() + 1).toString() + "/" + date.getDate().toString() + " 上午";

        } else {

            return date.getFullYear().toString() + "/" + (date.getMonth() + 1).toString() + "/" + date.getDate().toString() + " 下午";
        }
    }else if(pattern == "B"){//当YYYY/MM/DD 转为格式YYYY-MM-DD

        return new Date(Date.parse(datetime.replace(/\//g, "-")));

    }else {
        var m = moment(datetime);
        if (m.isValid())
            return moment(datetime).locale("zh-cn").format(pattern);
        return '';
    }

});

/*格式化日期
 * 今天 1：00
 * 昨天 2：00
 * 2017-01-01 2：00
 * */
template.helper('_dateSpecFormat', function (datetime, pattern) {
    return dateSpecFormat(datetime, pattern);
});

/**
 * 两日期天数差值
 */
template.helper('_timeDifference', function (time1,time2) {

    if(time1!=null && time1!='' && time2!=null && time2!=''){
        return diffDays(time1,time2)+1;
    }
    return '';
});

/**
 * 作用：财务相关金额的格式控制
 * 显示：项目相关的金额数字位数与小数点后面数字的控制，如：23456.13显示为23,456.13;且小数位控制在两位内
 */
template.helper('_expNumberFilter', function (value) {
    return expNumberFilter(value);
});
/**Number(-10000023).toFixed(2)
 * 当负数时，转为正数
 */
template.helper('_expPositiveNumberFilter', function (value) {
    value = value+'';
    if(value.indexOf('-')>-1){
        value = value.substring(1,value.length);
    }
    return expNumberFilter(value);
});
/**
 * Number(-10000023).toFixed(2)
 */
template.helper('_expNumberDecimalFilter', function (value) {

    value = Number(value).toFixed(2);
    return expNumberFilter(value);
});

/*格式化日期
 * 今天
 * 昨天
 * 2017-01-01
 * */
template.helper('_dateSpecShortFormat', function (datetime) {
    return dateSpecShortFormat(datetime);
});

template.helper('_ifPresent', function (s, presentVal, elseVal) {
    if (s !== null && s !== void 0)
        return presentVal;
    return elseVal;
});

template.helper('_include',function(tpl,obj){
   return template(tpl, obj);
});

template.helper('_jsonStringify',function(obj){
    return JSON.stringify(obj);
});
/**
 * 截取字符串
 * str:字符串
 * s:以此截取
 * i:下标
 */
template.helper('_subStr', function (str,s,i) {
    if(str!=null && s!=''){
        var strArr = str.split(s);
        return strArr[i];
    }else{
        return '';
    }
});

    /*v:1*/
template('m_alert/m_alert_error',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,id=$data.id,_uniqueId=$helpers._uniqueId,content=$data.content,$out='';$out+='<div data-id="';
$out+=$escape(id||_uniqueId('m_alert_error_'));
$out+='" class="alert alert-danger fade in alert-dismissable"> <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button> ';
$out+=$escape(content);
$out+=' </div>';
return new String($out);
});/*v:1*/
template('m_approval/m_approval_mgt','<div class="ibox"> <div class="ibox-content no-borders"> <h4>审批管理</h4> <p>轻量级审批管理，支持自定义审批流程</p> <div id="approvalManagement"> </div> </div> </div>');/*v:1*/
template('m_approval/m_approval_mgt_content',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,approvalList=$data.approvalList,a=$data.a,i=$data.i,$escape=$utils.$escape,p=$data.p,pi=$data.pi,$out='';$out+='<table class="table table-hover"> <thead> <tr> <th>审批名称</th> <th>说明</th> <th>审批人设置</th> <th>操作</th> </tr> </thead> <tbody> ';
$each(approvalList,function(a,i){
$out+=' <tr> <td colspan="4"> <h5 class="f-s-13">';
$out+=$escape(a.name);
$out+='</h5>  </td> </tr> ';
if(a.processDefineList!=null && a.processDefineList.length>0){
$out+=' ';
$each(a.processDefineList,function(p,pi){
$out+=' <tr> <td> <!--<label class="i-checks"> <input class="ck" name="iCheck';
$out+=$escape(p.id);
$out+='" type="radio" /> </label>--> ';
$out+=$escape(p.name);
$out+=' </td> <td>';
$out+=$escape(p.documentation);
$out+='</td> <td> <a class="btn btn-link text-navy no-padding pull-left" data-action="setProcess" data-key="';
$out+=$escape(p.key);
$out+='" data-type="';
$out+=$escape(p.type);
$out+='">审批人</a> <span class="fc-ccc">（已设置）</span> </td> <td>  </td> </tr> ';
});
$out+=' ';
}
$out+=' ';
});
$out+=' </tbody> </table>';
return new String($out);
});/*v:1*/
template('m_approval/m_approval_mgt_setProcess',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,name=$data.name,fixedProcess=$data.fixedProcess,$each=$utils.$each,f=$data.f,i=$data.i,p=$data.p,pi=$data.pi,u=$data.u,ui=$data.ui,_isNullOrBlank=$helpers._isNullOrBlank,_url=$helpers._url,condProcess=$data.condProcess,$out='';$out+='<div class="ibox m_approval_mgt_setProcess"> <div class="ibox-content no-borders"> <h4>模板管理</h4> <div class="row m-t-sm"> <div class="col-md-2 text-right">模版模型：</div> <div class="col-md-10">';
$out+=$escape(name);
$out+='</div> </div> <div class="row m-t-sm"> <div class="col-md-2 text-right">审批流程：</div> <div class="col-md-10"> <div> <label class="i-checks"> <input class="ck" name="iCheck" type="radio" data-type="1" /> 自由流程 </label> <span class="fc-ccc">（组织成员可自行添加相应的审批人）</span> </div> <div class="m-t-xs"> <label class="i-checks"> <input class="ck" name="iCheck" type="radio" data-type="2" /> 固定流程 </label> <span class="fc-ccc">（组织成员需按照规定好的审批人流程进行审批）</span> </div> <div class="panel panel-default" data-type="2" data-i="0" style="display: none;"> <div class="panel-heading"> 当为“固定流程”时 </div> <div class="panel-body" > ';
if(fixedProcess!=null && fixedProcess.length>0){
$out+=' ';
$each(fixedProcess,function(f,i){
$out+=' ';
if(f.flowTaskList!=null && f.flowTaskList.length>0){
$out+=' ';
$each(f.flowTaskList,function(p,pi){
$out+=' <div class="approver-outbox"> ';
$each(p.candidateUserList,function(u,ui){
$out+=' <div class="approver-box text-center"> <div class="img-circle"> <img alt="image" class="img-circle" src="';
$out+=$escape(_isNullOrBlank(u.imgUrl)?_url('/assets/img/head_default.png'):u.imgUrl);
$out+='" width="50" height="50"> </div> <div class="m-t-xs">';
$out+=$escape(u.name);
$out+='</div> <a href="javascript:void(0);" class="approver-remove" data-action="removeApprover" style="display: none;"><i class="glyphicon glyphicon-remove"></i></a> </div> ';
});
$out+=' </div> ';
if(pi+1 < f.flowTaskList.length ){
$out+=' <div class="arrow-icon p-h-m"> <i class="fa fa-long-arrow-right"></i> </div> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' <div class="approver-box text-center"> <a class="btn btn-default btn-circle " type="button" data-action="addReview"> <i class="fa fa-plus"></i> </a> <div class="m-t-xs">添加</div> </div> </div> <div class="panel-footer"> <p>比如组织报销流程为“酋长 > 部落”，设定固定流程后，组织成员填写审批单时，审批人已经默认设置为“酋长 > 部落”，且成员自己不能修改。 </p> </div> </div> <div class="m-t-xs"> <label class="i-checks"> <input class="ck" name="iCheck" type="radio" data-type="3" /> 分条件设置流程 </label> <span class="fc-ccc">（申请人提交的表单会进入相应审批条件设置的审批流程）</span> <a class="btn btn-link" data-action="setApprovalCondition" data-type="3" style="display: none;">设置审批条件</a> </div> <div id="flowTaskGroupList"> ';
if(condProcess!=null && condProcess.length>0){
$out+=' ';
$each(condProcess,function(f,i){
$out+=' <div class="panel panel-default" data-type="3" data-i="';
$out+=$escape(i);
$out+='"> <div class="panel-heading"> ';
$out+=$escape(f.title);
$out+=' </div> <div class="panel-body"> ';
if(f.flowTaskList!=null && f.flowTaskList.length>0){
$out+=' ';
$each(f.flowTaskList,function(p,pi){
$out+=' <div class="approver-outbox"> ';
$each(p.candidateUserList,function(u,ui){
$out+=' <div class="approver-box text-center"> <div class="img-circle"> <img alt="image" class="img-circle" src="';
$out+=$escape(_isNullOrBlank(u.imgUrl)?_url('/assets/img/head_default.png'):u.imgUrl);
$out+='" width="50" height="50"> </div> <div class="m-t-xs">';
$out+=$escape(u.name);
$out+='</div> <a href="javascript:void(0);" class="approver-remove" data-action="removeApprover" style="display: none;"><i class="glyphicon glyphicon-remove"></i></a> </div> ';
});
$out+=' </div> ';
if(pi+1 < f.flowTaskList.length ){
$out+=' <div class="arrow-icon p-h-m"> <i class="fa fa-long-arrow-right"></i> </div> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' <div class="approver-box text-center"> <a class="btn btn-default btn-circle " type="button" data-action="addReview"> <i class="fa fa-plus"></i> </a> <div class="m-t-xs">添加</div> </div> </div> </div> ';
});
$out+=' ';
}
$out+=' </div> </div> </div> <div class="row"> <div class="col-md-2"></div> <div class="col-md-10"> <a class="btn btn-primary" data-action="save">保存</a> <a class="btn btn-default" data-action="back">返回</a> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_approval/m_approval_mgt_setProcessCondition',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,processData=$data.processData,$out='';$out+='<form class="form-horizontal m-r m-l m_approval_mgt_setProcessCondition "> <div class="form-group"> <h4>';
$out+=$escape(processData.name);
$out+='</h4> <p class="fc-ccc"> 请输入“';
$out+=$escape(processData.name);
$out+='（';
$out+=$escape(processData.unit);
$out+='）”的分隔数字，我们将为您自动生成数值区间做为审批条件： </p> </div> <div class="form-group"> <div class="col-md-2 m-b-xs"> <input class="form-control input-sm" type="text" name="conditionalVal"> </div> <div class="col-md-1 m-b-xs p-xxs text-center"> < </div> <div class="col-md-2 m-b-xs"> <input class="form-control input-sm" type="text" name="conditionalVal"> </div> <div class="col-md-1 m-b-xs"> <a class="btn btn-primary btn-circle " type="button" data-action="addCondition"> <i class="fa fa-plus"></i> </a> </div> </div> <div class="well m-b-none"> <p>例如：</p> <p>1、输入“报销金额”分隔数字</p> <div class="row"> <div class="col-md-2 m-b-xs"> <input class="form-control input-sm" type="text" value="1000" readonly> </div> <div class="col-md-1 m-b-xs p-xxs text-center"> < </div> <div class="col-md-2 m-b-xs"> <input class="form-control input-sm" type="text" value="3000" readonly> </div> </div> <p>2、我们将为您自动生成数值区间做为审批条件</p> <div class="row"> <div class="col-md-12 "> <span class="label label-primary">报销金额 < 1000</span> </div> <div class="col-md-12 m-t-xs"> <span class="label label-white">审批人：张三</span> </div> <div class="col-md-12 m-t-xs"> <span class="label label-primary">1000 < 报销金额 < 3000</span> </div> <div class="col-md-12 m-t-xs"> <span class="label label-white">审批人：李四</span> </div> <div class="col-md-12 m-t-xs"> <span class="label label-white">审批人：王五</span> </div> <div class="col-md-12 m-t-xs"> <span class="label label-primary">报销金额 >= 3000</span> </div> <div class="col-md-12 m-t-xs"> <span class="label label-white">审批人：张三</span> </div> </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_approval/m_approval_mgt_setProcess_approver',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,isAppendArrow=$data.isAppendArrow,$each=$utils.$each,approverList=$data.approverList,p=$data.p,i=$data.i,$escape=$utils.$escape,_url=$helpers._url,$out='';if(isAppendArrow==1){
$out+=' <div class="arrow-icon p-h-m"> <i class="fa fa-long-arrow-right"></i> </div> ';
}
$out+=' ';
$each(approverList,function(p,i){
$out+=' <div class="approver-outbox"> <div class="approver-box text-center"> <div class="img-circle"> <img alt="image" class="img-circle" src="';
$out+=$escape(_url('/assets/img/head_default.png'));
$out+='" width="50" height="50"> </div> <div class="m-t-xs">';
$out+=$escape(p.userName);
$out+='</div> <a href="javascript:void(0);" class="approver-remove" data-action="removeApprover" style="display: none;"><i class="glyphicon glyphicon-remove"></i></a> </div> </div> ';
if((i+1) < approverList.length ){
$out+=' <div class="arrow-icon p-h-m"> <i class="fa fa-long-arrow-right"></i> </div> ';
}
$out+=' ';
});
return new String($out);
});/*v:1*/
template('m_approval/m_approval_mgt_setProcess_flow',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,flowTaskGroupList=$data.flowTaskGroupList,f=$data.f,i=$data.i,$escape=$utils.$escape,$out='';$each(flowTaskGroupList,function(f,i){
$out+=' <div class="panel panel-default" data-type="3" data-i="';
$out+=$escape(i);
$out+='"> <div class="panel-heading"> ';
$out+=$escape(f.title);
$out+=' </div> <div class="panel-body">  <div class="approver-box text-center"> <a class="btn btn-default btn-circle " type="button" data-action="addReview"> <i class="fa fa-plus"></i> </a> <div class="m-t-xs">添加</div> </div> </div> </div> ';
});
return new String($out);
});/*v:1*/
template('m_browser_tips/m_browser_tips',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="m-browser-tips"> <div class="tips-head"> <img src="';
$out+=$escape(_url('/assets/img/logo_blue.png'));
$out+='"> </div> <div class="tips-content"> <p>由于你当前使用的浏览器版本过低，若继续访问可能会导致部分功能展示不正常，我们推荐你使用以下浏览器进行访问。</p> <p style="margin: 50px 0 30px 0;"><a href="javascript:void(0)" data-action="continue">&gt;&gt;&gt;&nbsp;继续访问</a></p> <ul> <li> <img src="';
$out+=$escape(_url('/assets/img/browser/chrome.png'));
$out+='"> <p>Chrome（推荐）</p> </li> <li> <img src="';
$out+=$escape(_url('/assets/img/browser/firefox.png'));
$out+='"> <p>Firefox</p> </li> <li> <img src="';
$out+=$escape(_url('/assets/img/browser/360.png'));
$out+='"> <p>360极速</p> </li> <li> <img src="';
$out+=$escape(_url('/assets/img/browser/ie.png'));
$out+='"> <p>IE 10+</p> </li> </ul> </div> <div> <span style="font-size: 12px;bottom: 10px;right:15px;position: absolute;"><input id="m-browser-never-tips" type="checkbox" />&nbsp;不再提示</span> </div> </div>';
return new String($out);
});/*v:1*/
template('m_common/m_attach',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,netFileId=$data.netFileId,fullPath=$data.fullPath,fileName=$data.fileName,$out='';$out+='<span class="label m-r-xs" style="background: #ecf0f1;padding: 5px 10px;"> <a href="javascript:void(0)" data-action="preview" data-net-file-id="';
$out+=$escape(netFileId);
$out+='" data-src="';
$out+=$escape(fullPath);
$out+='" data-name="';
$out+=$escape(fileName);
$out+='">';
$out+=$escape(fileName);
$out+='</a> <a class="curp m-l-xs" href="javascript:void(0)" data-action="deleteAttach" data-net-file-id="';
$out+=$escape(netFileId);
$out+='"> <i class="fa fa-times color-red"></i> </a> </span>';
return new String($out);
});/*v:1*/
template('m_common/m_bottom',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="pull-right"> </div> <div> <a href="';
$out+=$escape(_url('/'));
$out+='" target="_blank" style="color: #676a6c"><strong>Copyright</strong>&nbsp;imaoding.com&nbsp;&copy;&nbsp;2015-2017</a> <span id="footDiskInfo">已使用：0B&nbsp;&nbsp;总容量：5.0GB</span> </div>';
return new String($out);
});/*v:1*/
template('m_common/m_dialog','<div class="dialogOBox" style="height: 100%;overflow: auto;"> </div>');/*v:1*/
template('m_common/m_inputProcessTime',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,appointmentStartTime=$data.appointmentStartTime,$timeInfo=$data.$timeInfo,appointmentEndTime=$data.appointmentEndTime,$isHaveMemo=$data.$isHaveMemo,$out='';$out+='<style> .form-horizontal .m-r-xs{margin:5px -5px;} /*label.error{position: absolute;top:30px;left: -5px;}*/ </style> <form class="form-horizontal rounded-bottom inputTimeOBox p-m"> <div class="col-md-4"> <div class="form-group m-r-xs"> <label>开始时间</label> <div class="input-group"> <input type="text" class="form-control timeInput startTime input-sm" id="ipt_startTime" name="startTime" data-appointmentStartTime = "';
$out+=$escape(appointmentStartTime);
$out+='" placeholder="开始日期" readonly onFocus="startTimeFun(this,m_inputProcessTime_onpicked)" value="';
$out+=$escape($timeInfo.startTime);
$out+='" style="width: 110px"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </div> </div> <div class="col-md-4"> <div class="form-group m-r-xs"> <label>结束时间</label> <div class="input-group"> <input type="text" class="form-control timeInput endTime input-sm" id="ipt_endTime" name="endTime" data-appointmentEndTime = "';
$out+=$escape(appointmentEndTime);
$out+='" placeholder="结束日期" readonly onFocus="endTimeFun(this,m_inputProcessTime_onpicked)" value="';
$out+=$escape($timeInfo.endTime);
$out+='" style="width: 110px"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </div> </div> <div class="col-md-4"> <div class="form-group m-r-xs"> <label>总天数</label> <div class="input-group spinner dayCountSpinner" data-trigger="spinner"> <input type="text" class="form-control text-center input-sm dayCount" value="0" data-rule="quantity"> <div class="input-group-addon"> <a href="javascript:;" class="spin-up" style="height: 8px;width: 8px;" data-spin="up"><i class="fa fa-caret-up"></i></a> <a href="javascript:;" class="spin-down" style="height: 8px;width: 8px;" data-spin="down"><i class="fa fa-caret-down"></i></a> </div> </div> </div> </div> ';
if($isHaveMemo){
$out+=' <div class="col-md-12"> <div class="form-group m-r-xs"> <label>变更原因</label> <textarea name="memo" class="form-control">';
$out+=$escape($timeInfo.memo);
$out+='</textarea> </div> </div> ';
}
$out+=' <div class="clearfix"></div> </form>';
return new String($out);
});/*v:1*/
template('m_common/m_leftNav',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,rootPath=$data.rootPath,userInfo=$data.userInfo,$out='';$out+='<nav class="navbar-default navbar-static-side" role="navigation"> <div class="navbar-header" style="float: none;"> <a class="navbar-minimalize minimalize-styl-2 btn btn-primary" href="#"><i id=\'navBarZoomIcon\' class="fa fa-outdent"></i></a> </div> <div class="sidebar-collapse"> <ul class="nav metismenu" id="side-menu"> <li class="nav-header"> <div class="dropdown profile-element"> <img alt="个人头像" class="img-circle" src="';
$out+=$escape(rootPath);
$out+='/assets/img/profile_small.jpg"> <a data-toggle="dropdown" class="dropdown-toggle" href="#"> <span class="clear"> <span class="block m-t-xs"> <strong class="font-bold">';
$out+=$escape(userInfo.userName);
$out+='</strong><b class="caret"></b> </span></span> </a> <ul class="dropdown-menu animated fadeInUp m-t-xs"> <li><a href="';
$out+=$escape(rootPath);
$out+='//iWork/personal/center">个人中心</a></li> </ul> </div> <div class="logo-element"> <img alt="个人头像" class="img-circle" src="';
$out+=$escape(rootPath);
$out+='/assets/img/profile_small.jpg"> </div> </li> <li class="active"> <a href="javascript:void(0);"><i class="fa fa-home"></i> <span class="nav-label">工作台</span></a> </li> <li> <a href="expense.html"><i class="fa fa-money"></i> <span class="nav-label">费用报销</span> </a> </li> <li> <a href="expense.html"><i class="fa fa-envelope"></i> <span class="nav-label">通知公告</span> </a> </li> <li> <a href="expense.html"><i class="fa fa-users"></i> <span class="nav-label">人员管理</span> </a> </li> <li> <a href="expense.html"><i class="fa fa-cogs"></i> <span class="nav-label">后台管理</span> </a> </li> </ul> </div> </nav>';
return new String($out);
});/*v:1*/
template('m_common/m_popover',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,popoverStyle=$data.popoverStyle,$string=$utils.$string,titleHtml=$data.titleHtml,contentStyle=$data.contentStyle,content=$data.content,$out='';$out+='<div class="popover m-popover box-shadow" role="tooltip" style="';
$out+=$escape(popoverStyle);
$out+='"> <div class="arrow" style="left: 50%;"></div>  ';
$out+=$string(titleHtml);
$out+=' <div class="popover-content" style="';
$out+=$escape(contentStyle);
$out+='"> ';
$out+=$string(content);
$out+=' </div> </div>';
return new String($out);
});/*v:1*/
template('m_common/m_popover_confirm',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$string=$utils.$string,confirmMsg=$data.confirmMsg,$out='';$out+='<div> <p class="f-s-13">';
$out+=$string(confirmMsg);
$out+='</p> <p class="pull-right" > <button type="button" class="popover-btn-no btn-u btn-u-default btn-u-xs rounded m-popover-close left m-r-4" style="line-height:22px;">取消 </button> <button type="button" class="popover-btn-yes btn-u btn-u-red btn-u-xs rounded m-popover-submit m-l-sm left" style="line-height:22px;">确定</button> </p> </div>';
return new String($out);
});/*v:1*/
template('m_common/m_quickDatePicker',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,isClear=$data.isClear,$out='';$out+='<style> #quickDatePicker iframe{box-shadow: none !important;} </style> <div id=\'quickDatePicker\' class="quickDatePicker"> </div> <div class="row"> <div class="col-md-12 text-right m-b-sm"> ';
if(isClear){
$out+=' <button type="button" class="btn btn-primary btn-sm m-popover-clear">清空</button> ';
}
$out+=' </div> </div>';
return new String($out);
});/*v:1*/
template('m_common/m_top',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,rootPath=$data.rootPath,companyInfo=$data.companyInfo,_isNullOrBlank=$helpers._isNullOrBlank,isAuth=$data.isAuth,orgList=$data.orgList,$each=$utils.$each,o=$data.o,i=$data.i,roleCodes=$data.roleCodes,adminFlag=$data.adminFlag,userInfo=$data.userInfo,unReadNotice=$data.unReadNotice,unReadMessage=$data.unReadMessage,$out='';$out+='<style> a.svg { display: inline-block; position: relative; } a.svg:after { content: ""; position: absolute; top: 0; right: 0; bottom: 0; left: 0; } .navbar-brand{ height: 60px; padding: 12px 38px; } </style> <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0px;">  <div class="navbar-header"> <button aria-controls="navbar" aria-expanded="false" data-target="#navbar" data-toggle="collapse" class="navbar-toggle collapsed" type="button"> <i class="fa fa-reorder"></i> </button> <a href="';
$out+=$escape(rootPath);
$out+='" class="navbar-brand svg" target="_blank"> <object class="maoding_logo pt-relative" data="';
$out+=$escape(rootPath);
$out+='/assets/img/logo_white.png"></object> </a> </div>  <div id="navbar" class="navbar-collapse collapse"> ';
if(companyInfo&&!_isNullOrBlank(companyInfo.filePath)){
$out+=' <ul class="nav navbar-top-links navbar-left" style="padding: 0 25px 0 5px;"> ';
}else{
$out+=' <ul class="nav navbar-top-links navbar-left" style="padding: 0 10px 0 20px;"> ';
}
$out+=' <li class="pull-left"> ';
if(companyInfo!=null && companyInfo.companyName!=null && companyInfo.companyName!=''){
$out+=' <div class="dropdown l-h-60 no-pd"> <a class="orgInfo" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" href="javascript:void(0)"> ';
$out+=$escape(companyInfo.companyName);
$out+=' ';
if(isAuth && isAuth==2){
$out+=' <object data="';
$out+=$escape(rootPath);
$out+='/assets/img/org/verifiedIcon.svg" type="image/svg+xml" style="height: 16px;margin-bottom: 4px; vertical-align: middle;"></object> <span style="color: #F8AC5B;">已认证</span> ';
}
$out+=' <span class="caret"></span> </a> <ul id=\'m_top_orgList\' class="dropdown-menu dropdown-menu-left dropdown-menu-new" style="min-width: 250px;margin-left:-7px;"> ';
if(orgList&&orgList.length>1){
$out+=' <li style="margin: 2px 0 0 0;"> <a class="title pt-relative" href="javascript:void(0)"><span class="lineLeft"></span>切换组织<span class="lineRight"></span></a> </li> ';
$each(orgList,function(o,i){
$out+=' ';
if(o.id!== companyInfo.id ){
$out+=' <li> <a href="javascript:void(0);" data-action="switchOrg" org-id="';
$out+=$escape(o.id);
$out+='"> <div> ';
$out+=$escape(o.companyName);
$out+=' </div> </a> </li> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' </ul> </div> ';
}
$out+=' </li> ';
if((roleCodes!=null && ( roleCodes.indexOf('sys_enterprise_logout')>-1 || roleCodes.indexOf('com_enterprise_edit')>-1 || roleCodes.indexOf('hr_org_set')>-1 || roleCodes.indexOf('hr_employee')>-1
                || roleCodes.indexOf('org_data_import')>-1 || roleCodes.indexOf('data_import')>-1 || roleCodes.indexOf('org_partner')>-1 || roleCodes.indexOf('sys_role_permission')>-1
                || roleCodes.indexOf('sys_role_auth')>-1 ) ) || adminFlag=='1'){
$out+=' <li class="pull-left"> <a data-action="backstageMgt" title="后台管理" style="padding: 22px 20px 14px 10px;"> <i class="fa fa-cog" style="margin-right: 9px;margin-left: 2px;"></i> </a> </li> ';
}
$out+=' </ul>  <ul class="nav navbar-top-links navbar-right"> <li class="pull-left"> <div class="dropdown l-h-60 no-pd"> <img alt="image" class="img-circle m-t-n-xs" src="';
$out+=$escape(userInfo.imgUrl);
$out+='" style="width: 40px;height: 40px;margin-right:5px;"> <a class="userInfo" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" data-type="textUserName" href="javascript:void(0)"> ';
$out+=$escape(userInfo.userName);
$out+='<span class="caret"></span> </a> <ul class="dropdown-menu dropdown-menu-left dropdown-menu-new" style="z-index: 99999"> <li><a href="javascript:void(0)" data-action="personalSettings">个人设置</a></li> <li><a href="javascript:void(0)" data-action="createOrg">创建组织</a></li> <li class="divider"></li> <li><a href="';
$out+=$escape(rootPath);
$out+='/iWork/sys/logout">退出登录</a></li> </ul> </div> </li> <li class="pull-left"> <a class="messageInfo" href="javascript:void(0);" data-action="announcement"> <i class="fa fa-bell"></i><span id="unReadNoticeCount" class="label label-warning">';
$out+=$escape(unReadNotice);
$out+='</span> </a> </li> <li class="pull-left"> <a class="messageInfo" href="javascript:void(0);" data-action="messageCenter"> <i class="fa fa-envelope"></i> <span id="unReadMessageCount" class="label label-warning">';
$out+=$escape(unReadMessage);
$out+='</span> </a> </li> </ul> </div> </nav>';
return new String($out);
});/*v:1*/
template('m_cost/m_cost_addNode','<div class="ibox m-contractPayment-add"> <div class="ibox-content"> <div class="row"> <div class="col-sm-12"> <form > <div class="form-group"> <label>节点描述</label> <input placeholder="描述" class="form-control" type="text" name="feeDescription"> </div> <div class="form-group"> <label>比例</label> <div class="input-group"> <input placeholder="比例" class="form-control" type="text" data-action="feeCalculation" name="feeProportion"> <span class="input-group-addon">%</span> </div> </div> <div class="form-group"> <label>金额</label> <div class="input-group"> <input placeholder="金额" class="form-control" type="text" data-action="feeCalculation" name="fee"> <span class="input-group-addon">万元</span> </div> </div> </form> </div> </div> </div> </div>');/*v:1*/
template('m_cost/m_cost_addPaidFee',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,doType=$data.doType,$out='';$out+='<form class="form feeForm wid-270"> <div class="form-group m-b-xs"> <label class="col-24-md-8 control-label no-pd-left m-t-xs">';
$out+=$escape(doType==2?'付款':'到账');
$out+='金额</label> <div class="col-24-md-16 no-padding"> <div class="input-group"> <input placeholder="金额" class="form-control pay-input" type="text" name="fee" > <span class="input-group-addon">万元</span> </div> </div> <div class="clearfix"></div> </div> <div class="form-group m-b-xs"> <label class="col-24-md-8 control-label no-pd-left m-t-xs">';
$out+=$escape(doType==2?'付款':'到账');
$out+='日期</label> <div class="col-24-md-16 inpt-group no-pd-left"> <div class="input-group"> <input class="form-control pay-input" type="text" name="paidDate" onFocus="WdatePicker({maxDate:new Date().toLocaleDateString()})" readonly /> <span class="input-group-addon"><i class="icon-append fa fa-calendar"></i></span> </div> </div> <div class="clearfix"></div> </div> <div class="form-group m-b-xs no-pd-right col-md-12"> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right m-b-sm"> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-b-sm m-r-xs"> <i class="fa fa-check"></i> </button> </div> </form>';
return new String($out);
});/*v:1*/
template('m_cost/m_cost_addPayment',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,currentDate=$data.currentDate,$out='';$out+='<div class="ibox no-margins"> <div class="ibox-content no-borders"> <form class="form-horizontal"> <div class="form-group"> <label class="col-sm-2 control-label">金额：</label> <div class="col-sm-10"> <div class="input-group"> <input placeholder="请输入发起金额" class="form-control" type="text" name="fee"> <span class="input-group-addon">万元</span> </div> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">是否开票：</label> <div class="col-sm-10 m-t-xs"> <label class="i-checks"> <input class="ck" type="checkbox" name="addInvoiceCk"/> <span class="v-middle fw-normal">开票信息</span> </label> <span class="text-muted f-s-12">(若需要开票，请勾选并填写相关信息)</span> </div> </div> <div class="hr-line-dashed m-t-none"></div> <div class="invoice-box" style="display: none;"> <div class="form-group"> <label class="col-sm-2 control-label">申请日期：</label> <div class="col-sm-10"> <div class="input-group"> <input type="text" class="form-control" name="applyDate" placeholder="申请日期" readonly onclick="WdatePicker()" value="';
$out+=$escape(currentDate);
$out+='"> <span class="input-group-addon"> <i class="icon-append fa fa-calendar"></i> </span> </div> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">收票方名称：</label> <div class="col-sm-10"> <select class="form-control" name="invoiceName" style="width: 548px;"> </select> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">纳税识别号：</label> <div class="col-sm-10"> <input placeholder="请输入纳税识别号" class="form-control" type="text" name="taxIdNumber"> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">开票内容：</label> <div class="col-sm-10"> <input placeholder="请输入开票内容" class="form-control" type="text" name="invoiceContent"> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">备注：</label> <div class="col-sm-10"> <textarea placeholder="请输入备注" class="form-control" type="text" name="invoiceRemark"></textarea> </div> </div> </div> </form> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_cost/m_cost_collectionPlan',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,isManager=$data.isManager,dataList=$data.dataList,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-content"> <div class="row"> <div class="col-md-12 text-right"> ';
if(isManager==1){
$out+=' <a class="btn btn-primary" data-action="addCollectionPlan">添加收款计划</a> ';
}
$out+=' </div> </div> <div class="m-t-xs" id="collectionPlanBox"> ';
if(dataList==null || dataList.length==0){
$out+=' <div class="panel panel-default no-borders m-t-xs"> <div class="panel-body text-center"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">没有相关数据</span> </div> </div> ';
}
$out+=' </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_cost/m_cost_collectionPlan_add',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,title=$data.title,doType=$data.doType,$out='';$out+='<form class="form-horizontal m_cost_collectionPlan_add m-r m-l "> <div class="form-group"> <label class="col-24-md-5 control-label">';
$out+=$escape(title);
$out+='类型：</label> <div class="col-24-md-19"> <select class="form-control" name="feeType" style="width: 100%;"> <option value="">请选择</option> ';
if(doType==0){
$out+=' <option value="1">合同回款</option> ';
}
$out+=' <option value="2">技术审查费</option> <option value="3">合作设计费</option> ';
if(doType==0){
$out+=' <option value="5">其他收入</option> ';
}else{
$out+=' <option value="4">其他支出</option> ';
}
$out+=' </select> </div> </div> <div class="form-group"> <label class="col-24-md-5 control-label no-pd-left">计划';
$out+=$escape(title);
$out+='金额：</label> <div class="col-24-md-19"> <div class="input-group"> <input class="form-control" type="text" name="fee"> <span class="input-group-addon">万元</span> </div> </div> </div> <div class="form-group"> <label class="col-24-md-5 control-label"> ';
if(doType==1){
$out+=' 收款单位： ';
}else{
$out+=' 付款单位： ';
}
$out+=' </label> <div class="col-24-md-19"> <select class="form-control full-width" name="receivingUnit" readonly="" > </select> </div> </div> <div class="form-group"> <label class="col-24-md-5 control-label">关联合同：</label> <div class="col-24-md-19"> <div class="input-group select2-multiple"> <select class="js-example-disabled-results form-control" name="projectContract" multiple="multiple" readonly="" style="width: 392px;"> </select> <div class="input-group-btn"> <button tabindex="-1" class="web-uploader btn btn-primary" type="button" data-action="upload"></button> </div> </div> </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_cost/m_cost_collectionPlan_item',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,pageText=$data.pageText,isManager=$data.isManager,operateCompanyId=$data.operateCompanyId,currentCompanyId=$data.currentCompanyId,totalCost=$data.totalCost,_expNumberFilter=$helpers._expNumberFilter,_isNullOrBlank=$helpers._isNullOrBlank,attachList=$data.attachList,$each=$utils.$each,a=$data.a,$index=$data.$index,companyName=$data.companyName,pointList=$data.pointList,p=$data.p,i=$data.i,startReceiveFlag=$data.startReceiveFlag,v=$data.v,vi=$data.vi,isFinancial=$data.isFinancial,_momentFormat=$helpers._momentFormat,c=$data.c,ci=$data.ci,total=$data.total,$out='';$out+='<div class="panel-body popover-box no-borders no-padding"> <table class="table table-bordered"> <thead> <tr> <th colspan="10"> <div class="row"> <div class="col-md-3">';
$out+=$escape(pageText.title);
$out+='</div> <div class="col-md-3"> 计划收款金额： <span class="text-navy "> ';
if(isManager==1 && operateCompanyId==currentCompanyId){
$out+=' <a href="javascript:void(0);" class="totalContract ';
$out+=$escape(totalCost!=null && totalCost-0>0?'':'withoutSet');
$out+='" data-value="';
$out+=$escape(totalCost);
$out+='"> ';
$out+=$escape(totalCost!=null && totalCost-0>0?_expNumberFilter(totalCost):'未设置');
$out+='</a> ';
}else{
$out+=' ';
$out+=$escape(totalCost==null||totalCost-0==0?'0':_expNumberFilter(totalCost));
$out+=' ';
}
$out+=' &nbsp;';
$out+=$escape(_isNullOrBlank(totalCost==null)?'':'万元');
$out+=' </span> </div> <div class="col-md-3"> 合同附件： ';
if(attachList!=null && attachList.length>0){
$out+=' ';
$each(attachList,function(a,$index){
$out+=' <a href="';
$out+=$escape(a.fileFullPath);
$out+='" target="_blank">';
$out+=$escape(a.fileName);
$out+='</a> ';
});
$out+=' ';
}else{
$out+=' <span>无</span> ';
}
$out+=' </div> <div class="col-md-3"> 付款单位：';
$out+=$escape(companyName);
$out+=' </div> </div> </th> </tr> <tr> <th colspan="3" class="text-center v-middle">节点信息</th> <th colspan="2" class="text-center v-middle">收款信息</th> <th colspan="2" class="text-center v-middle">开票信息</th> <th colspan="3" class="text-center v-middle">到账明细</th> </tr> <tr> <th width="18%" class="text-center v-middle">回款节点</th> <th width="7%" class="text-center v-middle">比例</th> <th width="9%" class="text-center v-middle">金额（万元）</th> <th width="9%" class="text-center">发起人</th> <th width="9%" class="text-center">金额（万元）</th> <th width="8%" class="text-center">发票号码</th> <th width="6%" class="text-center">发票类型</th> <th width="13%" class="text-center">到账金额（万元）</th> <th width="9%" class="text-center">到账日期</th> <th width="12%" class="text-center">应收（万元）</th> </tr> </thead> <tbody class="border-no-t"> ';
$each(pointList,function(p,i){
$out+=' <tr data-i="';
$out+=$escape(i);
$out+='" data-id="';
$out+=$escape(p.id);
$out+='" data-fee="';
$out+=$escape(p.fee);
$out+='" data-backFee="';
$out+=$escape(p.backFee);
$out+='">  <td class="feeDescription" rowspan="';
$out+=$escape(p.pointDetailList.length==0?1:p.pointDetailList.length);
$out+='"> ';
if(isManager==1 && operateCompanyId==currentCompanyId){
$out+=' <span class="feeDescription pt-relative l-h-22" data-string="';
$out+=$escape(p.feeDescription);
$out+='"> <a href="javascript:void(0)" data-action="editContract" data-edit-type="1" data-id="';
$out+=$escape(p.id);
$out+='">';
$out+=$escape(p.feeDescription);
$out+='</a> </span> ';
}else{
$out+=' <span class="feeDescription pt-relative" data-string="';
$out+=$escape(p.feeDescription);
$out+='"> ';
$out+=$escape(p.feeDescription);
$out+=' </span> ';
}
$out+=' ';
if(isManager==1 && p.backFee-0!=p.fee && (p.fee!=null && p.fee-0>0)  && startReceiveFlag==1){
$out+=' <a href="javascript:void(0)" class="btn btn-primary btn-xs pull-right" data-action="initiatedContract" data-id="';
$out+=$escape(p.id);
$out+='" >发起回款</a> ';
}
$out+=' ';
if(isManager==1 && p.id!=null && operateCompanyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right m-r-xs" data-action="delCostPoint" data-id="';
$out+=$escape(p.id);
$out+='" >删除</a> ';
}
$out+=' </td>  <td rowspan="';
$out+=$escape(p.pointDetailList.length==0?1:p.pointDetailList.length);
$out+='" class="text-center feeProportion ';
$out+=$escape(p.pid==null?'f-w-bold':'');
$out+='" data-value="';
$out+=$escape(p.feeProportion);
$out+='"> ';
if(isManager==1 && totalCost!=null && totalCost-0>0 && operateCompanyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="border-b-dashed ';
$out+=$escape(p.feeProportion!=null && p.feeProportion-0>0?'':'withoutSet');
$out+='" data-action="editContract" data-edit-type="2" data-id="';
$out+=$escape(p.id);
$out+='"> ';
$out+=$escape(!(p.feeProportion!=null && p.feeProportion-0>0)?'未设置':p.feeProportion+'%');
$out+=' </a> ';
}else{
$out+=' ';
$out+=$escape(p.feeProportion==null || p.feeProportion-0==0?'0':p.feeProportion+'%');
$out+=' ';
}
$out+=' </td>  <td rowspan="';
$out+=$escape(p.pointDetailList.length==0?1:p.pointDetailList.length);
$out+='" class="text-center fee ';
$out+=$escape(p.pid==null?'f-w-bold':'');
$out+='" data-value="';
$out+=$escape(p.fee);
$out+='"> ';
if(isManager==1 && totalCost!=null && totalCost-0>0 && operateCompanyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="border-b-dashed ';
$out+=$escape(p.fee!=null && p.fee-0>0?'':'withoutSet');
$out+='" data-action="editContract" data-edit-type="3" data-id="';
$out+=$escape(p.id);
$out+='" > ';
$out+=$escape(p.fee!=null && p.fee-0>0?_expNumberFilter(p.fee):'未设置');
$out+=' </a> ';
}else{
$out+=' ';
$out+=$escape(p.fee!=null && p.fee-0>0?_expNumberFilter(p.fee):'0');
$out+=' ';
}
$out+=' </td>  <td class="text-center"> ';
if(p.pointDetailList!=null && p.pointDetailList.length>0){
$out+=' <span data-toggle="tooltip" data-placement="top" title="';
$out+=$escape(p.pointDetailList[0].createDate);
$out+='"> ';
$out+=$escape(p.pointDetailList[0].userName);
$out+=' </span> ';
}
$out+=' </td>  <td class=" totalPaidFee text-center" data-value="';
$out+=$escape(p.pointDetailList.length>0?p.pointDetailList[0].fee:'');
$out+='"> <div> ';
if(p.pointDetailList!=null && p.pointDetailList.length>0){
$out+=' ';
if(isManager==1 && (p.pointDetailList[0].paymentList==null || p.pointDetailList[0].paymentList.length==0) && p.pointDetailList[0].companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" data-action="editContract" data-edit-type="4" data-id="';
$out+=$escape(p.pointDetailList[0].id);
$out+='" data-point-id="';
$out+=$escape(p.id);
$out+='"> ';
$out+=$escape(_expNumberFilter(p.pointDetailList[0].fee));
$out+=' </a> ';
}else{
$out+=' ';
$out+=$escape(_expNumberFilter(p.pointDetailList[0].fee));
$out+=' ';
}
$out+=' ';
if(p.pointDetailList[0]!=null && p.pointDetailList[0].id!=null && isManager==1 && p.pointDetailList[0].companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right" data-action="delCostPointDetail" data-id="';
$out+=$escape(p.pointDetailList[0].id);
$out+='">删除</a> ';
}
$out+=' ';
}
$out+=' </div> </td>  <td class="text-center"> ';
if(p.pointDetailList!=null && p.pointDetailList.length>0 && !_isNullOrBlank(p.pointDetailList[0].invoice) && !_isNullOrBlank(p.pointDetailList[0].roleMap.invoiceConfirm)){
$out+=' <a href="javascript:void(0)" class="btn btn-primary btn-xs" data-action="confirmInvoice" data-id="';
$out+=$escape(p.pointDetailList[0].id);
$out+='" data-invoice="';
$out+=$escape(p.pointDetailList[0].invoice);
$out+='">确认开票</a> ';
}else if(p.pointDetailList!=null && p.pointDetailList.length>0){
$out+=' ';
$out+=$escape(p.pointDetailList[0].invoiceNo);
$out+=' ';
}
$out+=' </td> <td class="text-center"> ';
if(p.pointDetailList!=null && p.pointDetailList.length>0 && p.pointDetailList[0].invoiceType==1){
$out+=' 普票 ';
}else if(p.pointDetailList!=null && p.pointDetailList.length>0 && p.pointDetailList[0].invoiceType==2){
$out+=' 专票 ';
}
$out+=' </td>  <td class="text-center no-padding" colspan="2"> ';
if(p.pointDetailList!=null  && p.pointDetailList.length>0 && p.pointDetailList[0].paymentList.length>0){
$out+=' ';
$each(p.pointDetailList[0].paymentList,function(v,vi){
$out+=' <div class="';
$out+=$escape(p.pointDetailList[0].paymentList.length==vi+1 && !(p.pointDetailList[0].roleMap!=null && p.pointDetailList[0].roleMap.financialForFee!=null)?'l-h-38':'paid-detail-box');
$out+='"> <div class="bPaidFee"> <span>';
$out+=$escape(_expNumberFilter(v.fee));
$out+='</span> ';
if(isFinancial=="1" && p.pointDetailList[0].companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right m-r-xs" data-action="delPaidFee" data-id="';
$out+=$escape(v.id);
$out+='">删除</a> ';
}
$out+=' </div> <div class="paid-date">';
$out+=$escape(_momentFormat(v.paidDate,'YYYY/MM/DD'));
$out+='</div> <div class="clearfix"></div> </div> ';
});
$out+=' ';
}
$out+=' ';
if(p.pointDetailList.length>0 && p.pointDetailList[0].roleMap!=null && p.pointDetailList[0].roleMap.financialForFee!=null){
$out+=' <span data-toggle="tooltip" data-placement="top" title="添加到账金额" class="l-h-38"> <a class="btn btn-outline full-width no-padding" href="javascript:void(0)" data-id="';
$out+=$escape(p.pointDetailList[0].roleMap.financialForFee);
$out+='" data-limit="';
$out+=$escape(p.pointDetailList.length>0?p.pointDetailList[0].notReceiveFee:'');
$out+='" data-action="costConfirm" type="button" > <i class="fa fa-plus color-dark-blue f-s-20" ></i> </a> </span> ';
}
$out+=' </td> <td class="text-center"> ';
if(p.pointDetailList!=null && p.pointDetailList.length>0){
$out+=' ';
$out+=$escape(_expNumberFilter(p.pointDetailList[0].notReceiveFee));
$out+=' ';
}
$out+=' </td> </tr> ';
if(p.pointDetailList!=null && p.pointDetailList.length>1){
$out+=' ';
$each(p.pointDetailList,function(c,ci){
$out+=' ';
if(ci>0){
$out+=' <tr data-id="';
$out+=$escape(p.id);
$out+='" data-fee="';
$out+=$escape(p.fee);
$out+='" data-backFee="';
$out+=$escape(p.backFee);
$out+='">  <td class="text-center"> <span data-toggle="tooltip" data-placement="top" title="';
$out+=$escape(c.createDate);
$out+='"> ';
$out+=$escape(c.userName);
$out+=' </span> </td>  <td class=" totalPaidFee text-center" data-value="';
$out+=$escape(c.fee);
$out+='"> <div> ';
if(isManager==1 && (c.paymentList==null || c.paymentList.length==0) && c.companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" data-action="editContract" data-edit-type="4" data-id="';
$out+=$escape(c.id);
$out+='" data-point-id="';
$out+=$escape(p.id);
$out+='"> ';
$out+=$escape(_expNumberFilter(c.fee));
$out+=' </a> ';
}else{
$out+=' ';
$out+=$escape(_expNumberFilter(c.fee));
$out+=' ';
}
$out+=' ';
if(c.id!=null && isManager==1 && c.companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right" data-action="delCostPointDetail" data-id="';
$out+=$escape(c.id);
$out+='">删除</a> ';
}
$out+=' </div> </td>  <td class="text-center"> ';
if(!_isNullOrBlank(c.invoice) && !_isNullOrBlank(c.roleMap.invoiceConfirm)){
$out+=' <a href="javascript:void(0)" class="btn btn-primary btn-xs" data-action="confirmInvoice" data-id="';
$out+=$escape(c.id);
$out+='" data-invoice="';
$out+=$escape(c.invoice);
$out+='">确认开票</a> ';
}else{
$out+=' ';
$out+=$escape(c.invoiceNo);
$out+=' ';
}
$out+=' </td> <td class="text-center"> ';
if(c.invoiceType==1){
$out+=' 普票 ';
}else if(c.invoiceType==2){
$out+=' 专票 ';
}
$out+=' </td>  <td class="text-center no-padding" colspan="2"> ';
if(c.paymentList.length>0){
$out+=' ';
$each(c.paymentList,function(v,vi){
$out+=' <div class="';
$out+=$escape(c.paymentList.length==vi+1 && !(c.roleMap!=null && c.roleMap.financialForFee!=null)?'l-h-38':'paid-detail-box');
$out+='" > <div class="bPaidFee"> <span>';
$out+=$escape(_expNumberFilter(v.fee));
$out+='</span> ';
if(isFinancial=="1" && c.companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right m-r-xs" data-action="delPaidFee" data-id="';
$out+=$escape(v.id);
$out+='">删除</a> ';
}
$out+=' </div> <div class="paid-date">';
$out+=$escape(_momentFormat(v.paidDate,'YYYY/MM/DD'));
$out+='</div> <div class="clearfix"></div> </div> ';
});
$out+=' ';
}
$out+=' ';
if(c.roleMap!=null && c.roleMap.financialForFee!=null ){
$out+=' <span data-toggle="tooltip" data-placement="top" title="添加到账金额" class="l-h-38"> <a class="btn btn-outline full-width no-padding" data-id="';
$out+=$escape(c.roleMap.financialForFee);
$out+='" data-limit="';
$out+=$escape(c.notReceiveFee);
$out+='" href="javascript:void(0)" data-action="costConfirm" type="button" > <i class="fa fa-plus color-dark-blue f-s-20" ></i> </a> </span> ';
}
$out+=' </td> <td class="text-center">';
$out+=$escape(_expNumberFilter(c.notReceiveFee));
$out+='</td> </tr> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' ';
});
$out+=' ';
if(isManager==1 && totalCost!=null && totalCost-0>0 && operateCompanyId==currentCompanyId){
$out+=' <tr> <td colspan="10" align="center"> <span data-toggle="tooltip" data-placement="top" title="添加回款节点"> <a class="btn btn-outline full-width border-dashed" href="javascript:void(0)" data-action="addContract" type="button" > <i class="fa fa-plus color-dark-blue f-s-20" ></i> </a> </span> </td> </tr> ';
}
$out+=' <tr class="gray-bg"> <td class="text-center">合计</td> <td class="text-center f-w-bold">';
$out+=$escape(total.feeProportion==null || total.feeProportion-0==0?'0':total.feeProportion+'%');
$out+='</td> <td class="text-center f-w-bold">';
$out+=$escape(total.fee==null || total.fee-0==0?0:_expNumberFilter(total.fee));
$out+='</td> <td></td> <td class="text-center">';
$out+=$escape(total.backMoney==null || total.backMoney-0==0?0:_expNumberFilter(total.backMoney));
$out+='</td> <td></td> <td></td> <td class="text-center">';
$out+=$escape(total.toTheMoney==null || total.toTheMoney-0==0?0:_expNumberFilter(total.toTheMoney));
$out+='</td> <td></td> <td class="text-center">';
$out+=$escape(total.receivedUncollected==null || total.receivedUncollected-0==0?0:_expNumberFilter(total.receivedUncollected));
$out+='</td> </tr> </tbody> </table> </div> ';
return new String($out);
});/*v:1*/
template('m_cost/m_cost_confirmInvoice',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_isNullOrBlank=$helpers._isNullOrBlank,pointInfo=$data.pointInfo,_expNumberFilter=$helpers._expNumberFilter,_momentFormat=$helpers._momentFormat,invoiceInfo=$data.invoiceInfo,$out='';$out+='<div class="ibox no-margins"> <div class="ibox-content no-borders"> <form class="form-horizontal"> <div class="form-group m-b-none"> <label class="col-sm-2 text-right">金额：</label> <div class="col-sm-10"> ';
$out+=$escape(_isNullOrBlank(pointInfo.fee)?'0':_expNumberFilter(pointInfo.fee));
$out+=' 万元 </div> </div> <div class="form-group m-b-none"> <label class="col-sm-2 text-right">申请日期：</label> <div class="col-sm-10"> ';
$out+=$escape(_momentFormat(invoiceInfo.applyDate,'YYYY/MM/DD'));
$out+=' </div> </div> <div class="form-group m-b-none"> <label class="col-sm-2 text-right">收票方名称：</label> <div class="col-sm-10"> ';
$out+=$escape(invoiceInfo.relationCompanyName);
$out+=' </div> </div> <div class="form-group m-b-none"> <label class="col-sm-2 text-right">纳税识别号：</label> <div class="col-sm-10"> ';
$out+=$escape(invoiceInfo.taxIdNumber);
$out+=' </div> </div> <div class="form-group m-b-none"> <label class="col-sm-2 text-right">备注：</label> <div class="col-sm-10"> ';
$out+=$escape(invoiceInfo.invoiceRemark);
$out+=' </div> </div> <div class="hr-line-dashed m-t-none"></div> <div class="form-group"> <label class="col-sm-2 control-label">发票号码：</label> <div class="col-sm-10"> <input placeholder="请输入发票号码" class="form-control" type="text" name="invoiceNo"> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">发票种类：</label> <div class="col-sm-10 "> <label class="i-checks"> <input class="ck" type="radio" name="invoiceType" checked value="1"/> <span class="v-middle">普票</span> </label> <label class="i-checks"> <input class="ck" type="radio" name="invoiceType" value="2"/> <span class="v-middle">专票</span> </label> </div> </div> <div class="invoice-box" style="display: none;"> <div class="form-group"> <label class="col-sm-2 control-label">地址：</label> <div class="col-sm-10"> <input placeholder="请输入地址" class="form-control" type="text" name="address"> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">电话：</label> <div class="col-sm-10"> <input placeholder="请输入电话" class="form-control" type="text" name="cellphone"> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">开户行：</label> <div class="col-sm-10"> <input placeholder="请输入开户行" class="form-control" type="text" name="accountBank" /> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">账号：</label> <div class="col-sm-10"> <input placeholder="请输入账号" class="form-control" type="text" name="bankNo" /> </div> </div> </div> </form> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_cost/m_cost_menu',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,id=$data.id,projectNameCode=$data.projectNameCode,$out='';$out+='<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb "> <li> 我的项目 </li> <li class=" fa fa-angle-right"> ';
$out+=$escape(projectName);
$out+=' </li> <li class="active fa fa-angle-right"> <strong>收款计划</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right"> <div class="pull-right m-r-xl"> <ul class="secondary-menu-ul"> <!--<li class="active"> <a href="#/projectDetails/cost?type=details&id=';
$out+=$escape(id);
$out+='&projectName=';
$out+=$escape(projectNameCode);
$out+='" id="details">收支明细</a> </li>--> <li class="active"> <a href="#/projectDetails/cost?type=collectionPlan&id=';
$out+=$escape(id);
$out+='&projectName=';
$out+=$escape(projectNameCode);
$out+='" id="collectionPlan">收款计划</a> </li> <li class="" > <a href="#/projectDetails/cost?type=paymentPlan&id=';
$out+=$escape(id);
$out+='&projectName=';
$out+=$escape(projectNameCode);
$out+='" id="paymentPlan">付款计划</a> </li> </ul> </div> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding" id="content-box"> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_cost/m_cost_paymentApplication',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,pointInfo=$data.pointInfo,$out='';$out+='<div class="ibox no-margins"> <div class="ibox-content no-borders"> <form class="form-horizontal"> <div class="form-group m-b-none"> <label class="col-24-md-5 text-right">收款方：</label> <div class="col-24-md-19"> ';
$out+=$escape(pointInfo.companyName);
$out+=' </div> </div> <div class="form-group m-b-none"> <label class="col-24-md-5 text-right">关联节点：</label> <div class="col-24-md-19"> ';
$out+=$escape(pointInfo.feeDescription);
$out+=' </div> </div> <div class="form-group m-b-none"> <label class="col-24-md-5 text-right">节点金额：</label> <div class="col-24-md-19"> ';
$out+=$escape(pointInfo.fee);
$out+=' 万元 </div> </div> ';
if(pointInfo.isInnerCompany){
$out+=' <div class="form-group m-b-none"> <label class="col-24-md-5 text-right">申请人：</label> <div class="col-24-md-19"> ';
$out+=$escape(pointInfo.userName);
$out+=' </div> </div> <div class="form-group m-b-none"> <label class="col-24-md-5 text-right">申请金额：</label> <div class="col-24-md-19"> ';
$out+=$escape(pointInfo.subFee);
$out+=' 万元 </div> </div> ';
}
$out+=' <div class="hr-line-dashed m-t-none"></div> <div class="form-group ';
$out+=$escape(pointInfo.isInnerCompany?'hide':'');
$out+='"> <label class="col-24-md-5 control-label">申请付款金额：</label> <div class="col-24-md-19"> <div class="input-group"> <input placeholder="金额" class="form-control pay-input" type="text" name="fee" value="';
$out+=$escape(pointInfo.subFee);
$out+='"> <span class="input-group-addon">万元</span> </div> </div> </div> <div class="form-group"> <label class="col-24-md-5 control-label">备注：</label> <div class="col-24-md-19"> <textarea placeholder="请输入备注" class="form-control" type="text" name="pointDetailDescription"></textarea> </div> </div> </form> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_cost/m_cost_paymentPlan',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,isManager=$data.isManager,dataList=$data.dataList,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-content"> <div class="row"> <div class="col-md-12 text-right"> ';
if(isManager==1){
$out+=' <a class="btn btn-primary" data-action="addCollectionPlan">添加付款计划</a> ';
}
$out+=' </div> </div> <div class="m-t-xs" id="collectionPlanBox" > ';
if(dataList==null || dataList.length==0){
$out+=' <div class="panel panel-default no-borders m-t-xs"> <div class="panel-body text-center"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">没有相关数据</span> </div> </div> ';
}
$out+=' </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_cost/m_cost_paymentPlan_item',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,pageText=$data.pageText,isManager=$data.isManager,operateCompanyId=$data.operateCompanyId,currentCompanyId=$data.currentCompanyId,totalCost=$data.totalCost,_expNumberFilter=$helpers._expNumberFilter,_isNullOrBlank=$helpers._isNullOrBlank,attachList=$data.attachList,$each=$utils.$each,a=$data.a,$index=$data.$index,companyName=$data.companyName,pointList=$data.pointList,p=$data.p,i=$data.i,startPayFlag=$data.startPayFlag,startPayFlagForInner=$data.startPayFlagForInner,v=$data.v,vi=$data.vi,isFinancial=$data.isFinancial,_momentFormat=$helpers._momentFormat,c=$data.c,ci=$data.ci,total=$data.total,$out='';$out+='<div class="panel-body popover-box no-borders no-padding"> <table class="table table-bordered"> <thead> <tr> <th colspan="10"> <div class="row"> <div class="col-md-3">';
$out+=$escape(pageText.title);
$out+='</div> <div class="col-md-3"> <span>计划付款金额：</span> <span class="text-navy "> ';
if(isManager==1 && operateCompanyId==currentCompanyId){
$out+=' <a href="javascript:void(0);" class="totalContract ';
$out+=$escape(totalCost!=null && totalCost-0>0?'':'withoutSet');
$out+='" data-value="';
$out+=$escape(totalCost);
$out+='"> ';
$out+=$escape(totalCost!=null && totalCost-0>0?_expNumberFilter(totalCost):'未设置');
$out+='</a> ';
}else{
$out+=' ';
$out+=$escape(totalCost==null||totalCost-0==0?'0':_expNumberFilter(totalCost));
$out+=' ';
}
$out+=' &nbsp;';
$out+=$escape(_isNullOrBlank(totalCost==null)?'':'万元');
$out+=' </span> </div> <div class="col-md-3"> 合同附件： ';
if(attachList!=null && attachList.length>0){
$out+=' ';
$each(attachList,function(a,$index){
$out+=' <a href="';
$out+=$escape(a.fileFullPath);
$out+='" target="_blank">';
$out+=$escape(a.fileName);
$out+='</a> ';
});
$out+=' ';
}else{
$out+=' <span>无</span> ';
}
$out+=' </div> <div class="col-md-3"> 收款单位：';
$out+=$escape(companyName);
$out+=' </div> </div> </th> </tr> <tr> <th colspan="3" class="text-center v-middle">付款节点</th> <th colspan="3" class="text-center v-middle">付款通知</th> <th colspan="3" class="text-center v-middle">付款明细</th> </tr> <tr> <th width="18%" class="text-center v-middle">付款节点</th> <th width="7%" class="text-center v-middle">比例</th> <th width="9%" class="text-center v-middle">金额（万元）</th> <th width="9%" class="text-center">发起人</th> <th width="9%" class="text-center">金额（万元）</th> <th width="14%" class="text-center">审批人</th> <th width="13%" class="text-center">付款金额（万元）</th> <th width="9%" class="text-center">付款日期</th> <th width="12%" class="text-center">应付（万元）</th> </tr> </thead> <tbody class="border-no-t"> ';
$each(pointList,function(p,i){
$out+=' <tr data-i="';
$out+=$escape(i);
$out+='" data-id="';
$out+=$escape(p.id);
$out+='" data-fee="';
$out+=$escape(p.fee);
$out+='" data-backFee="';
$out+=$escape(p.backFee);
$out+='">  <td class="feeDescription" rowspan="';
$out+=$escape(p.pointDetailList.length==0?1:p.pointDetailList.length);
$out+='"> ';
if( isManager==1 && operateCompanyId==currentCompanyId){
$out+=' <span class="feeDescription pt-relative l-h-22" data-string="';
$out+=$escape(p.feeDescription);
$out+='"> <a href="javascript:void(0)" data-action="editContract" data-edit-type="1" data-id="';
$out+=$escape(p.id);
$out+='">';
$out+=$escape(p.feeDescription);
$out+='</a> </span> ';
}else{
$out+=' <span class="feeDescription pt-relative" data-string="';
$out+=$escape(p.feeDescription);
$out+='"> ';
$out+=$escape(p.feeDescription);
$out+=' </span> ';
}
$out+=' ';
if(isManager==1 && p.backFee-0!=p.fee && (p.fee!=null && p.fee-0>0) && startPayFlag==1){
$out+=' <a href="javascript:void(0)" class="btn btn-primary btn-xs pull-right" data-action="paymentRequest2" >付款申请</a> ';
}
$out+=' ';
if(isManager==1 && p.id!=null && operateCompanyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right m-r-xs" data-action="delCostPoint" data-id="';
$out+=$escape(p.id);
$out+='" >删除</a> ';
}
$out+=' </td>  <td rowspan="';
$out+=$escape(p.pointDetailList.length==0?1:p.pointDetailList.length);
$out+='" class="text-center feeProportion ';
$out+=$escape(p.pid==null?'f-w-bold':'');
$out+='" data-value="';
$out+=$escape(p.feeProportion);
$out+='"> ';
if(isManager==1 && totalCost!=null && totalCost-0>0 && operateCompanyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="border-b-dashed ';
$out+=$escape(p.feeProportion!=null && p.feeProportion-0>0?'':'withoutSet');
$out+='" data-action="editContract" data-edit-type="2" data-id="';
$out+=$escape(p.id);
$out+='">';
$out+=$escape(!(p.feeProportion!=null && p.feeProportion-0>0)?'未设置':p.feeProportion+'%');
$out+='</a> ';
}else{
$out+=' ';
$out+=$escape(p.feeProportion==null || p.feeProportion-0==0?'0':p.feeProportion+'%');
$out+=' ';
}
$out+=' </td>  <td rowspan="';
$out+=$escape(p.pointDetailList.length==0?1:p.pointDetailList.length);
$out+='" class="text-center fee ';
$out+=$escape(p.pid==null?'f-w-bold':'');
$out+='" data-value="';
$out+=$escape(p.fee);
$out+='"> ';
if(isManager==1 && totalCost!=null && totalCost-0>0 && operateCompanyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="border-b-dashed ';
$out+=$escape(p.fee!=null && p.fee-0>0?'':'withoutSet');
$out+='" data-action="editContract" data-edit-type="3" data-id="';
$out+=$escape(p.id);
$out+='" > ';
$out+=$escape(p.fee!=null && p.fee-0>0?_expNumberFilter(p.fee):'未设置');
$out+=' </a> ';
}else{
$out+=' ';
$out+=$escape(p.fee!=null && p.fee-0>0?_expNumberFilter(p.fee):'0');
$out+=' ';
}
$out+=' </td>  <td class="text-center"> ';
if(p.pointDetailList!=null && p.pointDetailList.length>0 &&  startPayFlagForInner==1 && p.pointDetailList[0].feeStatus==0){
$out+=' <a href="javascript:void(0)" class="btn btn-primary btn-xs" data-action="paymentRequest" data-id="';
$out+=$escape(p.pointDetailList[0].id);
$out+='" >付款申请</a> ';
}else if(p.pointDetailList!=null && p.pointDetailList.length>0){
$out+=' <span data-toggle="tooltip" data-placement="top" title="';
$out+=$escape(p.pointDetailList[0].createDate);
$out+='"> ';
$out+=$escape(p.pointDetailList[0].userName);
$out+=' </span> ';
}
$out+=' </td>  <td class=" totalPaidFee text-center" data-value="';
$out+=$escape(p.pointDetailList.length>0?p.pointDetailList[0].fee:'');
$out+='"> <div> ';
if(p.pointDetailList!=null && p.pointDetailList.length>0){
$out+=' ';
if(isManager==1 && (p.pointDetailList[0].paymentList==null || p.pointDetailList[0].paymentList.length==0) && p.pointDetailList[0].companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" data-action="editContract" data-edit-type="4" data-id="';
$out+=$escape(p.pointDetailList[0].id);
$out+='" data-point-id="';
$out+=$escape(p.id);
$out+='"> ';
$out+=$escape(_expNumberFilter(p.pointDetailList[0].fee));
$out+=' </a> ';
}else{
$out+=' ';
$out+=$escape(_expNumberFilter(p.pointDetailList[0].fee));
$out+=' ';
}
$out+=' ';
if(p.pointDetailList[0]!=null && p.pointDetailList[0].id!=null && isManager==1 && p.pointDetailList[0].companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right" data-action="delCostPointDetail" data-id="';
$out+=$escape(p.pointDetailList[0].id);
$out+='">删除</a> ';
}
$out+=' ';
}
$out+=' </div> </td>  <td class="text-center"> ';
if(p.pointDetailList!=null && p.pointDetailList.length>0){
$out+=' ';
$out+=$escape(p.pointDetailList[0].auditPersonName);
$out+=' ';
}
$out+=' </td>  <td class="text-center no-padding" colspan="2"> ';
if(p.pointDetailList!=null  && p.pointDetailList.length>0 && p.pointDetailList[0].paymentList.length>0){
$out+=' ';
$each(p.pointDetailList[0].paymentList,function(v,vi){
$out+=' <div class="';
$out+=$escape(p.pointDetailList[0].paymentList.length==vi+1 && !(p.pointDetailList[0].roleMap!=null && p.pointDetailList[0].roleMap.financialForFee!=null)?'l-h-38':'paid-detail-box');
$out+='"> <div class="bPaidFee"> <span>';
$out+=$escape(_expNumberFilter(v.fee));
$out+='</span> ';
if(isFinancial=="1" && p.pointDetailList[0].companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right m-r-xs" data-action="delPaidFee" data-id="';
$out+=$escape(v.id);
$out+='">删除</a> ';
}
$out+=' </div> <div class="paid-date">';
$out+=$escape(_momentFormat(v.payDate,'YYYY/MM/DD'));
$out+='</div> <div class="clearfix"></div> </div> ';
});
$out+=' ';
}
$out+=' ';
if(p.pointDetailList.length>0 && p.pointDetailList[0].roleMap!=null && p.pointDetailList[0].roleMap.financialForFee!=null){
$out+=' <span data-toggle="tooltip" data-placement="top" title="添加付款金额" class="l-h-38"> <a class="btn btn-outline full-width no-padding" href="javascript:void(0)" data-id="';
$out+=$escape(p.pointDetailList[0].roleMap.financialForFee);
$out+='" data-limit="';
$out+=$escape(p.pointDetailList.length>0?p.pointDetailList[0].notPayFee2:'');
$out+='" data-action="costConfirm" type="button" > <i class="fa fa-plus color-dark-blue f-s-20" ></i> </a> </span> ';
}
$out+=' </td> <td class="text-center"> ';
if(p.pointDetailList!=null && p.pointDetailList.length>0){
$out+=' ';
$out+=$escape(_expNumberFilter(p.pointDetailList[0].notPayFee));
$out+=' ';
}
$out+=' </td> </tr> ';
if(p.pointDetailList!=null && p.pointDetailList.length>1){
$out+=' ';
$each(p.pointDetailList,function(c,ci){
$out+=' ';
if(ci>0){
$out+=' <tr data-i="';
$out+=$escape(i);
$out+='" data-id="';
$out+=$escape(p.id);
$out+='" data-fee="';
$out+=$escape(p.fee);
$out+='" data-backFee="';
$out+=$escape(p.backFee);
$out+='">  <td class="text-center"> ';
if(startPayFlagForInner==1 && c.feeStatus==0){
$out+=' <a href="javascript:void(0)" class="btn btn-primary btn-xs" data-action="paymentRequest" data-id="';
$out+=$escape(c.id);
$out+='" >付款申请</a> ';
}else{
$out+=' <span data-toggle="tooltip" data-placement="top" title="';
$out+=$escape(c.createDate);
$out+='"> ';
$out+=$escape(c.userName);
$out+=' </span> ';
}
$out+=' </td>  <td class=" totalPaidFee text-center" data-value="';
$out+=$escape(c.fee);
$out+='"> <div> ';
if(isManager==1 && (c.paymentList==null || c.paymentList.length==0) && c.companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" data-action="editContract" data-edit-type="4" data-id="';
$out+=$escape(c.id);
$out+='" data-point-id="';
$out+=$escape(p.id);
$out+='"> ';
$out+=$escape(_expNumberFilter(c.fee));
$out+=' </a> ';
}else{
$out+=' ';
$out+=$escape(_expNumberFilter(c.fee));
$out+=' ';
}
$out+=' ';
if(c.id!=null && isManager==1 && c.companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right" data-action="delCostPointDetail" data-id="';
$out+=$escape(c.id);
$out+='">删除</a> ';
}
$out+=' </div> </td>  <td class="text-center"> ';
$out+=$escape(c.auditPersonName);
$out+=' </td>  <td class="text-center no-padding" colspan="2"> ';
if(c.paymentList.length>0){
$out+=' ';
$each(c.paymentList,function(v,vi){
$out+=' <div class="';
$out+=$escape(c.paymentList.length==vi+1 && !(c.roleMap!=null && c.roleMap.financialForFee!=null)?'l-h-38':'paid-detail-box');
$out+='" > <div class="bPaidFee"> <span>';
$out+=$escape(_expNumberFilter(v.fee));
$out+='</span> ';
if(isFinancial=="1" && c.companyId==currentCompanyId){
$out+=' <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right m-r-xs" data-action="delPaidFee" data-id="';
$out+=$escape(v.id);
$out+='">删除</a> ';
}
$out+=' </div> <div class="paid-date">';
$out+=$escape(_momentFormat(v.payDate,'YYYY/MM/DD'));
$out+='</div> <div class="clearfix"></div> </div> ';
});
$out+=' ';
}
$out+=' ';
if(c.roleMap!=null && c.roleMap.financialForFee!=null ){
$out+=' <span data-toggle="tooltip" data-placement="top" title="添加付款金额" class="l-h-38"> <a class="btn btn-outline full-width no-padding" data-id="';
$out+=$escape(c.roleMap.financialForFee);
$out+='" data-limit="';
$out+=$escape(c.notPayFee2);
$out+='" href="javascript:void(0)" data-action="costConfirm" type="button" > <i class="fa fa-plus color-dark-blue f-s-20" ></i> </a> </span> ';
}
$out+=' </td> <td class="text-center">';
$out+=$escape(_expNumberFilter(c.notPayFee));
$out+='</td> </tr> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' ';
});
$out+=' ';
if(isManager==1 && totalCost!=null && totalCost-0>0 && operateCompanyId==currentCompanyId){
$out+=' <tr> <td colspan="10" align="center"> <span data-toggle="tooltip" data-placement="top" title="添加付款节点"> <a class="btn btn-outline full-width border-dashed" href="javascript:void(0)" data-action="addContract" type="button" > <i class="fa fa-plus color-dark-blue f-s-20" ></i> </a> </span> </td> </tr> ';
}
$out+=' <tr class="gray-bg"> <td class="text-center">合计</td> <td class="text-center f-w-bold">';
$out+=$escape(total.feeProportion==null || total.feeProportion-0==0?'0':total.feeProportion+'%');
$out+='</td> <td class="text-center f-w-bold">';
$out+=$escape(total.fee==null || total.fee-0==0?0:_expNumberFilter(total.fee));
$out+='</td> <td></td> <td class="text-center">';
$out+=$escape(total.backMoney==null || total.backMoney-0==0?0:_expNumberFilter(total.backMoney));
$out+='</td> <td ></td> <td class="text-center">';
$out+=$escape(total.payTheMoney==null || total.payTheMoney-0==0?0:_expNumberFilter(total.payTheMoney));
$out+='</td> <td></td> <td class="text-center">';
$out+=$escape(total.payUncollected==null || total.payUncollected-0==0?0:_expNumberFilter(total.payUncollected));
$out+='</td> </tr> </tbody> </table> </div> ';
return new String($out);
});/*v:1*/
template('m_cost/m_editFee','<form class=" form m-editFee wid-270"> <div class="form-group m-b-xs clearfix"> <label class="col-md-2 control-label no-pd-left m-t-xs no-pd-right">比例</label> <div class="col-md-10 no-pd-right wid-225 input-group" style="padding-left:15px;"> <input type="text" class="form-control full-width" name="feeProportion" data-action="feeCalculation" placeholder="比例" > <span class="input-group-addon">%</span> </div> </div> <div class="form-group m-b-xs clearfix"> <label class="col-md-2 control-label no-pd-left m-t-xs no-pd-right">金额</label> <div class="col-md-10 no-pd-right wid-225 input-group" style="padding-left:15px;" > <input type="text" class="form-control full-width" name="fee" data-action="feeCalculation" placeholder="金额"> <span class="input-group-addon">万元</span> </div> </div> <div class="form-group col-md-12 m-b-xs no-pd-right clearfix"> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right "> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-r-xs"> <i class="fa fa-check"></i> </button> <div class="clearfix"></div> </div> </form>');/*v:1*/
template('m_cost/m_editPaidFee','<form class="form feeForm wid-270"> <div class="form-group m-b-xs"> <label class="col-24-md-8 control-label no-pd-left m-t-xs">付款金额</label> <div class="col-24-md-16 no-padding"> <div class="input-group"> <input placeholder="金额" class="form-control" type="text" name="fee" data-action="feeCalculation"> <span class="input-group-addon">万元</span> </div> </div> <div class="clearfix"></div> </div> <div class="form-group m-b-xs hide"> <label class="col-24-md-8 control-label m-t-xs no-pd-left m-t-xs">付款日期</label> <div class="col-24-md-16 no-padding"> <div class="input-group"> <input class="form-control" type="text" name="paidDate" onFocus="WdatePicker({maxDate:new Date().toLocaleDateString()})" readonly /> <span class="input-group-addon"><i class="icon-append fa fa-calendar"></i></span> </div> </div> <div class="clearfix"></div> </div> <div class="form-group col-md-12 m-b-xs no-pd-right"> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right m-b-sm"> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-b-sm m-r-xs"> <i class="fa fa-check"></i> </button>     </div> </form>');/*v:1*/
template('m_docmgr/m_docmgr',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,$out='';$out+='<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb "> <li> 我的项目 </li> <li class=" fa fa-angle-right"> ';
$out+=$escape(projectName);
$out+=' </li> <li class="active fa fa-angle-right"> <strong>文档库</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right"> <a class="btn btn-primary btn-sm m-t-sm btn-flag" data-action="uploadFile">上传文件</a> <a class="btn btn-primary btn-sm m-t-sm btn-flag" data-action="createFolder">新建文件夹</a> <a class="btn btn-primary btn-sm m-t-sm" data-action="sendArchiveNotice" style="display: none;">发送归档通知</a> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div class="col-24-sm-8 col-24-md-8 col-24-lg-6" id="documentTreeBox" style="height: 500px;overflow: auto;border-right:solid 1px #d9d9d9;"> <div class="m-t-md" id="documentDirectoryTree"> <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div> </div> <div class="col-24-sm-16 col-24-md-16 col-24-lg-18 no-padding"> <div class="docmgr panel panel-default tag-box-v4 border-none" id="fileItems"> </div> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_docmgr/m_docmgr_createFolder',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,fileTypeStr=$data.fileTypeStr,itemData=$data.itemData,$out='';$out+='<div class="ibox"> <div class="ibox-content"> <form class="createFolder"> <div class="row"> <div class="form-group col-md-12"> <label for="folderName"> ';
$out+=$escape(fileTypeStr);
$out+='名称 <span class="color-red">*</span> </label> <input type="text" id="folderName" class="form-control" name="folderName" value="';
$out+=$escape(itemData!=null?itemData.fileName:'');
$out+='"> </div> </div> </form> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_docmgr/m_docmgr_fileItem',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,item=$data.item,_momentFormat=$helpers._momentFormat,$out='';$out+='<tr class="file-list-row"> <td> <div class="file-icon ';
$out+=$escape(item.type===0?'file-icon-dir-small':'file-icon-file-small');
$out+='"></div> <div class="file-name"> <div class="text"> <a id=';
$out+=$escape(item.id);
$out+=' data-pid="';
$out+=$escape(item.pid);
$out+='" data-fileType="';
$out+=$escape(item.type);
$out+='" data-download="';
$out+=$escape(item.fileGroup);
$out+='/';
$out+=$escape(item.filePath);
$out+='" data-fileName="';
$out+=$escape(item.fileName);
$out+='" href="javascript:void(0);" title="';
$out+=$escape(item.fileName);
$out+='">';
$out+=$escape(item.fileName);
$out+='</a> </div> </div> </td> <td class="file-operate"> <div class="file-operate-list"> ';
if(item.type===1){
$out+=' <a class="file-operate-download f-s-16" href="javascript:void(0);" title="下载"><i class="glyphicon glyphicon-save text-success"></i></a> ';
}
$out+=' ';
if(item.isCustomize!==1&&item.editFlag===1){
$out+=' <a class="file-operate-delete f-s-16" href="javascript:void(0);" ><i class="glyphicon glyphicon-trash color-red"></i></a> ';
}
$out+=' </div> </td> <td class="file-size">';
$out+=$escape(item.fileSize);
$out+=' </td> <td class="file-user"><span data-toggle="tooltip" data-placement="top" title="';
$out+=$escape(item.companyName);
$out+='">';
$out+=$escape(item.createBy);
$out+='</span></td> <td class="file-date">';
$out+=$escape(_momentFormat(item.createDate,'YYYY/MM/DD'));
$out+='</td> </tr>';
return new String($out);
});/*v:1*/
template('m_docmgr/m_docmgr_fileItems',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,fileItems=$data.fileItems,$escape=$utils.$escape,rootPath=$data.rootPath,$each=$utils.$each,item=$data.item,index=$data.index,_momentFormat=$helpers._momentFormat,$out='';if((fileItems===void 0 || fileItems===null || fileItems.length===0) ){
$out+=' <tr class="file-list-row no-data"> <td colspan="5" class="file-not-found text-center"> <div> <img src="';
$out+=$escape(rootPath);
$out+='/assets/img/default/without_file.png"> </div> <span class="fc-dark-blue">当前目录不存在任何文件</span> </td> </tr> ';
}else{
$out+=' ';
$each(fileItems,function(item,index){
$out+=' <tr class="file-list-row"> <td style="max-width: 200px;"> <a id=';
$out+=$escape(item.id);
$out+=' data-pid="';
$out+=$escape(item.pid);
$out+='" data-fileType="';
$out+=$escape(item.type);
$out+='" data-download="';
$out+=$escape(item.fileGroup);
$out+='/';
$out+=$escape(item.filePath);
$out+='" data-fileName="';
$out+=$escape(item.fileName);
$out+='" href="javascript:void(0);" title="';
$out+=$escape(item.fileName);
$out+='"> <div class="file-icon ';
$out+=$escape(item.type===0?'file-icon-dir-small':'file-icon-file-small');
$out+='"></div> <div class="file-name"> <div class="text">';
$out+=$escape(item.fileName);
$out+='</div> </div> </a> </td> <td class="file-operate"> <div class="file-operate-list"> ';
if(item.type===1){
$out+=' <a class="file-operate-download f-s-16" href="javascript:void(0);" title="下载"><i class="glyphicon glyphicon-save text-warning"></i></a> ';
}
$out+=' ';
if(item.isCustomize!==1&&item.editFlag===1){
$out+=' <a class="file-operate-rename f-s-16" href="javascript:void(0);" title="重命名"><i class="glyphicon glyphicon-edit text-success" ></i></a> <a class="file-operate-delete f-s-16" href="javascript:void(0);"><i class="glyphicon glyphicon-trash color-red" ></i></a> ';
}
$out+=' </div> </td> <td class="file-size">';
$out+=$escape(item.fileSize);
$out+=' </td> <td class="file-user"><span data-toggle="tooltip" data-placement="top" title="';
$out+=$escape(item.companyName);
$out+='">';
$out+=$escape(item.createBy);
$out+='</span></td> <td class="file-date">';
$out+=$escape(_momentFormat(item.createDate,'YYYY/MM/DD'));
$out+='</td> </tr> ';
});
$out+=' ';
}
return new String($out);
});/*v:1*/
template('m_docmgr/m_docmgr_fileItems_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,fileItems=$data.fileItems,$escape=$utils.$escape,rootPath=$data.rootPath,$each=$utils.$each,item=$data.item,index=$data.index,_fastdfsUrl=$helpers._fastdfsUrl,_momentFormat=$helpers._momentFormat,$out='';$out+='<table class="table file-list"> <thead> <tr class="file-list-row">  <th width="5%">  </th> <th width="45%">文件名</th> <th width="15%">大小</th> <th width="15%">创建人</th> <th width="15%">创建时间</th> </tr> </thead> <tbody class="file-list-items" > ';
if((fileItems===void 0 || fileItems===null || fileItems.length===0) ){
$out+=' <tr class="file-list-row no-data"> <td colspan="5" class="file-not-found text-center"> <div> <img src="';
$out+=$escape(rootPath);
$out+='/assets/img/default/without_file.png"> </div> <span class="fc-dark-blue">当前目录不存在任何文件</span> </td> </tr> ';
}else{
$out+=' ';
$each(fileItems,function(item,index){
$out+=' <tr class="file-list-row" data-i="';
$out+=$escape(index);
$out+='" data-id="';
$out+=$escape(item.id);
$out+='" data-type="';
$out+=$escape(item.type);
$out+='" id="box';
$out+=$escape(item.id);
$out+='" unselectable="on" onselectstart="return false;" style="-moz-user-select:none;-webkit-user-select:none; cursor: default;">  <td> <div class="list-action-box"> ';
if(item.type==2 || item.type==1 || (item.type==0 || item.type==1) ||  item.type==50 || (item.sendResults==1 && item.type==30)){
$out+=' <div class="btn-group singleOperation" style="display: none;"> <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <span class="fa fa-angle-down"></span> </button> <ul class="dropdown-menu" id="menu';
$out+=$escape(item.id);
$out+='"> ';
if(item.type==2){
$out+=' <li> <a href="javascript:void(0);" data-action="editFileName">修改名称</a> </li> ';
}
$out+=' ';
if(item.type==1){
$out+=' <li> <a href="javascript:void(0);" data-action="downloadFile">下载文件</a> </li> ';
}
$out+='  ';
if(item.type==0 || item.type==1){
$out+=' <li> <a href="javascript:void(0);" data-action="delFile">删除文件</a> </li> ';
}
$out+=' ';
if(item.type==50){
$out+=' <li> <a href="javascript:void(0);" data-action="submitDocResults">提交成果</a> </li> ';
}
$out+=' ';
if(item.sendResults==1 && item.type==30){
$out+=' <li> <a href="javascript:void(0);" data-action="sendDocResults">发送成果</a> </li> ';
}
$out+=' </ul> </div> ';
}
$out+=' </div> </td> <td style="max-width: 200px;"> <a href="javascript:void(0);" id=';
$out+=$escape(item.id);
$out+=' data-pid="';
$out+=$escape(item.pid);
$out+='" data-fileType="';
$out+=$escape(item.type);
$out+='" data-download="';
$out+=$escape(item.fileGroup);
$out+='/';
$out+=$escape(item.filePath);
$out+='" data-fileName="';
$out+=$escape(item.fileName);
$out+='" title="';
$out+=$escape(item.fileName);
$out+='" data-action="intoSubDirectory"> ';
if(item.type===0 || item.type===40 || item.type===30 || item.type===2 || item.type === 41){
$out+=' <div class="file-icon file-icon-dir-small"></div> ';
}else if(item.type===50){
$out+=' <div class="fa fa-steam-square"></div> ';
}else{
$out+=' <div class="file-icon file-icon-file-small"></div> ';
}
$out+=' <div class="file-name"> <div class="text"> ';
if(item.type==1){
$out+=' <a href="';
$out+=$escape(_fastdfsUrl(item.fileGroup+'/'+item.filePath));
$out+='" target="_blank"> ';
$out+=$escape(item.fileName);
$out+=' </a> ';
}else{
$out+=' ';
$out+=$escape(item.fileName);
$out+=' ';
}
$out+=' </div> </div> </a> </td> <!--<td class="file-operate"> <div class="file-operate-list"> ';
if(item.type===1){
$out+=' <a class="file-operate-download f-s-16" href="javascript:void(0);" title="下载"><i class="glyphicon glyphicon-save text-warning"></i></a> ';
}
$out+=' ';
if(item.isCustomize!==1&&item.editFlag===1){
$out+=' <a class="file-operate-rename f-s-16" href="javascript:void(0);" title="重命名"><i class="glyphicon glyphicon-edit text-success" ></i></a> <a class="file-operate-delete f-s-16" href="javascript:void(0);"><i class="glyphicon glyphicon-trash color-red" ></i></a> ';
}
$out+=' </div> </td>--> <td class="file-size">';
$out+=$escape(item.fileSize);
$out+=' </td> <td class="file-user"><span data-toggle="tooltip" data-placement="top" title="';
$out+=$escape(item.companyName);
$out+='">';
$out+=$escape(item.createBy);
$out+='</span></td> <td class="file-date">';
$out+=$escape(_momentFormat(item.createDate,'YYYY/MM/DD'));
$out+='</td> </tr> ';
});
$out+=' ';
}
$out+=' </tbody> </table> <iframe class="iframeDown dp-none"></iframe>';
return new String($out);
});/*v:1*/
template('m_docmgr/m_docmgr_fileUpload','<style> .uploadmgrContainer .tag-box{ margin-bottom: 0; } </style> <div class="ibox"> <div class="ibox-content"> <div class="uploadmgrContainer"> </div> </div> </div> ');/*v:1*/
template('m_docmgr/m_docmgr_sendArchiveNotice',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,selectedNodeObj=$data.selectedNodeObj,nowDate=$data.nowDate,$out='';$out+='<div class="ibox"> <div class="ibox-content"> <form class="sendArchiveNotice"> <div class="row"> <div class="form-group col-md-12"> <label class="col-24-md-5 text-right m-t-sm">归档任务<span class="color-red">*</span>：</label> <div class="col-24-md-19"> <input type="text" class="form-control" name="taskName" maxlength="50" value="';
$out+=$escape(selectedNodeObj.text);
$out+='"> </div> </div> <div class="form-group col-md-12"> <label class="col-24-md-5 text-right m-t-sm">通知人员<span class="color-red">*</span>：</label> <div class="col-24-md-19"> <select class="js-example-disabled-results form-control" name="users" disabled> </select> </div> </div> <div class="form-group col-md-12"> <label class="col-24-md-5 text-right m-t-sm">归档截止时间<span class="color-red">*</span>：</label> <div class="col-24-md-19"> <input type="text" class="form-control" name="deadline" onFocus="WdatePicker({dateFmt:\'yyyy-MM-dd\'})" readonly value="';
$out+=$escape(nowDate);
$out+='"> </div> </div> <div class="form-group col-md-12"> <label class="col-24-md-5 text-right m-t-sm">备注：</label> <div class="col-24-md-19"> <textarea class="form-control" name="remarks"> </textarea> </div> </div> </div> </form> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_docmgr/m_docmgr_sendDocResults',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,companyData=$data.companyData,$out='';$out+='<div class="ibox"> <div class="ibox-content"> <form class="sendArchiveNotice"> <div class="row"> <div class="form-group col-md-12"> <label class="col-24-md-5 text-right m-t-sm">甲方：</label> <div class="col-24-md-19 m-t-sm"> ';
$out+=$escape(companyData.companyName);
$out+=' </div> </div> </div> </form> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_docmgr/m_projectArchiving','<div class="ibox m_projectArchiving"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6"> <div class="pull-left m-r-xl"> <h3 class="dp-inline-block" >项目文档</h3> </div> </div> <div class="col-md-6 text-right"> <a class="btn btn-primary btn-sm m-t-sm pull-right m-l-xs" data-action="sendArchiveNotice" style="display: none;">发送归档通知</a> <a class="btn btn-primary btn-sm m-t-sm btn-flag pull-right m-l-xs" data-action="uploadFile" style="display: none;">上传文件</a> <a class="btn btn-primary btn-sm m-t-sm btn-flag pull-right m-l-xs" data-action="createFolder" style="display: none;">新建文件夹</a>  </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div class="col-24-sm-8 col-24-md-8 col-24-lg-6" id="documentTreeBox" style="height: 500px;overflow: auto;border-right:solid 1px #d9d9d9;"> <div class="row m-t-sm"> <div class="col-md-3 no-pd-right select-search-box" > <select class="select-search-by" name="selectSearchBy"> <option value="0" selected>项目名称</option> <option value="1">文件名称</option> </select> </div> <div class="col-md-9"> <div class="input-group "> <input class="form-control" type="text" name="projectName" placeholder="请输入项目名称"> <span class="input-group-btn"> <button type="button" class="btn btn-primary" data-action="searchByProjectName">搜索</button> </span> </div> </div> </div> <div class="m-t-md" id="documentDirectoryTree"> <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div> </div> <div class="col-24-sm-16 col-24-md-16 col-24-lg-18 no-padding"> <div class="docmgr panel panel-default tag-box-v4 border-none" id="fileItems" > </div> <div class="col-md-12 p-w-m"> <div id="data-pagination-container" class="m-pagination pull-right " style="display: none;"></div> </div> </div> </div> </div> ');/*v:1*/
template('m_docmgr/m_uploadmgr',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,boxClass=$data.boxClass,isShowBtnClose=$data.isShowBtnClose,$out='';$out+='<div class="uploadmgr ';
$out+=$escape(boxClass==null?'tag-box tag-box-v1 box-shadow shadow-effect-1':boxClass);
$out+='"> <div class="alertmgr"></div> <a href="javascript:void(0)" class="btn-select btn-u btn-u-sm btn-u-dark-blue rounded" type="button"><i class="fa fa-plus"></i>&nbsp;选择文件</a>  ';
if(isShowBtnClose){
$out+=' <a href="javascript:void(0)" type="button" class="btn-close close">×</a> ';
}
$out+=' <div class="upload-item-list"> </div> <p class="pull-right"></p> </div>';
return new String($out);
});/*v:1*/
template('m_docmgr/m_uploadmgr_uploadItem',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,file=$data.file,pid=$data.pid,$out='';$out+='<div class="uploadItem uploadItem_';
$out+=$escape(file.id);
$out+='" data-fileId="';
$out+=$escape(file.id);
$out+='" data-pid="';
$out+=$escape(pid);
$out+='">  <h3 class="heading-xs"> <span class="file-name">';
$out+=$escape(file.name);
$out+='</span> <span style="padding-left:5px;" class="span_progress"></span> <a class="pull-right btn-link " data-action="removeFile"> <i class="glyphicon glyphicon-remove"></i> </a> <a class="pull-right btn-link m-r-xs" data-action="pauseUpload" style="display: none;"> <i class="glyphicon glyphicon-pause"></i> </a> <a class="pull-right btn-link m-r-xs" data-action="continueUpload" style="display: none;"> <i class="glyphicon glyphicon-play"></i> </a> <span class="span_status pull-right m-r-sm"></span> <div class="clearfix"></div> </h3> <div class="progress progress-u progress-xs"> <div class="progress-bar progress-bar-u" role="progressbar" name="div_progress" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%" > </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_filterableField/m_filterTag',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,filters=$data.filters,$each=$utils.$each,f=$data.f,$index=$data.$index,$escape=$utils.$escape,_jsonStringify=$helpers._jsonStringify,_filterDateRangeToString=$helpers._filterDateRangeToString,$out='';if(filters&&filters.length>0){
$out+=' <span class="f-s-13 fw-600 fc-dark-blue">过滤条件：</span> ';
$each(filters,function(f,$index){
$out+=' <div class="filter-tag" data-field="';
$out+=$escape(f.field);
$out+='" data-filterJson="';
$out+=$escape(_jsonStringify(f));
$out+='"> <a href="javascript:void(0)" class="filter-tag-remove"><i class="fa fa-times"></i></a> ';
if(f.filterType === 'contain'){
$out+=' ';
$out+=$escape(f.fieldDisplayName);
$out+='：';
$out+=$escape(f.filterValue);
$out+=' ';
}else if(f.filterType === 'custom'){
$out+=' ';
$out+=$escape(f.fieldDisplayName);
$out+='：';
$out+=$escape(f.filterValueDisplayName);
$out+=' ';
}else if(f.filterType === 'select_local_data'){
$out+=' ';
$out+=$escape(f.fieldDisplayName);
$out+='：';
$out+=$escape(f.filterValueDisplayName);
$out+=' ';
}else if(f.filterType === 'dateRange'){
$out+=' ';
$out+=$escape(f.fieldDisplayName);
$out+='：';
$out+=$escape(_filterDateRangeToString(f.filterValue));
$out+=' ';
}
$out+=' </div> ';
});
$out+=' ';
}
return new String($out);
});/*v:1*/
template('m_filterableField/m_filterType_contain','<form class="form-inline"> <div class="form-group filter-control"> <input type="text" class="form-control input-sm" name="filter_contain" style="width: 150px;min-width: 150px;"> </div> <div class="text-right m-t-xs m-b-xs"> <button type="button" class="btn btn-default btn-sm filter-clear"> <i class="fa fa-eraser"></i> </button> <button type="button" class="btn btn-primary btn-sm filter-submit"> <i class="fa fa-check"></i> </button> </div> </form>');/*v:1*/
template('m_filterableField/m_filterType_dateRange','<style> form.form-inline div.input-group{width:30px;} </style> <form class="form-inline" style="width: 305px;"> <div class="form-group filter-control"> <div class="input-group"> <input type="text" id="filter_startDate" name="filter_startDate" onFocus="WdatePicker({maxDate:\'#F{$dp.$D(\\\'filter_endDate\\\')||\\\'2099-12-30\\\'}\'})" placeholder="开始时间" class="form-control input-sm curp wth-100 min-wth-100 bg-none" readonly="readonly"> <span class="input-group-addon"><i class="icon-append fa fa-calendar"></i></span> </div> <div class="text-align-center dp-inline-block"style="width: 20px;">~</div> <div class="input-group"> <input type="text" id="filter_endDate" name="filter_endDate" onFocus="WdatePicker({minDate:\'#F{$dp.$D(\\\'filter_startDate\\\')}\',maxDate:\'2099-12-30\'})" placeholder="结束时间" class="form-control input-sm curp wth-100 min-wth-100 bg-none" readonly="readonly"> <span class="input-group-addon"><i class="icon-append fa fa-calendar"></i></span> </div> </div> <div class="text-right m-t-xs m-b-xs"> <button type="button" class="btn btn-default btn-sm filter-clear"> <i class="fa fa-eraser"></i> </button> <button type="button" class="btn btn-primary btn-sm filter-submit"> <i class="fa fa-check"></i> </button> </div> </form>');/*v:1*/
template('m_filterableField/m_filterType_select','<form class="form-inline"> <div class="form-group filter-control"> <select class="form-control input-sm" name="filter_select" style="width: 150px;min-width: 150px;"></select> </div> <button type="button" class="btn btn-default btn-sm filter-clear"> <i class="fa fa-eraser"></i> </button> <button type="button" class="btn btn-primary btn-sm filter-submit"> <i class="fa fa-check"></i> </button> </form> <script type="text/javascript"> $(\'select[name="filter_select"]\').select2({ clear:true, language: "zh-CN", ajax: { contentType: "application/json", url: restApi.url_getUserByKeyWord, dataType: \'json\', type: \'POST\', delay: 500, data: function (params) { var ret = { keyword: params.term /*,companyId: window.currentCompanyId*/ }; return JSON.stringify(ret); }, processResults: function (data, params) { return { results: $.map(data.data, function (o, i) { return { id: o.id, text: o.user_name } }) }; }, cache: true } }); </script>');/*v:1*/
template('m_filterableField/m_filterType_select_local_data','<form class="form-inline"> <div class="form-group filter-control"> <select class="form-control input-sm" name="filter_select" style="width: 265px;min-width: 265px;"></select> </div> <div class="text-right m-t-xs m-b-xs">    <button type="button" class="btn btn-primary btn-sm filter-submit"> <i class="fa fa-check"></i> </button> </div> </form> <script type="text/javascript"> </script>');/*v:1*/
template('m_filterableField/m_filter_address','<div class="data-list-filter p-xs" > <div class="m-b-sm"> <div class="form-group m-b-xs col-md-12 no-pd-right no-pd-left" style="margin: 0 0 5px 0"> <label for="selectRegion" class="m-l-xs">所在地区</label> <div class="input-group cityBox" id="selectRegion" name="selectRegion"> <div class="dp-inline-block m-l-xs"> <select class="prov form-control" name="province"></select> </div> <div class="dp-inline-block m-l-xs"> <select class="city form-control" name="city" disabled="disabled" ></select> </div> <div class="dp-inline-block m-l-xs"> <select class="dist form-control" name="county" disabled="disabled" ></select> </div> </div> </div> </div> <div class="m-t-xs"> <button type="button" class="btn btn-primary btn-xs rounded pull-right" data-action="confirm">确定</button> <button type="button" class="btn btn-default btn-xs rounded pull-right m-r-xs" data-action="cancel">取消</button> <div class="clearfix"></div> </div> </div> ');/*v:1*/
template('m_filterableField/m_filter_checkbox',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,checkList=$data.checkList,d=$data.d,$index=$data.$index,currCheckedVal=$data.currCheckedVal,$escape=$utils.$escape,$out='';$out+='<div class="data-list-filter p-sm" > <div class="m-b-sm" style="border-bottom: solid 1px #f2f2f2;"> <label class="i-checks fw-normal"> <input name="ipt_allCheck" type="checkbox" value="0"/> <span class="i-checks-span">全选</span> </label> </div> <div> ';
$each(checkList,function(d,$index){
$out+=' <div class="col-md-4 no-pd-right no-pd-left"> <label class="i-checks fw-normal"> ';
if(currCheckedVal.indexOf(d.id)>-1){
$out+=' <input name="ipt_check" type="checkbox" checked value="';
$out+=$escape(d.id);
$out+='"/> ';
}else{
$out+=' <input name="ipt_check" type="checkbox" value="';
$out+=$escape(d.id);
$out+='"/> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(d.name);
$out+='</span> </label> </div> ';
});
$out+=' <div class="clearfix"></div> </div> <div class="m-t-xs"> <button type="button" class="btn btn-primary btn-xs rounded pull-right" data-action="confirm">确定</button> <button type="button" class="btn btn-default btn-xs rounded pull-right m-r-xs" data-action="cancel">取消</button> <div class="clearfix"></div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_filterableField/m_filter_checkbox_select',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,boxStyle=$data.boxStyle,$each=$utils.$each,selectList=$data.selectList,s=$data.s,$index=$data.$index,isParentCheck=$data.isParentCheck,sub=$data.sub,colClass=$data.colClass,$out='';$out+='<div class="data-list-filter " style="';
$out+=$escape(boxStyle);
$out+='"> <div class="m-b-sm check-box-title" > <label class="i-checks fw-normal"> <input name="itemCk" type="checkbox" value=""/> <span class="i-checks-span">全选</span> </label> </div> <div class="p-w-sm"> ';
$each(selectList,function(s,$index){
$out+=' ';
if(s.childList!=null && s.childList.length>0){
$out+=' <div class="col-md-12 no-pd-right no-pd-left"> ';
if(isParentCheck){
$out+=' <label class="i-checks fw-normal"> ';
if(s.isSelected){
$out+=' <input name="itemCk" type="checkbox" checked value="';
$out+=$escape(s.id);
$out+='"/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" value="';
$out+=$escape(s.id);
$out+='" /> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(s.name);
$out+='</span> </label> ';
}else{
$out+=' <label>';
$out+=$escape(s.name);
$out+='</label> ';
}
$out+=' </div> ';
$each(s.childList,function(sub,$index){
$out+=' <div class="';
$out+=$escape(colClass?colClass:'col-md-4');
$out+=' no-pd-right no-pd-left"> <label class="i-checks fw-normal"> ';
if(sub.isSelected){
$out+=' <input name="itemCk" type="checkbox" checked value="';
$out+=$escape(sub.id);
$out+='"/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" value="';
$out+=$escape(sub.id);
$out+='" /> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(sub.name);
$out+='</span> </label> </div> ';
});
$out+=' ';
}else{
$out+=' <div class="';
$out+=$escape(colClass?colClass:'col-md-4');
$out+=' no-pd-right no-pd-left"> <label class="i-checks fw-normal"> ';
if(s.isSelected){
$out+=' <input name="itemCk" type="checkbox" checked value="';
$out+=$escape(s.id);
$out+='"/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" value="';
$out+=$escape(s.id);
$out+='" /> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(s.name);
$out+='</span> </label> </div> ';
}
$out+=' ';
});
$out+=' <div class="clearfix"></div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_filterableField/m_filter_input',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,placeholder=$data.placeholder,txtVal=$data.txtVal,$out='';$out+='<div class="data-list-filter p-xs"> <div> <div class="form-group"> <input type="text" class="form-control input-sm" name="txtVal" placeholder="';
$out+=$escape(placeholder);
$out+='" value="';
$out+=$escape(txtVal);
$out+='"> </div> <div class="m-t-xs"> <button type="button" class="btn btn-primary btn-xs rounded pull-right" data-action="sureFilter">确定</button>  <div class="clearfix"></div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_filterableField/m_filter_select',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,selectList=$data.selectList,s=$data.s,$index=$data.$index,$escape=$utils.$escape,currCheckValue=$data.currCheckValue,$out='';$out+='<div class="data-list-filter"> <ul class="dropdown-menu"> ';
$each(selectList,function(s,$index){
$out+=' <li> <a data-state-no="';
$out+=$escape(s.fieldValue);
$out+='"> <span class="check"> ';
if(s.fieldValue == currCheckValue){
$out+=' <i class="fa fa-check"></i> ';
}
$out+=' </span> ';
$out+=$escape(s.fieldName);
$out+=' </a> </li> ';
});
$out+=' </ul> </div>';
return new String($out);
});/*v:1*/
template('m_filterableField/m_filter_select1',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,selectList=$data.selectList,s=$data.s,$index=$data.$index,$escape=$utils.$escape,isMultiple=$data.isMultiple,$out='';$out+='<div class="data-list-filter"> <ul class="dropdown-menu"> ';
$each(selectList,function(s,$index){
$out+=' <li> <a class="no-margins p-r-5" data-state-no="';
$out+=$escape(s.id);
$out+='"> ';
if(isMultiple){
$out+=' <label class="i-checks fw-normal"> ';
if(s.isSelected){
$out+=' <input name="itemCk" type="checkbox" checked value="';
$out+=$escape(s.id);
$out+='"/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" value="';
$out+=$escape(s.id);
$out+='" /> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(s.name);
$out+='</span> </label> ';
}else{
$out+=' <span class="check"> ';
if(s.isSelected){
$out+=' <i class="fa fa-check"></i> ';
}
$out+=' </span> ';
$out+=$escape(s.name);
$out+=' ';
}
$out+=' </a> </li> ';
});
$out+=' </ul> </div>';
return new String($out);
});/*v:1*/
template('m_filterableField/m_filter_time',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,startTime=$data.startTime,endTime=$data.endTime,$out='';$out+='<div class="data-list-filter p-xs"> <div> <div class="form-group"> <div class="input-group"> <input type="text" class="form-control input-sm" id="ipt_startTime" name="startTime" placeholder="开始时间" readonly onFocus="WdatePicker({maxDate:\'#F{$dp.$D(\\\'ipt_endTime\\\')}\'})" value="';
$out+=$escape(startTime);
$out+='"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </div> <div class="form-group m-t-xs"> <div class="input-group "> <input type="text" class="form-control input-sm" id="ipt_endTime" name="endTime" placeholder="结束时间" readonly onFocus="WdatePicker({minDate:\'#F{$dp.$D(\\\'ipt_startTime\\\')}\'})" value="';
$out+=$escape(endTime);
$out+='"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </div> <div class="m-t-xs"> <button type="button" class="btn btn-primary btn-xs rounded pull-right" data-action="sureTimeFilter">确定</button> <button type="button" class="btn btn-default btn-xs rounded pull-right m-r-xs" data-action="clearTimeInput">清空</button> <div class="clearfix"></div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_filterableField/m_filter_timeCombination','<div class="m_filter_timeCombination"> <div class="form-group"> <label class="m-t-xs">时间：</label> <div class="pull-right m-l-xs"> <input type="text" class="form-control input-sm wth-100" name="ipt_year" onclick="" placeholder="请选择年份" readonly/> <select class="form-control input-sm wth-100 no-padding" name="ipt_month"> </select> </div> <div class="btn-group pull-right"> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="30">一个月</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="90">一季度</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="180">半年</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="360">一年</a> </div> <div class="clearfix"></div> </div> <div class="form-group"> <div class="input-group dp-inline-block"> <input type="text" class="form-control input-sm time-input" id="ipt_startTime" name="startTime" placeholder="开始日期" readonly="" value=""> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> <div class="input-group dp-inline-block"> <input type="text" class="form-control input-sm time-input" id="ipt_endTime" name="endTime" placeholder="结束日期" readonly="" value=""> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> </div> </div>');/*v:1*/
template('m_filterableField/m_popover_filter',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,popoverStyle=$data.popoverStyle,_isNullOrBlank=$helpers._isNullOrBlank,title=$data.title,$string=$utils.$string,titleHtml=$data.titleHtml,$out='';$out+='<div class="popover popover-filter box-shadow" role="tooltip" style="';
$out+=$escape(popoverStyle);
$out+='"> <div class="arrow border-top" style="left: 50%;"></div>  ';
if(!_isNullOrBlank(title) ){
$out+=' ';
$out+=$string(titleHtml);
$out+=' ';
}
$out+=' <div class="popover-content p-sm"> </div> </div>';
return new String($out);
});/*v:1*/
template('m_finance/m_addExpDetailTr',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,detail=$data.detail,$each=$utils.$each,projectList=$data.projectList,pro=$data.pro,d=$data.d,$out='';$out+=' <tr target="target';
$out+=$escape(detail);
$out+='"> <td> <input class="form-control" type="text" id="expAmount" class="form-control" maxlength="20" name="expAmount';
$out+=$escape(detail);
$out+='" placeholder="输入报销金额"> </td> <td class="expTypeTd"> </td> <td> <input type="text" id="expUse" name="expUse';
$out+=$escape(detail);
$out+='" class="form-control" placeholder="输入用途说明" maxlength="1000" /> </td> <td> <select id="projectId" name="projectId';
$out+=$escape(detail);
$out+='" class="form-control curp p-r-24"> <option value="">选择关联项目</option> ';
$each(projectList,function(pro,d){
$out+=' <option value="';
$out+=$escape(pro.id);
$out+='">';
$out+=$escape(pro.projectName);
$out+='</option> ';
});
$out+=' </select> </td> <td> ';
if(detail!=0){
$out+=' <div class="m-t-xs"> <a href="javascript:void(0);" class="btn-u btn-u-sm btn-u-red getClickFun rounded" i="';
$out+=$escape(detail);
$out+='" data-action="deleteDetail" title="删除"><i class="fa fa-close"></i></a> </div> ';
}
$out+=' </td> </tr>';
return new String($out);
});/*v:1*/
template('m_finance/m_costSharing_settings','<div class="m_costSharing_settings" > <div class="col-24-sm-8 col-24-md-8 col-24-lg-6 no-padding" id="left-box" style="border-right: solid 1px #ccc;"> <div class="clearfix margin-bottom-10"></div> <div id="organization_treeH"> <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div> </div> <div class="col-24-sm-16 col-24-md-16 col-24-lg-18 no-padding " id="right-box"> </div> </div> ');/*v:1*/
template('m_finance/m_costSharing_settings_content',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,feeEntryFieldList=$data.feeEntryFieldList,f=$data.f,i=$data.i,$escape=$utils.$escape,c=$data.c,ci=$data.ci,$out='';$out+='<div class="m_feeEntry_settings" > <table class="table table-bordered table-responsive"> <thead> <tr> <th width="35%">收支分类</th> <th width="45%">收支分类细类</th> <th width="20%">数据来源</th> </tr> </thead> <tbody> ';
$each(feeEntryFieldList,function(f,i){
$out+=' <tr> <td rowspan="';
$out+=$escape(f.childList!=null && f.childList.length>0 ?f.childList.length+1:'');
$out+='" class="gray-bg v-middle"> <div class="check-box"> <label class="i-checks pull-left"> <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(f.code);
$out+='" data-pid="';
$out+=$escape(f.pid);
$out+='" data-id="';
$out+=$escape(f.id);
$out+='"/> <span class="i-checks-span">';
$out+=$escape(f.name);
$out+='</span> </label> </div> </td> <td> ';
if(f.childList!=null && f.childList.length>0){
$out+=' <div class="check-box"> <label class="i-checks pull-left"> ';
if(f.childList[0].showStatus == 1){
$out+=' ';
if(f.childList[0].categoryType == 0){
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(f.childList[0].code);
$out+='" data-pid="';
$out+=$escape(f.childList[0].pid);
$out+='" data-id="';
$out+=$escape(f.childList[0].id);
$out+='" checked disabled/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(f.childList[0].code);
$out+='" data-pid="';
$out+=$escape(f.childList[0].pid);
$out+='" data-id="';
$out+=$escape(f.childList[0].id);
$out+='" checked/> ';
}
$out+=' ';
}else{
$out+=' ';
if(f.childList[0].categoryType == 0){
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(f.childList[0].code);
$out+='" data-pid="';
$out+=$escape(f.childList[0].pid);
$out+='" data-id="';
$out+=$escape(f.childList[0].id);
$out+='" disabled/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(f.childList[0].code);
$out+='" data-pid="';
$out+=$escape(f.childList[0].pid);
$out+='" data-id="';
$out+=$escape(f.childList[0].id);
$out+='"/> ';
}
$out+=' ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(f.childList[0].name);
$out+='</span> </label> </div> ';
}else{
$out+=' <button class="btn btn-link btn-add" data-action="addFeeField" title="添加设计任务" data-id="';
$out+=$escape(f.id);
$out+='"><i class="fa fa-plus"></i></button> ';
}
$out+=' </td> <td> ';
if(f.childList!=null && f.childList.length>0 && (f.childList[0].categoryType==2 || f.childList[0].categoryType==3)){
$out+=' 人工输入 ';
}
$out+=' </td> </tr> ';
if(f.childList!=null && f.childList.length>0){
$out+=' ';
$each(f.childList,function(c,ci){
$out+=' ';
if(ci>0){
$out+=' <tr class=""> <td> <div class="check-box"> <label class="i-checks pull-left"> ';
if(c.showStatus==1){
$out+=' ';
if(c.categoryType==0){
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(c.code);
$out+='" data-pid="';
$out+=$escape(c.pid);
$out+='" data-id="';
$out+=$escape(c.id);
$out+='" checked disabled/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(c.code);
$out+='" data-pid="';
$out+=$escape(c.pid);
$out+='" data-id="';
$out+=$escape(c.id);
$out+='" checked/> ';
}
$out+=' ';
}else{
$out+=' ';
if(c.categoryType==0){
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(c.code);
$out+='" data-pid="';
$out+=$escape(c.pid);
$out+='" data-id="';
$out+=$escape(c.id);
$out+='" disabled/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(c.code);
$out+='" data-pid="';
$out+=$escape(c.pid);
$out+='" data-id="';
$out+=$escape(c.id);
$out+='"/> ';
}
$out+=' ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(c.name);
$out+='</span> </label> </div> </td> <td> ';
if(c.categoryType==2 || c.categoryType==3){
$out+=' 人工输入 ';
}
$out+=' </td> </tr> ';
}
$out+=' ';
});
$out+=' <tr> <td > <button class="btn btn-link btn-add" data-action="addFeeField" title="添加设计任务" data-id="';
$out+=$escape(f.id);
$out+='"><i class="fa fa-plus"></i></button> </td> <td></td> </tr> ';
}
$out+=' ';
});
$out+=' </tbody> </table> </div> ';
return new String($out);
});/*v:1*/
template('m_finance/m_costSummary',' <div class="ibox ibox_min_height m_costSummary"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 审批管理 </li> <li class="active fa fa-angle-right"> <strong>费用汇总</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div class="" id="summary"> <input type="hidden" name="approveStatus" value=""/> <input type="hidden" name="myExpStartDate" value=""/> <input type="hidden" name="myExpEndDate" value=""/> <input type="hidden" name="expNo" value=""/> <input type="hidden" name="expType" value=""/> <input type="hidden" name="approveStartDate" value=""/> <input type="hidden" name="approveEndDate" value=""/> <input type="hidden" name="approveUserName" value=""/> <input type="hidden" name="allocationStatus" value=""/> <input type="hidden" name="allocationStartDate" value=""/> <input type="hidden" name="allocationEndDate" value=""/> <input type="hidden" name="applyCompanyName" value=""/> <input type="hidden" name="sortAllocationDate" value=""/>  <section class="mySummaryListBox"> <div class="row"> <div class="col-md-12" id="mySummaryListData"></div> <div class="col-md-12 padding-right-25"> <div id="mySummary-pagination-container" class="m-pagination pull-right"></div> </div> </div> </section>  </div> </div> </div> ');/*v:1*/
template('m_finance/m_costSummary_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,myDataList=$data.myDataList,p=$data.p,$index=$data.$index,$escape=$utils.$escape,pageIndex=$data.pageIndex,_momentFormat=$helpers._momentFormat,_expNumberFilter=$helpers._expNumberFilter,rootPath=$data.rootPath,expSumAmount=$data.expSumAmount,financialAllocationSumAmount=$data.financialAllocationSumAmount,$out='';$out+='<style> .table-hover>tbody>tr.no-data:hover { background-color: transparent; } </style> <table class="table table-hover table-bordered table-responsive dataTable" > <thead> <tr> <th width="6%">序号</th> <th width="10%"> 编号 <a class="icon-filter pull-right" id="filterExpNo" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th>  <th width="10%"> 申请时间 <a class="icon-filter pull-right" id="filterExpDate" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="10%"> 申请人 <a class="icon-filter pull-right" id="filterApproveUserName" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="10%"> 所在组织 <a class="icon-filter pull-right" id="filterTheOrg" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="15%">用途说明</th> <th width="10%" class="text-center"><span>申请金额（元）</span></th> <th width="10%"> 审批人 </th> <th width="10%"> 审批时间  </th>  <th width="10%" class="sorting_desc" data-action="sort" data-sort-type="allocationDate"> 拨款时间 <a class="icon-filter pull-right" id="filterAllocationDate" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> </tr> </thead> <tbody> ';
$each(myDataList,function(p,$index){
$out+=' <tr class="curp" data-action="openShowExp" i="';
$out+=$escape($index);
$out+='" versionNum="';
$out+=$escape(p.versionNum);
$out+='"> <td>';
$out+=$escape($index+1+pageIndex*10);
$out+='</td> <td>';
$out+=$escape(p.expNo);
$out+='</td> <!--<td> ';
if(p.type==1){
$out+=' 报销申请 ';
}else{
$out+=' 费用申请 ';
}
$out+=' </td>--> <td>';
$out+=$escape(_momentFormat(p.expDate,'YYYY/MM/DD'));
$out+='</td> <td>';
$out+=$escape(p.userName);
$out+='</td> <td>';
$out+=$escape(p.departName);
$out+='</td> <td>';
$out+=$escape(p.expUse);
$out+='</td> <td class="text-right"> <span class="td-span-pr">';
$out+=$escape(_expNumberFilter(p.expSumAmount));
$out+='</span> </td> <td>';
$out+=$escape(p.auditPersonName);
$out+='</td> <td>';
$out+=$escape(_momentFormat(p.approveDate,'YYYY/MM/DD'));
$out+='</td> <!--<td> ';
if(p.approveStatus==1){
$out+=' <a type="button" class="btn-u btn-u-xs rounded" data-action="agreeToGrant" data-id="';
$out+=$escape(p.id);
$out+='">同意拨款</a> ';
}else if(p.approveStatus==6){
$out+=' 已拨款 ';
}
$out+=' </td>--> <td> ';
if(p.allocationDate!=null && p.allocationDate!=''){
$out+=' ';
$out+=$escape(_momentFormat(p.allocationDate,'YYYY/MM/DD'));
$out+=' ';
}else if(p.approveStatus==1 && p.role!=null && p.role.financialAllocation==1){
$out+=' <a type="button" class="btn btn-primary btn-xs rounded" data-action="agreeToGrant" data-id="';
$out+=$escape(p.id);
$out+='">同意拨款</a> ';
}else{
$out+=' 待拨款 ';
}
$out+=' ';
if(p.role!=null && p.role.financialRecall==1){
$out+=' <a type="button" class="btn btn-default btn-xs rounded" data-action="sendBack" data-id="';
$out+=$escape(p.id);
$out+='">退回</a> ';
}
$out+=' </td> </tr> ';
});
$out+=' ';
if(myDataList==null || myDataList.length==0){
$out+=' <tr class="no-data"> <td colspan="10" align="center"> <div class="text-center"> <img src="';
$out+=$escape(rootPath);
$out+='/assets/img/default/without_exp.png"> </div> <span style="color:#4765a0">暂无汇总内容</span> </td> </tr> ';
}
$out+=' </tbody> </table> ';
if(myDataList!=null && myDataList.length!=0){
$out+=' <div class="row"> <div class="col-md-6"> &nbsp;&nbsp;总申请金额：<span style="color:#ff756d;">';
$out+=$escape(_expNumberFilter(expSumAmount));
$out+='</span>元 &nbsp;&nbsp;总拨款金额：<span style="color:#ff756d;">';
$out+=$escape(_expNumberFilter(financialAllocationSumAmount));
$out+='</span>元 </div> </div> ';
}
return new String($out);
});/*v:1*/
template('m_finance/m_expTypeSelect',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,expTypeList=$data.expTypeList,value=$data.value,i=$data.i,$escape=$utils.$escape,item=$data.item,d=$data.d,$out='';$out+='<div class="m_expTypeSelect"> <input type="text" class="form-control curp bg-none" readonly="readonly" id="collectExpType" name="collectExpType" data-action="collectExpType" placeholder="报销类别"> <div class=" typeNameArea hide pt-absolute" style="z-index: 99;min-width: 596px;"> <div class="thumbnail clearfix click p-sm"> <div class="clearBtn"> <a href="javascript:void(0);" data-action="deleteAll" class="btn-u btn-u-red btn-u-xs rounded">清除</a> </div> ';
$each(expTypeList,function(value,i){
$out+=' <div class="items col-lg-2 text-center no-padding"> <a href="javascript:void(0);" class="click firstItem color-dark curp" data-action="firstItem" name="';
$out+=$escape(value.parent.name);
$out+='" id="';
$out+=$escape(value.parent.id);
$out+='">';
$out+=$escape(value.parent.name);
$out+='</a> <hr/> ';
$each(value.child,function(item,d){
$out+=' <div class="childItem margin-bottom-5"> <a href="javascript:void(0);" data-action="childItem" parent-name="';
$out+=$escape(value.parent.name);
$out+='" name="';
$out+=$escape(item.name);
$out+='" id="';
$out+=$escape(item.id);
$out+='" class="color-dark curp">';
$out+=$escape(item.name);
$out+='</a> </div> ';
});
$out+=' </div> ';
});
$out+=' <div class="items col-lg-2 text-center no-padding"><a class="click firstItem color-dark " >&nbsp;</a><hr/></div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_finance/m_expTypeSetting',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,expTypeList=$data.expTypeList,mainE=$data.mainE,i=$data.i,$escape=$utils.$escape,ch=$data.ch,j=$data.j,$out='';$out+='<div class="ibox ibox_min_height">  <div class="ibox-content no-padding"> <form id="expTypeSetting"> <fieldset id="expTypeMainContent"> <section class="categoryListOBox"> ';
$each(expTypeList,function(mainE,i){
$out+=' <div class="col-md-12" style="padding:10px 0 10px;"> <div class="row"> <div class="col-24-md-offset-1 col-24-md-22"> <div class="headline"><h2>';
$out+=$escape(mainE.parent.name);
$out+='</h2></div> </div> </div> <div class="row"> <div class="col-24-md-offset-4 col-24-md-16 "> <div class="row"> ';
$each(mainE.child,function(ch,j){
$out+=' <div class="col-md-12 "> <div class="row margin-bottom-10"> <div class="col-md-2"> <strong>';
$out+=$escape(ch.name);
$out+='</strong> </div> <div class="col-md-10"> ';
$out+=$escape(ch.expTypeMemo);
$out+=' </div> </div> </div> ';
});
$out+=' <div class="col-md-12 "> <div class="row"> <div class="col-md-3" style="padding: 8px 13px;"> <a href="javascript:void(0)" class="editExpButton roleControl" roleCode="sys_finance_type" flag="2" parent_name="';
$out+=$escape(mainE.parent.name);
$out+='" data-action="editButton';
$out+=$escape(i);
$out+='" style="color:#4765a0">修改';
$out+=$escape(mainE.parent.name);
$out+='>></a> </div> </div> </div> </div> </div> </div> </div> ';
});
$out+=' </section> </fieldset> </form> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_finance/m_expTypeSetting_edit',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,expTypeData=$data.expTypeData,$each=$utils.$each,value=$data.value,i=$data.i,$out='';$out+='<section class="categorySettingOBox"> <div class="col-md-12 margin-bottom-10" style="padding:10px 0 10px;"> <div class="row"> <div class="col-24-md-offset-1 col-24-md-22"> <div class="headline"><h2>';
$out+=$escape(expTypeData.parent.name);
$out+='</h2></div> </div> </div> <div class="row"> <div class="col-24-md-offset-1 col-24-md-22"> <table class="table"> <thead> <tr> <th width="20%">类别名称</th> <th width="70%">类别描述</th> <th width="10%">操作</th> </tr> </thead> <tbody> ';
$each(expTypeData.child,function(value,i){
$out+=' <tr class="categoryInput out-box" id="';
$out+=$escape(value.id);
$out+='"> <td> <input class="form-control" type="text" id="categoryName" name="categoryName" value="';
$out+=$escape(value.name);
$out+='"/> </td> <td> <input class="form-control" type="text" id="categoryMemo" name="categoryMemo" value="';
$out+=$escape(value.expTypeMemo);
$out+='" /> </td> <td> <div class="btn_del" style="padding-top: 5px;"> ';
if(!(expTypeData.parent.name=='其他费用' && i==0)){
$out+=' <a class="btn-u btn-u-sm btn-u-red rounded" data-action="categoryDelete" href="javascript:void(0);"><i class="fa fa-close"></i> </a> ';
}
$out+=' </div> </td> </tr> ';
});
$out+=' <tr class="addList"> <td align="center" colspan="3"> <div class="call-action-v1 call-action-v1-boxed rounded tag-box-v4 curp" data-action="addList" > <div class="call-action-v1-box margin-bottom-10 margin-top-10"> <div class="call-action-v1-in text-center color-dark-blue"> <a href="javascript:void(0);" style="cursor: pointer;"><i class="fa fa-plus fa-2x"></i></a> </div> </div> </div> </td> </tr> </tbody> </table> </div> </div>         </div> </section> <div class="footTools"> <div class="col-24-md-offset-1 col-24-md-22 text-right"> <a class="btn btn-default rounded" data-action="cancelEditType">返回</a> <a class="btn btn-primary rounded" data-action="saveEditType">保存</a> </div> </div>';
return new String($out);
});/*v:1*/
template('m_finance/m_feeEntry','<div class="ibox m_feeEntry no-margin"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 财务管理 </li> <li class="active fa fa-angle-right"> <strong>费用录入</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding" > <div class="no-margin p-sm breadcrumb-box"> <form class="form-inline"> <div class="form-group z-index-1"> <label>当前组织：</label> <div class="btn-group" id="selectOrg"> </div> </div> </form> </div> <div class="col-24-sm-8 col-24-md-8 col-24-lg-6 no-padding" id="left-box"> </div> <div class="col-24-sm-16 col-24-md-16 col-24-lg-18 no-padding " id="right-box"> </div> </div> </div> ');/*v:1*/
template('m_finance/m_feeEntry_content',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_momentFormat=$helpers._momentFormat,expFixedData=$data.expFixedData,_expNumberFilter=$helpers._expNumberFilter,$each=$utils.$each,p=$data.p,pi=$data.pi,s=$data.s,si=$data.si,$out='';$out+='<div class="text-center border-bottom date-chose-box"> <span class="f-s-20 pull-left m-l-md">';
$out+=$escape(_momentFormat(expFixedData.expDate,'YYYY年MM月'));
$out+='</span> <span class="pull-right m-r-md">支出：<span class="f-s-20">';
$out+=$escape(_expNumberFilter(expFixedData.expAmount));
$out+='</span> 元</span> <span class="pull-right m-r-md">收入：<span class="f-s-20">';
$out+=$escape(_expNumberFilter(expFixedData.incomeAmount));
$out+='</span> 元</span> <div class="clearfix"></div> </div> <div class="p-m"> <form id="expFixedForm"> <table class="table table-bordered table-responsive"> <thead> <tr> <th colspan="2">类别名称</th> <th>金额（元）</th> </tr> </thead> <tbody> ';
if(expFixedData.fixedList!=null && expFixedData.fixedList.length>0){
$out+=' ';
$each(expFixedData.fixedList,function(p,pi){
$out+=' ';
if(p.detailList!=null && p.detailList.length>0){
$out+=' ';
$each(p.detailList,function(s,si){
$out+=' <tr> ';
if(si==0){
$out+=' <td rowspan="';
$out+=$escape(p.detailList.length);
$out+='" class="v-middle">';
$out+=$escape(p.expTypeName);
$out+='</td> ';
}
$out+=' <td class="v-middle">';
$out+=$escape(s.expTypeName);
$out+='</td> <td class="v-middle"> <input class="form-control input-sm" type="text" name="expAmount" maxlength="50" value="';
$out+=$escape(s.expAmount);
$out+='" id="';
$out+=$escape(s.id);
$out+='" data-exptype="';
$out+=$escape(s.expType);
$out+='" data-parent-name="';
$out+=$escape(p.expTypeName);
$out+='" data-name="';
$out+=$escape(s.expTypeName);
$out+='" data-seq="';
$out+=$escape(s.seq);
$out+='"> </td> </tr> ';
});
$out+=' ';
}
$out+=' ';
});
$out+=' ';
}
$out+='  </tbody> </table> </form> <div class="text-right"> <a class="btn btn-primary" data-action="saveExpFixed">保存</a> </div> </div>';
return new String($out);
});/*v:1*/
template('m_finance/m_feeEntry_monthList',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,selectedYear=$data.selectedYear,currentYear=$data.currentYear,$each=$utils.$each,monthList=$data.monthList,m=$data.m,mi=$data.mi,currentExpDate=$data.currentExpDate,_momentFormat=$helpers._momentFormat,_expNumberFilter=$helpers._expNumberFilter,$out='';$out+='<div class="text-center f-s-20 border-bottom date-chose-box"> <a class="m-r-md" data-action="prevYear"><i class="fa fa-angle-left"></i></a> <span data-type="selectedYear">';
$out+=$escape(selectedYear);
$out+='</span> ';
if(selectedYear<currentYear){
$out+=' <a class="m-l-md" data-action="nextYear"><i class="fa fa-angle-right"></i></a> ';
}else{
$out+=' <span class="m-l-md fc-ccc"><i class="fa fa-angle-right"></i></span> ';
}
$out+=' </div> ';
$each(monthList,function(m,mi){
$out+=' <div class="month-menu-box curp ';
$out+=$escape(currentExpDate==null?(mi==0?'active':''):(currentExpDate==m.expDate?'active':''));
$out+='" data-action="getExpFixed" data-expdate="';
$out+=$escape(m.expDate);
$out+='"> <div class="col-md-6 month"> ';
$out+=$escape(_momentFormat(m.expDate,'MM月'));
$out+=' <div class="user-name"> 财务人员： ';
if(m.userName!=null && m.userName!=''){
$out+=' ';
$out+=$escape(m.userName);
$out+=' ';
}else{
$out+=' -- ';
}
$out+=' </div> </div> <div class="col-md-6 detail text-right"> <div class="income"> 收入：<span>';
$out+=$escape(_expNumberFilter(m.incomeAmount));
$out+='</span> 元 </div> <div class="expenditure"> 支出：<span>';
$out+=$escape(_expNumberFilter(m.expAmount));
$out+='</span> 元 </div> </div> </div> ';
});
$out+=' ';
return new String($out);
});/*v:1*/
template('m_finance/m_feeEntry_settings','<div class="m_feeEntry_settings" > <form class="form-inline m-md"> <div class="form-group z-index-1"> <label class="">当前组织：</label> <div class="btn-group" id="selectOrg"> </div> </div> <div class="form-group z-index-1"> <label class="m-l-xs">收支筛选：</label> <select class="form-control" name="payType" style="padding: 2px 12px;height: 30px;width: 70px;" data-action="categoryTypeSelect"> <option value="">全部</option> <option value="1">收入</option> <option value="2">支出</option> </select> </div> </form>  <div class="m-md" id="right-box"> </div> </div> ');/*v:1*/
template('m_finance/m_feeEntry_settings_add','<style> body>span.select2-container{ z-index: 1125 !important; } </style> <form class="addFeeFieldForm"> <div class="p-h-m"> <label class="col-md-3 no-pd-right m-t-xs">类型名称：</label> <div class="col-md-9"> <input class="form-control input-sm" type="text" name="feeField" placeholder="请输入类型名称"/> </div> </div>  </form>');/*v:1*/
template('m_finance/m_feeEntry_settings_content',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,feeEntryFieldList=$data.feeEntryFieldList,f=$data.f,i=$data.i,$escape=$utils.$escape,c=$data.c,ci=$data.ci,$out='';$out+='<div class="m_feeEntry_settings" > <table class="table table-bordered table-responsive"> <thead> <tr> <th width="20%">收支分类</th> <th width="40%">收支分类子项</th> <th width="30%">数据来源</th> </tr> </thead> <tbody> ';
$each(feeEntryFieldList,function(f,i){
$out+=' <tr> <td rowspan="';
$out+=$escape(f.childList!=null && f.childList.length>0 ?f.childList.length+1:'');
$out+='" class="gray-bg v-middle"> <div class="check-box"> <label class="i-checks pull-left"> <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(f.code);
$out+='" data-pid="';
$out+=$escape(f.pid);
$out+='" data-id="';
$out+=$escape(f.id);
$out+='"/> <span class="i-checks-span">';
$out+=$escape(f.name);
$out+='</span> </label> </div> </td> <td> ';
if(f.childList!=null && f.childList.length>0){
$out+=' <div class="check-box"> <label class="i-checks pull-left"> ';
if(f.childList[0].showStatus == 1){
$out+=' ';
if(f.childList[0].disabled==true){
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(f.childList[0].code);
$out+='" data-pid="';
$out+=$escape(f.childList[0].pid);
$out+='" data-id="';
$out+=$escape(f.childList[0].id);
$out+='" checked disabled/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(f.childList[0].code);
$out+='" data-pid="';
$out+=$escape(f.childList[0].pid);
$out+='" data-id="';
$out+=$escape(f.childList[0].id);
$out+='" checked/> ';
}
$out+=' ';
}else{
$out+=' ';
if(f.childList[0].disabled==true){
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(f.childList[0].code);
$out+='" data-pid="';
$out+=$escape(f.childList[0].pid);
$out+='" data-id="';
$out+=$escape(f.childList[0].id);
$out+='" disabled/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(f.childList[0].code);
$out+='" data-pid="';
$out+=$escape(f.childList[0].pid);
$out+='" data-id="';
$out+=$escape(f.childList[0].id);
$out+='"/> ';
}
$out+=' ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(f.childList[0].name);
$out+='</span> </label> </div> ';
if(f.childList[0].isDefaulted !=1){
$out+=' <a href="javascript:void(0);" data-action="editName" data-id="';
$out+=$escape(f.childList[0].id);
$out+='" data-pid="';
$out+=$escape(f.id);
$out+='" data-name="';
$out+=$escape(f.childList[0].name);
$out+='" data-category-type="';
$out+=$escape(f.childList[0].categoryType);
$out+='" style="display: none;"> <i class="ic-edit"></i> </a> <a href="javascript:void(0);" data-action="delFeeField" data-id="';
$out+=$escape(f.childList[0].id);
$out+='" style="display: none;"> <i class="glyphicon glyphicon-remove text-danger"></i> </a> ';
}
$out+=' ';
}else{
$out+=' <a class="btn btn-link btn-add" data-action="addFeeField" title="添加设计任务" data-id="';
$out+=$escape(f.id);
$out+='"><i class="fa fa-plus"></i></a> ';
}
$out+=' </td> <td> ';
if(f.childList!=null && f.childList.length>0){
$out+=' ';
$out+=$escape(f.childList[0].dataSource);
$out+=' ';
}
$out+=' </td> </tr> ';
if(f.childList!=null && f.childList.length>0){
$out+=' ';
$each(f.childList,function(c,ci){
$out+=' ';
if(ci>0){
$out+=' <tr class=""> <td> <div class="check-box"> <label class="i-checks pull-left"> ';
if(c.showStatus==1){
$out+=' ';
if(c.disabled==true){
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(c.code);
$out+='" data-pid="';
$out+=$escape(c.pid);
$out+='" data-id="';
$out+=$escape(c.id);
$out+='" checked disabled/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(c.code);
$out+='" data-pid="';
$out+=$escape(c.pid);
$out+='" data-id="';
$out+=$escape(c.id);
$out+='" checked/> ';
}
$out+=' ';
}else{
$out+=' ';
if(c.disabled==true){
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(c.code);
$out+='" data-pid="';
$out+=$escape(c.pid);
$out+='" data-id="';
$out+=$escape(c.id);
$out+='" disabled/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" data-code="';
$out+=$escape(c.code);
$out+='" data-pid="';
$out+=$escape(c.pid);
$out+='" data-id="';
$out+=$escape(c.id);
$out+='"/> ';
}
$out+=' ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(c.name);
$out+='</span> </label> </div> ';
if(c.isDefaulted !=1){
$out+=' <a href="javascript:void(0);" data-action="editName" data-id="';
$out+=$escape(c.id);
$out+='" data-pid="';
$out+=$escape(f.id);
$out+='" data-name="';
$out+=$escape(c.name);
$out+='" data-category-type="';
$out+=$escape(c.categoryType);
$out+='" style="display: none;"> <i class="ic-edit"></i> </a> <a href="javascript:void(0);" data-action="delFeeField" data-id="';
$out+=$escape(c.id);
$out+='" style="display: none;"> <i class="glyphicon glyphicon-remove text-danger"></i> </a> ';
}
$out+=' </td> <td> ';
$out+=$escape(c.dataSource);
$out+=' </td> </tr> ';
}
$out+=' ';
});
$out+=' <tr> <td> <a class="btn btn-link btn-add" data-action="addFeeField" title="添加设计任务" data-id="';
$out+=$escape(f.id);
$out+='"><i class="fa fa-plus"></i></a> </td> <td></td> </tr> ';
}
$out+=' ';
});
$out+=' </tbody> </table> </div> ';
return new String($out);
});/*v:1*/
template('m_finance/m_finance_basic_settings',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,companyDataList=$data.companyDataList,c=$data.c,i=$data.i,$escape=$utils.$escape,_expNumberDecimalFilter=$helpers._expNumberDecimalFilter,$out='';$out+='<div class="m_finance_basic_settings p-m" > <div class="fc-000 font-bold"> <p>说明：</p> <P>1、余额初始值：设置日期：年、月由用户确定，日期由系统默认为用户确定月份的最后一天。</P> <p>2、余额初始日期：对应于设定日期各团队的账面资金。设置日期之后相应的各项财务数据将在此基础上产生，避免由于录入历史财务数据的不完整带来的各项财务数据的不准确。</p> <p>3、最低余额：确保各团队正常运作的最低余额（账面可流动资金），根据企业的实际需求可设置可不设置。</p> </div> <table class="table table-bordered table-responsive"> <thead> <tr> <th width="20%">组织名称</th> <th width="17%" class="text-right">录入余额初始值（元）</th> <th width="16%" class="text-right">录入余额初始日期</th> <th width="17%" class="text-right">设置最低余额（元）</th> <th width="10%" class="text-right">当前余额（元）</th>  <th width="10%" class="text-right">上月费用录入（元）</th> </tr> </thead> <tbody> ';
$each(companyDataList,function(c,i){
$out+=' <tr data-i="';
$out+=$escape(i);
$out+='" data-company-id="';
$out+=$escape(c.companyId);
$out+='"> <td class="fc-000 font-bold"> ';
$out+=$escape(c.companyName);
$out+=' </td> <td class="text-right"> <a href="javascript:void(0);" data-action="xEditable" data-action-type="initialBalance" data-value="';
$out+=$escape(c.initialBalance);
$out+='"> ';
$out+=$escape(c.initialBalance==null||c.initialBalance==''?'未设置':_expNumberDecimalFilter(c.initialBalance));
$out+=' </a> </td> <td class="text-right"> <a href="javascript:void(0);" data-action="editDate" data-action-type="';
$out+=$escape(i);
$out+='" class="editable editable-click" > ';
$out+=$escape(c.setBalanceDate==null||c.setBalanceDate==''?'未设置':c.setBalanceDate);
$out+=' </a> </td> <td class="text-right"> <a href="javascript:void(0);" data-action="xEditable" data-action-type="lowBalance" data-value="';
$out+=$escape(c.lowBalance);
$out+='"> ';
$out+=$escape(c.lowBalance==null||c.lowBalance==''?'未设置':_expNumberDecimalFilter(c.lowBalance));
$out+=' </a> </td> <td class="text-right">';
$out+=$escape(_expNumberDecimalFilter(c.currentBalance));
$out+='</td> <!--<td class="text-right">';
$out+=$escape(_expNumberDecimalFilter(c.currentIncome));
$out+='</td>--> <td class="text-right">';
$out+=$escape(_expNumberDecimalFilter(c.lastMonthFixFee));
$out+='</td> </tr> ';
});
$out+=' </tbody> </table> </div> ';
return new String($out);
});/*v:1*/
template('m_finance/m_finance_settings_menu',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,role=$data.role,$out='';$out+='<div class="ibox no-margin"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 财务设置 </li> <li class="active fa fa-angle-right"> <strong>收支类别设置</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> <ul class="secondary-menu-ul pull-right">  ';
if(role.expCategorySet==1){
$out+=' <li id="feeEntrySettings" class="active" ><a>收支类别设置</a></li> ';
}
$out+=' ';
if(role.baseFinanceDataSet==1){
$out+=' <li id="financeBasicSettings" ><a>基础财务数据设置</a></li> ';
}
$out+=' <!--';
if(role.shareCostSet==1){
$out+=' <li id="costSharingSettings" ><a>费用分摊项设置</a></li> ';
}
$out+='--> </ul> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div class="row"> <div class="col-md-12" id="content-box"> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_finance/m_reimbursementSummary',' <div class="ibox ibox_min_height m_reimbursementSummary"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 审批管理 </li> <li class="active fa fa-angle-right"> <strong>报销汇总</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm">  </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div class="" id="summary"> <input type="hidden" name="approveStatus" value=""/> <input type="hidden" name="myExpStartDate" value=""/> <input type="hidden" name="myExpEndDate" value=""/> <input type="hidden" name="expNo" value=""/> <input type="hidden" name="expType" value=""/> <input type="hidden" name="approveStartDate" value=""/> <input type="hidden" name="approveEndDate" value=""/> <input type="hidden" name="approveUserName" value=""/> <input type="hidden" name="allocationStatus" value=""/> <input type="hidden" name="allocationStartDate" value=""/> <input type="hidden" name="allocationEndDate" value=""/> <input type="hidden" name="applayCompanyName" value=""/> <input type="hidden" name="sortAllocationDate" value=""/>  <section class="mySummaryListBox"> <div class="row"> <div class="col-md-12" id="mySummaryListData"></div> <div class="col-md-12 padding-right-25"> <div id="mySummary-pagination-container" class="m-pagination pull-right"></div> </div> </div> </section>  </div> </div> </div> ');/*v:1*/
template('m_finance/m_reimbursementSummary_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,myDataList=$data.myDataList,p=$data.p,$index=$data.$index,$escape=$utils.$escape,pageIndex=$data.pageIndex,_momentFormat=$helpers._momentFormat,_expNumberFilter=$helpers._expNumberFilter,rootPath=$data.rootPath,expSumAmount=$data.expSumAmount,financialAllocationSumAmount=$data.financialAllocationSumAmount,$out='';$out+='<style> .table-hover>tbody>tr.no-data:hover { background-color: transparent; } </style> <table class="table table-hover table-bordered table-responsive dataTable"> <thead> <tr> <th width="6%">序号</th> <th width="10%"> 编号 <a class="icon-filter pull-right" id="filterExpNo" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th>  <th width="10%"> 申请时间 <a class="icon-filter pull-right" id="filterExpDate" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="10%"> 申请人 <a class="icon-filter pull-right" id="filterApproveUserName" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="10%"> 所在组织 <a class="icon-filter pull-right" id="filterTheOrg" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="15%">用途说明</th> <th width="10%" class="text-center"><span class="">申请金额（元）</span></th> <th width="10%"> 审批人 </th> <th width="10%"> 审批时间  </th>  <th width="10%" class="sorting_desc" data-action="sort" data-sort-type="allocationDate"> 拨款时间 <a class="icon-filter pull-right" id="filterAllocationDate" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> </tr> </thead> <tbody> ';
$each(myDataList,function(p,$index){
$out+=' <tr class="curp" data-action="openShowExp" i="';
$out+=$escape($index);
$out+='" versionNum="';
$out+=$escape(p.versionNum);
$out+='"> <td>';
$out+=$escape($index+1+pageIndex*10);
$out+='</td> <td>';
$out+=$escape(p.expNo);
$out+='</td> <!--<td> ';
if(p.type==1){
$out+=' 报销申请 ';
}else{
$out+=' 费用申请 ';
}
$out+=' </td>--> <td>';
$out+=$escape(_momentFormat(p.expDate,'YYYY/MM/DD'));
$out+='</td> <td>';
$out+=$escape(p.userName);
$out+='</td> <td>';
$out+=$escape(p.departName);
$out+='</td> <td>';
$out+=$escape(p.expUse);
$out+='</td> <td class="text-right"> <span class="td-span-pr">';
$out+=$escape(_expNumberFilter(p.expSumAmount));
$out+='</span> </td> <td>';
$out+=$escape(p.auditPersonName);
$out+='</td> <td>';
$out+=$escape(_momentFormat(p.approveDate,'YYYY/MM/DD'));
$out+='</td> <!--<td> ';
if(p.approveStatus==1){
$out+=' <a type="button" class="btn-u btn-u-xs rounded" data-action="agreeToGrant" data-id="';
$out+=$escape(p.id);
$out+='">同意拨款</a> ';
}else if(p.approveStatus==6){
$out+=' 已拨款 ';
}
$out+=' </td>--> <td> ';
if(p.allocationDate!=null && p.allocationDate!=''){
$out+=' ';
$out+=$escape(_momentFormat(p.allocationDate,'YYYY/MM/DD'));
$out+=' ';
}else if(p.approveStatus==1 && p.role!=null && p.role.financialAllocation==1){
$out+=' <a type="button" class="btn btn-primary btn-xs rounded" data-action="agreeToGrant" data-id="';
$out+=$escape(p.id);
$out+='">同意拨款</a> ';
}else{
$out+=' 待拨款 ';
}
$out+=' ';
if(p.role!=null && p.role.financialRecall==1){
$out+=' <a type="button" class="btn btn-default btn-xs rounded" data-action="sendBack" data-id="';
$out+=$escape(p.id);
$out+='">退回</a> ';
}
$out+=' </td> </tr> ';
});
$out+=' ';
if(myDataList==null || myDataList.length==0){
$out+=' <tr class="no-data"> <td colspan="10" align="center"> <div class="text-center"> <img src="';
$out+=$escape(rootPath);
$out+='/assets/img/default/without_exp.png"> </div> <span style="color:#4765a0">暂无汇总内容</span> </td> </tr> ';
}
$out+=' </tbody> </table> ';
if(myDataList!=null && myDataList.length!=0){
$out+=' <div class="row"> <div class="col-md-6"> &nbsp;&nbsp;总申请金额：<span style="color:#ff756d;">';
$out+=$escape(_expNumberFilter(expSumAmount));
$out+='</span>元 &nbsp;&nbsp;总拨款金额：<span style="color:#ff756d;">';
$out+=$escape(_expNumberFilter(financialAllocationSumAmount));
$out+='</span>元 </div> </div> ';
}
return new String($out);
});/*v:1*/
template('m_finance/m_reimbursement_add',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,reimburseObj=$data.reimburseObj,$out='';$out+='<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 个人报销 </li> <li class="active fa fa-angle-right"> <strong>我要报销</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content" id="toExpApplication"> <form class="tessExpenseBox" style="overflow-x:hidden;"> <fieldset> <section> <div class="headline"> <h2>基础内容</h2> </div> <div class="row"> <div class="form-group col-md-12"> <label for="receiptType">单据类型<span class="color-red">*</span></label> <select id="receiptType" name="" class="form-control curp p-r-24"> <option value="1">报销申请</option> <option value="2">费用申请</option> </select> </div> <div class="form-group col-md-12"> <label for="auditPerson">审批人<span class="color-red">*</span></label> <input id=\'auditPerson\' type="text" class="getClickFun form-control" value="';
$out+=$escape(reimburseObj.auditPersonName);
$out+='" data-action="auditPerson" userid="';
$out+=$escape(reimburseObj.auditPerson);
$out+='" id="auditPerson" readonly="readonly" placeholder="选择审批人" style="background-color: #fff;"> </div> <div class="form-group col-md-12 margin-bottom-20"> <label for="remark">备注</label> <input type="text" id="remark" class="form-control" value="';
$out+=$escape(reimburseObj.remark);
$out+='" maxlength="1000"> </div> <div class="col-md-12 margin-bottom-10"> <span id="showFileLoading"> </span> <button class="getClickFun btn-u btn-u-sm btn-u-dark-blue rounded" id="fileUpload" data-action="fileUpload" type="button"><i class="fa fa-upload"></i>&nbsp;上传附件 </button> </div> <div class="col-md-12 margin-bottom-10"> <span class="uploadmgrContainer"> </span> </div> </div> </section> <section> <div class="headline"> <h2>报销条目</h2> </div> <table class="table table-responsive m-b-none" id="reimburseTable"> <thead> <tr> <th width="18%">报销金额<span class="color-red">*</span>（元）</th> <th width="24%">报销类别<span class="color-red">*</span></th> <th width="30%">用途说明<span class="color-red">*</span></th> <th width="20%">关联项目</th> <th width="8%">操作</th> </tr> </thead> <tbody> <tr> <td colspan="5" class="text-center border-no-t"> <div class="call-action-v1 rounded call-action-v1-boxed tag-box-v4 curp getClickFun" data-action="addExpItem"> <div class="call-action-v1-box margin-bottom-10 margin-top-10"> <div class="call-action-v1-in text-center"> <a href="javascript:void(0);"><i class="fa fa-plus fa-2x color-dark-blue"></i> </a> </div> </div> </div> </td> </tr> <tr> <td colspan="5"> <div class="text-right"> <p>合计：<i class="fa fa-jpy"></i> <span id="expAmount"></span>元</p> </div> </td> </tr> </tbody> </table> </section> </fieldset> <div class="footTools"></div> </form> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_finance/m_showExpDetailDialog',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,typeStr=$data.typeStr,$each=$utils.$each,myExpDetails=$data.myExpDetails,item=$data.item,j=$data.j,file=$data.file,fastdfsUrl=$data.fastdfsUrl,expNo=$data.expNo,each=$data.each,i=$data.i,$out='';$out+='<style> .MyExpDetailsCheckExpenseOBox .float_Mask{background-color: white;padding: 21px 3px;position: absolute;top: 19px;left: 30%; } .MyExpDetailsCheckExpenseOBox.ibox-content .timeline-v2.timeline-me>li .cbp_tmtime{width: 40%;} .MyExpDetailsCheckExpenseOBox.ibox-content .timeline-v2.timeline-me:before{left: 32%} .MyExpDetailsCheckExpenseOBox.ibox-content .timeline-v2.timeline-me>li .cbp_tmicon{top: 5px;left: 32%;} .MyExpDetailsCheckExpenseOBox.ibox-content .timeline-v2.timeline-me>li .cbp_tmlabel{margin: 0 0 20px 40%;} </style> <div class="ibox m-b-xs"> <div class="ibox-content MyExpDetailsCheckExpenseOBox no-pd-bottom" style="max-height:700px;overflow-y: auto"> <fieldset style="padding-top:5px;"> <div class="headline m-b-sm"> <h2>';
$out+=$escape(typeStr);
$out+='状态</h2> </div> <div class="row"> <div class="col-md-12"> <div class="panel-body"> <ul class="timeline-v2 timeline-me"> ';
$each(myExpDetails.auditList,function(item,j){
$out+=' <li style="height: 60px;"> <time class="cbp_tmtime" datetime=""> <span style="top:0;font-size: 14px; ';
$out+=$escape((j==myExpDetails.auditList.length-1)?'color: #FF5722;':'');
$out+='">';
$out+=$escape(item.userName);
$out+='</span> <span style="top:0;font-size: 13px;">';
$out+=$escape(item.companyName);
$out+='</span> </time> <i class="cbp_tmicon rounded-x hidden-xs" style=" ';
$out+=$escape((j==myExpDetails.auditList.length-1)?'background: #FF5722;':'');
$out+='"></i> <div class="cbp_tmlabel"> <h2 style="font-size: 14px;padding:0; ';
$out+=$escape((j==myExpDetails.auditList.length-1)?'color:#FF5722;':'');
$out+='"> ';
$out+=$escape(item.approveStatusName=="待审核"?"待审批":item.approveStatusName);
$out+=' &nbsp;&nbsp;&nbsp;<span>';
$out+=$escape(item.expDate);
$out+='</span> </h2> ';
if(item.approveStatusName=='发起申请'){
$out+=' <p style="font-size: 13px;word-break: break-all;color:#999;line-height: 20px;"> ';
$out+=$escape(typeStr);
$out+='备注：';
$out+=$escape(item.remark);
$out+='</p> ';
}
$out+=' ';
if(item.approveStatusName=='退回'){
$out+=' <p style="font-size: 13px;word-break: break-all;color:#999;line-height: 20px;"> 退回原因：';
$out+=$escape(item.remark);
$out+='</p> ';
}
$out+=' </div> ';
if(item.approveStatusName.indexOf('完成')>-1){
$out+=' <span class="float_Mask"></span> ';
}
$out+=' </li> ';
});
$out+=' </ul> </div> </div> </div> ';
if(myExpDetails.expAttachEntityList&&myExpDetails.expAttachEntityList.length>0){
$out+=' <div class="headline m-b"> <h2>相关票据</h2> </div> <div class="row margin-bottom-30"> <div class="col-md-12 p-w-m"> ';
$each(myExpDetails.expAttachEntityList,function(file,j){
$out+=' <span class="label m-r-xs dp-inline-block" style="background: #ecf0f1;padding: 5px 10px;"> <a class="curp m-l-xs" href="';
$out+=$escape(fastdfsUrl);
$out+=$escape(file.fileGroup);
$out+='/';
$out+=$escape(file.filePath);
$out+='" target="_blank"> <i class="fa fa-file-image-o"></i>&nbsp;';
$out+=$escape(file.fileName);
$out+=' </a> </span> ';
});
$out+=' </div> </div> ';
}
$out+=' <div class="headline m-b-sm"> <h2>';
$out+=$escape(typeStr);
$out+='信息</h2> </div> <div class="row"> <div class="col-md-12"> <label>';
$out+=$escape(typeStr);
$out+='编号：</label> <b>';
$out+=$escape(expNo);
$out+='</b> </div> <div class="col-md-12"> <table class="table m-b-none"> <thead> <tr> <td class="no-pd-left">';
$out+=$escape(typeStr);
$out+='条目</td> <td>';
$out+=$escape(typeStr);
$out+='金额</td> <td>';
$out+=$escape(typeStr);
$out+='类别</td> <td>用途说明</td> <td class="no-pd-right">关联项目</td> </tr> </thead> <tbody> ';
$each(myExpDetails.detailList,function(each,i){
$out+=' <tr> <td class="no-pd-left">';
$out+=$escape(i+1);
$out+='</td> <td>';
$out+=$escape(each.expAmount);
$out+='</td> <td>';
$out+=$escape(each.expTypeName);
$out+='</td> <td style="word-break: break-all;">';
$out+=$escape(each.expUse);
$out+='</td> <td class="no-pd-right">';
$out+=$escape(each.projectName);
$out+='</td> </tr> ';
});
$out+=' <tr> <td colspan="5"> <div class="pull-right "> <h5>合计金额：<p class="color-red dp-inline-block">';
$out+=$escape(myExpDetails.totalExpAmount);
$out+=' </p>元 </h5> </div> </td> </tr> </tbody> </table> </div> </div> </fieldset> </div> </div>';
return new String($out);
});/*v:1*/
template('m_home/m_metismenu',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<nav class="navbar-default navbar-static-side m_metismenu " role="navigation" style="z-index: 900;"> <div class="sidebar-collapse"> <ul class="nav metismenu" id="side-menu" style="display: block;"> <li class="navbar-minimalize" style=""> <a href="javascript:void(0);" class="svg"> <object class="pull-left" data="';
$out+=$escape(_url('/assets/img/home/workbench.svg'));
$out+='" type="image/svg+xml"></object> <span class="nav-label pull-left">工作台</span> <div class="clearfix"></div>  </a> </li> <li> <a id="addProject" class="svg" href="#/addProject"> <object class="pull-left" data="';
$out+=$escape(_url('/assets/img/home/addProject.svg'));
$out+='" type="image/svg+xml"></object> <span class="nav-label pull-left">项目立项</span> <div class="clearfix"></div> </a> </li> <li class="project-menu-box" id="project-menu-box"> <a id="projectList" class="svg" href="#/"> <object class="pull-left" data="';
$out+=$escape(_url('/assets/img/home/myProjects.svg'));
$out+='" type="image/svg+xml"></object> <span class="nav-label pull-left">我的项目</span> <div class="clearfix"></div> </a> </li> <li> <a id="myTask" class="svg" href="#/myTask"> <object class="pull-left" class="pull-left" data="';
$out+=$escape(_url('/assets/img/home/myTask.svg'));
$out+='" type="image/svg+xml"></object> <span class="nav-label pull-left">我的任务</span> <div class="clearfix"></div> </a> </li> <!--<li> <a id="projectOverview" class="svg" href="javascript:void(0);"> <object class="pull-left" data="';
$out+=$escape(_url('/assets/img/home/projectOverview.svg'));
$out+='" type="image/svg+xml"></object> <span class="nav-label pull-left">项目总览</span> <div class="clearfix"></div> </a> </li>--> <!--<li class="roleControl" roleCode="project_charge_manage" flag="2"> <a class="svg" href="javascript:void(0);"> <object class="pull-left" data="';
$out+=$escape(_url('/assets/img/home/incomeExpenditure.svg'));
$out+='" type="image/svg+xml"></object> <span class="nav-label pull-left">收支总览</span> <span class="fa arrow"></span> <div class="clearfix"></div> </a> <ul class="nav nav-second-level collapse in" > <li><a href="#/paymentsDetail" id="paymentsDetail"><span class="nav-label ">收支明细</span></a></li> <li><a href="#/paymentsStatistics" id="paymentsStatistics"><span class="nav-label ">分类统计</span></a></li> <li><a href="#/profitStatement" id="profitStatement"><span class="nav-label ">利润报表</span></a></li> </ul> </li>--> <li> <a class="svg" href="javascript:void(0);"> <object class="pull-left" data="';
$out+=$escape(_url('/assets/img/home/financialManagement.svg'));
$out+='" type="image/svg+xml"></object> <span class="nav-label pull-left">财务管理</span> <span class="fa arrow"></span> <div class="clearfix"></div> </a> <ul class="nav nav-second-level collapse in" >  <li class="roleControl" roleCode="finance_report" flag="2"><a href="#/paymentsDetail" id="paymentsDetail"><span class="nav-label ">收支明细</span></a></li> <li class="roleControl" roleCode="finance_report" flag="2"><a href="#/paymentsStatistics" id="paymentsStatistics"><span class="nav-label ">分类统计</span></a></li> <li class="roleControl" roleCode="finance_report" flag="2"><a href="#/profitStatement" id="profitStatement"><span class="nav-label ">利润报表</span></a></li> <li class="roleControl" roleCode="sys_finance_type" flag="2"><a href="#/financeSettings" id="financeSettings"><span class="nav-label ">财务设置</span></a></li> <li class="roleControl" roleCode="finance_fixed_edit" flag="2"><a href="#/feeEntry" id="feeEntry"><span class="nav-label ">费用录入</span></a></li> <li class="roleControl" ><a href="#/projectCost" id="projectCost"><span class="nav-label ">项目收支</span></a></li> <li class="roleControl" ><a href="#/invoiceSummary" id="invoiceSummary"><span class="nav-label ">发票汇总</span></a></li> </ul> </li> <li> <a class="svg" href="javascript:void(0);"> <object class="pull-left" data="';
$out+=$escape(_url('/assets/img/home/financeInformation.svg'));
$out+='" type="image/svg+xml"></object> <span class="nav-label pull-left">审批管理</span> <span class="fa arrow"></span> <div class="clearfix"></div> </a> <ul class="nav nav-second-level collapse in" > <li class="roleControl" roleCode="report_exp_static" flag="2"><a href="#/reimbursementSummary" id="reimbursementSummary"><span class="nav-label ">报销统计</span></a></li> <li class="roleControl" roleCode="report_exp_static" flag="2"><a href="#/costSummary" id="costSummary"><span class="nav-label ">费用统计</span></a></li> <li class="roleControl" roleCode="summary_leave" flag="2"><a href="#/leaveSummary" id="leaveSummary"><span class="nav-label ">请假统计</span></a></li> <li class="roleControl" roleCode="summary_leave" flag="2"><a href="#/businessSummary" id="businessSummary"><span class="nav-label ">出差统计</span></a></li> <li class="roleControl" roleCode="summary_leave" flag="2"><a href="#/workingHoursSummary" id="workingHoursSummary"><span class="nav-label ">工时统计</span></a></li> </ul> </li> <li> <a class="svg" href="javascript:void(0);"> <object class="pull-left" data="';
$out+=$escape(_url('/assets/img/home/addressBook.svg'));
$out+='" type="image/svg+xml"></object> <span class="nav-label pull-left">通讯录</span> <span class="fa arrow"></span> <div class="clearfix"></div> </a> <ul class="nav nav-second-level collapse in" > <li><a href="#/orgInfomationShow" id="orgInfomationShow"><span class="nav-label ">组织信息</span></a></li> <li><a href="#/addressBook" id="addressBook"><span class="nav-label ">通讯录</span></a></li> </ul> </li> <li class="roleControl" roleCode="project_edit" flag="2"> <a id="projectArchiving" class="svg" href="#/projectArchiving" > <object class="pull-left" data="';
$out+=$escape(_url('/assets/img/home/projectDocmgr.svg'));
$out+='" type="image/svg+xml"></object> <span class="nav-label pull-left">项目文档</span> <div class="clearfix"></div> </a> </li> </ul> </div> </nav> <div class="clearfix"></div>';
return new String($out);
});/*v:1*/
template('m_historyData/m_historyData',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="ibox ibox-shadow m_historyData"> <div class="ibox-title border-no-t"> <div class="ibox-tools"> <div class="mt-element-step-new" style="margin-bottom: 30px;"> <div class="row step-line"> <div class="mt-step-desc"> </div> <div class="col-md-4 mt-step-col first active" data-step="1"> <div class="mt-step-number bg-white">1</div> <div class="mt-step-title uppercase font-grey-cascade">文件上传</div> <div class="mt-step-content font-grey-cascade">根据模板填充数据并上传</div> </div> <div class="col-md-4 mt-step-col" data-step="2"> <div class="mt-step-number bg-white">2</div> <div class="mt-step-title uppercase font-grey-cascade">数据预览</div> <div class="mt-step-content font-grey-cascade">预览并校验要导入的数据</div> </div> <div class="col-md-4 mt-step-col last" data-step="3"> <div class="mt-step-number bg-white">3</div> <div class="mt-step-title uppercase font-grey-cascade">导入完成</div> <div class="mt-step-content font-grey-cascade">提交最终处理后的数据</div> </div> </div> </div> </div> </div> <div class="ibox-content"> <div class="row step-container" style="margin-bottom: 150px;"> <div class="col-md-12 text-center" style="margin-bottom: 20px;"> <img src="';
$out+=$escape(_url('/assets/img/default/without_exp.png'));
$out+='"> <p style="font-size:24px;">您需要准备好可导入的Excel文档和设置立项组织</p> <select id="selectOrg"></select> </div> <div class="col-md-12 text-center"> <button type="button" class="btn btn-outline btn-warning m-r-md" data-action="downloadTemplate">下载模板</button> <button type="button" class="btn btn-primary" data-action="uploadFile">上传文件</button> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_historyData/m_historyData_step2',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,msg=$data.msg,showSubmit=$data.showSubmit,list=$data.list,$each=$utils.$each,o=$data.o,i=$data.i,p=$data.p,t=$data.t,$out='';$out+='<div class="col-md-12"> <div class="alert alert-warning"> ';
$out+=$escape(msg);
$out+='&nbsp;&nbsp;<a href="javascript:void(0);" data-action="reImport">重新导入</a>';
if(showSubmit===true){
$out+=' <button type="button" class="btn btn-v1-green btn-sm pull-right" data-action="submit" style="position: relative;top:-5px;">提交有效数据</button>';
}
$out+=' </div> </div> <div class="col-md-12"> <table class="footable table table-stripped toggle-arrow-tiny" data-page-size="10"> <thead> <tr> <th data-sort-ignore="true" data-toggle="true">项目编号</th> <th data-sort-ignore="true">项目名称</th> <th data-sort-ignore="true">立项组织</th> <th data-sort-ignore="true">立项人</th> <th data-sort-ignore="true">立项日期</th> <th data-sort-ignore="true" data-hide="all">合同签订日期</th> <th data-sort-ignore="true" data-hide="all">项目地点</th> <th data-sort-ignore="true" data-hide="all">项目状态</th> <th data-sort-ignore="true" data-hide="all">甲方</th> <th data-sort-ignore="true" data-hide="all">乙方</th> <th data-sort-ignore="true" data-hide="all">可导入</th> <th data-sort-ignore="true">可导入</th> <th data-sort-ignore="true" data-hide="all">项目详情</th> </tr> </thead> <tbody> ';
if(list&&list.length>0 ){
$out+=' ';
$each(list,function(o,i){
$out+=' <tr> <td>';
$out+=$escape(o.projectNo);
$out+='</td> <td>';
$out+=$escape(o.projectName);
$out+='</td> <td>';
$out+=$escape(o.creatorOrgName);
$out+='</td> <td>';
$out+=$escape(o.creatorUserName);
$out+='</td> <td>';
$out+=$escape(o.projectCreateDate);
$out+='</td> <td>';
$out+=$escape(o.contractDate);
$out+='</td> <td>';
$out+=$escape(o.province+' '+o.city+' '+o.detailAddress);
$out+='</td> <td>';
$out+=$escape(o.projectStatus);
$out+='</td> <td>';
$out+=$escape(o.aCompanyName);
$out+='</td> <td>';
$out+=$escape(o.bCompanyName);
$out+='</td> <td>';
if(o.valid===true){
$out+=' <span class="fc-v1-green">是</span> ';
}else{
$out+=' <span class="fc-v1-red">否，';
$out+=$escape(o.errorReason);
$out+='</span> ';
}
$out+=' </td> <td> ';
if(o.valid===true){
$out+=' <i class="fa fa-check fc-v1-green"></i> ';
}else{
$out+=' <span class="fc-v1-red">不可导入，';
$out+=$escape(o.errorReason);
$out+='</span> ';
}
$out+=' </td> <td> ';
$each(o.designContentList,function(p,t){
$out+=' <strong>';
$out+=$escape(p.contentName);
$out+=':</strong> ';
if(p.startDate!=null){
$out+=' <span>开始时间：';
$out+=$escape(p.startDate);
$out+=',&nbsp;</span> ';
}else{
$out+=' <span>开始时间：未设置,&nbsp;</span> ';
}
$out+=' ';
if(p.endDate!=null){
$out+=' <span>结束时间：';
$out+=$escape(p.endDate);
$out+=',&nbsp;</span> ';
}else{
$out+=' <span>结束时间：未设置,&nbsp;</span> ';
}
$out+=' <span>工期：';
$out+=$escape(p.periodString);
$out+='</span></br> ';
});
$out+=' </td> </tr> ';
});
$out+=' ';
}
$out+=' </tbody> <tfoot> <tr> <td colspan="11"> <ul class="pagination pull-right"></ul> </td> </tr> </tfoot> </table> </div>';
return new String($out);
});/*v:1*/
template('m_historyData/m_historyData_step3',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="col-md-12 text-center" style="margin-bottom: 20px;"> <img src="';
$out+=$escape(_url('/assets/img/default/invite_success.png'));
$out+='"> <p style="font-size:24px;">恭喜你，导入成功！</p> </div>';
return new String($out);
});/*v:1*/
template('m_imgCropper/m_imgCropper','<div class="m_imgCropper" style="overflow: auto;max-height: 500px;"> <form class="form-horizontal rounded-4x noborder"> <div class="ibox m-b-xs"> <div class="ibox-content"> <div class="title row"> <div class="col-md-12"> <div class="margin-bottom-20"> <span>请先上传一张图片,支持jpg、jpeg、png格式。</span> <div class="btnFilePicker" class="dp-inline-block"></div> </div> </div> </div> <div class="setArea row m-b-xs hide"> <div class="col-md-9"> <div class="thumbnail thumbnail-style"> <img class="img-container img-responsive" src=""> </div> </div> <div class="col-md-3"> <div class="thumbnail thumbnail-style" style="width: 110px;height: 110px;"> <div class="clearfix"> <div class="img-preview preview-md"></div> </div> </div> </div> </div> </div> </div> </form> </div>');/*v:1*/
template('m_inviteBPartner/m_inviteBPartner',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,filePath=$data.filePath,$escape=$utils.$escape,_url=$helpers._url,companyName=$data.companyName,systemManager=$data.systemManager,cellphone=$data.cellphone,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="f-s-16 dp-inline-block l-h-16">事业合伙人邀请组织</span> </div> <div class="ibox-content text-center"> <div class="m-b-sm"> ';
if(filePath!='' && filePath!=null){
$out+=' <img alt="image" class="img-circle wth-100 h-100" src="';
$out+=$escape(filePath);
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle wth-100 h-100" src="';
$out+=$escape(_url('/assets/img/default/org_default_headPic.png'));
$out+='"> ';
}
$out+=' </div> <p class="f-s-18">';
$out+=$escape(companyName);
$out+='</p> <p class="f-s-14 fc-aaa">企业负责人:';
$out+=$escape(systemManager);
$out+='</p> </div> <div class="ibox-content"> <div class="row"> <div class="col-sm-12"> <form role="form"> <div class="form-group"> <label class="f-s-14 fw-400">您的手机号：</label> <p class="f-s-14 fc-dark-blue fw-600">';
$out+=$escape(cellphone);
$out+='</p> </div> <div class="form-group"> <label class="f-s-14 fw-400">输入完整手机号校验身份：</label> <input name="cellphone" id=\'cellphone\' placeholder="手机号" class="form-control"></div> <div class="form-group"> <a class="btn btn-primary btn-block btn-ok">确定</a> </div> </form> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBPartner/m_inviteBPartner_create_has',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,a_companyName=$data.a_companyName,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="f-s-16 l-h-16 dp-inline-block" >创建新组织</span> </div> <div class="ibox-content"> <p class="f-s-14 l-h-14 p-h-xs">该组织创建后将会自动成为&nbsp;<span class="fc-v1-yellow">';
$out+=$escape(a_companyName);
$out+='</span>&nbsp;的事业合伙人</p> <div class="row"> <div class="col-sm-12"> <form class="createOrgOBox"> <div class="form-group"> <input name="companyName" placeholder="组织名称" class="form-control"> </div> <div class="form-group pt-fixed" style="bottom: 0;left: 20px;right: 20px;"> <a class="btn btn-primary btn-lg btn-block btn-ok" data-action="createOrgSubmit">立即创建</a> </div> </form> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBPartner/m_inviteBPartner_create_hasNo',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,a_companyName=$data.a_companyName,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="dp-inline-block f-s-16 l-h-16">创建账号和组织</span> </div> <div class="ibox-content"> <p class="f-s-14 l-h-14 p-h-xs">该组织创建后将会自动成为&nbsp;<span class="fc-v1-yellow">';
$out+=$escape(a_companyName);
$out+='</span>&nbsp;的事业合伙人</p> <div class="row"> <div class="col-sm-12"> <form role="form" class="createOrgOBox"> <div class="form-group"> <label class="fw-400 f-s-14 dp-block m-b-xs">个人信息：</label> <input name="userName" placeholder="姓名" class="form-control m-b-xs"> <input name="adminPassword" placeholder="密码" type="password" class="form-control m-b-xs"> <label class="fw-400 f-s-14 dp-block">组织信息：</label> <input name="companyName" placeholder="组织名称" class="form-control m-b-xs"> </div> <div class="form-group pt-fixed" style="bottom: 0;left: 20px;right: 20px;"> <a class="btn btn-primary btn-lg btn-block btn-ok" data-action="createOrgAndAccountSubmit">立即创建</a> </div> </form> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBPartner/m_inviteBPartner_org',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,a_companyName=$data.a_companyName,$each=$utils.$each,companyList=$data.companyList,c=$data.c,i=$data.i,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="dp-inline-block f-s-16 l-h-16">组织选择</span> </div> <div class="ibox-content no-pd"> <ul class="todo-list small-list m-invite-group"> <li><p class="f-s-14 l-h-14 m-t-sm"> 请选择&nbsp;<span class="fc-v1-yellow">你已创建的组织</span>&nbsp;或&nbsp;<span class="fc-v1-yellow">新创建一个组织</span>&nbsp;成为&nbsp;<span class="fc-v1-yellow">“';
$out+=$escape(a_companyName);
$out+='”</span>&nbsp;的事业合伙人</p></li> ';
$each(companyList,function(c,i){
$out+=' ';
if(c.flag===1){
$out+=' <li class="unselectable" data-action="selectOrg" data-memo="';
$out+=$escape(c.memo);
$out+='"> <div class="ibox-content box-shadow border" style="padding: 10px 20px 5px;height:90px;"> <div class="dp-inline-block p-h-sm"> <p class="fc-aaa f-s-16 l-h-16">';
$out+=$escape(c.companyName);
$out+='</p> <p class="f-s-12 l-h-12 fc-aaa">';
$out+=$escape(c.memo);
$out+='</p> </div> ';
if(c.filePath!=null && c.filePath!=''){
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(c.filePath);
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(_url('/assets/img/default/org_default_headPic.png'));
$out+='"> ';
}
$out+=' </div> </li> ';
}else{
$out+=' <li class="selectable" data-action="selectOrgApply" data-company-id="';
$out+=$escape(c.id);
$out+='" data-company-name="';
$out+=$escape(c.companyName);
$out+='"> <div class="ibox-content box-shadow border" style="padding: 10px 20px 5px;height:90px;"> <div class="dp-inline-block p-h-sm"> <p class="f-s-16 l-h-16">';
$out+=$escape(c.companyName);
$out+='</p> <p class="f-s-12 l-h-12 fc-aaa">企业负责人:';
$out+=$escape(c.systemManager);
$out+='</p> </div> ';
if(c.filePath!=null && c.filePath!=''){
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(c.filePath);
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(_url('/assets/img/default/org_default_headPic.png'));
$out+='"> ';
}
$out+=' </div> </li> ';
}
$out+=' ';
});
$out+=' <li class="selectable"> <a href="javascript:void(0)"> <div class="ibox-content box-shadow border" style="padding: 0;"> <p style="font-size: 16px;line-height: 16px;"> <a href="javascript:void(0)" data-action="createOrg" style="padding: 20px 20px 15px;display: inline-block;width: 100%;">创建新组织</a> </p> </div> </a> </li> </ul> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBPartner/m_inviteBPartner_org_hasNo',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-content text-center no-border no-pd-right no-pd-left"> <div class="m-b-md m-t-lg"> <img alt="image" class="img-circle wid-200" src="';
$out+=$escape(_url('/assets/img/default/defaultpage_pic_data.png'));
$out+='" > </div> <p class="f-s-18 m-t-md">你还没有组织，请先创建组织</p> <div class="form-group"> <a class="btn btn-primary btn-lg btn-ok" href="javascript:void(0)" data-action="createOrg">创建组织</a> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBPartner/m_inviteBPartner_success',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,companyName=$data.companyName,a_companyName=$data.a_companyName,$out='';$out+='<div class="ibox"> <div class="ibox-content text-center no-border no-pd-left no-pd-right"> <div class="m-b-sm"> <img alt="image" class="img-circle wth-100" src="';
$out+=$escape(_url('/assets/img/default/invite_success.png'));
$out+='" > </div> <p class="f-s-18">恭喜！</p> <p class="f-s-14 fc-aaa">';
$out+=$escape(companyName);
$out+=' 已经成为 ';
$out+=$escape(a_companyName);
$out+=' 的事业合伙人</p> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBranch/m_inviteBranch',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,filePath=$data.filePath,$escape=$utils.$escape,_url=$helpers._url,companyName=$data.companyName,systemManager=$data.systemManager,cellphone=$data.cellphone,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="f-s-16 dp-inline-block l-h-16">分支机构邀请组织</span> </div> <div class="ibox-content text-center"> <div class="m-b-sm"> ';
if(filePath!='' && filePath!=null){
$out+=' <img alt="image" class="img-circle wth-100 h-100" src="';
$out+=$escape(filePath);
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle wth-100 h-100" src="';
$out+=$escape(_url('/assets/img/default/org_default_headPic.png'));
$out+='"> ';
}
$out+=' </div> <p class="f-s-18">';
$out+=$escape(companyName);
$out+='</p> <p class="f-s-14 fc-aaa">企业负责人:';
$out+=$escape(systemManager);
$out+='</p> </div> <div class="ibox-content"> <div class="row"> <div class="col-sm-12"> <form role="form"> <div class="form-group"> <label class="f-s-14 fw-400">您的手机号：</label> <p class="f-s-14 fc-dark-blue fw--600">';
$out+=$escape(cellphone);
$out+='</p> </div> <div class="form-group"> <label class="f-s14 fw-400">输入完整手机号校验身份：</label> <input name="cellphone" id=\'cellphone\' placeholder="手机号" class="form-control"></div> <div class="form-group"> <a class="btn btn-primary btn-block btn-ok">确定</a> </div> </form> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBranch/m_inviteBranch_create_has',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,a_companyName=$data.a_companyName,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="f-s-16 l-h-16 dp-inline-block">创建新组织</span> </div> <div class="ibox-content"> <p class="f-s-14 l-h-14 p-h-xs">该组织创建后将会自动成为&nbsp;<span class="fc-v1-yellow">';
$out+=$escape(a_companyName);
$out+='</span>&nbsp;的分支机构</p> <div class="row"> <div class="col-sm-12"> <form class="createOrgOBox"> <div class="form-group"> <input name="companyName" placeholder="组织名称" class="form-control"> </div> <div class="form-group pt-fixed" style="bottom: 0;left: 20px;right: 20px;"> <a class="btn btn-primary btn-lg btn-block btn-ok" data-action="createOrgSubmit">立即创建</a> </div> </form> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBranch/m_inviteBranch_create_hasNo',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,a_companyName=$data.a_companyName,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="f-s-16 l-h-16 dp-inline-block">创建账号和组织</span> </div> <div class="ibox-content"> <p class="f-s-14 l-h-14 p-h-xs">该组织创建后将会自动成为&nbsp;<span class="fc-v1-yellow">';
$out+=$escape(a_companyName);
$out+='</span>&nbsp;的分支机构</p> <div class="row"> <div class="col-sm-12"> <form role="form" class="createOrgOBox"> <div class="form-group"> <label class="f-s-14 fw-400 dp-block">个人信息：</label> <input name="userName" placeholder="姓名" class="form-control m-b-xs"> <input name="adminPassword" placeholder="密码" type="password" class="form-control m-b-xs"> <label class="f-s-14 fw-400 dp-block">组织信息：</label> <input name="companyName" placeholder="组织名称" class="form-control m-b-xs"> </div> <div class="form-group pt-fixed" style="bottom: 0;left: 20px;right: 20px;"> <a class="btn btn-primary btn-lg btn-block btn-ok" data-action="createOrgAndAccountSubmit">立即创建</a> </div> </form> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBranch/m_inviteBranch_org',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,a_companyName=$data.a_companyName,$each=$utils.$each,companyList=$data.companyList,c=$data.c,i=$data.i,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="f-s-16 l-h-16 dp-inline-block">组织选择</span> </div> <div class="ibox-content no-pd"> <ul class="todo-list small-list m-invite-group"> <li><p class="f-s-14 l-h-14 m-t-sm"> 请选择&nbsp;<span class="fc-v1-yellow">你已创建的组织</span>&nbsp;或&nbsp;<span class="fc-v1-yellow">新创建一个组织</span>&nbsp;成为&nbsp;<span class="fc-v1-yellow">“';
$out+=$escape(a_companyName);
$out+='”</span>&nbsp;的分支机构</p></li> ';
$each(companyList,function(c,i){
$out+=' ';
if(c.flag===1){
$out+=' <li class="unselectable" data-action="selectOrg" data-memo="';
$out+=$escape(c.memo);
$out+='"> <div class="ibox-content box-shadow border" style="padding: 10px 20px 5px;height:90px;"> <div class="dp-inline-block p-h-sm"> <p class="f-s-16 l-h-16 fc-aaa">';
$out+=$escape(c.companyName);
$out+='</p> <p class="f-s-12 l-h-12 fc-aaa">';
$out+=$escape(c.memo);
$out+='</p> </div> ';
if(c.filePath!=null && c.filePath!=''){
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(c.filePath);
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(_url('/assets/img/default/org_default_headPic.png'));
$out+='"> ';
}
$out+=' </div> </li> ';
}else{
$out+=' <li class="selectable" data-action="selectOrgApply" data-company-id="';
$out+=$escape(c.id);
$out+='" data-company-name="';
$out+=$escape(c.companyName);
$out+='"> <div class="ibox-content box-shadow border" style="padding: 10px 20px 5px;height:90px;"> <div class="dp-inline-block p-h-sm"> <p class="f-s-16 l-h-16">';
$out+=$escape(c.companyName);
$out+='</p> <p class="f-s-12 l-h-12 fc-aaa">企业负责人:';
$out+=$escape(c.systemManager);
$out+='</p> </div> ';
if(c.filePath!=null && c.filePath!=''){
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(c.filePath);
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(_url('/assets/img/default/org_default_headPic.png'));
$out+='"> ';
}
$out+=' </div> </li> ';
}
$out+=' ';
});
$out+=' <li class="selectable"> <a href="javascript:void(0)"> <div class="ibox-content box-shadow border" style="padding: 0;"> <p class="f-s-16 l-h-16"> <a href="javascript:void(0)" data-action="createOrg" style="padding: 20px 20px 15px;display: inline-block;width: 100%;">创建新组织</a> </p> </div> </a> </li> </ul> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBranch/m_inviteBranch_org_hasNo',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-content text-center no-border no-pd-left no-pd-right"> <div class="m-b-md m-t-lg"> <img alt="image" class="img-circle wid-200" src="';
$out+=$escape(_url('/assets/img/default/defaultpage_pic_data.png'));
$out+='" > </div> <p class="f-s-18 m-t-md">你还没有组织，请先创建组织</p> <div class="form-group"> <a class="btn btn-primary btn-lg btn-ok" href="javascript:void(0)" data-action="createOrg">创建组织</a> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_inviteBranch/m_inviteBranch_success',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,companyName=$data.companyName,a_companyName=$data.a_companyName,$out='';$out+='<div class="ibox"> <div class="ibox-content text-center no-border no-pd-right no-pd-left"> <div class="m-b-sm"> <img alt="image" class="img-circle wth-100" src="';
$out+=$escape(_url('/assets/img/default/invite_success.png'));
$out+='" > </div> <p class="f-s-18">恭喜！</p> <p class="f-s-14 fc-aaa">';
$out+=$escape(companyName);
$out+=' 已经成为 ';
$out+=$escape(a_companyName);
$out+=' 的分支机构</p> </div> </div>';
return new String($out);
});/*v:1*/
template('m_login/m_forgetPWDStep1',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,title=$data.title,rootPath=$data.rootPath,$out='';$out+='<style> .form-group{position: relative;} </style> <div class="passwordBox animated fadeInDown"> <div class="row"> <div class="col-md-12"> <div class="ibox-content"> <h2 class="font-bold">';
$out+=$escape(title);
$out+='</h2> <p> 请填写您的卯丁账号 </p> <div class="row"> <div class="col-lg-12"> <form class="m-t forgetStep1OBox"> <div class="form-group"> <input class="form-control" type="text" placeholder="请输入手机号" name="cellphone" id="cellphone" maxlength="11"> </div> <div class="form-group "> <div class="input-group"> <input placeholder="请输入验证码" class="input form-control" type="text" id="verifcationCode" name="verifcationCode" placeholder="验证码"> <span class="input-group-btn"> <a type="button" class="btn btn-u" id="getCode" data-action="getCode" style="width:96px;">获取验证码</a> </span> </div> </div> <a type="submit" class="btn btn-primary block full-width m-b" href="javascript:void(0)" data-action="nextStep">下一步</a> <p class="text-muted text-left">  <a href="';
$out+=$escape(rootPath);
$out+='/iWork/sys/login">返回登录</a> </p> </form> </div> </div> </div> </div> </div> <hr>         </div>';
return new String($out);
});/*v:1*/
template('m_login/m_forgetPWDStep2',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,title=$data.title,cellphone=$data.cellphone,$out='';$out+='<style> .form-group{position: relative;} </style> <div class="passwordBox animated fadeInDown"> <div class="row"> <div class="col-md-12"> <div class="ibox-content"> <h2 class="font-bold">';
$out+=$escape(title);
$out+='</h2>    <div class="row"> <div class="col-lg-12"> <form class="m-t forgetStep2OBox"> <div class="form-group"> <label class="label">注册手机号： <span class="cellPhone form-control-static">';
$out+=$escape(cellphone);
$out+='</span></label> </div> <div class="form-group"> <input class="form-control" type="text" placeholder="请输入密码" id="password" name="password" autocomplete="off" onfocus="this.type=\'password\'"> </div> <a type="submit" class="btn btn-primary block full-width m-b" href="javascript:void(0)" data-action="completeChange">完成</a> </form> </div> </div> </div> </div> </div> <hr>         </div>';
return new String($out);
});/*v:1*/
template('m_login/m_login',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,rootPath=$data.rootPath,$out='';$out+='<style> div.ibox-content,div.login-form-qrcode-wrapper{float: left;} div.ibox-content{ width: 60%;border-radius: 4px;} div.login-form-qrcode-wrapper{ width: auto; background-color: transparent;} </style> <div class="ibox-content "> <form class="m-t" id="loginForm"> <div class="form-group"> <input type="text" tabindex="1" id="cellphone" name="cellphone" placeholder="请输入手机号码" class="form-control margin-bottom-20 rounded" maxlength="11"> </div> <div class="form-group"> <input type="text" tabindex="2" id="password" name="password" placeholder="请输入登录密码" class="form-control margin-bottom-20 rounded" autocomplete="off" onfocus="this.type=\'password\'"> </div> <a class="btn btn-primary block full-width m-b" id="btnLogin" tabindex="3" type="submit">登录</a> <a href="';
$out+=$escape(rootPath);
$out+='/iWork/sys/forgetLoginPwd"> <small>忘记密码?</small> </a>    <!--<a class="btn btn-sm btn-white btn-block" href="';
$out+=$escape(rootPath);
$out+='/iWork/sys/register">立即注册</a>--> </form>  </div> ';
return new String($out);
});/*v:1*/
template('m_login/m_popover_login',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<form id="loginForm" style="width: 240px;"> <div class="form-group"> <input type="text" tabindex="1" id="cellphone" name="cellphone" placeholder="请输入手机号码" class="form-control margin-bottom-20 rounded" maxlength="11"> </div> <div class="form-group"> <input type="text" tabindex="2" id="password" name="password" placeholder="请输入登录密码" class="form-control margin-bottom-20 rounded" autocomplete="off" onfocus="this.type=\'password\'"> </div> <div class="form-group" style="margin-bottom: 0;overflow: hidden;"> <a class="btn-u btn-u-dark-blue block full-width m-b rounded text-center m-popover-submit" id="btnLogin" tabindex="3">登录</a> <a data-url="';
$out+=$escape(_url('/iWork/sys/forgetLoginPwd'));
$out+='" href="javascript:void(0)" id="btnForgetPwd" class="pull-right"> <small>忘记密码？</small> </a> </div> </form>';
return new String($out);
});/*v:1*/
template('m_login/m_register',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,rootPath=$data.rootPath,cdnUrl=$data.cdnUrl,$out='';$out+='<style> .form-group{position: relative;} </style> <div class="middle-box text-center loginscreen animated fadeInDown"> <div> <div class="text-align-center" style=" margin-bottom: 36px;"> <object data="';
$out+=$escape(rootPath);
$out+='/assets/img/logo_blue.png"></object> </div>  <form class="m-t registerOBox" role="form" action="login.html"> <div class="form-group text-left"> <input class="form-control" type="text" placeholder="手机号" name="cellphone" id="cellphone" maxlength="11"> </div> <div class="form-group text-left"> <div class="input-group"> <input placeholder="验证码" class="input form-control" type="text" id="verifcationCode" name="verifcationCode" placeholder="验证码"> <span class="input-group-btn"> <a type="button" class="btn btn-u" id="getCode" data-action="getCode" style="width:96px;">获取验证码</a> </span> </div> </div> <div class="form-group text-left"> <input class="form-control" type="text" placeholder="姓名" id="userName" name="userName"> </div> <div class="form-group text-left"> <input class="form-control" type="text" placeholder="密码，至少包含6位字符" id="password" name="password" autocomplete="off" onfocus="this.type=\'password\'" maxlength="32"> </div> <div class="form-group text-left"> <input type="checkbox" id="serviceTerm" name="serviceTerm"/><span style="display: inline-block;margin-left: 5px;">已阅读并同意 <a href="';
$out+=$escape(cdnUrl);
$out+='/download/SeriveTerm.pdf" target="_blank">《卯丁用户服务协议》</a></span> </div> <a type="submit" class="btn btn-primary block full-width m-b" href="javascript:void(0)" data-action="submitRegister">注册</a> <p class="text-muted text-center"> <small>已有卯丁账号？</small> <a href="';
$out+=$escape(rootPath);
$out+='/iWork/sys/login">立即登录</a> </p> </form> </div> </div>';
return new String($out);
});/*v:1*/
template('m_message/m_message','<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-12" id="secondary-menu"> <div class="pull-left m-r-xl"> <h3 class="dp-inline-block" >消息中心</h3> </div> <a type="button" class="btn btn-w-m btn-link btn-u-sm m-t-sm pull-right " data-action="refreshMsg"> <i class="fa fa-refresh"></i> 刷新数据 </a> </div> </div> </div> <div class="ibox-content no-padding"> <div class="row"> <div class="col-md-12" id="content-box"> <div class="messageList m-l m-r" style="min-height: 690px"> </div> <div class="row margin-bottom-30"> <div class="col-md-12 text-center m-t-sm"> <div class="m-page" style="display: none;"></div> <a class="btnPageNext btn btn-default btn-xs" href="javascript:void(0)" style="display: none;">加载更多</a> </div> </div> </div> </div> </div> </div>');/*v:1*/
template('m_message/m_message_empty',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,rootPath=$data.rootPath,$out='';$out+='<div class="headline"> <h3>今天</h3> </div> <div class="margin-bottom-30 text-center" style="margin-top: 150px"> <div class="text-center"> <img src="';
$out+=$escape(rootPath);
$out+='/assets/img/default/without_message.png"> </div> <span style="color:#4765a0;">暂无消息</span> </div>';
return new String($out);
});/*v:1*/
template('m_message/m_message_group',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_dateSpecShortFormat=$helpers._dateSpecShortFormat,createDate=$data.createDate,$out='';$out+='<!--<div class="headline"> <h3>';
$out+=$escape(_dateSpecShortFormat(createDate));
$out+='</h3> </div>--> <div class=""> <div class="feed-activity-list m-message-group" data-createDate="';
$out+=$escape(_dateSpecShortFormat(createDate));
$out+='"> </div> </div>';
return new String($out);
});/*v:1*/
template('m_message/m_message_group_old',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_dateSpecShortFormat=$helpers._dateSpecShortFormat,createDate=$data.createDate,$out='';$out+='<div class="headline"> <h3>';
$out+=$escape(_dateSpecShortFormat(createDate));
$out+='</h3> </div> <div class="margin-bottom-30"> <ul class="todo-list m-t small-list m-message-group" data-createDate="';
$out+=$escape(_dateSpecShortFormat(createDate));
$out+='"> </ul> </div>';
return new String($out);
});/*v:1*/
template('m_message/m_message_single',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,messageType=$data.messageType,companyId=$data.companyId,status=$data.status,projectId=$data.projectId,targetId=$data.targetId,projectName=$data.projectName,expNo=$data.expNo,headImg=$data.headImg,_url=$helpers._url,sendUserName=$data.sendUserName,companyName=$data.companyName,$string=$utils.$string,messageContent=$data.messageContent,_momentFormat=$helpers._momentFormat,sendDate=$data.sendDate,$out='';$out+='<div class="row"> <div class="col-24-md-24 col-xl-12 notice-list-content m-t"> <div class="feed-element" data-messageType="';
$out+=$escape(messageType);
$out+='" data-companyId="';
$out+=$escape(companyId);
$out+='" data-status="';
$out+=$escape(status);
$out+='" data-projectId="';
$out+=$escape(projectId);
$out+='" data-targetId="';
$out+=$escape(targetId);
$out+='" data-project-name="';
$out+=$escape(projectName);
$out+='" data-expNo="';
$out+=$escape(expNo);
$out+='" > <div class="media-body "> <div class="col-24-md-1" > ';
if(headImg!=null && headImg!=''){
$out+=' <img alt="image" class="img-circle pull-left m-xs" src="';
$out+=$escape(headImg);
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle pull-left m-xs" src="';
$out+=$escape(_url('/assets/img/default/default_headPic.png'));
$out+='"> ';
}
$out+=' </div> <div class="col-24-md-4" > <span class="dp-block l-h-20">';
$out+=$escape(sendUserName);
$out+='</span> <span class="dp-block span-company">';
$out+=$escape(companyName);
$out+='</span> </div> <div class="col-24-md-17 msg-content-box"> <div class="msg-content">';
$out+=$string(messageContent);
$out+='</div> </div> <div class="col-24-md-2 text-right m-t" > ';
$out+=$escape(_momentFormat(sendDate,'YYYY/MM/DD'));
$out+=' </div> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_message/m_message_single_old',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_shortTime=$helpers._shortTime,createDate=$data.createDate,messageType=$data.messageType,companyId=$data.companyId,projectId=$data.projectId,targetId=$data.targetId,expNo=$data.expNo,messageContent=$data.messageContent,$out='';$out+='<li> <span class="m-l-xsd fc-v2-green fw-600">';
$out+=$escape(_shortTime(createDate));
$out+='</span>&nbsp;&nbsp;&nbsp;<span class="m-l-xsd"><a href="javascript:void(0)" data-messageType="';
$out+=$escape(messageType);
$out+='" data-companyId="';
$out+=$escape(companyId);
$out+='" data-projectId="';
$out+=$escape(projectId);
$out+='" data-targetId="';
$out+=$escape(targetId);
$out+='" data-expNo="';
$out+=$escape(expNo);
$out+='" class="btn-handle-msg" style="cursor: default;">';
$out+=$escape(messageContent);
$out+='</a></span> </li>';
return new String($out);
});/*v:1*/
template('m_myTask/m_myTask','<div class="ibox m_myTask"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6" id="secondary-menu"> <div class="pull-left m-r-xl"> <h3 class="dp-inline-block" >我的任务</h3> </div> </div> <div class="col-md-6 text-right btn-pd"> <strong>按任务状态筛选：</strong> <select class="search-by-state" name="searchByState" > <option value="">全部</option> <option value="0" selected>未完成</option> <option value="1">已完成</option> </select> </div> </div> </div> <div class="ibox-content no-padding"> <div class="my-task-list"></div> <div id="pagination-container" class="m-pagination pull-right"></div> </div> </div> ');/*v:1*/
template('m_myTask/m_myTask_expDetail',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,myExpDetails=$data.myExpDetails,item=$data.item,j=$data.j,$escape=$utils.$escape,file=$data.file,fastdfsUrl=$data.fastdfsUrl,expNo=$data.expNo,each=$data.each,i=$data.i,$out='';$out+='<style> .float_Mask{background-color: white;padding: 21px 3px;position: absolute;top: 19px;left: 124px; } </style> <div class="ibox m-b-xs"> <div class="ibox-content MyExpDetailsCheckExpenseOBox no-pd-bottom" style="max-height:700px;overflow-y: auto"> <fieldset style="padding-top:5px;"> <div class="headline m-b-sm"> <h2>报销状态</h2> </div> <div class="row"> <div class="col-md-12"> <div class="panel-body"> <ul class="timeline-v2 timeline-me"> ';
$each(myExpDetails.auditList,function(item,j){
$out+=' <li style="height: 60px;"> <time class="cbp_tmtime" datetime=""> <span style="top:0;font-size: 14px; ';
$out+=$escape((j==myExpDetails.auditList.length-1)?'color: #FF5722;':'');
$out+='">';
$out+=$escape(item.userName);
$out+='</span> <span style="top:0;font-size: 13px;">';
$out+=$escape(item.expDate);
$out+='</span> </time> <i class="cbp_tmicon rounded-x hidden-xs" style="top: 5px; ';
$out+=$escape((j==myExpDetails.auditList.length-1)?'background: #FF5722;':'');
$out+='"></i> <div class="cbp_tmlabel"> <h2 style="font-size: 14px;padding:0; ';
$out+=$escape((j==myExpDetails.auditList.length-1)?'color:#FF5722;':'');
$out+='"> ';
$out+=$escape(item.approveStatusName=="待审核"?"待审批":item.approveStatusName);
$out+='</h2> ';
if(item.approveStatusName=='发起申请'){
$out+=' <p style="font-size: 13px;word-break: break-all;color:#999;line-height: 20px;"> 报销备注：';
$out+=$escape(item.remark);
$out+='</p> ';
}
$out+=' ';
if(item.approveStatusName=='退回'){
$out+=' <p style="font-size: 13px;word-break: break-all;color:#999;line-height: 20px;"> 退回原因：';
$out+=$escape(item.remark);
$out+='</p> ';
}
$out+=' </div> ';
if(item.approveStatusName.indexOf('完成')>-1){
$out+=' <span class="float_Mask"></span> ';
}
$out+=' </li> ';
});
$out+=' </ul> </div> </div> </div> ';
if(myExpDetails.expAttachEntityList&&myExpDetails.expAttachEntityList.length>0){
$out+=' <div class="headline m-b"> <h2>报销附件</h2> </div> <div class="row margin-bottom-30"> <div class="col-md-12 p-w-m"> ';
$each(myExpDetails.expAttachEntityList,function(file,j){
$out+=' <span class="label m-r-xs dp-inline-block" style="background: #ecf0f1;padding: 5px 10px;"> <a class="curp m-l-xs" href="';
$out+=$escape(fastdfsUrl);
$out+=$escape(file.fileGroup);
$out+='/';
$out+=$escape(file.filePath);
$out+='" target="_blank"> <i class="fa fa-file-image-o"></i>&nbsp;';
$out+=$escape(file.fileName);
$out+=' </a> </span> ';
});
$out+=' </div> </div> ';
}
$out+=' <div class="headline m-b-sm"> <h2>报销信息</h2> </div> <div class="row"> <div class="col-md-12"> <label>报销编号：</label> <b>';
$out+=$escape(expNo);
$out+='</b> </div> <div class="col-md-12"> <table class="table m-b-none"> <thead> <tr> <td class="no-pd-left">报销条目</td> <td>报销金额</td> <td>报销类别</td> <td>用途说明</td> <td class="no-pd-right">关联项目</td> </tr> </thead> <tbody> ';
$each(myExpDetails.detailList,function(each,i){
$out+=' <tr> <td class="no-pd-left">';
$out+=$escape(i+1);
$out+='</td> <td>';
$out+=$escape(each.expAmount);
$out+='</td> <td>';
$out+=$escape(each.expTypeName);
$out+='</td> <td style="word-break: break-all;">';
$out+=$escape(each.expUse);
$out+='</td> <td class="no-pd-right">';
$out+=$escape(each.projectName);
$out+='</td> </tr> ';
});
$out+=' <tr> <td colspan="5"> <div class="pull-right "> <h5>合计金额：<p class="color-red dp-inline-block">';
$out+=$escape(myExpDetails.totalExpAmount);
$out+=' </p>元 </h5> </div> </td> </tr> </tbody> </table> </div> </div> </fieldset> </div> <div class="ibox-footer text-right"> <a type="button" class="btn btn-sm btn-primary rounded" data-action="agreeAndDone">同意并完成</a> <a type="button" class="btn btn-sm btn-primary rounded" data-action="agreeAndToNext">同意并继续</a> <a type="button" class="btn btn-sm btn-default rounded" data-action="cancel">取消</a> </div> </div>';
return new String($out);
});/*v:1*/
template('m_myTask/m_myTask_incomeExpenditure',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,$out='';$out+='<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 我的任务 </li> <li class=" fa fa-angle-right"> ';
$out+=$escape(projectName);
$out+=' </li> <li class="active fa fa-angle-right"> <strong>收支管理</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding" id="incomeExpenditureBox"> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_myTask/m_myTask_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,myDataList=$data.myDataList,m=$data.m,mi=$data.mi,$escape=$utils.$escape,pageIndex=$data.pageIndex,_timeDifference=$helpers._timeDifference,$string=$utils.$string,_momentFormat=$helpers._momentFormat,_url=$helpers._url,$out='';$out+='<table class="table table-hover table-bordered table-responsive"> <thead> <tr> <th width="5%">序号</th> <th width="20%">任务名称</th> <th width="20%">项目</th> <th width="12%">角色</th> <th width="28%">描述</th> <th width="15%">时间</th>  </tr> </thead> <tbody> ';
$each(myDataList,function(m,mi){
$out+=' <tr class="curp" id="';
$out+=$escape(m.id);
$out+='" i="';
$out+=$escape(mi);
$out+='" data-type="';
$out+=$escape(m.taskType);
$out+='" data-project-id="';
$out+=$escape(m.projectId);
$out+='" data-target-id="';
$out+=$escape(m.targetId);
$out+='"> <td class="v-middle"> ';
$out+=$escape(mi+1+pageIndex*10);
$out+=' </td> <td class="v-middle">';
$out+=$escape(m.taskName);
$out+='</td> <td class="v-middle"><span data-type="projectName">';
$out+=$escape(m.projectName);
$out+='</span></td> <td class="v-middle">';
$out+=$escape(m.role);
$out+='</td> <td class="v-middle"> <!--';
$out+=$escape(m.planStartTime);
$out+='-';
$out+=$escape(m.planEndTime);
$out+=' ';
if(m.planStartTime!=null && m.planStartTime!='' && m.planEndTime!=null && m.planEndTime!=''){
$out+=' | 共';
$out+=$escape(_timeDifference(m.planStartTime,m.planEndTime));
$out+='天 ';
}
$out+='--> ';
$out+=$string(m.description);
$out+=' </td> <td class="v-middle"> ';
$out+=$escape(_momentFormat(m.createDate,'YYYY/MM/DD'));
$out+=' </td> </tr> ';
});
$out+=' ';
if(myDataList==null || myDataList.length==0){
$out+=' <tr class="no-data"> <td colspan="6" align="center"> <div class="text-center"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> </div> <span style="color:#4765a0">您还没有相关任务</span> </td> </tr> ';
}
$out+=' </tbody> </table> ';
return new String($out);
});/*v:1*/
template('m_myTask/m_myTask_production',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,enterType=$data.enterType,$out='';$out+='<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 我的任务 </li> <li class=" fa fa-angle-right"> ';
$out+=$escape(projectName);
$out+=' </li> <li class="active fa fa-angle-right"> <strong> ';
if(enterType == 'approved'){
$out+=' 审批 ';
}else if(enterType == 'designer'){
$out+=' 设、校、审 ';
}else{
$out+=' 生产安排 ';
}
$out+=' </strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div id="productionList" class="list-box"></div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_myTask/m_myTask_taskIssue',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,$out='';$out+='<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 我的任务 </li> <li class=" fa fa-angle-right"> ';
$out+=$escape(projectName);
$out+=' </li> <li class="active fa fa-angle-right"> <strong>任务签发</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div id="taskIssueList" class="list-box"></div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_notice/m_establishUediter','<div id="myEditor2" style="width:100%;resize:none;overflow-y:auto;height: 300px;"></div>');/*v:1*/
template('m_notice/m_notice','<div class="ibox m_notice"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-12" id="secondary-menu"> <div class="pull-left m-r-xl"> <h3 class="dp-inline-block" >通知公告</h3> </div> <a type="button" class="btn btn-primary btn-submit btn-u-sm m-t-sm pull-right roleControl" roleCode="admin_notice" flag="2" data-action="sendNotice"> 发送公告 </a> <a type="button" class="btn btn-w-m btn-link btn-u-sm m-t-sm pull-right " data-action="refreshNotice"> <i class="fa fa-refresh"></i> 刷新数据 </a> </div> </div> </div> <div class="ibox-content no-padding"> <div class="row"> <div class="col-md-12" id="content-box"> <div class="noticeList m-l m-r" style="min-height: 690px"> </div> <div class="row margin-bottom-30"> <div class="col-md-12 text-center m-t-sm"> <div class="m-page" style="display: none;"></div> <a class="btnPageNext btn btn-default btn-xs" href="javascript:void(0)" style="display: none;">加载更多</a> </div> </div> </div> </div> </div> </div>');/*v:1*/
template('m_notice/m_notice_empty',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="margin-bottom-30 text-center" style="margin-top: 150px"> <div class="text-center"> <img src="';
$out+=$escape(_url('/assets/img/default/without_message.png'));
$out+='"> </div> <span style="color:#4765a0;">暂无公告</span> </div>';
return new String($out);
});/*v:1*/
template('m_notice/m_notice_group',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_dateSpecShortFormat=$helpers._dateSpecShortFormat,noticePublishdate=$data.noticePublishdate,$out='';$out+='<div class="feed-activity-list m-message-group" data-createDate="';
$out+=$escape(_dateSpecShortFormat(noticePublishdate));
$out+='"> </div> ';
return new String($out);
});/*v:1*/
template('m_notice/m_notice_publish',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,noticeTitle=$data.noticeTitle,$out='';$out+='<div class="ibox publicNoticeListOBox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-sm breadcrumb-box" > <ol class="breadcrumb m-l-md"> <li> 通知公告 </li> <li class="active fa fa-angle-right"> <strong>新增公告</strong> </li> </ol> </div> </div> </div> </div> <div class="ibox-content no-padding"> <div class="row"> <div class="col-md-12" id="content-box"> <div class="animated fadeInRight"> <div class="mail-box no-border"> <div class="mail-body no-border"> <form class="form-horizontal publishPublicNoticeOBox m-t-lg" method="get"> <div class="form-group"> <label class="col-md-1 control-label">标题:</label> <div class="col-md-8"> <input class="form-control" name="noticeTitle" maxlength="300" value="';
$out+=$escape(noticeTitle);
$out+='" type="text"> </div> </div> <div class="form-group"> <label class="col-md-1 control-label">发布范围:</label> <div class="col-md-8"> <input class="form-control curp fc-999" name="choseDepartment" readOnly value="点击设置" data-action="choseDepartment" type="text"> </div> </div> <div class="form-group"> <label class="col-md-1 control-label">发送内容:</label> <div class="col-md-8">     <div class="summernote"></div> </div> </div> <div class="form-group"> <label class="col-md-1 control-label m-t-sm">附件:</label> <div class="col-md-8"> <div class="row"> <div class="col-md-12 m-t-xs m-b-sm"> <span id="showFileLoading"> </span> </div> <div class="col-md-12 m-b-sm"> <span class="uploadmgrContainer"> </span> </div> </div> </div> </div> </form> </div> <div class="mail-body col-md-8 col-md-offset-1 text-right tooltip-demo"> <a class="btn btn-sm btn-primary" data-action="savePublish" >发布</a> <a class="btn btn-white btn-sm" data-action="announcement">返回</a> </div> <div class="clearfix"></div> </div> </div> </div> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_notice/m_notice_single',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,messageType=$data.messageType,companyId=$data.companyId,projectId=$data.projectId,targetId=$data.targetId,expNo=$data.expNo,id=$data.id,companyName=$data.companyName,noticeType=$data.noticeType,_url=$helpers._url,noticeTitle=$data.noticeTitle,noticePublisherName=$data.noticePublisherName,_momentFormat=$helpers._momentFormat,noticePublishdate=$data.noticePublishdate,$string=$utils.$string,noticeContent=$data.noticeContent,$out='';$out+='<div class="row curp"> <div class="col-24-md-24 col-xl-12 notice-list-content m-t"> <div class="feed-element" data-messageType="';
$out+=$escape(messageType);
$out+='" data-companyId="';
$out+=$escape(companyId);
$out+='" data-projectId="';
$out+=$escape(projectId);
$out+='" data-targetId="';
$out+=$escape(targetId);
$out+='" data-expNo="';
$out+=$escape(expNo);
$out+='" > <div class="media-body " data-action="toNoticeDetail" data-notice-id="';
$out+=$escape(id);
$out+='" data-company-name="';
$out+=$escape(companyName);
$out+='" > <div class="col-24-md-2 text-center"> <span class="notice-icon-bg ';
$out+=$escape(noticeType==1?'':'project-type');
$out+='"> ';
if(noticeType==1){
$out+=' <img alt="image" class="img-circle pull-left m-xs" src="';
$out+=$escape(_url('/assets/img/common/massNotice.png'));
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle pull-left m-xs" src="';
$out+=$escape(_url('/assets/img/common/projectNotice.png'));
$out+='"> ';
}
$out+=' </span> </div> <div class="col-24-md-22 "> <h3 class="noticeTitle fc-333" data-string="';
$out+=$escape(noticeTitle);
$out+='" >';
$out+=$escape(noticeTitle);
$out+='</h3> <p class="fc-999">发送人： <span title="';
$out+=$escape(companyName);
$out+='">';
$out+=$escape(noticePublisherName);
$out+='</span> <span class="pull-right">';
$out+=$escape(_momentFormat(noticePublishdate,'YYYY/MM/DD'));
$out+='</span> </p> <div class="fc-666 notice-content-box"> ';
$out+=$string(noticeContent);
$out+=' </div> </div> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_notice/m_onlyGetTeamByTree','<form class="sky-form onlyGetTeamByTreeOBox rounded-4x margin-top-20"> <fieldset> <div class="col-md-12" style="height: 400px;overflow: auto;"> <div id="organization_treeH"> <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div> </div> </fieldset> </form>');/*v:1*/
template('m_notice/m_showNoticeDetail',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,notice=$data.notice,$each=$utils.$each,p=$data.p,$index=$data.$index,_fastdfsUrl=$helpers._fastdfsUrl,$out='';$out+='<div class="ibox publicNoticeListOBox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-sm breadcrumb-box" > <ol class="breadcrumb m-l-md"> <li> 通知公告 </li> <li class="active fa fa-angle-right"> <strong>查看公告</strong> </li> </ol> </div> </div> <div class="col-md-6"> <a type="button" class="btn btn-primary btn-submit btn-u-sm m-sm pull-right roleControl" roleCode="admin_notice" flag="2" data-action="sendNotice">发送公告</a> </div> </div> </div> <div class="ibox-content no-padding"> <div class="row"> <div class="col-md-12 " id="content-box"> <div class="mail-box no-border m-r-xl m-l-xl m-t" style="min-height: 500px;"> <div class="p-m"> <p><h3 class="noticeTitle fc-333" data-string="';
$out+=$escape(notice.noticeTitle);
$out+='" >';
$out+=$escape(notice.noticeTitle);
$out+='</h3></p> <p class="fc-999"> <span class="pull-left">发送人：<span title="';
$out+=$escape(notice.companyName);
$out+='">';
$out+=$escape(notice.noticePublisherName);
$out+='</span></span> <span class="pull-right">发送日期：';
$out+=$escape(notice.noticePublishdate);
$out+='</span> </p> </div> <div> </div> <div class="mail-body border-no-t fc-666 "> </div> <div class="border-bottom"></div> <div class="m-t-md p-w-m"> ';
if(notice.attachList!=null && notice.attachList.length>0){
$out+=' <span class="fc-999">';
$out+=$escape(notice.attachList.length);
$out+='个附件：</span> ';
$each(notice.attachList,function(p,$index){
$out+=' <span class="bg-muted b-r-sm p-xxs m-xs"> <a class="fc-dark-blue m-r-sm " href="';
$out+=$escape(_fastdfsUrl(p.fileGroup+'/'+p.filePath));
$out+='" target="_blank">';
$out+=$escape(p.fileName);
$out+='</a> <a class="" data-action="fileDownload" data-id="';
$out+=$escape(p.id);
$out+='"><i class="fa fa-cloud-download"></i></a> </span> ';
});
$out+=' ';
}
$out+=' </div> <div class="clearfix"></div> </div> </div> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_org/m_addressBook',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,rootPath=$data.rootPath,$out='';$out+='<div class="ibox m-b-none m_addressBook"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 通讯录 </li> <li class="active fa fa-angle-right"> <strong>通讯录</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-xs treeSwitch"> <a href="javascript:void(0)" data-action="treeSwitch"> <img src="';
$out+=$escape(rootPath);
$out+='/assets/img/whole_org_framework.png" style="height: 23px;" data-toggle="tooltip" data-placement="top" title="查看完整组织架构" /> </a> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding no-border"> <div class="row"> <div class="col-24-sm-8 col-24-md-8 col-24-lg-6 " id="org-tree-box" style="height:500px;overflow: auto;border-right:solid 1px #d9d9d9;"> <div class="clearfix margin-bottom-10"></div> <div id="organization_treeH"> <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div> </div>  <div class="col-24-sm-16 col-24-md-16 col-24-lg-18 no-padding" id="orgUserListBox"> </div> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_org/m_bulkImport',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,companyName=$data.companyName,$out='';$out+='<div class="ibox white-bg m_bulkImport"> <div class="ibox-content"> <div class="row"> <div class="col-md-12 m-md"> <div class="call-action-v1 call-action-v1-boxed margin-bottom-40"> <div class="call-action-v1-box"> <div class="call-action-v1-in"> <h3 class="color-darker"><strong>组织名称：';
$out+=$escape(companyName);
$out+='</strong></h3> <h4 >通过批量导入的方式快速建立您的组织架构。</h4> <p>您需要先准备好指定格式的Excel文档. <a href="javascript:void(0);" data-action="downLoadTemplate">下载模版文件</a> </p> </div> <div class="call-action-v1-in inner-btn page-scroll"> <div id="filePicker" class="btn-u btn-primary no-padding rounded"> </div> <a class="btn-u btn-u-default rounded" href="javascript:void(0);" data-action="backToLast">返回</a> </div> <div class="progress progress-u progress-xs m-t-md m-r-md hide"> <div class="progress-bar progress-bar-u w-0" role="progressbar" name="div_progress" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"> </div> </div> </div> </div> </div> </div> <div class="row"> <div class="col-md-12" id="errorListBox"> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_org/m_bulkImportListTips',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,userErrorList=$data.userErrorList,u=$data.u,$index=$data.$index,$escape=$utils.$escape,$out='';$out+='<div class="call-action-v1 call-action-v1-boxed tag-box-v3 table"> <div class="alert alert-danger fade in margin-top-20"> <div class="call-action-v1 call-action-v1-boxed tag-box-v3 table"> <table class="table table-responsive"> <thead> <tr> <td>部门</td> <td>姓名</td> <td>手机号码</td> <td>邮箱</td> <td>入职时间</td> <td>联系电话</td> <td>提示</td> </tr> </thead> <tbody> ';
$each(userErrorList,function(u,$index){
$out+=' <tr> <td>';
$out+=$escape(u.departName);
$out+='</td> <td>';
$out+=$escape(u.userName);
$out+='</td> <td>';
$out+=$escape(u.cellphone);
$out+='</td> <td>';
$out+=$escape(u.email);
$out+='</td> <td>';
$out+=$escape(u.entryTime);
$out+='</td> <td>';
$out+=$escape(u.phone);
$out+='</td> <td>';
$out+=$escape(u.remark);
$out+='</td> </tr> ';
});
$out+=' <tr> <td colspan="7"> <b> 以上成员信息存在错误或者已经存在,请重新修改并上传。</b> </td> </tr> </tbody> </table> </div> </div> <div class="row dp-none"> <div class="col-md-12"> <h3 class="text-center"><i class="fa fa-check text-success"></i> 批量导入成功 </h3> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_org/m_createOrg',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,serverTypeList=$data.serverTypeList,$value=$data.$value,$index=$data.$index,$escape=$utils.$escape,$out='';$out+='<style> .ui-popup.ui-popup-modal{z-index: 9999999!important;} #createOrgBox{ width: 620px; margin:0 auto; } </style> <div class="container-fluid"> <div class="row margin-top-30 animated fadeInRight "> <div id="createOrgBox"> <div class="ibox">  <div class="ibox-content"> <form class="createOrgBox"> <fieldset> <section> <div class="headline"> <h2>创建组织</h2> </div> </section> <section> <div class="row"> <div class="form-group col-md-12"> <label for="companyName">组织名称<span class="color-red">*</span></label> <input type="text" id="companyName" class="form-control" name="companyName"> </div>  <div class="form-group col-md-12" id="citysBox"> <label for="selectRegion">所在地区</label> <div class="input-group" id="selectRegion" name="selectRegion"> <div class="dp-inline-block"> <select class="prov form-control" name="province"></select> </div> <div class="dp-inline-block m-l-xs"> <select class="city form-control" name="city" disabled="disabled" style="display: none"></select> </div> <div class="dp-inline-block m-l-xs"> <select class="dist form-control" name="county" disabled="disabled" style="display: none"></select> </div> </div> </div> <div class="form-group col-md-12 serviceTypeEdit"> <label>服务类型</label> <div class="input-group dp-block"> ';
$each(serverTypeList,function($value,$index){
$out+=' <div class="serviceType col-24-md-8 col-24-lg-4 col-24-xs-24 no-pd-left"> <div class="i-checks checkbox-inline"> <input name="serverType" id="serverType-';
$out+=$escape($value.id);
$out+='" value="';
$out+=$escape($value.id);
$out+='" type="checkbox"> <span for="serverType-';
$out+=$escape($value.id);
$out+='">';
$out+=$escape($value.name);
$out+='</span> </div> </div> ';
});
$out+=' </div> </div> </div> </section> </fieldset> <div class="footTools"> <a type="button" class="btn-u pull-right getClickFun rounded" data-action="saveOrg" href="javascript:void(0)">立即创建</a> </div> </form> </div> <script type="text/javascript"> $(\'.i-checks\').iCheck({ checkboxClass: \'icheckbox_minimal-green\', radioClass: \'iradio_minimal-green\' }); </script> </div> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_org/m_editAlias',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,companyOriginalName=$data.companyOriginalName,companyAlias=$data.companyAlias,relationTypeId=$data.relationTypeId,$out='';$out+='<div class="of-hidden" style="padding:15px;"> <form class="editAliasForm" style="font-size: 12px;"> <!--<div class="form-group"> <label class="col-24-sm-5 control-label" for="companyOriginalName">原名：</label> <label name="companyOriginalName" id="companyOriginalName" class="col-sm-9 form-control no-pd-left f-s-14 fw-400 border-none" style="width:270px;">';
$out+=$escape(companyOriginalName);
$out+='</label> </div>--> <div class="row form-group"> <label class="col-md-3 text-right m-t-xs">组织名称： <span class="color-red">*</span></label> <div class="col-md-9"> <input type="text" class="form-control input-sm" name="companyName" value="';
$out+=$escape(companyOriginalName);
$out+='"> </div> </div> <!--<div class="row form-group"> <label class="col-md-3 text-right m-t-xs" for="companyAlias">别名：</label> <div class="col-md-9"> <input name="companyAlias" id="companyAlias" class="form-control input-sm" value="';
$out+=$escape(companyAlias);
$out+='" /> </div> </div>--> <div class="row"> <label class="col-md-3 text-right m-t-xs">类型标示： <span class="color-red">*</span></label> <div class="col-md-9 m-t-xs"> <div class="radio radio-inline m-t-none "> ';
if(relationTypeId==null || relationTypeId==1){
$out+=' <input id="t1" class="curp" value="1" name="roleType" type="radio" checked> ';
}else{
$out+=' <input id="t1" class="curp" value="1" name="roleType" type="radio"> ';
}
$out+=' <label class="r-blue-bg" for="t1"></label> </div> <div class="radio radio-inline curp"> ';
if(relationTypeId!=null && relationTypeId==2){
$out+=' <input id="t2" class="curp" value="2" name="roleType" type="radio" checked> ';
}else{
$out+=' <input id="t2" class="curp" value="2" name="roleType" type="radio"> ';
}
$out+=' <label class="r-green-bg" for="t2"></label> </div> <div class="radio radio-inline curp"> ';
if(relationTypeId!=null && relationTypeId==3){
$out+=' <input id="t3" class="curp" value="3" name="roleType" type="radio" checked> ';
}else{
$out+=' <input id="t3" class="curp" value="3" name="roleType" type="radio"> ';
}
$out+=' <label class="r-red-bg" for="t3"></label> </div> <span><a class="btn-link" data-action="roleRightsPreView">权限预览</a></span> </div> </div> </form> </div>';
return new String($out);
});/*v:1*/
template('m_org/m_editDepart',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,parentDepart=$data.parentDepart,departObj=$data.departObj,doType=$data.doType,isDailog=$data.isDailog,$out='';$out+='<form class="editDepartOBox sky-form rounded-4x noborder"> <div class="panel-body"> <div class="form-group"> <div class="col-lg-12 m-b-sm"> <span class="f-s-14 fw-600">上级部门：<i class="m-r-md"></i>';
$out+=$escape(parentDepart.departName);
$out+='</span> </div> </div> <div class="form-group"> <label class="col-lg-2 control-label">部门名称<span class="color-red">*</span></label> <div class="col-lg-10 no-pd-left"> <input type="text" class="form-control" name="departName" value="';
$out+=$escape(departObj.departName);
$out+='"/> </div> </div> </div> <div class="panel-footer white-bg"> ';
if(doType=='edit'){
$out+=' <div class="pull-left"> <button class="btn-u btn-u-red rounded" type="button" data-action="delDepart">删除部门</button> </div> ';
}
$out+=' <div class="pull-right"> ';
if(isDailog){
$out+=' <button type="button" class="btn-u btn-u-default rounded" onclick="S_dialog.close($(this))">取消</button> ';
}
$out+=' <button type="button" class="btn-u rounded" data-action="saveDepart">保存</button> </div> <div class="clearfix"></div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_org/m_editPartner',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,companyObj=$data.companyObj,$each=$utils.$each,serverTypeList=$data.serverTypeList,st=$data.st,i=$data.i,$out='';$out+='<form class="sky-form rounded-bottom branchCompanyOBox"> <div class="panel-body"> <fieldset>  <div class="row"> <label class="col-md-3 text-right">类型标示： <span class="color-red">*</span></label> <div class="col-md-9"> <div class="radio radio-inline m-t-none "> <input id="t1" class="curp" value="1" name="roleType" type="radio" checked> <label class="r-blue-bg" for="t1"></label>  </div> <div class="radio radio-inline curp"> <input id="t2" class="curp" value="2" name="roleType" type="radio" > <label class="r-green-bg" for="t2"></label>  </div> <div class="radio radio-inline curp"> <input id="t3" class="curp" value="3" name="roleType" type="radio" > <label class="r-red-bg" for="t3"></label>  </div> <span><a class="btn-link" data-action="roleRightsPreView">权限预览</a></span> </div> </div> <div class="row form-group m-t-xs"> <label class="col-md-3 text-right">组织名称： <span class="color-red">*</span></label> <div class="col-md-9"> <input type="text" class="form-control" name="companyName" value="';
$out+=$escape(companyObj.companyName);
$out+='"> </div> </div> <!--<div class="row"> <div class="col-md-6 form-group" id="citysBox"> <label>所在地区<span class="color-red">*</span></label> <div> <label class="select dp-inline-block no-padding" style="width: 32.5%;"> <select class="prov form-control" name="province"></select> <i></i> </label> <label class="select dp-inline-block no-padding" style="width: 32.5%;"> <select class="city form-control" name="city" disabled="disabled" style="display: none;"></select> <i style="display: none;"></i> </label> <label class="select dp-inline-block no-padding" style="width: 32%;"> <select class="dist form-control" name="county" disabled="disabled" style="display: none;"></select> <i style="display: none;"></i> </label> </div> </div> </div> <div class="row serviceTypeEdit"> <div class="col-md-12 "> <label>服务类型<span class="color-red">*</span></label> <div class="row out-box"> <div class="col-md-12"> ';
$each(serverTypeList,function(st,i){
$out+=' <div class="serviceType checkbox checkbox-inline"> <input type="checkbox" id="serviceType';
$out+=$escape(i);
$out+='" name="serverType" value="';
$out+=$escape(st.id);
$out+='"> <label for="serviceType';
$out+=$escape(i);
$out+='"> ';
$out+=$escape(st.name);
$out+='</label> </div> ';
});
$out+=' </div> </div> </div> <span name="severType" style="padding-left: 15px;"></span> </div>--> ';
if(companyObj.editType==1){
$out+=' <div class="row form-group"> <label class="col-md-3 text-right">负责人手机号： <span class="color-red">*</span></label> <div class="col-md-9"> <input type="text" class="form-control" name="cellphone" maxlength="11"> </div> </div> <div class="row form-group"> <label class="col-md-3 text-right">负责人姓名： <span class="color-red">*</span></label> <div class="col-md-9"> <input type="text" class="form-control" name="userName"> </div> </div>  ';
}
$out+=' </fieldset> </div> </form> ';
return new String($out);
});/*v:1*/
template('m_org/m_editSubCompany',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,companyObj=$data.companyObj,$each=$utils.$each,serverTypeList=$data.serverTypeList,st=$data.st,i=$data.i,$out='';$out+='<form class="sky-form rounded-bottom branchCompanyOBox"> <div class="panel-body"> <fieldset>  <div class="row"> <label class="col-md-3 text-right">类型标示： <span class="color-red">*</span></label> <div class="col-md-9"> <div class="radio radio-inline m-t-none"> <input id="t1" class="curp" value="1" name="roleType" type="radio" checked> <label class="r-blue-bg" for="t1"></label>  </div> <div class="radio radio-inline curp"> <input id="t2" class="curp" value="2" name="roleType" type="radio" > <label class="r-green-bg" for="t2"></label>  </div> <div class="radio radio-inline curp"> <input id="t3" class="curp" value="3" name="roleType" type="radio" > <label class="r-red-bg" for="t3"></label>  </div> <span><a class="btn-link" data-action="roleRightsPreView">权限预览</a></span> </div> </div> <div class="row form-group m-t-xs"> <label class="col-md-3 text-right">组织名称： <span class="color-red">*</span></label> <div class="col-md-9"> <input type="text" class="form-control" name="companyName" value="';
$out+=$escape(companyObj.companyName);
$out+='"> </div> </div> <!--<div class="row"> <div class="col-md-6 form-group" id="citysBox"> <label>所在地区<span class="color-red">*</span></label> <div> <label class="select dp-inline-block no-padding" style="width: 32.5%;"> <select class="prov form-control" name="province"></select> <i></i> </label> <label class="select dp-inline-block no-padding" style="width: 32.5%;"> <select class="city form-control" name="city" disabled="disabled" style="display: none;"></select> <i style="display: none;"></i> </label> <label class="select dp-inline-block no-padding" style="width: 32%;"> <select class="dist form-control" name="county" disabled="disabled" style="display: none;"></select> <i style="display: none;"></i> </label> </div> </div> </div> <div class="row serviceTypeEdit"> <div class="col-md-12 "> <label>服务类型<span class="color-red">*</span></label> <div class="row out-box"> <div class="col-md-12"> ';
$each(serverTypeList,function(st,i){
$out+=' <div class="serviceType checkbox checkbox-inline"> <input type="checkbox" id="serviceType';
$out+=$escape(i);
$out+='" name="serverType" value="';
$out+=$escape(st.id);
$out+='"> <label for="serviceType';
$out+=$escape(i);
$out+='"> ';
$out+=$escape(st.name);
$out+='</label> </div> ';
});
$out+=' </div> </div> </div> <span name="severType" style="padding-left: 15px;"></span> </div>--> ';
if(companyObj.editType==1){
$out+=' <div class="row form-group"> <label class="col-md-3 text-right">负责人手机号： <span class="color-red">*</span></label> <div class="col-md-9"> <input type="text" class="form-control" name="cellphone" maxlength="11"> </div> </div> <div class="row form-group"> <label class="col-md-3 text-right">负责人姓名： <span class="color-red">*</span></label> <div class="col-md-9"> <input type="text" class="form-control" name="userName"> </div> </div> ';
}
$out+=' </fieldset> </div> </form> ';
return new String($out);
});/*v:1*/
template('m_org/m_editUser',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,memberObj=$data.memberObj,$each=$utils.$each,org=$data.org,$index=$data.$index,orgList=$data.orgList,d=$data.d,$out='';$out+='<form class="sky-form editUserOBox rounded-bottom"> <div class="ibox" style="max-height: 700px;overflow: auto;"> <div class="ibox-content border-none"> <h4>基本信息</h4> <fieldset> <section> <div class="row"> <div class="col-24-md-11 form-group"> <label>姓名<span class="color-red">*</span></label> <input type="text" class="form-control" id="userName" name="userName" value="';
$out+=$escape(memberObj.userName);
$out+='"> </div> <div class="col-24-md-11 form-group "> <label>手机号码<span class="color-red">*</span></label> ';
if(memberObj.cellphone!='' && memberObj.cellphone!=null){
$out+=' <input type="text" class="form-control" id="cellphone" disabled="disabled" style="cursor: not-allowed;" name="cellphone" value="';
$out+=$escape(memberObj.cellphone);
$out+='" maxlength="11"> ';
}
$out+=' ';
if(memberObj.cellphone=='' || memberObj.cellphone==null){
$out+=' <input type="text" class="form-control" id="cellphone" name="cellphone" value="';
$out+=$escape(memberObj.cellphone);
$out+='" maxlength="11"> ';
}
$out+=' </div> </div> <div class="row"> <div class="col-24-md-11 form-group"> <label>入职时间</label> <div class="input-group"> <input type="text" class="form-control" id="entryTime" name="entryTime" onfocus="WdatePicker();" readonly value="';
$out+=$escape(memberObj.entryTime);
$out+='"> <span class="input-group-addon"><i class="icon-append fa fa-calendar"></i></span> </div> </div> <div class="col-24-md-11 form-group "> <label>办公电话</label> <input type="text" class="form-control" id="phone" name="phone" value="';
$out+=$escape(memberObj.phone);
$out+='"> </div> </div> <div class="row"> <div class="col-24-md-11 form-group"> <label>邮箱</label> <input type="text" class="form-control" id="email" name="email" value="';
$out+=$escape(memberObj.email);
$out+='"> </div> </div> </section> </fieldset>   <fieldset> <section> <table class="table table-responsive" > <thead> ';
if(memberObj.departList && memberObj.departList.length>0){
$out+=' <tr> <th width="45%" style="padding-left:0;">所属部门</th> <th width="47%" style="padding-left:20px;">职位</th> <th width="8%"></th> </tr> ';
}
$out+=' </thead> ';
$each(memberObj.departList,function(org,$index){
$out+=' <tbody class="departListTbody"> <tr class="departList"> <td class="no-pd-left"> <select class="form-control" name="departIdSel" data-depart-id="';
$out+=$escape(org.id);
$out+='"> <option value="">请选择</option> ';
$each(orgList,function(d,$index){
$out+=' ';
if(org.departId==d.id){
$out+=' <option value="';
$out+=$escape(d.id);
$out+='" selected> ';
$out+=$escape(d.departName);
$out+='</option> ';
}
$out+=' ';
if(org.departId!=d.id){
$out+=' <option value="';
$out+=$escape(d.id);
$out+='"> ';
$out+=$escape(d.departName);
$out+='</option> ';
}
$out+=' ';
});
$out+=' </select> </td> <td class="p-l-md"> <input type="text" class="form-control" name="serverStation" placeholder="职位" value="';
$out+=$escape(org.serverStation);
$out+='"/> </td> <td class="delDepartServerStationTd"> <a class="btn btn-danger rounded" data-action="delDepartServerStation"> <i class="fa fa-close"></i> </a> </td> </tr> </tbody> ';
});
$out+=' <tbody class="addDepartListTbody"> <tr> <td colspan="5" class="text-center no-pd-right no-pd-left" style="border-top: none;"> <div class="call-action-v1 rounded call-action-v1-boxed tag-box-v4 curp"> <div class="call-action-v1-box margin-bottom-10 margin-top-10"> <div class="call-action-v1-in text-center"> <a href="javascript:void(0);" data-action="addDepartServerStation"> <i class="fa fa-plus fa-2x color-dark-blue"></i> </a> </div> </div> </div>    </td> </tr> </tbody> </table> </section> </fieldset>  </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_org/m_enterpriseCertification',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,authentication=$data.authentication,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="ibox m_enterpriseCertification"> <div class="ibox-content border-no-t"> ';
if(authentication && authentication.authenticationStatus==1){
$out+=' <div class="alert alert-warning alert-dismissable"> <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button> <div class="pull-left tip-icon"><i class="fa fa-check-circle-o text-warning"></i></div> <div class="col-md-11"> <p>认证资料提交成功，请耐心等待3个工作日</p> <p>认证过程中，修改资料重新提交将会进行重新审核，如审核过程中有任何问题，请拨打免费客服热线 400-900-6299。</p> </div> <div class="clearfix"></div> </div> ';
}
$out+=' ';
if(authentication && authentication.authenticationStatus==3){
$out+=' <div class="alert alert-warning alert-dismissable"> <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button> <div class="pull-left tip-icon"><i class="fa fa-times-circle-o text-danger"></i></div> <div class="col-md-11"> <p>认证失败，请重新提交认证资料</p> <p>失败原因：';
$out+=$escape(authentication.rejectReason);
$out+='</p> <p>如有问题，请联系审核人员或者拨打免费客服热线 400-900-6299。</p> </div> <div class="clearfix"></div> </div> ';
}
$out+=' <form class="form-horizontal"> <div id="vertical-timeline" class="vertical-container light-timeline no-margins"> <div class="vertical-timeline-block"> <div class="vertical-timeline-icon navy-bg p-xxs">1</div> <div class="vertical-timeline-content p-h-xs"> <h4>填写企业认证信息</h4> <div class="form-group"> <label class="col-sm-2 control-label">证件类型：</label> <div class="col-sm-10"> <div class="radio "> ';
if(authentication && authentication.businessLicenseType==1){
$out+=' <input id="t1" value="1" name="businessLicenseType" type="radio" checked> ';
}else{
$out+=' <input id="t1" value="1" name="businessLicenseType" type="radio"> ';
}
$out+=' <label for="t1"> 多证合一营业执照（原“注册号”字样，调整为18位的“统一社会信用代码”）</label> </div> <div class="radio "> ';
if(authentication && (authentication.businessLicenseType==0 || authentication.businessLicenseType==null)){
$out+=' <input id="t0" value="0" name="businessLicenseType" type="radio" checked> ';
}else{
$out+=' <input id="t0" value="0" name="businessLicenseType" type="radio"> ';
}
$out+=' <label for="t0"> 普通营业执照（仍然标识为15位的“注册号”）</label> </div> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">营业执照：</label> <div class="col-sm-10"> <div class="pull-left" style="width:120px;"> ';
if(authentication && authentication.businessLicensePhoto!=null){
$out+=' <img class="curp" src="';
$out+=$escape(authentication.businessLicensePhoto);
$out+='" id="businessLicensePhoto" data-action="preview" style="max-width: 100px;max-height: 100px;"/> ';
}else{
$out+=' ';
if(authentication && authentication.businessLicenseType==1){
$out+=' <img class="curp" src="';
$out+=$escape(_url('/assets/img/org/businessLicenseTemp2.jpg'));
$out+='" id="businessLicensePhoto" style="max-width: 100px;max-height: 100px;" data-action="preview"/> ';
}else{
$out+=' <img class="curp" src="';
$out+=$escape(_url('/assets/img/org/businessLicenseTemp1.jpg'));
$out+='" id="businessLicensePhoto" style="max-width: 100px;max-height: 100px;" data-action="preview"/> ';
}
$out+=' ';
}
$out+=' <a class="block" href="javascript:void(0);" data-action="viewBusinessLicensePhotoTemp">点击查看模板</a> </div> <div class="pull-left"> <p class="m-b-xs">上传：营业执照图片；</p> <p class="m-b-xs">照片所有信息需清晰可见，内容真实有效，不得做任何修改。</p> <p class="m-b-xs">照片支持.jpg、.jpeg、.bmp、.gif、.png格式，大小不超过8M。</p> <p><a class="btn btn-primary btn-sm file-upload pull-left" id="businessLicensePhotoUpload">选择文件</a></p> <p><label id="businessLicensePhoto-error" class="error" ></label></p> </div> </div> </div> <div class="form-group"> ';
if(authentication && authentication.businessLicenseType==1){
$out+=' <label class="col-sm-2 control-label number-label-0 hide">注册号：</label> <label class="col-sm-2 control-label number-label-1 ">统一社会信用代码：</label> ';
}else{
$out+=' <label class="col-sm-2 control-label number-label-0">注册号：</label> <label class="col-sm-2 control-label number-label-1 hide">统一社会信用代码：</label> ';
}
$out+=' <div class="col-sm-4"> ';
if(authentication && authentication.businessLicenseType==1){
$out+=' <input class="form-control" name="businessLicenseNumber" type="text" value="';
$out+=$escape(authentication.businessLicenseNumber);
$out+='" placeholder="请输入18位统一社会信用代码">  ';
}else{
$out+=' <input class="form-control" name="businessLicenseNumber" type="text" value="';
$out+=$escape(authentication.businessLicenseNumber);
$out+='" placeholder="请输入15位工商注册号"> ';
}
$out+=' </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">企业名称：</label> <div class="col-sm-4"> <input class="form-control" name="orgName" type="text" value="';
$out+=$escape(authentication.orgName);
$out+='" placeholder="请填写营业执照上的名称">  </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">法人：</label> <div class="col-sm-4"> <input class="form-control" name="legalRepresentative" type="text" value="';
$out+=$escape(authentication.legalRepresentative);
$out+='" placeholder="请输入法人姓名"> </div> </div> </div> </div> <div class="vertical-timeline-block"> <div class="vertical-timeline-icon navy-bg p-xxs">2</div> <div class="vertical-timeline-content p-h-xs"> <h4>法人身份证信息</h4> <div class="form-group"> <label class="col-sm-2 control-label"></label> <div class="col-sm-10"> <div class="pull-left" style="width:175px;"> <p>正面：</p> ';
if(authentication && authentication.legalRepresentativePhoto!=null){
$out+=' <img class="curp" src="';
$out+=$escape(authentication.legalRepresentativePhoto);
$out+='" id="legalRepresentativePhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}else{
$out+=' <img class="curp" src="';
$out+=$escape(_url('/assets/img/org/IDCardTemp.jpg'));
$out+='" id="legalRepresentativePhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}
$out+=' </div> <div class="pull-left m-t-25"> <p class="m-b-xs">上传：身份证照片；</p> <p class="m-b-xs">请本人手持身份证，照片所有信息需清晰可见，内容真实有效，不得做任何修改。</p> <p class="m-b-xs">正面头像清晰，背面国徽，签发机关清晰可见。</p> <p><a class="btn btn-primary btn-sm file-upload pull-left" id="legalRepresentativePhotoUpload">选择文件</a></p> <p><label id="legalRepresentativePhoto-error" class="error" ></label></p> </div> </div> </div> </div> </div> <div class="vertical-timeline-block"> <div class="vertical-timeline-icon navy-bg p-xxs">3</div> <div class="vertical-timeline-content p-h-xs"> <h4>经办人身份证信息</h4> <div class="form-group"> <label class="col-sm-2 control-label"></label> <div class="col-sm-10"> <div class="pull-left" style="width:175px;"> <p>正面：</p> ';
if(authentication && authentication.operatorPhoto){
$out+=' <img class="curp" src="';
$out+=$escape(authentication.operatorPhoto);
$out+='" id="operatorPhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}else{
$out+=' <img class="curp" src="';
$out+=$escape(_url('/assets/img/org/IDCardTemp.jpg'));
$out+='" id="operatorPhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}
$out+=' </div> <div class="pull-left m-t-25"> <p class="m-b-xs">上传：身份证照片；</p> <p class="m-b-xs">请本人手持身份，照片所有信息需清晰可见，内容真实有效，不得做任何修改。</p> <p class="m-b-xs">正面头像清晰，背面国徽，签发机关清晰可见。</p> <p><a class="btn btn-primary btn-sm file-upload pull-left" id="operatorPhotoUpload">选择文件</a></p> <p><label id="operatorPhoto-error" class="error" ></label></p> </div> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">经办人：</label> <div class="col-sm-4"> <input class="form-control" name="operatorName" type="text" value="';
$out+=$escape(authentication.operatorName);
$out+='"> </div> </div> </div> </div> <div class="vertical-timeline-block"> <div class="vertical-timeline-icon navy-bg p-xxs">4</div> <div class="vertical-timeline-content p-h-xs"> <h4>认证授权书</h4> <div class="form-group"> <label class="col-sm-2 control-label"></label> <div class="col-sm-10"> <div class="pull-left" style="width:175px;"> ';
if(authentication && authentication.sealPhoto){
$out+=' <img class="curp" src="';
$out+=$escape(authentication.sealPhoto);
$out+='" id="sealPhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}else{
$out+=' <img class="curp" src="';
$out+=$escape(_url('/assets/img/org/powerOfAttorney.jpg'));
$out+='" id="sealPhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}
$out+=' </div> <div class="pull-left"> <p class="m-b-xs">请下载《认证授权书》<a href="javascript:void(0)" data-action="sealPhotoDownLoad">点击下载</a></p> <p class="m-b-xs">按要求填写，并手写仅用于卯丁认证字样，并加盖企业公章。</p> <p class="m-b-xs">照片所有信息需清晰可见，内容真实有效。</p> <p class="m-b-xs">照片支持.jpg、.jpeg、.bmp、.gif、.png格式，大小不超过8M。</p> <p><a class="btn btn-primary btn-sm file-upload pull-left" id="sealPhotoUpload">选择文件</a></p> <p><label id="sealPhoto-error" class="error" ></label></p> </div> </div> </div> </div> </div> <div class="vertical-timeline-block"> <div class="vertical-timeline-icon navy-bg p-xxs">5</div> <div class="vertical-timeline-content p-h-xs"> <h4>提交审核信息</h4> <div class="form-group"> <label class="col-sm-2 control-label"></label> <div class="col-sm-10"> <div class="check-div"> <div class="i-checks " > <input type="checkbox" id="ck1" name="convention" /> <label for="ck1" class="fw-normal">已阅读并同意<a href="javascript:void(0)" data-action="maodingConvention">《卯丁公约》</a></label> </div> </div> <div class="check-div"> <div class="i-checks " > <input type="checkbox" id="ck2" name="specification" /> <label for="ck2" class="fw-normal">已阅读并同意<a href="javascript:void(0)" data-action="maodingCertificationSpecification">《卯丁认证规范》</a></label> </div> </div> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label"></label> <div class="col-sm-10"> <a class="btn btn-primary pull-left" data-action="submitReview">提交审核</a> </div> </div> <div class="clearfix"></div> </div> </div> </div> </form> </div> </div> <script> $(\'.i-checks\').iCheck({ checkboxClass: \'icheckbox_minimal-green\', radioClass: \'iradio_minimal-green\' }); </script>';
return new String($out);
});/*v:1*/
template('m_org/m_enterpriseCertification_old',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,authentication=$data.authentication,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="ibox m_enterpriseCertification"> <div class="ibox-content border-no-t"> ';
if(authentication && authentication.authenticationStatus==1){
$out+=' <div class="alert alert-warning alert-dismissable"> <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button> <div class="pull-left tip-icon"><i class="fa fa-check-circle-o text-warning"></i></div> <div class="col-md-11"> <p>认证资料提交成功，请耐心等待3个工作日</p> <p>认证过程中，修改资料重新提交将会进行重新审核，如审核过程中有任何问题，请拨代4000000。</p> </div> <div class="clearfix"></div> </div> ';
}
$out+=' ';
if(authentication && authentication.authenticationStatus==3){
$out+=' <div class="alert alert-warning alert-dismissable"> <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button> <div class="pull-left tip-icon"><i class="fa fa-times-circle-o text-danger"></i></div> <div class="col-md-11"> <p>认证失败，请重新提交认证资料</p> <p>失败原因：1</p> <p>如有问题，请联系审核人员或者拨打客服电话400-400-400。</p> </div> <div class="clearfix"></div> </div> ';
}
$out+=' <form class="form-horizontal"> <h4>填写企业认证信息</h4> <div class="form-group"> <label class="col-sm-2 control-label">证件类型：</label> <div class="col-sm-10"> <div class="radio "> ';
if(authentication && authentication.businessLicenseType==1){
$out+=' <input id="t1" value="1" name="businessLicenseType" type="radio" checked> ';
}else{
$out+=' <input id="t1" value="1" name="businessLicenseType" type="radio"> ';
}
$out+=' <label for="t1"> 多证合一营业执照（原“注册号”字样，调整为18位的“统一社会信用代码”）</label> </div> <div class="radio "> ';
if(authentication && (authentication.businessLicenseType==0 || authentication.businessLicenseType==null)){
$out+=' <input id="t0" value="0" name="businessLicenseType" type="radio" checked> ';
}else{
$out+=' <input id="t0" value="0" name="businessLicenseType" type="radio"> ';
}
$out+=' <label for="t0"> 普通营业执照（仍然标识为15位的“注册号”）</label> </div> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">营业执照：</label> <div class="col-sm-10"> <div class="pull-left" style="width:120px;"> ';
if(authentication && authentication.businessLicensePhoto!=null){
$out+=' <img class="curp" src="';
$out+=$escape(authentication.businessLicensePhoto);
$out+='" id="businessLicensePhoto" data-action="preview" style="max-width: 100px;max-height: 100px;"/> ';
}else{
$out+=' ';
if(authentication && authentication.businessLicenseType==1){
$out+=' <img class="curp" src="';
$out+=$escape(_url('/assets/img/org/businessLicenseTemp2.jpg'));
$out+='" id="businessLicensePhoto" style="max-width: 100px;max-height: 100px;" data-action="preview"/> ';
}else{
$out+=' <img class="curp" src="';
$out+=$escape(_url('/assets/img/org/businessLicenseTemp1.jpg'));
$out+='" id="businessLicensePhoto" style="max-width: 100px;max-height: 100px;" data-action="preview"/> ';
}
$out+=' ';
}
$out+=' <a class="block" href="javascript:void(0);" data-action="viewBusinessLicensePhotoTemp">点击查看模板</a> </div> <div class="pull-left"> <p class="m-b-xs">上传：营业执照图片；</p> <p class="m-b-xs">照片所有信息需清晰可见，内容真实有效，不得做任何修改。</p> <p class="m-b-xs">照片支持.jpg、.jpeg、.bmp、.gif、.png格式，大小不超过8M。</p> <p><a class="btn btn-primary btn-sm file-upload" id="businessLicensePhotoUpload">选择文件</a></p> <p><label id="businessLicensePhoto-error" class="error" ></label></p> </div> </div> </div> <div class="form-group"> ';
if(authentication && authentication.businessLicenseType==1){
$out+=' <label class="col-sm-2 control-label number-label-0 hide">注册号：</label> <label class="col-sm-2 control-label number-label-1 ">统一社会信用代码：</label> ';
}else{
$out+=' <label class="col-sm-2 control-label number-label-0">注册号：</label> <label class="col-sm-2 control-label number-label-1 hide">统一社会信用代码：</label> ';
}
$out+=' <div class="col-sm-3"> <input class="form-control" name="businessLicenseNumber" type="text" value="';
$out+=$escape(authentication.businessLicenseNumber);
$out+='"> ';
if(authentication && authentication.businessLicenseType==1){
$out+=' <span class="help-block m-b-none number-label-0 text-muted hide">请输入15位工商注册号</span> <span class="help-block m-b-none number-label-1 text-muted ">请输入18位统一社会信用代码</span> ';
}else{
$out+=' <span class="help-block m-b-none number-label-0 text-muted">请输入15位工商注册号</span> <span class="help-block m-b-none number-label-1 text-muted hide">请输入18位统一社会信用代码</span> ';
}
$out+=' </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">企业名称：</label> <div class="col-sm-3"> <input class="form-control" name="orgName" type="text" value="';
$out+=$escape(authentication.orgName);
$out+='"> <span class="help-block text-muted m-b-none">请填写营业执照上的名称</span> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">法人：</label> <div class="col-sm-3"> <input class="form-control" name="legalRepresentative" type="text" value="';
$out+=$escape(authentication.legalRepresentative);
$out+='"> </div> </div> <h4 class="m-t-md">法人身份证信息</h4> <div class="form-group"> <label class="col-sm-2 control-label"></label> <div class="col-sm-10"> <div class="pull-left" style="width:175px;"> <p>正面：</p> ';
if(authentication && authentication.legalRepresentativePhoto!=null){
$out+=' <img class="curp" src="';
$out+=$escape(authentication.legalRepresentativePhoto);
$out+='" id="legalRepresentativePhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}else{
$out+=' <img class="curp" src="';
$out+=$escape(_url('/assets/img/org/IDCardTemp.jpg'));
$out+='" id="legalRepresentativePhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}
$out+=' </div> <div class="pull-left m-t-25"> <p class="m-b-xs">上传：身份证照片；</p> <p class="m-b-xs">请本人手持身份，照片所有信息需清晰可见，内容真实有效，不得做任何修改。</p> <p class="m-b-xs">正面头像清晰，背面国微，签发机关清晰可见。</p> <p><a class="btn btn-primary btn-sm file-upload" id="legalRepresentativePhotoUpload">选择文件</a></p> <p><label id="legalRepresentativePhoto-error" class="error" ></label></p> </div> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">经办人：</label> <div class="col-sm-3"> <input class="form-control" name="operatorName" type="text" value="';
$out+=$escape(authentication.operatorName);
$out+='"> </div> </div> <h4 class="m-t-md">经办人身份证信息</h4> <div class="form-group"> <label class="col-sm-2 control-label"></label> <div class="col-sm-10"> <div class="pull-left" style="width:175px;"> <p>正面：</p> ';
if(authentication && authentication.operatorPhoto){
$out+=' <img class="curp" src="';
$out+=$escape(authentication.operatorPhoto);
$out+='" id="operatorPhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}else{
$out+=' <img class="curp" src="';
$out+=$escape(_url('/assets/img/org/IDCardTemp.jpg'));
$out+='" id="operatorPhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}
$out+=' </div> <div class="pull-left m-t-25"> <p class="m-b-xs">上传：身份证照片；</p> <p class="m-b-xs">请本人手持身份，照片所有信息需清晰可见，内容真实有效，不得做任何修改。</p> <p class="m-b-xs">正面头像清晰，背面国微，签发机关清晰可见。</p> <p><a class="btn btn-primary btn-sm file-upload" id="operatorPhotoUpload">选择文件</a></p> <p><label id="operatorPhoto-error" class="error" ></label></p> </div> </div> </div> <h4 class="m-t-md">认证授权书</h4> <div class="form-group"> <label class="col-sm-2 control-label"></label> <div class="col-sm-10"> <div class="pull-left" style="width:175px;"> ';
if(authentication && authentication.sealPhoto){
$out+=' <img class="curp" src="';
$out+=$escape(authentication.sealPhoto);
$out+='" id="sealPhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}else{
$out+=' <img class="curp" src="';
$out+=$escape(_url('/assets/img/org/powerOfAttorney.jpg'));
$out+='" id="sealPhoto" data-action="preview" style="max-width: 155px;max-height: 155px;"/> ';
}
$out+=' </div> <div class="pull-left"> <p class="m-b-xs">请下载《认证授权书》<a href="javascript:void(0)" data-action="sealPhotoDownLoad">点击下载</a></p> <p class="m-b-xs">按要求填写，并手写仅用于卯丁认证字样，并加盖企业公章。</p> <p class="m-b-xs">照片所有信息需清晰可见，内容真实有效。</p> <p class="m-b-xs">照片支持.jpg、.jpeg、.bmp、.gif、.png格式，大小不超过8M。</p> <p><a class="btn btn-primary btn-sm file-upload" id="sealPhotoUpload">选择文件</a></p> <p><label id="sealPhoto-error" class="error" ></label></p> </div> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">证件类型：</label> <div class="col-sm-10"> <div class="check-div"> <div class="i-checks " > <input type="checkbox" id="ck1" name="convention" /> <label for="ck1" class="fw-normal">已阅读并同意<a href="javascript:void(0)" data-action="maodingConvention">《卯丁公约》</a></label> </div> </div> <div class="check-div"> <div class="i-checks " > <input type="checkbox" id="ck2" name="specification" /> <label for="ck2" class="fw-normal">已阅读并同意<a href="javascript:void(0)" data-action="maodingCertificationSpecification">《卯丁认证规范》</a></label> </div> </div> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label"></label> <div class="col-sm-10"> <a class="btn btn-primary" data-action="submitReview">提交审核</a> </div> </div> <div class="clearfix"></div> </form> </div> </div> <script> $(\'.i-checks\').iCheck({ checkboxClass: \'icheckbox_minimal-green\', radioClass: \'iradio_minimal-green\' }); </script>';
return new String($out);
});/*v:1*/
template('m_org/m_firstCreateOrg',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,serverTypeList=$data.serverTypeList,$value=$data.$value,$index=$data.$index,$escape=$utils.$escape,$out='';$out+='<div class="ibox"> <div class="ibox-title"> <span class="f-s-20 l-h-20 fw-400">创建组织</span> </div> <div class="ibox-content"> <form class="createOrgBox"> <fieldset> <section> <div class="row"> <div class="form-group col-md-12"> <label for="companyName">组织名称<span class="color-red">*</span></label> <input type="text" id="companyName" class="form-control" name="companyName"> <span class="help-block m-b-none">创建一个组织，邀请成员加入，通过项目一起高效的协作</span> </div> <div class=" col-md-12"> <div class="ibox float-e-margins"> <div class="ibox-content no-border no-padding dp-none"> <div class="row"> <div class="form-group col-md-12" id="citysBox"> <label for="selectRegion">所在地区</label> <div class="input-group" id="selectRegion" name="selectRegion"> <div class="dp-inline-block"> <select class="prov form-control" name="province"></select> </div> <div class="dp-inline-block m-l-xs"> <select class="city form-control" style="display: none;" name="city" disabled="disabled"></select> </div> <div class="dp-inline-block m-l-xs"> <select class="dist form-control" style="display: none;" name="county" disabled="disabled"></select> </div> </div> </div> <div class="form-group col-md-12 serviceTypeEdit"> <label>服务类型</label> <div class="input-group col-md-12 dp-block"> ';
$each(serverTypeList,function($value,$index){
$out+=' <div class="serviceType col-24-md-8 col-24-lg-4 col-24-xs-24 no-pd-left"> <div class="i-checks checkbox-inline"> <input name="serverType" id="serverType-';
$out+=$escape($value.id);
$out+='" value="';
$out+=$escape($value.id);
$out+='" type="checkbox"> <span for="serverType-';
$out+=$escape($value.id);
$out+='">';
$out+=$escape($value.name);
$out+='</span> </div> </div> ';
});
$out+=' </div> </div> </div> </div> <div class="ibox-title p-xxs border-top-bottom" style="min-height:0;"> <div class="ibox-tools text-align-center"> <a class="collapse-link"> <i class="fa fa-chevron-down"></i><span class="text">更多</span> </a> </div> </div> </div> </div> </div> </section> </fieldset> <div class="footTools"> <a type="button" class="btn btn-primary btn-sm pull-right" data-action="saveOrg" href="javascript:void(0)">立即创建</a> </div> </form> </div> <script type="text/javascript"> $(\'.i-checks\').iCheck({ checkboxClass: \'icheckbox_minimal-green\', radioClass: \'iradio_minimal-green\' }); </script> </div>';
return new String($out);
});/*v:1*/
template('m_org/m_firstCreateOrg_pre','<div class="ibox"> <div class="ibox-title"> <span class="f-s-20 fw-400 l-h-20">欢迎使用卯丁</span> </div> <div class="ibox-content"> <p class="f-s-16 l-h-16"> <span class="dp-inline-block" style="width: 16px;"><i class="fc-dark-blue fa fa-info-circle"></i></span> 温馨提示</p> <p class="f-s-14 m-t-sm l-h-28">初次使用卯丁系统且无组织邀请的用户，可进行以下两个操作：</p> <ul style="padding: 0 20px 15px;"> <li> <p class="f-s-14 l-h-14">若你是组织管理者，可进行创建组织</p> </li> <li> <p class="f-s-14 l-h-14">若你是组织普通成员，可等待你组织的邀请，将你加入到组织中</p> </li> </ul> <div class="footTools"> <a type="button" class="btn btn-primary btn-sm pull-right" data-action="startCreateOrg" href="javascript:void(0)">开始创建</a> <a type="button" class="btn btn-default btn-sm pull-right disabled m-r-sm" href="javascript:void(0)">等待邀请</a> </div> </div> </div>');/*v:1*/
template('m_org/m_inviteCorp',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,inviteType=$data.inviteType,$out='';$out+='<div class="container-fluid inviteCorpBox"> <div class="row"> <div class="col-md-12" style="padding: 25px 15px 15px 15px"> <div class="form-group"> <!--请输入';
$out+=$escape(inviteType===1?'分支机构':'事业合伙人');
$out+='的手机号：--> 请输入被邀请组织负责人的手机号： </div> <div class="inviteCorpPart form-group input-group"> <input name="bPartnerPhone" class="form-control"> <span class="input-group-btn"> <a class="btn btn-primary pull-right" data-action="sendMessage">发送邀请</a>  </span> </div>  </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_org/m_inviteCorp_part','<div class="inviteCorpPart form-group input-group"> <input class="form-control"> <span class="input-group-btn"> <a class="btn btn-primary" data-action="addCorp"><i class="fa fa-plus"></i></a> <a class="btn btn-danger" data-action="removeCorp" style="display: none;"><i class="fa fa-close"></i></a> </span> </div>');/*v:1*/
template('m_org/m_inviteUser',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,inivteUserUrl=$data.inivteUserUrl,$out='';$out+='<div class="container-fluid invitePersonelOBox"> <div class=" panel-invite"> <div class="panel-body no-pd" id="qqShareDiv"> <div class="bdsharebuttonbox hide" data-tag="share_1"> <a id="share" target="_blank"><i class="fa fa-qq"></i></a> </div> </div> <div class="col-md-12 p-h-xs"> <div class="form-group"> <div class="input-group" style="width: 100%;"> 请复制以下链接发送给你想邀请的人员 </div> </div> <div class="form-group"> <div class="input-group full-width"> <input class="form-control left" id="url" style="width: 78%;height: 31px;" value="';
$out+=$escape(inivteUserUrl);
$out+='"> <button type="button" class="btn btn-primary" left data-action="copyUrl" style="margin: -1px 0 0 8px;">复制链接</button> </div> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_org/m_organizational',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,rootPath=$data.rootPath,toAuditEmpNum=$data.toAuditEmpNum,roleCodes=$data.roleCodes,$out='';$out+='<div class="ibox m_organizational">  <div class="ibox-content no-padding"> <div class="col-24-md-24"> <div class="row org-btn-box"> <div class="treeSwitch" style=""> <a class="m-t-sm" href="javascript:void(0)" data-action="treeSwitch"> <img src="';
$out+=$escape(rootPath);
$out+='/assets/img/whole_org_framework.png" style="height: 23px;" data-toggle="tooltip" data-placement="top" title="查看完整组织架构" /> </a> </div> <div class="col-24-md-23 col-24-sm-23 col-24-xs-23 headContent"> <a href="javascript:void(0)" class=" roleControl" roleCode="hr_org_set" flag="2" data-action="addDepart"> <i class="iconfont rounded icon-2fengongsi m-r-xs"></i>添加部门 </a> <span class="divice roleControl" roleCode="hr_org_set" flag="2" >|</span>  <a href="javascript:void(0)" class="roleControl no-pd-right" roleCode="hr_org_set" flag="2" data-action="addUser">添加人员</a> <a type="button" class="dropdown-toggle roleControl" roleCode="hr_org_set" flag="2" data-toggle="dropdown" aria-haspopup="false" aria-expanded="false" data-action="drop_down"><i class="fa fa-caret-down "></i></a> <ul class="dropdown-menu dropdown-menu-left dropdown-menu-v1"> <li> <a href="javascript:void(0)" class="roleControl dp-block" roleCode="hr_org_set" flag="2" data-action="inviteUser">邀请加入</a> </li> </ul> <a href="javascript:void(0)" class="roleControl" roleCode="hr_org_set" flag="2" data-action="bulkImport">批量导入成员</a> <a href="javascript:void(0)" class="count-info roleControl" roleCode="hr_org_set" flag="2" data-action="toAuditUser"> 待审核人员 ';
if(toAuditEmpNum>0){
$out+=' <span id="toAuditEmpNumTip" class="badge badge-danger"> ';
$out+=$escape(toAuditEmpNum);
$out+=' </span> ';
}
$out+=' </a> ';
if(roleCodes.indexOf('hr_org_set')>-1){
$out+=' <span class="divice">|</span> ';
}
$out+=' <a href="javascript:void(0)" class="roleControl" roleCode="org_partner" flag="2" data-action="addSubCompany"><i class="iconfont rounded icon-2fengongsi1 m-r-xs"></i>创建分支机构</a> <a href="javascript:void(0)" class="roleControl" roleCode="org_partner" flag="2" data-action="addPartner"><i class="iconfont rounded icon-2fengongsi1 m-r-xs"></i>创建事业合伙人</a>  </div> </div> </div> <div class="col-24-sm-8 col-24-md-8 col-24-lg-6" id="org-tree-box" style="height: 500px;overflow: auto;border-right:solid 1px #d9d9d9;"> <div class="clearfix margin-bottom-10"></div> <div id="organization_treeH"> <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div> </div>  <div class="col-24-sm-16 col-24-md-16 col-24-lg-18 no-padding" id="orgUserListBox"> </div> <div class="clearfix"></div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_org/m_orgByTree',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,selectedUserList=$data.selectedUserList,u=$data.u,$index=$data.$index,$escape=$utils.$escape,isASingleSelectUser=$data.isASingleSelectUser,$out='';$out+='<style> #userlist-pagination-container{position: absolute;right: 8px;bottom: -46px;height: auto;margin-top: 0} .orgUserTreeOBox .ibox-content{border-top: none;} .orgUserTreeOBox .ibox{margin-bottom: 0} </style> <form class="form-horizontal orgUserTreeOBox rounded-4x"> <div class="ibox"> <div class="ibox-content">  <div class="row"> <div class="col-md-5" style="height: 320px;overflow: auto;"> <div id="organization_treeH"> <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div> </div> <div class="col-md-7 " > <div data-list="userList" style="height: 270px;overflow: auto"></div> <div id="userlist-pagination-container" class="m-pagination pull-right m-pagination-cus"></div> </div> </div> <div class="row"> <div class="col-md-12"> <div class="panel panel-default m-b-none" style="min-height: 50px;"> <div class="panel-body chosedUserBox" style="height: 100%"> ';
$each(selectedUserList,function(u,$index){
$out+=' <span class=" m-r-xs" style="background-color: rgb(245, 245, 245);padding: 2px 0px;"> <span class="label label-light m-r-xs no-pd-right dp-inline-block" style="background: transparent"> ';
$out+=$escape(u.userName);
$out+=' ';
if(isASingleSelectUser!=2){
$out+=' <a class="curp" href="javascript:void(0)" data-id="';
$out+=$escape(u.id);
$out+='" data-action="delChosedUser"><i class="fa fa-times color-red"></i></a> ';
}
$out+=' </span> </span> ';
});
$out+=' </div> </div> </div> </div> </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_org/m_orgInfomation',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,type=$data.type,$out='';if(type == 1){
$out+=' <div class="ibox ibox-shadow ibox_min_height profile" id="mainContain"> <div class="ibox-content no-padding"> <div class="col-md-3 col-md-offset-9 col-xl-4 col-xl-offset-8 text-right m-b-xs m-t-xs p-w-md"> <a href="javascript:void(0)" id="dissolutionCompany" class="btn btn-default btn-sm hide" > 解散组织 </a> </div> <div class="row"> <div class="col-md-9 col-xl-8" id="infoMainOBox"> </div> <div class="col-md-3 col-xl-4 p-w-xl" id="teamPicShow"> </div> </div> <div class="clearfix"></div> </div> </div> ';
}else{
$out+=' <div class="ibox m-b-none profile" id="mainContain"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 通讯录 </li> <li class="active fa fa-angle-right"> <strong>组织信息</strong> </li> </ol> </div> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding no-border"> <div class="row m-t-xl"> <div class="col-md-9 col-xl-8 media-p-l-50" id="infoMainOBox"> </div> <div class="col-md-3 col-xl-4 p-w-xl" id="teamPicShow"> </div> </div> </div> </div> ';
}
$out+=' ';
return new String($out);
});/*v:1*/
template('m_org/m_orgUserList',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,isClick=$data.isClick,orgUserList=$data.orgUserList,$escape=$utils.$escape,$each=$utils.$each,u=$data.u,$index=$data.$index,d=$data.d,$out='';$out+='<table class="table table-responsive table-striped table-bordered orgUserOBox"> <thead> <tr> <th width="13%">姓名</th>  <th width="15%">手机号码</th> <th width="20%">办公电话</th> <th width="20%">邮箱</th> ';
if(isClick){
$out+=' <th width="10%"></th> <th width="7%"></th> ';
}
$out+=' </tr> </thead> ';
if(orgUserList==null || orgUserList.length==0){
$out+=' <tbody> <tr> <td colspan="';
$out+=$escape(isClick==true?6:4);
$out+='" align="center">暂无数据</td> </tr> </tbody> ';
}
$out+=' <tbody> ';
$each(orgUserList,function(u,$index){
$out+=' <tr class="userListTr curp" data-id="';
$out+=$escape(u.userId);
$out+='"> <td class="word-break td-first"> <i class="fa fa-plus-square"></i>&nbsp;';
$out+=$escape(u.userName);
$out+=' </td> <!--<td class="word-break">';
$out+=$escape(u.orgServerStation);
$out+='</td>--> <td class="word-break">';
$out+=$escape(u.cellphone);
$out+='</td> <td class="word-break">';
$out+=$escape(u.phone);
$out+='</td> <td class="word-break">';
$out+=$escape(u.email);
$out+='</td> ';
if(isClick){
$out+=' <td> <div class="btn-group btnReturnFalse"> <a type="button" class="btn btn-default btn-xs dropdown-toggle btnReturnFalse roleControl" roleCode="hr_org_set" flag="2" data-toggle="dropdown" aria-expanded="true"> 操作 <i class="fa fa-angle-down btnReturnFalse"></i> </a> <ul class="dropdown-menu dropdown-menu-v1" role="menu"> <li> <a href="javascript:void(0)" data-i="';
$out+=$escape($index);
$out+='" data-action="editOrgUser">编辑</a> </li> ';
if(u.adminFlag==null||u.adminFlag==''){
$out+=' <li> <a href="javascript:void(0)" data-id="';
$out+=$escape(u.id);
$out+='" data-action="delOrgUser">删除</a> </li> ';
}
$out+=' </ul> </div> </td> <td class="lastTd"> ';
if($index>0){
$out+=' <a class="btnReturnFalse curp roleControl" roleCode="hr_org_set" flag="2" title="向上排序" style="text-decoration: none" data-i="';
$out+=$escape($index);
$out+='" data-action="upSorting"> <i class="iconfont icon-xiangshang"></i> </a> ';
}
$out+=' ';
if($index < orgUserList.length-1){
$out+=' <a class="btnReturnFalse curp roleControl" roleCode="hr_org_set" flag="2" title="向下排序" style="text-decoration: none" data-i="';
$out+=$escape($index);
$out+='" data-action="downSorting"> <i class="iconfont icon-xiangxia"></i> </a> ';
}
$out+=' </td> ';
}
$out+=' </tr> <tr class="userInfoTr" style="display: none;"> <td colspan="7" class="no-padding"> <div> <table class="table table-hover m-b-none "> <thead> <tr> <th>所属部门</th> <th>职位</th> </tr> </thead> <tbody> ';
$each(u.departList,function(d,$index){
$out+=' <tr> <td class="word-break">';
$out+=$escape(d.departName);
$out+='</td> <td class="word-break">';
$out+=$escape(d.serverStation);
$out+='</td> </tr> ';
});
$out+=' </tbody> </table> </div> </td> </tr> ';
});
$out+=' </tbody> </table> <div id="pagination-container" class="m-pagination pull-right"></div>';
return new String($out);
});/*v:1*/
template('m_org/m_org_chose_byTree',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,renderType=$data.renderType,$escape=$utils.$escape,buttonStyle=$data.buttonStyle,spanStyle=$data.spanStyle,companyList=$data.companyList,$out='';$out+='<style> .m_org_chose_byTree #orgTreeH{ display: block; position: absolute; top: 0px; border: 1px solid #e7eaec; padding: 10px; border-radius: 4px; height: 300px; overflow: auto; z-index: 9; background-color: #fff; } </style> ';
if(renderType==1){
$out+=' <div id="orgTreeH" > <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div> ';
}else{
$out+=' <div class="btn-group m_org_chose_byTree"> <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-action="selectOrg" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" data-stopPropagation="true" style="';
$out+=$escape(buttonStyle==null?'':buttonStyle);
$out+='"> <span class="company-name" style="';
$out+=$escape(spanStyle==null?'':spanStyle);
$out+='">';
$out+=$escape(companyList.text);
$out+='</span> <span class="caret"></span> </button> <ul class="dropdown-menu"> <li> <div id="orgTreeH" > <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div> </li> </ul> </div> ';
}
$out+=' ';
return new String($out);
});/*v:1*/
template('m_org/m_org_menu',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,roleCodes=$data.roleCodes,adminFlag=$data.adminFlag,$out='';$out+='<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-12" id="secondary-menu"> <div class="pull-left m-r-xl"> <h3 class="" >后台管理</h3> </div> <div class="pull-right m-r-xl"> <ul class="secondary-menu-ul"> <li class="active roleControl" id="orgInfo" roleCode="com_enterprise_edit" flag="2"> <a href="#/backstageMgt/orgInfo">组织信息</a> </li> <li id="organizational" class="roleControl" roleCode="hr_org_set,hr_employee,org_partner" flag="2"> <a href="#/backstageMgt/organizational">组织架构</a> </li> ';
if((roleCodes!=null &&  roleCodes.indexOf('sys_role_permission')>-1  ) || adminFlag=='1'){
$out+=' <li id="permissionSettings"> <a href="#/backstageMgt/permissionSettings">权限配置</a> </li> ';
}
$out+='  <li id="approvalMgt" class="" > <a href="#/backstageMgt/approvalMgt">审批管理</a> </li> <li id="enterpriseCertification" class="roleControl" roleCode="sys_role_auth" flag="2"> <a href="#/backstageMgt/enterpriseCertification">企业认证</a> </li> <li id="historicalDataImport" class="roleControl" roleCode="org_data_import,data_import" flag="2"> <a href="#/backstageMgt/historicalDataImport">历史数据导入 </a> </li> </ul> </div> </div> </div> </div> <div class="ibox-content no-padding"> <div class="row"> <div class="col-md-12" id="content-box"> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_org/m_selectedOrgTree','<div class="chose-user-tree hide" id="organizationTree" > <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div>');/*v:1*/
template('m_org/m_toAuditOrgList',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,toAuditCompanyList=$data.toAuditCompanyList,o=$data.o,$index=$data.$index,$escape=$utils.$escape,currentCompanyId=$data.currentCompanyId,$out='';$out+='<table class="table table-responsive toAuditOrgListBox"> <thead> <tr> <th width="20%">序号</th> <th width="40%">组织名称</th> <th width="20%">邀请时间</th> <th width="20%" >操作</th> </tr> </thead> <tbody> ';
$each(toAuditCompanyList,function(o,$index){
$out+=' <tr> <td>';
$out+=$escape($index+1);
$out+='</td> <td> <span>';
$out+=$escape(currentCompanyId==o.orgId?o.pCompanyName:o.companyName);
$out+='</span> </td> <td> <span>';
$out+=$escape(o.createDate);
$out+='</span> </td> <td> <a href="" class="btn-u btn-u btn-u-xs rounded" data-i="';
$out+=$escape($index);
$out+='" data-action="orgRelationApproval0">同意</a> <a href="" class="btn-u btn-u-default btn-u-xs rounded" data-i="';
$out+=$escape($index);
$out+='" data-action="orgRelationApproval1">拒绝</a> </td> </tr> ';
});
$out+=' ';
if(toAuditCompanyList==null || toAuditCompanyList.length==0){
$out+=' <tr> <td colspan="4" align="center">暂无数据</td> </tr> ';
}
$out+=' </tbody> </table>';
return new String($out);
});/*v:1*/
template('m_org/m_toAuditOrgTab','<div class="sky-form rounded-bottom toAuditOrgTabBox p-m"> <fieldset> <div class="tabs-container"> <ul class="nav nav-tabs"> <li class="active" > <a data-toggle="tab" href="#branch" aria-expanded="true" data-action="getCompanyToAudit2" style="padding: 5px 15px;">分支机构</a> </li> <li class="" > <a data-toggle="tab" href="#businessPartner" aria-expanded="false" data-action="getCompanyToAudit3" style="padding: 5px 15px;">事业合伙人</a> </li> </ul> <div class="tab-content"> <div id="branch" class="tab-pane fade active in"> <section> 1 </section> </div> <div id="businessPartner" class="tab-pane fade"> <section> 2 </section> </div> </div> </div> </fieldset> </div>');/*v:1*/
template('m_org/m_toAuditOrgUserList',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,userAuditList=$data.userAuditList,u=$data.u,$index=$data.$index,$escape=$utils.$escape,$out='';$out+='<table class="table table-responsive toAuditOrgUserListBox"> <thead> <tr> <th>序号</th> <th>姓名</th> <th>手机号码</th> <th>操作</th> </tr> </thead> <tbody> ';
$each(userAuditList,function(u,$index){
$out+=' <tr> <td>';
$out+=$escape($index+1);
$out+='</td> <td>';
$out+=$escape(u.userName);
$out+='</td> <td>';
$out+=$escape(u.cellphone);
$out+='</td> <td> <a href="javascript:void(0)" class="btn-u btn-u btn-u-xs rounded" data-i="';
$out+=$escape($index);
$out+='" data-action="agreedToJoin">同意</a> <a href="javascript:void(0)" class="btn-u btn-u-red btn-u-xs rounded" data-i="';
$out+=$escape($index);
$out+='" data-action="refusedToJoin">拒绝</a> </td> </tr> ';
});
$out+=' ';
if(!userAuditList||!userAuditList.length){
$out+=' <tr> <td colspan="4" align="center">暂无数据</td> </tr> ';
}
$out+=' </tbody> </table> ';
return new String($out);
});/*v:1*/
template('m_org/m_userList',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,orgUserList=$data.orgUserList,$value=$data.$value,$index=$data.$index,$escape=$utils.$escape,$out='';$out+='<div class="userListBox"> <table class="table table-striped table-hover table-responsive m-b-none" > <thead> <tr> <th>姓名</th> <th>手机号</th> <th></th> </tr> </thead> <tbody> ';
$each(orgUserList,function($value,$index){
$out+=' <tr> <td>';
$out+=$escape($value.userName);
$out+='</td> <td>';
$out+=$escape($value.cellphone);
$out+='</td> <td> <a href="javascript:void(0)" data-action="choseUser" data-companyUserId="';
$out+=$escape($value.id);
$out+='" data-userId="';
$out+=$escape($value.userId);
$out+='" class="btn-u btn-u-primany btn-u-xs rounded">选择</a> </td> </tr> ';
});
$out+=' ';
if(orgUserList==null || orgUserList.length==0){
$out+=' <tr> <td colspan="3" align="center">暂无数据</td> </tr> ';
}
$out+=' </tbody> <tfoot><tr><td colspan="3" id="userList"></td></tr></tfoot> </table> </div>';
return new String($out);
});/*v:1*/
template('m_payments/m_payments_detail_menu','<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 财务管理 </li> <li class="active fa fa-angle-right"> <strong>收支明细</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> <ul class="secondary-menu-ul pull-right"> <li class="active" id="ledger" class="roleControl" ><a>台账</a></li> <li id="receivable" class="roleControl" ><a>应收</a></li> <li id="payable" class="roleControl" ><a>应付</a></li> </ul> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding" id="content-box"> </div> </div>');/*v:1*/
template('m_payments/m_payments_ledger','<div class="m_payments_ledger"> <form role="form" class="form-inline m-md"> <div class="form-group z-index-1"> <label class="">当前组织：</label> <div class="btn-group" id="selectOrg"> </div> </div> <div class="time-combination form-group"> </div> <button class="btn btn-white btn-sm" data-action="refreshBtn">刷新数据</button> </form> <div class="data-list-box"> <div class="row"> <div class="col-md-12 data-list-container p-w-lg"></div> <div class="col-md-12 p-w-m"> <div id="data-pagination-container" class="m-pagination pull-right "></div> </div> </div> </div> </div>');/*v:1*/
template('m_payments/m_payments_ledger_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,_isBlank=$helpers._isBlank,summary=$data.summary,$escape=$utils.$escape,_expNumberFilter=$helpers._expNumberFilter,$each=$utils.$each,dataList=$data.dataList,d=$data.d,$index=$data.$index,_momentFormat=$helpers._momentFormat,_expPositiveNumberFilter=$helpers._expPositiveNumberFilter,_url=$helpers._url,$out='';$out+='<div class="m-b-sm"> <span>当前余额：</span> <span> ';
if(_isBlank(summary.sumBalance)){
$out+=' <span class="fc-v1-green">0</span> ';
}else if( (summary.sumBalance+'').indexOf('-')>-1){
$out+=' <span class="fc-v1-red">';
$out+=$escape(_expNumberFilter(summary.sumBalance));
$out+='</span> ';
}else{
$out+=' <span class="fc-v1-green">';
$out+=$escape(_expNumberFilter(summary.sumBalance));
$out+='</span> ';
}
$out+=' 元 &nbsp;&nbsp; </span> </div> <table class="table table-bordered table-responsive"> <thead> <tr> <th width="12%">日期</th> <th width="13%"> <span class="th-span-pr">金额（元）</span> <a class="icon-filter pull-right" id="filterProfitType"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="12%"> 收支分类 <a class="icon-filter pull-right" id="filterFeeType"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="12%"> 收支分类子项 <a class="icon-filter pull-right" id="filterSubFeeType"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="16%">备注</th> <th width="13%"> 收款组织 <a class="icon-filter pull-right" id="filterToCompany"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="13%"> 付款组织 <a class="icon-filter pull-right" id="filterFromCompany"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="14%"> 关联项目 <a class="icon-filter pull-right" id="filterProjectName"><i class="icon iconfont icon-shaixuan"></i></a> </th> </tr> </thead> <tbody> ';
$each(dataList,function(d,$index){
$out+=' <tr> <td>';
$out+=$escape(_momentFormat(d.profitDate,'YYYY/MM/DD'));
$out+='</td> <td class="text-right"> ';
if((d.profitFee+'').indexOf('-')>-1 ){
$out+=' <span class="fc-v1-red td-span-pr">';
$out+=$escape(_expPositiveNumberFilter(d.profitFee));
$out+='</span> ';
}else{
$out+=' <span class="fc-v1-green td-span-pr">';
$out+=$escape(_expNumberFilter(d.profitFee));
$out+='</span> ';
}
$out+=' </td> <td>';
$out+=$escape(d.feeTypeParentName);
$out+='</td> <td>';
$out+=$escape(d.feeTypeName);
$out+='</td> <td>';
$out+=$escape(d.feeName);
$out+='</td> <td> ';
if(d.toCompanyName!=null && d.toCompanyName!=''){
$out+=' ';
$out+=$escape(d.toCompanyName);
$out+=' ';
}else{
$out+=' -- ';
}
$out+=' </td> <td> ';
if(d.fromCompanyName!=null && d.fromCompanyName!=''){
$out+=' ';
$out+=$escape(d.fromCompanyName);
$out+=' ';
}else{
$out+=' -- ';
}
$out+=' </td> <td>';
$out+=$escape(d.projectName);
$out+='</td> </tr> ';
});
$out+=' ';
if(!(dataList && dataList.length>0)){
$out+=' <tr> <td colspan="6" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">没有相关数据</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> <div class="pt-absolute "> <span>合计金额：</span> <span> ';
if(_isBlank(summary.amount)){
$out+=' <span class="fc-v1-green">0</span> ';
}else if( (summary.amount+'').indexOf('-')>-1){
$out+=' <span class="fc-v1-red">';
$out+=$escape(_expNumberFilter(summary.amount));
$out+='</span> ';
}else{
$out+=' <span class="fc-v1-green">';
$out+=$escape(_expNumberFilter(summary.amount));
$out+='</span> ';
}
$out+=' 元 &nbsp;&nbsp; </span> <span>收入：</span> <span> ';
if(_isBlank(summary.gain)){
$out+=' <span class="fc-v1-green">0</span> ';
}else if( (summary.gain+'').indexOf('-')>-1){
$out+=' <span class="fc-v1-red">';
$out+=$escape(_expNumberFilter(summary.gain));
$out+='</span> ';
}else{
$out+=' <span class="fc-v1-green">';
$out+=$escape(_expNumberFilter(summary.gain));
$out+='</span> ';
}
$out+=' 元 &nbsp;&nbsp; </span> <span>支出：</span> <span> ';
if(_isBlank(summary.pay)){
$out+=' <span class="fc-v1-green">0</span> ';
}else if( (summary.pay+'').indexOf('-')>-1){
$out+=' <span class="fc-v1-red">';
$out+=$escape(_expPositiveNumberFilter(summary.pay));
$out+='</span> ';
}else{
$out+=' <span class="fc-v1-green">';
$out+=$escape(_expNumberFilter(summary.pay));
$out+='</span> ';
}
$out+=' 元 &nbsp;&nbsp; </span> </div>';
return new String($out);
});/*v:1*/
template('m_payments/m_payments_list_detail',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,detailData=$data.detailData,_momentFormat=$helpers._momentFormat,_expNumberFilter=$helpers._expNumberFilter,type=$data.type,$each=$utils.$each,d=$data.d,$index=$data.$index,_url=$helpers._url,$out='';$out+='<div class="m-t-md"> <div class="form-group col-md-12"> <label class="col-sm-2 control-label">节点信息：</label> <div class="col-sm-10">';
$out+=$escape(detailData.feeName);
$out+='</div> </div> <div class="form-group col-md-12"> <label class="col-sm-2 control-label">关联项目：</label> <div class="col-sm-10">';
$out+=$escape(detailData.projectName);
$out+='</div> </div> <div class="form-group col-md-12"> <label class="col-sm-2 control-label">收支类型：</label> <div class="col-sm-10">';
$out+=$escape(detailData.feeTypeName);
$out+='</div> </div> <div class="form-group col-md-12"> <label class="col-sm-2 control-label">发起时间：</label> <div class="col-sm-10">';
$out+=$escape(_momentFormat(detailData.createDate,'YYYY/MM/DD'));
$out+='</div> </div> <div class="form-group col-md-12"> <label class="col-sm-2 control-label">节点金额：</label> <div class="col-sm-10">';
$out+=$escape(_expNumberFilter(detailData.nodeFee));
$out+='元</div> </div> <div class="form-group col-md-12"> <label class="col-sm-2 control-label">发起金额：</label> <div class="col-sm-10">';
$out+=$escape(_expNumberFilter(detailData.launchFee));
$out+='元</div> </div> <div class="form-group col-md-12"> <label class="col-sm-2 control-label"> ';
if(type==2){
$out+=' 付款金额： ';
}else{
$out+=' 到账金额： ';
}
$out+=' </label> <div class="col-sm-10">';
$out+=$escape(_expNumberFilter(detailData.accountFee));
$out+='元</div> </div> <div class="form-group col-md-12"> <label class="col-sm-2 control-label"> ';
if(type==2){
$out+=' 应付金额： ';
}else{
$out+=' 应收金额： ';
}
$out+=' </label> <div class="col-sm-10">';
$out+=$escape(_expNumberFilter(detailData.receivableFee));
$out+='元</div> </div> <div class="col-md-12 p-w-lg"> <label class="control-label"> ';
if(type==2){
$out+=' 付款明细： ';
}else{
$out+=' 到账明细： ';
}
$out+=' </label> <div style="max-height: 200px;overflow: auto;"> <table class="table table-bordered table-responsive"> <thead> <tr> <th> ';
if(type==2){
$out+=' 付款日期 ';
}else{
$out+=' 到账日期 ';
}
$out+=' </th> <th> ';
if(type==2){
$out+=' 付账金额（元） ';
}else{
$out+=' 到账金额（元） ';
}
$out+=' </th> <th>收支类型</th> </tr> </thead> <tbody> ';
$each(detailData.paymentDetailDTOList,function(d,$index){
$out+=' <tr data-action="viewDetail" data-id="';
$out+=$escape(d.id);
$out+='"> <td>';
$out+=$escape(_momentFormat(d.createDate,'YYYY/MM/DD'));
$out+='</td> <td>';
$out+=$escape(_expNumberFilter(d.accountFee));
$out+=' </td> <td>';
$out+=$escape(d.feeTypeName);
$out+='</td> </tr> ';
});
$out+=' ';
if(!(detailData.paymentDetailDTOList && detailData.paymentDetailDTOList.length>0)){
$out+=' <tr> <td colspan="3" class="text-center v-middle"> <div class=""> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='" height="100"> <span class="fc-dark-blue dp-block">没有相关数据</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_payments/m_payments_payable','<div> <form role="form" class="form-inline m-md"> <div class="form-group z-index-1"> <label class="">当前组织：</label> <div class="btn-group" id="selectOrg"> </div> </div> <div class="form-group"> <label class="m-t-xs">时间：</label> <div class="btn-group pull-right"> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="30">一个月</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="90">一季度</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="180">半年</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="360">一年</a> </div> </div> <div class="form-group"> <div class="input-group dp-inline-block"> <input type="text" class="form-control input-sm " id="ipt_startTime" name="startTime" placeholder="开始日期" readonly="" value="" style="width: 110px;" > <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> <div class="input-group dp-inline-block"> <input type="text" class="form-control input-sm" id="ipt_endTime" name="endTime" placeholder="结束日期" readonly="" value="" style="width: 110px;" > <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> </div> <button class="btn btn-white btn-sm" data-action="refreshBtn">刷新数据</button> </form> <div class="data-list-box"> <div class="row"> <input type="hidden" name="feeType" value=""/> <input type="hidden" name="associatedOrg" value=""/> <input type="hidden" name="projectName" value=""/> <div class="col-md-12 data-list-container p-w-lg"></div> <div class="col-md-12 p-w-m"> <div id="data-pagination-container" class="m-pagination pull-right "></div> </div> </div> </div> </div>');/*v:1*/
template('m_payments/m_payments_payable_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,dataList=$data.dataList,d=$data.d,$index=$data.$index,$escape=$utils.$escape,_momentFormat=$helpers._momentFormat,_expNumberFilter=$helpers._expNumberFilter,_url=$helpers._url,_isBlank=$helpers._isBlank,paymentSum=$data.paymentSum,$out='';$out+='<table class="table table-bordered table-responsive"> <thead> <tr> <th width="15%">日期</th> <th width="15%"><span class="th-span-pr">应付（元）</span></th> <th width="15%"> 收支分类子项 <a class="icon-filter pull-right" id="filterFeeType" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="25%">备注</th> <th width="15%"> 关联组织 <a class="icon-filter pull-right" id="filterAssociatedOrg" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="15%"> 关联项目 <a class="icon-filter pull-right" id="filterProjectName" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> </tr> </thead> <tbody> ';
$each(dataList,function(d,$index){
$out+=' <tr > <td>';
$out+=$escape(_momentFormat(d.createDate,'YYYY/MM/DD'));
$out+='</td> <td class="text-right"> <a class="td-span-pr" href="javascript:void(0);" data-action="viewDetail" data-id="';
$out+=$escape(d.id);
$out+='">';
$out+=$escape(_expNumberFilter(d.profitFee));
$out+='</a> </td> <td>';
$out+=$escape(d.feeTypeName);
$out+='</td> <td>';
$out+=$escape(d.feeName);
$out+='</td> <td> ';
if(d.fromCompanyName!=null && d.fromCompanyName!=''){
$out+=' ';
$out+=$escape(d.fromCompanyName);
$out+=' ';
}else if(d.toCompanyName!=null && d.toCompanyName!=''){
$out+=' ';
$out+=$escape(d.toCompanyName);
$out+=' ';
}else{
$out+=' -- ';
}
$out+=' </td> <td> <a href="javascript:void(0);" data-action="goExpensesPage" data-project-id="';
$out+=$escape(d.projectId);
$out+='" data-type="';
$out+=$escape(d.feeType);
$out+='">';
$out+=$escape(d.projectName);
$out+='</a> </td> </tr> ';
});
$out+=' ';
if(!(dataList && dataList.length>0)){
$out+=' <tr> <td colspan="6" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">没有相关数据</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> <div class="pt-absolute"> <span>总应付金额：</span> <span> ';
if(_isBlank(paymentSum)){
$out+=' <span class="fc-v1-green">0</span> ';
}else{
$out+=' <span class="fc-v1-red">';
$out+=$escape(_expNumberFilter(paymentSum));
$out+='</span> ';
}
$out+=' 元 </span> </div>';
return new String($out);
});/*v:1*/
template('m_payments/m_payments_profitStatement',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,currentYear=$data.currentYear,$out='';$out+='<div class="ibox m_payments_profitStatement"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 财务管理 </li> <li class="active fa fa-angle-right"> <strong>利润报表</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <form role="form" class="form-inline m-md"> <div class="form-group z-index-1"> <label>当前组织：</label> <div class="btn-group" id="selectOrg"> </div> </div> <div class="form-group"> <label class="m-t-xs">时间：</label> <div class="btn-group pull-right"> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-type="0">今年</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-type="-1">去年</a> </div> </div> <div class="form-group"> <div class="input-group dp-inline-block"> <input type="text" class="form-control input-sm " id="ipt_date" name="date" placeholder="年份" readonly="" value="';
$out+=$escape(currentYear);
$out+='年" style="width: 110px;"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> </div> <button class="btn btn-white btn-sm" data-action="refreshBtn">刷新数据</button> </form> <div class="p-w-m" id="profitBox"> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_payments/m_payments_profitStatement_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,profitData=$data.profitData,p=$data.p,i=$data.i,$escape=$utils.$escape,_expNumberFilter=$helpers._expNumberFilter,s=$data.s,si=$data.si,$out='';$out+='<table class="table table-bordered table-responsive"> <thead> <tr> <th colspan="2">月份</th> <th class="text-right">1月</th> <th class="text-right">2月</th> <th class="text-right">3月</th> <th class="text-right">4月</th> <th class="text-right">5月</th> <th class="text-right">6月</th> <th class="text-right">7月</th> <th class="text-right">8月</th> <th class="text-right">9月</th> <th class="text-right">10月</th> <th class="text-right">11月</th> <th class="text-right">12月</th> <th class="text-right">合计（元）</th> </tr> </thead> <tbody> ';
$each(profitData,function(p,i){
$out+=' <tr data-type="';
$out+=$escape(p.arrowsFlag==1?'':p.code);
$out+='" style="';
$out+=$escape(p.flag==1?'':'display: none;');
$out+='"> <td width="11.5%" class="';
$out+=$escape(p.flag==1?'bg-title':'');
$out+=' v-middle" rowspan="';
$out+=$escape(p.list.length);
$out+='" > ';
if(p.arrowsFlag==1){
$out+=' <a href="javascript:void(0);" class="icon-angle" data-action="expand" data-type="';
$out+=$escape(p.code);
$out+='"><i class="fa fa-angle-right"></i></a> ';
}
$out+=' <span class="';
$out+=$escape(p.arrowsFlag==1?'':'m-l');
$out+='">';
$out+=$escape(p.key);
$out+='</span> </td> ';
if(p.list.length>0){
$out+=' <td width="10.5%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+='">';
$out+=$escape(p.list[0].expTypeName);
$out+='：</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].januaryData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].februaryData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].marchData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].aprilData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].mayData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].juneData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].julyData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].augustData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].septemberData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].octoberData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].novemberData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].decemberData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(p.list[0].totalData));
$out+='</td> ';
}
$out+=' </tr> ';
if( p.list.length>1){
$out+=' ';
$each(p.list,function(s,si){
$out+=' ';
if(si>0){
$out+=' <tr data-type="';
$out+=$escape(p.code);
$out+='" style="';
$out+=$escape(p.flag==1?'':'display: none;');
$out+='"> <td width="10.5%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+='">';
$out+=$escape(s.expTypeName);
$out+='：</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.januaryData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.februaryData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.marchData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.aprilData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.mayData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.juneData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.julyData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.augustData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.septemberData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.octoberData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.novemberData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.decemberData));
$out+='</td> <td width="6%" class="';
$out+=$escape(p.flag==1?'bg-value':'');
$out+=' text-right">';
$out+=$escape(_expNumberFilter(s.totalData));
$out+='</td> </tr> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' ';
});
$out+=' </tbody> </table>';
return new String($out);
});/*v:1*/
template('m_payments/m_payments_projectCost','<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 财务管理 </li> <li class="active fa fa-angle-right"> <strong>项目收支</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding" style=""> <form role="form" class="form-inline m-md"> <div class="m_cost_details"> <form class="form-inline m-md"> <div class="form-group z-index-1"> <label class="">当前组织：</label> <div class="btn-group" id="selectOrg"> </div> </div> <div class="time-combination form-group"> </div> <button class="btn btn-white btn-sm" data-action="refreshBtn">刷新数据</button> </form> </div> </form> <div class="data-list-box"> <div class="row"> <div class="col-md-12 data-list-container p-w-lg"></div> <div class="col-md-12 p-w-m"> <div id="data-pagination-container" class="m-pagination pull-right "></div> </div> </div> </div> </div> </div> ');/*v:1*/
template('m_payments/m_payments_projectCost_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,dataList=$data.dataList,$each=$utils.$each,d=$data.d,i=$data.i,$escape=$utils.$escape,_expNumberFilter=$helpers._expNumberFilter,_url=$helpers._url,$out='';$out+='<table class="table table-bordered table-responsive"> <thead> <tr> <th width="15%">项目名称</th> <th> <span class="th-span-pr">立项组织/立项人</span> </th> <th> 合同回款 </th> <th> 到账金额 </th> <th> 技术审查费/收 </th> <th>到账金额</th> <th>合作设计费/收</th> <th>到账金额</th> <th>累计到账</th> <th>合作设计费/付</th> <th>付款金额</th> <th>报销</th> <th>费用</th> <th> 累计付款 <a class="icon-filter pull-right" id="filterFromCompany" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> </tr> </thead> <tbody> ';
if(dataList!=null && dataList.length>0){
$out+=' ';
$each(dataList,function(d,i){
$out+=' <tr> <td> ';
$out+=$escape(d.projectName);
$out+=' </td> <td> ';
$out+=$escape(d.projectCreator);
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.contract));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.contractReal));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.technical));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.technicalReal));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.cooperateGain));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.cooperateGainReal));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.gainRealSummary));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.cooperatePay));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.cooperatePayReal));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.payExpense));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.payOther));
$out+=' </td> <td> ';
$out+=$escape(_expNumberFilter(d.payRealSummary));
$out+=' </td> </tr> ';
});
$out+=' ';
}else{
$out+=' <tr> <td colspan="14" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">没有相关数据</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> ';
return new String($out);
});/*v:1*/
template('m_payments/m_payments_receivable','<div> <form role="form" class="form-inline m-md"> <div class="form-group z-index-1"> <label class="">当前组织：</label> <div class="btn-group" id="selectOrg"> </div> </div> <div class="form-group"> <label class="m-t-xs">时间：</label> <div class="btn-group pull-right"> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="30">一个月</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="90">一季度</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="180">半年</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-days="360">一年</a> </div> </div> <div class="form-group"> <div class="input-group dp-inline-block"> <input type="text" class="form-control input-sm " id="ipt_startTime" name="startTime" placeholder="开始日期" readonly="" value="" style="width: 110px;"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> <div class="input-group dp-inline-block"> <input type="text" class="form-control input-sm" id="ipt_endTime" name="endTime" placeholder="结束日期" readonly="" value="" style="width: 110px;"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> </div> <button class="btn btn-white btn-sm" data-action="refreshBtn">刷新数据</button> </form> <div class="data-list-box"> <div class="row"> <input type="hidden" name="feeType" value=""/> <input type="hidden" name="associatedOrg" value=""/> <input type="hidden" name="projectName" value=""/> <div class="col-md-12 data-list-container p-w-lg"></div> <div class="col-md-12 p-w-m"> <div id="data-pagination-container" class="m-pagination pull-right "></div> </div> </div> </div> </div>');/*v:1*/
template('m_payments/m_payments_receivable_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,dataList=$data.dataList,d=$data.d,$index=$data.$index,$escape=$utils.$escape,_momentFormat=$helpers._momentFormat,_expNumberFilter=$helpers._expNumberFilter,_url=$helpers._url,_isBlank=$helpers._isBlank,receivaleSum=$data.receivaleSum,$out='';$out+='<table class="table table-bordered table-responsive"> <thead> <tr> <th width="15%">日期</th> <th width="15%"><span class="th-span-pr">应收（元）</span></th> <th width="15%"> 收支分类子项 <a class="icon-filter pull-right" id="filterFeeType" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="25%">备注</th> <th width="15%"> 关联组织 <a class="icon-filter pull-right" id="filterAssociatedOrg" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="15%"> 关联项目 <a class="icon-filter pull-right" id="filterProjectName" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> </tr> </thead> <tbody> ';
$each(dataList,function(d,$index){
$out+=' <tr > <td>';
$out+=$escape(_momentFormat(d.createDate,'YYYY/MM/DD'));
$out+='</td> <td class="text-right"> <a class="td-span-pr" href="javascript:void(0);" data-action="viewDetail" data-id="';
$out+=$escape(d.id);
$out+='">';
$out+=$escape(_expNumberFilter(d.profitFee));
$out+='</a> </td> <td>';
$out+=$escape(d.feeTypeName);
$out+='</td> <td>';
$out+=$escape(d.feeName);
$out+='</td> <td> ';
if(d.fromCompanyName!=null && d.fromCompanyName!=''){
$out+=' ';
$out+=$escape(d.fromCompanyName);
$out+=' ';
}else if(d.toCompanyName!=null && d.toCompanyName!=''){
$out+=' ';
$out+=$escape(d.toCompanyName);
$out+=' ';
}else{
$out+=' -- ';
}
$out+=' </td> <td> <a href="javascript:void(0);" data-action="goExpensesPage" data-project-id="';
$out+=$escape(d.projectId);
$out+='" data-type="';
$out+=$escape(d.feeType);
$out+='">';
$out+=$escape(d.projectName);
$out+='</a> </td> </tr> ';
});
$out+=' ';
if(!(dataList && dataList.length>0)){
$out+=' <tr> <td colspan="6" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">没有相关数据</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> <div class="pt-absolute "> <span>总应收金额：</span> <span> ';
if(_isBlank(receivaleSum)){
$out+=' <span class="fc-v1-green">0</span> ';
}else if( (receivaleSum+'').indexOf('-')>-1){
$out+=' <span class="fc-v1-red">';
$out+=$escape(_expNumberFilter(receivaleSum));
$out+='</span> ';
}else{
$out+=' <span class="fc-v1-green">';
$out+=$escape(_expNumberFilter(receivaleSum));
$out+='</span> ';
}
$out+=' 元 </span> </div>';
return new String($out);
});/*v:1*/
template('m_payments/m_payments_setFields',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,fieldsList=$data.fieldsList,f=$data.f,fi=$data.fi,$escape=$utils.$escape,c=$data.c,ci=$data.ci,cc=$data.cc,cci=$data.cci,$out='';$out+='<div class="ibox m_payments_setFields no-margin"> <div class="ibox-title" style="min-height: 30px;padding:7px 15px 2px;"> <h4>可选字段</h4> </div> <div class="ibox-content no-padding"> ';
$each(fieldsList,function(f,fi){
$out+=' <div class="panel panel-default"> <div class="panel-body"> <ul class="dd-list"> <li> ';
if(f.expTypeValue!=null && f.expTypeValue!=''){
$out+=' <label class="i-checks fw-normal no-margin col-md-6"> <input name="typeValueCk" type="checkbox" value="';
$out+=$escape(f.expTypeValue);
$out+='" data-key="';
$out+=$escape(f.expTypeKey);
$out+='" data-level="';
$out+=$escape(f.level);
$out+='"/> <span class="i-checks-span">';
$out+=$escape(f.expTypeValue);
$out+='</span> </label> ';
}
$out+=' ';
if(f.childList!=null && f.childList.length>0){
$out+=' <ul class="dd-list"> ';
$each(f.childList,function(c,ci){
$out+=' <li> ';
if(c.expTypeValue!=null && c.expTypeValue!=''){
$out+=' <label class="i-checks fw-normal no-margin col-md-6 "> <input name="typeValueCk" type="checkbox" value="';
$out+=$escape(c.expTypeValue);
$out+='" data-key="';
$out+=$escape(c.expTypeKey);
$out+='" data-level="';
$out+=$escape(c.level);
$out+='"/> <span class="i-checks-span">';
$out+=$escape(c.expTypeValue);
$out+='</span> </label> ';
}
$out+=' ';
if(c.childList!=null && c.childList.length>0){
$out+=' <ul class="dd-list"> ';
$each(c.childList,function(cc,cci){
$out+=' <li> ';
if(cc.expTypeValue!=null && cc.expTypeValue!=''){
$out+=' <label class="i-checks fw-normal no-margin col-md-6"> <input name="typeValueCk" type="checkbox" value="';
$out+=$escape(cc.expTypeValue);
$out+='" data-key="';
$out+=$escape(cc.expTypeKey);
$out+='" data-level="';
$out+=$escape(cc.level);
$out+='"/> <span class="i-checks-span">';
$out+=$escape(cc.expTypeValue);
$out+='</span> </label> ';
}
$out+=' </li> ';
});
$out+=' <div class="clearfix"></div> </ul> ';
}
$out+=' </li> ';
});
$out+=' <div class="clearfix"></div> </ul> ';
}
$out+=' </li> </ul> </div> </div> ';
});
$out+=' </div> </div> ';
return new String($out);
});/*v:1*/
template('m_payments/m_payments_statistics','<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 财务管理 </li> <li class="active fa fa-angle-right"> <strong>分类统计</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding" style=""> <form role="form" class="form-inline m-md"> <div class="form-group z-index-1"> <label class="">当前组织：</label> <div class="btn-group" id="selectOrg"> </div> </div> <div class="form-group"> <label class="m-t-xs">时间单位：</label> <div class="btn-group pull-right"> <a class="btn btn-primary btn-sm m-r-none " href="javascript:void(0)" data-action="setTime" data-type="month">月</a> <a class="btn btn-default btn-sm m-r-none" href="javascript:void(0)" data-action="setTime" data-type="year">年</a> </div> </div> <div class="form-group time-box"> <div class="input-group dp-inline-block"> <input type="text" class="form-control input-sm " id="timeStart" name="timeStart" placeholder="开始月份" readonly style="width: 110px;" > <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> <div class="input-group dp-inline-block"> <input type="text" class="form-control input-sm " id="timeEnd" name="timeEnd" placeholder="结束月份" readonly style="width: 110px;" > <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> </div> <button class="btn btn-white btn-sm" data-action="refreshBtn">刷新数据</button> </form> <div class="ibox m-b-none"> <div class="ibox-title no-borders min-h-50">分类统计范围：</div> <div class="ibox-content no-borders of-hidden m-b-none" id="categoryTypeBox"> </div> </div> <div class="ibox"> <div class="ibox-title no-borders min-h-50 m-b-none p-w-m"> 单位： <a class="btn btn-link text-info" data-action="amountUnit" data-action-type="1">元</a>/ <a class="btn btn-link" data-action="amountUnit" data-action-type="2">万元</a> </div> <div class="ibox-content no-borders of-hidden" id="barChartBox"> </div> </div> </div> </div> <script> </script>');/*v:1*/
template('m_payments/m_payments_statistics_barChart','<style> canvas{ -moz-user-select: none; -webkit-user-select: none; -ms-user-select: none; } #chartjs-tooltip-barChart1,#chartjs-tooltip-barChart2 { opacity: 1; position: absolute; background: rgba(0, 0, 0, .7); color: white; border-radius: 3px; -webkit-transition: all .1s ease; transition: all .1s ease; pointer-events: none; -webkit-transform: translate(-50%, 0); transform: translate(-50%, 0); } .chartjs-tooltip-key { display: inline-block; width: 10px; height: 10px; margin-right: 10px; } </style> <div class="col-md-12 m-b-md"> <canvas id="barChart1" height="80"></canvas> </div> <div class="col-md-12 m-b-md"> <canvas id="barChart2" height="120"></canvas> </div>');/*v:1*/
template('m_payments/m_payments_statistics_categoryType',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,categoryTypeList=$data.categoryTypeList,$each=$utils.$each,c=$data.c,ci=$data.ci,$escape=$utils.$escape,t=$data.t,ti=$data.ti,$out='';$out+='<table class="table table-bordered table-responsive m-b-none"> ';
if(categoryTypeList!=null && categoryTypeList.length>0){
$out+=' ';
$each(categoryTypeList,function(c,ci){
$out+=' <tr class="';
$out+=$escape(ci==0?'gray-bg':'');
$out+='"> ';
$each(c,function(t,ti){
$out+=' <td colspan="';
$out+=$escape(t.level);
$out+='" class="text-center"> ';
if(t.expTypeValue!=null && t.expTypeValue!=''){
$out+=' <div class="check-box"> <label class="i-checks"> ';
if(t.selected==1){
$out+=' <input name="itemCk" type="checkbox" data-pid="';
$out+=$escape(t.pid);
$out+='" data-id="';
$out+=$escape(t.id);
$out+='" data-value="';
$out+=$escape(t.expTypeValue);
$out+='" checked/> ';
}else{
$out+=' <input name="itemCk" type="checkbox" data-pid="';
$out+=$escape(t.pid);
$out+='" data-id="';
$out+=$escape(t.id);
$out+='" data-value="';
$out+=$escape(t.expTypeValue);
$out+='"/> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(t.expTypeValue);
$out+='</span> </label> </div> ';
}
$out+=' </td> ';
});
$out+=' </tr> ';
});
$out+=' ';
}
$out+=' </table>';
return new String($out);
});/*v:1*/
template('m_payments/m_payments_statistics_lineChart','<div class="col-md-12"> <div id="lineChart" class="c3" style="max-height: 320px; position: relative;"></div> </div>');/*v:1*/
template('m_payments/m_payments_statistics_pieChart','<div class="col-md-6"> <div id="totalRevenueChart"></div> </div> <div class="col-md-6"> <div id="totalExpenditureChart"></div> </div> ');/*v:1*/
template('m_personal/m_bindEmail','<form class="form-horizontal rounded-bottom bindEmailBox"> <div class="ibox"> <div class="ibox-content"> <div class="col-md-12 m-b-sm"> <label>邮箱地址</label> <div class="input-group"> <input type="text" class="form-control" name="bindEmailDtoEmail" maxlength="100"> <span class="input-group-btn"> <button class="btn-u rounded" type="button" data-action="sendValidationEemail">发送验证邮件</button> </span> </div> <div class="note">请输入您要绑定的邮箱地址。</div> </div> </div> </div> <div class="clearfix"></div> </form>');/*v:1*/
template('m_personal/m_bindPhone','<form class="form-horizontal rounded-bottom bindPhoneBox"> <div class="panel-body"> <div class="col-md-12 receiveCodeDiv"> <div class="form-group">  <div class="input-group"> <input placeholder="手机号" class="input form-control" type="text" id="cellphone" name="bindCellPhoneDtoCellphone" maxlength="11"> <span class="input-group-btn"> <a type="button" class="btn btn-u" data-action="receiveCode" style="width:96px;">获取验证码</a> </span> </div> </div> </div> <div class="col-md-12"> <div class="form-group">  <input class="form-control" type="text" placeholder="验证码" name="bindCellPhoneDtoCode"> </div> </div> <div class="note"><b>注：</b>更换绑定手机号，即为更改登录账号！</div> </div>      </form>');/*v:1*/
template('m_personal/m_safety',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,userDto=$data.userDto,$out='';$out+='<div class="ibox"> <div class="ibox-content"> <div class="row"> <!--<div class="col-md-12"> <div class="headline"><h4>绑定邮箱</h4></div> <div class="col-md-12"> <p> <span>当前绑定邮箱: <span data-show name="email">';
$out+=$escape(userDto.email);
$out+='</span></span> <a href="JavaScript:void(0);" class="" data-action="bindEmail">修改</a> </p> </div> </div>--> <div class="col-md-12"> <div class="headline"><h4>绑定手机</h4></div> <div class="col-md-12"> <p>当前绑定手机: <span data-show name="cellphone">';
$out+=$escape(userDto.cellphone);
$out+='</span> <a href="JavaScript:void(0);" data-action="bindPhone">修改</a> </p> </div> </div> <div class="col-md-12"> <div class="headline"><h4>修改密码</h4></div> <div class="col-md-12"> <p>修改当前用户的登录密码: <a href="JavaScript:void(0);" data-action="changePwd">修改</a> </p> </div> </div> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_personal/m_uptPassword','<form class="form-horizontal rounded-bottom changePassWordOBox"> <input type="password" style="display:none;"> <div class="panel-body"> <div class="col-md-12"> <div class="form-group"> <label class="col-sm-3 control-label">旧密码<span class="color-red">*</span></label> <div class="col-sm-9"> <input type="password" class="form-control changePwdDtoOldPwd" name="oldPassword" > </div> </div> <div class="form-group"> <label class="col-sm-3 control-label">新密码<span class="color-red">*</span></label> <div class="col-sm-9"> <input type="password" class="form-control changePwdDtoPwd" name="password" id="password"> </div> </div> <div class="form-group"> <label class="col-sm-3 control-label">确认新密码<span class="color-red">*</span></label> <div class="col-sm-9"> <input type="password" class="form-control changePwdDtoRePwd" name="rePassword"> </div> </div> </div> </div>     </form>');/*v:1*/
template('m_personal/m_userInfo',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,userDto=$data.userDto,$each=$utils.$each,p=$data.p,i=$data.i,d=$data.d,di=$data.di,r=$data.r,$index=$data.$index,m=$data.m,mi=$data.mi,$out='';$out+='<div class="ibox m_userInfo"> <div class="ibox-title ibox-title-shadow secondary-menu-outbox"> <div class="row"> <div class="col-md-12" id="secondary-menu"> <div class="pull-left m-r-xl"> <h3 class="dp-inline-block" >个人设置</h3> </div> </div> </div> </div> <div class="ibox-content ibox-content-shadow"> <div class="row"> <div class="col-md-12" id="content-box"> <div class=" user-info col-lg-12 no-padding"> <div class="ibox"> <div class="ibox-content content-basic-info"> <div class="row cube-portfolio editableInfo"> <div class="col-lg-1 cbp-l-grid-agency cbp-caption-active cbp-caption-zoom cbp-ready"> <div class="imgContent cbp-item-wrapper pt-relative pull-left pt-relative" style="width: 110px;height: 110px;overflow: hidden; left:-30px;top:-30px; border:5px solid rgb(255,255,255);box-shadow:0 0 2px #e5e6e7;"> <div class="cbp-caption"> <img alt="image" id="headImage" class=" pull-left" src="';
$out+=$escape(userDto.headImgUrl);
$out+='" style="width: 100%;height: 100%;min-height:100px;"> <!--<span class="btnFilePicker" style="display: none">';
$out+=$escape(userDto.headImg!=null && userDto.headImg!=''?'替换头像':'上传头像');
$out+='</span>--> <div class="cbp-caption-activeWrap dark-transparent-hover"> <div class="btnFilePicker cbp-l-caption-body" title="上传头像"><i class="fa fa-camera" style="color: rgba(255,255,255,0.9);"></i></div> </div> </div> </div> </div> <div class="col-lg-8 m-t-xs"> <h3 class="p-t-15 currentCompanyName f-s-20 fc-dark-blue"> <a href="javascript:void(0);" class="m-l-xs" data-action="text_userName"> ';
$out+=$escape(userDto.userName==null || userDto.userName==''?'未设置':userDto.userName);
$out+=' </a> </h3> <div class="editableInfo m-t-lg m-l-xs"> <div class="row"> <div class="col-md-12"> <p class="f-s-16"> <span class="title-span">绑定手机:</span> <span data-show name="cellphone">';
$out+=$escape(userDto.cellphone);
$out+='</span> <a href="JavaScript:void(0);" data-action="bindPhone">修改</a> </p> </div> <div class="col-md-12"> <p class="f-s-16"> <span class="title-span">用户密码:</span> <a href="JavaScript:void(0);" data-action="changePwd">修改</a> </p> </div> </div> </div> </div> </div> </div> <div class="ibox-content border-no-t"> <div class="row"> <div class="col-md-12"> <div class="" style="margin-bottom: 10px;"><h4 class="title-line">所在组织及权限</h4></div> <div class=""> ';
$each(userDto.orgpermissionList,function(p,i){
$out+=' <div class="clearfix"> <p> <h4 class="dp-inline-block col1" >';
$out+=$escape(p.companyName);
$out+='</h4> <span class="text-muted f-s-14"> ';
if(p.departList && p.departList.length>0){
$out+=' ( ';
$each(p.departList,function(d,di){
$out+=' ';
if(di==p.departList.length-1){
$out+=' ';
$out+=$escape(d.departName);
$out+=' ';
$out+=$escape(d.serverStation==null||d.serverStation==''?'':'- '+d.serverStation);
$out+=' ';
}else{
$out+=' ';
$out+=$escape(d.departName);
$out+=' ';
$out+=$escape(d.serverStation==null||d.serverStation==''?'':'- '+d.serverStation);
$out+=' &nbsp;|&nbsp; ';
}
$out+=' ';
});
$out+=' ) ';
}
$out+=' </span> </p> ';
if(p.roleList && p.roleList.length>0){
$out+=' <ul class="authority-list"> ';
$each(p.roleList,function(r,$index){
$out+=' <li class="m-b-xs"> <span class="font-bold f-s-14 role-name" >';
$out+=$escape(r.name);
$out+='：</span> <span class="text-muted f-s-14"> ';
if(r.permissionList && r.permissionList.length>0){
$out+=' ';
$each(r.permissionList,function(m,mi){
$out+=' ';
if(mi==r.permissionList.length-1){
$out+=' ';
$out+=$escape(m.name);
$out+=' ';
}else{
$out+=' ';
$out+=$escape(m.name);
$out+='、 ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' </span> </li> ';
});
$out+=' </ul> ';
}
$out+=' </div> <div class="headline"></div> ';
});
$out+=' </div> </div> </div> </div> </div> </div> </div> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_personal/m_userInfo_edit',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,userDto=$data.userDto,$out='';$out+='<form class="userInfoForm form-horizontal border-none"> <div class="ibox"> <div class="ibox-content"> <div class="row"> <div class="col-md-12 margin-top-20"> <div class="row cube-portfolio margin-top-20"> <div class="col-sm-offset-1 col-md-offset-1 col-md-3 cbp-l-grid-agency cbp-caption-active cbp-caption-zoom cbp-ready"> <div class="thumbnails thumbnail-style cbp-item-wrapper text-align-center wid-200" style="box-sizing:border-box;padding:9px;border: solid 1px #eee;"> <div class="cbp-caption"> <img class="img-responsive" alt="Picture" src="';
$out+=$escape(userDto.headImgUrl);
$out+='" style="width: 180px;height: 180px;" > <div class="cbp-caption-activeWrap"> <div class="cbp-l-caption-alignCenter"> <div class="cbp-l-caption-body"> <ul class="link-captions no-bottom-space"> <li> <a href="javascript:void(0)" class="btn-u btn-u-primary rounded" id="comUpload" data-action="changeHeadPic" title="点击设置头像">';
$out+=$escape(userDto.headImg!=null && userDto.headImg!=''?'替换头像':'上传头像');
$out+='</a> </li> </ul> </div> </div> </div> </div> </div> </div> <div class="col-sm-offset-1 col-md-offset-1 col-md-5 col-lg-offset-0"> <div class="form-group"> <label class="col-sm-2 control-label">姓名<span class="color-red">*</span>：</label> <div class="col-sm-10"> <input type="text" class="form-control" name="userName" value="';
$out+=$escape(userDto.userName);
$out+='"> <input type="hidden" name="id" value="';
$out+=$escape(userDto.id);
$out+='"> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">性别：</label> <div class="col-sm-10"> <select class="form-control" name="sex"> <option value="男" ';
if(userDto.sex=='男'){
$out+='selected';
}
$out+='>男</option> <option value="女" ';
if(userDto.sex=='女'){
$out+='selected';
}
$out+='>女</option> </select> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">出生日期：</label> <div class="col-sm-10 "> <div class="input-group"> <input type="text" class="form-control" name="birthday" onFocus="WdatePicker()" value="';
$out+=$escape(userDto.birthday);
$out+='"> <span class="input-group-addon"><i class="icon-append fa fa-calendar"></i></span> </div> </div> </div> </div> <div class="col-sm-offset-1 col-md-offset-0 col-md-2 col-lg-offset-1"> <label class="input"> <a href="javascript:void(0)" data-action="cancelEditUserInfo" class="btn-u btn-u-dark-blue rounded">取消</a> <a href="javascript:void(0)" data-action="saveUserInfo" class="btn-u btn-u-dark-blue rounded">保存</a> </label> </div> </div> </div> </div> </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_personal/m_userInfo_edit2',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,userDto=$data.userDto,$out='';$out+='<form class="userInfoForm sky-form border-none"> <div class="col-md-12 userInfoOBox cube-portfolio"> <div class="col-md-3 margin-top-20 cbp-l-grid-agency cbp-caption-active cbp-caption-zoom cbp-ready" > <div class="note"> <b>注</b>：请点击头像进行上传操作！ </div> </div> <div class="col-md-9 margin-top-20"> <div class="col-md-12"> <div class="col-md-5 "> <label class="label">姓名<span class="color-red">*</span></label> <label class="input"> <input type="text" name="userName" value="';
$out+=$escape(userDto.userName);
$out+='"> <input type="hidden" name="id" value="';
$out+=$escape(userDto.id);
$out+='"> </label> </div> <div class="col-md-7 text-right"> <a href="javascript:void(0)" data-action="cancelEditUserInfo" class="btn-u btn-u-default rounded" >取消</a> <a href="javascript:void(0)" data-action="saveUserInfo" class="btn-u rounded" >保存</a> </div> </div> <div class="col-md-12"> <div class="col-md-5"> <label class="label">性别</label> <label class="select"> <select class="curp" name="sex"> <option value="男" ';
if(userDto.sex=='男'){
$out+='selected';
}
$out+='>男</option> <option value="女" ';
if(userDto.sex=='女'){
$out+='selected';
}
$out+='>女</option> </select> <i></i> <span class="whiteMask"></span> </label> </div> </div> <div class="col-md-12"> <div class="col-md-5"> <label class="label">出生日期</label> <label class="input"> <i class="icon-append fa fa-calendar"></i> <input type="text" name="birthday" onFocus="WdatePicker()" value="';
$out+=$escape(userDto.birthday);
$out+='"> </label> </div> </div> </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_personal/m_userInfo_major_edit',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,majorList=$data.majorList,m=$data.m,i=$data.i,majorId=$data.majorId,$escape=$utils.$escape,$out='';$out+='<form class="userInfo_profession_edit border-none"> <div> <div class="row"> ';
$each(majorList,function(m,i){
$out+=' <div class="col-sm-3"> <div class="radio radio-inline"> ';
if(majorId==m.id){
$out+=' <input id="t';
$out+=$escape(i);
$out+='" value="';
$out+=$escape(m.id);
$out+='" name="major" type="radio" checked> ';
}else{
$out+=' <input id="t';
$out+=$escape(i);
$out+='" value="';
$out+=$escape(m.id);
$out+='" name="major" type="radio" > ';
}
$out+=' <label for="t';
$out+=$escape(i);
$out+='"> ';
$out+=$escape(m.name);
$out+=' </label> </div> </div> ';
});
$out+=' <div class="col-md-12"> </div> <div class="clearfix"></div> </div> <div class="row"> <div class="col-md-12 "> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right "> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-r-xs"> <i class="fa fa-check"></i> </button> </div> </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_popConfirm/m_popConfirm','');/*v:1*/
template('m_process/m_process_finance_setting','<div class="m_finance_setting_process" > <div class="col-24-sm-8 col-24-md-8 col-24-lg-6 p-h-xs p-w-xs" id="left-box"> </div> <div class="col-24-sm-16 col-24-md-16 col-24-lg-18 p-h-xs" id="right-box"> </div> </div> ');/*v:1*/
template('m_process/m_process_finance_setting_add',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,typeName=$data.typeName,type=$data.type,processInfo=$data.processInfo,$out='';$out+='<div class="ibox"> <div class="ibox-content no-borders"> <form class="form-horizontal m_finance_setting_process_add"> <div class="form-group "> <label class="col-24-md-5 text-right m-t-sm">';
$out+=$escape(typeName);
$out+='类型：<span class="color-red">*</span>：</label> <div class="col-24-md-19"> <select class="form-control" name="receiptType" > <option value="">请选择</option> ';
if(type==0){
$out+='  <option value="2">合同回款</option> <option value="3">技术审查费</option> <option value="5">合作设计费</option> ';
}else{
$out+='  <option value="4">技术审查费</option> <option value="6">合作设计费</option> ';
}
$out+=' </select> </div> </div> <div class="form-group "> <label class="col-24-md-5 text-right m-t-sm">适用团队：<span class="color-red">*</span>：</label> <div class="col-24-md-19"> <div class="btn-group" id="applicableTeam"> </div> </div> </div> <div class="form-group "> <label class="col-24-md-5 text-right m-t-sm">关联团队：<span class="color-red">*</span>：</label> <div class="col-24-md-19"> <div class="btn-group" id="associatedTeam"> </div> </div> </div> <div class="form-group "> <label class="col-24-md-5 text-right m-t-sm">说明：</label> <div class="col-24-md-19"> <textarea class="form-control b-r-sm" name="description" placeholder="请输入内容">';
$out+=$escape(processInfo!=null?processInfo.description:'');
$out+='</textarea> </div> </div> </form> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_process/m_process_finance_setting_content',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,receiveProcessList=$data.receiveProcessList,$each=$utils.$each,r=$data.r,i=$data.i,$escape=$utils.$escape,payProcessList=$data.payProcessList,$out='';$out+='<table class="table table-hover"> <thead> <tr> <th>流程名称</th> <th>说明</th> <th>适用团队</th> <th>关联团队</th> <th>修改人</th> <th>操作</th> </tr> </thead> <tbody> <tr> <td colspan="7"> 收款计划流程 <button class="btn btn-link" data-action="add" data-action-type="0" title="添加收款计划流程"><i class="fa fa-plus"></i></button> </td> </tr> ';
if(receiveProcessList!=null && receiveProcessList.length>0){
$out+=' ';
$each(receiveProcessList,function(r,i){
$out+=' <tr data-process-type="';
$out+=$escape(r.processType);
$out+='" data-i="';
$out+=$escape(i);
$out+='" data-id="';
$out+=$escape(r.id);
$out+='" data-process-id="';
$out+=$escape(r.processId);
$out+='"> <td> <label class="i-checks"> ';
if(r.status==1){
$out+=' <input class="ck" name="iCheck';
$out+=$escape(r.id);
$out+='" type="radio" checked /> ';
}else{
$out+=' <input class="ck" name="iCheck';
$out+=$escape(r.id);
$out+='" type="radio" /> ';
}
$out+=' ';
$out+=$escape(r.processName);
$out+=' </label> <a href="javascript:void(0);" data-action="itemEdit"><i class="icon iconfont icon-bianji"></i></a> </td> <td>';
$out+=$escape(r.description);
$out+='</td> <td>';
$out+=$escape(r.companyName);
$out+='</td> <td>';
$out+=$escape(r.relationCompanyName);
$out+='</td> <td>';
$out+=$escape(r.updateUserName);
$out+='</td> <td> ';
if(r.isTemplate!='1' ){
$out+=' <button class="btn btn-primary btn-xs " data-action="edit" data-action-type="0">编辑</button> <button class="btn btn-danger btn-xs " data-action="del" data-action-type="0">删除</button> ';
}
$out+=' </td> </tr> ';
});
$out+=' ';
}else{
$out+=' <tr class="no-data-tr"> <td colspan="6" class="text-center v-middle"> 暂无数据 </td> </tr> ';
}
$out+=' <tr> <td colspan="7"> 付款计划流程 <button class="btn btn-link" data-action="add" data-action-type="1" title="添加付款计划流程"><i class="fa fa-plus"></i></button> </td> </tr> ';
if(payProcessList!=null && payProcessList.length>0){
$out+=' ';
$each(payProcessList,function(r,i){
$out+=' <tr data-process-type="';
$out+=$escape(r.processType);
$out+='" data-i="';
$out+=$escape(i);
$out+='" data-id="';
$out+=$escape(r.id);
$out+='" data-process-id="';
$out+=$escape(r.processId);
$out+='"> <td> <label class="i-checks"> ';
if(r.status==1){
$out+=' <input class="ck" name="iCheck';
$out+=$escape(r.id);
$out+='" type="radio" checked /> ';
}else{
$out+=' <input class="ck" name="iCheck';
$out+=$escape(r.id);
$out+='" type="radio" /> ';
}
$out+=' ';
$out+=$escape(r.processName);
$out+=' </label> </td> <td>';
$out+=$escape(r.description);
$out+='</td> <td>';
$out+=$escape(r.companyName);
$out+='</td> <td>';
$out+=$escape(r.relationCompanyName);
$out+='</td> <td>';
$out+=$escape(r.updateUserName);
$out+='</td> <td> ';
if(r.isTemplate!='1' ){
$out+=' <button class="btn btn-primary btn-xs " data-action="edit" data-action-type="1">编辑</button> <button class="btn btn-danger btn-xs " data-action="del" data-action-type="1">删除</button> ';
}
$out+=' </td> </tr> ';
});
$out+=' ';
}else{
$out+=' <tr class="no-data-tr"> <td colspan="6" class="text-center v-middle"> 暂无数据 </td> </tr> ';
}
$out+=' </tbody> </table>';
return new String($out);
});/*v:1*/
template('m_process/m_process_finance_setting_contractPayment',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,listProcessNode=$data.listProcessNode,$each=$utils.$each,n=$data.n,i=$data.i,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-title ibox-title-min"> <a href="javascript:void(0);" class="btn btn-link no-padding" data-action="back">返回项目收支流程</a> </div> <div class="ibox-content"> <table class="table table-hover"> <thead> <tr> <th>合同回款流程</th> <th>说明</th> <th>操作人</th> <th>关联内容</th> <th>应收状态</th> <th>到账状态</th> </tr> </thead> <tbody> ';
if(listProcessNode!=null && listProcessNode.length>0){
$out+=' ';
$each(listProcessNode,function(n,i){
$out+=' <tr data-id="';
$out+=$escape(n.id);
$out+='" data-process-id="';
$out+=$escape(n.processId);
$out+='"> <td> <a href="javascript:void(0);" class="btn btn-link no-padding ';
$out+=$escape(i==0?'active':'');
$out+='" data-action="viewTemplate" data-action-type="';
$out+=$escape(i);
$out+='">';
$out+=$escape(n.nodeName);
$out+='</a> </td> <td> ';
$out+=$escape(n.description);
$out+=' </td> <td>';
$out+=$escape(n.operatorName);
$out+='</td> <td class="check-box"> <label class="i-checks"> ';
if(n.invoiceStatus==1){
$out+=' <input class="ck" name="iCheck" type="checkbox" checked data-type="1"/> <span class="i-checks-span">开票信息</span> ';
}else if(n.invoiceStatus==0){
$out+=' <input class="ck" name="iCheck" type="checkbox" data-type="1"/> <span class="i-checks-span">开票信息</span> ';
}else{
$out+=' ';
}
$out+=' </label> </td> <td> <label class="i-checks"> ';
if(n.receiveOrPayAbleStatus==1){
$out+=' <input class="ck" name="iCheck" type="checkbox" checked data-type="2" /> ';
}else if(n.receiveOrPayAbleStatus==0){
$out+=' <input class="ck" name="iCheck" type="checkbox" data-type="2" /> ';
}else{
$out+=' ';
}
$out+=' </label> </td> <td> <label class="i-checks"> ';
if(n.receiveOrPayStatus==1){
$out+=' <input class="ck" name="iCheck" type="checkbox" checked data-type="4" /> ';
}else if(n.receiveOrPayStatus==0){
$out+=' <input class="ck" name="iCheck" type="checkbox" data-type="4" /> ';
}else{
$out+=' ';
}
$out+=' </label> </td> </tr> ';
});
$out+=' ';
}else{
$out+=' <tr class="no-data"> <td colspan="6" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">暂无相关数据</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> <div class="row"> <div class="col-md-6"> <div class="panel panel-default" id="template0"> <div class="panel-heading"> 填写收款计划录入内容 </div> <div class="panel-body"> <form class="form-horizontal"> <div class="form-group"> <label class="col-sm-2 control-label">收款类型：</label> <div class="col-sm-10"> <select class="form-control" > <option value="">请选择</option> <option value="0">合同回款</option> <option value="1">技术审查费</option> <option value="2">合作设计费</option> </select> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">收款类型：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="请输入收款金额"/> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">付款单位：</label> <div class="col-sm-10"> <select class="form-control" > <option value="">甲方单位</option> </select> </div> </div> </form> </div> </div> <div class="panel panel-default" id="template1" style="display: none;"> <div class="panel-heading"> 填写节点信息录入内容 </div> <div class="panel-body"> <form class="form-horizontal"> <div class="form-group"> <label class="col-sm-2 control-label">节点信息：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="项目签订后支付定金20%"></input> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">比例：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="20%"></input> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">金额（万元）：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="400万元"></input> </div> </div> </form> </div> </div> <div class="panel panel-default" id="template2" style="display: none;"> <div class="panel-heading"> 发起回款申请<a href="javascript:void(0)" class="btn btn-primary btn-sm pull-right">增加开票信息</a> </div> <div class="panel-body"> <form class="form-horizontal"> <div class="form-group"> <label class="col-sm-2 control-label">申请人：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="当前项目的经营负责人、经营助理"></input> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">金额（万元）：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="400万元"></input> </div> </div> <a class="close-link pull-right"> <i class="fa fa-times"></i> </a> <div class="clearfix"></div> <div class="hr-line-solid"></div> <div class="form-group"> <label class="col-sm-2 control-label">申请日期：</label> <div class="col-sm-10"> <input type="text" class="form-control" value="" placeholder="yyyy-MM-dd"></input> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">收票方名称：</label> <div class="col-sm-10"> <select class="form-control"> <option value="">请选择</option> </select> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">纳税识别号：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="9144444444444444444"></input> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">开票金额：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="请输入开票金额"></input> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">开票内容：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="请输入开票内容"></input> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">备注：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="请输入备注"></input> </div> </div> </form> </div> </div> <div class="panel panel-default" id="template3" style="display: none;"> <div class="panel-heading"> 填写到账金额及日期 </div> <div class="panel-body"> <form class="form-horizontal"> <div class="form-group"> <label class="col-sm-2 control-label">到账金额：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="100万元"></input> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">到账日期：</label> <div class="col-sm-10"> <input type="text" class="form-control" placeholder="yyyy-MM-dd"></input> </div> </div> </form> </div> </div> </div> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_process/m_process_finance_setting_technicalReviewFee',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,listProcessNode=$data.listProcessNode,$each=$utils.$each,n=$data.n,i=$data.i,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-title ibox-title-min"> <a href="javascript:void(0);" class="btn btn-link no-padding" data-action="back">返回项目收支流程</a> </div> <div class="ibox-content"> <table class="table table-hover"> <thead> <tr> <th>技术审查费收款流程</th> <th>说明</th> <th>操作人</th> <th>关联内容</th> <th>同步信息</th> <th>应收状态</th> <th>到账状态</th> </tr> </thead> <tbody> ';
if(listProcessNode!=null && listProcessNode.length>0){
$out+=' ';
$each(listProcessNode,function(n,i){
$out+=' <tr data-id="';
$out+=$escape(n.id);
$out+='" data-process-id="';
$out+=$escape(n.processId);
$out+='"> <td> <a href="javascript:void(0);" class="btn btn-link no-padding ';
$out+=$escape(i==0?'active':'');
$out+='" data-action="viewTemplate" data-action-type="';
$out+=$escape(i);
$out+='">';
$out+=$escape(n.nodeName);
$out+='</a> </td> <td> ';
$out+=$escape(n.description);
$out+=' </td> <td>';
$out+=$escape(n.operatorName);
$out+='</td> <td class="check-box"> <label class="i-checks"> ';
if(n.invoiceStatus==1){
$out+=' <input class="ck" name="iCheck" type="checkbox" checked data-type="1"/> <span class="i-checks-span">开票信息</span> ';
}else if(n.invoiceStatus==0){
$out+=' <input class="ck" name="iCheck" type="checkbox" data-type="1"/> <span class="i-checks-span">开票信息</span> ';
}else{
$out+=' ';
}
$out+=' </label> </td> <td> <label class="i-checks"> ';
if(n.syncStatus==1){
$out+=' <input class="ck" name="iCheck" type="checkbox" checked data-type="6" /> ';
}else if(n.syncStatus==0){
$out+=' <input class="ck" name="iCheck" type="checkbox" data-type="6" /> ';
}else{
$out+=' ';
}
$out+=' </label> </td> <td> <label class="i-checks"> ';
if(n.receiveOrPayAbleStatus==1){
$out+=' <input class="ck" name="iCheck" type="checkbox" checked data-type="2" /> ';
}else if(n.receiveOrPayAbleStatus==0){
$out+=' <input class="ck" name="iCheck" type="checkbox" data-type="2" /> ';
}else{
$out+=' ';
}
$out+=' </label> </td> <td> <label class="i-checks"> ';
if(n.receiveOrPayStatus==1){
$out+=' <input class="ck" name="iCheck" type="checkbox" checked data-type="4" /> ';
}else if(n.receiveOrPayStatus==0){
$out+=' <input class="ck" name="iCheck" type="checkbox" data-type="4" /> ';
}else{
$out+=' ';
}
$out+=' </label> </td> </tr> ';
});
$out+=' ';
}else{
$out+=' <tr class="no-data"> <td colspan="7" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">暂无相关数据</span> </div> </td> </tr> ';
}
$out+=' <tr> <td colspan="7" class="text-muted"> 注释：如无需财务进行确认到账金额及日期确认时，请在 【发起收款申请】后勾选到款状态 </td> </tr> </tbody> </table> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_production/m_choseUserBox','<div class="m_choseUserBox"> <div class="pt-relative bg-color-white"> <div class="col-sm-12"> <span>选择部门: </span> <button type="button" class="btn btn-primary btn-xs btn-filter" name="btn-filter"> <span class="btn-filter-name" id="orgName"></span> <span class="btn-filter-icon caret"></span> </button> </div> <div class="col-sm-12" style="height: 10px;"> <hr class="line-class"/> </div> <div class="space-15"></div> <div class="chose-user-tree hide" id="organizationTree" > <ul class="sidebar-nav list-group sidebar-nav-v1"> </ul> </div> </div> <div class="pt-relative"> <div class="col-sm-12"> <div id="userListBox" style="height: 385px;"> </div> <div id="userlist-pagination-container" class="m-pagination pull-right m-pagination-cus" style="height: 25px;"></div> </div> </div> </div>');/*v:1*/
template('m_production/m_choseUserList',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,orgUserList=$data.orgUserList,u=$data.u,$index=$data.$index,$escape=$utils.$escape,$out='';$out+='<table class="table table-striped table-hover table-responsive m-b-none" > <thead> <tr> <th>姓名</th> <th>手机号</th> <th></th> </tr> </thead> <tbody> ';
$each(orgUserList,function(u,$index){
$out+=' <tr> <td>';
$out+=$escape(u.userName);
$out+='</td> <td>';
$out+=$escape(u.cellphone);
$out+='</td> <td class="pt-relative "> <!--<a href="javascript:void(0)" data-action="choseUser" data-companyUserId="';
$out+=$escape(u.id);
$out+='" data-userId="';
$out+=$escape(u.userId);
$out+='" class="btn-u btn-u-primany btn-u-xs rounded">选择</a>--> <a type="button" class="btn btn-default btn-xs dropdown-toggle btnReturnFalse" data-toggle="dropdown" aria-expanded="true"> 添加到...<i class="fa fa-angle-down btnReturnFalse"></i> </a> <ul class="dropdown-menu dropdown-menu-v1" role="menu" style="left: -108px;"> <li> <a href="javascript:void(0)" data-id="';
$out+=$escape(u.id);
$out+='" data-user-id="';
$out+=$escape(u.userId);
$out+='" data-action="choseOrgUser" data-i="1">设计</a> </li> <li class="divider"></li> <li> <a href="javascript:void(0)" data-id="';
$out+=$escape(u.id);
$out+='" data-user-id="';
$out+=$escape(u.userId);
$out+='" data-action="choseOrgUser" data-i="2">校对</a> </li> <li class="divider"></li> <li> <a href="javascript:void(0)" data-id="';
$out+=$escape(u.id);
$out+='" data-user-id="';
$out+=$escape(u.userId);
$out+='" data-action="choseOrgUser" data-i="3">审核</a> </li> </ul> </td> </tr> ';
});
$out+=' ';
if(orgUserList==null || orgUserList.length==0){
$out+=' <tr> <td colspan="3" align="center">暂无数据</td> </tr> ';
}
$out+=' </tbody> <tfoot><tr><td colspan="3" id="userList"></td></tr></tfoot> </table> ';
return new String($out);
});/*v:1*/
template('m_production/m_confirmCompletion',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,currDate=$data.currDate,$out='';$out+='<form class="form-horizontal m_confirmCompletion m"> <div class="form-group "> <label class="col-md-3 control-label no-pd-left m-t-xs">完成时间：</label> <div class="col-md-8 no-pd-right"> <div class="input-group"> <input type="text" class="form-control input-sm " name="completeDate" onclick="WdatePicker()" value="';
$out+=$escape(currDate);
$out+='"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> </div> </div> <div class="form-group "> <label class="col-md-3 control-label no-pd-left m-t-xs">完成情况：</label> <div class="col-md-8 no-pd-right"> <textarea class="form-control" name="completion" style="height: 50px;width: 100%;"></textarea> </div> </div>  </form>';
return new String($out);
});/*v:1*/
template('m_production/m_deliveryHistory',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,listDeliver=$data.listDeliver,$each=$utils.$each,d=$data.d,i=$data.i,$escape=$utils.$escape,_momentFormat=$helpers._momentFormat,r=$data.r,ri=$data.ri,$out='';$out+='<table class="table table-bordered"> <thead> <tr> <th width="15%">交付名称</th> <th width="15%">说明</th> <th width="16%">时间</th> <th width="14%">负责人</th> <th width="10%">发起人</th> <th width="10%">发起时间</th> <th width="10%">状态</th> <th width="10%">操作</th> </tr> </thead> <tbody> ';
if(listDeliver!=null && listDeliver.length>0){
$out+=' ';
$each(listDeliver,function(d,i){
$out+=' <tr data-id="';
$out+=$escape(d.id);
$out+='" data-i="';
$out+=$escape(i);
$out+='"> <td> <a href="javascript:void(0)" class="editable editable-disabled" data-action="xeditable" data-action-type="text" data-field="taskName"> ';
$out+=$escape(d.name);
$out+=' </a> </td> <td> <a href="javascript:void(0)" class="editable editable-disabled" data-action="xeditable" data-action-type="textarea" data-field="remarks"> ';
$out+=$escape(d.description);
$out+=' </a> </td> <td> <a href="javascript:void(0)" class="editable editable-click editable-disabled" data-action="xeditable" data-action-type="time" data-value="';
$out+=$escape(d.endTime);
$out+='"> ';
$out+=$escape(_momentFormat(d.endTime,'YYYY/MM/DD'));
$out+=' </a> </td> <td> ';
if(d.responseList!=null && d.responseList.length>0){
$out+=' <a href="javascript:void(0)" class="editable editable-click editable-disabled" data-action="xeditable" data-action-type="selectUser"> ';
$each(d.responseList,function(r,ri){
$out+=' ';
if(ri==d.responseList.length-1){
$out+=' <span data-id="';
$out+=$escape(r.id);
$out+='">';
$out+=$escape(r.name);
$out+='</span> ';
}else{
$out+=' <span data-id="';
$out+=$escape(r.id);
$out+='">';
$out+=$escape(r.name);
$out+='</span>, ';
}
$out+=' ';
});
$out+=' </a> ';
}
$out+=' </td> <td> ';
$out+=$escape(d.createByName);
$out+=' </td> <td> ';
$out+=$escape(d.createDate);
$out+=' </td> <td> <a href="javascript:void(0)" class="editable editable-disabled" data-action="xeditable" data-action-type="select"> ';
if(d.isFinished==0){
$out+=' 未完成 ';
}else{
$out+=' 已完成 ';
}
$out+=' </a> </td> <td> <a href="javascript:void(0);" data-action="view" data-id="" ><i class="icon iconfont icon-chakan"></i></a> <a href="javascript:void(0);" data-action="edit" data-id="" ><i class="icon iconfont icon-bianji"></i></a> <a href="javascript:void(0);" data-action="delete" data-id="" ><i class="icon iconfont icon-lajitong text-danger"></i></a> </td> </tr> ';
});
$out+=' ';
}else{
$out+=' <tr> <td class="text-center" colspan="8"> 暂无数据 </td> </tr> ';
}
$out+=' </tbody> </table>';
return new String($out);
});/*v:1*/
template('m_production/m_initiateDelivery','<form class="form-horizontal m"> <div class="form-group"> <label class="col-sm-2 control-label">名称：</label> <div class="col-sm-10"> <input class="form-control" type="text" name="taskName"> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">说明：</label> <div class="col-sm-10"> <textarea class="form-control" name="remarks"></textarea> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">截止时间：</label> <div class="col-sm-10 "> <div class="input-group"> <input type="text" class="form-control input-sm" name="deadline" placeholder="请选择时间" readonly onFocus="WdatePicker()"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" ></i> </span> </div> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">负责人：</label> <div class="col-sm-10 p-t-7"> <input type="text" class="form-control input-sm hide" name="personInCharge"> <a href="javascript:void(0);" data-action="addPersonInCharge"><i class="fa fa-plus-circle"></i></a> </div> </div> </form>');/*v:1*/
template('m_production/m_production',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,orgList=$data.orgList,$each=$utils.$each,o=$data.o,$index=$data.$index,dataCompanyId=$data.dataCompanyId,currentManagerObj=$data.currentManagerObj,currentCompanyId=$data.currentCompanyId,currentCompanyUserId=$data.currentCompanyUserId,$out='';$out+='<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-4">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 我的项目 </li> <li class=" fa fa-angle-right"> ';
$out+=$escape(projectName);
$out+=' </li> <li class="active fa fa-angle-right"> <strong>生产安排</strong> </li> </ol> </div> </div> <div class="col-md-8 text-right" style="padding: 9px 15px;"> ';
if(orgList!=null && orgList.length>0){
$out+=' <strong>当前视图：</strong> <select name="viewByOrg" class="wid-200"> ';
$each(orgList,function(o,$index){
$out+=' ';
if(dataCompanyId == o.id){
$out+=' <option value="';
$out+=$escape(o.id);
$out+='" selected>';
$out+=$escape(o.companyName);
$out+='</option> ';
}else{
$out+=' <option value="';
$out+=$escape(o.id);
$out+='">';
$out+=$escape(o.companyName);
$out+='</option> ';
}
$out+=' ';
});
$out+=' </select> <span class="icon-separation-line"></span> ';
}
$out+=' <span class="">设计负责人：</span> <span class="m-r-sm"> ';
$out+=$escape(currentManagerObj.projectManager?currentManagerObj.projectManager.companyUserName:'');
$out+=' ';
if(currentManagerObj.projectManager.isUpdateOperator==1 && currentCompanyId==dataCompanyId){
$out+=' <a href="javascript:void(0)" data-action="changeOperatorPerson" data-i="0" data-id="';
$out+=$escape(currentManagerObj.projectManager.companyUserId);
$out+='" data-company-id="';
$out+=$escape(currentManagerObj.projectManager.companyId);
$out+='" data-user-name="';
$out+=$escape(currentManagerObj.projectManager.companyUserName);
$out+='" class="showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="更换设计负责人"> <i class="fa fa-retweet"></i> </a> ';
}
$out+=' </span> <span>设计助理：</span> <span class="m-r-sm"> ';
$out+=$escape(currentManagerObj.assistant==null || currentManagerObj.assistant.companyUserName==null?'未设置':currentManagerObj.assistant.companyUserName);
$out+=' ';
if(currentManagerObj.projectManager.companyUserId==currentCompanyUserId && currentCompanyId==dataCompanyId){
$out+=' ';
if(currentManagerObj.assistant!=null){
$out+=' <a href="javascript:void(0)" data-action="changeManagerPerson" data-i="0" data-id="';
$out+=$escape(currentManagerObj.assistant.companyUserId);
$out+='" data-user-name="';
$out+=$escape(currentManagerObj.assistant.companyUserName);
$out+='" data-company-id="';
$out+=$escape(currentManagerObj.assistant.companyId);
$out+='" class="showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="更换设计助理"> <i class="fa fa-retweet"></i> </a> ';
}else{
$out+=' <a href="javascript:void(0)" data-action="changeManagerPerson" data-i="0" data-id="" data-user-name="" data-company-id="" class="showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="更换助理"> <i class="fa fa-retweet"></i> </a> ';
}
$out+=' ';
}
$out+=' </span> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding" id="tabContent"> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_production/m_productionSchedule',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,taskScheduleChangesList=$data.taskScheduleChangesList,$escape=$utils.$escape,$each=$utils.$each,v=$data.v,$index=$data.$index,$out='';$out+='<form class="form-horizontal rounded-bottom taskProgressOBox noborder"> <div class="ibox"> <div class="ibox-content"> <div class="col-md-12"> <div class="col-md-2 text-right" style="line-height: 22px;"> <label> <b>任务计划进度:</b> </label> </div> ';
if(taskScheduleChangesList && taskScheduleChangesList.length>0){
$out+=' <div class="col-md-7" style="line-height: 22px;"> ';
$out+=$escape(taskScheduleChangesList[0].startTime);
$out+=' ';
if((taskScheduleChangesList[0].startTime!=null && taskScheduleChangesList[0].startTime!='')
                    || (taskScheduleChangesList[0].endTime!=null && taskScheduleChangesList[0].endTime!='')){
$out+=' <span>~</span> ';
}
$out+=' ';
$out+=$escape(taskScheduleChangesList[0].endTime);
$out+=' ';
if((taskScheduleChangesList[0].startTime!=null && taskScheduleChangesList[0].startTime!='')
                    && (taskScheduleChangesList[0].endTime!=null && taskScheduleChangesList[0].endTime!='')){
$out+=' ';
if(taskScheduleChangesList[0].allDay!=null){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(taskScheduleChangesList[0].allDay-0);
$out+='</span>天 )</span> ';
}
$out+=' ';
}
$out+=' </div> <div class="col-md-3"> <a href="javascript:void(0)" data-action="addScheduleProgressChange" class="btn-u btn-u-xs rounded ';
$out+=$escape(taskScheduleChangesList && taskScheduleChangesList.length>1?'hide':'');
$out+='" data-id="';
$out+=$escape(taskScheduleChangesList[0].id);
$out+='" data-type="';
$out+=$escape(taskScheduleChangesList[0].type);
$out+='" data-list-type="1" data-start-time="';
$out+=$escape(taskScheduleChangesList[0].startTime);
$out+='" data-end-time="';
$out+=$escape(taskScheduleChangesList[0].endTime);
$out+='" > ';
$out+=$escape(taskScheduleChangesList[0].type==3?'修改':'变更');
$out+=' </a> <!--<a href="javascript:void(0)" data-action="delScheduleProgressChange" data-seq="0" data-id="';
$out+=$escape(taskScheduleChangesList[0].id);
$out+='" class="btn-u btn-u-xs btn-u-red rounded ';
if(taskScheduleChangesList && taskScheduleChangesList.length>1){
$out+='hide';
}
$out+='" >删除</a>--> </div> ';
}
$out+=' <div class="clearfix"></div> </div> ';
$each(taskScheduleChangesList,function(v,$index){
$out+=' ';
if($index>0){
$out+=' <div class="col-md-12"> <div class="col-md-2 text-right" style="line-height: 22px;"> <label> <b>第';
$out+=$escape($index);
$out+='次变更:</b> </label> </div> <div class="col-md-7" style="line-height: 22px;"> ';
$out+=$escape(v.startTime);
$out+=' ';
if((v.startTime!=null && v.startTime!='') || (v.endTime!=null && v.endTime!='')){
$out+=' <span>~</span> ';
}
$out+=' ';
$out+=$escape(v.endTime);
$out+=' ';
if((v.startTime!=null && v.startTime!='') && (v.endTime!=null && v.endTime!='')){
$out+=' ';
if(v.timeDiffStr!=null){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(v.timeDiffStr-0);
$out+='</span>天 )</span> ';
}
$out+=' ';
}
$out+=' </div> <div class="col-md-3"> <a href="javascript:void(0)" data-action="addScheduleProgressChange" class="btn-u btn-u-xs rounded ';
$out+=$escape(taskScheduleChangesList && taskScheduleChangesList.length!=$index+1?'hide':'');
$out+='" data-id="';
$out+=$escape(v.id);
$out+='" data-type="';
$out+=$escape(v.type);
$out+='" data-start-time="';
$out+=$escape(v.startTime);
$out+='" data-end-time="';
$out+=$escape(v.endTime);
$out+='"> ';
$out+=$escape(v.type==3?'修改':'变更');
$out+=' </a> <!--<a href="javascript:void(0)" data-action="delScheduleProgressChange" data-id="';
$out+=$escape(v.id);
$out+='" class="btn-u btn-u-xs btn-u-red rounded ';
if(taskScheduleChangesList && taskScheduleChangesList.length!=$index+1){
$out+='hide';
}
$out+='">删除</a>--> </div> <div class="col-md-10 col-md-offset-2 "> <label class="" style="position: relative;line-height:18px;word-break: break-all;"> <b>变更原因：</b> <span class="memo-span">';
$out+=$escape(v.memo);
$out+='</span> </label> <div class="clearfix"></div> </div> <div class="clearfix"></div> </div> ';
}
$out+=' ';
});
$out+=' </div> </div> </form> ';
return new String($out);
});/*v:1*/
template('m_production/m_production_add',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,personInCharge=$data.personInCharge,personInChargeId=$data.personInChargeId,$escape=$utils.$escape,appointmentStartTime=$data.appointmentStartTime,appointmentEndTime=$data.appointmentEndTime,$out='';$out+='<style> .add-issue-box{width:410px; } .add-issue-box .form-group .form-control{width: 100%;} .add-issue-box .select2.select2-container{width: 293px !important;} .add-issue-box .select2.select2-container span.select2-selection{border-radius: 0;height: 34px;} .add-issue-box .select2-container--default .select2-selection--single .select2-selection__arrow b{border-width: 6px 3px 0px 3px;margin-left: -2px;margin-top: 1px;} </style> <form class="add-issue-form"> <div class="add-issue-box"> <div class="form-group m-b-xs clearfix"> <label class="col-sm-3 control-label no-pd-left m-t-xs" >设计任务<span class="color-red">*</span></label> <div class="col-sm-9 no-pd-right"> <input placeholder="设计任务" class="form-control" type="text" name="taskName" maxlength="100"> </div> </div> <div class="form-group operator-div m-b-xs clearfix"> <label class="col-sm-3 control-label no-pd-right no-pd-left m-t-xs">任务负责人<span class="color-red">*</span></label> <div class="col-sm-9 no-pd-right"> <select class="js-example-disabled-results form-control" name="designerId" style="width:278px;"> ';
if(personInCharge!=null && personInCharge!='' && personInChargeId!=null && personInChargeId!=''){
$out+=' <option value="';
$out+=$escape(personInChargeId);
$out+='">';
$out+=$escape(personInCharge);
$out+='</option> ';
}
$out+=' </select> </div> </div> <div class="form-group time-box m-b-xs clearfix"> <label class="col-sm-3 control-label no-pd-left no-pd-right m-t-xs">计划开始时间<span class="color-red">*</span></label> <div class="col-sm-9 no-pd-right"> <div class="input-group"> <input type="text" class="form-control timeInput startTime input-sm" id="ipt_startTime" name="startTime" data-appointmentStartTime = "';
$out+=$escape(appointmentStartTime);
$out+='" placeholder="开始日期" readonly onFocus="startTimeFun(this,m_inputProcessTime_onpicked)" value="';
$out+=$escape(appointmentStartTime);
$out+='"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </div> </div> <div class="form-group time-box m-b-xs clearfix"> <label class="col-sm-3 control-label no-pd-left no-pd-right m-t-xs">计划结束时间<span class="color-red">*</span></label> <div class="col-sm-9 no-pd-right"> <div class="input-group"> <input type="text" class="form-control timeInput endTime input-sm" id="ipt_endTime" name="endTime" data-appointmentEndTime = "';
$out+=$escape(appointmentEndTime);
$out+='" placeholder="结束日期" readonly onFocus="endTimeFun(this,m_inputProcessTime_onpicked)" value="';
$out+=$escape(appointmentEndTime);
$out+='"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </div> </div> <div class="form-group m-b-xs clearfix"> <label class="col-sm-3 control-label no-pd-left m-t-xs" >任务描述</label> <div class="col-sm-9 no-pd-right"> <textarea class="form-control" name="taskRemark" style="height: 80px;max-width: 100%;"></textarea> </div> </div> <div class="col-md-12 m-b-sm no-pd-right clearfix"> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right "> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-r-xs"> <i class="fa fa-check"></i> </button> </div> </div> </form> ';
return new String($out);
});/*v:1*/
template('m_production/m_production_edit','<form class="form"> <div style="width: 380px;"> <div class="form-group m-b-xs clearfix"> <label class="col-md-3 control-label no-pd-left m-t-xs">设计任务</label> <div class="col-md-9 no-pd-right"> <input type="text" class="form-control input-sm" name="taskName" > </div> </div> <div class="form-group m-b-xs clearfix"> <label class="col-md-3 control-label no-pd-left m-t-xs">任务描述</label> <div class="col-md-9 no-pd-right"> <textarea class="form-control" name="taskRemark" style="height: 80px;max-width: 100%;"></textarea> </div> </div> <div class="form-group m-b-n clearfix"> <div class="col-md-12 no-pd-right"> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right m-b-sm"> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-b-sm m-r-xs"> <i class="fa fa-check"></i> </button> </div> </div> </div> </form>');/*v:1*/
template('m_production/m_production_editRemark',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,remark=$data.remark,$out='';$out+='<form class="form"> <div class="view-remark-box" style="display: none;"> <div class="col-md-12 "> ';
$out+=$escape(remark);
$out+=' <a class="" href="javascript:void(0);" data-action="editRemark"> <i class="fa fa-pencil-square-o"></i> </a> </div> </div> <div class="edit-remark-box"> <div class="col-md-12 " style="border-bottom: solid 1px #ccc;margin-bottom: 10px;"> <textarea style="width: 100%;min-height: 110px;margin: 10px 0;">';
$out+=$escape(remark);
$out+='</textarea> </div> <div class="col-md-12"> <button type="button" class="btn btn-default btn-sm pull-right m-b-sm" data-action="cancel">取消</button> <button type="button" class="btn btn-primary btn-sm pull-right m-b-sm m-r-xs" data-action="submit">确认</button> </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_production/m_production_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,productionList=$data.productionList,t=$data.t,ti=$data.ti,$escape=$utils.$escape,currentCompanyUserId=$data.currentCompanyUserId,u=$data.u,$index=$data.$index,_isNullOrBlank=$helpers._isNullOrBlank,_momentFormat=$helpers._momentFormat,_timeDifference=$helpers._timeDifference,_url=$helpers._url,$out='';$out+='<form class="m_production_list task-list-box" style="overflow-x: auto;overflow-y:hidden; min-height: 280px;"> <table class="tree tree-box table table-bordered table-striped" style="min-width: 1500px;margin-bottom: 140px;"> <thead> <tr> <th width="4%" class="b-r-none"></th> <th width="18%" class="b-r-none b-l-none" style="padding-left: 38px;min-width: 400px;">设计任务</th> <th width="5%" class="b-r-none">任务描述</th> <th width="6%" class="b-r-none">任务负责人</th> <th width="9%" class="b-r-none">设计人员</th> <th width="8%" class="b-r-none">校对人员</th> <th width="8%" class="b-r-none">审核人员</th> <th width="14%" class="b-r-none" >计划进度</th> <th width="6%" class="b-r-none" >进度提示</th> <th width="7%" class="b-r-none" >完成时间</th> <th width="5%" class="b-r-none" >完成情况</th> <th width="5%" class="b-r-none">任务状态</th> <th width="5%" class="">优先级</th> </tr> </thead> ';
$each(productionList,function(t,ti){
$out+=' <tr class="tree-box-tr treegrid-';
$out+=$escape(t.id);
$out+=' ';
if(t.taskPid!=null && t.taskPid!=''){
$out+=' treegrid-parent-';
$out+=$escape(t.taskPid);
$out+=' ';
}
$out+=' ';
$out+=$escape(t.completeDate==null?'':'completeDate-tr');
$out+=' " data-i="';
$out+=$escape(ti);
$out+='" data-id="';
$out+=$escape(t.id);
$out+='" data-pid="';
$out+=$escape(t.taskPid);
$out+='" data-company-id="';
$out+=$escape(t.companyId);
$out+='" data-publish-id="';
$out+=$escape(t.publishId);
$out+='"> <td class="b-r-none v-middle"> <div class="list-action-box"> <div class="btn-group singleOperation" style="display: none;"> <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true"> <span class="ic-operation"></span> </button> <ul class="dropdown-menu"> ';
if(t.roleFlag!=null && t.roleFlag.flag1!=null && !(t.taskState=='3'||t.taskState=='4') && t.gradeStatus !=1  && t.isOperaterTask==0){
$out+=' <li><a href="javascript:void(0);" data-action="addSubTask">添加子任务</a></li> ';
}
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag7!=null){
$out+=' <li><a href="javascript:void(0);" data-action="addSubTaskByDesigner">分解设计任务</a></li> ';
}
$out+='  ';
if(t.isOperaterTask==0 && t.completeDate ==null && t.personInChargeId == currentCompanyUserId){
$out+=' <li><a href="javascript:void(0);" data-action="completeTask" data-status="1" data-original-title="请点击确定完成任务">任务确认完成</a></li> ';
}
$out+='  ';
if(t.isOperaterTask==0 && t.completeDate !=null && t.personInChargeId == currentCompanyUserId){
$out+=' <li><a href="javascript:void(0);" data-action="completeTask" data-status="0" data-original-title="请点击确定激活任务">激活任务</a></li> ';
}
$out+='  ';
if(!t.first){
$out+=' <li><a href="javascript:void(0);" data-action="moveUp">向上移动</a></li> ';
}
$out+='  ';
if(!t.last){
$out+=' <li><a href="javascript:void(0);" data-action="moveDown">向下移动</a></li> ';
}
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag6!=null){
$out+=' <li><a href="javascript:void(0);" data-action="delTask" data-canbedelete="">删除</a></li> ';
}
$out+=' </ul> </div> </div> </td> <td class="treeTd pt-relative popover-box no-pd-right b-r-none b-l-none v-middle" height="40" data-task-state="';
$out+=$escape(t.taskState=='3'||t.taskState=='4'?'1':'0');
$out+='"> ';
if(t.roleFlag!=null && t.roleFlag.flag5!=null ){
$out+=' <span class="show-span taskName pt-relative" data-string="';
$out+=$escape(t.taskName);
$out+='" > ';
$out+=$escape(t.taskName);
$out+=' </span> <span class="edit-span-box"> <a class="tree-td-a" href="javascript:void(0);" data-action="taskNameEdit" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> </span> ';
}else{
$out+=' <span class="taskName pt-relative" data-string="';
$out+=$escape(t.taskName);
$out+='"> ';
$out+=$escape(t.taskName);
$out+=' </span> ';
}
$out+=' </td> <td class="b-r-none v-middle"> ';
if(t.taskRemark==null || t.taskRemark==''){
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag5!=null ){
$out+=' <span class="fc-ccc show-span">未设置</span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' ';
}else{
$out+=' <span class="edit-span-box wh-16" > <a data-action="viewTaskRemarkEdit" id="viewTaskRemarkEdit';
$out+=$escape(ti);
$out+='"> <i class="ic-describe"></i></a> </span> ';
}
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag5!=null ){
$out+=' <span class="edit-span-box wh-16"> <a href="javascript:void(0);" data-action="taskRemarkEdit" id="taskRemarkEdit';
$out+=$escape(ti);
$out+='" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> </span> ';
}
$out+=' </td> <td class="no-pd v-middle b-r-none"> <p class="m-b-none"> ';
if(t.roleFlag!=null && t.roleFlag.flag2!=null){
$out+=' <span class="show-span person-in-charge ';
$out+=$escape(t.personInChargeId == currentCompanyUserId?'text-navy':'');
$out+='" data-id="';
$out+=$escape(t.personInChargeId);
$out+='" data-task-id="';
$out+=$escape(t.id);
$out+='" data-user-name="';
$out+=$escape(t.personInCharge);
$out+='" data-toggle="tooltip" data-original-title="';
$out+=$escape(t.designOrg);
$out+=$escape(t.departName==null?'':'('+t.departName+')');
$out+='"> ';
$out+=$escape(t.personInCharge==null?'未设置':t.personInCharge);
$out+=' </span> <span class="edit-span-box wh-16"> <a href="javascript:void(0);" data-action="setPersonInCharge" data-deal-type="edit" data-id="';
$out+=$escape(t.personInChargeId);
$out+='" data-task-id="';
$out+=$escape(t.id);
$out+='" data-user-name="';
$out+=$escape(t.personInCharge);
$out+='" style="display: none;"> <i class="ic-edit"></i> </a> </span> ';
}else{
$out+=' <span class="person-in-charge" data-toggle="tooltip" data-original-title="';
$out+=$escape(t.designOrg);
$out+=$escape(t.departName==null?'':'('+t.departName+')');
$out+='"> <!--';
if(t.completeDate!=null){
$out+=' <i class="fa fa-check fc-v1-green"></i> ';
}
$out+='--> <span class="';
$out+=$escape(t.personInChargeId == currentCompanyUserId?'text-navy':'');
$out+='">';
$out+=$escape(t.personInCharge);
$out+='</span> </span> ';
}
$out+=' </p> </td> <td class="b-r-none v-middle"> <span class="show-span"> ';
if(t.designUser && t.designUser.userList!=null && t.designUser.userList.length>0 ){
$out+=' <span class="';
$out+=$escape(t.projectDesignUser.companyUserId == currentCompanyUserId?'text-navy':'');
$out+='">';
$out+=$escape(t.projectDesignUser.userName);
$out+='</span> ';
$each(t.designUser.userList,function(u,$index){
$out+=' ';
if(u.id!=t.projectDesignUser.id){
$out+=' , <!--';
if(u.completeTime!=null){
$out+=' <span><i class="fa fa-check fc-v1-green"></i></span> ';
}
$out+='--> <span>';
$out+=$escape(u.userName);
$out+='</span> ';
}
$out+=' ';
});
$out+=' ';
}else{
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1 ){
$out+=' <span class="fc-ccc">未设置</span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' ';
}
$out+=' </span> <span class="edit-span-box wh-16"> ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1 ){
$out+=' <a href="javascript:void(0);" data-action="setTaskDesigner" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </span> </td> <td class="b-r-none v-middle"> <span class="show-span"> ';
if(t.checkUser && t.checkUser.userList!=null && t.checkUser.userList.length>0){
$out+=' <span class="';
$out+=$escape(t.projectCheckUser.companyUserId == currentCompanyUserId?'text-navy':'');
$out+='">';
$out+=$escape(t.projectCheckUser.userName);
$out+='</span> ';
$each(t.checkUser.userList,function(u,$index){
$out+=' ';
if(u.id!=t.projectCheckUser.id){
$out+=' , <!--';
if(u.completeTime!=null){
$out+=' <span><i class="fa fa-check fc-v1-green"></i></span> ';
}
$out+='--> <span>';
$out+=$escape(u.userName);
$out+='</span> ';
}
$out+=' ';
});
$out+=' ';
}else{
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1 ){
$out+=' <span class="fc-ccc">未设置</span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' ';
}
$out+=' </span> <span class="edit-span-box wh-16"> ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1){
$out+=' <a href="javascript:void(0);" data-action="setTaskCheckUser" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </span> </td> <td class="b-r-none v-middle"> <span class="show-span"> ';
if(t.examineUser && t.examineUser.userList!=null && t.examineUser.userList.length>0){
$out+=' <span class="';
$out+=$escape(t.projectExamineUser.companyUserId == currentCompanyUserId?'text-navy':'');
$out+='">';
$out+=$escape(t.projectExamineUser.userName);
$out+='</span> ';
$each(t.examineUser.userList,function(u,$index){
$out+=' ';
if(u.id!=t.projectExamineUser.id){
$out+=' , <!--';
if(u.completeTime!=null){
$out+=' <span><i class="fa fa-check fc-v1-green"></i></span> ';
}
$out+='--> <span>';
$out+=$escape(u.userName);
$out+='</span> ';
}
$out+=' ';
});
$out+=' ';
}else{
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1 ){
$out+=' <span class="fc-ccc">未设置</span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' ';
}
$out+=' </span> <span class="edit-span-box wh-16"> ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1){
$out+=' <a href="javascript:void(0);" data-action="setTaskExamineUser" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </span> </td> <td class="b-r-none v-middle"> <span class="show-span" data-type="planTime" data-start-time="';
$out+=$escape(t.planStartTime);
$out+='" data-end-time="';
$out+=$escape(t.planEndTime);
$out+='"> ';
if((t.planStartTime==null || t.planStartTime=='') && (t.planEndTime==null || t.planEndTime=='')  && (t.roleFlag!=null && t.roleFlag.flag5!=null)){
$out+=' <span class="fc-ccc">未设置</span> ';
}else{
$out+=' ';
if(_isNullOrBlank(t.planStartTime) && _isNullOrBlank(t.planEndTime)){
$out+=' -- ';
}else if(!_isNullOrBlank(t.planStartTime) && !_isNullOrBlank(t.planEndTime)){
$out+=' ';
$out+=$escape(_momentFormat(t.planStartTime,'YYYY/MM/DD'));
$out+=' - ';
$out+=$escape(_momentFormat(t.planEndTime,'YYYY/MM/DD'));
$out+=' | 共 ';
$out+=$escape(_timeDifference(t.planStartTime,t.planEndTime));
$out+=' 天 ';
}else{
$out+=' ';
$out+=$escape(_momentFormat(t.planStartTime,'YYYY/MM/DD'));
$out+=' - ';
$out+=$escape(_momentFormat(t.planEndTime,'YYYY/MM/DD'));
$out+=' ';
}
$out+=' ';
if((t.changeTime==true)){
$out+=' <a href="javascript:void(0);" data-action="viewProgressChange" id="viewProgressChange1';
$out+=$escape(t.id);
$out+='"><i class="fa fa-info-circle"></i></a> ';
}
$out+=' ';
}
$out+=' </span> <span class="edit-span-box wh-16"> ';
if(t.roleFlag!=null && t.roleFlag.flag4!=null){
$out+=' <a href="javascript:void(0);" data-action="planningSchedule" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </span> </td> <td class="b-r-none v-middle"> ';
if(t.taskState==2 || t.taskState==4){
$out+=' <span class="text-danger">';
$out+=$escape(t.statusText);
$out+='</span> ';
}else{
$out+=' <span class="text-warning">';
$out+=$escape(t.statusText);
$out+='</span> ';
}
$out+=' </td> <td class="b-r-none v-middle"> <span> ';
if(t.completeDate!=null &&  t.completeDate!=''){
$out+=' ';
$out+=$escape(_momentFormat(t.completeDate,'YYYY/MM/DD'));
$out+=' ';
}else{
$out+=' -- ';
}
$out+=' </span> </td> <td class="b-r-none v-middle"> <span> ';
if(t.completeDate!=null &&  t.completeDate!='' && t.completion!=null && t.completion!=''){
$out+=' <span class="edit-span-box wh-16" > <a data-action="viewTaskCompletion" id="viewTaskCompletion';
$out+=$escape(ti);
$out+='"> <i class="ic-describe"></i></a> </span> ';
}else{
$out+=' -- ';
}
$out+=' </span> </td> <td class="b-r-none v-middle "> <span> ';
if(t.stateHtml!=null && t.stateHtml!=''){
$out+=' ';
$out+=$escape(t.stateHtml);
$out+=' ';
}
$out+=' </span> </td> <td class="v-middle "> ';
if(t.roleFlag!=null && t.roleFlag.flag5!=null ){
$out+=' <span class="show-span pt-relative"> ';
if(t.priority==5){
$out+=' 紧急 ';
}else if(t.priority==4){
$out+=' 高 ';
}else if(t.priority==3){
$out+=' 中 ';
}else if(t.priority==2){
$out+=' 低 ';
}else if(t.priority==1){
$out+=' 无关紧要 ';
}else{
$out+=' ';
}
$out+=' </span> <span class="edit-span-box"> <a class="tree-td-a" href="javascript:void(0);" data-action="priorityEdit" data-deal-type="edit" data-priority="';
$out+=$escape(t.priority);
$out+='" style="display: none;"> <i class="ic-edit"></i> </a> </span> ';
}else{
$out+=' <span class="pt-relative"> ';
if(t.priority==5){
$out+=' 紧急 ';
}else if(t.priority==4){
$out+=' 高 ';
}else if(t.priority==3){
$out+=' 中 ';
}else if(t.priority==2){
$out+=' 低 ';
}else if(t.priority==1){
$out+=' 无关紧要 ';
}else{
$out+=' ';
}
$out+=' </span> ';
}
$out+=' </td> </tr> ';
});
$out+=' ';
if(!(productionList && productionList.length>0)){
$out+=' <tr class="no-data"> <td colspan="13" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">您还没有相关生产安排</span> </div> </td> </tr> ';
}
$out+=' </table> </form>';
return new String($out);
});/*v:1*/
template('m_production/m_production_list_add',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,taskType=$data.taskType,$escape=$utils.$escape,personInCharge=$data.personInCharge,personInChargeId=$data.personInChargeId,$out='';$out+='<tr class="row-edit edit-box"> <td class="b-r-none b-m-none"></td> <td class="td-class1" colspan="2"> <input placeholder="设计任务" class="form-control" type="text" name="taskName" style="width: 80%;display: inline-block;"> <span><span class="wordCount">0</span>/<span>50</span></span> </td> <!--<td class="b-r-none b-l-none v-middle"> ';
if(taskType==null || taskType!='5'){
$out+=' <input placeholder="任务描述" class="form-control" type="text" name="taskRemark" > ';
}
$out+=' </td>--> <td class="td-class1"> <span class="show-span"> <span class="fc-ccc">';
$out+=$escape(personInCharge);
$out+='</span> </span> ';
if(taskType==null || taskType!='5'){
$out+=' <a href="javascript:void(0);" data-action="setTaskLeader" data-user-name="';
$out+=$escape(personInCharge);
$out+='" data-id="';
$out+=$escape(personInChargeId);
$out+='"> <i class="fa fa-pencil-square-o"></i> </a> ';
}
$out+=' </td> <td class="td-class1"> <span class="show-span" data-toggle="tooltip" data-original-title=""> <span class="fc-ccc">--</span> </span>    </td> <td class="td-class1"> <span class="show-span" data-toggle="tooltip" data-original-title=""> <span class="fc-ccc">--</span> </span>    </td> <td class="td-class1"> <span class="show-span" data-toggle="tooltip" data-original-title=""> <span class="fc-ccc">--</span> </span>    </td> <td class="td-class1" colspan="5"> <div class="input-group dp-inline-block"> <input type="text" class="form-control timeInput input-sm " id="ipt_startTime" name="startTime" data-appointmentStartTime="" placeholder="开始日期" readonly onFocus="startTimeFun(this,m_inputProcessTime_onpicked)" value="" style="width: 110px;"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar"></i> </span> </div> <div class="input-group dp-inline-block"> <input type="text" class="form-control timeInput input-sm" id="ipt_endTime" name="endTime" data-appointmentEndTime="" placeholder="结束日期" readonly onFocus="endTimeFun(this,m_inputProcessTime_onpicked)" value="" style="width: 110px;"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" ></i> </span> </div> </td> <td class="b-l-none b-m-none"> <button class="btn btn-primary btn-sm" data-action="submit">创建</button> <button class="btn btn-default btn-sm" data-action="cancel">取消</button> </td> </tr> ';
if(taskType==null || taskType!='5'){
$out+=' <tr class="row-edit edit-box"> <td class="b-r-none b-t-none"></td> <td class="b-r-none b-l-none b-t-none v-middle" colspan="11"> <input placeholder="任务描述" class="form-control" type="text" name="taskRemark" > </td> <td class="b-l-none b-t-none"></td> </tr> ';
}
return new String($out);
});/*v:1*/
template('m_production/m_production_list_old',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,productionList=$data.productionList,t=$data.t,ti=$data.ti,$escape=$utils.$escape,currentCompanyUserId=$data.currentCompanyUserId,u=$data.u,$index=$data.$index,_isNullOrBlank=$helpers._isNullOrBlank,_momentFormat=$helpers._momentFormat,_timeDifference=$helpers._timeDifference,_url=$helpers._url,$out='';$out+='<form class="m_production_list task-list-box" style="overflow-x: auto;overflow-y:hidden; min-height: 280px;"> <table class="tree tree-box table table-bordered table-striped" style="min-width: 1500px;margin-bottom: 140px;"> <thead> <tr> <th width="4%" class="b-r-none"></th> <th width="18%" class="b-r-none b-l-none" style="padding-left: 38px;min-width: 400px;">设计任务</th> <th width="5%" class="b-r-none">任务描述</th> <th width="6%" class="b-r-none">任务负责人</th> <th width="9%" class="b-r-none">设计人员</th> <th width="8%" class="b-r-none">校对人员</th> <th width="8%" class="b-r-none">审核人员</th> <th width="14%" class="b-r-none" >计划进度</th> <th width="6%" class="b-r-none" >进度提示</th> <th width="7%" class="b-r-none" >完成时间</th> <th width="5%" class="b-r-none" >完成情况</th> <th width="5%" class="b-r-none">任务状态</th> <th width="5%" class="">优先级</th> </tr> </thead> ';
$each(productionList,function(t,ti){
$out+=' <tr class="tree-box-tr treegrid-';
$out+=$escape(t.id);
$out+=' ';
if(t.taskPid!=null && t.taskPid!=''){
$out+=' treegrid-parent-';
$out+=$escape(t.taskPid);
$out+=' ';
}
$out+=' ';
$out+=$escape(t.completeDate==null?'':'completeDate-tr');
$out+=' " data-i="';
$out+=$escape(ti);
$out+='" data-id="';
$out+=$escape(t.id);
$out+='" data-pid="';
$out+=$escape(t.taskPid);
$out+='" data-company-id="';
$out+=$escape(t.companyId);
$out+='" data-publish-id="';
$out+=$escape(t.publishId);
$out+='"> <td class="b-r-none v-middle"> <div class="list-action-box"> <div class="btn-group singleOperation" style="display: none;"> <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true"> <span class="ic-operation"></span> </button> <ul class="dropdown-menu"> ';
if(t.roleFlag!=null && t.roleFlag.flag1!=null && !(t.taskState=='3'||t.taskState=='4') && t.gradeStatus !=1  && t.isOperaterTask==0){
$out+=' <li><a href="javascript:void(0);" data-action="addSubTask">添加子任务</a></li> ';
}
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag7!=null){
$out+=' <li><a href="javascript:void(0);" data-action="addSubTaskByDesigner">分解设计任务</a></li> ';
}
$out+='  ';
if(t.isOperaterTask==0 && t.completeDate ==null && t.personInChargeId == currentCompanyUserId){
$out+=' <li><a href="javascript:void(0);" data-action="completeTask" data-status="1" data-original-title="请点击确定完成任务">任务确认完成</a></li> ';
}
$out+='  ';
if(t.isOperaterTask==0 && t.completeDate !=null && t.personInChargeId == currentCompanyUserId){
$out+=' <li><a href="javascript:void(0);" data-action="completeTask" data-status="0" data-original-title="请点击确定激活任务">激活任务</a></li> ';
}
$out+='  ';
if(!t.first){
$out+=' <li><a href="javascript:void(0);" data-action="moveUp">向上移动</a></li> ';
}
$out+='  ';
if(!t.last){
$out+=' <li><a href="javascript:void(0);" data-action="moveDown">向下移动</a></li> ';
}
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag6!=null){
$out+=' <li><a href="javascript:void(0);" data-action="delTask" data-canbedelete="">删除</a></li> ';
}
$out+=' </ul> </div> </div> </td> <td class="treeTd pt-relative popover-box no-pd-right b-r-none b-l-none v-middle" height="40" data-task-state="';
$out+=$escape(t.taskState=='3'||t.taskState=='4'?'1':'0');
$out+='"> ';
if(t.roleFlag!=null && t.roleFlag.flag5!=null ){
$out+=' <span class="show-span taskName pt-relative" data-string="';
$out+=$escape(t.taskName);
$out+='" > ';
$out+=$escape(t.taskName);
$out+=' </span> <span class="edit-span-box"> <a class="tree-td-a" href="javascript:void(0);" data-action="taskNameEdit" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> </span> ';
}else{
$out+=' <span class="taskName pt-relative" data-string="';
$out+=$escape(t.taskName);
$out+='"> ';
$out+=$escape(t.taskName);
$out+=' </span> ';
}
$out+=' </td> <td class="b-r-none v-middle"> ';
if(t.taskRemark==null || t.taskRemark==''){
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag5!=null ){
$out+=' <span class="fc-ccc show-span">未设置</span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' ';
}else{
$out+=' <span class="edit-span-box wh-16" > <a data-action="viewTaskRemarkEdit" id="viewTaskRemarkEdit';
$out+=$escape(ti);
$out+='"> <i class="ic-describe"></i></a> </span> ';
}
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag5!=null ){
$out+=' <span class="edit-span-box wh-16"> <a href="javascript:void(0);" data-action="taskRemarkEdit" id="taskRemarkEdit';
$out+=$escape(ti);
$out+='" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> </span> ';
}
$out+=' </td> <td class="no-pd v-middle b-r-none"> <p class="m-b-none"> ';
if(t.roleFlag!=null && t.roleFlag.flag2!=null){
$out+=' <span class="show-span person-in-charge ';
$out+=$escape(t.personInChargeId == currentCompanyUserId?'text-navy':'');
$out+='" data-id="';
$out+=$escape(t.personInChargeId);
$out+='" data-task-id="';
$out+=$escape(t.id);
$out+='" data-user-name="';
$out+=$escape(t.personInCharge);
$out+='" data-toggle="tooltip" data-original-title="';
$out+=$escape(t.designOrg);
$out+=$escape(t.departName==null?'':'('+t.departName+')');
$out+='"> ';
$out+=$escape(t.personInCharge==null?'未设置':t.personInCharge);
$out+=' </span> <span class="edit-span-box wh-16"> <a href="javascript:void(0);" data-action="setPersonInCharge" data-deal-type="edit" data-id="';
$out+=$escape(t.personInChargeId);
$out+='" data-task-id="';
$out+=$escape(t.id);
$out+='" data-user-name="';
$out+=$escape(t.personInCharge);
$out+='" style="display: none;"> <i class="ic-edit"></i> </a> </span> ';
}else{
$out+=' <span class="person-in-charge" data-toggle="tooltip" data-original-title="';
$out+=$escape(t.designOrg);
$out+=$escape(t.departName==null?'':'('+t.departName+')');
$out+='"> <!--';
if(t.completeDate!=null){
$out+=' <i class="fa fa-check fc-v1-green"></i> ';
}
$out+='--> <span class="';
$out+=$escape(t.personInChargeId == currentCompanyUserId?'text-navy':'');
$out+='">';
$out+=$escape(t.personInCharge);
$out+='</span> </span> ';
}
$out+=' </p> </td> <td class="b-r-none v-middle"> <span class="show-span"> ';
if(t.designUser && t.designUser.userList!=null && t.designUser.userList.length>0 ){
$out+=' <span class="';
$out+=$escape(t.projectDesignUser.companyUserId == currentCompanyUserId?'text-navy':'');
$out+='">';
$out+=$escape(t.projectDesignUser.userName);
$out+='</span> ';
$each(t.designUser.userList,function(u,$index){
$out+=' ';
if(u.id!=t.projectDesignUser.id){
$out+=' , <!--';
if(u.completeTime!=null){
$out+=' <span><i class="fa fa-check fc-v1-green"></i></span> ';
}
$out+='--> <span>';
$out+=$escape(u.userName);
$out+='</span> ';
}
$out+=' ';
});
$out+=' ';
}else{
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1 ){
$out+=' <span class="fc-ccc">未设置</span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' ';
}
$out+=' </span> <span class="edit-span-box wh-16"> ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1 ){
$out+=' <a href="javascript:void(0);" data-action="setTaskDesigner" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </span> </td> <td class="b-r-none v-middle"> <span class="show-span"> ';
if(t.checkUser && t.checkUser.userList!=null && t.checkUser.userList.length>0){
$out+=' <span class="';
$out+=$escape(t.projectCheckUser.companyUserId == currentCompanyUserId?'text-navy':'');
$out+='">';
$out+=$escape(t.projectCheckUser.userName);
$out+='</span> ';
$each(t.checkUser.userList,function(u,$index){
$out+=' ';
if(u.id!=t.projectCheckUser.id){
$out+=' , <!--';
if(u.completeTime!=null){
$out+=' <span><i class="fa fa-check fc-v1-green"></i></span> ';
}
$out+='--> <span>';
$out+=$escape(u.userName);
$out+='</span> ';
}
$out+=' ';
});
$out+=' ';
}else{
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1 ){
$out+=' <span class="fc-ccc">未设置</span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' ';
}
$out+=' </span> <span class="edit-span-box wh-16"> ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1){
$out+=' <a href="javascript:void(0);" data-action="setTaskCheckUser" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </span> </td> <td class="b-r-none v-middle"> <span class="show-span"> ';
if(t.examineUser && t.examineUser.userList!=null && t.examineUser.userList.length>0){
$out+=' <span class="';
$out+=$escape(t.projectExamineUser.companyUserId == currentCompanyUserId?'text-navy':'');
$out+='">';
$out+=$escape(t.projectExamineUser.userName);
$out+='</span> ';
$each(t.examineUser.userList,function(u,$index){
$out+=' ';
if(u.id!=t.projectExamineUser.id){
$out+=' , <!--';
if(u.completeTime!=null){
$out+=' <span><i class="fa fa-check fc-v1-green"></i></span> ';
}
$out+='--> <span>';
$out+=$escape(u.userName);
$out+='</span> ';
}
$out+=' ';
});
$out+=' ';
}else{
$out+=' ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1 ){
$out+=' <span class="fc-ccc">未设置</span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' ';
}
$out+=' </span> <span class="edit-span-box wh-16"> ';
if(t.roleFlag!=null && t.roleFlag.flag3!=null && !(t.taskState=='3'||t.taskState=='4') && t.saveExtent!=1){
$out+=' <a href="javascript:void(0);" data-action="setTaskExamineUser" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </span> </td> <td class="b-r-none v-middle"> <span class="show-span" data-type="planTime" data-start-time="';
$out+=$escape(t.planStartTime);
$out+='" data-end-time="';
$out+=$escape(t.planEndTime);
$out+='"> ';
if((t.planStartTime==null || t.planStartTime=='') && (t.planEndTime==null || t.planEndTime=='')  && (t.roleFlag!=null && t.roleFlag.flag5!=null)){
$out+=' <span class="fc-ccc">未设置</span> ';
}else{
$out+=' ';
if(_isNullOrBlank(t.planStartTime) && _isNullOrBlank(t.planEndTime)){
$out+=' -- ';
}else if(!_isNullOrBlank(t.planStartTime) && !_isNullOrBlank(t.planEndTime)){
$out+=' ';
$out+=$escape(_momentFormat(t.planStartTime,'YYYY/MM/DD'));
$out+=' - ';
$out+=$escape(_momentFormat(t.planEndTime,'YYYY/MM/DD'));
$out+=' | 共 ';
$out+=$escape(_timeDifference(t.planStartTime,t.planEndTime));
$out+=' 天 ';
}else{
$out+=' ';
$out+=$escape(_momentFormat(t.planStartTime,'YYYY/MM/DD'));
$out+=' - ';
$out+=$escape(_momentFormat(t.planEndTime,'YYYY/MM/DD'));
$out+=' ';
}
$out+=' ';
if((t.changeTime==true)){
$out+=' <a href="javascript:void(0);" data-action="viewProgressChange" id="viewProgressChange1';
$out+=$escape(t.id);
$out+='"><i class="fa fa-info-circle"></i></a> ';
}
$out+=' ';
}
$out+=' </span> <span class="edit-span-box wh-16"> ';
if(t.roleFlag!=null && t.roleFlag.flag4!=null){
$out+=' <a href="javascript:void(0);" data-action="planningSchedule" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </span> </td> <td class="b-r-none v-middle"> ';
if(t.taskState==2 || t.taskState==4){
$out+=' <span class="text-danger">';
$out+=$escape(t.statusText);
$out+='</span> ';
}else{
$out+=' <span class="text-warning">';
$out+=$escape(t.statusText);
$out+='</span> ';
}
$out+=' </td> <td class="b-r-none v-middle"> <span> ';
if(t.completeDate!=null &&  t.completeDate!=''){
$out+=' ';
$out+=$escape(_momentFormat(t.completeDate,'YYYY/MM/DD'));
$out+=' ';
}else{
$out+=' -- ';
}
$out+=' </span> </td> <td class="b-r-none v-middle"> <span> ';
if(t.completeDate!=null &&  t.completeDate!='' && t.completion!=null && t.completion!=''){
$out+=' <span class="edit-span-box wh-16" > <a data-action="viewTaskCompletion" id="viewTaskCompletion';
$out+=$escape(ti);
$out+='"> <i class="ic-describe"></i></a> </span> ';
}else{
$out+=' -- ';
}
$out+=' </span> </td> <td class="b-r-none v-middle "> <span> ';
if(t.stateHtml!=null && t.stateHtml!=''){
$out+=' ';
$out+=$escape(t.stateHtml);
$out+=' ';
}
$out+=' </span> </td> <td class="v-middle "> ';
if(t.roleFlag!=null && t.roleFlag.flag5!=null ){
$out+=' <span class="show-span pt-relative"> ';
if(t.priority==5){
$out+=' 紧急 ';
}else if(t.priority==4){
$out+=' 高 ';
}else if(t.priority==3){
$out+=' 中 ';
}else if(t.priority==2){
$out+=' 低 ';
}else if(t.priority==1){
$out+=' 无关紧要 ';
}else{
$out+=' ';
}
$out+=' </span> <span class="edit-span-box"> <a class="tree-td-a" href="javascript:void(0);" data-action="priorityEdit" data-deal-type="edit" data-priority="';
$out+=$escape(t.priority);
$out+='" style="display: none;"> <i class="ic-edit"></i> </a> </span> ';
}else{
$out+=' <span class="pt-relative"> ';
if(t.priority==5){
$out+=' 紧急 ';
}else if(t.priority==4){
$out+=' 高 ';
}else if(t.priority==3){
$out+=' 中 ';
}else if(t.priority==2){
$out+=' 低 ';
}else if(t.priority==1){
$out+=' 无关紧要 ';
}else{
$out+=' ';
}
$out+=' </span> ';
}
$out+=' </td> </tr> ';
});
$out+=' ';
if(!(productionList && productionList.length>0)){
$out+=' <tr class="no-data"> <td colspan="13" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">您还没有相关生产安排</span> </div> </td> </tr> ';
}
$out+=' </table> </form>';
return new String($out);
});/*v:1*/
template('m_production/m_production_list_usertip',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,unSubmittedUserList=$data.unSubmittedUserList,u=$data.u,i=$data.i,$escape=$utils.$escape,submittedUserList=$data.submittedUserList,$out='';$out+='<div class="tip-box"> <div class="text-left"> 未提交： <span class="un-submitted"> ';
$each(unSubmittedUserList,function(u,i){
$out+=' <span class="span-name">';
$out+=$escape(u.userName);
$out+='</span> ';
if(i < unSubmittedUserList.length-1){
$out+=' <span class="span-comma">,</span> ';
}
$out+=' ';
});
$out+=' </span> </div> <div class="text-left"> 已提交： <span class="submitted"> ';
$each(submittedUserList,function(u,i){
$out+=' <span class="span-name">';
$out+=$escape(u.userName);
$out+='</span> ';
if(i < submittedUserList.length-1){
$out+=' <span class="span-comma">,</span> ';
}
$out+=' ';
});
$out+=' </span> </div> </div>';
return new String($out);
});/*v:1*/
template('m_production/m_production_org',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,isShowTips=$data.isShowTips,departBCompany=$data.departBCompany,isRoleCompany=$data.isRoleCompany,$escape=$utils.$escape,currentCompanyUserId=$data.currentCompanyUserId,productionOrgList=$data.productionOrgList,$each=$utils.$each,t=$data.t,$index=$data.$index,$out='';$out+='<style> table.partB{border-right:0px;margin-bottom: 0px;} table.partB,table.partB tbody,table.partB tbody tr,table.partB tbody tr td{border-bottom: 0px} </style> ';
if(isShowTips==1){
$out+=' <div class="alert alert-warning alert-dismissable"> <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button> 如若要进行生产安排，请先指定设计负责人！ </div> ';
}
$out+=' ';
if(departBCompany !=null && isRoleCompany){
$out+=' <table class="table table-bordered tree-box partB" style="width: 12%;float: left;border-right:0px;"> <tr class="" > <td align="center" height="40" class="gray-bg font-bold" style="border-right:0px; ">乙方</td> </tr> </table> <table class="table table-bordered tree-box partB" style="width: 88%;float: left;"> <tbody> <tr class="" > <td width="32%" height="40" class="treeTd">&nbsp;';
$out+=$escape(departBCompany.companyName);
$out+='</td> <td width="14%" align="center" class="gray-bg font-bold">经营负责人</td> <td width="20%"> <div class="full-width l-h-20"> ';
if(departBCompany.isUpdateOperator==1){
$out+=' <a href="javascript:void(0)" data-action="changeOperatorPerson" data-i="0" data-id="';
$out+=$escape(departBCompany.operatorPersonId);
$out+='" data-user-name="';
$out+=$escape(departBCompany.operatorPersonName);
$out+='" data-company-id="';
$out+=$escape(departBCompany.id);
$out+='" class="pull-left">';
$out+=$escape(departBCompany.operatorPersonName);
$out+='</a> <a href="javascript:void(0)" data-action="changeOperatorPerson" data-i="0" data-id="';
$out+=$escape(departBCompany.operatorPersonId);
$out+='" data-user-name="';
$out+=$escape(departBCompany.operatorPersonName);
$out+='" data-company-id="';
$out+=$escape(departBCompany.id);
$out+='" class="pull-right showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="';
$out+=$escape(departBCompany.operatorPersonId==currentCompanyUserId?'更换':'更换');
$out+='经营负责人"> <i class="fa fa-retweet"></i> </a> ';
}else{
$out+=' ';
$out+=$escape(departBCompany.operatorPersonName);
$out+=' ';
}
$out+=' </div> </td> <td width="14%" align="center" class="gray-bg font-bold">设计负责人</td> <td width="20%"> <div class="full-width l-h-20"> ';
if(departBCompany.isUpdateDesign==1){
$out+=' <a href="javascript:void(0)" data-action="changeManagerPerson" data-i="0" data-id="';
$out+=$escape(departBCompany.designPersonId);
$out+='" data-user-name="';
$out+=$escape(departBCompany.designPersonName);
$out+='" data-company-id="';
$out+=$escape(departBCompany.id);
$out+='" class="pull-left"> ';
$out+=$escape(departBCompany.designPersonName==null?'未设置':departBCompany.designPersonName);
$out+=' </a> ';
if(departBCompany.designPersonName!=null){
$out+=' <a href="javascript:void(0)" data-action="changeManagerPerson" data-i="0" data-id="';
$out+=$escape(departBCompany.designPersonId);
$out+='" data-user-name="';
$out+=$escape(departBCompany.designPersonName);
$out+='" data-company-id="';
$out+=$escape(departBCompany.id);
$out+='" class="pull-right showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="更换设计负责人"> <i class="fa fa-retweet"></i> </a> ';
}
$out+=' ';
}else{
$out+=' ';
$out+=$escape(departBCompany.designPersonName);
$out+=' ';
}
$out+=' </div> </td> </tr> </tbody> </table> ';
}
$out+=' <table class="table table-bordered tree-box" style="width: 12%;float: left;border-right:0px;"> <tbody> <tr class="" > <td align="center" height="40" class="gray-bg font-bold" style="border-right:0px; ">立项组织</td> </tr> ';
if(productionOrgList.length>1){
$out+=' <tr class="" > <td align="center" height="';
$out+=$escape((productionOrgList.length-1)*40);
$out+='" class="gray-bg font-bold" style="vertical-align: middle;border-right:0px; ">设计组织</td> </tr> ';
}
$out+=' </tbody> </table> <table class="tree table no-treegrid-expander table-bordered tree-box" style="width: 88%;float: left;"> <tbody> ';
$each(productionOrgList,function(t,$index){
$out+=' ';
if(t!=null){
$out+=' <tr class="tree-box-tr treegrid-';
$out+=$escape(t.treeId);
$out+=' ';
if(t.pid!=null && t.pid!=''){
$out+='treegrid-parent-';
$out+=$escape(t.pid);
}
$out+='" data-i="';
$out+=$escape($index);
$out+='"> <td width="32%" height="40" class="treeTd"><span class="companyName" data-string="';
$out+=$escape(t.companyName);
$out+='">&nbsp;';
$out+=$escape(t.companyName);
$out+='</span></td> <td width="14%" align="center" class="gray-bg font-bold">经营负责人</td> <td width="20%"> <div class="full-width l-h-20"> ';
if(t.isUpdateOperator==1){
$out+=' <a href="javascript:void(0)" data-action="changeOperatorPerson" data-i="0" data-id="';
$out+=$escape(t.operatorPersonId);
$out+='" data-user-name="';
$out+=$escape(t.operatorPersonName);
$out+='" data-company-id="';
$out+=$escape(t.id);
$out+='" class="pull-left">';
$out+=$escape(t.operatorPersonName);
$out+='</a> <a href="javascript:void(0)" data-action="changeOperatorPerson" data-i="0" data-id="';
$out+=$escape(t.operatorPersonId);
$out+='" data-user-name="';
$out+=$escape(t.operatorPersonName);
$out+='" data-company-id="';
$out+=$escape(t.id);
$out+='" class="pull-right showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="';
$out+=$escape(t.operatorPersonId==currentCompanyUserId?'更换':'更换');
$out+='经营负责人"> <i class="fa fa-retweet"></i> </a> ';
}else{
$out+=' ';
$out+=$escape(t.operatorPersonName);
$out+=' ';
}
$out+=' </div> </td> <td width="14%" align="center" class="gray-bg font-bold">设计负责人</td> <td width="20%"> <div class="full-width l-h-20"> ';
if(t.isUpdateDesign==1){
$out+=' <a href="javascript:void(0)" data-action="changeManagerPerson" data-i="0" data-id="';
$out+=$escape(t.designPersonId);
$out+='" data-user-name="';
$out+=$escape(t.designPersonName);
$out+='" data-company-id="';
$out+=$escape(t.id);
$out+='" class="pull-left">';
$out+=$escape(t.designPersonName==null?'未设置':t.designPersonName);
$out+='</a> ';
if(t.designPersonName!=null){
$out+=' <a href="javascript:void(0)" data-action="changeManagerPerson" data-i="0" data-id="';
$out+=$escape(t.designPersonId);
$out+='" data-user-name="';
$out+=$escape(t.designPersonName);
$out+='" data-company-id="';
$out+=$escape(t.id);
$out+='" class="pull-right showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="更换设计负责人"> <i class="fa fa-retweet"></i> </a> ';
}
$out+=' ';
}else{
$out+=' ';
$out+=$escape(t.designPersonName);
$out+=' ';
}
$out+=' </div> </td> </tr> ';
}
$out+=' ';
});
$out+=' </tbody> </table> <div class="clearfix"></div> <hr/>';
return new String($out);
});/*v:1*/
template('m_production/m_production_overview','  <div id="productionList" class="list-box"></div>  ');/*v:1*/
template('m_production/m_production_overview_header',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,projectCreateByName=$data.projectCreateByName,operatorPersonName=$data.operatorPersonName,designPersonName=$data.designPersonName,$out='';$out+='<div class="col-md-6 "> <h4>项目：';
$out+=$escape(projectName);
$out+='</h4> </div> <div class="col-md-6 text-right"> <h4> 立项人：<span class="text-info">';
$out+=$escape(projectCreateByName);
$out+='</span>&nbsp;&nbsp; 经营负责人：<span class="text-info">';
$out+=$escape(operatorPersonName==null?'未设置':operatorPersonName);
$out+='</span>&nbsp;&nbsp; 设计负责人：<span class="text-info">';
$out+=$escape(designPersonName==null?'未设置':designPersonName);
$out+='</span> </h4> </div>';
return new String($out);
});/*v:1*/
template('m_production/m_production_overview_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,productionList=$data.productionList,t=$data.t,ti=$data.ti,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<form class="m_production_list"> <table class="tree table table-bordered table-striped table-condensed"> <thead> <tr> <th width="23%" class="b-r-none " style="padding-left: 45px;">设计任务</th> <th width="12%" class="b-r-none b-l-none">任务描述</th> <th width="9%" class="b-r-none b-l-none">任务负责人</th> <th width="9%" class="b-r-none b-l-none">设计</th> <th width="9%" class="b-r-none b-l-none">校对</th> <th width="9%" class="b-r-none b-l-none">审核</th> <th width="9%" class="b-r-none b-l-none">开始时间</th> <th width="9%" class="b-r-none b-l-none">结束时间</th> <th width="8%" class="b-l-none">任务状态</th> </tr> </thead> ';
$each(productionList,function(t,ti){
$out+=' <tr class="tree-box-tr treegrid-';
$out+=$escape(t.id);
$out+=' ';
if(t.taskPid!=null && t.taskPid!=''){
$out+=' treegrid-parent-';
$out+=$escape(t.taskPid);
$out+=' ';
}
$out+='" data-i="';
$out+=$escape(ti);
$out+='" data-id="';
$out+=$escape(t.id);
$out+='" data-pid="';
$out+=$escape(t.taskPid);
$out+='" data-company-id="';
$out+=$escape(t.companyId);
$out+='" data-publish-id="';
$out+=$escape(t.publishId);
$out+='"> <td class="treeTd pt-relative popover-box no-pd-right b-r-none b-l-none" height="40" data-task-state="';
$out+=$escape(t.taskState=='3'||t.taskState=='4'?'1':'0');
$out+='"> ';
if(t.taskState=='3'||t.taskState=='4'){
$out+=' <i class="fa fa-check fc-v1-green"></i> ';
}
$out+=' <span class="taskName pt-relative m-w-300" data-string="';
$out+=$escape(t.taskName);
$out+='"> ';
$out+=$escape(t.taskName);
$out+=' </span> </td> <td class="b-r-none b-l-none v-middle"> <span class="taskRemark pt-relative m-w-300" data-string="';
$out+=$escape(t.taskRemark);
$out+='"> ';
$out+=$escape(t.taskRemark==null || t.taskRemark==''?'--':t.taskRemark);
$out+=' </span> </td> <td class="no-pd v-middle b-r-none b-l-none"> <p class="m-xxs"> <span>';
$out+=$escape(t.companyName);
$out+=' :</span> <span class="person-in-charge" data-toggle="tooltip" data-original-title="';
$out+=$escape(t.designOrg);
$out+=$escape(t.departName==null?'':'('+t.departName+')');
$out+='"> ';
if(t.completeDate!=null){
$out+=' <i class="fa fa-check fc-v1-green"></i> ';
}
$out+=' ';
$out+=$escape(t.personInChargeName);
$out+=' </span> </p> </td> <td class="b-r-none b-l-none v-middle"> <span class="show-span"> ';
if(t.designUser && t.designUser.userList!=null && t.designUser.userList.length>0){
$out+=' ';
$out+=$escape(t.designUserName);
$out+=' <span class="fa fa-bookmark-o" tooltip-type="1" ata-toggle="tooltip" data-original-title=""></span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' </span> </td> <td class="b-r-none b-l-none v-middle"> <span class="show-span"> ';
if(t.checkUser && t.checkUser.userList!=null && t.checkUser.userList.length>0){
$out+=' ';
$out+=$escape(t.checkUserName);
$out+=' <span class="fa fa-bookmark-o" tooltip-type="2" ata-toggle="tooltip" data-original-title=""></span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' </span> </td> <td class="b-r-none b-l-none v-middle"> <span class="show-span"> ';
if(t.examineUser && t.examineUser.userList!=null && t.examineUser.userList.length>0){
$out+=' ';
$out+=$escape(t.examineUserName);
$out+=' <span class="fa fa-bookmark-o" tooltip-type="3" ata-toggle="tooltip" data-original-title=""></span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' </span> </td> <td class="b-r-none b-l-none v-middle"> <span class="show-span"> ';
if(t.planStartTime==null || t.planStartTime==''){
$out+=' <span class="fc-ccc">--</span> ';
}else{
$out+=' ';
$out+=$escape(t.planStartTime);
$out+=' <a href="javascript:void(0);" data-action="viewProgressChange" id="viewProgressChanges1';
$out+=$escape(t.id);
$out+='"><i class="fa fa-info-circle"></i></a> ';
}
$out+=' </span> </td> <td class="b-r-none b-l-none v-middle"> <span class="show-span"> ';
if(t.planEndTime==null || t.planEndTime==''){
$out+=' <span class="fc-ccc">--</span> ';
}else{
$out+=' ';
$out+=$escape(t.planEndTime);
$out+=' <a href="javascript:void(0);" data-action="viewProgressChange" id="viewProgressChanges2';
$out+=$escape(t.id);
$out+='"><i class="fa fa-info-circle"></i></a> ';
}
$out+=' </span> </td> <td class="v-middle b-l-none v-middle"> ';
if(t.stateHtml!=null && t.stateHtml!=''){
$out+=' ';
$out+=$escape(t.stateHtml);
$out+=' ';
}
$out+=' </td> </tr> ';
});
$out+=' ';
if(!(productionList && productionList.length>0)){
$out+=' <tr> <td colspan="9" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">您还没有相关生产安排</span> </div> </td> </tr> ';
}
$out+=' </table> </form>';
return new String($out);
});/*v:1*/
template('m_production/m_production_tab',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,tabList=$data.tabList,$each=$utils.$each,t=$data.t,i=$data.i,$escape=$utils.$escape,currTabI=$data.currTabI,$out='';$out+='<div class="tabs-container"> <ul class="nav nav-tabs"> ';
if(tabList!=null && tabList.length>0){
$out+=' ';
$each(tabList,function(t,i){
$out+=' <li class="';
$out+=$escape(i==currTabI?'active':'');
$out+='"> <a data-toggle="tab" href="#tab-';
$out+=$escape(i);
$out+='" data-action="getProductionById" data-i="';
$out+=$escape(i);
$out+='" data-id="';
$out+=$escape(t.id);
$out+='" aria-expanded="false">';
$out+=$escape(t.name);
$out+='</a> </li> ';
});
$out+=' ';
}
$out+=' </ul> <div class="tab-content"> ';
if(tabList!=null && tabList.length>0){
$out+=' ';
$each(tabList,function(t,i){
$out+=' <div id="tab-';
$out+=$escape(i);
$out+='" class="tab-pane ';
$out+=$escape(i==currTabI?'active':'');
$out+='"> <div class="panel-body"> <div id="taskIssueList';
$out+=$escape(i);
$out+='"> </div> <div id="productionList';
$out+=$escape(i);
$out+='" class="list-box"> </div> </div> </div> ';
});
$out+=' ';
}
$out+=' </div> </div>';
return new String($out);
});/*v:1*/
template('m_production/m_production_taskIssue',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,contentTaskList=$data.contentTaskList,$each=$utils.$each,t=$data.t,i=$data.i,$escape=$utils.$escape,_momentFormat=$helpers._momentFormat,$out='';if(contentTaskList!=null && contentTaskList.length>0){
$out+=' ';
$each(contentTaskList,function(t,i){
$out+=' <div class="panel panel-default"> <div class="panel-body"> <div class="row"> <div class="col-md-6"><h5>计划进度：';
$out+=$escape(_momentFormat(t.planStartTime,'YYYY/MM/DD'));
$out+=' - ';
$out+=$escape(_momentFormat(t.planEndTime,'YYYY/MM/DD'));
$out+='</h5></div> <div class="col-md-6 text-right"> ';
if(t.taskState==2 || t.taskState==4){
$out+=' <span class="text-danger">';
$out+=$escape(t.statusText);
$out+='</span> ';
}else{
$out+=' <span class="text-warning">';
$out+=$escape(t.statusText);
$out+='</span> ';
}
$out+=' </div> </div> <div class="row"> <div class="col-md-12"><h5>任务描述：</h5></div> <div class="col-md-8"> <P> ';
if(t.taskRemark==null || t.taskRemark==''){
$out+=' <span class="fc-ccc">--</span> ';
}else{
$out+=' ';
$out+=$escape(t.taskRemark);
$out+=' ';
}
$out+=' </P> </div> <div class="col-md-4 text-right"> 签发人：';
$out+=$escape(t.issueUserName);
$out+=' &nbsp;&nbsp; 签发时间：';
$out+=$escape(_momentFormat(t.issueTime,'YYYY/MM/DD'));
$out+=' </div> </div> <div class="row"> <div class="col-md-12 text-right"> <a class="btn btn-info btn-sm" data-action="deliveryHistory" data-task-id="';
$out+=$escape(t.id);
$out+='">交付历史</a> <a class="btn btn-default btn-sm" data-action="initiateDelivery" data-task-id="';
$out+=$escape(t.id);
$out+='">发起交付</a> </div> </div> </div> </div> ';
});
$out+=' ';
}
return new String($out);
});/*v:1*/
template('m_production/m_schedule_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,taskScheduleChangesList=$data.taskScheduleChangesList,t=$data.t,ti=$data.ti,$escape=$utils.$escape,$out='';$out+='<style> .m_schedule_list { height: 200px;overflow: auto; } .m_schedule_list .timeline-v2>li{ height: 42px; } .m_schedule_list .timeline-v2>li .cbp_tmlabel:after{ border-right-color:#fff } .m_schedule_list .timeline-v2>li .cbp_tmlabel{ background: #fff; color: #666; margin: 0 0 40px 45px; font-size: 13px; } .m_schedule_list .timeline-v2:before{ left: 40px; } .m_schedule_list .timeline-v2>li .cbp_tmicon{ left:40px; top:15px; } </style> <div class="m_schedule_list"> <ul class="timeline-v2 timeline-me"> ';
$each(taskScheduleChangesList,function(t,ti){
$out+=' <li > <time class="cbp_tmtime" datetime=""> </time> <i class="cbp_tmicon rounded-x hidden-xs" style=" "></i> <div class="cbp_tmlabel"> <p>';
$out+=$escape(t.historyText);
$out+='</p> </div> </li> ';
});
$out+=' </ul> </div>';
return new String($out);
});/*v:1*/
template('m_production/m_setDesigners',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,projectProcessNodes=$data.projectProcessNodes,p=$data.p,i=$data.i,$escape=$utils.$escape,t=$data.t,$index=$data.$index,$out='';$out+='<style> span.label.designerSpan{background-color: rgb(245, 245, 245);padding: 6px 9px;position: relative;} span.nameSpan{padding-right: 10px;} a[data-action="delDesigner"]{position: absolute;right: 3px;} a[data-action="addDesigners"]{padding-top: 2px;} </style> <div class="ibox m-b-xs"> <div class="ibox-content no-pd-bottom" > <div class="col-md-6"> ';
$each(projectProcessNodes,function(p,i){
$out+=' <div class="row m-b-sm designerRow" data-node-name="';
$out+=$escape(p.nodeName);
$out+='" data-node-id="" data-node-seq="';
$out+=$escape(i+1);
$out+='"> <div class="col-md-12"> <div class="col-md-2 no-pd-right"><span class="m-t-xs inline">';
$out+=$escape(p.nodeName);
$out+='：</span></div> <div class="col-md-10 user-list" style="padding-top: 4px;"> ';
$each(p.projectProcessNodeDTOList,function(t,$index){
$out+=' <span class="label label-default inline m-r-xs m-b-xs designerSpan" data-companyUserId="';
$out+=$escape(t.companyUserId);
$out+='" data-id="';
$out+=$escape(t.id);
$out+='"> <span class="nameSpan">';
$out+=$escape(t.userName);
$out+='</span> <a href="javascript:void(0)" data-action="delDesigner"><i class="glyphicon glyphicon-remove text-danger"></i></a> </span> ';
});
$out+='  </div> </div> </div> ';
});
$out+=' </div> <div class="col-md-6 gray-bg" id="choseUserBox"> </div> <div class="clearfix"></div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_project/m_addProjectDesignContent',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,designContentList=$data.designContentList,c=$data.c,$index=$data.$index,$escape=$utils.$escape,$out='';$out+='<style> .form-group.designContentBox label.checkbox input{position: absolute;} .form-group.designContentBox form{margin: 0;} .btn-u{background: #4765a0;font-size: 12px;padding: 2px 12px;line-height: 18px;} .btn-u.btn-u-red{background: #e74c3c;} </style> <div class="form-group designContentBox p-xs" style="width: 300px;"> ';
$each(designContentList,function(c,$index){
$out+=' <div class="designContentDiv" data-i="';
$out+=$escape($index);
$out+='"> <label class="checkbox dp-inline-block"> ';
if(c.isChecked==1){
$out+=' <input name="designContent" class="checkbox dinline" type="checkbox" checked value="';
$out+=$escape(c.id);
$out+='" content-name="';
$out+=$escape(c.name);
$out+='" design-content-id="';
$out+=$escape(c.designContentId);
$out+='" data-isHas="';
$out+=$escape(c.isHas);
$out+='"/> ';
}
$out+=' ';
if(c.isChecked!=1){
$out+=' <input name="designContent" class="checkbox dinline" type="checkbox" value="';
$out+=$escape(c.id);
$out+='" content-name="';
$out+=$escape(c.name);
$out+='" design-content-id="';
$out+=$escape(c.designContentId);
$out+='"/> ';
}
$out+=' <i></i> ';
$out+=$escape(c.name);
$out+=' <!--<span class="timeSpan">';
$out+=$escape(c.processTimeStr);
$out+='</span>--> <span class="timeBtn ';
if(c.isChecked!=1){
$out+='hide';
}
$out+='"> <a href="javascript:void(0)" type="button" data-action="setDesignContentTime"> ';
$out+=$escape(c.processTimeBtnStr==null?'设置进度':c.processTimeBtnStr);
$out+=' </a> </span> </label> </div> ';
});
$out+=' </div> ';
return new String($out);
});/*v:1*/
template('m_project/m_addProjectDesignRange',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,designRangeList=$data.designRangeList,d=$data.d,$index=$data.$index,$escape=$utils.$escape,otherRange=$data.otherRange,$out='';$out+='<style> .form-group.designRangeBox label.checkbox input{position: absolute;} .form-group.designRangeBox input[type="text"].form-control{ width: 100%;} .form-group.designRangeBox form{margin: 0;} </style> <div class="form-group designRangeBox p-n m-b-xs "> <form class="sky-form rounded-bottom" > <fieldset class="no-padding"> <div class="row"> ';
$each(designRangeList,function(d,$index){
$out+=' <div class="col-md-4"> <label class="i-checks fw-normal"> ';
if(d.isChecked==1){
$out+=' <input name="range" type="checkbox" checked value="';
$out+=$escape(d.name);
$out+='" data-id="';
$out+=$escape(d.id);
$out+='"/> ';
}
$out+=' ';
if(d.isChecked!=1){
$out+=' <input name="range" type="checkbox" value="';
$out+=$escape(d.name);
$out+='" data-id="';
$out+=$escape(d.id);
$out+='"/> ';
}
$out+=' ';
$out+=$escape(d.name);
$out+=' </label> </div> ';
});
$out+=' </div> <div class="row otherRangeRow"> ';
$each(otherRange,function(d,$index){
$out+=' <div class="col-md-4 liBox"> <div class="col-md-2 no-padding" > <label class=" i-checks fw-normal" title="';
$out+=$escape(d.designRange);
$out+='"> <input name="otherRange" class="checkbox" type="checkbox" checked/> </label> </div> <div class="col-md-10 no-pd-left no-pd-right"> <label class="input"> <input id="';
$out+=$escape(d.id);
$out+='" class="designRange form-control input-sm" type="text" name="designRange" placeholder="请输入名称" value="';
$out+=$escape(d.designRange);
$out+='"/> </label> </div> </div> ';
});
$out+=' <div class="col-md-4"> <label class="label hao curp no-bg" data-action="addOtherRange" style="padding: 3px 8px 3px 0"> <i class="fa fa-plus-square f-s-20" style="color: #1ab394"></i> <span class="color-dark-blue f-s-15">&nbsp;自定义</span> </label> </div> </div> <div class="row error-box"> <div class="col-md-12"></div> </div> <div class="row m-b-xs no-pd-right"> <div class="col-md-12"> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right "> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-r-xs"> <i class="fa fa-check"></i> </button> </div> </div> </fieldset> </form> </div> ';
return new String($out);
});/*v:1*/
template('m_project/m_addTimeChangeRecord',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,$index=$data.$index,startTime=$data.startTime,endTime=$data.endTime,timeDiffStr=$data.timeDiffStr,memo=$data.memo,$out='';$out+='<div class="col-md-12 detailListDiv time-row m-t-sm"> <div class="col-md-3 text-center"> <b>第';
$out+=$escape($index+1);
$out+='次变更:</b> </div> <div class="col-md-6 no-pd-left" > <span data-type="startTime">';
$out+=$escape(startTime);
$out+='</span> <span >~</span> <span data-type="endTime">';
$out+=$escape(endTime);
$out+='</span> ';
if(timeDiffStr!=''){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(timeDiffStr);
$out+='</span>天 )</span> ';
}
$out+=' </div> <div class="col-md-3 text-right"> <a class="btn-u btn-u-xs rounded" href="javascript:void(0)" data-action="addTimeChangeRecord" >变更</a>  </div> <div class="col-md-9 col-md-offset-3 no-pd-left m-t-sm"> <b>变更原因：</b> ';
$out+=$escape(memo);
$out+=' <div class="clearfix"></div> </div> <div class="clearfix"></div> </div> <div class="clearfix"></div> ';
return new String($out);
});/*v:1*/
template('m_project/m_choseConstructCompany','<div class="choiseConstructOBox" >  <form class="sky-form rounded-bottom noborder"> <fieldset> <section> <div class="col-md-12"> <div class="section row headline" style="height: 40px;"> <div class="col-md-6"> <div class="row"> <div class="col-md-4"> <label class="radio"> <input name="choiseConstruct" checked type="radio" data-action="choiseCommonAConstruct"> <i class="rounded-x"></i>常用甲方 </label> </div> <div class="col-md-4"> <label class="radio"> <input name="choiseConstruct" type="radio" data-action="searchAConstruct"> <i class="rounded-x"></i>搜索甲方 </label> </div> </div> </div> <div class="col-md-6 searchBox hide" > <div class="row"> <div class="col-md-6 col-md-offset-4" style="padding:0 7px;"> <div class="input"> <label class="input"> <input type="text" id="partAWord" name="keyword" placeholder="请输入甲方名称" > </label> </div> </div> <div class="col-md-2" style="padding:0 7px;"> <button type="submit" class="btn-u rounded" data-action="toSearchAConstruct">确定</button> </div> </div> </div> </div> </div> <table class="table table-hover constructList"> <thead> <tr> <td>序号</td> <td>组织名称</td> </tr> </thead> <tbody> </tbody> </table> </section> </fieldset> </form>  </div> ');/*v:1*/
template('m_project/m_choseOrg','<div class="choseOrgBox" >  <form class="sky-form rounded-bottom noborder"> <fieldset> <section>  <div class="col-md-12"> <table class="table table-hover orgList"> <thead> <tr> <td>序号</td> <td>组织名称</td> </tr> </thead> <tbody> </tbody> </table> </div> </section> </fieldset> </form>  </div> ');/*v:1*/
template('m_project/m_customDataDictionary',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,dataDictionaryList=$data.dataDictionaryList,d=$data.d,$index=$data.$index,$escape=$utils.$escape,customDataList=$data.customDataList,$out='';$out+='<style> .form-group.data-dictionary label.checkbox input{position: absolute;} .form-group.data-dictionary input[type="text"].form-control{ width: 100%;} .form-group.data-dictionary form{margin: 0;} </style> <div class="form-group data-dictionary p-n m-b-xs "> <form class="sky-form rounded-bottom" > <fieldset class="no-padding"> <div class="row"> ';
$each(dataDictionaryList,function(d,$index){
$out+=' <div class="col-md-4"> <label class="i-checks fw-normal"> ';
if(d.selected==true){
$out+=' <input name="dataDictionary" type="checkbox" checked value="';
$out+=$escape(d.name);
$out+='" data-id="';
$out+=$escape(d.id);
$out+='"/> ';
}else{
$out+=' <input name="dataDictionary" type="checkbox" value="';
$out+=$escape(d.name);
$out+='" data-id="';
$out+=$escape(d.id);
$out+='"/> ';
}
$out+=' ';
$out+=$escape(d.name);
$out+=' </label> </div> ';
});
$out+=' </div> <div class="row custom-data-dictionary"> ';
$each(customDataList,function(d,$index){
$out+=' <div class="col-md-4 liBox"> <div class="col-md-2 no-padding" > <label class=" i-checks fw-normal" title="';
$out+=$escape(d.name);
$out+='"> <input name="customDataDictionary" class="checkbox" type="checkbox" checked data-id="';
$out+=$escape(d.id);
$out+='"/> </label> </div> <div class="col-md-10 no-pd-left no-pd-right"> <label class="input"> <input id="';
$out+=$escape(d.id);
$out+='" class="form-control input-sm" type="text" name="iptCustomDataDictionary" placeholder="请输入名称" value="';
$out+=$escape(d.name);
$out+='"/> </label> </div> </div> ';
});
$out+=' <div class="col-md-4"> <label class="label hao curp no-bg" data-action="addCustomDataDictionary" style="padding: 3px 8px 3px 0"> <i class="fa fa-plus-square f-s-20" style="color: #1ab394"></i> <span class="color-dark-blue f-s-15">&nbsp;自定义</span> </label> </div> </div> <div class="row error-box"> <div class="col-md-12"></div> </div> <div class="row m-b-xs no-pd-right"> <div class="col-md-12"> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right "> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-r-xs"> <i class="fa fa-check"></i> </button> </div> </div> </fieldset> </form> </div> ';
return new String($out);
});/*v:1*/
template('m_project/m_defineEditableContent',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,title=$data.title,$escape=$utils.$escape,showAllButton=$data.showAllButton,showCloseButton=$data.showCloseButton,$out='';$out+=' <div class="popover editable-container editable-popup fade top in" role="tooltip"> <div class="arrow" style="left: 50%;"></div> ';
if(title && title!=''){
$out+=' <h3 class="popover-title">';
$out+=$escape(title);
$out+='</h3> ';
}
$out+=' <div class="popover-content"> <div> <div class="editableform-loading dp-none"></div> <form class="editableform"> <div class="control-group"> <div> <div class="editable-input"></div> <div class="editable-error-block help-block " style="display:none;"></div> ';
if(showAllButton){
$out+=' <div class="editable-buttons dp-block text-right overflow-hidden"> <button type="button" class="btn btn-default btn-sm editable-cancel m-n pull-right"> <i class="fa fa-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm editable-submit m-r-xs pull-right" > <i class="fa fa-check"></i> </button> </div> ';
}
$out+=' ';
if(showCloseButton){
$out+=' <div class="editable-buttons text-right"> <button type="button" class="btn btn-default btn-sm editable-cancel"> <i class="glyphicon glyphicon-remove"></i> </button> </div> ';
}
$out+=' </div> </div> </form> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_project/m_editCustomPropertyTemp',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,projectPropertyData=$data.projectPropertyData,$each=$utils.$each,p=$data.p,$index=$data.$index,$escape=$utils.$escape,u=$data.u,$out='';$out+='<div class="m_editCustomPropertyTemp"> <div class="col-md-6"> <div class="ibox"> <div class="ibox-title border-none p-h-sm min-h-50">数据模板</div> <div class="ibox-content border-darker-grey box-left"> <h4>模板标签库</h4> <div class="m-b-sm check-box"> <label class="i-checks fw-normal"> <input name="allProjectFieldCK" type="checkbox" value="0"/> <span class="i-checks-span">全选</span> </label> </div> <div class="row"> ';
if(projectPropertyData.basicPropertyList!=null && projectPropertyData.basicPropertyList.length>0){
$out+=' ';
$each(projectPropertyData.basicPropertyList,function(p,$index){
$out+=' <div class="col-md-6 no-pd-right check-box"> <label class="i-checks fw-normal"> <input name="projectFieldCk" type="checkbox" value="';
$out+=$escape(p.id);
$out+='" data-field-name="';
$out+=$escape(p.fieldName);
$out+='" data-unit-name="';
$out+=$escape(p.unitName);
$out+='"/> <span class="i-checks-span"> ';
$out+=$escape(p.fieldName);
$out+='&nbsp; ';
$out+=$escape(p.unitName!=null&& p.unitName!=''?'('+p.unitName+')':'');
$out+=' </span> </label> </div> ';
});
$out+=' ';
}
$out+=' <div class="clearfix"></div> </div> <h4>自定义库</h4> <div class="row"> <form class="addPropertyForm"> <div class="col-md-6"> <input class="form-control no-pd-right" type="text" name="fieldName"> </div> <div class="col-md-4 no-pd-right no-pd-left"> <!--<select class="form-control p-xxs" name="unitName"> <option value="">选择单位</option> ';
if(projectPropertyData.unitNameList!=null && projectPropertyData.unitNameList.length>0){
$out+=' ';
$each(projectPropertyData.unitNameList,function(u,$index){
$out+=' <option value="';
$out+=$escape(u);
$out+='">';
$out+=$escape(u);
$out+='</option> ';
});
$out+=' ';
}
$out+=' </select>--> <div class="input-group"> <input type="text" class="form-control" name="unitName" placeholder="请输入或选择" maxlength="8"> <div class="input-group-btn"> <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button> <ul class="dropdown-menu dropdown-menu-right" style="right: 0;left: inherit;max-height: 208px;overflow: auto;"> ';
if(projectPropertyData.unitNameList!=null && projectPropertyData.unitNameList.length>0){
$out+=' ';
$each(projectPropertyData.unitNameList,function(u,$index){
$out+=' ';
if(u!=null && u!=''){
$out+=' <li><a href="javascript:void(0);" style="padding:0px 20px;">';
$out+=$escape(u);
$out+='</a></li> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' </ul> </div> </div> </div> <div class="col-md-2"> <button type="button" class="btn btn-primary btn-sm" data-action="addPropertyBtn">添加</button> </div> <div class="col-md-12"></div> <div class="clearfix"></div> </form> </div> <div class="row m-t-sm" id="customPropertyBox"> </div> </div> </div> </div> <div class="col-md-6"> <div class="ibox"> <div class="ibox-title border-none p-h-sm min-h-50">已选择项 (拖拽进行调整顺序)</div> <div class="ibox-content border-darker-grey box-right"> <div class="row list-group" id="selectPropertyBox"> </div> </div> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_project/m_editCustomPropertyTempAdd',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,customPropertyList=$data.customPropertyList,$each=$utils.$each,s=$data.s,si=$data.si,$escape=$utils.$escape,$out='';if(customPropertyList!=null && customPropertyList.length>0 ){
$out+=' ';
$each(customPropertyList,function(s,si){
$out+=' <div class="col-md-6 no-pd-right m-b-xs check-box"> <label class="i-checks fw-normal"> <input name="cusProjectFieldCk" type="checkbox" value="';
$out+=$escape(s.id);
$out+='" data-field-name="';
$out+=$escape(s.fieldName);
$out+='" data-unit-name="';
$out+=$escape(s.unitName);
$out+='"/> <span class="i-checks-span"> <span class=" property-span m-r-xs bg-muted p-xxs f-s-12" > <span class="field-name" data-string="';
$out+=$escape(s.fieldName);
$out+=' ';
$out+=$escape(s.unitName!=null&& s.unitName!=''?'('+s.unitName+')':'');
$out+='"> ';
$out+=$escape(s.fieldName);
$out+='&nbsp; ';
$out+=$escape(s.unitName!=null&& s.unitName!=''?'('+s.unitName+')':'');
$out+=' </span> <a class="curp" href="javascript:void(0)" data-id="';
$out+=$escape(s.id);
$out+='" data-index=';
$out+=$escape(si);
$out+=' data-field-name="';
$out+=$escape(s.fieldName);
$out+='" data-unit-name="';
$out+=$escape(s.unitName);
$out+='" data-action="delCusProperty"> <i class="fa fa-times color-red"></i></a> </span> </span> </label> </div> ';
});
$out+=' ';
}
return new String($out);
});/*v:1*/
template('m_project/m_editCustomPropertyTempSelect',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,selectedPropertyList=$data.selectedPropertyList,$each=$utils.$each,s=$data.s,si=$data.si,$escape=$utils.$escape,$out='';if(selectedPropertyList!=null && selectedPropertyList.length>0 ){
$out+=' ';
$each(selectedPropertyList,function(s,si){
$out+=' <div class="col-md-6 no-pd-right m-b list-group-item" data-sortId="';
$out+=$escape(si);
$out+='"> <span class=" property-span m-r-xs bg-muted p-xxs f-s-12" > <span class="field-name" data-string="';
$out+=$escape(s.fieldName);
$out+=' ';
$out+=$escape(s.unitName!=null&& s.unitName!=''?'('+s.unitName+')':'');
$out+='"> ';
$out+=$escape(s.fieldName);
$out+='&nbsp; ';
$out+=$escape(s.unitName!=null&& s.unitName!=''?'('+s.unitName+')':'');
$out+=' </span> <a class="curp" href="javascript:void(0)" data-id="';
$out+=$escape(s.id);
$out+='" data-field-name="';
$out+=$escape(s.fieldName);
$out+='" data-unit-name="';
$out+=$escape(s.unitName);
$out+='" data-action="delSelectedProperty"> <i class="fa fa-times color-red"></i></a> </span> </div> ';
});
$out+=' ';
}
return new String($out);
});/*v:1*/
template('m_project/m_editDesignContent',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,designContentList=$data.designContentList,d=$data.d,$index=$data.$index,$escape=$utils.$escape,v=$data.v,$out='';$out+='<div class="designContentOBox" > <form class="sky-form rounded-bottom noborder p-m" > ';
$each(designContentList,function(d,$index){
$out+=' <div class=" designContentDiv" data-i="';
$out+=$escape($index);
$out+='"> <div class="col-md-12 liBox time-row"> <div class="col-md-3 no-padding m-t-sm text-center" title="';
$out+=$escape(d.contentName);
$out+='" style="overflow:hidden;text-overflow:ellipsis;"> ';
$out+=$escape(d.contentName);
$out+=' </div> <div class="col-md-7 no-padding m-t-sm">  ';
if(d.projectProcessTimeEntityList && d.projectProcessTimeEntityList.length>0){
$out+=' <span data-type="startTime">';
$out+=$escape(d.projectProcessTimeEntityList[0].startTime);
$out+='</span> <span>~</span> <span data-type="endTime">';
$out+=$escape(d.projectProcessTimeEntityList[0].endTime);
$out+='</span> ';
if(d.projectProcessTimeEntityList[0].timeDiffStr!=''){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(d.projectProcessTimeEntityList[0].timeDiffStr);
$out+='</span>天 )</span> ';
}
$out+=' ';
}
$out+=' </div> <div class="col-md-2 text-right btnBox m-t-sm"> <a class="btn btn-primary btn-xs rounded ';
if(d.projectProcessTimeEntityList.length>1){
$out+='hide';
}
$out+='" href="javascript:void(0)" data-action="addTimeChangeRecord">变更</a> <!--<a class="btn btn-xs btn-danger rounded ';
if(d.projectProcessTimeEntityList.length>1){
$out+='hide';
}
$out+='" href="javascript:void(0)" data-action="delTimeChangeRecord">删除</a>--> </div> </div>  ';
$each(d.projectProcessTimeEntityList,function(v,$index){
$out+=' ';
if($index>0){
$out+=' <div class="col-md-12 detailListDiv time-row m-t-sm "> <div class="col-md-3 text-center"> <b>第';
$out+=$escape($index);
$out+='次变更:</b> </div> <div class="col-md-6 no-pd-left" style="background: transparent"> <span data-type="startTime">';
$out+=$escape(v.startTime);
$out+='</span> ';
if((v.startTime!=null && v.startTime!='') || (v.endTime!=null && v.endTime!='')){
$out+=' <span>~</span> ';
}
$out+=' <span data-type="endTime">';
$out+=$escape(v.endTime);
$out+='</span> ';
if((v.startTime!=null && v.startTime!='') && (v.endTime!=null && v.endTime!='')){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(v.timeDiffStr);
$out+='</span>天 )</span> ';
}
$out+=' </div> <div class="col-md-3 text-right btnBox" style="padding-top: 3px;"> <a class="btn-u btn-u-xs rounded ';
if(!(d.projectProcessTimeEntityList.length==$index+1)){
$out+='hide';
}
$out+='" href="javascript:void(0)" data-action="addTimeChangeRecord">变更</a> <!--<a class="btn-u btn-u-xs btn-u-red rounded ';
if(!(d.projectProcessTimeEntityList.length==$index+1)){
$out+='hide';
}
$out+='" href="javascript:void(0)" data-action="delTimeChangeRecord">删除</a>--> </div> <div class="col-md-9 col-md-offset-3 no-pd-left m-t-sm"> <b>变更原因：</b> ';
$out+=$escape(v.memo);
$out+=' <div class="clearfix"></div> </div> <div class="clearfix"></div> </div> ';
}
$out+=' ';
});
$out+='  <div class="clearfix"></div> </div> ';
});
$out+=' </form> </div> <div class="clearfix"></div> ';
return new String($out);
});/*v:1*/
template('m_project/m_editDesignRange',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,designRangeList=$data.designRangeList,d=$data.d,$index=$data.$index,$escape=$utils.$escape,otherRange=$data.otherRange,$out='';$out+=' <div class="addRangeOBox"> <form class="sky-form rounded-bottom" > <fieldset> <div class="row"> ';
$each(designRangeList,function(d,$index){
$out+=' <div class="col-md-3"> <label class="checkbox" title="';
$out+=$escape(d.name);
$out+='"> ';
if(d.isChecked==1){
$out+=' <input name="range" class="checkbox" type="checkbox" checked value="';
$out+=$escape(d.name);
$out+='"/> ';
}
$out+=' ';
if(d.isChecked!=1){
$out+=' <input name="range" class="checkbox" type="checkbox" value="';
$out+=$escape(d.name);
$out+='"/> ';
}
$out+=' <i></i> ';
$out+=$escape(d.name);
$out+=' </label> </div> ';
});
$out+=' </div> <div class="row otherRangeRow"> ';
$each(otherRange,function(d,$index){
$out+=' <div class="col-md-3 liBox"> <div class="col-md-2 no-padding" > <label class="checkbox" title="';
$out+=$escape(d.designRange);
$out+='"> <input name="otherRange" class="checkbox" type="checkbox" checked/> <i></i> </label> </div> <div class="col-md-10 out-box no-pd-left no-pd-right"> <label class="input"> <input id="';
$out+=$escape(d.id);
$out+='" class="designRange form-control" type="text" name="designRange" placeholder="请输入名称" value="';
$out+=$escape(d.designRange);
$out+='"/> </label> </div> </div> ';
});
$out+=' <div class="col-md-3"> <label class="label hao curp" data-action="addOtherRange"> <i class="fa fa-plus-square f-s-20"></i> <span class="color-dark-blue f-s-15">&nbsp;自定义</span> </label> </div> </div> </fieldset> </form> </div> ';
return new String($out);
});/*v:1*/
template('m_project/m_entryAddress',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,$detailAddress=$data.$detailAddress,$out='';$out+='<div class="selectRegionOBox border-none no-pd-bottom" style="width: 370px;"> <fieldset> <section> <div class="form-group m-b-xs col-md-12 no-pd-right no-pd-left" style="margin: 0 0 5px 0"> <label for="selectRegion">所在地区</label> <div class="input-group cityBox" id="selectRegion" name="selectRegion"> <div class="dp-inline-block"> <select class="prov form-control" name="province"></select> </div> <div class="dp-inline-block m-l-xs"> <select class="city form-control" name="city" disabled="disabled" style="display: none;"></select> </div> <div class="dp-inline-block m-l-xs"> <select class="dist form-control" name="county" disabled="disabled" style="display: none;"></select> </div> </div> </div> <div class="form-group m-b-xs col-md-12 no-pd-right no-pd-left" style="margin: 0 0 5px 0"> <label for="detailAddress">详细地址：</label> <input type="text" class="form-control" style="width: 100%;" name="detailAddress" id="detailAddress" maxlength="255" value="';
$out+=$escape($detailAddress);
$out+='"> </div> </section> </fieldset> <div class="col-md-12 m-b-xs no-pd-right"> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right "> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-r-xs"> <i class="fa fa-check"></i> </button> <div class="clearfix"></div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_project/m_popover_partyA',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,enterpriseId=$data.enterpriseId,constructCompanyName=$data.constructCompanyName,$out='';$out+='<form class="form" style="font-size: 12px;"> <div class="form-group m-b-xs" style="padding: 15px 0 60px;"> <label class="col-24-sm-4 control-label text-right m-t-xs" for="constructCompanyName" >甲方</label>  <div class="col-24-sm-14" style="padding-right: 0;"> <input type="text" class="form-control constructCompanyName" name="constructCompanyName" id="constructCompanyName" placeholder="请输入甲方名称" autocomplete="off" data-id="';
$out+=$escape(enterpriseId);
$out+='" value="';
$out+=$escape(constructCompanyName);
$out+='"> <div class="partyA-select-box" style="position: absolute; top: 100%; left: 14px; z-index: 100; right: auto;background: #fff;border: solid 1px #ccc;border-radius: 5px;box-shadow: 1px 1px 6px 0 #9a9a9a;min-width: 80%; display: none;"> </div> </div> <div class="col-24-sm-6" style="padding: 0 10px 0;"> <button type="button" class="btn btn-default btn-sm m-popover-close m-n pull-right"> <i class="fa fa-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit m-r-xs pull-right" > <i class="fa fa-check"></i> </button> </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_project/m_popover_partyB','<style> .select2 span.select2-selection{height:34px;border-radius:0px;} </style> <form class="form" style="font-size: 12px;"> <div class="form-group m-b-xs"> <label class="col-24-sm-7 control-label m-t-sm no-pd-left" for="partB">乙方</label> <select name="partB" id="partB" class="col-24-sm-17 orm-control input-sm" style="width:230px;"></select> </div>   <div class="form-group clearfix"> <div class="col-md-12 no-pd-right"> <button type="button" class="btn btn-default btn-sm m-popover-close m-n pull-right"> <i class="fa fa-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit m-r-xs pull-right" > <i class="fa fa-check"></i> </button>      </div> </div> </form>');/*v:1*/
template('m_project/m_popover_projectType',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectType=$data.projectType,$each=$utils.$each,projectTypeList=$data.projectTypeList,p=$data.p,$index=$data.$index,$out='';$out+='<form class="form"> <div class="form-group m-b-none" > <div class="col-24-sm-14 no-pd"> <div class="input-group"> <input type="text" class="form-control" name="projectType" style="height: 30px;padding-right: 24px;" value="';
$out+=$escape(projectType);
$out+='"> <span class="editable-clear-x" style="right: 40px;display: none;"></span> <div class="input-group-btn"> <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" style="height: 30px;padding: 3px 12px;"> <span class="caret"></span> </button> <ul class="dropdown-menu dropdown-menu-right" style="right: 0;left: inherit;"> ';
$each(projectTypeList,function(p,$index){
$out+=' <li><a href="javascript:void(0);">';
$out+=$escape(p.name);
$out+='</a></li> ';
});
$out+=' </ul> </div> </div> </div> <div class="col-24-sm-8 no-pd" > <button type="button" class="btn btn-primary btn-sm m-popover-submit m-l-xs" > <i class="fa fa-check"></i> </button> <button type="button" class="btn btn-default btn-sm m-popover-close m-n"> <i class="fa fa-remove"></i> </button> </div> <div class="clearfix"></div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_project/m_projectBasicInfo',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,isView=$data.isView,project=$data.project,deleteFlag=$data.deleteFlag,editFlag=$data.editFlag,_isNullOrBlank=$helpers._isNullOrBlank,$each=$utils.$each,b=$data.b,bi=$data.bi,p=$data.p,pi=$data.pi,_subStr=$helpers._subStr,r=$data.r,j=$data.j,f=$data.f,fi=$data.fi,fastdfsUrl=$data.fastdfsUrl,currentCompanyId=$data.currentCompanyId,i=$data.i,c=$data.c,_momentFormat=$helpers._momentFormat,$out='';$out+='<div class="ibox m_projectBasicInfo ';
$out+=$escape(isView==true?'min-h':'');
$out+='"> <div class="ibox-title secondary-menu-outbox ';
$out+=$escape(isView==true?'hide':'');
$out+='"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 我的项目 </li> <li class=" fa fa-angle-right"> ';
$out+=$escape(project.projectName);
$out+=' </li> <li class="active fa fa-angle-right"> <strong>基本信息</strong> </li> </ol> </div> </div> <div class="col-md-6"> <div class="btn-group pull-right m-t-sm m-r-xs"> <!--';
if((project.attentionId==null || project.attentionId=='')){
$out+=' <a class="btn btn-sm btn-default btn-bitbucket attention shoucang m-r-none" title="关注该项目" data-id="';
$out+=$escape(project.id);
$out+='" data-attention-id="';
$out+=$escape(project.attentionId);
$out+='"><i class="fa fa-star"></i>&nbsp;<span class="text">关注</span></a> ';
}
$out+=' ';
if(project.attentionId!=null && project.attentionId!=''){
$out+=' <a class="btn btn-sm btn-default btn-bitbucket attention shoucangshixin m-r-none" title="取消关注该项目" data-id="';
$out+=$escape(project.id);
$out+='" data-attention-id="';
$out+=$escape(project.attentionId);
$out+='"><i class="fa fa-star fc-v1-yellow"></i>&nbsp;<span class="text">取消关注</span></a> ';
}
$out+='--> </div> </div> </div> </div> <div class="ibox-content no-pd-bottom no-borders"> <form class="sky-form no-margins"> <div class="row"> <div class="col-md-6"><h3>基本信息</h3></div> <div class="col-md-6"> ';
if(deleteFlag!=null && deleteFlag==1){
$out+=' <a class="btn btn-default btn-sm m-r-none pull-right ';
$out+=$escape(isView==true?'hide':'');
$out+='" data-toggle="tooltip" data-placement="top" data-action="deleteProject" data-id="';
$out+=$escape(project.id);
$out+='" title="删除该项目" style="color: red"><span>删除项目</span> </a> ';
}
$out+=' </div> </div> <table class="table table-bordered"> <tbody> <tr> <td width="12%"><b>项目名称</b></td> <td width="37%"> <span><a href="javascript:void(0);" data-action="text_projectName">';
$out+=$escape(project.projectName);
$out+='</a></span> </td> <td><b>项目状态</b></td> <td> <a href="javascript:void(0);" data-action="select_status" data-status="';
$out+=$escape(project.projectStatus.valueId);
$out+='"> ';
$out+=$escape(project.projectStatus.content);
$out+=' </a> </td> </tr> <tr> <td><b>项目编号</b></td> <td> <span><a href="javascript:void(0);" data-action="text_projectNo">';
$out+=$escape(project.projectNo);
$out+='</a> </span> </td> <td width="12%"><b>甲方</b></td> <td width="39%"> ';
if(editFlag){
$out+=' ';
if(_isNullOrBlank(project.partyACompany)){
$out+=' <a class="curp constructCompanyName" style="line-height: 30px;color:#ccc;" data-action="edit_constructCompanyName">未设置</a> ';
}else{
$out+=' <a class="curp constructCompanyName" data-action="edit_constructCompanyName"> ';
$out+=$escape(project.partyACompany.companyName);
$out+=' </a> ';
}
$out+=' ';
}else{
$out+=' <span> ';
if(!_isNullOrBlank(project.partyACompany)){
$out+=' ';
$out+=$escape(project.partyACompany.companyName);
$out+=' ';
}
$out+=' </span> ';
}
$out+=' </td> </tr> <tr> <td><b>合同签订</b></td> <td> ';
if(editFlag){
$out+=' ';
if(project.contractDate!=null && project.contractDate!=''){
$out+=' <span><a href="javascript:void(0);" data-action="edit_signDate" class="editable editable-click">';
$out+=$escape(project.contractDate);
$out+='</a></span> ';
}
$out+=' ';
if(project.contractDate==null || project.contractDate==''){
$out+=' <span><a href="javascript:void(0);" data-action="edit_signDate" class="editable editable-click" style="color:#ccc;font-style:normal;">未签订</a></span> ';
}
$out+=' ';
}
$out+=' ';
if(!editFlag){
$out+=' ';
if(project.contractDate!=null && project.contractDate!=''){
$out+=' <span>';
$out+=$escape(project.contractDate);
$out+='</span> ';
}
$out+=' ';
if(project.contractDate==null || project.contractDate==''){
$out+=' <span>未签订</span> ';
}
$out+=' ';
}
$out+=' </td> <td><b>乙方</b></td> <td> <span> <input type="hidden" name="companyBid" value="';
$out+=$escape(project.partyBCompany!=null?project.partyBCompany.id:'');
$out+='"/> ';
if(editFlag){
$out+=' ';
if(_isNullOrBlank(project.partyBCompany)){
$out+=' <a class="curp companyChoise" style="line-height: 30px;color:#ccc;" data-action="edit_companyBidName">请点击选择乙方</a> ';
}else{
$out+=' <a class="curp companyBidName" style="line-height: 30px;" data-action="edit_companyBidName" data-companyBid="';
$out+=$escape(project.partyBCompany.id);
$out+='" data-companyBidName="';
$out+=$escape(project.partyBCompany.companyName);
$out+='" data-partBManagerId="';
$out+=$escape(project.operatorOfPartyB==null?'':project.operatorOfPartyB.id);
$out+='" data-partBManagerName="';
$out+=$escape(project.operatorOfPartyB==null?'':project.operatorOfPartyB.companyUserName);
$out+='" data-partBDesignerId="" data-partBDesignerName=""> ';
$out+=$escape(project.partyBCompany.companyName);
$out+='&nbsp;&nbsp; </a> ';
}
$out+=' ';
}else{
$out+=' <span></span> ';
}
$out+=' ';
if(!editFlag && project.partyBCompany!=null){
$out+=' ';
$out+=$escape(project.partyBCompany.companyName);
$out+='&nbsp;&nbsp; ';
}
$out+=' </span> </td> </tr> <tr> <td><b>项目类型</b></td> <td> <a href="javascript:void(0);" data-action="edit_projectType" class="editable editable-click"> ';
$out+=$escape(project.projectType==null||project.projectType.content==null||project.projectType.content==''?'未设置':project.projectType.content);
$out+=' </a> </td> <td><b>立项组织</b></td> <td> ';
if(project.creatorCompany!=null){
$out+=' ';
$out+=$escape(project.creatorCompany.companyName);
$out+=' ';
}
$out+=' </td> </tr> <tr> <td><b>功能分类</b></td> <td> <a href="javascript:void(0);" class="editable editable-click" data-action="edit_builtType"> ';
if(project.buildTypeList!=null && project.buildTypeList.length>0){
$out+=' ';
$each(project.buildTypeList,function(b,bi){
$out+=' ';
if(bi == project.buildTypeList.length-1){
$out+=' ';
$out+=$escape(b.name);
$out+=' ';
}else{
$out+=' ';
$out+=$escape(b.name);
$out+=', ';
}
$out+=' ';
});
$out+=' ';
}else{
$out+=' 未设置 ';
}
$out+=' </a> </td> <td><b>项目地点</b></td> <td> <span> <a href="javascript:void(0);" class="editable editable-click" data-action="edit_address" id="address" data-original-title title> ';
if(project.projectLocation!=null){
$out+=' ';
if(project.projectLocation.province && project.projectLocation.province!=''){
$out+=' ';
$out+=$escape(project.projectLocation.province);
$out+='&nbsp;&nbsp; ';
}
$out+=' ';
if(project.projectLocation.city && project.projectLocation.city!=''){
$out+=' ';
$out+=$escape(project.projectLocation.city);
$out+='&nbsp;&nbsp; ';
}
$out+=' ';
if(project.projectLocation.county && project.projectLocation.county!=''){
$out+=' ';
$out+=$escape(project.projectLocation.county);
$out+='&nbsp;&nbsp; ';
}
$out+=' ';
if(project.projectLocation.detailAddress && project.projectLocation.detailAddress!=''){
$out+=' ';
$out+=$escape(project.projectLocation.detailAddress);
$out+=' ';
}
$out+=' ';
}
$out+=' </a> </span> </td> </tr> </tbody> </table> <div class="row"> <div class="col-md-6"> <h3 class="pull-left">专业信息</h3> ';
if(editFlag){
$out+=' <a class="pull-left m-t-xs m-l-sm" href="javascript:void(0);" data-action="customInfoTemp"> <i class="fa fa-pencil text-navy"></i> </a> ';
}
$out+=' </div> </div> <table class="table table-bordered"> ';
if(project.projectPropertyList!=null && project.projectPropertyList.length>0){
$out+=' ';
$each(project.projectPropertyList,function(p,pi){
$out+=' ';
if(pi%2==0){
$out+=' <tr> <td width="12%"><b>';
$out+=$escape(p.fieldName);
$out+='</b></td> <td width="37%"> ';
if(p.fieldName=='建筑层数' && p.unitName=='层'){
$out+=' <div> 地下： <span class="dp-inline-block" style="min-width: 10%;"> <a href="javascript:void(0);" data-field-id="';
$out+=$escape(p.id);
$out+='" data-field-unit="';
$out+=$escape(p.unitName);
$out+='" data-field-value="';
$out+=$escape(_subStr(p.fieldValue,';',0));
$out+='" data-action="text_propertyFieldDown';
$out+=$escape(pi);
$out+='">';
$out+=$escape(_subStr(p.fieldValue,';',0));
$out+='</a> </span> ';
if(_subStr(p.fieldValue,';',0)!=''){
$out+=' <span class="unit-span">&nbsp;层</span> ';
}
$out+=' ；地上： <span class="dp-inline-block" style="min-width: 10%;"> <a href="javascript:void(0);" data-field-id="';
$out+=$escape(p.id);
$out+='" data-field-unit="';
$out+=$escape(p.unitName);
$out+='" data-field-value="';
$out+=$escape(_subStr(p.fieldValue,';',1));
$out+='" data-action="text_propertyFieldUp';
$out+=$escape(pi);
$out+='">';
$out+=$escape(_subStr(p.fieldValue,';',1));
$out+='</a> </span> ';
if(_subStr(p.fieldValue,';',1)!=''){
$out+=' <span class="unit-span">&nbsp;层</span> ';
}
$out+=' </div> ';
}else{
$out+=' <span> ';
if(p.unitName!=null && p.unitName!=''){
$out+=' <a href="javascript:void(0);" data-field-id="';
$out+=$escape(p.id);
$out+='" data-field-unit="';
$out+=$escape(p.unitName);
$out+='" data-field-value="';
$out+=$escape(p.fieldValue);
$out+='" data-action="text_propertyField';
$out+=$escape(pi);
$out+='">';
$out+=$escape(p.fieldValue);
$out+='</a> <span class="unit-span">';
$out+=$escape(p.unitName);
$out+='</span> ';
}else{
$out+=' <a href="javascript:void(0);" data-field-id="';
$out+=$escape(p.id);
$out+='" data-field-unit="';
$out+=$escape(p.unitName);
$out+='" data-field-value="';
$out+=$escape(p.fieldValue);
$out+='" data-action="text_propertyField';
$out+=$escape(pi);
$out+='">';
$out+=$escape(p.fieldValue);
$out+='</a> ';
}
$out+=' </span> ';
}
$out+=' </td> ';
}else{
$out+=' <td width="12%"><b>';
$out+=$escape(p.fieldName);
$out+='</b></td> <td> ';
if(p.fieldName=='建筑层数' && p.unitName=='层'){
$out+=' <div> 地下： <span class="dp-inline-block" style="min-width: 10%;"> <a href="javascript:void(0);" data-field-id="';
$out+=$escape(p.id);
$out+='" data-field-unit="';
$out+=$escape(p.unitName);
$out+='" data-field-value="';
$out+=$escape(_subStr(p.fieldValue,';',0));
$out+='" data-action="text_propertyFieldDown';
$out+=$escape(pi);
$out+='">';
$out+=$escape(_subStr(p.fieldValue,';',0));
$out+='</a> </span> ';
if(_subStr(p.fieldValue,';',0)!=''){
$out+=' <span class="unit-span">&nbsp;层</span> ';
}
$out+=' ；地上： <span class="dp-inline-block" style="min-width: 10%;"> <a href="javascript:void(0);" data-field-id="';
$out+=$escape(p.id);
$out+='" data-field-unit="';
$out+=$escape(p.unitName);
$out+='" data-field-value="';
$out+=$escape(_subStr(p.fieldValue,';',1));
$out+='" data-action="text_propertyFieldUp';
$out+=$escape(pi);
$out+='">';
$out+=$escape(_subStr(p.fieldValue,';',1));
$out+='</a> </span> ';
if(_subStr(p.fieldValue,';',1)!=''){
$out+=' <span class="unit-span">&nbsp;层</span> ';
}
$out+=' </div> ';
}else{
$out+=' <span> ';
if(p.unitName!=null && p.unitName!=''){
$out+=' <a href="javascript:void(0);" data-field-id="';
$out+=$escape(p.id);
$out+='" data-field-unit="';
$out+=$escape(p.unitName);
$out+='" data-field-value="';
$out+=$escape(p.fieldValue);
$out+='" data-action="text_propertyField';
$out+=$escape(pi);
$out+='">';
$out+=$escape(p.fieldValue);
$out+='</a> <span class="unit-span">';
$out+=$escape(p.unitName);
$out+='</span> ';
}else{
$out+=' <a href="javascript:void(0);" data-field-id="';
$out+=$escape(p.id);
$out+='" data-field-unit="';
$out+=$escape(p.unitName);
$out+='" data-field-value="';
$out+=$escape(p.fieldValue);
$out+='" data-action="text_propertyField';
$out+=$escape(pi);
$out+='">';
$out+=$escape(p.fieldValue);
$out+='</a> ';
}
$out+=' </span> ';
}
$out+=' </td> </tr> ';
}
$out+=' ';
});
$out+=' ';
if((project.projectPropertyList.length+1)%2==0){
$out+=' <td width="12%"></td> <td></td> </tr> ';
}
$out+=' ';
}
$out+=' </table> <div class="row"> <div class="col-md-6"> <h3 class="pull-left">设计范围</h3> ';
if(editFlag){
$out+=' <a href="javascript:void(0)" class="designRangeEditBtn pull-left m-t-xs m-l-sm" data-action="edit_designRange"> <i class=" fa fa-pencil fc-dark-blue"></i> </a> ';
}
$out+=' </div> </div> <table class="table table-bordered"> <tbody> <tr> <td width="12%"><b>设计范围</b></td> <td colspan="3"> ';
if(project.projectRangeList && project.projectRangeList.length>0){
$out+=' ';
$each(project.projectRangeList,function(r,j){
$out+=' <div class=" adaptWidth2 c-tool-tip designRangeDiv m-xs dp-inline-block"> <div class="rounded-md text-center greyBg_item" title="';
$out+=$escape(r.designRange);
$out+='" tool-tip> <span>';
$out+=$escape(r.designRange);
$out+='</span> </div> </div> ';
});
$out+=' ';
}else if(!editFlag){
$out+=' <span></span> ';
}
$out+=' </td> </tr> </tbody> </table> <div class="row"> <div class="col-md-6"> <h3 class="pull-left">合同信息</h3> </div> </div> <table class="table table-bordered"> <tbody> <tr> <td><b>合同附件</b></td> <td colspan="3">   ';
if(editFlag){
$out+=' ';
if(project.contractList!=null && project.contractList.length>0){
$out+=' ';
$each(project.contractList,function(f,fi){
$out+=' <span class="file-span m-r-xs p-w-xs"> <a href="';
$out+=$escape(fastdfsUrl+f.filePath);
$out+='" target="_blank" class="designIcon fa theContractFile "> ';
$out+=$escape((f.fileName==null || f.fileName=='')?project.projectName+'合同附件.pdf':f.fileName);
$out+=' </a> <span class="file-del-span"> <a class="curp" href="javascript:void(0)" data-id="';
$out+=$escape(f.id);
$out+='" data-action="delFile" style="display: none;"> <i class="fa fa-times color-red"></i> </a> </span> </span> ';
});
$out+=' ';
}
$out+='  ';
}else if(!editFlag && project.creatorCompany.id == currentCompanyId && (project.contractList!=null && project.contractList.length>0)){
$out+=' ';
$each(project.contractList,function(f,fi){
$out+=' <a href="';
$out+=$escape(fastdfsUrl+f.filePath);
$out+='" target="_blank" class="designIcon fa theContractFile m-r-xs p-w-xs roleControl" roleCode="project_view_amount,sys_enterprise_logout,project_eidt" flag="1"> ';
$out+=$escape((f.fileName==null || f.fileName=='')?project.projectName+'合同附件.pdf':f.fileName);
$out+=' </a> ';
});
$out+='  ';
}else if(!editFlag && project.creatorCompany.id == currentCompanyId && (project.contractList==null || project.contractList.length==0)){
$out+=' <span class=" roleControl" roleCode="project_view_amount,sys_enterprise_logout" flag="6">暂无上传文件</span> ';
}else{
$out+=' *** ';
}
$out+='  ';
if(editFlag){
$out+=' <a href="javascript:void(0)" id="filePicker" name="recordAttach" class="upload-btn" title="请上传PDF文件"></a> ';
if(project.filePath==null || project.filePath==''){
$out+=' <note class="pt-relative" style="color: #777;bottom: 3px;" >请上传pdf文件！</note> ';
}
$out+=' ';
}
$out+=' </td> </tr> <tr style="height: 70px"> <td width="12%"><b>设计任务</b></td> <td colspan="3"> <div class="no-padding projectDesignContent"> <ul class="todo-list small-list bg-white" style="border:1px solid rgba(0,0,0,0)"> <li class="designContentLi" data-i="';
$out+=$escape(i);
$out+='" style="margin-bottom: -8px;padding-bottom: 0px;"> ';
if(project.projectDesignContentList && project.projectDesignContentList.length>0){
$out+=' ';
$each(project.projectDesignContentList,function(c,i){
$out+=' ';
if(editFlag){
$out+=' <span class="design_item designContentDiv" data-i="';
$out+=$escape(i);
$out+='" data-id="';
$out+=$escape(c.id);
$out+='"> <label class="i-checks dp-inline-block m-b-none"> <input name="designContent" class="checkbox dinline" type="checkbox" checked value="';
$out+=$escape(c.id);
$out+='" content-name="';
$out+=$escape(c.contentName);
$out+='" data-isHas="';
$out+=$escape(c.isHas);
$out+='"/> </label> <div class="pull-left"> <span class="content-name" data-toggle="tooltip" data-original-title="';
$out+=$escape(c.contentName);
$out+='"> ';
$out+=$escape(c.contentName);
$out+=' </span> ';
if(c.projectProcessTimeEntityList!=null && c.projectProcessTimeEntityList.length>0){
$out+=' <a href="javascript:void(0)" type="button" class="editable editable-click dp-block" data-action="edit_setDesignContentTime"> ';
$out+=$escape(_momentFormat(c.projectProcessTimeEntityList[c.projectProcessTimeEntityList.length-1].startTime,'YYYY/MM/DD'));
$out+=' - ';
$out+=$escape(_momentFormat(c.projectProcessTimeEntityList[c.projectProcessTimeEntityList.length-1].endTime,'YYYY/MM/DD'));
$out+=' </a> ';
}else{
$out+=' <a href="javascript:void(0)" type="button" class="editable editable-click editable-empty dp-block" data-action="edit_setDesignContentTime">未设置合同进度</a> ';
}
$out+=' </div> </span> ';
}else{
$out+=' <span class="design_item designContentDiv" data-i=""> <div class="pull-left"> <span class="content-name" data-toggle="tooltip" data-original-title="';
$out+=$escape(c.contentName);
$out+='"> ';
$out+=$escape(c.contentName);
$out+=' </span> ';
if(project.creatorCompany.id == currentCompanyId || (project.partyBCompany!=null && project.partyBCompany.id==currentCompanyId)){
$out+=' <span class="dp-block"> ';
if(c.projectProcessTimeEntityList.length>0 && c.projectProcessTimeEntityList[c.projectProcessTimeEntityList.length-1].startTime){
$out+=' ';
$out+=$escape(_momentFormat(c.projectProcessTimeEntityList[c.projectProcessTimeEntityList.length-1].startTime,'YYYY/MM/DD'));
$out+=' - ';
$out+=$escape(_momentFormat(c.projectProcessTimeEntityList[c.projectProcessTimeEntityList.length-1].endTime,'YYYY/MM/DD'));
$out+=' ';
}
$out+=' </span> ';
}
$out+=' </div> </span> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' ';
if(editFlag){
$out+=' <span class="design_item addDesignContentDiv v-middle text-center no-padding" > <a class="btn btn-outline btn-default" href="javascript:void(0)" data-action="addDesignContent" type="button"> <i class="fa fa-plus-square fc-v1-green"></i>&nbsp;添加设计任务 </a> </span> ';
}
$out+=' </li> </ul> </div> </td> </tr> </tbody> </table> </form> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_project/m_projectDesignContent_add','<style> div.m-projectDesignContent_add #time-box .inputTimeOBox.p-m{padding: 0} </style> <div class="content-box m-projectDesignContent_add" style="width: 440px;min-width: 440px;"> <form class="form designContentNameOBox overflow-hidden"> <div class="form-group m-b-xs"> <label class="dp-block">设计任务</label> <input placeholder="设计任务" class="form-control" type="text" name="designContentName" maxlength="100"> </div> </form> <div class="form-group" style="margin: 0 -10px;"> <div id="time-box"> </div> </div> <div class="form-group m-b-xs no-pd-right col-md-12"> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right"> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-r-xs"> <i class="fa fa-check"></i> </button>     </div> <div class="clearfix"></div> </div> ');/*v:1*/
template('m_project/m_projectMenu',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,id=$data.id,projectName=$data.projectName,$out='';$out+='<ul class="nav nav-second-level collapse in project-second-menu"> <li> <a id="basicInfo" href="#/projectDetails/basicInfo?id=';
$out+=$escape(id);
$out+='&projectName=';
$out+=$escape(projectName);
$out+='"> <span class="nav-label ">基本信息</span> </a> </li> <li> <a id="taskIssue" href="#/projectDetails/taskIssue?id=';
$out+=$escape(id);
$out+='&projectName=';
$out+=$escape(projectName);
$out+='"> <span class="nav-label ">任务签发</span> </a> </li> <li> <a id="productionArrangement" href="#/projectDetails/productionArrangement?id=';
$out+=$escape(id);
$out+='&projectName=';
$out+=$escape(projectName);
$out+='"> <span class="nav-label ">生产安排</span> </a> </li> <li> <a id="cost" href="#/projectDetails/cost?id=';
$out+=$escape(id);
$out+='&projectName=';
$out+=$escape(projectName);
$out+='"> <span class="nav-label ">收支管理</span> </a> </li> <li> <a id="projectDocumentLib" href="#/projectDetails/projectDocumentLib?id=';
$out+=$escape(id);
$out+='&projectName=';
$out+=$escape(projectName);
$out+='" > <span class="nav-label ">项目文档</span> </a> </li> <li> <a id="projectMember" href="#/projectDetails/projectMember?id=';
$out+=$escape(id);
$out+='&projectName=';
$out+=$escape(projectName);
$out+='"> <span class="nav-label ">项目成员</span> </a> </li> <li> <a id="externalCooperation" href="#/projectDetails/externalCooperation?id=';
$out+=$escape(id);
$out+='&projectName=';
$out+=$escape(projectName);
$out+='"> <span class="nav-label ">外部合作</span> </a> </li> </ul>';
return new String($out);
});/*v:1*/
template('m_projectAdd/m_addProject_addRange','<div class="col-md-4 checkbox-inline otherRangeBox m-b-xs m-l-none no-pd"> <div class="input-group input-group-sm"> <div class="i-checks input-group-addon no-border no-padding"> <input name="designRange" data-action="otherRange" type="checkbox" checked/> </div> <input type="text" name="designRangeName" class="form-control input-sm" style="width: 130px;margin-left:4px;" placeholder="请输入名称" maxlength="50"/> </div> </div>');/*v:1*/
template('m_projectAdd/m_designContent_template',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,contentList=$data.contentList,$each=$utils.$each,c=$data.c,ci=$data.ci,$escape=$utils.$escape,$out='';if(contentList && contentList.length>0){
$out+=' ';
$each(contentList,function(c,ci){
$out+=' <span class="design_item designContentDiv" data-i=""> <label class="i-checks dp-inline-block" style="margin-bottom: 0;"> <input name="designContent" class="checkbox dinline" data-id="';
$out+=$escape(c.id);
$out+='" type="checkbox" value="" data-name="';
$out+=$escape(c.name);
$out+='"/> </label> <div class="pull-left"> <span> ';
$out+=$escape(c.name);
$out+=' </span> <a href="javascript:void(0)" class="editable editable-click editable-empty dp-block" data-action="edit_setDesignContentTime" data-startTime="" data-endTime="">设置合同进度 </a> </div> </span> ';
});
$out+=' ';
}
return new String($out);
});/*v:1*/
template('m_projectAdd/m_designContent_time','<span data-action="timeSet" class="m-l-xs"> <a href="javascript:void(0)" class="editable editable-click editable-empty" data-action="edit_setDesignContentTime" data-startTime="" data-endTime>设置合同进度</a> </span>');/*v:1*/
template('m_projectAdd/m_partyA','<div class="m_partyA"> <div class="col-md-12 border-bottom" style="padding: 5px 0;"> <button type="button" class="btn btn-w-m btn-link" data-action="searchPartyA"> <i class="fa fa-search"></i> 在工商注册名中搜索 </button> </div> <div class="col-md-12"> <div class="fc-ccc m-t-xs ">搜索结果</div> <div class="" id="partyAList" style="min-height: 10px;max-height: 200px;overflow: auto;"> </div> </div> </div>');/*v:1*/
template('m_projectAdd/m_partyA_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,partyAList=$data.partyAList,$each=$utils.$each,p=$data.p,pi=$data.pi,$escape=$utils.$escape,$out='';if(partyAList!=null && partyAList.length>0){
$out+=' <ul class="dropdown-menu" style="display: block;position: relative;border: 0;box-shadow: none;width: 100%;top: 0px;left:-20px;"> ';
$each(partyAList,function(p,pi){
$out+=' <li> <a href="javascript:void(0);" data-action="selectPartyA" data-id="';
$out+=$escape(p.companyid);
$out+='"> ';
$out+=$escape(p.corpname);
$out+=' </a> </li> ';
});
$out+=' </ul> ';
}else{
$out+=' <span class="fc-ccc m-t-xs ">未找到相关单位，试试输入更详细关键字查询</span> ';
}
return new String($out);
});/*v:1*/
template('m_projectAdd/m_projectAdd',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,projectTypeList=$data.projectTypeList,p=$data.p,$index=$data.$index,$escape=$utils.$escape,$detailAddress=$data.$detailAddress,contentList=$data.contentList,c=$data.c,ci=$data.ci,$out='';$out+='<div class="ibox m_projectAdd"> <div class="ibox-title ibox-title-shadow secondary-menu-outbox"> <div class="row"> <div class="col-md-12" id="secondary-menu"> <div class="pull-left m-r-xl"> <h3 class="" >项目立项</h3> </div> </div> </div> </div> <div class="ibox-content ibox-content-shadow addProjectOBox"> <form role="form " class="projectAddForm m-t-md"> <div class="row form-group"> <label class="col-24-md-3 text-right m-t-sm" for="detailAddress">项目类型<span class="color-red">*</span>：</label> <div class="col-24-md-8"> <div class="input-group project-type"> <input type="text" class="form-control" name="projectType" value=""> <div class="input-group-btn"> <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" > <span class="caret"></span> </button> <ul class="dropdown-menu dropdown-menu-right" style="right: 0;left: inherit;"> ';
$each(projectTypeList,function(p,$index){
$out+=' <li><a href="javascript:void(0);">';
$out+=$escape(p.name);
$out+='</a></li> ';
});
$out+=' </ul> </div> </div> </div> </div> <div class="row form-group"> <label class="col-24-md-3 text-right m-t-sm">项目名称<span class="color-red">*</span>：</label> <div class="col-24-md-8"> <input type="text" class="form-control" name="projectName" id="projectName" maxlength="50" placeholder="请输入项目名称"> </div> </div> <div class="row form-group"> <label class="col-24-md-3 text-right m-t-sm">甲方<span class="color-red">*</span>：</label> <div class="col-24-md-8"> <input type="text" class="form-control constructCompanyName" name="constructCompanyName" id="constructCompanyName" placeholder="请输入甲方名称" autocomplete="off" data-id="">  <div class="partyA-select-box" style="position: absolute; top: 100%; left: 14px; z-index: 100; right: auto;background: #fff;border: solid 1px #ccc;border-radius: 5px;box-shadow: 1px 1px 6px 0 #9a9a9a;min-width: 80%; display: none;"> </div> </div> </div> <div class="row form-group"> <label class="col-24-md-3 text-right m-t-sm" for="selectRegion">项目所在地<span class="color-red">*</span>：</label></label> <div class="col-24-md-8"> <div class="input-group cityBox" id="selectRegion" name="selectRegion"> <div class="dp-inline-block"> <select class="prov form-control" name="province"></select> </div> <div class="dp-inline-block m-l-xs"> <select class="city form-control" name="city" disabled="disabled" style="display: none;"></select> </div> <div class="dp-inline-block m-l-xs"> <select class="dist form-control" name="county" disabled="disabled" style="display: none;"></select> </div> </div> </div> </div> <div class="row form-group"> <label class="col-24-md-3 text-right m-t-sm" for="detailAddress">详细地址：</label> <div class="col-24-md-8"> <input type="text" class="form-control" style="width: 100%;" name="detailAddress" id="detailAddress" maxlength="255" value="';
$out+=$escape($detailAddress?$detailAddress:'');
$out+='"> </div> </div> <!--<div class="row form-group designContentBox"> <label class="col-24-md-3 text-right m-t-sm">设计任务<span class="color-red">*</span>：</label> <div class="col-24-md-20"> <div class="input-group"> <ul class="todo-list small-list bg-white"> <li class="designContentLi" style="padding-top: 0px;"> ';
if(contentList && contentList.length>0){
$out+=' ';
$each(contentList,function(c,ci){
$out+=' <span class="design_item designContentDiv" data-i=""> <label class="i-checks dp-inline-block m-b-none"> <input name="designContent" class="checkbox dinline" type="checkbox" value="" data-name="';
$out+=$escape(c.name);
$out+='" data-id="';
$out+=$escape(c.id);
$out+='"/> </label> <div class="pull-left"> <span class="content-name" data-toggle="tooltip" data-original-title="';
$out+=$escape(c.name);
$out+='"> ';
$out+=$escape(c.name);
$out+=' </span> <a href="javascript:void(0)" class="editable editable-click editable-empty dp-block" data-action="edit_setDesignContentTime" data-startTime="" data-endTime="">设置合同进度 </a> </div> </span> ';
});
$out+=' ';
}
$out+=' </li> <li class="addDesignContentLi"> <span class="design_item addDesignContentDiv v-middle text-center no-padding"> <a class="btn btn-outline btn-default" href="javascript:void(0)" data-action="addDesignContent" type="button"> <i class="fa fa-plus-square " style="color: #1ab394"></i>&nbsp;添加设计任务 </a> </span> </li> </ul> </div> </div> </div>--> <div class="row"> <div class="col-md-11 col-md-offset-1 no-padding"> <a type="button" class="btn btn-primary btn-submit ">保存</a> <a type="button" class="btn btn-default btn-cancel m-l-xs">取消</a> </div> </div> </form> </div> </div>';
return new String($out);
});/*v:1*/
template('m_projectExternalCooperation/m_inviteCooperation',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,filePath=$data.filePath,$escape=$utils.$escape,_url=$helpers._url,companyName=$data.companyName,systemManager=$data.systemManager,cellphone=$data.cellphone,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="f-s-16 dp-inline-block l-h-16">外部合作邀请组织</span> </div> <div class="ibox-content text-center"> <div class="m-b-sm"> ';
if(filePath!='' && filePath!=null){
$out+=' <img alt="image" class="img-circle wth-100 h-100" src="';
$out+=$escape(filePath);
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle wth-100 h-100" src="';
$out+=$escape(_url('/assets/img/default/org_default_headPic.png'));
$out+='"> ';
}
$out+=' </div> <p class="f-s-18">';
$out+=$escape(companyName);
$out+='</p> <p class="f-s-14 fc-aaa">企业负责人:';
$out+=$escape(systemManager);
$out+='</p> </div> <div class="ibox-content"> <div class="row"> <div class="col-sm-12"> <form role="form"> <div class="form-group"> <label class="f-s-14 fw-400">您的手机号：</label> <p class="f-s-14 fc-dark-blue fw--600">';
$out+=$escape(cellphone);
$out+='</p> </div> <div class="form-group"> <label class="f-s14 fw-400">输入完整手机号校验身份：</label> <input name="cellphone" id=\'cellphone\' placeholder="手机号" class="form-control"></div> <div class="form-group"> <a class="btn btn-primary btn-block btn-ok">确定</a> </div> </form> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_projectExternalCooperation/m_inviteCooperation_create_has',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,a_companyName=$data.a_companyName,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="f-s-16 l-h-16 dp-inline-block">创建新组织</span> </div> <div class="ibox-content"> <p class="f-s-14 l-h-14 p-h-xs">该组织创建后将会自动成为&nbsp;<span class="fc-v1-yellow">';
$out+=$escape(a_companyName);
$out+='</span>&nbsp;的外部合作组织</p> <div class="row"> <div class="col-sm-12"> <form class="create-form"> <div class="form-group"> <input name="companyName" placeholder="组织名称" class="form-control"> </div> <div class="form-group pt-fixed" style="bottom: 0;left: 20px;right: 20px;"> <a class="btn btn-primary btn-lg btn-block btn-ok" data-action="createOrgSubmit">立即创建</a> </div> </form> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_projectExternalCooperation/m_inviteCooperation_create_hasNo',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,a_companyName=$data.a_companyName,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="f-s-16 l-h-16 dp-inline-block">创建账号和组织</span> </div> <div class="ibox-content"> <p class="f-s-14 l-h-14 p-h-xs">该组织创建后将会自动成为&nbsp;<span class="fc-v1-yellow">';
$out+=$escape(a_companyName);
$out+='</span>&nbsp;的外部合作组织</p> <div class="row"> <div class="col-sm-12"> <form class="create-form" role="form"> <div class="form-group"> <label class="f-s-14 fw-400 dp-block">个人信息：</label> <input name="userName" placeholder="姓名" class="form-control m-b-xs"> <input name="adminPassword" placeholder="密码" type="password" class="form-control m-b-xs"> <br /> <label class="f-s-14 fw-400 dp-block">组织信息：</label> <input name="companyName" placeholder="组织名称" class="form-control m-b-xs"> </div> <div class="form-group pt-fixed" style="bottom: 0;left: 20px;right: 20px;"> <a class="btn btn-primary btn-lg btn-block btn-ok" data-action="createOrgAndAccountSubmit">立即创建</a> </div> </form> </div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_projectExternalCooperation/m_inviteCooperation_org',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,a_companyName=$data.a_companyName,projectName=$data.projectName,$each=$utils.$each,companyList=$data.companyList,c=$data.c,i=$data.i,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-title border-fff"> <span class="f-s-16 l-h-16 dp-inline-block">组织选择</span> </div> <div class="ibox-content no-pd"> <ul class="todo-list small-list m-invite-group"> <li> <p class="f-s-14 l-h-14 m-t-sm"> 请选择&nbsp;<span class="fc-v1-yellow">已有的组织</span>&nbsp;或&nbsp; <span class="fc-v1-yellow">创建一个新组织</span>&nbsp;成为&nbsp; <span class="fc-v1-yellow">“';
$out+=$escape(a_companyName);
$out+='”</span>&nbsp;的 <span class="fc-v1-yellow">“';
$out+=$escape(projectName);
$out+='”</span>&nbsp;项目外部合作组织 </p> </li> ';
$each(companyList,function(c,i){
$out+=' ';
if(c.flag===1){
$out+=' <li class="unselectable" data-action="selectOrg" data-memo="';
$out+=$escape(c.memo);
$out+='"> <div class="ibox-content box-shadow border" style="padding: 10px 20px 5px;height:90px;"> <div class="dp-inline-block p-h-sm"> <p class="f-s-16 l-h-16 fc-aaa">';
$out+=$escape(c.companyName);
$out+='</p> <p class="f-s-12 l-h-12 fc-aaa">';
$out+=$escape(c.memo);
$out+='</p> </div> ';
if(c.filePath!=null && c.filePath!=''){
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(c.filePath);
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(_url('/assets/img/default/org_default_headPic.png'));
$out+='"> ';
}
$out+=' </div> </li> ';
}else{
$out+=' <li class="selectable" data-action="selectOrgApply" data-company-id="';
$out+=$escape(c.id);
$out+='" data-company-name="';
$out+=$escape(c.companyName);
$out+='"> <div class="ibox-content box-shadow border" style="padding: 10px 20px 5px;height:90px;"> <div class="dp-inline-block p-h-sm"> <p class="f-s-16 l-h-16">';
$out+=$escape(c.companyName);
$out+='</p> <p class="f-s-12 l-h-12 fc-aaa">企业负责人:';
$out+=$escape(c.systemManager);
$out+='</p> </div> ';
if(c.filePath!=null && c.filePath!=''){
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(c.filePath);
$out+='"> ';
}else{
$out+=' <img alt="image" class="img-circle pull-right img-responsive hidden-xs" width="90" src="';
$out+=$escape(_url('/assets/img/default/org_default_headPic.png'));
$out+='"> ';
}
$out+=' </div> </li> ';
}
$out+=' ';
});
$out+=' <li class="selectable"> <a href="javascript:void(0)"> <div class="ibox-content box-shadow border" style="padding: 0;"> <p class="f-s-16 l-h-16"> <a href="javascript:void(0)" data-action="createOrg" style="padding: 20px 20px 15px;display: inline-block;width: 100%;">创建新组织</a> </p> </div> </a> </li> </ul> </div> </div>';
return new String($out);
});/*v:1*/
template('m_projectExternalCooperation/m_inviteCooperation_org_hasNo',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-content text-center no-border no-pd-left no-pd-right"> <div class="m-b-md m-t-lg"> <img alt="image" class="img-circle wid-200" src="';
$out+=$escape(_url('/assets/img/default/defaultpage_pic_data.png'));
$out+='" > </div> <p class="f-s-18 m-t-md">你还没有组织，请先创建组织</p> <div class="form-group"> <a class="btn btn-primary btn-lg btn-ok" href="javascript:void(0)" data-action="createOrg">创建组织</a> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_projectExternalCooperation/m_inviteCooperation_success',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,companyName=$data.companyName,a_companyName=$data.a_companyName,$out='';$out+='<div class="ibox"> <div class="ibox-content text-center no-border no-pd-right no-pd-left"> <div class="m-b-sm"> <img alt="image" class="img-circle wth-100" src="';
$out+=$escape(_url('/assets/img/default/invite_success.png'));
$out+='" > </div> <p class="f-s-18">恭喜！</p> <p class="f-s-14 fc-aaa">';
$out+=$escape(companyName);
$out+=' 已经成为 ';
$out+=$escape(a_companyName);
$out+=' 的外部合作组织</p> </div> </div>';
return new String($out);
});/*v:1*/
template('m_projectExternalCooperation/m_inviteExternalCooperation','<div class="container-fluid inviteCorpBox"> <div class="row"> <div class="col-md-12" style="padding: 25px 15px 15px 15px"> <div class="form-group"> 请输入被邀请组织负责人的手机号： </div> <div class="inviteCorpPart form-group input-group"> <input name="bPartnerPhone" class="form-control"> <span class="input-group-btn"> <a class="btn btn-primary pull-right" data-action="sendMessage">发送邀请</a> </span> </div> </div> </div> </div>');/*v:1*/
template('m_projectExternalCooperation/m_projectExternalCooperation',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,isHasRoleOperate=$data.isHasRoleOperate,$each=$utils.$each,projectPartnerList=$data.projectPartnerList,p=$data.p,$index=$data.$index,_url=$helpers._url,$out='';$out+='<div class="ibox m_contractPayment"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 我的项目 </li> <li class=" fa fa-angle-right"> ';
$out+=$escape(projectName);
$out+=' </li> <li class="active fa fa-angle-right"> <strong>外部合作</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right "> ';
if(isHasRoleOperate==1){
$out+=' <a href="javascript:void(0)" class="btn btn-primary btn-xs pull-right m-t" data-action="inviteExternalCooperation" >邀请合作设计组织</a> ';
}
$out+=' </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <table class="table table-bordered"> <thead> <tr> <th class="text-align-center ">联系方式</th> <th class="text-align-center ">合作组织</th> <th class="text-align-center ">企业负责人</th> <th class="text-align-center ">合作内容</th> <th class="text-align-center ">经营负责人</th> <th class="text-align-center ">操作</th> </tr> </thead> <tbody class="border-no-t"> ';
$each(projectPartnerList,function(p,$index){
$out+=' <tr class="';
$out+=$escape(p.companyId!=null && p.companyId!=''?'':'gray-bg');
$out+='"> <td class="text-align-center">';
$out+=$escape(p.phone);
$out+='</td> <td class="text-align-center">';
$out+=$escape(p.companyName);
$out+='</td> <td class="text-align-center">';
$out+=$escape(p.companyManagerName);
$out+='</td> <td class="text-align-center">';
$out+=$escape(p.taskNameSplice);
$out+='</td> <td class="text-align-center">';
$out+=$escape(p.projectManagerName);
$out+='</td> <td class="text-align-center"> ';
if(p.companyId!=null && p.companyId!='' && isHasRoleOperate==1){
$out+=' <a href="javascript:void(0);" class="btn btn-primary btn-xs m-r-xs autoMargin" data-action="relieveRelationship" data-id="';
$out+=$escape(p.id);
$out+='">解除合作</a> ';
}
$out+=' ';
if(p.companyId==null || p.companyId==''){
$out+=' <span class="badge">未建立合作关系</span> ';
if(isHasRoleOperate==1){
$out+=' <a href="javascript:void(0);" class="btn btn-danger btn-xs pull-right m-r-xs autoMargin" data-action="resendSMS" data-id="';
$out+=$escape(p.id);
$out+='">重新推送短信</a> ';
}
$out+=' ';
}
$out+=' </td> </tr> ';
});
$out+=' ';
if(projectPartnerList==null || projectPartnerList.length==0){
$out+=' <tr class="no-data"> <td colspan="10" align="center"> <div class="text-center"> <img src="';
$out+=$escape(_url('/assets/img/default/without_exp.png'));
$out+='"> </div> <span style="color:#4765a0">暂无合作设计组织</span> </td> </tr> ';
}
$out+=' </tbody> </table> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_projectList/m_projectList','<div class="m_projectList"> <form role="form" class="form-inline m-md"> <div class="row"> <div class="col-md-6"> <div class="input-group"> <input class="form-control" type="text" name="keyword" placeholder="请输入关键字" style="width: 250px;"> <span class="input-group-btn"> <button type="button" class="btn btn-primary" data-action="searchProject"> <i class="fa fa-search"></i> </button> </span> </div> </div> <div class="col-md-6 text-right"> <button type="button" class="btn btn-primary" data-action="selectColumn" id="selectColumn"> <span><i class="fa fa-list"></i> <span class="caret m-t-n-xs"></span></span> </button> </div> </div> </form> <div class="data-list-box"> <div class="row"> <div class="col-md-12 data-list-container p-w-lg"></div> <div class="col-md-12 p-w-m"> <div id="data-pagination-container" class="m-pagination pull-right "></div> </div> </div> </div> </div>');/*v:1*/
template('m_projectList/m_projectList_content',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,columnCodes=$data.columnCodes,projectList=$data.projectList,$each=$utils.$each,p=$data.p,i=$data.i,$escape=$utils.$escape,_momentFormat=$helpers._momentFormat,columnLen=$data.columnLen,$out='';$out+='<div class="project-list of-auto"> <table class="table table-bordered table-hover cell-border dataTable" > <thead> <th>项目编号</th> <th>项目名称</th> <th> 立项组织/立项人 <a class="icon-filter pull-right" id="filterCreateBy" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
if(columnCodes.indexOf('projectCreateDate')>-1){
$out+=' <th class="sorting_desc" data-action="sort" data-sort-type="createDate"> 立项时间 <a class="icon-filter pull-right" id="filterCreateDate" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' ';
if(columnCodes.indexOf('signDate')>-1){
$out+=' <th class="sorting" data-action="sort" data-sort-type="signDate"> 合同签订 <a class="icon-filter pull-right" id="filterSignDate" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' ';
if(columnCodes.indexOf('status')>-1){
$out+=' <th> 项目状态 <a class="icon-filter pull-right" id="filterStatus" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' ';
if(columnCodes.indexOf('buildName')>-1){
$out+=' <th> 功能分类 <a class="icon-filter pull-right" id="filterBuildName" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' ';
if(columnCodes.indexOf('address')>-1){
$out+=' <th> 地点 <a class="icon-filter pull-right" id="filterAddress" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' ';
if(columnCodes.indexOf('partyA')>-1){
$out+=' <th> 甲方 <a class="icon-filter pull-right" id="filterPartyA" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' ';
if(columnCodes.indexOf('partyB')>-1){
$out+=' <th> 乙方 <a class="icon-filter pull-right" id="filterPartyB" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' ';
if(columnCodes.indexOf('designCompanyName')>-1){
$out+=' <th> 合作组织 <a class="icon-filter pull-right" id="filterDesignCompanyName" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' <!--';
if(columnCodes.indexOf('busPersonInCharge')>-1){
$out+=' <th> 经营负责人 <a class="icon-filter pull-right" id="filterBusPersonInCharge" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' ';
if(columnCodes.indexOf('busPersonInChargeAssistant')>-1){
$out+=' <th style="min-width: 130px;"> 经营助理 <a class="icon-filter pull-right" id="filterBusAss" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' ';
if(columnCodes.indexOf('designPersonInCharge')>-1){
$out+=' <th> 设计负责人 <a class="icon-filter pull-right" id="filterDesignPersonInCharge" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+=' ';
if(columnCodes.indexOf('designPersonInChargeAssistant')>-1){
$out+=' <th style="min-width: 130px;"> 设计助理 <a class="icon-filter pull-right" id="filterDesignAss" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> ';
}
$out+='--> </thead> <tbody> ';
if(projectList&&projectList.length>0){
$out+=' ';
$each(projectList,function(p,i){
$out+=' <tr> <td > ';
$out+=$escape(p.projectNo==null?'--':p.projectNo);
$out+=' </td> <td > <a href="javascript:void(0)" data-action="gotoProject" data-pId="';
$out+=$escape(p.id);
$out+='" data-pName="';
$out+=$escape(p.projectName);
$out+='" style="font-size: 14px;"> ';
$out+=$escape(p.projectName);
$out+=' </a> </td> <td > ';
$out+=$escape(p.companyName);
$out+='/';
$out+=$escape(p.createBy);
$out+=' </td> ';
if(columnCodes.indexOf('projectCreateDate')>-1){
$out+=' <td >';
$out+=$escape(_momentFormat(p.projectCreateDate,'YYYY/MM/DD'));
$out+='</td> ';
}
$out+=' ';
if(columnCodes.indexOf('signDate')>-1){
$out+=' <td >';
$out+=$escape(p.signDate==null?'--':_momentFormat(p.signDate,'YYYY/MM/DD'));
$out+='</td> ';
}
$out+=' ';
if(columnCodes.indexOf('status')>-1){
$out+=' <td > ';
if(p.status == 0){
$out+=' 进行中 ';
}else if(p.status == 2 ){
$out+=' 已完成-未结清 ';
}else if(p.status == 4 ){
$out+=' 已完成-已结清 ';
}else if(p.status == 1){
$out+=' 已暂停-未结清 ';
}else if(p.status == 5){
$out+=' 已暂停-已结清 ';
}else if(p.status == 3 ){
$out+=' 已终止-未结清 ';
}else if(p.status == 6 ){
$out+=' 已终止-已结清 ';
}else{
$out+=' -- ';
}
$out+=' </td> ';
}
$out+=' ';
if(columnCodes.indexOf('buildName')>-1){
$out+=' <td >';
$out+=$escape(p.buildName==null?'--':p.buildName);
$out+='</td> ';
}
$out+=' ';
if(columnCodes.indexOf('address')>-1){
$out+=' <td >';
$out+=$escape(p.address==null?'--':p.address);
$out+='</td> ';
}
$out+=' ';
if(columnCodes.indexOf('partyA')>-1){
$out+=' <td >';
$out+=$escape(p.partyA==null?'--':p.partyA);
$out+='</td> ';
}
$out+=' ';
if(columnCodes.indexOf('partyB')>-1){
$out+=' <td >';
$out+=$escape(p.partyB==null?'--':p.partyB);
$out+='</td> ';
}
$out+=' ';
if(columnCodes.indexOf('designCompanyName')>-1){
$out+=' <td >';
$out+=$escape(p.designCompanyName==null?'--':p.designCompanyName);
$out+='</td> ';
}
$out+=' <!--';
if(columnCodes.indexOf('busPersonInCharge')>-1){
$out+=' <td >';
$out+=$escape(p.busPersonInCharge==null?'&#45;&#45;':p.busPersonInCharge);
$out+='</td> ';
}
$out+=' ';
if(columnCodes.indexOf('busPersonInChargeAssistant')>-1){
$out+=' <td >';
$out+=$escape(p.busPersonInChargeAssistant==null?'&#45;&#45;':p.busPersonInChargeAssistant);
$out+='</td> ';
}
$out+=' ';
if(columnCodes.indexOf('designPersonInCharge')>-1){
$out+=' <td >';
$out+=$escape(p.designPersonInCharge==null?'&#45;&#45;':p.designPersonInCharge);
$out+='</td> ';
}
$out+=' ';
if(columnCodes.indexOf('designPersonInChargeAssistant')>-1){
$out+=' <td >';
$out+=$escape(p.designPersonInChargeAssistant==null?'&#45;&#45;':p.designPersonInChargeAssistant);
$out+='</td> ';
}
$out+='--> </tr> ';
});
$out+=' ';
}else{
$out+=' <tr> <td colspan="';
$out+=$escape(3+columnLen);
$out+='" align="center">暂无数据</td> </tr> ';
}
$out+=' </tbody> </table> </div> ';
return new String($out);
});/*v:1*/
template('m_projectList/m_projectList_empty','<div class="ibox ibox-shadow"> <div class="ibox-content" style="padding: 150px 20px 250px;"> <div class="projectList-content"> <div class="row"> <div class="col-lg-12"> <div class="text-center m-t-lg"> <h1> 你的组织还没建立任何项目 </h1> <p style="margin-top:16px;"> 点击 <button href="javascript:void(0)" class="btn btn-primary btn-xs m-b-xs" data-action="addProject">开始立项</button> 为你的组织建立第一个项目 </p> </div> </div> </div> </div> </div> </div>');/*v:1*/
template('m_projectList/m_projectList_filter_buildType',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,buildTypeArr=$data.buildTypeArr,d=$data.d,$index=$data.$index,currBuildType=$data.currBuildType,$escape=$utils.$escape,$out='';$out+='<div class="data-list-filter p-sm" > <div class="m-b-sm" style="border-bottom: solid 1px #f2f2f2;"> <label class="i-checks fw-normal"> <input name="allBuildType" type="checkbox" value="0"/> <span class="i-checks-span">全选</span> </label> </div> <div> ';
$each(buildTypeArr,function(d,$index){
$out+=' <div class="col-md-4 no-pd-right no-pd-left"> <label class="i-checks fw-normal"> ';
if(currBuildType.indexOf(d.name)>-1){
$out+=' <input name="buildType" type="checkbox" checked value="';
$out+=$escape(d.name);
$out+='"/> ';
}else{
$out+=' <input name="buildType" type="checkbox" value="';
$out+=$escape(d.name);
$out+='"/> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(d.name);
$out+='</span> </label> </div> ';
});
$out+=' <div class="clearfix"></div> </div> <div class="m-t-xs"> <button type="button" class="btn btn-primary btn-xs rounded pull-right" data-action="confirm">确定</button> <button type="button" class="btn btn-default btn-xs rounded pull-right m-r-xs" data-action="cancel">取消</button> <div class="clearfix"></div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_projectList/m_projectList_filter_orgName',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,orgNameList=$data.orgNameList,d=$data.d,$index=$data.$index,$escape=$utils.$escape,currCheck=$data.currCheck,$out='';$out+='<div class="data-list-filter"> <ul class="dropdown-menu"> ';
$each(orgNameList,function(d,$index){
$out+=' <li> <a data-org-name="';
$out+=$escape(d.name);
$out+='"> <span class="check"> ';
if(d.name == currCheck){
$out+=' <i class="fa fa-check"></i> ';
}
$out+=' </span> ';
$out+=$escape(d.name);
$out+=' </a> </li> ';
});
$out+=' </ul> </div>';
return new String($out);
});/*v:1*/
template('m_projectList/m_projectList_filter_state',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,currStateCheck=$data.currStateCheck,$out='';$out+='<div class="data-list-filter"> <ul class="dropdown-menu"> <li> <a data-state-name="全部"> <span class="check"> ';
if("全部" == currStateCheck){
$out+=' <i class="fa fa-check"></i> ';
}
$out+=' </span> 全部 </a> </li> <li> <a data-state-name="进行中"> <span class="check"> ';
if("进行中" == currStateCheck){
$out+=' <i class="fa fa-check"></i> ';
}
$out+=' </span> 进行中 </a> </li> <li> <a data-state-name="已暂停"> <span class="check"> ';
if("已暂停" == currStateCheck){
$out+=' <i class="fa fa-check"></i> ';
}
$out+=' </span> 已暂停 </a> </li> <li> <a data-state-name="已完成"> <span class="check"> ';
if("已完成" == currStateCheck){
$out+=' <i class="fa fa-check"></i> ';
}
$out+=' </span> 已完成 </a> </li> <li> <a data-state-name="已终止"> <span class="check"> ';
if("已终止" == currStateCheck){
$out+=' <i class="fa fa-check"></i> ';
}
$out+=' </span> 已终止 </a> </li> </ul> </div>';
return new String($out);
});/*v:1*/
template('m_projectList/m_projectList_filter_time',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,currTimes=$data.currTimes,$out='';$out+='<div class="data-list-filter p-xs"> <div> <div class="form-group"> <div class="input-group"> <input type="text" class="form-control input-sm" id="ipt_startTime" name="startTime" placeholder="开始时间" readonly onFocus="WdatePicker({maxDate:\'#F{$dp.$D(\\\'ipt_endTime\\\')}\'})" value="';
$out+=$escape(currTimes.startTime);
$out+='"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </div> <div class="form-group m-t-xs"> <div class="input-group "> <input type="text" class="form-control input-sm" id="ipt_endTime" name="endTime" placeholder="结束时间" readonly onFocus="WdatePicker({minDate:\'#F{$dp.$D(\\\'ipt_startTime\\\')}\'})" value="';
$out+=$escape(currTimes.endTime);
$out+='"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </div> <div class="m-t-xs"> <button type="button" class="btn btn-primary btn-xs rounded pull-right" data-action="sureTimeFilter">确定</button> <button type="button" class="btn btn-default btn-xs rounded pull-right m-r-xs" data-action="clearTimeInput">清空</button> <div class="clearfix"></div> </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_projectList/m_projectList_headTools','<div class="col-sm-7 btn-box" style="padding-left: 0;margin-bottom: 20px;"> <button type="button" class="btn btn-primary" data-action="addProject">项目立项</button> <div class="btn-group margin-left-5" data-toggle="buttons"> <label class="btn btn-white active" data-action="allProject"><input type="radio" id="option1" name="allProject">所有项目</label> <label class="btn btn-white " data-action="myProject"><input type="radio" id="option2" name="myProject">我的项目</label> <label class="btn btn-white " data-action="concernedProject"><input type="radio" id="option3" name="concernedProject">我关注的项目</label> <a class="btn btn-white" title="刷新数据" data-action="refreshProjectList"><i class="fa fa-refresh"></i>刷新数据</a> </div> </div>');/*v:1*/
template('m_projectList/m_projectList_leftNav',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,projectTypes=$data.projectTypes,$each=$utils.$each,t=$data.t,i=$data.i,$escape=$utils.$escape,$out='';$out+='<ul> <li id="all_project" data-action="all_project">全部项目</li> <li data-action="my_project">我的项目</li> <li class="jstree-open" data-jstree=\'{"disabled":true}\'>按项目状态筛选 <ul>  <li data-action="filter_status" data-status="0">进行中</li> <li data-action="filter_status" data-status="2">已完成</li> </ul> </li> ';
if(projectTypes&&projectTypes.length>0){
$out+=' <li class="jstree-open" data-jstree=\'{"disabled":true}\'>按功能分类筛选 <ul> ';
$each(projectTypes,function(t,i){
$out+=' <li data-action="filter_projectType" data-type="';
$out+=$escape(t.id);
$out+='">';
$out+=$escape(t.name);
$out+='</li> ';
});
$out+=' </ul> </li> ';
}
$out+=' </ul> ';
return new String($out);
});/*v:1*/
template('m_projectList/m_projectList_menu','<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 我的项目 </li> <li class="active fa fa-angle-right"> <strong>我的项目</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> <ul class="secondary-menu-ul pull-right" style="display: none;"> <li class="active" id="myProjectList" ><a>我的项目</a></li> <li id="projectOverview" ><a>项目总览</a></li> </ul> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content " id="content-box"> </div> </div>');/*v:1*/
template('m_projectList/m_projectList_old','<div class="m_projectList"> <form role="form" class="form-inline m-md"> <div class="input-group"> <input class="form-control" type="text" name="keyword" placeholder="请输入关键字" style="width: 250px;"> <span class="input-group-btn"> <button type="button" class="btn btn-primary" data-action="searchProject"> <i class="fa fa-search"></i> </button> </span> </div> <button type="button" class="btn btn-primary" data-action="selectColumn" id="selectColumn"> <span><i class="fa fa-list"></i> <span class="caret m-t-n-xs"></span></span> </button> </form> <div class="data-list-box"> <div class="row"> <div class="col-md-12 data-list-container p-w-lg"></div> <div class="col-md-12 p-w-m"> <div id="data-pagination-container" class="m-pagination pull-right "></div> </div> </div> </div> </div>');/*v:1*/
template('m_projectList/m_projectList_row',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,p=$data.p,_url=$helpers._url,_isNullOrBlank=$helpers._isNullOrBlank,$each=$utils.$each,c=$data.c,ci=$data.ci,$out='';$out+='<td class="text-center"> 111 </td> <td class="text-center" > <a href="javascript:void(0)" data-action="gotoProject" data-pId="';
$out+=$escape(p.id);
$out+='" data-pName="';
$out+=$escape(p.projectName);
$out+='" data-pUrl="';
$out+=$escape(_url('/iWork/project/projectInformation/'+p.id+'/1'));
$out+='" style="font-size: 17px;">';
$out+=$escape(p.projectName);
$out+=' </a> <!--<p class="pInfo"> 立项人：<span class="person">';
$out+=$escape(p.createBy);
$out+='</span>&nbsp;&nbsp; 经营负责人：<span class="person">';
$out+=$escape(p.busPersonInCharge);
$out+='</span>&nbsp;&nbsp; ';
if(!_isNullOrBlank(p.designPersonInCharge)){
$out+=' 设计负责人：<span class="person">';
$out+=$escape(p.designPersonInCharge);
$out+='</span> ';
}
$out+=' </p>--> </td> <td class="project-stage text-center no-padding"> <!--';
if(p.designContentList!=null && p.designContentList.length>0){
$out+=' ';
$out+=$escape(p.designContentList[0].contentName);
$out+=' ';
}
$out+='--> ';
if(p.designContentList!=null && p.designContentList.length>1){
$out+=' ';
$each(p.designContentList,function(c,ci){
$out+=' ';
if(ci == p.designContentList.length-1){
$out+=' <div class="div-td-last">';
$out+=$escape(c.contentName);
$out+='</div> ';
}else{
$out+=' <div class="div-td">';
$out+=$escape(c.contentName);
$out+='</div> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' </td> <td class="text-center no-padding"> <!--';
if(p.designContentList[0].planStartTime!=null && p.designContentList[0].planEndTime!=null){
$out+=' ';
$out+=$escape(p.designContentList[0].planStartTime);
$out+=' ~ ';
$out+=$escape(p.designContentList[0].planEndTime);
$out+=' ';
}
$out+='--> ';
if(p.designContentList!=null && p.designContentList.length>1){
$out+=' ';
$each(p.designContentList,function(c,ci){
$out+=' ';
if(ci == p.designContentList.length-1){
$out+=' <div class="div-td-last"> ';
if(c.planStartTime!=null && c.planEndTime!=null){
$out+=' ';
$out+=$escape(c.planStartTime);
$out+=' ~ ';
$out+=$escape(c.planEndTime);
$out+=' ';
}else{
$out+=' &nbsp; ';
}
$out+=' </div> ';
}else{
$out+=' <div class="div-td"> ';
if(c.planStartTime!=null && c.planEndTime!=null){
$out+=' ';
$out+=$escape(c.planStartTime);
$out+=' ~ ';
$out+=$escape(c.planEndTime);
$out+=' ';
}else{
$out+=' &nbsp; ';
}
$out+=' </div> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' </td>  ';
if((!_isNullOrBlank(p.designContentList[0].statusText)&&p.designContentList[0].statusText.indexOf('超时')>-1)){
$out+=' <td class="text-center no-padding"> <small class="label2 bg-v1-red">';
$out+=$escape(p.designContentList[0].statusText);
$out+=' </small> </td> ';
}else if((!_isNullOrBlank(p.designContentList[0].statusText)&&p.designContentList[0].statusText.indexOf('剩余')>-1)){
$out+=' <td class="text-center no-padding"> <small class="label2 bg-v1-yellow">';
$out+=$escape(p.designContentList[0].statusText);
$out+=' </small> </td> ';
}else if((!_isNullOrBlank(p.designContentList[0].statusText)&&p.designContentList[0].statusText.indexOf('进行中')>-1)){
$out+=' <td class="text-center no-padding"> <small class="label2 bg-v2-blue">';
$out+=$escape(p.designContentList[0].statusText);
$out+='</small> </td> ';
}else if((!_isNullOrBlank(p.designContentList[0].statusText)&&p.designContentList[0].statusText.indexOf('未开始')>-1)){
$out+=' <td class="text-center no-padding"> <small class="label2 bg-v1-grey">';
$out+=$escape(p.designContentList[0].statusText);
$out+='</small> </td> ';
}else if((!_isNullOrBlank(p.designContentList[0].statusText)&&p.designContentList[0].statusText.indexOf('未发布')>-1)){
$out+=' <td class="text-center no-padding"> <small class="label2 bg-v1-grey">';
$out+=$escape(p.designContentList[0].statusText);
$out+='</small> </td> ';
}else if((!_isNullOrBlank(p.designContentList[0].statusText)&&p.designContentList[0].statusText.indexOf('已完成')>-1)){
$out+=' <td class="text-center no-padding"> <small class="label2 bg-v1-green">';
$out+=$escape(p.designContentList[0].statusText);
$out+='</small> </td> ';
}else if(_isNullOrBlank(p.designContentList[0].statusText)){
$out+=' <td class="text-center no-padding"> <small class="label2 bg-v1-grey">未设置进度</small> </td> ';
}else{
$out+=' <td class="text-center"> ';
$out+=$escape(p.designContentList[0].statusText);
$out+=' </td> ';
}
$out+=' <td class="text-center">';
$out+=$escape(p.companyName);
$out+='</td> <td class="text-center">';
$out+=$escape(p.createDate);
$out+='</td> <td class="text-center">';
$out+=$escape(p.createBy);
$out+='</td> <td class="text-center">';
$out+=$escape(p.busPersonInCharge);
$out+='</td> <td class="text-center">';
$out+=$escape(p.designPersonInCharge);
$out+='</td>';
return new String($out);
});/*v:1*/
template('m_projectList/m_projectList_selectColumn',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,columnArr=$data.columnArr,d=$data.d,i=$data.i,$escape=$utils.$escape,columnCodes=$data.columnCodes,$out='';$out+='<div class="select-column"> <ul class="dropdown-menu" style="display: block;position: relative;width: 100%;margin: 0;"> ';
$each(columnArr,function(d,i){
$out+=' ';
if(i>2){
$out+=' <li> <a data-code="';
$out+=$escape(d.code);
$out+='" class="';
$out+=$escape(columnCodes.indexOf(d.code)>-1?'active':'');
$out+='"> ';
$out+=$escape(d.name);
$out+=' </a> </li> ';
}
$out+=' ';
});
$out+=' </ul> </div> ';
return new String($out);
});/*v:1*/
template('m_projectMember/m_projectMember',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,$each=$utils.$each,parts=$data.parts,p=$data.p,$index=$data.$index,_url=$helpers._url,$out='';$out+='<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 我的项目 </li> <li class=" fa fa-angle-right"> ';
$out+=$escape(projectName);
$out+=' </li> <li class="active fa fa-angle-right"> <strong>项目成员</strong> </li> </ol> </div> </div> </div> </div> <div class="ibox-content no-padding"> <table class="table table-bordered"> <thead> <tr> <th class="border-no-l">姓名</th> <th >角色</th> <th >组织</th> <th >联系电话</th> <th >邮箱</th> </tr> </thead> <tbody class="border-no-t"> ';
$each(parts,function(p,$index){
$out+=' <tr class=""> <td class="border-no-l v-middle"> ';
if(p.headImg==null || p.headImg==''){
$out+=' <img alt="image" class="img-circle img-responsive dp-inline-block" src="';
$out+=$escape(_url('/assets/img/head_default.png'));
$out+='" width="30"/> ';
}else{
$out+=' <img alt="image" class="img-circle img-responsive dp-inline-block" src="';
$out+=$escape(p.headImg);
$out+='" width="30"/> ';
}
$out+=' &nbsp;';
$out+=$escape(p.companyUserName);
$out+=' </td> <td class="v-middle">';
$out+=$escape(p.nodeName);
$out+='</td> <td class="v-middle">';
$out+=$escape(p.companyName);
$out+='</td> <td class="v-middle">';
$out+=$escape(p.cellphone);
$out+='</td> <td class="v-middle">';
$out+=$escape(p.email);
$out+='</td> </tr> ';
});
$out+=' </tbody> </table> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_projectMember/m_projectMember_old',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,parts=$data.parts,p=$data.p,i=$data.i,$escape=$utils.$escape,n=$data.n,j=$data.j,u=$data.u,k=$data.k,currentCompanyId=$data.currentCompanyId,currentCompanyUserId=$data.currentCompanyUserId,currentUserId=$data.currentUserId,isOrgManager=$data.isOrgManager,$out='';$out+='<div class="ibox ibox-shadow"> <div class="ibox-content"> ';
$each(parts,function(p,i){
$out+=' <div class="headline"> <h3>';
$out+=$escape(p.companyName);
$out+='</h3> </div> <div class="margin-bottom-30"> <ul class="todo-list m-t small-list"> ';
$each(p.nodeList,function(n,j){
$out+=' <li> <span class="m-l-xsd"><strong>';
$out+=$escape(n.nodeName);
$out+='：</strong>&nbsp;&nbsp;&nbsp; ';
$each(n.userList,function(u,k){
$out+=' ';
if((n.nodeName==='经营负责人')&& ( p.companyId == currentCompanyId &&
                            (p.operatorManagerId===currentCompanyUserId || (currentUserId==p.projectCreateBy && p.projectCompanyId==currentCompanyId) || isOrgManager==1))){
$out+=' <a href="javascript:void(0)" data-companyId="';
$out+=$escape(p.companyId);
$out+='" data-action="changeOperatorPerson" class="editable editable-click">';
$out+=$escape(u.userName);
$out+='</a>&nbsp;&nbsp; ';
}else if((n.nodeName==='设计负责人')&&p.operatorManagerId===currentCompanyUserId){
$out+=' <a href="javascript:void(0)" data-companyId="';
$out+=$escape(p.companyId);
$out+='" data-action="changeManagerPerson" data-id="';
$out+=$escape(u.companyUserId);
$out+='" class="editable editable-click">';
$out+=$escape(u.userName);
$out+='</a>&nbsp;&nbsp; ';
}else{
$out+=' ';
$out+=$escape(u.userName);
$out+='&nbsp;&nbsp; ';
}
$out+=' ';
});
$out+=' </span> </li> ';
});
$out+=' </ul> </div> ';
});
$out+=' </div> </div>';
return new String($out);
});/*v:1*/
template('m_role/m_changeManager',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,adminInfo=$data.adminInfo,type=$data.type,$out='';$out+='<div class="ibox"> <div class="ibox-content"> <form class="sky-form ensureChangeAdministraterOBox rounded-4x"> <input type="password" class="dp-none"> <div class="space-md-hor"> <!--<div class="col-md-12 no-pd-left"> <div class="form-group col-md-6 "style="margin-bottom: 0"> <label>新的管理员： <b>';
$out+=$escape(adminInfo.userName);
$out+='</b></label> </div> </div> <div class="form-group col-md-12"> <input type="password" class="form-control password" name="adminPassword" data-action="adminPassword" placeholder="请输入当前账户密码"> </div>--> ';
if(type==1){
$out+=' <span>移交系统管理员给<span class="text-navy font-bold">“';
$out+=$escape(adminInfo.userName);
$out+='”</span>后，你将不在拥有系统管理员权限，是否选择移交</span> ';
}else if(type==2){
$out+=' <span>指定<span class="text-navy font-bold">“';
$out+=$escape(adminInfo.userName);
$out+='”</span>为系统管理员后，系统管理员将拥有系统管理员所有权限，是否进行指定。</span> ';
}else if(type==3){
$out+=' <span>移交企业负责人给<span class="text-navy font-bold">“';
$out+=$escape(adminInfo.userName);
$out+='”</span>后，你将不在拥有企业负责人权限，是否选择移交</span> ';
}else if(type==4){
$out+=' <span>指定<span class="text-navy font-bold">“';
$out+=$escape(adminInfo.userName);
$out+='”</span>为企业负责人后，企业负责人将拥有企业负责人所有权限，是否进行指定。</span> ';
}
$out+=' <div class="clearfix"></div> </div> </form> </div> </div>';
return new String($out);
});/*v:1*/
template('m_role/m_changeManagerPWD','<form class="sky-form rounded-4x changeAdminPWDOBox dp-none border-none"> <fieldset> <div class="row"> <div class="col-md-12"> <div class="out-box"> <label class="label">旧密码<span class="color-red">*</span></label> <label class="input"> <input type="password" class="oldPassword" name="oldPassword"> </label> </div> <div class="out-box"> <label class="label">新密码<span class="color-red">*</span></label> <label class="input"> <input type="password" id="newPassword" name="newPassword"> </label> </div> <div class="out-box"> <label class="label">确认新密码<span class="color-red">*</span></label> <label class="input"> <input type="password" class="changeAdminRePwd" name="changeAdminRePwd"> </label> </div> </div> </div> </fieldset> </form>');/*v:1*/
template('m_role/m_roleAuthorization',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,rolePermissions=$data.rolePermissions,role=$data.role,i=$data.i,$escape=$utils.$escape,mem=$data.mem,a=$data.a,$out='';$out+='<form class="sky-form rounded-4x m-roleAuthorization rolePermissionsConfigOBox border-none"> <fieldset style="max-height: 600px;overflow: auto;"> <div class="ibox"> <div class="ibox-content"> <table class="table table-bordered"> <thead style="background: #f5f5f5;"> <tr> <th width="20%" class="text-center">权限组</th> <th width="50%" class="text-center">操作权限</th> <th width="30%" class="text-center"> <a href="javascript:void(0)" class="btn btn-link btn-xs" data-action="chooseAll">全选</a> <a href="javascript:void(0)" class="btn btn-link btn-xs" data-action="chooseNothing">全不选</a> <a href="javascript:void(0)" class="btn btn-link btn-xs" data-action="recoveryChoice">恢复原始</a> </th> </tr> </thead> ';
$each(rolePermissions,function(role,i){
$out+=' <tbody p-code="';
$out+=$escape(role.code);
$out+='"> <tr style="background-clip: padding-box;" role-code="';
$out+=$escape(role.code);
$out+='"> <td rowspan="';
$out+=$escape(role.permissionList.length);
$out+='" id="';
$out+=$escape(role.id);
$out+='" class="text-center vmiddle v_a_middle pTarget"> <span class="dp-block pt-relative"> ';
$out+=$escape(role.name);
$out+=' ';
if(role.code != 'SystemManager'){
$out+=' <span class="dp-inline-block"> <a href="javascript:void(0)" class="btn btn-link btn-xs" data-action="chooseAllRoleItem">全选</a> <a href="javascript:void(0)" class="btn btn-link btn-xs" data-action="delAllRoleItem">全不选</a> </span> ';
}
$out+=' </span> </td> <td class="vmiddle" p-code="';
$out+=$escape(role.permissionList[0].code);
$out+='" id="';
$out+=$escape(role.permissionList[0].id);
$out+='"> <span class="dp-block pt-relative"> ';
$out+=$escape(role.permissionList[0].name);
$out+=' </span> </td> ';
if(role.permissionList[0].code != 'sys_enterprise_logout'){
$out+=' <td data-action="chooseUserPermission" class="v-middle text-align-center"> <div class="i-checks checkbox-inline" style="min-height: 17px;"> <input type="checkbox" id="inlineCheckbox';
$out+=$escape(role.permissionList[0].id);
$out+='" name="userPermission" permission-id="';
$out+=$escape(role.permissionList[0].id);
$out+='" isCheck="';
$out+=$escape(role.permissionList[0].type);
$out+='"/> <label for="inlineCheckbox';
$out+=$escape(role.permissionList[0].id);
$out+='"></label> </div> </td> ';
}else{
$out+=' <td class="text-center"> ';
if(role.permissionList[0].type==1){
$out+=' <span class="fa fa-check p-r-5" permission-id="';
$out+=$escape(role.permissionList[0].id);
$out+='"></span> ';
}
$out+=' ';
if(role.permissionList[0].type!=1){
$out+=' <span class="glyphicon glyphicon-remove p-r-5"></span> ';
}
$out+=' </td> ';
}
$out+=' </tr> ';
$each(role.permissionList,function(mem,a){
$out+=' ';
if(a>0){
$out+=' <tr style="background-clip: padding-box;"> <td class="vmiddle" p-code="';
$out+=$escape(mem.code);
$out+='" id="';
$out+=$escape(mem.id);
$out+='"> <span class="dp-block pt-relative"> ';
$out+=$escape(mem.name);
$out+=' </span> </td> ';
if(mem.code != 'sys_enterprise_logout'){
$out+=' <td data-action="chooseUserPermission" class="v-middle text-align-center" isCheck="';
$out+=$escape(mem.type);
$out+='"> <div class="i-checks checkbox-inline" style="min-height: 17px;"> <input type="checkbox" id="inlineCheckbox';
$out+=$escape(mem.id);
$out+='" name="userPermission" permission-id="';
$out+=$escape(mem.id);
$out+='" isCheck="';
$out+=$escape(mem.type);
$out+='"/> <label for="inlineCheckbox';
$out+=$escape(mem.id);
$out+='"></label> </div> </td> ';
}else{
$out+=' <td class="text-center"> ';
if(mem.type==1){
$out+=' <span class="fa fa-check p-r-5" permission-id="';
$out+=$escape(mem.id);
$out+='"></span> ';
}
$out+=' ';
if(mem.type!=1){
$out+=' <span class="glyphicon glyphicon-remove p-r-5"></span> ';
}
$out+=' </td> ';
}
$out+=' </tr> ';
}
$out+=' ';
});
$out+=' </tbody> ';
});
$out+=' </table> </div> </div> </fieldset> </form> <script> $(\'.i-checks\').iCheck({ checkboxClass: \'icheckbox_minimal-green\', radioClass: \'iradio_minimal-green\' }); </script> ';
return new String($out);
});/*v:1*/
template('m_role/m_roleList',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,rolePermissions=$data.rolePermissions,currentCompanyUserId=$data.currentCompanyUserId,$escape=$utils.$escape,$each=$utils.$each,role=$data.role,i=$data.i,user=$data.user,j=$data.j,mem=$data.mem,a=$data.a,isDialogShow=$data.isDialogShow,b=$data.b,$out='';$out+=' <div class="no-margin p-sm breadcrumb-box" > <div class="col-md-6 text-left f-s-14 p-w-md"> <span>系统管理员：</span> ';
if(rolePermissions.systemManager!=null && rolePermissions.systemManager.id==currentCompanyUserId){
$out+=' <a id="sysManagerA" data-action="transferSysManager">';
$out+=$escape(rolePermissions.systemManager==null?'':rolePermissions.systemManager.userName);
$out+='</a> <a title="系统管理员" data-action="transferSysManager"><i class="fa fa-retweet"></i></a> ';
}else{
$out+=' ';
$out+=$escape(rolePermissions.systemManager==null?'':rolePermissions.systemManager.userName);
$out+=' ';
}
$out+=' </div> <div class="clearfix"></div> </div> <div class="ibox m-roleList"> <div class="ibox-content"> <table class="table table-bordered"> <thead style="background: #f5f5f5;"> <tr> <th width="15%" class="text-center">权限组</th> <th width="25%" class="text-center">操作权限</th> <th width="25%" class="text-center">权限描述</th> <th width="35%" class="text-center">人员</th> </tr> </thead> ';
$each(rolePermissions.roleList,function(role,i){
$out+=' <tbody p-code="';
$out+=$escape(role.code);
$out+='"> <tr style="background-clip: padding-box;" role-code="';
$out+=$escape(role.code);
$out+='"> <td rowspan="';
$out+=$escape(role.permissionList.length);
$out+='" id="';
$out+=$escape(role.id);
$out+='" class="text-center vmiddle v_a_middle pTarget"> <span class="dp-block pt-relative"> <span data-type="roleName">';
$out+=$escape(role.name);
$out+='</span> ';
if(role.code != 'SystemManager'){
$out+=' <a href="javascript:void(0);" class="btn-u btn-u-dark-blue btn-u-xs rounded add-button margin-left-5 hide pt-absolute" style="padding: 2px 6px;" data-action="addMemberBtn">添加人员</a> ';
}
$out+=' </span> </td> <td class="vmiddle" p-code="';
$out+=$escape(role.permissionList[0].code);
$out+='" id="';
$out+=$escape(role.permissionList[0].id);
$out+='"> <span class="dp-block pt-relative"> <span data-type="roleName">';
$out+=$escape(role.permissionList[0].name);
$out+='</span> ';
if(role.permissionList[0].code != 'sys_enterprise_logout'){
$out+=' <a href="javascript:void(0);" class="btn-u btn-u-dark-blue btn-u-xs rounded add-button hide " style="padding: 2px 6px;" data-action="addMemberBtn" >添加人员</a> ';
}
$out+=' </span> </td> <td class="vmiddle"> </td> <td p-code="';
$out+=$escape(role.permissionList[0].code);
$out+='" data-type="roleUsersTd"> ';
$each(role.permissionList[0].companyUserList,function(user,j){
$out+=' ';
if(role.permissionList[0].code != 'sys_enterprise_logout'){
$out+=' <span class="user-span pt-relative p-4"userId="';
$out+=$escape(user.userId);
$out+='" roleId="';
$out+=$escape(role.permissionList[0].id);
$out+='"> <span class="user-name curp btn-link" data-action="chooseRoleByDialog">';
$out+=$escape(user.userName);
$out+='</span> <span>';
$out+=$escape(j==role.permissionList[0].companyUserList.length-1?'&nbsp;&nbsp;':'&nbsp;');
$out+='</span> <span class="user-del hide pt-absolute" style="top: -2px;right: 2px;" data-action="deleteRoleUser"><i class="fa fa-times color-red curp "></i></span> </span> ';
}else{
$out+=' <span class="p-4" > <span class="user-name " p-code="';
$out+=$escape(role.permissionList[0].code);
$out+='" userId="';
$out+=$escape(user.userId);
$out+='" data-company-user-id="';
$out+=$escape(user.companyUserId);
$out+='">';
$out+=$escape(user.userName);
$out+='</span> <span>';
$out+=$escape(j==role.permissionList[0].companyUserList.length-1?'&nbsp;&nbsp;':'&nbsp;');
$out+='</span> </span> ';
}
$out+=' ';
});
$out+=' </td> </tr> ';
$each(role.permissionList,function(mem,a){
$out+=' ';
if(a>0){
$out+=' <tr style="background-clip: padding-box;"> <td class="vmiddle" p-code="';
$out+=$escape(mem.code);
$out+='" id="';
$out+=$escape(mem.id);
$out+='"> <span class="dp-block pt-relative"> <span data-type="roleName">';
$out+=$escape(mem.name);
$out+='</span> ';
if(mem.code != 'sys_enterprise_logout'){
$out+=' <a href="javascript:void(0);" class="btn-u btn-u-dark-blue btn-u-xs rounded add-button hide" style="padding: 2px 6px;" data-action="addMemberBtn" >添加人员</a> ';
}
$out+=' </span> </td> <td> </td> ';
if(isDialogShow==null||isDialogShow==1){
$out+=' <td p-code="';
$out+=$escape(mem.code);
$out+='" data-type="roleUsersTd"> ';
$each(mem.companyUserList,function(user,b){
$out+=' ';
if(mem.code != 'sys_enterprise_logout'){
$out+=' <span class="user-span p-4 pt-relative" userId="';
$out+=$escape(user.userId);
$out+='" roleId="';
$out+=$escape(mem.id);
$out+='"> <span class="user-name curp btn-link" data-action="chooseRoleByDialog">';
$out+=$escape(user.userName);
$out+='</span> <span>';
$out+=$escape(b==mem.companyUserList.length-1?'&nbsp;&nbsp;':'&nbsp;');
$out+='</span> <span class="user-del hide" style="position:absolute;top: -2px;right: 2px;" data-action="deleteRoleUser"> <i class="fa fa-times color-red curp "></i> </span> </span> ';
}else{
$out+=' <span class="p-4"> <span class="user-name" p-code="';
$out+=$escape(mem.code);
$out+='" userId="';
$out+=$escape(user.userId);
$out+='" data-company-user-id="';
$out+=$escape(user.companyUserId);
$out+='">';
$out+=$escape(user.userName);
$out+='</span> <span>';
$out+=$escape(b==mem.companyUserList.length-1?'&nbsp;&nbsp;':'&nbsp;');
$out+='</span> </span> ';
}
$out+=' ';
});
$out+=' </td> ';
}
$out+=' </tr> ';
}
$out+=' ';
});
$out+=' </tbody> ';
});
$out+=' </table> </div> </div> <script> $(\'.i-checks\').iCheck({ checkboxClass: \'icheckbox_minimal-green\', radioClass: \'iradio_minimal-green\' }); </script> ';
return new String($out);
});/*v:1*/
template('m_role/m_roleRightsPreview',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,rolePermissions=$data.rolePermissions,role=$data.role,i=$data.i,$escape=$utils.$escape,mem=$data.mem,a=$data.a,$out='';$out+='<div class="ibox m-b-none"> <div class="ibox-content m-b-none"> <table class="table table-bordered"> ';
$each(rolePermissions,function(role,i){
$out+=' <tbody p-code="';
$out+=$escape(role.code);
$out+='" class="border-none"> <tr role-code="';
$out+=$escape(role.code);
$out+='"> <td rowspan="';
$out+=$escape(role.permissionList.length);
$out+='" id="';
$out+=$escape(role.id);
$out+='" class="text-center vmiddle v_a_middle pTarget"> <span class="dp-block pt-relative"> ';
$out+=$escape(role.name);
$out+=' </span> </td> <td class="vmiddle" p-code="';
$out+=$escape(role.permissionList[0].code);
$out+='" id="';
$out+=$escape(role.permissionList[0].id);
$out+='"> <span class="dp-block pt-relative"> ';
$out+=$escape(role.permissionList[0].name);
$out+=' </span> </td> </tr> ';
$each(role.permissionList,function(mem,a){
$out+=' ';
if(a>0){
$out+=' <tr > <td class="vmiddle" p-code="';
$out+=$escape(mem.code);
$out+='" id="';
$out+=$escape(mem.id);
$out+='"> <span class="dp-block pt-relative"> ';
$out+=$escape(mem.name);
$out+=' </span> </td> </tr> ';
}
$out+=' ';
});
$out+=' </tbody> ';
});
$out+=' </table> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_summary/m_leaveSummary',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,type=$data.type,$out='';$out+=' <div class="ibox ibox_min_height"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 审批管理 </li> <li class="active fa fa-angle-right"> <strong> ';
if(type==4){
$out+=' 出差汇总 ';
}else{
$out+=' 请假汇总 ';
}
$out+=' </strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div class="" id="summary"> <input type="hidden" name="startDate" value=""/> <input type="hidden" name="endDate" value=""/> <input type="hidden" name="applyName" value=""/> <input type="hidden" name="leaveType" value=""/> <input type="hidden" name="auditPerson" value=""/>  <section class="mySummaryListBox"> <div class="row"> <div class="col-md-12" id="mySummaryListData"></div> <div class="col-md-12 padding-right-25"> <div id="mySummary-pagination-container" class="m-pagination pull-right"></div> </div> </div> </section>  </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_summary/m_leaveSummary_detail',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,detailData=$data.detailData,item=$data.item,j=$data.j,$escape=$utils.$escape,file=$data.file,fastdfsUrl=$data.fastdfsUrl,typeStr=$data.typeStr,type=$data.type,p=$data.p,i=$data.i,_momentFormat=$helpers._momentFormat,$out='';$out+='<style> .float_Mask{background-color: white;padding: 21px 3px;position: absolute;top: 19px;left: 124px; } </style> <div class="ibox m-b-xs"> <div class="ibox-content MyExpDetailsCheckExpenseOBox no-pd-bottom" style="max-height:700px;overflow-y: auto"> <fieldset style="padding-top:5px;"> <div class="headline m-b-sm"> <h2>审批状态</h2> </div> <div class="row"> <div class="col-md-12"> <div class="panel-body"> <ul class="timeline-v2 timeline-me"> ';
$each(detailData.flow.expAuditEntities,function(item,j){
$out+=' <li style="height: 60px;"> <time class="cbp_tmtime" datetime=""> <span style="top:0;font-size: 14px; ';
$out+=$escape((j==detailData.flow.expAuditEntities.length-1)?'color: #FF5722;':'');
$out+='">';
$out+=$escape(item.userName);
$out+='</span> <span style="top:0;font-size: 13px;">';
$out+=$escape(item.expDate);
$out+='</span> </time> <i class="cbp_tmicon rounded-x hidden-xs" style="top: 5px; ';
$out+=$escape((j==detailData.flow.expAuditEntities.length-1)?'background: #FF5722;':'');
$out+='"></i> <div class="cbp_tmlabel"> <h2 style="font-size: 14px;padding:0; ';
$out+=$escape((j==detailData.flow.expAuditEntities.length-1)?'color:#FF5722;':'');
$out+='"> ';
$out+=$escape(item.approveStatusName=="待审核"?"待审批":item.approveStatusName);
$out+=' </h2> ';
if(item.approveStatusName=='退回'){
$out+=' <p style="font-size: 13px;word-break: break-all;color:#999;line-height: 20px;"> 退回原因：';
$out+=$escape(item.remark);
$out+=' </p> ';
}
$out+=' </div> ';
if(item.approveStatusName.indexOf('完成')>-1){
$out+=' <span class="float_Mask"></span> ';
}
$out+=' </li> ';
});
$out+=' </ul> </div> </div> </div> ';
if(detailData.data[0].projectSkyDriveEntity.length>0){
$out+=' <div class="headline m-b"> <h2>相关附件</h2> </div> <div class="row margin-bottom-30"> <div class="col-md-12 p-w-m"> ';
$each(detailData.data[0].projectSkyDriveEntity,function(file,j){
$out+=' <span class="label m-r-xs dp-inline-block" style="background: #ecf0f1;padding: 5px 10px;"> <a class="curp m-l-xs" href="';
$out+=$escape(fastdfsUrl);
$out+=$escape(file.fileGroup);
$out+='/';
$out+=$escape(file.filePath);
$out+='" target="_blank"> <i class="fa fa-file-image-o"></i>&nbsp;';
$out+=$escape(file.fileName);
$out+=' </a> </span> ';
});
$out+=' </div> </div> ';
}
$out+=' <div class="headline m-b-sm"> <h2>';
$out+=$escape(typeStr);
$out+='信息</h2> </div> <div class="row"> <div class="col-md-12"> <table class="table m-b-none"> <thead> <tr> <td class="no-pd-left"> ';
if(type==4){
$out+=' 出差地点 ';
}else{
$out+=' 请假类型 ';
}
$out+=' </td> <td>开始时间</td> <td>结束时间</td> <!--<td> ';
if(type==4){
$out+=' 出差天数（天） ';
}else{
$out+=' 请假时长（小时） ';
}
$out+=' </td>--> <td class="no-pd-right"> ';
$out+=$escape(typeStr);
$out+='事由 </td> ';
if(type==4){
$out+=' <td>关联项目</td> ';
}
$out+=' </tr> </thead> <tbody> ';
$each(detailData.data,function(p,i){
$out+=' <tr> <td class="no-pd-left"> ';
if(type==4){
$out+=' ';
$out+=$escape(p.address);
$out+=' ';
}else{
$out+=' ';
$out+=$escape(p.typeName);
$out+=' ';
}
$out+=' </td> <td> ';
if(type==4){
$out+=' ';
$out+=$escape(_momentFormat(p.startTime,'A'));
$out+=' ';
}else{
$out+=' ';
$out+=$escape(_momentFormat(p.startTime,'YYYY/MM/DD H:mm'));
$out+=' ';
}
$out+=' </td> <td> ';
if(type==4){
$out+=' ';
$out+=$escape(_momentFormat(p.endTime,'A'));
$out+=' ';
}else{
$out+=' ';
$out+=$escape(_momentFormat(p.endTime,'YYYY/MM/DD H:mm'));
$out+=' ';
}
$out+=' </td> <!--<td >';
$out+=$escape(p.leaveTime);
$out+=$escape(p.leaveTime!=null?'天':'');
$out+='</td>--> <td style="word-break: break-all;">';
$out+=$escape(p.comment);
$out+='</td> ';
if(type==4){
$out+=' <td class="no-pd-right" >';
$out+=$escape(p.projectName);
$out+='</td> ';
}
$out+=' </tr> ';
});
$out+=' </tbody> </table> </div> </div> </fieldset> </div> </div>';
return new String($out);
});/*v:1*/
template('m_summary/m_leaveSummary_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,type=$data.type,$each=$utils.$each,myDataList=$data.myDataList,p=$data.p,$index=$data.$index,$escape=$utils.$escape,pageIndex=$data.pageIndex,_momentFormat=$helpers._momentFormat,rootPath=$data.rootPath,$out='';$out+='<style> </style> <table class="table table-hover table-bordered table-responsive"> <thead> <tr> <th width="6%">序号</th> <th width="10%"> 申请时间 <a class="icon-filter pull-right" id="filterApplyDate" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="10%"> 申请人 <a class="icon-filter pull-right" id="filterApplyName" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="10%"> ';
if(type==4){
$out+=' 出差地点 ';
}else{
$out+=' 请假类型 <a class="icon-filter pull-right" id="filterLeaveType" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> ';
}
$out+=' </th> <th width="18%"> ';
if(type==4){
$out+=' 出差事由 ';
}else{
$out+=' 请假事由 ';
}
$out+=' </th> <th width="13%">开始时间</th> <th width="13%">结束时间</th> <!--<th width="9%"> ';
if(type==4){
$out+=' 出差时长（天） ';
}else{
$out+=' 请假时长（天） ';
}
$out+=' </th>--> <th width="10%"> 审批人 <a class="icon-filter pull-right" id="filterAuditPerson" style="display: none;"><i class="icon iconfont icon-shaixuan"></i></a> </th> <th width="10%"> 审批时间 </th> </tr> </thead> <tbody> ';
$each(myDataList,function(p,$index){
$out+=' <tr class="curp" data-action="openShowExp" i="';
$out+=$escape($index);
$out+='" versionNum="';
$out+=$escape(p.versionNum);
$out+='"> <td>';
$out+=$escape($index+1+pageIndex*10);
$out+='</td> <td>';
$out+=$escape(p.applyDate);
$out+='</td> <td> ';
$out+=$escape(p.applyName);
$out+=' </td> <td> ';
if(type==4){
$out+=' ';
$out+=$escape(p.address);
$out+=' ';
}else{
$out+=' ';
$out+=$escape(p.typeName);
$out+=' ';
}
$out+=' </td> <td>';
$out+=$escape(p.comment);
$out+='</td> <td> ';
if(type==4){
$out+=' ';
$out+=$escape(_momentFormat(p.startTime,'A'));
$out+=' ';
}else{
$out+=' ';
$out+=$escape(_momentFormat(p.startTime,'YYYY/MM/DD H:mm'));
$out+=' ';
}
$out+=' </td> <td> ';
if(type==4){
$out+=' ';
$out+=$escape(_momentFormat(p.endTime,'A'));
$out+=' ';
}else{
$out+=' ';
$out+=$escape(_momentFormat(p.endTime,'YYYY/MM/DD H:mm'));
$out+=' ';
}
$out+=' </td> <!--<td>';
$out+=$escape(p.leaveTime);
$out+='天</td>--> <td>';
$out+=$escape(p.auditPerson);
$out+='</td> <td> ';
$out+=$escape(p.approveDate);
$out+=' </td> </tr> ';
});
$out+=' ';
if(myDataList==null || myDataList.length==0){
$out+=' <tr class="no-data"> <td colspan="9" align="center"> <div class="text-center"> <img src="';
$out+=$escape(rootPath);
$out+='/assets/img/default/without_exp.png"> </div> <span style="color:#4765a0">暂无内容</span> </td> </tr> ';
}
$out+=' </tbody> </table> ';
return new String($out);
});/*v:1*/
template('m_summary/m_summary_invoice','<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 财务管理 </li> <li class="active fa fa-angle-right"> <strong>发票汇总</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding" style=""> <form role="form" class="form-inline m-md"> <div class="m_cost_details"> <form class="form-inline m-md"> </form> </div> </form> <div class="data-list-box"> <div class="row"> <div class="col-md-12 data-list-container p-w-lg"></div> <div class="col-md-12 p-w-m"> <div id="data-pagination-container" class="m-pagination pull-right "></div> </div> </div> </div> </div> </div> ');/*v:1*/
template('m_summary/m_summary_invoice_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,dataList=$data.dataList,$each=$utils.$each,d=$data.d,i=$data.i,$escape=$utils.$escape,_momentFormat=$helpers._momentFormat,_expNumberFilter=$helpers._expNumberFilter,_url=$helpers._url,$out='';$out+='<table class="table table-bordered table-responsive"> <thead> <tr> <th width="">日期</th> <th> 申请人 </th> <th> 金额（元） </th> <th> 收支分类子项 </th> <th> 备注 </th> <th>收票方名称</th> <th>关联项目</th> <th>发票类型</th> <th>发票号码</th> </tr> </thead> <tbody> ';
if(dataList!=null && dataList.length>0){
$out+=' ';
$each(dataList,function(d,i){
$out+=' <tr> <td>';
$out+=$escape(_momentFormat(d.applyDate,'YYYY/MM/DD'));
$out+='</td> <td>';
$out+=$escape(d.companyUserName);
$out+='</td> <td>';
$out+=$escape(_expNumberFilter(d.fee));
$out+='</td> <td>';
$out+=$escape(d.costTypeName);
$out+='</td> <td>';
$out+=$escape(d.feeDescription);
$out+='</td> <td>';
$out+=$escape(d.relationCompanyName);
$out+='</td> <td>';
$out+=$escape(d.projectName);
$out+='</td> <td>';
$out+=$escape(d.invoiceTypeName);
$out+='</td> <td>';
$out+=$escape(d.invoiceNo);
$out+='</td> </tr> ';
});
$out+=' ';
}else{
$out+=' <tr> <td colspan="9" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">没有相关数据</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> ';
return new String($out);
});/*v:1*/
template('m_summary/m_summary_workingHours','<div class="ibox m_payments_profitStatement"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 审批管理 </li> <li class="active fa fa-angle-right"> <strong>工时汇总</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div class="data-list-box"> <div class="row"> <div class="col-md-12 data-list-container"></div> <div class="col-md-12 p-w-m"> <div id="data-pagination-container" class="m-pagination pull-right "></div> </div> </div> </div> </div> </div>');/*v:1*/
template('m_summary/m_summary_workingHours_detail',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,$out='';$out+='<div class="ibox m_payments_profitStatement"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-6">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 审批 </li> <li class=" fa fa-angle-right"> 工时汇总 </li> <li class="active fa fa-angle-right"> <strong>';
$out+=$escape(projectName);
$out+='</strong> </li> </ol> </div> </div> <div class="col-md-6 text-right p-w-sm"> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div class="data-list-box"> <div class="row"> <div class="col-md-12 data-list-container"></div> <div class="col-md-12 p-w-m"> <div id="data-pagination-container" class="m-pagination pull-right "></div> </div> </div> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_summary/m_summary_workingHours_detail_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,dataList=$data.dataList,d=$data.d,$index=$data.$index,$escape=$utils.$escape,_url=$helpers._url,sum=$data.sum,$out='';$out+='<table class="table table-bordered table-responsive"> <thead> <tr> <th width="20%">姓名</th> <th width="30%">角色</th> <th width="15%">联系电话</th> <th width="20%">邮箱</th> <th width="15%">工时（小时）</th> </tr> </thead> <tbody> ';
$each(dataList,function(d,$index){
$out+=' <tr> <td> <img class="img-circle" src="';
$out+=$escape(d.headImg);
$out+='" width="30"/>&nbsp;&nbsp;';
$out+=$escape(d.companyUserName);
$out+=' </td> <td>';
$out+=$escape(d.nodeName==null||d.nodeName==''?'--':d.nodeName);
$out+='</td> <td>';
$out+=$escape(d.cellphone==null||d.cellphone==''?'--':d.cellphone);
$out+='</td> <td>';
$out+=$escape(d.email==null||d.email==''?'--':d.email);
$out+='</td> <td>';
$out+=$escape(d.hours==null?'--':d.hours);
$out+='</td> </tr> ';
});
$out+=' ';
if(!(dataList && dataList.length>0)){
$out+=' <tr> <td colspan="6" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">没有相关数据</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> <div class="pt-absolute "> <div class="m-l-xs"> ';
if(sum!=null){
$out+=' <span>总工时：';
$out+=$escape(sum);
$out+='小时</span> ';
}
$out+=' </div> </div>';
return new String($out);
});/*v:1*/
template('m_summary/m_summary_workingHours_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,dataList=$data.dataList,d=$data.d,$index=$data.$index,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<table class="table table-bordered table-responsive"> <thead> <tr> <th width="20%">项目编号</th> <th width="30%">项目名称</th> <th width="25%">参与人数</th> <th width="25%">累计工时</th> </tr> </thead> <tbody> ';
$each(dataList,function(d,$index){
$out+=' <tr> <td>';
$out+=$escape(d.projectNo==null?'--':d.projectNo);
$out+='</td> <td> <a class="p-r" href="javascript:void(0);" data-action="viewDetail" data-id="';
$out+=$escape(d.id);
$out+='">';
$out+=$escape(d.projectName);
$out+='</a> </td> <td>';
$out+=$escape(d.num==null?'--':d.num);
$out+='</td> <td>';
$out+=$escape(d.hours==null?'--':d.hours);
$out+='</td> </tr> ';
});
$out+=' ';
if(!(dataList && dataList.length>0)){
$out+=' <tr> <td colspan="4" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">没有相关数据</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> ';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_changeOperator',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,orgUserList=$data.orgUserList,p=$data.p,$index=$data.$index,$escape=$utils.$escape,selectedUserList=$data.selectedUserList,s=$data.s,$out='';$out+='<form class="form-horizontal rounded-bottom taskProgressOBox noborder" style="max-height: 200px;overflow: auto;"> <div class="ibox"> <div class="ibox-content"> <div class="userListBox"> <table class="table table-striped table-hover table-responsive m-b-none" > <thead> <tr> <th>姓名</th> <th>手机号</th> <th></th> </tr> </thead> <tbody> ';
$each(orgUserList,function(p,$index){
$out+=' <tr> <td>';
$out+=$escape(p.userName);
$out+='</td> <td>';
$out+=$escape(p.cellphone);
$out+='</td> <td> ';
if(selectedUserList!=null && selectedUserList.length>0){
$out+=' ';
$each(selectedUserList,function(s,$index){
$out+=' ';
if(s.id == p.id){
$out+=' <a href="javascript:void(0)" data-companyUserId="';
$out+=$escape(p.id);
$out+='" data-userId="';
$out+=$escape(p.userId);
$out+='" class="btn-u btn-u-default btn-u-xs rounded no-hover">选择</a> ';
}else{
$out+=' <a href="javascript:void(0)" data-action="choseUser" data-companyUserId="';
$out+=$escape(p.id);
$out+='" data-userId="';
$out+=$escape(p.userId);
$out+='" class="btn-u btn-u-primany btn-u-xs rounded">选择</a> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' </td> </tr> ';
});
$out+=' ';
if(orgUserList==null || orgUserList.length==0){
$out+=' <tr> <td colspan="3" align="center">暂无数据</td> </tr> ';
}
$out+=' </tbody> <tfoot><tr><td colspan="3" id="userList"></td></tr></tfoot> </table> </div> </div> </div> </form> ';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_progressChange_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,taskScheduleChangesList=$data.taskScheduleChangesList,$escape=$utils.$escape,_momentFormat=$helpers._momentFormat,$each=$utils.$each,v=$data.v,$index=$data.$index,$out='';$out+='<form class="form-horizontal rounded-bottom taskProgressOBox noborder" style="max-height: 200px;overflow: auto;"> <div class="ibox"> <div class="ibox-content"> <div class="row"> <div class="col-md-3 no-pd-right"> <label style="text-align: right"> <b>计划进度:</b> </label> </div> ';
if(taskScheduleChangesList && taskScheduleChangesList.length>0){
$out+=' <div class="col-md-9"> ';
$out+=$escape(_momentFormat(taskScheduleChangesList[0].startTime,'YYYY/MM/DD'));
$out+=' ';
if((taskScheduleChangesList[0].startTime!=null && taskScheduleChangesList[0].startTime!='')
                    || (taskScheduleChangesList[0].endTime!=null && taskScheduleChangesList[0].endTime!='')){
$out+=' <span>~</span> ';
}
$out+=' ';
$out+=$escape(_momentFormat(taskScheduleChangesList[0].endTime,'YYYY/MM/DD'));
$out+=' ';
if((taskScheduleChangesList[0].startTime!=null && taskScheduleChangesList[0].startTime!='')
                    && (taskScheduleChangesList[0].endTime!=null && taskScheduleChangesList[0].endTime!='')){
$out+=' ';
if(taskScheduleChangesList[0].allDay!=null){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(taskScheduleChangesList[0].allDay);
$out+='</span>天 )</span> ';
}
$out+=' ';
}
$out+=' </div> ';
}
$out+=' <div class="clearfix"></div> </div> ';
$each(taskScheduleChangesList,function(v,$index){
$out+=' ';
if($index>0 && $index==1){
$out+=' <div class="row"> <div class="col-md-3 no-pd-right"> <label class="" style="text-align: right"> <b>第';
$out+=$escape($index);
$out+='次变更:</b> </label> </div> <div class="col-md-9"> ';
$out+=$escape(_momentFormat(v.startTime,'YYYY/MM/DD'));
$out+=' ';
if((v.startTime!=null && v.startTime!='') || (v.endTime!=null && v.endTime!='')){
$out+=' <span>~</span> ';
}
$out+=' ';
$out+=$escape(_momentFormat(v.endTime,'YYYY/MM/DD'));
$out+=' ';
if((v.startTime!=null && v.startTime!='') && (v.endTime!=null && v.endTime!='')){
$out+=' ';
if(v.timeDiffStr!=null){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(v.timeDiffStr);
$out+='</span>天 )</span> ';
}
$out+=' ';
}
$out+=' </div> </div> <div class="col-md-7 col-md-offset-3 "> <label class="" style="position: relative;top: -2px;word-break: break-all"> ';
if((v.startTime==null || v.startTime=='') || (v.endTime==null || v.endTime=='')){
$out+=' <b>变更原因：</b> <span class="memo-span">';
$out+=$escape(v.memo);
$out+='</span> ';
}else{
$out+=' <b>变更原因：</b> <span class="memo-span">';
$out+=$escape(v.memo);
$out+='</span> ';
}
$out+=' </label> <div class="clearfix"></div> </div> <div class="clearfix"></div> </div> ';
}
$out+=' ';
});
$out+=' <div class="clearfix"></div> </div> </div> </form> ';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_scheduleChanges',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,taskScheduleChangesList=$data.taskScheduleChangesList,$escape=$utils.$escape,$each=$utils.$each,v=$data.v,$index=$data.$index,$out='';$out+='<form class="form-horizontal rounded-bottom taskProgressOBox noborder"> <div class="ibox"> <div class="ibox-content"> <div class="col-md-12"> <div class="col-md-2"> <label> <b>任务计划进度:</b> </label> </div> ';
if(taskScheduleChangesList && taskScheduleChangesList.length>0){
$out+=' <div class="col-md-6"> ';
$out+=$escape(taskScheduleChangesList[0].startTime);
$out+=' ';
if((taskScheduleChangesList[0].startTime!=null && taskScheduleChangesList[0].startTime!='')
                    || (taskScheduleChangesList[0].endTime!=null && taskScheduleChangesList[0].endTime!='')){
$out+=' <span>~</span> ';
}
$out+=' ';
$out+=$escape(taskScheduleChangesList[0].endTime);
$out+=' ';
if((taskScheduleChangesList[0].startTime!=null && taskScheduleChangesList[0].startTime!='')
                    && (taskScheduleChangesList[0].endTime!=null && taskScheduleChangesList[0].endTime!='')){
$out+=' ';
if(taskScheduleChangesList[0].allDay!=null){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(taskScheduleChangesList[0].allDay);
$out+='</span>天 )</span> ';
}
$out+=' ';
}
$out+=' </div> <div class="col-md-4"> <a href="javascript:void(0)" data-action="addScheduleProgressChange" class="btn-u btn-u-xs rounded ';
if(taskScheduleChangesList && taskScheduleChangesList.length>1){
$out+='hide';
}
$out+='">变更</a> <!--<a href="javascript:void(0)" data-action="delScheduleProgressChange" data-seq="0" data-id="';
$out+=$escape(taskScheduleChangesList[0].id);
$out+='" class="btn-u btn-u-xs btn-u-red rounded ';
if(taskScheduleChangesList && taskScheduleChangesList.length>1){
$out+='hide';
}
$out+='" >删除</a>--> </div> ';
}
$out+=' <div class="clearfix"></div> </div> ';
$each(taskScheduleChangesList,function(v,$index){
$out+=' ';
if($index>0){
$out+=' <div class="col-md-12"> <div class="col-md-2"> <label class="" style="text-align: right"> <b>第';
$out+=$escape($index);
$out+='次变更:</b> </label> </div> <div class="col-md-6"> ';
$out+=$escape(v.startTime);
$out+=' ';
if((v.startTime!=null && v.startTime!='') || (v.endTime!=null && v.endTime!='')){
$out+=' <span>~</span> ';
}
$out+=' ';
$out+=$escape(v.endTime);
$out+=' ';
if((v.startTime!=null && v.startTime!='') && (v.endTime!=null && v.endTime!='')){
$out+=' ';
if(v.timeDiffStr!=null){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(v.timeDiffStr);
$out+='</span>天 )</span> ';
}
$out+=' ';
}
$out+=' </div> <div class="col-md-4"> <a href="javascript:void(0)" data-action="addScheduleProgressChange" class="btn-u btn-u-xs rounded ';
if(taskScheduleChangesList && taskScheduleChangesList.length!=$index+1){
$out+='hide';
}
$out+='" >变更</a> <!-- <a href="javascript:void(0)" data-action="delScheduleProgressChange" data-id="';
$out+=$escape(v.id);
$out+='" class="btn-u btn-u-xs btn-u-red rounded ';
if(taskScheduleChangesList && taskScheduleChangesList.length!=$index+1){
$out+='hide';
}
$out+='">删除</a>--> </div> <div class="col-md-10 col-md-offset-2 "> <label class="" style="position: relative;top: -2px;word-break: break-all"> <b>变更原因：</b> ';
$out+=$escape(v.memo);
$out+=' </label> <div class="clearfix"></div> </div> <div class="clearfix"></div> </div> ';
}
$out+=' ';
});
$out+=' </div> </div> </form> ';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_scheduleChanges_new',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,$labelText=$data.$labelText,taskScheduleChangesList=$data.taskScheduleChangesList,_momentFormat=$helpers._momentFormat,$each=$utils.$each,v=$data.v,$index=$data.$index,$out='';$out+='<form class="form-horizontal rounded-bottom taskProgressOBox noborder"> <div class="ibox"> <div class="ibox-content"> <div class="col-md-12"> <div class="col-md-2"> <label> <b>';
$out+=$escape($labelText);
$out+=':</b> </label> </div> ';
if(taskScheduleChangesList && taskScheduleChangesList.length>0){
$out+=' <div class="col-md-7"> ';
$out+=$escape(_momentFormat(taskScheduleChangesList[0].startTime,'YYYY/MM/DD'));
$out+=' ';
if((taskScheduleChangesList[0].startTime!=null && taskScheduleChangesList[0].startTime!='')
                    || (taskScheduleChangesList[0].endTime!=null && taskScheduleChangesList[0].endTime!='')){
$out+=' <span>~</span> ';
}
$out+=' ';
$out+=$escape(_momentFormat(taskScheduleChangesList[0].endTime,'YYYY/MM/DD'));
$out+=' ';
if((taskScheduleChangesList[0].startTime!=null && taskScheduleChangesList[0].startTime!='')
                    && (taskScheduleChangesList[0].endTime!=null && taskScheduleChangesList[0].endTime!='')){
$out+=' ';
if(taskScheduleChangesList[0].allDay!=null){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(taskScheduleChangesList[0].allDay);
$out+='</span>天 )</span> ';
}
$out+=' ';
}
$out+=' </div> <div class="col-md-3"> <a href="javascript:void(0)" data-action="addScheduleProgressChange" class="btn-u btn-u-xs rounded ';
$out+=$escape(taskScheduleChangesList && taskScheduleChangesList.length>1?'hide':'');
$out+='" data-id="';
$out+=$escape(taskScheduleChangesList[0].id);
$out+='" data-type="';
$out+=$escape(taskScheduleChangesList[0].type);
$out+='" data-list-type="1" data-start-time="';
$out+=$escape(taskScheduleChangesList[0].startTime);
$out+='" data-end-time="';
$out+=$escape(taskScheduleChangesList[0].endTime);
$out+='"> ';
$out+=$escape(taskScheduleChangesList[0].type==3?'修改':'变更');
$out+=' </a> <!--<a href="javascript:void(0)" data-action="delScheduleProgressChange" data-seq="0" data-id="';
$out+=$escape(taskScheduleChangesList[0].id);
$out+='" class="btn-u btn-u-xs btn-u-red rounded ';
if(taskScheduleChangesList && taskScheduleChangesList.length>1){
$out+='hide';
}
$out+='" >删除</a>--> </div> ';
}
$out+=' <div class="clearfix"></div> </div> ';
$each(taskScheduleChangesList,function(v,$index){
$out+=' ';
if($index>0){
$out+=' <div class="col-md-12"> <div class="col-md-2"> <label class="" style="text-align: right"> <b>第';
$out+=$escape($index);
$out+='次变更:</b> </label> </div> <div class="col-md-7"> ';
$out+=$escape(_momentFormat(v.startTime,'YYYY/MM/DD'));
$out+=' ';
if((v.startTime!=null && v.startTime!='') || (v.endTime!=null && v.endTime!='')){
$out+=' <span>~</span> ';
}
$out+=' ';
$out+=$escape(_momentFormat(v.endTime,'YYYY/MM/DD'));
$out+=' ';
if((v.startTime!=null && v.startTime!='') && (v.endTime!=null && v.endTime!='')){
$out+=' ';
if(v.timeDiffStr!=null){
$out+=' <span>&nbsp;( <span class="diffDaysTxt">';
$out+=$escape(v.timeDiffStr);
$out+='</span>天 )</span> ';
}
$out+=' ';
}
$out+=' </div> <div class="col-md-3"> <a href="javascript:void(0)" data-action="addScheduleProgressChange" class="btn-u btn-u-xs rounded ';
$out+=$escape(taskScheduleChangesList && taskScheduleChangesList.length!=$index+1?'hide':'');
$out+='" data-id="';
$out+=$escape(v.id);
$out+='" data-type="';
$out+=$escape(v.type);
$out+='" data-start-time="';
$out+=$escape(v.startTime);
$out+='" data-end-time="';
$out+=$escape(v.endTime);
$out+='"> ';
$out+=$escape(v.type==3?'修改':'变更');
$out+=' </a> <!-- <a href="javascript:void(0)" data-action="delScheduleProgressChange" data-id="';
$out+=$escape(v.id);
$out+='" class="btn-u btn-u-xs btn-u-red rounded ';
if(taskScheduleChangesList && taskScheduleChangesList.length!=$index+1){
$out+='hide';
}
$out+='">删除</a>--> </div> <div class="col-md-10 col-md-offset-2 "> <label class="" style="position: relative;line-height:18px;word-break: break-all;"> <b>变更原因：</b> <span class="memo-span">';
$out+=$escape(v.memo);
$out+='</span> </label> <div class="clearfix"></div> </div> <div class="clearfix"></div> </div> ';
}
$out+=' ';
});
$out+=' </div> </div> </form> ';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,orgList=$data.orgList,$each=$utils.$each,o=$data.o,$index=$data.$index,dataCompanyId=$data.dataCompanyId,currentManagerObj=$data.currentManagerObj,currentCompanyId=$data.currentCompanyId,currentCompanyUserId=$data.currentCompanyUserId,$out='';$out+='<div class="ibox"> <div class="ibox-title secondary-menu-outbox"> <div class="row"> <div class="col-md-4">  <div class="no-margin p-h-sm breadcrumb-box" > <ol class="breadcrumb"> <li> 我的项目 </li> <li class=" fa fa-angle-right"> ';
$out+=$escape(projectName);
$out+=' </li> <li class="active fa fa-angle-right"> <strong>任务签发</strong> </li> </ol> </div> </div> <div class="col-md-8 text-right" style="padding: 9px 15px;"> ';
if(orgList!=null && orgList.length>0){
$out+=' <strong>当前视图：</strong> <select name="viewByOrg" class="wid-200"> ';
$each(orgList,function(o,$index){
$out+=' ';
if(dataCompanyId == o.id){
$out+=' <option value="';
$out+=$escape(o.id);
$out+='" selected>';
$out+=$escape(o.companyName);
$out+='</option> ';
}else{
$out+=' <option value="';
$out+=$escape(o.id);
$out+='">';
$out+=$escape(o.companyName);
$out+='</option> ';
}
$out+=' ';
});
$out+=' </select> <span class="icon-separation-line"></span> ';
}
$out+=' <span class="">经营负责人：</span> <span class="m-r-sm"> ';
$out+=$escape(currentManagerObj.projectManager.companyUserName);
$out+=' ';
if(currentManagerObj.projectManager.isUpdateOperator==1 && currentCompanyId==dataCompanyId){
$out+=' <a href="javascript:void(0)" data-action="changeOperatorPerson" data-i="0" data-id="';
$out+=$escape(currentManagerObj.projectManager.companyUserId);
$out+='" data-company-id="';
$out+=$escape(currentManagerObj.projectManager.companyId);
$out+='" data-user-name="';
$out+=$escape(currentManagerObj.projectManager.companyUserName);
$out+='" class="showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="更换经营负责人"> <i class="fa fa-retweet"></i> </a> ';
}
$out+=' </span> <span>经营助理：</span> <span class=""> ';
$out+=$escape(currentManagerObj.assistant==null || currentManagerObj.assistant.companyUserName==null?'未设置':currentManagerObj.assistant.companyUserName);
$out+=' ';
if(currentManagerObj.projectManager.companyUserId==currentCompanyUserId && currentCompanyId==dataCompanyId){
$out+=' ';
if(currentManagerObj.assistant!=null){
$out+=' <a href="javascript:void(0)" data-action="changeManagerPerson" data-i="0" data-id="';
$out+=$escape(currentManagerObj.assistant.companyUserId);
$out+='" data-user-name="';
$out+=$escape(currentManagerObj.assistant.companyUserName);
$out+='" data-company-id="';
$out+=$escape(currentManagerObj.assistant.companyId);
$out+='" class="showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="更换经营助理"> <i class="fa fa-retweet"></i> </a> ';
}else{
$out+=' <a href="javascript:void(0)" data-action="changeManagerPerson" data-i="0" data-id="" data-user-name="" data-company-id="" class="showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="更换助理"> <i class="fa fa-retweet"></i> </a> ';
}
$out+=' ';
}
$out+=' </span> <span class="icon-separation-line"></span> <a href="javascript:void(0);" data-action="viewProjectInfo"> 项目基本信息&nbsp;<i class="icon iconfont icon-jibenxinxi"></i> </a> </div> <div class="clearfix"></div> </div> </div> <div class="ibox-content no-padding"> <div id="taskIssueList" class="list-box"></div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssueDesignContent_add','<div class="content-box overflow-hidden"> <form class="content-form"> <div class="col-md-12" style="padding: 20px 30px 0;"> <div class="form-group m-r-xs"> <label>设计任务</label> <input placeholder="设计任务" class="form-control" type="text" name="taskName" maxlength="100"> </div> </div> </form> <div id="time-box"> </div> </div> ');/*v:1*/
template('m_taskIssue/m_taskIssue_add',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,doType=$data.doType,$each=$utils.$each,allCompanyList=$data.allCompanyList,s=$data.s,$index=$data.$index,companyId=$data.companyId,$escape=$utils.$escape,orgList=$data.orgList,d=$data.d,currentCompanyId=$data.currentCompanyId,orgId=$data.orgId,appointmentStartTime=$data.appointmentStartTime,appointmentEndTime=$data.appointmentEndTime,$out='';$out+='<style> .add-issue-box{width:430px;} .add-issue-box .form-group .form-control{width: 100%;} .add-issue-box .select2.select2-container{width: 309px !important;} .add-issue-box .select2.select2-container span.select2-selection{border-radius: 0;height: 34px;} .add-issue-box .select2-container--default .select2-selection--single .select2-selection__arrow b{border-width: 6px 3px 0px 3px;margin-left: -2px;margin-top: 1px;} </style> <form class="add-issue-form"> <div class="add-issue-box "> ';
if(doType==null){
$out+=' <div class="form-group m-b-xs clearfix"> <label class="col-24-sm-6 control-label no-pd-left m-t-xs">设计任务<span class="color-red">*</span></label> <div class="col-24-sm-18 no-pd-right"> <input placeholder="设计任务" class="form-control" type="text" name="taskName" maxlength="100"> </div> </div> ';
}
$out+=' <div class="form-group m-b-xs clearfix"> <label class="col-24-sm-6 control-label no-pd-left m-t-xs">设计组织<span class="color-red">*</span></label> <div class="col-24-sm-18 no-pd-right"> <select class="form-control" name="designOrg"> ';
$each(allCompanyList,function(s,$index){
$out+=' ';
if(companyId == s.id){
$out+=' <option value="';
$out+=$escape(s.id);
$out+='" data-type="';
$out+=$escape(s.companyType);
$out+='" selected>';
$out+=$escape(s.companyName);
$out+='</option> ';
}else{
$out+=' <option value="';
$out+=$escape(s.id);
$out+='" data-type="';
$out+=$escape(s.companyType);
$out+='">';
$out+=$escape(s.companyName);
$out+='</option> ';
}
$out+=' ';
});
$out+=' </select> </div> </div> <div class="form-group depart-div m-b-xs clearfix"> <label class="col-24-sm-6 control-label no-pd-left m-t-xs">部门</label> <div class="col-24-sm-18 no-pd-right"> <select class="form-control" name="orgId"> <option value="">请选择</option> ';
$each(orgList,function(d,$index){
$out+=' ';
if(currentCompanyId!=d.id){
$out+=' ';
if(orgId==d.id){
$out+=' <option value="';
$out+=$escape(d.id);
$out+='" selected> ';
$out+=$escape(d.departName);
$out+='</option> ';
}else{
$out+=' <option value="';
$out+=$escape(d.id);
$out+='"> ';
$out+=$escape(d.departName);
$out+='</option> ';
}
$out+=' ';
}
$out+=' ';
});
$out+=' </select> </div> </div> <div class="form-group hide operator-div m-b-xs clearfix" style="display: none;"> <label class="col-24-sm-6 control-label no-pd-left no-pd-right m-t-xs">经营负责人</label> <div class="col-24-sm-18 no-pd-right"> <select class="js-example-disabled-results form-control" name="managerId" style="width:280px;"> </select> </div> <div class="clearfix"></div> </div>  <div class="form-group time-box m-b-xs clearfix">  <label class="col-24-sm-6 control-label no-pd-left no-pd-right m-t-xs">计划开始时间<span class="color-red">*</span></label> <div class="col-24-sm-18 no-pd-right"> <div class="input-group"> <input type="text" class="form-control timeInput startTime input-sm" id="ipt_startTime" name="startTime" data-appointmentStartTime = "';
$out+=$escape(appointmentStartTime);
$out+='" placeholder="开始日期" readonly onFocus="startTimeFun(this,m_inputProcessTime_onpicked)" value="';
$out+=$escape(appointmentStartTime);
$out+='"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </div> </div> <div class="form-group time-box m-b-xs clearfix">  <label class="col-24-sm-6 control-label no-pd-left no-pd-right m-t-xs">计划结束时间<span class="color-red">*</span></label> <div class="col-24-sm-18 no-pd-right"> <div class="input-group"> <input type="text" class="form-control timeInput endTime input-sm" id="ipt_endTime" name="endTime" data-appointmentEndTime = "';
$out+=$escape(appointmentEndTime);
$out+='" placeholder="结束日期" readonly onFocus="endTimeFun(this,m_inputProcessTime_onpicked)" value="';
$out+=$escape(appointmentEndTime);
$out+='"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </div> </div> <div class="col-md-12 m-b-xs no-pd-right"> <button type="button" class="btn btn-default btn-sm m-popover-close pull-right "> <i class="glyphicon glyphicon-remove"></i> </button> <button type="button" class="btn btn-primary btn-sm m-popover-submit pull-right m-r-xs"> <i class="fa fa-check"></i> </button> </div> </div> </form> ';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,taskIssueList=$data.taskIssueList,isOrgManager=$data.isOrgManager,isAssistant=$data.isAssistant,currentCompanyId=$data.currentCompanyId,projectCompanyId=$data.projectCompanyId,$each=$utils.$each,t=$data.t,ti=$data.ti,$escape=$utils.$escape,_momentFormat=$helpers._momentFormat,_isNullOrBlank=$helpers._isNullOrBlank,_timeDifference=$helpers._timeDifference,_url=$helpers._url,$out='';$out+='<form class="m_taskIssue_list task-list-box"> <table class="tree table table-bordered table-striped tree-box"> <thead> <tr> <th width="3%" class="b-r-none"> ';
if(taskIssueList!=null && taskIssueList.length>0 && (isOrgManager==1 || isAssistant==1)){
$out+=' <div class="list-check-box"> <label class="i-checks fw-normal m-b-none"> <input name="taskAllCk" type="checkbox" /> <span class="i-checks-span"></span> </label> </div> ';
}
$out+=' </th> <th width="3%" class="b-r-none b-l-none"> <div class="list-action-box"> ';
if(isOrgManager==1 || isAssistant==1){
$out+=' <div class="btn-group batchOperation" id="batchAllOperation"> <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <span class="ic-operation-batch"></span> </button> <ul class="dropdown-menu"> <li> <a href="javascript:void(0);" data-action="batchPublishTask" class="">批量发布任务</a> </li> <li> <a href="javascript:void(0);" class="" data-action="batchDelTask" data-canbedelete="">批量删除</a> </li> </ul> </div> ';
}
$out+=' </div> </th> <th width="24%" class="b-r-none b-l-none v-middle" style="padding-left: 38px;"> <span>设计任务</span> ';
if(isOrgManager==1 && currentCompanyId == projectCompanyId){
$out+=' <button class="btn btn-link" data-action="addDesignTask" title="添加设计任务"><i class="fa fa-plus"></i></button> ';
}
$out+=' </th> <th width="5%" class="b-r-none">任务描述</th> <th width="15%" class="b-r-none v-middle">设计组织</th> <th width="20%" class="b-r-none v-middle">计划进度</th> <th width="10%" class="b-r-none v-middle">进度提示</th> <th width="10%" class="b-r-none v-middle">完成时间</th> <th width="10%" class="v-middle">任务状态</th> </tr> </thead> <tbody> ';
$each(taskIssueList,function(t,ti){
$out+=' <tr class="tree-box-tr treegrid-';
$out+=$escape(t.id);
$out+=' ';
if(t.taskPid!=null && t.taskPid!=''){
$out+='treegrid-parent-';
$out+=$escape(t.taskPid);
}
$out+=' ';
$out+=$escape(t.taskState=='3'||t.taskState=='4'?'completeDate-tr':'');
$out+='" data-i="';
$out+=$escape(ti);
$out+='" data-id="';
$out+=$escape(t.id);
$out+='" data-pid="';
$out+=$escape(t.taskPid);
$out+='" data-company-id="';
$out+=$escape(t.companyId);
$out+='" data-publish-id="';
$out+=$escape(t.publishId);
$out+='"> <td class="v-middle b-r-none l-h-14"> <div class="list-check-box"> <label class="i-checks fw-normal m-b-none"> <input name="taskCk" type="checkbox" /> <span class="i-checks-span"></span> </label> </div> </td> <td class="b-r-none b-l-none"> <div class="list-action-box"> <div class="btn-group singleOperation" style="display: none;"> <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <span class="ic-operation"></span> </button> <ul class="dropdown-menu"> ';
if(t.addChild && (isOrgManager==1 || isAssistant==1)){
$out+=' <li> <a href="javascript:void(0);" data-action="addSubTask">添加子任务</a> </li> ';
}
$out+=' ';
if(t.taskStatus==2 && (isOrgManager==1 || isAssistant==1)){
$out+=' <li> <a href="javascript:void(0);" data-action="publishTask">发布任务</a> </li> ';
}
$out+='  ';
if(!t.first && (isOrgManager==1 || isAssistant==1)){
$out+=' <li> <a href="javascript:void(0);" data-action="moveUp">向上移动</a> </li> ';
}
$out+='  ';
if(!t.last && (isOrgManager==1 || isAssistant==1)){
$out+=' <li> <a href="javascript:void(0);" data-action="moveDown">向下移动</a> </li> ';
}
$out+=' ';
if(t.canBeDelete && (isOrgManager==1 || isAssistant==1)){
$out+=' <li> <a href="javascript:void(0);" data-action="delTask" data-canbedelete="';
$out+=$escape(t.canBeDelete?'1':'0');
$out+='">删除</a> </li> ';
}
$out+=' </ul> </div> ';
if((isOrgManager==1 || isAssistant==1) && t.isHasChild==1){
$out+=' <div class="btn-group batchOperation" style="display: none;"> <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <span class="ic-operation-batch"></span> </button> <ul class="dropdown-menu"> <li> <a href="javascript:void(0);" data-action="batchPublishTask" >批量发布任务</a> </li> <li> <a href="javascript:void(0);" data-action="batchDelTask" data-canbedelete="';
$out+=$escape(t.canBeDelete?'1':'0');
$out+='">批量删除</a> </li> </ul> </div> ';
}
$out+=' </div> </td> <td class="treeTd pt-relative v-middle b-r-none b-l-none" height="40" > <!--';
if(t.taskState=='3'||t.taskState=='4'){
$out+='<span class="pull-left"><i class="fa fa-check fc-v1-green"></i></span>';
}
$out+='--> <span class="show-span taskName pt-relative" data-string="';
$out+=$escape(t.taskName);
$out+='">';
$out+=$escape(t.taskName);
$out+='</span> ';
if(t.issuePlanList!=null && t.issuePlanList.length>0 ){
$out+=' <a class="tree-td-a" href="javascript:void(0);" data-action="viewPlanTime" id="viewPlanTime';
$out+=$escape(t.id);
$out+='"> <i class="fa fa-star-half-empty"></i> </a> ';
}
$out+=' ';
if(t.canBeEdit && (isOrgManager==1 || isAssistant==1)){
$out+=' <a class="tree-td-a" href="javascript:void(0);" data-action="taskNameEdit" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </td> <td class="b-r-none v-middle"> ';
if(t.taskRemark==null || t.taskRemark==''){
$out+=' ';
if(t.canBeEdit && (isOrgManager==1 || isAssistant==1)){
$out+=' <span class="fc-ccc show-span">未设置</span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' ';
}else{
$out+=' <span class="edit-span-box wh-16" > <a data-action="viewTaskRemarkEdit" id="viewTaskRemarkEdit';
$out+=$escape(ti);
$out+='"> <i class="ic-describe"></i></a> </span> ';
}
$out+=' ';
if(t.canBeEdit && (isOrgManager==1 || isAssistant==1)){
$out+=' <span class="edit-span-box wh-16"> <a href="javascript:void(0);" data-action="taskRemarkEdit" id="taskRemarkEdit';
$out+=$escape(ti);
$out+='" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> </span> ';
}
$out+=' </td> <td class="v-middle b-r-none "> <span class="show-span" data-company-id="';
$out+=$escape(t.companyId);
$out+='" data-depart-id="';
$out+=$escape(t.departId);
$out+='"> ';
if(t.departName!=null && t.departName!=''){
$out+=' ';
$out+=$escape(t.departName);
$out+=' <i class="iconfont rounded icon-2fengongsi m-r-xs" data-toggle="tooltip" data-original-title="';
$out+=$escape(t.companyName);
$out+='"></i> ';
}else{
$out+=' ';
$out+=$escape(t.companyName);
$out+=' ';
}
$out+=' </span> ';
if(t.issueLevel<2 && t.taskStatus == 2 && (isOrgManager==1 || isAssistant==1)){
$out+=' <a class="" href="javascript:void(0);" data-action="choseDesignOrg" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </td> <td class="v-middle b-r-none "> <span class="show-span" data-type="planTime" data-start-time="';
$out+=$escape(t.planStartTime);
$out+='" data-end-time="';
$out+=$escape(t.planEndTime);
$out+='"> ';
if((t.planStartTime==null || t.planStartTime=='') && (t.planEndTime==null || t.planEndTime=='')){
$out+=' <span class="fc-ccc">未设置</span> ';
}else{
$out+=' ';
$out+=$escape(_momentFormat(t.planStartTime,'YYYY/MM/DD'));
$out+=' - ';
$out+=$escape(_momentFormat(t.planEndTime,'YYYY/MM/DD'));
$out+=' ';
if(_isNullOrBlank(t.planStartTime) || _isNullOrBlank(t.planEndTime)){
$out+=' | 共 - 天 ';
}else{
$out+=' | 共 ';
$out+=$escape(_timeDifference(t.planStartTime,t.planEndTime));
$out+=' 天 ';
}
$out+=' ';
if(t.changeTime && t.taskState!=7){
$out+=' <a href="javascript:void(0);" data-action="viewProgressChange" id="viewProgressByChange1';
$out+=$escape(t.id);
$out+='"><i class="fa fa-info-circle"></i></a> ';
}
$out+=' ';
}
$out+=' </span> ';
if((t.fromCompanyId==currentCompanyId || (t.fromCompanyId==null && t.companyId == currentCompanyId)) && (isOrgManager==1 || isAssistant==1)){
$out+=' <a href="javascript:void(0);" data-action="startTimeEdit" data-status="';
$out+=$escape(t.taskStatus);
$out+='" data-start-time="';
$out+=$escape(t.planStartTime);
$out+='" data-end-time="';
$out+=$escape(t.planEndTime);
$out+='" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </td> <td> ';
if(t.taskState==2 || t.taskState==4){
$out+=' <span class="text-danger">';
$out+=$escape(t.statusText);
$out+='</span> ';
}else{
$out+=' <span class="text-warning">';
$out+=$escape(t.statusText);
$out+='</span> ';
}
$out+=' </td> <td class="v-middle b-l-none"> <span>';
$out+=$escape(_momentFormat(t.completeDate,'YYYY/MM/DD'));
$out+='</span> </td> <td class="v-middle b-l-none">  ';
if(t.taskState === 7){
$out+=' <span class="text-danger">';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}else if(t.taskState ===3){
$out+=' <span class="text-success">';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}else if(t.taskState ===2){
$out+=' <span class="text-danger">';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}else{
$out+=' <span>';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}
$out+=' <!--';
if(t.taskStatus==2 && (isOrgManager==1 || isAssistant==1)){
$out+=' <a href="javascript:void(0);" data-action="publishTask" class="icon-publish-task" style="display: none;"> <i class="fa fa-flag"></i> </a> ';
}
$out+='--> </td> </tr> ';
});
$out+=' ';
if(!(taskIssueList && taskIssueList.length>0)){
$out+=' <tr class="no-data-tr"> <td colspan="9" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">您还没有相关签发任务</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> </form>';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_list_add',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,allCompanyList=$data.allCompanyList,$each=$utils.$each,s=$data.s,$index=$data.$index,currentCompanyId=$data.currentCompanyId,departList=$data.departList,d=$data.d,$out='';$out+='<tr class="row-edit edit-box"> <td class="b-r-none "></td> <td class="b-r-none b-l-none"></td> <td class="b-r-none b-l-none" colspan="2"> <input placeholder="设计任务" class="form-control" type="text" name="taskName" style="width: 80%;display: inline-block;"> <span><span class="wordCount">0</span>/<span>50</span></span> </td> <td class="b-r-none b-l-none"> <div class="btn-group org-select-box"> <button class="btn btn-default btn-sm dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <span class="company-name" data-company-id="';
$out+=$escape(allCompanyList[0].id);
$out+='" data-depart-id=""> ';
$out+=$escape(allCompanyList==null || allCompanyList.length==0?'':allCompanyList[0].companyName);
$out+=' </span> <span class="caret"></span> </button> <ul class="dropdown-menu" style="max-height: 155px;overflow-y: auto;overflow-x: hidden;"> ';
$each(allCompanyList,function(s,$index){
$out+=' <li> <a class="';
$out+=$escape(currentCompanyId == s.id && departList!=null && departList.length>1?'p-r-lg':'');
$out+='" href="javascript:void(0);" data-action="choseOrg" data-type="';
$out+=$escape(s.companyType);
$out+='" data-chose-type="company" data-company-id="';
$out+=$escape(s.id);
$out+='">';
$out+=$escape(s.companyName);
$out+='</a> ';
if(currentCompanyId == s.id && departList!=null && departList.length>1){
$out+=' <a class="open-depart-btn" title="选择部门"> <i class="fa fa-angle-double-down"></i> </a> <ul class="dropdown-menu"> ';
$each(departList,function(d,$index){
$out+=' ';
if(currentCompanyId!=d.id){
$out+=' <li> <a href="javascript:void(0);" data-action="choseOrg" data-chose-type="depart" data-company-id="';
$out+=$escape(d.id);
$out+='">';
$out+=$escape(d.departName);
$out+='</a> </li> ';
}
$out+=' ';
});
$out+=' </ul> ';
}
$out+=' </li> ';
});
$out+=' </ul> </div> </td> <td class="b-r-none b-l-none" colspan="2"> <div class="input-group dp-inline-block"> <input type="text" class="form-control timeInput startTime input-sm" id="ipt_startTime" name="startTime" data-appointmentStartTime = "" placeholder="开始日期" readonly onFocus="startTimeFun(this,m_inputProcessTime_onpicked)" value="" style="width: 110px;"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> <div class="input-group dp-inline-block"> <input type="text" class="form-control timeInput endTime input-sm" id="ipt_endTime" name="endTime" data-appointmentEndTime = "" placeholder="结束日期" readonly onFocus="endTimeFun(this,m_inputProcessTime_onpicked)" value="" style="width: 110px;"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div> </td> <td></td> <td class="b-l-none"> <button class="btn btn-primary btn-sm" data-action="submit">创建</button> <button class="btn btn-default btn-sm" data-action="cancel">取消</button> </td> </tr> <tr class="row-edit edit-box"> <td class="b-r-none b-t-none" colspan="2"></td> <td class="b-r-none b-l-none b-t-none v-middle" colspan="7"> <input placeholder="任务描述" class="form-control" type="text" name="taskRemark" > </td> <td class="b-l-none b-t-none"></td> </tr> ';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_list_changeTime',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,issuePlanList=$data.issuePlanList,p=$data.p,i=$data.i,$escape=$utils.$escape,$out='';$out+='<div class="ibox"> <div class="ibox-content"> <div class="row"> ';
$each(issuePlanList,function(p,i){
$out+=' <div class="col-md-12"> <span>';
$out+=$escape(p.fromCompanyName);
$out+='</span> <span> > </span> <span>';
$out+=$escape(p.companyName);
$out+='</span> <span>';
$out+=$escape(p.planStartTime);
$out+=' ';
if(p.changeTime){
$out+=' <a href="javascript:void(0);" data-action="viewProgressChange" id="viewPlanTimeByTask1';
$out+=$escape(p.id);
$out+='" data-i="';
$out+=$escape(i);
$out+='" data-id="';
$out+=$escape(p.id);
$out+='" data-pid="';
$out+=$escape(p.taskPid);
$out+='" data-company-id="';
$out+=$escape(p.companyId);
$out+='" data-publish-id="';
$out+=$escape(p.publishId);
$out+='"><i class="fa fa-info-circle"></i></a> ';
}
$out+=' </span> <span>-</span> <span>';
$out+=$escape(p.planEndTime);
$out+=' ';
if(p.changeTime){
$out+=' <a href="javascript:void(0);" data-action="viewProgressChange" id="viewPlanTimeByTask2';
$out+=$escape(p.id);
$out+='" data-i="';
$out+=$escape(i);
$out+='" data-id="';
$out+=$escape(p.id);
$out+='" data-pid="';
$out+=$escape(p.taskPid);
$out+='" data-company-id="';
$out+=$escape(p.companyId);
$out+='" data-publish-id="';
$out+=$escape(p.publishId);
$out+='"><i class="fa fa-info-circle"></i></a> ';
}
$out+=' </span> <span> ';
if(p.taskState==1){
$out+=' 进行中 ';
}else if(p.taskState==2){
$out+=' 超时进行 ';
}else if(p.taskState==3){
$out+=' 已完成 ';
}else if(p.taskState==4){
$out+=' 超时完成 ';
}else if(p.taskState==5){
$out+=' 未开始 ';
}
$out+=' </span> </div> ';
});
$out+=' </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_list_editOrg',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,selectedCompanyId=$data.selectedCompanyId,selectedDepartId=$data.selectedDepartId,selectedCompanyText=$data.selectedCompanyText,$each=$utils.$each,allCompanyList=$data.allCompanyList,s=$data.s,$index=$data.$index,currentCompanyId=$data.currentCompanyId,departList=$data.departList,d=$data.d,$out='';$out+='<div class="btn-group edit-box reign-edit-box edit-org-box"> <button class="btn btn-default btn-sm dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <span class="company-name" data-company-id="';
$out+=$escape(selectedCompanyId);
$out+='" data-depart-id="';
$out+=$escape(selectedDepartId==null?'':selectedDepartId);
$out+='"> ';
$out+=$escape(selectedCompanyText);
$out+=' </span> <span class="caret"></span> </button> <ul class="dropdown-menu" style="max-height: 155px;overflow-y: auto;overflow-x: hidden;"> ';
$each(allCompanyList,function(s,$index){
$out+=' <li> <a class="';
$out+=$escape(currentCompanyId == s.id && departList!=null && departList.length>1?'p-r-lg':'');
$out+='" href="javascript:void(0);" data-action="choseOrg" data-type="';
$out+=$escape(s.companyType);
$out+='" data-chose-type="company" data-company-id="';
$out+=$escape(s.id);
$out+='">';
$out+=$escape(s.companyName);
$out+='</a> ';
if(currentCompanyId == s.id && departList!=null && departList.length>1){
$out+=' <a class="open-depart-btn no-padding" title="选择部门"> <i class="fa fa-angle-double-down"></i> </a> <ul class="dropdown-menu"> ';
$each(departList,function(d,$index){
$out+=' ';
if(currentCompanyId!=d.id){
$out+=' <li> <a href="javascript:void(0);" data-action="choseOrg" data-chose-type="depart" data-company-id="';
$out+=$escape(d.id);
$out+='">';
$out+=$escape(d.departName);
$out+='</a> </li> ';
}
$out+=' ';
});
$out+=' </ul> ';
}
$out+=' </li> ';
});
$out+=' </ul> </div> ';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_list_editTime',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,placeholder=$data.placeholder,textVal=$data.textVal,$out='';$out+='<div class="input-group edit-box reign-edit-box"> <input type="text" class="form-control input-sm timeInput" id="ipt_time" name="ipt_time" placeholder="';
$out+=$escape(placeholder);
$out+='" value="';
$out+=$escape(textVal);
$out+='" > <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" style="height: 28px;line-height: 28px;"></i> </span> </div>';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_list_old',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,taskIssueList=$data.taskIssueList,isOrgManager=$data.isOrgManager,isAssistant=$data.isAssistant,currentCompanyId=$data.currentCompanyId,projectCompanyId=$data.projectCompanyId,$each=$utils.$each,t=$data.t,ti=$data.ti,$escape=$utils.$escape,_momentFormat=$helpers._momentFormat,_isNullOrBlank=$helpers._isNullOrBlank,_timeDifference=$helpers._timeDifference,_url=$helpers._url,$out='';$out+='<form class="m_taskIssue_list task-list-box"> <table class="tree table table-bordered table-striped tree-box"> <thead> <tr> <th width="3%" class="b-r-none"> ';
if(taskIssueList!=null && taskIssueList.length>0 && (isOrgManager==1 || isAssistant==1)){
$out+=' <div class="list-check-box"> <label class="i-checks fw-normal m-b-none"> <input name="taskAllCk" type="checkbox" /> <span class="i-checks-span"></span> </label> </div> ';
}
$out+=' </th> <th width="3%" class="b-r-none b-l-none"> <div class="list-action-box"> ';
if(isOrgManager==1 || isAssistant==1){
$out+=' <div class="btn-group batchOperation" id="batchAllOperation"> <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <span class="ic-operation-batch"></span> </button> <ul class="dropdown-menu"> <li> <a href="javascript:void(0);" data-action="batchPublishTask" class="">批量发布任务</a> </li> <li> <a href="javascript:void(0);" class="" data-action="batchDelTask" data-canbedelete="">批量删除</a> </li> </ul> </div> ';
}
$out+=' </div> </th> <th width="24%" class="b-r-none b-l-none v-middle" style="padding-left: 38px;"> <span>设计任务</span> ';
if(isOrgManager==1 && currentCompanyId == projectCompanyId){
$out+=' <button class="btn btn-link" data-action="addDesignTask" title="添加设计任务"><i class="fa fa-plus"></i></button> ';
}
$out+=' </th> <th width="5%" class="b-r-none">任务描述</th> <th width="15%" class="b-r-none v-middle">设计组织</th> <th width="20%" class="b-r-none v-middle">计划进度</th> <th width="10%" class="b-r-none v-middle">进度提示</th> <th width="10%" class="b-r-none v-middle">完成时间</th> <th width="10%" class="v-middle">任务状态</th> </tr> </thead> <tbody> ';
$each(taskIssueList,function(t,ti){
$out+=' <tr class="tree-box-tr treegrid-';
$out+=$escape(t.id);
$out+=' ';
if(t.taskPid!=null && t.taskPid!=''){
$out+='treegrid-parent-';
$out+=$escape(t.taskPid);
}
$out+=' ';
$out+=$escape(t.taskState=='3'||t.taskState=='4'?'completeDate-tr':'');
$out+='" data-i="';
$out+=$escape(ti);
$out+='" data-id="';
$out+=$escape(t.id);
$out+='" data-pid="';
$out+=$escape(t.taskPid);
$out+='" data-company-id="';
$out+=$escape(t.companyId);
$out+='" data-publish-id="';
$out+=$escape(t.publishId);
$out+='"> <td class="v-middle b-r-none l-h-14"> <div class="list-check-box"> <label class="i-checks fw-normal m-b-none"> <input name="taskCk" type="checkbox" /> <span class="i-checks-span"></span> </label> </div> </td> <td class="b-r-none b-l-none"> <div class="list-action-box"> <div class="btn-group singleOperation" style="display: none;"> <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <span class="ic-operation"></span> </button> <ul class="dropdown-menu"> ';
if(t.addChild && (isOrgManager==1 || isAssistant==1)){
$out+=' <li> <a href="javascript:void(0);" data-action="addSubTask">添加子任务</a> </li> ';
}
$out+=' ';
if(t.taskStatus==2 && (isOrgManager==1 || isAssistant==1)){
$out+=' <li> <a href="javascript:void(0);" data-action="publishTask">发布任务</a> </li> ';
}
$out+='  ';
if(!t.first && (isOrgManager==1 || isAssistant==1)){
$out+=' <li> <a href="javascript:void(0);" data-action="moveUp">向上移动</a> </li> ';
}
$out+='  ';
if(!t.last && (isOrgManager==1 || isAssistant==1)){
$out+=' <li> <a href="javascript:void(0);" data-action="moveDown">向下移动</a> </li> ';
}
$out+=' ';
if(t.canBeDelete && (isOrgManager==1 || isAssistant==1)){
$out+=' <li> <a href="javascript:void(0);" data-action="delTask" data-canbedelete="';
$out+=$escape(t.canBeDelete?'1':'0');
$out+='">删除</a> </li> ';
}
$out+=' </ul> </div> ';
if((isOrgManager==1 || isAssistant==1) && t.isHasChild==1){
$out+=' <div class="btn-group batchOperation" style="display: none;"> <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> <span class="ic-operation-batch"></span> </button> <ul class="dropdown-menu"> <li> <a href="javascript:void(0);" data-action="batchPublishTask" >批量发布任务</a> </li> <li> <a href="javascript:void(0);" data-action="batchDelTask" data-canbedelete="';
$out+=$escape(t.canBeDelete?'1':'0');
$out+='">批量删除</a> </li> </ul> </div> ';
}
$out+=' </div> </td> <td class="treeTd pt-relative v-middle b-r-none b-l-none" height="40" > <!--';
if(t.taskState=='3'||t.taskState=='4'){
$out+='<span class="pull-left"><i class="fa fa-check fc-v1-green"></i></span>';
}
$out+='--> <span class="show-span taskName pt-relative" data-string="';
$out+=$escape(t.taskName);
$out+='">';
$out+=$escape(t.taskName);
$out+='</span> ';
if(t.issuePlanList!=null && t.issuePlanList.length>0 ){
$out+=' <a class="tree-td-a" href="javascript:void(0);" data-action="viewPlanTime" id="viewPlanTime';
$out+=$escape(t.id);
$out+='"> <i class="fa fa-star-half-empty"></i> </a> ';
}
$out+=' ';
if(t.canBeEdit && (isOrgManager==1 || isAssistant==1)){
$out+=' <a class="tree-td-a" href="javascript:void(0);" data-action="taskNameEdit" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </td> <td class="b-r-none v-middle"> ';
if(t.taskRemark==null || t.taskRemark==''){
$out+=' ';
if(t.canBeEdit && (isOrgManager==1 || isAssistant==1)){
$out+=' <span class="fc-ccc show-span">未设置</span> ';
}else{
$out+=' <span class="fc-ccc">--</span> ';
}
$out+=' ';
}else{
$out+=' <span class="edit-span-box wh-16" > <a data-action="viewTaskRemarkEdit" id="viewTaskRemarkEdit';
$out+=$escape(ti);
$out+='"> <i class="ic-describe"></i></a> </span> ';
}
$out+=' ';
if(t.canBeEdit && (isOrgManager==1 || isAssistant==1)){
$out+=' <span class="edit-span-box wh-16"> <a href="javascript:void(0);" data-action="taskRemarkEdit" id="taskRemarkEdit';
$out+=$escape(ti);
$out+='" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> </span> ';
}
$out+=' </td> <td class="v-middle b-r-none "> <span class="show-span" data-company-id="';
$out+=$escape(t.companyId);
$out+='" data-depart-id="';
$out+=$escape(t.departId);
$out+='"> ';
if(t.departName!=null && t.departName!=''){
$out+=' ';
$out+=$escape(t.departName);
$out+=' <i class="iconfont rounded icon-2fengongsi m-r-xs" data-toggle="tooltip" data-original-title="';
$out+=$escape(t.companyName);
$out+='"></i> ';
}else{
$out+=' ';
$out+=$escape(t.companyName);
$out+=' ';
}
$out+=' </span> ';
if(t.issueLevel<2 && t.taskStatus == 2 && (isOrgManager==1 || isAssistant==1)){
$out+=' <a class="" href="javascript:void(0);" data-action="choseDesignOrg" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </td> <td class="v-middle b-r-none "> <span class="show-span" data-type="planTime" data-start-time="';
$out+=$escape(t.planStartTime);
$out+='" data-end-time="';
$out+=$escape(t.planEndTime);
$out+='"> ';
if((t.planStartTime==null || t.planStartTime=='') && (t.planEndTime==null || t.planEndTime=='')){
$out+=' <span class="fc-ccc">未设置</span> ';
}else{
$out+=' ';
$out+=$escape(_momentFormat(t.planStartTime,'YYYY/MM/DD'));
$out+=' - ';
$out+=$escape(_momentFormat(t.planEndTime,'YYYY/MM/DD'));
$out+=' ';
if(_isNullOrBlank(t.planStartTime) || _isNullOrBlank(t.planEndTime)){
$out+=' | 共 - 天 ';
}else{
$out+=' | 共 ';
$out+=$escape(_timeDifference(t.planStartTime,t.planEndTime));
$out+=' 天 ';
}
$out+=' ';
if(t.changeTime && t.taskState!=7){
$out+=' <a href="javascript:void(0);" data-action="viewProgressChange" id="viewProgressByChange1';
$out+=$escape(t.id);
$out+='"><i class="fa fa-info-circle"></i></a> ';
}
$out+=' ';
}
$out+=' </span> ';
if((t.fromCompanyId==currentCompanyId || (t.fromCompanyId==null && t.companyId == currentCompanyId)) && (isOrgManager==1 || isAssistant==1)){
$out+=' <a href="javascript:void(0);" data-action="startTimeEdit" data-status="';
$out+=$escape(t.taskStatus);
$out+='" data-start-time="';
$out+=$escape(t.planStartTime);
$out+='" data-end-time="';
$out+=$escape(t.planEndTime);
$out+='" data-deal-type="edit" style="display: none;"> <i class="ic-edit"></i> </a> ';
}
$out+=' </td> <td> ';
if(t.taskState==2 || t.taskState==4){
$out+=' <span class="text-danger">';
$out+=$escape(t.statusText);
$out+='</span> ';
}else{
$out+=' <span class="text-warning">';
$out+=$escape(t.statusText);
$out+='</span> ';
}
$out+=' </td> <td class="v-middle b-l-none"> <span>';
$out+=$escape(_momentFormat(t.completeDate,'YYYY/MM/DD'));
$out+='</span> </td> <td class="v-middle b-l-none">  ';
if(t.taskState === 7){
$out+=' <span class="text-danger">';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}else if(t.taskState ===3){
$out+=' <span class="text-success">';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}else if(t.taskState ===2){
$out+=' <span class="text-danger">';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}else{
$out+=' <span>';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}
$out+=' <!--';
if(t.taskStatus==2 && (isOrgManager==1 || isAssistant==1)){
$out+=' <a href="javascript:void(0);" data-action="publishTask" class="icon-publish-task" style="display: none;"> <i class="fa fa-flag"></i> </a> ';
}
$out+='--> </td> </tr> ';
});
$out+=' ';
if(!(taskIssueList && taskIssueList.length>0)){
$out+=' <tr class="no-data-tr"> <td colspan="9" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">您还没有相关签发任务</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> </form>';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_org',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,isShowTips=$data.isShowTips,departBCompany=$data.departBCompany,isRoleCompany=$data.isRoleCompany,$escape=$utils.$escape,isView=$data.isView,currentCompanyUserId=$data.currentCompanyUserId,taskIssueOrgList=$data.taskIssueOrgList,$each=$utils.$each,t=$data.t,$index=$data.$index,$out='';$out+='<style> table.partB{border-right:0px;margin-bottom: 0px;} table.partB,table.partB tbody,table.partB tbody tr,table.partB tbody tr td{border-bottom: 0px} </style> ';
if(isShowTips==1){
$out+=' <div class="alert alert-warning alert-dismissable"> <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button> 如若要进行生产安排，请先指定设计负责人！ </div> ';
}
$out+=' ';
if(departBCompany !=null && isRoleCompany){
$out+=' <table class="table table-bordered tree-box partB" style="width: 12%;float: left;border-right:0px;"> <tbody> <tr class="" > <td align="center" height="40" class="gray-bg font-bold" style="border-right:0px; ">乙方</td> </tr> </tbody> </table> <table class="table table-bordered tree-box partB" style="width: 88%;float: left;"> <tbody> <tr class="" > <td width="32%" height="40" class="treeTd"> <span class="companyName" data-string="';
$out+=$escape(departBCompany.companyName);
$out+='"> &nbsp;';
$out+=$escape(departBCompany.companyName);
$out+=' </span> </td> <td width="14%" align="center" class="gray-bg font-bold">经营负责人</td> <td width="20%"> <div class="full-width l-h-20"> ';
if(departBCompany.isUpdateOperator==1 && isView == false){
$out+=' <span data-action="changeOperatorPerson" data-i="0" data-id="';
$out+=$escape(departBCompany.operatorPersonId);
$out+='" data-company-id="';
$out+=$escape(departBCompany.id);
$out+='" class="pull-left">';
$out+=$escape(departBCompany.operatorPersonName);
$out+='</span> <!--<a href="javascript:void(0)" data-action="changeOperatorPerson" data-i="0" data-id="';
$out+=$escape(departBCompany.operatorPersonId);
$out+='" data-company-id="';
$out+=$escape(departBCompany.id);
$out+='" class="pull-right showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="';
$out+=$escape(departBCompany.operatorPersonId==currentCompanyUserId?'更换':'更换');
$out+='经营负责人"> <i class="fa fa-retweet"></i> </a>--> ';
}else{
$out+=' ';
$out+=$escape(departBCompany.operatorPersonName);
$out+=' ';
}
$out+=' </div> </td> <td width="14%" align="center" class="gray-bg font-bold">设计负责人</td> <td width="20%"> <div class="full-width l-h-20"> ';
if(departBCompany.isUpdateDesign==1 && isView == false){
$out+=' <span data-action="changeManagerPerson" data-i="0" data-id="';
$out+=$escape(departBCompany.designPersonId);
$out+='" data-company-id="';
$out+=$escape(departBCompany.id);
$out+='" class="pull-left"> ';
$out+=$escape(departBCompany.designPersonName==null?'未设置':departBCompany.designPersonName);
$out+=' </span> <!--';
if(departBCompany.designPersonName!=null){
$out+=' <a href="javascript:void(0)" data-action="changeManagerPerson" data-i="0" data-id="';
$out+=$escape(departBCompany.designPersonId);
$out+='" data-company-id="';
$out+=$escape(departBCompany.id);
$out+='" class="pull-right showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="更换设计负责人"> <i class="fa fa-retweet"></i> </a> ';
}
$out+='--> ';
}else{
$out+=' ';
$out+=$escape(departBCompany.designPersonName);
$out+=' ';
}
$out+=' </div> </td> </tr> </tbody> </table> ';
}
$out+=' <table class="table table-bordered tree-box" style="width: 12%;float: left;border-right:0px;"> <tbody> <tr class="" > <td align="center" height="40" class="gray-bg font-bold" style="border-right:0px; ">立项组织</td> </tr> ';
if(taskIssueOrgList.length>1){
$out+=' <tr class="" > <td align="center" height="';
$out+=$escape((taskIssueOrgList.length-1)*40);
$out+='" class="gray-bg font-bold" style="vertical-align: middle;border-right:0px; ">设计组织</td> </tr> ';
}
$out+=' </tbody> </table> <table class="tree table no-treegrid-expander table-bordered tree-box" style="width: 88%;float: left;"> <tbody> ';
$each(taskIssueOrgList,function(t,$index){
$out+=' ';
if(t!=null){
$out+=' <tr class="tree-box-tr treegrid-';
$out+=$escape(t.treeId);
$out+=' ';
if(t.pid!=null && t.pid!=''){
$out+='treegrid-parent-';
$out+=$escape(t.pid);
}
$out+='" data-i="';
$out+=$escape($index);
$out+='"> <td width="32%" height="40" class="treeTd"> <span class="companyName" data-string="';
$out+=$escape(t.companyName);
$out+='"> &nbsp;';
$out+=$escape(t.companyName);
$out+=' <!--';
if(t.type==0){
$out+=' <i class="icon iconfont icon-hehuoren color-dark-blue f-s-20" ></i> ';
}
$out+='--> </span> </td> <td width="14%" align="center" class="gray-bg font-bold">经营负责人</td> <td width="20%"> <div class="full-width l-h-20"> ';
if(t.isUpdateOperator==1 && isView == false){
$out+=' <span data-action="changeOperatorPerson" data-i="0" data-id="';
$out+=$escape(t.operatorPersonId);
$out+='" data-user-name="';
$out+=$escape(t.operatorPersonName);
$out+='" data-company-id="';
$out+=$escape(t.id);
$out+='" class="pull-left">';
$out+=$escape(t.operatorPersonName);
$out+='</span> <!--<a href="javascript:void(0)" data-action="changeOperatorPerson" data-i="0" data-id="';
$out+=$escape(t.operatorPersonId);
$out+='" data-user-name="';
$out+=$escape(t.operatorPersonName);
$out+='" data-company-id="';
$out+=$escape(t.id);
$out+='" class="pull-right showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="';
$out+=$escape(t.operatorPersonId==currentCompanyUserId?'更换':'更换');
$out+='经营负责人"> <i class="fa fa-retweet"></i> </a>--> ';
}else{
$out+=' ';
$out+=$escape(t.operatorPersonName);
$out+=' ';
}
$out+=' </div> </td> <td width="14%" align="center" class="gray-bg font-bold">设计负责人</td> <td width="20%"> <div class="full-width l-h-20"> ';
if(t.isUpdateDesign==1 && isView == false){
$out+=' <span data-action="changeManagerPerson" data-i="0" data-id="';
$out+=$escape(t.designPersonId);
$out+='" data-user-name="';
$out+=$escape(t.designPersonName);
$out+='" data-company-id="';
$out+=$escape(t.id);
$out+='" class="pull-left"> ';
$out+=$escape(t.designPersonName==null?'未设置':t.designPersonName);
$out+=' </span> <!--';
if(t.designPersonName!=null){
$out+=' <a href="javascript:void(0)" data-action="changeManagerPerson" data-i="0" data-id="';
$out+=$escape(t.designPersonId);
$out+='" data-user-name="';
$out+=$escape(t.designPersonName);
$out+='" data-company-id="';
$out+=$escape(t.id);
$out+='" class="pull-right showTooltip" data-placement="top" data-toggle="tooltip" data-original-title="更换设计负责人"> <i class="fa fa-retweet"></i> </a> ';
}
$out+='--> ';
}else{
$out+=' ';
$out+=$escape(t.designPersonName);
$out+=' ';
}
$out+=' </div> </td> </tr> ';
}
$out+=' ';
});
$out+=' </tbody> </table> <div class="clearfix"></div>';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_overview',' <div id="taskIssueOverviewList" > </div>  ');/*v:1*/
template('m_taskIssue/m_taskIssue_overview_header',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,projectName=$data.projectName,projectCreateByName=$data.projectCreateByName,operatorPersonName=$data.operatorPersonName,designPersonName=$data.designPersonName,$out='';$out+='<div class="col-md-6 "> <h4>项目：';
$out+=$escape(projectName);
$out+='</h4> </div> <div class="col-md-6 text-right"> <h4> 立项人：<span class="text-info">';
$out+=$escape(projectCreateByName);
$out+='</span>&nbsp;&nbsp; 经营负责人：<span class="text-info">';
$out+=$escape(operatorPersonName==null?'未设置':operatorPersonName);
$out+='</span>&nbsp;&nbsp; 设计负责人：<span class="text-info">';
$out+=$escape(designPersonName==null?'未设置':designPersonName);
$out+='</span> </h4> </div>';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_overview_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,taskIssueList=$data.taskIssueList,t=$data.t,ti=$data.ti,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<form style="padding-top: 1px;"> <table class="tree table table-bordered tree-box"> <thead> <tr> <th width="40%" class="b-r-none" style="padding-left: 38px;">设计任务</th> <th width="20%" class="b-r-none b-l-none">设计组织</th> <th width="14%" class="b-r-none b-l-none">开始时间</th> <th width="14%" class="b-r-none b-l-none">结束时间</th> <th width="12%" class="b-l-none">任务状态</th> </tr> </thead> <tbody> ';
$each(taskIssueList,function(t,ti){
$out+=' <tr class="tree-box-tr treegrid-';
$out+=$escape(t.taskId);
$out+=' ';
if(t.taskPid!=null && t.taskPid!=''){
$out+='treegrid-parent-';
$out+=$escape(t.taskPid);
}
$out+='" data-i="';
$out+=$escape(ti);
$out+='" data-id="';
$out+=$escape(t.taskId);
$out+='" data-pid="';
$out+=$escape(t.taskPid);
$out+='" data-company-id="';
$out+=$escape(t.companyId);
$out+='" data-publish-id="';
$out+=$escape(t.publishId);
$out+='"> <td class="treeTd pt-relative v-middle b-r-none" height="40" > ';
if(t.taskState=='3'||t.taskState=='4'){
$out+='<i class="fa fa-check fc-v1-green"></i>';
}
$out+=' <span class="show-span taskName pt-relative" data-string="';
$out+=$escape(t.taskName);
$out+='">';
$out+=$escape(t.taskName);
$out+='</span> ';
if(t.issuePlanList!=null && t.issuePlanList.length>0){
$out+=' <a class="tree-td-a" href="javascript:void(0);" data-action="viewPlanTime" id="viewPlanTime';
$out+=$escape(t.taskId);
$out+='"><i class="fa fa-star-half-empty"></i></a> ';
}
$out+=' </td> <td class="v-middle b-r-none b-l-none"> <sapn class="show-span" data-id="';
$out+=$escape(t.id);
$out+='" data-p-id="';
$out+=$escape(t.taskPid);
$out+='" data-depart-id="';
$out+=$escape(t.departId);
$out+='"> ';
if(t.departName!=null && t.departName!=''){
$out+=' ';
$out+=$escape(t.departName);
$out+=' <i class="iconfont rounded icon-2fengongsi m-r-xs" data-toggle="tooltip" data-original-title="';
$out+=$escape(t.companyName);
$out+='"></i> ';
}else{
$out+=' ';
$out+=$escape(t.companyName);
$out+=' ';
}
$out+=' </sapn> </td> <td class="v-middle b-r-none b-l-none"> <span class="show-span"> ';
if(t.planStartTime==null || t.planStartTime==''){
$out+=' <span class="fc-ccc">--</span> ';
}else{
$out+=' ';
$out+=$escape(t.planStartTime);
$out+=' ';
}
$out+=' </span> </td> <td class="v-middle b-r-none b-l-none"> <span class="show-span"> ';
if(t.planEndTime==null || t.planEndTime==''){
$out+=' <span class="fc-ccc">--</span> ';
}else{
$out+=' ';
$out+=$escape(t.planEndTime);
$out+=' ';
}
$out+=' </span> </td> <td class="v-middle b-l-none">  ';
if(t.taskState === 7){
$out+=' <span class="text-danger">';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}else if(t.stateHtml ===3){
$out+=' <span class="text-success">';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}else if(t.stateHtml ===2){
$out+=' <span class="text-danger">';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}else{
$out+=' <span>';
$out+=$escape(t.stateHtml);
$out+='</span> ';
}
$out+=' </td> </tr> ';
});
$out+=' ';
if(!(taskIssueList && taskIssueList.length>0)){
$out+=' <tr> <td colspan="5" class="text-center v-middle"> <div class="m-b-xl m-t-md"> <img src="';
$out+=$escape(_url('/assets/img/default/without_data.png'));
$out+='"> <span class="fc-dark-blue dp-block">没有相关签发任务</span> </div> </td> </tr> ';
}
$out+=' </tbody> </table> </form>';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_overview_list_relatedTime',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,issuePlanList=$data.issuePlanList,p=$data.p,i=$data.i,$escape=$utils.$escape,$out='';$out+='<div class="ibox"> <div class="ibox-content"> <div class="row"> ';
$each(issuePlanList,function(p,i){
$out+=' <div class="col-md-12"> <span>';
$out+=$escape(p.fromCompanyName);
$out+='</span> <span> > </span> <span>';
$out+=$escape(p.companyName);
$out+='</span> <span>';
$out+=$escape(p.planStartTime);
$out+='</span> <span>-</span> <span>';
$out+=$escape(p.planEndTime);
$out+='</span> <span> ';
if(p.taskState==1){
$out+=' 进行中 ';
}else if(p.taskState==2){
$out+=' 超时进行 ';
}else if(p.taskState==3){
$out+=' 正常完成 ';
}else if(p.taskState==4){
$out+=' 超时完成 ';
}else if(p.taskState==5){
$out+=' 未开始 ';
}
$out+=' </span> </div> ';
});
$out+=' </div> </div> </div>';
return new String($out);
});/*v:1*/
template('m_taskIssue/m_taskIssue_selection',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,companyUserName=$data.companyUserName,isFirstSetDesign=$data.isFirstSetDesign,allTaskList=$data.allTaskList,$each=$utils.$each,p=$data.p,i=$data.i,t=$data.t,ti=$data.ti,$out='';$out+='<form class="form-horizontal rounded-bottom taskProgressOBox noborder"> <div class="ibox"> <div class="ibox-content"> <ul class="todo-list small-list bg-white"> <li class="design_item"> <label>新的设计负责人：';
$out+=$escape(companyUserName);
$out+='</label> ';
if(!isFirstSetDesign){
$out+=' <p>因你在项目中有以下设计任务，请选择是否进行移交，你可以选择：</p> <p>1.移交，勾选移交任务并点击保存按钮；2.不移交，直接点击保存按钮。</p> ';
}
$out+=' </li> ';
if(allTaskList!=null && allTaskList.length>0){
$out+=' <li class="design_item"> <label class="i-checks dp-inline-block"> <input name="allChoseCk" class="checkbox" type="checkbox" value="" /> 全选 </label> </li> ';
}
$out+=' ';
$each(allTaskList,function(p,i){
$out+=' ';
$each(p.taskList,function(t,ti){
$out+=' <li class="design_item" data-i="';
$out+=$escape(ti);
$out+='"> <label class="i-checks dp-inline-block"> <input name="taskCk" class="checkbox" type="checkbox" value="';
$out+=$escape(t.id);
$out+='" /> ';
$out+=$escape(t.taskName);
$out+=' </label> </li> ';
});
$out+=' ';
});
$out+=' </ul> </div> </div> </form> ';
return new String($out);
});/*v:1*/
template('m_teamInfo/m_editServerType',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,serverTypeList=$data.serverTypeList,d=$data.d,$index=$data.$index,serverTypeIndex=$data.serverTypeIndex,$escape=$utils.$escape,$out='';$out+='<style> .form-group.serverTypeBox label.checkbox input{position: absolute;} .form-group.serverTypeBox input[type="text"].form-control{width: 100%;} .form-group.serverTypeBox form{margin: 0;} .form-group.serverTypeBox{margin: 0;} </style> <div class="form-group serverTypeBox p-n" style="width: 340px;"> <form class="sky-form rounded-bottom" > <fieldset class="no-padding"> <div class="row"> ';
$each(serverTypeList,function(d,$index){
$out+=' <div class="col-md-4"> <label class="i-checks fw-normal"> ';
if(serverTypeIndex!=null &&  serverTypeIndex.indexOf(d.id)>-1){
$out+=' <input name="serverType" type="checkbox" checked value="';
$out+=$escape(d.name);
$out+='" data-id="';
$out+=$escape(d.id);
$out+='"/> ';
}else{
$out+=' <input name="serverType" type="checkbox" value="';
$out+=$escape(d.name);
$out+='" data-id="';
$out+=$escape(d.id);
$out+='"/> ';
}
$out+=' ';
$out+=$escape(d.name);
$out+=' </label> </div> ';
});
$out+=' </div> </fieldset> </form> </div>';
return new String($out);
});/*v:1*/
template('m_teamInfo/m_teamDissolution',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,teamInfo=$data.teamInfo,$out='';$out+='<form class="sky-form rounded-bottom teamDissolutionOBox p-lg" style="border-bottom: none;"> <input type="password" class="dp-none"> <div class="space-md-hor content-xs"> <div class="col-md-12 margin-bottom-20"> <h5 >请校验以下信息，如确认无误请输入当前账户密码解散组织。</h5> <h3 class="text-center margin-top-20">';
$out+=$escape(teamInfo.companyName);
$out+='</h3> </div> <div class="form-group col-md-6"> <label>管理员姓名： <b>';
$out+=$escape(teamInfo.userName);
$out+='</b></label> </div> <div class="form-group col-md-6"> <label>手机号码： <b>';
$out+=$escape(teamInfo.cellphone);
$out+='</b></label> </div> <div class="form-group col-md-12"> <label>当前用户密码：</label> <input type="password" class="form-control password" name="password"> </div> <div class="clearfix"></div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_teamInfo/m_teamInfoEdit',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,teamInfo=$data.teamInfo,$each=$utils.$each,serverTypeList=$data.serverTypeList,server=$data.server,a=$data.a,$out='';$out+='<form class="form-horizontal" style=""> <div class="form-group"> <label class="col-sm-2 control-label">组织名称<span class="color-red">*</span></label> <div class="col-sm-10"> <input class="form-control" id="companyName" name="companyName" value="';
$out+=$escape(teamInfo.companyName);
$out+='" type="text" placeholder="组织名称用于外部显示"> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">所在地区<span class="color-red">*</span></label> <div class="col-sm-10 choseProvinceCity" id="city_1"> <label class="select dp-inline-block no-padding" style="width: 32.5%;"> <select class="prov form-control" name="province"></select> <i></i> </label> <label class="select dp-inline-block no-padding" style="width: 32.5%;"> <select class="city form-control" name="city" disabled="disabled" style="display: none;"></select> <i style="display: none;"></i> </label> <label class="select dp-inline-block no-padding" style="width: 32.5%;"> <select class="dist form-control" name="county" disabled="disabled" style="display: none;"></select> <i style="display: none;"></i> </label> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">详细地址</label> <label class="col-sm-10"> <input class="form-control" id="companyAddress" name="companyAddress" value="';
$out+=$escape(teamInfo.companyAddress);
$out+='" maxlength="255"> </label> </div> <div class="form-group serviceTypeEdit"> <label class="col-sm-2 control-label">服务类型<span class="color-red">*</span></label> <div class="col-sm-10"> ';
$each(serverTypeList,function(server,a){
$out+=' <div class="col-md-4 serviceType" > <div class="checkbox checkbox-inline" > <input type="checkbox" id="serverType';
$out+=$escape(a);
$out+='" name="serverType" value="';
$out+=$escape(server.id);
$out+='"> <label for="serverType';
$out+=$escape(a);
$out+='"> ';
$out+=$escape(server.name);
$out+='</label> </div> </div> ';
});
$out+=' </div> <span name="severType"></span> </div> <div class="form-group"> <label class="col-sm-2 control-label">联系电话</label> <div class="col-sm-10 input-group"> <input class="form-control" id="companyPhone" name="companyPhone" value="';
$out+=$escape(teamInfo.companyPhone);
$out+='" maxlength="30"> <span class="input-group-addon"><i class="icon-append fa fa-phone"></i></span> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">传真号码</label> <div class="col-sm-10 input-group"> <input class="form-control" id="companyFax" name="companyFax" value="';
$out+=$escape(teamInfo.companyFax);
$out+='" maxlength="20" type="text"> <span class="input-group-addon"><i class="icon-append fa fa-phone"></i></span> </div> </div> <div class="form-group"> <label class="col-sm-2 control-label">电子邮箱</label> <div class="col-sm-10 input-group"> <input class="form-control" type="text" id="companyEmail" name="companyEmail" value="';
$out+=$escape(teamInfo.companyEmail);
$out+='" value="abc" maxlength="50"> <span class="input-group-addon"><i class="icon-append fa fa-envelope"></i></span> </div> </div> <div class="form-group" > <label class="col-sm-2 control-label">组织简介</label> <div class="col-sm-10"> <textarea class="form-control" rows="5" id="companyComment" name="companyComment" maxlength="255">';
$out+=$escape(teamInfo.companyComment);
$out+='</textarea> </div> </div> <footer>  <button type="button" class="pull-right btn-u btn-u-dark-blue rounded" data-action="saveTeamInfo">保存</button> </footer> </form>';
return new String($out);
});/*v:1*/
template('m_teamInfo/m_teamInfoShow',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,teamInfo=$data.teamInfo,$each=$utils.$each,server=$data.server,a=$data.a,$out='';$out+='<form class="form-horizontal m_teamInfoShow"> <div class="form-group"> <label class="col-24-md-5 col-24-lg-3 control-label">组织名称：</label> <div class="col-24-md-19 col-24-lg-21 form-control-static"> <a href="javascript:void(0)" data-action="text_companyName">';
$out+=$escape(teamInfo.companyName);
$out+='</a> </div> </div> <div class="form-group"> <label class="col-24-md-5 col-24-lg-3 control-label">公司地址：</label> <div class="col-24-md-19 col-24-lg-21 form-control-static" id="city_1"> <a href="javascript:void(0);" data-action="edit_address" id="address" class="editable editable-click"> ';
if(teamInfo.province && teamInfo.province!=''){
$out+=' ';
$out+=$escape(teamInfo.province);
$out+='&nbsp; ';
}
$out+=' ';
if(teamInfo.city && teamInfo.city!=''){
$out+=' ';
$out+=$escape(teamInfo.city);
$out+='&nbsp; ';
}
$out+=' ';
if(teamInfo.county && teamInfo.county!=''){
$out+=' ';
$out+=$escape(teamInfo.county);
$out+='&nbsp; ';
}
$out+=' ';
$out+=$escape(teamInfo.companyAddress);
$out+=' </a> </div> </div>   <!--<div class="col-24-md-19 col-24-lg-21 form-control-static">';
$out+=$escape(teamInfo.companyAddress);
$out+='</div>-->  <div class="form-group"> <label class="col-24-md-5 col-24-lg-3 control-label">服务类型：</label> <div class="col-24-md-19 col-24-lg-21 form-control-static"> <a href="javascript:void(0)" data-action="checklist_serverTypeList" class="editable editable-click"> ';
$each(teamInfo.serverTypeList,function(server,a){
$out+=' <span class="serverType m-r">';
$out+=$escape(server.name);
$out+='</span> ';
});
$out+=' </a> </div> </div> <div class="form-group"> <label class="col-24-md-5 col-24-lg-3 control-label">联系电话：</label> <div class="col-24-md-19 col-24-lg-21 form-control-static"> <a href="javascript:void(0)" data-action="text_companyPhone">';
$out+=$escape(teamInfo.companyPhone);
$out+='</a> </div> </div> <div class="form-group"> <label class="col-24-md-5 col-24-lg-3 control-label">传真号码：</label> <div class="col-24-md-19 col-24-lg-21 form-control-static"> <a href="javascript:void(0)" data-action="text_companyFax">';
$out+=$escape(teamInfo.companyFax);
$out+='</a> </div> </div> <div class="form-group"> <label class="col-24-md-5 col-24-lg-3 control-label">电子邮箱：</label> <div class="col-24-md-19 col-24-lg-21 form-control-static"> <a href="javascript:void(0)" data-action="text_companyEmail">';
$out+=$escape(teamInfo.companyEmail);
$out+='</a> </div> </div> <div class="form-group companyComment" > <label class="col-24-md-5 col-24-lg-3 control-label">组织简介：</label> <div class="col-24-md-19 col-24-lg-21 form-control-static" style="min-height: 44px;height: auto;"> <a href="javascript:void(0)" data-action="textarea_companyComment" data-mode="popup">';
$out+=$escape(teamInfo.companyComment);
$out+='</a> </div> <div class="clearfix"></div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_teamInfo/m_teamPicUpload',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,filePath=$data.filePath,isEdit=$data.isEdit,$out='';$out+='<div class="row cube-portfolio pull-right"> <div class="col-md-12 cbp-l-grid-agency cbp-caption-active cbp-caption-zoom cbp-ready"> <div class="thumbnails thumbnail-style cbp-item-wrapper text-align-center" style="box-sizing:border-box;padding:9px;width: 240px;height: 240px; border: solid 1px #eee;"> <div class="cbp-caption text-align-center" style="min-height: 220px;"> <img src="';
$out+=$escape(filePath);
$out+='" class="img-logo" alt="组织logo" style="max-height: 240px;width: 220px;"> ';
if(isEdit){
$out+=' <div class="cbp-caption-activeWrap"> <div class="cbp-l-caption-alignCenter"> <div class="cbp-l-caption-body"> <ul class="link-captions no-bottom-space"> <li> <a href="javascript:void(0)" id="btnUploadImg" title="点击设置LOGO"></a> </li> </ul> </div> </div> </div> ';
}
$out+=' </div> </div> </div> </div> ';
if(isEdit){
$out+=' <div class="clearfix"></div> <div class="row caption pull-right" style="width: 240px;margin-top: 10px;"> <h3>组织LOGO</h3> <p>LOGO用于组织展示，最佳展示像素为200*200，建议上传png、jpg、bmp、jpeg格式的图像文件.</p> </div> ';
}
return new String($out);
});/*v:1*/
template('m_update_tips/m_update_tips',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="m-update-tips"> <div class="m-update-title"> <p>卯丁更新提示</p> </div> <div class="m-update-left"> <p class="f-s-16 l-h-16"> <span class="dp-inline-block" style="width: 28px;"><i class="fc-dark-blue fa fa-info-circle"></i></span> 卯丁权限设置</p> <p class="f-s-14 l-h-28 m-t-xl" style="text-indent: 28px;">由于卯丁版本升级，权限系统有部分调整，为了保证系统功能的正常使用，你需要进行相关设置。</p> <p class="f-s-14 l-h-16 m-t-xl" style="text-indent: 28px;">请根据以下提示进行相关操作：</p> <ul class="m-t-md" style="padding: 15px 20px;"> <li> <p class="f-s-14 l-h-14">企业负责人</p> <p class="f-s-13 l-h-14 fc-aaa">拥有组织中所有查看权限</p> </li> <li> <p style="font-size: 14px;line-height: 14px;">项目收支</p> <p class="f-s-13 l-h-14 fc-aaa">项目中，所有的财务回款操作</p> </li> <li> <p sclass="f-s-14 l-h-14">项目管理</p> <p class="f-s-13 l-h-14 fc-aaa">编辑项目基本信息</p> </li> </ul> </div> <div class="m-update-right"> <img src="';
$out+=$escape(_url('/assets/img/update/update1.png'));
$out+='" style="width: 510px;" /> </div> <div class="clear-both"></div> <div class="full-width" style="padding: 15px 20px;"> <a class="btn btn-primary pull-right" data-action="resetPermission">配置权限</a> </div> </div>';
return new String($out);
});/*v:1*/
template('m_update_tips/m_update_tips1',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="m-update-tips" style="padding: 20px 0 0 20px;"> <div class="m-update-title"> <p>欢迎使用卯丁</p> <p style="font-weight: normal;font-size: 16px;">我们带你快速熟悉卯丁</p> </div> <div class="m-update-left" style="width: 430px;padding: 0 0 0 20px;"> <img src="';
$out+=$escape(_url('/assets/img/update/createOrgTips.png'));
$out+='" style="width: 400px;" /> </div> <div class="m-update-right" style="padding: 0;width: 520px;"> <img src="';
$out+=$escape(_url('/assets/img/update/createOrg.png'));
$out+='" style="width: 520px;" /> </div> <div class="clear-both"></div> <div class="full-width" style="padding: 40px 28px;"> <a class="btn btn-default pull-left" data-action="jumpOutWizard">跳出向导</a> <a class="btn btn-primary pull-right" data-action="nextPage">下一步</a> </div> </div>';
return new String($out);
});/*v:1*/
template('m_update_tips/m_update_tips2',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="m-update-tips" style="padding: 20px 0 0 20px;"> <div class="m-update-title"> <p>欢迎使用卯丁</p> <p style="font-weight: normal;font-size: 16px;">我们带你快速熟悉卯丁</p> </div> <div class="m-update-left" style="width: 430px;padding: 0 0 0 20px;"> <img src="';
$out+=$escape(_url('/assets/img/update/importMemberTips.png'));
$out+='" style="width: 400px;" /> </div> <div class="m-update-right" style="padding: 0;width: 520px;"> <img src="';
$out+=$escape(_url('/assets/img/update/importMember.png'));
$out+='" style="width: 520px;" /> </div> <div class="clear-both"></div> <div class="full-width" style="padding: 20px 28px;"> <a class="btn btn-default pull-left" data-action="jumpOutWizard">跳出向导</a> <a class="btn btn-primary pull-right" data-action="nextPage">下一步</a> <a class="btn btn-default pull-right m-r-sm" data-action="prevPage">上一步</a> </div> </div>';
return new String($out);
});/*v:1*/
template('m_update_tips/m_update_tips3',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="m-update-tips" style="padding: 20px 0 0 20px;"> <div class="m-update-title"> <p>欢迎使用卯丁</p> <p style="font-weight: normal;font-size: 16px;">我们带你快速熟悉卯丁</p> </div> <div class="m-update-left" style="width: 430px;padding: 0 0 0 20px;"> <img src="';
$out+=$escape(_url('/assets/img/update/roleTips.png'));
$out+='" style="width: 400px;" /> </div> <div class="m-update-right" style="padding: 0;width: 520px;"> <img src="';
$out+=$escape(_url('/assets/img/update/role.png'));
$out+='" style="width: 520px;" /> </div> <div class="clear-both"></div> <div class="full-width" style="padding: 20px 28px;"> <a class="btn btn-default pull-left" data-action="jumpOutWizard">跳出向导</a> <a class="btn btn-primary pull-right" data-action="nextPage">下一步</a> <a class="btn btn-default pull-right m-r-sm" data-action="prevPage">上一步</a> </div> </div>';
return new String($out);
});/*v:1*/
template('m_update_tips/m_update_tips4',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="m-update-tips" style="padding: 20px 0 0 20px;"> <div class="m-update-title"> <p>欢迎使用卯丁</p> <p style="font-weight: normal;font-size: 16px;">我们带你快速熟悉卯丁</p> </div> <div class="m-update-left" style="width: 430px;padding: 0 0 0 20px;"> <img src="';
$out+=$escape(_url('/assets/img/update/addProjectTips.png'));
$out+='" style="width: 400px;" /> </div> <div class="m-update-right" style="padding: 0;width: 520px;"> <img src="';
$out+=$escape(_url('/assets/img/update/addProject.png'));
$out+='" style="width: 520px;" /> </div> <div class="clear-both"></div> <div class="full-width" style="padding: 40px 28px;"> <a class="btn btn-default pull-left" data-action="jumpOutWizard">跳出向导</a> <a class="btn btn-primary pull-right" data-action="complete">完成</a> <a class="btn btn-default pull-right m-r-sm" data-action="prevPage">上一步</a> </div> </div>';
return new String($out);
});/*v:1*/
template('m_update_tips/m_update_tips5',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="m-update-tips" style="padding: 20px 0 0 20px;"> <div class="m-update-title"> <p>欢迎使用卯丁</p> <p style="font-weight: normal;font-size: 16px;">我们带你快速熟悉卯丁</p> </div> <div class="m-update-left" style="width: 430px;padding: 15px 0 0 20px;"> <img src="';
$out+=$escape(_url('/assets/img/update/newRoleTips.png'));
$out+='" style="width: 365px;" /> </div> <div class="m-update-right" style="padding: 0;width: 520px;"> <img src="';
$out+=$escape(_url('/assets/img/update/newRole.png'));
$out+='" style="width: 520px;" /> </div> <div class="clear-both"></div> <div class="full-width" style="padding: 40px 28px;"> <a class="btn btn-primary pull-right" data-action="complete">完成</a> </div> </div>';
return new String($out);
});/*v:1*/
template('m_website/m_website_footer',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+=' <div class="g-brd-y g-brd-gray-light-v4"> <div class="container g-pt-100 g-pb-70"> <div class="row justify-content-start"> <div class="col-md-3"> <h2 class="h4 mb-4">产品</h2> <div class="row"> <div class="col-6 g-mb-20">  <ul class="list-unstyled g-font-size-13 mb-0"> <li class="g-my-10"> <a class="u-link-v5 g-color-gray-dark-v4">专业版(敬请期待)</a></li> <li class="g-my-10"> <a class="u-link-v5 g-color-gray-dark-v4 g-color-primary--hover" href="';
$out+=$escape(_url('/home/products'));
$out+='">企业版</a></li> <li class="g-my-10"> <a class="u-link-v5 g-color-gray-dark-v4 g-color-primary--hover" href="';
$out+=$escape(_url('/home/pricing'));
$out+='">产品价格</a></li> </ul>  </div> </div> </div> <div class="col-sm-3 col-md-3"> <h2 class="h4 mb-4">服务</h2> <div class="row"> <div class="col-6 g-mb-20">  <ul class="list-unstyled g-font-size-13 mb-0"> <li class="g-mb-10"> <a class="u-link-v5 g-color-gray-dark-v4 g-color-primary--hover" href="';
$out+=$escape(_url('/home/faq'));
$out+='">帮助中心</a></li> <li class="g-mb-10"> <a class="u-link-v5 g-color-gray-dark-v4 g-color-primary--hover" href="';
$out+=$escape(_url('/home/deployment_guide'));
$out+='">部署指南</a> </li> </ul>  </div> </div> </div> <div class="col-sm-3 col-md-3"> <h2 class="h4 mb-4">保障</h2> <div class="row"> <div class="col-6 g-mb-20">  <ul class="list-unstyled g-font-size-13 mb-0"> <li class="g-mb-10"> <a class="u-link-v5 g-color-gray-dark-v4 g-color-primary--hover" href="';
$out+=$escape(_url('/home/terms'));
$out+='">服务协议</a> </li> <li class="g-my-10"> <a class="u-link-v5 g-color-gray-dark-v4 g-color-primary--hover" href="';
$out+=$escape(_url('/home/security'));
$out+='">安全保障</a></li>       </ul>  </div> </div> </div> <div class="col-sm-3 col-md-3 ml-auto"> <h2 class="h4 mb-4">联系我们</h2>  <ul class="list-unstyled g-color-gray-dark-v4 g-font-size-13"> <li class="media my-3"> <i class="d-flex mt-1 mr-3 icon-hotel-restaurant-235 u-line-icon-pro"></i> <div class="media-body"> 中国 广东 深圳<br>前海深港合作区前湾一路1号A栋201室 </div> </li> <li class="media my-3"> <i class="d-flex mt-1 mr-3 icon-communication-062 u-line-icon-pro"></i> <div class="media-body"> services@imaoding.com </div> </li> <li class="media my-3"> <i class="d-flex mt-1 mr-3 icon-communication-033 u-line-icon-pro"></i> <div class="media-body"> 400-900-6299 </div> </li> </ul>  </div> </div> </div> </div>   <div class="container g-pt-50 g-pb-30"> <div class="row justify-content-between align-items-center"> <div class="col-md-6 g-mb-20"> <p class="g-font-size-13 mb-0">2017 © www.imaoding.com. All Rights Reserved.</p> </div> <div class="col-md-6 text-md-right g-mb-20"> <p><a href="http://www.miitbeian.gov.cn" target="_blank">ICP备案号：粤ICP备16006511号-1</a></p> </div> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_website/m_website_header',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<style> .f-s-16{font-size: 16px;} </style> <div class="u-header__section u-header__section--light g-bg-white g-transition-0_3 g-py-10"> <nav class="js-mega-menu navbar navbar-toggleable-md"> <div class="container">  <button class="navbar-toggler navbar-toggler-right btn g-line-height-1 g-brd-none g-pa-0 g-pos-abs g-right-0" type="button" aria-label="Toggle navigation" aria-expanded="false" aria-controls="navBar" data-toggle="collapse" data-target="#navBar"> <span class="hamburger hamburger--slider"> <span class="hamburger-box"> <span class="hamburger-inner"></span> </span> </span> </button>   <a href="';
$out+=$escape(_url(''));
$out+='" class="navbar-brand"> <img src="';
$out+=$escape(_url('/assets/img/logo.png'));
$out+='" class="g-width-100" alt="卯丁"> </a>   <div class="collapse navbar-collapse align-items-center flex-sm-row g-pt-10 g-pt-5--lg g-mr-40--lg" id="navBar"> <ul class="navbar-nav text-uppercase g-font-weight-600 ml-auto">  <li class="hs-has-mega-menu nav-item g-mx-10--lg g-mx-20--lg" data-animation-in="fadeIn" data-animation-out="fadeOut" data-position="right"> <a id="mega-menu-label-3" class="nav-link g-px-0 f-s-16" href="javascript:void(0);" aria-haspopup="true" aria-expanded="false">登录</a>  <div class="g-width-400 hs-mega-menu u-shadow-v11 g-text-transform-none font-weight-normal g-brd-top g-brd-primary g-brd-top-2 g-mt-17 g-mt-7--lg--scrolling" aria-labelledby="mega-menu-label-3"> <div class="row justify-content-center"> <div class="col-sm-12 col-lg-12"> <div class="g-brd-around g-brd-gray-light-v4 rounded g-py-40 g-px-30" id="loginBox"> </div> </div> </div> </div>  </li> <li class="nav-item g-mx-10--lg g-mx-20--lg f-s-16"> <a class="nav-link" href="';
$out+=$escape(_url('/iWork/sys/register'));
$out+='">注册</a> </li> </ul>  </div>  </div> </nav> </div>';
return new String($out);
});/*v:1*/
template('m_website/m_website_login',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+=' <form id="loginForm" class="g-py-15"> <div class="mb-4"> <label class="g-color-gray-dark-v2 g-font-weight-600 g-font-size-13">卯丁账号:</label> <input id="account" name="account" class="form-control g-color-black g-bg-white g-bg-white--focus g-brd-gray-light-v4 g-brd-primary--hover rounded g-py-15 g-px-15" type="account" placeholder="请输入您注册卯丁使用的手机号码"> </div> <div class="g-mb-35"> <div class="row justify-content-between"> <div class="col align-self-center"> <label class="g-color-gray-dark-v2 g-font-weight-600 g-font-size-13">密码:</label> </div> <div class="col align-self-center text-right"> <a tabindex="-1" class="d-inline-block g-font-size-12 mb-2" href="';
$out+=$escape(_url('/iWork/sys/forgetLoginPwd'));
$out+='">忘记密码?</a> </div> </div> <input id="password" name="password" class="form-control g-color-black g-bg-white g-bg-white--focus g-brd-gray-light-v4 g-brd-primary--hover rounded g-py-15 g-px-15 mb-3" type="password" placeholder="请输入您的卯丁账号密码"> <div class="row justify-content-between"> <div class="col-8 align-self-center"> <label class="form-check-inline u-check g-color-gray-dark-v5 g-font-size-13 g-pl-25 mb-0"> <input class="hidden-xs-up g-pos-abs g-top-0 g-left-0" type="checkbox"> <div class="u-check-icon-checkbox-v6 g-absolute-centered--y g-left-0"> <i class="fa" data-check-icon="&#xf00c"></i> </div> 下次自动登录 </label> </div> <div class="col-4 align-self-center text-right"> <a href="javascript:void(0);" id="btnLogin" class="btn btn-md u-btn-primary rounded g-py-13 g-px-25">登录</a> </div> </div> </div> </form>  <footer class="text-center"> <p class="g-color-gray-dark-v5 g-font-size-13 mb-0">还没有卯丁账号? <a class="g-font-weight-600" href="';
$out+=$escape(_url('/iWork/sys/register'));
$out+='">注册</a></p> </footer>';
return new String($out);
});/*v:1*/
template('m_website/m_website_products',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="g-mb-60"> <h2 class="g-color-black g-font-weight-600 text-center g-mb-30">组织管理</h2> <p><span class="d-inline-block float-left g-width-60 g-height-60 g-color-black g-font-weight-600 g-font-size-30 text-center g-pa-7 mr-2">卯</span>丁支持快速创建多元化的 企业组织构架，支持跨组织项目合作设计。</p> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">01.</span> 组织信息 </h3> <p>卯丁中的组织信息包括组织logo ，组织名称，服务类型、联系电话、传真号码、电子邮箱以及组织简介等.</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/1-1.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 flex-md-unordered g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 组织架构 </h3> <p>卯丁支持各种组织形态，无论是单一组织，拥有分支机构的大中型组织，以及拥有事业合伙人的平台型组织，都可以在系统中快速建立.</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/1-2.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">03.</span> 权限设置</h2> <p>卯丁的权限体系，除了满足组织内部相关事务的权限设置需求外，还针对诸如独立核算，事业合伙人等不同形态的组织关系设置了权限控制，让管理过程中的数据、操作等控制轻松自如.</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/1-3.png'));
$out+='" alt="Image Description"> </div> </div>';
return new String($out);
});/*v:1*/
template('m_website/m_website_products2',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="g-mb-60"> <h2 class="g-color-black g-font-weight-600 text-center g-mb-30">项目管理</h2> <p><span class="d-inline-block float-left g-width-60 g-height-60 g-color-black g-font-weight-600 g-font-size-30 text-center g-pa-7 mr-2">卯</span>丁为设计企业量身定做的项目管理 功能，全程任务驱动体系，实时采集设计数据，管理与生产无缝对接。</p> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">01.</span> 项目立项 </h3> <p>卯丁中的项目立项分成了两个部分，一个是只需要录入项目立项必要信息的简单立项，一个是完成简单立项后，根据项目运行情况，对于项目详细信息补充完善的项目基本信息编辑。</p> <p>在项目基本信息中，您可以对项目的专业信息进行自定义，满足不同项目类型的需求。</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/2-1.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 任务签发 </h3> <p>任务签发作为项目生产的起点，由经营人员设置项目对应合同的内容目标，并结合实际情况，决定完成生产的组织或部门.</p> <p>任务签发的内容，将作为生产安排的起点，交由设计部门进行实际生产安排，经营人员可以很方便的查看到对应的经营任务的相关状态。</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/2-2.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">03.</span> 生产安排</h2> <p>通过生产安排，您可以制定具体的生产计划，包括人员安排，进度计划等.</p> <p>在生产安排中，所有参与人员都将收到所负责的任务，并根据实际情况逐级反馈任务进度。</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/2-3.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">04.</span> 协同设计 </h3> <p>卯丁的协同平台将作为设计人员使用卯丁的主要入口，通过虚拟盘的方式，无缝集成于windows系统中，设计人员就像操作本地磁盘一样，将设计文件有序的存放于系统中，系统自动的根据生产安排情况建立文件夹结构，并赋予不同的操作权限。</p> <p>设计人员通过协同平台，将生产中的状态反馈到卯丁的管理体系当中，让管理人员可以轻松掌握设计状态。</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/img1.png'));
$out+='" alt="Image Description"> </div> </div> ';
return new String($out);
});/*v:1*/
template('m_website/m_website_products3',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="g-mb-60"> <h2 class="g-color-black g-font-weight-600 text-center g-mb-30">财务管理</h2> <p><span class="d-inline-block float-left g-width-60 g-height-60 g-color-black g-font-weight-600 g-font-size-30 text-center g-pa-7 mr-2">卯</span>丁全程记录、自动采集企业各项收支，成本、费用、利润一目了然。</p> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">01.</span> 收支明细 </h3> <p>卯丁中的财务收支总览，可以按照时间跨度方便的查看组织的收支明细，关联组织、关联项目等信息，让您可以轻松明确收支的具体内容，灵活的筛选功能，可以帮助您快速查找到所需的数据.</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/3-1.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 分类统计 </h3> <p>在分类统计功能中，你可以按照时间跨度，查看到收入和支出的汇总情况，不同类别的占比情况以及时间变化曲线.</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/3-2.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">03.</span> 利润报表</h2> <p>利润报表功能，按照财务需要，分主营业务收入、主营业务税金及附加、主营业务成本（直接项目成本、直接人工成本）、主营业务利润、管理费用、财务费用、利润总额、所得税费用、净利润，让您对企业的经营状况一目了然.</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/3-3.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">04.</span> 费用录入 </h3> <p>在费用录入中，您可以根据实际情况，将企业的运营成本，如工资、房租、水电等分项录入.</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/3-4.png'));
$out+='" alt="Image Description"> </div> </div>';
return new String($out);
});/*v:1*/
template('m_website/m_website_products4',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='<div class="g-mb-60"> <h2 class="g-color-black g-font-weight-600 text-center g-mb-30">移动办公</h2> <p><span class="d-inline-block float-left g-width-60 g-height-60 g-color-black g-font-weight-600 g-font-size-30 text-center g-pa-7 mr-2">卯</span>丁移动端拥有轻量级OA系统， 项目成员可通过项目讨论区方便的交流协作，轻量任务更加适合临时性，低复杂度的任务安排和响应。</p> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">01.</span> 项目讨论区 </h3> <p>每个项目对应单独的项目讨论区，项目成员可进入对应的讨论区进行发布话题和互相评论。以话题 形式有效地记录项目相关重要信息。</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/4-1.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 日程安排 </h3> <p>日历模式，记录日常安排，设置日程提醒， 快速创建会议，即时邀请参会人，参会人可确认是否参加会议，让每一位参会者及时了解会议情况并提醒参加。</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/4-2.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">03.</span> 审批</h2> <p>审批功能目前包括费用报销和请假出差。</p> <p>费用报销：</p> <ul class="list-unstyled"> <li>1、支持自定义报销费用类别，统一规范组织内部报销费用类型</li> <li>2、快速上报报销明细，无纸化审批流程，领导随时随地审批，财务轻松便捷处理。</li> <li>3、全面打通报销流程一站式闭环管理，让公司费用报销有据可依，合理可控</li> </ul> <p>请假出差</p> <ul class="list-unstyled"> <li>1、摒弃传统纸质申请，随时随地都能申请审批</li> <li>2、无纸化多维度汇总，行政人员轻松查阅</li> </ul> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/4-3.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">04.</span> 工时 </h3> <p>项目成员可根据参与项目情况，定时填写工时，最终计入项目人工成本。</p> <ul class="list-unstyled"> <li>1、项目成员填写工时</li> <li>2、实际工时数据有效支持项目成本控制</li> </ul> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/4-4.png'));
$out+='" alt="Image Description"> </div> </div> <div class="row"> <div class="col-md-12 g-mb-30"> <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">05.</span> 轻量任务</h2> <p>轻量任务是对项目管理的有效补充，它主要针对于行政类、临时类任务，采取点对点的方式，帮助您通过移动端轻松下达任务，并了解任务状态.</p> </div> <div class="col-md-12 g-mb-60"> <img class="img-fluid" src="';
$out+=$escape(_url('/assets/img/website/img-temp/770x502/4-5.png'));
$out+='" alt="Image Description"> </div> </div>';
return new String($out);
});/*v:1*/
template('m_xeditable/m_xeditable_time',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,text=$data.text,$out='';$out+='<form> <div class="input-group"> <input type="text" class="form-control input-sm" name="time" placeholder="请选择时间" readonly onclick="WdatePicker()" value="';
$out+=$escape(text);
$out+='"> <span class="input-group-addon no-padding"> <i class="icon-sm icon-append fa fa-calendar" ></i> </span> </div> </form> ';
return new String($out);
});

}()