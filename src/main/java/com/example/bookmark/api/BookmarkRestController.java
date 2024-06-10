package com.example.bookmark.api;

import com.example.bookmark.DataInitializer;
import com.example.bookmark.service.Bookmark;
import com.example.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkRestController {

    private final BookmarkService bookmarkService;

    public BookmarkRestController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @Operation(
            summary = "Retrieves list of bookmarks for given user",
            tags = {"Bookmark-API"},
            parameters = {@Parameter(name = "userid", description = "The identifier of the user", required = true, example = DataInitializer.USERID_BRUCE_WAYNE)}
    )
    @ResponseStatus(OK)
    @GetMapping
    List<Bookmark> findAllBookmarks(@RequestParam("userid") String userid) {
        return bookmarkService.findAllBookmarksByUser(userid);
    }

    @Operation(
            summary = "Retrieves bookmark with given id for given user",
            tags = {"Bookmark-API"},
            parameters = {@Parameter(name = "userid", description = "The identifier of the user", required = true, example = DataInitializer.USERID_BRUCE_WAYNE)}
    )
    @ResponseStatus(OK)
    @GetMapping("/{id}")
    ResponseEntity<Bookmark> findBookmarkWithId(@PathVariable("id") String id) {
        return bookmarkService.findOneBookmarkByIdentifier(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Searches bookmarks by name",
            tags = {"Bookmark-API"},
            parameters = {@Parameter(name = "name", description = "The name of the bookmarks to be searched for", required = true, example = "Ama")}
    )
    @ResponseStatus(OK)
    @GetMapping("/search")
    List<Bookmark> searchBookmarks(@RequestParam("name") String name) {
        return bookmarkService.search(name);
    }

    @Operation(
            summary = "Creates a new bookmark for given user",
            tags = {"Bookmark-API"}
    )
    @ResponseStatus(CREATED)
    @PostMapping
    Bookmark createBookmark(@RequestBody Bookmark bookmark) {
        return bookmarkService.create(bookmark);
    }

    @Operation(
            summary = "Deletes an existing bookmark by its identifier",
            tags = {"Bookmark-API"}
    )
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{bookmarkId}")
    void deleteBookmark(@PathVariable("bookmarkId") String bookmarkIdentifier) {
        bookmarkService.deleteBookmarkEntityByIdentifier(bookmarkIdentifier);
    }

    @Operation(
            summary = "Uploads bookmarks inside an excel sheet",
            tags = {"Bookmark-API"}
    )
    @ResponseStatus(OK)
    @PostMapping("/upload")
    List<Bookmark> uploadBookmarks(@RequestParam("file") MultipartFile file) {
        return bookmarkService.importBookmarks(file);
    }


}
