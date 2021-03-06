<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="th.util.DateUtil"%>
<%@page import="th.util.StringUtils"%>
<%@ page import="th.action.report.ReportCommonAction" %>
<%@page import="th.com.util.Define4Report"%>
<%@page import="th.entity.DeviceRunningBean"%>
<%
	//用于获得工程根目录   然后使用它进行其他文件的引用
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path;
	String pageNumber = (String) request.getAttribute( Define4Report.REQ_PARAM_PAGE_NUMBER  );
	if(pageNumber==null||pageNumber.equals("")){
		pageNumber="1";
	}
	String totalPageCount = (String) request.getAttribute( Define4Report.REQ_PARAM_TOTAL_PAGE_COUNT );
	if(totalPageCount==null||totalPageCount.equals("")){
		totalPageCount="0";
	}
	ArrayList<DeviceRunningBean> viewList = (ArrayList<DeviceRunningBean>)request.getAttribute("viewList");
	//String allOrg=(String)request.getAttribute("allOrg");
	String selectedOrgId=(String)request.getAttribute(Define4Report.REQ_PARAM_ORG_SELECT);
	
	String timeType = (String) request.getAttribute( Define4Report.REQ_PARAM_TIME_TYPE );
	timeType = StringUtils.isBlank( timeType ) ? Define4Report.TIME_TYPE_ANY : timeType;
	
	String dayRadioChecked = ( Define4Report.TIME_TYPE_DAY.equals( timeType ) ) ? "checked='checked'" : "";
	String weekRadioChecked = ( Define4Report.TIME_TYPE_WEEK.equals( timeType ) ) ? "checked='checked'" : "";
	String monthRadioChecked = ( Define4Report.TIME_TYPE_MONTH.equals( timeType ) ) ? "checked='checked'" : "";
	String anyRadioChecked = ( Define4Report.TIME_TYPE_ANY.equals( timeType ) ) ? "checked='checked'" : "";
	String dayTypeStyle = ( Define4Report.TIME_TYPE_DAY.equals( timeType ) ) ? "" : "display:none";
	String weekTypeStyle = ( Define4Report.TIME_TYPE_WEEK.equals( timeType ) ) ? "" : "display:none";
	String monthTypeStyle = ( Define4Report.TIME_TYPE_MONTH.equals( timeType ) ) ? "" : "display:none";
	String anyTypeStyle = ( Define4Report.TIME_TYPE_ANY.equals( timeType ) ) ? "" : "display:none";
	
	String dayTypeTime = (String) request.getAttribute( Define4Report.REQ_PARAM_DAY_TYPE_TIME );
	String weekTypeTime = (String) request.getAttribute( Define4Report.REQ_PARAM_WEEK_TYPE_TIME );
	String monthTypeTime = (String) request.getAttribute( Define4Report.REQ_PARAM_MONTH_TYPE_TIME );
	String anyTypeStartTime = (String) request.getAttribute( Define4Report.REQ_PARAM_ANY_TYPE_START_TIME );
	String anyTypeEndTime = (String) request.getAttribute( Define4Report.REQ_PARAM_ANY_TYPE_END_TIME );	
	
	String currentDate = DateUtil.getYesterdayDate(Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD);
	dayTypeTime = StringUtils.isBlank( dayTypeTime ) ? currentDate : dayTypeTime;
	weekTypeTime = StringUtils.isBlank( weekTypeTime ) ? currentDate : weekTypeTime;
	monthTypeTime = StringUtils.isBlank( monthTypeTime ) ? currentDate : monthTypeTime;	
	anyTypeStartTime = StringUtils.isBlank( anyTypeStartTime ) ? currentDate : anyTypeStartTime;
	anyTypeEndTime = StringUtils.isBlank( anyTypeEndTime ) ? currentDate : anyTypeEndTime;	
	
	String orgs = ReportCommonAction.getOrgOrderNodes( request );
	String firstPageDisabled = Integer.parseInt( pageNumber ) < 2 ? "disabled" : "";
	String previousPageDisabled = Integer.parseInt( pageNumber ) < 2 ? "disabled" : "";
	String nextPageDisabled = Integer.parseInt( pageNumber ) >= Integer.parseInt( totalPageCount ) ? "disabled": "";
	String lastPageDisabled = Integer.parseInt( pageNumber ) >= Integer.parseInt( totalPageCount ) ? "disabled": "";
	String exportDisabled = (viewList!=null && viewList.size()>0)? "": "disabled";
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表管理-设备运行考核</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/report.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/zTree/css/demo.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/zTree/css/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="<%=basePath%>/js/Calendar.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script language="JavaScript">
	var oCalendarChs=new PopupCalendar("oCalendarChs"); //初始化控件时,请给出实例名称:oCalendarChs
	oCalendarChs.weekDaySting=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
	oCalendarChs.monthSting=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
	oCalendarChs.oBtnTodayTitle="今天";
	oCalendarChs.oBtnCancelTitle="取消";
	oCalendarChs.Init();
	
	function OMOver(OMO){OMO.style.backgroundColor='#CAE8EA';}
	
	function OMOut(OMO){OMO.style.backgroundColor='';} 
	
	function init(){
	
		var selectedOrgId='<%=selectedOrgId%>';
		if(selectedOrgId&&selectedOrgId!='null'){
			var orgSelect = document.getElementById("orgSelect");
			for (var i = 0; i < orgSelect.options.length; i++) {        
		        if (orgSelect.options[i].value == selectedOrgId) {        
		        	orgSelect.options[i].selected = true;        
		            break;        
		        }     
			}
		}
	}
	
	function check(){
		<%--document.form1.action = "/th/DeviceRunningServlet.html";
		var statisticDateInput = document.getElementById("statisticDate");
		var statisticDate= statisticDateInput.value;
		if(statisticDate){
			document.getElementById("pageNumber").value = "1";
			return true;
		}else{
			alert("请选择统计时间。");
			return false;
		}--%>
		var orgSelectedID = document.getElementById("orgSelect").value;
		if(-100 == orgSelectedID){
			alert("您所在分行暂不支持检索");
			return;
		}
		
		var rodioIds = "day,week,month,any".split(',');
		for (var i = 0; i < rodioIds.length; i++) {
			var rodioId = rodioIds[i];
			var tempId = rodioId +"Type";
			if(document.getElementById(tempId).style.display == ''){
				if("anyType" == tempId){
					var timeStart = $('#'+tempId+'StartTime').val();
					var timeEnd = $('#'+tempId+'EndTime').val();
					if(timeStart =='' || timeEnd ==''){
						 alert('请选择时间');
						 return false;
					}
				} else{
					var time = $('#'+tempId+'Time').val();
					if(time ==''){
						 alert('请选择时间');
						 return false;
					}
				}
			}
		}
		document.form1.action = "/th/DeviceRunningServlet.html";
	}
	function exportReport(){
	  	document.form1.action = "/th/DeviceRunningServlet.html?export=true";
	  	document.form1.submit();
	 	} 	
	function change(){
		var isAllOrgChecker = document.getElementById("allOrg").checked;
		if(isAllOrgChecker){
			document.getElementById("orgSelect").disabled=true;
		}else{
			document.getElementById("orgSelect").disabled=false;
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
		  	document.form1.action = "/th/DeviceRunningServlet.html?reportPage=1&functionCode=2";
		  	document.form1.submit();
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
</script>
</head>
<body onload="init()">
	<form method="post" name="form1" class="x-client-form"   action="/th/DeviceRunningServlet.html"  onSubmit="return check();">
		<div class="x-title"><span>&nbsp;&nbsp;报表管理-设备运行考核</span></div>
		<table><tr style ="heigt:15px"></tr></table>
		<div class="x-title">
			<div id="searchFormStyleId" class="x-fold-minus" onclick="onFold('searchFormId');"/>
		</div>
		<div style="height:26px;text-align:left;line-height:26px">检索</div>
	  	</div>
		<div id="searchFormId" style="background-color:#B2DFEE">
			<table  >
				<tr>
					<!-- <td>全部组织<input type="checkbox" name="allOrg" id="allOrg" value="allOrg" onchange="change()"  /></td> --> 
					<td width="5%" style="border:0;text-align:left">&nbsp;&nbsp;组&nbsp;&nbsp;&nbsp;织：</td>
			        <td width="28%" style="border:0;text-align:left"> 
			            <select id="orgSelect" name="orgSelect"></select>
			        </td>
					<td width="33%" style="border:0;text-align:left"/>
					<td width="33%" style="border:0;text-align:left"/>
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
					<td style="border:0;text-align:left">
						<span>&nbsp;&nbsp;</span><input type="submit" class="x-button" value="检索" > 
					</td>
				</tr>
			</table>
  		</div>
  		<p></p>
  		<div>
		<div class="x-data"><span  style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span></div>
			<div id="summaryDataId" >
				 <table class="x-data-table">
					<tr>
						<td>
							<table width="100%"  class="x-data-table" >
								<tr>
									<th align="center" colspan='14'  class="x-data-table-th">设备运行考核表</th>
								</tr>
								<tr  >
									<th align="center" colspan='2' class="x-data-table-th">综合排名</th>
									<th align="center" colspan='3' class="x-data-table-th">正常运行率(%)</th>
									<th align="center" colspan='3' class="x-data-table-th">开机率(%)</th>
									<th align="center" colspan='3' class="x-data-table-th">台均频道使用次数</th>
								</tr>
								<tr>
									<th align="center" class="x-data-table-th">名次</th>
									<th align="center" class="x-data-table-th">分行名称</th>
									
									<th align="center" class="x-data-table-th">名次</th>
									<th align="center" class="x-data-table-th">分行名称</th>
									<th align="center" class="x-data-table-th">数值</th>
									
									<th align="center" class="x-data-table-th">名次</th>
									<th align="center" class="x-data-table-th">分行名称</th>
									<th align="center" class="x-data-table-th">数值</th>
									
									<th align="center" class="x-data-table-th">名次</th>
									<th align="center" class="x-data-table-th">分行名称</th>
									<th align="center" class="x-data-table-th">数值</th>
									
								</tr>
								<%
									if(viewList!=null){
										for(int i =0;i<viewList.size();i++){
											DeviceRunningBean deviceRunningBean = (DeviceRunningBean)viewList.get(i);
											 String trClass = ( i % 2 != 0 ) ? "x-data-table-tr-white" : "x-data-table-tr-gray";
								%>	 
								<tr class="<%=trClass%>"  onmouseover='OMOver(this);' onmouseout='OMOut(this);'>
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getRank()%></td>
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getMultiOrgName()%></td>
									
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getRank()%></td>
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getNormalRateOrgName() %></td>
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getNormalRate() %></td>
									
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getRank()%></td>
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getOpenRateOrgName()%></td>
									<!-- 开机率 -->
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getOpenRate()%></td>
									
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getRank()%></td>
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getAvrOrgname() %></td>
									<td align="center" class="x-data-table-td"><%=deviceRunningBean.getAvrNum() %></td>
								</tr>
									
									<%	 
										 
									}
								}
								
								
								%>
							</table>
						</td>
					</tr>
				</table>
			</div>
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