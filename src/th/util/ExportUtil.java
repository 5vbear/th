/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import th.com.property.LocalProperties;
import th.com.util.Define4Report;

/**
 * Descriptions
 * 
 * @version 2013-8-27
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ExportUtil {
	/**
	 * 导出功能类
	 * 
	 * @param beansHashMap
	 *            HashMap<key,value> key：Excel模板中使用到的bean的名称; value：Excel模板中使用到的bean的名称对应的list
	 * @param templateName
	 *            模板文件名称
	 * @return
	 * @throws ParsePropertyException
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static byte[] getExportBytes( Map beansHashMap, String templateName ) throws ParsePropertyException,
			IOException, InvalidFormatException {
		byte[] bytes = null;
		String xlsFilePathName = null;
		try {
			// 取得“模板文件”和“生成Excel临时文件”的存放目录
			String templatePath = LocalProperties.getProperty( Define4Report.EXPORT_FILE_PATH_TEMPLATE );
			String tempPath = LocalProperties.getProperty( Define4Report.EXPORT_FILE_PATH_TEMP );
			if ( null == templatePath || null == tempPath ) {
				return null;
			}
			String dateMillis = DateUtil.getCurrentDate( Define4Report.DATE_FORMAT_PATTERN_MILLIS );
			String tempExcelName = dateMillis + ".xls";// 取当前时间（毫秒级）作为临时excel文件名
			String templateFilePathName = FileTools.buildFullFilePath( templatePath, templateName );
			xlsFilePathName = FileTools.buildFullFilePath( tempPath, tempExcelName );

			// 导出excel
			XLSTransformer transformer = new XLSTransformer();
			transformer.transformXLS( templateFilePathName, beansHashMap, xlsFilePathName );

			// 取得文件byte[]
			if ( xlsFilePathName != null ) {
				File file = new File( xlsFilePathName );
				if ( file.exists() && file.isFile() ) {
					bytes = FileTools.fileToByteArray( file );
				}
			}

		}
		catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
		finally {
			// 删除导出的临时文件
			if ( xlsFilePathName != null ) {
				File file = new File( xlsFilePathName );
				if ( file.exists() && file.isFile() ) {
					file.delete();
				}
			}

		}

		return bytes;

	}

	/**
	 * 导出报表Excel文件
	 * 
	 * @param response
	 * @param result
	 * @param beanName
	 * @param fileName
	 * @throws Exception
	 */
	public static void exportExcelFile( HttpServletResponse response, byte[] exportBytes, String fileName )
			throws Exception {
		if ( exportBytes != null ) {
			response.reset();
			response.setHeader( "Cache-Control", "no-cache" );
			response.setHeader( "Pragma", "no-cache" );
			response.setHeader( "Content-Type", "application/ms-excel; charset=UTF-8" );
			response.setHeader( "Content-Disposition", "attachment;filename=" + fileName
					+ Define4Report.EXPORT_EXCEL_FILE_EXTENSION );

			ServletOutputStream fos = response.getOutputStream();
			IOUtils.write( exportBytes, fos );

			fos.flush();
			fos.close();

		}

	}

}
