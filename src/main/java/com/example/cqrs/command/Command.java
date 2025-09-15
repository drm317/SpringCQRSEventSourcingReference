package com.example.cqrs.command;

import java.util.UUID;

public interface Command {
    UUID getAggregateId();
}