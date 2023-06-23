package net.hectus.hectusblockbattles.util;

import net.hectus.hectusblockbattles.match.Match;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class BBPlayer {
    public final Player player;
    private int luck = 10;
    private boolean highGround = false;
    private boolean defended = false;
    private boolean movement = false;
    private boolean attacked = false;
    private Match.PlayerState state;
    private int dieCounter = -3;
    private int extraTurns = 0;

    @Contract(pure = true)
    public BBPlayer(Player player) {
        this.player = player;
    }

    public int luck() {
        return luck;
    }
    public void resetLuck() {
        luck = 10;
    }
    public void addLuck(int luck) {
        this.luck += luck;
    }
    public void removeLuck(int luck) {
        this.luck -= luck;
    }

    public boolean hasHighGround() {
        return highGround;
    }
    public void setHighGround(boolean highGround) {
        this.highGround = highGround;
    }

    public boolean isDefended() {
        return defended;
    }
    public void setDefended(boolean defended) {
        this.defended = defended;
    }

    public boolean canMove() {
        return movement;
    }
    public void setMovement(boolean movement) {
        this.movement = movement;
    }

    public Match.PlayerState getState() {
        return state;
    }
    public void setState(Match.PlayerState state) {
        this.state = state;
    }

    public boolean isAttacked() {
        return attacked;
    }
    public boolean hasToCounter() {
        return attacked;
    }
    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }

    public void startDieCounter(int turns) {
        dieCounter = turns;
    }

    public void addExtraTurns(int turns) {
        extraTurns += turns;
    }
    public void removeExtraTurns(int turns) {
        extraTurns -= turns;
    }
    public int getExtraTurns() {
        return extraTurns;
    }

    public void count() {
        dieCounter--;
        if (dieCounter == 0) {
            if (this == Match.getPlacer()) {
                Match.lose();
            } else {
                Match.win();
            }
        }
    }

    //==========================================//
    // The next code is basically just from the //
    // Bukkit Player but so it's easier to use. //
    //==========================================//

    public void sendMessage(@NotNull String msg) {
        player.sendMessage(Component.text(msg));
    }

    public void sendActionBar(@NotNull String msg) {
        player.sendActionBar(Component.text(msg));
    }

    public void showTitle(@NotNull String ttl, @NotNull String sttl, @Nullable Title.Times tms) {
        if (tms == null) player.showTitle(Title.title(Component.text(ttl), Component.text(sttl)));
        else player.showTitle(Title.title(Component.text(ttl), Component.text(sttl), tms));
    }

    public void clearTitle() {
        player.clearTitle();
    }

    public void playSound(@NotNull Sound snd) {
        player.playSound(snd);
    }

    public void playSound(@NotNull Sound sound, double x, double y, double z) {
        player.playSound(sound, x, y, z);
    }
}
