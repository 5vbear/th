<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="th.util.StringUtils" %>	
<%@ page import="java.util.HashMap" %>
<%
    String zNodes = (String)request.getAttribute( "zNodes" );
    String yyyyMMdd = (String)request.getAttribute( "yyyyMMdd" );
    String hour = (String)request.getAttribute( "hour" );
    String minute = (String)request.getAttribute( "minute" );
    HashMap[] resultData = (HashMap[])request.getSession().getAttribute("packagesArray");
    HashMap[] osTypes = (HashMap[])request.getAttribute( "osTypes" );
    String checkedIds = (String)request.getSession().getAttribute("checkedIds");
    String osTypeId = (String)request.getSession().getAttribute("osType");
    String checkedAll = (String)request.getSession().getAttribute("checkedAll");
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
    var alertInfo_PleaseChoosePackage='请选择应用!';
    var alertInfo_WarnForNoUpdate='类型不匹配的端机将不能被升级!';
    var alertInfo_PleaseChooseDate='请选择要下发的日期!';
    var alertInfo_PleaseChooseTime='请选择要下发的时间!';

	var ctx='<%=ctx%>';
	var checkedAll=<%=checkedAll==null?"false":checkedAll%>;
	var checkedIds='<%=checkedIds==null?"":checkedIds%>';
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
		},
		callback: {
			onCheck: initTreeByOS
		}
	};
	
	var zNodes = <%= zNodes %>;
	$(document).ready(function() {
		initTable();
		intiTree();
		$('#chooseUpdatePackagesButton').click(chooseUpdatePackages);
		$('#sendButton').click(send);
		$('#expandAllButton').click(expandAllButton);
		$('#closeAllButton').click(closeAllButton);
		$('#checkAll').click(function(){
			$('[name="list"]').attr('checked',this.checked);
		});
		$('[name="list"]').click(function(){
			var $tmp = $('[name="list"]');
			$('#checkAll').attr('checked',$tmp.length==$tmp.filter(':checked').length);
		});
		$("#osTypeId").val('<%=osTypeId%>');
		initSendDate();
	});	
	
	function initSendDate(){
		$("#valueTime").val('<%=yyyyMMdd%>');
		$("#valueTime_hh").val(<%=hour%>);
		$("#valueTime_mi").val(<%=minute%>);
	}
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
		if(checkedIds!=""){
			var checkIdsArray = checkedIds.split(",");
			if(checkIdsArray!=null&&checkIdsArray.length>0){
				for(i=0;i<checkIdsArray.length;i++){
					if(checkIdsArray[i]==null||checkIdsArray[i]=="") continue;
					var tmpNode = zTreeObj.getNodeByTId(checkIdsArray[i]);
					tmpNode.checked=true;
					if(tmpNode.mactype!='mac'){
						zTreeObj.expandNode(tmpNode,true);
					}
					zTreeObj.updateNode(tmpNode);
				}
			}
		}
		if(checkedAll){
			zTreeObj.expandAll(checkedAll);
		}
	}
	
	function chooseUpdatePackages(){
		var selectNodes = zTreeObj.getCheckedNodes();
		var checkedIds = "";
		for(k=0;k<selectNodes.length;k++){
			if(!selectNodes[k].checked) continue;
			if(!selectNodes[k].mactype=='mac') continue;
			checkedIds = checkedIds+","+selectNodes[k].tId;
		}
		var osType = $('#osTypeId').val();
        $('#sendUpdateOrder')[0].action=ctx+"/jsp/appStore/ChooseAppStore.html?action=chooseUpdatePackages&checkedIds="+checkedIds.substring(1)+"&checkedAll="+checkedAll+"&osType="+osType;
        $('#sendUpdateOrder')[0].submit();
	}
	function send(){
		if(!isCheck()){
			return;
		}
		
	  	var selectNodes = zTreeObj.getCheckedNodes();
	  	if(selectNodes==null||selectNodes.length==0){
	  		alert(alertInfo_ChooseMacOrOrg);
	  		return;
	  	}

	  	var selectMacValues = getCheckedNodesValues();
	  	
	  	if(selectMacValues.length==0){
	  		alert(alertInfo_ChooseHasNoAnyMac);
	  		return;
	  	}
	  	
	  	var checkValues = "";
	    $('[name="list"]').each(function(){
	        if($(this).attr("checked") == true){
	        	checkValues += $(this).val() + ",";
	        }
	    });
	    
	    var valueTime = $("#valueTime").val(); 
	    var valueTime_hh = $("#valueTime_hh").val(); 
	    var valueTime_mi = $("#valueTime_mi").val(); 
		
	   $.ajax({
		   type:"post",
		   url:ctx+"/jsp/updateManagement/UpdateManagement.html?action=sendPackages",
		   data:{
		   		macIds:selectMacValues.substring(1),
		   		packageIds:checkValues.substring(0,checkValues.length-1),
		   		sendDate:valueTime+" "+valueTime_hh+":"+valueTime_mi+":00"
		   },
		   success:function(data,textStatus){
		    	if(data)
		    		alert('下发成功!');
		    	else
		    		alert('下发失败!');
		   }
		});
	}
	
	function getCheckedNodesValues(){
		var selectNodes = zTreeObj.getCheckedNodes();
		var selectMacValues = "";
	  	for(var i=0;i<selectNodes.length;i++){
	  		var tmpNodes = selectNodes[i];
	  		if(tmpNodes.mactype=='mac'){
	  			selectMacValues = selectMacValues+","+tmpNodes.id.substring(4);
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
<!-- <fieldset><legend>应用管理-应用下发</legend> -->
<div class="x-title"><span>&nbsp;&nbsp;应用管理-应用下发</span></div>
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
						<th style="width:2%"><input type=checkbox
							name="checkAll" id="checkAll" /></th>
						<th style="width:20%">应用名</th>
						<th style="width:40%">创建时间</th>
						<th style="width:30%">说明</th>
					</tr>
					<%
						if(resultData!=null&&resultData.length>=1){
							for(int i=0;i<resultData.length;i++){
								HashMap tmp = resultData[i];
								out.print("<tr>");
								out.print("<td class='row' align='center'><input type=checkbox name=list value='"+tmp.get("APP_ID")+"' /></td>");
								out.print("<td class='row'>"+tmp.get("APP_NAME")+"</td>");
								out.print("<td class='row'>"+tmp.get("OPERATETIME")+"</td>");
								out.print("<td class='row'>"+tmp.get("DESCRIPTION")+"</td>");
								out.print("</tr>");
							}
						}
					%>
					</tr>
				</table>
		</div>
		<div>
			<div style="float: left">下发时间：
				<input type="text" size="8" name="valueTime" id="valueTime" maxlength="10" style="ime-mode:disabled" 
								value="" onclick="getDateString(this,oCalendarChs)">年月日
					<select name="valueTime_hh" id="valueTime_hh">
						<%
						for(int i=0;i<24;i++){
							out.print("<option value='"+i+"'>"+i+"</option>\n");
						}
						%>
					</select>时
					<select name="valueTime_mi" id="valueTime_mi">
						<%
						for(int i=0;i<60;i++){
							out.print("<option value='"+i+"'>"+i+"</option>\n");
						}
						%>
					</select>分
					&nbsp;&nbsp;
					<span id='ostypeAjaxSel'>下发端机类型 <select name="osType" id="osTypeId" onChange="initTreeByOS()" >
					<option value=''>--全选--</option>");
					<%
						if(osTypes!=null&&osTypes.length>=1){
							for(int i=0;i<osTypes.length;i++){
								out.print("<option value='" + osTypes[i].get("OS") + "'>" + osTypes[i].get("OS") + "</option>");
							}
						}
					%>
					</select></span>
				</div>
				<div style="float: right">
					<input class="tableBtn" id="sendButton" type="button" value="下发" />
					<input class="tableBtn" id="chooseUpdatePackagesButton" type="button" value="添加" />
<!-- 			<input type="button" value="本地选取" /> -->
				</div>
		</div>
	</div>
</div>
<!-- </fieldset> -->
</form>	

</body>

</html>