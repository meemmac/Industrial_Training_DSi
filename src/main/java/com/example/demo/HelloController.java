package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Main Page";
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return """
        <html>
          <head>
            <title>Hello</title>
          </head>
          <body style="margin:0; height:100vh; display:flex; justify-content:center; align-items:center;">
            <div style="font-size:64px; font-weight:700;">
              Hello, World!
            </div>
          </body>
        </html>
        """;
    }
}