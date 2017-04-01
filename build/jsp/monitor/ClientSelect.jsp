<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="th.com.util.Define" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
	Log logger = LogFactory.getLog("main.jsp");
	String jspName = "ClientSetting.jsp";
	logger.info( jspName + " : start" );
	
	User user = (User) session.getAttribute("user_info");
	String realname =null;
	if (user == null) {
		 response.setContentType("text/html; charset=utf-8");
		 response.sendRedirect("/th/index.jsp");
		return;
	} else {//此处表明当前用户已经login
		realname = user.getReal_name();
		logger.info("获得当前用户的用户名，用户名是： " + realname);
	
	}
	//开始判断当前用户是否有权限设置进行当前操作。
	String currentAction="999";//获得进行当前操作所需要的用户权限。
	if(!user.hasRight(user.getOrg_id(),currentAction)){
		 logger.info( "当前用户没有所需要的权限，跳转到/th/common/noaction.jsp" );
		 response.setContentType("text/html; charset=utf-8");
		 response.sendRedirect(request.getContextPath()+"/jsp/common/noaction.jsp");
		return;
	}else{
		 logger.info( "当前用户拥有所需要的权限" );
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
<title></title>
<link rel="stylesheet" type="text/css" href="./css/monitor.css">
<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
<link rel="stylesheet" type="text/css" href="./css/style.css"/>
<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
<link rel="stylesheet" type="text/css" href="./css/channel.css">
<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script language="JavaScript">
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
	callback: {
		onClick: zTreeOnClick
	},
	view: {
		selectedMulti: false

	}

};
var zNodes = <%=(String)request.getAttribute( "zNodes" )%> ;


function zTreeOnClick(event, treeId, treeNode) {

		var treeID = treeNode.id+"";
		var prefix = "<%=Define.TREE_ID_PREFIX%>";
		if(0 == treeID.indexOf(prefix)){
			treeID = treeID.replace(prefix,"");
		}
	    redirect(treeID);
	
}

function redirect(orgID){
	window.open("/th/MonitorServlet.html?pageID=monitor07&orgID="+orgID,target='sub');
}

$(document).ready(function() {
	zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	
	var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
	// 设置ztree不同层级的节点显示图标 
	for (var i=0; i<nodes.length; i++) {
		
		var level = 0;
		level = nodes[i].level;
		level = level + 1;
		if(nodes[i].isParent){
			nodes[i].iconSkin = "pIcon0" + level;
		}else{
			nodes[i].iconSkin = "icon0" + level;
		}
		zTreeObj.updateNode(nodes[i]);
	}
});	
</script>
<style type="text/css">
.ztree li span.button.pIcon01_ico_open{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon01_ico_close{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/bank/2-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon01_ico_docu{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_open{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_close{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/bank/3-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon02_ico_docu{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_open{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_close{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/bank/4-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon03_ico_docu{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_open{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_close{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon04_ico_docu{margin-right:2px; background: url(./zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
</style>

</head>

<div class="x-title"><span>&nbsp;&nbsp;监控管理-端机报警设置</span></div>
<body class="panel" topmargin="0" leftmargin="0">
<form method="POST" name="myform" action="/th/MonitorServlet.html">
<input type="hidden" name="orderType"   value="2">
<input type="hidden" name="pageID"   value="<%=Define.JSP_MONITOR_CLIENT_INFO %>">
<input type="hidden" name="machineID"   value="">
<div style="height: 520px; padding-top: 0px; float: left;">
		<ul id="treeDemo" class="ztree"></ul>
</div>
<Iframe  src="/th/MonitorServlet.html?pageID=monitor07&orgID=-1"  scrolling="no" height="600"  width="748"  frameborder="0" name="sub" id="sub"></iframe>
</form>
</body>

</html>
<% System.out.println( jspName + " : end"); %>
