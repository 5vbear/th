<%@page import="th.com.util.Define4Report"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@page isELIgnored="false"%> 
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.util.DateUtil"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macAlertList.jsp";
    logger.info( jspName + " : start" );
    User user = (User) session.getAttribute("user_info");
    String realname =null;
    if (user == null) {
	    response.setContentType("text/html; charset=utf-8");
	    response.sendRedirect("/th/index.jsp");
    } else {
	    realname = user.getReal_name();
	    logger.info("获得当前用户的用户名，用户名是： " + realname);
    }
    
    String anyTypeStartTime = (String) request.getParameter( Define4Report.REQ_PARAM_ANY_TYPE_START_TIME );
    String alertinfo = (String)  request.getParameter("alertinfo");
    alertinfo = StringUtils.isBlank( alertinfo ) ? "" : alertinfo;
    
    String operType = request.getParameter("operType");
    
    
	String currentDate = DateUtil.getYesterdayDate(Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD);	
	anyTypeStartTime = StringUtils.isBlank( anyTypeStartTime ) ? currentDate : anyTypeStartTime;

    String machineID = StringUtils.transStr((String)request.getAttribute("machineID"));
    logger.info("machineID = " + machineID);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="./css/machine.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="./css/report.css">		
		<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script type="text/javascript" src="./js/Calendar.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
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
		</script>
		<title>告警管理</title>
	</head>
	<%!
		private String parseStr(Object obj){
			if(obj != null){									
				return obj.toString();
			}else{
				return "";
			}
		}
	%>
	<style type="text/css">
		body {
			font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica,
				sans-serif;
		}
		
		a {
			color: #c75f3e;
		}
		
		#mytable {
			width: 100%;
			padding: 0;
			margin: 0;
		}
		
	/**	caption {
			padding: 0 0 5px 0;
			width: 700px;
			font: italic 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
			text-align: right;
		}
		
		th {
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
		}
		
		th.nobg {
			border-top: 0;
			border-left: 0;
			border-right: 1px solid #C1DAD7;
			background: none;
		}
		
		td {
			border-right: 1px solid #C1DAD7;
			border-bottom: 1px solid #C1DAD7;
			font-size: 11px;
			padding: 6px 6px 6px 12px;
			color: #4f6b72;
		}
		
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
		}*/
		/*---------for IE 5.x bug*/
		/**
		html>body td {
			font-size: 11px;
		}
		
		body,td,th {
			font-family: 宋体, Arial;
			font-size: 12px;
		}*/
	</style>
	<SCRIPT LANGUAGE="JavaScript">
	<!-- Begin
		var checkflag = "false";
		function check(field) {
			if (checkflag == "false") {
				for (i = 0; i < field.length; i++) {
					field[i].checked = true;
				}
				checkflag = "true";
				return "取消";
			} else {
				for (i = 0; i < field.length; i++) {
					field[i].checked = false;
				}
				checkflag = "false";
				return "全选";
			}
		}
		//  End -->
	
		function showtable() {			
			document.getElementById("operType").value=${operType};
			document.getElementById("orgSelect").value='${orgSelect}';
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
		var zNodes = ${orgs};
		
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
			showtable();
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
		
		function macQry(){
			var otype = document.getElementById("operType").value
			var orgSelect = document.getElementById("orgSelect").value
			var anyTypeStartTime = document.getElementById("anyTypeStartTime").value
			var alertinfo =  document.getElementById("alertinfo").value
			self.location = "/th/machineServlet?model=alert&method=query&pageIdx=1&operType="+otype+"&orgSelect="+orgSelect+"&anyTypeStartTime="+anyTypeStartTime+"&alertinfo="+alertinfo;
		}
		
		function goFirst(){
			var anyTypeStartTime = document.getElementById("anyTypeStartTime").value
			var alertinfo =  document.getElementById("alertinfo").value
			self.location = "/th/machineServlet?model=alert&method=query&pageIdx=1&operType="+${operType}+"&orgSelect="+${orgSelect}+"&anyTypeStartTime="+anyTypeStartTime+"&alertinfo="+alertinfo;
		}
		
		function goPrevious(curPageIdx){
			var pageIdx = 1;
			if(curPageIdx>1){
				pageIdx = curPageIdx - 1;
			}
			var anyTypeStartTime = document.getElementById("anyTypeStartTime").value
			var alertinfo =  document.getElementById("alertinfo").value
			self.location = "/th/machineServlet?model=alert&method=query&pageIdx="+pageIdx+"&operType="+${operType}+"&orgSelect="+${orgSelect}+"&anyTypeStartTime="+anyTypeStartTime+"&alertinfo="+alertinfo;
		}
		
		function goNext(curPageIdx){
			var anyTypeStartTime = document.getElementById("anyTypeStartTime").value
			var alertinfo =  document.getElementById("alertinfo").value
			var pageMaxNum = document.getElementById("pageNum").value;
			var pageIdx = pageMaxNum;
			if(curPageIdx < pageMaxNum){
				pageIdx = curPageIdx + 1;
			}
			self.location = "/th/machineServlet?model=alert&method=query&pageIdx="+pageIdx+"&operType="+${operType}+"&orgSelect="+${orgSelect}+"&anyTypeStartTime="+anyTypeStartTime+"&alertinfo="+alertinfo;
		}
		
		function goLast(){
			var anyTypeStartTime = document.getElementById("anyTypeStartTime").value
			var alertinfo =  document.getElementById("alertinfo").value
			var pageMaxNum = document.getElementById("pageNum").value;
			self.location = "/th/machineServlet?model=alert&method=query&pageIdx="+pageMaxNum+"&operType="+${operType}+"&orgSelect="+${orgSelect}+"&anyTypeStartTime="+anyTypeStartTime+"&alertinfo="+alertinfo;
		}
		
		function auditAlert(){
			var anyTypeStartTime = document.getElementById("anyTypeStartTime").value
			var alertinfo =  document.getElementById("alertinfo").value
			if(!isChecked()){
				alert("请选择要进行操作的记录!");
				return;
			}
			var htype = document.getElementById("htype").value;
			if(htype != '3' && htype != 3){
				alert('请审批已修理完成的告警记录!');
				return;
			}
			self.location = "/th/machineServlet?model=alert&method=audit&alertIds="+getCheckedValue()+"&orgSelect="+${orgSelect}+"&anyTypeStartTime="+anyTypeStartTime+"&alertinfo="+alertinfo;
		}
		
		function dealAlert(){
			if(!isChecked()){
				alert("请选择要进行操作的记录!");
				return;
			}
			var htype = document.getElementById("htype").value;
			if(htype != '2' && htype != 2){
				alert('请处理已派修的告警记录!');
				return;
			}
			self.location = "/th/machineServlet?model=alert&method=deal&alertIds="+getCheckedValue()+"&orgSelect="+${orgSelect};
		}
		
		function repairAlert(){
			if(!isChecked()){
				alert("请选择要进行操作的记录!");
				return;
			}
			var htype = document.getElementById("htype").value;
			if(htype != '1' && htype != 1){
				alert('请派修未处理的告警记录!');
				return;
			}
			alert('系统将会发送邮件给派修人员,请耐心等待!');
			self.location = "/th/machineServlet?model=alert&method=repair&alertIds="+getCheckedValue()+"&orgSelect="+${orgSelect};
		}
		
		function isChecked(){
			var checkBoxs = document.getElementsByName("maccbox");
			var flag = false;
			for (var i = 1; i < checkBoxs.length; i++) {
				if(checkBoxs[i].checked){
					flag = true;
					break;
				}
			}
			return flag;
		}
		
		function getCheckedValue(){
			var checkBoxs = document.getElementsByName("maccbox");
			var checkedValue = "";
			for (var i = 1; i < checkBoxs.length; i++) {
				if(checkBoxs[i].checked){
					if(checkedValue == ""){
						checkedValue += checkBoxs[i].value;
					}else{
						checkedValue = checkedValue + "," + checkBoxs[i].value;
					}
				}
			}
			return checkedValue;
		}

		function OpenWindow(aid){
			var paramers="dialogWidth:800px;DialogHeight:400px;status:no;help:no;unadorned:no;resizable:no;status:no";  
			var ret=window.showModalDialog('/th/machineServlet?model=alert&method=alert_detailinfo_subwindow&alertIds='+aid+'&orgSelect='+${orgSelect},'',paramers);  
				
			
		}
		function singleAudit(aid){
			var anyTypeStartTime = document.getElementById("anyTypeStartTime").value
			var alertinfo =  document.getElementById("alertinfo").value
			self.location = "/th/machineServlet?model=alert&method=audit&alertIds="+aid+"&orgSelect="+${orgSelect}+"&anyTypeStartTime="+anyTypeStartTime+"&alertinfo="+alertinfo;
		}
		
	</script>
	<body style="background-color: #fff;">
		<div class="x-title"><span>&nbsp;&nbsp;端机管理-告警管理</span></div>
		<table><tr style ="height:3px"></tr></table>
		<div class="x-title">
	    	<div id="foldStyleId" class="x-fold-minus" onclick="onFold('foldId');"/>
		</div>
		<div style="height:26px;text-align:left;line-height:26px">检索</div>
		<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
	    	<div class="x-chanelName" style="width:100%;padding-left:20px">
		        <span>组&nbsp;&nbsp;&nbsp;织：</span>
		    	<select id="orgSelect" name="orgSelect" style="width:200px"></select>
		    	<span style="margin-left:20px">操作状态:</span>
		    	<select name="select" id="operType" name="operType" style="width:80px">
			        <option value="1">我知道了</option>
			        <option value="2">已派修</option>
			        <option value="3">修理完成</option>
			        <option value="4">已处理</option>
		    	</select>
		    	<span style="margin-left:20px">告警时间：</span><input type="text" size="10" id="anyTypeStartTime" name="anyTypeStartTime" value="<%=anyTypeStartTime%>" onclick="getDateString(this,oCalendarChs)"/>
		    	<span style="margin-left:20px">告警说明：</span><input type="text" id="alertinfo" value="<%=alertinfo %>"/>
		    	<input type="button" value="搜索" onclick="macQry()"/>
		    </div>
		</div>
		<div>
		    <div class="x-data">
		    	<span  style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span>
		    </div>
		    <div>
				<form name=myform action="" method=post>
					<input type="hidden" id="pageNum" name="pageNum" value="${pageNum}" />
					<input type="hidden" id="htype" name="htype" value="${operType}"/>
					<table id="mytable" cellspacing="0" class="x-data-table">
						<tr>
							<th scope="col" style="width:2%"><input type=checkbox name=maccbox
								onClick="this.value=check(this.form.maccbox)" value="" /></th>
							<th scope="col" style="width:10%" class="x-data-table-th">告警端机</th>
							<th scope="col" style="width:10%" class="x-data-table-th">告警级别</th>
							<th scope="col" style="width:10%" class="x-data-table-th">告警类别</th>
							<th scope="col" style="width:32%" class="x-data-table-th">告警说明</th>
							<th scope="col" style="width:16%" class="x-data-table-th">告警时间</th>
							<th scope="col" style="width:7%" class="x-data-table-th">操作</th>
							<th scope="col" style="width:7%" class="x-data-table-th">详情</th>
							<%
							if( operType != null && "3".equals(operType) ){
							%>
							<th scope="col" style="width:7%" class="x-data-table-th">审批</th>
							<%	
							}
							%>
							
						</tr>						
					    <%
							HashMap[] maclist = (HashMap[]) request.getAttribute( "maclist" );
					    	int pageIdx = Integer.parseInt(request.getAttribute( "pageIdx" ).toString());
					    	if(pageIdx<1){
					    		pageIdx = 1;
					    	}
							for ( int i = 10*(pageIdx-1); i<maclist.length && i<10*(pageIdx); i++ ) {
								out.print("<tr>");
								out.print("	 <td class='x-data-table-td'>");
								out.print("	   <input type='checkbox' name='maccbox' value='" + maclist[i].get("AID") + "' />");
								out.print("	 </td>");
								out.print("	 <td class='x-data-table-td'>" + parseStr(maclist[i].get("MMARK")) + "</td>");
								out.print("	 <td class='x-data-table-td'>" + parseStr(maclist[i].get("ALEVEL")) + "</td>");
								out.print("	 <td class='x-data-table-td'>" + parseStr(maclist[i].get("ATYPE")) + "</td>");
								out.print("	 <td class='x-data-table-td'>" + parseStr(maclist[i].get("ADESC")) + "</td>");
								out.print("	 <td class='x-data-table-td'>" + parseStr(maclist[i].get("ATIME")) + "</td>");
								out.print("	 <td class='x-data-table-td'>" + parseStr(maclist[i].get("ASTATUS")) + "</td>");
								out.print("	 <td class='x-data-table-td'><a href='javascript:OpenWindow("+maclist[i].get("AID")+")'>查看详情</a> </td>");
								if( operType != null && "3".equals(operType) ){
									out.print("  <td class='x-data-table-td'><a href='javascript:singleAudit("+maclist[i].get("AID")+")'>审批</a> </td>");
								}								
								out.print("</tr>");
							}
						%>
					</table>
				</form>
	    	</div>
	  	
		<div>
			<input type="button" class="x-first-page" style="margin-left:5px" onclick="goFirst()" /> 
			<input type="button" class="x-previous-page" style="margin-left:-2px" onclick="goPrevious(${pageIdx})" /> 
			<input type="button" class="x-next-page" style="margin-left:-4px" onclick="goNext(${pageIdx})" />
			<input type="button" class="x-last-page" style="margin-left:-2px" onclick="goLast()" />
			[当前第${pageIdx}页/共${pageNum}页][每页10条][共<%=maclist.length %>条]
			<input type="button" class="rightBtn" value="批量审批" onclick="auditAlert()" /> 
			<!-- <input type="button" class="rightBtn" value="批量处理" onclick="dealAlert()" />  -->			
			<input type="button" class="rightBtn" value="批量派修" onclick="repairAlert()" /> 
		</div>
	</body>
</html>