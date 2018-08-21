/**
 * Created by wrb on 2018/05/09.
 */
var website_index = {
    init: function () {

        /*判断浏览器版本是否是ie10以下 开始*/
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
        var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
        var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
        if (isIE) {
            var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
            reIE.test(userAgent);
            var fIEVersion = parseFloat(RegExp["$1"]);
            //当浏览器为小于ie10版本时，去掉data-ease属性
            if (fIEVersion < 10) {
                $('#slider-wrapper .master-slider').find('img').removeAttr('data-ease');
                $('#slider-wrapper .ms-slide').find('h1').removeAttr('data-ease');
            }
        }

        /*判断浏览器版本是否是ie10以下 结束*/


        $('.website-header').m_website_header();
        $('.website-footer').m_website_footer();
        $('#loginBox').m_website_login();


        //$.HSCore.components.HSCarousel.init('.js-carousel');

        /*$('#we-provide').slick('setOption', 'responsive', [{
            breakpoint: 992,
            settings: {
                slidesToShow: 2
            }
        }, {
            breakpoint: 576,
            settings: {
                slidesToShow: 1
            }
        }], true);*/

        // initialization of masonry
        /*$('.masonry-grid').imagesLoaded().then(function () {
            $('.masonry-grid').masonry({
                columnWidth: '.masonry-grid-sizer',
                itemSelector: '.masonry-grid-item',
                percentPosition: true
            });
        });*/





        // initialization of text animation (typing)
        $(".u-text-animation.u-text-animation--typing").typed({
            strings: [
                "管理方式",
                "工作方式",
                "交流方式"
            ],
            typeSpeed: 100,
            loop: true,
            backDelay: 1500
        });



    }

};