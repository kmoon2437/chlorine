package kr.choyunjin.commands;

import java.lang.reflect.Field;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
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

    private CommandNode<?> commandNode;

    public CommandNode<?> generateCommandNode(String name) {
        return LiteralArgumentBuilder.literal(name).build();
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

    @SuppressWarnings("unchecked")
    private void removeChildNode(RootCommandNode<?> rootNode, String name) {
        try {
            for (Field field : CHILDREN_FIELDS) {
                Map<String, ?> children = (Map<String, ?>)field.get(rootNode);
                children.remove(name);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void applyCommandNode(Player player, RootCommandNode<?> rootNode, String label) {
        if (this.commandNode == null) {
            this.commandNode = this.generateCommandNode(label);
        }

        this.removeChildNode(rootNode, this.commandNode.getName());
        rootNode.addChild((CommandNode)this.commandNode);
    }
}