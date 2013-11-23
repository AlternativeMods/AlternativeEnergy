package jkmau5.alternativeenergy.gui.element;

import jkmau5.alternativeenergy.client.render.ToolTip;

/**
 * No description given
 *
 * @author jk-5
 */
public interface IIndicatorController {
    ToolTip getToolTip();
    int getScaledLevel(int scale);
}
