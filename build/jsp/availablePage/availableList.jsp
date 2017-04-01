<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="th.com.util.Define"%>
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="th.action.report.ReportCommonAction"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Log logger = LogFactory.getLog(this.getClass());
	String pageNumber = (Integer) request.getAttribute("pageIdx") + "";
	String totalPageCount = "";
	int page_view = Define.VIEW_NUM;
	String total_num = "";
	User user = (User) session.getAttribute("user_info");
	String realname = null;
	if (user == null) {
		response.setContentType("text/html; charset=utf-8");
		response.sendRedirect("/th/index.jsp");
		return;
	} else {//此处表明当前用户已经login
		realname = user.getReal_name();

	}
	String orgID = (String) request.getAttribute("orgid");
	String type = (String) request.getAttribute("type");
	logger.info("当前获得到的type值是： " + type);

	String title = "";
	String alertDel = "";
	String typename = "";
	String addressname = "";
	if ("availablelist".equals(type)) {
		typename = "白名单名称"; 
		addressname = "白名单地址";
		title = "频道管理-白名单管理";
		alertDel = "请选择要进行操作的白名单!";

	} else if ("channel".equals(type)) {
		typename = "频道名称";
		addressname = "频道地址";
		title = "频道管理-频道管理";
		alertDel = "请选择要进行操作的频道!";
	} else if ("quick".equals(type)) {
		typename = "快速入口名称";
		addressname = "快速入口地址";
		title = "频道管理-快速入口管理";
		alertDel = "请选择要进行操作的快速入口!";
	}else if("document".equals(type)){
		typename = "企业文档名称";
		addressname = "企业文档内容";
		alertDel = "请选择要进行操作的企业文档!";
		title = "企业文档管理-企业文档管理";

	};
	logger.info("当前的警告语句是: " + alertDel);
	
	String channelName = (String) request.getAttribute("channelName");
	if("null".equals(channelName)||channelName==null){
		channelName = "";
	}
	String channelUrl = (String) request.getAttribute("channelUrl");
	if("null".equals(channelUrl)||channelUrl==null){
		channelUrl = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../../css/channel.css">
<link rel="stylesheet" type="text/css" href="../../css/machine.css">
<link rel="stylesheet" type="text/css" href="../../css/monitor.css">
<link rel="stylesheet" type="text/css" href="../../css/sdmenu.css" />
<link rel="stylesheet" type="text/css" href="../../css/style.css" />
<link rel="stylesheet" type="text/css" href="../../css/menu.css" />
<script type="text/javascript">
			function onFold(id) {
				var vDiv = document.getElementById(id);
				vDiv.style.display = (vDiv.style.display == 'none') ? 'block' : 'none';
				var fold = document.getElementById('foldStyleId');
				if (vDiv.style.display === 'none') {
					fold.className = 'x-fold-plus';
				} else {
					fold.className = 'x-fold-minus';
				}
			}
		</script>
<title><%=title%></title>
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
<SCRIPT LANGUAGE="JavaScript">

	var checkflag = "false";
	function check(field) {
		if (checkflag == "false") {
			for (i = 0; i < field.length; i++) {
				field[i].checked = true;
			}
			checkflag = "true";
		} else {
			for (i = 0; i < field.length; i++) {
				field[i].checked = false;
			}
			checkflag = "false";
		}
	}
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
		
		function goFirst(){
			self.location =  "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_DATALIST_ID%>&pageIdx=1"
		}
		
		function goPrevious(curPageIdx){
			var pageIdx = 1;
			if(curPageIdx>1){
				pageIdx = curPageIdx - 1;
			}
			self.location =  "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_DATALIST_ID%>&pageIdx="+pageIdx
		}
		
		function goNext(curPageIdx){
			var pageMaxNum = document.getElementById("pageNum").value;
			var pageIdx = pageMaxNum;
			if(curPageIdx < pageMaxNum){
				pageIdx = curPageIdx + 1;
			}
			self.location = "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_DATALIST_ID%>&pageIdx="+pageIdx;

		}
		
		function goLast(){
			var pageMaxNum = document.getElementById("pageNum").value;
			self.location = "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_DATALIST_ID%>&pageIdx="+pageMaxNum;

		}
		
		function add(){
			var orgID =document.getElementById("orgid").value;
			if(!orgID || orgID==null || orgID =='' || orgID =='null'){
				alert('请选择要进行操作的组织!');
				return;
			}

			self.location = "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_ADDPAGE_ID%>";
		}

		function btnChangeClick(me){
			var btnId = me.id;
			var requestID = btnId.substring(10);
			self.location = "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_ADDPAGE_ID%>&requestID="+requestID;

		};
		

		function btnWClick(me){
			var btnId = me.id;
			var requestID = btnId.substring(10);
			self.location = "/th/jsp/Available/availablePageList.html?item=look&type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_ADDPAGE_ID%>&requestID="+requestID;

		};
		
		function del(){
			if(!isChecked()){
				alert("<%=alertDel%>");
				return;
			}
			var x=document.getElementsByTagName("input");
			document.getElementById("first").disabled=true;
			document.getElementById("previous").disabled=true;
			document.getElementById("nextPage").disabled=true;
			document.getElementById("lastPage").disabled=true;
			document.getElementById("addData").disabled=true;
			document.getElementById("delData").disabled=true;
			
			self.location = "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_DELETE_ID%>&availableIds="+getCheckedValue();
		}

		function openChannel(){
			if(!isChecked()){
				alert("<%=alertDel%>");
				return;
			}
			var x=document.getElementsByTagName("input");
			document.getElementById("first").disabled=true;
			document.getElementById("previous").disabled=true;
			document.getElementById("nextPage").disabled=true;
			document.getElementById("lastPage").disabled=true;
			document.getElementById("addData").disabled=true;
			document.getElementById("delData").disabled=true;
			self.location = "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_CHANNEL_OPEN_ID%>&availableIds="+getCheckedValue();
		}
		function stopChannel(){
			if(!isChecked()){
				alert("<%=alertDel%>");
				return;
			}
			var x=document.getElementsByTagName("input");
			document.getElementById("first").disabled=true;
			document.getElementById("previous").disabled=true;
			document.getElementById("nextPage").disabled=true;
			document.getElementById("lastPage").disabled=true;
			document.getElementById("addData").disabled=true;
			document.getElementById("delData").disabled=true;
			self.location = "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_CHANNEL_STOP_ID%>&availableIds="+getCheckedValue();
		}
		 function editChannel() {
			if(!isChecked()){
				alert("<%=alertDel%>");
				return;
			}
			document.getElementById("first").disabled=true;
			document.getElementById("previous").disabled=true;
			document.getElementById("nextPage").disabled=true;
			document.getElementById("lastPage").disabled=true;
			document.getElementById("addData").disabled=true;
			document.getElementById("delData").disabled=true;
			var checkValues = getCheckedValue();
			var checkNum = checkValues.split(",");
			if(checkNum.length>1){
				alert("请选择一条记录；");
				return;
			}
			self.location = "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_EDITPAGE_ID%>&availableIds="+getCheckedValue();
         }
         
		function downData(){
			if(!isChecked()){
				alert("<%=alertDel%>");
				return;
			}
			var x=document.getElementsByTagName("input");
			document.getElementById("first").disabled=true;
			document.getElementById("previous").disabled=true;
			document.getElementById("nextPage").disabled=true;
			document.getElementById("lastPage").disabled=true;
			document.getElementById("addData").disabled=true;
			document.getElementById("delData").disabled=true;
			document.getElementById("downData").disabled=true;
			self.location = "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_DOCUMENT_DOWN_ID%>&availableIds="+getCheckedValue();
		}
	
		
		function isChecked(){
			var checkBoxs = document.getElementsByName("maccbox");
			var flag = false;
			for (var i = 0; i < checkBoxs.length; i++) {
				if(checkBoxs[i].checked){
					flag = true;
					break;
				}
			}
			return flag;
		}
		
		function getCheckedValue(){
			var checkBoxs = document.getElementsByName("maccbox");
			var checkedValue = "";
			for (var i = 0; i < checkBoxs.length; i++) {
				if(checkBoxs[i].checked){
					if(checkedValue == ""){
						checkedValue += checkBoxs[i].value;
					}else{
						checkedValue = checkedValue + "," + checkBoxs[i].value;
					}
				}
			}
			return checkedValue;
		}
		
		function search(){
			if(document.getElementById("orgid").value==-1){
				alert("请选择组织");
				return;
			}
			// 拼接搜索条件 
			var serCond = "";
			var orgSelect = document.getElementById("channelName");
			serCond += orgSelect.value + ",";
			var contentDpt = document.getElementById("channelUrl");
			if(contentDpt.value!=""){
				serCond += contentDpt.value;
			}else{
				serCond += "null";
			}
			self.location = "/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_CHANNEL_SEARCH_ID%>&search_con_info="+serCond;

		}
	</script>
<body>
<div style="overflow-x: auto; overflow-y: hidden;">
<form name=myform action="" method=post>
	<input type="hidden" id="pageNum" name="pageNum" value="${pageNum}" /> 
	<input type="hidden" id="orgid" name="orgid" value="<%=request.getAttribute("orgid")%>" />
	<%if("channel".equals(type)) {%>
		<div class="x-title">
			<div id="clientStyleId" class="x-fold-minus" onclick="onFold('foldId');" />
		</div>
		<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
		</div>
		<div id="foldId" style="width: 100%; display: block; height: 30px; line-height: 30px; background-color: #B2DFEE">
			<div style="float: left">
				<span>频道名称</span> 
				<input type="text" name="channelName" id="channelName" value="<%=channelName%>" />
				</select>
			</div>
			<div style="float: left">
				<span>&nbsp;&nbsp;频道路径</span> 
				<input type="text" name="channelUrl" id="channelUrl" value="<%=channelUrl%>" />
			</div>
			<span>&nbsp;&nbsp;</span> 
			<input class="tableBtn" type="button" name="button_Search" id="btnSer" value="搜索" onclick="search()"/>
		</div>
	<%} %>
	<table id="mytable" cellspacing="0">
		<tr>
			<th scope="col" style="width: 2%"><br />
			</th>
			<th scope="col" style="width: 14%" ><%=typename%></th>
			<%if(!"document".equals(type)){%>
			<th scope="col" style="width: 18%"><%=addressname%></th>
			<%} %>
			<%if("document".equals(type)){ %>
			<th scope="col" style="width: 5%"></th>
			<%} %>
			<%if("channel".equals(type)){ %>
			<th scope="col" style="width: 5%">类型</th>
			<%} %>
			<%if("channel".equals(type)){ %>
			<th scope="col" style="width: 5%">状态</th>
			<%} %>
		</tr>
	<%
		HashMap[] availableList = (HashMap[]) request
				.getAttribute("availableList");
	    total_num=availableList.length+"";
		totalPageCount = ReportCommonAction.getTotalPageCount(availableList.length)+"";
		int pageIdx = Integer.parseInt(request.getAttribute("pageIdx")
				.toString());
		if (pageIdx < 1) {
			pageIdx = 1;
		}
		for (int i = 10 * (pageIdx - 1); i < availableList.length
				&& i < 10 * (pageIdx); i++) {

			out.print("<tr>");
			out.print("	 <td class='row'>");
			out.println("	 <input type='checkbox' name='maccbox'  value='" + availableList[i].get("REQUEST_ID") +"@"+ availableList[i].get("FLAG")+ "' />");
			out.println("	 </td>");
			out.println("	 <td class='row'>"
					+ availableList[i].get("REQUEST_NAME") + "</td>");
			if(!"document".equals(type)){
				
			out.println("	 <td class='row'>"
					+ availableList[i].get("REQUEST_URL") + "</td>");
			}
			if("channel".equals(type)){ 
				out.println("	 <td class='row'>"+ ("0".equals((String)availableList[i].get("ENTERPRISES_TYPE")) ? "频道":"企业主页") +"</td>");
			
			}
			if("channel".equals(type)){ 
				out.println("	 <td class='row'>"+ ("1".equals((String)availableList[i].get("STATUS")) ? "启用":"停用") +"</td>");
			
			}
			if("document".equals(type)){
				out.println("	 <td class='row'>"
						+"<input class='tableBtn' type='button' value='编辑' name='buttonChange_" + availableList[i].get("REQUEST_ID") + "' id='btnChange_" + availableList[i].get("REQUEST_ID") + 
				           "' onclick='btnChangeClick(this)'/>"+ 
						"<input class='tableBtn' type='button' value='查看' name='buttonChange_" + availableList[i].get("REQUEST_ID") + "' id='btnChange_" + availableList[i].get("REQUEST_ID") + 
				           "' onclick='btnWClick(this)'/>"+"</td>");
			}
	
			out.println("</tr>");
		}
	%>
</table>
</form>
</div>
<div>
	<input type="button" id="first" class="x-first-page" style="margin-left: 5px" onclick="goFirst()" /> 
	<input type="button" id="previous" class="x-previous-page" style="margin-left: -2px" onclick="goPrevious(${pageIdx})" /> 
	<input type="button" id="nextPage" 	class="x-next-page" style="margin-left: -4px" onclick="goNext(${pageIdx})" /> 
	<input type="button" id="lastPage" 	class="x-last-page" style="margin-left: -2px" onclick="goLast()" />[当前第<%=pageNumber%>页/共<%=totalPageCount%>页][每页<%=page_view%>条][共<%=total_num%>条]
	<input type="button" id="addData" value="添加"	
	<%if ("-1".equals(orgID)) {%> style="float: right; font-family: Verdana; font-size: 10pt; font-weight: bold; border-width: 1px;"
	disabled <%} else {%> class="rightBtn" <%}%> onclick="add()" /> 
	<input
	type="button" id="delData" value="删除" 
	<%if ("-1".equals(orgID)) {%> style="float: right; font-family: Verdana; font-size: 10pt; font-weight: bold; border-width: 1px;"
	disabled <%} else {%> class="rightBtn" <%}%> onclick="del()" />
	
	<%if("channel".equals(type)) {%>
		<input type="button" id="editChannel" value="编辑"	
		<%if ("-1".equals(orgID)) {%> style="float: right; font-family: Verdana; font-size: 10pt; font-weight: bold; border-width: 1px;"
		disabled <%} else {%> class="rightBtn" <%}%> onclick="editChannel()" /> 
		
		<input type="button" id="openChannel" value="启用"	
		<%if ("-1".equals(orgID)) {%> style="float: right; font-family: Verdana; font-size: 10pt; font-weight: bold; border-width: 1px;"
		disabled <%} else {%> class="rightBtn" <%}%> onclick="openChannel()" /> 
		
		<input type="button" id="stopChannel" value="停用"	
		<%if ("-1".equals(orgID)) {%> style="float: right; font-family: Verdana; font-size: 10pt; font-weight: bold; border-width: 1px;"
		disabled <%} else {%> class="rightBtn" <%}%> onclick="stopChannel()" /> 
		
	<%} %>
	
	<%if("document".equals(type)) {%>
			<input
	type="button" id="downData" value="下发" 
	<%if ("-1".equals(orgID)) {%> style="float: right; font-family: Verdana; font-size: 10pt; font-weight: bold; border-width: 1px;"
	disabled <%} else {%> class="rightBtn" <%}%> onclick="downData()" />
	<%} %>
	</div>
	

</body>
</html>