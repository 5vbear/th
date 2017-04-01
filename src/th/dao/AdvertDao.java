package th.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import th.entity.AdvertBean;
import th.util.DateUtil;

public class AdvertDao extends BaseDao {

	/**
	 * 媒体素材检索
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] searchMaterial( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT a.* FROM ( " );
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " '1' as ADVERT_TYPE, " );
		stringBuffer.append( " m.MEDIA_ID, " );
		stringBuffer.append( " m.MEDIA_NAME, " );
		stringBuffer.append( " g.GROUP_NAME," );
		stringBuffer.append( " m.MEDIA_TYPE," );
		stringBuffer.append( " u.NAME," );
		stringBuffer.append( " TO_CHAR(m.OPERATETIME + '1years', 'YYYY-MM-DD') as EXPIRETIME, " );
		stringBuffer.append( " TO_CHAR(m.OPERATETIME, 'YYYY-MM-DD') as OPERATETIME, " );
		stringBuffer.append( " m.STATUS, " );
		stringBuffer.append( " m.OPERATOR as USER_ID " );
		stringBuffer.append( "FROM TB_AD_MATERIAL_MEDIA m, TB_AD_MATERIAL_GROUP g, TB_CCB_USER u " );
		stringBuffer.append( "WHERE " );
		stringBuffer.append( " m.GROUP_ID = g.GROUP_ID " );
		stringBuffer.append( " AND m.OPERATOR = u.USER_ID " );
		stringBuffer.append( " UNION ALL " );
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " '2' as ADVERT_TYPE, " );
		stringBuffer.append( " m.SUBTITLES_ID as MEDIA_ID, " );
		stringBuffer.append( " m.SUBTITLES_NAME as MEDIA_NAME, " );
		stringBuffer.append( " g.GROUP_NAME," );
		stringBuffer.append( " '字幕' as MEDIA_TYPE," );
		stringBuffer.append( " u.NAME," );
		stringBuffer.append( " TO_CHAR(m.OPERATETIME + '1years', 'YYYY-MM-DD') as EXPIRETIME, " );
		stringBuffer.append( " TO_CHAR(m.OPERATETIME, 'YYYY-MM-DD') as OPERATETIME, " );
		stringBuffer.append( " m.STATUS, " );
		stringBuffer.append( " m.OPERATOR as USER_ID " );
		stringBuffer.append( "FROM TB_AD_MATERIAL_SUBTITLES m, TB_AD_MATERIAL_GROUP g, TB_CCB_USER u " );
		stringBuffer.append( "WHERE" );
		stringBuffer.append( " m.GROUP_ID = g.GROUP_ID" );
		stringBuffer.append( " AND m.OPERATOR = u.USER_ID" );
		stringBuffer.append( " ) a,  TB_CCB_USER u " );
		stringBuffer.append( "WHERE 1=1 " );
		stringBuffer.append( " AND a.USER_ID=u.USER_ID " );
		stringBuffer.append( " AND u.ORG_ID in " + bean.getSubOrgIdArr() );
		
		//Where条件
		//素材ID
		if (bean.getMaterialId() != null && !"".equals(bean.getMaterialId())) {
			stringBuffer.append(" AND a.MEDIA_ID = '");
			stringBuffer.append(bean.getMaterialId());
			stringBuffer.append("'");
		}
		//素材类型
		if (bean.getMaterialType() != null && !"".equals(bean.getMaterialType())) {
			stringBuffer.append(" AND a.ADVERT_TYPE = '");
			stringBuffer.append(bean.getMaterialType());
			stringBuffer.append("'");
		}
		//素材名称
		if (bean.getMaterialName() != null && !"".equals(bean.getMaterialName())){
			stringBuffer.append(" AND a.MEDIA_NAME like '%");
			stringBuffer.append(bean.getMaterialName());
			stringBuffer.append("%' ");
		}
		//审核状态
		if (bean.getAuditStatus() != null && !"".equals(bean.getAuditStatus())) {
			stringBuffer.append(" AND a.STATUS = '");
			stringBuffer.append(bean.getAuditStatus());
			stringBuffer.append("'");
		}
		//添加时间From
		if (bean.getMaterialAddDateFrom() != null && !"".equals(bean.getMaterialAddDateFrom())) {
			stringBuffer.append(" AND a.OPERATETIME >= '");
			stringBuffer.append(bean.getMaterialAddDateFrom());
			stringBuffer.append("'");
		}
		//添加时间To
		if (bean.getMaterialAddDateTo() != null && !"".equals(bean.getMaterialAddDateTo())) {
			stringBuffer.append(" AND a.OPERATETIME <= '");
			stringBuffer.append(bean.getMaterialAddDateTo());
			stringBuffer.append("'");
		}
		//文件格式
		if (bean.getMaterialFileType() != null && !"".equals(bean.getMaterialFileType())) {
			stringBuffer.append(" AND a.MEDIA_TYPE = '");
			stringBuffer.append(bean.getMaterialFileType());
			stringBuffer.append("'");
		}
		//排序方式
		if (bean.getSortField()!= null && !"".equals(bean.getSortField())) {
			stringBuffer.append(" ORDER BY  ");
			stringBuffer.append(bean.getSortField());
			stringBuffer.append(" ");
			stringBuffer.append(bean.getSortType());
		} else {
			stringBuffer.append(" ORDER BY a.MEDIA_ID ");
		}
		
		HashMap<String, String>[] beans = null;
		try {
			logger.info("The SQL is "+stringBuffer.toString());
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return beans;
	}
	
	/**
	 * 媒体素材检索
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] searchMaterialById( String id ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " m.MEDIA_ID, " );
		stringBuffer.append( " m.MEDIA_NAME, " );
		stringBuffer.append( " m.MEDIA_FILE_NAME," );
		stringBuffer.append( " m.MEDIA_URL," );
		stringBuffer.append( " m.DESCRIPTION," );
		stringBuffer.append( " m.LINK_URL," );
		stringBuffer.append( " m.GROUP_ID," );
		stringBuffer.append( " m.MEDIA_TYPE " );
		stringBuffer.append( "FROM TB_AD_MATERIAL_MEDIA m " );
		stringBuffer.append( "WHERE m.MEDIA_ID=?" );
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(id));
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return beans;
	}
	/**
	 * 字幕检索
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] searchSubtitlesById( String id ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " m.SUBTITLES_ID, " );
		stringBuffer.append( " m.SUBTITLES_NAME, " );
		stringBuffer.append( " m.GROUP_ID," );
		stringBuffer.append( " m.DESCRIPTION," );
		stringBuffer.append( " m.SUBTITLES_CONTENT," );
		stringBuffer.append( " m.BACKGROUND_PICTURE," );
		stringBuffer.append( " m.BACKGROUND_COLOR," );
		stringBuffer.append( " m.FONT_PROPERTIES," );
		stringBuffer.append( " m.FONT_COLOR," );
		stringBuffer.append( " m.HEIGHT," );
		stringBuffer.append( " m.WIDTH," );
		stringBuffer.append( " m.VERTICAL_ALIGN," );
		stringBuffer.append( " m.HORIZONTAL_ALIGN," );
		stringBuffer.append( " m.DELAY_TIME " );
		stringBuffer.append( "FROM TB_AD_MATERIAL_SUBTITLES m " );
		stringBuffer.append( "WHERE m.SUBTITLES_ID=?" );
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(id));
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return beans;
	}
	
	/**
	 * 取得素材组信息
	 * @return
	 */
	public HashMap<String, String>[] getGaterialGroupList(AdvertBean bean) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT GROUP_ID, GROUP_NAME FROM TB_AD_MATERIAL_GROUP WHERE OPERATOR = ? ORDER BY GROUP_ID " );
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(bean.getOperator()));
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return beans;
	}
	

	/**
	 * 素材组添加
	 * @param bean
	 * @return
	 */
	public String insertMaterialGroup( AdvertBean bean ) {
		//素材组ID
		StringBuffer seqBuffer = new StringBuffer();
		seqBuffer.append( "select nextval( 'SEQ_TB_AD_MATERIAL_GROUP') as GROUP_ID " );
		String groupId = "";
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		try {
			stmt = con.prepareStatement( seqBuffer.toString() );
			stmt.clearParameters();
			HashMap[] map = select();
			groupId = (String) map[0].get("GROUP_ID");
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} 
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "INSERT INTO TB_AD_MATERIAL_GROUP( " );
		stringBuffer.append( " GROUP_ID," );
		stringBuffer.append( " GROUP_NAME," );
		stringBuffer.append( " OPERATETIME," );
		stringBuffer.append( " OPERATOR) " );
		stringBuffer.append( "VALUES(?,?,?,?)" );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(groupId));
			stmt.setString(2, bean.getCustom_group());
			stmt.setTimestamp( 3, new Timestamp(new Date().getTime()) );
			stmt.setLong( 4, Long.parseLong(bean.getOperator()));
			
			//DB插入
			result = insert();
			commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return groupId;
	}
	
	/**
	 * 媒体素材添加
	 * @param bean
	 * @return
	 */
	public int[] insertMaterial( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "INSERT INTO TB_AD_MATERIAL_MEDIA( " );
		stringBuffer.append( " MEDIA_ID, " );
		stringBuffer.append( " MEDIA_NAME," );
		stringBuffer.append( " MEDIA_FILE_NAME," );
		stringBuffer.append( " MEDIA_URL," );
		stringBuffer.append( " DESCRIPTION, " );
		stringBuffer.append( " GROUP_ID," );
		stringBuffer.append( " MEDIA_TYPE," );
		stringBuffer.append( " STATUS," );
		stringBuffer.append( " OPERATETIME," );
		stringBuffer.append( " OPERATOR," );
		stringBuffer.append( " LINK_URL) " );
		stringBuffer.append( " VALUES(nextval( 'SEQ_TB_AD_MATERIAL' ),?,?,?,?,?,?,?,?,?,?)" );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int[] result = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			//媒体文件上传数量
			int count = bean.getMaterial_filePath().length;
			for (int i = 0; i < count; i++) {
				int index = 1;
				//stmt.setLong( 1, getSequence("SEQ_TB_AD_MATERIAL") );
				//stmt.setString( 1, new String(bean.getMaterial_name()[i].getBytes("iso-8859-1"), "UTF-8") );
				stmt.setString( index++, bean.getMaterial_name()[i] );
				stmt.setString( index++, bean.getMaterial_filePath()[i] );
				stmt.setString( index++, bean.getServiceFilePath()[i] );
				//stmt.setString( index++, new String(bean.getMaterial_describe()[i].getBytes("iso-8859-1"), "UTF-8") );
				stmt.setString( index++, bean.getMaterial_describe()[i] );
				if (bean.getMaterial_group() != null && !"".equals(bean.getMaterial_group())) {
					stmt.setLong( index++, Long.parseLong(bean.getMaterial_group()) );
				} else {
					stmt.setObject( index++, null );
				}
				//stmt.setString( index++, new String(bean.getMaterial_type()[i].getBytes("iso-8859-1"), "UTF-8") );
				stmt.setString( index++, bean.getMaterial_type()[i] );
				stmt.setString( index++, "1" );
				stmt.setTimestamp( index++, new Timestamp(new Date().getTime()) );
				stmt.setLong( index++, Long.parseLong(bean.getOperator()));
				stmt.setString( index++, bean.getMaterial_link()[i]);
				
				stmt.addBatch();
				//result = insert();
			}
			result = insertBatch();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 * 字幕添加
	 * @param bean
	 * @return
	 */
	public int insertSubtitles( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "INSERT INTO TB_AD_MATERIAL_SUBTITLES( " );
		stringBuffer.append( " SUBTITLES_ID," );
		stringBuffer.append( " SUBTITLES_NAME," );
		stringBuffer.append( " GROUP_ID," );
		stringBuffer.append( " DESCRIPTION," );
		stringBuffer.append( " SUBTITLES_CONTENT," );
		stringBuffer.append( " BACKGROUND_PICTURE," );
		stringBuffer.append( " BACKGROUND_COLOR," );
		stringBuffer.append( " FONT_COLOR," );
		stringBuffer.append( " FONT_PROPERTIES," );
		stringBuffer.append( " HEIGHT," );
		stringBuffer.append( " WIDTH," );
		stringBuffer.append( " VERTICAL_ALIGN," );
		stringBuffer.append( " HORIZONTAL_ALIGN," );
		stringBuffer.append( " DELAY_TIME," );
		stringBuffer.append( " STATUS," );
		stringBuffer.append( " SUBTITLES_FILE_URL," );
		stringBuffer.append( " OPERATETIME," );
		stringBuffer.append( " OPERATOR) " );
		stringBuffer.append( "VALUES(nextval( 'SEQ_TB_AD_MATERIAL' ),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			int index = 1;
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			//stmt.setLong( index++, getSequence("SEQ_TB_AD_MATERIAL"));
			//stmt.setString(index++, new String(bean.getSubtitles_name().getBytes("iso-8859-1"), "UTF-8"));
			stmt.setString(index++, bean.getSubtitles_name());
			if (bean.getMaterial_group() != null && !"".equals(bean.getMaterial_group())) {
				stmt.setLong( index++, Long.parseLong(bean.getMaterial_group()) );
			} else {
				stmt.setObject( index++, null );
			}
			//stmt.setString(index++, new String(bean.getSubtitles_describe().getBytes("iso-8859-1"), "UTF-8"));
			stmt.setString(index++, bean.getSubtitles_describe());
			//stmt.setString(index++, new String(bean.getRoll_word().getBytes("iso-8859-1"), "UTF-8"));
			stmt.setString(index++, bean.getRoll_word());
			if (bean.getServiceFilePath() == null || bean.getServiceFilePath().length == 0) {
				stmt.setString(index++, "");
			} else {
				stmt.setString(index++, bean.getServiceFilePath()[0]);
			}
			stmt.setString(index++, bean.getBackground_color());
			stmt.setString(index++, bean.getWord_color());
			stmt.setString(index++, bean.getWord_attribute());
			if (bean.getWord_height() != null && !"".equals(bean.getWord_height())) {
				stmt.setInt(index++, Integer.parseInt(bean.getWord_height()));
			} else {
				stmt.setObject(index++, null);
			}
			if (bean.getWord_width() != null && !"".equals(bean.getWord_width())) {
				stmt.setInt(index++, Integer.parseInt(bean.getWord_width()));
			} else {
				stmt.setObject(index++, null);
			}
			stmt.setString(index++, bean.getVertical_align());
			stmt.setString(index++, bean.getText_align());
			if (bean.getRoll_delay() != null && !"".equals(bean.getRoll_delay())) {
				stmt.setInt(index++, Integer.parseInt(bean.getRoll_delay()));
			} else {
				stmt.setObject(index++, null);
			}
			stmt.setString(index++, "1");
			stmt.setString(index++, "");
			stmt.setTimestamp( index++, new Timestamp(new Date().getTime()) );
			stmt.setLong( index++, Long.parseLong(bean.getOperator()));
			
			//DB插入
			result = insert();
			commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 媒体素材审核
	 * @param bean
	 * @return
	 */
	public int materialAudit( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_MATERIAL_MEDIA SET STATUS='2' WHERE MEDIA_ID IN (?) AND STATUS='1'" );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getMaterialId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 字幕审核
	 * @param bean
	 * @return
	 */
	public int subtitlesAudit( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_MATERIAL_SUBTITLES SET STATUS='2' WHERE SUBTITLES_ID IN (?) AND STATUS='1' " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getMaterialId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 媒体素材更新
	 * @param bean
	 * @return
	 */
	public int materialUpdate( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_MATERIAL_MEDIA SET " );
		stringBuffer.append( " MEDIA_NAME=?, " );
		//stringBuffer.append( " MEDIA_FILE_NAME=?, " );
		//stringBuffer.append( " MEDIA_URL=?, " );
		stringBuffer.append( " DESCRIPTION=?, " );
		//stringBuffer.append( " GROUP_ID=?, " );
		stringBuffer.append( " MEDIA_TYPE=?, " );
		stringBuffer.append( " LINK_URL=? " );
		stringBuffer.append( "WHERE MEDIA_ID=? " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			int index = 1;
			stmt.setString(index++, bean.getMaterial_name()[0] );
			//stmt.setString(index++, bean.getMaterial_filePath()[0] );
			//stmt.setString(index++, bean.getServiceFilePath()[0] );
			stmt.setString(index++, bean.getMaterial_describe()[0] );
			//stmt.setLong(index++, Long.parseLong(bean.getMaterial_group()) );
			stmt.setString(index++, bean.getMaterial_type()[0] );
			stmt.setString(index++, bean.getMaterial_link()[0] );
			stmt.setLong(index++, Long.parseLong(bean.getMaterialId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 * 字幕更新
	 * @param bean
	 * @return
	 */
	public int subtitlesUpdate( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_MATERIAL_SUBTITLES SET " );
		stringBuffer.append( " SUBTITLES_NAME=?," );
		//stringBuffer.append( " GROUP_ID=?," );
		stringBuffer.append( " DESCRIPTION=?," );
		stringBuffer.append( " SUBTITLES_CONTENT=?," );
		//stringBuffer.append( " BACKGROUND_PICTURE=?," );
		stringBuffer.append( " BACKGROUND_COLOR=?," );
		stringBuffer.append( " FONT_COLOR=?," );
		stringBuffer.append( " FONT_PROPERTIES=?," );
		stringBuffer.append( " HEIGHT=?," );
		stringBuffer.append( " WIDTH=?," );
		stringBuffer.append( " VERTICAL_ALIGN=?," );
		stringBuffer.append( " HORIZONTAL_ALIGN=?," );
		stringBuffer.append( " DELAY_TIME=? " );
		stringBuffer.append( "WHERE SUBTITLES_ID=? " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			int index = 1;
			stmt.setString(index++, bean.getSubtitles_name());
			//stmt.setLong(2, Long.parseLong(bean.getMaterial_group()));
			stmt.setString(index++, bean.getSubtitles_describe());
			stmt.setString(index++, bean.getRoll_word());
			stmt.setString(index++, bean.getBackground_color());
			stmt.setString(index++, bean.getWord_color());
			stmt.setString(index++, bean.getWord_attribute());
			stmt.setLong(index++, Long.parseLong(bean.getWord_height()));
			stmt.setLong(index++, Long.parseLong(bean.getWord_width()));
			stmt.setString(index++, bean.getVertical_align());
			stmt.setString(index++, bean.getText_align());
			stmt.setLong(index++, Long.parseLong(bean.getRoll_delay()));
			//stmt.setString(5, bean.getServiceFilePath()[0]);
			stmt.setLong(index++, Long.parseLong(bean.getMaterialId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 媒体素材删除
	 * @param bean
	 * @return
	 */
	public int materialDelete( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_MATERIAL_MEDIA SET STATUS='3' WHERE MEDIA_ID IN (?) " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getMaterialId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 字幕删除
	 * @param bean
	 * @return
	 */
	public int subtitlesDelete( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_MATERIAL_SUBTITLES SET STATUS='3' WHERE SUBTITLES_ID IN (?) " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getMaterialId()));
			
			//DB更新
			result = update();
			commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 布局检索
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] searchLayout( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT * FROM( " );
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " m.LAYOUT_ID, " );
		stringBuffer.append( " m.LAYOUT_NAME, " );
		stringBuffer.append( " m.DESCRIPTION, " );
		stringBuffer.append( " m.LAYOUT_WIDTH, " );
		stringBuffer.append( " m.LAYOUT_HEIGHT, " );
		stringBuffer.append( " u.NAME," );
		stringBuffer.append( " TO_CHAR(m.OPERATETIME, 'YYYY-MM-DD') as OPERATETIME, " );
		stringBuffer.append( " m.STATUS, " );
		stringBuffer.append( " (SELECT COUNT(*) FROM TB_AD_LAYOUT_DETAIL WHERE LAYOUT_ID=m.LAYOUT_ID) AS SCREEN_NUM " );
		stringBuffer.append( "FROM TB_AD_LAYOUT m, TB_CCB_USER u " );
		stringBuffer.append( "WHERE" );
		stringBuffer.append( " m.OPERATOR = u.USER_ID" );
		stringBuffer.append( " AND u.ORG_ID in " + bean.getSubOrgIdArr() );
		//stringBuffer.append( " AND m.STATUS = '2'" );
		stringBuffer.append( " ) m" );
		stringBuffer.append( " WHERE SCREEN_NUM > 0" );
		
		//Where条件
		//布局ID
		if (bean.getLayoutId() != null && !"".equals(bean.getLayoutId())){
			stringBuffer.append(" AND m.LAYOUT_ID= '");
			stringBuffer.append(bean.getLayoutId());
			stringBuffer.append("' ");
		}
		//布局名称
		if (bean.getLayoutName() != null && !"".equals(bean.getLayoutName())){
			stringBuffer.append(" AND m.LAYOUT_NAME like '%");
			stringBuffer.append(bean.getLayoutName());
			stringBuffer.append("%' ");
		}
		//审核状态
		if (bean.getAuditStatus() != null && !"".equals(bean.getAuditStatus())) {
			stringBuffer.append(" AND m.STATUS = '");
			stringBuffer.append(bean.getAuditStatus());
			stringBuffer.append("'");
		}
		//创建用户
		if (bean.getOperator() != null && !"".equals(bean.getOperator())) {
			stringBuffer.append(" AND m.name like '%");
			stringBuffer.append(bean.getOperator());
			stringBuffer.append("%' ");
		}
		//添加时间From
		if (bean.getLayoutAddDateFrom() != null && !"".equals(bean.getLayoutAddDateFrom())) {
			stringBuffer.append(" AND OPERATETIME >= '");
			stringBuffer.append(bean.getLayoutAddDateFrom());
			stringBuffer.append("'");
		}
		//添加时间To
		if (bean.getLayoutAddDateTo() != null && !"".equals(bean.getLayoutAddDateTo())) {
			stringBuffer.append(" AND OPERATETIME <= '");
			stringBuffer.append(bean.getLayoutAddDateTo());
			stringBuffer.append("'");
		}
		//排序方式
		if (bean.getSortField()!= null && !"".equals(bean.getSortField())) {
			stringBuffer.append(" ORDER BY  ");
			stringBuffer.append(bean.getSortField());
			stringBuffer.append(" ");
			stringBuffer.append(bean.getSortType());
		} else {
			stringBuffer.append(" ORDER BY LAYOUT_ID ");
		}
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return beans;
	}
	
	/**
	 * 布局元素检索
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] searchLayoutDetail( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " d.WINDOW_ID, " );
		stringBuffer.append( " d.WINDOW_NAME, " );
		stringBuffer.append( " d.START_X, " );
		stringBuffer.append( " d.START_Y, " );
		stringBuffer.append( " d.END_X, " );
		stringBuffer.append( " d.END_Y, " );
		stringBuffer.append( " d.WINDOW_NO " );
		stringBuffer.append( "FROM TB_AD_LAYOUT_DETAIL d " );
		stringBuffer.append( "WHERE" );
		stringBuffer.append( " d.LAYOUT_ID = ? " );
		stringBuffer.append( "ORDER BY WINDOW_ID" );
		
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(bean.getLayoutId()));
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return beans;
	}
	
	/**
	 * 布局添加
	 * @param bean
	 * @return
	 */
	public String insertLayout( AdvertBean bean ) {
		int result = 0;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		//布局ID检索
		StringBuffer seqBuffer = new StringBuffer();
		seqBuffer.append( "select nextval( 'SEQ_TB_AD_LAYOUT') as LAYOUT_ID " );
		String layoutId = "";
		try {
			stmt = con.prepareStatement( seqBuffer.toString() );
			stmt.clearParameters();
			HashMap[] map = select();
			layoutId = (String) map[0].get("LAYOUT_ID");
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} 
		
		//布局表插入
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "INSERT INTO TB_AD_LAYOUT( " );
		stringBuffer.append( " LAYOUT_ID," );
		stringBuffer.append( " LAYOUT_NAME," );
		stringBuffer.append( " DESCRIPTION," );
		stringBuffer.append( " STATUS," );
		stringBuffer.append( " LAYOUT_WIDTH," );
		stringBuffer.append( " LAYOUT_HEIGHT," );
		stringBuffer.append( " OPERATETIME," );
		stringBuffer.append( " OPERATOR) " );
		stringBuffer.append( "VALUES(?,?,?,?,?,?,?,?)" );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(layoutId));
			stmt.setString(2, bean.getLayoutName());
			stmt.setString(3, bean.getLayoutDescribe());
			stmt.setString(4, "1");
			stmt.setInt(5, bean.getLayoutwidth());
			stmt.setInt(6, bean.getLayoutHeight());
			stmt.setTimestamp( 7, new Timestamp(new Date().getTime()) );
			stmt.setLong( 8, Long.parseLong(bean.getOperator()));
			
			//DB插入
			result = insert();
			if (result == 0) {
				return null;
			}
			commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} 

		//布局元素表插入
		StringBuffer detailBuffer = new StringBuffer();
		detailBuffer.append( "INSERT INTO TB_AD_LAYOUT_DETAIL( " );
		detailBuffer.append( " WINDOW_ID," );
		detailBuffer.append( " LAYOUT_ID," );
		detailBuffer.append( " WINDOW_NAME," );
		detailBuffer.append( " START_X," );
		detailBuffer.append( " START_Y," );
		detailBuffer.append( " END_X," );
		detailBuffer.append( " END_Y, " );
		detailBuffer.append( " WINDOW_NO) " );
		detailBuffer.append( "VALUES(nextval( 'SEQ_TB_AD_LAYOUT' ),?,?,?,?,?,?,?)" );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( detailBuffer.toString() );
			for (int i = 0; i < bean.getScreenNum(); i++) {
				stmt.clearParameters();
				stmt.setLong(1, Long.parseLong(layoutId));
				stmt.setString(2, bean.getScreenName()[i]);
				stmt.setInt(3, bean.getScreenStartX()[i]);
				stmt.setInt(4, bean.getScreenStartY()[i]);
				stmt.setInt(5, bean.getScreenEndX()[i]);
				stmt.setInt( 6, bean.getScreenEndY()[i]);
				stmt.setInt( 7, i + 1);
				stmt.addBatch();
			}
			//DB插入
			int[] results = insertBatch();
			if (results == null) {
				return null;
			}
			commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return layoutId;
	}
	
	/**
	 * 布局审核
	 * @param bean
	 * @return
	 */
	public int layoutAudit( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_LAYOUT SET STATUS='2' WHERE LAYOUT_ID IN(?) AND STATUS='1' " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getLayoutId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 布局删除
	 * @param bean
	 * @return
	 */
	public int layoutDelete( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_LAYOUT SET STATUS='3' WHERE LAYOUT_ID IN (?) " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getLayoutId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 布局更新
	 * @param bean
	 * @return
	 */
	public int layoutUpdate( AdvertBean bean ) {
		int result = 0;
		//布局表更新
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_LAYOUT SET " );
		stringBuffer.append( " LAYOUT_NAME=?," );
		stringBuffer.append( " DESCRIPTION=?," );
		stringBuffer.append( " LAYOUT_WIDTH=?," );
		stringBuffer.append( " LAYOUT_HEIGHT=? " );
		stringBuffer.append( "WHERE LAYOUT_ID=? " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			
			stmt.setString(1, bean.getLayoutName());
			stmt.setString(2, bean.getLayoutDescribe());
			stmt.setInt(3, bean.getLayoutwidth());
			stmt.setInt(4, bean.getLayoutHeight());
			stmt.setLong( 5, Long.parseLong(bean.getLayoutId()));
			
			//DB更新
			result = update();
			if (result == 0) {
				return result;
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} 
		
		try {
			//布局元素表删除
			StringBuffer deteleteBuffer = new StringBuffer();
			deteleteBuffer.append( "DELETE FROM TB_AD_LAYOUT_DETAIL WHERE LAYOUT_ID=?" );
			stmt = con.prepareStatement( deteleteBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getLayoutId()));
			
			result = update();
			if (result == 0) {
				return result;
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} 
		
		//布局元素表再插入
		StringBuffer insertBuffer = new StringBuffer();
		insertBuffer.append( "INSERT INTO TB_AD_LAYOUT_DETAIL( " );
		insertBuffer.append( " WINDOW_ID," );
		insertBuffer.append( " LAYOUT_ID," );
		insertBuffer.append( " WINDOW_NAME," );
		insertBuffer.append( " START_X," );
		insertBuffer.append( " START_Y," );
		insertBuffer.append( " END_X," );
		insertBuffer.append( " END_Y, " );
		insertBuffer.append( " WINDOW_NO) " );
		insertBuffer.append( "VALUES(nextval( 'SEQ_TB_AD_LAYOUT' ),?,?,?,?,?,?,?)" );

		try {
			stmt = con.prepareStatement( insertBuffer.toString() );
			for (int i = 0; i < bean.getScreenNum(); i++) {
				stmt.clearParameters();
				stmt.setLong(1, Long.parseLong(bean.getLayoutId()));
				stmt.setString(2, bean.getScreenName()[i]);
				stmt.setInt(3, bean.getScreenStartX()[i]);
				stmt.setInt(4, bean.getScreenStartY()[i]);
				stmt.setInt(5, bean.getScreenEndX()[i]);
				stmt.setInt( 6, bean.getScreenEndY()[i]);
				stmt.setInt( 7, i + 1);
				stmt.addBatch();
			}
			//DB插入
			int[] results = insertBatch();
			if (results == null) {
				result = 0;
			}
			commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 节目单检索
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] searchProgramlist( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " p.BILL_ID, " );
		stringBuffer.append( " p.LAYOUT_ID, " );
		stringBuffer.append( " l.LAYOUT_NAME, " );
		stringBuffer.append( " l.DESCRIPTION as LAYOUT_DESCRIPTION, " );
		stringBuffer.append( " l.LAYOUT_WIDTH, " );
		stringBuffer.append( " l.LAYOUT_HEIGHT, " );
		stringBuffer.append( " p.BILL_NAME, " );
		stringBuffer.append( " p.DESCRIPTION, " );
		stringBuffer.append( " p.PLAYTIME, " );
		stringBuffer.append( " u.NAME," );
		stringBuffer.append( " p.OPERATETIME, " );
		stringBuffer.append( " p.STATUS, " );
		stringBuffer.append( " (SELECT COUNT(*) FROM TB_AD_LAYOUT_DETAIL WHERE LAYOUT_ID=p.LAYOUT_ID) AS SCREEN_NUM " );
		stringBuffer.append( "FROM TB_AD_PLAYBILL p, TB_AD_LAYOUT l, TB_CCB_USER u " );
		stringBuffer.append( "WHERE" );
		stringBuffer.append( " p.OPERATOR = u.USER_ID" );
		stringBuffer.append( " AND p.LAYOUT_ID = l.LAYOUT_ID" );
		stringBuffer.append( " AND (u.ORG_ID in " + bean.getSubOrgIdArr() );
		stringBuffer.append( " OR p.ORG_ID in " + bean.getSubOrgIdArr() + ")" );
		
		//Where条件
		//节目单ID
		if (bean.getProgramlistId() != null && !"".equals(bean.getProgramlistId())){
			stringBuffer.append(" AND p.BILL_ID= '");
			stringBuffer.append(bean.getProgramlistId());
			stringBuffer.append("' ");
		}
		//节目单名称
		if (bean.getProgramlistName() != null && !"".equals(bean.getProgramlistName())){
			stringBuffer.append(" AND p.BILL_NAME like '%");
			stringBuffer.append(bean.getProgramlistName());
			stringBuffer.append("%' ");
		}
		//审核状态
		if (bean.getAuditStatus() != null && !"".equals(bean.getAuditStatus())) {
			stringBuffer.append(" AND p.STATUS = '");
			stringBuffer.append(bean.getAuditStatus());
			stringBuffer.append("'");
		}
		//创建用户
		if (bean.getOperator() != null && !"".equals(bean.getOperator())) {
			stringBuffer.append(" AND u.name like '%");
			stringBuffer.append(bean.getOperator());
			stringBuffer.append("%' ");
		}
		//添加时间From
		if (bean.getProgramlistAddDateFrom() != null && !"".equals(bean.getProgramlistAddDateFrom())) {
			stringBuffer.append(" AND to_char(p.OPERATETIME,'YYYY-MM-DD') >= '");
			stringBuffer.append(bean.getProgramlistAddDateFrom());
			stringBuffer.append("'");
		}
		//添加时间To
		if (bean.getProgramlistAddDateTo() != null && !"".equals(bean.getProgramlistAddDateTo())) {
			stringBuffer.append(" AND to_char(p.OPERATETIME,'YYYY-MM-DD') <= '");
			stringBuffer.append(bean.getProgramlistAddDateTo());
			stringBuffer.append("'");
		}
		//排序方式
		if (bean.getSortField()!= null && !"".equals(bean.getSortField())) {
			stringBuffer.append(" ORDER BY  ");
			stringBuffer.append(bean.getSortField());
			stringBuffer.append(" ");
			stringBuffer.append(bean.getSortType());
		}
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return beans;
	}
	
	/**
	 * 节目单素材信息检索
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] searchProgramlistDetail( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " pd.BILL_ID, " );
		stringBuffer.append( " pd.MEDIA_ID, " );
		stringBuffer.append( " pd.MEDIA_LEVEL, " );
		stringBuffer.append( " pd.WINDOW_ID, " );
		stringBuffer.append( " pd.PLAYTIME, " );
		stringBuffer.append( " ld.WINDOW_NO, " );
		stringBuffer.append( " m.MEDIA_NAME, " );
		stringBuffer.append( " m.MEDIA_FILE_NAME, " );
		stringBuffer.append( " m.MEDIA_URL, " );
		stringBuffer.append( " m.LINK_URL, " );
		stringBuffer.append( " pd.OPERATETIME, " );
		stringBuffer.append( " pd.OPERATOR " );
		stringBuffer.append( "FROM TB_AD_PLAYBILL_DETAIL pd, TB_AD_LAYOUT_DETAIL ld, TB_AD_MATERIAL_MEDIA m " );
		stringBuffer.append( "WHERE" );
		stringBuffer.append( " pd.WINDOW_ID = ld.WINDOW_ID " );
		stringBuffer.append( " AND pd.MEDIA_ID = m.MEDIA_ID " );
		stringBuffer.append( " AND pd.BILL_ID = ? " );
		stringBuffer.append( "ORDER BY WINDOW_ID" );
		
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(bean.getProgramlistId()));
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return beans;
	}
	
	/**
	 * 节目单添加
	 * @param bean
	 * @return
	 */
	public String insertProgramlist( AdvertBean bean ) {
		String result = null;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		//节目单ID检索
		StringBuffer seqBuffer = new StringBuffer();
		seqBuffer.append( "select nextval( 'SEQ_TB_AD_PLAYBILL') as BILL_ID " );
		String programlistId = "";
		try {
			stmt = con.prepareStatement( seqBuffer.toString() );
			stmt.clearParameters();
			HashMap[] map = select();
			programlistId = (String) map[0].get("BILL_ID");
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} 
		result = programlistId;
		
		//节目单表插入
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "INSERT INTO TB_AD_PLAYBILL( " );
		stringBuffer.append( " BILL_ID," );
		stringBuffer.append( " LAYOUT_ID," );
		stringBuffer.append( " BILL_NAME," );
		stringBuffer.append( " DESCRIPTION," );
		stringBuffer.append( " STATUS," );
		//stringBuffer.append( " BILL_FILE_LOCATION," );
		stringBuffer.append( " PLAYTIME," );
		stringBuffer.append( " ORG_ID," );
		stringBuffer.append( " OPERATETIME," );
		stringBuffer.append( " OPERATOR) " );
		stringBuffer.append( "VALUES(?,?,?,?,?,?,?,?,?)" );
		
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(programlistId));
			stmt.setLong(2, Long.parseLong(bean.getLayoutId()));
			stmt.setString(3, bean.getProgramlistName());
			stmt.setString(4, bean.getProgramlistDescribe());
			stmt.setString(5, "1");
			stmt.setInt(6, bean.getProgramlistPlayTime());
			stmt.setLong(7, Long.parseLong(bean.getOrgId()));
			stmt.setTimestamp( 8, new Timestamp(new Date().getTime()) );
			stmt.setLong( 9, Long.parseLong(bean.getOperator()));
			
			//DB插入
			int cnt = insert();
			if (cnt == 0) {
				result = null;
			}
			commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} 

		//节目单-布局-素材表插入
		StringBuffer detailBuffer = new StringBuffer();
		detailBuffer.append( "INSERT INTO TB_AD_PLAYBILL_DETAIL( " );
		detailBuffer.append( " MAPPING_ID," );
		detailBuffer.append( " BILL_ID," );
		detailBuffer.append( " MEDIA_ID," );
		detailBuffer.append( " MEDIA_LEVEL," );
		detailBuffer.append( " WINDOW_ID," );
		detailBuffer.append( " PLAYTIME," );
		detailBuffer.append( " OPERATETIME," );
		detailBuffer.append( " OPERATOR) " );
		detailBuffer.append( "VALUES(nextval( 'SEQ_TB_AD_PLAYBILL_DETAIL' ),?,?,?,?,?,?,?)" );

		try {
			stmt = con.prepareStatement( detailBuffer.toString() );
			for (int i = 0; i < bean.getProgramlistMaterials().size(); i++) {
				stmt.clearParameters();
				AdvertBean itemBean = bean.getProgramlistMaterials().get(i);
				stmt.setLong(1, Long.parseLong(programlistId));
				stmt.setLong(2, Long.parseLong(itemBean.getMaterialId()));
				stmt.setInt(3, itemBean.getMaterialLevel());
				stmt.setLong(4, Long.parseLong(itemBean.getWindowId()));
				stmt.setInt(5, itemBean.getMaterialPlayTime());
				stmt.setTimestamp(6, new Timestamp(new Date().getTime()));
				if (bean.getOperator() == null || "".equals(bean.getOperator())) {
					stmt.setObject( 7, null);
				} else {
					stmt.setLong( 7, Long.parseLong(bean.getOperator()));
				}
				stmt.addBatch();
			}
			//DB插入
			int[] results = insertBatch();
			if (results == null) {
				result = null;
			}
			commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 * 节目单审核
	 * @param bean
	 * @return
	 */
	public int programlistAudit( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_PLAYBILL SET STATUS='2' WHERE BILL_ID IN(?) AND STATUS='1' " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getProgramlistId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 节目单删除
	 * @param bean
	 * @return
	 */
	public int programlistDelete( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_PLAYBILL SET STATUS='3' WHERE BILL_ID IN (?) " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getProgramlistId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 节目单更新
	 * @param bean
	 * @return
	 */
	public int programlistUpdate( AdvertBean bean ) {
		int result = 0;
		//节目单表更新
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_PLAYBILL SET " );
		stringBuffer.append( " BILL_NAME=?," );
		stringBuffer.append( " LAYOUT_ID=?," );
		stringBuffer.append( " DESCRIPTION=?," );
		stringBuffer.append( " PLAYTIME=? " );
		stringBuffer.append( "WHERE BILL_ID=? " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			
			stmt.setString(1, bean.getProgramlistName());
			stmt.setLong(2, Long.parseLong(bean.getLayoutId()));
			stmt.setString(3, bean.getProgramlistDescribe());
			stmt.setInt(4, bean.getProgramlistPlayTime());
			stmt.setLong( 5, Long.parseLong(bean.getProgramlistId()));
			
			//DB更新
			result = update();
			if (result == 0) {
				return result;
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} 
		
		try {
			//节目单-布局-素材关系表删除
			StringBuffer deteleteBuffer = new StringBuffer();
			deteleteBuffer.append( "DELETE FROM TB_AD_PLAYBILL_DETAIL WHERE BILL_ID=?" );
			stmt = con.prepareStatement( deteleteBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getProgramlistId()));
			
			result = update();
			if (result == 0) {
				return result;
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} 
		
		//节目单-布局-素材关系表再插入
		StringBuffer insertBuffer = new StringBuffer();
		insertBuffer.append( "INSERT INTO TB_AD_PLAYBILL_DETAIL( " );
		insertBuffer.append( " MAPPING_ID," );
		insertBuffer.append( " BILL_ID," );
		insertBuffer.append( " MEDIA_ID," );
		insertBuffer.append( " MEDIA_LEVEL," );
		insertBuffer.append( " WINDOW_ID," );
		insertBuffer.append( " PLAYTIME," );
		insertBuffer.append( " OPERATETIME," );
		insertBuffer.append( " OPERATOR) " );
		insertBuffer.append( "VALUES(nextval( 'SEQ_TB_AD_PLAYBILL_DETAIL' ),?,?,?,?,?,?,?)" );
		
		try {
			stmt = con.prepareStatement( insertBuffer.toString() );
			for (int i = 0; i < bean.getProgramlistMaterials().size(); i++) {
				stmt.clearParameters();
				AdvertBean itemBean = bean.getProgramlistMaterials().get(i);
				stmt.setLong(1, Long.parseLong(bean.getProgramlistId()));
				stmt.setLong(2, Long.parseLong(itemBean.getMaterialId()));
				stmt.setInt(3, itemBean.getMaterialLevel());
				stmt.setLong(4, Long.parseLong(itemBean.getWindowId()));
				stmt.setInt(5, itemBean.getMaterialPlayTime());
				stmt.setTimestamp(6, new Timestamp(new Date().getTime()));
				stmt.setLong( 7, Long.parseLong(bean.getOperator()));
				stmt.addBatch();
			}
			
			//DB插入
			int[] results = insertBatch();
			if (results == null) {
				result = 0;
			}
			commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 媒体素材选择
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] selectMaterial( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " m.MEDIA_ID, " );
		stringBuffer.append( " m.MEDIA_NAME, " );
		stringBuffer.append( " m.MEDIA_FILE_NAME, " );
		stringBuffer.append( " m.MEDIA_URL, " );
		stringBuffer.append( " g.GROUP_NAME," );
		stringBuffer.append( " m.MEDIA_TYPE," );
		stringBuffer.append( " u.NAME," );
		stringBuffer.append( " m.LINK_URL, " );
		stringBuffer.append( " TO_CHAR(m.OPERATETIME + '1years', 'YYYY-MM-DD') as EXPIRETIME, " );
		stringBuffer.append( " m.OPERATETIME, " );
		stringBuffer.append( " m.STATUS " );
		stringBuffer.append( "FROM TB_AD_MATERIAL_MEDIA m, TB_AD_MATERIAL_GROUP g, TB_CCB_USER u " );
		stringBuffer.append( "WHERE " );
		stringBuffer.append( " m.GROUP_ID = g.GROUP_ID " );
		stringBuffer.append( " AND m.OPERATOR = u.USER_ID " );
		stringBuffer.append( " AND m.STATUS = '2' " );
		stringBuffer.append( " AND u.ORG_ID in " + bean.getSubOrgIdArr() );
		
		//Where条件
		//素材ID
		if (bean.getMaterialId() != null && !"".equals(bean.getMaterialId())) {
			stringBuffer.append(" AND MEDIA_ID = '");
			stringBuffer.append(bean.getMaterialId());
			stringBuffer.append("'");
		}
		//素材类型
		if (bean.getMaterialType() != null && !"".equals(bean.getMaterialType())) {
			stringBuffer.append(" AND ADVERT_TYPE = '");
			stringBuffer.append(bean.getMaterialType());
			stringBuffer.append("'");
		}
		//素材名称
		if (bean.getMaterialName() != null && !"".equals(bean.getMaterialName())){
			stringBuffer.append(" AND MEDIA_NAME like '%");
			stringBuffer.append(bean.getMaterialName());
			stringBuffer.append("%' ");
		}
		//添加时间From
		if (bean.getMaterialAddDateFrom() != null && !"".equals(bean.getMaterialAddDateFrom())) {
			stringBuffer.append(" AND to_char(OPERATETIME,'YYYY-MM-DD') >= '");
			stringBuffer.append(bean.getMaterialAddDateFrom());
			stringBuffer.append("'");
		}
		//添加时间To
		if (bean.getMaterialAddDateTo() != null && !"".equals(bean.getMaterialAddDateTo())) {
			stringBuffer.append(" AND to_char(OPERATETIME,'YYYY-MM-DD') <= '");
			stringBuffer.append(bean.getMaterialAddDateTo());
			stringBuffer.append("'");
		}
		//文件格式
		if (bean.getMaterialFileType() != null && !"".equals(bean.getMaterialFileType())) {
			stringBuffer.append(" AND MEDIA_TYPE = '");
			stringBuffer.append(bean.getMaterialFileType());
			stringBuffer.append("'");
		}
		//排序方式
		if (bean.getSortField()!= null && !"".equals(bean.getSortField())) {
			stringBuffer.append(" ORDER BY  ");
			stringBuffer.append(bean.getSortField());
			stringBuffer.append(" ");
			stringBuffer.append(bean.getSortType());
		} else {
			stringBuffer.append(" ORDER BY MEDIA_ID ");
		}
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return beans;
	}
	
	/**
	 * 字幕选择
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] selectSubtitles( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " m.SUBTITLES_ID, " );
		stringBuffer.append( " m.SUBTITLES_NAME, " );
		stringBuffer.append( " g.GROUP_NAME," );
		stringBuffer.append( " u.NAME," );
		stringBuffer.append( " m.OPERATETIME, " );
		stringBuffer.append( " m.STATUS " );
		stringBuffer.append( "FROM TB_AD_MATERIAL_SUBTITLES m, TB_AD_MATERIAL_GROUP g, TB_CCB_USER u " );
		stringBuffer.append( "WHERE " );
		stringBuffer.append( " m.GROUP_ID = g.GROUP_ID " );
		stringBuffer.append( " AND m.OPERATOR = u.USER_ID " );
		stringBuffer.append( " AND m.STATUS = '2' " );
		stringBuffer.append( " AND u.ORG_ID in " + bean.getSubOrgIdArr() );
		
		//Where条件
		//字幕ID
		if (bean.getSubtitlesId() != null && !"".equals(bean.getSubtitlesId())) {
			stringBuffer.append(" AND SUBTITLES_ID = '");
			stringBuffer.append(bean.getSubtitlesId());
			stringBuffer.append("'");
		}
		//字幕名称
		if (bean.getSubtitles_name() != null && !"".equals(bean.getSubtitles_name())){
			stringBuffer.append(" AND SUBTITLES_NAME like '%");
			stringBuffer.append(bean.getSubtitles_name());
			stringBuffer.append("%' ");
		}
		//添加时间From
		if (bean.getMaterialAddDateFrom() != null && !"".equals(bean.getMaterialAddDateFrom())) {
			stringBuffer.append(" AND to_char(OPERATETIME,'YYYY-MM-DD') >= '");
			stringBuffer.append(bean.getMaterialAddDateFrom());
			stringBuffer.append("'");
		}
		//添加时间To
		if (bean.getMaterialAddDateTo() != null && !"".equals(bean.getMaterialAddDateTo())) {
			stringBuffer.append(" AND to_char(OPERATETIME,'YYYY-MM-DD') <= '");
			stringBuffer.append(bean.getMaterialAddDateTo());
			stringBuffer.append("'");
		}
		//排序方式
		if (bean.getSortField()!= null && !"".equals(bean.getSortField())) {
			stringBuffer.append(" ORDER BY  ");
			stringBuffer.append(bean.getSortField());
			stringBuffer.append(" ");
			stringBuffer.append(bean.getSortType());
		} else {
			stringBuffer.append(" ORDER BY SUBTITLES_ID ");
		}
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return beans;
	}
	
	/**
	 * 节目单检索
	 * @param org_id
	 * @return list
	 */
	public HashMap<String, String>[] programlistSearchDAO(AdvertBean bean){
		String FUNCTION_NAME = "programlistSearchDAO() ";
		logger.info( FUNCTION_NAME + "start" );
		
		HashMap<String,String>[] programList = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select p.* from TB_AD_PLAYBILL p,  TB_CCB_USER u ");
		sb.append(" where p.STATUS= '2' ");
		sb.append(" AND p.OPERATOR=u.USER_ID ");
		sb.append(" AND (u.ORG_ID in " + bean.getSubOrgIdArr() );
		sb.append(" OR p.ORG_ID in " + bean.getSubOrgIdArr() + ")" );
		
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		
		try{
			if( con == null){
				connection();
			}
			stmt = con.prepareStatement(sb.toString());
			
			logger.debug(sb);
			
			programList = select();			
			
		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		
		}
		
		
		return programList;
	}
	
	public HashMap<String, String>[] programBillIdSearchDAO(int billId ){
		String FUNCTION_NAME = "programlistSearchDAO() ";
		logger.info( FUNCTION_NAME + "start" );
		
		HashMap<String,String>[] programList = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select * from TB_AD_PLAYBILL ");
		sb.append(" where bill_id= ? ");
		
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "billId = " + billId);
		
		try{
			if( con == null){
				connection();
			}
			stmt = con.prepareStatement(sb.toString());
			stmt.setInt(1, billId);
			
			logger.debug(sb);
			
			programList = select();			
			
		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		
		}
		
		
		return programList;
	}
	
	/**
	 * 通过节目单ID检索出布局名称
	 * @param BILL_ID
	 * @return String
	 */
	public HashMap<String, String>[] getLayoutInfoDAO( int billId ){
		String FUNCTION_NAME = "getLayoutNameDAO() ";
		logger.info( FUNCTION_NAME + "start" );
		
		String layoutInfo = "";
		
		StringBuffer sb = new StringBuffer();
		sb.append( " select  " );
		sb.append( " la.LAYOUT_ID,  " );
		sb.append( " la.LAYOUT_NAME,  " );
		sb.append( " (SELECT COUNT(*) FROM TB_AD_LAYOUT_DETAIL WHERE LAYOUT_ID=la.LAYOUT_ID) AS SCREEN_NUM  " );
		sb.append( " from TB_AD_PLAYBILL pl ,TB_AD_LAYOUT la " );
		sb.append( " where pl.bill_id=? " );
		sb.append( " and pl.layout_id = la.layout_id ");
		
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "pl.bill_id = " + billId);
		
		HashMap<String, String>[] result = null;
		try{
			
			if( con == null){
				connection();
			}
			stmt = con.prepareStatement(sb.toString());
			stmt.setInt(1, billId);
			
			logger.debug(sb);
			result = select();
			
			
		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		
		}
		
		return result;
		
	}
	
	/**
	 * 区域授权
	 * @param BILL_ID
	 * @return String
	 */
	
	public int regionalAuthDAO(int billId ,int orgId){
		String FUNCTION_NAME = "regionalAuthDAO() ";
		logger.info( FUNCTION_NAME + "start" );
		
		int re=0;
		
		
		HashMap<String, String>[]result = programBillIdSearchDAO(billId);
		
		StringBuffer sb = new StringBuffer("");
		
		sb.append( " insert into TB_AD_PLAYBILL (" );
		sb.append("BILL_ID,");
		sb.append("LAYOUT_ID,");
		sb.append("BILL_NAME,");
		sb.append("DESCRIPTION,");
		sb.append("STATUS,");
		sb.append("PLAYTIME,");
		sb.append("OPERATETIME,");
		sb.append("OPERATOR,");		
		sb.append("ORG_ID");
		sb.append(") values(");
		sb.append("nextval('SEQ_TB_AD_PLAYBILL'),");
		sb.append(result[0].get("LAYOUT_ID") + ",");
		sb.append("'" + result[0].get("BILL_NAME") + "',");
		sb.append("'" + result[0].get("DESCRIPTION") + "',");
		sb.append("'" + result[0].get("STATUS") + "',");
		sb.append(result[0].get("PLAYTIME") + ",");
		sb.append("'" + result[0].get("OPERATETIME") + "',");
		sb.append(result[0].get("OPERATOR") + ",");
		sb.append(orgId + ")");
		
		
		
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "pl.bill_id = " + billId);
		
		try{
			if( con == null){
				connection();
			}
			
			stmt = con.prepareStatement(sb.toString());
			
			re = stmt.executeUpdate();
			commit();
			
			
		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		
		}
		return re;
	}
	
	/**
	 * 节目单发布
	 * @param BILL_ID
	 * @return String
	 */
	public int[] programSendDAO(AdvertBean bean){
		String FUNCTION_NAME = "programSendDAO() ";
		logger.info( FUNCTION_NAME + "start" );
		int[] re = null;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("insert into TB_PLAYBILL_MANAGEMENT (");
		sb.append(" MAPPING_ID, ");
		sb.append(" MACHINE_ID, ");
		sb.append(" BILL_ID, ");
		sb.append(" SCREEN_TYPE, ");
		sb.append(" SEND_TYPE, ");
		sb.append(" SEND_TIME, ");
		sb.append(" VALID_TIME, ");
		sb.append(" DOWNLOAD_CONTROL_TYPE,");
		sb.append(" BILL_TYPE, ");
		sb.append(" OPERATETIME, ");
		sb.append(" OPERATOR,");
		sb.append(" STATUS");
		sb.append(" ) values( ");
		sb.append("nextval('SEQ_TB_PLAYBILL_MANAGEMENT'),?,?,?,?,?,?,?,?,?,?,?)");
		//输出SQL
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		
		try{
			stmt = con.prepareStatement( sb.toString() );
			String[] machineId = bean.getmachineId();
			for(int i =0; i<machineId.length; i++){
				stmt.setLong(1, Long.parseLong(machineId[i]));
				stmt.setLong(2, Long.parseLong(bean.getProgramlistId()));
				stmt.setString(3, bean.getScreenType());
				stmt.setString(4, bean.getSendType());
				if (bean.getSendTime() == null || "".equals(bean.getSendTime().trim())) {
					stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
				} else {
					stmt.setTimestamp(5, new Timestamp(DateUtil.getDateTime(bean.getSendTime(), "yyyy-MM-dd hhmm")));
				}
				if (bean.getValueTime() == null || "".equals(bean.getValueTime().trim())) {
					Calendar cd = Calendar.getInstance();
					cd.add( Calendar.YEAR, 1 );// 增加一年
					stmt.setTimestamp(6, new Timestamp(cd.getTimeInMillis()));
				} else {
					stmt.setTimestamp(6, new Timestamp(DateUtil.getDateTime(bean.getValueTime(), "yyyy-MM-dd hhmm")));
				}
				stmt.setString(7, bean.getControlType());
				stmt.setString(8, bean.getTempAds());
				stmt.setTimestamp(9, new Timestamp(new Date().getTime()));
				stmt.setLong( 10, Long.parseLong(bean.getOperator()));
				stmt.setString( 11, "1");
				stmt.addBatch();
			}
			re = insertBatch();
			commit();
		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		
		}
		return re;
	}
	
	/**
	 *字幕发布
	 * @param BILL_ID
	 * @return String
	 */
	public int[] subtitlesSendDAO(AdvertBean bean){
		String FUNCTION_NAME = "subtitlesSendDAO() ";
		logger.info( FUNCTION_NAME + "start" );
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int[] re = null;
		StringBuffer sb = new StringBuffer();
		sb.append("insert into TB_SUBTITLES_MANAGEMENT (");
		sb.append(" MAPPING_ID, ");
		sb.append(" MACHINE_ID, ");
		sb.append(" SUBTITLES_ID, ");
		sb.append(" SEND_TIME, ");
		sb.append(" VALID_TIME, ");
		sb.append(" OPERATETIME, ");
		sb.append(" OPERATOR, ");
		sb.append(" STATUS");
		sb.append(" ) values( ");
		sb.append("nextval('SEQ_TB_PLAYBILL_MANAGEMENT'),?,?,?,?,?,?,?)");
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		
		try{
			stmt = con.prepareStatement( sb.toString() );
			String[] machineId = bean.getmachineId();
			for(int i =0; i<machineId.length; i++){
				stmt.clearParameters();
				stmt.setLong(1, Long.parseLong(machineId[i]));
				stmt.setLong(2, Long.parseLong(bean.getSubtitlesId()));
				if (bean.getSendTime2() == null || "".equals(bean.getSendTime2().trim())) {
					stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
				} else {
					stmt.setTimestamp(3, new Timestamp(DateUtil.getDateTime(bean.getSendTime2(), "yyyy-MM-dd hhmm")));
				}
				if (bean.getValueTime2() == null || "".equals(bean.getValueTime2().trim())) {
					Calendar cd = Calendar.getInstance();
					cd.add( Calendar.DATE, 1 );// 增加一天
					stmt.setTimestamp(4, new Timestamp(cd.getTimeInMillis()));
				} else {
					stmt.setTimestamp(4, new Timestamp(DateUtil.getDateTime(bean.getValueTime2(), "yyyy-MM-dd hhmm")));
				}
				stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
				stmt.setLong( 6, Long.parseLong(bean.getOperator()));
				stmt.setString( 7, "1");
				stmt.addBatch();
			}
			re = insertBatch();
			commit();
		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		
		}
		return re;
		
	}
	
	/**
	 * 清除端机节目单列表
	 * @param BILL_ID
	 * @return String
	 */
	public int updateProgramSend(AdvertBean bean){
		String FUNCTION_NAME = "updateProgramSend() ";
		logger.info( FUNCTION_NAME + "start" );
		int re = 0;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE TB_PLAYBILL_MANAGEMENT SET STATUS='0' WHERE MACHINE_ID IN(");
		String[] machineId = bean.getmachineId();
		for(int i = 0; i < machineId.length; i++){
			if (i > 0) {
				sb.append(",");
			} 
			sb.append(Integer.parseInt(machineId[i]));
		}
		sb.append(")");
		//输出SQL
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		
		try{
			stmt = con.prepareStatement( sb.toString() );
			re = update();
			commit();
		}catch (SQLException e){
			logger.debug(e.getMessage());
			re = 1;
		}catch ( Exception ex ) {
			logger.debug(ex.getMessage());
			re = 1;
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				logger.debug(ee.getMessage());
				re = 1;
			}
			logger.info(FUNCTION_NAME + "end");
		
		}
		return re;
	}
	
	public boolean isProgramSend(AdvertBean bean) {
		String FUNCTION_NAME = "isProgramSend() ";
		logger.info(FUNCTION_NAME + "start");

		boolean result = false;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT MACHINE_ID FROM TB_PLAYBILL_MANAGEMENT WHERE STATUS='0' AND MACHINE_ID IN(");
		String[] machineId = bean.getmachineId();
		for(int i = 0; i < machineId.length; i++){
			if (i > 0) {
				sb.append(",");
			} 
			sb.append(Integer.parseInt(machineId[i]));
		}
		sb.append(")");
		//输出SQL
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] ret = select();
			if (ret != null && ret.length > 0) {		
				result =true;
			}

		}catch (SQLException e){
			logger.debug(e.getMessage());
		}catch ( Exception ex ) {
			logger.debug(ex.getMessage());
		} finally {

			try {
				releaseConnection();
			} catch (SQLException ee) {
				logger.debug(ee.getMessage());
			}
			logger.info(FUNCTION_NAME + "end");
		}
		
		return result;
	}
	
	public boolean isSubtitlesSend(AdvertBean bean) {
		String FUNCTION_NAME = "isSubtitlesSend() ";
		logger.info(FUNCTION_NAME + "start");

		boolean result = false;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT MACHINE_ID FROM TB_SUBTITLES_MANAGEMENT WHERE STATUS='0' AND MACHINE_ID IN(");
		String[] machineId = bean.getmachineId();
		for(int i = 0; i < machineId.length; i++){
			if (i > 0) {
				sb.append(",");
			} 
			sb.append(Integer.parseInt(machineId[i]));
		}
		sb.append(")");
		//输出SQL
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] ret = select();
			if (ret != null && ret.length > 0) {		
				result =true;
			}

		}catch (SQLException e){
			logger.debug(e.getMessage());
		}catch ( Exception ex ) {
			logger.debug(ex.getMessage());
		} finally {

			try {
				releaseConnection();
			} catch (SQLException ee) {
				logger.debug(ee.getMessage());
			}
			logger.info(FUNCTION_NAME + "end");
		}
		
		return result;
	}
	
	/**
	 * 清除端机节目单列表
	 * @param BILL_ID
	 * @return String
	 */
	public int updateSubtitlesSend(AdvertBean bean){
		String FUNCTION_NAME = "updateSubtitlesSend() ";
		logger.info( FUNCTION_NAME + "start" );
		int re = 0;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE TB_SUBTITLES_MANAGEMENT SET STATUS='0' WHERE MACHINE_ID IN(");
		String[] machineId = bean.getmachineId();
		for(int i = 0; i < machineId.length; i++){
			if (i > 0) {
				sb.append(",");
			} 
			sb.append(Integer.parseInt(machineId[i]));
		}
		sb.append(")");
		//输出SQL
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		
		try{
			stmt = con.prepareStatement( sb.toString() );
			re = update();
			commit();
		}catch (SQLException e){
			logger.debug(e.getMessage());
			re = 1;
		}catch ( Exception ex ) {
			logger.debug(ex.getMessage());
			re = 1;
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				logger.debug(ee.getMessage());
				re = 1;
			}
			logger.info(FUNCTION_NAME + "end");
			
		}
		return re;
	}
	
	/*-------------------------------------------------------------------------
	/  Method名 ： getMacByMachineID()
	/------------------------------------------------------------------------*/
	public String getMacByMachineID(long machineID){
		String FUNCTION_NAME = "getMacByMachineID() ";
		logger.info( FUNCTION_NAME + "start" );
		
		String ret = "";
		HashMap[] result = null;
		
		StringBuffer sb = new StringBuffer("");
		
		sb.append( "SELECT mac  FROM tb_ccb_machine WHERE machine_id = ?" );		
		
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "pl.machineID = " + machineID);
		
		try{
			if( con == null){
				connection();
			}
			
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong( 1, machineID );
			
			result = select();
			if (result != null && result.length > 0) {		
				ret = (String)result[0].get( "MAC" );
			}
			
		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		}
		return ret;
		
	}
	
	/*-------------------------------------------------------------------------
	/  Method名 ： getBillIssuedList()
	/------------------------------------------------------------------------*/
	public HashMap<String, String>[] getBillIssuedList( long machineID ) {
		String FUNCTION_NAME = "getBillIssuedList() ";
		logger.info(FUNCTION_NAME + "start");

		HashMap<String, String>[] ret = null;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT pbm.mapping_id, pbm.machine_id, pbm.bill_id, pbm.bill_type, ");
		sb.append("pbm.screen_type, pbm.send_type, pbm.valid_time, pbm.download_control_type, ");
		sb.append("pbm.bill_type, pb.layout_id, pb.bill_name, pb.description, pb.status, tpgm.playbill_action,");
		sb.append("pb.bill_file_loaction, pb.playtime, pb.org_id ");
		sb.append("FROM tb_playbill_management pbm ");
		sb.append("JOIN TB_AD_PLAYBILL pb ON pbm.bill_id = pb.bill_id ");
		sb.append("LEFT JOIN TB_PLAYBILL_GROUP_MANAGEMENT tpgm ON pbm.bill_id = tpgm.bill_id ");
		sb.append("WHERE pb.STATUS = '2' AND pbm.machine_id = ? ");
		sb.append("AND pbm.STATUS='1' ");


		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "machineID = " + machineID);

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong( 1, machineID );

			ret = select();

		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {

			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		}
		
		return ret;
	}
	
	public int UpdateCommandManagement(long machineID, String status, String commandContent, int commandID, String sendTime){
		String FUNCTION_NAME = "UpdateCommandManagement() ";
		logger.info( FUNCTION_NAME + "start" );
		
		int ret=0;
		
		StringBuffer sb = new StringBuffer("");
		
		sb.append( "UPDATE tb_command_management " );
		sb.append( "SET status=?, command_content=?, operatetime=? " );
		sb.append( "WHERE command_id=? AND machine_id=? " );
	
		
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "pl.machineID = " + machineID);
		logger.info(FUNCTION_NAME + "pl.status = " + status);
		logger.info(FUNCTION_NAME + "pl.commandID = " + commandID);
		logger.info(FUNCTION_NAME + "pl.commandContent = " + commandContent);
		
		try{
			if( con == null){
				connection();
			}
			
			stmt = con.prepareStatement(sb.toString());
			stmt.setString( 1, status );
			stmt.setString( 2, commandContent);
			stmt.setTimestamp( 3, new Timestamp(DateUtil.getDateTime(sendTime, "yyyy-MM-dd hhmm")));
			stmt.setInt( 4, commandID );
			stmt.setLong( 5, machineID );
						
			ret = stmt.executeUpdate();
			commit();
			
			
		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		
		}
		return ret;
	}
	
	/**
	 * 布局元素检索
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] getProgramlistInfo( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " pd.PLAYTIME, " );
		stringBuffer.append( " ld.WINDOW_NAME, " );
		stringBuffer.append( " ld.START_X, " );
		stringBuffer.append( " ld.START_Y, " );
		stringBuffer.append( " ld.END_X, " );
		stringBuffer.append( " ld.END_Y, " );
		stringBuffer.append( " ld.WINDOW_NO, " );
		stringBuffer.append( " mm.MEDIA_URL, " );
		stringBuffer.append( " mm.LINK_URL " );
		stringBuffer.append( "FROM TB_AD_PLAYBILL_DETAIL pd,   " );
		//stringBuffer.append( " TB_PLAYBILL_MANAGEMENT pm,  " );
		stringBuffer.append( " TB_AD_LAYOUT_DETAIL ld,  " );
		stringBuffer.append( " TB_AD_MATERIAL_MEDIA mm " );
		stringBuffer.append( "WHERE 1=1" );
		//stringBuffer.append( " pd.BILL_ID=pm.BILL_ID " );
		stringBuffer.append( " AND pd.WINDOW_ID=ld.WINDOW_ID " );
		stringBuffer.append( " AND pd.MEDIA_ID=mm.MEDIA_ID " );
		stringBuffer.append( " AND pd.MEDIA_ID=mm.MEDIA_ID " );
		stringBuffer.append( " AND pd.BILL_ID=? " );
		stringBuffer.append( "ORDER BY WINDOW_NO" );
		
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(bean.getProgramlistId()));
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return beans;
	}
	
	public HashMap<String,String>[] getMachineInfoDAO(int operator_id){
		String FUNCTION_NAME = "getMachineInfoDAO() ";
		logger.info( FUNCTION_NAME + "start" );
		
		HashMap<String,String>[] machineList = null;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select * from TB_CCB_MACHINE ");
		sb.append(" where ORG_ID= ? ");
		
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "operator_id = " + operator_id);
		
		try{
			if( con == null){
				connection();
			}
			stmt = con.prepareStatement(sb.toString());
			stmt.setInt(1, operator_id);
			
			logger.debug(sb);
			
			machineList = select();			
			
		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		
		}
		
		return machineList;
	}
	
	/**
	 * 生成节目单文件后更新节目单文件路径字段
	 * @param bean
	 * @return
	 */
	public int updateBillFileLocation( AdvertBean bean ) {
		int result = 0;
		//节目单表更新
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_PLAYBILL SET BILL_FILE_LOACTION=? WHERE BILL_ID=?" );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			
			stmt.setString(1, bean.getBill_file_location());
			stmt.setLong(2, Long.parseLong(bean.getProgramlistId()));
			
			//DB更新
			result = update();
			if (result == 0) {
				return result;
			}
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 *获取字幕列表
	 * @param BILL_ID
	 * @return String
	 */
	public HashMap<String,String>[] getSubtitlesList(String mac){
		String FUNCTION_NAME = "getSubtitlesList() ";
		logger.info( FUNCTION_NAME + "start" );
		HashMap<String,String>[] subtitlesList = null;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append(" s.SUBTITLES_ID, ");
		sb.append(" s.SUBTITLES_NAME, ");
		sb.append(" s.DESCRIPTION, ");
		sb.append(" s.SUBTITLES_CONTENT, ");
		sb.append(" s.BACKGROUND_PICTURE, ");
		sb.append(" s.BACKGROUND_COLOR, ");
		sb.append(" s.FONT_COLOR, ");
		sb.append(" s.FONT_PROPERTIES, ");
		sb.append(" s.SUBTITLES_CONTENT, ");
		sb.append(" s.HEIGHT, ");
		sb.append(" s.WIDTH, ");
		sb.append(" s.VERTICAL_ALIGN, ");
		sb.append(" s.HORIZONTAL_ALIGN, ");
		sb.append(" s.DELAY_TIME ");
		sb.append("FROM TB_SUBTITLES_MANAGEMENT m, TB_AD_MATERIAL_SUBTITLES s, TB_CCB_MACHINE ma ");
		sb.append("WHERE m.SUBTITLES_ID=s.SUBTITLES_ID ");
		sb.append(" AND m.MACHINE_ID = ma.MACHINE_ID ");
		sb.append(" AND m.STATUS = '1' ");
		sb.append(" AND ma.MAC=? ");
		
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		
		try{
			stmt = con.prepareStatement( sb.toString() );
			stmt.clearParameters();
			stmt.setString(1, mac);
			subtitlesList = select();
		}catch (SQLException e){
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally{
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");
		
		}
		return subtitlesList;
	}
	
	/**
	 * 节目单组检索
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] searchProgramlistGroup( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " g.GROUP_ID, " );
		stringBuffer.append( " g.GROUP_NAME, " );
		stringBuffer.append( " g.DESCRIPTION, " );
		stringBuffer.append( " g.LOOP_TIME, " );
		stringBuffer.append( " u.NAME," );
		stringBuffer.append( " g.OPERATETIME, " );
		stringBuffer.append( " g.STATUS " );
		stringBuffer.append( "FROM TB_AD_PLAYBILL_GROUP g, TB_CCB_USER u " );
		stringBuffer.append( "WHERE" );
		stringBuffer.append( " g.OPERATOR = u.USER_ID" );
		stringBuffer.append( " AND u.ORG_ID in " + bean.getSubOrgIdArr() );
		
		//Where条件
		//节目单组ID
		if (bean.getProgramlistGroupId() != null && !"".equals(bean.getProgramlistGroupId())){
			stringBuffer.append(" AND g.GROUP_ID= '");
			stringBuffer.append(bean.getProgramlistGroupId());
			stringBuffer.append("' ");
		}
		//节目单组名称
		if (bean.getProgramlistGroupName() != null && !"".equals(bean.getProgramlistGroupName())){
			stringBuffer.append(" AND g.GROUP_NAME like '%");
			stringBuffer.append(bean.getProgramlistGroupName());
			stringBuffer.append("%' ");
		}
		//审核状态
		if (bean.getAuditStatus() != null && !"".equals(bean.getAuditStatus())) {
			stringBuffer.append(" AND g.STATUS = '");
			stringBuffer.append(bean.getAuditStatus());
			stringBuffer.append("'");
		}
		//创建用户
		if (bean.getOperator() != null && !"".equals(bean.getOperator())) {
			stringBuffer.append(" AND u.name like '%");
			stringBuffer.append(bean.getOperator());
			stringBuffer.append("%' ");
		}
		//添加时间From
		if (bean.getProgramlistAddDateFrom() != null && !"".equals(bean.getProgramlistAddDateFrom())) {
			stringBuffer.append(" AND to_char(g.OPERATETIME,'YYYY-MM-DD') >= '");
			stringBuffer.append(bean.getProgramlistAddDateFrom());
			stringBuffer.append("'");
		}
		//添加时间To
		if (bean.getProgramlistAddDateTo() != null && !"".equals(bean.getProgramlistAddDateTo())) {
			stringBuffer.append(" AND to_char(g.OPERATETIME,'YYYY-MM-DD') <= '");
			stringBuffer.append(bean.getProgramlistAddDateTo());
			stringBuffer.append("'");
		}
		//排序方式
		if (bean.getSortField()!= null && !"".equals(bean.getSortField())) {
			stringBuffer.append(" ORDER BY  ");
			stringBuffer.append(bean.getSortField());
			stringBuffer.append(" ");
			stringBuffer.append(bean.getSortType());
		}
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return beans;
	}
	
	/**
	 * 节目单组详细信息检索
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] searchProgramlistByGroupId( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " p.BILL_ID, " );
		stringBuffer.append( " p.BILL_NAME, " );
		stringBuffer.append( " p.DESCRIPTION, " );
		stringBuffer.append( " p.PLAYTIME, " );
		stringBuffer.append( " m.PLAYBILL_ACTION, " );
		stringBuffer.append( " p.OPERATETIME, " );
		stringBuffer.append( " p.STATUS, " );
		stringBuffer.append( " (SELECT COUNT(*) FROM TB_AD_LAYOUT_DETAIL WHERE LAYOUT_ID=p.LAYOUT_ID) AS SCREEN_NUM " );
		stringBuffer.append( "FROM TB_AD_PLAYBILL_GROUP g, TB_PLAYBILL_GROUP_MANAGEMENT m, TB_AD_PLAYBILL p " );
		stringBuffer.append( "WHERE" );
		stringBuffer.append( " g.GROUP_ID = m.GROUP_ID " );
		stringBuffer.append( " AND m.BILL_ID = p.BILL_ID " );
		stringBuffer.append( " AND g.GROUP_ID = ? " );
		stringBuffer.append( "ORDER BY g.GROUP_ID" );
		
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(bean.getProgramlistGroupId()));
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return beans;
	}
	
	/**
	 * 节目单组添加
	 * @param bean
	 * @return
	 */
	public String insertProgramlistGroup( AdvertBean bean ) {
		String result = null;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		//节目单组ID检索
		StringBuffer seqBuffer = new StringBuffer();
		seqBuffer.append( "select nextval( 'SEQ_TB_AD_PLAYBILL_GROUP') as GROUP_ID " );
		String programlistGroupId = "";
		try {
			stmt = con.prepareStatement( seqBuffer.toString() );
			stmt.clearParameters();
			HashMap[] map = select();
			programlistGroupId = (String) map[0].get("GROUP_ID");
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} 
		result = programlistGroupId;
		
		//节目单组表插入
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "INSERT INTO TB_AD_PLAYBILL_GROUP( " );
		stringBuffer.append( " GROUP_ID," );
		stringBuffer.append( " GROUP_NAME," );
		stringBuffer.append( " DESCRIPTION," );
		stringBuffer.append( " LOOP_TIME," );
		stringBuffer.append( " STATUS," );
		stringBuffer.append( " OPERATETIME," );
		stringBuffer.append( " OPERATOR) " );
		stringBuffer.append( "VALUES(?,?,?,?,?,?,?)" );
		
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			int index = 1;
			stmt.setLong(index++, Long.parseLong(programlistGroupId));
			stmt.setString(index++, bean.getProgramlistGroupName());
			stmt.setString(index++, bean.getProgramlistGroup_desc());
			stmt.setInt(index++, bean.getProgramlistPlayTime());
			stmt.setString(index++, "1");
			stmt.setTimestamp(index++, new Timestamp(new Date().getTime()) );
			stmt.setLong(index++, Long.parseLong(bean.getOperator()));
			
			//DB插入
			int cnt = insert();
			if (cnt == 0) {
				result = null;
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} 

		//节目单－节目单组映射表插入
		if (bean.getProgramlistGroup() != null) {
			StringBuffer detailBuffer = new StringBuffer();
			detailBuffer.append( "INSERT INTO TB_PLAYBILL_GROUP_MANAGEMENT( " );
			detailBuffer.append( " MAPPING_ID," );
			detailBuffer.append( " GROUP_ID," );
			detailBuffer.append( " BILL_ID," );
			detailBuffer.append( " PLAYBILL_ACTION," );
			detailBuffer.append( " OPERATETIME," );
			detailBuffer.append( " OPERATOR) " );
			detailBuffer.append( "VALUES(nextval( 'SEQ_TB_PLAYBILL_GROUP_MANAGEMENT' ),?,?,?,?,?)" );
	
			try {
				stmt = con.prepareStatement( detailBuffer.toString() );
				for (int i = 0; i < bean.getProgramlistGroup().length; i++) {
					stmt.clearParameters();
					String programlistId = bean.getProgramlistGroup()[i];
					stmt.setLong(1, Long.parseLong(programlistGroupId));
					stmt.setLong(2, Long.parseLong(programlistId));
					stmt.setString(3, bean.getProgramlistActionList()[i]);
					stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
					stmt.setLong( 5, Long.parseLong(bean.getOperator()));
					stmt.addBatch();
				}
				//DB插入
				int[] results = insertBatch();
				if (results == null) {
					result = null;
				}
				commit();
			} catch ( SQLException e ) {
				e.printStackTrace();
			} catch ( Exception ex ) {
				ex.printStackTrace();
			} finally {
				try {
					releaseConnection();
				} catch (SQLException ee) {
					ee.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 节目单组审核
	 * @param bean
	 * @return
	 */
	public int programlistGroupAudit( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_PLAYBILL_GROUP SET STATUS='2' WHERE GROUP_ID IN(?) AND STATUS='1' " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getProgramlistGroupId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 节目单组删除
	 * @param bean
	 * @return
	 */
	public int programlistGroupDelete( AdvertBean bean ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_PLAYBILL_GROUP SET STATUS='3' WHERE GROUP_ID IN (?) " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getProgramlistGroupId()));
			
			//DB更新
			result = update();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 节目单组更新
	 * @param bean
	 * @return
	 */
	public int programlistGroupUpdate( AdvertBean bean ) {
		int result = 0;
		//节目单组表更新
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "UPDATE TB_AD_PLAYBILL_GROUP SET " );
		stringBuffer.append( " GROUP_NAME=?, " );
		stringBuffer.append( " DESCRIPTION=?, " );
		stringBuffer.append( " LOOP_TIME=? " );
		stringBuffer.append( "WHERE GROUP_ID=? " );

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			
			stmt.setString(1, bean.getProgramlistGroupName());
			stmt.setString(2, bean.getProgramlistGroup_desc());
			stmt.setInt(3, bean.getProgramlistGroup_loop());
			stmt.setLong(4, Long.parseLong(bean.getProgramlistGroupId()));
			
			//DB更新
			result = update();
			if (result == 0) {
				return result;
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} 
		
		try {
			//节目单－节目单组映射表删除
			StringBuffer deteleteBuffer = new StringBuffer();
			deteleteBuffer.append( "DELETE FROM TB_PLAYBILL_GROUP_MANAGEMENT WHERE GROUP_ID=?" );
			stmt = con.prepareStatement( deteleteBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(bean.getProgramlistGroupId()));
			
			result = update();
			if (result == 0) {
				return result;
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		} 
		
		//节目单－节目单组映射表再插入
		StringBuffer insertBuffer = new StringBuffer();
		insertBuffer.append( "INSERT INTO TB_PLAYBILL_GROUP_MANAGEMENT( " );
		insertBuffer.append( " MAPPING_ID," );
		insertBuffer.append( " GROUP_ID," );
		insertBuffer.append( " BILL_ID," );
		insertBuffer.append( " PLAYBILL_ACTION," );
		insertBuffer.append( " OPERATETIME," );
		insertBuffer.append( " OPERATOR) " );
		insertBuffer.append( "VALUES(nextval( 'SEQ_TB_PLAYBILL_GROUP_MANAGEMENT' ),?,?,?,?,?)" );
		
		try {
			stmt = con.prepareStatement( insertBuffer.toString() );
			for (int i = 0; i < bean.getProgramlistGroup().length; i++) {
				stmt.clearParameters();
				String programlistId = bean.getProgramlistGroup()[i];
				stmt.setLong(1, Long.parseLong(bean.getProgramlistGroupId()));
				stmt.setLong(2, Long.parseLong(programlistId));
				stmt.setString(3, bean.getProgramlistActionList()[i]);
				stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
				stmt.setLong( 5, Long.parseLong(bean.getOperator()));
				stmt.addBatch();
			}
			//DB插入
			int[] results = insertBatch();
			if (results == null) {
				result = 0;
			}
			commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 根据组织ID和端机类型取得端机ID
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] getMachineIdByOrgIdAndType( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		//取得当前组织下级组织ID
		String subOrgIds = "";
		StringBuffer subBuffer = new StringBuffer();
		subBuffer.append( "SELECT * FROM suborg(?)" );
		try {
			stmt = con.prepareStatement( subBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(bean.getOrgId()));
			HashMap<String, String>[] map = select();
			subOrgIds = map[0].get("SUBORG");
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} 
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " m.MACHINE_ID " );
		stringBuffer.append( "FROM TB_CCB_MACHINE m, TB_MACHINE_ENVIRONMENT e " );
		stringBuffer.append( "WHERE" );
		stringBuffer.append( " m.MACHINE_ID = e.MACHINE_ID " );
		stringBuffer.append( " AND (m.ORG_ID IN( " );
		stringBuffer.append( bean.getOrgId() );
		stringBuffer.append( ")" );
		if (subOrgIds != null && !"".equals(subOrgIds)) {
			stringBuffer.append( " OR m.ORG_ID IN( " );
			stringBuffer.append( subOrgIds );
			stringBuffer.append( ")" );
		}
		stringBuffer.append( ")" );
		if (bean.getMachingType() != null && bean.getMachingType().length > 0) {
			stringBuffer.append( " AND ( " );
			for (int i = 0; i < bean.getMachingType().length; i++) {
				if (i == 0) {
					stringBuffer.append( "  e.OS like '%" );
					stringBuffer.append(bean.getMachingType()[i]);
					stringBuffer.append( "%'" );
				} else {
					stringBuffer.append( " OR e.OS like '%" );
					stringBuffer.append(bean.getMachingType()[i]);
					stringBuffer.append( "%'" );
				}
			}
			stringBuffer.append( " ) " );
		}
		stringBuffer.append( "ORDER BY m.MACHINE_ID" );
		
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return beans;
	}
	
	/**
	 * 取得端机类型
	 * @return
	 * @throws SQLException
	 */
	public HashMap<String, String>[] getAllOSTypes() throws SQLException {
		String FUNCTION_NAME = "getAllOSType() ";
		logger.info( FUNCTION_NAME + "start" );
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "SELECT dev_os as os FROM tb_ebank_device_management ORDER BY os" );
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null) {
				connection();
			}

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;
		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	
	/**
	 * 取得布局元素详细信息
	 * @param bean
	 * @return
	 */
	public HashMap<String, String>[] getLayoutDetail( AdvertBean bean ) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "SELECT  " );
		stringBuffer.append( " d.WINDOW_ID, " );
		stringBuffer.append( " d.WINDOW_NAME, " );
		stringBuffer.append( " d.LAYOUT_ID, " );
		stringBuffer.append( " d.START_X, " );
		stringBuffer.append( " d.START_Y, " );
		stringBuffer.append( " d.END_X, " );
		stringBuffer.append( " d.END_Y, " );
		stringBuffer.append( " d.WINDOW_NO " );
		stringBuffer.append( "FROM TB_AD_PLAYBILL p,   " );
		stringBuffer.append( " TB_AD_LAYOUT_DETAIL d  " );
		stringBuffer.append( "WHERE 1=1" );
		stringBuffer.append( " AND p.LAYOUT_ID=d.LAYOUT_ID " );
		stringBuffer.append( " AND p.BILL_ID=? " );
		stringBuffer.append( "ORDER BY WINDOW_NO" );
		
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(bean.getProgramlistId()));
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return beans;
	}
}
