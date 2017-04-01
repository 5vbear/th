<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%
String strContextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>下发中</title>
<link rel="stylesheet" type="text/css" href="<%=strContextPath %>/css/monitor.css">
<link rel="stylesheet" type="text/css" href="<%=strContextPath %>/css/sdmenu.css" />
<link rel="stylesheet" type="text/css" href="<%=strContextPath %>/css/style.css"/>
<link rel="stylesheet" type="text/css" href="<%=strContextPath %>/css/menu.css"/>
<link rel="stylesheet" type="text/css" href="<%=strContextPath %>/css/channel.css">
<script src="../js/sdmenu.js" language="javascript"></script>
</head>
<body onload="go();">
<strong>请在30秒内做完操作：</strong>
<p>
<div id="leftSeconds">
还剩余30秒
</div>
<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="确认" id ="returnupdate" onclick="goupdate();" />
<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="取消" id ="send" onclick="cancelSend();" />
</body>
<script language="JavaScript">
var id, iM = 0, iS = 1;

start = new Date();

function go(){
	now = new Date();
	time = (now.getTime() - start.getTime()) / 1000;
	time = Math.floor(time);
	iS = time % 60;
	iM = Math.floor(time / 60);
	if (iS <= 30) {
		document.getElementById("leftSeconds").innerHTML = "还剩余" + (30 - iS) + "秒";
		} else {
			goupdate();
		}
	id = setTimeout("go()", 1000);
}
function goupdate(){
	window.dialogArguments.send();
	 self.close();
}
function cancelSend(){
	self.close();
}
</script>
</html>