
/**
 * Represents a movable piece that the bot owns
 */
public class Piece {
	private boolean moving;      // is the piece moving
	private Location location;   // location of piece
	private Direction direction; // direction piece is moving
	private GameMap map;         // the map of the game
	private Direction spiralDir; // direction to spiral in
	
	public Piece(Location location, GameMap map, Direction spiralDir) {
		this.moving    = false;
		this.location  = location;
		this.direction = Direction.STILL;
		this.map       = map;
		this.spiralDir = spiralDir;
	}
	
	public boolean   isMoving()     { return moving; }
	public Location  getLocation()  { return location; }
	public Direction getDirection() { return direction; }
	public Direction getSpiralDir() { return spiralDir; }
	
	public Move move(Direction direction, Direction newSpiral) {		
		this.direction = direction;
		this.moving    = true;
		Move move      = new Move(this.location, this.direction);
		this.location  = map.getLocation(this.location, this.direction);
		this.spiralDir = newSpiral;
		return move;
	}
	
	public Location[] getNearbyLocations() {
		Location[] nearby = new Location[4];
		nearby[0] = map.getLocation(this.location, Direction.NORTH);
		nearby[1] = map.getLocation(this.location, Direction.EAST);
		nearby[2] = map.getLocation(this.location, Direction.SOUTH);
		nearby[3] = map.getLocation(this.location, Direction.WEST);
		return nearby;
	}

}
