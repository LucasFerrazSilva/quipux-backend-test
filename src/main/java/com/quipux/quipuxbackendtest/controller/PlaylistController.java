package com.quipux.quipuxbackendtest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lists")
public class PlaylistController {

    @GetMapping
    public String list() {
        return "ok";
    }

}
