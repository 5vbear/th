package th.entity;

import th.com.util.Define;

public class MachineServiceBean {
	// 服务名称
	private String name = "";
	// 显示名称
	private String displayName = "";
	// 可执行文件路径
	private String filePath = "";
	// 检索结果总数
	private int total_num = 0;
	// 检索结果现在页
	private int point_num = 0;
	// 检索结果每页显示行数
	private int view_num = Define.VIEW_NUM;;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

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
}
