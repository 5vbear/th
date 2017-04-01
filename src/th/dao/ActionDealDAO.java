package th.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import th.com.util.Define;
import th.db.DBAccess;
import th.user.User;

/**
 * 权限处理
 * 
 * @author TH
 * 
 */
public class ActionDealDAO extends DBAccess {

	protected Logger logger = Logger.getLogger(this.getClass());

	public ActionDealDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 由用户ID，组织ID，部门ID，取得用户的权限合集
	 * 
	 * @param id
	 * @param org_id
	 * @param department_id
	 * @return
	 */
	public List<String> getActionListByUserId(String id, String org_id,
			String department_id) {
		// TODO Auto-generated method stub

		logger.info("开始查找用户权限，当前用户的ID： " + id + "用户所属组织ID： " + org_id
				+ "用户所属部门： " + department_id);
		ResultSet rs = null;
		List<String> actionList = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		OrgDealDAO orgDao = new OrgDealDAO();
		String subOrgId = orgDao.getSubOrg(org_id);

		sb.append(" SELECT DISTINCT");
		sb.append(" am.action_id");
		sb.append(" FROM");
		sb.append(" tb_action_management am");
		sb.append(" WHERE");
		sb.append(" am.role_id IN(");
		sb.append(" SELECT");
		sb.append(" rm.role_id");
		sb.append(" FROM");
		sb.append(" tb_role_management rm");
		sb.append(" WHERE");
		sb.append(" (");
		sb.append(" rm.object_id = ?");
		sb.append(" AND rm.object_type = '1'");
		sb.append(" )OR(");
		sb.append(" rm.object_id = ?");
		sb.append(" AND rm.object_type = '3'");
		sb.append(" )OR(");
		sb.append(" rm.object_id IN(");
		if (subOrgId != null && !"".equals(subOrgId)) {
			sb.append(Long.parseLong(org_id) + "," + subOrgId);
		} else {
			sb.append(Long.parseLong(org_id));
		}
		sb.append("	)");
		sb.append(" AND rm.object_type = '2'");
		sb.append(" )");
		sb.append(" )");
		logger.info("执行的SQL语句是" + sb.toString());

		try {
			this.connection();
			PreparedStatement stat = con.prepareStatement(sb.toString());
			stat.setLong(1, Long.parseLong(id));
			stat.setLong(2, Long.parseLong(department_id));
			rs = stat.executeQuery();
			StringBuffer sbAction=new StringBuffer();
			if (rs != null) {
				while (rs.next()) {
					long actionId = rs.getLong("ACTION_ID");
					actionList.add(String.valueOf(actionId));
					sbAction.append(actionId+" ");
				}
				logger.info("该用户的权限ID包括：" + sbAction.toString());
			}

		} catch (SQLException e) {
			logger.info("检索用户权限失败。用户ID： " + id);
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					releaseConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return actionList;

	}

	/**
	 * 权限判定
	 * 
	 * @param userId
	 * @param actionId
	 * @return
	 */
	public boolean hasActionRight(String userId, String actionId) {

		// TODO Auto-generated method stub

		logger.info("开始判定用户权限，当前用户的ID： " + userId + "权限ID： " + actionId);

		String subOrgs = "";
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT");
		sb.append(" suborg(o.org_id) as sub_org");
		sb.append(" FROM");
		sb.append(" tb_ccb_user r,");
		sb.append(" tb_ccb_organization o");
		sb.append("	WHERE");
		sb.append(" r.org_id = o.org_id");
		sb.append(" AND r.user_id = ?");
		try {
			this.connection();
			PreparedStatement stat = con.prepareStatement(sb.toString());
			stat.setLong(1, Long.parseLong(userId));
			rs = stat.executeQuery();
			if (rs.next()) {
				subOrgs = rs.getString("sub_org");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sb = new StringBuffer();
		sb.append(" SELECT");
		sb.append(" u.user_id");
		sb.append(" FROM");
		sb.append(" tb_role_management rm,");
		sb.append(" tb_action_management am,");
		sb.append(" tb_ccb_user u");
		sb.append(" WHERE");
		sb.append(" am.role_id = rm.role_id");
		sb.append(" AND(");
		sb.append(" (");
		sb.append(" rm.object_id = u.user_id");
		sb.append(" AND rm.object_type = '1'");
		sb.append(" )");
		sb.append(" OR(");
		sb.append(" rm.object_id = u.department_id");
		sb.append(" AND rm.object_type = '3'");
		sb.append(" )OR(");
		// 对subOrgs进行非空判断，排除叶子节点没有子节点SQL出错的可能性
		if (subOrgs != null && !"".equals(subOrgs)) {
			sb.append(" ( rm.object_id IN(");
			sb.append(subOrgs);
			sb.append(" ) or");
			sb.append(" rm.object_id = u.org_id )");
		} else {
			sb.append(" rm.object_id = u.org_id");
		}
		sb.append(" AND rm.object_type = '2'");
		sb.append(" )");
		sb.append(" )");
		sb.append(" AND");
		sb.append(" u.user_id = ?");
		sb.append(" AND");
		sb.append(" am .action_id = ?");
		logger.info("执行的SQL语句是" + sb.toString());

		try {
			this.connection();
			PreparedStatement stat = con.prepareStatement(sb.toString());
			stat.setLong(1, Long.parseLong(userId));
			stat.setLong(2, Long.parseLong(actionId));
			rs = stat.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					return true;
				}
			}

		} catch (SQLException e) {
			logger.info("检索用户权限失败。用户ID： " + userId);
			e.printStackTrace();
			return false;
		} finally {
			if (con != null) {
				try {
					releaseConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 获取管理员权限
	 * 
	 * @param id
	 * @return
	 */
	public List<String> getAdminActionList() {
		// TODO Auto-generated method stub

		logger.info("开始检索系统管理员权限");

		ResultSet rs = null;
		List<String> actionList = new ArrayList<String>();

		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT");
		sb.append(" action_id");
		sb.append(" FROM");
		sb.append(" mt_ccb_action");
		logger.info("执行的SQL语句是" + sb.toString());

		try {
			this.connection();
			PreparedStatement stat = con.prepareStatement(sb.toString());
			rs = stat.executeQuery();
			if (rs != null) {
				StringBuffer sbAction = new StringBuffer();
				while (rs.next()) {
					long actionId = rs.getLong("ACTION_ID");
					actionList.add(String.valueOf(actionId));
					sbAction.append(actionId + ",");

				}
				logger.info("该用户的权限ID包括：" + sbAction.toString());
			}

		} catch (SQLException e) {
			logger.info("检索用户权限失败。");
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					releaseConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return actionList;

	}

	/**
	 * 判定当前用户是否有操作权限
	 * 
	 * @param userId
	 *            用户id
	 * @param deal_org_id
	 *            用户执行操作所属org
	 * @param user_orgId
	 *            用户所在org
	 * @param actionId
	 *            用户执行操作所需权限
	 * @return
	 */
	public boolean isAuthorized(String userId, String user_orgId,
			String deal_org_id, String actionId) {
		// TODO Auto-generated method stub

		logger.info("开始判定用户当前操作权限，当前用户的ID： " + userId + "执行操作所需权限ID： "
				+ actionId + "执行操作所在组织ID" + deal_org_id + "用户所在组织ID"
				+ user_orgId);
		boolean hasOrgRight = false;
		if (user_orgId.equals(Define.TOP_ORG) || user_orgId.equals(deal_org_id)) {
			hasOrgRight = true;
		} else {
			hasOrgRight = hasOrgRight(user_orgId, deal_org_id);
		}
		if (hasOrgRight) {
			if (this.hasActionRight(userId, actionId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判定用户操作的模块是否在其组织权限下
	 * 
	 * @param user_orgId
	 * @param deal_org_id
	 * @return
	 */
	private boolean hasOrgRight(String user_orgId, String deal_org_id) {
		// TODO Auto-generated method stub

		OrgDealDAO orgDao = new OrgDealDAO();
		String orgList = orgDao.getSubOrg(user_orgId);
		String[] orgs = orgList.split(",");
		for (String subOrg : orgs) {
			if (subOrg.equals(deal_org_id)) {
				return true;
			}
		}
		return false;
	}
}
