import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}

object FutureApply {

  val future: Future[String] = Future {
    Thread.sleep(3000)
    "3秒待って実行"
  }

  future.foreach(println)

  // 以下のように書き換えられる

  val promise: Promise[String] = Promise[String]
  val f: Future[String] = promise.future

  future.foreach(println)

  scala.concurrent.ExecutionContext.Implicits.global.execute(
    new Runnable {
      def run = {
        Thread.sleep(3000)
        promise.success("promiseが成功したよ")
      }
    }
  )

  // でもこれPromiseとか書かなきゃいけなくて面倒
  // そこでFuture.applyの登場
  // Future.apply は CPUコア数分しか同時に処理しない（大事）

  (1 to 30).foreach(_ => Future {
    Thread.sleep(2000)
    def now() = new java.util.Date()
    println(now)
  })

  // 実行結果
  // Sun Apr 29 12:51:00 JST 2018
  // Sun Apr 29 12:51:00 JST 2018
  // Sun Apr 29 12:51:00 JST 2018
  // Sun Apr 29 12:51:00 JST 2018
  // Sun Apr 29 12:51:02 JST 2018
  // Sun Apr 29 12:51:02 JST 2018
  // Sun Apr 29 12:51:02 JST 2018
  // Sun Apr 29 12:51:02 JST 2018
  // Sun Apr 29 12:51:04 JST 2018...
  // 4コアのマシンを使って動いている
}
