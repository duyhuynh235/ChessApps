package com.chess.engine;

import com.chess.model.Board;
import com.chess.model.GameState;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.model.PieceColor;
import com.chess.model.PieceType;
import com.chess.model.Position;

public class MoveExecutor {

    public Move executeMove(GameState state, Move move) {
        Board board = state.getBoard();

        moveMainPiece(board, move);
        handleSpecialMoves(board, move);
        updateCastlingRights(state, move);
        updateEnPassantTarget(state, move);
        updateClockHistoryAndTurn(state, move);

        return move;
    }

    private void moveMainPiece(Board board, Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        Piece movingPiece = move.getMoved();

        // Moving current pieces
        board.setPiece(from, null);
        board.setPiece(to, movingPiece);
        movingPiece.setPosition(to);
    }

    private void handleSpecialMoves(Board board, Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        Piece movingPiece = move.getMoved();

        if (move.isEnPassant()) {
            // Captured pawn sits on the moving pawn's starting rank, not on the destination square
            board.setPiece(new Position(from.getRow(), to.getCol()), null);
        }

        if (move.isKingSideCastle()) {
            movingRookForCastling(board, from.getRow(), 7, 5);
        } else if (move.isQueenSideCastle()) {
            movingRookForCastling(board, from.getRow(), 0, 3);
        }

        if (move.getPromotion() != null) {
            // Replace the promoted pawn on the destination square with the chosen piece
            Piece promoted = Board.createStandardPiece(movingPiece.getColor(), move.getPromotion(), to);
            board.setPiece(to, promoted);
        }
    }

    private void movingRookForCastling(Board board, int row, int fromCol, int toCol) {
        Position rookFrom = new Position(row, fromCol);
        Position rookTo = new Position(row, toCol);
        Piece rook = board.getPiece(rookFrom);

        board.setPiece(rookFrom, null);
        board.setPiece(rookTo, rook);
        rook.setPosition(rookTo);
    }

    private void updateCastlingRights(GameState state, Move move){
        Piece movingPiece = move.getMoved();
        PieceColor color = movingPiece.getColor();
        Position from = move.getFrom();
        Position to = move.getTo();

        if(movingPiece.getType() == PieceType.KING){
            state.revokeAllCastlingRights(color);
        } else if(movingPiece.getType() == PieceType.ROOK){
            int rootRow = (color == PieceColor.WHITE) ? 7 : 0;
            Position rookFromQueen = new Position(rootRow, 0);
            Position rookFromKing = new Position(rootRow, 7);
            if (from.equals(rookFromQueen)) {
                state.revokeQueenSideCastling(color);
            }
            if (from.equals(rookFromKing)) {
                state.revokeKingSideCastling(color);
            }
        }

        Piece captured = move.getCaptured();
        // Revoke opponent's castling right if their corner rook was captured before moving
        if (captured != null && captured.getType() == PieceType.ROOK) {
            PieceColor oppColor = color.getPieceColor();
            int oppRow = (oppColor == PieceColor.WHITE) ? 7 : 0;
            if (to.equals(new Position(oppRow, 0))) {
                state.revokeQueenSideCastling(oppColor);
            }
            if (to.equals(new Position(oppRow, 7))) {
                state.revokeKingSideCastling(oppColor);
            }
        }
    }

    private void updateEnPassantTarget(GameState state, Move move) {
        Piece movingPiece = move.getMoved();
        Position from = move.getFrom();
        Position to = move.getTo();

        // En passant is only valid on the immediately following turn after a 2-square pawn push
        if (movingPiece.getType() == PieceType.PAWN && Math.abs(to.getRow() - from.getRow()) == 2) {
            int midRow = (from.getRow() + to.getRow()) / 2;
            state.setEnPassantTarget(new Position(midRow, from.getCol()));
        } else {
            state.setEnPassantTarget(null);
        }
    }

    private void updateClockHistoryAndTurn(GameState state, Move move) {
        Piece movingPiece = move.getMoved();

        // Reset 50-move rule clock on pawn moves or captures; otherwise increment
        if (movingPiece.getType() == PieceType.PAWN || move.getCaptured() != null) {
            state.setHalfMoveClock(0);
        } else {
            state.setHalfMoveClock(state.getHalfMoveClock() + 1);
        }

        state.getMoveHistory().add(move);
        state.setCurrentTurnColor(state.getCurrentTurnColor().getPieceColor());
    }
}
