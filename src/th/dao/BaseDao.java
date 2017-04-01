package th.dao;

import org.apache.log4j.Logger;

import th.db.DBAccess;

public class BaseDao extends DBAccess {
	protected Logger logger = Logger.getLogger(this.getClass());
}
