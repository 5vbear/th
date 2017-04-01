<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.user.User"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="th.com.util.Define"%>
<%@page import="th.util.DateUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	Log logger = LogFactory.getLog(this.getClass());
	//response.setContentType("text/html; charset=utf-8");
	String strContextPath = request.getContextPath();
	String defaultStyle = strContextPath + "/css/lottery.css";
	String awardID = (String) request.getAttribute( "awardID" );
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<style>
</style>
<title>奖品发送</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script language="JavaScript" type="text/javascript">
function save(){
	var userName = document.getElementById("username").value;
	var phone = document.getElementById("phone").value;
	var address = document.getElementById("address").value;
	var zipCode = document.getElementById("zip_code").value;
	if(userName==''){
		alert("请输入姓名");
		return;
	}
	if(phone==''){
		alert("请输入电话");
		return;
	}
	if(address==''){
		alert("请输入地址");
		return;
	}
	if(zipCode==''){
		alert("请输入邮编");
		return;
	}
	
	xmlHttp = this.getXmlHttpRequest();
	if(xmlHttp == null) {
	    alert("Explore is Unsupport XmlHttpRequest！");
	    return;
	}
	var url = "/th/lottery_history.html?&functionID=2&awardID=<%=awardID%>&userName="+userName+"&phone="+phone+"&address="+address+"&zipCode="+zipCode;
	url = encodeURI(url);
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
        if("1"==xmlHttp.responseText){
        	alert("信息已保存");
        }else{
        	alert("信息保存出错,请联系工作人员处理");
        }
        window.close();
    }
}
</script>
</head>
<body>
<fieldset><legend>奖品发送</legend>
<p>恭喜您中奖，请输入奖品寄送相关信息:</p>
	<form name="award_form" action="" method="post" >
		</br>
		<div id="material1" class="firstDiv" style="display: block;border-style: solid ;border-width: 1px;">
			<table class="templeTable">
				<tr>
					<th class="th_tianhe">姓&nbsp;&nbsp;名：</th>
					<td>
					  <input maxlength="12" type="text" id="username" style="width:300px;">
					</td>
					<th class="th_tianhe">电&nbsp;&nbsp;话：</th>
					<td><input maxlength="30" type="text" id="phone" style="width:300px;"></td>
				</tr>
				<tr>
					<th class="th_tianhe">地&nbsp;&nbsp;址：</th>
					<td colspan=3 ><input maxlength="128" type="text" style="width: 690px;" id="address"></td>
				</tr>
				<tr>
					<th class="th_tianhe">邮&nbsp;&nbsp;编：</th>
					<td><input maxlength="6" type="text" style="width:300px;" id="zip_code"></td>
				</tr>
				
			</table>
		</div>
		<div id="addBtn" style="display:block;">
			<table>
			<tr>
				<td><input id="saveAwardBtn" type="button" class="rightBtn" value="提交" onclick="save()"></td>
			</tr>
			</table>
		</div>
	</form>
</fieldset>
</body>
</html>