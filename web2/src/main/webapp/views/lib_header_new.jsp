<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%! private final long v = System.currentTimeMillis(); %>
<% String rootPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<meta name="keywords" content="卯丁、设计院管理、设计协同、协同设计、项目管理、建筑设计管理、规划设计管理、 景观设计管理、工业设计管理、项目管理软件、项目进度管理软件">
<meta name="description" content="卯丁，一个轻量、高效的设计管理协同工作平台，主要功能：组织管理、项目管理、协同设计、项目费控、即时通讯。400-900-6299">
<meta name="author" content="深圳市卯丁科技有限公司">

<link rel="shortcut icon" href="<%=rootPath %>/assets/img/favicon.ico">

<link rel="stylesheet" href="<%=rootPath %>/assets/lib/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/css/bootstrap_ext.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/jquery.spinner/css/bootstrap-spinner.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/icon/iconfont.min.css">

<link rel="stylesheet" href="<%=rootPath %>/assets/lib/layer/theme/default/layer.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/toastr/toastr.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/mricodePagination/mricode.pagination.min.css">

<link rel="stylesheet" href="<%=rootPath %>/assets/css/style.min.css?v=1">
<link rel="stylesheet" href="<%=rootPath %>/assets/css/animate.min.css">

<link rel="stylesheet" href="<%=rootPath %>/assets/css/module.min.css?v=<%=v%>">

<script type="text/javascript">
    var v = '<%=v%>';
    var rootPath = '<%=rootPath%>';
    var socketUrl='${scoketUrl}';
    var fastdfsUrl='${fastdfsUrl}';
    var fileCenterUrl='${fileCenterUrl}';
    var enterpriseUrl='${enterpriseUrl}';
    var cdnUrl='${cdnUrl}';
    var currentCompanyId = '<%=request.getSession().getAttribute("companyId") %>';
    var currentUserId = '<%=request.getSession().getAttribute("userId") %>';
    var currentCompanyUserId = '<%=request.getSession().getAttribute("companyUserId") %>';
    var currentRoleCodes = '<%=request.getSession().getAttribute("roleCodes") %>';
</script>
