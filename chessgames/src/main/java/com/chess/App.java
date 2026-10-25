package com.chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.chess.model.PieceColor.BLACK;
import static com.chess.model.PieceColor.WHITE;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.chess.engine.ChessRules;
import com.chess.engine.MoveGenerator;
import com.chess.model.Bishop;
import com.chess.model.Board;
import com.chess.model.King;
import com.chess.model.Knight;
import com.chess.model.Move;
import com.chess.model.Pawn;
import com.chess.model.Piece;
import com.chess.model.PieceColor;
import com.chess.model.PieceType;
import com.chess.model.Position;
import com.chess.model.Rook;

/**
 * JavaFX App
 */
public class App {

    // private static Scene scene;

    // @Override
    // public void start(Stage stage) throws IOException {
    // scene = new Scene(loadFXML("primary"), 640, 480);
    // stage.setScene(scene);
    // stage.show();
    // }

    // static void setRoot(String fxml) throws IOException {
    // scene.setRoot(loadFXML(fxml));
    // }

    // private static Parent loadFXML(String fxml) throws IOException {
    // FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml +
    // ".fxml"));
    // return fxmlLoader.load();
    // }

    public static void main(String[] args) {
        // launch();
        // System.out.println(Board.createStandardBoard());

        Board board = new Board();

        // MoveGenerator generator = new MoveGenerator();

        // List<Move> mvoes = generator.generateMoves(board, PieceColor.WHITE);

        // System.out.println("numbevr of move: " + mvoes.size());
        // for(Move move: mvoes){
        // System.out.println(move);
        // }
        System.out.println(generateBoard());

    }

    public static String generateBoard() {
        StringBuilder sb = new StringBuilder();
        Board board = new Board();

        Position pos1 = new Position(0, 4);
        Position pos2 = new Position(7, 4);
        Position rookCheckMate1 = new Position(7, 3);
        Position rookCheckMate2 = new Position(7, 5);

        // Position pos3 = new Position(3, 4);
        King king = new King(PieceColor.WHITE, pos1);
        Rook rook = new Rook(PieceColor.BLACK, pos2);
        // Rook rookPiece = new Rook(BLACK, rookCheckMate1);
        Rook rookPiece2 = new Rook(BLACK, rookCheckMate2);

        // Pawn pawn = new Pawn(PieceColor.BLACK, pos3);
        // Pawn pawn2 = new Pawn(PieceColor.BLACK, pos2);

        // Bishop bishop = new Bishop(PieceColor.BLACK, pos3);

        // board.setPiece(pos1, pawn);
        // board.setPiece(pos2, pawn2);
        board.setPiece(pos1, king);
        board.setPiece(pos2, rook);
        // board.setPiece(rookCheckMate1, rookPiece);
        board.setPiece(rookCheckMate2, rookPiece2);
        
        // board.setPiece(pos2, pawn);
        // board.setPiece(pos3, pawn);
        ChessRules rules = new ChessRules();
        boolean result = rules.isCheckIn(board, WHITE);
        boolean resultsMate = rules.isCheckMate(board, WHITE);
        if(resultsMate){
            // System.out.println("King checked");
            System.out.println("King white checked mate");

        } else {
            System.out.println("King not check mate");
        }
        MoveGenerator generator = new MoveGenerator();
        List<Move> moves = generator.generateMoves(board, PieceColor.WHITE);
        System.out.println("Number of move: " + moves.size());
        List<Move> legalMoves = rules.getLegalMoves(board, WHITE);
        System.out.println("Legal moves: " +legalMoves.size());
        



        //ban co
        int size = 8;
        sb.append("  ");
        for (char col = 'A'; col < 'A' + size; col++) {
            sb.append(col).append(" ");
        }
        sb.append("\n");
        for (int row = 0; row < size; row++) {
            sb.append(row).append(" ");
            for (int col = 0; col < size; col++) {
                // sb.append(". ");
                char c = '.';
                Position pos = new Position(row, col);
                Piece p = board.getPiece(pos);
                if (p != null) {
                    switch (p.getType()) {
                        case PAWN:
                            c = 'P';
                            break;
                        case ROOK:
                            c = 'R';
                            break;
                        case KNIGHT:
                            c = 'N';
                            break;
                        case BISHOP:
                            c = 'B';
                            break;
                        case QUEEN:
                            c = 'Q';
                            break;
                        default:
                            c = 'K';
                            break;
                    }
                    if (p.getColor() == PieceColor.WHITE)
                        c = Character.toLowerCase(c);

                }
                sb.append(c).append(' ');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}