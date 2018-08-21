//构造函数
function Router() {
    this.routes = {};//注册的所有路由
    this.currentUrl = '';
    this.before = null;//切换前
    this.after = null;//切换后
}
function getParamsUrl() {
    var hashDeatail = location.hash.split("?"),
        hashName = hashDeatail[0].split("#")[1],//路由地址
        params = hashDeatail[1] ? hashDeatail[1].split("&") : [],//参数内容
        query = {};
    for(var i = 0;i<params.length ; i++){
        var item = params[i].split("=");
        query[item[0]] = item[1]
    }
    return  {
        path:hashName,
        query:query
    }
}
Router.prototype.route = function(path, callback) {
    this.routes[path] = callback || function(){};//给不同的hash设置不同的回调函数
};
Router.prototype.refresh = function() {
    /*console.log(location.hash.slice(1));//获取到相应的hash值
    this.currentUrl = location.hash.slice(1) || '/';//如果存在hash值则获取到，否则设置hash值为/
    console.log(this.currentUrl);
    this.routes[this.currentUrl]();//根据当前的hash值来调用相对应的回调函数*/

    var currentHash = getParamsUrl();
    if(currentHash.path!=undefined){
        this.currentUrl = currentHash.path;
        this.routes[this.currentUrl](currentHash.query);
    }else{
        location.hash = '/';
    }
    this.after();

};
//切换成功之后
Router.prototype.afterCallback=function(callback){
    if(Object.prototype.toString.call(callback) === '[object Function]'){
        this.after = callback;
    }else{
        console.trace('路由切换后回调函数不正确')
    }
};
Router.prototype.init = function() {
    window.addEventListener('load', this.refresh.bind(this), false);
    window.addEventListener('hashchange', this.refresh.bind(this), false);
};
//给window对象挂载属性
window.Router = new Router();
window.Router.init();