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
//素材类型
String adertType = "";
//素材名称
String materialName = "";
//审核状态
String auditStatus = "";
//添加时间From
String search_date_s = "";
//添加时间To
String search_date_e = "";
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
	//排序方式名称
	if ("MEDIA_NAME".equals(sortField)) {
		sortFieldName = "素材名称";
	}else if ("GROUP_NAME".equals(sortField)) {
		sortFieldName = "素材分组";
	}else if ("MEDIA_TYPE".equals(sortField)) {
		sortFieldName = "素材类型";
	}else if ("NAME".equals(sortField)) {
		sortFieldName = "创建用户";
	}else if ("OPERATETIME".equals(sortField)) {
		sortFieldName = "过期时间";
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
	//素材类型
	adertType = selectBean.getMaterialType();
	//素材名称
	materialName = selectBean.getMaterialName();
	//审核状态
	auditStatus = selectBean.getAuditStatus();
	//添加时间From
	search_date_s = selectBean.getMaterialAddDateFrom();
	//添加时间To
	search_date_e = selectBean.getMaterialAddDateTo();
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
<script type="text/javascript" src="<%=strContextPath%>/js/Calendar.js"></script>
<title>广告管理-素材管理</title>
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
	
	// 素材添加
	function materialAdd() {
		window.document.form_data.pageId.value = "<%=Define.JSP_MATERIAL_ADD_ID%>";
		window.document.form_data.submit();
	}
	
	//素材检索
	function materialSearch() {
		window.document.form_data.point_num.value = 1;
		window.document.form_data.pageId.value = "<%=Define.JSP_MATERIAL_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_MATERIAL_SEARCH_ID%>";
		//window.document.form_data.point_num.value = 0;
		window.document.form_data.submit();
	}
	//审核
	function materialAudit(id, type) {
		var result = confirm("审核确认");
		if (result) {
			window.document.form_data.pageId.value = "<%=Define.JSP_MATERIAL_SEARCH_ID%>";
			window.document.form_data.funcId.value = "<%=Define.FUNC_MATERIAL_AUDIT_ID%>";
			window.document.form_data.materialId.value = id;
			window.document.form_data.adertType.value = type;
			window.document.form_data.submit();
		}
	}
	//编辑
	function materialEdit(id, type) {
		window.document.form_data.pageId.value = "<%=Define.JSP_MATERIAL_EDIT_ID%>";
		window.document.form_data.materialId.value = id;
		window.document.form_data.adertType.value = type;
		window.document.form_data.submit();
	}
	//删除
	function materialDelete(id, type) {
		var result = confirm("删除确认");
		if (result) {
			window.document.form_data.pageId.value = "<%=Define.JSP_MATERIAL_SEARCH_ID%>";
			window.document.form_data.funcId.value = "<%=Define.FUNC_MATERIAL_DELETE_ID%>";
			window.document.form_data.materialId.value = id;
			window.document.form_data.adertType.value = type;
			window.document.form_data.submit();
		}
	}
	//批量审核
	function materialAllAudit() {
		var checkBoxs = document.getElementsByName("child_checkBox");
		var materialIds = "";
		var adertTypes = "";
		for (var i = 0; i < checkBoxs.length; i++) {
			if (checkBoxs[i].checked) {
				if (materialIds == "") {
					materialIds = checkBoxs[i].id;
				} else {
					materialIds += "," + checkBoxs[i].id;
				}
				if (adertTypes == "") {
					adertTypes = checkBoxs[i].value;
				} else {
					adertTypes += "," + checkBoxs[i].value;
				}
			}
		}
		if(materialIds == "") {
			return;
		}
		window.document.form_data.pageId.value = "<%=Define.JSP_MATERIAL_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_MATERIAL_ALLAUDIT_ID%>";
		window.document.form_data.materialId.value = materialIds;
		window.document.form_data.adertType.value = adertTypes;
		window.document.form_data.submit();
	}
	//批量删除
	function materialAllDel() {
		var checkBoxs = document.getElementsByName("child_checkBox");
		var materialIds = "";
		var adertTypes = "";
		for (var i = 0; i < checkBoxs.length; i++) {
			if (checkBoxs[i].checked) {
				if (materialIds == "") {
					materialIds = checkBoxs[i].id;
				} else {
					materialIds += "," + checkBoxs[i].id;
				}
				if (adertTypes == "") {
					adertTypes = checkBoxs[i].value;
				} else {
					adertTypes += "," + checkBoxs[i].value;
				}
			}
		}
		if(materialIds == "") {
			return;
		}
		window.document.form_data.pageId.value = "<%=Define.JSP_MATERIAL_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_MATERIAL_ALLDELETE_ID%>";
		window.document.form_data.materialId.value = materialIds;
		window.document.form_data.adertType.value = adertTypes;
		window.document.form_data.submit();
	}
	//翻页
	function pageTurn (page) {
		if (page <= 0 || page > <%=page_total%> || <%=total_num%> == 0) {
			return;
		}
		window.document.form_data.pageId.value = "<%=Define.JSP_MATERIAL_SEARCH_ID%>";
		window.document.form_data.funcId.value = "<%=Define.FUNC_MATERIAL_SEARCH_ID%>";
		window.document.form_data.point_num.value = page;
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
</script>
</head>
<body onload="onload();">
<div class="search_title"><span>广告管理-素材管理</span></div>
<table><tr style ="heigt:30px"></tr></table>
	<form name="form_data" action="<%=url %>" method="post">
		<input type="hidden" name="pageId" value="">
		<input type="hidden" name="funcId" value="">
		<input type="hidden" name="materialId" value="">
		<input type="hidden" name="materialArr" value="">
		<input type="hidden" name="adertType" value="">
		<input type="hidden" name="point_num" value="<%=point_num %>">
		<div class="search_title">
			<div id="foldStyleId" class="search_minus" onclick="onFold('foldId');" ></div>
			<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
		</div>
		<div id="foldId" style="height: 90px; line-height: 26px;background-color:#B2DFEE;">
			<table>
				<tr>
					<td>素材名称：</td>
					<td><input type="text" name="material_name" value="<%=materialName%>"></td>
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
					<td>素材类型：</td>
					<td>
						<select id="file_type" name="file_type">
							<option value=""></option>
							<option value="图片">图片</option>
							<option value="视频">视频</option>
							<option value="音频">音频</option>
							<option value="字幕">字幕</option>
						</select>
					</td>
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
						<option value="MEDIA_NAME" 
							<%if("MEDIA_NAME".equals(sortField)){%>selected<% } %>
						>素材名称</option>
						<option value="GROUP_NAME" 
							<%if("GROUP_NAME".equals(sortField)){%>selected<% } %>
						>素材分组</option>
						<option value="MEDIA_TYPE" 
							<%if("MEDIA_TYPE".equals(sortField)){%>selected<% } %>
						>素材类型</option>
						<option value="NAME" 
							<%if("NAME".equals(sortField)){%>selected<% } %>
						>创建用户</option>
						<option value="OPERATETIME" 
							<%if("OPERATETIME".equals(sortField)){%>selected<% } %>
						>过期时间</option>
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
						<th style="width: 30px" class="th_tianhe">
							<input type="checkbox" id="head_checkBox" name="head_checkBox" onClick="check(this);"/>
						</th>
						<th class="th_tianhe">素材名称</th>
						<th class="th_tianhe">素材分组</th>
						<th class="th_tianhe">素材类型</th>
						<th class="th_tianhe">创建用户</th>
						<th class="th_tianhe">过期时间</th>
						<th class="th_tianhe">创建时间</th>
						<th class="th_tianhe">审核状态</th>
						<th class="th_tianhe">操作</th>
					</tr>
					<% 
					if (resultBeans != null) {
						for (int i = 0; i < resultBeans.size(); i++) {
							AdvertBean bean = (AdvertBean)resultBeans.get(i);
							//素材ID
							String materialId = bean.getMaterialId();
							//素材类型
							String advertType = bean.getAdvert_type();
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
							type="checkbox" name="child_checkBox" onClick="check(this);" id="<%=materialId%>" value="<%=advertType%>"/></td>
						<td class="td_tianhe"><%=bean.getMaterialName()%></td>
						<td class="td_tianhe"><%=bean.getMaterial_group()%></td>
						<td class="td_tianhe"><%=bean.getMaterialType()%></td>
						<td class="td_tianhe"><%=bean.getOperator()%></td>
						<td class="td_tianhe"><%=bean.getExpireTime()%></td>
						<td class="td_tianhe"><%=bean.getCreateTime()%></td>
						<td class="td_tianhe"><%=statusValue%></td>
						<td class="td_tianhe">
							<input type="button" value="审核" <%=statusBtnStyle%> onclick="materialAudit('<%=materialId%>','<%=advertType%>')"/>
							<input type="button" value="编辑" <%=editBtnStyle%> onclick="materialEdit('<%=materialId%>','<%=advertType%>')"/>
							<input type="button" value="删除" <%=delBtnStyle%> onclick="materialDelete('<%=materialId%>','<%=advertType%>')"/>
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
							<input type="button" class="rightBtn" value="删除" onclick="materialAllDel();"/>
							<input type="button" class="rightBtn" value="审核" onclick="materialAllAudit();"/>
							<input type="button" class="rightBtn" value="添加" onclick="materialAdd();"/>
							<!--<input type="button" class="rightBtn" value="review" onclick="review();"/>
						--></td>
					</tr>
				</table>
				</div>
				
			</div>
		</div>
	</form>
</body>
</html>