/*
 * Copyright (c) SpaceToad, 2011-2012
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.fluids;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 *
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class RestrictedTank extends Tank {

	private final Fluid[] acceptedFluids;

	public RestrictedTank(String name, int capacity, Fluid... acceptedFluids) {
		super(name, capacity);
		this.acceptedFluids = acceptedFluids;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (!acceptsFluid(resource.getFluid()))
			return 0;
		return super.fill(resource, doFill);
	}

	public boolean acceptsFluid(Fluid fluid) {
		for (Fluid accepted : acceptedFluids) {
			if (accepted.equals(fluid))
				return true;
		}
		return false;
	}
}
