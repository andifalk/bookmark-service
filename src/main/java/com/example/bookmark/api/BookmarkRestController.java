package com.example.bookmark.api;

import com.example.bookmark.DataInitializer;
import com.example.bookmark.service.Bookmark;
import com.example.bookmark.service.BookmarkService;
import com.example.bookmark.service.User;
import com.google.common.escape.Escaper;
import com.google.common.html.types.SafeUrl;
import com.google.common.html.types.SafeUrls;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/bookmarks")
@Validated
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
    List<Bookmark> findAllBookmarks(@RequestParam("userid") UUID userIdentifier, @AuthenticationPrincipal User user) {
        return bookmarkService.findAllBookmarksByUser(userIdentifier, user);
    }

    @Operation(
            summary = "Searches bookmarks by name",
            tags = {"Bookmark-API"},
            parameters = {@Parameter(name = "name", description = "The name of the bookmarks to be searched for", required = true, example = "Ama")}
    )
    @ResponseStatus(OK)
    @GetMapping("/search")
    List<Bookmark> searchBookmarks(@RequestParam("name") String name, @AuthenticationPrincipal User user) {
        return bookmarkService.search(name, user.getIdentifier());
    }

    @Operation(
            summary = "Creates a new bookmark for given user",
            tags = {"Bookmark-API"}
    )
    @ResponseStatus(CREATED)
    @PostMapping
    Bookmark createBookmark(@Valid @RequestBody Bookmark bookmark, @AuthenticationPrincipal User user) {
        try {
            String sanitizedUrl = SafeUrls.sanitize(bookmark.getUrl().toString()).getSafeUrlString();
            if (sanitizedUrl.equals(SafeUrl.INNOCUOUS_STRING)) {
                throw new IllegalArgumentException("Invalid URL " + bookmark.getUrl());
            }
            bookmark.setUrl(new URL(sanitizedUrl));
            return bookmarkService.create(bookmark, user.getIdentifier());
        } catch(MalformedURLException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @Operation(
            summary = "Deletes an existing bookmark by its identifier",
            tags = {"Bookmark-API"}
    )
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{bookmarkId}")
    void deleteBookmark(@PathVariable("bookmarkId") UUID bookmarkIdentifier, @AuthenticationPrincipal User user) {
        bookmarkService.deleteBookmarkEntityByIdentifier(bookmarkIdentifier, user.getIdentifier());
    }

    @Operation(
            summary = "Uploads bookmarks inside an excel sheet",
            tags = {"Bookmark-API"}
    )
    @ResponseStatus(OK)
    @PostMapping("/upload")
    List<Bookmark> uploadBookmarks(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) {
        return bookmarkService.importBookmarks(file, user);
    }


}
