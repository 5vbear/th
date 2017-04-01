package th.management.backup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import th.com.util.Define;


public class BackupManagement {

	private static String OS_WINDOWS = "1";
	private static String OS_LINUX = "2";
	private Log logger = LogFactory.getLog(BackupManagement.class.getName());
	private static Date latestFileName;
	
	public static void setTime(Date date){
		latestFileName = date;
	}
	public BackupManagement() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * 开始备份
	 */
	public void doBackup(){
		logger.info("METHOD:doBackup  start...");
		String authentication = Define.BACKUP_CONFIG;
		String dbname = Define.BACKUP_DBNAME;
		String backupDirsName = "";
		String backupFileName=""; 
		String pgpass_url = "";
		String dirs_url = "";
		String os = currentSystem();
		Runtime rt = Runtime.getRuntime(); 
		Process child =null;
		logger.info("BACKUP_CONFIG = "+authentication);
		logger.info("BACKUP_DBNAME = "+dbname);
        try {        
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = sdf.format(latestFileName);
        	//windows操作系统
        	if(os.equals(OS_WINDOWS)){
        		backupDirsName=Define.BACKUP_DIRECTORY;
        		backupFileName = backupDirsName+"\\" + currentTime +".backup" ;
        		logger.info("SYSTEM:WINDOWS...");
        		String appData = this.getAppData();
        		if(null !=appData&&!"".equals(appData)){
        			pgpass_url = appData+"\\postgresql\\pgpass.conf";
        			dirs_url = appData+"\\postgresql";
        			this.checkPass(authentication,pgpass_url,dirs_url);
        		}
        		logger.info("BACKUP_DUMP_CMD = "+Define.BACKUP_DUMP);
        		// 调用 postgresql 的 cmd:        
        		child = rt.exec(Define.BACKUP_DUMP+" "+dbname);// 设置导出编码为utf8。这里必须是utf8  
        	//linux操作系统
        	}else if(os.equals(OS_LINUX)){
        		backupDirsName=Define.BACKUP_DIRECTORY;
        		backupFileName = backupDirsName+"/" + currentTime +".backup" ;
        		logger.info("SYSTEM:LINUX...");
        		pgpass_url ="/home/postgres/.pgpass";
        		dirs_url = "/home/postgres";
        		this.checkPass(authentication,pgpass_url,dirs_url);
        		// 调用 postgresql 的 cmd:        
        		child = rt.exec(Define.BACKUP_DUMP_LINUX+" "+dbname);// 设置导出编码为utf8。这里必须是utf8  
        	}
                   
            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行        
            InputStream in = child.getInputStream();// 控制台的输出信息作为输入流        
                               
            InputStreamReader xx = new InputStreamReader(in, "utf8");// 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码        
                   
            String inStr;       
            // 组合控制台输出信息字符串        
            BufferedReader br = new BufferedReader(xx);        
//            
//            String currentTime = sdf.format(latestFileName);
//            backupFileName = backupDirsName+"\\" + currentTime +".backup" ;
            File a = new File(backupFileName);
            File b = new File(backupDirsName);
            if(!b.exists()){
            	b.mkdirs();
            }
            if(a.exists()){
            	a.delete();
            }
            a.createNewFile();
            
            // 要用来做导入用的sql目标文件：        
            FileOutputStream fout = new FileOutputStream(backupFileName);        
            OutputStreamWriter writer = new OutputStreamWriter(fout, "utf8");
            StringBuffer sb = new StringBuffer("");  
            while ((inStr = br.readLine()) != null) {        
                sb.append(inStr + "\r\n");
                if(sb.length()>1024){
	                writer.write(sb.toString());        
	                // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免        
	                writer.flush();
	                sb = new StringBuffer();
                }
            }
            if(sb.length()>0){
            	writer.write(sb.toString());        
                // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免        
                writer.flush();
            }
       
            // 别忘记关闭输入输出流        
            in.close();        
            xx.close();        
            br.close();        
            writer.close();        
            fout.close();        
            System.out.println("/* Output OK! */");        
       
        } catch (Exception e) {        
            e.printStackTrace();        
        }        

		logger.info("METHOD:doBackup  end...");
	}
	/**
	 * 操作系统检测
	 * @return
	 */
	private String currentSystem() {
		// TODO Auto-generated method stub
        String os = System.getProperty("os.name");
        if (os.toUpperCase().indexOf("WINDOWS") != -1 ) {  
            return "1";  
        } else {  
            return "2";  
        }  
    }  
	/**
	 * 密码认证
	 * @param authentication
	 * @param pgpass_url
	 * @param dirs_url 
	 */
	private void checkPass(String authentication, String pgpass_url, String dirs_url) {
		// TODO Auto-generated method stub
		File f1 = new File(pgpass_url);
		File f2 = new File(dirs_url);
		PrintWriter w =null;
		try {
			//文件存在
			if(f1.exists()){
				//没有认证
				if(!exist(authentication,f1)){
						w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f1, true)));
						w.write("\r\n");
						w.write(authentication);
				}
			//文件不存在
			}else{
				if(!f2.exists()){
					f2.mkdirs();
				}
				f1.createNewFile();
				w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f1, true)));
				w.write(authentication);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(null!= w){
				w.flush();
				w.close();
			}
		}
	}
	/**
	 * 是否已经认证过
	 * @param authentication
	 * @param f1
	 * @return
	 */
	private boolean exist(String authentication, File f1) {
		// TODO Auto-generated method stub
		try {
			String content;
			BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
			while(null!=(content =r.readLine())){
				if(content.contains(authentication)){
					return true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 获取appdata路径
	 * @return
	 */
	private String getAppData() {
		// TODO Auto-generated method stub
		Map m = System.getenv();
		String appdata = (String) m.get("APPDATA");
		return appdata;
	}
	/**
	 * 删除备份
	 */
	public void clear(){
		String os = currentSystem();
		String backupDirsName=Define.BACKUP_DIRECTORY;
		String backupName = null;
		File dirs = new File(backupDirsName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(!dirs.exists()){
			return;
		}
		String[] files = dirs.list();
		logger.info("一共有"+files.length+"个文件");
		for (String fileName : files) {
			String first = parse(fileName);
			if(sdf.format(latestFileName).equals(first)){
				logger.info(backupName+"保留,不删除");
				continue;
			}else{
				if(os.equals(OS_WINDOWS)){
					backupName = backupDirsName+"\\"+fileName;
				}else{
					backupName = backupDirsName+"/"+fileName;
				}
				clearThis(backupName);
				logger.info(backupName+"删除成功");
			}
		}
	}
	/**
	 * 执行删除操作
	 * @param backupName
	 */
	private void clearThis(String backupName) {
		// TODO Auto-generated method stub
		File clear = new File(backupName);
		if(clear.exists()){
			clear.delete();
		}
	}
	/**
	 * 字符串转long型
	 * @param fileName
	 * @return
	 */
	private String parse(String fileName) {
		// TODO Auto-generated method stub
		try {
			String[] split = fileName.split("\\.");
			if(split.length == 1){
				return "";
			}
			return split[0];
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
	public static void main(String[] args) {
//		BackupManagement a = new BackupManagement();
//		a.clear();
		BackupManagement bm = new BackupManagement();
		bm.currentSystem();
	}
}
