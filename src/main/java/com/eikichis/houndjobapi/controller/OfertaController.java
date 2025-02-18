package com.eikichis.houndjobapi.controller;

import com.eikichis.houndjobapi.dto.Job;
import com.eikichis.houndjobapi.services.GetonboardService;
import com.eikichis.houndjobapi.services.TrabajandoService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class OfertaController {

   @Autowired
   private GetonboardService getonboardService;

   @Autowired
   private TrabajandoService trabajandoService;

   @GetMapping("/getonboard")
   public ResponseEntity<List<JsonNode>> getOferts() throws Exception {
       List<JsonNode> ofertas = getonboardService.getOferts();
       return ResponseEntity.ok(ofertas);
   }

    @GetMapping("/scrape-jobs")
    public ResponseEntity<List<Job>> scrapeJobs(@RequestParam String query) {
        try {
            return ResponseEntity.ok(trabajandoService.scrapeJobs(query));
        } catch (IOException e) {
            throw new RuntimeException("Error durante el scraping", e);
        }
    }

}
