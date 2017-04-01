<%@page import="java.util.ArrayList"%>
<%@page import="th.entity.LogListBean"%>
<%@page import="java.util.List"%>
<%@page import="th.com.util.Define"%>
<%@page import="th.com.util.Define4Report"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.util.StringUtils"%>
<%
Log logger = LogFactory.getLog(this.getClass());
String strContextPath = request.getContextPath();
String selectedOrgId = (String) request.getAttribute( Define4Report.REQ_PARAM_SELECTED_ORG_ID );
selectedOrgId = StringUtils.isBlank( selectedOrgId ) ? "" : selectedOrgId;
logger.info("The secected org id is: "+selectedOrgId);

String url = strContextPath + "/Advice.html";
//检索结果
List resultBeans = (ArrayList)request.getAttribute("resultList");
//检索条件
LogListBean selectBean = (LogListBean)request.getAttribute("select_object");
String orgs = (String) request.getAttribute( "orgs" );
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

    function init(){
        var selectedOrgId='<%=selectedOrgId%>';
     	if(selectedOrgId&&selectedOrgId!=''){
     		var orgSelect = document.getElementById("orgSelect");
     		for (var i = 0; i < orgSelect.options.length; i++) {        
     	        if (orgSelect.options[i].value == selectedOrgId) {        
     	        	orgSelect.options[i].selected = true;        
     	            break;        
     	        }     
     		}
     	}         	
    }        
        
    function OMOver(OMO){OMO.style.backgroundColor='#CAE8EA';}
    function OMOut(OMO){OMO.style.backgroundColor='';}    
        
	function onFold(id){
		var vDiv = document.getElementById(id);
		vDiv.style.display = (vDiv.style.display == 'none')?'block':'none';
		var fold = document.getElementById('searchFormStyleId'); 
		if(vDiv.style.display === 'none'){ 
			fold.className = 'x-fold-plus';
		}else{
			fold.className = 'x-fold-minus';
		}
  	}
	function changeDataTypeRodio(id){ 
  		if(id === 'summary'){
			document.getElementById('summaryDataId').style.display = '';
			document.getElementById('detailDataId').style.display = 'none';
			document.getElementById('summarySortSelect').style.display = '';
			document.getElementById('detailSortSelect').style.display = 'none';			
		}else if(id === 'detail'){
			document.getElementById('summaryDataId').style.display = 'none';
			document.getElementById('detailDataId').style.display = '';
			document.getElementById('summarySortSelect').style.display = 'none';
			document.getElementById('detailSortSelect').style.display = '';
			
		}
  	}

  	function changeTimeTypeRodio(checkedRodio) {
  		var checkedRodioId = checkedRodio.id
		var rodioIds = "day,week,month,any".split(',');
		for (var i = 0; i < rodioIds.length; i++) {
			var rodioId = rodioIds[i];
			var tempId = rodioId +"Type";
			if(rodioId == checkedRodioId){
				document.getElementById(tempId).style.display = '';
			}else{
				document.getElementById(tempId).style.display = 'none';
			}
		}
	}
	
  	function doSearch(){
	  	document.machineOpenRateForm.action = "/th/Report?reportPage=3&functionCode=2";
	  	document.machineOpenRateForm.submit();
  	}
  	
  	function exportReport(){
	  	document.machineOpenRateForm.action = "/th/Report?reportPage=3&functionCode=3";
	  	document.machineOpenRateForm.submit();
  	} 
  	
  	function changePage(button){
  		var buttonId = button.id;
  		var pageNumber = document.getElementById("pageNumber").value;
  		var totalPageCount = '<%=point_num%>';
  		if(buttonId == "firstPageButton"){
  			pageNumber = "1";
  		}else if(buttonId == "previousPageButton"){
  			pageNumber =  (parseInt(pageNumber) - 1)+"";
  		}else if(buttonId == "nextPageButton"){
  			pageNumber =  (parseInt(pageNumber) + 1)+"";
  		}else if(buttonId == "lastPageButton"){
  			pageNumber =  totalPageCount;
  		}
  		document.getElementById("pageNumber").value = pageNumber;
	  	document.machineOpenRateForm.action = "/th/Report?reportPage=3&functionCode=2";
	  	document.machineOpenRateForm.submit();
  	}  	
  	
	//组织结构下拉菜单
	var setting = {
	            view: {  
	                dblClickExpand: false  
	            },  
	            data: {  
	                simpleData: {  
	                    enable: true  
	                }  
	            },  
	            callback: {  
	                //beforeClick: beforeClick,//(点击之前)用于捕获 勾选 或 取消勾选 之前的事件回调函数，并且根据返回值确定是否允许 勾选 或 取消勾选   
	                onClick: onClick  
	            }  
	};  
	var zNodes = <%=orgs%> ;

    $(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		
		var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
		var v = "", w = "";
		// 设置组织名称下拉列表值 
		var orgList = document.getElementById("orgSelect");
		for (var i=0; i<nodes.length; i++) {
			w = "";
			if(i<nodes.length-1){
				for(var j=0; j<(nodes[i].level-nodes[0].level); j++){
					w += "├─";					
				}
			}else{
				for(var j=0; j<(nodes[i].level-nodes[0].level); j++){
					w += "└─";
				}
			}
			// 为select下拉菜单动态赋予option值 
			orgList.options.add(new Option(w+nodes[i].name,nodes[i].id));
			// 组织List信息拼接  
			v += w + nodes[i].name + "_" + nodes[i].id;
			if(i<nodes.length-1){
				v += ",";
			}
			
		}
	});
    function onClick(e, treeId, treeNode) {  
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),  
        nodes = zTree.getSelectedNodes(),//获取 zTree 当前被选中的节点数据集合（按Ctrl多选择）  
        v = "";
        var nid = "";
        nodes.sort(function compare(a,b){return a.id-b.id;});//按照id从小到大进行排序  
        for (var i=0, l=nodes.length; i<l; i++) {  
            v += nodes[i].name + ",";  
            nid = nodes[i].id;
        }  
    }   	
</script>
		<title>意见薄</title>
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
	<div class="x-title"><span>&nbsp;&nbsp;日志管理-意见薄</span></div>
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
	 <div id="searchFormId" style="background-color:#B2DFEE">
    <table cellpadding="0" border="0">
      <tr>
        <td width="50" style="border:0;text-align:left">&nbsp;&nbsp;组&nbsp;&nbsp;&nbsp;织：</td>
        <td width="300" style="border:0;text-align:left"> 
            <select id="orgSelect" name="orgSelect"></select>
        </td>
        
            <input type="button" class="x-button" value="搜索" onclick ="doSearch()" />
 		</td>     
      </tr>      
    </table>
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
			<input type="button" class="x-last-page" style="margin-left:-2px" name="button_page_last" id="btnLast"  onclick="pageTurn(<%=page_total %>)"/>&nbsp;[当前第<%=point_num%>页/共<%=page_total%>页][每页<%=page_view%>条][共<%=total_num%>条]
		</div>
	</form>
	</body>
</html>