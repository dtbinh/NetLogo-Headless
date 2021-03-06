// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.api

import org.nlogo.core.{ DummyExtensionManager => CoreDummyExtensionManager, ErrorSource, Primitive }

// This isn't a singleton because in testing contexts it's sometimes useful
// to override a few methods. - ST 11/5/11

class DummyExtensionManager extends CoreDummyExtensionManager with ExtensionManager {
  override def storeObject(obj: AnyRef) { }
  override def retrieveObject: AnyRef = unsupported
  override def readExtensionObject(extname: String, typeName: String, value: String): ExtensionObject = unsupported
  override def readFromString(src: String): AnyRef = unsupported
  override def reset() = unsupported
  override def loadedExtensions = java.util.Collections.emptyList[ClassManager]
  override def resolvePathAsURL(path: String): String = unsupported
  override def dumpExtensions: String = unsupported
  override def dumpExtensionPrimitives(): String = unsupported
  override def addToLibraryPath(classManager: AnyRef, directory: String) = unsupported
  override def getFile(path: String) = unsupported
  override def getJarPaths = unsupported
  override def getExtensionNames = unsupported
  override def profilingEnabled = false
  private def unsupported = throw new UnsupportedOperationException
}
