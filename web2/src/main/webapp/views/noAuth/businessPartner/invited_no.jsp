<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if IE 8]> <html class="ie8"> <![endif]-->
<!--[if IE 9]> <html class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html> <!--<![endif]-->
<head>
    <title>卯丁－设计同行的工作方式</title>
    <%@ include file="../../lib_header_new_lite.jsp" %>
</head>
<body class="top-navigation bg-white" >
<div id="wrapper">
    <div id="page-wrapper" class="white-bg">
        <!--头部信息-->
        <div id="m_top" class="row white-bg">
            <nav class="navbar navbar-static-top" role="navigation">
                <div class="navbar-header">
                    <a href="<%=rootPath%>" class="navbar-brand">
                        <img src="<%=rootPath%>/assets/img/logo_white.png" style="height: 35px;">
                    </a>
                </div>
            </nav>
        </div>
        <div class="wrapper wrapper-content" style="padding: 20px 0;">
            <div class="container-fluid">
                <div class="row">
                    <div class="div-invite-bPaner col-md-offset-2 col-md-8 col-lg-offset-3 col-lg-6 col-xl-offset-4 col-xl-4">
                        <div class="ibox">
                            <div class="ibox-content text-center no-border" style="padding-left: 0;padding-right: 0">
                                <div class="m-b-sm">
                                    <img alt="image" class="img-circle"
                                         src="<%=rootPath%>/assets/img/default/url_invalid.png"
                                         style="width: 100px;width: 100px;">
                                </div>
                                <p style="color:#aaa;font-size: 18px;margin-top:20px;">该邀请链接已失效</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../../lib_footer_new_lite.jsp" %>
</body>
</html>
