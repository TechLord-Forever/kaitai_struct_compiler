package io.kaitai.struct.format

import io.kaitai.struct.exprlang.{Expressions, Ast}

trait ProcessExpr {
  def outputType: String
}

case object ProcessZlib extends ProcessExpr {
  override def outputType: String = null
}
case object ProcessHexStrToInt extends ProcessExpr {
  override def outputType: String = "u4"
}
case class ProcessXor(key: Ast.expr) extends ProcessExpr {
  override def outputType: String = null
}
case class ProcessRotate(left: Boolean, key: Ast.expr) extends ProcessExpr {
  override def outputType: String = null
}

object ProcessExpr {
  private val ReXor = "^xor\\(\\s*(.*?)\\s*\\)$".r
  private val ReRotate = "^ro(l|r)\\(\\s*(.*?)\\s*\\)$".r

  def fromStr(s: Option[String]): Option[ProcessExpr] = {
    s match {
      case None =>
        None
      case Some(op) =>
        Some(op match {
          case "zlib" =>
            ProcessZlib
          case "hexstr_to_int" =>
            ProcessHexStrToInt
          case ReXor(arg) =>
            ProcessXor(Expressions.parse(arg))
          case ReRotate(dir, arg) =>
            ProcessRotate(dir == "l", Expressions.parse(arg))
          case _ =>
            throw new RuntimeException(s"Invalid process: '$s'")
        })
    }
  }
}
