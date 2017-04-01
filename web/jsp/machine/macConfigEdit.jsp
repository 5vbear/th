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
    String jspName = "macConfigEdit.jsp";
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
		HashMap cfgInfo = (HashMap) request.getAttribute( "cfgInfo" );

		String hotime = (String)cfgInfo.get("HOTIME");
		String motime = (String)cfgInfo.get("MOTIME");
		String hctime = (String)cfgInfo.get("HCTIME");
		String mctime = (String)cfgInfo.get("MCTIME");
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
	<!-- Begin
		var checkflag = "false";
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
			document.getElementById('hotime').value = '<%=hotime %>';	
			document.getElementById('motime').value = '<%=motime %>';	
			document.getElementById('hctime').value = '<%=hctime %>';	
			document.getElementById('mctime').value = '<%=mctime %>';			
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
		
		function saveData(){
			var paramers="dialogWidth:400px;DialogHeight:250px;status:no;help:no;unadorned:no;resizable:no;status:no;";  
			var url = "/th/jsp/machine/macConfigReason.jsp";
			var ret=window.showModalDialog(url,'',paramers);  
			if (ret == null || ret == "") {
				return;
			}
			document.getElementById('desc').value = ret;
			
		    window.document.configForm.target = "_self";
		    window.document.configForm.submit();
		}
		
		function editable(){
			document.getElementById('saveData').style.display = '';
			document.getElementById('unEditable').style.display = '';
			document.getElementById('editable').style.display = 'none';
			document.getElementById('hotime').disabled = false;
			document.getElementById('motime').disabled = false;
			document.getElementById('hctime').disabled = false;
			document.getElementById('mctime').disabled = false;
			document.getElementById('protime').disabled = false;
			document.getElementById('propath').disabled = false;
			document.getElementById('scrtime').disabled = false;
			document.getElementById('ivltime').disabled = false;
			document.getElementById('serverUrl').disabled = false;
			document.getElementById('ftpIp').disabled = false;
		}
		
		function unEditable(){
			window.document.configForm.reset();
			document.getElementById('saveData').style.display = 'none';
			document.getElementById('unEditable').style.display = 'none';
			document.getElementById('editable').style.display = '';
			document.getElementById('hotime').disabled = true;
			document.getElementById('motime').disabled = true;
			document.getElementById('hctime').disabled = true;
			document.getElementById('mctime').disabled = true;
			document.getElementById('protime').disabled = true;
			document.getElementById('propath').disabled = true;
			document.getElementById('scrtime').disabled = true;
			document.getElementById('ivltime').disabled = true;
			document.getElementById('serverUrl').disabled = true;
			document.getElementById('ftpIp').disabled = true;
			//self.location = '/th/machineServlet?model=config&method=goEdit&orgid=${orgid}";
		}
	</script>
	<body style="background-color: #fff;">
		<div id="foldId" style="width: 100%; display: block;height: 30px;line-height: 30px;padding:0; background-color:#B2DFEE">
	    	<div class="x-chanelName" style="width:100%;padding-top:3px">
		    	<input type="button" class="leftBtn" id="editable" value="编辑" onclick="editable()"/>
		    	<input type="button" class="leftBtn" id="unEditable" style="display:none" value="撤销" onclick="unEditable()"/>
		    	<input type="button" class="leftBtn" id="saveData" style="display:none" value="保存" onclick="saveData()"/>
		    </div>
		</div>
	    <div>
			<form name="configForm" action="/th/machineServlet?model=config&method=editData&pageIdx=${pageIdx}&orgid=${orgid}&cfgid=${cfgid}" method=post>
				<input type="hidden" name="orgid" value="${orgid }">
				<input type="hidden" id="desc" name="desc">
				<table id="mytable" cellspacing="0">						
					<tr>
						<td class="row" colspan="2" style="font-weight:bold;background-color:#EEE">配置信息:</td>
					</tr>
					<tr>
						<td class="row" width="30%">开机时间</td>
						<td class="row">
							<select style="width:40px;" id="hotime" name="hotime" disabled="true">
								<option value="00">00</option>
								<option value="01">01</option>
								<option value="02">02</option>
								<option value="03">03</option>
								<option value="04">04</option>
								<option value="05">05</option>
								<option value="06">06</option>
								<option value="07">07</option>
								<option value="08" selected="selected">08</option>
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
							<select style="width:40px;" id="motime" name="motime" disabled="true">
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
								<option value="30" selected="selected">30</option>
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
							<select style="width:40px;" id="hctime" name="hctime" disabled="true">
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
								<option value="17" selected="selected">17</option>
								<option value="18">18</option>
								<option value="19">19</option>
								<option value="20">20</option>
								<option value="21">21</option>
								<option value="22">22</option>
								<option value="23">23</option>
							</select>
							<b>:</b>
							<select style="width:40px;" id="mctime" name="mctime" disabled="true">
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
								<option value="30" selected="selected">30</option>
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
						<td class="row"><input type="text" id="protime" name="protime" disabled="true" value="<% out.print(parseStr(cfgInfo.get("PROTIME"))); %>" style="width:50px;text-align:right;padding-right:2px"/>(单位:秒)</td>
					</tr>
					<tr>
						<td class="row">写保护目录</td>
						<td class="row"><input type="text" id="propath" name="propath" disabled="true" value="<% out.print(parseStr(cfgInfo.get("PROPATH"))); %>" style="width:350px"/></td>
					</tr>
					<tr>
						<td class="row">截屏结束时间</td>
						<td class="row"><input type="text" id="scrtime" name="scrtime" disabled="true" value="<% out.print(parseStr(cfgInfo.get("SCRTIME"))); %>" style="width:50px;text-align:right;padding-right:2px"/>(单位:秒)</td>
					</tr>
					<tr>
						<td class="row">截屏间隔时间</td>
						<td class="row"><input type="text" id="ivltime" name="ivltime" disabled="true" value="<% out.print(parseStr(cfgInfo.get("IVLTIME"))); %>" style="width:50px;text-align:right;padding-right:2px"/>(单位:秒)</td>
					</tr>
					<tr>
						<td class="row">应用服务器地址</td>
						<td class="row"><input type="text" id="serverUrl" name="serverUrl" disabled="true" value="<% out.print(parseStr(cfgInfo.get("SURL"))); %>" style="width:350px"/></td>
					</tr>
					<tr>
						<td class="row">FTP服务器IP</td>
						<td class="row"><input type="text" id="ftpIp" name="ftpIp" disabled="true" value="<% out.print(parseStr(cfgInfo.get("FTPIP"))); %>" style="width:350px"/></td>
					</tr>
				</table>
			</form>
    	</div>
	</body>
</html>