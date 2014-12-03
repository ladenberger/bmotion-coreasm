import de.bms.observer.BMotionObserver

// The bms variable is the entry point to your current
// CoreAsmVisualisation instance
System.out.println(bms)

// Example custom observer
def customObserver = [
        apply: {
            System.out.println("Triggering custom observer")
        }
] as BMotionObserver
// Register custom observer, so that it is called
// after every state change
bms.registerObserver(customObserver)

// Example method observer
// This observer calls a method called "mymethod"
// on the client side
callMethod("mymethod") {
    data([foo: "bar", foo2: "bar2"])
    register(bms)
}

// Example transform observer. In this case the value
// to be set is a Groovy closure that returns
// the value of evaluating the expression "ruleElement"
transform("#mydiv") {
    set "content", { bms.eval("ruleElement") }
    register(bms)
}

// INFO: You can use the entire power and feature range of
// Groovy for defining your observers

// Register a method called "groovyMethod" on the server
// side that can be called from the client side
bms.registerMethod("groovyMethod", { data ->
    // The data contains a json object from the client side
    System.out.println(data)
    // Do some stuff on server side ...
    // ... and return a json object to the client
    return [foo: "bar"]
})