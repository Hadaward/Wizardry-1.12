package me.lordsaad.wizardry.spells.modules.shapes;

import me.lordsaad.wizardry.api.modules.Module;
import me.lordsaad.wizardry.api.modules.attribute.Attribute;
import me.lordsaad.wizardry.spells.modules.ModuleType;
import net.minecraft.nbt.NBTTagCompound;

public class ModuleCone extends Module {
    public ModuleCone() {
    	attributes.addAttribute(Attribute.DISTANCE);
    	attributes.addAttribute(Attribute.SCATTER);
    }

    @Override
    public ModuleType getType() {
        return ModuleType.SHAPE;
    }

    @Override
    public NBTTagCompound getModuleData() {
        return null;
    }
}