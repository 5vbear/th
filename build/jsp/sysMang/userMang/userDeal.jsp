<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="th.entity.UserBean" %>
    
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
	String orgSelect = (String) request.getAttribute("orgSelect");
	// 选中用户原有信息取得
	UserBean userBean = (UserBean) request.getAttribute( "selUserBean" );
	long selUserId = userBean.getUserId();
	long selOrgId = userBean.getOrgId();
	String selDptName = userBean.getDptName();
	String selNickName = userBean.getNickName();
	String selUserName = userBean.getUserName();
	String selPassword = userBean.getPassword();
	String selEmail = userBean.getEmail();
	String selFixedTel = userBean.getFixedTel();
	String selOthCont = userBean.getMobilePhone();
	String selDesp = userBean.getUserDesp();
	String selUserType = userBean.getUserType();
	
	// 选中用户口令确认标识取得
	String userConfirmFlg = (String) request.getAttribute("userConfirmFlg");
	
	String operaType = "";
	// 操作类型判断
	if("1".equals(userConfirmFlg)){
		operaType = "add";
	}else if("0".equals(userConfirmFlg)){
		operaType = "change";
	}
	
	String pageTitle = (String)request.getAttribute( "pageTitle" );
	String saveResult = (String)request.getAttribute( "saveResult" );
	String dealRoleList = (String)request.getAttribute( "dealRoleList" );
	String acTurn = (String)request.getAttribute( "acTurn" );
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户信息处理页面</title>
<link rel="stylesheet" href="../../../zTree/css/demo.css" type="text/css">
<link rel="stylesheet" href="../../../zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="../../../zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="../../../zTree/js/jquery.ztree.excheck-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/channel.css" />
<link rel="stylesheet" type="text/css" href="../../../css/machine.css" />

<SCRIPT LANGUAGE="JavaScript">

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

	// window.onload必须等到页面内包括图片的所有元素加载完毕后才能执行 
	window.onload = function(){
		window.document.form_data.hide_org_select.value = "<%= orgSelect %>";
		var acTurn = "<%=acTurn%>";
		if(acTurn!=""){
			document.getElementById("roleListAjaxCheck").disabled = true;
		}
		var operaType = "<%= operaType %>";
		var saveResult = "<%=saveResult%>";
		if(saveResult==""){
			if(operaType=="add"){
				getDpts();
				getRoleList();
			}else if(operaType=="change"){
				document.getElementById("SelectOrg").value = <%= selOrgId %>;
				// Ajax组织与部门之间的级联处理
				getDpts();
				document.getElementById("userType").value = <%= selUserType %>;
				getRoleList();
			}
		}else{
			document.getElementById("SelectOrg").value = <%= selOrgId %>;
			getDpts();
			document.getElementById("userType").value = <%= selUserType %>;
			if(operaType=="add"){
				document.getElementById("btnEnabled").disabled = true;
				document.getElementById("btnSave").disabled = true;
			}
			alert(saveResult);			
		}
		
	};
	
	var checkFlg = "-1";
	
	// Ajax联动处理
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
	     var type = url.substring(0,4);
	     url = url.substring(4);
	     if(type=="dpts"){
	    	//指定响应处理函数
		     http_request.onreadystatechange=prDpts;
		     //发送HTTP请求信息
		     // 默认true是异步处理，false是同步处理
		     http_request.open("GET",url,false);
	     }else if(type=="role"){
		     http_request.onreadystatechange=prRoleList;
		     http_request.open("GET",url,false);
	     }else if(type=="nick"){
		     http_request.onreadystatechange=prNickName;
		     http_request.open("GET",url,false);
		     http_request.setRequestHeader( "Content-Type", "text/html;charset=UTF-8" );	    	 
	     }
	     
	     http_request.send(null);
	 };
	
	 //处理返回信息函数
	 function prDpts(){
	     //判断对象状态
	     if(http_request.readyState==4){
	         //判断HTTP状态码
	         if(http_request.status==200){
	             //信息已经成功返回
	            //window.document.write(http_request.responseText);
	            //alert(http_request.responseText);
	            document.getElementById("dptAjaxSel").innerHTML=http_request.responseText;
	         }
	         else{
	             //请求页面有问题
	             alert("您所请求的页面有异常prDpts!错误状态:"+http_request.status);
	         }
	     }
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
	            document.getElementById("roleListAjaxCheck").innerHTML=http_request.responseText;
	         }
	         else{
	             //请求页面有问题
	             alert("您所请求的页面有异常prRoleList!错误状态:"+http_request.status);
	         }
	     }
	 };
	 
	//处理返回信息函数
	 function prNickName(){
	     //判断对象状态
	     if(http_request.readyState==4){
	         //判断HTTP状态码
	         if(http_request.status==200){
	             //信息已经成功返回
	            //window.document.write(http_request.responseText);
	            //alert(http_request.responseText);
	        	 var res = http_request.responseText;
		         if(res != -1){
			        var nickName = document.getElementById('nickName').value;
			        alert('用户账号为"' + nickName + '"的用户已经存在，请重新命名!');
			        document.getElementById('nickName').focus();			        	
			     }
		         checkFlg = res;
	         }
	         else{
	             //请求页面有问题
	             alert("您所请求的页面有异常prNickName!错误状态:"+http_request.status);
	         }
	     }
	 };
	
	function getDpts() {
		var selOrgId = document.getElementById("SelectOrg").value;
		var sendUrl = "dpts" + "/th/jsp/sysMang/dptMang/dptAjaxSelect.jsp?orgId=" + selOrgId;
		// 添加时间戳，避免因缓存结果而导致不能实时得到最新的结果
		sendUrl += "&nocache=" + new Date().getTime(); 
		send_request(sendUrl);
	};
	
	function getRoleList() {
		var operaType = "<%= operaType %>";
		var dealUserId = <%= selUserId %>;
		var selOrgId = document.getElementById("SelectOrg").value;
		var userType = document.getElementById("userType").value;
		var sendUrl = "role" + "/th/jsp/sysMang/com/ajaxCheck.html?type=userTypeCheck&orgId=" + selOrgId + "&userType=" + userType;
		sendUrl += "&dealFlg=" + operaType + "&dealUserId=" + dealUserId;
		sendUrl += "&nocache=" + new Date().getTime();
		send_request(sendUrl);
		
	};
	
	function checkNickName() {
		var inNickName = document.getElementById("nickName").value;
		var sendUrl = "nick" + "/th/jsp/sysMang/com/ajaxCheck.html?type=nickNameCheck&nickName=" + inNickName
		// 添加时间戳，避免因缓存结果而导致不能实时得到最新的结果
		sendUrl += "&nocache=" + new Date().getTime(); 
		sendUrl = encodeURI(encodeURI(sendUrl));
		send_request(sendUrl);
	};
	
	function btnOperations() {
		
		this.enabled = function(){
			
			document.getElementById("SelectOrg").disabled = false;
			document.getElementById("dptAjaxSel").disabled = false;
			document.getElementById("userName").disabled = false;
			document.getElementById("passWord").disabled = false;
			document.getElementById("email").disabled = false;
			document.getElementById("fixedTel").disabled = false;
			document.getElementById("otherContacts").disabled = false;
			document.getElementById("description").disabled = false;
			document.getElementById("userType").disabled = false;
			
			var confirmFlg = "<%=userConfirmFlg%>";
			// 添加
			if(confirmFlg=="1"){				
				document.getElementById("nickName").disabled = false;				
				document.getElementById("confirmPWD").disabled = false;				
			}
			// 角色列表变成可编辑状态
			/* var rolesList = document.getElementsByName("role");
			for (var i=0; i<rolesList.length; i++) {
				rolesList[i].disabled = false;
			} */
			document.getElementById("roleListAjaxCheck").disabled = false;
			
		};
		this.save = function(){
			
			// 处理标识变量取得 
			var dealFlg = "<%=operaType%>";
			
			// 共同项目提出
			/* var dptName = document.getElementById("dptName"); */
			var dptSelect = document.getElementById("SelectDpt");
			var password = document.getElementById("passWord");
			var orgSelect = document.getElementById("SelectOrg");
			var realName = document.getElementById("userName");
			var email = document.getElementById("email");
			var fixedTel = document.getElementById("fixedTel");
			var otherContacts = document.getElementById("otherContacts");
			var description = document.getElementById("description");
			var userType = document.getElementById("userType");
			
			// 选中的角色列表取得
			var roleList = "";
			// 当前页面显示的所有角色列表取得
			var allRoles = "";
			var roles=document.getElementsByName("role");
			for (var i=0;i<roles.length;i++){
			  if(roles[i].checked == true){
				  roleList += roles[i].value + ",";
			  }
			  allRoles += roles[i].value + ",";
			}

			
			// add处理机能
			if("add"==dealFlg){	
				
				var nickName = document.getElementById("nickName");
				var confirmPWD = document.getElementById("confirmPWD");
				
				
				/*if(dptSelect.value==-1){
					alert("请确认当前用户所在组织下存在部门信息，并重新点选!");
				}else*/if(nickName.value==""){
					alert("请输入当前用户账号的名称!");
				}else if(password.value==""){
					alert("请输入当前的用户口令!");
				}else if(password.value!=confirmPWD.value){
					alert("确认口令与用户口令不一致，请重新输入!");
				}else if(email.value==""){
					alert("请输入当前用户的邮件地址!");
				}else if(fixedTel.value.length>15){
					alert("固定电话号码长度必须限制在15个字符内，请重新输入!");
				}else if(otherContacts.value!=""&&otherContacts.value.length!=11){
					alert("移动电话号码长度必须限制为11个字符，请重新输入!");
				}else if(roleList==""){
					alert("请在角色组中点选要授权的角色!");
				}else{
					
					// 检查用户账户是否已经存在
					checkNickName();
					if(checkFlg=="-1"){
						window.document.form_data.dealFlg.value = "add";
						window.document.form_data.action = "/th/jsp/sysMang/userMang/userDeal.html";
						// 获取所属组织信息
						window.document.form_data.sel_org_id.value = orgSelect.value;
						// 获取所在部门信息
						window.document.form_data.input_dpt_id.value = dptSelect.value;
						// 获取用户账号信息
						window.document.form_data.input_user_nickname.value = nickName.value;
						// 获取输入的用户口令
						window.document.form_data.input_user_password.value = password.value;
						// 获取输入的用户名称					
						window.document.form_data.input_user_name.value = realName.value;
						// 获取输入的邮件地址					
						window.document.form_data.input_user_email.value = email.value;
						// 获取输入的联系方式 					
						window.document.form_data.input_user_fixedTel.value = fixedTel.value;
						// 获取输入的其他联系方式					
						window.document.form_data.input_user_othCont.value = otherContacts.value;
						// 获取输入的用户描述						
						window.document.form_data.input_user_desp.value = description.value;
						// 获取点选的用户类型
						window.document.form_data.input_user_type.value = userType.value;
						// 获取勾选的角色列表
						window.document.form_data.roleList.value = roleList;
						// 当前页面显示的全部角色列表取得
						window.document.form_data.curAllRoles.value = allRoles;
						
						window.document.form_data.submit();
					}
					
				}
				
			// change处理机能 
			}else if("change"==dealFlg){
				
				if(dptSelect.value==-1){
					alert("请确认当前用户所在组织下存在部门信息，并重新点选!");
				}else if(password.value==""){
					alert("请输入当前的用户口令!");
				}else if(email.value==""){
					alert("请输入当前用户的邮件地址!");
				}else if(fixedTel.value.length>15){
					alert("固定电话号码长度必须限制在15个字符内，请重新输入!");
				}else if(otherContacts.value!=""&&otherContacts.value.length!=11){
					alert("移动电话号码长度必须限制为11个字符，请重新输入!");
				}else if(roleList==""){
					alert("请在角色组中点选要授权的角色!");
				}else{
					window.document.form_data.dealFlg.value = "change";
					window.document.form_data.action = "/th/jsp/sysMang/userMang/userDeal.html";
					// 获取所属组织信息
					window.document.form_data.sel_org_id.value = orgSelect.value;
					// 获取所在部门信息
					window.document.form_data.input_dpt_id.value = dptSelect.value;
					// 获取输入的用户口令
					window.document.form_data.input_user_password.value = password.value;
					// 获取输入的用户名称					
					window.document.form_data.input_user_name.value = realName.value;
					// 获取输入的邮件地址					
					window.document.form_data.input_user_email.value = email.value;
					// 获取输入的联系方式 					
					window.document.form_data.input_user_fixedTel.value = fixedTel.value;
					// 获取输入的其他联系方式					
					window.document.form_data.input_user_othCont.value = otherContacts.value;
					// 获取输入的用户描述						
					window.document.form_data.input_user_desp.value = description.value;
					// 获取点选的用户类型
					window.document.form_data.input_user_type.value = userType.value;
					// 获取勾选的角色列表
					window.document.form_data.roleList.value = roleList;
					// 当前页面显示的全部角色列表取得
					window.document.form_data.curAllRoles.value = allRoles;
					
					window.document.form_data.hide_user_id.value = <%=selUserId%>;
					window.document.form_data.submit();
					
				}

			}

		};
		this.back = function(){
			window.document.form_data.action = "/th/jsp/sysMang/userMang/userSearch.html";
			window.document.form_data.submit();
		}
		
	};	
	var btnOper = new btnOperations();

</SCRIPT>
</head>
<body>

	<div class="x-title"><span>&nbsp;&nbsp;<%=pageTitle %></span></div>
	<div></div>
	<form class="x-client-form" method="POST" name="form_data" action="">
		<input type="hidden" name="roleList" value=""/>
		<input type="hidden" name="curAllRoles" value=""/>
		<input type="hidden" name="dealFlg" value=""/>
		<input type="hidden" name="hide_user_id" value=""/>
		<input type="hidden" name="hide_org_select" value=""/>
		<input type="hidden" name="sel_org_id" value=""/>
		<input type="hidden" name="input_dpt_id" value=""/>
		<input type="hidden" name="input_user_nickname" value=""/>
		<input type="hidden" name="input_user_password" value=""/>
		<input type="hidden" name="input_user_name" value=""/>
		<input type="hidden" name="input_user_email" value=""/>
		<input type="hidden" name="input_user_fixedTel" value=""/>
		<input type="hidden" name="input_user_othCont" value=""/>
		<input type="hidden" name="input_user_desp" value=""/>
		<input type="hidden" name="input_user_type" value=""/>

		<div style="height: 520px; float: left;">
			<table style="width: 500px" class="x-data-table">
				<tr>
					<th style="width: 45%" class="x-data-table-th">所属组织</th>
					<td class="x-data-table-td"><select id="SelectOrg" name="select" style="width:270px" <% out.print(acTurn); %> onChange="getDpts()" ><%=orgSelect%></select></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">所在部门</th>
					<%
						if("add".equals(operaType)||"change".equals(operaType)){
							out.print("<td class='x-data-table-td'><span id='dptAjaxSel' " + acTurn + "><select id='SelectAjax' name='selectajax'><option value='-1'>请先点选组织下拉列表</select></span></td>");
						}else if("auth".equals(operaType)){
							out.print("<td class='x-data-table-td'><input type='text' size='40' name='dptName' id='dptName' disabled='disabled' value='" + selDptName + "' /></td>");
						}
					%>
					<%-- <td class="x-data-table-td"><input type="text" size="40" name="dptName" id="dptName" <% out.print(userInfoShow); %> value="<%=selDptName%>" onkeypress="returnCancel(event);"/></td> --%>
					<!-- <td class="x-data-table-td"><span id="dptAjaxSel"><select id="SelectAjax" name="selectajax"><option value="-1">请先点选组织下拉列表</select></span></td> -->
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">用户账号[*]</th>
					<%
						if("1".equals(userConfirmFlg)){
							out.print("<td class='x-data-table-td'><input type='text' size='40' name='nickName' id='nickName' " + acTurn + " value='" + selNickName + "' /></td>");
						}else{
							out.print("<td class='x-data-table-td'>" + selNickName + "</td>");
						}
					%>
					<%-- <td style="float: left"><input type="text" size="40" name="nickName" id="nickName" <% out.print(userInfoShow); %> value="<%=selNickName%>" onkeypress="returnCancel(event);"/></td> --%>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">用户姓名[*]</th>
					<td class="x-data-table-td"><input type="text" size="40" name="userName" id="userName" <% out.print(acTurn); %> value="<%=selUserName%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">用户口令[*]</th>
					<td class="x-data-table-td"><input type="password" style="width:267px" name="passWord" id="passWord" <% out.print(acTurn); %> value="<%=selPassword%>" /></td>
				</tr>
				<%
					if("1".equals(userConfirmFlg)){
						out.print("<tr><th style='width: 45%' class='x-data-table-th'>用户确认口令[*]</th>");
						out.print("<td class='x-data-table-td'><input type='password' style='width:267px' name='confirmPWD' id='confirmPWD' " + acTurn + " value='" + selPassword + "' /></td></tr>");
					}
				%>
				<tr>
					<th style="width: 45%" class="x-data-table-th">邮件地址[*]</th>
					<td class="x-data-table-td"><input type="text" size="40" name="email" id="email" <% out.print(acTurn); %> value="<%=selEmail%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">联系方式</th>
					<td class="x-data-table-td"><input type="text" size="40" name="fixedTel" id="fixedTel" <% out.print(acTurn); %> value="<%=selFixedTel%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">其他联系方式</th>
					<td class="x-data-table-td"><input type="text" size="40" name="otherContacts" id="otherContacts" <% out.print(acTurn); %> value="<%=selOthCont%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">用户描述</th>
					<td class="x-data-table-td"><input type="text" size="40" name="description" id="description" <% out.print(acTurn); %> value="<%=selDesp%>" /></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">用户类型</th>
					<td class="x-data-table-td">
						<select id="userType" name="userType" style="width:270px" <% out.print(acTurn); %> onChange="getRoleList()" >
							<option value="0">普通用户</option>
							<option value="1">管理员</option>
						</select></td>
				</tr>
				<tr>
					<th style="width: 45%" class="x-data-table-th">用户状态</th>
					<td class="x-data-table-td">
						<select id="userStatus" name="userStatus" style="width:270px" disabled="disabled" >
							<option value="1">启用</option>
							<option value="0">关闭</option>
						</select></td>
				</tr>
			</table>
			<div class="x-client-form">
				<%
					if(!"".equals(acTurn)){
						out.print("<input class='tableBtn' type='button' name='button_Enabled' id='btnEnabled' value='编辑' onclick='btnOper.enabled()' />");
					}
				%> 
	    		<input class="tableBtn" type="button" name="button_Save" id="btnSave" value="保存" onclick="btnOper.save()" /> 
	    		<input class="tableBtn" type="button" name="button_Back" id="btnBack" value="返回" onclick="btnOper.back()" /> 
	  		</div>
		</div>
		<div style="height: 520px; float: right;">
			<div id="roleListAjaxCheck" style="float: left; border-style: solid; border-width: 1px; border-color: #000000; overflow: scroll; width: 260px; height: 50%">
				<%=dealRoleList %>
			</div>
		</div>
	</form>

</body>
</html>