/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import th.com.util.Define;
import th.dao.DptDealDAO;
import th.dao.OrgDealDAO;
import th.dao.RoleDealDAO;
import th.dao.StrategyDealDAO;
import th.dao.UserDao;
import th.entity.RoleObjectManagementBean;
import th.entity.TaskBean;
import th.entity.UserBean;
import th.user.User;

/**
 * Descriptions
 *
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 *
 */
public class OrgDealAction extends BaseAction{

	private List childNodesList = null;

	public OrgDealAction(){

		this.childNodesList = new ArrayList();
	}

	public String getAppStrFromNode(HashMap orgNode){

		String result = null;
		if(orgNode!=null){

			long orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
			long parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
			String orgName = orgNode.get( "ORG_NAME" ).toString();

			StringBuffer sb = new StringBuffer();
			sb.append( "{ \"id\": " );
			sb.append( orgId + "," );
			sb.append( " \"pId\": " );
			sb.append( parentOrgId + "," );
			sb.append( " \"name\": " );
			sb.append( "\"" + orgName + "\"" );
			sb.append( "}" );

			result = sb.toString();
		}

		return result;
	}

	public String getPIdListByMap(HashMap[] nodes){

		HashMap curNode = null;
		long orgId = -1;
		String pidList = null;
		if(nodes!=null&&nodes.length>0){
			StringBuffer sb = new StringBuffer();
			sb.append( "( " );
			for(int i=0;i<nodes.length;i++){
				curNode = (HashMap)nodes[i];
				orgId = Long.parseLong( curNode.get( "ORG_ID" ).toString());
				sb.append( orgId );
				if(i<nodes.length-1){
					sb.append( ", " );
				}	
			}
			sb.append( " )" );
			pidList = sb.toString();
		}	
		return pidList;

	}
	
	public String getPIdFirstListByMap(HashMap[] nodes){

		HashMap curNode = null;
		long orgId = -1;
		String pidList = null;
		if(nodes!=null&&nodes.length>0){
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<nodes.length;i++){
				curNode = (HashMap)nodes[i];
				orgId = Long.parseLong( curNode.get( "ORG_ID" ).toString());
				sb.append( orgId );
				if(i<nodes.length-1){
					sb.append( ", " );
				}	
			}
			pidList = sb.toString();
		}	
		return pidList;

	}
	
	
	public String getIdStrByList(List nodes){

		HashMap curNode = null;
		long orgId = -1;
		String idStr = null;
		if(nodes!=null&&nodes.size()>0){
			StringBuffer sb = new StringBuffer();
			sb.append( "( " );
			for(int i=0;i<nodes.size();i++){
				curNode = (HashMap)nodes.get( i );
				orgId = Long.parseLong( curNode.get( "ORG_ID" ).toString());
				sb.append( orgId );
				if(i<nodes.size()-1){
					sb.append( ", " );
				}	
			}
			sb.append( " )" );
			idStr = sb.toString();
		}	
		return idStr;

	}
	
	public String getIdStrByMap(HashMap[] nodes, String idStr){

		HashMap curNode = null;
		long idNum = -1;
		String result = null;
		
		if(nodes!=null&&nodes.length>0){
			StringBuffer sb = new StringBuffer();
			sb.append( "( " );
			for(int i=0;i<nodes.length;i++){
				curNode = (HashMap)nodes[i];
				idNum = Long.parseLong( curNode.get(idStr).toString());
				sb.append( idNum );
				if(i<nodes.length-1){
					sb.append( ", " );
				}	
			}
			sb.append( " )" );
			result = sb.toString();
		}	
		return result;

	}
	
	public String getSQLCondByStr(String markIdStr){
		
		if(markIdStr==null||"".equals( markIdStr )){
			return null;
		}
		
		String[] markIdArray =  markIdStr.split( "," );
		StringBuffer sb = new StringBuffer();
		sb.append( "( " );
		for(int i=0;i<markIdArray.length;i++){
			sb.append( "'" );
			sb.append( markIdArray[i] );
			sb.append( "'" );
			if(i<markIdArray.length-1){
				sb.append( ", " );
			}
		}
		sb.append( " )" );
		
		return sb.toString();
		
	}
	
	public String mapToString(HashMap[] nodes, String idStr){

		HashMap curNode = null;
		long idNum = -1;
		String result = null;
		
		if(nodes!=null&&nodes.length>0){
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<nodes.length;i++){
				curNode = (HashMap)nodes[i];
				idNum = Long.parseLong( curNode.get(idStr).toString());
				sb.append( idNum );
				sb.append( "," );
			}
			result = sb.toString();
		}	
		return result;

	}
	
	public String stringSplice(HashMap[] nodes, String columnStr){

		HashMap curNode = null;
		String columnContent = "";
		String result = null;
		
		if(nodes!=null&&nodes.length>0){
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<nodes.length;i++){
				curNode = (HashMap)nodes[i];
				columnContent = (String) curNode.get(columnStr);
				if(columnContent!=null&&!"".equals( columnContent )){
					sb.append( columnContent );
					if(i<nodes.length-1){
						sb.append( ", " );
					}
				}
			}
			result = sb.toString();
		}	
		return result;

	}

	public void mapAppend(HashMap[] map2){

		if(map2!=null&&map2.length>0){
			for(int i=0;i<map2.length;i++){
				this.childNodesList.add( (HashMap)map2[i] );
			}
		}

	}

	public boolean checkOrgIdExist(long orgId, List userOrgs){

		boolean checkFlg =false;
		HashMap curOrgNode = null;
		long curOrgId = -1;
		if(userOrgs!=null&&userOrgs.size()>0){
			for(int i=0;i<userOrgs.size();i++){
				curOrgNode = (HashMap)userOrgs.get( i );
				curOrgId = Long.parseLong(curOrgNode.get( "ORG_ID" ).toString());
				if(orgId == curOrgId){
					checkFlg = true;
					break;
				}
			}

		}

		return checkFlg;
	}


	public List getChildNodesByUserID(long userId){

		String FUNCTION_NAME = "getChildNodesByUserID() ";
		logger.info( FUNCTION_NAME + "start" );


		try{

			this.childNodesList.clear();
			OrgDealDAO odd = new OrgDealDAO();

			String pidList = null;
			HashMap[] curOrgNodes = null;
			HashMap[] childLevelOrgNodes = null;

			curOrgNodes = odd.getCurOrgNodeByUserID( userId );
			this.mapAppend( curOrgNodes );
			pidList = this.getPIdListByMap( curOrgNodes );

			while(pidList!=null){

				childLevelOrgNodes = odd.getChildNodesByPIdList( pidList );
				this.mapAppend( childLevelOrgNodes );
				pidList = this.getPIdListByMap( childLevelOrgNodes );

			}

			return this.childNodesList;


		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return null;
	}

	public List getChildNodesByOrgId(long orgId){

		String FUNCTION_NAME = "getChildNodesByOrgId() ";
		logger.info( FUNCTION_NAME + "start" );


		try{

			this.childNodesList.clear();
			OrgDealDAO odd = new OrgDealDAO();

			String pidList = null;
			HashMap[] curOrgNodes = null;
			HashMap[] childLevelOrgNodes = null;

			curOrgNodes = odd.getCurOrgNodeByOrgId( orgId );
			this.mapAppend( curOrgNodes );
			pidList = this.getPIdListByMap( curOrgNodes );

			while(pidList!=null){

				childLevelOrgNodes = odd.getChildNodesByPIdList( pidList );
				this.mapAppend( childLevelOrgNodes );
				pidList = this.getPIdListByMap( childLevelOrgNodes );

			}

			return this.childNodesList;


		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return null;
	}
	public List getChildNodesByOrgIdClient(long orgId){

		String FUNCTION_NAME = "getChildNodesByOrgId() ";
		logger.info( FUNCTION_NAME + "start" );


		try{

			this.childNodesList.clear();
			OrgDealDAO odd = new OrgDealDAO();

			String pidList = null;
			HashMap[] curOrgNodes = null;
			HashMap[] childLevelOrgNodes = null;

			curOrgNodes = odd.getCurOrgNodeByOrgId( orgId );
			this.mapAppend( curOrgNodes );
			pidList = this.getPIdListByMap( curOrgNodes );

			while(pidList!=null){

				childLevelOrgNodes = odd.getChildNodesByOrgIdClient( pidList );
				this.mapAppend( childLevelOrgNodes );
				pidList = this.getPIdListByMap( childLevelOrgNodes );

			}

			return this.childNodesList;


		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return null;
	}
	public List getFirstChildNodesByOrgId(long orgId){

		String FUNCTION_NAME = "getFirstChildNodesByOrgId() ";
		logger.info( FUNCTION_NAME + "start" );


		try{

			this.childNodesList.clear();
			OrgDealDAO odd = new OrgDealDAO();

			String pidList = null;
			HashMap[] curOrgNodes = null;
			HashMap[] childLevelOrgNodes = null;

			curOrgNodes = odd.getCurOrgNodeByOrgId( orgId );
//			this.mapAppend( curOrgNodes );
			pidList = this.getPIdFirstListByMap( curOrgNodes );

//			while(pidList!=null){

				childLevelOrgNodes = odd.getFirstNodesByPIdList( pidList );
				this.mapAppend( childLevelOrgNodes );
//				pidList = this.getPIdListByMap( childLevelOrgNodes );

//			}

			return this.childNodesList;


		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return null;
	}
	
	public List getCurrentNodesByOrgId(long orgId){

		String FUNCTION_NAME = "getCurrentNodesByOrgId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			this.childNodesList.clear();
			OrgDealDAO odd = new OrgDealDAO();

			HashMap[] curOrgNodes = odd.getCurOrgNodeByOrgId( orgId );
			this.mapAppend( curOrgNodes );

			return this.childNodesList;


		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return null;
	}
	
	public List getFirstAndSelfChildNodesByOrgId(long orgId){

		String FUNCTION_NAME = "getFirstChildNodesByOrgId() ";
		logger.info( FUNCTION_NAME + "start" );


		try{

			this.childNodesList.clear();
			OrgDealDAO odd = new OrgDealDAO();

			String pidList = null;
			HashMap[] curOrgNodes = null;
			HashMap[] childLevelOrgNodes = null;

			curOrgNodes = odd.getCurOrgNodeByOrgId( orgId );
			this.mapAppend( curOrgNodes );
			pidList = this.getPIdFirstListByMap( curOrgNodes );

//			while(pidList!=null){

				childLevelOrgNodes = odd.getFirstNodesByPIdList( pidList );
				this.mapAppend( childLevelOrgNodes );
//				pidList = this.getPIdListByMap( childLevelOrgNodes );

//			}

			return this.childNodesList;


		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return null;
	}
	
	public boolean checkOwnMachineIDs(List delOrgList){

		String FUNCTION_NAME = "checkOwnMachineIDs() ";
		logger.info( FUNCTION_NAME + "start" );

		boolean owningFlg = false;
		try{
	
			if(delOrgList!=null&&delOrgList.size()>0){
				
				OrgDealDAO odd = new OrgDealDAO();
				String inOrgId = this.getIdStrByList( delOrgList );
				if(inOrgId!=null){
					HashMap[] macIds = odd.getMachineInfosByOrgid(inOrgId);					
					StringBuffer sb = new StringBuffer();
					if(macIds!=null&&macIds.length>0){
						for(int i=0;i<macIds.length;i++){
							sb.append( macIds[i].get( "MACHINE_MARK" ).toString() );
							if(i<macIds.length-1){
								sb.append( ", " );
							}
						}
						logger.info( FUNCTION_NAME + "当前组织下存在挂载端机信息：" + sb.toString() );
						owningFlg = true;
					}
				}

			}
			
			return owningFlg;
			
		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}
		
		return false;

	}
	
	public void deleteOrgCorrelatedRecords(List delOrgList){

		String FUNCTION_NAME = "deleteOrgCorrelatedRecords() ";
		logger.info( FUNCTION_NAME + "start" );


		try{
	
			if(delOrgList!=null&&delOrgList.size()>0){
				
				OrgDealDAO odd = new OrgDealDAO();
				HashMap curOrgNode = null;
				HashMap[] deptNodes = null;
				long curOrgId = -1;
				long curDptId = -1;
				for(int i=0;i<delOrgList.size();i++){
					curOrgNode = (HashMap)delOrgList.get( i );
					curOrgId = Long.parseLong( curOrgNode.get( "ORG_ID" ).toString() );
					// 在"组织表"中删除该组织Id对应的记录
					odd.deleteOrgByOrgID( curOrgId );
					// 在"部门表"中查找该组织Id对应的记录
					deptNodes = odd.getDepartmentsByOrgID( curOrgId );
					if(deptNodes!=null&&deptNodes.length>0){
						for(int j=0;j<deptNodes.length;j++){
							curDptId = Long.parseLong( deptNodes[j].get( "DEPARTMENT_ID" ).toString() );
							// 在"角色_组织映射表"中删除该部门Id对应的映射记录
							odd.deleteRoleDptMappingByDptId( curDptId );
						}
					}
					// 在"部门表"中删除该组织Id对应的所有部门记录
					odd.deleteDepartmentsByOrgID( curOrgId );
					// 在"角色_组织映射表"中删除该组织Id对应的映射记录
					odd.deleteRoleOrgMappingByOrgID( curOrgId );
	
				}
				
			}
			
		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

	}
	
	public int getCurOrgLevelbyOrgId(long orgId){

		String FUNCTION_NAME = "getCurOrgLevelbyOrgId() ";
		logger.info( FUNCTION_NAME + "start" );


		
		try{
	
			int curOrgLevel = -1;
			
			OrgDealDAO odd = new OrgDealDAO();
			HashMap curOrgNode = null;
			HashMap[] map = odd.getCurOrgNodeByOrgId( orgId );
			if(map!=null&&map.length>0){
				curOrgNode = (HashMap)map[0];
				curOrgLevel = Integer.parseInt( curOrgNode.get( "ORG_LEVEL" ).toString() );
			}
			
			return curOrgLevel;
			
		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}
		return -1;

	}
	
	public HashMap[] getMasterRolesByUserId(long userId){

		String FUNCTION_NAME = "getMasterRolesByUserId() ";
		logger.info( FUNCTION_NAME + "start" );


		try{

			// 当前用户所在组织及其所有子组织信息取得
			List orgList = this.getChildNodesByUserID( userId );
			String orgIdStr = this.getIdStrByList( orgList );
			
			DptDealDAO ddd = new DptDealDAO();
			// 所有部门信息取得
			HashMap[] dptMap = ddd.getAllDptsByOrgIdStr(orgIdStr);
			String dptIdStr = this.getIdStrByMap( dptMap, "DEPARTMENT_ID" );
			
			UserDao ud = new UserDao();
			// 所用用户信息取得
			HashMap[] userMap = ud.getAllUsersByOrgIdStr(orgIdStr);
			String userIdStr = this.getIdStrByMap( userMap, "USER_ID" );
			
			RoleDealDAO rdd = new RoleDealDAO();
			// 所有角色信息取得
			HashMap[] roleMap = rdd.getRolesByODUStr( orgIdStr, dptIdStr, userIdStr );
			String roleIdStr = this.getIdStrByMap( roleMap, "ROLE_ID" );
			
			HashMap[] result = null;
			if(roleIdStr!=null){
				result = rdd.getRolesByRoleIdStr( roleIdStr );
			}			

			return result;

		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return null;

	}
	
	/**
	 * 特定角色处理，将hashMap[]中包含的默认的两级管理员角色记录删除掉
	 * @param userId
	 * @return
	 */
	public HashMap[] roleMapDeal(HashMap[] nodes, String removeStr, String objName){

		String FUNCTION_NAME = "roleMapDeal() ";
		logger.info( FUNCTION_NAME + "start" );

		List nodeList = new ArrayList();
		HashMap[] returnMap = null;
		
		try{

			String[] removeItems = null;
			if(removeStr!=null&&!"".equals( removeStr )){
				removeItems = removeStr.split( "," );
			}else{
				return nodes;
			}
			HashMap node = null;
			String nodeName = "";
			int count =0;
			if(nodes!=null&&nodes.length>0){
				for(int i=0;i<nodes.length;i++){
					node = (HashMap) nodes[i];
					nodeName = (String) node.get( objName );
					count=0;
					for(int j=0;j<removeItems.length;j++){
						if(!removeItems[j].equals(nodeName)){
							count++;
						}else{
							break;
						}
					}
					if(count==removeItems.length){
						nodeList.add( node );
					}
				}
				returnMap = (HashMap[]) nodeList.toArray(new HashMap[0]);
	
			}
					
			return returnMap;

		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return null;

	}
	
	/**
	 * 拼接页面显示reportList(checkbox)
	 * 如果stgId=-1,标识策略添加,直接拼接所有报表列表(非选中);
	 * 否则，stgId具有的报表选项的复选框处于选中状态。
	 * @param stgId
	 * @return
	 */
	public String getReportListHtml(long stgId){
		
		String FUNCTION_NAME = "getReportListHtml() ";
		logger.info( FUNCTION_NAME + "start" );

		String result = "";
		
		try{

			StrategyDealDAO sdd = new StrategyDealDAO();
			
			HashMap[] allReportList = sdd.getAllReportNameList();
			HashMap[] ownReportList = null;
			TaskBean taskBean = sdd.getTaskBeanByStgId( stgId );
			
			if(taskBean!=null){
				String repMarkIdStr = taskBean.getReportNameList();
				String sqlCond = this.getSQLCondByStr( repMarkIdStr );
				ownReportList = sdd.getReportNameListByMarkIdStr( sqlCond );
			}
			
			if(allReportList!=null&&allReportList.length>0){
				HashMap curReport = null;
				boolean containFlg = false;
				for(int i=0;i<allReportList.length;i++){
					containFlg = false;
					curReport = (HashMap) allReportList[i];
					if(ownReportList==null){
						containFlg = false;
					}else{
						for(int j=0;j<ownReportList.length;j++){
							if(Long.parseLong( (String) curReport.get( "REPORT_ID" ))
									== Long.parseLong( (String) ownReportList[j].get( "REPORT_ID" ))){
								containFlg = true;
								break;
							}
						}
					}
					result += "<div><input type='checkbox' name='report' value='" + (String) curReport.get( "MARK_ID" ) + "' ";
					if(containFlg){
						result += "checked='checked'";
					}
					result += " /> " + (String) curReport.get( "REPORT_NAME" ) + "</div> ";
					
				}
			}
				
			return result;

		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return "";
		
		
	}
	
	
	
	
	
	
	/**
	 * 拼接页面显示roleList
	 * @param userId
	 * @return
	 */
	public String getRoleListHtml(String selRoleStr, String allRoleStr){

		String FUNCTION_NAME = "getRoleListHtml() ";
		logger.info( FUNCTION_NAME + "start" );

		String result = "";
		
		try{

			RoleDealDAO rdd = new RoleDealDAO();
			
			if(allRoleStr!=null&&!"".equals(allRoleStr)){
				String allRoleIdStr = "( " + allRoleStr.substring( 0, allRoleStr.length()-1 ) + " )";
				HashMap[] allRolesMap = rdd.getRolesByRoleIdStr( allRoleIdStr );
				if(allRolesMap!=null&&allRolesMap.length>0){
					HashMap curRole = null;
					boolean containFlg = false;
					for(int i=0;i<allRolesMap.length;i++){
						// 每次循环前初始化设置flg值
						containFlg = false;
						curRole = (HashMap) allRolesMap[i];
						if(selRoleStr==null||"".equals( selRoleStr )){
							containFlg = false;
						}else{
							String[] selRoles = selRoleStr.split( "," );
							for(String curRoleId:selRoles){
								if(((String) curRole.get( "ROLE_ID" )).equals(curRoleId)){
									containFlg = true;
									break;
								}
							}
						}
						result += "<div><input type='checkbox' name='role' value='" + (String) curRole.get( "ROLE_ID" ) + "' ";
						if(containFlg){
							result += "checked='checked'";
						}
						result += " /> " + (String) curRole.get( "ROLE_NAME" ) + "</div> ";
						
					}
				}
			}
				
			return result;

		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return "";

	}
	
	/**
	 * 根据策略中的"发送方式"和"接收方角色列表"取得对应的邮件或者手机列表
	 * @param sendType
	 * @param deliverRoleList
	 * @return
	 */
	public String getDeliverTerminals(String sendType, String deliverRoleList){
		
		String FUNCTION_NAME = "getDeliverTerminals() ";
		logger.info( FUNCTION_NAME + "start" );

		String result = "";

		try{

			RoleDealDAO rdd = new RoleDealDAO();
			UserDao ud = new UserDao();
			/****** 角色－组织映射表中对应记录取得 ********/
			// 所有用户信息取得
			String userIdStr = this.getIdStrByMap( rdd.getRoleMappingRecordsByRoleIdStr( "1", deliverRoleList ), "OBJECT_ID" );
			// 所有组织信息取得
			String orgIdStr = this.getIdStrByMap( rdd.getRoleMappingRecordsByRoleIdStr( "2", deliverRoleList ), "OBJECT_ID" );
			// 所有部门信息取得
			String dptIdStr = this.getIdStrByMap( rdd.getRoleMappingRecordsByRoleIdStr( "3", deliverRoleList ), "OBJECT_ID" );
			
			
			String deliverUserIdList = this.getIdStrByMap( ud.getUsersByODUStr( orgIdStr, dptIdStr, userIdStr ), "USER_ID" );
			String columnStr = "";
			if(Define.CHAR_IDENTIFY_ONE.equals( sendType )){
				columnStr = "EMAIL";
			}else if(Define.CHAR_IDENTIFY_TWO.equals( sendType )){
				columnStr = "CELLPHONE";
			}
			result = this.stringSplice( ud.getUsersByUserIdStr( columnStr, deliverUserIdList ), columnStr );

			return result;

		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return "";


	}
	
	/** orgID--Mail Strings
	 * @param sendType
	 * @param deliverRoleList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap<String,String> getDeliverTerminalsMap(String sendType, String deliverRoleList){
		
		String FUNCTION_NAME = "getDeliverTerminalsMap() ";
		logger.info( FUNCTION_NAME + "start" );
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> mapOrgMail = new HashMap<String,String>();
		String orgID = "";
		String userIDTemp = "";
		String userID = "";
		String result = "";

		try{

			RoleDealDAO rdd = new RoleDealDAO();
			UserDao ud = new UserDao();
			/****** 角色－组织映射表中对应记录取得 ********/
			// 所有用户信息取得
			String userIdStr = this.getIdStrByMap( rdd.getRoleMappingRecordsByRoleIdStr( "1", deliverRoleList ), "OBJECT_ID" );
			// 所有组织信息取得
			String orgIdStr = this.getIdStrByMap( rdd.getRoleMappingRecordsByRoleIdStr( "2", deliverRoleList ), "OBJECT_ID" );
			// 所有部门信息取得
			String dptIdStr = this.getIdStrByMap( rdd.getRoleMappingRecordsByRoleIdStr( "3", deliverRoleList ), "OBJECT_ID" );
			
			
			String deliverUserIdList = this.mapToString( ud.getUsersByODUStr( orgIdStr, dptIdStr, userIdStr ), "USER_ID" );
			String[] userIDSplit = deliverUserIdList.split(",");
			for(int i = 0; i < userIDSplit.length; i++){
				orgID = String.valueOf(ud.getUsersByUserIdStr( "ORG_ID", "("+userIDSplit[i]+")" )[0].get("ORG_ID"));
				if(map.containsKey(orgID)){
					userIDTemp = map.get(orgID);
					if( -1 == userIDTemp.indexOf(userIDSplit[i])){
						map.put(orgID, userIDTemp + "," + userIDSplit[i]);
					}
				} else {
					map.put(orgID, userIDSplit[i]);
				}
				
			}
			
			String columnStr = "";
			if(Define.CHAR_IDENTIFY_ONE.equals( sendType )){
				columnStr = "EMAIL";
			}else if(Define.CHAR_IDENTIFY_TWO.equals( sendType )){
				columnStr = "CELLPHONE";
			}
			
		    Iterator it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry entry = (Map.Entry) it.next();
		        String orgIDVal = String.valueOf(entry.getKey());
		        result = this.stringSplice( ud.getUsersByUserIdStr( columnStr, "("+String.valueOf(entry.getValue())+")" ), columnStr );
		        mapOrgMail.put(orgIDVal, result);
		    }
		}
		catch( Exception ex ){
			logger.warn( FUNCTION_NAME + "异常情况发生" , ex );

		}
		finally{
			logger.info( FUNCTION_NAME + "end" );
		}

		return mapOrgMail;
	}
	
	
	/**
	 * 
	 * 
	 * @param str
	*/
	public boolean checkUserRecord(String str, int index, User user) {
		String[] info = str.split(",");
		if(info.length != 11 ){
			logger.debug("第"+index+"行数据格式不正确,应该使用符号','隔开!");
			System.out.println("第"+index+"行数据格式不正确,应该使用符号','隔开!");
			return false;
		}
		try {

			// 组织名称
			String orgName = info[0];
			OrgDealDAO odd = new OrgDealDAO();
			long orgId = odd.getOrgIdByOrgName( orgName );
			if(orgId==-1){
				logger.debug("第"+index+"行数据用户所属组织名称不存在，请填写已经存在的正确的组织名称!");
				System.out.println("第"+index+"行数据用户所属组织名称不存在，请填写已经存在的正确的组织名称!");
				return false;
			}
			// 部门名称
			String dptName = info[1];
			DptDealDAO ddd = new DptDealDAO();
			long dptId = ddd.getDptIdByDptNameAndOrgId( orgId, dptName );
			if(dptId==-1){
				logger.debug("第"+index+"行数据用户所在部门名称不存在，请填写已经存在的正确的部门名称!");
				System.out.println("第"+index+"行数据用户所在部门名称不存在，请填写已经存在的正确的部门名称!");
				return false;
			}
			// 用户账号
			String nickName = info[2];
			UserDao ud = new UserDao();
			long inUserId = ud.getUserIdByNickName( nickName );
			if(inUserId!=-1){
				logger.debug("第"+index+"行数据用户账号已经存在，请填写不存在的正确的用户账号!");
				System.out.println("第"+index+"行数据用户账号已经存在，请填写不存在的正确的用户账号!");
				return false;
			}

			// 登陆用户信息取得
			long userId = Long.parseLong((String)user.getId());
			String userType = user.getType();

			UserBean ub = new UserBean();

			long inputOrgId = orgId;
			long inDptId = dptId;
			String inUserNickname = nickName;
			String inUserPassword = info[3];
			String inUserName = info[4];
			String inUserEmail = info[5];
			String inUserAddress = info[6];
			String inUserFixTel = info[7];
			String inUserOthCont = info[8];
			String inUserDesp = info[9];
			String inUserType = info[10];

			ub.setOrgId( inputOrgId );
			ub.setDptId( inDptId );
			ub.setNickName( inUserNickname );
			ub.setPassword( inUserPassword );
			ub.setUserName( inUserName );
			ub.setEmail( inUserEmail );
			ub.setAddress( inUserAddress );
			ub.setFixedTel( inUserFixTel );
			ub.setMobilePhone( inUserOthCont );
			ub.setUserDesp( inUserDesp );
			ub.setUserType( inUserType );
			ub.setOperator( userId );

			// 用户添加_52/用户授权_55
			if(user.hasRight( String.valueOf( inputOrgId ), String.valueOf( 52 ) )
					&&user.hasRight( String.valueOf( inputOrgId ), String.valueOf( 55 ) )){

				// 插入一条新的记录
				long newUserId = ud.insertUserRecord( ub );
				/************ 授权操作 *********************/
				// 当前登陆用户所在组织层级取得
				HashMap[] curNodeMap = odd.getCurOrgNodeByUserID( userId );
				long curUserOrgLevel = Long.parseLong( (String) curNodeMap[0].get( "ORG_LEVEL" ) );
				// 读入用户记录中所属组织所在层级取得
				HashMap[] selNodeMap = odd.getCurOrgNodeByOrgId( inputOrgId );
				long selOrgLevel = Long.parseLong( (String) selNodeMap[0].get( "ORG_LEVEL" ) );

				// 获取策略表中配置的层级和角色之间对应关系记录
				StrategyDealDAO sdd = new StrategyDealDAO();
				// 读入用户记录中用户类型是管理员，并且其对应组织层级低于当前登陆用户所在组织层级
				if((selOrgLevel>curUserOrgLevel)&&Define.USER_ADMIN.equals(inUserType)){
					// 策略中定义角色Id取得
					String stgRoleId = sdd.getObjEndByObjBegin( String.valueOf( selOrgLevel ) );
					if(stgRoleId!=null&&!"".equals( stgRoleId )){
						// 权限授予
						RoleObjectManagementBean romb = new RoleObjectManagementBean();
						// "角色_组织映射"中用户信息设置
						romb.setObjectType( "1" );
						romb.setObjectId( newUserId );
						romb.setOperator( userId );
						romb.setRoleId( Long.parseLong( stgRoleId ) );
						// 角色_组织映射表中对应组织和角色记录插入
						odd.insertRoleObjectMap( romb );


					}

				}

			}else{
				return false;
			}

			return true;
		} catch (Exception e) {
			return false;			
		}
	}
	

	/**
	 * 组织id获取下级组织id，返回字符串类型，以逗号（,）分隔
	 * @param orgId
	 * @return
	 * @throws SQLException
	 */
	public String getSubOrg(String orgId) {
		OrgDealDAO dao = new OrgDealDAO();
		String subOrgStr = dao.getSubOrg(orgId);
		return subOrgStr;
	}
	/**
	 * 组织id获取下级组织id，返回字符串数组
	 * @param orgId
	 * @return
	 * @throws SQLException
	 */
	public List<String> getSubOrgList(String orgId) {
		List<String> orgList = new ArrayList<String>();
		String subOrg = getSubOrg(orgId);
		orgList = StringToList(subOrg);
		return orgList;
	}

	/**
	 * 字符串转字符串数组
	 * @param subOrg
	 * @return
	 */
	private List<String> StringToList(String subOrg) {
		// TODO Auto-generated method stub
		List<String> returnList = new ArrayList<String>();
		String []orgs = subOrg.split(",");
		for(int i = 0;i<orgs.length;i++){
			returnList.add(orgs[i]);
		}
		return returnList;
	}
}
