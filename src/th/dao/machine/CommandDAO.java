package th.dao.machine;

import java.sql.SQLException;
import java.util.HashMap;

import th.util.StringUtils;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class CommandDAO extends MachineDAO {

	/**
	 * 
	 * 
	 * @return
	*/
	public boolean getScrnCapture(String macIDStd) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT id, ");
		sb.append("		  machine_id mid, ");
		sb.append("		  status, ");
		sb.append("		  command_id cid, ");
		sb.append("		  command_content ccontent, ");
		sb.append("		  operatetime otime ");
		sb.append("  FROM tb_command_management ");
		sb.append(" WHERE status = '1' ");
		sb.append("   AND command_id = 6 ");
		sb.append("   AND  machine_id in ( ");
		sb.append(macIDStd);
		sb.append("   ) ");
		boolean result = false;
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			HashMap[] maps = select();
			if(maps.length > 0){
				result = true;
			}
			commit();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally  {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	public boolean getMacStatus(String macIDStd, int operType) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT id, ");
		sb.append("		  machine_id mid, ");
		sb.append("		  status, ");
		sb.append("		  command_id cid, ");
		sb.append("		  command_content ccontent, ");
		sb.append("		  operatetime otime ");
		sb.append("  FROM tb_command_management ");
		sb.append(" WHERE status = '1' ");
		sb.append("   AND command_id = ");
		sb.append(operType);
		sb.append("   AND  machine_id in ( ");
		sb.append(macIDStd);
		sb.append("   ) ");
		boolean result = false;
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			HashMap[] maps = select();
			if(maps.length > 0){
				result = true;
			}
			commit();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally  {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	public String getMacStatus(String macIDStd, String operType) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT status ");
		sb.append("  FROM tb_command_management ");
		sb.append(" WHERE command_id = ");
		sb.append(operType);
		sb.append("   AND  machine_id in ( ");
		sb.append(macIDStd);
		sb.append("   ) ");
		String result = "0";
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			HashMap[] maps = select();
			if(maps != null &&maps.length > 0){
				result = (String) maps[0].get("STATUS");
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		} finally  {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
	}

	/**
	 * 
	 * 
	 * @return
	 * @throws SQLException 
	*/
	public HashMap[] getMacStatus(String macIDStd) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT machine_id mid, cmd.command_id cid, command_name cname ");
		sb.append("	 FROM tb_command_management cmg, mt_command cmd ");
		sb.append(" WHERE cmg.command_id = cmd.command_id ");
		sb.append("   AND machine_id in ( ");
		sb.append(macIDStd);
		sb.append("   ) ");
		sb.append("   AND cmd.command_id != '16' ");
		sb.append("   AND status = '1' ");
		sb.append(" ORDER BY cmg.operatetime DESC ");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			return select();
		} finally  {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
	}

	/**
	 * 
	 * 
	 * @param macIDStd
	 * @return
	*/
	public boolean getClosePCStatus(String macIDStd) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT id, ");
		sb.append("		  machine_id mid, ");
		sb.append("		  status, ");
		sb.append("		  command_id cid, ");
		sb.append("		  command_content ccontent, ");
		sb.append("		  operatetime otime ");
		sb.append("  FROM tb_command_management ");
		sb.append(" WHERE status = '1' ");
		sb.append("   AND command_id = 4 ");
		sb.append("   AND  machine_id in ( ");
		sb.append(macIDStd);
		sb.append("   ) ");
		boolean result = false;
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			HashMap[] maps = select();
			if(maps.length > 0){
				result = true;
			}
			commit();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally  {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}

	/**
	 * 
	 * 
	 * @param macIDStd
	 * @return
	*/
	public boolean getRestartPCStatus(String macIDStd) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT id, ");
		sb.append("		  machine_id mid, ");
		sb.append("		  status, ");
		sb.append("		  command_id cid, ");
		sb.append("		  command_content ccontent, ");
		sb.append("		  operatetime otime ");
		sb.append("  FROM tb_command_management ");
		sb.append(" WHERE status = '1' ");
		sb.append("   AND command_id = 5 ");
		sb.append("   AND  machine_id in ( ");
		sb.append(macIDStd);
		sb.append("   ) ");
		boolean result = false;
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			HashMap[] maps = select();
			if(maps.length > 0){
				result = true;
			}
			commit();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally  {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}

	/**
	 * 
	 * 
	 * @param macIDStd
	 * @param operType
	 * @return
	 * @throws SQLException 
	*/
	public int doOper(String macIDStd, String operType, String content) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("update tb_command_management ");
		sb.append("   set status = 1, ");
		if(StringUtils.isNotBlank(content)){
			sb.append("   command_content = ?, ");
		}
		sb.append("       operatetime = current_timestamp ");
		sb.append(" where machine_id in ( ");
		sb.append(macIDStd);
		sb.append("	)");
		sb.append("	  and command_id = ");
		sb.append(operType);
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			if(StringUtils.isNotBlank(content)){
				stmt.setString(1, content);
			}
			int result = update();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}

	/**
	 * 
	 * 
	 * @param macIDStd
	 * @param operType
	 * @return
	 * @throws SQLException 
	*/
	public int undoOper(String macIDStd, String operType) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("update tb_command_management ");
		sb.append("   set status = 0, ");
		sb.append("       operatetime = current_timestamp ");
		sb.append(" where machine_id in ( ");
		sb.append(macIDStd);
		sb.append("	)");
		sb.append("	  and command_id = ");
		sb.append(operType);
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			int result = update();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}
	
	public void insertMachinePauseHistory(String machineID, String userID) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "INSERT INTO tb_machine_pause_history(" );
			sb.append( "	history_id," );
			sb.append( "	machine_id," );
			sb.append( "	pause_time," );
			sb.append( "	operatetime," );
			sb.append( "	operator" );
			sb.append( ")" );
			sb.append( "VALUES" );
			sb.append( "	(" );
			sb.append( "		nextval('SEQ_TB_MACHINE_INFORMATION')," );
			sb.append( "		?, " );
			sb.append( "		CURRENT_TIMESTAMP," );
			sb.append( "		CURRENT_TIMESTAMP," );
			sb.append( "		?" );
			sb.append( "	);" );


			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(machineID) );
			stmt.setLong( 2, Long.parseLong(userID) );

			insert();
			commit();
		}
		finally {
			releaseConnection();

		}
	}
	
	/**
	 * 端机启用时，更新恢复时间
	 * @param machineID
	 * @return
	 * @throws SQLException
	 */
	public int updateMachinePauseHistory(String machineID) throws SQLException {


		StringBuffer sb = new StringBuffer();
		sb.append("update tb_machine_pause_history set resume_time=CURRENT_TIMESTAMP where machine_id=? and resume_time is null");

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong(1, Long.parseLong(machineID));

			int ret = stmt.executeUpdate();
			con.commit();

			return ret;

		} finally {

			// release処理
			releaseConnection();

		}

	}

	/**
	 * 
	 * 
	 * @param macIDStd
	 * @param userid 
	 * @param content 
	 * @throws SQLException 
	*/
	public void insertMessageHistory(String macIDStd, String content, String userid) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "INSERT INTO tb_ccb_message(" );
			sb.append( "	msg_id, machine_id, msg_content, operatetime, operator)" );
			sb.append( "VALUES (nextval('seq_tb_ccb_message'), ?, ?, CURRENT_TIMESTAMP, ?)" );


			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(macIDStd) );
			stmt.setString( 2, content );
			stmt.setLong( 3, Long.parseLong(userid) );

			insert();
			commit();
		}
		finally {
			releaseConnection();

		}
	}
	
	/**
	 * 
	 * 报废
	 * @param mac 
	 * @param machineID
	 * @throws SQLException 
	*/
	public int insertMachineRetirement(String mac, String machineID) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "INSERT INTO tb_machine_retirement(" );
			sb.append( "	retirement_id, mac, machine_id)" );
			sb.append( "VALUES (nextval('seq_tb_machine_retirement'), ?, ?)" );


			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString( 1, mac );
			stmt.setLong( 2 , Long.parseLong(machineID) );

			int result = insert();
			commit();
			return result;
		}
		finally {
			releaseConnection();

		}
	}
	

	/**
	 * 
	 * 机器mac取得
	 * @param mac 
	 * @throws SQLException 
	*/
	public HashMap[] getMachineMac(String machineID) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "SELECT mac FROM tb_ccb_machine WHERE machine_id = ?" );

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1 , Long.parseLong(machineID) );

			HashMap[] map = select();
			return map;
		}
		finally {
			releaseConnection();

		}
	}
	

	/**
	 * 
	 * 机器状态更新
	 * @param machineID 
	 * @throws SQLException 
	*/
	public int updateMachineStatus(String machineID, String status) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("update tb_ccb_machine ");
		sb.append("   set status = ? ");
		sb.append(" where machine_id = ? ");
		try {
			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString( 1, status );
			stmt.setLong( 2 , Long.parseLong(machineID) );
			
			int result = update();
			commit();
			return result;
		}
		finally {
			releaseConnection();

		}
	}
	
	/**
	 * 
	 * 升级
	 * @param mac 
	 * @param machineID
	 * @throws SQLException 
	*/
	public int insertMachineUpdating(String mac, String machineID) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "INSERT INTO tb_machine_updating(" );
			sb.append( "	updating_id, mac, machine_id)" );
			sb.append( "VALUES (nextval('seq_tb_machine_updating'), ?, ?)" );


			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString( 1, mac );
			stmt.setLong( 2 , Long.parseLong(machineID) );

			int result = insert();
			commit();
			return result;
		}
		finally {
			releaseConnection();

		}
	}
	
	/**
	 * 
	 * 停止升级
	 * @param mac 
	 * @throws SQLException 
	*/
	public int deleteMachineUpdating(String mac) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "DELETE FROM tb_machine_updating WHERE mac = ?");

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString( 1, mac );

			int result = delete();
			commit();
			return result;
		}
		finally {
			releaseConnection();

		}
	}
	
	/**
	 * 
	 * 查询升级的机器
	 * @param mac 
	 * @throws SQLException 
	*/
	public HashMap[] getUpdatingMachine(String mac) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "SELECT * FROM tb_machine_updating WHERE mac = ?");

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString( 1, mac );

			HashMap[] map = select();
			return map;
		}
		finally {
			releaseConnection();

		}
	}
	
	/**
	 * 
	 * 查询报废的机器
	 * @param mac 
	 * @throws SQLException 
	*/
	public HashMap[] getRetirementMachine(String mac) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "SELECT * FROM tb_machine_retirement WHERE mac = ?");

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString( 1, mac );

			HashMap[] map = select();
			return map;
		}
		finally {
			releaseConnection();

		}
	}


}
