<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="th.com.util.Define" %>
    
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
	String zNodes = (String) request.getAttribute( "zNodes" );
	List fileNameList = (List) request.getAttribute( "fileList" );
	long curSelMacId = Long.parseLong((String) request.getAttribute( "curSelMac" ));
	String macFilePath = (String) request.getAttribute( "selMacFtpUrl" );

	int listCount = 0;
	int pageLimit = Define.VIEW_NUM;
	int pagesNum = 0;
	int curPageIndex = Integer.parseInt( (String) request.getAttribute( "CurPageIndex" ) );
	String jumpVisiableFlg = "";

	if ( fileNameList != null && fileNameList.size() > 0 ) {
		// 总记录条数
		listCount = fileNameList.size();
		// 总页数 
		if ( listCount % pageLimit > 0 ) {
			pagesNum = listCount / pageLimit + 1;
		}
		else {
			pagesNum = listCount / pageLimit;
		}
		jumpVisiableFlg = "";
	}
	else {
		curPageIndex = 0;
		jumpVisiableFlg = "disabled='disabled'";
	}
	
	String saveResult = (String)request.getAttribute( "saveResult" );
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>文件管理页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
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
		callback : {
			onClick : zTreeOnClick
		},
		view : {
			selectedMulti : false

		}

	};
	var zNodes = <%=zNodes%>
	$(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		
		var curSelMacId = "<%=curSelMacId%>";
		var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
		// 设置ztree不同层级的节点显示图标 
		for (var i=0; i<nodes.length; i++) {
			
			var level = 0;
			level = nodes[i].level;
			level = level + 1;
			if(nodes[i].isParent){
				nodes[i].iconSkin = "pIcon0" + level;
			}else{
				if(nodes[i].mactype == 'mac'){
					nodes[i].iconSkin = "icon04";
					if(nodes[i].id.substring(1)==curSelMacId){
						zTreeObj.selectNode(nodes[i]);
					}
				}else{
					nodes[i].iconSkin = "icon0" + level;
				}
			}
			zTreeObj.updateNode(nodes[i]);
		}
	});	
	
	window.onload = function(){
		var saveResult = "<%=saveResult%>";
		if(saveResult!=""){
			alert(saveResult);
		}
	};
	
	function zTreeOnClick(event, treeId, treeNode) {
	    // 如果当前节点是端机，触发动作
	    if(treeNode.mactype=="mac"){
	    	window.document.form_data.dealFlg.value = "get";
			window.document.form_data.sel_mac_id.value = treeNode.id.substring(1);
			window.document.form_data.submit();
	    }
	}; 
	
	function check(me){
		var chkVal = me.value;
		var selFlg = false;
		if(me.checked == true){
			selFlg = true;
		}else{
			selFlg = false;
		}
		var selFileList=document.getElementsByName("fileListCheckbox");
		if(chkVal==-10){				
			for (var i=0;i<selFileList.length;i++){
				selFileList[i].checked = selFlg;
			}
		}else{
			if(!selFlg){	
				for (var i=0;i<selFileList.length;i++){
					if(selFileList[i].value==-10){
						selFileList[i].checked = selFlg;
						break;
					}
				}
			}else{
				var count = 0;
				for (var i=0;i<selFileList.length;i++){
					if(selFileList[i].value!=-10&&selFileList[i].checked==selFlg){
						count++;
					}
				}
				if(count==selFileList.length-1){
					for (var i=0;i<selFileList.length;i++){
						if(selFileList[i].value==-10){
							selFileList[i].checked = selFlg;
							break;
						}
					}
				}
			}
		}

	};
	
	function deleteFile(me){		
		var btnId = me.id;
		// "btnDel_"
		var fileName = btnId.substring(7);
		window.document.form_data.dealFlg.value = "del";
		window.document.form_data.sel_file_name.value = fileName;
		var nodes = zTreeObj.getSelectedNodes();
		var macId = nodes[0].id.substring(1);
		window.document.form_data.sel_mac_id.value = macId;
		window.document.form_data.submit();

	};
	
	function downloadFile(me){
		var btnId = me.id;
		// "btnDL_"
		var fileName = btnId.substring(6);
		window.document.form_data.dealFlg.value = "download";
		window.document.form_data.sel_file_name.value = fileName;
		var nodes = zTreeObj.getSelectedNodes();
		var macId = nodes[0].id.substring(1);
		window.document.form_data.sel_mac_id.value = macId;
		window.document.form_data.submit();
	};
	
	function btnOperations() {
		
		this.bathDel = function(){
			var selectFiles="";
			var selFileList=document.getElementsByName("fileListCheckbox");
			for (var i=0;i<selFileList.length;i++){
				if(selFileList[i].checked == true&&selFileList[i].value != -10){
					selectFiles += selFileList[i].value + ",";
				}
			}
			if(selectFiles==""){
				alert("请先点选待删除的文件，然后再执行批量删除操作!");
			}else{
				window.document.form_data.dealFlg.value = "bathDel";
				window.document.form_data.sel_file_List.value = selectFiles;
				var nodes = zTreeObj.getSelectedNodes();
				var macId = nodes[0].id.substring(1);
				window.document.form_data.sel_mac_id.value = macId;
				window.document.form_data.submit();
			}
		}		
		
	};	
	function btnPageOperate(){
		var tempCurPageIndex = <%=curPageIndex%>;
		var tempCurPageNum = <%=pagesNum%>;
		this.init = function(){
			window.document.form_data.dealFlg.value = "jump";
			var nodes = zTreeObj.getSelectedNodes();
			var macId = nodes[0].id.substring(1);
			window.document.form_data.sel_mac_id.value = macId;		
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
<style type="text/css">
.ztree li span.button.pIcon01_ico_open{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon01_ico_close{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/2-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon01_ico_docu{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_open{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_close{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/3-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon02_ico_docu{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_open{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_close{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/4-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon03_ico_docu{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_open{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_close{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon04_ico_docu{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
</style>
</head>
<body>

	
	
	<div class="x-title"><span>&nbsp;&nbsp;系统管理-文件管理</span></div>
	<table><tr style ="heigt:30px"></tr></table>
	<form class="x-client-form" method="POST" name="form_data" action="/th/jsp/sysMang/fileMang/upFileDeal.html">
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="sel_mac_id" value=""/>
		<input type="hidden" name="sel_file_name" value=""/>
		<input type="hidden" name="sel_file_List" value=""/>
		<input type="hidden" name="page_jump_index" value=""/>

		<div style="float:left;width:30%">
			<fieldset style="width:90%;height:100px;">
				<ul id="treeDemo" class="ztree" style="margin-top:0; width:100%;"></ul>
			</fieldset>
		</div>
		<div style="float:left;width:70%">
			<div style="overflow-y:auto;">
				<table class="x-data-table" id="dataTableId" width="100%">
					<tr>
						<th style="width: 2%" class="x-data-table-th"><input
							type="checkbox" name="fileListCheckbox" value="-10" onclick='check(this)' /></th>
						<th style='width: 35%' class='x-data-table-th'>文件名称</th>
						<th style='width: 15%' class='x-data-table-th'>操作</th>
					</tr>
					<%
						if ( fileNameList != null && fileNameList.size() > 0 ) {
							// 起点：
							int startIndex = ( curPageIndex - 1 ) * pageLimit;
							// 终点： 
							int endIndex = 0;
							if ( curPageIndex < pagesNum ) {
								endIndex = curPageIndex * pageLimit;
							}
							else {
								endIndex = fileNameList.size();
							}

							for ( int i = startIndex; i < endIndex; i++ ) {
								// 拼接返还的文件列表
								// top
								out.print( "<tr><td style='width: 2%' class='x-data-table-td-left-pad'><input type='checkbox' name='fileListCheckbox' value='"
										+ fileNameList.get( i ) + "' onclick='check(this)'/></td>" );
								// middle
								out.print( "<td class='x-data-table-td-left-pad'>" + fileNameList.get( i ) + "</td> " );
								// bottom
								/* out.print( "<td class='x-data-table-td-left-pad' ><input class='tableBtn' type='button' value='删除' name='buttonDelete_"
										+ fileNameList.get( i ) + "' id='btnDel_" + fileNameList.get( i ) + "' onclick='deleteFile(this)'/><input class='tableBtn' type='button' value='下载' name='buttonDownload_"
										+ fileNameList.get( i ) + "' id='btnDL_" + fileNameList.get( i ) + "' onclick='downloadFile(this)'/></td></tr>" ); */
								out.print( "<td class='x-data-table-td-left-pad' ><input class='tableBtn' type='button' value='删除' name='buttonDelete_"
										+ fileNameList.get( i ) + "' id='btnDel_" + fileNameList.get( i ) + "' onclick='deleteFile(this)'/>"
										+ "&nbsp;<a href='" + macFilePath + "/" + fileNameList.get( i ) + "' target='_blank' class='tableLink'>下载</a></td></tr>" );
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
				<%
					out.print( "<tr><td>[当前第" + curPageIndex + "页/共" + pagesNum + "页][每页" + pageLimit + "条][共" + listCount + "条]</td></tr>" );
				%>
			</div>
			<div style="float: right">
				<input class="tableBtn" type="button" name="button_BathDelete" id="btnBatDel" value="批量删除" onclick="btnOper.bathDel()" /> 
			</div>
		</div>

</body>
</html>