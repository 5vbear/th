<%@ page language="java" contentType="text/html; charset=utf-8"%>
<html>
<head>
<title>菜单</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="../../css/login.css" media="all">
<script language="JavaScript">
function macShow(){
	window.document.form_data.orderType.value ="2";
	window.document.form_data.pageID.value ="1";
    window.document.form_data.target =  "_self";
    window.document.form_data.submit();
}


function searchGuestBook(){
	window.document.form_data.pageID.value ="3";
    window.document.form_data.target =  "_self";
    window.document.form_data.submit();
}

function searchMessage(){
	window.document.form_data.pageID.value ="4";
    window.document.form_data.target =  "_self";
    window.document.form_data.submit();
}
</script>
</head>
<body>
<div id="box">
<form method="post" action="/th/mobile.html" name="form_data">
<input type="hidden" name="orderType"   value="2">
<input type="hidden" name="pageID"   value="">
<BR>
<table width="160" height="80" border="0" align="center" valign="center" cellpadding="0" cellspacing="0" id="__01">
	<tr>
	    <td width="80px" height="30px">
	    <div style="top: 30px; position: relative;width: 40px; height: 20px;float:left;">
		<input type="button" name="loginButton" id="loginButton" value="矩阵列表" onclick="macShow();"/>
		</div>
		</td>
	</tr>
	<tr>
	    <td width="80px" height="30px">
		<div style="top: 50px; position: relative;width: 40px; height: 20px;float:left;">
		<input type="button" name="loginButton" id="loginButton" value="意 见 簿" onclick="searchGuestBook();"/>
		</div>
		</td>
	</tr>
	<tr>
		<td width="80px" height="30px">
		<div style="top: 70px; position: relative;width: 40px; height: 20px;float:left;">
		<input type="button" name="cancelButton" id="cancelButton" value="历史消息" onclick="searchMessage();"/>
		</div>
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>