<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="th.entity.AdvertBean"%>
<%@page import="th.com.util.Define"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String strContextPath = request.getContextPath();
String url = strContextPath + "/AdvertServlet";
String defaultStyle = strContextPath + "/css/advert.css";
request.setCharacterEncoding("UTF-8");

//检索结果
List beans = (ArrayList)request.getAttribute("resultList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<title>素材选择</title>
<script language="JavaScript" type="text/javascript">

	//确定
	function confirm() {
		var obj = document.getElementsByName("material_item");
		var retArr = new Array();
		var index = 0;
		for (var i=0; i < obj.length; i++) {
			if(obj[i].checked) {
				retArr[index] = new Object();
				retArr[index].id = document.getElementById("materialId"+i).value;
				retArr[index].name = document.getElementById("materialName"+i).value;
				retArr[index].file = document.getElementById("materiaFilelName"+i).value;
				retArr[index].windowNo = "";
				retArr[index].materialLevel = "";
				retArr[index].playTime = "";
				retArr[index].materiaLink = document.getElementById("materiaLink"+i).value;
				index++;
			}
		}
		if(retArr.length == 0){
			alert("请选择素材！");
			return;
		}
		window.returnValue=retArr;
		window.close();
	}

	//取消
	function cancel() {
		window.returnValue="";
		window.close();
	}


</script>
</head>
<body>
<fieldset><legend>素材选取</legend>
	<div style="height: 250px; width: 350px;border: 1px solid #565656; overflow: scroll;">
	<table>
	<%
	if(beans != null) {
		for(int i=0; i<beans.size(); i++) {
			AdvertBean bean = (AdvertBean)beans.get(i);
			//素材ID
			String materialId = bean.getMaterialId();
			//素材名
			String materialName = bean.getMaterialName();
			//素材文件名
			String materiaFilelName = bean.getMaterialFilelName();
			String show = bean.getMaterialName();
			//素材链接
			String materiaLink = bean.getMaterialLink();
			
			String materialId_h = "materialId" + i;
			String materialName_h = "materialName" + i;
			String materiaFilelName_h = "materiaFilelName" + i;
			String materiaLink_h = "materiaLink" + i;
	%>
			<tr><td>
				<input type="hidden" id="<%=materialId_h%>" value="<%=materialId%>">
				<input type="hidden" id="<%=materialName_h%>" value="<%=materialName%>">
				<input type="hidden" id="<%=materiaFilelName_h%>" value="<%=materiaFilelName%>">
				<input type="hidden" id="<%=materiaLink_h%>" value="<%=materiaLink%>">
				<input type="checkbox"" name="material_item" value="<%=i %>"/><%=show %>
			</td></tr>
	<%
		}
		
	}else {
	%>
		<tr><td>请先创建素材</td></tr>
	<%
	}
	%>
	
	</table>
	</div>
	<div>
		<input type="button" value="确定" onclick="confirm()">
		<input type="button" value="取消" onclick="cancel()">
	</div>

</fieldset>
</body>
</html>