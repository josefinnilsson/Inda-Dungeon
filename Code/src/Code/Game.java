package Code;

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
 * Generates a simple game utilizing random level generation.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Game extends Application
{
	public static final int ROOM_WIDTH = 1024;
	public static final int ROOM_HEIGHT = 1024;
	public static final int CELL_WIDTH = 32;
	public static final int CELL_HEIGHT = 32;
	
	private static final int SCALE_X = 2;
	private static final int SCALE_Y = 2;
	
	private static double viewportX;
	private static double viewportY;
	
	private VBox appRoot;
	private Pane gameRoot;
	private Canvas canvas;
	private GraphicsContext gc;
	private HBox uiRoot;
	
	private Player player;
	
	public static int[][] level;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
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
												e.getSceneX()/2-viewportX, 
												e.getSceneY()/2-viewportY));
		scene.setOnMouseReleased(e -> Input.releaseMouse(e.getButton()));
		
		//Initializes the window.
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Random Generator");
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
		//Create a canvas to draw the level on.
		canvas = new Canvas(ROOM_WIDTH, ROOM_HEIGHT);
		gc = canvas.getGraphicsContext2D();
		createLevel(ROOM_WIDTH, ROOM_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
		
		//TODO: Add other game objects
		for(int x = 0; x < ROOM_WIDTH/CELL_WIDTH; x++)
		{
			boolean done = false;
			for(int y = 0; y < ROOM_HEIGHT/CELL_HEIGHT; y++)
			{
				if(level[x][y] == RandomLevelGenerator.FLOOR)
				{
					double playerX = (double) x*CELL_WIDTH+4;
					double playerY = (double) y*CELL_HEIGHT+4;
					player = new Player(playerX, playerY, "Res/" + 
										"Indo.png", 8);
					done = true;
					break;
				}
			}
			if(done) break;
		}
		
		//Draw everything to the canvas and set the correct scale of the view.
		gc.scale(SCALE_X, SCALE_Y);
		
		//Set the viewport's x and y coordinates.
		double viewX = -(player.getX() - ROOM_WIDTH/(4*SCALE_X));
		double viewY = -(player.getY() - ROOM_HEIGHT/(4*SCALE_Y));
		//Make sure the view stays inside the screen.
		viewX = MathMethods.clamp(viewX, 
									-(4*SCALE_X-1)*ROOM_WIDTH/(4*SCALE_X), 0);
		viewY = MathMethods.clamp(viewY, 
									-(4*SCALE_Y-1)*ROOM_HEIGHT/(4*SCALE_Y), 0);
		//Move viewport to player
		gc.translate(viewX, viewY);
		viewportX = viewX;
		viewportY = viewY;
		
		render(gc);
		gameRoot.getChildren().add(canvas);
		
		//Add a button to create new levels
		Button newLevelButton = new Button("NEW LEVEL");
		newLevelButton.setOnAction(e -> 
		{
			gameRoot.getChildren().clear();
			uiRoot.getChildren().clear();
			appRoot.getChildren().clear();
			initiateContent();
		});	
		
		//Add everything to the panes.
		uiRoot.getChildren().add(newLevelButton);
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
		player.render(gc);
		//TODO: Add other objects' render code
	}
	
	/**
	 * This updates the game every frame.
	 */
	private void update()
	{
		//TODO: Add objects' update code
		player.update();
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
		//Draw a black background
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, ROOM_WIDTH, ROOM_HEIGHT);
		
		for(int x = 0; x < roomWidth/cellWidth; x++)
		{
			for(int y = 0; y < roomHeight/cellHeight; y++)
			{
				//Draw the walls
				if(level[x][y] == RandomLevelGenerator.WALL)
				{
					gc.setFill(Color.DARKGRAY);
					gc.fillRect(x*cellWidth, y*cellHeight, 
									cellWidth, cellHeight);
				}
				//Draw the floor
				else if(level[x][y] == RandomLevelGenerator.FLOOR)
				{
					gc.setFill(Color.FORESTGREEN);
					gc.fillRect(x*cellWidth, y*cellHeight, 
									cellWidth, cellHeight);
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
}