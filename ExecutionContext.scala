object ExecutionContext{

  val ec = scala.concurrent.ExecutionContext.Implicits.global

  def now() = new java.util.Date()

  println(now)

  // ExecutionContextにThread.sleepしてprintlnするだけのRunnableを渡してみる
  ec.execute(new Runnable {
    def run(): Unit = {
      Thread.sleep(3000)
      println("現在時刻は" + now)
    }
  })

  println(now)

  // 実行結果
  //  scala> ExecutionContext
  //  ExecutionContext
  //  Sun Apr 29 12:20:35 JST 2018
  //  Sun Apr 29 12:20:35 JST 2018
  //  res0: ExecutionContext.type = ExecutionContext$@7e04139e
  //
  //  scala> 現在時刻はSun Apr 29 12:20:38 JST 2018
  // -> ExecutionContextに渡したRunnableは非同期に実行されていることがわかる
  // ExectionContextは一定数を最大数とするスレッドプールを持っており、空いているスレッドを利用してRunnableを効率的に処理してくれる」
}