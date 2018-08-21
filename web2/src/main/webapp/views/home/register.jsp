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
        /*兼容手机端footer挡住注册按钮*/
        @media only screen and (min-height: 0px) and (max-height: 600px) {
            .wrapper .footer{
                position: relative;display: block;
            }
        }
    </style>
</head>
<body class="bg-v1">
<div class="wrapper">
    <input type="password" style="display:none;">
    <div class="container content registerContent">
        <div id="step1" class="row">
            <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3" id="registerStepBox">

            </div>
        </div>
    </div>
    <div class="footer text-center" style="border: none;background: transparent;">
        <p class="m-t" >
            <small>深圳市卯丁技术有限公司 © 2017 粤ICP备16006511号-1</small>
        </p>
    </div>
</div>
<%@ include file="../lib_footer_new.jsp" %>
<script type="text/javascript">
    $(function () {
        home_register.init();
    });
</script>
</body>
</html>