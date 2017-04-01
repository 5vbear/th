<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="th.entity.UKeyBean"%>
<%@page import="th.util.StringUtils"%>
<%@page import="th.util.DateUtil"%>
<%@page import="th.com.util.Define4Report"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>注册信息审核</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path;
	String result = (String) request.getAttribute("result");
	if("true".equals(result)){
		result = "注册信息审核中,请耐心等待......";
	} else {
		result = "注册信息审核失败,请联系工作人员处理。";
	}
%>

<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/report.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/zTree/css/demo.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/zTree/css/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="<%=basePath%>/js/Calendar.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery.ztree.excheck-3.5.js"></script>
<style type="text/css">
* { padding:0; margin:0; }
body { text-align:center; padding:50px;}
.select-outer { display:inline-block; *display:inline; zoom:1; border:1px solid #ccc; }
.select-inner { display:inline-block; *display:inline; zoom:1; height:19px; overflow:hidden; position:relative; }
.select-h { border:1px solid #ccc; height:21px; font-size:12px; position:relative; zoom:1; margin:-1px; }
.select-outer-m { display:inline-block; *display:inline; zoom:1; }
.select-m { height:21px; font-size:12px; position:relative; zoom:1; margin:-1px; }
.select-wh200 { padding:3px; }
.select-wh200 .select-inner { width:200px; }
.select-wh200 select { width:202px; }
</style>
</head>
<body>
<form class="x-client-form" action="" name="" method="post">
  <div class="select-outer-m select-wh200">
  <div class="select-inner">
<span class="select-m">&nbsp;<%=result%>&nbsp;</span>
</div>
  </div>
<br/><br/>
  <input type="button" style="width:60px ;height: 30px;" value="关闭" onclick ="window.close();" />
</form>
</body>
</html>