package th.dao.log;

import java.sql.SQLException;
import java.util.HashMap;

import th.action.OrgDealAction;
import th.dao.BaseDao;
import th.util.StringUtils;

public class MessageDao extends BaseDao {

	public HashMap<String, String>[] searchMesssageList(String orgID) {
		return null;
	}

	public HashMap<String, String>[] searchMesssageList(String orgID,
			String machineID, String sortType) {
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
		stringBuffer.append(" TCME.operatetime,");
		stringBuffer.append(" TCME.msg_id,");
		stringBuffer.append(" TCME.msg_content,");
		stringBuffer.append(" TCME.operator,");
		stringBuffer.append(" ORG.ORG_NAME,");
		stringBuffer.append(" USG.name,");
		stringBuffer.append(" TCM.machine_mark ");
		stringBuffer.append(" FROM ");
		stringBuffer.append(" tb_ccb_message TCME ,");
		stringBuffer.append(" tb_ccb_machine TCM ,");
		stringBuffer.append(" tb_ccb_organization ORG ,");
		stringBuffer.append(" tb_ccb_user USG ");
		stringBuffer.append(" WHERE");
		stringBuffer.append(" TCME.MACHINE_ID = TCM.MACHINE_ID");
		stringBuffer.append(" AND USG.user_id = TCME.operator ");
		stringBuffer.append(" AND ORG.org_id = TCM.org_id ");
		stringBuffer.append(" AND ORG.org_id in " + "(" + orgList + ")");
		if (machineID != null) {
			stringBuffer.append(" AND TCM.machine_mark = ?");
		}
		if (sortType != null) {
			stringBuffer.append(" ORDER BY TCME.operatetime " + sortType );
		}else{
			stringBuffer.append(" ORDER BY TCME.operatetime DESC");
		}
		logger.info("需要被执行的sql语句： " + stringBuffer.toString());
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			if (machineID != null) {
				stmt.setString(1, machineID);
			}
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
