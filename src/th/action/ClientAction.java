/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import th.dao.ClientDAO;
import th.entity.ClientBean;
import th.entity.ClientUseBean;
import th.entity.OrganizationBean;
import th.entity.PageBean;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ClientAction extends BaseAction {

	public List<ClientBean> getClientInfo( String userId ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientBean> list = clientDAO.getClientInfo( userId );

		return list;
	}

	public List<OrganizationBean> getOrgInfo( String userId ) {

// ClientDAO clientDAO = new ClientDAO();

// List<OrganizationBean> list = clientDAO.getOrgInfo( userId );

		return null;

	}

	public List<String> getOsList() {

		ClientDAO clientDAO = new ClientDAO();

		List<String> list = clientDAO.getOsList();

		return list;

	}

	public List<ClientBean> searchClientInfo( String orgId, String selectedOs, String softwareVersion, String cpuFreq,
			String diskSize, String ramSize, String userId, String macType, String macName ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientBean> list = clientDAO.searchClientInfo( orgId, selectedOs, softwareVersion, cpuFreq, diskSize,
				ramSize, userId,macType,macName );

		return list;
	}
	public List<ClientBean> searchClientInfoMachine( String orgId, String selectedOs, String softwareVersion, String cpuFreq,
			String diskSize, String ramSize, String userId, String macType, String macName ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientBean> list = clientDAO.searchClientInfoMachine( orgId, selectedOs, softwareVersion, cpuFreq, diskSize,
				ramSize, userId,macType,macName );

		return list;
	}
	public List<ClientBean> pageChannel( String pageType, PageBean pageBean, String orgId, String selectedOs, String softwareVersion, String cpuFreq,
			String diskSize, String ramSize, String macType,String macName ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientBean> list = clientDAO.pageChannel( pageType, pageBean, orgId, selectedOs, softwareVersion, cpuFreq, diskSize,
				ramSize,macType,macName );

		return list;

	}

	public List<ClientUseBean> getAllClientUseData( String userId ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientUseBean> list = clientDAO.getAllClientUseData( userId );

		return list;

	}

	public List<ClientUseBean> getDayReportData( String orgId, String day, String dataType, String sequenceType,
			String userId ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientUseBean> list = clientDAO.getDayReportData( orgId, day, dataType, sequenceType, userId );

		return list;

	}

	public List<ClientUseBean> getWeekReportData( String orgId, String week, String dataType, String sequenceType,
			String userId ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientUseBean> list = clientDAO.getWeekReportData( orgId, week, dataType, sequenceType, userId );

		return list;

	}

	public List<ClientUseBean> getMonthReportData( String orgId, String month, String dataType, String sequenceType,
			String userId ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientUseBean> list = clientDAO.getMonthReportData( orgId, month, dataType, sequenceType, userId );

		return list;

	}

	public List<ClientUseBean> getAnyTimeReportData( String orgId, String startTime, String endTime, String dataType,
			String sequenceType, String userId ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientUseBean> list = clientDAO.getAnyTimeReportData( orgId, startTime, endTime, dataType, sequenceType,
				userId );

		return list;

	}

	public List<ClientBean> getReportData( HttpServletRequest request ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientBean> list = clientDAO.getReportData( request );

		return list;

	}

	public List<ClientUseBean> getClientUseReportData( HttpServletRequest request ) {

		ClientDAO clientDAO = new ClientDAO();

		List<ClientUseBean> list = clientDAO.getClientUseReportData( request );

		return list;

	}
}
