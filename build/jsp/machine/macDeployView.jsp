<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%> 
<%@ page import="java.util.*" %>
<%@ page import="th.com.util.Define" %>
<%@ page import="th.util.StringUtils" %>
<%@ page import="th.com.property.LocalProperties" %>
<%@ page import="th.user.User"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%
    Log logger = LogFactory.getLog(this.getClass());
    String jspName = "macDeployView.jsp";
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
		<link href="./css/machine.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="./css/advert.css">
		<link rel="stylesheet" type="text/css" href="./css/channel.css">
		<link rel="stylesheet" type="text/css" href="./css/monitor.css">
		<link rel="stylesheet" type="text/css" href="./css/sdmenu.css" />
		<link rel="stylesheet" type="text/css" href="./css/style.css"/>
		<link rel="stylesheet" type="text/css" href="./css/menu.css"/>
		<link rel="stylesheet" type="text/css" href="./css/machine.css">
		<link rel="stylesheet" href="./zTree/css/demo.css" type="text/css">
		<link rel="stylesheet" href="./zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<title>端机信息</title>
	</head>
	<style type="text/css">
		body {
			font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica,
				sans-serif;
			color: #4f6b72;
			background: #E6EAE9;
		}
		
		a {
			color: #c75f3e;
		}
		
		.myrtable {
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
		.widthDiv{ float:left; width: 700px;}
		.mydiv{ float:left; width: 700px;}
		.mydiv ul li{ float:left; padding-left:5px;}
		.mytable{ float:left; width: 700px;}
		#mytable1{
			width: 700px;
			padding: 0;
			margin: 0;
		}
	</style>
	<%!
		private String parseStr(Object obj){
			if(obj != null){									
				return obj.toString();
			}else{
				return "";
			}
		}
	%>
	<% 
		HashMap deployInfo = (HashMap) request.getAttribute( "deployInfo" );
		HashMap moreInfo = (HashMap) request.getAttribute( "moreInfo" );

		String hotime = (String)deployInfo.get("HOTIME");
		String motime = (String)deployInfo.get("MOTIME");
		String hctime = (String)deployInfo.get("HCTIME");
		String mctime = (String)deployInfo.get("MCTIME");
		String orgId = (String)deployInfo.get("OID");
		if(hotime==null){
			hotime = "08";
		}
		if(motime==null){
			motime = "30";
		}
		if(hctime==null){
			hctime = "17";
		}
		if(mctime==null){
			mctime = "30";
		}
	%>
	<SCRIPT LANGUAGE="JavaScript">
	
		window.onload = function showtable() {
			document.getElementById('hotime').value = '<%=hotime %>';	
			document.getElementById('motime').value = '<%=motime %>';	
			document.getElementById('hctime').value = '<%=hctime %>';	
			document.getElementById('mctime').value = '<%=mctime %>';	
			document.getElementById('orgId').value = '<%=orgId %>';	
			//var tablename = document.getElementById("mytable");
			var li = document.getElementsByTagName("tr");
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
		
		function showTab(tabid){
			var tab1 = document.getElementById("tab1");
			var tab2 = document.getElementById("tab2");
			var tab3 = document.getElementById("tab3");
			var tab4 = document.getElementById("tab4");
			var tab5 = document.getElementById("tab5");
			var tab6 = document.getElementById("tab6");
			tab1.style.display = 'none';
			tab2.style.display = 'none';
			tab3.style.display = 'none';
			tab4.style.display = 'none';
			tab5.style.display = 'none';
			tab6.style.display = 'none';
			document.getElementById(tabid).style.display = '';
		}
		
		function saveData(){
			var bname = document.getElementById('bname').value;
		    window.document.deployForm.target = "_self";
		    window.document.deployForm.submit();
		}
		
		function cancel(){
			var pageIdx = document.getElementById('pageIdx').value;
			self.location = '/th/machineServlet?model=deploy&method=query&pageIdx=' + pageIdx;
		}
		
		function editable(){
			document.getElementById('saveData').style.display = '';
			document.getElementById('unEditable').style.display = '';
			document.getElementById('editable').style.display = 'none';
			document.getElementById('orgId').disabled = false;
			document.getElementById('bname').disabled = false;
			document.getElementById('baddr').disabled = false;
			document.getElementById('bno').disabled = false;
			document.getElementById('ryears').disabled = false;
			document.getElementById('maname').disabled = false;
			document.getElementById('coname').disabled = false;
			document.getElementById('tphone').disabled = false;
			document.getElementById('cphone').disabled = false;
			document.getElementById('hotime').disabled = false;
			document.getElementById('motime').disabled = false;
			document.getElementById('hctime').disabled = false;
			document.getElementById('mctime').disabled = false;
			document.getElementById('odate').disabled = false;
			document.getElementById('llon').disabled = false;
			document.getElementById('llat').disabled = false;
		}
		
		function unEditable(){
			window.document.deployForm.reset();
			document.getElementById('saveData').style.display = 'none';
			document.getElementById('unEditable').style.display = 'none';
			document.getElementById('editable').style.display = '';
			document.getElementById('orgId').disabled = 'true';
			document.getElementById('bname').disabled = 'true';
			document.getElementById('baddr').disabled = 'true';
			document.getElementById('bno').disabled = 'true';
			document.getElementById('ryears').disabled = 'true';
			document.getElementById('maname').disabled = 'true';
			document.getElementById('coname').disabled = 'true';
			document.getElementById('tphone').disabled = 'true';
			document.getElementById('cphone').disabled = 'true';
			document.getElementById('hotime').disabled = 'true';
			document.getElementById('motime').disabled = 'true';
			document.getElementById('hctime').disabled = 'true';
			document.getElementById('mctime').disabled = 'true';
			document.getElementById('odate').disabled = 'true';
			document.getElementById('llon').disabled = 'true';
			document.getElementById('llat').disabled = 'true';
			//self.location = "/th/machineServlet?model=deploy&method=view&pageIdx="+${pageIdx}+"&macIdStd="+${macIdStd};
			
			
		}
		
	</script>
	<body style="background-color: #fff;">
		<div class="x-title"><span>&nbsp;&nbsp;端机管理-端机部署</span></div>
		<table><tr style ="height:3px"></tr></table>
		<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px; background-color:#B2DFEE">
	    	<div class="x-chanelName" style="width:100%;padding-top:3px">
		    	<input type="button" class="leftBtn" id="editable" value="编辑" onclick="editable()"/>
		    	<input type="button" class="leftBtn" id="unEditable" style="display:none" value="撤销" onclick="unEditable()"/>
		    	<input type="button" class="leftBtn" id="saveData" style="display:none" value="保存" onclick="saveData()"/>
		    	<input type="button" class="leftBtn" value="返回" onclick="cancel()"/>
		    </div>
		</div>
		<div class="widthDiv">
			<div class="mydiv">
		    	<table id="mytable1" cellspacing="0">
					<caption></caption>
					<tr>
						<th scope="col"><a href="javascript:showTab('tab1');">部署信息</a></th>
						<th scope="col"><a href="javascript:showTab('tab2');">档案信息</a></th>
						<th scope="col"><a href="javascript:showTab('tab3');">硬件信息</a></th>
						<th scope="col"><a href="javascript:showTab('tab4');">浏览器信息</a></th>
						<th scope="col"><a href="javascript:showTab('tab5');">报停信息</a></th>
		                <th scope="col"><a href="javascript:showTab('tab6');">迁移信息</a></th>	           
					</tr>
		        </table>
		    </div>
		    <div class="mytable" id="tab1">
				<form name="deployForm" action="/th/machineServlet?model=deploy&method=saveData&" method="post">
					<input type="hidden" id="pageIdx" name="pageIdx" value="${pageIdx}" />
					<input type="hidden" id="macIdStd" name="macIdStd" value="${macIdStd}" />
					<table class="myrtable" cellspacing="0">
						<caption></caption>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机部署信息:</td>
						</tr>
						<tr>
							<td class="row">端机ID:</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("MMARK")));  %></td>
						</tr>
						<tr>
							<td class="row">所属银行:</td>
							<td class="row">
								<select style="width:400px" id="orgId" name="orgId" disabled = "true">
									<%
										HashMap[] orgMaps = (HashMap[]) request.getAttribute( "orgMaps" ); 
										for ( int i = 0; i<orgMaps.length; i++ ) {
											out.print("<option value=" + orgMaps[i].get("OID") + ">--");
											out.print(orgMaps[i].get("ONAME"));
											out.print("</option>");
										}
									%>
								</select>
							</td>
						</tr>
						<tr>
							<td class="row">网点名称</td>
							<td class="row"><input type="text" id="bname" disabled="true" name="bname" value="<% out.print(parseStr(deployInfo.get("BNAME"))); %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">网点地址</td>
							<td class="row"><input type="text" id="baddr" disabled="true" name="baddr" value="<% out.print(parseStr(deployInfo.get("BADDRESS"))); %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">网点编号</td>
							<td class="row"><input type="text" id="bno" disabled="true" name="bno" value="<% out.print(parseStr(deployInfo.get("BNO"))); %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">保修年限</td>
							<td class="row"><input type="text" id="ryears" disabled="true" name="ryears" value="<% out.print(parseStr(deployInfo.get("RYEARS"))); %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">负责人</td>
							<td class="row"><input type="text" id="maname" disabled="true" name="maname" value="<% out.print(parseStr(deployInfo.get("MANAME"))); %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">联系人</td>
							<td class="row"><input type="text" id="coname" disabled="true" name="coname" value="<% out.print(parseStr(deployInfo.get("CONAME"))); %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">联系电话</td>
							<td class="row"><input type="text" id="tphone" disabled="true" name="tphone" value="<% out.print(parseStr(deployInfo.get("TPHONE"))); %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">联系手机</td>
							<td class="row"><input type="text" id="cphone" disabled="true" name="cphone" value="<% out.print(parseStr(deployInfo.get("CPHONE"))); %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">营业时间</td>
							<td class="row">
								<select style="width:40px;" id="hotime" disabled="true" name="hotime">
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
								<select style="width:40px;" id="motime" disabled="true" name="motime">
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
									<option value="25" selected="selected">25</option>
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
								</select>&nbsp;--
								<select style="width:40px;" id="hctime" disabled="true" name="hctime">
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
									<option value="18" selected="selected">18</option>
									<option value="19">19</option>
									<option value="20">20</option>
									<option value="21">21</option>
									<option value="22">22</option>
									<option value="23">23</option>
								</select>
								<b>:</b>
								<select style="width:40px;" id="mctime" disabled="true" name="mctime">
									<option value="00" selected="selected">00</option>
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
							<td class="row">营业周期</td>
							<td class="row"><input type="text" id="odate" disabled="true" name="odate" value="<% out.print(parseStr(deployInfo.get("ODATE"))); %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">经度</td>
							<td class="row"><input type="text" id="llon" disabled="true" name="llon" value="<% out.print(parseStr(deployInfo.get("LLON"))); %>" style="width:300px" /></td>
						</tr>
						<tr>
							<td class="row">纬度</td>
							<td class="row"><input type="text" id="llat" disabled="true" name="llat" value="<% out.print(parseStr(deployInfo.get("LLAT"))); %>" style="width:300px" /></td>
						</tr>
					</table>
				</form>
		    </div>
		    <div class="mytable" id="tab2" style="display:none">
				<table class="myrtable" cellspacing="0">
					<caption></caption>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机档案信息:</td>
						</tr>
						<tr>
							<td class="row" width="30%">设备类型:</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("MTYPE")));  %></td>
						</tr>
						<tr>
							<td class="row">出产日期</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("MDATE")));  %></td>
						</tr>
						<tr>
							<td class="row">设备厂商</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("MFACTORY")));  %></td>
						</tr>
						<tr>
							<td class="row">设备编号</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("DNO")));  %></td>
						</tr>
						<tr>
							<td class="row">保修年限</td>
							<td class="row"><% out.print(parseStr(deployInfo.get("RYEARS"))); %></td>
						</tr>
				</table>
		    </div>
		    <div class="mytable" id="tab3" style="display:none">
				<table class="myrtable" cellspacing="0">
					<caption></caption>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">端机硬件信息:</td>
						</tr>
						<tr>
							<td class="row" width="30%">CPU频率</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("CPUFREQ")));  %></td>
						</tr>
						<tr>
							<td class="row">操作系统</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("OS")));  %></td>
						</tr>
						<tr>
							<td class="row">硬盘容量</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("DSIZE")));  %></td>
						</tr>
						<tr>
							<td class="row">内存大小</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("MSIZE")));  %></td>
						</tr>
						<tr>
							<td class="row">总版本号</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("OVERSION")));  %></td>
						</tr>
				</table>
		    </div>
		    <div class="mytable" id="tab4" style="display:none">
				<table class="myrtable" cellspacing="0">
					<caption></caption>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">浏览器信息:</td>
						</tr>
						<tr>
							<td class="row" width="30%">浏览器名称:</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("BROWNAME")));  %></td>
						</tr>
						<tr>
							<td class="row">浏览器版本</td>
							<td class="row"><% out.print(parseStr(moreInfo.get("BROWVERSION")));  %></td>
						</tr>
				</table>
		    </div>
		    <div class="mytable" id="tab5" style="display:none">
				<table class="myrtable" cellspacing="0">
					<caption></caption>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">报停信息:</td>
						</tr>
						<%
							HashMap[] alertInfo = (HashMap[]) request.getAttribute( "alertInfo" ); 
							for ( int i = 0; i<alertInfo.length; i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row' width='30%'>" + alertInfo[i].get("PTIME") + "报停</td>");
								out.print("	 <td class='row'>" + alertInfo[i].get("RTIME") + "恢复</td>");
								out.print("</tr>");
							}
						%>
				</table>
		    </div>
		    <div class="mytable" id="tab6" style="display:none">
				<table class="myrtable" cellspacing="0">
					<caption></caption>
						<tr>
							<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">迁移历史:</td>
						</tr>
						<%
							HashMap[] historyInfo = (HashMap[]) request.getAttribute( "historyInfo" ); 
							for ( int i = 0; i<historyInfo.length; i++ ) {
								out.print("<tr>");
								out.print("	 <td class='row' width='30%'>" + historyInfo[i].get("TTIME") + "</td>");
								out.print("	 <td class='row'>" + historyInfo[i].get("TDEST") + "</td>");
								out.print("</tr>");
							}
						%>
				</table>
		    </div>
	    </div>
	</body>
</html>