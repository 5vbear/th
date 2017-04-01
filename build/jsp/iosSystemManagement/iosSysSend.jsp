<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="th.util.StringUtils" %>	
<%@ page import="java.util.HashMap" %>
<%
    String zNodes = (String)request.getAttribute( "zNodes" );
	Object[] resultData = (Object[])request.getSession().getAttribute("packagesArray");
	String ctx = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>Insert title here</title>
<link rel="stylesheet" href="../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../css/channel.css" type="text/css">
<link rel="stylesheet" href="../../css/machine.css" type="text/css"/>
<link rel="stylesheet" href="../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="../../css/updateManagement.css" type="text/css">
<script type="text/javascript" src="../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="../../js/Calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript">
//**************
var oCalendarChs=new PopupCalendar("oCalendarChs"); //初始化控件时,请给出实例名称:oCalendarChs
oCalendarChs.weekDaySting=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
oCalendarChs.monthSting=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
oCalendarChs.oBtnTodayTitle="今天";
oCalendarChs.oBtnCancelTitle="取消";
oCalendarChs.Init();
//**************
    var alertInfo_ChooseMacOrOrg='请选择下发的端机或者组织!';
    var alertInfo_ChooseHasNoAnyMac='您的选择不包含任何端机!';

	var ctx='<%=ctx%>';

	var zTreeObj, rMenu;
	var setting = {	
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		},			
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pid",
				rootPId : "org_0",
			}
		},
		view: {
			selectedMulti: false
		}
	};
	
	var zNodes = <%= zNodes %>;
	$(document).ready(function() {
		initTable();
		intiTree();
		$('#deleteButton').click(deleteButton);
		$('#installButton').click(installButton);
		$('#updateButton').click(updateButton);
		$('#expandAllButton').click(expandAllButton);
		$('#closeAllButton').click(closeAllButton);
	});	
	

	function expandAllButton(){
		zTreeObj.expandAll(true);
		checkedAll = true;
	}
	function closeAllButton(){
		zTreeObj.expandAll(false);
		checkedAll = false;
	}
	
	function intiTree(){
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
		// 设置ztree不同层级的节点显示图标 
		for (var i=0; i<nodes.length; i++) {
			var level = 0;
			level = nodes[i].level;
			if(level>4) continue;
			if(nodes[i].mactype=='mac'){
				if(nodes[i].isParent){
					nodes[i].iconSkin = "pIcon04";
				}else{
					nodes[i].iconSkin = "icon04";
				}
			}else{
				level = level + 1;
				if(nodes[i].isParent){
					nodes[i].iconSkin = "pIcon0" + level;
				}else{
					nodes[i].iconSkin = "icon0" + level;
				}
			}
			zTreeObj.updateNode(nodes[i]);
		}
		zTreeObj.expandNode(zTreeObj.getNodeByTId('1'),true);
	}
	
	function deleteButton(){
	  	var selectNodes = zTreeObj.getCheckedNodes();
	  	if(selectNodes==null||selectNodes.length==0){
	  		alert(alertInfo_ChooseMacOrOrg);
	  		return;
	  	}
	  	
	  	if(!confirm('要对选择的端机进行删除操作吗?')){
	  		 return;
	  	}

	  	var selectMacValues = getCheckedNodesValues();
	  	if(selectMacValues.length==0){
	  		alert(alertInfo_ChooseHasNoAnyMac);
	  		return;
	  	}
		
	   $.ajax({
		   type:"post",
		   url:ctx+"/jsp/iosManagement/IosManagementServlet.html?action=delete",
		   data:{
		   		macIds:selectMacValues.substring(1)
		   },
		   success:function(data,textStatus){
		    	if(data=="true") alert('删除成功!');
		    	else alert('删除失败!');
		   }
		});
	}
	function installButton(){
		
	  	var selectNodes = zTreeObj.getCheckedNodes();
	  	if(selectNodes==null||selectNodes.length==0){
	  		alert(alertInfo_ChooseMacOrOrg);
	  		return;
	  	}
	  	
	  	if(!confirm('要对选择的端机进行安装操作吗?')){
	  		 return;
	  	}

	  	var selectMacValues = getCheckedNodesValues();
	  	if(selectMacValues.length==0){
	  		alert(alertInfo_ChooseHasNoAnyMac);
	  		return;
	  	}
		
	   $.ajax({
		   type:"post",
		   url:ctx+"/jsp/iosManagement/IosManagementServlet.html?action=install",
		   data:{
		   		macIds:selectMacValues.substring(1)
		   },
		   success:function(data,textStatus){
		    	if(data=="true") alert('安装成功!');
		    	else alert('安装失败!');
		   }
		});
	}
	function updateButton(){
		
	  	var selectNodes = zTreeObj.getCheckedNodes();
	  	if(selectNodes==null||selectNodes.length==0){
	  		alert(alertInfo_ChooseMacOrOrg);
	  		return;
	  	}
	  	
	  	if(!confirm('要对选择的端机进行更新操作吗?')){
	  		 return;
	  	}

	  	var selectMacValues = getCheckedNodesValues();
	  	if(selectMacValues.length==0){
	  		alert(alertInfo_ChooseHasNoAnyMac);
	  		return;
	  	}
		
	   $.ajax({
		   type:"post",
		   url:ctx+"/jsp/iosManagement/IosManagementServlet.html?action=install",
		   data:{
		   		macIds:selectMacValues.substring(1)
		   },
		   success:function(data,textStatus){
		    	if(data=="true") alert('更新成功!');
		    	else alert('更新失败!');
		   }
		});
	}
	
	function getCheckedNodesValues(){
		var selectNodes = zTreeObj.getCheckedNodes();
		var selectMacValues = "";
	  	for(var i=0;i<selectNodes.length;i++){
	  		var tmpNodes = selectNodes[i];
	  		if(tmpNodes.mactype=='mac'){
	  			selectMacValues = selectMacValues+","+tmpNodes.mac;
	  		}
	  	}
	  	return selectMacValues;
	}
	function isCheck(){
		 var isCheck = false;
		 $('[name="list"]').each(function(){
	    	if($(this).attr("checked")){
	    		isCheck = true;
	  		}
	  	});
	  	if(!isCheck){
	  		alert(alertInfo_PleaseChoosePackage);
	  	}
	  	return isCheck;
	}
	
	function initTreeByOS() {
		var osType = $('#osTypeId').val();
		if(osType=="") return;
		var selectNodes = zTreeObj.getCheckedNodes();
		var hasMachineChecked = false;
		var selectNodesNumPre = 0;
		
	  	for(var i=0;i<selectNodes.length;i++){
	  		var tmpNode = selectNodes[i];
	  		if(tmpNode.mactype=='mac'&&tmpNode.os==osType){
				tmpNode.checked=true;
				zTreeObj.updateNode(tmpNode);
				selectNodesNumPre++;
	  		}
	  		else{
	  			if(tmpNode.mactype=='mac'){
	  				selectNodesNumPre++;
	  			}
				tmpNode.checked=false;
				zTreeObj.updateNode(tmpNode);
	  		}
	  	}
	  	selectNodes = zTreeObj.getCheckedNodes();
	  	
	  	var selectNodesNumBeh = selectNodes.length;
	  	
	  	for(var i=0;i<selectNodes.length;i++){
	  		var tmpNode = selectNodes[i];
	  		if(tmpNode.parentTId!=null){
	  			doCheckStatus(tmpNode);
	  		}
	  	}
	  	
	  	if(selectNodesNumPre != selectNodesNumBeh){
	  		alert(alertInfo_WarnForNoUpdate);
	  	}
	}
	function doCheckStatus(tmpNode){
		tmpNode.checked=true;
		zTreeObj.updateNode(tmpNode);
		if(tmpNode.id == 'org_0'){
			return;
		}
  		if(tmpNode.parentTId!=null){
  			doCheckStatus(zTreeObj.getNodeByTId(tmpNode.parentTId));
  		}
  		return;
	}
	
	
	//*************************************************************************
	function initTable(){
		var tablename = $('#dataTableId')[0];
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

</SCRIPT>
<style type="text/css">
.ztree li span.button.pIcon01_ico_open{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon01_ico_close{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/2-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon01_ico_docu{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_open{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_close{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/3-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon02_ico_docu{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_open{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_close{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/4-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon03_ico_docu{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_open{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_close{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon04_ico_docu{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
</style>

</head>
<body>


<form method="POST" name="form_data" action="" id="sendUpdateOrder">
<input type="hidden" id="checkedIds">
<div class="x-title"><span>&nbsp;&nbsp;系统升级管理-系统升级包下发</span></div>
<table><tr style ="heigt:30px"></tr></table>
	<div>
	<div style="float:left;width:30%">
		<fieldset style="width:90%;height:100px;">
			<ul id="treeDemo" class="ztree" style="margin-top:0; width:100%;"></ul>
			<div style="float: center">
			<input class="tableBtn" id="expandAllButton" type="button" value="打开全部" />
			<input class="tableBtn" id="closeAllButton" type="button" value="关闭全部" />
			</div>
		</fieldset>
	</div>
	
	<div style="float:left;width:70%">
		<div style="overflow-y:auto;">
				<table class="x-data-table" id="dataTableId" width="100%">
					<tr>
						<th style="width:20%">包名</th>
					</tr>
					<%
						if(resultData!=null&&resultData.length>=1){
							for(int i=0;i<resultData.length;i++){
								out.print("<tr><td class='row'>&nbsp;"+resultData[i].toString()+"</td></tr>");
							}
						}
					%>
					</tr>
				</table>
		</div>
		<div>
				<div style="float: right">
					<input class="tableBtn" id="installButton" type="button" value="安装" />
					<input class="tableBtn" id="updateButton" type="button" value="更新" />
					<input class="tableBtn" id="deleteButton" type="button" value="删除" />
				</div>
		</div>
	</div>
</div>
<!-- </fieldset> -->
</form>	

</body>

</html>