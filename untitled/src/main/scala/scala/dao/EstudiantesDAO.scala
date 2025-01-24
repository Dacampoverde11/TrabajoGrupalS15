package scala.dao

import doobie._
import doobie.implicits._
import cats.effect.IO
import cats.implicits._

import models.Estudiante
import config.Database

object EstudiantesDAO {
  def insert(estudiante: Estudiante): ConnectionIO[Int] = {
    sql"""
     INSERT INTO alumnos (nombre, edad, calificacion, genero)
     VALUES (
       ${estudiante.nombre},
       ${estudiante.edad},
       ${estudiante.calificacion},
       ${estudiante.genero},
     )
   """.update.run
  }

  def insertAll(estudiante: List[Estudiante]): IO[List[Int]] = {
    Database.transactor.use { xa =>
      estudiante.traverse(t => insert(t).transact(xa))
    }
  }


  def obtenerTodos: ConnectionIO[List[(Int, String, Int)]] =
    sql"SELECT * FROM alumnos".query[(Int, String, Int)].to[List]
}
