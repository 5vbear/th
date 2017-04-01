<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
    String jspName = "macConfigList.jsp";
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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="./css/channel.css">
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/style.css"/>
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
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
		<title>端机配置</title>
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
			width: 710px;
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
	<!-- Begin
		var checkflag = "false";
		function check(field) {
			if (checkflag == "false") {
				for (i = 0; i < field.length; i++) {
					field[i].checked = true;
				}
				checkflag = "true";
				return "取消";
			} else {
				for (i = 0; i < field.length; i++) {
					field[i].checked = false;
				}
				checkflag = "false";
				return "全选";
			}
		}
		//  End -->
	
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
		
		function add(){
			self.location = "/th/machineServlet?model=config&method=goAdd&pageIdx=${pageIdx}&orgid=${orgid}";
		}
		
		function edit(cfgid){
			self.location = "/th/machineServlet?model=config&method=goEdit&pageIdx=${pageIdx}&orgid=${orgid}&cfgid="+cfgid;
		}
		
		function deleteData(cfgid){
			self.location = "/th/machineServlet?model=config&method=deleteData&cfgIds="+cfgid+"&orgid=${orgid}";
		}
		
		function deleteDatas(){
			if(!isChecked()){
				alert("请选择要进行操作的模版!");
				return;
			}
			self.location = "/th/machineServlet?model=config&method=deleteData&cfgIds="+getCheckedValue()+"&orgid=${orgid}";
		}
		
		function distribute(){
			if(!isCheckedOne()){
				alert("请选择一个要进行操作的模版!");
				return;
			}
			var orgID = document.getElementById("orgid").value;
			if(!orgID || orgID==null || orgID =='' || orgID =='null'){
				alert('请选择要进行操作的组织!');
				return;
			}
			self.location = "/th/machineServlet?model=config&method=distribute&cfgIds="
					+getCheckedValue()+"&pageIdx=${pageIdx}&orgid="+orgID;
		}
		
		function goFirst(){
			self.location = "/th/machineServlet?model=config&method=cfgListQry&pageIdx=1&orgid=${orgid}";
		}
		
		function goPrevious(curPageIdx){
			var pageIdx = 1;
			if(curPageIdx>1){
				pageIdx = curPageIdx - 1;
			}
			self.location = "/th/machineServlet?model=config&method=cfgListQry&pageIdx="+pageIdx+"&orgid=${orgid}";
		}
		
		function goNext(curPageIdx){
			var pageMaxNum = document.getElementById("pageNum").value;
			var pageIdx = pageMaxNum;
			if(curPageIdx < pageMaxNum){
				pageIdx = curPageIdx + 1;
			}
			self.location = "/th/machineServlet?model=config&method=cfgListQry&pageIdx="+pageIdx+"&orgid=${orgid}";
		}
		
		function goLast(){
			var pageMaxNum = document.getElementById("pageNum").value;
			self.location = "/th/machineServlet?model=config&method=cfgListQry&pageIdx="+pageMaxNum+"&orgid=${orgid}";
		}
		
		function isChecked(){
			var checkBoxs = document.getElementsByName("maccbox");
			var flag = false;
			for (var i = 1; i < checkBoxs.length; i++) {
				if(checkBoxs[i].checked){
					flag = true;
					break;
				}
			}
			return flag;
		}
		
		function isCheckedOne(){
			var checkBoxs = document.getElementsByName("maccbox");
			var cnt = 0;
			for (var i = 1; i < checkBoxs.length; i++) {
				if(checkBoxs[i].checked){
					cnt++;
				}
			}
			return cnt==1?true:false;
		}
		
		function getCheckedValue(){
			var checkBoxs = document.getElementsByName("maccbox");
			var checkedValue = "";
			for (var i = 1; i < checkBoxs.length; i++) {
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
	</script>
	<body style="background-color: #fff;">
		<div>
		    <div>
				<form name=myform action="" method="post" >
					<input type="hidden" id="pageNum" name="pageNum" value="${pageNum}" />
					<input type="hidden" id="orgid" name="orgid" value="<%=request.getAttribute("orgid") %>" />
					<table id="mytable" cellspacing="0">
						<tr>
							<th scope="col" style="width:2%"><input type=checkbox name=maccbox
								onClick="this.value=check(this.form.maccbox)" value="" /></th>
							<th scope="col" style="width:18%">配置名称</th>
							<th scope="col" style="width:26%">配置说明</th>
							<th scope="col" style="width:15%">创建用户</th>
							<th scope="col" style="width:20%">创建时间</th>
							<th scope="col" style="width:19%">操作</th>
						</tr>	
						<%!
							private String parseStr(Object obj){
								if(obj != null){									
									return obj.toString();
								}else{
									return "";
								}
							}
						%>				
					    <%
							HashMap[] cfgList = (HashMap[]) request.getAttribute( "cfgList" );
					    	int pageIdx = Integer.parseInt(request.getAttribute( "pageIdx" ).toString());
					    	if(pageIdx<1){
					    		pageIdx = 1;
					    	}
							for ( int i = 10*(pageIdx-1); i<cfgList.length && i<10*(pageIdx); i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row'>");
								out.print("	   <input type='checkbox' name='maccbox' value='" + cfgList[i].get("MDID") + "' />");
								out.print("	 </td>");
								out.print("	 <td class='row'>" + parseStr(cfgList[i].get("MDNAME")) + "</td>");
								out.print("	 <td class='row'>" + parseStr(cfgList[i].get("MDDESC")) + "</td>");
								out.print("	 <td class='row'>" + parseStr(cfgList[i].get("UNAME")) + "</td>");
								out.print("	 <td class='row'>" + cfgList[i].get("CTIME") + "</td>");
								out.print("	 <td class='row'>");
								out.print("	 	<input type='button' value='编辑' class='leftBtn' onclick='edit("+cfgList[i].get("MDID")+")'/>");
								out.print("	 	<input type='button' value='删除' class='leftBtn' onclick='deleteData("+cfgList[i].get("MDID")+")'/>");
								out.print("	 </td>");
								out.print("</tr>");
							}
						%>
					</table>
				</form>
	    	</div>
	  	</div>
		<div>
			<input type="button" class="x-first-page" style="margin-left:5px" onclick="goFirst()" /> 
			<input type="button" class="x-previous-page" style="margin-left:-2px" onclick="goPrevious(${pageIdx})" /> 
			<input type="button" class="x-next-page" style="margin-left:-4px" onclick="goNext(${pageIdx})" />
			<input type="button" class="x-last-page" style="margin-left:-2px" onclick="goLast()" />
			[当前第${pageIdx}页/共${pageNum}页]
			<input type="button" value="下发" class="rightBtn" onclick="distribute()" />  
			<input type="button" value="删除" class="rightBtn" onclick="deleteDatas()" /> 
			<input type="button" value="添加" class="rightBtn" onclick="add()" />
		</div>
	</body>
</html>