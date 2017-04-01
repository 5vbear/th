<%@page import="th.entity.AdvertBean"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.user.User"%>
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
String colorPickerStyle = strContextPath + "/css/jquery.colorpicker.css";
String jqueryJs = strContextPath + "/js/jquery-1.6.1.js";
String colorPickerJs = strContextPath + "/js/jquery.colorpicker.js";
request.setCharacterEncoding("UTF-8");
String result = (String)request.getAttribute("result");
//素材类型
String advert_type = (String)request.getAttribute("advert_type");
if(advert_type == null || "".equals(advert_type)){
	advert_type = "1";
}
//素材分组
List groupList = (List)request.getAttribute("GROUP_LIST");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=colorPickerStyle %>" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<title>广告管理-素材添加</title>
<script type="text/javascript" src="<%=jqueryJs %>"></script>
<script type="text/javascript" src="<%=colorPickerJs %>"></script>
<script language="JavaScript" type="text/javascript">
	var numType = new RegExp(/^[0-9]*$/);
	var fileType=new Array(".jpg",".png",".bmp",".gif",".mpg",".swf",".mp3",".wma",".wmv",".avi",".mpeg",".mp4");
	//页面初始化
	window.onload = function doload() {
		var obj = window.document.material_form.advert_type;
		materialTypeChange(obj.value);
		var message = "<%=result%>";
		if (message == null || message == "" || message == "null") {
			return;
		}
		alert(message);
	}

	//自定义组
	function customGroup(){
		var group_btn = document.getElementById("customBtn");
		var group_text = document.getElementById("custom_group");
		var group_select = document.getElementById("material_group");
		if(group_text.style.display == 'none'){
			group_text.style.display = 'block';
			group_select.style.display = 'none';
			group_btn.value = "取消";
		}else{
			group_text.style.display = 'none';
			group_select.style.display = 'block';
			group_btn.value = "自定义组";
			group_text.value="";
		}
	}
	
	//返回检索画面
	function returnSearch() {
		window.document.material_form.pageId.value = "<%=Define.JSP_MATERIAL_SEARCH_ID%>";
		window.document.material_form.funcId.value = "";
		window.document.material_form.submit();
	}

	function materialTypeChange(val) {
		var type_obj = document.getElementById("advert_type");
		if (val == 0){
			type_obj.value = (type_obj.value == "1" ? "2" : "1");
		}else{
			type_obj.value = val;
		}
		var type = type_obj.value;
		var div1 = document.getElementById("material1");
		var div2 = document.getElementById("subtitles");
		var div3 = document.getElementById("addBtn");
		var advert_btn = document.getElementById("advertBtn");
		if (type=="1") {
			div1.style.display = "block";
			div2.style.display = "none";
			div3.style.display = "block";
			advert_btn.value = "自定义素材";
		} else {
			div1.style.display = "none";
			div2.style.display = "block";
			div3.style.display = "none";
			advert_btn.value = "媒体素材";
		}
	}

	//媒体素材添加
	var divIndex = 1;
	var divCnt = 1;
	function mediaMaterialAdd() {
		if(divCnt > 10){
			alert("1次添加的素材超出最大限制！");
			return;
		}
		divIndex++;
		divCnt++;
		var mouldInner = document.getElementById("mould").innerHTML;
		var divInner = document.getElementById("addDiv");
		var addDiv = document.createElement("div"); 
		addDiv.setAttribute("id","material"+divIndex); 
		addDiv.style.display = "block";
		addDiv.style.borderStyle = "solid";
		addDiv.style.borderWidth = "1px";
		addDiv.innerHTML = mouldInner + "<p></p>";
		//divInner.innerHTML = divInner.innerHTML + addDiv.innerHTML + "<p></p>";
		divInner.appendChild(addDiv);

		//document.getElementById("delBtn").style.visibility = "visible";
	}

	//媒体素材删除
	function mediaMaterialDel(obj) {
		if (divCnt > 1) {
			if(obj.parentNode.id == 'material1'){
				obj.parentNode.parentNode.removeChild(obj.parentNode);
			}else{
				var parentDiv = document.getElementById("addDiv");
				parentDiv.removeChild(obj.parentNode);
			}
			divCnt--; 
		}
	}
	
	// 颜色选择
	$(function(){
		$("#background_color_btn").bigColorpicker("background_color");
		$("#word_color_btn").bigColorpicker("word_color");
	})

	//保存
	function confirm() {
		// check
		var type = document.getElementById("advert_type").value;
		//自定义分组
		if(document.getElementById("custom_group").style.display=='block' && document.getElementById("custom_group").value == "") {
			alert("自定义分组不能为空！");
			return;
		}
		//媒体素材或字幕判断
		if (type == "1") {
			//素材文件
			if (document.getElementById("material_filePath").value == "") {
				alert("请选择素材文件！");
				return;
			}
			//素材文件类型
			if (document.getElementById("material_type").value == "") {
				alert("素材文件类型不能为空！");
				return;
			}
			//素材名称
			if (document.getElementById("material_name").value == "") {
				alert("素材名称类型不能为空！");
				return;
			}
			//
			var filePath = document.getElementById("material_filePath").value;
			var objtype=filePath.substring(filePath.lastIndexOf(".")).toLowerCase();
			var flag = false;
			for(var i=0; i<fileType.length; i++){
				if(objtype == fileType[i]){
					flag = true;
					break;
				}
			}
			if(!flag){
				alert("上传的文件类型不正确！\\n支持.jpg、.png、.bmp、.gif、.mpg、.swf、.mp3、.wma、.wmv、.avi、.mpeg、.mp4类型的文件上传！");
				return;
			}

		} else if (type == "2") {
			//素材名称
			if (document.getElementById("subtitles_name").value == "") {
				alert("素材名称不能为空！");
				return;
			}
			//滚动文字
			if (document.getElementById("roll_word").value == "") {
				alert("滚动文字不能为空！");
				return;
			}
			var colorReg = new RegExp(/^#[0-9a-fA-F]{6}$/);
			//背景颜色
			if (document.getElementById("background_color").value != "" 
					&& document.getElementById("background_color").value.match(colorReg) == null) {
				alert("背景颜色输入不正确！");
				return;
			}
			//字体颜色
			if (document.getElementById("word_color").value != "" 
					&& document.getElementById("word_color").value.match(colorReg) == null) {
				alert("字体颜色输入不正确！");
				return;
			}
			//高
			if (document.getElementById("word_height").value.match(numType) == null) {
				alert("高必须为数值类型！");
				return;
			}
			//宽
			if (document.getElementById("word_width").value.match(numType) == null) {
				alert("宽必须为数值类型！");
				return;
			}
			//滚动延迟
			if (document.getElementById("roll_delay").value.match(numType) == null) {
				alert("滚动延迟必须为数值类型！");
				return;
			}
			
			//字体属性-粗体
			if (window.document.material_form.chk_word_bold.checked) {
				window.document.material_form.word_bold.value="bold";
			} else {
				window.document.material_form.word_bold.value="normal";
			}
			//字体属性-斜体
			if (window.document.material_form.chk_word_tilt.checked) {
				window.document.material_form.word_tilt.value="italic";
			} else {
				window.document.material_form.word_tilt.value="normal";
			}
			//水平对齐
			var text_align=document.getElementsByName("rad_text_align");
			for(var i=0;i<text_align.length;i++){
				if(text_align[i].checked) {
					document.getElementById("text_align").value=i+1;
				}
			}
			//垂直对齐
			var vertical_align=document.getElementsByName("rad_vertical_align");
			for(var i=0;i<vertical_align.length;i++){
				if(vertical_align[i].checked) {
					document.getElementById("vertical_align").value=i+1;
				}
			}
		} else {
			alert("素材类型选择不正确！");
		}
		
		var actionUrl = "<%=url%>" + "?pageId="+"<%=Define.JSP_MATERIAL_ADD_ID%>" + "&funcId=" + "<%=Define.FUNC_MATERIAL_ADD_ID%>";
		window.document.material_form.action = actionUrl;
		//window.document.material_form.pageId.value = "<%=Define.JSP_MATERIAL_ADD_ID%>";
		//window.document.material_form.funcId.value = "<%=Define.FUNC_MATERIAL_ADD_ID%>";
		window.document.material_form.encoding = "multipart/form-data";
		window.document.material_form.submit();
		document.getElementById("confirmBtn").disabled = true;
	}

	//字幕预览
	function preview(){
		//滚动文字
		if (document.getElementById("roll_word").value == "") {
			alert("滚动文字不能为空！");
			return;
		}
		//高
		if (document.getElementById("word_height").value.match(numType) == null) {
			alert("高必须为数值类型！");
			return;
		}
		//宽
		if (document.getElementById("word_width").value.match(numType) == null) {
			alert("宽必须为数值类型！");
			return;
		}
		//滚动延迟
		if (document.getElementById("roll_delay").value.match(numType) == null) {
			alert("滚动延迟必须为数值类型！");
			return;
		}
		//字体属性-粗体
		if (window.document.material_form.chk_word_bold.checked) {
			window.document.material_form.word_bold.value="bold";
		} else {
			window.document.material_form.word_bold.value="normal";
		}
		//字体属性-斜体
		if (window.document.material_form.chk_word_tilt.checked) {
			window.document.material_form.word_tilt.value="italic";
		} else {
			window.document.material_form.word_tilt.value="normal";
		}
		//水平对齐
		var text_align=document.getElementsByName("rad_text_align");
		for(var i=0;i<text_align.length;i++){
			if(text_align[i].checked) {
				document.getElementById("text_align").value=i+1;
			}
		}
		//垂直对齐
		var vertical_align=document.getElementsByName("rad_vertical_align");
		for(var i=0;i<vertical_align.length;i++){
			if(vertical_align[i].checked) {
				document.getElementById("vertical_align").value=i+1;
			}
		}
		window.document.material_form.pageId.value = "<%=Define.JSP_MATERIAL_ADD_ID%>";
		window.document.material_form.funcId.value = "<%=Define.FUNC_SUBTITLES_PREVIEW_ID%>";
		window.document.material_form.submit();
	}		
</script>
</head>
<body>
<div class="search_title"><span>广告管理-素材添加</span></div>
<table><tr style ="heigt:30px"></tr></table>
	<form name="material_form" action="<%=url %>" method="post" >
		<input type="hidden" name="pageId" value="">
		<input type="hidden" name="funcId" value="">
		<table>
			<tr>
				<td><font color="red">*</font>素材分组：</td>
				<td><input type="button" id="customBtn" value="自定义组" onclick="customGroup()"></td>
				<td>
					<input type="text" id="custom_group" name="custom_group" style="display: none;"/>
					<select id="material_group" name="material_group">
					<%
					if (groupList != null){
						for (int i = 0; i < groupList.size(); i++) {
							AdvertBean bean = (AdvertBean)groupList.get(i);
					%>
					<option value="<%=bean.getMaterial_group() %>"><%=bean.getGroupName() %></option>
					<% 
						}
					}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<input type="hidden" id="advert_type" name="advert_type" value="<%=advert_type %>">
					<input type="button" id="advertBtn" value="自定义素材" onclick="materialTypeChange(0)">
				</td>
			</tr>
		</table>
		<div style="height: 560px; overflow: auto;">
		<div id="material1" style="display: block;border-style: solid ;border-width: 1px;">
			<table style="display: inline;">
				<tr>
					<td><font color="red">*</font>素材文件：</td>
					<td colspan="3"><input type="file" id="material_filePath" name="material_filePath"></td>
				</tr>
				<tr>
					<td><font color="red">*</font>素材名称：</td>
					<td><input type="text" id="material_name" name="material_name" /></td>
					<td>素材类型：</td>
					<td><select id="material_type" name="material_type">
							<option value=""></option>
							<option value="图片">图片</option>
							<option value="视频">视频</option>
							<option value="音频">音频</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>素材说明：</td>
					<td><input type="text" id="material_describe" name="material_describe" /></td>
					<td>素材链接：</td>
					<td><input type="text" name="material_link" /></td>
				</tr>
			</table>
			<a onclick="javascript:mediaMaterialDel(this);" class="btn_close">
				<img alt="删除" title="删除" src="./image/advert/btn_close.png">
			</a> 
			<p></p>			
		</div>
		<div id="addDiv"></div>
		<div id="subtitles" style="display: none; border-style: solid ;border-width: 1px;">
			<table>
				<tr>
					<td><font color="red">*</font>素材名称：</td>
					<td><input type="text" id="subtitles_name" name="subtitles_name" />
						<input type="button" value="效果预览" onclick="preview()"></td>
					<td></td>
				</tr>
				<tr>
					<td>素材描述：</td>
					<td><input type="text" id="subtitles_describe" name="subtitles_describe" /></td>
					<td></td>
				</tr>
				<tr>
					<td><font color="red">*</font>滚动文字：</td>
					<td><input type="text" id="roll_word" name="roll_word" /></td>
					<td></td>
				</tr>
				<!--<tr>
					<td>背景图片：</td>
					<td><input type="file" id="background_filePath" name="background_filePath" ></td>
					<td></td>
				</tr>
				-->
				<tr>
					<td>背景颜色：</td>
					<td><input type="text" id="background_color" name="background_color" />
						<input type="button" id="background_color_btn" value="选色">
					</td>
					<td></td>
				</tr>
				<tr>
					<td>字体颜色：</td>
					<td><input type="text" id="word_color" name="word_color" />
						<input type="button" id="word_color_btn" value="选色">
					</td>
					<td></td>
				</tr>
				<tr>
					<td>字体属性：</td>
					<td><select id="word_type" name="word_type" >
							<option value="Arial">Arial</option>
							<option value="KaiTi">楷书</option>
							<option value="SimSun">宋体</option>
						</select>
						<select id="word_size" name="word_size" >
							<option value="4px">4号字</option>
							<option value="5px">5号字</option>
							<option value="6px">6号字</option>
							<option value="7px">7号字</option>
							<option value="8px">8号字</option>
							<option value="9px">9号字</option>
							<option value="10px">10号字</option>
							<option value="11px">11号字</option>
							<option value="12px">12号字</option>
							<option value="13px">13号字</option>
							<option value="14px">14号字</option>
							<option value="15px">15号字</option>
							<option value="16px">16号字</option>
							<option value="17px">17号字</option>
							<option value="18px">18号字</option>
							<option value="19px">19号字</option>
							<option value="20px">20号字</option>
							<option value="30px">30号字</option>
							<option value="40px">40号字</option>
						</select>
						<input type="hidden"" id="word_bold" name="word_bold"/>
						<input type="hidden"" id="word_tilt" name="word_tilt"/>
						<input type="checkbox" id="chk_word_bold" name="chk_word_bold" value=""/><strong>B</strong> 
						<input type="checkbox" id="chk_word_tilt" name="chk_word_tilt" value=""/><strong>/</strong> 
					</td>
					<td></td>
				</tr>
				<tr>
					<td>高：</td>
					<td><input type="text" id="word_height" name="word_height" />(不填默认为字体高度)</td>
					<td></td>
				</tr>
				<tr>
					<td>宽：</td>
					<td><input type="text" id="word_width" name="word_width" />(不填默认为屏幕宽度)</td>
					<td></td>
				</tr>
				<tr>
					<td>水平对齐：</td>
					<td>
						<input type="hidden" id="text_align" name="text_align"/>
						<input type="radio"" name="rad_text_align" />居左
						<input type="radio"" name="rad_text_align" />居中
						<input type="radio"" name="rad_text_align" />居右
					</td>
					<td></td>
				</tr>
				<tr>
					<td>垂直对齐：</td>
					<td>
						<input type="hidden"" id="vertical_align" name="vertical_align"/>
						<input type="radio"" name="rad_vertical_align" />居顶
						<input type="radio"" name="rad_vertical_align" />居中
						<input type="radio"" name="rad_vertical_align" />居底
					</td>
					<td></td>
				</tr>
				<tr>
					<td>滚动延迟：</td>
					<td><input type="text" id="roll_delay" name="roll_delay" />微秒</td>
					<td></td>
				</tr>
			</table>
		</div>
		<div>
			<table width="100%">
				<tr>
					<td>
						<input type="button" class="rightBtn" value="返回" onclick="returnSearch()">
						<input type="button" id="confirmBtn" class="rightBtn" value="保存" onclick="confirm()">
						<input id="addBtn" type="button" class="rightBtn" value="添加" onclick="mediaMaterialAdd()">
					</td>
				</tr>
			</table>
		</div>
		</div>
	</form>
	<div id="mould" style="display: none;">
		<table style="display: inline;">
			<tr>
				<td><font color="red">*</font>素材文件：</td>
				<td colspan="3"><input type="file" name="material_filePath"></td>
			</tr>
			<tr>
				<td><font color="red">*</font>素材名称：</td>
				<td><input type="text" name="material_name" /></td>
				<td>素材类型：</td>
				<td><select id="material_type" name="material_type">
						<option value=""></option>
						<option value="图片">图片</option>
						<option value="视频">视频</option>
						<option value="音频">音频</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>素材说明：</td>
				<td><input type="text" name="material_describe" /></td>
				<td>素材联接：</td>
				<td><input type="text" name="material_link" /></td>
			</tr>
		</table>
		<a onclick="javascript:mediaMaterialDel(this);" class="btn_close">
			<img alt="删除" title="删除" src="./image/advert/btn_close.png">
		</a> 
	</div>
</body>
</html>