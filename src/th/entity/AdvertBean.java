package th.entity;

import java.util.ArrayList;
import java.util.List;

import th.com.util.Define;

public class AdvertBean {
	/** 素材管理 */
	//素材分组
	private String material_group = "";
	//自定义组
	private String custom_group = "";
	//素材分组名称
	private String groupName = "";
	//素材类型
	private String advert_type = "";
	//素材文件路径
	private String[] material_filePath = null;
	//素材类型
	private String[] material_type = null;
	//素材名称
	private String[] material_name = null;
	//素材说明
	private String[] material_describe = null;
	//素材联接
	private String[] material_link = null;
	//字幕名称
	private String subtitles_name = "";
	//字幕说明
	private String subtitles_describe = "";
	//滚动文字
	private String roll_word = "";
	//背景图片路径
	private String background_filePath = "";
	//背景颜色
	private String background_color = "";
	//字体颜色
	private String word_color = "";
	//字体属性
	private String word_attribute = "";
	//字体高
	private String word_height = "";
	//字体宽
	private String word_width = "";
	//字体水平对齐(0:居左 1:居中 2:居右)
	private String text_align = "";
	//字体垂直对齐(0:居左 1:居中 2:居右)
	private String vertical_align = "";
	//滚动延迟(微妙)
	private String roll_delay = "";
	//字体样式
	private String word_style = "";
	//媒体文件上传数量
	private int uploadFileCount = 0;
	//媒体文件服务器路径
	private String[] serviceFilePath = null;
	//检索用素材类型
	private String materialType = "";
	//检索用素材名称
	private String materialName = "";
	//检索用素材文件名称
	private String materialFilelName = "";
	//检索用添加时间From
	private String materialAddDateFrom = "";
	//检索用添加时间To
	private String materialAddDateTo = "";
	//检索用文件格式
	private String materialFileType = "";
	//检索结果素材ID
	private String materialId = "";
	//检索结果字幕ID
	private String subtitlesId = "";
	//检索结果过期时间
	private String expireTime = "";
	//检索结果素材链接
	private String materialLink = "";
	//操作者
	private String operator = "";
	//创建时间
	private String createTime = "";
	//审核情况
	private String auditStatus = "";
	// 检索结果总数
    private int total_num = 0;
    // 检索结果现在页
    private int point_num = 0;
    // 检索结果每页显示行数
    private int view_num = Define.VIEW_NUM;;
    //排序方式
    private String sortField = "";
    //排序类型
    private String sortType = "";
	
    /** 布局管理 */
    //布局ID
    private String layoutId = "";
    //布局名称
    private String layoutName = "";
    //布局描述
    private String layoutDescribe = "";
    //布局类型(几分屏)
    private String layoutScreen = "";
    //布局添加时间From
	private String layoutAddDateFrom = "";
	//布局添加时间To
	private String layoutAddDateTo = "";
	//布局分辨率宽
	private int layoutwidth = 0;
	//布局分辨率高
	private int layoutHeight = 0;
	//分屏数
	private int screenNum = 0;
	//布局坐标信息
	private String coordinate = "";
	//分屏开始横坐标
	private int[] screenStartX = null;
	//分屏开始纵坐标
	private int[] screenStartY = null;
	//分屏结束横坐标
	private int[] screenEndX = null;
	//分屏结束纵坐标
	private int[] screenEndY = null;
	//分屏名称
	private String[] screenName = null;
	//分屏ID
	private String windowId = "";
	//分屏No
	private int windowNo = 0;
	
    
	/** 节目单管理 */
	//节目单ID
    private String programlistId = "";
    //节目单名称
    private String programlistName = "";
    //节目单描述
    private String programlistDescribe = "";
    //节目单播放时长
    private int programlistPlayTime = 0;
    //节目单添加时间From
	private String programlistAddDateFrom = "";
	//节目单添加时间To
	private String programlistAddDateTo = "";
	//节目单所属组织ID
	private String orgId = "";
	//节目单包含素材数量
	private int programlistMaterialNum = 0;
	//素材播放时长
	private int materialPlayTime = 0;
	//素材显示级别
	private int materialLevel = 0;
	//节目单包含素材
	private List<AdvertBean> programlistMaterials = null;
	//节目单文件路径(BILL_FILE_LOCATION)
	private String bill_file_location = "";
	//布局List
	private List<String> layoutList = new ArrayList<String>();
	
	/** 节目单发布 */
	//端机ID
	private String[] machineId = null;
	//播放屏幕
	private String screenType = "";
	//临时广告
	private String tempAds = "";
	//立刻下发
	private String sendType = "";
	//下发时间(节目单)
	private String sendTime = "";
	//结束播放时间(节目单)
	private String valueTime = "";
	//下发时间(字幕)
	private String sendTime2 = "";
	//结束播放时间(字幕)
	private String valueTime2 = "";
	//下载控制
	private String controlType = "";
	//端机类型
	private String[] machingType = null;
	
	/** 节目单组管理 */
	//节目单组ID
	private String programlistGroupId = "";
	//节目单组名称
	private String programlistGroupName = "";
	//节目单组
	private String[] programlistGroup = null;
	//节目列表说明
	private String programlistGroup_desc = "";
	//循环周期
	private int programlistGroup_loop = 0;
	//节目单动作说明List
	private String programlistAction = "";
	//节目单动作说明List
	private String[] programlistActionList = null;
	
	//子组织List
	private String subOrgIdArr = "";
	
	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**
	 * @return the material_group
	 */
	public String getMaterial_group() {
		return material_group;
	}
	/**
	 * @param material_group the material_group to set
	 */
	public void setMaterial_group(String material_group) {
		this.material_group = material_group;
	}
	/**
	 * @return the custom_group
	 */
	public String getCustom_group() {
		return custom_group;
	}
	/**
	 * @param custom_group the custom_group to set
	 */
	public void setCustom_group(String custom_group) {
		this.custom_group = custom_group;
	}
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return the material_type
	 */
	public String getAdvert_type() {
		return advert_type;
	}
	/**
	 * @param material_type the material_type to set
	 */
	public void setAdvert_type(String advert_type) {
		this.advert_type = advert_type;
	}
	/**
	 * @return the material_filePath
	 */
	public String[] getMaterial_filePath() {
		return material_filePath;
	}
	/**
	 * @param material_filePath the material_filePath to set
	 */
	public void setMaterial_filePath(String[] material_filePath) {
		this.material_filePath = material_filePath;
	}
	/**
	 * @return the material_type
	 */
	public String[] getMaterial_type() {
		return material_type;
	}
	/**
	 * @param material_type the material_type to set
	 */
	public void setMaterial_type(String[] material_type) {
		this.material_type = material_type;
	}
	/**
	 * @return the material_name
	 */
	public String[] getMaterial_name() {
		return material_name;
	}
	/**
	 * @param material_name the material_name to set
	 */
	public void setMaterial_name(String[] material_name) {
		this.material_name = material_name;
	}
	/**
	 * @return the material_describe
	 */
	public String[] getMaterial_describe() {
		return material_describe;
	}
	/**
	 * @param material_describe the material_describe to set
	 */
	public void setMaterial_describe(String[] material_describe) {
		this.material_describe = material_describe;
	}
	/**
	 * @return the material_link
	 */
	public String[] getMaterial_link() {
		return material_link;
	}
	/**
	 * @param material_link the material_link to set
	 */
	public void setMaterial_link(String[] material_link) {
		this.material_link = material_link;
	}
	/**
	 * @return the subtitles_name
	 */
	public String getSubtitles_name() {
		return subtitles_name;
	}
	/**
	 * @param subtitles_name the subtitles_name to set
	 */
	public void setSubtitles_name(String subtitles_name) {
		this.subtitles_name = subtitles_name;
	}
	/**
	 * @return the subtitles_describe
	 */
	public String getSubtitles_describe() {
		return subtitles_describe;
	}
	/**
	 * @param subtitles_describe the subtitles_describe to set
	 */
	public void setSubtitles_describe(String subtitles_describe) {
		this.subtitles_describe = subtitles_describe;
	}
	/**
	 * @return the roll_word
	 */
	public String getRoll_word() {
		return roll_word;
	}
	/**
	 * @param roll_word the roll_word to set
	 */
	public void setRoll_word(String roll_word) {
		this.roll_word = roll_word;
	}
	/**
	 * @return the background_filePath
	 */
	public String getBackground_filePath() {
		return background_filePath;
	}
	/**
	 * @param background_filePath the background_filePath to set
	 */
	public void setBackground_filePath(String background_filePath) {
		this.background_filePath = background_filePath;
	}
	/**
	 * @return the background_color
	 */
	public String getBackground_color() {
		return background_color;
	}
	/**
	 * @param background_color the background_color to set
	 */
	public void setBackground_color(String background_color) {
		this.background_color = background_color;
	}
	/**
	 * @return the word_color
	 */
	public String getWord_color() {
		return word_color;
	}
	/**
	 * @param word_color the word_color to set
	 */
	public void setWord_color(String word_color) {
		this.word_color = word_color;
	}
	/**
	 * @return the word_attribute
	 */
	public String getWord_attribute() {
		return word_attribute;
	}
	/**
	 * @param word_attribute the word_attribute to set
	 */
	public void setWord_attribute(String word_attribute) {
		this.word_attribute = word_attribute;
	}
	/**
	 * @return the word_height
	 */
	public String getWord_height() {
		return word_height;
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
	/**
	 * @param word_height the word_height to set
	 */
	public void setWord_height(String word_height) {
		this.word_height = word_height;
	}
	/**
	 * @return the word_width
	 */
	public String getWord_width() {
		return word_width;
	}
	/**
	 * @param word_width the word_width to set
	 */
	public void setWord_width(String word_width) {
		this.word_width = word_width;
	}
	/**
	 * @return the text_align
	 */
	public String getText_align() {
		return text_align;
	}
	/**
	 * @param text_align the text_align to set
	 */
	public void setText_align(String text_align) {
		this.text_align = text_align;
	}
	/**
	 * @return the vertical_align
	 */
	public String getVertical_align() {
		return vertical_align;
	}
	/**
	 * @param vertical_align the vertical_align to set
	 */
	public void setVertical_align(String vertical_align) {
		this.vertical_align = vertical_align;
	}
	/**
	 * @return the roll_delay
	 */
	public String getRoll_delay() {
		return roll_delay;
	}
	/**
	 * @param roll_delay the roll_delay to set
	 */
	public void setRoll_delay(String roll_delay) {
		this.roll_delay = roll_delay;
	}
	/**
	 * @return the word_style
	 */
	public String getWord_style() {
		return word_style;
	}
	/**
	 * @param word_style the word_style to set
	 */
	public void setWord_style(String word_style) {
		this.word_style = word_style;
	}
	/**
	 * @return the serviceFilePath
	 */
	public String[] getServiceFilePath() {
		return serviceFilePath;
	}
	/**
	 * @param serviceFilePath the serviceFilePath to set
	 */
	public void setServiceFilePath(String[] serviceFilePath) {
		this.serviceFilePath = serviceFilePath;
	}
	/**
	 * @return the materialType
	 */
	public String getMaterialType() {
		return materialType;
	}
	/**
	 * @param materialType the materialType to set
	 */
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	/**
	 * @return the materialName
	 */
	public String getMaterialName() {
		return materialName;
	}
	/**
	 * @param materialName the materialName to set
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	/**
	 * @return the materialFilelName
	 */
	public String getMaterialFilelName() {
		return materialFilelName;
	}
	/**
	 * @param materialFilelName the materialFilelName to set
	 */
	public void setMaterialFilelName(String materialFilelName) {
		this.materialFilelName = materialFilelName;
	}
	/**
	 * @return the materialAddDateFrom
	 */
	public String getMaterialAddDateFrom() {
		return materialAddDateFrom;
	}
	/**
	 * @param materialAddDateFrom the materialAddDateFrom to set
	 */
	public void setMaterialAddDateFrom(String materialAddDateFrom) {
		this.materialAddDateFrom = materialAddDateFrom;
	}
	/**
	 * @return the materialAddDateTo
	 */
	public String getMaterialAddDateTo() {
		return materialAddDateTo;
	}
	/**
	 * @param materialAddDateTo the materialAddDateTo to set
	 */
	public void setMaterialAddDateTo(String materialAddDateTo) {
		this.materialAddDateTo = materialAddDateTo;
	}
	/**
	 * @return the materialFileType
	 */
	public String getMaterialFileType() {
		return materialFileType;
	}
	/**
	 * @param materialFileType the materialFileType to set
	 */
	public void setMaterialFileType(String materialFileType) {
		this.materialFileType = materialFileType;
	}
	/**
	 * @return the materialId
	 */
	public String getMaterialId() {
		return materialId;
	}
	/**
	 * @param materialId the materialId to set
	 */
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	/**
	 * @return the subtitlesId
	 */
	public String getSubtitlesId() {
		return subtitlesId;
	}
	/**
	 * @param subtitlesId the subtitlesId to set
	 */
	public void setSubtitlesId(String subtitlesId) {
		this.subtitlesId = subtitlesId;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the expireTime
	 */
	public String getExpireTime() {
		return expireTime;
	}
	/**
	 * @param expireTime the expireTime to set
	 */
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	/**
	 * @return the materialLink
	 */
	public String getMaterialLink() {
		return materialLink;
	}
	/**
	 * @param materialLink the materialLink to set
	 */
	public void setMaterialLink(String materialLink) {
		this.materialLink = materialLink;
	}
	/**
	 * @return the auditStatus
	 */
	public String getAuditStatus() {
		return auditStatus;
	}
	/**
	 * @param auditStatus the auditStatus to set
	 */
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	/**
	 * @return the total_num
	 */
	public int getTotal_num() {
		return total_num;
	}
	/**
	 * @param total_num the total_num to set
	 */
	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}
	/**
	 * @return the point_num
	 */
	public int getPoint_num() {
		return point_num;
	}
	/**
	 * @param point_num the point_num to set
	 */
	public void setPoint_num(int point_num) {
		this.point_num = point_num;
	}
	/**
	 * @return the view_num
	 */
	public int getView_num() {
		return view_num;
	}
	/**
	 * @param view_num the view_num to set
	 */
	public void setView_num(int view_num) {
		this.view_num = view_num;
	}
	/**
	 * @return the sortField
	 */
	public String getSortField() {
		return sortField;
	}
	/**
	 * @param sortField the sortField to set
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	/**
	 * @return the sortType
	 */
	public String getSortType() {
		return sortType;
	}
	/**
	 * @param sortType the sortType to set
	 */
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	/**
	 * @return the layoutId
	 */
	public String getLayoutId() {
		return layoutId;
	}
	/**
	 * @param layoutId the layoutId to set
	 */
	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}
	/**
	 * @return the layoutName
	 */
	public String getLayoutName() {
		return layoutName;
	}
	/**
	 * @param layoutName the layoutName to set
	 */
	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}
	/**
	 * @return the layoutDescribe
	 */
	public String getLayoutDescribe() {
		return layoutDescribe;
	}
	/**
	 * @param layoutDescribe the layoutDescribe to set
	 */
	public void setLayoutDescribe(String layoutDescribe) {
		this.layoutDescribe = layoutDescribe;
	}
	/**
	 * @return the layoutScreen
	 */
	public String getLayoutScreen() {
		return layoutScreen;
	}
	/**
	 * @param layoutScreen the layoutScreen to set
	 */
	public void setLayoutScreen(String layoutScreen) {
		this.layoutScreen = layoutScreen;
	}
	/**
	 * @return the layoutAddDateFrom
	 */
	public String getLayoutAddDateFrom() {
		return layoutAddDateFrom;
	}
	/**
	 * @param layoutAddDateFrom the layoutAddDateFrom to set
	 */
	public void setLayoutAddDateFrom(String layoutAddDateFrom) {
		this.layoutAddDateFrom = layoutAddDateFrom;
	}
	/**
	 * @return the layoutAddDateTo
	 */
	public String getLayoutAddDateTo() {
		return layoutAddDateTo;
	}
	/**
	 * @param layoutAddDateTo the layoutAddDateTo to set
	 */
	public void setLayoutAddDateTo(String layoutAddDateTo) {
		this.layoutAddDateTo = layoutAddDateTo;
	}
	/**
	 * @return the layoutwidth
	 */
	public int getLayoutwidth() {
		return layoutwidth;
	}
	/**
	 * @param layoutwidth the layoutwidth to set
	 */
	public void setLayoutwidth(int layoutwidth) {
		this.layoutwidth = layoutwidth;
	}
	/**
	 * @return the layoutHeight
	 */
	public int getLayoutHeight() {
		return layoutHeight;
	}
	/**
	 * @param layoutHeight the layoutHeight to set
	 */
	public void setLayoutHeight(int layoutHeight) {
		this.layoutHeight = layoutHeight;
	}
	/**
	 * @return the screenNum
	 */
	public int getScreenNum() {
		return screenNum;
	}
	/**
	 * @param screenNum the screenNum to set
	 */
	public void setScreenNum(int screenNum) {
		this.screenNum = screenNum;
	}
	/**
	 * @return the coordinate
	 */
	public String getCoordinate() {
		return coordinate;
	}
	/**
	 * @param coordinate the coordinate to set
	 */
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	/**
	 * @return the screenStartX
	 */
	public int[] getScreenStartX() {
		return screenStartX;
	}
	/**
	 * @param screenStartX the screenStartX to set
	 */
	public void setScreenStartX(int[] screenStartX) {
		this.screenStartX = screenStartX;
	}
	/**
	 * @return the screenStartY
	 */
	public int[] getScreenStartY() {
		return screenStartY;
	}
	/**
	 * @param screenStartY the screenStartY to set
	 */
	public void setScreenStartY(int[] screenStartY) {
		this.screenStartY = screenStartY;
	}
	/**
	 * @return the screenEndX
	 */
	public int[] getScreenEndX() {
		return screenEndX;
	}
	/**
	 * @param screenEndX the screenEndX to set
	 */
	public void setScreenEndX(int[] screenEndX) {
		this.screenEndX = screenEndX;
	}
	/**
	 * @return the screenEndY
	 */
	public int[] getScreenEndY() {
		return screenEndY;
	}
	/**
	 * @param screenEndY the screenEndY to set
	 */
	public void setScreenEndY(int[] screenEndY) {
		this.screenEndY = screenEndY;
	}
	/**
	 * @return the screenName
	 */
	public String[] getScreenName() {
		return screenName;
	}
	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String[] screenName) {
		this.screenName = screenName;
	}
	/**
	 * @return the windowId
	 */
	public String getWindowId() {
		return windowId;
	}
	/**
	 * @param windowId the windowId to set
	 */
	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}
	/**
	 * @return the programlistId
	 */
	public String getProgramlistId() {
		return programlistId;
	}
	/**
	 * @param programlistId the programlistId to set
	 */
	public void setProgramlistId(String programlistId) {
		this.programlistId = programlistId;
	}
	/**
	 * @return the programlistName
	 */
	public String getProgramlistName() {
		return programlistName;
	}
	/**
	 * @param programlistName the programlistName to set
	 */
	public void setProgramlistName(String programlistName) {
		this.programlistName = programlistName;
	}
	/**
	 * @return the programlistDescribe
	 */
	public String getProgramlistDescribe() {
		return programlistDescribe;
	}
	/**
	 * @param programlistDescribe the programlistDescribe to set
	 */
	public void setProgramlistDescribe(String programlistDescribe) {
		this.programlistDescribe = programlistDescribe;
	}
	/**
	 * @return the programlistPlayTime
	 */
	public int getProgramlistPlayTime() {
		return programlistPlayTime;
	}
	/**
	 * @param programlistPlayTime the programlistPlayTime to set
	 */
	public void setProgramlistPlayTime(int programlistPlayTime) {
		this.programlistPlayTime = programlistPlayTime;
	}
	/**
	 * @return the programlistAddDateFrom
	 */
	public String getProgramlistAddDateFrom() {
		return programlistAddDateFrom;
	}
	/**
	 * @param programlistAddDateFrom the programlistAddDateFrom to set
	 */
	public void setProgramlistAddDateFrom(String programlistAddDateFrom) {
		this.programlistAddDateFrom = programlistAddDateFrom;
	}
	/**
	 * @return the programlistAddDateTo
	 */
	public String getProgramlistAddDateTo() {
		return programlistAddDateTo;
	}
	/**
	 * @param programlistAddDateTo the programlistAddDateTo to set
	 */
	public void setProgramlistAddDateTo(String programlistAddDateTo) {
		this.programlistAddDateTo = programlistAddDateTo;
	}
	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the programlistMaterialNum
	 */
	public int getProgramlistMaterialNum() {
		return programlistMaterialNum;
	}
	/**
	 * @param programlistMaterialNum the programlistMaterialNum to set
	 */
	public void setProgramlistMaterialNum(int programlistMaterialNum) {
		this.programlistMaterialNum = programlistMaterialNum;
	}
	/**
	 * @return the programlistMaterials
	 */
	public List<AdvertBean> getProgramlistMaterials() {
		return programlistMaterials;
	}
	/**
	 * @param programlistMaterials the programlistMaterials to set
	 */
	public void setProgramlistMaterials(List<AdvertBean> programlistMaterials) {
		this.programlistMaterials = programlistMaterials;
	}
	/**
	 * @return the layoutList
	 */
	public List<String> getLayoutList() {
		return layoutList;
	}
	/**
	 * @param layoutList the layoutList to set
	 */
	public void setLayoutList(List<String> layoutList) {
		this.layoutList = layoutList;
	}
	/**
	 * @return the materialPlayTime
	 */
	public int getMaterialPlayTime() {
		return materialPlayTime;
	}
	/**
	 * @param materialPlayTime the materialPlayTime to set
	 */
	public void setMaterialPlayTime(int materialPlayTime) {
		this.materialPlayTime = materialPlayTime;
	}
	/**
	 * @return the materialLevel
	 */
	public int getMaterialLevel() {
		return materialLevel;
	}
	/**
	 * @param materialLevel the materialLevel to set
	 */
	public void setMaterialLevel(int materialLevel) {
		this.materialLevel = materialLevel;
	}
	/**
	 * @return the windowNo
	 */
	public int getWindowNo() {
		return windowNo;
	}
	/**
	 * @param windowNo the windowNo to set
	 */
	public void setWindowNo(int windowNo) {
		this.windowNo = windowNo;
	}
	/**
	 * @return the bill_file_location
	 */
	public String getBill_file_location() {
		return bill_file_location;
	}
	/**
	 * @param bill_file_location the bill_file_location to set
	 */
	public void setBill_file_location(String bill_file_location) {
		this.bill_file_location = bill_file_location;
	}
	/**
	 * @return the machineId
	 */
	public String[] getmachineId() {
		return machineId;
	}
	/**
	 * @param machineId the machineId to set
	 */
	public void setmachineId(String[] machineId) {
		this.machineId = machineId;
	}
	/**
	 * @return the screenType
	 */
	public String getScreenType() {
		return screenType;
	}
	/**
	 * @param screenType the screenType to set
	 */
	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}
	/**
	 * @return the tempAds
	 */
	public String getTempAds() {
		return tempAds;
	}
	/**
	 * @param tempAds the tempAds to set
	 */
	public void setTempAds(String tempAds) {
		this.tempAds = tempAds;
	}
	/**
	 * @return the sendType
	 */
	public String getSendType() {
		return sendType;
	}
	/**
	 * @param sendType the sendType to set
	 */
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	/**
	 * @return the sendTime
	 */
	public String getSendTime() {
		return sendTime;
	}
	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return the valueTime
	 */
	public String getValueTime() {
		return valueTime;
	}
	/**
	 * @param valueTime the valueTime to set
	 */
	public void setValueTime(String valueTime) {
		this.valueTime = valueTime;
	}
	/**
	 * @return the sendTime2
	 */
	public String getSendTime2() {
		return sendTime2;
	}
	/**
	 * @param sendTime2 the sendTime2 to set
	 */
	public void setSendTime2(String sendTime2) {
		this.sendTime2 = sendTime2;
	}
	/**
	 * @return the valueTime2
	 */
	public String getValueTime2() {
		return valueTime2;
	}
	/**
	 * @param valueTime2 the valueTime2 to set
	 */
	public void setValueTime2(String valueTime2) {
		this.valueTime2 = valueTime2;
	}
	/**
	 * @return the controlType
	 */
	public String getControlType() {
		return controlType;
	}
	/**
	 * @param controlType the controlType to set
	 */
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	/**
	 * @return the machingType
	 */
	public String[] getMachingType() {
		return machingType;
	}
	/**
	 * @param machingType the machingType to set
	 */
	public void setMachingType(String[] machingType) {
		this.machingType = machingType;
	}
	/**
	 * @return the programlistGroupId
	 */
	public String getProgramlistGroupId() {
		return programlistGroupId;
	}
	/**
	 * @param programlistGroupId the programlistGroupId to set
	 */
	public void setProgramlistGroupId(String programlistGroupId) {
		this.programlistGroupId = programlistGroupId;
	}
	/**
	 * @return the programlistGroupName
	 */
	public String getProgramlistGroupName() {
		return programlistGroupName;
	}
	/**
	 * @param programlistGroupName the programlistGroupName to set
	 */
	public void setProgramlistGroupName(String programlistGroupName) {
		this.programlistGroupName = programlistGroupName;
	}
	/**
	 * @return the programlistGroup
	 */
	public String[] getProgramlistGroup() {
		return programlistGroup;
	}
	/**
	 * @param programlistGroup the programlistGroup to set
	 */
	public void setProgramlistGroup(String[] programlistGroup) {
		this.programlistGroup = programlistGroup;
	}
	/**
	 * @return the programlistGroup_desc
	 */
	public String getProgramlistGroup_desc() {
		return programlistGroup_desc;
	}
	/**
	 * @param programlistGroup_desc the programlistGroup_desc to set
	 */
	public void setProgramlistGroup_desc(String programlistGroup_desc) {
		this.programlistGroup_desc = programlistGroup_desc;
	}
	/**
	 * @return the programlistGroup_loop
	 */
	public int getProgramlistGroup_loop() {
		return programlistGroup_loop;
	}
	/**
	 * @param programlistGroup_loop the programlistGroup_loop to set
	 */
	public void setProgramlistGroup_loop(int programlistGroup_loop) {
		this.programlistGroup_loop = programlistGroup_loop;
	}
	
	/**
	 * @return the programlistAction
	 */
	public String getProgramlistAction() {
		return programlistAction;
	}
	/**
	 * @param programlistAction the programlistAction to set
	 */
	public void setProgramlistAction(String programlistAction) {
		this.programlistAction = programlistAction;
	}
	/**
	 * @return the programlistActionList
	 */
	public String[] getProgramlistActionList() {
		return programlistActionList;
	}
	/**
	 * @param programlistActionList the programlistActionList to set
	 */
	public void setProgramlistActionList(String[] programlistActionList) {
		this.programlistActionList = programlistActionList;
	}
	/**
	 * @return the subOrgIdArr
	 */
	public String getSubOrgIdArr() {
		return subOrgIdArr;
	}
	/**
	 * @param subOrgIdArr the subOrgIdArr to set
	 */
	public void setSubOrgIdArr(String subOrgIdArr) {
		this.subOrgIdArr = subOrgIdArr;
	}
}
