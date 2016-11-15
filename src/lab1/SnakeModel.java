package lab1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;

import lab1.GoldModel.Directions;

/**
 * Classic game snake.
 * <p>
 * Initially the snake, which consist of one tile, is placed on the gameboard.
 * One foodtile is placed on random on the gamefield. The snake is controlled by the
 * arrow keys on the keyboard. If the snake eats the food, it's length adds by one, 
 * and a new foodtile is placed by random on the gameboard.
 */
public class SnakeModel extends GameModel{

		public enum Directions {
			EAST(1, 0),
			WEST(-1, 0),
			NORTH(0, -1),
			SOUTH(0, 1),
			NONE(0, 0);

			private final int xDelta;
			private final int yDelta;

			Directions(final int xDelta, final int yDelta) {
				this.xDelta = xDelta;
				this.yDelta = yDelta;
			}

			public int getXDelta() {
				return this.xDelta;
			}

			public int getYDelta() {
				return this.yDelta;
			}
		}

		/** Graphical representation of a food-tile. */
		private static final GameTile FOOD_TILE = new RoundTile(new Color(255, 215,
				0),
				Color.BLACK, 2.0);

		/** Graphical representation of the snake's head while heading North */
		private static final GameTile SNAKEHEAD_TILE = new SnakeHeadTile(Color.BLACK);
		
		/** Graphical representation of the snake's head while heading South */
		private static final GameTile SNAKEHEAD_TILE_SOUTH = new SnakeHeadTileSouth(Color.BLACK);
		/** Graphical representation of the snake's head while heading West */
		private static final GameTile SNAKEHEAD_TILE_WEST = new SnakeHeadTileWest(Color.BLACK);
		/** Graphical representation of the snake's head while heading East*/
		private static final GameTile SNAKEHEAD_TILE_EAST = new SnakeHeadTileEast(Color.BLACK);
		
		/** Graphical representation of the snake's tail */
		private static final GameTile SNAKETAIL_TILE = new RoundTile(Color.BLACK,
				new Color(255, 255, 0), 2.0);

		/** Graphical representation of a blank tile. */
		private static final GameTile BLANK_TILE = new GameTile();

		/** The position of the food */
		private Position food;

		/** The position of the snakes head */
		private Position snakeHeadPos;
		
		/** Deque of position of the snake's tail */
		private ArrayDeque<Position> tailPos = new ArrayDeque<Position>();

		/** The direction of the snake. */
		private Directions direction = Directions.NORTH;

		/** The number of food found. */
		private int score;

		/**
		 * Create a new model for the gold game.
		 */
		public SnakeModel() {
			Dimension size = getGameboardSize();

			// Blank out the whole gameboard
			for (int i = 0; i < size.width; i++) {
				for (int j = 0; j < size.height; j++) {
					setGameboardState(i, j, BLANK_TILE);
				}
			}

			// Insert the collector in the middle of the gameboard.
			this.snakeHeadPos = new Position(size.width / 2, size.height / 2);
			setGameboardState(this.snakeHeadPos, SNAKEHEAD_TILE);

			// Insert food into the game board. 
				addFood();
		}

		/**
		 * Insert another food into the game board.
		 */
		private void addFood() {
			Position newFoodPos;
			Dimension size = getGameboardSize();
			// Loop until a blank position is found and ...
			do {
				newFoodPos = new Position((int) (Math.random() * size.width),
											(int) (Math.random() * size.height));
			} while (!isPositionEmpty(newFoodPos));

			// ... add a new food to the empty tile.
			setGameboardState(newFoodPos, FOOD_TILE);
			this.food = newFoodPos;
		}

		/**
		 * Return whether the specified position is empty.
		 * 
		 * @param pos
		 *            The position to test.
		 * @return true if position is empty.
		 */
		private boolean isPositionEmpty(final Position pos) {
			return (getGameboardState(pos) == BLANK_TILE);
		}

		/**
		 * Update the direction of the collector
		 * according to the user's key punch.
		 */
		private void updateDirection(final int key) {
			switch (key) {
				case KeyEvent.VK_LEFT:
					this.direction = Directions.WEST;
					break;
				case KeyEvent.VK_UP:
					this.direction = Directions.NORTH;
					break;
				case KeyEvent.VK_RIGHT:
					this.direction = Directions.EAST;
					break;
				case KeyEvent.VK_DOWN:
					this.direction = Directions.SOUTH;
					break;
				default:
					// Don't change direction if another key is pressed
					break;
			}
		}
		
		/**
		 * @param direction
		 * @return opposite direction
		 */
		private Directions oppositeDirection(Directions direction){
			if (direction == Directions.NORTH){return Directions.SOUTH;}
			if (direction == Directions.SOUTH){return Directions.NORTH;}
			if (direction == Directions.WEST){return Directions.EAST;}
			if (direction == Directions.EAST){return Directions.WEST;}
			else return Directions.NORTH;
		}
		
		/**
		 * @param key
		 * @return direction related to the key
		 */
		private Directions directionOfKey(final int key){
			if(key == KeyEvent.VK_LEFT){return Directions.WEST;}
			if(key == KeyEvent.VK_RIGHT){return Directions.EAST;}
			if(key == KeyEvent.VK_DOWN){return Directions.SOUTH;}
			if(key == KeyEvent.VK_UP){return Directions.NORTH;}
			else return this.direction;
		}

		/**
		 * Get next position of the snake head.
		 */
		private Position getNextSnakeHeadPos() {
			return new Position(
					this.snakeHeadPos.getX() + this.direction.getXDelta(),
					this.snakeHeadPos.getY() + this.direction.getYDelta());
		}
		

		/**
		 * This method is called repeatedly so that the
		 * game can update its state.
		 * 
		 * @param lastKey
		 *            The most recent keystroke.
		 */
		@Override
		public void gameUpdate(final int lastKey) throws GameOverException {
			if(directionOfKey(lastKey) == this.direction){}
			if(oppositeDirection(directionOfKey(lastKey)) == this.direction){}
			else updateDirection(lastKey);
			 
			
			// Change snake tail position
			this.tailPos.addFirst(this.snakeHeadPos);

			// Change snake head position position.
			this.snakeHeadPos = getNextSnakeHeadPos();
			
			//Check if out of bounds
			if (isOutOfBounds(this.snakeHeadPos) || tailPos.contains(this.snakeHeadPos)) {
				throw new GameOverException(this.score);
			}

			// Remove the food at the new snake head position(if any) and add one to snakes tale
			if (this.food.equals(this.snakeHeadPos)){
				this.tailPos.add(food);
				this.score++;
				addFood();
			}
			
			// Draw snake head and snake tail at new position and draw blank at end
			if (directionOfKey(lastKey)==Directions.NORTH){
				setGameboardState(this.snakeHeadPos, SNAKEHEAD_TILE);
			}else if (directionOfKey(lastKey)==Directions.SOUTH){
				setGameboardState(this.snakeHeadPos, SNAKEHEAD_TILE_SOUTH);
			}else if (directionOfKey(lastKey)==Directions.WEST){
				setGameboardState(this.snakeHeadPos, SNAKEHEAD_TILE_WEST);
			}else{
				setGameboardState(this.snakeHeadPos, SNAKEHEAD_TILE_EAST);
			}
			
			setGameboardState(this.tailPos.getFirst(), SNAKETAIL_TILE);
			setGameboardState(this.tailPos.getLast(), BLANK_TILE);
			this.tailPos.removeLast();
		}

			// Check if the snake covers the whole game field hej

		/**
		 * 
		 * @param pos The position to test.
		 * @return <code>false</code> if the position is outside the playing field, <code>true</code> otherwise.
		 */
		private boolean isOutOfBounds(Position pos) {
			return pos.getX() < 0 || pos.getX() >= getGameboardSize().width
					|| pos.getY() < 0 || pos.getY() >= getGameboardSize().height;
		}


}


