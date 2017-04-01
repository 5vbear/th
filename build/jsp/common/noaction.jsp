<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<title>权限错误</title>
<link rel="stylesheet" type="text/css" href="../../css/monitor.css">
<link rel="stylesheet" type="text/css" href="../../css/sdmenu.css" />
<link rel="stylesheet" type="text/css" href="../../css/style.css"/>
<link rel="stylesheet" type="text/css" href="../../css/menu.css"/>
<link rel="stylesheet" type="text/css" href="../../css/channel.css">
<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
<script src="../js/sdmenu.js" language="javascript"></script>

<script type="text/javascript">
	//          
	function logout(){
	window.open("/th/index.jsp","_parent");
}
	function mainjsp(){
		window.open("/th/jsp/main.jsp","_parent");
	}
	//
</script>

</head>
<body>
<div class="x-title"><span>&nbsp;&nbsp;权限错误</span></div>

<fieldset>

该用户权限错误,请重新登录或返回主画面！
</fieldset>
		
				
			<div style="alain">
				<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="退出" id ="save" onclick="logout();" />
				<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="主画面" id ="backpage" onclick="mainjsp();"/>
			</div>
			
</body>

</html>