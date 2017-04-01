package th.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Descriptions
 * 
 * @version 2012-11-20
 * @author PSET
 * @since JDK1.6
 * 
 */
public class TransferFileUtil {

	/**
	 * transfer file from [oldPath] to [newPath]
	 * 
	 * @param oldPath
	 * @param newPath
	 * @throws Exception
	 * @throws IOException
	 */
	public static void transferFile( String oldPath, String newPath ) throws Exception, IOException {
		int byteread = 0;
		File oldFile = new File( oldPath );
		FileInputStream fin = null;
		FileOutputStream fout = null;

		if ( oldFile.exists() ) {
			fin = new FileInputStream( oldFile );
			fout = new FileOutputStream( newPath );
			byte[] buffer = new byte[fin.available()];
			while ( ( byteread = fin.read( buffer ) ) != -1 ) {
				fout.write( buffer, 0, byteread );
			}
		}
		else {
			throw new Exception( "Old file is not exist." );
		}

		if ( fin != null ) {
			fin.close();
		}
		if ( fout != null ) {
			fout.close();
		}
	}

	/**
	 * delete file util
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void delFile( File file ) throws Exception {
		if ( !file.exists() ) {
			throw new Exception( "file:" + file.getName() + "is not exist! Pls check again." );
		}
		if ( file.isFile() ) {
			if ( file.canWrite() ) {
				if ( !file.delete() ) {
					throw new Exception( "File delete failed." );
				}
			}
			else {
				throw new Exception( "file:" + file.getName() + "is only readable, can't be deleted!" );
			}
		}
		else {
			throw new Exception( "file:" + file.getName() + "is a directory!" );
		}
	}

}