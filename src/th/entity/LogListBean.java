package th.entity;

import th.com.util.Define;

public class LogListBean {
	
	//节目单
	private String bill_name="";
	//端机标识
	private String machine_mark="";
	//布局
	private String layout_name="";
	//素材
	private String media_file_name="";
	//检索时间(开始时间)
	private String search_time_start="";	
	//检索时间(结束时间)
	private String search_time_end="";
	//状态时间
	private String start_play_time="";
	//点击时间
	private String click_time="";
	//操作用户
	private String operation_user="";	
	//操作类型
	private String operation_type="";	
	//操作时间
	private String operation_time="";	
	//操作描述
	private String operation_description="";
	
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
	
	
	public int getTotal_num() {
		return total_num;
	}
	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}
	public int getPoint_num() {
		return point_num;
	}
	public void setPoint_num(int point_num) {
		this.point_num = point_num;
	}
	public int getView_num() {
		return view_num;
	}
	public void setView_num(int view_num) {
		this.view_num = view_num;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public String getBill_name() {
		return bill_name;
	}
	public void setBill_name(String bill_name) {
		this.bill_name = bill_name;
	}
	public String getMachine_mark() {
		return machine_mark;
	}
	public void setMachine_mark(String machine_mark) {
		this.machine_mark = machine_mark;
	}
	public String getSearch_time_start() {
		return search_time_start;
	}
	public void setSearch_time_start(String search_time_start) {
		this.search_time_start = search_time_start;
	}
	public String getSearch_time_end() {
		return search_time_end;
	}
	public void setSearch_time_end(String search_time_end) {
		this.search_time_end = search_time_end;
	}
	public String getLayout_name() {
		return layout_name;
	}
	public void setLayout_name(String layout_name) {
		this.layout_name = layout_name;
	}
	public String getMedia_file_name() {
		return media_file_name;
	}
	public void setMedia_file_name(String media_file_name) {
		this.media_file_name = media_file_name;
	}	
	public String getStart_play_time() {
		return start_play_time;
	}
	public void setStart_play_time(String start_play_time) {
		this.start_play_time = start_play_time;
	}
	public String getClick_time() {
		return click_time;
	}
	public void setClick_time(String click_time) {
		this.click_time = click_time;
	}
	public String getOperation_user() {
		return operation_user;
	}
	public void setOperation_user(String operation_user) {
		this.operation_user = operation_user;
	}
	public String getOperation_type() {
		return operation_type;
	}
	public void setOperation_type(String operation_type) {
		this.operation_type = operation_type;
	}
	public String getOperation_time() {
		return operation_time;
	}
	public void setOperation_time(String operation_time) {
		this.operation_time = operation_time;
	}
	public String getOperation_description() {
		return operation_description;
	}
	public void setOperation_description(String operation_description) {
		this.operation_description = operation_description;
	}


	
}
