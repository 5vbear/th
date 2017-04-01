<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="th.entity.UserBean" %>
   
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
	UserBean sab = (UserBean) request.getAttribute( "selAdminBean" );
	String adminUserName = sab.getUserName();
	String adminNickName = sab.getNickName();
	String adminPassword = sab.getPassword();
	
	String saveResult = (String)request.getAttribute( "saveResult" );
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统管理员初始密码配置页面</title>
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
			if(confirm("是否要重置系统管理员初始密码？")){
				document.getElementById("password").disabled = false;
			}
		};
		this.save = function(){
			
			var inputPwd = document.getElementById("password");
			if(inputPwd.value==""){
				alert("请输入当前系统管理员密码!");
			}else{
				window.document.form_data.dealFlg.value = "save";
				window.document.form_data.input_admin_orig_pwd.value = inputPwd.value;
				window.document.form_data.submit();
			}

		}
		
	};	
	var btnOper = new btnOperations();
	
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;系统管理-系统设置</span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="/th/jsp/sysMang/sysConf/sysServConf.html">
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="input_admin_orig_pwd" value=""/>
		<table class="x-data-table">
			<tr>
				<th style="width: 20%" class="x-data-table-th">系统管理员用户</th>
				<td class="x-data-table-td"><input type="text" size="40" name="userName" id="userName" disabled="disabled" value="<%=adminUserName%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">系统管理员账号</th>
				<td class="x-data-table-td"><input type="text" size="40" name="nickName" id="nickName" disabled="disabled" value="<%=adminNickName%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">系统管理员密码[*]</th>
				<td class="x-data-table-td"><input type="text" size="40" name="password" id="password" disabled="disabled" value="<%=adminPassword%>" /></td>
			</tr>
		</table>
		<div class="x-client-form">
			<input class="tableBtn" type="button" name="button_Enabled" id="btnEnabled" value="编辑" onclick="btnOper.enabled()" /> 
    		<input class="tableBtn" type="button" name="button_Save" id="btnSave" value="保存" onclick="btnOper.save()" /> 
  		</div>
	</form>

</body>
</html>