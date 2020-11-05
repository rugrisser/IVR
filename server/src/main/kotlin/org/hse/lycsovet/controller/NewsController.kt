package org.hse.lycsovet.controller

import org.hse.lycsovet.Article
import org.hse.lycsovet.ArticleDTO
import org.hse.lycsovet.service.NewsServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/news")
class NewsController(
        private val newsService: NewsServiceImpl
) {
    @GetMapping("/{id}")
    fun article(@PathVariable id: Long): ResponseEntity<Article> {
        return ResponseEntity.ok(newsService.get(id))
    }

    @GetMapping
    fun feed(): ResponseEntity<List<Article>> {
        return ResponseEntity.ok(newsService.feed())
    }

    @GetMapping("/all")
    fun all(): ResponseEntity<List<Article>> {
        return ResponseEntity.ok(newsService.all())
    }

    @PostMapping
    fun publish(
            @RequestHeader("Authorization") token: String,
            @RequestBody articleDTO: ArticleDTO
    ): ResponseEntity<Map<String, Any?>> {
        val id = newsService.publish(token, articleDTO)
        val result = HashMap<String, Any?>()

        result["id"] = id

        return ResponseEntity.ok(result)
    }

    @PutMapping("/{id}")
    fun publish(
            @RequestHeader("Authorization") token: String,
            @PathVariable id: Long
    ): ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        newsService.publish(token, id)
        result["status"] = "ok"

        return ResponseEntity.ok(result)
    }

    @PutMapping
    fun edit(
            @RequestHeader("Authorization") token: String,
            @RequestBody articleDTO: ArticleDTO
    ): ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        newsService.edit(token, articleDTO)
        result["status"] = "ok"

        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{id}")
    fun delete(
            @RequestHeader("Authorization") token: String,
            @PathVariable id: Long
    ): ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        newsService.delete(token, id)
        result["status"] = "ok"

        return ResponseEntity.ok(result)
    }

}