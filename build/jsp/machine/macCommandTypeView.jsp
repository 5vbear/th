<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page isELIgnored="false"%>
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    User user = (User) session.getAttribute("user_info");
    String realname =null;
    if (user == null) {
	    response.setContentType("text/html; charset=utf-8");
	    response.sendRedirect("/th/index.jsp");
    } else {
	    realname = user.getReal_name();
    }
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/style.css"/>
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
		<style type="text/css">
			body {
				font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica,
					sans-serif;
			}
			
			a {
				color: #c75f3e;
			}
			
			#mytable {
				width: 100%;
				padding: 0;
				margin: 0;
			}
			
			caption {
				padding: 0 0 5px 0;
				width: 700px;
				font: italic 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
				text-align: right;
			}
			
			th {
				font: bold 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
				color: #4f6b72;
				border-right: 1px solid #C1DAD7;
				border-bottom: 1px solid #C1DAD7;
				border-top: 1px solid #C1DAD7;
				letter-spacing: 2px;
				text-transform: uppercase;
				text-align: left;
				padding: 6px 6px 6px 12px;
				background: #CAE8EA no-repeat;
			}
			
			th.nobg {
				border-top: 0;
				border-left: 0;
				border-right: 1px solid #C1DAD7;
				background: none;
			}
			
			td {
				border-right: 1px solid #C1DAD7;
				border-bottom: 1px solid #C1DAD7;
				font-size: 11px;
				padding: 6px 6px 6px 12px;
				color: #4f6b72;
			}
			
			td.alt {
				background: #F5FAFA;
				color: #797268;
			}
			
			th.spec {
				border-left: 1px solid #C1DAD7;
				border-top: 0;
				background: #fff no-repeat;
				font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
			}
			
			th.specalt {
				border-left: 1px solid #C1DAD7;
				border-top: 0;
				background: #f5fafa no-repeat;
				font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
				color: #797268;
			}
			/*---------for IE 5.x bug*/
			html>body td {
				font-size: 11px;
			}
			
			body,td,th {
				font-family: 宋体, Arial;
				font-size: 12px;
			}
		</style>
		<script type="text/javascript">	
			window.onload = function showtable() {
				var tablename = document.getElementById("mytable");
				var li = tablename.getElementsByTagName("tr");
				for ( var i = 0; i < li.length; i++) {	
					if (i % 2 == 0) {
						li[i].style.backgroundColor = "#E5EEFD";
						li[i].onmouseover = function() {
							this.style.backgroundColor = "#CAE8EA";
						}
						li[i].onmouseout = function() {
							this.style.backgroundColor = "#E5EEFD";
						}
					} else {
						li[i].style.backgroundColor = "#FFFFFF";
						li[i].onmouseover = function() {
							this.style.backgroundColor = "#CAE8EA";
						}
						li[i].onmouseout = function() {
							this.style.backgroundColor = "#FFFFFF";
						}
					}
				}
			}
			
			function sendMessage(orgIDStd){
				var paramers="dialogWidth:400px;DialogHeight:250px;status:no;help:no;unadorned:no;resizable:no;status:no;";  
				var url = "/th/jsp/machine/macCommandMessage.jsp";
				var ret=window.showModalDialog(url,'',paramers);  
				if (ret == null || ret == "") {
					return;
				}
				self.location = '/th/machineServlet?model=command&method=orgOper&orgIDStd=' + orgIDStd + '&operType=30&content=' + ret;	
			}

		</script>
		<title>端机指令</title>
	</head>
	<body style="background-color: #fff;">
		<div>
		    <div>
				<form name=myform action="" method=post>
					<table id="mytable" cellspacing="0">
						<tr>
							<th class="row" colspan="1"><span style="color:blue;font-weight:bold">${orgNameStd}:</span></td>
						</tr>
						 
						<tr>
							<td class="row" height="22">
								<!--
								<input type="button" style="margin-left:3px;" id="sendMsg" value="发送消息" class="leftBtn" onclick="sendMessage('${orgIDStd}')" />
								-->
								请选择端机
							</td>
						</tr>
						 
					</table>
				</form>
	    	</div>
	  	</div>
	</body>
</html>