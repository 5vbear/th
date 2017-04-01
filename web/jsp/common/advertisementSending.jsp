<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%
String macId =(String)request.getAttribute( "macId" );
String macName =(String)request.getAttribute( "macName" );
String commandType =(String)request.getAttribute( "type" );
System.out.print("11111111111111111111111112222222"+commandType);
String status =(String)request.getAttribute( "status" )==null?"1":(String)request.getAttribute( "status" );
String strContextPath = request.getContextPath();
System.out.print(status);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>下发中</title>
<link rel="stylesheet" type="text/css" href="<%=strContextPath %>/css/monitor.css">
<link rel="stylesheet" type="text/css" href="<%=strContextPath %>/css/sdmenu.css" />
<link rel="stylesheet" type="text/css" href="<%=strContextPath %>/css/style.css"/>
<link rel="stylesheet" type="text/css" href="<%=strContextPath %>/css/menu.css"/>
<link rel="stylesheet" type="text/css" href="<%=strContextPath %>/css/channel.css">


<script src="../js/sdmenu.js" language="javascript"></script>

<script   language="JavaScript" type="text/javascript">


function myrefresh() 
{ 
	var form = document.getElementById('form1');

	document.getElementById('reload').value="true";
	form.action = "/th/cancelSend.html";
	form.submit();
} 
<%if(!"0".equals(status)){%>
setTimeout('myrefresh()',5000);
<%}%>
       
       
	function cancelSend(){
		var form = document.getElementById('form1');
		form.action = "/th/cancelSend.html";
		form.submit();
}

	
	function a(URL)
	{
		window.open(URL,'_self') ;
	} 
	
	
	function returnupdate1(){
		
			window.open("/th/jsp/updateManagement/UpdateManagement.html?action=sendUpdateOrder122","_self");

	}

	function returnTop1(){

		window.open("/th/AdvertServlet?pageId=jsp_programlist_send_id&type=123","_self");
	
	}
	

	//
</script>

</head>
<body>
<form  id="form1" name="form1" method="post" action="">
<input type="hidden" id="type" name="type" value=""/>
<input type="hidden" id="reload" name="reload" value=""/>
<input type="hidden" id="macId" name="macId" value="<%=macId%>"/>
<input type="hidden" id="macName" name="macName" value="<%=macName%>"/>
<input type="hidden" id="commandType" name=commandType value="<%=commandType%>"/>
<div class="x-title"><span>&nbsp;&nbsp;下发</span></div>

<fieldset>
	<%if(!"0".equals(status)){%>
		<%if("updateSend".equals(commandType)){ %>
		<%=macName %>正在升级中，请稍后！
		<%}else{%>
		<%=macName %>正在广告下发中，请稍后！
		<%} %>
	<%}else{%>
		<%if("updateSend".equals(commandType)){ %>
		<%=macName %>下发升级成功，请选择其它操作或请返回！
		<%}else{%>
		<%=macName %>下发广告成功，请选择其它操作或请返回！
		<%} %>
	<%} %>
</fieldset>
		
				
			<div style="alain">
			<%if("updateSend".equals(commandType)){ %>
				<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="返回" id ="returnupdate" onclick="returnupdate1();" />
			
				<!-- <input type="button" <%if("0".equals(status)){%>disabled<%} %> name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="取消" id ="send" onclick="cancelSend();" /> -->
				<%}else{ %>
				<input type="button" name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="返回" id ="returnTop" onclick="returnTop1();" />
				
				<!-- <input type="button"  <%if("0".equals(status)){%>disabled<%} %>  name="" class="rightBtn" style="float:right;font-family: Verdana;font-size: 10pt;font-weight: bold;border-width: 1px;"  value="取消" id ="send" onclick="cancelSend();" /> -->
				
				<%} %>
				
			
			
			</div>
	
	</form>		
</body>

</html>