/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action;
import java.io.IOException;

import th.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
public class BackUpAction extends BaseAction {
	
	public String setService(HttpServletRequest req) {
		String fwTime = StringUtils.transStr(req.getParameter("fwTime"));
		String dbTime = StringUtils.transStr(req.getParameter("dbTime"));
		String timeStr = "";
		String result = "设置失败";
		if(!"".equals(fwTime)){
			timeStr = fwTime;
		} else if(!"".equals(dbTime)){
			timeStr = dbTime;
		}
		try{
			if(!"".equals(timeStr)){
				String[] timeArray = timeStr.split(" ");
				
				String date = timeArray[0].replace("-", "/");
		        String time = timeArray[1];
		        Process p;
		        String[] cmd = new String[] {"cmd /c date " + date, "cmd /c time " + time};
		        for (String s : cmd) { //分别调用各个命令设置时间
		        	p = Runtime.getRuntime().exec(s); //设置本机时间
		        }
			}
	        result = "设置成功";
		}catch(Exception e){
			logger.debug(e.getMessage());
		}
		return result;
	}
	public String setServiceName(HttpServletRequest req) {
		String fwName = StringUtils.transStr(req.getParameter("fwName"));
		String result = "设置失败";
		
		try{
			//Runtime.getRuntime().exec("cmd /c set computername="+fwName);
			Runtime.getRuntime().exec("cmd /c start c:/opt/cms/app/th/updateServerName.bat "+fwName);
	        result = "设置成功";
		}catch(Exception e){
			logger.debug(e.getMessage());
		}
		return result;
	}
}
