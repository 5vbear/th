<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ page import="org.apache.commons.logging.Log" %>

<%
Log	logger	= LogFactory.getLog(getClass());
logger.info( "PS11 page log info" );
out.print( "TTT" );
%>

