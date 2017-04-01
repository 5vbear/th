<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="th.taglib.Pager" %>	
<%@ page import="java.util.HashMap" %>
<%@ page import="th.dao.EBankDeviceDAO" %>
<%
	String ctx = request.getContextPath();
	EBankDeviceDAO	dao	= new EBankDeviceDAO();
	HashMap[] osTypes = dao.getAllEBankDevices();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/channel.css">
<link rel="stylesheet" type="text/css" href="../../css/machine.css" />
<script type="text/javascript">

	var zTreeObj;
	var setting = {
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId",
				rootPId : 0
			}
		},
		view : {
			selectedMulti : false
	
		}
	
	};
	var zNodes = [ { "id": 0, "pId": -1, "name": "中国建设银行", open:true },
	               { "id": 1, "pId": 0, "name": "辽宁省分行", open:true },
	               { "id": 2, "pId": 0, "name": "北京市分行", open:true },
	               { "id": 3, "pId": 0, "name": "吉林省分行", open:true },
	               { "id": 4, "pId": 0, "name": "山东省分行", open:true },
	               { "id": 5, "pId": 1, "name": "沈阳市支行"},
	               { "id": 6, "pId": 1, "name": "朝阳市支行"} ];
	
	
	$(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
	});

	var ctx='<%=ctx%>';
	<%
		Pager pager = (Pager)request.getAttribute("pager");
		out.print("var totalRows = "+pager.getTotalRows()+";\n");
		out.print("var pageSize = "+pager.getPageSize()+";\n");
		out.print("var currentPage = "+pager.getCurrentPage()+";\n");
		out.print("var totalPages = "+pager.getTotalPages()+";\n");
		
		String message = (String)request.getAttribute("message");
		if(message!=null&&!"".equals(message)){
			out.print("alert('"+message+"');\n");
		}
	%>

	
	var formActionValue = ctx+"/jsp/appStore/AppManagement.html";
	//$(document).ready(function() 
	function  onload(){
		initTable();
		$('#addButton').click(function(){
			subbmitForm(formActionValue+"?action=goToAdd");
		});
		$('#deleteButton').click(deleteFunction);
		$('#checkAll').click(function(){
			$('[name="list"]').attr('checked',this.checked);
		});
		$('[name="list"]').click(function(){
			var $tmp = $('[name="list"]');
			$('#checkAll').attr('checked',$tmp.length==$tmp.filter(':checked').length);
		});
		
		//*******Page*********
		initButton();
		$('#btnFirst').click(function (){
			subbmitForm(formActionValue+"?action=PageAction&currPage=1");
		});
		$('#btnPre').click(function (){
			subbmitForm(formActionValue+"?action=PageAction&currPage="+(currentPage-1));
		});
		$('#btnNext').click(function (){
			subbmitForm(formActionValue+"?action=PageAction&currPage="+(currentPage+1));
		});
		$('#btnLast').click(function (){
			subbmitForm(formActionValue+"?action=PageAction&currPage="+totalPages);
		});
		$('#searchBtn').click(searchFunction);
	}
			//);
function searchFunction(){



	var versionType=$('#versionType').val();
	var osType=$('#osType').val();
	var appName=$('#appName').val();

	$('#appManagementList')[0].action=formActionValue+"?action=List&appName="+appName+"&osType="+osType+"&versionType="+versionType;
	$('#appManagementList')[0].submit();
   
}
	
	function deleteFunction(){
		
		if(!isCheck()){
			return;
		}
		var checkValues = "";
	    $('[name="list"]').each(function(){
	        if($(this).attr("checked") == true){
	        	checkValues += $(this).val() + ",";
	        }
	    })
	    $('#appManagementIds').attr("value",checkValues.substring(0,checkValues.length-1));
	    subbmitForm(formActionValue+"?action=Delete");
	}
	function initButton(){
		if(currentPage==1){
			$('#btnFirst')[0].disabled='disabled';
			$('#btnPre')[0].disabled='disabled';
		}
		if(currentPage==totalPages){
			$('#btnLast')[0].disabled='disabled';
			$('#btnNext')[0].disabled='disabled';
		}
	}
	
	function subbmitForm(actionUrl){
	    $('#appManagementList')[0].action=actionUrl;
	    $('#appManagementList')[0].submit();
	}

	function goEditFunction(id){
		subbmitForm(formActionValue+"?action=goToEdit&ids="+id);
	}
	
	function isCheck(){
		 var isCheck = false;
		 $('[name="list"]').each(function(){
	    	if($(this).attr("checked")){
	    		isCheck = true;
	  		}
	  	});
	  	if(!isCheck){
	  		alert('请选择应用程序!');
	  	}
	  	return isCheck;
	}
	
	function btnDeleteClick(id){
		$('#appManagementIds').attr("value",id);
		subbmitForm(formActionValue+"?action=Delete");
	}
	function btnEditClick(id){
		$('#appManagementIds').attr("value",id);
		subbmitForm(formActionValue+"?action=goToEdit");
	}

	
	//*********************************************************************************************
	function initTable(){
		var tablename = $('#dataTableId')[0];
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
</script>
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
			word-break: break-all;
		}
		body,td,th {
			font-family: 宋体, Arial;
			font-size: 12px;
		}
	</style>
<title>选择升级包</title>
</head>
<body onload="onload();">
	<form class="x-client-form" method="POST" name="form_data" action="" id="appManagementList">
		<input type="hidden" name="appManagementIds" id="appManagementIds" value=""/>
		<div>
			<div class="x-data"><span style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;应用商店-应用程序管理</span></div>
		<div>
		<div id="searchFormId" style="background-color:#B2DFEE">
			<table  >
				<tr>
					<!-- <td>全部组织<input type="checkbox" name="allOrg" id="allOrg" value="allOrg" onchange="change()"  /></td> --> 
					<td width="100px" style="border:0;text-align:left">&nbsp;&nbsp;平台类别：</td>
			        <td width="160px" style="border:0;text-align:left"> 
        			    <select id="osType" name="osType">
        			    	<option value="all"></option>
        			    	<%if(osTypes!=null&&osTypes.length>0){ 
        			    		for(int i = 0 ; i <osTypes.length ; i++){
        			    			HashMap osType= osTypes[i];
        			    			out.print("<option value='"+osType.get("DEV_ID")+"'>"+osType.get("DEV_OS")+"</option>");
        			    		} 
        			    	}	
        			    	%>
        			    
				        </select>
			        </td>
			        <td width="100px" style="border:0;text-align:left">&nbsp;&nbsp;软件类别：</td>
			        <td width="120px" style="border:0;text-align:left"> 
			            <select id="versionType" name="versionType">
			                <option value="all" ></option>
				            <option value="alpha" >alpha</option>
				            <option value="beta" >beta</option>
				            <option value="stable" >stable</option>
				        </select>
			        </td>
			        <td width="100px" style="border:0;text-align:left">&nbsp;&nbsp;软件名称：</td>
    			    <td width="160px" style="border:0;text-align:left"> 
			            <input type="text" name="appName" id="appName" ></td>
			        </td>
					<td style="border:0;text-align:left">
						<span>&nbsp;&nbsp;</span><input type="submit" class="x-button"  id="searchBtn"  value="搜索" /> 
					</td>
				</tr>
			</table>
  		</div>
		<table><tr style ="heigt:30px"></tr></table>
			<div>
				<span>&nbsp;&nbsp;数据
			
				</span>
			</div>
			<div>
				<table class="x-data-table" id="dataTableId" width="100%" style="table-layout:fixed;">
					<tr>
						<th style="width:20px"><input type=checkbox
							name="checkAll" id="checkAll" /></th>
						<th class="x-data-table-th" style="width:90px">软件名称</th>
						<th class="x-data-table-th" style="width:90px">软件描述</th>
						<th class="x-data-table-th" style="width:60px">软件包名</th>
						<th class="x-data-table-th" style="width:60px">平台</th>
						<th class="x-data-table-th" style="width:50px">类别</th>
						<th class="x-data-table-th" style="width:60px">软件版本</th>
						<th class="x-data-table-th" style="width:110px">软件存储地址</th>
						<th class="x-data-table-th" style="width:110PX">图标地址</th>
						<th class="x-data-table-th" style="width:100px">操作</th>
					</tr>
					<%
						HashMap[] resultData = pager.getResultData();
						for(int i=0;i<resultData.length;i++){
							HashMap tmp = resultData[i];
							out.print("<tr>");
							out.print("<td class='x-data-table-td'><input type=checkbox name=list value='"+tmp.get("APP_ID")+"' /></td>");
							out.print("<td class='x-data-table-td' title="+tmp.get("APP_NAME")+">"+tmp.get("APP_NAME")+"</td>");
							out.print("<td class='x-data-table-td' title="+tmp.get("DESCRIPTION")+">"+tmp.get("DESCRIPTION")+"</td>");
							out.print("<td class='x-data-table-td' title="+tmp.get("PACKAGE_NAME")+">"+tmp.get("PACKAGE_NAME")+"</td>");
							
							out.print("<td class='x-data-table-td' title="+tmp.get("DEV_OS")+">"+tmp.get("DEV_OS")+"</td>");
							out.print("<td class='x-data-table-td' title="+tmp.get("VERSION_TYPE")+">"+tmp.get("VERSION_TYPE")+"</td>");
							
							out.print("<td class='x-data-table-td' title="+tmp.get("VERSION")+">"+tmp.get("VERSION")+"</td>");
							out.print("<td class='x-data-table-td' title="+tmp.get("DL_URL")+">"+tmp.get("DL_URL")+"</td>");
							out.print("<td class='x-data-table-td' title="+tmp.get("ICON_URL")+">"+tmp.get("ICON_URL")+"</td>");
							out.print("<td class='x-data-table-td'>"
								+"<input class='tableBtn' type='button' value='删除' name='buttonDelete_"+tmp.get("APP_ID")+"' id='btnDel_"+tmp.get("APP_ID")+"' onclick='btnDeleteClick("+tmp.get("APP_ID")+")'/>"
								+"<input class='tableBtn' type='button' value='编辑' name='buttonEdit_"+tmp.get("APP_ID")+"' id='btnEdit_"+tmp.get("APP_ID")+"' onclick='btnEditClick("+tmp.get("APP_ID")+")'/>"
								+"</td>");
							out.print("</tr>");
						}
					%>
					</tr>
				</table>			
				
			</div>
			<div>
				<div style="float: left">
						<input type="button" class="x-first-page" name="button_page_first" id="btnFirst"/>&nbsp; 
						<input type="button" class="x-previous-page" name="button_page_previous" id="btnPre"/>&nbsp; 
						<input type="button" class="x-next-page" name="button_page_next" id="btnNext"/>&nbsp; 
						<input type="button" class="x-last-page" name="button_page_last" id="btnLast"/>&nbsp;
						
						<span>[当前第<%out.print(pager.getCurrentPage());%>页/
						共<%out.print(pager.getTotalPages());%>页]
						[每页<%out.print(pager.getPageSize());%>条]
						[共<%out.print(pager.getTotalRows());%>条]</span>
				</div>
				<div style="float: right">
					<input class="tableBtn" type="button" id="addButton" value="添加" />
					<input class="tableBtn" type="button" id="deleteButton" value="删除" />
				</div>
			</div>
		</div>
	</form>
<!-- 	</fieldset> -->
</body>
</html>