package model;

import javax.persistence.*;


@Entity
@Access(AccessType.FIELD)
@Table(name = "teams")
public class Team {
    @Id
    @Column(name = "idteam")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int teamId;
    @Column(name = "name", length = 45)
    String name;
    @Column(name = "location", length = 45)
    String location;
    @Column(name = "games")
    int totalGames;
    @Column(name = "wins")
    int wins;
    @Column(name = "loses")
    int loses;
    @Column(name = "playoff")
    int playoffAppearances;
    @Column(name = "confchampions")
    int conferenceChampions;
    @Column(name = "nbachampions")
    int championships;
    @Column(name = "conference", length = 45)
    String conference;

    /**
     * Define atributos clase Team
     *
     * @param name                Nombre
     * @param location            Ubicación
     * @param totalGames               Partidos
     * @param wins                Ganados
     * @param loses               Perdidos
     * @param playoffAppearances  Apariciones PlayOff
     * @param conferenceChampions Campeón de Conferencia
     * @param championships       Campeón NBA
     * @param conference          Conferencia
     */
    public Team(String name, String location, int totalGames, int wins, int loses,
                int playoffAppearances, int conferenceChampions, int championships, String conference) {
        this.name = name;
        this.location = location;
        this.totalGames = totalGames;
        this.wins = wins;
        this.loses = loses;
        this.playoffAppearances = playoffAppearances;
        this.conferenceChampions = conferenceChampions;
        this.championships = championships;
        this.conference = conference;
    }

    /**
     * Constructor vacío.
     */
    public Team() {
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getPlayoffAppearances() {
        return playoffAppearances;
    }

    public void setPlayoffAppearances(int playoffAppearances) {
        this.playoffAppearances = playoffAppearances;
    }

    public int getConferenceChampions() {
        return conferenceChampions;
    }

    public void setConferenceChampions(int conferenceChampions) {
        this.conferenceChampions = conferenceChampions;
    }

    public int getChampionships() {
        return championships;
    }

    public void setChampionships(int championships) {
        this.championships = championships;
    }

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    /**
     * Devuelve como cadena de caracteres los atributos de la clase.
     *
     * @return Cadena con los atributos.
     */
    @Override
    public String toString() {
        return "Team{" +
                "teamId='" + teamId + '\'' +" | " +
                "name='" + name + '\'' +" | " +
                "location='" + location + '\'' +" | " +
                "games=" + totalGames +" | " +
                "wins=" + wins +" | " +
                "loses=" + loses +" | " +
                "playoffAppearances=" + playoffAppearances +" | " +
                "conferenceChampions=" + conferenceChampions +" | " +
                "championships=" + championships +" | " +
                "conference=" + conference +
                '}';
    }
}
