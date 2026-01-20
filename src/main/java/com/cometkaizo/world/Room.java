package com.cometkaizo.world;

import com.cometkaizo.Main;
import com.cometkaizo.game.Game;
import com.cometkaizo.game.GameState;
import com.cometkaizo.game.LoadException;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Renderable;
import com.cometkaizo.util.CollectionUtils;
import com.cometkaizo.util.MathUtils;
import com.cometkaizo.world.block.Block;
import com.cometkaizo.world.block.BlockTypes;
import com.cometkaizo.world.entity.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

import static com.cometkaizo.util.MathUtils.almostEquals;
import static java.lang.Math.*;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-17
 * Description: A room with a grid of blocks and entities
 */
public class Room implements Tickable, Renderable, Resettable {

    public static final String SAVE_EXTENSION = ".csv";
    private static final double BLIP_AROUND_AMT = 0.4;
    public final Game game;
    public String namespace;
    public final World world;
    public String name;
    public List<Checkpoint> checkpoints;
    public List<CameraLock> cameraLocks;
    public Player player;

    public Layer ground, walls, background, foreground;

    /// Creates a new room in the given world, and reads the room layout from the given path
    public Room(Game game, World world, Path path) throws IOException {
        this.game = game;
        this.world = world;
        this.name = path.getFileName().toString();
        this.namespace = name;

        try {
            this.ground = new Layer("ground", Main.getResource(path.resolve("ground" + SAVE_EXTENSION).toString()));
            this.walls = new Layer("walls", Main.getResource(path.resolve("walls" + SAVE_EXTENSION).toString()));
            this.background = new Layer("background", Main.getResource(path.resolve("background" + SAVE_EXTENSION).toString()));
            this.foreground = new Layer("foreground", Main.getResource(path.resolve("foreground" + SAVE_EXTENSION).toString()));
        } catch (Exception e) {
            throw new LoadException("Room layers failed to load", e);
        }

        checkpoints = walls.checkpoints;
        cameraLocks = background.cameraLocks;
    }


    /// Calculates the camera position that is enforced by any active camera locks
    public void lockCamera(Vector.MutableDouble cameraPos) {
        var closestLockPos = cameraLocks.stream()
                .filter(l -> l.isActive(player.getPosition())) // filter for active camera locks
                .min(Comparator.comparingDouble(l -> l.restrict(cameraPos).distanceSqr(player.getPosition()))); // find minimum distance camera lock

        closestLockPos.ifPresent(cameraLock -> cameraPos.set(cameraLock.restrict(cameraPos)));
    }


    /// Updates the blocks and entities in this room every tick
    @Override
    public void tick() {
        ground.tick();
        walls.tick();
        background.tick();
        foreground.tick();
    }

    /// Renders this room to the screen
    @Override
    public void render(Canvas canvas) {
        background.render(canvas);
        ground.render(canvas);
        walls.render(canvas);
        foreground.render(canvas);
        cameraLocks.forEach(l -> l.render(canvas));
    }

    /// Returns the object with the given name, or null if there is no such block or entity
    public Object getBlockOrEntity(String name) {
        Object result;
        if ((result = ground.getBlockOrEntity(name)) != null) return result;
        if ((result = walls.getBlockOrEntity(name)) != null) return result;
        if ((result = background.getBlockOrEntity(name)) != null) return result;
        if ((result = foreground.getBlockOrEntity(name)) != null) return result;
        return null;
    }

    /// Gets this room's ID
    public String getNamespace() {
        return namespace;
    }

    /// Gets this room's name
    public String getName() {
        return name;
    }

    /// Gets the checkpoints in this room
    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
    /// Gets the checkpoint that the player spawns at
    public Checkpoint getFirstCheckpoint() {
        return (Checkpoint) walls.named.get("first");
    }

    /// Resets everything in the room
    @Override
    public void reset() {
        ground.reset();
        walls.reset();
        background.reset();
        foreground.reset();
    }

    /// Sets the player in this room
    public void setPlayer(Player player) {
        if (this.player != null) walls.removeEntity(player);
        this.player = player;
        walls.addEntity(player); // the player exists on the "walls" layer
    }

    /// Writes this room to the game state
    public void write(GameState state) {
        ground.write(state);
        walls.write(state);
        background.write(state);
        foreground.write(state);
    }

    /// A single layer in the room
    public class Layer implements Tickable, Renderable, Resettable {
        public static final String RESPAWN_ID = "R", CAMERA_LOCK_ID = "CL";
        public final Room room = Room.this;
        public final Block[][] blocks;
        public final Light[][] light;
        public final List<Entity> entities = new ArrayList<>();
        private List<Entity> entitiesSortedByY = new ArrayList<>(); // list of entities sorted by largest y to lowest y, updated every tick, maintained for rendering order
        public final Map<String, Object> named = new HashMap<>();
        public final List<Checkpoint> checkpoints;
        public final List<CameraLock> cameraLocks;
        public final String name;
        public final Image baseImage;

        /// Reads the layer in from the given input stream
        public Layer(String name, InputStream is) throws IOException {
            this.name = name;
            baseImage = Assets.texture("layer/" + name);

            var in = new BufferedReader(new InputStreamReader(is));
            var lines = in.lines().toList().reversed(); // reverse y

            var blocks = new ArrayList<List<Block>>();
            checkpoints = new ArrayList<>();
            cameraLocks = new ArrayList<>();

            for (int r = 0; r < lines.size(); r ++) { // for each row index r
                var line = lines.get(r);
                var row = new ArrayList<Block>();

                int c = -1; // for each column index c
                String[] split = line.split(",", -1); // split the line into cells (separated by , in a csv file)
                for (String s : split) {
                    c ++;

                    Args args = new Args(s); // parse the current cell into an Args instance for easy reading
                    String id = args.id(); // get id of the object in this cell

                    // special values
                    if (readSpecialValue(id, r, c, args)) {
                        row.add(newAirBlock(c, r));
                        continue;
                    }

                    // blocks
                    if (readBlock(id, c, r, args, row)) continue;

                    // entities
                    if (readEntity(id, c, r, args)) continue;

                    // err
                    Main.err(getUnknownObjectIdMsg(name, id, lines, r, c));
                }

                // add the row of blocks to the 2D list
                blocks.add(row);
            }

            // convert list to array
            this.blocks = new Block[blocks.size()][];
            this.light = new Light[blocks.size()][];
            for (int r = 0; r < blocks.size(); r++) {
                var row = blocks.get(r);
                this.blocks[r] = row.toArray(Block[]::new);
                this.light[r] = new Light[row.size()];
            }

            in.close();
        }

        /// Gets the error message for an unknown object id
        private static String getUnknownObjectIdMsg(String name, String id, List<String> lines, int r, int c) {
            return name + " - unknown object id: " + id +
                    " at (" + (lines.size() - r) + "," + (c + 1) + ")" +
                    " or (" + MathUtils.toSheetCol(c) + (lines.size() - r) + ")";
        }

        /// Reads a special value such as a camera lock or checkpoint
        private boolean readSpecialValue(String id, int r, int c, Args args) {
            switch (id) {
                case RESPAWN_ID -> {
                    var checkpoint = new Checkpoint(r, c, args.nextInt(0), args.nextInt(0), args.nextInt(4), args.nextInt(4), args.next());
                    checkpoints.add(checkpoint);
                    if (!checkpoint.name.isEmpty()) named.put(checkpoint.name, checkpoint);
                    return true;
                }
                case CAMERA_LOCK_ID -> {
                    cameraLocks.add(new CameraLock(args, r, c));
                    return true;
                }
                case null, default -> {
                }
            }
            return false;
        }

        /// Reads a block in and stores it in the given row
        private boolean readBlock(String id, int c, int r, Args args, ArrayList<Block> row) {
            if (BlockTypes.BLOCKS.containsKey(id)) {
                var b = BlockTypes.BLOCKS.get(id).apply(this, Vector.immutable(c, r), args);
                row.add(b);
                if (b.hasName()) named.put(b.getName(), b);

                return true;
            }

            row.add(newAirBlock(c, r)); // add an air block if no known block id is found
            return false;
        }

        /// Reads an entity in and stores it in the entities list
        private boolean readEntity(String id, int c, int r, Args args) {
            if (EntityTypes.ENTITIES.containsKey(id)) {
                var e = EntityTypes.ENTITIES.get(id).apply(this, Vector.mutable((double) c, r), args);
                entities.add(e);
                if (e.hasName()) named.put(e.getName(), e);
                return true;
            }
            return false;
        }

        /// Creates a new air block at the given position
        private Block newAirBlock(int c, int r) {
            return BlockTypes.BLOCKS.get("").apply(this, Vector.immutable(c, r), Args.EMPTY);
        }

        /// Calculates the allowed movement from the given starting position to the given ending position that the given
        /// entity is performing, and stores the resulting allowed movement (after collision checks) into the given result vector
        public void calcAllowedMovement(Vector.Double from, Vector.Double to, CollidableEntity entity, Vector.MutableDouble result, boolean canBlip) {
            if (entity == null) return;
            var boundingBox = entity.getBoundingBox();
            if (boundingBox == null) return;

            double bbOffsetX = from.getX() - boundingBox.getX();
            double bbOffsetY = from.getY() - boundingBox.getY();

            boolean xAxisAligned = almostEquals(from.getY(), to.getY());
            boolean yAxisAligned = almostEquals(from.getX(), to.getX());

            calcAllowedOnlyYMovement(from, to, entity, result, canBlip && yAxisAligned);
            boundingBox.position.y = result.y - bbOffsetY;
            calcAllowedOnlyXMovement(from, to, entity, result, canBlip && xAxisAligned);
            boundingBox.position.x = result.x - bbOffsetX;
        }

        /// Calculates the allowed vertical movement from the given starting position to the given ending position
        private void calcAllowedOnlyYMovement(Vector.Double f, Vector.Double t, CollidableEntity entity, Vector.MutableDouble result, boolean canBlip) {
            double from = f.getY(), to = t.getY();
            var boundingBox = entity.getBoundingBox();
            int direction = (int) signum(to - from);
            boolean isMovingUp = direction == 1;
            double originalBoundingBoxY = boundingBox.getY();
            double bbOffsetY = from - originalBoundingBoxY;
            double bbOffsetX = f.getX() - boundingBox.getX();

            for (int y = (int) from; y != (int) to + direction; y += direction) {
                boundingBox.position.y = y != (int) to ?
                        (y - (int) from) + originalBoundingBoxY :
                        to - bbOffsetY;

                var solidBlocks = getBlocksWithin(boundingBox, block -> block.isSolid(entity));
                var solidEntities = getEntitiesWithin(boundingBox, e -> e != entity && e instanceof CollidableEntity c && c.isSolid(entity));
                if (!solidBlocks.isEmpty() || !solidEntities.isEmpty()) {
                    boundingBox.position.y = result.y = getTruncatedYMovement(boundingBox, solidBlocks, solidEntities, isMovingUp, bbOffsetY);
                    if (canBlip) { // try blip
                        if (solidBlocks.size() + solidEntities.size() == 1) {
                            double xBeforeBlip = result.x;
                            double left, right;
                            if (!solidBlocks.isEmpty()) {
                                left = solidBlocks.getFirst().getX();
                                right = left + 1;
                            } else {
                                var c = (CollidableEntity) solidEntities.getFirst();
                                left = c.getBoundingBox().getLeft();
                                right = c.getBoundingBox().getRight();
                            }
                            if (boundingBox.getRight() - left < BLIP_AROUND_AMT) result.x -= boundingBox.getRight() - left;
                            else if (right - boundingBox.getLeft() < BLIP_AROUND_AMT) result.x += right - boundingBox.getLeft();
                            else return;
                            if (containsSolid(boundingBox, entity)) {
                                result.x = xBeforeBlip;
                                return;
                            }
                            boundingBox.position.x = result.x - bbOffsetX;
                        } else return;
                    } else return;
                }
            }

            result.y = to;
        }

        /// Calculates the allowed horizontal movement from the given starting position to the given ending position
        private void calcAllowedOnlyXMovement(Vector.Double f, Vector.Double t, CollidableEntity entity, Vector.MutableDouble result, boolean canBlip) {
            double from = f.getX(), to = t.getX();
            var boundingBox = entity.getBoundingBox();
            int direction = (int) signum(to - from);
            boolean isMovingRight = direction == 1;
            double originalBoundingBoxX = boundingBox.getX();
            double bbOffsetX = from - originalBoundingBoxX;
            double bbOffsetY = f.getY() - boundingBox.getY();

            for (int x = (int) from; x != (int) to + direction; x += direction) {
                boundingBox.position.x = x != (int) to ?
                        (x - (int) from) + originalBoundingBoxX :
                        to - bbOffsetX;

                var solidBlocks = getBlocksWithin(boundingBox, block -> block.isSolid(entity));
                var solidEntities = getEntitiesWithin(boundingBox, e -> e != entity && e instanceof CollidableEntity c && c.isSolid(entity));
                if (!solidBlocks.isEmpty() || !solidEntities.isEmpty()) {
                    result.x = getTruncatedXMovement(boundingBox, solidBlocks, solidEntities, isMovingRight, bbOffsetX);
                    boundingBox.position.x = result.x - bbOffsetX;
                    if (canBlip) { // try blip
                        if (solidBlocks.size() + solidEntities.size() == 1) {
                            double yBeforeBlip = result.y;
                            double top, bottom;
                            if (!solidBlocks.isEmpty()) {
                                bottom = solidBlocks.getFirst().getY();
                                top = bottom + 1;
                            } else {
                                var c = (CollidableEntity) solidEntities.getFirst();
                                bottom = c.getBoundingBox().getBottom();
                                top = c.getBoundingBox().getTop();
                            }
                            if (top - boundingBox.getBottom() < BLIP_AROUND_AMT) result.y += top - boundingBox.getBottom();
                            else if (boundingBox.getTop() - bottom < BLIP_AROUND_AMT) result.y -= boundingBox.getTop() - bottom;
                            else return;
                            if (containsSolid(boundingBox, entity)) {
                                result.y = yBeforeBlip;
                                return;
                            }
                            boundingBox.position.y = result.y - bbOffsetY;
                        } else return;
                    } else return;
                }
            }

            result.x = to;
        }

        /// Truncates the movement vertically according to solid block and entity collision, and returns the truncated y value
        private static double getTruncatedYMovement(BoundingBox boundingBox, List<Block> solidBlocks, List<Entity> solidEntities, boolean truncateUnder, double bbOffset) {
            if (solidBlocks.isEmpty() && solidEntities.isEmpty()) return boundingBox.position.y;
            if (truncateUnder) {
                double result = Double.MAX_VALUE;
                var bottomMostBlock = CollectionUtils.findMin(solidBlocks, Block::getY);
                if (bottomMostBlock != null) result = min(result, bottomMostBlock.getY() - bbOffset);
                var bottomMostEntity = (CollidableEntity) CollectionUtils.findMin(solidEntities, e -> ((CollidableEntity) e).getBoundingBox().getBottom());
                if (bottomMostEntity != null) result = min(result, bottomMostEntity.getBoundingBox().getBottom());
                return result - boundingBox.getHeight();
            } else {
                double result = -Double.MAX_VALUE;
                Block topMostBlock = CollectionUtils.findMax(solidBlocks, Block::getY);
                if (topMostBlock != null) result = max(result, topMostBlock.getY() + 1 + bbOffset);
                var topMostEntity = (CollidableEntity) CollectionUtils.findMin(solidEntities, e -> ((CollidableEntity) e).getBoundingBox().getTop());
                if (topMostEntity != null) result = max(result, topMostEntity.getBoundingBox().getTop());
                return result;
            }
        }

        /// Truncates the movement horizontally according to solid block and entity collision, and returns the truncated x value
        private static double getTruncatedXMovement(BoundingBox boundingBox, List<Block> solidBlocks, List<Entity> solidEntities, boolean truncateToLeft, double bbOffset) {
            if (solidBlocks.isEmpty() && solidEntities.isEmpty()) return boundingBox.position.x;
            if (truncateToLeft) {
                double result = Double.MAX_VALUE;
                var leftMostBlock = CollectionUtils.findMin(solidBlocks, Block::getX);
                if (leftMostBlock != null) result = min(result, leftMostBlock.getX() - boundingBox.getWidth() + bbOffset);
                var leftMostEntity = (CollidableEntity) CollectionUtils.findMin(solidEntities, e -> ((CollidableEntity) e).getBoundingBox().getLeft());
                if (leftMostEntity != null) result = min(result, leftMostEntity.getBoundingBox().getLeft() - bbOffset);
                return result;
            } else {
                double result = -Double.MAX_VALUE;
                Block rightMostBlock = CollectionUtils.findMax(solidBlocks, Block::getX);
                if (rightMostBlock != null) result = max(result, rightMostBlock.getX() + 1 + bbOffset);
                var rightMostEntity = (CollidableEntity) CollectionUtils.findMin(solidEntities, e -> ((CollidableEntity) e).getBoundingBox().getRight());
                if (rightMostEntity != null) result = max(result, rightMostEntity.getBoundingBox().getRight() + bbOffset);
                return result;
            }
        }

        /// Gets all blocks within the given bounding box
        public List<Block> getBlocksWithin(BoundingBox boundingBox) {
            return getBlocksWithin(boundingBox, b -> true);
        }

        /// Gets all blocks within the given bounding box that satisfy the given condition
        public List<Block> getBlocksWithin(BoundingBox boundingBox, Predicate<? super Block> condition) {
            int fromX = (int) floor(boundingBox.getLeft() + 1E-5);
            int fromY = (int) floor(boundingBox.getBottom() + 1E-5);
            int toX = (int) floor(boundingBox.getRight() - 1E-5);
            int toY = (int) floor(boundingBox.getTop() - 1E-5);

            List<Block> result = new ArrayList<>((abs(toX - fromX) + 1) * (abs(toY - fromY) + 1));

            for (int y = fromY; y <= toY; y ++) {
                for (int x = fromX; x <= toX; x ++) {
                    getBlock(x, y).ifPresent(block -> {
                        if (condition.test(block)) result.add(block);
                    });
                }
            }

            return result;
        }

        /// Gets all entities within the given block
        private List<Entity> getEntitiesWithinBlock(Vector.Int pos) {
            return getEntitiesWithin(new BoundingBox(Vector.mutableDouble(pos), Vector.immutable(1D, 1D)));
        }
        /// Gets all entities within the given bounding box
        public List<Entity> getEntitiesWithin(BoundingBox boundingBox) {
            return getEntitiesWithin(boundingBox, b -> true);
        }

        /// Gets all entities within the given bounding box that satisfy the given condition
        public List<Entity> getEntitiesWithin(BoundingBox boundingBox, Predicate<? super Entity> condition) {
            var result = new ArrayList<Entity>();
            for (var e : entities) {
                if (!condition.test(e)) continue;
                if (boundingBox.contains(e.getPosition()) ||
                        e instanceof CollidableEntity c && c.getBoundingBox().intersects(boundingBox)) {
                    result.add(e);
                }
            }
            return result;
        }

        /// Gets the block at the given position, if it exists
        public Optional<Block> getBlock(Vector.Int position) {
            return getBlock(position.getX(), position.getY());
        }
        /// Gets the block at the given position, if it exists
        public Optional<Block> getBlock(int x, int y) {
            if (y < 0 || y >= blocks.length) return Optional.empty();
            var row = blocks[y];
            if (x < 0 || x >= row.length) return Optional.empty();
            return Optional.of(row[x]);
        }
        /// Gets the block type at the given position, if it exists
        public Optional<Class<? extends Block>> getBlockType(int x, int y) {
            return getBlock(x, y).map(Block::getClass);
        }
        /// Returns whether the block at the given position is of the given type
        public boolean isBlockType(int x, int y, Class<? extends Block> type) {
            return getBlockType(x, y).orElse(null) == type;
        }

        /// Returns whether the given bounding box contains a block or entity that is solid to the given entity
        public boolean containsSolid(BoundingBox boundingBox, Entity entity) {
            return !getBlocksWithin(boundingBox, block -> block.isSolid(entity)).isEmpty() ||
                    !getEntitiesWithin(boundingBox, e -> e instanceof CollidableEntity c && c.isSolid(entity)).isEmpty();
        }

        /// Returns the block or entity with the given name, or null if there is no such object
        public Object getBlockOrEntity(String name) {
            return name == null ? null : named.get(name);
        }

        /// Updates the blocks and entities on this layer
        @Override
        public void tick() {
            for (var row : light) Arrays.fill(row, null); // reset light every single tick

            entities.forEach(Entity::resetLight);
            for (var row : blocks) for (var b : row) b.resetLight();
            entities.forEach(Entity::tickLightEmission);
            for (var row : blocks) for (var b : row) b.tickLightEmission();
            entities.forEach(Tickable::tick);

            var entitiesSortedByY = new ArrayList<>(entities);
            entitiesSortedByY.sort(Comparator.comparingDouble(Entity::getRenderY).reversed());
            this.entitiesSortedByY = entitiesSortedByY;
        }

        /// Renders this layer to the screen
        @Override
        public void render(Canvas canvas) {
            int nextRenderEntityId = 0; // keep track of which entity we are yet to render

            // render blocks from the top row down
            for (int r = min(game.getCameraTop(), blocks.length - 1); r >= max(game.getCameraBottom(), 0); r--) {
                // render the row of light that must be behind the player
                for (int c = max(game.getCameraLeft(), 0); c < min(game.getCameraRight(), blocks[r].length); c++) {
                    if (blocks[r][c].shouldRenderBehindEntities()) blocks[r][c].render(canvas);
                    if (light[r][c] != null && light[r][c].shouldRenderBehindEntities()) light[r][c].render(canvas);
                }

                // render all entities that are in this row's 1 block y range
                while (nextRenderEntityId < entitiesSortedByY.size() &&
                        entitiesSortedByY.get(nextRenderEntityId).getRenderY() > r) {
                    entitiesSortedByY.get(nextRenderEntityId ++).render(canvas);
                }

                // render the row of the blocks and light
                for (int c = max(game.getCameraLeft(), 0); c < min(game.getCameraRight(), blocks[r].length); c++) {
                    if (!blocks[r][c].shouldRenderBehindEntities()) blocks[r][c].render(canvas);
                    if (light[r][c] != null && !light[r][c].shouldRenderBehindEntities()) light[r][c].render(canvas);
                }
            }

            // render remaining entities
            while (nextRenderEntityId < entitiesSortedByY.size())
                entitiesSortedByY.get(nextRenderEntityId++).render(canvas);

            // render base image for this layer
            canvas.renderImage(baseImage, -14D, -10D, 0, -1);
        }

        /// Resets this layer
        public void reset() {
            entities.forEach(Entity::reset);
            for (var row : blocks) for (var b : row) b.reset();
        }

        /// Adds an entity to this layer
        public <T extends Entity> T addEntity(T entity) {
            entities.add(entity);
            if (entity.hasName()) named.put(entity.getName(), entity);
            return entity;
        }
        /// Removes an entity from this layer
        public <T extends Entity> T removeEntity(T entity) {
            entities.remove(entity);
            if (entity.hasName()) named.remove(entity.getName(), entity);
            return entity;
        }

        /// Lights up a line of blocks starting from the given position and traveling in the given direction
        public void lightUp(Vector.Int fromPos, Direction direction, Object emitter) {
            var pos = Vector.mutableInt(fromPos);
            while (!getBlock(pos).map(Block::blocksLight).orElse(true)) {
                // set the light
                boolean first = pos.equals(fromPos);
                var collision = findFirstEntityCollision(pos, direction, emitter);
                setLight(pos, new Light(this, pos, direction, collision.orElse(null), first));

                // update any blocks or entities if it's not the emission block
                if (!first) {
                    getEntitiesWithinBlock(pos).forEach(e -> e.updateLight(direction));
                    getBlock(pos).ifPresent(b -> b.updateLight(direction));
                }

                // move the current pos
                pos.add(direction.delta());

                // if collision with entity occurred, break
                if (collision.isPresent()) return;
            }
            // update the blocking block/entity
            getEntitiesWithinBlock(pos).forEach(e -> e.updateLight(direction));
            getBlock(pos).ifPresent(b -> b.updateLight(direction));
        }

        /// Gets the light at the given position
        public Optional<Light> getLight(Vector.Int pos) {
            return getLight(pos.getX(), pos.getY());
        }
        /// Gets the light at the given position
        public Optional<Light> getLight(int x, int y) {
            if (y < 0 || y >= light.length) return Optional.empty();
            var row = light[y];
            if (x < 0 || x >= row.length) return Optional.empty();
            return Optional.of(row[x]);
        }
        /// Sets the light at the given position
        public void setLight(Vector.Int pos, Light l) {
            setLight(pos.getX(), pos.getY(), l);
        }
        /// Sets the light at the given position
        public void setLight(int x, int y, Light l) {
            if (y < 0 || y >= light.length) return;
            var row = light[y];
            if (x < 0 || x >= row.length) return;
            row[x] = l;
        }

        /// Finds the first entity collision at the given position in the given direction
        private Optional<CollidableEntity> findFirstEntityCollision(Vector.MutableInt pos, Direction direction, Object emitter) {
            var entities = getEntitiesWithinBlock(pos).stream()
                    .filter(CollidableEntity.class::isInstance)
                    .filter(e -> e != emitter)
                    .filter(Entity::blocksLight)
                    .map(CollidableEntity.class::cast);
            return switch (direction) {
                case UP -> entities.min(Comparator.comparingDouble(Entity::getY));
                case RIGHT -> entities.min(Comparator.comparingDouble(Entity::getX));
                case DOWN -> entities.max(Comparator.comparingDouble(Entity::getY));
                case LEFT -> entities.max(Comparator.comparingDouble(Entity::getX));
            };
        }

        /// Saves this layer to the game state
        public void write(GameState state) {
            for (var row : blocks)
                for (var block : row)
                    block.write(state);
            for (var entity : entities)
                entity.write(state);
        }
    }

    /// A checkpoint
    public record Checkpoint(Vector.ImmutableDouble pos, BoundingBox activationArea, String name) {
        /// Creates a new checkpoint
        public Checkpoint(int r, int c, int left, int right, int up, int down, String name) {
            // its mutable and I don't think records are supposed to be mutable but whatever
            this(Vector.immutable(c + 0.5, r), new BoundingBox(Vector.mutable((double) c - left, r - down), Vector.immutable((double) left + 1 + right, up + 1 + down)), name);
        }
    }

    /// A region which restricts the camera
    public static class CameraLock implements Renderable {
        protected final int left, right, top, bottom;
        protected final BoundingBox activationArea;

        /// Reads a camera lock in from the Args instance
        public CameraLock(Args args, int r, int c) {
            this(c, r, args.nextInt(0), args.nextInt(0), args.nextInt(5), args.nextInt(3), args.nextInt(5), args.nextInt(3));
        }
        /// Creates a new camera lock
        public CameraLock(int left, int bottom, int width, int height, int leftRange, int topRange, int rightRange, int bottomRange) {
            int right = left + width;
            int top = bottom + height;
            this.activationArea = new BoundingBox(Vector.mutable((double) left - leftRange, bottom - bottomRange),
                    Vector.immutable((double) (right + rightRange) - (left - leftRange), (top + topRange) - (bottom - bottomRange)));
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

        /// Returns the restricted camera position given an initial camera position
        public Vector.ImmutableDouble restrict(Vector.Double pos) {
            return Vector.immutable(clamp(pos.getX(), left, right), clamp(pos.getY(), bottom, top));
        }
        /// Returns whether the given position is within this camera lock's activation area
        public boolean isActive(Vector.Double pos) {
            return activationArea.contains(pos);
        }

        /// Renders this camera lock to the screen
        @Override
        public void render(Canvas canvas) {
            canvas.renderDebugBoundingBox(activationArea, Color.ORANGE);
        }
    }
}
