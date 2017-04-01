<%@page import="th.util.StringUtils"%>
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
    String jspName = "ClientRunningMap.jsp";
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
    
    int level = 5;
    try{
    	if(!"".equals(StringUtils.transStr(request.getAttribute("zoomLevel").toString()))){
    		level = (Integer)request.getAttribute("zoomLevel");
    	}
    } catch(Exception e){
    	logger.info( e.getMessage() );
    }
    
    String lngCenter = "116.404";
    String latCenter = "39.915";
    String titleCenter = "";
    String mesCenter ="";
    String statusCenter = "";
    String iconPathCenter = "";
    List<MachineBean> beans = (ArrayList<MachineBean>)request.getAttribute("location");
    if(beans != null && beans.size() > 0){
    	lngCenter = beans.get(0).getLongitude();
        latCenter = beans.get(0).getLatitude();
        titleCenter = beans.get(0).getMachineMark();
        mesCenter = beans.get(0).getLocationName();
        statusCenter = beans.get(0).getStatus();
        iconPathCenter = beans.get(0).getIconPath();
    }
    
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
<meta name="description" content="端机运行地图监控">
<title>端机运行地图监控</title>
<link rel="stylesheet" type="text/css" href="./css/monitor.css">
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.4"></script>
 <script language="JavaScript"><!--
function refreshByOrder(type){
	window.document.form_data.orderType.value =type;
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_RUNNING_ID %>";
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


function myrefresh(){ 
	showMacMap();
}
var refreshTime = <%=LocalProperties.getProperty("MONITOR_RUNNING_REFRESH_TIME")%> + 0;
//setTimeout('myrefresh()',refreshTime);

function onland() {
            var map = new BMap.Map("container");
            var point = new BMap.Point(Number(<%=lngCenter%>), Number(<%=latCenter%>)); //默认中心点
            <% if(beans != null && beans.size() > 0){%>
            var marker = new BMap.Marker(point);

            var sContent =
            	"<h4 style='margin:0 0 5px 0;padding:0.2em 0'><%=titleCenter%></h4>" +  
            	"<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'><%=mesCenter%></p>";
            var infoWindow = new BMap.InfoWindow(sContent);  
            
            marker.addEventListener("mouseover", function(){ 
            	   this.openInfoWindow(infoWindow);
            });

            marker.addEventListener("mouseout", function(){ 
         	   this.closeInfoWindow();
         	});

            marker.setIcon(new BMap.Icon("<%=iconPathCenter%>",new BMap.Size(31,49)));
            //marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画,中心点位置

            <% for(int m = 1; m < beans.size(); m++){
            	MachineBean mac = beans.get(m);
            %>
            var point = new BMap.Point(Number(<%=mac.getLongitude()%>),Number(<%=mac.getLatitude()%>));
            addMarker(map,point,"<%=mac.getMachineMark()%>","<%=mac.getLocationName()%>","<%=mac.getIconPath()%>");
            <%} %>
            map.addOverlay(marker); //标记地图
            <%}%>

          //marker.enableDragging(); //启用拖拽
            map.centerAndZoom(point, <%=level%>); //绘制地图
            map.addControl(new BMap.NavigationControl()); //左上角控件
            map.enableScrollWheelZoom(); //滚动放大
            map.enableKeyboard(); //键盘放大
            map.addControl(new BMap.OverviewMapControl());//添加默认缩略地图控件
            map.addControl(new BMap.OverviewMapControl({isOpen:true, anchor: BMAP_ANCHOR_TOP_RIGHT}));//右上角，打开
        }

//编写自定义函数,创建标注
function addMarker(map,point,wTitle,wMessage,path){
  var marker = new BMap.Marker(point);
  map.addOverlay(marker);//标记地图

  var sContent =
  	"<h4 style='margin:0 0 5px 0;padding:0.2em 0'>"+wTitle+"</h4>" +  
  	"<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>"+wMessage+"</p>";
  var infoWindow = new BMap.InfoWindow(sContent);
  
  map.addOverlay(marker); //标记地图
  marker.addEventListener("mouseover", function(){ 
	   this.openInfoWindow(infoWindow);
  });

  marker.addEventListener("mouseout", function(){ 
       this.closeInfoWindow();
  });
  marker.setIcon(new BMap.Icon(path,new BMap.Size(31,49)));
}

 </script>

</head>
<body onload="onland()">
<form method="POST" name="form_data" action="/th/MonitorServlet.html">
<input type="hidden" name="orderType"   value="">
<input type="hidden" name="pageID"   value="">
<input type="hidden" name="orgID"   value="">
<input type="hidden" name="macType"   value="<%=(String) request.getAttribute( "macType" ) %>">
<div id="mapbox" style="width:99%;height:490px; background-color:#CCC; border:1px solid #9CF; font-size:12px;">  
<div id="container" style="width:100%;height:490px; float:left;"></div>
</div>
			<div>
						<input type="button" class="rb_jzjk1" value="地图监控" onmouseover="this.className='rb_jzjk2';" onmouseout="this.className='rb_jzjk1';" onclick="showMacMap();"/>
						<input type="button" class="rb_jzjk1" value="列表监控" onmouseover="this.className='rb_jzjk2';" onmouseout="this.className='rb_jzjk1';" onclick="refreshListByOrder('2');"/>
						<input type="button" class="rb_jzjk1" value="矩阵监控" onmouseover="this.className='rb_jzjk2';" onmouseout="this.className='rb_jzjk1';" onclick="refreshByOrder('2');"/>
			</div>

</form>
</body>
</html>
<% logger.info( jspName + " : end"); %>
