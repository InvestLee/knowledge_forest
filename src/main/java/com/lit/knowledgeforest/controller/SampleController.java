package com.lit.knowledgeforest.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController {

    @GetMapping("/sample1")
    public String sayHello(Model model) {
        model.addAttribute("hi", "hello");
        return "sample";
    }

}