package th.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import th.action.report.ReportCommonAction;
import th.com.util.Define;
import th.com.util.Define4Report;
import th.dao.LogListDao;
import th.dao.log.AdviceDao;
import th.dao.log.MessageDao;
import th.entity.LogListBean;
import th.entity.log.AdviceBean;
import th.entity.log.MessageBean;
import th.util.StringUtils;

public class LogListAction extends BaseAction {

	protected HttpServletRequest req;
	protected HttpServletResponse res;
	protected String pageId;

	protected Logger logger = Logger.getLogger(this.getClass());

	public LogListAction() {

	}

	public LogListAction(HttpServletRequest req, HttpServletResponse res) {
		this.req = req;
		this.res = res;
		this.pageId = req.getParameter("pageId");
	}

	public void advertisingPlaySearch(HttpServletRequest req) {

		LogListBean bean = new LogListBean();

		// 节目单
		String bill_name = req.getParameter("bill_name");
		bean.setBill_name(bill_name);

		// 端机标识
		String Machine_mark = req.getParameter("Machine_mark");
		bean.setMachine_mark(Machine_mark);

		// 检索时间(开始时间)
		String search_time_start = req.getParameter("search_time_start");
		bean.setSearch_time_start(search_time_start);

		// 检索时间(结束时间)
		String search_time_end = req.getParameter("search_time_end");
		bean.setSearch_time_end(search_time_end);

		// 检索结果当前位置
		if ("".equals(req.getParameter("point_num"))
				|| null == req.getParameter("point_num")) {
			bean.setPoint_num(1);
		} else {
			bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		}

		// 返回结果
		List<LogListBean> resultBeans = new ArrayList<LogListBean>();

		LogListDao dao = new LogListDao();
		HashMap<String, String>[] advertisingBeans = dao
				.searchAdvertising(bean);

		if (advertisingBeans != null) {
			// 检索结果开始位置
			int start_p = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			// 检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;

			int loop = 0;
			for (HashMap<String, String> itemMap : advertisingBeans) {
				loop++;
				if (loop <= start_p) {
					continue;
				}
				if (loop > start_p && loop <= end_p) {
					start_p++;
					LogListBean logListBean = new LogListBean();
					// 节目单
					logListBean.setBill_name(itemMap.get("BILL_NAME"));
					// 端机标识
					logListBean.setMachine_mark(itemMap.get("MACHINE_MARK"));
					// 状态时间
					logListBean.setStart_play_time(itemMap
							.get("START_PLAY_TIME"));

					resultBeans.add(logListBean);
				}
			}
			// 检索总行数
			bean.setTotal_num(advertisingBeans.length);

			// 检索条件保存
			req.setAttribute("select_object", bean);
			// 检索结果保存
			req.setAttribute("resultList", resultBeans);
		}

	}

	public void advertisingClickSearch(HttpServletRequest req) {

		LogListBean bean = new LogListBean();

		// 节目单
		String bill_name = req.getParameter("bill_name");
		bean.setBill_name(bill_name);

		// 素材
		String media_file_name = req.getParameter("media_file_name");
		bean.setMedia_file_name(media_file_name);

		// 检索时间(开始时间)
		String search_time_start = req.getParameter("search_time_start");
		bean.setSearch_time_start(search_time_start);

		// 检索时间(结束时间)
		String search_time_end = req.getParameter("search_time_end");
		bean.setSearch_time_end(search_time_end);

		// 检索结果当前位置
		if ("".equals(req.getParameter("point_num"))
				|| null == req.getParameter("point_num")) {
			bean.setPoint_num(1);
		} else {
			bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		}

		// 返回结果
		List<LogListBean> resultBeans = new ArrayList<LogListBean>();

		LogListDao dao = new LogListDao();
		HashMap<String, String>[] advertisingClickBeans = dao
				.searchAdvertisingClickLog(bean);

		if (advertisingClickBeans != null) {
			// 检索结果开始位置
			int start_p = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			// 检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;

			int loop = 0;
			for (HashMap<String, String> itemMap : advertisingClickBeans) {
				loop++;
				if (loop <= start_p) {
					continue;
				}
				if (loop > start_p && loop <= end_p) {
					start_p++;
					LogListBean logListBean = new LogListBean();
					// 节目单
					logListBean.setBill_name(itemMap.get("BILL_NAME"));
					// 布局
					logListBean.setLayout_name(itemMap.get("LAYOUT_NAME"));
					// 素材
					logListBean.setMedia_file_name(itemMap
							.get("MEDIA_FILE_NAME"));
					// 端机标识
					logListBean.setMachine_mark(itemMap.get("MACHINE_MARK"));
					// 点击时间
					logListBean.setClick_time(itemMap.get("CLICK_TIME"));

					resultBeans.add(logListBean);
				}
			}
			// 检索总行数
			bean.setTotal_num(advertisingClickBeans.length);

			// 检索条件保存
			req.setAttribute("select_object", bean);
			// 检索结果保存
			req.setAttribute("resultList", resultBeans);
		}

	}

	public void adviceSearch(HttpServletRequest req) {
		Integer pageNume = null;
		logger.info("req.getParameter(" + "pageNumber+" + ") is: "
				+ req.getParameter("pageNumber"));
		if (req.getParameter("pageNumber") == null
				|| req.getParameter("pageNumber").equals("")) {
			pageNume = 1;
		} else {
			pageNume = Integer.parseInt(req.getParameter("pageNumber"));
		}
		String gID = null;

		String selectedOrgId = (String) req.getParameter("orgSelect");

		selectedOrgId = StringUtils.isBlank(selectedOrgId) ? "1"
				: selectedOrgId;

		logger.info("需要检索的GID： " + selectedOrgId);

		List<AdviceBean> resultBeans = new ArrayList<AdviceBean>();
		AdviceDao adviceDao = new AdviceDao();
		String machineID = req.getParameter("machinename");
		HashMap<String, String>[] result = null;
		if (StringUtils.isBlank(machineID)) {
			result = adviceDao.searchAdviceList(selectedOrgId, null);
		} else {
			result = adviceDao.searchAdviceList(selectedOrgId, machineID);
		}

		if (result != null) {
			// 检索结果开始位置
			int start_p = (pageNume - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			// 检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;
			int loop = 0;
			for (HashMap<String, String> itemMap : result) {
				loop++;
				if (loop <= start_p) {
					continue;
				}
				if (loop > start_p && loop <= end_p) {
					start_p++;
					AdviceBean adviceBean = new AdviceBean();
					adviceBean.setType(itemMap.get("IDEA_TYPE"));
					adviceBean.setMachineName(itemMap.get("MACHINE_MARK"));
					adviceBean.setOrgName(itemMap.get("ORG_NAME"));
					adviceBean.setContent(itemMap.get("IDEA_CONTENT"));
					adviceBean.setExpression(itemMap.get("EXPRESSION"));
					adviceBean.setTelephone(itemMap.get("PHONE"));
					adviceBean.setEmail(itemMap.get("EMAIL"));
					adviceBean.setTime(itemMap.get("OPERATETIME"));
					resultBeans.add(adviceBean);
				}
			}
			// 检索结果保存
			int totalPageCount = 0;
			try {
				totalPageCount = ReportCommonAction
						.getTotalPageCount(result.length);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			req.setAttribute("viewList", resultBeans);
			req.setAttribute(Define4Report.REQ_PARAM_PAGE_NUMBER, pageNume + "");
			req.setAttribute(Define4Report.REQ_PARAM_TOTAL_PAGE_COUNT,
					totalPageCount + "");
			req.setAttribute("total_num", result.length + "");
		}

	}

	public void messsageSearch(HttpServletRequest req) {
		Integer pageNume = null;
		logger.info("req.getParameter(" + "pageNumber+" + ") is: "
				+ req.getParameter("pageNumber"));
		if (req.getParameter("pageNumber") == null
				|| req.getParameter("pageNumber").equals("")) {
			pageNume = 1;
		} else {
			pageNume = Integer.parseInt(req.getParameter("pageNumber"));
		}
		String gID = null;

		String selectedOrgId = (String) req.getParameter("orgSelect");

		selectedOrgId = StringUtils.isBlank(selectedOrgId) ? "1"
				: selectedOrgId;

		logger.info("需要检索的GID： " + selectedOrgId);

		List<MessageBean> resultBeans = new ArrayList<MessageBean>();
		MessageDao messageeDao = new MessageDao();
		String machineID = req.getParameter("machinename");
		String sortType = req.getParameter("sortType");
		HashMap<String, String>[] result = null;
		if (StringUtils.isBlank(machineID)) {
			result = messageeDao.searchMesssageList(selectedOrgId,null, sortType);
		} else {
			result = messageeDao.searchMesssageList(selectedOrgId, machineID, sortType);
		}

		if (result != null) {
			// 检索结果开始位置
			int start_p = (pageNume - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			// 检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;
			int loop = 0;
			for (HashMap<String, String> itemMap : result) {
				loop++;
				if (loop <= start_p) {
					continue;
				}
				if (loop > start_p && loop <= end_p) {
					start_p++;
					MessageBean messageBean = new MessageBean();
					messageBean.setOperator(itemMap.get("NAME"));
					messageBean.setMachineName(itemMap.get("MACHINE_MARK"));
					messageBean.setOrgName(itemMap.get("ORG_NAME"));
					messageBean.setContent(itemMap.get("MSG_CONTENT"));
					messageBean.setTime(itemMap.get("OPERATETIME"));
					resultBeans.add(messageBean);
				}
			}
			// 检索结果保存
			req.setAttribute("viewList", resultBeans);
			req.setAttribute(Define4Report.REQ_PARAM_PAGE_NUMBER, pageNume + "");
			req.setAttribute("sortType", sortType + "");
			int totalPageCount = 0;
			try {
				totalPageCount = ReportCommonAction
						.getTotalPageCount(result.length);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			req.setAttribute(Define4Report.REQ_PARAM_TOTAL_PAGE_COUNT,
					totalPageCount + "");
			req.setAttribute("total_num", result.length + "");
		}

	}

	public void systemOperationSearch(HttpServletRequest req) {

		LogListBean bean = new LogListBean();

		// 操作用户
		String operation_user = req.getParameter("operation_user");
		bean.setOperation_user(operation_user);

		// 操作类型
		String operation_type = req.getParameter("operation_type");
		bean.setOperation_type(operation_type);

		// 检索时间(开始时间)
		String search_time_start = req.getParameter("search_time_start");
		bean.setSearch_time_start(search_time_start);

		// 检索时间(结束时间)
		String search_time_end = req.getParameter("search_time_end");
		bean.setSearch_time_end(search_time_end);

		// 检索结果当前位置
		if ("".equals(req.getParameter("point_num"))
				|| null == req.getParameter("point_num")) {
			bean.setPoint_num(1);
		} else {
			bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		}

		// 返回结果
		List<LogListBean> resultBeans = new ArrayList<LogListBean>();

		LogListDao dao = new LogListDao();
		HashMap<String, String>[] systemOperationBeans = dao
				.systemOperationSearchLog(bean);

		if (systemOperationBeans != null) {
			// 检索结果开始位置
			int start_p = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			// 检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;

			int loop = 0;
			for (HashMap<String, String> itemMap : systemOperationBeans) {
				loop++;
				if (loop <= start_p) {
					continue;
				}
				if (loop > start_p && loop <= end_p) {
					start_p++;
					LogListBean logListBean = new LogListBean();
					// 操作用户
					logListBean
							.setOperation_user(itemMap.get("OPERATION_USER"));
					// 操作类型
					logListBean
							.setOperation_type(itemMap.get("OPERATION_TYPE"));
					// 操作时间
					logListBean
							.setOperation_time(itemMap.get("OPERATION_TIME"));
					// 操作描述
					logListBean.setOperation_description("["
							+ itemMap.get("MODULE_NAME") + "]"
							+ itemMap.get("OPERATION_DESCRIPTION"));

					resultBeans.add(logListBean);
				}
			}
			// 检索总行数
			bean.setTotal_num(systemOperationBeans.length);

			// 检索条件保存
			req.setAttribute("select_object", bean);
			// 检索结果保存
			req.setAttribute("resultList", resultBeans);
		}

	}

}
