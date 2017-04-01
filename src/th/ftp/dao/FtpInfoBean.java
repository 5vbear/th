package th.ftp.dao;

public class FtpInfoBean {
	private Long ftpMangId;
	private String ftpuser;
	private String maxDownloadSpeed;
	private String maxUploadSpeed;

	public void setFtpMangId(Long ftpMangId) {
		this.ftpMangId = ftpMangId;
	}

	public Long getFtpMangId() {
		return ftpMangId;
	}

	public String getFtpuser() {
		return ftpuser;
	}

	public void setFtpuser(String ftpuser) {
		this.ftpuser = ftpuser;
	}

	public String getMaxDownloadSpeed() {
		return maxDownloadSpeed;
	}

	public void setMaxDownloadSpeed(String maxDownloadSpeed) {
		this.maxDownloadSpeed = maxDownloadSpeed;
	}

	public String getMaxUploadSpeed() {
		return maxUploadSpeed;
	}

	public void setMaxUploadSpeed(String maxUploadSpeed) {
		this.maxUploadSpeed = maxUploadSpeed;
	}

}
