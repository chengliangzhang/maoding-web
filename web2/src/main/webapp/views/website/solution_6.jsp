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
                    <h2 class="g-color-black g-font-weight-600 text-center g-mb-30">项目讨论区</h2>
                </div>
                <div class="row">
                    <div class="col-md-12 g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">*</span> 重要信息，沉没在99+的消息中，过多耗费了您的关注度
                        </h3>
                        <p>卯丁引入了项目讨论区功能，用户可以针对某一问题创建话题讨论，进行评论和回复，项目话题内容只对项目成员可见。以项目维度，很好归位记录汇总重要的信息话题，达到信息及时通知及反馈。</p>
                    </div>
                    <div class="col-md-12 g-mb-60">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-six-one.png" alt="重要信息"/>
                    </div>
                </div>
                <div class="row">
                        <div class="col-md-12 g-mb-30">
                            <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">01.</span> 发起话题
                            </h3>
                            <p>项目参与人员都可进行发起话题，设置特别提醒人员，贴心提醒需通知人员。</p>
                        </div>
                        <div class="col-md-12 g-mb-60 text-center">
                            <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-six-two.png" alt="发起话题"/>
                        </div>
                </div>

                <div class="row">
                    <div class="col-md-12  g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 评论及回复
                        </h3>
                        <p>项目成员可进行互评回复，互评消息即时提醒。</p>
                    </div>
                    <div class="col-md-12 g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-six-three.png" alt="评论回复"/>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12 g-mb-30">
                        <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">03.</span> 删除话题</h2>
                        <p>仅发话题者可进行删除话题。</p>
                    </div>
                </div>
                <p>接下来您还可以浏览</p>
                <ul class="list-unstyled">
                    <li><a href="<%=rootPath %>/home/solution_1">卯丁官方指南-快速建立组织架构</a></li>
                    <li><a href="<%=rootPath %>/home/solution_2">卯丁官方指南-合理的权限设置</a></li>
                    <li><a href="<%=rootPath %>/home/solution_3">卯丁官方指南-项目管理流程</a></li>
                    <li><a href="<%=rootPath %>/home/solution_4">卯丁官方指南-项目费控</a></li>
                    <li><a href="<%=rootPath %>/home/solution_5">卯丁官方指南-统计分析</a></li>
                    <li class="active"><a href="<%=rootPath %>/home/solution_6">卯丁官方指南-项目讨论区</a></li>
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