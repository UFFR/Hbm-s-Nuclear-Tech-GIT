package com.hbm.entity.mob.smart.combine;

import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.entity.mob.smart.NameUtil;
import com.hbm.faction.FactionCombine;
import com.hbm.handler.GunConfiguration;
import com.hbm.handler.guncfg.GunOSIPRFactory;
import com.hbm.util.I18nUtil;

import api.hbm.faction.Faction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class EntityCombineBase extends EntitySmartNPCBase
{
	protected static final GunConfiguration OSIPR_CONFIG = GunOSIPRFactory.getOSIPRConfig(), OSIPR_ALT_CONFIG = GunOSIPRFactory.getAltConfig();
	
	public EntityCombineBase(World world, SquadBase squad, String name, String squadName)
	{
		super(world, squad, name, squadName);
		this.name = name;
		getFaction().getRegisteredNames().add(name);
	}
	
	public EntityCombineBase(World world, SquadBase squad)
	{
		this(world, squad,
				NameUtil.generateNameFromDefined(NameUtil::generateCombineName, FactionCombine.getCombine().getRegisteredNames(), Short.MAX_VALUE),
				squad == null ? NameUtil.generateCombineSquadName() : NameUtil.generateNameFromDefined(NameUtil::generateCombineSquadName, squad.getRegisteredNames(), NameUtil.MAX_COMBINE_SQUAD_NAMES));
	}
	
	@Override
	public float getWidth()
	{
		return 0.6f;
	}
	
	@Override
	public float getHeight()
	{
		return 1.8f;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getSquadName()
	{
		return squadName;
	}

	@Override
	public Faction getFaction()
	{
		return FactionCombine.getCombine();
	}
	
	@Override
	public boolean hasFaction()
	{
		return true;
	}

	@Override
	public void onKilledPlayer(EntityPlayer player)
	{
		worldChat.accept(chatSupplier.apply(I18nUtil.resolveKey("death.attack.cmb" + rand.nextInt(6), player.getCommandSenderName(), getSquadName())));
	}
	
	@Override
	public int getTalkInterval()
	{
		return 600;
	}
	
	@Override
	public String toDisplayShort()
	{
		return new StringBuilder("Unit #").append(name).append("\nSquad Name: ").append(squadName).toString();
	}
}
