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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.BaseAction;
import th.com.util.Define4Machine;
import th.dao.lottery.LottoryDAO;
import th.dao.machine.RepairDAO;
import th.entity.LotteryBean;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
public class LotteryHistoryAction extends BaseAction {
	
	/**
	 * 
	 * 
	 * @return
	*/
	public int insert(HttpServletRequest req) {
		int ret =0;
		try {
			LottoryDAO dao = new LottoryDAO();
			ret = dao.insertLotteryHistory(req);
			return ret;
		} catch (SQLException e) {
			return ret;
		}	
		
	}
	
	
}
