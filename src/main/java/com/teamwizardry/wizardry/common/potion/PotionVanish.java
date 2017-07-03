package com.teamwizardry.wizardry.common.potion;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import com.teamwizardry.librarianlib.features.methodhandles.MethodHandleHelper;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.wizardry.Wizardry;
import com.teamwizardry.wizardry.api.events.EntityRenderShadowAndFireEvent;
import com.teamwizardry.wizardry.common.network.PacketVanishPotion;
import com.teamwizardry.wizardry.init.ModSounds;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LordSaad.
 */
// TODO: mob ai, other player testing, proper in/out fading
public class PotionVanish extends PotionMod {

	private Function2<RenderLivingBase, Object[], Object> interpolateRotation = MethodHandleHelper.wrapperForMethod(RenderLivingBase.class, new String[]{"interpolateRotation", "func_77034_a", "a"}, float.class, float.class, float.class);
	private Function2<RenderLivingBase, Object[], Object> renderLivingAt = MethodHandleHelper.wrapperForMethod(RenderLivingBase.class, new String[]{"renderLivingAt", "func_77039_a", "a"}, EntityLivingBase.class, double.class, double.class, double.class);
	private Function2<RenderLivingBase, Object[], Object> handleRotationFloat = MethodHandleHelper.wrapperForMethod(RenderLivingBase.class, new String[]{"handleRotationFloat", "func_77044_a", "b"}, EntityLivingBase.class, float.class);
	private Function2<RenderLivingBase, Object[], Object> applyRotations = MethodHandleHelper.wrapperForMethod(RenderLivingBase.class, new String[]{"applyRotations", "func_77043_a", "a"}, EntityLivingBase.class, float.class, float.class, float.class);
	private Function2<RenderLivingBase, Object[], Object> renderModel = MethodHandleHelper.wrapperForMethod(RenderLivingBase.class, new String[]{"renderModel", "func_77036_a", "a"}, EntityLivingBase.class, float.class, float.class, float.class, float.class, float.class, float.class);
	private Function2<RenderLivingBase, Object[], Object> renderLayers = MethodHandleHelper.wrapperForMethod(RenderLivingBase.class, new String[]{"renderLayers", "func_177093_a", "a"}, EntityLivingBase.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class);
	private Function2<RenderLivingBase, Object[], Object> setDoRenderBrightness = MethodHandleHelper.wrapperForMethod(RenderLivingBase.class, new String[]{"setDoRenderBrightness", "func_177090_c", "d"}, EntityLivingBase.class, float.class);
	private Function2<RenderLivingBase, Object[], Object> unsetBrightness = MethodHandleHelper.wrapperForMethod(RenderLivingBase.class, new String[]{"unsetBrightness", "func_177091_f", "g"});
	private Function2<RenderLivingBase, Object[], Object> setBrightness = MethodHandleHelper.wrapperForMethod(RenderLivingBase.class, new String[]{"setBrightness", "func_177092_a", "a"}, EntityLivingBase.class, float.class, boolean.class);
	private Function1<Render, Object> renderOutlines = MethodHandleHelper.wrapperForGetter(Render.class, "renderOutlines", "field_188301_f", "e");
	private Function1<RenderLivingBase, Object> renderMarker = MethodHandleHelper.wrapperForGetter(RenderLivingBase.class, "renderMarker", "field_188323_j", "i");
	private Function1<RenderLivingBase, Object> layerRenderers = MethodHandleHelper.wrapperForGetter(RenderLivingBase.class, "layerRenderers", "field_177097_h", "a");

	public PotionVanish() {
		super("vanish", false, 0xA9F3A9);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@NotNull
	@Override
	public List<ItemStack> getCurativeItems() {
		return new ArrayList<>();
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, @Nonnull AbstractAttributeMap attributeMapIn, int amplifier) {
		super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
		entityLivingBaseIn.world.playSound(null, entityLivingBaseIn.getPosition(), ModSounds.ETHEREAL_PASS_BY, SoundCategory.NEUTRAL, 1f, 1);

		if (!(entityLivingBaseIn instanceof EntityPlayer))
			PacketHandler.NETWORK.sendToAll(new PacketVanishPotion(entityLivingBaseIn.getEntityId(), 0, 100));
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, @Nonnull AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
		entityLivingBaseIn.world.playSound(null, entityLivingBaseIn.getPosition(), ModSounds.ETHEREAL_PASS_BY, SoundCategory.NEUTRAL, 1f, 1);

		if (!(entityLivingBaseIn instanceof EntityPlayer))
			PacketHandler.NETWORK.sendToAll(new PacketVanishPotion(entityLivingBaseIn.getEntityId()));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void doRenderOverride(RenderLivingEvent.Pre event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		EntityLivingBase entity = event.getEntity();

		// FIXME: 7/2/2017 FADE SHIT
		float x = 0;//MathHelper.clamp(3 / time, 0, 1);

		boolean iWalked = player.posX != player.prevPosX || player.posY != player.prevPosY || player.posZ != player.prevPosZ;
		boolean theyWalked = entity.posX != entity.prevPosX || entity.posY != entity.prevPosY || entity.posZ != entity.prevPosZ;
		boolean amRenderingMyself = event.getEntity().getEntityId() == player.getEntityId();

		boolean override = false;
		boolean hide = false;

		if (player.isPotionActive(this)) {
			override = true;
			if (iWalked) {
				if (amRenderingMyself) x = 1;
				else {
					hide = true;
					x = 0;
				}
			} else {
				if (amRenderingMyself) {
					hide = true;
					x = 0;
				} else x = 1;
			}
		}

		if (!amRenderingMyself && event.getEntity().isPotionActive(this)) {
			override = true;
			if (theyWalked) {
				x = 1;
			} else {
				hide = true;
				x = 0;
			}
		}

		if (!override) return;


		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1, 1, 1, x);
		//GlStateManager.disableCull();
		event.getRenderer().getMainModel().swingProgress = entity.swingProgress;
		boolean shouldSit = entity.isRiding() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
		event.getRenderer().getMainModel().isRiding = shouldSit;
		event.getRenderer().getMainModel().isChild = entity.isChild();

		try {
			float f = (float) interpolateRotation.invoke(event.getRenderer(), new Object[]{entity.prevRenderYawOffset, entity.renderYawOffset, ClientTickHandler.getPartialTicks()});
			float f1 = (float) interpolateRotation.invoke(event.getRenderer(), new Object[]{entity.prevRotationYawHead, entity.rotationYawHead, ClientTickHandler.getPartialTicks()});
			float f2 = f1 - f;

			if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
				f = (float) interpolateRotation.invoke(event.getRenderer(), new Object[]{entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, ClientTickHandler.getPartialTicks()});
				f2 = f1 - f;
				float f3 = MathHelper.wrapDegrees(f2);

				if (f3 < -85.0F) {
					f3 = -85.0F;
				}

				if (f3 >= 85.0F) {
					f3 = 85.0F;
				}

				f = f1 - f3;

				if (f3 * f3 > 2500.0F) {
					f += f3 * 0.2F;
				}
				f2 = f1 - f; // Forge: Fix MC-1207
			}

			float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * ClientTickHandler.getPartialTicks();
			renderLivingAt.invoke(event.getRenderer(), new Object[]{entity, event.getX(), event.getY(), event.getZ()});
			float f8 = (float) handleRotationFloat.invoke(event.getRenderer(), new Object[]{entity, ClientTickHandler.getPartialTicks()});
			applyRotations.invoke(event.getRenderer(), new Object[]{entity, f8, f, ClientTickHandler.getPartialTicks()});
			float f4 = event.getRenderer().prepareScale(entity, ClientTickHandler.getPartialTicks());
			float f5 = 0.0F;
			float f6 = 0.0F;

			if (!entity.isRiding()) {
				f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * ClientTickHandler.getPartialTicks();
				f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - ClientTickHandler.getPartialTicks());

				if (entity.isChild()) {
					f6 *= 3.0F;
				}

				if (f5 > 1.0F) {
					f5 = 1.0F;
				}
			}

			GlStateManager.enableAlpha();
			event.getRenderer().getMainModel().setLivingAnimations(entity, f6, f5, ClientTickHandler.getPartialTicks());
			event.getRenderer().getMainModel().setRotationAngles(f6, f5, f8, f2, f7, f4, entity);

			if ((boolean) renderOutlines.invoke(event.getRenderer())) {
				// TODO: boolean flag1 = this.setScoreTeamColor(entity);
				GlStateManager.enableColorMaterial();
				// TODO: GlStateManager.enableOutlineMode(this.getTeamColor(entity));

				if (!(boolean) renderMarker.invoke(event.getRenderer())) {
					renderModel.invoke(event.getRenderer(), new Object[]{entity, f6, f5, f8, f2, f7, f4});
				}

				if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
					renderLayers.invoke(event.getRenderer(), new Object[]{entity, f6, f5, ClientTickHandler.getPartialTicks(), f8, f2, f7, f4});
				}

				GlStateManager.disableOutlineMode();
				GlStateManager.disableColorMaterial();

				// TODO: if (flag1) {
				// TODO: 	this.unsetScoreTeamColor();
				// TODO: }
			} else {
				boolean flag = (boolean) setDoRenderBrightness.invoke(event.getRenderer(), new Object[]{entity, ClientTickHandler.getPartialTicks()});
				renderModel.invoke(event.getRenderer(), new Object[]{entity, f6, f5, f8, f2, f7, f4});

				if (flag) {
					unsetBrightness.invoke(event.getRenderer(), new Object[]{});
				}

				GlStateManager.depthMask(true);

				if (!hide)
					if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
						renderLayers.invoke(event.getRenderer(), new Object[]{entity, f6, f5, ClientTickHandler.getPartialTicks(), f8, f2, f7, f4});
					}
			}

			GlStateManager.disableRescaleNormal();
		} catch (Exception exception) {
			Wizardry.logger.error("Couldn\'t render entity", exception);
		}

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		event.setCanceled(true);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderShadowAndFire(EntityRenderShadowAndFireEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player.isPotionActive(this)) {
			boolean walked = player.posX != player.prevPosX || player.posY != player.prevPosY || player.posZ != player.prevPosZ;
			if (walked) event.override = true;
		}
		if (event.entity instanceof EntityLivingBase && ((EntityLivingBase) event.entity).isPotionActive(this)) {
			boolean walked = event.entity.posX != event.entity.prevPosX || event.entity.posY != event.entity.prevPosY || event.entity.posZ != event.entity.prevPosZ;
			if (!walked) event.override = true;
		}
	}
}