/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action;

import java.util.List;

import th.dao.ChannelDAO;
import th.entity.ChannelBean;
import th.entity.PageBean;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ChannelAction extends BaseAction {

	public boolean insertChannel( String channelName, String channelPath, String channelType, String operator ) {

		ChannelDAO channelDAO = new ChannelDAO();

		String status = "1";// 默认启用

		channelName = channelName.trim();
		channelPath = channelPath.trim();
		ChannelBean channelBean = new ChannelBean();
		channelBean.setChannelName( channelName );
		channelBean.setChannelPath( channelPath );
		channelBean.setChannelType( channelType );
		channelBean.setStatus( status );
		channelBean.setOperator( operator );

		int result = channelDAO.insertChannel( channelBean );

		if ( result == 0 ) {
			return false;
		}
		else {
			return true;
		}
	}

	public List<ChannelBean> getAllChannelData( String userId, String channelType ) {

		ChannelDAO channelDAO = new ChannelDAO();

		List<ChannelBean> list = channelDAO.getAllChannelData( userId, channelType );

		return list;
	}

	public List<ChannelBean> searchChannelData( String channelName, String isEnabled, String userId, String channelType ) {

		ChannelDAO channelDAO = new ChannelDAO();

		List<ChannelBean> list = channelDAO.searchChannelData( channelName, isEnabled, userId, channelType );

		return list;

	}

	public boolean deleteChannel( String channelId ) {
		ChannelDAO channelDAO = new ChannelDAO();

		boolean result = channelDAO.deleteChannel( channelId );

		return result;
	}

	public int stopChannel( String allStopChannelId ) {
		ChannelDAO channelDAO = new ChannelDAO();

		int result = channelDAO.stopChannel( allStopChannelId );

		return result;
	}

	public int openChannel( String allOpenChannelId ) {
		ChannelDAO channelDAO = new ChannelDAO();

		int result = channelDAO.openChannel( allOpenChannelId );

		return result;
	}

	public List<ChannelBean> pageChannel( String pageType, PageBean pageBean, String userId, String isEnabled,
			String channelType ) {

		ChannelDAO channelDAO = new ChannelDAO();

		List<ChannelBean> list = channelDAO.pageChannel( pageType, pageBean, userId, isEnabled, channelType );

		return list;

	}

	public ChannelBean editChannel( long editChannelId ) {

		ChannelDAO channelDAO = new ChannelDAO();

		ChannelBean channelBean = new ChannelBean();
		channelBean.setChannelId( editChannelId );

		channelDAO.editChannel( channelBean );

		return channelBean;
	}

	public int saveEditChannel( ChannelBean channelBean ) {
		ChannelDAO channelDAO = new ChannelDAO();

		int result = channelDAO.saveEditChannel( channelBean );

		return result;
	}

	public int checkChannelName( String channelName ) {
		ChannelDAO channelDAO = new ChannelDAO();

		int result = channelDAO.checkChannelName( channelName );

		return result;
	}

	public List<ChannelBean> getReportData( String channelName, String isEnabled, String channelType ) {
		ChannelDAO channelDAO = new ChannelDAO();

		List<ChannelBean> list = channelDAO.getReportData( channelName, isEnabled, channelType );

		return list;

	}

}
