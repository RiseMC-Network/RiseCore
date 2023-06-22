package xyz.lotho.risecore.network.util;

import org.bukkit.Bukkit;
import xyz.lotho.risecore.network.RiseCore;

public class Tasks {

    // run bukkit scheduler async in static method "runAsync"
    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(RiseCore.getInstance(), runnable);
    }

    // run bukkit scheduler runTaskLater async in static method "runLaterAsync"
    public static void runLaterAsync(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(RiseCore.getInstance(), runnable, delay);
    }

    // run bukkit scheduler runTaskLater sync in static method "runLater"
    public static void runLater(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(RiseCore.getInstance(), runnable, delay);
    }

    // run bukkit scheduler that will async run consistently
    public static void runAsyncTimer(Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(RiseCore.getInstance(), runnable, delay, period);
    }

    // run bukkit scheduler sync
    public static void runSync(Runnable runnable) {
        Bukkit.getScheduler().runTask(RiseCore.getInstance(), runnable);
    }

    // run bukkit timer sync
    public static void runSyncTimer(Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(RiseCore.getInstance(), runnable, delay, period);
    }

}
