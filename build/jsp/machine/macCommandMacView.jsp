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
		
</script>
		<script type="text/javascript">	
			window.onload = function showtable() {
				showOpers();
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
			
			function showOpers(){
				if('${stopStatus}'=='0'){
					displayOper('stop');
				}else if('${stopStatus}' != '0' && '${startStatus}'=='0'){
					displayOper('start');
				}
				if('${srnStartStatus}'=='0'){
					displayOper('startScreen');
				}else if('${srnStartStatus}' != '0' && '${srnEndStatus}'=='0'){
					displayOper('endScreen');
				}
				if('${startAdPlayStatus}'=='0'){
					displayOper('startAdPlay');
				}else if('${startAdPlayStatus}' != '0' && '${endAdPlayStatus}'=='0'){
					displayOper('endAdPlay');
				}
				if('${startTemAdPlayStatus}'=='0'){
					displayOper('startTemAdPlay');
				}else if('${startTemAdPlayStatus}' != '0' && '${endTemAdPlayStatus}'=='0'){
					displayOper('endTemAdPlay');
				}
				if('${lockStatus}'=='0'){
					displayOper('lock');
				}else if('${lockStatus}' != '0' && '${unlockStatus}'=='0'){
					displayOper('unlock');
				}
				if('${updateStatus}'=='停止升级中...'){
					displayOper('update');
				}else{
					displayOper('stopUpdate');
				}
			}
			
			function remoteMgr(macIDStd){
				//var paramers="dialogWidth:400px;DialogHeight:250px;status:no;help:no;unadorned:no;resizable:no;status:no;";  
				//var url = "/th/jsp/machine/macCommandContent.jsp";
				//var ret=window.showModalDialog(url,'',paramers);  
				//if (ret == null || ret == "") {
				//	return;
				//}
				window.parent.forwardRemote(macIDStd);
				//self.location = '/th/machineServlet?model=command&method=macOper&macIDStd=' + macIDStd + '&operType=9&content=' + ret;	
			}

			function deleteAppMgr(macIDStd){
				window.parent.forwardDeleteApp(encodeURI(macIDStd));
			}
			
			function sendMessage(macIDStd){
				var paramers="dialogWidth:400px;DialogHeight:250px;status:no;help:no;unadorned:no;resizable:no;status:no;";  
				var url = "/th/jsp/machine/macCommandMessage.jsp";
				var ret=window.showModalDialog(url,'',paramers);  
				if (ret == null || ret == "") {
					return;
				}
				self.location = '/th/machineServlet?model=command&method=macOper&macIDStd=' + macIDStd + '&operType=30&content=' + ret;	
			}
			
			function displayOper(element){
				document.getElementById(element).style.display = '';
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
				self.location = '/th/machineServlet?model=command&method=macOper&macIDStd=' + macIDStd + '&operType=' + operType;
				if(operType =='31'){
					parent.location = '/th/machineServlet?model=command';
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
							<th class="row" colspan="1"><span style="color:blue;font-weight:bold">${orgNameStd}-${macNameStd}:</span><span id="macstatus" style="color:blue;font-weight:bold">${operName},${updateStatus}</span></td>
							<% } %>
						</tr>
						<tr>
							<td class="row" height="22">
								<input type="button" style="margin-left:3px;" id="sendMsg" value="发送消息" class="leftBtn" onclick="sendMessage('${macIDStd}')" />
								<input type="button" style="margin-left:3px;" id="remote" value="远程接管" class="leftBtn" onclick="remoteMgr('${macIDStd}')" />
								<input type="button" style="margin-left:3px;" id="retirement" value="报废" class="leftBtn" onclick="dOper('${macIDStd}', 31)" />
								<!-- <input type="button" style="margin-left:3px;display:none;" id="update" value="升级" class="leftBtn" onclick="dOper('${macIDStd}', 32)" /> -->
								<input type="button" style="margin-left:3px;display:none;" id="stopUpdate" value="停止升级" class="leftBtn" onclick="dOper('${macIDStd}', 33)" />
								<input type="button" style="margin-left:3px;" id="deleteApp" value="卸载程序" class="leftBtn" onclick="deleteAppMgr('${macIDStd}')" />
							</td>
						</tr>
						<tr>
							<td class="row" height="22">
								<input type="button" style="margin-left:3px;" id="locate" value="定位" class="leftBtn" onclick="dOper('${macIDStd}', 27)" />
								<input type="button" style="margin-left:3px;" id="restartPC" value="重启" class="leftBtn" onclick="dOper('${macIDStd}', 5)" />
								<input type="button" style="margin-left:3px;" id="closePC" value="关机" class="leftBtn" onclick="dOper('${macIDStd}', 4)" />
								<input type="button" style="margin-left:3px;" id="cleanData" value="清除端机冗余数据" class="leftBtn" onclick="dOper('${macIDStd}', 8)" />
							</td>
						</tr>
						<tr>
							<td class="row" height="22">
								<input type="button" style="margin-left:3px;display:none;" id="startScreen" value="启动截屏" class="leftBtn" onclick="dOper('${macIDStd}', 6)"/>
								<input type="button" style="margin-left:3px;display:none;" id="endScreen"  value="停止截屏" class="leftBtn"  onclick="dOper('${macIDStd}', 7)"/>
								<input type="button" style="margin-left:3px;display:none;" id="stop" value="锁定\报停" class="leftBtn" onclick="dOper('${macIDStd}', 2)" />
								<input type="button" style="margin-left:3px;display:none;" id="start" value="启用" class="leftBtn" onclick="dOper('${macIDStd}', 3)" />
								<input type="button" style="margin-left:3px;display:none;" id="lock" value="锁屏" class="leftBtn" onclick="dOper('${macIDStd}', 28)" />
								<input type="button" style="margin-left:3px;display:none;" id="unlock" value="解锁" class="leftBtn" onclick="dOper('${macIDStd}', 29)" />
							</td>
						</tr>
						<tr>
							<td class="row" height="22">
								<input type="button" style="margin-left:3px;display:none;" id="startAdPlay" value="开始广告播放" class="leftBtn" onclick="dOper('${macIDStd}', 10)" />
								<input type="button" style="margin-left:3px;display:none;" id="endAdPlay" value="停止广告播放" class="leftBtn" onclick="dOper('${macIDStd}', 11)" />
								<input type="button" style="margin-left:3px;display:none;" id="startTemAdPlay" value="启动临时广告" class="leftBtn" onclick="dOper('${macIDStd}', 12)" />
								<input type="button" style="margin-left:3px;display:none;" id="endTemAdPlay" value="停止临时广告" class="leftBtn" onclick="dOper('${macIDStd}', 13)" />
							</td>
						</tr>
					</table>
				</form>
	    	</div>
	  	</div>
	</body>
</html>