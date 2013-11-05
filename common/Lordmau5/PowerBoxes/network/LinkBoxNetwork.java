package Lordmau5.PowerBoxes.network;

import Lordmau5.PowerBoxes.core.Config;
import Lordmau5.PowerBoxes.tile.TileEntityLinkBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Lordmau5
 * Date: 02.11.13
 * Time: 23:20
 * You are allowed to change this code,
 * however, not to publish it without my permission.
 */
public class LinkBoxNetwork {

    public Map<Integer, ArrayList<TileEntityLinkBox>> linkBoxes = new HashMap<Integer, ArrayList<TileEntityLinkBox>>();
    public Map<Integer, Boolean> initiatedNBTPower = new HashMap<Integer, Boolean>();

    public LinkBoxNetwork() {

    }

    public void addLinkBoxToNetwork(TileEntityLinkBox linkBox, int id) {
        ArrayList<TileEntityLinkBox> list = linkBoxes.get(id);
        if(list == null) {
            list = new ArrayList<TileEntityLinkBox>();
        }
        list.add(linkBox);
        linkBoxes.put(id, list);
    }

    public void initiateNetworkPower(int linkId, int power) {
        if(initiatedNBTPower.get(linkId) == null || initiatedNBTPower.get(linkId) == false) {
            initiatedNBTPower.put(linkId, true);
            setNetworkPower(linkId, power);
        }
    }

    public TileEntityLinkBox getFirstOfLink(int linkId) {
        if(linkBoxes.get(linkId) == null || linkBoxes.get(linkId).isEmpty())
            return null;
        return linkBoxes.get(linkId).get(0);
    }

    public void removeFromLink(TileEntityLinkBox linkBox, int oldLinkID) {
        ArrayList<TileEntityLinkBox> list = linkBoxes.get(oldLinkID);
        if(list == null)
            return;
        list.remove(linkBox);
        linkBoxes.put(oldLinkID, list);
        System.out.println(list.size());
        if(list.size() == 0)
            setNetworkPower(oldLinkID, 0);
    }

    public void addNetworkPower(int linkId, int power) {
        int oldPower = getNetworkPower(linkId);
        oldPower += power;
        if(oldPower > Config.powerBox_capacity)
            oldPower = Config.powerBox_capacity;
        setNetworkPower(linkId, oldPower);
    }

    public void drainNetworkPower(int linkId, int power) {
        int oldPower = getNetworkPower(linkId);
        oldPower -= power;
        if(oldPower < 0)
            oldPower = 0;
        setNetworkPower(linkId, oldPower);
    }

    public int neededPower(int linkId) {
        int power = getNetworkPower(linkId);
        return Config.powerBox_capacity - power;
    }

    public void setNetworkPower(int linkId, int power) {
        if(power > Config.powerBox_capacity)
            power = Config.powerBox_capacity;
        if(getFirstOfLink(linkId) == null || !getFirstOfLink(linkId).doesExist())
            return;
        getFirstOfLink(linkId).setPowerStored(power, true);
    }

    public int getNetworkPower(int linkId) {
        if(getFirstOfLink(linkId) == null || !getFirstOfLink(linkId).doesExist())
            return 0;
        return getFirstOfLink(linkId).getPowerStored(true);
    }
}