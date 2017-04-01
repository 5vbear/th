<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="java.util.HashMap"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% 
Log logger = LogFactory.getLog(this.getClass());
String orgID =(String) request.getAttribute("orgid");
String requestID =(String) request.getAttribute("requestID");
String availableIds =(String) request.getAttribute("availableIds");

HashMap[] map =(HashMap[]) request.getAttribute("map");
User user = (User) session.getAttribute("user_info");
String realname =null;
if (user == null) {
	 response.setContentType("text/html; charset=utf-8");
	 response.sendRedirect("/th/index.jsp");
	return;
} else {//此处表明当前用户已经login
	realname = user.getReal_name();

}

String type=(String)request.getAttribute("type");
logger.info("当前获得到的type值是： "+type);
String title="";
String alertType = "";
String alertName = "";
String alertTypeMessage = "";
String alertNameMessage = "";
if("availablelist".equals(type)){
	title="频道管理-白名单管理";
	alertType = "请输入白名单名称!";
	alertName = "请输入白名单地址!";
	alertTypeMessage = "白名单名称";
	alertNameMessage = "白名单地址";
}else if("channel".equals(type)){
	title="频道管理-频道管理";
	alertType = "请输入频道名称!";
	alertName = "请输入频道地址!";
	alertTypeMessage = "频道名称";
	alertNameMessage = "频道地址";
}else if("quick".equals(type)){
	title = "频道管理-快速入口管理";
	alertType = "请输入快速入口名称!";
	alertName = "请输入快速入口地址!";
	alertTypeMessage = "快速入口名称";
	alertNameMessage = "快速入口地址";
}else if("document".equals(type)){
	title = "企业文档管理-企业文档管理";
	alertType = "请输入企业文档名称!";
	alertName = "请输入企业文档内容!";
	alertTypeMessage = "企业文档名称";
	alertNameMessage = "企业文档内容";
};

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="../../css/channel.css"/>
		<link rel="stylesheet" type="text/css" href="../../css/machine.css"/>
		<link rel="stylesheet" type="text/css" href="../../css/monitor.css"/>
		<link rel="stylesheet" type="text/css" href="../../css/sdmenu.css"/>
		<link rel="stylesheet" type="text/css" href="../../css/style.css"/>
		<link rel="stylesheet" type="text/css" href="../../css/menu.css"/>
<script type="text/javascript">

	String.prototype.Trim = function() 
	{ 
	return this.replace(/(^\s*)|(\s*$)/g, ""); 
	} 
	String.prototype.LTrim = function() 
	{ 
	return this.replace(/(^\s*)/g, ""); 
	} 
	String.prototype.RTrim = function() 
	{ 
	return this.replace(/(\s*$)/g, ""); 
	} 
	function insertAvailable(){
		var form = document.getElementById('availableForm');
		var name = document.getElementById('requestName').value.Trim();
		var url = document.getElementById('requestURL').value.Trim();
		document.getElementById("save").disabled=true;
		document.getElementById("backpage").disabled=true;
	    if(name == "") {
	        alert("<%=alertName%>");
	        document.getElementById('requestName').value="";
			document.getElementById("save").disabled=false;
			document.getElementById("backpage").disabled=false;
	        return ;
	    }
	    if(url == "") {
	        alert("<%=alertName%>");
	        document.getElementById('requestURL').value="";
			document.getElementById("save").disabled=false;
			document.getElementById("backpage").disabled=false;
	        return ;
	    }

		
		form.action = "/th/jsp/Available/availablePageList.html?type=<%=type %>&requestID=<%=requestID%>";
		form.submit();
	}
	function updateAvailable(){
		var form = document.getElementById('availableForm');
		var name = document.getElementById('requestName').value.Trim();
		var url = document.getElementById('requestURL').value.Trim();
		document.getElementById("save").disabled=true;
		document.getElementById("backpage").disabled=true;
	    if(name == "") {
	        alert("<%=alertName%>");
	        document.getElementById('requestName').value="";
			document.getElementById("save").disabled=false;
			document.getElementById("backpage").disabled=false;
	        return ;
	    }
	    if(url == "") {
	        alert("<%=alertName%>");
	        document.getElementById('requestURL').value="";
			document.getElementById("save").disabled=false;
			document.getElementById("backpage").disabled=false;
	        return ;
	    }

		
		form.action = "/th/jsp/Available/availablePageList.html?type=<%=type %>";
		form.submit();
	}
	
	function back(){
		window.open("/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_CHANNEL_DO_EDIT_ID%>" ,target='sub');

	}
	

</script>
<title><%= title%></title>
</head>
<body>
	<div>
		<div class="x-title">
			<span>&nbsp;&nbsp;</span>
		</div>

		<form class="x-client-form" id="availableForm" action="">
		<%if(requestID!=null&&"document".equals(type)){ %>
				<input type="hidden" id="processId" name ="processId" value="<%=Define.FUNC_AVAILABLE_PAGE_MOD_ID%>"></input>
		<%}else{ %>
				<input type="hidden" id="processId" name ="processId" value="<%=Define.FUNC_CHANNEL_DO_EDIT_ID%>"></input>	
				<input type="hidden" id="availableIds" name ="availableIds" value="<%=availableIds%>"></input>
				<input type="hidden" id="orgID" name ="orgID" value="<%=orgID%>"></input>
				
		<%} %>
				<input type="hidden" id="orgID" name ="orgID" value="<%=orgID%>"></input>
				<input type="hidden" id="type" name ="type" value="<%=type%>"></input>
				<input type="hidden" id="requestID" name ="requestID" value="<%=requestID%>"></input>
			<table class="x-data-table">
				<tr>
					<th style="width: 20%" class="x-data-table-th"><%=alertTypeMessage %></th>
					<td class="x-data-table-td">
					<%if(requestID!=null&&"document".equals(type)){ %>
						<input type="text" size="80" name="requestName" id="requestName" value="<%=map[0].get("REQUEST_NAME") %>"/>
					<%}else{ %>
						<input type="text" size="80" name="requestName" id="requestName" value="<%=map[0].get("CHANNEL_NAME") %>"/>
					<%} %>
					</td>
				</tr>
				<tr>
					<th style="width: 20%" class="x-data-table-th"><%=alertNameMessage %></th>
					<%if("document".equals(type)){ %>
					<td class="x-data-table-td">
					<%if(requestID!=null&&"document".equals(type)){ %>
							<textarea rows="6" cols="60" name="requestURL"   id="requestURL" ><%=map[0].get("REQUEST_URL") %></textarea>
					<%}else{ %>
							<textarea rows="6" cols="60" name="requestURL"   id="requestURL" ><%=map[0].get("CHANNEL_URL") %></textarea>
					
					<%} %>
					</td>
					<%}else{ %>
					<td class="x-data-table-td"><input type="text" size="80"
						name="requestURL"   id="requestURL" value="<%=map[0].get("CHANNEL_URL") %>"/></td>
					<%} %>
				</tr>
				<%if("channel".equals(type)){ %>
				<tr>
					<th style="width: 20%" class="x-data-table-th">类型</th>
					<td class="x-data-table-td">
					<%if("0".equals(map[0].get("ENTERPRISES_TYPE"))){ %>
					<label><input type="radio" name="enterType" value="0" checked='checked' onclick=""/>频道</label>
					<label><input type="radio" name="enterType" value="1" onclick=""/>企业主页</label>
					<%} else{%>
					<label><input type="radio" name="enterType" value="0"  onclick=""/>频道</label>
					<label><input type="radio" name="enterType" value="1" checked='checked' onclick=""/>企业主页</label>
					<%} %>
					</td>
				</tr>
				<%} %>
			</table>
			<br/>
			<div>
				<%if(requestID!=null&&"document".equals(type)){ %>
					<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="保存" id ="save" onclick="updateAvailable();" />
				<%}else{ %>
					<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="保存" id ="save" onclick="insertAvailable();" />
					
				<%} %>
				<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="返回" id ="backpage" onclick="back();"/>
			</div>
		</form>
	</div>
</body>
</html>
