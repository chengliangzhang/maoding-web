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
		.forgetOBox{padding-top: 60px;}
		.forgetOBox .form-group{text-align: left;}
		.forgetOBox .forgetStep1OBox fieldset section input.startCode{opacity:0.5;cursor: not-allowed;}
		.forgetOBox .forgetStep1OBox fieldset section input.endCode{opacity:0;cursor: pointer;}
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
	<div class="container content forgetOBox">
		<div class="row">
			<div class="col-24-md-10 col-24-md-offset-7 col-24-sm-14 col-24-sm-offset-6 " id="forgetStepOBox">

			</div>
		</div>
	</div>
	<div class="footer text-center" style="border: none;background: transparent;">
		<p class="m-t"> <small>深圳市卯丁技术有限公司 © 2017 粤ICP备16006511号-1</small> </p>
	</div>
</div>

<%@ include file="../lib_footer_new.jsp" %>
<script type="text/javascript">
    $(function(){
        home_forgetPwd.init();
    });
</script>
</body>
</html>