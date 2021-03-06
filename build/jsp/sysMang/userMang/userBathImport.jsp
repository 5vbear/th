<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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
<title>用户批量导入页面</title>
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
		
		this.commit = function(){
			var filename = document.getElementById("userBathScan").value;
			var suffix = filename.substring(filename.lastIndexOf('.')+1).toLowerCase();
			if(suffix != 'txt'){
				alert('请选择上传txt文本文件!');
				return;
			}
			window.document.form_data.action = "/th/jsp/sysMang/userMang/userSearch.html?dealFlg=bathIn";
		    window.document.form_data.submit();
		};
		this.back = function(){
			window.document.form_data.action = "/th/jsp/sysMang/userMang/userSearch.html";
			window.document.form_data.submit();
		}	
	};
	
	var btnOper = new btnOperations();
	
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;系统管理-用户批量导入</span></div>
	<table><tr style ="heigt:30px"></tr></table>
	<form class="x-client-form" method="POST" name="form_data" enctype="multipart/form-data" action="" >
		<div>
			<table class="x-data-table" id="dataTableId">
				<tr>
					<th style="width: 20%" class="x-data-table-th">文件选择[*]</th>
					<td class="x-data-table-td">&nbsp;&nbsp;<input type="file" name="userBathScan" id="userBathScan"></td>
				</tr>
			</table>
		</div>
		<div style="float: left">
			<input class="tableBtn" type="button" name="button_commit" id="btnCmt" value="提交" onclick="btnOper.commit()" /> 
	    	<input class="tableBtn" type="button" name="button_Back" id="btnBack" value="返回" onclick="btnOper.back()" /> 
		</div>
	</form>

</body>
</html>