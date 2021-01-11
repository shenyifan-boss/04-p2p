
//同意实名认证协议
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
	//真实姓名失焦事件
	$("#realName").on('blur',function () {
		var Name = $("#realName").val()
		if(""==Name){
			showError("realName","请输入" +
				"姓名")
		}else if(! (/^[\u4e00-\u9fa5]{0,}$/.test(Name) ) || Name.length<2||Name.length>20){
			showError("realName","请输入正确的姓名")
		}else {
			showSuccess("realName")
		}
	})
	//真实姓名失焦事件完成
	//身份证号失焦事件
	$("#idCard").on('blur',function () {
		var idCard = $("#idCard").val();
		if( !(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard) )){
			showError("idCard","身份证号有误")
		}else {
			showSuccess("idCard")
		}
	})
	//身份证号失焦事件完成
	//注册按钮单击事件
	$("#btnRegist").on('click',function () {
		var Name = $("#realName").val()
		var idCard = $("#idCard").val();
		var result=$("div[id$=Err]").text();
		alert(result)
		if(Name!=''&&idCard!=''&&result==''){
			//发送请求
			$.ajax({
				url:contextPath+"/loan/reRealName",
				data:{
					Name:Name,
					idCard:idCard
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					alert("成功")
					//跳转首页
					window.location.href=contextPath+"/loan/myCenter"
				},
				error:function () {
					alert("请稍后重试")
				}
			})
		}
	})
	//注册按钮单击事件完成
});
//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}
