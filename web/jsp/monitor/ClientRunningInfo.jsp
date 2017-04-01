<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="th.com.property.LocalProperties" %>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "ClientRunningInfo.jsp";
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
    String macType = (String) request.getAttribute( "macType" );
    macType = th.util.StringUtils.isBlank( macType ) ? "0" : macType;
    HashMap[] MTmap =  (HashMap[])request.getAttribute( "MTmap" );
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<meta http-equiv="content-script-type" content="text/javascript">
<meta http-equiv="content-style-type" content="text/css">
<meta name="copyright" content="Copyright(c) 天和. All Rights Reserved.">
<meta name="keywords" content="ClientRunning">
<meta name="description" content="端机运行监控">
<title>端机运行监控</title>
<link href="./css/monitor.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$('#remoteControl').click(function(){
		var idName="remoteMenu";
		var mid = document.getElementById(idName).value
		$('#machineID').val(mid);
		window.parent.forwardRemote(mid);
	});
});
</script>
 <script language="JavaScript">
 var leftClickFlag = false;
 var rightStatus = "";
 function openRightMenu(id, name){
leftClickFlag=false;

document.getElementById("refreshMenu").style.display="";
document.getElementById("macMenu").style.display="";
document.getElementById("deployMenu").style.display="";
document.getElementById("useMenu").style.display="";
document.getElementById("stopMenu").style.display="";
document.getElementById("restartMenu").style.display="";
document.getElementById("shutDownMenu").style.display="";
document.getElementById("remoteMenu").style.display="";
document.getElementById("clearDataMenu").style.display="";
document.getElementById("startScreenShotMenu").style.display="";
document.getElementById("endScreenShotMenu").style.display="";
document.getElementById("locateMenu").style.display="";
document.getElementById("startAdvMenu").style.display="";
document.getElementById("endAdvMenu").style.display="";
document.getElementById("startTempAdvMenu").style.display="";
document.getElementById("endTempAdvMenu").style.display="";
document.getElementById("lockMenu").style.display="";
document.getElementById("unlockMenu").style.display="";
document.getElementById("sendMessage").style.display="";
document.getElementById("retirement").style.display="";

xmlHttp = this.getXmlHttpRequest();
if(xmlHttp == null) {
    alert("Explore is Unsupport XmlHttpRequest！");
    return;
}

var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + id + "&funcId=6";
xmlHttp.open("GET", url, false);
xmlHttp.onreadystatechange = this.statusCallBack;
xmlHttp.send(null);

if(rightStatus != ""){
	//alert("rightStatus = "+rightStatus);
	var statusArray = rightStatus.split(",");
	var enableStatus = statusArray[0];
	var startScreenshotStatus = statusArray[1];
	var startPlayStatus = statusArray[2];
	var startPlayTempStatus = statusArray[3];
	var unlockStatus = statusArray[4];
	//启用 报停菜单
	if(enableStatus=="1"){
		//显示启用
		document.getElementById("stopMenu").style.display="none";
	} else if(enableStatus=="0"){
		//显示报停
		document.getElementById("useMenu").style.display="none";
	} else {
		//均不显示
		document.getElementById("stopMenu").style.display="none";
		document.getElementById("useMenu").style.display="none";
	}

	//启动截屏 停止截屏菜单
	if(startScreenshotStatus=="1"){
		//显示停止截屏
		document.getElementById("startScreenShotMenu").style.display="none";
	} else if(startScreenshotStatus=="0"){
		//显示启动截屏
		document.getElementById("endScreenShotMenu").style.display="none";
	} else {
		//均不显示
		document.getElementById("startScreenShotMenu").style.display="none";
		document.getElementById("endScreenShotMenu").style.display="none";
	}
	
	//开始播放广告 停止播放广告
	if(startPlayStatus=="1"){
		//显示停止播放广告
		document.getElementById("startAdvMenu").style.display="none";
	} else if(startPlayStatus=="0"){
		//显示开始播放广告
		document.getElementById("endAdvMenu").style.display="none";
	} else {
		//均不显示
		document.getElementById("startAdvMenu").style.display="none";
		document.getElementById("endAdvMenu").style.display="none";
	}
	
	//启动临时广告 停止临时广告
	if(startPlayTempStatus=="1"){
		//显示停止临时广告
		document.getElementById("startTempAdvMenu").style.display="none";
	} else if(startPlayTempStatus=="0"){
		//显示启动临时广告
		document.getElementById("endTempAdvMenu").style.display="none";
	} else {
		//均不显示
		document.getElementById("startTempAdvMenu").style.display="none";
		document.getElementById("endTempAdvMenu").style.display="none";
	}
	
	//锁屏 解锁
	if(unlockStatus=="1"){
		//显示解锁
		document.getElementById("lockMenu").style.display="none";
	} else if(unlockStatus=="0"){
		//显示锁屏
		document.getElementById("unlockMenu").style.display="none";
	} else {
		//均不显示
		document.getElementById("lockMenu").style.display="none";
		document.getElementById("unlockMenu").style.display="none";
	}
}
if(name == null || name == "" || name.indexOf("Windows") != -1){
	//windows系统不显示的功能
	document.getElementById("deployMenu").style.display="none";
	document.getElementById("useMenu").style.display="none";
	document.getElementById("stopMenu").style.display="none";
	//document.getElementById("clearDataMenu").style.display="none";
	document.getElementById("locateMenu").style.display="none";
	document.getElementById("lockMenu").style.display="none";
	document.getElementById("unlockMenu").style.display="none";
	document.getElementById("sendMessage").style.display="none";
	document.getElementById("retirement").style.display="none";
}else if(name.indexOf("IOS") != -1){
	//ios系统不显示的功能
	document.getElementById("deployMenu").style.display="none";
	document.getElementById("useMenu").style.display="none";
	document.getElementById("stopMenu").style.display="none";
	document.getElementById("restartMenu").style.display="none";
	document.getElementById("shutDownMenu").style.display="none";
	document.getElementById("clearDataMenu").style.display="none";
	document.getElementById("startScreenShotMenu").style.display="none";
	document.getElementById("endScreenShotMenu").style.display="none";
	document.getElementById("locateMenu").style.display="none";
	document.getElementById("remoteMenu").style.display="none";
}else if(name.indexOf("Android") != -1){
	//andriod系统不显示的功能
	document.getElementById("deployMenu").style.display="none";
	document.getElementById("restartMenu").style.display="none";
	document.getElementById("shutDownMenu").style.display="none";
	document.getElementById("remoteMenu").style.display="none";
}

document.getElementById("refreshMenu").value=id;
document.getElementById("macMenu").value=id;
document.getElementById("deployMenu").value=id;
document.getElementById("useMenu").value=id;
document.getElementById("stopMenu").value=id;
document.getElementById("restartMenu").value=id;
document.getElementById("shutDownMenu").value=id;
document.getElementById("remoteMenu").value=id;
document.getElementById("clearDataMenu").value=id;
document.getElementById("startScreenShotMenu").value=id;
document.getElementById("endScreenShotMenu").value=id;
document.getElementById("locateMenu").value=id;
document.getElementById("startAdvMenu").value=id;
document.getElementById("endAdvMenu").value=id;
document.getElementById("startTempAdvMenu").value=id;
document.getElementById("endTempAdvMenu").value=id;
document.getElementById("lockMenu").value=id;
document.getElementById("unlockMenu").value=id;
document.getElementById("sendMessage").value=id;
document.getElementById("retirement").value=id;
closeRightMenu();
 //选择菜单方块的显示样式
ie5menu.className = menuskin;
//重定向鼠标右键事件的处理过程为自定义程序showmenuie5
document.oncontextmenu = showmenuie5;
//重定向鼠标左键事件的处理过程为自定义程序hidemenuie5
document.body.onclick = hidemenuie5;
}
  function closeRightMenu(){
document.oncontextmenu=function(e){return false;} 
 }

  function statusCallBack() {
	    if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
	    	rightStatus=xmlHttp.responseText;
	    }
	}
 
 
 //定义菜单显示的外观，可以从上面定义的2种格式中选择其一
var menuskin = "skin1";
//是否在浏览器窗口的状态行中显示菜单项目条对应的链接字符串
var display_url = 0;
function showmenuie5() {
	closeRightMenu();
if(leftClickFlag==true){
 return;
}

//获取当前鼠标右键按下后的位置，据此定义菜单显示的位置
var rightedge = document.body.clientWidth-event.clientX;
var bottomedge = document.body.clientHeight-event.clientY;
//如果从鼠标位置到窗口右边的空间小于菜单的宽度，就定位菜单的左坐标（Left）为当前鼠标位置向左一个菜单宽度
if (rightedge <ie5menu.offsetWidth)
ie5menu.style.left = document.body.scrollLeft + event.clientX - ie5menu.offsetWidth + 'px';
else
//否则，就定位菜单的左坐标为当前鼠标位置
ie5menu.style.left = document.body.scrollLeft + event.clientX + 'px';
//如果从鼠标位置到窗口下边的空间小于菜单的高度，就定位菜单的上坐标（Top）为当前鼠标位置向上一个菜单高度
if (bottomedge <ie5menu.offsetHeight)
ie5menu.style.top = document.body.scrollTop + event.clientY - ie5menu.offsetHeight + 'px';
else
//否则，就定位菜单的上坐标为当前鼠标位置
ie5menu.style.top = document.body.scrollTop + event.clientY + 'px';
//设置菜单可见
ie5menu.style.visibility = "visible";
return false;
}
function hidemenuie5() {
//隐藏菜单
//很简单，设置visibility为hidden就OK！
ie5menu.style.visibility = "hidden";
}
function highlightie5() {
//高亮度鼠标经过的菜单条项目
//如果鼠标经过的对象是menuitems，就重新设置背景色与前景色
//event.srcElement.className表示事件来自对象的名称，必须首先判断这个值，这很重要！
if (event.srcElement.className == "menuitems") {
//event.srcElement.style.backgroundColor = "highlight";
event.srcElement.style.color = "white";
//将链接信息显示到状态行
//event.srcElement.url表示事件来自对象表示的链接URL
if (display_url)
window.status = event.srcElement.url;
}
}
function lowlightie5() {
//恢复菜单条项目的正常显示
if (event.srcElement.className == "menuitems") {
event.srcElement.style.backgroundColor = "";
event.srcElement.style.color = "black";
window.status = "";
}
}
//右键下拉菜单功能跳转
function jumptoie5() {
//转到新的链接位置
var seltext=window.document.selection.createRange().text
if (event.srcElement.className == "menuitems") {
//如果存在打开链接的目标窗口，就在那个窗口中打开链接
if (event.srcElement.getAttribute("target") != null){
	window.open(event.srcElement.url, event.srcElement.getAttribute("target"));
} else {
	return;
//否则，在当前窗口打开链接
//window.location = event.srcElement.url;
  }
}
}

function rightOperation(ptype){
leftClickFlag=false;
var idName = "";
//刷新-1
if("<%= Define.MONITOR_RUNNING_REFREASH %>"==ptype){
	idName="refreshMenu";
}
//设备信息-2
else if ("<%= Define.MONITOR_RUNNING_MACHINEINFO %>"==ptype){
	idName="macMenu";
}
//部署信息-3
else if ("<%= Define.MONITOR_RUNNING_DEPLOYINFO %>"==ptype){
	idName="deployMenu";
}
//启用-4
else if ("<%= Define.MONITOR_RUNNING_INUSE %>"==ptype){
	idName="useMenu";
}
//报停\锁定-5
else if ("<%= Define.MONITOR_RUNNING_STOP %>"==ptype){
	idName="stopMenu";
}
//重启-6
else if ("<%= Define.MONITOR_RUNNING_RESTART %>"==ptype){
	idName="restartMenu";
}
//关机-7
else if ("<%= Define.MONITOR_RUNNING_SHUTDOWN %>"==ptype){
	idName="shutDownMenu";
}
//远程-9
else if ("<%= Define.MONITOR_RUNNING_REMOUTE %>"==ptype){
	idName="remoteMenu";
}
//清除数据-8
else if ("<%= Define.MONITOR_CLEAR_DATA %>"==ptype){
	idName="clearDataMenu";
}
//启动截屏-10
else if ("<%= Define.MONITOR_START_SCREEN_SHOT %>"==ptype){
	idName="startScreenShotMenu";
}
//停止截屏-11
else if ("<%= Define.MONITOR_STOP_SCREEN_SHOT %>"==ptype){
	idName="endScreenShotMenu";
}
//定位-12
else if ("<%= Define.MONITOR_LOCATE %>"==ptype){
	idName="locateMenu";
}
//开始广告播放-13
else if ("<%= Define.MONITOR_START_ADV %>"==ptype){
	idName="startAdvMenu";
}
//停止广告播放-14
else if ("<%= Define.MONITOR_END_ADV %>"==ptype){
	idName="endAdvMenu";
}
//开始播放临时广告-15
else if ("<%= Define.MONITOR_START_TEMP_ADV %>"==ptype){
	idName="startTempAdvMenu";
}
//停止播放临时广告-16
else if ("<%= Define.MONITOR_END_TEMP_ADV %>"==ptype){
	idName="endTempAdvMenu";
}
//锁定-17
else if ("<%= Define.MONITOR_LOCK %>"==ptype){
	idName="lockMenu";
}
//解锁-18
else if ("<%= Define.MONITOR_UNLOCK %>"==ptype){
	idName="unlockMenu";
}
//发送消息-19
else if ("<%= Define.MONITOR_SEND_MESSAGE %>"==ptype){
	idName="sendMessage";
}
//报废-20
else if ("<%= Define.MONITOR_RETIREMENT %>"==ptype){
	idName="retirement";
}
var mid = document.getElementById(idName).value

xmlHttp = this.getXmlHttpRequest();
if(xmlHttp == null) {
    alert("Explore is Unsupport XmlHttpRequest！");
    return;
}
if("<%= Define.MONITOR_RUNNING_REMOUTE %>"==ptype){
	var paramers="dialogWidth:400px;DialogHeight:250px;status:no;help:no;unadorned:no;resizable:no;status:no;";  
	var url = "/th/jsp/machine/macCommandContent.jsp";
	var ret=window.showModalDialog(url,'',paramers);  
	if (ret == null || ret == "") {
		return;
	}	
	var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + mid + "&rightType=" + ptype + "&funcId=1&content=" + ret;
} else if("<%= Define.MONITOR_SEND_MESSAGE %>"==ptype){
	var paramers="dialogWidth:400px;DialogHeight:250px;status:no;help:no;unadorned:no;resizable:no;status:no;";  
	var url = "/th/jsp/machine/macCommandMessage.jsp";
	var ret=window.showModalDialog(url,'',paramers);  
	if (ret == null || ret == "") {
		return;
	}
	var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + mid + "&rightType=" + ptype + "&funcId=1&content=" + ret;	
}else{
	var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + mid + "&rightType=" + ptype + "&funcId=1";
}
url = encodeURI(url);
xmlHttp.open("GET", url, true);
xmlHttp.onreadystatechange = this.callBack;
xmlHttp.send(null);

closeRightMenu();
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
    	alert(xmlHttp.responseText);
    }
}
//按广告播放中排序 0=服务中  ; 1=报停  ; 2=广告中  ; 4=线路中断
function refreshByOrder(type){
	window.document.form_data.orderType.value =type;
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_RUNNING_ID %>";
	window.document.form_data.orgID.value ="<%= (String)request.getAttribute("orgID") %>";
    window.document.form_data.action = "<%= Define.MONITOR_SERVLET %>";
    window.document.form_data.target = "_self";
    window.document.form_data.submit();
}
//按端机类型筛选 全部  ; Microsoft Windows XP  ; Windows 7 Professional  ; Android
function refreshByMacType(value){
	window.document.form_data.orderType.value ="<%= (String)request.getAttribute("orderType") %>";
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_RUNNING_ID %>";
	window.document.form_data.orgID.value ="<%= (String)request.getAttribute("orgID") %>";
    window.document.form_data.action = "<%= Define.MONITOR_SERVLET %>";
    window.document.form_data.target = "_self";
    window.document.form_data.submit();
}

function refreshListByOrder(type){
	window.document.form_data.orderType.value =type;
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_RUNNING_LIST_ID %>";
	window.document.form_data.orgID.value ="<%= (String)request.getAttribute("orgID") %>";
    window.document.form_data.target = "_self";
    window.document.form_data.submit();
}

function showMacMap(){
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_RUNNING_MAP_ID %>";
	window.document.form_data.orgID.value ="<%= (String)request.getAttribute("orgID") %>";
    window.document.form_data.target = "_self";
    window.document.form_data.submit();
}
function selChange(obj){
	var index = obj.selectedIndex
	var text = obj.options[index].text;
	var val = obj.value;
	text = text.replace(/\|/g,"").replace(/\-/g,"");
	var val = obj.options[index]=new Option(text,val);
	obj.options[index].selected=true;
}
function myrefresh(){ 
	refreshByOrder('2');
}
var refreshTime = <%=LocalProperties.getProperty("MONITOR_RUNNING_REFRESH_TIME")%> + 0;
setTimeout('myrefresh()',refreshTime);

 </script>

</head>
<body oncontextmenu="self.event.returnValue=false" onselectstart="return false">
<form method="POST" name="form_data" action="/th/MonitorServlet.html">
<input type="hidden" name="orderType"   value="">
<input type="hidden" name="pageID"   value="">
<input type="hidden" name="orgID"   value="">
<input type="hidden" name="rightType"   value="">
<input type="hidden" name="machineID" id="machineID" >
<input type="hidden" name="deal"   value="remote_top">
<div   style="HEIGHT: 25px;margin-left:40px;">
<input name="ggbfzButton" type="submit" class="rb_gg1" value="锁屏\广告(<%=(Integer)request.getAttribute("ggNum")%>)" onmouseover="this.className='rb_gg2';" onmouseout="this.className='rb_gg1';" onclick="refreshByOrder('2');" />
&nbsp;
<input name="fwzButton" type="submit" class="rb_fwz1" value="服务中(<%=(Integer)request.getAttribute("fwzNum")%>)" onmouseover="this.className='rb_fwz2';" onmouseout="this.className='rb_fwz1';" onclick="refreshByOrder('0');"/>
&nbsp;
<input name="xlzdButton" type="submit" class="rb_xlzd1" value="离线(<%=(Integer)request.getAttribute("xlzdNum")%>)" onmouseover="this.className='rb_xlzd2';" onmouseout="this.className='rb_xlzd1';" onclick="refreshByOrder('4');"/>
&nbsp;
<input name="btButton" type="submit" class="rb_bt1" value="锁定\报停(<%=(Integer)request.getAttribute("btNum")%>)" onmouseover="this.className='rb_bt2';" onmouseout="this.className='rb_bt1';" onclick="refreshByOrder('1');"/>

<span style="margin-left: 20px">设备类型:</span> 
<select id="macType" name="macType" style="width: 180px;HEIGHT: 25px" onchange=refreshByMacType(this.value)>
	<option value="0" <%if("0".equals(macType)){ %>selected="selected"<%}%>>全部</option>
	<%if (null != MTmap && MTmap.length != 0){
		
		for (int i = 0; i < MTmap.length; i++) {
			String str = MTmap[i].get("OS").toString()+ "_"+ MTmap[i].get("MACHINE_KIND").toString();
			String macName = MTmap[i].get("TYPE_NAME").toString();
	%>
	<option value="<%=str %>" <%if(str.equals(macType)){ %>selected="selected"<%}%>><%=macName %></option>
	<%		
		}
	}%>
</select> 
</div>

 <BR>
<div id="ie5menu" class="skin0" onMouseover="highlightie5()" onMouseout="lowlightie5()" onClick="jumptoie5();">
<div id="refreshMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('1');return false" >刷新</a>
</div>
<div id="macMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('2');return false" >设备信息</a>
</div>
<div id="deployMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('3');return false" >部署信息</a>
</div>
<div id="useMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('4');return false" >启用</a>
</div>
<div id="stopMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('5');return false" >锁定\报停</a>
</div>
<div id="locateMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('12');return false" >定位</a>
</div>
<div id="clearDataMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('8');return false" >清除数据</a>
</div>
<div id="startScreenShotMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('10');return false" >启动截屏</a>
</div>
<div id="endScreenShotMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('11');return false" >停止截屏</a>
</div>
<div id="startAdvMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('13');return false" >开始广告播放</a>
</div>
<div id="endAdvMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('14');return false" >停止广告播放</a>
</div>
<div id="startTempAdvMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('15');return false" >开始播放临时广告</a>
</div>
<div id="endTempAdvMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('16');return false" >停止播放临时广告</a>
</div>
<div id="lockMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('17');return false" >锁屏</a>
</div>
<div id="unlockMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('18');return false" >解锁</a>
</div>
<div id="restartMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('6');return false" >重启</a>
</div>
<div id="shutDownMenu" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('7');return false" >关机</a>
</div>

<div id="remoteMenu" class="menuitems">
	<a href="" id="remoteControl" style="text-decoration: none;color: #000000;" target="" >远程接管</a>
</div>
<div id="sendMessage" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('19');return false" >发送消息</a>
</div>
<div id="retirement" class="menuitems">
	<a href="" style="text-decoration: none;color: #000000;" target="" onClick="javascript:rightOperation('20');return false" >报废</a>
</div>

</div>
 <div style="OVERFLOW-Y:auto;height:405px;margin-left:20px;">
 <table  id="orderList" class="table_roll" width="650px" height="400px" border="0" cellspacing="1" cellpadding="3" align="center" frame=void>
   <%=(String)request.getAttribute("MONITOR_RUNNING")%>
  </table>
  </div> 
  <br>
<div>
<input name="jzjk" type="button" class="rb_jzjk1" value="地图监控" onmouseover="this.className='rb_jzjk2';" onmouseout="this.className='rb_jzjk1';" onclick="showMacMap();"/>
&nbsp;
<input name="jzjk" type="button" class="rb_jzjk1" value="列表监控" onmouseover="this.className='rb_jzjk2';" onmouseout="this.className='rb_jzjk1';" onclick="refreshListByOrder('2');"/>
&nbsp;
<input name="jzjk" type="button" class="rb_jzjk1" value="矩阵监控" onmouseover="this.className='rb_jzjk2';" onmouseout="this.className='rb_jzjk1';" onclick="refreshByOrder('2');"/>
</div>
</form>
</body>
</html>
<% logger.info( jspName + " : end"); %>
