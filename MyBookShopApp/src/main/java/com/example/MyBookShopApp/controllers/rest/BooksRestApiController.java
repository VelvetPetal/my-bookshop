package com.example.MyBookShopApp.controllers.rest;

import com.example.MyBookShopApp.dto.ResultDto;
import com.example.MyBookShopApp.dto.book.BooksPageDto;
import com.example.MyBookShopApp.dto.book.ChangeStatusPayload;
import com.example.MyBookShopApp.dto.book.request.RequestDto;
import com.example.MyBookShopApp.dto.review.BookRatingDto;
import com.example.MyBookShopApp.dto.review.MyReviewDto;
import com.example.MyBookShopApp.dto.review.ReviewLikeDto;
import com.example.MyBookShopApp.services.author.AuthorService;
import com.example.MyBookShopApp.services.book.BookService;
import com.example.MyBookShopApp.services.bookStatus.BookStatusService;
import com.example.MyBookShopApp.services.genre.GenreService;
import com.example.MyBookShopApp.services.payment.PaymentService;
import com.example.MyBookShopApp.services.ratingAndReview.RatingReviewService;
import com.example.MyBookShopApp.services.search.SearchService;
import com.example.MyBookShopApp.services.tag.TagService;
import com.example.MyBookShopApp.services.util.CookieUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api("book data api")
@RequiredArgsConstructor
@Slf4j
public class BooksRestApiController {
    private final BookService bookService;
    private final SearchService searchService;
    private final AuthorService authorService;
    private final RatingReviewService ratingService;

    private final BookStatusService bookStatusService;
    private final GenreService genreService;
    private final TagService tagService;
    private final CookieUtils cookieUtils;
    private final PaymentService paymentService;


    @GetMapping("/books/recommended")
    public BooksPageDto getRecommendedBookPage(RequestDto request) {
        return bookService.getRecommendedBooksPage(request);
    }

    @GetMapping("/books/popular")
    public BooksPageDto getPopularBookPage(RequestDto request) {
        return bookService.getPopularBooksPage(request);
    }

    @GetMapping("/books/recent")
    public BooksPageDto getRecentBookPage(RequestDto request) {
        return bookService.getRecentBooksPage(request);
    }


    @GetMapping("/books/tag/{slug}")
    public BooksPageDto tagPage(RequestDto request) {
        return tagService.getBooksPageByTag(request);
    }

    @GetMapping("/books/genre/{slug}")
    public BooksPageDto genrePage(RequestDto request) {
        return genreService.getBooksPageByGenre(request);
    }

    @GetMapping("/books/author/{slug}")
    public BooksPageDto bookOfAuthorPage(RequestDto request) {
        return authorService.getBooksPageByAuthor(request);
    }

    @PostMapping("/rateBook")
    public ResultDto rateBook(
            @RequestBody BookRatingDto bookRatingDto) {
        return ratingService.addRating(bookRatingDto);
    }

    @GetMapping("/search/{searchWord}")
    public BooksPageDto getNextSearchPage(RequestDto request) {
        return searchService.getPageOfGoogleBooksApiSearchResult(request);
    }

    @PostMapping("/bookReview")
    public ResultDto addBookReview(@RequestBody MyReviewDto reviewDto) {
        return ratingService.addBookReview(reviewDto);
    }

    @PostMapping("/rateBookReview")
    public ResultDto rateBookReview(@RequestBody ReviewLikeDto reviewRatingDto) {
        return ratingService.addReviewRating(reviewRatingDto);

    }

    @PostMapping("/anonym/changeBookStatus")
    public ResponseEntity<?> changeBookStatusAnonym(@RequestBody RequestDto request) {
        cookieUtils.addBook2Cookie(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/changeBookStatus")
    public ResultDto changeBookStatus(@RequestBody ChangeStatusPayload changeStatusPayload) {
        return bookStatusService.changeBookStatus(changeStatusPayload);
    }

    @GetMapping("/payment")
    public ResultDto handlePay() {
        return paymentService.buyCartItems();
    }
}
