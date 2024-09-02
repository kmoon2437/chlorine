package kr.choyunjin.commands;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kr.choyunjin.commands.annotations.Command;
import kr.choyunjin.commands.annotations.Permission;
import kr.choyunjin.commands.annotations.Default;
import kr.choyunjin.commands.annotations.PlayerSender;
import kr.choyunjin.commands.annotations.arg.PlayerArg;
import kr.choyunjin.commands.annotations.arg.StringArg;
import kr.choyunjin.commands.annotations.arg.TextArg;
import kr.choyunjin.commands.exceptions.NoPermissionException;
import kr.choyunjin.commands.exceptions.NoExecutionMethodException;

// 일단 지금은 runtime에서 annotation을 처리하고 있긴 한데
// 추후 compile time에 처리하도록 바꿀 예정
public class CommandWrapper {
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

    public String name;
    public String[] aliases;
    public String permission;
    public Object inner;
    private LiteralCommandNode<?> commandNode;
    
    public CommandWrapper(Object inner) {
        Class<?> clazz = inner.getClass();
        Command commandAnno = clazz.getAnnotation(Command.class);
        this.name = commandAnno.name();
        this.aliases = commandAnno.aliases();
        this.permission = this.getPermission(clazz);
        this.inner = inner;
        this.commandNode = this.generateCommandNode();
    }

    private LiteralCommandNode<?> generateCommandNode() {
        return LiteralArgumentBuilder.literal(this.name).build();
    }

    @SuppressWarnings("unchecked")
    private void removeChild(RootCommandNode<?> rootNode, String name) {
        try {
            for (Field field : CHILDREN_FIELDS) {
                Map<String, ?> children = (Map<String, ?>)field.get(rootNode);
                children.remove(name);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void applyCommandNode(Player player, RootCommandNode<?> rootNode) {
        if (this.permission != null && !player.hasPermission(this.permission)) {
            return;
        }
        this.removeChild(rootNode, this.commandNode.getName());
        rootNode.addChild((CommandNode)this.commandNode);
    }

    public void run(CommandSender sender, String label, String[] args) throws NoPermissionException, NoExecutionMethodException {
        if (this.permission != null && !sender.hasPermission(this.permission)) {
            throw new NoPermissionException();
        }

        Class<?> clazz = this.inner.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        Method method = this.selectMethod(methods, sender);
        
        // method 인자 집어넣기
        Object[] methodArgs = new Object[method.getParameterCount()];
        Class<?>[] argTypes = method.getParameterTypes();
        int i = 0;
        int argI = 0;
        boolean isGreedyTextArg = false;
        for (Annotation[] annotations : method.getParameterAnnotations()) {
            if (annotations.length == 0) {
                if (argTypes[i] == Player.class) {
                    if (sender instanceof Player) {
                        methodArgs[i] = (Player)sender;
                    }
                } else if (argTypes[i] == CommandSender.class) {
                    methodArgs[i] = sender;
                }
            }
            for (Annotation annotation : annotations) {
                if (false) {
                    // 언젠간 쓰겠지
                } else if (!isGreedyTextArg) {
                    if (argI >= args.length) {
                        continue;
                    } else if (annotation instanceof PlayerArg) {
                        methodArgs[i] = sender.getServer().getPlayer(args[argI]);
                    } else if (annotation instanceof StringArg) {
                        methodArgs[i] = args[argI];
                    } else if (annotation instanceof TextArg) {
                        StringBuilder builder = new StringBuilder(args[argI++]);
                        for (; argI < args.length; argI++) {
                            builder.append(" ");
                            builder.append(args[argI]);
                        }
                        methodArgs[i] = builder.toString();
                        isGreedyTextArg = true;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
                argI++; // 여기까지 왔다는 건 명령어 인자를 나타내는 annotation이 있다는 뜻
                break;
            }
            i++;
        }

        // method 실행
        try {
            method.invoke(this.inner, methodArgs);
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Method selectMethod(Method[] originalMethods, CommandSender sender) throws NoPermissionException, NoExecutionMethodException {
        List<Method> methodsWithAnnotation = Arrays.stream(originalMethods).filter(method -> {
            return method.isAnnotationPresent(Default.class);
        }).toList();
        
        if (methodsWithAnnotation.size() == 0) {
            throw new NoExecutionMethodException();
        }
        
        List<Method> permittedMethods = methodsWithAnnotation.stream().filter(method -> {
            String permission = this.getPermission(method);
            return permission == null || sender.hasPermission(permission);
        }).toList();

        if (permittedMethods.size() == 0) {
            throw new NoPermissionException();
        }

        List<Method> methods = null;
        
        if (sender instanceof Player) {
            List<Method> methodsForPlayer = permittedMethods.stream().filter(method -> {
                return method.isAnnotationPresent(PlayerSender.class);
            }).toList();
            methods = methodsForPlayer.size() == 0 ? permittedMethods : methodsForPlayer;
        } else {
            List<Method> methodsNotForPlayer = permittedMethods.stream().filter(method -> {
                return !method.isAnnotationPresent(PlayerSender.class);
            }).toList();
            if (methodsNotForPlayer.size() == 0) {
                throw new NoExecutionMethodException();
            }
            methods = methodsNotForPlayer;
        }
        
        if (methods.size() == 0) {
            throw new NoExecutionMethodException();
        } else {
            return methods.get(0);
        }
    }
    
    private String getPermission(AnnotatedElement element) {
        if (element.isAnnotationPresent(Permission.class)) {
            Permission permission = element.getAnnotation(Permission.class);
            return permission.value();
        } else {
            return null;
        }
    }
}