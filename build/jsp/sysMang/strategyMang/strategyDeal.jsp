<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="th.entity.StrategyBean" %>
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

	StrategyBean stb = (StrategyBean) request.getAttribute( "selStgBean" );
	long selStgId = stb.getStgId();
	String selStgName = stb.getStgName();
	/* String selStgType = stb.getStgType(); */
	String selObjBegin = stb.getObjBegin();
	String selObjEnd = stb.getObjEnd();
	
	String saveResult = (String)request.getAttribute( "saveResult" );
	String pageTitle = (String)request.getAttribute( "pageTitle" );
	String acTurn = (String)request.getAttribute( "acTurn" );
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>默认角色策略信息定义页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="../../../js/myDataCheck.js"></script>
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
		var orgLevel = "<%=selObjBegin%>";
		if(!orgLevel==""){
			document.getElementById("SelectOrg").value = "<%=selObjBegin%>";
		}
		var saveResult = "<%=saveResult%>";
		if(saveResult!=""){
			alert(saveResult);
		}
	};

	function btnOperations() {
		
		var role="";
		this.checkRadio = function(){
			var roles=document.getElementsByName("role");
			for (i=0;i<roles.length;i++){
			  if(roles[i].checked == true){
				  role = roles[i].value;
				  break;
			  }
			}
		};	
		this.enabled = function(){
			
			document.getElementById("stgName").disabled = false;
			document.getElementById("SelectOrg").disabled = false;
			
			// 角色列表变成可编辑状态
			var rolesList = document.getElementsByName("role");
			for (var i=0; i<rolesList.length; i++) {
				rolesList[i].disabled = false;
			}
			
		};	
		this.save = function(){	
			this.checkRadio();
			// 策略名称取得
			var inStgName = document.getElementById("stgName");
			var inOrgLevel = document.getElementById("SelectOrg");
			//sjw mod start
			if(myTrim(inStgName.value)==""){
				alert("请输入当前希望添加的策略名称!");
			}else if(role==""||role==" "){
				alert("请在角色组中点选要授权的角色!");
				//sjw mod end
			}else{
				window.document.form_data.dealFlg.value = "add/change";
				window.document.form_data.action = "/th/jsp/sysMang/strategyMang/strategyDeal.html";
				window.document.form_data.hide_stg_id.value = <%=selStgId%>;
				window.document.form_data.sel_role_id.value = role;
				window.document.form_data.in_stg_name.value = inStgName.value;
				window.document.form_data.in_org_level.value = inOrgLevel.value;
				window.document.form_data.page_index.value = "stgAuth";
				window.document.form_data.submit();
			}
		};
		this.back = function(){
			window.document.form_data.action = "/th/jsp/sysMang/strategyMang/strategyList.html";
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
		<input type="hidden" name="sel_role_id" value=""/>
		<input type="hidden" name="in_stg_name" value=""/>
		<input type="hidden" name="in_org_level" value=""/>
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="hide_stg_id" value=""/>
		<input type="hidden" name="page_index" value=""/>

		<div style="height: 520px; float: left;">
			<table style="width: 500px" class="x-data-table">
				<tr>
					<th style="width: 45%" class="x-data-table-th">策略名称[*]</th>
					<td class="x-data-table-td"><input type="text" size="40" name="stgName" id="stgName" <% out.print(acTurn); %> value="<%=selStgName%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">组织层级</th>
					<td class="x-data-table-td">
						<select id="SelectOrg" name="select" style="width:273px" <% out.print(acTurn); %> >
							<!-- <option value="0">总行</option> -->
							<option value="1">分行</option>
							<option value="2">支行</option>
						</select>
					</td>
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
		</div>
		<div style="height: 520px; float: right;">
			<div style="float: left; border-style: solid; border-width: 1px; border-color: #000000; overflow: scroll; width: 260px; height: 50%">
				<%
					HashMap[] rolesList = (HashMap[]) request.getAttribute( "RoleList" );
					String selRoleId = (String) request.getAttribute( "selRoleId" );
					boolean checkFlg = true;
					if(selRoleId==null||"".equals(selRoleId)){
						checkFlg = false;
					}					
					for ( int i = 0; i < rolesList.length; i++ ) {
						HashMap tmpMap = rolesList[i];
						out.print( "<div><input type='radio' name='role' value='" + (String) tmpMap.get( "ROLE_ID" ) + "' " + acTurn  );
						if(checkFlg){
							if(selRoleId.equals((String) tmpMap.get( "ROLE_ID" ))){
								out.print( " checked='checked' " );
							}
						}
						out.print( "/> " + (String) tmpMap.get( "ROLE_NAME" ) + "</div> " );
					}
				%>
			</div>
		</div>
	</form>
</body>
</html>