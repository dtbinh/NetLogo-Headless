// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim.etc

import org.nlogo.agent.{ Agent, AgentSet, Patch, Turtle }
import org.nlogo.core.{ AgentKind, I18N, Syntax }
import org.nlogo.nvm.{ ArgumentTypeException, Context, EngineException, Reporter }

class _breedon(breedName: String) extends Reporter {

  override def toString =
    super.toString + ":" + breedName

  override def report(context: Context): AgentSet =
    report_1(context, args(0).report(context))

  def report_1(context: Context, arg0: AnyRef): AgentSet = {
    val result = collection.mutable.ArrayBuffer[Agent]()
    val breed = world.getBreed(breedName)
    arg0 match {
      case turtle: Turtle =>
        if (turtle.id == -1)
          throw new EngineException(
            context, this, I18N.errors.getN(
              "org.nlogo.$common.thatAgentIsDead", turtle.classDisplayName))
        val iter = turtle.getPatchHere.turtlesHere.iterator
        while(iter.hasNext) {
          val t = iter.next()
          if (t.getBreed eq breed)
            result += t
        }
      case patch: Patch =>
        val iter = patch.turtlesHere.iterator
        while(iter.hasNext) {
          val t = iter.next()
          if (t.getBreed eq breed)
            result += t
        }
      case sourceSet: AgentSet =>
        sourceSet.kind match {
          case AgentKind.Turtle =>
            val iter = sourceSet.iterator
            while(iter.hasNext) {
              val iter2 = iter.next().asInstanceOf[Turtle].getPatchHere.turtlesHere.iterator
              while(iter2.hasNext) {
                val t = iter2.next()
                if (t.getBreed eq breed)
                  result += t
              }
            }
          case AgentKind.Patch =>
            val iter = sourceSet.iterator
            while(iter.hasNext) {
              val iter2 = iter.next().asInstanceOf[Patch].turtlesHere.iterator
              while(iter2.hasNext) {
                val t = iter2.next()
                if (t.getBreed eq breed)
                  result += t
              }
            }
          case _ =>
            throw new ArgumentTypeException(
              context, this, 0,
              Syntax.TurtlesetType | Syntax.PatchsetType,
              arg0)
        }
      case _ =>
        throw new ArgumentTypeException(
          context, this, 0,
          Syntax.TurtleType | Syntax.PatchType | Syntax.TurtlesetType | Syntax.PatchsetType,
          arg0)
    }
    AgentSet.fromArray(AgentKind.Turtle, result.toArray)
  }

  def report_2(context: Context, sourceSet: AgentSet): AgentSet = {
    val result = collection.mutable.ArrayBuffer[Agent]()
    val breed = world.getBreed(breedName)
    sourceSet.kind match {
      case AgentKind.Turtle =>
        val iter = sourceSet.iterator
        while(iter.hasNext) {
          val iter2 = iter.next().asInstanceOf[Turtle].getPatchHere.turtlesHere.iterator
          while(iter2.hasNext) {
            val t = iter2.next()
            if (t.getBreed eq breed)
              result += t
          }
        }
      case AgentKind.Patch =>
        val iter = sourceSet.iterator
        while(iter.hasNext) {
          val iter2 = iter.next().asInstanceOf[Patch].turtlesHere.iterator
          while(iter2.hasNext) {
            val t = iter2.next()
            if (t.getBreed eq breed)
              result += t
          }
        }
      case _ =>
        throw new ArgumentTypeException(
          context, this, 0,
          Syntax.TurtlesetType | Syntax.PatchsetType,
          sourceSet)
    }
    AgentSet.fromArray(AgentKind.Turtle, result.toArray)
  }

  def report_3(context: Context, agent: Agent): AgentSet = {
    val result = collection.mutable.ArrayBuffer[Agent]()
    val breed = world.getBreed(breedName)
    agent match {
      case turtle: Turtle =>
        if (turtle.id == -1)
          throw new EngineException(
            context, this, I18N.errors.getN(
              "org.nlogo.$common.thatAgentIsDead", turtle.classDisplayName))
        val iter = turtle.getPatchHere.turtlesHere.iterator
        while(iter.hasNext) {
          val t = iter.next()
          if (t.getBreed eq breed)
            result += t
        }
      case patch: Patch =>
        val iter = patch.turtlesHere.iterator
        while(iter.hasNext) {
          val t = iter.next()
          if (t.getBreed eq breed)
            result += t
        }
      case _ =>
        throw new ArgumentTypeException(
          context, this, 0,
          Syntax.TurtleType | Syntax.PatchType,
          agent)
    }
    AgentSet.fromArray(AgentKind.Turtle, result.toArray)
  }

  def report_4(context: Context, turtle: Turtle): AgentSet = {
    val result = collection.mutable.ArrayBuffer[Agent]()
    val breed = world.getBreed(breedName)
    if (turtle.id == -1)
      throw new EngineException(
        context, this, I18N.errors.getN(
          "org.nlogo.$common.thatAgentIsDead", turtle.classDisplayName))
    val iter = turtle.getPatchHere.turtlesHere.iterator
    while(iter.hasNext) {
      val t = iter.next()
      if (t.getBreed eq breed)
        result += t
    }
    AgentSet.fromArray(AgentKind.Turtle, result.toArray)
  }

  def report_5(context: Context, patch: Patch): AgentSet = {
    val result = collection.mutable.ArrayBuffer[Agent]()
    val breed = world.getBreed(breedName)
    val iter = patch.turtlesHere.iterator
    while(iter.hasNext) {
      val t = iter.next()
      if (t.getBreed eq breed)
        result += t
    }
    AgentSet.fromArray(AgentKind.Turtle, result.toArray)
  }

  //MethodRipper died if I just made it a val above - F.D. (10/3/13), ST 4/22/14
  def getBreedName: String = breedName
}
