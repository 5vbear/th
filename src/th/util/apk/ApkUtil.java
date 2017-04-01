/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.util.apk;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;

/**
 * Descriptions
 * 
 * @version 2014-1-20
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ApkUtil {
	//
	protected Logger logger = Logger.getLogger( this.getClass() );
	
	private static final String aaptCommandParams = " d badging ";// D:/apk/tencentmobilemanager4.3.1_android_build1607_102027av.apk";
//	private static final String aaptCommand = "D:/apktool-install-windows-r05-ibot/aapt.exe d badging ";// D:/apk/tencentmobilemanager4.3.1_android_build1607_102027av.apk";
	private static String aaptCommand="";
	static {
		try {
			 aaptCommand = LocalProperties.getProperty("AAPT_COMMAND_PATH");
		}
		catch ( LocalPropertiesException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	String s;

	public ApkInfo getApkInfo( String path ) {
		

		ApkInfo apkInfo = null;
		try {
			String command = aaptCommand +aaptCommandParams+ path;
			Runtime rt = Runtime.getRuntime();
			this.logger.info( "------------command -start-------------" );
			this.logger.info( "command is :" + command );
// Process pcs = rt.exec(str);
			Process pcs = rt.exec( command );
			BufferedReader br = new BufferedReader( new InputStreamReader( pcs.getInputStream() ) );
			String line = new String();
			apkInfo = new ApkInfo();
			while ( ( line = br.readLine() ) != null ) {

				if ( line.startsWith( "package:" ) ) {
					String[] result = line.replaceFirst( "package:", "" ).trim().split( " " );
					for ( int i = 0; i < result.length; i++ ) {
						String temp = result[i];
						String[] tempArr = temp.split( "=" );
						if ( tempArr[0].equals( "name" ) ) {
							apkInfo.setPackageName( tempArr[1] );
						}
						else if ( tempArr[0].equals( "versionName" ) ) {
							apkInfo.setVersion( tempArr[1] );
						}
						else if ( tempArr[0].equals( "versionCode" ) ) {
							apkInfo.setVersionCode( tempArr[1] );
						}
						

// else if(tempArr[0].equals("versionCode")){
// resultMap.put( "versionCode", tempArr[1] );
// }
					}
					break;
				}
			}
			this.logger.info( apkInfo );
//			try {
//				int ret =pcs.waitFor();
//				if(ret!=0){
//					this.logger.info( "解析命令执行失败" );
//					throw new InterruptedException();
//				}
//			}
//			catch ( InterruptedException e ) {
//				this.logger.info( "processes was interrupted" );
//			}
			br.close();
//			int ret = pcs.exitValue();
			// System.out.println(ret);

		}
		catch ( Exception e ) {
			this.logger.error( "processes error " + e );
		}
		this.logger.info( "解析命令执行结束" );
		return apkInfo;
	}

	public static void test( Boolean b ) {
		System.out.println( "begin" );
		System.out.println( b );
		if ( b ) {
			b = new Boolean( false );
		}
		else {
			b = new Boolean( true );
		}
		System.out.println( b );
		System.out.println( "end" );
		// return b;
	}

	public void testString( Person p ) {
		System.out.println( "begin" );
		System.out.println( p.getName() );
		String name = p.getName();// s+" b ";
		p.setName( name + "b" );// s+" b ";
		System.out.println( p.getName() );
		System.out.println( "end" );
		// return b;
	}

	public class Person {
		String name;

		public String getName() {
			return name;
		}

		public void setName( String name ) {
			this.name = name;
		}

		public Person() {

		}
	}

	/**
	 * @param args
	 */
	public static void main( String[] args ) {

		// TODO Auto-generated method stub
		try {
			// String[] cmd = new String[] { "F:/task/aapt.exe", "l -a
			// F:/task/update/10-6-23/apk/4003373.apk >
			// F:/task/update/10-6-23/apk/4003373.txt" };
			// Process process = Runtime.getRuntime().exec(cmd,null,new
			// File("F:/task/"));
// String command = "D:/apktool-install-windows-r05-ibot/aapt.exe d badging D:/apk/appFramework-debug.apk";
// String command = "D:/apktool-install-windows-r05-ibot/aapt.exe d badging D:/apk/com.tencent.mtt_450520.apk";
// String command = "D:/apktool-install-windows-r05-ibot/aapt.exe d badging D:/apk/Grand_Theft_Auto_III_v1.2.apk";
// String command = "D:/apktool-install-windows-r05-ibot/aapt.exe d badging D:/apk/qqbrowser_4.5.2.540_20820.apk";
// String command =
// "D:/apktool-install-windows-r05-ibot/aapt.exe d badging D:/apk/qqphonebook4.9.0_android_build1150_101004.apk";
			String command = "/apktool-install-windows-r05-ibot/aapt.exe d badging D:/apk/tencentmobilemanager4.3.1_android_build1607_102027av.apk";

			// command ="F:/task/aapt.exe l -a
			// F:/task/update/10-6-23/apk/4003373.apk >
			// F:/task/update/10-6-23/apk/4003373.txt";

			/*
			 * command = "/home/dingym/aapt d badging /home/dingym/YichaMarket.apk";
			 * System.out.println("------------start-------------"); Runtime.getRuntime().exec(command);
			 * System.out.println("------------end---------------");
			 */
			/*
			 * BufferedReader r = new BufferedReader(new InputStreamReader(process .getErrorStream())); String l = null;
			 * while ((l = r.readLine()) != null) { System.out.println(l); }
			 */

			Runtime rt = Runtime.getRuntime();
			System.out.println( "------------start-test-------------" );
			// String str[] = { "/bin/sh", "-c", "./aapt d badging YichaMarket.apk" };
			System.out.println( "------------end-test---------------" );
// Process pcs = rt.exec(str);
			Process pcs = rt.exec( command );
			BufferedReader br = new BufferedReader( new InputStreamReader( pcs.getInputStream() ) );
			String line = new String();
			ApkInfo apkInfo = new ApkInfo();
			while ( ( line = br.readLine() ) != null ) {

				if ( line.startsWith( "package:" ) ) {
					String[] result = line.replaceFirst( "package:", "" ).trim().split( " " );
					for ( int i = 0; i < result.length; i++ ) {
						String temp = result[i];
						String[] tempArr = temp.split( "=" );
						if ( tempArr[0].equals( "name" ) ) {
							apkInfo.setPackageName( tempArr[1] );
						}
						else if ( tempArr[0].equals( "versionName" ) ) {
							apkInfo.setVersion( tempArr[1] );
						}

// else if(tempArr[0].equals("versionCode")){
// resultMap.put( "versionCode", tempArr[1] );
// }
					}
					break;
				}
			}
			System.out.println( apkInfo );
			try {
				pcs.waitFor();
			}
			catch ( InterruptedException e ) {
				System.err.println( "processes was interrupted" );
			}
			br.close();
//			int ret = pcs.exitValue();
			//int ret =pcs.waitFor();
			// System.out.println(ret);

		}
		catch ( Exception e ) {
			e.printStackTrace();
		}

	}

}
