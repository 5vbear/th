<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.entity.MachineBean"%>
<%@ page import="th.com.property.LocalProperties" %>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "ClientRunningListInfo.jsp";
    logger.info( jspName + " : start" );
    
    User user = (User) session.getAttribute("user_info");
    String realname =null;
    if (user == null) {
	    response.setContentType("text/html; charset=utf-8");
	    response.sendRedirect("/th/index.jsp");
    } else {
	    realname = user.getReal_name();
	    logger.info("获得当前用户的用户名,用户名是: " + realname);
    }
    
    String strContextPath = request.getContextPath();
    String url = strContextPath + "/MonitorServlet.html";
    String defaultStyle = strContextPath + "/css/advert.css";
    
    //检索结果
    List resultBeans = (ArrayList)request.getAttribute("resultList");
    //检索条件
    MachineBean selectBean = (MachineBean)request.getAttribute("select_object");
    //检索结果总数
    int total_num = 0;
    //当前页数
    int point_num = 1;
    //Table总页数
    int page_total = 1;
    //每页显示行数
    int page_view = Define.VIEW_NUM;
    
    if (selectBean != null) {
    	//检索结果总数
    	total_num = selectBean.getTotal_num();
    	//当前页数
    	point_num = selectBean.getPoint_num();
    	//Table总页数
    	if (total_num != 0) {
    		if(total_num % page_view == 0){
    			page_total = total_num / page_view;
    		} else {
    			page_total = total_num / page_view + 1;
    		}
    	}
    	
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
<meta name="description" content="端机运行列表监控">
<title>端机运行列表监控</title>
<link href="./css/monitor.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
<link rel="stylesheet" type="text/css" href="./css/monitor.css">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
 <script language="JavaScript">
 var rightResult="";
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
	}
	
function search(){
	window.document.form_data.orderType.value ="2";
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_RUNNING_LIST_ID %>";
	window.document.form_data.orgID.value = document.getElementById("SelectOrg").value;
	window.document.form_data.action = "<%= Define.MONITOR_SERVLET %>";
	window.document.form_data.target = "_self";
	window.document.form_data.submit();
}

function remote(id){
	window.parent.parent.abcdeftList(id);
}

function rightOperation(obj,ptype){
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
	var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + obj.id + "&rightType=" + ptype + "&funcId=1&content=" + ret;	
}else{
	var url = "<%= Define.MONITOR_RIGHT_SETTTING_SERVLET %>?machineID=" + obj.id + "&rightType=" + ptype + "&funcId=1";
}

xmlHttp.open("GET", url, false);
xmlHttp.onreadystatechange = this.callBack;
xmlHttp.send(null);
if(rightResult != "操作失败"){
	if(ptype=="4"){
		obj.value = "锁定";
		obj.onclick=function(){rightOperation(obj,"5")};
	} else if(ptype=="5"){
		obj.value = "启用";
		obj.onclick=function(){rightOperation(obj,"4")};
	} else if(ptype=="10"){
		obj.value = "停止截屏";
		obj.onclick=function(){rightOperation(obj,"11")};
	} else if(ptype=="11"){
		obj.value = "启动截屏";
		obj.onclick=function(){rightOperation(obj,"10")};
	} else if(ptype=="13"){
		obj.value = "停止广告播放";
		obj.onclick=function(){rightOperation(obj,"14")};
	} else if(ptype=="14"){
		obj.value = "开始广告播放";
		obj.onclick=function(){rightOperation(obj,"13")};
	} else if(ptype=="15"){
		obj.value = "停止播放临时广告";
		obj.onclick=function(){rightOperation(obj,"16")};
	} else if(ptype=="16"){
		obj.value = "开始播放临时广告";
		obj.onclick=function(){rightOperation(obj,"15")};
	} else if(ptype=="17"){
		obj.value = "解锁";
		obj.onclick=function(){rightOperation(obj,"18")};
	} else if(ptype=="18"){
		obj.value = "锁屏";
		obj.onclick=function(){rightOperation(obj,"17")};
	}
  }
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
    	rightResult=xmlHttp.responseText;
    	alert(rightResult);
    }
}
//按广告播放中排序 0=服务中  ; 1=报停  ; 2=广告中  ; 4=线路中断
function refreshByOrder(type){
	window.document.form_data.orderType.value =type;
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_RUNNING_ID %>";
	window.document.form_data.orgID.value ="<%= (String)request.getAttribute("orgID") %>";
    window.document.form_data.target = "_self";
    window.document.form_data.submit();
}

//按端机类型筛选 全部  ; Microsoft Windows XP  ; Windows 7 Professional  ; Android
function refreshByMacType(value){
	window.document.form_data.orderType.value ="<%= (String)request.getAttribute("orderType") %>";
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_RUNNING_LIST_ID %>";
	window.document.form_data.orgID.value ="<%= (String)request.getAttribute("orgID") %>";
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

function pageTurn (page) {
	if (page <= 0 || page > <%=page_total%> || <%=total_num%> == 0) {
		return;
	}
	
	window.document.form_data.point_num.value = page;
	window.document.form_data.orgID.value ="<%= (String)request.getAttribute("orgID") %>";
	window.document.form_data.orderType.value ="<%= (String)request.getAttribute("orderType") %>";
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_RUNNING_LIST_ID %>";
    window.document.form_data.target = "_self";
    window.document.form_data.submit();
}

function myrefresh(){ 
	refreshListByOrder('2');
}
var refreshTime = <%=LocalProperties.getProperty("MONITOR_RUNNING_REFRESH_TIME")%> + 0;
setTimeout('myrefresh()',refreshTime);
 </script>

</head>
<body>
<form method="POST" name="form_data" action="/th/MonitorServlet.html">
<input type="hidden" name="orderType"   value="">
<input type="hidden" name="pageID"   value="">
<input type="hidden" name="orgID"   value="">
<input type="hidden" name="point_num" value="<%=point_num %>">
<div   style="HEIGHT: 25px;text-align:left;margin:0 auto;">
<input name="ggbfzButton" type="submit" class="rb_gg1" value="锁屏\广告(<%=(Integer)request.getAttribute("ggNum")%>)" onmouseover="this.className='rb_gg2';" onmouseout="this.className='rb_gg1';" onclick="refreshListByOrder('2');" />
&nbsp;
<input name="fwzButton" type="submit" class="rb_fwz1" value="服务中(<%=(Integer)request.getAttribute("fwzNum")%>)" onmouseover="this.className='rb_fwz2';" onmouseout="this.className='rb_fwz1';" onclick="refreshListByOrder('0');"/>
&nbsp;
<input name="xlzdButton" type="submit" class="rb_xlzd1" value="离线(<%=(Integer)request.getAttribute("xlzdNum")%>)" onmouseover="this.className='rb_xlzd2';" onmouseout="this.className='rb_xlzd1';" onclick="refreshListByOrder('4');"/>
&nbsp;
<input name="btButton" type="submit" class="rb_bt1" value="锁定\报停(<%=(Integer)request.getAttribute("btNum")%>)" onmouseover="this.className='rb_bt2';" onmouseout="this.className='rb_bt1';" onclick="refreshListByOrder('1');"/>
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
			<div>
				<table id="dataTableId">
					<tr>
						<th style="width: 10%" class="th_tianhe">端机名称</th>
						<th style="width: 30%" class="th_tianhe">操作</th>
					</tr>
					<% 
					if (resultBeans != null) {
						for (int i = 0; i < resultBeans.size(); i++) {
							String str = (String)resultBeans.get(i);
					%>		
					<tr>
						<%=str %>
					</tr>
					<%		
						}
					}
					%>
				</table>
			</div>
			<div>
						<input type="button" class="first_page" style="margin-left:5px" onclick="pageTurn(1)"/> 
						<input type="button" class="previous_page" style="margin-left:-2px" onclick="pageTurn(<%=point_num-1 %>)"/> 
						<input type="button" class="next_page" style="margin-left:-4px" onclick="pageTurn(<%=point_num+1 %>)"/>
						<input type="button" class="last_page" style="margin-left:-2px" onclick="pageTurn(<%=page_total %>)" />[当前第<%=point_num%>页/共<%=page_total%>页]
						<input type="button" class="rb_jzjk1" value="地图监控" onmouseover="this.className='rb_jzjk2';" onmouseout="this.className='rb_jzjk1';" onclick="showMacMap();"/>
						<input type="button" class="rb_jzjk1" value="列表监控" onmouseover="this.className='rb_jzjk2';" onmouseout="this.className='rb_jzjk1';" onclick="refreshListByOrder('2');"/>
						<input type="button" class="rb_jzjk1" value="矩阵监控" onmouseover="this.className='rb_jzjk2';" onmouseout="this.className='rb_jzjk1';" onclick="refreshByOrder('2');"/>
			</div>

</form>
</body>
</html>
<% logger.info( jspName + " : end"); %>
