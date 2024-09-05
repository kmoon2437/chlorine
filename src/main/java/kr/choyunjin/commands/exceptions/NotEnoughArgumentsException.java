package kr.choyunjin.commands.exceptions;

import kr.choyunjin.commands.BaseCommand;

public class NotEnoughArgumentsException extends Exception {
    private BaseCommand command;

    public NotEnoughArgumentsException(BaseCommand command) {
        this.command = command;
    }

    public BaseCommand getCommand() {
        return this.command;
    }
}