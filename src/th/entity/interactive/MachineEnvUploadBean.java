package th.entity.interactive;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import th.util.StringUtils;

public class MachineEnvUploadBean extends BaseUploadBean {

	public static final String	REQ_PARAM_MACHINETYPE		= "machinetype";
	public static final String	REQ_PARAM_MANUFACTUREDATE	= "manufacturedate";
	public static final String	REQ_PARAM_MANUFACTURER		= "manufacturer";
	public static final String	REQ_PARAM_DEVICENO			= "deviceno";
	public static final String	REQ_PARAM_MACHINE_KIND		= "machinekind";
	public static final String	REQ_PARAM_CPURATE			= "cpurate";
	public static final String	REQ_PARAM_OS				= "os";
	public static final String	REQ_PARAM_DISKSIZE			= "disksize";
	public static final String	REQ_PARAM_MEMERYSIZE		= "memerysize";
	public static final String	REQ_PARAM_VERSION			= "version";
	public static final String	REQ_PARAM_BROWSERNAME		= "browsername";
	public static final String	REQ_PARAM_BROWSERVERSION	= "browserversion";

	public static String[]		paramCheckArray				= { REQ_PARAM_MAC, REQ_PARAM_MACHINETYPE, REQ_PARAM_MANUFACTUREDATE,
			REQ_PARAM_MANUFACTURER, REQ_PARAM_DEVICENO,REQ_PARAM_MACHINE_KIND, REQ_PARAM_CPURATE, REQ_PARAM_OS, REQ_PARAM_DISKSIZE, REQ_PARAM_MEMERYSIZE, REQ_PARAM_VERSION,
			REQ_PARAM_BROWSERNAME, REQ_PARAM_BROWSERVERSION };

	public MachineEnvUploadBean() {

	}

	public MachineEnvUploadBean(HttpServletRequest request) {
		this.setMac(request.getParameter("mac"));
		machinetype = request.getParameter(REQ_PARAM_MACHINETYPE);
		manufacturedate = StringUtils.getYyyyMMddHHmmss(request.getParameter(REQ_PARAM_MANUFACTUREDATE).toString());
		manufacturer = request.getParameter(REQ_PARAM_MANUFACTURER);
		deviceno = request.getParameter(REQ_PARAM_DEVICENO);
		machineKind = request.getParameter(REQ_PARAM_MACHINE_KIND);
		cpurate = request.getParameter(REQ_PARAM_CPURATE);
		os = request.getParameter(REQ_PARAM_OS);
		disksize = StringUtils.getIntValue(request.getParameter(REQ_PARAM_DISKSIZE));
		memerysize = StringUtils.getIntValue(request.getParameter(REQ_PARAM_MEMERYSIZE));
		version = request.getParameter(REQ_PARAM_VERSION);
		browsername = request.getParameter(REQ_PARAM_BROWSERNAME);
		browserversion = request.getParameter(REQ_PARAM_BROWSERVERSION);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[mac=").append(this.getMac());
		sb.append(",machinetype=").append(machinetype);
		sb.append(",manufacturedate=").append(StringUtils.getYyyyMMddHHmmss(manufacturedate)/*manufacturedate*/);
		sb.append(",manufacturer=").append(manufacturer);
		sb.append(",deviceno=").append(deviceno);
//		sb.append(",freeserviceperiod=").append(freeserviceperiod);
		sb.append(",machineid=").append(this.getMachineId());
		sb.append(",cpurate=").append(cpurate);
		sb.append(",os=").append(os);
		sb.append(",disksize=").append(disksize);
		sb.append(",memerysize=").append(memerysize);
		sb.append(",version=").append(version);
		sb.append(",browsername=").append(browsername);
		sb.append(",browserversion=").append(browserversion);
		sb.append("]");
		return sb.toString();
	}

	private String	machinetype			= "";
	private Date	manufacturedate		= null;
	private String	manufacturer		= "";
	private String	deviceno			= "";
	private String	machineKind			= "";

//	private String	freeserviceperiod	= "";
	private String	cpurate				= "";
	private String	os					= "";

	private int		disksize			= 0;
	private int		memerysize			= 0;
	private String	version				= "";
	private String	browsername			= "";
	private String	browserversion		= "";


	/**
	 * @return the machinetype
	 */
	public String getMachinetype() {
		return machinetype;
	}

	/**
	 * @param machinetype the machinetype to set
	 */
	public void setMachinetype( String machinetype ) {
		this.machinetype = machinetype;
	}

	/**
	 * @return the manufacturedate
	 */
	public Date getManufacturedate() {
		return manufacturedate;
	}

	/**
	 * @param manufacturedate the manufacturedate to set
	 */
	public void setManufacturedate( Date manufacturedate ) {
		this.manufacturedate = manufacturedate;
	}

	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer( String manufacturer ) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the deviceno
	 */
	public String getDeviceno() {
		return deviceno;
	}

	/**
	 * @param deviceno the deviceno to set
	 */
	public void setDeviceno( String deviceno ) {
		this.deviceno = deviceno;
	}

	/**
	 * @return the machineKind
	 */
	public String getMachineKind() {
		return machineKind;
	}

	/**
	 * @param machineKind the machineKind to set
	 */
	public void setMachineKind( String machineKind ) {
		this.machineKind = machineKind;
	}

	/**
	 * @return the cpurate
	 */
	public String getCpurate() {
		return cpurate;
	}

	/**
	 * @param cpurate the cpurate to set
	 */
	public void setCpurate( String cpurate ) {
		this.cpurate = cpurate;
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
	 * @return the disksize
	 */
	public int getDisksize() {
		return disksize;
	}

	/**
	 * @param disksize the disksize to set
	 */
	public void setDisksize( int disksize ) {
		this.disksize = disksize;
	}

	/**
	 * @return the memerysize
	 */
	public int getMemerysize() {
		return memerysize;
	}

	/**
	 * @param memerysize the memerysize to set
	 */
	public void setMemerysize( int memerysize ) {
		this.memerysize = memerysize;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion( String version ) {
		this.version = version;
	}

	/**
	 * @return the browsername
	 */
	public String getBrowsername() {
		return browsername;
	}

	/**
	 * @param browsername the browsername to set
	 */
	public void setBrowsername( String browsername ) {
		this.browsername = browsername;
	}

	/**
	 * @return the browserversion
	 */
	public String getBrowserversion() {
		return browserversion;
	}

	/**
	 * @param browserversion the browserversion to set
	 */
	public void setBrowserversion( String browserversion ) {
		this.browserversion = browserversion;
	}

	public static void main(String[] args) {
		System.out.println(StringUtils.getYyyyMMddHHmmss("2000-11-12 11:11:11"));
	}
}
