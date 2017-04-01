<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%> 
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macInfoView.jsp";
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
		<link rel="stylesheet" type="text/css" href="./css/channel.css">
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/style.css"/>
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
		</script>
		<title>端机信息</title>
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
	<%!
		private String parseStr(Object obj){
			if(obj != null){									
				return obj.toString();
			}else{
				return "";
			}
		}
	%>
	<% 
		HashMap[] detaillist = (HashMap[]) request.getAttribute( "detaillist" );
		HashMap info = detaillist[0];

	%>
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


		function getXmlHttpRequest() {
		    try {
		        // Firefox, Opera 8.0+, Safari
		        var xmlHttp = new XMLHttpRequest();
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
		        var res = xmlHttp.responseText;
		        if(res != ""){
		        	var attributes = res.split("&");
		        	alert(attributes[0]);
		        	if(attributes[1] == 1 || attributes[1] =='1'){
		        		document.getElementById('macstatus').innerHTML = attributes[2];
		        	}
		        } else {
		        	alert("指令执行出现异常,请联系管理员！");
		        }
		    }
		}
	</script>
	<body style="background-color: #fff;height: 100%">
		<div>
		    <div>
				<form name=myform action="" method=post>
					<table id="mytable" cellspacing="0">
						<tr>
							<th scope="col" colspan="2" class="machineTd"><% out.print(parseStr(info.get("MACHINE_MARK")));  %></th>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机档案信息:</td>
						</tr>
						<tr>
							<td class="row" width="30%">告警说明</td>
							<td class="row"><% out.print(info.get("ADESC"));  %></td>
						</tr>
						<tr>
							<td class="row">告警类别</td>
							<td class="row"><% out.print(parseStr(info.get("ALERT_NAME")));  %></td>
						</tr>
						<tr>
							<td class="row">告警时间</td>
							<td class="row"><% out.print(parseStr(info.get("ATIME")));  %></td>
						</tr>
						<tr>
							<td class="row">操作</td>
							<td class="row"><% out.print(parseStr(info.get("ASTATUS")));  %></td>
						</tr>
						<tr>
							<td class="row">端机名称</td>
							<td class="row"><% out.print(parseStr(info.get("MACHINE_NAME"))); %></td>
						</tr>
						<tr>
							<td class="row">MAC地址</td>
							<td class="row"><% out.print(parseStr(info.get("MAC"))); %></td>
						</tr>
						<tr>
							<td class="row">端机IP</td>
							<td class="row"><% out.print(parseStr(info.get("IP"))); %></td>
						</tr>
						<tr>
							<td class="row">网点名称</td>
							<td class="row"><% out.print(parseStr(info.get("BRANCH_NAME"))); %></td>
						</tr>
						<tr>
							<td class="row">网点地址</td>
							<td class="row"><% out.print(parseStr(info.get("BRANCH_ADDRESS"))); %></td>
						</tr>
						<tr>
							<td class="row">负责人</td>
							<td class="row"><% out.print(parseStr(info.get("MANAGER_NAME"))); %></td>
						</tr>
						<tr>
							<td class="row">联系人</td>
							<td class="row"><% out.print(parseStr(info.get("CONTACT_NAME"))); %></td>
						</tr>
						<tr>
							<td class="row">创建人ID</td>
							<td class="row"><% out.print(parseStr(info.get("OPERATOR"))); %></td>
						</tr>
						
					</table>
				</form>
	    	</div>
	  	</div>
	</body>
</html>