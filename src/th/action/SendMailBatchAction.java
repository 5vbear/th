package th.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import th.action.MonitorAction;
import th.com.property.LocalProperties;
import th.com.util.Define;
import th.dao.MonitorDAO;
import th.entity.AlarmBean;
import th.util.mail.MailUtil;

public class SendMailBatchAction extends BaseAction{

    private static String CLASS = "SendMailBatchMain ";
    @SuppressWarnings("rawtypes")
	public void mailBatch() {
    	
    	MonitorAction action = new MonitorAction();
    	try {
    		//取得所有已设定邮件报警的组织及上级组织下用户的邮箱、端机信息
			List<AlarmBean> list = action.getAlarmListInfo();
			if(null != list && list.size() > 0){
				//email address - mail content
				Map<String,String> mailMap = new HashMap<String,String>();
				StringBuffer mailContent = new StringBuffer();
				String emailTemp = "";
				String alertNameTemp = "";
				Map<String, Boolean> selectFlagMap = new HashMap<String, Boolean>();
				String flag = "";
				for(int i = 0; i < list.size(); i++){
					AlarmBean alarm = list.get(i);
					flag = alarm.getAlertID() + "-" + alarm.getMachineID();
					
					// 相同alertID 和 machineID不再进行报警判断
					if(!selectFlagMap.containsKey(flag)){
						if (!isAlarm(alarm.getAlertID(), alarm.getMachineID())){
							selectFlagMap.put(flag, false);
							continue;
						} else {
							selectFlagMap.put(flag, true);
						}
					} else if(selectFlagMap.containsKey(flag) && !selectFlagMap.get(flag)){
						continue;
					}
					
					
					//处理第一条消息
					if("".equals(emailTemp)){
						// 遍历第一条信息
						mailContent.append(alarm.getUserName());
						mailContent.append(" ");
						mailContent.append("你好:");
						mailContent.append("\r\n");
						mailContent.append(alarm.getAlertName());
						mailContent.append(":");
						mailContent.append(!Define.ALERTNAME_FFFWL.equals(alarm.getAlertID()) ? alarm.getMachineMark() : "请及时审核端机");
						mailContent.append(" ");

						emailTemp = alarm.getEmail();
						alertNameTemp = alarm.getAlertName();
					}
					//连续相同邮件时
					else if (emailTemp.equals(alarm.getEmail())) {
						// 相同报警类型
						if (alertNameTemp.equals(alarm.getAlertName())) {
							mailContent.append(!Define.ALERTNAME_FFFWL.equals(alarm.getAlertID()) ? alarm.getMachineMark() : "");
							mailContent.append(" ");
						} 
						//不同报警类型
						else {
							mailContent.append("\r\n");
							mailContent.append(alarm.getAlertName());
							mailContent.append(":");
							mailContent.append(!Define.ALERTNAME_FFFWL.equals(alarm.getAlertID()) ? alarm.getMachineMark() : "请及时审核端机");
							mailContent.append(" ");
						}
						
						//emailTemp = alarm.getEmail();
						alertNameTemp = alarm.getAlertName();
					} 
					//处理两条不同邮件记录时，将之前的邮件和拼接邮件内容添加到map
					else {
						mailMap.put(emailTemp, mailContent.toString());
						//清空StringBuffer
						mailContent.setLength(0);
						
						mailContent.append(alarm.getUserName());
						mailContent.append(" ");
						mailContent.append("你好:");
						mailContent.append("\r\n");
						mailContent.append(alarm.getAlertName());
						mailContent.append(":");
						mailContent.append(!Define.ALERTNAME_FFFWL.equals(alarm.getAlertID()) ? alarm.getMachineMark() : "请及时审核端机");
						mailContent.append(" ");
						
						emailTemp = alarm.getEmail();
						alertNameTemp = alarm.getAlertName();
					}
				}
				
				mailMap.put(emailTemp, mailContent.toString());
				
				if(!mailMap.isEmpty()){
					//batch send mail
					Iterator it = mailMap.entrySet().iterator();   
				    while (it.hasNext()) {
				    	Map.Entry entry = (Map.Entry) it.next();
				    	String[] mails = {(String)entry.getKey()}; 
				    	new MailUtil().sendTextMail(mails,Define.ALERT_MAIL_TITLE,(String)entry.getValue());
				    }
				    logger.info(CLASS + "邮件已发送");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /** 判断已设定报警组织中是否有报警端机
     * @param alertType
     * @param machineID
     * @return true--非法; false--合法
     * @throws Exception
     */
    public static boolean isAlarm(String alertType, String machineID){
    	try{
    		MonitorAction action = new MonitorAction();
        	
        	//非法进程
        	if(Define.ALERTNAME_FFJC.equals(alertType)){
        		return action.isUnauthProcess(LocalProperties.getProperty("FTP_UPLOAD_FILE_PATH_PROCESS"), machineID);
        	} 
        	//非法服务
        	else if(Define.ALERTNAME_FFFW.equals(alertType)){
        		return action.isUnauthService(LocalProperties.getProperty("FTP_UPLOAD_FILE_PATH_SERVICE"), machineID);
        	} 
        	//cpu负荷过高
        	else if(Define.ALERTNAME_FHGG_CPU.equals(alertType)){
        		return action.isGreaterCommonVal(Define.ALERT_CPU_FIELD_NAME, machineID);
        	} 
        	//内存负荷过高
        	else if(Define.ALERTNAME_FHGG_MEMORY.equals(alertType)){
        		return action.isGreaterCommonVal(Define.ALERT_MEMORY_FIELD_NAME, machineID);
        	} 
        	//硬盘容量不足
        	else if(Define.ALERTNAME_RLBZ_HARD.equals(alertType)){
        		return action.isGreaterCommonVal(Define.ALERT_DISK_UNUSED_FIELD_NAME, machineID);
        	} 
        	//上行速率过慢
        	else if(Define.ALERTNAME_SDGM_UP.equals(alertType)){
        		return action.isGreaterCommonVal(Define.ALERT_UPLOAD_RATE_FIELD_NAME, machineID);
        	} 
        	//下行速率过慢
        	else if(Define.ALERTNAME_SDGM_DOWN.equals(alertType)){
        		return action.isGreaterCommonVal(Define.ALERT_DOWNLOAD_RATE_FIELD_NAME, machineID);
        	} 
        	//非法访问率过高(未分配组织的机器数>20 时即报警)(报警上限机器数可配置)
        	else if(Define.ALERTNAME_FFFWL.equals(alertType)){
        		return new MonitorDAO().isUnauthAccess();
        	} 
        	//断线报警
        	else if(Define.ALERTNAME_DXJB.equals(alertType)){
        		return action.isMachineBreak(machineID);
        	} else {
        		return false;
        	}
    	} catch(Exception e){
    		return false;
    	}
    	
    }
}
