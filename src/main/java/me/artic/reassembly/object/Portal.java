package me.artic.reassembly.object;

import lombok.Getter;
import me.artic.reassembly.Reassembly;
import org.bukkit.Location;

@Getter
public class Portal {
    private String playerId;
    private Location loc1;
    private Location loc2;
    private int id;

    public Portal(String playerId, Location loc1, Location loc2) {
        this.playerId = playerId;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.id = Reassembly.getNextId();
    }

    /*  @Getter:
    public String getPlayerId() {
        return playerId;
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
      }

    @AllArgsConstructor:
    public Portal(String playerId, Location loc1, Location loc2) {
        this.playerId = playerId;
        this.loc1 = loc1;
        this.loc2 = loc2;
    }
 */

}


