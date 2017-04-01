<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="th.ftp.dao.FtpInfoBean" %>
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

	FtpInfoBean ftpInfoBean = (FtpInfoBean) request.getAttribute( "ftpInfoBean" );
	long mangId = ftpInfoBean.getFtpMangId();
	String ftpUserName = ftpInfoBean.getFtpuser();
	String uploadSpeed = ftpInfoBean.getMaxUploadSpeed();
	String downloadSpeed = ftpInfoBean.getMaxDownloadSpeed();

	uploadSpeed = th.util.StringUtils.isBlank( uploadSpeed ) ? "" : uploadSpeed;
	downloadSpeed = th.util.StringUtils.isBlank( downloadSpeed ) ? "" : downloadSpeed;
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FTP设定画面</title>
<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="./css/channel.css" />
<link rel="stylesheet" type="text/css" href="./css/machine.css" />

<SCRIPT LANGUAGE="JavaScript">

	function btnOperations() {
		
		this.save = function(){	
			// 验证非负整数
			var myInt = new RegExp("^([1-9]\\d*|0)$");
			// 上传速度取得
			var inUploadSpeed = document.getElementById("up");
			// 下载速度取得
			var inDownloadSpeed = document.getElementById("down");
			//alert("上传速度请输入为整数"+!myInt.test(inUploadSpeed.value));
			//alert("上传速度请输入为整数"+inUploadSpeed.value);
			//alert("下载速度请输入为整数"+!myInt.test(inDownloadSpeed.value));
			if(null != inUploadSpeed.value && ""!=inUploadSpeed.value && !myInt.test(inUploadSpeed.value)){
				alert("上传速度请输入为非负整数");
			}else if(null != inDownloadSpeed.value && ""!=inDownloadSpeed.value && !myInt.test(inDownloadSpeed.value)){
				alert("下载速度请输入为非负整数");
			}else{
				window.document.form_data.dealFlg.value = "save";
				window.document.form_data.action = "/th/ftpsetting.html";
				window.document.form_data.mangId.value = "<%=mangId%>";
				window.document.form_data.ftpUserName.value = "<%=ftpUserName%>";
				window.document.form_data.uploadSpeed.value = inUploadSpeed.value;
				window.document.form_data.downloadSpeed.value = inDownloadSpeed.value;
				window.document.form_data.submit();
			}
		};
		this.back = function(){
			window.document.form_data.action = "/th/ftpsetting.html";
			window.document.form_data.submit();
		}
		
	};	
	var btnOper = new btnOperations();
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp; 系统管理-FTP设定</span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="mangId" value=""/>
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="ftpUserName" value=""/>
		<input type="hidden" name="uploadSpeed" value=""/>
		<input type="hidden" name="downloadSpeed" value=""/>

		<div style="height: 520px; float: left;">
			<table style="width: 500px" class="x-data-table">
				<tr>
					<th style="width: 45%" class="x-data-table-th">用户名</th>
					<td class="x-data-table-td"><input type="text" size="40" name="fun" id="fun"  value="<%=ftpUserName%>"  disabled/></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">上传速度</th>
					<td class="x-data-table-td"><input type="text" size="40" name="up" id="up"  value="<%=uploadSpeed%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">下载速度</th>
					<td class="x-data-table-td"><input type="text" size="40" name="down" id="down"  value="<%=downloadSpeed %>" /></td>
				</tr>
			</table>
			<div class="x-client-form">
    			<input class="tableBtn" type="button" name="button_Save" id="btnSave" value="保存" onclick="btnOper.save()" /> 
				<input class="tableBtn" type="button" name="button_Return" id="btnRet" value="返回" onclick="btnOper.back()" /> 
	  		</div>
		</div>
	</form>
</body>
</html>