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
<title>字幕选取</title>
<script language="JavaScript" type="text/javascript">

	//确定
	function confirm() {
		var obj = document.getElementsByName("subtitles_item");
		var retObj = new Object();
		for (var i=0; i < obj.length; i++) {
			if(obj[i].checked) {
				var selectIndex = obj[i].value;
				retObj.subtitlesId = document.getElementById("subtitlesId"+selectIndex).value;
				retObj.subtitlesName = document.getElementById("subtitlesName"+selectIndex).value;
				break;
			}
		}
		if(retObj == null || retObj == ""){
			alert("请选择字幕！");
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
<fieldset><legend>字幕选取</legend>
	<div style="height: 310px; width: 380px; border: 1px solid #565656; overflow: scroll;">
	<table>
	<%
	if(beans != null) {
		for(int i=0; i<beans.size(); i++) {
			AdvertBean bean = (AdvertBean)beans.get(i);
			//字幕ID
			String subtitlesId = bean.getSubtitlesId();
			//字幕名称
			String subtitlesName = bean.getSubtitles_name();
			//画面显示值
			String show = bean.getSubtitles_name();
			String subtitlesIdh = "subtitlesId" + i;
			String subtitlesNameh = "subtitlesName" + i;
	%>
			<tr><td>
				<input type="hidden" id="<%=subtitlesIdh%>" value="<%=subtitlesId%>">
				<input type="hidden" id="<%=subtitlesNameh%>" value="<%=subtitlesName%>">
				<input type="radio"" name="subtitles_item"  value="<%=i %>"/><%=show %>
			</td></tr>
	<%
		}
		
	}else {
	%>
		<tr><td>请先到素材管理模块创建字幕</td></tr>
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