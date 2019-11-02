package me.alvin.duck;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        for (Duck duck : DuckPlugin.getInstance().spawnedDucks) {
            if (duck.isEntityPartOf(event.getEntity())) {
                if (!duck.isRemoved()) duck.remove();
                event.getDrops().clear();
            }
        }
    }
}