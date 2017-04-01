package th.servlet.interactiveAPI;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.dao.UploadDao;
import th.servlet.BaseServlet;
import th.util.ResponseOut;
import th.util.StringUtils;

public abstract class BaseIfServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	protected final String		RETURN_INFO			= "RET=";
	protected final String		NORMAL_CODE			= "0";
	protected final String		ERROR_CODE			= "1";
	protected final String		RET_CODE_NULL		= "";
	

	protected UploadDao			dao					= new UploadDao();

	protected void returnResponse(HttpServletResponse res, String body) {
		// final String LOG_HEADER = CLASS_NAME + "::returnError | ";

		ResponseOut resOut;
		try {
			resOut = new ResponseOut(res, ResponseOut.CONTENT_TYPE_TEXT_PLAIN);
			resOut.open();
			String ret = "RET=" + body;
			resOut.write(ret);
			resOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void returnResponseJson(HttpServletResponse res, String body) {
		// final String LOG_HEADER = CLASS_NAME + "::returnError | ";

		ResponseOut resOut;
		try {
			resOut = new ResponseOut(res, ResponseOut.CONTENT_TYPE_TEXT_PLAIN_GB2312);
			resOut.open();
			resOut.write(body);
			resOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isParamValidate(HttpServletRequest request, String[] paramCheckArray) {
		if (paramCheckArray == null || paramCheckArray.length == 0) {
			return true;
		}
		for (String params : paramCheckArray) {
			if (StringUtils.isBlank(request.getParameter(params))) {
				return false;
			}
		}
		return true;
	}
}
