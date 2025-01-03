package com.teamwizardry.wizardry.common.item.halos;

import baubles.api.BaubleType;
import com.teamwizardry.librarianlib.features.base.item.ItemModBauble;
import com.teamwizardry.wizardry.api.capability.player.mana.ManaManager;
import com.teamwizardry.wizardry.api.config.ConfigHandler;
import com.teamwizardry.wizardry.api.item.halo.IHalo;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Demoniaque on 8/30/2016.
 */
@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemRealHaloBauble extends ItemModBauble implements IHalo {

	public ItemRealHaloBauble() {
		super("halo_real");
		setMaxStackSize(1);
	}

	@Override
	public void onWornTick(@Nonnull ItemStack stack, @Nonnull EntityLivingBase player) {
		if (player.world.isRemote) return;

		try (ManaManager.CapManagerBuilder mgr = ManaManager.forObject(player)) {
			mgr.setMaxMana(ConfigHandler.server.item.realHaloBufferSize);
			mgr.setMaxBurnout(ConfigHandler.server.item.realHaloBufferSize);
			mgr.removeBurnout(mgr.getMaxBurnout() * ConfigHandler.server.item.haloGenSpeed * 2);
			mgr.addMana(mgr.getMaxMana() * ConfigHandler.server.item.haloGenSpeed);
		}
	}

	@Nonnull
	@Optional.Method(modid = "baubles")
	@Override
	public BaubleType getBaubleType(@Nonnull ItemStack itemStack) {
		return BaubleType.HEAD;
	}

	@NotNull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

	@Override
	public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		tooltip.addAll(getHaloTooltip(stack));
	}
}
