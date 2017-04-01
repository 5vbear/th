<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="th.dao.*"%>
<%@ page import="th.user.*"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
	Log logger = LogFactory.getLog( this.getClass() );
	User user = (User) session.getAttribute( "user_info" );
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

<%
	String zNodes = (String) request.getAttribute( "zNodes" );
	String roleName = (String) request.getAttribute( "roleName" );
	if ( roleName == null ) {
		roleName = "";
	}
	String pageTitle = (String) request.getAttribute( "pageTitle" );
	String dealFlg = (String) request.getAttribute( "dealFlg" );
	String saveResult = (String) request.getAttribute( "saveResult" );
	String acTurn = (String)request.getAttribute( "acTurn" );
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>角色定义页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../../css/machine.css" />
<SCRIPT LANGUAGE="JavaScript">

	//window.onload必须等到页面内包括图片的所有元素加载完毕后才能执行 
	window.onload = function(){
		var dealFlg = "<%=dealFlg%>";
		if(dealFlg=="change"){
			document.getElementById("roleText").disabled = true;
		}
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
	            var res = http_request.responseText;
	            if(res != 0){
		        	var roleName = document.getElementById('roleText').value;
		        	alert('角色名为"' + roleName + '"的角色已经存在，请重新命名!');
		        	document.getElementById('roleText').focus();			        	
		        }
	            checkFlg = res;
	            //document.getElementById("dptAjaxSel").innerHTML=http_request.responseText;
	         }
	         else{
	             //请求页面有问题
	             alert("您所请求的页面有异常!错误状态:"+http_request.status);
	         }
	     }
	 };
	
	
	function checkRoleName() {
		var inRoleName = document.getElementById("roleText").value;
		var sendUrl = "/th/jsp/sysMang/com/ajaxCheck.html?type=roleNameCheck&roleName=" + inRoleName
		// 添加时间戳，避免因缓存结果而导致不能实时得到最新的结果
		sendUrl += "&nocache=" + new Date().getTime(); 
		sendUrl = encodeURI(encodeURI(sendUrl));
		send_request(sendUrl);
	};
	
	
	var zTreeObj;
	var setting = {
		view: {
			showIcon: false
		},
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		},			
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId",
				rootPId : 0,
			}
		},		
		callback: {
			onClick: zTreeOnClick
		}

	};

	var zNodes = <%=zNodes%>
	$(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		
		var acTurner = "<%=acTurn%>";
		// "修改"
		if(acTurner!=""){
			// 初始化加载时，禁用所有树节点
			var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
			for (var i=0; i<nodes.length; i++) {
				zTreeObj.setChkDisabled(nodes[i], true);
			}
		}

	});	
	function zTreeOnClick(event, treeId, treeNode) {
	    /* alert(treeNode.tId + ", " + treeNode.name); */
	};	
	function btnOperations() {
	
		this.enabled = function(){
			// 可编辑状态，将所有树节点解禁
			var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
			for (var i=0; i<nodes.length; i++) {
				zTreeObj.setChkDisabled(nodes[i], false);
			}
			// 文本框状态判断
			var dealFlg = "<%=dealFlg%>";
			if(dealFlg=="add"){
				document.getElementById("roleText").disabled = false;
			}
		};
		this.save = function(){
			var content = document.getElementById("roleText");
			var zNodes = zTreeObj.getCheckedNodes(true);
			var v = "";
			if(content.value!=""&&zNodes!=null&&zNodes.length>0){
				var dealFlg = "<%=dealFlg%>";
				if(dealFlg=="add"){
					// 检查角色名称是否已经存在
					checkRoleName();
				}
				if(checkFlg=="0"){
					for (var i=0; i<zNodes.length; i++) {
						v += zNodes[i].id;
						if(i<zNodes.length-1){
							v += ",";
						}
					}
					window.document.form_data.action = "/th/jsp/sysMang/roleMang/roleDeal.html";
					window.document.form_data.sel_nodes_info.value = v;
					window.document.form_data.roleName.value = content.value;
					window.document.form_data.dealFlg.value = dealFlg;
					window.document.form_data.submit();
				}
			}else if(content.value==""){
				alert("请先在文本框中输入角色名称，然后再进行确认操作!");
			}else if(zNodes==null||zNodes.length==0){
				alert("请先在当前权限树中点选节点，然后再进行确认操作!");
			}else{
				alert("请先点选权限和输入角色名称，然后再进行确认操作!");
			}
		};
		this.back = function(){
			window.document.form_data.action = "/th/jsp/sysMang/roleMang/roleList.html";
			window.document.form_data.submit();
		}
		
	};	
	var btnOper = new btnOperations();

</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;<%=pageTitle%></span></div>
	<div></div>
	<div style="margin: 0 auto;">
		<ul style="margin: 0 auto; list-style: none;">
			<div style="height: 520px; padding-top: 10px; float: left;">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
			<div style="height: 520px; padding-top: 20px; padding-left: 20px; float: left;">
				<br>	
				<form method="POST" name="form_data" action="">
					<input type="hidden" name="dealFlg" value=""/>
				    <input type="hidden" name="sel_nodes_info" value=""/>
				    <input type="hidden" name="roleName" value=""/>
					<table border="0" cellspacing="0" cellpadding="0" class="contact_input">
						<tr>
							<th colspan="2">用户角色：</th>
							<td colspan="2"><input size="40" type="text" name="roleText" value="<%=roleName%>" <% out.print(acTurn); %>
								id="roleText" maxlength="64" ></td>
						</tr>
						<tr>
							<td colspan="4">
								<%
							 		if(!"".equals(acTurn)){
							 			out.print("<input class='tableBtn' type='button' name='button_Enabled' id='btnEnabled' value='编辑' onclick='btnOper.enabled()' />");
							 		}
							 	%>	
								<input class="tableBtn" type="button" name="button_Save" id="btnSave" value="保存" onclick="btnOper.save()" />
								<input class="tableBtn" type="button" name="button_Back" id="btnBack" value="返回" onclick="btnOper.back()" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</ul>
	</div>

</body>
</html>