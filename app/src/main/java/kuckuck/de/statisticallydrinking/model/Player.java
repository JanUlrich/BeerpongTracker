package kuckuck.de.statisticallydrinking.model;

/**
 * This class only contains cosmetic info
 * Like player name, picture and stuff
 * A player name is unique
 */
public class Player {
    private final String name;

    public Player(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        return name.equals(obj);
    }
}
