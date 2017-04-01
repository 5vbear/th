package th.action.sys.lottery;

import java.sql.SQLException;
import java.util.List;

import th.action.BaseAction;
import th.dao.LotteryDAO;
import th.entity.AwardBean;

public class LotteryAction extends BaseAction {


	LotteryDAO dao = new LotteryDAO();
	public LotteryAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 查询奖品信息
	 * @param orgID
	 * @param offset 
	 * @return
	 * @throws SQLException
	 */
	public List<AwardBean> getAwardList(String orgID, long offset, String currentOrgID) throws SQLException {
		// TODO Auto-generated method stub
		List<AwardBean> awardList = dao.getAwardList(orgID,offset,currentOrgID);
		return awardList;
	}

	/**
	 * 奖品下发
	 * @param awardList
	 * @param orgId
	 */
	public void sendAWards(List<String> awardList, String orgId) {
		// TODO Auto-generated method stub
		dao.sendAwards(awardList,orgId);
	}
	/**
	 * 奖品信息修改
	 * @param editBean
	 */
	public void editAward(AwardBean editBean) {
		// TODO Auto-generated method stub
		dao.editAward(editBean);
	}
	/**
	 * 奖品信息删除
	 * @param id
	 * @param selsctOrg 
	 */
	public void dropAward(String id, String selsctOrg) {
		// TODO Auto-generated method stub
		dao.dropAward(id,selsctOrg);
	}

	/**
	 * 奖品信息批量删除
	 * @param orgList
	 */
	public void dropAwards(List<String> awardList) {
		// TODO Auto-generated method stub
		dao.dropAwards(awardList);
	}

	public void addAwards(List<AwardBean> awards) {
		// TODO Auto-generated method stub
		dao.addAwards(awards);
	}

	/**
	 * 查询奖品数量
	 * @return
	 * @throws SQLException 
	 */
	public long getAwardCount() throws SQLException {
		// TODO Auto-generated method stub
		long count = dao.getAwardCount();
		return count;
	}

	public String getOrgNameById(String orgId) throws SQLException {
		// TODO Auto-generated method stub
		if(orgId ==null ||orgId.equals("")){
			return "";
		}
		return dao.getOrgNameById(orgId);
	}

	
}
