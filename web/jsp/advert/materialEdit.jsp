<%@page import="java.util.List"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.user.User"%>
<%@page import="org.apache.commons.logging.Log"%>
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
String colorPickerStyle = strContextPath + "/css/jquery.colorpicker.css";
String jqueryJs = strContextPath + "/js/jquery-1.6.1.js";
String colorPickerJs = strContextPath + "/js/jquery.colorpicker.js";
request.setCharacterEncoding("UTF-8");

//素材ID
String materialId = "";
//素材分组
String materialGroup = "";
//素材类型
String adertType = "";
//素材名称
String materialName = "";
//素材文件类型
String materialType = "";
//素材文件路径
String materialFilePath = "";
//文件服务器路径
String serviceFilePath = "";
//素材描述
String desctibe = "";
//素材联接
String materialLink = "";

//字幕名称
String subtitlesName = "";
//字幕描述
String subtitlesDescribe = "";
//字幕内容
String roll_word = "";;
//背景图片
String background_filePath = "";
//背景颜色
String background_color = "";
//文字颜色
String word_color = "";
//文字属性
String word_type = "";
String word_size = "";
String word_bold = "";
String word_tilt = "";
//高度
String word_height = "";
//宽度
String word_width = "";
//垂直对齐方式
String vertical_align = "";
//水平对齐方式
String text_align = "";
//延迟播放时间　
String roll_delay = "";


//检索结果
AdvertBean editBean = (AdvertBean)request.getAttribute("resultBean");
if (editBean != null) {
	//素材ID
	materialId = editBean.getMaterialId();
	//素材分组
	materialGroup = editBean.getMaterial_group();
	//素材类型
	adertType = editBean.getAdvert_type();
	//素材名称
	materialName = editBean.getMaterialName();
	//素材文件类型
	materialType = editBean.getMaterialType();
	//素材文件路径
	if (editBean.getMaterial_filePath() != null && editBean.getMaterial_filePath().length > 0) {
		materialFilePath = editBean.getMaterial_filePath()[0];
	}
	//文件服务器路径
	if (editBean.getServiceFilePath() != null && editBean.getServiceFilePath().length > 0) {
		serviceFilePath = editBean.getServiceFilePath()[0];
	}
	//素材描述
	if (editBean.getMaterial_describe() != null && editBean.getMaterial_describe().length > 0) {
		desctibe = editBean.getMaterial_describe()[0];
	}
	//素材联接
	if (editBean.getMaterial_link() != null && editBean.getMaterial_link().length > 0) {
		materialLink = editBean.getMaterial_link()[0];
	}
	//字幕名称
	subtitlesName = editBean.getSubtitles_name();
	//字幕描述
	subtitlesDescribe = editBean.getSubtitles_describe();
	//字幕内容
	roll_word = editBean.getRoll_word();;
	//背景图片
	background_filePath = editBean.getBackground_filePath();
	//背景颜色
	background_color = editBean.getBackground_color();
	//文字颜色
	word_color = editBean.getWord_color();
	//文字属性
	String[] word_attribute = editBean.getWord_attribute().split(";");
	if (word_attribute.length == 4) {
		word_type = word_attribute[3];
		word_size = word_attribute[2];
		word_bold = word_attribute[1];
		word_tilt = word_attribute[0];
	}
	//高度
	word_height = editBean.getWord_height();
	//宽度
	word_width = editBean.getWord_width();
	//垂直对齐方式
	vertical_align = editBean.getVertical_align();
	//水平对齐方式
	text_align = editBean.getText_align();
	//延迟播放时间　
	roll_delay = editBean.getRoll_delay();
}

String result = (String)request.getAttribute("result");
//素材分组
List groupList = (List)request.getAttribute("GROUP_LIST");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=colorPickerStyle %>" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<title>广告管理-素材编辑</title>
<script type="text/javascript" src="<%=jqueryJs %>"></script>
<script type="text/javascript" src="<%=colorPickerJs %>"></script>
<script language="JavaScript" type="text/javascript">
	var numType = new RegExp(/^[0-9]*$/);
	//页面初始化
	function onload() {
		var obj = window.document.material_form.advert_type;
		materialTypeChange(obj);
		var message = "<%=result%>";
		if (message == null || message == "" || message == "null") {
			return;
		}
		alert(message);
	}
	//返回检索画面
	function returm() {
		window.document.material_form.pageId.value = "<%=Define.JSP_MATERIAL_SEARCH_ID%>";
		window.document.material_form.submit();
	}

	function materialTypeChange(obj) {
		var type = obj.value;
		var div1 = document.getElementById("material1");
		var div2 = document.getElementById("subtitles");
		if (type=="1") {
			div1.style.display = "block";
			div2.style.display = "none";
		} else {
			div1.style.display = "none";
			div2.style.display = "block";
		}
	}

	//媒体素材添加
	var cnt = 1;
	function mediaMaterialAdd() {
		cnt++;
		var mouldInner = document.getElementById("mould").innerHTML;
		var divInner = document.getElementById("addDiv");
		var addDiv = document.createElement("div"); 
		addDiv.setAttribute("id","material"+cnt); 
		addDiv.style.display = "block";
		addDiv.style.borderStyle = "solid";
		addDiv.style.borderWidth = "1px";
		addDiv.innerHTML = mouldInner + "<p></p>";
		//divInner.innerHTML = divInner.innerHTML + addDiv.innerHTML + "<p></p>";
		divInner.appendChild(addDiv);

		document.getElementById("delBtn").style.visibility = "visible";
	}

	//媒体素材删除
	function mediaMaterialDel() {
		if (cnt > 1) {
			var parentDiv = document.getElementById("addDiv");
			var childDiv = document.getElementById("material"+cnt);
			parentDiv.removeChild(childDiv);
			cnt--; 
		}
		if(cnt == 1) {
			document.getElementById("delBtn").style.visibility = "hidden";
		}
	}
	
	function openUpload_(){  
		openUpload(null,'JPG,GIF,JPEG,PNG','5',callback);  
	} 
	function callback(realName,saveName,maxSize){
		document.getElementById("mater_file").value=saveName;
	}
	function openUpload(functionId,fileType,maxSize,callback){  
	    var url = root+"/UploadServlet?kinou_id=1";  
	    if(functionId!=null){  
	        url = url + "functionId="+functionId+"&";  
	    }  
	    if(fileType!=null){  
	        url = url + "fileType="+fileType+"&";  
	    }  
	    if(maxSize!=null){  
	        url = url + "maxSize="+maxSize;  
	    }  
	    var win = window.showModalDialog(url,"","dialogWidth:300px;dialogHeight:150px;scroll:no;status:no");  
	    if(win != null){  
	        var arrWin = win.split(",");  
	        callback(arrWin[0],arrWin[1],arrWin[2]);  
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
		var type = document.getElementById("advert_type").value
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
		
		var actionUrl = "<%=url%>" + "?pageId="+"<%=Define.JSP_MATERIAL_EDIT_ID%>" + "&funcId=" + "<%=Define.FUNC_MATERIAL_UPDATE_ID%>";
		window.document.material_form.action = actionUrl;
		//window.document.material_form.pageId.value = "<%=Define.JSP_MATERIAL_EDIT_ID%>";
		//window.document.material_form.funcId.value = "<%=Define.FUNC_MATERIAL_UPDATE_ID%>";
		//window.document.material_form.encoding = "multipart/form-data";
		window.document.material_form.submit();
	}

	//返回
	function returnSearch() {
		window.document.material_form.pageId.value = "<%=Define.JSP_MATERIAL_SEARCH_ID%>";
		window.document.material_form.funcId.value = "";
		window.document.material_form.submit();
	}
	//文件编辑
	function fileEdit(obj) {
		var file = document.getElementById("file_upload");
		if (obj.value=="编辑"){
			obj.value="取消";
			file.style.display = "block";
		}else {
			obj.value="编辑";
			file.style.display = "none";
			file.select();  
		    document.execCommand('Delete');
		}
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
<body onload="onload();">
<div class="search_title"><span>广告管理-素材编辑</span></div>
<table><tr style ="heigt:30px"></tr></table>
	<form name="material_form" action="<%=url %>" method="post" >
		<input type="hidden" name="pageId" value="">
		<input type="hidden" name="funcId" value="">
		<input type="hidden" name="adertType" value="<%=adertType %>">
		<input type="hidden" name="materialId" value="<%=materialId %>">
		<br/>
		<table>
			<tr>
				<td>素材分组：</td>
				<td><input type="button" value="自定义组" ></td>
				<td><select id="material_group" name="material_group" value="<%=materialGroup %>" disabled="disabled">
					<%
					if (groupList != null){
						for (int i = 0; i < groupList.size(); i++) {
							AdvertBean bean = (AdvertBean)groupList.get(i);
							if(materialGroup.equals(bean.getMaterial_group())) {
					%>
								<option value="<%=bean.getMaterial_group() %>" selected="selected"><%=bean.getGroupName() %></option>
					<% 
							} else {
					%>
								<option value="<%=bean.getMaterial_group() %>"><%=bean.getGroupName() %></option>
					<% 
							}
						}
					}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td>素材分类：</td>
				<td colspan="2"><select id="advert_type" name="advert_type" value="<%=adertType%>" disabled="disabled">
						<option value=""></option>
						<option value="1" 
							<%if("1".equals(adertType)){%>selected<% } %>
						>媒体素材</option>
						<option value="2" 
							<%if("2".equals(adertType)){%>selected<% } %>
						>字幕</option>
					</select>
				</td>
			
		</table>
		<div id="material1" style="display: block;border-style: solid ;border-width: 1px;">
			<table>
				<tr>
					<td>素材文件：</td>
					<td colspan="3">
						<input type="hidden" id="service_filePath" name="service_filePath" value="<%=serviceFilePath%>">
						<input type="text" id="material_filePath" name="material_filePath" disabled="disabled" value="<%=materialFilePath%>">
					</td>
				</tr>
				<tr>
					<td>素材名称：</td>
					<td><input type="text" id="material_name" name="material_name" value="<%=materialName%>"/></td>
					<td>素材类型：</td>
					<td><select id="material_type" name="material_type">
							<option value=""></option>
							<option value="图片"
								<%if("图片".equals(materialType)) {%>selected="selected"<%} %>
							>图片</option>
							<option value="视频"
								<%if("视频".equals(materialType)) {%>selected="selected"<%} %>
							>视频</option>
							<option value="音频"
								<%if("音频".equals(materialType)) {%>selected="selected"<%} %>
							>音频</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>素材说明：</td>
					<td><input type="text" id="material_describe" name="material_describe" value="<%=desctibe%>"/></td>
					<td>素材链接：</td>
					<td><input type="text" id="material_link" name="material_link" value="<%=materialLink %>"/></td>
				</tr>
			</table>
		</div>
		<div id="subtitles" style="display: none; border-style: solid ;border-width: 1px;">
			<table>
				<tr>
					<td>素材名称：</td>
					<td><input type="text" id="subtitles_name" name="subtitles_name" value="<%=subtitlesName%>"/>
						<input type="button" value="效果预览" onclick="preview()"></td>
					<td></td>
				</tr>
				<tr>
					<td>素材描述：</td>
					<td><input type="text" id="subtitles_describe" name="subtitles_describe" value="<%=subtitlesDescribe%>"/></td>
					<td></td>
				</tr>
				<tr>
					<td>滚动文字：</td>
					<td><input type="text" id="roll_word" name="roll_word" value="<%=roll_word%>"/></td>
					<td></td>
				</tr>
				<!--<tr>
					<td>背景图片：</td>
					<td>
						<input type="file" name="background_filePath" value="<%=background_filePath%>">
						<input type="text" id="background_filePath" name="background_filePath" value="<%=background_filePath%>" disabled="disabled">
					</td>
					<td></td>
				</tr>
				-->
				<tr>
					<td>背景颜色：</td>
					<td><input type="text" id="background_color" name="background_color" value="<%=background_color%>"/>
						<input type="button" id="background_color_btn" value="选色">
					</td>
					<td></td>
				</tr>
				<tr>
					<td>字体颜色：</td>
					<td><input type="text" id="word_color" name="word_color" value="<%=word_color%>"/>
						<input type="button" id="word_color_btn" value="选色">
					</td>
					<td></td>
				</tr>
				<tr>
					<td>字体属性：</td>
					<td><select id="word_type" name="word_type" value="<%=word_type%>">
							<option value="Arial"
								<%if("Arial".equals(word_type)){%>selected<% } %>
							>隶书</option>
							<option value="KaiTi" 
								<%if("KaiTi".equals(word_type)){%>selected<% } %>
							>楷书</option>
							<option value="SimSun" 
								<%if("SimSun".equals(word_type)){%>selected<% } %>
							>宋体</option>
						</select>
						<select id="word_size" name="word_size" value="<%=word_size%>">
							<option value="4px" 
								<%if("4px".equals(word_size)){%>selected<% } %>
							>4号字</option>
							<option value="5px" 
								<%if("5px".equals(word_size)){%>selected<% } %>
							>5号字</option>
							<option value="6px" 
								<%if("6px".equals(word_size)){%>selected<% } %>
							>6号字</option>
							<option value="7px" 
								<%if("7px".equals(word_size)){%>selected<% } %>
							>7号字</option>
							<option value="8px" 
								<%if("8px".equals(word_size)){%>selected<% } %>
							>8号字</option>
							<option value="9px" 
								<%if("9px".equals(word_size)){%>selected<% } %>
							>9号字</option>
							<option value="10px" 
								<%if("10px".equals(word_size)){%>selected<% } %>
							>10号字</option>
							<option value="11px" 
								<%if("11px".equals(word_size)){%>selected<% } %>
							>11号字</option>
							<option value="12px" 
								<%if("12px".equals(word_size)){%>selected<% } %>
							>12号字</option>
							<option value="13px" 
								<%if("13px".equals(word_size)){%>selected<% } %>
							>13号字</option>
							<option value="14px" 
								<%if("14px".equals(word_size)){%>selected<% } %>
							>14号字</option>
							<option value="15px" 
								<%if("15px".equals(word_size)){%>selected<% } %>
							>15号字</option>
							<option value="16px" 
								<%if("16px".equals(word_size)){%>selected<% } %>
							>16号字</option>
							<option value="17px" 
								<%if("17px".equals(word_size)){%>selected<% } %>
							>17号字</option>
							<option value="18px" 
								<%if("18px".equals(word_size)){%>selected<% } %>
							>18号字</option>
							<option value="19px" 
								<%if("19px".equals(word_size)){%>selected<% } %>
							>19号字</option>
							<option value="20px" 
								<%if("20px".equals(word_size)){%>selected<% } %>
							>20号字</option>
							<option value="30px" 
								<%if("30px".equals(word_size)){%>selected<% } %>
							>30号字</option>
							<option value="40px" 
								<%if("40px".equals(word_size)){%>selected<% } %>
							>40号字</option>
						</select>
						<input type="hidden"" id="word_bold" name="word_bold"/>
						<input type="hidden"" id="word_tilt" name="word_tilt"/>
						<input type="checkbox" id="chk_word_bold" name="chk_word_bold" value="<%=word_bold %>"
						<%if ("bold".equals(word_bold)) {%>
							checked="checked"
						<% }%>
						/><strong>B</strong> 
						<input type="checkbox" id="chk_word_tilt" name="chk_word_tilt" value="<%=word_tilt %>" 
						<%if ("italic".equals(word_tilt)) {%>
							checked="checked"
						<% }%>
						/><strong>/</strong> 
					</td>
					</td>
					<td></td>
				</tr>
				<tr>
					<td>高：</td>
					<td><input type="text" id="word_height" name="word_height" value="<%=word_height%>"/>(不填默认为字体高度)</td>
					<td></td>
				</tr>
				<tr>
					<td>宽：</td>
					<td><input type="text" id="word_width" name="word_width" value="<%=word_width%>"/>(不填默认为屏幕宽度)</td>
					<td></td>
				</tr>
				<tr>
					<td>水平对齐：</td>
					<td>
						<input type="hidden" id="text_align" name="text_align"/>
						<input type="radio" id="rad_text_align" name="rad_text_align" value="1" 
						<%if ("1".equals(text_align)) {%>
							checked="checked"
						<% }%>
						/>居左
						<input type="radio"" name="rad_text_align" value="2" 
						<%if ("2".equals(text_align)) {%>
							checked="checked"
						<% }%>
						/>居中
						<input type="radio"" name="rad_text_align" value="3"
						<%if ("3".equals(text_align)) {%>
							checked="checked"
						<% }%>
						/>居右
					</td>
					<td></td>
				</tr>
				<tr>
					<td>垂直对齐：</td>
					<td>
						<input type="hidden" id="vertical_align" name="vertical_align"/>
						<input type="radio" id="rad_vertical_align" name="rad_vertical_align" value="1" 
						<%if ("1".equals(vertical_align)) {%>
							checked="checked"
						<% }%>
						/>居顶
						<input type="radio"" name="rad_vertical_align" value="2" 
						<%if ("2".equals(vertical_align)) {%>
							checked="checked"
						<% }%>
						/>居中
						<input type="radio"" name="rad_vertical_align" value="3" 
						<%if ("3".equals(vertical_align)) {%>
							checked="checked"
						<% }%>
						/>居底
					</td>
					<td></td>
				</tr>
				<tr>
					<td>滚动延迟：</td>
					<td><input type="text" id="roll_delay" name="roll_delay" value="<%=roll_delay%>"/>微秒</td>
					<td></td>
				</tr>
			</table>
		</div>
		<div>
			<table width="100%">
				<tr>
					<td>
						<input type="button" class="rightBtn" value="返回" onclick="returnSearch()">
						<input type="button" class="rightBtn" value="保存" onclick="confirm()">
					</td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>