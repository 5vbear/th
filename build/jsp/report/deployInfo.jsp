<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="th.entity.MachineBean"%>
<%@page import="th.util.StringUtils"%>
<%@page import="th.util.DateUtil"%>
<%@page import="th.com.util.Define4Report"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>端机部署检索</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path;
	
	String timeType = (String) request.getAttribute( Define4Report.REQ_PARAM_TIME_TYPE );
	String macType = (String) request.getAttribute( Define4Report.REQ_PARAM_MAC_TYPE );
	
	String pageNumber = (String) request.getAttribute( Define4Report.REQ_PARAM_PAGE_NUMBER );
	String totalPageCount = (String) request.getAttribute( Define4Report.REQ_PARAM_TOTAL_PAGE_COUNT );
	String selectedOrgId = (String) request.getAttribute( Define4Report.REQ_PARAM_SELECTED_ORG_ID );
	
	String orgs = (String) request.getAttribute( "orgs" );
	ArrayList<MachineBean> viewList = (ArrayList<MachineBean>) request.getAttribute( "viewList" );

	pageNumber = StringUtils.isBlank( pageNumber ) ? "1" : pageNumber;
	totalPageCount = StringUtils.isBlank( totalPageCount ) ? "1" : totalPageCount;
	selectedOrgId = StringUtils.isBlank( selectedOrgId ) ? "" : selectedOrgId;
	timeType = StringUtils.isBlank( timeType ) ? Define4Report.TIME_TYPE_ANY : timeType;
	macType = StringUtils.isBlank( macType ) ? "0" : macType;
	
	String firstPageDisabled = Integer.parseInt( pageNumber ) < 2 ? "disabled" : "";
	String previousPageDisabled = Integer.parseInt( pageNumber ) < 2 ? "disabled" : "";
	String nextPageDisabled = Integer.parseInt( pageNumber ) >= Integer.parseInt( totalPageCount ) ? "disabled": "";
	String lastPageDisabled = Integer.parseInt( pageNumber ) >= Integer.parseInt( totalPageCount ) ? "disabled": "";
	String exportDisabled = (viewList!=null && viewList.size()>0)? "": "disabled";
	
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
	String macMarkVal = (String) request.getAttribute("macMark");	
	
	String currentDate = DateUtil.getYesterdayDate(Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD);
	dayTypeTime = StringUtils.isBlank( dayTypeTime ) ? currentDate : dayTypeTime;
	weekTypeTime = StringUtils.isBlank( weekTypeTime ) ? currentDate : weekTypeTime;
	monthTypeTime = StringUtils.isBlank( monthTypeTime ) ? currentDate : monthTypeTime;	
	anyTypeStartTime = StringUtils.isBlank( anyTypeStartTime ) ? currentDate : anyTypeStartTime;
	anyTypeEndTime = StringUtils.isBlank( anyTypeEndTime ) ? currentDate : anyTypeEndTime;	
	macMarkVal = StringUtils.isBlank( macMarkVal ) ? "" : macMarkVal;	
	HashMap[] MTmap =  (HashMap[])request.getAttribute( "MTmap" );
			
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
	
  	function doSearch(){
  		if(check()){
  			document.machineFaultForm.action = "/th/Report?reportPage=6&functionCode=2";
  		  	document.machineFaultForm.submit();
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
	  	document.machineFaultForm.action = "/th/Report?reportPage=6&functionCode=2";
	  	document.machineFaultForm.submit();
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
<form class="x-client-form" action="" name="machineFaultForm" method="post">
  <div class="x-title"><span>&nbsp;&nbsp;端机部署</span></div>
  <table><tr style ="heigt:15px"></tr></table>
  <div class="x-title">
    <div id="searchFormStyleId" class="x-fold-minus" onclick="onFold('searchFormId');"/>
  </div>
  <div style="height:26px;text-align:left;line-height:26px">检索</div>
  </div>
  <div id="searchFormId" style="background-color:#B2DFEE">
    <table cellpadding="0" border="0">
      <tr>
        <td width="7%" style="border:0;text-align:left">&nbsp;所属组织：</td>
        <td width="26%" style="border:0;text-align:left;"> 
            <select id="orgSelect" name="orgSelect"></select>
        </td>
        <td width="33%" style="border:0;text-align:left;">机器名：&nbsp;<input name="macMark" type="text" value="<%=macMarkVal %>" size="22" /></td>
        <td width="33%" style="border:0;text-align:left">&nbsp;&nbsp;设备类型：&nbsp;
						     <select id="macType" name="macType" style="width: 180px;HEIGHT: 25px" >
								<option value="0" <%if("0".equals(macType)){ %>selected="selected"<%}%>>全部</option>
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
        <td width="7%" style="border:0;text-align:left">&nbsp;注册时间：</td>
        <td width="26%" style="border:0;text-align:left">
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
			<span>&nbsp;&nbsp;</span><input type="button" class="x-button" value="搜索" onclick ="doSearch()" />
		</td>
      </tr>
    </table>
  </div>
  <p></p>
  <div>
    <div class="x-data"><span  style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span></div>
    <div id="summaryDataId" style="overflow-x:auto;overflow-y:hidden;">
      <table class="x-data-table" style="width:1800px;">
        <tr>
          <th colspan ="17" class="x-data-table-th">端机部署信息</th>
        </tr>             
        <tr>
          <th class="x-data-table-th">所属组织</th>
          <th class="x-data-table-th">机器名</th>
          <th class="x-data-table-th">设备类型</th>
          <th class="x-data-table-th">注册时间</th>
          <th class="x-data-table-th">网点名称</th>
          <th class="x-data-table-th">网点地址</th>
          <th class="x-data-table-th">网点编号</th>
          <th class="x-data-table-th">保修年限</th>
          <th class="x-data-table-th">负责人</th>
          <th class="x-data-table-th">联系人</th>
          <th class="x-data-table-th">联系电话</th>
          <th class="x-data-table-th">联系手机</th>
          <th class="x-data-table-th">联系Email</th>
          <th class="x-data-table-th">营业时间</th>
          <th class="x-data-table-th">营业周期</th>
          <th class="x-data-table-th">经度</th>
          <th class="x-data-table-th">纬度</th>
        </tr>
 		<%
 			if ( viewList != null && viewList.size() > 0 ) {
 				for ( int i = 0; i < viewList.size(); i++ ) {
 					MachineBean mac = (MachineBean) viewList.get( i );
 					String trClass = ( i % 2 != 0 ) ? "x-data-table-tr-white" : "x-data-table-tr-gray";
 		%>
		<tr class="<%=trClass%>" onmouseover='OMOver(this);' onmouseout='OMOut(this);'>
			<td class="x-data-table-td"><%=mac.getOrgName()%></td>
			<td class="x-data-table-td"><%=mac.getMachineMark()%></td>
			<td class="x-data-table-td"><%=mac.getMacType()%></td>
			<td class="x-data-table-td"><%=mac.getRegTime()%></td>
			<td class="x-data-table-td"><%=mac.getBranchName()%></td>
			<td class="x-data-table-td"><%=mac.getBranchAddress()%></td>
			<td class="x-data-table-td"><%=mac.getBranchNumber()%></td>
			<td class="x-data-table-td"><%=mac.getFreeRepairYear()%></td>
			<td class="x-data-table-td"><%=mac.getManagerName()%></td>
			<td class="x-data-table-td"><%=mac.getContactName()%></td>
			<td class="x-data-table-td"><%=mac.getContactTel()%></td>
			<td class="x-data-table-td"><%=mac.getContactMobile()%></td>
			<td class="x-data-table-td"><%=mac.getContactEmail()%></td>
			<td class="x-data-table-td"><%=mac.getWorkTime()%></td>
			<td class="x-data-table-td"><%=mac.getOpenDate()%></td>
			<td class="x-data-table-td"><%=mac.getLongitude()%></td>
			<td class="x-data-table-td"><%=mac.getLatitude()%></td>
		</tr>
		<%
				}
			}
		%>        
      </table>
    </div>
    <div> 
    	<input type="hidden" id="pageNumber" name="pageNumber" value="<%=pageNumber%>" />
        <input type="button" <%=firstPageDisabled%> id="firstPageButton" class="x-first-page" onclick="changePage(this)" />&nbsp;
    	<input type="button" <%=previousPageDisabled%> id="previousPageButton" class="x-previous-page" onclick="changePage(this)"/>&nbsp;
    	<input type="button" <%=nextPageDisabled%> id="nextPageButton" class="x-next-page" onclick="changePage(this)"/>&nbsp;
    	<input type="button" <%=lastPageDisabled%> id="lastPageButton" class="x-last-page" onclick="changePage(this)"/>&nbsp;[当前第<%=pageNumber%>页/共<%=totalPageCount%>页]
    </div>
  </div>
</form>
</body>
</html>