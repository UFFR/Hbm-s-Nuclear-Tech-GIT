package com.hbm.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.Level;

import com.google.common.annotations.Beta;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.handler.FluidTypeHandler.FluidType;
import com.hbm.interfaces.Untested;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.machine.TileEntityFluidConverter;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
@Beta
@Untested
public class FluidConverterConfig
{
	private static File config;
	private static File template;
	public static final String fileName = "fluidCompat.json";
	private static final boolean[] compats = new boolean[FluidType.values().length];
	public static void preInit(File dir)
	{
		if (dir == null || !dir.isDirectory())
			return;
		
		template = dir;
		for (File f : dir.listFiles())
			if (f.getName().equals(fileName))
				config = f;
	}
	@SuppressWarnings("resource")
	public static void loadConfig()
	{
		try
		{
			JsonObject json = new Gson().fromJson(new FileReader(config), JsonObject.class);
			JsonArray fluids = json.getAsJsonArray();
			
			for (JsonElement fluid : fluids)
			{
				if (fluid.isJsonObject())
				{
					JsonObject fluidInfoArray = fluid.getAsJsonObject();
					
					if (fluidInfoArray.get("compat").getAsBoolean())
					{
						String fluidName = fluidInfoArray.getAsString();
						int density = fluidInfoArray.get("density").getAsInt();
						boolean isGas = fluidInfoArray.get("isGas").getAsBoolean();
						byte light = (byte) Math.max(0, fluidInfoArray.get("light").getAsByte());
						String name = fluidInfoArray.get("name").getAsString();
						int viscosity = Math.max(1, fluidInfoArray.get("viscosity").getAsInt());
						
						FluidType bobFluid = FluidType.valueOf(fluidName);
						Fluid forgeFluid = new Fluid(name).setDensity(density).setGaseous(isGas).setLuminosity(light).setViscosity(viscosity);
						TileEntityFluidConverter.bobToForge.put(bobFluid, forgeFluid);
						TileEntityFluidConverter.forgeToBob.put(forgeFluid, bobFluid);
						compats[bobFluid.ordinal()] = true;
						FluidRegistry.registerFluid(forgeFluid);
					}
				}
			}
		}
		catch (FileNotFoundException e)
		{
			MainRegistry.logger.catching(Level.WARN, e);
			generateConfig(template);
			loadConfig();
		}
		catch (EnumConstantNotPresentException e)
		{
			MainRegistry.logger.catching(Level.ERROR, e);
			MainRegistry.logger.error("Attempted to use invalid name for FluidType!");
		}
		catch (Exception e)
		{
			MainRegistry.logger.catching(Level.ERROR, e);
		}

	}
	
	/*
	 *{
	 *	"OIL": {
	 *		"compat": false,
	 *		"density": 800,
	 *		"isGas": false,
	 *		"light": 0,
	 *		"rarity": "COMMON",
	 *		"temperature": 300,
	 *		"name": "OIL",
	 *		"viscosity": 10
	 *		}
	 *}
	*/
	@SuppressWarnings("resource")
	public static void generateConfig(File dir)
	{
		template = new File(dir.getAbsolutePath() + File.separatorChar + fileName);
		
		try (JsonWriter writer = new JsonWriter(new FileWriter(template)))
		{
			writer.beginArray();
			writer.setIndent("	");
			
			for (FluidType bobFluid : FluidType.values())
			{
				writer.name(bobFluid.toString()).beginObject();
				writer.name("compat").value(false);
				Fluid forgeFluid = constructBasic(bobFluid);
				writer.name("density").value(forgeFluid.getDensity());
				writer.name("isGas").value(forgeFluid.isGaseous());
				writer.name("light").value(forgeFluid.getLuminosity());
				writer.name("rarity").value(forgeFluid.getRarity().toString());
				writer.name("temperature").value(forgeFluid.getTemperature());
				writer.name("name").value(bobFluid.toString());
				writer.name("viscosity").value(forgeFluid.getViscosity());
				writer.endObject();
			}
			writer.endArray();
			writer.close();
			
		}
		catch (IOException e)
		{
			MainRegistry.logger.catching(Level.WARN, e);
		}
	}
	
	public static boolean hasCompat(FluidType fluid)
	{
		return compats[fluid.ordinal()];
	}
	
	private static Fluid constructBasic(FluidType f)
	{
		Fluid newFluid = new Fluid(f.toString());
		switch (f)
		{
		case LAVA:
			return FluidRegistry.LAVA;
		case WATER:
			return FluidRegistry.WATER;
		case DIESEL:
			newFluid.setDensity(850);
			break;
		case OIL:
			newFluid.setDensity(800).setViscosity(10);
			break;
		case GASOLINE:
			newFluid.setDensity(800);
			break;
		default:
			break;
		}
		return newFluid;
	}
}
