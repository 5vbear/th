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
String result = (String)request.getAttribute("result");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<title>广告管理-节目单组添加</title>
<script language="JavaScript" type="text/javascript">
	//页面初始化
	window.onload = function doload() {
		var message = "<%=result%>";
		if (message == null || message == "" || message == "null") {
			return;
		}
		alert(message);
	}

	//返回检索画面
	function returnSearch() {
		window.document.material_form.pageId.value = "<%=Define.JSP_PROGRAMLISTGROUP_SEARCH_ID%>";
		window.document.material_form.funcId.value = "";
		window.document.material_form.submit();
	}
	//节目单选取
	function chooseProgramlist() {
		var paramers="dialogWidth:400px;DialogHeight:300px;status:no;help:no;unadorned:no;resizable:no;status:no;scroll:no;";  
		var url = "<%=url %>" + "?pageId=jsp_sub_window_id&funcId=func_programlist_subwindow_id";
		var ret=window.showModalDialog(url,'',paramers);  
		if (ret == null) {
			return;
		}
		document.getElementById("group").style.display = "block";
		//清除Table行
		var tableObj = document.getElementById("advert_table");
		var rowsCnt = tableObj.rows.length;
		for(var i = rowsCnt - 1; i > 0; i--) {
			tableObj.deleteRow(i);
		}
				
		for(var i=0; i < ret.length; i++) {
			var newTR = tableObj.insertRow();
			//节目单名称
			var newTD_0=newTR.insertCell(0);
			newTD_0.className = "td_tianhe";
			newTD_0.innerHTML = "<input type='hidden' name='programlistId' value='"+ret[i].id+"'>"+ret[i].name;
			//节目单描述
			var newTD_1=newTR.insertCell(1);
			newTD_1.className = "td_tianhe";
			newTD_1.innerHTML = ret[i].describe;
			//节目单播放时长
			var newTD_2=newTR.insertCell(2);
			newTD_2.className = "td_tianhe";
			newTD_2.innerHTML = ret[i].playTime;
			//节目单布局类型
			var newTD_3=newTR.insertCell(3);
			newTD_3.className = "td_tianhe";
			if(ret[i].layoutScreen == "1"){
				newTD_3.innerHTML = "<img alt='一分屏' src='./image/advert/single_small.png'>(一分屏)";	
			}else if(ret[i].layoutScreen == "2"){
				newTD_3.innerHTML = "<img alt='二分屏' src='./image/advert/double_small.png'>(二分屏)";	
			}else if(ret[i].layoutScreen == "4"){
				newTD_3.innerHTML = "<img alt='四分屏' src='./image/advert/four_small.png'>(四分屏)";	
			}else {
				newTD_3.innerHTML = ret[i].layoutScreen;	
			}
			//节目单动作说明
			var newTD_4=newTR.insertCell(4);
			newTD_4.className = "td_tianhe";
			//newTD_4.innerHTML = "<input type='text' name='programlist_action' value=''>";
			newTD_4.innerHTML = "<select name='programlist_action' value=''><option value='0'>无动作</option>		<option value='1'>从左到右飞入</option><option value='2'>从右到左飞入</option><option value='3'>从上到下飞入</option><option value='4'>从下到上飞入</option></select>";
		}
	}
	
	//保存
	function confirm() {
		// check
		//节目单组名称
		if(document.getElementById("programlistGroup_name").value == "") {
			alert("节目单组名称不能为空！");
			return;
		}
		var tableObj = document.getElementById("advert_table");
		if (tableObj.rows.length == 1) {
			var confirm = confirm("该节目单组没有添加任何节目单，是否继续添加？");
			if(!confirm){
				return;
			}
		} 
		
		window.document.material_form.pageId.value = "<%=Define.JSP_PROGRAMLISTGROUP_ADD_ID%>";
		window.document.material_form.funcId.value = "<%=Define.FUNC_PROGRAMLISTGROUP_ADD_ID%>";
		window.document.material_form.submit();
	}

</script>
</head>
<body>
<div class="search_title"><span>广告管理-节目单组添加</span></div>
<table><tr style ="heigt:30px"></tr></table>
	<form name="material_form" action="<%=url %>" method="post" >
		<input type="hidden" name="pageId" value="">
		<input type="hidden" name="funcId" value="">
		<table>
			<tr>
				<td>
					<font color="red">*</font>节目单组名称:
				</td>
				<td>
					<input type="text" id="programlistGroup_name" name="programlistGroup_name" />
				</td>
				<td>
					节目列表说明:
				</td>
				<td>
					<input type="text" id="programlistGroup_desc" name="programlistGroup_desc" />
				</td>
				<td>
					循环周期:
				</td>
				<td>
					<input type="text" id="programlistGroup_loop" name="programlistGroup_loop" />
				</td>
			</tr>
			<tr>
				<td>
					<input type="button" value="选择节目单" onclick="chooseProgramlist()">
				</td>
			</tr>
		</table>
		<div id="group" style="overflow: auto; display: none;">
			<table id="advert_table">
				<tr>
					<th class="th_tianhe">节目单名称</th>
					<th class="th_tianhe">节目单描述</th>
					<th class="th_tianhe">节目单播放时长</th>
					<th class="th_tianhe">节目单布局类型</th>
					<th class="th_tianhe">节目单动作说明</th>
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