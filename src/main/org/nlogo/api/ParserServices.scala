// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.api

trait ParserServices {
  def isConstant(s: String): Boolean
  def readFromString(s: String): AnyRef
  def readNumberFromString(source: String): AnyRef
  def isReporter(s: String): Boolean
  def isValidIdentifier(s: String): Boolean
  def getTokenAtPosition(source: String, position: Int): Token
  def findProcedurePositions(source: String): Map[String, (String, Int, Int, Int)]
}
