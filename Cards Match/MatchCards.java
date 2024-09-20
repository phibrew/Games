import java.awt.*;
import java.awt.event.*;
// import java.lang.classfile.constantpool.InterfaceMethodRefEntry;
import java.util.ArrayList;
import javax.swing.*;

public class MatchCards {
    class Card{
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon){
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString(){
            return cardName;
        }
    }

    String[] cardList = {
        "darkness", 
        "double", 
        "fairy", 
        "fighting",
        "fire", 
        "grass",
        "lightning",
        "metal", 
        "psychic",
        "water"
    };

    int rows = 4, columns = 5;
    int cardWidth = 90, cardHeight = 128;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth; 
    int boardHeight = rows*cardHeight;

    JFrame frame = new JFrame("Pokemon Match Cards");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    ArrayList<JButton> board;
    int errorCount=0;
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton card1Selected, card2Selected;

    MatchCards(){
        setUpCards();
        shuffleCards();

        // frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: "+Integer.toString(errorCount));

        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        //card game board
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for(int i=0; i<cardSet.size(); ++i){
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    if(!gameReady){
                        return;
                    }
                    JButton tile = (JButton) e.getSource();
                    if(tile.getIcon() == cardBackImageIcon){
                        if(card1Selected == null){
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                        }
                        else if(card2Selected == null){
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(cardSet.get(index).cardImageIcon);
                            if(card1Selected.getIcon() != card2Selected.getIcon()){
                                errorCount += 1;
                                textLabel.setText("Errors: " + Integer.toString(errorCount));
                                hideCardTimer.start();
                            }
                            else {
                                card1Selected = card2Selected = null;
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        frame.add(boardPanel);

        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(!gameReady){
                    return;
                }
                gameReady = false;
                restartButton.setEnabled(false);
                card1Selected = card2Selected = null;
                shuffleCards();

                //reassign
                for(int i=0; i<board.size(); ++i){
                    board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                }
                errorCount=0;
                textLabel.setText("Errors: "+ Integer.toString(errorCount));
                hideCardTimer.start();
            }
        });
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        // start game
        hideCardTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();  
    }

    void setUpCards(){
        cardSet = new ArrayList<Card>();
        for(String cardName : cardList){
            Image cardImg = new ImageIcon(getClass().
            getResource("./img/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance
            (cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet);

        //load the back...
        Image cardBackImg = new ImageIcon(getClass(). getResource
        ("./img/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(
            cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards(){
        for(int i=0; i<cardSet.size(); ++i){
            int j = (int) (Math.random() * cardSet.size());
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp); 
        }
    }

    void hideCards(){
        if(gameReady && card1Selected!=null && card2Selected!=null){
            card1Selected.setIcon(cardBackImageIcon);
            card2Selected.setIcon(cardBackImageIcon);
            card1Selected = card2Selected = null;
        }else{
            for (int i=0; i<board.size(); ++i){
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
        }
    }
}
