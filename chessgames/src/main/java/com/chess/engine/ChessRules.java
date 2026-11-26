package com.chess.engine;

import static com.chess.model.PieceType.PAWN;

import java.util.ArrayList;
import java.util.List;

import com.chess.model.Board;
import com.chess.model.GameState;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.model.PieceColor;
import com.chess.model.Position;

public class ChessRules {

    // Kieserm tra vua có bị chiếu ko
    public boolean isCheckIn(Board board, PieceColor color) {
        // Lấy ví trí quân VUA
        Position kingPosition = board.findKingPiece(color);
        if(kingPosition == null) return false;
        return isSquareAttacked(board, kingPosition, color.getPieceColor());
    }

    // keim tra nuoc di hop le
    public List<Move> getLegalMoves(Board board, PieceColor color, GameState state) {
        List<Move> legalMoves = new ArrayList<>();

        // lấy generateMove
        MoveGenerator generator = new MoveGenerator();
        List<Move> allMoves = generator.generateMoves(board, color, state);

        // duyệt toàn bộ allMoves
        for (Move move : allMoves) {
            // clone board để check
            Board cloneBoard = board.clone();
            // thuc hien nuoc di tren clone board
            Piece movingPiece = cloneBoard.getPiece(move.getFrom());
            cloneBoard.setPiece(move.getFrom(), null);
            cloneBoard.setPiece(move.getTo(), movingPiece);

            if(move.isEnPassant()){
                Position capturedPos = new Position(move.getFrom().getRow(), move.getTo().getCol());
                cloneBoard.setPiece(capturedPos, null);
            }

            // kiếm tra
            boolean isChecked = isCheckIn(cloneBoard, color);
            if (!isChecked) {
                legalMoves.add(move);
            }
        }
        // ko chiếu thì giữ lại thành legalMoves
        return legalMoves;
    }

    public boolean isCheckMate(Board board, PieceColor color , GameState state) {
        if(!isCheckIn(board, color)){
            return false;
        }
        return getLegalMoves(board, color, state).isEmpty();
    }

    public boolean isStalemate(Board board, PieceColor color, GameState state){
        if(isCheckIn(board, color)){
            return false;
        }
        return getLegalMoves(board, color,state).isEmpty();
    }

    public boolean isSquareAttacked(Board board, Position square, PieceColor attackedColor){
        MoveGenerator generator = new MoveGenerator();
        List<Move> opponentMoves = generator.generateMoves(board, attackedColor, null);

        for (Move move : opponentMoves) {
            // Tot an cheo, ko tan cong di thang
            if(move.getMoved().getType() == PAWN 
                && move.getFrom().getCol()== move.getTo().getCol()){
                    continue;
            }
            if(move.getTo().equals(square)) return true;
        }
        return false;
    }    
}
