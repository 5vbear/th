package th.action.machine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.com.util.Define4Machine;
import th.util.StringUtils;


/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class InvalidAction extends MachineAction {

	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public InvalidAction(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see th.action.machine.MachineAction#doIt()
	 */
	@Override
	public String doIt() {
		return Define4Machine.JSP_MACHINE_INVALID;
		
	}

}
