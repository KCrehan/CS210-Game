# CS210-Game
Repository for the CS210 Game Project - Hangman

Important Note

Please ensure to update the path to the words.txt file within the java file. If this isn’t done then the game WILL NOT WORK. Additionally if possible use the words.txt provided in the assignment submission as this has more accessible words, the other one kept giving obscure and hard to guess words.


Overview of Game

The game is classic hangman. When the user runs the game initially, they get a start screen that lets them choose a difficulty and then starts the game. It has simple displays showing how much of the word has been guessed and how many tries remain. It also has two different end screens depending on whether the user won or lost.

How To Play

The game follows all the usual rules of classic hangman. When the game is run, a start screen appears with the choice of 3 difficulties, easy, medium, and hard. The higher the difficulty, the longer the word can be and the less tries the user will get. 

Once a difficulty is chosen, the game starts. The main game screen appears and the user can now play. Initially, no letters are revealed and the word is completely hidden. When the user presses one of the buttons, one of two things will happen. If it’s a correct guess, the corresponding letters will be revealed and the tries remaining will remain the same. If the guess is incorrect, no letters will be revealed and the tries remaining will decrease by one. Regardless of whether or not the guess was correct, the button will disable itself so that it cannot be guessed again. This helps the user keep track of their guesses. 

The game also has 2 distinct end states. If the word is guessed correctly with some tries still remaining, the user is taken to the win screen and congratulated. If the user runs out of tries before they can guess the word, they’re taken to the loss screen where they’re told to try again. For both screens, the word is revealed and the option to play again is given. The user can play as many games as they like, with the game resetting every time.

Code Structure

The game is primarily built around the Java.awt and Javax.Swing libraries. These allow for GUI components such as JFrames, JPanels, JButtons, and JLabels. It also allows for Layout Managers which auto-place components depending on the parameters given.

The game itself is stored in a custom object. In the Game class and main method, a new instance of the NewGame object is created. No parameters are needed so only a default constructor is needed. This constructor builds the necessary JFrames and Panels needed for the game. This means that the Frame, Start, and Game Panels are built whenever a NewGame object is created, before the game is run. 

The Start Panel is a JPanel with a BoxLayout. It consists of 4 components, a label and 3 difficulty buttons. Each button has a custom ActionListener to do something when the button is pushed. For the difficulty buttons, this means assigning the difficulty variable its appropriate value and starting the main game. All these components are then added to the panel along with dummy components to space them out.

The Game Panel is the primary user interface. This is where the game is actually played. It actually has 2 sub-panels and a GridLayout so that the screen is separated into 2 halves. 

The top half is for the displayed word and the remaining tries. The word is displayed using a JLabel that gets updated every time a correct guess is made. The remaining tries are also displayed using a JLabel that gets updated every time an incorrect guess is made. A BorderLayout is used so that the displayed word can be centered and the remaining tries can be bound to the top right corner.


The bottom half is for all the letter buttons and the reset button. The alphabet buttons are stored in a JButton array so that 26 individual initializations aren’t needed. A for loop is used to create each button with its own ActionListener. A temporary button is used to set all the parameters as I ran into issues with referencing the button array when writing the ActionListener. The corresponding letter is stored within the button's ActionCommand variable and this is then retrieved when the button is pressed. If the guess is contained within the word, the word display is updated and the tries display is kept the same. If the guess isn’t correct, the tries remaining get reduced by one and the display is updated. There’s also a reset button that brings the user back to the start screen and resets all the buttons. The word and tries are reset when a new difficulty is chosen. A GridLayout is used to create 3 rows of 9 buttons so that the 26 letter buttons and the reset button fit in appropriately.

Back in the main method, the startScreen method is called and the game begins. This method just brings up the start screen and refreshes the frame. Pressing any of the buttons on the start screen will call the startGame method and begin the main game.


The startGame method is the primary driver of the game. The difficulty will be chosen on the start screen and now it is used to pick a word from the provided text file. The getWord method reads the difficulty and continually generates words using the dictionary object until an appropriately sized one is obtained. Easy difficulty has shorter words and more tries. Hard difficulty has longer words and less tries. Once an appropriate word is chosen, it is passed into the breakup method which will separate the word into individual letters and return them in a character array. The array for which letters are revealed is then initialized and all values are set to false, aside from any hyphens or periods which will be automatically revealed. The word display is then updated. A switch statement is used to decide the tries given depending on the difficulty. Finally, the game panel is displayed and the frame is updated. From here any action will depend on the ActionListeners of the buttons.

When a user presses one of the buttons, its corresponding letter is fed into the inWord method. This uses an enhanced for loop to compare the guess to all the letters in the word and returns whether or not a match was found.

Depending on whether or not a match is found, different things will happen. 

If a match is found, the guess is fed into the updateRevealed method, which scans through the array of letters for the word and updates the revealed array appropriately. The word display is then updated again. It will scan the revealed array and construct a string of either characters or underscores depending on whether each letter has been revealed or not. This update method also checks to see if the word has been successfully guessed.   

It counts the amount of revealed letters and if this is the same as the length of the array then the word has been successfully guessed and the endGame method can be called with the win state equaling true. 

If no match is found, the tries variable is decreased by one and the display for tries remaining is updated. The updateTries method has a check for the case in which the tries equal zero. This means the user has lost and the endGame method can be called with the win state equaling false.

When pressed the buttons will also disable themselves so that the same letter can’t be guessed twice allowing the user to keep track of their guesses. Along with the letter buttons, there’s also a reset button which brings the user back to the start screen and calls the resetGame method. This method goes through the button array and re-enables all the buttons so the user can play again.

The final state of the game is the end state. This happens when the endGame method is called. It takes a boolean value which dictates whether or not the game has been won. It builds a new JPanel and depending on the win condition, changes the message to either congratulate the user or tell them to try again. In both cases the word is revealed and a button allowing them to restart the game is shown. The button when pressed calls the startScreen method and loops back around to the beginning. 



Both Dictionary and Random objects were used in this project. The Dictionary object reads in a file full of words and then has a method to choose which word to return. Using this functionality in conjunction with the Random object, a random word can be chosen and used in the game.
