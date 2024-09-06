package kr.choyunjin.commands;

import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.arguments.*;

public class CommandNodeBuilder {
    private static CommandNodeBuilder instance;

    public static CommandNodeBuilder getInstance() {
        if (instance == null) {
            instance = new CommandNodeBuilder();
        }
        return instance;
    }

    public <T> LiteralArgumentBuilder<T> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public <T, U> RequiredArgumentBuilder<T, U> argument(String name, ArgumentType<U> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public StringArgumentType wordArg() {
        return StringArgumentType.word();
    }

    public StringArgumentType stringArg() {
        return StringArgumentType.string();
    }

    public StringArgumentType greedyStringArg() {
        return StringArgumentType.greedyString();
    }
}