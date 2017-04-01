<%@page import="java.util.ArrayList"%>
<%@page import="th.entity.LogListBean"%>
<%@page import="java.util.List"%>
<%@page import="th.com.util.Define"%>
<%

String strContextPath = request.getContextPath();

String url = strContextPath + "/LogListServlet.html";
//检索结果
List resultBeans = (ArrayList)request.getAttribute("resultList");
//检索条件
LogListBean selectBean = (LogListBean)request.getAttribute("select_object");

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
//操作用户
String operationUser = "";
//操作类型
String operationType = "";
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
/* 	//排序方式
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
	} */
	//操作用户
	if(selectBean.getOperation_user()!=null)
	{
		operationUser = selectBean.getOperation_user();
	}
	//操作类型
	if(selectBean.getOperation_type()!=null)
	{
		operationType = selectBean.getOperation_type();
	}
	//添加时间From
	if(selectBean.getSearch_time_start()!=null)
	{
		search_date_s = selectBean.getSearch_time_start();
	}
	//添加时间To
	if(selectBean.getSearch_time_end()!=null)
	{
		search_date_e = selectBean.getSearch_time_end();
	}
	
}

%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="./css/channel.css" />
		<link rel="stylesheet" type="text/css" href="./css/machine.css" />
		<script type="text/javascript" src="./js/Calendar.js"></script>
		<%@ page import="java.util.*"%>
		<script type="text/javascript">
		   var oCalendarChs=new PopupCalendar("oCalendarChs"); //初始化控件时,请给出实例名称:oCalendarChs
	        oCalendarChs.weekDaySting=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
	        oCalendarChs.monthSting=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
	        oCalendarChs.oBtnTodayTitle="今天";
	        oCalendarChs.oBtnCancelTitle="取消";
	        oCalendarChs.Init();
		
			function onFold(id) {
				var vDiv = document.getElementById(id);
				vDiv.style.display = (vDiv.style.display == 'none') ? 'block' : 'none';
				var fold = document.getElementById('foldStyleId');
				if (vDiv.style.display === 'none') {
					fold.className = 'x-fold-plus';
				} else {
					fold.className = 'x-fold-minus';
				}
			}
			//系统操作日志检索
			function systemOperLogSearch() {
				if( document.getElementById("txttime2").value!== ""  && document.getElementById("txttime1").value > document.getElementById("txttime2").value){
					window.alert("您选择的检索时间段不正确，请重新选择");  
					return;
				}	
				window.document.form_data.pageId.value = 'system_operational';
				window.document.form_data.point_num.value = '1';
				window.document.form_data.submit();
			}
			//翻页
			function pageTurn (page) {
				if (page <= 0 || page > <%=page_total%> || <%=total_num%> == 0) {
					return;
				}
				window.document.form_data.pageId.value = 'system_operational';
				window.document.form_data.point_num.value = page;
				window.document.form_data.operation_user.value = window.document.form_data.turnpage_operation_user.value;
				window.document.form_data.operation_type.value = window.document.form_data.turnpage_operation_type.value;
				window.document.form_data.search_time_start.value = window.document.form_data.turnpage_search_time_start.value;
				window.document.form_data.search_time_end.value = window.document.form_data.turnpage_search_time_end.value;
				window.document.form_data.submit();
			}
		</script>
		<title>系统操作日志</title>
	</head>
	<style type="text/css">
		body {
			font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica,
				sans-serif;
			color: #4f6b72;
		}
		
		a {
			color: #c75f3e;
		}
		
		#mytable {
			width: 100%;
			padding: 0;
			margin: 0;
		}
		
		caption {
			padding: 0 0 5px 0;
			width: 700px;
			font: italic 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
			text-align: right;
		}
		
/* 		th {
			font: bold 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
			color: #4f6b72;
			border-right: 1px solid #C1DAD7;
			border-bottom: 1px solid #C1DAD7;
			border-top: 1px solid #C1DAD7;
			letter-spacing: 2px;
			text-transform: uppercase;
			text-align: left;
			padding: 6px 6px 6px 12px;
			background: #CAE8EA no-repeat;
		} */
		
		th.nobg {
			border-top: 0;
			border-left: 0;
			border-right: 1px solid #C1DAD7;
			background: none;
		}
		
/* 		td {
			border-right: 1px solid #C1DAD7;
			border-bottom: 1px solid #C1DAD7;
			font-size: 11px;
			padding: 6px 6px 6px 12px;
			color: #4f6b72;
		} */
		
		td.alt {
			background: #F5FAFA;
			color: #797268;
		}
		
		th.spec {
			border-left: 1px solid #C1DAD7;
			border-top: 0;
			background: #fff no-repeat;
			font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
		}
		
		th.specalt {
			border-left: 1px solid #C1DAD7;
			border-top: 0;
			background: #f5fafa no-repeat;
			font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
			color: #797268;
		}
		/*---------for IE 5.x bug*/
		html>body td {
			font-size: 11px;
		}
		
		body,td,th {
			font-family: 宋体, Arial;
			font-size: 12px;
		}
	</style>
	<SCRIPT LANGUAGE="JavaScript">
	
		window.onload = function showtable() {			
			var tablename = document.getElementById("mytable");
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
	</script>
	<div class="x-title"><span>&nbsp;&nbsp;日志管理-系统操作日志</span></div>
	<body>
	<table><tr style ="heigt:30px"></tr></table>
	<form class="x-client-form" name="form_data" action="<%=url %>" method="post">
		<input type="hidden" name="pageId" value="">
		<input type="hidden" name="point_num" value="<%=point_num %>">
		<input type="hidden" name="turnpage_operation_user" value="<%=operationUser %>">
		<input type="hidden" name="turnpage_operation_type" value="<%=operationType %>">
		<input type="hidden" name="turnpage_search_time_start" value="<%=search_date_s %>">
		<input type="hidden" name="turnpage_search_time_end" value="<%=search_date_e %>">
		<div class="x-title">
	    	<div id="foldStyleId" class="x-fold-minus" onclick="onFold('foldId');" />
		</div>
		<div style="height:26px;text-align:left;line-height:26px">检索</div>
		</div>
		<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
			<div style="float: left">
				<span>&nbsp;操作用户</span> 
				<input type="text" name="operation_user" value="<%=operationUser %>" style="width:120px"/>
			</div>
			<div style="float: left">
				<span>&nbsp;&nbsp;操作类型</span> 
		    	<select name="operation_type" value="<%=operationType%>" style="width:100px">
					<option value="">全部</option>
					<option value="<%=Define.OPERATION_TYPE_BILL%>" 
					 <% if(Define.OPERATION_TYPE_BILL.equals(operationType)){%>selected<%} %>>
					 下发节目单</option>
					<option value="<%=Define.OPERATION_TYPE_SUBTITLES%>" 
					<% if(Define.OPERATION_TYPE_SUBTITLES.equals(operationType)){%>selected<%} %>>
					字幕</option>
					<option value="<%=Define.OPERATION_TYPE_MODULE%>" 
					<% if(Define.OPERATION_TYPE_MODULE.equals(operationType)){%>selected<%} %>>
					配置</option>
			        <option value="<%=Define.OPERATION_TYPE_SYSTEM_UPDATE%>" 
			        <% if(Define.OPERATION_TYPE_SYSTEM_UPDATE.equals(operationType)){%>selected<%} %>>
		        	系统升级</option>
			        <option value="<%=Define.OPERATION_TYPE_CHANNEL%>" 
			        <% if(Define.OPERATION_TYPE_CHANNEL.equals(operationType)){%>selected<%} %>>
		        	频道发布</option>
			        <option value="<%=Define.OPERATION_TYPE_QUICK_ENTRY%>" 
			        <% if(Define.OPERATION_TYPE_QUICK_ENTRY.equals(operationType)){%>selected<%} %>>
		        	快速入口发布</option>
					</select>
		    	</select>
			</div>
			<div style="float: left">
				<span>&nbsp;&nbsp;操作时间</span> 
			</div>
	    	<input type="text" size="10" id="txttime1" name="search_time_start" value="<%=search_date_s %>" onclick="getDateString(this,oCalendarChs)"/> --- <input type="text" size="10" id="txttime2" name="search_time_end" value="<%=search_date_e %>" onclick="getDateString(this,oCalendarChs)"/>
			<input class="tableBtn" type="button" value="搜索"  onclick="systemOperLogSearch();"/>
		</div>
		<table><tr style ="heigt:30px"></tr></table>
		<div>
		    <div class="x-data">
		    	<span  style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span>
		    </div>
		    <div>
				
					<table id="mytable" cellspacing="0">
						<tr>
							<th scope="col" style="width:15%" class="x-data-table-th">操作用户</th>
							<th scope="col" style="width:15%" class="x-data-table-th">操作类型</th>
							<th scope="col" style="width:15%" class="x-data-table-th">操作时间</th>
							<th scope="col" style="width:53%" class="x-data-table-th">操作描述</th>
						</tr>
						<% 
						if (resultBeans != null) {
							for (int i = 0; i < resultBeans.size(); i++) {
								LogListBean bean = (LogListBean)resultBeans.get(i);
						%>
						<tr>				
							<td class="x-data-table-td"><%=bean.getOperation_user()%></td>
							<td class="x-data-table-td"><%=bean.getOperation_type()%></td>
							<td class="x-data-table-td"><%=bean.getOperation_time()%></td>
							<td class="x-data-table-td"><%=bean.getOperation_description()%></td>
						</tr>
						<%		
							}
						}
						%>
					</table>
				
	    	</div>
	  	</div>
		<div style="float: left">
			<input type="button" class="x-first-page" style="margin-left:5px" name="button_page_first" id="btnFirst"  onclick="pageTurn(1)"/>
			<input type="button" class="x-previous-page" style="margin-left:-2px" name="button_page_previous" id="btnPre"  onclick="pageTurn(<%=point_num-1 %>)"/> 
			<input type="button" class="x-next-page" style="margin-left:-4px" name="button_page_next" id="btnNext"  onclick="pageTurn(<%=point_num+1 %>)"/>
			<input type="button" class="x-last-page" style="margin-left:-2px" name="button_page_last" id="btnLast"  onclick="pageTurn(<%=page_total %>)"/>
			[当前第<%=point_num%>页/共<%=page_total%>页][每页<%=page_view%>条][共<%=total_num%>条]
		</div>
	</form>
	</body>
</html>