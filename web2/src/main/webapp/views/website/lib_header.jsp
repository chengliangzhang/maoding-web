<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%! private final long v = System.currentTimeMillis(); %>
<% String rootPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="keywords" content="卯丁、设计院管理、设计协同、协同设计、项目管理、建筑设计管理、规划设计管理、 景观设计管理、工业设计管理、项目管理软件、项目进度管理软件">
<meta name="description" content="卯丁，一个轻量、高效的设计管理协同工作平台，主要功能：组织管理、项目管理、协同设计、项目费控、即时通讯。400-900-6299">
<meta name="author" content="深圳市卯丁科技有限公司">

<link rel="shortcut icon" href="<%=rootPath %>/assets/img/favicon.ico">

<!-- CSS Global Compulsory -->
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/bootstrap4/bootstrap.min.css">

<!-- CSS Implementing Plugins -->
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/icon-awesome/css/font-awesome.min.css">
<%--<link rel="stylesheet" href="<%=rootPath %>/assets/lib/icon-etlinefont/style.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/icon-hs/style.css">--%>
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/icon-line-pro/style.css">

<%--<link rel="stylesheet" href="<%=rootPath %>/assets/lib/slick-carousel/slick/slick.css">--%>
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/hs-megamenu/src/hs.megamenu.css">
<%--<link rel="stylesheet" href="<%=rootPath %>/assets/lib/hamburgers/hamburgers.min.css">--%>

<!-- CSS Unify -->
<link rel="stylesheet" href="<%=rootPath %>/assets/css/website/unify.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/css/website/animate.css">

<!-- Revolution Slider -->
<%--<link rel="stylesheet" href="<%=rootPath %>/assets/lib/revolution-slider/revolution-addons/particles/css/revolution.addon.particles.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/revolution-slider/revolution/fonts/pe-icon-7-stroke/css/pe-icon-7-stroke.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/revolution-slider/revolution/css/settings.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/revolution-slider/revolution-addons/polyfold/css/revolution.addon.polyfold.css">--%>

<link rel="stylesheet" href="<%=rootPath %>/assets/lib/toastr/toastr.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/layer/theme/default/layer.css">

<script type="text/javascript">
    window.rootPath = '<%=rootPath %>';
</script>
