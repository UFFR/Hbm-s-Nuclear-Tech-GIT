package com.hbm.faction;

import java.util.List;

import com.hbm.interfaces.ILocationProvider;

import api.hbm.faction.Faction;
import api.hbm.faction.SquadBuilder;
import net.minecraft.world.World;

public class Factions
{
	public static Faction COMBINE;
	
	public static void initialize(World world)
	{
		COMBINE = new Faction("cmb", world)
		{
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String toString()
			{
				return "The Combine";
			}
			
			@Override
			public int hashCode()
			{
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public List<SquadBuilder> getSquadBuilders()
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean equals(Object obj)
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void constructAndDeploySquad(ILocationProvider loc)
			{
				// TODO Auto-generated method stub
				
			}
		};
	}
}
