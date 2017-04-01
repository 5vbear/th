/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.servlet.interactiveAPI;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.entity.interactive.BaseUploadBean;
import th.entity.interactive.UserAuthenticateBean;

/**
 * Descriptions
 *	http://localhost:8080/th/UserAuthenticateAPI?mac=00-50-56-C0-01-18&username=administrator&password=administrator
 * @version 2013年8月20日
 * @author PSET
 * @since JDK1.6
 *
 */
public class UserAuthenticateServlet extends BaseIfServlet {

	/* (non-Javadoc)
	 * @see th.servlet.BaseServlet#doIt(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String doIt( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException,
			SQLException {
		String FUNCTION_NAME = "doIt() ";
		logger.info(FUNCTION_NAME + " start ");
		
		// 实例化上传参数的实体BEAN
		UserAuthenticateBean bean = new UserAuthenticateBean(req);
		
		// 取得machineID
//		int machineId = dao.getMachinIdByMac(bean.getMac());
//		if (machineId == 0) {
//			logger.error(FUNCTION_NAME + " Get machineId error,check mac is correct ");
//			returnResponse(res, ERROR_CODE);
//			logger.info(FUNCTION_NAME + " end ");
//			return null;
//		}
//		bean.setMachineId(machineId);
		
		// 向表中插入数据
		boolean result = dao.checkUserAuthenticate(bean.getUsername(), bean.getPassword());
		
		//验证结果判定
		if(true == result){
			returnResponse(res, NORMAL_CODE);
		}else{
			returnResponse(res, ERROR_CODE);
		}
		
		logger.info(FUNCTION_NAME + " end ");
		return null;
	}
	
	/**
	 * 覆盖父类方法,用于上传的参数非空检查.
	 */
	@Override
	protected boolean isValidParams(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String FUNCTION_NAME = "isValidParams() ";
		logger.info(FUNCTION_NAME + " start ");
		if (!isParamValidate(request, BaseUploadBean.paramCheckArray)) {
			logger.error(FUNCTION_NAME + " Some Parameters is not validate! " + new BaseUploadBean(request).toString());
			returnResponse(response, ERROR_CODE);
			logger.info(FUNCTION_NAME + " end ");
			return false;
		}
		logger.info(FUNCTION_NAME + " end ");
		return true;
	}

}
