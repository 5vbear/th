/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 * 
 * @version 2013-8-14
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ClientBean {

	private String machineName;
	private String cpuFrequency;
	private String os;
	private String diskMemory;
	private String ramMemory;
	private String version;
	/** 浏览器信息 */  
    private String browserInfo;   
    /** 操作系统信息 */  
    private String osInfo; 
    private String macType;

	/**
	 * @return the machineName
	 */
	public String getMachineName() {
		return machineName;
	}

	/**
	 * @param machineName
	 *            the machineName to set
	 */
	public void setMachineName( String machineName ) {
		this.machineName = machineName;
	}

	/**
	 * @return the cpuFrequency
	 */
	public String getCpuFrequency() {
		return cpuFrequency;
	}

	/**
	 * @param cpuFrequency
	 *            the cpuFrequency to set
	 */
	public void setCpuFrequency( String cpuFrequency ) {
		this.cpuFrequency = cpuFrequency;
	}

	/**
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * @param os
	 *            the os to set
	 */
	public void setOs( String os ) {
		this.os = os;
	}

	/**
	 * @return the diskMemory
	 */
	public String getDiskMemory() {
		return diskMemory;
	}

	/**
	 * @param diskMemory
	 *            the diskMemory to set
	 */
	public void setDiskMemory( String diskMemory ) {
		this.diskMemory = diskMemory;
	}

	/**
	 * @return the ramMemory
	 */
	public String getRamMemory() {
		return ramMemory;
	}

	/**
	 * @param ramMemory
	 *            the ramMemory to set
	 */
	public void setRamMemory( String ramMemory ) {
		this.ramMemory = ramMemory;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion( String version ) {
		this.version = version;
	}

	public String getBrowserInfo() {
		return browserInfo;
	}

	public void setBrowserInfo(String browserInfo) {
		this.browserInfo = browserInfo;
	}

	public String getOsInfo() {
		return osInfo;
	}

	public void setOsInfo(String osInfo) {
		this.osInfo = osInfo;
	}

	public String getMacType() {
		return macType;
	}

	public void setMacType(String macType) {
		this.macType = macType;
	}
}
