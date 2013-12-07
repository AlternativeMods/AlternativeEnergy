package jkmau5.alternativeenergy.gui.element;

import jkmau5.alternativeenergy.client.render.ToolTip;
import jkmau5.alternativeenergy.client.render.ToolTipLine;

/**
 * No description given
 *
 * @author jk-5
 */
public abstract class AbstractIndicatorController implements IIndicatorController {

    private final ToolTip defaultToolTip = new ToolTip() {
        @Override
        public void refresh() {
            AbstractIndicatorController.this.refreshToolTip();
        }
    };

    protected ToolTipLine tipLine = new ToolTipLine();

    protected AbstractIndicatorController() {
        this.defaultToolTip.add(this.tipLine);
    }

    @Override
    public ToolTip getToolTip() {
        return this.defaultToolTip;
    }

    protected void refreshToolTip() {

    }
}
