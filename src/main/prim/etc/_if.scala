// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim.etc

import org.nlogo.core.{ Syntax, SyntaxJ }
import org.nlogo.nvm, nvm.{ Command, Context }

class _if extends Command with nvm.CustomAssembled {

  override def syntax =
    SyntaxJ.commandSyntax(
      Array(Syntax.BooleanType, Syntax.CommandBlockType))

  override def toString =
    super.toString + ":+" + offset

  def assemble(a: nvm.AssemblerAssistant) {
    a.add(this)
    a.block()
    a.resume()
  }

  override def perform(context: Context) {
    perform_1(context, argEvalBooleanValue(context, 0))
  }

  def perform_1(context: Context, arg0: Boolean) {
    context.ip = if (arg0) next else offset
  }

}
