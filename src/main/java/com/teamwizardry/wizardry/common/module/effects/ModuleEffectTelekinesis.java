package com.teamwizardry.wizardry.common.module.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.numeric.InterpFloatInOut;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.wizardry.Wizardry;
import com.teamwizardry.wizardry.api.NBTConstants;
import com.teamwizardry.wizardry.api.spell.IContinuousModule;
import com.teamwizardry.wizardry.api.spell.SpellData;
import com.teamwizardry.wizardry.api.spell.SpellRing;
import com.teamwizardry.wizardry.api.spell.annotation.RegisterModule;
import com.teamwizardry.wizardry.api.spell.attribute.AttributeRegistry;
import com.teamwizardry.wizardry.api.spell.module.IModuleEffect;
import com.teamwizardry.wizardry.api.spell.module.ModuleInstanceEffect;
import com.teamwizardry.wizardry.api.util.RandUtil;
import com.teamwizardry.wizardry.api.util.interp.InterpScale;
import com.teamwizardry.wizardry.init.ModPotions;
import com.teamwizardry.wizardry.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Demoniaque.
 */
@RegisterModule(ID="effect_telekinesis")
public class ModuleEffectTelekinesis implements IModuleEffect, IContinuousModule {

	@Override
	public String[] compatibleModifiers() {
		return new String[]{"modifier_increase_aoe"};
	}

	@Override
	public boolean run(@NotNull World world, ModuleInstanceEffect instance, @Nonnull SpellData spell, @Nonnull SpellRing spellRing) {
		Vec3d targetPos = spell.getTarget(world);
		Entity caster = spell.getCaster(world);

		double potency = spellRing.getAttributeValue(world, AttributeRegistry.AREA, spell) * 2;

		if (targetPos == null) return false;

		List<Entity> entityList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(new BlockPos(targetPos)).grow(potency, potency, potency));

		if (RandUtil.nextInt(10) == 0)
			world.playSound(null, new BlockPos(targetPos), ModSounds.ETHEREAL_PASS_BY, SoundCategory.NEUTRAL, 0.5f, RandUtil.nextFloat());
		for (Entity entity : entityList) {
			double dist = entity.getPositionVector().distanceTo(targetPos);
			if (dist > potency) continue;
			if (!spellRing.taxCaster(world, spell, true)) return false;

			final double upperMag = 1;
			final double scale = 1;
			double mag = upperMag * (scale * dist / (-scale * dist - 1) + 1);

			Vec3d dir = targetPos.subtract(entity.getPositionVector()).normalize().scale(mag);

			entity.motionX = (dir.x);
			entity.motionY = (dir.y);
			entity.motionZ = (dir.z);
			entity.fallDistance = 0;
			entity.velocityChanged = true;

			if (entity instanceof EntityLivingBase) {
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(ModPotions.NULLIFY_GRAVITY, 2, 1, false, false));
			}

		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderSpell(World world, ModuleInstanceEffect instance, @Nonnull SpellData spell, @Nonnull SpellRing spellRing) {
		Vec3d position = spell.getTarget(world);

		if (position == null) return;

		ParticleBuilder glitter = new ParticleBuilder(50);
		glitter.setColorFunction(new InterpColorHSV(instance.getPrimaryColor(), instance.getSecondaryColor()));
		glitter.setScale(1);
		glitter.setRender(new ResourceLocation(Wizardry.MODID, NBTConstants.MISC.SPARKLE_BLURRED));

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 5, 0, (aFloat, particleBuilder) -> {
			glitter.setLifetime(RandUtil.nextInt(10, 20));
			glitter.setScale(RandUtil.nextFloat());
			glitter.setScaleFunction(new InterpScale(1, 0));
			glitter.setAlphaFunction(new InterpFloatInOut(0.3f, RandUtil.nextFloat()));
			glitter.setMotion(new Vec3d(
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1)
			));
		});
	}
}
