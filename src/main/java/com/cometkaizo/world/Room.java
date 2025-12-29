package com.cometkaizo.world;

import com.cometkaizo.Main;
import com.cometkaizo.game.Game;
import com.cometkaizo.game.LoadException;
import com.cometkaizo.io.data.CompoundData;
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
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.cometkaizo.util.MathUtils.almostEquals;
import static java.lang.Math.*;

public class Room implements Tickable, Renderable, Resettable {

    public static final String SAVE_EXTENSION = ".csv";
    private static final double BLIP_AROUND_AMT = 0.4;
    public final Game game;
    public String namespace;
    public final World world;
    public String name;
    public ConnectionSet connectionSet = new ConnectionSet(null, null, null, null);
    public List<Checkpoint> checkpoints;
    public List<CameraLock> cameraLocks;
    public List<Trigger> triggers;
    public Player player;

    public Layer ground, walls, background, foreground;

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
        triggers = walls.triggers;
    }

    void onAddedTo(World world) {
    }

    public Connection getConnection(Direction direction) {
        return connectionSet.get(direction);
    }

    public void lockCamera(Vector.MutableDouble cameraPos) {
        var closestLockPos = cameraLocks.stream()
                .filter(l -> l.isActive(player.getPosition())) // filter for active camera locks
                .min(Comparator.comparingDouble(l -> l.restrict(cameraPos).distanceSqr(player.getPosition()))); // find minimum distance camera lock

        closestLockPos.ifPresent(cameraLock -> cameraPos.set(cameraLock.restrict(cameraPos)));
    }


    @Override
    public void tick() {
        ground.tick();
        walls.tick();
        background.tick();
        foreground.tick();
    }

    @Override
    public void render(Canvas canvas) {
        background.render(canvas);
        ground.render(canvas);
        walls.render(canvas);
        foreground.render(canvas);
        cameraLocks.forEach(l -> l.render(canvas));
    }

    public Object getBlockOrEntity(String name) {
        Object result;
        if ((result = ground.getBlockOrEntity(name)) != null) return result;
        if ((result = walls.getBlockOrEntity(name)) != null) return result;
        if ((result = background.getBlockOrEntity(name)) != null) return result;
        if ((result = foreground.getBlockOrEntity(name)) != null) return result;
        return null;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
    public Checkpoint getFirstCheckpoint() {
        return (Checkpoint) walls.named.get("first");
    }

    @Override
    public void reset() {
        ground.reset();
        walls.reset();
        background.reset();
        foreground.reset();
    }

    public List<Block> getGroundBeneath(CollidableEntity entity) {
        return ground.getBlocksWithin(entity.getBoundingBox(), b -> b.isSolid(entity));
    }

    public void setPlayer(Player player) {
        if (this.player != null) walls.removeEntity(player);
        this.player = player;
        walls.addEntity(player); // the player exists on the "walls" layer
    }


    public static class ConnectionSet {
        private final Map<Direction, Connection> connections = new HashMap<>(4);
        public ConnectionSet(Connection upConnection,
                             Connection downConnection,
                             Connection leftConnection,
                             Connection rightConnection) {
            if (upConnection != null) connections.put(Direction.UP, upConnection);
            if (downConnection != null) connections.put(Direction.DOWN, downConnection);
            if (leftConnection != null) connections.put(Direction.LEFT, leftConnection);
            if (rightConnection != null) connections.put(Direction.RIGHT, rightConnection);
        }
        private ConnectionSet(Map<Direction, Connection> connections) {
            this.connections.putAll(connections);
        }

        public static ConnectionSet of(CompoundData data, Map<String, Room> rooms) {
            Map<Direction, Connection> connections = new HashMap<>(4);

            for (String key : data.asMap().keySet()) {
                var direction = Direction.valueOf(key);
                var connection = Connection.of(data.getCompound(key), rooms);
                connections.put(direction, connection);
            }
            return new ConnectionSet(connections);
        }

        public static ConnectionSet of(String data, Map<String, Room> rooms) {
            CompoundData compound = new CompoundData();

            Stream<String> lines = data.lines();
            for (String line : lines.toList()) {
                String[] parts = line.split(",");

                String direction = parts[0];
                int start = Integer.parseInt(parts[1]);
                int length = Integer.parseInt(parts[2]);
                String destination = parts[3];

                compound.put(direction, new Connection(start, length, destination, rooms).write());
            }

            return of(compound, rooms);
        }

        public Connection get(Direction direction) {
            return connections.get(direction);
        }

        public CompoundData write() {
            CompoundData data = new CompoundData();
            for (Direction key : connections.keySet()) {
                var respawnPos = connections.get(key);
                if (respawnPos != null) data.put(key.name(), respawnPos.write());
            }
            return data;
        }

    }

    public record Connection(int start, int length, Supplier<Room> destination) {
        public static final String DESTINATION_KEY = "destination";
        public static final String START_KEY = "start";
        public static final String LENGTH_KEY = "length";

        public Connection(int start, int length, String destination, Map<String, Room> rooms) {
            this(start, length, () -> getRoom(destination, rooms));
        }

        private static Room getRoom(String namespace, Map<String, Room> rooms) {
            Room room = rooms.get(namespace);
            if (room == null) throw new NoSuchElementException("Unknown room with namespace '" + namespace + "'; available rooms are: " + rooms);
            return room;
        }

        public CompoundData write() {
            CompoundData data = new CompoundData();
            data.putString(DESTINATION_KEY, destination.get().getNamespace());
            data.putInt(START_KEY, start);
            data.putInt(LENGTH_KEY, length);
            return data;
        }

        public static Connection of(CompoundData data, Map<String, Room> rooms) {
            String destination = data.getString(DESTINATION_KEY);
            int start = data.getInt(START_KEY);
            int length = data.getInt(LENGTH_KEY);

            return new Connection(start, length, destination, rooms);
        }
    }

    public class Layer implements Tickable, Renderable, Resettable {
        public static final String RESPAWN_ID = "R", CAMERA_LOCK_ID = "CL", TRIGGER_ID = "T";
        public final Room room = Room.this;
        public final Block[][] blocks;
        public final List<Entity> entities = new ArrayList<>();
        private List<Entity> entitiesSortedByY = new ArrayList<>(); // list of entities sorted by largest y to lowest y, updated every tick, maintained for rendering order
        public final Map<String, Object> named = new HashMap<>();
        public final List<Checkpoint> checkpoints;
        public final List<CameraLock> cameraLocks;
        public final List<Trigger> triggers;
        public final String name;
        public final Image baseImage;

        public Layer(String name, InputStream is) throws IOException {
            this.name = name;
            baseImage = Assets.texture("layer/" + name);

            var in = new BufferedReader(new InputStreamReader(is));
            var lines = in.lines().toList().reversed(); // reverse y

            var blocks = new ArrayList<List<Block>>();
            checkpoints = new ArrayList<>();
            cameraLocks = new ArrayList<>();
            triggers = new ArrayList<>();

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
            for (int r = 0; r < blocks.size(); r++) {
                var row = blocks.get(r);
                this.blocks[r] = row.toArray(Block[]::new);
            }

            in.close();
        }

        private static String getUnknownObjectIdMsg(String name, String id, List<String> lines, int r, int c) {
            return name + " - unknown object id: " + id +
                    " at (" + (lines.size() - r) + "," + (c + 1) + ")" +
                    " or (" + MathUtils.toSheetCol(c) + (lines.size() - r) + ")";
        }

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
                case TRIGGER_ID -> {
                    triggers.add(new Trigger(r, c, args.nextInt(0), args.nextInt(0), args.nextInt(4), args.nextInt(4), args.nextInt(-1)));
                    return true;
                }
                case null, default -> {
                }
            }
            return false;
        }

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

        private boolean readEntity(String id, int c, int r, Args args) {
            if (EntityTypes.ENTITIES.containsKey(id)) {
                var e = EntityTypes.ENTITIES.get(id).apply(this, Vector.mutable((double) c, r), args);
                entities.add(e);
                if (e.hasName()) named.put(e.getName(), e);
                return true;
            }
            return false;
        }

        private Block newAirBlock(int c, int r) {
            return BlockTypes.BLOCKS.get("").apply(this, Vector.immutable(c, r), Args.EMPTY);
        }

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

        public List<Block> getBlocksWithin(BoundingBox boundingBox) {
            return getBlocksWithin(boundingBox, b -> true);
        }

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

        public List<Entity> getEntitiesWithin(BoundingBox boundingBox) {
            return getEntitiesWithin(boundingBox, b -> true);
        }

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

        public Optional<Block> getBlock(Vector.Int position) {
            return getBlock(position.getX(), position.getY());
        }

        public Optional<Block> getBlock(int x, int y) {
            if (y < 0 || y >= blocks.length) return Optional.empty();
            var row = blocks[y];
            if (x < 0 || x >= row.length) return Optional.empty();
            return Optional.of(row[x]);
        }
        public Optional<Class<? extends Block>> getBlockType(int x, int y) {
            return getBlock(x, y).map(Block::getClass);
        }

        public boolean containsSolid(BoundingBox boundingBox, Entity entity) {
            return !getBlocksWithin(boundingBox, block -> block.isSolid(entity)).isEmpty() ||
                    !getEntitiesWithin(boundingBox, e -> e instanceof CollidableEntity c && c.isSolid(entity)).isEmpty();
        }

        public Object getBlockOrEntity(String name) {
            return name == null ? null : named.get(name);
        }

        @Override
        public void tick() {
            entities.forEach(Tickable::tick);

            var entitiesSortedByY = new ArrayList<>(entities);
            entitiesSortedByY.sort(Comparator.comparingDouble(Entity::getRenderY).reversed());
            this.entitiesSortedByY = entitiesSortedByY;
        }

        @Override
        public void render(Canvas canvas) {
            int nextRenderEntityId = 0;
            // render blocks from the top row down
            for (int r = blocks.length - 1; r >= 0; r--) {
                // render all entities that are in this row's 1 block y range
                while (nextRenderEntityId < entitiesSortedByY.size() &&
                        entitiesSortedByY.get(nextRenderEntityId).getRenderY() > r) {
                    entitiesSortedByY.get(nextRenderEntityId ++).render(canvas);
                }
                // render the row of the blocks
                for (var b : blocks[r]) b.render(canvas);
            }
            canvas.renderImage(baseImage, -14D, -10D, 0, -1);
        }

        public void reset() {
            entities.forEach(Entity::reset);
            for (var row : blocks) for (var b : row) b.reset();
        }

        public <T extends Entity> T addEntity(T entity) {
            entities.add(entity);
            if (entity.hasName()) named.put(entity.getName(), entity);
            return entity;
        }
        public <T extends Entity> T removeEntity(T entity) {
            entities.remove(entity);
            if (entity.hasName()) named.remove(entity.getName(), entity);
            return entity;
        }

    }

    public record Checkpoint(Vector.ImmutableDouble pos, BoundingBox activationArea, String name) {
        public Checkpoint(int r, int c, int left, int right, int up, int down, String name) {
            // its mutable and I don't think records are supposed to be mutable but whatever
            this(Vector.immutable(c + 0.5, r), new BoundingBox(Vector.mutable((double) c - left, r - down), Vector.immutable((double) left + 1 + right, up + 1 + down)), name);
        }
    }

    public record Trigger(BoundingBox activationArea, int id) {
        public static final int LUGGAGE_CP0 = 0, WIN = 1; // cp0 haha
        public Trigger(int r, int c, int left, int right, int up, int down, int id) {
            // its mutable and I don't think records are supposed to be mutable but whatever
            this(new BoundingBox(Vector.mutable((double) c - left, r - down), Vector.immutable((double) left + 1 + right, up + 1 + down)), id);
        }
        public void activate(Player player) { // should this be called the other way, where Trigger is ticked?
            switch (id) {
                case LUGGAGE_CP0 -> luggageCP0(player);
                case WIN -> win(player);
                default -> throw new IllegalStateException("Unexpected trigger id: " + id);
            }
        }

        private void luggageCP0(Player player) {
            if (!player.isHolding()) return;
            player.setPosition(player.getRoom().getFirstCheckpoint().pos());
            player.getGame().teleportCamera();
            player.getGame().hasLuggage = true;
            Assets.sound("bump").play();
        }

        private void win(Player player) {
            if (!player.isHolding()) return;
            player.getGame().end();
        }
    }

    public static class CameraLock implements Renderable {
        protected final int left, right, top, bottom;
        protected final BoundingBox activationArea;

        public CameraLock(Args args, int r, int c) {
            this(c, r, args.nextInt(0), args.nextInt(0), args.nextInt(5), args.nextInt(3), args.nextInt(5), args.nextInt(3));
        }
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

        public Vector.ImmutableDouble restrict(Vector.Double pos) {
            return Vector.immutable(clamp(pos.getX(), left, right), clamp(pos.getY(), bottom, top));
        }
        public boolean isActive(Vector.Double pos) {
            return activationArea.contains(pos);
        }

        @Override
        public void render(Canvas canvas) {
            canvas.renderDebugBoundingBox(activationArea, Color.ORANGE);
        }
    }
}
