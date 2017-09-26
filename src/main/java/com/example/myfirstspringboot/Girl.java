package com.example.myfirstspringboot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author liango
 * @version 1.0
 * @since 2017-09-26 15:58
 */
@Entity
public class Girl {
    @Id
    @GeneratedValue
    private Integer id;
    
    private String cupSize;

    private Integer age;


    /**
     * 必须加上这个无参构造函数，参会莫给给你建表： girl
     */
    public Girl() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCupSize() {
        return cupSize;
    }

    public void setCupSize(String cupSize) {
        this.cupSize = cupSize;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
