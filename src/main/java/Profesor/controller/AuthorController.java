package Profesor.controller;

import Profesor.model.Author;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AuthorController {

  private Connection connection;
  private EntityManagerFactory entityManagerFactory;

  public AuthorController(Connection connection) {
    this.connection = connection;
  }

  public AuthorController(Connection connection, EntityManagerFactory entityManagerFactory) {
    this.connection = connection;
    this.entityManagerFactory = entityManagerFactory;
  }

  public List<Author> readAuthorsFile(String filename) throws IOException {
    int id;
    String name, year, country;
    boolean active;
    List<Author> authorsList = new ArrayList<Author>();

    BufferedReader br = new BufferedReader(new FileReader(filename));
    String linea = "";
    while ((linea = br.readLine()) != null) {
      StringTokenizer str = new StringTokenizer(linea, ",");
      id = Integer.parseInt(str.nextToken());
      name = str.nextToken();
      year = str.nextToken();
      country = str.nextToken();
      active = Boolean.parseBoolean(str.nextToken());
      // System.out.println(id + name + country + year + active);
      authorsList.add(new Author(id, name, country, year, active));

    }
    br.close();

    return authorsList;
  }

  public void printAutors(ArrayList<Author> authorsList) {
    for (int i = 0; i < authorsList.size(); i++) {
      System.out.println(authorsList.get(i).toString());
    }
  }


  /* Method to CREATE an Autor in the database */
  public void addAuthor(Author author) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Author authotExists = (Author) em.find(Author.class, author.getAuthorId());
    if (authotExists == null ){
      System.out.println("insert autor");
      em.persist(author);
    }
    em.getTransaction().commit();
    em.close();
  }


  /* Method to READ all Autors */
  public void listAuthors() {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    List<Author> result = em.createQuery("from Author", Author.class)
        .getResultList();
    for (Author author : result) {
      System.out.println(author.toString());
    }
    em.getTransaction().commit();
    em.close();
  }

  /* Method to UPDATE activity for an author */
  public void updateAutor(Integer authorId, boolean active) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Author author = (Author) em.find(Author.class, authorId);
    author.setActive(active);
    em.merge(author);
    em.getTransaction().commit();
    em.close();
  }

  /* Method to DELETE an Author from the records */
  public void deleteAuthor(Integer authorId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.getTransaction().begin();
    Author author = (Author) em.find(Author.class, authorId);
    em.remove(author);
    em.getTransaction().commit();
    em.close();
  }

}
