/**
 * Created by Wizzer on 14-9-2.
 */
var Login = {
    init: function () {
        this.initLogin();
        this.handleLogin();
    },
    initLogin: function () {
        var rememberMe = $.cookie("rememberMe");
        if (rememberMe) {
            $("#rememberMe").prop("checked", true);
            $("#rememberMe").parent().addClass("checked");
        }
    },
    handleLogin: function () {
        var opts = {
            lines: 12, // The number of lines to draw
            length: 7, // The length of each line
            width: 5, // The line thickness
            radius: 10, // The radius of the inner circle
            corners: 1, // Corner roundness (0..1)
            rotate: 0, // The rotation offset
            direction: 1, // 1: clockwise, -1: counterclockwise
            color: '#ff892a', // #rgb or #rrggbb or array of colors
            speed: 1, // Rounds per second
            trail: 100, // Afterglow percentage
            shadow: false, // Whether to render a shadow
            hwaccel: true, // Whether to use hardware acceleration
            className: 'spinner', // The CSS class to assign to the spinner
            zIndex: 2e9, // The z-index (defaults to 2000000000)
            top: '50%', // Top position relative to parent
            left: '50%' // Left position relative to parent
        };
        var spinContainer = $("#spin-container").get(0);
        var spinner = new Spinner(opts);
        $('#btn-submit').click(function () {
            $('#login-form').submit();
        });
        $('#login-form').validate({
            errorElement: 'div',
            errorClass: 'help-block',
            focusInvalid: false,
            onfocusout: function (element) {
                $(element).valid();
            },
            rules: {
                password: {
                    required: true
                },
                username: {
                    required: true
                },
                captcha:  {
                    required: true
                }
            },

            messages: {
                username: "用户名不能为空",
                password: "密码不能为空",
                captcha: '验证码不能为空'
            },
            invalidHandler: function (event, validator) {
            },
            highlight: function (e) {
                $(e).closest('label').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('label').removeClass('has-error').addClass('has-info');
                $(e).remove();
            },
            errorPlacement: function (error, element) {
                error.insertAfter(element.parent());
            },
            submitHandler: function (form) {
                spinner.spin(spinContainer);
                $.ajax({
                    type: 'POST',
                    url: APP_BASE + "/private/doLogin",
                    dataType: 'json',
                    data: $(form).serialize(),
                    success: function (data) {
                        spinner.spin();
                        if (data.type == "success") {
                            if($("#rememberMe").is(":checked")){
                                $.cookie("rememberMe", "true", { expires: 24 });
                            }else{
                                $.cookie("rememberMe","");
                            }
                            window.location = APP_BASE+"/private/index";
                        } else {
                            bootbox.alert(data.content);
                            $('#captcha').val("");
                            Login.refreshCaptcha();
                        }
                    },
                    error: function (xhr, error, status) {
                        spinner.spin();
                        bootbox.alert("服务器出错，请联系管理员。");
                    }
                });
            }
        });

        $("input").keydown(function (e) {
            if (e.keyCode == 13) {
                $('#login-form').submit()
            }
        });
        $("#captchaid").click(function () {
            $("#captchaid").attr("src", APP_BASE + "/captcha?rnd=" + new Date().getTime());
        });
    },
    refreshCaptcha: function () {
        $("#captchaid").attr("src", APP_BASE + "/captcha?rnd=" + new Date().getTime());
    }
};