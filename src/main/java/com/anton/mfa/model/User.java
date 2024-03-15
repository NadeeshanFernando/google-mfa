package com.anton.mfa.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author by nadeeshan_fdz
 */
@Setter
@Getter @AllArgsConstructor @NoArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    public long userId;

    @Column(name = "username")
    public String username;

    @Column(name = "password")
    public String password;

    @Column(name = "mfa_enabled")
    public boolean mfaEnabled;

    @Column(name = "secret_Key")
    public String secretKey;
}
