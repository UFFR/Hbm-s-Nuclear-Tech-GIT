package com.hbm.blocks;

import net.minecraft.block.Block;

public class ModSoundType extends Block.SoundType {

	public ModSoundType(String name, float volume, float pitch) {
		super(name, volume, pitch);
	}

	@Override
	public String getBreakSound() {
		return "hbm:" + super.getBreakSound();
	}

	@Override
	public String getStepResourcePath() {
		return "hbm:" + super.getStepResourcePath();
	}

}
