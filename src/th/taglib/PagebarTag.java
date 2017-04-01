package th.taglib;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class PagebarTag extends TagSupport {

	private static final long	serialVersionUID	= -5799306106964257669L;

	private Pager				pager				= null;

	private String				url					= null;

	private String				conditionStr		= null;

	public int doStartTag() {

		try {

			StringBuffer str = new StringBuffer();

			if (pager == null) {
				return super.SKIP_BODY;
			}

			str.append("共").append(pager.getTotalRows()).append("条　每页").append(pager.getPageSize()).append("条　第").append(pager.getCurrentPage()).append("页 / 共").append(pager.getTotalPages()).append("页 ");

			if (pager.getCurrentPage() == 1) {
				str.append("[首页] ");
				str.append("[上一页] ");
			}
			if (pager.getCurrentPage() != 1) {
				str.append("[<a href='").append(url).append(buildRequestParameter(1)).append("'>首页</a>] ");
				str.append("[<a href='").append(url).append(buildRequestParameter(pager.getCurrentPage() - 1)).append("'>上一页</a>] ");
			}

			for (int i = pager.getCurrentPage() - 3; i <= pager.getCurrentPage() + 3; i++) {

				if (i <= 0 || i > pager.getTotalPages()) {
					continue;
				}

				if (i == pager.getCurrentPage()) {
					str.append(" [<span style='color:#E2144A; font-weight:bold; width:15px;text-align: center;'> ").append(i).append(" </span>] ");
				} else {
					str.append(" [<a href='").append(url).append(buildRequestParameter(i)).append("'> ").append(i).append(" </a>] ");
				}
			}

			if (pager.getCurrentPage() == pager.getTotalPages() || pager.getTotalPages() == 0) {
				str.append(" [下一页]");
				str.append(" [末页]");
			}
			if (pager.getCurrentPage() != pager.getTotalPages() && pager.getTotalPages() != 0) {
				str.append(" [<a href='").append(url).append(buildRequestParameter(pager.getCurrentPage() + 1)).append("'>下一页</a>]");
				str.append(" [<a href='").append(url).append(buildRequestParameter(pager.getTotalPages())).append("'>末页</a>]");
			}

			JspWriter out = pageContext.getOut();
			out.print(str.toString());
			out.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.SKIP_BODY;
	}

	private String buildRequestParameter(int curPage) {
		return "?currPage=" + curPage + conditionStr;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the conditionStr
	 */
	public String getConditionStr() {
		return this.conditionStr;
	}

	/**
	 * @param conditionStr
	 *            the conditionStr to set
	 */
	public void setConditionStr(String conditionStr) {
		this.conditionStr = conditionStr;
	}

}
