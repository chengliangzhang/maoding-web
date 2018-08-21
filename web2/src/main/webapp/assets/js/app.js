/**
 * Created by Wuwq on 2017/1/17.
 */
var app = {
    init: function () {
        var that = this;


        that.renderTop();
        that.renderBottom();
    }
    /*//宽度
     , fix_width: function () {
     var resizeWidth = function () {
     if ($(window).width() < 769) {
     $('body').addClass('body-small');
     } else {
     $('body').removeClass('body-small');
     }

     if (!$('body').hasClass('mini-navbar')&&$('body').hasClass('body-small')) {
     $('#navBarZoomIcon').removeClass('fa-outdent').addClass('fa-indent');
     }else if (!$('body').hasClass('mini-navbar')&&!$('body').hasClass('body-small')) {
     $('#navBarZoomIcon').removeClass('fa-indent').addClass('fa-outdent');
     }else if ($('body').hasClass('mini-navbar')&&$('body').hasClass('body-small')) {
     $('#navBarZoomIcon').removeClass('fa-indent').addClass('fa-outdent');
     }
     };
     resizeWidth();
     $(window).bind("resize", function () {
     resizeWidth();
     });
     }
     //高度
     , fix_height: function () {
     var fix = function () {
     var heightWithoutNavbar = $("body > #wrapper").height()-61;
     $(".sidebard-panel").css("min-height", heightWithoutNavbar + "px");

     var navbarHeigh = $('nav.navbar-default').height();
     var wrapperHeigh = $('#page-wrapper').height();

     if (navbarHeigh > wrapperHeigh) {
     $('#page-wrapper').css("min-height", navbarHeigh + "px");
     }

     if (navbarHeigh < wrapperHeigh) {
     //console.log('window-height'+$(window).height());
     $('#page-wrapper').css("min-height", $(window).height() + "px");
     }

     if ($('body').hasClass('fixed-nav')) {
     if (navbarHeigh > wrapperHeigh) {
     $('#page-wrapper').css("min-height", navbarHeigh - 60 + "px");
     } else {
     $('#page-wrapper').css("min-height", $(window).height() - 60 + "px");
     }
     }
     };
     fix();

     $(window).bind("load resize scroll", function () {
     if (!$("body").hasClass('body-small')) {
     fix();
     }
     });
     }*/

    //渲染侧边导航
    /*, renderLeftNav: function (data) {
     var that = this;
     var html = template('m_common/m_leftNav', data);
     $('#m_leftNav').html(html);

     $('#side-menu').metisMenu();
     }*/
    //缩放左侧导航
    /*,SmoothlyMenu: function () {
     if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
     $('#side-menu').hide();
     setTimeout(
     function () {
     $('#side-menu').fadeIn(0);
     }, 200);

     if (!$('body').hasClass('mini-navbar')&&$('body').hasClass('body-small')) {
     $('#navBarZoomIcon').removeClass('fa-outdent').addClass('fa-indent');
     }else if (!$('body').hasClass('mini-navbar')&&!$('body').hasClass('body-small')) {
     $('#navBarZoomIcon').removeClass('fa-indent').addClass('fa-outdent');
     }else if ($('body').hasClass('mini-navbar')&&$('body').hasClass('body-small')) {
     $('#navBarZoomIcon').removeClass('fa-indent').addClass('fa-outdent');
     }

     } else if ($('body').hasClass('fixed-sidebar')) {
     $('#side-menu').hide();
     setTimeout(
     function () {
     $('#side-menu').fadeIn(0);
     }, 100);

     $('#navBarZoomIcon').removeClass('fa-indent').addClass('fa-outdent');
     } else {
     $('#side-menu').removeAttr('style');
     $('#navBarZoomIcon').removeClass('fa-outdent').addClass('fa-indent');
     }
     }*/
    //渲染顶部
    , renderTop: function (data) {
        var that = this;
        $('#m_top').m_top({});

        /*//缩放左侧导航
         $('.navbar-minimalize').on('click', function () {
         $("body").toggleClass("mini-navbar");

         that.SmoothlyMenu();
         });*/
    }
    //渲染底部
    , renderBottom: function () {
        var html = template('m_common/m_bottom', {rootPath: window.rootPath});
        $('#m_bottom').html(html);

        var option = {
            ignoreError: true,
            url: restApi.url_getCompanyDiskInfo,
            postData:{
                companyId: window.currentCompanyId
            }
        };
        m_ajax.postJson(option, function (res) {
            if(res.code==='0'){
                var totalSize=parseFloat(res.data.totalSize);
                var freeSize=parseFloat(res.data.freeSize);
                var usedSize=totalSize-freeSize;
                var text=_.sprintf('已使用：%s&nbsp;&nbsp;总容量：%s',formatFileSize(usedSize),formatFileSize(totalSize));
                $('#footDiskInfo').html(text);
            }
        });
    }
};
