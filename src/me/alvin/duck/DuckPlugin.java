package me.alvin.duck;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class DuckPlugin extends JavaPlugin {

    private HashMap<String, String> messageData = new HashMap<>();
    public List<Duck> spawnedDucks = new ArrayList<>();

    /**
    * As there is two male heads the "Mallard Duck (female)" head is listed twice to make
    * it a 50/50 chance for a female or male duck.
    */
    public final String[] duckHeads = {
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFjNWI5NzgyNjNlMGI4OTU4ZGQ4NmY3NzBhOWYzM2UwNmNlN2I3OGQyNmIzM2QyNWY4ODIyMWY2YThlYjU1ZSJ9fX0=", // Mallard Duck (female)
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFjNWI5NzgyNjNlMGI4OTU4ZGQ4NmY3NzBhOWYzM2UwNmNlN2I3OGQyNmIzM2QyNWY4ODIyMWY2YThlYjU1ZSJ9fX0=", // Mallard Duck (female)
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Y5ZTgxYmNiYmFiMmU3M2IxMzE4NDgzYjA2NjJiNDBiMWU5NWU5ZDM4MTkxNWQ0NjMzZGY1NjdiYTU0MzIifX19", // Mallard Duck (male)
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmU2ODY0NzAxMWZmMjI0NzlmYTgzYTM4YzI0N2U5NWFiZmY2ZGUyMTJhMzE3NjAzYjg5MTVkYzg0MDNhOTU0YSJ9fX0=" // Mallard Duck (male)
    };

    private static DuckPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        this.getCommand("duck").setExecutor(new CommandDuck());

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // config.addDefault("config", true);
        // config.options().copyDefaults(true);
        // saveConfig();

        File messages = new File(getDataFolder() + File.separator + "messages.yml");
        if (!messages.exists()) {
            try {
                boolean fileCreated = messages.createNewFile();
                if (fileCreated) getLogger().info("The messages.yml was successfully created");
                else getLogger().severe("The messages.yml was not created");
            } catch (Exception e) {
                getLogger().severe("Failed to make messages.yml!");
                e.printStackTrace();
            }
        }
        FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messages);

        addDefaultMessage(messagesConfig, "command.reload", "The config and messages have been reloaded");
        addDefaultMessage(messagesConfig, "command.duck.spawn.neutral", "A neutral duck has been spawned");
        addDefaultMessage(messagesConfig, "command.duck.spawn.scared", "A scared duck has been spawned");
        addDefaultMessage(messagesConfig, "command.duck.spawn.hostile", "A hostile duck has been spawned");
        addDefaultMessage(messagesConfig, "command.duck.spawn.unknown", "§cUnknown duck type! Choose between neutral, scared and hostile");
        addDefaultMessage(messagesConfig, "command.noPermission", "§cYou do not have permission to use this command!");

        reloadMessages();


        // This is probably not the best way to do this, but I am teleporting the
        // Duck armorstand to an invisible zombie every tick.
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Duck duck : DuckPlugin.this.spawnedDucks) {
                duck.tick();
            }
        }, 0, 1); // interval is in ticks

        getLogger().info("Duck has been enabled");
    }

    @Override
    public void onDisable() {
        for (Duck duck : DuckPlugin.this.spawnedDucks) {
            duck.remove();
        }

        getLogger().info("Duck has been disabled");
    }




    public static DuckPlugin getInstance() {
        return instance;
    }

    private void addDefaultMessage(FileConfiguration config, String name, String message) {
        if (!config.isSet(name)) {
            config.set(name, message);
        }
    }

    /**
     * Gets the message with the provided id from the messages.yml
     *
     * @param id The id of the message you want to get
     * @return The message with the specified id. If no message with that id is found the id is returned.
     */
    public String getMessage(String id) {
        return this.messageData.getOrDefault(id, id);
    }

    /**
     * Reloads the messages used by the Duck plugin
     */
    public void reloadMessages() {
        File messages = new File(getDataFolder() + File.separator + "messages.yml");
        FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messages);

        for (String message : messagesConfig.getConfigurationSection("").getKeys(false)) {
            this.messageData.put(message, messagesConfig.getString(message));
        }
    }

    /**
     * Spawns a duck at the specific location
     *
     * @return The spawned duck.
     */
    public Duck spawnDuck(Location location, DuckType type) {
        return new Duck(location, type);
    }
}