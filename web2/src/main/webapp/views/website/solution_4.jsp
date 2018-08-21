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


                <div class="row">
                    <div class="col-md-6 g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">01.</span> 合同回款
                        </h3>
                        <p>1.经营负责人设置合同回款总金额及对应回款节点信息.</p>
                        <p>2.根据设计任务完成情况发起合同回款.</p>
                        <p>3.财务人员确认到账日期.</p>

                    </div>
                    <div class="col-md-6 g-mb-60">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-four-one.jpg" alt="合同回款">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6  g-mb-60">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-four-two.jpg" alt="技术审查费">
                    </div>
                    <div class="col-md-6  g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 技术审查费
                        </h3>
                        <p>1.【&nbsp乙&nbsp&nbsp方&nbsp】经营负责人 - 设置收款金额及节点信息</p>
                        <p>2.【&nbsp乙&nbsp&nbsp方&nbsp】经营负责人 - 在对应节点发起收款</p>
                        <p>3.【立项方】经营负责人 - 确认付款金额</p>
                        <p>4.【立项方】财务 - 确认付款日期</p>
                        <p>5.【&nbsp乙&nbsp&nbsp方&nbsp】财务 - 确认到账日期</p>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 g-mb-30">
                        <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">03.</span> 合作设计费</h2>
                        <p>1.【发包方】经营负责人 - 设置付款金额及节点信息</p>
                        <p>2.【合作方】经营负责人 - 在对应节点发起收款</p>
                        <p>3.【发包方】经营负责人 - 确认付款金额</p>
                        <p>4.【发包方】财务 - 确认付款日期</p>
                        <p>5.【合作方】财务 - 确认到账日期</p>
                    </div>
                    <div class="col-md-6 g-mb-60">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-four-three.jpg" alt="合作设计费">
                    </div>
                </div>



                <p>接下来您还可以浏览</p>
                <ul class="list-unstyled">
                    <li><a href="<%=rootPath %>/home/solution_1">卯丁官方指南-快速建立组织架构</a></li>
                    <li><a href="<%=rootPath %>/home/solution_2">卯丁官方指南-合理的权限设置</a></li>
                    <li><a href="<%=rootPath %>/home/solution_3">卯丁官方指南-项目管理流程</a></li>
                    <li class="active"><a href="<%=rootPath %>/home/solution_4">卯丁官方指南-项目费控</a></li>
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