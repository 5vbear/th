<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="th.user.User"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="java.util.ArrayList"%>
<%@page import="th.entity.AdvertBean"%>
<%@page import="java.util.List"%>
<%@page import="th.util.DateUtil"%>
<%@page import="th.com.util.Define"%>
<%@page import="th.entity.AwardBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
	/* 获取奖品list集合--start--*/
	List awardList = (ArrayList)request.getAttribute("award_list");
	
	/* 获取奖品list集合 --end--*/
	String defaultTime = DateUtil.getYesterdayDate("yyyy-MM-dd");
	String pageId = (String)request.getAttribute("pageId");
	String pageCount = (String)request.getAttribute("pageCount");
	String selectedOrg = (String)request.getAttribute("selectedOrg");
	String orgName = (String)request.getAttribute("orgName");
	String rowCount = (String)request.getAttribute("rowCount");
	int item_count = 0;
	if(!pageId.equals("1")){
		item_count = Integer.parseInt((String)request.getAttribute("itemCount"));
	} 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<%=defaultStyle %>" />
<script type="text/javascript" src="<%=strContextPath%>/js/Calendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<title>系统管理-抽奖管理</title>
<script language="JavaScript" type="text/javascript">
	var oCalendarChs=new PopupCalendar("oCalendarChs"); //初始化控件时,请给出实例名称:oCalendarChs
	oCalendarChs.weekDaySting=new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
	oCalendarChs.monthSting=new Array("一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月");
	oCalendarChs.oBtnTodayTitle="今天";
	oCalendarChs.oBtnCancelTitle="取消";
	oCalendarChs.Init();
	
	window.onload = function showtable() {
		var tablename = document.getElementById("advert_table");
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
	//checkBox联动
	function check(obj){
		if (obj.name == "child_checkBox") {
			if( obj.checked == false ) {
				document.getElementById("head_checkBox").checked = false;
                return;
            }
			var oElements = document.getElementsByName("child_checkBox");

            for( var i = 0 ; i < oElements.length ; i++ ) {
                if( oElements[i].checked == false ) {
                    return;
                }
            }
            document.getElementById("head_checkBox").checked = true;
		} else {
             var oElements = document.getElementsByName("child_checkBox");
             for( var i = 0 ; i < oElements.length ; i++ ) {
                 oElements[i].checked = obj.checked;
             }
		}
	}
$(document).ready(function(){
	<%if(orgName !=null&&!orgName.equals("")){%>
	
		alert("<%=orgName %>"+"抽奖计划下发成功！");
	<%}%>
	// 添加按钮动作触发
	$('#addBtn').click(function(){
		$('#typeId').val("add_award");
		$('form').submit();
	});
	//下发按钮触发
	$('#sendBtn').click(function(){
		$('#typeId').val("send_award");
		var cnt_selected = 0;
		var cnt_new = 0;
		var awards = "";
		var point = ",";
		var orgId = $('#orgID').val();
		if(orgId == "null"||orgId ==""||orgId == "-1"){
			alert("请选择组织！");
			return false;
		}
		cnt_selected =parseInt($('#item_count').val());
		$("input:checkbox[name=child_checkBox]:checked").each(function(){
			cnt_new=cnt_new+1;
			if(cnt_selected+cnt_new>5){
				return false;
			}
			awards = awards+$(this).val()+point;
		});
		if(cnt_selected==5){
			alert("累计下发奖品数量已经达到5个！不可以再继续下发！");
			return false;
		}
		if(cnt_selected+cnt_new>5){
			alert("累计下发奖品数量不能多于5个！");
			return false;
		}
		if(cnt_new ==0){
			alert("请选择下发的奖品！");
			return false;
		}
			$('#awardsId').val(awards);
			$('#page_id').val('1');
			$('form').submit();
	});
	//编辑按钮
	$('.editBtn').click(function(){
		checkbox_diabled();
		$(this).parent().parent().parent().find('.edit_tr').css('display','none');
		$(this).parent().parent().parent().find('.show_tr').css('display','block');
		var top = $(this).parent().parent();
		top.css('display','none');
		top.next().css('display','block');
	});
	//取消编辑按钮
	$('.cancelBtn').click(function(){
		checkbox_enabled();
		$(this).parent().parent().prev().css('display','block');
		$(this).parent().parent().css('display','none');
	});
	//修改后提交
	$('.saveBtn').click(function(){
		var top = $(this).parent().parent();
		$('#typeId').val("edit_award");
		var num = top.find('input[name="num"]').val();
		if(!isNum(num)){
			alert("请正确填写奖品数量！");
			return false;
		}
		$('#num').val(num);
		var start = top.find('input[name="startDate"]').val();
		if(start ==null||start ==""||start ==""){
			alert("抽奖开始时间不能为空！");
			return false;
		}
		var end = top.find('input[name="endDate"]').val();
		if(end ==null||end ==""||end ==""){
			alert("抽奖结束时间不能为空！");
			return false;
		}
		if(!dateCheck(start,end)){
			return false;
		}
		$('#start_date').val(start);
		$('#end_date').val(end);
		var hits = top.find('input[name="dailyHits"]').val();
		if(!isNum(hits)){
			alert("请正确填写点击量！");
			return false;
		}
		$('#dailyhits').val(hits);
		$('#operateId').val($(this).parent().find('.awardId').val());
		$('form').submit();
	});
	//删除按钮
	$('.dropBtn').click(function(){
		var result = confirm("删除确认");
		if(result){
			$('#operateId').val($(this).parent().find('.awardId').val());
			$('#typeId').val("drop_award");
			$('form').submit();
		}
	});
	//首页
	$('.first_page').click(function(){
		var thisPage = $('#page_id').val();
		if(thisPage =='1'){
			alert("已经是第一页！");
			return false;
		}
		$('#page_id').val('1');
		$('#typeId').val("list_award_panel");
		$('form').submit();
	});
	//上一页
	$('.previous_page').click(function(){
		var thisPage = $('#page_id').val();
		if(thisPage =='1'){
			alert("已经是第一页！");
			return false;
		}
		$('#page_id').val(parseInt(thisPage)-1);
		$('#typeId').val("list_award_panel");
		$('form').submit();
	});
	//下一页
	$('.next_page').click(function(){
		var thisPage = $('#page_id').val();
		var pageCount = $('#page_count').val();
		if(thisPage >=pageCount){
			alert("已经是最后一页！");
			return false;
		}
		$('#page_id').val(parseInt(thisPage)+1);
		$('#typeId').val("list_award_panel");
		$('form').submit();
		
	});
	//最后一页
	$('.last_page').click(function(){
		var thisPage = $('#page_id').val();
		var pageCount = $('#page_count').val();
		if(thisPage >=pageCount){
			alert("已经是最后一页！");
			return false;
		}
		$('#page_id').val('-1');
		$('#typeId').val("list_award_panel");
		$('form').submit();
	});
	//设置checkbox为不可用状态
	function checkbox_diabled(){
		document.getElementById("head_checkBox").checked = false;
		document.getElementById("head_checkBox").disabled = true;
		var oElements = document.getElementsByName("child_checkBox");
		var oElements_selected = document.getElementsByName("child_checkBox_selected");
		var oElements_edit = document.getElementsByName("child_checkBox_edit");
        for( var i = 0 ; i < oElements_edit.length ; i++ ) {
            oElements_edit[i].checked = false;
            oElements_edit[i].disabled = true;
        }
        for( var i = 0 ; i < oElements.length ; i++ ) {
            oElements[i].checked = false;
            oElements[i].disabled = true;
        }
        for( var i = 0 ; i < oElements_selected.length ; i++ ) {
            oElements_selected[i].checked = true;
            oElements_selected[i].disabled = true;
        }
	}
	//设置checkbox为可用状态
	function checkbox_enabled(){
		document.getElementById("head_checkBox").checked = false;
		document.getElementById("head_checkBox").disabled = false;
		var oElements = document.getElementsByName("child_checkBox");
		var oElements_edit = document.getElementsByName("child_checkBox_edit");
        for( var i = 0 ; i < oElements.length ; i++ ) {
            oElements[i].checked = false;
            oElements[i].disabled = false;
        }
        for( var i = 0 ; i < oElements_edit.length ; i++ ) {
            oElements_edit[i].checked = false;
            oElements_edit[i].disabled = false;
        }
		var oElements_selected = document.getElementsByName("child_checkBox_selected");
		for( var i = 0 ; i < oElements_selected.length ; i++ ) {
			oElements_selected[i].checked = true;
			oElements_selected[i].disabled = true;
        }
	}
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
<body onload="onload();">
<form name="form_data" action="<%=url %>" method="post">
<input type="hidden" id="typeId" name="type" value="award_add"/>
<input type="hidden" id="operateId" name="award_id" />
<input type="hidden" id="currentPageId" name="currentPage" />
<input type="hidden" id="num" name="award_num" />
<input type="hidden" id="start_date" name="award_start_date" />
<input type="hidden" id="end_date" name="award_end_date" />
<input type="hidden" id="dailyhits" name="award_dailyhits" />
<input type="hidden" id="awardsId" name="awards"/>
<input type="hidden" id="orgID" name="selectedOrg" value="<%=selectedOrg %>"/>
<input type="hidden" id="page_id" name="pageId" value="<%=pageId %>" />
<input type="hidden" id="page_count" name="pageCount" value="<%=pageCount %>" />
<input type="hidden" id="row_count" name="rowCount" value="<%=rowCount %>" />
<input type="hidden" id="awardsId" name="awards"/>
	<div style="width: 100%; height: 400px;">
		<div style="height: 380px;">
			<table id="advert_table">
				<tr>
					<th style="width: 30px" class="th_tianhe_checkbox_top">
						<input type="checkbox"  id="head_checkBox" name="head_checkBox"  onclick="check(this)"/>
					</th>
					<th class="th_tianhe_center">奖品名称</th>
					<th class="th_tianhe_center">奖品数量</th>
					<th class="th_tianhe_center">抽奖开始时间</th>
					<th class="th_tianhe_center">抽奖结束时间</th>
					<th class="th_tianhe_center">每日点击量</th>
					<th class=th_tianhe_center>操作</th>
				</tr>
				<%for(int i=0;i<awardList.size();i++){
					AwardBean bean = (AwardBean)awardList.get(i);
				%>
				<tr class="show_tr">
					<td width="30" class="th_tianhe_checkbox">
						<%if(bean.getItem().equals("1")){%>
							<% if(pageId.equals("1")){
								item_count = item_count+1;
							} 
							%>
						<input type="checkbox" name="child_checkBox_selected" onClick="check(this);" disabled="disabled" checked="checked" value="<%=bean.getId()%>"/>
						<%}else{%>
						<input type="checkbox" name="child_checkBox" onClick="check(this);"  value="<%=bean.getId()%>"/>
						<%} %>  
					</td>
					<td class="td_tianhe"><%=bean.getAwardName() %></td>
					<td class="td_tianhe" ><%=bean.getAwardNum() %></td>
					<td class="td_tianhe_time" ><%=bean.getStartDate() %></td>
					<td class="td_tianhe_time" ><%=bean.getEndDate() %></td>
					<td class="td_tianhe" ><%=bean.getDailyHits() %></td>
					<td class="td_tianhe_time" width="110px;" >
						<input type="button" class="editBtn" value="编辑" style="float: left;"/>
						<input type="button" class="dropBtn" value="删除" style="float: left;"/>
						<input type="hidden" class="awardId" value="<%=bean.getId()%>"/>
					</td>
				</tr>
				<tr style="display: none;"class="edit_tr">
					<td width="30" class="th_tianhe_checkbox">
						<%if(bean.getItem().equals("1")){%>
						<input type="checkbox" name="child_checkBox_selected" onClick="check(this);" disabled="disabled"  checked="checked"  value="<%=bean.getId()%>"/>
						<%}else{%>
						<input type="checkbox" name="child_checkBox_edit" onClick="check(this);"  value="<%=bean.getId()%>"/>
						<%} %>
					</td>
					<td class="td_tianhe"><%=bean.getAwardName() %></td>
					<td class="td_tianhe" >
						<input type="text" style="font-size: 11px;" size="10" maxlength="9" value="<%=bean.getAwardNum() %>" name="num"   />
					</td>
					<td class="td_tianhe_time" >
						<input type="text" style="font-size: 11px;" size="10" value="<%=bean.getStartDate() %>" name="startDate"  onclick="getDateString(this,oCalendarChs)"  />
					</td>
					<td class="td_tianhe_time" >
						<input type="text" style="font-size: 11px;" size="10" value="<%=bean.getEndDate() %>" name="endDate"  onclick="getDateString(this,oCalendarChs)" />
					</td>
					<td class="td_tianhe" >
						<input type="text" style="font-size: 11px;" size="10" maxlength="9" value="<%=bean.getDailyHits() %>" name="dailyHits" />
					</td>
					
					<td class="td_tianhe_time" width="110px;">
						<input type="button" class="cancelBtn" value="取消" style="float: left;"/>
						<input type="button" class="saveBtn" value="保存" style="float: left;"/>
						<input type="hidden" class="awardId" value="<%=bean.getId()%>"/>
					</td>
				</tr>
				<%} %>
			</table>
			<div>
			<table height="20" width="100%">
				<tr>
					<td align="left">
						<input type="button" class="first_page" style="margin-left:5px" /> 
						<input type="button" class="previous_page" style="margin-left:-2px" /> 
						<input type="button" class="next_page" style="margin-left:-4px" />
						<input type="button" class="last_page" style="margin-left:-2px" />
						[当前第<%=pageId%>页/共<%=pageCount%>页][每页<%=10%>条][共<%=rowCount%>条]
					</td>
					<td align="right">
						<input type="button" id="sendBtn" class="rightBtn" value="下发" />&nbsp;
						<input type="button" id="addBtn" class="rightBtn" value="添加" />
					</td>
				</tr>
			</table>
			</div>
		</div>
	</div>
	<embed src="./image/advert/single_big.png" autostart="true" hidden="true"></embed>
<input type="hidden" name="itemCount" id="item_count" value="<%=item_count%>"/>
</form>
</body>
</html>