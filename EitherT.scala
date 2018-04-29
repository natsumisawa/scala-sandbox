import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scalaz.std.scalaFuture._

sealed trait Error

final case class UserNotFound(userId: Long) extends Error

final case class ConnectionError(message: String) extends Error

final case class User(id: Long, name: String)

object UserRepository {

  // NOTE: EitherTのありがたみがわかるまでの道のりを示す

  // 指定したuserIdのfollower一覧を取得するメソッド
  def followers(userId: Long): Either[Error, List[User]] = ???

  // 相互followかどうか
  def isFriends0(user1: Long, user2: Long): Either[Error, Boolean] = {
    for {
      a <- followers(user1).right
      b <- followers(user2).right
    } yield a.exists(_.id == user2) && b.exists(_.id == user1)
  }

  // followersはデータベースへのIOアクションなので非同期にしたい
  def followersFuture(userid: Long): Future[Either[Error, List[User]]] = ???

  def isFriends1(user1: Long, user2: Long): Future[Either[Error, Boolean]] = {
    for {
      a <- followersFuture(user1)
      b <- followersFuture(user2)
    } yield for {
      x <- a
      y <- b
    } yield x.exists(_.id == user1) && y.exists(_.id == user2)
  }

  // for式ネストしてるし最悪・・・
  // NOTE: ただ、crawlerの実装でFuture.failedでエラーハンドリングしてSQL頑張ってかけばだいたい凌げた

  // ScalaZ EitherTだ〜
  import scalaz.EitherT

  def followersEither(userid: Long): EitherT[Future, Error, List[User]] = ???

  def isFriends2(user1: Long, user2: Long): EitherT[Future, Error, Boolean] = {
    for {
      a <- followersEither(user1)
      b <- followersEither(user2)
    } yield a.exists(_.id == user2) && b.exists(_.id == user1)
  }
  // 綺麗 :clapping:
}