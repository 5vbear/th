/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.util.apk;

/**
 * Descriptions
 * 
 * @version 2014-1-21
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ApkInfo {
	private String packageName;
	private String version;
	private String versionCode;

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName( String packageName ) {
		this.packageName = packageName;
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

	/**
	 * @return the versionCode
	 */
	public String getVersionCode() {
		return versionCode;
	}

	/**
	 * @param versionCode the versionCode to set
	 */
	public void setVersionCode( String versionCode ) {
		this.versionCode = versionCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApkInfo [packageName=" + packageName + ", version=" + version + ", versionCode=" + versionCode + "]";
	}


}
