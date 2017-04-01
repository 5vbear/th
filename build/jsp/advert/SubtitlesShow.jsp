<%@page import="th.entity.AdvertBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
//文字样式
String wordStyle = "";
//滚动延迟
String rollDelay = "";
//滚动文字
String rollWord = "";

//检索结果
AdvertBean resultBeans = (AdvertBean)request.getAttribute("resultBean");
if (resultBeans != null) {
	//文字样式
	wordStyle = resultBeans.getWord_style();
	//滚动延迟
	rollDelay = resultBeans.getRoll_delay();
	//滚动文字
	rollWord = resultBeans.getRoll_word();
}
String prePage = (String)request.getAttribute("prePage");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="/th/css/advert.css" />
<title>字幕</title>
</head>
<body>
	<div style="<%=wordStyle%>">
		<MARQUEE scrolldelay="<%=rollDelay%>"><%=rollWord%></MARQUEE>
	</div>
	<%if(prePage != null && !"".equals(prePage)){ %>
	<div>
	<input type="button" class="rightBtn" value="返回" onclick="history.back();"/>
	</div>
	<%} %>
</body>
</html>