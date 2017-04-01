package th.entity.interactive;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import th.util.StringUtils;

public class MonitorCommonUploadBean extends BaseUploadBean {

	public static final String	REQ_PARAM_TIME			= "time";
	public static final String	REQ_PARAM_CPULOAD		= "cpuload";
	public static final String	REQ_PARAM_MEMORYLOAD	= "memoryload";
	public static final String	REQ_PARAM_USEDDISK		= "useddisk";
	public static final String	REQ_PARAM_UNUSEDDISK	= "unuseddisk";
	public static final String	REQ_PARAM_UPLOADRATE	= "uploadrate";
	public static final String	REQ_PARAM_DOWNLOADRATE	= "downloadrate";
//	public static final String	REQ_PARAM_STATUS		= "status";

	public static String[]		paramCheckArray			= { REQ_PARAM_MAC, REQ_PARAM_TIME, REQ_PARAM_CPULOAD, REQ_PARAM_MEMORYLOAD,
			REQ_PARAM_USEDDISK, REQ_PARAM_UNUSEDDISK, REQ_PARAM_UPLOADRATE, REQ_PARAM_DOWNLOADRATE/*, REQ_PARAM_STATUS*/ };

	public MonitorCommonUploadBean() {

	}

	public MonitorCommonUploadBean(HttpServletRequest request) {
		setMac(request.getParameter(REQ_PARAM_MAC));
		time = StringUtils.getYyyyMMddHHmmss(request.getParameter(REQ_PARAM_TIME));
		cpuload = StringUtils.getFloatValue(request.getParameter(REQ_PARAM_CPULOAD));
		memoryload = StringUtils.getFloatValue(request.getParameter(REQ_PARAM_MEMORYLOAD));
		useddisk = StringUtils.getFloatValue(request.getParameter(REQ_PARAM_USEDDISK));
		unuseddisk = StringUtils.getFloatValue(request.getParameter(REQ_PARAM_UNUSEDDISK));
		uploadrate = StringUtils.getFloatValue(request.getParameter(REQ_PARAM_UPLOADRATE));
		downloadrate = StringUtils.getFloatValue(request.getParameter(REQ_PARAM_DOWNLOADRATE));
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[mac=").append(this.getMac());
		sb.append(",machinId=").append(this.getMachineId());
		sb.append(",time=").append(time);
		sb.append(",cpuload=").append(cpuload);
		sb.append(",memoryload=").append(memoryload);
		sb.append(",useddisk=").append(useddisk);
		sb.append(",unuseddisk=").append(unuseddisk);
		sb.append(",uploadrate=").append(uploadrate);
		sb.append(",downloadrate=").append(downloadrate);
		sb.append("]");
		return sb.toString();
	}

	private Date	time			= null;
	private float	cpuload			= 0f;
	private float	memoryload		= 0f;
	private float	useddisk		= 0f;
	private float	unuseddisk		= 0f;
	private float	uploadrate		= 0f;
	private float	downloadrate	= 0f;

	// private String status = "";

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public float getCpuload() {
		return cpuload;
	}

	public void setCpuload(float cpuload) {
		this.cpuload = cpuload;
	}

	public float getMemoryload() {
		return memoryload;
	}

	public void setMemoryload(float memoryload) {
		this.memoryload = memoryload;
	}

	public float getUseddisk() {
		return useddisk;
	}

	public void setUseddisk(float useddisk) {
		this.useddisk = useddisk;
	}

	public float getUnuseddisk() {
		return unuseddisk;
	}

	public void setUnuseddisk(float unuseddisk) {
		this.unuseddisk = unuseddisk;
	}

	public float getUploadrate() {
		return uploadrate;
	}

	public void setUploadrate(float uploadrate) {
		this.uploadrate = uploadrate;
	}

	public float getDownloadrate() {
		return downloadrate;
	}

	public void setDownloadrate(float downloadrate) {
		this.downloadrate = downloadrate;
	}

}
