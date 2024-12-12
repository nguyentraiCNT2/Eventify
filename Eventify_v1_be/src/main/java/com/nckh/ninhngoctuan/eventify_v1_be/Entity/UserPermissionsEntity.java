package com.nckh.ninhngoctuan.eventify_v1_be.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_permissions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @ManyToOne
    @JoinColumn(name = "assigned_by", referencedColumnName = "id", nullable = false)
    private UserEntity assignedBy;

    @Column(name = "assigned_at", nullable = false)
    private Timestamp assignedAt;

    @PrePersist
    protected void onCreate() {
        this.assignedAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserEntity getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(UserEntity assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Timestamp getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Timestamp assignedAt) {
        this.assignedAt = assignedAt;
    }
}
