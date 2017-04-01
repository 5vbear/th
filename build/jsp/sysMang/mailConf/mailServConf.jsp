<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="th.entity.SysServBean"%>
   
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
	Log logger = LogFactory.getLog( this.getClass() );
	User user = (User) session.getAttribute( "user_info" );
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
	SysServBean msb = (SysServBean) request.getAttribute( "selMailBean" );
	int confId = msb.getConfId();
	String selSmtpHost = msb.getSmtpHost();
	long selSmtpPort = msb.getSmtpPort();
	String selMailFrom = msb.getMailFrom();
	String selMailAuthNick = msb.getMailAuthNick();
	String selMailAuthPass = msb.getMailAuthPass();
	String selMailStatus = msb.getSerStatus();
	
	String saveResult = (String)request.getAttribute( "saveResult" );
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>邮件服务器配置页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../../css/machine.css" />
<SCRIPT LANGUAGE="JavaScript">

	//window.onload必须等到页面内包括图片的所有元素加载完毕后才能执行 
	window.onload = function(){
		var saveResult = "<%=saveResult%>";
		var selMailStatus = "<%=selMailStatus%>";
		if(selMailStatus!=""){
			document.getElementById("serStatus").value = selMailStatus;
		}
		if(saveResult!=""){
			alert(saveResult);
		}
	};

	var zTreeObj;
	var setting = {
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId",
				rootPId : 0,
			}
		},
		view : {
			selectedMulti : false
	
		}
	
	};
	var zNodes = [ { "id": 0, "pId": -1, "name": "中国建设银行", open:true },
	               { "id": 1, "pId": 0, "name": "辽宁省分行", open:true },
	               { "id": 2, "pId": 0, "name": "北京市分行", open:true },
	               { "id": 3, "pId": 0, "name": "吉林省分行", open:true },
	               { "id": 4, "pId": 0, "name": "山东省分行", open:true },
	               { "id": 5, "pId": 1, "name": "沈阳市支行"},
	               { "id": 6, "pId": 1, "name": "朝阳市支行"} ];
	
	
	$(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	});
	
	function btnOperations() {
		
		this.enabled = function(){
			document.getElementById("smtpHost").disabled = false;
			document.getElementById("smtpPort").disabled = false;
			document.getElementById("MailFrom").disabled = false;
			document.getElementById("MailAuthNickName").disabled = false;
			document.getElementById("MailAuthPassword").disabled = false;
			document.getElementById("serStatus").disabled = false;
		};
		this.save = function(){
			
			var smtpHost = document.getElementById("smtpHost");
			var smtpPort = document.getElementById("smtpPort");
			var mailFrom = document.getElementById("MailFrom");
			var mailAuthNick = document.getElementById("MailAuthNickName");	
			var mailAuthPass = document.getElementById("MailAuthPassword");
			var serStatus = document.getElementById("serStatus");
			if(smtpHost.value==""){
				alert("请输入当前配置SMTP服务器地址!");
			}else if(smtpPort.value==""){
				alert("请输入当前配置SMTP服务器端口!");
			}else if(mailFrom.value==""){
				alert("请输入当前配置SMTP服务器邮件头!");
			}else if(mailAuthNick.value==""){
				alert("请输入当前配置SMTP邮件服务器认证账户!");
			}else if(mailAuthPass.value==""){
				alert("请输入当前配置SMTP邮件服务器认证密码!");
			}else {
				window.document.form_data.dealFlg.value = "save";
				window.document.form_data.input_smtp_host.value = smtpHost.value;
				window.document.form_data.input_smtp_port.value = smtpPort.value;
				window.document.form_data.input_mail_from.value = mailFrom.value;
				window.document.form_data.input_mail_auth_nick.value = mailAuthNick.value;
				window.document.form_data.input_mail_auth_pass.value = mailAuthPass.value;
				window.document.form_data.input_mail_status.value = serStatus.value;
				window.document.form_data.submit();
			}

		};
		this.test = function(){
			var mailStatus = "<%=selMailStatus%>";
			if(mailStatus=="1"){
				document.getElementById("testEditDIV").style.display = "block";
				document.getElementById("testEditDIV").innerHTML="<table class='x-data-table'><tr><th style='width: 20%' class='x-data-table-th'>测试邮件地址:</th>" + 
			    "<td style='width: 30%' class='x-data-table-td'><input type='text' size='40' name='toAddress' id='toAddress' value='' /></td>" +
			    "<td ><input class='tableBtn' type='button' name='button_Send' id='btnSend' value='发送' onclick='btnOper.send()'/></td></tr></table>" ;
			    document.getElementById("btnTest").disabled = true;
			}else if(mailStatus=="0"){
				alert("当前邮件服务器处于'停止'状态，不能发送邮件!\r\n欲发送测试邮件，请先设置邮件服务器状态为'开启'!");
			}
		};
		this.send = function(){
			var toAddress = document.getElementById("toAddress");
			if(toAddress.value==""){
				alert("请输入测试邮件发送地址!");
			}else{
				window.document.form_data.dealFlg.value = "send";
				window.document.form_data.input_mail_to.value = toAddress.value;
				window.document.form_data.submit();
			}
		}
		
		
	};	
	var btnOper = new btnOperations();
	
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;系统管理-邮件设置</span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="/th/jsp/sysMang/mailConf/mailServConf.html">
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="input_smtp_host" value=""/>
		<input type="hidden" name="input_smtp_port" value=""/>
		<input type="hidden" name="input_mail_from" value=""/>
		<input type="hidden" name="input_mail_auth_nick" value=""/>
		<input type="hidden" name="input_mail_auth_pass" value=""/>
		<input type="hidden" name="input_mail_status" value=""/>
		<input type="hidden" name="input_mail_to" value=""/>
		
		<table class="x-data-table">
			<tr>
				<th style="width: 20%" class="x-data-table-th">邮件服务SMTP服务地址[*]</th>
				<td class="x-data-table-td"><input type="text" size="40" name="smtpHost" id="smtpHost" disabled="disabled" value="<%=selSmtpHost%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">服务器端口[*]</th>
				<td class="x-data-table-td"><input type="text" size="40" name="smtpPort" id="smtpPort" disabled="disabled" value="<%=selSmtpPort%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">邮件头[*]</th>
				<td class="x-data-table-td"><input type="text" size="40" name="MailFrom" id="MailFrom" disabled="disabled" value="<%=selMailFrom%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">邮件用户名[*]</th>
				<td class="x-data-table-td"><input type="text" size="40" name="MailAuthNickName" id="MailAuthNickName" disabled="disabled" value="<%=selMailAuthNick%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">邮件口令[*]</th>
				<td class="x-data-table-td"><input type="password" size="40" name="MailAuthPassword" id="MailAuthPassword" disabled="disabled" value="<%=selMailAuthPass%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">开起邮件服务[*]</th>
				<td class="x-data-table-td">
					<select id="serStatus" name="serStatus" style="width:270px" disabled = "disabled" >
						<option value="1">开启</option>
						<option value="0">停用</option>
					</select>
				</td>
			</tr>
		</table>
		<div class="x-client-form">
			<input class="tableBtn" type="button" name="button_Enabled" id="btnEnabled" value="编辑" onclick="btnOper.enabled()" /> 
    		<input class="tableBtn" type="button" name="button_Save" id="btnSave" value="保存" onclick="btnOper.save()" /> 
    		<input class="tableBtn" type="button" name="button_Test" id="btnTest" value="测试" onclick="btnOper.test()" /> 
  		</div>
  		<div style="overflow-y:auto;">
			<div id="testEditDIV"  style="display:none; height: 50px; width: 100%;float: left;">
			</div>
		</div>
	</form>

</body>
</html>