package newModel;

import javax.persistence.Column;
import javax.persistence.Id;

public class Season {

    @Id
    @Column(name = "idseason")
    int seasonId;
    @Column(name = "years",length = 45)
    String year;

    @Column(name = "league",length = 45)
    String league;


    String champion;

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
     * @param APG_Leader Líder Asistencias
     * @param WS_Leader  Líder Victorias
     */
    public Season(String year, String league, String champion, String MVP, String ROTY, String PPG_Leader, String RGP_Leader, String APG_Leader, String WS_Leader) {
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
