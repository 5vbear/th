// ファイル名 : Define.java
// バージョン : $Id: Define.java 38483 2011-10-26 08:34:11Z YangLL $

package th.com.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

/*-----------------------------------------------------------------------------
 /  クラス名 ： Define
 /----------------------------------------------------------------------------*/
/**
 * 定義値クラス<br>
 * 本システムシステムで使用する定義値を提供する。 また、プロパティファイル（Define.properties）から定義値を読み込み、 変数に設定する。
 */
public final class Define {

	// *********************************************
	// * 共通デファイン値
	// *********************************************/

	// 正常
	public static final int OK = 0;
	// 異常
	public static final int NG = -1;
	// APIステータス正常
	public static final String apiOK = "0";
	// APIステータス異常
	public static final String apiNG = "-1";
	// 系统管理员
	public static final String ADMIN = "administrator";
	// 顶级组织
	public static final String TOP_ORG = "0";
	// Tabel检索结果每页显示行数
	public static final int VIEW_NUM = 10;
	// 默认密码(密码重置)
	public static final String DEFALULT_PASSWORD = "111111";
	// 默认FTP路径(文件管理->下载)
	public static final String DEFALUT_FTP_DOWNLOAD_PATH = "C:/FTP/";
	/* ------------策略定义------------ */
	// 字符位标识
	public static final String CHAR_IDENTIFY_ZERO = "0";
	public static final String CHAR_IDENTIFY_ONE = "1";
	public static final String CHAR_IDENTIFY_TWO = "2";
	public static final String CHAR_IDENTIFY_THREE = "3";
	// 发送频率
	public static final String SEND_INTERNAL_DAY = "每天";
	public static final String SEND_INTERNAL_WEEK = "每周";
	public static final String SEND_INTERNAL_MONTH = "每月";
	// 发送方式
	public static final String SEND_TYPE_EMAIL = "邮件";
	public static final String SEND_TYPE_MOBILE = "彩信";
	// 状态
	public static final String STRATEGY_STATUS_ENABLE = "启用";
	public static final String STRATEGY_STATUS_DISABLE = "停止";

	public static class JspConstants {
		public static final String LOGIN_SUCCESS = "main.html";
		public static final String JSP_MAIN = "/th/jsp/main.jsp";
	}

	// 用户类型定义
	// 普通用户
	public static final String USER_NORMAL = "0";
	// 管理员用户
	public static final String USER_ADMIN = "1";

	// *********************************************
	// * PCアプリ関連
	// *********************************************/
	public static final String PC_APP_FLG = "1";

	// 会員情報管理データソース名
	public static String USR_DB_SOURCE_NAME;

	public static String MACHINE_CONFIG_HOTIME;
	public static String MACHINE_CONFIG_MOTIME;
	public static String MACHINE_CONFIG_HCTIME;
	public static String MACHINE_CONFIG_MCTIME;
	public static String MACHINE_CONFIG_PROTIME;
	public static String MACHINE_CONFIG_SCREENCOPYDURATION;
	public static String MACHINE_CONFIG_SCREENCOPYINTERVAL;

	public static String FTP_SERVER_HOST;
	public static String HOST_SERVER;
	public static String MACHINE_CONFIG_COMMAND_TIME;
	
	/* ------------监控管理------------ */
	// 端机运行监控画面ID
	public static final String JSP_MONITOR_CLIENT_RUNNING_FRAME_ID = "monitor00";
	// 端机运行矩阵监控画面ID
	public static final String JSP_MONITOR_CLIENT_RUNNING_ID = "monitor01";
	// 端机运行列表监控画面ID
	public static final String JSP_MONITOR_CLIENT_RUNNING_LIST_ID = "monitor08";
	// 端机运行地图监控画面ID
	public static final String JSP_MONITOR_CLIENT_RUNNING_MAP_ID = "monitor09";
	// 端机系统监控画面ID
	public static final String JSP_MONITOR_CLIENT_SYSTEM_ID = "monitor02";
	// 端机信息画面ID
	public static final String JSP_MONITOR_CLIENT_ID = "monitor03";
	// 端机进程画面ID
	public static final String JSP_MONITOR_CLIENT_PROCESS_ID = "monitor04";
	// 端机服务画面ID
	public static final String JSP_MONITOR_CLIENT_SERVICE_ID = "monitor05";
	// 端机报警监控设置画面ID
	public static final String JSP_MONITOR_CLIENT_ALARM_SETTING_ID = "monitor06";
	// 端机报警选择画面ID
	public static final String JSP_MONITOR_CLIENT_ALART_SELECT_ID = "monitor07";

	// 端机状态 0=服务中
	public static final String MACHINE_STATUS_SERVICE = "0";
	// 端机状态 1=报停
	public static final String MACHINE_STATUS_STOP = "1";
	// 端机状态 2=广告中
	public static final String MACHINE_STATUS_ADS = "2";
	// 端机状态 4=线路中断
	public static final String MACHINE_STATUS_BREAK = "4";
	// 端机db中状态 4=报废
	public static final String MACHINE_STATUS_RETIREMENT = "4";

	// 端机运行监控FRAME画面
	public static final String JSP_MONITOR_CLIENT_RUNNING_FRAME_INFO = "/jsp/monitor/ClientRunningFrame.jsp";
	// 端机运行矩阵监控画面
	public static final String JSP_MONITOR_CLIENT_RUNNING_INFO = "/jsp/monitor/ClientRunningInfo.jsp";
	// 端机运行列表监控画面
	public static final String JSP_MONITOR_CLIENT_RUNNING_LIST_INFO = "/jsp/monitor/ClientRunningListInfo.jsp";
	// 端机运行地图监控画面
	public static final String JSP_MONITOR_CLIENT_RUNNING_MAP = "/jsp/monitor/ClientRunningMap.jsp";
	// 端机系统监控画面
	public static final String JSP_MONITOR_CLIENT_SYSTEM_INFO = "/jsp/monitor/ClientSystemInfo.jsp";
	// 端机信息画面
	public static final String JSP_MONITOR_CLIENT_INFO = "/jsp/monitor/ClientInfo.jsp";
	// 端机进程信息画面
	public static final String JSP_MONITOR_CLIENT_PROCESS_INFO = "/jsp/monitor/ClientProcessInfo.jsp";
	// 端机服务信息画面
	public static final String JSP_MONITOR_CLIENT_SERVICE_INFO = "/jsp/monitor/ClientServiceInfo.jsp";
	// 端机报警监控设置画面
	public static final String JSP_MONITOR_CLIENT_ALARM_SETTING_INFO = "/jsp/monitor/ClientSetting.jsp";
	// 端机报警监控设置画面
	public static final String JSP_MONITOR_CLIENT_ALART_SELECT_INFO = "/jsp/monitor/ClientSelect.jsp";
	// 为避免与组织树中id重复，在本次端机循环中将 "M"+端机ID 作为id
	public static final String TREE_ID_PREFIX = "M";
	// 白名单orgList画面
	public static final String JSP_AVAILABLE_PAGE_LIST = "/jsp/availablePage/orgSelect.jsp";

	// 白名单DataList画面
	public static final String JSP_AVAILABLE_PAGE_DATA_LIST = "/jsp/availablePage/availableList.jsp";

	// 白名单DataADD画面
	public static final String JSP_AVAILABLE_PAGE_DATA_ADDD = "/jsp/availablePage/addAvailableList.jsp";
	// 白名单DataEDIT画面
	public static final String JSP_AVAILABLE_PAGE_DATA_EDIT = "/jsp/availablePage/editAvailableList.jsp";
	// 白名单下发DataList画面
	public static final String JSP_AVAILABLE_PAGE_DATA_UPLOAD_LIST = "/jsp/availablePage/sendUpdateAvaiable.jsp";

	// 监控管理URL
	public static String MONITOR_SERVLET = "/th/MonitorServlet.html";
	// 监控右键设定URL
	public static String MONITOR_RIGHT_SETTTING_SERVLET = "/th/MonitorRightSettingServlet.html";

	// 抽奖画面
	public static final String JSP_LOTTERY = "/jsp/lottery/lottery.jsp";

	// 端机运行监控设置FLAG
	// 刷新
	public static final String MONITOR_RUNNING_REFREASH = "1";
	// 设备信息
	public static final String MONITOR_RUNNING_MACHINEINFO = "2";
	// 部署信息
	public static final String MONITOR_RUNNING_DEPLOYINFO = "3";
	// 启用
	public static final String MONITOR_RUNNING_INUSE = "4";
	// 报停
	public static final String MONITOR_RUNNING_STOP = "5";
	// 重启
	public static final String MONITOR_RUNNING_RESTART = "6";
	// 关机
	public static final String MONITOR_RUNNING_SHUTDOWN = "7";
	// 远程
	public static final String MONITOR_RUNNING_REMOUTE = "9";
	// 清除数据
	public static final String MONITOR_CLEAR_DATA = "8";
	// 启动截屏
	public static final String MONITOR_START_SCREEN_SHOT = "10";
	// 停止截屏
	public static final String MONITOR_STOP_SCREEN_SHOT = "11";
	// 定位
	public static final String MONITOR_LOCATE = "12";
	// 开始广告播放
	public static final String MONITOR_START_ADV = "13";
	// 停止广告播放
	public static final String MONITOR_END_ADV = "14";
	// 开始播放临时广告
	public static final String MONITOR_START_TEMP_ADV = "15";
	// 停止播放临时广告
	public static final String MONITOR_END_TEMP_ADV = "16";
	// 锁定
	public static final String MONITOR_LOCK = "17";
	// 解锁
	public static final String MONITOR_UNLOCK = "18";
	// 发送消息
	public static final String MONITOR_SEND_MESSAGE = "19";
	// 报废
	public static final String MONITOR_RETIREMENT = "20";
	// 删除应用程序
	public static final String MONITOR_RUNNING_DELETE_APP = "32";
	/** ------------机器配置管理------------ */

	// 机器配置检索画面ID
	public static final String JSP_MACHINE_SEARCH_ID = "jsp_machine_search_id";
	// 机器配置添加画面ID

	public static final String JSP_MACHINE_ADD_ID = "jsp_machine_add_id";
	// 机器配置下发画面ID
	public static final String JSP_MACHINE_SEND_ID = "jsp_machine_send_id";
	// 机器配置检索处理ID
	public static final String FUNC_MACHINE_SEARCH_ID = "func_machine_search_id";
	// 机器配置下发listID
	public static final String FUNC_MACHINE_SEARCH_SEND_ID = "func_machine_search_send_id";
	// 机器配置下发
	public static final String FUNC_MACHINE_SEND_ID = "func_machine_send_id";
	// 机器配置删除画面ID

	public static final String FUNC_MACHINE_DEL_ID = "func_machine_del_id";
	/** ------------机器配置管理------------ */
	// 端机报警邮件标题
	public static String ALERT_MAIL_TITLE = "端机报警测试";

	// 非法进程
	public static String ALERTNAME_FFJC = "1";
	// 非法服务
	public static String ALERTNAME_FFFW = "2";
	// cpu负荷过高
	public static String ALERTNAME_FHGG_CPU = "3";
	// 内存负荷过高
	public static String ALERTNAME_FHGG_MEMORY = "4";
	// 硬盘容量不足
	public static String ALERTNAME_RLBZ_HARD = "5";
	// 上行速率过慢
	public static String ALERTNAME_SDGM_UP = "6";
	// 下行速率过慢
	public static String ALERTNAME_SDGM_DOWN = "7";
	// 非法访问率过高
	public static String ALERTNAME_FFFWL = "8";
	// 断线报警
	public static String ALERTNAME_DXJB = "9";

	// cpu负荷字段名
	public static String ALERT_CPU_FIELD_NAME = "CUP_LOAD";
	// 内存负荷字段名
	public static String ALERT_MEMORY_FIELD_NAME = "MEMORY_LOAD";
	// 剩余硬盘空间字段名
	public static String ALERT_DISK_UNUSED_FIELD_NAME = "DISK_UNUSED";
	// 上行速率字段名
	public static String ALERT_UPLOAD_RATE_FIELD_NAME = "UPLOAD_RATE";
	// 下行速率字段名
	public static String ALERT_DOWNLOAD_RATE_FIELD_NAME = "DOWNLOAD_RATE";

	/** ------------广告管理------------ */
	// 素材检索画面ID
	public static final String JSP_MATERIAL_SEARCH_ID = "jsp_material_search_id";
	// 素材添加画面ID
	public static final String JSP_MATERIAL_ADD_ID = "jsp_material_add_id";
	// 素材编辑画面ID
	public static final String JSP_MATERIAL_EDIT_ID = "jsp_material_edit_id";
	// 布局检索画面ID
	public static final String JSP_LAYOUT_SEARCH_ID = "jsp_layout_search_id";
	// 布局添加画面ID
	public static final String JSP_LAYOUT_ADD_ID = "jsp_layout_add_id";
	// 布局编辑画面ID
	public static final String JSP_LAYOUT_EDIT_ID = "jsp_layout_edit_id";
	// 节目单检索画面ID
	public static final String JSP_PROGRAMLIST_SEARCH_ID = "jsp_programlist_search_id";
	// 节目单添加画面ID
	public static final String JSP_PROGRAMLIST_ADD_ID = "jsp_programlist_add_id";
	// 节目单编辑画面ID
	public static final String JSP_PROGRAMLIST_EDIT_ID = "jsp_programlist_edit_id";
	// 模式窗口画面ID
	public static final String JSP_SUB_WINDOW_ID = "jsp_sub_window_id";
	// 区域授权处理ID
	public static final String JSP_REGIONAL_AUTH_ID = "jsp_regional_auth_id";
	// 节目单发布画面ID
	public static final String JSP_PROGRAMLIST_SEND_ID = "jsp_programlist_send_id";
	// 节目单组检索画面ID
	public static final String JSP_PROGRAMLISTGROUP_SEARCH_ID = "jsp_programlistgroup_search_id";
	// 节目单组添加画面ID
	public static final String JSP_PROGRAMLISTGROUP_ADD_ID = "jsp_programlistgroup_add_id";
	// 节目单组编辑画面ID
	public static final String JSP_PROGRAMLISTGROUP_EDIT_ID = "jsp_programlistgroup_edit_id";

	// 素材检索处理ID
	public static final String FUNC_MATERIAL_SEARCH_ID = "func_material_search_id";
	// 素材添加处理ID
	public static final String FUNC_MATERIAL_ADD_ID = "func_material_add_id";
	// 素材审核处理ID
	public static final String FUNC_MATERIAL_AUDIT_ID = "func_material_audit_id";
	// 素材更新处理ID
	public static final String FUNC_MATERIAL_UPDATE_ID = "func_material_update_id";
	// 素材删除处理ID
	public static final String FUNC_MATERIAL_DELETE_ID = "func_material_delete_id";
	// 素材批量审核处理ID
	public static final String FUNC_MATERIAL_ALLAUDIT_ID = "func_material_allaudit_id";
	// 素材批量删除处理ID
	public static final String FUNC_MATERIAL_ALLDELETE_ID = "func_material_alldelete_id";
	// 布局检索处理ID
	public static final String FUNC_LAYOUT_SEARCH_ID = "func_layout_search_id";
	// 布局添加处理ID
	public static final String FUNC_LAYOUT_ADD_ID = "func_layout_add_id";
	// 布局审核处理ID
	public static final String FUNC_LAYOUT_AUDIT_ID = "func_layout_audit_id";
	// 布局更新处理ID
	public static final String FUNC_LAYOUT_UPDATE_ID = "func_layout_update_id";
	// 布局删除处理ID
	public static final String FUNC_LAYOUT_DELETE_ID = "func_layout_delete_id";
	// 布局批量审核处理ID
	public static final String FUNC_LAYOUT_ALLAUDIT_ID = "func_layout_allaudit_id";
	// 布局批量删除处理ID
	public static final String FUNC_LAYOUT_ALLDELETE_ID = "func_layout_alldelete_id";
	// 节目单检索处理ID
	public static final String FUNC_PROGRAMLIST_SEARCH_ID = "func_programlist_search_id";
	// 节目单添加处理ID
	public static final String FUNC_PROGRAMLIST_ADD_ID = "func_programlist_add_id";
	// 节目单审核处理ID
	public static final String FUNC_PROGRAMLIST_AUDIT_ID = "func_programlist_audit_id";
	// 节目单更新处理ID
	public static final String FUNC_PROGRAMLIST_UPDATE_ID = "func_programlist_update_id";
	// 节目单删除处理ID
	public static final String FUNC_PROGRAMLIST_DELETE_ID = "func_programlist_delete_id";
	// 节目单批量审核处理ID
	public static final String FUNC_PROGRAMLIST_ALLAUDIT_ID = "func_programlist_allaudit_id";
	// 节目单批量删除处理ID
	public static final String FUNC_PROGRAMLIST_ALLDELETE_ID = "func_programlist_alldelete_id";
	// 选取布局模式窗口处理IID
	public static final String FUNC_LAYOUT_SUBWINDOW_ID = "func_layout_subwindow_id";
	// 选取素材模式窗口处理IID
	public static final String FUNC_MATERIAL_SUBWINDOW_ID = "func_material_subwindow_id";
	// 选取字幕模式窗口处理IID
	public static final String FUNC_SUBTITLES_SUBWINDOW_ID = "func_subtitles_subwindow_id";
	// 选取节目单模式窗口处理IID
	public static final String FUNC_PROGRAMLIST_SUBWINDOW_ID = "func_programlist_subwindow_id";
	// 选取节目单组模式窗口处理IID
	public static final String FUNC_PROGRAMLISTGROUP_SUBWINDOW_ID = "func_programlistgroup_subwindow_id";
	// 区域授权处理ID
	public static final String FUNC_REGIONAL_AUTH_ID = "func_regional_auth_id";
	// 区域授权取得布局信息处理ID
	public static final String FUNC_GET_LAYOUT_INFO = "func_get_layout_info";
	// 节目单授权处理ID
	public static final String FUNC_PROGRAMLIST_AUTH_ID = "func_programlist_auth_id";
	// 节目单下发处理ID
	public static final String FUNC_PROGRAM_SEND_ID = "func_program_send_id";
	// 清除端机节目单列表处理ID
	public static final String FUNC_PROGRAMLIST_CLEAN_ID = "func_programlist_clean_id";
	// FTP取得素材缩略图处理ID
	public static final String FUNC_FTP_FILE_GET_ID = "func_ftp_file_get_id";
	// 字幕预览处理ID
	public static final String FUNC_SUBTITLES_PREVIEW_ID = "func_subtitles_preview_id";
	// 节目单组检索处理ID
	public static final String FUNC_PROGRAMLISTGROUP_SEARCH_ID = "func_programlistgroup_search_id";
	// 节目单组添加处理ID
	public static final String FUNC_PROGRAMLISTGROUP_ADD_ID = "func_programlistgroup_add_id";
	// 节目单组审核处理ID
	public static final String FUNC_PROGRAMLISTGROUP_AUDIT_ID = "func_programlistgroup_audit_id";
	// 节目单组更新处理ID
	public static final String FUNC_PROGRAMLISTGROUP_UPDATE_ID = "func_programlistgroup_update_id";
	// 节目单组删除处理ID
	public static final String FUNC_PROGRAMLISTGROUP_DELETE_ID = "func_programlistgroup_delete_id";
	// 节目单组批量审核处理ID
	public static final String FUNC_PROGRAMLISTGROUP_ALLAUDIT_ID = "func_programlistgroup_allaudit_id";
	// 节目单组批量删除处理ID
	public static final String FUNC_PROGRAMLISTGROUP_ALLDELETE_ID = "func_programlistgroup_alldelete_id";

	// 白名单组织检索处理Id
	public static final String FUNC_AVAILABLE_PAGE_ORGLIST_ID = "func_available_page_orgList";
	// 白名单内容检索处理Id
	public static final String FUNC_AVAILABLE_PAGE_DATALIST_ID = "func_available_page_dataList";
	// 白名单添加处理Id
	public static final String FUNC_AVAILABLE_PAGE_ADD_ID = "func_available_page_add_id";
	// 白名单添加画面处理Id
	public static final String FUNC_AVAILABLE_PAGE_ADDPAGE_ID = "func_available_page_addPage_id";
	// 白名单编辑画面处理Id
	public static final String FUNC_AVAILABLE_PAGE_EDITPAGE_ID = "func_available_page_editPage_id";
	// 白名单删除画面处理Id
	public static final String FUNC_AVAILABLE_PAGE_DELETE_ID = "func_available_page_delete_id";
	// channel启用
	public static final String FUNC_CHANNEL_OPEN_ID = "func_channel_open_id";
	// channel停用
	public static final String FUNC_CHANNEL_STOP_ID = "func_channel_stop_id";
	// channel编辑
	public static final String FUNC_CHANNEL_EDIT_ID = "func_channel_edit_id";
	// 企业文档下发画面处理Id
	public static final String FUNC_DOCUMENT_DOWN_ID = "func_func_document_down_id";
	public static final String FUNC_CHANNEL_SEARCH_ID = "func_channel_search_id";
	// 白名单编辑画面处理Id
	public static final String FUNC_AVAILABLE_PAGE_MOD_ID = "func_available_page_mod_id";
	public static final String FUNC_CHANNEL_DO_EDIT_ID = "func_channel_do_edit_id";
	// 白名单下发显示处理Id
	public static final String FUNC_AVAILABLE_PAGE_UPLOAD_LIST_ID = "func_available_page_upload_list_id";
	// 白名单状态 1 添加
	public static final String AVAILABLE_PAGE_TYPE_ADD = "1";
	// 白名单状态 2 编辑
	public static final String AVAILABLE_PAGE_TYPE_eidt = "2";
	// 白名单状态 3 删除
	public static final String AVAILABLE_PAGE_TYPE_DEL = "3";
	// 抽奖画面处理ID
	public static final String FUNC_LOTTERY_ID = "func_lottery_id";
	/** ------------日志管理------------ */
	// 下发节目单
	public static final String OPERATION_TYPE_BILL = "下发节目单";
	// 字幕
	public static final String OPERATION_TYPE_SUBTITLES = "字幕下发";
	// 配置
	public static final String OPERATION_TYPE_MODULE = "配置";
	// 系统升级
	public static final String OPERATION_TYPE_SYSTEM_UPDATE = "系统升级";
	// 频道发布
	public static final String OPERATION_TYPE_CHANNEL = "频道发布";
	// 快速入口发布
	public static final String OPERATION_TYPE_QUICK_ENTRY = "快速入口发布";

	/*********************************************
	 * Cookieコントロール
	 *********************************************/
	// ログインリトライ回数
	public static int LOGIN_LIMIT_COUNT;
	// ログインリトライ回数Cookie識別子
	public static String LOGIN_LIMIT_NAME;
	// ログインリトライ間隔（秒）
	public static int LOGIN_LIMIT_TIME;

	// 数据库备份
	// 备份目录
	public static String BACKUP_DIRECTORY;
	// 备份数据库
	public static String BACKUP_DBNAME;
	// 备份用密码文件配置
	public static String BACKUP_CONFIG;
	// 备份CMD命令
	public static String BACKUP_DUMP;
	// 备份CMD命令(linux)
	public static String BACKUP_DUMP_LINUX;
	// API送信時コンテンツタイプ
	public static String CONTENT_TYPE;
	// 备份url
	public static String BACKUP_URL;
	// 机器信息查询url
	public static String ALERT_MONITOR_URL;
	// HOST SERVER NAME
	public static String BATCH_SERVER_NAME;
	public static String BATCH_SERVER_PORT;
	// FTP开始命令
	public static String FTP_START;
	// FTP路径
	public static String FTP_PATH;
	// FTP关闭命令
	public static String FTP_CLOSE;
	// FTPw配置文件路径
	public static String FTP_USERS_FILE_PATH;
	
	
	// staticフィールドでプロパティファイルから値を取得する
	static {
		// プロパティファイルから値を取得
		getEnv();
	}

	/*-------------------------------------------------------------------------
	/  メソッド名 ： getEnv
	/------------------------------------------------------------------------*/
	/**
	 * プロパティファイル名をContextから取得し、設定値をインスタンス変数 に設定する。
	 */
	private static void getEnv() {

		FileInputStream fis = null; // 設定ファイルInputStream

		try {
			// イニシャルコンテキスト取得
			Context ctx = (Context) new InitialContext();
			// 環境変数取得用コンテキスト取得
			Context envCtx = (Context) ctx.lookup("java:comp/env");
			// 設定ファイル名取得
			String propertyFile = (String) envCtx.lookup("property_file_name");

			// ファイルの存在をチェックする
			File file = new File(propertyFile);
			if (!file.exists()) {
				System.out.println("NOT FOUND Define.properties");
				throw new Exception("NOT FOUND Define.properties ="
						+ propertyFile);
			}

			// InputStream作成
			fis = new FileInputStream(file);

			// プロパティに設定
			Properties pro = new Properties();
			pro.load(fis);

			// 数据库备份
			BACKUP_DIRECTORY = pro.getProperty("BACKUP_DIRECTORY").trim();
			BACKUP_DBNAME = pro.getProperty("BACKUP_DBNAME").trim();
			BACKUP_CONFIG = pro.getProperty("BACKUP_CONFIG").trim();
			BACKUP_DUMP = pro.getProperty("BACKUP_DUMP").trim();
			BACKUP_DUMP_LINUX = pro.getProperty("BACKUP_DUMP_LINUX").trim();
			CONTENT_TYPE = pro.getProperty("CONTENT_TYPE").trim();
			BACKUP_URL = pro.getProperty("BACKUP_URL").trim();
			ALERT_MONITOR_URL = pro.getProperty("ALERT_MONITOR_URL").trim();
			BATCH_SERVER_NAME = pro.getProperty("BATCH_SERVER_NAME").trim();
			BATCH_SERVER_PORT = pro.getProperty("BATCH_SERVER_PORT").trim();
			// FTP管理
			FTP_START = pro.getProperty("FTP_START").trim();
			FTP_PATH = pro.getProperty("FTP_PATH").trim();
			FTP_CLOSE = pro.getProperty("FTP_CLOSE").trim();
			FTP_USERS_FILE_PATH = pro.getProperty("FTP_USERS_FILE_PATH").trim();

			// プロパティファイルから値を取得
			USR_DB_SOURCE_NAME = pro.getProperty("USR_DB_SOURCE_NAME").trim();

			MACHINE_CONFIG_HOTIME = pro.getProperty("MACHINE_CONFIG_HOTIME")
					.trim();
			MACHINE_CONFIG_MOTIME = pro.getProperty("MACHINE_CONFIG_MOTIME")
					.trim();
			MACHINE_CONFIG_HCTIME = pro.getProperty("MACHINE_CONFIG_HCTIME")
					.trim();
			MACHINE_CONFIG_MCTIME = pro.getProperty("MACHINE_CONFIG_MCTIME")
					.trim();
			MACHINE_CONFIG_PROTIME = pro.getProperty("MACHINE_CONFIG_PROTIME")
					.trim();
			MACHINE_CONFIG_SCREENCOPYDURATION = pro.getProperty(
					"MACHINE_CONFIG_SCREENCOPYDURATION").trim();
			MACHINE_CONFIG_SCREENCOPYINTERVAL = pro.getProperty(
					"MACHINE_CONFIG_SCREENCOPYINTERVAL").trim();

			MACHINE_CONFIG_COMMAND_TIME = pro.getProperty(
					"MACHINE_CONFIG_COMMAND_TIME").trim();

			FTP_SERVER_HOST = pro.getProperty("FTP_SERVER_HOST").trim();
			HOST_SERVER = pro.getProperty("HOST_SERVER").trim();
		} catch (Exception e) {
			System.out.println("DEFINE ERROR");
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e) {
				System.out.println("DEFINE ERROR");
				e.printStackTrace();
			}
		}
	}
}
/*-- End of file ------------------------------------------------------------*/
