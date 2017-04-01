<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/channel.css" />
<%@ page import="th.entity.ChannelBean"%>
<%@ page import="java.util.*" %>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>	
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
<script type="text/javascript">
	function saveChannel(){
		var form = document.getElementById('channelForm');
		document.getElementById('searchType').value = "saveEdit";
		form.action = "/th/jsp/channel/editChannel.html";
		form.submit();
	}
</script>
<title>快速入口编辑</title>
</head>
<body>
	<div>
		<div class="x-title">
			<span>&nbsp;&nbsp;报表管理-快速入口编辑</span>
		</div>

		<form class="x-client-form" id="channelForm" action="">
			<input type="hidden" name="type" id="searchType" />
			<input type="hidden" name="channelType" value="1" />
			<%
				ChannelBean channelBean = (ChannelBean)request.getAttribute( "channelBean" );
			%>
			<table class="x-data-table">
				<input type="hidden" name="channelNameId" value="<%=channelBean.getChannelId()%>" />
				<tr>
					<th style="width: 20%" class="x-data-table-th">快速入口名称</th>
					<td class="x-data-table-td"><input type="text" size="80"
						name="channelName" value="<%=channelBean.getChannelName()%>"/></td>
				</tr>
				<tr>
					<th style="width: 20%" class="x-data-table-th">快速入口路径</th>
					<td class="x-data-table-td"><input type="text" size="80"
						name="channelPath" value="<%=channelBean.getChannelPath()%>"/></td>
				</tr>
			</table>
			<div class="x-client-form">
				<input type="button" name="" value="保存" onclick="saveChannel();"  class="x-button"/>
				<input type="button" name="" value="返回" onClick="javascript:window.history.back()" class="x-button"/>
			</div>
		</form>
	</div>
</body>
</html>
