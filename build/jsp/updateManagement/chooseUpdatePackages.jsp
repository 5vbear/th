<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="th.taglib.Pager" %>	
<%@ page import="java.util.HashMap" %>
<%
	String ctx = request.getContextPath();
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

	var ctx='<%=ctx%>';
	<%
		Pager pager = (Pager)request.getAttribute("pager");
		out.print("var totalRows = "+pager.getTotalRows()+";");
		out.print("var pageSize = "+pager.getPageSize()+";");
		out.print("var currentPage = "+pager.getCurrentPage()+";");
		out.print("var totalPages = "+pager.getTotalPages()+";");
	%>	
	var formActionValue = ctx+"/jsp/updateManagement/ChoosePackages.html";
	$(document).ready(function() {
		initTable();
		$('#chooseButton').click(chooseUpdatePackages);
		$('#returnButton').click(returnFunction);
		$('#refreshButton').click(refreshFunction);
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
	});
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
	    $('#ChoosePackages')[0].action=actionUrl;
	    $('#ChoosePackages')[0].submit();
	}
	function returnFunction(){
		subbmitForm(ctx+"/jsp/updateManagement/UpdateManagement.html?action=backToSendUpdateOrder");
	}
	function refreshFunction(){
	    window.location.reload();
	}

	function chooseUpdatePackages(){
		if(!isCheck()){
			return;
		}
		var checkValues = "";
	    $('[name="list"]').each(function(){
	        if($(this).attr("checked") == true){
	        	checkValues += $(this).val() + ",";
	        }
	    })
	    $('#updatePackageIds').attr("value",checkValues.substring(0,checkValues.length-1));
	    subbmitForm(formActionValue+"?action=submitChoose");
	}
	
	function isCheck(){
		 var isCheck = false;
		 $('[name="list"]').each(function(){
	    	if($(this).attr("checked")){
	    		isCheck = true;
	  		}
	  	});
	  	if(!isCheck){
	  		alert('请选择升级包!');
	  	}
	  	return isCheck;
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
<title>选择升级包</title>
</head>
<body>
	<form class="x-client-form" method="POST" name="form_data" action="" id="ChoosePackages">
		<input type="hidden" name="updatePackageIds" id="updatePackageIds" value=""/>
		<div>
			<div class="x-data"><span style="height:30px;width:100px;line-height:30px">&nbsp;&nbsp;升级管理-升级包选择</span></div>
			<table><tr style ="heigt:30px"></tr></table>
			<div>  				
  				<span>&nbsp;&nbsp;数据
  				
  							<!--
  							升级包管理表
							[当前第<%out.print(pager.getCurrentPage());%>页/
							共<%out.print(pager.getTotalPages());%>页][排序方式:/排序类型:]
							[每页<%out.print(pager.getPageSize());%>条]
							[共<%out.print(pager.getTotalRows());%>条]
							-->
				</span>
			</div>
			<div>
			
				<table class="x-data-table" id="dataTableId" width="100%">
					<tr>
						<th class="x-data-table-td" style="width:2%"><input type=checkbox
							name="checkAll" id="checkAll" /></th>
						<th class="x-data-table-th" style="width:10%">包名</th>
						<th class="x-data-table-th" style="width:10%">创建时间</th>
						<th class="x-data-table-th" style="width:10%">说明</th>
					</tr>
					<%
						HashMap[] resultData = pager.getResultData();
						for(int i=0;i<resultData.length;i++){
							HashMap tmp = resultData[i];
							out.print("<tr>");
							out.print("<td class='x-data-table-td'><input type=checkbox name=list value='"+tmp.get("FILE_ID")+"' /></td>");
							out.print("<td class='x-data-table-td'>&nbsp;"+tmp.get("FILE_NAME")+"</td>");
							out.print("<td class='x-data-table-td'>&nbsp;"+tmp.get("OPERATETIME")+"</td>");
							out.print("<td class='x-data-table-td'>&nbsp;"+tmp.get("DESCRIPTION")+"</td>");
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
					<input class="tableBtn" type="button" id="chooseButton" class="rightBtn" value="选择" />
					<input class="tableBtn" type="button" id="returnButton" class="rightBtn" value="返回" />
					<input class="tableBtn" type="button" id="refreshButton" class="rightBtn" value="刷新" />				
				</div>
			</div>
		</div>
	</form>
</body>
</html>