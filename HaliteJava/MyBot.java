import java.util.ArrayList;
import java.util.Random;

public class MyBot {
    public static void main(String[] args) throws java.io.IOException {
        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;
        GameMap gameMap = iPackage.map;

        Networking.sendInit("MyJavaBot");

        while(true) {
            ArrayList<Move> moves = new ArrayList<Move>();

            gameMap = Networking.getFrame();

            for(int y = 0; y < gameMap.height; y++) {
                for(int x = 0; x < gameMap.width; x++) {
                    Site site = gameMap.getSite(new Location(x, y));
                    if(site.owner == myID) {
                        Direction dir = getDirection(gameMap, site, x, y);
                        moves.add(new Move(new Location(x, y), dir));
                    }
                }
            }
            Networking.sendFrame(moves);
        }
    }
    
    private static Direction getDirection(GameMap map, Site site, int x, int y) {
    	Site[] neighbours = getNeighbours(map, x, y);
    	
    	// check all four surrounding neighbours in random order
    	int offset = new Random().nextInt(4);
    	for (int i=offset; i<4+offset; i++) {
    		int j = i;
    		if (j >= 4) j = j-4;
    		if (canWinAttack(site, neighbours[j])) {
    			return calcDirection(j);
    		} 
    	}
    	
    	// if max strength, charge to nearest border
    	if (site.strength >= 255) return directionToClosestBorder(map, site.owner, x, y);
    	
    	// wait if I don't own all neighbouring sites
    	for (int i=0; i<4; i++) {
    		if (neighbours[i].owner != site.owner) {
    			return Direction.STILL;
    		}
    	}
    	
    	// try to help myself grow a site to max strength
    	for (int i=offset; i<4+offset; i++) {
    		int j = i;
    		if (j >= 4) j = j-4;
    		if (canHelpNeighbour(site, neighbours[j])) {
    			return calcDirection(j);
    		}
    	}    	
    	return Direction.randomDirection();
    }
    
    /**
     * find the direction of the closest border to my territory
     */
    private static Direction directionToClosestBorder(GameMap map, int owner, int x, int y) {
    	Direction dir = Direction.randomDirection();
    	Direction dirX = Direction.EAST;
    	Direction dirY = Direction.NORTH;
    	int height = map.height-1;
    	int width  = map.width-1;
    	int minY = height;
    	int minX = width;
    	for (int h = 0; h<width; h++) {
    		int east = offset(x+h, width);
    		int destOwner = map.getSite(new Location(east, y)).owner;
    		if (destOwner != owner) {
    			if (destOwner != 0) { // enemy on border
    				if (h < minX+4) minX = h;
    			} else {              // unclaimed territory
    				if (h < minX) minX = h;
    			}
    			dirX = Direction.EAST;
    			break;
    		}
    		int west = offset(x-h, width);
    		destOwner = map.getSite(new Location(west, y)).owner;
    		if (destOwner != owner) {
    			if (destOwner != 0) { // enemy on border
    				if (h < minX+4) minX = h;
    			} else {              // unclaimed territory
    				if (h < minX) minX = h;
    			}
    			dirX = Direction.WEST;
    			break;
    		}
    	}
    	for (int v = 0; v<height; v++) {
    		int north = offset(y-v, height);
    		int destOwner = map.getSite(new Location(x, north)).owner;
    		if (destOwner != owner) {
    			if (destOwner != 0) { // enemy on border
    				if (v < minY+4) minY = v;
    			} else {              // unclaimed territory
    				if (v < minY) minY = v;
    			}
    			dirY = Direction.NORTH;
    			break;
    		}
    		int south = offset(y+v, height);
    		destOwner = map.getSite(new Location(x, south)).owner;
    		if (destOwner != owner) {
      			if (destOwner != 0) { // enemy on border
    				if (v < minY+4) minY = v;
    			} else {              // unclaimed territory
    				if (v < minY) minY = v;
    			}
    			dirY = Direction.SOUTH;
    			break;
    		}
    	}
    	if (minX<minY) {
    		dir = dirX;
    	} else if (minY<minX) {
    		dir = dirY;
    	} 
    	return dir;
    }
    
    /**
     * return the sites of the surrounding four spaces
     */
    private static Site[] getNeighbours(GameMap map, int x, int y) {
    	Site[] neighbours = new Site[4];
    	int height = map.height-1;
    	int width  = map.width-1;
    	Location[] locs = new Location[4];
    	// north, east, south, west
    	locs[0] = (y-1<0)      ? new Location(x, height) : new Location(x, y-1); 
    	locs[1] = (x+1>width)  ? new Location(0, y)      : new Location(x+1, y); 
    	locs[2] = (y+1>height) ? new Location(x, 0)      : new Location(x, y+1); 
    	locs[3] = (x-1<0)      ? new Location(width, 0)  : new Location(x-1, y);
    	for (int i=0; i<4; i++) {
    		neighbours[i] = map.getSite(locs[i]);
    	}
    	return neighbours;
    }
    
    /**
     * checks to see if you can win against enemy, 
     * and take territory
     */
    private static boolean canWinAttack(Site me, Site enemy) {
    	if (enemy.owner == me.owner) return false; // not enemy
    	return (me.strength > enemy.strength) ? true : false; 
    }
    
    /**
     * determines if I can add to the strength of my neighbour
     */
    private static boolean canHelpNeighbour(Site me, Site neighbour) {
    	if (neighbour.owner != me.owner) return false; // neighbour isn't me
    	if (me.strength < 1) return false; // can't help with no strength
    	return (me.strength + neighbour.strength <= 255) ? true : false; 
    }
    
    /**
     * helper method to get direction
     * @param i = 0,1,2,or 3 
     * @return corresponding direction
     */
    private static Direction calcDirection(int i) {
    	switch(i) {
			case 0  : return Direction.NORTH;
			case 1  : return Direction.EAST;
			case 2  : return Direction.SOUTH;
			case 3  : return Direction.WEST;
			default : return Direction.STILL;
    	}
    }
    
    /**
     * loop from 0 to max
     */
    private static int offset(int pos, int max) {
    	if (pos>max) {
    		return offset(pos-max-1, max);
    	} else if (pos<0) {
    		return offset(pos+max-1, max);
    	} else {
    		return pos;
    	}
    }
}
