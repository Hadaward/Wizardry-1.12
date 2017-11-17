package com.teamwizardry.wizardry.common.item.halos;

import baubles.api.BaubleType;
import com.teamwizardry.librarianlib.features.base.item.ItemModBauble;
import com.teamwizardry.wizardry.api.capability.BaubleWizardryCapability;
import com.teamwizardry.wizardry.api.capability.CapManager;
import com.teamwizardry.wizardry.api.capability.WizardryCapabilityProvider;
import com.teamwizardry.wizardry.api.item.IFakeHalo;
import com.teamwizardry.wizardry.api.item.IHalo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Saad on 8/30/2016.
 */
@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemRealHaloBauble extends ItemModBauble implements IFakeHalo, IHalo {

	public ItemRealHaloBauble() {
		super("halo_real");
		setMaxStackSize(1);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new WizardryCapabilityProvider(new BaubleWizardryCapability(stack, 50000, 50000, 0, 0));
	}

	@Override
	public void onWornTick(@NotNull ItemStack stack, @NotNull EntityLivingBase player) {
		if (player.world.isRemote) return;
		CapManager manager = new CapManager(stack);
		if (!manager.isManaFull()) manager.addMana(manager.getMaxMana() * 0.001);
		if (!manager.isBurnoutEmpty()) manager.removeBurnout(manager.getMaxBurnout() * 0.001);
	}

	@Override
	public void onEquippedOrLoadedIntoWorld(@NotNull ItemStack stack, @NotNull EntityLivingBase player) {
		super.onEquippedOrLoadedIntoWorld(stack, player);
		if (player.world.isRemote) return;
		new CapManager(stack).setEntity(player).sync(true);
	}

	@Override
	public void onEquipped(@NotNull ItemStack stack, @NotNull EntityLivingBase player) {
		super.onEquipped(stack, player);
		if (player.world.isRemote) return;
		new CapManager(stack).setEntity(player).sync(true);
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		if (player.world.isRemote) return;
		new CapManager(itemstack).setEntity(player).sync(true);
	}

	@Nonnull
	@Optional.Method(modid = "baubles")
	@Override
	public BaubleType getBaubleType(@NotNull ItemStack itemStack) {
		return BaubleType.HEAD;
	}
}