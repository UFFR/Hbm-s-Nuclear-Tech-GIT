package com.hbm.faction;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.hbm.entity.mob.ai.squad.CombineCommandoSquad;
import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.entity.mob.smart.NameUtil;
import com.hbm.entity.mob.smart.combine.EntityCombineBase;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.main.DeserializationException;

import api.hbm.faction.Faction;
import api.hbm.faction.SquadBuilder;
import bytes.ByteSequence;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public final class FactionCombine extends Faction
{
	private static final Map<Class<? extends SquadBase>, Function<Collection<String>, String>> NAME_GENERATORS = new HashMap<Class<? extends SquadBase>, Function<Collection<String>,String>>();
	private static final Map<String, Function<World, SquadBase>> SQUAD_CLASSES = new HashMap<String, Function<World,SquadBase>>();
	
	private static Faction combine;
	
	static
	{
		SQUAD_CLASSES.put(CombineCommandoSquad.class.getCanonicalName(), CombineCommandoSquad::new);
		
		NAME_GENERATORS.put(CombineCommandoSquad.class, NameUtil::generateCombineCommandoSquadName);
	}
	
	public static Faction getCombine()
	{
		return combine == null ? combine = new FactionCombine() : combine;
	}

	private FactionCombine()
	{
		super("cmb");
	}

	@Override
	public void constructAndDeploySquad(ILocationProvider loc)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<SquadBuilder> getSquadBuilders()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValidTarget(EntityLivingBase entity)
	{
		if (entity == null)
			return false;
		if (entity instanceof EntityPlayer && !((EntityPlayer) entity).capabilities.isCreativeMode)
			return true;
		if (entity.isEntityUndead() || entity instanceof IMob && !(entity instanceof EntityCombineBase))
			return true;
		
		return false;
	}

	@Override
	public boolean isBadTarget(EntityLivingBase entity)
	{
		return !isValidTarget(entity) || entity.getClass().getCanonicalName().contains("passive");
	}

	@Override
	public String defaultSquadName()
	{
		return "Stabilization Team";
	}

	@Override
	public void regenerateSquads(ByteSequence data, List<String> classes)
	{
		for (String clazz : classes)
		{
			if (SQUAD_CLASSES.containsKey(clazz))
			{
				try
				{
					final SquadBase squad = SQUAD_CLASSES.get(clazz).apply(world);
					squad.readFromBytes(data);
					activeSquads.add(squad);
				} catch (DeserializationException e)
				{
					LOGGER.warn("Caught exception while deserializing squad, must abort squad deserialization.");
					LOGGER.catching(e);
					break;
				}
			} else
			{
				LOGGER.warn("Class [" + clazz + "] not recognized, must abort squad deserialization.");
				break;
			}
		}
	}
	
	@Override
	public String generateSquadName(Class<? extends SquadBase> clazz, Set<String> alreadyRegisteredNames)
	{
		return NAME_GENERATORS.containsKey(clazz) ? NAME_GENERATORS.get(clazz).apply(alreadyRegisteredNames) : defaultSquadName();
	}
	

	@Override
	public String toString()
	{
		return "The Combine";
	}

}
