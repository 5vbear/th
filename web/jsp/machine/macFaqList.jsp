<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%> 
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macAlertList.jsp";
    logger.info( jspName + " : start" );
    User user = (User) session.getAttribute("user_info");
    String realname =null;
    if (user == null) {
	    response.setContentType("text/html; charset=utf-8");
	    response.sendRedirect("/th/index.jsp");
    } else {
	    realname = user.getReal_name();
	    logger.info("获得当前用户的用户名，用户名是： " + realname);
    }
    HashMap[] MTmap =  (HashMap[])request.getAttribute( "MTmap" );
    String machineID = StringUtils.transStr((String)request.getAttribute("machineID"));
    logger.info("machineID = " + machineID);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="./css/machine.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="./css/advert.css">
		<link rel="stylesheet" type="text/css" href="./css/channel.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/style.css"/>
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
		<script type="text/javascript">
			function onFold(id) {
				var vDiv = document.getElementById(id);
				vDiv.style.display = (vDiv.style.display == 'none') ? 'block' : 'none';
				var fold = document.getElementById('foldStyleId');
				if (vDiv.style.display === 'none') {
					fold.className = 'x-fold-plus';
				} else {
					fold.className = 'x-fold-minus';
				}
			}
		</script>
		<title>故障知识库</title>
	</head>
	<%!
		private String parseStr(Object obj){
			if(obj != null){									
				return obj.toString();
			}else{
				return "";
			}
		}
	%>
	<style type="text/css">
		body {
			font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica,
				sans-serif;
		}
		
		a {
			color: #c75f3e;
		}
		
		#mytable {
			width: 100%;
			padding: 0;
			margin: 0;
		}
		
		caption {
			padding: 0 0 5px 0;
			width: 700px;
			font: italic 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
			text-align: right;
		}
		
		th {
			font: bold 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
			color: #4f6b72;
			border-right: 1px solid #C1DAD7;
			border-bottom: 1px solid #C1DAD7;
			border-top: 1px solid #C1DAD7;
			letter-spacing: 2px;
			text-transform: uppercase;
			text-align: left;
			padding: 6px 6px 6px 12px;
			background: #CAE8EA no-repeat;
		}
		
		th.nobg {
			border-top: 0;
			border-left: 0;
			border-right: 1px solid #C1DAD7;
			background: none;
		}
		
		td {
			border-right: 1px solid #C1DAD7;
			border-bottom: 1px solid #C1DAD7;
			font-size: 11px;
			padding: 6px 6px 6px 12px;
			color: #4f6b72;
		}
		
		td.alt {
			background: #F5FAFA;
			color: #797268;
		}
		
		th.spec {
			border-left: 1px solid #C1DAD7;
			border-top: 0;
			background: #fff no-repeat;
			font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
		}
		
		th.specalt {
			border-left: 1px solid #C1DAD7;
			border-top: 0;
			background: #f5fafa no-repeat;
			font: bold 10px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
			color: #797268;
		}
		/*---------for IE 5.x bug*/
		html>body td {
			font-size: 11px;
		}
		
		body,td,th {
			font-family: 宋体, Arial;
			font-size: 12px;
		}
	</style>
	<SCRIPT LANGUAGE="JavaScript">
	<!-- Begin
		var checkflag = "false";
		function check(field) {
			if (checkflag == "false") {
				for (i = 0; i < field.length; i++) {
					field[i].checked = true;
				}
				checkflag = "true";
				return "取消";
			} else {
				for (i = 0; i < field.length; i++) {
					field[i].checked = false;
				}
				checkflag = "false";
				return "全选";
			}
		}
		//  End -->
	
		window.onload = function showtable() {
			document.getElementById("faqType").value='${faqType}';
			document.getElementById("machineName").value='${machineName}';
			document.getElementById("machineType").value='${machineType}';
			document.getElementById("SelectOrg").value='${SelectOrg}';
			var tablename = document.getElementById("mytable");
			var li = tablename.getElementsByTagName("tr");
			for ( var i = 0; i < li.length; i++) {	
				if (i % 2 == 0) {
					li[i].style.backgroundColor = "#E5EEFD";
					li[i].onmouseover = function() {
						this.style.backgroundColor = "#CAE8EA";
					}
					li[i].onmouseout = function() {
						this.style.backgroundColor = "#E5EEFD";
					}
				} else {
					li[i].style.backgroundColor = "#FFFFFF";
					li[i].onmouseover = function() {
						this.style.backgroundColor = "#CAE8EA";
					}
					li[i].onmouseout = function() {
						this.style.backgroundColor = "#FFFFFF";
					}
				}
			}
		} 
		
		function faqQry(){
			var faqType = document.getElementById("faqType").value;
			var machineName = document.getElementById("machineName").value;
			var machineType = document.getElementById("machineType").value;
			var SelectOrg = document.getElementById("SelectOrg").value;
			self.location = "/th/machineServlet?model=faq&method=query&pageIdx=1&faqType="+faqType+"&machineName="+machineName+"&machineType="+machineType+"&SelectOrg="+SelectOrg;
		}
		
		function goFirst(){
			self.location = "/th/machineServlet?model=faq&method=query&pageIdx=1&faqType=${faqType}&machineName=${machineName}&machineType=${machineType}&SelectOrg=${SelectOrg}";
		}
		
		function goPrevious(curPageIdx){
			var pageIdx = 1;
			if(curPageIdx>1){
				pageIdx = curPageIdx - 1;
			}
			self.location = "/th/machineServlet?model=faq&method=query&pageIdx="+pageIdx+"&faqType=${faqType}&machineName=${machineName}&machineType=${machineType}&SelectOrg=${SelectOrg}";
		}
		
		function goNext(curPageIdx){
			var pageMaxNum = document.getElementById("pageNum").value;
			var pageIdx = pageMaxNum;
			if(curPageIdx < pageMaxNum){
				pageIdx = curPageIdx + 1;
			}
			self.location = "/th/machineServlet?model=faq&method=query&pageIdx="+pageIdx+"&faqType=${faqType}&machineName=${machineName}&machineType=${machineType}&SelectOrg=${SelectOrg}";
		}
		
		function goLast(){
			var pageMaxNum = document.getElementById("pageNum").value;
			self.location = "/th/machineServlet?model=faq&method=query&pageIdx="+pageMaxNum+"&faqType=${faqType}&machineName=${machineName}&machineType=${machineType}&SelectOrg=${SelectOrg}";
		}
		
		function view(){
			if(!isCheckedOne()){
				alert("请选择一条记录进行查看!");
				return;
			}
			self.location = "/th/machineServlet?model=faq&method=view&pageIdx=${pageIdx}&faqType=${faqType}&machineName=${machineName}&machineType=${machineType}&SelectOrg=${SelectOrg}&fid="+getCheckedValue();
		}
		function update(){
			if(!isCheckedOne()){
				alert("请选择一条记录进行更新!");
				return;
			}
			self.location = "/th/machineServlet?model=faq&method=update&pageIdx=${pageIdx}&faqType=${faqType}&machineName=${machineName}&machineType=${machineType}&SelectOrg=${SelectOrg}&fid="+getCheckedValue();
		}
		
		function isChecked(){
			var checkBoxs = document.getElementsByName("maccbox");
			var flag = false;
			for (var i = 1; i < checkBoxs.length; i++) {
				if(checkBoxs[i].checked){
					flag = true;
					break;
				}
			}
			return flag;
		}
		
		function getCheckedValue(){
			var checkBoxs = document.getElementsByName("maccbox");
			var checkedValue = "";
			for (var i = 1; i < checkBoxs.length; i++) {
				if(checkBoxs[i].checked){
					if(checkedValue == ""){
						checkedValue += checkBoxs[i].value;
					}else{
						checkedValue = checkedValue + "," + checkBoxs[i].value;
					}
				}
			}
			return checkedValue;
		}
		
		function isCheckedOne(){
			var checkBoxs = document.getElementsByName("maccbox");
			var cnt = 0;
			for (var i = 1; i < checkBoxs.length; i++) {
				if(checkBoxs[i].checked){
					cnt++;
				}
			}
			return cnt==1?true:false;
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
	</script>
	<body style="background-color: #fff;">
		<div class="x-title"><span>&nbsp;&nbsp;端机管理-故障知识库</span></div>
		<table><tr style ="height:3px"></tr></table>
		<div class="x-title">
	    	<div id="foldStyleId" class="x-fold-minus" onclick="onFold('foldId');"/>
		</div>
	
		<div style="height:26px;text-align:left;line-height:26px">检索</div>
		<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
	    	<div class="x-chanelName" style="width:100%;padding-left:20px">
		    	<span style="margin-left:20px">所属组织:</span>
		    	
		    	<select name="select" id="SelectOrg" name="SelectOrg" style="width:80px">
			
		    	</select>
		    	<span style="margin-left:20px">机器名:</span>
		    	<input type="text" name="machineName" id="machineName" value="" />
		    	<span style="margin-left:20px">设备类型:</span>
		    	<select name="select" id="machineType" name="machineType" style="width:80px">
			        <option value="0">全部</option>
			        <%if (null != MTmap && MTmap.length != 0){
            	  for (int i = 0; i < MTmap.length; i++) {
            		  String str = MTmap[i].get("OS").toString()+ "_"+ MTmap[i].get("MACHINE_KIND").toString();
            		  String macName = MTmap[i].get("TYPE_NAME").toString();
              %>
              <option value="<%=str %>" ><%=macName %></option>
              <%		
                  }
              }%>


		    	</select>
		    	<span style="margin-left:20px">故障类型:</span>
		    	<select name="select" id="faqType" name="faqType" style="width:80px">
        			<option value=""></option>
			        <option value="1">非法进程</option>
			        <option value="2">非法服务</option>
			        <option value="3">cpu负荷过高</option>
			        <option value="4">内存负荷过高 </option>
			        <option value="5">硬盘容量不足</option>
			        <option value="6">上行速率过慢</option>
			        <option value="7">下行速率过慢</option>
			        <option value="8">非法访问率过高</option>
			        <option value="9">断线报警</option>
		    	</select>
				<input type="button" value="搜索" onclick="faqQry()"/>
		    </div>
		</div>
		<div>
		    <div class="x-data">
		    	<span  style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;数据</span>
		    </div>
		    <div>
				<form name=myform action="" method=post>
					<input type="hidden" id="pageNum" name="pageNum" value="${pageNum}" />
					<input type="hidden" id="ftype" name="ftype" value="${faqType}"/>
					<table id="mytable" cellspacing="0">
						<tr>
							<th scope="col" style="width:2%"><input type=checkbox name=maccbox
								onClick="this.value=check(this.form.maccbox)" value="" /></th>
							<th scope="col" style="width:20%">所属组织</th>
							<th scope="col" style="width:18%">机器名</th>
							<th scope="col" style="width:20%">故障类型</th>
							<th scope="col" style="width:20%">故障描述</th>
							<th scope="col" style="width:20%">设备类型</th>
						</tr>						
					    <%
							HashMap[] faqlist = (HashMap[]) request.getAttribute( "faqlist" );
					    	int pageIdx = Integer.parseInt(request.getAttribute( "pageIdx" ).toString());
					    	if(pageIdx<1){
					    		pageIdx = 1;
					    	}
							for ( int i = 10*(pageIdx-1); i<faqlist.length && i<10*(pageIdx); i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row'>");
								out.print("	   <input type='checkbox' name='maccbox' value='" + faqlist[i].get("FAULT_ID") + "' />");
								out.print("	 </td>");
								out.print("	 <td class='row'>" + parseStr(faqlist[i].get("ORG_NAME")) + "</td>");
								out.print("	 <td class='row'>" + parseStr(faqlist[i].get("MACHINE_MARK")) + "</td>");
								out.print("	 <td class='row'>" + parseStr(faqlist[i].get("HELP_ID")) + "</td>");
								out.print("	 <td class='row'>" + parseStr(faqlist[i].get("QUESTION")) + "</td>");
								out.print("	 <td class='row'>" + parseStr(faqlist[i].get("MACHINE_TYPE")) + "</td>");
								out.print("</tr>");
							}
						%>
					</table>
				</form>
	    	</div>
	  	</div>
		<div>
			<input type="button" class="x-first-page" style="margin-left:5px" onclick="goFirst()" /> 
			<input type="button" class="x-previous-page" style="margin-left:-2px" onclick="goPrevious(${pageIdx})" /> 
			<input type="button" class="x-next-page" style="margin-left:-4px" onclick="goNext(${pageIdx})" />
			<input type="button" class="x-last-page" style="margin-left:-2px" onclick="goLast()" />
			[当前第${pageIdx}页/共${pageNum}页][每页10条][共<%=faqlist.length %>条]
			<input type="button" class="rightBtn" value="查看" onclick="view()" /> 
			<input type="button" class="rightBtn" value="更新" onclick="update()" /> 
		</div>
	</body>
</html>