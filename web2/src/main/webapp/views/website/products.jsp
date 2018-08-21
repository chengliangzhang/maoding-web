<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>卯丁-设计同行的工作方式</title>
    <%@ include file="lib_header.jsp" %>
</head>
<body>

<!-- Header -->
<header id="js-header" class="u-header u-header--static website-header"></header>
<!-- End Header -->

<section class="g-py-100">
    <div class="container">
        <div class="row">
            <div class="col-lg-3 flex-lg-first g-brd-right--lg g-brd-gray-light-v4 g-mb-80">
                <div class="g-pr-20--lg">
                    <!-- Links -->
                    <div class="g-mb-50">
                        <h3 class="h5 g-color-black g-font-weight-600 mb-4">企业版</h3>
                        <ul class="list-unstyled g-font-size-13 mb-0" id="productsMenu">
                            <li>
                                <a class="d-block u-link-v5 g-color-black g-bg-gray-light-v5 g-font-weight-600 g-rounded-50 g-px-20 g-py-8"  href="javascript:void(0);" data-type="">
                                    <i class="mr-2 fa fa-angle-right"></i> 组织管理
                                </a>
                            </li>
                            <li>
                                <a class="d-block u-link-v5 g-color-gray-dark-v4  g-px-20 g-py-8" href="javascript:void(0);" data-type="2">
                                    <i class="mr-2 fa fa-angle-right"></i> 项目管理
                                </a>
                            </li>
                            <li>
                                <a class="d-block u-link-v5 g-color-gray-dark-v4  g-px-20 g-py-8" href="javascript:void(0);" data-type="3">
                                    <i class="mr-2 fa fa-angle-right"></i> 财务管理
                                </a>
                            </li>
                            <li>
                                <a class="d-block u-link-v5 g-color-gray-dark-v4  g-px-20 g-py-8" href="javascript:void(0);" data-type="4">
                                    <i class="mr-2 fa fa-angle-right"></i> 移动办公
                                </a>
                            </li>
                        </ul>
                    </div>
                    <!-- End Links -->
                </div>
            </div>
            <div class="col-lg-9" id="projectContent">

            </div>
        </div>
    </div>
</section>

<!-- Footer -->
<footer class="website-footer">

</footer>
<!-- End Footer -->

<%@ include file="lib_footer.jsp" %>

<!-- JS Plugins Init. -->
<script>
    $(document).ready(function () {
        website_products.init();
    });
</script>

</body>
</html>