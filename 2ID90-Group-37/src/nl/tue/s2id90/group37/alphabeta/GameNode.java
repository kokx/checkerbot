/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group37.alphabeta;

import nl.tue.s2id90.game.GameState;
import org10x10.dam.game.Move;

/**
 *
 * @author pieter
 */
public class GameNode {

    private Move bestMove;
    private Move move;
    private GameState state;
    private GameNode prev;

    public GameNode(GameState state) {
        this.state = state;
        this.prev = null;
        this.move = null;
    }

    public GameNode(GameState state, GameNode prev, Move move) {
        this.state = state;
        this.prev = prev;
        this.move = move;
    }

    public GameState getGameState() {
        return state;
    }

    public void setGameState(GameState state) {
        this.state = state;
    }

    public Move getBestMove() {
        return bestMove;
    }

    public void setBestMove(Move move) {
        this.bestMove = move;
    }

    public Move getMove() {
        return move;
    }

    public GameNode getPrev() {
        return this.prev;
    }
}
