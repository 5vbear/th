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
				out.print("alert('添加成功!');\n");
			else
				out.print("alert('添加失败!');\n");
		}
	%>
	$(document).ready(function() {
		$('#chooseButton').click(chooseUpdatePackages);
		$('#returnButton').click(returnFunction);
	});
	function returnFunction(){
	    $('#PackagesList')[0].action=ctx+"/jsp/sysUpdateManagement/SysUpdatePackage.html?action=List";
	    $('#PackagesList')[0].submit();
	}
	function chooseUpdatePackages(){
		var data = $('#file_desc').val();
		data = $.trim(data);
		if(data.length < 1) {  
			alert('版本号不能为空');
			return;
		}
		var checkNum = /^(\d*\.){2}(\d*)$/;

		if(!new RegExp(checkNum).test(data)){
			alert('版本号格式为: 1.0.0');
			return;
		}
	    $('#PackagesList')[0].action=ctx+"/jsp/sysUpdateManagement/SysUpdatePackage.html?action=Add&file_desc="+$('#file_desc').val()+"&devId="+$('#devId').val();
	    $('#PackagesList')[0].submit();
	}

</script>
<title>系统升级文件管理</title>
</head>
<body>
	
	<form class="x-client-form" method="POST" name="form_data" action="" id="PackagesList" enctype="multipart/form-data"> 
		<input type="hidden" name="updatePackageIds" id="updatePackageIds" value=""/>
		<div class="x-title"><span>&nbsp;&nbsp;系统升级管理-系统升级包添加</span></div>
		<table><tr style ="heigt:30px"></tr></table>
		<div>
			<table class="x-data-table">
				<tr>
					<td class="x-data-table-td">文件选择：</td>
					<td class="x-data-table-td"><input type="file" name="upload_zip" id="upload_zip"></td>
				</tr>
				<tr>
					<td class="x-data-table-td">下发端机类型：</td>
					<td class="x-data-table-td">
 					<select name="devId" id="devId">
					<%
				    	HashMap[] osTypes = (HashMap[])request.getAttribute( "osTypes" );
						if(osTypes!=null&&osTypes.length>=1){
							for(int i=0;i<osTypes.length;i++){
								out.print("<option value='" + osTypes[i].get("ID") + "'>" + osTypes[i].get("OS") + "</option>");
							}
						}
					%>
					</select>	
					</td>
				</tr>
				<tr>
					<td class="x-data-table-td">版本号：</td>
					<td class="x-data-table-td"><input type="text" name="file_desc" id="file_desc" value="" onKeyUp="checkisChinese(this)" ></td>
				</tr>
			<!-- 王经纬   add start -->
			<script language="javascript" type="text/javascript">  
			function checkisChinese(obj){						//判断输入是否为中文
				var val = obj.value;
				if(val!=null||val!=" "){
					var c =val.split("");
					val = "";
					for (var i = 0;i < c.length;i++){
						if((/[\u4e00-\u9fa5]+/).test(c[i])){
							alert("不能输入中文！")；
							break;
						}
						val += c[i];
					}
					obj.value = val;
				} 
			}
			</script>
			<!-- 王经纬  add end -->
			</table>
		</div>
		<div style="float: right">
			<input class="tableBtn" type="button" id="chooseButton" value="提交" />
			<input class="tableBtn" type="button" id="returnButton" value="返回" />
		</div>
	</form>
</body>
</html>