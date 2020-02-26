package com.javachen.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class User implements Serializable {
    @Id
    private Long id;
    private String username;
    private String password;
    private Date createdAt;
    private Date updatedAt;
}
