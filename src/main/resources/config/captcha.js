var ioc = {
	fastHashMapCaptchaStore : {
		type : "com.octo.captcha.service.captchastore.FastHashMapCaptchaStore"
	},
	mailEngine : {
		type : "cn.xuetang.mvc.GMailEngine",
		args : [ "cn/xuetang/common/captcha/images" ]
	},
	imageCaptchaService : {
		type : "com.octo.captcha.service.image.DefaultManageableImageCaptchaService",
		args : [ {
			refer : "fastHashMapCaptchaStore"
		}, {
			refer : "mailEngine"
		}, 180, 100000, 75000 ]
	},
	jPEGView : {
		type : "cn.xuetang.mvc.JPEGView",
		args : [ {
			refer : "imageCaptchaService"
		} ]
	}
};