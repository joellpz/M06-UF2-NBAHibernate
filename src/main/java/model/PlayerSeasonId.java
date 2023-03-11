package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite Primary Keys for PlayerSeason
 */
@Embeddable
public class PlayerSeasonId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idplayer")
    protected Player idPlayer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idseason")
    protected Season idSeason;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idteam")
    protected Team idTeam;

    public PlayerSeasonId() {
    }

    public PlayerSeasonId(Player idPlayer, Season idSeason, Team idTeam) {
        this.idPlayer = idPlayer;
        this.idSeason = idSeason;
        this.idTeam = idTeam;
    }

    public Player getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Player idPlayer) {
        this.idPlayer = idPlayer;
    }

    public Season getIdSeason() {
        return idSeason;
    }

    public void setIdSeason(Season idSeason) {
        this.idSeason = idSeason;
    }

    public Team getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Team idTeam) {
        this.idTeam = idTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerSeasonId that = (PlayerSeasonId) o;
        return getIdPlayer().equals(that.getIdPlayer()) && getIdSeason().equals(that.getIdSeason()) && getIdTeam().equals(that.getIdTeam());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdPlayer(), getIdSeason(), getIdTeam());
    }

    @Override
    public String toString() {
        return "playerId=" + idPlayer.getPlayerId() + ":" + idPlayer.getName() + "|" +
                "seasonId=" + idSeason.getSeasonId() + ":" + idSeason.getYear() + "|" +
                "teamId=" + idTeam.getTeamId() + ":" + idTeam.getName() + "|";
    }
}
