package jkmau5.alternativeenergy.gui.button;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * No description given
 *
 * @author jk-5
 */
@RequiredArgsConstructor
public class DefaultButtonTextureSet implements IButtonTextureSet {
    @Getter private final int x;
    @Getter private final int y;
    @Getter private final int height;
    @Getter private final int width;
}
