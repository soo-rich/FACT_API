package com.soosmart.facts.repository.jwt;

import com.soosmart.facts.entity.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenDAO extends JpaRepository<RefreshToken, UUID> {
}
