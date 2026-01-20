package com.cometkaizo.command.nodes;

import com.cometkaizo.command.CommandSyntaxException;
import com.cometkaizo.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Author: Andy Wang
 * Date Modified: 2025-12-19
 * Description:
 * A CommandNode represents a single action in a command (e.g. getting user input, translating user input, executing code, etc).
 * Specific functionality is specified by subclasses.
 *
 * @see Command
 * @see RootCommandNode
 * @see LiteralCommandNode
 * @see ArgumentCommandNode
 * @see NoArgCommandNode
 * @see ConditionalCommandNode
 */
abstract class CommandNode {

    protected final int level;
    protected final List<CommandNode> subNodes;
    protected final List<Runnable> tasks;
    protected Command context;

    /// Creates a new command node
    protected CommandNode(CommandNodeBuilder builder) {
        this.subNodes = buildSubNodes(builder);
        this.tasks = List.copyOf(builder.tasks);
        this.level = builder.level;
    }
    /// builds the subnodes of the given builder
    private static List<CommandNode> buildSubNodes(CommandNodeBuilder builder) {
        return CollectionUtils.map(builder.subNodes, CommandNodeBuilder::build);
    }


    /**
     * Executes this node's tasks, and potentially one of its sub-nodes depending on arguments provided by {@code context}.
     * If no sub-nodes can be executed or there are insufficient arguments, a CommandSyntaxException is thrown.
     * If multiple sub-nodes can be executed, it will execute the first one that accepts the next argument.
     * @param context the context to run this command in
     * @throws CommandSyntaxException If there are insufficient arguments, or an argument could not be parsed by any sub-nodes.
     */
    final void execute(Command context) throws CommandSyntaxException {
        this.context = context;

        executeFunctionality();

        for (Runnable task : tasks)
            task.run();

        if (hasSubNodes())
            executeSubNodes();
    }

    /// Executes the subnodes
    private void executeSubNodes() throws CommandSyntaxException {
        if (!hasSubNodes()) return;

        if (hasNextArg())
            executeSubNodesWithNextArg();
        else {
            if (requiresNextArg()) {
                throw notEnoughArgsException();
            } else executeNoArgSubNode();
        }
    }


    /// Returns whether this command node accepts the given argument
    protected abstract boolean accepts(String arg);

    /// Executes the functionality for this command node
    protected abstract void executeFunctionality();

    /// Creates a new "not enough args" exception
    private CommandSyntaxException notEnoughArgsException() {
        String formattedArgs = Arrays.stream(context.args)
                .map(Objects::toString)
                .collect(Collectors.joining(" "));
        String formattedSubNodes = subNodes.stream()
                .map(CommandNode::toPrettyString)
                .collect(Collectors.joining("\n or "));

        return new CommandSyntaxException(
                "Unexpected end of arguments: \n    " +
                        formattedArgs + "\n    " +
                        " ".repeat(formattedArgs.length()) + "^\n" +
                        "required: \n    " +
                        formattedSubNodes
        );
    }

    /// Returns a user-displayable string representation of this command node
    public String toPrettyString() {
        return getClass().getSimpleName().replaceAll("(?<=.)CommandNode$", "").toUpperCase();
    }

    /// Executes the subnodes of this node with the next argument
    private void executeSubNodesWithNextArg() throws CommandSyntaxException {

        for (CommandNode subNode : subNodes) {
            if (subNode.accepts(nextArg())) {
                subNode.execute(context);
                return;
            }
        }

        throw wrongArgumentTypeException();
    }

    /// Creates a new "wrong argument type" exception
    private CommandSyntaxException wrongArgumentTypeException() {
        String formattedSubNodes = subNodes.stream()
                .map(CommandNode::toPrettyString)
                .collect(Collectors.joining("\n or "));

        return new CommandSyntaxException("Unexpected argument '" + nextArg() + "'; required: \n    " + formattedSubNodes);
    }

    /// Executes a subnode that does not accept any arguments
    private void executeNoArgSubNode() {
        CommandNode noArgSubNode = getNoArgSubNode();

        if (noArgSubNode == null) throw new NoSuchElementException();

        noArgSubNode.execute(context);
    }

    /// Returns whether any of the subnodes of this command node accepts arguments
    private boolean requiresNextArg() {
        return subNodes.stream().allMatch(CommandNode::acceptsArguments);
    }

    /// Gets the next subnode that does not accept arguments
    private CommandNode getNoArgSubNode() {
        return subNodes.stream()
                .filter(node -> !node.acceptsArguments())
                .findFirst().orElse(null);
    }

    /// Returns whether this command node accepts arguments
    public final boolean acceptsArguments() {
        return !(this instanceof NoArgCommandNode);
    }

    /// Returns the next argument
    private String nextArg() {
        return context.args[level + 1];
    }

    /// Returns whether there is an next argument
    private boolean hasNextArg() {
        return context.args.length - 1 > level;
    }

    /// Returns whether this command node has subnodes
    public boolean hasSubNodes() {
        return subNodes.size() > 0;
    }

}
