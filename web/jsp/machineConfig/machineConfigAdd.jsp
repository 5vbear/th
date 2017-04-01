<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%> 
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macConfigAdd.jsp";
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

    String machineID = StringUtils.transStr((String)request.getAttribute("machineID"));
    logger.info("machineID = " + machineID);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="./css/channel.css">
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/style.css"/>
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
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
		<title>端机配置</title>
	</head>
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
	<SCRIPT  language="JavaScript" type="text/javascript">
	<!-- Begin
		var checkflag = "false";
		var  flag1 =false;
		var  flag2 =false;
		var  flag3 =false;
		var  flag4 =false;
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
		
		/*添加函数ckradio()
		 *添加人：张沛光
		 *功能：判断提交时是否有未选中的radio项
		 */
		function ckradioconfig(){
			 var name = document.getElementsByName("config");
			 var resualt = false;		 
				for (var i=0; i < name.length; i++) {	
					if(name.item(i).checked){
						resualt=true;				
					}
				}
				if(!resualt){
						alert(" 请选择配置类别！");
						return;
				}		
		}
		
		function ckradioitem(){
			 var name = document.getElementsByName("item");
			 var resualt = false;
				for (var i=0; i < name.length; i++) {	
					if(name.item(i).checked){
						resualt=true;
					}
				}
				if(!resualt){
						alert(" 请选择项类别！");
						return;
				}	
		}
		
		function ckradioitemName(){
			var name = document.getElementsByName("itemName");
			var resualt = false; 
			for (var i=0; i < name.length; i++) {	
				if(name.item(i).checked){
					resualt=true;
				}
			}
			if(!resualt){
					alert(" 请选择项名称！");
					return;
			}	
		}
		/*****/
		
		function JTrim1(s) {
			return s.replace(/(^\s*)|(\s*$)/g, "");
			          }
			  
		function JTrim2(s) {
			return s.replace(/\s+/g, "");
			          }
		
		

		function saveData(){
		   window.document.configForm.target = "_self";
		   
			/*张沛光修改
			 *原先只判断配置名称是否为空
			 *修改后：加入对配置描述和配置路径的判断
			 */
		   var name1 = document.getElementById("mdname");
		   if(JTrim1(name1.value)==""){
			   alert("请输入配置名称！");
			   return;
		   }
		   
		   var name2 = document.getElementById("mdurl");
		   if(JTrim1(name2.value)==""){
			   alert("请输入配置路径！");
			   return;
		   }
		   /****/
		   /*张沛光修改
			 *判断每一个radio是否选择过
			 */
		   //var flag = -1;
		   ckradioconfig();
		   ckradioitem();
		   ckradioitemName();
		   /****/

		   
		   var screenCopyDuration = document.getElementById("screenCopyDuration").value;
		   var screenCopyInterval = document.getElementById("screenCopyInterval").value;
		   var time = document.getElementById("time").value;
		   var protime = document.getElementById("protime").value;
		   
		   if(protime!=null){
				if(!isNaN(protime)){
					
				}else{
				   alert("屏幕保护时间请输入数字！");
				   return;
				}
				}   
		   
		   if(screenCopyDuration!=null){
			if(!isNaN(screenCopyDuration)){
				
			}else{
			   alert("截屏时间请输入数字！");
			   return;
			}
			}   
		   
		   if(screenCopyInterval!=null){
				if(!isNaN(screenCopyInterval)){
				}else{
				   alert("截屏时间间隔请输入数字！");
				   return;
				}
				}
		   
		   if(time!=null){
				if(!isNaN(time)){
				}else{
				   alert("心跳时间请输入数字！");
				   return;
				}
				}
		    window.document.configForm.submit();
		}
		
		function cancel(){
			self.location = "/th/jsp/machineConfig/machineConfigSeach.html?pageId=jsp_machine_search_id";
		}
		
		function selectConfig(){

			var config = document.getElementsByName("config");

			var myFavorite;
			for (var i=0; i < config.length; i++) {
				if (config.item(i).checked) {
					 myFavorite = config.item(i).getAttribute("value");
					
					 break;
				   }
			 } 					
			setNull();
			 switch (myFavorite)
 			{
 				case '1':
 					document.getElementById("type11").disabled=false;
 					document.getElementById("type12").disabled=false;
 					document.getElementById("type13").disabled=false;
 					document.getElementById("type21").disabled=true;
 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("motime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("type21").checked = false;

 					document.getElementById("type111").checked=false;
 					document.getElementById("type112").checked=false;
 					document.getElementById("type113").checked=false;
 					document.getElementById("type114").checked=false;
 					document.getElementById("type115").checked=false;
 					document.getElementById("type116").checked=false;
 					document.getElementById("type117").checked=false;
 					document.getElementById("type118").checked=false;
 					document.getElementById("type119").checked=false;
			 	break;
 				case '2':
 					document.getElementById("type21").disabled=false;
 					document.getElementById("type11").disabled=true;
 					document.getElementById("type12").disabled=true;
 					document.getElementById("type13").disabled=true;

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=true;
 					document.getElementById("type11").checked = false;
 					document.getElementById("type12").checked = false; 		
 					document.getElementById("type13").checked = false;

 					document.getElementById("type111").checked=false;
 					document.getElementById("type112").checked=false;
 					document.getElementById("type113").checked=false;
 					document.getElementById("type114").checked=false;
 					document.getElementById("type115").checked=false;
 					document.getElementById("type116").checked=false;
 					document.getElementById("type117").checked=false;
 					document.getElementById("type118").checked=false;
 					document.getElementById("type119").checked=false;

 				 break;
 				default:
 			}
			 				}
		
		
		function selectItem(){

			var config = document.getElementsByName("item");
			var myFavorite;
			for (var i=0; i < config.length; i++) {
				if (config.item(i).checked) {
					 myFavorite = config.item(i).getAttribute("value");
					 break;
				   }
			 }
			setNull();
			 switch (myFavorite)
 			{
 				case '1':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;					
 					document.getElementById("motime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("type111").disabled=false;
 					document.getElementById("type112").disabled=false;
 					document.getElementById("type113").disabled=true;
 					document.getElementById("type114").disabled=true;
 					document.getElementById("type115").disabled=true;
 					document.getElementById("type116").disabled=true;
 					document.getElementById("type117").disabled=true;
 					document.getElementById("type118").disabled=true;
 					document.getElementById("type119").disabled=true;

			 	break;
 				case '2':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true
 					document.getElementById("motime").disabled=true;
 					document.getElementById("type111").disabled=true;
 					document.getElementById("type112").disabled=true;
 					document.getElementById("type113").disabled=false;
 					document.getElementById("type114").disabled=true;
 					document.getElementById("type115").disabled=true;
 					document.getElementById("type116").disabled=true;
 					document.getElementById("type117").disabled=true;
 					document.getElementById("type118").disabled=true;
 					document.getElementById("type119").disabled=false;
 				 break;
 				case '3':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=true;
 					
 					document.getElementById("type111").disabled=true;
 					document.getElementById("type112").disabled=true;
 					document.getElementById("type113").disabled=true;
 					document.getElementById("type114").disabled=false;
 					document.getElementById("type115").disabled=false;
 					document.getElementById("type116").disabled=true;
 					document.getElementById("type117").disabled=true;
 					document.getElementById("type118").disabled=true;
 					document.getElementById("type119").disabled=true;
 				 break;
 				case '4':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=true;
 					
					
 					document.getElementById("type111").disabled=true;
 					document.getElementById("type112").disabled=true;
 					document.getElementById("type113").disabled=true;
 					document.getElementById("type114").disabled=true;
 					document.getElementById("type115").disabled=true;
 					document.getElementById("type116").disabled=false;
 					document.getElementById("type117").disabled=false;
 					document.getElementById("type118").disabled=false;
 					document.getElementById("type119").disabled=true;
 				 break;
 				default:
 			}
			 				}
		
		function selectItemName(){

			var config = document.getElementsByName("itemName");
			var myFavorite;
			for (var i=0; i < config.length; i++) {
				if (config.item(i).checked) {
					 myFavorite = config.item(i).getAttribute("value");
					 break;
				   }
			 }
			setNull();
			 switch (myFavorite)
 			{
 				case '0':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=false;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=false;

			 	break;
 				case '1':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=false;
 					document.getElementById("hctime").disabled=false;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=true;

 				 break;
 				case '2':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=false;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=true;

 				 break;
 				case '3':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=false;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=true;

 				 break;
 				case '4':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=false;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=true;

 				 break;
 				case '5':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=false;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					
 					document.getElementById("motime").disabled=true;

 				 break;
 				case '6':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=false;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=true;

 				 break;
 				case '7':

 					document.getElementById("serverIp").disabled=false;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=true;

 				 break;
 				case '8':

 					document.getElementById("serverIp").disabled=true;
 					document.getElementById("serverUrl").disabled=true;
 					document.getElementById("screenCopyInterval").disabled=true;
 					document.getElementById("screenCopyDuration").disabled=true;
 					document.getElementById("propath").disabled=true;
 					document.getElementById("protime").disabled=true;
 					document.getElementById("mctime").disabled=true;
 					document.getElementById("hctime").disabled=true;
 					document.getElementById("hotime").disabled=true;
 					document.getElementById("time").disabled=true;
 					document.getElementById("motime").disabled=true;
 					document.getElementById("time").disabled=false;
 				 break;
 				default:
 			}
			 				}
		function setNull(){
					document.getElementById("serverIp").value="";
					document.getElementById("serverUrl").value="";
					document.getElementById("screenCopyInterval").value="";
					document.getElementById("screenCopyDuration").value="";
					document.getElementById("propath").value="";
					document.getElementById("protime").value="";
					document.getElementById("mctime").value="";
					document.getElementById("hctime").value="";
					document.getElementById("hotime").value="";
					document.getElementById("time").value="";
					document.getElementById("motime").value="";
			
	}
		

	</script>
	<body style="background-color: #fff;">
		<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
	    	<div class="x-chanelName" style="width:100%;padding-top:3px">
		    	<input type="button" class="leftBtn" value="保存" onclick="saveData()"/>
		    	<input type="button" class="leftBtn" value="取消" onclick="cancel()"/>
		    </div>
		</div>
	    <div>
			<form name="configForm" action="/th/jsp/machineConfig/machineConfigSeach.html?pageId=jsp_machine_add_id&funcId=func_material_add_id" method=post>
				<table id="mytable" cellspacing="0">						
					<tr>
						<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">配置信息:</td>
					</tr>
					<tr>
						<td class="row">配置名称</td>
						<td class="row"><input type="text" name="mdname" id="mdname" style="width:350px"/></td>
					</tr>
					<tr>
						<td class="row">配置描述</td>
						<td class="row"><input type="text" name="mddesc" id="mddesc" style="width:350px"/></td>
					</tr>
					<tr>
						<td class="row">配置路径</td>
						<td class="row"><input type="text" name="mdurl" id="mdurl" style="width:350px"/></td>
					</tr>
					<tr>
						<td class="row">配置类别</td>
						<td class="row">时间相关：<input type ="radio" onclick="selectConfig()" name= "config" id= "type1" value ="1" />&nbsp;&nbsp;
								
										路径相关：<input type ="radio"  onclick="selectConfig()" name= "config" id= "type2" value ="2"/>
						</td>
					</tr>
					<tr>
						<td class="row">项类别</td>
						<td class="row">开  关   机：<input type ="radio" onclick="selectItem()" name= "item" id= "type11" value ="1" disabled/>&nbsp;&nbsp;
										屏&nbsp;&nbsp;保：<input onclick="selectItem()" type ="radio" name= "item" id= "type12" value ="2"  disabled/>&nbsp;&nbsp;
										截&nbsp;&nbsp;屏：<input onclick="selectItem()" type ="radio" name= "item" id= "type13" value ="3"  disabled/>&nbsp;&nbsp;
										路径相关：<input onclick="selectItem()" type ="radio" name= "item" id= "type21" value ="4"  disabled/>
						
						</td>
					</tr>
					<tr>
						<td class="row">项名称</td>
						<td class="row">
										开&nbsp;&nbsp;机：<input onclick="selectItemName()" type ="radio"  name= "itemName" id= "type111" value ="0" disabled/>&nbsp;&nbsp;
										关&nbsp;&nbsp;机：<input onclick="selectItemName()" type ="radio" name= "itemName" id= "type112" value ="1"  disabled/>&nbsp;&nbsp;
										屏&nbsp;&nbsp;保：<input onclick="selectItemName()" type ="radio" name= "itemName" id= "type113" value ="2"  disabled/>&nbsp;&nbsp;
										截屏时间：<input onclick="selectItemName()" type ="radio" name= "itemName" id= "type114" value ="3"  disabled/>&nbsp;&nbsp;
										截屏间隔：<input onclick="selectItemName()" type ="radio" name= "itemName" id= "type115" value ="4"  disabled/>&nbsp;&nbsp;
										写保护目录：<input onclick="selectItemName()" type ="radio" name= "itemName" id= "type116" value ="5"  disabled/>&nbsp;&nbsp;
										应用服务器地址：<input onclick="selectItemName()" type ="radio" name= "itemName" id= "type117" value ="6"  disabled/>&nbsp;&nbsp;
										FTP服务器IP：<input onclick="selectItemName()" type ="radio" name= "itemName" id= "type118" value ="7"  disabled/>&nbsp;&nbsp;
										心跳设定：<input onclick="selectItemName()" type ="radio" name= "itemName" id= "type119" value ="8"  disabled/>
						 </td>
					</tr>
					<tr>
						<td class="row">开机时间</td>
						<td class="row">
							<select style="width:40px;" id="hotime" name="hotime" disabled>
								<option value="-1" selected></option>
								<option value="00">00</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
							</select>
							<b>:</b>
							<select style="width:40px;" id="motime" name="motime" disabled>
								<option value="-1" selected></option>
								<option value="00">00</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
								<option value="24">24</option>
								<option value="25">25</option>
								<option value="26">26</option>
								<option value="27">27</option>
								<option value="28">28</option>
								<option value="29">29</option>
								<option value="30">30</option>
								<option value="31">31</option>
								<option value="32">32</option>
								<option value="33">33</option>
								<option value="34">34</option>
								<option value="35">35</option>
								<option value="36">36</option>
								<option value="37">37</option>
								<option value="38">38</option>
								<option value="39">39</option>
								<option value="40">40</option>
								<option value="41">41</option>
								<option value="42">42</option>
								<option value="43">43</option>
								<option value="44">44</option>
								<option value="45">45</option>
								<option value="46">46</option>
								<option value="47">47</option>
								<option value="48">48</option>
								<option value="49">49</option>
								<option value="50">50</option>
								<option value="51">51</option>
								<option value="52">52</option>
								<option value="53">53</option>
								<option value="54">54</option>
								<option value="55">55</option>
								<option value="56">56</option>
								<option value="57">57</option>
								<option value="58">58</option>
								<option value="59">59</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="row">关机时间</td>
						<td class="row">
							<select style="width:40px;" id="hctime" name="hctime" disabled>
								<option value="-1" selected></option>
								<option value="00">00</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
							</select>
							<b>:</b>
							<select style="width:40px;" id="mctime" name="mctime" disabled>
								<option value="-1" selected></option>
								<option value="00">00</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08">08</option>
								<option value="09">09</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
								<option value="13">13</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="16">16</option>
								<option value="17">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
								<option value="24">24</option>
								<option value="25">25</option>
								<option value="26">26</option>
								<option value="27">27</option>
								<option value="28">28</option>
								<option value="29">29</option>
								<option value="30">30</option>
								<option value="31">31</option>
								<option value="32">32</option>
								<option value="33">33</option>
								<option value="34">34</option>
								<option value="35">35</option>
								<option value="36">36</option>
								<option value="37">37</option>
								<option value="38">38</option>
								<option value="39">39</option>
								<option value="40">40</option>
								<option value="41">41</option>
								<option value="42">42</option>
								<option value="43">43</option>
								<option value="44">44</option>
								<option value="45">45</option>
								<option value="46">46</option>
								<option value="47">47</option>
								<option value="48">48</option>
								<option value="49">49</option>
								<option value="50">50</option>
								<option value="51">51</option>
								<option value="52">52</option>
								<option value="53">53</option>
								<option value="54">54</option>
								<option value="55">55</option>
								<option value="56">56</option>
								<option value="57">57</option>
								<option value="58">58</option>
								<option value="59">59</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="row">屏幕保护时间</td>
						<td class="row"><input type="text" name="protime"  id ="protime"  style="width:50px;text-align:right;padding-right:2px" disabled/>(单位:秒)</td>
					</tr>
					<tr>
						<td class="row">写保护目录</td>
						<td class="row"><input type="text" name="propath"  id ="propath"  style="width:350px" disabled/></td>
					</tr>
					<tr>
						<td class="row">截屏时间</td>
						<td class="row"><input type="text" name="screenCopyDuration"  id ="screenCopyDuration" style="width:350px" disabled/></td>
					</tr>
					<tr>
						<td class="row">截屏间隔时间</td>
						<td class="row"><input type="text" name="screenCopyInterval" id ="screenCopyInterval" style="width:350px" disabled/>(单位:秒)</td>
					</tr>
					<tr>
						<td class="row">应用服务器地址</td>
						<td class="row"><input type="text" name="serverUrl" id ="serverUrl" style="width:350px" disabled/></td>
					</tr>
					<tr>
						<td class="row">FTP服务器IP</td>
						<td class="row"><input type="text" name="serverIp" id ="serverIp" style="width:350px" disabled/></td>
					</tr>
					
					<tr>
						<td class="row">心跳时间</td>
						<td class="row"><input type="text" name="time" id ="time" style="width:350px" disabled/></td>
					</tr>
				</table>
			</form>
    	</div>
	</body>
</html>