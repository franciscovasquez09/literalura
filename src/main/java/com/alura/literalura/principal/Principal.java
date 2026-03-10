package com.alura.literalura.principal;

import com.alura.literalura.dto.DatosLibro;
import com.alura.literalura.dto.DatosRespuesta;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {

    private Scanner teclado = new Scanner(System.in);

    private ConsumoAPI consumo = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    private final String URL_BASE = "https://gutendex.com/books/?search=";

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public void muestraMenu(){

        var opcion = -1;

        while(opcion != 0){

            System.out.println("""
                    
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos por año
                    5 - Listar libros por idioma
                    
                    0 - Salir
                    """);

            opcion = teclado.nextInt();
            teclado.nextLine();

            switch(opcion){

                case 1:
                    buscarLibro();
                    break;

                case 2:
                    listarLibrosRegistrados();
                    break;

                case 3:
                    listarAutoresRegistrados();
                    break;

                case 4:
                    listarAutoresVivos();
                    break;

                case 5:
                    listarLibrosPorIdioma();
                    break;

                case 0:
                    System.out.println("Cerrando aplicación...");
                    break;

                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibro(){

        System.out.println("Escribe el nombre del libro:");
        var nombreLibro = teclado.nextLine();

        var json = consumo.obtenerDatos(URL_BASE + nombreLibro.replace(" ","%20"));

        DatosRespuesta datos = conversor.obtenerDatos(json, DatosRespuesta.class);

        Optional<DatosLibro> libroBuscado = datos.resultados()
                .stream()
                .findFirst();

        if(libroBuscado.isPresent()){

            DatosLibro datosLibro = libroBuscado.get();

            Autor autor = new Autor(
                    datosLibro.autores().get(0).nombre(),
                    datosLibro.autores().get(0).nacimiento(),
                    datosLibro.autores().get(0).muerte()
            );

            autorRepository.save(autor);

            Libro libro = new Libro(
                    datosLibro.titulo(),
                    datosLibro.idiomas().get(0),
                    datosLibro.descargas(),
                    autor
            );

            libroRepository.save(libro);

            System.out.println("Libro guardado correctamente:");
            System.out.println(libro);

        }else{
            System.out.println("Libro no encontrado.");
        }
    }

    private void listarLibrosRegistrados(){

        List<Libro> libros = libroRepository.findAll();

        if(libros.isEmpty()){
            System.out.println("No hay libros registrados.");
        }else{
            libros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados(){

        List<Autor> autores = autorRepository.findAll();

        if(autores.isEmpty()){
            System.out.println("No hay autores registrados.");
        }else{
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivos(){

        System.out.println("Ingrese el año:");
        var anio = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores = autorRepository.findAll();

        autores.stream()
                .filter(a -> a.getNacimiento() <= anio && a.getMuerte() >= anio)
                .forEach(System.out::println);
    }

    private void listarLibrosPorIdioma(){

        System.out.println("""
                Ingrese el idioma:
                
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);

        var idioma = teclado.nextLine();

        List<Libro> libros = libroRepository.findAll();

        libros.stream()
                .filter(l -> l.getIdioma().contains(idioma))
                .forEach(System.out::println);
    }

}