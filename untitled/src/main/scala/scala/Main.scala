package scala
import config.Database._
import models._
import config._
import cats.effect.{IO, IOApp}
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import java.io.File
import models.Estudiante
import dao._
import doobie.{ConnectionIO, HC}
import doobie.implicits._
import cats.effect.unsafe.implicits.global



// Extiende de IOApp.Simple para manejar efectos IO y recursos de forma segura
object Main extends IOApp.Simple {
  val path2DataFile2 = "src/main/resources/data/estudiantes.csv"

  val dataSource = new File(path2DataFile2)
    .readCsv[List, Estudiante](rfc.withHeader.withCellSeparator(','))

  val estudiante = dataSource.collect {
    case Right(estudiante) => estudiante
  }

  def run: IO[Unit] = {
    //val(transactor, cleanup) = Database.transactor.allocated.unsafeRunSync(
    EstudiantesDAO.insertAll(estudiante)
      .flatMap(result => IO.println(s"Registros insertados: ${result.size}"))
  }
}