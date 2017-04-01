package th.servlet.interactiveAPI;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.entity.interactive.MachineAccessHistoryUploadBean;

/**
 * Descriptions
 * http://localhost:8090/th/MachineAccessHistoryUploadAPI?mac=00-50-56-C0-01-18&accesstotaltimes=222&illegalvisittimes=333&statisticsdate=2013-10-10
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 */
public class MachineAccessHistoryUploadServlet extends BaseIfServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
		String FUNCTION_NAME = "doIt() ";
		logger.info(FUNCTION_NAME + " start ");
		// 实例化上传参数的实体BEAN
		MachineAccessHistoryUploadBean bean = new MachineAccessHistoryUploadBean(req);
		// 取得machineID
		int machineId = dao.getMachinIdByMac(bean.getMac());
		if (machineId == 0) {
			logger.error(FUNCTION_NAME + " Get machineId error,check mac is correct ");
			returnResponse(res, ERROR_CODE);
			logger.info(FUNCTION_NAME + " end ");
			return null;
		}
		bean.setMachineId(machineId);
		// 向表中插入数据
		dao.insertMachineAccessHistory(bean);
		returnResponse(res, NORMAL_CODE);
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
		if (!isParamValidate(request, MachineAccessHistoryUploadBean.paramCheckArray)) {
			logger.error(FUNCTION_NAME + " Some Parameters is not validate! " + new MachineAccessHistoryUploadBean(request).toString());
			returnResponse(response, ERROR_CODE);
			logger.info(FUNCTION_NAME + " end ");
			return false;
		}
		logger.info(FUNCTION_NAME + " end ");
		return true;
	}

}
