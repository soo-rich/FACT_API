package com.soosmart.facts.repository;

import com.soosmart.facts.entity.user.Role;
import com.soosmart.facts.enumpack.TypeDeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
    Role findById(UUID id);
    Role findByLibelle(TypeDeRole libelle);
}
