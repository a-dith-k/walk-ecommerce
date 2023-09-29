package com.adith.walk.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Notification {

    @Id
    long id;
    String title;
    String message;
    String link;
}
