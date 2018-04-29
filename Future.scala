import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FutureSample {

  val s = "Hello"
  val f: Future[String] = Future {
    Thread.sleep(1000)
    s + " future!"
  }

  f.foreach { case s: String =>
    println(s)
  }

  println(f.isCompleted) // false

  Thread.sleep(5000) // Hello future!

  println(f.isCompleted) // true

  val f2: Future[String] = Future {
    Thread.sleep(1000)
    throw new RuntimeException("わざと失敗")
  }

  f2.failed.foreach { case e: Throwable =>
    println(e.getMessage)
  }

  println(f2.isCompleted) // false

  Thread.sleep(5000) // わざと失敗

  println(f2.isCompleted) // true
}