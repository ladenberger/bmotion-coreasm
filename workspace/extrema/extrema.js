require.config({
    paths: {
        'coreasm': '/bms/libs/coreasm/coreasm'
    }
});
define(["require","coreasm"], function(require) {

    var bms = require('bmotion')

    // This method is called after every state change / after do step
    // This method is registered in the Groovy script "extrema.groovy"
    bms.socket.on('mymethod', function (data) {
        // Do something with data
    });

    // Register a click listener on the object with the id "#mybutton"
    // This is jQuery! You can use the entire power of jQuery.
    $("#mybutton").click(function() {
        // Call the registered method "groovyMethod" on the server side
        // with the given data and the callback. The callback is called
        // whenever the method on the server side returns some json objects
        bms.callMethod("groovyMethod",
            {
                foo: "bar",
                callback: function(data) {
                    // Data contains a json object
                    // that is returned from the server side
                    console.log(data)
                }
            }
        )
    });


});
