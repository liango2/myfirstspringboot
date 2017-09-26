package com.example.myfirstspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liango
 * @version 1.0
 * @since 2017-09-26 23:52
 */
@Service
public class GirlService {

    @Autowired
    private GirlRepository girlRepository;

    /**
     * 必须使用InnoDB引擎才支持这里的事务
     * > alter table girl engine=InnoDB;
     */
    @Transactional
    public void insertTwo() {
        Girl girl1 = new Girl();
        girl1.setAge(16);
        girl1.setCupSize("A");
        girlRepository.save(girl1);

        Girl girl2 = new Girl();
        girl2.setAge(17);
        girl2.setCupSize("BBB");
        girlRepository.save(girl2);
    }
}
