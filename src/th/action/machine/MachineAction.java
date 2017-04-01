package th.action.machine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.action.BaseAction;
import th.com.property.LocalProperties;
import th.com.util.Define4Machine;
import th.dao.MonitorDAO;
import th.dao.OrgDealDAO;
import th.entity.MachineBean;
import th.user.User;
import th.util.FileTools;
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
public abstract class MachineAction extends BaseAction {

	protected HttpServletRequest req;
	protected HttpServletResponse res;
	protected String method;
	protected User user;
	
	public MachineAction(HttpServletRequest req, HttpServletResponse res){
		this.req = req;
		this.res = res;
		this.method = req.getParameter("method");
		HttpSession session = req.getSession(false);
		// 获取用户信息
		user = (User) session.getAttribute("user_info");
	}
	
	public abstract String doIt();
	
	protected void uploadIniFileToFTP(String updateZipFilePath, HashMap cfgInfo, String mac) throws IOException {
		String macIniLocalTmpPath = LocalProperties
				.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_CONFIGURATION");
		String suffix = LocalProperties
				.getProperty("FTP_DOWNLOAD_FILE_SUFFIX");

		// STEP1: 生成对应的临时目录
		FileTools.mkdirs(macIniLocalTmpPath);

		// STEP2: 向ini文件中写入数据
		writePackageInfoToIniFile(cfgInfo,
				FileTools.buildFullFilePath(macIniLocalTmpPath, mac + suffix));

		// STEP3: 上传文件列表
		FtpUtils.uploadFile(FileTools.getSubFiles(macIniLocalTmpPath),
				updateZipFilePath);

		// STEP4: 移除临时文件
		FileTools.removeSubFiles(macIniLocalTmpPath);
	}
	
	/**
	 * 
	 * 
	 * @param cfgInfo
	 * @param buildFullFilePath
	 * @throws IOException 
	*/
	private void writePackageInfoToIniFile(HashMap cfgInfo,	String path) throws IOException {
		FileWriter fw = new FileWriter(new File(path));
		BufferedWriter bw = new BufferedWriter(fw);
		if(cfgInfo != null){

			
			bw.write(parseStr(cfgInfo.get("STIME")));
			bw.newLine();
			bw.write(parseStr(cfgInfo.get("CTIME")));
			bw.newLine();
			bw.write(parseStr(cfgInfo.get("SPTIME")));
			bw.newLine();
			bw.write(parseStr(cfgInfo.get("PROPATH")));
			bw.newLine();
			bw.write(parseStr(cfgInfo.get("IVLTIME")));
			bw.newLine();
			bw.write(parseStr(cfgInfo.get("SCRTIME")));
			bw.newLine();
			bw.write(parseStr(cfgInfo.get("SURL")));
			bw.newLine();
			bw.write(parseStr(cfgInfo.get("FTPIP")));
			bw.newLine();
			bw.write(parseStr(cfgInfo.get("COMMAND_TIME")));
			bw.newLine();
		}

		fw.flush();
		bw.close();
		fw.close();
//		FileTools.closeWriter(bw);
	}
	
	private String parseStr(Object obj){
		if(obj != null){									
			return obj.toString();
		}else{
			return "";
		}
	}

	protected void hasRight(String actionId) throws Exception{

		// 用户ID
		String userId = user.getId();
		//组织ID
		String orgId = user.getOrg_id();
		if(StringUtils.isBlank(userId)) {
			logger.debug("用户ID为空");
		}
		if(StringUtils.isBlank(orgId)){
			logger.debug("组织ID为空");
		}
		if(!user.hasRight(orgId, actionId)){
			throw new Exception("无权限异常"); 
		}
	}
	
	/**
	 * @param userOrgs
	 * @param orgCurrentID
	 * @return 
	 */
	protected String buildOrgOption(List<HashMap> userOrgs){
		/*HashMap curOrgNode = null;
		long curOrgId = -1;
		long curParentOrgId = -1;*/
		HashMap orgNode = null;
		long orgId = -1;
		long parentOrgId = -1;
		String orgName = "";
		int org_level = 0;
		StringBuffer sb = new StringBuffer();
		if(userOrgs!=null&&userOrgs.size()>0){
			/*System.out.println("--------------------组织节点详情---------------------");
			for(int i=0;i<userOrgs.size();i++){
				curOrgNode = (HashMap)userOrgs.get( i );
				curOrgId = Long.parseLong(curOrgNode.get( "ORG_ID" ).toString());
				orgName = curOrgNode.get( "ORG_NAME" ).toString();
				curParentOrgId = Long.parseLong(curOrgNode.get( "PARENT_ORG_ID" ).toString());
				System.out.println("父节点ID : "+ curParentOrgId + " --- 当前节点ID ： "+curOrgId+" --- 节点名称 ： "+orgName);
			}
			System.out.println("--------------------生成组织树---------------------");*/
			sb.append( "[ " );
			for (int i=0;i<userOrgs.size();i++){//userOrgs替换

				orgNode = userOrgs.get(i);
				orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
				parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
				orgName = orgNode.get( "ORG_NAME" ).toString();
				org_level = Integer.parseInt( orgNode.get( "ORG_LEVEL" ).toString() );

				sb.append( "{ \"id\": " );
				sb.append( orgId + "," );
				sb.append( " \"pId\": " );
				sb.append( parentOrgId + "," );
				sb.append( " \"name\": " );
				sb.append( "\"" + orgName + "\"" );
				if( org_level<=2 ){
					sb.append( ", open:true " );
				}
				
				sb.append( "}" );

				if(i<userOrgs.size()-1){
					sb.append( "," );
				}

			}
			sb.append( " ];" );
			
		}
		return sb.toString();
	}
	
	/**
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	protected String getOrgNameByUserID(String orgID) throws Exception, SQLException{
		HashMap[] map = new OrgDealDAO().getCurOrgNodeByOrgId( Long.parseLong(orgID) );
		if(map != null && map.length > 0){
			return (String)map[0].get("ORG_NAME");
		}
		return "";
	}
	
	protected String buildMachineInOrgCommnd(List<HashMap> userOrgs) throws SQLException{
		String macIdStd = req.getParameter("macIdStd");
		HashMap orgNode = null;
		long orgId = -1;
		long parentOrgId = -1;
		String orgName = "";
		String treeStr = "";
		int org_level = 0;
		StringBuffer sb = new StringBuffer();
		MonitorDAO monitorDAO = new MonitorDAO();
		List<Long> orgIDList = new ArrayList<Long>();
		List<MachineBean> lBean = new ArrayList<MachineBean>();
		List<MachineBean> machineOrgList =  new MonitorDAO().getRelationlistBetweenMachineInfoWithOrgAndEnv();
		if(userOrgs!=null&&userOrgs.size()>0){
			sb.append( "[ " );
			for (int i=0;i<userOrgs.size();i++){

				orgNode = userOrgs.get(i);
				orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
				parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
				orgName = orgNode.get( "ORG_NAME" ).toString();
				org_level = Integer.parseInt( orgNode.get( "ORG_LEVEL" ).toString() );

				orgIDList.add(orgId);
				
				sb.append( "{ \"id\": " );
				sb.append( orgId + "," );
				sb.append( " \"pId\": " );
				sb.append( parentOrgId + "," );
				sb.append( " \"name\": " );
				sb.append( "\"" + orgName + "\"" );
				if( org_level<=1 ){
					sb.append( ", open:true " );
				}
				sb.append(", \"mactype\":\"org\"");
				
				sb.append( "}" );

				if(i<userOrgs.size()-1){
					sb.append( "," );
				}
			}
			
			sb.append( "," );
			
			
			//组织<---->端机类型
			int n = 0;
			String tempMacType = "";
			HashMap<String,String> typeMap = monitorDAO.getType();
			List<String> tempShowMacTypeList = new ArrayList<String>();
			for(int k = 0; k < orgIDList.size(); k++){
				orgId = orgIDList.get(k);
				lBean = getMachineByOrgID(orgId, machineOrgList);
				
				for(n = 0; n < lBean.size(); n++){
					tempMacType = StringUtils.transStr(lBean.get(n).getShowMacType());
					if("".equals(tempMacType) || tempShowMacTypeList.contains(tempMacType)){
						continue;
					}
					sb.append( "{ \"id\": " );
					sb.append( "\"" + orgId+typeMap.get(tempMacType) + "\"," );
					sb.append( " \"pId\": " );
					sb.append( orgId + "," );
					sb.append( " \"name\": " );
					sb.append( "\"" + tempMacType + "\"" );
					sb.append(", \"mactype\":\"type\"");
					sb.append( "}" );

					if(n<lBean.size()-1){
						sb.append( "," );
					}
					tempShowMacTypeList.add(tempMacType);
				}
				if(n != 0 && k<orgIDList.size()-1 && n == tempShowMacTypeList.size()){
					sb.append( "," );
				}
				tempShowMacTypeList.clear();
				
				if(sb.length()-1 != sb.lastIndexOf(",")){
					sb.append( "," );
				}
			}
			
			if(sb.length()-1 != sb.lastIndexOf(",")){
				sb.append( "," );
			}
			
			//端机类型<---->端机
			int m = 0;
			for(int k = 0; k < orgIDList.size(); k++){
				orgId = orgIDList.get(k);
				lBean = getMachineByOrgID(orgId, machineOrgList);
				for(m = 0; m < lBean.size(); m++){
					sb.append( "{ \"id\": " );
					sb.append( "\"" + Define4Machine.TREE_MACID_PREFIX+lBean.get(m).getMachineId() + "\"," );
					sb.append( " \"pId\": " );
					sb.append( "\"" + orgId+typeMap.get(lBean.get(m).getShowMacType()) + "\"," );
					sb.append( " \"name\": " );
					sb.append( "\"" + lBean.get(m).getMachineMark() + "\"" );
					sb.append(", \"mactype\":\"mac\"");
					sb.append( "}" );

					if(m<lBean.size()-1){
						sb.append( "," );
					}
				}
				if(m != 0 && k<orgIDList.size()-1){
					sb.append( "," );
				}
			}
			
			
			
			//添加组织下端机ID NAME
			/*int n = 0;
			for(int k = 0; k < orgIDList.size(); k++){
				orgId = orgIDList.get(k);
				lBean = getMachineByOrgID(orgId, machineOrgList);
				for(n = 0; n < lBean.size(); n++){
					sb.append( "{ \"id\": " );
					//为避免与组织树中id重复，在本次端机循环中将   "999999"+端机ID 作为id
					//注：组织ID最大不能超过999999,否则资源数将显示错误
					sb.append( "\"" + Define4Machine.TREE_MACID_PREFIX+lBean.get(n).getMachineId() + "\"," );
					sb.append( " \"pId\": " );
					sb.append( orgId + "," );
					sb.append( " \"name\": " );
					sb.append( "\"" + lBean.get(n).getMachineMark() + "\"" );
					sb.append(", \"mactype\":\"mac\"");
					sb.append( "}" );

					if(n<lBean.size()-1){
						sb.append( "," );
					}
				}
				if(n != 0 && k<orgIDList.size()-1){
					sb.append( "," );
				}
			}*/
			treeStr = sb.toString();
			//当组织下没有任何端机时过滤结尾逗号
			if(treeStr.endsWith(",")){
				treeStr = treeStr.substring(0, treeStr.length()-1);
			}
			treeStr += " ];" ;
			
		}
		return treeStr;
	}
	
	/**
	 * @param userOrgs
	 * @return
	 * @throws SQLException
	 */
	protected String buildMachineInOrg(List<HashMap> userOrgs) throws SQLException{
		String macIdStd = req.getParameter("macIdStd");
		HashMap orgNode = null;
		long orgId = -1;
		long parentOrgId = -1;
		String orgName = "";
		String treeStr = "";
		int org_level = 0;
		StringBuffer sb = new StringBuffer();
		List<Long> orgIDList = new ArrayList<Long>();
		List<MachineBean> lBean = new ArrayList<MachineBean>();
		List<MachineBean> machineOrgList =  new MonitorDAO().getRelationlistBetweenMachineInfoWithOrg();
		if(userOrgs!=null&&userOrgs.size()>0){
			sb.append( "[ " );
			for (int i=0;i<userOrgs.size();i++){

				orgNode = userOrgs.get(i);
				orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
				parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
				orgName = orgNode.get( "ORG_NAME" ).toString();
				org_level = Integer.parseInt( orgNode.get( "ORG_LEVEL" ).toString() );

				orgIDList.add(orgId);
				
				sb.append( "{ \"id\": " );
				sb.append( orgId + "," );
				sb.append( " \"pId\": " );
				sb.append( parentOrgId + "," );
				sb.append( " \"name\": " );
				sb.append( "\"" + orgName + "\"" );
				if( org_level<=1 ){
					sb.append( ", open:true " );
				}
				sb.append(", \"mactype\":\"org\"");
				
				sb.append( "}" );

				if(i<userOrgs.size()-1){
					sb.append( "," );
				}
			}
			
			sb.append( "," );
			//添加组织下端机ID NAME
			int n = 0;
			for(int k = 0; k < orgIDList.size(); k++){
				orgId = orgIDList.get(k);
				lBean = getMachineByOrgID(orgId, machineOrgList);
				for(n = 0; n < lBean.size(); n++){
					sb.append( "{ \"id\": " );
					//为避免与组织树中id重复，在本次端机循环中将   "999999"+端机ID 作为id
					//注：组织ID最大不能超过999999,否则资源数将显示错误
					sb.append( "\"" + Define4Machine.TREE_MACID_PREFIX+lBean.get(n).getMachineId() + "\"," );
					sb.append( " \"pId\": " );
					sb.append( orgId + "," );
					sb.append( " \"name\": " );
					sb.append( "\"" + lBean.get(n).getMachineMark() + "\"" );
					sb.append(", \"mactype\":\"mac\"");
					sb.append( "}" );

					if(n<lBean.size()-1){
						sb.append( "," );
					}
				}
				if(n != 0 && k<orgIDList.size()-1){
					sb.append( "," );
				}
			}
			treeStr = sb.toString();
			//当组织下没有任何端机时过滤结尾逗号
			if(treeStr.endsWith(",")){
				treeStr = treeStr.substring(0, treeStr.length()-1);
			}
			treeStr += " ];" ;
			
		}
		return treeStr;
	}
	
	/**
	 * @param userOrgs
	 * @return
	 * @throws SQLException
	 */
	protected String buildMachineInOrg2(List<HashMap> userOrgs) throws SQLException{
		String macIdStd = req.getParameter("macIdStd");
		HashMap orgNode = null;
		long orgId = -1;
		long parentOrgId = -1;
		String orgName = "";
		String treeStr = "";
		int org_level = 0;
		StringBuffer sb = new StringBuffer();
		List<Long> orgIDList = new ArrayList<Long>();
		List<MachineBean> lBean = new ArrayList<MachineBean>();
		List<MachineBean> machineOrgList =  new MonitorDAO().getRelationlistBetweenMachineInfoWithOrg();
		if(userOrgs!=null&&userOrgs.size()>0){
			sb.append( "[ " );
			for (int i=0;i<userOrgs.size();i++){

				orgNode = userOrgs.get(i);
				orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
				parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
				orgName = orgNode.get( "ORG_NAME" ).toString();
				org_level = Integer.parseInt( orgNode.get( "ORG_LEVEL" ).toString() );

				orgIDList.add(orgId);
				
				sb.append( "{ \"id\": " );
				sb.append( orgId + "," );
				sb.append( " \"pId\": " );
				sb.append( parentOrgId + "," );
				sb.append( " \"name\": " );
				sb.append( "\"" + orgName + "\"" );
				if( org_level<=1 ){
					sb.append( ", open:true " );
				}
				sb.append(", \"mactype\":\"org\"");
				
				sb.append( "}" );

				if(i<userOrgs.size()-1){
					sb.append( "," );
				}
			}
			
			sb.append( "," );
			//添加组织下端机ID NAME
			int n = 0;
			for(int k = 0; k < orgIDList.size(); k++){
				orgId = orgIDList.get(k);
				lBean = getMachineByOrgID(orgId, machineOrgList);
				for(n = 0; n < lBean.size(); n++){
					sb.append( "{ \"id\": " );
					//为避免与组织树中id重复，在本次端机循环中将   "999999"+端机ID 作为id
					//注：组织ID最大不能超过999999,否则资源数将显示错误
					sb.append( "\"" + Define4Machine.TREE_MACID_PREFIX+lBean.get(n).getMachineId() + "\"," );
					sb.append( " \"pId\": " );
					sb.append( orgId + "," );
					sb.append( " \"name\": " );
					sb.append( "\"" + lBean.get(n).getMachineMark() + "\"" );
					sb.append(", \"mactype\":\"mac\"");
					if(StringUtils.isBlank(macIdStd)){
						macIdStd = String.valueOf(lBean.get(n).getMachineId());
						sb.append(", \"checked\":true");
						req.setAttribute("macIdStd", macIdStd);
					}
					sb.append( "}" );

					if(n<lBean.size()-1){
						sb.append( "," );
					}
				}
				if(n != 0 && k<orgIDList.size()-1){
					sb.append( "," );
				}
			}
			treeStr = sb.toString();
			//当组织下没有任何端机时过滤结尾逗号
			if(treeStr.endsWith(",")){
				treeStr = treeStr.substring(0, treeStr.length()-1);
			}
			treeStr += " ];" ;
			
		}
		return treeStr;
	}
	
	/**
	 * 取得机器列表中与orgID相等的机器列表
	 * @param orgID
	 * @param list
	 * @return
	 */
	private List<MachineBean> getMachineByOrgID(long orgID, List<MachineBean> list){
		List<MachineBean> beanList = new ArrayList<MachineBean>();
		for(int m = 0; m<list.size(); m++){
			if(orgID == list.get(m).getOrgID()){
				beanList.add(list.get(m));
			}
		}
		return beanList;
	}
	
	/**
	 * 取得组织及组织下子组织的orgId，以,分隔。如：1,2,3
	 * 
	 * @param parentId
	 * @return
	 */
	protected String getOrgIdsByParentId( String parentId ) {
		StringBuffer sb = new StringBuffer( parentId );
		OrgDealDAO orgDealDAO = new OrgDealDAO();
		String orgIds = orgDealDAO.getSubOrg( parentId );
		if ( StringUtils.isNotBlank(orgIds) ) {
			sb.append( "," + orgIds );
		}
		return sb.toString();
	}

}
