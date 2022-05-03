package com.livre.repository;

import com.livre.domain.Aparelho;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Aparelho entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AparelhoRepository extends JpaRepository<Aparelho, Long> {}
