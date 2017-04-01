<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="th.entity.DepartmentBean" %>
<%@ page import="java.util.HashMap" %>
    
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
	String orgSelect = (String) request.getAttribute( "orgSelect" );
	DepartmentBean dpt = (DepartmentBean) request.getAttribute( "selDptBean" );
	long selDptId = dpt.getDptId();
	long selOrgId = dpt.getOrgId();

	String selDptName = dpt.getDptName();
	String selDptDesp = dpt.getDptDescription();
	String selMngName = dpt.getManagerName();
	String selMngEmail = dpt.getManagerMail();
	String selMngTel = dpt.getManagerTel();
	String selMngOthConts = dpt.getOtherContacts();
	
	String saveResult = (String)request.getAttribute( "saveResult" );
	String dealRoleList = (String)request.getAttribute( "dealRoleList" );
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门信息授权页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../../css/machine.css" />

<SCRIPT LANGUAGE="JavaScript">

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

	// window.onload必须等到页面内包括图片的所有元素加载完毕后才能执行 
	window.onload = function(){
		document.getElementById("roleListAjaxCheck").disabled = true;
		document.getElementById("SelectOrg").value = <%=selOrgId%>;
		window.document.form_data.hide_org_select.value = "<%= orgSelect %>";
		var saveResult = "<%=saveResult%>";
		if(saveResult!=""){
			alert(saveResult);
		}
	};

	function btnOperations() {
		
		var roleList="";
		this.checkBox = function(){
			var roles=document.getElementsByName("role");
			for (i=0;i<roles.length;i++){
			  if(roles[i].checked == true){
				  roleList += roles[i].value + ",";
			  }
			}
		};	
		this.enabled = function(){
			
			// 角色列表变成可编辑状态
			document.getElementById("roleListAjaxCheck").disabled = false;
			
		};	
		this.save = function(){		
			this.checkBox();
			if(roleList==""){
				alert("请在角色组中点选要授权的角色!");
			}else{
				window.document.form_data.dealFlg.value = "auth";
				window.document.form_data.action = "/th/jsp/sysMang/dptMang/dptAuth.html";
				window.document.form_data.hide_dpt_id.value = <%=selDptId%>;
				window.document.form_data.roleList.value = roleList;
				window.document.form_data.page_index.value = "dptAuth";
				window.document.form_data.submit();
			}
		}
		this.back = function(){
			window.document.form_data.action = "/th/jsp/sysMang/dptMang/dptSearch.html";
			window.document.form_data.submit();
		}
		
	};	
	var btnOper = new btnOperations();
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;部门管理-部门信息授权</span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="roleList" value=""/>
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="hide_dpt_id" value=""/>
		<input type="hidden" name="hide_org_select" value=""/>
		<input type="hidden" name="page_index" value=""/>

		<div style="height: 520px; float: left;">
			<table style="width: 500px" class="x-data-table">
				<tr>
					<th style="width: 45%" class="x-data-table-th">所属组织</th>
					<td class="x-data-table-td"><select id="SelectOrg" name="select" style="width:270px" disabled="true"><%=orgSelect%></select></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">部门名称[*]</th>
					<td class="x-data-table-td"><input type="text" size="40" name="dptName" id="dptName" disabled="true" value="<%=selDptName%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">部门描述</th>
					<td class="x-data-table-td"><input type="text" size="40" name="dptDesp" id="dptDesp" disabled="true" value="<%=selDptDesp%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">部门负责人</th>
					<td class="x-data-table-td"><input type="text" size="40" name="dptManager" id="dptManager" disabled="true" value="<%=selMngName%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">负责人邮箱[*]</th>
					<td class="x-data-table-td"><input type="text" size="40" name="managerEmail" id="managerEmail" disabled="true" value="<%=selMngEmail%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">负责人联系方式</th>
					<td class="x-data-table-td"><input type="text" size="40" name="managerTel" id="managerTel" disabled="true" value="<%=selMngTel%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">负责人其他联系方式</th>
					<td class="x-data-table-td"><input type="text" size="40" name="otherContacts" id="otherContacts" disabled="true" value="<%=selMngOthConts%>" /></td>
				</tr>
			</table>
			<div class="x-client-form">
	    		<input class="tableBtn" type="button" name="button_Enabled" id="btnEnabled" value="编辑" onclick="btnOper.enabled()" /> 
    			<input class="tableBtn" type="button" name="button_Save" id="btnSave" value="保存" onclick="btnOper.save()" /> 
				<input class="tableBtn" type="button" name="button_Return" id="btnRet" value="返回" onclick="btnOper.back()" /> 
	  		</div>
		</div>
		<div style="height: 520px; float: right;">
			<div id="roleListAjaxCheck" style="float: left; border-style: solid; border-width: 1px; border-color: #000000; overflow: scroll; width: 260px; height: 50%">
				<%=dealRoleList %>
			</div>
		</div>
	</form>
</body>
</html>