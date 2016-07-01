package com.teamwizardry.wizardry.common.spell.module.modifiers;

import com.teamwizardry.wizardry.api.module.Module;
import com.teamwizardry.wizardry.api.module.attribute.Attribute;
import com.teamwizardry.wizardry.api.module.attribute.AttributeMap;
import com.teamwizardry.wizardry.api.module.attribute.AttributeModifier;
import com.teamwizardry.wizardry.api.spell.IModifier;
import com.teamwizardry.wizardry.api.spell.ModuleType;

public class ModuleBurnOut extends Module implements IModifier {
    public ModuleBurnOut() {
        canHaveChildren = false;
    }

    @Override
    public ModuleType getType() {
        return ModuleType.MODIFIER;
    }

    @Override
    public String getDescription()
    {
    	return "Decreases the burnout of a spell shape or effect.";
    }
    
    @Override
    public void apply(AttributeMap map) {
        map.putModifier(Attribute.BURNOUT, new AttributeModifier(AttributeModifier.Operation.ADD, -10, AttributeModifier.Priority.LOWEST));
    }
}