package com.hbm.handler;

import java.util.Optional;

import javax.annotation.CheckReturnValue;

import com.google.common.annotations.Beta;
import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.BlockBobble.TileEntityBobble;
import com.hbm.blocks.machine.NTMAnvil;
import com.hbm.inventory.container.*;
import com.hbm.inventory.container.warpcore.*;
import com.hbm.inventory.gui.*;
import com.hbm.inventory.gui.warpcore.*;
import com.hbm.inventory.inv.*;
import com.hbm.tileentity.bomb.*;
import com.hbm.tileentity.machine.*;
import com.hbm.tileentity.machine.computer.*;
import com.hbm.tileentity.machine.rbmk.*;
import com.hbm.tileentity.machine.storage.*;
import com.hbm.tileentity.machine.oil.*;
import com.hbm.tileentity.turret.*;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
/**
 * Guess who's back?
 * @author UFFR
 *
 */
@Beta
public enum EnumGUI
{
	// TODO: Alphabetical order
	NUKE_TEST(ContainerTestNuke.class, GUITestNuke.class, TileEntityTestNuke.class),
	COMPUTER(ContainerComputer.class, GUIComputer.class, TileEntityComputer.class),
	ANVIL(new CustomGUIBehavior()
			{
				@Override public Object getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return world.getBlock(x, y, z) instanceof NTMAnvil ? new ContainerAnvil(player.inventory, ((NTMAnvil) world.getBlock(x, y, z)).tier) : null;}
				@Override public Object getGUI(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return world.getBlock(x, y, z) instanceof NTMAnvil ? new GUIAnvil(player.inventory, ((NTMAnvil) world.getBlock(x, y, z)).tier) : null;}
			}),
	ARMOR_TABLE(new CustomGUIBehavior()
			{
				@Override public Object getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return world.getBlock(x, y, z) == ModBlocks.machine_armor_table ? new ContainerArmorTable(player.inventory) : null;}
				@Override public Object getGUI(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return world.getBlock(x, y, z) == ModBlocks.machine_armor_table ? new GUIArmorTable(player.inventory) : null;}
			}),
	TURRET_MAXWELL(ContainerTurretBase.class, GUITurretMaxwell.class, TileEntityTurretBaseNT.class),
	TURRET_HOWARD(ContainerTurretBase.class, GUITurretHoward.class, TileEntityTurretBaseNT.class),
	TURRET_RICHARD(ContainerTurretBase.class, GUITurretRichard.class, TileEntityTurretBaseNT.class),
	TURRET_TAUON(ContainerTurretBase.class, GUITurretTauon.class, TileEntityTurretBaseNT.class),
	TURRET_JEREMY(ContainerTurretBase.class, GUITurretJeremy.class, TileEntityTurretBaseNT.class),
	TURRET_MR_FRIENDLY(ContainerTurretBase.class, GUITurretFriendly.class, TileEntityTurretBaseNT.class),
	TURRET_CHEKHOV(ContainerTurretBase.class, GUITurretChekhov.class, TileEntityTurretBaseNT.class),
	TURRET_BRANDON(ContainerTurretBase.class, GUITurretBase.class, TileEntityTurretBaseNT.class),// TODO
	TURRET_FRITZ(ContainerTurretBase.class, GUITurretFritz.class, TileEntityTurretBaseNT.class),
	TURRET_TSUKUYOMI(ContainerTsukuyomi.class, GUITsukuyomi.class, TileEntityTsukuyomi.class),
	TURRET_BISHAMONTEN(ContainerBishamonten.class, GUIBishamonten.class, TileEntityBishamonten.class),
	RBMK_CONSOLE(GUIRBMKConsole.class, TileEntityRBMKConsole.class),
	RBMK_FUEL(ContainerRBMKRod.class, GUIRBMKRod.class, TileEntityRBMKRod.class),
	RBMK_BOILER(ContainerRBMKBoiler.class, GUIRBMKBoiler.class, TileEntityRBMKBoiler.class),
	RBMK_CONTROL_MANUAL(ContainerRBMKControl.class, GUIRBMKControl.class, TileEntityRBMKControlManual.class),
	RBMK_CONTROL_AUTO(ContainerRBMKControlAuto.class, GUIRBMKControlAuto.class, TileEntityRBMKControlAuto.class),
	RBMK_STORAGE(ContainerRBMKStorage.class, GUIRBMKStorage.class, TileEntityRBMKStorage.class),
	RBMK_OUTGASSER(ContainerRBMKOutgasser.class, GUIRBMKOutgasser.class, TileEntityRBMKOutgasser.class),
	ATOMIC_CLOCK(ContainerAtomicClock.class, GUIAtomicClock.class, TileEntityAtomicClock.class),
//	WARP_CORE(ContainerReactorWarp.class, GUIReactorWarp.class, TileEntityReactorWarp.class),
	REACTOR_WARP_CORE(ContainerReactorWarpCore.class, GUIReactorWarpCore.class, TileEntityReactorWarp.class),
	REACTOR_WARP_OUTPUT(ContainerReactorWarpOutput.class, GUIReactorWarpOutput.class, TileEntityReactorWarp.class),
	REACTOR_WARP_TANK(ContainerReactorWarpTank.class, GUIReactorWarpTank.class, TileEntityReactorWarp.class),
	SING_GEN(ContainerSingGen.class, GUISingGen.class, TileEntitySingGen.class),
	RTG_BFURNACE(ContainerDiFurnaceRTG.class, GUIDiFurnaceRTG.class, TileEntityDiFurnaceRTG.class),
	BFURNACE(ContainerDiFurnace.class, GUITestDiFurnace.class, TileEntityDiFurnace.class),
	NUKE_GADGET(ContainerNukeGadget.class, GUINukeGadget.class, TileEntityNukeGadget.class),
	NUKE_LITTLE_BOY(ContainerNukeBoy.class, GUINukeBoy.class, TileEntityNukeBoy.class),
	NUKE_FAT_MAN(ContainerNukeMan.class, GUINukeMan.class, TileEntityNukeMan.class),
	NUKE_IVY_MIKE(ContainerNukeMike.class, GUINukeMike.class, TileEntityNukeMike.class),
	NUKE_TSAR_BOMBA(ContainerNukeTsar.class, GUINukeTsar.class, TileEntityNukeTsar.class),
	NUKE_FLEIJA(ContainerNukeFleija.class, GUINukeFleija.class, TileEntityNukeFleija.class),
	NUKE_PROTOTYPE(ContainerNukePrototype.class, GUINukePrototype.class, TileEntityNukePrototype.class),
	NUKE_SOLINIUM(ContainerNukeSolinium.class, GUINukeSolinium.class, TileEntityNukeSolinium.class),
	NUKE_N2(ContainerNukeN2.class, GUINukeN2.class, TileEntityNukeN2.class),
	NUKE_N45(ContainerNukeN45.class, GUINukeN45.class, TileEntityNukeN45.class),
	NUKE_BALEFIRE(ContainerNukeFstbmb.class, GUINukeFstbmb.class, TileEntityNukeBalefire.class),
	NUKE_CUSTOM(ContainerNukeCustom.class, GUINukeCustom.class, TileEntityNukeCustom.class),
	CENTRIFUGE(ContainerCentrifuge.class, GUIMachineCentrifuge.class, TileEntityMachineCentrifuge.class),
	CENTRIFUGE_GAS(ContainerMachineGasCent.class, GUIMachineGasCent.class, TileEntityMachineGasCent.class),
	UF6_TANK(ContainerUF6Tank.class, GUIMachineUF6Tank.class, TileEntityMachineUF6Tank.class),
	PUF6_TANK(ContainerPuF6Tank.class, GUIMachinePuF6Tank.class, TileEntityMachinePuF6Tank.class),
	BREEDING_REACTOR(ContainerMachineReactorBreeding.class, GUIMachineReactorBreeding.class, TileEntityMachineReactorBreeding.class),
	BOMB_MULTI(ContainerBombMulti.class, GUIBombMulti.class, TileEntityBombMulti.class),
	FURNACE_NUCLEAR(ContainerNukeFurnace.class, GUINukeFurnace.class, TileEntityNukeFurnace.class),
	FURNACE_RTG(ContainerRtgFurnace.class, GUIRtgFurnace.class, TileEntityRtgFurnace.class),
	FURNACE_ELECTRIC(ContainerElectricFurnace.class, GUIMachineElectricFurnace.class, TileEntityMachineElectricFurnace.class),
	FURNACE_ARC(ContainerMachineArcFurnace.class, GUIMachineArcFurnace.class, TileEntityMachineArcFurnace.class),
	RADAR(ContainerMachineRadar.class, GUIMachineRadar.class, TileEntityMachineRadar.class),
	SAT_LINKER(ContainerMachineSatLinker.class, GUIMachineSatLinker.class, TileEntityMachineSatLinker.class),
	SAT_DOCK(ContainerSatDock.class, GUISatDock.class, TileEntityMachineSatDock.class),
	BATTERY(ContainerMachineBattery.class, GUIMachineBattery.class, TileEntityMachineBattery.class),
//	FENSU(ContainerMachineBattery.class, GUIMachineBattery.class, TileEntityMachineFENSU.class),
	GENERATOR_COAL(ContainerMachineCoal.class, GUIMachineCoal.class, TileEntityMachineCoal.class),
	GENERATOR_DIESEL(ContainerMachineDiesel.class, GUIMachineDiesel.class, TileEntityMachineDiesel.class),
	GENERATOR_RTG(ContainerMachineRTG.class, GUIMachineRTG.class, TileEntityMachineRTG.class),
	GENERATOR_TURBOFAN(ContainerMachineTurbofan.class, GUIMachineTurbofan.class, TileEntityMachineTurbofan.class),
	GENERATOR_RADIATION(ContainerMachineRadGen.class, GUIMachineRadGen.class, TileEntityMachineRadGen.class),
	GENERATOR_SELENIUM(ContainerMachineSelenium.class, GUIMachineSelenium.class, TileEntityMachineSeleniumEngine.class),
	GENERATOR_TURBINE(ContainerMachineTurbine.class, GUIMachineTurbine.class, TileEntityMachineTurbine.class),
	GENERATOR_TURBINE_LARGE(ContainerMachineLargeTurbine.class, GUIMachineLargeTurbine.class, TileEntityMachineLargeTurbine.class),
	FORCE_FIELD(ContainerForceField.class, GUIForceField.class, TileEntityForceField.class),
	AMS_BASE(ContainerAMSBase.class, GUIAMSBase.class, TileEntityAMSBase.class),
	AMS_EMITTER(ContainerAMSEmitter.class, GUIAMSEmitter.class, TileEntityAMSEmitter.class),
	AMS_LIMITER(ContainerAMSLimiter.class, GUIAMSLimiter.class, TileEntityAMSLimiter.class),
	CRATE_IRON(ContainerCrateIron.class, GUICrateIron.class, TileEntityCrateIron.class),
	CRATE_STEEL(ContainerCrateSteel.class, GUICrateSteel.class, TileEntityCrateSteel.class),
	CRATE_TUNGSTEN(ContainerCrateTungsten.class, GUICrateTungsten.class, TileEntityCrateTungsten.class),
	PRESS_STEAM(ContainerMachinePress.class, GUIMachinePress.class, TileEntityMachinePress.class),
	PRESS_ELECTRIC(ContainerMachineEPress.class, GUIMachineEPress.class, TileEntityMachineEPress.class),
	SIREN(ContainerMachineSiren.class, GUIMachineSiren.class, TileEntityMachineSiren.class),
	FEL(ContainerFEL.class, GUIFEL.class, TileEntityFEL.class),
	OIL_WELL(ContainerMachineOilWell.class, GUIMachineOilWell.class, TileEntityOilDrillBase.class),
	OIL_REFINERY(ContainerMachineRefinery.class, GUIMachineRefinery.class, TileEntityMachineRefinery.class),
	GAS_FLARE(ContainerMachineGasFlare.class, GUIMachineGasFlare.class, TileEntityMachineGasFlare.class),
	MINING_DRILL(ContainerMachineMiningDrill.class, GUIMachineMiningDrill.class, TileEntityMachineMiningDrill.class),
	MINING_LASER(ContainerMiningLaser.class, GUIMiningLaser.class, TileEntityMachineMiningLaser.class),
	ASSEMBLER(ContainerMachineAssembler.class, GUIMachineAssembler.class, TileEntityMachineAssembler.class),
	CHEM_PLANT(ContainerMachineChemplant.class, GUIMachineChemplant.class, TileEntityMachineChemplant.class),
	FLUID_TANK(ContainerMachineFluidTank.class, GUIMachineFluidTank.class, TileEntityMachineFluidTank.class),
	SILEX(ContainerSILEX.class, GUISILEX.class, TileEntitySILEX.class),
	CRYSTALLIZER(ContainerCrystallizer.class, GUICrystallizer.class, TileEntityMachineCrystallizer.class),
	SHREDDER(ContainerMachineShredder.class, GUIMachineShredder.class, TileEntityMachineShredder.class),
	FURNACE_CMB(ContainerMachineCMBFactory.class, GUIMachineCMBFactory.class, TileEntityMachineCMBFactory.class),
	IGEN(ContainerIGenerator.class, GUIIGenerator.class, TileEntityMachineIGenerator.class),
	CYCLOTRON(ContainerMachineCyclotron.class, GUIMachineCyclotron.class, TileEntityMachineCyclotron.class),
	HADRON(ContainerHadron.class, GUIHadron.class, TileEntityHadron.class),
	DFC_EMITTER(ContainerCoreEmitter.class, GUICoreEmitter.class, TileEntityCoreEmitter.class),
	DFC_RECEIVER(ContainerCoreReceiver.class, GUICoreReceiver.class, TileEntityCoreReceiver.class),
	DFC_INJECTOR(ContainerCoreInjector.class, GUICoreInjector.class, TileEntityCoreInjector.class),
	DFC_STABILIZER(ContainerCoreStabilizer.class, GUICoreStabilizer.class, TileEntityCoreStabilizer.class),
	DFC_CORE(ContainerCore.class, GUICore.class, TileEntityCore.class),
	MISSILE_ASSEMBLY(ContainerMachineMissileAssembly.class, GUIMachineMissileAssembly.class, TileEntityMachineMissileAssembly.class),
	LAUNCHER_COMPACT(ContainerCompactLauncher.class, GUIMachineCompactLauncher.class, TileEntityCompactLauncher.class),
	LAUNCHER_TABLE(ContainerLaunchTable.class, GUIMachineLaunchTable.class, TileEntityLaunchTable.class),
	LAUNCHER_SOYUZ(ContainerSoyuzLauncher.class, GUISoyuzLauncher.class, TileEntitySoyuzLauncher.class),
	DRUM_STORAGE(ContainerStorageDrum.class, GUIStorageDrum.class, TileEntityStorageDrum.class),
	DRUM_WASTE(ContainerWasteDrum.class, GUIWasteDrum.class, TileEntityWasteDrum.class),
	//TODO: FOOD_SYNTH
	MICROWAVE(ContainerMicrowave.class, GUIMicrowave.class, TileEntityMicrowave.class),
	BARREL(ContainerBarrel.class, GUIBarrel.class, TileEntityBarrel.class),
	TELEPORTER(ContainerMachineTeleporter.class, GUIMachineTeleporter.class, TileEntityMachineTeleporter.class),
	TELELINKER(ContainerMachineTeleLinker.class, GUIMachineTeleLinker.class, TileEntityMachineTeleLinker.class),
	KEY_FORGE(ContainerMachineKeyForge.class, GUIMachineKeyForge.class, TileEntityMachineKeyForge.class),
	RADIO_RECORDER(ContainerRadioRec.class, GUIRadioRec.class, TileEntityRadioRec.class),
	SAFE(ContainerSafe.class, GUISafe.class, TileEntitySafe.class),
	BOILER(ContainerMachineBoiler.class, GUIMachineBoiler.class, TileEntityMachineBoiler.class),
	BOILER_ELECTRIC(ContainerMachineBoilerElectric.class, GUIMachineBoilerElectric.class, TileEntityMachineBoilerElectric.class),
	PLASMA_HEATER(ContainerPlasmaHeater.class, GUIPlasmaHeater.class, TileEntityMachinePlasmaHeater.class),
	REACTOR_FUSION(ContainerITER.class, GUIITER.class, TileEntityITER.class),
	REACTOR_RESEARCH_CONTROL(ContainerReactorControl.class, GUIReactorControl.class, TileEntityReactorControl.class),
	REACTOR_ZIRNOX(ContainerReactorZirnox.class, GUIReactorZirnox.class, TileEntityReactorZirnox.class),
	REACTOR_WATZ(ContainerWatzCore.class, GUIWatzCore.class, TileEntityWatzCore.class),
	REACTOR_FWATZ(ContainerFWatzCore.class, GUIFWatzCore.class, TileEntityFWatzCore.class),
	REACTOR_GENERIC_LARGE(ContainerReactorMultiblock.class, GUIReactorMultiblock.class, TileEntityMachineReactorLarge.class),
	REACTOR_RESEARCH(ContainerReactorResearch.class, GUIReactorResearch.class, TileEntityReactorResearch.class),
	LIQUEFACTOR(ContainerLiquefactor.class, GUILiquefactor.class, TileEntityMachineLiquefactor.class),
	SOLIDIFIER(ContainerSolidifier.class, GUISolidifier.class, TileEntityMachineSolidifier.class),
	WASTE_DRUM(ContainerWasteDrum.class, GUIWasteDrum.class, TileEntityWasteDrum.class),
	ITEM_FOLDER(GUIScreenTemplateFolder.class),
	ITEM_HOLOTAPE(GUIScreenHolotape.class),
	ITEM_DESIGNATOR(GUIScreenDesignator.class),
	ITEM_SAT_INTERFACE(GUIScreenSatInterface.class),
	ITEM_SAT_COORD(GUIScreenSatCoord.class),
	ITEM_BOX(new CustomGUIBehavior()
			{
				@Override public Object getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return null;}
				@Override public Object getGUI(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return new GUILeadBox(new ContainerLeadBox(player, player.inventory, new InventoryLeadBox(player.getHeldItem())));}
			}),
	ITEM_BOBMAZON(new CustomGUIBehavior()
			{
				@Override public Object getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return null;}
				@Override public Object getGUI(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return BobmazonOfferFactory.getOffers(player.getHeldItem()) != null ? new GUIScreenBobmazon(player, BobmazonOfferFactory.getOffers(player.getHeldItem())) : null;}
			}),
	ITEM_BLACK_BOOK(new CustomGUIBehavior()
			{
				@Override public Object getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return new ContainerBook(player.inventory);}
				@Override public Object getGUI(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return new GUIBook(player);}
			}),
	ITEM_GUIDE(GUIScreenGuide.class),
	BOBBLE(new CustomGUIBehavior()
			{
				@Override public Object getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return null;}
				@Override public Object getGUI(TileEntity entity, EntityPlayer player, World world, int x, int y, int z) {return new GUIScreenBobble((TileEntityBobble) entity);}
			}),
	CAPSULE(ContainerSoyuzCapsule.class, GUISoyuzCapsule.class, TileEntitySoyuzCapsule.class),
	LAUNCH_PAD(ContainerLaunchPadTier1.class, GUILaunchPadTier1.class, TileEntityLaunchPad.class),
	FACTORY_TITANIUM(ContainerCoreTitanium.class, GUICoreTitanium.class, TileEntityCoreTitanium.class),
	FACTORY_ADVANCED(ContainerCoreAdvanced.class, GUICoreAdvanced.class, TileEntityCoreAdvanced.class),
	TRANSMUTATOR(ContainerMachineSchrabidiumTransmutator.class, GUIMachineSchrabidiumTransmutator.class, TileEntityMachineSchrabidiumTransmutator.class),
	CEL_PRIME(ContainerCelPrime.class, GUICelPrime.class, TileEntityCelPrime.class);
	
	private Optional<Class<? extends Container>> containerClass = Optional.empty();
	private Optional<CustomGUIBehavior> guiBehavior = Optional.empty();
	private Class<? extends GuiScreen> guiClass;
	private Class<? extends TileEntity> teClass;
	private boolean standardBehavior;
	private EnumGUI(Class<? extends Container> container, Class<? extends GuiScreen> gui, Class<? extends TileEntity> te)
	{
		containerClass = Optional.of(container);
		guiClass = gui;
		teClass = te;
		standardBehavior = true;
	}
	private EnumGUI(Class<? extends GuiScreen> gui)
	{
		guiClass = gui;
		standardBehavior = false;
	}
	private EnumGUI(Class<? extends GuiScreen> gui, Class<? extends TileEntity> te)
	{
		guiClass = gui;
		teClass = te;
		standardBehavior = true;
	}
	private EnumGUI(CustomGUIBehavior guiBehavior)
	{
		this.guiBehavior = Optional.of(guiBehavior);
		standardBehavior = false;
	}
	@CheckReturnValue
	public Optional<Class<? extends Container>> getContainer()
	{
		return containerClass;
	}
	public Class<? extends GuiScreen> getGui()
	{
		return guiClass;
	}
	public Class<? extends TileEntity> getTE()
	{
		return teClass;
	}
	public boolean hasStandardBehavior()
	{
		return standardBehavior;
	}
	public Optional<CustomGUIBehavior> getCustomBehavior()
	{
		return guiBehavior;
	}
	public interface CustomGUIBehavior
	{
		Object getContainer(TileEntity entity, EntityPlayer player, World world, int x, int y, int z);
		Object getGUI(TileEntity entity, EntityPlayer player, World world, int x, int y, int z);
	}
}
