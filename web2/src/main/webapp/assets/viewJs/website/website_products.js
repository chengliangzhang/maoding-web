/**
 * Created by wrb on 2018/05/09.
 */
var website_products = {
    init: function () {


        $('.website-header').m_website_header({$type:'products'});
        $('.website-footer').m_website_footer();
        $('#loginBox').m_website_login();

        $('#productsMenu').find('li a').off('click').on('click',function () {
            $('#productsMenu').find('li a').removeClass('g-color-black g-bg-gray-light-v5 g-font-weight-600 g-rounded-50').addClass('g-color-gray-dark-v4');
            $(this).addClass('g-color-black g-bg-gray-light-v5 g-font-weight-600 g-rounded-50').removeClass('g-color-gray-dark-v4');

            var dataType = $(this).attr('data-type');
            $('#projectContent').m_website_products({$type:dataType},true);

            return false;
        });
        $('#projectContent').m_website_products({},true);
    }

};