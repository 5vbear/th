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
HashMap[] map =(HashMap[]) request.getAttribute("map");
User user = (User) session.getAttribute("user_info");
String item =(String) request.getAttribute("item");
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
		

<%if(!"look".equals(item)){ %>
<script type="text/javascript">




</script>
<script type="text/javascript">
  _editor_url = "../js/";
  _editor_lang = "en";


</script>
		<script type="text/javascript" src="../js/htmlarea1.js"></script>
<script language="javascript">
var editor1=null;

</script>

<script type="text/javascript">

HTMLArea.loadPlugin("FullPage");
// this function will get called at body.onload
function initDocument(){
  // cache these values as we need to pass it for both editors

  //---------------------------------------------------------------------
  // GENERAL PATTERN
  //
//  1. Instantitate an editor object.
//  2. Register plugins (note, it's required to have them loaded).
//  3. Configure any other items in editor.config.
//  4. generate() the editor
//
// The above are steps that you use to create one editor.  Nothing new
// so far.  In order to create more than one editor, you just have to
// repeat those steps for each of one.  Of course, you can register any
// plugins you want (no need to register the same plugins for all
// editors, and to demonstrate that we'll skip the TableOperations
// plugin for the second editor).  Just be careful to pass different
// ID-s in the constructor (you don't want to _even try_ to create more
// editors for the same TEXTAREA element ;-)).
//
// So much for the noise, see the action below.
//---------------------------------------------------------------------


  //---------------------------------------------------------------------
  // CREATE FIRST EDITOR
  //
  editor1 = new HTMLArea("requestURL");

  // plugins must be registered _per editor_.  Therefore, we register
  // plugins for the first editor here, and we will also do this for the
  // second editor.
  //editor1.registerPlugin(TableOperations);
  //editor1.registerPlugin(SpellChecker);
  //editor1.registerPlugin(CSS, css_plugin_args);
  editor1.registerPlugin(FullPage);
  // custom config must be done per editor.  Here we're importing the
  // stylesheet used by the CSS plugin.
 //editor1.config.pageStyle = "@import url(custom.css);";

  // generate first editor
  editor1.generate();

}
HTMLArea.onload=initDocument;

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
		
		document.getElementById('requestURL').value=editor1.getHTML();
		document.getElementById("save").disabled=true;
		document.getElementById("backpage").disabled=true;
	    if(name == "") {
	        alert("<%=alertType%>");
	        document.getElementById('requestName').value="";
			document.getElementById("save").disabled=false;
			document.getElementById("backpage").disabled=false;
	        return ;
	    }

		
		form.action = "/th/jsp/Available/availablePageList.html?type=<%=type %>";
		form.submit();
	}
	function updateAvailable(){
		var form = document.getElementById('availableForm');
		var name = document.getElementById('requestName').value.Trim();
		document.getElementById('requestURL').value=editor1.getHTML();
		
		document.getElementById("save").disabled=true;
		document.getElementById("backpage").disabled=true;
	    if(name == "") {
	        alert("<%=alertType%>");
	        document.getElementById('requestName').value="";
			document.getElementById("save").disabled=false;
			document.getElementById("backpage").disabled=false;
	        return ;
	    }
	
		form.action = "/th/jsp/Available/availablePageList.html?type=<%=type %>";
		form.submit();
	}
	
	function back(){
		window.open("/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_DATALIST_ID%>" ,target='sub');

	}
	

</script>
<%}else{ %>
<script type="text/javascript">
function back(){
	window.open("/th/jsp/Available/availablePageList.html?type=<%=type %>&orgID=<%=orgID%>&processId=<%=Define.FUNC_AVAILABLE_PAGE_DATALIST_ID%>" ,target='sub');

}
document.getElementById('span1').innerHTML =<%=map[0].get("REQUEST_URL") %>

</script>
<%}%>
<title><%= title%></title>
</head>
<body  <%if(!"look".equals(item)){ %>onLoad="HTMLArea.init();"<%} %>>
	<div>
		<div class="x-title">
			<span>&nbsp;&nbsp;</span>
		</div>

		<form class="x-client-form" id="availableForm" action="" method="post">
		<%if(requestID!=null&&"document".equals(type)){ %>
				<input type="hidden" id="processId" name ="processId" value="<%=Define.FUNC_AVAILABLE_PAGE_MOD_ID%>"></input>
		<%}else{ %>
				<input type="hidden" id="processId" name ="processId" value="<%=Define.FUNC_AVAILABLE_PAGE_ADD_ID%>"></input>	
		<%} %>
				<input type="hidden" id="orgID" name ="orgID" value="<%=orgID%>"></input>
				<input type="hidden" id="type" name ="type" value="<%=type%>"></input>
				<input type="hidden" id="requestID" name ="requestID" value="<%=requestID%>"></input>
			<table class="x-data-table">
				<tr>
					<th style="width: 20%" class="x-data-table-th"><%=alertTypeMessage %></th>
					<td class="x-data-table-td">
					<%if(requestID!=null&&"document".equals(type)){ %>
						<%if("look".equals(item)){ %>
							<input type="text" size="80" name="requestName" id="requestName" readonly value="<%=map[0].get("REQUEST_NAME") %>"/>
						
						<%}else{ %>
						<input type="text" size="80" name="requestName" id="requestName" value="<%=map[0].get("REQUEST_NAME") %>"/>
						<%} %>
					<%}else{ %>
						<input type="text" size="80" name="requestName" id="requestName" />
					<%} %>
					</td>
				</tr>
				<tr>
					<th style="width: 20%" class="x-data-table-th"><%=alertNameMessage %></th>
					<%if("document".equals(type)){ %>
					<td class="x-data-table-td">
					<%if(requestID!=null&&"document".equals(type)){ %>
								<%if("look".equals(item)){ %>
								<div id="span1"  style="width:600px;height:462px; overflow:scroll;"><%=map[0].get("REQUEST_URL") %> </div>								
									<%}else{ %>
								<textarea rows="20" cols="60" name="requestURL"   id="requestURL" ><%=map[0].get("REQUEST_URL") %></textarea>
									
										<%} %>
										
					<%}else{ %>
										<textarea rows="20" cols="60" name="requestURL"   id="requestURL" ></textarea>
										
					<%} %>
					</td>
					<%}else{ %>
					<td class="x-data-table-td"><input type="text" size="80"
						name="requestURL"   id="requestURL" /></td>
					<%} %>
				</tr>
			</table>
			<br/>
			<div>
				<%if(requestID!=null&&"document".equals(type)){ %>
				<%if("look".equals(item)){ %>
					<%}else{ %>
					<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="保存" id ="save" onclick="updateAvailable();" />
					
					<%} %>
				<%}else{ %>
					<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="保存" id ="save" onclick="insertAvailable();" />
					
				<%} %>
				<%if(!"look".equals(item)){ %>
				<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="返回" id ="backpage" onclick="back();"/>
				<%} %>
			</div>
		</form>
	</div>
</body>
</html>
