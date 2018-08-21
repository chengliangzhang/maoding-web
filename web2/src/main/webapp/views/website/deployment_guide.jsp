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

<!-- Our Pricing -->
<section class="g-bg-secondary g-py-100">
    <div class="container">
        <!-- Heading -->
        <div class="text-center g-mb-60">
            <div class="d-inline-block g-width-35 g-height-1 g-bg-primary mb-2"></div>
            <h2 class="g-color-black g-font-weight-600 mb-2">卯丁部署指南</h2>
            <p class="lead g-width-60x--md mx-auto">帮助您快速实现卯丁使用前的准备工作及必要设置</p>
        </div>
        <!-- End Heading -->

        <div class="g-font-size-16 g-line-height-1_8 g-mb-30">

            <p>本指南将帮助您快速建立您在卯丁中的组织，并完成在正式使用前的必要设置和准备工作，请根据接下来的步骤进行操作。</p>

            <div class="text-center g-width-70x--md mx-auto g-my-40">
                <h3 class="text-uppercase">首先,您需要<a class="g-color-primary" href="<%=rootPath %>/iWork/sys/register" >注册卯丁账号</a></h3>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <h3 class="h5 g-color-gray-dark-v1 g-font-weight-600 g-mb-10">01.创建组织</h3>
                    <p>
                        填写组织名称、所在地区、服务类型、即组织创建成功.
                    </p>

                    <figure class="text-center text-md-center float-md-center g-pr-30--sm g-py-20 mb-0">
                        <img class="u-shadow-v25 img-fluid " src="<%=rootPath %>/assets/img/website/products/1-1.png" alt="创建组织">
                    </figure>
                </div>

                <div class="col-lg-12">
                    <h3 class="h5 g-color-gray-dark-v1 g-font-weight-600 g-mb-10">02.创建组织架构</h3>
                    <p>
                        创建组织架构，上传企业通讯录进行批量导入人员、部门即可一键完成组织人员的创建。
                    </p>
                    <figure class="text-center text-md-center float-md-center g-pr-30--sm g-py-20 mb-0">
                        <img class="u-shadow-v25 img-fluid " src="<%=rootPath %>/assets/img/website/solution/solution%20-detail%20-%20one.png" alt="创建组织架构">
                    </figure>

                </div>
                <div class="col-lg-12">
                    <div class="g-width-70x--md 1mx-auto g-my-40 " >
                        <blockquote class="blockquote g-color-primary g-brd-2 g-brd-primary--hover text-uppercase g-font-size-22 g-transition-0_2 g-mb-30">
                            <p>卯丁中的下设机构分为三种类型，您可以根据您的管理需要进行设置。</p>
                            <footer class="blockquote-footer g-font-size-12"><a href="#">了解详情</a></footer>
                        </blockquote>
                    </div>
                </div>

                <div class="col-lg-12">
                    <h3 class="h5 g-color-gray-dark-v1 g-font-weight-600 g-mb-10">03.设置权限</h3>
                    <p>
                        合理的权限配置必不可少。虽然卯丁的权限体系足够灵活，可以根据您的需要进行任意的组合，但是，一个合理的权限配置将会让您的管理更加的井井有条。
                    </p>
                    <figure class="ttext-center text-md-center float-md-center g-pr-30--sm g-py-20 mb-0">
                        <img class="u-shadow-v25 img-fluid " src="<%=rootPath %>/assets/img/website/solution/solution-two-one.png" alt="权限配置">
                    </figure>

                </div>
            </div>

        </div>
    </div>
</section>
<!-- End Our Pricing -->

<!-- Footer -->
<footer class="website-footer">

</footer>
<!-- End Footer -->

<%@ include file="lib_footer.jsp" %>

<!-- JS Plugins Init. -->
<script>
    $(document).on('ready', function () {
        website_guide.init();
    });
</script>

</body>
</html>