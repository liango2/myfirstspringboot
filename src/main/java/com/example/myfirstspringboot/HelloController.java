package com.example.myfirstspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liango
 * @version 1.0
 * @since 2017-09-26 15:28
 */
@RestController
//@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private GirlProperties girlProperties;

    // @RequestMapping(value = "/{id}/say", method = RequestMethod.GET)
    // String say(@PathVariable("id") Integer id, @RequestParam(value = "age", required = false, defaultValue = "-3") String age){


    @GetMapping(value = {"hello", "hi", "say"})
    String say(@RequestParam(value = "id", required = false, defaultValue = "0") String id) {
        return "id: " + id;
//        return "妹子罩杯：" + girlProperties.getCupSize();
//        return "index";
    }

}
