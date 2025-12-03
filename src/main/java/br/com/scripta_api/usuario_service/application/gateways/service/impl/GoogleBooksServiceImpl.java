package br.com.scripta_api.usuario_service.application.gateways.service.impl;

import br.com.scripta_api.usuario_service.application.gateways.service.GoogleBooksService;
import br.com.scripta_api.usuario_service.infra.data.LivroEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GoogleBooksServiceImpl implements GoogleBooksService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public LivroEntity buscarLivroPorIsbn(String isbn) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        try {
            Map response = restTemplate.getForObject(url, Map.class);
            if (response == null) {
                throw new RuntimeException("Resposta vazia da Google Books API");
            }

            // totalItems pode ser Integer ou Number — tratar com segurança
            Object totalItemsObj = response.get("totalItems");
            int totalItems = 0;
            if (totalItemsObj instanceof Number) {
                totalItems = ((Number) totalItemsObj).intValue();
            }

            if (totalItems == 0) {
                throw new RuntimeException("Livro não encontrado na Google Books API");
            }

            List items = (List) response.get("items");
            if (items == null || items.isEmpty()) {
                throw new RuntimeException("Nenhum item retornado pela Google Books API");
            }

            Map item0 = (Map) items.get(0);
            Map volumeInfo = (Map) item0.get("volumeInfo");
            if (volumeInfo == null) {
                throw new RuntimeException("volumeInfo ausente na resposta da Google Books API");
            }

            LivroEntity entity = new LivroEntity();

            // title
            Object titleObj = volumeInfo.get("title");
            String title = titleObj != null ? String.valueOf(titleObj) : "Título desconhecido";
            entity.setTitulo(title);

            // authors (pode ser array)
            Object authorsObj = volumeInfo.get("authors");
            if (authorsObj instanceof List && !((List) authorsObj).isEmpty()) {
                Object firstAuthor = ((List) authorsObj).get(0);
                entity.setAutor(firstAuthor != null ? String.valueOf(firstAuthor) : "Autor desconhecido");
            } else {
                entity.setAutor("Autor desconhecido");
            }

            // ISBN: seu controller passa o ISBN, então setamos ele direto
            entity.setIsbn(isbn);

            // publishedDate pode ter formato "YYYY" ou "YYYY-MM-DD"
            Object publishedDateObj = volumeInfo.get("publishedDate");
            if (publishedDateObj != null) {
                String pd = String.valueOf(publishedDateObj);
                try {
                    int ano = Integer.parseInt(pd.substring(0, Math.min(4, pd.length())));
                    entity.setAnoPublicacao(ano);
                } catch (Exception e) {
                    entity.setAnoPublicacao(0);
                }
            } else {
                entity.setAnoPublicacao(0);
            }

            // quantity defaults
            entity.setQuantidadeTotal(1);
            entity.setQuantidadeDisponivel(1);

            return entity;

        } catch (Exception e) {
            log.error("Erro ao buscar livro por ISBN {}: {}", isbn, e.getMessage(), e);
            throw new RuntimeException("Erro ao importar livro: " + e.getMessage());
        }
    }
}
