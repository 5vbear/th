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
    String jspName = "macSychInfoView.jsp";
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
		</script>
		<title>端机同步</title>
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
	<%!
		private String parseStr(Object obj){
			if(obj != null){									
				return obj.toString();
			}else{
				return "";
			}
		}
	%>
	<% 
		HashMap deployInfo = (HashMap) request.getAttribute( "deployInfo" );
		HashMap macConfig = (HashMap) request.getAttribute( "macConfig" );
		String orgId = (String)deployInfo.get("OID");
	%>
	<SCRIPT LANGUAGE="JavaScript">
	
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
		
		function macSync(){
			xmlHttp = this.getXmlHttpRequest();
			if(xmlHttp == null) {
			    alert("Explore is Unsupport XmlHttpRequest！");
			    return;
			}
			var url ='/th/machineServlet?model=sych&method=macSync&machineId=${macIdStd}';
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
	        	alert("同步成功！");
		    }else if(xmlHttp.readyState == 4 && xmlHttp.status != 200){
	        	alert("指令执行出现异常,请联系管理员！");
	        }
		}
	</script>
	<body style="background-color: #fff;height: 100%">
		<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
	    	<div class="x-chanelName" style="width:100%;padding-top:3px">
		    	<input type="button" class="leftBtn" id="editable" value="同步" onclick="macSync()"/>
		    </div>
		</div>
		<div>
		    <div>
				<form name=myform action="" method=post>
					<table id="mytable" cellspacing="0">
						<tr>
							<th scope="col" colspan="2" class="machineTd"><% out.print(parseStr(deployInfo.get("MMARK")));  %></th>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">配置信息:</td>
						</tr>
						<tr>
							<td class="row" style="width:30%">开机时间</td>
							<td class="row"><% out.print(parseStr(macConfig.get("STIME")));  %></td>
						</tr>
						<tr>
							<td class="row">关机时间</td>
							<td class="row"><% out.print(parseStr(macConfig.get("CTIME")));  %></td>
						</tr>
						<tr>
							<td class="row">屏幕保护时间</td>
							<td class="row"><% out.print(parseStr(macConfig.get("SPTIME")));  %>秒</td>
						</tr>
						<tr>
							<td class="row">写保护目录</td>
							<td class="row"><% out.print(parseStr(macConfig.get("PROPATH")));  %></td>
						</tr>
						<tr>
							<td class="row">截屏结束时间</td>
							<td class="row"><% out.print(parseStr(macConfig.get("SCRTIME"))); %>秒</td>
						</tr>
						<tr>
							<td class="row">截屏间隔时间</td>
							<td class="row"><% out.print(parseStr(macConfig.get("IVLTIME"))); %>秒</td>
						</tr>
						<tr>
							<td class="row">应用服务器地址</td>
							<td class="row"><% out.print(parseStr(macConfig.get("SURL"))); %></td>
						</tr>
						<tr>
							<td class="row">FTP服务器IP</td>
							<td class="row"><% out.print(parseStr(macConfig.get("FTPIP"))); %></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">节目单列表:</td>
						</tr>
						<tr>
							<td class="row">节目单名称</td>
							<td class="row">节目单描述</td>
						</tr>
						<%
							HashMap[] adList = (HashMap[]) request.getAttribute( "adList" );
							for ( int i = 0; i<adList.length; i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row'>" + adList[i].get("BILL_NAME") + "</td>");
								out.print("	 <td class='row'>" + adList[i].get("DESCRIPTION") + "</td>");
								out.print("</tr>");
							}
						%>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">频道列表:</td>
						</tr>
						<tr>
							<td class="row">频道名称</td>
							<td class="row">频道路径</td>
						</tr>
						<%
							HashMap[] channelList = (HashMap[]) request.getAttribute( "channelList" );
							for ( int i = 0; i<channelList.length; i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row'>" + channelList[i].get("CHANNEL_NAME") + "</td>");
								out.print("	 <td class='row'>" + channelList[i].get("CHANNEL_URL") + "</td>");
								out.print("</tr>");
							}
						%>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">快速入口列表:</td>
						</tr>
						<tr>
							<td class="row">快速入口名称</td>
							<td class="row">快速入口路径</td>
						</tr>
						<%
							HashMap[] quickEntryList = (HashMap[]) request.getAttribute( "quickEntryList" );
							for ( int i = 0; i<quickEntryList.length; i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row'>" + quickEntryList[i].get("CHANNEL_NAME") + "</td>");
								out.print("	 <td class='row'>" + quickEntryList[i].get("CHANNEL_URL") + "</td>");
								out.print("</tr>");
							}
						%>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">白名单列表:</td>
						</tr>
						<tr>
							<td class="row">白名单名称</td>
							<td class="row">白名单地址</td>
						</tr>
						<%
							HashMap[] whiteList = (HashMap[]) request.getAttribute( "whiteList" );
							for ( int i = 0; i<whiteList.length; i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row'>" + whiteList[i].get("REQUEST_NAME") + "</td>");
								out.print("	 <td class='row'>" + whiteList[i].get("REQUEST_URL") + "</td>");
								out.print("</tr>");
							}
						%>
					</table>
				</form>
	    	</div>
	  	</div>
	</body>
</html>