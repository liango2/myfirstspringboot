package com.example.myfirstspringboot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author liango
 * @version 1.0
 * @since 2017-09-26 15:58
 */
public interface GirlRepository extends JpaRepository<Girl, Integer> {

    public List<Girl> findByAge(Integer age);
}
