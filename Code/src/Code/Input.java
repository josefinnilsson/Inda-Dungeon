package Code;

import java.util.HashMap;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/**
 * This class handles input by adding and removing key pressed to a map as
 * boolean values.
 * 
 * @author Fredrik Omstedt
 * @version 1.0.0
 */
public class Input
{
	//This map contains values for every key that has been pressed.
	//True means that it is currently being pressed, false if not.
	public static HashMap<KeyCode, Boolean> keys = new HashMap<>();
	public static HashMap<MouseButton, Boolean> mouseButtons = new HashMap<>();
	
	public static double mouseX = 0;
	public static double mouseY = 0;
	
	/**
	 * Returns whether a given key is currently pressed.
	 * @param key The key to check for.
	 * @return true if the key is being pressed, false otherwise.
	 */
	public static boolean keyPressed(KeyCode key)
	{
		return keys.getOrDefault(key, false);
	}
	
	/**
	 * Sets a given key to being pressed.
	 * @param key The key to check as true.
	 */
	public static void pressKey(KeyCode key)
	{
		keys.put(key, true);
	}
	
	/**
	 * Sets a given key to being released.
	 * @param key The key to check as false.
	 */
	public static void releaseKey(KeyCode key)
	{
		keys.put(key, false);
	}
	
	/**
	 * Sets a given mouse button to being pressed.
	 * @param button The button to check as true.
	 */
	public static void pressMouse(MouseButton button, double x, double y)
	{
		mouseButtons.put(button, true);
		setMouseCoordinates(x, y);
	}
	
	/**
	 * Sets a given mouse button to being released.
	 * @param button The button to check as false.
	 */
	public static void releaseMouse(MouseButton button)
	{
		mouseButtons.put(button, false);
	}
	
	/**
	 * Sets the mouse coordinates to the given values.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public static void setMouseCoordinates(double x, double y)
	{
		mouseX = x;
		mouseY = y;
	}
}
