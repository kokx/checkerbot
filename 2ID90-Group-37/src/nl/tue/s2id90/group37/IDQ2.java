/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group37;

import java.util.List;
import java.util.Random;
import nl.tue.s2id90.draughts.DraughtsState;
import nl.tue.s2id90.draughts.player.DraughtsPlayer;
import nl.tue.s2id90.game.GameState;
import org10x10.dam.game.Move;

import nl.tue.s2id90.group37.alphabeta.GameNode;


/**
 *
 * @author pieter
 */
public class IDQ2 extends DraughtsPlayer {

    private int value = 0;

    Random rand = new Random();

    public IDQ2() {
        super(UninformedPlayer.class.getResource("resources/shark.png"));
    }

    protected boolean isWhite;

    protected boolean stopped = false;

    @Override
    /** @return a good move **/
    public Move getMove(DraughtsState s) {
        isWhite = s.isWhiteToMove();
        GameNode node = new GameNode(s);
        stopped = false;
        try {
            for (int i = 2; true; i++) {
                depthLimit = i;
                value = alphaBeta(node);
            }
        } catch (Exception e) {
            stopped = false;
        }
        return node.getBestMove();
    }

    public void stop() { stopped = true; }

    int alphaBeta(GameNode node) throws Exception {
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
    int alphaBetaMax(GameNode node, int alpha, int beta, int depth) throws Exception {
        if (stopped) {
            throw new Exception("stopped");
        }

        GameState state = node.getGameState();
        List<Move> moves = state.getMoves();

        if (moves.isEmpty() || (depth >= depthLimit && isQuiet((DraughtsState) state))) {
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
    int alphaBetaMin(GameNode node, int alpha, int beta, int depth) throws Exception {
        GameState state = node.getGameState();
        List<Move> moves = state.getMoves();

        if (moves.isEmpty() || (depth >= depthLimit && isQuiet((DraughtsState) state))) {
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

    protected boolean isQuiet(DraughtsState state) {
        return state.getMoves().isEmpty() || !state.getMoves().get(0).isCapture();
    }

    public int evaluate(DraughtsState state) {
        int[] pieces = state.getPieces();

        int whiteCount = 0;
        int blackCount = 0;

        for (int piece : pieces) {
            int mul = 100;

            switch (piece) {
                case DraughtsState.WHITEPIECE:
                    whiteCount += 2 * mul;
                    break;
                case DraughtsState.WHITEKING:
                    whiteCount += 5 * mul;
                    break;
                case DraughtsState.BLACKPIECE:
                    blackCount += 2 * mul;
                    break;
                case DraughtsState.BLACKKING:
                    blackCount += 5 * mul;
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
            return Integer.MIN_VALUE + 5000;
        }
        if (opCount == 0) {
            return Integer.MAX_VALUE - 5000;
        }

        meCount *= meCount / opCount;

        return (meCount - opCount) + rand.nextInt(10);
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
