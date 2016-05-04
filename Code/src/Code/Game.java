package Code;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The main class for the game. This class controls the creation and 
 * maintenance of the game window, as well as everything in it.
 * 
 * The game is a "dungeon crawler" utilizing randomly generated levels with a 
 * 2.5D perspective. It is created using JavaFX.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Game extends Application
{
	//Random object to add randomness to spawning and things like that.
	private Random r;
	
	//Level size
	public static final int ROOM_WIDTH = 1024;
	public static final int ROOM_HEIGHT = 1024;
	public static final int CELL_WIDTH = 32;
	public static final int CELL_HEIGHT = 32;
	
	//Zoom in for a better view
	private static final int SCALE_X = 2;
	private static final int SCALE_Y = 2;
	
	//Viewport coordinates
	private static double viewportX;
	private static double viewportY;
	
	//The different parts of the game window
	private VBox appRoot;
	private Pane gameRoot;
	private Canvas canvas;
	private GraphicsContext gc;
	private HBox uiRoot;
	
	//This list contains all objects within the game.
	private ArrayList<GameObject> objects;
	
	private Player player;
	private static boolean playerType = true;
	
	public static int[][] level;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		r = new Random();
		
		//Create the different panes and initialize them.
		appRoot = new VBox(2);
		gameRoot = new Pane();
		uiRoot = new HBox(3);
		uiRoot.setAlignment(Pos.CENTER);
		initiateContent();
		
		//Create the scene for the game.
		Scene scene = new Scene(appRoot, ROOM_WIDTH/2, ROOM_HEIGHT/2);
		
		//Add listeners for input.
		scene.setOnKeyPressed(e -> Input.pressKey(e.getCode()));
		scene.setOnKeyReleased(e -> Input.releaseKey(e.getCode()));
		scene.setOnMousePressed(e -> Input.pressMouse(e.getButton(), 
											e.getSceneX()/SCALE_X-viewportX, 
											e.getSceneY()/SCALE_Y-viewportY));
		scene.setOnMouseReleased(e -> Input.releaseMouse(e.getButton()));
		
		//Initialize the window.
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Generic Dungeon Crawler");
		primaryStage.show();
		
		//Game loop
		AnimationTimer timer = new AnimationTimer() 
		{
			@Override
			public void handle(long now)
			{
				update();
				render(gc);
			}
		};
		timer.start();
	}
	
	/**
	 * Initializes the different panes by creating a level, player object, etc.
	 */
	private void initiateContent()
	{
		objects = new ArrayList<GameObject>();
		//Create a canvas to draw the level on.
		canvas = new Canvas(ROOM_WIDTH, ROOM_HEIGHT);
		gc = canvas.getGraphicsContext2D();
		createLevel(ROOM_WIDTH, ROOM_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
		
		//TODO: Add other game objects
		//Add the player to the room
		addPlayer();
		
		//Set the correct scale of the view.
		gc.scale(SCALE_X, SCALE_Y);
		
		//Set the viewport's x and y coordinates. The player object should be
		//in the middle of the view (except for when close to edges).
		double viewX = -(player.getX() - ROOM_WIDTH/(4*SCALE_X));
		double viewY = -(player.getY() - ROOM_HEIGHT/(4*SCALE_Y));
		
		//Make sure the view only shows the level.
		viewX = MathMethods.clamp(viewX, 
									-(4*SCALE_X-1)*ROOM_WIDTH/(4*SCALE_X), 0);
		viewY = MathMethods.clamp(viewY, 
									-(4*SCALE_Y-1)*ROOM_HEIGHT/(4*SCALE_Y), 0);
		//Move viewport to player
		gc.translate(viewX, viewY);
		viewportX = viewX;
		viewportY = viewY;
		
		//Draw everything to the screen.
		render(gc);
		gameRoot.getChildren().add(canvas);
		
		//Add a button to create new levels. Temporary!!!
		Button newLevelButton = new Button("NEW LEVEL");
		newLevelButton.setOnAction(e -> 
		{
			gameRoot.getChildren().clear();
			uiRoot.getChildren().clear();
			appRoot.getChildren().clear();
			initiateContent();
		});	
		
		//Add a button to change sex. Temporary!!!
		Button characterButton = new Button("Change Character");
		characterButton.setOnAction(e -> 
		{
			player.setPlayer(playerType);
			playerType = !playerType;
		});	
		
		//Add everything to the panes.
		uiRoot.getChildren().addAll(newLevelButton, characterButton);
		appRoot.getChildren().addAll(gameRoot, uiRoot);
	}
	
	
	/**
	 * This renders everything to the game pane.
	 */
	private void render(GraphicsContext gc)
	{
		
		//Show only part of the canvas that is within the viewport.
		setViewport();
		//Draws everything onto the canvas.
		drawLevel(gc, ROOM_WIDTH, ROOM_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
	}
	
	/**
	 * This updates the game every frame.
	 */
	private void update()
	{
		for(GameObject object : objects)
		{
			object.update();
		}
	}
	
	/**
	 * Creates the level.
	 * @param roomWidth The width of the level.
	 * @param roomHeight The height of the level.
	 * @param cellWidth The width of a cell within the level.
	 * @param cellHeight The height of a cell within the level.
	 */
	private void createLevel(int roomWidth, int roomHeight,
							int cellWidth, int cellHeight)
	{
		level = RandomLevelGenerator.generateLevel(roomWidth, roomHeight,
													cellWidth, cellHeight);
	}
	
	/**
	 * Draws the level onto the canvas.
	 * @param gc The object to draw with.
	 * @param canvas The canvas to draw on.
	 * @param roomWidth The width of the level.
	 * @param roomHeight The height of the level.
	 * @param cellWidth The width of a cell within the level.
	 * @param cellHeight The height of a cell within the level.
	 */
	private void drawLevel(GraphicsContext gc, 
							int roomWidth, int roomHeight,
							int cellWidth, int cellHeight)
	{
		//Draw the top of the walls
		gc.setFill(new Color(.76863, .41569, .23922, 1));
		gc.fillRect(0, 0, ROOM_WIDTH, ROOM_HEIGHT);
		
		final int FLOOR = RandomLevelGenerator.FLOOR;
		
		//Draw the floor
		TileSet floor = new TileSet("Res/FloorTileInda.png", 1, 1);
		for(int x = 0; x < roomWidth/cellWidth; x++)
		{
			for(int y = 0; y < roomHeight/cellHeight; y++)
			{
				if(level[x][y] == FLOOR)
				{
					gc.setFill(Color.BURLYWOOD);
					floor.draw(gc, x*cellWidth, y*cellHeight, 
								cellWidth, cellHeight, 0, 0);
					
				}
			}
		}
		
		//Walls
		int tileWidth = cellWidth/2;
		int tileHeight = cellHeight/2;
		
		TileSet tiles = new TileSet("Res/IndaWallTiles.png", 5, 3);
		
		//Draw every wall except those who should show up above game objects, to
		//give a 2.5D effect.
		
		//The following might seem like magic but basically this code checks 
		//where walls are in regards to floor tiles, and depending on how 
		//they're positioned chooses what wall tile to draw.
		for(int x = 0; x < roomWidth/cellWidth*2-1; x++)
		{
			for(int y = 0; y < roomHeight/cellHeight*2-1; y++)
			{
				if(level[x/2][y/2] == FLOOR)
				{
					int tileX = x * tileWidth;
					int tileY = y * tileHeight;
					
					//Checks to see what type of tile to add
					boolean right = level[(x+1)/2][y/2] != FLOOR;
					boolean left = level[(x-1)/2][y/2] != FLOOR;
					boolean top = level[x/2][(y-1)/2] != FLOOR;
					boolean bottom = level[x/2][(y+1)/2] != FLOOR;
					
					boolean topRight = level[(x+1)/2][(y-1)/2] != FLOOR;
					boolean topLeft = level[(x-1)/2][(y-1)/2] != FLOOR;
					
					//Draw different tiles depending on the checks above
					if(right)
					{
						if(bottom)
						{
							//Bottom right corner tile
							tiles.draw(gc, tileX+tileWidth, 
							tileY, tileWidth, tileHeight, 4, 1);
						}
						else if(top)
						{
							if(topRight)
							{
								//Top right corner tile
								tiles.draw(gc, tileX+tileWidth, 
								tileY-tileHeight, tileWidth, tileHeight, 4, 0);
							}
							else
							{
								//Top left corner tile
								tiles.draw(gc, tileX, 
								tileY-tileHeight, tileWidth, tileHeight, 3, 0);
							}
							//Right tile
							tiles.draw(gc, tileX+tileWidth, 
							tileY, tileWidth, tileHeight, 0, 1);
						}
						else
						{
							//Right tile
							tiles.draw(gc, tileX+tileWidth, 
							tileY, tileWidth, tileHeight, 0, 1);
						}
					}
					if(left)
					{
						if(bottom)
						{
							//Bottom left corner tile
							tiles.draw(gc, tileX-tileWidth, 
							tileY, tileWidth, tileHeight, 3, 1);
						}
						else if(top)
						{
							if(topLeft)
							{
								//Top left corner tile
								tiles.draw(gc, tileX-tileWidth, 
								tileY-tileHeight, tileWidth, tileHeight, 3, 0);
							}
							else
							{
								//Top right corner tile
								tiles.draw(gc, tileX, 
								tileY-tileHeight, tileWidth, tileHeight, 4, 0);
							}
							//Left tile
							tiles.draw(gc, tileX-tileWidth, 
							tileY, tileWidth, tileHeight, 2, 1);
						}
						else
						{
							//Because of how these loops traverse the level,
							//this if-statement must be checked to make sure
							//that the wrong tile (left tile) is drawn where
							//a top left tile should be.
							if(!(level[(x-1)/2][(y+1)/2] == FLOOR))
							{
								//Left tile
								tiles.draw(gc, tileX-tileWidth, 
								tileY, tileWidth, tileHeight, 2, 1);
							}
						}
					}
					if(top)
					{
						if(!topRight)
						{
							//Top left tile
							tiles.draw(gc, tileX, 
							tileY-tileHeight, tileWidth, tileHeight, 2, 2);
						}
						else if(!topLeft)
						{
							//Top right tile
							tiles.draw(gc, tileX, 
							tileY-tileHeight, tileWidth, tileHeight, 0, 2);
						}
						else
						{
							//Top tile
							tiles.draw(gc, tileX, 
							tileY-tileHeight, tileWidth, tileHeight, 1, 2);
						}
					}
				}
			}
		}
		
		//Draw all the game objects
		for(GameObject object : objects)
		{
			object.render(gc);
		}
		
		//Draw the remaining walls
		for(int x = 0; x < roomWidth/cellWidth*2-1; x++)
		{
			for(int y = 0; y < roomHeight/cellHeight*2-1; y++)
			{
				if(level[x/2][y/2] == FLOOR)
				{
					int tileX = x * tileWidth;
					int tileY = y * tileHeight;
					
					//Get checks to see what type of tile to add
					boolean bottom = level[x/2][(y+1)/2] != FLOOR;
					boolean bottomRight = level[(x+1)/2][(y+1)/2] != FLOOR;
					boolean bottomLeft = level[(x-1)/2][(y+1)/2] != FLOOR;
					
					//Draw depending on the checks above
					if(bottom)
					{
						if(!bottomRight)
						{
							//Bottom left tile
							tiles.draw(gc, tileX, 
							tileY+1, tileWidth, tileHeight, 2, 0);
						}
						else if(!bottomLeft)
						{
							//Bottom right tile
							tiles.draw(gc, tileX, 
							tileY+1, tileWidth, tileHeight, 0, 0);
						}
						else
						{
							//Bottom tile
							tiles.draw(gc, tileX, 
							tileY+1, tileWidth, tileHeight, 1, 0);
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
		
		if(player.getX() > ROOM_WIDTH/(4*SCALE_X) && 
						player.getX() < (4*SCALE_X-1)*ROOM_WIDTH/(4*SCALE_X))
		{
			gc.translate(viewX, 0);
			viewportX += viewX;
		}
		if(player.getY() > ROOM_HEIGHT/(4*SCALE_Y) && 
						player.getY() < (4*SCALE_Y-1)*ROOM_HEIGHT/(4*SCALE_Y))
		{
			gc.translate(0, viewY);
			viewportY += viewY;
		}
	}
	
	/**
	 * Finds a place to spawn the player object and puts it there.
	 */
	private void addPlayer()
	{
		int x = r.nextInt(ROOM_WIDTH/CELL_WIDTH);
		int y = r.nextInt(ROOM_HEIGHT/CELL_HEIGHT);
		while(level[x][y] != RandomLevelGenerator.FLOOR)
		{
			x = r.nextInt(ROOM_WIDTH/CELL_WIDTH);
			y = r.nextInt(ROOM_HEIGHT/CELL_HEIGHT);
		}
		double playerX = (double) x*CELL_WIDTH+4;
		double playerY = (double) y*CELL_HEIGHT+4;
		player = new Player(playerX, playerY);
		objects.add(player);
	}
}