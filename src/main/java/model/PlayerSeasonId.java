package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlayerSeasonId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idplayer")
    protected Player idPlayer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idseason")
    protected Season seasonId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idteam")
    protected Team teamId;

    public PlayerSeasonId() {
    }

    public PlayerSeasonId(Player idPlayer, Season seasonId, Team teamId) {
        this.idPlayer = idPlayer;
        this.seasonId = seasonId;
        this.teamId = teamId;
    }

    public Player getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Player idPlayer) {
        this.idPlayer = idPlayer;
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
        return getIdPlayer().equals(that.getIdPlayer()) && getSeasonId().equals(that.getSeasonId()) && getTeamId().equals(that.getTeamId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdPlayer(), getSeasonId(), getTeamId());
    }

    @Override
    public String toString() {
        return "playerId=" + idPlayer.getPlayerId() +":"+ idPlayer.getName()+
                ", seasonId=" + seasonId.getSeasonId()+":"+seasonId.getYear() +
                ", teamId=" + teamId.getTeamId()+":"+teamId.getName();
    }
}
