/**
 * Created by Wuwq on 2017/1/17.
 */
var app = {
    init: function () {
        var that = this;

        that.renderTop();
        that.renderBottom();
    }

    //渲染顶部
    , renderTop: function (data) {
        var that = this;
        $('#m_top').m_top({});
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

$(document).ready(function () {
    // Add body-small class if window less than 768px
    if ($(this).width() < 769) {
        $('body').addClass('body-small')
    } else {
        $('body').removeClass('body-small')
    }
    // Minimalize menu when screen is less than 768px
    $(window).bind("resize", function () {
        if ($(this).width() < 769) {
            $('body').addClass('body-small')
        } else {
            $('body').removeClass('body-small')
        }
        fix_height();
    });
    // Full height of sidebar
    function fix_height() {
        var heightWithoutNavbar = $("body > #wrapper").height() - 61;
        $(".sidebar-panel").css("min-height", heightWithoutNavbar + "px");

        var navbarheight = $('nav.navbar-default').height();
        var wrapperHeight = $('#page-wrapper').height();

        if (navbarheight > wrapperHeight) {
            $('#page-wrapper').css("min-height", navbarheight + "px");
        }

        if (navbarheight < wrapperHeight) {
            $('#page-wrapper').css("min-height", ($(window).height()-60) + "px");
        }

        if ($('body').hasClass('fixed-nav')) {
            if (navbarheight > wrapperHeight) {
                $('#page-wrapper').css("min-height", navbarheight + "px");
            } else {
                $('#page-wrapper').css("min-height", $(window).height() - 60 + "px");
            }
        }

    }

    fix_height();
});
