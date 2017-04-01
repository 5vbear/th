<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "MobileClientRunningInfo.jsp";
    logger.info( jspName + " : start" );
    
    User user = (User) session.getAttribute("user_info");
    String realname =null;
    if (user == null) {
	    response.setContentType("text/html; charset=utf-8");
	    response.sendRedirect("/th/mobile.jsp");
    } else {
	    realname = user.getReal_name();
	    logger.info("当前用户名是: " + realname);
    }
%>
<html>
<head>
<title>矩阵监控</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="./css/login.css" media="all">
<link rel="stylesheet" type="text/css" href="./css/monitor.css">
<script language="JavaScript">
function refreshByOrder(type){
	window.document.form_data.orderType.value =type;
	window.document.form_data.pageID.value ="1";
    window.document.form_data.target = "_self";
    window.document.form_data.submit();
}

function toMain(){
	window.document.form_data.action = "/th/jsp/mobile/mobileMain.jsp";
	window.document.form_data.target = "_self";
    window.document.form_data.submit();
}
</script>
</head>
<body>
<div id="box">
<form method="post" action="/th/mobile.html" name="form_data">
<input type="hidden" name="orgID"   value="">
<input type="hidden" name="orderType"   value="2">
<input type="hidden" name="pageID"   value="">
<div   style="HEIGHT: 20px; margin-left:15% ">
<input name="onlineButton" type="button" class="rb_mobile_online" value="在线(<%=(Integer)request.getAttribute("onlineNum")%>)" onclick="refreshByOrder('2');" />
&nbsp;
<input name="offlineButton" type="button" class="rb_mobile_offline" value="离线(<%=(Integer)request.getAttribute("offlineNum")%>)" onclick="refreshByOrder('1');"/>
</div>
 <BR>
<div style="OVERFLOW-Y:auto;height:405px;margin-left:20px;">
 <table  id="orderList" class="table_roll" width="650px" height="400px" border="0" cellspacing="1" cellpadding="3" align="center" frame=void>
   <%=(String)request.getAttribute("MONITOR_RUNNING")%>
  </table>
  </div>
  <br>
  <div   style="HEIGHT: 20px;float:right;margin-right:15%">
  <input name="backButton" type="button" value="返回" class="rb_mobile_back" onclick="toMain();"/>
  </div>
</form>
</div>
</body>
</html>