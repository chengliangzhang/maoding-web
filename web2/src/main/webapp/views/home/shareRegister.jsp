<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--[if IE 8]> <html class="ie8"> <![endif]-->
<!--[if IE 9]> <html class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html> <!--<![endif]-->
<head>
    <title>卯丁－设计同行的工作方式</title>
    <%@ include file="../lib_header_new.jsp" %>
    <style>
        .timeline-v2:before{left:78%;}
        #step1 .sky-form .col-md-offset-3 img.img-responsive{margin: 0 auto}
        #showViewType1 div.form-group{text-align: left}
    </style>
</head>
<body class="bg-v1">
<%--<!--=== 页头 ===-->--%>
<%--<jsp:include page="../../../admin/directives/header/adminLoginHeader.jsp"></jsp:include>--%>
<%--<!--=== 页头结束 ===-->--%>

<!--=== 注册 开始 ===-->
    <input type="hidden" id="companyInfoId" value="${companyInfo.id}"/>
    <input type="hidden" id="userInfoId" value="${userInfo.userId}"/>
    <input type="hidden" id="companyInfoFile" value="${companyInfo.filePath}"/>
    <input type="hidden" id="serverUrl" value="${serverUrl}">
	<!-- 邀请注册步骤一   开始-->

    <div class="container content shareRegisterOBox">
        <div id="step1" class="row">
            <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3" id="shareStep1">
                <div class="middle-box text-center loginscreen  animated fadeInDown">
                    <div>
                        <div>
                            <img alt="" src="<%=rootPath%>/assets/img/withoutLogo.png" class="img-responsive filePath" style="margin: 0 auto;">
                        </div>
                        <h4 style="word-break:break-all">${companyInfo.companyName}</h4>
                        <p>${userInfo!=null?userInfo.userName:""}诚邀您加入</p>
                        <!--<p>填写手机号</p>-->
                        <form class="m-t" id="showViewType1" role="form" action="login.html">
                            <div class="form-group">
                                <input class="form-control" type="text" placeholder="手机号"  name="cellphone" id="cellphone" maxlength="11">
                            </div>
                            <div class="form-group">
                                <div class="input-group">
                                    <input placeholder="验证码" class="input form-control" type="text" id="verifcationCode" name="verifcationCode"  placeholder="验证码">
                                    <span class="input-group-btn">
                                        <a type="button" class="btn btn-u" id="getCode" data-action="getCode" style="width:96px;">获取验证码</a>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group">
                                <input class="form-control" type="text" placeholder="姓名"  id="userName" name="userName">
                            </div>
                            <p class="text-muted text-center" style="margin-bottom: 18px;">
                                <%--<small>点击加入表示你已阅读并同意</small>
                                <a href="javascript:void(0)">《卯丁 服务条款》</a>--%>
                            </p>
                            <a type="submit" class="btn btn-primary block full-width m-b" href="javascript:void(0)" data-action="submitRegister">加入</a>

                            <p class="text-muted text-center">
                                <small>已有卯丁账号？</small>
                                <a href="<%=rootPath%>/iWork/sys/login">立即登录</a>
                            </p>
                        </form>
                        <form class="m-t hide" id="showViewType2" role="form" action="login.html">
                            <div class="row">
                                <div class="col-md-12 col-sm-12 margin-bottom-30">
                                    <span>
                                        <img alt="" src="<%=rootPath%>/assets/img/submitSuccesed.png/img/submitSuccesed.png" class="img-responsive" style="margin: 0 auto">
                                    </span>
                                </div>
                                <div class="col-md-12 col-sm-12 text-center margin-bottom-40">
                                    <p>你的申请已提交，正在等待管理员审核</p>
                                </div>
                                <div class="col-md-12 col-sm-12 text-center">
                                    <label class="label" style="text-align: center">
                                        <a class="btn-u btn-u-primany btn-u-lg downloadBtn" onclick="home_shareRegister.toDownload();" href="javascript:void(0)" type="button" >下载卯丁APP</a>
                                    </label>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 邀请注册步骤一   结束-->
<iframe id="iframeDown" style="display: none;"></iframe>
<!--=== 注册结束 ===-->

<div class="maoding-footer-box"></div>

<%@ include file="../lib_footer_new.jsp" %>
<script type="text/javascript">
    $(function(){
        home_shareRegister.init();
    });
</script>
</body>
</html>