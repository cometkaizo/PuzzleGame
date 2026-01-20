package com.cometkaizo.command;

import com.cometkaizo.command.nodes.Command;
import com.cometkaizo.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description: A collection of commands that can be invoked by user input
 */
public class CommandGroup {

    private final List<List<String>> names;
    private final List<Command> commands;
    private final List<Supplier<Command>> commandSuppliers;

    /// Creates a new command group with the given commands
    @SafeVarargs
    public CommandGroup(Supplier<Command>... commands) {
        this.commandSuppliers = List.of(commands);
        this.commands = new ArrayList<>(commands.length);
        this.names = new ArrayList<>(commands.length);

        for (Supplier<Command> commandSupplier : commands) {
            Command command = commandSupplier.get();
            this.commands.add(command);
            this.names.add(command.getNames());
        }

        assert this.commands.size() == this.names.size();
    }
    /// Creates a new command group with the given commands and corresponding names
    @SafeVarargs
    public CommandGroup(Function<Command, List<String>> nameGenerator, Supplier<Command>... commands) {
        this.commandSuppliers = List.of(commands);
        this.commands = new ArrayList<>(commands.length);
        this.names = new ArrayList<>(commands.length);

        for (Supplier<Command> commandSupplier : commands) {
            Command command = commandSupplier.get();
            this.commands.add(command);
            this.names.add(nameGenerator.apply(command));
        }

        assert this.commands.size() == this.names.size();
    }

    /// Executes a command corresponding to the string input
    public Object execute(String input) {
        validateInput(input);

        // getting inputted command information
        String[] parts = getInputParts(input);
        String commandName = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        // getting index of correct command based on input command name
        int commandIndex = getValidCommandIndexOrThrow(commandName);

        // getting, running, and resetting the correct command
        Command command = commands.get(commandIndex);
        resetCommand(commandIndex);
        return command.execute(args);
    }

    /// Resets the command with the given index
    private void resetCommand(int commandIndex) {
        commands.set(commandIndex, commandSuppliers.get(commandIndex).get());
    }

    /// Gets the command index with the command name, or throws if there is no such command
    private int getValidCommandIndexOrThrow(String commandName) {
        int commandIndex = CollectionUtils.indexOf(names, names -> names.contains(commandName));
        if (commandIndex == -1) throw new UnknownCommandException(commandName, names);
        return commandIndex;
    }

    /// Gets the parts of the input, separated by spaces
    private static String[] getInputParts(String input) {
        return input.trim().split(" ");
    }

    /// Ensures the input is not null or blank
    private static void validateInput(String input) {
        Objects.requireNonNull(input, "Command cannot be null");
        if (input.isBlank()) throw new UnknownCommandException("Command cannot be blank");
    }

    /// Returns a string representation of this object
    @Override
    public String toString() {
        return "CommandGroup{" +
                commands.stream().map(c -> "\t" + c).collect(Collectors.joining(",\n", "\n", "\n")) +
                '}';
    }
}
