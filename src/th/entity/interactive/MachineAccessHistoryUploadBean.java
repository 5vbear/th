package th.entity.interactive;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import th.util.StringUtils;

/**
 * Descriptions
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 */
public class MachineAccessHistoryUploadBean extends BaseUploadBean {

	public static final String	REQ_PARAM_ACCESSTOTALTIMES	= "accesstotaltimes";
	public static final String	REQ_PARAM_ILLEGALVISITTIMES	= "illegalvisittimes";
	public static final String	REQ_PARAM_STATISTICSDATE	= "statisticsdate";

	public static String[]		paramCheckArray				= { REQ_PARAM_MAC, REQ_PARAM_ACCESSTOTALTIMES, REQ_PARAM_ILLEGALVISITTIMES,REQ_PARAM_STATISTICSDATE };

	public MachineAccessHistoryUploadBean() {

	}

	public MachineAccessHistoryUploadBean(HttpServletRequest request) {
		this.setMac(request.getParameter(REQ_PARAM_MAC));
		accesstotaltimes = StringUtils.getIntValue(request.getParameter(REQ_PARAM_ACCESSTOTALTIMES));
		illegalvisittimes = StringUtils.getIntValue(request.getParameter(REQ_PARAM_ILLEGALVISITTIMES));
		statisticsdate = StringUtils.getYyyyMMdd(request.getParameter(REQ_PARAM_STATISTICSDATE));
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[mac=").append(this.getMac());
		sb.append(",machinId=").append(this.getMachineId());
		sb.append(",accesstotaltimes=").append(accesstotaltimes);
		sb.append(",illegalvisittimes=").append(illegalvisittimes);
		sb.append(",statisticsdate=").append(statisticsdate);
		sb.append("]");
		return sb.toString();
	}

	private int		accesstotaltimes	= 0;
	private int		illegalvisittimes	= 0;
	private Date	statisticsdate		= null;

	public int getAccesstotaltimes() {
		return accesstotaltimes;
	}

	public void setAccesstotaltimes(int accesstotaltimes) {
		this.accesstotaltimes = accesstotaltimes;
	}

	public int getIllegalvisittimes() {
		return illegalvisittimes;
	}

	public void setIllegalvisittimes(int illegalvisittimes) {
		this.illegalvisittimes = illegalvisittimes;
	}

	public Date getStatisticsdate() {
		return this.statisticsdate;
	}
	
	public void setStatisticsdate(Date statisticsdate) {
		this.statisticsdate = statisticsdate;
	}

}
