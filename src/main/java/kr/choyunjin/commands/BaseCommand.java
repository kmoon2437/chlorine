package kr.choyunjin.commands;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

public abstract class BaseCommand {
    private static final Field[] CHILDREN_FIELDS;

    static {
        try {
            CHILDREN_FIELDS = new Field[]{
                CommandNode.class.getDeclaredField("children"),
                CommandNode.class.getDeclaredField("literals"),
                CommandNode.class.getDeclaredField("arguments")
            };
            for (Field field : CHILDREN_FIELDS) {
                field.setAccessible(true);
            }
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private String name;
    private String[] aliases;
    private String permission;
    private LiteralCommandNode<?> commandNode;

    protected BaseCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
        this.permission = null;
        this.commandNode = this.generateCommandNode(CommandNodeBuilder.getInstance());
    }
    
    public String name() {
        return this.name;
    }

    public String[] aliases() {
        return this.aliases;
    }

    public String permission() {
        return this.permission;
    }

    protected void permission(String permission) {
        this.permission = permission;
    }

    protected LiteralCommandNode<?> generateCommandNode(CommandNodeBuilder b) {
        return b.literal(this.name).build();
    }

    public LiteralCommandNode<?> getCommandNode() {
        return this.commandNode;
    }

    protected String getGreedyString(String[] args, int i) {
        StringBuilder builder = new StringBuilder(args[i]);
        i++;
        for (; i < args.length; i++) {
            builder.append(" ").append(args[i]);
        }
        return builder.toString();
    }

    public void run(Server server, Player sender, String label, String[] args) throws Exception {
        this.run(server, (CommandSender)sender, label, args);
    }

    public void run(Server server, CommandSender sender, String label, String[] args) throws Exception {
        throw new Exception("No command runner method found. Please implement a new one");
    }

    public void showHelp(CommandSender sender) {
        // 일단 아무것도 안함
    }

    protected List<String> getAllPlayerNames(Server server) {
        List<String> playerNames = new ArrayList<>();
        Iterator<? extends Player> players = server.getOnlinePlayers().iterator();
        while (players.hasNext()) {
            playerNames.add(players.next().getName());
        }
        return playerNames;
    }

    public List<String> getTabCompleteOptions(Server server, CommandSender sender, String label, String[] args) {
        return Collections.emptyList();
    }
}