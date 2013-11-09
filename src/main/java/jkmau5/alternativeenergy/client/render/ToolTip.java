package jkmau5.alternativeenergy.client.render;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class ToolTip extends ForwardingList<ToolTipLine> {

    private final long delay;
    private long mouseOverStart;

    public ToolTip(long delay){
        this.delay = delay;
    }

    public ToolTip(){
        this(0L);
    }

    @Override
    protected List<ToolTipLine> delegate() {
        return Lists.newArrayList();
    }

    public boolean add(String line){
        return this.add(new ToolTipLine(line));
    }

    public void onTick(boolean mouseOver){
        if(this.delay == 0L) return;
        if(mouseOver && this.mouseOverStart == 0){
            this.mouseOverStart = System.currentTimeMillis();
        }else{
            this.mouseOverStart = 0;
        }
    }

    public boolean isReady(){
        if(this.delay == 0) return true;
        if(this.mouseOverStart == 0) return false;
        return System.currentTimeMillis() - this.mouseOverStart >= this.delay;
    }

    public List<String> toStringList(){
        List<String> ret = Lists.newArrayListWithCapacity(this.size());
        for(ToolTipLine line : this) ret.add(line.getText());
        return ret;
    }
}
