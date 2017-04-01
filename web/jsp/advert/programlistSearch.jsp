<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.user.User"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="java.util.ArrayList"%>
<%@page import="th.entity.AdvertBean"%>
<%@page import="java.util.List"%>
<%@page import="th.com.util.Define"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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

//检索结果
List resultBeans = (ArrayList)request.getAttribute("resultList");
//检索条件
AdvertBean selectBean = (AdvertBean)request.getAttribute("select_object");
//检索结果总数
int total_num = 0;
//当前页数
int point_num = 1;
//Table总页数
int page_total = 1;
//每页显示行数
int page_view = Define.VIEW_NUM;
//排序方式
String sortField = "";
//排序方式名称
String sortFieldName = "";
//排序类型
String sortType = "";
//排序类型名称
String sortTypeName = "";
//节目单名称
String programlistName = "";
//审核状态
String auditStatus = "";
//创建用户
String operator_name = "";
//添加时间From
String search_date_s = "";
//添加时间To
String search_date_e = "";

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
	//排序方式名称
	if ("BILL_NAME".equals(sortField)) {
		sortFieldName = "节目单名称";
	}else if ("NAME".equals(sortField)) {
		sortFieldName = "创建用户";
	}else if ("OPERATETIME".equals(sortField)) {
		sortFieldName = "创建时间";
	}else if ("STATUS".equals(sortField)) {
		sortFieldName = "审核状态";
	}
	//排序类型
	sortType = selectBean.getSortType();
	//排序类型名称
	if ("ASC".equals(sortField)) {
		sortTypeName = "升序";
	}else if ("DESC".equals(sortField)) {
		sortTypeName = "降序";
	}
	//节目单名称
	programlistName = selectBean.getProgramlistName();
	//审核状态
	auditStatus = selectBean.getAuditStatus();
	//创建用户
	operator_name = selectBean.getOperator();
	//添加时间From
	search_date_s = selectBean.getProgramlistAddDateFrom();
	//添加时间To
	search_date_e = selectBean.getProgramlistAddDateTo();
}

String result = (String)request.getAttribute("result");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<script type="text/javascript" src="<%=strContextPath%>/js/Calendar.js"></script>
<title>广告管理-节目单管理</title>
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
	
	//节目单添加
	function programlistAdd() {
		window.document.form_data.pageId.value = "<%=Define.JSP_PROGRAMLIST_ADD_ID%>";
		window.document.form_data.submit();
	}
	
	//节目单检索
	function programlistSearch() {
		window.document.form_data.point_num.value = 1;
		window.document.form_data.pageId.value = "<%=Define.JSP_PROGRAMLIST_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_PROGRAMLIST_SEARCH_ID%>";
		window.document.form_data.submit();
	}
	//审核
	function programlistAudit(id, type) {
		var result = confirm("审核确认");
		if (result) {
			window.document.form_data.pageId.value = "<%=Define.JSP_PROGRAMLIST_SEARCH_ID%>";
			window.document.form_data.funcId.value = "<%=Define.FUNC_PROGRAMLIST_AUDIT_ID%>";
			window.document.form_data.programlistId.value = id;
			window.document.form_data.submit();
		}
	}
	//编辑
	function programlistEdit(id, type) {
		window.document.form_data.pageId.value = "<%=Define.JSP_PROGRAMLIST_EDIT_ID%>";
		window.document.form_data.programlistId.value = id;
		window.document.form_data.submit();
	}
	//删除
	function programlistDelete(id, type) {
		var result = confirm("删除确认");
		if (result) {
			window.document.form_data.pageId.value = "<%=Define.JSP_PROGRAMLIST_SEARCH_ID%>";
			window.document.form_data.funcId.value = "<%=Define.FUNC_PROGRAMLIST_DELETE_ID%>";
			window.document.form_data.programlistId.value = id;
			window.document.form_data.submit();
		}
	}
	//批量审核
	function programlistAllAudit() {
		var checkBoxs = document.getElementsByName("child_checkBox");
		var programlistIds = "";
		for (var i = 0; i < checkBoxs.length; i++) {
			if (checkBoxs[i].checked) {
				if (programlistIds == "") {
					programlistIds = checkBoxs[i].id;
				} else {
					programlistIds += "," + checkBoxs[i].id;
				}
			}
		}
		if(programlistIds == "") {
			return;
		}
		window.document.form_data.pageId.value = "<%=Define.JSP_PROGRAMLIST_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_PROGRAMLIST_ALLAUDIT_ID%>";
		window.document.form_data.programlistId.value = programlistIds;
		window.document.form_data.submit();
	}
	//批量删除
	function programlistAllDel() {
		var checkBoxs = document.getElementsByName("child_checkBox");
		var programlistIds = "";
		for (var i = 0; i < checkBoxs.length; i++) {
			if (checkBoxs[i].checked) {
				if (programlistIds == "") {
					programlistIds = checkBoxs[i].id;
				} else {
					programlistIds += "," + checkBoxs[i].id;
				}
			}
		}
		if(programlistIds == "") {
			return;
		}
		window.document.form_data.pageId.value = "<%=Define.JSP_PROGRAMLIST_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_PROGRAMLIST_ALLDELETE_ID%>";
		window.document.form_data.programlistId.value = programlistIds;
		window.document.form_data.submit();
	}
	//翻页
	function pageTurn (page) {
		if (page <= 0 || page > <%=page_total%> || <%=total_num%> == 0) {
			return;
		}
		window.document.form_data.pageId.value = "<%=Define.JSP_PROGRAMLIST_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_PROGRAMLIST_SEARCH_ID%>";
		window.document.form_data.point_num.value = page;
		window.document.form_data.submit();
	}
	//区域授权
	function grant() {
		window.document.form_data.pageId.value = "<%=Define.JSP_REGIONAL_AUTH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_PROGRAMLIST_AUTH_ID%>";
		window.document.form_data.submit();
	}
	
</script>
</head>
<body onload="onload();">
<div class="search_title"><span>广告管理-节目单管理</span></div>
<table><tr style ="heigt:30px"></tr></table>
	<form name="form_data" action="<%=url %>" method="post">
		<input type="hidden" name="pageId" value="">
		<input type="hidden" name="funcId" value="">
		<input type="hidden" name="programlistId" value="">
		<input type="hidden" name="programlistArr" value="">
		<input type="hidden" name="point_num" value="<%=point_num %>">
		<div class="search_title">
			<div id="foldStyleId" class="search_minus" onclick="onFold('foldId');" ></div>
			<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
		</div>
		<div id="foldId" style="display: block; height: 90px; line-height: 26px;background-color:#B2DFEE;">
			<table>
				<tr>
					<td>节目单名称：</td>
					<td><input type="text" name="programlist_name" value="<%=programlistName%>"></td>
					<td>审核状态：</td>
					<td><select name="audit_type" value="<%=auditStatus%>">
						<option value=""></option>
						<option value="1" 
							<%if("1".equals(auditStatus)){%>selected<% } %>
						>未通过</option>
						<option value="2" 
							<%if("2".equals(auditStatus)){%>selected<% } %>
						>已通过</option>
						<option value="3" 
							<%if("3".equals(auditStatus)){%>selected<% } %>
						>已删除</option>
						</select>
					</td>
					<td></td>
				</tr>
				<tr>
					<td>创建用户：</td>
					<td><input type="text" name="operator_name" value="<%=operator_name%>"></td>
					<td>创建时间：</td>
					<td>
						<input type="text" size="8" name="search_date_s" maxlength="10" style="ime-mode:disabled" 
							value="<%=search_date_s%>" onclick="getDateString(this,oCalendarChs)">
						~
                        <input type="text" size="8" name="search_date_e" maxlength="10" style="ime-mode:disabled" 
                        	value="<%=search_date_e%>" onclick="getDateString(this,oCalendarChs)">
					</td>
					<td></td>
				</tr>
				<tr>
					<td>排序方式：</td>
					<td><select name="sort_field" value="<%=sortField%>">
						<option value=""></option>
						<option value="BILL_NAME" 
							<%if("BILL_NAME".equals(sortField)){%>selected<% } %>
						>节目单名称</option>
						<option value="NAME" 
							<%if("NAME".equals(sortField)){%>selected<% } %>
						>创建用户</option>
						<option value="OPERATETIME" 
							<%if("OPERATETIME".equals(sortField)){%>selected<% } %>
						>创建时间</option>
						<option value="STATUS" 
							<%if("STATUS".equals(sortField)){%>selected<% } %>
						>审核状态</option>
						</select>
					</td>
					<td>排序类型：</td>
					<td><select name="sort_type" value="<%=sortType%>">
						<option value=""></option>
						<option value="ASC" 
							<%if("ASC".equals(sortType)){%>selected<% } %>
						>升序</option>
						<option value="DESC" 
							<%if("DESC".equals(sortType)){%>selected<% } %>
						>降序</option>
						</select>
					</td>
					<td>
						<input type="button" value="搜索" class="search_button" onclick="programlistSearch();"/>
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
						<th style="width: 50px" class="th_tianhe">
							<input type="checkbox" id="head_checkBox" name="head_checkBox" onClick="check(this);"/>
						</th>
						<th class="th_tianhe">节目单名称</th>
						<th class="th_tianhe">创建用户</th>
						<th class="th_tianhe">创建时间</th>
						<th class="th_tianhe">审核状态</th>
						<th class="th_tianhe">操作</th>
					</tr>
					<% 
					if (resultBeans != null) {
						for (int i = 0; i < resultBeans.size(); i++) {
							AdvertBean bean = (AdvertBean)resultBeans.get(i);
							//节目单ID
							String programlistId = bean.getProgramlistId();
							//审核状态
							String status = bean.getAuditStatus();
							//审核显示值
							String statusValue = "";
							//审核按钮状态
							String statusBtnStyle = "";
							//编辑按钮状态
							String editBtnStyle = "";
							//删除按钮状态
							String delBtnStyle = "";
							if ("1".equals(status)) {
								statusValue = "未通过";
							} else if ("2".equals(status)) {
								statusValue = "已通过";
								statusBtnStyle = "disabled";
								editBtnStyle = "disabled";
							} else if ("3".equals(status)) {
								statusValue = "已删除";
								statusBtnStyle = "disabled";
								editBtnStyle = "disabled";
								delBtnStyle = "disabled";
							} else {
								statusBtnStyle = "disabled";
								editBtnStyle = "disabled";
								delBtnStyle = "disabled";
							}
					%>
					<tr>
						<td width="30" class="td_tianhe"><input
							type="checkbox" name="child_checkBox" onClick="check(this);" id="<%=programlistId%>"/></td>
						<td class="td_tianhe"><%=bean.getProgramlistName()%></td>
						<td class="td_tianhe"><%=bean.getOperator()%></td>
						<td class="td_tianhe"><%=bean.getCreateTime()%></td>
						<td class="td_tianhe"><%=statusValue%></td>
						<td class="td_tianhe">
							<input type="button" value="审核" <%=statusBtnStyle%> onclick="programlistAudit(<%=programlistId%>)"/>
							<input type="button" value="编辑" <%=editBtnStyle %> onclick="programlistEdit(<%=programlistId%>)"/>
							<input type="button" value="删除" <%=delBtnStyle %> onclick="programlistDelete(<%=programlistId%>)"/>
						</td>
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
							<input type="button" class="first_page" style="margin-left:5px" onclick="pageTurn(1)"/> 
							<input type="button" class="previous_page" style="margin-left:-2px" onclick="pageTurn(<%=point_num-1 %>)"/> 
							<input type="button" class="next_page" style="margin-left:-4px" onclick="pageTurn(<%=point_num+1 %>)"/>
							<input type="button" class="last_page" style="margin-left:-2px" onclick="pageTurn(<%=page_total %>)" />
							[当前第<%=point_num%>页/共<%=page_total%>页][每页<%=page_view%>条][共<%=total_num%>条]
						</td>
						<td width="300">
							<input type="button" class="rightBtn" value="删除" onclick="programlistAllDel();"/>
							<input type="button" class="rightBtn" value="审核" onclick="programlistAllAudit();"/>
							<input type="button" class="rightBtn" value="添加" onclick="programlistAdd();"/>
							<input type="button" class="rightBtn" value="区域授权" onclick="grant();"/>
						</td>
					</tr>
				</table>
				</div>
			</div>
		</div>
	</form>
</body>
</html>