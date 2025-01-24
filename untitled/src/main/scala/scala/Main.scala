package scala

import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import java.io.File
import models.Temperatura
import dao.TemperaturaDAO

// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object Main extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/temperaturas.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List, Temperatura](rfc.withHeader.withCellSeparator(','))

  val temperaturas = dataSource.collect {
    case Right(temperatura) => temperatura
  }

  // Secuencia de operaciones IO usando for-comprehension

  /**
   * Punto de entrada principal de la aplicación.
   * Lee temperaturas desde CSV, las inserta en la base de datos,
   * e imprime el número de registros insertados.
   *
   * @return IO[Unit] que representa la secuencia de operaciones
   */
  def run: IO[Unit] =
    TemperaturaDAO.insertAll(temperaturas)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))

}