/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import th.com.util.Define;
import th.dao.DptDealDAO;
import th.dao.InternetBankingDao;
import th.dao.OrgDealDAO;
import th.dao.RoleDealDAO;
import th.dao.StrategyDealDAO;
import th.dao.UserDao;
import th.entity.InternetBankingBean;
import th.entity.RoleObjectManagementBean;
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
public class InternetBankingAction extends BaseAction{

	private List childNodesList = null;

	public InternetBankingAction(){

		this.childNodesList = new ArrayList();
	}

	/**
	 * 
	 * 
	 * @param str
	*/
	public boolean checkInternetBankingRecord(String str, int index, User user) {
		String[] info = str.split(",");
		if(info.length != 5 ){
			logger.debug("第"+index+"行数据格式不正确,应该使用符号'，'隔开!");
			System.out.println("第"+index+"行数据格式不正确,应该使用符号'，'隔开!");
			return false;
		}
		try {
			InternetBankingDao internetBankingDao = new InternetBankingDao();
			InternetBankingBean ibb = new InternetBankingBean();
			if(!internetBankingDao.getDetailByFactotyIdAndSnNum( info[0], info[1] )){
				ibb.setDevId( Long.parseLong( internetBankingDao.getTbCCBEbank() ) );
				ibb.setFatoryId( info[0] );
				ibb.setSnNum( info[1] );
				ibb.setDevType( info[2] );
				ibb.setRemark( info[3] );
				ibb.setDevStatus( info[4] );
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//				ibb.setOperateTime( df.format(new Date()) );
				ibb.setOperator( Long.parseLong( user.getId() ) );
				internetBankingDao.insertInternetBanking(ibb);
				return true;
			}
		} catch (Exception e) {
			return false;			
		}
		return false;
	}
}
