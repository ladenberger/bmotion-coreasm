package de.bms.coreasm

import de.bms.BMotion
import de.bms.BMotionVisualisationProvider

public class CoreAsmVisualisationProvider implements BMotionVisualisationProvider {

    @Override
    BMotion get(String type, String templatePath) {
        if ("CoreAsm".equals(type)) {
            return new CoreAsmVisualisation(UUID.randomUUID(), templatePath)
        }
        return null
    }

}
