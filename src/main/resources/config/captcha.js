var ioc = {
	fastHashMapCaptchaStore : {
		type : "com.octo.captcha.service.captchastore.FastHashMapCaptchaStore"
	},
	mailEngine : {
		type : "org.nutz.weixin.mvc.GMailEngine",
		args : [ "cn/xuetang/captcha" ]
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
		type : "org.nutz.weixin.mvc.JPEGView",
		args : [ {
			refer : "imageCaptchaService"
		} ]
	}
};