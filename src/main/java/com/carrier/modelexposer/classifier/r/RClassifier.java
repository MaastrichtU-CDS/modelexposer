package com.carrier.modelexposer.classifier.r;

import org.renjin.script.RenjinScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;

public final class RClassifier {

    private RClassifier() {
    }


    public static double run() throws IOException, URISyntaxException, ScriptException {
//        String fileContent = RClassifier.getRScript();
        RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
        // create a Renjin engine:
        ScriptEngine engine = factory.getScriptEngine();

        StringWriter outputWriter = new StringWriter();
        engine.getContext().setWriter(outputWriter);
        engine.eval("print(df <- data.frame(x=1:10, y=(1:10)+rnorm(n=10)))");

        String output = outputWriter.toString();

        //output is stored as string, can evaluate an arbitrary function

        return 0;
    }

    public static void main(String[] args) throws ScriptException, IOException, URISyntaxException {
        RClassifier.run();
    }
}
