package jkmau5.alternativeenergy.gui.button;


import jkmau5.alternativeenergy.client.render.ToolTip;

public interface IMultiButtonState {

    String getLabel();
    String name();
    IButtonTextureSet getTextureSet();
    ToolTip getToolTip();
}