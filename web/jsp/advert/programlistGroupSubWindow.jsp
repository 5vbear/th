<%@page import="th.entity.AdvertBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%> 
<%
//检索结果
List groupList = (ArrayList)request.getAttribute("resultGroupList");
List programList = (ArrayList)request.getAttribute("resultProgramList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>节目单选取</title>
<script language="javascript" type="text/javascript" >

function CloseWind(){
	var retObj = new Object();
	if(document.getElementById("group").checked){
		var obj = document.getElementsByName("group_item");
		for (var i=0; i < obj.length; i++) {
			if(obj[i].checked) {
				retObj.type = "1";
				retObj.id = document.getElementById("programlistGroupId"+i).value;
				retObj.name = document.getElementById("programlistGroupName"+i).value;
				break;
			}
		}
	}else{
		var obj = document.getElementsByName("programlist_item");
		for (var i=0; i < obj.length; i++) {
			if(obj[i].checked) {
				retObj.type = "2";
				retObj.id = document.getElementById("programlistId"+i).value;
				retObj.name = document.getElementById("programlistName"+i).value;
				retObj.describe = document.getElementById("describe"+i).value;
				retObj.layoutScreen = document.getElementById("layoutScreen"+i).value;
				retObj.playTime = document.getElementById("playTime"+i).value;
				break;
			}
		}
	}

	if(retObj == ""){
		alert("请选择节目单！");
		return;
	}
	
	window.returnValue=retObj;
	window.close();
}

//取消
function cancel() {
	window.returnValue="";
	window.close();
}

function change(id){
	if(id == 1){
		document.getElementById("groupDiv").style.display = "block";
		document.getElementById("programlistDiv").style.display = "none";
	}else{
		document.getElementById("groupDiv").style.display = "none";
		document.getElementById("programlistDiv").style.display = "block";
	}
}
</script> 

</head>
<body>

<div>
	<fieldset><legend>节目单选取</legend>
	<table>
		<tr>
			<td>
				<input type="radio" name="item" id="group" checked="checked" onclick="change(1)">节目单组
				<input type="radio" name="item" id="programlist" onclick="change(2)">节目单
			</td>
		</tr>
	</table>
	<div id="groupDiv" style="height: 280px; width: 380px;border: 1px solid #565656; overflow: auto;">
	<table>
	<%
		for(int i=0; i<groupList.size(); i++) {
			AdvertBean bean = (AdvertBean)groupList.get(i);
			//节目单组ID
			String programlistGroupId = bean.getProgramlistGroupId();
			//节目单组名称
			String programlistGroupName = bean.getProgramlistGroupName();
			String programlistGroupIdh = "programlistGroupId" + i;
			String programlistGroupNameh = "programlistGroupName" + i;
	%>
			<tr><td>
				<input type="hidden" id="<%=programlistGroupIdh %>" value="<%=programlistGroupId %>">
				<input type="hidden" id="<%=programlistGroupNameh %>" value="<%=programlistGroupName %>">
				<input type="radio"" name="group_item" /><%=programlistGroupName %>
			</td></tr>
	<%
		}
	%>
	</table>
	</div>
	<div id="programlistDiv" style="height: 280px; width: 380px;border: 1px solid #565656; overflow: auto; display: none;">
	<table>
	<%
		for(int i=0; i<programList.size(); i++) {
			AdvertBean bean = (AdvertBean)programList.get(i);
			//节目单ID
			String programlistId = bean.getProgramlistId();
			//节目单名称
			String programlistName = bean.getProgramlistName();
			//节目单名称
			String describe = bean.getProgramlistDescribe();
			//布局类型
			String layoutScreen = bean.getLayoutScreen();
			//节目单播放时长
			int playTime = bean.getProgramlistPlayTime();
			String programlistIdh = "programlistId" + i;
			String programlistNameh = "programlistName" + i;
			String describeh = "describe" + i;
			String layoutScreenh = "layoutScreen" + i;
			String playTimeh = "playTime" + i;
	%>
			<tr><td>
				<input type="hidden" id="<%=programlistIdh %>" value="<%=programlistId %>">
				<input type="hidden" id="<%=programlistNameh %>" value="<%=programlistName %>">
				<input type="hidden" id="<%=describeh %>" value="<%=describe %>">
				<input type="hidden" id="<%=layoutScreenh %>" value="<%=layoutScreen %>">
				<input type="hidden" id="<%=playTimeh %>" value="<%=playTime %>">
				<input type="radio"" name="programlist_item" /><%=programlistName %>
			</td></tr>
	<%
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