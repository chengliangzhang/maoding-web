﻿function SelCity(obj,e) {
    var ths = obj;
    var dal = '<div class="_citys"><span title="关闭" id="cColse" >×</span><ul id="_citysheng" class="_citys0">';
    dal+='<li class="citySel">省份</li>';
    dal+='<li>城市</li>';
    //dal+='<li>区县</li>';
    dal+='</ul><div id="_citys0" class="_citys1"></div><div style="display:none" id="_citys1" class="_citys1"></div><div style="display:none" id="_citys2" class="_citys1"></div></div>';
    
    Iput.show({ id: ths, event: e, content: dal,width:"470"});
    $("#cColse").click(function () {
        Iput.colse();
    });
    var tb_province = [];
    var b = province;
    for (var i = 0, len = b.length; i < len; i++) {
        tb_province.push('<a data-level="0" data-id="' + b[i]['id'] + '" data-name="' + b[i]['name'] + '">' + b[i]['name'] + '</a>');
    }
    $("#_citys0").append(tb_province.join(""));
    $("#_citys0 a").click(function () {
        var g = getCity($(this));
        $("#_citys1 a").remove();
        $("#_citys1").append(g);
        $("._citys1").hide();
        $("._citys1:eq(1)").show();
        $("#_citys0 a,#_citys1 a,#_citys2 a").removeClass("AreaS");
        $(this).addClass("AreaS");
        var lev = $(this).data("name");
        ths.value = $(this).data("name");
        if (document.getElementById("hcity") == null) {
            var hcitys = $('<input>', {
                type: 'hidden',
                name: "hcity",
                "data-id": $(this).data("id"),
                id: "hcity",
                val: lev
            });
            $(ths).after(hcitys);
        }
        else {
            $("#hcity").val(lev);
            $("#hcity").attr("data-id", $(this).data("id"));
        }
        
        //自定义－特殊处理（北京＝110000，天津＝120000，上海＝310000，重庆＝500000，香港＝810000，澳门＝820000，台湾＝710000）
        if($(this).data("id")=='110000' || $(this).data("id")=='120000' || $(this).data("id")=='310000' || $(this).data("id")=='500000' 
        	|| $(this).data("id")=='810000' || $(this).data("id")=='820000' || $(this).data("id")=='710000'){
        	
        	Iput.colse();//自定义-目前两级即可（省、市）
        }
        
        //自定义触发方法
		if(window.whenChoseByCityFun!=undefined){
			whenChoseByCityFun(ths);
		}
		
        $("#_citys1 a").click(function () {
            $("#_citys1 a,#_citys2 a").removeClass("AreaS");
            $(this).addClass("AreaS");
            var lev =  $(this).data("name");
            if (document.getElementById("hproper") == null) {
                var hcitys = $('<input>', {
                    type: 'hidden',
                    name: "hproper",
                    "data-id": $(this).data("id"),
                    id: "hproper",
                    val: lev
                });
                $(ths).after(hcitys);
            }
            else {
                $("#hproper").attr("data-id", $(this).data("id"));
                $("#hproper").val(lev);
            }
            var bc = $("#hcity").val();
            ths.value = bc+ "-" + $(this).data("name");

            var ar = getArea($(this));

            $("#_citys2 a").remove();
            $("#_citys2").append(ar);
            $("._citys1").hide();
            $("._citys1:eq(2)").show();
            
            //自定义触发方法
    		if(window.whenChoseByCityFun!=undefined){
    			whenChoseByCityFun(ths);
    		}   
            Iput.colse();//自定义-目前两级即可（省、市）

            $("#_citys2 a").click(function () {
                $("#_citys2 a").removeClass("AreaS");
                $(this).addClass("AreaS");
                var lev = $(this).data("name");
                if (document.getElementById("harea") == null) {
                    var hcitys = $('<input>', {
                        type: 'hidden',
                        name: "harea",
                        "data-id": $(this).data("id"),
                        id: "harea",
                        val: lev
                    });
                    $(ths).after(hcitys);
                }
                else {
                    $("#harea").val(lev);
                    $("#harea").attr("data-id", $(this).data("id"));
                }
                var bc = $("#hcity").val();
                var bp = $("#hproper").val();
                ths.value = bc + "-" + bp + "-" + $(this).data("name");
                Iput.colse();
            });

        });
    });
    $("#_citysheng li").click(function () {
        $("#_citysheng li").removeClass("citySel");
        $(this).addClass("citySel");
        var s = $("#_citysheng li").index(this);
        $("._citys1").hide();
        $("._citys1:eq(" + s + ")").show();
    });
}

function getCity(obj) {
    var c = obj.data('id');
    var e = province;
    var f;
    var g = '';
    for (var i = 0, plen = e.length; i < plen; i++) {
        if (e[i]['id'] == parseInt(c)) {
            f = e[i]['city'];
            break
        }
    }
    for (var j = 0, clen = f.length; j < clen; j++) {
        g += '<a data-level="1" data-id="' + f[j]['id'] + '" data-name="' + f[j]['name'] + '" title="' + f[j]['name'] + '">' + f[j]['name'] + '</a>'
    }
    $("#_citysheng li").removeClass("citySel");
    $("#_citysheng li:eq(1)").addClass("citySel");
    return g;
}
function getArea(obj) {
    var c = obj.data('id');
    var e = area;
    var f = [];
    var g = '';
    for (var i = 0, plen = e.length; i < plen; i++) {
        if (e[i]['pid'] == parseInt(c)) {
            f.push(e[i]);
        }
    }
    for (var j = 0, clen = f.length; j < clen; j++) {
        g += '<a data-level="1" data-id="' + f[j]['id'] + '" data-name="' + f[j]['name'] + '" title="' + f[j]['name'] + '">' + f[j]['name'] + '</a>'
    }

    $("#_citysheng li").removeClass("citySel");
    $("#_citysheng li:eq(2)").addClass("citySel");
    return g;
}

Array.prototype.unique = function () {//去数组重复
    return this.sort().join(",,").replace(/(,|^)([^,]+)(,,\2)+(,|$)/g, "$1$2$4").replace(/,,+/g, ",").replace(/,$/, "").split(",");
}
var Iput={confg:{hand:"0",idIframe:"PoPx",idBox:"PoPy",content:"",ok:null,id:null,event:window.event,top:0,left:0,bodyHeight:0,bodyWidth:0,width:0,soll:null,pop:null},get:function(obj){return document.getElementById(obj)},lft:function(e){var l=0;while(e){l+=e.offsetLeft;e=e.offsetParent}return l},ltp:function(e){var t=0;while(e){t+=e.offsetTop;e=e.offsetParent}return t},clear:function(){Iput.confg.hand="0";Iput.confg.ok=null;Iput.confg.top=0;Iput.confg.left=0;Iput.confg.bodyHeight=0;Iput.confg.bodyWidth=0;Iput.confg.width=0;Iput.confg.pop=null},stopBubble:function(e){if(e&&e.stopPropagation){e.stopPropagation()}else{window.event.cancelBubble=true}},pop:function(){var $a=document.getElementsByTagName("body").item(0);var $c=document.createElement("iframe");var $b=document.createElement("div");$c.setAttribute('id',Iput.confg.idIframe);$c.setAttribute("src","about:blank");$c.style.zindex='100';$c.frameBorder="0";$c.style.width="0px";$c.style.height="0px";$c.style.position='absolute';$b.setAttribute('id',Iput.confg.idBox);$b.setAttribute('align','left');$b.style.position='absolute';$b.style.background='transparent';$b.style.zIndex='20000';if($a){if(Iput.get(Iput.confg.idIframe)){Iput.colse()}$a.appendChild($c);if($c){$c.ownerDocument.body.appendChild($b)}Iput.get(Iput.confg.idBox).innerHTML=Iput.confg.content;Iput.drice(Iput.confg.event)}if(!document.all){window.document.addEventListener("click",Iput.hide,false)}else{window.document.attachEvent("onclick",Iput.hide)}},drice:function(e){var bodyHith=Iput.confg.bodyHeight==0?document.body.scrollHeight:Iput.confg.bodyHeight;var bodywidth=Iput.confg.bodyWidth==0?document.body.scrollWidth:Iput.confg.bodyWidth;if(!e)e=window.event;var top=0,left=0;var a=Iput.get(Iput.confg.idBox);var b=Iput.get(Iput.confg.idIframe);var c=Iput.confg.id.offsetHeight;var d=Iput.confg.id.offsetWidth;var w=0;var st=0;var sl=0;if(Iput.confg.soll!=null){st=document.getElementById(Iput.confg.soll).scrollTop;sl=document.getElementById(Iput.confg.soll).scrollLeft}if(Iput.get(Iput.confg.idIframe)){if(Iput.confg.hand=="1"){top=Iput.confg.top+document.body.scrollTop+document.documentElement.scrollTop+e.clientY;left=Iput.confg.left+e.clientX+document.body.scrollLeft+document.documentElement.scrollLeft;if(a.offsetHeight+top>bodyHith){top=top-a.offsetHeight+Iput.get(Iput.confg.idBox).firstChild.offsetHeight}if(a.offsetWidth+left>bodywidth){left=left-a.offsetWidth+Iput.get(Iput.confg.idBox).firstChild.offsetWidth}a.style.top=top-st+"px";b.style.top=top-st+"px";a.style.left=left-sl+"px";b.style.left=left-sl+"px"}else if(Iput.confg.hand=="0"){w=Iput.confg.id.offsetWidth+"px";a.style.width=w;b.style.width=w;height=c;top=Iput.confg.top+Iput.ltp(Iput.confg.id);left=Iput.confg.left+Iput.lft(Iput.confg.id);if(a.firstChild.offsetHeight+top+c>bodyHith){top=top-a.firstChild.offsetHeight-c}if(a.firstChild.offsetWidth+left>bodywidth){left=left-a.firstChild.offsetWidth+d}b.style.top=top-st+"px";a.style.top=top-st+height+"px";b.style.left=left-sl+"px";a.style.left=left-sl+"px"}else{height=c;top=Iput.confg.top-Iput.get(Iput.confg.hand).scrollTop+Iput.ltp(Iput.confg.id);left=Iput.confg.left-Iput.get(Iput.confg.hand).scrollLeft+Iput.lft(Iput.confg.id);if(a.offsetHeight+top>bodyHith){top=top-a.offsetHeight-c}if(a.offsetWidth+left>bodywidth){left=left-a.offsetWidth-d}b.style.top=top-st+height+"px";a.style.top=top-st+height+"px";b.style.left=left-sl+"px";a.style.left=left-sl+"px"}}},show:function(){var config=arguments[0];var that=Iput.confg;Iput.clear();for(var i in that){if(config[i]!=undefined){that[i]=config[i]}};Iput.pop();if(Iput.confg.ok!=null){Iput.action(Iput.confg.ok())}},colse:function(){if(Iput.get(Iput.confg.idIframe)){document.body.removeChild(Iput.get(Iput.confg.idBox));document.body.removeChild(Iput.get(Iput.confg.idIframe))}if(Iput.get(Iput.confg.pop)){Iput.get(Iput.confg.pop).style.display="none"}},$colse:function(){Iput.colse()},hide:function(e){e=window.event||e;var srcElement=e.srcElement||e.target;if(Iput.confg.event==undefined){Iput.colse()}else{var a=Iput.confg.event.srcElement||Iput.confg.event.target;var b=Iput.get(Iput.confg.pop);if(a!=srcElement){Iput.colse()}if(b!=null){if(b!=srcElement&&a!=srcElement){Iput.colse()}}}if(Iput.get(Iput.confg.idIframe)){Iput.get(Iput.confg.idIframe).onclick=function(e){Iput.stopBubble(e)};Iput.get(Iput.confg.idBox).onclick=function(e){Iput.stopBubble(e)}}if(Iput.get(Iput.confg.pop)){Iput.get(Iput.confg.pop).onclick=function(e){Iput.stopBubble(e)}}},action:function(obj){eval(obj)},cookie:{Set:function(name,val){var Days=30;var exp=new Date();exp.setTime(exp.getTime()+Days*24*60*60*1000);document.cookie=name+"="+escape(val)+";expires="+exp.toGMTString()+"; path=/"},Get:function(name){var start=document.cookie.indexOf(name);var end=document.cookie.indexOf(";",start);return start==-1?null:unescape(document.cookie.substring(start+name.length+1,(end>start?end:document.cookie.length)))},Del:function(name){var exp=new Date();exp.setTime(exp.getTime()-1);var cval=this.GetCookie(name);if(cval!=null)document.cookie=name+"="+cval+";expires="+exp.toGMTString()}},ischeck:function(bol){var objs=form1.getElementsByTagName("input");if(bol){for(var i=0;i<objs.length;i++){if(objs[i].type.toLowerCase()=="checkbox"){objs[i].checked=true}}}else{for(var i=0;i<objs.length;i++){if(objs[i].type.toLowerCase()=="checkbox"){objs[i].checked=false}}}},contains:function(star,end,isIgnoreCase){if(isIgnoreCase){star=star.toLowerCase();end=end.toLowerCase()}var startChar=end.substring(0,1);var strLen=end.length;for(var j=0;j<star.length-strLen+1;j++){if(star.charAt(j)==startChar){if(star.substring(j,j+strLen)==end){return true}}}return false},gData:function(name,value){var top=window.top,cache=top['_CACHE']||{};top['_CACHE']=cache;return value?cache[name]=value:cache[name]},rData:function(name){var cache=window.top['_CACHE'];if(cache&&cache[name])delete cache[name]}}