package jkmau5.alternativeenergy.client.render;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;
import jkmau5.alternativeenergy.AltEngLog;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class ToolTip extends ForwardingList<ToolTipLine> {

    private final long delay;
    private long mouseOverStart;
    private final List<ToolTipLine> delegate = Lists.newArrayList();

    public ToolTip(long delay) {
        this.delay = delay;
    }

    public ToolTip() {
        this(0L);
    }

    @Override
    protected List<ToolTipLine> delegate() {
        return this.delegate;
    }

    public boolean add(String line) {
        return this.add(new ToolTipLine(line));
    }

    public void onTick(boolean mouseOver) {
        if(this.delay == 0L) {
            return;
        }
        if(mouseOver && this.mouseOverStart == 0) {
            this.mouseOverStart = System.currentTimeMillis();
        } else {
            this.mouseOverStart = 0;
        }
    }

    public boolean isReady() {
        if(this.delay == 0) {
            return true;
        }
        if(this.mouseOverStart == 0) {
            return false;
        }
        return System.currentTimeMillis() - this.mouseOverStart >= this.delay;
    }

    public List<String> toStringList() {
        List<String> ret = Lists.newArrayListWithCapacity(this.size());
        for(ToolTipLine line : this) {
            ret.add(line.getText());
        }
        return ret;
    }

    public void refresh() {}

    public static ToolTip buildToolTip(String tipTag, String... vars) {
        try {
            ToolTip toolTip = new ToolTip(750);
            String text = StatCollector.translateToLocalFormatted(tipTag, vars);
            for (String var : vars) {
                String[] pair = var.split("=");
                text = text.replace(pair[0], pair[1]);
            }
            String[] tips = text.split("\n");
            for (String tip : tips) {
                tip = tip.trim();
                toolTip.add(new ToolTipLine(tip));
            }
            return toolTip;
        } catch(RuntimeException ex) {
            AltEngLog.severe(ex, "Failed to parse tooltip %s", tipTag);
            throw ex;
        }
    }
}
