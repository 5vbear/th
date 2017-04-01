<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%> 
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macRepairUpdate.jsp";
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

    String machineID = StringUtils.transStr((String)request.getAttribute("machineID"));
    logger.info("machineID = " + machineID);
    String fid = StringUtils.transStr((String)request.getAttribute("fid"));
	HashMap repairinfo = (HashMap) request.getAttribute( "repairinfo" );
	HashMap orgId = (HashMap) request.getAttribute( "orgId" );
	String  department_id = parseStr(repairinfo.get("DEPARTMENT_ID"));
	String  org_id = parseStr(orgId.get("ORG_ID"));
	String sb = (String)request.getAttribute("sb");

	String  aType = parseStr(repairinfo.get("ATYPE"));
	String aTime_day = "";
	String aTime_hour = "";
	String aTime_min = "";
	if(!"".equals(parseStr(repairinfo.get("ATIME"))))
	{
		String[] aTime = parseStr(repairinfo.get("ATIME")).split(" ");
		aTime_day = aTime[0];
		String[] aTime1 = aTime[1].split(":");
		aTime_hour = aTime1[0];
		aTime_min = aTime1[1];
	}
	String assignTime_day = "";
	String assignTime_hour = "";
	String assignTime_min = "";
	
	if(!"".equals(parseStr(repairinfo.get("ASSIGN_TIME"))))
	{
		String[] assignTime = parseStr(repairinfo.get("ASSIGN_TIME")).split(" ");
		assignTime_day = assignTime[0];
		String[] assignTime1 = assignTime[1].split(":");
		assignTime_hour = assignTime1[0];
		assignTime_min = assignTime1[1];

	}
	String startTime_day = "";
	String startTime_hour = "";
	String startTime_min = "";
	if(!"".equals(parseStr(repairinfo.get("START_TIME"))))
	{
		String[] startTime = parseStr(repairinfo.get("START_TIME")).split(" ");
		startTime_day = startTime[0];
		String[] startTime1 = startTime[1].split(":");
		startTime_hour = startTime1[0];
		startTime_min = startTime1[1];
	}
	String endTime_day = "";
	String endTime_hour = "";
	String endTime_min = "";
	if(!"".equals(parseStr(repairinfo.get("END_TIME"))))
	{
		String[] endTime = parseStr(repairinfo.get("END_TIME")).split(" ");
		endTime_day = endTime[0];
		String[] endTime1 = endTime[1].split(":");
		endTime_hour = endTime1[0];
		endTime_min = endTime1[1];
	}
	String confirmTime_day = "";
	String confirmTime_hour = "";
	String confirmTime_min = "";
	if(!"".equals(parseStr(repairinfo.get("CONFIRM_TIME"))))
	{
		String[] confirmTime = parseStr(repairinfo.get("CONFIRM_TIME")).split(" ");
		confirmTime_day = confirmTime[0];
		String[] confirmTime1 = confirmTime[1].split(":");
		confirmTime_hour = confirmTime1[0];
		confirmTime_min = confirmTime1[1];
	}
	String zNodes = (String) request.getAttribute( "MONITOR_ORG_LIST" );
	//long ConOrgId = Long.parseLong( request.getAttribute( "ConOrgId" ).toString() );
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="./css/machine.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="./css/advert.css">
		<link rel="stylesheet" type="text/css" href="./css/channel.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
		<script type="text/javascript" src="./js/Calendar.js"></script>
		<title>端机信息</title>
	</head>
	<style type="text/css">
		body {
			font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica,
				sans-serif;
			color: #4f6b72;
			background: #E6EAE9;
		}
		
		a {
			color: #c75f3e;
		}
		
		.myrtable {
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
		}
		/*---------for IE 5.x bug*/
		html>body td {
			font-size: 11px;
		}
		
		body,td,th {
			font-family: 宋体, Arial;
			font-size: 12px;
		}
		.widthDiv{ float:left; width: 100%;}
		.mydiv{ float:left; width: 700px;}
		.mydiv ul li{ float:left; padding-left:5px;}
		.mytable{ float:left; width: 100%;}
		#mytable1{
			width: 700px;
			padding: 0;
			margin: 0;
		}
	</style>
	<%!
		private String parseStr(Object obj){
			if(obj != null){									
				return obj.toString();
			}else{
				return "";
			}
		}
	%>

	<SCRIPT LANGUAGE="JavaScript">
	//**************
	var oCalendarChs=new PopupCalendar("oCalendarChs"); //初始化控件时,请给出实例名称:oCalendarChs
	oCalendarChs.weekDaySting=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
	oCalendarChs.monthSting=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
	oCalendarChs.oBtnTodayTitle="今天";
	oCalendarChs.oBtnCancelTitle="取消";
	oCalendarChs.Init();
	//**************

	
		
		function saveData(){
			var bname = document.getElementById('bname').value;
		    window.document.deployForm.target = "_self";
		    window.document.deployForm.submit();
		}
		
		function update(){
			var SelectAjax = document.getElementById("SelectDpt").value;
			var ATYPE = document.getElementById("ATYPE").value;
			var ADESC = document.getElementById("ADESC").value;
			var ATIME = document.getElementById("ATIME").value;
			var ATIME_hh = document.getElementById("ATIME_hh").value;
			var ATIME_mi = document.getElementById("ATIME_mi").value;
			var TREATMENT = document.getElementById("TREATMENT").value;
			var DIVIDER = document.getElementById("DIVIDER").value;
			var ASSIGN_TIME = document.getElementById("ASSIGN_TIME").value;
			var ASSIGN_TIME_hh = document.getElementById("ASSIGN_TIME_hh").value;
			var ASSIGN_TIME_mi = document.getElementById("ASSIGN_TIME_mi").value;
			var REPAIRER = document.getElementById("REPAIRER").value;
			var START_TIME = document.getElementById("START_TIME").value;
			var START_TIME_hh = document.getElementById("START_TIME_hh").value;
			var START_TIME_mi = document.getElementById("START_TIME_mi").value;
			var END_TIME = document.getElementById("END_TIME").value;
			var END_TIME_hh = document.getElementById("END_TIME_hh").value;
			var END_TIME_mi = document.getElementById("END_TIME_mi").value;
			var REPAIR_CONTENT = document.getElementById("REPAIR_CONTENT").value;
			var CONFIRMOR = document.getElementById("CONFIRMOR").value;
			var REMARK = document.getElementById("REMARK").value;
			var CONFIRM_TIME = document.getElementById("CONFIRM_TIME").value;
			var CONFIRM_TIME_hh = document.getElementById("CONFIRM_TIME_hh").value;
			var CONFIRM_TIME_mi = document.getElementById("CONFIRM_TIME_mi").value;			
			var fid = "<%=fid%>";
			
			var d = new Date();
			var year = d.getFullYear();
			var month = d.getMonth() + 1;
			month = month < 10 ? ("0" + month) : month;
			var day = d.getDate();
			day = day < 10 ? ("0" + day) : day;
			var today = year + "/" + month + "/" + day;
			var todayTime = new Date(Date.parse(today));
			var confirm =document.getElementById("CONFIRM_TIME").value;
			var str=confirm.replace("-","/").replace("-","/");
			var confirmTime = new Date(Date.parse(str));
			if(confirmTime>todayTime){
				window.alert("确认时间不能为今日以后");  
				return;
			}
			
			
			if( document.getElementById("SelectDpt").value== ""||document.getElementById("SelectDpt").value== -1){
				window.alert("请选择部门名称");  
				return;
			}
			

			if( document.getElementById("ATYPE").value== ""){
				window.alert("请选择故障类型");  
				return;
			}
			if( document.getElementById("ADESC").value== ""){
				window.alert("请填写故障描述");  
				return;
			}
			if( document.getElementById("ATIME").value== ""){
				window.alert("请填写故障时间");  
				return;
			}
			if( document.getElementById("TREATMENT").value== ""){
				window.alert("请填写处理方法");  
				return;
			}
			if( document.getElementById("DIVIDER").value== ""){
				window.alert("请填写派修人");  
				return;
			}
			if( document.getElementById("ASSIGN_TIME").value== ""){
				window.alert("请填写派修时间");  
				return;
			}
			if( document.getElementById("REPAIRER").value== ""){
				window.alert("请填写维修人");  
				return;
			}
			if( document.getElementById("START_TIME").value== ""){
				window.alert("请填写维修开始时间");  
				return;
			}
			if( document.getElementById("END_TIME").value== ""){
				window.alert("请填写维修结束时间");  
				return;
			}
			if( document.getElementById("REPAIR_CONTENT").value== ""){
				window.alert("请填写维修内容");  
				return;
			}

			if( document.getElementById("REMARK").value== ""){
				window.alert("请填写备注");  
				return;
			}
			
			self.location = "/th/machineServlet?model=repair&method=doupdate&pageIdx=${pageIdx}&machineName=${machineName}&SelectOrg=${SelectOrg}&operateTime_e=${operateTime_e}&operateTime_s=${operateTime_s}&ATYPE="+ATYPE+"&ADESC="+ADESC+"&ATIME="+ATIME+"&ATIME_hh="+ATIME_hh+"&ATIME_mi="+ATIME_mi+"&fid="+fid+"&TREATMENT="+TREATMENT+"&DIVIDER="+DIVIDER+"&ASSIGN_TIME="+ASSIGN_TIME+"&ASSIGN_TIME_hh="+ASSIGN_TIME_hh+"&ASSIGN_TIME_mi="+ASSIGN_TIME_mi+"&REPAIRER="+REPAIRER+"&START_TIME="+START_TIME+"&START_TIME_hh="+START_TIME_hh+"&START_TIME_mi="+START_TIME_mi+"&END_TIME="+END_TIME+"&END_TIME_hh="+END_TIME_hh+"&END_TIME_mi="+END_TIME_mi+"&REPAIR_CONTENT="+REPAIR_CONTENT+"&CONFIRMOR="+CONFIRMOR+"&REMARK="+REMARK+"&CONFIRM_TIME="+CONFIRM_TIME+"&CONFIRM_TIME_hh="+CONFIRM_TIME_hh+"&CONFIRM_TIME_mi="+CONFIRM_TIME_mi+"&SelectAjax="+SelectAjax;
	
		}
		
		function editable(){
			document.getElementById('saveData').style.display = '';
			document.getElementById('unEditable').style.display = '';
			document.getElementById('editable').style.display = 'none';
			document.getElementById('orgId').disabled = false;
			document.getElementById('bname').disabled = false;
			document.getElementById('baddr').disabled = false;
		}
		
		function unEditable(){
			window.document.deployForm.reset();
			document.getElementById('saveData').style.display = 'none';
			document.getElementById('unEditable').style.display = 'none';
			document.getElementById('editable').style.display = '';
			document.getElementById('orgId').disabled = 'true';
			document.getElementById('bname').disabled = 'true';
			document.getElementById('baddr').disabled = 'true';
			//self.location = "/th/machineServlet?model=deploy&method=view&pageIdx="+${pageIdx}+"&macIdStd="+${macIdStd};
			
			
		}
		// Ajax联动处理
		//定义XMLHttpRequest对象
		 var http_request=false;
		
		 function send_request(url){
		     http_request=false;
		     //开始初始化XMLHttpRequest对象
		     if(window.XMLHttpRequest){//Mozilla等浏览器初始化XMLHttpRequest过程
		         http_request=new XMLHttpRequest();
		         //有些版本的Mozilla浏览器处理服务器返回的未包含XML mime-type头部信息的内容时会出错.
		         //因此,要确保返回的内容包含text/xml信息.
		         if(http_request.overrideMimeType){
		             http_request.overrideMimeType("text/xml");
		         }
		     }
		     else if(window.ActiveXObject){//IE浏览器初始化XMLHttpRequest过程
		         try{
		             http_request=new ActiveXObject("Msxml2.XMLHTTP");
		         }
		         catch(e){
		             try{
		                 http_request=new ActiveXObject("Microsoft.XMLHTTP");
		             }
		             catch(e){}
		         }
		     }
		     //异常,创建对象失败
		     if(!http_request){
		         window.alert("不能创建XMLHttpRequest对象实例!");
		         return false;
		     }
		     //指定响应处理函数
		     http_request.onreadystatechange=processRequest;
		     //发送HTTP请求信息
		     http_request.open("GET",url,true);
		     http_request.send(null);
		 };
		
		 //处理返回信息函数
		 function processRequest(){
		     //判断对象状态
		     if(http_request.readyState==4){
		         //判断HTTP状态码
		         if(http_request.status==200){
		             //信息已经成功返回
		            //window.document.write(http_request.responseText);
		            //alert(http_request.responseText);
		            document.getElementById("dptAjaxSel").innerHTML=http_request.responseText;
		         }
		         else{
		             //请求页面有问题
		             alert("您所请求的页面有异常!错误状态:"+http_request.status);
		         }
		     }
		 };
		
		
		function getDpts() {
			var selOrgId = document.getElementById("SelectOrg").value;
			var sendUrl = "/th/jsp/sysMang/dptMang/repairAjaxSelect.jsp?orgId=" + selOrgId
			// 添加时间戳，避免因缓存结果而导致不能实时得到最新的结果
			sendUrl += "&nocache=" + new Date().getTime(); 
			send_request(sendUrl);
		};

		var zTreeObj;
		var setting = {
			data : {
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "pId",
					rootPId : 0
				}
			},
			view : {
				selectedMulti : false

			}

		};
		var zNodes = <%= zNodes %>
		$(document).ready(function() {
			zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
			
			var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
			var v = "", w = "";
			// 设置组织名称下拉列表值 
			var orgList = document.getElementById("SelectOrg");
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
			// 保存组织list信息 
			<%--window.document.form_data.org_list_info.value = v;--%>
			// 显示之前选中的组织名称 
			<%-- var inConOrgId = <%= ConOrgId %>;
			if(inConOrgId!=-1){
				orgList.value = inConOrgId;
			}else{
				orgList.value = nodes[0].id;
			}
			orgList.value = <%=ConOrgId%>; --%>
			// Ajax组织与部门之间的级联处理
			
		
		});	
		window.onload = function showtable() {
			document.getElementById("ATYPE").value=<%=aType%>;
			
			document.getElementById("ATIME_hh").value=<%=aTime_hour%>;
			document.getElementById("ATIME_mi").value=<%=aTime_min%>;
			<%if("".equals(assignTime_day)){%>
				document.getElementById("ASSIGN_TIME_hh").value="";
				document.getElementById("ASSIGN_TIME_mi").value="";
			<%} else {%>
				document.getElementById("ASSIGN_TIME_hh").value=<%=assignTime_hour%>;
				document.getElementById("ASSIGN_TIME_mi").value=<%=assignTime_min%>;
			<%}if("".equals(startTime_day)){%>
				document.getElementById("START_TIME_hh").value="";
				document.getElementById("START_TIME_mi").value="";
			<%} else {%>
				document.getElementById("START_TIME_hh").value=<%=startTime_hour%>;
				document.getElementById("START_TIME_mi").value=<%=startTime_min%>;
			<%}if("".equals(endTime_day)){%>
				document.getElementById("END_TIME_hh").value="";
				document.getElementById("END_TIME_mi").value="";
			<%} else {%>
				document.getElementById("END_TIME_hh").value=<%=endTime_hour%>;
				document.getElementById("END_TIME_mi").value=<%=endTime_min%>;
			<%}if("".equals(confirmTime_day)){%>
				document.getElementById("CONFIRM_TIME_hh").value="";
				document.getElementById("CONFIRM_TIME_mi").value="";
			<%} else {%>
				document.getElementById("CONFIRM_TIME_hh").value=<%=confirmTime_hour%>;
				document.getElementById("CONFIRM_TIME_mi").value=<%=confirmTime_min%>;
			<%}%>
			<%if(!"".equals(org_id)){%>
				document.getElementById("SelectOrg").value=<%=org_id%>;					
			<%}%>
			<%if(!"".equals(department_id)){%>
				document.getElementById("dptAjaxSel").innerHTML="<%=sb%>";
			<%}%>
			<%if("".equals(org_id)){%>
				getDpts();
			<%}%>



	
			
			//var tablename = document.getElementById("mytable");
			var li = document.getElementsByTagName("tr");
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
		};
	</script>
	<body style="background-color: #fff;height: 100%">
		

		<div class="widthDiv">
			<div class="x-title"><span>&nbsp;&nbsp;端机管理-派修管理</span></div>
			<table><tr style ="height:3px"></tr></table>
			<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
		    	<div class="x-chanelName" style="width:100%;padding-top:3px">
			    	<input type="button" class="leftBtn" value="保存" onclick="update()"/>
			    </div>
			</div>
		    <div class="mytable" id="tab1">
				<form name="deployForm" action="/th/machineServlet?model=faq&method=query&" method="post">
				    <%

				    %>		
					
					<table class="myrtable" cellspacing="0">
						<caption></caption>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">机器名:</td>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">故障时间:</td>
						</tr>
						<tr>
							<td class="row"><% out.print(parseStr(repairinfo.get("MMARK")));  %></td>
							<td class="row">
							<input type="text" size="8" name="ATIME" id="ATIME" maxlength="10" style="ime-mode:disabled" 
								 value="<%=aTime_day%>" onclick="getDateString(this,oCalendarChs)">
							<select name="ATIME_hh" id="ATIME_hh" style="width:80px">
								<%
								for(int i=0;i<24;i++){
									out.print("<option value='"+i+"'>"+i+"</option>\n");
								}
								%>
							</select>时
							<select name="ATIME_mi" id="ATIME_mi" style="width:80px">
								<%
								for(int i=0;i<60;i++){
									out.print("<option value='"+i+"'>"+i+"</option>\n");
								}
								%>
							</select>分
							</td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">故障类型:</td>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">派修时间:</td>
						</tr>
						<tr>
							<td class="row">
							<select name="select" id="ATYPE" name="ATYPE" style="width:80px">
						        <option value=""></option>
						        <option value="1">非法进程</option>
						        <option value="2">非法服务</option>
						        <option value="3">cpu负荷过高</option>
						        <option value="4">内存负荷过高 </option>
						        <option value="5">硬盘容量不足</option>
						        <option value="6">上行速率过慢</option>
						        <option value="7">下行速率过慢</option>
						        <option value="8">非法访问率过高</option>
						        <option value="9">断线报警</option>
		    				</select>
							</td>
							<td class="row">
							<input type="text" size="8" name="ASSIGN_TIME" id="ASSIGN_TIME" maxlength="10" style="ime-mode:disabled" 
								 value="<%=assignTime_day%>" onclick="getDateString(this,oCalendarChs)">
	
							<select name="ASSIGN_TIME_hh" id="ASSIGN_TIME_hh" style="width:80px">
								<%
								out.print("<option value=''>"+"</option>\n");
								for(int i=0;i<24;i++){
									out.print("<option value='"+i+"'>"+i+"</option>\n");
								}
								%>
							</select>时
	
							<select name="ASSIGN_TIME_mi" id="ASSIGN_TIME_mi" style="width:80px">
								<%
								out.print("<option value=''>"+"</option>\n");
								for(int i=0;i<60;i++){
									out.print("<option value='"+i+"'>"+i+"</option>\n");
								}
								%>
							</select>分
							</td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">维修人:</td>

							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">维修开始时间:</td>
						</tr>
						<tr>
							<td class="row"><textarea id="REPAIRER" rows="1" cols="40"><%=parseStr(repairinfo.get("REPAIRER"))%></textarea></td>

							<td class="row">
							<input type="text" size="8" name="START_TIME" id="START_TIME" maxlength="10" style="ime-mode:disabled" 
								 value="<%=startTime_day%>" onclick="getDateString(this,oCalendarChs)">

							<select name="START_TIME_hh" id="START_TIME_hh" style="width:80px">
								<%
								out.print("<option value=''>"+"</option>\n");
								for(int i=0;i<24;i++){
									out.print("<option value='"+i+"'>"+i+"</option>\n");
								}
								%>
							</select>时

							<select name="START_TIME_mi" id="START_TIME_mi" style="width:80px">
								<%
								out.print("<option value=''>"+"</option>\n");
								for(int i=0;i<60;i++){
									out.print("<option value='"+i+"'>"+i+"</option>\n");
								}
								%>
							</select>分
							</td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">派修人:</td>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">维修结束时间:</td>
						</tr>
						<tr>
							<td class="row"><textarea id="DIVIDER" rows="1" cols="40"><%=parseStr(repairinfo.get("DIVIDER"))%></textarea></td>

							<td class="row">
							<input type="text" size="8" name="END_TIME" id="END_TIME" maxlength="10" style="ime-mode:disabled" 
								 value="<%=endTime_day%>" onclick="getDateString(this,oCalendarChs)">
							
							<select name="END_TIME_hh" id="END_TIME_hh" style="width:80px">
								<%
								out.print("<option value=''>"+"</option>\n");
								for(int i=0;i<24;i++){
									out.print("<option value='"+i+"'>"+i+"</option>\n");
								}
								%>
							</select>时
							<select name="END_TIME_mi" id="END_TIME_mi" style="width:80px">
								<%
								out.print("<option value=''>"+"</option>\n");
								for(int i=0;i<60;i++){
									out.print("<option value='"+i+"'>"+i+"</option>\n");
								}
								%>
							</select>分
							</td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">确认人:</td>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">确认时间:</td>
						</tr>
						<tr>
							<td class="row"><textarea id="CONFIRMOR" rows="1" cols="40"><%=parseStr(repairinfo.get("CONFIRMOR"))%></textarea></td>													
							<td class="row">
							<input type="text" size="8" name="CONFIRM_TIME" id="CONFIRM_TIME" maxlength="10" style="ime-mode:disabled" 
								 value="<%=confirmTime_day%>" onclick="getDateString(this,oCalendarChs)">
							
							<select name="CONFIRM_TIME_hh" id="CONFIRM_TIME_hh" style="width:80px">
								<%
								out.print("<option value=''>"+"</option>\n");
								for(int i=0;i<24;i++){
									out.print("<option value='"+i+"'>"+i+"</option>\n");
								}
								%>
							</select>时
							<select name="CONFIRM_TIME_mi" id="CONFIRM_TIME_mi" style="width:80px">
								<%
								out.print("<option value=''>"+"</option>\n");
								for(int i=0;i<60;i++){
									out.print("<option value='"+i+"'>"+i+"</option>\n");
								}
								%>
							</select>分
							</td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">派修部门(组织):</td>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">派修部门(部门):</td>
						</tr>
						<tr>
							<td class="row">
								<select id="SelectOrg" name="selectorg" onChange="getDpts()">
								</select>
							</td>
							<td class="row">
								<span id="dptAjaxSel">
									<select id="SelectAjax" name="selectajax">
										<option value="-1">请先点选组织下拉列表</option>
									</select>
								</span>
							</td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">故障描述:</td>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">处理方法:</td>
						</tr>
						<tr>
							<td class="row"><textarea id="ADESC" rows="4" cols="40"><%=parseStr(repairinfo.get("ADESC"))%></textarea></td>
							<td class="row"><textarea id="TREATMENT" rows="4" cols="40"><%=parseStr(repairinfo.get("TREATMENT"))%></textarea></td>
						</tr>
						<tr>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">维修内容:</td>
							<td class="row" style="font-weight:bold;background-color:#EEE;width: 50%">备注:</td>
						</tr>
						<tr>
							<td class="row"><textarea id="REPAIR_CONTENT" rows="4" cols="40"><%=parseStr(repairinfo.get("REPAIR_CONTENT"))%></textarea></td>
							<td class="row"><textarea id="REMARK" rows="4" cols="40"><%=parseStr(repairinfo.get("REMARK"))%></textarea></td>
						</tr>
					</table>
				</form>
			</div>
		</div>						 
	</body>
</html>