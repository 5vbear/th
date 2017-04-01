<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="th.com.util.Define" %>

<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="th.ftp.dao.FtpInfoBean"%>
<%@ page import="java.util.List"%>

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
	List<FtpInfoBean> ftpsList =  (List<FtpInfoBean>)request.getAttribute( "FTP_INFO" );
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FTP管理页面</title>
<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="./css/channel.css" />
<link rel="stylesheet" type="text/css" href="./css/machine.css" />

<SCRIPT LANGUAGE="JavaScript">

	function btnOperations() {

		this.changeStg = function(me){
			var mangId = me.id;;
			window.document.form_data.dealFlg.value = "change";
			window.document.form_data.action = "/th/ftpsetting.html";
			window.document.form_data.mangId.value = mangId;
			window.document.form_data.submit();
		};

	};
	var btnOper = new btnOperations();
	
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;系统管理-FTP管理</span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="mangId" value=""/>
		

		<div>
			<div>
				<table class="x-data-table" id="dataTableId">
					<%
						out.print( "<tr><th style='width: 30%' class='x-data-table-th'>用户名</th>"
								+ "<th style='width: 30%' class='x-data-table-th'>上传速度</th>"
								+ "<th style='width: 30%' class='x-data-table-th'>下载速度</th>"
								+ "<th style='width: 10%' class='x-data-table-th'>操作</th></tr>" );

						if ( ftpsList != null && ftpsList.size() > 0 ) {

							for ( int i = 0; i < ftpsList.size(); i++ ) {
								long mangId = (Long) ftpsList.get(i).getFtpMangId();
								String ftpUserName = (String) ftpsList.get(i).getFtpuser();
								String downloadSpeed = (String) ftpsList.get(i).getMaxDownloadSpeed();
								String uploadSpeed = (String) ftpsList.get(i).getMaxUploadSpeed();
								downloadSpeed = th.util.StringUtils.isBlank( downloadSpeed ) ? "无限制" : downloadSpeed;
								uploadSpeed = th.util.StringUtils.isBlank( uploadSpeed ) ? "无限制" : uploadSpeed;

								// middle
								out.print( "<tr><td class='x-data-table-td-left-pad'>" + ftpUserName + "</td> " );
								out.print( "<td class='x-data-table-td-left-pad'>" + uploadSpeed + "</td> " );
								out.print( "<td class='x-data-table-td-left-pad'>" + downloadSpeed + "</td> " );
								// bottom
								out.print( "<td class='x-data-table-td-left-pad'><input class='tableBtn' type='button' value='编辑' name='buttonChange_"
										+ mangId + "' id='" + mangId + "' onclick='btnOper.changeStg(this)'/></td></tr>" );

							}

						}
					%>
				</table>
			</div>
		</div>
	</form>
</body>
</html>
