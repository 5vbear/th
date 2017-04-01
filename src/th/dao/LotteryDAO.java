package th.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import th.action.OrgDealAction;
import th.dao.AvailablePage.AvailablePageDAO;
import th.entity.AwardBean;

public class LotteryDAO extends BaseDao {

	public List<AwardBean> getAwardList(String orgID, long offset,String currentOrgID) throws SQLException {
		
		ResultSet rel = null;
		List<AwardBean> beanList = null;
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT");
		sql.append(" aaa.item,");
		sql.append(" aaa.award_id,");
		sql.append(" aaa .award_name,");
		sql.append(" aaa .award_num,");
		sql.append(" aaa .start_date,");
		sql.append(" aaa .end_date,");
		sql.append(" aaa .logo_url,");
		sql.append(" aaa .daily_hits,");
		sql.append(" aaa .operatetime");
		sql.append(" FROM");
		sql.append(" (SELECT");
		sql.append(" CASE WHEN(SELECT");
		sql.append(" M .award_id");
		sql.append(" FROM");
		sql.append(" tb_lottery_management M");
		sql.append(" WHERE");
		sql.append(" M .org_id = ?");
		sql.append(" AND M .award_id = A .award_id");
		sql.append(" )IS NULL THEN");
		sql.append(" '0'");
		sql.append(" ELSE");
		sql.append(" '1'");
		sql.append(" END AS item,");
		sql.append(" A .award_id,");
		sql.append(" A .award_name,");
		sql.append(" A .award_num,");
		sql.append(" A .start_date,");
		sql.append(" A .end_date,");
		sql.append(" A .logo_url,");
		sql.append(" A .daily_hits,");
		sql.append(" A .operatetime");
		sql.append(" FROM");
		sql.append(" tb_lottery_award A");
		sql.append(" WHERE");
		sql.append(" A .status = '1'");
		sql.append(" AND");
		sql.append(" a.end_date>now()");
		sql.append(" and a.org_id in ("+new AvailablePageDAO().getSuperOrgIdByOrgId(orgID)+") ");
		sql.append(" )aaa");
		sql.append(" ORDER BY");
		sql.append(" aaa.item DESC,");
		sql.append(" aaa.award_id OFFSET ? LIMIT 10");

		try {
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1, Long.parseLong(orgID));
			stmt.setLong(2, offset);
			rel = stmt.executeQuery();
			beanList = this.getBeanList(rel);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			releaseConnection();
		}
		return beanList;
	}

	private List<AwardBean> getBeanList(ResultSet rel) {
		// TODO Auto-generated method stub
		List <AwardBean> list = new ArrayList<AwardBean>();
		try {
			while(rel.next()){
				AwardBean award = new AwardBean();
				award.setItem(rel.getString("item"));
				award.setId(rel.getLong("award_id"));
				award.setAwardName(rel.getString("award_name"));
				award.setAwardNum(rel.getInt("award_num"));
				award.setStartDate(rel.getDate("start_date"));
				award.setEndDate(rel.getDate("end_date"));
				award.setDailyHits(rel.getInt("daily_hits"));
				award.setCreateTime(rel.getDate("operatetime"));
				list.add(award);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return list;
		}
		return list;
	}

	/**
	 * 奖品下发
	 * @param awardList
	 * @param orgId
	 */
	public void sendAwards(List<String> awardList, String orgId) {
		// TODO Auto-generated method stub
		//删除该组织的抽奖计划
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int[] result = null;
		StringBuffer stringBuffer = new StringBuffer();
		try {

			stringBuffer.append( " INSERT INTO tb_lottery_management(" );
			stringBuffer.append( " id,");
			stringBuffer.append( " award_id,");
			stringBuffer.append( " org_id,");
			stringBuffer.append( " status,");
			stringBuffer.append( " operatetime,");
			stringBuffer.append( " operator");
			stringBuffer.append( " )values(");
			stringBuffer.append( " nextval('SEQ_TB_LOTTERY_MANAGEMENT'),");
			stringBuffer.append( " ?,?,'1',now(),?)");
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			//媒体文件上传数量
			for (int i = 0; i <awardList.size() ; i++) {
				stmt.setLong(1, Long.parseLong(awardList.get(i)));
				stmt.setLong(2, Long.parseLong(orgId));
				stmt.setLong(3, 0);
				stmt.addBatch();
			}
			result = insertBatch();
			commit();
			con.commit();
		}
		catch ( SQLException e ) {
			try {
				con.rollback();
			}
			catch ( SQLException e1 ) {
			}
		}
		finally {
			try {
				releaseConnection();
			} catch (Exception ee ) {
				ee.printStackTrace();
			}
		}
		
	
	}

	/**
	 * 奖品信息变更
	 * @param editBean
	 */
	public void editAward(AwardBean bean) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( " update  tb_lottery_award" );
		stringBuffer.append( " set ");
		stringBuffer.append( " award_num = ? ,");
		stringBuffer.append( " start_date = ? ,");
		stringBuffer.append( " end_date = ? ,");
		stringBuffer.append( " daily_hits= ? ");
		stringBuffer.append( " where award_id = ?");
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setInt(1, bean.getAwardNum());
			stmt.setDate(2, new java.sql.Date(bean.getStartDate().getTime()));
			stmt.setDate(3, new java.sql.Date(bean.getEndDate().getTime()));
			stmt.setInt(4, bean.getDailyHits());
			stmt.setLong(5, bean.getId());
			stmt.executeUpdate();
			con.commit();
		}
		catch ( SQLException e ) {
			try {
				con.rollback();
			}
			catch ( SQLException e1 ) {
			}
		}
		finally {
			try {
				releaseConnection();
			} catch (Exception ee ) {
				ee.printStackTrace();
			}
		}
	
	}

	/**
	 * 删除奖品信息
	 * @param id
	 * @param selsctOrg 
	 */
	public void dropAward(String id, String selsctOrg) {
		// TODO Auto-generated method stub
		ResultSet rel = null;
		boolean deleteFlg = true;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append( "select award_id from tb_lottery_management where org_id = "+Long.parseLong(selsctOrg)+" and award_id = "+Long.parseLong(id) );
			String searchSql = stringBuffer.toString();
			stmt = con.prepareStatement( searchSql );
			rel = stmt.executeQuery();
			while(rel.next()){
				deleteFlg = false;
				StringBuffer sql_1 = new StringBuffer();
				sql_1.append(" delete from tb_lottery_management m");
				sql_1.append(" where m.org_id = "+Long.parseLong(selsctOrg));
				sql_1.append(" and m.award_id = "+Long.parseLong(id));
				stmt = con.prepareStatement( sql_1.toString() );
				stmt.executeUpdate();
				con.commit();
			}
			if(deleteFlg){
				stringBuffer = new StringBuffer();
				stringBuffer.append( "update TB_LOTTERY_AWARD set status = '0' WHERE AWARD_ID ="+id );
				stmt = con.prepareStatement( stringBuffer.toString() );
				int result = stmt.executeUpdate();
				con.commit();
			}
		}
		catch ( SQLException e ) {
			try {
				con.rollback();
			}
			catch ( SQLException e1 ) {
			}
		}
		finally {
			try {
				releaseConnection();
			} catch (Exception ee ) {
				ee.printStackTrace();
			}
		}
	}

	/**
	 * 批量删除
	 * @param orgList
	 */
	public void dropAwards(List<String> awardList) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "update  TB_LOTTERY_AWARD set status = '0' WHERE AWARD_ID =?" );
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int[] result = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			//媒体文件上传数量
			for (int i = 0; i <awardList.size() ; i++) {
				String bean =awardList.get(i);
				stmt.setLong( 1,Long.parseLong(bean));
				stmt.addBatch();
			}
			result = insertBatch();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (Exception ee ) {
				ee.printStackTrace();
			}
		}
	}

	/**
	 * 批量添加奖品
	 * @param awards
	 */
	public void addAwards(List<AwardBean> awards) {
		// TODO Auto-generated method stub
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( " INSERT INTO tb_lottery_award(" );
		stringBuffer.append( " award_id,");
		stringBuffer.append( " award_name,");
		stringBuffer.append( " award_num,");
		stringBuffer.append( " start_date,");
		stringBuffer.append( " end_date,");
		stringBuffer.append( " logo_url,");
		stringBuffer.append( " status,");
		stringBuffer.append( " daily_hits,");
		stringBuffer.append( " operatetime,");
		stringBuffer.append( " OPERATOR,");
		stringBuffer.append( " ORG_ID");
		stringBuffer.append( " ) VALUES (");
		stringBuffer.append( " nextval('SEQ_TB_LOTTERY_AWARD'),?,?,?,?, '',");
		stringBuffer.append( " '1' ,?, now(),?,?");
		stringBuffer.append( " )");
		long orgId = 0;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int[] result = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			//媒体文件上传数量
			for (int i = 0; i <awards.size() ; i++) {
				AwardBean bean = (AwardBean)awards.get(i);
				stmt.setString( 1,bean.getAwardName());
				stmt.setInt(2, bean.getAwardNum());
				stmt.setDate(3, new java.sql.Date(bean.getStartDate().getTime()));
				stmt.setDate(4, new java.sql.Date(bean.getEndDate().getTime()));
				stmt.setInt(5, bean.getDailyHits());
				stmt.setLong(6, 0);
				stmt.setLong(7, Long.parseLong(bean.getCurrentOrgID()));
				stmt.addBatch();
			}
			result = insertBatch();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (Exception ee ) {
				ee.printStackTrace();
			}
		}
	}

	/**
	 * 查询奖品数量
	 * @throws SQLException 
	 */
	public long getAwardCount() throws SQLException {
		// TODO Auto-generated method stub

		
		ResultSet rel = null;
		long count = 0;
		List<AwardBean> beanList = null;
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(award_id) as number");
		sql.append(" FROM");
		sql.append(" tb_lottery_award");
		sql.append(" WHERE");
		sql.append(" status = '1'");
		sql.append(" AND");
		sql.append(" end_date>now()");

		try {
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sql.toString());
			rel = stmt.executeQuery();
			while(rel.next()){
				count = rel.getLong("number");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			releaseConnection();
		}
		return count;
	}

	public String getOrgNameById(String orgId) throws SQLException {
		// TODO Auto-generated method stub

		
		ResultSet rel = null;
		String name = null;
		List<AwardBean> beanList = null;
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT tb_ccb_organization.org_name as name");
		sql.append(" FROM tb_ccb_organization");
		sql.append(" WHERE");
		sql.append(" org_id ="+Long.parseLong(orgId));

		try {
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sql.toString());
			rel = stmt.executeQuery();
			while(rel.next()){
				name = rel.getString("name");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			releaseConnection();
		}
		return name;
	
	}
	

}
