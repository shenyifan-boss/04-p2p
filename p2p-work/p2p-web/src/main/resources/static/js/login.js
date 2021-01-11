/*var referrer = "";//登录后返回页面*/
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
referrer = document.referrer;
if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
}

//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		$("#loginId").click()
	}
});

$(function () {
	$("#phone").on('blur',function () {
		var phone = $("#phone").val();
		if(phone.length!=11){
			showError("phone","请检查手机号长度")
		}else if(! /^1[1-9]\d{9}$/.test(phone)){
			showError("phone","手机号不正确")
		}else {
			showSuccess("phone")
		}
	})

	$("#loginPassword").on("blur",function () {
		var loginPassword = $("#loginPassword").val();
		if(loginPassword.length==0){
			showError("loginPassword","密码不能为空")
		}else if( !/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/.test(loginPassword) ){
			showError("loginPassword","密码格式不正确")
		}else {
			showSuccess("loginPassword")
		}
	})

	//注册发ajax
	$("#loginBtn").on('click',function () {
		$("#phone").blur()
		$("#loginPassword").blur()
		var text = $("div[id$=Err]").text();
		var url = contextPath+'/loan/loginVerification'
		alert(url)
		if(""==text){
			var phone = $("#phone").val();
			var loginPassword = $("#loginPassword").val();
			$.ajax({
				url:contextPath+'/loan/loginVerification',
				data:{
					phone:phone,
					loginPassword:$.md5(loginPassword)
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code==200&&data.error==1000){
						//返回returnStr地址
						window.location.href=$("#hiddenUrl").val()
					}
				},
				error:function () {
					alert("ajax失败")
				}
			})
		}
	})
})


