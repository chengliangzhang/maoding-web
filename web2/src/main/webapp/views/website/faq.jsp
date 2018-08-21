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

<!-- Promo Block -->
<section class="u-bg-overlay g-bg-cover g-bg-size-cover g-bg-bluegray-opacity-0_3--after"
         style="background: url(<%=rootPath %>/assets/img/website/help/bg.jpg)"></div>
    <div class="container text-center g-z-index-1 g-py-150">
        <div class="row justify-content-center">
            <div class="col-lg-6">
                <div class="mb-5">
                    <h1 class="g-color-white g-font-size-60 mb-4">帮助中心</h1>
                    <h2 class="g-color-white g-font-weight-300 g-font-size-20 mb-0">帮助您全面了解卯丁</h2>
                </div>

                <!-- Promo Blocks - Input -->
                <!--<div class="form-group mb-0">-->
                <!--<div class="input-group">-->
                <!--<input id="inputGroup1_1"-->
                <!--class="form-control u-form-control g-brd-white g-font-size-default g-placeholder-gray g-rounded-left-30 g-px-25 g-py-15"-->
                <!--type="text" placeholder="请输入关键字">-->
                <!--<div class="input-group-btn">-->
                <!--<button class="btn btn-primary g-font-size-18 g-rounded-right-30 g-px-25 g-py-15"-->
                <!--type="submit">-->
                <!--<i class="g-font-size-default fa fa-search"></i>-->
                <!--</button>-->
                <!--</div>-->
                <!--</div>-->
                <!--</div>-->
                <!-- End Promo Blocks - Input -->
            </div>
        </div>
    </div>
</section>
<!-- End Promo Block -->

<!-- Icons Block -->
<section class="container g-pt-100 g-pb-70">
    <div class="row">
        <div class="col-md-4 g-mb-30">
            <!-- Icon Blocks -->
            <div class="g-bg-purple text-center rounded g-pos-rel g-z-index-1 g-px-20 g-py-30">
              <span class="u-icon-v1 u-icon-size--xl g-color-white g-mb-10">
                <i class="icon-communication-009 u-line-icon-pro"></i>
              </span>
                <h3 class="h4 g-color-white mb-2">电话咨询</h3>
                <span class="d-block h5 g-color-white-opacity-0_7 mb-4">400 900 6299</span>
            </div>
            <!-- End Icon Blocks -->
        </div>

        <div class="col-md-4 g-mb-30">
            <!-- Icon Blocks -->
            <div class="g-bg-teal text-center rounded g-pos-rel g-z-index-1 g-px-20 g-py-30">
              <span class="u-icon-v1 u-icon-size--xl g-color-white g-mb-10">
                <i class="icon-communication-062 u-line-icon-pro"></i>
              </span>
                <h3 class="h4 g-color-white mb-2">邮件咨询</h3>
                <span class="d-block h5 g-color-white-opacity-0_7 mb-4">services@imaoding.com</span>
            </div>
            <!-- End Icon Blocks -->
        </div>

        <div class="col-md-4 g-mb-30">
            <!-- Icon Blocks -->
            <div class="g-bg-blue text-center rounded g-pos-rel g-z-index-1 g-px-20 g-py-30" style="cursor: pointer" >
              <span class="u-icon-v1 u-icon-size--xl g-color-white g-mb-10">
                <i class="icon-communication-058 u-line-icon-pro"></i>
              </span>
                <h3 class="h4 g-color-white mb-2">即将推出</h3>
                <span class="d-block h5 g-color-white-opacity-0_7 mb-4">Online Documents</span>
            </div>
            <!-- End Icon Blocks -->
        </div>
    </div>
</section>
<!-- End Icons Block -->

<!-- 卯丁快速上手 -->
<section id="quickstart" class="g-bg-secondary">
    <div class="container g-py-100">
        <!-- Carousel -->
        <div class="row justify-content-center">
            <div class="col-lg-11">
                <!-- Heading -->
                <div class="text-center g-mb-60">
                    <div class="d-inline-block g-width-35 g-height-1 g-bg-primary mb-2"></div>
                    <h2 class="g-color-black g-font-weight-600 mb-2">卯丁快速上手</h2>
                    <p class="lead g-width-60x--md mx-auto">我们教您一步步的了解使用卯丁.</p>
                </div>
                <!-- End Heading -->

                <div class="row no-gutters g-mx-minus-10">
                    <div class="col-sm-6 col-lg-4 g-px-10 g-mb-20">
                        <!-- Projects -->
                        <div class="u-block-hover g-brd-around g-brd-gray-light-v4 g-color-black g-color-white--hover g-bg-purple--hover text-center rounded g-transition-0_3 g-px-30 g-py-50" onclick="self.location='<%=rootPath %>/home/solution_1'">
                            <img class="img-fluid u-block-hover__main--zoom-v1 mb-5" src="<%=rootPath %>/assets/img/website/help/organization.png" alt="快速建立组织架构">
                            <span class="g-font-weight-600 g-font-size-12 text-uppercase">官方指南</span>
                            <h3 class="h4 g-font-weight-600 mb-0">快速建立组织架构</h3>

                            <a class="u-link-v2" href="#"></a>
                        </div>
                        <!-- End Projects -->
                    </div>

                    <div class="col-sm-6 col-lg-4 g-px-10 g-mb-20">
                        <!-- Projects -->
                        <div class="u-block-hover g-brd-around g-brd-gray-light-v4 g-color-black g-color-white--hover g-bg-cyan--hover text-center rounded g-transition-0_3 g-px-30 g-py-50" onclick="self.location='<%=rootPath %>/home/solution_2'">
                            <img class="img-fluid u-block-hover__main--zoom-v1 mb-5" src="<%=rootPath %>/assets/img/website/help/authority.png" alt="合理的权限设置">
                            <span class="g-font-weight-600 g-font-size-12 text-uppercase">官方指南</span>
                            <h3 class="h4 g-font-weight-600 mb-0">合理的权限设置</h3>

                            <a class="u-link-v2" href="#"></a>
                        </div>
                        <!-- End Projects -->
                    </div>

                    <div class="col-sm-6 col-lg-4 g-px-10 g-mb-20">
                        <!-- Projects -->
                        <div class="u-block-hover g-brd-around g-brd-gray-light-v4 g-color-black g-color-white--hover g-bg-teal--hover text-center rounded g-transition-0_3 g-px-30 g-py-50" onclick="self.location='<%=rootPath %>/home/solution_3'">
                            <img class="img-fluid u-block-hover__main--zoom-v1 mb-5" src="<%=rootPath %>/assets/img/website/help/project.png" alt="项目管理流程">
                            <span class="g-font-weight-600 g-font-size-12 text-uppercase">官方指南</span>
                            <h3 class="h4 g-font-weight-600 mb-0">项目管理流程</h3>

                            <a class="u-link-v2" href="#"></a>
                        </div>
                        <!-- End Projects -->
                    </div>

                    <div class="col-sm-6 col-lg-4 g-px-10 g-mb-20">
                        <!-- Projects -->
                        <div class="u-block-hover g-brd-around g-brd-gray-light-v4 g-color-black g-color-white--hover g-bg-lightred--hover text-center rounded g-transition-0_3 g-px-30 g-py-50" onclick="self.location='<%=rootPath %>/home/solution_4'">
                            <img class="img-fluid u-block-hover__main--zoom-v1 mb-5" src="<%=rootPath %>/assets/img/website/help/money.png" alt="项目费控流程">
                            <span class="g-font-weight-600 g-font-size-12 text-uppercase">官方指南</span>
                            <h3 class="h4 g-font-weight-600 mb-0">项目费控流程</h3>

                            <a class="u-link-v2" href="#"></a>
                        </div>
                        <!-- End Projects -->
                    </div>

                    <div class="col-sm-6 col-lg-4 g-px-10 g-mb-20">
                        <!-- Projects -->
                        <div class="u-block-hover g-brd-around g-brd-gray-light-v4 g-color-black g-color-white--hover g-bg-primary--hover text-center rounded g-transition-0_3 g-px-30 g-py-50" onclick="self.location='<%=rootPath %>/home/solution_5'">
                            <img class="img-fluid u-block-hover__main--zoom-v1 mb-5" src="<%=rootPath %>/assets/img/website/help/statistics.png" alt="卯丁的财务数据统计">
                            <span class="g-font-weight-600 g-font-size-12 text-uppercase">官方指南</span>
                            <h3 class="h4 g-font-weight-600 mb-0">卯丁的财务数据统计</h3>

                            <a class="u-link-v2" href="#"></a>
                        </div>
                        <!-- End Projects -->
                    </div>

                    <div class="col-sm-6 col-lg-4 g-px-10 g-mb-20">
                        <!-- Projects -->
                        <div class="u-block-hover g-brd-around g-brd-gray-light-v4 g-color-black g-color-white--hover g-bg-pink--hover text-center rounded g-transition-0_3 g-px-30 g-py-50" onclick="self.location='<%=rootPath %>/home/solution_6'">
                            <img class="img-fluid u-block-hover__main--zoom-v1 mb-5" src="<%=rootPath %>/assets/img/website/help/discuss.png" alt="项目讨论区">
                            <span class="g-font-weight-600 g-font-size-12 text-uppercase">官方指南</span>
                            <h3 class="h4 g-font-weight-600 mb-0">项目讨论区</h3>

                            <a class="u-link-v2" href="#"></a>
                        </div>
                        <!-- End Projects -->
                    </div>
                </div>
            </div>
        </div>
        <!-- End Carousel -->
    </div>
</section>
<!-- 卯丁快速上手 结束 -->

<!-- Q&A -->
<section id="FAQ" class="g-py-90">
    <div class="container">
        <div class="text-uppercase g-line-height-1_3 g-mb-20">
            <h2 class="g-font-size-36 mb-0">常见问题</h2>
        </div>

        <p class="g-mb-65">这里是一些卯丁使用过程中的常见问题，您也可以访问在线文档查看更加系统详细的使用帮助.</p>

        <!-- Nav tabs -->
        <ul class="nav u-nav-v5-1 text-uppercase g-line-height-1_4 g-font-weight-700 g-font-size-11 g-brd-bottom--md g-brd-gray-light-v4"
            role="tablist"
            data-target="FAQTabs"
            data-tabs-mobile-type="slide-up-down"
            data-btn-classes="btn btn-md u-btn-primary btn-block rounded-0 u-btn-outline-lightgray">
            <li class="nav-item">
                <a class="nav-link g-px-0--md g-pb-15--md g-mr-30--md active" data-toggle="tab" href="#everything"
                   role="tab">组织管理</a>
            </li>
            <li class="nav-item">
                <a class="nav-link g-px-0--md g-pb-15--md g-mr-30--md" data-toggle="tab" href="#login"
                   role="tab">项目管理</a>
            </li>
            <li class="nav-item">
                <a class="nav-link g-px-0--md g-pb-15--md g-mr-30--md" data-toggle="tab" href="#security" role="tab">财务管理</a>
            </li>
            <li class="nav-item">
                <a class="nav-link g-px-0--md g-pb-15--md g-mr-30--md" data-toggle="tab" href="#privacyPolicy"
                   role="tab">移动办公</a>
            </li>
        </ul>
        <!-- End Nav tabs -->

        <!-- Tab panes -->
        <div id="FAQTabs" class="tab-content g-pt-20">
            <!--组织管理-->
            <div class="tab-pane fade show active" id="everything" role="tabpanel">

                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">如何创建分公司/事业合伙人？</h4>
                    <p class="g-font-size-default g-mb-30">当你的公司拥有分公司或事业合伙人时，可以在系统中创建分公司或事业合伙人，同时可设置组织权限有三种 ：
                        <br> 1.如某地区分公司，拥有与总部公司同样的权限.
                        <br> 2.可独立处理相关公司所有事宜，但不可下设分公司/事业合伙人.
                        <br> 3.相关财务由总公司代为处理，但不可下设分公司/事业合伙人.</p>
                </div>
                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">如何把企业已经产生的数据进行导入？</h4>
                    <p class="g-font-size-default g-mb-30">相关负责人在后台管理中，选择要导入的团队，下载模版，填写对应的项目信息后，点击上传导入内容.</p>
                </div>
            </div>
            <!--项目管理-->
            <div class="tab-pane fade" id="login" role="tabpanel">
                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">为什么所有人都可以创建项目？</h4>
                    <p class="g-font-size-default g-mb-30">卯丁基于完整后台日志记录的前提下，最大限度的从去流程化，提高效率出发，去除了一些不会导致严重后果的权限限制.</p>
                </div>

                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">如何进行组织间的设计合作？</h4>
                    <p class="g-font-size-default g-mb-30">在系统中，由经营负责人通过任务签发来确认设计任务对应的设计组织，设计任务可以分配给分公司、事业合伙人或是通过项目合作方功能临时邀请的设计合作方.</p>
                </div>

                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">如何查看项目进度情况？</h4>
                    <p class="g-font-size-default g-mb-30">在具体项目中，通过生产安排，可以查看每个任务的具体安排情况和具体进度情况，是否超时、超时情况、完成时间等.</p>
                </div>

                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">如何进行归档？</h4>
                    <p class="g-font-size-default g-mb-30">在项目文档 - 归档文件中，发出归档通知，选择要通知到的归档人员，相关人员会收到通知，通过系统上传对应的文件进行归档.</p>
                </div>
            </div>
            <!--财务管理-->
            <div class="tab-pane fade" id="security" role="tabpanel">
                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">技术审查费是如何产生的？</h4>
                    <p class="g-font-size-default g-mb-30">当下属组织（分支机构/事业合伙人）在项目中选择所属上级组织为乙方时，会在项目 - 收支管理中出现技术审查费，由乙方根据立项方的合同回款节点信息，进行编辑技术审查费的收款节点信息.</p>
                </div>

                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">收支明细中都展示了哪些财务信息？</h4>
                    <p class="g-font-size-default g-mb-30">收支明细中会展示企业中所有的收入与支出记录，包括"合同回款、技术审查费、合作设计费、其他收支、报销与费用等".</p>
                </div>

                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">财务管理都有谁可以看？</h4>
                    <p class="g-font-size-default g-mb-30">在后台管理 - 权限配置 具有查看财务报表权限的人，才可以进行查看.</p>
                </div>


            </div>
            <!--移动办公-->
            <div class="tab-pane fade" id="privacyPolicy" role="tabpanel">

                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">如何快速找到甲方联系人？</h4>
                    <p class="g-font-size-default g-mb-30">在app端， 联系人 - 客户 中可添加、保存每个项目的甲方联系，当添加联系人后，参与此项目的人均可在客户中进行查看。</p>
                </div>

                <div class="g-brd-bottom g-theme-brd-gray-light-v1 g-py-40">
                    <h4 class="h6 text-uppercase g-font-weight-700 g-mb-10">如何使用日程管理？</h4>
                    <p class="g-font-size-default g-mb-30">在日程管理中，可根据
                        您的实际需要，在日历中添加事件.</p>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- End Q&A -->

<!-- Footer -->
<footer class="website-footer">

</footer>
<!-- End Footer -->

<%@ include file="lib_footer.jsp" %>

<!-- JS Plugins Init. -->
<script>
    $(document).ready(function () {
        website_faq.init();
    });
</script>

</body>
</html>