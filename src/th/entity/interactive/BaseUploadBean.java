package th.entity.interactive;

import javax.servlet.http.HttpServletRequest;

public class BaseUploadBean {

	/** 客户端mac地址 */
	public static final String	REQ_PARAM_MAC	= "mac";
	public static String[]		paramCheckArray	= { REQ_PARAM_MAC };

	private String				mac				= "";
	private int					machineId		= 0;

	public BaseUploadBean(){
		
	}
	
	public BaseUploadBean(HttpServletRequest request) {
		this.setMac(request.getParameter(REQ_PARAM_MAC));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[mac=").append(this.getMac());
		sb.append(",machinId=").append(this.getMachineId());
		sb.append("]");
		return sb.toString();
	}
	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getMachineId() {
		return machineId;
	}

	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}

}
