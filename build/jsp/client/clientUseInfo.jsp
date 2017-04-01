<%@ page contentType="text/html; charset=utf-8" language="java"
	pageEncoding="UTF-8" import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="copyright" content="Copyright(c) 天和. All Rights Reserved.">
	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/css/channel.css" />
	<link rel="stylesheet" href="../../zTree/css/demo.css" type="text/css">
		<link rel="stylesheet"
			href="../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
			<script type="text/javascript"
				src="../../zTree/js/jquery-1.4.4.min.js"></script>
			<script type="text/javascript"
				src="../../zTree/js/jquery.ztree.core-3.5.js"></script>
			<script type="text/javascript"
				src="../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
			<script type="text/javascript"
				src="<%=request.getContextPath()%>/js/Calendar.js"></script>
			<%@ page import="th.entity.ClientBean"%>
			<%@ page import="th.entity.ClientUseBean"%>
			<%@ page import="th.entity.OrganizationBean"%>
			<%@ page import="th.com.util.Define"%>
			<%@ page import="java.util.*"%>
			<%@ page import="th.user.*"%>
			<%@ page import="th.util.StringUtils"%>
			<%@ page import="org.apache.commons.logging.Log"%>
			<%@ page import="org.apache.commons.logging.LogFactory"%>
			<%
				Log logger = LogFactory.getLog( this.getClass() );
				User user = (User) session.getAttribute( "user_info" );
				String realname = null;
				if ( user == null ) {
					response.setContentType( "text/html; charset=utf-8" );
					response.sendRedirect( "/th/index.jsp" );
				}
				else {
					realname = user.getReal_name();
					logger.info( "获得当前用户的用户名，用户名是： " + realname );
				}
				String pageNo = (String) request.getAttribute( "pageNo" );
				Integer dataCountInteger = (Integer) request.getAttribute( "dataCount" );
				Integer totalPageNumInteger = (Integer) request.getAttribute( "totalPageNum" );
				String orgId = (String)request.getAttribute( "orgId" );
				orgId = StringUtils.isBlank( orgId ) ? "" : orgId;
				String firstPageDisabled = Integer.parseInt( pageNo ) < 2 ? "disabled" : "";
				String previousPageDisabled = Integer.parseInt( pageNo ) < 2 ? "disabled" : "";
				String nextPageDisabled = Integer.parseInt( pageNo ) >= totalPageNumInteger  ? "disabled": "";
				String lastPageDisabled = Integer.parseInt( pageNo ) >= totalPageNumInteger  ? "disabled": "";
			%>	
			<script type="text/javascript">
window.onload = function showtable() {
    var tablename = document.getElementById("dataTableId");
    var li = tablename.getElementsByTagName("tr");
    for (var i = 0; i < li.length; i++) {
        if (i % 2 == 0) {
            li[i].style.backgroundColor = "#E5EEFD";
            li[i].onmouseover = function () {
                this.style.backgroundColor = "#CAE8EA";
            }
            li[i].onmouseout = function () {
                this.style.backgroundColor = "#E5EEFD";
            }
        }
        else {
            li[i].style.backgroundColor = "#FFFFFF";
            li[i].onmouseover = function () {
                this.style.backgroundColor = "#CAE8EA";
            }
            li[i].onmouseout = function () {
                this.style.backgroundColor = "#FFFFFF";
            }
        }
    }
    
    		var today=new Date();
            var yesterday_milliseconds=today.getTime()-1000*60*60*24;
             
            var yesterday=new Date();      
            yesterday.setTime(yesterday_milliseconds);      
                
            var strYear=yesterday.getFullYear();   
            var strDay=yesterday.getDate();   
            var strMonth=yesterday.getMonth()+1;   
            if(strMonth<10)   
            {   
                strMonth="0"+strMonth;   
            }   
    		if(strDay<10)   
            {   
                strDay="0"+strDay;   
            } 
            var time =strYear+"-"+strMonth+"-"+strDay; 
    
//     var date = new Date();
//     var year = date.getFullYear();
//     var month = date.getMonth();
//     var day = date.getDate();
    
//     //改成昨天
//     day = day - 1;
//     if(day.toString().length === 1){
//     	day = '0' +  day.toString();
//     } 
    
//     alert(date-24*60*60);
    
//     var time = year + '-' + (Number(month) + 1) + '-' + day;
//     if(month.toString().length === 1){
//     	time = year + '-0' + (Number(month) + 1) + '-' + day;
//     }
 
    
    document.getElementById('selectDateDayId').value = time;

    document.getElementById('selectDateWeekId').value = time;

    document.getElementById('selectDateMonthId').value = time;
    
    document.getElementById('selectDateAnyStartId').value = time;

    document.getElementById('selectDateAnyEndId').value = time;
    
    if('<%=request.getAttribute( "dataType" )%>' != 'null'){
    	 showData('<%=request.getAttribute( "dataType" )%>');
    }
   
    if('<%=request.getAttribute( "dataType" )%>' !='null' && '<%=request.getAttribute( "dataType" )%>' === 'detail'){
    	document.getElementById('detailRadioId').checked =true;
    }
    if('<%=request.getAttribute( "dataType" )%>' != 'null'){
    	document.getElementById('<%=request.getAttribute( "searchTimeType" )%>Id').checked =true;
    }
    if('<%=request.getAttribute( "searchTimeType" )%>' === 'day'){
    	this.dayReportSelect();
    	document.getElementById('selectDateDayId').value = '<%=request.getAttribute( "day" )%>';
    }else if('<%=request.getAttribute( "searchTimeType" )%>' === 'week'){
    	this.weekReportSelect();
    	document.getElementById('selectDateWeekId').value = '<%=request.getAttribute( "week" )%>';
    }else if('<%=request.getAttribute( "searchTimeType" )%>' === 'month'){
    	this.monthReportSelect();
<%--     	document.getElementById('selectDateMonthId').value = '<%=request.getAttribute("month")%>'; --%>
    }else if('<%=request.getAttribute( "searchTimeType" )%>' === 'anyTime'){
    	this.anyTimeReportSelect();
    	document.getElementById('selectDateAnyStartId').value = '<%=request.getAttribute( "startTime" )%>';
    	document.getElementById('selectDateAnyEndId').value = '<%=request.getAttribute( "endTime" )%>';
    }
    var seqType = '<%=request.getAttribute( "sequenceType" )%>';
    if( seqType != 'null'){
    	document.getElementById('sequenceTypeId').value = seqType;
    }

    var selectedOrgId='<%=orgId%>';
    if(selectedOrgId&&selectedOrgId!=''){
 		var orgSelect = document.getElementById("selectOrgName");
 		for (var i = 0; i < orgSelect.options.length; i++) {        
 	        if (orgSelect.options[i].value == selectedOrgId) {        
 	        	orgSelect.options[i].selected = true;        
 	            break;        
 	        }     
 		}
 	}
    
}
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
  function showData(id){ 
  	if(id === 'all'){
		document.getElementById('allDataId').style.display = '';
		document.getElementById('detailDataId').style.display = 'none';
	}else if(id === 'detail'){
		document.getElementById('allDataId').style.display = 'none';
		document.getElementById('detailDataId').style.display = '';
	}
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
	var zNodes = <%=(String) request.getAttribute( "MONITOR_ORG_LIST" )%> ;

  $(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		
		var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
		var v = "", w = "";
		// 设置组织名称下拉列表值 
		var orgList = document.getElementById("selectOrgName");
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
      if (v.length > 0 ) v = v.substring(0, v.length-1);  
      var cityObj = $("#citySel");  
      cityObj.attr("value", v);//设置文本框的值  
      
      //选中一个节点后关闭div
      hideMenu();
      //alert(nid);
      window.document.form_data.orderType.value ="2";
  	window.document.form_data.pageID.value ="<%=Define.JSP_MONITOR_CLIENT_RUNNING_ID%>";
  	window.document.form_data.orgID.value = nid;
      window.document.form_data.action = "<%=Define.MONITOR_SERVLET%>";
      window.document.form_data.target = "_self";
      window.document.form_data.submit();
  } 
  function dayReportSelect(){
	  document.getElementById('dayReport').style.display = '';
	  document.getElementById('weekReport').style.display = 'none';
	  document.getElementById('monthReport').style.display = 'none';
	  document.getElementById('anyTimeReport').style.display = 'none';
  }
  function weekReportSelect(){
	  document.getElementById('dayReport').style.display = 'none';
	  document.getElementById('weekReport').style.display = '';
	  document.getElementById('monthReport').style.display = 'none';
	  document.getElementById('anyTimeReport').style.display = 'none';
  }
  function monthReportSelect(){
	  document.getElementById('dayReport').style.display = 'none';
	  document.getElementById('weekReport').style.display = 'none';
	  document.getElementById('monthReport').style.display = '';
	  document.getElementById('anyTimeReport').style.display = 'none';
  }
  function anyTimeReportSelect(){
	  document.getElementById('dayReport').style.display = 'none';
	  document.getElementById('weekReport').style.display = 'none';
	  document.getElementById('monthReport').style.display = 'none';
	  document.getElementById('anyTimeReport').style.display = '';
  }
  function searchClientUseReport(){
	  var form = document.getElementById('clientUseForm');
	  document.getElementById('searchInfoId').value = 'searchClientUseReport';
	  //汇总 or 详细
	  var dataTypeRadio = document.getElementsByName('dataTypeRadio');
	  if(dataTypeRadio[0].checked){
		  document.getElementById('dataTypeId').value = 'allData';
	  }
	  if(dataTypeRadio[1].checked){
		  document.getElementById('dataTypeId').value = 'detailData';
	  }
	  //时间选择
	  if(document.getElementById('dayId').checked == true){
		  document.getElementById('searchTimeTypeId').value='day';
		  if(document.getElementById('selectDateDayId').value === ''){
			  alert('请选择日期!');
			  return;
		  }
	  }
	  if(document.getElementById('weekId').checked == true){
		  document.getElementById('searchTimeTypeId').value='week';
		  if(document.getElementById('selectDateWeekId').value === ''){
			  alert('请选择日期!');
			  return;
		  }
	  }
	  if(document.getElementById('monthId').checked == true){
		  document.getElementById('searchTimeTypeId').value='month';
	  }
	  if(document.getElementById('anyTimeId').checked == true){
		  document.getElementById('searchTimeTypeId').value='anyTime';
		  var startTime = document.getElementById('selectDateAnyStartId').value;
		  var endTime = document.getElementById('selectDateAnyEndId').value;
		  if(startTime === ''){
			  alert('请选择起始日期!');
			  return;
		  }
		  if(endTime === ''){
			  alert('请选择终止日期!');
			  return;
		  }
		  var regS = new RegExp("-","gi");
		  startTime = startTime.replace(regS,'');
		  endTime = endTime.replace(regS,'');
		  if(parseFloat(endTime) < parseFloat(startTime)){
			  alert('终止时间不能早于结束时间!');
			  return;
		  }
	  }
	  form.action = "/th/jsp/client/clientUseInfo.html";
	  form.submit();
  }
	function gotoPage(pageType) {
        document.getElementById('searchInfoId').value = "searchClientUseReport";
        if(document.getElementById('dayId').checked === true){
        	document.getElementById('searchTimeTypeId').value = "day";
        }else if(document.getElementById('monthId').checked === true){
        	document.getElementById('searchTimeTypeId').value = "month";
        }else if(document.getElementById('weekId').checked === true){
        	document.getElementById('searchTimeTypeId').value = "week";
        }else if(document.getElementById('anyTimeId').checked === true){
        	document.getElementById('searchTimeTypeId').value = "anyTime";
        }
        if(document.getElementById('detailRadioId').checked === true){
        	document.getElementById('dataTypeId').value = "detailData";
        }else{
        	document.getElementById('dataTypeId').value = "allData";
        }
        document.getElementById('pageTypeId').value = pageType;
        var form = document.getElementById('clientUseForm');
        form.action = "/th/jsp/client/clientUseInfo.html";
        form.submit();
    }
	function exportReport(){
  		document.getElementById('searchInfoId').value = "reportClientUse";
  		var form = document.getElementById('clientUseForm');
  		if(document.getElementById('detailRadioId').checked === true){
        	document.getElementById('dataTypeId').value = "detailData";
        }else{
        	document.getElementById('dataTypeId').value = "allData";
        }
  		if(document.getElementById('dayId').checked === true){
        	document.getElementById('searchTimeTypeId').value = "day";
        }else if(document.getElementById('monthId').checked === true){
        	document.getElementById('searchTimeTypeId').value = "month";
        }else if(document.getElementById('weekId').checked === true){
        	document.getElementById('searchTimeTypeId').value = "week";
        }else if(document.getElementById('anyTimeId').checked === true){
        	document.getElementById('searchTimeTypeId').value = "anyTime";
        }
  		form.action = "/th/jsp/client/clientUseInfo.html";
        form.submit();
  	} 
	
</script>
<title>设备使用统计表</title>
</head>
<body>
		<div class="x-title"><span>&nbsp;&nbsp;报表管理-设备使用统计</span></div>
		<table><tr style ="heigt:15px"></tr></table>
		<form class="x-client-form" id="clientUseForm">
			<input type="hidden" name="dataType" id="dataTypeId" /> 
			<input type="hidden" name="searchTimeType" id="searchTimeTypeId" /> 
			<input type="hidden" name="type" id="searchInfoId" /> 
			<input type="hidden" name="pageType" id="pageTypeId" />
			<div class="x-title">
				<div id="clientStyleId" class="x-fold-minus" onclick="onFold('clientId');" />
			</div>
			<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
			</div>
			<div id="clientId" style="background-color:#B2DFEE">
				<table cellpadding="0" border="0">
					<tr>
						<td width="5%" style="border:0;text-align:left">&nbsp;&nbsp;组&nbsp;&nbsp;&nbsp;织：</td>
						<td width="28%" style="border:0;text-align:left"> 
							<select id="selectOrgName" name="selectOrgName"></select>
						</td>
						<td width="33%" style="border:0;text-align:left">生成类型：
							<label><input type="radio" name="dataTypeRadio" value="1" checked="checked" onClick="showData('all');" />汇总数据</label> 
							<label><input type="radio" name="dataTypeRadio" id="detailRadioId" value="0" onClick="showData('detail');" />明细数据</label>
						</td>
						<td width="5%" style="border:0;text-align:left">&nbsp;&nbsp;排&nbsp;&nbsp;&nbsp;序：</td>
						<td width="28%" style="border:0;text-align:left">
							<select name="sequenceType" id="sequenceTypeId">
								<option value="1">使用率升序</option>
								<option value="0" selected=true>使用率降序</option>
							</select>
						</td>
					</tr>
					<tr>
						<td width="5%" style="border:0;text-align:left">&nbsp;&nbsp;时&nbsp;&nbsp;&nbsp;间：</td>
						<td width="28%" style="border:0;text-align:left">
							<label> <input type="radio" name="time" id="dayId" value="day" onClick="dayReportSelect();" />日报&nbsp;&nbsp;</label> 
							<label> <input type="radio" id="weekId" name="time" value="week" onClick="weekReportSelect();" />周报&nbsp;&nbsp;</label> 
							<label> <input type="radio" id="monthId" name="time" value="month" onClick="monthReportSelect();" />月报&nbsp;&nbsp;</label> 
							<label> <input type="radio" id="anyTimeId" name="time" value="any" checked="true" onClick="anyTimeReportSelect();" />任意时间段&nbsp;&nbsp;</label>
						</td>
						<!-- <td style="border: 0; text-align: left" id="tjsj">统计时间：</td> -->
						<td style="display: none" id="dayReport" width="33%" style="border:0;text-align:left">
							统计时间：<input type="text" name="selectDateDay" size="10" id="selectDateDayId" onClick="getDateString(this,oCalendarChs)" />
						</td>
						<td style="display: none" id="weekReport" width="33%" style="border:0;text-align:left">
							统计时间：<input type="text" name="selectDateWeek" size="10" id="selectDateWeekId" onclick="getDateString(this,oCalendarChs)" />
						</td>
						<td style="display: none" id="monthReport" width="33%" style="border:0;text-align:left">
							统计时间：<input type="text" name="selectDateMonth" size="10" id="selectDateMonthId" onClick="getDateString(this,oCalendarChs)" />
						</td>
						<td id="anyTimeReport" width="33%" style="border:0;text-align:left">
		  					起始时间：<input type="text" size="10" name="selectDateAnyStart" id="selectDateAnyStartId" onclick="getDateString(this,oCalendarChs)"/> 
		 					结束时间: <input type="text" size="10" name="selectDateAnyEnd" id="selectDateAnyEndId" onclick="getDateString(this,oCalendarChs)"/>
						</td>
<!-- 						<td style="border: 0; text-align: left">
							<div id="dayReport">
								<input type="text" name="selectDateDay" size="10"
									id="selectDateDayId" onClick="getDateString(this,oCalendarChs)" />
							</div>
							<div id="weekReport" style="display: none">
								<input type="text" name="selectDateWeek" size="10"
									id="selectDateWeekId"
									onclick="getDateString(this,oCalendarChs)" />
							</div>
							<div id="monthReport" style="display: none">
								<input type="text" name="selectDateMonth" size="10"
									id="selectDateMonthId"
									onclick="getDateString(this,oCalendarChs)" />
																<select name="selectDateMonth" id="selectDateMonthId">
																</select>
							</div>
							<div id="anyTimeReport" style="display: none">
								<input type="text" size="10" name="selectDateAnyStart"
									id="selectDateAnyStartId"
									onclick="getDateString(this,oCalendarChs)" />结束时间:<input
									type="text" size="10" name="selectDateAnyEnd"
									id="selectDateAnyEndId"
									onclick="getDateString(this,oCalendarChs)" />
							</div>
						</td> -->
						<td style="border:0;text-align:left">
							<span>&nbsp;&nbsp;</span><input type="button" value="检索" onClick="searchClientUseReport();" class="x-button" />
						</td>
					</tr>
				</table>
			</div>
			<div>
				<div class="x-data"><span style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span></div>
				<div id="allDataId">
					<table class="x-data-table" id="dataTableId">
						<tr>
							<th style="width: 16%" class="x-data-table-th">机构名称</th>
							<th style="width: 16%" class="x-data-table-th">点击次数</th>
							<th style="width: 16%" class="x-data-table-th">有效时长(秒)</th>
							<th style="width: 16%" class="x-data-table-th">开机时长(秒)</th>
							<th style="width: 16%" class="x-data-table-th">使用率(%)</th>
							<th style="width: 16%" class="x-data-table-th">使用率排名</th>
						</tr>
						<%
							List allData = (List) request.getAttribute( "allData" );
							String exportDisabled = "";
							if ( allData != null ) {
								if ( allData.size() == 0 ) {
									exportDisabled = "disabled";
								}
								else {
									exportDisabled = "";
								}
							}
							if ( allData != null && allData.size() != 0 ) {
								if ( allData.size() >= 10 ) {
									for ( int i = 0; i < 10; i++ ) {
										ClientUseBean clientUseBean = (ClientUseBean) allData.get( i );
										Double rate = clientUseBean.getUseRate();
										String rateString = String.valueOf( rate );
										if ( rateString.length() > 4 ) {
											rateString = rateString.substring( 0, 4 );
										}
						%>
						<tr>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBean.getOrgName()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBean.getClickCount()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBean.getValidTime()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBean.getOpenTime()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=rateString%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=i + 1%></td>
						</tr>
						<%
							}
								}
								else {
									for ( int i = 0; i < allData.size(); i++ ) {
										ClientUseBean clientUseBean = (ClientUseBean) allData.get( i );
										Double rate = clientUseBean.getUseRate();
										String rateString = String.valueOf( rate );
										if ( rateString.length() > 4 ) {
											rateString = rateString.substring( 0, 4 );
										}
						%>
						<tr>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBean.getOrgName()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBean.getClickCount()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBean.getValidTime()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBean.getOpenTime()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=rateString%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=i + 1%></td>
						</tr>
						<%
							}
								}
							}
						%>
					</table>
				</div>
				<div id="detailDataId" style="display: none">
					<table class="x-data-table">
						<tr>
							<th style="width: 11%" class="x-data-table-th">归属分行</th>
							<th style="width: 9%" class="x-data-table-th">机器名</th>
							<th style="width: 9%" class="x-data-table-th">类型</th>
							<th style="width: 12%" class="x-data-table-th">规定时长(秒)</th>
							<th style="width: 13%" class="x-data-table-th">使用时间(秒)</th>
							<th style="width: 9%" class="x-data-table-th">空闲时间(秒)</th>
							<th style="width: 7%" class="x-data-table-th">使用率(%)</th>
						</tr>
						<%
							List detailData = (List) request.getAttribute( "detailData" );
							if ( detailData != null ) {
								if ( detailData.size() == 0 ) {
									exportDisabled = "disabled";
								}
								else {
									exportDisabled = "";
								}
							}
							int dataCount = dataCountInteger.intValue();
							if ( detailData != null && detailData.size() != 0 ) {
								if ( detailData.size() >= 10 ) {
									for ( int i = 0; i < 10; i++ ) {
										ClientUseBean clientUseBeanDetail = (ClientUseBean) detailData.get( i );
										Double rate = clientUseBeanDetail.getUseRate();
										String rateString = "";
										if ( rate != null ) {
											rateString = String.valueOf( rate );
											if ( rateString.length() > 4 ) {
												rateString = rateString.substring( 0, 4 );
											}
										}
						%>
						<tr>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getBranches()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getMachineId()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getMachineType()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getOpenTime()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getValidTime()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getFreeTime()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=rateString%></td>
						</tr>
						<%
							}
								}
								else {
									for ( int i = 0; i < detailData.size(); i++ ) {
										ClientUseBean clientUseBeanDetail = (ClientUseBean) detailData.get( i );
										Double rate = clientUseBeanDetail.getUseRate();
										Double freeRate = clientUseBeanDetail.getFreeUseRate();
										String rateString = "";
										if ( rate != null ) {
											rateString = String.valueOf( rate );
											if ( rateString.length() > 4 ) {
												rateString = rateString.substring( 0, 4 );
											}
										}
						%>
						<tr>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getBranches()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getMachineId()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getMachineType()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getOpenTime()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getValidTime()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientUseBeanDetail.getFreeTime()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=rateString%></td>
						</tr>
						<%
							}
								}
							}
						%>
					</table>
				</div>
				<div>
					<input type="button" <%=firstPageDisabled%> class="x-first-page" onClick="gotoPage('first')" />&nbsp; 
					<input type="button" <%=previousPageDisabled%> class="x-previous-page" onClick="gotoPage('previous')" />&nbsp; 
					<input type="button" <%=nextPageDisabled%> class="x-next-page" onClick="gotoPage('next')" />&nbsp;
					<input type="button" <%=lastPageDisabled%> class="x-last-page" onClick="gotoPage('last')" />&nbsp;[当前第<%=pageNo%>页/共<%=totalPageNumInteger%>页]
					<div style="display: none">
						<label>第 <input type="text" size="1" name="pageNoName"
							id="pageNoId" value="<%=pageNo%>" />页
						</label> <input type="button" class="x-goto-page" onClick="gotoPage('')" />
						<span>共<%=dataCount%>条数据
						</span>
					</div>
					<div style="float: right">
						<input type="button" <%=exportDisabled%> id="exportButton"
							class="x-button" value="导出" onclick="exportReport()" />
					</div>
				</div>
			</div>
		</form>
</body>
</html>
