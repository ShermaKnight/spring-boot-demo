package org.example.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.domain.dto.AuthorView;
import org.example.domain.dto.EditAuthorRequest;
import org.example.domain.dto.Page;
import org.example.domain.dto.SearchAuthorsQuery;
import org.example.domain.mapper.AuthorEditMapper;
import org.example.domain.mapper.AuthorViewMapper;
import org.example.domain.model.Author;
import org.example.domain.model.Book;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorEditMapper authorEditMapper;
    private final AuthorViewMapper authorViewMapper;

    @Transactional
    public AuthorView create(EditAuthorRequest request) {
        Author author = authorEditMapper.create(request);
        author = authorRepository.save(author);
        return authorViewMapper.toAuthorView(author);
    }

    @Transactional
    public AuthorView update(ObjectId id, EditAuthorRequest request) {
        Author author = authorRepository.getById(id);
        authorEditMapper.update(request, author);
        author = authorRepository.save(author);
        return authorViewMapper.toAuthorView(author);
    }

    @Transactional
    public AuthorView delete(ObjectId id) {
        Author author = authorRepository.getById(id);
        authorRepository.delete(author);
        bookRepository.deleteAll(bookRepository.findAllById(author.getBookIds()));
        return authorViewMapper.toAuthorView(author);
    }

    public AuthorView getAuthor(ObjectId id) {
        return authorViewMapper.toAuthorView(authorRepository.getById(id));
    }

    public List<AuthorView> getAuthors(Iterable<ObjectId> ids) {
        return authorViewMapper.toAuthorView(authorRepository.findAllById(ids));
    }

    public List<AuthorView> getBookAuthors(ObjectId bookId) {
        Book book = bookRepository.getById(bookId);
        return authorViewMapper.toAuthorView(authorRepository.findAllById(book.getAuthorIds()));
    }

    public List<AuthorView> searchAuthors(Page page, SearchAuthorsQuery query) {
        return authorViewMapper.toAuthorView(authorRepository.searchAuthors(page, query));
    }
}
