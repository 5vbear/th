<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
    
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
	String ConMacMark = (String) request.getAttribute( "ConMacMark" );
	String ConFileName = (String) request.getAttribute( "ConFileName" );
	String ConTimeStart = (String) request.getAttribute( "ConTimeStart" );
	String ConTimeEnd = (String) request.getAttribute( "ConTimeEnd" );

	HashMap[] upHistorysList = (HashMap[]) request.getAttribute( "searchResults" );

	int listCount = 0;
	int pageLimit = 10;
	int pagesNum = 0;
	int curPageIndex = Integer.parseInt((String)request.getAttribute( "CurPageIndex" ));
	String jumpVisiableFlg = "";

	if ( upHistorysList!= null&&upHistorysList.length>0 ) {
		// 总记录条数
		listCount = upHistorysList.length;
		// 总页数 
		if ( listCount % pageLimit > 0 ) {
			pagesNum = listCount / pageLimit + 1;
		}
		else {
			pagesNum = listCount / pageLimit;
		}
		jumpVisiableFlg = "";
	}else{
		curPageIndex = 0;
		jumpVisiableFlg = "disabled='disabled'";
	}
	
	
	
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>升级历史搜索页面</title>
<link rel="stylesheet" href="../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../css/machine.css" />
<script type="text/javascript" src="../../js/Calendar.js"></script>

<SCRIPT LANGUAGE="JavaScript">

	var oCalendarChs=new PopupCalendar("oCalendarChs"); //初始化控件时,请给出实例名称:oCalendarChs
	oCalendarChs.weekDaySting=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
	oCalendarChs.monthSting=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
	oCalendarChs.oBtnTodayTitle="今天";
	oCalendarChs.oBtnCancelTitle="取消";
	oCalendarChs.Init();
	
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
	function btnOperations() {
		
		this.search = function(){
			var searSTime = document.getElementById("searStartTime");
 			var searETime = document.getElementById("searEndTime");
			//if(searSTime.value==""||searETime.value==""){
				//alert("请先输入检索时间范围!");
			//}else{
				window.document.form_data.dealFlg.value = "search";
				window.document.form_data.action = "/th/jsp/sysUpdateManagement/SysUpdateHistorySearch.html";
				// 拼接搜索条件 
				var serCond = "";
				var contentMachine = document.getElementById("macMark");
				if(contentMachine.value!=""){
					serCond += contentMachine.value + ",";
				}else{
					serCond += "null,";
				}
				var contentFile = document.getElementById("upFileName");
				if(contentFile.value!=""){
					serCond += contentFile.value + ",";
				}else{
					serCond += "null,";
				}
				if(searSTime.value!=""){
					serCond += searSTime.value + ",";
				}else{
					serCond += "null,";
				}
				if(searETime.value!=""){
					serCond += searETime.value;
				}else{
					serCond += "null";
				}
				window.document.form_data.search_con_info.value = serCond;
				window.document.form_data.submit();
			//} 
			
		}
		
	};	
	function btnPageOperate(){
		var tempCurPageIndex = <%= curPageIndex %>;
		var tempCurPageNum = <%= pagesNum %>;
		this.init = function(){
			var searSTime = document.getElementById("searStartTime");
 			var searETime = document.getElementById("searEndTime");
			//if(searSTime.value==""||searETime.value==""){
				//alert("请先输入检索时间范围!");
			//}else{
				window.document.form_data.dealFlg.value = "jump";
				window.document.form_data.action = "/th/jsp/sysUpdateManagement/SysUpdateHistorySearch.html";
				// 拼接搜索条件 
				var serCond = "";
				var contentMachine = document.getElementById("macMark");
				if(contentMachine.value!=""){
					serCond += contentMachine.value + ",";
				}else{
					serCond += "null,";
				}
				var contentFile = document.getElementById("upFileName");
				if(contentFile.value!=""){
					serCond += contentFile.value + ",";
				}else{
					serCond += "null,";
				}
				if(searSTime.value!=""){
					serCond += searSTime.value + ",";
				}else{
					serCond += "null,";
				}
				if(searETime.value!=""){
					serCond += searETime.value;
				}else{
					serCond += "null";
				}
				window.document.form_data.search_con_info.value = serCond;
			//} 
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
		}
		
	};
	var btnOper = new btnOperations();
	var pageOper = new btnPageOperate();

</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;系统升级管理-系统升级历史查询</span></div>
	<table><tr style ="heigt:30px"></tr></table>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="search_con_info" value=""/>
		<input type="hidden" name="page_jump_index" value=""/>
		<div class="x-title">
			<div id="clientStyleId" class="x-fold-minus" onclick="onFold('foldId');" />
		</div>
		<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
		</div>
		<div id="foldId" style="width: 100%; display: block; height: 30px; line-height: 30px; background-color: #B2DFEE">
			<div style="float: left">
				<span>&nbsp;&nbsp;机器标识</span> 
				<input type="text" name="macMark" id="macMark" value="<%=ConMacMark%>" onKeyUp = "checkisChinese(this)" />
			</div>
			<div style="float: left">
				<span>&nbsp;&nbsp;升级文件名称</span> 
				<input type="text" name="upFileName" id="upFileName" value="<%=ConFileName%>" onkeyUp = "checkisChinese(this)" />
			</div>
			<div style="float: left">
				<span>&nbsp;&nbsp;时间范围</span>
				<input type="text" readonly = "true" size="10" id="searStartTime" name="searStartTime" value="<%=ConTimeStart%>" onclick="getDateString(this,oCalendarChs)"/> 
				<span>&nbsp;---&nbsp;</span>
				<input type="text" readonly = "true" size="10" id="searEndTime" name="searEndTime" value="<%=ConTimeEnd%>" onclick="getDateString(this,oCalendarChs)"/>				
			</div>
			<!-- 王经纬   add  start -->
			<script language="javascript" type="text/javascript">  
			function checkisChinese(obj){						//判断输入字符是否为中文
				var val = obj.value;
				if(val!=null||val!=" "){
					var c =val.split("");
					val = "";
					for (var i = 0;i < c.length;i++){
						if((/[\u4e00-\u9fa5]+/).test(c[i])){
							alert("不能输入中文！")；
							break;
						}
						val += c[i];
					}
					obj.value = val;
				} 
			}
			</script>
			<!-- 王经纬 add end -->
			<div>
				<span>&nbsp;&nbsp;</span> 
				<input class="tableBtn" type="button" name="button_Search" id="btnSer" value="搜索" onclick="btnOper.search()"/>
			</div>
		</div>
		<table><tr style ="heigt:30px"></tr></table>
		<div>
			<div class="x-data"><span style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span></div>
			<div>
				<table class="x-data-table" id="dataTableId">
					<%
						
//						out.print( "<tr><td  colspan='7' style='text-align: left' class='x-data-table-td'>&nbsp;&nbsp;端机升级文件履历表[当前第" + curPageIndex + "页/共"
//								+ pagesNum + "页][排序方式:记录ID/排序类型:升序][每页" + pageLimit + "条][共" + listCount + "条]</td></tr>" );
						out.print( "<tr><td  colspan='7' style='text-align: left' class='x-data-table-td'>&nbsp;&nbsp;数据</td></tr>" );
					
						out.print( "<tr><th style='width: 2%' class='x-data-table-th'><input type='checkbox' name='upHistoryListCheckbox' value='-10' disabled='true' style='visibility: hidden'/></th>"
								+ "<th style='width: 10%' class='x-data-table-th'>机器标识</th>"
								+ "<th style='width: 20%' class='x-data-table-th'>升级文件名称</th>"
								+ "<th style='width: 10%' class='x-data-table-th'>说明</th>"
								+ "<th style='width: 10%' class='x-data-table-th'>升级状态</th>"
								+ "<th style='width: 15%' class='x-data-table-th'>创建时间</th>"
								+ "<th style='width: 10%' class='x-data-table-th'>创建人</th></tr>" );

						if ( upHistorysList!= null&&upHistorysList.length>0 ) {

							// 相对于HashMap[]的情况,curPageIndex = curPageIndex-1; 
							// 起点：
							int startIndex = (curPageIndex -1)*pageLimit;
							// 终点： 
							int endIndex = 0;
							if(curPageIndex<pagesNum){
								endIndex = curPageIndex*pageLimit;
							}else{
								endIndex = upHistorysList.length;
							}
							
							for ( int i = startIndex; i < endIndex; i++ ) {
								HashMap upHistory = upHistorysList[i];
								long hisID = Long.parseLong(upHistory.get( "ID" ).toString());
								String mapMacMark = (String) upHistory.get( "MACHINE_MARK" );
								String mapFileName = (String) upHistory.get( "FILE_NAME" );
								String mapDescription = (String) upHistory.get( "DESCRIPTION" );
								String mapStatus = (String) upHistory.get( "STATUS" );
								String mapOpeTime = (String) upHistory.get( "OPERATETIME" );
								String mapUserName = (String) upHistory.get( "REAL_NAME" );
								
								// top
								out.print( "<tr><td style='width: 2%' class='x-data-table-td-left-pad'><input type='checkbox' name='upHistoryListCheckbox' value='" + hisID + "' /></td>" );
								// middle
								out.print( "<td class='x-data-table-td-left-pad'>" + mapMacMark + 
										   "</td> " + "<td class='x-data-table-td-left-pad'>" + mapFileName + 
										   "</td> " + "<td class='x-data-table-td-left-pad'>" + mapDescription + 
										   "</td> " + "<td class='x-data-table-td-left-pad'>" + mapStatus + 
										   "</td> " + "<td class='x-data-table-td-left-pad'>" + mapOpeTime + 
										   "</td> " + "<td class='x-data-table-td-left-pad'>" + mapUserName + "</td>" );
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
			</div>
			
			<%			
				out.print( "<tr><td  colspan='7' style='text-align: left' class='x-data-table-td'>[当前第" + curPageIndex + "页/共"
					+ pagesNum + "页][每页" + pageLimit + "条][共" + listCount + "条]</td></tr>" );
			%>
		</div>
	</form>
</body>
</html>