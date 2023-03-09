package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Access(AccessType.FIELD)
@Table(name = "playerseasons")
public class PlayerSeason implements Serializable {

    @EmbeddedId
    PlayerSeasonId playerSeasonId;
    @Column(name = "age")
    int age;
    @Column(name = "league", length = 45)
    String league;
    @Column(name = "position", length = 45)
    String position;
    @Column(name = "games")
    int games;
    @Column(name = "gamesstarter")
    int gamesStarter;
    @Column(name = "minutesplayed")
    int minutesPlayed;
    @Column(name = "fieldgoals")
    int fieldGoals;
    @Column(name = "fgattempts")
    int fgAttempts;
    @Column(name = "fgperc")
    float fgPerc;
    @Column(name = "3PTSFG")
    int fgPts3;
    @Column(name = "3PFGAttempts")
    int pfgAttempts3;
    @Column(name = "3PFGPerc")
    float pfgPerc3;
    @Column(name = "2PtsFG")
    int fgPts2;
    @Column(name = "2PFGAttempts")
    int pfgAttempts2;
    @Column(name = "2PFGPerc")
    float pfgPerc2;
    @Column(name = "effectivegoalperc")
    float effGoalPerc;
    @Column(name = "freethrows")
    int freeThrows;
    @Column(name = "freethrowsattempts")
    int freetAttempts;
    @Column(name = "ftperc")
    float ftPerc;
    @Column(name = "offrbds")
    int offRbds;
    @Column(name = "defrbds")
    int defRbds;
    @Column(name = "totalrbds")
    int totalRbds;
    @Column(name = "assists")
    int assists;
    @Column(name = "steals")
    int steals;
    @Column(name = "blocks")
    int blocks;
    @Column(name = "turnovers")
    int turnovers;
    @Column(name = "fouls")
    int fouls;
    @Column(name = "points")
    int points;
    @Column(name = "tripledouble")
    int tripleDouble;

    /**
     * Constructor for PlayerSeason
     *
     * @param playerSeasonId playerSeasonId
     * @param age           age
     * @param league        league
     * @param position      position
     * @param games         games
     * @param gamesStarter  gamesStarter
     * @param minutesPlayed minutesPlayed
     * @param fieldGoals    fieldGoals
     * @param fgAttempts    fgAttempts
     * @param fgPerc        fgPerc
     * @param fgPts3        fgPts3
     * @param pfgAttempts3  pfgAttempts3
     * @param pfgPerc3      pfgPerc3
     * @param fgPts2        fgPts2
     * @param pfgAttempts2  pfgAttempts2
     * @param pfgPerc2      pfgPerc2
     * @param effGoalPerc   effGoalPerc
     * @param freeThrows    freeThrows
     * @param freetAttempts freetAttempts
     * @param ftPerc        ftPerc
     * @param offRbds       offRbds
     * @param defRbds       defRbds
     * @param totalRbds     totalRbds
     * @param assists       assists
     * @param steals        steals
     * @param blocks        blocks
     * @param turnovers     turnovers
     * @param fouls         fouls
     * @param points        points
     * @param tripleDouble  tripleDouble
     */
    public PlayerSeason(PlayerSeasonId playerSeasonId, int age, String league, String position, int games, int gamesStarter, int minutesPlayed, int fieldGoals, int fgAttempts, float fgPerc, int fgPts3, int pfgAttempts3, float pfgPerc3, int fgPts2, int pfgAttempts2, float pfgPerc2, float effGoalPerc, int freeThrows, int freetAttempts, float ftPerc, int offRbds, int defRbds, int totalRbds, int assists, int steals, int blocks, int turnovers, int fouls, int points, int tripleDouble) {
        this.playerSeasonId = playerSeasonId;
        this.age = age;
        this.league = league;
        this.position = position;
        this.games = games;
        this.gamesStarter = gamesStarter;
        this.minutesPlayed = minutesPlayed;
        this.fieldGoals = fieldGoals;
        this.fgAttempts = fgAttempts;
        this.fgPerc = fgPerc;
        this.fgPts3 = fgPts3;
        this.pfgAttempts3 = pfgAttempts3;
        this.pfgPerc3 = pfgPerc3;
        this.fgPts2 = fgPts2;
        this.pfgAttempts2 = pfgAttempts2;
        this.pfgPerc2 = pfgPerc2;
        this.effGoalPerc = effGoalPerc;
        this.freeThrows = freeThrows;
        this.freetAttempts = freetAttempts;
        this.ftPerc = ftPerc;
        this.offRbds = offRbds;
        this.defRbds = defRbds;
        this.totalRbds = totalRbds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
        this.turnovers = turnovers;
        this.fouls = fouls;
        this.points = points;
        this.tripleDouble = tripleDouble;
    }

    /**
     * Empty Contructor for Player Season
     */
    public PlayerSeason() {
    }

    @Override
    public String toString() {
        return "PlayerSeasons{" +
                "playerId=" + playerSeasonId.playerId +
                ", seasonId=" + playerSeasonId.seasonId +
                ", teamId=" + playerSeasonId.teamId +
                ", age=" + age +
                ", league='" + league + '\'' +
                ", position='" + position + '\'' +
                ", games=" + games +
                ", gamesStarter=" + gamesStarter +
                ", minutesPlayed=" + minutesPlayed +
                ", fieldGoals=" + fieldGoals +
                ", fgAttempts=" + fgAttempts +
                ", fgPerc=" + fgPerc +
                ", fgPts3=" + fgPts3 +
                ", pfgAttempts3=" + pfgAttempts3 +
                ", pfgPerc3=" + pfgPerc3 +
                ", fgPts2=" + fgPts2 +
                ", pfgAttempts2=" + pfgAttempts2 +
                ", pfgPerc2=" + pfgPerc2 +
                ", effGoalPerc=" + effGoalPerc +
                ", freeThrows=" + freeThrows +
                ", freetAttempts=" + freetAttempts +
                ", ftPerc=" + ftPerc +
                ", offRbds=" + offRbds +
                ", defRbds=" + defRbds +
                ", totalRbds=" + totalRbds +
                ", assists=" + assists +
                ", steals=" + steals +
                ", blocks=" + blocks +
                ", turnovers=" + turnovers +
                ", fouls=" + fouls +
                ", points=" + points +
                ", tripleDouble=" + tripleDouble +
                '}';
    }

    public PlayerSeasonId getPlayerSeasonId() {
        return playerSeasonId;
    }

    public void setPlayerSeasonId(PlayerSeasonId playerSeasonId) {
        this.playerSeasonId = playerSeasonId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getGamesStarter() {
        return gamesStarter;
    }

    public void setGamesStarter(int gamesStarter) {
        this.gamesStarter = gamesStarter;
    }

    public int getMinutesPlayed() {
        return minutesPlayed;
    }

    public void setMinutesPlayed(int minutesPlayed) {
        this.minutesPlayed = minutesPlayed;
    }

    public int getFieldGoals() {
        return fieldGoals;
    }

    public void setFieldGoals(int fieldGoals) {
        this.fieldGoals = fieldGoals;
    }

    public int getFgAttempts() {
        return fgAttempts;
    }

    public void setFgAttempts(int fgAttempts) {
        this.fgAttempts = fgAttempts;
    }

    public float getFgPerc() {
        return fgPerc;
    }

    public void setFgPerc(float fgPerc) {
        this.fgPerc = fgPerc;
    }

    public int getFgPts3() {
        return fgPts3;
    }

    public void setFgPts3(int fgPts3) {
        this.fgPts3 = fgPts3;
    }

    public int getPfgAttempts3() {
        return pfgAttempts3;
    }

    public void setPfgAttempts3(int pfgAttempts3) {
        this.pfgAttempts3 = pfgAttempts3;
    }

    public float getPfgPerc3() {
        return pfgPerc3;
    }

    public void setPfgPerc3(float pfgPerc3) {
        this.pfgPerc3 = pfgPerc3;
    }

    public int getFgPts2() {
        return fgPts2;
    }

    public void setFgPts2(int fgPts2) {
        this.fgPts2 = fgPts2;
    }

    public int getPfgAttempts2() {
        return pfgAttempts2;
    }

    public void setPfgAttempts2(int pfgAttempts2) {
        this.pfgAttempts2 = pfgAttempts2;
    }

    public float getPfgPerc2() {
        return pfgPerc2;
    }

    public void setPfgPerc2(float pfgPerc2) {
        this.pfgPerc2 = pfgPerc2;
    }

    public float getEffGoalPerc() {
        return effGoalPerc;
    }

    public void setEffGoalPerc(float effGoalPerc) {
        this.effGoalPerc = effGoalPerc;
    }

    public int getFreeThrows() {
        return freeThrows;
    }

    public void setFreeThrows(int freeThrows) {
        this.freeThrows = freeThrows;
    }

    public int getFreetAttempts() {
        return freetAttempts;
    }

    public void setFreetAttempts(int freetAttempts) {
        this.freetAttempts = freetAttempts;
    }

    public float getFtPerc() {
        return ftPerc;
    }

    public void setFtPerc(float ftPerc) {
        this.ftPerc = ftPerc;
    }

    public int getOffRbds() {
        return offRbds;
    }

    public void setOffRbds(int offRbds) {
        this.offRbds = offRbds;
    }

    public int getDefRbds() {
        return defRbds;
    }

    public void setDefRbds(int defRbds) {
        this.defRbds = defRbds;
    }

    public int getTotalRbds() {
        return totalRbds;
    }

    public void setTotalRbds(int totalRbds) {
        this.totalRbds = totalRbds;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getSteals() {
        return steals;
    }

    public void setSteals(int steals) {
        this.steals = steals;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public int getTurnovers() {
        return turnovers;
    }

    public void setTurnovers(int turnovers) {
        this.turnovers = turnovers;
    }

    public int getFouls() {
        return fouls;
    }

    public void setFouls(int fouls) {
        this.fouls = fouls;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTripleDouble() {
        return tripleDouble;
    }

    public void setTripleDouble(int tripleDouble) {
        this.tripleDouble = tripleDouble;
    }
}

