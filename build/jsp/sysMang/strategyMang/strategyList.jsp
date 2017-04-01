<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="th.com.util.Define" %>

<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

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

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>默认角色策略列表页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../../css/machine.css" />

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
		view : {
			selectedMulti : false
	
		}
	
	};
	var zNodes = [ { "id": 0, "pId": -1, "name": "中国建设银行", open:true },
	               { "id": 1, "pId": 0, "name": "辽宁省分行", open:true },
	               { "id": 2, "pId": 0, "name": "北京市分行", open:true },
	               { "id": 3, "pId": 0, "name": "吉林省分行", open:true },
	               { "id": 4, "pId": 0, "name": "山东省分行", open:true },
	               { "id": 5, "pId": 1, "name": "沈阳市支行"},
	               { "id": 6, "pId": 1, "name": "朝阳市支行"} ];
	
	
	$(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	});

	function btnOperations() {

		this.addStg = function() {
			window.document.form_data.dealFlg.value = "add";
			window.document.form_data.action = "/th/jsp/sysMang/strategyMang/strategyDeal.html";
			window.document.form_data.submit();
		};
		this.changeStg = function(me){
			var btnId = me.id;
			// "btnChange_"
			var stgId = btnId.substring(10);
			window.document.form_data.dealFlg.value = "change";
			window.document.form_data.action = "/th/jsp/sysMang/strategyMang/strategyDeal.html";
			window.document.form_data.sel_stg_id.value = stgId;
			window.document.form_data.submit();
		};
		this.deleteStg = function(me){
			var btnId = me.id;
			// "btnDel_"
			var stgId = btnId.substring(7);
			window.document.form_data.dealFlg.value = "del";
			window.document.form_data.action = "/th/jsp/sysMang/strategyMang/strategyList.html";
			window.document.form_data.sel_stg_id.value = stgId;
			window.document.form_data.submit();

		}

	};
	var btnOper = new btnOperations();
	
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;系统管理-默认角色策略定义</span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="sel_stg_id" value=""/>

		<div>
			<div>
				<table class="x-data-table" id="dataTableId">
					<%
						out.print( "<tr><th style='width: 30%' class='x-data-table-th'>策略名称</th>"
								+ "<th style='width: 15%' class='x-data-table-th'>操作</th></tr>" );

						HashMap[] stgsList = (HashMap[]) request.getAttribute( "StgList" );

						if ( stgsList != null && stgsList.length > 0 ) {

							for ( int i = 0; i < stgsList.length; i++ ) {
								HashMap stgMap = stgsList[i];
								long stgID = Long.parseLong( (String) stgMap.get( "STG_ID" ) );
								String stgName = (String) stgMap.get( "STG_NAME" );

								// middle
								out.print( "<tr><td class='x-data-table-td-left-pad'>" + stgName + "</td> " );
								// bottom
								out.print( "<td class='x-data-table-td-left-pad'><input class='tableBtn' type='button' value='编辑' name='buttonChange_"
										+ stgID + "' id='btnChange_" + stgID + "' onclick='btnOper.changeStg(this)'/><input class='tableBtn' type='button' value='删除' name='buttonDelete_"
										+ stgID + "' id='btnDel_" + stgID + "' onclick='btnOper.deleteStg(this)'/></td></tr>" );

							}

						}
					%>
				</table>
			</div>
			<div style="float: left">
				<input class="leftBtn" type="button" name="button_Add" id="btnAdd" value="添加" onclick="btnOper.addStg()" /> 
			</div>
		</div>
	</form>
</body>
</html>
