/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.Wikifont;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Michele Castrucci 636159
 */
@Controller
@RequestMapping(path="/font")
public class MainController {
    //Numero massimo di font caricati
    private static final int MAXDB=1800;
    
    @Autowired
    private FontRepository fontRepository;
    
    @Autowired
    private VariantsRepository variantsRepository;
    
    //Resituisce tutti i font
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Font> getAllFonts(){
        return fontRepository.findAll();
    }

    //Carica i dati dalla api di google, e restituisce lo stesso risultato di getAllFonts()
    @GetMapping(path="/caricadati")
    public @ResponseBody Iterable<Font> dowloadData() throws MalformedURLException, ProtocolException, IOException {
        
        //controlla se il databse è stato già riempito nel caso si comporta come getAllFonts()
        Iterable<Font> fonts =fontRepository.findAll();
        if(fonts.iterator().hasNext()){
            return fonts;
        }
        
        //Si connette all'api di google per scaricare i font
        URL url = new URL("https://www.googleapis.com/webfonts/v1/webfonts?key="/*TO-DO inserire la chiave*/);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        //Inserisce il risultato in una StringBuffer
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        
        //Deserializza i dati ottenuti dall'api e gli inserisce in due tabelle font e variant
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(content.toString(), JsonElement.class);
        JsonObject rootObject = json.getAsJsonObject();
        JsonArray items = rootObject.get("items").getAsJsonArray();
        
        //Ogni font viene inserito nella tabella font
        for (int i = 0; i < items.size()&&i<MAXDB; i++) {
            JsonObject d = items.get(i).getAsJsonObject();
            Font font= new Font(
                    d.get("family").getAsString(),
                    d.get("version").getAsString(),
                    Date.valueOf(d.get("lastModified").getAsString()),
                    d.get("category").getAsString(),
                    d.get("kind").getAsString(),
                    d.get("menu").getAsString());
            fontRepository.save(font);
            JsonObject variants=d.get("files").getAsJsonObject();
            
            //Per ogni font vengono inserite le varianti corrispondenti nella tabella varianti
            for(String key: variants.keySet()){
                VariantsDB variant=new VariantsDB(
                        new VariantsDB.PrimaryKey (
                        font.getFamily(),
                        key),    
                        font,    
                        variants.get(key).getAsString());
                variantsRepository.save(variant);
            }
        }
        //Restituisce lo stesso risultato di getAllFonts()
        return getAllFonts();
    }
    
    //Cancella tutte le varianti del font e il font che gli viene passato nel body della richiesta
    @PostMapping(path="/deletefont")
    public @ResponseBody String deleteFont (@RequestBody Font f) {
        
        //Prima cancella tutte le varianti legate al font che si desidera eliminare
        for(VariantsDB vdb: variantsRepository.findAllByVariantFamily(f.getFamily())){
            variantsRepository.delete(vdb);
        }
        
        //Elimina il font
        fontRepository.delete(f);
        return "done";
    }
    
    //Cancella la variante che gli viene passata nel body
    @PostMapping(path="/deletevariant")
    public @ResponseBody String deleteVariant (@RequestBody Variants variant) {
        variantsRepository.delete(
                //Trasforma la variants in una VariantsDB per comunicare con il Database
                new VariantsDB(
                        new VariantsDB.PrimaryKey(
                                variant.getFamily(),
                                variant.getVariant()
                        ),
                        fontRepository.findByFamily(variant.getFamily()),
                        variant.getLink()
                ));
        return "done";
    }
    
    //Restituisce tutte le varianti di una famiglia di font che viene passata come parametro nella richiesta
    @PostMapping(path="/variants")
    public @ResponseBody Iterable<Variants> getVariants (@RequestParam String  family) {
        //Cerca tutte le varianti con quella determinata famiglia di font
        List<VariantsDB> vdb=variantsRepository.findAllByVariantFamily(family);
        
        //Trasforma la lista di VariantsDB in una lista di Variants e la restituisce
        ArrayList<Variants> variants=new ArrayList<>();
        for(VariantsDB v: vdb){
            variants.add( new Variants(
                    v.getVariant().getFamily(),
                    v.getVariant().getVariant(),
                    v.getLink()));
        }
        return variants;
    }
    
    //Aggiunge il font che gli viene passato nel body della richiesta
    @PostMapping(path="/addfont")
    public @ResponseBody String addNewFont(@RequestBody Font font){
        fontRepository.save(font);
        return "done";
    }
        //Aggiunge la variante che gli viene passata nel body della richiesta, restituisce error se non esiste il font a cui appartiene la variante
    @PostMapping(path="/addvariant")
    public @ResponseBody String addNewVariant(@RequestBody Variants variants){
        Font font=fontRepository.findByFamily(variants.getFamily());
        if(font!=null){
            //Aggiunge la variante
            variantsRepository.save(
                //Trasforma la Variants in una VariantsDB
                new VariantsDB(
                    new VariantsDB.PrimaryKey(variants.getFamily(),variants.getVariant()),
                    font,
                    variants.getLink()
            ));
            return "done";
        }
        return "error";
    }
}
