package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Class for entity Player
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "players")
public class Player implements Serializable {
    @Id
    @Column(name = "idplayer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int playerId;
    @Column(name = "name", length = 45)
    String name;
    @Column(name = "age")
    int age;
    @Column(name = "position", length = 100)
    String position;
    @Column(name = "college", length = 100)
    String college;
    @Column(name = "draftteam", length = 45)
    String draftTeam;
    @Column(name = "draftposition")
    int draftPos;
    @Column(name = "draftyear")
    int draftYear;
    @Column(name = "born")
    Date born;
    @Column(name = "experience")
    int expCareer;


    /**
     * Constructor vacío.
     */
    public Player() {
    }

    /**
     * Permite definir los atributos de la clase
     *
     * @param name      Nombre
     * @param position  Posicion
     * @param college   Universidad
     * @param draftTeam Equipo del Draft
     * @param draftPos  Posicion del Draft
     * @param born      Fecha de Nacimiento
     * @param age       Edad
     * @param draftYear Año del Draft
     * @param expCareer Carrera
     */
    public Player(String name, String position, String college, String draftTeam, int draftPos, Date born, int age, int draftYear, int expCareer) {
        super();
        this.name = name;
        this.position = position;
        this.college = college;
        this.draftTeam = draftTeam;
        this.draftPos = draftPos;
        this.born = born;
        this.age = age;
        this.draftYear = draftYear;
        this.expCareer = expCareer;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDraftTeam() {
        return draftTeam;
    }

    public void setDraftTeam(String draftTeam) {
        this.draftTeam = draftTeam;
    }

    public int getDraftPos() {
        return draftPos;
    }

    public void setDraftPos(int draftPos) {
        this.draftPos = draftPos;
    }

    public int getDraftYear() {
        return draftYear;
    }

    public void setDraftYear(int draftYear) {
        this.draftYear = draftYear;
    }

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public int getExpCareer() {
        return expCareer;
    }

    public void setExpCareer(int expCareer) {
        this.expCareer = expCareer;
    }

    /**
     * Devuelve como cadena de caracteres los atributos de la clase.
     *
     * @return Cadena con los atributos.
     */
    @Override
    public String toString() {

        return "Player{" +
                "playerId='" + playerId + '\'' + " | " +
                "name='" + name + '\'' + " | " +
                "born='" + born + '\'' + " | " +
                "age=" + age + " | " +
                "college='" + college + '\'' + " | " +
                "position='" + position + '\'' + " | " +
                "draftTeam='" + draftTeam + '\'' + " | " +
                "draftPos='" + draftPos + '\'' + " | " +
                "draftYear=" + draftYear + " | " +
                "expCareer=" + expCareer +
                '}';
    }
}
