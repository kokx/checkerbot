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

    private static final int map[] = {
        0,
        130, 130, 130, 130, 130,
        120, 120, 120, 120, 120,
        100, 100, 100, 100, 100,
         80, 100, 110, 110,  90,
         90, 110, 120, 110,  80,
         80, 110, 120, 110,  90,
         90, 110, 110, 100,  80,
        100, 100, 100, 100, 100,
        120, 120, 120, 120, 120,
        130, 130, 130, 130, 130
    };

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
        if (state.getMoves().isEmpty()) {
            if (isWhite == state.isWhiteToMove()) {
                return Integer.MIN_VALUE + 5000;
            } else {
                return Integer.MAX_VALUE - 5000;
            }
        }
        int[] pieces = state.getPieces();

        int whiteCount = 0;
        int blackCount = 0;

        for (int i = 0; i < pieces.length; i++) {
            int piece = pieces[i];
            //int mul = (int) (hitsToMul(i, pieces) * map[i]);
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

        return (meCount - opCount) + ((int) (Math.random() * 10));
    }

    private double hitsToMul(int i, int[] pieces) {
        int hits = calculateHits(i, pieces);

        switch (hits) {
            case 1:
                return 0.9;
            case 2:
                return 0.75;
            case 0:
            default:
                return 1;
        }
    }

    private int calculateHits(int i, int[] pieces) {
        if (i <= 6 || i >= 45 || (i % 10) == 5 || (i % 10) == 6 || pieces[i] == DraughtsState.EMPTY) {
            return 0;
        }

        int hits = 0;
        if ((i-1) % 10 >= 5) {
            // see (i-6, i+5), (i-5, i+4)
            int x = getColor(pieces[i]);
            int x1 = getColor(pieces[i-6]);
            int x2 = getColor(pieces[i+5]);
            if ((x1 == 0 && x2 == 0) || (x != x1 && x1 != 0 && x2 == 0) || (x != x2 && x2 != 0 && x1 == 0)) {
                hits++;
            }

            x1 = getColor(pieces[i-5]);
            x2 = getColor(pieces[i+4]);
            if ((x1 == 0 && x2 == 0) || (x != x1 && x1 != 0 && x2 == 0) || (x != x2 && x2 != 0 && x1 == 0)) {
                hits++;
            }
        } else {
            // see (i-5, i+6), (i-4, i+5)
            int x = getColor(pieces[i]);
            int x1 = getColor(pieces[i-5]);
            int x2 = getColor(pieces[i+6]);
            if ((x1 == 0 && x2 == 0) || (x != x1 && x1 != 0 && x2 == 0) || (x != x2 && x2 != 0 && x1 == 0)) {
                hits++;
            }
            x1 = getColor(pieces[i-4]);
            x2 = getColor(pieces[i+5]);
            if ((x1 == 0 && x2 == 0) || (x != x1 && x1 != 0 && x2 == 0) || (x != x2 && x2 != 0 && x1 == 0)) {
                hits++;
            }
        }
        return hits;
    }

    private int getColor(int piece) {
        // 0 = empty
        // 1 = black
        // -1 = white
        if (piece == DraughtsState.EMPTY) {
            return 0;
        }
        if (piece == DraughtsState.BLACKKING || piece == DraughtsState.BLACKPIECE) {
            return 1;
        }
        return -1;
    }

    @Override
    public Integer getValue() {
        System.out.println("val1: " + value);
        return value;
    }
}
