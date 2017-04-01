<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>中国建设银行</title>
<link rel="stylesheet" type="text/css" href="../css/sdmenu.css" />
<link rel="stylesheet" type="text/css" href="../css/header.css" />
<link rel="stylesheet" type="text/css" href="../css/import_cy.css" />
<link rel="stylesheet" type="text/css" href="../css/footer.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.8.2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.simplemodal.1.4.4.min.js"></script>
<script src="../js/sdmenu.js" language="javascript"></script>
<style type="text/css">
	
	#model1{
		height: 40px;
		width: 400px;
		display: none;
		vertical-align: middle;
		text-align: center;
		margin: 0 auto;
		color: black;
		font-weight: bolder;
	}
</style>
<script type="text/javascript">
	//          
	var myMenu;
	window.onload = function() {
		myMenu = new SDMenu("my_menu");
		myMenu.init();
	};
	
	
	function a(URL,id)
	{
	set_current(id)
	window.open(URL,target='main');
	} 
	
	function abcdeft(mid){
		a('/th/remote.html?deal=remote_top&lastPage=0&machineID='+mid,'999');
	}
	
	function abcdeftList(mid){
		a('/th/remote.html?deal=remote_top&lastPage=1&machineID='+mid,'999');
	}
	
	
	var cur_id="";
	function set_current(id)
	{
	   cur_link=document.getElementById("f"+cur_id);
	   if(cur_link)
	      cur_link.className="";
	   cur_link=document.getElementById("f"+id);
	   if(cur_link)
	      cur_link.className="current";
	   cur_id=id;
	}
	//
</script>

</head>
<body>
<%
	Log logger = LogFactory.getLog(this.getClass());
	User user = (User) session.getAttribute("user_info");
	Date currentTime = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String dateString = formatter.format(currentTime);
	String realname =null;
	if (user == null) {
		 response.setContentType("text/html; charset=utf-8");
		 response.sendRedirect("/th/index.jsp");
	} else {
		realname = user.getReal_name();
		logger.info("获得当前用户的用户名，用户名是： " + realname);
	}
%>

<div style="margin-right: auto; margin-left: auto;" width='100%'>
<table width='100%' border='0' cellpadding='0' cellspacing='0'>

	<tr height="85">
		<td colspan="3">
		<table width='100%' border='0' cellpadding='0' cellspacing='0'
			background='../image/sy/sy_04.gif'>
			<tr>
				<td width='332' height='55'><img
					src='../image/untitled.png' width='276'
					height='50' /></td>
				<td>&nbsp;</td>
				<td width='419' align='left' valign='bottom'
					background='../image/sy/sy_06.gif'>
				<table width='96%' border='0' cellspacing='0' cellpadding='0'>
					<tr>
						<td height='20' align='right' valign='bottom' class='hy'><%=dateString %>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=realname==null?"":realname%>&nbsp;&nbsp;&nbsp;Ver 1.3</td>
					</tr>
					<tr>
						<td height='5px'></td>
					</tr>
					<tr>
						<td height='30' align='right' valign='top'>
						<table border='0' cellpadding='0' cellspacing='0'>
							<tr>
								<td width='6' height='25' background='../image/sy/top_25.gif'></td>
								<td width='68' align='center'
									background='../image/sy/top_26.gif' class='hy'><a
									href='/th/logout.html'>退出系统</a></td>
								<td width='8' background='../image/sy/top_28.gif'></td>
							</tr>
						</table>
						</td>
					</tr>

				</table>
				</td>
			</tr>
		</table>
		</td>

	</tr>

	<tr height="85">
		<td colspan="3">
		<table width='100%' border='0' cellpadding='0' cellspacing='0'
			background='../image/sy/sy_no.gif'>
			<tr>
				<td width='25' height='44'></td>
				<td align='left' valign='top'>
			</tr>
		</table>
		</td>

	</tr>
	<tr>
		<td style="float: right">

		<div class="sdmenu"
			style="float: left; overfllow:Hidden；height :480; width: 250;"
			id="my_menu">
		<div><span>监控管理</span> 
		<a onclick="javascript:a('/th/MonitorServlet.html?orderType=2&pageID=monitor00','1');" id="f1" target="main">端机运行监控</a> 
		<a onclick="javascript:a('/th/MonitorServlet.html?orderType=2&pageID=monitor02','2');" id="f2" target="main">端机系统监控</a> 
		<a onclick="javascript:a('/th/MonitorServlet.html?orderType=2&pageID=monitor06','3');" id="f3" target="main">端机报警设置</a>
		</div>
		<div class="collapsed"><span>端机配置</span> 
			<a onclick="a('/th/jsp/machineConfig/machineConfigSeach.html?pageId=jsp_machine_search_id','111');return false;" id="f111" target="main">端机配置检索</a> 
			<a onclick="javascript:a('/th/jsp/machineConfig/machineConfigSeach.html?pageId=jsp_machine_send_id','112');" id="f112" target="main">端机配置下发</a> 
		</div>
		<div class="collapsed"><span>端机指令</span> 
			<a onclick="javascript:a('/th/machineServlet?model=command','121');" id="f121" target="main">端机指令</a>
			 <a onclick="javascript:a('/th/jsp/updateManagement/UpdateManagement.html?action=sendUpdateOrder122','122');" id="f122" target="main">升级下发</a> 
			<a onclick="a('/th/AdvertServlet?pageId=jsp_programlist_send_id&type=123','123');return false;" id="f123"  target="main">广告下发</a>
		</div>
		<div class="collapsed"><span>端机管理</span> 
			<a onclick="javascript:a('/th/machineServlet?model=audit','11');" id="f11" target="main">端机审核</a> 
			<a onclick="javascript:a('/th/machineServlet?model=command','12');" id="f12" target="main">端机指令</a> 
			<a onclick="javascript:a('/th/machineServlet?model=deploy','13');" id="f13" target="main">端机部署</a> 
			<a onclick="javascript:a('/th/Report?reportPage=6','20');" id="f20" target="main">端机部署检索</a>
			<a onclick="javascript:a('/th/machineServlet?model=info','14');" id="f14" target="main">端机信息</a> 
			<a onclick="javascript:a('/th/machineServlet?model=sych','19');" id="f19" target="main">端机同步</a>
			<!--<a onclick="javascript:a('/th/machineServlet?model=config','15');" id="f15" target="main">端机配置</a> -->
			<a onclick="javascript:a('/th/machineServlet?model=alert','16');" id="f16" target="main">告警管理</a> 
			<a onclick="javascript:a('/th/machineServlet?model=repair','21');" id="f21" target="main">派修管理</a>
			<a onclick="javascript:a('/th/machineServlet?model=faq','17');" id="f17" target="main">故障知识库</a>
			<a onclick="javascript:a('/th/machineServlet?model=dict','18');" id="f18" target="main">配置数字字典</a> 
		</div>
		<div class="collapsed"><span>广告管理</span> 
			<a onclick="a('/th/AdvertServlet?pageId=jsp_material_search_id','21');return false;" id="f21" target="main">素材管理</a> 
			<a onclick="a('/th/AdvertServlet?pageId=jsp_layout_search_id','22');return false;" id="f22" target="main">布局管理</a> 
			<a onclick="a('/th/AdvertServlet?pageId=jsp_programlist_search_id','23');return false;" id="f23"  target="main">节目单管理</a> 
			<a onclick="a('/th/AdvertServlet?pageId=jsp_programlistgroup_search_id','24');return false;" id="f24"  target="main">节目单组管理</a> 
			<a onclick="a('/th/AdvertServlet?pageId=jsp_programlist_send_id','25');return false;" id="f25"  target="main">节目单发布</a>
		</div>
		<%
		if(user!=null&&"0".equals(user.getId())){%>
		<div class="collapsed"><span>升级管理</span> 
			 <a onclick="javascript:a('/th/jsp/updateManagement/UpdateManagement.html?action=sendUpdateOrder','41');" id="f41" target="main">升级包下发</a> 
			 <a onclick="javascript:a('/th/jsp/updateManagement/UpdatePackage.html?action=List','42');" id="f42" target="main">升级文件管理</a>
			 <a onclick="javascript:a('/th/jsp/updateManagement/UpdateHistorySearch.html','43');" id="f43" target="main">升级历史查询</a>
		</div>
		<div class="collapsed"><span>IOS升级管理</span> 
			 <a onclick="javascript:a('/th/jsp/iosManagement/IosManagementServlet.html?action=sendUpdateOrder','47');" id="f41" target="main">IOS系统升级</a> 
			 <a onclick="javascript:a('/th/jsp/iosManagement/IosSysUpdate.html','47');" id="f47" target="main">IOS升级包管理</a>
		</div>
		<div class="collapsed"><span>系统升级管理</span> 
			 <a onclick="javascript:a('/th/jsp/sysUpdateManagement/SysUpdateManagement.html?action=sendUpdateOrder','44');" id="f44" target="main">系统升级包下发</a> 
			 <a onclick="javascript:a('/th/jsp/sysUpdateManagement/SysUpdatePackage.html?action=List','45');" id="f45" target="main">系统升级包管理</a>
			 <a onclick="javascript:a('/th/jsp/sysUpdateManagement/SysUpdateHistorySearch.html','46');" id="f46" target="main">系统升级历史查询</a>
			
		</div>
		<%}%>
		<div class="collapsed"><span>报表管理</span> 
			<a onclick="javascript:a('/th/Report?reportPage=1','53');" id="f53" target="main">开机率统计</a> 
			<a onclick="javascript:a('/th/Report?reportPage=2','54');" id="f54" target="main">频道使用统计</a>
			<a onclick="javascript:a('/th/Report?reportPage=3','55');" id="f55" target="main">广告播放统计</a>
			<a onclick="javascript:a('/th/jsp/client/clientUseInfo.html?type=clientUseInfoGet&first=true','59');" id="f59" target="main">设备使用统计</a> 
			<a onclick="javascript:a('/th/Report?reportPage=4','76');" id="f76" target="main">网银盾使用统计</a>  
			<a onclick="javascript:a('report/DeviceRunningAssess.jsp','56');" id="f56" target="main">设备运行考核</a> 
			<a onclick="javascript:a('/th/Report?reportPage=5','77');" id="f77" target="main">设备故障统计</a>
			<a onclick="javascript:a('report/MultiRunningStatistics.jsp','57');" id="f57" target="main">综合运行统计</a> 
			<a onclick="javascript:a('/th/jsp/client/clientInfo.html?type=get&first=true','58');" id="f58" target="main">端机信息报表</a> 
			<!--<a onclick="javascript:a('/th/jsp/client/clientUseInfo.html?type=runningChart','75');" id="f75" target="main">设备考核趋势图</a>-->
			<a onclick="javascript:a('/th/jsp/client/clientUseInfo.html?type=runningNewChart','78');" id="f78" target="main">设备考核趋势图</a>
	
		</div>
		<div class="collapsed"><span>系统管理</span> 
			<a onclick="javascript:a('/th/jsp/sysMang/strategyMang/strategyList.html','60');" id="f60" target="main">默认角色策略定义</a>
			<a onclick="javascript:a('/th/jsp/sysMang/taskMang/taskList.html','30');" id="f30" target="main">策略定义</a>
		 	<a onclick="javascript:a('/th/jsp/sysMang/roleMang/roleList.html','61');" id="f61" target="main">角色管理</a> 
		 	<a onclick="javascript:a('/th/jsp/sysMang/orgMang/orgDeal.html','62');" id="f62" target="main">组织管理</a> 
		 	<a onclick="javascript:a('/th/jsp/sysMang/dptMang/dptSearch.html','63');" id="f63" target="main">部门管理</a>
		 	<a onclick="javascript:a('/th/jsp/sysMang/userMang/userSearch.html','64');" id="f64" target="main">用户管理</a>
		 	<%if(user!=null&&"0".equals(user.getId())){%>
		 	<a onclick="javascript:a('/th/jsp/sysMang/sysConf/sysServConf.html','65');" id="f65" target="main">系统设置</a> 
		 	<a onclick="javascript:a('/th/jsp/sysMang/mailConf/mailServConf.html','66');" id="f66" target="main">邮件设置</a> 
		 	<%}%>
		 	<a onclick="javascript:a('/th/jsp/sysMang/fileMang/upFileDeal.html','67');" id="f67" target="main">文件管理</a>
		 	<a onclick="javascript:a('/th/jsp/sysMang/devMang/ebDeviceList.html','68');" id="f68" target="main">设备操作系统定义</a> 
		 	<a onclick="javascript:a('/th/lottery.html?type=list_award','69');" id="f69" target="main">抽奖管理</a>
		 	<a onclick="javascript:a('/th/Report?reportPage=7','26');" id="f26" target="main">中奖检索</a>	
		 	<a onclick="javascript:a('/th/backup.html?deal=backup_top','100');" id="f100" target="main">服务器数据管理</a>
		 	<a onclick="javascript:a('/th/jsp/sysMang/internetBankingMang/internetBankingSearch.html','33');" id="f33" target="main">网银设备定义管理</a>
		 	<a onclick="javascript:a('/th/ftpsetting.html','601');" id="f601" target="main">FTP管理</a>
		</div>
		<div class="collapsed"><span>日志管理</span>
			<a onclick="javascript:a('/th/LogListServlet.html?pageId=system_operational_init','71');" id="f71" target="main">系统操作日志</a> 
			<a onclick="javascript:a('/th/LogListServlet.html?pageId=advertising_play_init','72');" id="f72" target="main">广告播放日志</a> 
			<a onclick="javascript:a('/th/LogListServlet.html?pageId=advertising_Click_init','73');" id="f73" target="main">广告点击日志</a>
			<a onclick="javascript:a('/th/Advice.html?pageId=advice_init','74');" id="f74" target="main">意见薄</a> 
			<a onclick="javascript:a('/th/Message.html?pageId=message_init','75');" id="f75" target="main">历史消息</a>
		</div>

		<div class="collapsed"><span>频道管理</span>
			<a onclick="javascript:a('/th/jsp/Available/availablePageList.html?type=channel&processId=func_available_page_orgList','81');" id="f81" target="main">频道管理</a>
			<a onclick="javascript:a('/th/jsp/Available/availablePageList.html?type=quick&processId=func_available_page_orgList','82');" id="f82" target="main">快速入口</a>
			<a onclick="javascript:a('/th/jsp/Available/availablePageList.html?type=availablelist&processId=func_available_page_orgList','83');" id="f83" target="main">白名单管理</a> 
		</div>
		
		<!--<div class="collapsed"><span>同步管理</span>
			<a onclick="javascript:a('/th/jsp/SyncManagement.html?','81');" id="f81" target="main">同步管理</a> 
		</div>-->
		<%
		if(user!=null&&"0".equals(user.getId())){%>
		<div class="collapsed"><span>应用商店</span>
			
			 <a onclick="javascript:a('/th/jsp/appStore/UpdateAppStore.html?action=List','91');" id="f91" target="main">应用上架</a>
	<!--cw_UpdateManagement -->	 <a onclick="javascript:a('/th/jsp/appStore/UpdateAppStoreManagement.html?action=sendUpdateAppOrder','92');" id="f92" target="main">应用下发</a>
	
		<a onclick="javascript:a('/th/jsp/appStore/AppManagement.html?action=List','93');" id="f93" target="main">应用管理</a> 
		</div>
		<%}%>
		<div class="collapsed"><span>企业文档管理</span>
			<a onclick="javascript:a('/th/jsp/Available/availablePageList.html?type=document&processId=func_available_page_orgList','101');" id="f101" target="main">企业文档管理</a>
		</div>
		</td>
		<td>
		
				
		<div style="height: 650px;font-family: Arial, sans-serif;font-size: 12px;padding-left: 5px;padding-bottom: 10px;background:  no-repeat  right bottom;"
			id="my_menu"><Iframe allowtransparency="true" style="float: left bottom" src="first.jsp"
			height="650" width="980" scrolling="no" frameborder="0" name="main"
			id="main"></iframe></div>
		
		</td>
	</tr>

</table>
<div id="model1" >
<h2>指令正在执行,请稍等...</h2>
</div>


<!-- footer -->
<div id="footer_container">
<div id="footer_contents">
<div id="footer_right"></div>
</div>
</div>
<!-- footer END -->
</body>
<script type="text/javascript">
	
	function modelWindows(){
		$('#model1').modal({
			opacity:'70',
			overlayCss:{
				'background-color':'white'
			}
		});
	}
	function stopModel(){
		//关闭模态窗口
		   $.modal.close();
	}
</script>
</html>