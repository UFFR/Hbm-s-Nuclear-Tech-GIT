package com.hbm.extprop;

import java.util.Random;

import com.hbm.entity.mob.faction.IFactionDataProvider;
import com.hbm.entity.mob.faction.NameUtil.EnumRank;
import com.hbm.handler.HbmKeybinds.EnumKeybind;
import com.hbm.main.MainRegistry;
import com.hbm.main.ServerProxy;
import com.hbm.saveddata.TimeSavedData;
import com.hbm.util.FactionUtil.Faction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class HbmPlayerProps implements IExtendedEntityProperties, IFactionDataProvider {
	
	public static final String key = "NTM_EXT_PLAYER";
	public EntityPlayer player;
	
	// Data
	private long age = 20;
	private byte birthday = 1;
	private long birthyear = 2280;
	private boolean hasAscended = false;
	public boolean enableHUD = true;
	public boolean enableBackpack = true;
	
	private boolean[] keysPressed = new boolean[EnumKeybind.values().length];
	
	public boolean dashActivated = true;
	
	public static final byte dashCooldownLength = 5;
	public int dashCooldown = 0;
	
	public int totalDashCount = 0;
	public int stamina = 0;
	
	public static final int plinkCooldownLength = 10;
	public int plinkCooldown = 0;
	
	// Faction data
	private Faction faction = null;
	private boolean inSquad = false;
	private String squad = "";
	private String squadName = "";
	
	
	public HbmPlayerProps(EntityPlayer player) {
		this.player = player;
	}
	
	public static HbmPlayerProps registerData(EntityPlayer player) {
		
		player.registerExtendedProperties(key, new HbmPlayerProps(player));
		HbmPlayerProps props = (HbmPlayerProps) player.getExtendedProperties(key);
		props.age = getNewAge();
		props.birthyear = TimeSavedData.getData(player.worldObj).dateData.getYear() - props.age;
		return props;
	}
	
	public static HbmPlayerProps getData(EntityPlayer player) {
		
		HbmPlayerProps props = (HbmPlayerProps) player.getExtendedProperties(key);
		return props != null ? props : registerData(player);
	}
	
	public static long getAge(EntityPlayer player)
	{
		return getData(player).age;
	}
	
	public static byte getBirthday(EntityPlayer player)
	{
		return getData(player).birthday;
	}
	
	public static long getBirthyear(EntityPlayer player)
	{
		return getData(player).birthyear;
	}
	
	public static boolean hasAscended(EntityPlayer player)
	{
		return getData(player).hasAscended;
	}
	
	public static void setAge(EntityPlayer player, long newAge, boolean increment)
	{
		if (increment)
			getData(player).age += newAge;
		else
			getData(player).age = newAge;
	}
	
	public boolean getKeyPressed(EnumKeybind key) {
		return keysPressed[key.ordinal()];
	}
	
	public static long getNewAge()
	{
		return 20 + new Random().nextInt(5);
	}
	
	public boolean isJetpackActive() {
		return this.enableBackpack && getKeyPressed(EnumKeybind.JETPACK);
	}
	
	public void setKeyPressed(EnumKeybind key, boolean pressed) {
		
		if(!getKeyPressed(key) && pressed) {
			
			if(key == EnumKeybind.TOGGLE_JETPACK) {
				this.enableBackpack = !this.enableBackpack;
				
				if(this.enableBackpack)
					MainRegistry.proxy.displayTooltip(EnumChatFormatting.GREEN + "Jetpack ON", ServerProxy.ID_JETPACK);
				else
					MainRegistry.proxy.displayTooltip(EnumChatFormatting.RED + "Jetpack OFF", ServerProxy.ID_JETPACK);
			}
			if(key == EnumKeybind.TOGGLE_HEAD) {
				this.enableHUD = !this.enableHUD;
				
				if(this.enableHUD)
					MainRegistry.proxy.displayTooltip(EnumChatFormatting.GREEN + "HUD ON", ServerProxy.ID_HUD);
				else
					MainRegistry.proxy.displayTooltip(EnumChatFormatting.RED + "HUD OFF", ServerProxy.ID_HUD);
			}
		}
		
		keysPressed[key.ordinal()] = pressed;
	}
	
	public void setDashCooldown(int cooldown) {
		this.dashCooldown = cooldown;
		return;
	}
	
	public int getDashCooldown() {
		return this.dashCooldown;
	}
	
	public void setStamina(int stamina) {
		this.stamina = stamina;
		return;
	}
	
	public int getStamina() {
		return this.stamina;
	}
	
	public void setDashCount(int count) {
		this.totalDashCount = count;
		return;
	}
	
	public int getDashCount() {
		return this.totalDashCount;
	}
	
	public static void plink(EntityPlayer player, String sound, float volume, float pitch) {
		HbmPlayerProps props = HbmPlayerProps.getData(player);
		
		if(props.plinkCooldown <= 0) {
			player.worldObj.playSoundAtEntity(player, sound, volume, pitch);
			props.plinkCooldown = props.plinkCooldownLength;
		}
	}

	@Override
	public void init(Entity entity, World world) {/*h*/ }

	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound props = new NBTTagCompound();
		props.setDouble("age", age);
		props.setByte("bithday", birthday);
		props.setLong("birthyear", birthyear);
		props.setBoolean("ascended", hasAscended);
		compound.setTag("HbmPlayerProps", props);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound props = (NBTTagCompound) compound.getTag("HbmPlayerProps");
		
		if (props != null)
		{
			age = props.hasKey("age") ? props.getLong("age") : getNewAge();
			
			birthyear = props.hasKey("birthyear") ? props.getLong("birthyear") : player == null ? 2300 - age : TimeSavedData.getData(player.worldObj).dateData.getYear() - age;
			
			birthday = props.hasKey("birthday") ? props.getByte("birthday") : 1;
			
			hasAscended = props.hasKey("ascended") ? props.getBoolean("ascended") : false;
		}
	}

	@Override
	public String getName()
	{
		return player.getCommandSenderName();
	}

	@Override
	public Faction getFaction()
	{
		return faction;
	}

	@Override
	public boolean hasSquad()
	{
		return inSquad;
	}

	@Override
	public String getSquad()
	{
		return squad;
	}

	@Override
	public String getSquadName()
	{
		return squadName;
	}

	@Override
	public boolean rankApplicable()
	{
		return false;
	}

	@Override
	public EnumRank getRank()
	{
		return null;
	}
}
