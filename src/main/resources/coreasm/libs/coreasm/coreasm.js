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
            console.log($("#bmotion-label"))
        });
    });

});
