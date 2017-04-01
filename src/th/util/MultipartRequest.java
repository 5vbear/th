package th.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import th.com.property.LocalProperties;


public class MultipartRequest extends HttpServletRequestWrapper{

    private static String className = "MultipartRequest ";
    
	HashMap requestMap = null;

	ArrayList uploadFileItem = null;

	ArrayList uploadFileName = null;

    int uploadFileCount = 0;

	
	//临时文件路径
	String tmpFilePath = null;
	String enc = null;
	
	int readOnMemorySize = 512000;
	int uploadFileSizeMax = -1;
	
	
	public MultipartRequest(HttpServletRequest request) throws Exception{
		super(request);
		
		//Config値設定
		tmpFilePath = LocalProperties.getProperty("FTP_TMP_FILE_PATH_MEDIA");
		enc = "UTF-8";

		//メンバ初期化
		uploadFileItem = new ArrayList();
		uploadFileName = new ArrayList();
	
		requestMap = new HashMap();
		
		
		doParse();
	}
	
	
	// 设置上传文件信息
	private void doParse() throws Exception{
		String methodName = "doParse() ";
		
		HttpServletRequest request = (HttpServletRequest)getRequest();
		if(request == null){
			throw new Exception("Systemerr!");
		}
		
		//创建临时文件目录
		File tmpFile = new File(tmpFilePath);
		if(!tmpFile.exists()){
			if(!tmpFile.mkdirs()){
				throw new Exception("Systemerr!");
			}
		}
		
		DiskFileItemFactory dfi = new DiskFileItemFactory();   
		//临时文件保存
		dfi.setRepository(tmpFile);  
	    //设置缓冲区大小
		dfi.setSizeThreshold(readOnMemorySize); 
		
		ServletFileUpload sfu = new ServletFileUpload(dfi); 
		//设置上传文件最大限制(-1 无限制)
		sfu.setSizeMax(uploadFileSizeMax);  
		//设置CharacterEncoding
	    sfu.setHeaderEncoding(enc);  
	    
		try {
			//上传情报取得
			List objLst= sfu.parseRequest(request);
			
			Iterator objItr=objLst.iterator();
			
			while(objItr.hasNext()){
		        
				FileItem objFi=(FileItem)objItr.next();
				
				String key = objFi.getFieldName();	
				
				//文件类型的场合
				if(!objFi.isFormField()){
					String fileName = objFi.getName().substring(objFi.getName().lastIndexOf("\\") + 1);

					if(this.requestMap == null){
						this.requestMap = new HashMap();
					}
					if (requestMap.containsKey(key)) {
						if (requestMap.get(key) instanceof String) {
							String val = (String) requestMap.get(key);
							String[] arr = {val, fileName};
							this.requestMap.put(key,arr);
						} else if (requestMap.get(key) instanceof String[]) {
							String[] val = (String[]) requestMap.get(key);
							String[] arr = new String[val.length + 1];
							for (int i = 0; i  < val.length; i++) {
								arr[i] = val[i];
							}
							arr[arr.length - 1] = fileName;
							this.requestMap.put(key,arr);
						} else {
							this.requestMap.put(key,fileName);
						}
					} else {
						this.requestMap.put(key,fileName);
					}
					
					uploadFileItem.add(uploadFileCount,objFi);
					uploadFileName.add(uploadFileCount,fileName);
					uploadFileCount++;

				}else{
					if(this.requestMap == null){
						this.requestMap = new HashMap();
					}
					if (requestMap.containsKey(key)) {
						if (requestMap.get(key) instanceof String) {
							String val = (String) requestMap.get(key);
							String[] arr = {val, objFi.getString(enc)};
							this.requestMap.put(key,arr);
						} else if (requestMap.get(key) instanceof String[]) {
							String[] val = (String[]) requestMap.get(key);
							String[] arr = new String[val.length + 1];
							for (int i = 0; i  < val.length; i++) {
								arr[i] = val[i];
							}
							arr[arr.length - 1] = objFi.getString(enc);
							this.requestMap.put(key,arr);
						} else {
							this.requestMap.put(key,objFi.getString(enc));
						}
					} else {
						this.requestMap.put(key,objFi.getString(enc));
					}
				}
		    }
	    }catch (FileUploadException fue){
	    	throw new Exception("Systemerr!");
	    }catch (Exception e){
	    	throw new Exception("Systemerr!");
	    }
		  
		return;
	}
	
	
	public File[] uploadFileWrite(String suffix)throws Exception{
		String methodName = "uploadFileWrite";
		
		File[] diskFile = null;
		FileItem uploadFile = null;
		
		if(suffix == null){
			suffix = "";
		}
		
		try{
			if(uploadFileCount > 0){
			
				diskFile= new File[uploadFileCount];
			
				for(int i=0 ; i<uploadFileCount ; i++){
					diskFile[i] = new File(tmpFilePath,uploadFileName.get(i)+suffix);

					uploadFile = (FileItem)uploadFileItem.get(i);
					uploadFile.write(diskFile[i]);
				}		
				
				uploadFileItemDelete();
			}
		}catch(Exception e){
	    	throw new Exception("Systemerr!");

			
		}
		return diskFile;

	}

	public File[] uploadFileWrite(String suffix, String filePath)throws Exception{
		File[] diskFile = null;
		FileItem uploadFile = null;

		if(suffix == null){
			suffix = "";
		}

		try{
			if(uploadFileCount > 0){
			
				diskFile= new File[uploadFileCount];
			
				for(int i = 0 ; i < uploadFileCount ; i++){
					String fileName = uploadFileName.get(i) + suffix;

					String addFileName = "";

					int count=1;
					for(; ; count++){
						
						if(count <= 1){
							addFileName = "";
						}else{
							addFileName = "_" + (count-1);
						}
						
						diskFile[i] = new File(filePath, fileName + addFileName);

						if( diskFile[i].exists()){
							
						}else{
							break;
						}
					}
					
					uploadFile = (FileItem)uploadFileItem.get(i);
					uploadFile.write(diskFile[i]);
				}
				
				uploadFileItemDelete();
			}
		}catch(Exception e){
	    	throw new Exception("Systemerr!");

		}
		return diskFile;

	}
	
	public int uploadFileItemDelete(){
		
		int deleteCount = 0;
		FileItem uploadFile = null;
		
		for(; deleteCount<uploadFileCount ; deleteCount++){
			uploadFile = (FileItem)uploadFileItem.get(deleteCount);
			uploadFile.delete();
		}	
		
		return deleteCount;
	}
	
	public void paramToRequestAttribute(String key){
		
		setAttribute(key,requestMap.get(key));
	}
	
	public String getParameter(String key){

		return (String)requestMap.get(key);
	}
	
	public String[] getParameters(String key){
		if (requestMap.get(key) instanceof String[]) {
			return (String[])requestMap.get(key);
		} else {
			String[] arr = new String[1];
			arr[0] = (String)requestMap.get(key);
			return arr;
		}
	}
	
	public ArrayList getUploadFileName() {
		
		return uploadFileName;
	}

	public ArrayList getParameterNamesList(){
		
		return new ArrayList(requestMap.keySet());
	}
	
	public ArrayList getUploadFileItem() {

		return uploadFileItem;
	}


	/**
	 * @return the uploadFileCount
	 */
	public int getUploadFileCount() {
		return uploadFileCount;
	}


	/**
	 * @param uploadFileCount the uploadFileCount to set
	 */
	public void setUploadFileCount(int uploadFileCount) {
		this.uploadFileCount = uploadFileCount;
	}

}
