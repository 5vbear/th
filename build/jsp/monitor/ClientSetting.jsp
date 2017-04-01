<%@ page contentType="text/html; charset=utf-8" language="java"  %>
<%@ page import="java.util.*" %>
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="th.com.util.Define" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
	Log logger = LogFactory.getLog("main.jsp");
    String jspName = "ClientSetting.jsp";
    logger.info( jspName + " : start" );
	List list = (List)request.getAttribute( "list" );
	List nameList = (List)request.getAttribute( "nameList" );
	String orgID = (String)request.getAttribute( "orgID" );

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<style type="text/css">
body {
	font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica,
		sans-serif;

}

a {
	color: #c75f3e;
}

#mytable {
	width: 700px;
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
.rightBtn{
	background-color:#D0E9D0;
	color:#4f6b72;
	float:right;
	font-family: Verdana;
	font-size: 10pt;
	font-weight: bold;
	border-width: 1px;
	margin-right: 3px;
}
</style>


<script language="JavaScript"  type="text/javascript">

	
	function saveData(){
		alert("保存成功！");
		window.document.myform.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_ALART_SELECT_ID %>";
		window.document.myform.action = "<%= Define.MONITOR_SERVLET %>";
		window.document.myform.orgID.value ="<%= orgID%>";
		window.document.myform.saveType.value ="1";
		window.document.myform.submit();
	}

	function allCheck() {
		var check = document.getElementsByName("checkbox");
		if(check[0].checked==false){
			 for (var i=0;i<check.length;i++) {
				 check[i].checked = true;
			 }
		}else{
			 for (var i=0;i<check.length;i++) {
				 check[i].checked = false;
			 }
		}

		}

	
	function resetData(){
		window.document.myform.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_ALART_SELECT_ID %>";
		window.document.myform.action = "<%= Define.MONITOR_SERVLET %>";
		window.document.myform.orgID.value ="<%= orgID%>";
		window.document.myform.submit();	
	}
	
	function e(){
		window.document.getElementById("saveButton").disabled =false;
		window.document.getElementById("resetButton").disabled =false;
		window.document.getElementById("selectAll").disabled =false;

		var origLength;
		origLength = document.all.length;
		for(i=0;i<origLength;i++){
		    document.all[i].disabled=false;
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
	
</script>
</head>
<body>
	<form name=myform  method=post>
		<input type="hidden" name="orgID"   value=""/>
		<input type="hidden" name="pageID"   value=""/>
		<input type="hidden" name="saveType"   value=""/>
		<table id="mytable" cellspacing="0">
			<caption></caption>
			<tr>
				<th scope="col">警告类型</th>
				<th scope="col">邮件报警</th>
				<th scope="col">短信报警</th>
			</tr>
			<%if(nameList!=null&&nameList.size()>0){
				for(int i=0;i<nameList.size();i++){
					String nameAndId =(String)nameList.get(i);
					if(nameAndId.split("@@").length>0){
						String  id = nameAndId.split("@@")[0];
						String  name = nameAndId.split("@@")[1];
					
					%>
					
			<tr>
				<td class="row"><div align="left"><span class="STYLE5"><%=name %></span></div></td>
				<td class="row"><label><input type="checkbox" disabled=true    <%if (list.contains(id+"@@2")){%> checked <%} %> name ="checkbox" id ="<%=id+"@@2"%>" value="<%=id+"@@2"%>"/></label></td>
				<td class="row"><label><input type="checkbox" disabled=true     <%if (list.contains(id+"@@1")){%> checked <%} %> name ="checkbox" id ="<%=id+"@@1"%>"  value="<%=id+"@@1"%>"/></label></td>
			</tr>					
					
		<%
					}
				}
			} 
			
			%>
											
		</table>	
		<input type="button" id ="exitButton" <% if("-1".equals(orgID)){ %>disabled<%} %>  class="rightBtn" value="编辑" onclick="e()"/> 
		<input type="button" id ="resetButton"    disabled=true   class="rightBtn" value="撤销" onclick="resetData()" /> 
		
		<input type="button" id ="saveButton"    disabled=true   class="rightBtn" value="保存" onclick="saveData()" /> 
		<input type="button" id ="selectAll"    disabled=true   class="rightBtn" value="全选/反选" onclick="allCheck()" /> 
	
	</form>
</body>
</html>