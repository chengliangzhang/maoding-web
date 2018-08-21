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
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/icheck-1.x/skins/minimal/green.min.css">
    <link rel="stylesheet" href="<%=rootPath %>/assets/lib/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.min.css">
    <style type="text/css">
        .ui-popup.ui-popup-modal{z-index: 9999999!important;}
        .createOrgOBox #createOrgBox{
            width: 620px;
            margin:0 auto;
        }
    </style>
</head>
<body class="top-navigation">
<input type="hidden" id="isFirst" value="${isFirst}"/>
<input type="hidden" id="showPre" value="${showPre}"/>
<div id="wrapper">
    <div id="page-wrapper" class="bg-v1">

        <!--头部信息-->
        <div id="m_top" class="row white-bg"></div>

        <%--<div class="row white-bg animated fadeIn" style="padding: 15px 20px;">
            <div class="col-md-12">
                <ol class="breadcrumb">
                    <li>
                        工作台
                    </li>
                    <li class="active">
                        <b>创建组织</b>
                    </li>
                </ol>
            </div>
        </div>--%>


        <div class="wrapper wrapper-content createOrgOBox" style="padding: 30px 0 20px 0;z-index:999;position: relative;">
            <div class="container-fluid">
                <div class="row  margin-top-30 animated fadeInRight ">
                      <div id="createOrgBox">

                      </div>
                </div>
            </div>
        </div>
        <div style="clear: both;"></div>

        <!--底部信息-->
        <div id="m_bottom" class="footer fixed"></div>
    </div>
</div>

<script type="text/javascript">
    var currentNav = '';
</script>
<%@ include file="../lib_footer_new.jsp" %>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/citys/jquery.cityselect.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/assets/lib/icheck-1.x/icheck.min.js"></script>
<script type="text/javascript">
    $(function () {
        home_createOrg.init();
    });
</script>
</body>
</html>
