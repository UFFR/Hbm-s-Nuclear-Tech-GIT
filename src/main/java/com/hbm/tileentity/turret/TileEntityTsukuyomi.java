package com.hbm.tileentity.turret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.hbm.calc.EasyLocation;
import com.hbm.config.GeneralConfig;
import com.hbm.config.MachineConfig;
import com.hbm.handler.BulletConfigSyncingUtil;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.interfaces.Spaghetti;
import com.hbm.items.ModItems;
import com.hbm.lib.Library;
import com.hbm.lib.Library.EnumHashAlgorithm;
import com.hbm.main.MainRegistry;
import com.hbm.saveddata.AuxSavedData;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.I18nUtil;
import com.hbm.util.Tuple.Pair;

import api.hbm.energy.IEnergyUser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
@Spaghetti("aaaaaa")
public class TileEntityTsukuyomi extends TileEntityMachineBase implements IEnergyUser, IControlReceiver
{
	private static int cooldown = 0;
	private int ammoCount = 0, config = 0;
	public boolean isOn, isReady, gotTargets, hasTachyon;
	public byte tachyonCount = 0, index = 0;
	private long power = 0;
	public static final long maxPower = 1000000000000L;
	public Optional<EntityPlayer> currPlayer = Optional.empty();
	private final ArrayList<String> targets = new ArrayList<String>();
	private final ArrayList<EntityPlayer> readyTargets = new ArrayList<EntityPlayer>();
	private static final HashMap<Item, Pair<Integer, Consumer<EntityPlayer>>> configMap = new HashMap<>();
	private static final Consumer<EntityPlayer> basic = p -> p.setHealth(0);
	static
	{
		configMap.put(ModItems.singularity_micro, new Pair<Integer, Consumer<EntityPlayer>>(1, basic));
		configMap.put(ModItems.singularity, new Pair<Integer, Consumer<EntityPlayer>>(100, basic));
		configMap.put(ModItems.singularity_super_heated, new Pair<Integer, Consumer<EntityPlayer>>(250, basic));
		configMap.put(ModItems.singularity_counter_resonant, new Pair<Integer, Consumer<EntityPlayer>>(500, basic));
	}
	public TileEntityTsukuyomi()
	{
		super(3);
		isOn = false;
		isReady = false;
		gotTargets = false;
		hasTachyon = false;
	}

	public void updateConfig()
	{
		if (slots[1] != null && ammoCount == 0)
		{
			Item ammo = slots[1].getItem();
			config = configMap.containsKey(ammo) ? Item.getIdFromItem(ammo) : -1;
		}
	}
	
	public String getAttackResult()
	{
		if (currPlayer.isPresent() && isReady)
		{
//			if (currPlayer.get().capabilities.isCreativeMode)
//				return I18nUtil.resolveKey("twr.result.fail");
//			final float pHealth = currPlayer.get().getHealth();
//			return config.dmgMin > pHealth ? I18nUtil.resolveKey("twr.result.success") :
//				config.dmgMax > pHealth ? I18nUtil.resolveKey("twr.result.partial") :
//					I18nUtil.resolveKey("twr.result.fail");
			return I18nUtil.resolveKey("twr.result." + (currPlayer.get().capabilities.isCreativeMode ? "fail" : "success"));
		}
		else
			return "N/A";
	}
	
	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			cooldown = AuxSavedData.getDataPair(getWorldObj(), "tsukuyomiCooldown");
			power = Library.chargeTEFromItems(slots, 0, power, maxPower);
			updateConfig();
			if (index > targets.size())
				index = 0;
			currPlayer = Optional.ofNullable(gotTargets ? readyTargets.get(index) : null);
			
			hasTachyon = tachyonCount > 0;
			
			if (ammoCount > 0 && config == -1)
				ammoCount = 0;
			
			if (cooldown > 0)
			{
				gotTargets = false;
				isReady = false;
				isOn = false;
			}
			
			if (worldObj.rand.nextInt(MachineConfig.twrTurretChance) == 0 && cooldown > 0)
				cooldown--;
			if (cooldown < 0)
				cooldown = 0;
			
			AuxSavedData.setDataPair(getWorldObj(), "tsukuyomiCooldown", cooldown);
//			System.out.println(gotTargets);
		}
		
		NBTTagCompound data = new NBTTagCompound();
		data.setLong("power", power);
		data.setInteger("ammoCount", ammoCount);
		data.setBoolean("gotTargets", gotTargets);
		data.setBoolean("isReady", isReady);
		data.setBoolean("hasTachyon", hasTachyon);
		data.setByte("tachyonCount", tachyonCount);
		data.setBoolean("isOn", isOn);
		data.setByte("index", index);
		data.setInteger("cooldown", cooldown);
		if (currPlayer.isPresent())
			data.setString("currPlayer", currPlayer.get().getDisplayName());
		if (config != -1)
			data.setInteger("bullConfig", config);
		if (!targets.isEmpty())
		{
			ByteBuf buf = Unpooled.buffer();
			buf.writeByte(targets.size());
			for (String targ : targets)
			{
				buf.writeByte(targ.length());
				buf.writeBytes(targ.getBytes());
			}
			data.setByteArray("targets", buf.array());
		}
		networkPack(data, 50);
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		power = nbt.getLong("power");
		ammoCount = nbt.getInteger("ammoCount");
		gotTargets = nbt.getBoolean("gotTargets");
		isReady = nbt.getBoolean("isReady");
		hasTachyon = nbt.getBoolean("hasTachyon");
		tachyonCount = nbt.getByte("tachyonCount");
		isOn = nbt.getBoolean("isOn");
		index = nbt.getByte("index");
		cooldown = nbt.getInteger("cooldown");
		if (nbt.hasKey("currPlayer"))
			currPlayer = Optional.ofNullable(worldObj.getPlayerEntityByName(nbt.getString("currPlayer")));
		if (nbt.hasKey("config"))
			config = nbt.getInteger("config");
		targets.clear();
		if (nbt.hasKey("targets"))
		{
			ByteBuf buf = Unpooled.copiedBuffer(nbt.getByteArray("targets"));
			byte listSize = buf.readByte();
			for (byte i = 0; i < listSize; i++)
			{
				byte[] stringBytes = new byte[buf.readByte()];
				buf.readBytes(stringBytes);
				targets.add(new String(stringBytes));
			}
		}
	}
	
	@Override
	public void handleButtonPacket(int value, int meta)
	{
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "gui.button.press", 1.0F, 1.0F);
		if (cooldown > 0)
		{
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:gui.buttonNo", 1.0F, 1.0F);
			return;
		}
		switch (value)
		{
		case 0:
			if (constructTargetList() > 0 && !gotTargets)
			{
				gotTargets = true;
				worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:alarm.defconstage", 1.0F, 1.0F);
				if (GeneralConfig.enableExtendedLogging)
					MainRegistry.logger.info("A TWR (Tsukuyomi) platform has entered the first stage");
			}
			else
			{
				gotTargets = false;
				isReady = false;
				if (readyTargets.isEmpty())
					worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:gui.buttonNo", 1.0F, 1.0F);
				readyTargets.clear();
				currPlayer = null;
				if (GeneralConfig.enableExtendedLogging)
					MainRegistry.logger.info("A TWR (Tsukuyomi) platform has halted the first stage");
			}
			break;
		case 1:
			if (gotTargets && !isReady && config != -1)
			{
				isReady = true;
				worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:gui.buttonYes", 1.0F, 1.0F);
				worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:alarm.defconstage", 1.0F, 1.0F);
				if (ammoCount == 0)
				{
					ammoCount = BulletConfigSyncingUtil.pullConfig(config).ammoCount;
					decrStackSize(1, 1);
				}
				if (GeneralConfig.enableExtendedLogging)
					MainRegistry.logger.info("A TWR (Tsukuyomi) platform has entered the second stage using the configuration: [" + I18nUtil.resolveKey(BulletConfigSyncingUtil.pullConfig(config).ammo.getFriendlyName()) + "] and is ready to purify");
			}
			else
			{
				if (!gotTargets || config == -1)
					worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:gui.buttonNo", 1.0F, 1.0F);
				isReady = false;
			}
			break;
		case 2:
			if (!readyTargets.isEmpty() && isReady && gotTargets && config != -1 && ammoCount > 0 && power == maxPower)
			{
				isOn = true;
				worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:alarm.defconstage", 1.0F, 1.0F);
				currPlayer = null;
				if (GeneralConfig.enableExtendedLogging)
					MainRegistry.logger.info("A TWR (Tsukuyomi) platform has commenced purification");
				purify();
			}
			else
				worldObj.playSoundEffect(xCoord, yCoord, zCoord, "hbm:gui.buttonNo", 1.0F, 1.0F);
			break;
		case 3:
			if (meta == 0)
			{
				if (!targets.isEmpty() && index > 0)
					index--;
			}
			else
			{
				if (!targets.isEmpty() && !(index == targets.size() - 1))
					index++;
			}
			if (index > 14)
				index = 14;
			if (index < 0)
				index = 0;
			break;
		case 4:
			boolean flag = false;
			if (targets.isEmpty())
				break;
			if (index == targets.size() - 1 && index != 0)
				flag = true;
			targets.remove(index);
			if (flag)
				index--;
			break;
		default:
			break;
		}
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		ammoCount = nbt.getInteger("ammoCount");
		power = nbt.getLong("power");
		tachyonCount = nbt.getByte("tachyonCount");
		config = nbt.getInteger("config");
		cooldown = AuxSavedData.getDataPair(getWorldObj(), "tsukuyomiCooldown");
		if (nbt.hasKey("targets"))
		{
			ByteBuf buf = Unpooled.copiedBuffer(nbt.getByteArray("targets"));
			byte listSize = buf.readByte();
			for (byte i = 0; i < listSize; i++)
			{
				byte[] stringBytes = new byte[buf.readByte()];
				buf.readBytes(stringBytes);
				targets.add(new String(stringBytes));
			}
		}
	}
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("ammoCount", ammoCount);
		nbt.setLong("power", power);
		nbt.setByte("tachyonCount", tachyonCount);
		nbt.setInteger("config", config);
		AuxSavedData.setDataPair(getWorldObj(), "tsukuyomiCooldown", cooldown);
		if (!targets.isEmpty())
		{
			ByteBuf buf = Unpooled.buffer();
			buf.writeByte(targets.size());
			for (String targ : targets)
			{
				buf.writeByte(targ.length());
				buf.writeBytes(targ.getBytes());
			}
			nbt.setByteArray("targets", buf.array());
		}
	}
	
	private void purify()
	{
		// TODO Finish
//		BulletConfiguration config = BulletConfigSyncingUtil.pullConfig(config);
		Consumer<EntityPlayer> logic = configMap.get(slots[1].getItem()).getValue();
		for (int i = 0; i < readyTargets.size(); i++)
		{
			logic.accept(readyTargets.get(i));
			readyTargets.get(i).onDeath(new EntityDamageSource("twr" + rand.nextInt(2) + 2, readyTargets.get(i)).setDamageIsAbsolute().setDamageBypassesArmor());
		}
		gotTargets = false;
		isOn = false;
		isReady = false;
		power = 0;
		cooldown = MachineConfig.twrTurretCooldown;
		ammoCount -= readyTargets.size();
		if (ammoCount == 0)
			config = -1;
		if (GeneralConfig.enableExtendedLogging)
			MainRegistry.logger.info("A TWR (Tsukuyomi) platform has completed purification, global cooldown is now: " + cooldown);
	}

	private boolean isPlayerNameValid(String name)
	{
		return worldObj.getPlayerEntityByName(name) != null ? true : false;
	}
	/**
	 * Check if a player is valid
	 * @param playerIn - The player object in question
	 * @return -1 if null; 0 if valid; 1 if outside current dimension
	 */
	private static byte isPlayerValid(EntityPlayer playerIn)
	{
		return (byte) (playerIn == null ? -1 : (playerIn.dimension == 0 ? 0 : 1));
	}
	/**
	 * Add a new player to the targeting list
	 * @param name - The player's name
	 * @return If the action was successful
	 */
	private boolean addPlayer(String name)
	{
		if (targets.size() == 15 || cooldown > 0)
			return false;
		else
			return targets.add(name);
	}
	
	private int constructTargetList()
	{
		if (!targets.isEmpty())
		{
			readyTargets.clear();
			index = 0;
			for (String t : new ArrayList<>(targets))
			{
				if (isPlayerNameValid(t) && isPlayerValid(worldObj.getPlayerEntityByName(t)) == worldObj.provider.dimensionId)
					readyTargets.add(worldObj.getPlayerEntityByName(t));
				else
					targets.remove(t);
			}
		}
		return readyTargets.size();
	}
	
	@Override
	public long getPower()
	{
		return power;
	}
	
	@Override
	public String getName()
	{
		return "container.turretTWR";
	}

	@Override
	public void setPower(long i)
	{
		power = i;
	}

	@Override
	public long getMaxPower()
	{
		return maxPower;
	}
	
	public int getAmmoCount()
	{
		return ammoCount;
	}

	public String getPlayerHash()
	{
		if (currPlayer.isPresent())
			return Library.toHexString(Library.getHash(Integer.toHexString((int) (7 * new EasyLocation(currPlayer.get()).hashCode() * currPlayer.get().getHealth() * currPlayer.get().dimension)).getBytes(), EnumHashAlgorithm.MD5));
		else
			return "N/A";
	}
	
	public List<String> getTargets()
	{
		return targets;
	}
	
	public static int getCooldown()
	{
		return cooldown;
	}
	
	@Override
	public boolean hasPermission(EntityPlayer player)
	{
		return isUseableByPlayer(player);
	}

	@Override
	public void receiveControl(NBTTagCompound data)
	{
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, buttonPress, 1.0F, 1.0F);
//		System.out.println("Got commands");
		if (data.hasKey("name") && targets.size() <= 15 && !targets.contains(data.getString("name")) && !gotTargets)
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, addPlayer(data.getString("name")) ? buttonYes : buttonNo, 1.0F, 1.0F);
	}
}
