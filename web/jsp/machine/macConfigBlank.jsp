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

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macConfigBlank.jsp";
    logger.info( jspName + " : start" );
    User user = (User) session.getAttribute("user_info");
    String realname =null;
    if (user == null) {
	    response.setContentType("text/html; charset=utf-8");
	    response.sendRedirect("/th/index.jsp");
    } else {
	    realname = user.getReal_name();
	    logger.info("获得当前用户的用户名，用户名是： " + realname);
    }

    String machineID = StringUtils.transStr((String)request.getAttribute("machineID"));
    logger.info("machineID = " + machineID);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="./css/channel.css">
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/style.css"/>
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
		<script type="text/javascript">
			function onFold(id) {
				var vDiv = document.getElementById(id);
				vDiv.style.display = (vDiv.style.display == 'none') ? 'block' : 'none';
				var fold = document.getElementById('foldStyleId');
				if (vDiv.style.display === 'none') {
					fold.className = 'x-fold-plus';
				} else {
					fold.className = 'x-fold-minus';
				}
			}
		</script>
		<title>端机配置</title>
	</head>
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
	<SCRIPT LANGUAGE="JavaScript">
	<!-- Begin
		var checkflag = "false";
		function check(field) {
			if (checkflag == "false") {
				for (i = 0; i < field.length; i++) {
					field[i].checked = true;
				}
				checkflag = "true";
				return "取消";
			} else {
				for (i = 0; i < field.length; i++) {
					field[i].checked = false;
				}
				checkflag = "false";
				return "全选";
			}
		}
		//  End -->
	
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
	</script>
	<body style="background-color: #fff;">
		<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
	    	<div class="x-chanelName" style="width:100%;padding-top:3px">
		    </div>
		</div>
	    <div>
			<form name="configForm" action="" method=post>
				<table id="mytable" cellspacing="0">						
					<tr>
						<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">配置信息:</td>
					</tr>
					<tr>
						<td class="row" width="30%">开机时间</td>
						<td class="row"></td>
					</tr>
					<tr>
						<td class="row">关机时间</td>
						<td class="row"></td>
					</tr>
					<tr>
						<td class="row">屏幕保护时间</td>
						<td class="row"></td>
					</tr>
					<tr>
						<td class="row">写保护目录</td>
						<td class="row"></td>
					</tr>
					<tr>
						<td class="row">截屏时间</td>
						<td class="row"></td>
					</tr>
					<tr>
						<td class="row">截屏间隔时间</td>
						<td class="row"></td>
					</tr>
					<tr>
						<td class="row">应用服务器地址</td>
						<td class="row"></td>
					</tr>
					<tr>
						<td class="row">FTP服务器IP</td>
						<td class="row"></td>
					</tr>
				</table>
			</form>
    	</div>
	</body>
</html>