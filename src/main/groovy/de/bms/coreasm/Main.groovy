package de.bms.coreasm

import com.google.common.io.Resources
import de.bms.DesktopApi
import de.bms.server.BMotionServer

public class Main {

    public static void main(final String[] args) throws InterruptedException {

        // Start BMotion Server
        BMotionServer server = new BMotionServer(args)

        //server.setScriptEngineProvider(new ProBScriptEngineProvider())
        server.setIToolProvider(new CoreAsmIToolProvider())

        String[] paths = [Resources.getResource("coreasm").toString()]
        server.setResourcePaths(paths)

        server.start()

        /*server.socketServer.getServer().addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                client.sendEvent("initProB", [host: "localhost", port: probPort])
            }
        });*/

        openBrowser(server)

    }

    static def openBrowser(BMotionServer server) {
        java.net.URI uri = new java.net.URI("http://" + server.host + ":" + server.port + "/bms/")
        DesktopApi.browse(uri)
    }

}
