package com.hotel.hotelmanagement.controller;

import com.hotel.hotelmanagement.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {
    @GetMapping
    public ResponseDto welcome(){
        return  new ResponseDto(200,"Welcome to JAVA-HMS server :)");
    }

    @GetMapping("/api")
    public ResponseDto health(){
        return  new ResponseDto(200,"Server running...");
    }
}
