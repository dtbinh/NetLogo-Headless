// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim.etc

import org.nlogo.agent.AgentSet
import org.nlogo.core.{ Syntax }
import org.nlogo.nvm.{ Context, Reporter }

class _nolinks extends Reporter {
  override def syntax =
    Syntax.reporterSyntax(
      ret = Syntax.LinksetType)
  override def report(context: Context): AgentSet =
    world.noLinks
}
