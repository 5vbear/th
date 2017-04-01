package th.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FileTools {
	private static final Logger logger = Logger.getLogger( FileTools.class );

	public static boolean mkdirs( String dirPath ) {

		File dirFile = new File( dirPath );
		if ( !dirFile.exists() ) {
			return dirFile.mkdirs();
		}
		return false;
	}

	public static String buildFullFilePath( String path, String fileName ) {
		if ( path.endsWith( "/" ) || path.endsWith( File.separator ) ) {
			return path + fileName;
		}
		return path + "/" + fileName;

	}

	public static void closeReader( Reader fileReader ) {

		if ( fileReader != null ) {
			try {
				fileReader.close();
				fileReader = null;
			}
			catch ( IOException e ) {
				logger.error( "close reader error.", e );
			}
		}
	}

	public static void closeWriter( Writer printWriter ) {

		if ( printWriter != null ) {
			try {
				printWriter.close();
			}
			catch ( Exception e ) {
				logger.error( "close writer error.", e );
			}
		}
	}

	public static void removeDirFiles( String root, String fileExpr ) {

		List<File> list = getSubDirFiles( root, fileExpr );
		for ( File file : list ) {
			file.delete();
		}
	}

	public static void removeDirByDirName( String root, String dirName ) {
		File file = new File( FileTools.buildFullFilePath( root, dirName ) );
		if ( file.exists() ) {
			file.delete();
		}
	}

	public static void removeSubFiles( String root ) {

		List<File> list = getSubFiles( root );
		for ( File file : list ) {
			file.delete();
		}
	}

	public static void removeFile( String path, String fileName ) {

		File file = new File( FileTools.buildFullFilePath( path, fileName ) );
		if ( file.exists() ) {
			file.delete();
		}
	}

	public static List<File> getSubDirFiles( String root, String fileExpr ) {

		List<File> list = new ArrayList<File>();
		getFileLists( root, list, fileExpr );
		return list;
	}

	public static List<File> getSubFiles( String root ) {

		List<File> list = new ArrayList<File>();
		File file = new File( root );
		File[] subFile = file.listFiles();
		for ( int i = 0; i < subFile.length; i++ ) {
			if ( subFile[i].isFile() ) {
				list.add( subFile[i] );
			}
		}
		return list;
	}

	private static void getFileLists( String root, List<File> list, String fileExpr ) {

		File file = new File( root );
		File[] subFile = file.listFiles( filter( fileExpr ) );
		for ( int i = 0; i < subFile.length; i++ ) {
			if ( subFile[i].isFile() ) {
				list.add( subFile[i] );
			}
		}

		File[] subDir = file.listFiles();
		for ( int i = 0; i < subDir.length; i++ ) {
			if ( subDir[i].isDirectory() ) {
				getFileLists( subDir[i].getPath(), list, fileExpr );
			}
		}
	}

	private static FilenameFilter filter( final String fileExpr ) {

		return new FilenameFilter() {

			public boolean accept( File file, String path ) {
				String filename = new File( path ).getName();
				return filename.matches( fileExpr );
			}
		};
	}

	/**
	 * 读取文件到byte[]
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static byte[] fileToByteArray( File file ) throws Exception {
		byte[] outputByte = null;
		FileInputStream fis = null;
		try {
			if ( file != null && file.isFile() ) {
				fis = new FileInputStream( file );
				int len = fis.available();
				outputByte = new byte[len];
				fis.read( outputByte );
			}

		}
		catch ( IOException ex ) {
			throw ex;
		}
		finally {
			if ( fis != null ) {
				fis.close();
			}
		}
		return outputByte;
	}
}
