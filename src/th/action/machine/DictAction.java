package th.action.machine;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.com.util.Define4Machine;
import th.dao.machine.DictDAO;
import th.util.StringUtils;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-9-10
 * @author PSET
 * @since JDK1.6
 */
public class DictAction extends MachineAction {
	
	private DictDAO dao = new DictDAO();
	
	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public DictAction(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see th.action.machine.MachineAction#doIt()
	 */
	@Override
	public String doIt() {
		String result;
		if("query".equals(method) || StringUtils.isBlank(method)){
			result = query();
		}else if("goAdd".equals(method)){
			result = goAdd();
		}else if("addData".equals(method)){
			result = addData();
		}else if("delete".equals(method)){
			result = delete();
		}else{
			result = Define4Machine.JSP_MACHINE_INVALID;
		}
		return result;
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String query() {
		try {
			String pageIdxs = req.getParameter("pageIdx");
			HashMap[] dictlist = dao.getDictList(req);
			int pageIdx = 1;
			if(StringUtils.isNotBlank(pageIdxs)){
				try {
					pageIdx = Integer.parseInt(pageIdxs);
				} catch (Exception e) {
					pageIdx = 1;
				}
			}
			req.setAttribute("dictlist", dictlist);
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("pageNum", (dictlist.length-1)/10+1);
			return Define4Machine.JSP_MACHINE_DICT_LIST;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String goAdd() {
		try {	
			req.setAttribute("pageIdx", req.getParameter("pageIdx"));
			req.setAttribute("macType", req.getParameter("macType"));
			return Define4Machine.JSP_MACHINE_DICT_ADD;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}	
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String addData() {
		try {	
			String pageIdx = req.getParameter("pageIdx");
			String macType = req.getParameter("mType");
			req.setAttribute("pageIdx", pageIdx);
			int result = dao.addData(req);
			return Define4Machine.SERVLET_MACHINE_DICT_LIST + pageIdx + "&macType=" + macType;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String delete() {
		try {	
			String pageIdx = req.getParameter("pageIdx");
			String macType = req.getParameter("macType");
			String operIds = req.getParameter("oid");
			req.setAttribute("pageIdx", pageIdx);
			int result = dao.delete(operIds);
			System.out.println("成功删除"+result+"条数据!");
			return Define4Machine.SERVLET_MACHINE_DICT_LIST + pageIdx + "&macType=" + macType;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}	
	}

}
