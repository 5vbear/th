<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String result = (String)request.getAttribute("result");
	String ctx = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>系统升级管理-IOS系统升级</title>
<link rel="stylesheet" href="../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../css/channel.css" type="text/css">
<link rel="stylesheet" href="../../css/machine.css" type="text/css"/>
<link rel="stylesheet" href="../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="../../css/updateManagement.css" type="text/css">
<script type="text/javascript" src="../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="../../js/Calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript">
//**************


	var ctx='<%=ctx%>';

	var result = '<%= result %>';
	$(document).ready(function() {
		$('#chooseButton').click(chooseUpdateApp);
		if(result&&result=='false'){
			alert('上传失败，请联系管理员。');
		}else if(result&&result=='true'){
			alert('上传成功。');
		}
	});
	function chooseUpdateApp(){
		
		if(!checkUploadFile()){
			return;
		}
	    $('#addForm')[0].action=ctx+"/jsp/iosManagement/IosSysUpdate.html?param=1";
	    $('#addForm')[0].submit();
	}

	function checkUploadFile(){   
	    //var objButton=document.getElementById("chooseButton");//上传按钮
		var objFileUpload=document.getElementById('upload_zip');//FileUpload
	    var fileName=new String(objFileUpload.value);//文件名

	    var objFileUpload1=document.getElementById('upload_zip1');//FileUpload
		   // var objMSG=document.getElementById('msg');//显示提示信息用的DIV
		var fileName1=new String(objFileUpload1.value);//文件名
	    if(!fileName||fileName.length==0){
			return false;
		}
	    if(!fileName1||fileName1.length==0){
			return false;
		}
		return true;
	    //var extension=new String (FileName.substring(FileName.lastIndexOf(".")+1,FileName.length));//文件扩展名
	 
	   // if(extension=="jpg"||extension=="JPG")//你可以添加扩展名
	   // {
		 //   objButton.disabled=false;//启用上传按钮
		   // objMSG.innerHTML="";
	    //}
	    //else
	    //{
	     //   objButton.disabled=true;//禁用上传按钮
	      //  objMSG.innerHTML="请选择正确的文件文件";
	    //}
	}
</SCRIPT>
<style type="text/css">
.ztree li span.button.pIcon01_ico_open{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon01_ico_close{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/2-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon01_ico_docu{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_open{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_close{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/3-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon02_ico_docu{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_open{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_close{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/4-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon03_ico_docu{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_open{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_close{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon04_ico_docu{margin-right:2px; background: url(../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
</style>

</head>
<body>
	
	<form class="x-client-form" method="POST" name="form_data" action="" id="addForm" enctype="multipart/form-data"> 
		<input type="hidden" name="updatePackageIds" id="updatePackageIds" value=""/>
		<div class="x-title"><span>&nbsp;&nbsp;系统升级管理-IOS系统升级</span></div>
		<table><tr style ="heigt:30px"></tr></table>
		<div>
			<table class="x-data-table">
				<tr>
					<td class="x-data-table-td">上传plist文件：</td>
					<td class="x-data-table-td"><input type="file" name="upload_zip" id="upload_zip" ></td>
				</tr>
				<tr>
					<td class="x-data-table-td">上传ipa文件：</td>
					<td class="x-data-table-td"><input type="file" name="upload_zip1" id="upload_zip1" ></td>
				</tr>
				<tr>
					<td class="x-data-table-td"></td>
					<td class="x-data-table-td"><input type="file" name="upload_zip2" id="upload_zip2" ></td>
				</tr>
				<tr>
					<td class="x-data-table-td"></td>
					<td class="x-data-table-td"><input type="file" name="upload_zip3" id="upload_zip3" ></td>
				</tr>
				<tr>
					<td class="x-data-table-td"></td>
					<td class="x-data-table-td"><input type="file" name="upload_zip4" id="upload_zip4" ></td>
				</tr>
				<tr>
					<td class="x-data-table-td"></td>
					<td class="x-data-table-td"><input type="file" name="upload_zip5" id="upload_zip5" ></td>
				</tr>
			</table>
		</div>
		<div style="float: right">
			<input class="tableBtn" type="button" id="chooseButton" value="确定" />
		</div>
	</form>

</body>

</html>