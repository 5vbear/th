package th.ftp.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import th.com.util.ChangeFtpUsersDLSpeed;
import th.com.util.Define;
import th.ftp.dao.FtpDao;
import th.ftp.dao.FtpInfoBean;

public class FtpSettingSevlet extends HttpServlet {
	private static final long serialVersionUID = 5318330021226939470L;
	protected Logger logger = Logger.getLogger(this.getClass());

	public String doIt(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, SQLException, IOException {
		// セッション取得
		ServletContext sc = getServletContext();
		HttpSession session = req.getSession(false);
		String dealFlg = req.getParameter("dealFlg");
		

		try {
			if (session == null || session.getAttribute("user_info") == null) {
				res.setContentType("text/html; charset=utf-8");
				res.sendRedirect("/th/index.jsp");
				return null;
			} else {
				String foward = "/jsp/sysMang/ftpMang/ftpList.jsp";
				String action = req.getParameter("action");

				if (null == action || action.trim().equals("")) {
					logger.info("请求中不包含action参数，初始化ftp设定列表");
					FtpDao ftpDao = new FtpDao();
					if(null == dealFlg || "".equals(dealFlg)){
						// FTP用户列表画面
						List<FtpInfoBean> infoList = ftpDao.getAllFtpUserInfo();
						req.setAttribute("FTP_INFO", infoList);

					}else if("change".equals(dealFlg)){
						// FTP用户信息修改画面
						Long mangId = Long.parseLong((String)req.getParameter("mangId"));
						HashMap[] ret = ftpDao.getFtpUserInfoById(mangId);
						FtpInfoBean ftpInfoBean = new FtpInfoBean();
						ftpInfoBean.setFtpMangId(mangId);
						ftpInfoBean.setFtpuser((String)ret[0].get("FTP_USER"));
						ftpInfoBean.setMaxUploadSpeed((String)ret[0].get("MAX_UPLOAD_SPEED"));
						ftpInfoBean.setMaxDownloadSpeed((String)ret[0].get("MAX_DOWNLOAD_SPEED"));
						
						req.setAttribute("ftpInfoBean", ftpInfoBean);
						foward = "/jsp/sysMang/ftpMang/ftpSetting.jsp";
					}else if("save".equals(dealFlg)){
						String mangId = req.getParameter("mangId");
						String ftpUserName = (String)req.getParameter("ftpUserName");
						String uploadSpeed = (String)req.getParameter("uploadSpeed");
						String downloadSpeed = (String)req.getParameter("downloadSpeed");
						
						// 更新FTP用户的速度
						logger.info("ftp stop...");
						//Runtime.getRuntime().exec(Define.FTP_CLOSE);
						// 停止ftp服务器
						FTPServer.destroy();
						ChangeFtpUsersDLSpeed.change(Define.FTP_PATH, ftpUserName, Integer.parseInt(downloadSpeed), Integer.parseInt(uploadSpeed));
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//Runtime.getRuntime().exec(Define.FTP_START);
						FTPServer.create();
						logger.info("ftp restart finished.");
						// 更新DB中FTP用户信息
						ftpDao.updateFtpUserInfoById(Long.parseLong(mangId), uploadSpeed, downloadSpeed);
						// 返回FTP用户列表画面
						List<FtpInfoBean> infoList = ftpDao.getAllFtpUserInfo();
						req.setAttribute("FTP_INFO", infoList);
					}
					

				}
				res.addHeader("Cache-Control", "private");
				req.setCharacterEncoding("UTF-8");
				sc.getRequestDispatcher(foward).forward(req, res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		try {
			doIt(req, res);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		try {
			doIt(req, res);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
