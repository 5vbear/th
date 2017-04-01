/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity.interactive;

import javax.servlet.http.HttpServletRequest;

/**
 * Descriptions
 *
 * @version 2013年8月20日
 * @author PSET
 * @since JDK1.6
 *
 */
public class InteractiveBean extends BaseUploadBean {
	
	/** 客户端状态 */
	public static final String	REQ_PARAM_STATUS	= "status";
	/** 终端执行指令 */
	public static final String	REQ_PARAM_COMMAND	= "command";
	/** 指令执行状态 */
	public static final String	REQ_PARAM_COMST	= "comst";
	/** 终端IP */
	public static final String	REQ_PARAM_ENDPOINTIP	= "endpointip";
	/** 终端操作系统 */
	public static final String	REQ_PARAM_OS	= "os";
	/** 终端计算机名 */
	public static final String	REQ_PARAM_ENDPOINTNAME	= "endpointname";
	
	public static String[] paramCheckArray = {REQ_PARAM_STATUS,REQ_PARAM_COMMAND,REQ_PARAM_COMST,REQ_PARAM_ENDPOINTIP,REQ_PARAM_OS,REQ_PARAM_ENDPOINTNAME};
	
	public InteractiveBean(){
		
	}
	
	public InteractiveBean(HttpServletRequest request){
		this.setMac( request.getParameter( REQ_PARAM_MAC ) );
		status = request.getParameter( REQ_PARAM_STATUS );
		command = request.getParameter( REQ_PARAM_COMMAND );
		comst = request.getParameter( REQ_PARAM_COMST );
		endpointip = request.getParameter( REQ_PARAM_ENDPOINTIP );
		os = request.getParameter( REQ_PARAM_OS );
		endpointname = request.getParameter( REQ_PARAM_ENDPOINTNAME );
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[mac=").append(this.getMac());
		sb.append(",machinId=").append(this.getMachineId());
		sb.append(",status=").append(status);
		sb.append(",command=").append(command);
		sb.append(",comst=").append(comst);
		sb.append(",endpointip=").append(endpointip);
		sb.append(",os=").append(os);
		sb.append(",endpointname=").append(endpointname);
		sb.append("]");
		return sb.toString();
	}
	
	String status = "";
	String command = "";
	String comst = "";
	String endpointip = "";
	String os = "";
	String endpointname = "";

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus( String status ) {
		this.status = status;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand( String command ) {
		this.command = command;
	}

	/**
	 * @return the comst
	 */
	public String getComst() {
		return comst;
	}

	/**
	 * @param comst the comst to set
	 */
	public void setComst( String comst ) {
		this.comst = comst;
	}

	/**
	 * @return the endpointip
	 */
	public String getEndpointip() {
		return endpointip;
	}

	/**
	 * @param endpointip the endpointip to set
	 */
	public void setEndpointip( String endpointip ) {
		this.endpointip = endpointip;
	}

	/**
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * @param os the os to set
	 */
	public void setOs( String os ) {
		this.os = os;
	}

	/**
	 * @return the endpointname
	 */
	public String getEndpointname() {
		return endpointname;
	}

	/**
	 * @param endpointname the endpointname to set
	 */
	public void setEndpointname( String endpointname ) {
		this.endpointname = endpointname;
	}
	
}
