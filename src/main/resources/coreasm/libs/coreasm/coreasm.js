require.config({
    paths: {
        'bmotion': '/bms/libs/bmotion/bmotion'
    }
});
define(['require', 'bmotion'], function (require, bmotion) {

    bmotion.socket.on('initialisation', function (data) {
        $(function () {
            console.log(data)

            $("#bmotion-label").html("BMotion Studio for CoreASM")
            $("#bmotion-navigation").append('<li class="dropdown">' +
            '                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="bt_doStep">Do Step</span></a>' +
            '                    </li>')
            $("#bt_doStep").click(function () {
                bmotion.executeEvent("na", {
                    callback: function(data) {
                        console.log(data)
                    }
                })
            });
        });
    });

});
