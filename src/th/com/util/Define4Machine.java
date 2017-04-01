package th.com.util;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public final class Define4Machine {

	//端机审核首页画面
	public static final String JSP_MACHINE_AUDIT = "/jsp/machine/macAudit.jsp";
	//端机审核首页画面
	public static final String JSP_MACHINE_BATCH_AUDIT = "/jsp/machine/macBatchAudit.jsp";
	//端机审核list画面
	public static final String JSP_MACHINE_AUDIT_LIST = "/jsp/machine/macAuditList.jsp";
	//端机审核查询url
	public static final String SERVLET_MACHINE_AUDIT_QUERY = "/machineServlet?model=audit&method=maclistQry&pageIdx=1";
	//端机审核查询url
	public static final String SERVLET_MACHINE_AUDIT = "/machineServlet?model=audit&method=enter&pageIdx=1";

	//端机指令首页画面
	public static final String JSP_MACHINE_COMMAND = "/jsp/machine/macCommand.jsp";
	//端机指令画面
	public static final String JSP_MACHINE_COMMAND_OPER = "/jsp/machine/macCommandOper.jsp";
	//端机指令画面
	public static final String JSP_MACHINE_COMMAND_MACVIEW = "/jsp/machine/macCommandMacView.jsp";;
	//分行指令画面
	public static final String JSP_MACHINE_COMMAND_ORGVIEW = "/jsp/machine/macCommandOrgView.jsp";
	//分行指令画面
	public static final String JSP_MACHINE_COMMAND_TYPEVIEW = "/jsp/machine/macCommandTypeView.jsp";
	//端机指令url
	public static final String SERVLET_MACHINE_COMMAND_MAC = "/machineServlet?model=command&method=macOperView&macIDStd=";
	//端机指令url
	public static final String SERVLET_MACHINE_COMMAND_ORG = "/machineServlet?model=command&method=orgOperView&orgIDStd=";

	//端机部署首页画面
	public static final String JSP_MACHINE_DEPLOY_LIST = "/jsp/machine/macDeployList.jsp";
	//端机部署显示画面
	public static final String JSP_MACHINE_DEPLOY_VIEW = "/jsp/machine/macDeployView.jsp";
	//端机部署url
	public static final String SERVLET_MACHINE_DEPLOY_LIST = "/machineServlet?model=deploy&method=query&pageIdx=";
	//端机部署url
	public static final String SERVLET_MACHINE_DEPLOY_VIEW = "/machineServlet?model=deploy&method=view&pageIdx=1";

	//端机信息首页画面
	public static final String JSP_MACHINE_INFO = "/jsp/machine/macInfo.jsp";
	//端机信息显示画面
	public static final String JSP_MACHINE_INFO_VIEW = "/jsp/machine/macInfoView.jsp";
	//端机信息显示画面
	public static final String JSP_MACHINE_INFO_BLANK = "/jsp/machine/macInfoBlank.jsp";
	
	//端机同步首页画面
	public static final String JSP_MACHINE_SYCH_INFO = "/jsp/machine/macSych.jsp";
	//端机同步信息画面
	public static final String JSP_MACHINE_SYCH_VIEW = "/jsp/machine/macSychView.jsp";
	//端机同步显示画面
	public static final String JSP_MACHINE_SYCH_BLANK = "/jsp/machine/macSychBlank.jsp";

	//端机配置首页画面
	public static final String JSP_MACHINE_CONFIG = "/jsp/machine/macConfig.jsp";
	//端机配置列表画面
	public static final String JSP_MACHINE_CONFIG_LIST = "/jsp/machine/macConfigList.jsp";
	//端机配置添加画面
	public static final String JSP_MACHINE_CONFIG_ADD = "/jsp/machine/macConfigAdd.jsp";
	//端机配置修改画面
	public static final String JSP_MACHINE_CONFIG_BLANK = "/jsp/machine/macConfigBlank.jsp";
	//端机配置修改画面
	public static final String JSP_MACHINE_CONFIG_EDIT = "/jsp/machine/macConfigEdit.jsp";
	//端机配置url
	public static final String SERVLET_MACHINE_CONFIG_LIST = "/machineServlet?model=config&method=cfgListQry&pageIdx=";
	//端机配置Edit url
	public static final String SERVLET_MACHINE_CONFIG_EDIT = "/machineServlet?model=config&method=goEdit&orgid=";
	//端机配置url
	public static final String SERVLET_MACHINE_CONFIG_VIEW = "/machineServlet?model=deploy&method=view&pageIdx=1";

	//端机告警首页画面
	public static final String JSP_MACHINE_ALERT_LIST = "/jsp/machine/macAlertList.jsp";
	//端机告警详细信息
	public static final String JSP_MACHINE_ALERT_DETAIL_INFO = "/jsp/machine/macAlertDetailInfo.jsp";
	//端机告警url
	public static final String SERVLET_MACHINE_ALERT_LIST = "/machineServlet?model=alert&method=query&operType=";

	//故障知识库列表画面
	public static final String JSP_MACHINE_FAQ_LIST = "/jsp/machine/macFaqList.jsp";
	//故障知识库查看画面
	public static final String JSP_MACHINE_FAQ_VIEW = "/jsp/machine/macFaqView.jsp";
	//故障知识库更新画面
	public static final String JSP_MACHINE_FAQ_UPDATE = "/jsp/machine/macFaqUpdate.jsp";
	//派修管理列表画面
	public static final String JSP_MACHINE_REPAIR_LIST = "/jsp/machine/macRepairList.jsp";
	//派修管理查看画面
	public static final String JSP_MACHINE_REPAIR_VIEW = "/jsp/machine/macRepairView.jsp";
	//派修管理更新画面
	public static final String JSP_MACHINE_REPAIR_UPDATE = "/jsp/machine/macRepairUpdate.jsp";
	//配置数字字典列表画面URL
	public static final String SERVLET_MACHINE_DICT_LIST = "/machineServlet?model=dict&method=query&pageIdx=";
	//配置数字字典列表画面
	public static final String JSP_MACHINE_DICT_LIST = "/jsp/machine/macDictList.jsp";
	//配置数字字典添加画面
	public static final String JSP_MACHINE_DICT_ADD = "/jsp/machine/macDictAdd.jsp";
	//配置数字字典编辑画面
	public static final String JSP_MACHINE_DICT_EDIT = "/jsp/machine/macDictEdit.jsp";
	
	//端机指令画面
	public static final String JSP_MACHINE_INVALID = "/jsp/common/noaction.jsp";

	//树中的macID前缀
	public static final String TREE_MACID_PREFIX = "000000000000000";
}