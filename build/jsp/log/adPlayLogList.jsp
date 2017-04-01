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
//节目单名称
String billName = "";
//端机标识
String machineMark = "";
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
	//节目单名称
	if(selectBean.getBill_name()!=null)
	{
		billName = selectBean.getBill_name();
	}
	//端机标识
	if(selectBean.getMachine_mark()!=null)
	{
		machineMark = selectBean.getMachine_mark();
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

<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css"/>
<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
<link rel="stylesheet" type="text/css" href="./css/channel.css" />
<link rel="stylesheet" type="text/css" href="./css/machine.css" />
<script type="text/javascript" src="./js/Calendar.js"></script>
<script type="text/javascript">
   var oCalendarChs=new PopupCalendar("oCalendarChs"); //初始化控件时,请给出实例名称:oCalendarChs
        oCalendarChs.weekDaySting=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
        oCalendarChs.monthSting=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
        oCalendarChs.oBtnTodayTitle="今天";
        oCalendarChs.oBtnCancelTitle="取消";
        oCalendarChs.Init();
        
	  	function onFold(id){
	    	var vDiv = document.getElementById(id);
			vDiv.style.display = (vDiv.style.display == 'none')?'block':'none';
			var fold = document.getElementById('clientStyleId'); 
			if(vDiv.style.display === 'none'){ 
				fold.className = 'x-fold-plus';
			}else{
				fold.className = 'x-fold-minus';
			}
	  	} 
	  	//计算天数差的函数，通用  
	  	function DateDiff(sDate1, sDate2){  //sDate1和sDate2是2002-12-18格式
	  		var aDate, oDate1, oDate2, iDays;
	  		aDate = sDate1.split("-");  
	        oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);  //转换为12-18-2002格式  
	        aDate = sDate2.split("-");  
	        oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);  
	        iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 /24);  //把相差的毫秒数转换为天数  
	        return iDays;  
		}  


		  	
		//广告播放日志检索
		function advertisingLogSearch() {
			if(document.getElementById("txttime1").value==""||document.getElementById("txttime2").value==""){
				window.alert("您选择的检索时间段不能为空，请重新选择");  
				return;
			}
			var d1 = document.getElementById("txttime1").value;
			var d2 = document.getElementById("txttime2").value;
			//ts 相减的差值  
            var ts = DateDiff(d1, d2);  
			if(ts>=30){
				window.alert("您选择的检索时间段不能大于30天，请重新选择");  
				return;
			}
			if( document.getElementById("txttime2").value!== ""  && document.getElementById("txttime1").value > document.getElementById("txttime2").value){
				window.alert("您选择的检索时间段不正确，请重新选择");  
				return;
			}	
			window.document.form_data.pageId.value = 'advertising_play';
			window.document.form_data.point_num.value = '1';
			window.document.form_data.submit();
		}
		//翻页
		function pageTurn (page) {
			if (page <= 0 || page > <%=page_total%> || <%=total_num%> == 0) {
				return;
			}
			window.document.form_data.pageId.value = 'advertising_play';
			window.document.form_data.point_num.value = page;
			window.document.form_data.bill_name.value = window.document.form_data.trunpage_bill_name.value;
			window.document.form_data.Machine_mark.value = window.document.form_data.turnpage_Machine_mark.value;
			window.document.form_data.search_time_start.value = window.document.form_data.turnpage_search_time_start.value;
			window.document.form_data.search_time_end.value = window.document.form_data.turnpage_search_time_end.value;
			window.document.form_data.submit();
		}
		</script>
		<title>广告播放日志</title>
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
	<script LANGUAGE="JavaScript">
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
	<div class="x-title"><span>&nbsp;&nbsp;日志管理-广告播放日志</span></div>
	<body> 
		<table><tr style ="heigt:30px"></tr></table>
		<form class="x-client-form" name="form_data" action="<%=url %>" method="post">
			<input type="hidden" name="pageId" value=""/>
			<input type="hidden" name="point_num" value="<%=point_num %>"/>
			<input type="hidden" name="trunpage_bill_name" value="<%=billName %>"/>
			<input type="hidden" name="turnpage_Machine_mark" value="<%=machineMark %>"/>
			<input type="hidden" name="turnpage_search_time_start" value="<%=search_date_s %>"/>
			<input type="hidden" name="turnpage_search_time_end" value="<%=search_date_e %>"/>
			<div class="x-title">
		    	<div id="foldStyleId" class="x-fold-minus" onclick="onFold('foldId');" />
		    </div>
			<div style="height:26px;text-align:left;line-height:26px">检索</div>
			</div>
			<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
				<div style="float: left">
					<span>&nbsp;节目单</span> 
					<input type="text" name="bill_name" value="<%=billName%>" style="width:120px"/>
				</div>
				<div style="float: left">
					<span>&nbsp;&nbsp;机器名</span> 
					<input type="text" name="Machine_mark" value="<%=machineMark%>" style="width:120px"/>
				</div>
				<div style="float: left">
					<span>&nbsp;&nbsp;创建时间</span> 
				</div>
				<input type="text" size="10" id="txttime1" name="search_time_start" value="<%=search_date_s%>" onclick="getDateString(this,oCalendarChs)"/> --- <input type="text" size="10" id="txttime2" name="search_time_end" value="<%=search_date_e%>" onclick="getDateString(this,oCalendarChs)"/>
				<input class="tableBtn" type="button" value="搜索"  onclick="advertisingLogSearch();"/>
			</div>
		  <table><tr style ="heigt:30px "></tr></table>
		  <div>
		    <div class="x-data">
		    	<span  style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span>
		    </div>
		    <div>
				<table id="mytable" cellspacing="0">
					<tr>
						<th scope="col" style="width:15%" class="x-data-table-th">节目单</th>
						<th scope="col" style="width:25%" class="x-data-table-th">运行状态</th>
						<th scope="col" style="width:15%" class="x-data-table-th">屏幕ID</th>
						<th scope="col" style="width:25%" class="x-data-table-th">机器名</th>
						<th scope="col" style="width:20%" class="x-data-table-th">状态时间</th>
					</tr>
					<% 
					if (resultBeans != null) {
						for (int i = 0; i < resultBeans.size(); i++) {
							LogListBean bean = (LogListBean)resultBeans.get(i);
					%>
					<tr>
						<td class="x-data-table-td"><%=bean.getBill_name()%></td>
						<td class="x-data-table-td">持续</td>
						<td class="x-data-table-td">主屏</td>
						<td class="x-data-table-td"><%=bean.getMachine_mark()%></td>
						<td class="x-data-table-td"><%=bean.getStart_play_time()%></td>
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