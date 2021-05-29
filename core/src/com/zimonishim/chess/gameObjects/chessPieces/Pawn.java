package com.zimonishim.chess.gameObjects.chessPieces;

import com.zimonishim.chess.IChessBoardCallback;
import com.zimonishim.chess.Players;
import com.zimonishim.chess.gameObjects.ChessField;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.zimonishim.chess.util.FilePathHandler.chessPieceTexturesPath;

public class Pawn extends ChessPiece {

    private boolean isFirstMove = true;

    public Pawn(ChessField chessField, IChessBoardCallback chessBoardCallback, Players player) {
        super(chessField, chessBoardCallback, player);
    }

    @Override
    protected String getPlayerTexture(Players player) {
        if (player == Players.WHITE){
            return chessPieceTexturesPath + "/white_pawn.png";
        } else {
            return chessPieceTexturesPath + "/black_pawn.png";
        }
    }

    @Override
    public Set<int[]> getPossibleMoves() {
        Set<int[]> set = new HashSet<>();

        int x = chessField.getPos()[0];
        int y = chessField.getPos()[1];

        if (player == Players.WHITE) {
            ChessField oneUp = chessField.getChessField(chessFields, y + 1, x);
            if ((oneUp != null && oneUp.getChessPiece() == null)) {
                set.add(new int[]{0, 1});
            }
            // diagonal hitting of other player pieces
            ChessField left = chessField.getChessField(chessFields, y + 1, x - 1);
            if ((left != null && left.getChessPiece() != null && left.getChessPiece().getPlayer() != player)) {
                set.add(new int[]{-1, 1});
            }
            ChessField right = chessField.getChessField(chessFields, y + 1, x + 1);
            if ((right != null && right.getChessPiece() != null && right.getChessPiece().getPlayer() != player)) {
                set.add(new int[]{1, 1});
            }
        } else {
            ChessField oneDown = chessField.getChessField(chessFields, y - 1, x);
            if ((oneDown != null && oneDown.getChessPiece() == null)) {
                set.add(new int[]{0, -1});
            }
            // diagonal hitting of other player pieces
            ChessField left = chessField.getChessField(chessFields, y + 1, x - 1);
            if ((left != null && left.getChessPiece() != null && left.getChessPiece().getPlayer() != player)) {
                set.add(new int[]{-1, -1});
            }
            ChessField right = chessField.getChessField(chessFields, y + 1, x + 1);
            if ((right != null && right.getChessPiece() != null && right.getChessPiece().getPlayer() != player)) {
                set.add(new int[]{1, -1});
            }
        }

        if (isFirstMove){
            if (player == Players.WHITE) {
                ChessField twoUp = chessField.getChessField(chessFields, y + 2, x);
                if ((twoUp != null && twoUp.getChessPiece() == null)) {
                    set.add(new int[]{0, 2});
                }
            } else {
                ChessField twoDown = chessField.getChessField(chessFields, y - 2, x);
                if ((twoDown != null && twoDown.getChessPiece() == null)) {
                    set.add(new int[]{0, -2});
                }
            }
        }

        return set;
    }

    @Override
    public void onMove() {
        this.isFirstMove = false;
    }
}