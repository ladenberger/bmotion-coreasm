package de.bms.coreasm

import de.bms.BMotion
import de.bms.ImpossibleStepException
import groovy.util.logging.Slf4j
import org.coreasm.engine.CoreASMEngine
import org.coreasm.engine.CoreASMEngineFactory
import org.coreasm.engine.Engine
import org.coreasm.engine.absstorage.*
import org.coreasm.engine.plugins.blockrule.TabBlocksPlugin

@Slf4j
public class CoreAsmVisualisation extends BMotion {

    def CoreASMEngine engine;

    public CoreAsmVisualisation(final UUID sessionId, final String templatePath) {
        super(sessionId, templatePath);
    }

    @Override
    public Object executeEvent(final String event, final data) throws ImpossibleStepException {
        engine.step();
        engine.waitWhileBusy();
        StringBuffer output = new StringBuffer();
        String state = getCurrentState();
        output.append("{ \"mode\": \"" + engine.getEngineMode().toString() + "\", \"state\": " + state);
        checkObserver()
        return output.toString();
    }

    /**
     * Returns the value of a location    */
    @Override
    public Object eval(String lname) {
        State state = engine.getState();
        Map<String, FunctionElement> funcs = state.getFunctions();

        String value = "";

        FunctionElement f = funcs.get(lname);

        String name = lname;
        HashMap<String, String> val = new HashMap<String, String>();

        for (Location l : f.getLocations(name)) {

            String tempStr = "";
            for (Element arg : l.args)
                tempStr = tempStr + arg.denotation() + ", ";
            if (tempStr.length() > 0)
                tempStr = tempStr.substring(0, tempStr.length() - 2);

            value = (f.getValue(l.args).denotation());

            val.put(tempStr, value);

        }

        String json = "{";
        for (String k : val.keySet()) {
            json += "\"" + (k == "" ? lname : k) + "\":" + "\"" + val.get(k)
            +"\", ";

        }
        if (val.size() > 0)
            json = json.substring(0, json.length() - 2);
        json += "}";

        return json;
    }

    public String getCurrentState() {
        Map<String, AbstractUniverse> universeEntries = engine.getState()
                .getUniverses();

        /*
         * { "backgrounds" : [ "b1", ..., "bn" ], "universes" : {"u1" : ["val",
         * ...]}, ..., {"un" ...}, "functions" : {"f1" : { "params": ["p1", ..,
         * "pn"], "value": "val" } , ..., {"fn" ...} }
         */

        StringWriter backgroundsWriter = new StringWriter();
        StringWriter universesWriter = new StringWriter();
        StringWriter functionsWriter = new StringWriter();

        backgroundsWriter.write("\"backgrounds\": [");
        universesWriter.write("\"universes\" : {");

        for (String key : universeEntries.keySet()) {
            AbstractUniverse o = universeEntries.get(key);
            // bg elements
            if (o instanceof BackgroundElement) {
                backgroundsWriter.write("\"" + key + "\", ");
            }
            // universes elements
            if (o instanceof UniverseElement) {
                universesWriter.write("\"" + key + "\" : [");
                UniverseElement ue = (UniverseElement) o;

                StringBuffer str = new StringBuffer();
                String lname = o.toString();
                Set<Location> locations = ue.getLocations(lname);

                for (Location l : locations) {
                    if (ue.getValue(l.args).equals(BooleanElement.TRUE)) {
                        if (l.args.size() > 0) {
                            str.append("\"" + l.args.get(0).denotation() + "\", ");
                        }
                    }
                }

                universesWriter.write(str.length() > 4 ? str.substring(0,
                        str.length() - 2) : "");
                universesWriter.write("], ");
            }
        }

        // functions
        Map<String, FunctionElement> felems = engine.getState().getFunctions();

        functionsWriter.write("\"functions\" : {");
        for (String key : felems.keySet()) {
            FunctionElement f = felems.get(key);

            if (f.isModifiable() && key != "output") {
                String params = "[";
                String value = "[";
                Set<Location> locs = f.getLocations(key);

                for (Location l : locs) {

                    for (Element arg : l.args) {
                        params += "\"" + arg.denotation() + "\", ";
                    }

                    value += "\"" + f.getValue(l.args).denotation() + "\", ";
                }

                if (params.endsWith(", "))
                    params = params.substring(0, params.length() - 2);
                if (value.endsWith(", "))
                    value = value.substring(0, value.length() - 2);

                value += "]";
                params += "]";

                functionsWriter.write("\"" + key + "\": { \"params\": " + params + ", \"value\": " + value + " }, ");
            }
        }

        backgroundsWriter.write("]");
        universesWriter.write("}");
        functionsWriter.write("}");

        String bg = backgroundsWriter.toString();
        if (bg.length() > 0)
            bg = bg.substring(0, bg.length() - 3) + "]";

        String univ = universesWriter.toString();
        if (univ.endsWith(", }"))
            univ = univ.substring(0, univ.length() - 3) + "}";

        String func = functionsWriter.toString();
        if (func.endsWith(", }"))
            func = func.substring(0, func.length() - 3) + "}";

        try {
            backgroundsWriter.close();
            universesWriter.close();
            functionsWriter.close();
        } catch (IOException e1) {
            // TODO check how to handle properly
            e1.printStackTrace();
        }

        return "{" + bg + ", " + univ + ", " + func + "}";

    }

    public List<String> getErrors(String arg0, String arg1) {
        List<String> errors = new ArrayList<String>();
        errors.add(engine.getEngineMode().toString());

        // TODO: get better errors
        return errors;
    }


    @Override
    void loadModel(File modelFile, boolean force) {
        log.info "Loading model " + modelFile
        if (engine == null) {
            engine = CoreASMEngineFactory.createEngine();
        }
        try {
            FileInputStream fi = new FileInputStream(modelFile)
            engine.initialize();
            engine.waitWhileBusy();
            engine.loadSpecification(new InputStreamReader(fi));
            engine.waitWhileBusy();
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    @Override
    void refresh() {
        checkObserver()
    }

}
