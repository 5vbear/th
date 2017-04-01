/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2013-8-27
 * @author PSET
 * @since JDK1.6
 *
 */
public class UpdateHistorySearchBean {
	
	private long macId;
	private String macMark;
	private long fileId;
	private String fileName;
	private String startTime;
	private String endTime;
	
	public UpdateHistorySearchBean(){
		this.macId = -1;
		this.macMark = "";
		this.fileId = -1;
		this.fileName = "";
		this.startTime = "";
		this.endTime = "";
	}
	
	/**
	 * @return the macId
	 */
	public long getMacId() {
		return macId;
	}

	/**
	 * @param macId the macId to set
	 */
	public void setMacId( long macId ) {
		this.macId = macId;
	}
	
	/**
	 * @return the macMark
	 */
	public String getMacMark() {
		return macMark;
	}

	/**
	 * @param macMark the macMark to set
	 */
	public void setMacMark( String macMark ) {
		this.macMark = macMark;
	}

	/**
	 * @return the fileId
	 */
	public long getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId( long fileId ) {
		this.fileId = fileId;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName( String fileName ) {
		this.fileName = fileName;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime( String startTime ) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime( String endTime ) {
		this.endTime = endTime;
	}


}
