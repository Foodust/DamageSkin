package org.foodust.damageSkin.BaseModule;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.foodust.damageSkin.Data.AnimateData;
import org.joml.Vector3d;


public class DisplayModule {

    public ItemDisplay makeItemDisplay(Entity entity, Location location, Material material, Double size) {
        ItemDisplay itemDisplay = entity.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(new ItemStack(material));
        Transformation transformation = itemDisplay.getTransformation();
        transformation.getScale().set(size);
        itemDisplay.setTransformation(transformation);
        AnimateData.ENTITIES.add(itemDisplay);
        return itemDisplay;
    }

    public ItemDisplay makeItemDisplay(Entity entity, Location location, ItemStack itemStack, Double size, Display.Billboard billboard) {
        ItemDisplay itemDisplay = entity.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(itemStack);
        itemDisplay.setBillboard(billboard);
        Transformation transformation = itemDisplay.getTransformation();
        transformation.getScale().set(size);
        itemDisplay.setTransformation(transformation);
        AnimateData.ENTITIES.add(itemDisplay);
        return itemDisplay;
    }

    public ItemDisplay makeItemDisplay(Entity entity, Location location, ItemStack itemStack, Double size) {
        ItemDisplay itemDisplay = entity.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(itemStack);
        Transformation transformation = itemDisplay.getTransformation();
        transformation.getScale().set(size);
        itemDisplay.setTransformation(transformation);
        AnimateData.ENTITIES.add(itemDisplay);
        return itemDisplay;
    }

    public ItemDisplay makeItemDisplay(Entity entity, Location location, ItemStack itemStack, float x, float y, float z) {
        ItemDisplay itemDisplay = entity.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(itemStack);
        Transformation transformation = itemDisplay.getTransformation();
        transformation.getScale().set(x, y, z);
        itemDisplay.setTransformation(transformation);
        AnimateData.ENTITIES.add(itemDisplay);
        return itemDisplay;
    }

    public ItemDisplay makeItemDisplay(Entity entity, Location location, ItemStack itemStack, Vector3d size) {
        ItemDisplay itemDisplay = entity.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(itemStack);
        Transformation transformation = itemDisplay.getTransformation();
        transformation.getScale().set(size);
        itemDisplay.setTransformation(transformation);
        AnimateData.ENTITIES.add(itemDisplay);
        return itemDisplay;
    }

    public ItemDisplay makeItemDisplay(Entity entity, Location location, ItemStack itemStack, Display.Billboard billboard, Vector3d size) {
        ItemDisplay itemDisplay = entity.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(itemStack);
        itemDisplay.setBillboard(billboard);
        Transformation transformation = itemDisplay.getTransformation();
        transformation.getScale().set(size);
        itemDisplay.setTransformation(transformation);
        AnimateData.ENTITIES.add(itemDisplay);
        return itemDisplay;
    }

    public BlockDisplay makeBlockDisplay(Entity entity, Location location, Material material, Double size) {
        BlockDisplay blockDisplay = entity.getWorld().spawn(location, BlockDisplay.class);
        blockDisplay.setBlock(material.createBlockData());
        Transformation transformation = blockDisplay.getTransformation();
        transformation.getScale().set(size);
        blockDisplay.setTransformation(transformation);
        AnimateData.ENTITIES.add(blockDisplay);
        return blockDisplay;
    }

    public TextDisplay makeTextDisplay(Entity entity, Location location, String text, Double size) {
        TextDisplay textDisplay = entity.getWorld().spawn(location, TextDisplay.class);
        textDisplay.setText(text);
        Transformation transformation = textDisplay.getTransformation();
        transformation.getScale().set(size);
        textDisplay.setTransformation(transformation);
        AnimateData.ENTITIES.add(textDisplay);
        return textDisplay;
    }

    public TextDisplay makeTextDisplay(Entity entity, Location location, String text, Double size, Display.Billboard billboard) {
        TextDisplay textDisplay = entity.getWorld().spawn(location, TextDisplay.class);
        textDisplay.setText(text);
        textDisplay.setBillboard(billboard);
        Transformation transformation = textDisplay.getTransformation();
        transformation.getScale().set(size);
        textDisplay.setTransformation(transformation);
        AnimateData.ENTITIES.add(textDisplay);
        return textDisplay;
    }

    public TextDisplay makeTextDisplay(Entity entity, Location location, String text, Vector size, Display.Billboard billboard) {
        TextDisplay textDisplay = entity.getWorld().spawn(location, TextDisplay.class);
        textDisplay.setText(text);
        textDisplay.setBillboard(billboard);
        Transformation transformation = textDisplay.getTransformation();
        transformation.getScale().set(size.getX(), size.getY(), size.getZ());
        textDisplay.setTransformation(transformation);
        AnimateData.ENTITIES.add(textDisplay);
        return textDisplay;
    }
}
