package th.servlet.backup;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import th.api.BackUpApi;
import th.com.util.Define;
import th.dao.backup.BackupManagementDao;
import th.management.backup.BackupManagement;
import th.servlet.BaseServlet;
import th.user.User;
public class BackupServlet extends BaseServlet{
	private static final long serialVersionUID = -978959465346526538L;

	private Log logger = LogFactory.getLog(BackupServlet.class.getName());
	private static String CLASS_NAME="BackupServlet.java";

	/* (non-Javadoc)
	 * @see th.servlet.BaseServlet#doIt(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String doIt( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException,
	SQLException {
		logger.info("CLASS_NAME:"+CLASS_NAME+"start...");
		// セッション取得
		ServletContext sc = getServletContext();
		HttpSession session = req.getSession(false);
		String dealFlg = req.getParameter( "deal" );
		String forward = null;

		if (session == null || session.getAttribute("user_info") == null) {
			res.setContentType("text/html; charset=utf-8");
			res.sendRedirect("/th/index.jsp");
			return null;
		}else{
			try {
				User user = (User) session.getAttribute("user_info");
				session.setAttribute("deal", session);
				String orgId = user.getOrg_id();
				String userId = user.getId();
				logger.info("DEAL="+dealFlg);
				//信息查看
				if("backup_top".equals(dealFlg)){
					if(user.hasRight(orgId, "123")){
						BackupManagementDao dao = new BackupManagementDao();
						HashMap map = dao.getInfo();
						if(null!= map&&map.size() >0){
							req.setAttribute("backup_info", map);
						}
						forward = "/jsp/backup/backup.jsp";
					}else{
						res.setContentType("text/html; charset=utf-8");
						res.sendRedirect("/th/jsp/common/noaction.jsp");
						return null;
					}
				}else if("backup_update".equals(dealFlg)){
					String internal = req.getParameter("renew_internal");
					String description = req.getParameter("renew_description");
					String clear_internal = req.getParameter("renew_clear_internal");
					String clear_description = req.getParameter("renew_clear_description");
					
					HashMap reqMap = new HashMap();
					reqMap.put("userId", userId);
					reqMap.put("dealFlg", dealFlg);
					reqMap.put("internal", internal);
					reqMap.put("description", description);
					reqMap.put("clear_internal", clear_internal);
					reqMap.put("clear_description", clear_description);
					
					// API送受信クラス生成
		            BackUpApi api = new BackUpApi(Define.BATCH_SERVER_NAME,Integer.parseInt(Define.BATCH_SERVER_PORT),Define.BACKUP_URL);
		            // API送信
		            api.communicate(reqMap);
					
					BackupManagementDao dao = new BackupManagementDao();
					dao.addBackupPlan(Long.parseLong(userId), internal, "1",description,clear_internal,clear_description);
					HashMap map =getinfo(dao);
					if(null!= map&&map.size() >0){
						req.setAttribute("backup_info", map);
					}
					forward = "/jsp/backup/backup.jsp";
				}else if("backup_clear".equals(dealFlg)){
					
					HashMap reqMap = new HashMap();
					reqMap.put("dealFlg", dealFlg);
					
					// API送受信クラス生成
					BackUpApi api = new BackUpApi(Define.BATCH_SERVER_NAME,Integer.parseInt(Define.BATCH_SERVER_PORT),Define.BACKUP_URL);
		            // API送信
		            api.communicate(reqMap);
					
					BackupManagementDao dao = new BackupManagementDao();
					HashMap map =getinfo(dao);
					if(null!= map&&map.size() >0){
						req.setAttribute("backup_info", map);
					}
					forward = "/jsp/backup/backup.jsp";
				}
				// 页面跳转
				res.addHeader( "Cache-Control", "private" );
				req.setCharacterEncoding( "UTF-8" );
				logger.info("FORWARD="+forward);
				sc.getRequestDispatcher( forward ).forward( req, res );
				

			}
			catch ( SQLException e ) {

				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		logger.info("CLASS_NAME:"+CLASS_NAME+"end...");
		return null;
	}

	private HashMap getinfo(BackupManagementDao dao) throws SQLException {
		// TODO Auto-generated method stub
		HashMap map = dao.getInfo();
		return map;
	}


}
