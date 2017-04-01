package th.entity.interactive;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import th.util.StringUtils;

public class MachineUseHistoryUploadBean extends BaseUploadBean {

	public static final String	REQ_PARAM_CLICKTIMES		= "clicktimes";
	public static final String	REQ_PARAM_ACCESSTIME		= "accesstime";
	public static final String	REQ_PARAM_STATISTICSDATE	= "statisticsdate";

	public static String[]		paramCheckArray				= { REQ_PARAM_MAC, REQ_PARAM_CLICKTIMES, REQ_PARAM_ACCESSTIME,REQ_PARAM_STATISTICSDATE };

	public MachineUseHistoryUploadBean() {

	}

	public MachineUseHistoryUploadBean(HttpServletRequest request) {
		setMac(request.getParameter("mac"));
		clicktimes = StringUtils.getIntValue(request.getParameter(REQ_PARAM_CLICKTIMES));
		accesstime = StringUtils.getIntValue(request.getParameter(REQ_PARAM_ACCESSTIME));
		statisticsdate = StringUtils.getYyyyMMdd(request.getParameter(REQ_PARAM_STATISTICSDATE));
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[mac=").append(this.getMac());
		sb.append(",machinId=").append(this.getMachineId());
		sb.append(",clicktimes=").append(clicktimes);
		sb.append(",accesstime=").append(accesstime);
		sb.append(",statisticsdate=").append(statisticsdate);
		sb.append("]");
		return sb.toString();
	}

	private int		clicktimes		= 0;
	private int		accesstime		= 0;
	private Date	statisticsdate	= null;

	private int		runningTime		= 0;

	public int getClicktimes() {
		return clicktimes;
	}

	public void setClicktimes(int clicktimes) {
		this.clicktimes = clicktimes;
	}

	public int getAccesstime() {
		return accesstime;
	}

	public void setAccesstime(int accesstime) {
		this.accesstime = accesstime;
	}

	public int getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}

	
	public Date getStatisticsdate() {
		return this.statisticsdate;
	}

	
	public void setStatisticsdate(Date statisticsdate) {
		this.statisticsdate = statisticsdate;
	}

}
