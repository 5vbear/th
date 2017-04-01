/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.lottery;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import th.action.BaseAction;
import th.dao.lottery.LottoryDAO;
import th.entity.LotteryBean;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
@SuppressWarnings("rawtypes")
public class LotteryAction extends BaseAction {

	/**
	 * 获取抽奖相关信息
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public ArrayList getLotteryMessage(String mac) throws Exception,
			SQLException {

		ArrayList returnList = new ArrayList();
		LottoryDAO dao = new LottoryDAO();
		String orgId = dao.getOrgIdBymac(mac);
		HashMap[] maps = dao.getLotteryMessage(orgId);
		// 设置抽奖概率
		for (int i = 0; i < maps.length; i++) {
			LotteryBean bean = new LotteryBean();
			HashMap map = maps[i];
			String award_id = (String) map.get("AWARD_ID");
			String award_name = (String) map.get("AWARD_NAME");
			String award_num = (String) map.get("AWARD_NUM");
			String start_date = (String) map.get("START_DATE");
			String end_date = (String) map.get("END_DATE");
			String logo_url = (String) map.get("LOGO_URL");
			bean.setAward_id(award_id);
			bean.setAward_name(award_name);
			bean.setAward_num(Integer.parseInt(award_num));
			bean.setLogo_url(logo_url);
			returnList.add(bean);
		}
		return returnList;
	}

	/**
	 * 抽奖
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public String lottery(String mac) throws Exception, SQLException {
		String returnValue = "-1";
		int check = -1;
		ArrayList returnList = new ArrayList();
		LottoryDAO dao = new LottoryDAO();
		String orgId = dao.getOrgIdBymac(mac);
		HashMap[] maps = dao.getLotteryMessage(orgId);
		// 设置抽奖概率
		for (int i = 0; i < maps.length; i++) {
			LotteryBean bean = new LotteryBean();
			HashMap map = maps[i];
			String award_id = (String) map.get("AWARD_ID");
			String award_name = (String) map.get("AWARD_NAME");
			String award_num = (String) map.get("AWARD_NUM");
			String start_date = (String) map.get("START_DATE");
			String end_date = (String) map.get("END_DATE");
			String status = (String) map.get("STATUS");
			String daily_hits = (String) map.get("DAILY_HITS");
			String day = (String) map.get("DAY");
			int day_int = Integer.parseInt(day);
			if (award_num != null) {
				int num = Integer.parseInt(award_num);
				if (num != 0) {
					long Probability = Long.parseLong(daily_hits) * day_int
							/ num;
					int ranNum = (int) (Math.random() * Probability);
					check = ranNum;
					if (i == 0) {
						if (ranNum == 0) {
							dao.modifyLotteryMessage(award_id,--num);
							bean.setRanNum(2);//
							returnValue = "2," + award_id;
							break;
						}
					}
					if (i == 1) {
						if (ranNum == 0) {
							dao.modifyLotteryMessage(award_id,--num);
							bean.setRanNum(6);//
							returnValue = "6," + award_id;
							break;
						}
					}

					if (i == 2) {
						if (ranNum == 0) {
							dao.modifyLotteryMessage(award_id,--num);
							bean.setRanNum(10);//
							returnValue = "10," + award_id;
							break;
						}
					}
					if (i == 3) {
						if (ranNum == 0) {
							dao.modifyLotteryMessage(award_id,--num);
							bean.setRanNum(12);//
							returnValue = "12," + award_id;
							break;
						}
					}

					if (i == 4) {
						if (ranNum == 0) {
							dao.modifyLotteryMessage(award_id,--num);
							bean.setRanNum(16);//
							returnValue = "16," + award_id;
							break;
						}
					}

				}

			}
		}
		if (check != 0) {
			for (int j = 0; j < Integer.MAX_VALUE; j++) {
				int data = (int) (Math.random() * 16 + 1);
				int length = maps.length;
				if(length ==1){
					if (data == 0 || data == 2) {

					} else {
						returnValue = String.valueOf(data);
						break;
					}
				}else if(length ==2){
					
					if (data == 0 || data == 2 || data == 6 ) {

					} else {
						returnValue = String.valueOf(data);
						break;
					}
				}else if(length ==3){
					if (data == 0 || data == 2 || data == 6 || data == 10) {

					} else {
						returnValue = String.valueOf(data);
						break;
					}
					
				}else if(length ==4){
					if (data == 0 || data == 2 || data == 6 || data == 10
							|| data == 12) {

					} else {
						returnValue = String.valueOf(data);
						break;
					}
					
				}else if(length ==5){
					if (data == 0 || data == 2 || data == 6 || data == 10
							|| data == 12 || data == 16) {

					} else {
						returnValue = String.valueOf(data);
						break;
					}
					
				}


			}
		}
		return returnValue;
	}

	public long fromDateStringToLong(String inVal) { // 此方法计算时间毫秒
		Date date = null; // 定义时间类型
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:ss");
		try {
			date = inputFormat.parse(inVal); // 将字符型转换成日期型
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date.getTime(); // 返回毫秒数
	}
}
