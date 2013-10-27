package core;

import net.minecraft.tileentity.TileEntity;
import tile.TileEntityPowerCable;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lordmau5
 * Date: 27.10.13
 * Time: 20:38
 * You are not allowed to change this code,
 * nor publish it without my permission.
 */
public class EnergyNetwork {
    public List<TileEntityPowerCable> cables = new ArrayList<TileEntityPowerCable>();
    public List<TileEntity> inputs = new ArrayList<TileEntity>();
    public List<TileEntity> outputs = new ArrayList<TileEntity>();

    public int networkPower;
    public int maxNetworkPower = 5000;

    public EnergyNetwork(TileEntityPowerCable headCable) {
        cables.add(headCable);
    }

    public void addCable(TileEntityPowerCable cable) {
        if(cables.contains(cable))
            return;
        cables.add(cable);
    }

    public void addInput(TileEntity input) {
        if(inputs.contains(input))
            return;
        inputs.add(input);
    }

    public void removeInput(TileEntity input) {
        if(!inputs.contains(input))
            return;
        inputs.remove(input);
    }

    public void addOutput(TileEntity output) {
        if(outputs.contains(output))
            return;
        outputs.add(output);
    }

    public void removeOutput(TileEntity output) {
        if(!outputs.contains(output))
            return;
        outputs.remove(output);
    }

    public void recalculateNetworks(TileEntityPowerCable exclude) {
        for(TileEntityPowerCable cable : cables) {
            cable.initializeNetwork();
        }
    }

    public EnergyNetwork mergeNetworks(EnergyNetwork headNetwork, EnergyNetwork soldierNetwork) {
        for(TileEntityPowerCable cable : soldierNetwork.cables) {
            headNetwork.addCable(cable);
            cable.setEnergyNetwork(headNetwork);
        }
        for(TileEntity input : soldierNetwork.inputs)
            headNetwork.addInput(input);
        for(TileEntity output : soldierNetwork.outputs)
            headNetwork.addOutput(output);

        return headNetwork;
    }

    public void setPower(int energy) {
        networkPower = energy;
        if(networkPower > maxNetworkPower)
            networkPower = maxNetworkPower;
    }

    public int addPower(int energy) {
        int returning = 0;

        networkPower += energy;
        if(networkPower > maxNetworkPower) {
            returning = networkPower - maxNetworkPower;
            networkPower = maxNetworkPower;
        }
        return returning;
    }

    public void drainPower(int energy) {
        networkPower -= energy;
        if(networkPower < 0)
            networkPower = 0;
    }
}