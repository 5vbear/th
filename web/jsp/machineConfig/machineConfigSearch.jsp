<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.user.User"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="java.util.ArrayList"%>
<%@page import="th.entity.AdvertBean"%>
<%@page import="java.util.List"%>
<%@page import="th.com.util.Define"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ page import="java.util.HashMap"%>
    <%@ page import="th.action.report.ReportCommonAction"%>
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
String url = strContextPath + "/jsp/machineConfig/machineConfigSeach.html";
String defaultStyle = strContextPath + "/css/advert.css";

//检索结果
HashMap[] map =(HashMap[]) request.getAttribute("machineConfigList");
//检索条件
AdvertBean selectBean = (AdvertBean)request.getAttribute("select_object");
String pageNumber = (Integer) request.getAttribute("pageIdx") + "";
String frist = (String)request.getAttribute("first");
String totalPageCount = "";
int page_view = Define.VIEW_NUM;
//检索结果总数
int total_num = 0;
//当前页数
int point_num = 1;
//Table总页数
int page_total = 1;

//排序方式
String sortField = "";

//排序类型
String sortType = "";
//排序类型名称
String sortTypeName = "";
//素材类型
String adertType = "";
//机器配置名称
String machineConfigName = (String) request.getAttribute("machineConfigName");
if(machineConfigName==null){
	machineConfigName="";
}
//操作用户
String userName = (String) request.getAttribute("userName");
if(userName==null){
	userName="";
}
//添加时间From
String search_date_s =(String) request.getAttribute("search_date_s");
if(search_date_s==null){
	search_date_s="";
}
//添加时间To
String search_date_e =(String) request.getAttribute("search_date_e");
if(search_date_e==null){
	search_date_e="";
}
//文件格式
String materialFileType = "";

if (selectBean != null) {
	//检索结果总数
	total_num = selectBean.getTotal_num();
	//当前页数
	point_num = selectBean.getPoint_num();
	//Table总页数
	if (total_num != 0) {
		page_total = (total_num+page_view-1) / page_view;
	}
	//排序方式
	sortField = selectBean.getSortField();
	
	//排序类型
	sortType = selectBean.getSortType();
	//排序类型名称
	if ("ASC".equals(sortField)) {
		sortTypeName = "升序";
	}else if ("DESC".equals(sortField)) {
		sortTypeName = "降序";
	}
	//素材类型
	adertType = selectBean.getMaterialType();


	//文件格式
	materialFileType = selectBean.getMaterialFileType();
}
String result = (String)request.getAttribute("result");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<link rel="stylesheet" type="text/css" href="../../css/channel.css">
<link rel="stylesheet" type="text/css" href="../../css/machine.css">
<link rel="stylesheet" type="text/css" href="../../css/monitor.css">
<link rel="stylesheet" type="text/css" href="../../css/sdmenu.css" />
<link rel="stylesheet" type="text/css" href="../../css/style.css" />
<link rel="stylesheet" type="text/css" href="../../css/menu.css" />
<script type="text/javascript" src="<%=strContextPath%>/js/Calendar.js"></script>
<title>端机配置-端机配置检索</title>
<script language="JavaScript" type="text/javascript">

	var oCalendarChs=new PopupCalendar("oCalendarChs"); //初始化控件时,请给出实例名称:oCalendarChs
	oCalendarChs.weekDaySting=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
	oCalendarChs.monthSting=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
	oCalendarChs.oBtnTodayTitle="今天";
	oCalendarChs.oBtnCancelTitle="取消";
	oCalendarChs.Init();
	
	//页面初始化
	function onload() {
		var message = "<%=result%>";
		if (message == null || message == "" || message == "null") {
			return;
		}
		alert(message);
	}
	
	function onFold(id) {
		var vDiv = document.getElementById(id);
		vDiv.style.display = (vDiv.style.display == 'none') ? 'block' : 'none';
		var fold = document.getElementById('foldStyleId');
		if (vDiv.style.display === 'none') {
			fold.className = 'search_plus';
		} else {
			fold.className = 'search_minus';
		}
	}
	window.onload = function showtable() {
		var tablename = document.getElementById("advert_table");
		var li = tablename.getElementsByTagName("tr");
		for ( var i = 0; i < li.length; i++) {
			if (i % 2 == 0) {
				li[i].style.backgroundColor = "#E5EEFD";
				li[i].onmouseover = function() {
					this.style.backgroundColor = "#CAE8EA";
				}
				li[i].onmouseout = function() {
					this.style.backgroundColor = "#E5EEFD";
	
				}
			} else {
				li[i].style.backgroundColor = "#FFFFFF";
				li[i].onmouseover = function() {
					this.style.backgroundColor = "#CAE8EA";
				}
				li[i].onmouseout = function() {
					this.style.backgroundColor = "#FFFFFF";
	
				}
			}
		}
	}

	//checkBox联动
	function check(obj){
		if (obj.name == "child_checkBox") {
			if( obj.checked == false ) {
				document.getElementById("head_checkBox").checked = false;
                return;
            }
			var oElements = document.getElementsByName("child_checkBox");

            for( var i = 0 ; i < oElements.length ; i++ ) {
                if( oElements[i].checked == false ) {
                    return;
                }
            }
            document.getElementById("head_checkBox").checked = true;
		} else {
             var oElements = document.getElementsByName("child_checkBox");
             for( var i = 0 ; i < oElements.length ; i++ ) {
                 oElements[i].checked = obj.checked;
             }
		}
	}
	
	function goFirst(){
		<%if("true".equals(frist)){%>
		return;
		<%}%>
		window.document.form_data.pageIdx.value = 1;
		window.document.form_data.pageId.value = "<%=Define.JSP_MACHINE_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_MACHINE_SEARCH_ID%>";
		window.document.form_data.submit();
	}
	
	function goPrevious(curPageIdx){
		<%if("true".equals(frist)){%>
		return;
		<%}%>
		

		
		var pageIdx = 1;
		if(curPageIdx>1){
			pageIdx = curPageIdx - 1;
		}
		
		window.document.form_data.pageIdx.value =pageIdx;
		window.document.form_data.pageId.value = "<%=Define.JSP_MACHINE_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_MACHINE_SEARCH_ID%>";
		window.document.form_data.submit();
	}
	
	function goNext(curPageIdx){
		<%if("true".equals(frist)){%>
		return;
		<%}%>
		var pageMaxNum = document.getElementById("pageNum").value;
		var pageIdx = pageMaxNum;
		if(curPageIdx < pageMaxNum){
			pageIdx = curPageIdx + 1;
		}
		window.document.form_data.pageIdx.value =pageIdx;
		window.document.form_data.pageId.value = "<%=Define.JSP_MACHINE_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_MACHINE_SEARCH_ID%>";
		window.document.form_data.submit();

	}
	
	function goLast(){
		<%if("true".equals(frist)){%>
		return;
		<%}%>
		var pageMaxNum = document.getElementById("pageNum").value;
		window.document.form_data.pageIdx.value =pageMaxNum;
		window.document.form_data.pageId.value = "<%=Define.JSP_MACHINE_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_MACHINE_SEARCH_ID%>";
		window.document.form_data.submit();

	}
	function init() {
		var a = document.getElementById("material_name").value;
		alert('<%=point_num%>'+':'+a);
	}
	
	function review(){
		var paramers="dialogWidth:400px;DialogHeight:300px;status:no;help:no;unadorned:no;resizable:no;status:no";  
		window.document.form_data.action= "/th/jsp/advert/review.jsp";
		window.document.form_data.submit();
	}
	//检索
	function materialSearch() {
		window.document.form_data.point_num.value = 1;
		window.document.form_data.pageId.value = "<%=Define.JSP_MACHINE_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_MACHINE_SEARCH_ID%>";
		window.document.form_data.submit();
	}
	
	function materialAdd() {
		window.document.form_data.pageId.value = "<%=Define.JSP_MACHINE_ADD_ID%>";
		window.document.form_data.submit();
	}
	
	var checkflag = "false";
	function check(field) {
		if (checkflag == "false") {
			for (i = 0; i < field.length; i++) {
				field[i].checked = true;
			}
			checkflag = "true";
			return "取消";
		} else {
			for (i = 0; i < field.length; i++) {
				field[i].checked = false;
			}
			checkflag = "false";
			return "全选";
		}
	}
	
	function materialDel(){
		if(!isChecked()){
			alert("请选择要删除的机器配置!");
			return;
		}
		window.document.form_data.checkValue.value = getCheckedValue();
		window.document.form_data.funcId.value = "<%=Define.FUNC_MACHINE_DEL_ID%>";
		window.document.form_data.pageId.value = "<%=Define.JSP_MACHINE_SEARCH_ID%>";
		window.document.form_data.submit();
	}
	function isChecked(){
		var checkBoxs = document.getElementsByName("maccbox");
		var flag = false;
		for (var i = 1; i < checkBoxs.length; i++) {
			if(checkBoxs[i].checked){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	function getCheckedValue(){
		var checkBoxs = document.getElementsByName("maccbox");
		var checkedValue = "";
		for (var i = 1; i < checkBoxs.length; i++) {
			if(checkBoxs[i].checked){
				if(checkedValue == ""){
					checkedValue += checkBoxs[i].value;
				}else{
					checkedValue = checkedValue + "," + checkBoxs[i].value;
				}
			}
		}
		return checkedValue;
	}
</script>
</head>
<body onload="onload();">
<div class="search_title"><span>端机配置-端机配置检索</span></div>
<table><tr style ="heigt:30px"></tr></table>
	<form name="form_data" action="<%=url %>" method="post">
		<input type="hidden" name="pageId" value="">
		<input type="hidden" name="funcId" value="">
		<input type="hidden" name="materialId" value="">
		<input type="hidden" name="materialArr" value="">
		<input type="hidden" name="adertType" value="">
		<input type="hidden" name="point_num" value="<%=point_num %>">
		<input type="hidden" name="pageIdx" value ="">
		<input type="hidden" name="checkValue" value ="">
		
		<input type="hidden" id="pageNum" name="pageNum" value="<%=request.getAttribute("pageNum") %>" /> 
		<div class="search_title">
			<div id="foldStyleId" class="search_minus" onclick="onFold('foldId');" ></div>
			<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
		</div>
		<div id="foldId" style="height: 90px; line-height: 26px;background-color:#B2DFEE;">
			<table>
				<tr>
					<td>配置名称：</td>
					<td><input type="text" name="machineConfigName" value="<%=machineConfigName%>"></td>
					<td>创建用户：</td>
					<td><input type="text" name="userName" value="<%=userName%>"></td>					
					<td>创建开始时间：</td>
					<td>
						<input type="text" size="8" name="search_date_s" maxlength="10" style="ime-mode:disabled" 
							value="<%=search_date_s%>"readonly="true" onclick="getDateString(this,oCalendarChs)">
					</td>
					<td>创建结束时间：</td>
					<td>
						<input type="text" size="8" name="search_date_e" maxlength="10" style="ime-mode:disabled" 
							value="<%=search_date_e%>" readonly="true" onclick="getDateString(this,oCalendarChs)">
					</td>
					<td>
						<input type="button" class="search_button" value="搜索" onclick="materialSearch();"/>
					</td>
				</tr>
			</table>
		</div>
		<p></p>
		<div>
			<div class="search_data">
				<span style="height: 30px; width: 100px; line-height: 26px">&nbsp;&nbsp;数据</span>
			</div>
			<div style="overflow: auto;">
				<table id="advert_table">
					<tr>
						<th class="th_tianhe" scope="col" style="width:2%"><input type=checkbox name=maccbox
								onClick="this.value=check(this.form.maccbox)" value="" /></th>
						<th class="th_tianhe">配置名称</th>
						<th class="th_tianhe">配置描述</th>
						<th class="th_tianhe">配置类别</th>
						<th class="th_tianhe">项类别</th>
						<th class="th_tianhe">项名称</th>
						<th class="th_tianhe">项值</th>
						<th class="th_tianhe">创建用户</th>
						<th class="th_tianhe">创建时间</th>
						
					</tr>
					<% 
					if (map != null&&map.length>0) {
						total_num=map.length;
						totalPageCount = ReportCommonAction.getTotalPageCount(map.length)+"";
						int pageIdx = Integer.parseInt(request.getAttribute("pageIdx")
								.toString());
						if (pageIdx < 1) {
							pageIdx = 1;
						}
						for (int i = 10 * (pageIdx - 1); i < map.length
								&& i < 10 * (pageIdx); i++) {
							String type="";
							String type2="";
							String type3="";
							String value ="";
							String startTime = (String)map[i].get("MACHINE_START_TIME");
							String endTime = (String)map[i].get("MACHINE_SHUTDOWN_TIME");
							String protect = (String)map[i].get("SCREEN_PROTECT_TIME");
							String duration = (String)map[i].get("SCREEN_COPY_DURATION");
							String interval = (String)map[i].get("SCREEN_COPY_INTERVAL");
							String dir = (String)map[i].get("WRITE_PROTECT_DIRS");
							String server_url = (String)map[i].get("SERVER_URL");
							String server_ip = (String)map[i].get("FTP_SERVER_IP");
							String itemname = (String)map[i].get("ITEMNAME");
							String time = (String)map[i].get("COMMAND_TIME");
							if("0".equals(itemname)){
								type= "时间相关";
								type2= "开关机";	
								type3= "开机";
								value =startTime.substring(10,19);
							}else if("1".equals(itemname)){
								type= "时间相关";
								type2= "开关机";	
								type3= "关机";
								value =endTime.substring(10,19);
							}else if("2".equals(itemname)){
								type= "时间相关";
								type2= "屏保";	
								type3= "屏保";
								value =protect;
							}else if("3".equals(itemname)){
								type= "时间相关";
								type2= "截屏";	
								type3= "截屏时间";
								value =duration;
							}else if("4".equals(itemname)){
								type= "时间相关";
								type2= "截屏";	
								type3= "截屏间隔时间";
								value =interval;
							}else if("5".equals(itemname)){
								type= "路径相关";
								type2= "路径相关";	
								type3= "写保护目录";
								value =dir;
							}else if("6".equals(itemname)){
								type= "路径相关";
								type2= "路径相关";	
								type3= "应用服务器地址";
								value =server_url;
							}else if("7".equals(itemname)){
								type= "路径相关";
								type2= "路径相关";	
								type3= "FTP服务器IP";
								value =server_ip;
							}else if("8".equals(itemname)){
								type= "时间相关";
								type2= "屏保";	
								type3= "心跳时间";
								value =time;
							}else{
								type= "默认配置";
								type2= "-";	
								type3= "所有";
								value ="默认值";
								
							}
						
					%>
					<tr><td class='td_tianhe'>
						<input type='checkbox' name='maccbox' value="<%=map[i].get("MODULE_ID")%>"/>
					</td>
						<td class="td_tianhe"><%=map[i].get("MODULE_NAME")%></td>
						<td class="td_tianhe"><%=map[i].get("DESCRIPTION")%></td>
						<td class="td_tianhe"><%=type%></td>
						<td class="td_tianhe"><%=type2%></td>
						<td class="td_tianhe"><%=type3%></td>
						<td class="td_tianhe"><%=value%></td>
						<td class="td_tianhe"><%=map[i].get("UNAME")%></td>
						<td class="td_tianhe"><%=map[i].get("OPERATETIME")%></td>
					</tr>
					<%		
						}
					}
					%>
				</table>
				<div>
				<table height="20" width="100%">
					<tr>
						<td>
							<input type="button" id="first" class="x-first-page" style="margin-left: 5px" onclick="goFirst()" /> 
							<input type="button" id="previous" class="x-previous-page" style="margin-left: -2px" onclick="goPrevious(<%=request.getAttribute("pageIdx") %>)" /> 
							<input type="button" id="nextPage" 	class="x-next-page" style="margin-left: -4px" onclick="goNext(<%=request.getAttribute("pageIdx") %>)" /> 
							<input type="button" id="lastPage" 	class="x-last-page" style="margin-left: -2px" onclick="goLast()" />[当前第<%=pageNumber%>页/共<%=totalPageCount%>页][每页<%=page_view%>条][共<%=total_num%>条]

						</td>
						<td width="300">
							<input type="button" class="rightBtn" value="添加" onclick="materialAdd();"/>
							<input type="button" class="rightBtn" value="删除" onclick="materialDel();"/>
						</td>
					</tr>
				</table>
				</div>
				
			</div>
		</div>
	</form>
</body>
</html>