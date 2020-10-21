package org.hse.lycsovet.controller

import org.hse.lycsovet.Article
import org.hse.lycsovet.ArticleDTO
import org.hse.lycsovet.service.NewsServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
    fun publish(@RequestBody articleDTO: ArticleDTO): ResponseEntity<Map<String, Any?>> {
        val id = newsService.publish(articleDTO)
        val result = HashMap<String, Any?>()

        result["id"] = id

        return ResponseEntity.ok(result)
    }

    @PutMapping("/{id}")
    fun publish(@PathVariable id: Long): ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        newsService.publish(id)
        result["status"] = "ok"

        return ResponseEntity.ok(result)
    }

    @PutMapping
    fun edit(@RequestBody articleDTO: ArticleDTO): ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        newsService.edit(articleDTO)
        result["status"] = "ok"

        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Map<String, Any?>> {
        val result = HashMap<String, Any?>()
        newsService.delete(id)
        result["status"] = "ok"

        return ResponseEntity.ok(result)
    }

}