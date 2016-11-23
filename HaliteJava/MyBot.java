import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class MyBot {
	
    public static void main(String[] args) throws java.io.IOException {
        InitPackage iPackage = Networking.getInit();
        int myID = iPackage.myID;
        GameMap map = iPackage.map;
        
        Networking.sendInit("MyJavaBot");
        
        while(true) {
            ArrayList<Move> moves = new ArrayList<Move>();

            map = Networking.getFrame();
            
            // highest production on site not owned by bot
            int highProd = 0;
            
            // all sites owned by this bot
            Map<Location,Site> bot = new HashMap<Location,Site>();
           
            // map the board
            for(int y = 0; y < map.height; y++) {
                for(int x = 0; x < map.width; x++) {
                	Location loc = new Location(x,y);
                    Site site = map.getSite(loc);
                    if(site.owner == myID) {
                    	bot.put(loc,site);
                    } else {
                    	if (site.production > highProd)
                    		highProd = site.production;
                    }
                }
            }
            
            /*
             ***** attack weak nearby enemy
             */
            Iterator<Map.Entry<Location,Site>> iter1 = bot.entrySet().iterator();
        	while (iter1.hasNext()) {
        		Map.Entry<Location,Site> entry = iter1.next();
        		Site site                      = entry.getValue();
        		Location loc                   = entry.getKey(); 
        		Location[] nearby              = new Location[4];
        		nearby[0]                      = map.getLocation(loc, Direction.NORTH);
        		nearby[1]                      = map.getLocation(loc, Direction.EAST);
        		nearby[2]                      = map.getLocation(loc, Direction.SOUTH);
        		nearby[3]                      = map.getLocation(loc, Direction.WEST);
        		Site greaterProdSite           = null;
        		Location greaterProdLoc        = null;
        		Direction moveDir              = Direction.STILL;
        		int offset                     = new Random().nextInt(4);
        		// find site nearby with greatest production that can be beat
        		for (int i=offset; i<4+offset; i++) {
            		int j = i;
            		if (j >= 4) j = j-4;
            		Site neighbour = map.getSite(nearby[j]);            		
            		if ((neighbour.owner != site.owner) &&
            				(site.compareStr(neighbour) > -1)) {
            			if (neighbour.compareProd(greaterProdSite) > 0) {
            				greaterProdSite = neighbour;
            				greaterProdLoc  = nearby[j];
            				moveDir         = calcDirection(j);
            			}
            		} 
            	}
        		// site found on boarder to attack
            	if (greaterProdSite != null) { 
            		// site moving to
            		greaterProdSite.strength = site.strength - greaterProdSite.strength;
            		if (greaterProdSite.strength > 0) {
            			greaterProdSite.owner = myID;
            		}
            		map.setSite(greaterProdLoc, greaterProdSite);
            		
            		// site moving from
            		site.strength = 0;
            		map.setSite(loc, site);
            		
            		moves.add(new Move(loc, moveDir));
            		iter1.remove();
            	}

        	} 
           
            /*
             ***** move strong reinforcements towards
             ***** closest border
             */
            Iterator<Map.Entry<Location,Site>> iter2 = bot.entrySet().iterator();
        	while (iter2.hasNext()) {
        		Map.Entry<Location,Site> entry = iter2.next();
        		Site site                      = entry.getValue();
        		Location loc                   = entry.getKey(); 
        		Direction closest              = directionToClosestBorder(map,myID,loc.x,loc.y);
        		Site neighbour                 = map.getSite(loc,closest);
        		if ((neighbour.owner == myID) &&
        				(site.compareStr(neighbour) > 0)) {
        			moves.add(new Move(loc,closest));
        			
        			// site moving to
        			neighbour.strength += site.strength;
        			if (neighbour.strength > 255) 
        				neighbour.strength = 255;
        			map.setSite(map.getLocation(loc,closest), neighbour);
        			
        			// site moving from
            		site.strength = 0;
            		map.setSite(loc, site);
            		
        			iter2.remove();
        		}
        	}
           
            Networking.sendFrame(moves);
        }
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
