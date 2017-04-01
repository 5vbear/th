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

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="/th/css/machine.css">
		<title>等待中</title>
	</head>
	<SCRIPT LANGUAGE="JavaScript">		
		function closeit(){
			setTimeout('self.close()',Math.round(Math.random()*2 + 1)*1000);
		}
	</script>
	<body style="background-color: #fff; text-align:center;padding-top:30px" onload="closeit()" >
		信息发送中...
	</body>
</html>