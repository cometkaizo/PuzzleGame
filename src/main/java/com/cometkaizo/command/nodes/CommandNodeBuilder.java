package com.cometkaizo.command.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-19
 * Description: Builder for a command node
 */
public abstract class CommandNodeBuilder {

    protected int level;
    protected final List<CommandNodeBuilder> subNodes = new ArrayList<>(0);
    protected final List<Runnable> tasks = new ArrayList<>(0);
    private boolean splits = false;
    /**
     * Represents the last node of our line of nodes. When a line splits and merges again, {@code focus} will be the merged node.
     * In a root command node, {@code focus} will always be the last node in the graph. Under normal circumstances, there should
     * be no need to ever access an inaccurate {@code focus}.
     */
    private CommandNodeBuilder focus = this;

    /// Builds this builder object into a command node
    protected abstract CommandNode build();

    /// Adds a task into the focus
    public final CommandNodeBuilder executes(Runnable task) {
        focus.tasks.add(task);
        return this;
    }

    /// Adds a subnode to the focus
    public final CommandNodeBuilder then(CommandNodeBuilder subNode) {

        focus.addSubNodeToEndOfLine(subNode);

        focus = subNode.focus;
        return this;
    }

    /// Splits this node into multiple subnodes
    public final CommandNodeBuilder split(CommandNodeBuilder... subNodes) {
        focus.splits = true;

        // always merge after splitting
        CommandNodeBuilder mergeDestination = new EmptyCommandNodeBuilder();

        for (CommandNodeBuilder subNode : subNodes) {
            subNode.addSubNodeToEndOfLine(mergeDestination);
            focus.addSubNode(subNode);
        }

        focus = mergeDestination;
        return this;
    }

    /// Adds a subnode to the end of the line
    private void addSubNodeToEndOfLine(CommandNodeBuilder subNode) {
        focus.addSubNode(subNode);
    }

    /// Adds a subnode to this node
    private void addSubNode(CommandNodeBuilder subNode) {
        throwIfNodeIsRoot(subNode);
        throwIfThisNotEndOfLine(subNode);

        addSubNodeWithCorrectLevel(subNode);
    }

    /// Adds a subnode, updating its level to the correct value
    private void addSubNodeWithCorrectLevel(CommandNodeBuilder subNode) {
        int level;
        if (subNode.acceptsArguments()) {
            level = this.level + 1;
        } else {
            level = this.level;
        }
        subNode.updateLevel(level);

        subNodes.add(subNode);
    }

    /// changes the level of this subnode by delta, and does the same with all direct or indirect subnodes
    private void updateLevel(int delta) {
        this.level += delta;
        subNodes.forEach(subNode -> subNode.updateLevel(delta));
    }

    /// Throws an exception if this node is a root node
    private static void throwIfNodeIsRoot(CommandNodeBuilder node) {
        if (node.isRoot())
            throw new IllegalArgumentException("Cannot add a root command node as a sub-node: \n" + node);
    }

    /// Throws an exception if this node is not the end of the line
    private void throwIfThisNotEndOfLine(CommandNodeBuilder subNode) {
        if (!splits && subNodes.size() == 1)
            throw new IllegalStateException("Cannot add multiple sub-nodes to a non-splitting node; attempted to add \n" +
                    subNode + "\nto node \n" + this + "\nwith sub-node \n" + subNodes);
    }

    /// Returns whether this node accepts arguments
    public final boolean acceptsArguments() {
        return !(this instanceof NoArgCommandNodeBuilder);
    }
    /// Returns whether this node is a root
    public final boolean isRoot() {
        return this instanceof RootCommandNodeBuilder;
    }


}
