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
                    <h2 class="g-color-black g-font-weight-600 text-center g-mb-30">项目管理流程</h2>
                    <p>卯丁项目管理分为2个主要阶段，1.分配任务到组织。2.分配具体任务到人员。</p>
                </div>



                <div class="row">
                    <div class="col-md-6  g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">01.</span> 任务签发
                        </h3>
                        <p>1.选择设计任务。</p>
                        <p>2.在选择的设计任务下添加子任务。</p>
                        <p>3.选择设计组织。</p>
                        <p>4.发布设计任务。</p>
                        <p>5.如当前设计组织是本组织，则设计任务直接进行生产环节，进行安排具体的设计/校对/审核人员。</p>
                        <p>6.如当前设计组织不是本组织，则对应设计组织重新进入签发流程。</p>

                    </div>
                    <div class="col-md-6  g-mb-60">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-three-one.jpg" alt="任务签发">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 g-mb-60">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-three-two.jpg" alt="生产安排">
                    </div>
                    <div class="col-md-6 g-mb-30">
                        <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 生产安排</h2>
                        <p>1.接到本组织发布的设计任务。</p>
                        <p>2.分解设计任务。</p>
                        <p>3.添加设计、校对、审核人员。</p>
                        <p>4.设计人员完成任务后提交校对人员。</p>
                        <p>5.校对人员完成任务后提交审核人员。</p>
                        <p>6.最终由任务负责人确认任务最终的完成状态。</p>
                    </div>
                </div>



                <p>接下来您还可以浏览</p>
                <ul class="list-unstyled">
                    <li><a href="<%=rootPath %>/home/solution_1">卯丁官方指南-快速建立组织架构</a></li>
                    <li><a href="<%=rootPath %>/home/solution_2">卯丁官方指南-合理的权限设置</a></li>
                    <li class="active"><a href="<%=rootPath %>/home/solution_3">卯丁官方指南-项目管理流程</a></li>
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