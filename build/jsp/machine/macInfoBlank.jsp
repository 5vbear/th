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
	</script>
	<body style="background-color: #fff;height: 100%">
		<div>
		    <div>
				<form name=myform action="" method=post>
					<table id="mytable" cellspacing="0">
						<tr>
							<th scope="col" colspan="2" class="machineTd">请选择要查看的端机</th>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机档案信息:</td>
						</tr>
						<tr>
							<td class="row" width="30%">设备类型:</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">出产日期</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">设备厂商</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">设备编号</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">保修年限</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机部署信息:</td>
						</tr>
						<tr>
							<td class="row">所属银行:</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">网点名称</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">网点地址</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">网点编号</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">保修年限</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">负责人</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">联系人</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">联系电话</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">联系手机</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">营业时间</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">营业周期</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">报停历史:</td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">迁移历史:</td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机配置信息:</td>
						</tr>
						<tr>
							<td class="row">开机时间</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">关机时间</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">屏幕保护时间</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">写保护目录</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">截屏结束时间</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">截屏间隔时间</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">应用服务器地址</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">FTP服务器IP</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机硬件信息:</td>
						</tr>
						<tr>
							<td class="row">CPU频率</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">操作系统</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">硬盘容量</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">内存大小</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">版本信息:</td>
						</tr>
						<tr>
							<td class="row">总版本号</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">浏览器信息:</td>
						</tr>
						<tr>
							<td class="row">浏览器名称:</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">浏览器版本</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">代理设置信息:</td>
						</tr>
						<tr>
							<td class="row">客户端代理地址</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">客户端代理端口</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机定位信息:</td>
						</tr>
						<tr>
							<td class="row" width="30%">定位时间</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">经度</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">纬度</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">范围</td>
							<td class="row"></td>
						</tr>
						<tr>
							<td class="row">定位点名称</td>
							<td class="row"></td>
						</tr>
					</table>
				</form>
	    	</div>
	  	</div>
	</body>
</html>