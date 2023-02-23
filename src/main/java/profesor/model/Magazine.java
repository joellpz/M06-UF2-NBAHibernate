package profesor.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Access(AccessType.FIELD)
@Table(name = "revistes")
public class Magazine implements Serializable {
  @Id
  @Column(name = "id_revista")
  private int magazineId;
  @Column(name = "titol", length = 30)
  private String title;
  @Column(name = "data_publicacio")
  private Date publicationDate;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_revista", referencedColumnName = "id_revista")
  private List<Article> articles = new ArrayList<Article>();

  public Magazine(int magazineId, String title, Date publicationDate) {
    super();
    this.title = title;
    this.publicationDate = publicationDate;
    this.magazineId = magazineId;
  }

  public Magazine() {
    super();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getPublicationDate() {
    return publicationDate;
  }

  public void setPublicationDate(Date publicationDate) {
    this.publicationDate = publicationDate;
  }

  public int getMagazineId() {
    return magazineId;
  }

  public void setMagazineId(int magazineId) {
    this.magazineId = magazineId;
  }

  public void addArticle(Article art) {
    articles.add(art);
  }

  public Article getArticle(int i) {
    return articles.get(i);
  }

  public List<Article> getArticles() {
    return articles;
  }

  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }


  @Override
  public String toString() {
    String result = "Revista [id_revista=" + magazineId + ",titol=" + title + ", data_publicacio="
        + publicationDate.toString() + "]";

    result += "\n Llista d'articles: [ \n";

    for (Article a : articles) {
      result += "\t";
      result += a.toString();
      result += "\n";
    }

    result += "] \n";

    return result;
  }

}
