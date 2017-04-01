<%@ page contentType="text/html; charset=utf-8" language="java" pageEncoding="UTF-8"
	import="java.sql.*" errorPage=""%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/channel.css" />
	<link rel="stylesheet" href="../../zTree/css/demo.css" type="text/css">
	<link rel="stylesheet" href="../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="../../zTree/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="../../zTree/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="../../zTree/js/jquery.ztree.excheck-3.5.js"></script>	
<script type="text/javascript">
	function insertChannel(){
		var form = document.getElementById('channelForm');
		document.getElementById('searchType').value = "add";
		form.action = "/th/jsp/channel/addChannel.html";
		form.submit();
	}
	function checkChannelName(channelName){
		if(channelName === ''){
			return;
		}
		xmlHttp = this.getXmlHttpRequest();
		if(xmlHttp == null) {
		    alert("Explore is Unsupport XmlHttpRequest！");
		    return;
		}
		var url = "/th/jsp/channel/addChannel.html?type=check&channelName="+channelName ;
		url = encodeURI(encodeURI(url));

		xmlHttp.open("GET", url, true);
		xmlHttp.onreadystatechange = this.callBack;
		xmlHttp.setRequestHeader( "Content-Type", "text/html;charset=UTF-8" );
		xmlHttp.send(null);
	}
	
	function callBack() {
	    if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
	        var res = xmlHttp.responseText;
	        if(res != 0){
	        	var channelName = document.getElementById('channelNameId').value;
	        	alert('系统已存在名为"' + channelName + '"的频道，请重新填写!');
	        	document.getElementById('channelNameId').focus();
	        }
	    }
	}
	
	function getXmlHttpRequest() {
		if (window.XMLHttpRequest) { 
			//alert("非IE浏览器"); 
			return new XMLHttpRequest(); 
			} else if (window.ActiveXObject && !window.XMLHttpRequest){ 
			var aVersion = ["MSXML2.XMLHttp.6.0", 
			"MSXML2.XMLHttp.5.0", "MSXML2.XMLHttp.4.0", 
			"MSXML2.XMLHttp.3.0", "MSXML2.XMLHttp", 
			"Microsoft.XMLHttp"]; 
			for (var i = 0; i < aVersion.length; i++) { 
			try { 
			var oXmlHttp = new ActiveXObject(aVersion[i]); 
			//alert("IE浏览器版本"+aVersion[i]); 
			return oXmlHttp; 
			} 
			catch (ex) {} 
			} 
			} 
			throw new Error("创建XMLHttpRequest对象出错!"); 
	    return  xmlHttp; 
	}
</script>
<%
	Log logger = LogFactory.getLog(this.getClass());
	User user = (User) session.getAttribute("user_info");
	String realname =null;
	if (user == null) {
	     response.setContentType("text/html; charset=utf-8");
	     response.sendRedirect("/th/index.jsp");
	} else {
	    realname = user.getReal_name();
	    logger.info("获得当前用户的用户名，用户名是： " + realname);
	}
%>
<title>频道定义</title>
</head>
<body>
	<div>
		<div class="x-title">
			<span>&nbsp;&nbsp;系统管理-频道定义添加</span>
		</div>
		<table><tr style ="heigt:30px"></tr></table>
		<form class="x-client-form" id="channelForm" action="">
			<input type="hidden" name="type" id="searchType" />
			<input type="hidden" name="channelType" value="0" />
			<table class="x-data-table">
				<tr>
					<th style="width: 20%" class="x-data-table-th">&nbsp;&nbsp;频道名称</th>
					<td class="x-data-table-td"><input type="text" size="80"
						name="channelName"  id="channelNameId" onblur="checkChannelName(this.value);"/></td>
				</tr>
				<tr>
					<th style="width: 20%" class="x-data-table-th">&nbsp;&nbsp;频道路径</th>
					<td class="x-data-table-td"><input type="text" size="80"
						name="channelPath" /></td>
				</tr>
			</table>
			<div style="float:left">
				<input type="button" name="" value="保存" onclick="insertChannel();" class="x-button"/>
				<input type="button" name="" value="返回" onClick="javascript:window.history.back()" class="x-button"/>
			</div>
		</form>
	</div>
</body>
</html>
