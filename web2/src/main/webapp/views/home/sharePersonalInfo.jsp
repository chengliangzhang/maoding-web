<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--[if IE 8]> <html class="ie8"> <![endif]-->
<!--[if IE 9]> <html class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html> <!--<![endif]-->
<head>
    <title>个人信息</title>
    <%@ include file="../lib_header_new_lite.jsp" %>
    <style>
        .user-info-box{
            border: solid 1px #d4d4d4;
            border-radius: 5px;
            min-height: 160px;
            box-shadow: 2px 4px 10px 4px #d4d4d4;
        }
        .company-name{
            text-align: left;
            font-size: 14px;
            font-family: "微软雅黑";
            color: #a9a9a9;
        }
        .user-name{
            text-align: left;
            color: #212121;
            margin-top: 50px;
            font-size: 20px;
        }
        .cellphone{
            text-align: left;
            color: #8c8c8c;
        }
        .img-circle{
            width: 55px;height: 55px;margin-top: 20px;margin-right: 5px;
        }
        .btn-box a{
            width: 100%; text-align: center;margin-top: 35px;height: 48px;padding: 13px;
        }
        .col-md-8{
            width: 67%;
            float: left;
        }
        .col-md-4{
            width: 33%;
            float: left;
        }
    </style>
</head>
<body class="bg-v1">
<%--<!--=== 页头 ===-->--%>
<%--<jsp:include page="../../../admin/directives/header/adminLoginHeader.jsp"></jsp:include>--%>
<%--<!--=== 页头结束 ===-->--%>

    <input type="hidden" id="serverUrl" value="${serverUrl}" />
    <div class="container content shareRegisterOBox">
        <div id="step1" class="row">
            <div class="" id="shareStep1">
                <div class="middle-box text-center loginscreen  animated fadeInDown">
                    <div class="white-bg user-info-box">
                        <div class="col-md-8">
                            <p class="m-t company-name">${companyInfo.companyName}</p>
                            <p class="user-name">${userInfo.companyUserName}</p>
                            <p class="cellphone">${userInfo.cellphone}</p>
                        </div>
                        <div class="col-md-4">
                            <img alt="image" class="img-circle m-t-n-xs" src="${fastdfsUrl}${userInfo.headImg}" >
                        </div>
                        <div class="clearfix"></div>
                    </div>
                    <%--<div class="btn-box">
                        <a class="btn-u btn-u-dark-blue rounded" href="javascript:void(0)">前往卯丁</a>
                    </div>--%>
                </div>
            </div>
        </div>
    </div>

    <div class="maoding-footer-box"></div>

<%@ include file="../lib_footer_new_lite.jsp" %>
<script type="text/javascript">
    $(function(){
        var serverUrl = $('#serverUrl').val();
        var u = navigator.userAgent;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端

        function android(){
            window.location.href = "maoding://com.tyrbl.wjtr"; /***打开app的协议，有安卓同事提供***/
            window.setTimeout(function(){
                window.location.href = serverUrl+"android/index.html"; /***打开app的协议，有安卓同事提供***/
            },2000);

        };
        function ios(){
            window.location.href = "iOSSharePersonDesignPlusAPP://"; /***打开app的协议，有ios同事提供***/
            window.setTimeout(function(){
                window.location.href = "https://www.pgyer.com/TsDw"; /***下载app的地址***/
            },2000);
        };

        $('.btn-box a').on('click',function () {
            if(isAndroid){
                android()
            }else if(isiOS){
                ios()
            }
        });
    });
</script>
</body>
</html>