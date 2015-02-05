// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.headless
package lang

import org.scalatest, scalatest.Assertions
import org.nlogo.{ api, agent, core }
import org.nlogo.core.{CompilerException, Femto, Model, View}
import CompilerException.{RuntimeErrorAtCompileTimePrefix => runtimePrefix}
import org.nlogo.nvm.CompilerInterface
import org.nlogo.headless.test.{RunMode, NormalMode, TestMode, CompileError,
                                Success, Result, Reporter, Command, AbstractFixture, RuntimeError, StackTrace}

trait FixtureSuite extends scalatest.fixture.FunSuite {
  type FixtureParam = Fixture
  override def withFixture(test: OneArgTest) =
    Fixture.withFixture(test.name) { fixture =>
      withFixture(test.toNoArgTest(fixture))
    }
}

object Fixture {
  def withFixture[T](name: String)(fn: Fixture => T) = {
    val fixture = new Fixture(name)
    try fn(fixture)
    finally fixture.workspace.dispose()
  }
}

class Fixture(name: String) extends AbstractFixture {

  import Assertions._

  // many individual tests expect this to exist - ST 7/31/13
  new java.io.File("tmp").mkdir()

  val workspace = HeadlessWorkspace.newInstance
  workspace.silent = true

  // the default error handler just spits something to stdout or stderr or somewhere.
  // we want to fail hard. - ST 7/21/10
  workspace.importerErrorHandler =
    new agent.ImporterJ.ErrorHandler() {
      def showError(title: String, errorDetails: String, fatalError: Boolean): Boolean =
        throw new IllegalStateException(
          s"$title / $errorDetails  / $fatalError")
    }

  // to get the test name into the stack traces on JobThread - ST 1/26/11, 8/7/13
  def owner(kind: core.AgentKind = core.AgentKind.Observer) =
    new api.SimpleJobOwner(name, workspace.world.mainRNG, kind)

  val compiler: CompilerInterface =
    Femto.scalaSingleton("org.nlogo.compile.Compiler")

  def defaultView = View.square(5)

  def declare(model: Model) = workspace.openModel(model)

  def readFromString(literal: String): AnyRef =
    compiler.utilities.readFromString(literal)

  // tempted to DRY runReporter and runCommand together since they're so similar, but refraining
  // since there are many little differences, too - ST 8/15/13

  def runReporter(reporter: Reporter, mode: TestMode = NormalMode) {
    require(workspace.modelOpened)
    try {
      workspace.clearLastLogoException()
      val wrappedReporter = mode match {
        case NormalMode =>
          reporter.reporter
        case RunMode    =>
          s"""runresult "${core.StringEscaper.escapeString(reporter.reporter)}""""
      }
      val compiled = workspace.compileReporter(wrappedReporter)
      val actualResult = workspace.runCompiledReporter(owner(), compiled)
      if(workspace.lastLogoException != null)
        throw workspace.lastLogoException
      reporter.result match {
        case Success(expectedResult) =>
          checkResult(mode, reporter.reporter, expectedResult, actualResult)
        case _ =>
          fail(s"""failed to cause runtime error: "${reporter.reporter}"""")
      }
    }
    catch catcher(s"$mode: reporter: $reporter", reporter.result)
  }

  def runCommand(command: Command, mode: TestMode = NormalMode) {
    require(workspace.modelOpened)
    try {
      workspace.clearLastLogoException()
      val wrappedCommand = mode match {
        case NormalMode =>
          command.command
        case RunMode    =>
          s"""run "${core.StringEscaper.escapeString(command.command)}""""
      }
      val compiled = workspace.compileCommands(wrappedCommand, command.kind)
      if (mode == NormalMode && command.result.isInstanceOf[CompileError])
        fail("no CompilerException occurred")
      workspace.runCompiledCommands(owner(command.kind), compiled)
      if(workspace.lastLogoException != null)
        throw workspace.lastLogoException
      if (!command.result.isInstanceOf[Success])
        fail(s"""failed to cause runtime error: "${command.command}"""")
    }
    catch catcher(s"command: $command", command.result)
  }

  // ConstantFolder makes this complicated, by turning some runtime errors into
  // compile-time errors.  Furthermore in RunMode the compile-time error again
  // becomes a runtime error, but with "Runtime error: " tacked onto the front.
  private def catcher(clue: String, result: Result): PartialFunction[Throwable, Unit] = {
    case ex @ (_: api.LogoException | _: CompilerException) =>
      withClue(clue) {
        result match {
          case CompileError(expected) =>
            assertResult(expected)(
              ex.getMessage.stripPrefix(runtimePrefix))
          case RuntimeError(expected) =>
            assertResult(expected)(
              ex.getMessage.stripPrefix(runtimePrefix))
          case StackTrace(expected) =>
            assertResult(expected)(workspace.lastErrorReport.stackTrace.get)
          case _ =>
            throw ex
        }
      }
  }

  // convenience methods for when you don't want to mess with constructing
  // Command, Reporter, and/or Result objects
  def testReporter(reporter: String, result: String) =
    runReporter(Reporter(reporter, Success(result)))
  def testCommand(command: String, result: Result = Success("")) =
    runCommand(Command(command, core.AgentKind.Observer, result))

  // more convenience
  def open(path: String) =
    workspace.open(path)
  def open(model: Model) =
    workspace.openModel(model)

}
