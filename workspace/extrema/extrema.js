require(['coreasm'], function (coreasm) {

    // This method is called after every state change / after do step
    // This method is registered in the Groovy script "extrema.groovy"
    coreasm.observe("method", {
        name: "mymethod",
        trigger: function (data) {
            // Do something with data
        }
    });

    // Register a click listener on the object with the id "#mybutton"
    // This is jQuery! You can use the entire power of jQuery.
    $("#mybutton").click(function () {
        // Call the registered method "groovyMethod" on the server side
        // with the given data and the callback. The callback is called
        // whenever the method on the server side returns some json objects
        coreasm.callMethod({
            name: "groovyMethod",
            foo: "bar",
            callback: function (data) {
                // Data contains a json object
                // that is returned from the server side
                console.log(data)
            }
        })
    });

    $("#mydiv").observe("formula", {
        formulas: ["someformula"],
        trigger: function (origin, data) {
            // Receive the results of the registered formulas
            console.log(data)
        }
    });


});
