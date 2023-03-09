package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlayerSeasonId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "idplayer")
    protected Player playerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "idseason")
    protected Season seasonId;
    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "idteam")
    protected Team teamId;

    public PlayerSeasonId() {
    }

    public PlayerSeasonId(Player playerId, Season seasonId, Team teamId) {
        this.playerId = playerId;
        this.seasonId = seasonId;
        this.teamId = teamId;
    }

    public Player getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Player playerId) {
        this.playerId = playerId;
    }

    public Season getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Season seasonId) {
        this.seasonId = seasonId;
    }

    public Team getTeamId() {
        return teamId;
    }

    public void setTeamId(Team teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerSeasonId that = (PlayerSeasonId) o;
        return getPlayerId().equals(that.getPlayerId()) && getSeasonId().equals(that.getSeasonId()) && getTeamId().equals(that.getTeamId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayerId(), getSeasonId(), getTeamId());
    }

    @Override
    public String toString() {
        return "playerId=" + playerId.getName() +
                ", seasonId=" + seasonId.getYear() +
                ", teamId=" + teamId.getName();
    }
}
