package com.hbm.items.food;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.hbm.config.VersatileConfig;
import com.hbm.interfaces.Spaghetti;
import com.hbm.items.ModItems;
import com.hbm.items.special.ItemCustomLore;
import com.hbm.main.MainRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
@Spaghetti("yet another if statement chain")
public class ItemEnergy extends ItemCustomLore
{
	private boolean isCan = false;
	private final Consumer<EntityPlayer> onConsume;
	private Optional<Item> cap = Optional.empty(), container = Optional.empty();
	public ItemEnergy(Consumer<EntityPlayer> onConsume)
	{
		this.onConsume = onConsume;
	}
	
	public ItemEnergy()
	{
		// TODO Auto-generated constructor stub
		onConsume = (player) -> {return;};
	}
	
	public ItemEnergy() {
		this.setCreativeTab(MainRegistry.consumableTab);
	}
	
	public ItemEnergy makeCan() {
		this.container = ModItems.can_empty;
		this.cap = ModItems.ring_pull;
		this.requiresOpener = false;
		this.setContainerItem(this.container);
		return this;
	}
	
	public ItemEnergy makeBottle(Item bottle, Item cap)
	{
		container = Optional.of(bottle);
		this.cap = Optional.of(cap);
		return this;
	}
	
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {

		if(!player.capabilities.isCreativeMode) {
			--stack.stackSize;
		}

		if(!world.isRemote) {

			if(player instanceof FakePlayer) {
				world.newExplosion(player, player.posX, player.posY, player.posZ, 5F, true, true);
				return super.onEaten(stack, world, player);
			}
			
			VersatileConfig.applyPotionSickness(player, 5);
			onConsume.accept(player);
		}

		if(!player.capabilities.isCreativeMode) {

			if(this.cap.isPresent()) {
				
				if(this == ModItems.bottle2_sunset && world.rand.nextInt(20) == 0)
					player.inventory.addItemStackToInventory(new ItemStack(ModItems.cap_star));
				else
					player.inventory.addItemStackToInventory(new ItemStack(this.cap.get()));
			}
			if(this.container.isPresent()) {
				if(stack.stackSize <= 0) {
					return new ItemStack(this.container.get());
				}
				player.inventory.addItemStackToInventory(new ItemStack(this.container.get()));
			}
		}
		player.inventory.addItemStackToInventory(getContainerItem(stack));
		if (isCan)
			player.inventory.addItemStackToInventory(new ItemStack(ModItems.ring_pull));
		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.drink;
	}

	@Spaghetti("cover yourself in oil")
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {

		if(VersatileConfig.hasPotionSickness(p_77659_3_))
			return p_77659_1_;
		
			if(this.requiresOpener && !p_77659_3_.inventory.hasItem(ModItems.bottle_opener))
				return p_77659_1_;

		p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));

		return p_77659_1_;
	}
	@Spaghetti("IF THIS IF THIS IF THIS IF THIS IF THIS IF THIS IF THIS IF THIS IF THIS IF THIS IF THIS IF THIS...")
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
		if(this == ModItems.can_smart) {
			list.add("Cheap and full of bubbles");
		}
		if(this == ModItems.can_creature) {
			list.add("Basically gasoline in a tin can");
		}
		if(this == ModItems.can_redbomb) {
			list.add("Liquefied explosives");
		}
		if(this == ModItems.can_mrsugar) {
			list.add("An intellectual drink, for the chosen ones!");
		}
		if(this == ModItems.can_overcharge) {
			list.add("Possible side effects include heart attacks, seizures or zombification");
		}
		if(this == ModItems.can_luna) {
			list.add("Contains actual selenium and star metal. Tastes like night.");
		}
		if(this == ModItems.can_bepis) {
			list.add("beppp");
		}
		if(this == ModItems.can_breen) {
			list.add("Don't drink the water. They put something in it, to make you forget.");
			list.add("I don't even know how I got here.");
		}
		if(this == ModItems.chocolate_milk) {
			list.add("Regular chocolate milk. Safe to drink.");
			list.add("Totally not made from nitroglycerine.");
		}
		if(this == ModItems.bottle_nuka) {
			list.add("Contains about 210 kcal and 1500 mSv.");
			list.add("[Requires bottle opener]");
		}
		if(this == ModItems.bottle_cherry) {
			list.add("Now with severe radiation poisoning in every seventh bottle!");
			list.add("[Requires bottle opener]");
		}
		if(this == ModItems.bottle_quantum) {
			list.add("Comes with a colorful mix of over 70 isotopes!");
			list.add("[Requires bottle opener]");
		}
		if(this == ModItems.bottle2_korl) {
			list.add("Contains actual orange juice!");
			list.add("[Requires bottle opener]");
		}
		if(this == ModItems.bottle2_fritz) {
			list.add("moremore caffeine");
			list.add("[Requires bottle opener]");
		}
		if(this == ModItems.bottle2_korl_special) {
			if(MainRegistry.polaroidID == 11)
				list.add("shgehgev u rguer");
			else
				list.add("Contains actual orange juice!");
			list.add("[Requires bottle opener]");
		}
		if(this == ModItems.bottle2_fritz_special) {
			if(MainRegistry.polaroidID == 11)
				list.add("ygrogr fgrof bf");
			else
				list.add("moremore caffeine");
			list.add("[Requires bottle opener]");
		}
		if(this == ModItems.bottle_sparkle) {
			if(MainRegistry.polaroidID == 11)
				list.add("Contains trace amounts of taint.");
			else
				list.add("The most delicious beverage in the wasteland!");
			list.add("[Requires bottle opener]");
		}
		if(this == ModItems.bottle_rad) {
			if(MainRegistry.polaroidID == 11)
				list.add("Now with 400% more radiation!");
			else
				list.add("Tastes like radish and radiation.");
			list.add("[Requires bottle opener]");
		}
		if(this == ModItems.bottle2_sunset) {
			if(MainRegistry.polaroidID == 11) {
				list.add("\"Authentic Sunset Juice\"");
				list.add("");
				list.add("This smells like fish.");
				list.add("*sip*");
				list.add("Yup, that's pretty disugsting.");
				list.add("...");
				list.add("...");
				list.add("*sip*");
			} else {
				list.add("The eternal #2. Screw you, Bradberton!");
			}
			list.add("[Requires bottle opener]");
		}
	}
}
