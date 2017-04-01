<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="th.entity.LotteryBean"%>
<%

	ArrayList list =(ArrayList) request.getAttribute("list");
	String mac =(String) request.getAttribute("mac");
	String macKind = (String) request.getAttribute("macKind");
	for(int i = 0 ;i <list.size();i++){
		LotteryBean bean = (LotteryBean)list.get(i);
		String url  = bean.getLogo_url();
		String award_name  = bean.getAward_name();
	}
	String agent = request.getHeader("user-agent");
	int i = -1;
%>



<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <title>抽奖</title>
    <style type="text/css">
 *{ margin: 0; padding: 0; font-size:12px;}
body{ background-color: #2C1914;font-family:"宋体"; }
a img, ul, li { list-style: none; }
a{text-decoration:none; outline:none; font-size:12px;}
input, textarea, select, button { font-size: 100%;}
.abs{ position:absolute;}
.rel{ position:relative;}
.wrap{ min-height:1000px;}
.main{ height:718px; }
.con980{ width:980px; margin:0 auto;}
<%if(agent!=null&&agent.indexOf("Windows")>0){ %>
.header{ width:100%; height:50px;} 
.play{ no-repeat; width:1000px; height:500px; padding:0px 0 0 0px;}
     td{width:220px; height:110px; font-family:"微软雅黑"; background-color:white; text-align:center; line-height:115px; font-size:15px; }
 .playcurr{  background:url(./image/5.jpg)}

 .playnormal1{ background:url(./image/1.jpg)}
  .playnormal2{ background:url(./image/2.jpg)}
   .playnormal3{ background:url(./image/3.jpg)}
    .playnormal4{ background:url(./image/4.jpg)}
    
    .playnormal5{ background:url(./image/zhongjiang.jpg)}
    .playnormal6{ background:url(./image/zhongjiang.jpg)}
     .playnormal7{ background:url(./image/zhongjiang.jpg)}
     .playnormal8{ background:url(./image/zhongjiang.jpg)}
     .playnormal9{ background:url(./image/zhongjiang.jpg)}
 .jiangpin1{ background:url(./image/11.jpg)}
 .tableBG{ background:url(./image/choujiang.jpg)}
  .playnormal12{ background:url(./image/12.jpg)}
 .playnormal13{ background:url(./image/zhongjiangxuanzhong.jpg)}
  .start{ background:url(./image/start.jpg)}
 .play_btn{ width:480px; height:115px; display:block; background-color:#F60;border:0; cursor:pointer; font-family:"微软雅黑";  font-size:40px;}
 .play_btn:hover{ background-position:0 -115px;}
 .btn_arr{ left:255px; top:255px;}
<%}else{%>
.header{ width:100%; height:0px;} 
.play{vertical-align : top;  background:url(images/fl01.jpg) no-repeat; width:100%; height:90%; padding:0px 0 0 0px;}
<%if("pad".equals(macKind)){%>
     td{width:20%; height:20%; font-family:"微软雅黑"; background-color:white; no-repeat fixed top left; background-size:100% 100%; line-height:110px; }
      .btn_arr{ left:255px; top:225px;}
      .play_btn{ width:480px; height:115px; display:block; background-color:#F60;border:0; cursor:pointer; font-family:"微软雅黑";  font-size:15px;}
<%}else{%>
     td{width:20%; height:20%; font-family:"微软雅黑"; background-color:white; no-repeat fixed top left; background-size:100% 100%; line-height:70px; }
      .btn_arr{ left:200px; top:150px;}
      .play_btn{ width:240px; height:58px; display:block; background-color:#F60;border:0; cursor:pointer; font-family:"微软雅黑";  font-size:15px;}
<%}%>
 .playcurr{  background:url(./image/5.jpg)}

 .playnormal1{ background:url(./image/1.jpg)}
  .playnormal2{ background:url(./image/2.jpg)}
   .playnormal3{ background:url(./image/3.jpg)}
    .playnormal4{ background:url(./image/4.jpg)}
    
    .playnormal5{ background:url(./image/zhongjiang.jpg)}
    .playnormal6{ background:url(./image/zhongjiang.jpg)}
     .playnormal7{ background:url(./image/zhongjiang.jpg)}
     .playnormal8{ background:url(./image/zhongjiang.jpg)}
     .playnormal9{ background:url(./image/zhongjiang.jpg)}
 .jiangpin1{ background:url(./image/11.jpg)}
 .tableBG{ background:url(./image/choujiang.jpg)}
   .playnormal12{ background:url(./image/12.jpg)}
  .playnormal13{ background:url(./image/zhongjiangxuanzhong.jpg)}
   .start{ background:url(./image/start1.jpg)}
 .play_btn:hover{ background-position:0 -115px;}
<%}%>

    </style>
</head>
<body onload="show()">
<%if(agent!=null&&agent.indexOf("Windows")>0){ %>
<div class="wrap">
       <div class="header"></div>
       <div class="main">
          <div class="con980">
          <%} %>
          
              <div class="play rel">
                 <p class="btn_arr abs"><input style ="background:url(./image/start.png)" value="" id="btn1" type="button" class="play_btn " ></p>
<table style="border :1px ;solid :black;width:100%;height:90%;table-layout:fixed;overflow:hidden;" id="tb" cellpadding="0" cellspacing="1" >
<tr>
    <td  class="playnormal4">&nbsp;</td><%if(list.size()>0){ 		LotteryBean bean = (LotteryBean)list.get(0);%><td class="playnormal6"><%=bean.getAward_name() %></td><%}else{ %><td  class="playnormal2">&nbsp;</td><%} %><td  class="playnormal3">&nbsp;</td><td  class="playnormal4">&nbsp;</td><td  class="playnormal1">&nbsp;</td>
</tr>
<tr>
    <%if(list.size()>4){ LotteryBean bean = (LotteryBean)list.get(4);%><td class="playnormal5"><%=bean.getAward_name() %></td><%}else{ %><td  class="playnormal2">&nbsp;</td><%} %><td></td><td></td><td></td><%if(list.size()>1){ LotteryBean bean = (LotteryBean)list.get(1);%><td class="playnormal7"><%=bean.getAward_name() %></td><%}else{ %><td  class="playnormal4">&nbsp;</td><%} %>
</tr>
<tr>
    <td  class="playnormal1">&nbsp;</td><td></td><td></td><td></td><td  class="playnormal3">&nbsp;</td>
</tr>
<tr>
    <td  class="playnormal4">&nbsp;</td><td> </td><td></td><td></td><td  class="playnormal2">&nbsp;</td>
</tr>
<tr>
    <td  class="playnormal1">&nbsp;</td><%if(list.size()>3){ LotteryBean bean = (LotteryBean)list.get(3);%><td class="playnormal9"><%=bean.getAward_name() %></td><%}else{ %><td  class="playnormal1">&nbsp;</td><%} %><td  class="playnormal2">&nbsp;</td><%if(list.size()>2){ LotteryBean bean = (LotteryBean)list.get(2);%><td class="playnormal8"><%=bean.getAward_name() %></td><%}else{ %><td  class="playnormal4">&nbsp;</td><%} %><td  class="playnormal1">&nbsp;</td>
</tr>
</table>
<%if(agent!=null&&agent.indexOf("Windows")>0){ %>
            </div>
        </div>
     </div>
     <%} %>
</div>
    <script type="text/javascript">
    function show(){
    	var button1 = document.getElementById('btn1');  
    	<%if((agent!=null&&agent.indexOf("Windows")>0) || "pad".equals(macKind)){ %>
    	      button1.style.backgroundImage="url(./image/start.png)";
    	<%}else{%>
    	      button1.style.backgroundImage="url(./image/start1.png)";
    	<%}%>
    }
    var Hits =3;
    var awardNum = "<%=list.size()%>";
    var awardID = "";
    <%if(list==null||list.size()==0){%>
    document.getElementById("btn1").disabled=true;
    alert("目前没有抽奖计划！");

    <%}%>
  	
    var ret;
    $(document).ready(function(){
    	 
    	  $("#btn1").click(function(){
  			if(Hits==0){
    				
    			    alert("您的抽奖次数已完毕，您不能再进行抽奖了！");
    			    return false;
    			}
    			document.getElementById("btn1").disabled=true;
  
				
    	         $.ajax({
    	             url:"/th/ajaxlottery_user.html",
    	             data:{'mac':"<%=mac%>"},
    	             type:"post",
    	             async:false,
    	             dataType:"text",
    	             success:function(result){
    	            	 if(result.indexOf(",") != -1){
    	            		 var arrays = result.split(",");
    	            		 ret = arrays[0];
    	            		 awardID = arrays[1];
        	             } else {
        	            	 ret = result;
            	         }
    	       	     	 clearInterval(Time);
    	    	         cycle=0;
    	    	         flag=false;
    	    	         EndIndex=Math.floor(Math.random()*16+1);
    	    	         EndCycle=Math.floor(Math.random()*(4)+3);
    	    	         Time = setInterval(Star,Speed);
    	    	         --Hits;
    	             }
    	     	});	         
    	  });
    	});


    

    

    
     /*
      * 删除左右两端的空格
      */
     function Trim(str){
         return str.replace(/(^\s*)|(\s*$)/g, ""); 
     }
     
        /*
         * 定义数组
         */
        function GetSide(m,n){
            //初始化数组
            var arr = [];
            for(var i=0;i<m;i++){
                arr.push([]);
                for(var j=0;j<n;j++){
                    arr[i][j]=i*n+j;
                }
            }
            //获取数组最外圈
            var resultArr=[];
            var tempX=0,
             tempY=0,
             direction="Along",
             count=0;
            while(tempX>=0 && tempX<n && tempY>=0 && tempY<m && count<m*n)
            {
                count++;
                resultArr.push([tempY,tempX]);
                if(direction=="Along"){
                    if(tempX==n-1)
                        tempY++;
                    else
                        tempX++;
                    if(tempX==n-1&&tempY==m-1)
                        direction="Inverse"
                }
                else{
                    if(tempX==0)
                        tempY--;
                    else
                        tempX--;
                    if(tempX==0&&tempY==0)
                        break;
                }
            }
            return resultArr;
        }
        
       var index=0,           //当前亮区位置
       prevIndex=0,          //前一位置
       Speed=400,           //初始速度
       Time,            //定义对象
       arr = GetSide(5,5),         //初始化数组
         EndIndex=0,           //决定在哪一格变慢
         tb = document.getElementById("tb"),     //获取tb对象 
         cycle=0,           //转动圈数   
         EndCycle=0,           //计算圈数
        flag=false,           //结束转动标志 
        quick=0;           //加速
        btn = document.getElementById("btn1")
        

        
        function Star(num){
        	
            //跑马灯变速
            if(flag==false){
              //走五格开始加速
             if(quick==5){
                 clearInterval(Time);
                 Speed=50;
                 Time=setInterval(Star,Speed);
             }
             //跑N圈减速
             if(cycle==EndCycle+1 && index==parseInt(EndIndex)){
                 clearInterval(Time);
                 Speed=400;
                 flag=true;       //触发结束    
                 Time=setInterval(Star,Speed);

             }
            }
            
            if(index>=arr.length){
                index=0;
                cycle++;
            }
            
           //结束转动并选中号码
     //trim里改成数字就可以减速，变成Endindex的话就没有减速效果了
         if(flag==true && index==parseInt(ret)-1){
             if(awardNum=="1"){
					if (parseInt(ret) == 0 || parseInt(ret) == 2) {
						setTimeout('inputInfo()',1000);
					}
				}else if(awardNum=="2"){
					if (parseInt(ret) == 0 || parseInt(ret) == 2 || parseInt(ret) == 6 ) {
						setTimeout('inputInfo()',1000);
					}
				}else if(awardNum =="3"){
					if (parseInt(ret) == 0 || parseInt(ret) == 2 || parseInt(ret) == 6 || parseInt(ret) == 10) {
						setTimeout('inputInfo()',1000);
					}
					
				}else if(awardNum =="4"){
					if (parseInt(ret) == 0 || parseInt(ret) == 2 || parseInt(ret) == 6 || parseInt(ret) == 10 || parseInt(ret) == 12) {
						setTimeout('inputInfo()',1000);
					}
					
				}else if(awardNum =="5"){
					if (parseInt(ret) == 0 || parseInt(ret) == 2 || parseInt(ret) == 6 || parseInt(ret) == 10 || parseInt(ret) == 12 || parseInt(ret) == 16) {
						setTimeout('inputInfo()',1000);
					}
					
				}
             
          quick=0;
          document.getElementById("btn1").disabled=false;
          clearInterval(Time);
			
            }
      
            if(index>0)
                prevIndex=index-1;
            else{
                prevIndex=arr.length-1;
            }
            
	       	if(index==15){
	     	   <%if(list.size()==5){%>
               tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal13";

        	   <%}else{%>
        	   
        		tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal12";

        	   <%}%>
	       		
	       		
	       	}else if(index==1){
		     	   <%if(list.size()>=1){%>
	               tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal13";

	        	   <%}else{%>
	        	   
	        		tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal12";

	        	   <%}%>
		       		
	       	}else if(index==5){
		     	   <%if(list.size()>=2){%>
	               tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal13";

	        	   <%}else{%>
	        	   
	        		tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal12";

	        	   <%}%>
	       		
	       	}else if(index==9){
		     	   <%if(list.size()>=3){%>
	               tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal13";

	        	   <%}else{%>
	        	   
	        		tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal12";

	        	   <%}%>
	       		
	       	}else if(index==11){
		     	   <%if(list.size()>=4){%>
	               tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal13";

	        	   <%}else{%>
	        	   
	        		tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal12";

	        	   <%}%>
	       		
	       	}else{
	       		
	       		tb.rows[arr[index][0]].cells[arr[index][1]].className="playnormal12";

	       	}
       	 
       	 
       	 
           if(prevIndex==15){
        	   <%if(list.size()==5){%>
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal5";

        	   <%}else{%>
        	   
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal2";

        	   <%}%>

           }else if(prevIndex==0){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal4";

           }else if(prevIndex==1){
        	   <%if(list.size()>=1){%>
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal6";

        	   <%}else{%>
        	   
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal2";

        	   <%}%>
           }else if(prevIndex==2){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal3";

           }else if(prevIndex==3){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal4";

           }else if(prevIndex==4){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal1";

           }else if(prevIndex==5){
        	   
        	   <%if(list.size()>=2){%>
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal7";

        	   <%}else{%>
        	   
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal4";

        	   <%}%>

           }else if(prevIndex==6){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal3";

           }else if(prevIndex==7){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal2";

           }else if(prevIndex==8){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal1";

           }else if(prevIndex==9){
        	   
        	   <%if(list.size()>=3){%>
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal8";

        	   <%}else{%>
        	   
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal4";

        	   <%}%>

           }else if(prevIndex==10){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal2";

           }else if(prevIndex==11){
        	   
        	   <%if(list.size()>=4){%>
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal9";

        	   <%}else{%>
        	   
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal1";

        	   <%}%>
           }else if(prevIndex==12){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal1";

           }else if(prevIndex==13){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal4";

           }else if(prevIndex==14){
               tb.rows[arr[prevIndex][0]].cells[arr[prevIndex][1]].className="playnormal1";

           }
            index++;
            quick++;
  
        }
         function inputInfo(){
        	//var paramers="dialogWidth:800px;DialogHeight:400px;status:no;help:no;unadorned:no;resizable:no;status:no";  
     		var url = "/th/lottery_history.html?awardID="+awardID+"&functionID=1";
     		window.location.href=url;
     		//window.showModalDialog(url,'',paramers);
         }
    </script>
</body>
</html>