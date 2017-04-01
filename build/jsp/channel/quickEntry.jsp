<%@ page contentType="text/html; charset=utf-8" language="java"
	pageEncoding="UTF-8" import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="copyright" content="Copyright(c) 天和. All Rights Reserved.">
	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/css/channel.css" />
	<link rel="stylesheet" href="../../zTree/css/demo.css" type="text/css">
	<link rel="stylesheet" href="../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="../../zTree/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="../../zTree/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="../../zTree/js/jquery.ztree.excheck-3.5.js"></script>		
	<%@ page import="th.entity.ChannelBean"%>
	<%@ page import="java.util.*"%>
	<%@ page import="th.user.*"%>
	<%@ page import="org.apache.commons.logging.Log"%>
	<%@ page import="org.apache.commons.logging.LogFactory"%>
	<%
		Log logger = LogFactory.getLog( this.getClass() );
		User user = (User) session.getAttribute( "user_info" );
		String userId = user.getId();
		String realname = null;
		if ( user == null ) {
			response.setContentType( "text/html; charset=utf-8" );
			response.sendRedirect( "/th/index.jsp" );
		}
		else {
			realname = user.getReal_name();
			logger.info( "获得当前用户的用户名，用户名是： " + realname );
		}
	%>
	<script type="text/javascript">
                    function onFold(id) {
                        var vDiv = document.getElementById(id);
                        vDiv.style.display = (vDiv.style.display == 'none') ? 'block' : 'none';
                        var fold = document.getElementById('clientStyleId');
                        if (vDiv.style.display === 'none') {
                            fold.className = 'x-fold-plus';
//                             document.getElementById('hiddenId').style.height = "30px";
                        }
                        else {
                            fold.className = 'x-fold-minus';
                            document.getElementById('hiddenId').style.height = "0px";
                        }
                    }
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
                        var isEnabled = '<%=request.getAttribute("isEnabled")%>';
                        if(isEnabled != 'null'){
                     	   document.getElementById('isEnabledId').value = isEnabled;
                        }
                        var channelName = '<%=request.getAttribute("channelName")%>';
                        if(channelName != 'null'){
                     	   document.getElementById('channelNameId').value = channelName;
                        }
                    }

                    function check(field) {
                        var allCheckbox = document.getElementsByName('channelListCheckboxName');
                        if (field.value = '1') {
                            if (field.checked) {
                                for (var i = 0; i < allCheckbox.length; i++) {
                                    allCheckbox[i].checked = true;
                                }
                            }
                            else {
                                for (var i = 0; i < allCheckbox.length; i++) {
                                    allCheckbox[i].checked = false;
                                }
                            }
                        }
                        else {
                            if (field.checked) {
                                field.checked = false;
                            }
                            else {
                                field.checked = true;
                            }
                        }
                    }

                    function searchChannel() {
                        var form = document.getElementById('channelForm');
                        document.getElementById('searchType').value = "search";
                        form.action = "/th/jsp/channel/searchChannel.html";
                        form.submit();
                    }

                    function stopChannel() {
                        var allChannelArray = "'";
                        var allCheckbox = document.getElementsByName('channelListCheckboxName');
                        for (var i = 1; i < allCheckbox.length; i++) {
                            if (allCheckbox[i].checked == true) {
                                allChannelArray = allChannelArray + allCheckbox[i].value + "','";
                                var channelId = 'channelIsEnabledId' + allCheckbox[i].value;
                                var status = document.getElementById(channelId)
                                    .innerHTML;
                                status = status.replace("&nbsp;&nbsp;","");
                                if (status == '停用') {
                                    alert('选择的记录中包含已停用的记录!');
                                    return;
                                }
                            }
                        }
                        if (allChannelArray.length == 1) {
                            alert('请至少选择一条记录');
                            return;
                        }
                        document.getElementById('searchType').value = "stop";
                        document.getElementById('allSelectChannelIds').value = allChannelArray;
                        var form = document.getElementById('channelForm');
                        form.action = "/th/jsp/channel/searchChannel.html";
                        form.submit();
                    }

                    function openChannel() {
                        var allChannelArray = "'";
                        var allCheckbox = document.getElementsByName('channelListCheckboxName');
                        for (var i = 1; i < allCheckbox.length; i++) {
                            if (allCheckbox[i].checked == true) {
                                allChannelArray = allChannelArray + allCheckbox[i].value + "','";
                                var channelId = 'channelIsEnabledId' + allCheckbox[i].value;
                                var status = document.getElementById(channelId)
                                    .innerHTML;
                                status = status.replace("&nbsp;&nbsp;", ""); 
                                if (status == '启用') {
                                    alert('选择的记录中包含已启用的记录!');
                                    return;
                                }
                            }
                        }
                        if (allChannelArray.length == 1) {
                            alert('请至少选择一条记录');
                            return;
                        }
                        document.getElementById('searchType').value = "open";
                        document.getElementById('allSelectChannelIds').value = allChannelArray;
                        var form = document.getElementById('channelForm');
                        form.action = "/th/jsp/channel/searchChannel.html";
                        form.submit();
                    }

                    function gotoPage(pageType) {
                        document.getElementById('searchType').value = "page";
                        if (pageType == '') {
                            pageType = document.getElementById('pageNoId').value;
                        }
                        document.getElementById('pageTypeId').value = pageType;
                        var form = document.getElementById('channelForm');
                        form.action = "/th/jsp/channel/searchChannel.html";
                        form.submit();
                    }

                    function editChannel() {
                        var allChannelArray = new Array();
                        var allCheckbox = document.getElementsByName('channelListCheckboxName');
                        for (var i = 1; i < allCheckbox.length; i++) {
                            if (allCheckbox[i].checked == true) {
                                allChannelArray.push(allCheckbox[i].value);
                            }
                        }
                        if (allChannelArray.length != 1) {
                            alert('请选择一条记录进行修改!');
                            return;
                        }
                        document.getElementById('searchType').value = "edit";
                        document.getElementById('editChannelId').value = allChannelArray[0];
                        var form = document.getElementById('channelForm');
                        form.action = "/th/jsp/channel/editChannel.html";
                        form.submit();
                    }
                    
                    function deleteChannel() {
                    	var allChannelArray = new Array();
                        var allCheckbox = document.getElementsByName('channelListCheckboxName');
                        for (var i = 1; i < allCheckbox.length; i++) {
                            if (allCheckbox[i].checked == true) {
                                allChannelArray.push(allCheckbox[i].value);
                            }
                        }
                        if(allChannelArray.length === 0){
                        	alert('请选择记录进行删除!');
                        	return;
                        }
                        document.getElementById('searchType').value = "delete";
                        document.getElementById('deleteChannelIds').value = allChannelArray;
                        var form = document.getElementById('channelForm');
                        form.action = "/th/jsp/channel/deleteChannel.html";
                        form.submit();
                    }
                    function addChannel(){
                    	document.getElementById('searchType').value = "addQuickEntry";
                    	var form = document.getElementById('channelForm');
                        form.action = "/th/jsp/channel/addQuickEntry.html";
                        form.submit();
                    }
                  	function exportReport(){
                  		document.getElementById('searchType').value = "report";
                  		var form = document.getElementById('channelForm');
                        form.action = "/th/jsp/channel/searchChannel.html";
                        form.submit();
                  	} 	
                </script>
	<title></title>
</head>

<body>
	<div class="x-title">
		<span>&nbsp;&nbsp;系统管理-快速入口管理</span>
	</div>
	<table><tr style ="heigt:30px"></tr></table>
	<form class="x-client-form" id="channelForm" action="">
		<input type="hidden" name="type" id="searchType" /> 
		<input type="hidden" name="deleteChannelName" id="deleteChannelIds" /> 
		<input type="hidden" name="allChannelIds" id="allSelectChannelIds" /> 
		<input type="hidden" name="pageType" id="pageTypeId" /> 
		<input type="hidden" name="editChannelName" id="editChannelId" />
		<input type="hidden" name="channelType" value="1" />
		<div class="x-title">
			<div id="clientStyleId" class="x-fold-minus" onclick="onFold('foldId');" />
		</div>
		<div style="height: 26px; text-align: left; line-height: 26px">检索</div>
		<div id="foldId"
			style="width: 100%; display: block; height: 30px; line-height: 30px; background-color: #B2DFEE">
			<div class="x-chanelName">
				<span>&nbsp;&nbsp;快速入口名称</span> <input type="text" name="channelName" id="channelNameId"/>
			</div>
			<div>
				<span>是否启用</span> <select name="isEnabled" id="isEnabledId">
					<option value="0">&nbsp;-&nbsp;请选择&nbsp;-&nbsp;</option>
					<option value="1">&nbsp;-&nbsp;启用&nbsp;-&nbsp;</option>
					<option value="2">&nbsp;-&nbsp;停用&nbsp;-&nbsp;</option>
				</select> &nbsp;&nbsp;<input type="button" value="搜索" onclick="searchChannel();"
					class="x-button" />
			</div>
		</div>
		<div id="hiddenId" style="height: 0px;">&nbsp;</div>
		<%
			List list = (List) request.getAttribute( "list" );
			String exportDisabled = "";
			if(list.size() == 0){
				exportDisabled = "style='display:none'";
			}else{
				exportDisabled = "";
			}
		%>
		<table class="x-data-table" id="dataTableId">
			<tr>
				<td colspan="5" class="x-data-table-td" style="text-align: left">&nbsp;&nbsp;快速入口定义表</td>
			</tr>
			<tr>
				<th style="width: 2%" class="x-data-table-td"><input
					type="checkbox" name="channelListCheckboxName"
					onClick="check(this);" value="1" id="allCheckBoxId" /></th>
				<th style="width: 15%" class="x-data-table-th">快速入口名称</th>
				<th style="width: 30%" class="x-data-table-th">快速入口路径</th>
				<th style="width: 10%" class="x-data-table-th">是否使用</th>
			</tr>
			<%
				String pageNo = (String) request.getAttribute( "pageNo" );
				Integer dataCountInteger = (Integer) request.getAttribute( "dataCount" );
				int dataCount = dataCountInteger.intValue();
				if ( list.size() >= 10 ) {
					for ( int i = 0; i < 10; i++ ) {
						ChannelBean channelBean = (ChannelBean) list.get( i );
			%>
			<tr>
				<td style="width: 2%" class="x-data-table-td"><input
					type="checkbox" name="channelListCheckboxName"
					value="<%=channelBean.getChannelId()%>" /></td>
				<td class="x-data-table-td">&nbsp;&nbsp;<%=channelBean.getChannelName()%>
				</td>
				<td class="x-data-table-td">&nbsp;&nbsp;<%=channelBean.getChannelPath()%>
				</td>
				<%
					if ( "1".equals( channelBean.getStatus() ) ) {
				%>
				<td class="x-data-table-td"
					id="channelIsEnabledId<%=channelBean.getChannelId()%>">&nbsp;&nbsp;启用</td>
				<%
					}
							else if ( "2".equals( channelBean.getStatus() ) ) {
				%>
				<td class="x-data-table-td"
					id="channelIsEnabledId<%=channelBean.getChannelId()%>">&nbsp;&nbsp;停用</td>
				<%
					}
				%>
			</tr>
			<%
				}
				}
				else {
					for ( int i = 0; i < list.size(); i++ ) {
						ChannelBean channelBean = (ChannelBean) list.get( i );
			%>
			<tr>
				<td style="width: 2%" class="x-data-table-td"><input
					type="checkbox" name="channelListCheckboxName"
					value="<%=channelBean.getChannelId()%>" /></td>
				<td class="x-data-table-td"><%=channelBean.getChannelName()%></td>
				<td class="x-data-table-td"><%=channelBean.getChannelPath()%></td>
				<%
					if ( "1".equals( channelBean.getStatus() ) ) {
				%>
				<td class="x-data-table-td"
					id="channelIsEnabledId<%=channelBean.getChannelId()%>">&nbsp;&nbsp;启用</td>
				<%
					}
							else if ( "2".equals( channelBean.getStatus() ) ) {
				%>
				<td class="x-data-table-td"
					id="channelIsEnabledId<%=channelBean.getChannelId()%>">&nbsp;&nbsp;停用</td>
				<%
					}
						}
					}
				%>
			</tr>
		</table>
		<div>
			<div style="float: left">
				<input type="button" class="x-first-page"
					onClick="gotoPage('first')" />&nbsp; <input type="button"
					class="x-previous-page" onClick="gotoPage('previous')" />&nbsp; <input
					type="button" class="x-next-page" onClick="gotoPage('next')" />&nbsp;
				<input type="button" class="x-last-page" onClick="gotoPage('last')" />&nbsp;
				<div style="display: none">
					<label>第 <input type="text" size="1" name="pageNoName"
						id="pageNoId" value="<%=pageNo%>" />页
					</label> <input type="button" class="x-goto-page" onClick="gotoPage('')" />
					<span>共<%=dataCount%>条数据
					</span>
				</div>
			</div>
			<%
				if ( "0".equals( userId ) ) {
			%>
			<div style="float: right">
				<input type="button" <%=exportDisabled%> id="exportButton" class="x-button" value="导出" onclick="exportReport()" />
				<input type="button" value="添加" onClick="addChannel();" class="x-button" /> 
				<input type="button" value="启用" onClick="openChannel();" class="x-button" /> 
				<input type="button" value="停用" onClick="stopChannel();" class="x-button" /> 
				<input type="button" value="编辑" onClick="editChannel();" class="x-button" />
				<input type="button" value="删除" onClick="deleteChannel();" class="x-button" />
			</div>
			<%
				}
			%>
		</div>
	</form>
</body>
</html>
