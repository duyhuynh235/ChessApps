package com.chess.engine;

import java.util.ArrayDeque;
import java.util.Deque;

import com.chess.model.GameState;

public class UndoManager {
    private Deque<GameState> undoStack = new ArrayDeque<GameState>();
    private Deque<GameState> redoStack = new ArrayDeque<GameState>();

    public void saveState(GameState state){
        undoStack.push(state.clone());
        redoStack.clear();
    }

    public boolean canUndo(){
        return !undoStack.isEmpty();
    }
    public boolean canRedo(){
        return !redoStack.isEmpty();
    }
    public GameState undo(GameState currentState){
        if(!canUndo()){
            return null;
        }
        redoStack.push(currentState.clone());
        return undoStack.pop();
    }

    public GameState redo(GameState currentState){
        if(!canRedo()){
            return null;
        }
        undoStack.push(currentState.clone());
        return redoStack.pop();
    }

    public void clear(){
        undoStack.clear();
        redoStack.clear();
    }
}
