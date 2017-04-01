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
    String jspName = "macInfoView.jsp";
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

    String machineID = (String)request.getAttribute("macIdStd");
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
		<title>端机信息</title>
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
		HashMap moreInfo = (HashMap) request.getAttribute( "moreInfo" );
		HashMap macConfig = (HashMap) request.getAttribute( "macConfig" );

		String hotime = (String)deployInfo.get("HOTIME");
		String motime = (String)deployInfo.get("MOTIME");
		String hctime = (String)deployInfo.get("HCTIME");
		String mctime = (String)deployInfo.get("MCTIME");
		String orgId = (String)deployInfo.get("OID");
		if(hotime==null){
			hotime = "08";
		}
		if(motime==null){
			motime = "30";
		}
		if(hctime==null){
			hctime = "17";
		}
		if(mctime==null){
			mctime = "30";
		}
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
		
		function saveHardData(){
			xmlHttp = this.getXmlHttpRequest();
			if(xmlHttp == null) {
			    alert("Explore is Unsupport XmlHttpRequest！");
			    return;
			}
			var cpu = document.getElementById('cpu').value;
			var oSystem = document.getElementById('oSystem').value;
			var hardNum = document.getElementById('hardNum').value;
			var memoryNum = document.getElementById('memoryNum').value;
			
			var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?funcId=7&macIdStd=<%=machineID%>&cpu="+cpu+"&oSystem="+oSystem+"&hardNum="+hardNum+"&memoryNum="+memoryNum;
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
		        if(res=="1"){
		        	document.getElementById('saveData').style.display = 'none';
					document.getElementById('unEditable').style.display = 'none';
					document.getElementById('editable').style.display = '';
					document.getElementById('cpu').disabled = 'true';
					document.getElementById('oSystem').disabled = 'true';
					document.getElementById('hardNum').disabled = 'true';
					document.getElementById('memoryNum').disabled = 'true';
		        } else {
		        	alert("指令执行出现异常,请联系管理员！");
			    }
		    }
		}
		function editHardInfo(){
			document.getElementById('saveData').style.display = '';
			document.getElementById('unEditable').style.display = '';
			document.getElementById('editable').style.display = 'none';
			document.getElementById('cpu').disabled = false;
			document.getElementById('oSystem').disabled = false;
			document.getElementById('hardNum').disabled = false;
			document.getElementById('memoryNum').disabled = false;
		}
		function unEditHardInfo(){
			window.document.myform.reset();
			document.getElementById('saveData').style.display = 'none';
			document.getElementById('unEditable').style.display = 'none';
			document.getElementById('editable').style.display = '';
			document.getElementById('cpu').disabled = 'true';
			document.getElementById('oSystem').disabled = 'true';
			document.getElementById('hardNum').disabled = 'true';
			document.getElementById('memoryNum').disabled = 'true';
		}
	</script>
	<body style="background-color: #fff;height: 100%">
		<div>
		    <div>
				<form name="myform" action="" method=post>
					<table id="mytable" cellspacing="0">
						<tr>
							<th scope="col" colspan="2" class="machineTd"><% out.print(parseStr(deployInfo.get("MMARK")));  %></th>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机档案信息:</td>
						</tr>
						<tr>
							<td class="row" width="30%">设备类型:</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("MTYPE")));  %></td>
						</tr>
						<tr>
							<td class="row">出产日期</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("MDATE")));  %></td>
						</tr>
						<tr>
							<td class="row">设备厂商</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("MFACTORY")));  %></td>
						</tr>
						<tr>
							<td class="row">设备编号</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("DNO")));  %></td>
						</tr>
						<tr>
							<td class="row">保修年限</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("RYEARS"))); %></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机部署信息:</td>
						</tr>
						<tr>
							<td class="row">所属银行:</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("ONAME"))); %></td>
						</tr>
						<tr>
							<td class="row">网点名称</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("BNAME"))); %></td>
						</tr>
						<tr>
							<td class="row">网点地址</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("BADDRESS"))); %></td>
						</tr>
						<tr>
							<td class="row">网点编号</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("BNO"))); %></td>
						</tr>
						<tr>
							<td class="row">保修年限</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("RYEARS"))); %></td>
						</tr>
						<tr>
							<td class="row">负责人</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("MANAME"))); %></td>
						</tr>
						<tr>
							<td class="row">联系人</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("CONAME"))); %></td>
						</tr>
						<tr>
							<td class="row">联系电话</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("TPHONE"))); %></td>
						</tr>
						<tr>
							<td class="row">联系手机</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("CPHONE"))); %></td>
						</tr>
						<tr>
							<td class="row">营业时间</td>
							<td class="row"><%=hotime %>:<%=motime %>-<%=hctime %>:<%=mctime %></td>
						</tr>
						<tr>
							<td class="row">营业周期</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("ODATE"))); %></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">报停历史:</td>
						</tr>
						<%
							HashMap[] alertInfo = (HashMap[]) request.getAttribute( "alertInfo" ); 
							for ( int i = 0; i<alertInfo.length; i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row'>" + alertInfo[i].get("PTIME") + "报停</td>");
								out.print("	 <td class='row'>" + alertInfo[i].get("RTIME") + "恢复</td>");
								out.print("</tr>");
							}
						%>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">迁移历史:</td>
						</tr>
						<%
							HashMap[] historyInfo = (HashMap[]) request.getAttribute( "historyInfo" ); 
							for ( int i = 0; i<historyInfo.length; i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row'>" + historyInfo[i].get("TTIME") + "</td>");
								out.print("	 <td class='row'>" + historyInfo[i].get("TDEST") + "</td>");
								out.print("</tr>");
							}
						%>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机配置信息:</td>
						</tr>
						<tr>
							<td class="row">开机时间</td>
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
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机硬件信息:		
		    	<input type="button" class="rightBtn" id="editable" value="编辑" onclick="editHardInfo()"/>
		    	<input type="button" class="rightBtn" id="unEditable" style="display:none" value="撤销" onclick="unEditHardInfo()"/>
		    	<input type="button" class="rightBtn" id="saveData" style="display:none" value="保存" onclick="saveHardData()"/>
							</td>
						</tr>
						<tr>
							<td class="row">CPU频率</td>
							<td class="row"><input type="text" id="cpu" disabled="true" name="cpu" value="<% out.print(parseStr(moreInfo.get("CPUFREQ")));  %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">操作系统</td>
							<td class="row"><input type="text" id="oSystem" disabled="true" name="oSystem" value="<% out.print(parseStr(moreInfo.get("OS")));  %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">硬盘容量</td>
							<td class="row"><input type="text" id="hardNum" disabled="true" name="hardNum" value="<% out.print(parseStr(moreInfo.get("DSIZE")));  %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">内存大小</td>
							<td class="row"><input type="text" id="memoryNum" disabled="true" name="memoryNum" value="<% out.print(parseStr(moreInfo.get("MSIZE")));  %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">版本信息:</td>
						</tr>
						<tr>
							<td class="row">总版本号</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("OVERSION")));  %></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">浏览器信息:</td>
						</tr>
						<tr>
							<td class="row">浏览器名称:</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("BROWNAME")));  %></td>
						</tr>
						<tr>
							<td class="row">浏览器版本</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("BROWVERSION")));  %></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">代理设置信息:</td>
						</tr>
						<tr>
							<td class="row">客户端代理地址</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("PROXY_HOST")));  %></td>
						</tr>
						<tr>
							<td class="row">客户端代理端口</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("PROXY_PORT")));  %></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机定位信息:</td>
						</tr>
						<tr>
							<td class="row" width="30%">定位时间</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("LTIME")));  %></td>
						</tr>
						<tr>
							<td class="row">经度</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("LLON")));  %></td>
						</tr>
						<tr>
							<td class="row">纬度</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("LLAT")));  %></td>
						</tr>
						<tr>
							<td class="row">范围</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("LRAD")));  %></td>
						</tr>
						<tr>
							<td class="row">定位点名称</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("LNAME")));  %></td>
						</tr>
					</table>
				</form>
	    	</div>
	  	</div>
	</body>
</html>