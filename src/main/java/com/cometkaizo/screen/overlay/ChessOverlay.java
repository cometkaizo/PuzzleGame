package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.Assets;
import com.cometkaizo.screen.Canvas;
import com.cometkaizo.screen.Clickable;
import com.cometkaizo.screen.ImageClickable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntUnaryOperator;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-20
 * Description: Screen overlay for chess
 */
public class ChessOverlay extends Overlay {
    private final String originalPieces;
    private final Piece[][] pieces = new Piece[8][8]; // row, column (row 0 is at top, col 0 is at left)
    private final Cell[][] cells = new Cell[8][8];
    private Piece pickedUp = null;
    private final Clickable resetButton = new ImageClickable(app, this::resetPieces, w -> w/2 + 84, h -> h/2 - 14, _ -> 23, _ -> 23, () -> "gui/chess/reset_button", -2, -2);

    /// Creates a new overlay
    public ChessOverlay(GameApp app, String pieces) {
        super(app);
        for (int r = 0; r < 8; r ++) {
            for (int c = 0; c < 8; c ++) {
                cells[r][c] = new Cell(r, c);
            }
        }

        originalPieces = pieces;
        resetPieces();
    }

    /// Resets the pieces to the original layout
    protected void resetPieces() {
        pickedUp = null;
        int r = 0;
        for (String row : originalPieces.lines().toList()) {
            int c = 0;
            for (char p : row.toCharArray()) {
                this.pieces[r][c] = newPiece(p, r, c);
                c ++;
            }
            r ++;
        }
        // calculate all movable cells for pieces after all of them have been placed on the board
        recalculatePieceMovableCells();
    }

    /// Recalculates pieces' movable cells
    private void recalculatePieceMovableCells() {
        for (var row : this.pieces) for (var piece : row) if (piece != null && !(piece instanceof King)) piece.recalculateMovableCells();
        for (var row : this.pieces) for (var piece : row) if (piece instanceof King) piece.recalculateMovableCells();
    }

    /// Renders the overlay to the screen
    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/chess/board"), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);

        for (var row : cells) for (var cell : row) cell.render(canvas);
        for (var row : pieces) for (var piece : row) if (piece != null) piece.img.render(canvas);

        resetButton.render(canvas);
    }

    /// Creates a new piece with the given name at the given location
    Piece newPiece(char name, int r, int c) {
        boolean white = Character.isUpperCase(name);
        return switch (Character.toLowerCase(name)) {
            case 'p' -> new Pawn(r, c, white);
            case 'r' -> new Rook(r, c, white);
            case 'n' -> new Knight(r, c, white);
            case 'b' -> new Bishop(r, c, white);
            case 'k' -> new King(r, c, white);
            case 'q' -> new Queen(r, c, white);
            case ' ' -> null;
            default -> throw new IllegalArgumentException("Unknown piece: " + name);
        };
    }

    /// Returns the cell at the given location, or null if there is none
    protected Cell cellAt(int r, int c) {
        return (r >= 0 && r < 8 && c >= 0 && c < 8) ? cells[r][c] : null;
    }
    /// Returns the piece at the given location, or null if there is none
    protected Piece pieceAt(int r, int c) {
        return (r >= 0 && r < 8 && c >= 0 && c < 8) ? pieces[r][c] : null;
    }

    // Note: not all rules of chess are implemented here, simply because it is not necessary for the purpose of the puzzles

    /// A chess piece
    public abstract class Piece {
        protected final Clickable img;
        protected final boolean white;
        protected int r, c;
        private Set<Cell> movableCells = Set.of(), attackableCells = Set.of();

        /// Creates a new chess piece
        public Piece(int r, int c, boolean white, String texturePath) {
            this.white = white;
            img = new ImageClickable(app, this::pickUp, this::screenX, this::screenY, _ -> 16, _ -> 16,
                    () -> white ? "gui/chess/white/" + texturePath : "gui/chess/black/" + texturePath, -2, -2) {
                @Override protected boolean isOutlined() {
                    return (super.isOutlined() && pickedUp == null) || pickedUp == Piece.this;
                }
            };
            setPos(r, c);
        }

        /// Recalculates the cells that this piece can move to
        public void recalculateMovableCells() {
            var movableCells = new HashSet<Cell>();
            calculateMovableCells(movableCells);
            movableCells.remove(cellAt(r, c));
            movableCells.removeIf(Objects::isNull);
            this.attackableCells = new HashSet<>(movableCells);
            movableCells.removeIf(c -> pieceAt(c.r, c.c) instanceof Piece p && p.white == white); // cannot capture piece of same side

            this.movableCells = movableCells;
        }

        /// calculates the movable cells and stores them into the given set
        protected abstract void calculateMovableCells(Set<Cell> movable);
        /// Returns whether the given cell is attacked by this piece
        protected boolean canAttackCell(Cell cell) {
            return cell != null && attackableCells.contains(cell);
        }

        /// Calculates the movable cells in a given line
        protected final void calculateMovableCellsInLine(Set<Cell> movable, IntUnaryOperator r, IntUnaryOperator c) {
            for (int i = 1; i < 8; i ++) {
                var cell = cellAt(r.applyAsInt(i), c.applyAsInt(i));
                if (cell == null) continue;
                var piece = pieceAt(cell.r, cell.c);
                movable.add(cell);
                if (piece != null) break;
            }
        }

        /// Picks this piece up
        private boolean pickUp() {
            if (pickedUp == null && canPickUp(this)) {
                pickedUp = this;
                return true;
            } else return false;
        }

        /// Gets the screen x
        int screenX(int w) {
            return boardLeft(w) + c * 16;
        }
        /// Gets the screen y
        int screenY(int h) {
            return boardTop(h) + r * 16 - (pickedUp == this ? 8 : 0);
        }

        /// Sets the position of this chess piece
        public void setPos(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    /// returns whether the given piece can be picked up
    protected boolean canPickUp(Piece piece) {
        return true;
    }

    /// A pawn
    public class Pawn extends Piece {
        public Pawn(int r, int c, boolean white) {
            super(r, c, white, "pawn");
        }

        /// calculates the movable cells and stores them into the given set
        @Override
        protected void calculateMovableCells(Set<Cell> movable) {
            int dr = white ? -1 : 1;
            if (pieceAt(r + dr, c) == null) movable.add(cellAt(r + dr, c));
            // captures
            if (pieceAt(r + dr, c + 1) instanceof Piece p && p.white != white) movable.add(cellAt(r + dr, c + 1));
            if (pieceAt(r + dr, c - 1) instanceof Piece p && p.white != white) movable.add(cellAt(r + dr, c - 1));
        }

        /// Returns whether the given cell is attacked by this piece
        @Override
        protected boolean canAttackCell(Cell cell) {
            int dr = white ? -1 : 1;
            return cell != null && cell.r == r + dr && (cell.c == c + 1 || cell.c == c - 1);
        }
    }
    /// A rook
    public class Rook extends Piece {
        public Rook(int r, int c, boolean white) {
            super(r, c, white, "rook");
        }

        /// calculates the movable cells and stores them into the given set
        @Override
        protected void calculateMovableCells(Set<Cell> movable) {
            calculateMovableCellsInLine(movable, i -> r + i, i -> c);
            calculateMovableCellsInLine(movable, i -> r - i, i -> c);
            calculateMovableCellsInLine(movable, i -> r, i -> c + i);
            calculateMovableCellsInLine(movable, i -> r, i -> c - i);
        }
    }
    /// A knight
    public class Knight extends Piece {
        public Knight(int r, int c, boolean white) {
            super(r, c, white, "knight");
        }

        /// calculates the movable cells and stores them into the given set
        @Override
        protected void calculateMovableCells(Set<Cell> movable) {
            movable.add(cellAt(r + 2, c + 1));
            movable.add(cellAt(r + 1, c + 2));
            movable.add(cellAt(r - 2, c + 1));
            movable.add(cellAt(r - 1, c + 2));
            movable.add(cellAt(r + 2, c - 1));
            movable.add(cellAt(r + 1, c - 2));
            movable.add(cellAt(r - 2, c - 1));
            movable.add(cellAt(r - 1, c - 2));
        }
    }
    /// A bishop
    public class Bishop extends Piece {
        public Bishop(int r, int c, boolean white) {
            super(r, c, white, "bishop");
        }

        /// calculates the movable cells and stores them into the given set
        @Override
        protected void calculateMovableCells(Set<Cell> movable) {
            calculateMovableCellsInLine(movable, i -> r + i, i -> c + i);
            calculateMovableCellsInLine(movable, i -> r - i, i -> c + i);
            calculateMovableCellsInLine(movable, i -> r + i, i -> c - i);
            calculateMovableCellsInLine(movable, i -> r - i, i -> c - i);
        }
    }
    /// A king
    public class King extends Piece {
        public King(int r, int c, boolean white) {
            super(r, c, white, "king");
        }

        /// calculates the movable cells and stores them into the given set
        @Override
        protected void calculateMovableCells(Set<Cell> movable) {
            movable.add(cellAt(r + 1, c));
            movable.add(cellAt(r, c));
            movable.add(cellAt(r - 1, c));

            movable.add(cellAt(r + 1, c + 1));
            movable.add(cellAt(r, c + 1));
            movable.add(cellAt(r - 1, c + 1));

            movable.add(cellAt(r + 1, c - 1));
            movable.add(cellAt(r, c - 1));
            movable.add(cellAt(r - 1, c - 1));

            // remove all movable cells which are attacked by a piece of the opposite side
            for (var row : pieces) {
                for (var piece : row) {
                    if (piece == null) continue;
                    if (piece.white != white) movable.removeIf(piece::canAttackCell);
                }
            }
        }

        /// Returns whether the given cell is attacked by this piece
        @Override
        protected boolean canAttackCell(Cell cell) {
            return cell != null && Math.abs(cell.r - r) <= 1 && Math.abs(cell.c - c) <= 1;
        }

    }
    /// A queen
    public class Queen extends Piece {
        public Queen(int r, int c, boolean white) {
            super(r, c, white, "queen");
        }

        /// calculates the movable cells and stores them into the given set
        @Override
        protected void calculateMovableCells(Set<Cell> movable) {
            // rook
            calculateMovableCellsInLine(movable, i -> r + i, i -> c);
            calculateMovableCellsInLine(movable, i -> r - i, i -> c);
            calculateMovableCellsInLine(movable, i -> r, i -> c + i);
            calculateMovableCellsInLine(movable, i -> r, i -> c - i);
            // bishop
            calculateMovableCellsInLine(movable, i -> r + i, i -> c + i);
            calculateMovableCellsInLine(movable, i -> r - i, i -> c + i);
            calculateMovableCellsInLine(movable, i -> r + i, i -> c - i);
            calculateMovableCellsInLine(movable, i -> r - i, i -> c - i);
        }
    }


    /// A single cell on the chess board
    public class Cell extends Clickable {
        private int r, c;
        /// Creates a new cell at the given location
        public Cell(int r, int c) {
            super(ChessOverlay.this.app, () -> {
                movePickedUpPiece(r, c);
            }, null, null, _ -> 16, _ -> 16);
            x = this::screenX;
            y = this::screenY;
            this.r = r;
            this.c = c;
        }
        /// Returns the screen x of this cell
        public int screenX(int w) {
            return boardLeft(w) + c * 16;
        }
        /// Returns the screen y of this cell
        public int screenY(int h) {
            return boardTop(h) + r * 16;
        }

        /// Renders this cell to the screen
        @Override
        public void render(Canvas canvas) {
            super.render(canvas);
            if (pickedUp != null && pickedUp.movableCells.contains(this)) canvas.renderImage(Assets.texture("gui/chess/highlight"), lastX, lastY);
        }
    }

    /// Moves the picked up piece to the given location
    protected void movePickedUpPiece(int r, int c) {
        if (pickedUp == null) return;
        if (!pickedUp.movableCells.contains(cellAt(r, c))) {
            pickedUp = null;
            return;
        }
        movePiece(pickedUp.r, pickedUp.c, r, c);
        pickedUp = null;
    }

    /// Moves a piece from the given starting location to the given ending location
    protected void movePiece(int fromR, int fromC, int toR, int toC) {
        var piece = pieces[fromR][fromC];
        pieces[fromR][fromC] = null;
        pieces[toR][toC] = piece;
        piece.setPos(toR, toC);
        recalculatePieceMovableCells();
    }

    /// returns the left side of the board on the screen
    private int boardLeft(int screenWidth) {
        return screenWidth / 2 - 64;
    }
    /// returns the top of the board on the screen
    private int boardTop(int screenHeight) {
        return screenHeight / 2 - 64;
    }

    /// Ticks this overlay
    @Override
    public void tick() {
        super.tick();
        for (var row : pieces) for (var piece : row) if (piece != null) piece.img.tick();
        for (var row : cells) for (var cell : row) cell.tick();

        resetButton.tick();
    }

    /// Called when the mouse is clicked
    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        for (var row : pieces) for (var piece : row) {
            if (piece != null && piece.img.onClick(click)) return; // if any piece is clicked, no cell should be clicked
        }
        for (var row : cells) for (var cell : row) cell.onClick(click);

        resetButton.onClick(click);
    }
}
