<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

	<link rel="stylesheet" href="${APP_PATH }/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${APP_PATH }/css/font-awesome.min.css">
	<link rel="stylesheet" href="${APP_PATH }/css/main.css">
	<link rel="stylesheet" href="${APP_PATH }/ztree/zTreeStyle.css">
	<style>
	.tree li {
        list-style-type: none;
		cursor:pointer;
	}
	table tbody tr:nth-child(odd){background:#F4F4F4;}
	table tbody td:nth-child(even){color:#C00;}
	</style>
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
           <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 角色维护</a></div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li style="padding-top:8px;">
				<%@include file="/WEB-INF/jsp/common/userinfo.jsp"%>
			</li>
            <li style="margin-left:10px;padding-top:8px;">
				<button type="button" class="btn btn-default btn-danger">
				  <span class="glyphicon glyphicon-question-sign"></span> 帮助
				</button>
			</li>
          </ul>
          <form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="Search...">
          </form>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
			<div class="tree">
				<%@include file="/WEB-INF/jsp/common/menu.jsp" %>
			</div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

			<div class="panel panel-default">
              <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限分配列表<div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
			  <div class="panel-body">
                  <button id="assignPermissionBtn" class="btn btn-success">分配许可</button>
                  <br><br>
                  <ul id="treeDemo" class="ztree"></ul>
			  </div>
			</div>
        </div>
      </div>
    </div>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">帮助</h4>
		  </div>
		  <div class="modal-body">
			<div class="bs-callout bs-callout-info">
				<h4>没有默认类</h4>
				<p>警告框没有默认类，只有基类和修饰类。默认的灰色警告框并没有多少意义。所以您要使用一种有意义的警告类。目前提供了成功、消息、警告或危险。</p>
			  </div>
			<div class="bs-callout bs-callout-info">
				<h4>没有默认类</h4>
				<p>警告框没有默认类，只有基类和修饰类。默认的灰色警告框并没有多少意义。所以您要使用一种有意义的警告类。目前提供了成功、消息、警告或危险。</p>
			  </div>
		  </div>
		  <!--
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			<button type="button" class="btn btn-primary">Save changes</button>
		  </div>
		  -->
		</div>
	  </div>
	</div>

    <script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
    <script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
	<script src="${APP_PATH }/script/docs.min.js"></script>
	<script src="${APP_PATH }/ztree/jquery.ztree.all-3.5.min.js"></script>
	<script type="text/javascript" src="${APP_PATH}/script/menu.js"></script>
	<script type="text/javascript" src="${APP_PATH}/jquery/layer/layer.js"></script>
        <script type="text/javascript">
            $(function () {
			    $(".list-group-item").click(function(){
				    if ( $(this).find("ul") ) {
						$(this).toggleClass("tree-closed");
						if ( $(this).hasClass("tree-closed") ) {
							$("ul", this).hide("fast");
						} else {
							$("ul", this).show("fast");
						}
					}
				});
			    showMenu();
			    loadData();
            });
            
            var setting = {
            	check:{
            		enable:true//在树节点前显示复选框 
            	},
            	view:{
            		selectedMulti:false,//不能一次选多个
            		addDiyDom:function(treeId, treeNode){
            			var icoObj = $("#" + treeNode.tId + "_ico");
            			if(treeNode.icon){
            				icoObj.removeClass("button ico_docu ico_open").addClass(treeNode.icon).css("background","");
            			}
            		},
            	},
            	async:{
            		enable:true,//采用异步
            		url:"${APP_PATH}/role/loadDataAsync.do?roleid=${param.roleid}",
            		autoParam:["id", "name=n", "level=lv"]//name=n相当于给name取了一个别名为n，传出去的值为 id=1&n=xxx&lv=2
            	},
            	callback:{
            		onClick:function(event, treeId, json){
            			
            		}//不知道什么意思
            	}
            };
				
            $.fn.zTree.init($("#treeDemo"),setting);//异步加载树的数据，注意：服务器端返回的结果必须是一个数组
            /* $.fn.zTree.init($("#treeDemo"),setting,ztreeJSON);//同步加载树的数据 */
            
            $("#assignPermissionBtn").click(function(){
            	
            	var jsonObj = {
            		roleid:"${param.roleid}"
            	};
            	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            	
            	var checkedNodes = treeObj.getCheckedNodes(true);
            	
            	$.each(checkedNodes,function(i,n){
            		jsonObj["ids["+i+"]"]=n.id;
            	});
            	
            	if(checkedNodes.length == 0){
            		layer.msg("请选择分配许可，至少分配一个许可",{time:1000,icon:5,shift:6});
            	}else {
            		var loadingIndex = -1;
            		$.ajax({
            			type:"POST",
            			url:"${APP_PATH}/role/doAssignPermission.do",
            			data:jsonObj,
           				beforeSend:function(){
           					loadingIndex = layer.msg("正在分配许可....",{icon:16});
           					return true;
           				},
           				success:function(result){
           					layer.close(loadingIndex);
           					
           					if(result.success){
           						layer.msg("分配成功",{time:1000,icon:5});
           					}else{
           						layer.msg("分配失败",{time:1000,icon:5,shift:6});
           					}
         				},
 						error:function(){
 							layer.msg("操作失败",{time:1000,icon:5,shift:6});
 						}
            		})
            	}
            	
            });
            
			/* var zNodes =[
				{ name:"父节点1 - 展开", open:true,
					children: [
						{ name:"父节点11 - 折叠",
							children: [
								{ name:"叶子节点111"},
								{ name:"叶子节点112"},
								{ name:"叶子节点113"},
								{ name:"叶子节点114"}
							]},
						{ name:"父节点12 - 折叠",
							children: [
								{ name:"叶子节点121"},
								{ name:"叶子节点122"},
								{ name:"叶子节点123"},
								{ name:"叶子节点124"}
							]},
						{ name:"父节点13 - 没有子节点", isParent:true}
					]},
				{ name:"父节点2 - 折叠",
					children: [
						{ name:"父节点21 - 展开", open:true,
							children: [
								{ name:"叶子节点211"},
								{ name:"叶子节点212"},
								{ name:"叶子节点213"},
								{ name:"叶子节点214"}
							]},
						{ name:"父节点22 - 折叠",
							children: [
								{ name:"叶子节点221"},
								{ name:"叶子节点222"},
								{ name:"叶子节点223"},
								{ name:"叶子节点224"}
							]},
						{ name:"父节点23 - 折叠",
							children: [
								{ name:"叶子节点231"},
								{ name:"叶子节点232"},
								{ name:"叶子节点233"},
								{ name:"叶子节点234"}
							]}
					]},
				{ name:"父节点3 - 没有子节点", isParent:true}
					]; */
			
					
			
           
								
							

							
						
						
		        
        </script>
  </body>
</html>
