package th.taglib;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class PagerHelper {

	public static Pager getPager(int totalRows, int pageSize, HttpServletRequest request) {

		Pager pager = new Pager(totalRows, pageSize);
		int currPage = NumberUtils.toInt(StringUtils.defaultIfEmpty(request.getParameter("currPage"), "1"));
		pager.setStart(currPage);
		return pager;
	}
}
