package compatibility.buildCraft;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;

/**
 * Author: Lordmau5
 * Date: 01.11.13
 * Time: 14:49
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public interface BuildCraftCompatibilityInterface {
    public void addPowerHandler(IPowerReceptor receptor, PowerHandler.Type type);

    public PowerHandler getPowerHandler(IPowerReceptor receptor);

    public void configurePowerHandler(PowerHandler handler, int minReceived, int maxReceived, int activation, int maxStored);

    public void configurePerdition(PowerHandler handler, int powerLoss, int powerLossRegularity);
}