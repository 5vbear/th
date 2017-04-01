<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.dao.StrategyDealDAO" %>
<%@ page import="th.action.OrgDealAction" %>
<%@ page import="th.entity.TaskBean" %>

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

	HashMap[] tasksList = (HashMap[]) request.getAttribute( "TasksList" );
	TaskBean condTaskBean = (TaskBean) request.getAttribute( "ConTask" );
	String conStgName = condTaskBean.getStgName();
	String conSendInternal = condTaskBean.getSendInternal();
	String conSendType = condTaskBean.getSendType();

	int listCount = 0;
	int pageLimit = Define.VIEW_NUM/2;
	int pagesNum = 0;
	int curPageIndex = Integer.parseInt((String) request.getAttribute("CurPageIndex"));
	String jumpVisiableFlg = "";
	
	if (tasksList != null && tasksList.length > 0) {
		// 总记录条数
		listCount = tasksList.length;
		// 总页数 
		if (listCount % pageLimit > 0) {
			pagesNum = listCount / pageLimit + 1;
		} else {
			pagesNum = listCount / pageLimit;
		}
		jumpVisiableFlg = "";
	} else {
		curPageIndex = 0;
		jumpVisiableFlg = "disabled='disabled'";
	}

	String saveResult = (String)request.getAttribute( "saveResult" );
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>策略列表页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../../css/machine.css" />

<SCRIPT LANGUAGE="JavaScript">

	window.onload = function showtable() {
		var tablename = document.getElementById("dataTableId");
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
		
		var conSendInternal = "<%=conSendInternal %>";
		var conSendType = "<%=conSendType %>";
		if(conSendInternal!=""){
			document.getElementById("sendInternal").value = conSendInternal;
		}
		if(conSendType!=""){
			document.getElementById("sendType").value = conSendType;
		}
		var saveResult = "<%=saveResult%>";
		if(saveResult!=""){
			alert(saveResult);			
		}
	};
	function onFold(id) {
		var vDiv = document.getElementById(id);
		vDiv.style.display = (vDiv.style.display == 'none') ? 'block' : 'none';
		var fold = document.getElementById('clientStyleId');
		if (vDiv.style.display === 'none') {
			fold.className = 'x-fold-plus';
		} else {
			fold.className = 'x-fold-minus';
		}
	};

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

	function btnOperations() {

		this.addTask = function() {
			window.document.form_data.dealFlg.value = "add";
			window.document.form_data.action = "/th/jsp/sysMang/taskMang/taskDeal.html";
			window.document.form_data.submit();
		};
		this.changeTask = function(me){
			var btnId = me.id;
			// "btnChange_"
			var stgId = btnId.substring(10);
			window.document.form_data.dealFlg.value = "change";
			window.document.form_data.action = "/th/jsp/sysMang/taskMang/taskDeal.html";
			window.document.form_data.sel_stg_id.value = stgId;
			window.document.form_data.submit();
		};
		this.deleteTask = function(me){
			var btnId = me.id;
			// "btnDel_"
			var stgId = btnId.substring(7);
			window.document.form_data.dealFlg.value = "del";
			window.document.form_data.action = "/th/jsp/sysMang/taskMang/taskList.html";
			window.document.form_data.sel_stg_id.value = stgId;
			window.document.form_data.submit();
		};
		this.enableTask = function(me){
			var btnId = me.id;
			// "btnEnable_"
			var stgId = btnId.substring(10);
			window.document.form_data.dealFlg.value = "enable";
			window.document.form_data.action = "/th/jsp/sysMang/taskMang/taskList.html";
			window.document.form_data.sel_stg_id.value = stgId;
			window.document.form_data.submit();
		};
		this.disableTask = function(me){
			var btnId = me.id;
			// "btnDisable_"
			var stgId = btnId.substring(11);
			window.document.form_data.dealFlg.value = "disable";
			window.document.form_data.action = "/th/jsp/sysMang/taskMang/taskList.html";
			window.document.form_data.sel_stg_id.value = stgId;
			window.document.form_data.submit();
		};
		this.search = function(){
			window.document.form_data.dealFlg.value = "search";
			window.document.form_data.action = "/th/jsp/sysMang/taskMang/taskList.html";
			// 拼接搜索条件 
			var serCond = "";
			var stgName = document.getElementById("stgName");
			if(stgName.value!=""){
				serCond += stgName.value + ",";
			}else{
				serCond += "null,";
			}		
			var sendInternal = document.getElementById("sendInternal");
			if(sendInternal.value!="-1"){
				serCond += sendInternal.value + ",";
			}else{
				serCond += "null,";
			}
			var sendType = document.getElementById("sendType");
			if(sendType.value!="-1"){
				serCond += sendType.value;
			}else{
				serCond += "null";
			}
			window.document.form_data.search_con_info.value = serCond;
			window.document.form_data.submit();

		}

	};
	
	function btnPageOperate(){
		var tempCurPageIndex = <%= curPageIndex %>;
		var tempCurPageNum = <%= pagesNum %>;
		this.init = function(){
			window.document.form_data.dealFlg.value = "jump";
			window.document.form_data.action = "/th/jsp/sysMang/taskMang/taskList.html";
			// 拼接搜索条件 
			var serCond = "";
			var stgName = document.getElementById("stgName");
			serCond += stgName.value + ",";
			var sendInternal = document.getElementById("sendInternal");
			if(sendInternal.value!="-1"){
				serCond += sendInternal.value + ",";
			}else{
				serCond += "null,";
			}
			var sendType = document.getElementById("sendType");
			if(sendType.value!="-1"){
				serCond += sendType.value;
			}else{
				serCond += "null";
			}
			window.document.form_data.search_con_info.value = serCond;	
		};
		this.first = function(){
			if(tempCurPageIndex==0){
				alert("当前页面中没有查询数据，页面跳转功能无效，请注意!");
			}else if(tempCurPageIndex==1){
				alert("当前页面已经是数据的首页面!");
			}else{
				this.init();
				window.document.form_data.page_jump_index.value = 1;
				window.document.form_data.submit();
			}		
		};
		this.previous = function(){
			if(tempCurPageIndex==0){
				alert("当前页面中没有查询数据，页面跳转功能无效，请注意!");
			}else if(tempCurPageIndex==1){
				alert("当前页面已经是数据的首页面，不能再向前翻页!");
			}else{
				this.init();
				window.document.form_data.page_jump_index.value = tempCurPageIndex-1;
				window.document.form_data.submit();
			}
		};
		this.next = function(){
			if(tempCurPageIndex==0){
				alert("当前页面中没有查询数据，页面跳转功能无效，请注意!");
			}else if(tempCurPageIndex==tempCurPageNum){
				alert("当前页面已经是数据的尾页面，不能再向后翻页!");
			}else{
				this.init();
				window.document.form_data.page_jump_index.value = tempCurPageIndex+1;
				window.document.form_data.submit();
			}
		};
		this.last = function(){
			if(tempCurPageIndex==0){
				alert("当前页面中没有查询数据，页面跳转功能无效，请注意!");
			}else if(tempCurPageIndex==tempCurPageNum){
				alert("当前页面已经是数据的尾页面!");
			}else{
				this.init();
				window.document.form_data.page_jump_index.value = tempCurPageNum;
				window.document.form_data.submit();
			}
		};
		/* this.goTo = function(){
			var pIndex = document.getElementById("pageIndex");
			if(pIndex.value==""||pIndex.value<0||pIndex.value>tempCurPageNum){
				alert("当前输入的跳转页面数是一个无效值，请重新输入!");
			}else{
				this.init();
				window.document.form_data.page_jump_index.value = pIndex.value;
				window.document.form_data.submit();
			}
		} */
		
	};	
	
	var btnOper = new btnOperations();
	var pageOper = new btnPageOperate();
	
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;系统管理-策略定义</span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="page_jump_index" value=""/>
		<input type="hidden" name="search_con_info" value=""/>
		<input type="hidden" name="sel_stg_id" value=""/>
		<div class="x-title">
			<div id="clientStyleId" class="x-fold-minus" onclick="onFold('foldId');" />
		</div>
		<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
		</div>
		<div id="foldId" style="width: 100%; display: block; height: 30px; line-height: 30px; background-color: #B2DFEE">
			<div style="float: left">
				<span>策略名称</span>
				<input type="text" name="stgName" id="stgName" value="<%= conStgName %>" />
				</select>
			</div>
			<div style="float: left">
				<span>&nbsp;&nbsp;发送频率</span> 
				<select id="sendInternal" name="sendInternal"  >
					<option value="-1">全部</option>
					<option value="1">每天</option>
					<option value="2">每周</option>
					<option value="3">每月</option>
				</select>
			</div>
			<div style="float: left">
				<span>&nbsp;&nbsp;发送方式</span>
				<select id="sendType" name="sendType" >
					<option value="-1">全部</option>
					<option value="1">邮件</option>
					<option value="2">彩信</option>
				</select> 
			</div>
			<span>&nbsp;&nbsp;</span> 
			<input class="tableBtn" type="button" name="button_Search" id="btnSer" value="搜索" onclick="btnOper.search()"/>
		</div>
		<table><tr style ="heigt:30px"></tr></table>
		<div style="overflow-x:hidden;overflow-y:scroll;width:100%;height:550px;">
			<div class="x-data"><span style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span></div>
			<div>
				<table class="x-data-table" id="dataTableId">
					<%
						out.print( "<tr><th style='width: 10%' class='x-data-table-th'>策略名称</th>"
								+ "<th style='width: 5%' class='x-data-table-th'>发送频率</th>"
								+ "<th style='width: 5%' class='x-data-table-th'>发送时间</th>"
								+ "<th style='width: 5%' class='x-data-table-th'>发送方式</th>"
								+ "<th style='width: 200px' class='x-data-table-th'>接收邮箱</th>"
								+ "<th style='width: 18%' class='x-data-table-th'>建立时间</th>"
								+ "<th style='width: 4%' class='x-data-table-th'>状态</th>"
								+ "<th style='width: 18%' class='x-data-table-th'>报表名列表</th>"
								+ "<th style='width: 20%' class='x-data-table-th'>操作</th></tr>" );

						if ( tasksList != null && tasksList.length > 0 ) {
							
							// 相对于HashMap[]的情况,curPageIndex = curPageIndex-1; 
							// 起点：
							int startIndex = (curPageIndex -1)*pageLimit;
							// 终点： 
							int endIndex = 0;
							if(curPageIndex<pagesNum){
								endIndex = curPageIndex*pageLimit;
							}else{
								endIndex = tasksList.length;
							}

							for ( int i = startIndex; i < endIndex; i++ ) {
								HashMap stgMap = tasksList[i];
								long stgID = Long.parseLong( (String) stgMap.get( "STG_ID" ) );
								String stgName = (String) stgMap.get( "STG_NAME" );
								// 发送频率
								String sendInternalIden = (String) stgMap.get( "SEND_INTERNAL" );
								String sendInternalContent = "";
								if(Define.CHAR_IDENTIFY_ONE.equals(sendInternalIden)){
									sendInternalContent = Define.SEND_INTERNAL_DAY;
								}else if(Define.CHAR_IDENTIFY_TWO.equals(sendInternalIden)){
									sendInternalContent = Define.SEND_INTERNAL_WEEK;
								}else if(Define.CHAR_IDENTIFY_THREE.equals(sendInternalIden)){
									sendInternalContent = Define.SEND_INTERNAL_MONTH;
								}
								// 发送时间
								int sendTimeHour = Integer.parseInt((String) stgMap.get( "SEND_TIME_HOUR" ));
								int sendTimeMinute = Integer.parseInt((String) stgMap.get( "SEND_TIME_MINUTE" ));
								/* String sendTimeContent = sendTimeHour + " 时 " + sendTimeMinute + " 分"; */	
								String sendTimeContent = sendTimeHour + " 时整 ";
								// 发送方式
								String sendTypeIden = (String) stgMap.get( "SEND_TYPE" );
								String sendTypeContent = "";
								if(Define.CHAR_IDENTIFY_ONE.equals(sendTypeIden)){
									sendTypeContent = Define.SEND_TYPE_EMAIL;
								}else if(Define.CHAR_IDENTIFY_TWO.equals(sendTypeIden)){
									sendTypeContent = Define.SEND_TYPE_MOBILE;
								}
								// 接收方(角色列表)
								/* String deliverTerminal = (String) stgMap.get( "DELIVER_TERMINAL" ); */
								String deliverRoleList = (String) stgMap.get( "DELIVER_ROLE_LIST" );
								OrgDealAction oda = new OrgDealAction();
								String deliverTerminal = oda.getDeliverTerminals( sendTypeIden, deliverRoleList );
								// 策略创建时间
								String createTime = (String) stgMap.get( "OPERATETIME" );
								// 策略状态
								String statusIden = (String) stgMap.get( "STATUS" );
								String statusContent = "";
								if(Define.CHAR_IDENTIFY_ONE.equals(statusIden)){
									statusContent = Define.STRATEGY_STATUS_ENABLE;
								}else if(Define.CHAR_IDENTIFY_ZERO.equals(statusIden)){
									statusContent = Define.STRATEGY_STATUS_DISABLE;
								}
								// 报表名列表
								String repMarkIdStr = (String) stgMap.get( "REPORT_NAME_LIST" );
								String repNameList = "";
								StrategyDealDAO sdd = new StrategyDealDAO();
								HashMap[] reportList = sdd.getReportNameListByMarkIdStr(oda.getSQLCondByStr(repMarkIdStr));
								if(reportList!=null&&reportList.length>0){
									for(int j=0;j<reportList.length;j++){
										repNameList += (String) reportList[j].get( "REPORT_NAME" );
										if(j<reportList.length-1){
											repNameList += ",";
										}
									}
								}
								
								// middle
								out.print( "<td class='x-data-table-td-left-pad'>" + stgName + "</td> "
										+ "<td class='x-data-table-td-left-pad'>" + sendInternalContent + "</td> " 
										+ "<td class='x-data-table-td-left-pad'>" + sendTimeContent + "</td> " 
										+ "<td class='x-data-table-td-left-pad'>" + sendTypeContent + "</td> " 
										+ "<td class='x-data-table-td-left-pad'>" + deliverTerminal + "</td> " 
										+ "<td class='x-data-table-td-left-pad'>" + createTime + "</td> " 
										+ "<td class='x-data-table-td-left-pad'>" + statusContent + "</td> "
										+ "<td class='x-data-table-td-left-pad'>" + repNameList + "</td>" );
								// bottom
								out.print( "<td class='x-data-table-td-left-pad'><input class='tableBtn' type='button' value='编辑' name='buttonChange_"
										+ stgID + "' id='btnChange_" + stgID + "' onclick='btnOper.changeTask(this)'/><input class='tableBtn' type='button' value='删除' name='buttonDelete_"
										+ stgID + "' id='btnDel_" + stgID + "' onclick='btnOper.deleteTask(this)'/>" );
								if(Define.CHAR_IDENTIFY_ONE.equals(statusIden)){
									out.print( "<input class='tableBtn' type='button' value='停止' name='buttonDisable_"
											+ stgID + "' id='btnDisable_" + stgID + "' onclick='btnOper.disableTask(this)'/></td></tr>" );
								}else if(Define.CHAR_IDENTIFY_ZERO.equals(statusIden)){
									out.print( "<input class='tableBtn' type='button' value='启用' name='buttonEnable_"
											+ stgID + "' id='btnEnable_" + stgID + "' onclick='btnOper.enableTask(this)'/></td></tr>" );
								}

							}

						}
					%>
				</table>
			</div>
			<div style="float: left">
				<input type="button" class="x-first-page" name="button_page_first" id="btnFirst" <%= jumpVisiableFlg %> onclick="pageOper.first()"/> &nbsp; 
				<input type="button" class="x-previous-page" name="button_page_previous" id="btnPre" <%= jumpVisiableFlg %> onclick="pageOper.previous()"/> &nbsp; 
				<input type="button" class="x-next-page" name="button_page_next" id="btnNext" <%= jumpVisiableFlg %> onclick="pageOper.next()"/> &nbsp; 
				<input type="button" class="x-last-page" name="button_page_last" id="btnLast" <%= jumpVisiableFlg %> onclick="pageOper.last()"/> &nbsp; 
				<%-- <label>第 <input type="text" size="1" name="pageIndex" value="" id="pageIndex" <%= jumpVisiableFlg %> onkeypress="returnCancel(event);"/>页 </label> 
				<input type="button" class="x-goto-page" name="button_page_goto" id="btnGoto" <%= jumpVisiableFlg %> onclick="pageOper.goTo()"/> --%>
				<%
					out.print( "<tr><td>[当前第" + curPageIndex + "页/共" + pagesNum + "页][每页" + pageLimit + "条][共" + listCount + "条]</td></tr>" );
				%>
			</div>
			<div style="float: right">
				<input class="tableBtn" type="button" name="button_Add" id="btnAdd" value="添加" onclick="btnOper.addTask()" /> 
			</div>
		</div>
	</form>
</body>
</html>