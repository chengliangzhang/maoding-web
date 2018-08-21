<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--[if IE 8]>
<html class="ie8">
<![endif]-->
<!--[if IE 9]>
<html class="ie9">
<![endif]-->
<!--[if !IE]>
<!-->
<html>
<!--<![endif]-->
<head>
    <title>卯丁－登录</title>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%! private final long v = System.currentTimeMillis(); %>
    <% String rootPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="<%=rootPath %>/assets/img/favicon2.ico">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/art-dialog/css/art-dialog.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/layer-2x/skin/default/layer.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/css/style.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/css/module.min.css?<%=v%>">

    <script type="text/javascript">
        var rootPath = '<%=rootPath%>';
        var v = '<%=v%>';
    </script>
    <link rel="stylesheet" href="<%=rootPath%>/assets/css/login.css?<%=v%>">
    <style type="text/css">
        body{
            background: #fff;
        }
        .valid-success {
            border-color: #008200;
        }
        .valid-error {
            border-color: #cd0a0a;
        }
        .tooltip.left .tooltip-inner {
            background-color:red;
        }
        .tooltip.left .tooltip-arrow {
            border-left-color: red;
        }
        a.svg{display: inline-block;position: relative;top: 15px;}
        a.svg:after {
            content: "";
            position: absolute;
            top: 0;
            right: 0;
            bottom: 0;
            left:0;
        }
    </style>
</head>
<body>
<div class="wrapper">
    <div class="header-v6 header-classic-white">
        <div class="container-fluid">
            <div class="row head-row">
                <div class="col-md-12">
                    <div class="head-logo">
                        <a href="<%=rootPath%>/" class="svg" style="height:45px;width:114px;">
                            <object data="<%=rootPath %>/assets/img/logo_blue.png"></object >
                            <%--<img src="<%=rootPath %>/assets/img/website/logo1-default.png" alt="Logo"/>--%>
                        </a>
                    </div>
                    <div class="head-reg" style="padding-right:0">
                        第一次使用卯丁？&nbsp;<a class="color-dark-blue" href="<%=rootPath %>/iWork/sys/register">立即注册</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="login-bg-wrapper">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <div class="left-word-wrapper">
                        <h1 class="left-word">
                            卯丁，设计同行的工作方式
                        </h1>
                    </div>
                    <div class="login-form-wrapper rounded login-content">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid">
        <div class="row foot-copy-rights">
            <p class="text-center">深圳市卯丁技术有限公司 © 2017 粤ICP备16006511号-1</p>
        </div>
    </div>
</div>

<%@ include file="../lib_footer_login.jsp" %>
<script>
    $(function () {
        home_login.init();
        $('body').m_browser_tips();
    });
</script>
</body>
</html>