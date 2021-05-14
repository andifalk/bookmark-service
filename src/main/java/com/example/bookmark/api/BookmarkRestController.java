package com.example.bookmark.api;

import com.example.bookmark.DataInitializer;
import com.example.bookmark.service.Bookmark;
import com.example.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

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
}
