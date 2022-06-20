package com.hbm.inventory.recipes;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbm.items.ModItems.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.hbm.config.GeneralConfig;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.*;
import com.hbm.inventory.fluid.*;
import com.hbm.items.ItemEnums.EnumCPU;
import com.hbm.items.ItemEnums.EnumRAM;
import com.hbm.items.ItemEnums.EnumWaferType;

import api.hbm.computer.Researchable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CircuitConstructorRecipes
{
	private static final ArrayList<CircuitRecipe> circuitRecipes = new ArrayList<>();
	private static final ArrayList<WaferRecipe> waferRecipes = new ArrayList<>();
	static
	{
		waferRecipes.add(new WaferRecipe(100, 150, new OreDictStack(NI.dust()), EnumWaferType.NICKEL));
		waferRecipes.add(new WaferRecipe(125, 200, new OreDictStack(LAPIS.dust()), EnumWaferType.LAPIS));
		waferRecipes.add(new WaferRecipe(125, 200, new OreDictStack(AU.dust()), EnumWaferType.GOLD));
		waferRecipes.add(new WaferRecipe(250, 300, new OreDictStack(DIAMOND.dust()), EnumWaferType.DIAMOND));
		waferRecipes.add(new WaferRecipe(500, 600, new OreDictStack(CS.dust()), EnumWaferType.CAESIUM));
		waferRecipes.add(new WaferRecipe(1000, 1200, new ComparableStack(powder_spark_mix), EnumWaferType.SPARK));
		
		circuitRecipes.add(circuit(circuit_copper, 100, EnumWaferType.SILICON, CU.plate(), wire_copper, circuit_aluminium));
		circuitRecipes.add(circuit(circuit_red_copper, 200, GeneralConfig.enable528 ? EnumWaferType.GOLD : EnumWaferType.NICKEL, CU.plate(), wire_red_copper, circuit_copper));
		circuitRecipes.add(circuit(circuit_gold, 800, EnumWaferType.LAPIS, POLYMER.ingot(), wire_gold, circuit_red_copper).setFluid(200, Fluids.PETROLEUM));
		circuitRecipes.add(circuit(circuit_schrabidium, 1600, GeneralConfig.enable528 ? EnumWaferType.CAESIUM : EnumWaferType.DIAMOND, DESH.ingot(), wire_schrabidium, circuit_gold).setFluid(200, Fluids.MERCURY));
		circuitRecipes.add(new CircuitRecipe(new ComparableStack(circuit_bismuth_raw), 800, new OreDictStack(REDSTONE.dust(), 4), new OreDictStack(POLYMER.ingot(), 2), new OreDictStack(ASBESTOS.ingot(), 2), new ComparableStack(nugget_bismuth)));
		circuitRecipes.add(new CircuitRecipe(new ComparableStack(circuit_tantalium_raw), 200, new OreDictStack(REDSTONE.dust(), 4), new OreDictStack(CU.plate(), 2), new ComparableStack(wire_gold, 2), new OreDictStack(TA.nugget())));
		
		circuitRecipes.add(cpu(EnumCPU.ALUMINIUM, 200, EnumWaferType.SILICON, new OreDictStack(CU.plate()), wire_aluminium, null));
		circuitRecipes.add(cpu(EnumCPU.COPPER, 300, EnumWaferType.NICKEL, new OreDictStack(ALLOY.plate()), wire_copper, EnumCPU.ALUMINIUM));
		circuitRecipes.add(cpu(EnumCPU.ADVANCED_ALLOY, 500, EnumWaferType.NICKEL, new ComparableStack(plate_paa), wire_gold, EnumCPU.ALUMINIUM));
		circuitRecipes.add(cpu(EnumCPU.PAA, 500, EnumWaferType.LAPIS, new ComparableStack(plate_paa), wire_magnetized_tungsten, EnumCPU.ALUMINIUM));
		circuitRecipes.add(cpu(EnumCPU.GOLD, 600, EnumWaferType.DIAMOND, new OreDictStack(AU.plate()), wire_schrabidium, EnumCPU.ADVANCED_ALLOY));
		circuitRecipes.add(cpu(EnumCPU.SCHRABIDIUM, 700, EnumWaferType.CAESIUM, new OreDictStack(SA326.plate()), wire_schrabidium, EnumCPU.ADVANCED_ALLOY));
		circuitRecipes.add(cpu(EnumCPU.BISMUTH, 800, EnumWaferType.CAESIUM, new ComparableStack(circuit_bismuth), wire_schrabidium, EnumCPU.SCHRABIDIUM));
		
		circuitRecipes.add(new CircuitRecipe(new ComparableStack(ram, 2, 0), 100, new OreDictStack(REDSTONE.dust()), new ComparableStack(plate_polymer), new ComparableStack(wire_copper), new ComparableStack(circuit_aluminium)));
		circuitRecipes.add(ram(EnumRAM.SILICON, 100, EnumWaferType.SILICON, wire_red_copper, circuit_copper));
		circuitRecipes.add(ram(EnumRAM.NICKEL, 200, EnumWaferType.NICKEL, wire_red_copper, circuit_red_copper));
		circuitRecipes.add(ram(EnumRAM.LAPIS, 400, EnumWaferType.LAPIS, wire_advanced_alloy, circuit_red_copper));
		circuitRecipes.add(ram(EnumRAM.GOLD, 800, EnumWaferType.GOLD, wire_advanced_alloy, circuit_gold));
		circuitRecipes.add(ram(EnumRAM.DIAMOND, 1600, EnumWaferType.DIAMOND, wire_gold, circuit_gold));
		circuitRecipes.add(ram(EnumRAM.CAESIUM, 3200, EnumWaferType.CAESIUM, wire_gold, circuit_schrabidium));
		circuitRecipes.add(ram(EnumRAM.SPARK, 6400, EnumWaferType.SPARK, wire_magnetized_tungsten, circuit_targeting_tier6));
	}
	private static CircuitRecipe circuit(Item out, int time, EnumWaferType waferType, String plate, Item wire, Item prev)
	{
		return new CircuitRecipe(new ComparableStack(out), time, new ComparableStack(wafer, 1, waferType.ordinal()), new OreDictStack(plate), new ComparableStack(wire, 4), new ComparableStack(prev));
	}
	private static CircuitRecipe cpu(EnumCPU out, int time, EnumWaferType waferType, AStack plate, Item wire, @Nullable EnumCPU prev)
	{
		return new CircuitRecipe(new ComparableStack(cpu, 1, out.ordinal()), time, new ComparableStack(wafer, 2, waferType.ordinal()), plate, new ComparableStack(wire, 4), prev == null ? new OreDictStack(CU.plate()) : new ComparableStack(cpu, 1, prev.ordinal()));
	}
	private static CircuitRecipe ram(EnumRAM out, int time, EnumWaferType waferType, Item wire, Item circuitBase)
	{
		return new CircuitRecipe(new ComparableStack(ram, 2, out.ordinal()), time, new ComparableStack(wafer, 1, waferType.ordinal()), new ComparableStack(plate_polymer), new ComparableStack(wire), new ComparableStack(circuitBase));
	}
	
	public static class CircuitRecipe extends Researchable
	{
		private final ComparableStack out;
		private final int time;
		private final AStack[] input;
		private FluidStack fluid = new FluidStack(0, Fluids.NONE);
		public CircuitRecipe(ComparableStack out, int time, AStack...stacks)
		{
			super(out.toStack().getUnlocalizedName());
			this.out = out;
			this.time = time;
			this.input = stacks;
		}
		public CircuitRecipe setFluid(int fill, FluidType type)
		{
			fluid = new FluidStack(type, fill);
			return this;
		}
		public ComparableStack getOutput()
		{
			return out.copy();
		}
		public List<AStack> getInputs()
		{
			return ImmutableList.copyOf(input);
		}
		public int getTime()
		{
			return time;
		}
		public FluidStack getFluid()
		{
			return fluid;
		}
		public boolean isOutputStackApplicable(ItemStack stack)
		{
			return out.matchesRecipe(stack, true) && stack.stackSize + out.stacksize <= stack.getMaxStackSize();
		}
	}
	
	public static class WaferRecipe
	{
		public final int fluidConsumed;
		public final int time;
		public final AStack input;
		public final EnumWaferType waferType;
		public WaferRecipe(int fluid, int dura, AStack item, EnumWaferType type)
		{
			fluidConsumed = fluid;
			time = dura;
			input = item;
			waferType = type;
		}
	}

}
