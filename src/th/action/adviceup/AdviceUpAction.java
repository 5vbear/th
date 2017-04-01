package th.action.adviceup;

import org.apache.log4j.Logger;

import th.dao.log.AdviceDao;
import th.entity.log.AdviceBean;

public class AdviceUpAction {
	protected Logger logger = Logger.getLogger(this.getClass());

	public void saveAdvice(AdviceBean aBean) {
		AdviceDao aDao = new AdviceDao();
		aDao.saveAdvice(aBean);
	}
}
