package Code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The main class for the game. This class controls the creation and maintenance
 * of the game window, as well as everything in it.
 * 
 * The game is a "dungeon crawler" utilizing randomly generated levels with a
 * 2.5D perspective. It is created using JavaFX.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Game extends Application
{
	// Random object to add randomness to spawning and things like that.
	private Random r;

	// Level size
	public static final int ROOM_WIDTH = 1024;
	public static final int ROOM_HEIGHT = 1024;
	public static final int CELL_WIDTH = 32;
	public static final int CELL_HEIGHT = 32;

	// Zoom in for a better view
	public static final int SCALE_X = 2;
	public static final int SCALE_Y = 2;

	// Viewport coordinates
	private static double viewportX;
	private static double viewportY;

	// The different parts of the game window
	private Pane introRoot;
	private Pane menuRoot;
	private StackPane appRoot;

	private Pane gameRoot;
	private Canvas canvas;
	private GraphicsContext gc;

	private Pane uiRoot;
	private Canvas uiCanvas;
	private GraphicsContext uiGc;
	private Sprite uiBar;

	// This list contains all objects within the game.
	public static ArrayList<GameObject> objects;

	// This list contains objects that need to be added to the objects list but
	// are created during iteration of the objects list.
	public static ArrayList<GameObject> objectWaitingRoom;

	// This variable is used to declare the id of every object within the game.
	public static long objectsID;

	public static Player player;
	public static Stairs stairs;

	private Snail snail;
	private Snake snake;
	private Spider spider;

	public static int[][] level;

	private int currentLevel;
	private int enemiesKilled;
	private int amountOfEnemies;
	private boolean stairsCreated;

	public static boolean playerGender;
	private boolean chosenGender;

	public static void main(String[] args)
	{
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		objectsID = 0;
		r = new Random();

		menuRoot = new Pane();
		chosenGender = false;
		
		introRoot = new Pane();

		// Create an intro for the game.
		Intro intro = new Intro();

		// Create the scene for the intro.
		Scene scene = new Scene(menuRoot, ROOM_WIDTH / 2, ROOM_HEIGHT / 2);

		// Initialize the window.
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Inda Dungeon");
		primaryStage.show();

		//The main menu
		menuRoot.setStyle(
				"-fx-background-image: url(/Code/indaBackground.png)");

		//Button for boy
		Button boyButton = new Button("Boy");
		boyButton.setOnAction(e ->
		{
			playerGender = true;
			chosenGender = true;
			primaryStage.setScene(new Scene(introRoot, ROOM_WIDTH / 2, ROOM_HEIGHT / 2));
		});
		menuRoot.getChildren().add(boyButton);
		boyButton.setLayoutX(160);
		boyButton.setLayoutY(220);
		boyButton.setScaleX(2);
		boyButton.setScaleY(2);
		boyButton.setStyle(
				"-fx-font: 16 kefa; -fx-base: #522b25;-fx-text-fill: #000000");

		//Button for girl
		Button girlButton = new Button("Girl");
		girlButton.setOnAction(e ->
		{
			playerGender = false;
			chosenGender = true;
			primaryStage.setScene(new Scene(introRoot, ROOM_WIDTH / 2, ROOM_HEIGHT / 2));
		});
		menuRoot.getChildren().add(girlButton);
		girlButton.setLayoutX(310);
		girlButton.setLayoutY(220);
		girlButton.setScaleX(2);
		girlButton.setScaleY(2);
		girlButton.setStyle("-fx-font: 16 kefa; -fx-base: #522b25;"
				+ "-fx-text-fill: #000000;");

		Text welcome = new Text("Welcome to the Dungeon");
		welcome.setStyle("-fx-font: 45 kefa");
		menuRoot.getChildren().add(welcome);
		welcome.setLayoutX(6);
		welcome.setLayoutY(120);
		Text welcome2 = new Text("Choose your character to start your journey");
		welcome2.setStyle("-fx-font: 25 kefa");
		menuRoot.getChildren().add(welcome2);
		welcome2.setLayoutX(11);
		welcome2.setLayoutY(150);

		// Add the intro to the window
		introRoot.getChildren().addAll(intro.getCanvas());

		// Intro loop
		AnimationTimer timer = new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				if(intro.finished())
				{
					stop();
					// Start the game
					initiateLevelContent(primaryStage);
				}
				if(chosenGender)
				{
					intro.render();
				}
			}
		};
		timer.start();

		// Create the different panes for the actual game and initialize them.
		appRoot = new StackPane();
		gameRoot = new Pane();
		uiRoot = new Pane();

		Sound sound = new Sound("bitsong.wav");
		sound.play();
		sound.loop();

	}

	/**
	 * Initializes the different panes by creating a level, player object, etc.
	 */
	private void initiateLevelContent(Stage primaryStage)
	{
		objects = new ArrayList<GameObject>();
		objectWaitingRoom = new ArrayList<GameObject>();
		currentLevel = 1;
		enemiesKilled = 0;
		stairsCreated = false;

		// Create a canvas to draw the level on.
		canvas = new Canvas(ROOM_WIDTH, ROOM_HEIGHT);
		gc = canvas.getGraphicsContext2D();
		createLevel(ROOM_WIDTH, ROOM_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

		// Add the player to the room
		addPlayer();

		// Add enemies to the room
		amountOfEnemies = addEnemies();

		// Set the correct scale of the view.
		gc.scale(SCALE_X, SCALE_Y);

		// Set the viewport's x and y coordinates. The player object should be
		// in the middle of the view (except for when close to edges).
		double viewX = -(player.getX() - ROOM_WIDTH / (4 * SCALE_X));
		double viewY = -(player.getY() - ROOM_HEIGHT / (4 * SCALE_Y));

		// Make sure the view only shows the level.
		viewX = MathMethods.clamp(viewX,
				-(4 * SCALE_X - 2) * ROOM_WIDTH / (4 * SCALE_X), 0);
		viewY = MathMethods.clamp(viewY,
				-(4 * SCALE_Y - 2) * ROOM_HEIGHT / (4 * SCALE_Y), 0);
		// Move viewport to player
		gc.translate(viewX, viewY);
		viewportX = viewX;
		viewportY = viewY;

		// Set up the UI
		uiCanvas = new Canvas(ROOM_WIDTH, ROOM_HEIGHT);
		uiGc = uiCanvas.getGraphicsContext2D();
		uiBar = new Sprite("Res/indaUIBar.png", 1);

		// Draw everything to the screen.
		render(gc, uiGc);
		gameRoot.getChildren().add(canvas);

		// Add everything to the panes.
		uiRoot.getChildren().add(uiCanvas);
		appRoot.getChildren().addAll(gameRoot, uiRoot);

		// Create the scene for the levels.
		Scene scene = new Scene(appRoot, ROOM_WIDTH / 2, ROOM_HEIGHT / 2);

		// Add listeners for input.
		scene.setOnKeyPressed(e -> Input.pressKey(e.getCode()));
		scene.setOnKeyReleased(e -> Input.releaseKey(e.getCode()));
		scene.setOnMousePressed(e -> Input.pressMouse(e.getButton(),
				e.getSceneX() / SCALE_X - viewportX,
				e.getSceneY() / SCALE_Y - viewportY));
		scene.setOnMouseReleased(e -> Input.releaseMouse(e.getButton()));

		// Initialize the game window.
		primaryStage.setScene(scene);

		// Game loop
		AnimationTimer timer = new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				update();
				render(gc, uiGc);
			}
		};
		timer.start();
	}

	/**
	 * Creates the next level and puts the player and viewport correctly in it.
	 */
	public void nextLevel()
	{
		currentLevel++;
		enemiesKilled = 0;
		stairsCreated = false;

		// Create a new canvas to use for the next level
		gameRoot.getChildren().clear();
		canvas = new Canvas(ROOM_WIDTH, ROOM_HEIGHT);
		gc = canvas.getGraphicsContext2D();

		// Create a new level
		if(currentLevel < 10)
		{
			createLevel(ROOM_WIDTH, ROOM_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
		}
		else
		{
			createBossLevel(ROOM_WIDTH, ROOM_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
			objects.add(new Monster(ROOM_WIDTH / 2 - 32, 2 * ROOM_HEIGHT / 5));
		}

		// Add the player to the room
		setPlayer();

		// Add the enemies to the room
		if(currentLevel < 10)
		{
			amountOfEnemies = addEnemies();
		}

		// Scale the view
		gc.scale(SCALE_X, SCALE_Y);

		// Set the viewport's x and y coordinates. The player object should be
		// in the middle of the view (except for when close to edges).
		double viewX = -(player.getX() - ROOM_WIDTH / (4 * SCALE_X));
		double viewY = -(player.getY() - ROOM_HEIGHT / (4 * SCALE_Y));

		// Make sure the view only shows the level.
		viewX = MathMethods.clamp(viewX,
				-(4 * SCALE_X - 2) * ROOM_WIDTH / (4 * SCALE_X), 0);
		viewY = MathMethods.clamp(viewY,
				-(4 * SCALE_Y - 2) * ROOM_HEIGHT / (4 * SCALE_Y), 0);
		// Move viewport to player
		gc.translate(viewX, viewY);
		viewportX = viewX;
		viewportY = viewY;

		// Draw everything to the screen.
		render(gc, uiGc);
		gameRoot.getChildren().add(canvas);
	}

	/**
	 * This renders everything to the game pane.
	 */
	private void render(GraphicsContext gc, GraphicsContext uiGc)
	{
		// Sort the objects so they're drawn in the correct order.
		Collections.sort(objects);
		// Show only part of the canvas that is within the viewport.
		setViewport();
		// Draws everything onto the canvas.
		drawLevel(gc, ROOM_WIDTH, ROOM_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
		drawUI(uiGc);
	}

	/**
	 * This updates the game every frame.
	 */
	private void update()
	{

		// Add the objects made during previous iteration and empty waiting
		// room.
		objects.addAll(objectWaitingRoom);
		objectWaitingRoom.clear();

		Iterator<GameObject> it = objects.iterator();
		while(it.hasNext())
		{
			GameObject object = it.next();
			object.update();

			// Remove objects that are no more
			if(object instanceof Damage)
			{
				if(((Damage) object).hasDamaged())
				{
					it.remove();
				}
			}
			else if(object instanceof PlayerAttackSlash)
			{
				if(((PlayerAttackSlash) object).isFinished())
				{
					it.remove();
				}
			}
			else if(object instanceof LifeForm)
			{
				if(((LifeForm) object).isDead())
				{
					if(object instanceof Enemy)
					{
						enemiesKilled++;
					}
					it.remove();
				}
			}
			else if(object instanceof Projectile)
			{
				if(((Projectile) object).shouldRemove())
				{
					it.remove();
				}
			}
			else if(object instanceof Pickable)
			{
				if(((Pickable) object).isPicked())
				{
					it.remove();
				}
			}
		}

		// Create stairs if enough enemies are killed
		if(((double) enemiesKilled) / ((double) amountOfEnemies) > 0.8
				&& !stairsCreated)
		{
			stairsCreated = true;
			createStairs();
		}

		// Check if it's time to go to next level
		if(currentLevel < 10)
		{
			isPlayerAtStairs();
		}
	}

	/**
	 * Creates the level.
	 *
	 * @param roomWidth The width of the level.
	 * @param roomHeight The height of the level.
	 * @param cellWidth The width of a cell within the level.
	 * @param cellHeight The height of a cell within the level.
	 */
	private void createLevel(int roomWidth, int roomHeight, int cellWidth,
			int cellHeight)
	{
		level = RandomLevelGenerator.generateLevel(roomWidth, roomHeight,
				cellWidth, cellHeight);
	}

	/**
	 * Creates the boss level.
	 *
	 * @param roomWidth The width of the level.
	 * @param roomHeight The height of the level.
	 * @param cellWidth The width of a cell within the level.
	 * @param cellHeight The height of a cell within the level.
	 */
	private void createBossLevel(int roomWidth, int roomHeight, int cellWidth,
			int cellHeight)
	{
		level = RandomLevelGenerator.generateBossLevel(roomWidth, roomHeight,
				cellWidth, cellHeight);
	}

	/**
	 * Draws the level onto the canvas.
	 *
	 * @param gc The object to draw with.
	 * @param canvas The canvas to draw on.
	 * @param roomWidth The width of the level.
	 * @param roomHeight The height of the level.
	 * @param cellWidth The width of a cell within the level.
	 * @param cellHeight The height of a cell within the level.
	 */
	private void drawLevel(GraphicsContext gc, int roomWidth, int roomHeight,
			int cellWidth, int cellHeight)
	{
		// Draw the top of the walls
		gc.setFill(new Color(.76863, .41569, .23922, 1));
		gc.fillRect(0, 0, ROOM_WIDTH, ROOM_HEIGHT);

		final int FLOOR = RandomLevelGenerator.FLOOR;

		// Draw the floor
		TileSet floor = new TileSet("Res/FloorTileInda.png", 1, 1);
		for(int x = 0; x < roomWidth / cellWidth; x++)
		{
			for(int y = 0; y < roomHeight / cellHeight; y++)
			{
				if(level[x][y] == FLOOR)
				{
					gc.setFill(Color.BURLYWOOD);
					floor.draw(gc, x * cellWidth, y * cellHeight, cellWidth,
							cellHeight, 0, 0);
				}
			}
		}

		// Walls
		int tileWidth = cellWidth / 2;
		int tileHeight = cellHeight / 2;

		TileSet tiles = new TileSet("Res/IndaWallTiles.png", 5, 3);

		// Draw every wall except those who should show up above game objects,
		// to
		// give a 2.5D effect.

		// The following might seem like magic but basically this code checks
		// where walls are in regards to floor tiles, and depending on how
		// they're positioned chooses what wall tile to draw.
		for(int x = 0; x < roomWidth / cellWidth * 2 - 1; x++)
		{
			for(int y = 0; y < roomHeight / cellHeight * 2 - 1; y++)
			{
				if(level[x / 2][y / 2] == FLOOR)
				{
					int tileX = x * tileWidth;
					int tileY = y * tileHeight;

					// Checks to see what type of tile to add
					boolean right = level[(x + 1) / 2][y / 2] != FLOOR;
					boolean left = level[(x - 1) / 2][y / 2] != FLOOR;
					boolean top = level[x / 2][(y - 1) / 2] != FLOOR;
					boolean bottom = level[x / 2][(y + 1) / 2] != FLOOR;

					boolean topRight = level[(x + 1) / 2][(y - 1) / 2] != FLOOR;
					boolean topLeft = level[(x - 1) / 2][(y - 1) / 2] != FLOOR;

					// Draw different tiles depending on the checks above
					if(right)
					{
						if(bottom)
						{
							// Bottom right corner tile
							tiles.draw(gc, tileX + tileWidth, tileY, tileWidth,
									tileHeight, 4, 1);
						}
						else if(top)
						{
							if(topRight)
							{
								// Top right corner tile
								tiles.draw(gc, tileX + tileWidth,
										tileY - tileHeight, tileWidth,
										tileHeight, 4, 0);
							}
							else
							{
								// Top left corner tile
								tiles.draw(gc, tileX, tileY - tileHeight,
										tileWidth, tileHeight, 3, 0);
							}
							// Right tile
							tiles.draw(gc, tileX + tileWidth, tileY, tileWidth,
									tileHeight, 0, 1);
						}
						else
						{
							// Right tile
							tiles.draw(gc, tileX + tileWidth, tileY, tileWidth,
									tileHeight, 0, 1);
						}
					}
					if(left)
					{
						if(bottom)
						{
							// Bottom left corner tile
							tiles.draw(gc, tileX - tileWidth, tileY, tileWidth,
									tileHeight, 3, 1);
						}
						else if(top)
						{
							if(topLeft)
							{
								// Top left corner tile
								tiles.draw(gc, tileX - tileWidth,
										tileY - tileHeight, tileWidth,
										tileHeight, 3, 0);
							}
							else
							{
								// Top right corner tile
								tiles.draw(gc, tileX, tileY - tileHeight,
										tileWidth, tileHeight, 4, 0);
							}
							// Left tile
							tiles.draw(gc, tileX - tileWidth, tileY, tileWidth,
									tileHeight, 2, 1);
						}
						else
						{
							// Because of how these loops traverse the level,
							// this if-statement must be checked to make sure
							// that the wrong tile (left tile) is drawn where
							// a top left tile should be.
							if(!(level[(x - 1) / 2][(y + 1) / 2] == FLOOR))
							{
								// Left tile
								tiles.draw(gc, tileX - tileWidth, tileY,
										tileWidth, tileHeight, 2, 1);
							}
						}
					}
					if(top)
					{
						if(!topRight)
						{
							// Top left tile
							tiles.draw(gc, tileX, tileY - tileHeight, tileWidth,
									tileHeight, 2, 2);
						}
						else if(!topLeft)
						{
							// Top right tile
							tiles.draw(gc, tileX, tileY - tileHeight, tileWidth,
									tileHeight, 0, 2);
						}
						else
						{
							// Top tile
							tiles.draw(gc, tileX, tileY - tileHeight, tileWidth,
									tileHeight, 1, 2);
						}
					}
				}
			}
		}

		// Draw all the game objects
		for(GameObject object : objects)
		{
			object.render(gc);
		}

		// Draw the remaining walls
		for(int x = 0; x < roomWidth / cellWidth * 2 - 1; x++)
		{
			for(int y = 0; y < roomHeight / cellHeight * 2 - 1; y++)
			{
				if(level[x / 2][y / 2] == FLOOR)
				{
					int tileX = x * tileWidth;
					int tileY = y * tileHeight;

					// Get checks to see what type of tile to add
					boolean bottom = level[x / 2][(y + 1) / 2] != FLOOR;
					boolean bottomRight = level[(x + 1) / 2][(y + 1)
							/ 2] != FLOOR;
					boolean bottomLeft = level[(x - 1) / 2][(y + 1)
							/ 2] != FLOOR;

					// Draw depending on the checks above
					if(bottom)
					{
						if(!bottomRight)
						{
							// Bottom left tile
							tiles.draw(gc, tileX, tileY + 1, tileWidth,
									tileHeight, 2, 0);
						}
						else if(!bottomLeft)
						{
							// Bottom right tile
							tiles.draw(gc, tileX, tileY + 1, tileWidth,
									tileHeight, 0, 0);
						}
						else
						{
							// Bottom tile
							tiles.draw(gc, tileX, tileY + 1, tileWidth,
									tileHeight, 1, 0);
						}
					}
				}
			}
		}
	}

	/**
	 * Modifies the viewport to show player and its surroundings.
	 */
	private void setViewport()
	{
		double viewX = -(player.getX() - player.getPrevX());
		double viewY = -(player.getY() - player.getPrevY());

		if(player.getX() > ROOM_WIDTH / (4 * SCALE_X) && player
				.getX() < (4 * SCALE_X - 1) * ROOM_WIDTH / (4 * SCALE_X))
		{
			gc.translate(viewX, 0);
			viewportX += viewX;
		}
		if(player.getY() > ROOM_HEIGHT / (4 * SCALE_Y) && player
				.getY() < (4 * SCALE_Y - 1) * ROOM_HEIGHT / (4 * SCALE_Y))
		{
			gc.translate(0, viewY);
			viewportY += viewY;
		}
	}

	/**
	 * Finds a place to spawn the player object for the first level and puts it
	 * there.
	 */
	private void addPlayer()
	{
		int x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
		int y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
		while(level[x][y] != RandomLevelGenerator.FLOOR)
		{
			x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
			y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
		}
		double playerX = (double) x * CELL_WIDTH + 4;
		double playerY = (double) y * CELL_HEIGHT + 4;
		player = new Player(playerX, playerY);
		player.setPlayer(playerGender);
		objects.add(player);
	}

	private void addSnail()
	{
		int x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
		int y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
		while(level[x][y] != RandomLevelGenerator.FLOOR)
		{
			x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
			y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
		}
		double snailX = (double) x * CELL_WIDTH + 4;
		double snailY = (double) y * CELL_HEIGHT + 4;
		snail = new Snail(snailX, snailY);
		objects.add(snail);
	}

	private void addSnake()
	{
		int x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
		int y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
		while(level[x][y] != RandomLevelGenerator.FLOOR)
		{
			x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
			y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
		}
		double snakeX = (double) x * CELL_WIDTH + 4;
		double snakeY = (double) y * CELL_HEIGHT + 4;
		snake = new Snake(snakeX, snakeY);
		objects.add(snake);
	}

	private void addSpider()
	{
		int x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
		int y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
		while(level[x][y] != RandomLevelGenerator.FLOOR)
		{
			x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
			y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
		}
		double spiderX = (double) x * CELL_WIDTH + 4;
		double spiderY = (double) y * CELL_HEIGHT + 4;
		spider = new Spider(spiderX, spiderY);
		objects.add(spider);
	}

	/**
	 * Finds a place to spawn the player object on the new level.
	 */
	private void setPlayer()
	{
		if(currentLevel < 10)
		{
			int x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
			int y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
			while(level[x][y] != RandomLevelGenerator.FLOOR)
			{
				x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
				y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
			}
			double playerX = (double) x * CELL_WIDTH + 4;
			double playerY = (double) y * CELL_HEIGHT + 4;
			player.setX(playerX);
			player.setY(playerY);
		}
		else
		{
			player.setX(ROOM_WIDTH / 2 - 8);
			player.setY(4 * ROOM_HEIGHT / 5);
		}
	}

	private void isPlayerAtStairs()
	{
		if(stairs != null && player.collidesWith(stairs))
		{
			levelCompleted();
		}
	}

	private void levelCompleted()
	{
		stairs = null;

		// Remove all remaining objects that aren't the player
		Iterator<GameObject> it = objects.iterator();
		while(it.hasNext())
		{
			GameObject object = it.next();
			if(!(object instanceof Player))
			{
				it.remove();
			}
		}
		// Goto next level
		nextLevel();
	}

	/**
	 * Draws the UI to the screen.
	 *
	 * @param uiGc The graphics object to draw with.
	 */
	private void drawUI(GraphicsContext uiGc)
	{
		// Clear previously drawn stuff
		uiGc.clearRect(16, 16, 128, 32);
		uiGc.clearRect(16, 64, 128, 32);
		uiGc.clearRect(16, 104, 128, 32);

		// Player health
		uiGc.setFill(Color.RED);
		uiGc.fillRect(16, 16,
				(player.getHealth() * 128) / player.getMaxHealth(), 32);
		uiBar.draw(uiGc, 16, 16, uiBar.getCellWidth(), uiBar.getCellHeight());
		// Player stamina
		uiGc.setFill(Color.FORESTGREEN);
		uiGc.fillRect(16, 64,
				(player.getStamina() * 128) / player.getMaxStamina(), 32);
		uiBar.draw(uiGc, 16, 64, uiBar.getCellWidth(), uiBar.getCellHeight());

		// Draw text showing stairs have spawned
		Sprite spawnedStairs = new Sprite("Res/indaStairsSpawned.png", 1);
		if(stairsCreated)
		{
			spawnedStairs.draw(uiGc, 16, 104, spawnedStairs.getCellWidth(),
					spawnedStairs.getCellHeight());
		}
	}

	/**
	 * Creates a stair object at the location generated in the method.
	 */
	private void createStairs()
	{
		int x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
		int y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
		while(!(level[x][y] == RandomLevelGenerator.FLOOR
				&& (level[x - 1][y] == RandomLevelGenerator.WALL
						|| level[x + 1][y] == RandomLevelGenerator.WALL
						|| level[x][y - 1] == RandomLevelGenerator.WALL)
				&& level[x][y + 1] != RandomLevelGenerator.WALL))
		{
			x = r.nextInt(ROOM_WIDTH / CELL_WIDTH);
			y = r.nextInt(ROOM_HEIGHT / CELL_HEIGHT);
		}
		double stairX = (double) x * CELL_WIDTH;
		double stairY = (double) y * CELL_HEIGHT;
		stairs = new Stairs(stairX, stairY);
		objects.add(stairs);
	}

	/**
	 * Creates the enemies based on the current level.
	 * @return the amount of enemies created
	 */
	private int addEnemies()
	{
		int cr = currentLevel;

		// These functions were created using polynomial interpolation
		double amountOfSnails = 1.161 * cr * cr + 11.518 * cr + 2.321;
		double amountOfSnakes = 0.1786 * cr * cr + 9.464 * cr + 0.3571;
		double amountOfSpiders = 0.625 * cr * cr + 3.125 * cr + 1.25;

		// Add the enemies depending on the above functions.
		for(int i = 0; i < (int) amountOfSnails; i++)
		{
			addSnail();
		}

		for(int i = 0; i < (int) amountOfSnakes; i++)
		{
			addSnake();
		}

		for(int i = 0; i < (int) amountOfSpiders; i++)
		{
			addSpider();
		}

		return (int) amountOfSnails + (int) amountOfSnakes
				+ (int) amountOfSpiders;
	}
}