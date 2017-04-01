<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "ClientInfo.jsp";
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<meta http-equiv="content-script-type" content="text/javascript">
<meta http-equiv="content-style-type" content="text/css">
<meta name="copyright" content="Copyright(c) 天和. All Rights Reserved.">
<meta name="keywords" content="ClientRunning">
<meta name="description" content="端机信息">
<title>端机信息</title>
<link rel="stylesheet" type="text/css" href="./css/monitor.css">
<style type="text/css">
.STYLE1 {
color: #0033FF;
font-size: 14px;
}
.tdBorder {
border:0px #669900 solid;
border-right : double green;
border-bottom: double green;
border-top: double green;
border-left: double green;
font-size: 14px;
}
</style>
<script language="JavaScript">
var timerId;
var macID = "<%= machineID %>";
var currentTime = "";
function getStatus(){
	if(checkMac() == false){
		return;
	}
	var heartTime = <%=LocalProperties.getProperty("MONITOR_HEART_TIME")%> + 0;
	var waitTime = <%=LocalProperties.getProperty("MONITOR_WAIT_TIME")%> + 0;
	currentTime =new Date().format('yyyy-MM-dd hh:mm:ss');
	document.getElementById('start').disabled=true;
	document.getElementById('end').disabled=false;
	timerId=window.setInterval('refresh()',heartTime);
	startCommond();
	setTimeout('redirect(currentTime)',waitTime);
}

/**
 * 时间对象的格式化;
 */
Date.prototype.format = function(format) {
    /*
     * eg:format="YYYY-MM-dd hh:mm:ss";
     */
    var o = {
        "M+" :this.getMonth() + 1, // month
        "d+" :this.getDate(), // day
        "h+" :this.getHours(), // hour
        "m+" :this.getMinutes(), // minute
        "s+" :this.getSeconds(), // second
        "q+" :Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" :this.getMilliseconds()
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for ( var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}

function stop(){
	if(checkMac() == false){
		return;
	}
	
	document.getElementById('start').disabled=false;
	document.getElementById('end').disabled=true;
	window.clearInterval(timerId);
	//设定心跳
	xmlHttp = this.getXmlHttpRequest();
	if(xmlHttp == null) {
	    alert("Explore is Unsupport XmlHttpRequest！");
	    return;
	}

	var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + macID + "&funcId=4";
	xmlHttp.open("GET", url, true);
	xmlHttp.onreadystatechange = this.callStopBack;
	xmlHttp.send(null);
}

function leavePage(){
	if(macID != ""){
		//设定心跳
		xmlHttp = this.getXmlHttpRequest();
		if(xmlHttp == null) {
		    alert("Explore is Unsupport XmlHttpRequest！");
		    return;
		}

		var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + macID + "&funcId=4";
		xmlHttp.open("GET", url, true);
		xmlHttp.onreadystatechange = this.callStopBack;
		xmlHttp.send(null);
	}
}
function refresh(){
	redirect(currentTime);
}

function startCommond(){
	xmlHttp = this.getXmlHttpRequest();
	if(xmlHttp == null) {
	    alert("Explore is Unsupport XmlHttpRequest！");
	    return;
	}

	var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + macID + "&funcId=5";
	xmlHttp.open("GET", url, true);
	xmlHttp.send(null);
}

function redirect(currentTime){
	xmlHttp = this.getXmlHttpRequest();
	if(xmlHttp == null) {
	    alert("Explore is Unsupport XmlHttpRequest！");
	    return;
	}

	var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + macID + "&currentTime="+currentTime + "&funcId=2";
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
        var res = xmlHttp.responseText;
        if(res != ""){
        	var attributes = res.split("|");
        	
        	document.getElementById("cpuCurrent").innerHTML = attributes[0];
        	document.getElementById("memoryCurrent").innerHTML = attributes[1];
        	document.getElementById("uploadRate").innerHTML = attributes[2];
        	document.getElementById("downloadRate").innerHTML = attributes[3];
        	document.getElementById("pieTarget").innerHTML = attributes[4];
        	document.getElementById("cpuTarget").innerHTML = attributes[5];
        	document.getElementById("memoryTarget").innerHTML = attributes[6];
        	document.getElementById("uploadTarget").innerHTML = attributes[7];
        	document.getElementById("downloadTarget").innerHTML = attributes[8];
        } else {
        	stop();
        	alert("暂无该端机系统信息");
        }
    }
}

function callStopBack() {
    if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
        if("" != xmlHttp.responseText){
        	alert(xmlHttp.responseText);
        }
    }
}

function getProcess(){
	if(checkMac() == false){
		return;
	}
	window.document.form_data.orderType.value ="2";
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_PROCESS_ID %>";
	window.document.form_data.machineID.value =macID;
    window.document.form_data.action = "<%= Define.MONITOR_SERVLET %>";
    window.document.form_data.target = "_self";
    window.document.form_data.submit();
}
function getService(){
	if(checkMac() == false){
		return;
	}
	window.document.form_data.orderType.value ="2";
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_SERVICE_ID %>";
	window.document.form_data.machineID.value =macID;
    window.document.form_data.action = "<%= Define.MONITOR_SERVLET %>";
    window.document.form_data.target = "_self";
    window.document.form_data.submit();
}
function checkMac(){
	if("" == macID){
		alert("请选择端机");
		return false;
	}
}
</script>
</head>
<body class="panel" topmargin="0" leftmargin="2" onunload="leavePage();">
<form method="POST" name="form_data" action="<%= Define.MONITOR_SERVLET %>">
<input type="hidden" name="orderType"   value="2">
<input type="hidden" name="pageID"   value="">
<input type="hidden" name="machineID"   value="<%= machineID %>">
<BR>
<div style="HEIGHT: 25px;margin-left:50px;">
        <input type="button" name="start" id="start" value="获取状态" class="leftBtn" onclick="javascript:getStatus();"/>
        <input type="button" name="end" disabled='disabled' id="end" value=" 停止获取" class="leftBtn" onclick="javascript:stop();"/>
        <input type="button" name="getProcess1" value="获取进程" class="leftBtn" onclick="javascript:getProcess();"/>
        <input type="button" name="getService1" value="获取服务" class="leftBtn" onclick="javascript:getService();"/>
      </div>
<table width="580" height="465" border="0" align="center" cellpadding="0" cellspacing="1">
  <tr>
    <td height="25" colspan="2">
      <label></label>
      
    </td>
  </tr>
  <tr>
    <td height="25" colspan="2"><div align="left" class="STYLE1">CPU占用率(当前:<span id="cpuCurrent"></span>%)</td>
  </tr>
  <tr>
    <td height="75" colspan="2" class="tdBorder" id="cpuTarget"></td>
  </tr>
  <tr>
    <td height="25" colspan="2"><div align="left"><span class="STYLE1">内存占用率(当前:<span id="memoryCurrent"></span>%)</span></div></td>
  </tr>
  <tr>
    <td height="75" colspan="2" class="tdBorder" id="memoryTarget"></td>
  </tr>
  <tr>
    <td width="204" height="25"><div align="left"><span class="STYLE1">硬盘使用率&nbsp;<img src="./image/hardRed.jpg"/>已使用<img src="./image/hardGreen.jpg"/>未使用</span> </div></td>
    <td width="383" height="25"><div align="left"><span class="STYLE1">上行速率(当前:<span id="uploadRate"></span>kb/秒)</span></div></td>
  </tr>
  <tr>
    <td rowspan="3" class="tdBorder" id="pieTarget"></td>
    <td height="75" class="tdBorder" id="uploadTarget"></td>
  </tr>
  <tr>
    <td height="25"><div align="left"><span class="STYLE1">下行速率(当前:<span id="downloadRate"></span>kb/秒)</span></div></td>
  </tr>
  <tr>
    <td height="75" class="tdBorder" id="downloadTarget"></td>
  </tr>
</table>
</form>
</body>

</html>
<% logger.info( jspName + " : end"); %>
