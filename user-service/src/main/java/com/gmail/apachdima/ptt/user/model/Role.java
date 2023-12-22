package com.gmail.apachdima.ptt.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Role {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "role_", columnDefinition = "bpchar", unique = true, nullable = false)
    private String roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
}
