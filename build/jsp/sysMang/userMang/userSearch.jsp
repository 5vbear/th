<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="th.com.util.Define" %>
    
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
	String zNodes = (String) request.getAttribute("zNodes");
	long ConOrgId = Long.parseLong(request.getAttribute("ConOrgId").toString());
	String ConDptName = (String) request.getAttribute("ConDptName");
	String ConUserName = (String) request.getAttribute("ConUserName");

	HashMap[] usersList = (HashMap[]) request.getAttribute("searchResults");

	int listCount = 0;
	int pageLimit = Define.VIEW_NUM;
	int pagesNum = 0;
	int curPageIndex = Integer.parseInt((String) request.getAttribute("CurPageIndex"));
	String jumpVisiableFlg = "";

	if (usersList != null && usersList.length > 0) {
		// 总记录条数
		listCount = usersList.length;
		// 总页数 
		if (listCount % pageLimit > 0) {
			pagesNum = listCount / pageLimit + 1;
		} else {
			pagesNum = listCount / pageLimit;
		}
		jumpVisiableFlg = "";
	} else {
		curPageIndex = 0;
		jumpVisiableFlg = "disabled='disabled'";
	}
	
	String saveResult = (String)request.getAttribute( "saveResult" );
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>用户搜索页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../../css/machine.css" />

<SCRIPT LANGUAGE="JavaScript">

	window.onload = function showtable() {
		var tablename = document.getElementById("dataTableId");
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
		var saveResult = "<%=saveResult%>";
		if(saveResult!=""){
			alert(saveResult);
		}
	};
	function onFold(id) {
		var vDiv = document.getElementById(id);
		vDiv.style.display = (vDiv.style.display == 'none') ? 'block' : 'none';
		var fold = document.getElementById('clientStyleId');
		if (vDiv.style.display === 'none') {
			fold.className = 'x-fold-plus';
		} else {
			fold.className = 'x-fold-minus';
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
	var zNodes = <%=zNodes%>
	
	$(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);

		var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
		var v = "", w = "";
		// 设置组织名称下拉列表值 
		var orgList = document.getElementById("SelectOrg");
		orgList.options.add(new Option("全部", -1));
		for ( var i = 0; i < nodes.length; i++) {
			w = "";
			if (i < nodes.length - 1) {
				for ( var j = 0; j < (nodes[i].level - nodes[0].level); j++) {
					w += "├─";
				}
			} else {
				for ( var j = 0; j < (nodes[i].level - nodes[0].level); j++) {
					w += "└─";
				}
			}
			// 为select下拉菜单动态赋予option值 
			orgList.options.add(new Option(w + nodes[i].name, nodes[i].id));
			// 组织List信息拼接  
			v += w + nodes[i].name + "_" + nodes[i].id;
			if (i < nodes.length - 1) {
				v += ",";
			}
		}
		// 保存组织list信息 
		window.document.form_data.org_list_info.value = v;
		// 显示之前选中的组织名称 
		<%-- var inConOrgId = <%=ConOrgId%>;
		if (inConOrgId != -1) {
			orgList.value = inConOrgId;
		} else {
			orgList.value = nodes[0].id;
		} --%>
		orgList.value = <%=ConOrgId%>;
	});
	
	function btnOperations() {
		
 		this.checkBox = function(){
 			var selectUsers="";
			var userList=document.getElementsByName("userListCheckbox");
			for (var i=0;i<userList.length;i++){
			  if(userList[i].checked == true){
				  selectUsers += userList[i].value + ",";
			  }
			}
			alert(selectUsers);
		};
			
		this.addUser = function(){
			window.document.form_data.dealFlg.value = "add";
			window.document.form_data.action = "/th/jsp/sysMang/userMang/userDeal.html";
			window.document.form_data.submit();
		};
		this.bathImport = function(){
			window.document.form_data.action = "/th/jsp/sysMang/userMang/userBathImport.jsp";
			window.document.form_data.submit();
		};
		this.changeUser = function(me){
			
			var btnId = me.id;
			// "btnChange_"
			var userId = btnId.substring(10);
			window.document.form_data.dealFlg.value = "change";
			window.document.form_data.action = "/th/jsp/sysMang/userMang/userDeal.html";
			window.document.form_data.sel_user_id.value = userId;
			window.document.form_data.submit();
	
		};
		this.deleteUser = function(me){
			
			var btnId = me.id;
			// "btnDel_"
			var userId = btnId.substring(7);
			window.document.form_data.dealFlg.value = "del";
			window.document.form_data.action = "/th/jsp/sysMang/userMang/userSearch.html";
			window.document.form_data.sel_user_id.value = userId;
			window.document.form_data.submit();
			
		};
		this.resetUserPwd = function(me){
			
			var btnId = me.id;
			// "btnReset_"
			var userId = btnId.substring(9);
			window.document.form_data.dealFlg.value = "reset";
			window.document.form_data.action = "/th/jsp/sysMang/userMang/userSearch.html";
			window.document.form_data.sel_user_id.value = userId;
			window.document.form_data.submit();
			
		}
		this.search = function(){
			window.document.form_data.dealFlg.value = "search";
			window.document.form_data.action = "/th/jsp/sysMang/userMang/userSearch.html";
			// 拼接搜索条件 
			var serCond = "";
			var orgSelect = document.getElementById("SelectOrg");
			serCond += orgSelect.value + ",";
			var contentDpt = document.getElementById("dptName");
			if(contentDpt.value!=""){
				serCond += contentDpt.value + ",";
			}else{
				serCond += "null,";
			}
			var contentUser = document.getElementById("userName");
			if(contentUser.value!=""){
				serCond += contentUser.value;
			}else{
				serCond += "null";
			}
			window.document.form_data.search_con_info.value = serCond;
			window.document.form_data.submit();

		}
		
	};	
	function btnPageOperate(){
		var tempCurPageIndex = <%= curPageIndex %>;
		var tempCurPageNum = <%= pagesNum %>;
		this.init = function(){
			window.document.form_data.dealFlg.value = "jump";
			window.document.form_data.action = "/th/jsp/sysMang/userMang/userSearch.html";
			// 拼接搜索条件 
			var serCond = "";
			var orgSelect = document.getElementById("SelectOrg");
			serCond += orgSelect.value + ",";
			var contentDpt = document.getElementById("dptName");
			if(contentDpt.value!=""){
				serCond += contentDpt.value + ",";
			}else{
				serCond += "null,";
			}
			var contentUser = document.getElementById("userName");
			if(contentUser.value!=""){
				serCond += contentUser.value;
			}else{
				serCond += "null";
			}
			window.document.form_data.search_con_info.value = serCond;		
		};
		this.first = function(){
			if(tempCurPageIndex==0){
				alert("当前页面中没有查询数据，页面跳转功能无效，请注意!");
			}else if(tempCurPageIndex==1){
				alert("当前页面已经是数据的首页面!");
			}else{
				this.init();
				window.document.form_data.page_jump_index.value = 1;
				window.document.form_data.submit();
			}		
		};
		this.previous = function(){
			if(tempCurPageIndex==0){
				alert("当前页面中没有查询数据，页面跳转功能无效，请注意!");
			}else if(tempCurPageIndex==1){
				alert("当前页面已经是数据的首页面，不能再向前翻页!");
			}else{
				this.init();
				window.document.form_data.page_jump_index.value = tempCurPageIndex-1;
				window.document.form_data.submit();
			}
		};
		this.next = function(){
			if(tempCurPageIndex==0){
				alert("当前页面中没有查询数据，页面跳转功能无效，请注意!");
			}else if(tempCurPageIndex==tempCurPageNum){
				alert("当前页面已经是数据的尾页面，不能再向后翻页!");
			}else{
				this.init();
				window.document.form_data.page_jump_index.value = tempCurPageIndex+1;
				window.document.form_data.submit();
			}
		};
		this.last = function(){
			if(tempCurPageIndex==0){
				alert("当前页面中没有查询数据，页面跳转功能无效，请注意!");
			}else if(tempCurPageIndex==tempCurPageNum){
				alert("当前页面已经是数据的尾页面!");
			}else{
				this.init();
				window.document.form_data.page_jump_index.value = tempCurPageNum;
				window.document.form_data.submit();
			}
		};
		/* this.goTo = function(){
			var pIndex = document.getElementById("pageIndex");
			if(pIndex.value==""||pIndex.value<0||pIndex.value>tempCurPageNum){
				alert("当前输入的跳转页面数是一个无效值，请重新输入!");
			}else{
				this.init();
				window.document.form_data.page_jump_index.value = pIndex.value;
				window.document.form_data.submit();
			}
		} */
		
	};
	
	var btnOper = new btnOperations();
	var pageOper = new btnPageOperate();
	
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;系统管理-用户管理</span></div>
	<table><tr style ="heigt:30px"></tr></table>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="org_list_info" value=""/>
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="search_con_info" value=""/>
		<input type="hidden" name="page_jump_index" value=""/>
		<input type="hidden" name="sel_user_id" value=""/>
		<div class="x-title">
			<div id="clientStyleId" class="x-fold-minus" onclick="onFold('foldId');" />
		</div>
		<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
		</div>
		<div id="foldId" style="width: 100%; display: block; height: 30px; line-height: 30px; background-color: #B2DFEE">
			<div style="float: left">
				<span>组织名称</span> <select id="SelectOrg" name="select">
				</select>
			</div>
			<div style="float: left">
				<span>&nbsp;&nbsp;部门名称</span> 
				<input type="text" name="dptName" id="dptName" value="<%= ConDptName %>" />
			</div>
			<div style="float: left">
				<span>&nbsp;&nbsp;用户姓名</span> 
				<input type="text" name="userName" id="userName" value="<%= ConUserName %>" />
			</div>
			<span>&nbsp;&nbsp;</span> 
			<input class="tableBtn" type="button" name="button_Search" id="btnSer" value="搜索" onclick="btnOper.search()"/>
		</div>
		<table><tr style ="heigt:30px"></tr></table>
		<div>
			<div class="x-data"><span style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span></div>
			<div>
				<table class="x-data-table" id="dataTableId">
					<%
						
						/* out.print( "<tr><td  colspan='9' style='text-align: left' class='x-data-table-td'>&nbsp;&nbsp;用户表[当前第" + curPageIndex + "页/共"
								+ pagesNum + "页][排序方式:操作时间/排序类型:降序][每页" + pageLimit + "条][共" + listCount + "条]</td></tr>" ); */

						out.print( "<tr><th style='width: 12%' class='x-data-table-th'>所属组织</th>"
								+ "<th style='width: 8%' class='x-data-table-th'>所在部门</th>"
								+ "<th style='width: 10%' class='x-data-table-th'>用户账户</th>"
								+ "<th style='width: 8%' class='x-data-table-th'>用户姓名</th>"
								+ "<th style='width: 10%' class='x-data-table-th'>联系方式</th>"
								+ "<th style='width: 15%' class='x-data-table-th'>邮件地址</th>"
								+ "<th style='width: 16%' class='x-data-table-th'>创建时间</th>"
								+ "<th style='width: 21%' class='x-data-table-th'>操作</th></tr>" );

						if ( usersList!= null&&usersList.length>0 ) {

							// 相对于HashMap[]的情况,curPageIndex = curPageIndex-1; 
							// 起点：
							int startIndex = (curPageIndex -1)*pageLimit;
							// 终点： 
							int endIndex = 0;
							if(curPageIndex<pagesNum){
								endIndex = curPageIndex*pageLimit;
							}else{
								endIndex = usersList.length;
							}
							
							for ( int i = startIndex; i < endIndex; i++ ) {
								HashMap userMap = usersList[i];
								long userID = Long.parseLong(userMap.get( "USER_ID" ).toString());
								String orgName = (String) userMap.get( "ORG_NAME" );
								String dptName = (String) userMap.get( "DEPARTMENT_NAME" );
								String nickName = (String) userMap.get( "NAME" );
								String userName = (String) userMap.get( "REAL_NAME" );
								if ( userName == null ) {
									userName = "";
								}
								String fixedTel = (String) userMap.get( "TELEPHONE" );
								if ( fixedTel == null ) {
									fixedTel = "";
								}
								String email = (String) userMap.get( "EMAIL" );
								if ( email == null ) {
									email = "";
								} 
								String operateTime = (String) userMap.get( "OPERATETIME" );
								// middle
								out.print( "<td class='x-data-table-td-left-pad'>" + orgName + "</td> "
										+ "<td class='x-data-table-td-left-pad'>" + dptName + "</td> " 
										+ "<td class='x-data-table-td-left-pad'>" + nickName + "</td> " 
										+ "<td class='x-data-table-td-left-pad'>" + userName + "</td> " 
										+ "<td class='x-data-table-td-left-pad'>" + fixedTel + "</td> " 
										+ "<td class='x-data-table-td-left-pad'>" + email + "</td> "
										+ "<td class='x-data-table-td-left-pad'>" + operateTime + "</td>" );
								// bottom
								out.print( "<td class='x-data-table-td-left-pad' ><input class='tableBtn' type='button' value='编辑' name='buttonChange_" + userID + "' id='btnChange_" + userID + 
								           "' onclick='btnOper.changeUser(this)'/><input class='tableBtn' type='button' value='删除' name='buttonDelete_" + userID + "' id='btnDel_" + userID + 
								           "' onclick='btnOper.deleteUser(this)'/><input class='tableBtn' type='button' value='重置密码' name='buttonReset_" + userID + "' id='btnReset_" + userID + 
								           "' onclick='btnOper.resetUserPwd(this)'/></td></tr>" );

							}

						}
					%>
				</table>
			</div>
			<div style="float: left">
				<input type="button" class="x-first-page" name="button_page_first" id="btnFirst" <%= jumpVisiableFlg %> onclick="pageOper.first()"/> &nbsp; 
				<input type="button" class="x-previous-page" name="button_page_previous" id="btnPre" <%= jumpVisiableFlg %> onclick="pageOper.previous()"/> &nbsp; 
				<input type="button" class="x-next-page" name="button_page_next" id="btnNext" <%= jumpVisiableFlg %> onclick="pageOper.next()"/> &nbsp; 
				<input type="button" class="x-last-page" name="button_page_last" id="btnLast" <%= jumpVisiableFlg %> onclick="pageOper.last()"/> &nbsp; 
				<%-- <label>第 <input type="text" size="1" name="pageIndex" value="" id="pageIndex" <%= jumpVisiableFlg %> onkeypress="returnCancel(event);"/>页 </label> 
				<input type="button" class="x-goto-page" name="button_page_goto" id="btnGoto" <%= jumpVisiableFlg %> onclick="pageOper.goTo()"/> --%>
				<%
					out.print( "<tr><td>[当前第" + curPageIndex + "页/共" + pagesNum + "页][每页" + pageLimit + "条][共" + listCount + "条]</td></tr>" );
				%>
			</div>
			<div style="float: right">
			    <input class="tableBtn" type="button" name="button_BathAdd" id="btnBathAdd" value="批量导入" onclick="btnOper.bathImport()" /> 
				<input class="tableBtn" type="button" name="button_Add" id="btnAdd" value="添加" onclick="btnOper.addUser()" /> 
			</div>
		</div>
	</form>

</body>
</html>