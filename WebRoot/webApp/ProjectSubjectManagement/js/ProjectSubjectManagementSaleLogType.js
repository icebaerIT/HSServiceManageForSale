var grocessdata="";
var OfferorsID="";
var process="";
var template="";//模板
var big_class = [];//获取对象
var ID="";//获取小类id
var submitaccount="";
var getcustom="";//流程人
var checkID="";//审核人ID
var checkName="";//审核人
var type=0;//手动1或者默认0
//var Assignments;//任务量
//var ContractVolume;//签约量
//var Receivableamount;//回款量
//var Phone;//电话量
//var Visit;//拜访量
//var Email;//邮件数
//var List;//签单量
var ContactID="";//联系人ID
//初始化界面
	$(function(){
		$(".Jump").bind("click",function(){
	        $(".SaleJourna").css({"color":"#00FF00","border-bottom-color":"#00FF00"});
	        $(".SalelogTemp").css({"color":"#000","border-bottom-color":"#000"});
	        });
		});
//查询方法
	function getSalelogByCondition(condition){
		$.ajax({
			url:"../../typeController/getSalelogByCondition.do",
			type:"POST",
			data:{condition:condition},
			async:false,
			success:function(data){
				var getData=JSON.parse(data);
				big_class=getData;
				}
			});
		};
//模板获取方法
	function getTemplateByCondition(condition){
		$.ajax({
			url:"../../templetController/getstr.do",
			Type:"POST",
			data:{conditions:condition},
			async:false,
			success:function(data){
				var getData=JSON.parse(data);
				template=getData;
				}
			});
		};
//获取审批人
	function achievegetcustomer(){
		$.ajax({
			url:"../../employeeController/getEmployee.do",
		    Type:"POST",
		    data:{},
			async:false,
			success:function(data){
				var getData=JSON.parse(data);
			    getcustom=getData;
			    }
		    });
		};
//审核人选择custom流程
	$("#custom").click(function(){
		var processcustom='<div class="inner" id="T" style="margin-top:5%"><div>审核人:</div>'+
		'<div class="DataContent"><span id="Custom"></span>'+
		'</div>'+
		'</div>';
		$("#Control4").html(processcustom);
		if(!grocessdata)
			alert("请按顺序选择！");
		else {
			$("#Custom").html(getcustom[0].checkname);
			type=1;
			}
		});
$("#auto").click(function(){
	type=0;
	$("#T").css({"display":"none"});
	});
//点击事件
	$("#small").click(function(){
		$("#Control2").animate({ height: 'toggle'}, 15);
		$("#Control1,#Control3").css("display","none");
		//$("#Control1").hide();
		});
	$("#Template").click(function(){
		if(template)
		$("#Control3").animate({ height: 'toggle'}, 15);
		else alert("请按顺序选择！");
		$("#Control1,#Control2").css("display","none");
		//$("#Control2").hide();
		});
	$("#Control4").click(function(){
		$("#processes").animate({ height: 'toggle'}, 15);
		$("#processes").html(grocessdata);
		});
	$("#ContactID").click(function(){
		$("#PEOPLE").html(getcustom[0].checkname);
		//$("#contactID").animate({ height: 'toggle'}, 15);
		//$("#processes").html(grocessdata);
		$("#Display").hide();
		$("#hidden").show();
		});
	$("#CONTACT").click(function(){
		$("#contactID").animate({ height: 'toggle'}, 15);
		});
	$("#submit").click(function(){
		$("#Display").show();
		$("#hidden").hide();
		});

//大类选择事件
	$("#Big").click(function(){
		var join="";
		template="";
		$("#Control2,#Control3").css("display","none");
		getSalelogByCondition("LEVEL=1 AND TYPE=3");
		for(var i=0;i<big_class.length;i++){
			if(big_class[i])
				join+="<div id='"+big_class[i].ID+"' class='choose' onclick=addsmallType('"+big_class[i].ID+"')>"+big_class[i].NAME+"</div>";
			$("#Biginner").html(big_class[0].NAME);
			} 	
		$("#Control1").html(join).animate({
			height: 'toggle'
				}, 15);
		});
//小类选择事件
	function addsmallType(p){
		var text=$("#"+p+"").text();
		$("#Control1").css("display","none");
		$("#Biginner").html(text);
		$("#smallinner,#Templateinner").html("");
//		$("#Templateinner").html();
		getSalelogByCondition("PARENTID='"+p+"' AND TYPE=3");
		var take="";
		if(big_class)
		for(var i=0;i<big_class.length;i++){
			take+="<div id='"+big_class[i].ID+"' class='choose' onclick=addType('"+big_class[i].ID+"')>"+big_class[i].NAME+"</div>";
			}
		else alert("请按顺序选择！");
		$("#Control2").html(take);
		};
//模板方法
	function addType(q){
		$("#Control1,#Control3").css("display","none");
		var text=$("#"+q+"").text();
		$("#Control2").css("display","none");
		$("#smallinner").html(text);
		$("#Templateinner").html("");
		getTemplateByCondition(q);
		var templates="";
		if(template)
			{
			for(var i=0;i<template.length;i++){
				templates+="<div id='id"+template[i].ID+"' class='choose' onclick=addTemplate('"+template[i].ID+"')>"+template[i].templetName+"</div>";
				ID=template[0].ID;	
				}
			$("#Control3").html(templates);
			}
		else alert("请按顺序选择！");

		};	
//模板选择
	function addTemplate(q){
		var text=$("#id"+q+"").text();
		$("#Control3").css("display","none");
		$("#Templateinner").html(text);
		ID=q;
		achievegetcustomer();
		grocessdata="";
		for(var i=0;i<getcustom.length;i++){
			grocessdata+="<div id='id"+getcustom[i].ID+"' class='choose' onclick=addcustom('"+getcustom[i].ID+"')>"+getcustom[i].checkname+"</div>";
			checkID=getcustom[0].ID;
			}
		};
//审批流程人
	function addcustom(q){
		var text=$("#id"+q+"").text();
		$("#processes").css("display","none");
		$("#Custom").html(text);
		checkID=q;
		checkName=text;
		};
//获得联系人
	$("#CONTACT").click(function(){
		var Contact="";
		for(var i=0;i<getcustom.length;i++){
			Contact+="<div id='idd"+getcustom[i].ID+"' class='choose' onclick=addContact('"+getcustom[i].ID+"')>"+getcustom[i].checkname+"</div>";
			ContactID=getcustom[0].ID;
			}
		$("#contactID").html(Contact);
	});
	$("#ContactID").click(function(){
		$("#people").html(getcustom[0].checkname);
		ContactID=getcustom[0].ID;
	});
//获得联系人
	function addContact(q){
	var text=$("#idd"+q+"").text();
	$("#contactID").css("display","none");
	$("#PEOPLE").html(text);
	$("#people").html(text);
	ContactID=q;
	};
//获取OfferorsID并初始化
	$("#next").bind("click",function(){
		var a=$("#Biginner").text();
		var b=$("#smallinner").text();
		var c=$("#Templateinner").text();
			if(a&&b&&c){
			$("#write2").show();
			$("#write1").hide();
//关联流程	
			$.ajax({
				url:"../../processController/getprocessID.do",
				data:{},
				type:"post",
				async:false,
				success:function(data){
					var getDat=JSON.parse(data);
					process=getDat[0].processID;
					submitaccount=getDat[0].submitaccountID;//暂时未用
					OfferorsID=getDat[0].ID;//暂时获取第一个员工的ID
					}
				});
			}
			else alert("有必填项为空！");
			});
//提交销售数据并保存
	$(document).on("touchend","#Submit",function(){
		var Assignments=parseInt($("#Assignments").val());
		var ContractVolume=parseInt($("#ContractVolume").val());
		var Receivableamount=parseInt($("#Receivableamount").val());
		var Phone=parseInt($("#Phone").val());
		var Visit=parseInt($("#Visit").val());
		var Email=parseInt($("#Email").val());
		var List=parseInt($("#List").val());
		var SaleLogName=$("#SaleLogName").val();
		var CustomerName=$("#CustomerName").val();
		var Purpose=$("#Purpose").val();
		var Results=$("#Results").val();
		var timeStr = $("#time").val();
	    var Contents=$("#Contents").val();
	    var timeArray;
	    var date1;
	    var date2;
	    var date;
	    var CreateTime;
	    var myData=new Date();
	    var compare=myData.getTime();
	    console.log(compare);
	    console.log(!isNaN(Assignments));
	    console.log("123:"+timeStr);
	    if(!isNaN(Assignments))
	    	{
	    	if(!isNaN(ContractVolume))
	    		{
	    		if(!isNaN(Receivableamount))
	    			{
		    		if(!isNaN(Phone))
		    			{
		    			if(!isNaN(Email))
		    				{
		    				if(!isNaN(Visit))
		    					{
		    					if(isNaN(List))
		    						{
		    						alert("签单量输入有误！");
		    						return 0;
		    						}
		    					}
		    				else {
		    					alert("访问量输入有误！");
		    					return 0;
		    				}
		    				}
		    			else {
		    				alert("邮件量输入有误！");
		    				return 0;
		    			}
		    			}
		    		else {
		    			alert("电话量输入有误！");
		    			return 0;
		    		}
		    		}
	    		else {
	    			alert("回款量输入有误！");
	    			return 0;
	    		}
	    		}
	    	else {
	    		alert("签约量输入有误！");
	    		return 0;
	    	}
	    	}
	    else {
	    	alert("任务量输入有误！");
	    	return 0;
	    }
	    if(timeStr){
	    	timeArray = timeStr.split("T");
			date1 = timeArray[0].split("-");
			date2 = timeArray[1].split(":");
			date = new Date();
			date.setFullYear(date1[0] , date1[1] - 1 ,date1[2] );
			date.setHours(date2[0] , date2[1], 0, 0);
			CreateTime = date.getTime();
			if(CreateTime>compare)
				{
				CreateTime="";
				alert("时间输入有误！");
				return 0;
				}
	    }   
		if(CreateTime&&SaleLogName&&!isNaN(Assignments)&&!isNaN(ContractVolume)&&!isNaN(Phone)&&!isNaN(Email) &&!isNaN(Visit) &&!isNaN(List) && CustomerName  && ContactID&&Purpose&& Results&&Contents&&!isNaN(Receivableamount))
		{
			var salelog={
					saleLogName:SaleLogName,
					Assignments:Assignments,
					ContractVolume:ContractVolume,
					Phone:Phone,
					Email:Email,
					Visit:Visit,
					List:List,
					CustomerName:CustomerName,
					ContactID:ContactID,
					Purpose:Purpose,
					Results:Results,
					createTime:CreateTime,
			        Contents:Contents,
			        Receivableamount:Receivableamount,
			        templetID:ID,
			        OfferorsID:OfferorsID,
			        processID:process
			        };
			var processing={
					processName:SaleLogName,
					createTime:CreateTime,
					processType:1,
					processState:1,
					type:type
					};
//保存salelog数据
			$.ajax({
				url:"../../saleLogController/savaSaleLog.do",
				type:"post",
				data:salelog,
				async:false,
				});
//保存process数据
			$.ajax({
				url:"../../processController/savaprocess.do",
				type:"post",
				data:processing,
				async:false,
				});		
//审核
			if(type==1)
			$.ajax({
				url:"../../checkProcessController/savacheckProcess.do",
				type:"post",
				data:{
					processID:process,
					orderNumber:1,
					checkName:checkName,
					checkUserID:checkID
					},
					async:false,
					});
//记录processDescription信息
			$.ajax({
				url:"../../processDescriptionController/savaProcessDescription.do",
				type:"post",
				data:{
					ProcessID:process,
					orderNumber:1,
					type:1,
					state:1
					},
					async:false,
					success:function(data){
						if(data=="1")
						{
							alert("OK!");
							window.location.href="ProjectSubjectManagementSaleLog.html";
							}
						}
					});
			}
		else {alert("数据并未填完整！或者数据输入有误！");}
		});
//删除数据
	/*$(".Jump").click(function(){
	 * $.ajax({
	 * url:"../../saleLogController/delete.do",
	 * Type:"post",
	 * data:{ID:"20151203204807915"},
	 * async:false,
	 * success:function(data){
	 * alert(data);
	 * if(data==1)
	 * {
	 * alert("success!");
	 * }
	 * else alert("fail!");
	 * }
	 * });
	 * });
	 */