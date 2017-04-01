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

import th.com.property.LocalProperties;
import th.com.util.Define;
import th.dao.OrgDealDAO;
import th.dao.machine.AuditDAO;
import th.entity.MachineBean;
import th.user.User;
import th.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
@SuppressWarnings("rawtypes")
public class PreAuditAction extends BaseAction {
	/**
	 * 取得一级行组织
	 * 
	 * @param req
	 * @throws Exception
	 */
	public static String getFirstOrgNodes() throws Exception {
		OrgDealAction orgDealAction = new OrgDealAction();
		long orgCode = new OrgDealDAO().getTopParentOrgID();	
		List<HashMap> orgNodesList = orgDealAction.getFirstChildNodesByOrgId( orgCode );
		MonitorAction monitorAction = new MonitorAction();
		String orgNodes = monitorAction.buildOrgOption( orgNodesList );

		return orgNodes;

	}
	/**
	 * 取得二级行组织
	 * 
	 * @param req
	 * @throws Exception
	 */
	public static String getSecondOrgNodes() throws Exception {
		OrgDealAction orgDealAction = new OrgDealAction();
		long orgCode = new OrgDealDAO().getTopParentOrgID();
		
		MonitorAction monitorAction = new MonitorAction();
		OrgDealDAO odd = new OrgDealDAO();
		List<HashMap> orgNodesList = orgDealAction.getFirstChildNodesByOrgId( orgCode );
		List<HashMap> orgSecondNodesList = null;
		String orgNodes = "";
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for (int i=0;i<orgNodesList.size();i++){
			HashMap orgNode = orgNodesList.get(i);
			long orgSID = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
			orgSecondNodesList = mapAppend(odd.getFirstNodesByPIdList(String.valueOf(orgSID)));
			orgNodes = monitorAction.buildSecondOrgOption( orgSecondNodesList );
			sb.append("\"");
			sb.append(orgSID);
			sb.append("\"");
			sb.append(":");
			if(!"".equals(orgNodes)){
				sb.append(orgNodes.substring(0, orgNodes.length()-1));
			} else {
				sb.append( "[ { \"id\": -100, \"pId\": -100, \"name\": \"------\", open:true }]" );
			}
			if(i<orgNodesList.size()-1){
				sb.append( "," );
			}
		}
		sb.append("}");

		return sb.toString();

	}
	
	private static List mapAppend(HashMap[] map2){
		List childNodesList = new ArrayList();
		if(map2!=null&&map2.length>0){
			for(int i=0;i<map2.length;i++){
				childNodesList.add( (HashMap)map2[i] );
			}
		}
		return childNodesList;
	}
	public int appendRemark(String firstOrdID, String secondOrgID, String mac) throws SQLException{
		String orgID = "";
		if("-100".equals(secondOrgID)){
			orgID = firstOrdID;
		} else {
			orgID = secondOrgID;
		}
		AuditDAO auditDAO = new AuditDAO();
		return auditDAO.auditRemark(mac, orgID);
	}
}
