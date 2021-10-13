package org.example.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.domain.dto.BookView;
import org.example.domain.dto.EditBookRequest;
import org.example.domain.dto.Page;
import org.example.domain.dto.SearchBooksQuery;
import org.example.domain.mapper.BookEditMapper;
import org.example.domain.mapper.BookViewMapper;
import org.example.domain.model.Author;
import org.example.domain.model.Book;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookEditMapper bookEditMapper;
    private final BookViewMapper bookViewMapper;

    @Transactional
    public BookView create(EditBookRequest request) {
        Book book = bookEditMapper.create(request);
        book = bookRepository.save(book);
        updateAuthors(book);
        return bookViewMapper.toBookView(book);
    }

    @Transactional
    public BookView update(ObjectId id, EditBookRequest request) {
        Book book = bookRepository.getById(id);
        bookEditMapper.update(request, book);
        book = bookRepository.save(book);
        if (!CollectionUtils.isEmpty(request.getAuthorIds())) {
            updateAuthors(book);
        }
        return bookViewMapper.toBookView(book);
    }

    private void updateAuthors(Book book) {
        List<Author> authors = authorRepository.findAllById(book.getAuthorIds());
        authors.forEach(author -> author.getBookIds().add(book.getId()));
        authorRepository.saveAll(authors);
    }

    @Transactional
    public BookView delete(ObjectId id) {
        Book book = bookRepository.getById(id);
        bookRepository.delete(book);
        return bookViewMapper.toBookView(book);
    }

    public BookView getBook(ObjectId id) {
        Book book = bookRepository.getById(id);
        return bookViewMapper.toBookView(book);
    }

    public List<BookView> getBooks(Iterable<ObjectId> ids) {
        List<Book> books = bookRepository.findAllById(ids);
        return bookViewMapper.toBookView(books);
    }

    public List<BookView> getAuthorBooks(ObjectId authorId) {
        Author author = authorRepository.getById(authorId);
        return bookViewMapper.toBookView(bookRepository.findAllById(author.getBookIds()));
    }

    public List<BookView> searchBooks(Page page, SearchBooksQuery query) {
        return bookViewMapper.toBookView(bookRepository.searchBooks(page, query));
    }
}
