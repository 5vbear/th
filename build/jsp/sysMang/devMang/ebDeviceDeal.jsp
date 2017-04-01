<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="th.entity.EBankDeviceBean" %>
<%@ page import="java.util.HashMap" %>
    
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

<%

	EBankDeviceBean ebb = (EBankDeviceBean) request.getAttribute( "selDevBean" );
	long selDevId = ebb.getDevId();
	String selDevOs = ebb.getDevOs();
	String selDevDesp = ebb.getDevDesp();
	
	String saveResult = (String)request.getAttribute( "saveResult" );
	String pageTitle = (String)request.getAttribute( "pageTitle" );
	String acTurn = (String)request.getAttribute( "acTurn" );
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备操作系统信息定义页面</title>
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

	// window.onload必须等到页面内包括图片的所有元素加载完毕后才能执行 
	window.onload = function(){
		var saveResult = "<%=saveResult%>";
		if(saveResult!=""){
			alert(saveResult);
		}
	};

	function btnOperations() {
		
		this.enabled = function(){
			
			document.getElementById("devOs").disabled = false;
			document.getElementById("devDesp").disabled = false;
			
		};	
		this.save = function(){	
			
			var inDevOs = document.getElementById("devOs");
			var inDevDesp = document.getElementById("devDesp");
			
			if(inDevOs.value==""){
				alert("请输入当前希望添加的设备操作类型!");
			}else{
				window.document.form_data.dealFlg.value = "add/change";
				window.document.form_data.action = "/th/jsp/sysMang/devMang/ebDeviceDeal.html";
				window.document.form_data.hide_dev_id.value = <%=selDevId%>;
				window.document.form_data.in_dev_os.value = inDevOs.value;
				window.document.form_data.in_dev_description.value = inDevDesp.value;
				window.document.form_data.page_index.value = "ebDeviceDeal";
				window.document.form_data.submit();
			}
		};
		this.back = function(){
			window.document.form_data.action = "/th/jsp/sysMang/devMang/ebDeviceList.html";
			window.document.form_data.submit();
		}
		
	};	
	var btnOper = new btnOperations();
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;<%=pageTitle%></span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="in_dev_os" value=""/>
		<input type="hidden" name="in_dev_description" value=""/>
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="hide_dev_id" value=""/>
		<input type="hidden" name="page_index" value=""/>

		<div style="height: 520px; float: left;">
			<table style="width: 500px" class="x-data-table">
				<tr>
					<th style="width: 45%" class="x-data-table-th">设备操作系统[*]</th>
					<td class="x-data-table-td"><input type="text" size="40" name="devOs" id="devOs" <% out.print(acTurn); %> value="<%=selDevOs%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">设备类型说明</th>
					<td class="x-data-table-td"><input type="text" size="40" name="devDesp" id="devDesp" <% out.print(acTurn); %> value="<%=selDevDesp%>" /></td>
				</tr>
			</table>
			<div class="x-client-form">
			 	<%
			 		if(!"".equals(acTurn)){
			 			out.print("<input class='tableBtn' type='button' name='button_Enabled' id='btnEnabled' value='编辑' onclick='btnOper.enabled()' /> ");
			 		}
			 	%>	    		
    			<input class="tableBtn" type="button" name="button_Save" id="btnSave" value="保存" onclick="btnOper.save()" /> 
				<input class="tableBtn" type="button" name="button_Return" id="btnRet" value="返回" onclick="btnOper.back()" /> 
	  		</div>
		</div>
	</form>
</body>
</html>