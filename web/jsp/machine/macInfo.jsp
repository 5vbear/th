<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%> 
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macInfo.jsp";
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

    String machineID = StringUtils.transStr((String)request.getAttribute("machineID"));
    logger.info("machineID = " + machineID);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>端机信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="./css/machine.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="./css/channel.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/style.css"/>
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
		<script language="JavaScript">
			var cur_id="";
			var flag=0,sflag=0;
			//组织结构下拉菜单
			var setting = {
			    view: {  
			        dblClickExpand: false  
			    },  
			    data: {  
			        simpleData: {  
			            enable: true  
			        }  
			    },  
			    callback: {  
			        //beforeClick: beforeClick,//(点击之前)用于捕获 勾选 或 取消勾选 之前的事件回调函数，并且根据返回值确定是否允许 勾选 或 取消勾选   
			        onClick: treeSelected 
			    }  
			};  
			var zTreeNodes;
			var zTree;
			$(function() {
				zTreeNodes = <%=(String)request.getAttribute( "orgsList" )%> ;
			}); 
			
			$(document).ready(function(){  
				zTreeObj = $.fn.zTree.init($("#menu"), setting, zTreeNodes);  
				
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
			
			function treeSelected(event, treeId, treeNode){
				if(treeNode.mactype == 'org'){
					return;
				}
				sub.location.href = '/th/machineServlet?model=info&method=view&macIdStd='+treeNode.id
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
	<body style="background-color: #fff;">
		<div class="x-title"><span>&nbsp;&nbsp;端机管理-端机信息</span></div>
		<table><tr style ="height:3px"></tr></table>
		<div style="height: 520px; padding-top: 0px; float: left;">
    		<ul id="menu" class="ztree"></ul>
		</div>
		<div>
			<Iframe src="/th/machineServlet?model=info&method=view" scrolling="scroll" height="580" width="710" frameborder="0" name="sub" id="sub"></iframe>
		</div>
	</body>
</html>