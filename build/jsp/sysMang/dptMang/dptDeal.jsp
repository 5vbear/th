<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="th.entity.DepartmentBean" %>
    
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
	// 当前是add操作跳转过来的 
	if ( selOrgId == -1 ) {
		selOrgId = Long.parseLong( (String) request.getAttribute( "orgListTopId" ) );
	}
	String selDptName = dpt.getDptName();
	String selDptDesp = dpt.getDptDescription();
	String selMngName = dpt.getManagerName();
	String selMngEmail = dpt.getManagerMail();
	String selMngTel = dpt.getManagerTel();
	String selMngOthConts = dpt.getOtherContacts();

	String pageTitle = (String) request.getAttribute( "pageTitle" );
	String saveResult = (String)request.getAttribute( "saveResult" );
	String acTurn = (String)request.getAttribute( "acTurn" );
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门信息编辑页面</title>
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

	// $(document).ready()是DOM结构绘制完毕后就执行，不必等到加载完毕 
	<%-- $(document).ready(function() {
		// 显示之前选中的组织名称 
		document.getElementById("SelectOrg").value = <%= selOrgId %>;
	});	 --%>
	// window.onload必须等到页面内包括图片的所有元素加载完毕后才能执行 
	window.onload = function(){
		document.getElementById("SelectOrg").value = <%=selOrgId%>;
		window.document.form_data.hide_org_select.value = "<%= orgSelect %>";
		var saveResult = "<%=saveResult%>";
		if(saveResult!=""){
			alert(saveResult);
		}
	};

	function btnOperations() {
		
		this.enabled = function(){
			
			document.getElementById("SelectOrg").disabled = false;
			document.getElementById("dptName").disabled = false;
			document.getElementById("dptDesp").disabled = false;
			document.getElementById("dptManager").disabled = false;
			document.getElementById("managerEmail").disabled = false;
			document.getElementById("managerTel").disabled = false;
			document.getElementById("otherContacts").disabled = false;
			
		};		
		this.save = function(){
			
			// 获取输入的部门名称，不能为空
			var dptName = document.getElementById("dptName");
			var managerEmail = document.getElementById("managerEmail");
			var managerTel = document.getElementById("managerTel");
			var otherContacts = document.getElementById("otherContacts");
			if(dptName.value==""){
				alert("请输入当前希望添加的部门名称!");
			}else if(managerEmail.value==""){
				alert("请输入当前希望添加的部门负责人邮箱!");
			}else if(managerTel.value.length>15){
				alert("固定电话号码长度必须限制在15个字符内，请重新输入!");
			}else if(otherContacts.value!=""&&otherContacts.value.length!=11){
				alert("移动电话号码长度必须限制为11个字符，请重新输入!");
			}else{
				window.document.form_data.dealFlg.value = "add/change";
				window.document.form_data.action = "/th/jsp/sysMang/dptMang/dptDeal.html";
				// 获取所属组织信息
				var orgSelect = document.getElementById("SelectOrg");
				window.document.form_data.sel_org_id.value = orgSelect.value;
				// 获取输入的部门名称
				window.document.form_data.input_dpt_name.value = dptName.value;
				// 获取输入的部门描述
				var dptDesp = document.getElementById("dptDesp");
				window.document.form_data.input_dpt_desp.value = dptDesp.value;
				// 获取输入的部门负责人
				var dptManager = document.getElementById("dptManager");
				window.document.form_data.input_dpt_manager.value = dptManager.value;
				// 获取输入的负责人邮箱 
				window.document.form_data.input_manager_email.value = managerEmail.value;
				// 获取输入的负责人联系方式 
				window.document.form_data.input_manager_tel.value = managerTel.value;
				// 获取输入的负责人其他联系方式 
				window.document.form_data.input_manager_otherContacts.value = otherContacts.value;
				
				window.document.form_data.hide_dpt_id.value = <%=selDptId%>;
				window.document.form_data.page_index.value = "dptDeal";
				window.document.form_data.submit();
					
			}
		};
		this.back = function(){
			window.document.form_data.action = "/th/jsp/sysMang/dptMang/dptSearch.html";
			window.document.form_data.submit();
		}
		
	};	
	var btnOper = new btnOperations();
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;<%=pageTitle%></span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="sel_org_id" value=""/>
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="input_dpt_name" value=""/>
		<input type="hidden" name="input_dpt_desp" value=""/>
		<input type="hidden" name="input_dpt_manager" value=""/>
		<input type="hidden" name="input_manager_email" value=""/>
		<input type="hidden" name="input_manager_tel" value=""/>
		<input type="hidden" name="input_manager_otherContacts" value=""/>
		<input type="hidden" name="hide_dpt_id" value=""/>
		<input type="hidden" name="hide_org_select" value=""/>
		<input type="hidden" name="page_index" value=""/>

		<table class="x-data-table">
			<tr>
				<th style="width: 20%" class="x-data-table-th">所属组织</th>
				<td class="x-data-table-td"><select id="SelectOrg" name="select" style="width:273px" <% out.print(acTurn); %> ><%=orgSelect%></select></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">部门名称[*]</th>
				<td class="x-data-table-td"><input type="text" size="40" name="dptName" id="dptName" <% out.print(acTurn); %> value="<%=selDptName%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">部门描述</th>
				<td class="x-data-table-td"><input type="text" size="40" name="dptDesp" id="dptDesp" <% out.print(acTurn); %> value="<%=selDptDesp%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">部门负责人</th>
				<td class="x-data-table-td"><input type="text" size="40" name="dptManager" id="dptManager" <% out.print(acTurn); %> value="<%=selMngName%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">负责人邮箱[*]</th>
				<td class="x-data-table-td"><input type="text" size="40" name="managerEmail" id="managerEmail" <% out.print(acTurn); %> value="<%=selMngEmail%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">负责人联系方式</th>
				<td class="x-data-table-td"><input type="text" size="40" name="managerTel" id="managerTel" <% out.print(acTurn); %> value="<%=selMngTel%>" /></td>
			</tr>
			<tr>
				<th style="width: 20%" class="x-data-table-th">负责人其他联系方式</th>
				<td class="x-data-table-td"><input type="text" size="40" name="otherContacts" id="otherContacts" <% out.print(acTurn); %> value="<%=selMngOthConts%>" /></td>
			</tr>
		</table>
		<div class="x-client-form">
			<%
			 	if(!"".equals(acTurn)){
			 		out.print("<input class='tableBtn' type='button' name='button_Enabled' id='btnEnabled' value='编辑' onclick='btnOper.enabled()' /> ");
			 	}
			 %>	
    		<input class="tableBtn" type="button" name="button_Save" id="btnSave" value="保存" onclick="btnOper.save()" /> 
			<input class="tableBtn" type="button" name="button_Return" id="btnRet" value="返回" onclick="btnOper.back()" />
  		</div>
	</form>
</body>
</html>