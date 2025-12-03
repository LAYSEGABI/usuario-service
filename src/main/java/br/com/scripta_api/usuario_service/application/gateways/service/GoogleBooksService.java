package br.com.scripta_api.usuario_service.application.gateways.service;

import br.com.scripta_api.usuario_service.infra.data.LivroEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleBooksService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public LivroEntity buscarLivroPorIsbn(String isbn) {
        try {
            String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            String json = restTemplate.getForObject(url, String.class);

            JsonNode root = mapper.readTree(json);
            JsonNode items = root.path("items");
            if (items.isMissingNode() || items.isEmpty()) {
                throw new RuntimeException("Livro não encontrado na Google Books API");
            }

            JsonNode volumeInfo = items.get(0).path("volumeInfo");

            LivroEntity livro = new LivroEntity();
            livro.setIsbn(isbn);
            livro.setTitulo(volumeInfo.path("title").asText("Título não encontrado"));

            // authors pode ser array — pegar o primeiro se existir
            if (volumeInfo.path("authors").isArray() && volumeInfo.path("authors").size() > 0) {
                livro.setAutor(volumeInfo.path("authors").get(0).asText("Autor não encontrado"));
            } else {
                livro.setAutor("Autor não encontrado");
            }

            // publishedDate pode ter formatos "YYYY", "YYYY-MM-DD", etc.
            String publishedDate = volumeInfo.path("publishedDate").asText("");
            livro.setAnoPublicacao(extrairAno(publishedDate));
            livro.setQuantidadeTotal(1);
            livro.setQuantidadeDisponivel(1);

            return livro;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar livro: " + e.getMessage(), e);
        }
    }

    private int extrairAno(String publishedDate) {
        try {
            if (publishedDate == null || publishedDate.isEmpty()) return 0;
            return Integer.parseInt(publishedDate.substring(0, 4));
        } catch (Exception e) {
            return 0;
        }
    }
}
