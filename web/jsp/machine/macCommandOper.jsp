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
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macCommandOper.jsp";
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
			
			function disable(element){
				document.getElementById(element).className = '';
				document.getElementById(element).style.cssFloat = 'left';
				document.getElementById(element).disabled="disabled";
			}
			
			function enable(element){
				document.getElementById(element).className = 'leftBtn';
				document.getElementById(element).disabled="";
			}
			
			function dOper(macIDStd, operType){
				var macIDStd = document.getElementById('macIDStd').value;
				if(macIDStd == null || macIDStd == "" || macIDStd =="null"){
					alert('请选择要操作的端机.');
					return;
				}
				if(operType == null){
					alert('无效操作!');
				}
				xmlHttp = this.getXmlHttpRequest();
				if(xmlHttp == null) {
				    alert("Explore is Unsupport XmlHttpRequest！");
				    return;
				}
				var url ='/th/machineServlet?model=command&method=dOper&macIDStd=' + macIDStd + '&operType=' + operType;
				xmlHttp.open("GET", url, true);
				xmlHttp.onreadystatechange = this.callBack;
				xmlHttp.send(null);
			}

			function getXmlHttpRequest() {
			    try {
			        // Firefox, Opera 8.0+, Safari
			        var xmlHttp = new XMLHttpRequest();
			    } catch (e) {
			        // Internet Explorer
			        try {
			            xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
			        } catch (e) {
			            try {
			                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			            } catch (e) {
			                alert("XMLHTTP is null");
			                return false;
			            }
			        }
			    }

			    return  xmlHttp; 
			}

			function callBack() {
			    if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			        var res = xmlHttp.responseText;
			        if(res != ""){
			        	var attributes = res.split("&");
			        	alert(attributes[0]);
			        	if(attributes[1] == 1 || attributes[1] =='1'){
			        		document.getElementById('macstatus').innerHTML = attributes[2];
			        	}
			        } else {
			        	alert("指令执行出现异常,请联系管理员！");
			        }
			    }
			}

		</script>
		<title>端机指令</title>
	</head>
	<body style="background-color: #fff;">
		<div>
		    <div>
				<form name=myform action="" method=post>
					<input type="hidden" id="macIDStd" value="${macIDStd}">
					<table id="mytable" cellspacing="0">
						<tr>
							<% 
								String macIDStd = (String)request.getAttribute("macIDStd");
								if(StringUtils.isBlank(macIDStd)){
							%>
							<th class="row" colspan="1"><span style="color:red;font-weight:bold">请选择你要操作的端机</span></td>
							<% }else{ %>
							<th class="row" colspan="1"><span style="color:blue;font-weight:bold">${orgNameStd}-${macNameStd}:</span><span id="macstatus" style="color:blue;font-weight:bold">${operName}</span></td>
							<% } %>
						</tr>
						<tr>
							<td class="row">
								<input type="button" style="margin-left:3px;" id="startScreen" value="启动截屏" class="leftBtn" onclick="dOper('${macIDStd}', 6)"/>
								<input type="button" style="margin-left:3px;" id="endScreen"  value="停止截屏" class="leftBtn"  onclick="dOper('${macIDStd}', 7)"/>
							</td>
						</tr>
						<tr>
							<td class="row">
								<input type="button" style="margin-left:3px;" id="startAdPlay" value="开始广告播放" class="leftBtn" onclick="dOper('${macIDStd}', 10)" />
								<input type="button" style="margin-left:3px;" id="endAdPlay" value="停止广告播放" class="leftBtn" onclick="dOper('${macIDStd}', 11)" />
							</td>
						</tr>
						<tr>
							<td class="row">
								<input type="button" style="margin-left:3px;" id="startAdPlay" value="开始播放临时广告" class="leftBtn" onclick="dOper('${macIDStd}', 12)" />
								<input type="button" style="margin-left:3px;" id="endAdPlay" value="停止播放临时广告" class="leftBtn" onclick="dOper('${macIDStd}', 13)" />
							</td>
						</tr>
						<tr>
							<td class="row">
								<input type="button" style="margin-left:3px;" id="startAdPlay" value="发消息" class="leftBtn" onclick="dOper('${macIDStd}', 12)" />
								<input type="button" style="margin-left:3px;" id="startAdPlay" value="远程接管" class="leftBtn" onclick="dOper('${macIDStd}', 12)" />
							</td>
						</tr>
						<tr>
							<td class="row">
								<input type="button" style="margin-left:3px;" id="stop" value="报停" class="leftBtn" onclick="dOper('${macIDStd}', 2)" />
								<input type="button" style="margin-left:3px;" id="stop" value="启用" class="leftBtn" onclick="dOper('${macIDStd}', 3)" />
								<input type="button" style="margin-left:3px;" id="lock" value="锁定" class="leftBtn" onclick="dOper('${macIDStd}', 28)" />
								<input type="button" style="margin-left:3px;" id="unlock" value="解锁" class="leftBtn" onclick="dOper('${macIDStd}', 29)" />
							</td>
						</tr>
						<tr>
							<td class="row">
								<input type="button" style="margin-left:3px;" id="locate" value="定位" class="leftBtn" onclick="dOper('${macIDStd}', 27)" />
								<input type="button" style="margin-left:3px;" id="restartPC" value="重启" class="leftBtn" onclick="dOper('${macIDStd}', 5)" />
								<input type="button" style="margin-left:3px;" id="closePC" value="关机" class="leftBtn" onclick="dOper('${macIDStd}', 4)" />
								<input type="button" style="margin-left:3px;" id="cleanData" value="清除端机冗余数据" class="leftBtn" onclick="dOper('${macIDStd}', 8)" />
							</td>
						</tr>
					</table>
				</form>
	    	</div>
	  	</div>
	</body>
</html>