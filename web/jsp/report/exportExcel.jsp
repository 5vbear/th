<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="th.util.ExportUtil"%>
<%@page import="th.com.util.Define4Report"%>
<%
	String fileName = (String) request.getAttribute( Define4Report.REQ_PARAM_EXPORT_FILE_NAME );
	byte[] exportBytes = (byte[]) request.getAttribute( Define4Report.REQ_PARAM_EXPORT_BYTES );
	ExportUtil.exportExcelFile( response, exportBytes, fileName );	
	
	out.clear();
	out = pageContext.pushBody();
%>


