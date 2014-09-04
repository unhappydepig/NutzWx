/**
 * Created by Wizzer on 14-9-3.
 */
var Index = {
    init: function () {
        this.handleIndex();
    },
    handleIndex: function () {
        //设置Loading样式
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
        //实现菜单URL内容的AJAX加载
        $("#page_menu").find("a").each(function () {
            $(this).click(function () {
                var url = $(this).attr("href");
                if (url && url.indexOf("javascript") < 0 && url != "index") {
                    spinner.spin(spinContainer);
                    $("#page-content").load($(this).attr("href"), function () {
                        spinner.spin();
                    });
                    return false;
                }

            });
        });
        //修改个人资料
        $("#user-info").click(function(){
            var form = $("<form class='form-inline'><label>个人资料 &nbsp;</label></form>");
            form.load(APP_BASE+"/private/sys/user/info");
            form.validate({
                errorElement : 'span',
                errorClass : 'help-block',
                focusInvalid : false,
                rules : {
                    realname : {
                        required : true
                    },
                    oldpassword:{
                        required : true
                    },
                    password : {
                        required : true
                    },
                    password2 : {
                        required : true
                    }
                },
                messages : {
                    realname : {
                        required : "请输入姓名"
                    },
                    oldpassword : {
                        required : "请输入原密码"
                    },
                    password : {
                        required : "请输入新密码"
                    },
                    password2 : {
                        required : "请再输一次密码"
                    }
                }
            });
            var div = bootbox.dialog({
                message: form,
                buttons: {
                    "cancel" : {
                        "label" : "关闭",
                        "className" : "btn btn-default"
                    },
                    "confirm" : {
                        "label" : "确定",
                        "className" : "btn btn-primary",
                        "callback": function() {
                                form.submit();
//                            spinner.spin(spinContainer);
//                            $.ajax({
//                                type: "POST",
//                                url: APP_BASE+"/private/sys/user/updateInfo",
//                                data: form.serialize(),
//                                dataType: "json",
//                                success: function(data){
//                                    spinner.spin();
//                                    if(data.type=="success"){
//                                        div.modal("hide");
//                                        bootbox.alert("修改成功");
//                                    }else{
//                                        bootbox.alert(data.content);
//                                    }
//                                }
//                            });
                            return false;
                        }
                    }
                }

            });

        });
    }
};