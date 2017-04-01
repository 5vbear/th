package th.dao.machine;

import java.sql.SQLException;
import java.util.HashMap;

import th.dao.AdvertDao;
import th.dao.ChannelDAO;
import th.dao.AvailablePage.AvailablePageDAO;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-9-13
 * @author PSET
 * @since JDK1.6
 */
public class SychDAO extends MachineDAO {
	
	/**
	 * 获取节目单
	 * 
	 * @param machineID
	 * @return
	 * @throws SQLException
	*/
	public HashMap[] getMacAdList(String machineID) throws SQLException{
		return new AdvertDao().getBillIssuedList(Long.parseLong(machineID));
	}
	
	/**
	 * 获取频道
	 * 
	 * @param machineID
	 * @return
	 * @throws SQLException
	*/
	public HashMap[] getMacChannel(String machineID) throws SQLException{
		return new ChannelDAO().getAllChannelByOrgIDInit("0", getOrgIDByMacID(machineID).get("ORG_ID").toString());
	}
	
	/**
	 * 获取快速入口
	 * 
	 * @param machineID
	 * @return
	 * @throws SQLException
	*/
	public HashMap[] getMacQuickEntry(String machineID) throws SQLException{
		return new ChannelDAO().getAllChannelByOrgID("1", getOrgIDByMacID(machineID).get("ORG_ID").toString());
	}
	
	/**
	 * 获取白名单
	 * 
	 * @param machineID
	 * @return
	 * @throws SQLException
	*/
	public HashMap[] getMacWhiteList(String machineID) throws SQLException{
		return new AvailablePageDAO().getAvailableList(getOrgIDByMacID(machineID).get("ORG_ID").toString());
	}
	
	/**
	 * 根据端机ID取得对应组织ID
	 * @param machineID
	 * @return
	 * @throws SQLException
	 */
	public HashMap getOrgIDByMacID(String machineID) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("select * ");
		sb.append("  from tb_ccb_machine t");
		sb.append(" where t.machine_id = ? and t.status!='3'");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(machineID));
			HashMap[] maps = select();
			if(maps!=null && maps.length > 0){
				return maps[0];
			}else{
				return new HashMap();
			}
		} finally  {
			releaseConnection();
		}		
		
	}
}
