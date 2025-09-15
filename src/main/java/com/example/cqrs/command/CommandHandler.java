package com.example.cqrs.command;

public interface CommandHandler<T extends Command> {
    void handle(T command);
}