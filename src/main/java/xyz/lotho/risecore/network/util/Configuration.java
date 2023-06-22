package xyz.lotho.risecore.network.util;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.lotho.risecore.network.RiseCore;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;

@Getter
public class Configuration {
    private final RiseCore riseCore;

    private YamlConfiguration configurationFile;
    private File configuration;

    private final String configurationName;

    public Configuration(RiseCore riseCore, String configurationName) {
        this.riseCore = riseCore;
        this.configurationName = configurationName;

        load();
    }

    public boolean load() {
        if (!getRiseCore().getDataFolder().exists()) {
            getRiseCore().getDataFolder().mkdirs();
        }

        configuration = new File(getRiseCore().getDataFolder(), getConfigurationName() + ".yml");

        if (!getConfiguration().exists()) {
            try (InputStream stream = getRiseCore().getResource(getConfigurationName() + ".yml")) {
                if (stream != null) {
                    Files.copy(stream, getConfiguration().toPath());
                }
            } catch (IOException e) {
                getRiseCore().getLogger().log(Level.WARNING, "Unable to copy or create " + getConfigurationName() + ".yml");
                e.printStackTrace();
                return false;
            }
        }

        configurationFile = YamlConfiguration.loadConfiguration(getConfiguration());
        return true;
    }

    public void save() {
        try {
            getConfigurationFile().save(getConfiguration());
        } catch (IOException e) {
            getRiseCore().getLogger().log(Level.WARNING, "Unable to save " + getConfigurationName() + ".yml");
        }
    }

    public YamlConfiguration get() {
        return getConfigurationFile();
    }
}
