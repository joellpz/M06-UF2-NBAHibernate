package Profesor.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Access(AccessType.FIELD)
@Table(name = "articles")
public class Article implements Serializable {
  @Id
  @Column(name = "id_article")
  int articleId;
  @Column(name = "titol", length = 30)
  String title;
  @Column(name = "data_creacio")
  Date publicationDate;
  @Column(name = "publicable")
  boolean publishable;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "id_autor")
  public Author author;

  public Article(int articleId, String title, Date publicationDate,
                 boolean publishable, Author author) {
    super();
    this.articleId = articleId;
    this.title = title;
    this.publicationDate = publicationDate;
    this.publishable = publishable;
    this.author = author;
  }

  public Article() {
    super();

  }

  public int getArticleId() {
    return articleId;
  }

  public void setArticleId(int articleId) {
    this.articleId = articleId;
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

  public boolean isPublishable() {
    return publishable;
  }

  public void setPublishable(boolean publishable) {
    this.publishable = publishable;
  }

  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  @Override
  public String toString() {
    return "Article{" +
        "articleId=" + articleId +
        ", title='" + title + '\'' +
        ", publicationDate=" + publicationDate +
        ", publishable=" + publishable +
        ", author=" + author.toString() +
        '}';
  }
}
