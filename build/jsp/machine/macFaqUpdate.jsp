<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%> 
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macDeployUpdate.jsp";
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
    String fid = StringUtils.transStr((String)request.getAttribute("fid"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="./css/machine.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="./css/advert.css">
		<link rel="stylesheet" type="text/css" href="./css/channel.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/style.css"/>
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<title>端机信息</title>
	</head>
	<style type="text/css">
		body {
			font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica,
				sans-serif;
			color: #4f6b72;
			background: #E6EAE9;
		}
		
		a {
			color: #c75f3e;
		}
		
		.myrtable {
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
		.widthDiv{ float:left; width: 100%;}
		.mydiv{ float:left; width: 700px;}
		.mydiv ul li{ float:left; padding-left:5px;}
		.mytable{ float:left; width: 100%;}
		#mytable1{
			width: 700px;
			padding: 0;
			margin: 0;
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
	<SCRIPT LANGUAGE="JavaScript">
	
		window.onload = function showtable() {
			//var tablename = document.getElementById("mytable");
			var li = document.getElementsByTagName("tr");
			document.getElementById("faqType").value='${faqType}';
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
		
		function saveData(){
			var bname = document.getElementById('bname').value;
		    window.document.deployForm.target = "_self";
		    window.document.deployForm.submit();
		}
		
		function update(){
			var faqType = document.getElementById("faqType").value;
			var question = document.getElementById("question").value;
			var answer = document.getElementById("answer").value;
			var remark = document.getElementById("remark").value;
			var repairman = document.getElementById("repairman").value;
			var fid = "<%=fid%>";	
			if( faqType.value== ""){
				window.alert("请选择故障类型");  
				return;
			}
			if( question== ""){
				window.alert("请填写故障描述");  
				return;
			}
			if( answer== ""){
				window.alert("请填写解决方法");  
				return;
			}
			if( remark== ""){
				window.alert("请填写备注");  
				return;
			}
			if( repairman== ""){
				window.alert("请填写修理人");  
				return;
			}
			self.location = "/th/machineServlet?model=faq&method=doupdate&pageIdx=${pageIdx}&machineName=${machineName}&machineType=${machineType}&SelectOrg=${SelectOrg}&faqType="+faqType+"&question="+question+"&answer="+answer+"&remark="+remark+"&repairman="+repairman+"&fid="+fid;
		}
		
		function editable(){
			document.getElementById('saveData').style.display = '';
			document.getElementById('unEditable').style.display = '';
			document.getElementById('editable').style.display = 'none';
			document.getElementById('orgId').disabled = false;
			document.getElementById('bname').disabled = false;
			document.getElementById('baddr').disabled = false;
		}
		
		function unEditable(){
			window.document.deployForm.reset();
			document.getElementById('saveData').style.display = 'none';
			document.getElementById('unEditable').style.display = 'none';
			document.getElementById('editable').style.display = '';
			document.getElementById('orgId').disabled = 'true';
			document.getElementById('bname').disabled = 'true';
			document.getElementById('baddr').disabled = 'true';
			//self.location = "/th/machineServlet?model=deploy&method=view&pageIdx="+${pageIdx}+"&macIdStd="+${macIdStd};
			
			
		}
		
	</script>
	<body style="background-color: #fff;">
		<div class="x-title"><span>&nbsp;&nbsp;端机管理-故障知识库</span></div>
		<table><tr style ="height:3px"></tr></table>
		<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
	    	<div class="x-chanelName" style="width:100%;padding-top:3px">
		    	<input type="button" class="leftBtn" value="更新" onclick="update()"/>
		    </div>
		</div>
		<div class="widthDiv">
		    <div class="mytable" id="tab1">
				<form name="deployForm" action="/th/machineServlet?model=faq&method=query&" method="post">
				    <%
						HashMap faqinfo = (HashMap) request.getAttribute( "faqinfo" );
				    %>
					<table class="myrtable" cellspacing="0">
						<caption></caption>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE">故障类型:</td>
						</tr>
						<tr>
							<td class="row">
							<select name="select" id="faqType" name="faqType" style="width:80px">
						        <option value=""></option>
						        <option value="1">非法进程</option>
						        <option value="2">非法服务</option>
						        <option value="3">cpu负荷过高</option>
						        <option value="4">内存负荷过高 </option>
						        <option value="5">硬盘容量不足</option>
						        <option value="6">上行速率过慢</option>
						        <option value="7">下行速率过慢</option>
						        <option value="8">非法访问率过高</option>
						        <option value="9">断线报警</option>
		    				</select>
							</td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE">故障描述:</td>
						</tr>
						<tr>
							<td class="row"><textarea id="question" rows="1" cols="40"><%=parseStr(faqinfo.get("QUESTION"))%></textarea></td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE">解决方法:</td>
						</tr>
						<tr>
							<td class="row"><textarea id="answer" rows="1" cols="40"><%=parseStr(faqinfo.get("ANSWER"))%></textarea></td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE">备注:</td>
						</tr>
						<tr>
							<td class="row"><textarea id="remark" rows="1" cols="40"><%=parseStr(faqinfo.get("REMARK"))%></textarea></td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE">设备类型:</td>
						</tr>
						<tr>
							<td class="row"><% out.print(parseStr(faqinfo.get("MACHINE_TYPE")));  %></td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE">机器名:</td>
						</tr>
						<tr>
							<td class="row"><% out.print(parseStr(faqinfo.get("MACHINE_MARK")));  %></td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE">所属组织:</td>
						</tr>
						<tr>
							<td class="row"><% out.print(parseStr(faqinfo.get("ORG_NAME")));  %></td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE">所属厂商:</td>
						</tr>
						<tr>
							<td class="row"><% out.print(parseStr(faqinfo.get("MANUFACTURER")));  %></td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE">派修部门:</td>
						</tr>
						<tr>
							<td class="row"><% out.print(parseStr(faqinfo.get("DEPARTMENT_NAME")));  %></td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE">修理人:</td>
						</tr>
						<tr>
							<td class="row"><textarea id="repairman" rows="1" cols="40"><%=parseStr(faqinfo.get("REPAIRMAN"))%></textarea></td>
						</tr>
					</table>
				</form>
		    </div>
	    </div>
	</body>
</html>