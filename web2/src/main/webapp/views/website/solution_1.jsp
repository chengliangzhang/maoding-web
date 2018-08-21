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
                    <h2 class="g-color-black g-font-weight-600 text-center g-mb-30">快速建立组织架构</h2>
                    <p>卯丁支持各种形态的
                    组织架构，您可以方便的创建部门、分支机构和事业合伙人，在组织创建完成后，您可以通过批量导入的功能，快速完成人员录入的工作。</p>
                </div>

                <div class="row">
                    <div class="col-md-12 g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">01.</span> 下载模版
                        </h3>
                        <p>首先，您需要进入卯丁后台管理界面，选择组织架构，然后点击批量导入成员，在接下来的界面中点击下载模版.</p>
                    </div>
                    <div class="col-md-12 g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution%20-detail%20-%20one.png" alt="下载模版">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12  g-mb-60">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 上传模版
                        </h3>
                        <p>根据模版中的示例，将您组织中的人员信息填写完毕后，点击上传模版，选择编辑好的文件，等待上传完成即可，系统将根据模版内容自动创建组织成员，并发送通知短信。</p>
                    </div>
                    <div class="col-md-12  g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution%20-detail%20-%20two.png" alt="上传模版">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12 g-mb-30">
                        <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">03.</span> 大功告成</h2>
                        <p>至此，您已完成了组织人员的创建过程，接下来需要进行必要的<a href="#">权限设置</a>，这是卯丁系统运行的基础.</p>
                    </div>
                    <div class="col-md-12 g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution%20-detail%20-%20three.png" alt="大功告成">
                    </div>
                </div>

                <p>接下来您还可以浏览</p>
                <ul class="list-unstyled">
                    <li class="active"><a href="<%=rootPath %>/home/solution_1">卯丁官方指南-快速建立组织架构</a></li>
                    <li><a href="<%=rootPath %>/home/solution_2">卯丁官方指南-合理的权限设置</a></li>
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