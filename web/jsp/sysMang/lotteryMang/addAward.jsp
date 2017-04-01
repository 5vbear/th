<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.user.User"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="th.com.util.Define"%>
<%@page import="th.util.DateUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	String strContextPath = request.getContextPath();
	String url = strContextPath + "/lottery.html";
	String defaultStyle = strContextPath + "/css/lottery.css";
	request.setCharacterEncoding("UTF-8");
	String defaultTime = DateUtil.getYesterdayDate("yyyy-MM-dd");
	String selectedOrg = (String)request.getAttribute("selectedOrg");
	if(selectedOrg ==null||selectedOrg.equals("")){
		selectedOrg = "0";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<title>奖品添加</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/Calendar.js"></script>
<script language="JavaScript" type="text/javascript">
var oCalendarChs=new PopupCalendar("oCalendarChs"); //初始化控件时,请给出实例名称:oCalendarChs
oCalendarChs.weekDaySting=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
oCalendarChs.monthSting=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
oCalendarChs.oBtnTodayTitle="今天";
oCalendarChs.oBtnCancelTitle="取消";
oCalendarChs.Init();
	
$(document).ready(function(){
	//奖品项添加
	$('#addAwardBtn').click(function(){
		var $htmlL = $('#mould');
		$('#material1').append($htmlL.html());
		$('.deleteBtn').bind('click',function(){
			$(this).parent().parent().parent().parent().remove();
		});
	});
	//返回到奖品列表
	$('#returnBtn').click(function(){
		$('#typeId').val('list_award_panel');
		$('form').submit();
	});
	//奖品添加提交
	$('#saveBtn').click(function(){
		$('#typeId').val('save_award');
		var name ="";
		var num = "";
		var start="";
		var end = "";
		var dailyhits = "";
		var point = ",";
		var at = "@";
		var totalStr ="";
		var submit_flg = false;
		$('#mould').find('.templeTable').attr('class','templeTable2');
		$('.templeTable').each(function(){
			name= $(this).find('.col_1').find('input[name="award_name"]').val();
			if(name ==null||name ==""){
				reset();
				alert("奖品名称不能为空！");
				return false;
			}
			num=$(this).find('.col_1').find('input[name="award_num"]').val();
			if(!isNum(num)){
				reset();
				alert("请正确填写奖品数量！");
				return false;
			}
			if(num ==null||num ==""){
				reset();
				alert("奖品数量不能为空！");
				return false;
			}
			start=$(this).find('.col_2').find('input[name="date_s"]').val();
			if(start ==null||start ==""||start ==""){
				reset();
				alert("抽奖开始时间不能为空！");
				return false;
			}
			end=$(this).find('.col_2').find('input[name="date_e"]').val();
			if(end ==null||end ==""||end ==""){
				reset();
				alert("抽奖结束时间不能为空！");
				return false;
			}
			if(!dateCheck(start,end)){
				reset();
				return false;
			};
			dailyhits=$(this).find('.col_3').find('input[name="award_hits"]').val();
			if(!isNum(dailyhits)){
				reset();
				alert("请正确填写点击量！");
				return false;
			}
			if(dailyhits ==null||dailyhits ==""){
				reset();
				alert("每日点击率不能为空！");
				return false;
			}
			totalStr =totalStr+name+point+num+point+start+point+end+point+dailyhits+at;
			submit_flg = true;
		});
		if(submit_flg){
			$('#total_id').val(totalStr);
			$('form').submit();
		}
	});
	//数字校验
	function isNum(str){
		var reg = /^[1-9][\d]{0,8}$/;
	    if(str.match( reg )){
	        return true;
	    }
	    return false;
	}
	//时间校验
	function dateCheck(date1,date2){
		if(!isDate(date1)){
			alert("抽奖开始日期格式不正确！");
			return false;
		}
		if(!isDate(date2)){
			alert("抽奖结束日期格式不正确！");
			return false;
		}
		var start=date1.split("-");
		var end=date2.split("-");
		var current= new Date();
		var year = current.getFullYear();
		var month = current.getMonth()+1;
		var day = current.getDate();
		var time1=new Date(start[0],start[1],start[2]).getTime();
		var time2=new Date(end[0],end[1],end[2]).getTime();
		var currentTime = new Date(year,month,day);
		if(currentTime >time2){
			alert("抽奖结束时间要晚于当前时间！");
			return false;
		}
		if(time1>=time2){
			alert("抽奖开始时间要晚于结束时间！");
			return false;
		} 
		else{
			return true;
		}
	}
	//提交失败后重置
	function reset(){
		$('#mould').find('.templeTable2').attr('class','templeTable');
	}
	function isDate(str){
		var reg =/^(\d{4})-(\d{2})-(\d{2})$/;
		if(str.match(reg)){
			return true;
		}
		return false;
	}
});
</script>
</head>
<body>
<fieldset><legend>系统管理-奖品添加</legend>
	<form name="material_form" action="<%=url %>" method="post" >
		<input type="hidden" name="type" id="typeId" value="send_award">
		<input type="hidden" name="total_str" id="total_id" >
		<input type="hidden" name="orgList" id="orgListId" value="">
		<input type="hidden" id="orgId" name="selectedOrg" value="<%=selectedOrg%>"/>
		<table>
			<tr>
				<td><input class="otherBtn" id="saveBtn" type="button" value="保存"></td>
				<td><input class="otherBtn" id="returnBtn" type="button" value="返回" "></td>
			</tr>
		</table>
		</br>
		<div id="material1" class="firstDiv" style="display: block;border-style: solid ;border-width: 1px;">
			<table class="templeTable">
				<tr class="col_1">
					<th class="th_tianhe">奖&nbsp;&nbsp;品&nbsp;&nbsp;名&nbsp;&nbsp;称：</th>
					<td><input maxlength="9" type="text" name="award_name"></td>
					<th class="th_tianhe">奖品数量：</th>
					<td><input maxlength="10" type="text"  name="award_num" /></td>
				</tr>
				<tr class="col_2">
					<th class="th_tianhe">抽&nbsp;奖&nbsp;开&nbsp;始&nbsp;时&nbsp;间：</th>
					<td><input type="text" size="20" name="date_s" maxlength="10" style="ime-mode:disabled" 
						value=<%=defaultTime%>	 onclick="getDateString(this,oCalendarChs)">
					</td>
					<th class="th_tianhe">抽奖结束时间：</th>
					<td><input type="text" size="20" name="date_e" maxlength="10" style="ime-mode:disabled" 
							 onclick="getDateString(this,oCalendarChs)">
					</td>
				</tr>
				<tr class="col_3">
					<th class="th_tianhe">预计点击量(次/日)</th>
					<td><input maxlength="9" type="text"  name="award_hits" /></td>
				</tr>
			</table>
		</div>
		<div id="addDiv"></div>
		<div id="addBtn" style="display:block;">
			<table>
			<tr>
				<td><input id="addAwardBtn" type="button" class="rightBtn" value="添加"></td>
			</tr>
			</table>
		</div>
		<div id="mould" class="templeDiv" style="display: none;">
		<table class="templeTable" style="margin-left: 0px;">
				<tr class="col_1">
					<th class="th_tianhe">奖&nbsp;&nbsp;品&nbsp;&nbsp;名&nbsp;&nbsp;称：</th>
					<td><input maxlength="10" type="text" name="award_name"></td>
					<th class="th_tianhe">奖品数量：</th>
					<td><input maxlength="9" type="text"  name="award_num" /></td>
				</tr>
				<tr class="col_2">
					<th class="th_tianhe">抽&nbsp;奖&nbsp;开&nbsp;始&nbsp;时&nbsp;间：</th>
					<td><input type="text" size="20" name="date_s" maxlength="10" style="ime-mode:disabled" 
						value=<%=defaultTime%>	 onclick="getDateString(this,oCalendarChs)">
					</td >
					<th class="th_tianhe">抽奖结束时间：</th>
					<td><input type="text" size="20" name="date_e" maxlength="10" style="ime-mode:disabled" 
							 onclick="getDateString(this,oCalendarChs)">
					</td>
				</tr>
				<tr class="col_3">
					<th class="th_tianhe">预计点击量(次/日)</th>
					<td><input maxlength="9" type="text"  name="award_hits" /></td>
					<td><input type="button" class="deleteBtn" name="dropIt" value="删除"/></td>
				</tr>
			</table>
	</div>
	</form>
</fieldset>
</body>
</html>