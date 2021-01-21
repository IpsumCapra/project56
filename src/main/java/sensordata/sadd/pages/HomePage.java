package main.java.sensordata.sadd.pages;

import main.java.sensordata.sadd.database.QuerySystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends Page implements ActionListener {
    //<editor-fold desc="Swing elements">
    public JPanel homePage;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel usernameLabel;
    private JButton logUitButton;
    private JPanel userIconPanel;
    private JPanel overview;
    private JPanel results;
    private JButton backToOverview;
    private JScrollPane shortcutPanel;
    private JScrollPane overviewPanel;
    private JTable resultTable;
    private JPanel contentPanel;
    private JLabel queryResult;
    private JButton addOverviewButton;
    private JButton addShortcutButton;
    private JPanel overviewContainer;
    private JPanel shortcutsContainer;
    private JPanel overviewWizard;
    private JPanel shortcutWizard;
    private JTextField shortcutQuery;
    private JButton addToShortcuts;
    private JTextField shortcutNameField;
    private JTextField overviewWizardQueryField;
    private JSpinner overviewWizardSpinner;
    private JButton addToOverview;
    private JTextField overviewNameField;
    //</editor-fold>

    private static final String SEARCH = "search";
    private static final String TO_OVERVIEW = "to overview";
    private static final String SHORTCUT = "shortcut";
    private static final String ADD_OVERVIEW = "add overview";
    private static final String ADD_SHORTCUT = "add shortcut";
    private static final String DELETE_OVERVIEW = "delete overview";
    private static final String DELETE_SHORTCUT = "delete shortcut";
    private static final String TOGGLE_OVERVIEW_WIZARD = "toggle overview wizard";
    private static final String TOGGLE_SHORTCUT_WIZARD = "toggle shortcut wizard";
    private static final String LOGOUT = "logout";

    private String email;
    private String username;

    private boolean inOverviewWizard = false;
    private boolean inShortcutWizard = false;

    private final JPopupMenu overviewMenu = new JPopupMenu();
    private final JPopupMenu shortcutMenu = new JPopupMenu();

    private final QuerySystem querySystem = new QuerySystem();

    public HomePage(String email,String username,CardLayout cards, Container parent) {
        super(cards, parent);

        JMenuItem overviewDeleteItem = new JMenuItem("Verwijder overzicht");
        overviewDeleteItem.setMnemonic(KeyEvent.VK_V);
        overviewDeleteItem.addActionListener(this);
        overviewDeleteItem.setActionCommand(DELETE_OVERVIEW);

        overviewMenu.add(overviewDeleteItem);

        JMenuItem shortcutDeleteItem = new JMenuItem("Verwijder snelkoppeling");
        shortcutDeleteItem.setMnemonic(KeyEvent.VK_V);
        shortcutDeleteItem.addActionListener(this);
        shortcutDeleteItem.setActionCommand(DELETE_SHORTCUT);

        addToOverview.setActionCommand(ADD_OVERVIEW);
        addToOverview.addActionListener(this);

        addToShortcuts.setActionCommand(ADD_SHORTCUT);
        addToShortcuts.addActionListener(this);

        shortcutMenu.add(shortcutDeleteItem);

        searchButton.setActionCommand(SEARCH);
        searchButton.addActionListener(this);
        searchField.setActionCommand(SEARCH);
        searchField.addActionListener(this);

        backToOverview.setActionCommand(TO_OVERVIEW);
        backToOverview.addActionListener(this);

        addOverviewButton.setActionCommand(TOGGLE_OVERVIEW_WIZARD);
        addOverviewButton.addActionListener(this);

        overviewWizardSpinner.setModel(new SpinnerNumberModel(1, 1, 60, 1));

        addShortcutButton.setActionCommand(TOGGLE_SHORTCUT_WIZARD);
        addShortcutButton.addActionListener(this);

        shortcutPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        overviewPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        logUitButton.setActionCommand(LOGOUT);
        logUitButton.addActionListener(this);

        //TODO Replace with login.
        setEmailUsername(email,username);
        usernameLabel.setText(username);


        buildShortcuts();
        buildOverview();
    }


    public void buildShortcuts() {
        JPanel shortcuts = new JPanel(new GridLayout(0, 2));

        ((GridLayout) shortcuts.getLayout()).setRows(10);
        ((GridLayout) shortcuts.getLayout()).setVgap(5);

        Shortcut[] shortcutButtons = null;
        try {
            shortcutButtons = querySystem.getShortcuts(email);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (shortcutButtons != null) {
            int length = shortcutButtons.length;
            ((GridLayout) shortcuts.getLayout()).setRows(Math.round(length/2) + 1);
            for (int i = 0; i < length; i++) {
                shortcuts.add(shortcutButtons[i]);
                shortcutButtons[i].setActionCommand(SHORTCUT);
                shortcutButtons[i].addActionListener(this);
                shortcutButtons[i].setComponentPopupMenu(shortcutMenu);
            }

            shortcutPanel.getViewport().add(shortcuts);
            shortcuts.setPreferredSize(new Dimension(shortcutPanel.getViewport().getSize().width, length * 20));
        }
    }

    public void buildOverview() {
        JPanel overviews = new JPanel(new GridLayout(0, 1));

        ((GridLayout) overviews.getLayout()).setVgap(5);

        Overview[] overviewEntries = null;
        try {
            overviewEntries = querySystem.getOverviews(email);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (overviewEntries != null) {
            int length = overviewEntries.length;
            ((GridLayout) overviews.getLayout()).setRows(overviewEntries.length);
            for (int i = 0; i < length; i++) {
                overviews.add(overviewEntries[i]);
                Thread othread = new Thread(overviewEntries[i]);
                othread.start();
                overviewEntries[i].setComponentPopupMenu(overviewMenu);
            }

            overviewPanel.getViewport().add(overviews);
            Dimension dim = overviews.getSize();
            overviews.setPreferredSize(new Dimension(overviewPanel.getViewport().getSize().width, length * 100));
        }
    }

    public void generateTable(String query) {
        DefaultTableModel res;
        long start = 0;
        long end = 0;
        try {
            start = System.nanoTime();
            res = querySystem.query(query);
            end = System.nanoTime();
        } catch (Exception ex) {
            res = null;
            ex.printStackTrace();
            queryResult.setText("Something went wrong. Try again later, or contact support.");
        }

        if (res != null) {
            queryResult.setText("Query OK (Got " + res.getColumnCount() + " COLS, " + res.getRowCount() + " ROWS in " + (end - start) / 1000000 + "ms.)");
            resultTable.setModel(res);
        }

        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "results");
    }

    public void setEmailUsername(String email,String username)
    {
        this.email = email;
        this.username= username;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case SEARCH:
                generateTable(searchField.getText());
                break;
            case TO_OVERVIEW:
                ((CardLayout) contentPanel.getLayout()).show(contentPanel, "overview");
                break;
            case SHORTCUT:
                generateTable(((Shortcut) e.getSource()).getQuery());
                break;
            case DELETE_OVERVIEW:
                Overview overview = (Overview)((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();
                if (querySystem.deleteOverview(email, overview.getID())) {
                    System.out.println("Success!");
                    buildOverview();
                } else {
                    System.out.println("Failed to delete overview!");
                    //TODO Add deletion failure code
                }
                break;
            case DELETE_SHORTCUT:
                Shortcut shortcut = (Shortcut)((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();
                if (querySystem.deleteShortcut(email, shortcut.getID())) {
                    System.out.println("Success!");
                    buildShortcuts();
                } else {
                    System.out.println("Failed to delete shortcut!");
                    //TODO Add deletion failure code
                }
                break;
            case ADD_OVERVIEW:
                String overName = overviewNameField.getText();
                String overQuery = overviewWizardQueryField.getText();
                if (!overName.equals("") && !overQuery.equals("")) {
                    if (querySystem.addOverview(email, overQuery, overName, (int) overviewWizardSpinner.getValue())) {
                        System.out.println("Successfully created new overview!");
                        buildOverview();
                    } else {
                        System.out.println("Failed.");
                    }
                } else {
                    System.out.println("Missing values.");
                    //TODO Add proper handling of missing values.
                }
                break;
            case ADD_SHORTCUT:
                String shortcutName = shortcutNameField.getText();
                String shortcutQueryString = shortcutQuery.getText();
                if (!shortcutName.equals("") && !shortcutQueryString.equals("")) {
                    if (querySystem.addShortcut(email, shortcutQueryString, shortcutName)) {
                        System.out.println("Succesfully created new shortcut!");
                        buildShortcuts();
                    } else {
                        System.out.println("Failed.");
                    }
                } else {
                    System.out.println("Missing values.");
                    //TODO Add proper handling of missing values.
                }
                break;
            case TOGGLE_OVERVIEW_WIZARD:
                ((CardLayout) overviewContainer.getLayout()).next(overviewContainer);
                inOverviewWizard = !inOverviewWizard;
                if (inOverviewWizard) {
                    addOverviewButton.setText("Terug");
                } else {
                    addOverviewButton.setText("Voeg nieuw overzicht toe");
                }
                break;
            case TOGGLE_SHORTCUT_WIZARD:
                ((CardLayout) shortcutsContainer.getLayout()).next(shortcutsContainer);
                inShortcutWizard = !inShortcutWizard;
                if (inShortcutWizard) {
                    addShortcutButton.setText("Terug");
                } else {
                    addShortcutButton.setText("Voeg nieuwe snelkoppeling toe");
                }
                break;
            case LOGOUT:
                getCards().show(getParent(), "login");
        }
    }
}
