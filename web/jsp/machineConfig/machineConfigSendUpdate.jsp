<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="th.util.StringUtils" %>	
<%@ page import="java.util.HashMap" %>
<%@ page import="th.com.util.Define" %>
 <%@ page import="th.action.report.ReportCommonAction"%>
<%
    String zNodes = (String)request.getAttribute( "zNodes" );
    String yyyyMMdd = (String)request.getAttribute( "yyyyMMdd" );
    String hour = (String)request.getAttribute( "hour" );
    String minute = (String)request.getAttribute( "minute" );
    HashMap[] resultData = (HashMap[])request.getAttribute("packagesArray");
    String checkedIds = (String)request.getSession().getAttribute("checkedIds");
    String osTypeId = (String)request.getSession().getAttribute("osType");
    String checkedAll = (String)request.getSession().getAttribute("checkedAll");
    String type = (String)request.getAttribute( "type" );
	String ctx = request.getContextPath();
	
	String pageNumber = (Integer) request.getAttribute("pageIdx") + "";
	String totalPageCount = "";
	int page_view = Define.VIEW_NUM;
	String total_num = "";
	String strContextPath = request.getContextPath();
	String defaultStyle = strContextPath + "/css/advert.css";
	int point_num = 1;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<link rel="stylesheet" type="text/css" href="../../css/monitor.css">
<link rel="stylesheet" type="text/css" href="../../css/sdmenu.css" />
<link rel="stylesheet" type="text/css" href="../../css/style.css" />
<link rel="stylesheet" type="text/css" href="../../css/menu.css" />

<link rel="stylesheet" href="../../zTree/css/demo.css" type="text/css"/>
<link rel="stylesheet" href="../../css/channel.css" type="text/css"/>
<link rel="stylesheet" href="../../css/machine.css" type="text/css"/>
<link rel="stylesheet" href="../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
<link rel="stylesheet" href="../../css/updateManagement.css" type="text/css"/>


<script type="text/javascript" src="../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="../../js/Calendar.js"></script>

<SCRIPT LANGUAGE="JavaScript" type="text/javascript">
<%if(type!=null&&"OK".equals(type)){%>

alert("下发成功！");
<%}%>
var alertInfo_PleaseChoosePackage='请选择需要下发的配置!';

/////////////////////////////////////////////////////////////////////////////
    var alertInfo_ChooseMacOrOrg='请选择下发的端机或者组织!';
    var alertInfo_ChooseHasNoAnyMac='您的选择不包含任何端机!';
    var alertInfo_WarnForNoUpdate='类型不匹配的端机将不能被升级!';

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
		intiTree();
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


function expandAllButton(){
	zTreeObj.expandAll(true);
	checkedAll = true;
}
function closeAllButton(){
	zTreeObj.expandAll(false);
	checkedAll = false;
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

///////////////////////////////////////////////////////////////////////


function send(){
	var arrayObj = new Array();
	var mac = document.getElementById('note').value;

	var checkValues = "";
	 var isCheck = false;
	 
	var selectNodes = zTreeObj.getCheckedNodes();
	var checkedIds = "";
	for(k=0;k<selectNodes.length;k++){
			if(!selectNodes[k].checked) continue;
			if(!selectNodes[k].mactype=='mac') continue;
			checkedIds = checkedIds+","+selectNodes[k].id;
	}
	 
	document.getElementById("checkedIds").value=checkedIds.substring(1);
	
	 $('[name="list"]').each(function(){
		   	if($(this).attr("checked")){
		   		isCheck = true;
		   		if(checkValues == ""){
		   			checkValues =$(this).val();
		   		}else{
			   		checkValues += "," + $(this).val() ;

		   		}
		 		}
		 	});
	 $('#checkedConfigs').attr("value",checkValues);

			for(var i = 0;i<checkValues.split(",").length;i++){
			var value =	checkValues.split(",")[i];
			
			var itemName = value.split("@@")[1];
			for(j=0;j<arrayObj.length;j++)  {
				if(arrayObj[j] == itemName)  {
					alert("每种项名称只可以选取一次");
					return;
				}
				
			}
			arrayObj.push(itemName);
			}
		

	if(!isCheck){
		alert(alertInfo_PleaseChoosePackage);
		return;
	}

	var form = document.getElementById('form1');
	form.action ="machineConfigSeach.html?pageId=<%=Define.JSP_MACHINE_SEND_ID%>&funcId=func_machine_send_id";
	form.submit();
}



function goFirst(){

	window.document.form1.pageIdx.value = 1;
	window.document.form1.pageId.value = "<%=Define.JSP_MACHINE_SEND_ID%>";
	window.document.form1.submit();
}

function goPrevious(curPageIdx){
	var pageIdx = 1;
	if(curPageIdx>1){
		pageIdx = curPageIdx - 1;
	}
	
	window.document.form1.pageIdx.value =pageIdx;
	window.document.form1.pageId.value = "<%=Define.JSP_MACHINE_SEND_ID%>";
	window.document.form1.submit();
}

function goNext(curPageIdx){

	var pageMaxNum = document.getElementById("pageNum").value;
	var pageIdx = pageMaxNum;
	if(curPageIdx < pageMaxNum){
		pageIdx = curPageIdx + 1;
	}
	window.document.form1.pageIdx.value =pageIdx;
	window.document.form1.pageId.value = "<%=Define.JSP_MACHINE_SEND_ID%>";
	window.document.form1.submit();

}

function goLast(){

	var pageMaxNum = document.getElementById("pageNum").value;
	window.document.form1.pageIdx.value =pageMaxNum;
	window.document.form1.pageId.value = "<%=Define.JSP_MACHINE_SEND_ID%>";
	window.document.form1.submit();
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


<form method="POST" name="form1" id = "form1" action="">
<input type="hidden" id="checkedIds" name="checkedIds"/>
<input type="hidden" id="note" name="note"/>
<input type="hidden" id="checkedConfigs" name="checkedConfigs"/>
<input type="hidden" name="point_num" value="<%=point_num %>"/>
<input type="hidden" name="pageIdx" value =""/>
<input type="hidden" id="pageNum" name="pageNum" value="<%=request.getAttribute("pageNum") %>" /> 
<input type="hidden" name="pageId" value=""/>
<input type="hidden" name="funcId" value=""/>

<!-- <fieldset><legend>升级管理-升级包下发</legend> -->
<div class="x-title"><span>&nbsp;&nbsp;端机配置-端机配置下发</span></div>
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
		<div style="overflow-y:auto;height:500px" >
		
				<table class="x-data-table" id="dataTableId" width="100%">
					<tr>
						<th style="width:2%"></th>
						<th style="width:20%">配置名</th>
						<th style="width:30%">配置描述</th>
						<th style="width:20%">项名称</th>
						<th style="width:30%">项值</th>
					</tr>
					<%
						if(resultData!=null&&resultData.length>=0){
							
							total_num=resultData.length+"";
							totalPageCount = ReportCommonAction.getTotalPageCount(resultData.length)+"";
							int pageIdx = Integer.parseInt(request.getAttribute("pageIdx")
									.toString());
							if (pageIdx < 1) {
								pageIdx = 1;
							}
							for(int i =0; i < resultData.length; i++){
								HashMap tmp = resultData[i];
								out.print("<tr>");
								out.print("<td class='row' align='center'><input type=checkbox name=list value='"+tmp.get("MODULE_ID")+"@@"+tmp.get("ITEMNAME")+"' /></td>");
								out.print("<td class='row'>"+tmp.get("MODULE_NAME")+"</td>");
								out.print("<td class='row'>"+tmp.get("DESCRIPTION")+"</td>");
								if("0".equals(tmp.get("ITEMNAME"))){
									out.print("<td class='row'>开机</td>");
									out.print("<td class='row'>"+String.valueOf(tmp.get("MACHINE_START_TIME")).substring(10,19)+"</td>");
									
								}else if("1".equals(tmp.get("ITEMNAME"))){
									out.print("<td class='row'>关机</td>");
									out.print("<td class='row'>"+String.valueOf(tmp.get("MACHINE_SHUTDOWN_TIME")).substring(10,19)+"</td>");
									
								}else if("2".equals(tmp.get("ITEMNAME"))){
									out.print("<td class='row'>屏保</td>");
									out.print("<td class='row'>"+tmp.get("SCREEN_PROTECT_TIME")+"</td>");
									
								}else if("3".equals(tmp.get("ITEMNAME"))){
									out.print("<td class='row'>截屏时间</td>");
									out.print("<td class='row'>"+tmp.get("SCREEN_COPY_DURATION")+"</td>");
									
								}else if("4".equals(tmp.get("ITEMNAME"))){
									out.print("<td class='row'>截屏间隔时间</td>");
									out.print("<td class='row'>"+tmp.get("SCREEN_COPY_INTERVAL")+"</td>");
									
								}else if("5".equals(tmp.get("ITEMNAME"))){
									out.print("<td class='row'>写保护目录</td>");
									out.print("<td class='row'>"+tmp.get("WRITE_PROTECT_DIRS")+"</td>");
									
								}else if("6".equals(tmp.get("ITEMNAME"))){
									out.print("<td class='row'>应用服务器地址</td>");
									out.print("<td class='row'>"+tmp.get("SERVER_URL")+"</td>");
									
								}else if("7".equals(tmp.get("ITEMNAME"))){
									out.print("<td class='row'>FTP服务器IP</td>");
									out.print("<td class='row'>"+tmp.get("FTP_SERVER_IP")+"</td>");
									
								}else if("8".equals(tmp.get("ITEMNAME"))){
									out.print("<td class='row'>心跳时间</td>");
									out.print("<td class='row'>"+tmp.get("COMMAND_TIME")+"</td>");
									
								}else{
									out.print("<td class='row'>-</td>");
									out.print("<td class='row'>默认值</td>");
								}					
								out.print("</tr>");
							}
						}
					%>
				</table>
		</div>



<input class="tableBtn" id="sendButton" class="rightBtn" type="button" style="float: right;"  onclick ="send()" value="下发" />

		</div>
	</div>
</form>	

</body>

</html>