/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group37;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
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
public class HeapPlayer extends DraughtsPlayer {

    public static class Pair {
        private int first;
        private GameNode second;

        public Pair(int first, GameNode second) {
            super();
            this.first = first;
            this.second = second;
        }

        public int getFirst() {
            return first;
        }

        public GameNode getSecond() {
            return second;
        }
    }

    public static class AlphaBetaPair {
        private int first;
        private PriorityQueue<Pair> second;

        public AlphaBetaPair(int first, PriorityQueue<Pair> second) {
            super();
            this.first = first;
            this.second = second;
        }

        public int getFirst() {
            return first;
        }

        public PriorityQueue<Pair> getSecond() {
            return second;
        }
    }

    Comparator<Pair> cmp = new Comparator<Pair>() {
        public int compare(Pair a, Pair b) {
            return Integer.compare(a.first, b.first);
        }
    };

    private int maxPower = 2047;

    private int value = 0;

    protected boolean stopped = false;

    private Random rand = new Random();

    public HeapPlayer() {
        super(UninformedPlayer.class.getResource("resources/shark.png"));
    }

    protected boolean isWhite;

    @Override
    /** @return a good move **/
    public Move getMove(DraughtsState s) {
        stopped = false;
        isWhite = s.isWhiteToMove();
        GameNode node = new GameNode(s);
        value = alphaBeta(node);
        return node.getBestMove();
    }

    public void stop() { stopped = true; }

    int alphaBeta(GameNode node) {
        int val = Integer.MIN_VALUE;
        try {
            depthLimit = 5;
            AlphaBetaPair abp = alphaBetaMax(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            val = abp.getFirst();

            LinkedList<PriorityQueue<Pair>> q = new LinkedList<PriorityQueue<Pair>>();

            q.add(abp.getSecond());

            depthLimit = 4;

            while (!q.isEmpty()) {
                PriorityQueue<Pair> pq = q.poll();
                while (!pq.isEmpty()) {
                    //System.out.println("haha");
                    Pair pp = pq.poll();
                    AlphaBetaPair abp2 = alphaBetaMax(pp.getSecond(), Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
                    q.add(abp2.getSecond());

                    if (abp2.getFirst() > val) {
                        // get the first move for this one
                        for (GameNode nxt = pp.getSecond(); nxt != null; nxt = nxt.getPrev()) {
                            if (nxt.getMove() != null) {
                                node.setBestMove(nxt.getMove());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return val;
        }

        return val;
    }

    int depthLimit = 4;

    /**
     *
     * @param node
     * @param alpha
     * @param beta
     * @return Integer
     */
    AlphaBetaPair alphaBetaMax(GameNode node, int alpha, int beta, int depth) throws Exception {
        if (stopped) {
            throw new Exception("stopped");
        }
        GameState state = node.getGameState();
        List<Move> moves = state.getMoves();

        PriorityQueue<Pair> pq = new PriorityQueue<Pair>(Collections.reverseOrder(cmp));

        if (moves.isEmpty() || (depth >= depthLimit && isQuiet((DraughtsState) state))) {
            pq.add(new Pair(evaluate((DraughtsState) state), node));
            return new AlphaBetaPair(pq.peek().getFirst(), pq);
        }

        Move bestMove = moves.get(0);

        for (Move move : moves) {
            state.doMove(move);

            // recursive call
            int prevAlpha = alpha;
            AlphaBetaPair abp = alphaBetaMin(new GameNode(state.clone(), node, move), alpha, beta, depth + 1);
            alpha = Math.max(alpha, abp.getFirst());
            if (prevAlpha != alpha) {
                bestMove = move;
            }

            // keep the maxPower best results
            while (!abp.getSecond().isEmpty()) {
                Pair pp = abp.getSecond().poll();
                if (pq.isEmpty() || pp.getFirst() > pq.peek().getFirst()) {
                    if (pq.size() > maxPower) {
                        pq.poll();
                    }
                    pq.add(pp);
                }
            }

            state.undoMove(move);
            if (alpha >= beta) {
                return new AlphaBetaPair(beta, pq);
            }
        }

        node.setBestMove(bestMove);
        return new AlphaBetaPair(alpha, pq);
    }

    /**
     *
     * @param node
     * @param alpha
     * @param beta
     * @return Integer
     */
    AlphaBetaPair alphaBetaMin(GameNode node, int alpha, int beta, int depth) throws Exception {
        GameState state = node.getGameState();
        List<Move> moves = state.getMoves();

        PriorityQueue<Pair> pq = new PriorityQueue<Pair>(cmp);

        if (moves.isEmpty() || (depth >= depthLimit && isQuiet((DraughtsState) state))) {
            pq.add(new Pair(evaluate((DraughtsState) state), node));
            return new AlphaBetaPair(pq.peek().getFirst(), pq);
        }

        Move bestMove = moves.get(0);

        for (Move move : moves) {
            state.doMove(move);

            // recursive call
            int prevBeta = beta;
            AlphaBetaPair abp = alphaBetaMax(new GameNode(state.clone(), node, move), alpha, beta, depth + 1);
            beta = Math.min(beta, abp.getFirst());
            if (prevBeta != beta) {
                bestMove = move;
            }

            // keep the maxPower worst results
            while (!abp.getSecond().isEmpty()) {
                Pair pp = abp.getSecond().poll();
                if (pq.isEmpty() || pp.getFirst() < pq.peek().getFirst()) {
                    if (pq.size() > maxPower) {
                        pq.poll();
                    }
                    pq.add(pp);
                }
            }

            state.undoMove(move);
            if (beta <= alpha) {
                return new AlphaBetaPair(alpha, pq);
            }
        }

        node.setBestMove(bestMove);
        return new AlphaBetaPair(beta, pq);
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
