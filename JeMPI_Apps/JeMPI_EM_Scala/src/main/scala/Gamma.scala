import Utils.{GAMMA_TAG_EQUAL, GAMMA_TAG_MISSING, GAMMA_TAG_NOT_EQUAL}

import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq
import scala.collection.parallel.immutable.ParVector

object Gamma {

  @tailrec
  def getGamma2(
      gamma: Map[String, Long],
      left: ArraySeq[String],
      right: ParVector[ArraySeq[String]]
  ): Map[String, Long] = {

    def innerLoop(
        left: ArraySeq[String],
        interactions: ParVector[ArraySeq[String]]
    ): Map[String, Long] = {

      def combineOp(
          m1: Map[String, Long],
          m2: Map[String, Long]
      ): Map[String, Long] = {
        m1 ++ m2.map { case (k: String, v: Long) =>
          k -> (v + m1.getOrElse(k, 0L))
        }
      }

      def sequenceOp(m1: Map[String, Long], t: String): Map[String, Long] = {
        m1 ++ Map(t -> (1L + m1.getOrElse(t, 0L)))
      }

      val gamma: ParVector[String] =
        interactions.map(right => gammaKey(left, right))
      gamma.aggregate(Map[String, Long]())(sequenceOp, combineOp)
    }

    if (right.isEmpty) {
      gamma
    } else {
      getGamma2(
        gamma ++ innerLoop(left, right)
          .map { case (k: String, v: Long) =>
            k -> (v + gamma.getOrElse(k, 0L))
          },
        right.head,
        right.tail
      )
    }
  }

  @tailrec
  def getGamma1(
      gamma: Map[String, Long],
      left: ArraySeq[String],
      right: ParVector[ArraySeq[String]]
  ): Map[String, Long] = {

    def innerLoop(
        left: ArraySeq[String],
        interactions: Vector[ArraySeq[String]]
    ): Map[String, Long] = {

      val gamma: Vector[String] =
        interactions.map(right => gammaKey(left, right))
      gamma.groupMapReduce(k => k)(_ => 1L)(_ + _)
    }

    if (right.isEmpty) {
      gamma
    } else {
      getGamma1(
        gamma ++ innerLoop(left, right.toVector).map {
          case (k: String, v: Long) => k -> (v + gamma.getOrElse(k, 0L))
        },
        right.head,
        right.tail
      )
    }
  }

  private def gammaKey(
      left: ArraySeq[String],
      right: ArraySeq[String]
  ): String = {
    val key = (left zip right).map(tuple => {
      val l = tuple._1
      val r = tuple._2
      if (l.isEmpty || r.isEmpty) {
        GAMMA_TAG_MISSING
      } else if (l.equals(r)) {
        GAMMA_TAG_EQUAL
      } else {
        GAMMA_TAG_NOT_EQUAL
      }
    })
    key.mkString("<", ",", ">")
  }

}
