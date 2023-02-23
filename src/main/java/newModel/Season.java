package newModel;

import javax.persistence.*;

public class Season {

    @Id
    @Column(name = "idseason")
    int seasonId;
    @Column(name = "years",length = 45)
    String year;

    @Column(name = "league",length = 45)
    String league;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idteam")
    Team champion;

    @Column(name = "mvp",length = 45)
    String MVP;

    @Column(name = "roty",length = 45)
    String ROTY;
    @Column(name = "pointsleader",length = 45)
    String PPG_Leader;

    @Column(name = "pointsleader",length = 45)
    String RGP_Leader;

    @Column(name = "reboundsleader",length = 45)
    String APG_Leader;

    @Column(name = "winssharesleader",length = 45)
    String WS_Leader;


    /**
     * Permite definir las caracteristicas de la temporada en su objeto
     *
     * @param year       Año
     * @param league     Liga
     * @param champion   Campeón
     * @param MVP        Mejor Jugador
     * @param ROTY       Mejor Rookie
     * @param PPG_Leader Líder Puntos
     * @param RGP_Leader Líder Rebotes
     * @param APG_Leader Líder Asistenciass
     * @param WS_Leader  Líder Victorias
     */
    public Season(String year, String league, Team champion, String MVP, String ROTY, String PPG_Leader, String RGP_Leader, String APG_Leader, String WS_Leader) {
        this.year = year;
        this.league = league;
        this.champion = champion;
        this.MVP = MVP;
        this.ROTY = ROTY;
        this.PPG_Leader = PPG_Leader;
        this.RGP_Leader = RGP_Leader;
        this.APG_Leader = APG_Leader;
        this.WS_Leader = WS_Leader;
    }

    /**
     * Constructor para JAXB
     */
    public Season() {
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public Team getChampion() {
        return champion;
    }

    public void setChampion(Team champion) {
        this.champion = champion;
    }

    public String getMVP() {
        return MVP;
    }

    public void setMVP(String MVP) {
        this.MVP = MVP;
    }

    public String getROTY() {
        return ROTY;
    }

    public void setROTY(String ROTY) {
        this.ROTY = ROTY;
    }

    public String getPPG_Leader() {
        return PPG_Leader;
    }

    public void setPPG_Leader(String PPG_Leader) {
        this.PPG_Leader = PPG_Leader;
    }

    public String getRGP_Leader() {
        return RGP_Leader;
    }

    public void setRGP_Leader(String RGP_Leader) {
        this.RGP_Leader = RGP_Leader;
    }

    public String getAPG_Leader() {
        return APG_Leader;
    }

    public void setAPG_Leader(String APG_Leader) {
        this.APG_Leader = APG_Leader;
    }

    public String getWS_Leader() {
        return WS_Leader;
    }

    public void setWS_Leader(String WS_Leader) {
        this.WS_Leader = WS_Leader;
    }

    /**
     * Devuelve como cadena de caracteres los atributos de la clase.
     *
     * @return Cadena con los atributos.
     */
    @Override
    public String toString() {
        return "Season{" +
                "year='" + year + '\'' +
                ", league='" + league + '\'' +
                ", champion='" + champion + '\'' +
                ", MVP='" + MVP + '\'' +
                ", ROTY='" + ROTY + '\'' +
                ", PPG_Leader='" + PPG_Leader + '\'' +
                ", RGP_Leader='" + RGP_Leader + '\'' +
                ", APG_Leader='" + APG_Leader + '\'' +
                ", WS_Leader='" + WS_Leader + '\'' +
                '}';
    }

}
