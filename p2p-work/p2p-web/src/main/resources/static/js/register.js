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

//注册协议确认
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
	//注册功能
	//手机号的验证
	$("#phone").on('blur',function () {
		var phone = $.trim($("#phone").val() );
		if(""==phone){
			showError("phone","手机号不能为空")
		}else if(!/^1[1-9]\d{9}$/.test(phone)){
			showError("phone","手机号有误")
		}else {
			//发送ajax,检查手机号是否已经注册过
			$.ajax({
				url:contextPath+"/loan/checkPhone",
				data:{
					phone:phone
				},
				dataType:'json',
				success:function (data) {
					if(data.code==200){
					showSuccess("phone",data.message)
					}else {
						showError("phone",data.message)
					}
				},
				error:function () {
					showError("phone","请求错误，稍后重试")
				}
			})
		}

	})
	//手机号验证完成
	//密码的blur事件
	$("#loginPassword").on('blur',function () {
		var loginPassword = $.trim($("#loginPassword").val() )
		if(loginPassword==null||loginPassword==""){
			showError("loginPassword","密码不能为空")
		}/*else if(loginPassword.length<6){
			alert(loginPassword.length)
			showError("loginPassword","密码需是大于6位的数字与字母组合")
		}*/else if(!/^[0-9a-zA-Z]+$/.test(loginPassword)){
			showError("loginPassword","密码字符只可使用数字和大小写英文字母")
		}else {
			showSuccess("loginPassword")
		}
	})
	//密码的blur事件完成
	//验证码的blur事件
	$("#messageCode").on("blur",function () {
		var code = $("#messageCode").val();
		if (code==""){
			showError("messageCode","请输入验证码");
		} else if(code.length!=4){
			showError("messageCode","验证码不正确")
		}else {
			showSuccess("messageCode")
		}
	})
	//验证码的blur事件完成
	//倒计时点击事件
	$("#messageCodeBtn").on('click',function () {
		$("#phone").blur()
		$("#loginPassword").blur()
		var text = $("#phoneErr").text()
		var text1 = $("#loginPasswordErr").text();
		if (""==text && ""==text1 ){
			$("#loginPassword").blur()
			$.leftTime(5,function(d){

				var second = parseInt(d.s);
				if(second!=0){
					$("#messageCodeBtn").addClass("on")
					$("#messageCodeBtn").attr("disabled",true)
					$("#messageCodeBtn").text(second!=0?second+"秒后获取":""+"获取验证码")
				} else {
					$("#messageCodeBtn").removeClass("on")
					$("#messageCodeBtn").attr("disabled",false)
					$("#messageCodeBtn").text("获取验证码")
				}
			});
			//发送短信验证码
			$.ajax({
				url:contextPath+"/loan/sendCode",
				data:{
					phone:$.trim($("#phone").val() )
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					alert("data="+data.data)
				}
			})
		}

	})
	//倒计时点击事件完成
	//注册按钮点击事件
	$("#btnRegist").on("click",function () {
		//验证数据,phone,loginPassword,messageCode
		$("#phone").blur()
		$("#loginPassword").blur()
		$("#messageCode").blur()
		var errorText = $("div[id$=Err]").text();
		alert("errorText="+errorText)
		if(""==errorText){
			//可以注册
			var phone = $("#phone").val()
			var pwd = $("#loginPassword").val();
			var code = $.trim($("#messageCode").val() );
			$.ajax({
				url:contextPath+"/loan/register",
				data:{
					phone:phone,
					pwd:$.md5(pwd),
					code:code
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					//跳转实名认证
					if(true){
						alert(111)
						window.location.href=contextPath+"/loan/realName";
						alert(222)
					}else {
						alert(data.message)
					}
				},
				error:function (data) {
					alertBox("shibai")
				}
			})
		}
	})
});
