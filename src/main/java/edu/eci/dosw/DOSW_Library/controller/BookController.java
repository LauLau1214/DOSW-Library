package edu.eci.dosw.DOSW_Library.controller;


import edu.eci.dosw.DOSW_Library.controller.dto.BookDTO;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "Operaciones relacionadas con libros")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {

        this.bookService = bookService;
    }

    @Operation(summary = "Agregar un libro", description = "Agrega un nuevo libro o suma copias si ya existe")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })

    @PostMapping
    public ResponseEntity<Void> addBook(@RequestBody BookDTO bookDTO) {
        Book book = new Book(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getId(), true);
        bookService.addBook(book, bookDTO.getCopies());
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Obtener todos los libros")
    @ApiResponse(responseCode = "200", description = "Lista de libros")
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(summary = "Obtener libro por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Actualizar disponibilidad de un libro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PutMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable String id,
                                                   @RequestParam boolean available) {
        bookService.updateBookAvailability(id, available);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar un libro")
    @ApiResponse(responseCode = "204", description = "Libro eliminado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.removeBook(id);
        return ResponseEntity.noContent().build();
    }
}
