<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="th.entity.ChannelUseBean"%>
<%@page import="th.entity.ChannelUseOrgBean"%>
<%@page import="th.entity.ChannelUseDetailBean"%>
<%@page import="th.entity.ChannelUseHeaderBean"%>
<%@page import="th.util.StringUtils"%>
<%@page import="th.util.DateUtil"%>
<%@page import="th.com.util.Define4Report"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>频道使用统计表</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path;
%>
<%
	String dataType = (String) request.getAttribute( Define4Report.REQ_PARAM_DATA_TYPE );
	String timeType = (String) request.getAttribute( Define4Report.REQ_PARAM_TIME_TYPE );
	String pageNumber = (String) request.getAttribute( Define4Report.REQ_PARAM_PAGE_NUMBER );
	String totalPageCount = (String) request.getAttribute( Define4Report.REQ_PARAM_TOTAL_PAGE_COUNT );
	String sortKey = (String) request.getAttribute( Define4Report.REQ_PARAM_SORT_KEY );
	String macType = (String) request.getAttribute( Define4Report.REQ_PARAM_MAC_TYPE );
	String selectedOrgId = (String) request.getAttribute( Define4Report.REQ_PARAM_SELECTED_ORG_ID );
	
	String dayTypeTime = (String) request.getAttribute( Define4Report.REQ_PARAM_DAY_TYPE_TIME );
	String weekTypeTime = (String) request.getAttribute( Define4Report.REQ_PARAM_WEEK_TYPE_TIME );
	String monthTypeTime = (String) request.getAttribute( Define4Report.REQ_PARAM_MONTH_TYPE_TIME );
	String anyTypeStartTime = (String) request.getAttribute( Define4Report.REQ_PARAM_ANY_TYPE_START_TIME );
	String anyTypeEndTime = (String) request.getAttribute( Define4Report.REQ_PARAM_ANY_TYPE_END_TIME );	
	HashMap[] MTmap =  (HashMap[])request.getAttribute( "MTmap" );

	String orgs = (String) request.getAttribute( "orgs" );
	ArrayList<ChannelUseHeaderBean> tableHeaderList = (ArrayList<ChannelUseHeaderBean>) request.getAttribute( "tableHeaderList" );
	ArrayList<ChannelUseOrgBean> viewList = (ArrayList<ChannelUseOrgBean>) request.getAttribute( "viewList" );
	ArrayList<ChannelUseDetailBean> detailViewList = (ArrayList<ChannelUseDetailBean>) request.getAttribute( "detailViewList" );	
%>
<%
	dataType = StringUtils.isBlank( dataType ) ? Define4Report.DATA_TYPE_SUMMARY : dataType;
	timeType = StringUtils.isBlank( timeType ) ? Define4Report.TIME_TYPE_ANY : timeType;
	pageNumber = StringUtils.isBlank( pageNumber ) ? "1" : pageNumber;
	totalPageCount = StringUtils.isBlank( totalPageCount ) ? "1" : totalPageCount;
	sortKey = StringUtils.isBlank( sortKey ) ? "" : sortKey;
	selectedOrgId = StringUtils.isBlank( selectedOrgId ) ? "" : selectedOrgId;
	macType = StringUtils.isBlank( macType ) ? "0" : macType;
	
	String currentDate = DateUtil.getYesterdayDate(Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD);
	dayTypeTime = StringUtils.isBlank( dayTypeTime ) ? currentDate : dayTypeTime;
	weekTypeTime = StringUtils.isBlank( weekTypeTime ) ? currentDate : weekTypeTime;
	monthTypeTime = StringUtils.isBlank( monthTypeTime ) ? currentDate : monthTypeTime;	
	anyTypeStartTime = StringUtils.isBlank( anyTypeStartTime ) ? currentDate : anyTypeStartTime;
	anyTypeEndTime = StringUtils.isBlank( anyTypeEndTime ) ? currentDate : anyTypeEndTime;	
%>
<%
	String summaryRadioChecked = ( Define4Report.DATA_TYPE_SUMMARY.equals( dataType ) ) ? "checked='checked'" : "";
	String detailRadioChecked = ( Define4Report.DATA_TYPE_DETAIL.equals( dataType ) ) ? "checked='checked'" : "";
	String summaryDivStyle = ( Define4Report.DATA_TYPE_SUMMARY.equals( dataType ) ) ? "" : "display:none";
	String detailDivStyle = ( Define4Report.DATA_TYPE_DETAIL.equals( dataType ) ) ? "" : "display:none";
	
	String dayRadioChecked = ( Define4Report.TIME_TYPE_DAY.equals( timeType ) ) ? "checked='checked'" : "";
	String weekRadioChecked = ( Define4Report.TIME_TYPE_WEEK.equals( timeType ) ) ? "checked='checked'" : "";
	String monthRadioChecked = ( Define4Report.TIME_TYPE_MONTH.equals( timeType ) ) ? "checked='checked'" : "";
	String anyRadioChecked = ( Define4Report.TIME_TYPE_ANY.equals( timeType ) ) ? "checked='checked'" : "";
	String dayTypeStyle = ( Define4Report.TIME_TYPE_DAY.equals( timeType ) ) ? "" : "display:none";
	String weekTypeStyle = ( Define4Report.TIME_TYPE_WEEK.equals( timeType ) ) ? "" : "display:none";
	String monthTypeStyle = ( Define4Report.TIME_TYPE_MONTH.equals( timeType ) ) ? "" : "display:none";
	String anyTypeStyle = ( Define4Report.TIME_TYPE_ANY.equals( timeType ) ) ? "" : "display:none";
	
	String firstPageDisabled = Integer.parseInt( pageNumber ) < 2 ? "disabled" : "";
	String previousPageDisabled = Integer.parseInt( pageNumber ) < 2 ? "disabled" : "";
	String nextPageDisabled = Integer.parseInt( pageNumber ) >= Integer.parseInt( totalPageCount ) ? "disabled": "";
	String lastPageDisabled = Integer.parseInt( pageNumber ) >= Integer.parseInt( totalPageCount ) ? "disabled": "";
	String exportDisabled = (viewList!=null && viewList.size()>0)||(detailViewList!=null && detailViewList.size()>0)? "": "disabled";
	
	long headerOtherCols = ( Define4Report.DATA_TYPE_SUMMARY.equals( dataType ) ) ? 4:3;
	long tableHeaderCols = tableHeaderList != null ? tableHeaderList.size()+headerOtherCols : 0;
	String tableHeaderWidth = tableHeaderList!=null ? tableHeaderCols*200 + "px":"99999px";
	//String divHeaderWidth = tableHeaderList!=null ? tableHeaderCols*200-200 + "px":"99999px";
			
%>

<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/report.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/zTree/css/demo.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/zTree/css/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="<%=basePath%>/js/Calendar.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery.ztree.excheck-3.5.js"></script>
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
	
  	function doSearch(){
  		if(check()){
	  	document.machineOpenRateForm.action = "/th/Report?reportPage=2&functionCode=2";
	  	document.machineOpenRateForm.submit();
  	}else{
	  	  	alert('请选择时间');
	  	}
  	} 
  	
  	function exportReport(){
  		if(check()){
	  	document.machineOpenRateForm.action = "/th/Report?reportPage=2&functionCode=3";
	  	document.machineOpenRateForm.submit();
  	}else{
	  	  	alert('请选择时间');
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
	  	document.machineOpenRateForm.action = "/th/Report?reportPage=2&functionCode=2";
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
    function check(){
    	var rodioIds = "day,week,month,any".split(',');
    	for (var i = 0; i < rodioIds.length; i++) {
    		var rodioId = rodioIds[i];
    		var tempId = rodioId +"Type";
    		if(document.getElementById(tempId).style.display == ''){
    			if("anyType" == tempId){
    				var timeStart = $('#'+tempId+'StartTime').val();
    				var timeEnd = $('#'+tempId+'EndTime').val();
    				if(timeStart =='' || timeEnd ==''){
    					return false;
    				}else{
        				return true;
        			}
    			} else{
    				var time = $('#'+tempId+'Time').val();
    				if(time ==''){
    					return false;
    				}else{
        				return true;
        			}
    			}
    		}
    	}
    }	
</script>
</head>
<body onload="init()">
<form class="x-client-form" action="" name="machineOpenRateForm" method="post">
  <div class="x-title"><span>&nbsp;&nbsp;报表管理-频道使用统计</span></div>
  <table><tr style ="heigt:15px"></tr></table>
  <div class="x-title">
    <div id="searchFormStyleId" class="x-fold-minus" onclick="onFold('searchFormId');"/>
  </div>
  <div style="height:26px;text-align:left;line-height:26px">检索</div>
  </div>
  <div id="searchFormId" style="background-color:#B2DFEE">
    <table cellpadding="0" border="0">
      <tr>
        <td width="5%" style="border:0;text-align:left">&nbsp;&nbsp;组&nbsp;&nbsp;&nbsp;织：</td>
        <td width="28%" style="border:0;text-align:left"> 
            <select id="orgSelect" name="orgSelect"></select>
        </td>
        <td width="33%" style="border:0;text-align:left">生成类型：
		  <label><input type="radio" name="dataType" value="<%=Define4Report.DATA_TYPE_SUMMARY%>" <%=summaryRadioChecked%> onclick="changeDataTypeRodio('summary');"/>汇总数据</label>
          <label><input type="radio" name="dataType" value="<%=Define4Report.DATA_TYPE_DETAIL%>" <%=detailRadioChecked%> onclick="changeDataTypeRodio('detail');"/>明细数据</label>
		 </td>
		<td width="7%" style="border:0;text-align:left">&nbsp;&nbsp;排&nbsp;&nbsp;&nbsp;序：</td>
 		<td width="26%" style="border:0;text-align:left">
 		    <select id="summarySortSelect" name="sortKey" style="<%=summaryDivStyle%>">
 		      <option value='orgName' <%if("orgName".equals(sortKey)){ %>selected="selected"<%}%>>机构名称</option>
              <option value='totalCount' <%if("totalCount".equals(sortKey)){ %>selected="selected"<%}%>>总次数</option>
              <option value='totalMachineCount' <%if("totalMachineCount".equals(sortKey)){ %>selected="selected"<%}%>>总台数</option>
              <option value='averageCount'  <%if("averageCount".equals(sortKey)){ %>selected="selected"<%}%>>平均每台次数</option>
            </select>
             <select id="detailSortSelect" name="detailSortKey" style="<%=detailDivStyle%>">
              <option value='detailMachineName'  <%if("detailMachineName".equals(sortKey)){ %>selected="selected"<%}%>>机器名称</option>
              <option value='detailOrgName'  <%if("detailOrgName".equals(sortKey)){ %>selected="selected"<%}%>>机构名称</option>
              <option value='detailTotalCount'  <%if("detailTotalCount".equals(sortKey)){ %>selected="selected"<%}%>>总次数</option>
            </select>
 		</td> 
      </tr>
      <tr>
        <td width="5%" style="border:0;text-align:left">&nbsp;&nbsp;时&nbsp;&nbsp;&nbsp;间：</td>
        <td width="28%" style="border:0;text-align:left">
		  <label><input type="radio" id="day" name="timeType" value="<%=Define4Report.TIME_TYPE_DAY%>" <%=dayRadioChecked%> onclick="changeTimeTypeRodio(this)"/>日报&nbsp;&nbsp;</label>
          <label><input type="radio" id="week" name="timeType" value="<%=Define4Report.TIME_TYPE_WEEK%>" <%=weekRadioChecked%> onclick="changeTimeTypeRodio(this)" />周报&nbsp;&nbsp;</label>
          <label><input type="radio" id="month" name="timeType" value="<%=Define4Report.TIME_TYPE_MONTH%>" <%=monthRadioChecked%> onclick="changeTimeTypeRodio(this)" />月报&nbsp;&nbsp;</label>
          <label><input type="radio" id="any" name="timeType" value="<%=Define4Report.TIME_TYPE_ANY%>" <%=anyRadioChecked%> onclick="changeTimeTypeRodio(this)" />任意时间段&nbsp;&nbsp;</label>
		</td>
		<td style="<%=dayTypeStyle%>" id="dayType" width="33%" style="border:0;text-align:left">
		  统计时间：<input type="text" size="10" id="dayTypeTime" name="dayTypeTime"  value="<%=dayTypeTime%>" onclick="getDateString(this,oCalendarChs)"/>
		</td>
		<td style="<%=weekTypeStyle%>" id="weekType" width="33%" style="border:0;text-align:left">
		  统计时间：<input type="text" size="10" id="weekTypeTime" name="weekTypeTime" value="<%=weekTypeTime%>" onclick="getDateString(this,oCalendarChs)"/>
		</td>
		<td style="<%=monthTypeStyle%>" id="monthType" width="33%" style="border:0;text-align:left">
		  统计时间：<input type="text" size="10" id="monthTypeTime" name="monthTypeTime" value="<%=monthTypeTime%>" onclick="getDateString(this,oCalendarChs)"/>
		</td>				
        <td style="<%=anyTypeStyle%>" id="anyType" width="33%" style="border:0;text-align:left">
		  起始时间：<input type="text" size="10" id="anyTypeStartTime" name="anyTypeStartTime" value="<%=anyTypeStartTime%>" onclick="getDateString(this,oCalendarChs)"/> 
		  结束时间: <input type="text" size="10" id="anyTypeEndTime" name="anyTypeEndTime" value="<%=anyTypeEndTime%>" onclick="getDateString(this,oCalendarChs)"/>
		</td>
		<td width="7%" style="border:0;text-align:left">&nbsp;&nbsp;设备类型：</td>
 		<td width="26%" style="border:0;text-align:left">
 		    <select id="macType" name="macType">
 		      <option value='0' <%if("0".equals(macType)){ %>selected="selected"<%}%>>全部</option>
 		      <%if (null != MTmap && MTmap.length != 0){
            	  for (int i = 0; i < MTmap.length; i++) {
            		  String str = MTmap[i].get("OS").toString()+ "_"+ MTmap[i].get("MACHINE_KIND").toString();
            		  String macName = MTmap[i].get("TYPE_NAME").toString();
              %>
              <option value="<%=str %>" <%if(str.equals(macType)){ %>selected="selected"<%}%>><%=macName %></option>
              <%		
                  }
              }%>
            </select>
 		</td> 
      </tr> 
      <tr>
			<td colspan="3"/>
			<td colspan="3" style="border:0;text-align:left"><span>&nbsp;&nbsp;</span><input type="button" class="x-button" value="检索" onclick ="doSearch()" /></td>
	  </tr>
    </table>
  </div>
  <p></p>
  <div>
  	<div class="x-data"><span  style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span></div>
    <div style="width:100%;overflow-x:auto;overflow-y:hidden;"><div id="summaryDataId" style="width:<%=tableHeaderWidth%>; <%=summaryDivStyle%>">
      <table class="x-data-table" style="width:<%=tableHeaderWidth%>;">
        <tr>
          <th colspan ="<%=tableHeaderCols%>" class="x-data-table-th">频道使用汇总表</th>
        </tr>             
        <tr>
        	<th style="width:300px" class="x-data-table-th">机构名称</th>
 		<%
 			if ( tableHeaderList != null && tableHeaderList.size() > 0 ) {
 				for ( int i = 0; i < tableHeaderList.size(); i++ ) {
 					ChannelUseHeaderBean channelUseHeader = (ChannelUseHeaderBean) tableHeaderList.get( i );
 					String headerName  = channelUseHeader.getChannelName();
 		%>        
          <th style="width:200px" class="x-data-table-th"><%=headerName%></th>
 		<%
				}
			}
		%>
			<th style="width:150px" class="x-data-table-th">总次数</th>
			<th style="width:150px" class="x-data-table-th">总台数</th>
			<th style="width:150px" class="x-data-table-th">平均每台次数</th>          
        </tr>
 		<%
		if ( viewList != null && viewList.size() > 0 ) {
			for ( int i = 0; i < viewList.size(); i++ ) {
				ChannelUseOrgBean orgChannelUse = (ChannelUseOrgBean) viewList.get( i );
				ArrayList<ChannelUseBean> channelUseList = orgChannelUse.getChannelUseList();
				String trClass = ( i % 2 != 0 ) ? "x-data-table-tr-white" : "x-data-table-tr-gray";
 		%>
			<tr class="<%=trClass%>" onmouseover='OMOver(this);' onmouseout='OMOut(this);'>
				<td class="x-data-table-td"><%=orgChannelUse.getOrgName()%></td>
				<%
	 			if ( channelUseList != null && channelUseList.size() > 0 ) {
	 				for ( int channelIndex = 0; channelIndex < channelUseList.size(); channelIndex++ ) {
	 					ChannelUseBean channelUse = (ChannelUseBean) channelUseList.get( channelIndex );
				%>
				<td class="x-data-table-td"><%=channelUse.getClickCount()%></td>
				<%
					}
				}
				%> 
				<td class="x-data-table-td"><%=orgChannelUse.getTotalCount()%></td>
				<td class="x-data-table-td"><%=orgChannelUse.getTotalMachineCount()%></td>
				<td class="x-data-table-td"><%=orgChannelUse.getAverageCount()%></td>			
			</tr>
		<%
			}
		}
		%>        
      </table>
    </div></div>
    <div style="width:100%;overflow-x:auto;overflow-y:hidden;"><div id="detailDataId" style="width:<%=tableHeaderWidth%>;<%=detailDivStyle%>">
      <table class="x-data-table">
        <tr>
          <th colspan ="<%=tableHeaderCols%>" class="x-data-table-th">频道使用明细表</th>
        </tr>
        <tr>
        	<th style="width:200px" class="x-data-table-th">机器名称</th>
        	<th style="width:300px" class="x-data-table-th">机构名称</th>
 		<%
 			if ( tableHeaderList != null && tableHeaderList.size() > 0 ) {
 				for ( int i = 0; i < tableHeaderList.size(); i++ ) {
 					ChannelUseHeaderBean channelUseHeader = (ChannelUseHeaderBean) tableHeaderList.get( i );
 					String headerName  = channelUseHeader.getChannelName();
 		%>        
          <th style="width:200px" class="x-data-table-th"><%=headerName%></th>
 		<%
				}
			}
		%>
			<th style="width:150px" class="x-data-table-th">总次数</th>         
        </tr>
 		<%
		if ( detailViewList != null && detailViewList.size() > 0 ) {
			for ( int i = 0; i < detailViewList.size(); i++ ) {
				ChannelUseDetailBean channelUseDetail = (ChannelUseDetailBean) detailViewList.get( i );
				ArrayList<ChannelUseBean> channelUseList = channelUseDetail.getChannelUseList();
				String trClass = ( i % 2 != 0 ) ? "x-data-table-tr-white" : "x-data-table-tr-gray";
 		%>
			<tr class="<%=trClass%>" onmouseover='OMOver(this);' onmouseout='OMOut(this);'>
				<td class="x-data-table-td"><%=channelUseDetail.getMachineName()%></td>
				<td class="x-data-table-td"><%=channelUseDetail.getOrgName()%></td>
				<%
	 			if ( channelUseList != null && channelUseList.size() > 0 ) {
	 				for ( int channelIndex = 0; channelIndex < channelUseList.size(); channelIndex++ ) {
	 					ChannelUseBean channelUse = (ChannelUseBean) channelUseList.get( channelIndex );
				%>
				<td class="x-data-table-td"><%=channelUse.getClickCount()%></td>
				<%
					}
				}
				%> 
				<td class="x-data-table-td"><%=channelUseDetail.getTotalCount()%></td>
			</tr>
		<%
			}
		}
		%>        
      </table>
    </div></div>
    <div> 
    	<input type="hidden" id="pageNumber" name="pageNumber" value="<%=pageNumber%>" />
        <input type="button" <%=firstPageDisabled%> id="firstPageButton" class="x-first-page" onclick="changePage(this)" />&nbsp;
    	<input type="button" <%=previousPageDisabled%> id="previousPageButton" class="x-previous-page" onclick="changePage(this)"/>&nbsp;
    	<input type="button" <%=nextPageDisabled%> id="nextPageButton" class="x-next-page" onclick="changePage(this)"/>&nbsp;
    	<input type="button" <%=lastPageDisabled%> id="lastPageButton" class="x-last-page" onclick="changePage(this)"/>&nbsp;[当前第<%=pageNumber%>页/共<%=totalPageCount%>页]
    	<input type="button" <%=exportDisabled%> id="exportButton" class="rightBtn" value="导出" onclick="exportReport()" />
    </div>
  </div>
</form>
</body>
</html>
