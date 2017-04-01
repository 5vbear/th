/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import th.action.BaseAction;
import th.dao.DeployDAO;
import th.dao.lottery.LottoryDAO;
import th.entity.LotteryBean;
import th.entity.MachineBean;
import th.util.StringUtils;

/**
 * 中奖处理类
 * 
 * @version 2013-9-11
 * @author PSET
 * @since JDK1.6
 * 
 */
public class AwardAction extends BaseAction {

	public void readFile2DB() {

	}

	@SuppressWarnings("rawtypes")
	public ArrayList<LotteryBean> getAwardInfo( String startTime, String endTime, String awardName, String userName, String phone ) throws Exception {
		ArrayList<LotteryBean> list = new ArrayList<LotteryBean>();
		
		HashMap[] resultMap = new LottoryDAO().getAwardInfo(startTime, endTime, awardName, userName, phone);
		if (null != resultMap && resultMap.length != 0){
			for (int i = 0; i < resultMap.length; i++) {
				Iterator it = resultMap[i].entrySet().iterator();
				LotteryBean bean = new LotteryBean();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					if("AWARD_NAME".equals((String) entry.getKey())){
						bean.setAward_name((String) entry.getValue());
					} else if("NAME".equals((String) entry.getKey())){
						bean.setUserName((String) entry.getValue());
					} else if("OPERATE_TIME".equals((String) entry.getKey())){
						bean.setOpertateTime((String) entry.getValue());
					} else if("PHONE".equals((String) entry.getKey())){
						bean.setPhone((String) entry.getValue());
					} else if("ADDRESS".equals((String) entry.getKey())){
						bean.setAddress((String) entry.getValue());
					} else if("ZIP_CODE".equals((String) entry.getKey())){
						bean.setZipCode((String) entry.getValue());
					}
				}
				list.add(bean);
			}
		}
		
		return list;

	}
	
}
