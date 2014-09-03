/**
 * Created by Wizzer on 14-9-3.
 */
var Index = {
    init: function () {
        this.handleIndex();
    },
    handleIndex: function () {
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
    }
};