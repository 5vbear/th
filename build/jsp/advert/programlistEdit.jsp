<%@page import="th.user.User"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="th.entity.AdvertBean"%>
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

//节目单ID
String programlistId = "";
//节目单名称
String programlistName = "";
//节目单描述
String programlistDescribe = "";
//节目单播放时长
String programlistPlayTime = "";
//布局ID
String layoutId = "";
//布局名称
String layoutName = "";
//布局描述
String layoutDescribe = "";
//创建用户
String operator = "";
//布局分辨率宽
String layoutwidth = "";
//布局分辨率高
String layoutHeight = "";
//分屏类型
String layoutScreen = "";
//分屏数
int screenNum = 0; 

int loop = 0;
List plMaterials = new ArrayList();

//检索结果
AdvertBean editBean = (AdvertBean)request.getAttribute("resultBean");
if (editBean != null) {
	//节目单ID
	programlistId = editBean.getProgramlistId();
	//节目单名称
	programlistName = editBean.getProgramlistName();
	//节目单描述
	programlistDescribe = editBean.getProgramlistDescribe();
	//节目单播放时长
	programlistPlayTime = String.valueOf(editBean.getProgramlistPlayTime());
	//布局ID
	layoutId = editBean.getLayoutId();
	//布局名称
	layoutName = editBean.getLayoutName();
	//布局名称
	layoutDescribe = editBean.getLayoutDescribe();
	//创建用户
	operator = editBean.getOperator();
	//布局分辨率宽
	layoutwidth = String.valueOf(editBean.getLayoutwidth());
	//布局分辨率高
	layoutHeight = String.valueOf(editBean.getLayoutHeight());
	//分屏类型
	layoutScreen = editBean.getLayoutScreen();
	//分屏数
	screenNum = editBean.getScreenNum(); 
	//节目单包含素材
	plMaterials = editBean.getProgramlistMaterials();
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<title>广告管理-节目单编辑</title>
<script language="JavaScript" type="text/javascript">
	var numType = new RegExp(/^[0-9]*$/);
	//页面初始化
	function onload() {
		var num =<%=screenNum%>;
		if(num == 1){
			document.getElementById("screen1").style.display = "";
			document.getElementById("screen2").style.display = "none";
			document.getElementById("screen4").style.display = "none";
		}else if(num == 2){
			document.getElementById("screen1").style.display = "none";
			document.getElementById("screen2").style.display = "";
			document.getElementById("screen4").style.display = "none";
		}else if(num == 4){
			document.getElementById("screen1").style.display = "none";
			document.getElementById("screen2").style.display = "none";
			document.getElementById("screen4").style.display = "";
		}
		//setLayoutOptions();
		objReadOnly(true);

		var message = "<%=result%>";
		if (message == null || message == "" || message == "null") {
			return;
		}
		alert(message);
	}
	
	//返回检索画面
	function returnSearch() {
		window.document.programlist_form.pageId.value = "<%=Define.JSP_PROGRAMLIST_SEARCH_ID%>";
		window.document.programlist_form.funcId.value = "";
		window.document.programlist_form.submit();
	}
	
	//保存
	function confirm() {
		// check
		//节目单名称
		if (document.getElementById("programlist_name").value == "") {
			alert("节目单名称不能为空！");
			return;
		}
		//布局名称
		if (document.getElementById("layout_name").value == "") {
			alert("布局类型不能为空！");
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
		window.document.programlist_form.coordinate.value = coordinate;
		window.document.programlist_form.pageId.value = "<%=Define.JSP_PROGRAMLIST_EDIT_ID%>";
		window.document.programlist_form.funcId.value = "<%=Define.FUNC_PROGRAMLIST_UPDATE_ID%>";
		window.document.programlist_form.submit();
	}
	
	//改变节目单类型
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
		setLayoutOptions();
	}	
	
	//更改大小
	function changeSize(){
		var num = document.getElementById("layout_screen").value;
		var screenType = document.getElementById("screen"+num);
	}	
	//素材库选取
	function selectMaterial() {
		var paramers="dialogWidth:400px;DialogHeight:300px;status:no;help:no;unadorned:no;resizable:no;status:no";  
		var url = "<%=url %>" + "?pageId=jsp_sub_window_id&funcId=func_material_subwindow_id";
		var ret=window.showModalDialog(url,'',paramers);  
		if (ret == null) {
			return;
		}
		setLayoutOptions();
		createMaterialDiv(ret);
	}
	//创建素材Div
	function createMaterialDiv(obj) {
		//显示选择的素材
		var mould = document.getElementById("mould");
		var addArea = document.getElementById("material_area");
		addArea.innerHTML = "";	
		var pushArr = new Array();
		for(var i=0; i<obj.length; i++) {
			document.getElementById("materialId").value=obj[i].id;
			document.getElementById("materialUrl").value=obj[i].file;
			//document.getElementById("showId").innerHTML=obj[i].name;
			var fileType = obj[i].file;
			var re_video = new RegExp(/(.mpg1)|(.swf)|(.wmv)|(.avi)|(.mpeg)|(.mpg)|(.mp4)/);
			var re_mp3 = new RegExp(/(.mp3)|(.wma)/);
			var re_pic = new RegExp(/(.jpg)|(.png)|(.bmp)|(.gif)/);
			
			if(fileType.match(re_video)) {
				var img = obj[i].file.replace(re_video, ".jpg")
				document.getElementById("show_img").src="<%=url%>"+"?pageId=func_ftp_file_get_id&dir="+img;
			}else if(fileType.match(re_mp3)){
				document.getElementById("show_img").src="./image/advert/mp3_icon.jpg";
			}else if(fileType.match(re_pic)){
				document.getElementById("show_img").src="<%=url%>"+"?pageId=func_ftp_file_get_id&dir="+obj[i].file;
			}
			document.getElementById("show_img").title=obj[i].name;
			document.getElementById("show_img").alt=obj[i].name;
			document.getElementById("layout_index").value = obj[i].windowNo;
			document.getElementById("show_level").value = obj[i].materialLevel;
			document.getElementById("play_time").value = obj[i].playTime;
			document.getElementById("material_link").value=obj[i].materiaLink;
			pushArr.push(mould.innerHTML);
			
		}
		for(var j=0; j<obj.length; j++) {
			var addDiv = document.createElement("div"); 
			addDiv.setAttribute("id","material"+j); 
			addDiv.innerHTML = pushArr[j];
			addArea.appendChild(addDiv);
		}
	}
	
	//删除被选择的素材
	function deleteMaterial() {
		
	}
	
	//创建布局
	function createLayout() {
		var radScreen = document.getElementsByName("rad_screen");
		radScreen[0].checked = "checked";
		changeScreen(1);
		objReadOnly(false);
		document.getElementById("layoutId").value = "";
		document.getElementById("layout_name").value = "";
		document.getElementById("layout_describe").value = "";
		document.getElementById("layout_screen").value = "1";
		document.getElementById("layout_width").value = "1024";
		document.getElementById("layout_height").value = "768";
	}
	
	//选取布局
	function selectLayout() {
		var paramers="dialogWidth:400px;DialogHeight:300px;status:no;help:no;unadorned:no;resizable:no;status:no";  
		var url = "<%=url %>" + "?pageId=jsp_sub_window_id&funcId=func_layout_subwindow_id";
		var ret=window.showModalDialog(url,'',paramers);  
		if (ret == null) {
			return;
		}
		//显示选择的布局
		document.getElementById("layoutId").value = ret.layoutId;
		document.getElementById("layout_name").value = ret.layoutName;
		document.getElementById("layout_describe").value = ret.layoutdescribe;
		var radScreen = document.getElementsByName("rad_screen");
		if(ret.layoutScreen == "1"){
			radScreen[0].checked = "checked";
		}else if(ret.layoutScreen == "2"){
			radScreen[1].checked = "checked";
		}else if(ret.layoutScreen == "4"){
			radScreen[2].checked = "checked";
		}
		document.getElementById("layout_screen").value = ret.layoutScreen;
		document.getElementById("layout_width").value = ret.layoutwidth;
		document.getElementById("layout_height").value = ret.layoutHeight;
		
		changeScreen(ret.layoutScreen);
		objReadOnly(true);
		//setLayoutOptions(ret.windowsId);
		setLayoutOptions();
	}
	
	//选取布局后设置不可修改
	function objReadOnly(flg) {
		//document.getElementById("layoutId").disabled = false;
		document.getElementById("layout_name").disabled = flg;
		document.getElementById("layout_describe").disabled = flg;
		var radScreen = document.getElementsByName("rad_screen");
		radScreen[0].disabled = flg;
		radScreen[1].disabled = flg;
		radScreen[2].disabled = flg;
		document.getElementById("layout_width").disabled = flg;
		document.getElementById("layout_height").disabled = flg;
	}
	
	//动态设置所在布局Select控件
	function setLayoutOptions(options) {
		var layoutScreen = document.getElementById("layout_screen").value;
		var selectObjs = document.getElementsByName("layout_index");
		if(options == null) {
			for(var i=0; i<selectObjs.length; i++) {
				selectObjs[i].options.length = 0;
				var varItem = new Option("", "");  
				selectObjs[i].options.add(varItem);         
				for(var j=1; j<=layoutScreen; j++) {
					varItem = new Option("布局"+j, j); 
					selectObjs[i].options.add(varItem);        
				}
			}
		}else {
			var objs = options.split(",");
			for(var i=0; i<selectObjs.length; i++) {
				selectObjs[i].options.length = 0;
				var varItem = new Option("", "");  
				selectObjs[i].options.add(varItem);         
				for(var j=1; j<=objs.length; j++) {
					varItem = new Option("布局"+j, objs[j-1]); 
					selectObjs[i].options.add(varItem);        
				}
			}
		}
	}
	//效果预览
	function preview(){
		var layoutScreen = document.getElementById("layout_screen").value;
		var materials = document.getElementsByName("materialId");
		var layout_indexs = document.getElementsByName("layout_index");
		var materialUrls = document.getElementsByName("materialUrl");
		if(materials.length == 0){
			alert("请先选择素材！");
			return;
		}
		var flg;
		for(var i=0; i<layout_indexs.length; i++){
			//alert("请选择素材所在布局！");
		}
		document.getElementById("bg").style.display = "block";
        document.getElementById("show").style.display = "block";
        var re_video = new RegExp(/(.mpg1)|(.swf)|(.wmv)|(.avi)|(.mpeg)|(.mpg)|(.mp4)/);
		var re_mp3 = new RegExp(/(.mp3)|(.wma)/);
		var re_pic = new RegExp(/(.jpg)|(.png)|(.bmp)|(.gif)/);
		var playerObj = document.getElementById("player");
		if(layoutScreen == "1"){
			document.getElementById("preview1").style.display = "block";
			document.getElementById("preview2").style.display = "none";
			document.getElementById("preview4").style.display = "none";
			var url;
			for(var i=0; i<layout_indexs.length; i++){
				if(layout_indexs[i].value == "1"){
					url = materialUrls[i].value;
					break;
				}
			}
			if(url){
				if(url.match(re_video)) {
					playerObj.width="600px";
					playerObj.height="450px";
					var videoUrl = "AdvertServlet?pageId=func_ftp_file_get_id&dir="+url;
					var playerMould = document.getElementById("playerMould").innerHTML; 
					playerMould = playerMould.replace("{url}", videoUrl);
					document.getElementById("objDiv1_1").innerHTML = playerMould;
				}else if(url.match(re_mp3)){
					var img = document.createElement("img"); 
					img.id = "obj1_1";
					img.src = "./image/advert/mp3_icon.jpg";
					img.style.width = "600px";
					img.style.height = "450px";
					document.getElementById("objDiv1_1").innerHTML = img.outerHTML;
				}else if(url.match(re_pic)){
					var img = document.createElement("img"); 
					img.id = "obj1_1";
					img.src = "<%=url%>"+"?pageId=func_ftp_file_get_id&dir="+url;
					img.style.width = "600px";
					img.style.height = "450px";
					document.getElementById("objDiv1_1").innerHTML = img.outerHTML;
				}
			}
		}else if(layoutScreen == "2"){
			document.getElementById("preview1").style.display = "none";
			document.getElementById("preview2").style.display = "block";
			document.getElementById("preview4").style.display = "none";
			var url1, url2;
			for(var i=0; i<layout_indexs.length; i++){
				if(layout_indexs[i].value == "1"){
					if(!url1){
						url1 = materialUrls[i].value;
					}
				}
				if(layout_indexs[i].value == "2"){
					if(!url2){
						url2 = materialUrls[i].value;
					}
				}
			}
			if(url1){
				if(url1.match(re_video)) {
					playerObj.width="300px";
					playerObj.height="450px";
					var videoUrl = "AdvertServlet?pageId=func_ftp_file_get_id&dir="+url1;
					var playerMould = document.getElementById("playerMould").innerHTML; 
					playerMould = playerMould.replace("{url}", videoUrl);
					document.getElementById("objDiv2_1").innerHTML = playerMould;
				}else if(url1.match(re_mp3)){
					var img = document.createElement("img"); 
					img.id = "obj2_1";
					img.src = "./image/advert/mp3_icon.jpg";
					img.style.width = "300px";
					img.style.height = "450px";
					document.getElementById("objDiv2_1").innerHTML = img.outerHTML;
				}else if(url1.match(re_pic)){
					var img = document.createElement("img"); 
					img.id = "obj2_1";
					img.src = "<%=url%>"+"?pageId=func_ftp_file_get_id&dir="+url1;
					img.style.width = "300px";
					img.style.height = "450px";
					document.getElementById("objDiv2_1").innerHTML = img.outerHTML;
				}
			}
			if(url2){
				if(url2.match(re_video)) {
					playerObj.width="300px";
					playerObj.height="450px";
					var videoUrl = "AdvertServlet?pageId=func_ftp_file_get_id&dir="+url2;
					var playerMould = document.getElementById("playerMould").innerHTML; 
					playerMould = playerMould.replace("{url}", videoUrl);
					document.getElementById("objDiv2_2").innerHTML = playerMould;
				}else if(url2.match(re_mp3)){
					var img = document.createElement("img"); 
					img.id = "obj2_2";
					img.src = "./image/advert/mp3_icon.jpg";
					img.style.width = "300px";
					img.style.height = "450px";
					document.getElementById("objDiv2_2").innerHTML = img.outerHTML;
				}else if(url2.match(re_pic)){
					var img = document.createElement("img"); 
					img.id = "obj2_2";
					img.src = "<%=url%>"+"?pageId=func_ftp_file_get_id&dir="+url2;
					img.style.width = "300px";
					img.style.height = "450px";
					document.getElementById("objDiv2_2").innerHTML = img.outerHTML;
				}
			}
		}else if(layoutScreen == "4"){
			document.getElementById("preview1").style.display = "none";
			document.getElementById("preview2").style.display = "none";
			document.getElementById("preview4").style.display = "block";
			var url1, url2,url3, url4;
			for(var i=0; i<layout_indexs.length; i++){
				if(layout_indexs[i].value == "1"){
					if(!url1){
						url1 = materialUrls[i].value;
					}
				}
				if(layout_indexs[i].value == "2"){
					if(!url2){
						url2 = materialUrls[i].value;
					}
				}
				if(layout_indexs[i].value == "3"){
					if(!url3){
						url3 = materialUrls[i].value;
					}
				}
				if(layout_indexs[i].value == "4"){
					if(!url4){
						url4 = materialUrls[i].value;
					}
				}
			}
			if(url1){
				if(url1.match(re_video)) {
					playerObj.width="300px";
					playerObj.height="225px";
					var videoUrl = "AdvertServlet?pageId=func_ftp_file_get_id&dir="+url1;
					var playerMould = document.getElementById("playerMould").innerHTML; 
					playerMould = playerMould.replace("{url}", videoUrl);
					document.getElementById("objDiv4_1").innerHTML = playerMould;
				}else if(url1.match(re_mp3)){
					var img = document.createElement("img"); 
					img.id = "obj4_1";
					img.src = "./image/advert/mp3_icon.jpg";
					img.style.width = "300px";
					img.style.height = "225px";
					document.getElementById("objDiv4_1").innerHTML = img.outerHTML;
				}else if(url1.match(re_pic)){
					var img = document.createElement("img"); 
					img.id = "obj4_1";
					img.src = "<%=url%>"+"?pageId=func_ftp_file_get_id&dir="+url1;
					img.style.width = "300px";
					img.style.height = "225px";
					document.getElementById("objDiv4_1").innerHTML = img.outerHTML;
				}
			}
			if(url2){
				if(url2.match(re_video)) {
					playerObj.width="300px";
					playerObj.height="225px";
					var videoUrl = "AdvertServlet?pageId=func_ftp_file_get_id&dir="+url2;
					var playerMould = document.getElementById("playerMould").innerHTML; 
					playerMould = playerMould.replace("{url}", videoUrl);
					document.getElementById("objDiv4_2").innerHTML = playerMould;
				}else if(url2.match(re_mp3)){
					var img = document.createElement("img"); 
					img.id = "obj4_2";
					img.src = "./image/advert/mp3_icon.jpg";
					img.style.width = "300px";
					img.style.height = "225px";
					document.getElementById("objDiv4_2").innerHTML = img.outerHTML;
				}else if(url2.match(re_pic)){
					var img = document.createElement("img"); 
					img.id = "obj4_2";
					img.src = "<%=url%>"+"?pageId=func_ftp_file_get_id&dir="+url2;
					img.style.width = "300px";
					img.style.height = "225px";
					document.getElementById("objDiv4_2").innerHTML = img.outerHTML;
				}
			}
			if(url3){
				if(url3.match(re_video)) {
					playerObj.width="300px";
					playerObj.height="225px";
					var videoUrl = "AdvertServlet?pageId=func_ftp_file_get_id&dir="+url3;
					var playerMould = document.getElementById("playerMould").innerHTML; 
					playerMould = playerMould.replace("{url}", videoUrl);
					document.getElementById("objDiv4_3").innerHTML = playerMould;
				}else if(url3.match(re_mp3)){
					var img = document.createElement("img"); 
					img.id = "obj4_3";
					img.src = "./image/advert/mp3_icon.jpg";
					img.style.width = "300px";
					img.style.height = "225px";
					document.getElementById("objDiv4_3").innerHTML = img.outerHTML;
				}else if(url3.match(re_pic)){
					var img = document.createElement("img"); 
					img.id = "obj4_3";
					img.src = "<%=url%>"+"?pageId=func_ftp_file_get_id&dir="+url3;
					img.style.width = "300px";
					img.style.height = "225px";
					document.getElementById("objDiv4_3").innerHTML = img.outerHTML;
				}
			}
			if(url4){
				if(url4.match(re_video)) {
					playerObj.width="300px";
					playerObj.height="225px";
					var videoUrl = "AdvertServlet?pageId=func_ftp_file_get_id&dir="+url4;
					var playerMould = document.getElementById("playerMould").innerHTML; 
					playerMould = playerMould.replace("{url}", videoUrl);
					document.getElementById("objDiv4_4").innerHTML = playerMould;
				}else if(url4.match(re_mp3)){
					var img = document.createElement("img"); 
					img.id = "obj4_4";
					img.src = "./image/advert/mp3_icon.jpg";
					img.style.width = "300px";
					img.style.height = "225px";
					document.getElementById("objDiv4_4").innerHTML = img.outerHTML;
				}else if(url4.match(re_pic)){
					var img = document.createElement("img"); 
					img.id = "obj4_4";
					img.src = "<%=url%>"+"?pageId=func_ftp_file_get_id&dir="+url4;
					img.style.width = "300px";
					img.style.height = "225px";
					document.getElementById("objDiv4_4").innerHTML = img.outerHTML;
				}
			}
		}else{
			alert("布局信息不正确，请重新选择！");
		}
	}
	//取消预览
	function hidediv() {
		var players = document.getElementsByName("player");
		for(var i=0; i<players.length; i++){
			players[i].controls.stop();
		}
        document.getElementById("bg").style.display = 'none';
        document.getElementById("show").style.display = 'none';
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
</script>
</head>
<body onload="onload();">
<div class="search_title"><span>广告管理-节目单编辑</span></div>
<table><tr style ="heigt:30px"></tr></table>
	<form name="programlist_form" action="<%=url %>" method="post" >
		<input type="hidden" name="pageId" value="">
		<input type="hidden" name="funcId" value="">
		<input type="hidden" id="programlistId" name="programlistId" value="<%=programlistId %>">
		<input type="hidden" id="layoutId" name="layoutId" value="<%=layoutId %>">
		<input type="hidden" name="coordinate" value="">
		<table>
			<tr>
				<td valign="top">
					<table>
						<tr>
							<td>
								<div id="screen_top">
								<div id="screen1" style="width: 410px; height: 307px;">
									<table width="100%">
										<tr>
											<!--<td style="height: 299px;border:1px solid #000000;">
											布局1
											</td>
										-->
											<td><img alt="一分屏" src="./image/advert/single_big.png"></td>
										</tr>
									</table>
								</div>
								<div id="screen2" style="width: 410px; height: 307px; display: none;" >
									<table width="100%">
										<tr>
											<td><img alt="二分屏" src="./image/advert/double_big.png"></td>
										</tr>
									</table>
								</div>
								<div id="screen4" style=";width: 410px; height: 307px; display: none;">
									<table width="100%">
										<tr>
											<td><img alt="四分屏" src="./image/advert/four_big.png"></td>
										</tr>
									</table>
								</div>
								</div>
							</td>
						</tr>
						<tr>
							<td><input type="button" value="效果预览" onclick="preview()"></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>(请允许弹出页面后再于预览节目单)</td>
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
								节目单名称：
								<input type="text" value="<%=programlistName %>" id="programlist_name" name="programlist_name">
							</td>
						</tr>
						<tr>
							<td>
								节目单描述：
								<input type="text" value="<%=programlistDescribe %>" id="programlist_describe" name="programlist_describe">
							</td>
						</tr>
						<tr>
							<td>
								节目单时长：
								<input type="text" value="<%=programlistPlayTime %>" id="programlist_time" name="programlist_time">秒
							</td>
						</tr>
						<tr>
							<td>
								<input type="button" value="创建布局" onclick="createLayout()">
								<input type="button" value="选取布局" onclick="selectLayout()">
							</td>
						</tr>
						<tr>
							<td>
								布局名称：
								<input type="text" value="<%=layoutName %>" id="layout_name" name="layout_name">
							</td>
						</tr>
						<tr>
							<td>
								布局描述：
								<input type="text" value="<%=layoutDescribe %>" id="layout_describe" name="layout_describe">
							</td>
						</tr>
						<tr>
							<td>
								布局类型：
								<input type="hidden"" id="layout_screen" name="layout_screen" value="<%=screenNum %>"/>
								<input type="radio"" name="rad_screen" checked="checked" onclick="changeScreen(1)"
									<%if ("1".equals(layoutScreen)) {%> checked="checked" <% }%>
								/><img alt="一分屏" src="./image/advert/single_small.png">(一分屏)
								<input type="radio"" name="rad_screen" onclick="changeScreen(2)"
									<%if ("2".equals(layoutScreen)) {%> checked="checked" <% }%>
								/><img alt="二分屏" src="./image/advert/double_small.png">(二分屏)
								<input type="radio"" name="rad_screen" onclick="changeScreen(4)"
									<%if ("4".equals(layoutScreen)) {%> checked="checked" <% }%>
								/><img alt="四分屏" src="./image/advert/four_small.png">(四分屏)
							</td>
						</tr>	
						<tr>
							<td>
								<input type="button" value="素材库选取" onclick="selectMaterial()">(请选好所有所需素材再编排)
							</td>
						</tr>
						<tr>
							<td>
								<div id="material_area" style="height: 200px; overflow: scroll;">
								<%
								if(plMaterials != null) {
									for(int i=0; i<plMaterials.size(); i++) {
										AdvertBean bean = (AdvertBean)plMaterials.get(i);
										String materialId=bean.getMaterialId();
										String materialName=bean.getMaterialName();
										String materiaFilelName = bean.getMaterialFilelName();
										String fileUrl = "";
										if(materiaFilelName.endsWith(".mpg1") 
												|| materiaFilelName.endsWith(".swf")
												|| materiaFilelName.endsWith(".wmv")
												|| materiaFilelName.endsWith(".avi")
												|| materiaFilelName.endsWith(".mpeg")
												|| materiaFilelName.endsWith(".mpg")
												|| materiaFilelName.endsWith(".mp4")
												) {
											String imgPath = materiaFilelName.substring(0, materiaFilelName.lastIndexOf(".")) + ".jpg";
											fileUrl = url + "?pageId=func_ftp_file_get_id&dir=" + imgPath;
										} else if (materiaFilelName.contains(".mp3") || materiaFilelName.contains(".wma")) {
											fileUrl = "./image/advert/mp3_icon.jpg";
										} else if (materiaFilelName.contains(".jpg") || materiaFilelName.contains(".png")
												|| materiaFilelName.contains(".bmp") || materiaFilelName.contains(".gif")) {
											fileUrl = url + "?pageId=func_ftp_file_get_id&dir=" + materiaFilelName;
										}
										int windowNo=bean.getWindowNo();
										int materialLevel=bean.getMaterialLevel();
										int materialPlayTime=bean.getMaterialPlayTime();
										String materialLink = bean.getMaterialLink();
										String divId = "materialId" + i;
								%>	
									<div id="<%=divId %>">
										<div style="height: 105px; width: 320px; border: 1px solid #565656;">
											<input type="hidden" id="materialId" name="materialId" value="<%=materialId %>">
											<input type="hidden" id="materialUrl" name="materialUrl" value="<%=materiaFilelName %>">
											<table>
												<tr>
													<td rowspan="4" width="100"><img id="show_img" height="75" width="100" alt="" src="<%=fileUrl %>" title=""></td>
													<td>
														所属布局:
													</td>
													<td>
														<select id="layout_index" name="layout_index">
															<option value=""></option>
															<%
															for(int j=1; j<=screenNum; j++) {
																String value = String.valueOf(j);
																String text = "布局"+j;
															%>	
																<%if(windowNo == j){%>
																<option value="<%=value %>" selected="selected"><%=text %></option>
																<% }else{ %>
																<option value="<%=value %>" ><%=text %></option>
																<% }%>
															<% 	
															}
															%>
														</select>
													</td>
												</tr>
												<tr>
													<td>
														显示级别:
													</td>
													<td>
														<select id="show_level" name="show_level">
														<option value="1"
															<%if (materialLevel == 1) {%> checked="checked" <% }%>
														>1</option>
														<option value="2" 
															<%if (materialLevel == 2) {%> checked="checked" <% }%>
														>2</option>
														<option value="3" 
															<%if (materialLevel == 3) {%> checked="checked" <% }%>
														>3</option>
														<option value="4" 
															<%if (materialLevel == 4) {%> checked="checked" <% }%>
														>4</option>
														</select>
													</td>
												</tr>
												<tr>
													<td>
														播放时长:
													</td>
													<td>
														<input type="text" id="play_time" name="play_time" size="4" value="<%=materialPlayTime %>"/>秒(0为不限制)
													</td>
												</tr>
												<tr>
													<td>
														素材热链:
													</td>
													<td>
														<input type="text" id="material_link" name="material_link" value="<%=materialLink%>" readonly="readonly"/>
													</td>
												</tr>
											</table>
										</div>
									</div>	
								<%
									}
								}
								%>
								</div>
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
	<div id="bg" class=".bg" style="display: none;"></div>
	<div id="show" class=".show" style="display: none;">
		<div id="preview1">
			<div id="objDiv1_1"></div>
		</div>
		<div id="preview2">
			<div style="float: left; margin: 0px;" id="objDiv2_1"></div>
			<div style="float: left; margin-left: 0px;" id="objDiv2_2"></div>
		</div>
		<div id="preview4">
			<div>
				<div style="float: left; margin: 0px;" id="objDiv4_1"></div>
				<div style="float: left; margin-left: 0px;" id="objDiv4_2"></div>
			</div>
			<div>
				<div style="float: left; margin: 0px;" id="objDiv4_3"></div>
				<div style="float: left; margin-left: 0px;" id="objDiv4_4"></div>
			</div>
		</div>
		<div style="height: 20px; ">
	    <input id="btnclose" type="button" value="关闭" onclick="hidediv();" />
	    </div>
	</div>
	<div id="mould" style="display: none;">
		<div style="height: 105px; width: 320px; border: 1px solid #565656;">
			<input type="hidden" id="materialId" name="materialId" value="">
			<input type="hidden" id="materialUrl" name="materialUrl" value="">
			<table>
				<tr>
					<td rowspan="4" width="100"><img id="show_img" height="75" width="100" alt="" src="" title=""></td>
					<td>
						所属布局:
					</td>
					<td>
						<select id="layout_index" name="layout_index">
							<option value=""></option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						显示级别:
					</td>
					<td>
						<select id="show_level" name="show_level">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						播放时长:
					</td>
					<td>
						<input type="text" id="play_time" name="play_time" size="4"/>秒(0为不限制)
					</td>
				</tr>
				<tr>
					<td>
						素材热链:
					</td>
					<td>
						<input type="text" id="material_link" name="material_link" value="" readonly="readonly"/>
					</td>
				</tr>
			</table>
		</div>
		<p></p>
	</div>
	<div id="playerMould" style="display: none;">
		<object id="player" name="player" width="" height="" classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6"> 
		<param name="url" value="{url}"> 
		<param name="uiMode" value="none"> 
		<param name="PlayCount" value="100">  
		</object>
	</div>
</body>
</html>