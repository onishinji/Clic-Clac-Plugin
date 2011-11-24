package onishinji;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import me.graindcafe.gls.DefaultLanguage;
import me.graindcafe.gls.Language;
import onishinji.commands.BreakCCCommand;
import onishinji.commands.CreateCCCommand;
import onishinji.commands.EditCCCommand;
import onishinji.commands.EndCCCommand;
import onishinji.commands.InfoCCCommand;
import onishinji.commands.LinkCCCommand;
import onishinji.commands.LinkReverseCC;
import onishinji.commands.ListCCCommand;
import onishinji.commands.MoveCCCommand;
import onishinji.commands.NextCCCommand;
import onishinji.commands.RemoveCCCommand;
import onishinji.commands.TPCCCommand;
import onishinji.commands.UndoCommand;
import onishinji.listener.CacheCachePlayerBlockListener;
import onishinji.listener.CacheCachePlayerListener;
import onishinji.models.BlockManager;
import onishinji.models.ButtonCC;
import onishinji.models.MyLocation;
import onishinji.models.SLAPI;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class ClicClac extends JavaPlugin {

    private final CacheCachePlayerListener playerListener = new CacheCachePlayerListener(this);
    private final CacheCachePlayerBlockListener blockListiner = new CacheCachePlayerBlockListener(this);

    public HashMap<Player, StructureCC> playerCreateCommand = new HashMap<Player, StructureCC>();
    public HashMap<Player, StructureCC> playerActiveLink = new HashMap<Player, StructureCC>();
    public ArrayList<StructureCC> structuresLoaded = new ArrayList<StructureCC>();
    public PermissionHandler _permissions;
    private Logger log;
    private Language lang;

    @Override
    public void onDisable() {

        playerCreateCommand = new HashMap<Player, StructureCC>();
        playerActiveLink = new HashMap<Player, StructureCC>();
        structuresLoaded = new ArrayList<StructureCC>();
        _permissions = null;

        System.out.println(">>> Clic Clac ne marche plus");

    }

    @Override
    public void onEnable() {
        log = Logger.getLogger("Minecraft");
        
        _permissions = null;

        playerCreateCommand = new HashMap<Player, StructureCC>();
        playerActiveLink = new HashMap<Player, StructureCC>();
        structuresLoaded = new ArrayList<StructureCC>();

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListiner, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListiner, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BURN, blockListiner, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListiner, Priority.Normal, this);
        pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListiner, Priority.Normal, this);

        // Register our commands
        getCommand("cc-start").setExecutor(new CreateCCCommand(this));
        getCommand("cc-end").setExecutor(new EndCCCommand(this));
        getCommand("cc-undo").setExecutor(new UndoCommand(this));
        getCommand("cc-link").setExecutor(new LinkCCCommand(this));
        getCommand("cc-link-reverse").setExecutor(new LinkReverseCC(this));
        getCommand("cc-info").setExecutor(new InfoCCCommand(this));
        getCommand("cc-list").setExecutor(new ListCCCommand(this));
        getCommand("cc-move").setExecutor(new MoveCCCommand(this));
        getCommand("cc-tp").setExecutor(new TPCCCommand(this));
        getCommand("cc-start-edit").setExecutor(new EditCCCommand(this, "start"));
        getCommand("cc-end-edit").setExecutor(new EditCCCommand(this, "end"));

        getCommand("cc-remove").setExecutor(new RemoveCCCommand(this));
        getCommand("cc-next").setExecutor(new NextCCCommand(this)); 
        getCommand("cc-break").setExecutor(new BreakCCCommand(this));

        getCommand("cc-run").setExecutor(new RunCCCommand(this));

        // Creation répertoire
        File testInstall = new File("plugins/ClicClac");
        if (!testInstall.exists()) {
            testInstall.mkdir();
        }
        
        this.initConfig();
        this.initLanguage();
       
        // Lecture du répertoire
        try {
            File[] files = SLAPI.listFiles("plugins", "ClicClac");
            for (File currentFile : files) {
                if (currentFile.getName().contains(".bin")) {
                    ArrayList<StructureCC> test;
                    test = (ArrayList<StructureCC>) SLAPI.load(currentFile.getPath());

                    if (test.size() > 0) {
                        structuresLoaded = test;
                    }
                }
            }

            // traitement pour les maj
            for (StructureCC c : structuresLoaded) {
                for (int i = 0; i < c.boutons.size(); i++) {
                    if (c.boutons.get(i) instanceof MyLocation) {
                        MyLocation old = c.boutons.get(i);
                        ButtonCC newbutton = new ButtonCC(old.getWordLocation());
                        newbutton.setPositiv(true);
                        c.boutons.set(i, newbutton);
                    }
                }

                // Verrouille l'édition des structures
                c.isEditable = false;
                c.isAnimated = false;
                
                if(c.isBreakable == null) c.isBreakable = false;


                // Met les clic clac dans un groupe "Non classé" si pas de
                // groupe
                if (c.groupName == null || c.groupName == "") {
                    c.groupName = "NC";
                }

                // Met les clic clac dans le monde "world"
                if (c.worldName == null || c.worldName == "") {
                    c.worldName = "world";
                }

                // Met les clic clac dans le monde "world"
                if (c.playerName == null || c.playerName == "") {
                    c.playerName = "un humain (sans doute).";
                }

                if (c.steps == null) {
                    c.steps = new ArrayList<ArrayList<BlockManager>>();
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        init();

        System.out.println(">>> Clic Clac est en route !!");
    }

    private void initLanguage() {
 
        // i18n clic clac
        DefaultLanguage.setAuthor("Onishinji");
        DefaultLanguage.setName("defaultlang");
        DefaultLanguage.setVersion(Constants.LanguageFileVersion);
        DefaultLanguage.setLanguagesFolder(getDataFolder().getPath()+"/languages/");
        DefaultLanguage.setLocales(new HashMap<String, String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {

                put("cc.forbidden.command",                 ChatColor.RED + "[ClicClac] You do not have permission to perform that action!");
                put("cc.error.missingName",                 ChatColor.RED + "Erreur: Il faut nommer le clic clac");
                put("cc.error.unknowCC",                    ChatColor.RED +  "Désolé, ce clic clac n'existe pas");
                
                put("cc.create.firstInstruction",           ChatColor.YELLOW + "Clique sur le premier point du clic Clac, pour annuler retappe la même commande ou /cc-undo");
                put("cc.create.secondInstruction",          ChatColor.YELLOW + "Clique sur le deuxième point");
                put("cc.create.cancelByRepeatCreate",       ChatColor.YELLOW + "Tu as annulé ce Clic Clac en rettapant la commande, ne te trompe pas cette fois ci :)");
                put("cc.create.error.nameIsTakenInGroup",   ChatColor.YELLOW + "Désolé, un Clic Clac cache porte déjà ce nom dans le groupe %s, recommence");
                put("cc.create.end",                        ChatColor.GREEN + "Félicitation, le clic Clac '%s' a été crée dans le groupe '%s'), pour l'utiliser tappe '/cc-link %s %s' puis clique sur un interrupteur" );
                
                put("cc.create.cuboidHasBeenCreated.text1", ChatColor.YELLOW + "Merci, la zone du Clic Clac a été vidé, "+ChatColor.RED+"tappe /cc-end"+ChatColor.YELLOW+" pour terminer ou "+ChatColor.RED+"/cc-undo"+ChatColor.YELLOW);
                put("cc.create.cuboidHasBeenCreated.text2", ChatColor.YELLOW + "Si tu veux faire une animation, tappe "+ChatColor.RED+"/cc-next "+ChatColor.YELLOW+" pour passer à la prochaine étape");
                put("cc.create.cuboidHasBeenCreated.text3", ChatColor.YELLOW + "Veuillez maintenant construire la nouvelle forme à l'intérieur de la zone.");
                
                put("cc.create.nextSaved",                  ChatColor.YELLOW + "l'étape vient d'être sauvegardé, construit la nouvelle maintenant");
                
                
                put("cc.undo.done",                         ChatColor.GREEN + "Vous venez d'annuler la création d'un clic clac");
                
                put("cc.edit.start",                        ChatColor.YELLOW + "La zone clic clac est éditable, tappe '/cc-end-edit %s %s' pour terminer ");
                put("cc.edit.end",                          ChatColor.GREEN + "La zone clic clac n'est plus éditable et a été sauvegardé");
                put("cc.edit.errorccWasntEditable",  ChatColor.RED + "Cette zone clic clac n'était pas éditable ...");
                
                put("cc.link.save",                         ChatColor.GREEN + "Bloc interrupteur sauvegardé pour  '%s %s', Have Fun :)");
                put("cc.link.click",                        ChatColor.YELLOW + "Clique sur le bloc qui te servira d'interrupteur.");
                
                put("cc.remove.done",                       ChatColor.GREEN + "Suppression de la zone Clic Clac '%s %s' terminé.");
                
                put("cc.info.name",                         ChatColor.YELLOW + "Information de la Zone clic clac '%s' du groupe '%s'");
                put("cc.info.createdBy",                    ChatColor.YELLOW + "crée par %s dans le monde '%s'");
                put("cc.info.stats.default",                ChatColor.YELLOW +  "Elle contient %s blocs");
                put("cc.info.animated.steps",               ChatColor.YELLOW +  "Elle a %s états et est affiché dans son état %s");
                put("cc.info.animated.ready",               ChatColor.YELLOW +  "Elle peut être animé maintenant");
                put("cc.info.animated.notReady",            ChatColor.YELLOW +  "Elle ne peut pas être animé maintenant, dernière utilisation %s");
                put("cc.info.warning.simple",               ChatColor.YELLOW +   "!!! il y a %s blocs de %s ( %s dans l'état 1, %s dans l'état 2)");
                put("cc.info.warning.multi",                ChatColor.YELLOW +   "!!! il y a %s blocs de %s ( %s dans l'état 1, %s dans l'état %s)");
                
                
                put("cc.move.info",                         ChatColor.YELLOW +  "Il faut tapper /cc-move nomZoneCC nomAncienGroupe > nomNouveauGroupe");
                put("cc.move.done",                         ChatColor.GREEN +  "La zone clic clac a été déplace de '%s' à '%s' ");
                put("cc.move.error",                        ChatColor.RED + "On ne peut déplacer un clic clac dans un groupe ayant un clic clac du même nom");
                
                
                put("cc.info.break",                        ChatColor.YELLOW + "Elle a %s états et est affiché dans son état %s");
                put("cc.info.stats.dateLimited",            ChatColor.YELLOW + "Elle a un usage limité à une utilisation toutes les %s secondes");
                
                
                
                put("cc.settime.isNowLimited",              ChatColor.GREEN +  "Ce clicClac est désormais limité à une utilisation toutes les %s secondes");
                put("cc.settime.isNowRemoved",              ChatColor.GREEN + "Vous venez de supprimer la limitation temporelle de ce clic clac");
                put("cc.break.isNowActive",                 ChatColor.YELLOW + "Cette zone Clic Clac est cassable");
                put("cc.break.isNowProtected",              ChatColor.YELLOW + "Cette zone Clic Clac n'est plus cassable");
                
                
                put("File.DefaultLanguageFile","# This is your default language file \n# You should not edit it !\n# Create another language file (custom.yml) \n# and put 'Default: english' if your default language is english\n");
                put("File.LanguageFileComplete", "# Your language file is complete\n");
                put("File.TranslationsToDo", "# Translations to do in this language file\n");
                put("Warning.LanguageFileOutdated", "Your current language file is outdated");
                put("Warning.LanguageFileMissing", "The chosen language file is missing");
                put("Info.ChosenLanguage", "Choosen language : %s (default). Provided by : %s.");
            }
        });  
        
        String language = getConfiguration().getString("PluginMode.Language", "defaultlang");
        lang = Language.init(this.log, language); 
        
    }

    public String getLocale(String key) {
        return lang.get(key);
    }
    
    private void initConfig() {
        getConfiguration().setHeader("# Clic Clac Config File Version " + Constants.ConfigFileVersion + " \n");
        HashMap<String, Object> DefaultConfiguration = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            { 
                put("PluginMode.Language", "defaultlang");

            }
        };
        for (String key : getConfiguration().getAll().keySet()) {
            DefaultConfiguration.remove(key);
        }
        
        // Add missings keys
        for (Entry<String, Object> e : DefaultConfiguration.entrySet()) {
            getConfiguration().setProperty(e.getKey(), e.getValue());
        }
        // Create the file if it doesn't exist
        getConfiguration().save();
    }

    
    public boolean has(Player player, String permissionsNode) {
        return _permissions.has(player, permissionsNode);
    }

    public boolean hasGuard(Player player, String permissionsNode) {
        if (_permissions != null && !this.has(player, permissionsNode)) {
            player.sendMessage(this.getLocale("cc.forbidden.command"));
            return false;
        } else {
            return true;
        }
    }

    public boolean init() {
        Plugin permissions = this.getServer().getPluginManager().getPlugin("Permissions");

        if (permissions == null) {
            return false;
        }

        _permissions = ((Permissions) permissions).getHandler();

        return true;
    }

    public boolean structureExist(String name, String groupName) {
        for (StructureCC struc : structuresLoaded) {
            if (struc.name.equalsIgnoreCase(name) && struc.groupName.equalsIgnoreCase(groupName)) {
                return true;
            }
        }

        return false;
    }

    public StructureCC getStructure(String name, String groupName) {
        for (StructureCC struc : structuresLoaded) {
            if (struc.name.equalsIgnoreCase(name) && struc.groupName.equalsIgnoreCase(groupName)) {
                return struc;
            }
        }

        return null;
    }

    // ////////////////////////////////////////////////////////
    // CreateCC
    // ////////////////////////////////////////////////////////

    public void startStructure(Player player, String name, String groupName) {

        if (!this.structureExist(name, groupName)) {
            StructureCC cc = new StructureCC();
            cc.name = name;
            cc.groupName = groupName;
            cc.playerName = player.getName();
            cc.isEditable = true;

            cc.worldName = player.getWorld().getName();
            cc.currentState = 0;

            playerCreateCommand.put(player, cc);
        }
    }

    public boolean playerHasBeginStructure(Player player) {
        return playerCreateCommand.containsKey(player);
    }

    public void undoRemove(Player player) {
        playerCreateCommand.get(player).undoStart(player.getWorld());
    }

    public boolean playerHasBeginLink(Player player) {
        return playerActiveLink.containsKey(player);
    }

    public void endStructure(Player player) {
        // TODO Auto-generated method stub
        playerCreateCommand.remove(player);
    }

    public void saveStructure(StructureCC currentStructure) {
        currentStructure.isEditable = false;
        currentStructure.isAnimated = false;
        this.structuresLoaded.add(currentStructure);

        try {
            SLAPI.save(this.structuresLoaded, "plugins/ClicClac/structuresLoaded.bin");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void activeLink(String eventName, String groupName, Player player, boolean positif) {

        StructureCC test = this.getStructure(eventName, groupName);
        test.sens = positif;
        if (test != null) {
            playerActiveLink.put(player, test);
        }

    }

    public void modifyStructure(StructureCC currentStructure) {

        int pos = 0;
        for (int i = 0; i < structuresLoaded.size(); i++) {
            if (structuresLoaded.get(i).name.equals(currentStructure.name)) {
                pos = i;
            }
        }

        structuresLoaded.remove(pos);
        this.saveStructure(currentStructure);

    }

    public void onRedStoneChange(MyLocation buttonLocation, BlockRedstoneEvent event) {

        for (StructureCC structure : structuresLoaded) {
            for (ButtonCC structureB : structure.boutons) {
                if (structureB.getX() == buttonLocation.getX() && structureB.getY() == buttonLocation.getY() && structureB.getZ() == buttonLocation.getZ()) {

                    structure.computeNewState(event.getNewCurrent(), structureB);
                    structure.switchBlocs(event.getBlock().getWorld());

                }
            }
        }

    }

    public void removeStructure(String eventName, String groupName, Player player) {

        StructureCC toRemove = null;

        ArrayList<StructureCC> toRemoves = new ArrayList<StructureCC>();

        for (StructureCC struc : structuresLoaded) {
            if (struc.name.equalsIgnoreCase(eventName) && struc.groupName.equalsIgnoreCase(groupName)) {
                toRemove = struc;
                toRemoves.add(struc);

            }
        }

        for (StructureCC struc : toRemoves) {
            this.structuresLoaded.remove(struc);
        }

        if (toRemove != null) {
            try {
                SLAPI.save(this.structuresLoaded, "plugins/ClicClac/structuresLoaded.bin");

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public boolean hasBlockOnStructure(MyLocation myLocation) {

        for (StructureCC struc : structuresLoaded) {
            if (struc.isEditable == false) {
                for (BlockManager bloc : struc.blocsFinal) {
                    if (bloc.geMyLocation().getX() == myLocation.getX() && bloc.geMyLocation().getY() == myLocation.getY() && bloc.geMyLocation().getZ() == myLocation.getZ()) {
                        if(struc.isBreakable())
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public ArrayList<StructureCC> getStructureByGroupName(String groupName) {
        ArrayList<StructureCC> listeStructure = new ArrayList<StructureCC>();
        for (StructureCC structure : this.structuresLoaded) {
            if (structure.groupName.equalsIgnoreCase(groupName)) {
                listeStructure.add(structure);
            }
        }

        return listeStructure;
    }

    public String getGroupNameFromArgs(String[] args) {
        String groupName = "";
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                groupName = groupName + " " + args[i];
            }
            groupName = groupName.substring(1, groupName.length());
        } else {
            groupName = "NC";
        }

        return groupName;

    }

    public void animateCC(String eventName, String groupName, final World world, long delay) {

        final StructureCC test = this.getStructure(eventName, groupName);
 
        if (test.canBeAnimated()) {

            Timer t = new Timer();
            final ButtonCC b1 = new ButtonCC(new Location(world, 0, 0, 0));

            if (delay < 0)
                b1.setPositiv(false);

            if (b1.isPositiv()) {
                test.reinitMultiCC(world);
            } else {
                test.finishMultiCC(world);
            }

            delay = Math.abs(delay); 
            
            if (delay == 0 || delay < 20)
                delay = 1000; 

            
            test.isAnimated = true;
            Date current = Calendar.getInstance().getTime();
            test.lastUsed = current ;
            
            final int i = 0;
            t.schedule(new TimerTask() {
                int nbrRepetitions = test.steps.size() - 1;

                public void run() {
                    if (nbrRepetitions > 0) {

                        test.computeNewState(nbrRepetitions, b1);
                        nbrRepetitions--;
                        test.switchBlocs(world);
                    } else {
                        test.isAnimated = false;
                        this.cancel();
                    }
                }

            }, delay, delay);
        }
    }

    public void checkSign(Block block, World world, Player player) {

        Material mat = block.getType();
        
        if (mat.equals(Material.SIGN) || mat.equals(Material.SIGN_POST) || mat.equals(Material.WALL_SIGN)) {//mat Material.SIGN) {
            Sign sign = (Sign) block.getState();

            String line1;
            String line2;
            String line3;
            String line4;

            line1 = sign.getLine(0);
            line2 = sign.getLine(1);
            line3 = sign.getLine(2);
            line4 = sign.getLine(3);

            if (line1.equalsIgnoreCase("cc-animate")) {
                if (line3.equalsIgnoreCase(""))
                    line3 = "NC";

                if (this.structureExist(line2, line3)) {
                    if (line4.equalsIgnoreCase(""))
                        line4 = "1000";
                    long val = Long.parseLong(line4);

                    this.animateCC(line2, line3, world, val);
                }
            }
            

            if (line1.equalsIgnoreCase("cc-settime")) {
                if (line3.equalsIgnoreCase(""))
                    line3 = "NC";

                if (this.structureExist(line2, line3)) {
                    if (line4.equalsIgnoreCase(""))
                        line4 = "1000";
                    long val = Long.parseLong(line4);

                    this.setTime(line2, line3, world, val, true, player);
                }
            } 

            if (line1.equalsIgnoreCase("cc-unsettime")) {
                if (line3.equalsIgnoreCase(""))
                    line3 = "NC";

                if (this.structureExist(line2, line3)) {
                    if (line4.equalsIgnoreCase(""))
                        line4 = "1000";
                    long val = Long.parseLong(line4);

                    this.setTime(line2, line3, world, val, false, player);
                }
            }
            
        }
    }

    private void setTime(String eventName, String groupName, final World world, long delay, boolean b, Player player) {

        final StructureCC test = this.getStructure(eventName, groupName);
        
        if(player != null)
        {
            if(b)
            {
                player.sendMessage(String.format(this.getLocale("cc.settime.isNowLimited"), delay));
            }
            else
            {       
                player.sendMessage(this.getLocale("cc.settime.isNowRemoved"));
            }
        }
        
        test.isDateLimited = b;
        test.timeForDateLimited = delay;
        this.modifyStructure(test);
        
    }
    

}
