package cn.xuetang.common.exception;


public class IncorrectCaptchaException extends org.apache.shiro.ShiroException {

	private static final long serialVersionUID = 3315875923669742156L;

	public IncorrectCaptchaException() {
		super();
	}

	public IncorrectCaptchaException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectCaptchaException(String message) {
		super(message);
	}

	public IncorrectCaptchaException(Throwable cause) {
		super(cause);
	}
}
