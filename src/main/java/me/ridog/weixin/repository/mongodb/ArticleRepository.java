package me.ridog.weixin.repository.mongodb;

import me.ridog.weixin.dto.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Tate on 2016/10/27.
 */
public interface ArticleRepository extends MongoRepository<Article, String> {

    Page<Article> findAll(Pageable pageable);

    Article findById(String id);
}
