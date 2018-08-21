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
        <!-- Heading -->
        <div class="text-center g-mb-60">
            <div class="d-inline-block g-width-35 g-height-1 g-bg-primary mb-2"></div>
            <h2 class="g-color-black g-font-weight-600 mb-2">卯丁官方指南</h2>
            <p class="lead g-width-60x--md mx-auto">帮助您更快的找到卯丁的价值.</p>
        </div>
        <!-- End Heading -->
        <div class="row">
            <div class="col-lg-12">
                <div class="g-mb-60">
                    <h2 class="g-color-black g-font-weight-600 text-center g-mb-30">合理的权限配置</h2>
                    <p>在正式使用卯丁之前，合理的权限配置必不可少。虽然卯丁的权限体系足够灵活，可以根据您的需要进行任意的组合，但是，一个合理的权限配置将会让您的管理更加的井井有条。</p>
                </div>

                <div class="row">
                    <div class="col-md-12 g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">01.</span> 权限列表
                        </h3>
                    </div>
                    <div class="col-md-12 g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-two-one.png" alt="权限列表">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12  g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 任务签发 - 经营助理
                        </h3>
                        <p>任务签发，拥有任务签发权限的人，通常为您组织中的经营负责人，在具体项目中，负责进行任务签发工作，即对应合同内容，设置设计任务并分配给对应的设计组织。您不需要给每一个需要进行任务签发工作的人都分配该权限，在卯丁中，我们设置了经营助理的角色，它拥有等同于任务签发权限的所有权限，您只需在任务签发界面中指定您组织中的任何人即可。

                        </p>
                    </div>
                    <div class="col-md-12  g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-two-two.png" alt="任务签发 - 经营助理">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12  g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 生产安排 - 设计助理
                        </h3>
                        <p>生产安排，拥有生产安排权限的人，通常为您组织中的设计负责人，在具体项目中，负责进行生产安排工作，即对应任务签发的内容，设置相关任务并分配给对应的人员。您不需要给每一个需要进行生产安排工作的人都分配该权限，在卯丁中，我们设置了设计助理的角色，它拥有等同于生产安排权限的所有权限，您只需在生产安排界面中指定您组织中的任何人即可。
                        </p>
                    </div>
                    <div class="col-md-12  g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-two-three.png" alt="生产安排 - 设计助理">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12 g-mb-30">
                        <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">03.</span> 财务 - 确认付款日期 / 财务 - 确认到账日期</h2>
                        <p>以上两种权限分别对应了卯丁系统中财务确认收款和确认付款的功能，只有拥有权限的人员才可以在费用管理中进行对应的操作</p>
                    </div>
                    <div class="col-md-12 g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-two-four.png" alt="财务 - 确认付款日期 / 财务 - 确认到账日期">
                    </div>
                </div>

                <p>接下来您还可以浏览</p>
                <ul class="list-unstyled">
                    <li><a href="<%=rootPath %>/home/solution_1">卯丁官方指南-快速建立组织架构</a></li>
                    <li class="active"><a href="<%=rootPath %>/home/solution_2">卯丁官方指南-合理的权限设置</a></li>
                    <li><a href="<%=rootPath %>/home/solution_3">卯丁官方指南-项目管理流程</a></li>
                    <li><a href="<%=rootPath %>/home/solution_4">卯丁官方指南-项目费控</a></li>
                    <li><a href="<%=rootPath %>/home/solution_5">卯丁官方指南-统计分析</a></li>
                    <li><a href="<%=rootPath %>/home/solution_6">卯丁官方指南-项目讨论区</a></li>
                </ul>
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
    $(document).on('ready', function () {
        website_solution.init();
    });
</script>

</body>
</html>