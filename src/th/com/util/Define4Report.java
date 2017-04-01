/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.com.util;

/**
 * Descriptions
 * 
 * @version 2013-8-20
 * @author PSET
 * @since JDK1.6
 * 
 */
public class Define4Report {
	
	/* Request Parameter Name */
	public static final String REQ_PARAM_PREPORT_PAGE = "reportPage";
	public static final String REQ_PARAM_FUNCTION_CODE = "functionCode";
	public static final String REQ_PARAM_DATA_TYPE = "dataType";
	public static final String REQ_PARAM_TIME_TYPE = "timeType";
	public static final String REQ_PARAM_MAC_MARK = "macMark";
	public static final String REQ_PARAM_USE_NUM = "useNum";
	public static final String REQ_PARAM_ORG_SELECT = "orgSelect";
	public static final String REQ_PARAM_DAY_TYPE_TIME = "dayTypeTime";
	public static final String REQ_PARAM_WEEK_TYPE_TIME = "weekTypeTime";
	public static final String REQ_PARAM_MONTH_TYPE_TIME = "monthTypeTime";
	public static final String REQ_PARAM_ANY_TYPE_START_TIME = "anyTypeStartTime";
	public static final String REQ_PARAM_ANY_TYPE_END_TIME = "anyTypeEndTime";
	public static final String REQ_PARAM_SORT_KEY = "sortKey";
	public static final String REQ_PARAM_DETAIL_SORT_KEY = "detailSortKey";
	public static final String REQ_PARAM_PAGE_NUMBER = "pageNumber";
	public static final String REQ_PARAM_MAC_TYPE = "macType";
	public static final String REQ_PARAM_TOTAL_PAGE_COUNT = "totalPageCount";
	public static final String REQ_PARAM_SELECTED_ORG_ID = "selectedOrgId";
	public static final String REQ_PARAM_EXPORT_BYTES = "exportBytes";
	public static final String REQ_PARAM_EXPORT_FILE_NAME = "fileName";
	public static final String REQ_PARAM_PROGRAMlIST_NAME="programListName";
	public static final String REQ_PARAM_MATERIAL_NAME="materialName";
	public static final String REQ_PARAM_LAYOUT_NAME="layoutName";
	public static final String REQ_PARAM_AWARD_NAME = "awardName";
	public static final String REQ_PARAM_AWARD_USERNAME = "userName";
	public static final String REQ_PARAM_AWARD_PHONE = "phone";

	/* Excel导出*/
	/** 模板文件名（开机率检索汇总） */
	public static final String EXPORT_TEMPLATE_NAME_MOR_SUMMARY = "machine_open_rate_summary_template.xls";
	/** 模板文件名（开机率检索明细） */
	public static final String EXPORT_TEMPLATE_NAME_MOR_DETAIL = "machine_open_rate_detail_template.xls";
	/** 模板文件名（频道使用汇总） */
	public static final String EXPORT_TEMPLATE_NAME_CU_SUMMARY = "channel_use_summary_template.xls";
	/** 模板文件名（频道使用明细） */
	public static final String EXPORT_TEMPLATE_NAME_CU_DETAIL = "channel_use_detail_template.xls";
	/** 模板文件名（广告播放汇总） */
	public static final String EXPORT_TEMPLATE_NAME_AP_SUMMARY = "advert_play_summary_template.xls";
	/** 模板文件名（广告播放明细） */
	public static final String EXPORT_TEMPLATE_NAME_AP_DETAIL = "advert_play_detail_template.xls";
	
	/** 模板文件名（設備運行） */
	public static final String EXPORT_TEMPLATE_NAME_DEVICE_RUNNING = "device_running_template.xls";
	
	/** 模板文件名（综合运行统计） */
	public static final String EXPORT_TEMPLATE_NAME_MULTI_RUNNING_STATISTICS = "multi_running_statistics_template.xls";
	/** 模板文件名（UKey使用统计） */
	public static final String EXPORT_TEMPLATE_UKEY = "uKey_info_template.xls";
	/** 模板文件名（设备故障统计） */
	public static final String EXPORT_TEMPLATE_FAULT = "fault_info_template.xls";
		
	/** 模板文件名（频道管理） */
	public static final String EXPORT_TEMPLATE_NAME_CHANNEL = "channel_manage_template.xls";
	/** 模板文件名（端机信息） */
	public static final String EXPORT_TEMPLATE_NAME_CLIENT = "machine_info_template.xls";
	/** 模板文件名（端机使用信息-汇总） */
	public static final String EXPORT_TEMPLATE_NAME_CLIENT_ALL_USE = "machine_use_info_template_all_data.xls";
	/** 模板文件名（端机使用信息-详细） */
	public static final String EXPORT_TEMPLATE_NAME_CLIENT_DETAIL_USE = "machine_use_info_template_detail_data.xls";

	
	/** 导出Excel模板文件存放目录 */
	public static final String EXPORT_FILE_PATH_TEMPLATE = "EXPORT_FILE_PATH_TEMPLATE";
	/** 导出临时的Excel文件存放目录 */
	public static final String EXPORT_FILE_PATH_TEMP = "EXPORT_FILE_PATH_TEMP";
	/** 导出Excel文件的扩展名 */
	public static final String EXPORT_EXCEL_FILE_EXTENSION  = ".xls";

	/* Report Page */
	/** 开机率检索页面 */
	public static final String REPORT_PAGE_MOR = "1";
	/** 频道使用统计页面 */
	public static final String REPORT_PAGE_CU = "2";
	/** 广告播放统计页面 */
	public static final String REPORT_PAGE_AP = "3";
	/** Ukey使用统计 */
	public static final String REPORT_PAGE_UKEY = "4";
	/** 设备故障统计 */
	public static final String REPORT_PAGE_FAULT = "5";
	/** 端机部署检索 */
	public static final String REPORT_PAGE_DEPLOY = "6";
	/** 用户中奖检索 */
	public static final String REPORT_PAGE_AWARD = "7";

	/* Function Code */
	/** 页面初始化 */
	public static final String FUNCTION_CODE_INIT = "1";
	/** 检索 */
	public static final String FUNCTION_CODE_SEARCHED = "2";
	/** 导出 */
	public static final String FUNCTION_CODE_EXPORT_EXCEL = "3";
	
	/* Data Type */
	/** 汇总数据 */
	public static final String DATA_TYPE_SUMMARY = "0";
	/** 明细数据 */
	public static final String DATA_TYPE_DETAIL = "1";

	/* Time Type */
	/** 日统计 */
	public static final String TIME_TYPE_DAY = "1";
	/** 周统计 */
	public static final String TIME_TYPE_WEEK = "2";
	/** 月统计 */
	public static final String TIME_TYPE_MONTH = "3";
	/** 时间区间统计 */
	public static final String TIME_TYPE_ANY = "4";

	/* Page Forward */
	/** 开机率检索页面 */
	public static final String PAGE_FORWARD_MOR = "/jsp/report/machineOpenRate.jsp";
	/** 频道使用统计页面 */
	public static final String PAGE_FORWARD_CU = "/jsp/report/channelUse.jsp";
	/** 广告播放统计页面 */
	public static final String PAGE_FORWARD_AP = "/jsp/report/advertPlay.jsp";
	/** 导出页面 */
	public static final String PAGE_FORWARD_EXPORT_EXCEL = "/jsp/report/exportExcel.jsp";
	/** UKey统计页面 */
	public static final String PAGE_FORWARD_UKEY = "/jsp/report/uKeyInfo.jsp";
	/** 设备故障统计页面 */
	public static final String PAGE_FORWARD_FAULT = "/jsp/report/faultInfo.jsp";
	/** 端机部署统计页面 */
	public static final String PAGE_FORWARD_DEPLOY = "/jsp/report/deployInfo.jsp";
	/** 中奖检索页面 */
	public static final String PAGE_FORWARD_AWARD = "/jsp/lottery/awardInfo.jsp";
	
	/** replaceAllのregex */
	public static final String DATE_REPLACEALL_REGEX = "[:/\\-\\. ]";
	/** Date format pattern（毫秒） */
	public static final String DATE_FORMAT_PATTERN_MILLIS	= "yyyyMMddHHmmssSSSS";
	/** Date format pattern（日期） */
	public static final String DATE_FORMAT_PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
	/** Date format pattern（日期） */
	public static final String DATE_FORMAT_PATTERN_YYYYMMDD = "yyyyMMdd";
	/** Date format pattern（年月） */
	public static final String DATE_FORMAT_PATTERN_YYYYMM = "yyyyMM";
	
	/* 机器状态 */
	/** 服务中 */
	public static final String MACHINE_STATUS_SERVICE = "0";
	/** 报停 */
	public static final String MACHINE_STATUS_PAUSE = "1";
	/** 广告中 */
	public static final String MACHINE_STATUS_ADVERTISING = "2";
	/** 未审核 */
	public static final String MACHINE_STATUS_UNAPPROVED = "3";
	/** 离线 */
	public static final String MACHINE_STATUS_OUTLINE = "4";
	/** 服务中 */
	public static final String MACHINE_STATUS_SERVICE_VALUE = "服务中";
	/** 报停 */
	public static final String MACHINE_STATUS_PAUSE_VALUE = "锁定\\报停";
	/** 广告中 */
	public static final String MACHINE_STATUS_ADVERTISING_VALUE = "锁屏\\广告";
	/** 未审核 */
	public static final String MACHINE_STATUS_UNAPPROVED_VALUE = "未审核";
	/** 离线 */
	public static final String MACHINE_STATUS_OUTLINE_VALUE = "离线";

	/* 广告播放点击状态 */
	/** 未点击 */
	public static final String ADVERT_CLICK_STATUS_NOT_EXIST = "0";	
	/** 已点击 */
	public static final String ADVERT_CLICK_STATUS_EXIST = "1";	
	
	/* FTP服务器中文件的"上传路径"和 "备份路径"在配置文件中的key值*/
	/** 频道 文件的上传路径*/
	public static final String FTP_UPLOAD_FILE_PATH_CHANNEL = "FTP_UPLOAD_FILE_PATH_CHANNEL";	
	/** 广告文件的上传路径*/
	public static final String FTP_UPLOAD_FILE_PATH_ADVERTISEMENT = "FTP_UPLOAD_FILE_PATH_ADVERTISEMENT";
	/** 频道 文件的备份路径*/
	public static final String FTP_UPLOAD_FILE_PATH_CHANNEL_BACKUP = "FTP_UPLOAD_FILE_PATH_CHANNEL_BACKUP";	
	/** 广告文件的备份路径*/
	public static final String FTP_UPLOAD_FILE_PATH_ADVERT_BACKUP = "FTP_UPLOAD_FILE_PATH_ADVERT_BACKUP";
	
	/* 趋势图时间统计类型 */
	/** 月度 */
	public static final String CHART_TIME_TYPE_MONTH = "1";
	/** 年度 */
	public static final String CHART_TIME_TYPE_YEAR = "2";
	
}
