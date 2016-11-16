import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class MyBot {
	
	public static Direction[][] spiral;
	
    public static void main(String[] args) throws java.io.IOException {
        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;
        GameMap gameMap = iPackage.map;
        
        // find the where the bot starts (center of spiral)
        Location center = new Location(0,0);
        boolean centerFound = false;
        for(int y = 0; y<gameMap.height; y++) {
            for(int x = 0; x<gameMap.width; x++) {
            	Location loc = new Location(x,y);
                Site site = gameMap.getSite(loc);
                if(site.owner == myID) {  
                	center = loc;
                	centerFound = true;
                	break;
                }
            }
            if (centerFound) break;
        }
        
        // create spiral with directions
//        spiral = new Direction[gameMap.height][gameMap.width];
//        for (int i=0; i<gameMap.height; i++) {
//        	for (int j=0; j<gameMap.width; j++) {
//        		spiral[i][j] = calculateSpiralDirection(center, new Location(i,j), gameMap);
//        	}
//        }
        spiral = calculateCenterSpiral(new Location(gameMap.height/2,gameMap.width/2),gameMap);
        
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
                    	pieces.put(new Piece(loc,gameMap,spiral[y][x]),site);
                    }
                }
            }
            
            // expand into new territory (enemy, or not owned)
            moves.addAll(expandTerritory(pieces,gameMap));
            
            // spiral time!
            moves.addAll(spiral(pieces,gameMap));
           
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
        			Direction newDirection = calcDirection(j);
        			Location newLoc = map.getLocation(piece.getLocation(), newDirection);
        			frontline.add(piece.move(newDirection,spiral[newLoc.y][newLoc.x]));
        			iter.remove();
        			break;
        		} 
        	}
    	}
    	return frontline;
    }
    
    /**
     * attempt to spiral outwards, bringing strength to the front lines
     * @param pieces :all currently owned pieces
     * @param map    :the game map
     * @return       :a list of spiraling moves
     */
    private static ArrayList<Move> spiral(Map<Piece,Site> pieces, GameMap map) {
    	ArrayList<Move> spiralers = new ArrayList<Move>();
    	Iterator<Map.Entry<Piece,Site>> iter = pieces.entrySet().iterator();
    	while (iter.hasNext()) {
    		Map.Entry<Piece,Site> entry = iter.next();
    		Site site                   = entry.getValue();
    		Piece piece                 = entry.getKey();  
    		Direction spiralDir         = piece.getSpiralDir();
    		Site destination            = map.getSite(piece.getLocation(), spiralDir);
    		if ((site.owner == destination.owner) &&
    				(site.strength + destination.strength <= 270 ) &&
    				(site.strength + destination.strength >= 150 )) {
    			Location newLoc = map.getLocation(piece.getLocation(), spiralDir);
    			spiralers.add(piece.move(spiralDir,spiral[newLoc.y][newLoc.x]));
    			iter.remove();
    		}
    	}
    	return spiralers;
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
    
	private static Direction calculateSpiralDirection(Location center, Location location, GameMap map) {
		Direction dir = Direction.SOUTH;
		Location currLoc = center;
		int n = 0;
		while(true) {
			n++;
			for (int i=0; i<2; i++) {
				for (int j=0; j<n; j++){
					if (location == currLoc) {
						return dir;
					}
					currLoc = map.getLocation(currLoc, dir);
				}
				dir = turn(dir);
			}
		}
	}
	
	private static Direction[][] calculateCenterSpiral(Location center, GameMap map) {
		Direction[][] spiral = new Direction[map.height][map.width];
		Location currLoc = center;
		Direction dir = Direction.SOUTH;
		boolean done = false;
		int n = 0;
		while (true) {
			n++;
			for (int i=0; i<2; i++) {
				for (int j=0; j<n; j++){
					if (spiral[currLoc.y][currLoc.x] != null) {
						done = true;
						break;
					}
					if (done) break;
					spiral[currLoc.y][currLoc.x] = dir;
				}
				if (done) break;
				currLoc = map.getLocation(currLoc, dir);
			}
			if (done) break;
			dir = turn(dir);
		}
		for (int y=0; y<map.height; y++) {
			for (int x=0; x<map.width; x++) {
				if (spiral[y][x] == null) 
					spiral[y][x] = Direction.SOUTH;
			}
		}
		return spiral;
	}
	
	private static Direction turn(Direction prev) {
		if (prev == Direction.SOUTH) {
			return Direction.EAST;
		} else if (prev == Direction.EAST) {
			return Direction.NORTH;
		} else if (prev == Direction.NORTH) {
			return Direction.WEST;
		} else if (prev == Direction.WEST) {
			return Direction.SOUTH;
		} else {
			return Direction.STILL;
		}
	}
}
