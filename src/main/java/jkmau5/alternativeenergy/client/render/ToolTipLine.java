package jkmau5.alternativeenergy.client.render;

import lombok.Getter;
import lombok.Setter;

/**
 * No description given
 *
 * @author jk-5
 */
public class ToolTipLine {

    @Getter @Setter private String text;
    @Getter @Setter private int color;
    @Getter @Setter private int spacing;

    public ToolTipLine(String text, int color) {
        this.text = text;
        this.color = color;
    }

    public ToolTipLine(String text) {
        this(text, -1);
    }

    public ToolTipLine() {
        this("", -1);
    }
}
