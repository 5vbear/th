<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
    
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
    String alertInfo = (String) request.getAttribute( "AlertInfo" );
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>组织定义界面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="../../../js/myDataCheck.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../../css/machine.css" />
<SCRIPT LANGUAGE="JavaScript">

	function enabled(){
		document.getElementById('orgMark').disabled='';
		document.getElementById('orgFullName').disabled='';
		document.getElementById('orgWebsite').disabled='';
		document.getElementById('orgIntroduction').disabled='';
		document.getElementById('orgDescription').disabled='';
		document.getElementById('contacter').disabled='';
		document.getElementById('contacterPhone').disabled='';
		document.getElementById('contacterMailBox').disabled='';
		document.getElementById('contacterIdNumber').disabled='';
		document.getElementById('otherContact').disabled='';
		document.getElementById('orgStatus').disabled='';
	}
	function save(){
		if(document.getElementById('orgMark').disabled==true){
			alert("请修改信息!");
			return;
		}
		var regex = new RegExp("^[0-9]*$");
		if(regex.test(document.getElementById('contacterPhone').value)==false){
			alert("联系人电话应该为整数。");
			return;
		}
		
		var zNodes = zTreeObj.getSelectedNodes();
		var zNode = zNodes[0];
		window.document.form_data.action = "/th/jsp/sysMang/orgMang/orgDeal.html";
		window.document.form_data.dealFlg.value = "save";
		window.document.form_data.sel_nodes_info.value = zNodes[0].id;
		window.document.form_data.submit();
	}
	
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
		 http_request.onreadystatechange=prRoleList;
		 //发送HTTP请求信息
		 // 默认true是异步处理，false是同步处理		  
		 http_request.open("GET",url,false);
		 http_request.send(null);
	 };
	
	 
	//处理返回信息函数
	 function prRoleList(){
	     //判断对象状态
	     if(http_request.readyState==4){
	         //判断HTTP状态码
	         if(http_request.status==200){
	             //信息已经成功返回
	            //window.document.write(http_request.responseText);
	            //alert(http_request.responseText);
	            document.getElementById("orEditDIV").innerHTML=http_request.responseText;
	         }
	         else{
	             //请求页面有问题
	             alert("您所请求的页面有异常!错误状态:"+http_request.status);
	         }
	     }
	 };
	
	function getRoleList() {
		var zNodes = zTreeObj.getSelectedNodes();
		var zNode = zNodes[0];
		var sendUrl = "/th/jsp/sysMang/com/ajaxCheck.html?type=orgRoleList&orgId=" + zNode.id;
		sendUrl += "&nocache=" + new Date().getTime();
		sendUrl = encodeURI(encodeURI(sendUrl));
		send_request(sendUrl);
		
	};

	//window.onload必须等到页面内包括图片的所有元素加载完毕后才能执行 
	window.onload = function(){
		document.getElementById("tableId").style.display = "none";
		document.getElementById("bottomId").style.display = "none";
		var alertInfos = "<%=alertInfo%>";
		if(alertInfos!=""){
			alert(alertInfos);
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
		callback: {
			beforeClick: zTreeBeforeClick,
			onClick: zTreeOnClick
		},
		view: {
			fontCss: setFontCss,
			selectedMulti: false

		}

	};
	var zNodes = <%=zNodes%>
	$(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		
		var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
		// 设置ztree不同层级的节点显示图标 
		for (var i=0; i<nodes.length; i++) {
			
			var level = 0;
			level = nodes[i].level;
			level = level + 1;
			if(nodes[i].isParent){
				nodes[i].iconSkin = "pIcon0" + level;
			}else{
				if(nodes[i].mactype == 'mac'){
					nodes[i].iconSkin = "icon04";
				}else{
					nodes[i].iconSkin = "icon0" + level;
				}
			}
			zTreeObj.updateNode(nodes[i]);
		}
	});	
	function setFontCss(treeId, treeNode) {
		
		return treeNode.checked == false ? {color:"gray"} : {};
	};
	function zTreeBeforeClick(treeId, treeNode, clickFlag) {
		
	    return (treeNode.checked !== false);
	};

	
	function zTreeOnClick(event, treeId, treeNode) {
	    //alert(treeNode.id + ", " + treeNode.name + ", " + treeNode.checked);
		if((treeNode.ORG_MARK)=="null"){
			document.getElementById("orgMark").value = "";
		}else{
			document.getElementById("orgMark").value=treeNode.ORG_MARK;
		}
		document.getElementById("orgMark").disabled="true";
		if(treeNode.ORG_FULL_NAME=="null"){
			document.getElementById("orgFullName").value = "";
		}else{
			document.getElementById("orgFullName").value=treeNode.ORG_FULL_NAME;
		}
		document.getElementById("orgFullName").disabled="true";
		if(treeNode.ORG_WEBSITE=="null"){
			document.getElementById("orgWebsite").value = "";
		}else{
			document.getElementById("orgWebsite").value=treeNode.ORG_WEBSITE;
		}
		document.getElementById("orgWebsite").disabled="true";
		if(treeNode.ORG_INTRODUCTION=="null"){
			document.getElementById("orgIntroduction").value = "";
		}else{
			document.getElementById("orgIntroduction").value=treeNode.ORG_INTRODUCTION;
		}
		document.getElementById("orgIntroduction").disabled="true";
		if(treeNode.ORG_DESCRIPTION=="null"){
			document.getElementById("orgDescription").value = "";
		}else{
			document.getElementById("orgDescription").value=treeNode.ORG_DESCRIPTION;
		}
		document.getElementById("orgDescription").disabled="true";
		if(treeNode.CONTACTER=="null"){
			document.getElementById("contacter").value = "";
		}else{
			document.getElementById("contacter").value=treeNode.CONTACTER;
		}
		document.getElementById("contacter").disabled="true";
		if(treeNode.CONTACTER_PHONE=="null"){
			document.getElementById("contacterPhone").value = "";
		}else{
			document.getElementById("contacterPhone").value=treeNode.CONTACTER_PHONE;
		}
		document.getElementById("contacterPhone").disabled="true";
		if(treeNode.CONTACTER_MAILBOX=="null"){
			document.getElementById("contacterMailBox").value = "";
		}else{
			document.getElementById("contacterMailBox").value=treeNode.CONTACTER_MAILBOX;
		}
		document.getElementById("contacterMailBox").disabled="true";
		if(treeNode.CONTACTER_ID_NUMBER=="null"){
			document.getElementById("contacterIdNumber").value = "";
		}else{
			document.getElementById("contacterIdNumber").value=treeNode.CONTACTER_ID_NUMBER;
		}
		document.getElementById("contacterIdNumber").disabled="true";
		if(treeNode.OTHER_CONTACTE=="null"){
			document.getElementById("otherContact").value = "";
		}else{
			document.getElementById("otherContact").value=treeNode.OTHER_CONTACTE;
		}
		document.getElementById("otherContact").disabled="true";

		if((treeNode.ORG_STATUS)=="null"){
			document.getElementById("orgStatus").value = "1";
		}else{
			document.getElementById("orgStatus").value=treeNode.ORG_STATUS;
		}
		document.getElementById("orgStatus").disabled="true";
		if((treeNode.ORG_CREATETIME)=="null"){
			document.getElementById("orgCreatetime").value = "";
		}else{
			document.getElementById("orgCreatetime").value=treeNode.ORG_CREATETIME;
		}
		document.getElementById("orgCreatetime").disabled="true";
		if((treeNode.REAL_NAME)=="null"){
			document.getElementById("orgCreator").value = "";
		}else{
			document.getElementById("orgCreator").value=treeNode.REAL_NAME;
		}
		document.getElementById("orgCreator").disabled="true";
		document.getElementById("tableId").style.display = "";
		document.getElementById("bottomId").style.display = "";
		document.getElementById("methodId").style.display = "none";
	}; 
		
	var roleList="";
	function btnOperations() {
		
		this.checkBox = function(){
			
			var roles=document.getElementsByName("role");
			for (i=0;i<roles.length;i++){
			  if(roles[i].checked == true){
				  roleList += roles[i].value + ",";
			  }
			}
		};
		this.orgEdit = function(me){
			var btnId = me.id;
			
			var type = btnId.substring(3);
			var zNodes = zTreeObj.getSelectedNodes();
			var zNode = zNodes[0];
			
			
			if(!zNode){
				alert("请先点选一个组织节点!");
			}else{
				if(type=="Add"){
					document.getElementById("orEditDIV").innerHTML="<table class='x-data-table'><tr><th style='width: 20%' class='x-data-table-th'>组织名称:</th>" + 
				    "<td style='width: 35%' class='x-data-table-td'><input type='text' size='40' name='orgName' id='orgName' value='' /></td>" +
				    "<td ><input class='tableBtn' type='button' value='确定' name='buttonOK' id='btnOK' onclick='btnOper.addOrg()'/></td></tr></table>" 
				}else if(type=="Change"){
					document.getElementById("orEditDIV").innerHTML="<table class='x-data-table'><tr><th style='width: 20%' class='x-data-table-th'>组织名称:</th>" + 
				    "<td style='width: 35%' class='x-data-table-td'><input type='text' size='40' name='orgName' id='orgName' value='" + zNode.name + "' /></td>" +
				    "<td ><input class='tableBtn' type='button' value='确定' name='buttonOK' id='btnOK' onclick='btnOper.changeOrg()'/></td></tr></table>" 
				}else if(type=="Auth"){
					getRoleList();
				}
			}
			document.getElementById("methodId").style.display = "";
			document.getElementById("tableId").style.display = "none";
			document.getElementById("bottomId").style.display = "none";
		};
		this.addOrg = function(){
			var zNodes = zTreeObj.getSelectedNodes();
			var zNode = zNodes[0];
			var curNodeLevel = zNode.level;
			if(curNodeLevel==2){
				alert("当前点选节点是二级组织,不允许添加三级组织节点,请重新点选一个组织节点!");
			}else{
				var content = document.getElementById("orgName");
				if(content==null||myTrim(content.value)==""){
					alert("请输入新建的组织名称!");
				}else if(!zNode){
					alert("请先点选一个组织节点!");
				}
				else{
					window.document.form_data.dealFlg.value = "add";
					window.document.form_data.sel_nodes_info.value = zNodes[0].id;
					window.document.form_data.in_text_content.value = content.value;
					window.document.form_data.submit();
				}
			}
		};
		this.changeOrg = function(){
			var zNodes = zTreeObj.getSelectedNodes();
			var zNode = zNodes[0];
			var content = document.getElementById("orgName");
			if(!zNode){
				alert("请先点选要修改的组织节点!");
			}else if(myTrim(content.value)==""||content == null){
				alert("请输入新的组织名称!");
			}
			else{
				window.document.form_data.dealFlg.value = "change";
				window.document.form_data.sel_nodes_info.value = zNodes[0].id;
				window.document.form_data.in_text_content.value = content.value;
				window.document.form_data.submit();
			}
			
		};
		this.deleteOrg = function(){
			var zNodes = zTreeObj.getSelectedNodes();
			var zNode = zNodes[0];
			if(zNode){
				window.document.form_data.dealFlg.value = "del";
				window.document.form_data.sel_nodes_info.value = zNodes[0].id;
				window.document.form_data.submit();
			}else{
				alert("请先点选要删除的组织节点!");
			}
			
		};
		this.authRole = function(){
			var zNodes = zTreeObj.getSelectedNodes();
			var zNode = zNodes[0];
			this.checkBox();
			if(zNode&&roleList!=""){
				window.document.form_data.dealFlg.value = "auth";
				window.document.form_data.sel_nodes_info.value = zNodes[0].id;
				window.document.form_data.roleList.value = roleList;
				window.document.form_data.submit();
			}else if(!zNode){
				alert("请先点选要授权的组织节点!");
			}else if(roleList==""){
				alert("请在角色组中点选要授权的角色!");
			}else{
				alert("在执行组织授权前，请先点选要授权的组织节点和角色!");
			}
		}
		
	};	
	var btnOper = new btnOperations();


</SCRIPT>
<style type="text/css">
.ztree li span.button.pIcon01_ico_open{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon01_ico_close{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/2-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon01_ico_docu{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/2-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_open{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon02_ico_close{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/3-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon02_ico_docu{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/3-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_open{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon03_ico_close{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/4-1.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon03_ico_docu{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/bank/4-2.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_open{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.pIcon04_ico_close{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.icon04_ico_docu{margin-right:2px; background: url(../../../zTree/css/zTreeStyle/img/diy/4.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
</style>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;系统管理-组织管理</span></div>
	<table><tr style ="heigt:30px"></tr></table>
	<div style="width:100%">
		<input class="leftBtn" type="button" name="button_Add" id="btnAdd" value="添加" onclick="btnOper.orgEdit(this)" /> 
		<input class="leftBtn" type="button" name="button_Change" id="btnChange" value="编辑" onclick="btnOper.orgEdit(this)" />
		<input class="leftBtn" type="button" name="button_Delete" id="btnDel" value="删除" onclick="btnOper.deleteOrg()" />
		<input class="leftBtn" type="button" name="button_Authorize" id="btnAuth" value="授权" onclick="btnOper.orgEdit(this)" />
	</div>
	<table><tr style ="heigt:50px"></tr></table> 
	<div style="margin: 0 auto; float: left; width: 100%">
		<form method="POST" name="form_data" action="/th/jsp/sysMang/orgMang/orgDeal.html">
			<input type="hidden" name="roleList" value=""/>
		    <input type="hidden" name="dealFlg" value=""/>
		    <input type="hidden" name="sel_nodes_info" value=""/>
		    <input type="hidden" name="in_text_content" value=""/>
			<ul style="margin: 0 auto; list-style: none;">
				<div style="float:left;width:30%">
					<fieldset style="width:90%;">
						<ul id="treeDemo" class="ztree" style="margin-top: 5px; width:100%;"></ul>
					</fieldset>
				</div>
				<div style="float:left;width:70%" id = "methodId">
					<div style="overflow-y:auto;">
						<div id="orEditDIV"  style="height: 500px; width: 100%; padding-top: 5px; float: left;">
						</div>
					</div>
				</div>		
				<div style="float:left;width:70%">
					<div style="overflow-y:auto;">
					<table style="width: 100px" id="tableId" class="x-data-table" >
						<tr>
							<th style="width: 45%" class="x-data-table-th">单位标识</th>
							<td class="x-data-table-td"><input type="text" size="40" name="orgMark" id="orgMark" disabled="true" value="" /></td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">单位全名</th>
							<td class="x-data-table-td"><input type="text" size="40" name="orgFullName" id="orgFullName" disabled="true" value="" /></td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">单位网址</th>
							<td class="x-data-table-td"><input type="text" size="40" name="orgWebsite" id="orgWebsite" disabled="true" value="" /></td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">单位简介</th>
							<td class="x-data-table-td"><input type="text" size="40" name="orgIntroduction" id="orgIntroduction" disabled="true" value="" /></td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">单位描述</th>
							<td class="x-data-table-td"><input type="text" size="40" name="orgDescription" id="orgDescription" disabled="true" value="" /></td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">单位状态</th>
							<td class="x-data-table-td">
								<select id="orgStatus" name="orgStatus" style="width:40" disabled="true" >
									<option value="1">正常</option>
									<option value="2">撤销</option>
								</select>
							</td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">联系人</th>
							<td class="x-data-table-td"><input type="text" size="40" name="contacter" id="contacter" disabled="true" value="" /></td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">联系人电话</th>
							<td class="x-data-table-td"><input type="text" size="40" name="contacterPhone" id="contacterPhone" disabled="true" value="" /></td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">联系人邮箱</th>
							<td class="x-data-table-td"><input type="text" size="40" name="contacterMailBox" id="contacterMailBox" disabled="true" value="" /></td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">联系人身份证号</th>
							<td class="x-data-table-td"><input type="text" size="40" name="contacterIdNumber" id="contacterIdNumber" disabled="true" value="" /></td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">联系人其它联系方式</th>
							<td class="x-data-table-td"><input type="text" size="40" name="otherContact" id="otherContact" disabled="true" value="" /></td>
						</tr>
						
						<tr>
							<th style="width: 45%" class="x-data-table-th">修改时间</th>
							<td class="x-data-table-td"><input type="text" size="40" name="orgCreatetime" id="orgCreatetime" disabled="true" value="" /></td>
						</tr>
						<tr>
							<th style="width: 45%" class="x-data-table-th">初始用户</th>
							<td class="x-data-table-td"><input type="text" size="40" name="orgCreator" id="orgCreator" disabled="true" value="" /></td>
						</tr>
					</table>
					<table style="width: 500px" id="bottomId" class="x-data-table" >
						<div class="x-client-form">
							<input class='tableBtn' type='button' name='button_Enabled' id='btnEnabled' value='编辑' onclick='enabled()' />
		    				<input class="tableBtn" type="button" name="button_Save" id="btnSave" value="保存" onclick="save()" /> 
		    				<!--  
		    				<input class="tableBtn" type="button" name="button_Back" id="btnBack" value="返回" onclick="javascript:back();" /> 
		  					-->
		  				</div>
	  				</table>
					</div>
				</div>		
			</ul>
			
		</form>
	</div>


</body>
</html>