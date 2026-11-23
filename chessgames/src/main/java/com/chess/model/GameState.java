package com.chess.model;

import static com.chess.model.PieceColor.WHITE;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;

public class GameState {
    private final Board board;
    private PieceColor currentTurnColor;

    // quyen nhap thanh: 4 ô(trắng-xe hậu, trắng-xe vua, đen-xe hậu, đen-xe vua)
    private final boolean[] castlingRights = new boolean[4];
    private static final int WHITE_QUEEN_SIDE = 0;
    private static final int WHITE_KING_SIDE = 1;
    private static final int BLACK_QUEEN_SIDE = 2;
    private static final int BLACK_KING_SIDE = 3;

    private Position enPassantTarget; // null neu ko co
    private int halfMoveClock; // cho luat 50 nuoc di
    private final List<Move> moveHistory = new ArrayList<>();

    public GameState(Board board) {
        this.board = board;
    }

    public static GameState newGame() {
        Board initialBoard = Board.createStandardBoard();
        GameState state = new GameState(initialBoard);
        state.setCurrentTurnColor(WHITE);
        for (int i = 0; i < 4; i++) {
            state.castlingRights[i] = true;
        }
        return state;
    }

    /**
     * Create a state copy for simulating moves without changing the original game.
     * CompletedL board and current turn.
     * In progess: castling rights halfmove clock
     * Remaining: en passant target and move history
     */
    public GameState clone() {
        Board copyBoard = board.clone();
        GameState copy = new GameState(copyBoard);
        copy.setCurrentTurnColor(currentTurnColor);
        for (int i = 0; i < castlingRights.length; i++) {
            // Copy each move before adding it to the copied history
            copy.castlingRights[i] = this.castlingRights[i];
        }
        copy.setHalfMoveClock(this.halfMoveClock);

        // Check enpassantTarget
        if (enPassantTarget != null) {
            // Get position and copied it
            Position copiedPosition = new Position(this.enPassantTarget.getRow(), this.enPassantTarget.getCol());
            copy.setEnPassantTarget(copiedPosition);
        }
        for (Move moves : this.moveHistory) {
            // Get position From
            Position originalFrom = moves.getFrom();
            Position copiedFrom = new Position(originalFrom.getRow(), originalFrom.getCol());
            // Get position to
            Position originalTo = moves.getTo();
            Position copiedTo = new Position(originalTo.getRow(), originalTo.getCol());
            // Get piece moved
            Piece originalMoved = moves.getMoved();
            Position originalPosition = originalMoved.getPosition();
            Position copiedPosition = new Position(originalPosition.getRow(), originalPosition.getCol());

            Piece copiedMoved = Board.createStandardPiece(originalMoved.getColor(), originalMoved.getType(),
                    copiedPosition);

            // piece capture
            Piece originalCaptured = moves.getCaptured();
            Piece copiedCaptured = null;
            if (originalCaptured != null) {
                Position originalCapturedPosition = originalCaptured.getPosition();
                Position copiedCapturePosition = new Position(originalCapturedPosition.getRow(),
                        originalCapturedPosition.getCol());
                copiedCaptured = Board.createStandardPiece(originalCaptured.getColor(), originalCaptured.getType(),
                        copiedCapturePosition);
                Move copiedMove = new Move(
                        copiedFrom,
                        copiedTo,
                        copiedMoved,
                        copiedCaptured,
                        moves.isEnPassant(),
                        moves.getPromotion(),
                        moves.isKingSideCastle(),
                        moves.isQueenSideCastle());
                copy.moveHistory.add(copiedMove);
            }

        }
        return copy;
    }

    public Board getBoard() {
        return board;
    }

    public PieceColor getCurrentTurnColor() {
        return currentTurnColor;
    }

    public void setCurrentTurnColor(PieceColor currentTurnColor) {
        this.currentTurnColor = currentTurnColor;
    }

    public boolean[] getCastlingRights(PieceColor color) {
        return castlingRights;
    }

    public boolean canCastleKingSide(PieceColor color) {
        if (color == WHITE) {
            return castlingRights[WHITE_KING_SIDE];
        } else {
            return castlingRights[BLACK_KING_SIDE];
        }
    }

    public boolean canCastleQueenSide(PieceColor color) {
        if (color == WHITE) {
            return castlingRights[WHITE_QUEEN_SIDE];
        } else {
            return castlingRights[BLACK_QUEEN_SIDE];
        }
    }

    public void revokeKingSideCastling(PieceColor color) {
        if (color == WHITE) {
            castlingRights[WHITE_KING_SIDE] = false;
        } else {
            castlingRights[BLACK_KING_SIDE] = false;
        }
    }

    public void revokeQueenSideCastling(PieceColor color) {
        if (color == WHITE) {
            castlingRights[WHITE_QUEEN_SIDE] = false;
        } else {
            castlingRights[BLACK_QUEEN_SIDE] = false;
        }
    }

    public void revokeAllCastlingRights(PieceColor color) {
        revokeKingSideCastling(color);
        revokeQueenSideCastling(color);
    }

    public Position getEnPassantTarget() {
        return enPassantTarget;
    }

    public void setEnPassantTarget(Position enPassantTarget) {
        this.enPassantTarget = enPassantTarget;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

}