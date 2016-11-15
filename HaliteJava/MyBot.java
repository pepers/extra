import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
                    Site site = gameMap.getSite(new Location(x, y));
                    if(site.owner == myID) {
                    	pieces.put(new Piece(new Location(x,y),gameMap),site);
                    }
                }
            }
            
            // expand into new territory (enemy, or not owned)
            moves.addAll(expandTerritory(pieces,gameMap));
            
            // help out friendly neighbour pieces
            moves.addAll(helpNeighbours(pieces,gameMap));
            
            // max strength, charge!
            moves.addAll(maxStrengthCharge(pieces,gameMap));
           
            Networking.sendFrame(moves);
        }
    }
    
    /**
     * updates a site on the game map
     * @param map :the game map
     * @param l   :location of site to replace
     * @param s   :site to replace with
     */
    private static void update(GameMap map, Location l, Site s) {
    	(map.contents).get(l.y).set(l.x, s);
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
        			// update site moving away from
        			Site oldSite = site;
        			oldSite.strength = 0;
        			update(map,piece.getLocation(),oldSite);
        			
        			// move to new site
        			frontline.add(piece.move(calcDirection(j)));
        			
        			// update new site
        			site.strength -= neighbour.strength;
        			update(map,piece.getLocation(),site);
        			
        			// remove piece from pieces that need to still move
        			iter.remove();
        			break;
        		} 
        	}
    	}
    	return frontline;
    }
    
    /**
     * Try to help friendly neighbour pieces get max strength
     * @param pieces :all currently owned pieces
     * @param map    :the game map
     * @return       :list of moves for helping pieces
     */
    private static ArrayList<Move> helpNeighbours(Map<Piece,Site> pieces, GameMap map) {
    	ArrayList<Move> helpers = new ArrayList<Move>();
    	List<Site> alreadyHelped = new LinkedList<Site>();
    	Iterator<Map.Entry<Piece,Site>> iter = pieces.entrySet().iterator();
    	while (iter.hasNext()) {
    		Map.Entry<Piece,Site> entry = iter.next();
    		Site site                   = entry.getValue();
    		if (alreadyHelped.contains(site)) continue;
    		Piece piece                 = entry.getKey();  
    		Location[] nearby           = (piece).getNearbyLocations();    		
    		int offset                  = new Random().nextInt(4);
        	for (int i=offset; i<4+offset; i++) {
        		int j = i;
        		if (j >= 4) j = j-4;
        		Site neighbour = map.getSite(nearby[j]);
        		int combinedStrength = neighbour.strength + site.strength;
        		
        		// if can join with friendly neighbour to make max strength
        		if ((neighbour.owner == site.owner) && 
        				(combinedStrength <= 255) &&
        				(combinedStrength >= 240)) {
        			alreadyHelped.add(neighbour);
        			
        			// update site moving away from
        			Site oldSite = site;
        			oldSite.strength = 0;
        			update(map,piece.getLocation(),oldSite);
        			
        			// move to new site
        			helpers.add(piece.move(calcDirection(j)));
        			
        			// update new site
        			site.strength += neighbour.strength;
        			update(map,piece.getLocation(),site);
        			
        			// remove piece from pieces that need to still move
        			iter.remove();
        			break;
        		}
        	}    		
    	}
    	return helpers;
    }
    
    /**
     * Try to make it to nearest border if already max strength
     * @param pieces :all currently owned pieces
     * @param map    :the game map
     * @return       :list of moves for charging pieces
     */
    private static ArrayList<Move> maxStrengthCharge(Map<Piece,Site> pieces, GameMap map) {
    	ArrayList<Move> charging = new ArrayList<Move>();
    	Iterator<Map.Entry<Piece,Site>> iter = pieces.entrySet().iterator();
    	while (iter.hasNext()) {
    		Map.Entry<Piece,Site> entry = iter.next();
    		Site site                   = entry.getValue();
    		Piece piece                 = entry.getKey();  
    		if (site.strength >= 255) {
    			// direction to closest border (TODO: take lowest strength path)
    			Direction dir = directionToClosestBorder(map, site.owner, piece.getLocation().x, piece.getLocation().y);
    			
    			// don't run over other high strength pieces
    			if (map.getSite(piece.getLocation(), dir).strength >= 240) {
    				charging.add(piece.move(Direction.STILL));
    				iter.remove();
    				continue;
    			}
    			
    			// update site moving away from
    			Site oldSite = site;
    			oldSite.strength = 0;
    			update(map,piece.getLocation(),oldSite);
    			
    			// move to new site
    			charging.add(piece.move(dir));
    			
    			// update new site
    			site.strength = 255;
    			update(map,piece.getLocation(),site);
    			
    			// remove piece from pieces that need to still move
    			iter.remove();
    		}
    	}
    	return charging;
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
