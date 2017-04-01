<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%
	Log logger = LogFactory.getLog( this.getClass() );
	User user = (User) session.getAttribute( "user_info" );
	String strContextPath = request.getContextPath();
	String url = strContextPath + "/remote.html";
	String realname = null;
	if ( user == null ) {
		response.setContentType( "text/html; charset=utf-8" );
		response.sendRedirect( "/th/index.jsp" );
	}
	else {
		realname = user.getReal_name();
		logger.info( "获得当前用户的用户名，用户名是： " + realname );
	}
%>
<%
	String commandStatus = (String)request.getAttribute("command_status");
	String lastPage = (String)request.getAttribute("lastPage");
	HashMap machineInfo = (HashMap)request.getAttribute("machineInfo");
	String response_contents = (String)request.getAttribute("response_contents");
	String command_contents = (String)request.getAttribute("command_contents");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>远程接管</title>
<link rel="stylesheet" href="../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.simplemodal.1.4.4.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/channel.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/machine.css" />
<style type="text/css">
	#remote_command{
		height: 300px;
		width: 50px;
	}
	#model1{
		height: 40px;
		width: 200px;
		display: none;
		vertical-align: middle;
		text-align: center;
		margin: 0 auto;
		color: #4682b4;
	}
	body{
		margin-top: 0px;
	}
</style>
<SCRIPT LANGUAGE="JavaScript" type="text/javascript">
	$(document).ready(function(){
		var abc = window.parent;
		window.parent.stopModel();
		$('#do_remote').click(function(){
			$('#deal').val('remote_send');
			var contents = $('#remote_command').val();
			if(contents ==''||contents ==null){
				alert('请填写需要发送的指令');
				return false;
			}
			$('#command_contents').val(contents);
			$('form').submit();
			//周一测试
			window.parent.modelWindows();
		});
		$('#do_cancel').click(function(){
			<%if(null !=lastPage&&lastPage.equals("non_monitor")){%>
			$('form').attr('action','/th/machineServlet?model=command');
			<%}else if(null !=lastPage&&lastPage.equals("1")){%>
			$('form').attr('action','/th/MonitorServlet.html?orderType=2&pageID=monitor00&returnPage=monitor08');
			<%}else{%>
			$('form').attr('action','/th/MonitorServlet.html?orderType=2&pageID=monitor00');
			<%}%>
			$('form').submit();
		});
	});
</SCRIPT>
</head>
<body>
	<div class="x-title"><span>&nbsp;&nbsp;端机运行监控-远程接管</span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="<%=url %>">
		<input type="hidden" name="deal" id="deal" value=""/>
		<input type="hidden" name="command_contents" id="command_contents" value=""/>
		<input type="hidden" name="machineID" id="machineID" value="<%=(String)machineInfo.get("MACHINE_ID")%>"/>
		<input type="hidden" name="mac" id="mac" value="<%=(String)machineInfo.get("MAC")%>"/>
		<input type="hidden" name="lastPage" id="lastPage" value="<%=lastPage%>"/>
		<div>
			<%if(null ==commandStatus||"".equals(commandStatus)||"0".equals(commandStatus)){ %>
			<div>
			<table class="x-data-table">
				<tr>
					<th class="x-data-table-th" width="20%">指令执行情况:</th>
					<td class="x-data-table-td" style="color: red">远程接管指令没有得到响应</td>
				</tr>
				<tr id="machine_head">
					<th class="x-data-table-th" width="20%">机器名:</th>
					<td class="x-data-table-td"><%=machineInfo.get("MACHINE_MARK") %></td>
				</tr>
				<tr id="redo2_clear">
					<th class="x-data-table-th" width="20%">指令内容:</th>
					<td class="x-data-table-td">
						<textarea rows="5" cols="100"  style="width: 600px;height: 100px;" readonly="readonly"  name="remote_command" id="remote_command"><%=command_contents %></textarea>
					</td>
				</tr>
			</table>
			</div>
			<div  class="x-client-form" style="white-space:nowrap;margin-top: 3px;padding-top: 3px;">
				<input class="tableBtn" type="button" name="do_cancel" id="do_cancel" value="返回" />
			</div>
			<%} else if("1".equals(commandStatus)){ %>
			<div>
			<table class="x-data-table">
				<tr>
					<th class="x-data-table-th" width="20%">指令执行情况:</th>
					<td class="x-data-table-td" style="color: red">远程接管指令执行成功</td>
				</tr>
				<tr id="machine_head">
					<th class="x-data-table-th" width="20%">机器名:</th>
					<td class="x-data-table-td"><%=machineInfo.get("MACHINE_MARK") %></td>
				</tr>
				<tr id="redo2_clear">
					<th class="x-data-table-th" width="20%">指令内容:</th>
					<td class="x-data-table-td">
						<textarea rows="5" cols="100"  style="width: 600px;height: 100px;"  name="remote_command" id="remote_command" readonly="readonly"><%=command_contents %></textarea>
					</td>
				</tr>
				<tr id="redo2_clear">
					<th class="x-data-table-th" width="20%">指令响应内容:</th>
					<td class="x-data-table-td">
						<textarea rows="5" cols="100"  style="width: 600px;height: 100px;" name="response_contents" id="response_contents" readonly="readonly" ><%=response_contents %></textarea>
					</td>
				</tr>
			</table>
			</div>
			<div  class="x-client-form" style="white-space:nowrap;margin-top: 3px;padding-top: 3px;">
				<input class="tableBtn" type="button" name="do_cancel" id="do_cancel" value="返回" />
			</div>
			<%} else if("no_command".equals(commandStatus)) {%>
			<div>
			<table class="x-data-table">
				<tr id="machine_head">
					<th class="x-data-table-th" width="20%">机器名:</th>
					<td class="x-data-table-td"><%=machineInfo.get("MACHINE_MARK") %></td>
				</tr>
				<tr id="redo2_clear">
					<th class="x-data-table-th" width="20%">远程接管指令:</th>
					<td class="x-data-table-td">
						<textarea rows="5" cols="100" style="width: 600px;height: 100px;"  name="remote_command" id="remote_command"></textarea>
					</td>
				</tr>
			</table>
			</div>
			<div  class="x-client-form" style="white-space:nowrap;margin-top: 3px;padding-top: 3px;">
				<input class="tableBtn" type="button" name="do_remote" id="do_remote" value="发送"  /> 
				<input class="tableBtn" type="button" name="do_cancel" id="do_cancel" value="返回" />
			</div>
			<%} %>
		</div>
		<div id="model1" >
		<h2>数据加载中...</h2>
		</div>
	</form>
</body>
</html>