package it.unipi.Wikifont;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WikifontApplicationTests {
        Gson gson=new Gson();
        WikifontApplicationTests.Font font=new WikifontApplicationTests.Font(
                        "FontProva",
                        "FontProva",
                        "0000-01-01",
                        "FontProva",
                        "FontProva",
                        "FontProva"
                        );
        Variants variant=new Variants(
                            "VariantProva",
                            font.getFamily(),
                            "VariantProva");
        
        @Autowired
        private MockMvc mvc;
        
	@Test
	void contextLoads() {
	}
        
        //Controlla che il server risponda correttamente a /all
        @Test
        public void getAllFont() throws Exception{
            mvc.perform(MockMvcRequestBuilders
                            .get("/font/all")
                            .accept(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk());
        }
        
        //Controlla che il server risponda correttamente a /caricadati
        @Test
        public void getData() throws Exception{
            mvc.perform(MockMvcRequestBuilders
                            .get("/font/caricadati")
                            .accept(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk());
        }
        
        //Controlla che le risposte ad /all e /caricadati siano le stesse
        @Test
        public void equalsData() throws Exception{
            String all= mvc.perform(MockMvcRequestBuilders
                            .get("/font/all")
                            .accept(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
            
            String caricadati= mvc.perform(MockMvcRequestBuilders
                            .get("/font/caricadati")
                            .accept(MediaType.APPLICATION_JSON))
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
            
            Assertions.assertEquals(all,caricadati,"/all e /caricadati non resituiscono la stessa risposta");
        }
        
        
        
        @Test
        public void tryAddDeleteNewFont() throws Exception{
            //Test di aggiunta di un font
            mvc.perform(MockMvcRequestBuilders
                    .post("/font/addfont")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(font).getBytes()))
                    .andExpect(status().isOk());
        
            //Test di aggiunta di una variante al font precedentemente caricato
            mvc.perform(MockMvcRequestBuilders
                    .post("/font/addvariant")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(variant).getBytes()))
                    .andExpect(status().isOk());
            
            //Test di persistenza della variante appena aggiunta
            mvc.perform(MockMvcRequestBuilders
                            .post("/font/variants")
                            .param("family", font.getFamily())
                            .accept(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk());
        
            //Test di rimozione della variante appena aggiunta
            mvc.perform(MockMvcRequestBuilders
                            .post("/font/deletevariant")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(variant).getBytes())
                            .accept(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk());
            
            //Test di rimozione del font appena aggiunto
            mvc.perform(MockMvcRequestBuilders
                            .post("/font/deletefont")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(font).getBytes())
                            .accept(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isOk());
        }
        //È stata inserita la classe font dei client in modo da comunicare con il server come un client durante i test
        class Font {
            public String family;
            public String version;
            public String lastModified;
            public String category;
            public String kind;
            public String menu;

            //Costruttori
            public Font(String family, String version, String lastModified, String category, String kind, String menu) {
                this.family = family;
                this.version = version;
                this.lastModified = lastModified;
                this.category = category;
                this.kind = kind;
                this.menu = menu;
            }

            public Font() {
            }

            //Metodi getter e setter
            public String getFamily() {
                return family;
            }

            public String getVersion() {
                return version;
            }

            public String getLastModified() {
                return lastModified;
            }

            public String getCategory() {
                return category;
            }

            public String getKind() {
                return kind;
            }

            public String getMenu() {
                return menu;
            }

            public void setFamily(String family) {
                this.family = family;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public void setLastModified(String lastModified) {
                this.lastModified = lastModified;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public void setMenu(String menu) {
                this.menu = menu;
            }


        }
}
