/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group37;

import java.util.List;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.game.GameState;
import org10x10.dam.game.Move;

import nl.tue.s2id90.group37.alphabeta.GameNode;


/**
 *
 * @author pieter
 */
public class SmartPlayer extends DraughtsPlayer {

    private int value = 0;

    public SmartPlayer() {
        super(UninformedPlayer.class.getResource("resources/smiley.png"));
    }

    protected boolean isWhite;

    @Override
    /** @return a good move **/
    public Move getMove(DraughtsState s) {
        isWhite = s.isWhiteToMove();
        GameNode node = new GameNode(s);
        value = alphaBeta(node);
        System.out.println("val1: " + value);
        return node.getBestMove();

    }

    int alphaBeta(GameNode node) {
        return alphaBetaMax(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
    }

    int depthLimit = 8;

    /**
     *
     * @param node
     * @param alpha
     * @param beta
     * @return Integer
     */
    int alphaBetaMax(GameNode node, int alpha, int beta, int depth) {
        GameState state = node.getGameState();
        List<Move> moves = state.getMoves();

        if (moves.isEmpty() || depth >= depthLimit) {
            return evaluate((DraughtsState) state);
        }

        Move bestMove = moves.get(0);

        for (Move move : moves) {
            state.doMove(move);

            // recursive call
            int prevAlpha = alpha;
            alpha = Math.max(alpha, alphaBetaMin(new GameNode(state), alpha, beta, depth + 1));
            if (prevAlpha != alpha) {
                bestMove = move;
            }

            state.undoMove(move);
            if (alpha >= beta) {
                return beta;
            }
        }

        node.setBestMove(bestMove);
        return alpha;
    }

    /**
     *
     * @param node
     * @param alpha
     * @param beta
     * @return Integer
     */
    int alphaBetaMin(GameNode node, int alpha, int beta, int depth) {
        GameState state = node.getGameState();
        List<Move> moves = state.getMoves();

        if (moves.isEmpty() || depth >= depthLimit) {
            return evaluate((DraughtsState) state);
        }

        Move bestMove = moves.get(0);

        for (Move move : moves) {
            state.doMove(move);

            // recursive call
            int prevBeta = beta;
            beta = Math.min(beta, alphaBetaMax(new GameNode(state), alpha, beta, depth + 1));
            if (prevBeta != beta) {
                bestMove = move;
            }

            state.undoMove(move);
            if (beta <= alpha) {
                return alpha;
            }
        }

        node.setBestMove(bestMove);
        return beta;
    }

    public int evaluate(DraughtsState state) {
        int[] pieces = state.getPieces();

        int whiteCount = 0;
        int blackCount = 0;

        for (int piece : pieces) {
            switch (piece) {
                case DraughtsState.WHITEPIECE:
                    whiteCount += 2;
                    break;
                case DraughtsState.WHITEKING:
                    whiteCount += 5;
                    break;
                case DraughtsState.BLACKPIECE:
                    blackCount += 2;
                    break;
                case DraughtsState.BLACKKING:
                    blackCount += 5;
                    break;
            }
        }

        int meCount;
        int opCount;

        if (isWhite) {
            meCount = whiteCount;
            opCount = blackCount;
        } else {
            meCount = blackCount;
            opCount = whiteCount;
        }

        if (meCount == 0) {
            meCount = -50000;
        }
        if (opCount == 0) {
            opCount = -50000;
        }

        return meCount - opCount;
    }

    @Override
    public Integer getValue() {
        System.out.println("val1: " + value);
        return value;
    }
}
