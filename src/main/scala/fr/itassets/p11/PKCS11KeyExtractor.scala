package fr.itassets.p11

import java.security.{KeyPair, KeyPairGenerator, Security}

import iaik.pkcs.pkcs11.objects.{RSAPrivateKey, RSAPublicKey}
import iaik.pkcs.pkcs11.wrapper.PKCS11Constants
import iaik.pkcs.pkcs11._
import org.bouncycastle.jce.provider.BouncyCastleProvider
import scopt.OptionParser

import scala.collection.mutable

case class PKCS11KeyExtractorConfig(pkcs11Lib: String = "", slotId: Long = 0, slotPassword: String = "", verbose: Boolean = false, test: Boolean = false, force: Boolean = false)

object PKCS11KeyExtractor extends App {

  // Registering the BouncyCastle provider
  Security.addProvider(new BouncyCastleProvider())

  val parser = new OptionParser[PKCS11KeyExtractorConfig]("PKCS11KeyExtractor") {
    head("PKCS11KeyExtractor", "0.9")
    // Global Options
    opt[String]('l', "library") required() valueName ("\"PKCS#11 Library Path\"") action { (x, c) => c.copy(pkcs11Lib = x) } text ("PKCS#11 Module Library Path (required)")
    opt[Long]('s', "slot") required() valueName ("\"PKCS#11 Slot ID\"") action { (x, c) => c.copy(slotId = x) } validate { x => if (x >= 0) success else failure("Slot ID cannot be negative") } text ("PKCS#11 Slot ID (required)")
    opt[String]('p', "password") required() valueName("\"PKCS#11 Slot Password\"") action { (x, c) => c.copy(slotPassword = x) } text("PKCS#11 Slot Password (required)")
    opt[Unit]('v', "verbose") optional() action { (_, c) => c.copy(verbose = true) } text("Verbose Mode")
    opt[Unit]('t', "test") optional() action { (_, c) => c.copy(test = true) } text("Test Mode")
    opt[Unit]('f', "force") optional() action { (_, c) => c.copy(force = true) } text("Disregard the CKA_EXTRACTABLE attribute")
  }

  // Parsing arguments
  parser.parse(args, PKCS11KeyExtractorConfig()) match {
    case Some(config) => {

      println("[*] PKCS#11 Key Extractor - Release 0.9")

      // Loading p11 module
      println(s"[*] Registering PKCS#11 Module '${config.pkcs11Lib}'")
      val module = registerModule(config.pkcs11Lib)

      // Opening session on specified slot with specified slot password
      println(s"[*] Opening session on slot '${config.slotId}'")
      val session = openTokenSession(module, config.slotId, config.slotPassword, config.verbose)

      // If test mode is enabled, generating test key pairs
      if (config.test) {
        println(s"[*] Generating test unextractable RSA 2048 bits keypair")
        generateRSATestKeyPair(session, 2048, false)
        println(s"[*] Generating test extractable RSA 2048 bits keypair")
        generateRSATestKeyPair(session, 2048, true)
      }

      // Searching for private RSA key with CKA_EXTRACTABLE set to TRUE
      val privateKeys = searchExtractableRSAPrivateKeys(session, config.force)

      if (privateKeys.size > 0) {

        if (config.force)
          println(s"[*] Found ${privateKeys.size} RSA private key(s)")
        else
          println(s"[*] Found ${privateKeys.size} RSA private key(s) with CKA_EXTRACTABLE set to TRUE")

        // Generating a software RSA 2048 bits wrap / unwrap key pair
        println(s"[*] Generating a wrapping software RSA keypair (2048 bits)")
        val extractKeyPair = generateRSAKeypair(2048)

        // Importing the public key into the token in memory
        println(s"[*] Importing the wrapping public key into the token memory (CKA_TOKEN: FALSE)")
        val wrappingKey = importWrappingKey(session, extractKeyPair.getPublic.asInstanceOf[java.security.interfaces.RSAPublicKey])

        privateKeys.foreach{ key =>
          println(s"[*] Extracting RSA Private Key (CKA_EXTRACTABLE: ${key.getExtractable.getBooleanValue}) with modulus '${key.getModulus.getByteArrayValue.map("%02x".format(_)).mkString(":").toUpperCase}'")
          val wrappedKey = try {
            session.wrapKey(Mechanism.get(PKCS11Constants.CKM_RSA_PKCS), wrappingKey, key)
          } catch {
            case e: Exception => println(s"[!] Error extracting private key: '${e.getMessage}'")
          }
        }

      } else {
        if (config.force)
          println(s"[*] No RSA private key")
        else
          println(s"[*] No RSA private key with CKA_EXTRACTABLE attribute set to TRUE")
      }

      // Closing session on the specified slot
      println(s"[*] Closing session on slot '${config.slotId}'")
      closeTokenSession(session)

      // Unregistering p11 module
      println(s"[*] Unregistering PKCS#11 Module '${config.pkcs11Lib}'")
      unregisterModule(module)
    }
    case None =>
  }

  private def generateRSATestKeyPair(session: Session, keyLength: Long, exportable: Boolean): Unit = {
    val rsaPublicKeyTemplate = new RSAPublicKey()
    val rsaPrivateKeyTemplate = new RSAPrivateKey()
    val publicExponentBytes = Array[Byte](0x01, 0x00, 0x01) // 2^6 + 1
    val mechanism = Mechanism.get(PKCS11Constants.CKM_RSA_PKCS_KEY_PAIR_GEN)

    // Set the general attributes for the public key
    rsaPublicKeyTemplate.getToken.setBooleanValue(true)
    rsaPublicKeyTemplate.getModulusBits.setLongValue(keyLength)
    rsaPublicKeyTemplate.getPublicExponent.setByteArrayValue(publicExponentBytes)
    rsaPublicKeyTemplate.getPrivate.setBooleanValue(false)
    rsaPublicKeyTemplate.getVerify.setBooleanValue(true)
    rsaPublicKeyTemplate.getEncrypt.setBooleanValue(false)
    rsaPublicKeyTemplate.getWrap().setBooleanValue(false)
    rsaPublicKeyTemplate.getModifiable.setBooleanValue(false)

    // Set the general attributes for the private key
    rsaPrivateKeyTemplate.getSensitive.setBooleanValue(true)
    rsaPrivateKeyTemplate.getToken.setBooleanValue(true)
    rsaPrivateKeyTemplate.getPrivate.setBooleanValue(true)
    rsaPrivateKeyTemplate.getSign.setBooleanValue(true)
    rsaPrivateKeyTemplate.getDecrypt.setBooleanValue(false)
    rsaPrivateKeyTemplate.getUnwrap.setBooleanValue(false)
    rsaPrivateKeyTemplate.getExtractable.setBooleanValue(exportable)
    rsaPrivateKeyTemplate.getModifiable.setBooleanValue(false)

    session.generateKeyPair(mechanism, rsaPublicKeyTemplate, rsaPrivateKeyTemplate)

  }

  private def importWrappingKey(session: Session, publicKey: java.security.interfaces.RSAPublicKey): RSAPublicKey = {
    val publicKeyTemplate = new RSAPublicKey()
    val modulus = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(publicKey.getModulus)
    publicKeyTemplate.getModulus.setByteArrayValue(modulus)
    val publicExponent = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(publicKey.getPublicExponent)
    publicKeyTemplate.getPublicExponent.setByteArrayValue(publicExponent)
    publicKeyTemplate.getToken.setBooleanValue(false)
    publicKeyTemplate.getWrap.setValue(true)
    publicKeyTemplate.getPrivate.setValue(false)
    session.createObject(publicKeyTemplate).asInstanceOf[RSAPublicKey]
  }

  private def searchExtractableRSAPrivateKeys(session: Session, force: Boolean): mutable.ArrayBuffer[RSAPrivateKey] = {
    val privateKeys = mutable.ArrayBuffer.empty[RSAPrivateKey]
    val privateKeyTemplate = new RSAPrivateKey()
    privateKeyTemplate.getPrivate.setBooleanValue(true)
    if (!force)
      privateKeyTemplate.getExtractable.setBooleanValue(true)
    session.findObjectsInit(privateKeyTemplate)
    def iterate: Unit = {
      while(true) {
        val key = session.findObjects(1)
        if (key.length > 0)
          privateKeys += key(0).asInstanceOf[RSAPrivateKey]
        else
          return
      }
    }
    iterate
    session.findObjectsFinal()
    privateKeys
  }

  private def generateRSAKeypair(size: Int): KeyPair = {
    val kpg = KeyPairGenerator.getInstance("RSA", "BC")
    kpg.initialize(size)
    kpg.generateKeyPair()
  }

  // Method to register a PKCS#11 module (i.e. a p11 library)
  private def registerModule(pkcs11Lib: String): Module = {
    // Instantiating Module
    val pkcs11InitArgs = new DefaultInitializeArgs()
    val pkcs11Module = Module.getInstance(pkcs11Lib)

    // Initializing Module
    pkcs11Module.initialize(pkcs11InitArgs)
    pkcs11Module
  }

  // Method to unregister a PKCS#11 Module (i.e a p11 library)
  private def unregisterModule(module: Module) = {
    module.finalize(null)
  }

  // Method to open a R/W session on a Token/Slot
  private def openTokenSession(module: Module, slotId: Long, slotPassword: String, verbose: Boolean): Session = {
    // Retrieving Slot
    val slot = module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT).filter(_.getSlotID == slotId).head

    // Retrieving Token
    val token = slot.getToken

    // Retrieving Token Infos
    val tokenInfo = token.getTokenInfo
    if (verbose)
      println(s"[?] Token Infos:\n${tokenInfo.toString}")

    // Opening session
    val session = token.openSession(Token.SessionType.SERIAL_SESSION, Token.SessionReadWriteBehavior.RW_SESSION, null, null)
    // Log in in if necessary
    if (tokenInfo.isLoginRequired)
      session.login(Session.UserType.USER, slotPassword.toCharArray)
    session
  }

  // Method to close a session
  private def closeTokenSession(session: Session) = {
    session.logout()
    session.closeSession()
  }


}