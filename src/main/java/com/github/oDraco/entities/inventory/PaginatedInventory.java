package com.github.oDraco.entities.inventory;

import com.github.oDraco.DracoUtils;
import com.github.oDraco.util.ItemUtils;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class PaginatedInventory extends SimpleInventory {

    @Getter
    protected final ArrayList<IIcon> icons = new ArrayList<>();

    @Getter
    private int currentPage = 0;
    @Getter
    private int maxPages = 1;
    private int perPage;

    @Getter
    private boolean fancy = false;

    public PaginatedInventory(String title, int size) {
        super(title, size);
        if (size <= 9)
            throw new IllegalArgumentException("Size can't be equal or less than 9");
        perPage = inv.getSize() - 9;
    }

    @Deprecated
    public PaginatedInventory(Inventory inventory) {
        super(inventory);
        if (inventory.getSize() <= 9)
            throw new IllegalArgumentException("Size can't be equal or less than 9");
        perPage = inv.getSize() - 9;
    }

    public void addIcon(IIcon icon) {
        icons.add(icon);
        updateMaxPages();
    }

    public void removeIcon(IIcon icon) {
        icons.remove(icon);
        updateMaxPages();
    }

    public void setFancy(boolean fancy) {
        if (fancy && inv.getSize() < 36)
            throw new IllegalStateException("Fancy inventories need at least a size of 36");
        this.fancy = fancy;
        perPage = fancy ? (inv.getSize() / 9 - 3) * 7 : inv.getSize() - 9;
    }

    public void setCurrentPage(int newPage) {
        if (newPage > getMaxPages())
            throw new IllegalArgumentException("New page can't be greater than the max of pages. (New Page: " + newPage + ", Max: " + maxPages + ")");
        if (newPage <= 0)
            throw new IllegalArgumentException("New page can't be equal or less than zero");

        // Clean current icons
        clear();

        // Put the fillers icons
        int start = inv.getSize() - 9;
        final IIcon filler = new BasicIconImpl(DracoUtils.getDefaultItems().get("filler"));
        for (int i = 0; i < 9; i++) {
            setItem(start + i, filler);
        }

        // Put the new icons
        int startIndex = perPage * (newPage - 1); // Icon index
        int invIndex, addedItems = 0;


        if (!isFancy()) { // I think that is best to use 2 separate FORs than add another if in one?
            invIndex = 0;
            for (IIcon icon : icons) {
                if (startIndex > 0) {
                    startIndex--;
                    continue;
                }
                if (addedItems >= perPage)
                    break;
                setItem(invIndex, icon);
                invIndex++;
                addedItems++;
            }
        } else {
            invIndex = 10;
            int row = 0;
            for (IIcon icon : icons) {
                if (startIndex > 0) {
                    startIndex--;
                    continue;
                }
                if (addedItems >= perPage)
                    break;
                setItem(invIndex + row * 9, icon);
                addedItems++;
                invIndex++;
                if (addedItems % 7 == 0) {
                    row++;
                    invIndex -= 7;
                }
            }
        }

        // Update info
        currentPage = newPage;
        updatePageInfo();
    }

    @Override
    public void updateInventory() {
        setCurrentPage(getCurrentPage());
    }

    private void updatePageInfo() {
        HashMap<String, ItemStack> items = DracoUtils.getDefaultItems();

        int infoSlot = inv.getSize() - 5;

        HashMap<String, String> replaces = new HashMap<>();
        replaces.put("{current}", String.valueOf(getCurrentPage()));
        replaces.put("{max}", String.valueOf(getMaxPages()));

        setItem(infoSlot, new BasicIconImpl(ItemUtils.replaceLore(items.get("pageInfo"), replaces)));

        if (currentPage > 1)
            setItem(infoSlot - 2, new PageChangeIconImpl(this, items.get("backPage"), -1));

        if (currentPage < maxPages)
            setItem(infoSlot + 2, new PageChangeIconImpl(this, items.get("nextPage"), +1));
    }

    private void updateMaxPages() {
        maxPages = (int) Math.ceil((double) icons.size() / perPage);
        if (maxPages <= 0)
            maxPages = 1;
    }
}
