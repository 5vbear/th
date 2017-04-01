<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "ClientSystemInfo.jsp";
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
<meta name="description" content="端机系统监控">
<title>端机系统监控</title>
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
	view: {
	    dblClickExpand: false,
	    showLine: false
    },
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "pId",
			rootPId : 0
		}
	},		
	callback: {
		//beforeClick: beforeClick,
		onClick: zTreeOnClick
	},
	view: {
		selectedMulti: false

	}

};
var zNodes = <%=(String)request.getAttribute( "MONITOR_ORG_MAC_LIST" )%> 
$(document).ready(function() {
	zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
});	

function beforeClick(treeId, treeNode) {
	//是节点，并且不是父级  
    var check = (treeNode && !treeNode.isParent);
    if (!check) 
        alert("请选择端机...");
    return check;  
}

function zTreeOnClick(event, treeId, treeNode) {
	if(treeNode.mactype == 'mac'){
		var treeID = treeNode.id+"";
		var prefix = "<%=Define.TREE_ID_PREFIX%>";
		if(0 == treeID.indexOf(prefix)){
			treeID = treeID.replace(prefix,"");
		}
	    
	    redirect(treeID);
	} else {
		//是节点，并且不是父级  
	    var check = (treeNode && !treeNode.isParent);
	    if (!check) {
	    	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.expandNode(treeNode);
		} else {
			alert("请选择端机...");
		}
	}
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
        	if(nodes[i].mactype == 'mac'){
				nodes[i].iconSkin = "icon04";
			}else{
				nodes[i].iconSkin = "icon0" + level;
			}
        }
        zTreeObj.updateNode(nodes[i]);
    }
});

function redirect(macID){
	//*************只有是端机的节点才能执行该函数****************
	window.document.form_data.orderType.value ="2";
	window.document.form_data.pageID.value ="<%= Define.JSP_MONITOR_CLIENT_ID %>";
    window.document.form_data.action = "<%= Define.MONITOR_SERVLET %>";
    window.document.form_data.machineID.value =macID;
    window.document.form_data.target = "sub";
    window.document.form_data.submit();
}


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
<div class="x-title"><span>&nbsp;&nbsp;监控管理-端机系统监控</span></div>
<body class="panel" topmargin="0" leftmargin="0">
<form method="POST" name="form_data" action="/th/MonitorServlet.html">
<input type="hidden" name="orderType"   value="2">
<input type="hidden" name="pageID"   value="<%=Define.JSP_MONITOR_CLIENT_INFO %>">
<input type="hidden" name="machineID"   value="">
<div style="height: 520px; padding-top: 0px; float: left;">
		<ul id="treeDemo" class="ztree"></ul>
</div>
<div>
<Iframe  src="/th/MonitorServlet.html?orderType=2&pageID=monitor03"  scrolling="none" height="540"  width="710"  frameborder="0" name="sub" id="sub"></iframe>
</div>
</form>
</body>
</fieldset>
</html>
<% logger.info( jspName + " : end"); %>
