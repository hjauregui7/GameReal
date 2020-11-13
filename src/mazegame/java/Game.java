package mazegame.java;

import java.util.Random;
import java.util.Scanner;

import mazegame.items.Item;
import mazegame.items.ItemType;
import mazegame.items.RedBull;

public class Game {

	public static void main(String[] args) {

		// System Objects
		Scanner in = new Scanner(System.in);
		Random rand = new Random();

		// Variables (Dungeon)
		String[] Enemies = { "Pesanta", "Boogey Man", "Akuma", "Armaros", "Baba Yaga" };
		int maxEnemyHealth = 50;
		int enemyAttackDamage = 25;

		// Player
		int health = 200;
		int atk = 20;
		
		//initialize the game with items
		BackPack backpack = new BackPack();
		RedBull redbull = new RedBull(3/*number of potion*/);
		backpack.addItem(redbull);

		Item key = new Item(ItemType.KEY, 0);
		backpack.addItem(key);

		int numHealthPot = 3;
		int HealthPotionHeal = 40;
		int HeathPotionDrop = 20; // Chance to Drop
		int numKey = 0; // Number of keys
		int Key = 100; // Chance to drop key to the exit
		int posx = 0, posy = 0; // position of player
		int exitPos = 0;

		boolean running = true;

		System.out.println("Welcome to !"); // Welcome to the game
		System.out.println("Scale of level?");
		System.out.println("For [5x5]:Input 5");
		System.out.println("For [10x10]:Input 10");
		System.out.println("For [15x15]:Input 15");
		System.out.println("For [20x20]:Input 20");
		System.out.println("For other sizes input a single number(like [30] or [40]");
		int x = in.nextInt();
		in.nextLine();

		posx = rand.nextInt(x);
		posy = rand.nextInt(x);
		exitPos = rand.nextInt(4 * x);

		// create a generated maze
		Maze myMaze = new Maze(x,x);										// will need (x,y) for an n x m matrix
		// copy generated maze to 2d String array
		String[][] mazeClone = cloneGeneratedMaze(myMaze.getMaze(), x);		// only passing in 'x' for n x n matrix

		// generate monsters
		generateMonsters(mazeClone, x);
		while (running) {

			// print maze to console
			for(int i = 0; i < x*2+1; i++){
				for(int j = 0; j < x+1; j++) {
					if (i == 2 * posy + 1 && j == posx)
						System.out.print(mazeClone[i][j].charAt(0) + " O ");
					else
						System.out.print(mazeClone[i][j]);
				}
				System.out.println();
			}

			System.out.println();
			System.out.println("--------------------------------------"); // acting as a separator

			if (mazeClone[2 * posy + 1][posx].contains("X")) {
				int enemyHealth = rand.nextInt(maxEnemyHealth);
				String Enemy = Enemies[rand.nextInt(Enemies.length)];
				System.out.println("\t#" + Enemy + " appeared! #\n"); // Enemy has appeared

				while (enemyHealth > 0) {
					System.out.println("\tYour HP: " + health);
					System.out.println("\t" + Enemy + "s HP: " + enemyHealth); // Enemy/your information and options to interact
					System.out.println("\n\tWhat would you like to do?");
					System.out.println("\t1. Attack?");
					System.out.println("\t2. Red Bull?");
					System.out.println("\t3. Run?");
					System.out.println("\tI. Inventory");

					String input = in.nextLine();
					if (input.equals("1")) { // Attack
						int damageDealt = rand.nextInt(atk);
						int damageTaken = rand.nextInt(enemyAttackDamage);

						enemyHealth -= damageDealt;
						health -= damageTaken;

						System.out.println("\t> You strike the " + Enemy + " for " + damageDealt + " damage."); // damage to enemy and to yourself
						System.out.println("\t> You receive " + damageTaken + " in recoil damage!");

						if (health < 1) {
							System.out.println(
									"\t> The damage taken thus far has eroded your soul, you can no longer wake up!"); // death
							break;
						}
					} else if (input.equals("2")) { // Drink Red Bull
						if (backpack.getItemCount(ItemType.RED_BULL) > 0) {
							int healAmount = RedBull.getHealAmount();
							health += healAmount;
							backpack.reduceItemAmount(ItemType.RED_BULL);
							numHealthPot = backpack.getItemCount(ItemType.RED_BULL);
							System.out.println("\t> You have used one of your Red Bull's to heal yourself for "
									+ HealthPotionHeal + " . " + "\n\t> You now have " + health + "HP" + "\n\t> You have "
									+ numHealthPot + " potions left.\n"); // restore HP
						} else {
							System.out.println(
									"\t> You have no Red Bull left, either defeat the enemy like a man (woman?) or run!"); // No more Red Bull
						}
					} else if (input.equals("3")) { // Run
						System.out.println("\t> You run away from " + Enemy + "!" + " We'll get them next time coach!");
						break;
					} else if(input.equalsIgnoreCase("i"))
					{
						printAllItems(backpack);
					}
						else {
						System.out.println("\tInvalid Command"); // invalid command
					}
				}
				if (health < 1) {
					System.out.println("Your soul has now been consumed by the dungeon for all eternity! " + Enemy
							+ " has taken your soul!"); // death
					break;
				}
				if (enemyHealth < 1) {
					System.out.println("--------------------------------------"); // Separator
					mazeClone[2 * posy + 1][posx] = mazeClone[2 * posy + 1][posx].replace('X', ' ');
					System.out.println("# " + Enemy + " was destroyed! # ");
					System.out.println("# You have " + health + " HP left for the next battle. #"); // defeated enemy drop chance for Red Bull
					if (rand.nextInt(100) < HeathPotionDrop) {
						backpack.increaseItemAmount(ItemType.RED_BULL);
						//numHealthPot++;
						System.out.println("# The " + Enemy + " dropped a Red Bull! #");
						System.out.println("# You now have " + numHealthPot + " Red Bull(s) left! #");
					}
					if (rand.nextInt(100) < Key) {
						backpack.increaseItemAmount(ItemType.KEY);
						switch(exitPos / x) {
							case 0:
								mazeClone[0][exitPos % x] = "+   ";  //Exit Position
								break;
							case 1:
								mazeClone[(exitPos % x) * 2 + 1][x] = " ";
								break;
							case 2:
								mazeClone[2 * x][exitPos % x] = "+   ";
								break;
							case 3:
								mazeClone[(exitPos % x) * 2 + 1][0] = " ";
								break;

						}
						System.out.println(" # " + Enemy + " dropped the key to the exit!");
						System.out.println("You have " + backpack.getItemCount(ItemType.KEY) + " key(s) in your inventory!");  //Get key and inventory
					}
				}
			}
			numHealthPot = backpack.getItemCount(ItemType.RED_BULL);
			numKey = backpack.getItemCount(ItemType.KEY);
			
			System.out.println("Use WASD keys to navigate in the maze. Press I for inventory.");  //Navigate the maze Controls
			String ch = in.nextLine();

			switch(ch) {
				case "w":
				case "W":
					if (numKey > 0 && posy == 0 && posx == exitPos % x) {
						System.out.println("You escape the dungeon and have awoken from the dream...for now."); // after finding key to leave maze
						running = false;
					}
					if ((myMaze.getMaze()[posx][posy] & 1) != 0)
						posy--;
					break;
				case "s":
				case "S":
					if (numKey > 0 && posy == x - 1 && posx == exitPos % x) {
						System.out.println("You escape the dungeon and have awoken from the dream...for now."); // after finding key to leave maze
						running = false;
					}
					if ((myMaze.getMaze()[posx][posy] & 2) != 0)
						posy++;
					break;
				case "d":
				case "D":
					if (numKey > 0 && posx == x - 1 && posy == exitPos % x) {
						System.out.println("You escape the dungeon and have awoken from the dream...for now."); // after finding key to leave maze
						running = false;
					}
					if ((myMaze.getMaze()[posx][posy] & 4) != 0)
						posx++;
					break;
				case "a":
				case "A":
					if (numKey > 0 && posx == 0 && posy == exitPos % x) {
						System.out.println("You escape the dungeon and have awoken from the dream...for now."); // after finding key to leave maze
						running = false;
					}
					if ((myMaze.getMaze()[posx][posy] & 8) != 0)
						posx--;
					break;
				case "i":
				case "I":
					printAllItems(backpack);					
					break;
				default:
					System.out.println("Invalid input");
			}

			// System.out.println("--------------------------------------"); // Separator
			// System.out.println(" How would you like to proceed?");
			// System.out.println("1. Continue fighting?");
			// System.out.println("Or");
			// System.out.println("2. Self inflict damage to wake from the dream"); // options after enemy fight

			// String input = in.nextLine();

			// while (!input.equals("1") && !input.equals("2")) { // invalid command
			// 	System.out.println("Invalid Command!");
			// 	input = in.nextLine();
			// }

			// if (input.equals("1")) {
			// 	System.out.println("You proceed deeper into the labyrinth...");
			// } else if (input.equals("2")) {
			// 	System.out.println("You escape the dungeon and have awoken from the dream...for now."); // after finding key to leave maze
			// 	break;
			// }
		}
		in.close(); // close scanner
		System.out.println("%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("Thank you for playing"); // Ending Thanks
		System.out.println("%%%%%%%%%%%%%%%%%%%%%");
	}
	
	public static void printAllItems(BackPack backpack)
	{
		System.out.println("--------------------------------------");
		String list = backpack.listAllItems();
		System.out.print(list);
		System.out.println("--------------------------------------");
		
	}

	public static String[][] cloneGeneratedMaze(int[][] maze, int x) {		// method only takes x for n x n matrix (add int y parameter if n x m matrix required)

		int a = x*2+1;
		int b = x+1;

		String[][] stringMaze = new String[a][b];	// x*2+1 arrays with x+1 length

		// East wall creation
		for(int i = 0; i < a; i+=2){
			stringMaze[i][b-1] = "+";				// store + in the last index of every array (east wall bits)
		}

		for(int i = 1; i < a; i+=2){
			stringMaze[i][b-1] = "|";				// store | at last index of every array (east wall bits)
		}
		// End east wall creation


		// part A, store horizontal walls or open spaces
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < x; j++) {
				stringMaze[j*2][i] = (maze[i][j] & 1) == 0 ? "+---" : "+   ";
			}
		}

		// part B, store vertical walls or open spaces
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < x; j++) {
				stringMaze[j*2+1][i] = (maze[i][j] & 8) == 0 ? "|   " : "    ";
			}
		}

		// store last row
		for(int i = 0; i < x; i++) {
			stringMaze[a-1][i] = "+---";
		}

		return stringMaze;
	}

	public static void generateMonsters(String[][] maze, int x){  //Generate Monsters Chance

		int numberOfMonsters = x * 2;

		for(int i = 0; i < numberOfMonsters; i++){
			Random rand = new Random();
			int jRand = rand.nextInt(x);
			int iRand = rand.nextInt(x);
			if(maze[iRand * 2 + 1][jRand] == "    "){
				maze[iRand * 2 + 1][jRand] = "  X ";
			}
		}

	}
}