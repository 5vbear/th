<%@ page language="java" contentType="text/html; charset=utf-8"%>
<html>
<head>
<title>登录</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="css/login.css" media="all">
<script type="text/javascript">
	function cancel(){
		document.getElementById('username').value="";
		document.getElementById('password').value="";
	} 
	
</script>
</head>
<body>
<%
	String pre_gamen = request.getParameter("pre_gamen_id");
	if ("pre_login".equalsIgnoreCase(pre_gamen)) {
%>
<div style="position: absolute; left: 450px; top: 458px;"><span
	id="FailureText" style="color: Red;">用户名密码不正确</span></div>
<%
	}
%>
<div id="box">
<form method="post" action="/th/login.html" id="form1">
<div style="position: absolute; top: 20px; left: 20px; width: 295px; height: 54px;background-image: url('image/login/login_LOGO.png');"></div>
<!-- <div style="position: absolute; top: 20px; left: 20px; text-align:center"></div> -->
<table width="544" height="331" border="0" align="center" valign="center" cellpadding="0" cellspacing="0" id="__01">
	<tr>
		<td background="image/login/login_blank.png" width="544px" height="331px">
		<div id="DivParent" style="width:544px; height:331px;">
		<div style="left: 260px; top: 150px; position: relative;"><input
			name="username" type="text" id="username"
			style="height: 20px; width: 150px;" /></div>
		<div style="left: 260px; top: 175px; position: relative;"><input
			name="password" type="password" id="password"
			style="height: 20px; width: 150px;" /></div>
			
		<div style="left: 150px; top: 220px; position: relative;width: 90px; height: 28px;float:left;"><input
			type="submit" name="loginButton" id="loginButton" value=""
			style="background:url(image/ok.jpg) no-repeat;width:90px;height:28px;"/></div>
		<div style="left: 230px; top: 220px; position: relative;width: 90px; height: 28px;float:left;"><input
			type="button" name="cancelButton" id="cancelButton" value=""
			onclick="javascript:cancel();"
			style="background:url(image/cancel.jpg) no-repeat;width:90px;height:28px;"/></div>
			</div>
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>