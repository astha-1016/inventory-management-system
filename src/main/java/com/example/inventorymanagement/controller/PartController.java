package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Part;
import com.example.inventorymanagement.service.ExcelExportService;
import com.example.inventorymanagement.service.PartService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping
    public String listParts(
            @RequestParam(required = false) String keyword,
            Model model) {

        model.addAttribute("parts", partService.searchParts(keyword));
        model.addAttribute("keyword", keyword);

        return "parts";
    }

    @GetMapping("/new")
    public String showPartForm(Model model) {
        model.addAttribute("part", new Part());
        model.addAttribute("pageTitle", "Add New Part");
        return "part-form";
    }

    // ==========================
    @PostMapping("/save")
    public String savePart(@ModelAttribute Part part) {
        partService.savePart(part);
        return "redirect:/parts";
    }

    @GetMapping("/edit/{id}")
    public String showEditPartForm(@PathVariable Long id, Model model) {

        Part part = partService.getPartById(id);

        model.addAttribute("part", part);
        model.addAttribute("pageTitle", "Edit Part");

        return "part-form";
    }

    @PostMapping("/update/{id}")
    public String updatePart(@PathVariable Long id,
                             @ModelAttribute Part part) {

        part.setId(id);

        partService.savePart(part);

        return "redirect:/parts";
    }

    @GetMapping("/delete/{id}")
    public String deletePart(@PathVariable Long id) {

        partService.deletePart(id);

        return "redirect:/parts";
    }

}