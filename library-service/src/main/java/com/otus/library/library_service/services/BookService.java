package com.otus.library.library_service.services;

import com.otus.library.library_service.dto.request.BookReqDto;
import com.otus.library.library_service.dto.response.BookRespDto;

import java.util.List;

public interface BookService {

    List<BookRespDto> findAll();

    BookRespDto findById(long id);

    List<BookRespDto> searchBooks(String title, String author, Long genreId, String year);

    BookRespDto create(BookReqDto request);

    BookRespDto updateBook(Long id, BookReqDto request);

    void deleById(long id);
}
