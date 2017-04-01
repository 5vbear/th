package th.dao.log;

import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import th.action.OrgDealAction;
import th.dao.BaseDao;
import th.entity.log.AdviceBean;
import th.util.StringUtils;

public class AdviceDao extends BaseDao {
	protected Logger logger = Logger.getLogger(this.getClass());

	public void saveAdvice(AdviceBean aBean) {
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("INSERT INTO TB_CCB_GUESTBOOK ");
		stringBuffer
				.append(" (idea_id,idea_type,machine_id,idea_content,operatetime,expression,email,phone) ");
		stringBuffer.append(" VALUES");
		stringBuffer.append(" (NEXTVAL('SEQ_TB_CCB_GUESTBOOK'),");
		stringBuffer.append(" ?,");
		stringBuffer
				.append(" ( SELECT tb_ccb_machine.machine_id FROM tb_ccb_machine WHERE tb_ccb_machine.mac = ? ),");//
		stringBuffer.append(" ?,");
		stringBuffer.append(" date_trunc('second', CURRENT_TIMESTAMP),");
		stringBuffer.append(" ?,");
		stringBuffer.append(" ?,");
		stringBuffer.append(" ?)");
		logger.info("将会被执行的SQL语句是： " + stringBuffer.toString());
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.setString(1, aBean.getType());
			stmt.setString(2, aBean.getMac());
			stmt.setString(3, aBean.getContent());
			stmt.setString(4, aBean.getExpression());
			if(null==aBean.getEmail()||"".equals(aBean.getEmail()))
			{
				stmt.setString(5, "");
			}else{
				stmt.setString(5, aBean.getEmail());
			}
			if(null==aBean.getTelephone()||"".equals(aBean.getTelephone()))
			{
				stmt.setString(6, "");
			}else{
				stmt.setString(6, aBean.getTelephone());
			}
			stmt.addBatch();
			insertBatch();
			commit();
		} catch (SQLException ex) {
			logger.error("SQL select execution failure...", ex);
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
	}

	public HashMap<String, String>[] searchAdviceList(String orgID) {
		return null;
	}

	public HashMap<String, String>[] searchAdviceList(String orgID,
			String machineID) {
		OrgDealAction ora = new OrgDealAction();
		String orgList = ora.getSubOrg(orgID);
		if (StringUtils.isBlank(orgList)) {
			orgList = orgID;
		} else {
			orgList = orgList + "," + orgID;
		}
		logger.info("组织ID字符串 : " + orgList);
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("SELECT ");
		stringBuffer.append(" GBOOK.IDEA_ID,");
		stringBuffer.append(" GBOOK.IDEA_TYPE,");
		stringBuffer.append(" GBOOK.MACHINE_ID,");
		stringBuffer.append(" GBOOK.IDEA_CONTENT,");
		stringBuffer.append(" GBOOK.OPERATETIME,");
		stringBuffer.append(" GBOOK.EMAIL,");
		stringBuffer.append(" GBOOK.PHONE,");
		stringBuffer.append(" GBOOK.EXPRESSION,");
		stringBuffer.append(" ORG.ORG_NAME,");
		stringBuffer.append(" TCM.machine_mark ");
		stringBuffer.append(" FROM ");
		stringBuffer.append(" TB_CCB_GUESTBOOK GBOOK,");
		stringBuffer.append(" TB_CCB_MACHINE TCM, ");
		stringBuffer.append(" tb_ccb_organization ORG ");
		stringBuffer.append(" WHERE");
		stringBuffer.append(" GBOOK.MACHINE_ID = TCM.MACHINE_ID");
		stringBuffer.append(" AND ORG.org_id = TCM.org_id ");
		stringBuffer.append(" AND ORG.org_id in " + "(" + orgList + ")");
		if (machineID != null) {
			stringBuffer.append(" AND TCM.machine_mark = ?");
		}

		stringBuffer.append(" ORDER BY GBOOK.operatetime DESC");
		logger.info("需要被执行的sql语句： " + stringBuffer.toString());
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			if (machineID != null) {
				stmt.setString(1, machineID);
			}
			// stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error("SQL select execution failure...", ex);
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return beans;
	}
}
