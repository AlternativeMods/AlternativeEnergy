package Lordmau5.PowerBoxes.network;

import net.minecraft.tileentity.TileEntity;
import Lordmau5.PowerBoxes.tile.TileEntityPowerCable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Lordmau5
 * Date: 27.10.13
 * Time: 20:38
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class EnergyNetwork {
    public List<TileEntityPowerCable> cables = new ArrayList<TileEntityPowerCable>();
    public Map<TileEntity, TileEntityPowerCable> inputs = new HashMap<TileEntity, TileEntityPowerCable>();

    public int networkPower;
    public int maxNetworkPower = 5000;

    public EnergyNetwork(TileEntityPowerCable headCable) {
        cables.add(headCable);
    }

    public int getNetworkPower() {
        return networkPower;
    }

    public void addCable(TileEntityPowerCable cable) {
        if(cables.contains(cable))
            return;
        cables.add(cable);
    }

    public void addInput(TileEntityPowerCable cable, TileEntity input) {
        if(inputs.containsKey(input))
            return;
        inputs.put(input, cable);
    }

    public void removeInput(TileEntityPowerCable cable, TileEntity input) {
        if(!inputs.containsKey(input))
            return;
        inputs.put(input, cable);
    }

    public boolean isAcceptor(TileEntityPowerCable cable, TileEntity input) {
        if(!inputs.containsKey(input))
            return false;
        if(inputs.get(cable) == input)
            return true;
        return false;
    }

    public void recalculateNetworks() {
        for(TileEntityPowerCable cable : cables) {
            cable.initializeNetwork();
        }
    }

    public EnergyNetwork mergeNetworks(EnergyNetwork headNetwork, EnergyNetwork soldierNetwork) {
        for(TileEntityPowerCable cable : soldierNetwork.cables) {
            headNetwork.addCable(cable);
            cable.setEnergyNetwork(headNetwork);
        }

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