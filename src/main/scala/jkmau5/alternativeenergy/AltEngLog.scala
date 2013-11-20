package jkmau5.alternativeenergy

import java.util.logging.{Level, Logger}
import cpw.mods.fml.relauncher.FMLRelaunchLog

/**
 * No description given
 *
 * @author jk-5
 */
object AltEngLog {

  private final val altEngLogger = Logger.getLogger("AlternativeEnergy")
  this.altEngLogger.setParent(FMLRelaunchLog.log.getLogger)

  def log(level: Level, format: String, data: Any*) = this.altEngLogger.log(level, format.format(data: _*))
  def log(level: Level, ex: Throwable, format: String, data: AnyRef*) = this.altEngLogger.log(level, format.format(data: _*), ex)
  def severe(format: String, data: Any*) = this.log(Level.SEVERE, format, data)
  def severe(ex: Throwable, format: String, data: Any*) = this.log(Level.SEVERE, ex, format, data)
  def warning(format: String, data: Any*) = this.log(Level.WARNING, format, data)
  def warning(ex: Throwable, format: String, data: Any*) = this.log(Level.WARNING, ex, format, data)
  def info(format: String, data: Any*) = this.log(Level.INFO, format, data)
  def fine(format: String, data: Any*) = this.log(Level.FINE, format, data)
  def finer(format: String, data: Any*) = this.log(Level.FINER, format, data)
  def finest(format: String, data: Any*) = this.log(Level.FINEST, format, data)
  @inline def getLogger = this.altEngLogger
}
