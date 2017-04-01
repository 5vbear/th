<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="th.entity.UKeyBean"%>
<%@page import="th.util.StringUtils"%>
<%@page import="th.util.DateUtil"%>
<%@page import="th.com.util.Define4Report"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>注册信息填写</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ path;
	
	String orgs1 = (String) request.getAttribute( "orgs1" );
	String orgs2 = (String) request.getAttribute( "orgs2" );
	String mac = (String) request.getAttribute("mac");
%>

<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/report.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/zTree/css/demo.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/zTree/css/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="<%=basePath%>/js/Calendar.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>/zTree/js/jquery.ztree.excheck-3.5.js"></script>
<style type="text/css">
* { padding:0; margin:0; }
body { text-align:center; padding:50px;}
.select-outer { display:inline-block; *display:inline; zoom:1; border:1px solid #ccc; }
.select-inner { display:inline-block; *display:inline; zoom:1; height:19px; overflow:hidden; position:relative; }
.select-h { border:1px solid #ccc; height:21px; font-size:12px; position:relative; zoom:1; margin:-1px; }
.select-outer-m { display:inline-block; *display:inline; zoom:1; }
.select-m { height:21px; font-size:12px; position:relative; zoom:1; margin:-1px;}
.select-wh200 { padding:3px; }
.select-wh200 .select-inner { width:200px; }
.select-wh200 select { width:202px; }

</style>
</head>

<script type="text/javascript">

  	function doConfirm(){
  		var orgSelect1 = document.getElementById("orgSelect1").value;
  		var orgSelect2 = document.getElementById("orgSelect2").value;
  		if(orgSelect1=="" && orgSelect2==""){
  	  		alert("请选择组织!");
  	  		return;
  	  	}
  		document.preAuditForm.submit();
  	}
  	
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
	                onClick: onClick  
	            }  
	};
	var setting2 = {
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
                onClick: onClickSec  
            }  
};
	var zNodes = <%=orgs1%> ;
	var zSeccondNodes = <%=orgs2%> ;

    $(document).ready(function() {
		zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		
		var nodes = zTreeObj.transformToArray(zTreeObj.getNodes());
		var v = "", w = "";
		// 设置组织名称下拉列表值 
		var orgList = document.getElementById("orgSelect1");
		for (var i=0; i<nodes.length; i++) {
			w = "";
			if(i<nodes.length-1){
				for(var j=0; j<(nodes[i].level-nodes[0].level); j++){
					w += "├─";					
				}
			}else{
				for(var j=0; j<(nodes[i].level-nodes[0].level); j++){
					w += "└─";
				}
			}
			// 为select下拉菜单动态赋予option值 
			orgList.options.add(new Option(w+nodes[i].name,nodes[i].id));
			// 组织List信息拼接  
			v += w + nodes[i].name + "_" + nodes[i].id;
			if(i<nodes.length-1){
				v += ",";
			}
		}
		showSecond(nodes[0].id);
	});
    function onClick(event, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),  
        nodes = zTree.getSelectedNodes(),//获取 zTree 当前被选中的节点数据集合（按Ctrl多选择）  
        v = "";
        var nid = "";
        nodes.sort(function compare(a,b){return a.id-b.id;});//按照id从小到大进行排序  
        for (var i=0, l=nodes.length; i<l; i++) {
            v += nodes[i].name + ",";  
            nid = nodes[i].id;
        }
    }
    function onClickSec(){
    }
    function showSecond(objValue){
    	var orgList = document.getElementById("orgSelect2");
    	orgList.options.length = 0;
            	zSecTreeObj = $.fn.zTree.init($("#tree2"), setting2, zSeccondNodes[objValue]);

                var nodes = zSecTreeObj.transformToArray(zSecTreeObj.getNodes());
        		var v = "", w = "";
        		// 设置组织名称下拉列表值 
        		for (var i=0; i<nodes.length; i++) {
        			w = "";
        			if(i<nodes.length-1){
        				for(var j=0; j<(nodes[i].level-nodes[0].level); j++){
        					w += "├─";					
        				}
        			}else{
        				for(var j=0; j<(nodes[i].level-nodes[0].level); j++){
        					w += "└─";
        				}
        			}
        			// 为select下拉菜单动态赋予option值 
        			orgList.options.add(new Option(w+nodes[i].name,nodes[i].id));
        			// 组织List信息拼接  
        			v += w + nodes[i].name + "_" + nodes[i].id;
        			if(i<nodes.length-1){
        				v += ",";
        			}
        			
        		}
    }
</script>
</head>
<body>
<form class="x-client-form" action="/th/preAudit" name="preAuditForm" method="post">
<input type="hidden" name="pageID"   value="1">
<input type="hidden" name="mac"   value="<%=mac%>">
<div class="select-outer-m select-wh200">
  <div class="select-inner">
<span class="select-m">&nbsp;请选择银行:&nbsp;</span>
</div>
  </div>
  <div class="select-outer select-wh200">
  <div class="select-inner">
    <select id="orgSelect1" name="orgSelect1" class="select-h" onChange="showSecond(this.value)"></select>
    </div>
  </div>
  <div class="select-outer select-wh200">
  <div class="select-inner">
  <select id="orgSelect2" name="orgSelect2" class="select-h"></select>
  </div>
  </div>
  <br/><br/>
  <input type="button" style="width:60px ;height: 30px;" value="确定" onclick ="doConfirm()" />
  
</form>
</body>
</html>