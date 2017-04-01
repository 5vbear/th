<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%> 
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macDictList.jsp";
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
		<link href="./css/machine.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="./css/advert.css">
		<link rel="stylesheet" type="text/css" href="./css/channel.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/style.css"/>
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
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
		<title>配置数字字典</title>
	</head>
	<%!
		private String parseStr(Object obj){
			if(obj != null){									
				return obj.toString();
			}else{
				return "";
			}
		}
	%>
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
			document.getElementById("macType").value='${macType}';
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
		
		function operQry(){
			var macType = document.getElementById("macType").value;
			self.location = "/th/machineServlet?model=dict&method=query&pageIdx=1&macType="+macType;
		}
		
		function goFirst(){
			self.location = "/th/machineServlet?model=dict&method=query&pageIdx=1&macType=${macType}";
		}
		
		function goPrevious(curPageIdx){
			var pageIdx = 1;
			if(curPageIdx>1){
				pageIdx = curPageIdx - 1;
			}
			self.location = "/th/machineServlet?model=dict&method=query&pageIdx="+pageIdx+"&macType=${macType}";
		}
		
		function goNext(curPageIdx){
			var pageMaxNum = document.getElementById("pageNum").value;
			var pageIdx = pageMaxNum;
			if(curPageIdx < pageMaxNum){
				pageIdx = curPageIdx + 1;
			}
			self.location = "/th/machineServlet?model=dict&method=query&pageIdx="+pageIdx+"&macType=${macType}";
		}
		
		function goLast(){
			var pageMaxNum = document.getElementById("pageNum").value;
			self.location = "/th/machineServlet?model=dict&method=query&pageIdx="+pageMaxNum+"&macType=${macType}";
		}
		
		function add(){
			self.location = "/th/machineServlet?model=dict&method=goAdd&pageIdx=${pageIdx}&macType=${macType}";
		}
		
		function deleteData(){
			if(!isChecked()){
				alert("请选择要删除的字典信息!");
				return;
			}
			self.location = "/th/machineServlet?model=dict&method=delete&pageIdx=${pageIdx}&macType=${macType}&oid="+getCheckedValue();
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
	</script>
	<body style="background-color: #fff;">
		<div class="x-title"><span>&nbsp;&nbsp;端机管理-配置数字字典</span></div>
		<table><tr style ="height:3px"></tr></table>
		<div class="x-title">
	    	<div id="foldStyleId" class="x-fold-minus" onclick="onFold('foldId');"/>
		</div>
		<div style="height:26px;text-align:left;line-height:26px">检索</div>
		<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
	    	<div class="x-chanelName" style="width:100%;padding-left:20px">
		    	<span style="margin-left:20px">端机类型:</span>
		    	<select name="select" id="macType" name="macType" style="width:180px">
			        <option value=""></option>
			        <option value="Microsoft Windows XP">Microsoft Windows XP</option>
			        <option value="Windows 7 Professional">Windows 7 Professional</option>
			        <option value="Android">Android</option>
		    	</select>
				<input type="button" value="搜索" onclick="operQry()"/>
		    </div>
		</div>
		<div>
		    <div class="x-data">
		    	<span  style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span>
		    </div>
		    <div>
				<form name=myform action="" method=post>
					<input type="hidden" id="pageNum" name="pageNum" value="${pageNum}" />
					<input type="hidden" id="mType" name="mType" value="${macType}"/>
					<table id="mytable" cellspacing="0">
						<tr>
							<th scope="col" style="width:2%"><input type=checkbox name=maccbox
								onClick="this.value=check(this.form.maccbox)" value="" /></th>
							<th scope="col" style="width:20%">端机类型</th>
							<th scope="col" style="width:10%">操作分类</th>
							<th scope="col" style="width:68%">操作名称</th>
						</tr>						
					    <%
							HashMap[] dictlist = (HashMap[]) request.getAttribute( "dictlist" );
					    	int pageIdx = Integer.parseInt(request.getAttribute( "pageIdx" ).toString());
					    	if(pageIdx<1){
					    		pageIdx = 1;
					    	}
							for ( int i = 10*(pageIdx-1); i<dictlist.length && i<10*(pageIdx); i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row'>");
								out.print("	   <input type='checkbox' name='maccbox' value='" + dictlist[i].get("OID") + "' />");
								out.print("	 </td>");
								out.print("	 <td class='row'>" + parseStr(dictlist[i].get("OS")) + "</td>");
								out.print("	 <td class='row'>" + parseStr(dictlist[i].get("OTYPE")) + "</td>");
								out.print("	 <td class='row'>" + parseStr(dictlist[i].get("ONAME")) + "</td>");
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
			[当前第${pageIdx}页/共${pageNum}页][每页10条][共<%=dictlist.length %>条]
			<input type="button" class="rightBtn" value="添加" onclick="add()" /> 
			<input type="button" class="rightBtn" value="删除" onclick="deleteData()" /> 
		</div>
	</body>
</html>