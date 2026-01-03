package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;
import com.cometkaizo.game.event.MousePressedEvent;
import com.cometkaizo.screen.*;
import com.cometkaizo.screen.Canvas;

public class ChessOverlay extends Overlay {
    private Piece[][] pieces = new Piece[8][8]; // row, column (row 0 is at top, col 0 is at left)
    private Cell[][] cells = new Cell[8][8];
    private Piece pickedUp = null;

    public ChessOverlay(GameApp app, String pieces) {
        super(app);
        for (int r = 0; r < 8; r ++) {
            for (int c = 0; c < 8; c ++) {
                cells[r][c] = new Cell(r, c);
            }
        }

        int r = 0;
        for (String row : pieces.lines().toList()) {
            int c = 0;
            for (char p : row.toCharArray()) {
                this.pieces[r][c] = newPiece(p, r, c);
                c ++;
            }
            r ++;
        }
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.renderImage(Assets.texture("gui/chess/board"), canvas.halfWidth(), canvas.halfHeight(), -0.5, -0.5);

        for (var row : pieces) for (var piece : row) if (piece != null) piece.img.render(canvas);
        for (var row : cells) for (var cell : row) cell.render(canvas);
    }

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

    class Piece {
        private Clickable img;
        private int r, c;
        Piece(int r, int c, boolean white, String texturePath) {
            img = new ImageClickable(app, this::pickUp, this::screenX, this::screenY, _ -> 16, _ -> 16,
                    () -> white ? "gui/chess/white/" + texturePath : "gui/chess/black/" + texturePath, -2, -2);
            this.r = r;
            this.c = c;
        }

        private boolean pickUp() {
            if (pickedUp == null) {
                pickedUp = this;
                return true;
            } else return false;
        }

        int screenX(int w) {
            return boardLeft(w) + c * 16;
        }
        int screenY(int h) {
            return boardTop(h) + r * 16 - (pickedUp == this ? 8 : 0);
        }
    }

    class Pawn extends Piece {
        Pawn(int r, int c, boolean white) {
            super(r, c, white, "pawn");
        }
    }
    class Rook extends Piece {
        Rook(int r, int c, boolean white) {
            super(r, c, white, "rook");
        }
    }
    class Knight extends Piece {
        Knight(int r, int c, boolean white) {
            super(r, c, white, "knight");
        }
    }
    class Bishop extends Piece {
        Bishop(int r, int c, boolean white) {
            super(r, c, white, "bishop");
        }
    }
    class King extends Piece {
        King(int r, int c, boolean white) {
            super(r, c, white, "king");
        }
    }
    class Queen extends Piece {
        Queen(int r, int c, boolean white) {
            super(r, c, white, "queen");
        }
    }



    public class Cell extends Clickable {
        private int r, c;
        public Cell(int r, int c) {
            super(ChessOverlay.this.app, () -> {
                movePickedUpPiece(r, c);
            }, null, null, _ -> 16, _ -> 16);
            x = this::screenX;
            y = this::screenY;
            this.r = r;
            this.c = c;
        }
        public int screenX(int w) {
            return boardLeft(w) + c * 16;
        }
        public int screenY(int h) {
            return boardTop(h) + r * 16;
        }
    }

    private void movePickedUpPiece(int r, int c) {
        if (pickedUp == null) return;
        pieces[pickedUp.r][pickedUp.c] = null;
        pieces[r][c] = pickedUp;
        pickedUp.r = r;
        pickedUp.c = c;
        pickedUp = null;
    }

    private int boardLeft(int screenWidth) {
        return screenWidth / 2 - 64;
    }
    private int boardTop(int screenHeight) {
        return screenHeight / 2 - 64;
    }

    @Override
    public void tick() {
        super.tick();
        for (var row : pieces) for (var piece : row) if (piece != null) piece.img.tick();
        for (var row : cells) for (var cell : row) cell.tick();
    }

    @Override
    protected void onClick(MousePressedEvent click) {
        super.onClick(click);
        for (var row : pieces) for (var piece : row) {
            if (piece != null && piece.img.onClick(click)) return; // if any piece is clicked, no cell should be clicked
        }
        for (var row : cells) for (var cell : row) cell.onClick(click);
    }
}
