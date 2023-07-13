package ui;

import postgres.Column;
import postgres.Db;
import postgres.Row;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Gui implements ActionListener {

    Db db;
    JComponent mainPanel;
    JButton b1,b2,b3,b4,apply;
    JLabel title;
    JTextArea textArea,server,database,username,password;
    JFrame settings;

    Gui() {
        db =new Db();
        ActionListener listener = this;

        mainPanel = new JPanel(new BorderLayout(4,4));
        mainPanel.setBorder(new EmptyBorder(15,15,15,15));

        // leftPanel : les boutons
        // rightPanel: le text area
        JPanel leftPanel = new JPanel(new GridLayout(4, 2, 0, 10));
        leftPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
        JPanel rightPanel = new JPanel(new GridLayout(1, 4, 0, 0));

        //titre
        title = new JLabel("<html><h1 style='padding:25px'>Projet du cours</h1></html>");
        title.setHorizontalAlignment(SwingConstants.CENTER);

        //boutons
        b1 = new JButton("Question 1");
        b2 = new JButton("Question 2");
        b3 = new JButton("Question 3");
        b4 = new JButton("Question 4");

        b1.addActionListener(listener);
        b2.addActionListener(listener);
        b3.addActionListener(listener);
        b4.addActionListener(listener);

        //ouvre la fenêtre des réglages si on clique sur le titre
        title.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                settings = new JFrame();
                JPanel panel = new JPanel(new BorderLayout(10, 10));
                panel.setBorder(new EmptyBorder(15,15,15,15));

                //labels : nom des champs modifiables
                //texts : les champs modifiables
                JPanel labels = new JPanel(new GridLayout(4, 2, 0, 5));
                JPanel texts = new JPanel(new GridLayout(4, 4, 0, 5));

                //les champs modifiables
                server = new JTextArea(db.getServer(),1, 20);
                database = new JTextArea(db.getDatabase(),1, 20);
                username = new JTextArea(db.getUser(), 1,20);
                password = new JTextArea(db.getPassword(),1, 20);

                apply = new JButton("Appliquer");
                apply.addActionListener(listener);

                //nom des champs
                labels.add(new JLabel("server:"));
                labels.add(new JLabel("nom db:"));
                labels.add(new JLabel("username:"));
                labels.add(new JLabel("password:"));

                texts.add(server);
                texts.add(database);
                texts.add(username);
                texts.add(password);

                panel.add(labels,BorderLayout.CENTER);
                panel.add(texts,BorderLayout.LINE_END);
                panel.add(apply,BorderLayout.PAGE_END);

                settings.add(panel);
                settings.pack();
                settings.setVisible(true);
            }
        });

        textArea = new JTextArea(10, 100);
        JScrollPane scroll = new JScrollPane(textArea);
        textArea.setEditable(false);

        leftPanel.add(b1);
        leftPanel.add(b2);
        leftPanel.add(b3);
        leftPanel.add(b4);

        rightPanel.add(scroll);

        mainPanel.add(title,BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.LINE_END);
    }

    /**
     * Gère les clicks des boutons
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        //applique les réglages
        if(source == apply){
            db.setServer(server.getText().trim());
            db.setDatabase(database.getText().trim());
            db.setUser(username.getText().trim());
            db.setPassword(password.getText().trim());
            settings.dispose();
            return;
        }

        ArrayList<Row> rows;
        rows = null;

        //exécute les 4 questions
        if(db.getConnection() != null) {
            if (source == b1) rows =db.question1();
            else if (source == b2) rows =db.question2();
            else if (source == b3) rows = db.question3();
            else rows = db.question4();

            printRows(rows);
            setTextArea(rows);
        }
        else{setTextArea(rows);}
    }

    /**
     * Efface le texte du de la du text area et le remplace par le résultat des questions ou une erreur s'il y a lieu
     * @param rows
     */
    public void setTextArea(ArrayList<Row> rows){
        textArea.setText("");

        StringBuilder s = new StringBuilder();

        if(rows == null)s.append(db.getError());
        else if(rows.size() > 0) {
            for (Row row : rows) {
                ArrayList<Column> cols = row.getCols();
                for(int i=0;i<cols.size();i++)
                    s.append(cols.get(i).getValue()).append(i != cols.size()-1? ", ":"");
                s.append("\n");
            }
        }
        else s.append("Aucune ligne à afficher");
        textArea.setText(String.valueOf(s));
    }

    /**
     * Affiche le résultat d'une question dans la console sous la forme
     * (ligne 1)
     * nom col 1: valeur col1
     * nom col 2: valeur col2
     *
     * ...
     *
     * (ligne n)
     * nom col 1: valeur col1
     * nom col 2: valeur col2
     *
     * @param rows
     */
    public void printRows(ArrayList<Row> rows){
        if(rows == null)System.out.println("\nErreur");
        else if(rows.size() > 0) {
            for (Row row : rows) {
                System.out.println();
                for (Column col : row.getCols())
                    System.out.print(col.getName() + ":\t\t" + col.getValue() + "\n");
            }
        }
        else System.out.println("\nAucun élément à afficher");
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Gui gui = new Gui();
                JFrame frame = new JFrame(gui.getClass().getSimpleName());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setContentPane(gui.mainPanel);
                frame.setMinimumSize(new Dimension(1270, 345));
                frame.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}