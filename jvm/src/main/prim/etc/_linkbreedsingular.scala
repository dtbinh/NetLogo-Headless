// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim.etc

import org.nlogo.core.Nobody
import org.nlogo.nvm.{ Context, Reporter }

class _linkbreedsingular(breedName: String) extends Reporter {

  override def toString =
    super.toString + ":" + breedName

  override def report(context: Context): AnyRef = {
    val breed = world.getLinkBreed(breedName)
    val link = world.getLink(
      argEvalDouble(context, 0), argEvalDouble(context, 1), breed)
    if (link == null)
      Nobody
    else
      link
  }

  def getBreedName = breedName

}
