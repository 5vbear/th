<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="th.taglib.Pager" %>	
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%
	String ctx = request.getContextPath();
	List devList = (List)request.getAttribute("devList");
	Pager pager = (Pager)request.getAttribute("pager");
	HashMap[]  result = pager.getResultData();
	String description="";
	String version="";
	String appName="";
	String dlUrl="";
	String iconUrl="";
	String devOS="";
	String appID="";
	String versionType="";
	if(result!=null&&result.length>0){
		HashMap appInfo = result[0];
		System.out.println(appInfo);
		description=(String)appInfo.get("DESCRIPTION");
		version=(String)appInfo.get("VERSION");
		appName=(String)appInfo.get("APP_NAME");
		dlUrl=(String)appInfo.get("DL_URL");
		iconUrl=(String)appInfo.get("ICON_URL");
		devOS=(String)appInfo.get("DEV_OS");
		appID=(String)appInfo.get("APP_ID");
		versionType=(String)appInfo.get("VERSION_TYPE");
		
	}
	
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
				rootPId : 0,
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
	
		Boolean message = (Boolean)request.getAttribute("message");
		if(message!=null){
			if(message.booleanValue())
				out.print("alert('保存成功!');\n");
			else
				out.print("alert('保存失败!');\n");
		}
	%>
	$(document).ready(function() {
		$('#chooseButton').click(chooseUpdateApp);
		$('#returnButton').click(returnFunction);
	});
	function returnFunction(){
	    $('#updateForm')[0].action=ctx+"/jsp/appStore/AppManagement.html?action=List";
	    $('#updateForm')[0].submit();
	}
	function chooseUpdateApp(){
		
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
		var versionType;
	    $('[name="versionType"]').each(function(){
	        if($(this).attr("checked") == true){
	        	versionType = $(this).val();
	        }
	    })
		
	    $('#updateForm')[0].action=ctx+"/jsp/appStore/AppManagement.html?action=Edit&appName="+$('#appName').val()
	    		+"&description="+$('#description').val()
	    		+"&version="+$('#version').val()
	    		+"&dlUrl="+$('#dlUrl').val()
	    		+"&iconUrl="+$('#iconUrl').val()
	    		+"&appName="+$('#appName').val()
	    		+"&versionType="+versionType
	    		+"&appID="+$('#appID').val()
	    		
	    +"&updatePackageIds="+$('#updatePackageIds').val();
	    //alert($('#updatePackageIds').val());
	    
	    		
	    $('#updateForm')[0].submit();
	}
	function isCheck(){
		 var isCheck = false;
		 $('[name="list"]').each(function(){
	    	if($(this).attr("checked")){
	    		isCheck = true;
	  		}
	  	});
	  	if(!isCheck){
	  		alert('请选择应用包!');
	  	}
	  	return isCheck;
	}
   function upload_zip_onselect(){
       var path;
       path=document.forms[0].upload_zip.value;      
       if(path&&path.length>0){
     	  document.forms[0].dlUrl.disabled=true;
       }else{
     	  document.forms[0].dlUrl.disabled=false;
       }
   }
   function upload_zip2_onselect(){
	   var path;
       path=document.forms[0].upload_zip2.value;     
       if(path&&path.length>0){
     	  document.forms[0].iconUrl.disabled=true;
       }else{
     	  document.forms[0].iconUrl.disabled=false;
       }

   }
   function dlUrl_onselect(){
	   var path;
       path=document.forms[0].dlUrl.value;    
       if(path&&path.length>0){
     	  document.forms[0].upload_zip.disabled=true;
       }else{
     	  document.forms[0].upload_zip.disabled=false;
       }
	}
   function iconUrl_onselect(){
	   var path;
       path=document.forms[0].iconUrl.value;      
       if(path&&path.length>0){
     	  document.forms[0].upload_zip2.disabled=true;
       }else{
     	  document.forms[0].upload_zip2.disabled=false;
       }
	}
</script>
<title>应用程序管理</title>
</head>
<body>
	
	<form class="x-client-form" method="POST" name="form_data" action="" id="updateForm" enctype="multipart/form-data"> 
		<input type="hidden" name="updatePackageIds" id="updatePackageIds" value=""/>
		<input type="hidden" name="appID" id="appID" value="<%=appID%>"/>
		
		
		<div class="x-title"><span>&nbsp;&nbsp;应用程序管理-应用程序添加</span></div>
		<table><tr style ="heigt:30px"></tr></table>
		<div>
			<table class="x-data-table">
				<tr>
				
					<td class="x-data-table-td">软件名称：</td>
					<td class="x-data-table-td"><input type="text" name="appName" id="appName" value="<%=appName%>"></td>
				</tr>
				<tr>
					<td class="x-data-table-td">软件描述：</td>
					<td class="x-data-table-td"><input type="text" name="description" id="description" value="<%=description%>"></td>
				</tr>
				<tr>
					<td class="x-data-table-td">软件版本：</td>
					<td class="x-data-table-td"><input type="text" name="version" id="version" value="<%=version%>"></td>
				</tr>
				
				<tr>
					<td class="x-data-table-td">类别：</td>
					<td class="x-data-table-td" style="border:0;text-align:left">
					
					<% if(versionType.equals("alpha")){%>
						<label><input type="radio" name="versionType" id="versionType" value="alpha" checked/>alpha</label>
					<%}else{ %>
						<label><input type="radio" name="versionType" id="versionType" value="alpha" />alpha</label>
					<%}%>
					<% if(versionType.equals("beta")){%>
						<label><input type="radio" name="versionType" id="versionType" value="beta" checked/>beta</label>
					<%}else{ %>
						<label><input type="radio" name="versionType" id="versionType" value="beta" />beta</label>
					<%}%>
					<% if(versionType.equals("stable")){%>
						<label><input type="radio" name="versionType" id="versionType" value="stable" checked/>stable</label>
					<%}else{ %>
						<label><input type="radio" name="versionType" id="versionType" value="stable" />stable</label>
					<%}%>
					</td>
				</tr>
				
				<tr>
					<td class="x-data-table-td">软件存储地址：</td>
					<td class="x-data-table-td"><input type="text" name="dlUrl" id="dlUrl" value="<%=dlUrl%>" onchange="return dlUrl_onselect()">
					或使用下边的"上传应用"。  输入"下载地址"或"上传应用"。
					</td>
				</tr>
				<tr>
					<td class="x-data-table-td">图标地址：</td>
					<td class="x-data-table-td"><input type="text" name="iconUrl" id="iconUrl" value="<%=iconUrl%>" onchange="return iconUrl_onselect()">
					或使用下边的"上传icon"。  输入"图标地址"或"上传icon"。
					</td>
				</tr>
				
			
				<tr>
					<td class="x-data-table-td">上传软件：</td>
					<td class="x-data-table-td"><input type="file" name="upload_zip" id="upload_zip" onchange="return upload_zip_onselect()"></td>
				</tr>

				
				
				<tr>
					<td class="x-data-table-td">上传图标：</td>
					<td class="x-data-table-td"><input type="file" name="upload_zip2" id="upload_zip2"  onchange="return upload_zip2_onselect()"></td>
				</tr>
			
				
				
				<tr>
				<td class="x-data-table-td">平台：</td><td class="x-data-table-td">
						<%
						HashMap[] resultData = (HashMap[])request.getAttribute("allDevices");//pager.getResultData();
						out.print("			<table class=\"x-data-table\">");
						for(int i=0;i<resultData.length;i++){
							HashMap tmp = resultData[i];
						
							out.print("			<tr>");
							if(tmp.get("DEV_OS").equals(devOS)){
								out.print("<td class='row'><input type=radio checked name=list value='"+tmp.get("DEV_ID")+"' /></td>");
							}else{
								out.print("<td class='row'><input type=radio name=list value='"+tmp.get("DEV_ID")+"' /></td>");
							}
							
							out.print("<td class='row'>&nbsp;"+tmp.get("DEV_OS")+"</td>");
							
							out.print("</tr>");
						}
						out.print("			</table>");
					%>
					</td></tr>
			
			</table>
		</div>
		<div style="float: right">
			<input class="tableBtn" type="button" id="chooseButton" value="保存" />
			<input class="tableBtn" type="button" id="returnButton" value="返回" />
		</div>
	</form>
</body>
</html>