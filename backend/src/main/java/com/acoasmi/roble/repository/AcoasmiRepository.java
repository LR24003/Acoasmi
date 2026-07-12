package com.acoasmi.roble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AcoasmiRepository<T, ID> extends JpaRepository<T, ID> {

}
