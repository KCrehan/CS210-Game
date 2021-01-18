import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;

/*
VERY IMPORTANT!!!!
The location of the words.txt file MUST BE CHANGED!!!
I have used a different one that I created and have submitted alongside this file
The original one consistently gave very difficult and obscure words that made the game unenjoyable
At bare minimum the path to the file must be updated otherwise a word can't be chosen
*/

public class Game
{	
	public static void main(String [] args)
	{
		//Create a new instance of the game 
		NewGame game = new NewGame();
		//Start the process of a game
		game.startScreen();
	}
}

class NewGame
{
	//JButton Array for the letters, condenses it down into a much smaller typed space instead of 26 declarations
	private JButton[] buttonArr = new JButton[26];
	//Alphabet array for buttons to reference
	private char[] alpha = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	//String to hold the difficulty that will be chosen later
	private String difficulty;
	
	//Different attributes of the chosen word
	private String word;
	private char[] letters;		//Holds each letter so they can be checked
	private boolean[] revealed;		//Hold boolean values of which letters have been correctly guessed
	
	//Number of tries remaining, changes with difficulty
	private int tries;
	
	/*
	Dictionary and Random objects for choosing words
	The below path MUST BE UPDATED!!!!
	If this isn't done then the game will not run
	If at all possible, use the txt file provided with this file as the words are less obscure and makes the game more enjoyable
	 */
	private Dictionary dic = new Dictionary("C:\\words.txt");
	private Random rand = new Random();
	
	//Swing objects that need to be accessed across the entire class
	JFrame frame;			//Main container that holds all other
	JPanel gamePanel;		//Panel that holds the game itself
	JPanel startPanel;		//Panel that holds the start screen 
	JLabel wordDisplay;		//Label that shows how much of the word has been guessed
	JLabel triesLeft;		//Label that shows how many tries remain
	
	//Constructor that builds everything before its displayed
	public NewGame()
	{
		//Set up some details relating to the frame e.g. Name, Size, Layout
		frame = new JFrame("Hangman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setSize(800,500);
		frame.setLayout(null);
		frame.setVisible(true);
		
		//Construct the Start and Game Panels before they're made visible
		buildStartPanel();
		buildGamePanel();
	}
	
	public void buildStartPanel()
	{
		//This method builds the pieces of the Start Screen
		
		//Creat the panel and set its layout, the boxlayou class allows for a vertical list
		startPanel = new JPanel();
		startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
		startPanel.setBackground(Color.decode("#0099ff"));
		
		Font buttonFont = new Font("Serif", Font.PLAIN, 30);
		
		//Create the Label for the title and set its parameters 
		JLabel lbl = new JLabel("Pick a difficulty");
		lbl.setFont(new Font("Arial", Font.PLAIN, 40));
		lbl.setForeground(Color.white);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setMinimumSize(new Dimension(250, 60));
        lbl.setPreferredSize(new Dimension(250, 60));
        lbl.setMaximumSize(new Dimension(Short.MAX_VALUE,
                                          Short.MAX_VALUE));
		
        //Create the three buttons that allow choice of difficulty
		JButton easy = new JButton("Easy");
		easy.setFont(buttonFont);
		easy.setMinimumSize(new Dimension(100, 60));
        easy.setPreferredSize(new Dimension(100, 60));
        easy.setMaximumSize(new Dimension(Short.MAX_VALUE,
                                          Short.MAX_VALUE));
		
        //Create the action listener for these buttons, assigning the difficulty and starting the game
        easy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ea)
			{
				difficulty = "easy";
				startGame();
			}
		});
		
		JButton medium = new JButton("Medium");
		medium.setFont(buttonFont);
		medium.setMinimumSize(new Dimension(100, 60));
        medium.setPreferredSize(new Dimension(100, 60));
        medium.setMaximumSize(new Dimension(Short.MAX_VALUE,
                                          Short.MAX_VALUE));
		medium.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ea)
			{
				difficulty = "medium";
				startGame();
			}
		});
		JButton hard = new JButton("Hard");
		hard.setFont(buttonFont);
		hard.setMinimumSize(new Dimension(100, 60));
        hard.setPreferredSize(new Dimension(100, 60));
        hard.setMaximumSize(new Dimension(Short.MAX_VALUE,
                                          Short.MAX_VALUE));
		hard.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ea)
			{
				difficulty = "hard";
				startGame();
			}
		});
		
		//Add the text and buttons to the panel, along with dummy objects to create space between buttons
		startPanel.add(lbl);
		startPanel.add(Box.createRigidArea(new Dimension(0,30)));
        startPanel.add(easy);
        startPanel.add(Box.createRigidArea(new Dimension(0,30)));
        startPanel.add(medium);
        startPanel.add(Box.createRigidArea(new Dimension(0,30)));
        startPanel.add(hard);
        startPanel.setVisible(true);
        
	}
	
	public void buildGamePanel()
	{
		//This method builds the game screen
		
		//Create the game panel and set its layout, seperating the frame into two parts
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(2,1));
		
		//Panel for the word and the tries remaining
		JPanel wordPanel = new JPanel();
		wordPanel.setLayout(new BorderLayout());	//Layout for the text
		
		//Panel for the letters
		JPanel letterPanel = new JPanel();
		letterPanel.setLayout(new GridLayout(3,9));
		
		//Creating the letters buttons
		for(int i = 0; i < alpha.length; i++)
		{			
			JButton temp = new JButton(alpha[i] + "");		//A temp JButton is used here as I ran into issues with referencing JButtons in the array while creating the action listener
			temp.setActionCommand(alpha[i] + "");			//Link the button to a letter
			temp.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					temp.setEnabled(false);								//Disable the button when it's pressed so they can't guess the same letter twice
					char lttr = (ae.getActionCommand().charAt(0));		//Get the letter that was chosen 
					if(inWord(lttr))						//Check if that letter is a successful guess
					{
						updateRevealed(lttr);				//Update the revealed array
						updateWord();						//Update the displayed word
					}
					else
					{
						tries--;							//If it's a bad guess, reduces the tries remaining
						updateTries();						//Update the tries remaining display
					}
				}
			});
			
			buttonArr[i] = temp;				//Add the button to the array
			letterPanel.add(buttonArr[i]);		//Add the button to the panel
		}
		
		JButton reset = new JButton("Reset");					//The button that resets the game
		reset.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					startScreen();							//Go back to the start screen
					resetGame();							//Reset the buttons and such
				}
			});
		letterPanel.add(reset);								//Add the reset button to the panel
		
		
		//Create the label for the tries remaining and set parameters for it
		triesLeft = new JLabel("Tries Remaining: " + tries, SwingConstants.RIGHT);		
		triesLeft.setFont(new Font("Arial", Font.BOLD, 24));							
		wordPanel.add(triesLeft, BorderLayout.PAGE_START);
		
		
		//Create the display for the word that will update as the user guesses
		wordDisplay = new JLabel("", SwingConstants.CENTER);
		wordDisplay.setFont(new Font("Arial", Font.PLAIN, 50));
		wordPanel.add(wordDisplay, BorderLayout.CENTER);
		
		//Add the text to the panel
		gamePanel.add(wordPanel);
		gamePanel.add(letterPanel);
		gamePanel.setVisible(true);
		
	}
	
	public void startScreen()
	{
		//Start the game by displaying the start screen and updating the frame
		frame.setContentPane(startPanel);
		SwingUtilities.updateComponentTreeUI(frame);
	}
	
	public void startGame()
	{
		//This method starts the actual game
		
		//With the difficulty now chosen, the word can be picked and the two arrays can be generated
		word = getWord(difficulty);
		letters = breakup(word);
		revealed = new boolean[letters.length];
		//Set all letters to be hidden
		for(int x = 0; x < revealed.length; x++)
		{
			if(letters[x] == '-' || letters[x] == '.')
				revealed[x] = true;
			else
				revealed[x] = false;
		}
		
		updateWord();		//Update the word in the game panel before displaying it
		
		//Use the difficulty to decide how many tries the user gets
		switch (difficulty)
		{
		case "easy": tries = 8; break; case "medium": tries = 6; break; case "hard": tries = 5; break; default: tries = 7;
		}
		
		updateTries();		//Update the tries remaining appropriately
		
		//Update the frame with the game panel
		frame.setContentPane(gamePanel);
		SwingUtilities.updateComponentTreeUI(frame);		
	}
	
	public void resetGame()
	{
		//Reset all buttons back to their enabled state
		for(JButton x : buttonArr)
		{
			x.setEnabled(true);
		}
	}
	
	public void endGame(boolean state)
	{
		//This methods handles the end states, either winning or losing
		
		//Create the end screen panel
		JPanel endPanel = new JPanel();
		endPanel.setLayout(new GridLayout(6,1));
		
		//Several labels are used as it was the easiest way to have text on seperate lines and centred
		JLabel lb1 = new JLabel();
		JLabel lb2 = new JLabel();
		JLabel lb3 = new JLabel();
		JLabel lb4 = new JLabel();
		JLabel lb5 = new JLabel();
		
		Font general = new Font("Arial", Font.PLAIN, 50);		//A general font for all labels
		
		lb1.setFont(general);
		lb1.setHorizontalAlignment(SwingConstants.CENTER);		//Format the labels
		lb1.setForeground(Color.white);

		lb2.setFont(general);
		lb2.setHorizontalAlignment(SwingConstants.CENTER);
		lb2.setForeground(Color.white);
		
		lb3.setFont(general);
		lb3.setHorizontalAlignment(SwingConstants.CENTER);
		lb3.setForeground(Color.white);
		
		lb4.setFont(general);
		lb4.setHorizontalAlignment(SwingConstants.CENTER);
		lb4.setForeground(Color.white);
		
		lb5.setFont(general);
		lb5.setHorizontalAlignment(SwingConstants.CENTER);
		lb5.setForeground(Color.white);
		
		lb3.setText("The word was " +"\"" + word.toUpperCase() + "\"");
		lb4.setText("Press reset to play again");
		lb5.setText("or close the window");
		
		//If they won, congratulate them 
		if(state)
		{
			lb1.setText("You Won!");
			lb2.setText("Congratulations!");
			endPanel.setBackground(Color.decode("#47d147"));
		}
		
		//If they lost then tell them to try again 
		else
		{
			lb1.setText("You Lost!");
			lb2.setText("Try Again!");
			endPanel.setBackground(Color.decode("#ff1a1a"));
		}
		
		//Create the button that lets them play again
		JButton reset = new JButton("Play Again");
		reset.setFont(new Font("Serif", Font.PLAIN, 30));
		reset.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					startScreen();
					resetGame();
				}
			});
		
		//Place all the components
		endPanel.add(lb1);
		endPanel.add(lb2);
		endPanel.add(lb3);
		endPanel.add(lb4);
		endPanel.add(lb5);
		endPanel.add(reset);
		
		//Update the frame
		frame.setContentPane(endPanel);
	}
	
	
	public String getWord(String diff)
	{
		//This method generates a word from the Dictionary object
		
		String temp = (dic.pickWord(rand.nextInt(dic.getSize()))).trim();
		
		//Depending on the difficulty, the word will vary in size
		if(diff.equals("easy"))
		{
			while(temp.length() > 5)
				temp = (dic.pickWord(rand.nextInt(dic.getSize()))).trim();
			return temp;
		}
		
		else if(diff.equals("medium"))
		{
			while(temp.length() > 8 || temp.length() < 5)
				temp = (dic.pickWord(rand.nextInt(dic.getSize()))).trim();
			return temp;
		}
		
		else if(diff.equals("hard"))
		{
			while(temp.length() < 6)
				temp = (dic.pickWord(rand.nextInt(dic.getSize()))).trim();
			return temp;
		}
		
		else
			return (dic.pickWord(rand.nextInt(dic.getSize()))).trim();
	}
	
	public char[] breakup(String text)
	{
		//This method breaks up the chosen word into individual letters and returns them as an array		
		text = (text.trim()).toUpperCase();
		
		char[] hold = new char[text.length()];
		for(int i = 0; i < hold.length; i++)
			hold[i] = text.charAt(i);
		return hold;
	}
	
	
	public void updateRevealed(char guess)
	{
		//This method updates the revealed array when a letter is guessed
		for(int a = 0; a < letters.length; a++)
		{
			if(letters[a] == guess)
				revealed[a] = true;
		}
	}
	
	public void updateWord()
	{
		//This method updates the displayed word
		
		String tempWord = "";
		int counter = 0;
		for(int a = 0; a < letters.length; a++)
		{
			if(revealed[a] == true)							//If the letter has been guessed, show it
			{
				tempWord = tempWord + letters[a] + " ";
				counter++;
			}
			
			else											//Otherwise have an underscore
			{
				tempWord = tempWord + "_  ";
			}
		}
		
		//Update the display and check if the word has been guessed
		wordDisplay.setText(tempWord);
		if(counter == revealed.length)
			endGame(true);
	}
	
	public void updateTries()
	{
		//This method updates the tries remaining display
		triesLeft.setText("Tries Remaining: " + tries);
		
		//Check if the game has been lost
		if(tries == 0)
			endGame(false);
	}
	
	
	public boolean inWord(char guess)
	{
		//This method checks if a guess is correct or not
		for(char x : letters)
		{
			if(x == guess)
				return true;
		}
		return false;
	}
}


class Dictionary
{
    //This is the class that was provided for reading in words from a text file
    private String input[]; 

    public Dictionary(String path)
    {
    	input = load(path);  
    }
    
    public int getSize(){
        return input.length;
    }
    
    public String pickWord(int n){
        return input[n];
    }
    
    private String[] load(String file) {
        
    	File aFile = new File(file);     
        StringBuffer contents = new StringBuffer();
        BufferedReader input = null;
        try {
            input = new BufferedReader( new FileReader(aFile) );
            String line = null; 
            int i = 0;
            while (( line = input.readLine()) != null){
                contents.append(line);
                i++;
                contents.append(System.getProperty("line.separator"));
            }
        }catch (FileNotFoundException ex){
            System.out.println("Can't find the file - are you sure the file is in this location: "+file);
            ex.printStackTrace();
        }catch (IOException ex){
            System.out.println("Input output exception while processing file");
            ex.printStackTrace();
        }finally{
            try {
                if (input!= null) {
                    input.close();
                }
            }catch (IOException ex){
                System.out.println("Input output exception while processing file");
                ex.printStackTrace();
            }
        }
        String[] array = contents.toString().split("\n");
        for(String s: array){
            s.trim();
        }
        return array;
    }
}
