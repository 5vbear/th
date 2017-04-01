<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="th.entity.log.MessageBean"%>
<%@page import="th.util.StringUtils"%>
<%@page import="th.util.DateUtil"%>
<%@page import="th.com.util.Define4Report"%>
<%@page import="th.com.util.Define"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>历史消息</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
	//每页显示行数
	int page_view = Define.VIEW_NUM;
	String total_num = "0";
%>
<%
	String dataType = (String) request
			.getAttribute(Define4Report.REQ_PARAM_DATA_TYPE);
	String timeType = (String) request
			.getAttribute(Define4Report.REQ_PARAM_TIME_TYPE);
	String pageNumber = (String) request
			.getAttribute(Define4Report.REQ_PARAM_PAGE_NUMBER);
	String totalPageCount = (String) request
			.getAttribute(Define4Report.REQ_PARAM_TOTAL_PAGE_COUNT);
	String sortKey = (String) request
			.getAttribute(Define4Report.REQ_PARAM_SORT_KEY);

	String selectedOrgId = (String) request
			.getAttribute(Define4Report.REQ_PARAM_SELECTED_ORG_ID);
	selectedOrgId = StringUtils.isBlank(selectedOrgId) ? ""
			: selectedOrgId;

	String dayTypeTime = (String) request
			.getAttribute(Define4Report.REQ_PARAM_DAY_TYPE_TIME);
	String weekTypeTime = (String) request
			.getAttribute(Define4Report.REQ_PARAM_WEEK_TYPE_TIME);
	String monthTypeTime = (String) request
			.getAttribute(Define4Report.REQ_PARAM_MONTH_TYPE_TIME);
	String anyTypeStartTime = (String) request
			.getAttribute(Define4Report.REQ_PARAM_ANY_TYPE_START_TIME);
	String anyTypeEndTime = (String) request
			.getAttribute(Define4Report.REQ_PARAM_ANY_TYPE_END_TIME);

	String orgs = (String) request.getAttribute("orgs");
	ArrayList<MessageBean> viewList = (ArrayList<MessageBean>) request
			.getAttribute("viewList");
	total_num = (String) request.getAttribute("total_num");
	total_num = StringUtils.isBlank(total_num) ? "0" : total_num;
%>
<%
	dataType = StringUtils.isBlank(dataType) ? Define4Report.DATA_TYPE_SUMMARY
			: dataType;
	timeType = StringUtils.isBlank(timeType) ? Define4Report.TIME_TYPE_ANY
			: timeType;
	pageNumber = StringUtils.isBlank(pageNumber) ? "1" : pageNumber;
	totalPageCount = StringUtils.isBlank(totalPageCount) ? "1"
			: totalPageCount;
	sortKey = StringUtils.isBlank(sortKey) ? "" : sortKey;
	selectedOrgId = StringUtils.isBlank(selectedOrgId) ? ""
			: selectedOrgId;

	String currentDate = DateUtil
			.getYesterdayDate(Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD);
	dayTypeTime = StringUtils.isBlank(dayTypeTime) ? currentDate
			: dayTypeTime;
	weekTypeTime = StringUtils.isBlank(weekTypeTime) ? currentDate
			: weekTypeTime;
	monthTypeTime = StringUtils.isBlank(monthTypeTime) ? currentDate
			: monthTypeTime;
	anyTypeStartTime = StringUtils.isBlank(anyTypeStartTime) ? currentDate
			: anyTypeStartTime;
	anyTypeEndTime = StringUtils.isBlank(anyTypeEndTime) ? currentDate
			: anyTypeEndTime;
%>
<%
	String summaryRadioChecked = (Define4Report.DATA_TYPE_SUMMARY
			.equals(dataType)) ? "checked='checked'" : "";
	String detailRadioChecked = (Define4Report.DATA_TYPE_DETAIL
			.equals(dataType)) ? "checked='checked'" : "";
	String summaryDivStyle = (Define4Report.DATA_TYPE_SUMMARY
			.equals(dataType)) ? "" : "OVERFLOW-Y:auto;height:400px;";
	String detailDivStyle = (Define4Report.DATA_TYPE_DETAIL
			.equals(dataType)) ? "" : "display:none";

	String dayRadioChecked = (Define4Report.TIME_TYPE_DAY
			.equals(timeType)) ? "checked='checked'" : "";
	String weekRadioChecked = (Define4Report.TIME_TYPE_WEEK
			.equals(timeType)) ? "checked='checked'" : "";
	String monthRadioChecked = (Define4Report.TIME_TYPE_MONTH
			.equals(timeType)) ? "checked='checked'" : "";
	String anyRadioChecked = (Define4Report.TIME_TYPE_ANY
			.equals(timeType)) ? "checked='checked'" : "";
	String dayTypeStyle = (Define4Report.TIME_TYPE_DAY.equals(timeType)) ? ""
			: "display:none";
	String weekTypeStyle = (Define4Report.TIME_TYPE_WEEK
			.equals(timeType)) ? "" : "display:none";
	String monthTypeStyle = (Define4Report.TIME_TYPE_MONTH
			.equals(timeType)) ? "" : "display:none";
	String anyTypeStyle = (Define4Report.TIME_TYPE_ANY.equals(timeType)) ? ""
			: "display:none";

	String firstPageDisabled = Integer.parseInt(pageNumber) < 2 ? "disabled"
			: "";
	String previousPageDisabled = Integer.parseInt(pageNumber) < 2 ? "disabled"
			: "";
	String nextPageDisabled = Integer.parseInt(pageNumber) >= Integer
			.parseInt(totalPageCount) ? "disabled" : "";
	String lastPageDisabled = Integer.parseInt(pageNumber) >= Integer
			.parseInt(totalPageCount) ? "disabled" : "";
%>
<%
	//排序类型
	String sortType = (String) request.getAttribute("sortType");

%>

<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/report.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/zTree/css/demo.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/zTree/css/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="<%=basePath%>/js/Calendar.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/zTree/js/jquery.ztree.excheck-3.5.js"></script>
</head>

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
	
  	

  	function changePage(button){
  		var buttonId = button.id;
  		var pageNumber = document.getElementById("pageNumber").value;
  		var totalPageCount = '<%=totalPageCount%>';
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
	  	document.adviceForm.action = "/th/Message.html?type=doSearch";
	  	document.adviceForm.submit();
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
	function searchMessageInfo(){
		var form = document.getElementById('adviceForm');
		form.action = '/th/Message.html';
		document.getElementById('searchInfoId').value = 'doSearch';
		document.getElementById('pageNumber').value = '1';
		form.submit();
	};
	
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
</head>
<body onload="init()">
<form class="x-client-form" action="" name="adviceForm" method="post"
	id="adviceForm"><input type="hidden" name="type"
	id="searchInfoId" value="init" />
<input type="hidden" name="dataType"   value="1">
<div class="x-title"><span>&nbsp;&nbsp;日志管理-历史消息</span></div>
<table>
	<tr style="heigt: 15px"></tr>
</table>
<div class="x-title">
<div id="searchFormStyleId" class="x-fold-minus"
	onclick="onFold('searchFormId');" />
</div>
<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
<div id="searchFormId" style="background-color: #B2DFEE">
<table>
	<tr>
		<td>&nbsp;组&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;织:</td>
		<td>
		<div><select id=orgSelect name="orgSelect"
			style="border: 1px solid #737C73;"></select>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		</td>
		<td><span>&nbsp;&nbsp;&nbsp;&nbsp;机器名:</span> <input
			name="machinename" type="text" size="19" /></td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;时间排序类型：</td>
		<td><select name="sortType" value="<%=sortType%>">
			<option value=""></option>
			<option value="ASC" 
				<%if("ASC".equals(sortType)){%>selected<% } %>
			>升序</option>
			<option value="DESC" 
				<%if("DESC".equals(sortType)){%>selected<% } %>
			>降序</option>
			</select>
		&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td><input type="button" value="搜索"
			onClick="searchMessageInfo();" class="x-button" /></td>
		<td>&nbsp;</td>
	</tr>

</table>
</div>
<p></p>
<div>
<div class="x-data"><span
	style="height: 30px; width: 100px; line-height: 30px">&nbsp;&nbsp;数据</span></div>
<div id="summaryDataId" style="<%=summaryDivStyle%>">
<table class="x-data-table">
	<tr>
		<th style="width: 8%" class="x-data-table-th">操作人员</th>
		<th style="width: 10%" class="x-data-table-th">机器名</th>
		<th style="width: 10%" class="x-data-table-th">组织名</th>
		<th style="width: 40%" class="x-data-table-th">内容</th>
		<th style="width: 15%" class="x-data-table-th">发出时间</th>
	</tr>
	<%
		if (viewList != null && viewList.size() > 0) {
			for (int i = 0; i < viewList.size(); i++) {
				MessageBean messasgeBean = (MessageBean) viewList.get(i);
				String trClass = (i % 2 != 0) ? "x-data-table-tr-white"
						: "x-data-table-tr-gray";
	%>
	<tr class="<%=trClass%>" onmouseover='OMOver(this);'
		onmouseout='OMOut(this);'>
		<td class="x-data-table-td"><%=messasgeBean.getOperator()%></td>
		<td class="x-data-table-td"><%=messasgeBean.getMachineName()%></td>
		<td class="x-data-table-td"><%=messasgeBean.getOrgName()%></td>
		<td class="x-data-table-td"><%=messasgeBean.getContent()%></td>
		<td class="x-data-table-td"><%=messasgeBean.getTime()%></td>
	</tr>
	<%
		}
		}
	%>
</table>
</div>
<div><input type="hidden" id="pageNumber" name="pageNumber"
	value="<%=pageNumber%>" /> <input type="button" <%=firstPageDisabled%>
	id="firstPageButton" class="x-first-page" onclick="changePage(this)" />&nbsp;
<input type="button" <%=previousPageDisabled%> id="previousPageButton"
	class="x-previous-page" onclick="changePage(this)" />&nbsp; <input
	type="button" <%=nextPageDisabled%> id="nextPageButton"
	class="x-next-page" onclick="changePage(this)" />&nbsp; <input
	type="button" <%=lastPageDisabled%> id="lastPageButton"
	class="x-last-page" onclick="changePage(this)" />&nbsp;[当前第<%=pageNumber%>页/共<%=totalPageCount%>页][每页<%=page_view%>条][共<%=total_num%>条]
</div>
</div>
</form>
</body>
</html>
