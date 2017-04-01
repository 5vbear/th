package th.action.machine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import th.action.OrgDealAction;
import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define4Machine;
import th.dao.ChannelDAO;
import th.dao.ClientDAO;
import th.dao.OrgDealDAO;
import th.dao.machine.InfoDAO;
import th.dao.machine.MachineDAO;
import th.entity.MachineProcessBean;
import th.user.User;
import th.util.StringUtils;
import th.util.ftp.FtpUtils;


/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class InfoAction extends MachineAction {

	protected InfoDAO dao = new InfoDAO();
	
	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public InfoAction(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see th.action.machine.MachineAction#doIt()
	 */
	@Override
	public String doIt() {
		String result;
		if("enter".equals(method) || StringUtils.isBlank(method)){
			result = enter();
		}else if("view".equals(method)){
			result = view();
		}else{
			result = Define4Machine.JSP_MACHINE_INVALID;
		}
		return result;
		
	}

	/**
	 * 进入信息首页
	 * 
	 * @return
	*/
	public String enter(){
		
		try {
			hasRight("11");
			List<HashMap> orgList = new OrgDealAction().getChildNodesByOrgId(Long.parseLong(user.getOrg_id()));
			req.setAttribute("orgName", getOrgNameByUserID(user.getOrg_id()));
			req.setAttribute("orgid", user.getOrg_id());
			req.setAttribute("orgsList", buildMachineInOrg(orgList));
			return Define4Machine.JSP_MACHINE_INFO;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String view() {
		try {
			hasRight("12");
			// 用户ID
			String userId = user.getId();
			//组织ID
			String orgId = "";
			if (userId == null) {
				logger.debug("用户ID为空");
			}
			orgId = req.getParameter("orgID");
			if(StringUtils.isBlank(orgId)){
				orgId = user.getOrg_id();
			}
			if(StringUtils.isBlank(orgId)){
				logger.debug("组织ID为空");
			}
			String macIdStd = req.getParameter("macIdStd");
			String pageIdx = req.getParameter("pageIdx");
			if(StringUtils.isBlank(macIdStd)){
				return Define4Machine.JSP_MACHINE_INFO_BLANK;
			}
			Map[] deployInfo = dao.getDeployInfo(macIdStd);
			Map moreInfo = dao.getMoreInfo(macIdStd);
			moreInfo = this.replaceMacType(moreInfo);
			Map[] alertInfo = dao.getAlertInfo(macIdStd);
			Map[] historyInfo = dao.getHistoryInfo(macIdStd);
			//Map macConfig = dao.getMacConfig(macIdStd);
			req.setAttribute("macIdStd", macIdStd);
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("macIdStd", macIdStd);
			req.setAttribute("deployInfo", deployInfo[0]);
			req.setAttribute("moreInfo", moreInfo);
			req.setAttribute("alertInfo", alertInfo);
			req.setAttribute("historyInfo", historyInfo);
			req.setAttribute("macConfig", getConfig(macIdStd));
			return Define4Machine.JSP_MACHINE_INFO_VIEW;
		}catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
		
	}
	
	private Map replaceMacType(Map moreInfo){
		HashMap<String, String> map = new ClientDAO().getMacType();
		String mType = (String)(moreInfo.get("MTYPE"));
		String os = (String)(moreInfo.get("OS"));
		String macKind = (String)(moreInfo.get("MACHINE_KIND"));
		if(-1 != os.indexOf("Win")){
			os = "Win";
		}
		String macTypeTemp = os + "_" + macKind;
		moreInfo.put("MTYPE", map.get(macTypeTemp).toString());
		return moreInfo;
	}
	public String macSaveHard() {
		try {
			return String.valueOf(dao.updateHardInfoByMacID(req));
		}catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		
	}
	private Map<String,String> getConfig(String machineID) throws SQLException, IOException{
		Map<String,String> map = new HashMap<String,String>();
		String mac = new MachineDAO().getMacInfo(machineID).get("MAC").toString();
		
		String path = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_PATH_CONFIGURATION");
		//由FTP读取端机进程列表
    	FTPFile[] files = FtpUtils.getFiles(path);
    	String fileName = "";
    	for (FTPFile ftpFile : files) {
			if(ftpFile.getName().startsWith(mac)){
	    		fileName = ftpFile.getName();
	    		break;
	    	}
		}
    	
    	if("".equals(fileName)){
    		logger.info("FTP服务器中未找到MAC地址为[" + mac + "]的文件");
    		//设定默认值
    		map.put("STIME", LocalProperties.getProperty("MACHINE_CONFIG_HOTIME")+":"+LocalProperties.getProperty("MACHINE_CONFIG_MOTIME")+":00");//开机时间
    		map.put("CTIME", LocalProperties.getProperty("MACHINE_CONFIG_HCTIME")+":"+LocalProperties.getProperty("MACHINE_CONFIG_MCTIME")+":00");//关机时间
    		map.put("SPTIME", LocalProperties.getProperty("MACHINE_CONFIG_PROTIME"));//屏幕保护时间
    		map.put("PROPATH", "");//写保护目录
    		map.put("SCRTIME", LocalProperties.getProperty("MACHINE_CONFIG_SCREENCOPYDURATION"));//截屏结束时间
    		map.put("IVLTIME", LocalProperties.getProperty("MACHINE_CONFIG_SCREENCOPYINTERVAL"));//截屏间隔时间
    		map.put("SURL", LocalProperties.getProperty("HOST_SERVER"));//应用服务器地址
    		map.put("FTPIP", LocalProperties.getProperty("FTP_SERVER_HOST"));//FTP服务器IP
    		return map;
    	}
    	
    	InputStream in = null;
    	FTPClient ftpClient = FtpUtils.getFTPClient();
    	ftpClient.changeWorkingDirectory(path);
    	in = ftpClient.retrieveFileStream(fileName);
    	List<String> list = new ArrayList<String>();
    	if (in != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String data = null;
			
			try {
				while ((data = br.readLine()) != null) {
					list.add(StringUtils.transStr(data));
				}
				
				map.put("STIME", list.get(0));//开机时间
	    		map.put("CTIME", list.get(1));//关机时间
	    		map.put("SPTIME", list.get(2));//屏幕保护时间
	    		map.put("PROPATH", list.get(3));//写保护目录
	    		map.put("IVLTIME", list.get(4));//截屏间隔时间
	    		map.put("SCRTIME", list.get(5));//截屏结束时间
	    		map.put("SURL", list.get(6));//应用服务器地址
	    		map.put("FTPIP", list.get(7));//FTP服务器IP
				
			} catch (IOException e) {
				logger.error("文件读取错误。");
				e.printStackTrace();
			} finally {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.error("InputStream为空，不能读取。");
		}
		
		return map;
		
	}
}
