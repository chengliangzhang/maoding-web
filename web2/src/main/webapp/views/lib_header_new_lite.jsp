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

<link rel="shortcut icon" href="<%=rootPath %>/assets/img/favicon.ico">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/css/bootstrap_ext.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/jasny-bootstrap/css/jasny-bootstrap.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/jquery.spinner/css/bootstrap-spinner.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/lib/layer/theme/default/layer.css">


<link rel="stylesheet" href="<%=rootPath %>/assets/css/style.min.css">
<link rel="stylesheet" href="<%=rootPath %>/assets/css/animate.min.css">

<link rel="stylesheet" href="<%=rootPath %>/assets/css/module.min.css?v=<%=v%>">

<script type="text/javascript">
    var v = '<%=v%>';
    var rootPath = '<%=rootPath%>';
</script>
