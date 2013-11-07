package lordmau5.powerboxes.compatibility.buildCraft;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Lordmau5
 * Date: 01.11.13
 * Time: 14:32
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class BuildCraftCompatibility {
    Map<IPowerReceptor, PowerHandler> powerHandlers = new HashMap<IPowerReceptor, PowerHandler>();

    public void addPowerHandler(IPowerReceptor receptor, PowerHandler.Type type) {
        if(powerHandlers.containsKey(receptor))
            return;
        powerHandlers.put(receptor, new PowerHandler(receptor, type));
    }

    public PowerHandler getPowerHandler(IPowerReceptor receptor) {
        if(!powerHandlers.containsKey(receptor))
            return null;
        return powerHandlers.get(receptor);
    }

    public void configurePowerHandler(PowerHandler handler, int minReceived, int maxReceived, int activation, int maxStored) {
        handler.configure(minReceived, maxReceived, activation, maxStored);
    }

    public void configurePerdition(PowerHandler handler, int powerLoss, int powerLossRegularity) {
        handler.configurePowerPerdition(powerLoss, powerLossRegularity);
    }
}