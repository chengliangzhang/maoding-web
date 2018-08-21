/**
 * Created by Wuwq on 2016/11/2.
 */
$(function() {
    var currentIndex=0;
    var vCenter=function(obj){
        if(obj&&obj.length>0){
            //console.log(obj.length);
            $.each(obj,function(i,o){
                var that=$(o);
                var windowHeight = document.documentElement.clientHeight;
                var objHeight = that.outerHeight(true);
                var scrollTop=$(document).scrollTop();
                //console.log('windowHeight:'+windowHeight);
                //console.log('objHeight:'+objHeight);
                //console.log('scrollTop:'+scrollTop);
                var margin=(windowHeight-objHeight)/2-100+scrollTop;
                //that.css({'margin-top': '0'});
                that.css({'margin-top': margin+'px'});
            });
        }
    };

    $(window).resize(function(){
        //console.log(currentIndex);
        if(currentIndex>0){
            vCenter($('#section'+currentIndex).find('.f_img_word_wrapper'));
        }else{
            vCenter($('#f1_img'));
        }
    });

    $('#fullpage').fullpage({
        navigation: true,
        verticalCentered: true,
        afterRender: function(anchorLink, index){
            if($('#container').is(':hidden')){
                $('#container').show();
                vCenter($('#f1_img'));
            }
        },
        onLeave: function(index, nextIndex, direction){
            currentIndex=nextIndex;
            vCenter($('#section'+currentIndex).find('.f_img_word_wrapper'));
        }
    });
});