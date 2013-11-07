package lordmau5.powerboxes.power;

import lordmau5.powerboxes.Config;
import lordmau5.powerboxes.world.tileentity.TileEntityLinkBox;

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

    public Map<Integer, ArrayList<TileEntityLinkBox>> linkBoxes;
    public Map<Integer, Boolean> initiatedNBTPower;
    public Map<Integer, Integer> networkPower;

    public LinkBoxNetwork() {
        initNetwork();
    }

    public void initNetwork() {
        linkBoxes = new HashMap<Integer, ArrayList<TileEntityLinkBox>>();
        initiatedNBTPower = new HashMap<Integer, Boolean>();
        networkPower = new HashMap<Integer, Integer>();
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
        if(list.size() == 0)
            setNetworkPower(oldLinkID, 0);
    }

    public int neededPower(int linkId) {
        int power = getNetworkPower(linkId);
        return Config.powerBox_capacity - power;
    }

    public void setNetworkPower(int linkId, int power) {
        if(power > Config.powerBox_capacity)
            power = Config.powerBox_capacity;
        networkPower.put(linkId, power);
    }

    public int getNetworkPower(int linkId) {
        if(networkPower.get(linkId) == null)
            return 0;
        return networkPower.get(linkId);
    }
}