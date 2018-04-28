import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.event.InternalFrameEvent.INTERNAL_FRAME_CLOSING;

/**
 * Class represts Window in which a version of the game runs
 * @Author Tobias Fetzer 198318, Simon Stratemeier 199067
 * @Version: 1.0
 * @Date: 27/04/18
 */
public class ViewGame extends JInternalFrame implements ActionListener{
    static int nr = 0, xpos = 30, ypos = 30;
    AnzeigeFlaeche myView;
    private Color dead=Color.GREEN;                 //saves the colors
    private Color alive=Color.RED;
    boolean isFigure=false;                         //is a figure being set
    boolean [][] figure={{false}};                  //saves figure
    private BoardView boardView;


    private GameOfLife game;
    JMenuBar menuBar = new JMenuBar();
    JMenu[] menus = { new JMenu("Modus"),
            new JMenu("Speed"), new JMenu("Fenster") , new JMenu("Figur")};
    JMenuItem[] items ={new JMenuItem("Run/Pause"),new JMenuItem("Set"),new JMenuItem("Paint"),
            new JMenuItem("Fast"),new JMenuItem("Medium"),new JMenuItem("Slow"),
            new JMenuItem("new View"),new JMenuItem("new Game"),new JMenuItem("Change Color Alive"),new JMenuItem("Change Color Dead"), new JMenuItem("FlipX"), new JMenuItem("FlipY"),
            new JMenuItem("Glider"),new JMenuItem("f-Pentomino"),new JMenuItem("Blinker"), new JMenuItem("Biploe"), new JMenuItem("Clear")};

    /**
     *  Construktor
     * @param myView        refrence to ViewGame
     * @param game          reference to GameOfLife
     */

    public ViewGame(AnzeigeFlaeche myView, GameOfLife game){
        super ("Game " + (++nr), true, true);
        this.myView=myView;
        this.game=game;
        for (int i = 0; i < items.length; i++) { // fuer alle Eintraege:
            menus[(i<3)?0:(i<6)?1:(i<11)?2:3].add(items[i]); // add Items in Menue 0|1|2
            items[i].addActionListener(this);
        }
        for (int i = 0; i < menus.length; i++) // fuer alle Menues:
            menuBar.add (menus[i]); // fuege ein in Menue-Leiste

        addInternalFrameListener(new InternalFrameCloseListener() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                game.deleteObserver(boardView);
            }
        });

        boardView = new BoardView(game, this);
        add(boardView);

        setJMenuBar (menuBar);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){

            case "Run/Pause":{                      //pauses or starts the game
                game.isRun=!game.isRun;
                game.isPaint=false;                 //disables paint and set when game runs, viable for change
                game.isSet=false;
                break;
            }
            case "Set":{                               //enables the option to set cells to alive
                game.isRun=false;               //pauses the game while setting, viable for change
                game.isSet=true;
                isFigure=false;                        //disables setting figures
                break;
            }
            case "Paint":{                                 //enables paint
                game.isRun=false;
                game.isPaint=true;
                isFigure=false;
                break;
            }
            case "Fast":{                                   //changes speed
                game.setSpeed(100);
                break;
            }
            case "Medium":{
                game.setSpeed(1000);
                break;
            }
            case "Slow":{
                game.setSpeed(2000);
                break;
            }
            case "new View":{                         //opens new window
                ViewGame viewGame1 = new ViewGame(AnzeigeFlaeche.desktop, game); //passes refernce to thread and the boolean values
                AnzeigeFlaeche.desktop.addChild (viewGame1, xpos+=20, ypos+=20);
                break;
            }
            case "new Game":{                         //opens new window
                StartGameWindow sgw=new StartGameWindow(AnzeigeFlaeche.desk);      //Creates a Stargame Window
                AnzeigeFlaeche.desktop.addChild (sgw, 10, 10); // Ein Kindfenster einfuegen
                break;
            }
            case "Change Color Alive": {                            //changes color of living Cells
                alive=JColorChooser.showDialog(this,"Select living color",Color.RED);
                break;
            }
            case "Change Color Dead":{                                 //changes color of dead cells
                dead=JColorChooser.showDialog(this,"Select dead color",Color.GREEN);
                break;
            }
            case "FlipX":{                                       //flips on the y axis (left is right)
                boardView.setFlipX(!boardView.isFlipX());
                break;

            }
            case "FlipY":{                                       //flips on the y axis (left is right)
                boardView.setFlipY(!boardView.isFlipY());
                break;
            }
            case "Glider":                                         //set figures on grid
                game.isPaint=false;
                game.isSet=false;
                isFigure=true;
                figure=KonstruktionsFeld.getForm(Konstruktionen.GLEITER);
                break;
            case "f-Pentomino":{
                game.isPaint=false;
                game.isSet=false;
                isFigure=true;
                figure=KonstruktionsFeld.getForm(Konstruktionen.F_PENTOMINO);
                break;
                }
            case "Blinker":{
                game.isPaint=false;
                game.isSet=false;
                isFigure=true;
                figure=KonstruktionsFeld.getForm(Konstruktionen.BLINKER);
                break;
            }
            case "Biploe":{
                game.isPaint=false;
                game.isSet=false;
                isFigure=true;
                figure=KonstruktionsFeld.getForm(Konstruktionen.BIPLOE);
                break;
            }
            case "Clear":{game.resetFeld(); break;}         //clears game, kills all cells
        }
    }

    public Color getAlive() {
        return alive;
    }

    public Color getDead() {
        return dead;
    }

    public boolean[][] getFigure() {
        return figure;
    }
}
