<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@page import="th.util.StringUtils"%>
<%@page import="th.util.DateUtil"%>
<%@page import="th.com.util.Define4Report"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="copyright" content="Copyright(c) 天和. All Rights Reserved.">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path;
%>
<%
	String macType = (String) request.getAttribute( Define4Report.REQ_PARAM_MAC_TYPE );
	String selectedOrgId = (String) request.getAttribute( Define4Report.REQ_PARAM_SELECTED_ORG_ID );
	String orgs = (String) request.getAttribute( "orgs" );
	String selectOsId = (String) request.getAttribute( "selectOsId" );
	String softwareVersion = (String) request.getAttribute( "softwareVersion" );
	String macName = (String) request.getAttribute( "macName" );
	String cpuFreq = (String) request.getAttribute( "cpuFreq" );
	String diskSize = (String) request.getAttribute( "diskSize" );
	String ramSize = (String) request.getAttribute( "ramSize" );
	HashMap[] MTmap =  (HashMap[])request.getAttribute( "MTmap" );
	
	String pageNo = (String) request.getAttribute( "pageNo" );
	Integer dataCountInteger = (Integer) request.getAttribute( "dataCount" );
	Integer totalPageNumInteger = (Integer) request.getAttribute( "totalPageNum" );
	String firstPageDisabled = Integer.parseInt( pageNo ) < 2 ? "disabled" : "";
	String previousPageDisabled = Integer.parseInt( pageNo ) < 2 ? "disabled" : "";
	String nextPageDisabled = Integer.parseInt( pageNo ) >= totalPageNumInteger ? "disabled": "";
	String lastPageDisabled = Integer.parseInt( pageNo ) >= totalPageNumInteger ? "disabled": "";
%>
<%
	selectOsId = StringUtils.isBlank( selectOsId ) ? "" : selectOsId;
	selectedOrgId = StringUtils.isBlank( selectedOrgId ) ? "" : selectedOrgId;
	macType = StringUtils.isBlank( macType ) ? "0" : macType;
	softwareVersion = StringUtils.isBlank( softwareVersion ) ? "" : softwareVersion;
	macName = StringUtils.isBlank( macName ) ? "" : macName;
	cpuFreq = StringUtils.isBlank( cpuFreq ) ? "" : cpuFreq;
	diskSize = StringUtils.isBlank( diskSize ) ? "" : diskSize;
	ramSize = StringUtils.isBlank( ramSize ) ? "" : ramSize;
%>
	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/css/channel.css" />
	<link rel="stylesheet" href="../../zTree/css/demo.css" type="text/css">
		<link rel="stylesheet"
			href="../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
			<script type="text/javascript"
				src="../../zTree/js/jquery-1.4.4.min.js"></script>
			<script type="text/javascript"
				src="../../zTree/js/jquery.ztree.core-3.5.js"></script>
			<script type="text/javascript"
				src="../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
			<%@ page import="th.entity.ClientBean"%>
			<%@ page import="th.entity.OrganizationBean"%>
			<%@ page import="th.entity.MachineBean"%>
			<%@ page import="java.util.*"%>
			<%@ page import="th.com.util.Define"%>
			<%@ page import="th.user.*"%>
			<%@ page import="org.apache.commons.logging.Log"%>
			<%@ page import="org.apache.commons.logging.LogFactory"%>	
			<%
				Log logger = LogFactory.getLog(this.getClass());
				User user = (User) session.getAttribute("user_info");
				String realname =null;
				if (user == null) {
				     response.setContentType("text/html; charset=utf-8");
				     response.sendRedirect("/th/index.jsp");
				} else {
				    realname = user.getReal_name();
				    logger.info("获得当前用户的用户名，用户名是： " + realname);
				}
			%>
			<script type="text/javascript">
window.onload = function showtable() {
    var tablename = document.getElementById("dataTableId");
    var li = tablename.getElementsByTagName("tr");
    for (var i = 0; i < li.length; i++) {
        if (i % 2 == 0) {
            li[i].style.backgroundColor = "#E5EEFD";
            li[i].onmouseover = function () {
                this.style.backgroundColor = "#CAE8EA";
            }
            li[i].onmouseout = function () {
                this.style.backgroundColor = "#E5EEFD";
            }
        }
        else {
            li[i].style.backgroundColor = "#FFFFFF";
            li[i].onmouseover = function () {
                this.style.backgroundColor = "#CAE8EA";
            }
            li[i].onmouseout = function () {
                this.style.backgroundColor = "#FFFFFF";
            }
        }
    }
}
	function onFold(id) {
		var vDiv = document.getElementById(id);
		vDiv.style.display = (vDiv.style.display == 'none') ? 'block' : 'none';
		var fold = document.getElementById('clientStyleId');
		if (vDiv.style.display === 'none') {
			fold.className = 'x-fold-plus';
		} else {
			fold.className = 'x-fold-minus';
		}
	}
	function searchClientInfo(){
		var form = document.getElementById('clientInfoForm');
		form.action = '/th/jsp/client/clientInfo.html';
		document.getElementById('searchInfoId').value = 'search';
		form.submit();
	}
	function checkAll(){
		document.getElementById('selectedAllId').value = '1';
	}
	function gotoPage(pageType) {
        document.getElementById('searchInfoId').value = "page";
        if (pageType == '') {
            pageType = document.getElementById('pageNoId').value;
        }
        document.getElementById('pageTypeId').value = pageType;
        var form = document.getElementById('clientInfoForm');
        form.action = "/th/jsp/client/clientInfo.html";
        form.submit();
    }
	//组织结构下拉菜单
	var setting = {
	            view: {  
	                dblClickExpand: false  
	            },  
	            data: {  
	                simpleData: {  
	                    enable: true  
	                }  
	            },  
	            callback: {  
	                //beforeClick: beforeClick,//(点击之前)用于捕获 勾选 或 取消勾选 之前的事件回调函数，并且根据返回值确定是否允许 勾选 或 取消勾选   
	                onClick: onClick  
	            }  
	};  
	var zNodes = <%=(String) request.getAttribute( "MONITOR_ORG_LIST" )%> ;

    $(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		
		var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
		var v = "", w = "";
		// 设置组织名称下拉列表值 
		var orgList = document.getElementById("SelectOrg");
		for (var i=0; i<nodes.length; i++) {
			w = "";
			if(i<nodes.length-1){
				for(var j=0; j<(nodes[i].level-nodes[0].level); j++){
					w += "├─";					
				}
			}else{
				for(var j=0; j<(nodes[i].level-nodes[0].level); j++){
					w += "└─";
				}
			}
			// 为select下拉菜单动态赋予option值 
			orgList.options.add(new Option(w+nodes[i].name,nodes[i].id));
			// 组织List信息拼接  
			v += w + nodes[i].name + "_" + nodes[i].id;
			if(i<nodes.length-1){
				v += ",";
			}
		}
	});
    function onClick(e, treeId, treeNode) {  
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),  
        nodes = zTree.getSelectedNodes(),//获取 zTree 当前被选中的节点数据集合（按Ctrl多选择）  
        v = "";
        var nid = "";
        nodes.sort(function compare(a,b){return a.id-b.id;});//按照id从小到大进行排序  
        for (var i=0, l=nodes.length; i<l; i++) {  
            v += nodes[i].name + ",";  
            nid = nodes[i].id;
        }  
        if (v.length > 0 ) v = v.substring(0, v.length-1);  
        var cityObj = $("#citySel");  
        cityObj.attr("value", v);//设置文本框的值  
        
        //选中一个节点后关闭div
        hideMenu();
        //alert(nid);
        window.document.form_data.orderType.value ="2";
    	window.document.form_data.pageID.value ="<%=Define.JSP_MONITOR_CLIENT_RUNNING_ID%>";
    	window.document.form_data.orgID.value = nid;
        window.document.form_data.action = "<%=Define.MONITOR_SERVLET%>";
        window.document.form_data.target = "_self";
        window.document.form_data.submit();
    }  
    function exportReport(){
  		document.getElementById('searchInfoId').value = "report";
  		var form = document.getElementById('clientInfoForm');
  		form.action = "/th/jsp/client/clientInfo.html";
        form.submit();
  	} 
    function init(){
        var selectedOrgId='<%=selectedOrgId%>';
     	if(selectedOrgId&&selectedOrgId!=''){
     		var orgSelect = document.getElementById("SelectOrg");
     		for (var i = 0; i < orgSelect.options.length; i++) {        
     	        if (orgSelect.options[i].value == selectedOrgId) {        
     	        	orgSelect.options[i].selected = true;        
     	            break;        
     	        }     
     		}
     	}         	
    }
</script>
</head>
<body onload="init()">
	<div class="x-title">
		<span>&nbsp;&nbsp;报表管理-端机信息统计</span>
	</div>
		<table><tr style ="heigt:30px"></tr></table>
		<form class="x-client-form" name="form_data" id="clientInfoForm">
			<input type="hidden" name="type" id="searchInfoId" /> <input
				type="hidden" name="isSelectedAll" id="selectedAllId" value="0" />
			<input type="hidden" name="pageType" id="pageTypeId" />
			<div class="x-title">
				<div id="clientStyleId" class="x-fold-minus"
					onclick="onFold('clientId');" />
			</div>
			<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
			</div>
			<div id="clientId" style="background-color: #B2DFEE">
				<div>
					<table width="100%">
						<tr>
							<td width="33%">组&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;织&nbsp;:&nbsp;&nbsp;<select id="SelectOrg" name="SelectOrg" style="border: 1px solid #737C73;width: 167px;"></select>
							</td>
							<td width="33%"><span>操作系统:</span> <select name="selectOsId" id="selectOsId" style="border: 1px solid #737C73;width: 167px;">
									<option value="-">不限</option>
									<%
										List osList = (List) request.getAttribute( "osList" );
										for ( int i = 0; i < osList.size(); i++ ) {
									%>
									<option value="<%=osList.get( i )%>" <%if(selectOsId.equals(osList.get( i ))){ %>selected="selected"<%}%>><%=osList.get( i )%></option>
									<%
										}
									%>
							</select></td>
							<td width="33%"><span>软件版本:</span> <input name="softwareVersion" id="softwareVersion" type="text" size="22" value="<%=softwareVersion %>"/></td>
						</tr>
						<tr>
							<td width="33%">CPU速度:&nbsp;<input name="cpuFreq" id="cpuFreq" type="text" size="22" value="<%=cpuFreq %>" /></td>
							<td width="33%"><span>硬盘容量:</span> <input name="diskSize" id="diskSize" type="text" size="22" value="<%=diskSize %>" /></td>
							<td width="33%"><span>内存容量:</span> <input name="ramSize" id="ramSize" type="text" size="22" value="<%=ramSize %>" /></td>
						</tr>
						<tr>
						<td width="33%"><span>设备名称:</span> <input name="macName" id="macName" type="text" size="22" value="<%=macName %>"/></td>
						<td width="33%" style="border:0;text-align:left">设备类型:&nbsp;
						     <select id="macType" name="macType" style="border: 1px solid #737C73;width: 167px;">
						           <option value='0' <%if("0".equals(macType)){ %>selected="selected"<%}%>>全部</option>
						           <%if (null != MTmap && MTmap.length != 0){
						        	   for (int i = 0; i < MTmap.length; i++) {
						        		   String str = MTmap[i].get("OS").toString()+ "_"+ MTmap[i].get("MACHINE_KIND").toString();
						        		   String macTypeName = MTmap[i].get("TYPE_NAME").toString();
							        	   %>
						           <option value="<%=str %>" <%if(str.equals(macType)){ %>selected="selected"<%}%>><%=macTypeName %></option>
						           <%		
						           	}
							      }%>
						     </select>
						</td>
						<td width="33%"><input type="button" value="检索" onClick="searchClientInfo();"  class="x-button"/></td>
						</tr>
					</table>
				</div>
			</div>
			<div>
				<div class="x-data">
					<span style="height: 30px; width: 100px; line-height: 30px">&nbsp;&nbsp;端机信息</span>
				</div>
				<div>
					<table class="x-data-table" id="dataTableId">
						<tr>
							<th style="width: 14%" class="x-data-table-th">设备名称</th>
							<th style="width: 14%" class="x-data-table-th">设备类型</th>
							<th style="width: 14%" class="x-data-table-th">CPU速度</th>
							<th style="width: 14%" class="x-data-table-th">操作系统</th>
							<th style="width: 14%" class="x-data-table-th">硬盘容量</th>
							<th style="width: 14%" class="x-data-table-th">内存容量</th>
							<th style="width: 14%" class="x-data-table-th">总版本号</th>
						</tr>
						<%
							List list = (List) request.getAttribute( "list" );
							String exportDisabled = "";
							if(list.size() == 0){
								exportDisabled = "disabled";
							}else{
								exportDisabled = "";
							}
							
							int dataCount = dataCountInteger.intValue();
							if ( list.size() != 0 ) {
								if ( list.size() < 10 ) {
									for ( int i = 0; i < list.size(); i++ ) {
										ClientBean clientBean = (ClientBean) list.get( i );
						%>
						<tr>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getMachineName()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getMacType()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getCpuFrequency()%>
								MHz
							</td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getOs()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getDiskMemory()%>
								GB
							</td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getRamMemory()%>
								KB
							</td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getVersion()%></td>
						</tr>
						<%
							}
								}
								else {
									for ( int i = 0; i < 10; i++ ) {
										ClientBean clientBean = (ClientBean) list.get( i );
						%>
						<tr>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getMachineName()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getMacType()%></td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getCpuFrequency()%>
								MHz
							</td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getOs()%>
							</td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getDiskMemory()%>
								GB
							</td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getRamMemory()%>
								KB
							</td>
							<td class="x-data-table-td">&nbsp;&nbsp;<%=clientBean.getVersion()%></td>
						</tr>
						<%
							}
								}
							}
						%>
					</table>
				</div>
				<div>
					<input type="button" <%=firstPageDisabled%> class="x-first-page" onClick="gotoPage('first')" />&nbsp; 
					<input type="button" <%=previousPageDisabled%> class="x-previous-page" onClick="gotoPage('previous')" />&nbsp; 
					<input type="button" <%=nextPageDisabled%> class="x-next-page" onClick="gotoPage('next')" />&nbsp;
					<input type="button" <%=lastPageDisabled%> class="x-last-page" onClick="gotoPage('last')" />&nbsp;[当前第<%=pageNo%>页/共<%=totalPageNumInteger%>页]
					<div style="display: none">
						<label>第 <input type="text" size="1" name="pageNoName" id="pageNoId" value="<%=pageNo%>" />页</label> 
						<input type="button" class="x-goto-page" onClick="gotoPage('')" /><span>共<%=dataCount%>条数据</span>
					</div>
					<div style="float:right">
						<input type="button" <%=exportDisabled%> id="exportButton" class="x-button" value="导出" onclick="exportReport()" />
					</div>
				</div>
			</div>
		</form>
</body>
</html>
