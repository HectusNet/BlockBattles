package net.hectus.hectusblockbattles;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class IngameShop implements Listener {
    private static ItemStack glassWall = new ItemStack(Material.GLASS, 1);
    private static int[] backgroundSlots = {0,1,2,3,5,6,7,8,9,10,16,17,18,19,25,26,27,28,34,35,36,37,43,44,45,46,47,48,49,50,51,52,53};
    private static int money = 40;
    private static boolean shopState = true;

    public static void initialize() {
        createItems();
    }

    public static void createItems() {
        ItemMeta meta = glassWall.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        glassWall.setItemMeta(meta);
    }

    public static void displayShop(Player player) {
        Inventory shop = Bukkit.createInventory(player, 9*6, "SHOP");
        for (int slot : backgroundSlots) {
            shop.setItem(slot, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
        }
        shop.setItem(4, new ItemStack(Material.GOLD_INGOT, money));
        shop.addItem(glassWall);
        player.openInventory(shop);
    }

    public static void onItemClicked(Player player, ItemStack item) {
        if ((item).equals(glassWall) && (money >= 2)) {
            money -= 2;
            player.getInventory().addItem(new ItemStack(Material.GLASS, 6));
            shopState = false;
            displayShop(player);
            shopState = true;
        }
    }

    public static void onShopClose() {
        if (shopState) {
            money = 40;
        }
    }


}
