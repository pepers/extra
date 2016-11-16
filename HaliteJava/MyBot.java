import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
            
            // all pieces owned by bot
            Map<Piece,Site> pieces = new HashMap<Piece,Site>();

            // get all bot's pieces
            for(int y = 0; y < gameMap.height; y++) {
                for(int x = 0; x < gameMap.width; x++) {
                	Location loc = new Location(x,y);
                    Site site = gameMap.getSite(loc);
                    if(site.owner == myID) {
                    	pieces.put(new Piece(loc,gameMap),site);
                    }
                }
            }
            
            // expand into new territory (enemy, or not owned)
            moves.addAll(expandTerritory(pieces,gameMap));
           
            // move strong reinforcements towards closest border
            moves.addAll(reinforcments(pieces,gameMap));
           
            Networking.sendFrame(moves);
        }
    }
    
    /**
     * attempt to expand territory into surrounding unowned spaces
     * @param pieces :all currently owned pieces
     * @param map    :the game map
     * @return       :a list of any territory expanding moves
     */
    private static ArrayList<Move> expandTerritory(Map<Piece,Site> pieces, GameMap map) {
    	ArrayList<Move> frontline = new ArrayList<Move>();
    	Iterator<Map.Entry<Piece,Site>> iter = pieces.entrySet().iterator();
    	while (iter.hasNext()) {
    		Map.Entry<Piece,Site> entry = iter.next();
    		Site site                   = entry.getValue();
    		Piece piece                 = entry.getKey();  
    		Location[] nearby           = (piece).getNearbyLocations();    		
    		int offset                  = new Random().nextInt(4);
        	for (int i=offset; i<4+offset; i++) {
        		int j = i;
        		if (j >= 4) j = j-4;
        		Site neighbour = map.getSite(nearby[j]);
        		if ((neighbour.owner != site.owner) &&
        				(neighbour.strength < site.strength)) {
        			frontline.add(piece.move(calcDirection(j)));
        			iter.remove();
        			break;
        		} 
        	}
    	}
    	return frontline;
    }
    
    /**
     * send strong reinforcements towards closest border
     * @param pieces :all currently owned pieces
     * @param map    :the game map
     * @return       :a list of reinforcement deploying moves
     */
    private static ArrayList<Move> reinforcments(Map<Piece,Site> pieces, GameMap map) {
    	ArrayList<Move> reinforcements = new ArrayList<Move>();
    	Iterator<Map.Entry<Piece,Site>> iter = pieces.entrySet().iterator();
    	while (iter.hasNext()) {
    		Map.Entry<Piece,Site> entry = iter.next();
    		Site site                   = entry.getValue();
    		Piece piece                 = entry.getKey();
    		if (site.strength >= 230) {
	    		Location[] nearby = (piece).getNearbyLocations();
	    		int weakpoint     = -1;
	    		int offset        = new Random().nextInt(4);
	        	for (int i=offset; i<4+offset; i++) {
	        		int j = i;
	        		if (j >= 4) j = j-4;
	        		Site neighbour = map.getSite(nearby[j]);
	        		if (neighbour.owner == site.owner) {
	        			if (weakpoint >= 0) {
	        				if (map.getSite(nearby[weakpoint]).production > neighbour.production) {
	        					weakpoint = j;
	        				}
	        			} 
	        		} 
	        	}
	        	// follow path of least destruction
	        	if (weakpoint >= 0) {
	        		reinforcements.add(piece.move(calcDirection(weakpoint)));
	        	// go towards closest border
	        	} else {
	        		Location loc = piece.getLocation();
	        		reinforcements.add(piece.move(directionToClosestBorder(map, site.owner, loc.x, loc.y)));
	        	}
	        	iter.remove();
    		}
    	}
    	return reinforcements;
    }
    
    /**		
     * find the direction of the closest border to my territory		
     * @param map   :the game map		
     * @param owner :the owner of the piece		
     * @param x     :x-coordinate of piece		
     * @param y     :y-coordinate of piece		
     * @return		
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
}
