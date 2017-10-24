import hlt.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyBot {

    public static void main(final String[] args) {
        final Networking networking = new Networking();
        final GameMap gameMap = networking.initialize("Tamagocchi");
        final ArrayList<Move> moveList = new ArrayList<>();

        gameMap.updateMap(Networking.readLineIntoMetadata());
        
        List<Ship> startingShips = new ArrayList<Ship>(gameMap.getMyPlayer().getShips().values());
        int startingShipCount = startingShips.size();

    	List<Planet> firstTurnPlanets = new ArrayList<Planet>();
        for (final Entity entity: gameMap.nearbyEntitiesByDistance(startingShips.get(0)).values()) {
        	if (entity instanceof Planet) {
        		firstTurnPlanets.add((Planet) entity);
        	}
        }
        
        // first turn, go to three closest planets
        int shipCount = 1; // current ship number
        for (final Ship ship : startingShips) {
        	moveList.add(new DockMove(ship, firstTurnPlanets.get(shipCount-1)));
	    	shipCount++;
        }
        
        int turnCount = 1; // turn number     
        
        for (;;) {
        	if (turnCount > 1) {
	            moveList.clear();
	            gameMap.updateMap(Networking.readLineIntoMetadata());
	            
	            shipCount = 1; // current ship number
	
	            for (final Ship ship : gameMap.getMyPlayer().getShips().values()) {
	                if (ship.getDockingStatus() != Ship.DockingStatus.Undocked) {
	                    continue;
	                }
	                
	                // categorize entities, ordered by distance:
	            	List<Planet> unownedPlanets = new ArrayList<Planet>();
	            	List<Ship> enemies 			= new ArrayList<Ship>();
	                for (final Entity entity: gameMap.nearbyEntitiesByDistance(ship).values()) {
	                	if (entity instanceof Planet) {
	                		if (entity.getOwner() != gameMap.getMyPlayerId()) {
	                			unownedPlanets.add((Planet) entity);
	                		}
	                	} else if (entity instanceof Ship) {
	                		if (entity.getOwner() != gameMap.getMyPlayerId()) {
	                			enemies.add((Ship) entity);
	                		}
	                	}
	                }
	                
	                // dock, or move to closest planet that I don't own
	                for (Planet planet : unownedPlanets) {                		
	                	
	                	// dock if able
	                	if (ship.canDock(planet)) {
	                		// dock if planet not owned by enemy
	                		if (planet.getOwner() == -1 || planet.getOwner() == gameMap.getMyPlayerId()) {
	                			moveList.add(new DockMove(ship, planet));
	                			
	                		// fight off nearby enemies
	                		} else {
	                			boolean attacking = false;
	                			for (Ship target : enemies) {
	                				// attack docked enemies first
	                				for (int enemyId : planet.getDockedShips()) {
	                					if (target.getId() == enemyId) {
	                						Ship enemy = target;
	                						// move within range of photon torpedos
	                						if (ship.getDistanceTo(enemy) > 5) {
	                							final ThrustMove interceptShip = new Navigation(ship, enemy).navigateTowards(gameMap, enemy, Constants.MAX_SPEED, true, Constants.MAX_CORRECTIONS, Math.PI/180);
	                							if (interceptShip != null) {
	                								moveList.add(interceptShip);
	                								attacking = true;
	                								break;
	                							}
	                						}
	                					}
	                				}
	                				if (attacking) {
	                					break;
	                				}
	                			}
	                			if (!attacking) {
	                				Ship enemy = enemies.get(0);
	                				// move within range of forward phasers
	        						if (ship.getDistanceTo(enemy) > 5) {
	        							final ThrustMove interceptShip = new Navigation(ship, enemy).navigateTowards(gameMap, enemy, Constants.MAX_SPEED, true, Constants.MAX_CORRECTIONS, Math.PI/180);
	        							if (interceptShip != null) {
	        								moveList.add(interceptShip);
	        								attacking = true;
	        								break;
	        							}
	        						}
	                			}
	                		}
	                		break;
	                	} else { // can't dock
		                	// move to closest planet
		                	final ThrustMove newThrustMove = new Navigation(ship, planet).navigateToDock(gameMap, Constants.MAX_SPEED);
		                	if (newThrustMove != null) {
		                		moveList.add(newThrustMove);
		                		break;
		                	}
	                	}
	                }
	                shipCount++;
	            } // end ship loop
        	} // end turnCount != 1
            turnCount++;
            Networking.sendMoves(moveList);
        } // main game loop
    }
    
    // TODO: 
    private Map<Planet, List<Planet>> initializeConnections(GameMap gameMap) {
    	List<Planet> allPlanets = new ArrayList<Planet>(gameMap.getAllPlanets().values());
    	return new HashMap<Planet, List<Planet>>();
    }
    
    /**
     * Remove a planet from a map of planets.
     * @param map		the map to remove from
     * @param planet	planet to remove
     * @return			the map with the planet removed
     */
    private Map<Planet, List<Planet>> removePlanet(Map<Planet, List<Planet>> map, Planet planet) {
    	if (map.containsKey(planet)) {
    		map.remove(planet);
    		for (List<Planet> connections : map.values()) {
    			for (Iterator<Planet> iter = connections.iterator(); iter.hasNext();) {
    				Planet p = (Planet) iter.next();
    				if (p.equals(planet)) {
    					iter.remove();
    					break;
    				}
    			}
    		}
    	}
    	return map;
    }
}
