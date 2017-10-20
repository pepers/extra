public class Site {
    public int owner, strength, production;
    
    /**
     * compare this site's production to another site's production
     * @param site : site (can be null) to compare to this site
     * @return     : 1 if this.production is greater
     *             : 0 if productions are equal
     *             : -1 if this.production is less
     */
    public int compareProd(Site site) {
    	if (site == null) return 1;
    	if (this.production == site.production) {
    		return 0;
    	} else if (this.production > site.production) {
    		return 1;
    	} else {
    		return -1;
    	}
    }
    
    /**
     * compare this site's strength to another site's strength
     * @param site : site (can be null) to compare to this site
     * @return     : 1 if this.strength is greater
     *             : 0 if strengths are equal
     *             : -1 if this.strength is less
     */
    public int compareStr(Site site) {
    	if (site == null) return 1;
    	if (this.strength == site.strength) {
    		return 0;
    	} else if (this.strength > site.strength) {
    		return 1;
    	} else {
    		return -1;
    	}
    }
    
    /**
     * Get the strength of this site after staying still for a 
     * number of turns.
     * @param turns : the number of turns to stay still for (>0)
     * @return      : the strength after a number of turns
     */
    public int futureStrength(int turns) {
    	if (turns<=0) return this.strength;
    	int futureStr = this.strength;
    	for (int i=0; i<turns; i++) {
    		futureStr += this.production;
    		if (futureStr>=255) return 255;
    	}
    	return futureStr; 
    }
    
    /**
     * Get the number of turns, staying still, that is needed to
     * accrue a strength of 255;
     * @return : the number of turns needed to get max strength
     */
    public int turnsToMax() {
    	int turns = 0;
    	for (int i=this.strength; i<255; i+=this.production) {
    		turns++;
    	}
    	return turns;
    }
}
