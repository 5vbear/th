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

//节目单组ID
String programlistGroupId = "";
//节目单组名称
String programlistGroupName = "";
//节目列表说明
String programlistGroupDesc = "";
//循环周期
String programlistGroupLoop = "";

//检索结果
AdvertBean editBean = (AdvertBean)request.getAttribute("resultBean");
if (editBean != null) {
	//节目单组ID
	programlistGroupId = editBean.getProgramlistGroupId();
	//节目单组名称
	programlistGroupName = editBean.getProgramlistGroupName();
	//节目列表说明
	programlistGroupDesc = editBean.getProgramlistGroup_desc();
	//循环周期
	if (editBean.getProgramlistGroup_loop() == 0) {
		programlistGroupLoop = "";
	} else {
		programlistGroupLoop = editBean.getProgramlistGroup_loop() + "";
	}
}
//节目单组
List list = (List)request.getAttribute("groupList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<title>广告管理-节目单组编辑</title>
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
		
		window.document.material_form.pageId.value = "<%=Define.JSP_PROGRAMLISTGROUP_EDIT_ID%>";
		window.document.material_form.funcId.value = "<%=Define.FUNC_PROGRAMLISTGROUP_UPDATE_ID%>";
		window.document.material_form.submit();
	}

</script>
</head>
<body>
<div class="search_title"><span>广告管理-节目单组编辑</span></div>
<table><tr style ="heigt:30px"></tr></table>
	<form name="material_form" action="<%=url %>" method="post" >
		<input type="hidden" name="pageId" value="">
		<input type="hidden" name="funcId" value="">
		<input type="hidden" name="programlistGroupId" value="<%=programlistGroupId %>">
		<table>
			<tr>
				<td>
					<font color="red">*</font>节目单组名称:
				</td>
				<td>
					<input type="text" id="programlistGroup_name" name="programlistGroup_name" value="<%=programlistGroupName %>"/>
				</td>
				<td>
					节目列表说明:
				</td>
				<td>
					<input type="text" id="programlistGroup_desc" name="programlistGroup_desc" value="<%=programlistGroupDesc %>" />
				</td>
				<td>
					循环周期:
				</td>
				<td>
					<input type="text" id="programlistGroup_loop" name="programlistGroup_loop" value="<%=programlistGroupLoop%>" />
				</td>
			</tr>
			<tr>
				<td>
					<input type="button" value="节目单选取" onclick="chooseProgramlist()">
				</td>
			</tr>
		</table>
		<div style="overflow: auto;">
			<table id="advert_table">
				<tr>
					<th class="th_tianhe">节目单名称</th>
					<th class="th_tianhe">节目单描述</th>
					<th class="th_tianhe">节目单播放时长</th>
					<th class="th_tianhe">节目单布局类型</th>
					<th class="th_tianhe">节目单动作说明</th>
				</tr>
				<%
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						AdvertBean bean = (AdvertBean)list.get(i);
						//节目单ID
						String programlistId = bean.getProgramlistId();
						//节目单名称
						String programlistName = bean.getProgramlistName();
						//节目单描述
						String programlistDescribe = bean.getProgramlistDescribe();
						//节目单播放时长
						int plauTime = bean.getProgramlistPlayTime();
						//节目单布局类型
						String layoutScreen = bean.getLayoutScreen();
						if("1".equals(bean.getLayoutScreen())){
							layoutScreen = "<img alt='一分屏' src='./image/advert/single_small.png'>(一分屏)";	
						}else if("2".equals(bean.getLayoutScreen())){
							layoutScreen = "<img alt='二分屏' src='./image/advert/double_small.png'>(二分屏)";	
						}else if("4".equals(bean.getLayoutScreen())){
							layoutScreen = "<img alt='四分屏' src='./image/advert/four_small.png'>(四分屏)";	
						}else {
							layoutScreen = bean.getLayoutScreen();	
						}
						//节目单动作说明
						String programlistAction = bean.getProgramlistAction();
				%>
						<tr>
							<td class="td_tianhe"><input type="hidden" name="programlistId" value="<%=programlistId %>"><%=programlistName %></td>
							<td class="td_tianhe"><%=programlistDescribe %></td>
							<td class="td_tianhe"><%=plauTime %></td>
							<td class="td_tianhe"><%=layoutScreen %></td>
							
							<!--  <td class="td_tianhe"><input type='text' name='programlist_action' value='<%=programlistAction %>'></td> -->
							<td class="td_tianhe"><select name="programlist_action" value="<%=programlistAction %>">
							<option value="0" <%if("0".equals(programlistAction)){%>selected<% } %>>无动作</option>
							<option value="1" <%if("1".equals(programlistAction)){%>selected<% } %>>从左到右飞入</option>
							<option value="1" <%if("2".equals(programlistAction)){%>selected<% } %>>从右到左飞入</option>
							<option value="1" <%if("3".equals(programlistAction)){%>selected<% } %>>从上到下飞入</option>
							<option value="1" <%if("4".equals(programlistAction)){%>selected<% } %>>从下到上飞入</option>
							</select>
						</tr>
				<% 
					}
				}
				%>
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