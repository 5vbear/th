<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	DptDealDAO ddd = new DptDealDAO();
	
	long orgId = Long.parseLong((String)request.getParameter("orgId"));
	HashMap[] dptsMap = ddd.getAllDptsByOrgId( orgId );

	HashMap curDpt = null;
	long dptId = -1;
	String dptName = "";
	if(dptsMap!=null&&dptsMap.length>0){
		out.print("<select id='SelectDpt' name='selectdpt'>");
		out.print("<option value='-1'></option>");
		for(int i=0;i<dptsMap.length;i++){
			curDpt = (HashMap)dptsMap[i];
			dptId = Long.parseLong((String)curDpt.get( "DEPARTMENT_ID" ));
			dptName = (String)curDpt.get( "DEPARTMENT_NAME" );
			out.print("<option value='" + dptId + "'>" + dptName + "</option>");
		}
		out.print("</select>");
	}else{
		out.print("<select id='SelectDpt' name='selectdpt'>");
		out.print("<option value='-1'></option>");
		out.print("</select>");
	}
%>

