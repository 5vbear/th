<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%> 
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="/th/css/machine.css">
		<title>发送信息</title>
	</head>
	<SCRIPT LANGUAGE="JavaScript">		
		function saveData(){
			var reason = document.getElementById('reason').value;
			reason = reason.replace(/^\s+|\s+$/g, '');
			if(reason == null || reason ==''){
				alert('请填写要发送的消息!');
				return;
			}
			window.returnValue = reason;
			window.showModalDialog("/th/jsp/machine/macCommandMessageTip.jsp",'',"dialogWidth:240px;DialogHeight:20px;status:no;help:no;unadorned:no;resizable:no;status:no;location:no;");
			window.close();
		}
		
		function cancel(){
			window.close();
		}
	</script>
	<body style="background-color: #fff;">
		<div>
			<fieldset><legend>发送信息</legend>
			<div style="border: 1px solid #565656; overflow: scroll;">
			<table>					
				<tr>
					<td><textarea id="reason" name="reason" style="width:350px;height:60px"></textarea></td>
				</tr>
			</table>
    	</div>
		</fieldset>
    	<input type="button" class="leftBtn" value="确定" onclick="saveData()"/>
    	<input type="button" class="leftBtn" value="取消" onclick="cancel()"/>
	</body>
</html>