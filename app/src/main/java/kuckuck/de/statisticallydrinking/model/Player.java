package kuckuck.de.statisticallydrinking.model;

/**
 * This class only contains cosmetic info
 * Like player name, picture and stuff
 * A player name is unique
 */
public class Player implements Identifiable{
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
        if(obj instanceof Player)
            return name.equals(((Player) obj).name);
        return false;
    }

    @Override
    public String getId() {
        return getName();
    }
}
