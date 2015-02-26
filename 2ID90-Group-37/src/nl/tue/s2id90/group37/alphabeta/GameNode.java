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

    private Move move;

    GameState getGameState() {
        return null;
    }

    Move getBestMove() {
        return move;
    }

    void setBestMove(Move move) {
        this.move = move;
    }
}
