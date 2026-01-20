package com.cometkaizo.screen.overlay;

import com.cometkaizo.app.GameApp;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Andy Wang
 * Date Modified: 2026-01-20
 * Description: Screen overlay for auto chess
 */
public class AutoChessOverlay extends ChessOverlay {
    private final Map<Move, Move> autoMoves = new HashMap<>();
    private long autoMoveTime = -1;
    private Move nextAutoMove;

    /// Creates a new overlay
    public AutoChessOverlay(GameApp app, String pieces) {
        super(app, pieces);
        // each auto move on the right is a reaction to the player move on the left
        putMove(new Move("a3", "a4"), new Move("c3", "c4"));
        putMove(new Move("a4", "a5"), new Move("c4", "c5"));
        putMove(new Move("a5", "a6"), new Move("c5", "c6"));
        putMove(new Move("a6", "a7"), new Move("c6", "b5"));
        putMove(new Move("a7", "b8"), new Move("b5", "a6"));
        putMove(new Move("b8", "c7"), new Move("a6", "b5"));
        putMove(new Move("c7", "d6"), new Move("b5", "c4"));
        putMove(new Move("d6", "e5"), new Move("c4", "d3"));
        putMove(new Move("e5", "f4"), new Move("h4", "d8"));
        putMove(new Move("f4", "g3"), new Move("d8", "h4"));
        putMove(new Move("g3", "h4"), new Move("h1", "g2"));
        putMove(new Move("h4", "h5"), new Move("g2", "g3"));
        putMove(new Move("h5", "h6"), new Move("g3", "g4"));
        putMove(new Move("h6", "h7"), new Move("g4", "g5"));
        putMove(new Move("h7", "h8"), new Move("g5", "g6"));
    }

    /// adds the moves and their inverses in case the player goes backwards
    private void putMove(Move playerMove, Move autoMove) {
        autoMoves.put(playerMove, autoMove);
        autoMoves.put(playerMove.inverse(), autoMove.inverse());
    }

    /// Resets the pieces to the original layout
    @Override
    protected void resetPieces() {
        super.resetPieces();
        autoMoveTime = -1;
        nextAutoMove = null;
    }

    /// Ticks this screen overlay
    @Override
    public void tick() {
        super.tick();
        if (autoMoveTime == 0) {
            movePiece(nextAutoMove.fromR, nextAutoMove.fromC, nextAutoMove.toR, nextAutoMove.toC);
        }
        if (autoMoveTime >= 0) autoMoveTime --;
    }

    /// moves the piece
    @Override
    protected void movePiece(int fromR, int fromC, int toR, int toC) {
        var piece = pieceAt(fromR, fromC);
        if (piece == null) return;

        if (piece.white) {
            autoMoveTime = 10; // auto move will be made this many ticks later
            var forwardMove = autoMoves.get(new Move(fromR, fromC, toR, toC));
            var backwardMove = autoMoves.get(new Move(toR, toC, fromR, fromC));

            if (forwardMove != null)
                nextAutoMove = forwardMove;
            else
                nextAutoMove = backwardMove;
        }

        super.movePiece(fromR, fromC, toR, toC);
    }

    /// returns whether the given piece can be picked up
    @Override
    protected boolean canPickUp(Piece piece) {
        return piece.white && autoMoveTime == -1; // can only pick up white pieces, when not auto-moving
    }

    /// Represents a single move from a coordinate on the board to another
    public record Move(int fromR, int fromC, int toR, int toC) {
        /// Constructs a new move using chess cell notation
        public Move(String from, String to) {
            this(8 - Integer.parseInt(from.substring(1)), from.charAt(0) - 'a',
                    8 - Integer.parseInt(to.substring(1)), to.charAt(0) - 'a');
        }

        /// Returns the same move but backwards
        public Move inverse() {
            return new Move(toR, toC, fromR, fromC);
        }
    }
}
