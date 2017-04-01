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
    String jspName = "macDeployList.jsp";
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
		<title>系统操作日志</title>
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
		
		function goFirst(){
			self.location = "/th/machineServlet?model=deploy&method=query&pageIdx=1"
		}
		
		function goPrevious(curPageIdx){
			var pageIdx = 1;
			if(curPageIdx>1){
				pageIdx = curPageIdx - 1;
			}
			self.location = "/th/machineServlet?model=deploy&method=query&pageIdx="+pageIdx;
		}
		
		function goNext(curPageIdx){
			var pageMaxNum = document.getElementById("pageNum").value;
			var pageIdx = pageMaxNum;
			if(curPageIdx < pageMaxNum){
				pageIdx = curPageIdx + 1;
			}
			self.location = "/th/machineServlet?model=deploy&method=query&pageIdx="+pageIdx;
		}
		
		function goLast(){
			var pageMaxNum = document.getElementById("pageNum").value;
			self.location = "/th/machineServlet?model=deploy&method=query&pageIdx="+pageMaxNum;
		}
		
		function macview(macid){
			self.location = "/th/machineServlet?model=deploy&method=view&pageIdx="+${pageIdx}+"&macIdStd="+macid;
		}
	</script>
	<body style="background-color: #fff;">
		<div class="x-title"><span>&nbsp;&nbsp;端机管理-端机部署</span></div>
		<table><tr style ="height:3px"></tr></table>
		<div>
			<form name=myform action="" method=post>
				<input type="hidden" id="pageNum" name="pageNum" value="${pageNum}" />
				<input type="hidden" id="orgid" name="orgid" value="<%=request.getAttribute("orgid") %>" />
				<table id="mytable" cellspacing="0">
					<tr>
						<th scope="col" style="width:16%">所属组织</th>
						<th scope="col" style="width:10%">端机标识</th>
						<th scope="col" style="width:10%">设备类型</th>
						<th scope="col" style="width:25%">网点名称</th>
						<th scope="col" style="width:23%">负责人/电话</th>
						<th scope="col" style="width:16%">注册时间</th>
					</tr>						
				    <%
						HashMap[] maclist = (HashMap[]) request.getAttribute( "maclist" );
				    	int pageIdx = Integer.parseInt(request.getAttribute( "pageIdx" ).toString());
				    	if(pageIdx<1){
				    		pageIdx = 1;
				    	}
						for ( int i = 10*(pageIdx-1); i<maclist.length && i<10*(pageIdx); i++ ) {
							out.print("<tr>");
							out.print("	 <td class='row'>" + maclist[i].get("ONAME") + "</td>");
							out.print("	 <td class='row'><a href='javascript:macview(" + maclist[i].get("MID") + ")' class='tableLink'>" 
									+ maclist[i].get("MMARK") + "</a></td>");
							out.print("	 <td class='row'>");
							out.print(maclist[i].get("MACTYPE")==null?"":maclist[i].get("MACTYPE"));
							out.print("</td>");
							out.print("	 <td class='row'>");
							out.print(maclist[i].get("BNAME")==null?"":maclist[i].get("BNAME")); 
							out.print("  </td>");
							out.print("	 <td class='row'>");
							out.print(maclist[i].get("MANAME")==null?"":maclist[i].get("MANAME"));
							out.print("/");
							out.print(maclist[i].get("TPHONE")==null?"":maclist[i].get("TPHONE")); 
							out.print("  </td>");
							out.print("	 <td class='row'>" + maclist[i].get("RTIME") + "</td>");
							out.print("</tr>");
						}
					%>
				</table>
			</form>
	  	</div>
		<div>
			<input type="button" class="x-first-page" style="margin-left:5px" onclick="goFirst()" /> 
			<input type="button" class="x-previous-page" style="margin-left:-2px" onclick="goPrevious(${pageIdx})" /> 
			<input type="button" class="x-next-page" style="margin-left:-4px" onclick="goNext(${pageIdx})" />
			<input type="button" class="x-last-page" style="margin-left:-2px" onclick="goLast()" />
			[当前第${pageIdx}页/共${pageNum}页][每页10条][共<%=maclist.length %>条]
		</div>
	</body>
</html>