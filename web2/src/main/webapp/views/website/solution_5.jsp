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
                    <h2 class="g-color-black g-font-weight-600 text-center g-mb-30">卯丁的财务数据统计</h2>
                    <p>
                        卯丁的财务数据统计，分为 1.收支明细“记录企业中每一笔费用的收入与支出情况” 2.利润报表“企业中每笔钱归属的类型，经过计算后得出对应月份的利润情况”</p>
                </div>

                <div class="row">
                    <div class="col-md-12 g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">01.</span> 项目中的财务数据
                        </h3>
                        <p>项目中产生的财务数据有"合同回款、合作设计费、技术审查费、其他收支"4个类型，对应在每个项目中产生的数据都会记录在对应的收支明细中.</p>
                    </div>
                    <div class="col-md-12 g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-five-one.png" alt="项目中的财务数据">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12  g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">02.</span> 报销/费用中产生的数据
                        </h3>
                        <p>企业的报销与费用当审批通过，且财务已经进行拨款后，同样会记录到对应的收支明细中。</p>
                    </div>
                    <div class="col-md-12  g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-five-two.png" alt="报销/费用中产生的数据">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12 g-mb-30">
                        <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">03.</span> 每月固定支出</h2>
                        <p>企业可根据每月的固定支出情况进行对应的录入，每页的固定成本如“行政管理人员成本、办公场地费用、水电费用”，等对应录入的内容同样会进入收支明细。</p>
                    </div>
                    <div class="col-md-12 g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-five-three.png" alt="每月固定支出">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12  g-mb-30">
                        <h3 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">04.</span> 收支明细
                        </h3>
                        <p>收支明细中，会记录企业每一笔的收入与支出情况，并明确记录该笔钱的收支日期、收付款组织、及该笔钱管理的项目，做到每笔钱都可以找到来源与出处。</p>
                    </div>
                    <div class="col-md-12  g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-five-four.png" alt="收支明细">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12 g-mb-30">
                        <h2 class="h4 g-color-black g-font-weight-600"><span class="g-font-size-25">03.</span> 利润报表</h2>
                        <p>利润报表，卯丁会记录每一笔钱的类型，分别关联到该笔钱所在的类型下，经过严格的计算分别得出当月的利润总额及净利润，供企业参考并帮助企业及时了解企业的财务状况.</p>
                    </div>
                    <div class="col-md-12 g-mb-60 text-center">
                        <img class="img-fluid" src="<%=rootPath %>/assets/img/website/solution/solution-five-five.png" alt="利润报表">
                    </div>
                </div>

                <p>接下来您还可以浏览</p>
                <ul class="list-unstyled">
                    <li><a href="<%=rootPath %>/home/solution_1">卯丁官方指南-快速建立组织架构</a></li>
                    <li><a href="<%=rootPath %>/home/solution_2">卯丁官方指南-合理的权限设置</a></li>
                    <li><a href="<%=rootPath %>/home/solution_3">卯丁官方指南-项目管理流程</a></li>
                    <li><a href="<%=rootPath %>/home/solution_4">卯丁官方指南-项目费控</a></li>
                    <li class="active"><a href="<%=rootPath %>/home/solution_5">卯丁官方指南-统计分析</a></li>
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