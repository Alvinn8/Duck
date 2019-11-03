package me.alvin.duck;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.UUID;

public class Duck {
    private LivingEntity entity;
    private ArmorStand armorStand;
    private boolean removed;

    public Duck(Location location, DuckType type) {
        DuckPlugin.getInstance().spawnedDucks.add(this);

        this.entity = (LivingEntity) location.getWorld().spawnEntity(location, type.getEntityType());
        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, false, false));
        this.entity.setSilent(true);

        this.armorStand = (ArmorStand) location.getWorld().spawnEntity(location.subtract(0, 1.5, 0), EntityType.ARMOR_STAND);
        this.armorStand.setVisible(false);

        String duckHead = DuckPlugin.getInstance().duckHeads[(int) Math.floor(Math.random() * DuckPlugin.getInstance().duckHeads.length)];

        // Make the head itemstack
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        // Make the fake Game Profile
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", duckHead));

        // Set the private field profile of the SkullMeta to the fake game profile
        try {
            Field profile = headMeta.getClass().getDeclaredField("profile");
            profile.setAccessible(true);
            profile.set(headMeta, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            DuckPlugin.getInstance().getLogger().severe("Failed to make custom head when spawning duck.");
            e.printStackTrace();
        }


        head.setItemMeta(headMeta);

        this.armorStand.setHelmet(head);
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public ArmorStand getArmorStand() {
        return this.armorStand;
    }

    /**
     * Triggered every tick. Teleports the armor stand to the zombie and down two blocks.
     */
    public void tick() {
        this.armorStand.teleport(this.entity.getLocation().subtract(0, 1.5, 0));
    }

    /**
     * Kills the entities handling and displaying the duck, and removes it from the spawnedDucks List in DuckPlugin.
     */
    public void remove() {
        this.entity.remove();
        this.armorStand.remove();
        DuckPlugin.getInstance().spawnedDucks.remove(this);
        this.removed = true;
    }

    /**
     * Whether you can kill the duck
     */
    public void setInvulnerable(boolean invulnerable) {
        this.entity.setInvulnerable(invulnerable);
    }

    /**
     * Checks if the provided entity is either the duck render entity, or the duck handler entity.
     */
    public boolean isEntityPartOf(Entity entity) {
        // Compares the uuids instead of just using ==. Incase one of the entities have unloaded and been
        // reloaded the entity instance will change but it's still the same entity. The uuid does not change
        return this.entity.getUniqueId().toString().equals(entity.getUniqueId().toString())
                || this.armorStand.getUniqueId().toString().equals(entity.getUniqueId().toString());
    }

    /**
     * Whether the duck has been removed using the Duck#remove method.
     */
    public boolean isRemoved() {
        return this.removed;
    }
}
