package com.http.example;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * com.http.example.HttpController
 *
 * @author lipeng
 * @date 2019-08-16 15:46
 */
@RestController
public class HttpController {

    private List<Student> studentList = new ArrayList<>();

    @GetMapping("/hello")
    public String hello(@RequestParam("name") String name) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return "hello " + name;
    }

    @GetMapping("/retry")
    public String retry() {
        throw new NullPointerException();
    }

    @PostMapping("/students/")
    public String saveStudent(@RequestBody Student student) {
        studentList.add(student);
        return "SUCCESS";
    }

    @GetMapping("/students/")
    public List<Student> listStudent() {
        return studentList;
    }
}
