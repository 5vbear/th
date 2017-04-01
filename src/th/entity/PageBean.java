/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2013-8-16
 * @author PSET
 * @since JDK1.6
 *
 */
/**
 * 分页信息Bean
 * 
 * @author Administrator
 * 
 */
public class PageBean {
	public int pageCount = 0; // 总页数
	public int pageSize = 10; // 每页的条数
	public int infoCount = 0; // 记录总数
	public int currentPage = 0;

	/**
	 * 得到首页
	 * 
	 * @return
	 */
	public int getTopPage() {
		return 1;
	}

	/**
	 * 得到上一页
	 * 
	 * @return
	 */
	public int getPreviousPageNo() {
		if ( currentPage <= 1 )
			return 1;
		else {
			return ( currentPage - 1 );
		}
	}

	/**
	 * 得到下一页
	 * 
	 * @return
	 */
	public int getNextPageNo() {
		if ( currentPage >= this.getPageCount() ) {
			return getPageCount() == 0 ? 1 : getPageCount();
		}
		else {
			return currentPage + 1;
		}
	}

	/**
	 * 得到尾页
	 * 
	 * @return
	 */
	public int getBottomPageNo() {
		return getPageCount() == 0 ? 1 : getPageCount();
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 *            the currentPage to set
	 */
	public void setCurrentPage( int currentPage ) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount
	 *            the pageCount to set
	 */
	public void setPageCount( int pageCount ) {
		this.pageCount = pageCount;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize( int pageSize ) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the infoCount
	 */
	public int getInfoCount() {
		return infoCount;
	}

	/**
	 * @param infoCount
	 *            the infoCount to set
	 */
	public void setInfoCount( int infoCount ) {
		this.infoCount = infoCount;
	}

	/***
	 * 获得总页数
	 */
	public int getPageCount( int count ) {
		try {
			if ( count % pageSize == 0 ) {
				pageCount = count / pageSize;
			}
			else if ( count % pageSize != 0 ) {
				pageCount = count / pageSize + 1;
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		return pageCount;
	}
}
