<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="th.entity.TaskBean"%>
    
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
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
%>

<%
	//选中策略原有信息取得
	TaskBean taskBean = (TaskBean) request.getAttribute( "selTaskBean" );
	long selStgId = taskBean.getStgId();
	String selStgName = taskBean.getStgName();
	String selStgDesp = taskBean.getStgDesp();
	String selSendInternal = taskBean.getSendInternal();
	int selSendTimeHour = taskBean.getSendTimeHour();
	int selSendTimeMinute = taskBean.getSendTimeMinute();
	String selSendType = taskBean.getSendType();
	String selDeliverTerminal = taskBean.getDeliverTerminal();
	String selReportNameList = taskBean.getReportNameList();
	String selDeliverRoleList = taskBean.getDeliverRoleList();
	
	String pageTitle = (String)request.getAttribute( "pageTitle" );
	String saveResult = (String)request.getAttribute( "saveResult" );
	String dealReportList = (String)request.getAttribute( "dealReportList" );
	String dealRoleList = (String)request.getAttribute( "dealRoleList" );
	String acTurn = (String)request.getAttribute( "acTurn" );
	
%>
    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>策略信息定义页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="../../../js/myDataCheck.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../../css/machine.css" />


<SCRIPT LANGUAGE="JavaScript">

	var zTreeObj;
	var setting = {
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId",
				rootPId : 0,
			}
		},
		view : {
			selectedMulti : false
	
		}
	
	};
	var zNodes = [ { "id": 0, "pId": -1, "name": "中国建设银行", open:true },
	               { "id": 1, "pId": 0, "name": "辽宁省分行", open:true },
	               { "id": 2, "pId": 0, "name": "北京市分行", open:true },
	               { "id": 3, "pId": 0, "name": "吉林省分行", open:true },
	               { "id": 4, "pId": 0, "name": "山东省分行", open:true },
	               { "id": 5, "pId": 1, "name": "沈阳市支行"},
	               { "id": 6, "pId": 1, "name": "朝阳市支行"} ];
	
	
	$(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	});
	
	// window.onload必须等到页面内包括图片的所有元素加载完毕后才能执行 
	window.onload = function(){
		
		var acTurn = "<%=acTurn%>";
		if(acTurn!=""){
			document.getElementById("listAjaxDiv").disabled = true;
		}
		var selSendInternal = "<%= selSendInternal %>";
		if(selSendInternal!=""){
			document.getElementById("sendInternal").value = selSendInternal;
		}
		var selSendTimeHour = "<%= selSendTimeHour %>";
		if(selSendTimeHour!=""){
			document.getElementById("sendTimeHour").value = selSendTimeHour;
		}
		var selSendTimeMinute = "<%= selSendTimeMinute %>";
		if(selSendTimeMinute!=""){
			document.getElementById("sendTimeMinute").value = selSendTimeMinute;
		}
		var selSendType = "<%= selSendType %>";
		if(selSendType!=""){
			document.getElementById("sendType").value = selSendType;
		}
		setDeliverTerminal();
		
		var saveResult = "<%=saveResult%>";
		if(saveResult!=""){
			alert(saveResult);			
		}
		
	};
	
	function setDeliverTerminal() {
		var sendTypeValue = document.getElementById("sendType").value;
		if("1"==sendTypeValue){
			document.getElementById("sendTypeSpan").innerHTML = "邮件地址[*]";
		}else if("2"==sendTypeValue){
			document.getElementById("sendTypeSpan").innerHTML = "联系方式[*]";
		}		
	};
	
	function btnOperations() {
		
		this.enabled = function(){
			
			document.getElementById("stgName").disabled = false;
			document.getElementById("stgDesp").disabled = false;
			document.getElementById("sendInternal").disabled = false;
			document.getElementById("sendTimeHour").disabled = false;
			document.getElementById("sendTimeMinute").disabled = false;
			document.getElementById("sendType").disabled = false;
			document.getElementById("deliverTerminal").disabled = false;
			document.getElementById("repNameList").disabled = false;
			document.getElementById("roleIdList").disabled = false;
			
			// 列表区域变成可编辑状态
			document.getElementById("listAjaxDiv").disabled = false;
			
		};
		this.save = function(){
			
			// 共同项目提出
			var inStgName = document.getElementById("stgName");
			var inStgDesp = document.getElementById("stgDesp");
			var inSendInternal = document.getElementById("sendInternal");
			var inSendTimeHour = document.getElementById("sendTimeHour");
			var inSendTimeMinute = document.getElementById("sendTimeMinute");
			var inSendType = document.getElementById("sendType");
			var inDeliverTerminal = document.getElementById("deliverTerminal");
			var inRepNameList = document.getElementById("repNameList");
			var inRoleIdList = document.getElementById("roleIdList");
			
			// 区域取得
			var lad = document.getElementById("listAjaxDiv");
			//sjw mod start
			if(myTrim(inStgName.value)==""){
				alert("请输入当前策略的名称!");
			}else if(checkMailAddress(inDeliverTerminal.value)!=0){
				alert("请输入当前策略的接收端子信息!");
			}else if(myTrim(inRepNameList.value)==""){
				alert("请输入当前策略的报表名列表项!");
			}else if(myTrim(inRoleIdList.value)==""){
				alert("请输入当前策略的接收方角色列表项!");
			}else if(lad.style.display !="none"){
				alert("请先在列表中点选要配布的名称,点击确定,再执行后续操作!");
			//sjw mod end
			}else{
				window.document.form_data.dealFlg.value = "add/change";
				window.document.form_data.action = "/th/jsp/sysMang/taskMang/taskDeal.html";
				window.document.form_data.hide_stg_id.value = <%=selStgId%>;
				window.document.form_data.input_stg_name.value = inStgName.value;
				window.document.form_data.input_stg_desp.value = inStgDesp.value;
				window.document.form_data.input_send_internal.value = inSendInternal.value;
				window.document.form_data.input_send_time_hour.value = inSendTimeHour.value;
				window.document.form_data.input_send_time_minute.value = inSendTimeMinute.value;
				window.document.form_data.input_send_type.value = inSendType.value;
				window.document.form_data.input_deliver_terminal.value = inDeliverTerminal.value;
				window.document.form_data.input_reportList.value = inRepNameList.value;
				window.document.form_data.input_roleList.value = inRoleIdList.value;
				window.document.form_data.submit();
			}
		};
		this.back = function(){
			window.document.form_data.action = "/th/jsp/sysMang/taskMang/taskList.html";
			window.document.form_data.submit();
		}
		
	};
	var btnOper = new btnOperations();
	
	function repListClick(){
		var curRepNameList = document.getElementById("repNameList").value;
		// 区域取得
		var lad = document.getElementById("listAjaxDiv");
		if(lad.style.display !="none"){
			alert("请先在列表中点选要配布的名称,点击确定,再执行后续操作!");
		}else{
			// 先在reportListCheck区域设置名为"report"的checkbox控件
			document.getElementById("reportListCheck").innerHTML = "<%=dealReportList %>";
			if(curRepNameList!=""){
				// 清空所有checked项
				repBtn.cancle();
				// 对"report"中复选框按照当前repNameList中值标记
				var curRepsArray = curRepNameList.split(",");
				var reports=document.getElementsByName("report");
				for(var i=0;i<reports.length;i++){
				  for(var j=0;j<curRepsArray.length;j++){
					  if(reports[i].value == curRepsArray[j]){
						  reports[i].checked = true;
						  break;
					  }
				  }
				} 
			}
			// 区域显示
			lad.style.display = "block";
		}
	};
	
	function rolListClick(){
		var curRoleIdList = document.getElementById("roleIdList").value;
		// 区域取得
		var lad = document.getElementById("listAjaxDiv");
		if(lad.style.display !="none"){
			alert("请先在列表中点选要配布的名称,点击确定,再执行后续操作!");
		}else{
			// 先在reportListCheck区域设置名为"role"的checkbox控件
			document.getElementById("reportListCheck").innerHTML = "<%=dealRoleList %>";
			if(curRoleIdList!=""){
				// 清空所有checked项
				repBtn.cancle();
				// 对"role"中复选框按照当前roleIdList中值标记
				var curRolesArray = curRoleIdList.split(",");
				var roles=document.getElementsByName("role");
				for(var i=0;i<roles.length;i++){
				  for(var j=0;j<curRolesArray.length;j++){
					  if(roles[i].value == curRolesArray[j]){
						  roles[i].checked = true;
						  break;
					  }
				  }
				} 
			}
			// 区域显示
			lad.style.display = "block";			
		}	
	};
	
	function btnExtraOpers(){
		var judgeFlg = "";
		this.judge = function(){
			var reports=document.getElementsByName("report");
			var roles=document.getElementsByName("role");
			if(reports.length==0&&roles.length>0){
				judgeFlg = "role";
			}else if(reports.length>0&&roles.length==0){
				judgeFlg = "report";
			}		
		};	
		this.save = function(){
			this.judge();
			// 选中的报表名列表取得
			var reportList = "";
			var reports=document.getElementsByName(judgeFlg);
			for (var i=0;i<reports.length;i++){
			  if(reports[i].checked == true){
				  reportList += reports[i].value + ",";
			  }
			}
			// 将拼接字符串末尾的','去掉
			if(reportList!=""){
				reportList = reportList.substring(0,reportList.length-1);
			}
			if(reportList==""){
				if(judgeFlg=="report"){
					alert("请在报表名组中点选要配布的报表名称!");
				}else if(judgeFlg=="role"){
					alert("请在角色列表中点选要配布的角色名称!");
				}
			}else{
				if(judgeFlg=="report"){
					document.getElementById("repNameList").value = reportList;
				}else if(judgeFlg=="role"){
					document.getElementById("roleIdList").value = reportList;
				}		
				document.getElementById("listAjaxDiv").style.display = "none";
			}
		};
		this.cancle = function(){
			this.judge();
			var reports=document.getElementsByName(judgeFlg);
			for (var i=0;i<reports.length;i++){
			  reports[i].checked = false;
			}
		}
	};
	
	var repBtn = new btnExtraOpers();
	
	
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;<%=pageTitle %></span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="hide_stg_id" value=""/>
		<input type="hidden" name="input_stg_name" value=""/>
		<input type="hidden" name="input_stg_desp" value=""/>
		<input type="hidden" name="input_send_internal" value=""/>
		<input type="hidden" name="input_send_time_hour" value=""/>
		<input type="hidden" name="input_send_time_minute" value=""/>
		<input type="hidden" name="input_send_type" value=""/>
		<input type="hidden" name="input_deliver_terminal" value=""/>
		<input type="hidden" name="input_reportList" value=""/>
		<input type="hidden" name="input_roleList" value=""/>
		<div style="height: 520px; float: left;">
			<table style="width: 500px" class="x-data-table">
				<tr>
					<th style="width: 45%" class="x-data-table-th">策略名称[*]</th>
					<td class="x-data-table-td"><input type="text" size="40" name="stgName" id="stgName" <% out.print(acTurn); %> value="<%=selStgName %>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">策略描述</th>
					<td class="x-data-table-td"><input type="text" size="40" name="stgDesp" id="stgDesp" <% out.print(acTurn); %> value="<%=selStgDesp %>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">发送频率</th>
					<td class="x-data-table-td">
						<select id="sendInternal" name="sendInternal" style="width:270px" <% out.print(acTurn); %> >
							<option value="1">每天</option>
							<option value="2">每周</option>
							<option value="3">每月</option>
						</select></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">发送时间</th>
					<td class="x-data-table-td">
						<select id="sendTimeHour" name="sendTimeHour" <% out.print(acTurn); %> >
							<option value="0">00</option>
							<option value="1">01</option>
							<option value="2">02</option>
							<option value="3">03</option>
							<option value="4">04</option>
							<option value="5">05</option>
							<option value="6">06</option>
							<option value="7">07</option>
							<option value="8">08</option>
							<option value="9">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
						</select> 时
						<select id="sendTimeMinute" name="sendTimeMinute" style='display:none;' <% out.print(acTurn); %> >
							<option value="0">00</option>
							<option value="10">10</option>
							<option value="20">20</option>
							<option value="30">30</option>
							<option value="40">40</option>
							<option value="50">50</option>
						</select> <!-- 分 -->
					</td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">发送方式</th>
					<td class="x-data-table-td">
						<select id="sendType" name="sendType" style="width:270px" <% out.print(acTurn); %> onChange="setDeliverTerminal()">
							<option value="1">邮件</option>
							<option value="2">彩信</option>
						</select></td>
				</tr>
				<tr>
					<th style='width: 45%' class='x-data-table-th'><span id='sendTypeSpan'></span></th>
					<td class="x-data-table-td"><input type="text" size="40" name="deliverTerminal" id="deliverTerminal" <% out.print(acTurn); %> value="<%=selDeliverTerminal %>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">报表名列表项[*]</th>
					<td class="x-data-table-td">
						<input type="text" size="40" name="repNameList" id="repNameList" <% out.print(acTurn); %> value="<%=selReportNameList %>" onclick="repListClick()"/>
					</td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">接收方(角色列表项)[*]</th>
					<td class="x-data-table-td">
						<input type="text" size="40" name="roleIdList" id="roleIdList" <% out.print(acTurn); %> value="<%=selDeliverRoleList %>" onclick="rolListClick()"/>
					</td>
				</tr>
			</table>
			<div class="x-client-form">
				<%
					if(!"".equals(acTurn)){
						out.print("<input class='tableBtn' type='button' name='button_Enabled' id='btnEnabled' value='编辑' onclick='btnOper.enabled()' />");
					}
				%> 
	    		<input class="tableBtn" type="button" name="button_Save" id="btnSave" value="保存" onclick="btnOper.save()" /> 
	    		<input class="tableBtn" type="button" name="button_Back" id="btnBack" value="返回" onclick="btnOper.back()" /> 
	  		</div>
		</div>
		<div style="height: 540px; float: right;">
			<div id="listAjaxDiv" style="display:none; float: left; border-style: solid; border-width: 1px; border-color: #000000; overflow: scroll; width: 260px; height: 50%">
				<div class="x-client-form" id="reportListCheck">
				</div>
				<hr/>
				<div class="x-client-form">
					<input class="tableBtn" type="button" name="button_OK" id="repBtnSave" value="确定" onclick="repBtn.save()" /> 
	    			<input class="tableBtn" type="button" name="button_Cancle" id="repBtnBack" value="取消" onclick="repBtn.cancle()" /> 
				</div>
			</div>
		</div>
	</form>

</body>
</html>