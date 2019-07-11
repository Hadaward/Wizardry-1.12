package com.teamwizardry.wizardry.common.core.fairytasks;

import com.teamwizardry.wizardry.api.entity.fairy.fairytasks.FairyTask;
import com.teamwizardry.wizardry.common.entity.EntityFairy;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class FairyTaskMove extends FairyTask {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void onStart(EntityFairy fairy) {

	}

	@Override
	public void onTick(EntityFairy fairy) {
		if (fairy.isMoving()) {
			EntityFairy attached = getAttachedFairy(fairy);
			if (attached != null && attached.fairyTaskController.getTask().getPriority() < getPriority()) {

			}
			return;
		}

		double fairyToTarget = fairy.getPositionVector().distanceTo(new Vec3d(fairy.getDataTargetBlock()).add(0.5, 0.5, 0.5));

		if (fairyToTarget > 0.25) {
			fairy.moveTo(fairy.getDataTargetBlock());
		} else fairy.moveTo(fairy.getDataOriginBlock());
	}

	@Override
	public void onEnd(EntityFairy fairy) {

	}

	@Override
	public void onConfigure(EntityFairy fairy, @Nullable BlockPos targetBlock, @Nullable Entity targetEntity, Vec3d lookVec) {

	}
}
