// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim.etc

import org.nlogo.core.{ Syntax, SyntaxOld }
import org.nlogo.nvm.{ Command, Context }

class _updatemonitor extends Command {
  override def syntax =
    SyntaxOld.commandSyntax(
      Array(Syntax.WildcardType), "O---", true)
  override def perform(context: Context) {
    workspace.updateMonitor(
      context.job.owner,
      args(0).report(context))
    context.ip = next
  }
}
