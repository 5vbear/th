package th.entity.interactive;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import th.util.StringUtils;

public class PositionUploadBean extends BaseUploadBean {

	public static final String	REQ_PARAM_TIME		= "time";
	public static final String	REQ_PARAM_LATITUDE	= "Latitude";
	public static final String	REQ_PARAM_LONTITUDE	= "Lontitude";
	public static final String	REQ_PARAM_RADIUS	= "Radius";
	public static final String	REQ_PARAM_ADDRNAME	= "AddrName";

	public static String[]		paramCheckArray		= { REQ_PARAM_MAC, REQ_PARAM_TIME, REQ_PARAM_LATITUDE, REQ_PARAM_LONTITUDE, REQ_PARAM_RADIUS,
			REQ_PARAM_ADDRNAME						};

	public PositionUploadBean() {

	}

	public PositionUploadBean(HttpServletRequest request) {
		setMac(request.getParameter("mac"));
		latitude = StringUtils.getDoubleValue(request.getParameter(REQ_PARAM_LATITUDE));
		longitude = StringUtils.getDoubleValue(request.getParameter(REQ_PARAM_LONTITUDE));
		radius = StringUtils.getDoubleValue(request.getParameter(REQ_PARAM_RADIUS));
		time = StringUtils.getYyyyMMddHHmmssDate(request.getParameter(REQ_PARAM_TIME));
		addrName = request.getParameter(REQ_PARAM_ADDRNAME);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[mac=").append(this.getMac());
		sb.append(",time=").append(this.getTime());
		sb.append(",latitude=").append(latitude);
		sb.append(",longitude=").append(longitude);
		sb.append(",radius=").append(radius);
		sb.append(",addrName=").append(addrName);
		sb.append("]");
		return sb.toString();
	}

	private Date	time		= null;
	private double	latitude	= 0d;
	private double	longitude	= 0d;
	private double	radius		= 0d;
	private String	addrName	= "";

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getRadius() {
		return this.radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getAddrName() {
		return this.addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

}
