package api.hbm.faction;

import com.hbm.entity.mob.ai.squad.SquadBase;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumDifficulty;

public abstract class SquadBuilder
{
	public abstract String getName();
	public abstract SquadBase build();
	
	public static EnumDifficulty getDifficulty()
	{
		return MinecraftServer.getServer().getEntityWorld().difficultySetting;
	}
}
