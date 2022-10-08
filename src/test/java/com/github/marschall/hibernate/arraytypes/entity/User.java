package com.github.marschall.hibernate.arraytypes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "user_table")
public class User {

  @Id
  private Integer id;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

}
