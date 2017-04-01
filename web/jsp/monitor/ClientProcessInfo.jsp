<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="th.com.util.Define" %>
<%@page import="java.util.ArrayList"%>
<%@page import="th.entity.MachineProcessBean"%>
<%@page import="java.util.List"%>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
	Log logger = LogFactory.getLog(this.getClass());
    String jspName = "ClientProcessInfo.jsp";
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
    
    String machineID = request.getParameter("machineID");
    logger.info("machineID = " + machineID);
    
    String strContextPath = request.getContextPath();
    String url = strContextPath + "/MonitorServlet.html";
    String defaultStyle = strContextPath + "/css/advert.css";
    
    //检索结果
    List resultBeans = (ArrayList)request.getAttribute("resultList");
    //检索条件
    MachineProcessBean selectBean = (MachineProcessBean)request.getAttribute("select_object");
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
	function shutdownProcess(machineID, processName){
		xmlHttp = this.getXmlHttpRequest();
		if(xmlHttp == null) {
		    alert("Explore is Unsupport XmlHttpRequest！");
		    return;
		}

		var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + machineID + "&processName=" + encodeURI(processName) + "&funcId=3";
		xmlHttp.open("GET", url, true);
		xmlHttp.onreadystatechange = this.callBack;
		xmlHttp.send(null);
	}

	function getXmlHttpRequest() {
	    try {
	        // Firefox, Opera 8.0+, Safari
	        xmlHttp = new XMLHttpRequest();
	    } catch (e) {
	        // Internet Explorer
	        try {
	            xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
	        } catch (e) {
	            try {
	                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	            } catch (e) {
	                alert("XMLHTTP is null");
	                return false;
	            }
	        }
	    }

	    return  xmlHttp; 
	}

	function callBack() {
	    if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
	    	alert(xmlHttp.responseText);
	    }
	}

	function back(){
		window.document.form_data.orderType.value ="2";
		window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_ID %>";
		window.document.form_data.machineID.value ="<%= machineID %>";
	    window.document.form_data.action = "<%= Define.MONITOR_SERVLET %>";
	    window.document.form_data.target = "_self";
	    window.document.form_data.submit();
	}

	//翻页
	function pageTurn (page) {
		if (page <= 0 || page > <%=page_total%> || <%=total_num%> == 0) {
			return;
		}
		
		window.document.form_data.point_num.value = page;
		window.document.form_data.orderType.value ="2";
		window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_PROCESS_ID %>";
		window.document.form_data.machineID.value ="<%= machineID %>";
	    window.document.form_data.target = "_self";
	    window.document.form_data.submit();
	}
</script>
<title>端机进程信息</title>
</head>
<body>
	<form method="POST" name="form_data" action="<%= Define.MONITOR_SERVLET %>">
		<input type="hidden" name="orderType"   value="2">
		<input type="hidden" name="pageID"   value="">
		<input type="hidden" name="machineID"   value="<%= machineID %>">
		<input type="hidden" name="point_num" value="<%=point_num %>">
		<div style="HEIGHT: 12px;margin-left:50px;">
			<input type="submit" name="returnBack" class="leftBtn" value="返回" onclick="javascript:back();"/>
		</div>
		<p></p>
			<div>
				<table id="dataTableId">
					<tr>
						
						<th style="width: 15%" class="th_tianhe">操作</th>
						<th style="width: 50%" class="th_tianhe">进程名</th>
						<th style="width: 35%" class="th_tianhe">进程类型</th>
						<th style="width: 10%" class="th_tianhe">进程ID</th>
						
					</tr>
					<% 
					if (resultBeans != null) {
						for (int i = 0; i < resultBeans.size(); i++) {
							MachineProcessBean bean = (MachineProcessBean)resultBeans.get(i);
							//进程名称
							String pName = bean.getName();
							//进程类型
							String pType = bean.getType();
							//进程ID
							String pID = bean.getId();
					%>		
					<tr>
						<td class="td_tianhe"><input type="button" value="结束进程" class="leftBtn" onclick="shutdownProcess('<%=machineID%>','<%=pName%>')"/></td>
						<td class="td_tianhe"><%=pName %></td>
						<td class="td_tianhe"><%=pType %></td>
						<td class="td_tianhe"><%=pID %></td>
						
					</tr>
					<%		
						}
					}
					%>
					
					
				</table>
			</div>
			<div>
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
</body>
</html>
<% logger.info( jspName + " : end"); %>