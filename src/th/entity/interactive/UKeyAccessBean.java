/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity.interactive;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import th.util.StringUtils;

/**
 * Descriptions
 *
 * @version 2013年9月10日
 * @author PSET
 * @since JDK1.6
 *
 */
public class UKeyAccessBean extends BaseUploadBean {

	public static final String	REQ_PARAM_ACCESSTIME		= "accesstime";

	public static String[]		paramCheckArray				= { REQ_PARAM_MAC, REQ_PARAM_ACCESSTIME };

	public UKeyAccessBean() {

	}

	public UKeyAccessBean(HttpServletRequest request) {
		setMac(request.getParameter("mac"));
		accesstime = StringUtils.getYyyyMMddHHmmss(request.getParameter(REQ_PARAM_ACCESSTIME));
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[mac=").append(this.getMac());
		sb.append(",machinId=").append(this.getMachineId());
		sb.append(",accesstime=").append(accesstime);
		sb.append("]");
		return sb.toString();
	}

	private Date	accesstime			= null;

	/**
	 * @return the accesstime
	 */
	public Date getAccesstime() {
		return accesstime;
	}

	/**
	 * @param accesstime the accesstime to set
	 */
	public void setAccesstime( Date accesstime ) {
		this.accesstime = accesstime;
	}
	
}
