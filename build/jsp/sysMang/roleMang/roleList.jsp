<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="th.com.util.Define" %>

<%@ page import="th.dao.*"%>
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

<%
	String saveResult = (String) request.getAttribute( "saveResult" );
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色列表页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="../../../js/myDataCheck.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../../css/machine.css" />

<SCRIPT LANGUAGE="JavaScript">

	//window.onload必须等到页面内包括图片的所有元素加载完毕后才能执行 
	window.onload = function(){
		var saveResult = "<%=saveResult%>";
		if(saveResult!=""){
			alert(saveResult);
		}
	};

	var checkFlg = "0";
	
	//Ajax联动处理
	//定义XMLHttpRequest对象
	 var http_request=false;
	
	 function send_request(url){
	     http_request=false;
	     //开始初始化XMLHttpRequest对象
	     if(window.XMLHttpRequest){//Mozilla等浏览器初始化XMLHttpRequest过程
	         http_request=new XMLHttpRequest();
	         //有些版本的Mozilla浏览器处理服务器返回的未包含XML mime-type头部信息的内容时会出错.
	         //因此,要确保返回的内容包含text/xml信息.
	         if(http_request.overrideMimeType){
	             http_request.overrideMimeType("text/xml");
	         }
	     }
	     else if(window.ActiveXObject){//IE浏览器初始化XMLHttpRequest过程
	         try{
	             http_request=new ActiveXObject("Msxml2.XMLHTTP");
	         }
	         catch(e){
	             try{
	                 http_request=new ActiveXObject("Microsoft.XMLHTTP");
	             }
	             catch(e){}
	         }
	     }
	     //异常,创建对象失败
	     if(!http_request){
	         window.alert("不能创建XMLHttpRequest对象实例!");
	         return false;
	     }
	     //指定响应处理函数
	     http_request.onreadystatechange=processRequest;
	     //发送HTTP请求信息
	     // 默认true是异步处理，false是同步处理
	     http_request.open("GET",url,false);
	     http_request.setRequestHeader( "Content-Type", "text/html;charset=UTF-8" );
	     http_request.send(null);
	 };
	
	 //处理返回信息函数
	 function processRequest(){
	     //判断对象状态
	     if(http_request.readyState==4){
	         //判断HTTP状态码
	         if(http_request.status==200){
	             //信息已经成功返回
	            //window.document.write(http_request.responseText);
	            //alert(http_request.responseText);
	            checkFlg = http_request.responseText;
	            //document.getElementById("dptAjaxSel").innerHTML=http_request.responseText;
	         }
	         else{
	             //请求页面有问题
	             alert("您所请求的页面有异常!错误状态:"+http_request.status);
	         }
	     }
	 };

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

	function btnChangeClick(me){
		var btnId = me.id;
		/* var btnChange = document.getElementById("btnChange_" + me.value); */
		var roleId = btnId.substring(10);
		window.document.form_data.dealFlg.value = "change";
		window.document.form_data.action = "/th/jsp/sysMang/roleMang/roleDeal.html";
		window.document.form_data.sel_role_id.value = roleId;
		window.document.form_data.submit();

	};
	function btnRenameClick(me){
		var btnId = me.id;
		/* var btnChange = document.getElementById("btnRename_" + me.value); */
		var roleId = btnId.substring(10);
		var curBtnValue = me.value;
		if(curBtnValue=="重命名"){
			var roleContent = document.getElementById("trRename_" + roleId);
			roleContent.innerHTML="<input type='text' size='40' name='reRoleName_" + roleId + "' id='rern_" + roleId + "' value='" + roleContent.innerText + "' />";
			me.value="确定";
			document.getElementById("btnCancle_" + roleId).style.visibility = "visible";
		}else if(curBtnValue=="确定"){
			var reRoleName = document.getElementById("rern_" + roleId).value;
			//sjw mod start
			if(myTrim(reRoleName)==""){
				//end
				alert("角色名不能为空，请输入修改后的角色名!");
				document.getElementById("rern_" + roleId).focus();
			}else{
				// 检查角色名称是否已经存在
				var sendUrl = "/th/jsp/sysMang/com/ajaxCheck.html?type=roleNameCheck&roleName=" + reRoleName
				sendUrl += "&nocache=" + new Date().getTime();
				sendUrl = encodeURI(encodeURI(sendUrl));
				send_request(sendUrl);
				// 角色名没有重复，可以送入后台进行修改处理
				if(checkFlg=="0"){
					window.document.form_data.dealFlg.value = "rename";
					window.document.form_data.action = "/th/jsp/sysMang/roleMang/roleList.html";
					window.document.form_data.sel_role_id.value = roleId;
					window.document.form_data.input_role_name.value = reRoleName;
					window.document.form_data.submit();
				}else{
					alert('角色名为"' + reRoleName + '"的角色已经存在，请重新命名!');
			        document.getElementById("rern_" + roleId).focus();
				}

			}
		}

	};
	function btnCancleClick(me) {
		window.document.form_data.action = "/th/jsp/sysMang/roleMang/roleList.html";
		window.document.form_data.submit();
		
	};
	function btnDeleteClick(me) {
		var btnId = me.id;
		/* var btnDel = document.getElementById("btnDel_" + me.value); */
		var roleId = btnId.substring(7);
		window.document.form_data.dealFlg.value = "del";
		window.document.form_data.action = "/th/jsp/sysMang/roleMang/roleList.html";
		window.document.form_data.sel_role_id.value = roleId;
		window.document.form_data.submit();

	};
	function btnOperations() {

		this.addRole = function() {
			window.document.form_data.action = "/th/jsp/sysMang/roleMang/roleDeal.html";
			window.document.form_data.dealFlg.value = "add";
			window.document.form_data.submit();
		}

	};
	var btnOper = new btnOperations();
</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;系统管理-角色管理</span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="sel_role_id" value=""/>
		<input type="hidden" name="input_role_name" value=""/>
		<div>
			<div>
				<table class="x-data-table" id="dataTableId">
					<%
						
						out.print( "<tr><th style='width: 30%' class='x-data-table-th'>角色名称</th>"
								+ "<th style='width: 15%' class='x-data-table-th'>操作</th></tr>" );
					
						HashMap[] rolesList = (HashMap[]) request.getAttribute( "RoleList" );

						if ( rolesList!= null&&rolesList.length>0 ) {
							
							for ( int i = 0; i < rolesList.length; i++ ) {
								HashMap roleMap = rolesList[i];
								long roleID = Long.parseLong(roleMap.get( "ROLE_ID" ).toString());
								String roleName = (String) roleMap.get( "ROLE_NAME" );																
								
								// middle
								out.print( "<tr><td class='x-data-table-td-left-pad'><span id='trRename_" + roleID + "'>" + roleName + "</span></td> ");
								// bottom
								out.print( "<td class='x-data-table-td-left-pad'><input class='tableBtn' type='button' value='编辑' name='buttonChange_" + roleID + "' id='btnChange_" + roleID + 
								           "' onclick='btnChangeClick(this)'/><input class='tableBtn' type='button' value='删除' name='buttonDelete_" + roleID + "' id='btnDel_" + roleID + 
								           "' onclick='btnDeleteClick(this)'/><input class='tableBtn' type='button' value='重命名' name='buttonRename_" + roleID + "' id='btnRename_" + roleID + 
								           "' onclick='btnRenameClick(this)'/><input class='tableBtn' type='button' value='撤销' name='buttonCancle_" + roleID + "' id='btnCancle_" + roleID + 
								           "' style='visibility:hidden' onclick='btnCancleClick(this)'/></td></tr>" );

							}

						}
					%>
				</table>
			</div>
			<div style="float: left">
				<input class="leftBtn" type="button" name="button_Add" id="btnAdd" value="添加" onclick="btnOper.addRole()" /> 
			</div>
		</div>
	</form>
</body>
</html>
