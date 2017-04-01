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
String result = (String)request.getAttribute("result");


//检索结果
List beans = (ArrayList)request.getAttribute("resultList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<title>布局选取</title>
<script language="JavaScript" type="text/javascript">

	//确定
	function confirm() {
		var obj = document.getElementsByName("layout_item");
		var retObj = new Object();
		for (var i=0; i < obj.length; i++) {
			if(obj[i].checked) {
				var selectIndex = obj[i].value;
				retObj.layoutId = document.getElementById("layoutId"+selectIndex).value;
				retObj.layoutName = document.getElementById("layoutName"+selectIndex).value;
				retObj.layoutdescribe = document.getElementById("layoutdescribe"+selectIndex).value;
				retObj.layoutScreen = document.getElementById("layoutScreen"+selectIndex).value;
				retObj.layoutwidth = document.getElementById("layoutwidth"+selectIndex).value;
				retObj.layoutHeight = document.getElementById("layoutHeight"+selectIndex).value;
				retObj.windowsId = document.getElementById("windowsId"+selectIndex).value;
				break;
			}
		}
		if(retObj == null || retObj == ""){
			alert("请选择布局！");
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


</script>
</head>
<body>
<fieldset><legend>布局选取</legend>
	<div style="height: 250px; width: 350px; border: 1px solid #565656; overflow: scroll;">
	<table>
	<%
	if(beans != null) {
		for(int i=0; i<beans.size(); i++) {
			AdvertBean bean = (AdvertBean)beans.get(i);
			//布局ID
			String layoutId = bean.getLayoutId();
			//布局名称
			String layoutName = bean.getLayoutName();
			//布局类型
			String layoutScreen = bean.getLayoutScreen();
			//布局描述
			String layoutdescribe = bean.getLayoutDescribe();
			//分屏ID
			String windowId = bean.getWindowId();
			//宽
			int layoutwidth = bean.getLayoutwidth();
			//高
			int layoutHeight = bean.getLayoutHeight();
			//画面显示值
			String show = bean.getLayoutName();
			String layoutIdh = "layoutId" + i;
			String layoutNameh = "layoutName" + i;
			String layoutScreenh = "layoutScreen" + i;
			String layoutdescribeh = "layoutdescribe" + i;
			String layoutwidthh = "layoutwidth" + i;
			String layoutHeighth = "layoutHeight" + i;
			String windowIdh = "windowsId" + i;
	%>
			<tr><td>
				<input type="hidden" id="<%=layoutIdh%>" value="<%=layoutId%>">
				<input type="hidden" id="<%=layoutNameh%>" value="<%=layoutName%>">
				<input type="hidden" id="<%=layoutScreenh%>" value="<%=layoutScreen%>">
				<input type="hidden" id="<%=layoutdescribeh%>" value="<%=layoutdescribe%>">
				<input type="hidden" id="<%=layoutwidthh%>" value="<%=layoutwidth%>">
				<input type="hidden" id="<%=layoutHeighth%>" value="<%=layoutHeight%>">
				<input type="hidden" id="<%=windowIdh%>" value="<%=windowId%>">
				<input type="radio"" name="layout_item"  value="<%=i %>"/><%=show %>
			</td></tr>
	<%
		}
		
	}else {
	%>
		<tr><td>请先到布局管理模块创建布局</td></tr>
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