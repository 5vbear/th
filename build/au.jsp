<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path;
	String mac=(String)request.getAttribute("mac");
%>
<script>
function Check(){
 if(document.getElementById('input_box_area').value=='') {
  alert('请您务必输入意见内容！');
  document.getElementById('input_box_area').focus();

  return false;
 }
 alert('已经将您的意见保存，感谢参与。');
//表情
var controlType=document.getElementsByName("controlType");
for(var i=0;i<controlType.length;i++){
	if(controlType[i].checked) {
		document.getElementById("expression").value=document.getElementsByName("controlType")[i].value;
	}
}
 var form = document.getElementById('usr_feedback_form');
 form.action = '/th/au.html';
 form.submit();
}
</script> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>意见薄</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/advice.css" />
</head>
<body>
  <div class="row">
            <div class="column grid_1190">
                <div id="feedback" class=" clear fl" style="height:910px;">
                    <div id="feedback_area">
                        <div class="clear fl">
                            <h1 class="f18 fb">意见薄</h1>
                        </div>
                        <div id="intro" class="clear fl">
                            <b>尊敬的用户:</b>
                            <p>
                                您在使用本电脑访问网上银行业务时遇到了哪些问题，有什么样的感受，请告诉我们。这样有助于我们进行系统的完善和优化，力求为您营造最佳的使用体验。
                            </p>
                        </div>
                        <div id="feedback_form_area" class="clear fl">
                            <form id="usr_feedback_form" name="usr_feedback_form" method="post">
                            	<input type="hidden" name="mac" value="<%=mac%>" />
								<div class="ini">
									<label for="content" >请留下您对页面的意见或建议(<em class="hb">必选</em>):</label>
									<select name="type">
										<option value ="意见">意见&nbsp;&nbsp;&nbsp;&nbsp;</option>
										<option value ="提问">提问&nbsp;&nbsp;&nbsp;&nbsp;</option>
										<option value="投诉">投诉&nbsp;&nbsp;&nbsp;&nbsp;</option>
									</select>
                       			</div>
                                <div class="ini">
                                    <label for="content" >请留下您对页面的意见或建议(<em class="hb">必填</em>):</label>
                                    <textarea id="input_box_area" name="content" class="clear fl "></textarea>
                                </div>
                                <div class="ini">
                                    <label for="phone" >请留下您的联系电话(选填):</label>
                                    <input type="text" name="phone" value="" class="input_box" />
                                </div>
                                <div class="ini">
                                    <label for="email" >请留下您的常用邮箱(选填):</label>
                                    <input type="text" name="email" value="" class="input_box" />
                                </div>
                                <div class="ini">
                                    <label for="expression" >填写该意见薄时，您的表情是(选填):</label>
					  				<input type="hidden"" id="expression" name="expression" value="没有选择"/>
										<tr>
						  					<td>
							  					<img id="aa" src="image/expression/veryglad.gif"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								  			</td>
						  					<td>
						  						<img id="aa" src="image/expression/glad.gif"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							  				</td>
						  					<td>
							  					<img id="aa" src="image/expression/general.gif"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								  			</td>
						  					<td>
							  					<img id="aa" src="image/expression/unglad.gif"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								  			</td>
						  					<td>
							  					<img id="aa" src="image/expression/anger.gif"/>
								  			</td>
										</tr>
										<br/>
										<tr>
						  					<td>
								  				<input type="radio"  name="controlType" value="非常满意"/>非常满意&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								  			</td>
						  					<td>
							  					<input type="radio" name="controlType" value="满意"/>满意&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							  				</td>
						  					<td>
									  			<input type="radio" name="controlType" value="一般"/>一般&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								  			</td>
						  					<td>
							  					<input type="radio" name="controlType" value="不满意"/>不满意&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								  			</td>
						  					<td>
												<input type="radio" name="controlType" value="愤怒"/>愤怒
								  			</td>
										</tr>
                                </div>
                                <div><input type="image" name="submitButton" id="submitButton" src="image/ok.jpg" onClick="Check();"/></div>
                            
                            </form>
                        </div>
                    </div>
                    
                </div>
            </div>
        </div>

</body>
</html>