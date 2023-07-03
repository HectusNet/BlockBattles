package net.hectus.hectusblockbattles.events;

import net.hectus.hectusblockbattles.InGameShop;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.util.BBPlayer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InGameShopEvents implements Listener {
    @EventHandler
    public static void onInventoryClick(@NotNull InventoryClickEvent event) {
        if (PlainTextComponentSerializer.plainText().serialize(event.getView().title()).toLowerCase().contains("shop")) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item != null) {
                Material mat = item.getType();
                String name = PlainTextComponentSerializer.plainText().serialize(item.displayName());

                Player player = (Player) event.getWhoClicked();

                if (mat != Material.LIGHT_GRAY_STAINED_GLASS_PANE && mat != Material.GRAY_STAINED_GLASS_PANE && mat != Material.GOLD_INGOT && mat != Material.WHITE_CONCRETE) {
                    InGameShop.onItemClicked(player, item, Objects.requireNonNull(event.getClickedInventory()));
                } else if (mat == Material.WHITE_CONCRETE) {
                    InGameShop.HotBar hotBar = (Match.getPlayer(player).getState() == Match.PlayerState.SHOP_NORMAL ? InGameShop.HotBar.NORMAL : InGameShop.HotBar.OVERTIME);
                    InGameShop.displayShop(player, hotBar, Objects.requireNonNull(Objects.requireNonNull(event.getClickedInventory()).getItem(4)).getAmount(), name.toLowerCase().contains("last") ? 1 : 2);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        if (!Match.shopPhase) {
            return;
        }
        if (event.getReason() == InventoryCloseEvent.Reason.OPEN_NEW) {
            return;
        }
        if (Match.getPlayer((Player) event.getPlayer()).getState() != Match.PlayerState.SHOP_OVERTIME) {
            return;
        }

        BBPlayer player = Match.getPlayer((Player) event.getPlayer());
        BBPlayer opponent = Match.getOpposite(player);
        if (player == null || opponent == null) return;

        if (player.getState() == Match.PlayerState.SHOP_NORMAL) {
            InGameShop.displayShop(player.player, InGameShop.HotBar.OVERTIME);
        } else if (player.getState() == Match.PlayerState.SHOP_OVERTIME) {
            if (opponent.getState() == Match.PlayerState.SHOP_OVERTIME_FINISHED) {
                Match.shopDone();
            } else {
                player.setState(Match.PlayerState.SHOP_OVERTIME_FINISHED);
            }
        }
    }
}
