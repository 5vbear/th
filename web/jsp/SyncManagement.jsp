<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
	String zNodes = (String) request.getAttribute( "zNodes" );
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>同步管理页面</title>
<link rel="stylesheet" href="../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../css/machine.css" />
<SCRIPT LANGUAGE="JavaScript">

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
	var zNodes = <%=zNodes%>
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
	
	function zTreeOnClick(event, treeId, treeNode) {
	    /* alert(treeNode.id + ", " + treeNode.name + ", " + treeNode.checked); */
	}; 
	
	function btnOperations() {
		
		this.servSync = function(){
			alert("服务端同步成功!");
		};		
		this.macSync = function(){
			alert("客户端同步成功!");
		}
		
	};	
	var btnOper = new btnOperations();


</SCRIPT>
<style type="text/css">
.ztree li span.button.pIcon01_ico_open{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon01_ico_close{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/bank/2-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon01_ico_docu{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_open{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_close{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/bank/3-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon02_ico_docu{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_open{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_close{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/bank/4-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon03_ico_docu{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_open{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_close{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon04_ico_docu{margin-right:2px; background: url(../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
</style>
</head>
<body>

	<%
		Log logger = LogFactory.getLog( this.getClass() );
		User user = (User) session.getAttribute( "user_info" );
		String realname = null;
		if ( user == null ) {
			response.setContentType( "text/html; charset=utf-8" );
			response.sendRedirect( "/th/index.jsp" );
		}
		else {
			realname = user.getReal_name();
			logger.info( "获得当前用户的用户名，用户名是： " + realname );
		}
	%>
	
	<div class="x-title"><span>&nbsp;&nbsp;同步管理</span></div>
	<table><tr style ="heigt:30px"></tr></table>
	<div style="margin: 0 auto; float: left;">
		<form method="POST" name="form_data" action="">

			<ul style="margin: 0 auto; list-style: none;">
				<div style="height: 520px; padding-top: 0px; float: left;">
					<ul id="treeDemo" class="ztree"></ul>
				</div>
				<div style="height: 520px; padding-top: 10px; padding-left: 20px; float: left;">
					<table border="0" cellspacing="0" cellpadding="0" class="contact_input">
						<tr>		
							<td colspan="2"><input class="tableBtn" type="button" name="button_server" id="btnServ" value="服务端同步" onclick="btnOper.servSync()" /></td>
							<td colspan="2"><input class="tableBtn" type="button" name="button_machine" id="btnMac" value="端机同步" onclick="btnOper.macSync()" /></td> 
						</tr>
					</table>
				</div>
			</ul>
		</form>
	</div>

</body>
</html>