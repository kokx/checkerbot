package nl.tue.s2id90.contest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.options.addpluginsfrom.OptionReportAfter;
import net.xeoh.plugins.base.util.PluginManagerUtil;
import net.xeoh.plugins.base.util.uri.ClassURI;
import nl.tue.s2id90.contest.util.Identity;
import nl.tue.s2id90.contest.util.SearchTask;
import nl.tue.s2id90.contest.util.TimedSearchTask;
import nl.tue.s2id90.game.Game;
import nl.tue.s2id90.game.Game.Result;
import nl.tue.s2id90.game.GameState;
import nl.tue.s2id90.game.Player;
import nl.tue.win.util.Timer;

/**
 *
 * @author huub
 * @param <Competitor> Player
 * @param <P> PlayerPlugin<P>
 * @param <M> Move
 * @param <S> GameState<M>
 */
public class CompetitionGUI<Competitor extends Player<M,S>, P extends PlayerProvider<Competitor>, M, S extends GameState<M>>
    extends javax.swing.JFrame implements GameGuiListener<S,M> {
    private static final Logger LOG = Logger.getLogger(CompetitionGUI.class.getName());


    private List<Game> schedule;

    protected GameGUI<S,Competitor,M> gameGUI;
    private final Predicate<Plugin> selector;
    private final String[] pluginFolders;
    protected Game currentGame=null;     // reference to current game, if this reference is null, there is no game going on
    protected List<CompetitionListener<M>> listeners = new ArrayList<>();

    /**
     * Creates new form CompetitionGUI
     * @param selector predicate that only results in true if the argument plugin is a suitable plugin
     * @param gameGUI
     */
    public CompetitionGUI(Predicate<Plugin> selector, String[] pluginFolders) {
        this.selector = selector;
        this.pluginFolders = pluginFolders;
    }

    public void initComponents(GameGUI<S,Competitor,M> gameGUI) {
        this.gameGUI = gameGUI;
        initComponents();


        JPanel boardPanel = gameGUI.getBoardPanel();
        boardContainerPanel.add(boardPanel,BorderLayout.CENTER);

        List<? extends JComponent> tabs = gameGUI.getPanels();
        for(JComponent tab : tabs) {
            tabbedPane.add(tab);
        }

        tabbedPane.remove(rankingPanel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        boardContainerPanel = new JPanel();
        JPanel jPanel2 = new JPanel();
        tabbedPane = new JTabbedPane();
        gamesPanel = new JPanel();
        JScrollPane jScrollPane1 = new JScrollPane();
        gamesTable = new JTable();
        JPanel jPanel3 = new JPanel();
        createScheduleButton = new JButton();
        startGameButton = new JButton();
        stopGameButton = new JButton();
        timeSlider = new JSlider();
        rankingPanel = new JPanel();
        jScrollPane2 = new JScrollPane();
        rankingTable = new JTable();
        JPanel jPanel7 = new JPanel();
        statusLabel = new JLabel();
        JPanel jPanel5 = new JPanel();
        blackPanel = new JPanel();
        blackLabel = new JLabel();
        JPanel jPanel6 = new JPanel();
        JLabel jLabel1 = new JLabel();
        blackValueLabel = new JLabel();
        whitePanel = new JPanel();
        whiteLabel = new JLabel();
        JPanel jPanel8 = new JPanel();
        JLabel jLabel3 = new JLabel();
        whiteValueLabel = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("ACT: AI Competition Tool 0.30");
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        boardContainerPanel.setPreferredSize(new Dimension(450, 450));
        boardContainerPanel.setLayout(new BorderLayout());
        getContentPane().add(boardContainerPanel, BorderLayout.CENTER);

        jPanel2.setPreferredSize(new Dimension(300, 419));
        jPanel2.setLayout(new BorderLayout());

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setPreferredSize(new Dimension(458, 300));

        gamesPanel.setPreferredSize(new Dimension(295, 300));
        gamesPanel.setLayout(new BorderLayout());

        jScrollPane1.setPreferredSize(new Dimension(453, 300));

        gamesTable.setModel(new DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "white", "black", "result"
            }
        ) {
            Class[] types = new Class [] {
                Object.class, Object.class, String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        gamesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(gamesTable);

        gamesPanel.add(jScrollPane1, BorderLayout.CENTER);

        createScheduleButton.setText("<html>create<br>schedule");
        createScheduleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                createScheduleButtonActionPerformed(evt);
            }
        });

        startGameButton.setText("<html>start<br>game");
        startGameButton.setEnabled(false);
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                startGameButtonActionPerformed(evt);
            }
        });

        stopGameButton.setText("<html>stop<br>game");
        stopGameButton.setEnabled(false);
        stopGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                stopGameButtonActionPerformed(evt);
            }
        });

        timeSlider.setMajorTickSpacing(1);
        timeSlider.setMaximum(8);
        timeSlider.setMinimum(1);
        timeSlider.setPaintLabels(true);
        timeSlider.setPaintTicks(true);
        timeSlider.setPaintTrack(false);
        timeSlider.setSnapToTicks(true);
        timeSlider.setToolTipText("<html>maximum thinking time<br>for computer player");
        timeSlider.setValue(2);

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(timeSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(createScheduleButton, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startGameButton, GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(stopGameButton, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(createScheduleButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(startGameButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(stopGameButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timeSlider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gamesPanel.add(jPanel3, BorderLayout.SOUTH);

        tabbedPane.addTab("games", gamesPanel);

        rankingPanel.setLayout(new BorderLayout());

        rankingTable.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null}
            },
            new String [] {
                "player", "win", "draw", "loss", "points"
            }
        ) {
            Class[] types = new Class [] {
                String.class, Integer.class, Integer.class, Integer.class, Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        rankingTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane2.setViewportView(rankingTable);

        rankingPanel.add(jScrollPane2, BorderLayout.CENTER);

        tabbedPane.addTab("ranking", rankingPanel);

        jPanel2.add(tabbedPane, BorderLayout.CENTER);

        jPanel7.setPreferredSize(new Dimension(300, 50));

        statusLabel.setFont(new Font("Dialog", 1, 24)); // NOI18N
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setText("   -   ");

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(statusLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(statusLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel7, BorderLayout.PAGE_START);

        getContentPane().add(jPanel2, BorderLayout.EAST);

        jPanel5.setPreferredSize(new Dimension(200, 532));
        jPanel5.setLayout(new BoxLayout(jPanel5, BoxLayout.Y_AXIS));

        blackPanel.setPreferredSize(new Dimension(100, 266));
        blackPanel.setLayout(new BorderLayout(0, 5));

        blackLabel.setFont(new Font("Dialog", 1, 18)); // NOI18N
        blackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        blackLabel.setText("black");
        blackLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        blackLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        blackLabel.setVerticalTextPosition(SwingConstants.TOP);
        blackPanel.add(blackLabel, BorderLayout.NORTH);

        jPanel6.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("value");

        blackValueLabel.setFont(new Font("Dialog", 1, 18)); // NOI18N
        blackValueLabel.setForeground(new Color(255, 0, 0));
        blackValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        blackValueLabel.setText("-");

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
            .addComponent(blackValueLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blackValueLabel)
                .addContainerGap(203, Short.MAX_VALUE))
        );

        blackPanel.add(jPanel6, BorderLayout.CENTER);

        jPanel5.add(blackPanel);

        whitePanel.setPreferredSize(new Dimension(100, 266));
        whitePanel.setLayout(new BorderLayout(0, 5));

        whiteLabel.setFont(new Font("Dialog", 1, 18)); // NOI18N
        whiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        whiteLabel.setText("white");
        whiteLabel.setVerticalAlignment(SwingConstants.TOP);
        whiteLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        whiteLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        whitePanel.add(whiteLabel, BorderLayout.SOUTH);

        jPanel8.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("value");

        whiteValueLabel.setFont(new Font("Dialog", 1, 18)); // NOI18N
        whiteValueLabel.setForeground(new Color(255, 0, 0));
        whiteValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        whiteValueLabel.setText("-");

        GroupLayout jPanel8Layout = new GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
            .addComponent(whiteValueLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 215, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(whiteValueLabel))
        );

        whitePanel.add(jPanel8, BorderLayout.CENTER);

        jPanel5.add(whitePanel);

        getContentPane().add(jPanel5, BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createScheduleButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_createScheduleButtonActionPerformed
        createSchedule();
    }//GEN-LAST:event_createScheduleButtonActionPerformed

    private void startGameButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_startGameButtonActionPerformed
        int row = gamesTable.getSelectedRow();
        if (row != -1) {
            Game g = schedule.get(row);
            startGame(g);
        }
    }//GEN-LAST:event_startGameButtonActionPerformed

    private void stopGameButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_stopGameButtonActionPerformed
        if (currentSearchTask!=null) {
            currentSearchTask.stop();
            currentGame = null;
        } else {
            currentGame = null;
            updateGUI();
        }
        finishGame(currentGame, gameGUI.getCurrentGameState());
    }//GEN-LAST:event_stopGameButtonActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel blackLabel;
    private JPanel blackPanel;
    private JLabel blackValueLabel;
    private JPanel boardContainerPanel;
    private JButton createScheduleButton;
    private JPanel gamesPanel;
    private JTable gamesTable;
    private JScrollPane jScrollPane2;
    private JPanel rankingPanel;
    private JTable rankingTable;
    private JButton startGameButton;
    protected JLabel statusLabel;
    private JButton stopGameButton;
    private JTabbedPane tabbedPane;
    private JSlider timeSlider;
    private JLabel whiteLabel;
    private JPanel whitePanel;
    private JLabel whiteValueLabel;
    // End of variables declaration//GEN-END:variables



    //<editor-fold defaultstate="collapsed" desc="game play">
    /**
     *
     * @param game
     */
    public void startGame(Game game) {
        currentGame = game;

        // initialize game state
        notifyCompetitionListeners(game, true); // notify of start of game
        S gs = gameGUI.getCurrentGameState();

        // fill player labels
        fillPlayerLabel(game.first, whiteLabel);
        fillPlayerLabel(game.second, blackLabel);

        // start the game
        continueGame(game, gs);
    }

    SearchTask currentSearchTask=null;
    private void continueGame(final Game game, final S gs) {
        if ((currentGame==null) || gs.isEndState()) {
            finishGame(game,gs);
        } else {
            updateGUI(game,gs); updateGUI();
            Player currentPlayer;
            if (gs.isWhiteToMove())
                currentPlayer  =  game.first;
            else currentPlayer = game.second;

            if (currentPlayer.isHuman()) {
                //getHumanMove(game, gs); done via GameGUIListener
                currentSearchTask=null;
            } else {
                currentSearchTask = getComputerMove(currentPlayer, gs, game);
            }
        }
    }

    private void finishGame(final Game game, final S gs) {
        currentGame = null;
        updateGUI(); updateGUI(game,gs);
        // for now, give a random result
        Result[] values = Result.values();
        int pick = new Random().nextInt(values.length - 1);
        if (game!=null) game.setResult(Result.values()[pick]);
        gamesTable.setModel(gamesTable.getModel()); // redraw ????
        updateRanking();
        notifyCompetitionListeners(game,false); // notify of end of game
    }

    private SearchTask getComputerMove(final Player currentPlayer, final S gs, final Game game) {
        SearchTask<M, Long, S> searchTask;
        final Timer timer = new Timer();
        final int maxTime = timeSlider.getValue();
        searchTask = new TimedSearchTask<M, Long, S>(currentPlayer, gs, maxTime) {
            private long MIN_DELAY=1500; // minimum time for a move 1500 milliseconds
            @Override
            public void done(M m) {
                timer.stop();

                // sleep at least MIN DELAY ms before doing the move on the board
                long dt = timer.elapsedTimeInMilliSeconds();
                System.err.println("dt = " + dt + "/" + 1000*maxTime+"\n\n");
                if (dt <MIN_DELAY) {
                    sleep(MIN_DELAY-dt);
                }

                // apply move in the current game state
                if (gs.getMoves().contains(m)) {
                    //gs.doMove(m);
                    notifyCompetitionListeners(m); // notify of next AI move
                    //gameGUI.animateMove(m);
                    // recurse
                    continueGame(game,gs);
                } else {
                    String message=("<html><center>"+(gs.isWhiteToMove()?"White":"Black") + " player ("+currentPlayer.getName()+")<br> tries an illegal move:<br>" + m);
                    LOG.log(Level.SEVERE, message);
                    JOptionPane.showMessageDialog(rootPane, message, "illegal move", JOptionPane.ERROR_MESSAGE);
                    finishGame(game,gs);
                }

            }
        };
        timer.start();
        searchTask.execute();
        return searchTask;
    }
    //</editor-fold>

    int getResult(Identity p) {
        int result=0;
        for (Game g : schedule) {
            if (p == g.first) {
                result += g.getResult().getWhitePoints();
            } else if (p == g.second) {
                result += g.getResult().getBlackPoints();
            }
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="update CompetitionGUI methods">
    private void updateGUI(Game game, S gs) {
        if (game!=null) {
            boolean whiteIsHuman = game.first.isHuman();
            boolean blackIsHuman = game.second.isHuman();
            whiteValueLabel.setText(whiteIsHuman?"":""+game.first.getValue());
            blackValueLabel.setText(blackIsHuman?"":""+game.second.getValue());
        }
        gameGUI.show(gs);
        updateWhoIsToMove(gs);
    }

    private void updateRanking() {
        final String[] columns = {"name", "W", "D","L", "P" };
        final Class[] classes = {String.class, Integer.class, Integer.class, Integer.class, Integer.class};

        // create ordered list of players, ordered by points gained
        final Set<Player> players = new TreeSet<>(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                int result0 = getResult(o1);
                int result1 = getResult(o2);
                int compare = Integer.compare(result0, result1);
                if (compare == 0) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                } else {
                    return compare;
                }
            }
        });

        for(Game game: schedule) {
            players.add(game.first);
            players.add(game.second);
        }

        rankingTable.setModel(new TableModel() {

            @Override
            public int getRowCount() {
                return players.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return columns[columnIndex];
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return classes[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Player[] h = players.toArray(new Player[0]);
                if (columns[columnIndex].equals("name"))
                    return h[rowIndex].getName();
                else {
                    return "0";
                }
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }
            @Override
            public void addTableModelListener(TableModelListener l) {  }
            @Override
            public void removeTableModelListener(TableModelListener l) { }
        });
    }

    protected void updateWhoIsToMove(S ds) {
        boolean w2m = ds.isWhiteToMove();
        whiteLabel.setEnabled(w2m);
        blackLabel.setEnabled(!w2m);
    }

    private void fillPlayerLabel(Player player, JLabel label) {

        label.setText(player.getName());
        ImageIcon icon = player.getIcon();
        if (icon!=null) {
            Image image = icon.getImage();
            if (image.getWidth(null)>128||image.getHeight(null)>128) {
                icon = new ImageIcon(icon.getImage().getScaledInstance(128, 128, java.awt.Image.SCALE_SMOOTH));
            }
            label.setIcon(icon);
        }
    }

    private void fillTable(List<Game> schedule) {
        TableModel model = new DefaultTableModel(new String[]{"white", "black", "result"}, schedule.size());
        int row = 0;
        for (Game game : schedule) {
            model.setValueAt(game.first.getName(), row, 0);
            model.setValueAt(game.second.getName(), row, 1);
            model.setValueAt(game.getResult(), row, 2);
            row = row + 1;
        }
        gamesTable.setModel(model);
        if (model.getRowCount()>0) {
            gamesTable.getSelectionModel().setSelectionInterval(0, 0);
        }
    }
//</editor-fold>


    private void updateGUI() {
        boolean scheduledGames = gamesTable.getModel().getRowCount()>0;
            startGameButton.setEnabled(scheduledGames && currentGame==null);
            stopGameButton.setEnabled(scheduledGames && currentGame!=null);
    }
    //<editor-fold defaultstate="collapsed" desc="auxiliary methods">



    private static void sleep(long dt) {
        try { TimeUnit.MILLISECONDS.sleep(dt); } catch (InterruptedException ex) { }
    }
    //</editor-fold>

    private void createSchedule() {
        List<P> plugins = getPlugins(pluginFolders);
        List<Competitor> players = SelectionPanel.showDialog(this, plugins);
        //List<P> players = PluginSelectionPanel.showDialog(this,plugins);

        if (players!=null) {
            Competition competition = new Competition(players);
            schedule = competition.createSchedule();
            fillTable(schedule);
            updateRanking();
        }
        updateGUI();
    }

    //<editor-fold defaultstate="collapsed" desc="CompetitionListener handling">
    public void add(CompetitionListener<M> l) {
        listeners.add(l);
    }

    public void remove(CompetitionListener<M> l) {
        listeners.remove(l);
    }

    private void notifyCompetitionListeners(M m) {
        for(CompetitionListener<M> l: listeners) {
            l.onAIMove(m);
        }
    }
    private void notifyCompetitionListeners(Game g, boolean start) {
        for(CompetitionListener<M> l : listeners) {
            if (start) {
                l.onStartGame(g);
            } else {
                l.onStopGame(g);
            }
        }
    }
//</editor-fold>

    @Override
    public void onHumanMove(M m) {
        continueGame(currentGame,gameGUI.getCurrentGameState());
    }

    @Override
    public void onNewGameState(S s) {
        if (currentGame==null) {
            boolean w2m = s.isWhiteToMove();
            whiteLabel.setEnabled(w2m);
            blackLabel.setEnabled(!w2m);
        }
    }

    private List<P> getPlugins(String[] pluginFolders) {
        PluginManager pm = PluginManagerFactory.createPluginManager();
        pm.addPluginsFrom(ClassURI.CLASSPATH, new OptionReportAfter());
        Arrays.asList(pluginFolders).stream().forEach(folder -> {
            pm.addPluginsFrom(new File(folder).toURI(), new OptionReportAfter());
        });
        PluginManagerUtil pmu= new PluginManagerUtil(pm);
        return pmu.getPlugins(Plugin.class).stream().filter(selector).map(p->(P)p).collect(Collectors.toList());
    }
}