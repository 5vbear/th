<%@page import="th.com.util.Define"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%> 
<%
String strContextPath = request.getContextPath();
String url = strContextPath + "/AdvertServlet";
String defaultStyle = strContextPath + "/css/advert.css";
//素材类型
String advert_type = (String)request.getAttribute("advert_type");
String result = (String)request.getAttribute("result");
//端机类型
HashMap[] osTypes = (HashMap[])request.getAttribute( "osTypes" );
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./js/jquery-1.6.1.js"></script>
<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="<%=strContextPath%>/js/Calendar.js"></script>
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<title>Insert title here</title>
<script type="text/javascript"><!--
var oCalendarChs=new PopupCalendar("oCalendarChs"); //初始化控件时,请给出实例名称:oCalendarChs
oCalendarChs.weekDaySting=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
oCalendarChs.monthSting=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
oCalendarChs.oBtnTodayTitle="今天";
oCalendarChs.oBtnCancelTitle="取消";
oCalendarChs.Init();


//页面初始化
function onload() {
	var myDate = new Date();
	var year = myDate.getFullYear();
	var month = myDate.getMonth()+ 1;
	if(month<10){
		month = '0' + month;
	}
	var date = myDate.getDate();
	if(date<10){
		date = '0' + date;
	}
	document.getElementById("sendTime").value=year+"-"+month+"-"+date;
	document.getElementById("sendTime2").value=year+"-"+month+"-"+date;
	document.getElementById("valueTime").value=(year+1)+"-"+month+"-"+date;
	document.getElementById("valueTime2").value=(year+1)+"-"+month+"-"+date;
	
	var message = "<%=result%>";
	if (message == null || message == "" || message == "null") {
		return;
	}
	alert(message);
}

function OpenWindow(type){
	var paramers="dialogWidth:400px;DialogHeight:360px;status:no;help:no;unadorned:no;resizable:no;status:no";  
	var url;
	if(type==1){
		url = "<%=url %>" + "?pageId=jsp_sub_window_id&funcId=func_programlistgroup_subwindow_id";
		var ret=window.showModalDialog(url,'',paramers);  
		if (ret != null) {
			document.getElementById("txt_programlist").value=ret.name;
			if(ret.type == "1"){
				document.getElementById("programlistGroupId").value=ret.id;
				document.getElementById("billId").value="";
			}else{
				document.getElementById("billId").value=ret.id;
				document.getElementById("programlistGroupId").value="";
			}
		}
		
	}else{
		url = "<%=url %>" + "?pageId=jsp_sub_window_id&funcId=func_subtitles_subwindow_id";
		var ret2=window.showModalDialog(url,'',paramers); 
		if (ret2 != null) {
			document.getElementById("txt_subtitles").value=ret2.subtitlesName;
			document.getElementById("subtitlesId").value=ret2.subtitlesId;
		}
	}
}

var cur_id="";
var flag=0,sflag=0;
//组织结构下拉菜单
	var setting = {
		view: {
			fontCss: setFontCss,
			selectedMulti: false
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId",
				rootPId : "org_0"
			}
		},		
		callback: {
			onClick: zTreeOnClick
		}

	};
 
var zTreeNodes;
var zTree;
$(function() {
	zTreeNodes = <%=(String)request.getAttribute( "machinList" )%> ;
}); 
var zTreeObj;
$(document).ready(function(){  
	intiTree();
	//zTreeObj = $.fn.zTree.init($("#menu"), setting, zTreeNodes);  
	
});
function setFontCss(treeId, treeNode) {
	
	return treeNode.checked == false ? {color:"black"} : {};
};

function zTreeOnClick(event, treeId, treeNode) {
    /* alert(treeNode.tId + ", " + treeNode.name); */
    if(treeNode.mactype=='mac'){
    	window.document.form1.sel_nodes_info.value = treeNode.id;
    	window.document.form1.sel_nodes_name.value = treeNode.name;
    } else{
        alert("请选择端机");
    }
    
};

function intiTree(){
	zTreeObj = $.fn.zTree.init($("#menu"), setting, zTreeNodes);
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

function delaySend() {
	window.showModalDialog("/th/jsp/updateManagement/updateSending.jsp",window,"dialogWidth:300px;dialogHeight:150px;scroll:no;status:no;location:no;");
};
//节目单下发
function send(){
	//端机
	if(window.document.form1.sel_nodes_info.value == "") {
		alert("请先选择端机，然后再进行下发操作！");
		return;
	}
	var advertType = document.getElementById("advert_type").value;
	if(advertType == "1"){
		if(document.getElementById("txt_programlist").value == ""){
			alert("请选择节目单，然后再进行下发操作！");
			return;
		}
	}else{
		if(document.getElementById("txt_subtitles").value == ""){
			alert("请选择字幕，然后再进行下发操作！");
			return;
		}
	}
	//下发理由
	if(document.getElementById("sendReason").value == ""){
		alert("下发理由不能为空！");
		return;
	}
	//临时广告
	if (window.document.form1.tempAds_chk.checked) {
		window.document.form1.tempAds.value="1";
	} else {
		window.document.form1.tempAds.value="0";
	}
	//立刻下发
	if (window.document.form1.sendType_chk.checked) {
		window.document.form1.sendType.value="1";
	} else {
		window.document.form1.sendType.value="0";
	}
	//下载控制
	var download=document.getElementsByName("download");
	for(var i=0;i<download.length;i++){
		if(download[i].checked) {
			document.getElementById("controlType").value=i+1;
		}
	}
	/*var zNodes = zTreeObj.getCheckedNodes(true);
	var v = "";
	if(zNodes!=null && zNodes.length>0){
		for (var i=0; i<zNodes.length; i++) {
			if(zNodes[i].mactype=='mac'){
			//if(!zNodes[i].isParent){
				v += zNodes[i].id;
				if(i<zNodes.length-1){
					v += ",";
				}
			}
		}
	}else {
		alert("请先在当前权限树中点选节点，然后再进行下发操作！");
		return;
	}*/
	
	/*//端机类型
	var machingType = "";
	var osType = document.getElementsByName("osType");
	for(var i=0; i<osType.length; i++){
		if(osType[i].checked){
			if(machingType == ""){
				machingType = osType[i].value;
			}else{
				machingType += "," + osType[i].value;
			}
		}
	}
	if(machingType == ""){
		alert("至少保留一种端机类型！");
		return;
	}
	document.getElementById("machingType").value = machingType;*/
	//window.document.form1.sel_nodes_info.value = v;
	window.document.form1.pageId.value = "<%=Define.JSP_PROGRAMLIST_SEND_ID%>";
	window.document.form1.funcId.value = "<%=Define.FUNC_PROGRAM_SEND_ID%>";
	window.document.form1.type.value = "123";
	window.document.form1.submit();
}
function advertTypeChange(obj) {
	var type = obj.value;
	var div1 = document.getElementById("div1");
	var div2 = document.getElementById("div2");
	if (type=="1") {
		div1.style.display = "block";
		div2.style.display = "none";
	} else {
		div1.style.display = "none";
		div2.style.display = "block";
	}
}

//清除端机历史节目单列表
function cleanProgramlist() {
	var ret = confirm("是否清除该端机历史记录？")
	if(!ret){
		return;
	}
	xmlHttp = this.getXmlHttpRequest();
	if(xmlHttp == null) {
	    alert("Explore is Unsupport XmlHttpRequest！");
	    return;
	}
	/*var zNodes = zTreeObj.getCheckedNodes(true);
	var v = "";
	if(zNodes!=null && zNodes.length>0){
		for (var i=0; i<zNodes.length; i++) {
			if(zNodes[i].mactype=='mac'){
			//if(!zNodes[i].isParent){
				v += zNodes[i].id;
				if(i<zNodes.length-1){
					v += ",";
				}
			}
		}
	}else {
		alert("请先在当前权限树中点选节点，然后再进行下发操作！");
		return;
	}*/
	//组织
	if(window.document.form1.sel_nodes_info.value == ""){
		alert("请先在当前权限树中点选节点，然后再进行下发操作！");
		return;
	}
	var v = window.document.form1.sel_nodes_info.value;
	var advertType = document.getElementById("advert_type").value;
	var url = "<%= url %>"+"?pageId=jsp_programlist_send_id&funcId=func_programlist_clean_id&advert_type="+advertType+"&sel_nodes_info="+v+"&type=123";
	xmlHttp.open("GET", url, true);
	xmlHttp.onreadystatechange = this.callBack;
	xmlHttp.send(null);
}

function getXmlHttpRequest() {
    try {
        // Firefox, Opera 8.0+, Safari
        xmlHttp = new XMLHttpRequest();
    } catch (e) {
        // Internet Explorer
        try {
            xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
                alert("XMLHTTP is null");
                return false;
            }
        }
    }

    return  xmlHttp; 
}






function callBack() {
    if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
        var res = xmlHttp.responseText;
        if(res != ""){
       		alert(res);
        } else {
        	alert("该端机历史记录清除失败");
        }
    }
}



--></script>
</head>
<body onload="onload();">
<form id="form1" name="form1" method="post" action="<%=url %>">
<div class="search_title"><span>端机指令-广告下发</span></div>
<div id="body" style="height:580; width:250; float: left;overflow:scroll;overflow-x:hidden"  class="sdmenu" > 
    		<ul id="menu" class="ztree" style="margin-top:0; width:200px;"></ul>
		</div>
	<table>
		<tr>
			<td>发布类型：</td>
			<td colspan="2"><select id="advert_type" name="advert_type" onchange="advertTypeChange(this)">
				<option value="1" 
					
				>节目单</option>
				<option value="2"
					
				>字幕</option>
				</select>
			</td>
	</table>
	<div id="div1">
	<table>
		<tr>
			<td>选择节目单：</td>
			<td>
				<input type="text" name="txt_programlist" id="txt_programlist" />
				<input type="button" name="btn_ShowClose" id="btn_ShowClose" value="选择节目单" onclick="OpenWindow(1);" />
				<input type="button" name="btn_ShowClose" id="btn_ShowClose" value="清除节目单列表" onclick="cleanProgramlist();" />
			</td>		
		</tr>
		<tr>
			<td>播放屏幕：</td>
			<td>
			<select name="screenType">
				<option value="1">主屏</option>
			</select>
			</td>
		</tr>
		<tr>
			<td>临时广告：</td>
			<td>
				<input type="hidden" name="tempAds" value="">
				<input type="checkbox" name="tempAds_chk"/>
			</td>
		</tr>
		<tr>
			<td>立刻下发：</td>
			<td>
				<input type="hidden" name="sendType" value="">
				<input type="checkbox" name="sendType_chk" checked="checked"/>
			</td>
		</tr>
		<tr>
			<td>下发时间：</td>
			<td><input type="text" size="8" id="sendTime" name="sendTime" maxlength="10" style="ime-mode:disabled" 
							value="" onclick="getDateString(this,oCalendarChs)">
				<select name="sendTime_hh">
					<option value="00">00</option>
					<option value="01">01</option>
					<option value="02">02</option>
					<option value="03">03</option>
					<option value="04">04</option>
					<option value="05">05</option>
					<option value="06">06</option>
					<option value="07">07</option>
					<option value="08">08</option>
					<option value="09">09</option>
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
				</select>
				<select name="sendTime_mi">
					<option value="00">00</option>
					<option value="10">10</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="40">40</option>
					<option value="50">50</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>结束播放时间：</td>
			<td><input type="text" size="8" id="valueTime" name="valueTime" maxlength="10" style="ime-mode:disabled" 
							value="" onclick="getDateString(this,oCalendarChs)">
				<select name="valueTime_hh">
					<option value="00">00</option>
					<option value="01">01</option>
					<option value="02">02</option>
					<option value="03">03</option>
					<option value="04">04</option>
					<option value="05">05</option>
					<option value="06">06</option>
					<option value="07">07</option>
					<option value="08">08</option>
					<option value="09">09</option>
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
				</select>
				<select name="valueTime_mi">
					<option value="00">00</option>
					<option value="10">10</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="40">40</option>
					<option value="50">50</option>
				</select>
			</td>
		</tr>
  		<tr>
  			<td>下载控制：</td>
  			<td>
  				<input type="hidden"" id="controlType" name="controlType" value="1"/>
  				<input type="radio" name="download"/>下载完成立刻播放
  				<input type="radio" name="download"/>下载完成重启播放
  				<input type="radio" name="download"/>下载完成关闭端机
  			</td>
  		</tr>
	</table>
	</div>
	<div id="div2" style="display: none;">
		<table>	
			<tr>
				<td>选择字幕：</td>
				<td>
					<input type="text" name="txt_subtitles" id="txt_subtitles" />
					<input type="button" name="btn_ShowClose" id="btn_ShowClose" value="选择字幕" onclick="OpenWindow(2);" />		
					<input type="button" name="btn_ShowClose" id="btn_ShowClose" value="清除字幕列表" onclick="cleanProgramlist();" />
				</td>		
			</tr>
			<tr>
			<td>下发时间：</td>
			<td><input type="text" size="8" id="sendTime2" name="sendTime2" maxlength="10" style="ime-mode:disabled" 
							value="" onclick="getDateString(this,oCalendarChs)">
				<select name="sendTime2_hh">
					<option value="01">00</option>
					<option value="01">01</option>
					<option value="02">02</option>
					<option value="03">03</option>
					<option value="04">04</option>
					<option value="05">05</option>
					<option value="06">06</option>
					<option value="07">07</option>
					<option value="08">08</option>
					<option value="09">09</option>
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
				</select>
				<select name="sendTime2_mi">
					<option value="00">00</option>
					<option value="10">10</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="40">40</option>
					<option value="50">50</option>
				</select>
			</td>
		</tr>
			<tr>
				<td>结束播放时间：</td>
				<td><input type="text" size="8" id="valueTime2" name="valueTime2" maxlength="10" style="ime-mode:disabled" 
								value="" onclick="getDateString(this,oCalendarChs)">
					<select name="valueTime2_hh">
						<option value="00">00</option>
						<option value="01">01</option>
						<option value="02">02</option>
						<option value="03">03</option>
						<option value="04">04</option>
						<option value="05">05</option>
						<option value="06">06</option>
						<option value="07">07</option>
						<option value="08">08</option>
						<option value="09">09</option>
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
					</select>
					<select name="valueTime2_mi">
						<option value="00">00</option>
						<option value="10">10</option>
						<option value="20">20</option>
						<option value="30">30</option>
						<option value="40">40</option>
						<option value="50">50</option>
					</select>
				</td>
			</tr>
			
		</table>
	</div>
	<table>
		<tr>
  			<td valign="top"><font color="red">*</font>下发理由：</td>
  			<td><textarea id="sendReason" rows="3" cols="40"></textarea></td>
  		</tr>
		<tr>
  			<td><input type="button" value="下发" onclick="delaySend();"/></td>
  		</tr>
	</table>
	<input type="hidden" name="programlistGroupId" id="programlistGroupId" />
	<input type="hidden" name="billId" id="billId" />
	<input type="hidden" name="subtitlesId" id="subtitlesId" />
	<input type="hidden" name="pageId" value="">
	<input type="hidden" name="funcId" value="">
	<input type="hidden" name="sel_nodes_info" value=""/>
	<input type="hidden" name="sel_nodes_name" value=""/>
	<input type="hidden" name="type" value=""/>
</form>
</body>
</html>