<%@page import="th.com.util.Define"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%> 
<%
String strContextPath = request.getContextPath();
String defaultStyle = strContextPath + "/css/advert.css";
String url = strContextPath + "/AdvertServlet";
String result = (String)request.getAttribute("result");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>区域授权</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
		<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script type="text/javascript" src="./zTree/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="./zTree/js/jquery.ztree.excheck-3.5.js"></script>
		<script language="JavaScript">
			//页面初始化
			function onload() {
				var message = "<%=result%>";
				if (message == null || message == "" || message == "null") {
					return;
				}
				alert(message);
			}
			var cur_id="";
			var flag=0,sflag=0;
			//组织结构下拉菜单
			var setting = {
			    view: {  
			        dblClickExpand: false  
			    },  
			    data: {  
			        simpleData: {  
			            enable: true  
			        }  
			    },  
			    callback: {  
			        //beforeClick: beforeClick,//(点击之前)用于捕获 勾选 或 取消勾选 之前的事件回调函数，并且根据返回值确定是否允许 勾选 或 取消勾选   
			        onClick: treeSelected 
			    }  
			};  
			var zTreeNodes;
			var zTree;
			$(function() {
				zTreeNodes = <%=(String)request.getAttribute( "orgsList" )%> ;
			}); 
			
			$(document).ready(function(){  
			    //$.fn.zTree.init($("#menu"), setting, zTreeNodes);  
			    intiTree();
			});

			function intiTree(){
				zTreeObj = $.fn.zTree.init($("#menu"), setting, zTreeNodes);
				var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
				// 设置ztree不同层级的节点显示图标 
				for (var i=0; i<nodes.length; i++) {
					var level = 0;
					level = nodes[i].level;
					if(level>4) continue;
					if(nodes[i].mactype=='mac'){
						if(nodes[i].isParent){
							nodes[i].iconSkin = "pIcon04";
						}else{
							nodes[i].iconSkin = "icon04";
						}
					}else{
						level = level + 1;
						if(nodes[i].isParent){
							nodes[i].iconSkin = "pIcon0" + level;
						}else{
							nodes[i].iconSkin = "icon0" + level;
						}
					}
					zTreeObj.updateNode(nodes[i]);
				}
				zTreeObj.expandNode(zTreeObj.getNodeByTId('1'),true);
			}
			
			function treeSelected(event, treeId, treeNode){
				document.programform.orgId.value = treeNode.id;
			}	

			function submitform(){
				if(document.programform.orgId.value == ""){
					alert("请先在组织树中点选节点，然后再进行授权操作!");
					return;
				}
				if(document.programform.billId.value == ""){
					alert("请先选择节目单，然后再进行授权操作!");
					return;
				}
				
				window.document.programform.pageId.value = "<%=Define.JSP_REGIONAL_AUTH_ID%>";
				window.document.programform.funcId.value = "<%=Define.FUNC_REGIONAL_AUTH_ID%>";
				window.document.programform.submit();
			}
			function regionalSuccess(){
				
					
					alert("授权成功");
					self.location ="empower.jsp";					
				
				
			}
			//改变布局类型
			function changeScreen(id){
				if(id == "" || id == 0){
					document.getElementById("screen_top").style.display = "none";
				}else{
					document.getElementById("screen_top").style.display = "";
				}
				if(id == 1){
					document.getElementById("screen1").style.display = "";
					document.getElementById("screen2").style.display = "none";
					document.getElementById("screen4").style.display = "none";
				}else if(id == 2){
					document.getElementById("screen1").style.display = "none";
					document.getElementById("screen2").style.display = "";
					document.getElementById("screen4").style.display = "none";
				}else if(id == 4){
					document.getElementById("screen1").style.display = "none";
					document.getElementById("screen2").style.display = "none";
					document.getElementById("screen4").style.display = "";
				}
			}	

			function change(v){
				if (v == ""){
					document.getElementById("screen_top").style.display = "none";
					return;
				}
				document.programform.billId.value=v;
				xmlHttp = this.getXmlHttpRequest();
				if(xmlHttp == null) {
				    alert("Explore is Unsupport XmlHttpRequest！");
				    return;
				}
				var url = "<%= url %>"+"?pageId=jsp_regional_auth_id&funcId=func_get_layout_info&billId="+v;
				xmlHttp.open("GET", url, true);
				xmlHttp.onreadystatechange = this.callBack;
				xmlHttp.send(null);
			}

			function getXmlHttpRequest() {
			    try {
			        // Firefox, Opera 8.0+, Safari
			        xmlHttp = new XMLHttpRequest();
			    } catch (e) {
			        // Internet Explorer
			        try {
			            xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
			        } catch (e) {
			            try {
			                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			            } catch (e) {
			                alert("XMLHTTP is null");
			                return false;
			            }
			        }
			    }

			    return  xmlHttp; 
			}

			function callBack() {
			    if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			        var res = xmlHttp.responseText;
			        if(res != ""){
			        	var attributes = res.split(",");
			        	if(attributes[2] =="" || attributes[2] == 0){
			        		alert("暂无该节目单的布局信息！");
			        	}
			        	changeScreen(attributes[2]);
			        } else {
			        	alert("暂无该节目单的布局信息！");
			        }
			    }
			}
			//返回检索画面
			function returnSearch() {
				window.document.programform.pageId.value = "<%=Define.JSP_PROGRAMLIST_SEARCH_ID%>";
				window.document.programform.funcId.value = "";
				window.document.programform.submit();
			}	
		</script>
	</head>
	<body onload="onload();">
	<fieldset><legend>广告管理-区域授权</legend>
	<%
	
	HashMap[] programList = (HashMap[]) session.getAttribute("programList");
	String layoutName = (String)request.getAttribute("layoutName");
	
	
%>
		<div id="body" style="height:580; width:250; float: left;overflow:scroll;overflow-x:hidden"  class="sdmenu" > 
    		<ul id="menu" class="ztree" style="margin-top:0; width:200px;"></ul>
		</div>
		<div>
				<form name="programform" action="/th/AdvertServlet" method=post>
		<table style="left" >
			<tr>
				<td>节目单：</td>
				<td>
					<select id="programListSelect" onchange="change(this.value)">
						<option value=""></option>
					<%
						for( int i=0; i<programList.length; i++ ){					
					%>
						<option value="<%=programList[i].get("BILL_ID") %>"><%=programList[i].get("BILL_NAME") %></option>
					<%	
						}
					%>
						
					</select>
				</td>
			</tr>
			<!--<tr>
			<td>布局：</td>
			<td>
				<select disabled>
					<option value="1">布局1</option>
					<option value="2">布局2</option>
					<option value="3">布局3</option>
					<option value="4">布局4</option>
				</select>
			</td>
			</tr>
			-->
			<tr>
				<td><input type="button" value="授权" onclick="submitform();"/></td>
				<td><input type="button" value="返回" onclick="returnSearch();"/></td>
			</tr>
		</table>
		<table>
			<tr>
				<td>
					<div id="screen_top" style="display: none;">
					<div id="screen1" style="width: 410px; height: 307px;">
						<table width="100%">
							<tr>
								<td><img alt="一分屏" src="./image/advert/single_big.png"></td>
							</tr>
						</table>
					</div>
					<div id="screen2" style="width: 410px; height: 307px; display: none;" >
						<table width="100%">
							<tr>
								<td><img alt="二分屏" src="./image/advert/double_big.png"></td>
							</tr>
						</table>
					</div>
					<div id="screen4" style=";width: 410px; height: 307px; display: none;">
						<table width="100%">
							<tr>
								<td><img alt="四分屏" src="./image/advert/four_big.png"></td>
							</tr>
						</table>
					</div>
					</div>
				</td>
			</tr>
		</table>
		<input type="hidden" name="orgId" value="">
		<input type="hidden" name="billId" value="">
		<input type="hidden" name="pageId" value="jsp_regional_auth_id">
		<input type="hidden" name="funcId" value="func_regional_auth_id">
	</form>
	
	<form name="form_data">
		<input type="hidden" name="pageId" value="jsp_regional_auth_id">
		<input type="hidden" name="funcId" value="func_programlist_auth_id">
	</form>
		</div>
	</fieldset>
	</body>
</html>