package com.eikichis.houndjobapi.controller;

import com.eikichis.houndjobapi.dto.Job;
import com.eikichis.houndjobapi.dto.JobDTO;
import com.eikichis.houndjobapi.dto.Portal;
import com.eikichis.houndjobapi.services.PublicAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class OfertaController {

   @Autowired
   private PublicAPIService getonboardService;

   @GetMapping("/offers")
   public ResponseEntity<List<JobDTO>> getOferts(@RequestParam String jobs) throws Exception {
       List<JobDTO> ofertas = getonboardService.getOferts(jobs);
       return ResponseEntity.ok(ofertas);
   }

   @GetMapping("/alloffers")
   public ResponseEntity<List<JobDTO>> getAllOferts() throws Exception {
       List<JobDTO> ofertas = getonboardService.getAllOferts();
       return ResponseEntity.ok(ofertas);
   }


}
