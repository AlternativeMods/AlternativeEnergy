package jkmau5.alternativeenergy.client;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import lombok.Getter;

import java.util.EnumSet;

/**
 * No description given
 *
 * @author jk-5
 */
public class TickHandlerClient implements ITickHandler {

    @Getter private static long ticks = 0;
    @Getter private static float partialTicks = 0f;

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        if(type.contains(TickType.CLIENT)) {
            ticks ++;
        } else if(type.contains(TickType.RENDER)) {
            partialTicks = (Float) tickData[0];
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {

    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT, TickType.RENDER);
    }

    @Override
    public String getLabel() {
        return this.getClass().getName();
    }
}
