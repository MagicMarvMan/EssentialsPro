package de.marvnet.minecraft.essentialspro.manager

import de.marvnet.minecraft.essentialspro.main.EssentialsPro
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.lang.Exception

class ConfigManager {

    var configFile: File? = null
    var config: YamlConfiguration? = null

    fun save() {
        config!!.save(configFile)
    }

    fun get(key: String): Any {
        var ret = "";
        try {
            ret = config!!.get(key) as String
        } catch(e: Exception) {
            return "§cCould not load message from config file. Please look at the Server's console for more details."
        }
        if(ret != null) {
            return config!!.get(key)
        } else {
            return "§cCould not load message from config file. Please look at the Server's console for more details."
        }
    }

    fun getString(key: String): String {
        return get(key) as String
    }

    fun getDouble(key: String): Double {
        return get(key) as Double
    }

    fun getFloat(key: String): Float {
        return get(key) as Float
    }

    fun getBoolean(key: String): Boolean {
        return get(key) as Boolean
    }

    fun getMessage(messageID: String): String {
        return (get("messages." + messageID.replace(" ", "_")) as String).replace("%prefix%", EssentialsPro.getPrefix())
    }

    fun setLocation(name: String, location: Location, category: String = "warps") {
        set("$category.$name.X", location.x, false)
        set("$category.$name.Y", location.y, false)
        set("$category.$name.Z", location.z, false)
        set("$category.$name.World", location.world.name, false)
        set("$category.$name.Pitch", location.pitch, false)
        set("$category.$name.Yaw", location.yaw, false)
        save()
    }

    fun getLocation(name: String, category: String = "warps"): Location {
        val loc = Location(Bukkit.getWorld(getString("$category.$name.World")),
                getDouble("$category.$name.X"),
                getDouble("$category.$name.Y"),
                getDouble("$category.$name.Z"))
        loc.pitch = getFloat("$category.$name.Pitch")
        loc.yaw = getFloat("$category.$name.Yaw")
        return loc
    }

    fun set(key: String, value: Any, autosave: Boolean = true) {
        config!!.set(key, value)
        if(autosave) {
            save()
        }
    }

    fun initFiles() {
        val folder = File("plugins//EssentialsPro//players")
        if(!folder.exists()) {
            folder.mkdirs()
        }

        configFile = File("plugins//EssentialsPro//config.yml")
        var fileFilled = true
        if(!configFile!!.exists()) {
            configFile!!.createNewFile()
            fileFilled = false
        }
        config = YamlConfiguration.loadConfiguration(configFile)
        if(!fileFilled) {
            set("messages.Plugin_Enabled", "%prefix% §aThe plugin has been enabled!", false)
            set("messages.Plugin_Disabled", "%prefix% §aThe plugin has been disabled!", false)
            set("messages.Flying_Enabled", "§6Flying mode has been enabled.", false)
            set("messages.Flying_Disabled", "§6Flying mode has been disabled.", false)
            set("messages.Broadcast", "§4[§aBroadcast§4]§f %message%", false)
            set("messages.Flying_Other_Toggled", "§6The fly-mode for %name% has been toggled!", false)
            set("messages.Gamemode_Survival", "§6Your gamemode has been set to Survival.", false)
            set("messages.Gamemode_Creative", "§6Your gamemode has been set to Creative.", false)
            set("messages.Gamemode_Adventure", "§6Your gamemode has been set to Adventure.", false)
            set("messages.Gamemode_Spectator", "§6Your gamemode has been set to Spectator.", false)
            set("messages.Gamemode_Others", "§6The gamemode for %name% has been set to %gamemode%.", false)
            set("messages.Error_No_Permission", "§cYou don't have the permission to execute this command!", false)
            set("messages.Error_Only_Player", "§cOnly players can execute this command!", false)
            set("messages.Error_Unknown_Player", "§cYou specified an unknown player!", false)
            set("messages.Error_Unknown_Arguments", "§cUnknown number of arguments!", false)
            set("messages.Unknown_Warp", "§cUnknown warp point!", false)
            set("messages.Warped", "§6You have been warped to %point%.", false)
            set("messages.Warped_Other", "§6%name% has been warped to %point%.", false)

            save()
        }
    }

    fun init() {
        initFiles()
    }
}