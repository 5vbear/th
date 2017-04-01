<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="th.com.util.Define" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.util.DateUtil"%>
<%
	Log logger = LogFactory.getLog("main.jsp");
	String jspName = "orgSelect.jsp";
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
	String currentAction="122";//获得进行当前操作所需要的用户权限。
	if(!user.hasRight(user.getOrg_id(),currentAction)){
		 logger.info( "当前用户没有所需要的权限，跳转到/th/common/noaction.jsp" );
		 response.setContentType("text/html; charset=utf-8");
		 response.sendRedirect(request.getContextPath()+"/jsp/common/noaction.jsp");
		return;
	}else{
		 logger.info( "当前用户拥有所需要的权限" );
	}
String defaultTime = DateUtil.getYesterdayDate("yyyy-MM-dd");
String selectedOrg = (String)request.getAttribute("selectedOrg");
if(selectedOrg ==null||selectedOrg.equals("")){
	selectedOrg = "0";
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
<link rel="stylesheet" href="<%=request.getContextPath()%>/zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/channel.css">
<style type="text/css">
.ztree li span.button.pIcon01_ico_open{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon01_ico_close{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/bank/2-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon01_ico_docu{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_open{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_close{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/bank/3-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon02_ico_docu{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_open{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_close{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/bank/4-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon03_ico_docu{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_open{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_close{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon04_ico_docu{margin-right:2px; background: url(<%=request.getContextPath()%>/zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script language="JavaScript">
var setting_add = {		
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId",
				rootPId : 0,
			}
		},		
		callback: {
			onClick: zTreeOnClick_check
		},
		view: {
			fontCss: setFontCss,
			selectedMulti: false

		}

	};
var zTreeNodes;
var zTree;
$(function() {
	zTreeNodes = <%=(String)request.getAttribute( "zNodes" )%> ;
}); 
var zTreeObj;
$(document).ready(function(){
	intiShowTree();
});

function setFontCss(treeId, treeNode) {
	return treeNode.checked == false ? {color:"black"} : {};
};
function zTreeOnClick_check(event, treeId, treeNode) {
	var treeID = treeNode.id+"";
	var prefix = "<%=Define.TREE_ID_PREFIX%>";
	if(0 == treeID.indexOf(prefix)){
		treeID = treeID.replace(prefix,"");
	}
    redirect(treeID);
}
function redirect(orgID){
	$('#orgID').val(orgID);
	window.open("/th/lottery.html?type=list_award_panel&selectedOrg="+orgID ,target='sub');
}
function intiShowTree(){
	zTreeObj = $.fn.zTree.init($("#treeDemo"), setting_add, zTreeNodes);
	
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
}
</script>
</head>
<div class="x-title"><span>系统管理-抽奖管理</span></div>
<body class="panel" topmargin="0" leftmargin="0">
<form method="POST" name="myform" action="/th/lottery.html">
<input type="hidden" name="type"   value="list_award">
<input type="hidden" name="selectedOrg" id="orgID"  >
	<div style="height: 520px; padding-top: 0px; float: left;">
			<ul id="treeDemo" class="ztree"></ul>
	</div>
	<div style=" height: 530px;float: left;margin-top: 10px;padding-left: 5px;">
	 	<Iframe  src="/th/lottery.html?type=list_award_panel&selectedOrg=<%=selectedOrg %>"  height="530" width="730"  frameborder="0" name="sub" id="sub"></iframe>
	</div>
</form>
</body>
</html>
<% System.out.println( jspName + " : end"); %>
