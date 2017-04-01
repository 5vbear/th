package th.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import th.action.lottery.LotteryAction;
import th.com.util.Define;


public class AjaxLotteryServlet extends BaseServlet {
	

	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException, SQLException {
		LotteryAction action = new LotteryAction();
		String mac = req.getParameter("mac");
		try {
			// 抽奖
			String returnValue = action.lottery(mac);
			try {
				res.getWriter().write(returnValue);
				res.flushBuffer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	}
