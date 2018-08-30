<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if IE 8]> <html class="ie8"> <![endif]-->
<!--[if IE 9]> <html class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html> <!--<![endif]-->
<head>
    <title>卯丁－设计同行的工作方式</title>
    <%@ include file="../lib_header_new.jsp" %>
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/jsTree/style.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/icheck-1.x/skins/minimal/green.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/icheck-1.x/skins/square/blue.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/select2/css/select2.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/x-editable/bootstrap3-editable/css/bootstrap-editable.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/x-editable/inputs-ext/typeaheadjs/lib/typeahead.js-bootstrap.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/jquery-treegrid/css/jquery.treegrid.css">

    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/webuploader/webuploader.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/cropper/cropper.min.css">

    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/icon/iconfont.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/footable/footable.core.css">

</head>
<body class="no-skin-config">
<input type="hidden" id="ipt_companyName" value="${companyInfo.companyName}"/>
<input type="hidden" id="ipt_companyFilePath" value="${companyInfo.filePath}"/>
<input type="hidden" id="ipt_isLoginComeIn" value="${isLoginComeIn}"/>
<div id="wrapper">
    <!--头部信息-->
    <div id="m_top" class="row white-bg"></div>
    <!--头部信息 end-->
    <div id="left-menu-box"></div>
    <div id="page-wrapper" class="bg-v1 workbench gray-bg">
        <div id="content-right" class="row">

        </div>
        <div class="clearfix"></div>
    </div>

</div>
<%--<!--底部信息-->--%>
<script type="text/javascript">
    var currentNav = 'workbench';
</script>
<%@ include file="../lib_footer_new.jsp" %>

<script type="text/javascript" src="<%=rootPath %>/assets/lib/icheck-1.x/icheck.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/citys/jquery.cityselect.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/select2/js/select2.full.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/select2/js/i18n/zh-CN.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/x-editable/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/x-editable/inputs-ext/typeaheadjs/lib/typeahead.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/x-editable/inputs-ext/typeaheadjs/typeaheadjs.js"></script>

<script type="text/javascript" src="<%=rootPath %>/assets/lib/jsTree/jstree.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/jquery-treegrid/js/jquery.treegrid.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/jquery-treegrid/js/jquery.treegrid.bootstrap3.js"></script>


<script type="text/javascript" src="<%=rootPath %>/assets/lib/cropper/cropper.js"></script>

<!-- include summernote css/js-->
<link href="<%=rootPath %>/assets/lib/summernote/summernote.css" rel="stylesheet"/>
<script src="<%=rootPath %>/assets/lib/summernote/summernote.js"></script>
<script src="<%=rootPath %>/assets/lib/summernote/lang/summernote-zh-CN.js"></script>

<script type="text/javascript" src="<%=rootPath %>/assets/lib/webuploader/webuploader.nolog.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/sortable/sortable.min.js"></script>

<script type="text/javascript" src="<%=rootPath %>/assets/lib/chartJs/Chart.bundle.js"></script>


<script type="text/javascript" src="<%=rootPath %>/assets/lib/footable/footable.all.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/js/router.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/context-menu/context-menu.js"></script>



<script type="text/javascript">
    $(function () {
        home_workbench_new.init();

    });
</script>
</body>
</html>
