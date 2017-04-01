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
    String jspName = "macAuditList.jsp";
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
		<title>端机审核</title>
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
			if('${auditmsg}' != null && '${auditmsg}' != ''){
				alert('${auditmsg}');
			}
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
			var orgID = document.getElementById("orgid").value;
			self.location = "/th/machineServlet?model=audit&method=maclistQry&pageIdx=1"+"&orgid="+orgID;
		}
		
		function goPrevious(curPageIdx){
			var pageIdx = 1;
			if(curPageIdx>1){
				pageIdx = curPageIdx - 1;
			}
			var orgID = document.getElementById("orgid").value;
			self.location = "/th/machineServlet?model=audit&method=maclistQry&pageIdx="+pageIdx+"&orgid="+orgID;
		}
		
		function goNext(curPageIdx){
			var pageMaxNum = document.getElementById("pageNum").value;
			var pageIdx = pageMaxNum;
			if(curPageIdx < pageMaxNum){
				pageIdx = curPageIdx + 1;
			}
			var orgID = document.getElementById("orgid").value;
			self.location = "/th/machineServlet?model=audit&method=maclistQry&pageIdx="+pageIdx+"&orgid="+orgID;;
		}
		
		function goLast(){
			var orgID = document.getElementById("orgid").value;
			var pageMaxNum = document.getElementById("pageNum").value;
			self.location = "/th/machineServlet?model=audit&method=maclistQry&pageIdx="+pageMaxNum+"&orgid="+orgID;;
		}
		
		function audit(){
			if(!isChecked()){
				alert("请选择要进行操作的端机!");
				return;
			}
			var isChildOrg =document.getElementById("isChildOrg").value;
			if(isChildOrg=="1"){
				alert('请选择根节点组织!');
				return;
			}
			var orgID =document.getElementById("orgid").value;
			if(!isOrgChecked()){
				if(!orgID || orgID==null || orgID =='' || orgID =='null'){
					alert('请选择要进行操作的组织!');
					return;
				}
			}
			
			parent.location = "/th/machineServlet?model=audit&method=macsAudit&macIds="+getCheckedValue()+"&orgid="+orgID;
		}
		
		function deleteMacs(){
			if(!isChecked()){
				alert("请选择要进行操作的端机!");
				return;
			}
			self.location = "/th/machineServlet?model=audit&method=macsDelete&macIds="+getCheckedValueOnDel();
		}
		
		function batchAudit(){
			self.location = "/th/machineServlet?model=audit&method=goBatch";
		}
		
		function isChecked(){
			var checkBoxs = document.getElementsByName("maccbox");
			var flag = false;
			//var strKey = "";
			for (var i = 1; i < checkBoxs.length; i++) {
				//strKey = checkBoxs[i].value;
				//if( -1!=strKey.indexOf("@")){
					//flag = true;
					//break;
				//}
				if(checkBoxs[i].checked){
					flag = true;
					break;
				}
			}
			return flag;
		}

		function isOrgChecked(){
			var checkBoxs = document.getElementsByName("maccbox");
			var flag = false;
			var strKey = "";
			for (var i = 1; i < checkBoxs.length; i++) {
				strKey = checkBoxs[i].value;
				if( -1!=strKey.indexOf("@") && checkBoxs[i].checked){
					flag = true;
					break;
				}
			}
			return flag;
		}

		function isCheckedOnDel(){
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
		
		function getCheckedValue(){
			var checkBoxs = document.getElementsByName("maccbox");
			var orgID = document.getElementById("orgid").value;
			var checkedValue = "";
			var strKey = "";
			for (var i = 1; i < checkBoxs.length; i++) {
				strKey = checkBoxs[i].value;
				/*
				if(checkBoxs[i].checked){
					if(-1!=strKey.indexOf("@")){
						if("null"!=orgID){
							strKey=strKey.substring(0,strKey.lastIndexOf("@")+1)+orgID;
						}
					}
					if(checkedValue == ""){
						checkedValue += strKey;
					}else{
						checkedValue = checkedValue + "," + strKey;
					}
				}
*/
				
				
				if( -1!=strKey.indexOf("@")){
				if(checkBoxs[i].checked){
					if("null"!=orgID){
						strKey=strKey.substring(0,strKey.lastIndexOf("@")+1)+orgID;
					}
				if(checkedValue == ""){
					checkedValue += strKey;
				}else{
					checkedValue = checkedValue + "," + strKey;
				}
				}
				continue;
			}
			
			if(checkBoxs[i].checked){
				if("null"!=orgID){
					if(checkedValue == ""){
						checkedValue += strKey;
					}else{
						checkedValue = checkedValue + "," + strKey;
					}
				}
			}
				
			}
			return checkedValue;
		}

		function getCheckedValueOnDel(){
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
				<form name=myform action="" method=post>
					<input type="hidden" id="pageNum" name="pageNum" value="${pageNum}" />
					<input type="hidden" id="orgid" name="orgid" value="<%=request.getAttribute("orgid") %>" />
					<input type="hidden" id="isChildOrg" name="isChildOrg" value="<%=request.getAttribute("isChildOrg") %>" />
					<table id="mytable" cellspacing="0">
						<tr>
							<th scope="col" style="width:2%"><input type=checkbox name=maccbox
								onClick="this.value=check(this.form.maccbox)" value="" /></th>
							<th scope="col" style="width:14%">端机标识</th>
							<th scope="col" style="width:10%">端机IP</th>
							<th scope="col" style="width:12%">机器名称</th>
							<th scope="col" style="width:14%">端机类型</th>
							<th scope="col" style="width:20%">MAC地址</th>
							<th scope="col" style="width:14%">注册时间</th>
							<th scope="col" style="width:16%">备注</th>
						</tr>						
					    <%
							HashMap[] maclist = (HashMap[]) request.getAttribute( "maclist" );
					    	int pageIdx = Integer.parseInt(request.getAttribute( "pageIdx" ).toString());
					    	if(pageIdx<1){
					    		pageIdx = 1;
					    	}
							for ( int i = 10*(pageIdx-1); i<maclist.length && i<10*(pageIdx); i++ ) {
								String remarkStr = "";
								if(null == maclist[i].get("REMARK")){
									remarkStr = "";
								} else{
									remarkStr = StringUtils.transStr(maclist[i].get("REMARK").toString());
								}
								String strID="";
								String strName="";
								if(!"".equals(remarkStr)){
									String[] splitRemark = remarkStr.split(",");
									strID = splitRemark[0];
									strName = splitRemark[1];
								}
								out.print("<tr>");
								out.print("	 <td class='row'>");
								if(!"".equals(remarkStr)){
									out.print("	   <input type='checkbox' name='maccbox' value='" + maclist[i].get("MID") + "@" + strID + "' />");
								} else {
									out.print("	   <input type='checkbox' name='maccbox' value='" + maclist[i].get("MID") + "' />");
								}
								out.print("	 </td>");
								out.print("	 <td class='row'>" + maclist[i].get("MMARK") + "</td>");
								out.print("	 <td class='row'>" + maclist[i].get("MIP") + "</td>");
								out.print("	 <td class='row'>" + maclist[i].get("CNAME") + "</td>");
								out.print("	 <td class='row'>" + maclist[i].get("MTYPE") + "</td>");
								out.print("	 <td class='row'>" + maclist[i].get("MMAC") + "</td>");
								out.print("	 <td class='row'>" + maclist[i].get("RTIME") + "</td>");
								out.print("	 <td class='row'>" + strName + "</td>");
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
			[当前第${pageIdx}页/共${pageNum}页][每页10条][共<%=maclist.length %>条]
			<input type="button" value="删除" class="rightBtn" onclick="deleteMacs()" /> 
			<input type="button" value="注册审核" class="rightBtn" onclick="audit()" /> 
			<input type="button" value="批量审核" class="rightBtn" onclick="batchAudit()" /> 
		</div>
	</body>
</html>