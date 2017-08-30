package edu.kit.iti.formal.psdbg.interpreter.assignhook;

import edu.kit.iti.formal.psdbg.interpreter.data.KeyData;
import edu.kit.iti.formal.psdbg.parser.data.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;

/**
 * @author Alexander Weigl
 * @version 1 (21.08.17)
 */
public class KeyAssignmentHook extends DefaultAssignmentHook<KeyData> {
    private static Logger logger = LogManager.getLogger(KeyAssignmentHook.class);

    public KeyAssignmentHook() {
        register("MAX_AUTO_STEPS", this::setMaxAutosteps, this::getMaxAutosteps);
        //register()
    }

    public Value<BigInteger> getMaxAutosteps(KeyData data) {
        return Value.from(100);
    }

    public boolean setMaxAutosteps(KeyData data, Value<BigInteger> val) {
        return true;
    }
}
