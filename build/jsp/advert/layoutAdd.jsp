<%@page import="th.user.User"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="th.com.util.Define"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
Log logger = LogFactory.getLog(this.getClass());
User user = (User) session.getAttribute("user_info");
String realname =null;
if (user == null) {
     response.setContentType("text/html; charset=utf-8");
     response.sendRedirect("/th/index.jsp");
} else {
    realname = user.getReal_name();
    logger.info("获得当前用户的用户名，用户名是： " + realname);
}

String strContextPath = request.getContextPath();
String url = strContextPath + "/AdvertServlet";
String defaultStyle = strContextPath + "/css/advert.css";
request.setCharacterEncoding("UTF-8");
String result = (String)request.getAttribute("result");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<title>广告管理-布局添加</title>
<script language="JavaScript" type="text/javascript">
	var numType = new RegExp(/^[0-9]*$/);
	//页面初始化
	function onload() {
		var message = "<%=result%>";
		if (message == null || message == "" || message == "null") {
			return;
		}
		alert(message);
	}
	
	//返回检索画面
	function returnSearch() {
		window.document.layout_form.pageId.value = "<%=Define.JSP_LAYOUT_SEARCH_ID%>";
		window.document.layout_form.funcId.value = "";
		window.document.layout_form.submit();
	}

	//保存
	function confirm() {
		// check
		//布局名称
		if (document.getElementById("layout_name").value == "") {
			alert("布局名称不能为空！");
			return;
		}
		//布局类型
		if (document.getElementById("layout_screen").value == "") {
			alert("布局类型不能为空！");
			return;
		}
		//场景大小
		var newWidth = document.getElementById("layout_width").value;
		var newHeight = document.getElementById("layout_height").value;
		if(newWidth =="" || newHeight == ""){
			alert("场景大小不能为空");
			return;
		}
		if(newWidth.match(numType) == null || newHeight.match(numType) == null){
			alert("场景大小必须为数值类型");
			return;
		}
		var coordinate;
		var layout_screen = document.getElementById("layout_screen").value;
		if(layout_screen == "1"){
			//坐标1
			coordinate = "0,0,"+newWidth+","+newHeight+";";
		}else if(layout_screen == "2"){
			//坐标1
			coordinate = "0,0,"+Math.ceil(newWidth/2)+","+newHeight+";";
			//坐标2
			coordinate += Math.ceil(newWidth/2)+",0,"+newWidth+","+newHeight+";";
		}else if(layout_screen == "4"){
			//坐标1
			coordinate = "0,0,"+Math.ceil(newWidth/2)+","+Math.ceil(newHeight/2)+";";
			//坐标2
			coordinate += Math.ceil(newWidth/2)+",0,"+newWidth+","+Math.ceil(newHeight/2)+";";
			//坐标3
			coordinate += "0,"+Math.ceil(newHeight/2)+","+Math.ceil(newWidth/2)+","+newHeight+";";
			//坐标4
			coordinate += Math.ceil(newWidth/2)+","+Math.ceil(newHeight/2)+","+newWidth+","+newHeight+";";
		}else{
			alert("布局类型不正确,请重新选取。");
			return;
		}
		window.document.layout_form.coordinate.value = coordinate;
		window.document.layout_form.pageId.value = "<%=Define.JSP_LAYOUT_ADD_ID%>";
		window.document.layout_form.funcId.value = "<%=Define.FUNC_LAYOUT_ADD_ID%>";
		window.document.layout_form.submit();
	}

	//改变布局类型
	function changeScreen(id){
		if(id == 1){
			document.getElementById("screen1").style.display = "";
			document.getElementById("screen2").style.display = "none";
			document.getElementById("screen4").style.display = "none";
		}else if(id == 2){
			document.getElementById("screen1").style.display = "none";
			document.getElementById("screen2").style.display = "";
			document.getElementById("screen4").style.display = "none";
		}else if(id == 4){
			document.getElementById("screen1").style.display = "none";
			document.getElementById("screen2").style.display = "none";
			document.getElementById("screen4").style.display = "";
		}
		document.getElementById("layout_screen").value=id;
	}	
	//动态调整大小
	function resize(id){
		var ratio = document.getElementById("ratio");
		if(!ratio.checked) {
			return;
		}
		var newWidth = document.getElementById("layout_width").value;
		var newHeight = document.getElementById("layout_height").value;
		if(newWidth =="" || newHeight == ""){
			return;
		}
		if(newWidth.match(numType) == null || newHeight.match(numType) == null){
			return;
		}
		if(id == 1){
			var height = Math.ceil(newWidth * 768 / 1024);
			document.getElementById("layout_height").value = height;
		}else {
			var width = Math.ceil(newHeight * 1024 / 768);
			document.getElementById("layout_width").value = width;
		}
	}
	//更改大小
	var oldWidth = 1024;
	var oldHeight = 768;
	function changeSize(){
		var newWidth = document.getElementById("layout_width").value;
		var newHeight = document.getElementById("layout_height").value;
		if(newWidth =="" || newHeight == ""){
			alert("场景大小不能为空");
		}
		if(newWidth.match(numType) == null || newHeight.match(numType) == null){
			alert("请输入数值类型");
		}
		if(newWidth > 1024 || newHeight > 768){
			alert("场景大小不能超过1024*768像素");
		}
		var num = document.getElementById("layout_screen").value;
		var screenType = document.getElementById("screen"+num);
		var screenImg = document.getElementById("img"+num);
		screenImg.style.width = Math.ceil(screenImg.offsetWidth * newWidth / oldWidth);
		//screenImg.style.height = Math.ceil(screenImg.offsetHeight * newHeight / oldHeight);
		oldWidth = newWidth;
		oldHeight = newHeight;
	}	
</script>
</head>
<body onload="onload();">
<div class="search_title"><span>广告管理-布局添加</span></div>
<table><tr style ="heigt:30px"></tr></table>
	<form name="layout_form" action="<%=url %>" method="post" >
		<input type="hidden" name="pageId" value="">
		<input type="hidden" name="funcId" value="">
		<input type="hidden" name="coordinate" value="">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td>
								<div id="screen_top">
								<div id="screen1" style="width: 410px; height: 307px; overflow: auto; ">
									<table width="100%">
										<tr>
											<!--<td style="height: 299px;border:1px solid #000000;">
											布局1
											</td>
										-->
											<td><img id="img1" alt="一分屏" src="./image/advert/single_big.png"></td>
										</tr>
									</table>
								</div>
								<div id="screen2" style="width: 410px; height: 307px; display: none; overflow: auto;" >
									<table width="100%">
										<tr>
											<td><img id="img2" alt="二分屏" src="./image/advert/double_big.png"></td>
										</tr>
									</table>
								</div>
								<div id="screen4" style=";width: 410px; height: 307px; display: none; overflow: auto;">
									<table width="100%">
										<tr>
											<td><img id="img4" alt="四分屏" src="./image/advert/four_big.png"></td>
										</tr>
									</table>
								</div>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								场景大小：
								<input type="text" value="1024" id="layout_width" name="layout_width" size="8" onkeyup="resize(1)">*
								<input type="text" value="768" id="layout_height" name="layout_height" size="8" onkeyup="resize(2)">
								<input type="checkbox" id="ratio" checked="checked">保持纵横比
							</td>
						</tr>
					</table>
				</td>
				<td valign="top">
					<table>
						<tr>
							<td>
								布&nbsp;&nbsp;&nbsp;&nbsp;局：：
								<input type="hidden"" id="layout_screen" name="layout_screen" value="1"/>
								<select id="rad_screen" name="rad_screen" onclick="changeScreen(this.value)">
									<option value="1">一分屏
									<option value="2">二分屏
									<option value="4">四分屏
								</select>
								

									
							</td>
						</tr>
						<tr>
							<td>
								布局描述：
								<input type="text" value="" id="layout_describe" name="layout_describe">
							</td>
						</tr>
						<tr>
							<td>
								布局名称：
								<input type="text" value="" id="layout_name" name="layout_name">
							</td>
						</tr>
	
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2">
				<input type="button" class="rightBtn" value="返回" onclick="returnSearch()">
				<input type="button" class="rightBtn" value="保存" onclick="confirm()">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>