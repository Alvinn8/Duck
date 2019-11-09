package me.alvin.duck;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import java.util.ArrayList;
import java.util.List;

public class EventListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        List<Duck> copy = new ArrayList<>(DuckPlugin.getInstance().spawnedDucks);
        for (Duck duck : copy) {
            if (duck.isEntityPartOf(event.getEntity())) {
                if (!duck.isRemoved()) duck.remove();
                event.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void a(PlayerArmorStandManipulateEvent event) {
        for (Duck duck : DuckPlugin.getInstance().spawnedDucks) {
            if (duck.isEntityPartOf(event.getRightClicked())) {
                event.setCancelled(true);
            }
        }
    }
}