<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="th.com.util.Define" %>
<%@page import="java.util.ArrayList"%>
<%@page import="th.entity.GuestBookBean"%>
<%@page import="java.util.List"%>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
	Log logger = LogFactory.getLog(this.getClass());
    String jspName = "AppStoreInfo.jsp";
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
    
    String strContextPath = request.getContextPath();
    String url = strContextPath + "/MonitorServlet.html";
    String defaultStyle = strContextPath + "/css/advert.css";
    
    //检索结果
    List resultBeans = (ArrayList)request.getAttribute("resultList");
    //检索条件
    GuestBookBean selectBean = (GuestBookBean)request.getAttribute("select_object");
    //检索结果总数
    int total_num = 0;
    //当前页数
    int point_num = 1;
    //Table总页数
    int page_total = 1;
    //每页显示行数
    int page_view = Define.VIEW_NUM;
    
    if (selectBean != null) {
    	//检索结果总数
    	total_num = selectBean.getTotal_num();
    	//当前页数
    	point_num = selectBean.getPoint_num();
    	//Table总页数
    	if (total_num != 0) {
    		if(total_num % page_view == 0){
    			page_total = total_num / page_view;
    		} else {
    			page_total = total_num / page_view + 1;
    		}
    	}
    	
    }
    String result = (String)request.getAttribute("result");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<link type="text/css" rel="stylesheet" href="./css/login.css" media="all">
<link rel="stylesheet" type="text/css" href="./css/monitor.css">
<script type="text/javascript">
	window.onload = function showtable() {
		var tablename = document.getElementById("dataTableId");
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

	function back(){
		window.document.form_data.action = "/th/jsp/mobile/mobileMain.jsp";
		window.document.form_data.target = "_self";
	    window.document.form_data.submit();
	}

	//翻页
	function pageTurn (page) {
		if (page <= 0 || page > <%=page_total%> || <%=total_num%> == 0) {
			return;
		}
		
		window.document.form_data.point_num.value = page;
		window.document.form_data.pageID.value ="3";
	    window.document.form_data.target = "_self";
	    window.document.form_data.submit();
	}
</script>
<title>意见簿</title>
</head>
<body>
<div id="box">
	<form method="POST" name="form_data" action="/th/mobile.html">
		<input type="hidden" name="orderType"   value="2">
		<input type="hidden" name="pageID"   value="">
		<input type="hidden" name="point_num" value="<%=point_num %>">
		<div align="left" style="HEIGHT: 20px; margin-left:20% ">
			<input type="submit" name="returnBack" value="返回" onclick="javascript:back();"/>
		</div>
		<p></p>
			<div style="OVERFLOW-Y:auto;height:425px;margin-left:20px;">
				<table id="dataTableId" width="800px" height="420px" border="0" cellspacing="1" cellpadding="3" align="center">
					<tr>
						<th style="width: 10%" class="th_tianhe">种类</th>
						<th style="width: 10%" class="th_tianhe">机器名</th>
						<th style="width: 15%" class="th_tianhe">组织名</th>
						<th style="width: 25%" class="th_tianhe">内容</th>
						<th style="width: 20%" class="th_tianhe">发出时间</th>
					</tr>
					<% 
					if (resultBeans != null) {
						for (int i = 0; i < resultBeans.size(); i++) {
							GuestBookBean bean = (GuestBookBean)resultBeans.get(i);
							String orgName = bean.getOrgName();
							String macMark = bean.getMachineMark();
							String iType = bean.getIdeaType();
							String iContent = bean.getIdeaContent();
							String time = bean.getOperateTime();
					%>		
					<tr>
						<td class="td_ss"><%=iType %></td>
						<td class="td_ss"><%=macMark %></td>
						<td class="td_ss"><%=orgName %></td>
						<td class="td_ss"><%=iContent %></td>
						<td class="td_ss"><%=time %></td>
					</tr>
					<%		
						}
						if(resultBeans.size() < 10){
							for(int j = 10-resultBeans.size(); j>0;j--){
					%>
					<tr>
						<td class="td_ss"/>
						<td class="td_ss"/>
						<td class="td_ss"/>
						<td class="td_ss"/>
						<td class="td_ss"/>
					</tr>
					<%	
							}
						}
					}
					%>
				</table>
			</div>
			<div style="HEIGHT: 20px;float:left;margin-left:20%">
			<table height="20">
				<tr>
					<td align="left">
						<input type="button" class="first_page" style="margin-left:5px" onclick="pageTurn(1)"/> 
						<input type="button" class="previous_page" style="margin-left:-2px" onclick="pageTurn(<%=point_num-1 %>)"/> 
						<input type="button" class="next_page" style="margin-left:-4px" onclick="pageTurn(<%=point_num+1 %>)"/>
						<input type="button" class="last_page" style="margin-left:-2px" onclick="pageTurn(<%=page_total %>)" />[当前第<%=point_num%>页/共<%=page_total%>页]
					</td>
				</tr>
			</table>
			</div>
	</form>
	</div>
</body>
</html>
<% logger.info( jspName + " : end"); %>