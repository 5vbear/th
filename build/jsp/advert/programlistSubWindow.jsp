<%@page import="th.entity.AdvertBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%> 
<%
//检索结果
List programList = (ArrayList)request.getAttribute("resultList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script language="javascript" type="text/javascript" >

function CloseWind(){

	var obj = document.getElementsByName("programlist_item");
	var retObj = new Array();
	var index = 0;
	for (var i=0; i < obj.length; i++) {
		if(obj[i].checked) {
			retObj[index] = new Object();
			retObj[index].id = document.getElementById("programlistId"+i ).value;
			retObj[index].name = document.getElementById("programlistName"+i ).value;
			retObj[index].describe = document.getElementById("describe"+i ).value;
			retObj[index].playTime = document.getElementById("playTime"+i ).value;
			retObj[index].layoutScreen = document.getElementById("layoutScreen"+i).value;
			index++;
		}
	}

	if(retObj.length == 0){
		alert("请选择节目单！");
		return;
	}
	
	window.returnValue=retObj;
	window.close();

    }

//取消
function cancel() {
	window.close();
}


</script> 

</head>
<body>

<div>
	<fieldset><legend>节目单选取</legend>
	<div style="height: 250px; width: 380px; border: 1px solid #565656; overflow: auto;">
	<table>
	<%
	if (programList != null) {
		for(int i=0; i<programList.size(); i++) {
			AdvertBean bean = (AdvertBean)programList.get(i);
			String programlistId = bean.getProgramlistId();
			String programlistName = bean.getProgramlistName();
			String describe = bean.getProgramlistDescribe();
			int playTime = bean.getProgramlistPlayTime();
			String layoutScreen = bean.getLayoutScreen();
			String programlistIdh = "programlistId" + i;
			String programlistNameh = "programlistName" + i;
			String describeh = "describe" + i;
			String playTimeh = "playTime" + i;
			String layoutScreenh = "layoutScreen" + i;
	%>
			<tr><td>
				<input type="hidden" id="<%=programlistIdh%>" value="<%=programlistId%>">
				<input type="hidden" id="<%=programlistNameh%>" value="<%=programlistName%>">
				<input type="hidden" id="<%=describeh%>" value="<%=describe%>">
				<input type="hidden" id="<%=playTimeh%>" value="<%=playTime%>">
				<input type="hidden" id="<%=layoutScreenh%>" value="<%=layoutScreen%>">
				<input type="checkbox"" name="programlist_item"  value=""/><%=programlistName%>
			</td></tr>
	<%
		}
	}	
	%>
		

	</table>
	</div>


</fieldset>
	<input type="button" name="btn_Close" id="btn_Close" value="确定" onclick="CloseWind();"/>
	<input type="button" name="btn_Cancel" id="btn_Cancel" value="取消" onclick="cancel();"/>
</div>
</body>
</html>