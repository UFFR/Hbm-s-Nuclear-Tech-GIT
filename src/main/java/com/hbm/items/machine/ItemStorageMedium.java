package com.hbm.items.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableSet;
import com.hbm.items.ItemEnumMultiSimple;
import com.hbm.items.ItemStorageEnums.EnumStorageMagneticNew;
import com.hbm.items.ItemStorageEnums.EnumStorageMagneticOld;
import com.hbm.items.ItemStorageEnums.EnumStorageOptical;
import com.hbm.lib.HbmCollection;
import com.hbm.lib.Library;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;

import api.hbm.computer.File;
import api.hbm.internet.IDataStorageItem;
import api.hbm.internet.IStorageItemEnum;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
@Beta
public class ItemStorageMedium extends ItemEnumMultiSimple implements IDataStorageItem
{
	private Optional<Item> dropItem = Optional.empty();
	private static final HashMap<Class<? extends Enum>, EnumStorageItemType> classMap = new HashMap<>();
	static
	{
		classMap.put(EnumStorageMagneticOld.class, EnumStorageItemType.MAGNETIC_BASIC);
		classMap.put(EnumStorageMagneticNew.class, EnumStorageItemType.MAGNETIC_ENHANCED);
		classMap.put(EnumStorageOptical.class, EnumStorageItemType.OPTICAL);
	}
	public ItemStorageMedium(Class<? extends Enum<? extends IStorageItemEnum>> clazz)
	{
		super(clazz, true, true);
	}
	
	public static enum EnumStorageItemType
	{
		MAGNETIC_BASIC,// FDDs & tape drives, tells it its very weak to magnets
		MAGNETIC_ENHANCED,// HDDs, somewhat weak to magnets
		OPTICAL,// CDs, DVDs, & BDs, immune to magnets
		FLASH,// SSDs & USBs, immune to magnets
		MAGNETIC_ADVANCED,// Super high capacity tape drives, very weak to magnets
		CRYSTALLINE,// Quite stable, immune to almost everything, overall very good albeit expensive, similar to optical
		SINGULARITY;// Endgame(?), immune to radiation
	}
	public static enum EnumStorageItemTraits
	{
		//DIRTY("dirty"),// Prone to errors, makes more of a hassle to use, but easy to fix
		DAMAGED("damaged"),// Physically damaged, prone to further corruption, fixable?
		CORRUPTED_LIGHT("corruptedLight"),// Data is somewhat corrupted, can be fixed relatively easily
		CORRUPTED_HEAVY("corruptedHeavy"),// Data is very corrupted, requires more effort
		CORRUPTED_ABSOLUTE("corruptedAbsolute");// Corruption is irreversible, just re-format it
		public String key;
		private EnumStorageItemTraits(String string)
		{
			key = "trait." + string;
		}
	}
	public static enum ProsConsList
	{
		LIFESPAN_GOOD("pro.lifespan1"),
		LIFESPAN_DECENT("pro.lifespan2"),
		CHEAP_VERY("pro.cheap_very"),
		CAPACITY_GOOD("pro.capacity1"),
		COMPACT("pro.compact"),
		SPEED_GOOD("pro.speed1"),
		MAGNETIC("neu.magnetic"),
		MECHANICAL("neu.mechanical"),
		OPTICAL("neu.optical"),
		FLASH("neu.flash"),
		NA("neu.na"),
		FRAGILE("con.fragile"),
		SPEED_POOR("con.speed1"),
		MAGNET("con.magnet"),
		EXPOSED("con.exposed"),
		MECHANICAL_BAD("con.mechanical"),
		CAPACITY_POOR("con.capacity1"),
		NAME("con.name");
		public String key = "storage.desc.";
		private ProsConsList(String string)
		{
			key += string;
		}
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean bool)
	{
		super.addInformation(itemStack, player, list, bool);
		IStorageItemEnum storageData = IStorageItemEnum.getInstance(theEnum, itemStack);
		// Auto-generated
		long written = storageData.getMaxCapacity();
		if (itemStack.hasTagCompound())
			written = IDataStorageItem.getUsedCapacity(itemStack);
		
		String capacityPercent = BobMathUtil.getShortNumber((written * 100) / storageData.getMaxCapacity());
		list.add("Capacity: " + capacityPercent + '%');
		list.add(String.format("(%s/%sbyte)", BobMathUtil.getShortNumber(written), BobMathUtil.getShortNumber(storageData.getMaxCapacity())));
		list.add(String.format("Write rate: %sbyte/s", BobMathUtil.getShortNumber(storageData.getWriteSpeed())));
		list.add(String.format("Read rate: %sbyte/s", BobMathUtil.getShortNumber(storageData.getReadSpeed())));
		
		list.add("");
		if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			list.add(I18nUtil.resolveKey(HbmCollection.lshift, I18nUtil.resolveKey("desc.item.storage.proscons")));
		else
		{
			list.clear();
			// There's probably a better way of doing this, I'll figure it out eventually, but until then, this'll do
			// Turns out, you could sort lists of enums the entire time
			ArrayList<ProsConsList> sortedList = new ArrayList<>(storageData.prosCons());
			Collections.sort(sortedList);
			if (sortedList.isEmpty())
				sortedList.add(ProsConsList.NA);
			for (ProsConsList trait : sortedList)
				list.add(I18nUtil.resolveKey(trait.key));
		}
		if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			list.add("Press <LCTRL> to view stored file list");
		else
		{
			list.clear();
			for (String f : getFileList(itemStack))
				list.add(f);
		}
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1D - (double) IDataStorageItem.getUsedCapacity(stack) / (double) getMaxCapacity(stack);
	}
	/**
	 * If not null, when dropped, the item will turn into the one specified to simulate "breaking"
	 * @param item - The broken form
	 * @return Itself
	 */
	public ItemStorageMedium setFragile(@Nonnull Item item)
	{
		this.dropItem = Optional.of(item);
		return this;
	}
	/**
	 * Sets the item to become "dirty" when in inventory after some time
	 * @return Itself
	 */
	public ItemStorageMedium isExposed()
	{
		return this;
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		if (entityItem != null && !entityItem.worldObj.isRemote && entityItem.onGround && dropItem.isPresent())
		{
			entityItem.setEntityItemStack(new ItemStack(dropItem.get()));
			return true;
		}		
		return false;
	}
		
	@Override
	public long getMaxCapacity(ItemStack stack)
	{
		return IStorageItemEnum.getInstance(theEnum, stack).getMaxCapacity();
	}

	@Override
	public long getReadSpeed(ItemStack stack)
	{
		return IStorageItemEnum.getInstance(theEnum, stack).getReadSpeed();
	}

	@Override
	public long getWriteSpeed(ItemStack stack)
	{
		return IStorageItemEnum.getInstance(theEnum, stack).getWriteSpeed();
	}

	@Override
	public boolean writeFile(File file, ItemStack stack)
	{
		IStorageItemEnum instance = IStorageItemEnum.getInstance(theEnum, stack);
		if (file.getSize() + IDataStorageItem.getUsedCapacity(stack) > instance.getMaxCapacity())
			return false;
		else
		{
			if (!stack.hasTagCompound())
				stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setByteArray(PREFIX + file.getName(), file.toBytes());
			stack.stackTagCompound.setString(PREFIX + file.getName() + H_SUFFIX, Library.getHash(file.toBytes()));
			HashSet<String> list = new HashSet<String>(getFileList(stack));
			list.add(file.getName());
			stack.stackTagCompound.setByteArray(KEY_FILE_LIST, Library.compressStringCollection(list));
			IDataStorageItem.setUsedCapacity(stack, IDataStorageItem.getUsedCapacity(stack) + file.getSize());
			return true;
		}
	}

	@Override
	public boolean deleteFile(String filename, ItemStack stack)
	{
		if (getFileList(stack).contains(filename))
		{
			long size = readFile(filename, stack).getSize();
			IDataStorageItem.setUsedCapacity(stack, IDataStorageItem.getUsedCapacity(stack) - size);
			stack.stackTagCompound.removeTag(PREFIX + filename);
			HashSet<String> list = new HashSet<>(getFileList(stack));
			list.remove(filename);
			stack.stackTagCompound.setByteArray(KEY_FILE_LIST, Library.compressStringCollection(list));
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean overwriteFile(File file, ItemStack stack)
	{
		IStorageItemEnum instance = IStorageItemEnum.getInstance(theEnum, stack);
		if (getFileList(stack).contains(file.getName()))
		{
			if (file.getSize() + IDataStorageItem.getUsedCapacity(stack) > instance.getMaxCapacity())
				return false;
			else
			{
				writeFile(file, stack);
				return true;
			}
		}
		else
			return false;
	}

	@Override
	public File readFile(String filename, ItemStack stack)
	{
		if (getFileList(stack).contains(PREFIX + filename))
			return File.fromBytes(stack.stackTagCompound.getByteArray(PREFIX + filename));
		else
			return null;
	}
	
	@Override
	public String readFileHash(String filename, ItemStack stack)
	{
		if (getFileList(stack).contains(PREFIX + filename))
			return stack.stackTagCompound.getString(PREFIX + filename + H_SUFFIX);
		else
			return "";
	}

	@Override
	public Set<String> getFileList(ItemStack stack)
	{
		return ImmutableSet.copyOf(Library.decompressStringBytes(stack.getTagCompound().getByteArray(KEY_FILE_LIST)));
	}
}
