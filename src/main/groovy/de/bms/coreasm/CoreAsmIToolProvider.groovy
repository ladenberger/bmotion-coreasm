package de.bms.coreasm

import de.bms.itool.ITool
import de.bms.itool.ToolRegistry
import de.bms.server.BMotionIToolProvider

public class CoreAsmIToolProvider implements BMotionIToolProvider {

    @Override
    ITool get(String tool, ToolRegistry toolRegistry) {
        if ("CoreAsm".equals(tool)) {
            return new CoreAsmTool(UUID.randomUUID().toString(), toolRegistry)
        }
        return null
    }

}
