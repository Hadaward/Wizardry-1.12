package com.teamwizardry.wizardry.client.render.item;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.numeric.InterpFloatInOut;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpCircle;
import com.teamwizardry.librarianlib.features.particle.ParticleBase;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleRenderManager;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.RenderFunction;
import com.teamwizardry.librarianlib.features.particle.functions.RenderFunctionBasic;
import com.teamwizardry.wizardry.Wizardry;
import com.teamwizardry.wizardry.api.NBTConstants;
import com.teamwizardry.wizardry.api.config.ConfigHandler;
import com.teamwizardry.wizardry.api.item.BaublesSupport;
import com.teamwizardry.wizardry.api.util.ColorUtils;
import com.teamwizardry.wizardry.api.util.RandUtil;
import com.teamwizardry.wizardry.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.awt.*;

public class RenderHaloEntity implements LayerRenderer<EntityLivingBase> {

	private ModelRenderer modelRenderer;

	public RenderHaloEntity(ModelRenderer modelRenderer) {
		this.modelRenderer = modelRenderer;
	}

	@Override
	public void doRenderLayer(@Nonnull EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (BaublesSupport.getItem(entitylivingbaseIn, ModItems.FAKE_HALO, ModItems.CREATIVE_HALO, ModItems.REAL_HALO).isEmpty())
			return;
		ItemStack halo = BaublesSupport.getItem(entitylivingbaseIn, ModItems.FAKE_HALO, ModItems.CREATIVE_HALO, ModItems.REAL_HALO);

		// TODO: Remove these once we have a cosmetics system
		if (halo.getItem() == ModItems.FAKE_HALO && !ConfigHandler.client.render.renderCrudeHalo) return;
		if (halo.getItem() == ModItems.REAL_HALO && !ConfigHandler.client.render.renderRealHalo) return;
		if (halo.getItem() == ModItems.CREATIVE_HALO && !ConfigHandler.client.render.renderCreativeHalo) return;

		if (halo.getItem() == ModItems.FAKE_HALO) {
			GlStateManager.pushMatrix();

			if (entitylivingbaseIn.isSneaking()) GlStateManager.translate(0.0f, 0.2f, 0.0f);

			boolean flag = entitylivingbaseIn instanceof EntityVillager || entitylivingbaseIn instanceof EntityZombieVillager;

			if (entitylivingbaseIn.isChild() && !(entitylivingbaseIn instanceof EntityVillager)) {
				GlStateManager.translate(0.0f, 0.5f * scale, 0.0f);
				GlStateManager.scale(0.7f, 0.7f, 0.7f);
				GlStateManager.translate(0.0f, 16.0f * scale, 0.0f);
			}

			if (flag) GlStateManager.translate(0.0f, 0.1875f, 0.0f);

			this.modelRenderer.postRender(0.0625f);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			GlStateManager.translate(0.0f, -0.25f, 0.0f);
			GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
			GlStateManager.scale(0.625f, -0.625f, -0.625f);

			Minecraft.getMinecraft().getItemRenderer().renderItem(entitylivingbaseIn, halo, ItemCameraTransforms.TransformType.HEAD);

			GlStateManager.popMatrix();
		} else {
		    Entity entity = entitylivingbaseIn;

			Vec3d entityOrigin = entity.getPositionVector().add(0, entity.height + (entity.isSneaking() ? 0.2 : 0.4), 0);
			InterpCircle circle = new InterpCircle(Vec3d.ZERO, new Vec3d(0, 1, 0), 0.3f, RandUtil.nextFloat(), RandUtil.nextFloat());

			for (Vec3d origin : circle.list(5)) {
				RenderFunction baseRenderFunction = new RenderFunctionBasic(new ResourceLocation(Wizardry.MODID, NBTConstants.MISC.SPARKLE_BLURRED), false);
				ParticleBuilder glitter = new ParticleBuilder(3);
				glitter.setAlphaFunction(new InterpFloatInOut(1f, 1f));
				glitter.disableMotionCalculation();
				glitter.disableRandom();

				ParticleSpawner.spawn(glitter, entity.world, new StaticInterp<>(entityOrigin.add(origin)), 1, 0, (aFloat, particleBuilder) -> {
					if (RandUtil.nextInt(10) != 0)
						if (halo.getItem() == ModItems.CREATIVE_HALO)
							glitter.setColor(ColorUtils.changeColorAlpha(new Color(0xd600d2), RandUtil.nextInt(60, 100)));
						else glitter.setColor(ColorUtils.changeColorAlpha(Color.YELLOW, RandUtil.nextInt(60, 100)));
					else glitter.setColor(ColorUtils.changeColorAlpha(Color.WHITE, RandUtil.nextInt(60, 100)));
					glitter.setAlphaFunction(new InterpFloatInOut(0.5f, 0.5f));
					glitter.setLifetime(10);
					glitter.setScaleFunction(new InterpFloatInOut(0.5f, 0.5f));

					glitter.setRenderFunction(
							new RenderFunction(ParticleRenderManager.getLAYER_BLOCK_MAP_ADDITIVE()) {
								@Override
								public void render(float i,
												   @NotNull ParticleBase particle, @NotNull Color color, float alpha,
												   @NotNull BufferBuilder worldRendererIn, @Nullable Entity entityIn,
												   float partialTicks, float rotationX, float rotationZ,
												   float rotationYZ, float rotationXY, float rotationXZ,
												   float scale, float rotation, @NotNull Vec3d pos,
												   int skyLight, int blockLight) {

									Vec3d interpPos =
											new Vec3d(
//													ClientTickHandler.interpPartialTicks(entity.prevPosX, entity.posX) - Particle.interpPosX,
//													ClientTickHandler.interpPartialTicks(entity.prevPosY, entity.posY) - Particle.interpPosY,
//													ClientTickHandler.interpPartialTicks(entity.prevPosZ, entity.posZ) - Particle.interpPosZ
													entity.prevPosX + (entity.posX - entity.prevPosX) * ClientTickHandler.getPartialTicks() - Particle.interpPosX,
													entity.prevPosY + (entity.posY - entity.prevPosY) * ClientTickHandler.getPartialTicks() - Particle.interpPosY,
													entity.prevPosZ + (entity.posZ - entity.prevPosZ) * ClientTickHandler.getPartialTicks() - Particle.interpPosZ
											);
									Vec3d newPos = interpPos
											.add(0, entity.height + (entity.isSneaking() ? 0.2 : 0.4), 0)
											.add(origin);

									baseRenderFunction.render(i,
											particle, color, alpha,
											worldRendererIn, entityIn,
											partialTicks, rotationX, rotationZ,
											rotationYZ, rotationXY, rotationXZ,
											scale, rotation, newPos,
											skyLight, blockLight);
								}
							}
					);
				});
			}
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
