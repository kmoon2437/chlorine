package kr.choyunjin.commands;

import java.lang.reflect.Field;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

// 일단 지금은 runtime에서 annotation을 처리하고 있긴 한데
// 추후 compile time에 처리하도록 바꿀 예정
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

    protected String getGreedyStringArg(String[] args, int i) {
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

    abstract public void run(Server server, CommandSender sender, String label, String[] args) throws Exception;

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
    public void applyCommandNode(Player player, RootCommandNode<?> rootNode) {
        if (this.permission != null && !player.hasPermission(this.permission)) {
            return;
        }
        this.removeChildNode(rootNode, this.commandNode.getName());
        rootNode.addChild((CommandNode)this.commandNode);
    }
}