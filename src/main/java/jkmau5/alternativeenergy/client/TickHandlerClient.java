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

    @Getter
    private static long ticks = 0;

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        ticks ++;
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {

    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return this.getClass().getName();
    }
}
