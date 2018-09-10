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

    if(datetime==null)
        return '';

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
