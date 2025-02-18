package com.eikichis.houndjobapi.services.Impl;


import com.eikichis.houndjobapi.dto.Job;
import com.eikichis.houndjobapi.services.TrabajandoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrabajandoServiceImpl implements TrabajandoService {

    private static final String BASE_URL = "https://cl.computrabajo.com/";
    @Override
    public List<Job> scrapeJobs(String searchQuery) throws IOException {
        // Construir la URL de búsqueda
        String searchUrl = BASE_URL  + searchQuery;

        // Conectar y obtener el documento HTML
        Document document = Jsoup.connect(searchUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36").get();

        // Seleccionar elementos relevantes
        Elements jobListings = document.select("article.box_offer"); // Selecciona los elementos de ofertas de trabajo

        System.out.println(jobListings);
        List<Job> jobs = new ArrayList<>();
            String titulo = "";
            String company = "";
            String location = "";
            String link  = "";
            for (Element job : jobListings) {
                titulo = job.select("h2.fs18").text();
                company = job.select("a.fc_base").text(); // Nombre de la empresa
                location = job.select("span.mr10").text(); // Ubicación
                link = job.select("a.fs18").attr("href"); // Enlace a la oferta

                System.out.println("Título: " + titulo);
                System.out.println("Empresa: " + company);
                System.out.println("Ubicación: " + location);
                System.out.println("Enlace: https://cl.computrabajo.com" + link);
                System.out.println("-----------------------------");
                jobs.add(new Job(titulo, company, location, link));
            }




        return jobs;
    }
}
