package org.obiba.magma.datasource.mongodb.support

import java.time.{Instant, LocalDate, LocalDateTime}

import com.mongodb.casbah.commons.conversions.MongoConversionHelper
import org.bson.{BSON, Transformer}


object JavaDateTimeConversionHelpers extends JavaDateTimeSerializer with JavaDateTimeDeserializer {
  def apply() {
    log.debug("Registering Java 8 date time conversions.")
    super.register()
  }
}

trait JavaDateTimeSerializer extends MongoConversionHelper {

  private val transformer = new Transformer {
    def transform(o: AnyRef): AnyRef = o match {
      case ldt: LocalDateTime => ldt.toString
      case ld: LocalDate => ld.toString
      case i: Instant => i.toString
      case _ => o
    }
  }

  override def register() {
    log.debug("Hooking up Java time serializer.")
    BSON.addEncodingHook(classOf[LocalDateTime], transformer)
    BSON.addEncodingHook(classOf[LocalDate], transformer)
    BSON.addEncodingHook(classOf[Instant], transformer)
    super.register()
  }

  override def unregister() {
    log.debug("De-registering Java time serializer.")
    BSON.removeEncodingHooks(classOf[LocalDateTime])
    BSON.removeEncodingHooks(classOf[LocalDate])
    BSON.removeEncodingHooks(classOf[Instant])
    super.unregister()
  }
}

trait JavaDateTimeDeserializer extends MongoConversionHelper {

  override def register() {
    log.debug("Hooking up Java time deserializer.")
    BSON.addDecodingHook(classOf[LocalDateTime], new Transformer {
      override def transform(o: AnyRef): AnyRef = {
        log.debug(s"Deserialize $o to LocalDateTime")
        o match {
          case s: String => LocalDateTime.parse(s)
          case ldt: LocalDateTime => ldt
          case _ => o
        }
      }
    })
    BSON.addDecodingHook(classOf[LocalDate], new Transformer {
      override def transform(o: AnyRef): AnyRef = {
        log.debug(s"Deserialize $o to LocalDate")
        o match {
          case s: String => LocalDate.parse(s)
          case ld: LocalDate => ld
          case _ => o
        }
      }
    })
    BSON.addDecodingHook(classOf[Instant], new Transformer {
      override def transform(o: AnyRef): AnyRef = {
        log.debug(s"Deserialize $o to Instant")
        o match {
          case s: String => Instant.parse(s)
          case i: Instant => i
          case _ => o
        }
      }
    })
    super.register()
  }

  override def unregister() {
    log.debug("De-registering Java time serializer.")
    BSON.removeDecodingHooks(classOf[LocalDateTime])
    BSON.removeDecodingHooks(classOf[LocalDate])
    BSON.removeDecodingHooks(classOf[Instant])
    super.unregister()
  }
}

