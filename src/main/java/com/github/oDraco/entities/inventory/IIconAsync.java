package com.github.oDraco.entities.inventory;

import com.github.oDraco.DracoUtils;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

public interface IIconAsync extends IIcon {

    CompletableFuture<ItemStack> getIconAsync();

    @Override
    default ItemStack getIcon() {
        return DracoUtils.getDefaultItems().get("loading");
    }
}
